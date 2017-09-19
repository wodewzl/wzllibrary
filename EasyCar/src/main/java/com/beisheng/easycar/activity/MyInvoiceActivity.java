package com.beisheng.easycar.activity;

import com.beisheng.easycar.R;
import com.beisheng.easycar.application.AppApplication;
import com.beisheng.easycar.constant.Constant;
import com.beisheng.easycar.mode.UserInfoVO;
import com.loopj.android.http.RequestParams;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.interfaces.PostCallback;
import com.wuzhanglong.library.mode.BaseVO;

public class MyInvoiceActivity extends BaseActivity implements PostCallback {


    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.activity_my_invoice);
    }

    @Override
    public void initView() {
        mBaseHeadLayout.setBackgroundResource(R.color.Car2);
        mBaseTitleTv.setText("开具发票");
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

    public void commit() {
        showProgressDialog();
        final RequestParams params = new RequestParams();

        if (AppApplication.getInstance().getUserInfoVO().getUin() != null)
            params.put("$uin", AppApplication.getInstance().getUserInfoVO().getUin());
        params.put("$type", "");
        params.put("$name", "");
        params.put("$money", "");
        params.put("$recipients", "");
        params.put("$phone", "");
        params.put("$province", "");
        params.put("$city", "");
        params.put("$address", "");
        params.put("$code", "");


        String mUrl = Constant.MY_INVOICE_COMMIT_URL;
        HttpClientUtil.post(mActivity, this, mUrl, params, UserInfoVO.class, this);
    }

    @Override
    public void success(BaseVO vo) {

    }
}
