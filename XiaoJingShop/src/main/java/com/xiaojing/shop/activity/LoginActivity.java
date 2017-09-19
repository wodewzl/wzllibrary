package com.xiaojing.shop.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.rey.material.widget.Button;
import com.rey.material.widget.TextView;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.fragment.BaseFragment;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.wuzhanglong.library.utils.SharePreferenceUtil;
import com.xiaojing.shop.R;
import com.xiaojing.shop.application.AppApplication;
import com.xiaojing.shop.constant.Constant;
import com.xiaojing.shop.fragment.TabFourFragment;
import com.xiaojing.shop.fragment.TabOneFragment2;
import com.xiaojing.shop.fragment.TabThreeFragment;
import com.xiaojing.shop.fragment.TabTwoFragment;
import com.xiaojing.shop.mode.UserInfoVO;

import net.anumbrella.customedittext.FloatLabelView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cz.msebera.android.httpclient.Header;


public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private FloatLabelView mAccountTv, mPasswordTv;
    private Button mLoginBt;
    private TextView mRegistTv, mResetTv, mPhoneTv;
    List<BaseFragment> mList = new ArrayList<>();


    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.login_activity);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initView() {
        mBaseTitleTv.setVisibility(View.GONE);
        mBaseBackTv.setVisibility(View.GONE);
        mBaseHeadLayout.setBackgroundResource(R.color.C1);
        mBaseHeadLayout.setVisibility(View.GONE);
//        int[] colors = new int[]{ContextCompat.getColor(this, R.color.C1), ContextCompat.getColor(this, R.color.C1), Color.parseColor("#5f000000")};
//        ViewColorUtil.viewGradient(colors, mBaseHeadLayout, 2);


        mAccountTv = getViewById(R.id.account);
        mPasswordTv = getViewById(R.id.password);
        mLoginBt = getViewById(R.id.login_bt);
        mLoginBt.setBackgroundDrawable(BaseCommonUtils.setBackgroundShap(this, BaseCommonUtils.dip2px(this, 2), R.color.C7, R.color.C7));
        mRegistTv = getViewById(R.id.regist_tv);
        mResetTv = getViewById(R.id.reset_tv);
        mPhoneTv = getViewById(R.id.phone_tv);

        TabOneFragment2 one = new TabOneFragment2();
        TabTwoFragment two = new TabTwoFragment();
        TabThreeFragment three = new TabThreeFragment();
        TabFourFragment four = new TabFourFragment();
        mList.add(one);
        mList.add(two);
        mList.add(three);
        mList.add(four);
    }

    @Override
    public void bindViewsListener() {
        mLoginBt.setOnClickListener(this);
        mRegistTv.setOnClickListener(this);
        mResetTv.setOnClickListener(this);
        mPhoneTv.setOnClickListener(this);
    }

    @Override
    public void getData() {
        HttpClientUtil.show(mThreadUtil);
    }

    @Override
    public void hasData(BaseVO vo) {
        UserInfoVO userInfoVO = (UserInfoVO) vo;
        AppApplication.getInstance().saveUserInfoVO(userInfoVO);
        Intent intent = new Intent();
        intent.putExtra("fragment_list", (Serializable) mList);
        intent.setClass(this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void noData(BaseVO vo) {
    }

    @Override
    public void noNet() {
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_bt:
                login();
                break;
            case R.id.regist_tv:
                openActivity(RegistActivity.class);
                break;
            case R.id.reset_tv:
//                openActivity(ResetPasswordActivity.class);
                String url =SharePreferenceUtil.getSharedpreferenceValue(mActivity,"reset_login_password","password_url");
                Bundle bundle= new Bundle();
                bundle.putString("url", url);
                open(WebViewActivity.class, bundle, 0);
                break;
            case R.id.phone_tv:
//                BaseCommonUtils.call(this,mPhoneTv.getText().toString());
                break;

            default:
                break;
        }
    }

    public void login() {
        showProgressDialog();
//        RequestParams paramsMap = new RequestParams();
//        paramsMap.put("username", mAccountTv.getEditText().getText().toString());
//        paramsMap.put("password", mPasswordTv.getEditText().getText().toString());
//        String mUrl = Constant.LOGIN_URL;
//        HttpClientUtil.post(mActivity, this, mUrl, paramsMap, UserInfoVO.class,this.getIntent().getStringExtra("type"));

        commit();
    }

    public void commit() {
        RequestParams params = new RequestParams();
        params.put("username", mAccountTv.getEditText().getText().toString());
        params.put("password", mPasswordTv.getEditText().getText().toString());
        String url = Constant.LOGIN_URL;


        final String allUrl = com.wuzhanglong.library.constant.BaseConstant.DOMAIN_NAME + url;
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(allUrl, params, new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                dismissProgressDialog();
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                dismissProgressDialog();
                final Gson gson = new Gson();
                String result = new String(arg2);
                UserInfoVO vo = (UserInfoVO) gson.fromJson(result, UserInfoVO.class);
                try {
                    if (com.wuzhanglong.library.constant.BaseConstant.RESULT_SUCCESS_CODE.equals(vo.getCode())) {
                        UserInfoVO userInfoVO = (UserInfoVO) vo.getDatas();
                        JPushInterface.setAlias(LoginActivity.this, userInfoVO.getJp_alias(), new TagAliasCallback() {
                            @Override
                            public void gotResult(int i, String s, Set<String> set) {
                            }
                        });

                        AppApplication.getInstance().saveUserInfoVO(userInfoVO);
                        Intent intent = new Intent();
                        intent.putExtra("fragment_list", (Serializable) mList);
                        if ("2".equals(LoginActivity.this.getIntent().getStringExtra("type"))) {
                            Intent intent1 = new Intent();
                            LoginActivity.this.setResult(1, intent);
                            LoginActivity.this.finish();
                        } else {
                            intent.setClass(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            LoginActivity.this.finish();
                        }


                    } else {
                        showCustomToast(vo.getError());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void setStatusBar() {
       SetStatusBarColor(ContextCompat.getColor(this,R.color.C7));
    }
}
