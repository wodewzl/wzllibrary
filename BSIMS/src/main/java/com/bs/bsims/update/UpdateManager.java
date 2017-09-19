
package com.bs.bsims.update;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.MainActivity;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.utils.CustomToast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UpdateManager {

    private Context context;

    // 提示语
    private String updateMsg = "有最新的软件包哦，亲快下载吧~";

    // 返回的安装包url
    // private String apkUrl = "http://erp.baiteng.org/Uploads/Update/Android/xunke_oa_v1.2.1.apk";

    // 下载包安装路径
    private static final String savePath = Constant.FileInfo.UPDATE_PATH;

    private static final String saveFileName = savePath + "oa.apk";

    // 进度条与通知ui刷新的handler和msg常量
    private ProgressBar mProgress;
    private TextView txt_progress;

    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private int progress;
    private boolean interceptFlag = false;

    public UpdateManager(Context context) {
        this.context = context;
    }

    // 外部接口让主Activity调用
    public void checkUpdateInfo(Context context, String number, String minimum, String address) {

        if (Integer.parseInt(minimum) > VersionUtils.getversionCode(context)) {// 最低版本

            differentCheck(true, context, address);

        } else if (Integer.parseInt(number) > VersionUtils.getversionCode(context)) {// 服务器版本

            differentCheck(false, context, address);

        } else {

            Intent intent = new Intent();
            intent.setClass(context, MainActivity.class);
            context.startActivity(intent);
            ((Activity) context).finish();
        }
    }

    private void differentCheck(final boolean b, final Context context, final String address) {

        AlertDialog.Builder builder = new Builder(context);
        builder.setCancelable(false);
        builder.setTitle("软件版本更新");
        builder.setMessage(updateMsg);
        builder.setPositiveButton("下载", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showDownloadDialog(address);
            }
        });
        builder.setNegativeButton("以后再说", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                if (b) {
                    CustomToast.showLongToast(context, "亲，你的版本过低，请更新后再使用！");
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            System.exit(0);
                        }
                    }, 2000);

                } else {

                    Intent intent = new Intent();
                    intent.setClass(context, MainActivity.class);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }

            }
        }).create().show();

    }

    private void showDownloadDialog(String address) {
        AlertDialog.Builder builder = new Builder(context);
        builder.setTitle("软件版本更新");

        final LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.ac_progress_change, null);
        mProgress = (ProgressBar) v.findViewById(R.id.progress);
        txt_progress = (TextView) v.findViewById(R.id.txt_progress);

        builder.setView(v);
        builder.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                interceptFlag = true;
            }
        }).create().show();

        downloadApk(address);
    }

    private void downloadApk(final String address) {
        new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    URL url = new URL(address);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    int length = conn.getContentLength();
                    InputStream is = conn.getInputStream();

                    File file = new File(savePath);
                    if (!file.exists()) {
                        file.mkdir();
                    }

                    String apkFile = saveFileName;
                    File ApkFile = new File(apkFile);
                    FileOutputStream fos = new FileOutputStream(ApkFile);

                    int count = 0;
                    byte buf[] = new byte[1024];

                    do {
                        int numread = is.read(buf);
                        count += numread;
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        mHandler.sendEmptyMessage(DOWN_UPDATE);
                        if (numread <= 0) {
                            // 下载完成通知安装
                            mHandler.sendEmptyMessage(DOWN_OVER);
                            break;
                        }
                        fos.write(buf, 0, numread);
                    } while (!interceptFlag);// 点击取消就停止下载.

                    fos.close();
                    is.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    mProgress.setProgress(progress);
                    txt_progress.setText(progress + "%");
                    break;
                case DOWN_OVER:

                    installApk();
                    break;
            }
        }
    };

    private void installApk() {
        File apkfile = new File(saveFileName);

        if (!apkfile.exists())
            return;

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        context.startActivity(i);

    }
}
