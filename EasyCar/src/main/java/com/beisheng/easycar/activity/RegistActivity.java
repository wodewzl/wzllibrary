package com.beisheng.easycar.activity;

import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.beisheng.easycar.R;
import com.beisheng.easycar.application.AppApplication;
import com.beisheng.easycar.constant.Constant;
import com.beisheng.easycar.mode.UserInfoVO;
import com.loopj.android.http.RequestParams;
import com.rey.material.widget.Button;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.interfaces.PostCallback;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.utils.BaseCommonUtils;

public class RegistActivity extends BaseActivity implements View.OnClickListener, PostCallback {
    private Button mLoginTv;
    private TextView mRegistCodeTv;
    private EditText mPhoneEt, mRegistCodeEt, mPasswordEt, mSurePasswordEt, mInvitatCodeEt;
    private boolean mCodeStae = true;

    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.activity_regist);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initView() {
        mBaseHeadLayout.setVisibility(View.GONE);
        mPhoneEt = getViewById(R.id.phone_et);
        mRegistCodeEt = getViewById(R.id.regist_code_et);
        mRegistCodeTv = getViewById(R.id.regist_code_tv);
        mPasswordEt = getViewById(R.id.password_et);
        mSurePasswordEt = getViewById(R.id.sure_password_et);
        mInvitatCodeEt = getViewById(R.id.invitat_code_et);
        mLoginTv = getViewById(R.id.login_bt);
        mLoginTv.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.Car2, R.color.Car2));

    }

    @Override
    public void bindViewsListener() {
        mLoginTv.setOnClickListener(this);
        mRegistCodeTv.setOnClickListener(this);
    }

    @Override
    public void getData() {
        HttpClientUtil.show(mThreadUtil);
    }

    @Override
    public void hasData(BaseVO vo) {

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
            case R.id.regist_code_tv:
                if (mPhoneEt.getText().toString().length() == 0) {
                    showCustomToast("请输入手机号");
                    return;
                }
                if (mCodeStae) {
                    mCodeStae = false;
                    getCode();
                    DaoJishi();
                }
                break;
            case R.id.login_bt:
                if (mPhoneEt.getText().toString().length() == 0) {
                    showCustomToast("请输入手机号");
                    return;
                }
                if (mRegistCodeEt.getText().toString().length() == 0) {
                    showCustomToast("请输入验证码");
                    return;
                }
                if (mPasswordEt.getText().toString().length() == 0) {
                    showCustomToast("请输入密码");
                    return;
                }
                if (mSurePasswordEt.getText().toString().length() == 0) {
                    showCustomToast("请输入确认密码");
                    return;
                }
                commitUserInfo();
                break;
            default:
                break;
        }
    }


    public void commitUserInfo() {
        showProgressDialog();
        final RequestParams params = new RequestParams();
        params.put("phone", mPhoneEt.getText().toString());
        params.put("code", mRegistCodeEt.getText().toString());
        params.put("pwd", mPasswordEt.getText().toString());
        params.put("again", mSurePasswordEt.getText().toString());
        params.put("invitation", mInvitatCodeEt.getText().toString());
        String mUrl = Constant.REGIST_URL;
        HttpClientUtil.post(mActivity, this, mUrl, params, UserInfoVO.class, this);
    }

    public void DaoJishi() {
        CountDownTimer timer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long arg0) {
                mRegistCodeTv.setText("发送验证码(" + arg0 / 1000 + ")");
            }

            @Override
            public void onFinish() {
                mCodeStae = true;
                mRegistCodeTv.setText("发送验证码");
            }
        };
        timer.start();
    }

    public void getCode() {
        RequestParams paramsMap = new RequestParams();
        paramsMap.put("phone", mPhoneEt.getText().toString());
        String mUrl = Constant.GET_CODE;
        HttpClientUtil.get(mActivity, this, mUrl, paramsMap, null);
    }

    @Override
    public void success(BaseVO vo) {
        UserInfoVO userInfoVO = (UserInfoVO) vo;
        AppApplication.getInstance().saveUserInfoVO(userInfoVO.getData());
        showCustomToast(vo.getInfo());
        mBaseContentLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                openActivity(HomeActivity.class);
            }
        }, 500);
    }
}
