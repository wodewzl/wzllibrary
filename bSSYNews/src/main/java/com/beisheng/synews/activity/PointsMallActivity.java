package com.beisheng.synews.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.http.HttpClientUtil;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.view.BSGridView;
import com.beisheng.synews.adapter.PointsMallAdapter;
import com.beisheng.synews.application.AppApplication;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.mode.PointsMallVO;
import com.google.gson.Gson;
import com.im.zhsy.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PointsMallActivity extends BaseActivity implements OnClickListener {
	private BSGridView mGridView;
	private PointsMallVO mPointsMallVO;
	private PointsMallAdapter mAdapter;
	private TextView mNotifyTv;
	private int[] mColorArray = new int[] { R.color.life_fragment_color1, R.color.life_fragment_color2, R.color.life_fragment_color3 };
	private LinearLayout mRootLayout;

	@Override
	public void baseSetContentView() {
		View.inflate(this, R.layout.points_mall_activity, mBaseContentLayout);
	}

	@Override
	public boolean getDataResult() {
		return getData();
	}

	@Override
	public void initView() {
		mBaseTitleTv.setText("积分商城");
		mNotifyTv = (TextView) findViewById(R.id.notify_tv);
		mRootLayout = (LinearLayout) findViewById(R.id.root_layout);
		// mGridView = (BSGridView) findViewById(R.id.gridview);
		// mAdapter = new PointsMallAdapter(this);
		// mGridView.setAdapter(mAdapter);
	}

	@Override
	public void bindViewsListener() {
		mNotifyTv.setOnClickListener(this);
	}

	public boolean getData() {
		try {
			Gson gson = new Gson();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("uid", AppApplication.getInstance().getUid());
			if (hasNetWork()) {
				String jsonStr = HttpClientUtil.getRequest(this, Constant.DOMAIN_NAME + Constant.SHOP_URL, map);
				mPointsMallVO = gson.fromJson(jsonStr, PointsMallVO.class);
				saveJsonCache(Constant.SHOP_URL, map, jsonStr);

			} else {
				String oldStr = getCacheFromDatabase(Constant.SHOP_URL, map);
				mPointsMallVO = gson.fromJson(oldStr, PointsMallVO.class);
			}

			if (Constant.RESULT_SUCCESS_CODE.equals(mPointsMallVO.getCode())) {
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
		if (mPointsMallVO.getScore() != null) {
			BaseCommonUtils.setDifferentTextColorMiddle(mNotifyTv, "你有", mPointsMallVO.getScore(), "积分可兑换，详情请查看积分规则 >>", "#B10304");
			mNotifyTv.setVisibility(View.VISIBLE);
		} else {
			mNotifyTv.setVisibility(View.GONE);
		}
		// mAdapter.updateData(mPointsMallVO.getList());
		createGroup();
	}

	@Override
	public void executeFailure() {
		super.executeFailure();
		if (mPointsMallVO != null) {
			showCustomToast(mPointsMallVO.getRetinfo());
			mNoContentTv.setVisibility(View.VISIBLE);
			mBaseContentLayout.setVisibility(View.VISIBLE);
			mNoContentTv.setText(mPointsMallVO.getRetinfo());
		} else {
			showCustomToast("亲，请检查网络哦");
		}

	}

	@Override
	public void onClick(View arg0) {
		Intent brokeIntent = new Intent();
		brokeIntent.putExtra("url", Constant.DOMAIN_NAME + Constant.BROKE_NOTE_URL + "/id/70");
		brokeIntent.putExtra("name", "积分规则");
		brokeIntent.setClass(this, WebViewActivity.class);
		startActivity(brokeIntent);
	}

	@SuppressLint("ResourceAsColor")
	public void createGroup() {
		ArrayList<HashMap<String, Object>> item = new ArrayList<HashMap<String, Object>>();

		List<PointsMallVO> listVo = mPointsMallVO.getList();
		for (int i = 0; i < listVo.size(); i++) {
			LinearLayout titleLayout = new LinearLayout(this);
			titleLayout.setBackgroundColor(R.color.C7);
			LinearLayout.LayoutParams titleLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
			titleLayout.setLayoutParams(titleLayoutParams);

			// 添加左边的
			// ImageView leftImg = new ImageView(this);
			// LinearLayout.LayoutParams leftImgParams = new
			// LinearLayout.LayoutParams(BaseCommonUtils.dip2px(this, 5),
			// LinearLayout.LayoutParams.MATCH_PARENT);
			// leftImg.setLayoutParams(leftImgParams);
			// titleLayout.addView(leftImg);
			// leftImg.setBackgroundColor(this.getResources().getColor(mColorArray[i
			// % 3]));

			// 添加标题
			TextView tv = new TextView(this);
			LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
			tvParams.weight = 1;
			tv.setLayoutParams(tvParams);
			tv.setPadding(BaseCommonUtils.dip2px(this, 15), BaseCommonUtils.dip2px(this, 10), BaseCommonUtils.dip2px(this, 15), BaseCommonUtils.dip2px(this, 10));
			tv.setTextColor(this.getResources().getColor(R.color.C1));
			tv.setBackgroundColor(this.getResources().getColor(mColorArray[i % 3]));
			tv.setText(listVo.get(i).getRanges());
			tv.setGravity(Gravity.CENTER);
			titleLayout.addView(tv);

			// 添加上边的两个
			mRootLayout.addView(titleLayout);
			final BSGridView gv = new BSGridView(this);
			// LinearLayout.LayoutParams gvParams = new
			// LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
			// LinearLayout.LayoutParams.WRAP_CONTENT);
			// gv.setLayoutParams(gvParams);
			gv.setNumColumns(2);
			gv.setVerticalSpacing(BaseCommonUtils.dip2px(this, 1));
			gv.setHorizontalSpacing(BaseCommonUtils.dip2px(this, 1));
			// gv.setPadding(0, BaseCommonUtils.dip2px(this, 15), 0,
			// BaseCommonUtils.dip2px(this,
			// 15));
			mRootLayout.addView(gv);

			// 填出数据
			// MenuItmeAdapter adapter = new MenuItmeAdapter(this);
			final PointsMallAdapter adapter = new PointsMallAdapter(this);
			gv.setAdapter(adapter);
			adapter.updateData(listVo.get(i).getGoodslist());
			if (listVo.get(i).getGoodslist().size() > 4) {
				adapter.setAll(true);
			}

			// 添加标题
			final TextView moreTv = new TextView(this);
			LinearLayout.LayoutParams moreTvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
			moreTvParams.gravity = Gravity.CENTER;
			moreTv.setLayoutParams(moreTvParams);
			moreTv.setPadding(BaseCommonUtils.dip2px(this, 15), BaseCommonUtils.dip2px(this, 10), BaseCommonUtils.dip2px(this, 15), BaseCommonUtils.dip2px(this, 10));
			moreTv.setTextColor(this.getResources().getColor(R.color.C5));
			moreTv.setBackgroundColor(this.getResources().getColor(R.color.C3));
			moreTv.setText("加载更多");
			moreTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.expend, 0, 0, 0);
			moreTv.setCompoundDrawablePadding(BaseCommonUtils.dip2px(this, 5));
			mRootLayout.addView(moreTv);
			if (listVo.get(i).getGoodslist().size() > 4) {
				moreTv.setVisibility(View.VISIBLE);
			} else {
				moreTv.setVisibility(View.GONE);
			}

			// if (i > 1) {
			// gv.setVisibility(View.GONE);
			// moreTv.setVisibility(View.VISIBLE);
			// } else {
			// gv.setVisibility(View.VISIBLE);
			// }

			final int index = i;
			moreTv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {

					if ("加载更多".equals(moreTv.getText().toString())) {
						adapter.setAll(false);
						adapter.notifyDataSetChanged();
						moreTv.setText("收起");
						// gv.setVisibility(View.VISIBLE);
					} else {
						adapter.setAll(true);
						adapter.notifyDataSetChanged();
						moreTv.setText("加载更多");
						// if (index > 1) {
						// gv.setVisibility(View.GONE);
						// moreTv.setVisibility(View.VISIBLE);
						// } else {
						// gv.setVisibility(View.VISIBLE);
						// }
					}
				}
			});

		}
	}
}
