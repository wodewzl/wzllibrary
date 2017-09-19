package com.beisheng.synews.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.adapter.UploadAdapter;
import com.beisheng.base.http.HttpClientUtil;
import com.beisheng.base.imgselector.ImageActivityUtils;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.BitmapUtil;
import com.beisheng.base.utils.FileUtil;
import com.beisheng.base.utils.Options;
import com.beisheng.base.view.BSCircleImageView;
import com.beisheng.base.view.BSDialog;
import com.beisheng.synews.application.AppApplication;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.mode.UserInfoVO;
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

public class UserInfoActivity extends BaseActivity implements OnClickListener {
	private static final int TAKE_PICTURE = 0x000001;// 拍照返回码
	private static final int RESULT_LOAD_IMAGE = 0x000002;
	private static final int RESULT_CUT = 0x000003;// 裁剪后返回码
	private UserInfoVO mUserInfo;
	private TextView mTextView01, mTextView02, mTextView03, mTextView04;
	private LinearLayout mLayout01, mLayout02, mLayout03, mLayout04, mLayout05, mLayout06;
	private BSDialog mDialog;
	private boolean mCommitFlag = true;
	private String mName, mPhone, mAddress;
	private BSCircleImageView mHeadImg;
	private UploadAdapter mAdapter;
	private File mFile;

	@Override
	public void baseSetContentView() {
		View.inflate(this, R.layout.user_info_activity, mBaseContentLayout);
	}

	@Override
	public boolean getDataResult() {
		return getData();
	}

	@Override
	public void initView() {
		mBaseTitleTv.setText("个人信息");
		mLayout01 = (LinearLayout) findViewById(R.id.layout_01);
		mLayout02 = (LinearLayout) findViewById(R.id.layout_02);
		mLayout03 = (LinearLayout) findViewById(R.id.layout_03);
		mLayout04 = (LinearLayout) findViewById(R.id.layout_04);
		mLayout05 = (LinearLayout) findViewById(R.id.layout_05);
		mLayout06 = (LinearLayout) findViewById(R.id.layout_06);
		mTextView01 = (TextView) findViewById(R.id.tv_01);
		mTextView02 = (TextView) findViewById(R.id.tv_02);
		mTextView03 = (TextView) findViewById(R.id.tv_03);
		mTextView04 = (TextView) findViewById(R.id.tv_04);
		mHeadImg = (BSCircleImageView) findViewById(R.id.head_img);
		mAdapter = new UploadAdapter(this);

		mImageLoader.displayImage(AppApplication.getInstance().getUserInfoVO().getHeadpic(), mHeadImg, Options.getOptionsHead(R.drawable.user_icon));
	}

	@Override
	public void bindViewsListener() {
		mHeadImg.setOnClickListener(this);
		mLayout01.setOnClickListener(this);
		mLayout02.setOnClickListener(this);
		mLayout03.setOnClickListener(this);
		mLayout04.setOnClickListener(this);
		mLayout05.setOnClickListener(this);
		mLayout06.setOnClickListener(this);
	}

	public void initData() {
		UserInfoVO vo = AppApplication.getInstance().getUserInfoVO();
		if (vo != null) {
			if (vo.getNickname() != null)
				mTextView01.setText(vo.getNickname());
			else
				mTextView01.setText("未设置");
			if (vo.getNickname() != null)
				mTextView02.setText(vo.getPhone());
			else
				mTextView02.setText("未设置");
			mTextView04.setText(vo.getAddress());
		}

	}

