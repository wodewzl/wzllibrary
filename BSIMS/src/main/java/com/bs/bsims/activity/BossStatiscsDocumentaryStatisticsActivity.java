/**
 * 
 */
package com.bs.bsims.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.CrmStatisticsVisitorCustomersAdapter;
import com.bs.bsims.adapter.CrmStatisticsVisitorUsersAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.CrmOptionsVO;
import com.bs.bsims.model.CrmStatisticsVisitorVO;
import com.bs.bsims.model.TreeVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.utils.CommonUtils.ResultCallback;
import com.bs.bsims.view.BSListScrollView;
import com.bs.bsims.view.BSListView;
import com.bs.bsims.view.BSPopupWindowsTitle;
import com.bs.bsims.view.BSPopupWindowsTitle.TreeCallBack;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;

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
 * @date 2016-1-21
 * 
 * @version 1.22
 * 
 * @content :跟单统计
 */
@SuppressLint("NewApi")
public class BossStatiscsDocumentaryStatisticsActivity extends BaseActivity implements OnTouchListener, OnClickListener, OnScrollListener, OnItemClickListener {
	private CrmStatisticsVisitorVO mVisitorVO, mVisitorVO1;
	private BSListScrollView mUsersLv;
	private BSListView mCustomersLv;
	private CrmStatisticsVisitorUsersAdapter mUsersAdapter;
	private CrmStatisticsVisitorCustomersAdapter mCustomersAdapter;
	private ImageView mPositionIv;
	private int downY;
	private float mItemHight;// 每条的高度
	private float mDrableHight;// 游标的高度
	private float mCurrentY;// 当前游标停留的Y值
	private boolean mMoveState = true;// true 为下滑的，false为上滑动
	private int mFirstVisiblePosition;
	private int mClickPosition;
	private int mItemPosition;
	private TextView mAllCountTv, mDayCountTv;
	private LinearLayout mViewly;
	private LinearLayout mMoreTitle, mOneTitle, mTwoTitle;
	private TextView mMoreTitleTv, mOneTitleTv, mTwoTitleTv;
	private BSPopupWindowsTitle mBsPopupWindowsTitleOther, mBsPopupWindowsTitleDep;
	  private PopupWindow mBsPopupWindowsTitleTime;// 时间筛选弹出框
	private int selectOne = 0;
	private String mDid = "0";// 0为默认全部部门
	private String mDateType = "";// 1：近半年，2：近三个月，3：上半年，4：下半年
	private String mType = "";// 跟单类型
	private String[] mTimeArray = { "近半年", "上半年", "下半年" };
	private String[] mVisitorArray = { "全部类型", "上门拜访", "电话拜访", "在线沟通", "其他方式" };
	private Context mContext;

	@Override
	public void baseSetContentView() {
		View.inflate(this, R.layout.bossstatistics_doucument_statics, mContentLayout);
		mContext = this;
	}

	@Override
	public boolean getDataResult() {
		return getData();
	}

	@Override
	public void updateUi() {
	}

	@Override
	public void executeSuccess() {
		CustomDialog.closeProgressDialog();
		if (mVisitorVO == null) {
			return;
		}
		super.executeSuccess();
		mVisitorVO1 = mVisitorVO.getInfo();
		// CommonUtils.setTextTwoBefore(mContext, mAllCountTv,
		// mVisitorVO1.getTotalCount(), "次", R.color.leavl_title, 1.5f);
		mAllCountTv.setText(mVisitorVO1.getTotalCount());
		mDayCountTv.setText(mVisitorVO1.getVisitPerDay());
		if (null != mVisitorVO1.getArray()) {
		    mPositionIv.setTranslationY(mItemHight - mDrableHight * 2);
			mViewly.setVisibility(View.VISIBLE);
			mCustomersLv.setVisibility(View.VISIBLE);
//			CrmStatisticsVisitorVO crmStatisticsVisitorVO = new CrmStatisticsVisitorVO();
//			crmStatisticsVisitorVO.setFullname("");
//			crmStatisticsVisitorVO.setVisitCount("");
//			mVisitorVO1.getArray().add(crmStatisticsVisitorVO);
			mUsersAdapter.updateData(mVisitorVO1.getArray());
			mCustomersAdapter.updateData(mVisitorVO1.getArray().get(0).getCustomers());

		} else {
			mViewly.setVisibility(View.GONE);
			mCustomersLv.setVisibility(View.GONE);
		}

	}

