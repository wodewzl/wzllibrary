
package com.bs.bsims.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.bs.bsims.R;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.chatutils.ConcatInfoUtils;
import com.bs.bsims.chatutils.NotificationTools;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.constant.Constant4PhoneInfo;
import com.bs.bsims.database.DBHelper;
import com.bs.bsims.fragment.BossMessageFragment;
import com.bs.bsims.fragment.BossMessageFragment.MenuBossSelected;
import com.bs.bsims.fragment.BossStatisticsLogoFragment;
import com.bs.bsims.fragment.ContactsFragment;
import com.bs.bsims.fragment.JournalListFragment;
import com.bs.bsims.fragment.MessageFragment;
import com.bs.bsims.fragment.MessageFragment.MenuNormalSelected;
import com.bs.bsims.fragment.WorkManagementFragment;
import com.bs.bsims.image.selector.ImageActivityUtils;
import com.bs.bsims.image.selector.MultiImageSelectorActivity;
import com.bs.bsims.model.UserFromServerVO;
import com.bs.bsims.observer.ImSdkGetInfoImp;
import com.bs.bsims.observer.MessageMyHeadImp;
import com.bs.bsims.observer.MessageRedPointsImp;
import com.bs.bsims.onekey.remove.CoverManager;
import com.bs.bsims.ui.alert.YusAlertDialog;
import com.bs.bsims.update.DownloadUtil;
import com.bs.bsims.update.VersionItem;
import com.bs.bsims.update.VersionUtils;
import com.bs.bsims.utils.CommonImageUtils;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.ConstantValues;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomLog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.DataCleanUtil;
import com.bs.bsims.utils.FileUtil;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.LocationUtils;
import com.bs.bsims.view.BSCircleImageView;
import com.bs.bsims.view.BSDialog;
import com.bs.bsims.view.BSTabWidget;
import com.bs.bsims.view.BSTabWidget.OnTabSelectedListener;
import com.bs.bsims.view.BSUPloadPopWindows;
import com.bs.bsims.view.FlakeView;
import com.bs.bsims.xutils.impl.HttpUtilsByPC;
import com.bs.bsims.xutils.impl.PhoneInfoUtil;
import com.bs.bsims.xutils.impl.RequestCallBackPC;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yzxIM.IMManager;
import com.yzxIM.data.db.ChatMessage;
import com.yzxIM.listener.MessageListener;
import com.yzxtcp.UCSManager;
import com.yzxtcp.data.UcsErrorCode;
import com.yzxtcp.data.UcsReason;
import com.yzxtcp.listener.ILoginListener;
import com.yzxtcp.listener.ISdkStatusListener;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.ex.HttpException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends BaseActivity implements
        OnTabSelectedListener, ISdkStatusListener, ILoginListener, MessageListener, MenuNormalSelected, MenuBossSelected {

    private static final String TAG = "MainActivity";
    private BSTabWidget mTabWidget;

    // private MessageBossFragment mMessageBossFragment;
    private BossMessageFragment mMessageBossFragment;
    private MessageFragment mMessageFragment;

    private WorkManagementFragment mWorkManagementFragment;

    private ContactsFragment mContactsFragment;
    private JournalListFragment mJournalListFragment;
    // private CrmBossFragment mCrmBossFragment;

    private BossStatisticsLogoFragment bossLogoFragment;

    private int mIndex = ConstantValues.BOTTOM_TABLE_ONE;
    private FragmentManager mFragmentManager;
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    private ImageView mIsBossMid;

    private long mExitTime = 0;

    private FlakeView flakeView;

    /** 最新版本 */
    private String number = "";

    /** 最低版本 */
    private String minimum = "";

    /** 地址 */
    private String address = "";

    /** 更新内容 */
    private String updateContent = "";

    private String versionName;
    private ProgressDialog mProgressDialog;

    private LinearLayout mLinearLayoutSonw;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;

    /** DrawerLayout */
    private DrawerLayout mDrawerLayout;
    /** 菜单列表 */
    private String[] mMenuTitles;
    /** Material Design风格 */
    // private MaterialMenuIcon mMaterialMenuIcon;
    /** 菜单打开/关闭状态 */
    private boolean isDirection_left = false;
    private View showView;
    /*
     * 侧滑menu共用的属性 *
     */
    private BSCircleImageView mEnuHead;// 头像
    private TextView mUserName, mUserPdName, mVersionName;
    private BSDialog dialog;
    private ArrayList<String> mSelectPath;
    private static final int TAKE_PICTURE = 0x000001;// 拍照返回码
    private static final int RESULT_LOAD_IMAGE = 0x000002;
    private static final int RESULT_CUT = 0x000003;// 裁剪后返回码
    private BSUPloadPopWindows mPop;
    private File mFile;
    // im聊天
    private boolean isConnect;
    Handler handlerRain = new Handler();
    Runnable runnableRain = new Runnable() {
        @Override
        public void run() {
            flakeView.addFlakes(15);
            handlerRain.postDelayed(runnableRain, 2000);
            if (flakeView.getNumFlakes() > 70)
            {
                handlerRain.removeCallbacks(runnableRain);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void init() {
        mFragmentManager = getSupportFragmentManager();
        mTabWidget = (BSTabWidget) findViewById(R.id.tab_widget);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mIsBossMid = (ImageView) findViewById(R.id.tab_middle);
        initJpushTag();
        initDrawerMenu();
    }

    public void initDrawerMenu() {
        // 设置抽屉打开时，主要内容区被自定义阴影覆盖
        mEnuHead = (BSCircleImageView) findViewById(R.id.menu_head);
        mUserName = (TextView) findViewById(R.id.menu_name);
        mUserPdName = (TextView) findViewById(R.id.menu_pdname);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerLayout.setDrawerListener(new DrawerLayoutStateListener());
        showView = findViewById(R.id.menu_view);
        initMenuData();
        onChangeHead();
    }

    public void initMenuData() {
        ImageLoader imageLoader = ImageLoader.getInstance();
        UserFromServerVO vo = BSApplication.getInstance().getUserFromServerVO();
        imageLoader.displayImage(vo.getHeadpic(), mEnuHead, CommonUtils.initImageLoaderOptions());
        mUserName.setText(vo.getFullname());
        mUserPdName.setText(vo.getPname() + "|" + vo.getDname());
        mVersionName = (TextView) findViewById(R.id.verson_name);
        mVersionName.setText(BSApplication.getInstance().getVersion());
    }

    private void initLoaction() {
        if (mLocationClient == null)
            // 初始化定位
            mLocationClient = new AMapLocationClient(getApplicationContext());
        // 初始化定位参数 // mPop = new BSUPloadPopWindows(mActivity, v, null, null, 0);
        if (mLocationOption == null)
            mLocationOption = new AMapLocationClientOption();
        LocationUtils.getLatLonForOneLoaction(getApplicationContext(), mLocationClient, mLocationOption);
    }

    private void initEvents() {
        mTabWidget.setOnTabSelectedListener(this);
        IMManager.getInstance(getApplicationContext()).setISdkStatusListener(this);
        IMManager.getInstance(getApplicationContext()).setSendMsgListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onTabSelected(mIndex);
        mTabWidget.setTabsDisplay(this, mIndex);
    }

    @Override
    public void onTabSelected(int index) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        String isboss = "0";
        if (BSApplication.getInstance() != null) {
            if (BSApplication.getInstance().getUserFromServerVO() != null) {
                isboss = BSApplication.getInstance().getUserFromServerVO().getIsboss();
            }
        }

        if ("1".equals(isboss)) {
            mIsBossMid.setVisibility(View.VISIBLE);
            switch (index) {
                case ConstantValues.BOTTOM_TABLE_ONE:
                    mIndex = index;
                    hideFragments(transaction);
                    if (null == mMessageBossFragment) {
                        mMessageBossFragment = new BossMessageFragment();
                        mMessageBossFragment.setMenuSelected(this);
                        transaction.add(R.id.center_layout, mMessageBossFragment);
                    } else {
                        transaction.show(mMessageBossFragment);
                        CommonUtils.sendBroadcast(this, Constant.HOME_MSG);
                    }
                    break;
                case ConstantValues.BOTTOM_TABLE_TWO:
                    mIndex = index;
                    hideFragments(transaction);
                    if (null == mJournalListFragment) {
                        mJournalListFragment = new JournalListFragment();
                        transaction.add(R.id.center_layout, mJournalListFragment);
                    } else {
                        transaction.show(mJournalListFragment);
                    }

                    break;
                case ConstantValues.BOTTOM_TABLE_THREE:
                    mIndex = index;
                    hideFragments(transaction);
                    if (null == bossLogoFragment) {
                        bossLogoFragment = new BossStatisticsLogoFragment();
                        transaction.add(R.id.center_layout, bossLogoFragment);
                    } else {
                        transaction.show(bossLogoFragment);
                    }

                    break;
                case ConstantValues.BOTTOM_TABLE_FOUR:
                    mIndex = index;
                    hideFragments(transaction);
                    if (null == mWorkManagementFragment) {
                        mWorkManagementFragment = new WorkManagementFragment();
                        transaction.add(R.id.center_layout, mWorkManagementFragment);
                    } else {
                        transaction.show(mWorkManagementFragment);
                    }

                    break;
                case ConstantValues.BOTTOM_TABLE_FIVE:
                    hideFragments(transaction);
                    mIndex = index;
                    if (null == mContactsFragment) {
                        mContactsFragment = new ContactsFragment(
                                ContactsFragment.CONTACTS);
                        transaction.add(R.id.center_layout, mContactsFragment);
                    } else {
                        transaction.show(mContactsFragment);
                    }
                    break;
                default:
                    break;
            }

        } else {
            mTabWidget.getLayoutParams().height = CommonUtils.getViewHigh(mIsBossMid)/7*6;
            mIsBossMid.setVisibility(View.GONE);
            switch (index) {
                case ConstantValues.BOTTOM_TABLE_ONE:
                    mIndex = index;
                    hideFragments(transaction);
                    if (null == mMessageFragment) {
                        mMessageFragment = new MessageFragment();
                        mMessageFragment.setClickHeadShowMenu(this);
                        transaction.add(R.id.center_layout, mMessageFragment);
                    } else {
                        transaction.show(mMessageFragment);
                        CommonUtils.sendBroadcast(this, Constant.HOME_MSG);
                    }

                    break;
                case ConstantValues.BOTTOM_TABLE_TWO:
                    mIndex = index;
                    hideFragments(transaction);
                    if (null == mJournalListFragment) {
                        mJournalListFragment = new JournalListFragment();
                        transaction.add(R.id.center_layout, mJournalListFragment);
                    } else {
                        transaction.show(mJournalListFragment);
                    }

                    break;
                case ConstantValues.BOTTOM_TABLE_THREE:

                    mIndex = index;
                    hideFragments(transaction);
                    if (null == mWorkManagementFragment) {
                        mWorkManagementFragment = new WorkManagementFragment();
                        transaction.add(R.id.center_layout, mWorkManagementFragment);
                    } else {
                        transaction.show(mWorkManagementFragment);
                    }

                    break;
                case ConstantValues.BOTTOM_TABLE_FOUR:
                    hideFragments(transaction);
                    mIndex = index;
                    if (null == mContactsFragment) {
                        mContactsFragment = new ContactsFragment(
                                ContactsFragment.CONTACTS);
                        transaction.add(R.id.center_layout, mContactsFragment);
                    } else {
                        transaction.show(mContactsFragment);
                    }
                    break;

                default:
                    break;
            }
        }

        transaction.commitAllowingStateLoss();
    }

    private void showMoreWindow(View view) {
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (null != mMessageFragment) {
            transaction.hide(mMessageFragment);
        }
        if (null != mMessageBossFragment) {
            transaction.hide(mMessageBossFragment);
        }

        if (null != bossLogoFragment) {
            transaction.hide(bossLogoFragment);
        }

        if (null != mWorkManagementFragment) {
            transaction.hide(mWorkManagementFragment);
        }
        if (null != mContactsFragment) {
            transaction.hide(mContactsFragment);
        }
        if (null != mJournalListFragment) {
            transaction.hide(mJournalListFragment);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("index", mIndex);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mIndex = savedInstanceState.getInt("index");
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            ExitApp();
            return true;
        } else if (event.getKeyCode() == KeyEvent.KEYCODE_MENU
                && event.getAction() == KeyEvent.ACTION_DOWN) {
        }
        return super.dispatchKeyEvent(event);
    }

    public void ExitApp() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            CustomToast.showShortToast(this, "再按一次退出程序");
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void baseSetContentView() {
        // setContentView(R.layout.activity_main);
        View view = View.inflate(this, R.layout.activity_main, mContentLayout);
        findViewById(R.id.main_layout).setBackgroundColor(Color.TRANSPARENT);
        mHeadLayout.setVisibility(View.GONE);
        init();
        initEvents();
        sendMyPhoneInfo();

//        initLoaction();

    }

    @Override
    public boolean getDataResult() {
        getAppUpdateInfo();
        return true;
    }

    @Override
    public void updateUi() {

    }

    @Override
    public void initView() {
        // 未读消息初始化
        CoverManager.getInstance().init(this);
        CoverManager.getInstance().setMaxDragDistance(150);
        CoverManager.getInstance().setExplosionTime(150);
        ConcatInfoUtils.getInstance().getDepartmentData(this);
        ConcatInfoUtils.getInstance().getIMJavaBean();
    }

    @Override
    public void bindViewsListener() {

    }

    /**
     * 获取版本更新信息
     */
    private void getAppUpdateInfo() {
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("platform", "1");
        paramsMap.put("type", "2");//1为1.2.6线路包，2为2.0线路包
        paramsMap.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());

        new HttpUtilsByPC().sendPostBYPC(Constant.APP_UPDATE, paramsMap,
                new RequestCallBackPC() {
                    @Override
                    public void onFailurePC(HttpException arg0,
                            String arg1) {
                        CustomDialog.closeProgressDialog();
                        CustomToast.showNetErrorToast(MainActivity.this);
                    }

                    @Override
                    public void onSuccessPC(ResponseInfo rstr) {

                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(new String(rstr.result.toString()));
                            String str = (String) jsonObject.get("retinfo");

                            String code = (String) jsonObject.get("code");
                            if (code.equals("200")) {
                                String arrayStr = jsonObject.getJSONObject("array").toString();
                                Gson sGson = new Gson();
                                VersionItem item = sGson.fromJson(arrayStr, VersionItem.class);
                                number = item.getNumber();
                                BSApplication.getInstance().setVersion(number);
                                minimum = item.getMinimum();
                                address = item.getAddress();
                                versionName = item.getName();
                                updateContent = item.getContent();

                                // 第一种方式检测版本是否需要更新
                                isShowDialog(number, minimum, address, updateContent);
                            } else {

//                                CustomToast.showShortToast(MainActivity.this, str);
                            }

                        }
                        catch (Exception e) {
                            e.printStackTrace();

                        } finally {
                            CustomDialog.closeProgressDialog();
                        }

                    }

                });
    }

    /**
     * 是否显示更新的提示框
     * 
     * @param updateVersionCode 新包的版本号
     * @param minVersionCode 最小的版本号
     * @param downloadPath 下载地址
     * @param updateContent 更新内容
     */
    protected void isShowDialog(String updateVersionCode,
            String minVersionCode, String downloadPath, String updateContent) {

        // 需要更新
        if (Integer.parseInt(updateVersionCode) > VersionUtils
                .getversionCode(this)) {
            if (Integer.parseInt(minVersionCode) > VersionUtils
                    .getversionCode(this)) {
                isUpdate(true, downloadPath, updateContent); // 强制更新
            } else {
                isUpdate(false, downloadPath, updateContent); // 不强制更新
            }
        } else { // 不需要更新，直接进入
            // inMainPage();
        }

    }

    /**
     * 是否更新
     * 
     * @param isMinVersion 是否低于服务器最低兼容版本
     * @param downloadPath 下载地址
     * @param updateContent 更新内容
     */
    private void isUpdate(final boolean underMinVersion,
            final String downloadPath, final String updateContent) {
        YusAlertDialog.Builder dialog = new YusAlertDialog.Builder(
                MainActivity.this);
        dialog.setCancelable(false);

        dialog.setTitle("发现新版本：v" + versionName).setMessage(updateContent)

                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DownLoadUI(downloadPath);
                        dialog.dismiss();
                    }
                }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (underMinVersion) { // 如果版本过低就退出App
                            CustomToast.showLongToast(MainActivity.this,
                                    "您的当前版本过低，请更新！");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    System.exit(0);
                                }
                            }, 1500);
                        } else {
                            // inMainPage();
                        }

                    }
                }).create().show();
    }

    public void DownLoadUI(final String url) {

        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setTitle("下载");
            mProgressDialog.setMessage("正在下载数据，请稍后...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.show();
        }

        Thread TD = new Thread(new Runnable() {
            @Override
            public void run() {
                DownLoadAPK(url);
            }
        });
        TD.setName("downapk");
        TD.start();

    }

    public void DownLoadAPK(String url) {

        File file = null;
        try {
            // 如果sd卡的状态可用
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                file = DownloadUtil.download(url,
                        Constant.FileInfo.UPDATE_PATH, mProgressDialog);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Message msg = handler.obtainMessage();
        msg.what = END_LOAD;
        msg.obj = file;
        msg.sendToTarget();

    }

    private static final int TOAST_LONG = 0;
    private static final int END_LOAD = 1;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TOAST_LONG:
                    CustomToast.showShortToast(MainActivity.this,
                            "亲，你的版本过低，请更新后再使用！");
                    break;
                case END_LOAD:
                    File file = null;
                    if (msg.obj == null) {
                        CustomToast.showShortToast(MainActivity.this,
                                "下载失败，请检查网络连接或者有无SD卡！");
                        SharedPreferences mSettings = getSharedPreferences(
                                "version_txt", Context.MODE_PRIVATE);
                        Editor editor = mSettings.edit();
                        editor.putString("version_tag", "1");
                        editor.commit();
                        if (mProgressDialog != null) {
                            mProgressDialog.dismiss();
                        }
                        return;
                    }

                    file = (File) msg.obj;
                    if (mProgressDialog != null) {
                        mProgressDialog.dismiss();
                        mProgressDialog = null;
                    }

                    file = new File(Environment.getExternalStorageDirectory()
                            .getAbsolutePath() + "/oa.apk");

                    InstallAPK(file, MainActivity.this);

                    break;
            }
        }

    };

    /** 安装APK文件 */
    public void InstallAPK(File file, Context context) {
        new DataCleanUtil(context).cleanApplicationCache(context);
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setData(uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public void inMainPage() {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        this.startActivity(intent);
        this.finish();
    }

    public void initJpushTag() {

        Set<String> tagSet = new LinkedHashSet<String>();
        tagSet.add(BSApplication.getInstance().getUserFromServerVO().getFjptags());
        JPushInterface.setAliasAndTags(getApplicationContext(),
                BSApplication.getInstance().getUserFromServerVO().getJpalias(), tagSet,
                mAliasCallback);
        JPushInterface.resumePush(getApplicationContext());

        // 设置Tags
        JPushInterface.setTags(getApplicationContext(), tagSet,
                new TagAliasCallback() {
                    @Override
                    public void gotResult(int arg0,
                            String arg1, Set<String> arg2) {
                        CustomLog.e(TAG, "setTags == " + arg0);
                    }
                });

        // 设置alias
        String jpalias = BSApplication.getInstance().getUserFromServerVO().getJpalias();
        JPushInterface.setAlias(getApplicationContext(),
                jpalias, new TagAliasCallback() {
                    @Override
                    public void gotResult(int arg0,
                            String arg1, Set<String> arg2) {
                        CustomLog.e(TAG, "setAlias == " + arg0);
                    }
                });
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
            }
        }

    };

    // 注册定时消息广播 (定位的)
    public void setReminder() {
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent();
        intent.setAction("LogistiRecevierOfGaode");
        PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
        // 五分钟响应一次闹钟// 二十分钟定位一次60*20*100
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 3 * 60 * 1000, pi);

    }

    /**
     * 向服务器提交登陆手机的基本信息
     */
    private void sendMyPhoneInfo() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        PhoneInfoUtil.width = dm.widthPixels;
        Map<String, String> mPhoneInfoMap = PhoneInfoUtil
                .getPhoneInfo(this);
        mPhoneInfoMap.put("ftoken", BSApplication.getInstance().getmCompany());
        mPhoneInfoMap.put("username", BSApplication.getInstance().getUserFromServerVO().getUsername());
        String url = BSApplication.getInstance().getHttpTitle()
                + Constant4PhoneInfo.SENDMYPHONEINFO_PATH;
        new HttpUtilsByPC().sendPostBYPC(url, mPhoneInfoMap,
                new RequestCallBackPC() {

                    @Override
                    public void onSuccessPC(ResponseInfo arg0) {
                        String result = (String) arg0.result;
                    }

                    @Override
                    public void onFailurePC(HttpException arg0, String arg1) {
                        // CustomToast.showShortToast(context, "网络数据错误，请重新尝试");
                    }
                });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /* 在这里，我们通过碎片管理器中的Tag，就是每个碎片的名称，来获取对应的fragment */
        if (mJournalListFragment != null && mJournalListFragment.isVisible()) {
            /* 然后在碎片中调用重写的onActivityResult方法 */
            mJournalListFragment.onActivityResult(requestCode, resultCode, data);
        }
        else {
            switch (requestCode) {
                case ImageActivityUtils.REQUEST_IMAGE:
                    if (data != null) {
                        ArrayList<String> mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                        mFile = new File(mSelectPath.get(0));
                        startPhotoZoom(mFile);
                    }
                    break;
                case RESULT_CUT:
                    File file = new File(FileUtil.getSaveFilePath(this) + "temp.jpg");
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 1;
                    // Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), options);
                    Bitmap bitmap = CommonImageUtils.getImageFromLocal(file.getPath());
                    mEnuHead.setImageBitmap(bitmap);
                    if (file.exists())
                        commit(file);
                    break;
            }
        }
    }

    public void commit(File file) {
        CustomDialog.showProgressDialog(this, "正在提交数据...");
        RequestParams params = new RequestParams();

        try {
            params.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            params.put("userid", BSApplication.getInstance().getUserId());
            params.put("file", file);

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        String url = BSApplication.getInstance().getHttpTitle() + Constant.HEAD_ICON_UPLAOD;
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                CustomDialog.closeProgressDialog();// 关闭对话框
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(new String(arg2));
                    String str = (String) jsonObject.get("retinfo");
                    String code = (String) jsonObject.get("code");
                    if (Constant.RESULT_CODE400.equals(code)) {
                        CustomToast.showShortToast(MainActivity.this, str);
                    } else {

                        File file = new File(FileUtil.getSaveFilePath(MainActivity.this) + "temp.jpg");
                        file.delete();
                        String headpic = (String) jsonObject.get("headpic");
                        UserFromServerVO userFromServerVO = BSApplication.getInstance().getUserFromServerVO();
                        userFromServerVO.setHeadpic(headpic);
                        BSApplication.getInstance().setUserFromServerVO(userFromServerVO);
                        MessageMyHeadImp.getInstance().notifyWatcher(headpic);
                        // DataChangedListenerImp dcli = new DataChangedListenerImp();
                        // dcli.dataChanged();
                        // Intent intent = new Intent();
                        // intent.setAction(Constant.UPLOAD_HEAD_ICON_MSG);
                        // // mActivity.sendBroadcast(intent);
                    }

                    CustomDialog.closeProgressDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onLogin(UcsReason reason) {
        // TODO Auto-generated method stub
        int sdkStatus = 0;
        if (reason.getReason() == UcsErrorCode.NET_ERROR_CONNECTOK) {
            isConnect = false;
            sdkStatus = 400;
        } else if (reason.getReason() == UcsErrorCode.PUBLIC_ERROR_NETUNCONNECT) {
            sdkStatus = 402;
        } else {
            // chatMessageFragment.handSdkStatus(408);
            sdkStatus = 408;
        }
        ImSdkGetInfoImp.getInstance().notifyWatcher(sdkStatus);
    }

    @Override
    public void onSdkStatus(UcsReason reason) {
        // TODO Auto-generated method stub
        Log.i(TAG, "onSdkStatus status = " + reason.getReason());

        int status = 0;
        if (reason.getReason() == UcsErrorCode.NET_ERROR_KICKOUT) {// 服务器强制下线通知
            // CustomLog.e("收到服务器强制下线通知");
            mHandler.sendEmptyMessage(101);
            // UCSManager.disconnect();
        } else if (reason.getReason() == UcsErrorCode.NET_ERROR_TOKENERROR) {
            // CustomLog.e("token超时,请重新登录");
            mHandler.sendEmptyMessage(102);
            ConcatInfoUtils.getInstance().getIMJavaBean();
        } else if (reason.getReason() == UcsErrorCode.NET_ERROR_TCPCONNECTOK) {
            // CustomLog.e("TCPCONNECTOK errorcode = " + reason.getReason());
            status = 400;
        } else if (reason.getReason() == UcsErrorCode.NET_ERROR_TCPCONNECTFAIL) {
            // CustomLog.i("TCPCONNECTFAIL errorcode = " + reason.getReason());
            // ((ConversationFragment)mTabs.get(0)).handSdkStatus(408);
            status = 408;
        } else if (reason.getReason() == UcsErrorCode.NET_ERROR_TCPCONNECTING) {
            // CustomLog.i("TCPCONNECTING errorcode = " + reason.getReason());
            // ((ConversationFragment)mTabs.get(0)).handSdkStatus(406);
            status = 406;
        } else if (reason.getReason() == UcsErrorCode.PUBLIC_ERROR_NETUNCONNECT) {
            // CustomLog.i("NETUNCONNECT errorcode = " + reason.getReason());
            // ((ConversationFragment)mTabs.get(0)).handSdkStatus(402);
            status = 402;
        } else if (reason.getReason() == UcsErrorCode.PUBLIC_ERROR_NETCONNECTED) {
            // CustomLog.i("NETCONNECTED errorcode = " + reason.getReason());
            // ((ConversationFragment)mTabs.get(0)).handSdkStatus(406);
            if (UCSManager.isConnect()) {
                status = 400;
            } else {
                status = 406;
            }
        }
        ImSdkGetInfoImp.getInstance().notifyWatcher(status);
    }

    @Override
    public void onDownloadAttachedProgress(String arg0, String arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub

    }

    // 获取IM聊天消息
    @Override
    public void onReceiveMessage(List msgs) {
        // TODO Auto-generated method stub
        List<ChatMessage> messages = (List<ChatMessage>) msgs;
        if (mHandler.hasMessages(406)) {
            mHandler.removeMessages(406);
        }
        Message msg = mHandler.obtainMessage();
        msg.obj = messages.get(messages.size() - 1);
        msg.what = 406;
        mHandler.sendMessage(msg);
        MessageRedPointsImp.getInstance().notifyWatcher(messages);
    }

    @Override
    public void onSendMsgRespone(ChatMessage msgs) {

    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 101:
                    Intent intent = new Intent();
                    intent.setAction("LoginOtherPlaceReceiver");
                    sendBroadcast(intent);
                    // 注册全局广播
                    break;
                case 102:
                    // MyToast.showLoginToast(IMChatActivity.this, "token超时,请重新登陆");
                    break;
                case 406:
                    // 振铃和声音
                    Log.i(TAG, "收到消息，添加推送");
                    if (BSApplication.getInstance().getResultVO() != null) {
                        NotificationTools.showNotification((ChatMessage) msg.obj);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(null);
        UCSManager.removeISdkStatusListener(this);
        super.onDestroy();
    }

    /**
     * DrawerLayout状态变化监听
     */
    private class DrawerLayoutStateListener extends
            DrawerLayout.SimpleDrawerListener {
        /**
         * 当导航菜单滑动的时候被执行
         */
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            showView = drawerView;
        }

        /**
         * 当导航菜单打开时执行
         */
        @Override
        public void onDrawerOpened(android.view.View drawerView) {
            isDirection_left = true;
            baseHeadLayout.setBackgroundColor(getResources().getColor(R.color.main_menu_title));
        }

        /**
         * 当导航菜单关闭时执行
         */
        @Override
        public void onDrawerClosed(android.view.View drawerView) {
            showView = drawerView;
            isDirection_left = false;
            baseHeadLayout.setBackgroundColor(getResources().getColor(R.color.title_father));
        }

    }

    /** 查看考勤 */
    public void onReadAttence(View view) {
        Intent intent = new Intent();
        intent.setClass(this, AttendanceActivity.class);
        startActivity(intent);

    }

    /** 侧滑菜单的点击功能 */
    public void updatePwd(View view) {

        // 表示隐藏修改密码
        if (getIntent().getIntExtra("isFastLogin", 0) != 1) {
            Intent intent = new Intent();
            intent.setClass(this, UpdatePasswordActivity.class);
            startActivity(intent);
        }
        else {
            CustomToast.showShortToast(this, "体验服无法修改密码");
        }

    }

    public void onReadSign(View view) {
        Intent intent = new Intent();
        intent.setClass(this, SignInActivity.class);
        startActivity(intent);
    }

    public void seeMyAddtence(View view) {
        Intent intent = new Intent();
        intent.putExtra("uid", BSApplication.getInstance().getUserId());
        intent.setClass(this, DanganIndextwoActivity.class);
        startActivity(intent);
    }

    public void checkMyVersion(View view) {
        getAppUpdateInfo();
    }

    public void ideaReveice(View view) {
        showDialog();
    }

    public void aboutOurSelf(View view) {
        Intent intent = new Intent();
        intent.setClass(this, AboutActivity.class);
        startActivity(intent);
    }

    public void clearCrash(View view) {
        cleanCache();
    }

    public void loginOut(View viwe) {
        JPushInterface.setAliasAndTags(this.getApplicationContext(), null, null, null);
        JPushInterface.clearAllNotifications(this.getApplicationContext());
        JPushInterface.stopPush(this.getApplicationContext());
        Intent intent = new Intent();
        new DataCleanUtil(this).cleanApplicationCache(this);// 不清除数据库
        new DataCleanUtil(this).cleanDatabaseByName(this, DBHelper.DB_NAME);// 不清除数据库
        SharedPreferences sp = this.getSharedPreferences("location_service", Activity.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putLong("service_gbLocationTime", 0);
        editor.commit();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("hiddenpwd", true);
        intent.setClass(this, LoginActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void uploadIdea(String content) {
        try {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put("username", BSApplication.getInstance().getUserFromServerVO().getUsername());
            map.put("fullname", BSApplication.getInstance().getUserFromServerVO().getFullname());
            map.put("company", BSApplication.getInstance().getUserFromServerVO().getFirmcname());
            map.put("model", android.os.Build.MODEL);
            map.put("content", content);
            HttpClientUtil.getRequest(Constant.UPLOAD_USER_IDEA_URL, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showDialog() {

        View v = LayoutInflater.from(this).inflate(R.layout.dialog_edittext, null);
        final EditText textView = (EditText) v.findViewById(R.id.edit_content);
        dialog = new BSDialog(this, "请输入内容", v, new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                if (textView.getText().toString().length() > 0) {
                    CustomToast.showLongToast(MainActivity.this, "谢谢您的宝贵意见");
                    new Thread() {
                        public void run() {
                            try {
                                uploadIdea(textView.getText().toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();

                } else {
                    CustomToast.showLongToast(MainActivity.this, "请填写您的意见");
                }

            }
        });
        dialog.show();
    }

    // 我的日志
    public void onReadJoural(View view) {
        Intent intent = new Intent();
        intent.putExtra("loguid", BSApplication.getInstance().getUserId());
        // intent.putExtra("show_title", "false");
        // intent.setClass(mActivity, JournalListActivity.class);
        intent.putExtra("defferent_goin", "3");
        intent.setClass(this, JournalSerachActivity.class);
        startActivity(intent);
    }

    public void cleanCache() {
        final DataCleanUtil dataClean = new DataCleanUtil(this);
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_lv_item, null);
        final TextView textView = (TextView) v.findViewById(R.id.textview);
        // textView.setText("您当前有" + dataClean.getFileSize() + "可以清除，确定清楚吗？");
        textView.setText("您确定清除缓存吗？");
        dialog = new BSDialog(this, "缓存清除", v, new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dataClean.cleanApplicationData(MainActivity.this, FileUtil.getSaveFilePath(MainActivity.this));
                CustomToast.showLongToast(MainActivity.this, "当前清除" + dataClean.getFileSize() + "文件缓存");
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void showMenu() {
        if (isDirection_left) {
            // 关闭，还原
            baseHeadLayout.setBackgroundColor(getResources().getColor(R.color.title_father));
            mDrawerLayout.closeDrawer(showView);
        }
        else {
            baseHeadLayout.setBackgroundColor(getResources().getColor(R.color.main_menu_title));
            mDrawerLayout.openDrawer(showView);
        }

    }

    public void onChangeHead() {

        mEnuHead.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MainActivity.this, MultiImageSelectorActivity.class);
                // 把viewconteleer的名字带过去
                intent.putExtra("activity", "选择头像");
                // 是否显示拍摄图片
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                // 最大可选择图片数量
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
                // 选择模式
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, 1);
                // 默认选择
                if (mSelectPath != null && mSelectPath.size() > 0) {
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
                }
                startActivityForResult(intent, ImageActivityUtils.REQUEST_IMAGE);
            }
        });

    }

    public void startPhotoZoom(File file) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.fromFile(file), "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        intent.putExtra("output", Uri.fromFile(new File(FileUtil.getSaveFilePath(this) + "temp.jpg")));
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        intent.putExtra("scale", true);// 黑边
        intent.putExtra("scaleUpIfNeeded", true);// 黑边
        startActivityForResult(intent, RESULT_CUT);
    }

    @Override
    public void onMenuClick() {
        // TODO Auto-generated method stub
        if (isDirection_left) {
            // 关闭，还原
            baseHeadLayout.setBackgroundColor(getResources().getColor(R.color.title_father));
            mDrawerLayout.closeDrawer(showView);
        }
        else {
            baseHeadLayout.setBackgroundColor(getResources().getColor(R.color.main_menu_title));
            mDrawerLayout.openDrawer(showView);
        }
    }

//    @Override
//    public boolean supportSlideBack() {
//        return false;
//    }
}
