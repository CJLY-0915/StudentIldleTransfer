package com.example.sit.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.example.sit.UI.Activity.EditPersonalMessageActivity;
import com.example.sit.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditMessageUtil {
    public static boolean isAble = true;

    // 回显查询到的用户信息
    public static void showUserMessage(ImageView headerImage,EditText userNameEditText,
                                       EditText nickNameEditText, EditText sexEditText,
                                       EditText birthEditText, EditText desEditText,
                                       EditText schoolEditText, HashMap map,EditPersonalMessageActivity em){
        userNameEditText.setText(LoginUtil.getCurrentAccount());
        nickNameEditText.setText(map.get("nickname").toString());
        if ((int)map.get("sex") == 1){
            sexEditText.setText("男");
        } else {
            sexEditText.setText("女");
        }
        if (map.get("birthday") == null){
            birthEditText.setHint("暂无");
        } else {
            Date date = (Date)map.get("birthday");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            birthEditText.setText(dateFormat.format(date));
        }
        if (map.get("introduction") == null){
            desEditText.setHint("暂无");
        } else {
            desEditText.setText(map.get("introduction").toString());
        }
        if (map.get("university") == null){
            schoolEditText.setHint("暂无");
        } else {
            schoolEditText.setText(map.get("university").toString());
        }
        // 根据图片url将图片转化成bitmap,再设置到相应的控件上
        // 是http前缀的网络图片就采用Picasso加载
        String originalUrl = (String)map.get("avatar_url");
        String url = originalUrl.substring(0, 4);
        if (url.equals("http")){
            // 图片未从网络加载完成时,会先显示默认头像
            Picasso.with(em)
                    .load(originalUrl)
                    .placeholder(R.mipmap.xxz_logo)
                    .into(headerImage);
        } // 不是网络图片url
        else {
            File filePath = new File(url);
            Bitmap bitmap = BitmapFactory.decodeFile(originalUrl);
            headerImage.setImageBitmap(bitmap);
        }
    }

    // 设置当前文本为可编辑状态(编辑)
    public static void setEditIsAble(EditText nickNameEditText,EditText sexEditText,
                                     EditText birthEditText,EditText desEditText,EditText schoolEditText){
        nickNameEditText.setEnabled(isAble);
        sexEditText.setEnabled(isAble);
        birthEditText.setEnabled(isAble);
        desEditText.setEnabled(isAble);
        schoolEditText.setEnabled(isAble);
    }

    // 设置当前文本为不可编辑状态(保存),并且将修改的信息封装成map,供后端调用
    public static Map setEditIsNotAble(EditText nickNameEditText,EditText sexEditText,
                                     EditText birthEditText,EditText desEditText,EditText schoolEditText){
        // 先将所有的信息框设置为不可编辑
        nickNameEditText.setEnabled(!isAble);
        sexEditText.setEnabled(!isAble);
        birthEditText.setEnabled(!isAble);
        desEditText.setEnabled(!isAble);
        schoolEditText.setEnabled(!isAble);
        // 再获取每个信息框的值,封装成集合
        int sex;
        String nickName = nickNameEditText.getText().toString();
        String sexEdit = sexEditText.getText().toString();
        if (sexEdit.equals("男")){
            sex = 1;
        }else {
            sex = 0;
        }
        String birth = birthEditText.getText().toString();
        String des = desEditText.getText().toString();
        String school = schoolEditText.getText().toString();
        HashMap<String, Object> newUserMessage = new HashMap<>();
        newUserMessage.put("nickname",nickName);
        newUserMessage.put("sex",sex);
        newUserMessage.put("birthday",birth);
        newUserMessage.put("introduction",des);
        newUserMessage.put("university",school);
        System.out.println(newUserMessage);
        return newUserMessage;
    }

}
