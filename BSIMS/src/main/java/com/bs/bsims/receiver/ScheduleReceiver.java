
package com.bs.bsims.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.bs.bsims.R;
import com.bs.bsims.activity.ScheduleDetailActivity;
import com.bs.bsims.application.BSApplication;

public class ScheduleReceiver extends BroadcastReceiver {
    private NotificationManager manager;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (TextUtils.isEmpty(BSApplication.getInstance().getUserId())) {
            SharedPreferences preference = context.getSharedPreferences("user", Context.MODE_PRIVATE);
            String userid = preference.getString("userid", "");
            String company = preference.getString("company", "");
            String httpTitle = preference.getString("http_title", "");
            BSApplication.getInstance().setUserId(userid);
            BSApplication.getInstance().setmCompany(company);
            BSApplication.getInstance().setHttpTitle(httpTitle);
        }

        long[] vir = {
                0, 100, 200, 300
        };
        manager = (NotificationManager) context
                .getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        // 例如这个id就是你传过来的
        String id = intent.getStringExtra("id");
        String info = intent.getStringExtra("info");
        // MainActivity是你点击通知时想要跳转的Activity
        // isBackground(context);
        Intent playIntent = new Intent(context, ScheduleDetailActivity.class);
        playIntent.putExtra("id", id);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1,
                playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context);

        // 自定义声音
        // .setContentText(info).setSound(Uri.parse("android.resource://" + context.getPackageName()
        // + "/" + R.raw.nsm_schedule_music))
        builder.setContentTitle("日程提醒")
                .setVibrate(vir)
                .setSmallIcon(R.drawable.ic_launcher)
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setContentIntent(pendingIntent).setAutoCancel(true);
        manager.notify(Integer.parseInt(id), builder.build());
    }

}
