
package com.wuzhanglong.sendmessage;


import android.content.Context;
import android.support.multidex.MultiDex;

import com.wuzhanglong.library.application.BaseAppApplication;

public class AppApplication extends BaseAppApplication {
    private static AppApplication mAppApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppApplication = this;
    }

    /**
     * 获取Application
     */
    public static AppApplication getInstance() {
        return mAppApplication;
    }



    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
