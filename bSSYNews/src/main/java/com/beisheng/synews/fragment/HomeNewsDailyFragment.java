package com.beisheng.synews.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckedTextView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.ViewFlipper;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.fragment.BaseFragment;
import com.beisheng.base.http.HttpClientUtil;
import com.beisheng.base.interfaces.UpdateCallback;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.CacheUtil;
import com.beisheng.base.utils.ThreadUtil;
import com.beisheng.base.utils.WebViewPictureUtil;
import com.beisheng.base.utils.WebViewPictureUtil.JSCallBack;
import com.beisheng.base.utils.WebviewUtil;
import com.beisheng.base.view.BSAutoSwipeRefreshLayout;
import com.beisheng.base.view.BSWebView;
import com.beisheng.synews.activity.LiveAddActivity;
import com.beisheng.synews.adapter.DateAdapter;
import com.beisheng.synews.adapter.DateAdapter.DateCallback;
import com.beisheng.synews.adapter.HomeNewsDialyAdapter;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.mode.DailyVO;
import com.beisheng.synews.mode.LiveVO;
import com.beisheng.synews.mode.NewsVO;
import com.beisheng.synews.utils.StartViewUitl;
import com.beisheng.synews.view.PinnedSectionListView;
import com.google.gson.Gson;
import com.im.zhsy.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class HomeNewsDailyFragment extends BaseFragment implements OnClickListener, UpdateCallback, OnItemClickListener, OnRefreshListener {
	private String TAG = "HomeNewsDailyFragment";
	private BaseActivity mActivity;
	private NewsVO mNewsVO;
	private HomeNewsDialyAdapter mAdapter;
	private SamplePagerAdapter mPagerAdapter;
	protected PinnedSectionListView mListView;
	private String mPage = "1";
	private int mState = 0; // 0为首次,1为下拉刷新 ，2为加载更多
	private ViewPager mViewPager;
	private String mCid;

	private CheckedTextView mChTextView01, mChTextView02, mChTextView03;
	private ImageView mImg01, mImg02, mImg03;
	public DatePopwindow mDatePopwindow;
	private View mDeviderView;
	private DateAdapter mDateAdapter;
	public BSAutoSwipeRefreshLayout mSwipeLayout;
	public BSAutoSwipeRefreshLayout mSwipeLayoutWeb;
	private String mType;

	public static HomeNewsDailyFragment newInstance() {
		HomeNewsDailyFragment liveFragment = new HomeNewsDailyFragment();
		return liveFragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = (BaseActivity) activity;
		mCid = this.getArguments().getInt("id") + "";
		mType = this.getArguments().getString("type");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_news_daily_fragment, container, false);
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
		new ThreadUtil(mActivity, this).start();
	}

	private void initViews(View view) {
		mActivity.mBaseOkTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.channel_glide, 0);
		mSwipeLayout = (BSAutoSwipeRefreshLayout) view.findViewById(R.id.swipe_container_list);
		mActivity.setSwipeRefreshLayoutColors(mSwipeLayout);
		mSwipeLayoutWeb = (BSAutoSwipeRefreshLayout) view.findViewById(R.id.swipe_container_web);
		mActivity.setSwipeRefreshLayoutColors(mSwipeLayoutWeb);
		mSwipeLayoutWeb.autoRefresh();
		mSwipeLayoutWeb.setEnabled(false);
		mChTextView01 = (CheckedTextView) view.findViewById(R.id.check_tv_01);
		mChTextView02 = (CheckedTextView) view.findViewById(R.id.check_tv_02);
		mChTextView03 = (CheckedTextView) view.findViewById(R.id.check_tv_03);
		mImg01 = (ImageView) view.findViewById(R.id.img_01);
		mImg02 = (ImageView) view.findViewById(R.id.img_02);
		mImg03 = (ImageView) view.findViewById(R.id.img_03);
		mListView = (PinnedSectionListView) view.findViewById(R.id.list_view);
		mAdapter = new HomeNewsDialyAdapter(mActivity);
		mListView.setAdapter(mAdapter);
		mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
		mPagerAdapter = new SamplePagerAdapter(mActivity);
		mViewPager.setOffscreenPageLimit(0);
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setCurrentItem(0);
		mDeviderView = view.findViewById(R.id.devider_view);
		mDatePopwindow = new DatePopwindow(mActivity);
	}

	public void bindViewsListener() {
		mActivity.mBaseOkTv.setOnClickListener(this);
		mListView.setOnItemClickListener(this);
		mSwipeLayout.setOnRefreshListener(this);
		mSwipeLayoutWeb.setOnRefreshListener(this);
		mChTextView01.setOnClickListener(this);
		mChTextView03.setOnClickListener(this);
		mChTextView02.setOnClickListener(this);
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
			Bundle bundle = new Bundle();
			bundle.putString("title", "直播现场");
			mActivity.openActivity(LiveAddActivity.class, bundle, 0);
			break;

		case R.id.check_tv_01:
			setCheckText(true, false, false);
			mSwipeLayoutWeb.setVisibility(View.VISIBLE);
			mSwipeLayout.setVisibility(View.GONE);
			break;
		case R.id.check_tv_02:
			setCheckText(false, true, false);
			mSwipeLayoutWeb.setVisibility(View.GONE);
			mSwipeLayout.setVisibility(View.VISIBLE);
			break;
		case R.id.check_tv_03:
			// mSwipeLayoutWeb.setVisibility(View.GONE);
			// mSwipeLayout.setVisibility(View.VISIBLE);
			setCheckText(false, false, true);
			mDatePopwindow.showPopupWindow(mDeviderView);
			break;

		default:
			break;
		}
	}

	public void setCheckText(boolean a, boolean b, boolean c) {
		mChTextView01.setChecked(a);
		mChTextView02.setChecked(b);
		mChTextView03.setChecked(c);

		if (a) {
			mImg01.setVisibility(View.VISIBLE);
		} else {
			mImg01.setVisibility(View.GONE);
		}

		if (b) {
			mImg02.setVisibility(View.VISIBLE);
		} else {
			mImg02.setVisibility(View.GONE);
		}

		if (c) {
			mImg03.setVisibility(View.VISIBLE);
		} else {
			mImg03.setVisibility(View.GONE);
		}

	}

	public boolean getData() {
		try {
			Gson gson = new Gson();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("page", mPage);
			map.put("cid", mCid);
			if (mDateAdapter.getSelectDate() != null)
				map.put("time", mDateAdapter.getSelectDate().replace("-", ""));
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
	public boolean execute() {
		return getData();
	}

	@Override
	public void executeSuccess() {
		mActivity.dismissProgressDialog();
		mListView.showFooterView(false);
		mSwipeLayout.setRefreshing(false);
		mSwipeLayoutWeb.setRefreshing(false);
		mPagerAdapter.updateData(mNewsVO.getList());
		mAdapter.updateData(mNewsVO.getPlate());
	}

	@Override
	public void executeFailure() {
		mActivity.dismissProgressDialog();
		mSwipeLayout.setRefreshing(false);
		mListView.showFooterView(false);
		mSwipeLayoutWeb.setRefreshing(false);
		if (mNewsVO != null)
			mActivity.showCustomToast(mNewsVO.getRetinfo());
		else
			mActivity.showCustomToast("亲，请检查网络哦");
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
		if (adapterView.getAdapter().getCount() == 0)
			return;
		NewsVO vo = (NewsVO) adapterView.getAdapter().getItem(position);

		if (vo == null || vo.getLink() == null || vo.getLinkv1() == null)
			return;

		// Bundle bundle = new Bundle();
		// LiveVO livevo = new LiveVO();
		// livevo.setSuburl("7");
		// livevo.setLid(vo.getContentid());
		// livevo.setTitle(vo.getTitle());
		// livevo.setLink(vo.getLink());
		// livevo.setComments(vo.getComments());
		// livevo.setShare_tit(vo.getTitle());
		// livevo.setShare_url(vo.getLink());
		// bundle.putString("url", vo.getLink());
		// bundle.putSerializable("livevo", livevo);
		// if ("7".equals(mCid)) {
		// bundle.putString("name", "十堰日报");
		// } else {
		// bundle.putString("name", "十堰晚报");
		// }
		// ((BaseActivity) mActivity).openActivity(WebViewActivity.class,
		// bundle, 0);
		StartViewUitl.startView(mActivity, "7", "", vo.getLinkv1(), "", "");

	}

	class SamplePagerAdapter extends PagerAdapter {
		private Context mContext;
		private List<NewsVO> mList;

		public SamplePagerAdapter(Context context) {
			this.mContext = context;
			mList = new ArrayList<NewsVO>();
		}

		@Override
		public int getCount() {
			return mList.size();

		}

		public void updateData(List<NewsVO> list) {
			this.mList = list;
			this.notifyDataSetChanged();
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@SuppressLint("NewApi")
		@Override
		public View instantiateItem(ViewGroup container, final int position) {
			WebView webView = new BSWebView(mContext);
			final ProgressBar progressbar = new ProgressBar(mContext, null, android.R.attr.progressBarStyleHorizontal);
			progressbar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, BaseCommonUtils.dip2px(mContext, 3)));
			Drawable drawable = mContext.getResources().getDrawable(R.drawable.webview_progress);
			progressbar.setProgressDrawable(drawable);
			webView.addView(progressbar);
			WebviewUtil.SetWebview(webView);
			webView.loadUrl(mList.get(position).getLink());
			JSCallBack jsCallBack = new JSCallBack() {
				@Override
				public void jsCallBack(String str) {
					String[] imgUrls = str.split(",");
					Bundle bundle = new Bundle();
					LiveVO vo = new LiveVO();
					vo.setSuburl("7");
					vo.setLid(imgUrls[1]);
					vo.setTitle(imgUrls[2]);
					vo.setLink(imgUrls[0]);
					vo.setComments(imgUrls[3]);
					vo.setShare_tit(imgUrls[2]);
					vo.setShare_url(imgUrls[0]);
					bundle.putString("url", imgUrls[0]);
					bundle.putSerializable("livevo", vo);
					if ("7".equals(mCid)) {
						bundle.putString("name", "十堰日报");
					} else {
						bundle.putString("name", "十堰晚报");
					}
					// ((BaseActivity)
					// mActivity).openActivity(WebViewActivity.class, bundle,
					// 0);
					StartViewUitl.startView(mActivity, "7", "", imgUrls[4], "", "");
				}

				@Override
				public void jsCallBack(String str, String status) {

				}
			};
			WebViewPictureUtil wp = new WebViewPictureUtil(mActivity, webView, jsCallBack);
			webView.setWebViewClient(wp);

			webView.setWebChromeClient(new WebChromeClient() {
				@Override
				public void onProgressChanged(WebView view, int newProgress) {
					if (newProgress == 100) {
						progressbar.setVisibility(View.INVISIBLE);
					} else {
						if (View.INVISIBLE == progressbar.getVisibility()) {
							progressbar.setVisibility(View.VISIBLE);
						}
						progressbar.setProgress(newProgress);
					}
					super.onProgressChanged(view, newProgress);
				}

			});
			container.addView(webView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			return webView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

	DateCallback mDateCallback = new DateCallback() {

		@Override
		public void dateCallback() {
			mActivity.showProgressDialog();
			mSwipeLayout.autoRefresh();
			mDatePopwindow.dismiss();
			new ThreadUtil(mActivity, HomeNewsDailyFragment.this).start();
		}
	};

	// , OnGestureListener
	public class DatePopwindow extends PopupWindow implements OnClickListener {
		private Context mContext;
		// private GestureDetector mGestureDetector = null;
		private ViewFlipper mFlipper = null;
		private GridView mGridView = null;
		private Date mCurrentDate = new Date(System.currentTimeMillis());
		private View mViewBg;

		public void showPopupWindow(View parent) {
			if (!this.isShowing()) {
				this.showAsDropDown(parent, 0, 0);
			} else {
				this.dismiss();
			}
		}

		public DatePopwindow(Context context) {
			this.mContext = context;
			initView();
		}

		public void initView() {
			View view = View.inflate(mContext, R.layout.pop_date, null);
			// mGestureDetector = new GestureDetector(this);
			mFlipper = (ViewFlipper) view.findViewById(R.id.flipper1);
			addGridView();
			// mDateAdapter = new DateAdapter(mContext, mCurrentDate,
			// mDateCallback);

			// 获取缓存的日期
			Gson gson = new Gson();
			HashMap<String, String> map = new HashMap<String, String>();
			String ribao = CacheUtil.getCacheFromDatabase(mActivity, Constant.RIBAO_URL, map);
			DailyVO vo = gson.fromJson(ribao, DailyVO.class);
			String[] dateArray;
			if ("ribao".equals(mType)) {
				dateArray = vo.getTime1();
			} else {
				dateArray = vo.getTime2();
			}

			// mDateAdapter = new DateAdapter(mContext, mCurrentDate,
			// mDateCallback);
			mDateAdapter = new DateAdapter(mContext, dateArray, mDateCallback);
			mGridView.setAdapter(mDateAdapter);
			mFlipper.addView(mGridView, 0);
			this.mFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_left_in));
			this.mFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_left_out));
			mViewBg = view.findViewById(R.id.view_bg);
			mViewBg.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					dismiss();
					return false;
				}
			});

			this.setWidth(LayoutParams.MATCH_PARENT);
			this.setHeight(LayoutParams.MATCH_PARENT);
			this.setFocusable(true);
			this.setOutsideTouchable(true);
			ColorDrawable drawable = new ColorDrawable(Color.parseColor("#40000000"));
			setBackgroundDrawable(drawable);
			setFocusable(true);
			this.setContentView(view);
			this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
			this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.commit_tv:

				break;
			default:
				break;
			}
		}

		@SuppressLint({ "NewApi", "ResourceAsColor" })
		private void addGridView() {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			mGridView = new GridView(mActivity);
			mGridView.setNumColumns(2);
			mGridView.setGravity(Gravity.CENTER);
			mGridView.setVerticalSpacing(BaseCommonUtils.dip2px(mContext, 5));
			mGridView.setHorizontalSpacing(0);
			mGridView.setVerticalSpacing(0);
			mGridView.setLayoutParams(params);
			// mGridView.setOnTouchListener(new OnTouchListener() {
			// @Override
			// public boolean onTouch(View v, MotionEvent event) {
			// return DatePopwindow.this.mGestureDetector.onTouchEvent(event);
			// }
			// });

		}
		// @Override
		// public boolean onDown(MotionEvent arg0) {
		// return false;
		// }

		// @Override
		// public boolean onFling(MotionEvent e1, MotionEvent e2, float
		// velocityX, float velocityY)
		// {
		// int index = -1;// 跨月下标
		// int gvFlag = 0;
		// if (e1 == null || e2 == null)
		// return false;
		//
		// if (e1.getX() - e2.getX() > 80) {
		// if
		// (DateUtils.ConverToString(mCurrentDate).equals(DateUtils.ConverToString(new
		// Date(System.currentTimeMillis()))))
		// return false;
		// // 向左滑
		// addGridView();
		// mCurrentDate = DateUtils.getDateAfter(mCurrentDate, 5);
		// mDateAdapter = new DateAdapter(mContext, mCurrentDate,
		// mDateCallback);
		// mGridView.setAdapter(mDateAdapter);
		// gvFlag++;
		// mFlipper.addView(mGridView, gvFlag);
		// this.mFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext,
		// R.anim.push_left_in));
		// this.mFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext,
		// R.anim.push_left_out));
		// this.mFlipper.showNext();
		// mFlipper.removeViewAt(0);
		// return true;
		//
		// } else if (e1.getX() - e2.getX() < -80) {
		// addGridView();
		// mCurrentDate = DateUtils.getDateBefore(mCurrentDate, 5);
		// mDateAdapter = new DateAdapter(mContext, mCurrentDate,
		// mDateCallback);
		// mGridView.setAdapter(mDateAdapter);
		// gvFlag++;
		// mFlipper.addView(mGridView, gvFlag);
		// this.mFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext,
		// R.anim.push_right_in));
		// this.mFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext,
		// R.anim.push_right_out));
		// this.mFlipper.showPrevious();
		// mFlipper.removeViewAt(0);
		// return true;
		// }
		// return false;
		// }

		// @Override
		// public void onLongPress(MotionEvent arg0) {
		//
		// }
		//
		// @Override
		// public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float
		// arg2, float arg3) {
		// return false;
		// }
		//
		// @Override
		// public void onShowPress(MotionEvent arg0) {
		//
		// }
		//
		// @Override
		// public boolean onSingleTapUp(MotionEvent arg0) {
		// return false;
		// }
	}

	public String getFragmentName() {
		return TAG;// 不知道该方法有没有用
	}

	@Override
	public void onRefresh() {
		mState = 1;
		mPage = "1";
		new ThreadUtil(mActivity, this).start();
	}

}
