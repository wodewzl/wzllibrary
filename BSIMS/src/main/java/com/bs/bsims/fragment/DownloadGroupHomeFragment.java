package com.bs.bsims.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.interfaces.UpdateCallback;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSIndexEditText;
import com.bs.bsims.view.BSTop3Indicator;

/**
 * @author peck
 * @Description:
 * @date 2015-6-12 下午2:27:01
 * @email 971371860@qq.com
 * @version V1.0
 */

public class DownloadGroupHomeFragment extends BaseFragment implements
		UpdateCallback, OnClickListener {

	private static final String TAG = "DownloadGroupHomeFragment";
	private BSIndexEditText mBSBsIndexEditText;
	private Context context;
	private Activity mActivity;

	private TextView mTextView;
	private ViewPager mViewPager;
	private BSTop3Indicator mBSTop3Indicator;
	private TabPagerAdapter mPagerAdapter;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.mActivity = activity;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean execute() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void executeSuccess() {
		// TODO Auto-generated method stub

	}

	@Override
	public void executeFailure() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getFragmentName() {
		// TODO Auto-generated method stub
		return TAG;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view;
		view = inflater.inflate(R.layout.fragment_downloadgrouphome, container,
				false);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initViews(view);
		// bindViewsListeners();
		// registBroadcast();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		 initDisplay();
	}

	private void initViews(View view) {
		mTextView = (TextView) view
				.findViewById(R.id.fragment_downloadgrouphome_loading);
		mTextView.setVisibility(View.VISIBLE);
		mViewPager = (ViewPager) view
				.findViewById(R.id.fragment_downloadgrouphome_view_pager);
		mPagerAdapter = new TabPagerAdapter(getFragmentManager());
		mBSTop3Indicator = (BSTop3Indicator) view
				.findViewById(R.id.fragment_downloadgrouphome_top_indicator);
		mBSTop3Indicator
				.setOnTopIndicatorListener(new com.bs.bsims.view.BSTop3Indicator.OnTopIndicatorListener() {

					@Override
					public void onIndicatorSelected(int index) {
						mViewPager.setCurrentItem(index);
					}
				});

		// mLoading = (TextView) view.findViewById(R.id.loading);
		// mLoadingLayout = (LinearLayout)
		// view.findViewById(R.id.loading_layout);
	}

	private void initDisplay() {
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.invalidate();
		new ThreadUtil(mActivity, this).start();
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
				mBSTop3Indicator.setTabsDisplay(mActivity, position);
		}

		public void addFragment() {
			CreativeIdeaAllFragment contacts;
			CreativeIdeaMyFragment departmnet;
//			if ("0".equals(mType)) {
//				contacts = new CreativeIdeaAllFragment(mAllResultVO, "1",
//						mIsboos, mIsall);
//				departmnet = new CreativeIdeaMyFragment(mMyResultVO, "2",
//						mIsboos, mIsall);
//			} else {
//				contacts = new CreativeIdeaAllFragment(mAllResultVO, mType,
//						mIsboos, "1");
//				departmnet = new CreativeIdeaMyFragment(mMyResultVO, mType,
//						mIsboos, "0");
//			}
//			mList.add(contacts);
//			mList.add(departmnet);
			notifyDataSetChanged();
		}
	}

}
