package com.example.sit.UI.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sit.Adapter.ListViewBaseAdapter;
import com.example.sit.Const;
import com.example.sit.Dialogs.AddAddressPopDialog;
import com.example.sit.Entity.Address;
import com.example.sit.R;
import com.example.sit.Service.Api;
import com.example.sit.Utils.ActivityCollectorUtil;
import com.example.sit.Utils.AddressManageDataUtil;
import com.example.sit.Utils.JsonUtil;
import com.example.sit.Utils.LoginUtil;
import com.example.sit.Utils.NotifyUtil;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

// 地址管理页面
public class AddressManageActivity extends AppCompatActivity {
    public ImageView backBtn; // 返回个人中心
    public ListView addressListView; // 地址信息列表
    public Button addNewAddressBtn; // 添加收货地址
    static List<Address> list = new ArrayList<>();
    List<Map<String, Object>> newList = new ArrayList<>();
    String TAG = "AddressManagementActivity ---> ";
    ListViewBaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_manage);
        // 将Activity添加到Collector中,方便结束调用
        ActivityCollectorUtil.getInstance().addActivity(this);
        // 加载布局后隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }
        backBtn = findViewById(R.id.address_title_back);
        addressListView = findViewById(R.id.address_manage_list);
        addNewAddressBtn = findViewById(R.id.address_addNewAddress);

        // 获取后端数据
        initRequest();

        // 自定义的列表适配器,继承了BaseAdapter,将视图和参数绑定
        // TODO: 2022/3/20 进入地址管理页面,根据id查询当前用户的所有收货地址,返回值list作为参数传递给适配器
        // int accountId = LoginUtil.getAccountId(); // 当前用户的id
        /** 返回参数列表
         address_id: 地址id
         address_userName: 联系人姓名
         address_phone: 联系人电话
         address_fullAddress: 详细收货地址
         */

        adapter = new ListViewBaseAdapter(this, newList);
        addressListView.setAdapter(adapter);

        // 编辑地址信息回调
        adapter.setItemOnEditClickListener(new ListViewBaseAdapter.ItemOnClickListener() {
            @Override
            public void itemOnClickListener(View view, int i, Map<String,String> newMessageMap) {

                String name = newMessageMap.get("name");
                String number = newMessageMap.get("number");
                String address = newMessageMap.get("address");
                // 重新设置数据,用于列表回显渲染
                Address addr = list.get(i);
                addr.setName(name);
                addr.setNumber(number);
                addr.setAddress(address);
                int id = addr.getId();
                newMessageMap.put("address_id", String.valueOf(id));

                /** 参数列表
                 id: 地址的id
                 name: 修改的联系人姓名
                 number: 修改的联系人电话
                 address: 修改的详细收货地址
                 */
                String res = Api.editAddress(newMessageMap);
                int code = JsonUtil.getStatusCode(res);
                if (code == Const.SuccessCode){
                    NotifyUtil.createNotify(AddressManageActivity.this,"修改成功");
                    // 渲染列表
                    adapter.notifyDataSetInvalidated();
                }
            }
        });

        // 删除地址信息回调
        adapter.setItemOnDeleteClickListener(new ListViewBaseAdapter.ItemOnDeleteClickListener() {
            @Override
            public void itemOnDeleteClickListener(View view, int i1, int i2) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddressManageActivity.this);
                builder.setTitle("提示");
                builder.setMessage("您确定要删除该地址信息吗?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int builderI) {
                        String res = Api.deleteAddress(String.valueOf(i1));
                        int code = JsonUtil.getStatusCode(res);
                        if (code == Const.SuccessCode){
                            NotifyUtil.createNotify(AddressManageActivity.this,"删除成功");
                            list.remove(i2);
                            // 渲染列表
                            adapter.notifyDataSetInvalidated();
                        }
                    }
                });
                builder.setNegativeButton("取消", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        // 添加地址信息监听
        addNewAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 初始化填写信息的弹窗
                AddAddressPopDialog popWindow = new AddAddressPopDialog(AddressManageActivity.this);
                popWindow.showAtLocation(view, Gravity.TOP,0,-10);
                // 点击添加的回调监听
                popWindow.setItemOnEditClickListener(new AddAddressPopDialog.ItemOnClickListener() {
                    @Override
                    public void itemOnClickListener(View view, Map<String, String> addMessageMap) {
                        System.out.println("添加的地址信息:"+addMessageMap);
                        // TODO: 2022/3/26 根据用户id,将新的地址信息添加到数据库,参数已经封装在addMessageMap中
                        int accountId = LoginUtil.getAccountId(); // 当前用户的id
                        /** 参数列表
                         number: 新添加的地址信息中,联系人的电话
                         name: 新添加的地址信息中,联系人的姓名
                         address: 新添加的地址信息中,详细收货地址
                         */
                        String res = Api.addAddress(addMessageMap);
                        int code = JsonUtil.getStatusCode(res);
                        if (code == Const.SuccessCode){
                            NotifyUtil.createNotify(AddressManageActivity.this,"添加成功");
                            // TODO: 2022/3/26 写入数据库成功后返回此次新添加地址的id,返回的id替换掉下面的 '999' 即可
                            String address_id = null;
                            JSONObject data = JsonUtil.getDataJson(res);
                            System.out.println(data.toString());
                            try {
                                address_id = data.getString("id");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            // 测试添加新数据
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("address_id",address_id);
                            map.put("address_userName",addMessageMap.get("name"));
                            map.put("address_phone",addMessageMap.get("number"));
                            map.put("address_fullAddress",addMessageMap.get("address"));
                            newList.add(map);
                            adapter.notifyDataSetInvalidated();
                            popWindow.dismiss();
                        }
                    }
                });
            }
        });

        // 返回个人中心监听
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddressManageActivity.this, MainActivity.class);
                intent.putExtra("page", 3);
                startActivity(intent);
            }
        });

        RefreshLayout refreshLayout = findViewById(R.id.address);
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                newList.clear();
                initRequest();
                adapter.notifyDataSetChanged();
                refreshlayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
            }
        });
    }

    private void initRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Api.getAddress(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        String res = response.body().string();
                        int code = JsonUtil.getStatusCode(res);
                        String msg = JsonUtil.getMessage(res);
                        JSONArray data = JsonUtil.getDataArray(res);
                        if (code == Const.SuccessCode) {
                            Log.v(TAG, msg);
                            Gson gson = new Gson();
                            list = gson.fromJson(data.toString(), new TypeToken<List<Address>>(){}.getType());

                            Log.v(TAG, list.toString());
                            initData();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                });
            }
        }).start();
    }

    private void initData() {
        for (Address address : list) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("address_id",address.getId());
            map.put("address_userName",address.getName());
            map.put("address_phone",address.getNumber());
            map.put("address_fullAddress",address.getAddress());
            newList.add(map);
        }
    }
}