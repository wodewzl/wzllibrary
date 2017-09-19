package com.beisheng.synews.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.fragment.BaseFragment;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.JsonUtil;
import com.beisheng.base.utils.ThreadUtil;
import com.beisheng.base.view.BSBadgeView;
import com.beisheng.synews.activity.CommunityAddActivity;
import com.beisheng.synews.activity.DisscussActivity;
import com.beisheng.synews.activity.LoginActivity;
import com.beisheng.synews.application.AppApplication;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.mode.CacheListVO;
import com.beisheng.synews.mode.CommunityVO;
import com.beisheng.synews.mode.LiveVO;
import com.beisheng.synews.mode.NewsVO;
import com.beisheng.synews.utils.LogUtil;
import com.beisheng.synews.utils.PointsAddUtil;
import com.beisheng.synews.utils.ShareUtil;
import com.beisheng.synews.view.BSPopwindowEditText;
import com.beisheng.synews.view.BSPopwindowEditText.CommitCallback;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.im.zhsy.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

@SuppressLint("NewApi")
public class BottomFragment extends BaseFragment implements OnClickListener {
	private String TAG = "BottomFragment";
	private BaseActivity mActivity;
	private TextView mCommentTv;
	private BSPopwindowEditText mPopEditText;
	private boolean mCommitFlag = true;
	private String mContent;
	private String mPid = "0";
	private String mType;// 0普通新闻，1论坛，2图片，3专题，4视频，5直播，6数字报
	private String mContentId;
	private String mTitle;
	private String mUserid;
	private String mLink;

	public NewsVO newsvo;// 新闻对象
	private CommunityVO communityvo;// 社区对象
	private ImageView mImg00, mImg01, mImg02, mImg03, mImg04, mImg05;
	public String speechStr;
	public SpeechSynthesizer tts;
	private String mFavorStr = "";
	private boolean mTtsClick = true;
	private String isRePlay = "1";
	public LiveVO liveVo;// 用于数字报跳转

	private String mSpeakStr[] = new String[2];
	private int mSpeakCount = 0;

	public static BottomFragment newInstance() {
		BottomFragment bottomFragment = new BottomFragment();
		return bottomFragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = (BaseActivity) activity;
		SpeechUtility.createUtility(mActivity, SpeechConstant.APPID + "=578f362e");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.bottom_fragment, container, false);
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
		mCommentTv = (TextView) view.findViewById(R.id.comment_et);
		mCommentTv.setBackground(BaseCommonUtils.setBackgroundShap(mActivity, 20, R.color.devider_bg, R.color.C1));
		mPopEditText = new BSPopwindowEditText(mActivity, mCallback);
		mImg00 = (ImageView) view.findViewById(R.id.img_00);
		mImg01 = (ImageView) view.findViewById(R.id.img_01);
		mImg03 = (ImageView) view.findViewById(R.id.img_03);
		mImg04 = (ImageView) view.findViewById(R.id.img_04);
		mImg05 = (ImageView) view.findViewById(R.id.img_05);

		// 根据类型控制底部图标
		if (newsvo != null) {
			if (!"4".equals(newsvo.getSuburl())) {
				mImg00.setVisibility(View.VISIBLE);
			} else {
				mImg00.setVisibility(View.GONE);
			}
			mType = newsvo.getSuburl();
			mContentId = newsvo.getContentid();
			mTitle = newsvo.getTitle();
			mLink = newsvo.getLink();
		}

		if (communityvo != null) {
			mImg00.setVisibility(View.GONE);
		}

		// 数字报专用，一开始效果图没设计好，导致不用重用，醉了
		if (liveVo != null) {
			mImg03.setVisibility(View.VISIBLE);
			mImg00.setVisibility(View.GONE);
			if (!"0".equals(liveVo.getComments())) {
				BSBadgeView commentBadge = new BSBadgeView(mActivity, mImg03);
				commentBadge.setText(liveVo.getComments());
				commentBadge.setBadgeMargin(BaseCommonUtils.dip2px(mActivity, 0), BaseCommonUtils.dip2px(mActivity, 0));
				commentBadge.setTextSize(10);
				commentBadge.show();
			}

			mType = liveVo.getSuburl();
			mContentId = liveVo.getLid();
			mTitle = liveVo.getTitle();
		}

