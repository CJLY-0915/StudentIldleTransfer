package com.example.sit.UI.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sit.Adapter.ReleaseListBaseAdapter;
import com.example.sit.Const;
import com.example.sit.Dialogs.ImagePickDialog;
import com.example.sit.Entity.Goods;
import com.example.sit.Entity.GoodsPhoto;
import com.example.sit.R;
import com.example.sit.Service.Api;
import com.example.sit.Utils.ActivityCollectorUtil;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyReleaseActivity extends AppCompatActivity {
    public ImageView backBtn; // 返回个人中心
    public ListView listView; // 发布的商品列表
    public ImageView productImage; // 商品预览图
    public String imgUrl = null; // 更换的预览图地址

    static List<Goods> goods = new ArrayList<>();
    static List<List<GoodsPhoto>> photos = new ArrayList<>();
    String TAG = "MyReleaseActivity ---> ";
    ArrayList<HashMap<String, Object>> list = new ArrayList<>();
    ReleaseListBaseAdapter adapter;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK){
            if (ImagePickDialog.getSDKVersionNumber() >= 19){
                ImagePickDialog.handleSelectImage(data,MyReleaseActivity.this,productImage);
                imgUrl = ImagePickDialog.getGoalImageUrl();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_release);
        // 将Activity添加到Collector中,方便结束调用
        ActivityCollectorUtil.getInstance().addActivity(this);
        // 加载布局后隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }
        backBtn = findViewById(R.id.release_title_back);
        listView = findViewById(R.id.release_listView);

        // TODO: 2022/4/3 进入我的发布页面,查询该用户发布的所有商品,封装成map添加到list中
        /**
         goods_id: 发布商品的id
         photo_url: 商品预览图地址
         introduction: 商品的信息
         price: 商品价格
         */
        initRequest();

        adapter = new ReleaseListBaseAdapter(this, list);

        listView.setAdapter(adapter);

        RefreshLayout refreshLayout = findViewById(R.id.release);
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                list.clear();
                initRequest();
                adapter.notifyDataSetInvalidated();
                refreshlayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
            }
        });

        // 返回个人中心监听
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyReleaseActivity.this, MainActivity.class);
                intent.putExtra("page", 3);
                startActivity(intent);
            }
        });
    }

    private void initRequest() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
                Api.getUserGoods(new Callback() {
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
                            goods = gson.fromJson(data.toString(), new TypeToken<List<Goods>>(){}.getType());
                            Log.v(TAG, goods.toString());
                        }
                        response.body().close();
                        if (goods != null) {
                            for (Goods good : goods) {
                                String goods_id = String.valueOf(good.getId());
                                String res1 = Api.getPhoto(goods_id).body().string();
                                int code1 = JsonUtil.getStatusCode(res1);
                                String msg1 = JsonUtil.getMessage(res1);
                                JSONArray data1 = JsonUtil.getDataArray(res1);
                                if (code1 == Const.SuccessCode) {
//                                    Log.v(TAG, msg1);
                                    Gson gson = new Gson();
                                    photos.add(gson.fromJson(data1.toString(), new TypeToken<List<GoodsPhoto>>(){}.getType()));
//                                    Log.v(TAG, photos.toString());
                                }
                            }
                            Log.v(TAG, goods.toString());
                            Log.v(TAG, photos.toString());
                            initData(goods, photos);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                });
//            }
//        }).start();
    }

    private void initData(List<Goods> goods, List<List<GoodsPhoto>> photos) {
        int size = goods.size();

        for (int i = 0; i < size; i++) {
            HashMap<String, Object> map = new HashMap<>();
            int id = goods.get(i).getId();
            String photo_url = photos.get(i).get(0).getPhoto_url();
            String introduction = goods.get(i).getIntroduction();
            int price = goods.get(i).getPrice();
            System.out.println(id + "  " + photo_url + "  " + introduction + "  " + price);
            map.put("goods_id", id);
            map.put("photo_url", Const.goodsUrl + photo_url);
            map.put("introduction", introduction.length() > 15 ? introduction.substring(0,15) + "..." : introduction);
            map.put("price", price);
            System.out.println(map);
            list.add(map);
        }
        Log.v(TAG, list.toString());
    }
}