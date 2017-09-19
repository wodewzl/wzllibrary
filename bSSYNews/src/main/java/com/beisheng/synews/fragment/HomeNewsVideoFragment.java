package com.beisheng.synews.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.fragment.BaseFragment;
import com.beisheng.base.http.HttpClientUtil;
import com.beisheng.base.interfaces.UpdateCallback;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.ThreadUtil;
import com.beisheng.base.view.BSAutoSwipeRefreshLayout;
import com.beisheng.synews.activity.KeyWordActivity;
import com.beisheng.synews.adapter.HomeNewsVideoAdapter;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.interfaces.LoadMoreListener;
import com.beisheng.synews.mode.NewsVO;
import com.beisheng.synews.utils.StartViewUitl;
import com.beisheng.synews.view.BSListViewLoadMore;
import com.beisheng.synews.viewimage.animations.SliderLayout;
import com.google.gson.Gson;
import com.im.zhsy.R;

import java.util.HashMap;

public class HomeNewsVideoFragment extends BaseFragment implements OnClickListener, OnRefreshListener, LoadMoreListener, UpdateCallback, OnTouchListener, OnScrollListener, OnItemClickListener {
	private String TAG = "HomeNewsVideoFragment";
	private BaseActivity mActivity;
	protected BSAutoSwipeRefreshLayout mSwipeLayout;
	protected BSListViewLoadMore mListView;
	protected ProgressBar mProgressBar;
	private HomeNewsVideoAdapter mAdapter;
	private NewsVO mNewsVO;
	private int mState = 0; // 0为首次,1为下拉刷新 ，2为加载更多
	private View mSearchView;
	protected SliderLayout mDemoSlider;
	public String mPage = "1";// 用来存储数据的，1为默认第一页，不是只有一页
	private String mCid;
	private int lastItemIndex;// 当前ListView中最后一个Item的索引
	private boolean mLodMore = true;// 控制多次加载，即当前加载就不在会加载直到结束
	public int currentPlayPosition = 0;