	@Override
	public void executeFailure() {
		super.executeFailure();
		CustomDialog.closeProgressDialog();
		if (mVisitorVO == null) {
			super.showNoNetView();
		} else {
			super.showNoContentView();
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void initView() {
		mTitleTv.setText("跟单统计");
		mPositionIv = (ImageView) findViewById(R.id.position_iv);
		mUsersLv = (BSListScrollView) findViewById(R.id.users_lv);
		mUsersLv.setParent_scrollview((ScrollView)findViewById(R.id.tantanview));
		LinearLayout layout = new LinearLayout(mContext);
		layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, CommonUtils.getScreenHigh(mContext) / 9));
		mUsersLv.addFooterView(layout,null,false);
 		setListViewHeightBasedOnChildren(mUsersLv);
		mUsersAdapter = new CrmStatisticsVisitorUsersAdapter(this);
		mUsersLv.setAdapter(mUsersAdapter);
		mCustomersLv = (BSListView) findViewById(R.id.customers_lv);
		mCustomersAdapter = new CrmStatisticsVisitorCustomersAdapter(this);
		mCustomersLv.setAdapter(mCustomersAdapter);
		mDrableHight = (float) (this.getResources().getDrawable(R.drawable.crm_potion).getMinimumHeight() * 1.0 / 2);
		mItemHight = (float) (CommonUtils.getViewHigh(mUsersLv) * 1.0 / 5 + mDrableHight);
		mPositionIv.setTranslationY(mItemHight - mDrableHight * 2);
		mCurrentY = mItemHight - mDrableHight * 2;
		mAllCountTv = (TextView) findViewById(R.id.all_count_tv);
		mDayCountTv = (TextView) findViewById(R.id.day_count_tv);
		mViewly = (LinearLayout) findViewById(R.id.list_stone);
		mViewly.getLayoutParams().height = CommonUtils.getScreenHigh(mContext) / 3;
		initTitleView();
		initPopData();
	}

	public void initTitleView() {
		mMoreTitle = (LinearLayout) findViewById(R.id.title03);
		mOneTitle = (LinearLayout) findViewById(R.id.title01);
		mTwoTitle = (LinearLayout) findViewById(R.id.title02);
		mOneTitleTv = (TextView) findViewById(R.id.title_name_01);
		mTwoTitleTv = (TextView) findViewById(R.id.title_name_02);
		mMoreTitleTv = (TextView) findViewById(R.id.title_name_03);
		mOneTitleTv.setText("跟单类型");
		mTwoTitleTv.setText("全部部门");
		mMoreTitleTv.setText("时间筛选");

	}

	@Override
	public void bindViewsListener() {
		mPositionIv.setOnTouchListener(this);
		mUsersLv.setOnScrollListener(this);
		mUsersLv.setOnItemClickListener(this);
		mOneTitle.setOnClickListener(this);
		mTwoTitle.setOnClickListener(this);
		mMoreTitle.setOnClickListener(this);
	 
	}

	public void initPopData() {

		// 跟单类型
		ArrayList<TreeVO> list2 = CommonUtils.getOneLeveTreeVoZero(mVisitorArray);
		mBsPopupWindowsTitleOther = new BSPopupWindowsTitle(this, list2, callback, ViewGroup.LayoutParams.WRAP_CONTENT);
//		// 时间筛选
//		ArrayList<TreeVO> list1 = CommonUtils.getOneLeveTreeVo(mTimeArray);
//		mBsPopupWindowsTitleTime = new BSPopupWindowsTitle(this, list1, callback, ViewGroup.LayoutParams.WRAP_CONTENT);
//
		if (null != getIntent().getSerializableExtra("mVokey")) {
			ArrayList<TreeVO> departList = CommonUtils.getOneLeveTreeVo(((CrmOptionsVO) getIntent().getSerializableExtra("mVokey")).getArray());
			mBsPopupWindowsTitleDep = new BSPopupWindowsTitle(this, departList, callback, CommonUtils.getScreenHigh(this) / 3);

		}
		  if (mBsPopupWindowsTitleTime == null) {
			  mBsPopupWindowsTitleTime = CommonUtils.initPopView(BossStatiscsDocumentaryStatisticsActivity.this, 6, ViewGroup.LayoutParams.WRAP_CONTENT, timeCallback);
	        }
	}
	
