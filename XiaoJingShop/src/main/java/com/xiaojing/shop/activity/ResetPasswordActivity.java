package com.xiaojing.shop.activity;

import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.mode.BaseVO;
import com.xiaojing.shop.R;

import net.anumbrella.customedittext.FloatLabelView;

public class ResetPasswordActivity extends BaseActivity {
    private FloatLabelView mPhoneTv, mPasswordTv, mNewPasswordTv, mSurePasswordTv;


    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.reset_password_activity);
    }

    @Override
    public void initView() {
        mBaseTitleTv.setText("修改密码");
        mPhoneTv = getViewById(R.id.account);
        mPasswordTv = getViewById(R.id.new_password);
        mNewPasswordTv = getViewById(R.id.new_password);
        mSurePasswordTv = getViewById(R.id.sure_password);
    }

    @Override
    public void bindViewsListener() {

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
}
