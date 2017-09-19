
package com.bs.bsims.viewpager;

import android.annotation.SuppressLint;
import android.content.Context;
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

import com.bs.bsims.R;
import com.bs.bsims.activity.LoginActivity;
import com.bs.bsims.activity.NewFastLoginActivity;
import com.bs.bsims.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerActivity extends FragmentActivity implements OnPageChangeListener, OnClickListener {
    private ViewPager mVPActivity;
    private Fragment1 mFragment1;
    private Fragment2 mFragment2;
    private Fragment3 mFragment3;
    private Fragment4 mFragment4;
    private Fragment5 mFragment5;
    private Fragment6 mFragment6;
    private Fragment7 mFragment7;

    private List<Fragment> mListFragment = new ArrayList<Fragment>();
    private PagerAdapter mPgAdapter;

    private static final String SHAREDPREFERENCES_NAME = "my_pref";
    private static final String KEY_GUIDE_ACTIVITY = "guide_activity";
    private TextView mLoginTv, mFastinTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        initView();
        bindViewsListener();
    }

    @SuppressLint("NewApi")
    private void initView() {
        mLoginTv = (TextView) findViewById(R.id.login_tv);
        mFastinTv = (TextView) findViewById(R.id.fastin_tv);
        mLoginTv.setBackground(CommonUtils.setBackgroundShap(this, 2, R.color.C1, R.color.C15));
        mFastinTv.setBackground(CommonUtils.setBackgroundShap(this, 2, R.color.C1, R.color.C15));
        mVPActivity = (ViewPager) findViewById(R.id.vp_activity);

        mFragment1 = new Fragment1();
        mFragment2 = new Fragment2();
        mFragment3 = new Fragment3();
        mFragment4 = new Fragment4();
        mFragment5 = new Fragment5();
        // mFragment6 = new Fragment6();
        // mFragment7 = new Fragment7();
        mListFragment.add(mFragment1);
        mListFragment.add(mFragment2);
        mListFragment.add(mFragment3);
        mListFragment.add(mFragment4);
        mListFragment.add(mFragment5);
        // mListFragment.add(mFragment6);
        // mListFragment.add(mFragment7);
        // mFragment1.getView().findViewById(R.id.login_layout_1).setOnClickListener(this);
        mPgAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mListFragment);
        mVPActivity.setAdapter(mPgAdapter);
        mVPActivity.setOnPageChangeListener(this);
    }

    public void bindViewsListener() {
        mFastinTv.setOnClickListener(this);
        mLoginTv.setOnClickListener(this);
    }

    private boolean isFirstEnter(Context context, String className) {
        if (context == null || className == null || "".equalsIgnoreCase(className))
            return false;
        String mResultStr = context.getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE)
                .getString(KEY_GUIDE_ACTIVITY, "");// 取得所有类名 如 com.my.MainActivity
        if (mResultStr.equalsIgnoreCase("false"))
            return false;
        else
            return true;
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        // if (arg0 == mListFragment.size() - 1) {
        // Intent intent = new Intent();
        // intent.setClass(this, LoginActivity.class);
        // // intent.setClass(this, MainActivity.class);
        // finish();
        // this.startActivity(intent);
        // }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.login_tv:
                intent.setClass(this, LoginActivity.class);
                break;

            case R.id.fastin_tv:
                intent.setClass(this, NewFastLoginActivity.class);
                break;

            default:
                break;
        }

        this.startActivity(intent);
        this.finish();
    }

}
