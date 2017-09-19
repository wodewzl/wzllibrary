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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.fragment.BaseFragment;
import com.beisheng.base.http.HttpClientUtil;
import com.beisheng.base.interfaces.UpdateCallback;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.ThreadUtil;
import com.beisheng.base.view.BSAutoSwipeRefreshLayout;
import com.beisheng.synews.activity.CommunityDetailActivity;
import com.beisheng.synews.activity.CommunitySerachActivity;
import com.beisheng.synews.adapter.CommunityTabFragmentAdapter;
import com.beisheng.synews.application.AppApplication;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.interfaces.LoadMoreListener;
import com.beisheng.synews.mode.CommunityVO;
import com.beisheng.synews.utils.LogUtil;
import com.beisheng.synews.view.BSListViewLoadMore;
import com.beisheng.synews.viewimage.animations.DescriptionAnimation;
import com.beisheng.synews.viewimage.animations.SliderLayout;
import com.beisheng.synews.viewimage.slidertypes.BaseSliderView;
import com.beisheng.synews.viewimage.slidertypes.BaseSliderView.OnSliderClickListener;
import com.beisheng.synews.viewimage.slidertypes.TextSliderView;
import com.google.gson.Gson;
import com.im.zhsy.R;

import java.util.HashMap;

public class CommunityTabFragment extends BaseFragment implements OnClickListener, OnRefreshListener, LoadMoreListener, UpdateCallback, OnSliderClickListener, OnItemClickListener, OnTouchListener {
	private String TAG = "CommunityTabFragment";
	private BaseActivity mActivity;

	protected BSAutoSwipeRefreshLayout mSwipeLayout;
	protected BSListViewLoadMore mListView;
	protected HashMap<String, String> url_maps;
	private CommunityTabFragmentAdapter mAdapter;
	private CommunityVO mCommunityVO;
	private int mState = 0; // 0为首次,1为下拉刷新 ，2为加载更多
	protected SliderLayout mDemoSlider;
	public String mPage = "1";// 用来存储数据的，1为默认第一页，不是只有一页
	private String mType;// 默认热帖，newForum新帖，activity活动详情
	private LinearLayout mImgHead;
	private String mName;
	private boolean mLodMore = true;// 控制多次加载，即当前加载就不在会加载直到结束
	private View mSearchView;

	public static CommunityTabFragment newInstance() {
		CommunityTabFragment liveFragment = new CommunityTabFragment();
		return liveFragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = (BaseActivity) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		mType = args.getString("id");
		mName = args.getString("name");
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

	}

	private void initViews(View view) {
		mSwipeLayout = (BSAutoSwipeRefreshLayout) view.findViewById(R.id.swipe_container);
		mActivity.setSwipeRefreshLayoutColors(mSwipeLayout);
		mListView = (BSListViewLoadMore) view.findViewById(R.id.list_view);
		mSwipeLayout.autoRefresh();
		View headImg = LayoutInflater.from(getActivity()).inflate(R.layout.head_img, null);
		mImgHead = (LinearLayout) headImg.findViewById(R.id.img_head_layout);
		mSearchView = headImg.findViewById(R.id.search_view);
		mDemoSlider = (SliderLayout) headImg.findViewById(R.id.slider);
		mDemoSlider.setmSwipeRefreshLayout(mSwipeLayout);
		mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
		mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
		mDemoSlider.setCustomAnimation(new DescriptionAnimation());
		mListView.addHeaderView(headImg);
		mAdapter = new CommunityTabFragmentAdapter(mActivity);
		mListView.setAdapter(mAdapter);
	}

