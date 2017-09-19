
package com.wuzhanglong.library.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.wuzhanglong.library.R;
import com.wuzhanglong.library.adapter.GuideAdapter;
import com.wuzhanglong.library.fragment.GuideFragment;
import com.wuzhanglong.library.mode.EBMessageVO;
import com.wuzhanglong.library.utils.BaseCommonUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class GuideActivity extends FragmentActivity implements  OnClickListener {
    private ViewPager mVPActivity;
    private GuideFragment mFragment;
    private List<Fragment> mListFragment = new ArrayList<Fragment>();
    private PagerAdapter mPgAdapter;
    private static final String SHAREDPREFERENCES_NAME = "my_pref";
    private static final String KEY_GUIDE_ACTIVITY = "guide_activity";
    private TextView mLoginTv, mFastinTv;
//        private BSFlowIndicator mFlowIndicator;
    private CircleIndicator mFlowIndicator;
    private int[] mDrawableId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
                .LayoutParams.FLAG_FULLSCREEN);
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
        mFastinTv.setBackground(BaseCommonUtils.setBackgroundShap(this, 2, R.color.C1, R.color.C15));
        mVPActivity = (ViewPager) findViewById(R.id.vp_activity);
        mDrawableId=this.getIntent().getIntArrayExtra("drawableId");
        for (int i = 0; i < mDrawableId.length; i++) {
            GuideFragment fragment = new GuideFragment();
            fragment.setIndex(i);
            fragment.setDrawableId(mDrawableId[i]);
            mListFragment.add(fragment);
        }

        mPgAdapter = new GuideAdapter(getSupportFragmentManager(), mListFragment);
        mVPActivity.setAdapter(mPgAdapter);
//        mVPActivity.setOnPageChangeListener(this);
//        mFlowIndicator = (BSFlowIndicator) findViewById(R.id.galleryIndicator);
//        mFlowIndicator.setCount(3);
//        mFlowIndicator.setSeletion(0);
        mFlowIndicator = (CircleIndicator) findViewById(R.id.galleryIndicator);
        mFlowIndicator.setViewPager(mVPActivity);
    }

    public void bindViewsListener() {
        mFastinTv.setOnClickListener(this);
        mLoginTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
//        switch (v.getId()) {
//            case R.id.login_tv:
//                break;
//            default:
//                break;
//        }

//        intent.setClass(this,TestActivity.class);
//        this.startActivity(intent);
//        this.finish();
        EventBus.getDefault().post(new EBMessageVO("guide"));
    }

}
