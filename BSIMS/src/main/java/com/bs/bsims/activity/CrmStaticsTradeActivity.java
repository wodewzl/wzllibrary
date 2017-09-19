
package com.bs.bsims.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.CrmStaticsClientAdpter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.BossStatisticsAttendanceVO;
import com.bs.bsims.model.CrmStaticsClinetVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSListView;
import com.bs.bsims.view.BSTopIndicator;
import com.bs.bsims.view.BSTopIndicator.OnTopIndicatorListener;
import com.bs.bsims.view.chart.Axis;
import com.bs.bsims.view.chart.AxisValue;
import com.bs.bsims.view.chart.Line;
import com.bs.bsims.view.chart.LineChartData;
import com.bs.bsims.view.chart.LineChartOnValueSelectListener;
import com.bs.bsims.view.chart.LineChartView;
import com.bs.bsims.view.chart.PointValue;
import com.bs.bsims.view.chart.ValueShape;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CrmStaticsTradeActivity extends BaseActivity implements LineChartOnValueSelectListener, OnClickListener, OnTopIndicatorListener, OnItemClickListener {

    private LinearLayout mDepartLayout01, mDepartLayout02;
    // 定义折线图相关变量
    private List<CrmStaticsClinetVO> mChartLine = new ArrayList<CrmStaticsClinetVO>();
    private LineChartView mLineChart;
    private LineChartData mLineData;
    private boolean hasAxes = true;
    private boolean hasLines = true;
    private boolean hasPoints = true;
    private ValueShape shape = ValueShape.CIRCLE;
    private boolean isFilled = false;
    private boolean hasLabels = true;
    private boolean isCubic = false;
    private boolean hasLabelForSelected = true;
    private int[] mColors = new int[] {
            Color.rgb(0, 166, 255), Color.rgb(255, 171, 0), Color.rgb(252, 0, 0),
            Color.rgb(0, 154, 37), Color.rgb(180, 180, 180)
    };

    private List<String> mStatusList = new ArrayList<String>();

    private TextView mTitle01, mTitle02, mTitle03;
    private TextView mSginMoneyTv, mRepaymentMoneyTv;
    private BSTopIndicator mTopIndicator;
    private int[] mDrawableIds = new int[] {
            0, 0
    };
    private CharSequence[] mLabels = new CharSequence[] {
            "签单金额", "签单量"
    };
    private CrmStaticsClinetVO mCrmStaticsTradeMoneyVO, mCrmStaticsTradeCountVO, mCrmStaticsTradeCurrentVO;
    private BSListView mListView;
    private CrmStaticsClientAdpter mAdapter;
    private TextView mDateTv;
    private String mDid, mUid, mDate, mType = "1";
    private LinearLayout mCenterLayout01, mCenterLayout02;
    private TextView mListViewTiteTv;
    private TextView mMenuTv, mHeadBack;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.crm_statics_trade_activity, mContentLayout);
        baseHeadLayout.setBackgroundColor(getResources().getColor(R.color.translucent));
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    public boolean getData() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> mapList = new HashMap<String, String>();
            mapList.put("userid", BSApplication.getInstance().getUserId());
            mapList.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            mapList.put("did", mDid);
            mapList.put("uid", mUid);
            mapList.put("date", mDate);
            mapList.put("type", mType);
            String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.CRM_STATICS_TRADE, mapList);

            mCrmStaticsTradeCurrentVO = gson.fromJson(jsonStrList, CrmStaticsClinetVO.class);
            if (Constant.RESULT_CODE.equals(mCrmStaticsTradeCurrentVO.getCode())) {
                return true;
            }

            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
        }
    }

    @Override
    public void executeSuccess() {
        super.executeSuccess();
        baseHeadLayout.setBackgroundColor(Color.parseColor("#336A91"));
        mListView.showHead(this, false);

        if ("1".equals(mCrmStaticsTradeCurrentVO.getInfo().getType())) {
            mCrmStaticsTradeMoneyVO = mCrmStaticsTradeCurrentVO;
            mType = "1";
            updateData("1");
        } else {
            mCrmStaticsTradeCountVO = mCrmStaticsTradeCurrentVO;
            mType = "2";
            updateData("2");
        }

    }

    public void updateData(String type) {
        // CrmStaticsClinetVO vo = mCrmStaticsTradeCurrentVO.getInfo();

        if ("1".equals(type)) {
            CrmStaticsClinetVO vo = mCrmStaticsTradeMoneyVO.getInfo();
            if (vo.getChart() != null) {
                mChartLine = vo.getChart();
                mTitle01.setText(CommonUtils.countNumberSecond(vo.getTotalMoney()));
                mTitle02.setText(vo.getTotalNum());
                mTitle03.setText(CommonUtils.countNumberSecond(vo.getTotalPayment()));
                initLineChartData(mCrmStaticsTradeMoneyVO);
            }
            // mSginMoneyTv.setText(vo.getMonthMoney());
            if (CommonUtils.countNumberSecond(vo.getMonthMoney()).contains("万")) {
                CommonUtils.setTextTwoBefore(this, mSginMoneyTv, "￥" + CommonUtils.countNumberSecond(vo.getMonthMoney()).split("万")[0], " 万", R.color.C7, 1.3f);
            } else if (CommonUtils.countNumberSecond(vo.getMonthMoney()).contains("亿")) {
                CommonUtils.setTextTwoBefore(this, mSginMoneyTv, "￥" + CommonUtils.countNumberSecond(vo.getMonthMoney()).split("亿")[0], " 亿", R.color.C7, 1.3f);

            } else {
                CommonUtils.setTextTwoBefore(this, mSginMoneyTv, "￥" + CommonUtils.countNumberSecond(vo.getMonthMoney()), "  ", R.color.C7, 1.3f);
            }
            if (CommonUtils.countNumberSecond(vo.getMonthPayment()).contains("万")) {
                CommonUtils.setTextTwoBefore(this, mRepaymentMoneyTv, "￥" + CommonUtils.countNumberSecond(vo.getMonthPayment()).split("万")[0], " 万", R.color.C10, 1.3f);
            } else if (CommonUtils.countNumberSecond(vo.getMonthPayment()).contains("亿")) {
                CommonUtils.setTextTwoBefore(this, mRepaymentMoneyTv, "￥" + CommonUtils.countNumberSecond(vo.getMonthPayment()).split("亿")[0], " 亿", R.color.C10, 1.3f);
            } else {
                // mRepaymentMoneyTv.setText("￥" +
                CommonUtils.setTextTwoBefore(this, mRepaymentMoneyTv, "￥" + CommonUtils.countNumberSecond(vo.getMonthPayment()), "  ", R.color.C10, 1.3f);
            }

            if (vo.getNext() == null) {
                mListView.setVisibility(View.GONE);
                this.findViewById(R.id.list_title_tv).setVisibility(View.GONE);
                this.findViewById(R.id.divider_tv).setVisibility(View.GONE);
            } else {
                mListView.setVisibility(View.VISIBLE);
                this.findViewById(R.id.list_title_tv).setVisibility(View.VISIBLE);
                this.findViewById(R.id.divider_tv).setVisibility(View.VISIBLE);
                mAdapter.updateData(vo.getNext(), "2");
            }

            CommonUtils.setTextThree(this, mDateTv, vo.getMonth().split("-")[0] + "年 ", vo.getMonth().split("-")[1], " 月", R.color.C4, 1.5f);
            mDateTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.miss_date, 0, 0, 0);
            mDateTv.setCompoundDrawablePadding(CommonUtils.dip2px(this, 5));
        } else {

            CrmStaticsClinetVO vo = mCrmStaticsTradeCountVO.getInfo();
            if (vo.getChart() != null) {
                mChartLine = vo.getChart();
                mTitle01.setText(CommonUtils.countNumberSecond(vo.getTotalMoney()));
                mTitle02.setText(vo.getTotalNum());
                mTitle03.setText(CommonUtils.countNumberSecond(vo.getTotalPayment()));
                initLineChartData(mCrmStaticsTradeCountVO);
            }
            // mSginMoneyTv.setText(vo.getMonthMoney());
            // mRepaymentMoneyTv.setText(vo.getMonthPayment());

            if (vo.getNext() == null) {
                mListView.setVisibility(View.GONE);
                this.findViewById(R.id.list_title_tv).setVisibility(View.GONE);
            } else {
                mListView.setVisibility(View.VISIBLE);
                this.findViewById(R.id.list_title_tv).setVisibility(View.VISIBLE);
                mAdapter.updateData(vo.getNext(), "1");
            }
            CommonUtils.setTextThree(this, mDateTv, vo.getMonth().split("-")[0] + "年 ", vo.getMonth().split("-")[1], " 月", R.color.C4, 1.5f);
            mDateTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.miss_date, 0, 0, 0);
            mDateTv.setCompoundDrawablePadding(CommonUtils.dip2px(this, 5));
        }
    }

    @Override
    public void executeFailure() {
        mLoadingLayout.setVisibility(View.VISIBLE);
        mContentLayout.setVisibility(View.GONE);
        if (mCrmStaticsTradeMoneyVO == null) {
            super.showNoNetView();
        } else {
            super.showNoContentView();
        }

    }

    @Override
    public void initView() {
        mHeadBack = (TextView) findViewById(R.id.head_back);
        mHeadLayout.setVisibility(View.GONE);
        mMenuTv = (TextView) findViewById(R.id.menu_tv);
        mTitle01 = (TextView) findViewById(R.id.title_01);
        mTitle02 = (TextView) findViewById(R.id.title_02);
        mTitle03 = (TextView) findViewById(R.id.title_03);
        mTopIndicator = (BSTopIndicator) findViewById(R.id.top_indicator);
        mTopIndicator.setmLabels(mLabels);
        mTopIndicator.setmDrawableIds(mDrawableIds);
        mTopIndicator.updateUI(this);
        mSginMoneyTv = (TextView) findViewById(R.id.sgin_money_tv);
        mRepaymentMoneyTv = (TextView) findViewById(R.id.repayment_money_tv);
        mDepartLayout01 = (LinearLayout) findViewById(R.id.depart_layout01);
        mDepartLayout02 = (LinearLayout) findViewById(R.id.depart_layout02);
        mDateTv = (TextView) findViewById(R.id.date_tv);
        mCenterLayout01 = (LinearLayout) findViewById(R.id.center_layout_01);
        mCenterLayout02 = (LinearLayout) findViewById(R.id.center_layout_02);
        mListViewTiteTv = (TextView) findViewById(R.id.list_title_tv);
        mListView = (BSListView) findViewById(R.id.list_view);
        mAdapter = new CrmStaticsClientAdpter(CrmStaticsTradeActivity.this);
        mListView.setAdapter(mAdapter);
        initLineChartView();
        initData();
    }

    public void initData() {
        Intent intent = this.getIntent();
        mUid = intent.getStringExtra("uid");
        mDid = intent.getStringExtra("did");
    }

    public void initLineChartView() {
        mLineChart = (LineChartView) this.findViewById(R.id.chart);
        mLineChart.setOnValueTouchListener(this);
        mLineChart.setValueSelectionEnabled(true);
    }

    @Override
    public void bindViewsListener() {
        mTopIndicator.setOnTopIndicatorListener(this);
        mMenuTv.setOnClickListener(this);
        mHeadBack.setOnClickListener(this);
        mListView.setOnItemClickListener(this);
    }

    // 折线图开始：
    private void initLineChartData(CrmStaticsClinetVO vo) {
        List<CrmStaticsClinetVO> chartLine = vo.getInfo().getChart();
        List<Line> lines = new ArrayList<Line>();
        for (int i = 0; i < chartLine.size(); ++i) {
            List<CrmStaticsClinetVO> lineValues = chartLine.get(i).getList();
            List<PointValue> values = new ArrayList<PointValue>();
            for (int j = 0; j < lineValues.size(); ++j) {
                values.add(new PointValue(j, 0));
            }

            Line line = new Line(values);
            line.setColor(mColors[i]);
            line.setShape(shape);
            line.setCubic(isCubic);
            line.setFilled(isFilled);
            line.setHasLabels(hasLabels);
            line.setHasLabelsOnlyForSelected(hasLabelForSelected);
            line.setHasLines(hasLines);
            line.setHasPoints(hasPoints);
            line.setPointColor(mColors[i]);
            line.setPointRadius(4);
            line.setStrokeWidth(2);
            line.setHasLines(true);
            lines.add(line);
        }

        mLineData = new LineChartData(lines);

        if (hasAxes) {
            Axis axisX = new Axis();
            List<AxisValue> namewValues = new ArrayList<AxisValue>();// 保存横坐标名称
            for (int i = 0; i < chartLine.get(0).getList().size(); i++) {
                namewValues.add(new AxisValue(i).setLabel(chartLine.get(0).getList().get(i).getDate()));
            }
            axisX.setValues(namewValues);
            Axis axisY = new Axis().setHasLines(true);
            mLineData.setAxisXBottom(axisX);
            mLineData.setAxisYLeft(axisY);
        } else {
            mLineData.setAxisXBottom(null);
            mLineData.setAxisYLeft(null);
        }
        mLineChart.setValueSelectionEnabled(true);
        mLineData.setBaseValue(Float.NEGATIVE_INFINITY);
        mLineChart.setLineChartData(mLineData);

        // 设置y轴最大值
        mLineChart.setZoomEnabled(false);
        mLineChart.setClickable(false);
        mLineChart.setScrollEnabled(false);
        mLineChart.setViewportCalculationEnabled(false);

        float maxChart = 0.0f;
        if ("".equals(vo.getInfo().getMaxchart())) {
            maxChart = 0.0f;
        } else {
            maxChart = Float.valueOf((vo.getInfo().getMaxchart()));
        }

        if (maxChart <= 12) {
            mLineChart.getCurrentViewport().top = 12;
        } else {
            mLineChart.getCurrentViewport().top = maxChart + maxChart / 6;
        }
        mLineChart.getCurrentViewport().bottom = 0;

        int count = 0;// 统计实际存在的折线条数
        for (int i = 0; i < chartLine.size(); ++i) {
            List<CrmStaticsClinetVO> lineValues = chartLine.get(i).getList();
            for (int j = 0; j < lineValues.size(); ++j) {
                float num = Float.parseFloat(lineValues.get(j).getNum());
                PointValue value = mLineData.getLines().get(count).getValues().get(j);
                value.setTarget(value.getX(), num);
            }
            count++;
        }
        mLineChart.startDataAnimation(1000);
    }

    // 折线图结束

    @Override
    public void onValueDeselected() {

    }

    @Override
    public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
        mListView.showHead(this, true);
        mDate = mChartLine.get(0).getList().get(pointIndex).getDateTime();
        new ThreadUtil(this, this).start();
    }

    public boolean getMonthData() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> mapList = new HashMap<String, String>();
            mapList.put("userid", BSApplication.getInstance().getUserId());
            mapList.put("did", mDid);
            mapList.put("uid", mUid);
            mapList.put("date", mDate);
            mapList.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle()
                    + Constant.BOSS_STATISTICS_TASK_MONTH_STATISTICS, mapList);

            mCrmStaticsTradeMoneyVO = gson.fromJson(jsonStrList, CrmStaticsClinetVO.class);
            if (Constant.RESULT_CODE.equals(mCrmStaticsTradeMoneyVO.getCode())) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
        }
    }

    public List<BossStatisticsAttendanceVO> refreshListData(List<BossStatisticsAttendanceVO> vo) {
        List<BossStatisticsAttendanceVO> mList = new ArrayList<BossStatisticsAttendanceVO>();
        for (int i = 0; i < vo.size(); i++) {
            if (mStatusList.contains(vo.get(i).getStatus())) {
                mList.add(vo.get(i));
            }
        }
        return mList;
    }

    public String getCurrentNumber(BossStatisticsAttendanceVO vo, int sort) {
        List<BossStatisticsAttendanceVO> list = vo.getInfo().getChart();
        int allCount = 0;
        for (int i = 0; i < list.size(); i++) {
            allCount += Integer.parseInt(list.get(i).getList().get(sort).getNum());
        }
        return allCount + "";
    }

    @Override
    public void onIndicatorSelected(int index) {
        mTopIndicator.setTabsDisplay(this, index);
        if (index == 0) {
            mType = "1";
            mDepartLayout01.setVisibility(View.VISIBLE);
            mDepartLayout02.setVisibility(View.VISIBLE);
            mCenterLayout01.setVisibility(View.VISIBLE);
            mCenterLayout02.setVisibility(View.VISIBLE);
            mListViewTiteTv.setText("签约排行");
            if (mCrmStaticsTradeMoneyVO == null) {
                mListView.showHead(this, true);
                new ThreadUtil(this, this).start();
            } else {
                updateData(mType);
            }
        } else {
            mType = "2";
            mDepartLayout01.setVisibility(View.GONE);
            mDepartLayout02.setVisibility(View.GONE);
            mCenterLayout01.setVisibility(View.GONE);
            mCenterLayout02.setVisibility(View.GONE);
            mListViewTiteTv.setText("新增合同排行");
            if (mCrmStaticsTradeCountVO == null) {
                mListView.showHead(this, true);
                new ThreadUtil(this, this).start();
            } else {
                updateData(mType);
            }
        }

    }

    @Override
    public void updateUi() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 2015) {
            if (data == null)
                return;
            mDate = "";
            mDid = data.getStringExtra("did");
            mUid = data.getStringExtra("userid");
            mListView.showHead(this, true);
            new ThreadUtil(this, this).start();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_tv:
                Intent intent = new Intent();
                intent.setClass(this, CrmPeopleAddDepartSelectActivity.class);
                startActivityForResult(intent, 2015);
                break;
            case R.id.head_back:
                this.finish();
                break;

            default:
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> listView, View arg1, int arg2, long arg3) {
        CrmStaticsClinetVO vo = (CrmStaticsClinetVO) listView.getAdapter().getItem(arg2);
        Intent intent = new Intent();
        intent.putExtra("uid", vo.getUserid());
        intent.putExtra("date", mDate);
        intent.putExtra("type", "1");
        intent.setClass(this, SalesmanHomeActivity.class);
        startActivity(intent);
    }

}
