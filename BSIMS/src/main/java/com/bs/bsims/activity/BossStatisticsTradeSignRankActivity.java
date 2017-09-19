
package com.bs.bsims.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
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
import com.bs.bsims.adapter.BossStatisticsTradeSignRankAdapter;
import com.bs.bsims.adapter.CrmBussinesTranctAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.BossStatiscsDocumentaryVo;
import com.bs.bsims.model.BossStatisticsTradeSignVo;
import com.bs.bsims.model.CrmOptionsVO;
import com.bs.bsims.model.CrmStatisticsVisitorVO;
import com.bs.bsims.model.TreeVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CommonUtils.ResultCallback;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSListScrollView;
import com.bs.bsims.view.BSListView;
import com.bs.bsims.view.BSPopupWindowsTitle;
import com.bs.bsims.view.BSPopupWindowsTitle.TreeCallBack;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint("NewApi")
public class BossStatisticsTradeSignRankActivity extends BaseActivity implements OnTouchListener, OnClickListener, OnScrollListener, OnItemClickListener {
    private BossStatisticsTradeSignVo mSignVO;
    private BSListView mCustomersLv;
    private BSListScrollView mUsersLv;
    private BossStatisticsTradeSignRankAdapter mUsersAdapter;
    private CrmBussinesTranctAdapter mCustomersAdapter;
    private TextView mAllCountTv, mDayCountTv;
    private ImageView mPositionIv;
    private LinearLayout mUsersLayout, mCustomersLayout;
    private LinearLayout mNoContentLayout;

    // 滑块滑动相关变量
    private int mDownY;
    private float mItemHight;// 每条的高度
    private float mDrableHight;// 游标的高度
    private float mCurrentY;// 当前游标停留的Y值
    private boolean mMoveState = true;// true 为下滑的，false为上滑动
    private int mFirstVisiblePosition;
    private int mClickPosition;
    private int mItemPosition;

    // 菜单相关变量
    private boolean mFlag = true;
    private CrmOptionsVO mCrmOptionsVO;
    private List<BossStatiscsDocumentaryVo> bVoList;
    private LinearLayout mOneTitle, mTwoTitle;
    private TextView mOneTitleTv, mTwoTitleTv;
    private BSPopupWindowsTitle mBsPopupWindowsTitle, mBsPopupWindowsTitleDep;
    private PopupWindow mTimePop;// 时间筛选弹出框
    private int mSelectOne = 0;
    private String mDid = "0";// 0为默认全部部门
    private String mDateType = "";// 1：近半年，2：近三个月，3：上半年，4：下半年
    private String[] mTimeArray = {
            "近半年", "上半年", "下半年"
    };

