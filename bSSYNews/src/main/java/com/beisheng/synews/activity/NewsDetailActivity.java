package com.beisheng.synews.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.http.HttpClientUtil;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.DateUtils;
import com.beisheng.base.utils.Options;
import com.beisheng.synews.adapter.DiscussAdapter;
import com.beisheng.synews.adapter.HomeNewsFragmentAdapter;
import com.beisheng.synews.application.AppApplication;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.fragment.BottomFragment;
import com.beisheng.synews.mode.NewsVO;
import com.beisheng.synews.utils.LogUtil;
import com.beisheng.synews.utils.StartViewUitl;
import com.beisheng.synews.videoplay.MediaHelp;
import com.beisheng.synews.videoplay.VideoSuperPlayer;
import com.beisheng.synews.videoplay.VideoSuperPlayer.VideoPlayCallbackImpl;
import com.beisheng.synews.view.BSListViewConflict;
import com.google.gson.Gson;
import com.iflytek.cloud.SpeechSynthesizer;
import com.im.zhsy.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.sufficientlysecure.htmltextview.HtmlResImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.io.Serializable;
import java.util.HashMap;

@SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
public class NewsDetailActivity extends BaseActivity implements OnLongClickListener, OnClickListener, OnItemClickListener {
	private NewsVO mNewsVO;
	private TextView mTitleTv, mSourceTv, mTimeTv;
//	private WebView mContentWb;
	private HtmlTextView mHtmlTextView;
	private String mContentId;
	private String mGovermentid;// 政务专用
	private String mSuburl;
	private String mUid;// 日报评论用
	private String mLink;// 日报专用
	private BSListViewConflict mLoveLv, mCommnetLv;
	private HomeNewsFragmentAdapter mLoveAdapter;
	private DiscussAdapter mDisscAdapter;
	private ImageView mAdvertImg;
	private LinearLayout mBottomLayout;
	private TextView mCountTv;
	private BottomFragment mBottomFragment;
	private LinearLayout mDisscussLayout;
	private TextView mViewMoreTv;
	private TextView mLoveTitleTv;
	private SpeechSynthesizer mTts;
	private String mIsReplay;

	// 视频播放
	private ImageView mVideoImg;
	private boolean mIsPlaying;
	private ImageView mPlayBtnView;
	private VideoSuperPlayer mSuperVideoPlayer;
	private RelativeLayout mVideoLayout;
	private TextView mReadTv;

	private LinearLayout mKeywordLayout;
	private TextView mLastOnclickTv;
	private TextView mZhengWuTv;
	private String mNewType;
	private TextView mRibaoTv01, mRibaoTv02;
	private Intent intent;

	@Override
	public void baseSetContentView() {
		View.inflate(this, R.layout.news_detail_activity, mBaseContentLayout);
		mTts = SpeechSynthesizer.createSynthesizer(this, null);
	}

	@Override
	public boolean getDataResult() {
		if (mLink != null) {
			return getDailyData();
		} else {
			return getData();
		}

	}

	@SuppressLint("NewApi")
	@Override
	public void initView() {
		mBaseTitleTv.setText("新闻详情");
		mRibaoTv01 = (TextView) findViewById(R.id.ribao_tv_01);
		mRibaoTv02 = (TextView) findViewById(R.id.ribao_tv_02);
		mPlayBtnView = (ImageView) findViewById(R.id.play_btn);
		mSuperVideoPlayer = (VideoSuperPlayer) findViewById(R.id.video);
		mVideoImg = (ImageView) findViewById(R.id.video_img);
		mVideoLayout = (RelativeLayout) findViewById(R.id.video_layout);

		mTitleTv = (TextView) findViewById(R.id.title_tv);
		mSourceTv = (TextView) findViewById(R.id.source_tv);
		mTimeTv = (TextView) findViewById(R.id.time_tv);
//		mContentWb = (WebView) findViewById(R.id.content_wb);
		mHtmlTextView= (HtmlTextView) findViewById(R.id.content_wb);
		mCountTv = (TextView) findViewById(R.id.count_tv);
		mCommnetLv = (BSListViewConflict) findViewById(R.id.comment_lv);
		mDisscAdapter = new DiscussAdapter(this);
		mDisscAdapter.setIsShow("1");
		mCommnetLv.setAdapter(mDisscAdapter);
		mLoveLv = (BSListViewConflict) findViewById(R.id.love_lv);
		mLoveAdapter = new HomeNewsFragmentAdapter(this);
		mLoveAdapter.setStatus("1");
		mLoveLv.setAdapter(mLoveAdapter);
		mAdvertImg = (ImageView) findViewById(R.id.advert_img);
		mBottomLayout = (LinearLayout) findViewById(R.id.bottom_layout);
		mDisscussLayout = (LinearLayout) findViewById(R.id.disscuss_layout);
		mViewMoreTv = (TextView) findViewById(R.id.view_more_tv);
		mLoveTitleTv = (TextView) findViewById(R.id.love_title_tv);
		initData();
		mReadTv = (TextView) findViewById(R.id.read_tv);
		mKeywordLayout = (LinearLayout) findViewById(R.id.keyword_layout);
		mZhengWuTv = (TextView) findViewById(R.id.zheng_wu_tv);
	}

