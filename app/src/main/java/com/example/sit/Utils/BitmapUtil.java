package com.example.sit.Utils;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

// Bitmap图片处理工具类
public class BitmapUtil {
    // Bitmap格式的图片存储在本地SD卡,并且转化成url
    public static String bitmapToUrl(Bitmap bitmapDate) throws IOException {
        String path = getAboSdPath() + "/temp/";
        File file = new File(path);
        // 第一次转换会创建图片的保存路径,也就是第一次更换头像时
        if (!file.exists()){
            file.mkdirs();
        }
        // 最终生成的图片在本地的url
        File saveHeaderImage = new File(path + createShortUUID() + ".png");
        // 将图片以流的形式传输,并且压缩图片
        BufferedOutputStream bs = null;
        try {
            saveHeaderImage.createNewFile();
            bs = new BufferedOutputStream(new FileOutputStream(saveHeaderImage));
            // 压缩图片的宽高,权重设置为100,
            bitmapDate.compress(Bitmap.CompressFormat.JPEG, 100, bs);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            bs.flush();
            bs.close();
        }
        return saveHeaderImage.toString();
    }

    // 获取SD卡的绝对路径
    public static String getAboSdPath(){
        boolean sdIsExit = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdIsExit)
            return Environment.getExternalStorageDirectory().toString();
        return "";
    }

    // 生成随机的UUID作为保存的图片名,避免重复的id将原来的图片覆盖
    public static String createShortUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0,6);
    }
}
