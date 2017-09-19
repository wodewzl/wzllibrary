
package com.bs.bsims.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.bs.bsims.R;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.chatutils.IMJavaBean;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.database.DBHelper;
import com.bs.bsims.model.LoginUser;
import com.bs.bsims.model.UserFromServerVO;
import com.bs.bsims.ui.alert.YusAlertDialog;
import com.bs.bsims.update.DownloadUtil;
import com.bs.bsims.update.VersionUtils;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomLog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.MD5;
import com.bs.bsims.utils.UserManager;
import com.bs.bsims.view.BSDialog;
import com.bs.bsims.xutils.impl.HttpUtilsByPC;
import com.bs.bsims.xutils.impl.RequestCallBackPC;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.yzxtcp.UCSManager;
import com.yzxtcp.data.UcsErrorCode;
import com.yzxtcp.data.UcsReason;
import com.yzxtcp.listener.ILoginListener;

import org.apache.http.Header;
import org.json.JSONObject;
import org.xutils.ex.HttpException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class LoginActivity extends Activity implements OnClickListener {
    private PopupWindow popView;
    private MyAdapter dropDownAdapter;
    private DBHelper dbHelper;
    private EditText mUserName;
    private EditText mPassword;
    private ImageButton mRememberPassword;
    private ImageButton mDropDown;
    private int mPasswordStatus = 1;
    private Button mLoginBt;
    private BSDialog mImDialog;
    private Context mContext;
    private String TAG = "LoginActivity";
    private String userName;
    private String password;
    private UserFromServerVO userFromServerVO;
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
    private LoginUser user;
    protected SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JPushInterface.setAliasAndTags(getApplicationContext(), null, null,
                null);
        JPushInterface.clearAllNotifications(getApplicationContext());
        JPushInterface.stopPush(getApplicationContext());
        setContentView(R.layout.login);
        initView();
        initData();
        bindViewsListener();
    }

    public void initView() {

        mContext = this;
        dbHelper = new DBHelper(this);
        mUserName = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mRememberPassword = (ImageButton) findViewById(R.id.remember_password);
        mDropDown = (ImageButton) findViewById(R.id.dropdown_button);
        mLoginBt = (Button) findViewById(R.id.login);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // mFastinLayout = (LinearLayout) findViewById(R.id.fastin_layout);
    }

    public void initData() {
        initLoginUserName();
        isClearPwd(getIntent());
        mSettings = PreferenceManager.getDefaultSharedPreferences(this);

    }

    public void bindViewsListener() {
        mLoginBt.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        otherLoginWarn();
        super.onResume();
    }

    private void initPopView(String[] usernames) {
        List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < usernames.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("name", usernames[i]);
            map.put("drawable", R.drawable.contacts_search_del);
            list.add(map);
        }
        dropDownAdapter = new MyAdapter(this, list, R.layout.dropdown_item,
                new String[] {
                        "name", "drawable"
                }, new int[] {
                        R.id.textview,
                        R.id.delete
                });
        ListView listView = new ListView(this);
        listView.setAdapter(dropDownAdapter);

        popView = new PopupWindow(listView, mUserName.getWidth(),
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popView.setFocusable(true);
        popView.setOutsideTouchable(true);
        popView.setBackgroundDrawable(getResources().getDrawable(R.color.white));
    }

    class MyAdapter extends SimpleAdapter {

        private List<HashMap<String, Object>> data;

        public MyAdapter(Context context, List<HashMap<String, Object>> data,
                int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView,
                ViewGroup parent) {
            System.out.println(position);
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(LoginActivity.this).inflate(R.layout.dropdown_item, null);
                holder.btn = (ImageButton) convertView.findViewById(R.id.delete);
                holder.tv = (TextView) convertView.findViewById(R.id.textview);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv.setText(data.get(position).get("name").toString());
            holder.tv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String[] usernames = dbHelper.queryAllUserName();
                    mUserName.setText(usernames[position]);
                    mPassword.setText(dbHelper.queryPasswordByName(usernames[position]));
                    popView.dismiss();
                }
            });
            holder.btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String[] usernames = dbHelper.queryAllUserName();
                    if (usernames.length > 0) {
                        dbHelper.delete(usernames[position]);
                    }
                    String[] newusernames = dbHelper.queryAllUserName();
                    if (newusernames.length > 0) {
                        initPopView(newusernames);
                        popView.showAsDropDown(mUserName);
                    } else {
                        popView.dismiss();
                        popView = null;
                    }
                }
            });
            return convertView;
        }

        class ViewHolder {
            private TextView tv;
            private ImageButton btn;
        }
    }

    private void initLoginUserName() {
        String[] usernames = dbHelper.queryAllUserName();
        if (usernames.length > 0) {
            String tempName = usernames[usernames.length - 1];
            mUserName.setText(tempName);
            mUserName.setSelection(tempName.length());
            String tempPwd = dbHelper.queryPasswordByName(tempName);
            int checkFlag = dbHelper.queryIsSavedByName(tempName);
            if (checkFlag == 0) {
                mRememberPassword
                        .setBackgroundResource(R.drawable.img_password);
            } else if (checkFlag == 1) {
                mRememberPassword
                        .setBackgroundResource(R.drawable.img_password);
            }
            mPassword.setText(tempPwd);
        }
        mUserName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                mPassword.setText("");
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                // 登录
                userName = mUserName.getText().toString();
                password = mPassword.getText().toString();

                // 非空判断
                if (TextUtils.isEmpty(userName)) {
                    CustomToast.showShortToast(mContext, "请输入号码");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    CustomToast.showShortToast(mContext, "请输入密码");
                    return;
                }

                // submitLogin();
                submitLogin4MyHost();

                break;
            case R.id.dropdown_button:
                if (popView != null) {
                    if (!popView.isShowing()) {
                        popView.showAsDropDown(mUserName);
                    } else {
                        popView.dismiss();
                    }
                } else {
                    // 如果有已经登录过账号
                    if (dbHelper.queryAllUserName().length > 0) {
                        initPopView(dbHelper.queryAllUserName());
                        if (!popView.isShowing()) {
                            popView.showAsDropDown(mUserName);
                        } else {
                            popView.dismiss();
                        }
                    }

                }
                break;

            case R.id.remember_password:
                Boolean isRememberPassword = true;
                // 1记住密码 0忘记密码
                if (mPasswordStatus == 0) {
                    mRememberPassword
                            .setBackgroundResource(R.drawable.img_password);
                    mPasswordStatus = 1;
                } else {

                    mRememberPassword
                            .setBackgroundResource(R.drawable.img_password);
                    mPasswordStatus = 0;
                }
                break;

            // case R.id.fastin_layout:
            // Intent intent = new Intent();
            // intent.setClass(this, NewFastLoginActivity.class);
            // this.startActivity(intent);
            // break;

            default:
                break;
        }
    }

    public void saveData(String userid, String company, String httpTitle) {
        SharedPreferences preference = getSharedPreferences("user",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putString("userid", userid);
        editor.putString("company", company);
        editor.putString("http_title", httpTitle);
        editor.commit();
    }

    private void submitLogin4MyHost() {
        CustomDialog.showProgressDialog(mContext);
        String url = Constant.LOGIN_URL;
        RequestParams paramsMap = new RequestParams();
        password = MD5.Md5(password);
        paramsMap.put("username", userName);
        paramsMap.put("password", password);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, paramsMap, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                    Throwable arg3) {
                CustomDialog.closeProgressDialog();
                CustomToast.showShortToast(mContext, "没有网络，请打开网络再试试");
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                String result = new String(arg2);
                CustomLog.e(TAG, result);
                try {
                    Gson gson = new Gson();
                    JSONObject jsonObject = new JSONObject(new String(arg2));
                    String retinfo = (String) jsonObject.get("retinfo");
                    String code = (String) jsonObject.get("code");
                    if ("200".equals(code)) {
                        String[] usernames = dbHelper.queryAllUserName();
                        if (usernames.length > 0) {
                            for (int i = 0; i < usernames.length; i++) {
                                dbHelper.delete(usernames[i]);
                            }
                        }

                        if (mPasswordStatus == 1) {
                            dbHelper.insertOrUpdate(userName, mPassword.getText()
                                    .toString(), 1);
                        } else {
                            dbHelper.insertOrUpdate(userName, "", 0);
                        }

                        String arrayStr = null;
                        String remind = null;
                        arrayStr = result;
                        user = gson.fromJson(arrayStr, LoginUser.class).getArray();
                        // 保存用户信息
                        UserManager.saveLoginInfo(mSettings, user);
                        userFromServerVO = new UserFromServerVO();
                        userFromServerVO = gson.fromJson(jsonObject.getJSONObject("array").toString(), UserFromServerVO.class);

                        // BSApplication.getInstance().setLogin4GetHostVO(login4GetHostVO);
                        BSApplication.getInstance().setUserId(userFromServerVO.getUserid());
                        BSApplication.getInstance().setmCompany(userFromServerVO.getFtoken());

                        BSApplication.getInstance().setHttpTitle(userFromServerVO.getSiteurl());
                        saveData(userFromServerVO.getUserid(), userFromServerVO.getFtoken(), userFromServerVO.getSiteurl());
                        String managementStatus = user.getManagement();
                        /**
                         * 是否管理层，只有两个值1是管理层
                         */
                        if ("0".equalsIgnoreCase(managementStatus)) {
                            Constant.WORKFRAGMENT = "WorkGeneralFragment";
                        } else {
                            Constant.WORKFRAGMENT = "WorkBossFragment";
                        }

                        if ("1".equals(userFromServerVO.getLogins())) {
                            Intent intent = new Intent();
                            intent.setClass(LoginActivity.this, UpdatePasswordActivity.class);
                            // 原始密码修改
                            intent.putExtra("first_password_update", true);
                            startActivity(intent);
                            LoginActivity.this.finish();
                            return;
                        }
                        BSApplication.getInstance().setUserFromServerVO(userFromServerVO);
                        inMainPage();
                        onConnectIm();

                    } else {
                        CustomDialog.closeProgressDialog();
                        CustomToast.showShortToast(mContext, retinfo);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    CustomDialog.closeProgressDialog();
                    CustomToast.showShortToast(mContext, "数据解析异常");
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
            inMainPage();
        }

    }

    public void inMainPage() {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        this.startActivity(intent);
        this.finish();
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
                LoginActivity.this);
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
                            CustomToast.showLongToast(LoginActivity.this,
                                    "您的当前版本过低，请更新！");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    System.exit(0);
                                }
                            }, 1500);
                        } else {
                            inMainPage();
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
                    CustomToast.showShortToast(LoginActivity.this,
                            "亲，你的版本过低，请更新后再使用！");
                    break;
                case END_LOAD:
                    File file = null;
                    if (msg.obj == null) {
                        CustomToast.showShortToast(LoginActivity.this,
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

                    InstallAPK(file, LoginActivity.this);

                    break;
            }
        }

    };

    /** 安装APK文件 */
    public void InstallAPK(File file, Context context) {
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setData(uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        // intent.setClassName("com.android.packageinstaller",
        // "com.android.packageinstaller.PackageInstallerActivity");
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
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

    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

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

    private UserFromServerVO setLoginUser2UserFromServerVO(UserFromServerVO userFromServerVO) {
        userFromServerVO.setAddress(user.getAddress());
        userFromServerVO.setDid(user.getDid());
        userFromServerVO.setDname(user.getDname());
        userFromServerVO.setEmail(user.getEmail());
        userFromServerVO.setFjptags(user.getFjptags());
        userFromServerVO.setFtoken(user.getFtoken());
        userFromServerVO.setFullname(user.getFullname());
        userFromServerVO.setHeadpic(user.getHeadpic());
        userFromServerVO.setIdcard(user.getIdcard());
        userFromServerVO.setIsinpost(user.getIsinpost());
        userFromServerVO.setIslogin(user.getIslogin());
        userFromServerVO.setUserid(user.getUserid());
        userFromServerVO.setUsername(user.getUsername());
        userFromServerVO.setSex(user.getSex());
        userFromServerVO.setPostsname(user.getPostsname());
        return userFromServerVO;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null)
            im.hideSoftInputFromWindow(getCurrentFocus()
                    .getApplicationWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        return super.onTouchEvent(event);
    }

    public void uploadLoginFailLog(final String log) {

        new Thread() {
            public void run() {
                try {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("userid", BSApplication.getInstance().getUserId());
                    map.put("log", log);
                    map.put("type", "_"
                            + BSApplication.getInstance().getUserFromServerVO().getFirmcname()
                            + "_登录失败" + "_" + android.os.Build.MODEL);
                    HttpClientUtil.getRequest(Constant.UPLOAD_LOG_URL, map);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void isClearPwd(Intent intent) {
        if (intent.getBooleanExtra("hiddenpwd", false)) {
            mPassword.setText("");
            password = "";
        }
        if (intent.getBooleanExtra("jupshpwd", false)) {// 密码在PC被修改，收到推送
            CustomToast.showLongToast(mContext, "密码已修改，请重新登录");
        }
    }

    public void otherLoginWarn() {
        if (!((Activity) mContext).isFinishing() && getIntent().getBooleanExtra("isOtherPlace", false)) {
            if (mImDialog == null) {
                View isoffice = View.inflate(mContext, R.layout.ishaveofficeapp, null);
                final TextView tv_content = (TextView) isoffice.findViewById(R.id.tv_text);
                isoffice.findViewById(R.id.office_logo).setVisibility(View.GONE);
                tv_content.setGravity(Gravity.CENTER);
                tv_content.setTextSize(16);
                tv_content.setText("您的帐号已在其他设备登录。如非本人操作，则密码可能已泄漏，建议联系贵公司系统管理员修改密码!");
                mImDialog = new BSDialog(mContext, "下线通知", isoffice, new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mImDialog.dismiss();

                    }
                });
                mImDialog.show();
                mImDialog.setButtonCanleVisible(false);
                mImDialog.setCanceledOnTouchOutside(false);
                mImDialog.setCancelable(false);
            }
            else {
                mImDialog.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        mImDialog.dismiss();
                    }
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        CustomDialog.closeProgressDialog();
        super.onDestroy();
    }

    public void onConnectIm() {
        final String url = BSApplication.getInstance().getHttpTitle() + Constant.IMLOGIN;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", BSApplication.getInstance().getUserId());
        map.put("uids", BSApplication.getInstance().getUserId());
        map.put("ftoken", BSApplication.getInstance().getmCompany());
        new HttpUtilsByPC().sendPostBYPC(url, map,
                new RequestCallBackPC() {
                    @Override
                    public void onFailurePC(HttpException arg0, String arg1) {
                        // TODO Auto-generated method stub
                        CustomLog.e("TAG", "账号或平台未注册聊天通讯功能");
                        CustomDialog.closeProgressDialog();
                        CustomToast.showShortToast(mContext, "网络异常");
                    }

                    @Override
                    public void onSuccessPC(ResponseInfo rstr) {
                        // TODO Auto-generated method stub
                        Gson gson = new Gson();
                        try {
                            IMJavaBean imJavaBean = gson.fromJson(rstr.result.toString(), IMJavaBean.class);
                            if (Constant.RESULT_CODE.equals(imJavaBean.getCode()) && null != imJavaBean.getClient() && null != imJavaBean.getClient().get(0)) {
                                BSApplication.getInstance().saveIMjavaBean(imJavaBean);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        UCSManager.connect(BSApplication.getInstance().getIMjavaBean().getClient().get(0).getLoginToken(), new ILoginListener() {
                                            @Override
                                            public void onLogin(UcsReason reason) {
                                                // TODO Auto-generated method stub
                                                if (reason.getReason() == UcsErrorCode.NET_ERROR_CONNECTOK) {
                                                    BSApplication.getInstance().setAppliactionChat(true);
                                                    CustomDialog.closeProgressDialog();
                                                }
                                                else {
                                                    CustomDialog.closeProgressDialog();
                                                    CustomToast.showShortToast(mContext, "登陆失败,聊天服务器无法链接");
                                                    BSApplication.getInstance().setAppliactionChat(false);
                                                    CustomLog.e("resss", "login fail errorCode = " + reason.getReason() + ",errorMsg = " + reason.getMsg());
                                                }
                                            }
                                        });
                                    }
                                }).start();
                            }
                            else {
                                CustomDialog.closeProgressDialog();
                                CustomToast.showLongToast(mContext, "账号注册失败导致,聊天服务器无法链接,请联系管理员及时更正您的账号");
                                BSApplication.getInstance().setAppliactionChat(false);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    // 呼叫北盛的电话
    public void callbeishengPhone(View view) {
        CommonUtils.call(mContext, "");

    }

    // 跳转到注册界面
    public void goToRegisterView(View view) {
        Intent intent = new Intent();
        intent.setClass(this, APPRegisterFirstActivity.class);
        this.startActivity(intent);
    }

    // 跳转到体验账号
    public void goToNewFastView(View view) {
        Intent intent = new Intent();
        intent.setClass(this, NewFastLoginActivity.class);
        this.startActivity(intent);
    }
}
