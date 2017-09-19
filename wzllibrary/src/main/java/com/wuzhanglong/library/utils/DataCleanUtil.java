
package com.wuzhanglong.library.utils;

/*  * 文 件 名:  DataCleanManager.java  * 描    述:  主要功能有清除内/外缓存，清除数据库，清除sharedPreference，清除files和清除自定义目录  */

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.text.DecimalFormat;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 本应用数据清除管理器
 */
public class DataCleanUtil {
    private Context mContext;
    private long mSize = 0;

    public DataCleanUtil(Context context) {
        this.mContext = context;
    }

    /**
     * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * * @param context
     */
    public void cleanInternalCache(Context context) {
        deleteFilesByDirectory(context.getCacheDir());
    }

    /**
     * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases) * * @param context
     */
    public void cleanDatabases(Context context) {
        deleteFilesByDirectory(new File("/data/data/"
                + context.getPackageName() + "/databases"));
    }

    /**
     * * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs) * * @param context
     */
    public void cleanSharedPreference(Context context) {
        deleteFilesByDirectory(new File("/data/data/"
                + context.getPackageName() + "/shared_prefs"));
    }

    /**
     * 按名字清除本应用数据库 * * @param context * @param dbName
     */
    public void cleanDatabaseByName(Context context, String dbName) {
        context.deleteDatabase(dbName);
    }

    /**
     * 清除/data/data/com.xxx.xxx/files下的内容 * * @param context
     */
    public void cleanFiles(Context context) {
        deleteFilesByDirectory(context.getFilesDir());
    }

    /**
     * * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache) * * @param context
     */
    public void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }

    /**
     * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除 * * @param filePath
     */
    public long cleanCustomCache(String filePath) {
        return deleteFilesByDirectory(new File(filePath));
    }

    /**
     * 清除本应用所有的数据 * * @param context * @param filepath
     */
    public void cleanApplicationData(Context context, String... filepath) {
        cleanInternalCache(context);
        cleanExternalCache(context);
//         cleanDatabases(context);
        // cleanSharedPreference(context);
//         cleanFiles(context);
        for (String filePath : filepath) {
            cleanCustomCache(filePath);
        }
    }

    /**
     * 清除本应用所有的数据 * * @param context * @param filepath
     */
    public void cleanApplicationData(Context context) {
        cleanInternalCache(context);
        cleanExternalCache(context);
        cleanDatabases(context);
        // cleanSharedPreference(context);
        cleanFiles(context);
    }

    /**
     * 不清楚数据库 * * @param context * @param filepath
     */
    public void cleanApplicationCache(Context context) {
        cleanInternalCache(context);
        cleanExternalCache(context);
        // cleanDatabases(context);
        // cleanSharedPreference(context);
        cleanFiles(context);
    }

    public void cleanApplicationAllData(Context context) {
        cleanInternalCache(context);
        cleanExternalCache(context);
        cleanDatabases(context);
        cleanSharedPreference(context);
        cleanFiles(context);
    }

    public void cleanApplicationAllDataSize(Context context) {
        cleanInternalCache(context);
        cleanExternalCache(context);
        cleanDatabases(context);
        cleanSharedPreference(context);
        cleanFiles(context);
    }

    /**
     * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * * @param directory
     */
    private long deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                mSize += item.length();
                item.delete();
            }
        }
        return mSize;
    }

    public String getFileSize() {
        DecimalFormat df = new DecimalFormat("###.##");
        float f = ((float) mSize / (float) (1024 * 1024));

        if (f < 1.0) {
            float f2 = ((float) mSize / (float) (1024));

            return df.format(new Float(f2).doubleValue()) + "KB";

        } else {
            return df.format(new Float(f).doubleValue()) + "M";
        }

    }


    public void cleanDataCache(final Context context, final String filePath) {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("确定要清除缓存吗?")
                .setConfirmText("确定")
                .setCancelText("取消")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        cleanApplicationData(context, FileUtil.getSaveFilePath(context, filePath));
                        sDialog.setTitleText("删除成功")
                                .setContentText("当前清除" + getFileSize() + "文件缓存")
                                .setConfirmText("确定")
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
//                        ((BaseActivity)context).showCustomToast("当前清除" + getFileSize() + "文件缓存");
                    }
                })
                .show();
    }

    public String getCacheSize(Context context,File directory) {
        getCacheSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            getCacheSize(context.getExternalCacheDir());

        }
       return getCacheSize(directory);
    }


    public String getCacheSize(File directory) {

        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                mSize += item.length();
            }
        }
        return getFileSize();
    }

}
