package com.example.sit.UI.Activity;

import static com.example.sit.Const.RequestErrorTag;
import static com.example.sit.Const.SuccessCode;
import static com.example.sit.Const.UserTag;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sit.R;
import com.example.sit.Service.Api;
import com.example.sit.Utils.ActivityCollectorUtil;
import com.example.sit.Utils.BarStatusUtil;
import com.example.sit.Utils.JsonUtil;
import com.example.sit.Utils.NotifyUtil;
import com.example.sit.Utils.RegistryUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

// App的用户注册页面
public class RegistryActivity extends AppCompatActivity {
    public EditText userNameText; // 用户名
    public EditText userPwdText; // 密码
    public EditText userRePwdText; // 确认密码
    public String regUserName;
    public String regPwd;
    public String regRePwd;
    public Button backToLoginBtn;
    public Button confirmToRegBtn;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registry);
        // 将Activity添加到Collector中,方便结束调用
        ActivityCollectorUtil.getInstance().addActivity(this);
        BarStatusUtil.setBarBackgroundColor(this, R.color.light_blue);

        confirmToRegBtn = findViewById(R.id.reg_confirm);
        backToLoginBtn = findViewById(R.id.back_to_login);
        // 点击返回时触发的监听事件
        backToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistryActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // 点击注册时触发的监听事件
        confirmToRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userNameText = findViewById(R.id.reg_username);
                userPwdText = findViewById(R.id.reg_pwd);
                userRePwdText = findViewById(R.id.reg_re_pwd);
                regUserName = userNameText.getText().toString();
                regPwd = userPwdText.getText().toString();
                regRePwd = userRePwdText.getText().toString();
                RegistryUtil regUtil = new RegistryUtil(regUserName, regPwd, regRePwd);
                boolean b = regUtil.handleInputUserMessage(RegistryActivity.this);
                if (b) {
                    /** regUserMap参数列表 key-value
                     account: regUserName(用户名)
                     password: regPwd(密码)
                     nickname: nickName(随机用户昵称)
                     avatar_url: http://sit.icedll.xyz/storage/app_icon.png(默认头像)
                     */
                    Map<String, String> regUserMap = regUtil.getRegUserMessage(regUserName, regPwd);
                    Log.e(UserTag, regUserMap.toString());
                    // 调用注册接口
                    initRequest(regUserMap);
                }
            }
        });
    }

    private void initRequest(Map<String, String> regUserMap) {
        Api.register(regUserMap, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(RequestErrorTag, e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String res = response.body().string();
                processResponse(res);
                response.body().close();
            }
        });
    }

    private void processResponse(String res) {
        int code = JsonUtil.getStatusCode(res);
        String msg = JsonUtil.getMessage(res);

        Looper.prepare();

        if (code == SuccessCode) {
            // 创建通知提醒用户,并且跳转页面
            JSONObject data = JsonUtil.getDataJson(res);
            String id = JsonUtil.getJson(data.toString()).toString();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().createAccount(id.trim(), regPwd.trim());
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        Log.e("RegistryActivity ---> ", "注册失败， " + e.getErrorCode() + ", " + e.getMessage());
                    }
                }
            }).start();
            NotifyUtil.createNotify(RegistryActivity.this, msg + ",2s后跳转至登录");
            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                Intent intent = new Intent(RegistryActivity.this, LoginActivity.class);
                startActivity(intent);
                }
            };
            timer.schedule(timerTask, 2000);
        } else {
            Log.e(RequestErrorTag, msg);
            NotifyUtil.createNotify(RegistryActivity.this, msg);
        }
        Looper.loop();
    }
}