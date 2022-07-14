package com.example.sit.UI.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sit.Const;
import com.example.sit.Dialogs.ImagePickDialog;
import com.example.sit.Entity.User;
import com.example.sit.R;
import com.example.sit.Service.Api;
import com.example.sit.Utils.ActivityCollectorUtil;
import com.example.sit.Utils.EditMessageUtil;
import com.example.sit.Utils.JsonUtil;
import com.example.sit.Utils.LoginUtil;
import com.example.sit.Utils.NotifyUtil;
import com.example.sit.Utils.SPUtil;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

// App编辑个人信息界面
public class EditPersonalMessageActivity extends AppCompatActivity {

    private static String TAG = "EditPersonalMessageActivity ---> ";
    public ImageView imageBtn;  // 标题栏返回
    public ImageView headerImage; // 头像
    public Button editBtn;  // 编辑
    public Button logoutBtn; // 退出登录按钮
    public String editText1 = "编辑";
    public String editText2 = "保存";
    public int editStatus = 0; // 当前文本状态,0不可编辑,1可编辑
    public EditText userNameEditText; // 账号
    public EditText nickNameEditText; // 昵称
    public EditText sexEditText; // 性别
    public EditText birthEditText; // 生日
    public EditText desEditText; // 简介
    public EditText schoolEditText; // 学校
    User user = MainActivity.getUser();

