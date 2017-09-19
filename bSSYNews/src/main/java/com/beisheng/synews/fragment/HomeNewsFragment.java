package com.beisheng.synews.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
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
import android.widget.TextView;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.fragment.BaseFragment;
import com.beisheng.base.http.HttpClientUtil;
import com.beisheng.base.interfaces.UpdateCallback;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.JsonUtil;
import com.beisheng.base.utils.SharePreferenceUtil;
import com.beisheng.base.utils.ThreadUtil;
import com.beisheng.base.view.BSAutoSwipeRefreshLayout;
import com.beisheng.synews.activity.KeyWordActivity;
import com.beisheng.synews.activity.LocalSwitchActivity;
import com.beisheng.synews.adapter.HomeNewsFragmentAdapter;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.interfaces.LoadMoreListener;
import com.beisheng.synews.mode.ChannelItem;
import com.beisheng.synews.mode.ChannelManage;
import com.beisheng.synews.mode.LocalVO;
import com.beisheng.synews.mode.NewsVO;
import com.beisheng.synews.utils.StartViewUitl;
import com.beisheng.synews.videoplay.MediaHelp;
import com.beisheng.synews.view.BSListViewLoadMore;
import com.beisheng.synews.viewimage.animations.DescriptionAnimation;
import com.beisheng.synews.viewimage.animations.SliderLayout;
import com.beisheng.synews.viewimage.slidertypes.BaseSliderView;
import com.beisheng.synews.viewimage.slidertypes.BaseSliderView.OnSliderClickListener;
import com.beisheng.synews.viewimage.slidertypes.TextSliderView;
import com.bs.bsims.observer.BSApplictionObserver.Watcher;
import com.google.gson.Gson;
import com.im.zhsy.R;

import java.util.HashMap;
import java.util.List;

