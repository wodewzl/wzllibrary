/**                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             
 * 
 */

package com.beisheng.base.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;

import com.beisheng.base.R;
import com.beisheng.base.application.BaseAppApplication;

/**
 * BS北盛最帅程序员 Copyright (c) ` 016 湖北北盛科技有限公司
 * 
 * @author 梁骚侠
 * @date 2016-5-3
 * @version 2.0
 */
public class BsPermissionUtils {
    private static AlertDialog alertDialog = null;

    private static Intent getAppDetailSettingIntent() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        localIntent.setData(Uri.fromParts("package", BaseAppApplication.getInstance().getPackageName(), null));
        return localIntent;
    }

    // 检测是否打开语音权限
    public static boolean checkIsOpenVoice(final Activity context) {
        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(context)
                    .setIcon(R.drawable.ic_launcher)
                    .setTitle(R.string.app_name) //
                    .setMessage("请打开设置中权限管理开启麦克风权限").setPositiveButton("设置", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 先判断当前系统版本
                            if (android.os.Build.VERSION.SDK_INT > 10) { // 3.0以上
                                context.startActivity(getAppDetailSettingIntent());
                                alertDialog = null;
                            }
                        }
                    }).setNegativeButton("知道了", new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog = null;
                        }
                    }).create();
            alertDialog.show();
        } else {
            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }
        }
        return true;
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     * 
     * @param context
     * @return true 表示开启
     */
    public static final boolean isOPenGps(final Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }

    /**
     * 强制帮用户打开GPS
     * 
     * @param context
     */
    public static final void openGPS(final Context context) {
        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(context)
                    .setIcon(R.drawable.ic_launcher)
                    .setTitle(R.string.app_name) //
                    .setMessage("请打开设置开启GPS提高定位的精准度").setPositiveButton("设置", new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            try {
                                context.startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            alertDialog = null;
                        }
                    }).setNegativeButton("知道了", new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog = null;
                        }
                    }).create();
            alertDialog.show();
        } else {
            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }
        }

    }

    /*
     * 检测是否有相机权限
     */
    /** Check if this device has a camera */
    public static boolean checkCameraHardware(Context context)
    {
        if (context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA))
        {
            // this device has a camera
            return true;
        }
        else
        {
            // no camera on this device
            return false;
        }
    }

    /**
     * 跳转到当前应用的位置信息权限
     **/

    public static void getAddressInfo(final Context context) {
        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(context)
                    .setIcon(R.drawable.ic_launcher)
                    .setTitle(R.string.app_name) //
                    .setMessage("请打开设置中权限管理开启定位权限").setPositiveButton("设置", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 先判断当前系统版本
                            if (android.os.Build.VERSION.SDK_INT > 10) { // 3.0以上
                                context.startActivity(getAppDetailSettingIntent());
                                alertDialog = null;
                            }
                        }
                    }).setNegativeButton("知道了", new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog = null;
                        }
                    }).create();
            alertDialog.show();
        } else {
            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }
        }
    }

}
