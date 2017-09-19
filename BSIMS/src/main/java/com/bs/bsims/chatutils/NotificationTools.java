
package com.bs.bsims.chatutils;

import java.util.List;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.bs.bsims.R;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.chat.ChatMessageWithPerson;
import com.bs.bsims.model.DepartmentAndEmployeeVO;
import com.yzxIM.IMManager;
import com.yzxIM.data.db.ChatMessage;
import com.yzxIM.data.db.ConversationInfo;
import com.yzxtcp.tools.CustomLog;

public class NotificationTools {

    private static int num = 0;
    private static Vibrator vibrator;
    private static long[] pattern = {
            100, 400, 100, 400
    }; // 停止 开启 停止 开启

    public static void showNotification(ChatMessage msg) {
        if (isMessageActivity(BSApplication.getInstance())) {
            CustomLog.d("聊天页面正在显示，不需要通知");
            return;
        }
        // if (userSetting.getMsgNotify() == 1 && userSetting.getMsgVoice() == 1) {
        // Uri notifytone = RingtoneManager
        // .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        // Ringtone r = RingtoneManager.getRingtone(
        // BSApplication.getInstance(), notifytone);
        // r.play();
        // }
        // if (userSetting.getMsgNotify() == 1 && userSetting.getMsgVitor() == 1) {
        // if (null == vibrator) {
        // vibrator = (Vibrator)BSApplication.getInstance()
        // .getSystemService(Context.VIBRATOR_SERVICE);
        // }
        // vibrator.vibrate(pattern, -1);
        // }

        if (!isBackground(BSApplication.getInstance())) {
            return;
        }

        DepartmentAndEmployeeVO mVo = ConcatInfoUtils.getInstance().getUserByIM(msg.getSenderId());
        if (null != mVo) {
            msg.setNickName(mVo.getFullname());
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                BSApplication.getInstance());
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setDefaults(Notification.DEFAULT_VIBRATE);
        builder.setAutoCancel(true);
        builder.setContentTitle("新的消息");
        CharSequence contentTitle = "北企星";
        CharSequence contentText = "";
        switch (msg.getMsgType()) {
        // 文本
            case MSG_DATA_TEXT:
                contentText = new String(msg.getNickName()) + ": "
                        + msg.getContent();
                break;
            // 图片
            case MSG_DATA_IMAGE:
                contentText = new String(msg.getNickName()) + "发来一张图片";
                break;
            // 语音
            case MSG_DATA_VOICE:
                contentText = new String(msg.getNickName()) + "发来一段语音";
                break;
            case MSG_DATA_LOCALMAP:
                contentText = new String(msg.getNickName()) + "分享一个地理位置";
                break;
            case MSG_DATA_CUSTOMMSG:
                contentText = new String(msg.getNickName()) + ": 自定义消息";
                break;
        }
        Intent notificationIntent = new Intent();
        try {
            // if(IMManager.getInstance(null).getConversation(msg.getParentID()).getCategoryId() ==
            // CategoryId.BROADCAST){
            // // contentText = "您有一条新的广播消息";
            // // notificationIntent = new Intent(BSApplication.getInstance(),
            // // IMBroadcastActivity.class);
            // }else{
            notificationIntent = new Intent(BSApplication.getInstance(),
                    ChatMessageWithPerson.class);
        } catch (Exception e) {
            e.printStackTrace();
            CustomLog.e(e.getMessage());
        }

        num++;
        if (num > 1) {
            contentText = "你有" + num + "条未读消息";
        }

        ConversationInfo info = IMManager.getInstance(
                BSApplication.getInstance()).getConversation(
                msg.getParentID());
        if (null == info) {
            CustomLog.e("Notification info is null");
            return;
        }
        builder.setContentText(contentText);
        info.setConversationTitle(msg.getNickName());
        notificationIntent.putExtra("conversation", info);
        notificationIntent.setAction(System.currentTimeMillis() + "");
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(
                BSApplication.getInstance(), 0, notificationIntent, 0);
        builder.setContentIntent(contentIntent);
        BSApplication.nm.notify(1000, builder.build());
    }

    public static void clearUnreadNum() {
        num = 0;
    }

    /**
     * @Description 是否是聊天页面
     * @return
     * @date 2015-12-18 上午10:06:02
     * @author zhuqian
     * @return boolean
     */
    public static boolean isMessageActivity(Context context) {
        ActivityManager manager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> runningTasks = manager.getRunningTasks(1);
        RunningTaskInfo cinfo = runningTasks.get(0);
        ComponentName component = cinfo.topActivity;
        String resumeClassName = component.getClassName();
        CustomLog.d("当前获取焦点的Activity class name ：" + resumeClassName);
        if (!TextUtils.isEmpty(resumeClassName)
                && resumeClassName.equals("ChatMessageWithPerson")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {

                if (appProcess.importance != RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    CustomLog.d("处于后台" + appProcess.processName);
                    return true;
                } else {
                    CustomLog.d("处于前台" + appProcess.processName);
                    // return false;
                    return true;// 处于前台也收到推送通知
                }
            }
        }
        return false;
    }

}
