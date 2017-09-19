
package com.bs.bsims.update;

import android.app.ProgressDialog;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadUtil {
    private static String TAG = "DownloadUtil";

    private static String newApkPath;

    public static File download(String serverPath, String savePath, ProgressDialog progressDialog) {

        // String lastName = serverPath.substring(serverPath.lastIndexOf("."));
        // savePath = savePath.replace(lastName, ".apk");

        newApkPath = savePath;
        try {
            URL url = new URL(serverPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            int code = conn.getResponseCode();
            if (code == 200) {
                int max = conn.getContentLength();
                progressDialog.setMax(max);

                InputStream is = conn.getInputStream();

                /*
                 * File file2 = new File(savePath); Log.e(TAG, "savePath ====" + savePath);
                 * if(!file2.exists()){ file2.mkdirs(); File f = new
                 * File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/oa/update");
                 * CustomLog.e(TAG, "miui f创建路径： " +
                 * Environment.getExternalStorageDirectory().getAbsolutePath() + "/oa/update");
                 * f.mkdirs(); }
                 */

                Log.e(TAG, "====" + savePath);
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/oa.apk");

                if (!file.exists()) {
                    file.createNewFile();
                }

                // 直接下载在root目录中
                FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/oa.apk");
                int len = 0;
                byte[] buffer = new byte[1024];
                int totle = 0;

                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    totle += len;
                    progressDialog.setProgress(totle);
                }
                fos.flush();
                fos.close();
                is.close();
                return file;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