    // 回调,返回从相册中选取的图片路径
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK) {
            // Api19以上,也就是安卓4.4版本以上处理返回的数据方式不同
            if (ImagePickDialog.getSDKVersionNumber() >= 19) {
                headerImage = findViewById(R.id.header_image);
                ImagePickDialog.handleSelectImage(data, EditPersonalMessageActivity.this, headerImage);
                String res = Api.setAvatar(ImagePickDialog.getGoalImageUrl());
                int code = JsonUtil.getStatusCode(res);
                System.out.println("返回状态码为"+code);
                String msg = JsonUtil.getMessage(res);
                Log.v(TAG, msg);
                if (code == Const.SuccessCode) {
                    JSONObject data1 = JsonUtil.getDataJson(res);
                    String str;
                    try {
                        str = JsonUtil.getJson(data1.toString()).getString("filePath");
                        user.setAvatar_url(str);
                        Log.v("file path ---> ", str);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_personal_message);
        // 将Activity添加到Collector中,方便结束调用
        ActivityCollectorUtil.getInstance().addActivity(this);
        // 加载布局后隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        imageBtn = findViewById(R.id.title_back);
        headerImage = findViewById(R.id.header_image);
        editBtn = findViewById(R.id.edit_or_save);
        logoutBtn = findViewById(R.id.user_logout);
        userNameEditText = findViewById(R.id.userNameEditText);
        nickNameEditText = findViewById(R.id.userNickNameEditText);
        sexEditText = findViewById(R.id.userSexEditText);
        birthEditText = findViewById(R.id.userBirthEditText);
        desEditText = findViewById(R.id.userDesEditText);
        schoolEditText = findViewById(R.id.userSchoolEditText);
        SPUtil spUtil = new SPUtil(this,"userSetting");

        initInfo();
//        // 获取当前的用户id,根据id查询用户信息
//        int userId = LoginUtil.getAccountId();


        // 返回个人中心的按键监听
        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditPersonalMessageActivity.this, MainActivity.class);
                intent.putExtra("page", 3);
                startActivity(intent);
            }
        });

        // 点击头像的按键监听
        headerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 调用系统相册
                ImagePickDialog.openAlbum(EditPersonalMessageActivity.this);
            }
        });

        // 退出登录的按键监听
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditPersonalMessageActivity.this);
                builder.setTitle("温馨提示");
                builder.setMessage("您确定要退出登录吗?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int id = LoginUtil.getAccountId();
                        String token = "";
                        LoginUtil.setCurrentUser(token, id);
                        spUtil.removeState();
                        EMClient.getInstance().logout(false, new EMCallBack() {
                            @Override
                            public void onSuccess() {
                                Log.v(TAG, "聊天系统退出登录成功");
                            }

                            @Override
                            public void onError(int code, String error) {
                                Log.e(TAG, "聊天系统退出登录失败" + code + ", " + error);
                            }

                            @Override
                            public void onProgress(int progress, String status) {

                            }
                        });
                        // 返回到登录界面
                        Intent intent = new Intent(EditPersonalMessageActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("取消", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        // 编辑按钮的按键监听
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentEditText = editBtn.getText().toString();
                // 进入编辑信息状态
                if (currentEditText.equals(editText1)) {
                    editStatus = 1;
                    editBtn.setText(editText2);
                }// 退出编辑,保存修改的信息
                else {
                    editStatus = 0;
                    editBtn.setText(editText1);
                }
                switch (editStatus) {
                    case 1:
                        // 设置文本框为可编辑状态
                        EditMessageUtil.setEditIsAble(nickNameEditText, sexEditText,
                                birthEditText, desEditText, schoolEditText);
                        break;
                    case 0:
//                        int currentUserId = LoginUtil.getAccountId();
                        Map newEditUserMessage = EditMessageUtil.setEditIsNotAble(nickNameEditText, sexEditText,
                                birthEditText, desEditText, schoolEditText);
                        /** newEditUserMessage中对应的 key-value
                         nickname: 昵称
                         sex: 性别(int)
                         birthday: 生日
                         introduction: 个人简介
                         university: 院校
                         */
                        String editRes = Api.editUserInfo(newEditUserMessage);
                        int code = JsonUtil.getStatusCode(editRes);
                        if(code == Const.SuccessCode) {
                            String msg = JsonUtil.getMessage(editRes);
                            user.setNickname(nickNameEditText.getText().toString());
                            NotifyUtil.createNotify(EditPersonalMessageActivity.this, msg);
                        }

                }
            }
        });
    }

    private void initInfo() {

        Intent intent = getIntent();
        User user = (User)intent.getSerializableExtra("Info");

        Log.d(Const.UserTag, user.toString());
        HashMap<String, Object> userMessageMap = new HashMap<>();
        userMessageMap.put("account", user.getAccount());
        Log.d("account ---> ", user.getAccount());
        userMessageMap.put("nickname", user.getNickname());
        Log.d("nickname ---> ", user.getNickname());
        userMessageMap.put("sex", user.getSex());
        userMessageMap.put("birthday", user.getBirthday());
        userMessageMap.put("introduction", user.getIntroduction());
        userMessageMap.put("university", user.getUniversity());
        userMessageMap.put("avatar_url", Const.avatarUrl + user.getAvatar_url());

        // 调用方法回显数据到页面上
        EditMessageUtil.showUserMessage(headerImage, userNameEditText, nickNameEditText, sexEditText,
                birthEditText, desEditText, schoolEditText, userMessageMap, this);
    }

    private void initRequest() {
        Api.getUser(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(Const.RequestErrorTag, e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String res = response.body().string();
                processRes(res);
                response.body().close();
            }
        });
    }

    private void processRes(String res) {
        int code = JsonUtil.getStatusCode(res);
        String msg = JsonUtil.getMessage(res);
        String data = JsonUtil.getDataJson(res).toString();
        Gson gson = new Gson();
        User user = gson.fromJson(data, User.class);
        Looper.prepare();
        Log.v(Const.RequestTag, user.toString());
        // 当前用户的用户名
        // String currentAccount = LoginUtil.getCurrentAccount();

        /** 返回参数列表
         nickname: 昵称
         sex: 性别(int)
         birthday: 生日
         introduction: 个人简介
         university: 院校
         avatar_url: 头像地址
         */
        if (code == Const.SuccessCode) {
            HashMap<String, Object> userMessageMap = new HashMap<>();
            userMessageMap.put("account", user.getAccount());
            userMessageMap.put("nickname", user.getNickname());
            userMessageMap.put("sex", user.getSex());
            userMessageMap.put("birthday", user.getBirthday());
            userMessageMap.put("introduction", user.getIntroduction());
            userMessageMap.put("university", user.getUniversity());
            userMessageMap.put("avatar_url", user.getAvatar_url());
            Log.v("UserInfo", msg);
            // 调用方法回显数据到页面上
            EditMessageUtil.showUserMessage(headerImage, userNameEditText, nickNameEditText, sexEditText,
                    birthEditText, desEditText, schoolEditText, userMessageMap, this);
        } else {
            Log.e(Const.RequestTag, msg);
            NotifyUtil.createNotify(EditPersonalMessageActivity.this, msg);
        }
        Looper.loop();
    }
}