		// 政务隐藏下面的输入框
		if ("0".equals(getIsRePlay())) {
			mCommentTv.setVisibility(View.INVISIBLE);
		}

	}

	private void bindViewsListener() {
		mCommentTv.setOnClickListener(this);
		mImg01.setOnClickListener(this);
		mImg00.setOnClickListener(this);
		mImg03.setOnClickListener(this);
		mImg04.setOnClickListener(this);
		mImg05.setOnClickListener(this);
	}

	CommitCallback mCallback = new CommitCallback() {

		@Override
		public void commtiCallback(String content) {
			mContent = content;
			commit(Constant.DISSCUSS_URL);
		}
	};

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
		case R.id.comment_et:
			if (AppApplication.getInstance().getUserInfoVO() == null) {
				mActivity.openActivity(LoginActivity.class);
				return;
			}
			if (newsvo != null) {
				if ("0".equals(getIsRePlay())) {
					mActivity.showCustomToast("亲，政务不支持评论哦");
					return;
				}
				mPopEditText.showPopupWindow(v);
			} else if (liveVo != null) {
				mPopEditText.showPopupWindow(v);
			} else if (communityvo != null) {
				Bundle bundle = new Bundle();
				bundle.putString("fid", communityvo.getFid());
				bundle.putString("tid", communityvo.getTid());
				bundle.putString("title", communityvo.getTitle());
				bundle.putString("type", "2");
				mActivity.openActivity(CommunityAddActivity.class, bundle, 0);
			}

			break;

		case R.id.img_01:
			if (AppApplication.getInstance().getUserInfoVO() == null) {
				mActivity.showCustomToast("亲，请先登录哦");
				return;
			}
			if (newsvo != null && "1".equals(newsvo.getIspraise())) {
				mActivity.showCustomToast("亲，只能点一次哦");
				return;
			}

			commit(Constant.PRAISE_URL);
			break;
		case R.id.img_03:
			Bundle bundle = new Bundle();
			bundle.putString("id", mContentId);
			bundle.putString("type", mType);
			bundle.putString("title", mTitle);
			mActivity.openActivity(DisscussActivity.class, bundle, 0);
			break;
		case R.id.img_04:
			if (newsvo != null) {
				ShareUtil.share(mActivity, newsvo.getShare_img(), newsvo.getShare_tit(), newsvo.getShare_des(), newsvo.getShare_url());
				PointsAddUtil.commitdPoints("4", newsvo.getContentid(), newsvo.getTitle());
			} else if (communityvo != null) {
				ShareUtil.share(mActivity, communityvo.getShare_img(), communityvo.getShare_tit(), communityvo.getShare_des(), communityvo.getShare_url());
				PointsAddUtil.commitdPoints("4", communityvo.getTid(), communityvo.getShare_tit());
			} else if (liveVo != null) {
				ShareUtil.share(mActivity, liveVo.getShare_img(), liveVo.getShare_tit(), liveVo.getShare_des(), liveVo.getShare_url());
				PointsAddUtil.commitdPoints("4", liveVo.getLid(), liveVo.getTitle());
			}

			break;

		case R.id.img_05:
			CacheListVO vo = new CacheListVO();
			if (newsvo != null) {
				vo.setContentid(mContentId);
				vo.setSuburl(mType);
				vo.setTitle(mTitle);
				vo.setLink(mLink);
				vo.setCreatime(newsvo.getCreatime());
				vo.setGovermentid(newsvo.getGoverment_contentid());
			} else if (liveVo != null) {
				vo.setContentid(mContentId);
				vo.setSuburl(mType);
				vo.setTitle(mTitle);
				vo.setComments(liveVo.getComments());
				vo.setLink(liveVo.getLink());// 数字报专用

			} else {
				vo.setContentid(communityvo.getTid());
				vo.setSuburl("1");
				vo.setTitle(communityvo.getShare_tit());
				vo.setCreatime(communityvo.getCreatime());
			}

			if ("".equals(mFavorStr)) {
				mFavorStr = JsonUtil.toJson(vo);
				mActivity.saveJsonCache("favor_" + vo.getContentid(), null, mFavorStr);
				mActivity.showCustomToast("收藏成功");
				mImg05.setImageResource(R.drawable.img_05_select);
			} else {
				mFavorStr = "";
				mActivity.deleteByKey("favor_" + vo.getContentid());
				mActivity.showCustomToast("取消收藏");
				mImg05.setImageResource(R.drawable.img_05_gray);
			}

			break;

		case R.id.img_00:
			if (mTtsClick) {
				mTtsClick = false;
				speech();
				mImg00.setImageResource(R.drawable.img_00_select);
			} else {
				tts.stopSpeaking();
				mTtsClick = true;
				mImg00.setImageResource(R.drawable.img_00_gray);
			}

			break;

		default:
			break;
		}
	}

	public void speech() {
		// 2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
		tts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");// 设置发音人
		tts.setParameter(SpeechConstant.SPEED, "50");// 设置语速
		tts.setParameter(SpeechConstant.VOLUME, "80");// 设置音量，范围0~100
		tts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); // 设置云端
		// 设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
		// 保存在SD卡需要在AndroidManifest.xml添加写SD卡权限
		// 如果不需要保存合成音频，注释该行代码
		// mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH,
		// "./sdcard/iflytek.pcm");
		// 3.开始合成

		// tts.startSpeaking(getSpeechStr(), mSynListener);
		// 防止语音无法播放
		LogUtil.e("getSpeechStr()","========"+getSpeechStr());
		mSpeakStr[0] = getSpeechStr().substring(0, getSpeechStr().length() / 2);
		mSpeakStr[1] = getSpeechStr().substring(getSpeechStr().length() / 2, getSpeechStr().length());
		tts.startSpeaking(mSpeakStr[0], mSynListener);
	}

	SynthesizerListener mSynListener = new SynthesizerListener() {
		// 会话结束回调接口，没有错误时，error为null
		public void onCompleted(SpeechError error) {
			mSpeakCount++;
			if (mSpeakCount > 1)
				return;
			tts.startSpeaking(mSpeakStr[1], mSynListener);
		}

		// 缓冲进度回调
		// percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息。
		public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
		}

		// 开始播放
		public void onSpeakBegin() {
		}

		// 暂停播放
		public void onSpeakPaused() {

		}

		// 播放进度回调
		// percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.
		public void onSpeakProgress(int percent, int beginPos, int endPos) {
		}

		// 恢复播放回调接口
		public void onSpeakResumed() {
		}

		// 会话事件回调接口
		public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
		}
	};

	public void commit(String url) {
		mCommitFlag = false;
		mActivity.showProgressDialog();
		RequestParams params = new RequestParams();
		try {
			params.put("uid", AppApplication.getInstance().getUid());
			params.put("sessionid", AppApplication.getInstance().getSessionid());
			// params.put("contentid", newsvo.getContentid());
			// params.put("type", newsvo.getSuburl());
			// params.put("title", newsvo.getTitle());
			params.put("contentid", mContentId);
			params.put("type", mType);
			params.put("title", mTitle);
			params.put("reply", mContent);
			params.put("pid", mPid);
			params.put("userid", mUserid);

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		String allUrl = Constant.DOMAIN_NAME + url;
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(allUrl, params, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				mCommitFlag = true;
				mActivity.dismissProgressDialog();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				String result = new String(arg2);
				mActivity.dismissProgressDialog();
				mCommitFlag = true;
				try {
					JSONObject jsonObject = new JSONObject(new String(arg2));
					String str = (String) jsonObject.get("retinfo");
					String code = (String) jsonObject.get("code");
					if (Constant.RESULT_SUCCESS_CODE.equals(code)) {
						mActivity.showCustomToast(str);
						new ThreadUtil(mActivity, mActivity).start();
					} else {
						mActivity.showCustomToast(str);
					}
				} catch (Exception e) {
				}

			}
		});
	}

	public void commitSecondDisscuss(NewsVO vo) {
		String hint = "回复#" + vo.getNickname() + "#：";
		mPid = vo.getPid();
		mUserid = vo.getUserid();
		mPopEditText.setSecondDisscuss(hint);
		mPopEditText.showPopupWindow(mCommentTv);

	}

	public NewsVO getNewsvo() {
		return newsvo;
	}

	public void setNewsvo(NewsVO newsvo) {
		this.newsvo = newsvo;
	}

	public String getFragmentName() {
		return TAG;// 不知道该方法有没有用
	}

	public SpeechSynthesizer getTts() {
		return tts;
	}

	public void setTts(SpeechSynthesizer tts) {
		this.tts = tts;
	}

	public String getSpeechStr() {
		return speechStr;
	}

	public void setSpeechStr(String speechStr) {
		this.speechStr = speechStr;
	}

	public String getIsRePlay() {
		return isRePlay;
	}

	public void setIsRePlay(String isRePlay) {
		this.isRePlay = isRePlay;
	}

	public CommunityVO getCommunityvo() {
		return communityvo;
	}

	public void setCommunityvo(CommunityVO communityvo) {
		this.communityvo = communityvo;
	}

	public LiveVO getLiveVo() {
		return liveVo;
	}

	public void setLiveVo(LiveVO liveVo) {
		this.liveVo = liveVo;
	}

}
