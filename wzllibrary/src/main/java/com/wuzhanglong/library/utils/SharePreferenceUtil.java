
package com.wuzhanglong.library.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferenceUtil {
    public static void putSharedpreferences(Context context, String fileName, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getSharedpreferenceValue(Context context, String fileName, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, Activity.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");// 1表示后台定位
    }

}
