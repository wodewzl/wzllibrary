
package com.xiaojing.shop.application;


import android.content.Context;
import android.support.multidex.MultiDex;

import com.umeng.socialize.PlatformConfig;
import com.wuzhanglong.library.application.BaseAppApplication;
import com.xiaojing.shop.mode.UserInfoVO;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cn.jpush.android.api.JPushInterface;

public class AppApplication extends BaseAppApplication {
    private static AppApplication mAppApplication;
    {
        PlatformConfig.setWeixin("wx93027a99f78841b5","12f61fc306634e3470d2b61de397296b");
        PlatformConfig.setQQZone("1106176952","KEYRhCNnGRnsBJ7b9tN");
        PlatformConfig.setSinaWeibo("319845988","fbb3df56985bcd557975e1bd8e8d8a1e","http://xiaojingsc.test.beisheng.wang/ht");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAppApplication = this;
//        RxUtils.init(this);
//        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    /** 获取Application */
    public static AppApplication getInstance() {
        return mAppApplication;
    }


    public UserInfoVO getUserInfoVO() {
        try {
            FileInputStream stream = this.openFileInput("data.UserInfoVO");
            ObjectInputStream ois = new ObjectInputStream(stream);
            UserInfoVO userInfoVO = (UserInfoVO) ois.readObject();
            return userInfoVO;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveUserInfoVO(UserInfoVO userInfoVO) {
        try {
            FileOutputStream stream = this.openFileOutput("data.UserInfoVO", MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(stream);
            oos.writeObject(userInfoVO);// td is an Instance of TableData;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
