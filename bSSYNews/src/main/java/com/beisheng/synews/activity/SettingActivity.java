package com.beisheng.synews.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.http.HttpClientUtil;
import com.beisheng.base.service.DownloadService;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.DataCleanUtil;
import com.beisheng.base.utils.DialogUtil;
import com.beisheng.base.utils.DialogUtil.DialogCallback;
import com.beisheng.base.utils.FileUtil;
import com.beisheng.base.utils.SharePreferenceUtil;
import com.beisheng.base.utils.ThreadUtil;
import com.beisheng.base.utils.VersionUtils;
import com.beisheng.base.view.BSDialog;
import com.beisheng.synews.application.AppApplication;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.mode.ChannelManage;
import com.beisheng.synews.mode.UpdateVersionVO;
import com.bs.bsims.observer.MessageMyHeadImp;
import com.google.gson.Gson;
import com.im.zhsy.R;

import java.util.HashMap;

public class SettingActivity extends BaseActivity implements OnClickListener {
	private LinearLayout mLayout03, mLayout04, mLayout06, mLayout07, mLayout08;
	private BSDialog mDialog;
	private String[] mFontSize = { "16,小", "18,中", "20,大", "22,特大" };
	private UpdateVersionVO mUpdateVersionVO;
	private String mState = "1";// 1为我的界面数据请求 2为检查更新
	private TextView mLayout03Size;
	private Button mOutBt;
	private String mLogout;

	@Override
	public void baseSetContentView() {
		View.inflate(this, R.layout.setting_activity, mBaseContentLayout);
	}

	@Override
	public boolean getDataResult() {
		if ("1".equals(mState)) {
			return true;
		} else {
			return checkUpdate();
		}

	}

	@SuppressLint("NewApi")
	@Override
	public void initView() {
		mBaseTitleTv.setText("设置");
		mLayout03 = (LinearLayout) findViewById(R.id.layout_03);
		mLayout03Size = (TextView) findViewById(R.id.layout_03_size);
		mLayout04 = (LinearLayout) findViewById(R.id.layout_04);
		mLayout06 = (LinearLayout) findViewById(R.id.layout_06);
		mLayout07 = (LinearLayout) findViewById(R.id.layout_07);
		mLayout08 = (LinearLayout) findViewById(R.id.layout_08);
		mOutBt = (Button) findViewById(R.id.out_bt);
		// mOutBt.setBackground(BaseCommonUtils.setBackgroundShap(this, 5,
		// R.color.sy_title_color, R.color.sy_title_color));
		// if (AppApplication.getInstance().getUserInfoVO() == null) {
		// mOutBt.setVisibility(View.GONE);
		// } else {
		// mOutBt.setVisibility(View.VISIBLE);
		//
		// }

	}

	@Override
	public void bindViewsListener() {
		mLayout03.setOnClickListener(this);
		mLayout04.setOnClickListener(this);
		mLayout06.setOnClickListener(this);
		mLayout07.setOnClickListener(this);
		mLayout08.setOnClickListener(this);
		mOutBt.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Bundle bundle = new Bundle();
		switch (v.getId()) {
		case R.id.layout_03:
			DialogUtil.initSimpleListDialog(this, "请选择字号", mFontSize, R.color.sy_title_color, dialogCallback);
			break;
		case R.id.layout_04:
			cleanCache();
			break;
		case R.id.layout_06:
			bundle.putString("url", Constant.DOMAIN_NAME + Constant.COPYRIGHT_STATEMENT_URL);
			bundle.putString("name", "版权声明");
			((BaseActivity) this).openActivity(WebViewActivity.class, bundle, 0);
			break;
		case R.id.layout_07:
			bundle.putString("url", Constant.DOMAIN_NAME + Constant.ABOUT_US_URL);
			bundle.putString("name", "关于我们");
			((BaseActivity) this).openActivity(WebViewActivity.class, bundle, 0);
			break;
		case R.id.layout_08:
			mState = "2";
			new ThreadUtil(this, this).start();
			break;

		case R.id.out_bt:
			mLogout = "1";
			// bundle.putString("logout", mLogout);
			openActivity(LoginActivity.class, bundle, 0);
			// mLayout01Count.setText("");
			// mLayout02Count.setText("");
			// mNameTv.setText("未登录");
			// mHeadImg.setImageResource(R.drawable.user_icon);
			AppApplication.getInstance().saveUserInfoVO(null);
			MessageMyHeadImp.getInstance().notifyWatcher(null);
			mOutBt.setVisibility(View.GONE);
			break;
		default:
			break;
		}

	}

	public void cleanCache() {
		final DataCleanUtil dataClean = new DataCleanUtil(this);
		View v = LayoutInflater.from(this).inflate(R.layout.pop_bottom_item, null);
		final TextView textView = (TextView) v.findViewById(R.id.textview);
		textView.setText("您确定清除缓存吗？");
		int color = this.getResources().getColor(R.color.sy_title_color);
		mDialog = new BSDialog(this, "缓存清除", v, color, new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dataClean.cleanApplicationData(SettingActivity.this, FileUtil.getSaveFilePath(SettingActivity.this));
				showCustomToast("当前清除" + dataClean.getFileSize() + "文件缓存");

				if (AppApplication.getInstance().getUserInfoVO() == null) {
					ChannelManage.getManage().saveUserChannel(null);
				}
				mDialog.dismiss();
			}
		});
		mDialog.show();
	}

	DialogCallback dialogCallback = new DialogCallback() {

		@Override
		public void callback(String str, int position) {
			mLayout03Size.setText(mFontSize[position].split(",")[1]);
			// BaseAppApplication.getInstance().setFontSize(str);
			SharePreferenceUtil.putSharedpreferences(SettingActivity.this, "webview_font_size", "size", str);

		}
	};

	public void updateDialog() {
		View v = LayoutInflater.from(this).inflate(R.layout.dialog_textview, null);
		final TextView textView = (TextView) v.findViewById(R.id.textview);
		textView.setText(mUpdateVersionVO.getVersion().getAndroid().getMessage());
		int color = this.getResources().getColor(R.color.sy_title_color);
		mDialog = new BSDialog(this, "版本更新", v, color, new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent updateIntent = new Intent(SettingActivity.this, DownloadService.class);
				updateIntent.putExtra("url", mUpdateVersionVO.getVersion().getAndroid().getUrl());
				updateIntent.putExtra("drawableId", R.drawable.ic_launcher);
				SettingActivity.this.startService(updateIntent);
				mDialog.dismiss();
			}
		});
		mDialog.show();
	}

	public boolean checkUpdate() {
		try {
			Gson gson = new Gson();
			HashMap<String, String> map = new HashMap<String, String>();
			String jsonStr = HttpClientUtil.getRequest(this, Constant.CHECK_UPDATE_URL, map);
			mUpdateVersionVO = gson.fromJson(jsonStr, UpdateVersionVO.class);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void executeSuccess() {
		super.executeSuccess();
		if (mUpdateVersionVO == null)
			return;
		if (BaseCommonUtils.parseInt(mUpdateVersionVO.getVersion().getAndroid().getBuild()) > VersionUtils.getversionCode(this)) {
			updateDialog();
		} else {
			// mActivity.showCustomToast("亲，已是最新版本哦");
			Toast.makeText(this, "亲，已是最新版本哦", Toast.LENGTH_SHORT).show();
		}
	}
}
