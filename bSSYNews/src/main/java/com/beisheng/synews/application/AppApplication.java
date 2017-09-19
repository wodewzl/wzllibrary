
package com.beisheng.synews.application;

import android.content.Context;
import android.util.Log;

import com.beisheng.base.application.BaseAppApplication;
import com.beisheng.base.utils.FileUtil;
import com.beisheng.synews.mode.UserInfoVO;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cn.jpush.android.api.JPushInterface;

public class AppApplication extends BaseAppApplication {
    private static AppApplication mAppApplication;
    public static UserInfoVO mUserInfoVO;
    public static String mUid;
    private static String mSessionid;

    @Override
    public void onCreate() {
        // super.onCreate();
        initImageLoader(getApplicationContext());
        mAppApplication = this;
        initShareSdk();
        initSpeech();

        JPushInterface.setDebugMode(false); // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this); // 初始化 JPus
        JPushInterface.resumePush(getApplicationContext());
        // 设置alias
        // JPushInterface.setAlias(getApplicationContext(),
        // "wuzhanglong", new TagAliasCallback() {
        // @Override
        // public void gotResult(int arg0,
        // String arg1, Set<String> arg2) {
        // }
        // });
//        Boolean isFirstIn = false;
//        SharedPreferences pref = mAppApplication.getSharedPreferences("myActivityName", 0);
//        //取得相应的值，如果没有该值，说明还未写入，用true作为默认值
//        isFirstIn = pref.getBoolean("isFirstIn", true);
//
//        if(isFirstIn){
//            final DataCleanUtil dataClean = new DataCleanUtil(this);
//            dataClean.cleanApplicationData(mAppApplication, FileUtil.getSaveFilePath(mAppApplication));
//        }






    }

    /** 获取Application */
    public static AppApplication getInstance() {
        return mAppApplication;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        // 整体摧毁的时候调用这个方法
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

    public String getUid() {
        if (mUid == null && getUserInfoVO() != null) {
            return getUserInfoVO().getUid();
        }
        return mUid;
    }

    public String getSessionid() {
        if (mSessionid == null && getUserInfoVO() != null) {
            return getUserInfoVO().getSessionid();
        }
        return mSessionid;
    }

    /** 初始化ImageLoader */
    public static void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, FileUtil.getSaveFilePath(context));// 获取到缓存的目录地址
        Log.d("cacheDir", cacheDir.getPath());
        // 创建配置ImageLoader(所有的选项都是可选的,只使用那些你真的想定制)，这个可以设定在APPLACATION里面，设置为全局的配置参数
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(context)
                        // .memoryCacheExtraOptions(480, 800) // max width, max
                        // height，即保存的每个缓存文件的最大长宽
                        // .discCacheExtraOptions(480, 800, CompressFormat.JPEG, 75, null) // Can
                        // slow ImageLoader, use it carefully (Better don't use
                        // it)设置缓存的详细信息，最好不要设置这个
                        .threadPoolSize(3)// 线程池内加载的数量
                        .threadPriority(Thread.NORM_PRIORITY - 2)
                        .denyCacheImageMultipleSizesInMemory()
                        // .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can
                        // pass your own memory cache implementation你可以通过自己的内存缓存实现
                        // .memoryCacheSize(2 * 1024 * 1024)
                        // /.discCacheSize(50 * 1024 * 1024)
                        .discCacheFileNameGenerator(new Md5FileNameGenerator())// 将保存的时候的URI名称用MD5
                                                                               // 加密
                        // .discCacheFileNameGenerator(new
                        // HashCodeFileNameGenerator())//将保存的时候的URI名称用HASHCODE加密
                        .tasksProcessingOrder(QueueProcessingType.LIFO)
                        // .discCacheFileCount(100) //缓存的File数量
                        .discCache(new UnlimitedDiskCache(cacheDir))
                        // .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                        // .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000))
                        // // connectTimeout (5 s), readTimeout (30 s)超时时间
                        .writeDebugLogs() // Remove for release app
                        .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);// 全局初始化此配置
    }

    public void initShareSdk() {
        // 微信 appid appsecret
        PlatformConfig.setWeixin("wx75607df1b6a14070", "44a0ef03ecc91191e0b132282a700475");
        // QQ appid appsecret
        PlatformConfig.setQQZone("1101085382", "rIuoc3dttpBSwhMf");
        // 新浪微博 appkey appsecret
        // PlatformConfig.setSinaWeibo("3198482537", "506cf1963ee641c6ea2411b7ea43bfb5");
        PlatformConfig.setSinaWeibo("4019389331", "577447fe32a39b9be00caec6603369e3");
        Config.REDIRECT_URL = "http://m.10yan.com";

    }

    public void initSpeech() {
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=578f362e");
    }

}
