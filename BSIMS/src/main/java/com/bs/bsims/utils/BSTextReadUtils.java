
package com.bs.bsims.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.io.File;
import java.util.List;

public class BSTextReadUtils {
    private static Context bSTextReadUtilscContext;

    public static Intent openFile(String filePath, Context context) {
        bSTextReadUtilscContext = context;
        File file = new File(filePath);
        if (!file.exists())
            return null;
        /* 取得扩展名 */
        String end = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).toLowerCase();
        /* 依扩展名的类型决定MimeType */
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") ||
                end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            return getAudioFileIntent(filePath);
        } else if (end.equals("3gp") || end.equals("mp4") || end.equals("avi") || end.equals("rm")) {
            return getVideoFileIntent(filePath);
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") ||
                end.equals("jpeg") || end.equals("bmp")) {
            return getImageFileIntent(filePath);
        } else if (end.equals("apk")) {
            return getApkFileIntent(filePath);
        } else if (end.equals("ppt")) {
            return getPptFileIntent(filePath);
        } else if (end.equals("xls") || end.equals("xlsx")) {
            return getExcelFileIntent(filePath);
        } else if (end.equals("doc") || end.equals("docx")) {
            return getWordFileIntent(filePath);
        } else if (end.equals("pdf")) {
            return getPdfFileIntent(filePath);
        } else if (end.equals("chm")) {
            return getChmFileIntent(filePath);
        } else if (end.equals("txt")) {
            return getTextFileIntent(filePath, false);
        }
        return null;
    }

    // Android获取一个用于打开APK文件的intent
    public static Intent getAllIntent(String param) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "*/*");
        return intent;
    }

    // Android获取一个用于打开APK文件的intent
    public static Intent getApkFileIntent(String param) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return intent;
    }

    // Android获取一个用于打开VIDEO文件的intent
    public static Intent getVideoFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "video/*");
        return intent;
    }

    // Android获取一个用于打开AUDIO文件的intent
    public static Intent getAudioFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "audio/*");

        return intent;
    }

    // Android获取一个用于打开Html文件的intent
    public static Intent getHtmlFileIntent(String param) {

        Uri uri = Uri.parse(param).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    // Android获取一个用于打开图片文件的intent
    public static Intent getImageFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    // Android获取一个用于打开PPT文件的intent
    public static Intent getPptFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

    // Android获取一个用于打开Excel文件的intent
    public static Intent getExcelFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    // Android获取一个用于打开Word文件的intent
    public static Intent getWordFileIntent(String param) {
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/msword");
        // isAvilible(bSTextReadUtilscContext, "cn.wps.moffice");
        // isAvilible(bSTextReadUtilscContext, "cn.wps.moffice.documentmanager.PreStartActivity");
        return intent;
    }

    // Android获取一个用于打开CHM文件的intent
    public static Intent getChmFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    // Android获取一个用于打开文本文件的intent
    public static Intent getTextFileIntent(String param, boolean paramBoolean) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (paramBoolean) {
            Uri uri1 = Uri.parse(param);
            intent.setDataAndType(uri1, "text/plain");
        } else {
            Uri uri2 = Uri.fromFile(new File(param));
            intent.setDataAndType(uri2, "text/plain");
        }
        return intent;
    }

    // Android获取一个用于打开PDF文件的intent
    public static Intent getPdfFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(param));
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }

    // Android获取一个用于打开Zip文件的intent ACTION_INTERNAL_STORAGE_SETTINGS
    private static Intent getZipFileIntent(String param) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClassName("com.android.phone", "ACTION_INTERNAL_STORAGE_SETTINGS");
        // intent.setClassName("com.rarlab.rar", "com.rarlab.rar.MainActivity");
        // try {
        // startActivity(intent);
        // } catch (ActivityNotFoundException e) {
        // try {
        // Uri uri = Uri.parse("market://search?q=" + "rar");
        // Intent it = new Intent(Intent.ACTION_VIEW, uri);
        // startActivity(it);
        // } catch (ActivityNotFoundException e2) {
        // Log.i(TAG, "market no found");
        // }
        // }
        return intent;
    }

    // public static void CheckIsHavingOfficeAPP(final Context contenxt) {
    // // WPS Office
    // // com.chinatelecom.bestpayclient
    // boolean AppState = false;
    // List<PackageInfo> list =
    // contenxt.getPackageManager().getInstalledPackages(PackageManager.GET_PERMISSIONS);
    // for (PackageInfo packageInfo : list) {
    // // stringBuilder.append("package name:" + packageInfo.packageName + "\n");
    // ApplicationInfo applicationInfo = packageInfo.applicationInfo;
    // // stringBuilder.append("应用名称:" +
    // // applicationInfo.loadLabel(contenxt.getPackageManager())
    // // + "\n");
    // if (packageInfo.packageName.equals("com.chinatelecom.bestpayclient") ||
    // applicationInfo.loadLabel(contenxt.getPackageManager()).equals("WPS Office")) {
    // // 判断是否存在office这个报名
    // CustomLog.e("OfficeAPP", applicationInfo.loadLabel(contenxt.getPackageManager()) + "");
    // AppState = true;
    // break;
    // }
    // // 就证明没有用户没有下载这个app 则引导用户去下载这个app
    // }
    // // 没有找到这个wps软件
    // if (!AppState) {
    //
    // View isoffice = View.inflate(contenxt, R.layout.ishaveofficeapp, null);
    // // 这里来个系统提醒
    // BSDialog bsd = new BSDialog(contenxt, "北企星系统提醒您!", isoffice, new OnClickListener() {
    // @Override
    // public void onClick(View arg0) {
    // // TODO Auto-generated method stub
    // Intent i = new Intent(Intent.ACTION_VIEW);
    // i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    // i.setDataAndType(Uri.parse("http://a.myapp.com/o/simple.jsp?pkgname=cn.wps.moffice_eng&g_f=992084"),
    // "application/vnd.android.package-archive");
    // contenxt.startActivity(i);
    // }
    // });
    // bsd.show();
    // }
    // }
    // private void doStartApplicationWithPackageName(String packagename, Context context) {
    //
    // // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
    // PackageInfo packageinfo = null;
    // try {
    // packageinfo = context.getPackageManager().getPackageInfo(packagename, 0);
    // } catch (NameNotFoundException e) {
    // e.printStackTrace();
    // }
    // if (packageinfo == null) {
    // return;
    // }
    //
    // // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
    // Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
    // resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
    // resolveIntent.setPackage(packageinfo.packageName);
    //
    // // 通过getPackageManager()的queryIntentActivities方法遍历
    // List<ResolveInfo> resolveinfoList = context.getPackageManager()
    // .queryIntentActivities(resolveIntent, 0);
    //
    // ResolveInfo resolveinfo = resolveinfoList.iterator().next();
    // if (resolveinfo != null) {
    // // packagename = 参数packname
    // String packageName = resolveinfo.activityInfo.packageName;
    // // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
    // String className = resolveinfo.activityInfo.name;
    // // LAUNCHER Intent
    // Intent intent = new Intent(Intent.ACTION_MAIN);
    // intent.addCategory(Intent.CATEGORY_LAUNCHER);
    //
    // // 设置ComponentName参数1:packagename参数2:MainActivity路径
    // ComponentName cn = new ComponentName(packageName, className);
    //
    // intent.setComponent(cn);
    // context.startActivity(intent);
    // }
    // }
    public static boolean isAvilible(Context context)
    {
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++)
        {

            if (pinfo.get(i).packageName.contains("wps") || pinfo.get(i).packageName.contains("office"))
            {
                return true;
            }
            // return true;

        }
        return false;
    }

    // // 匹配字符
    // public static String ContiansRelpace(String contextdeatil) {
    // String str = "";
    // if (contextdeatil.contains(".")) {
    // if (contextdeatil.indexOf(".") != 0 || contextdeatil.indexOf(".") !=
    // contextdeatil.length() - 1)
    // {
    // int indexpiont = contextdeatil.indexOf(".");
    //
    // CustomLog.e("aa", "有点 并找到下标。");
    // if ((contextdeatil.charAt(indexpiont - 1) + "").matches("[0-9]+") &&
    // (contextdeatil.charAt(indexpiont + 1) + "").matches("^[A-Za-z]+$")) {
    // str = contextdeatil.substring(0, indexpiont) + " " + contextdeatil.substring(indexpiont);
    // }
    //
    // }
    // else {
    // return str.replaceAll("F1", "/r");
    // }
    //
    // }
    // else {
    // return str.replaceAll("F1", "/r");
    // }
    //
    // return ContiansRelpace(str);
    // }
}