	public void initData() {
		Intent intent = this.getIntent();
		mContentId = intent.getStringExtra("id");
		mIsReplay = intent.getStringExtra("isReplay");
		mGovermentid = intent.getStringExtra("govermentid");
		mSuburl = intent.getStringExtra("suburl");
		mNewType = intent.getStringExtra("new_type");
		mLink = intent.getStringExtra("link");

		// 获取本地数据，优化速度
		if (mLink != null) {
			Gson gson = new Gson();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("uid", AppApplication.getInstance().getUid());
			String oldStr = getCacheFromDatabase(mLink, map);
			mNewsVO = gson.fromJson(oldStr, NewsVO.class);
			if (mNewsVO != null && Constant.RESULT_SUCCESS_CODE.equals(mNewsVO.getCode())) {
				executeSuccess();
			}
		} else {
			Gson gson = new Gson();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("contentid", mContentId);
			map.put("govermentid", mGovermentid);
			map.put("suburl", mSuburl);
			map.put("new_type", mNewType);
			map.put("uid", AppApplication.getInstance().getUid());
			String oldStr = getCacheFromDatabase(Constant.NEWS_DETAIL_URL, map);
			mNewsVO = gson.fromJson(oldStr, NewsVO.class);
			if (mNewsVO != null && Constant.RESULT_SUCCESS_CODE.equals(mNewsVO.getCode())) {
				executeSuccess();
			}
		}

	}

	@Override
	public void bindViewsListener() {
//		mContentWb.setOnLongClickListener(this);
		mCommnetLv.setOnItemClickListener(this);
		mLoveLv.setOnItemClickListener(this);
		mViewMoreTv.setOnClickListener(this);
		mAdvertImg.setOnClickListener(this);
		mPlayBtnView.setOnClickListener(new MyOnclick());
		mReadTv.setOnClickListener(this);
		mZhengWuTv.setOnClickListener(this);
	}

