
package com.bs.bsims.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.bs.bsims.R;

public class LogintsReceiver extends BroadcastReceiver {

    private NotificationManager manager;
    private Context revercontext;

    /**
     * called when the BroadcastReceiver is receiving an Intent broadcast.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        revercontext = context;
        /* start another activity - MyAlarm to display the alarm 直接跳出activity */
        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // intent.setClass(context, MyAlarm.class);
        // context.startActivity(intent);
        manager = (NotificationManager) context
                .getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        // 例如这个id就是你传过来的
        String id = intent.getStringExtra("id");
        String content = intent.getStringExtra("rinfo");

        Intent playIntent = new Intent();
        // MainActivity是你点击通知时想要跳转的Activity
        // if (TextUtils.isEmpty(BSApplication.getInstance().getUserId())
        // || TextUtils.isEmpty(BSApplication.getInstance()
        // .getHttpTitle())) {
        // playIntent.setClass(context, LoginActivity.class);
        // }
        // else {
        // if (!content.contains("日")) {
        // // MainActivity是你点击通知时想要跳转的Activity
        // playIntent.setClass(context, EXTSignInMapActivity.class);
        // }
        // else {
        // // MainActivity是你点击通知时想要跳转的Activity
        // playIntent.setClass(context, JournalEditActivity.class);
        // }
        // }

        playIntent.putExtra("id", id);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1,
                playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context);
        builder.setContentTitle("北企星").setContentText("您有新的通知")
                // .setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" +
                // R.raw.nsm_schedule_music))
                .setSmallIcon(R.drawable.ic_launcher)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent).setAutoCancel(true)
                .setSubText(content);
        manager.notify(Integer.parseInt(id), builder.build());

    }
    // private boolean isBackgroundRunning() {
    // String processName = "com.bs.bsims.activity";
    // ActivityManager activityManager = (ActivityManager)
    // revercontext.getSystemService(Context.ACTIVITY_SERVICE);
    // KeyguardManager keyguardManager = (KeyguardManager)
    // revercontext.getSystemService(Context.KEYGUARD_SERVICE);
    // if (activityManager == null)
    // return false;
    // // get running application processes
    // List<ActivityManager.RunningAppProcessInfo> processList =
    // activityManager.getRunningAppProcesses();
    // for (ActivityManager.RunningAppProcessInfo process : processList) {
    // if (process.processName.startsWith(processName)) {
    // boolean isBackground = process.importance != RunningAppProcessInfo.IMPORTANCE_FOREGROUND &&
    // process.importance != RunningAppProcessInfo.IMPORTANCE_VISIBLE;
    // boolean isLockedState = keyguardManager.inKeyguardRestrictedInputMode();
    // if (isBackground || isLockedState)
    // return true;
    // else
    // return false;
    // }
    // }
    // return false;
    // }

}
