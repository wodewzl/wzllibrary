package com.beisheng.easycar.activity;

import android.support.v4.content.ContextCompat;

import com.beisheng.easycar.R;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.mode.BaseVO;

public class MessageActivity extends BaseActivity {

    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.activity_message);
    }

    @Override
    public void initView() {
        mBaseTitleTv.setText("消息中心");
        mBaseHeadLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.Car2));

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
