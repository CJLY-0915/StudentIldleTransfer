package com.example.sit.Service;


import static com.example.sit.Const.baseUrl;
import static com.example.sit.Http.Http.buildHeader;
import static com.example.sit.Http.Http.lGetRequest;
import static com.example.sit.Http.Http.lPostRequest;
import static com.example.sit.Http.Http.postRequest;

import androidx.annotation.NonNull;

import com.example.sit.Http.Http;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

/**
 * 封装的api请求类
 *
 * @Author: Iced
 * @Date: 2022/3/22 22:43
 */
public class Api {

    // 注册
    public static void register(Map<String, String> params, Callback callback) {

        String url = baseUrl + "/api/register";

        FormBody.Builder body = buildHeader(params);

        Http.lPostRequest(url, new Gson().toJson(params), callback);
    }

    // 登录
    public static void login(Map<String, String> params, Callback callback) {

        String url = baseUrl + "/api/login";

        Http.lPostRequest(url, new Gson().toJson(params), callback);
    }

    // 登出
    public static void logout(Callback callback) {

        String url = baseUrl + "/api/logout";

        Http.getRequest(url, callback);
    }

    // 找回密码
    public static void findPassword(Map<String, String> params, Callback callback) {

        String url = baseUrl + "/api/find/password";

        Http.lPostRequest(url, new Gson().toJson(params), callback);
    }

    // 获取用户信息
    public static void getUser(Callback callback) {

        String url = baseUrl + "/api/user";

        Http.getRequest(url, callback);
    }

    // 修改信息
    public static String editUserInfo(Map params) {

        String url = baseUrl + "/api/user/edit";

        return Http.postRequest(url, new Gson().toJson(params));
    }

    // 设置头像
    public static String setAvatar(String file) {

        String url = baseUrl + "/api/file/uploadAvatar";

        return Http.postFileUpload(url, file);
    }

    // 设置昵称
    public static String setNick(String nick) {

        String url = baseUrl + "/api/user/setNick";

        Map<String, String> params = new HashMap<>();
        params.put("nickname", nick);

        return Http.postRequest(url, new Gson().toJson(params));
    }

    // 设置个人简介
    public static String setIntro(String intro) {

        String url = baseUrl + "/api/user/setIntro";

        Map<String, String> params = new HashMap<>();
        params.put("intro", intro);

        return Http.postRequest(url, new Gson().toJson(params));
    }

    // 设置院校
    public static String setSchool(String school) {

        String url = baseUrl + "/api/user/setSchool";

        Map<String, String> params = new HashMap<>();
        params.put("school", school);

        return Http.postRequest(url, new Gson().toJson(params));
    }

    // 设置生日
    public static String setBirth(String birth) {

        String url = baseUrl + "/api/user/setBirth";

        Map<String, String> params = new HashMap<>();
        params.put("birthday", birth);

        return Http.postRequest(url, new Gson().toJson(params));
    }

    // 设置性别
    public static String setSex(String sex) {

        String url = baseUrl + "/api/user/setSex";

        Map<String, String> params = new HashMap<>();
        params.put("sex", sex);

        return Http.postRequest(url, new Gson().toJson(params));
    }

    // 用户关注（关注其他人）
    public static Response follow(String follow_id) {

        String url = baseUrl + "/api/user/follow";

        Map<String, String> params = new HashMap<>();
        params.put("follow_id", follow_id);

        return Http.postRequest1(url, new Gson().toJson(params));
    }

    // 获取用户关注列表
    public static void getFollow(Callback callback) {
        String url = baseUrl + "/api/user/getFollow";

        Http.getRequest(url, callback);
    }

    // 获取其他用户信息
    public static void getOtherUser(String user_id, Callback callback) {

        String url = baseUrl + "/api/user/getUser";

        Map<String, String> params = new HashMap<>();
        params.put("user_id", user_id);

        Http.postRequest(url, new Gson().toJson(params), callback);
    }

