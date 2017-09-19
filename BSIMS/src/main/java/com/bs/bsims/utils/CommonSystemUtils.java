
package com.bs.bsims.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.os.Environment;

import java.util.List;

public class CommonSystemUtils {

    /**
     * 判断当前应用程序处于前台还是后台
     * 
     * @param context
     * @return true 在后台，false在前台
     */
    public static boolean isApplicationRunToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查是否有存储卡
     * 
     * @return
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检测软件是否在系统中安装
     * 
     * @param context
     * @param packageName 软件的包名
     * @return 安装了返回true，否则返回false
     */
    public static boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 获取软件的内部版本号
     * 
     * @param context
     * @return 错误返回-1
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 获取软件的外部版本号
     * 
     * @param context
     * @return 错误返回null
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过app的名称得到app的主activity名或包名
     * 
     * @param launchName launch页面上的软件名称
     * @param type 通过type类型确定最后得到的字符串类型 {@link AppNameType}
     * @param context
     * @return
     */
    public static String getPackageNameOrMainActivityName(String launchName, AppNameType type, Context context) {

        PackageManager mPackageManager = context.getPackageManager();
        List<ResolveInfo> mAllApps;

        String tempstr = null;
        String result = null;

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mAllApps = mPackageManager.queryIntentActivities(mainIntent, 0);

        for (int i = 0; i < mAllApps.size(); i++) {
            ResolveInfo info = mAllApps.get(i);
            tempstr = info.loadLabel(mPackageManager).toString();
            if (tempstr.equals(launchName)) {
                switch (type) {
                    case packageName:
                        result = info.activityInfo.packageName; // 得到包名
                        break;
                    case activityName:
                        result = info.activityInfo.name; // 得到主activity名
                        break;
                }
            }
        }
        return result;

    }

    /**
     * 判断应用程序是否在运行
     * 
     * @param context
     * @param packageName
     * @return
     */
    public static boolean appIsRun(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> list = am.getRunningTasks(100);
        for (RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(packageName) && info.baseActivity.getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    public static void openService(Context context, String packageName, String serviceClassName) {
        Intent intent = new Intent();
        intent.setClassName(packageName, serviceClassName);
        intent.setAction("android.intent.action.VIEW");
        context.startService(intent);
    }

    /**
     * 获取应用程序包名
     * 
     * @param context
     * @return
     */
    public static String getAppPackageName(Context context) {
        String packageName = context.getApplicationContext().getPackageName();
        return packageName;
    }

    private enum AppNameType {

        /** 应用程序包名 */
        packageName,

        /** 应用程序主Activity名 */
        activityName
    }

    /**
     * 4.4沉浸菜单 头部head高度变高的方法
     * 
     * @param context
     * @return
     */

    // public static void changeHeadFourFragment(View view) {
    // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
    // // 是否支持沉浸式菜单栏
    // // 如果支持把当前的head高度加高
    // view.getLayoutParams().height += view.getLayoutParams().height / 3;
    // //把内部布局整体往下调整
    // try{
    // TextView voAnmae=(TextView) view.findViewById(R.id.txt_comm_head_activityName);
    // TextView voARight=(TextView) view.findViewById(R.id.txt_comm_head_right);
    // voAnmae.setPadding(0, 30, 0, 0);
    // voARight.setPadding(0, 30, 0, 0);
    // }
    // catch(Exception e){
    // e.printStackTrace();
    // }
    //
    // }
    // }
}
