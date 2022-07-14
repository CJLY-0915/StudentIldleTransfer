package com.example.sit.Dialogs;

import android.app.AlertDialog;
import android.content.Context;

import com.example.sit.R;

// 登录页面,右上角关于我们弹窗
public class RegardingDialog {
    public static void createReDialog(Context context){
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(R.string.regarding_dialog_title)
                .setMessage(R.string.regarding_dialog_message)
                .setIcon(R.mipmap.about_us)
                .create();
        dialog.show();
    }
}
