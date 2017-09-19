package com.bs.aikushi;

import android.app.Application;

import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import cn.jpush.android.api.JPushInterface;
//import com.uuzuche.lib_zxing.activity.ZXingLibrary;

/**
 * Created by Administrator on 2017/3/21.
 */

public class AppApplication extends Application{
    {
        PlatformConfig.setWeixin("wxf9dd79182d5acfe5", "00c95bdd06bf520d5c28be9670fb9187");
        PlatformConfig.setQQZone("1105989423", "NpBdaAVqRO7vRi5G");
        PlatformConfig.setSinaWeibo("463273999", "bc9251113d0607eb0efecc2739cd65a7", "https://www.baidu.com");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        UMShareAPI.get(this);
//        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

    }
}
