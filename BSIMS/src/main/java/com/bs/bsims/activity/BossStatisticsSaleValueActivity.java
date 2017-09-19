
package com.bs.bsims.activity;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.CrmStaticSaleValueAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.BossStatiscsDocumentaryVo;
import com.bs.bsims.model.CrmOptionsVO;
import com.bs.bsims.model.CrmStaticSaleValueVO;
import com.bs.bsims.model.TreeVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CommonUtils.ResultCallback;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSCrmMonthSaleValue;
import com.bs.bsims.view.BSListView;
import com.bs.bsims.view.BSPopupWindowsTitle;
import com.bs.bsims.view.BSPopupWindowsTitle.TreeCallBack;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BossStatisticsSaleValueActivity extends BaseActivity implements OnClickListener {
    private TextView mComplteValue, mTargetMoney, mHuikuanMoney;
    private BSListView mSaleValueListView;
    private CrmStaticSaleValueVO mCrmSaleVo;
    private CrmStaticSaleValueAdapter mSaleAdapter;
    private List<CrmStaticSaleValueVO> mCrmStaticList = new ArrayList<CrmStaticSaleValueVO>();
    private BSCrmMonthSaleValue mBsSaleValue;

    // 定义菜单相关变量
    private boolean mFlag = true;
    private CrmOptionsVO mCrmOptionsVO;
    private List<BossStatiscsDocumentaryVo> bVoList;
    private LinearLayout mOneTitle, mTwoTitle;
    private TextView mOneTitleTv, mTwoTitleTv;
    private BSPopupWindowsTitle mBsPopupWindowsTitle, mBsPopupWindowsTitleDep;
    private PopupWindow mTimePop;// 时间筛选弹出框
    private int mSelectOne = 0;
    private String mDid = "0";// 0为默认全部部门
    private String mDateType = "";// 1：近半年，2：上半年，3：下半年
    private String[] mTimeArray = {
            "近半年", "上半年", "下半年"
    };

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.boss_statistics_sale_value_view, mContentLayout);

    }

    @Override
    public boolean getDataResult() {
        // TODO Auto-generated method stub
        return getData();
    }

    private boolean getData() {
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
            map.put("did", mDid);
            map.put("datetype", mDateType);
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.BOSS_STATISTICS_SALE_VALUE, map);
            mCrmSaleVo = gson.fromJson(jsonStr, CrmStaticSaleValueVO.class);

            if (Constant.RESULT_CODE.equals(mCrmSaleVo.getCode())) {
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
        mSaleValueListView.showHead(this, false);
        mLoadingLayout.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);
        if (mFlag) {
            if (mCrmOptionsVO == null)
                return;
            createLeftPop();
            mFlag = false;
        }
        if (mCrmSaleVo == null || mCrmSaleVo.getInfo() == null) {
            return;
        } else {
            updateUi();
        }
    }

    @Override
    public void executeFailure() {
        mSaleValueListView.showHead(this, false);
        mLoadingLayout.setVisibility(View.VISIBLE);
        mContentLayout.setVisibility(View.GONE);
        if (mCrmSaleVo == null) {
            super.showNoNetView();
        } else {
            super.showNoContentView();
        }
    }

    @Override
    public void updateUi() {
        mCrmSaleVo = mCrmSaleVo.getInfo();
        mComplteValue.setText(mCrmSaleVo.getCompPercent());// 完成率
        mTargetMoney.setText("￥" + mCrmSaleVo.getTotalTarget());// 目标金额
        mHuikuanMoney.setText("￥" + mCrmSaleVo.getTotalPayment());// 回款金额
        float data = Float.parseFloat(CommonUtils.isNormalData(mCrmSaleVo.getCompRate()));
        // 超额完成任务，就显示100%，对应于1
        if (data > 1) {
            mBsSaleValue.setData1(1);
        } else {
            mBsSaleValue.setData1(data);
        }
        mBsSaleValue.invalidate();
        mCrmStaticList = mCrmSaleVo.getUsers();
        mSaleAdapter.updateData(mCrmStaticList);

    }

    @Override
    public void initView() {
        initTitleView();
        initPopData();// 部门、 时间Pop初始化
        mTitleTv.setText("销售业绩");
        mBsSaleValue = (BSCrmMonthSaleValue) findViewById(R.id.static_sale_value);
        mComplteValue = (TextView) findViewById(R.id.complete_value);
        mTargetMoney = (TextView) findViewById(R.id.target_money);
        mHuikuanMoney = (TextView) findViewById(R.id.huikuan_money);
        mSaleValueListView = (BSListView) findViewById(R.id.static_lv_refresh);
        mSaleAdapter = new CrmStaticSaleValueAdapter(BossStatisticsSaleValueActivity.this, mCrmStaticList);
        mSaleValueListView.setAdapter(mSaleAdapter);

    }

    public void initPopData() {
        // 时间筛选
        // ArrayList<TreeVO> list1 = CommonUtils.getOneLeveTreeVo(mTimeArray);
        // mBsPopupWindowsTitle = new BSPopupWindowsTitle(this, list1, callback,
        // ViewGroup.LayoutParams.WRAP_CONTENT);
        if (mTimePop == null) {
            mTimePop = CommonUtils.initPopView(BossStatisticsSaleValueActivity.this, 6, ViewGroup.LayoutParams.WRAP_CONTENT, timeCallback);
        }
    }

    // 部门筛选
    public void createLeftPop() {
        ArrayList<TreeVO> list1 = CommonUtils.getOneLeveTreeVo(mCrmOptionsVO.getArray());
        mBsPopupWindowsTitleDep = new BSPopupWindowsTitle(this, list1, callback, CommonUtils.getScreenHigh(BossStatisticsSaleValueActivity.this) / 3);
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
        // mRefreshListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
        mSaleValueListView.showHead(this, true);
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

    @Override
    public void bindViewsListener() {
        mOneTitle.setOnClickListener(this);
        mTwoTitle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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

}
