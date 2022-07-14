package com.example.sit.UI.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sit.Const;
import com.example.sit.Dialogs.RegardingDialog;
import com.example.sit.Entity.User;
import com.example.sit.R;
import com.example.sit.Service.Api;
import com.example.sit.Utils.ActivityCollectorUtil;
import com.example.sit.Utils.BarStatusUtil;
import com.example.sit.Utils.GlobalExitUtil;
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
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

// App的登录页面
public class LoginActivity extends AppCompatActivity {
    public EditText userNameText;
    public EditText userPasswordText;
    public String userName;
    public String userPassword;
    public Button loginBtn;
    public Button registryBtn;
    public CheckBox checkBtn;
    public boolean accessFlag = false;
    public int flagN = 0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        // 将Activity添加到Collector中,方便结束调用
        ActivityCollectorUtil.getInstance().addActivity(this);
        // 设置状态栏背景颜色
        BarStatusUtil.setBarBackgroundColor(LoginActivity.this, R.color.light_green);

        loginBtn = findViewById(R.id.login_btn);
        registryBtn = findViewById(R.id.registry_btn);
        checkBtn = findViewById(R.id.login_check_btn);
        SPUtil spUtil = new SPUtil(this,"userSetting");
        // 绑定登录的监听事件
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userNameText = findViewById(R.id.userNameText);
                userPasswordText = findViewById(R.id.userPasswordText);

                userName = userNameText.getText().toString();
                userPassword = userPasswordText.getText().toString();
                boolean isLegal = LoginUtil.handleInputUserMessage(userName, userPassword, LoginActivity.this);
                if (isLegal) {
                    // 输入的用户信息合法,并且勾选了同意隐私协议,就将数据封装成Map集合传递给后端
                    if (accessFlag) {

                        /** userMessageMap参数列表  key-value
                         account: userName(用户名)
                         password: userPassword(密码)
                         */
                        spUtil.setAccount(userName);
                        spUtil.setPassword(userPassword);
                        Map<String, String> userMessageMap = LoginUtil.getUserMessageMap(userName, userPassword);
                        // 调用登录接口
                        initRequest(userMessageMap);
                    } else {
                        NotifyUtil.createNotify(LoginActivity.this, "请先勾选同意隐私协议");
                    }
                }
            }
        });

        // 绑定注册的监听事件
        registryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistryActivity.class);
                startActivity(intent);
            }
        });
        // 绑定勾选框的监听事件
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flagN++;
                if (flagN % 2 != 0) {
                    accessFlag = !accessFlag;
                } else
                    accessFlag = false;
            }
        });
    }

    // 创建子线程进行网络异步请求
    private void initRequest(Map<String, String> userMessageMap) {
        Log.d("UserMessage", userMessageMap.toString());
        Api.login(userMessageMap, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("Request Error ", e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String res = response.body().string();
                processRequest(res);
                response.body().close();
            }
        });
    }

    // 将子线程切换到主线程
    private void processRequest(String res) {
        int code = JsonUtil.getStatusCode(res);
        JSONObject data = JsonUtil.getDataJson(res);
        String msg = JsonUtil.getMessage(res);
        JSONObject userRes = null;
        Looper.prepare();
        int user_id = 0;
        String nickname = null;
        String avatar = null;
        if (code == Const.SuccessCode) {
            String token = "";
            try {
                userRes = data.getJSONObject("user");
                user_id = userRes.getInt("id");
                token = data.getString("access_token");
                Log.d("Request ", token);
                Log.d("Request ", msg);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Gson gson = new Gson();
            User user = gson.fromJson(userRes.toString(), User.class);
            nickname = user.getNickname();
            avatar = Const.avatarUrl + user.getAvatar_url();
            Log.e("user", user.toString());
            LoginUtil.setCurrentUser(token, user_id);
            NotifyUtil.createNotify(LoginActivity.this, msg);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("user", user);
            intent.putExtra("page", 1);
            startActivity(intent);
            LoginActivity.this.finish();
        } else {
            Log.d("Request ", msg);
            NotifyUtil.createNotify(LoginActivity.this, msg);
        }
        EMClient.getInstance().login(String.valueOf(user_id).trim(), userPassword.trim(), new EMCallBack() {
            @Override
            public void onSuccess() {
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.v("LoginActivity ---> ", "聊天系统登陆成功");
            }

            @Override
            public void onError(int code, String error) {
                Log.e("LoginActivity ---> ", "聊天系统" + code + ", " + error);
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
        String finalNickname = nickname;
        String finalAvatar = avatar;
//        EaseIM.getInstance().setUserProvider(new EaseUserProfileProvider() {
//            @Override
//            public EaseUser getUser(String username) {
//                //根据username，从数据库中或者内存中取出之前保存的用户信息，如从数据库中取出的用户对象为DemoUserBean
//                EaseUser user = new EaseUser(username);
//                //设置用户昵称
//                user.setNickname(finalNickname);
//                //设置头像地址
//                user.setAvatar(finalAvatar);
//                //最后返回构建的EaseUser对象
//                return user;
//            }
//        });
        Looper.loop();
    }

    // 设置右上角菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option, menu);
        return true;
    }

    // 获取右上角菜单选项对应的id,设置菜单选项对应的操作
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.regarding:
                this.clickToRegarding(item);
                break;
            case R.id.exit:
                this.clickToExit(item);
                break;
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }

    // 点击右上角的关于我们
    public void clickToRegarding(MenuItem item) {
        RegardingDialog.createReDialog(this);
    }

    // 点击右上角的退出
    public void clickToExit(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage("您确定要退出程序吗?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 调用Collector中的方法结束程序
                ActivityCollectorUtil.getInstance().finishAll();
            }
        });
        builder.setNegativeButton("取消", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // 连按两次返回键全局退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            GlobalExitUtil.Exit(keyCode, event, this);
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}