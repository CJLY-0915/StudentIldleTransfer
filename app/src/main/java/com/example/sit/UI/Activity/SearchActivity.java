package com.example.sit.UI.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.sit.Adapter.HomeAdapter;
import com.example.sit.Const;
import com.example.sit.Entity.Goods;
import com.example.sit.Entity.GoodsPhoto;
import com.example.sit.R;
import com.example.sit.Service.Api;
import com.example.sit.UI.Fragment.RecommendFragment;
import com.example.sit.Utils.JsonUtil;
import com.example.sit.Utils.NotifyUtil;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private int space = 5;
    private String TAG = "SearchActivity";
    Button searchBtn;
    ImageButton backBtn;
    EditText searchEt;
    Context context;
    static List<Goods> goods = new ArrayList<>();
    static List<List<GoodsPhoto>> photo = new ArrayList<>();
    List<String> images = new ArrayList<>();
    List<String> titles = new ArrayList<>();
    List<String> prices = new ArrayList<>();
    List<Integer> goods_id = new ArrayList<>();
    HomeAdapter homeAdapter;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchBtn = findViewById(R.id.searchBtn);
        backBtn = findViewById(R.id.backBtn);
        searchEt = findViewById(R.id.searchEt);
        context = this;
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String entry = searchEt.getText().toString();
                images.clear();
                titles.clear();
                prices.clear();
                goods_id.clear();
                initRequest(entry);

                if (images == null) {
                    NotifyUtil.createNotify(SearchActivity.this, "搜索不到该商品");
                }
                RecyclerView mRV = findViewById(R.id.search_recycler);

                homeAdapter = new HomeAdapter(images, titles, prices, goods_id, context);
                mRV.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                mRV.addItemDecoration(new space_item(space));
                homeAdapter.setHasStableIds(true);
                mRV.setAdapter(homeAdapter);
                homeAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initRequest(String entry) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String res = Api.search(entry);
                int code = JsonUtil.getStatusCode(res);
                String msg = JsonUtil.getMessage(res);
                JSONArray data;
                if (code == Const.SuccessCode) {
                    Log.v(TAG, msg);
                    data = JsonUtil.getDataArray(res);
                    Gson gson = new Gson();
                    goods = gson.fromJson(data.toString(), new TypeToken<List<Goods>>() {}.getType());
                    Log.v(TAG, goods.toString());
                }
                if (goods != null) {
                    for (Goods good : goods) {
                        String goods_id = String.valueOf(good.getId());
                        try {
                            String res1 = Api.getPhoto(goods_id).body().string();
                            String msg1 = JsonUtil.getMessage(res1);
                            JSONArray data1 = JsonUtil.getDataArray(res1);

                            if (code == Const.SuccessCode) {
                                Log.v(TAG, msg1);
                                Gson gson = new Gson();
                                photo.add(gson.fromJson(data1.toString(), new TypeToken<List<GoodsPhoto>>() {
                                }.getType()));
                                Log.v(TAG, photo.toString());
                            }
                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage());
                        }
                    }
                }
                initData();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        homeAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    private void initData() {
        if (goods != null) {
            for (int i = 0; i < goods.size(); i++) {
                String title = goods.get(i).getIntroduction();
                titles.add(title.length() > 10 ? title.substring(0, 10) : title);
                prices.add(String.valueOf(goods.get(i).getPrice()));
                goods_id.add(goods.get(i).getId());
                String uri = photo.get(i).get(0).getPhoto_url();
                images.add(Const.goodsUrl + uri);
            }
        }

        Log.v(TAG, images.toString());
        Log.v(TAG, titles.toString());
        Log.v(TAG, goods_id.toString());
        Log.v(TAG, prices.toString());

    }

    private class space_item extends RecyclerView.ItemDecoration {
        private int space = 5;

        public space_item(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            outRect.bottom = space;
            outRect.top = space;
        }
    }
}