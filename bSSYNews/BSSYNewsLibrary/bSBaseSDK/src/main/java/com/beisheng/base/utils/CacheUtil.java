
package com.beisheng.base.utils;

import android.content.Context;

import com.beisheng.base.cache.ACache;
import com.beisheng.base.database.CacheDbHelper;

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

    // 没有网络从数据库获取缓存
    public static String getCacheFromDatabase(Context context, String url, Map<String, String> map) {
        CacheDbHelper dbHelper = new CacheDbHelper(context);
        return dbHelper.queryJsonByKeyName(getKey(url, map));
    }

    // 有网路的时候保存jsonstrl
    public static void saveJsonCache(Context context, String url, Map<String, String> map, String jsonStr) {
        CacheDbHelper dbHelper = new CacheDbHelper(context);
        dbHelper.insertOrUpdate(getKey(url, map), jsonStr);
    }

    /**
     * 设置缓存数据（key,value）
     */
    public void setCacheStr(Context context, String key, String value) {
        if (!StringUtils.isEmpty(value)) {
            ACache.get(context).put(key, value);
        }
    }

    /**
     * 获取缓存数据更具key
     */
    public String getCacheStr(Context context, String key) {
        return ACache.get(context).getAsString(key);
    }

}
