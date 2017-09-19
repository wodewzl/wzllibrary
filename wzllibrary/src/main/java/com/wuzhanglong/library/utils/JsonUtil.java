
package com.wuzhanglong.library.utils;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

public final class JsonUtil {

    private JsonUtil() {
    }

    /**
     * 对象转换成json字符串
     * 
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    /**
     * json字符串转成对象
     * 
     * @param str
     * @param type
     * @return
     */
    public static <T> T fromJson(String str, Type type) {
        Gson gson = new Gson();
        return gson.fromJson(str, type);
    }

    /**
     * json字符串转成对象
     * 
     * @param str
     * @param type
     * @return
     */
    public static <T> T fromJson(String str, Class<T> type) {
        Gson gson = new Gson();
        return gson.fromJson(str, type);
    }

    // 判断json数据里是否有某个key
    public static boolean hasJsonKey(String str, String key) {
        try {
            JSONObject json = new JSONObject(str);
            if (json.has(key))
                return true;
            else
                return false;
        } catch (Exception e) {
            return false;
        }
    }

    // 根据key 获取value
    public static String getJsonStr(String JsonStr, String key) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(JsonStr);
            return jsonObject.get(key).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }
}
