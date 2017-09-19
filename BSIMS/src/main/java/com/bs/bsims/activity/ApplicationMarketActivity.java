/**
 * 
 */

package com.bs.bsims.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.bs.bsims.R;
import com.bs.bsims.fragment.AppMarketLeftFragment;
import com.bs.bsims.fragment.AppMarketRightFragment;
import com.bs.bsims.view.BSTopIndicator;
import com.bs.bsims.view.BSTopIndicator.OnTopIndicatorListener;

import java.util.ArrayList;
import java.util.List;

/**
 * BS北盛最帅程序员 Copyright (c) 2016 湖北北盛科技有限公司
 * 
 * @author 梁骚侠
 * @date 2016-8-30
 * @version 2.0
 */
public class ApplicationMarketActivity extends BaseActivity implements OnTopIndicatorListener {
    private int[] mDrawableIds = new int[] {
            R.drawable.app_makert_left,
            R.drawable.app_market_right
    };
    private CharSequence[] mLabels = new CharSequence[] {
            "精品应用", "我的应用"
    };
    private BSTopIndicator mTopIndicator;
    private TabPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private AppMarketLeftFragment marketLeftFragment;
    private AppMarketRightFragment marketRightFragment;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.app_makert_index, mContentLayout);
    }

    @Override
    public boolean getDataResult() {
        return true;
    }

    private class TabPagerAdapter extends FragmentStatePagerAdapter implements
            ViewPager.OnPageChangeListener {
        private List<Fragment> mList;

        public TabPagerAdapter(FragmentManager fm) {
            super(fm);
            mViewPager.setOnPageChangeListener(this);
            mList = new ArrayList<Fragment>();
        }

        @Override
        public Fragment getItem(int position) {
            return mList.get(position);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int position, float positionOffset,
                int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mTopIndicator.setTabsDisplay(ApplicationMarketActivity.this, position);
        }

        public void addFragment() {
            marketLeftFragment = new AppMarketLeftFragment();
            marketRightFragment = new AppMarketRightFragment();
            mList.add(marketLeftFragment);
            mList.add(marketRightFragment);

            notifyDataSetChanged();
        }
    }

    @Override
    public void updateUi() {

    }

    @Override
    public void initView() {
        mTitleTv.setText("应用市场");
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mTopIndicator = (BSTopIndicator) findViewById(R.id.top_indicator);
        mTopIndicator.setmLabels(mLabels);
        mTopIndicator.setmDrawableIds(mDrawableIds);
        mTopIndicator.updateUI(this);
        mPagerAdapter = new TabPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.invalidate();
        mPagerAdapter.addFragment();
    }

    @Override
    public void bindViewsListener() {
        mTopIndicator.setOnTopIndicatorListener(this);
    }

    @Override
    public void onIndicatorSelected(int index) {
        if (marketLeftFragment.getAppMakertModel() == null && index == 1) {
            return;
        }
        mViewPager.setCurrentItem(index);
        if (index == 1) {
            // Bundle bundle = new Bundle();
            // bundle.putSerializable("model", marketLeftFragment.getAppMakertModel());
            // marketRightFragment.setArguments(bundle);
            marketRightFragment.setAppMakertModel(marketLeftFragment.getAppMakertModel());
        }
    }
}
