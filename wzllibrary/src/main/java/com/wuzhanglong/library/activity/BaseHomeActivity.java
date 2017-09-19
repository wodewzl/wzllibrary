package com.wuzhanglong.library.activity;


import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.wuzhanglong.library.R;
import com.wuzhanglong.library.fragment.BaseFragment;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.mode.BaseVO;

import java.util.ArrayList;


//
public abstract class BaseHomeActivity extends BaseActivity  {
    public static final int TAB_ONE = 0;
    public static final int TAB_TWO = 1;
    public static final int TAB_THREE = 2;
    public static final int TAB_FOUR = 3;

    public BottomNavigationBar mBottomNavigationBar;
    public ArrayList<BaseFragment> mFragmentList = new ArrayList<>();
    private int mIndex = 0;
    private double mBackPressed;
    public abstract void initBottomBar();
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.base_home_activity, mBaseContentLayout);
//        setSwipeBackEnable(false);//首页不侧滑退出
    }

    @Override
    public void initView() {
        mFragmentList = (ArrayList<BaseFragment>) this.getIntent().getSerializableExtra("fragment_list");
        mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
    }

    @Override
    public void bindViewsListener() {
        mBottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                try {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    mBaseBackTv.setVisibility(View.GONE);
                    mIndex = position;
                    hideFragments(transaction);
                    if (!mFragmentList.get(position).isAdd()) {
                        mFragmentList.get(position).setAdd(true);
                        transaction.add(R.id.center_layout, mFragmentList.get(position));
                        transaction.show(mFragmentList.get(position));
                    } else {
                        transaction.show(mFragmentList.get(position));
                    }
                    transaction.commitAllowingStateLoss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTabUnselected(int position) {
            }

            @Override
            public void onTabReselected(int position) {
            }
        });

        initBottomBar();
    }

   @Override
    public void getData() {
//        showView();
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

    private void hideFragments(FragmentTransaction transaction) {
        for (int i = 0; i < mFragmentList.size(); i++) {
            if (null != mFragmentList.get(i)) {
                transaction.hide(mFragmentList.get(i));
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (isShow()) {
//            dismissProgressDialog();
        } else {
            if (mBackPressed + 3000 > System.currentTimeMillis()) {
                finish();
                super.onBackPressed();
            } else
                showCustomToast("再次点击，退出" + this.getResources().getString(R.string.app_name));
            mBackPressed = System.currentTimeMillis();
        }
    }

//    @Override
//    public boolean supportSlideBack() {
//        return false;
//    }
}
