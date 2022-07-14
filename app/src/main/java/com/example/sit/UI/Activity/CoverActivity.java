package com.example.sit.UI.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.sit.Const;
import com.example.sit.Entity.User;
import com.example.sit.R;
import com.example.sit.Service.Api;
import com.example.sit.Utils.ActivityCollectorUtil;
import com.example.sit.Utils.BarStatusUtil;
import com.example.sit.Utils.JsonUtil;
import com.example.sit.Utils.LoginUtil;
import com.example.sit.Utils.NotifyUtil;
import com.example.sit.Utils.SPUtil;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseIM;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.provider.EaseUserProfileProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

// 进入App时的封面动画以及跳转到LoginActivity
public class CoverActivity extends AppCompatActivity {
    private View cover_bg;
    private LottieAnimationView cover_anim;
    public Context context;
    private String account;
    private String password;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 隐藏标题栏
        BarStatusUtil.setTitleBarHide(this);
        // 设置状态栏为默认白色
        BarStatusUtil.setBarBackgroundColor(CoverActivity.this,R.color.light_green);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cover);
        // 将Activity添加到Collector中,方便结束调用
        ActivityCollectorUtil.getInstance().addActivity(this);
        // 设置全屏显示动画
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 获取app加载封面的背景和动画
        cover_bg = findViewById(R.id.cover_bg);
        cover_anim = findViewById(R.id.cover_anim);

        // 让背景往上滑动,动画往下滑动
        cover_bg.animate().translationY(-2000).setDuration(4000).setStartDelay(5000);
        cover_anim.animate().translationY(1000).setDuration(4000).setStartDelay(5000);
//        initLogin();
        SPUtil spUtil = new SPUtil(this,"userSetting");

        boolean isLogin = spUtil.getState();
        account = spUtil.getAccount();
        password = spUtil.getPassword();

        if (isLogin) {
            Log.v("account ---> ", account);
            Log.v("password ---> ", password);
            if (account.equals("") && password.equals("")) {
                initLogin();
            } else {
                initMain();
            }
        } else {
            spUtil.setState();
            initLogin();
        }
    }

    private void initLogin() {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
            // 动画结束后跳转到Login页面
            Intent intent = new Intent(CoverActivity.this, LoginActivity.class);
            startActivity(intent);
            CoverActivity.this.finish();
            }
        };
        timer.schedule(timerTask,2*1000);
    }

    private void initMain() {
        Api.getUser(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(Const.RequestErrorTag, e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String res = response.body().string();
                int code = JsonUtil.getStatusCode(res);
                String msg = JsonUtil.getMessage(res);
                Looper.prepare();
                if (code == 401 && msg.equals("Please login")) {
                    reLogin();
                } else {
                    Gson gson = new Gson();
                    User user = gson.fromJson(JsonUtil.getDataJson(res).toString(), User.class);
                    Timer timer = new Timer();
                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(CoverActivity.this, MainActivity.class);
                            intent.putExtra("user", user);
                            startActivity(intent);
                            CoverActivity.this.finish();
                        }
                    };
                    timer.schedule(timerTask,2*1000);
                    EMClient.getInstance().login(String.valueOf(user.getId()).trim(), password.trim(), new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            EMClient.getInstance().chatManager().loadAllConversations();
                            Log.v("LoginActivity ---> ", "聊天系统登陆成功");
                        }

                        @Override
                        public void onError(int code, String error) {
                            if (code == 200) {
                                EMClient.getInstance().chatManager().loadAllConversations();
                            }
                            Log.e("LoginActivity ---> ", "聊天系统" + code + ", " + error);
                        }

                        @Override
                        public void onProgress(int progress, String status) {

                        }
                    });
                    String finalNickname = user.getNickname();
                    String finalAvatar = Const.avatarUrl + user.getAvatar_url();
//                    EaseIM.getInstance().setUserProvider(new EaseUserProfileProvider() {
//                        @Override
//                        public EaseUser getUser(String username) {
//                            //根据username，从数据库中或者内存中取出之前保存的用户信息，如从数据库中取出的用户对象为DemoUserBean
//                            EaseUser user = new EaseUser(username);
//                            //设置用户昵称
//                            user.setNickname(finalNickname);
//                            //设置头像地址
//                            user.setAvatar(finalAvatar);
//                            //最后返回构建的EaseUser对象
//                            return user;
//                        }
//                    });
                }
                Looper.loop();
                response.body().close();
            }
        }
        );
    }

    private void reLogin() {
        Map<String, String> user = new HashMap<>();
        user.put("account", account);
        user.put("password", password);

        Api.login(user, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String res = response.body().string();
                int code1 = JsonUtil.getStatusCode(res);
                String msg1 = JsonUtil.getMessage(res);
                JSONObject data = JsonUtil.getDataJson(res);
                JSONObject userRes = null;
                Looper.prepare();
                if (code1 == Const.SuccessCode) {
                    int user_id = 0;
                    String avatar = null;
                    String nickname = null;
                    String token = "";
                    try {
                        userRes = data.getJSONObject("user");
                        user_id = userRes.getInt("id");
                        token = data.getString("access_token");

                        Log.d("Request ", token);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Gson gson = new Gson();
                    User user = gson.fromJson(userRes.toString(), User.class);
                    nickname = user.getNickname();
                    avatar = Const.avatarUrl + user.getAvatar_url();
                    LoginUtil.setCurrentUser(token, user_id);
                    LoginUtil.setAccount(user.getAccount());
                    NotifyUtil.createNotify(CoverActivity.this, msg1);

                    EMClient.getInstance().login(String.valueOf(user_id), password.trim(), new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            EMClient.getInstance().chatManager().loadAllConversations();
                            Log.v("LoginActivity ---> ", "聊天系统登陆成功");
                        }

                        @Override
                        public void onError(int code, String error) {
                            if (code == 200) {
                                EMClient.getInstance().chatManager().loadAllConversations();
                            }
                            Log.e("LoginActivity ---> ", "聊天系统" + code + ", " + error);
                        }

                        @Override
                        public void onProgress(int progress, String status) {

                        }
                    });
                    String finalNickname = nickname;
                    String finalAvatar = avatar;
//                    EaseIM.getInstance().setUserProvider(new EaseUserProfileProvider() {
//                        @Override
//                        public EaseUser getUser(String username) {
//                            //根据username，从数据库中或者内存中取出之前保存的用户信息，如从数据库中取出的用户对象为DemoUserBean
//                            EaseUser user = new EaseUser(username);
//                            //设置用户昵称
//                            user.setNickname(finalNickname);
//                            //设置头像地址
//                            user.setAvatar(finalAvatar);
//                            //最后返回构建的EaseUser对象
//                            return user;
//                        }
//                    });

                    Timer timer = new Timer();
                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(CoverActivity.this, MainActivity.class);
                            intent.putExtra("user", user);
                            startActivity(intent);
                            CoverActivity.this.finish();
                        }
                    };
                    timer.schedule(timerTask,2*1000);
                } else {
                    Log.d("Request ", msg1);
                    NotifyUtil.createNotify(CoverActivity.this, msg1);

                    Timer timer = new Timer();
                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(CoverActivity.this, LoginActivity.class);
                            startActivity(intent);
                            CoverActivity.this.finish();
                        }
                    };
                    timer.schedule(timerTask,2*1000);
                }

                Looper.loop();
                response.body().close();
            }
        });
    }
}