    // 获取其他用户信息
    public static Response getOtherUser1(String user_id) {

        String url = baseUrl + "/api/user/getUser";

        Map<String, String> params = new HashMap<>();
        params.put("user_id", user_id);

        return Http.postRequest1(url, new Gson().toJson(params));
    }

    // 用户发布商品
    public static void getUserGoods(Callback callback) {

        String url = baseUrl + "/api/goods";

        Http.getRequest(url, callback);
    }

    // 用户卖出商品
    public static void getSellGoods(Callback callback) {

        String url = baseUrl + "/api/goods/sell";

        Http.getRequest(url, callback);
    }

    // 用户买入商品
    public static void getBuyGoods(Callback callback) {

        String url = baseUrl + "/api/goods/buy";

        Http.getRequest(url, callback);
    }

    // 用户卖出商品
    public static Response getSellGoods1() {

        String url = baseUrl + "/api/goods/sell";

        return Http.getRequest1(url);
    }

    // 用户买入商品
    public static Response getBuyGoods1() {

        String url = baseUrl + "/api/goods/buy";

        return Http.getRequest1(url);
    }

    // 获取商品信息
    public static String getGoods(String goods_id) {

        String url = baseUrl + "/api/goods/getGoods";

        Map<String, String> params = new HashMap<>();
        params.put("goods_id", goods_id);

        return Http.postRequest(url, new Gson().toJson(params));
    }

    // 获取商品信息
    public static Response getGoods1(String goods_id) {

        String url = baseUrl + "/api/goods/getGoods";

        Map<String, String> params = new HashMap<>();
        params.put("goods_id", goods_id);

        return Http.postRequest1(url, new Gson().toJson(params));
    }

    // 获取商品图片
    public static Response getPhoto(String goods_id){

        String url = baseUrl + "/api/goods/getPhoto";

        Map<String, String> params = new HashMap<>();
        params.put("goods_id", goods_id);

        return Http.photoRequest(url, new Gson().toJson(params));
    }

    // 买入商品
    public static Response buy(String goods_id) {

        String url = baseUrl + "/api/buy";

        Map<String, String> params = new HashMap<>();
        params.put("goods_id", goods_id);

        return Http.postRequest1(url, new Gson().toJson(params));
    }

    // 首页
    public static void index(Callback callback) {

        String url = baseUrl + "/api/index";

        Http.getRequest(url, callback);
    }

    // 首页 - 关注
    public static void indexFollow(Callback callback) {

        String url = baseUrl + "/api/index/follow";

        Http.getRequest(url, callback);
    }

    // 首页 - 院校
    public static void indexUniversity(Callback callback) {

        String url = baseUrl + "/api/index/university";

        Http.getRequest(url, callback);
    }

    // 搜索
    public static String search(String entry) {

        String url = baseUrl + "/api/index/search";

        Map<String, String> params = new HashMap<>();
        params.put("entry", entry);

        return Http.postRequest(url, new Gson().toJson(params));
    }

    // 获取地址
    public static void getAddress(Callback callback) {
        String url = baseUrl + "/api/address/get";

        Http.getRequest(url, callback);
    }

    // 修改地址
    public static String editAddress(Map<String, String> params) {
        String url = baseUrl + "/api/address/edit";

        return Http.postRequest(url, new Gson().toJson(params));
    }

    // 添加地址
    public static String addAddress(Map<String, String> params) {
        String url = baseUrl + "/api/address/add";

        return Http.postRequest(url, new Gson().toJson(params));
    }

    // 删除地址
    public static String deleteAddress(String address_id) {
        String url = baseUrl + "/api/address/delete";

        Map<String, String> params = new HashMap<>();
        params.put("address_id", address_id);

        return Http.postRequest(url, new Gson().toJson(params));
    }

    // 用户发布商品
    public static Response releaseGoods(Map<String, Object> map) {
        String url = baseUrl + "/api/goods/uploadGoods";

        return Http.postParamFileUpload(url, map);
    }
}
