
package com.bs.bsims.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.bs.bsims.R;
import com.bs.bsims.fragment.DanganChuQingInfoFragment;
import com.bs.bsims.fragment.DanganGongZuoInfoFragment;
import com.bs.bsims.fragment.DanganJiBenInfoFragment;
import com.bs.bsims.view.BSDanganTotitle;
import com.bs.bsims.view.BSDanganTotitle.DanganOnTopIndicatorListener;

import java.util.ArrayList;
import java.util.List;

public class DanganIndextwoActivity extends BaseActivity implements DanganOnTopIndicatorListener, OnClickListener {
    private FragmentManager mFragmentManager;
    private String uid;
    private ViewPager mViewPager;// 设置滑动页
    private TabPagerAdapter mPagerAdapter;
    private BSDanganTotitle mTopIndicator;
    private LinearLayout mLoadingLayout;

    @Override
    public void baseSetContentView() {
        // TODO Auto-generated method stub
        View layout = View.inflate(this, R.layout.danganindex, null);
        mContentLayout.addView(layout);
        // setContentView(R.layout.danganindex);
        initView();
    }

    @Override
    public boolean getDataResult() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void updateUi() {
        // TODO Auto-generated method stub

    }

    @Override
    public void initView() {
        mTitleTv.setText("员工档案");
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mPagerAdapter = new TabPagerAdapter(getSupportFragmentManager());
        mTopIndicator = (BSDanganTotitle) this.findViewById(R.id.dtop_indicator);
        mTopIndicator.setDanganOnTopIndicatorListener(this);
        // TODO Auto-generated method stub
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.invalidate();
        mPagerAdapter.addFragment();
        mFragmentManager = getSupportFragmentManager();
        mViewPager.setOffscreenPageLimit(2);

    }

    @Override
    public void bindViewsListener() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.img_head_back) {
            this.finish();
        }
    }

    @Override
    public void onIndicatorSelected(int index) {
        // TODO Auto-generated method stub
        mViewPager.setCurrentItem(index);
    }

    private class TabPagerAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener {
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
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mTopIndicator.setTabsDisplay(getApplicationContext(), position);
        }

        public void addFragment() {
            uid = getIntent().getStringExtra("uid");
            DanganJiBenInfoFragment j = new DanganJiBenInfoFragment(uid);
            DanganChuQingInfoFragment c = new DanganChuQingInfoFragment(uid);
            DanganGongZuoInfoFragment g = new DanganGongZuoInfoFragment(uid);
            mList.add(j);
            mList.add(c);
            mList.add(g);
            notifyDataSetChanged();

        }
    }

}
