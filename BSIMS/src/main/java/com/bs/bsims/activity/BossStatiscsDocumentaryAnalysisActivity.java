/**
 * 
 */

package com.bs.bsims.activity;

import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.CrmStatisticsVisitorCustomersAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.BossStatiscsDocumentaryVo;
import com.bs.bsims.model.CrmOptionsVO;
import com.bs.bsims.model.TreeVO;
import com.bs.bsims.pc.chart.piechart.BossStatisticsPieChart;
import com.bs.bsims.pc.chart.piechart.BossStatisticsPieChart.ItemOnClickCallback;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CommonUtils.ResultCallback;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomLog;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSListView;
import com.bs.bsims.view.BSPopupWindowsTitle;
import com.bs.bsims.view.BSPopupWindowsTitle.TreeCallBack;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * BS北盛最帅程序员 Copyright (c) 2016 湖北北盛科技有限公司
 * 
 * @author 梁骚侠
 * @date 2016-1-19
 * @version 1.22 content:跟单分析
 */
public class BossStatiscsDocumentaryAnalysisActivity extends BaseActivity implements OnClickListener {

    private TextView mCustomerTotal, mCustomerCount, mCustomerDay;
    private BossStatisticsPieChart bossStatisticsPieChart;
    private TextView text_item_info, mPiechart_Count, mPiechart_Day_Count;
    private BSListView mBsListView;

    private CrmStatisticsVisitorCustomersAdapter mCustomersAdapter;

    private BossStatiscsDocumentaryVo bVo;

    private LinearLayout mOneTitle, mTwoTitle, mLinearly;
    private TextView mOneTitleTv, mTwoTitleTv;
    private BSPopupWindowsTitle mBsPopupWindowsTitle, mBsPopupWindowsTitleDep;
    private PopupWindow mTimePop;// 时间筛选弹出框
    private int selectOne = 0;
    private String mDid = "0";// 0为默认全部部门
    private String mDateType = "";// 1：近半年，2：近三个月，3：上半年，4：下半年
    private String[] mTimeArray = {
            "近半年", "上半年", "下半年"
    };

    private boolean mFlag = true;

    private CrmOptionsVO mCrmOptionsVO;
    private List<BossStatiscsDocumentaryVo> bVoList;

    /*
     * (non-Javadoc)
     * @see BaseActivity#baseSetContentView()
     */
    @Override
    public void baseSetContentView() {
        // TODO Auto-generated method stub
        View.inflate(this, R.layout.documentaryanalyis_index, mContentLayout);
    }

    /*
     * (non-Javadoc)
     * @see BaseActivity#getDataResult()
     */
    @Override
    public boolean getDataResult() {
        // TODO Auto-generated method stub
        return getData();
    }

