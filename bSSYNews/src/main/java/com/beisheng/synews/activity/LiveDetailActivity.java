package com.beisheng.synews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.http.HttpClientUtil;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.ThreadUtil;
import com.beisheng.base.view.BSAutoSwipeRefreshLayout;
import com.beisheng.base.view.BSPopWindowsBottom;
import com.beisheng.base.view.BSPopWindowsBottom.PopCallback;
import com.beisheng.synews.adapter.LiveDetailAdapter;
import com.beisheng.synews.application.AppApplication;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.fragment.BottomFragmentPhoto;
import com.beisheng.synews.interfaces.LoadMoreListener;
import com.beisheng.synews.mode.LiveVO;
import com.beisheng.synews.videoplay.MediaHelp;
import com.beisheng.synews.view.BSListViewLoadMore;
import com.google.gson.Gson;
import com.im.zhsy.R;

import java.util.HashMap;
import java.util.List;

public class LiveDetailActivity extends BaseActivity implements OnRefreshListener, LoadMoreListener, OnScrollListener, OnClickListener {
	private LiveVO mLiveVO;
	private LiveDetailAdapter mAdapter;
	protected BSAutoSwipeRefreshLayout mSwipeLayout;
	protected BSListViewLoadMore mListView;
	private String mLid;
	private String mPage = "1";
	private int mState = 0; // 0为首次,1为下拉刷新 ，2为加载更多
	private TextView mLiveTitleTv;

	private View currentItemView;
	private int scrollDistance;// 记录切换到横屏时滑动的距离
	private List<String> path;
	private BSPopWindowsBottom mPop;
	private String[] array = { "照片", "视频", "取消" };
	private LinearLayout mBottomLayout;
	private BottomFragmentPhoto mBottomFragment;
	private int lastItemIndex;// 当前ListView中最后一个Item的索引
	private boolean mLodMore = true;// 控制多次加载，即当前加载就不在会加载直到结束
	private TextView mDesTv;
	private ImageView mImg;

	@Override
	public void baseSetContentView() {
		View.inflate(this, R.layout.live_detail_activity, mBaseContentLayout);
	}

	@Override
	public boolean getDataResult() {
		return getData();
	}

	@Override
	public void initView() {
		mBaseTitleTv.setText("直播间");
		mLiveTitleTv = (TextView) findViewById(R.id.live_title_tv);
		mAdapter = new LiveDetailAdapter(this);
		mSwipeLayout = (BSAutoSwipeRefreshLayout) findViewById(R.id.swipe_container);
		setSwipeRefreshLayoutColors(mSwipeLayout);
		mSwipeLayout.autoRefresh();
		mListView = (BSListViewLoadMore) findViewById(R.id.list_view);
		View headLayout = LayoutInflater.from(this).inflate(R.layout.live_detail_list_head, null);
		mDesTv = (TextView) headLayout.findViewById(R.id.desc_tv);
		mImg = (ImageView) headLayout.findViewById(R.id.img);
		mListView.addHeaderView(headLayout);
		mListView.setAdapter(mAdapter);
		mBottomLayout = (LinearLayout) findViewById(R.id.bottom_layout);
		initData();
		initPop();

	}

	public void initData() {
		Intent intent = this.getIntent();
		mLid = intent.getStringExtra("id");

	}

	public void initPop() {
		PopCallback callback = new PopCallback() {

			@Override
			public void callback(String str, int position) {
				Bundle bundle = new Bundle();
				bundle.putString("id", mLid);
				switch (position) {
				case 0:
					openActivity(LiveAddActivity.class, bundle, 0);
					break;
				case 1:
					openActivity(LiveRecordActivity.class, bundle, 0);
					break;

				default:
					break;
				}
			}
		};
		mPop = new BSPopWindowsBottom(this, array, callback);
	}

	@Override
	public void bindViewsListener() {
		mBaseOkTv.setOnClickListener(this);
		mSwipeLayout.setOnRefreshListener(this);
		mListView.setLoadMoreListener(this);
		mListView.setOnScrollListener(this);
	}