    private List<CrmStatisticsVisitorVO> mVisitorVO;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.boss_statistics_trade_sign_rank_view, mContentLayout);

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
        super.executeSuccess();
        mCustomersLv.showHead(this, false);
        mSignVO = mSignVO.getInfo();
        mAllCountTv.setText(mSignVO.getTotalCount());
        mDayCountTv.setText(mSignVO.getCountPerUser());
        if (mSignVO.getList() == null) {
            mUsersLayout.setVisibility(View.GONE);
            mCustomersLayout.setVisibility(View.GONE);
            mNoContentLayout.setVisibility(View.VISIBLE);
        } else {
            mNoContentLayout.setVisibility(View.GONE);
            mUsersLayout.setVisibility(View.VISIBLE);
            mCustomersLayout.setVisibility(View.VISIBLE);
            mUsersAdapter.updateData(mSignVO.getList());
            mCustomersAdapter.updateData(mSignVO.getList().get(0).getContracts());
            mFirstVisiblePosition = mUsersLv.getFirstVisiblePosition();
        }
        if (mFlag) {
            if (mCrmOptionsVO == null)
                return;
            createLeftPop();
            mFlag = false;
        }

    }

    @Override
    public void executeFailure() {
        super.executeFailure();
        mCustomersLv.showHead(this, false);
        if (mSignVO == null) {
            super.showNoNetView();
        } else {
            super.showNoContentView();
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void initView() {
        initTitleView();// 定义菜单菜单
        initPopData();// 部门、 时间Pop初始化
        mTitleTv.setText("签单排名");
        mUsersLv = (BSListScrollView) findViewById(R.id.users_lv);
        mCustomersLv = (BSListView) findViewById(R.id.customers_lv);
        mUsersLv.setParent_scrollview((ScrollView) findViewById(R.id.tantanview));
        mUsersAdapter = new BossStatisticsTradeSignRankAdapter(this);
        mCustomersAdapter = new CrmBussinesTranctAdapter(this);
        mUsersLv.setAdapter(mUsersAdapter);
        mCustomersLv.setAdapter(mCustomersAdapter);
        mPositionIv = (ImageView) findViewById(R.id.position_iv);
        mDrableHight = (float) (this.getResources().getDrawable(R.drawable.crm_potion).getMinimumHeight() * 1.0 / 2);
        mItemHight = (float) (CommonUtils.getViewHigh(mUsersLv) * 1.0 / 5 + mDrableHight);
        mPositionIv.setTranslationY(mItemHight - mDrableHight * 2);
        mCurrentY = mItemHight - mDrableHight * 2;
        mAllCountTv = (TextView) findViewById(R.id.all_count_tv);
        mDayCountTv = (TextView) findViewById(R.id.day_count_tv);
        mUsersLayout = (LinearLayout) findViewById(R.id.users_layout);
        // mUsersLayout.getLayoutParams().height = CommonUtils.dip2px(this,
        // CommonUtils.getScreenHigh(BossStatisticsTradeSignRankActivity.this) / 7);

        mUsersLayout.getLayoutParams().height = CommonUtils.dip2px(this, 260);

        mCustomersLayout = (LinearLayout) findViewById(R.id.customers_layout);
        mNoContentLayout = (LinearLayout) findViewById(R.id.no_content_layout);
    }

    public void initTitleView() {
        mOneTitle = (LinearLayout) findViewById(R.id.title01);
        mTwoTitle = (LinearLayout) findViewById(R.id.title02);
        findViewById(R.id.title03).setVisibility(View.GONE);
        mOneTitleTv = (TextView) findViewById(R.id.title_name_01);
        mTwoTitleTv = (TextView) findViewById(R.id.title_name_02);
        mOneTitleTv.setText("全部部门");
        mTwoTitleTv.setText("时间筛选");

    }

    // 时间筛选
    public void initPopData() {
        // ArrayList<TreeVO> list1 = CommonUtils.getOneLeveTreeVo(mTimeArray);
        // mBsPopupWindowsTitle = new BSPopupWindowsTitle(this, list1, callback,
        // ViewGroup.LayoutParams.WRAP_CONTENT);
        if (mTimePop == null) {
            mTimePop = CommonUtils.initPopView(BossStatisticsTradeSignRankActivity.this, 6, ViewGroup.LayoutParams.WRAP_CONTENT, timeCallback);
        }
    }

    // 部门筛选
    public void createLeftPop() {
        ArrayList<TreeVO> list1 = CommonUtils.getOneLeveTreeVo(mCrmOptionsVO.getArray());
        mBsPopupWindowsTitleDep = new BSPopupWindowsTitle(this, list1, callback, CommonUtils.getScreenHigh(BossStatisticsTradeSignRankActivity.this) / 3);
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
            mTwoTitleTv.setText(timeShow);
            match(2, str);
        }
    };

    // 菜单点击回调函数
    TreeCallBack callback = new TreeCallBack() {

        @Override
        public void callback(TreeVO vo) {
            if (mSelectOne == 0) {
                // 审批一级菜单
                mOneTitleTv.setText(vo.getName());
                match(1, vo.getSearchId() + "");
            } else if (mSelectOne == 1) {
                mTwoTitleTv.setText(vo.getName());
                match(2, vo.getParentSerachId() + "");
            }
        }
    };

    public void match(int key, String value) {
        switch (key) {

            case 1:
                mDid = value;
                break;
            case 2:
                mDateType = value;
                break;

            default:
                break;
        }
        mCustomersLv.showHead(this, true);
        new ThreadUtil(this, this).start();
    }

    @Override
    public void bindViewsListener() {
        mPositionIv.setOnTouchListener(this);
        mUsersLv.setOnScrollListener(this);

        mUsersLv.setOnItemClickListener(this);
        mOneTitle.setOnClickListener(this);
        mTwoTitle.setOnClickListener(this);
    }

    public void updateCustomers() {
        int count = Math.abs((int) (mCurrentY / mItemHight));
        if (mUsersAdapter.mList.size() > mFirstVisiblePosition + count) {
            mCustomersAdapter.updateData(mUsersAdapter.mList.get(mFirstVisiblePosition + count).getContracts());
            mClickPosition = mFirstVisiblePosition + count;
        } else {
            mCustomersAdapter.updateData(mUsersAdapter.mList.get(mUsersAdapter.mList.size() - 1).getContracts());
            mClickPosition = mUsersAdapter.mList.size() - 1;
            // mPositionIv.setTranslationY(mCurrentY - Math.abs(mItemHight) * (count -
            // mUsersAdapter.mList.size() + 1));
            ObjectAnimator oa1 = ObjectAnimator.ofFloat(mPositionIv, "translationY", mCurrentY, mCurrentY - Math.abs(mItemHight) * (count - mUsersAdapter.mList.size() + 1)).setDuration(500);
            oa1.start();
            mCurrentY = mCurrentY - Math.abs(mItemHight) * (count - mUsersAdapter.mList.size() + 1);
        }
    }

    public boolean getData() {
        Gson gson = new Gson();
        if (mFlag) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.BOSS_STATISTICS_CRMOPTIONSBYDP, map);
            mCrmOptionsVO = gson.fromJson(jsonStr, CrmOptionsVO.class);
        }
        try {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            // map.put("userid", "22");
            map.put("did", mDid);
            map.put("datetype", mDateType);
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.BOSS_STATISTICS_TRADE_SIGN_RANK, map);
            mSignVO = gson.fromJson(jsonStrList, BossStatisticsTradeSignVo.class);
            if (Constant.RESULT_CODE.equals(mSignVO.getCode())) {
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
                mDownY = (int) event.getRawY();

                break;

            case MotionEvent.ACTION_MOVE:
                float dy = event.getRawY() - mDownY;

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
                float ddy = v.getTranslationY();
                if (ddy + mDrableHight > mUsersLv.getMeasuredHeight() || ddy < 0) {
                    mPositionIv.setTranslationY(mCurrentY);
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
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.title01:
                if (mBsPopupWindowsTitleDep != null) {
                    mSelectOne = 0;
                    mBsPopupWindowsTitleDep.showPopupWindow(mOneTitle);
                }
                break;
            case R.id.title02:
                // if (mBsPopupWindowsTitle != null) {
                // mSelectOne = 1;
                // mBsPopupWindowsTitle.showPopupWindow(mOneTitle);
                // }
                if (!mTimePop.isShowing()) {
                    mTimePop.showAsDropDown(mOneTitle);
                } else {
                    mTimePop.dismiss();
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onScroll(AbsListView v, int arg1, int arg2, int arg3) {
        v.getParent().requestDisallowInterceptTouchEvent(true);
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
        if (mClickPosition == 0) {
            mClickPosition = position - mFirstVisiblePosition;
        } else {
            mClickPosition = position - Math.abs(mClickPosition);
        }
        mCustomersAdapter.updateData(mUsersAdapter.mList.get(position).getContracts());
        ObjectAnimator oa1 = ObjectAnimator.ofFloat(mPositionIv, "translationY", mCurrentY, mCurrentY + Math.abs(mItemHight) * mClickPosition).setDuration(500);
        oa1.start();
        mCurrentY = mCurrentY + Math.abs(mItemHight) * mClickPosition;
        mClickPosition = position;

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null)
            return;
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
