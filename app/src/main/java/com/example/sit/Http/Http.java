package com.example.sit.Http;

import androidx.annotation.NonNull;

import com.example.sit.Utils.LoginUtil;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @Author: Iced
 * @Date: 2022/3/27 16:24
 */
public class Http {

    public static final MediaType Json = MediaType.parse("application/json;charset=utf-8");
    private static final String TAG = "HTTP Exception";
    private static String res;
    private static String ResponseData = null;
    private static Boolean aBoolean = false;

    // 构建请求头
    public static FormBody.Builder buildHeader(Map<String, String> params) {

        FormBody.Builder body = new FormBody.Builder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            body.add(entry.getKey(), entry.getValue());
        }

        return body;
    }

    // 带token的get请求
    public static void getRequest(String url, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer" + LoginUtil.getToken())
                .get()
                .build();
        client.newCall(request).enqueue(callback);
    }

    // 带token的get请求
    public static Response getRequest1(String url) {
        OkHttpClient client = new OkHttpClient();
        Response response = null;
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer" + LoginUtil.getToken())
                .get()
                .build();
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    // 不带token的get请求
    public static String lGetRequest(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                ResponseData = e.getMessage();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String data = response.body().string();
                ResponseData = data;
                aBoolean = true;
            }
        });
        while(true) {
            if (aBoolean) return ResponseData;
            else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 带token的post请求
    public static String postRequest(String url, String json) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(json, Json);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer" + LoginUtil.getToken())
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                ResponseData = e.getMessage();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String data = response.body().string();
                ResponseData = data;
                aBoolean = true;
                response.body().close();
            }
        });
        while(true) {
            if (aBoolean) return ResponseData;
            else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void postRequest(String url, String json, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(json, Json);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer" + LoginUtil.getToken())
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static Response postRequest1(String url, String json) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(json, Json);
        Response response = null;
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer" + LoginUtil.getToken())
                .post(body)
                .build();
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static Response photoRequest(String url, String json) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(json, Json);
        Response response = null;
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer" + LoginUtil.getToken())
                .post(body)
                .build();
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    // 不带token的post请求
    public static void lPostRequest(String url, String json, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(json, Json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    // 带token不带参数的文件上传
    public static String postFileUpload(String url, String path) {
        OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
        File file = new File(path);
        if (file != null) {
            String fileName = file.getName();
            body.addFormDataPart("avatar", fileName, RequestBody.create(MediaType.parse("image/*"),file));
        }
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "multipart/form-data")
                .addHeader("Authorization", "Bearer" + LoginUtil.getToken())
                .post(body.build())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                ResponseData = e.getMessage();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String data = response.body().string();
                ResponseData = data;
                aBoolean = true;
            }
        });
        while(true) {
            if (aBoolean) return ResponseData;
            else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 带token不带参数的文件上传
    public static String postFileUploads(String url, String path) {
        OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
        File file = new File(path);
        if (file != null) {
            String fileName = file.getName();
            body.addFormDataPart("avatar", fileName, RequestBody.create(MediaType.parse("image/*"),file));
        }
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "multipart/form-data")
                .addHeader("Authorization", "Bearer" + LoginUtil.getToken())
                .post(body.build())
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                ResponseData = e.getMessage();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String data = response.body().string();
                ResponseData = data;
                aBoolean = true;
            }
        });
        while(true) {
            if (aBoolean) return ResponseData;
            else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 带token带参数的文件上传
    public static Response postParamFileUpload(String url, Map<String, Object> params) {

        OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        Response response = null;

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof String[]) {
                String[] files = (String[]) value;
                for (int i = 0; i < files.length; i++) {
                    System.out.println(files[i]);
                    if (files[i] != null) {
                        File file = new File(files[i]);
                        builder.addFormDataPart("images[]", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
                    }
                }
            }
            else if (value instanceof String) {
                builder.addFormDataPart(key, (String)value);
            }
        }
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "multipart/form-data")
                .addHeader("Authorization", "Bearer" + LoginUtil.getToken())
                .post(builder.build())
                .build();
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
