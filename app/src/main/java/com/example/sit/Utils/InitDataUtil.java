package com.example.sit.Utils;

import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.sit.Const;
import com.example.sit.Entity.Goods;
import com.example.sit.Entity.GoodsPhoto;
import com.example.sit.Service.Api;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @Author: Iced
 * @Date: 2022/4/25 23:01
 */
public class InitDataUtil {

    private List<Goods> goodsList = new ArrayList<>();
    private List<List<GoodsPhoto>> photoList = new ArrayList<>();

//    public static List<Goods> getGoodsList() {
//        Api.index(new Callback() {
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                String res = response.body().string();
//                int code = JsonUtil.getStatusCode(res);
//                String msg = JsonUtil.getMessage(res);
//                Looper.prepare();
//                if (code == Const.SuccessCode) {
//                    Log.v(Const.RequestTag, msg);
//                    JSONArray data = JsonUtil.getDataArray(res);
//                    Gson gson = new Gson();
//                    List<Goods> goodsList = gson.fromJson(data.toString(), new TypeToken<List<Goods>>(){}.getType());
//                    Log.v(Const.GoodsTag, goodsList.toString());
//                }
//                for (Goods good : goodsList) {
//                    String good_id = String.valueOf(good.getId());
//                    Api.getPhoto(good_id, new Callback() {
//                        @Override
//                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
//
//                        }
//
//                        @Override
//                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                            String res = response.body().string();
//                            int code = JsonUtil.getStatusCode(res);
//                            String msg = JsonUtil.getMessage(res);
//                            if (code == Const.SuccessCode) {
//                                Log.v("GoodsPhoto ---> ", msg);
//                                JSONArray data = JsonUtil.getDataArray(res);
//                                Gson gson = new Gson();
//                                photoList.add(gson.fromJson(data.toString(), new TypeToken<List<GoodsPhoto>>(){}.getType()));
//                                Log.v("GoodsPhoto ---> ", photoList.toString());
//                            }
//                        }
//                    });
//                }
//                Looper.loop();
//                response.body().close();
//            }
//        });
//    }
}