@SuppressLint("NewApi")
public class HomeNewsFragment extends BaseFragment implements OnClickListener, OnRefreshListener, LoadMoreListener, UpdateCallback, OnTouchListener, OnSliderClickListener, OnItemClickListener,
		OnScrollListener, Watcher {
	private String TAG = "HomeNewsFragment";
	private BaseActivity mActivity;
	protected BSAutoSwipeRefreshLayout mSwipeLayout;
	protected BSListViewLoadMore mListView;
	protected ProgressBar mProgressBar;
	protected HashMap<String, String> url_maps;
	private HomeNewsFragmentAdapter mAdapter;
	private NewsVO mNewsVO;
	private int mState = 0; // 0为首次,1为下拉刷新 ，2为加载更多
	private View mSearchView;
	protected SliderLayout mDemoSlider;
	public String mPage = "1";// 用来存储数据的，1为默认第一页，不是只有一页
	private int mCid;// 代表新闻类型，
	private LinearLayout mImgHead;
	// private LocalVO mLocalVO;
	private String mId = "";// 位置id
	private String mLocalName = "默认";
	private TextView mLocalTv;
	private String mType;
	private int lastItemIndex;// 当前ListView中最后一个Item的索引
	private boolean mLodMore = true;// 控制多次加载，即当前加载就不在会加载直到结束
	private View headImg;
	private String mCurrentJsonStr = "";
	private String address;

	public static HomeNewsFragment newInstance() {
		HomeNewsFragment liveFragment = new HomeNewsFragment();
		return liveFragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = (BaseActivity) activity;
		Bundle args = this.getArguments();
		mCid = args.getInt("id", 0);
		mType = args.getString("type");

		if (mCid == 5) {

			// 去掉定位
			// Gson gson = new Gson();
			// HashMap<String, String> localMap = new HashMap<String, String>();
			// String oldStr =
			// mActivity.getCacheFromDatabase(Constant.LOCATION_URL, localMap);
			// LocalVO localVO = gson.fromJson(oldStr, LocalVO.class);
			// if (localVO != null &&
			// Constant.RESULT_SUCCESS_CODE.equals(localVO.getCode()) &&
			// localVO.getList() != null) {
			// String localName =
			// SharePreferenceUtil.getSharedpreferenceValue(mActivity,
			// " shiyan_address", "address");
			// for (int i = 0; i < localVO.getList().size(); i++) {
			// if (localName != null &&
			// localName.contains(localVO.getList().get(i).getName())) {
			// mLocalName = localVO.getList().get(i).getName();
			// return;
			// }
			// }
			// }
			String logcalName = SharePreferenceUtil.getSharedpreferenceValue(mActivity, "local_name", "local_name");
			if (!"18".equals(logcalName)) {
				mLocalName = logcalName;
			} else {
				mLocalName = "全部";
			}

		}
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
		mSwipeLayout = (BSAutoSwipeRefreshLayout) view.findViewById(R.id.swipe_container);
		mActivity.setSwipeRefreshLayoutColors(mSwipeLayout);
		mListView = (BSListViewLoadMore) view.findViewById(R.id.list_view);
		headImg = LayoutInflater.from(getActivity()).inflate(R.layout.head_img, null);

		mImgHead = (LinearLayout) headImg.findViewById(R.id.img_head_layout);
		mImgHead.setVisibility(View.VISIBLE);
		mSearchView = (LinearLayout) headImg.findViewById(R.id.search_view);
		LinearLayout localLayout = (LinearLayout) headImg.findViewById(R.id.local_layout);// 县市多个定位布局
		if ("city".equals(mType)) {
			localLayout.setVisibility(View.VISIBLE);
			mLocalTv = (TextView) headImg.findViewById(R.id.local_tv);
			mLocalTv.setBackground(BaseCommonUtils.setBackgroundShap(mActivity, 5, R.color.sy_title_color, R.color.sy_title_color));
			mLocalTv.setText(mLocalName);
			TextView viewTv = (TextView) headImg.findViewById(R.id.view_other_tv);
			viewTv.setOnClickListener(this);
		} else {
			localLayout.setVisibility(View.GONE);
		}
		mDemoSlider = (SliderLayout) headImg.findViewById(R.id.slider);
		mDemoSlider.setmSwipeRefreshLayout(mSwipeLayout);
		mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
		mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
		mDemoSlider.setCustomAnimation(new DescriptionAnimation());
		mListView.addHeaderView(headImg);
		mAdapter = new HomeNewsFragmentAdapter(mActivity);
		// AnimationAdapter animationAdapter = new
		// CardsAnimationAdapter(mAdapter);
		// animationAdapter.setAbsListView(mListView);
		mListView.setAdapter(mAdapter);
		mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);

		mCurrentJsonStr = "";
		// 为了首页更快加载数据的奇葩要求，这里第一数据在logo里加载缓存了起来，只是第一页做了缓存
		Gson gson = new Gson();
		HashMap<String, String> map = new HashMap<String, String>();
		List<ChannelItem.ListvBean.ListBeanX> list = ChannelManage.getManage().getUserChannel();
		map.put("page", mPage);
		map.put("cid", list.get(0).getCid());
		map.put("id", mId);
		String oldStr = mActivity.getCacheFromDatabase(Constant.HOME_NEWS_URL, map);
		mNewsVO = gson.fromJson(oldStr, NewsVO.class);
		if (list.get(0).getCid().equals(mCid + "") && mNewsVO != null && Constant.RESULT_SUCCESS_CODE.equals(mNewsVO.getCode())) {
			executeSuccess();
		} else {
			mSwipeLayout.autoRefresh();
		}

	}

	public void bindViewsListener() {
		mListView.setOnItemClickListener(this);
		mListView.setLoadMoreListener(this);
		mListView.setOnScrollListener(this);
		mSwipeLayout.setOnRefreshListener(this);
		mSwipeLayout.setOnTouchListener(this);
		mSearchView.setOnClickListener(this);
		// MessageMyHeadImp.getInstance().add(this);
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
		case R.id.search_view:
			mActivity.openActivity(KeyWordActivity.class);
			break;

		case R.id.view_other_tv:
			mActivity.openActivity(LocalSwitchActivity.class, bundle, 2);
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
			map.put("cid", mCid + "");
			map.put("id", mId);
			String jsonStr = "";
			if (mActivity.hasNetWork()) {
				jsonStr = HttpClientUtil.getRequest(mActivity, Constant.DOMAIN_NAME + Constant.HOME_NEWS_URL, map);
				mNewsVO = gson.fromJson(jsonStr, NewsVO.class);
			} else {
				String oldStr = mActivity.getCacheFromDatabase(Constant.HOME_NEWS_URL, map);
			}

			if (Constant.RESULT_SUCCESS_CODE.equals(mNewsVO.getCode())) {
				mActivity.saveJsonCache(Constant.HOME_NEWS_URL, map, jsonStr);
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
		mSwipeLayout.postDelayed(new Runnable() {
			@Override
			public void run() {
				mSwipeLayout.setRefreshing(false);
				mSearchView.setVisibility(View.GONE);
			}
		}, 1000);
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

		if (mCurrentJsonStr.equals(JsonUtil.toJson(mNewsVO))) {
			return;
		}
		mCurrentJsonStr = JsonUtil.toJson(mNewsVO);

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

		if ((mNewsVO.getSlide() == null || mNewsVO.getSlide().size() == 0)) {
			// 加载更多没有slider
			if (mState != 2)
				mImgHead.setVisibility(View.GONE);
			return;
		}

		// if (mDemoSlider.getSiderCount() > 0)
		// return;

		mDemoSlider.removeAllSliders();
		// 头部幻灯片
		for (int i = 0; i < mNewsVO.getSlide().size(); i++) {
			NewsVO vo = mNewsVO.getSlide().get(i);
			TextSliderView textSliderView = new TextSliderView(getActivity());
			textSliderView.setOnSliderClickListener(this);
			textSliderView.description(vo.getTitle()).image(vo.getThumb());
			textSliderView.getBundle().putString("link", vo.getLink());
			textSliderView.getBundle().putString("contentid", vo.getContentid());
			textSliderView.getBundle().putString("suburl", vo.getSuburl());
			textSliderView.getBundle().putString("title", vo.getTitle());
			mDemoSlider.addSlider(textSliderView);
		}
		mDemoSlider.stopAutoCycle();
		if ("1".equals(mNewsVO.getSlider_is())) {
			mDemoSlider.startAutoCycle(3000, Long.parseLong(mNewsVO.getSlider_time()) * 1000, true);
		} else {
			mDemoSlider.stopAutoCycle();
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
			// mListView.setVisibility(View.GONE);
		}
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

	@Override
	public void onSliderClick(BaseSliderView slider) {
		Bundle bundle = slider.getBundle();
		String link = (String) bundle.get("link");
		String avdName = (String) bundle.get("title");
		String contentid = bundle.getString("contentid");
		String suburl = bundle.getString("suburl");
		// StartViewUitl.startView(mActivity, suburl, contentid, link, null,
		// null);
		StartViewUitl.startView(mActivity, suburl, contentid, link, null, null, avdName);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
		if (adapterView.getAdapter().getCount() == 0)
			return;
		NewsVO vo = (NewsVO) adapterView.getAdapter().getItem(position);
		if (vo == null)
			return;

		if ("1".equals(vo.getAdv())) {
			StartViewUitl.startView(mActivity, vo.getSuburl(), vo.getContentid(), vo.getLink(), vo.getGovermentid(), vo.getTypename(), vo.getAdv_type());

		} else {
			if ("3".equals(vo.getSuburl())) {
				StartViewUitl.startView(mActivity, vo.getSuburl(), vo.getContentid(), vo.getLink(), vo.getGovermentid(), vo.getTypename(), "热点专题");

			} else {
				StartViewUitl.startView(mActivity, vo.getSuburl(), vo.getContentid(), vo.getLink(), vo.getGovermentid(), vo.getTypename(), vo.getTitle());

			}

		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null)
			return;
		switch (resultCode) {
		case 2:
			mId = data.getStringExtra("id");
			String local = data.getStringExtra("name");
			if (mLocalTv != null && local != null) {
				mLocalTv.setText(local);
			}
			if (mSwipeLayout != null)
				mSwipeLayout.autoRefresh();
			new ThreadUtil(mActivity, this).start();
			break;

		default:
			break;
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
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && mAdapter != null && lastItemIndex == mAdapter.getCount()) {
			// 加载数据代码，此处省略了
			if (mNewsVO != null && BaseCommonUtils.parseInt(mNewsVO.getTotal()) > BaseCommonUtils.parseInt(mNewsVO.getPage())) {
				loadMore();
			}
		}
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
		if (mAdapter != null) {
			mAdapter.indexPostion = -1;
			mAdapter.isPlaying = false;
			mAdapter.notifyDataSetChanged();
			MediaHelp.release();
		}
	}

	@Override
	public void updateNotify(Object content) {
		if (content == null)
			return;
		LocalVO vo = (LocalVO) content;
		mId = vo.getId();
		String local = vo.getName();
		mLocalTv.setText(local);
		mSwipeLayout.autoRefresh();
		new ThreadUtil(mActivity, this).start();
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