    /*
     * (non-Javadoc)
     * @see BaseActivity#updateUi()
     */
    @Override
    public void updateUi() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see BaseActivity#initView()
     */
    @Override
    public void initView() {
        // TODO Auto-generated method stub

        mCustomerTotal = (TextView) findViewById(R.id.customer_total);
        mCustomerCount = (TextView) findViewById(R.id.customer_count);
        mCustomerDay = (TextView) findViewById(R.id.customer_vday);

        text_item_info = (TextView) findViewById(R.id.text_item_info);
        mPiechart_Count = (TextView) findViewById(R.id.piechart_count);
        mPiechart_Day_Count = (TextView) findViewById(R.id.piechart_day_count);
        mBsListView = (BSListView) findViewById(R.id.bslistview);

        mCustomersAdapter = new CrmStatisticsVisitorCustomersAdapter(this);
        mBsListView.setAdapter(mCustomersAdapter);
        bossStatisticsPieChart = new BossStatisticsPieChart(this);
        LinearLayout layout = (LinearLayout) findViewById(R.id.piechart);
        layout.addView(bossStatisticsPieChart);
        initTitleView();
        initPopData();
        mLinearly = (LinearLayout) findViewById(R.id.piechart_text);
        mTitleTv.setText("跟单分布");
        mOkTv.setVisibility(View.GONE);
        mOkTv.setText("排行");

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
            HashMap<String, String> mapList = new HashMap<String, String>();
            mapList.put("userid", BSApplication.getInstance().getUserId());
            mapList.put("did", mDid);
            mapList.put("datetype", mDateType);
            mapList.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.BOSS_STATISTICS_DOCUMENTARY, mapList);
            bVo = gson.fromJson(jsonStrList, BossStatiscsDocumentaryVo.class);
            if (Constant.RESULT_CODE.equals(bVo.getCode())) {
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

    /*
     * (non-Javadoc)
     * @see BaseActivity#bindViewsListener()
     */
    @Override
    public void bindViewsListener() {
        // TODO Auto-generated method stub
        mOneTitle.setOnClickListener(this);
        mTwoTitle.setOnClickListener(this);
        mOkTv.setOnClickListener(this);
    }

    /*
     * (non-Javadoc)
     * @see BaseActivity#executeSuccess()
     */
    @Override
    public void executeSuccess() {
        // TODO Auto-generated method stub
        super.executeSuccess();
        mOkTv.setVisibility(View.VISIBLE);
        if (mFlag) {
            if (mCrmOptionsVO == null)
                return;
            createLeftPop();
            mFlag = false;
        }
        if (null == bVo || null == bVo.getInfo()) {
            return;
        }

        mCustomerTotal.setText(bVo.getInfo().getTotalCustomerCount());
        mCustomerCount.setText(bVo.getInfo().getTotalVisitCount());
        mCustomerDay.setText(bVo.getInfo().getVisitPerCustomer());

        if (null == bVo.getInfo().getCharts()) {// 饼子图没得数据的时候
            bossStatisticsPieChart.setAllTotal(-1);
            bossStatisticsPieChart.setItems(new float[] {
                    100
            });
            // 作用是在条件刷选下，避免上次饼状图值对下次无数据值情况下颜色块造成影响， 无数据时饼状图为灰色，但是有时第一次总为其他色（eg:绿色）
            bossStatisticsPieChart.setFirstItemSizes();
            bossStatisticsPieChart.intitPieChart(new ItemOnClickCallback() {

                @Override
                public void itemClickCallback(int position) {
                    // TODO Auto-generated method stub
                    bossStatisticsPieChart.setRadiocontent("无数据");
                    mCustomersAdapter.mList.clear();
                    mCustomersAdapter.notifyDataSetChanged();
                    if (mBsListView.getVisibility() == View.VISIBLE) {
                        mBsListView.setVisibility(View.GONE);
                        mLinearly.setVisibility(View.GONE);
                    }
                }
            });

        } else {
            bVoList = new ArrayList<BossStatiscsDocumentaryVo>();
            bVoList.clear();
            bVoList.addAll(bVo.getInfo().getCharts());
            CustomLog.e("eero", bVoList.size() + "size");
            String colors[] = new String[bVoList.size()];
            for (int i = 0; i < bVoList.size(); i++) {
                String str = bVoList.get(i).getId();
                if (str.equals("1")) {
                    colors[i] = "#FFB310";
                } else if (str.equals("2")) {
                    colors[i] = "#76CBF4";
                } else if (str.equals("3")) {
                    colors[i] = "#39C0F0";
                } else {
                    colors[i] = "#00A8FF";
                }
            }

            bossStatisticsPieChart.setColors(colors);
            bossStatisticsPieChart.setAllTotal(Float.parseFloat(bVo.getInfo().getTotalCustomerCount()));
            float iFloat[] = new float[bVoList.size()];
            for (int i = 0; i < iFloat.length; i++) {
                iFloat[i] = Float.parseFloat(bVoList.get(i).getRate()) * 100;
            }
            bossStatisticsPieChart.setItems(iFloat);
            bossStatisticsPieChart.intitPieChart(new ItemOnClickCallback() {

                @Override
                public void itemClickCallback(int position) {
                    // TODO Auto-generated method stub

                    bossStatisticsPieChart.setRadiocontent(bVoList.get(position).getName());
                    text_item_info.setText(bVoList.get(position).getName() + " " + bVoList.get(position).getPercent());
                    if (bVoList.get(position).getId().equals("1")) {
                        text_item_info.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.documentaryanalysis_oragele), null, null, null);
                    } else if (bVoList.get(position).getId().equals("2")) {
                        text_item_info.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.documentaryanalysis_phone), null, null, null);

                    } else if (bVoList.get(position).getId().equals("3")) {
                        text_item_info.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.documentaryanalysis_weixin), null, null, null);

                    } else {
                        text_item_info.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.documentaryanalysis_other), null, null, null);

                    }

                    String str1 = "<font size=\"10\" color=\"#aaaaaa\">" + "合计" + "</font>" + bVoList.get(position).getCount() + "<font size=\"10\" color=\"#aaaaaa\">" + "次"
                            + "</font>";
                    mPiechart_Count.setText(Html.fromHtml(str1));
                    String str2 = "<font size=\"10\" color=\"#aaaaaa\">" + "客户均访" + "</font>" + bVoList.get(position).getPerCustomer() + "<font size=\"10\" color=\"#aaaaaa\">"
                            + "次" + "</font>";
                    mPiechart_Day_Count.setText(Html.fromHtml(str2));
                    mCustomersAdapter.mList.clear();
                    mCustomersAdapter.mList.addAll(bVoList.get(position).getCustomers());
                    mCustomersAdapter.notifyDataSetChanged();
                    if (mBsListView.getVisibility() == View.GONE) {
                        mBsListView.setVisibility(View.VISIBLE);
                        mLinearly.setVisibility(View.VISIBLE);
                    }

                }

            });

        }

        CustomDialog.closeProgressDialog();
    }

    public void createLeftPop() {
        ArrayList<TreeVO> list1 = CommonUtils.getOneLeveTreeVo(mCrmOptionsVO.getArray());
        mBsPopupWindowsTitleDep = new BSPopupWindowsTitle(this, list1, callback, CommonUtils.getScreenHigh(BossStatiscsDocumentaryAnalysisActivity.this) / 3);
    }

    public void initPopData() {
        // 时间筛选
        // ArrayList<TreeVO> list1 = CommonUtils.getOneLeveTreeVo(mTimeArray);
        // mBsPopupWindowsTitle = new BSPopupWindowsTitle(this, list1, callback,
        // ViewGroup.LayoutParams.WRAP_CONTENT);
        if (mTimePop == null) {
            mTimePop = CommonUtils.initPopView(BossStatiscsDocumentaryAnalysisActivity.this, 6, ViewGroup.LayoutParams.WRAP_CONTENT, timeCallback);
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

    // 菜单点击回调函数
    TreeCallBack callback = new TreeCallBack() {

        @Override
        public void callback(TreeVO vo) {
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
        CustomDialog.showProgressDialog(this, "正在加载...");
        new ThreadUtil(this, this).start();
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

    /*
     * (non-Javadoc)
     * @see BaseActivity#executeFailure()
     */
    @Override
    public void executeFailure() {
        // TODO Auto-generated method stub
        CustomDialog.closeProgressDialog();
        if (null == bVo) {
            super.showNoNetView();
            return;
        } else {
            mContentLayout.setVisibility(View.GONE);
            mLoadingLayout.setVisibility(View.VISIBLE);
            CommonUtils.setNonetContent(this, mLoading, "没有相关信息");
        }
    }

    /*
     * (non-Javadoc)
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.title01:
                if (mBsPopupWindowsTitleDep != null) {
                    selectOne = 0;
                    mBsPopupWindowsTitleDep.showPopupWindow(mOneTitle);
                }
                break;
            case R.id.title02:
                // if (mBsPopupWindowsTitle != null) {
                // selectOne = 1;
                // mBsPopupWindowsTitle.showPopupWindow(mOneTitle);
                // }
                if (!mTimePop.isShowing()) {
                    mTimePop.showAsDropDown(mOneTitle);
                } else {
                    mTimePop.dismiss();
                }
                break;

            case R.id.txt_comm_head_right:
                Intent i = new Intent();
                i.putExtra("mVokey", mCrmOptionsVO);
                i.setClass(BossStatiscsDocumentaryAnalysisActivity.this, BossStatiscsDocumentaryStatisticsActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }

}
