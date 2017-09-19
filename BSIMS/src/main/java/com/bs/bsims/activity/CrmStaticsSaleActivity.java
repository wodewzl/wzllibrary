
package com.bs.bsims.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.CrmStaticSaleAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.CrmStaticsClinetVO;
import com.bs.bsims.model.CrmStaticsSaleVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSListView;
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

public class CrmStaticsSaleActivity extends BaseActivity implements LineChartOnValueSelectListener, OnClickListener, OnItemClickListener {

    // 折线图的一些参数
    private LineChartView mLineChart;
    private boolean hasAxes = true;
    private boolean hasLines = true;
    private boolean hasPoints = true;
    private ValueShape shape = ValueShape.CIRCLE;
    private boolean isFilled = false;
    private boolean hasLabels = true;
    private boolean isCubic = false;
    private boolean hasLabelForSelected = true;
    private int[] mColors = new int[] {
            Color.rgb(0, 169, 254), Color.rgb(255, 174, 30),
            Color.GREEN, Color.RED, Color.rgb(132, 76, 125)
    };
    private LineChartData mLineData;
    private List<CrmStaticsSaleVO> mChartLine = new ArrayList<CrmStaticsSaleVO>();

    private CrmStaticsSaleVO mCrmStaticsSaleVO;
    private BSListView mListView;
    private CrmStaticSaleAdapter mAdapter;
    private TextView mRepamentTotalMoneyTv, mNeedMoneyTv;
    private TextView mTargetMoneyTv, mSiginMoneyTv, mRepamentMoneyTv;
    private TextView mDateTv;
    private String mDid, mUid, mDate;
    private TextView mMenuTv, mHeadBackTv;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.crm_statics_sale_activity, mContentLayout);
        baseHeadLayout.setBackgroundColor(getResources().getColor(R.color.translucent));
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
        baseHeadLayout.setBackgroundColor(Color.parseColor("#336A91"));
        mListView.showHead(this, false);
        CrmStaticsSaleVO vo = mCrmStaticsSaleVO.getInfo();

        if (CommonUtils.countNumberSecond(vo.getTarget()).contains("万")) {
            CommonUtils.setTextTwoBefore(this, mTargetMoneyTv, "￥" + CommonUtils.countNumberSecond(vo.getTarget()).split("万")[0], " 万", R.color.C7, 1.3f);
        } else if (CommonUtils.countNumberSecond(vo.getTarget()).contains("亿")) {
            CommonUtils.setTextTwoBefore(this, mTargetMoneyTv, "￥" + CommonUtils.countNumberSecond(vo.getTarget()).split("亿")[0], " 亿", R.color.C7, 1.3f);
        } else {
            CommonUtils.setTextTwoBefore(this, mTargetMoneyTv, "￥" + CommonUtils.countNumberSecond(vo.getTarget()), "  ", R.color.C7, 1.3f);

            mTargetMoneyTv.setTextColor(this.getResources().getColor(R.color.C7));
        }

        if (CommonUtils.countNumberSecond(vo.getMoney()).contains("万")) {
            CommonUtils.setTextTwoBefore(this, mSiginMoneyTv, "￥" + CommonUtils.countNumberSecond(vo.getMoney()).split("万")[0], " 万", R.color.C10, 1.3f);
        } else if (CommonUtils.countNumberSecond(vo.getMoney()).contains("亿")) {
            CommonUtils.setTextTwoBefore(this, mSiginMoneyTv, "￥" + CommonUtils.countNumberSecond(vo.getMoney()).split("亿")[0], " 亿", R.color.C10, 1.3f);
        } else {
            CommonUtils.setTextTwoBefore(this, mSiginMoneyTv, "￥" + CommonUtils.countNumberSecond(vo.getMoney()), "  ", R.color.C10, 1.3f);
        }

        if (CommonUtils.countNumberSecond(vo.getPayment()).contains("万")) {
            CommonUtils.setTextTwoBefore(this, mRepamentMoneyTv, "￥" + CommonUtils.countNumberSecond(vo.getPayment()).split("万")[0], " 万", R.color.C9, 1.3f);
        } else if (CommonUtils.countNumberSecond(vo.getPayment()).contains("亿")) {
            CommonUtils.setTextTwoBefore(this, mRepamentMoneyTv, "￥" + CommonUtils.countNumberSecond(vo.getPayment()).split("亿")[0], " 亿", R.color.C9, 1.3f);
        } else {
            CommonUtils.setTextTwoBefore(this, mRepamentMoneyTv, "￥" + CommonUtils.countNumberSecond(vo.getPayment()), "  ", R.color.C9, 1.3f);
        }

        if (vo.getChart() != null) {
            mChartLine = vo.getChart();
            initLineChartData(mCrmStaticsSaleVO);
            mRepamentTotalMoneyTv.setText(CommonUtils.countNumberSecond(vo.getTotalPayment()));
            mNeedMoneyTv.setText(CommonUtils.countNumberSecond(vo.getTotalMoney()));
        }

        if (vo.getShow() == null) {
            mListView.setVisibility(View.GONE);
            this.findViewById(R.id.list_title_tv).setVisibility(View.GONE);
            this.findViewById(R.id.divider_tv).setVisibility(View.GONE);
            this.findViewById(R.id.list_title_layout).setVisibility(View.GONE);

        } else {
            mListView.setVisibility(View.VISIBLE);
            this.findViewById(R.id.list_title_tv).setVisibility(View.VISIBLE);
            this.findViewById(R.id.divider_tv).setVisibility(View.VISIBLE);
            this.findViewById(R.id.list_title_layout).setVisibility(View.VISIBLE);
            mAdapter.updateData(vo.getShow(), "1");
        }
        mDateTv = (TextView) findViewById(R.id.date_tv);
        CommonUtils.setTextThree(this, mDateTv, vo.getYear() + "年 ", vo.getMonth(), " 月", R.color.C4, 1.5f);
        mDateTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.miss_date, 0, 0, 0);
        mDateTv.setCompoundDrawablePadding(CommonUtils.dip2px(this, 5));
    }

    @Override
    public void executeFailure() {
        super.executeFailure();
    }

    @Override
    public void initView() {
        mMenuTv = (TextView) findViewById(R.id.menu_tv);
        // mMenuTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.crm_select_icon, 0, 0, 0);
        mHeadBackTv = (TextView) findViewById(R.id.head_back);
        mRepamentTotalMoneyTv = (TextView) findViewById(R.id.repament_total_money);
        mNeedMoneyTv = (TextView) findViewById(R.id.need_money);
        mTargetMoneyTv = (TextView) findViewById(R.id.target_money_tv);
        mSiginMoneyTv = (TextView) findViewById(R.id.sigin_money_tv);
        mRepamentMoneyTv = (TextView) findViewById(R.id.repayment_money_tv);
        mListView = (BSListView) findViewById(R.id.list_view);
        mAdapter = new CrmStaticSaleAdapter(this);
        mListView.setAdapter(mAdapter);
        mHeadLayout.setVisibility(View.GONE);
        initLineChartView();
        initData();
    }

    public void initData() {
        Intent intent = this.getIntent();
        mUid = intent.getStringExtra("uid");
        mDid = intent.getStringExtra("did");
    }

    @Override
    public void bindViewsListener() {
        mMenuTv.setOnClickListener(this);
        mHeadBackTv.setOnClickListener(this);
        mListView.setOnItemClickListener(this);
    }

    public void initLineChartView() {
        mLineChart = (LineChartView) this.findViewById(R.id.chart);
        mLineChart.setOnValueTouchListener(this);
        mLineChart.setZoomEnabled(false);
        mLineChart.setClickable(false);
        mLineChart.setScrollEnabled(false);
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
            String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.CRM_STATICS_SALE, mapList);
            mCrmStaticsSaleVO = gson.fromJson(jsonStrList, CrmStaticsSaleVO.class);
            if (Constant.RESULT_CODE.equals(mCrmStaticsSaleVO.getCode())) {
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
    public void onValueDeselected() {

    }

    @Override
    public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
        mListView.showHead(this, true);
        mDate = mChartLine.get(0).getList().get(pointIndex).getDateTime();
        new ThreadUtil(this, this).start();
    }

    private void initLineChartData(CrmStaticsSaleVO vo) {
        List<CrmStaticsSaleVO> chartLine = vo.getInfo().getChart();
        List<Line> lines = new ArrayList<Line>();
        for (int i = 0; i < chartLine.size(); ++i) {
            List<CrmStaticsClinetVO> lineValues = chartLine.get(i).getList();
            List<PointValue> values = new ArrayList<PointValue>();
            for (int j = 0; j < lineValues.size(); ++j) {
                values.add(new PointValue(j, 0));
            }

            Line line = new Line(values);
            line.setColor(mColors[0]);
            line.setShape(shape);
            line.setCubic(isCubic);
            line.setFilled(isFilled);
            line.setHasLabels(hasLabels);
            line.setHasLabelsOnlyForSelected(hasLabelForSelected);
            line.setHasLines(hasLines);
            line.setHasPoints(hasPoints);
            line.setPointColor(mColors[0]);
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
        mLineData.setBaseValue(Float.NEGATIVE_INFINITY);
        mLineChart.setLineChartData(mLineData);
        mLineChart.setViewportCalculationEnabled(false);
        mLineChart.setValueSelectionEnabled(true);

        // 设置y轴最大值

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
        CrmStaticsSaleVO vo = (CrmStaticsSaleVO) listView.getAdapter().getItem(arg2);
        Intent intent = new Intent();
        intent.putExtra("uid", vo.getUserid());
        intent.putExtra("date", mDate);
        intent.putExtra("type", "1");
        intent.setClass(this, SalesmanHomeActivity.class);
        startActivity(intent);
    }

}
