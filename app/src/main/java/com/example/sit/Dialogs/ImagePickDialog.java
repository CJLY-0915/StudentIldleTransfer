package com.example.sit.Dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import com.example.sit.Const;
import com.example.sit.Entity.User;
import com.example.sit.Service.Api;
import com.example.sit.UI.Activity.MainActivity;
import com.example.sit.Utils.BitmapUtil;
import com.example.sit.Utils.JsonUtil;
import com.example.sit.Utils.LoginUtil;
import com.example.sit.Utils.SaveUrlToFileUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import io.grpc.internal.ConscryptLoader;

// 更换头像的弹窗
public class ImagePickDialog {
    public static final int ALBUM_RE_CODE = 200;
    public static String goalImageUrl = "";
    public static File goodFile = null;

    // 打开系统相册
    public static void openAlbum(Activity av) {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        // 打开所有的图片
        intent.setType("image/*");
        // 自定义的响应码为200,用于在回调中判断状态
        av.startActivityForResult(intent, ALBUM_RE_CODE);
    }

    // 获取项目的API版本
    public static int getSDKVersionNumber() {
        int sdkVersion;
        try {
            sdkVersion = Integer.valueOf(Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            sdkVersion = 0;
            e.printStackTrace();
        }
        return sdkVersion;
    }

    // 调用系统相册后在回调函数中调用这个方法,解析返回的图片并显示
    public static void handleSelectImage(Intent data, Activity av, ImageView headerImg) {
        String imagePath = "";
        Uri uri = data.getData();
        // 安卓版本大于4.4 返回的uri不是真实的uri,需要解析后才能得到真正的图片地址
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            imagePath = getRealPath(uri, av);
            displayImage(imagePath,headerImg);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("Range")
    // 根据不同情况解析,并得到选取图片的真实地址
    public static String getRealPath(Uri uri, Activity av) {
        String path = null;
        if (DocumentsContract.isDocumentUri(av, uri)) {
            // document类型的uri, 通过document的id来解析
            String documentId = DocumentsContract.getDocumentId(uri);
            if (isMediaDocument(uri)) {
                // 根据uri地址,按照:将地址分割开,写进字符串数组 域名/主机名/路径/id
                // 测试后,获取的相册本地图片格式:com.android.providers.media.documents
                // 下载的图片格式:com.android.providers.downloads.documents
                String[] divide = documentId.split(":");
                String type = divide[0];
                Uri mediaUri = null;
                if ("image".equals(type)) {
                    mediaUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    mediaUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    mediaUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                } else {
                    return null;
                }
                String selection = BaseColumns._ID + "=?";
                String[] selectionArgs = {divide[1]};
                path = getDataColumn(av, mediaUri, selection, selectionArgs);
            } else if (isDownloadsDocument(uri)) {
                // 下载类型的图片
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                path = getDataColumn(av, contentUri, null, null);
            }else if(isExternalStorageDocument(uri)) {
                String [] split = documentId.split(":");
                if(split.length >= 2) {
                    String type = split[0];
                    if("primary".equalsIgnoreCase(type)) {
                        path = Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                }
            }
        } else if (ContentResolver.SCHEME_CONTENT.equalsIgnoreCase(uri.getScheme())){
            // content类型的图片
            path = getDataColumn(av, uri, null, null);
        } else if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
            // file类型的图片,直接获取图片对应的路径
            path = uri.getPath();
        }
        return path;
    }

    // 查询相册'数据库'
    private static String getDataColumn(Activity av, Uri uri, String selection, String[] selectionArgs) {
        String path = null;
        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            // 调用相册本质上是查询数据库
            cursor = av.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

    // 匹配文件的前缀,分别是系统自带的文件(图片、音频、视频等),下载的文件,系统内部存储空间的文件
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    // 设置头像的显示,并解析生成url
    private static void displayImage(String imagePath,ImageView headerImg) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            // 头像的url
            String url = null;
            try {
                url = BitmapUtil.bitmapToUrl(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 头像文件
            File file = SaveUrlToFileUtil.getFile(bitmap);
            System.out.println(file);
            setGoalImageUrl(url);
            setGoodFile(file);
            System.out.println("头像的url:"+url);
            // TODO: 2022/3/20 根据用户id,将新头像的url存进数据库
//                int accountId = LoginUtil.getAccountId();
            // 设置头像回显
//                File filePath = new File(url);
            Bitmap headerImage = BitmapFactory.decodeFile(url);
            headerImg.setImageBitmap(headerImage);
        }
    }

    public static void setGoalImageUrl(String url){
        goalImageUrl = url;
    }

    public static String getGoalImageUrl(){
        return goalImageUrl;
    }

    public static void setGoodFile(File file) {
        goodFile = file;
    }

    public static File  getGoodFile() {
        return goodFile;
    }
}
