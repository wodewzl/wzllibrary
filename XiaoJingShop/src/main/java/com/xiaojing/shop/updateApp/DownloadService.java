
package com.xiaojing.shop.updateApp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.widget.RemoteViews;

import com.wuzhanglong.library.R;
import com.wuzhanglong.library.utils.FileUtil;
import com.xiaojing.shop.activity.HomeActivity;
import com.xiaojing.shop.constant.Constant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.umeng.socialize.utils.DeviceConfig.context;

public class DownloadService extends Service {
    // notification 名字
    private String notify_name = "正在下载...";
    private Context mContext = this;
    Notification mNotification;
    private static final int NOTIFY_ID = 0;
    private NotificationManager mNotificationManager;
    /* 下载包安装路径 */
    private String savePath;
    private String saveFileName;
    private String apkUrl;
    private int drawableId;
    private int progress;
    boolean canceled;
    private Thread downLoadThread;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        savePath = FileUtil.getSaveFilePath(this, Constant.SDCARD_CACHE);
        saveFileName = savePath + "xiaojing.apk";
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.hasExtra("drawableId")) {
            drawableId = intent.getIntExtra("drawableId", 0);
        }

        if (intent.hasExtra("url")) {
            apkUrl = (String) intent.getExtras().get("url");
        }
        progress = 0;
        setUpNotification();
        new Thread() {
            public void run() {
                // 开始下载
                startDownload();
            }

            ;
        }.start();

        return startId;

    }

    ;

    private void startDownload() {
        canceled = false;
        downloadApk();
    }

    private Handler mHandler = new Handler() {

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    // 下载完毕
                    // 取消通知
                    mNotificationManager.cancel(NOTIFY_ID);
                    installApk();
                    break;
                case 2:
                    // 这里是用户界面手动取消，所以会经过activity的onDestroy();方法
                    // 取消通知
                    mNotificationManager.cancel(NOTIFY_ID);
                    break;
                case 1:
                    int rate = msg.arg1;
                    if (rate < 100) {
                        RemoteViews contentview = mNotification.contentView;
                        contentview.setTextViewText(R.id.tv_progress, rate + "%");
                        contentview.setProgressBar(R.id.progressbar, 100, rate, false);
                    } else {
                        // 下载完毕后变换通知形式
                        mNotification.flags = Notification.FLAG_AUTO_CANCEL;
                        mNotification.contentView = null;
//                        mNotification.setLatestEventInfo(mContext, "下载完成", "文件已下载完毕", null);

                        mNotification = new Notification.Builder(mContext)
                                .setContentTitle("下载完成 ")
                                .setContentText("文件已下载完毕")
                                .setSmallIcon(R.drawable.ic_launcher).build();


                        stopSelf();// 停掉服务自身
                    }
                    PendingIntent contentIntent2 = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
                    mNotification.contentIntent = contentIntent2;
                    mNotificationManager.notify(NOTIFY_ID, mNotification);
                    break;
                case 3:
                    mNotification.flags = Notification.FLAG_AUTO_CANCEL;
                    RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.update_download_notification_layout);
                    contentView.setTextViewText(R.id.name, "下载失败");
                    // 指定个性化视图
                    mNotification.contentView = contentView;
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                    // 指定内容意图
                    mNotification.contentIntent = contentIntent;
//                    mNotificationManager.notify(NOTIFY_ID, mNotification);

                    stopSelf();// 停掉服务自身
                    break;

            }
        }
    };

    /**
     * 安装apk
     *
     * @param
     */
    private void installApk() {
        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);

        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= 24) {
            //provider authorities
            File apkPath = new File(Environment.getExternalStorageDirectory(), Constant.SDCARD_CACHE);
            File newFile = new File(apkPath, "xiaojing.apk");
            Uri apkUri = FileProvider.getUriForFile(context, "com.xiaojing.shop.fileprovider", newFile);
            //Granting Temporary Permissions to a URI
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(apkfile);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        }


        mContext.startActivity(intent);
    }

    private int lastRate = 0;
    private InputStream is = null;
    private FileOutputStream fos = null;

    /**
     * 下载apk
     *
     * @param
     */
    private void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }

    private Runnable mdownApkRunnable = new Runnable() {

        @Override
        public void run() {
            try {
                URL url = new URL(apkUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                int length = conn.getContentLength();
                is = conn.getInputStream();

                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                String apkFile = saveFileName;
                File ApkFile = new File(apkFile);
                fos = new FileOutputStream(ApkFile);

                int count = 0;
                byte buf[] = new byte[1024];

                do {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    // 更新进度
                    Message msg = mHandler.obtainMessage();
                    msg.what = 1;
                    msg.arg1 = progress;
                    if (progress >= lastRate + 1) {
                        mHandler.sendMessage(msg);
                        lastRate = progress;
                    }
                    if (numread <= 0) {
                        mHandler.sendEmptyMessage(0);// 下载完成通知安装
                        // 下载完了，cancelled也要设置
                        canceled = true;
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (!canceled);// 点击取消就停止下载.

                fos.close();
                is.close();
            } catch (Exception e) {

                Message msg = mHandler.obtainMessage();
                msg.what = 3;
                mHandler.sendMessage(msg);
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                    is.close();
                    if (is != null) {
                        is.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    };

    /**
     * 创建通知
     */
    private void setUpNotification() {
        int icon = R.drawable.ic_launcher;
        CharSequence tickerText = "开始下载";
        long when = System.currentTimeMillis();
        mNotification = new Notification(icon, tickerText, when);
        ;
        // 放置在"正在运行"栏目中
        mNotification.flags = Notification.FLAG_ONGOING_EVENT;

        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.update_download_notification_layout);
        contentView.setTextViewText(R.id.name, notify_name);
        contentView.setImageViewResource(R.id.image, drawableId);
        // 指定个性化视图
        mNotification.contentView = contentView;

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);

        // 指定内容意图
        mNotification.contentIntent = contentIntent;
        mNotificationManager.notify(NOTIFY_ID, mNotification);
    }
}
