
package com.bs.aikushi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class JPushReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Gson gson = new Gson();
        Map<String, String> map = gson.fromJson(extra, new TypeToken<Map<String, String>>() {
        }.getType());

        if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            // 打开自定义的Activity
            String url = map.get("url");
            EventBus.getDefault().post(new EBMessageVO("push",url));
        }
    }
}
