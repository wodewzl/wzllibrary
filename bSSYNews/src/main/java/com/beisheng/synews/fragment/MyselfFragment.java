package com.beisheng.synews.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.adapter.UploadAdapter;
import com.beisheng.base.fragment.BaseFragment;
import com.beisheng.base.http.HttpClientUtil;
import com.beisheng.base.imgselector.ImageActivityUtils;
import com.beisheng.base.interfaces.UpdateCallback;
import com.beisheng.base.service.DownloadService;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.BitmapUtil;
import com.beisheng.base.utils.DataCleanUtil;
import com.beisheng.base.utils.DialogUtil;
import com.beisheng.base.utils.DialogUtil.DialogCallback;
import com.beisheng.base.utils.FileUtil;
import com.beisheng.base.utils.Options;
import com.beisheng.base.utils.SharePreferenceUtil;
import com.beisheng.base.utils.ThreadUtil;
import com.beisheng.base.utils.VersionUtils;
import com.beisheng.base.view.BSCircleImageView;
import com.beisheng.base.view.BSDialog;
import com.beisheng.synews.activity.BrokeActivity;
import com.beisheng.synews.activity.CacheListActivity;
import com.beisheng.synews.activity.DisscussMyselfActivity;
import com.beisheng.synews.activity.FeedbackActivity;
import com.beisheng.synews.activity.InvitatCodeActivity;
import com.beisheng.synews.activity.LoginActivity;
import com.beisheng.synews.activity.MessageActivity;
import com.beisheng.synews.activity.PointsMallActivity;
import com.beisheng.synews.activity.PointsRecordActivity;
import com.beisheng.synews.activity.SettingActivity;
import com.beisheng.synews.activity.UserInfoActivity;
import com.beisheng.synews.activity.WebViewActivity;
import com.beisheng.synews.application.AppApplication;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.mode.MySelfVO;
import com.beisheng.synews.mode.UpdateVersionVO;
import com.beisheng.synews.mode.UserInfoVO;
import com.beisheng.synews.view.BSPopwindowEditText;
import com.beisheng.synews.view.BSPopwindowEditText.CommitTwoCallback;
import com.bs.bsims.observer.BSApplictionObserver.Watcher;
import com.bs.bsims.observer.MessageMyHeadImp;
import com.google.gson.Gson;
import com.im.zhsy.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

public class MyselfFragment extends BaseFragment implements OnClickListener, UpdateCallback, Watcher {
	private String TAG = "LiveFragment";
	private static final int TAKE_PICTURE = 0x000001;// 拍照返回码
	private static final int RESULT_LOAD_IMAGE = 0x000002;
	private static final int RESULT_CUT = 0x000003;// 裁剪后返回码
	private BaseActivity mActivity;
	private LinearLayout mLayout01, mLayout02, mLayout03, mLayout04, mLayout05, mLayout06, mLayout07, mLayout08, mLayout09, mLayout10, mLayout11;
	private BSPopwindowEditText mPopEditText;
	private TextView mPointsTv, mNameTv, mHistoryTv, mCollectTv, mBrokeTv, mMessageTv;
	private boolean mCommitFlag = true;
	private BSDialog mDialog;
	private BSCircleImageView mHeadImg;
	private MySelfVO mMySelfVO;
	private TextView mLayout01Count, mLayout02Count, mLayout03Size;
	private String[] mFontSize = { "16,小", "18,中", "20,大", "22,特大" };
	private UpdateVersionVO mUpdateVersionVO;
	private String mState = "1";// 1为我的界面数据请求 2为检查更新
	private File mFile;
	private UploadAdapter mAdapter;
	private Button mOutBt;
	private String mLogout;

	public static MyselfFragment newInstance() {
		MyselfFragment myselftFragment = new MyselfFragment();
		return myselftFragment;
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
		View view = inflater.inflate(R.layout.myself_fragment, container, false);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initViews(view);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		new ThreadUtil(mActivity, this).start();

	}