	public void bindViewsListener() {
		mListView.setOnItemClickListener(this);
		mListView.setLoadMoreListener(this);
		mSwipeLayout.setOnRefreshListener(this);
		mSwipeLayout.setOnTouchListener(this);
		// mSearchView.setOnClickListener(this);
		// mActivity.mBaseBackTv.setOnClickListener(this);
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
		Bundle bundle = new Bundle();
		switch (v.getId()) {
		case R.id.base_back_tv:
			LogUtil.e("onClick", "==CommunitySerachActivity");
			mActivity.openActivity(CommunitySerachActivity.class);
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
			map.put("type", mType);
			if (AppApplication.getInstance().getUserInfoVO() != null)
				map.put("tuid", AppApplication.getInstance().getUserInfoVO().getTuid());

			String url = "";
			if ("1".equals(1)) {
				url = Constant.COMMUNITY_URL;
			} else if ("1".equals(1)) {
				url = "1";
			} else if ("1".equals(1)) {
				url = "1";
			} else if ("1".equals(1)) {
				url = "1";
			} else {
				url = "1";
			}
			if (mActivity.hasNetWork()) {
				String jsonStr = HttpClientUtil.getRequest(mActivity, Constant.DOMAIN_NAME + url, map);
				mCommunityVO = gson.fromJson(jsonStr, CommunityVO.class);
				mActivity.saveJsonCache(Constant.COMMUNITY_URL, map, jsonStr);
			} else {
				String oldStr = mActivity.getCacheFromDatabase(Constant.COMMUNITY_URL, map);
				mCommunityVO = gson.fromJson(oldStr, CommunityVO.class);
			}
			if (Constant.RESULT_SUCCESS_CODE.equals(mCommunityVO.getCode())) {
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
		mSwipeLayout.setRefreshing(false);
		mSearchView.setVisibility(View.GONE);
		mListView.setVisibility(View.VISIBLE);
		if (mCommunityVO == null)
			return;
		if (BaseCommonUtils.parseInt(mCommunityVO.getTotal()) > BaseCommonUtils.parseInt(mCommunityVO.getPage())) {
			mListView.showFooterView(true);
		} else {
			mListView.showFooterView(false);

		}
		if (1 == mState) {
			mAdapter.mList.size();
			mAdapter.updateDataFrist(mCommunityVO.getForum());
		} else if (2 == mState) {
			mAdapter.mList.size();
			mAdapter.updateDataLast(mCommunityVO.getForum());
		} else {
			mAdapter.updateData(mCommunityVO.getForum());
		}

		if ((mCommunityVO.getSlide() == null || mCommunityVO.getSlide().size() == 0)) {
			// 加载更多没有slider
			if (mState != 2)
				mImgHead.setVisibility(View.GONE);
			return;
		} else {
			mImgHead.setVisibility(View.VISIBLE);
		}

		mDemoSlider.removeAllSliders();
		// 头部幻灯片
		for (int i = 0; i < mCommunityVO.getSlide().size(); i++) {
			CommunityVO vo = mCommunityVO.getSlide().get(i);
			TextSliderView textSliderView = new TextSliderView(getActivity());
			textSliderView.setOnSliderClickListener(this);
			textSliderView.description(vo.getNote()).image(vo.getImage());
			textSliderView.getBundle().putString("tid", vo.getTid());
			mDemoSlider.addSlider(textSliderView);
		}
	}

	@Override
	public void executeFailure() {
		mLodMore = true;
		mListView.setVisibility(View.GONE);
		mListView.showFooterView(false);
		mSwipeLayout.setRefreshing(false);
		if (mCommunityVO != null) {
			if (!"".equals(mCommunityVO.getRetinfo()))
				System.out.println();
			// mActivity.showCustomToast(mCommunityVO.getRetinfo());
		} else {
			mActivity.showCustomToast("亲，请检查网络哦");
		}

	}

	@Override
	public void onSliderClick(BaseSliderView slider) {
		Bundle bundle = slider.getBundle();
		String tid = (String) bundle.get("tid");
		bundle.putString("id", tid);
		if (!"".equals(tid) && tid != null) {
			mActivity.openActivity(CommunityDetailActivity.class, bundle, 0);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
		if (adapterView.getAdapter().getCount() == 0)
			return;
		CommunityVO vo = (CommunityVO) adapterView.getAdapter().getItem(position);
		if (vo == null)
			return;
		Bundle bundle = new Bundle();
		bundle.putString("id", vo.getTid());
		bundle.putString("title", mName);

		mActivity.openActivity(CommunityDetailActivity.class, bundle, 0);
	}

	public String getFragmentName() {
		return TAG;// 不知道该方法有没有用
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent event) {
		float x1 = 0f, y1 = 0f, x2, y2;
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// 当手指按下的时候
			x1 = event.getX();
			y1 = event.getY();
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			// 当手指离开的时候
			x2 = event.getX();
			y2 = event.getY();
			if (y2 - y1 > 450) {
				// mSearchView.setVisibility(View.VISIBLE);
			}
		}
		return false;
	}

}
