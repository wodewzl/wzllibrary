/**
 * 
 */
package com.bs.bsims.activity;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.fragment.ExpensesColumnLineChartFragemnt;
import com.bs.bsims.fragment.ExpensesPieChartFragment;
import com.bs.bsims.model.BossStatisticsAttendanceVO;
import com.bs.bsims.model.BossStatisticsExpensesVo;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.utils.UrlUtil;
import com.bs.bsims.utils.CommonUtils.ResultCallback;
import com.bs.bsims.view.BSDialog;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * BS北盛最帅程序员
 * 
 * Copyright (c) 2016
 * 
 * 湖北北盛科技有限公司
 * 
 * @author 梁骚侠
 * 
 * @date 2016-2-14
 * 
 * @version 1.22 费用统计饼子图
 */
public class BossStatisticsExpensesPieChartActivity extends BaseActivity implements OnClickListener {

	/**
	 * talbe切换的view
	 **/
	private int mTableKey = 1;// table选项卡
	private TextView mOneTable, mTwoTable;
	private TextView mAllMoney;// 合计金额
	private TextView mMonthMoney;// 月均收入
	private Context mContext;
	private BossStatisticsExpensesVo mStatisticsExpensesVo, mVo;
	private BossStatisticsAttendanceVO mStatisticsAttendanVo, aVo;

	public ExpensesPieChartFragment chartFragment;
	public ExpensesColumnLineChartFragemnt lineFragment;

	private String datetime = "";
	private BSDialog mBSDialog;
	int year = 0;// calendar.get(Calendar.YEAR);
	int month = 0;// calendar.get(Calendar.MONTH)

	@Override
	public void baseSetContentView() {
		// TODO Auto-generated method stub
		View.inflate(this, R.layout.statistics_expenses_piechart_view, mContentLayout);
		mContext = this;
	}

	@Override
	public boolean getDataResult() {
		// TODO Auto-generated method stub
		return getDataOne();
	}

