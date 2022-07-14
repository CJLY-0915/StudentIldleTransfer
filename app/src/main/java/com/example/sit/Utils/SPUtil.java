package com.example.sit.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @Author: Iced
 * @Date: 2022/4/24 23:09
 */
public class SPUtil {

    private static SharedPreferences sp;
    static Context context;

    public SPUtil(Context context, String name) {
        SPUtil.context = context;
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public void setState() {

        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isLogin", true);
        editor.apply();
    }

    public boolean getState() {
        return sp.getBoolean("isLogin", false);
    }

    public void removeState() {
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    public String getAccount() {
        return sp.getString("account", "");
    }

    public String getPassword() {

        return sp.getString("password", "");
    }

    public String getToken() {

        return sp.getString("token", "");
    }

    public void setAccount(String username) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("account", username);
        editor.apply();
    }

    public void setPassword(String password) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("password", password);
        editor.apply();
    }

    public void setToken(String token) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("token", token);
        editor.apply();
    }
}
