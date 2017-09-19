
package com.beisheng.synews.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.DialogUtil;
import com.beisheng.base.utils.MD5;
import com.beisheng.base.view.BSDialog;
import com.bs.bsims.observer.MessageMyHeadImp;
import com.beisheng.synews.application.AppApplication;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.mode.UserInfoVO;
import com.google.gson.Gson;
import com.im.zhsy.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.socialize.Config;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class LoginActivity extends BaseActivity implements OnClickListener {
    private EditText mNameEt, mPasswordEt;
    private TextView mForgetPwdTv, mRegisterTv;
    private Button mLoginBt;
    private ImageView mWeixinImg, mQQImg, mXinlangImg;
    private boolean mCommitFlag = true;
    private UMShareAPI mShareAPI;

    private String mUserName;
    private String mPassword;
    private String mOpenid;
    private String mEmail;
    private String mPhone;
    private String mHeadurl;
    private String mType = "";
    private BSDialog mDialog;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.login_activity, mBaseContentLayout);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean getDataResult() {
        return true;
    }

    @SuppressLint("NewApi")
    @Override
    public void initView() {
        mBaseTitleTv.setText("登录");
        mNameEt = (EditText) findViewById(R.id.name_et);
        mPasswordEt = (EditText) findViewById(R.id.password_et);
        mForgetPwdTv = (TextView) findViewById(R.id.forget_pwd_tv);
        mLoginBt = (Button) findViewById(R.id.login_bt);
        mWeixinImg = (ImageView) findViewById(R.id.weixin_img);
        mQQImg = (ImageView) findViewById(R.id.qq_img);
        mXinlangImg = (ImageView) findViewById(R.id.xinlang_img);
        mLoginBt.setBackground(BaseCommonUtils.setBackgroundShap(this, 30, R.color.sy_title_color, R.color.sy_title_color));
        mRegisterTv = (TextView) findViewById(R.id.register_tv);
        // 第三方登录
        mShareAPI = UMShareAPI.get(this);
        initData();
    }

    public void initData() {
    }

    @Override
    public void bindViewsListener() {
        mRegisterTv.setOnClickListener(this);
        mLoginBt.setOnClickListener(this);
        mWeixinImg.setOnClickListener(this);
        mQQImg.setOnClickListener(this);
        mXinlangImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_tv:
                openActivity(RegisteredActivtiy.class);
                this.finish();
                break;

            case R.id.login_bt:
                mUserName = mNameEt.getText().toString();
                mPassword = mPasswordEt.getText().toString();
                if (mUserName.length() > 0)
                    mType = "0";
                commit();
                break;

            case R.id.weixin_img:
                SHARE_MEDIA wx_platform = SHARE_MEDIA.WEIXIN;
                loginOther(wx_platform);
                mType = "2";

                break;
            case R.id.qq_img:
                SHARE_MEDIA qq_platform = SHARE_MEDIA.QQ;
                loginOther(qq_platform);
                mType = "1";
                break;
            case R.id.xinlang_img:
                SHARE_MEDIA xl_platform = SHARE_MEDIA.SINA;
                loginOther(xl_platform);
                mType = "3";
                break;

            default:
                break;
        }
    }

    UMAuthListener umLoginListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            showCustomToast("授权成功");
            mShareAPI.getPlatformInfo(LoginActivity.this, platform, umAuthListener);
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            showCustomToast("授权失败,请检查是否已安装");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            showCustomToast("授权取消");
        }
    };

    UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            showCustomToast("授权成功");

            if ("1".equals(mType)) {
                mUserName = data.get("screen_name");
                mOpenid = data.get("openid");
                mHeadurl = data.get("profile_image_url");
            } else if ("2".equals(mType)) {
                mUserName = data.get("nickname");
                mOpenid = data.get("openid");
                mHeadurl = data.get("headimgurl");
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(new String(data.get("result")));
                    mUserName = (String) jsonObject.get("name");
                    mOpenid = MD5.Md5((String) jsonObject.get("idstr"));
                    mHeadurl = (String) jsonObject.get("profile_image_url");
                } catch (Exception e) {

                }
            }
            commitCheck();
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
        }
    };

    public void loginOther(SHARE_MEDIA platform) {
        Config.dialog = DialogUtil.createLoadingDialog(this);
        mShareAPI.doOauthVerify(this, platform, umLoginListener);
    }

    public void commit() {
        mCommitFlag = false;
        showProgressDialog();
        RequestParams params = new RequestParams();
        try {
            params.put("username", mUserName);
            params.put("password", mPassword);

            // 第三方登录需要用到
            params.put("type", mType);
            params.put("openid", mOpenid);
            // params.put("", mHeadurl);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        String url = Constant.DOMAIN_NAME + Constant.LOGIN_URL;
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                mCommitFlag = true;
                dismissProgressDialog();
                showCustomToast("请检查网络！");
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                String result = new String(arg2);
                dismissProgressDialog();
                mCommitFlag = true;
                JSONObject jsonObject;
                try {
                    Gson gson = new Gson();
                    UserInfoVO vo = gson.fromJson(result, UserInfoVO.class);
                    if (Constant.RESULT_SUCCESS_CODE.equals(vo.getCode())) {
                        if (!"0".equals(mType)) {
                            vo.setHeadpic(mHeadurl);
                        }
                        AppApplication.getInstance().saveUserInfoVO(vo);
                        MessageMyHeadImp.getInstance().notifyWatcher(vo);
                        // openActivity(HomeActivity.class);
                        LoginActivity.this.finish();
                    } else {
                        showCustomToast(vo.getRetinfo());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShareAPI.onActivityResult(requestCode, resultCode, data);
    }

    public void commitRegist() {
        mCommitFlag = false;
        showProgressDialog();
        RequestParams params = new RequestParams();
        try {
            params.put("useracc", mUserName);
            params.put("email", mEmail);
            // 第三方登录需要用到
            params.put("openid", mOpenid);
            params.put("type", mType);
            params.put("phone", mPhone);

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        String url = Constant.DOMAIN_NAME + Constant.REGISTER_URL;
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                mCommitFlag = true;
                dismissProgressDialog();
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                dismissProgressDialog();
                mCommitFlag = true;
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(new String(arg2));
                    String str = (String) jsonObject.get("retinfo");
                    String code = (String) jsonObject.get("code");
                    if (Constant.RESULT_SUCCESS_CODE.equals(code)) {
                        // 登录
                        commit();
                    } else {
                        showCustomToast(str);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // 检查是否第三方登录过
    public void commitCheck() {
        mCommitFlag = false;
        showProgressDialog();
        RequestParams params = new RequestParams();
        try {
            params.put("type", mType);
            params.put("openid", mOpenid);
            // params.put("email", mEmailEt.getText().toString());

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        String url = Constant.DOMAIN_NAME + Constant.CHECK_OTHER_LOGIN_URL;
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                mCommitFlag = true;
                dismissProgressDialog();
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                dismissProgressDialog();
                mCommitFlag = true;
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(new String(arg2));
                    String str = (String) jsonObject.get("retinfo");
                    String code = (String) jsonObject.get("code");
                    String islogin = (String) jsonObject.get("islogin");
                    if (Constant.RESULT_SUCCESS_CODE.equals(code)) {
                        if ("1".equals(islogin)) {
                            // 直接登录
                            commit();
                        } else {
                            bindEmail();
                        }
                    } else {
                        showCustomToast(str);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @SuppressLint("NewApi")
    public void bindEmail() {
        final EditText et = new EditText(this);
        et.setBackgroundResource(R.color.C1);
        et.setHeight(BaseCommonUtils.dip2px(this, 50));
        et.setHint("请输入邮箱");
        et.setHintTextColor(this.getResources().getColor(R.color.text_hint_color));
        et.setTextSize(14);
        et.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.devider_bg, R.color.C1));

        final EditText phone = new EditText(this);
        phone.setBackgroundResource(R.color.C1);
        phone.setHeight(BaseCommonUtils.dip2px(this, 50));
        phone.setHint("请输入手机号，用于积分商品兑换");
        phone.setHintTextColor(this.getResources().getColor(R.color.text_hint_color));
        phone.setTextSize(14);
        LayoutParams params = (LayoutParams) phone.getLayoutParams();
        phone.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.devider_bg, R.color.C1));

        TextView view = new TextView(this);
        view.setHeight(BaseCommonUtils.dip2px(this, 10));
        view.setVisibility(View.INVISIBLE);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(et);
        layout.addView(view);
        layout.addView(phone);

        int color = this.getResources().getColor(R.color.sy_title_color);
        mDialog = new BSDialog(LoginActivity.this, "请输入信息", layout, color, new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mEmail = et.getText().toString();
                mPhone = phone.getText().toString();
                commitRegist();
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null)
            im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        return super.onTouchEvent(event);
    }
}
