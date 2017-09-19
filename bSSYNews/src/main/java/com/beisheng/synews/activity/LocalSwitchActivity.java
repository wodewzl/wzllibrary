package com.beisheng.synews.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.http.HttpClientUtil;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.SharePreferenceUtil;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.mode.LocalVO;
import com.google.gson.Gson;
import com.im.zhsy.R;

public class LocalSwitchActivity extends BaseActivity {
	private LinearLayout mRootLayout;
	private LocalVO mLocalVO;

	@Override
	public void baseSetContentView() {
		View.inflate(this, R.layout.local_switchl_activity, mBaseContentLayout);
	}

	@Override
	public boolean getDataResult() {
		// return getData();
		return true;
	}

	@Override
	public void initView() {
		mBaseTitleTv.setText("县市");
		mRootLayout = (LinearLayout) this.findViewById(R.id.root_layout);
		LocalVO vo = (LocalVO) this.getIntent().getSerializableExtra("localvo");
		// initData(vo.getList());
		Gson gson = new Gson();
		HashMap<String, String> localMap = new HashMap<String, String>();
		String oldStr = getCacheFromDatabase(Constant.LOCATION_URL, localMap);
		LocalVO localVO = gson.fromJson(oldStr, LocalVO.class);
		if (localVO != null && Constant.RESULT_SUCCESS_CODE.equals(localVO.getCode()) && localVO.getList() != null) {
			List<LocalVO> list = new ArrayList<LocalVO>();
			LocalVO allVo = new LocalVO();
			allVo.setName("全部");
			list.add(allVo);
			list.addAll(localVO.getList());
			initData(list);
		}
	}

	@Override
	public void bindViewsListener() {

	}

	public boolean getData() {
		try {
			Gson gson = new Gson();
			HashMap<String, String> localMap = new HashMap<String, String>();
			if (hasNetWork()) {
				String jsonStr = HttpClientUtil.getRequest(this, Constant.DOMAIN_NAME + Constant.LOCATION_URL, localMap);
				mLocalVO = gson.fromJson(jsonStr, LocalVO.class);
				saveJsonCache(Constant.LOCATION_URL, localMap, jsonStr);
			} else {
				String oldStr = getCacheFromDatabase(Constant.LOCATION_URL, localMap);
				mLocalVO = gson.fromJson(oldStr, LocalVO.class);
			}
			if (Constant.RESULT_SUCCESS_CODE.equals(mLocalVO.getCode())) {
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
		dismissProgressDialog();
		// 手动添加全部

	}

	@Override
	public void executeFailure() {
		super.executeFailure();
		dismissProgressDialog();
		showCustomToast(mLocalVO.getRetinfo());
	}

	@SuppressLint("NewApi")
	public void initData(List<LocalVO> localList) {
		List<LocalVO> list = new ArrayList<LocalVO>();
		list.addAll(localList);
		LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		LinearLayout rowLayout = new LinearLayout(this);
		rowLayout.setOrientation(LinearLayout.HORIZONTAL);
		float screenWidth = BaseCommonUtils.getScreenWidth(this) - BaseCommonUtils.dip2px(this, 15);
		float height = BaseCommonUtils.getScreenHigh(this);
		float currentWidth = 0;
		float everyWidth = 0;
		LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		for (int i = 0; i < localList.size(); i++) {
			final LocalVO vo = localList.get(i);
			textViewParams.setMargins(0, 0, BaseCommonUtils.dip2px(this, 15), BaseCommonUtils.dip2px(this, 15));
			final TextView childTv = new TextView(this);
			childTv.setPadding(BaseCommonUtils.dip2px(this, 20), BaseCommonUtils.dip2px(this, 8), BaseCommonUtils.dip2px(this, 20), BaseCommonUtils.dip2px(this, 8));
			childTv.setLayoutParams(textViewParams);
			childTv.setText(vo.getName());
			childTv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					childTv.setBackground(BaseCommonUtils.setBackgroundShap(LocalSwitchActivity.this, 20, "#B10304", "#B10304"));
					Intent intent = new Intent();
					childTv.setTextColor(Color.parseColor("#ffffff"));
					intent.putExtra("id", vo.getId());
					intent.putExtra("name", vo.getName());
					SharePreferenceUtil.putSharedpreferences(LocalSwitchActivity.this, "local_name", "local_name", vo.getName());
					LocalSwitchActivity.this.setResult(2, intent);
					LocalSwitchActivity.this.finish();
					// MessageMyHeadImp.getInstance().notifyWatcher(vo);
				}
			});
			childTv.setBackground(BaseCommonUtils.setBackgroundShap(this, 20, "#DDDDDD", "#F8F8F8"));
			childTv.setTextColor(Color.parseColor("#818181"));
			Paint paint = childTv.getPaint();
			float textViewWidth = BaseCommonUtils.getViewWidth(childTv);
			everyWidth = textViewWidth + BaseCommonUtils.dip2px(this, 15);
			if (screenWidth - currentWidth < everyWidth) {
				mRootLayout.addView(rowLayout);
				initData(list);
				break;
			} else {
				list.remove(localList.get(i));
			}
			currentWidth = everyWidth + currentWidth;
			rowLayout.addView(childTv);
			if (localList.size() - 1 == i) {
				mRootLayout.addView(rowLayout);
				break;
			}
		}
	}

}