	@Override
	public void updateUi() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		mTitleTv.setText("费用统计");
		mOneTable = (TextView) findViewById(R.id.detailinfo);
		mTwoTable = (TextView) findViewById(R.id.trade_dongtai);
		mAllMoney = (TextView) findViewById(R.id.inout_tv);
		mMonthMoney = (TextView) findViewById(R.id.out_tv);
		datetime = DateUtils.getCurrentDate111122();
		mOkTv.setText(datetime);
		mOkTv.setPadding(5, 5, 5, 5);
	}

	@Override
	public void bindViewsListener() {
		// TODO Auto-generated method stub
		mOneTable.setOnClickListener(this);
		mTwoTable.setOnClickListener(this);
		mOkTv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.detailinfo:
			if (mTableKey == 1)
				return;// 如果是当前视图，不需要在对view操作了，消耗内存
			tableSelect(1);
			break;
		case R.id.trade_dongtai:
			if (mTableKey == 2)
				return;// 如果是当前视图，不需要在对view操作了，消耗内存
			tableSelect(2);
			break;

		case R.id.txt_comm_head_right:

			if (mBSDialog == null) {
				mBSDialog = CommonUtils.initDateViewCallback(this, "请选择时间", mOkTv, 3, callback);
			} else {
				mBSDialog.show();
			}

			break;

		}
	}

	ResultCallback callback = new ResultCallback() {

		@Override
		public void callback(String str, int position) {
			CustomDialog.showProgressDialog(mContext, "正在加载..");
			datetime = str;
			year = Integer.parseInt(datetime.split("-")[0]);
			month = Integer.parseInt(datetime.split("-")[1]);
			mOkTv.setText(str);
			new ThreadUtil(mContext, BossStatisticsExpensesPieChartActivity.this).start();
		}
	};

	/**
	 * 选项卡切换
	 **/
	public void tableSelect(int key) {
		mTableKey = key;
		switch (key) {
		case 1:
			/* 第二还原 */
			mTwoTable.setTextColor(getResources().getColor(R.color.C5));
			mTwoTable.setBackgroundResource(R.drawable.corners_tab_right_normal);
			/* 第一变色 */
			mOneTable.setTextColor(getResources().getColor(R.color.white));
			mOneTable.setBackgroundResource(R.drawable.corners_tab_left_select);
			break;
		case 2:
			/* 第二变色 */
			mTwoTable.setTextColor(getResources().getColor(R.color.white));
			mTwoTable.setBackgroundResource(R.drawable.corners_tab_right_select);
			/* 第一还原 */
			mOneTable.setTextColor(getResources().getColor(R.color.C5));
			mOneTable.setBackgroundResource(R.drawable.corners_tab_left_normal);
			break;
		}

		setSelect(mTableKey);

	}

	/**
	 * 费种分布数据带入 一起加载
	 * 
	 **/
	public boolean getDataOne() {
		Gson gson = new Gson();
		Map paramsMap = new HashMap<String, String>();
		paramsMap.put("date", datetime);
		String urlStr;
		String jsonUrlStr;
		String urlStr1;
		String jsonUrlStr1;
		try {
			urlStr = UrlUtil.getUrlByMap1(Constant.BOSS_STATISTICS_EXPENSE_PIECHART_TABLEONE, paramsMap);
			jsonUrlStr = HttpClientUtil.get(urlStr, Constant.ENCODING).trim();
			mStatisticsExpensesVo = gson.fromJson(jsonUrlStr, BossStatisticsExpensesVo.class);
			urlStr1 = UrlUtil.getUrlByMap1(Constant.BOSS_STATISTICS_EXPENSE_COUML_TABLETWO, paramsMap);
			jsonUrlStr1 = HttpClientUtil.get(urlStr1, Constant.ENCODING).trim();
			mStatisticsAttendanVo = gson.fromJson(jsonUrlStr1, BossStatisticsAttendanceVO.class);
			if (mStatisticsExpensesVo.getCode().equals("200") && mStatisticsAttendanVo.getCode().equals("200")) {
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 费种部门数据带入
	 * 
	 **/
	// public boolean getDataTwo() {
	// Gson gson = new Gson();
	// Map paramsMap = new HashMap<String, String>();
	// paramsMap.put("date", "2015-12");
	// String urlStr;
	// String jsonUrlStr;
	// try {
	// urlStr =
	// UrlUtil.getUrlByMap1(Constant.BOSS_STATISTICS_EXPENSE_COUML_TABLETWO,
	// paramsMap);
	// jsonUrlStr = HttpClientUtil.get(urlStr, Constant.ENCODING).trim();
	// mStatisticsExpensesVo = gson.fromJson(jsonUrlStr,
	// BossStatisticsExpensesVo.class);
	// if (mStatisticsExpensesVo.getCode().equals("200")) {
	// return true;
	// }
	// return false;
	// } catch (Exception e) {
	// e.printStackTrace();
	// return false;
	// }
	// }

	@Override
	public void executeSuccess() {
		// TODO Auto-generated method stub
		super.executeSuccess();
		if (null == mStatisticsExpensesVo || null == mStatisticsExpensesVo.getInfo()) {
			return;
		} else if (null == mStatisticsAttendanVo || null == mStatisticsAttendanVo.getInfo()) {
			return;
		}

		mVo = mStatisticsExpensesVo.getInfo();
		aVo = mStatisticsAttendanVo.getInfo();
		mAllMoney.setText(mVo.getTotal());
		mMonthMoney.setText(mVo.getAllnum());
		tableSelect(mTableKey);
		CustomDialog.closeProgressDialog();
	}

	@Override
	public void executeFailure() {
		// TODO Auto-generated method stub
		super.executeFailure();
		CustomDialog.closeProgressDialog();
	}

	private void hideFragment(FragmentTransaction transaction) {
		if (chartFragment != null) {
			transaction.hide(chartFragment);
		}
		if (lineFragment != null) {
			transaction.hide(lineFragment);
		}

	}

	public void setSelect(int i) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		hideFragment(transaction);
		// 把图片设置为亮的
		// 设置内容区域
		switch (i) {
		case 1:
			if (chartFragment == null) {
				chartFragment = new ExpensesPieChartFragment(mVo);
				transaction.add(R.id.id_content, chartFragment);
			} else {
				chartFragment.setmVo(mVo);
				transaction.show(chartFragment);
			}
			break;
		case 2:
			if (lineFragment == null) {
				lineFragment = new ExpensesColumnLineChartFragemnt(aVo);
				transaction.add(R.id.id_content, lineFragment);
			} else {
				lineFragment.setmVo(aVo);
				transaction.show(lineFragment);
			}
			break;

		default:
			break;

		}
		// 有时这行会报错
		try {
			transaction.commitAllowingStateLoss();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
