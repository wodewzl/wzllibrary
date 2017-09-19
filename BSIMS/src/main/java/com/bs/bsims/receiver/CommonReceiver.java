
package com.bs.bsims.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bs.bsims.utils.CustomToast;

public class CommonReceiver extends BroadcastReceiver {
    public static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";// 开机广播

    @Override
    public void onReceive(Context context, Intent intent) {
        CustomToast.showShortToast(context, "开机啦1");
        if (ACTION_BOOT.equals(intent.getAction())) {
            // setReminder(context);
        }
    }

    // 注册定时消息广播 (定位的)
    public void setReminder(Context context) {
        AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Intent i = new Intent();
        i.setAction("LogistiRecevierOfGaode");
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000, pi);// 二十分钟定位一次
    }

}
