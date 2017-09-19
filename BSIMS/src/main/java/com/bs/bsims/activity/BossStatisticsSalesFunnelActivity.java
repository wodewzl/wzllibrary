
package com.bs.bsims.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.BossStatisticsSalesFunnelAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.BossStatisticsSalesFunnelVO;
import com.bs.bsims.model.CrmOptionsVO;
import com.bs.bsims.model.TreeVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CommonUtils.ResultCallback;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSListView;
import com.bs.bsims.view.BSPopupWindowsTitle;
import com.bs.bsims.view.BSPopupWindowsTitle.TreeCallBack;
import com.bs.bsims.view.BSStatisticsFunnelView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("NewApi")
public class BossStatisticsSalesFunnelActivity extends BaseActivity implements OnTouchListener, OnClickListener, OnItemClickListener {
    private BossStatisticsSalesFunnelVO mSalesFunnelVO;
    private BSListView mCustomersLv;
    private BossStatisticsSalesFunnelAdapter mAdapter;
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

    // 赛选菜单部分
    private BSPopupWindowsTitle mBsPopupWindowsTitle, mBsPopupWindowsTitleDep;
    private PopupWindow mTimePop;// 时间筛选弹出框
    private int selectOne = 0;
    private String mDid = "0";// 0为默认全部部门
    private String mDateType = "";// 1：近半年，2：上半年，3：下半年
    private String[] mTimeArray = {
            "近半年", "上半年", "下半年"
    };
    private LinearLayout mOneTitle, mTwoTitle;
    private TextView mOneTitleTv, mTwoTitleTv;
    private RelativeLayout mSlideLayout;
    private int mCurrentIndex = 0;
    private TextView mTotalMoneyTv, mCurrentMoneyTv;

