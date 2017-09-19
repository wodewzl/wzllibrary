package com.wuzhanglong.library.test;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.wuzhanglong.library.R;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.mode.EBMessageVO;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Administrator on 2017/3/7.
 */

public class TestActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTv1, mTv2;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.test_activity, mBaseContentLayout);
    }

    @Override
    public void initView() {
        mTv1 = (TextView) findViewById(R.id.tv_1);
        mTv2 = (TextView) findViewById(R.id.tv_2);
    }

    @Override
    public void bindViewsListener() {
        mTv1.setOnClickListener(this);
        mTv2.setOnClickListener(this);
        EventBus.getDefault().register(this);
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
        Intent intent = new Intent();
//        switch (v.getId()) {
//            case R.id.tv_1:
//                intent.setClass(this, SecondActivity.class);
//                break;
//            case R.id.tv_2:
//                intent.setClass(this, GuideActivity.class);
//                int [] drawableId= new int[]{R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3};
//                intent.putExtra("drawableId",drawableId);
//                break;
//            default:
//                break;
//        }
        if (intent.getComponent() != null)
            startActivity(intent);
    }

    @Subscribe
    public void onEventMainThread(EBMessageVO event) {
        if("guide".equals(event.getMessage())){
            Intent intent= new Intent();
            intent.setClass(this,TestActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
