package com.beisheng.synews.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.beisheng.base.database.CacheDbHelper;
import com.beisheng.base.http.HttpClientUtil;
import com.beisheng.base.http.HttpUtils;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.CacheUtil;
import com.beisheng.base.utils.Options;
import com.beisheng.synews.application.AppApplication;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.mode.ChannelItem;
import com.beisheng.synews.mode.ChannelManage;
import com.beisheng.synews.mode.LogoVO;
import com.google.gson.Gson;
import com.im.zhsy.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.HashMap;
import java.util.List;

public class LogoActivity extends Activity implements OnClickListener {
	public static final String APP_VERSION = "V2.0";
	private ImageView mLogoImageView, mAvdImg;
	private String mImageUrl;
	private LogoVO mLogoVO;
	private CountDownTimer mTimer;
	private TextView mTimeTv;
	private LinearLayout mTimeLayout, mTimeRootLayout;
	private CacheDbHelper mDbHelper;
	private ImageLoader mImageLoader;

	// 显示定位
	public LocationClient mLocationClient = null;
//	public BDLocationListener myListener = new MyLocationListener();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logo_activity);
		initView();
		bindViewsListener();

		// 县市定位
//		mLocationClient = new LocationClient(this.getApplicationContext()); // 声明LocationClient类
//		mLocationClient.registerLocationListener(myListener); // 注册监听函数
//		initLocation();
//		mLocationClient.start();
	}

	public void bindViewsListener() {
		mLogoImageView.setOnClickListener(this);
		// mTimeLayout.setOnClickListener(this);
		mTimeRootLayout.setOnClickListener(this);
	}

	@SuppressLint("NewApi")
	public void initView() {
		mLogoImageView = (ImageView) findViewById(R.id.logo_image);
		mAvdImg = (ImageView) findViewById(R.id.avd_image);
		mTimeTv = (TextView) findViewById(R.id.time_tv);
		mTimeLayout = (LinearLayout) findViewById(R.id.time_layout);
		mTimeLayout.setBackground(BaseCommonUtils.setBackgroundShap(this, 20, "#30000000", "#30000000"));
		mTimeRootLayout = (LinearLayout) findViewById(R.id.time_root_layout);
		mDbHelper = new CacheDbHelper(this);
		mImageLoader = ImageLoader.getInstance();

		Gson gson = new Gson();
		String oldStr = CacheUtil.getCacheFromDatabase(LogoActivity.this, Constant.LOGO_URL, null);
		mLogoVO = gson.fromJson(oldStr, LogoVO.class);

		if (mLogoVO != null && Constant.RESULT_SUCCESS_CODE.equals(mLogoVO.getCode()) && mLogoVO.getThum().length() > 0) {
			DisplayImageOptions options = Options.getOptionsDefaultIcon(0);
			mAvdImg.setVisibility(View.VISIBLE);
			mImageLoader.displayImage(mLogoVO.getThum(), mAvdImg, options, new ImageLoadingListener() {

				@Override
				public void onLoadingStarted(String arg0, View arg1) {
				}

				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {

				}

				@Override
				public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
					mTimeLayout.setVisibility(View.VISIBLE);
					initData();
				}

				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
				}
			});

		} else {
			mAvdImg.postDelayed(new Runnable() {
				@Override
				public void run() {
					Intent intent = new Intent();
					if (AppApplication.getInstance().getUserInfoVO() == null) {
						intent.setClass(LogoActivity.this, GuideActivity.class);
					} else {
						intent.setClass(LogoActivity.this, HomeActivity.class);
					}
					LogoActivity.this.startActivity(intent);
					LogoActivity.this.finish();
				}
			}, 2000);
		}
		getHomeData();
	}

	public boolean getData() {
		try {
			Gson gson = new Gson();
			HashMap<String, String> map = new HashMap<String, String>();

			if (HttpUtils.isNetworkAvailable(LogoActivity.this)) {
				String jsonStr = HttpClientUtil.getRequest(this, Constant.DOMAIN_NAME + Constant.LOGO_URL, map);
				mLogoVO = gson.fromJson(jsonStr, LogoVO.class);
				CacheUtil.saveJsonCache(LogoActivity.this, Constant.LOGO_URL, null, jsonStr);
			} else {
				String oldStr = CacheUtil.getCacheFromDatabase(LogoActivity.this, Constant.LOGO_URL, null);
				mLogoVO = gson.fromJson(oldStr, LogoVO.class);
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// 先使用本地数据，
	public void initData() {
		long time = 0;
		if (mLogoVO == null) {
			time = 4000;
		} else {
			time = BaseCommonUtils.parseInt(mLogoVO.getSecond()) * 1000;
		}
		mTimer = new CountDownTimer(time, 1000) {
			@Override
			public void onTick(long arg0) {
				mTimeTv.setText(arg0 / 1000 + "");
				Animation animation = AnimationUtils.loadAnimation(LogoActivity.this, R.anim.anim_textview_time);
				animation.reset();
				mTimeTv.startAnimation(animation);
			}

			@Override
			public void onFinish() {
				Intent intent = new Intent();
				intent.setClass(LogoActivity.this, HomeActivity.class);
				LogoActivity.this.startActivity(intent);
				LogoActivity.this.finish();
			}
		};
		mTimer.start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mTimer != null)
			mTimer.cancel();
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.logo_image:
			if ("".equals(mLogoVO.getLink()))
				return;

			if (mTimer != null) {
				if (mLogoVO == null) {
					intent.putExtra("url", "http://www.10yan.com");
				} else {
					intent.putExtra("url", mLogoVO.getLink());
				}

				intent.putExtra("name", "头条推荐");
				intent.putExtra("result", "1");// 有返回结果
				intent.setClass(this, WebViewActivity.class);
				startActivityForResult(intent, 1);
				mTimer.cancel();
			}
			break;

		case R.id.time_root_layout:
			if (mTimer != null) {

				intent.setClass(LogoActivity.this, HomeActivity.class);
				LogoActivity.this.startActivity(intent);
				this.finish();
				mTimer.cancel();
			}
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		switch (requestCode) {
		case 1:
			mTimer.start();
			break;

		default:
			break;
		}
	}

	public void getHomeData() {
		// 首页后台加载
		new Thread() {
			public void run() {
				try {
					Gson gson = new Gson();
					HashMap<String, String> map = new HashMap<String, String>();
					List<ChannelItem.ListvBean.ListBeanX> list = ChannelManage.getManage().getUserChannel();
					map.put("cid", list.get(0).getCid());
					map.put("page", "1");

					if (HttpUtils.isNetworkAvailable(LogoActivity.this)) {
						String jsonStr = HttpClientUtil.getRequest(LogoActivity.this, Constant.DOMAIN_NAME + Constant.HOME_NEWS_URL, map);
						if (jsonStr != null && !"".equals(jsonStr))
							CacheUtil.saveJsonCache(LogoActivity.this, Constant.HOME_NEWS_URL, map, jsonStr);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();

		// 导航栏后台加载
		new Thread() {
			public void run() {
				Gson gson = new Gson();
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("uid", AppApplication.getInstance().getUid());
				if (HttpUtils.isNetworkAvailable(LogoActivity.this)) {
					String jsonStr = HttpClientUtil.getRequest(LogoActivity.this, Constant.CHANNEL_CATEGORY_ULR, map);
					if (jsonStr != null && !"".equals(jsonStr)) {
						ChannelItem channelItem = gson.fromJson(jsonStr, ChannelItem.class);
						CacheUtil.saveJsonCache(LogoActivity.this, Constant.CHANNEL_CATEGORY_ULR, map, jsonStr);
					}
				}
			};
		}.start();

		// 后台加载广告图片
		new Thread() {
			public void run() {
				getData();
			};
		}.start();

		// 后台加载县市
		new Thread() {
			public void run() {
				try {
					Gson gson = new Gson();
					HashMap<String, String> localMap = new HashMap<String, String>();
					String jsonStr = HttpClientUtil.getRequest(LogoActivity.this, Constant.DOMAIN_NAME + Constant.LOCATION_URL, localMap);
					if (jsonStr != null && !"".equals(jsonStr))
						CacheUtil.saveJsonCache(LogoActivity.this, Constant.LOCATION_URL, localMap, jsonStr);
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();

		// 后台请求日报日期
		new Thread() {
			public void run() {
				try {
					Gson gson = new Gson();
					HashMap<String, String> localMap = new HashMap<String, String>();
					String ribaoStr = HttpClientUtil.getRequest(LogoActivity.this, Constant.RIBAO_URL, localMap);
					if (ribaoStr != null && !"".equals(ribaoStr))
						CacheUtil.saveJsonCache(LogoActivity.this, Constant.RIBAO_URL, localMap, ribaoStr);
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

	public void initLocation() {
		// mLocationClient = new
		// LocationClient(mActivity.getApplicationContext()); //
		// 声明LocationClient类
		// mLocationClient.registerLocationListener(myListener); // 注册监听函数

		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		int span = 1000;
		option.setScanSpan(0);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
		option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		mLocationClient.setLocOption(option);
	}

//	public class MyLocationListener implements BDLocationListener {
//
//		@Override
//		public void onReceiveLocation(BDLocation location) {
//			String str = location.getAddrStr();
//			SharePreferenceUtil.putSharedpreferences(LogoActivity.this, " shiyan_address", "address", str);
//		}
//
//		@Override
//		public void onConnectHotSpotMessage(String s, int i) {
//
//		}
//	}

}
