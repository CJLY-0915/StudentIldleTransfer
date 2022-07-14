package com.example.sit.Utils;

import android.content.Context;
import android.widget.Toast;

// 用于系统内通知的工具类
public class NotifyUtil {
    // 创建通知信息
    public static void createNotify(Context context,String tipMessage){
            Toast.makeText(context, tipMessage, Toast.LENGTH_SHORT).show();
        }
}
