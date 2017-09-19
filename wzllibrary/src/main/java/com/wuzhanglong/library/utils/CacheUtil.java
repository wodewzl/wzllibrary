
package com.wuzhanglong.library.utils;

import android.content.Context;

import com.loopj.android.http.RequestParams;
import com.wuzhanglong.library.cache.ACache;

import java.util.Map;

public class CacheUtil {
    public static String getKey(String url, Map<String, String> map) {
        StringBuffer sb = new StringBuffer();
        sb.append(url);
        if (map == null || map.size() == 0)
            return url;
        for (String key : map.keySet()) {
            if ("".equals(map.get(key)) || map.get(key) == null)
                continue;
            sb.append("/").append(key).append("/").append(map.get(key));
        }
        return sb.toString();
    }



    /**
     * 设置缓存数据（key,value）
     */
    public static void setCacheStr(Context context, String key, String value) {
        if (!StringUtils.isEmpty(value)) {
            ACache.get(context).put(key, value);
        }
    }

    /**
     * 获取缓存数据更具key
     */
    public static String getCacheStr(Context context, String key) {
        return ACache.get(context).getAsString(key);
    }


    /**
     * 设置缓存数据（key,value）
     */
    public static void setCacheStrTime(Context context, String url, RequestParams params, String result, int time) {
        if (!StringUtils.isEmpty(result)) {
//            ACache.get(context).put(getKey(url, map), result, time);
            ACache.get(context).put(url + params.toString(), result, 60*60*24);
        }
    }

    /**
     * 设置缓存数据（key,value）
     */
    public static void setCacheStr(Context context, String url, RequestParams params, String result) {
        if (!StringUtils.isEmpty(result)) {
//            ACache.get(context).put(getKey(url, map), value);
            ACache.get(context).put(url + params.toString(), result);
        }
    }

    /**
     * 获取缓存数据更具key
     */
    public static String getCacheStr(Context context,String url, RequestParams params) {
//        return ACache.get(context).getAsString(getKey(url, map));
        return ACache.get(context).getAsString(url + params.toString());
    }




}
