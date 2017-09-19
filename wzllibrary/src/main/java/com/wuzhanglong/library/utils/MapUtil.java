package com.wuzhanglong.library.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import java.io.File;

/**
 * Created by ${Wuzhanglong} on 2017/5/15.
 */

public class MapUtil {
    public static void guide(Context context, String latitude, String longitude, String desName) {
        Intent intent;
        String url;
        //com.autonavi.minimap  高德包 com.baidu.BaiduMap百度报名
        try {
            if (isInstallByread("com.baidu.BaiduMap")) {
                intent = new Intent();
                url = "baidumap://map/direction?destination=" + latitude + "," + longitude + "&mode=driving&coord_type=gcj02&output=";
                Uri uri = Uri.parse(url);
                intent.setData(uri);
            } else {
                if (isInstallByread("com.autonavi.minimap")) {
                    intent = Intent.getIntent("androidamap://navi?&poiname=" + desName + "&lat=" + latitude + "&lon=" + longitude + "&dev=0");
                } else {
                    url = "http://uri.amap.com/navigation?to=" + longitude + "," + latitude + "," + desName + "&&mode=car";
                    intent = Intent.getIntent(url);
                }
            }
            context.startActivity(intent); //启动调用
        } catch (Exception e) {
//            Uri uri = Uri.parse("http://m.amap.com/?to=" + latitude + "," + longitude + "(" + desName + ")&type=0&opt=1");
//            intent = new Intent(Intent.ACTION_VIEW, uri);
            Toast.makeText(context,"请下载高德地图或百度地图",Toast.LENGTH_LONG);
        }

    }

    private static boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }
}
