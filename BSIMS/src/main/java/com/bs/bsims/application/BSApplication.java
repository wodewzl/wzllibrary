
package com.bs.bsims.application;

import android.annotation.TargetApi;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.view.WindowManager;

import com.bs.bsims.chatutils.IMJavaBean;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.LoginUser;
import com.bs.bsims.model.ResultVO;
import com.bs.bsims.model.UserFromServerVO;
import com.bs.bsims.utils.CommonUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.yzxIM.IMManager;
import com.yzxtcp.UCSManager;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.xutils.x;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cn.jpush.android.api.JPushInterface;

public class BSApplication extends Application {
    private static BSApplication mBSApplication;
    private HttpClient mHttpClient;
    private String mCompany;
    // private Login4GetHostVO login4GetHostVO;
    private UserFromServerVO userFromServerVO;
    private LoginUser loginUserVO;
    private String userId;
    private String httpTitle;
    private String version;
    private String approvalBoss = "0";
    private String TaskBoss = "0";
    public static NotificationManager nm;
    private SharedPreferences mPreferences;
    private ResultVO resultVO;
    private boolean appliactionChat = false;

    public boolean isAppliactionChat() {
        return appliactionChat;
    }

    public void setAppliactionChat(boolean appliactionChat) {
        this.appliactionChat = appliactionChat;
    }

    public String getApprovalBoss() {
        return CommonUtils.getLimitsSpecial(Constant.LIMITS_SPECIAL001);
    }

    public void setApprovalBoss(String approvalBoss) {
        this.approvalBoss = approvalBoss;
    }

    public String getTaskBoss() {
        return CommonUtils.getLimitsSpecial(Constant.LIMITS_SPECIAL002);
    }

    public void setTaskBoss(String taskBoss) {
        TaskBoss = taskBoss;
    }

    public WindowManager.LayoutParams getWmParams() {
        return wmParams;
    }

    public void setWmParams(WindowManager.LayoutParams wmParams) {
        this.wmParams = wmParams;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onCreate() {
        super.onCreate();
        mBSApplication = this;
//        EMChat.getInstance().init(this);
//        EMChat.getInstance().setDebugMode(false);
        x.Ext.init(this);// Xutils3必须要这里初始化
        initImageLoader(getApplicationContext());
        JPushInterface.setDebugMode(true); // 设置开启日志,发布时请关闭日志
        JPushInterface.init(getApplicationContext()); // 初始化
        nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);// IM聊天的推送
        if (!isApkDebugable(this, this.getPackageName())) {
            // 保存log打开，手机logcat将不会有log，所以调试时要注意
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(getApplicationContext());
        }
        mPreferences = this.getSharedPreferences("user", Context.MODE_PRIVATE);
        UCSManager.init(this);// 启动云之讯聊天服务
        IMManager.getInstance(this);


//        registerActivityLifecycleCallbacks(ActivityLifecycleHelper.build());
    }

    public int getVersionCode() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = this.getPackageManager().getPackageInfo(
                    getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 1;
    }

