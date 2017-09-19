
package com.bs.bsims.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.bs.bsims.utils.LocationUtils;

import java.io.File;
import java.io.FileOutputStream;

public class LogistiRecevierOfGaode extends BroadcastReceiver
{
    private Context revercontext;
    private String TAG = "LogistiRecevierOfGaode";
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;

    @Override
    // 1450155978922 1450159793012
    public void onReceive(Context context, Intent arg1) {
        revercontext = context;
        // 大于5分钟唤醒
        if (checkTimeIsOverFive(Long.parseLong(LocationUtils.getServiceTime(context)),
                System.currentTimeMillis())) {

            if (mLocationClient == null)
                // 初始化定位
                mLocationClient = new AMapLocationClient(context.getApplicationContext());
            // 初始化定位参数
            if (mLocationOption == null)
                mLocationOption = new AMapLocationClientOption();

            LocationUtils.getLatLonForService(context.getApplicationContext(), mLocationClient,
                    mLocationOption);
        }

    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    private void writeFileToSD(String stra) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            Log.d("TestFile", "SD card is not avaiable/writeable right now.");
            return;
        }
        try {
            String pathName = "/sdcard/test/";
            String fileName = "file.txt";
            File path = new File(pathName);
            File file = new File(pathName + fileName);
            if (!path.exists()) {
                path.mkdir();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream stream = new FileOutputStream(file);
            String s = stra;
            byte[] buf = s.getBytes();
            stream.write(buf);
            stream.close();

        } catch (Exception e) {
            Log.e("TestFile", "Error on writeFilToSD.");
            e.printStackTrace();
        }
    }

    // 计算时间差
    public boolean checkTimeIsOverFive(long lastTime, long realTime) {
        if ((realTime - lastTime) > 3 * 60 * 1000) {// 如果大于三分钟
            return true;
        }
        else {
            return false;
        }
    }
}