	@SuppressLint("NewApi")
	private void initViews(View view) {
		mActivity.mBaseOkTv.setVisibility(View.GONE);
		mActivity.mBaseHeadLayout.setVisibility(View.GONE);
		mNameTv = (TextView) view.findViewById(R.id.name_tv);
		if (AppApplication.getInstance().getUserInfoVO() != null) {
			mNameTv.setText(AppApplication.getInstance().getUserInfoVO().getNickname());
		}

		mHeadImg = (BSCircleImageView) view.findViewById(R.id.head_img);
		mPointsTv = (TextView) view.findViewById(R.id.points_tv);
		mHistoryTv = (TextView) view.findViewById(R.id.history_tv);
		mCollectTv = (TextView) view.findViewById(R.id.collect_tv);
		mBrokeTv = (TextView) view.findViewById(R.id.broke_tv);
		mMessageTv = (TextView) view.findViewById(R.id.message_tv);
		mLayout01 = (LinearLayout) view.findViewById(R.id.layout_01);
		mLayout01Count = (TextView) view.findViewById(R.id.layout_01_count);
		mLayout02 = (LinearLayout) view.findViewById(R.id.layout_02);
		mLayout02Count = (TextView) view.findViewById(R.id.layout_02_count);
		mLayout03 = (LinearLayout) view.findViewById(R.id.layout_03);
		mLayout03Size = (TextView) view.findViewById(R.id.layout_03_size);
		mLayout04 = (LinearLayout) view.findViewById(R.id.layout_04);
		mLayout05 = (LinearLayout) view.findViewById(R.id.layout_05);
		mLayout06 = (LinearLayout) view.findViewById(R.id.layout_06);
		mLayout07 = (LinearLayout) view.findViewById(R.id.layout_07);
		mLayout08 = (LinearLayout) view.findViewById(R.id.layout_08);
		mLayout09 = (LinearLayout) view.findViewById(R.id.layout_09);
		mLayout10 = (LinearLayout) view.findViewById(R.id.layout_10);
		mLayout11 = (LinearLayout) view.findViewById(R.id.layout_11);

		mOutBt = (Button) view.findViewById(R.id.out_bt);
		mOutBt.setBackground(BaseCommonUtils.setBackgroundShap(mActivity, 5, R.color.sy_title_color, R.color.sy_title_color));
		if (AppApplication.getInstance().getUserInfoVO() == null) {
			mOutBt.setVisibility(View.GONE);
		} else {
			mOutBt.setVisibility(View.VISIBLE);

		}
		mPopEditText = new BSPopwindowEditText(mActivity, mCallback, 1);
		mAdapter = new UploadAdapter(mActivity);

		bindViewsListener();
	}

	public void bindViewsListener() {
		mHeadImg.setOnClickListener(this);
		mHistoryTv.setOnClickListener(this);
		mPointsTv.setOnClickListener(this);
		mCollectTv.setOnClickListener(this);
		mBrokeTv.setOnClickListener(this);
		mMessageTv.setOnClickListener(this);
		mLayout01.setOnClickListener(this);
		mLayout02.setOnClickListener(this);
		mLayout03.setOnClickListener(this);
		mLayout04.setOnClickListener(this);
		mLayout05.setOnClickListener(this);
		mLayout06.setOnClickListener(this);
		mLayout07.setOnClickListener(this);
		mLayout08.setOnClickListener(this);
		mLayout09.setOnClickListener(this);
		mLayout10.setOnClickListener(this);
		mLayout11.setOnClickListener(this);
		mOutBt.setOnClickListener(this);
		MessageMyHeadImp.getInstance().add(this);
	}

	CommitTwoCallback mCallback = new CommitTwoCallback() {

		@Override
		public void commtiCallback(String content, String contanct) {
			commit(content, contanct);
		}
	};