    public String getVersion() {
        String str = "版本：1.0." + version;

        try {
            String pkName = this.getPackageName();
            String versionName = this.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
            return versionName;
        } catch (Exception e) {
        }
        return str;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getHttpTitle() {
        if (httpTitle == null) {
            return mPreferences.getString("http_title", "");
        }
        return httpTitle;

//         return "http://cp.beisheng.wang/dev_2.0.0/";
//         return "http://baixiang.beisheng.wang/dev_2.0.0/";

    }

    public void setHttpTitle(String httpTitle) {
        this.httpTitle = httpTitle;
    }

    public String getUserId() {
        if (userId == null) {
            return mPreferences.getString("userid", "");
        }
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserFromServerVO getUserFromServerVO() {
        return getServerVO();
    }

    public void setUserFromServerVO(UserFromServerVO userFromServerVO) {
        try {
            FileOutputStream stream = this.openFileOutput("data.serverVO", MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(stream);
            oos.writeObject(userFromServerVO);// td is an Instance of TableData;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getmCompany() {
        if (mCompany == null) {
            return mPreferences.getString("company", "");
        }
        return mCompany;
    }

    public void setmCompany(String mCompany) {
        this.mCompany = mCompany;
    }

    public LoginUser getLoginUserVO() {

        return getUserVO();
    }

    public void setLoginUserVO(LoginUser loginUserVO) {
        // this.loginUserVO = loginUserVO;
        try {
            FileOutputStream stream = this.openFileOutput("data.uservo", MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(stream);
            oos.writeObject(loginUserVO);// td is an Instance of TableData;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static BSApplication getInstance() {
        return mBSApplication;
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        this.shutdownHttpClient();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        this.shutdownHttpClient();
    }

    /**
     * 创建全局变量 全局变量一般都比较倾向于创建一个单独的数据类文件，并使用static静态变量 这里使用了在Application中添加数据的方法实现全局变量
     * 注意在AndroidManifest.xml中的Application节点添加android:name=".MyApplication"属性
     */
    private WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();

    public WindowManager.LayoutParams getWindowParams() {
        return wmParams;
    }

    // 创建HttpClient实例
    private HttpClient createHttpClient() {
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
        HttpProtocolParams.setUseExpectContinue(params, true);
        HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
        HttpConnectionParams.setSoTimeout(params, 20 * 1000);
        HttpConnectionParams.setSocketBufferSize(params, 8192);
        SchemeRegistry schReg = new SchemeRegistry();
        schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

        ClientConnectionManager connMgr = new ThreadSafeClientConnManager(params, schReg);

        return new DefaultHttpClient(connMgr, params);
    }

    // 关闭连接管理器并释放资源
    private void shutdownHttpClient() {
        if (mHttpClient != null && mHttpClient.getConnectionManager() != null) {
            mHttpClient.getConnectionManager().shutdown();
        }
    }

    // 对外提供HttpClient实例
    public HttpClient getHttpClient() {
        return mHttpClient;
    }

    public UserFromServerVO getServerVO() {
        try {
            FileInputStream stream = this.openFileInput("data.serverVO");
            ObjectInputStream ois = new ObjectInputStream(stream);
            UserFromServerVO uservo = (UserFromServerVO) ois.readObject();
            return uservo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userFromServerVO;
    }

    public void saveServerVO(UserFromServerVO uservo) {
        try {
            FileOutputStream stream = this.openFileOutput("data.serverVO", MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(stream);
            oos.writeObject(uservo);// td is an Instance of TableData;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // public Login4GetHostVO getLogin4GetHostVO() {
    // try {
    // FileInputStream stream = this.openFileInput("data.hostVO");
    // ObjectInputStream ois = new ObjectInputStream(stream);
    // Login4GetHostVO hostVO = (Login4GetHostVO) ois.readObject();
    // return hostVO;
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // return login4GetHostVO;
    // }
    //
    // public void setLogin4GetHostVO(Login4GetHostVO login4GetHostVO) {
    // // this.login4GetHostVO = login4GetHostVO;
    // try {
    // FileOutputStream stream = this.openFileOutput("data.hostVO", MODE_PRIVATE);
    // ObjectOutputStream oos = new ObjectOutputStream(stream);
    // oos.writeObject(login4GetHostVO);// td is an Instance of TableData;
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }

    public IMJavaBean getIMjavaBean() {
        try {
            FileInputStream stream = this.openFileInput("data.imvo");
            ObjectInputStream ois = new ObjectInputStream(stream);
            IMJavaBean userVO = (IMJavaBean) ois.readObject();
            return userVO;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveIMjavaBean(IMJavaBean uservo) {
        try {
            FileOutputStream stream = this.openFileOutput("data.imvo", MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(stream);
            oos.writeObject(uservo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LoginUser getUserVO() {
        try {
            FileInputStream stream = this.openFileInput("data.uservo");
            ObjectInputStream ois = new ObjectInputStream(stream);
            LoginUser userVO = (LoginUser) ois.readObject();
            return userVO;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loginUserVO;
    }

    public void saveUserVO(LoginUser uservo) {
        try {
            FileOutputStream stream = this.openFileOutput("data.uservo", MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(stream);
            oos.writeObject(uservo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultVO getResultVO() {
        try {
            FileInputStream stream = this.openFileInput("data.resultvo");
            ObjectInputStream ois = new ObjectInputStream(stream);
            ResultVO resultVO = (ResultVO) ois.readObject();
            return resultVO;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultVO;
    }

    public void setResultVO(ResultVO resultVO) {

        try {
            FileOutputStream stream = this.openFileOutput("data.resultvo", MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(stream);
            oos.writeObject(resultVO);// td is an Instance of TableData;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getCacheDirectory(context);
        ImageLoaderConfiguration config = ImageLoaderConfiguration.createDefault(context);
        ImageLoader.getInstance().init(config);
    }

    public boolean isApkDebugable(Context context, String packageName) {
        try {
            PackageInfo pkginfo = context.getPackageManager().getPackageInfo(packageName, 0);

            if (pkginfo != null) {
                ApplicationInfo info = pkginfo.applicationInfo;
                return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
            }

        } catch (Exception e) {

        }
        return false;
    }

    public static NotificationManager getNotificationManager() {
        return nm;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
