package com.example.sit.Utils;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

// 用于处理注册数据的工具类
public class RegistryUtil {

    private static boolean flag = false;
    private String regUserName;
    private String regPwd;
    private String regRePwd;

    public RegistryUtil() {
    }

    public RegistryUtil(String regUserName, String regPwd, String regRePwd){
        this.regUserName = regUserName.trim().replaceAll(" ","");
        this.regPwd = regPwd.trim().replaceAll(" ","");
        this.regRePwd = regRePwd.trim().replaceAll(" ","");
    }

    // 获取用户输入的注册数据并判断是否合法
    public boolean handleInputUserMessage(Context context){
        // 判断空值
        if (regUserName.equals("") || regPwd.equals("") || regRePwd.equals("")){
            if (regUserName.equals("")){
                NotifyUtil.createNotify(context,"用户名不能为空");
            }else if (regPwd.equals("")){
                NotifyUtil.createNotify(context,"密码不能为空");
            }else {
                NotifyUtil.createNotify(context,"二次确认密码不能为空");
            }
            return flag;
        }else {
            // 判断长度和组成是否合法(用户名必须包含字母数字,密码只能由且必须包含大小写字母、数字)
            GetCharCombineUtil charCombineUtil = new GetCharCombineUtil();
            if (regUserName.length() < 6 || regUserName.length() > 16){
                NotifyUtil.createNotify(context,"用户名长度在6~16位之间");
            }else if(charCombineUtil.getUserNameCharCombine(regUserName) != true){
                NotifyUtil.createNotify(context,"用户名必须包含字母和数字");
            }else if (regPwd.length() < 7 || regPwd.length() > 14){
                NotifyUtil.createNotify(context,"密码长度在7~14位之间");
            }else if (charCombineUtil.getPwdCharCombine(regPwd) != true){
                NotifyUtil.createNotify(context,"密码由大小写字母和数字组成");
            }else if (!regPwd.equals(regRePwd)){
                NotifyUtil.createNotify(context,"两次密码输入不一致");
            }else
                return !flag;
        }
        return flag;
    }

    // 封装合法的注册数据,供后台调用
    public Map<String, String> getRegUserMessage(String regUserName,String regPwd){
        HashMap<String, String> map = new HashMap<>();
        // 1.用户名 2.密码 3.昵称 4.默认头像
        map.put("account",regUserName);
        map.put("password",regPwd);
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String nickName = uuid.substring(0, 6);
        map.put("nickname",nickName);
        map.put("avatar_url","/app_icon.png");
        return map;
    }
}
