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
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.fragment.BaseFragment;
import com.beisheng.base.http.HttpClientUtil;
import com.beisheng.base.interfaces.UpdateCallback;
import com.beisheng.base.service.DownloadService;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.ThreadUtil;
import com.beisheng.base.utils.VersionUtils;
import com.beisheng.base.view.BSDialog;
import com.bs.bsims.observer.BSHtmlObserver.Watcher;
import com.bs.bsims.observer.MessageHtml;
import com.beisheng.synews.activity.ChannelActivity;
import com.beisheng.synews.activity.HomeActivity;
import com.beisheng.synews.activity.KeyWordActivity;
import com.beisheng.synews.activity.LiveAddThemeActivity;
import com.beisheng.synews.adapter.HomeNewsViewPagerAdapter;
import com.beisheng.synews.application.AppApplication;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.mode.ChannelItem;
import com.beisheng.synews.mode.ChannelManage;
import com.beisheng.synews.mode.UpdateVersionVO;
import com.beisheng.synews.utils.LogUtil;
import com.beisheng.synews.view.ColumnHorizontalScrollView;
import com.google.gson.Gson;
import com.im.zhsy.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * 改动说明: 布局文件框架 改为帧布局嵌套 为了webview覆盖功能
 * 
 * 
 * 在标题栏点击切换监听内加入 根据服务器返回 封装后的数据islink存在并且为“1”时 webview才显示
 * 并且将linkurl加载出来，否则销毁webview 并gone掉 islink =
 * mUserChannelList.get(i).getIslink(); if (localView != v) {
 * localView.setSelected(false); if(islink!=null){ if (islink.equals("1")) {
 * webView.setVisibility(View.VISIBLE);
 * load(mUserChannelList.get(i).getLinkurl()); } else { destroyWebView(webView);
 * webView.setVisibility(View.GONE);
 * 
 * } } 在onActivityResult中也要做如上操作 但是取的是ChannleActivity中initent传递过来的islink
 * 及linkurl
 * 
 * 在executeSuccess 中改动首先判断了封装的数据中 与服务器返回的长度是否一致 否则重新封装一遍
 * 
 * ChannelManage类里封装的原有mode(ChannelItem)修改为配合服务器返回数据 新增的mode(ChannelItem1) ，并且
 * 内部方法新增getCityChannel(); saveCityChannel(); ( 城市 ) getBranchChannel();
 * saveBranchChannel(); ( 部门 ) ChannleActivity1中增加
 * getUserChangeBranchAdd(channel) 方法内部 作用： 在ChannelActivity创建时取存在本地的栏目列表json
 * 再次封装 在此方法内部调用本地分类存的列表与原始json做比较 这个判断为了从我的频道中点击删除时 移动及删除到指定目录
 * 
 * 
 * 加入eventbus 修改标题栏 在itemOnclick里面处理 发送消息 在HomeActivity中接收
 * 
 * 
 * 
 * */
public class HomeFragment extends BaseFragment implements OnClickListener, OnPageChangeListener, UpdateCallback, Watcher {
	private String TAG = "HomeFragment";
	public static final int CHANNEL_CODE = 1;// 点击更多栏目返回码
	public static final int SWITCH_LOCAL = 2;// 切换地方
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
	private ChannelItem mChannelItem;
	private ArrayList<ChannelItem.ListvBean.ListBeanX> mOtherChannelList = new ArrayList<ChannelItem.ListvBean.ListBeanX>();// 其它栏目
	private ArrayList<ChannelItem.ListvBean.ListBeanX> mUserChannelList = new ArrayList<ChannelItem.ListvBean.ListBeanX>();// 用户栏目
	private ArrayList<ChannelItem.ListvBean.ListBeanX> mCityChannelList = new ArrayList<ChannelItem.ListvBean.ListBeanX>();// 城市栏目
	private ArrayList<ChannelItem.ListvBean.ListBeanX> mBranchChannelList = new ArrayList<ChannelItem.ListvBean.ListBeanX>();// 部门栏目
	public String mPage = "1";// 用来存储数据的，1为默认第一页，不是只有一页
	private HomeNewsFragment mLocalFragment;
	private UpdateVersionVO mUpdateVersionVO;
	private BSDialog mDialog;
	private LiveFragment mLiveFragment;
	private HomeNewsViewPagerAdapter mFragemtAdapter;
	// private WebView webView;
	private String share_title = "测试";
	private String share_image = "测试";
	private String share_description = "测试";
	private String share_url = "测试";
	private String Linkurl;// 用来判断webview 返回还是关闭
	private LinearLayout mChanelLyaout;
	private HomeNewsWebFragment mWebFragment;
	private HomeNewsDailyFragment mDailyFragment;
	private String htmlLink;
	private String htmlTitle;

	public static HomeFragment newInstance() {
		HomeFragment homeFragment = new HomeFragment();
		return homeFragment;
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
		View view = inflater.inflate(R.layout.home_fragment, container, false);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initViews(view);
		checkUpdate();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void initViews(View view) {
		mActivity.mBaseOkTv.setVisibility(View.GONE);
		mActivity.mBaseHeadLayout.setVisibility(View.VISIBLE);
		mChanelLyaout = (LinearLayout) view.findViewById(R.id.channel_layout);
		mUserChannelList = (ArrayList<ChannelItem.ListvBean.ListBeanX>) ChannelManage.getManage().getUserChannel();
		mOtherChannelList = (ArrayList<ChannelItem.ListvBean.ListBeanX>) ChannelManage.getManage().getOtherChannel();
		mCityChannelList = (ArrayList<ChannelItem.ListvBean.ListBeanX>) ChannelManage.getManage().getCityChannel();
		mBranchChannelList = (ArrayList<ChannelItem.ListvBean.ListBeanX>) ChannelManage.getManage().getBranchChannel();
		mViewPager = (ViewPager) view.findViewById(R.id.view_pager);

		mFragemtAdapter = new HomeNewsViewPagerAdapter(mActivity.getSupportFragmentManager());
		mViewPager.setAdapter(mFragemtAdapter);

		mColumnHorizontalScrollView = (ColumnHorizontalScrollView) view.findViewById(R.id.top_scrollview);
		mScrollViewItemContent = (LinearLayout) view.findViewById(R.id.scrollview_item_content);
		mShadeLeftImg = (ImageView) view.findViewById(R.id.shade_left);
		mShadeRightImg = (ImageView) view.findViewById(R.id.shade_right);
		mMoreImg = (LinearLayout) view.findViewById(R.id.more_columns_layout);
		mMoreClumnsLayout = (LinearLayout) view.findViewById(R.id.more_columns_layout);
		mAllcolumnLayout = (RelativeLayout) view.findViewById(R.id.all_column_layout);
		mScreenWidth = BaseCommonUtils.getScreenWidth(mActivity);
		// mItemWidth = mScreenWidth / 9;// 一个Item宽度为屏幕的1/9
		mItemWidth = BaseCommonUtils.dip2px(mActivity, 40.3f);
		mFragments = new ArrayList<Fragment>();
		// initColumnData();
		bindViewsListener();
		initLocalData();

	}

	public void initLocalData() {
		// 为了首页更快加载数据的奇葩要求，这里第一数据在logo里加载缓存了起来，只是第一页做了缓存
		Gson gson = new Gson();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("uid", AppApplication.getInstance().getUid());
		String oldStr = mActivity.getCacheFromDatabase(Constant.CHANNEL_CATEGORY_ULR, map);
		mChannelItem = gson.fromJson(oldStr, ChannelItem.class);
		LogUtil.e("initLocalData+mChannelItem", oldStr);
		if (mChannelItem != null && Constant.RESULT_SUCCESS_CODE.equals(mChannelItem.getCode())) {
			executeSuccess();
		} else {
			new ThreadUtil(mActivity, this).start();
		}
	}

	public void bindViewsListener() {
		mViewPager.setOnPageChangeListener(this);
		mMoreImg.setOnClickListener(this);
		mActivity.mBaseOkTv.setOnClickListener(this);
		mActivity.mBaseBackTv.setOnClickListener(this);
		MessageHtml.getInstance().add(this);
	}

	/** 获取Column栏目 数据 */
	private void initColumnData() {
		initTabColumn();
		initFragment();
	}

	// 选择的Column里面的Tab
	private void selectTab(int tab_postion) {

		if (AppApplication.getInstance().getUserInfoVO() != null && "1".equals(AppApplication.getInstance().getUserInfoVO().getReporter())
				&& "liveplay".equals(mUserChannelList.get(tab_postion).getType())) {
			mActivity.mBaseOkTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.live_add, 0);
			mActivity.mBaseOkTv.setVisibility(View.VISIBLE);
			mActivity.mBaseOkTv.setText("");
		} else {
			mActivity.mBaseOkTv.setVisibility(View.GONE);
		}

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

	/**
	 * 初始化Column栏目项
	 */
	private void initTabColumn() {
		mScrollViewItemContent.removeAllViews();
		int count = mUserChannelList.size();
		LogUtil.e("initTabColumn", mUserChannelList.get(0).getTitle());
		mColumnHorizontalScrollView.setParam(mActivity, mScreenWidth, mScrollViewItemContent, mShadeLeftImg, mShadeRightImg, mMoreClumnsLayout, mAllcolumnLayout);
		for (int i = 0; i < count; i++) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.leftMargin = BaseCommonUtils.dip2px(mActivity, 8);
			params.rightMargin = BaseCommonUtils.dip2px(mActivity, 8);
			TextView columnTextView = new TextView(mActivity);
			columnTextView.setTextAppearance(mActivity, R.style.top_category_scroll_view_item_text);
			columnTextView.setGravity(Gravity.CENTER);
			columnTextView.setPadding(BaseCommonUtils.dip2px(mActivity, 2), BaseCommonUtils.dip2px(mActivity, 2), BaseCommonUtils.dip2px(mActivity, 2), BaseCommonUtils.dip2px(mActivity, 2));
			columnTextView.setId(i);
			LogUtil.e("initTabColumn", "" + count);
			LogUtil.e("initTabColumn", mUserChannelList.get(i).getName());
			if ("十堰日报".equals(mUserChannelList.get(i).getName())) {
				columnTextView.setText("日报");
			} else if ("十堰晚报".equals(mUserChannelList.get(i).getName())) {
				columnTextView.setText("晚报");
			} else {
				columnTextView.setText(mUserChannelList.get(i).getName());
			}

			columnTextView.setTextColor(mActivity.getResources().getColorStateList(R.color.top_category_scroll_text_color_day));
			if (mColumnSelectIndex == i) {
				columnTextView.setSelected(true);
			}
			columnTextView.setOnClickListener(new OnClickListener() {

				private String islink;

				@Override
				public void onClick(View v) {
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

	final class InJavaScriptLocalObj {
		@JavascriptInterface
		public void showSource(String share_title) {
			LogUtil.d("share_title", share_title);
			HomeFragment.this.share_title = share_title;
		}

		@JavascriptInterface
		public void showSource1(String share_image) {
			LogUtil.d("share_image", share_image);
			HomeFragment.this.share_image = share_image;
		}

		@JavascriptInterface
		public void showSource2(String share_description) {
			LogUtil.d("share_description", share_description);
			HomeFragment.this.share_description = share_description;
		}

		@JavascriptInterface
		public void showSource3(String share_url) {
			LogUtil.d("share_url", share_url);
			HomeFragment.this.share_url = share_url;
		}

	}

	public void destroyWebView(WebView webView) {
		if (webView != null) {
			// STATE=false;
			webView.clearHistory();
			webView.clearCache(true);
			webView.freeMemory();
			webView.pauseTimers();
		}
	}

	// 初始化Fragment

	private void initFragment() {

		mFragments.clear();// 清空
		int count = mUserChannelList.size();
		for (int i = 0; i < count; i++) {
			Bundle data = new Bundle();
			data.putString("text", mUserChannelList.get(i).getName());
			data.putInt("id", mUserChannelList.get(i).getId());
			data.putString("type", mUserChannelList.get(i).getType());

			if ("city".equals(mUserChannelList.get(i).getType())) {
				mLocalFragment = new HomeNewsFragment();
				mLocalFragment.setArguments(data);
				mFragments.add(mLocalFragment);
			} else if ("sina".equals(mUserChannelList.get(i).getType())) {
				HomeNewsPublishFragment publishFragment = new HomeNewsPublishFragment();
				publishFragment.setArguments(data);
				mFragments.add(publishFragment);
			} else if ("image".equals(mUserChannelList.get(i).getType())) {
				HomeNewsPhotoFragment photoFragment = new HomeNewsPhotoFragment();
				photoFragment.setArguments(data);
				mFragments.add(photoFragment);
			} /*
			 * else if ("video".equals(mUserChannelList.get(i).getType())) {
			 * mVideoFragment = new HomeNewsVideoFragment();
			 * mVideoFragment.setArguments(data);
			 * mFragments.add(mVideoFragment); }
			 */
			else if ("liveplay".equals(mUserChannelList.get(i).getType())) {
				mLiveFragment = new LiveFragment();

				// liveFragment.setArguments(data);
				mFragments.add(mLiveFragment);
			} else if ("ribao".equals(mUserChannelList.get(i).getType())) {
				mDailyFragment = new HomeNewsDailyFragment();
				mDailyFragment.setArguments(data);
				mFragments.add(mDailyFragment);
			} else if ("wanbao".equals(mUserChannelList.get(i).getType())) {
				HomeNewsDailyFragment wanFragment = new HomeNewsDailyFragment();
				wanFragment.setArguments(data);
				mFragments.add(wanFragment);
			} else if (mUserChannelList.get(i).getLinkurl() != null) {
				data.putString("link", mUserChannelList.get(i).getLinkurl());
				data.putString("title", mUserChannelList.get(i).getTitle());
				mWebFragment = new HomeNewsWebFragment();
				mWebFragment.setArguments(data);
				mFragments.add(mWebFragment);
			} else {
				HomeNewsFragment newfragment = new HomeNewsFragment();
				newfragment.setArguments(data);
				mFragments.add(newfragment);
			}

		}
		FragmentActivity activity = (FragmentActivity) mActivity;
		// HomeNewsViewPagerAdapter adapetr = new
		// HomeNewsViewPagerAdapter(activity.getSupportFragmentManager(),
		// mFragments);
		// mViewPager.setOffscreenPageLimit(6);
		// mViewPager.setAdapter(adapetr);
		mFragemtAdapter.updateData(mFragments);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.more_columns_layout:
			mActivity.openActivityForResult(ChannelActivity.class, CHANNEL_CODE);
			// webView.removeView(progressbar);
			break;
		case R.id.base_ok_tv:
			Bundle bundle = new Bundle();
			bundle.putString("title", "直播现场");
			mActivity.openActivity(LiveAddThemeActivity.class, bundle, 0);
			break;
		case R.id.base_back_tv:

			mActivity.openActivity(KeyWordActivity.class);
			break;
		default:
			break;
		}
	}

	public void commit(String sortId) {
		RequestParams params = new RequestParams();
		try {
			params.put("uid", AppApplication.getInstance().getUid());
			params.put("sessionid", AppApplication.getInstance().getSessionid());
			params.put("cate", sortId);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		String url = Constant.DOMAIN_NAME + Constant.CHANNEL_SORT_URL;
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(new String(arg2));
					String str = (String) jsonObject.get("retinfo");
					String code = (String) jsonObject.get("code");
					if (Constant.RESULT_SUCCESS_CODE.equals(code)) {
						LogUtil.e("commit", code + str);
					} else {
						LogUtil.e("commit", code + str);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(final int position) {
		mViewPager.setCurrentItem(position);
		selectTab(position);

		HomeActivity activity = (HomeActivity) mActivity;
		if (mUserChannelList.get(position).getLinkurl() != null) {
			mActivity.mBaseTitleTv.setText("十堰头条·" + mUserChannelList.get(position).getTitle());
			mActivity.mBaseBackTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_head_back, 0, 0, 0);
			mActivity.base_back_tv_finish.setVisibility(View.VISIBLE);
			setHtmlTitle("十堰头条·" + mUserChannelList.get(position).getTitle());
			MessageHtml.getInstance().notifyWatcher("2", mUserChannelList.get(position).getLinkurl());
			setHtmlLink(mUserChannelList.get(position).getLinkurl());
			activity.setWebFragment(mWebFragment);

		} else {
			mActivity.mBaseBackTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search, 0, 0, 0);
			// mChanelLyaout.setVisibility(View.VISIBLE);
			activity.setWebFragment(null);
			mActivity.mBaseTitleTv.setText("十堰头条");
			setHtmlTitle("十堰头条");
			mActivity.base_back_tv_finish.setVisibility(View.GONE);
		}
		mChanelLyaout.postDelayed(new Runnable() {

			@Override
			public void run() {
				if (mUserChannelList.get(position).getLinkurl() != null) {
					mChanelLyaout.setVisibility(View.GONE);
				} else {
					mChanelLyaout.setVisibility(View.VISIBLE);
				}
			}
		}, 500);

	}

	@Override
	public boolean execute() {
		return getData();
	}

	@Override
	public void executeSuccess() {
		if (mChannelItem == null)
			return;
		List<ChannelItem.ListvBean.ListBeanX> list = mChannelItem.getListv().get(0).getList();
		LogUtil.e("list" + list.size(), list.toString());
		List<ChannelItem.ListvBean.ListBeanX> listCity = mChannelItem.getListv().get(1).getList();
		LogUtil.e("listCity" + listCity.size(), listCity.toString());
		List<ChannelItem.ListvBean.ListBeanX> listBranch = mChannelItem.getListv().get(2).getList();
		// LogUtil.e("listBranch" + listBranch.size(), listBranch.toString());
		// LogUtil.e("executeSuccess",
		// "AppApplication.getInstance().getUserInfoVO() == null" +
		// mChannelItem.getListv().get(0).getList().size() +
		// mUserChannelList.size() + mOtherChannelList.size()
		// + mChannelItem.getListv().get(1).getList().size() +
		// mCityChannelList.size());
		if (AppApplication.getInstance().getUserInfoVO() == null) {

			if ((list.size() + listCity.size() + listBranch.size() != mUserChannelList.size() + mOtherChannelList.size() + mCityChannelList.size() + mBranchChannelList.size())) {
				LogUtil.e("mUserChannelList.size() + mOtherChannelList.size()", "" + mUserChannelList.size() + mOtherChannelList.size());
				LogUtil.e("mCityChannelList.size()", "" + mCityChannelList.size());
				LogUtil.e("mBranchChannelList.size()", "" + mBranchChannelList.size());
				mUserChannelList.clear();
				mOtherChannelList.clear();
				mCityChannelList.clear();
				mBranchChannelList.clear();
				for (int i = 0; i < list.size(); i++) {
					ChannelItem.ListvBean.ListBeanX vo = list.get(i);
					vo.setName(vo.getTitle());
					vo.setId(BaseCommonUtils.parseInt(vo.getCid()));
					if ("1".equals(vo.getDef())) {
						mUserChannelList.add(vo);
						vo.setOrderId(i);
						vo.setSelected(1);
					} else {
						mOtherChannelList.add(vo);
						vo.setOrderId(i - mOtherChannelList.size());
						vo.setSelected(0);
					}
				}
				for (int i = 0; i < listCity.size(); i++) {
					ChannelItem.ListvBean.ListBeanX beanX = listCity.get(i);
					beanX.setName(beanX.getTitle());
					beanX.setId(BaseCommonUtils.parseInt(beanX.getCid()));
					mCityChannelList.add(beanX);
					beanX.setOrderId(i - mCityChannelList.size());
					beanX.setSelected(0);
				}
				for (int i = 0; i < listBranch.size(); i++) {
					ChannelItem.ListvBean.ListBeanX beanX1 = listBranch.get(i);
					beanX1.setName(beanX1.getTitle());
					beanX1.setId(BaseCommonUtils.parseInt(beanX1.getCid()));
					mBranchChannelList.add(beanX1);
					beanX1.setOrderId(i - mBranchChannelList.size());
					beanX1.setSelected(0);
				}
				LogUtil.e("mUserChannelList", mUserChannelList.toString());
				LogUtil.e("listCity", mCityChannelList.toString());
				LogUtil.e("listBranch", mBranchChannelList.toString());
				ChannelManage.getManage().saveUserChannel(mUserChannelList);
				ChannelManage.getManage().saveOtherChannel(mOtherChannelList);
				ChannelManage.getManage().saveCityChannel(mCityChannelList);
				ChannelManage.getManage().saveBranchChannel(mBranchChannelList);
			}
		} else {

			mUserChannelList.clear();
			mOtherChannelList.clear();
			mCityChannelList.clear();
			mBranchChannelList.clear();
			for (int i = 0; i < list.size(); i++) {
				ChannelItem.ListvBean.ListBeanX vo = list.get(i);
				vo.setName(vo.getTitle());
				vo.setId(BaseCommonUtils.parseInt(vo.getCid()));
				if ("1".equals(vo.getDef())) {
					mUserChannelList.add(vo);

					vo.setOrderId(i);
					vo.setSelected(1);
				} else {
					mOtherChannelList.add(vo);
					vo.setOrderId(i - mOtherChannelList.size());
					vo.setSelected(0);
				}
			}
			for (int i = 0; i < listCity.size(); i++) {
				ChannelItem.ListvBean.ListBeanX beanX = listCity.get(i);
				beanX.setName(beanX.getTitle());
				beanX.setId(BaseCommonUtils.parseInt(beanX.getCid()));
				if ("1".equals(beanX.getDef())) {
					mUserChannelList.add(beanX);

					beanX.setOrderId(i);
					beanX.setSelected(1);
				} else {
					mCityChannelList.add(beanX);
					beanX.setOrderId(i - mCityChannelList.size());
					beanX.setSelected(0);
				}
			}
			for (int i = 0; i < listBranch.size(); i++) {
				ChannelItem.ListvBean.ListBeanX beanX1 = listBranch.get(i);
				beanX1.setName(beanX1.getTitle());
				beanX1.setId(BaseCommonUtils.parseInt(beanX1.getCid()));
				if ("1".equals(beanX1.getDef())) {
					mUserChannelList.add(beanX1);

					beanX1.setOrderId(i);
					beanX1.setSelected(1);
				} else {
					mBranchChannelList.add(beanX1);
					beanX1.setOrderId(i - mBranchChannelList.size());
					beanX1.setSelected(0);
				}
			}

			Collections.sort(mUserChannelList, new SortArrayList());

			ChannelManage.getManage().saveUserChannel(mUserChannelList);
			ChannelManage.getManage().saveOtherChannel(mOtherChannelList);
			ChannelManage.getManage().saveCityChannel(mCityChannelList);
			ChannelManage.getManage().saveBranchChannel(mBranchChannelList);
			LogUtil.e("mUserChannelList1", mUserChannelList.toString());

		}

		initColumnData();

		mViewPager.setOffscreenPageLimit(mUserChannelList.size());
		// if (mUserChannelList.size() > 0) {
		// mViewPager.setOffscreenPageLimit(mUserChannelList.size());
		// } else {
		// mViewPager.setOffscreenPageLimit(4);
		// }
	}

	class SortArrayList implements Comparator {
		public int compare(Object o1, Object o2) {
			ChannelItem.ListvBean.ListBeanX beanX1 = (ChannelItem.ListvBean.ListBeanX) o1;
			ChannelItem.ListvBean.ListBeanX beanX2 = (ChannelItem.ListvBean.ListBeanX) o2;
			if (BaseCommonUtils.parseInt(beanX1.getSorts()) < BaseCommonUtils.parseInt(beanX2.getSorts()))
				return 1;
			return 0;
		}
	}

	@Override
	public void executeFailure() {
	}

	public boolean getData() {
		try {
			Gson gson = new Gson();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("uid", AppApplication.getInstance().getUid());
			if (mActivity.hasNetWork()) {
				String jsonStr = HttpClientUtil.getRequest(mActivity, Constant.CHANNEL_CATEGORY_ULR, map);
				mChannelItem = gson.fromJson(jsonStr, ChannelItem.class);
				mActivity.saveJsonCache(Constant.CHANNEL_CATEGORY_ULR, map, jsonStr);
			} else {
				String oldStr = mActivity.getCacheFromDatabase(Constant.CHANNEL_CATEGORY_ULR, map);
				mChannelItem = gson.fromJson(oldStr, ChannelItem.class);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, final Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		switch (resultCode) {
		case CHANNEL_CODE:
			if (intent == null)
				return;
			boolean ischange = (Boolean) intent.getBooleanExtra("ischange", false);
			if (ischange) {
				mUserChannelList = (ArrayList<ChannelItem.ListvBean.ListBeanX>) ChannelManage.getManage().getUserChannel();
				initColumnData();

				if (AppApplication.getInstance().getUserInfoVO() != null) {
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < mUserChannelList.size(); i++) {
						sb.append(mUserChannelList.get(i).getCid()).append(",");
					}
					String sortId = sb.toString();
					LogUtil.e("sortId", sortId);
					commit(sortId);
				}
			}

			if (intent.hasExtra("position")) {
				final int position = intent.getIntExtra("position", 0);
				final String islink = intent.getStringExtra("islink");
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

					}
				}, 10);
			}

			break;
		case SWITCH_LOCAL:
			mLocalFragment.onActivityResult(requestCode, resultCode, intent);
			break;
		default:
			break;
		}
	}

	public String getFragmentName() {
		return TAG;
	}

	public void checkUpdate() {
		new Thread() {
			public void run() {
				try {
					Gson gson = new Gson();
					HashMap<String, String> map = new HashMap<String, String>();
					String jsonStr = HttpClientUtil.getRequest(mActivity, Constant.CHECK_UPDATE_URL, map);
					mUpdateVersionVO = gson.fromJson(jsonStr, UpdateVersionVO.class);
					if (BaseCommonUtils.parseInt(mUpdateVersionVO.getVersion().getAndroid().getBuild()) > VersionUtils.getversionCode(mActivity)) {
						mViewPager.post(new Runnable() {
							@Override
							public void run() {
								updateDialog();
							}
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void updateDialog() {
		View v = LayoutInflater.from(mActivity).inflate(R.layout.dialog_textview, null);
		final TextView textView = (TextView) v.findViewById(R.id.textview);
		textView.setText(mUpdateVersionVO.getVersion().getAndroid().getMessage());
		int color = this.getResources().getColor(R.color.sy_title_color);
		mDialog = new BSDialog(mActivity, "版本更新", v, color, new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent updateIntent = new Intent(mActivity, DownloadService.class);
				updateIntent.putExtra("url", mUpdateVersionVO.getVersion().getAndroid().getUrl());
				updateIntent.putExtra("drawableId", R.drawable.ic_launcher);
				mActivity.startService(updateIntent);
				mDialog.dismiss();
			}
		});
		mDialog.show();
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		mActivity.mBaseOkTv.setVisibility(View.GONE);
		mActivity.mBaseHeadLayout.setVisibility(View.VISIBLE);
		if (mLiveFragment != null && mLiveFragment.isVisible() && "liveplay".equals(mUserChannelList.get(mColumnSelectIndex).getType())) {
			mLiveFragment.onHiddenChanged(hidden);
		}

		if (mWebFragment != null && mWebFragment.isVisible() && mUserChannelList.get(mColumnSelectIndex).getLinkurl() != null) {
			// mWebFragment.onHiddenChanged(hidden);
			MessageHtml.getInstance().notifyWatcher("2", getHtmlLink());
		}

		if (!hidden && mWebFragment != null && mWebFragment.isVisible() && mUserChannelList.get(mColumnSelectIndex).getLinkurl() != null) {
			mActivity.mBaseTitleTv.setText(getHtmlTitle());
			MessageHtml.getInstance().notifyWatcher("2", getHtmlLink());
		}

	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		MessageHtml.getInstance().remove(this);
	}

	@Override
	public void updateNotify(Object content, String status) {
		if (content instanceof String) {
			if (!status.equals(htmlLink))
				return;
			String str = (String) content;
			if ("1".equals(str)) {
				mViewPager.setCurrentItem(0);
				mActivity.mBaseBackTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search, 0, 0, 0);
				mActivity.base_back_tv_finish.setVisibility(View.GONE);
				mChanelLyaout.setVisibility(View.VISIBLE);
				mActivity.mBaseBackTv.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						mActivity.openActivity(KeyWordActivity.class);
					}
				});
			}
		}

	}

	public String getHtmlLink() {
		return htmlLink;
	}

	public void setHtmlLink(String htmlLink) {
		this.htmlLink = htmlLink;
	}

	public String getHtmlTitle() {
		return htmlTitle;
	}

	public void setHtmlTitle(String htmlTitle) {
		this.htmlTitle = htmlTitle;
	}

}
