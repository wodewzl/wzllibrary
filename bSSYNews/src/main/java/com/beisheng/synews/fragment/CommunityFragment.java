package com.beisheng.synews.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.fragment.BaseFragment;
import com.beisheng.base.http.HttpClientUtil;
import com.beisheng.base.interfaces.UpdateCallback;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.ThreadUtil;
import com.beisheng.synews.activity.CommunityAddActivity;
import com.beisheng.synews.activity.CommunityItemActivity;
import com.beisheng.synews.activity.LoginActivity;
import com.beisheng.synews.adapter.HomeNewsViewPagerAdapter;
import com.beisheng.synews.application.AppApplication;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.mode.ChannelManage;
import com.beisheng.synews.mode.CommunityVO;
import com.beisheng.synews.view.ColumnHorizontalScrollView;
import com.bs.bsims.observer.BSApplictionObserver.Watcher;
import com.google.gson.Gson;
import com.im.zhsy.R;

import java.util.ArrayList;
import java.util.HashMap;

public class CommunityFragment extends BaseFragment implements OnClickListener, OnPageChangeListener, UpdateCallback, Watcher {
	private String TAG = "CommunityFragment";
	public static final int MORE_ITEM_RESULT = 1;// 点击更多栏目返回码
	private BaseActivity mActivity;
	private HomeNewsViewPagerAdapter mAdapter;
	protected ViewPager mViewPager;
	private int mColumnSelectIndex = 0;// 当前选中的栏目
	private ArrayList<Fragment> mFragments;
	private ColumnHorizontalScrollView mColumnHorizontalScrollView;
	private LinearLayout mScrollViewItemContent;
	private int mScreenWidth;
	private int mItemWidth = 0;
	protected ImageView mShadeLeftImg;
	protected ImageView mShadeRightImg;
	protected LinearLayout mMoreClumnsLayout;// 加号更多
	protected RelativeLayout mAllcolumnLayout;// 加号左边所有选项的layout
	private LinearLayout mMoreImg;
	private ArrayList<CommunityVO> mCommunityListDef;// 用户栏目
	private ArrayList<CommunityVO> mCommunityAll;// 全部栏目
	private CommunityVO mCommunityVO;

