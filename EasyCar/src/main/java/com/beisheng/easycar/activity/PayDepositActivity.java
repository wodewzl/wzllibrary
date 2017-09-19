package com.beisheng.easycar.activity;

import com.beisheng.easycar.R;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.mode.BaseVO;

public class PayDepositActivity extends BaseActivity {


    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.activity_pay_deposit);
    }

    @Override
    public void initView() {

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
