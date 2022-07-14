package com.example.sit.UI.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sit.Adapter.FollowListBaseAdapter;
import com.example.sit.Const;
import com.example.sit.Entity.User;
import com.example.sit.R;
import com.example.sit.Service.Api;
import com.example.sit.UI.Fragment.PersonalCenterActivity;
import com.example.sit.Utils.ActivityCollectorUtil;
import com.example.sit.Utils.JsonUtil;
import com.example.sit.Utils.LoginUtil;
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
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

// 我的关注界面
public class FollowActivity extends AppCompatActivity {
    public ImageView backBtn; // 返回个人中心
    public ListView followList; // 关注列表
    static List<User> followUser = new ArrayList<>();
    List<Map<String, Object>> list = new ArrayList<>();
    String TAG = "FollowActivity ---> ";
    FollowListBaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.follow);
        // 将Activity添加到Collector中,方便结束调用
        ActivityCollectorUtil.getInstance().addActivity(this);
        // 加载布局后隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }

        backBtn = findViewById(R.id.follow_title_back);
        followList = findViewById(R.id.follow_list);

        initRequest();

        adapter = new FollowListBaseAdapter(this, list);
        // TODO: 2022/3/26 根据用户id查询关注的用户数据,封装成map添加到list中
//        int accountId = LoginUtil.getAccountId();
        /** 返回参数列表
         avatar_url: 关注用户的头像url
         nickname: 关注用户的昵称
         introduction: 关注用户的个人简介
         */
        followList.setAdapter(adapter);

        RefreshLayout refreshLayout = findViewById(R.id.follow);
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                list.clear();
                initRequest();
                adapter.notifyDataSetChanged();
                refreshlayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
            }
        });

        // 返回个人中心监听
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addressIntent = new Intent(FollowActivity.this,
                        MainActivity.class);
                addressIntent.putExtra("page", 3);
                startActivity(addressIntent);
            }
        });
    }

    private void initRequest() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Api.getFollow(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.e("FollowActivity ---> ", e.getMessage());
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
                            followUser = gson.fromJson(data.toString(), new TypeToken<List<User>>(){}.getType());
                            Log.v(TAG, followUser.toString());
                        }
                        initData();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                                followList.setAdapter(adapter);
                            }
                        });
                    }
                });
            }
        }).start();
    }

    private void initData() {
        for (User user : followUser) {
            Map<String,Object> map = new HashMap<>();
            map.put("avatar_url",user.getAvatar_url());
            map.put("nickname",user.getNickname());
            map.put("introduction",user.getIntroduction());
            list.add(map);
        }
    }
}