	DialogCallback dialogCallback = new DialogCallback() {

		@Override
		public void callback(String str, int position) {
			mLayout03Size.setText(mFontSize[position].split(",")[1]);
			// BaseAppApplication.getInstance().setFontSize(str);
			SharePreferenceUtil.putSharedpreferences(mActivity, "webview_font_size", "size", str);

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
		Bundle bundle = new Bundle();
		switch (v.getId()) {
		case R.id.head_img:
			if (AppApplication.getInstance().getUserInfoVO() == null) {
				mActivity.openActivity(LoginActivity.class);
			} else {
				// File file = new File(FileUtil.getSaveFilePath(mActivity)
				// + "temp.jpg");
				// if (file.exists())
				// file.delete();
				// if (mAdapter.mPicList.size() == 0) {
				// ImageActivityUtils.setImageForActivity(mActivity, mAdapter,
				// 0);
				// } else {
				// ImageActivityUtils.setImageForActivity(mActivity, mAdapter,
				// 1);
				// }

				mActivity.openActivity(UserInfoActivity.class);
			}
			break;

		case R.id.history_tv:
			bundle.putString("type", "1");
			mActivity.openActivity(CacheListActivity.class, bundle, 0);
			break;
		case R.id.collect_tv:
			bundle.putString("type", "2");
			mActivity.openActivity(CacheListActivity.class, bundle, 0);
			break;

		case R.id.broke_tv:
			((BaseActivity) mActivity).openActivity(BrokeActivity.class);
			break;
		case R.id.points_tv:
			mActivity.openActivity(PointsRecordActivity.class);
			break;
		case R.id.message_tv:
			mActivity.openActivity(MessageActivity.class);
			break;
		case R.id.layout_01:
			mActivity.openActivity(DisscussMyselfActivity.class);
			break;
		case R.id.layout_02:
			mActivity.openActivity(PointsMallActivity.class);
			break;
		case R.id.layout_03:
			DialogUtil.initSimpleListDialog(mActivity, "请选择字号", mFontSize, R.color.sy_title_color, dialogCallback);
			break;
		case R.id.layout_04:
			cleanCache();
			break;
		case R.id.layout_05:
			// mPopEditText.showPopupWindow(v);
			mActivity.openActivity(FeedbackActivity.class);
			break;
		case R.id.layout_06:
			bundle.putString("url", Constant.DOMAIN_NAME + Constant.COPYRIGHT_STATEMENT_URL);
			bundle.putString("name", "版权声明");
			((BaseActivity) mActivity).openActivity(WebViewActivity.class, bundle, 0);
			break;
		case R.id.layout_07:
			bundle.putString("url", Constant.DOMAIN_NAME + Constant.ABOUT_US_URL);
			bundle.putString("name", "关于我们");
			((BaseActivity) mActivity).openActivity(WebViewActivity.class, bundle, 0);
			break;
		case R.id.layout_08:
			mState = "2";
			new ThreadUtil(mActivity, this).start();
			break;
		case R.id.layout_09:

			mActivity.openActivity(SettingActivity.class);
			break;

		case R.id.layout_10:
			mActivity.openActivity(InvitatCodeActivity.class);
			break;
		case R.id.layout_11:
			if (AppApplication.getInstance().getUserInfoVO() == null) {
				mActivity.openActivity(LoginActivity.class);
			} else {
				mActivity.openActivity(UserInfoActivity.class);
			}
			break;
		case R.id.out_bt:
			mLogout = "1";
			bundle.putString("logout", mLogout);
			mActivity.openActivity(LoginActivity.class, bundle, 0);
			mLayout01Count.setText("");
			mLayout02Count.setText("");
			mNameTv.setText("未登录");
			mHeadImg.setImageResource(R.drawable.user_icon);
			AppApplication.getInstance().saveUserInfoVO(null);
			MessageMyHeadImp.getInstance().notifyWatcher(null);
			mOutBt.setVisibility(View.GONE);
			break;

		default:
			break;
		}
	}

	public void commit(String content, String contanct) {
		mCommitFlag = false;
		mActivity.showProgressDialog();
		RequestParams params = new RequestParams();
		try {
			params.put("email", contanct);
			params.put("content", content);
			params.put("uid", AppApplication.getInstance().getUid());
			params.put("sessionid", AppApplication.getInstance().getSessionid());
			params.put("phone", android.os.Build.MODEL);
			TelephonyManager TelephonyMgr = (TelephonyManager) mActivity.getSystemService(Context.TELEPHONY_SERVICE);
			String imei = TelephonyMgr.getDeviceId();
			params.put("mobile", imei);
			params.put("platform", "android");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		String url = Constant.DOMAIN_NAME + Constant.IDEA_FEEDBACK_URL;
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
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
					mActivity.showCustomToast(str);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void cleanCache() {
		final DataCleanUtil dataClean = new DataCleanUtil(mActivity);
		View v = LayoutInflater.from(mActivity).inflate(R.layout.pop_bottom_item, null);
		final TextView textView = (TextView) v.findViewById(R.id.textview);
		textView.setText("您确定清除缓存吗？");
		int color = mActivity.getResources().getColor(R.color.sy_title_color);
		mDialog = new BSDialog(mActivity, "缓存清除", v, color, new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dataClean.cleanApplicationData(mActivity, FileUtil.getSaveFilePath(mActivity));
				mActivity.showCustomToast("当前清除" + dataClean.getFileSize() + "文件缓存");
				mDialog.dismiss();
			}
		});
		mDialog.show();
	}

	@Override
	public boolean execute() {
		if ("1".equals(mState)) {
			return getData();
		} else {
			return checkUpdate();
		}

	}

	@Override
	public void executeSuccess() {
		if ("1".equals(mState)) {
			mLayout01Count.setText(mMySelfVO.getReplyNum());
			mLayout02Count.setText("积分 " + mMySelfVO.getScoreNum());
			if (AppApplication.getInstance().getUserInfoVO() != null)
				mActivity.mImageLoader.displayImage(AppApplication.getInstance().getUserInfoVO().getHeadpic(), mHeadImg, Options.getOptionsHead(R.drawable.user_icon));

		} else {
			if (mUpdateVersionVO == null)
				return;
			if (BaseCommonUtils.parseInt(mUpdateVersionVO.getVersion().getAndroid().getBuild()) > VersionUtils.getversionCode(mActivity)) {
				updateDialog();
			} else {
				// mActivity.showCustomToast("亲，已是最新版本哦");
				Toast.makeText(mActivity, "亲，已是最新版本哦", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void executeFailure() {
		mLayout01Count.setText("");
		mLayout02Count.setText("");
		mNameTv.setText("点击登录");
		mHeadImg.setImageResource(R.drawable.user_icon);

	}

	public boolean getData() {
		try {
			Gson gson = new Gson();
			HashMap<String, String> map = new HashMap<String, String>();
			if (AppApplication.getInstance().getUserInfoVO() != null) {
				map.put("uid", AppApplication.getInstance().getUserInfoVO().getUid());
			}

			if (mActivity.hasNetWork()) {
				String jsonStr = HttpClientUtil.getRequest(mActivity, Constant.DOMAIN_NAME + Constant.MYSELF_URL, map);
				mMySelfVO = gson.fromJson(jsonStr, MySelfVO.class);
				mActivity.saveJsonCache(Constant.MYSELF_URL, map, jsonStr);
			} else {
				String oldStr = mActivity.getCacheFromDatabase(Constant.MYSELF_URL, map);
				mMySelfVO = gson.fromJson(oldStr, MySelfVO.class);
			}
			if (Constant.RESULT_SUCCESS_CODE.equals(mMySelfVO.getCode())) {
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public String getFragmentName() {
		return TAG;// 不知道该方法有没有用
	}

	public boolean checkUpdate() {
		try {
			Gson gson = new Gson();
			HashMap<String, String> map = new HashMap<String, String>();
			String jsonStr = HttpClientUtil.getRequest(mActivity, Constant.CHECK_UPDATE_URL, map);
			mUpdateVersionVO = gson.fromJson(jsonStr, UpdateVersionVO.class);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
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
	public void updateNotify(Object content) {
		if (content == null)
			return;
		if (content instanceof UserInfoVO) {
			UserInfoVO vo = (UserInfoVO) content;
			mNameTv.setText(vo.getNickname());
			mActivity.mImageLoader.displayImage(vo.getHeadpic(), mHeadImg, Options.getOptionsHead(R.drawable.user_icon));
			mOutBt.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		MessageMyHeadImp.getInstance().remove(this);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case ImageActivityUtils.REQUEST_IMAGE:
			if (data != null) {
				ImageActivityUtils.setImageGetActivity(data, mAdapter);
				File file = new File(mAdapter.mPicList.get(0));
				startPhotoZoom(file);
			}
			break;

		case RESULT_CUT:
			File file = new File(FileUtil.getSaveFilePath(mActivity) + "temp.jpg");
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 1;
			Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), options);
			mHeadImg.setImageBitmap(bitmap);
			if (file.exists()) {
				mFile = BitmapUtil.bitmapToFile(FileUtil.getSaveFilePath(mActivity) + "temp.jpg");
				commitHead();
			} else {
				mAdapter.mList.clear();
				mAdapter.mPicList.clear();
			}

			break;
		default:
			break;
		}
	}

	public void startPhotoZoom(File file) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(Uri.fromFile(file), "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		intent.putExtra("output", Uri.fromFile(new File(FileUtil.getSaveFilePath(mActivity) + "temp.jpg")));
		intent.putExtra("return-data", true);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		intent.putExtra("scale", true);// 黑边
		intent.putExtra("scaleUpIfNeeded", true);// 黑边
		startActivityForResult(intent, RESULT_CUT);
	}

	public void commitHead() {
		mCommitFlag = false;
		RequestParams params = new RequestParams();
		try {
			params.put("img", mFile);
			params.put("uid", AppApplication.getInstance().getUid());
			params.put("sessionid", AppApplication.getInstance().getSessionid());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		String url = Constant.DOMAIN_NAME + Constant.UPLAOD_HEAD_ICON_URL;
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				mCommitFlag = true;
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
					String headpic = (String) jsonObject.get("headpic");
					if (Constant.RESULT_SUCCESS_CODE.equals(code)) {
						UserInfoVO vo = AppApplication.getInstance().getUserInfoVO();
						vo.setHeadpic(headpic);
						AppApplication.getInstance().saveUserInfoVO(vo);
					}
					mActivity.showCustomToast(str);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (!hidden) {
			mActivity.mBaseOkTv.setVisibility(View.GONE);
			mActivity.mBaseHeadLayout.setVisibility(View.GONE);
			mState = "1";
			new ThreadUtil(mActivity, this).start();
		}
	}
}
