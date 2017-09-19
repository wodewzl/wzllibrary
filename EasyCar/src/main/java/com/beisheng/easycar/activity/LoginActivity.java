package com.beisheng.easycar.activity;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;

import com.beisheng.easycar.R;
import com.beisheng.easycar.application.AppApplication;
import com.beisheng.easycar.constant.Constant;
import com.beisheng.easycar.mode.UserInfoVO;
import com.loopj.android.http.RequestParams;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.interfaces.PostCallback;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.utils.BaseCommonUtils;

import net.anumbrella.customedittext.FloatLabelView;

public class LoginActivity extends BaseActivity implements PostCallback, View.OnClickListener {
    private FloatLabelView mAccount, mPassword;
    private Button mLoginBt;

    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.activity_login);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initView() {
        mBaseHeadLayout.setVisibility(View.GONE);
        mAccount = getViewById(R.id.account);
        mPassword = getViewById(R.id.password);
        mLoginBt = getViewById(R.id.login_bt);
        mLoginBt.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.Car2, R.color.Car2));
    }

    @Override
    public void bindViewsListener() {
        mLoginBt.setOnClickListener(this);

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
    public void success(BaseVO vo) {
        dismissProgressDialog();
        UserInfoVO userInfoVO = (UserInfoVO) vo;
        AppApplication.getInstance().saveUserInfoVO(userInfoVO.getData());
        showCustomToast(vo.getInfo());
        openActivity(HomeActivity.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_bt:
                if (mAccount.getEditText().getText().toString().length() == 0) {
                    showCustomToast("请输入账号");
                    return;
                }
                if (mPassword.getEditText().getText().toString().length() == 0) {
                    showCustomToast("请输入密码");
                    return;
                }
                showProgressDialog();
                final RequestParams params = new RequestParams();
                params.put("phone", mAccount.getEditText().getText().toString());
                params.put("pwd", mPassword.getEditText().getText().toString());
                String mUrl = Constant.LOGIN_URL;
                HttpClientUtil.post(mActivity, this, mUrl, params, UserInfoVO.class, this);
                break;
            default:
                break;
        }
    }
}