    // 漏斗部分
    private TextView mTextView01, mTextView02, mTextView03, mTextView04, mTextView05;
    private BSStatisticsFunnelView mFunnelView;
    private CrmOptionsVO mCrmOptionsVO;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.boss_statistics_sales_funnel, mContentLayout);

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
        BossStatisticsSalesFunnelVO vo = mSalesFunnelVO.getInfo();
        CommonUtils.setTextTwoBefore(this, mTotalMoneyTv, "总金额： ", "￥" + vo.getMaxMoney(), R.color.C5, 1.0f);
        showCurrentMoney();
        float crmfunnelview_one = Float.parseFloat(CommonUtils.isNormalData(vo.getFunnel().get(0).getCount()));
        float crmfunnelview_two = Float.parseFloat(CommonUtils.isNormalData(vo.getFunnel().get(1).getCount()));
        float crmfunnelview_three = Float.parseFloat(CommonUtils.isNormalData(vo.getFunnel().get(2).getCount()));
        float crmfunnelview_four = Float.parseFloat(CommonUtils.isNormalData(vo.getFunnel().get(3).getCount()));
        float crmfunnelview_five = Float.parseFloat(CommonUtils.isNormalData(vo.getFunnel().get(4).getCount()));

        if (crmfunnelview_one == 0 && crmfunnelview_two == 0 && crmfunnelview_three == 0 && crmfunnelview_four == 0) {
            mFunnelView.setFlag(true);
        } else {
            mFunnelView.setFlag(false);
        }
        mFunnelView.setPrice1(crmfunnelview_one);
        mFunnelView.setPrice2(crmfunnelview_two);
        mFunnelView.setPrice3(crmfunnelview_three);
        mFunnelView.setPrice4(crmfunnelview_four);
        mFunnelView.setPrice5(crmfunnelview_five);
        mFunnelView.invalidate();

        mTextView01.setText(vo.getFunnel().get(0).getStatusName() + "( " + vo.getFunnel().get(0).getPercent() + ")");
        mTextView02.setText(vo.getFunnel().get(1).getStatusName() + "(" + vo.getFunnel().get(1).getPercent() + ")");
        mTextView03.setText(vo.getFunnel().get(2).getStatusName() + "(" + vo.getFunnel().get(2).getPercent() + ")");
        mTextView04.setText(vo.getFunnel().get(3).getStatusName() + "(" + vo.getFunnel().get(3).getPercent() + ")");
        mTextView05.setText(vo.getFunnel().get(4).getStatusName() + "(" + vo.getFunnel().get(4).getPercent() + ")");

    }

    public void showCurrentMoney() {
        if (mSalesFunnelVO == null || mSalesFunnelVO.getInfo() == null || mSalesFunnelVO.getInfo().getFunnel() == null)
            return;
        BossStatisticsSalesFunnelVO vo = mSalesFunnelVO.getInfo().getFunnel().get(mCurrentIndex);
        switch (mCurrentIndex) {
            case 0:
                CommonUtils.setTextTwoBefore(this, mCurrentMoneyTv, "初步接洽： ", "￥" + vo.getMoney() + "/" + vo.getCount() + "个", R.color.C5, 1.0f);
                break;
            case 1:
                CommonUtils.setTextTwoBefore(this, mCurrentMoneyTv, "需求确定： ", "￥" + vo.getMoney() + "/" + vo.getCount() + "个", R.color.C5, 1.0f);
                break;
            case 2:
                CommonUtils.setTextTwoBefore(this, mCurrentMoneyTv, "方案报价： ", "￥" + vo.getMoney() + "/" + vo.getCount() + "个", R.color.C5, 1.0f);
                break;
            case 3:
                CommonUtils.setTextTwoBefore(this, mCurrentMoneyTv, "谈判审核： ", "￥" + vo.getMoney() + "/" + vo.getCount() + "个", R.color.C5, 1.0f);
                break;
            case 4:
                CommonUtils.setTextTwoBefore(this, mCurrentMoneyTv, "赢单： ", "￥" + vo.getMoney() + "/" + vo.getCount() + "个", R.color.C5, 1.0f);
                break;

            default:
                break;
        }
        mAdapter.updateData(vo.getList());
    }

    @Override
    public void executeFailure() {
        super.executeFailure();
        if (mSalesFunnelVO == null) {
            super.showNoNetView();
        } else {
            super.showNoContentView();
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void initView() {

        mTitleTv.setText("销售漏斗");
        mSlideLayout = (RelativeLayout) findViewById(R.id.slide_layout);
        mCustomersLv = (BSListView) findViewById(R.id.customers_lv);
        mCustomersLv.showHead(this, true);
        mAdapter = new BossStatisticsSalesFunnelAdapter(this);
        mCustomersLv.setAdapter(mAdapter);
        mPositionIv = (ImageView) findViewById(R.id.position_iv);
        mDrableHight = (float) (this.getResources().getDrawable(R.drawable.crm_potion).getMinimumHeight() * 1.0 / 2);
        mItemHight = (float) (CommonUtils.getViewHigh(mSlideLayout) * 1.0 / 5);
        mPositionIv.setTranslationY(mItemHight / 2 - mDrableHight);
        mCurrentY = mItemHight / 2 - mDrableHight;
        mAllCountTv = (TextView) findViewById(R.id.all_count_tv);
        mDayCountTv = (TextView) findViewById(R.id.day_count_tv);
        initData();
        initTitleView();
        mTotalMoneyTv = (TextView) findViewById(R.id.total_money_tv);
        mCurrentMoneyTv = (TextView) findViewById(R.id.current_money_tv);

        // 漏斗部分
        mFunnelView = (BSStatisticsFunnelView) findViewById(R.id.crmfunnelview);
        mTextView01 = (TextView) findViewById(R.id.text01);
        mTextView02 = (TextView) findViewById(R.id.text02);
        mTextView03 = (TextView) findViewById(R.id.text03);
        mTextView04 = (TextView) findViewById(R.id.text04);
        mTextView05 = (TextView) findViewById(R.id.text05);
    }

    public void initData() {
        getOptionsData();
    }

    @Override
    public void bindViewsListener() {
        mOneTitle.setOnClickListener(this);
        mTwoTitle.setOnClickListener(this);
        mPositionIv.setOnTouchListener(this);
    }

    public void updateCustomers() {
        showCurrentMoney();
    }

    public boolean getData() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> mapList = new HashMap<String, String>();
            mapList.put("userid", BSApplication.getInstance().getUserId());
            mapList.put("type", getIntent().getStringExtra("type"));
            mapList.put("did", mDid);
            mapList.put("datetype", mDateType);
            mapList.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.BOSS_STATISTICS_SALES_FUNNEL, mapList);
            mSalesFunnelVO = gson.fromJson(jsonStrList, BossStatisticsSalesFunnelVO.class);
            if (Constant.RESULT_CODE.equals(mSalesFunnelVO.getCode())) {
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        v.getParent().requestDisallowInterceptTouchEvent(true);
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
                float ddy = v.getTranslationY();
                if (ddy + mDrableHight > mSlideLayout.getMeasuredHeight() || ddy < 0) {
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

                mCurrentIndex = Math.abs((int) (mCurrentY / mItemHight));
                if (mCurrentIndex > 4)
                    mCurrentIndex = 4;
                updateCustomers();

                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title01:
                if (mBsPopupWindowsTitleDep != null) {
                    selectOne = 0;
                    mBsPopupWindowsTitleDep.showPopupWindow(mOneTitle);
                } else {
                    ArrayList<TreeVO> list = CommonUtils.getOneLeveTreeVo(mCrmOptionsVO.getArray());
                    mBsPopupWindowsTitleDep = new BSPopupWindowsTitle(this, list, callback, CommonUtils.getScreenHigh(this) / 3);
                    mBsPopupWindowsTitleDep.showPopupWindow(mOneTitle);
                }
                break;
            case R.id.title02:
                // if (mBsPopupWindowsTitle != null) {
                // selectOne = 1;
                // mBsPopupWindowsTitle.showPopupWindow(mOneTitle);
                // }
                if (mTimePop == null) {
                    mTimePop = CommonUtils.initPopView(BossStatisticsSalesFunnelActivity.this, 6, ViewGroup.LayoutParams.WRAP_CONTENT, timeCallback);
                }
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

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        int position = (int) arg3;
        if (mClickPosition == 0) {
            mClickPosition = position - mFirstVisiblePosition;
        } else {
            mClickPosition = position - Math.abs(mClickPosition);
        }
        ObjectAnimator oa1 = ObjectAnimator.ofFloat(mPositionIv, "translationY", mCurrentY, mCurrentY + Math.abs(mItemHight) * mClickPosition).setDuration(500);
        oa1.start();
        mCurrentY = mCurrentY + Math.abs(mItemHight) * mClickPosition;
        mClickPosition = position;

    }

    // 菜单点击回调函数
    TreeCallBack callback = new TreeCallBack() {

        @Override
        public void callback(TreeVO vo) {
            mCustomersLv.showHead(BossStatisticsSalesFunnelActivity.this, true);
            if (selectOne == 0) {
                // 审批一级菜单
                mOneTitleTv.setText(vo.getName());
                match(1, vo.getSearchId() + "");
            } else if (selectOne == 1) {
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
        // mRefreshListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);

        new ThreadUtil(this, this).start();
    }

    public void initTitleView() {
        mOneTitle = (LinearLayout) findViewById(R.id.title01);
        mTwoTitle = (LinearLayout) findViewById(R.id.title02);
        findViewById(R.id.title03).setVisibility(View.GONE);
        mOneTitleTv = (TextView) findViewById(R.id.title_name_01);
        mTwoTitleTv = (TextView) findViewById(R.id.title_name_02);
        mOneTitleTv.setText("部门筛选");
        mTwoTitleTv.setText("时间筛选");
    }

    public void getOptionsData() {
        new Thread() {
            public void run() {
                try {
                    Gson gson = new Gson();
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("userid", BSApplication.getInstance().getUserId());
                    map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
                    String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.BOSS_STATISTICS_CRMOPTIONSBYDP, map);
                    mCrmOptionsVO = gson.fromJson(jsonStr, CrmOptionsVO.class);

                    if (HttpClientUtil.isNetworkConnected(BossStatisticsSalesFunnelActivity.this)) {
                        if (mCrmOptionsVO == null) {
                            // if (Constant.RESULT_CODE400.equals(mCrmOptionsVO.getCode()))
                            // return;
                            getOptionsData();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