	public boolean getData() {
		try {
			Gson gson = new Gson();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("uid", AppApplication.getInstance().getUid());
			map.put("sessionid", AppApplication.getInstance().getSessionid());
			if (hasNetWork()) {
				String jsonStr = HttpClientUtil.getRequest(this, Constant.DOMAIN_NAME + Constant.USER_INFO_GET, map);
				mUserInfo = gson.fromJson(jsonStr, UserInfoVO.class);
				saveJsonCache(Constant.MYSELF_MESSAGE_URL, map, jsonStr);

			} else {
				String oldStr = getCacheFromDatabase(Constant.MYSELF_MESSAGE_URL, map);
				mUserInfo = gson.fromJson(oldStr, UserInfoVO.class);
			}

			if (Constant.RESULT_SUCCESS_CODE.equals(mUserInfo.getCode())) {
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
		UserInfoVO vo = AppApplication.getInstance().getUserInfoVO();
		vo.setPhone(mUserInfo.getPhone());
		vo.setAddress(mUserInfo.getAddress());
		AppApplication.getInstance().saveUserInfoVO(vo);
		initData();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_01:
			bindEmail("请填写昵称", mTextView01.getText().toString(), 1);
			break;
		case R.id.layout_02:
			bindEmail("请填写手机", mTextView02.getText().toString(), 2);
			break;
		case R.id.layout_03:
			openActivity(UpdatePasswordActivity.class);
			break;
		case R.id.layout_04:
			bindEmail("请填写收货地址", mTextView04.getText().toString(), 4);
			break;
		case R.id.layout_05:
			openActivity(PointsRedeemActivity.class);
			break;
		case R.id.layout_06:
			File file = new File(FileUtil.getSaveFilePath(this) + "temp.jpg");
			if (file.exists())
				file.delete();
			if (mAdapter.mPicList.size() == 0) {
				ImageActivityUtils.setImageForActivity(this, mAdapter, 0);
			} else {
				ImageActivityUtils.setImageForActivity(this, mAdapter, 1);
			}
			break;

		default:
			break;
		}
	}

	@SuppressLint("NewApi")
	public void bindEmail(String hint, String text, final int type) {
		final EditText et = new EditText(this);
		et.setBackgroundResource(R.color.C1);
		int padding = BaseCommonUtils.dip2px(this, 15);
		et.setPadding(padding, padding, padding, padding);
		et.setHint(hint);
		et.setText(text);
		et.setHintTextColor(this.getResources().getColor(R.color.text_hint_color));
		et.setTextSize(14);
		et.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.devider_bg, R.color.C1));

		TextView view = new TextView(this);
		view.setHeight(BaseCommonUtils.dip2px(this, 10));
		view.setVisibility(View.INVISIBLE);

		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(et);

		int color = this.getResources().getColor(R.color.sy_title_color);
		mDialog = new BSDialog(UserInfoActivity.this, "请输入", layout, color, new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				switch (type) {
				case 1:
					mName = et.getText().toString();
					break;
				case 2:
					mPhone = et.getText().toString();
					break;
				case 4:
					mAddress = et.getText().toString();
					break;

				default:
					break;
				}
				mDialog.dismiss();
				commit();
			}
		});
		mDialog.show();
	}

	public void commit() {
		mCommitFlag = false;
		showProgressDialog();
		RequestParams params = new RequestParams();
		try {
			params.put("nickname", mName);
			params.put("phone", mPhone);
			params.put("address", mAddress);
			params.put("uid", AppApplication.getInstance().getUid());
			params.put("sessionid", AppApplication.getInstance().getSessionid());

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		String url = Constant.DOMAIN_NAME + Constant.USER_INFO_MODIFY;
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				mCommitFlag = true;
				dismissProgressDialog();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				String result = new String(arg2);
				dismissProgressDialog();
				mCommitFlag = true;
				try {
					JSONObject jsonObject = new JSONObject(new String(arg2));
					String str = (String) jsonObject.get("retinfo");
					String code = (String) jsonObject.get("code");
					if (Constant.RESULT_SUCCESS_CODE.equals(code)) {
						showCustomToast(str);
						UserInfoVO vo = AppApplication.getInstance().getUserInfoVO();
						if (mName != null) {
							vo.setNickname(mName);
							MessageMyHeadImp.getInstance().notifyWatcher(vo);
						}

						if (mPhone != null)
							vo.setPhone(mPhone);
						if (mAddress != null)
							vo.setAddress(mAddress);
						AppApplication.getInstance().saveUserInfoVO(vo);
						initData();
					} else {
						showCustomToast(str);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
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
			File file = new File(FileUtil.getSaveFilePath(this) + "temp.jpg");
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 1;
			Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), options);
			mHeadImg.setImageBitmap(bitmap);
			if (file.exists()) {
				mFile = BitmapUtil.bitmapToFile(FileUtil.getSaveFilePath(this) + "temp.jpg");
				commitHead();
				mAdapter.mList.clear();
				mAdapter.mPicList.clear();
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
		intent.putExtra("output", Uri.fromFile(new File(FileUtil.getSaveFilePath(this) + "temp.jpg")));
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
				dismissProgressDialog();
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
					showCustomToast(str);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