	public boolean getData() {
		try {
			Gson gson = new Gson();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("contentid", mContentId);
			map.put("govermentid", mGovermentid);
			map.put("suburl", mSuburl);
			map.put("new_type", mNewType);

			map.put("uid", AppApplication.getInstance().getUid());
			if (hasNetWork()) {
				String jsonStr = HttpClientUtil.getRequest(this, Constant.DOMAIN_NAME + Constant.NEWS_DETAIL_URL, map);
				mNewsVO = gson.fromJson(jsonStr, NewsVO.class);
				LogUtil.e("mNewsVO",mNewsVO.toString());
				saveJsonCache(Constant.NEWS_DETAIL_URL, map, jsonStr);

			} else {
				String oldStr = getCacheFromDatabase(Constant.NEWS_DETAIL_URL, map);
				mNewsVO = gson.fromJson(oldStr, NewsVO.class);
			}

			if (Constant.RESULT_SUCCESS_CODE.equals(mNewsVO.getCode())) {
				LogUtil.e("11111","111111");
				return true;
			} else {
				LogUtil.e("222222","2222222");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean getDailyData() {
		try {
			Gson gson = new Gson();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("uid", AppApplication.getInstance().getUid());
			if (hasNetWork()) {
				String jsonStr = HttpClientUtil.getRequest(this, mLink, map);
				mNewsVO = gson.fromJson(jsonStr, NewsVO.class);
				saveJsonCache(mLink, map, jsonStr);

			} else {
				String oldStr = getCacheFromDatabase(mLink, map);
				mNewsVO = gson.fromJson(oldStr, NewsVO.class);
			}

			if (Constant.RESULT_SUCCESS_CODE.equals(mNewsVO.getCode())) {
				LogUtil.e("mNewsVO","=========="+mNewsVO.toString());
				return true;
			} else {
				LogUtil.e("44444444","4444444444444");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void executeSuccess() {
		super.executeSuccess();
		if (mLink != null) {
			if (!"".equals(mNewsVO.getIntrotitle())) {
				mRibaoTv01.setVisibility(View.VISIBLE);
				mRibaoTv01.setText(mNewsVO.getIntrotitle());
			} else {
				mRibaoTv01.setVisibility(View.GONE);
			}
			if (!"".equals(mNewsVO.getSubtitle())) {
				mRibaoTv02.setVisibility(View.VISIBLE);
				mRibaoTv02.setText(mNewsVO.getSubtitle());
			} else {
				mRibaoTv02.setVisibility(View.GONE);
			}
		} else {
			mRibaoTv02.setVisibility(View.GONE);
			mRibaoTv01.setVisibility(View.GONE);
		}

		if (mNewsVO.getOuturl() != null) {
			DisplayImageOptions options = Options.getOptionsDefaultIcon(R.drawable.video_def);
			mImageLoader.displayImage(mNewsVO.getThumb(), mVideoImg, options);
			mVideoLayout.setVisibility(View.VISIBLE);
		} else {
			mVideoLayout.setVisibility(View.GONE);
		}

		mTitleTv.setText(mNewsVO.getTitle());
		mSourceTv.setText("来源：" + mNewsVO.getSource());
		if (mLink != null) {
			mTimeTv.setText(DateUtils.parseDateDay(mNewsVO.getCreatime()));
		} else {
			mTimeTv.setText(DateUtils.parseDateDayAndHour(mNewsVO.getCreatime()));
		}

		String content = mNewsVO.getContent();

//		mContentWb.loadDataWithBaseURL(FileUtil.getSaveFilePath(this), content, "text/html", "utf-8", null);
		TextView tv = new TextView(this);
//
//		// 去掉这部分内容 p{text-indent:2em;line-height:25px;}
		tv.setText(Html.fromHtml(content).toString().replace("p{text-indent:2em;line-height:1.5;}", ""));
//		WebviewUtil.SetWebview(mContentWb);
//		WebviewUtil.setWebViewFontSize(this, mContentWb);
//		mContentWb.setWebViewClient(new WebViewPictureUtil(this, mContentWb, "img", "this.src"));

		mHtmlTextView.setHtml(content, new HtmlResImageGetter(mHtmlTextView));
		mAdvertImg.postDelayed(new Runnable() {

			@Override
			public void run() {
				// 显示关键字 关键字去掉了
				// if (mNewsVO.getTags() != null) {
				// String[] keywordArray = mNewsVO.getTags().split(",");
				// showKeywordLayout(keywordArray);
				// mKeywordLayout.setVisibility(View.VISIBLE);
				// }
				// 广告
				if (mNewsVO.getBanner() == null || "".equals(mNewsVO.getBanner().getThum()) || mNewsVO.getBanner().getThum() == null) {
					mAdvertImg.setVisibility(View.GONE);
				} else {
					mImageLoader.displayImage(mNewsVO.getBanner().getThum(), mAdvertImg, mOptions);
					mAdvertImg.setVisibility(View.VISIBLE);
				}

				if (mNewsVO.getGoverment_title() != null) {
					mZhengWuTv.setText(mNewsVO.getGoverment_title());
					mZhengWuTv.setVisibility(View.VISIBLE);
				}
				// 评论
				if (mNewsVO.getReply() != null && mNewsVO.getReply().size() > 0) {
					mDisscussLayout.setVisibility(View.VISIBLE);
					mDisscAdapter.updateData(mNewsVO.getReply());
					mCountTv.setText("评论 " + mNewsVO.getComments());
					if (BaseCommonUtils.parseInt(mNewsVO.getMore_conmments()) > 3)
						mViewMoreTv.setVisibility(View.VISIBLE);
				} else {
					mDisscussLayout.setVisibility(View.GONE);
				}
				// 猜你喜欢
				if (mNewsVO.getLike() != null && mNewsVO.getLike().size() > 0) {
					mLoveAdapter.updateData(mNewsVO.getLike());
					mLoveTitleTv.setVisibility(View.VISIBLE);
					mLoveLv.setVisibility(View.VISIBLE);
				} else {
					mLoveTitleTv.setVisibility(View.GONE);
					mLoveLv.setVisibility(View.GONE);
				}

			}
		}, 1000);

		try {
			// 底部View
			mBottomFragment = new BottomFragment();
			// mBottomFragment.setSpeechStr(tv.getText().toString());
			LogUtil.e("mNewsVO.getRead_content()","======"+mNewsVO.getRead_content());
			mBottomFragment.setSpeechStr(Html.fromHtml(content).toString().replace("p{text-indent:2em;line-height:1.5;}", ""));
			mBottomFragment.setTts(mTts);
			mBottomFragment.setIsRePlay(mIsReplay);
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction transaction = fragmentManager.beginTransaction();
			transaction.replace(R.id.bottom_layout, mBottomFragment);
			mBottomFragment.setNewsvo(mNewsVO);
			transaction.commit();
		} catch (Exception e) {
		}
	}

	@Override
	public void executeFailure() {
		super.executeFailure();
		if (mNewsVO != null)
			showCustomToast(mNewsVO.getRetinfo());
		else
			showCustomToast("亲，请检查网络哦");
	}

	@Override
	public boolean onLongClick(View arg0) {
		return true;
	}

	@Override
	public void onClick(View v) {
		Bundle bundle = new Bundle();
		switch (v.getId()) {
		case R.id.advert_img:
			StartViewUitl.startView(this, "0", mNewsVO.getBanner().getContentid(), mNewsVO.getBanner().getLink(), mNewsVO.getGovermentid(), mNewsVO.getTypename());
			break;

		case R.id.view_more_tv:
			bundle.putString("id", mNewsVO.getContentid());
			bundle.putString("type", mNewsVO.getSuburl());
			bundle.putString("title", mNewsVO.getTitle());
			openActivity(DisscussActivity.class, bundle, 0);
			break;

		case R.id.read_tv:
			if ("语音播报".equals(mReadTv.getText().toString())) {
				mReadTv.setText("停止播报");
				mBottomFragment.speech();
			} else {
				mReadTv.setText("语音播报");
				mTts.stopSpeaking();
			}

			break;

		case R.id.zheng_wu_tv:
			bundle.putString("id", mNewsVO.getGoverment_contentid());
			bundle.putString("isReplay", "0");
			bundle.putString("suburl", "6");
			openActivity(ZhengWuActivity.class, bundle, 0);
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
		if (adapterView.getAdapter().getCount() == 0) {
			LogUtil.e("onItemClick","==================1");
			return;
		}
		NewsVO vo = (NewsVO) adapterView.getAdapter().getItem(position);
		if (vo == null) {
			LogUtil.e("onItemClick","==================2");
			return;
		}
		if (adapterView == mCommnetLv) {
			if (vo.getChildren() == null) {
				mBottomFragment.commitSecondDisscuss(vo);

				LogUtil.e("onItemClick","==================3");
			} else {
				LogUtil.e("onItemClick","==================4");
				Bundle bundle = new Bundle();
				bundle.putString("id", mContentId);
				bundle.putString("type", mSuburl);
				bundle.putSerializable("vo", (Serializable) vo);
				bundle.putString("title", mNewsVO.getTitle());
				openActivity(DisscussDetailActivity.class, bundle, 0);
			}

		} else {
			LogUtil.e("onItemClick","==================5");
			LogUtil.e("vo.getSuburl()","=================="+vo.getSuburl());
			LogUtil.e("vo.getContentid()","=================="+vo.getContentid());
			LogUtil.e("vo.getLink()","=================="+vo.getLink());
			LogUtil.e("vo.getGovermentid()","=================="+vo.getGovermentid());
			LogUtil.e("mNewsVO.getTypename()","=================="+mNewsVO.getTypename());
//			Bundle bundle = new Bundle();
//			bundle.putString("id",  vo.getContentid());
//			bundle.putString("suburl", vo.getSuburl());
//			bundle.putString("new_type", mNewsVO.getTypename());
//			intent = new Intent(getApplicationContext(), NewsDetailActivity.class);
//			intent.putExtras(bundle);
//			startActivity(intent);
//			StartViewUitl.startView(this, vo.getSuburl(), vo.getContentid(), vo.getLink(), vo.getGovermentid(), mNewsVO.getTypename());
			StartViewUitl.startView(this, vo.getSuburl(), vo.getContentid(), vo.getLink(), vo.getGovermentid(), mNewsVO.getTypename(),vo.getTitle());
		}
	}

	class MyOnclick implements OnClickListener, VideoPlayCallbackImpl {
		@Override
		public void onClick(View v) {
			MediaHelp.release();
			mIsPlaying = true;
			mSuperVideoPlayer.setVisibility(View.VISIBLE);
			mSuperVideoPlayer.loadAndPlay(MediaHelp.getInstance(), mNewsVO.getOuturl(), 0, false);
			mSuperVideoPlayer.setVideoPlayCallback(this);
		}

		@Override
		public void onCloseVideo() {
			closeVideo();
		}

		@Override
		public void onSwitchPageType() {
			if (NewsDetailActivity.this.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
				Intent intent = new Intent(NewsDetailActivity.this, VideoFullActivity.class);
				intent.putExtra("url", mNewsVO.getOuturl());
				intent.putExtra("position", mSuperVideoPlayer.getCurrentPosition());
				NewsDetailActivity.this.startActivityForResult(intent, 1);
			}
		}

		@Override
		public void onPlayFinish() {
			closeVideo();
		}

		private void closeVideo() {
			mIsPlaying = false;
			mSuperVideoPlayer.close();
			MediaHelp.release();
			mPlayBtnView.setVisibility(View.VISIBLE);
			mSuperVideoPlayer.setVisibility(View.GONE);
		}
	}

	public void stopVideo() {
		mSuperVideoPlayer.close();
		MediaHelp.release();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mTts.stopSpeaking();
		stopVideo();
	}

	public void showKeywordLayout(String[] keywordArray) {
		mKeywordLayout.removeAllViews();
		LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		for (int i = 0; i < keywordArray.length; i++) {
			textViewParams.setMargins(0, 0, BaseCommonUtils.dip2px(NewsDetailActivity.this, 15), BaseCommonUtils.dip2px(NewsDetailActivity.this, 15));
			final TextView childTv = new TextView(NewsDetailActivity.this);
			childTv.setPadding(BaseCommonUtils.dip2px(NewsDetailActivity.this, 10), BaseCommonUtils.dip2px(NewsDetailActivity.this, 5), BaseCommonUtils.dip2px(NewsDetailActivity.this, 10),
					BaseCommonUtils.dip2px(NewsDetailActivity.this, 5));
			childTv.setLayoutParams(textViewParams);
			childTv.setText(keywordArray[i]);
			childTv.setBackground(BaseCommonUtils.setBackgroundShap(NewsDetailActivity.this, 20, "#DDDDDD", "#F8F8F8"));
			childTv.setTextColor(Color.parseColor("#818181"));
			childTv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					childTv.setBackground(BaseCommonUtils.setBackgroundShap(NewsDetailActivity.this, 20, "#B10304", "#B10304"));
					childTv.setTextColor(Color.parseColor("#ffffff"));
					if (mLastOnclickTv != null) {
						mLastOnclickTv.setBackground(BaseCommonUtils.setBackgroundShap(NewsDetailActivity.this, 20, "#DDDDDD", "#F8F8F8"));
						mLastOnclickTv.setTextColor(Color.parseColor("#818181"));
					}
					mLastOnclickTv = childTv;

					Bundle bundle = new Bundle();
					bundle.putString("keyword", childTv.getText().toString());
					openActivity(KeyWordActivity.class, bundle, 0);
				}
			});
			mKeywordLayout.addView(childTv);
		}
	}

}