	  ResultCallback timeCallback = new ResultCallback() {

	        @Override
	        public void callback(String str, int position) {
	            String timeShow = null;
	            if (str != null) {
	                timeShow = str.split("-")[1];
	                if ("13".equals(timeShow)) {
	                    timeShow = "第一季度";
	                } else if ("14".equals(timeShow)) {
	                    timeShow = "第二季度";
	                } else if ("15".equals(timeShow)) {
	                    timeShow = "第三季度";
	                } else if ("16".equals(timeShow)) {
	                    timeShow = "第四季度";
	                } else {
	                    timeShow = str;
	                }
	            }
	            mMoreTitleTv.setText(timeShow);
	            match(2, str);
	        }
	    };

	// 菜单点击回调函数
	TreeCallBack callback = new TreeCallBack() {

		@Override
		public void callback(TreeVO vo) {
			if (selectOne == -1) {
				mOneTitleTv.setText(vo.getName());
				match(-1, vo.getParentSerachId() + "");
			} else if (selectOne == 0) {
				mTwoTitleTv.setText(vo.getName());
				match(1, vo.getSearchId() + "");
			} else if (selectOne == 1) {
				mMoreTitleTv.setText(vo.getName());
				match(2, vo.getParentSerachId() + "");
			}
		}
	};

	public void match(int key, String value) {
		switch (key) {

		case -1:
			mType = value;
			break;

		case 1:
			mDid = value;
			break;
		case 2:
			mDateType = value;
			break;

		default:
			break;
		}
		// mRefreshListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
		CustomDialog.showProgressDialog(this, "正在加载...");
		new ThreadUtil(this, this).start();
	}

	public void updateCustomers() {
		int count = Math.abs((int) (mCurrentY / mItemHight));
		if (mUsersAdapter.mList.size() > mFirstVisiblePosition + count) {
			mCustomersAdapter.updateData(mUsersAdapter.mList.get(mFirstVisiblePosition + count).getCustomers());
			mClickPosition = mFirstVisiblePosition + count;
		} else {
			mCustomersAdapter.updateData(mUsersAdapter.mList.get(mUsersAdapter.mList.size() - 1).getCustomers());
			mClickPosition = mUsersAdapter.mList.size() - 1;
			// mPositionIv.setTranslationY(mCurrentY - Math.abs(mItemHight) *
			// (count -
			// mUsersAdapter.mList.size() + 1));
			ObjectAnimator oa1 = ObjectAnimator.ofFloat(mPositionIv, "translationY", mCurrentY, mCurrentY - Math.abs(mItemHight) * (count - mUsersAdapter.mList.size() + 1))
					.setDuration(500);
			oa1.start();
			mCurrentY = mCurrentY - Math.abs(mItemHight) * (count - mUsersAdapter.mList.size() + 1);
		}
	}

