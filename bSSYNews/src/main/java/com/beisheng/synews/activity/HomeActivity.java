package com.beisheng.synews.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.synews.fragment.CommunityFragment;
import com.beisheng.synews.fragment.HomeFragment;
import com.beisheng.synews.fragment.HomeNewsVideoFragment;
import com.beisheng.synews.fragment.HomeNewsWebFragment;
import com.beisheng.synews.fragment.LifeFragment1;
import com.beisheng.synews.fragment.MyselfFragment;
import com.beisheng.synews.videoplay.MediaHelp;
import com.beisheng.synews.view.BSTabWidget;
import com.beisheng.synews.view.BSTabWidget.OnTabSelectedListener;
import com.bs.bsims.observer.MessageHtml;
import com.im.zhsy.R;

public class HomeActivity extends BaseActivity implements OnTabSelectedListener, OnClickListener {
	private BSTabWidget mTabWidget;
	private int mIndex = 0;
	public static final int TAB_ONE = 0;// 首页
	public static final int TAB_TWO = 1;// 直播
	public static final int TAB_THREE = 2;// 社区
	public static final int TAB_FOUR = 3;// 生活
	public static final int TAB_FIVE = 4;// 我的
	private HomeFragment mHomeFragment;
	private HomeNewsVideoFragment mVideoFragment;
	private CommunityFragment mCommunityFragment;
	// private LifeFragment mLifeFragment;
	private LifeFragment1 mLifeFragment;
	private MyselfFragment mMyselfFragment;
	private FragmentManager mFragmentManager;
	private double mBackPressed;

	public HomeNewsWebFragment webFragment;

	@Override
	public void baseSetContentView() {
		View.inflate(this, R.layout.home_activity, mBaseContentLayout);
		mSlidingLayout.setSlideable(false);
	}

	@Override
	public boolean getDataResult() {
		return true;
	}

	@Override
	public void initView() {
		mBaseBackTv.setVisibility(View.GONE);
		mTabWidget = (BSTabWidget) findViewById(R.id.tab_widget);
		mFragmentManager = getSupportFragmentManager();
		dismissProgressDialog();
	}

	@Override
	public void bindViewsListener() {
		mTabWidget.setOnTabSelectedListener(this);
	}

	@Override
	public void onTabSelected(int index) {
		try {
			FragmentTransaction transaction = mFragmentManager.beginTransaction();
			mBaseBackTv.setVisibility(View.GONE);
			base_back_tv_finish.setVisibility(View.GONE);
			switch (index) {
			case TAB_ONE:
				mBaseBackTv.setVisibility(View.VISIBLE);
				mBaseBackTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search, 0, 0, 0);
				mIndex = index;
				mBaseTitleTv.setText("十堰头条");
				hideFragments(transaction);
				if (null == mHomeFragment) {
					mHomeFragment = new HomeFragment();
					transaction.add(R.id.center_layout, mHomeFragment);
				} else {
					transaction.show(mHomeFragment);
				}

				mBaseBackTv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						openActivity(KeyWordActivity.class);
					}
				});

				break;
			case TAB_TWO:
				mIndex = index;
				mBaseTitleTv.setText("秦楚视频");
				mBaseBackTv.setVisibility(View.VISIBLE);
				mBaseBackTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search, 0, 0, 0);

				hideFragments(transaction);
				if (null == mVideoFragment) {
					mVideoFragment = new HomeNewsVideoFragment();
					transaction.add(R.id.center_layout, mVideoFragment);
				} else {
					transaction.show(mVideoFragment);
				}
				break;
			case TAB_THREE:
				mIndex = index;
				mBaseBackTv.setVisibility(View.VISIBLE);
				mBaseBackTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search, 0, 0, 0);
				mBaseTitleTv.setText("秦楚论坛");
				hideFragments(transaction);
				if (null == mCommunityFragment) {
					mCommunityFragment = new CommunityFragment();
					transaction.add(R.id.center_layout, mCommunityFragment);
				} else {
					transaction.show(mCommunityFragment);
				}
				mBaseBackTv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						openActivity(CommunitySerachActivity.class);
					}
				});

				break;
			case TAB_FOUR:
				mIndex = index;
				mBaseTitleTv.setText("生活服务");
				hideFragments(transaction);
				if (null == mLifeFragment) {
					mLifeFragment = new LifeFragment1();
					transaction.add(R.id.center_layout, mLifeFragment);
				} else {
					transaction.show(mLifeFragment);
				}

				break;
			case TAB_FIVE:
				mIndex = index;
				mBaseTitleTv.setText("我的");
				hideFragments(transaction);
				if (null == mMyselfFragment) {
					mMyselfFragment = new MyselfFragment();
					transaction.add(R.id.center_layout, mMyselfFragment);
				} else {
					transaction.show(mMyselfFragment);
				}
				break;

			default:
				break;
			}
			transaction.commitAllowingStateLoss();
		} catch (Exception e) {
		}
	}

	private void hideFragments(FragmentTransaction transaction) {
		if (null != mHomeFragment) {
			transaction.hide(mHomeFragment);
		}
		if (null != mVideoFragment) {
			transaction.hide(mVideoFragment);
		}
		if (null != mCommunityFragment) {
			transaction.hide(mCommunityFragment);
		}
		if (null != mLifeFragment) {
			transaction.hide(mLifeFragment);
		}
		if (null != mMyselfFragment) {
			transaction.hide(mMyselfFragment);
		}

	}

	@Override
	public void onClick(View v) {
	}

	@Override
	public void onResume() {
		super.onResume();
		onTabSelected(mIndex);
		mTabWidget.setTabsDisplay(this, mIndex);
	}

	@Override
	public void onBackPressed() {
		if (this.getWebFragment() != null) {
			MessageHtml.getInstance().notifyWatcher("3", mHomeFragment.getHtmlLink());
		} else {
			if (isShow()) {
				dismissProgressDialog();
			} else {
				if (mBackPressed + 3000 > System.currentTimeMillis()) {
					finish();
					super.onBackPressed();
				} else
					showCustomToast("再次点击，退出十堰新闻");
				mBackPressed = System.currentTimeMillis();
			}
		}

	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (mHomeFragment != null && mHomeFragment.isVisible()) {
			/* 然后在碎片中调用重写的onActivityResult方法 */
			mHomeFragment.onActivityResult(arg0, arg1, arg2);
		}

		if (mCommunityFragment != null && mCommunityFragment.isVisible()) {
			/* 然后在碎片中调用重写的onActivityResult方法 */
			mCommunityFragment.onActivityResult(arg0, arg1, arg2);
		}

		if (mMyselfFragment != null && mMyselfFragment.isVisible()) {
			/* 然后在碎片中调用重写的onActivityResult方法 */
			mMyselfFragment.onActivityResult(arg0, arg1, arg2);
		}

	}

	@Override
	public void onPause() {
		super.onPause();
		MediaHelp.pause();
	}

	public HomeNewsWebFragment getWebFragment() {
		return webFragment;
	}

	public void setWebFragment(HomeNewsWebFragment webFragment) {
		this.webFragment = webFragment;
	}

}
