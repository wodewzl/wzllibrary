
package com.beisheng.synews.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.view.BSFlowIndicator;
import com.beisheng.synews.adapter.GuideAdapter;
import com.beisheng.synews.fragment.GuideFragment;
import com.im.zhsy.R;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends FragmentActivity implements OnPageChangeListener,
        OnClickListener {
    private ViewPager mVPActivity;
    private GuideFragment mFragment;
    private List<Fragment> mListFragment = new ArrayList<Fragment>();
    private PagerAdapter mPgAdapter;
    private static final String SHAREDPREFERENCES_NAME = "my_pref";
    private static final String KEY_GUIDE_ACTIVITY = "guide_activity";
    private TextView mLoginTv, mFastinTv;
        private BSFlowIndicator mFlowIndicator;
//    private CircleIndicator mFlowIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_activity);
        initView();
        bindViewsListener();
    }

    @SuppressLint("NewApi")
    private void initView() {
        mLoginTv = (TextView) findViewById(R.id.login_tv);
        mFastinTv = (TextView) findViewById(R.id.fastin_tv);
        mLoginTv.setBackground(BaseCommonUtils.setBackgroundShap(this, 2, R.color.C1, R.color.C15));
        mFastinTv.setBackground(BaseCommonUtils.setBackgroundShap(this, 2, R.color.C1, R.color
                .C15));
        mVPActivity = (ViewPager) findViewById(R.id.vp_activity);

        for (int i = 0; i < 3; i++) {
            GuideFragment fragment = new GuideFragment();
            fragment.setIndex(i);
            mListFragment.add(fragment);
        }

        mPgAdapter = new GuideAdapter(getSupportFragmentManager(), mListFragment);
        mVPActivity.setAdapter(mPgAdapter);
        mVPActivity.setOnPageChangeListener(this);
        mFlowIndicator = (BSFlowIndicator) findViewById(R.id.galleryIndicator);
        mFlowIndicator.setCount(3);
        mFlowIndicator.setSeletion(0);

//        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.galleryIndicator);
//        indicator.setViewPager(mVPActivity);
    }

    public void bindViewsListener() {
        mFastinTv.setOnClickListener(this);
        mLoginTv.setOnClickListener(this);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        mFlowIndicator.setSeletion(arg0);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.login_tv:
                intent.setClass(this, HomeActivity.class);
                break;
            default:
                break;
        }

        this.startActivity(intent);
        this.finish();
    }

}