	public boolean getData() {
		try {
			Gson gson = new Gson();
			HashMap<String, String> mapList = new HashMap<String, String>();
			mapList.put("userid", BSApplication.getInstance().getUserId());
			mapList.put("did", mDid);
			mapList.put("datetype", mDateType);
			mapList.put("type", mType);
			mapList.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
			String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.BOSS_STATISTICS_DOCUMENTARY_CHIDREN, mapList);
			mVisitorVO = gson.fromJson(jsonStrList, CrmStatisticsVisitorVO.class);
			if (Constant.RESULT_CODE.equals(mVisitorVO.getCode())) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		v.getParent().requestDisallowInterceptTouchEvent(true);
		int everyHight = mUsersLv.getHeight();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downY = (int) event.getRawY();

			break;

		case MotionEvent.ACTION_MOVE:
			float dy = event.getRawY() - downY;

			if (dy < 0) {
				mMoveState = false;
				v.setTranslationY(mCurrentY + dy);
			} else {
				mMoveState = true;
				v.setTranslationY(mCurrentY + dy);
			}
			break;
		case MotionEvent.ACTION_CANCEL:
			break;
		case MotionEvent.ACTION_UP:
			// 获取已经移动的
//			float ddy = v.getTranslationY();
//		       if (ddy + mDrableHight > mUsersLv.getMeasuredHeight() || ddy < 0) {
//                   mPositionIv.setTranslationY(mCurrentY);
//                   return true;
//               }
		       
		       float ddy = v.getTranslationY();
               if (ddy + mDrableHight > mUsersLv.getMeasuredHeight() || ddy < 0) {
                   ObjectAnimator oa1 = ObjectAnimator.ofFloat(mPositionIv, "translationY", mCurrentY).setDuration(500);
                   oa1.start();
                   return true;
               }


			float count = Math.abs((Math.abs(ddy - mCurrentY) / mItemHight));
			if (mMoveState) {
				mItemHight = Math.abs(mItemHight);
			} else {
				mItemHight = 0 - Math.abs(mItemHight);
			}

			if (0 <= count && count < 0.5) {
				mPositionIv.setTranslationY(mCurrentY);
			} else if (0.5 <= count && count < 1.5) {
				mPositionIv.setTranslationY(mCurrentY + mItemHight * 1);
				mCurrentY = mCurrentY + mItemHight * 1;
			} else if (1.5 <= count && count < 2.5) {
				mPositionIv.setTranslationY(mCurrentY + mItemHight * 2);
				mCurrentY = mCurrentY + mItemHight * 2;
			} else if (2.5 <= count && count < 3.5) {
				mPositionIv.setTranslationY(mCurrentY + mItemHight * 3);
				mCurrentY = mCurrentY + mItemHight * 3;
			} else {
				mPositionIv.setTranslationY(mCurrentY + mItemHight * 3);
				mCurrentY = mCurrentY + mItemHight * 3;
			}

			updateCustomers();

			break;
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		// mPositionIv.setTranslationY(mCurrentY + mItemHight);
		// mCurrentY = mCurrentY + mItemHight;
		switch (v.getId()) {
		case R.id.title01:
			selectOne = -1;
			mBsPopupWindowsTitleOther.showPopupWindow(mOneTitle);
			break;
		case R.id.title02:
			if (mBsPopupWindowsTitleDep != null) {
				selectOne = 0;
				mBsPopupWindowsTitleDep.showPopupWindow(mOneTitle);
			}
			break;
		case R.id.title03:
			if (mBsPopupWindowsTitleTime != null) {
				selectOne = 1;
				mBsPopupWindowsTitleTime.showAsDropDown(mOneTitle);
			}
			break;

		default:
			break;
		}

	}

	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int scrollState) {
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			// 获取可见位置
			mFirstVisiblePosition = mUsersLv.getFirstVisiblePosition();
			mUsersLv.setSelection(mFirstVisiblePosition);
			updateCustomers();
			// 判断是否是最后一页
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		int position = (int) arg3;
		if(mCurrentY<0) mCurrentY=0;
		if (mClickPosition == 0) {
			mClickPosition = position - mFirstVisiblePosition;
		} else {
			mClickPosition = position - Math.abs(mClickPosition);
		}
		mCustomersAdapter.updateData(mUsersAdapter.mList.get(position).getCustomers());
		ObjectAnimator oa1 = ObjectAnimator.ofFloat(mPositionIv, "translationY", mCurrentY, mCurrentY + Math.abs(mItemHight) * mClickPosition).setDuration(500);
		oa1.start();
		mCurrentY = mCurrentY + Math.abs(mItemHight) * mClickPosition;
		mClickPosition = position;

	}
	
	public static void setListViewHeightBasedOnChildren(ListView listView) {
	    if(listView == null) return;
	    ListAdapter listAdapter = listView.getAdapter();
	    if (listAdapter == null) {
	        // pre-condition
	        return;
	    }
	    int totalHeight = 0;
	    for (int i = 0; i < listAdapter.getCount(); i++) {
	        View listItem = listAdapter.getView(i, null, listView);
	        listItem.measure(0, 0);
	        totalHeight += listItem.getMeasuredHeight();
	    }
	    ViewGroup.LayoutParams params = listView.getLayoutParams();
	    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
	    listView.setLayoutParams(params);
	}
}
