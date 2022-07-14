package com.example.sit.Utils;

import android.content.Context;
import android.view.KeyEvent;
import android.widget.Toast;

// 全局适用的退出工具类
public class GlobalExitUtil {
    public static long clickFirstTime;
    public static long clickSecondTime;

    public static void Exit(int code, KeyEvent event, Context context){
        switch (code){
            case KeyEvent.KEYCODE_BACK:
                // 记录第一次点击返回的时间,转化成毫秒存储
                clickSecondTime = System.currentTimeMillis();
                if (clickSecondTime - clickFirstTime > 2000){
                    Toast.makeText(context, "再按一次返回退出", Toast.LENGTH_SHORT).show();
                    clickFirstTime = clickSecondTime;
                }else {
                    ActivityCollectorUtil.getInstance().finishAll();
                }
                break;
        }
    }
}
