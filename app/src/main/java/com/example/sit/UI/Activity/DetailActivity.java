package com.example.sit.UI.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sit.Adapter.DetailAdapter;
import com.example.sit.Const;
import com.example.sit.Entity.Goods;
import com.example.sit.Entity.GoodsPhoto;
import com.example.sit.Entity.User;
import com.example.sit.R;
import com.example.sit.Service.Api;
import com.example.sit.UI.Fragment.RecommendFragment;
import com.example.sit.Utils.JsonUtil;
import com.example.sit.Utils.NotifyUtil;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.constants.EaseConstant;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DetailActivity extends AppCompatActivity {

    private String TAG = "DetailActivity ---> ";
    ImageButton backBtn;

    RecyclerView imagesRV;
    ImageView avatarImgV;
    Button followBtn, contentBtn, buyBtn;
    TextView nickTV, universityTV, priceTV, introTV;

    static int goods_id = 0;
    static Goods goods;
    static User user;
    static List<GoodsPhoto> photoList;
    List<String> images = new ArrayList<>();
    static Map<String, Object> data = new HashMap<>();
    DetailAdapter detailAdapter;
    Context context = this;


    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        backBtn = findViewById(R.id.detail_back_btn);
        avatarImgV = findViewById(R.id.detail_avatar_img);
        nickTV = findViewById(R.id.detail_nick);
        universityTV = findViewById(R.id.detail_university);
        priceTV = findViewById(R.id.detail_price);
        introTV = findViewById(R.id.detail_intro);
        imagesRV = findViewById(R.id.detail_img);
        followBtn = findViewById(R.id.detail_follow);
        contentBtn = findViewById(R.id.detail_contact_seller);
        buyBtn = findViewById(R.id.detail_buy);
        Intent intent = getIntent();
        goods_id = intent.getIntExtra("goods_id", 1);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });
        // 初始化网络请求
        initRequest();

        detailAdapter = new DetailAdapter(this, images);
        imagesRV.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        detailAdapter.setHasStableIds(true);
        imagesRV.setAdapter(detailAdapter);
        detailAdapter.notifyDataSetChanged();

        // 关注按钮点击事件
        followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        String res = null;
                        try {
                            res = Api.follow(String.valueOf(user.getId())).body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.v(TAG, res);
                        String msg = JsonUtil.getMessage(res);
                        int code = JsonUtil.getStatusCode(res);
                        Log.v(TAG, msg);
                        if (code == Const.SuccessCode) {
                            NotifyUtil.createNotify(DetailActivity.this, msg);
                        }
                        Looper.loop();
                    }
                }).start();
            }
        });

        // 联系商家按钮点击事件
        contentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, ChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EMMessage.ChatType.Chat);
                intent.putExtra(EaseConstant.EXTRA_CONVERSATION_ID, String.valueOf(user.getId()));
                startActivity(intent);
            }
        });

        // 购买按钮点击事件
        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, BuyActivity.class);
                intent.putExtra("price", (String)String.valueOf(data.get("price")));
                intent.putExtra("intro", (String)data.get("intro"));
                intent.putExtra("image", (String)images.get(0));
                intent.putExtra("user_id", user.getId());
                intent.putExtra("good_id", goods_id);
                startActivity(intent);
            }
        });
    }

    private void initRequest() {
        user = null;
        goods = null;
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 获取商品信息
                String res = null;
                try {
                    res = Api.getGoods1(String.valueOf(goods_id)).body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int code = JsonUtil.getStatusCode(res);
                String msg = JsonUtil.getMessage(res);
                JSONObject datas = JsonUtil.getDataJson(res);
                Gson gson = new Gson();
                if (code == Const.SuccessCode) {
                    Log.v(TAG, msg);
                    goods = gson.fromJson(datas.toString(), Goods.class);
                    Log.v(TAG, goods.toString());
                }
                if (goods != null) {
                    // 获取发布该商品的用户信息
                    String user_id = String.valueOf(goods.getUser_id());
                    System.out.println(user_id);
                    String res1 = null;
                    try {
                        res1 = Api.getOtherUser1(user_id).body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    int code1 = JsonUtil.getStatusCode(res1);
                    Log.v(TAG, String.valueOf(code1));
                    String msg1 = JsonUtil.getMessage(res1);
                    JSONObject data1 = JsonUtil.getDataJson(res1);

                    if (code1 == Const.SuccessCode) {
                        Log.v(TAG, msg1);
                        user = gson.fromJson(data1.toString(), User.class);
                        Log.v(TAG, user.toString());
                    }

                    // 获取该商品的图片
                    String photo_res = null;
                    try {
                        photo_res = Api.getPhoto(String.valueOf(goods_id)).body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    int code2 = JsonUtil.getStatusCode(photo_res);
                    String msg2 = JsonUtil.getMessage(photo_res);
                    JSONArray photo_data = JsonUtil.getDataArray(photo_res);
                    if (code2 == Const.SuccessCode) {
                        Log.v(TAG, msg2);
                        photoList = gson.fromJson(photo_data.toString(), new TypeToken<List<GoodsPhoto>>() {}.getType());
                        Log.v(TAG, photoList.toString());
                    }
                    // 初始化数据
                    initData();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            detailAdapter.notifyDataSetChanged();
                            initView();
                        }
                    });
                }
            }
        }).start();

    }

    private void initData() {
        String avatar_url = user.getAvatar_url();
        String nick = user.getNickname();
        String university = user.getUniversity();
        int price = goods.getPrice();
        String intro = goods.getIntroduction();
        for (GoodsPhoto photo : photoList) {
            String uri = photo.getPhoto_url();
            images.add(Const.goodsUrl + uri);
        }
        data.clear();
        data.put("avatar", Const.avatarUrl + avatar_url);
        data.put("nick", nick);
        data.put("university", university);
        data.put("price", price);
        data.put("intro", intro);
        Log.v(TAG, data.toString());
        Log.v(TAG, images.toString());
    }

    private void initView() {
        if (data != null) {
            // 设置头像
            Picasso.with(context)
                    .load((String) data.get("avatar"))
                    .into(avatarImgV);
            // 设置昵称、价格、学校、介绍
            nickTV.setText((String) data.get("nick"));
            universityTV.setText((data.get("university") == null ? "" : "发布于"+data.get("university")));
            priceTV.setText(String.valueOf(data.get("price")));
            introTV.setText((String) data.get("intro"));
        }
    }

}