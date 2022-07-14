package com.example.sit.Utils;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

// 登录界面的数据和逻辑处理工具类
public class LoginUtil {
    private static boolean flag = false;
    public static String account = "";
    public static String token = "";
    public static int id;

    // 获取用户输入的账号密码并判断是否合法
    public static boolean handleInputUserMessage(String userName, String userPassword, Context context){
        if (userName.equals("") || userPassword.equals("")){
            if (userName.equals("")){
                NotifyUtil.createNotify(context,"用户名不能为空");
            }else {
                NotifyUtil.createNotify(context,"密码不能为空");
            }
            return flag;
        }
        return !flag;
    }

    // 对于合法登录用户数据的处理,封装成集合,给后端调用
    public static Map<String, String> getUserMessageMap(String userName, String userPassword){
        Map <String,String> userMessageMap = new HashMap<>();
        userMessageMap.put("account",userName);
        userMessageMap.put("password",userPassword);
        account = userName;
        return userMessageMap;
    }

    // 登录之后存储信息,以便后续使用
    public static void setCurrentUser(String userToken,int userId){
        token = userToken;
        id = userId;
    }

    public static void setAccount(String ac) {
        account = ac;
    }
    public static String getCurrentAccount(){
        return account;
    }

    public static String getToken(){ return token; }

    public static int getAccountId(){ return id; }
}
