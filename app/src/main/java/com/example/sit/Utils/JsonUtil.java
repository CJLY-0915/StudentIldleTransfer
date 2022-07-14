package com.example.sit.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @Author: Iced
 * @Date: 2022/4/23 12:11
 */
public class JsonUtil {

    public static JSONObject getJson(String res) {
        JSONObject json = null;
        try {
            json = new JSONObject(res);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static int getStatusCode(String res) {
        int code = 0;
        try {
            code = getJson(res).getInt("status_code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return code;
    }

    public static String getMessage(String res) {
        String msg = "";
        try {
            msg = getJson(res).getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return msg;
    }

    public static JSONArray getDataArray(String res) {
        JSONArray arr = new JSONArray();
        try {
            arr = getJson(res).getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arr;
    }

    public static JSONObject getDataJson(String res) {

        JSONObject data = new JSONObject();
        try {
            data = getJson(res).getJSONObject("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

}