	public boolean getData() {
		try {
			Gson gson = new Gson();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("uid", AppApplication.getInstance().getUid());
			map.put("lid", mLid);
			map.put("page", mPage);

			if (hasNetWork()) {
				String jsonStr = HttpClientUtil.getRequest(this, Constant.DOMAIN_NAME + Constant.LIVE_DETAIL_URL, map);
				mLiveVO = gson.fromJson(jsonStr, LiveVO.class);
				saveJsonCache(Constant.LIVE_DETAIL_URL, map, jsonStr);

			} else {
				String oldStr = getCacheFromDatabase(Constant.LIVE_DETAIL_URL, map);
				mLiveVO = gson.fromJson(oldStr, LiveVO.class);
			}

			if (Constant.RESULT_SUCCESS_CODE.equals(mLiveVO.getCode())) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void executeSuccess() {
		super.executeSuccess();
		mLodMore = true;
		mSwipeLayout.setRefreshing(false);
		mListView.setVisibility(View.VISIBLE);

		mImageLoader.displayImage(mLiveVO.getThumb(), mImg, mOptions);

		if ("1".equals(mLiveVO.getLive())) {
			mBaseOkTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.live_add_icon, 0);
			mBaseOkTv.setVisibility(View.VISIBLE);
		} else {
			mBaseOkTv.setVisibility(View.GONE);
		}
		if (BaseCommonUtils.parseInt(mLiveVO.getTotal()) > BaseCommonUtils.parseInt(mLiveVO.getPage())) {
			mListView.showFooterView(true);
		} else {
			mListView.showFooterView(false);

		}
		mLiveTitleTv.setText("                           " + mLiveVO.getTitle());
		mDesTv.setText("      " + mLiveVO.getDescriptions());
		if (mLiveVO.getList() != null) {
			if (1 == mState) {
				mAdapter.mList.size();
				mAdapter.updateDataFrist(mLiveVO.getList());
			} else if (2 == mState) {
				mAdapter.mList.size();
				mAdapter.updateDataLast(mLiveVO.getList());
			} else {
				mAdapter.updateData(mLiveVO.getList());
			}
		}

		try {
			// 底部View
			mBottomFragment = new BottomFragmentPhoto();
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction transaction = fragmentManager.beginTransaction();
			transaction.replace(R.id.bottom_layout, mBottomFragment);
			mBottomFragment.setLiveVo(mLiveVO);
			transaction.commit();
		} catch (Exception e) {
		}

	}

	@Override
	public void executeFailure() {
		super.executeFailure();
		mLodMore = true;
		mSwipeLayout.setRefreshing(false);
		mListView.setVisibility(View.VISIBLE);
		mListView.showFooterView(false);
		mAdapter.updateDataFrist(null);
		if (mLiveVO != null) {
			showCustomToast(mLiveVO.getRetinfo());
			if ("1".equals(mLiveVO.getLive())) {
				mBaseOkTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.live_add_icon, 0);
				mBaseOkTv.setVisibility(View.VISIBLE);
			} else {
				mBaseOkTv.setVisibility(View.GONE);
			}
		} else {
			showCustomToast("亲，请检查网络哦");
		}

	}

	@Override
	public void onRefresh() {
		mState = 1;
		mPage = "1";
		new ThreadUtil(this, this).start();
	}

	@Override
	public void loadMore() {
		if (BaseCommonUtils.parseInt(mLiveVO.getTotal()) > BaseCommonUtils.parseInt(mLiveVO.getPage()) && mLodMore) {
			mLodMore = false;
			mState = 2;
			mPage = (BaseCommonUtils.parseInt(mPage) + 1) + "";
			new ThreadUtil(this, this).start();
		}
	}

	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		if ((mAdapter.indexPostion < mListView.getFirstVisiblePosition() || mAdapter.indexPostion > mListView.getLastVisiblePosition()) && mAdapter.isPlaying) {
			mAdapter.indexPostion = -1;
			mAdapter.isPlaying = false;
			mAdapter.notifyDataSetChanged();
			MediaHelp.release();
		}
		lastItemIndex = arg1 + arg2 - 1 - 1;

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// 停止状态的判断条件，此处给去掉，影响体验 && mHasMoreData
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && lastItemIndex == mAdapter.getCount()) {
			// 加载数据代码，此处省略了
			loadMore();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		MediaHelp.getInstance().seekTo(data.getIntExtra("position", 0));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.base_ok_tv:
			mPop.showPopupWindow(v);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopVideo();
	}

	public void stopVideo() {
		if (mAdapter != null) {
			mAdapter.indexPostion = -1;
			mAdapter.isPlaying = false;
			mAdapter.notifyDataSetChanged();
			MediaHelp.release();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		MediaHelp.pause();
	}
}
