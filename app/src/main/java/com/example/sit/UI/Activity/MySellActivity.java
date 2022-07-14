package com.example.sit.UI.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sit.Const;
import com.example.sit.Entity.Goods;
import com.example.sit.Entity.GoodsPhoto;
import com.example.sit.R;
import com.example.sit.Adapter.SellListBaseAdapter;
import com.example.sit.Service.Api;
import com.example.sit.Utils.ActivityCollectorUtil;
import com.example.sit.Utils.JsonUtil;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

// 我卖出的界面
public class MySellActivity extends AppCompatActivity {
    public ImageView backBtn; // 标题栏返回
    public ListView listView; // 列表
    SellListBaseAdapter adapter;
    MySellActivity activity = this;

    static List<Goods> goods = new ArrayList<>();
    static List<List<GoodsPhoto>> photos = new ArrayList<>();
    String TAG = "MySellActivity ---> ";
    ArrayList<HashMap<String, Object>> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_sell);
        // 将Activity添加到Collector中,方便结束调用
        ActivityCollectorUtil.getInstance().addActivity(this);
        // 加载布局后隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }

        backBtn = findViewById(R.id.sell_title_back);
        listView = findViewById(R.id.my_sell_list);

        initRequest();

        // TODO: 2022/4/2 根据用户的id查询出该用户卖出的商品,将信息作为map添加到list中
        /** 返回参数列表
            id: 卖出的商品的id
            photo_url: 商品预览图
            introduction: 商品的信息
            price: 商品价格(int)
         */
        adapter = new SellListBaseAdapter(this, list);
        listView.setAdapter(adapter);

        RefreshLayout refreshLayout = findViewById(R.id.sell);
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

        // 返回个人中心的监听
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MySellActivity.this, MainActivity.class);
                intent.putExtra("page", 3);
                startActivity(intent);
            }
        });


    }

    private void initRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String res = null;
                try {
                    res = Api.getSellGoods1().body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int code = JsonUtil.getStatusCode(res);
                String msg = JsonUtil.getMessage(res);
                JSONArray data = JsonUtil.getDataArray(res);
                if (code == Const.SuccessCode) {
                    Log.v(TAG, msg);
                    Gson gson = new Gson();
                    goods = gson.fromJson(data.toString(), new TypeToken<List<Goods>>(){}.getType());
                    Log.v(TAG, goods.toString());
                }
                if (goods != null) {
                    for (Goods good : goods) {
                        String goods_id = String.valueOf(good.getId());
                        String res1 = null;
                        try {
                            res1 = Api.getPhoto(goods_id).body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        int code1 = JsonUtil.getStatusCode(res1);
                        String msg1 = JsonUtil.getMessage(res1);
                        JSONArray data1 = JsonUtil.getDataArray(res1);
                        if (code1 == Const.SuccessCode) {
                            Log.v(TAG, msg1);
                            Gson gson = new Gson();
                            photos.add(gson.fromJson(data1.toString(), new TypeToken<List<GoodsPhoto>>(){}.getType()));
                            Log.v(TAG, photos.toString());
                        }
                    }
                    initData();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }

//                Api.getSellGoods(new Callback() {
//                    @Override
//                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                        Log.e(TAG, e.getMessage());
//                    }
//
//                    @Override
//                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                        String res = response.body().string();
//                        int code = JsonUtil.getStatusCode(res);
//                        String msg = JsonUtil.getMessage(res);
//                        JSONArray data = JsonUtil.getDataArray(res);
//                        if (code == Const.SuccessCode) {
//                            Log.v(TAG, msg);
//                            Gson gson = new Gson();
//                            goods = gson.fromJson(data.toString(), new TypeToken<List<Goods>>(){}.getType());
//                            Log.v(TAG, goods.toString());
//                        }
//                        if (goods != null) {
//                            for (Goods good : goods) {
//                                String goods_id = String.valueOf(good.getId());
//                                String res1 = Api.getPhoto(goods_id).body().string();
//                                int code1 = JsonUtil.getStatusCode(res1);
//                                String msg1 = JsonUtil.getMessage(res1);
//                                JSONArray data1 = JsonUtil.getDataArray(res1);
//                                if (code1 == Const.SuccessCode) {
//                                    Log.v(TAG, msg1);
//                                    Gson gson = new Gson();
//                                    photos.add(gson.fromJson(data1.toString(), new TypeToken<List<GoodsPhoto>>(){}.getType()));
//                                    Log.v(TAG, photos.toString());
//                                }
//                            }
//                            initData();
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    adapter.notifyDataSetChanged();
//                                }
//                            });
//                        }
//                    }
//                });
            }
        }).start();

    }

    private void initData() {
        int size = goods.size();

        int id;
        String photo_url;
        String introduction;
        int price;
        for (int i = 0; i < size; i++) {
            HashMap<String, Object> map = new HashMap<>();
            id = goods.get(i).getId();
            photo_url = photos.get(i).get(0).getPhoto_url();
            introduction = goods.get(i).getIntroduction();
            price = goods.get(i).getPrice();
            map.put("id", id);
            map.put("photo_url", Const.goodsUrl + photo_url);
            map.put("introduction", introduction.length() > 15 ? introduction.substring(0,15) + "..." : introduction);
            map.put("price", price);
            list.add(map);
        }

    }
}