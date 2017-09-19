package com.bs.luzhongbao;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;
//import com.uuzuche.lib_zxing.activity.ZXingLibrary;

/**
 * Created by Administrator on 2017/3/21.
 */

public class AppApplication extends Application{


    @Override
    public void onCreate() {
        super.onCreate();

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

    }
}