	public static CommunityFragment newInstance() {
		CommunityFragment communityFragment = new CommunityFragment();
		return communityFragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = (BaseActivity) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.community_fragment, container, false);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initViews(view);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		new ThreadUtil(mActivity, this).start();
	}

	private void initViews(View view) {
		mActivity.mBaseHeadLayout.setVisibility(View.VISIBLE);
		mActivity.mBaseOkTv.setVisibility(View.VISIBLE);
		mActivity.mBaseOkTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		mActivity.mBaseOkTv.setText("发帖");
		mCommunityListDef = ((ArrayList<CommunityVO>) ChannelManage.getManage().getCommunityDef());
		mCommunityAll = ((ArrayList<CommunityVO>) ChannelManage.getManage().getCommunity());
		mColumnHorizontalScrollView = (ColumnHorizontalScrollView) view.findViewById(R.id.top_scrollview);
		mScrollViewItemContent = (LinearLayout) view.findViewById(R.id.scrollview_item_content);
		mShadeLeftImg = (ImageView) view.findViewById(R.id.shade_left);
		mShadeRightImg = (ImageView) view.findViewById(R.id.shade_right);
		mMoreImg = (LinearLayout) view.findViewById(R.id.more_columns_layout);
		mMoreClumnsLayout = (LinearLayout) view.findViewById(R.id.more_columns_layout);
		mAllcolumnLayout = (RelativeLayout) view.findViewById(R.id.all_column_layout);
		mScreenWidth = BaseCommonUtils.getScreenWidth(mActivity);
		mItemWidth = mScreenWidth / 7;// 一个Item宽度为屏幕的1/7

		mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
		if (mCommunityListDef.size() > 0) {
			mViewPager.setOffscreenPageLimit(mCommunityListDef.size());
		} else {
			mViewPager.setOffscreenPageLimit(4);
		}
		mFragments = new ArrayList<Fragment>();
		bindViewsListener();
		// initTabColumn();
		initColumnData();
	}

	public boolean getData() {
		try {
			Gson gson = new Gson();
			HashMap<String, String> map = new HashMap<String, String>();
			if (AppApplication.getInstance().getUserInfoVO() != null)
				map.put("tuid", AppApplication.getInstance().getUserInfoVO().getTuid());
			if (mActivity.hasNetWork()) {
				String jsonStr = HttpClientUtil.getRequest(mActivity, Constant.DOMAIN_NAME + Constant.COMMUNITY_ITEM, map);
				mCommunityVO = gson.fromJson(jsonStr, CommunityVO.class);
				mActivity.saveJsonCache(Constant.COMMUNITY_ITEM, map, jsonStr);
			} else {
				String oldStr = mActivity.getCacheFromDatabase(Constant.COMMUNITY_ITEM, map);
				mCommunityVO = gson.fromJson(oldStr, CommunityVO.class);
			}
			if (Constant.RESULT_SUCCESS_CODE.equals(mCommunityVO.getCode())) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/** 获取Column栏目 数据 */
	private void initColumnData() {
		initTabColumn();
		initFragment();
	}

	/**
	 * 初始化Column栏目项
	 */
	private void initTabColumn() {
		mScrollViewItemContent.removeAllViews();
		int count = ChannelManage.getManage().getCommunityDef().size();
		mColumnHorizontalScrollView.setParam(mActivity, mScreenWidth, mScrollViewItemContent, mShadeLeftImg, mShadeRightImg, mMoreClumnsLayout, mAllcolumnLayout);
		for (int i = 0; i < count; i++) {
			final CommunityVO vo = mCommunityListDef.get(i);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.leftMargin = BaseCommonUtils.dip2px(mActivity, 4);
			params.rightMargin = BaseCommonUtils.dip2px(mActivity, 4);
			TextView columnTextView = new TextView(mActivity);
			columnTextView.setTextAppearance(mActivity, R.style.top_category_scroll_view_item_text);
			columnTextView.setGravity(Gravity.CENTER);
			columnTextView.setPadding(BaseCommonUtils.dip2px(mActivity, 10), BaseCommonUtils.dip2px(mActivity, 2), BaseCommonUtils.dip2px(mActivity, 10), BaseCommonUtils.dip2px(mActivity, 2));
			columnTextView.setId(i);
			columnTextView.setText(vo.getName());

			columnTextView.setTextColor(getResources().getColorStateList(R.color.top_category_scroll_text_color_day));
			if (mColumnSelectIndex == i) {
				columnTextView.setSelected(true);
			}
			columnTextView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if ("my".equals(vo.getFid()) && AppApplication.getInstance().getUserInfoVO() == null) {
						mActivity.openActivity(LoginActivity.class);
						return;
					}
					for (int i = 0; i < mScrollViewItemContent.getChildCount(); i++) {
						View localView = mScrollViewItemContent.getChildAt(i);
						if (localView != v)
							localView.setSelected(false);
						else {
							localView.setSelected(true);
							mViewPager.setCurrentItem(i);
						}
					}
				}
			});
			mScrollViewItemContent.addView(columnTextView, i, params);
		}
	}

	public void bindViewsListener() {
		mViewPager.setOnPageChangeListener(this);
		mActivity.mBaseOkTv.setOnClickListener(this);
		mMoreImg.setOnClickListener(this);
		// MessageMyHeadImp.getInstance().add(this);
	}

	private void initFragment() {
		mFragments.clear();// 清空
		int count = ChannelManage.getManage().getCommunityDef().size();
		for (int i = 0; i < count; i++) {
			Bundle data = new Bundle();
			data.putString("name", mCommunityListDef.get(i).getName());
			data.putString("id", mCommunityListDef.get(i).getFid());
			CommunityTabFragment fragment = new CommunityTabFragment();
			fragment.setArguments(data);
			mFragments.add(fragment);
		}
		FragmentActivity activity = (FragmentActivity) mActivity;
		HomeNewsViewPagerAdapter adapetr = new HomeNewsViewPagerAdapter(getChildFragmentManager(), mFragments);
		mViewPager.setAdapter(adapetr);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.base_ok_tv:
			if (AppApplication.getInstance().getUserInfoVO() == null) {
				mActivity.openActivity(LoginActivity.class);
				return;
			}
			Bundle bundle = new Bundle();
			int count = mViewPager.getCurrentItem();
			if (count > 4) {
				bundle.putString("fid", mCommunityListDef.get(count).getFid());// 发帖fid是固定的
			} else {
				bundle.putString("fid", "88");// 发帖fid是固定的
			}

			bundle.putString("type", "1");
			mActivity.openActivity(CommunityAddActivity.class, bundle, 0);
			break;

		case R.id.more_columns_layout:
			mActivity.openActivityForResult(CommunityItemActivity.class, MORE_ITEM_RESULT);
			break;

		default:
			break;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int position) {
		mViewPager.setCurrentItem(position);
		selectTab(position);
	}

	// 选择的Column里面的Tab
	private void selectTab(int tab_postion) {
		mColumnSelectIndex = tab_postion;
		for (int i = 0; i < mScrollViewItemContent.getChildCount(); i++) {
			View checkView = mScrollViewItemContent.getChildAt(tab_postion);
			int k = checkView.getMeasuredWidth();
			int l = checkView.getLeft();
			int i2 = l + k / 2 - mScreenWidth / 2;
			mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
		}
		// 判断是否选中
		for (int j = 0; j < mScrollViewItemContent.getChildCount(); j++) {
			View checkView = mScrollViewItemContent.getChildAt(j);
			boolean ischeck;
			if (j == tab_postion) {
				ischeck = true;
			} else {
				ischeck = false;
			}
			checkView.setSelected(ischeck);
		}
	}

	public String getFragmentName() {
		return TAG;// 不知道该方法有没有用
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (!hidden) {
			mActivity.mBaseHeadLayout.setVisibility(View.VISIBLE);
			mActivity.mBaseOkTv.setVisibility(View.VISIBLE);
			mActivity.mBaseOkTv.setText("发帖");
			mActivity.mBaseOkTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			// mActivity.mBaseOkTv.setOnClickListener(this);
			initTabColumn();
		}
	}

	@Override
	public boolean execute() {
		return getData();
	}

	@Override
	public void executeSuccess() {
		if (mCommunityListDef.size() == 0) {
			mCommunityListDef = (ArrayList<CommunityVO>) mCommunityVO.getCate().get(0).getList();
			ChannelManage.getManage().saveCommunityDef(mCommunityListDef);
			initColumnData();
		}
		mCommunityAll = (ArrayList<CommunityVO>) mCommunityVO.getCate();
		ChannelManage.getManage().saveCommunity(mCommunityAll);
		// initColumnData();
	}

	@Override
	public void executeFailure() {

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		switch (resultCode) {
		case MORE_ITEM_RESULT:
			if (intent == null)
				return;
			mCommunityListDef = ((ArrayList<CommunityVO>) ChannelManage.getManage().getCommunityDef());
			initColumnData();

			if (intent.hasExtra("position")) {
				final int position = intent.getIntExtra("position", 0);
				mColumnHorizontalScrollView.postDelayed(new Runnable() {
					@Override
					public void run() {
						mViewPager.setCurrentItem(position);
					}
				}, 10);
			} else {
				mColumnHorizontalScrollView.postDelayed(new Runnable() {
					@Override
					public void run() {
						mViewPager.setCurrentItem(0);
						selectTab(0);
					}
				}, 10);
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void updateNotify(Object content) {
		// 登录后论坛更新我的帖子
		for (int i = 0; i < ChannelManage.getManage().getCommunityDef().size(); i++) {
			if (AppApplication.getInstance().getUserInfoVO() == null) {
				if ("我的".equals(ChannelManage.getManage().getCommunityDef().get(i).getName()))
					ChannelManage.getManage().getCommunityDef().remove(i);
			} else {
				if ("我的".equals(ChannelManage.getManage().getCommunityDef().get(i).getName())) {
					break;
				}
				if (i == ChannelManage.getManage().getCommunityDef().size() - 1) {
					CommunityVO vo = new CommunityVO();
					vo.setFid("my");
					vo.setName("我的");
					ChannelManage.getManage().getCommunityDef().add(vo);
				}
			}
		}

		initColumnData();
	}
}