	public static HomeNewsFragment newInstance() {
		HomeNewsFragment liveFragment = new HomeNewsFragment();
		return liveFragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = (BaseActivity) activity;
		// mCid = this.getArguments().getInt("id") + "";
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_news_fragment, container, false);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initViews(view);
		bindViewsListener();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// new ThreadUtil(mActivity, this).start();

	}

	private void initViews(View view) {
		mActivity.mBaseOkTv.setVisibility(View.GONE);
		mActivity.mBaseHeadLayout.setVisibility(View.VISIBLE);
		mSwipeLayout = (BSAutoSwipeRefreshLayout) view.findViewById(R.id.swipe_container);
		mActivity.setSwipeRefreshLayoutColors(mSwipeLayout);
		mListView = (BSListViewLoadMore) view.findViewById(R.id.list_view);
		View headImg = LayoutInflater.from(getActivity()).inflate(R.layout.head_img, null);
		mSearchView = (LinearLayout) headImg.findViewById(R.id.search_view);
		mSearchView.setVisibility(View.GONE);
		headImg.findViewById(R.id.img_head_layout).setVisibility(View.GONE);
		mListView.addHeaderView(headImg);
		mSearchView = (LinearLayout) headImg.findViewById(R.id.search_view);
		mAdapter = new HomeNewsVideoAdapter(mActivity);
		mListView.setAdapter(mAdapter);
		mSwipeLayout.autoRefresh();
	}

	public void bindViewsListener() {
		mListView.setLoadMoreListener(this);
		mListView.setOnScrollListener(this);
		mSwipeLayout.setOnRefreshListener(this);
		mSwipeLayout.setOnTouchListener(this);
		mSearchView.setOnClickListener(this);
		mActivity.mBaseBackTv.setOnClickListener(this);
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
	public void onStop() {

		super.onStop();
		stopVideo();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		stopVideo();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.base_back_tv:
			mActivity.openActivity(KeyWordActivity.class);
			break;

		default:
			break;
		}
	}

	public boolean getData() {
		try {
			Gson gson = new Gson();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("page", mPage);
			map.put("cid", "10");
			if (mActivity.hasNetWork()) {
				String jsonStr = HttpClientUtil.getRequest(mActivity, Constant.DOMAIN_NAME + Constant.HOME_NEWS_URL, map);
				mNewsVO = gson.fromJson(jsonStr, NewsVO.class);
				mActivity.saveJsonCache(Constant.HOME_NEWS_URL, map, jsonStr);
			} else {
				String oldStr = mActivity.getCacheFromDatabase(Constant.HOME_NEWS_URL, map);
				mNewsVO = gson.fromJson(oldStr, NewsVO.class);
			}
			if (Constant.RESULT_SUCCESS_CODE.equals(mNewsVO.getCode())) {
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void onRefresh() {
		mState = 1;
		mPage = "1";
		new ThreadUtil(mActivity, this).start();
	}

	@Override
	public void loadMore() {
		if (mLodMore) {
			mLodMore = false;
			mState = 2;
			mPage = (BaseCommonUtils.parseInt(mPage) + 1) + "";
			new ThreadUtil(mActivity, this).start();
		}
	}

	@Override
	public boolean execute() {
		return getData();
	}

	@Override
	public void executeSuccess() {
		mLodMore = true;
		mSearchView.setVisibility(View.GONE);
		mListView.setVisibility(View.VISIBLE);
		mSwipeLayout.setRefreshing(false);
		if (BaseCommonUtils.parseInt(mNewsVO.getTotal()) > BaseCommonUtils.parseInt(mNewsVO.getPage())) {
			mListView.showFooterView(true);
		} else {
			mListView.showFooterView(false);
		}
		if (1 == mState) {
			mAdapter.mList.size();
			mAdapter.updateDataFrist(mNewsVO.getList());
		} else if (2 == mState) {
			mAdapter.mList.size();
			mAdapter.updateDataLast(mNewsVO.getList());
		} else {
			mAdapter.updateData(mNewsVO.getList());
		}

	}

	@Override
	public void executeFailure() {
		mLodMore = true;
		mListView.setVisibility(View.VISIBLE);
		mSwipeLayout.setRefreshing(false);
		mListView.showFooterView(false);
		if (mNewsVO != null) {
			// mActivity.showCustomToast(mNewsVO.getRetinfo());
		} else {
			mActivity.showCustomToast("亲，请检查网络哦");
			mListView.setVisibility(View.GONE);
		}

	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		mSearchView.setVisibility(View.GONE);
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
		if (adapterView.getAdapter().getCount() == 0)
			return;
		NewsVO vo = (NewsVO) adapterView.getAdapter().getItem(position);
		if (vo == null)
			return;
		StartViewUitl.startView(mActivity, vo.getSuburl(), vo.getContentid(), vo.getLink(), vo.getGovermentid(), vo.getTypename());
	}

	public String getFragmentName() {
		return TAG;// 不知道该方法有没有用
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		stopVideo();
	}

	public void stopVideo() {
		if (mAdapter != null && mAdapter.mCurrentVideoSuperPlayer != null && mAdapter.indexPostion >= 0) {
			mAdapter.mList.get(mAdapter.indexPostion).setVideoPosition(mAdapter.mCurrentVideoSuperPlayer.getCurrentPosition());
			mAdapter.mCurrentVideoSuperPlayer.pausePlay();
			mAdapter.indexPostion = -1;
			mAdapter.isPlaying = false;
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		if (((mAdapter.indexPostion + 1) < mListView.getFirstVisiblePosition() || mAdapter.indexPostion > mListView.getLastVisiblePosition()) && mAdapter.isPlaying) {
			mAdapter.mList.get(mAdapter.indexPostion).setVideoPosition(mAdapter.mCurrentVideoSuperPlayer.getCurrentPosition());
			mAdapter.mCurrentVideoSuperPlayer.close();
			mAdapter.indexPostion = -1;
			mAdapter.isPlaying = false;
			mAdapter.notifyDataSetChanged();
		}
		lastItemIndex = arg1 + arg2 - 1 - 1;

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// 停止状态的判断条件，此处给去掉，影响体验 && mHasMoreData
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && lastItemIndex == mAdapter.getCount()) {
			// 加载数据代码，此处省略了
			if (BaseCommonUtils.parseInt(mNewsVO.getTotal()) > BaseCommonUtils.parseInt(mNewsVO.getPage())) {
				loadMore();
			}
		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);

		mActivity.mBaseOkTv.setVisibility(View.GONE);
		mActivity.mBaseHeadLayout.setVisibility(View.VISIBLE);

	}

}
