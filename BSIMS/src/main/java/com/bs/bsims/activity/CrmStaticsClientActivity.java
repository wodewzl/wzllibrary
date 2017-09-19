
package com.bs.bsims.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.CrmStaticsClientAdpter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.CrmStaticsClinetVO;
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

public class CrmStaticsClientActivity extends BaseActivity implements LineChartOnValueSelectListener, OnClickListener, OnItemClickListener {

    // 折线图的一些参数
    private LineChartView mLineChart;
    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
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
    private List<CrmStaticsClinetVO> mChartLine = new ArrayList<CrmStaticsClinetVO>();

    private CrmStaticsClinetVO mCrmStaticsClinetVO, mLineVO;
    private BSListView mListView;
    private CrmStaticsClientAdpter mAdapter;
    private TextView mRepamentMoneyTv, mNeedMoneyTv;
    private TextView mTargetMoneyTv, mSiginMoneyTv, mRepaymentMoneyTv;
    private TextView mDateTv;
    private String mDid, mUid, mDate;
    private TextView mNumberTv;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.crm_statics_client_activity, mContentLayout);
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
        final CrmStaticsClinetVO vo = mCrmStaticsClinetVO.getInfo();
        if (vo.getChart() != null) {
            mChartLine = vo.getChart();
            initLineChartData(mCrmStaticsClinetVO);
        }

        CommonUtils.setTextThree(this, mDateTv, vo.getYear() + "年 ", vo.getMonth(), " 月",
                R.color.C4, 1.5f);
        mDateTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.miss_date, 0, 0, 0);
        mDateTv.setCompoundDrawablePadding(CommonUtils.dip2px(this, 5));
        CommonUtils.setTextTwoBefore(this, mNumberTv, vo.getAllnum(), "个", R.color.C10, 1.5f);
        mNumberTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.boss_static_icon_people, 0,
                0, 0);
        mNumberTv.setCompoundDrawablePadding(CommonUtils.dip2px(this, 5));

        mListView.showHead(this, false);

        if (vo.getShow() == null) {
            mListView.setVisibility(View.GONE);
            this.findViewById(R.id.list_title_tv).setVisibility(View.GONE);
            // this.findViewById(R.id.divider_tv).setVisibility(View.GONE);
        } else {
            mListView.setVisibility(View.VISIBLE);
            this.findViewById(R.id.list_title_tv).setVisibility(View.VISIBLE);
            // this.findViewById(R.id.divider_tv).setVisibility(View.VISIBLE);
            mAdapter.updateData(vo.getShow(), "1");
        }
    }

    @Override
    public void executeFailure() {
        super.executeFailure();
    }

    @Override
    public void initView() {
        mTitleTv.setText("客户分析");
        mOkTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.crm_select_icon, 0, 0, 0);
        mNumberTv = (TextView) findViewById(R.id.number_tv);
        mDateTv = (TextView) findViewById(R.id.date_tv);
        mListView = (BSListView) findViewById(R.id.list_view);
        mAdapter = new CrmStaticsClientAdpter(this);
        mListView.setAdapter(mAdapter);
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
        mOkTv.setOnClickListener(this);
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
            String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.CRM_STATICS_CLIENT, mapList);
            mCrmStaticsClinetVO = gson.fromJson(jsonStrList, CrmStaticsClinetVO.class);
            if (Constant.RESULT_CODE.equals(mCrmStaticsClinetVO.getCode())) {
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
        mLineData.setBaseValue(Float.NEGATIVE_INFINITY);
        mLineChart.setLineChartData(mLineData);
        mLineChart.setViewportCalculationEnabled(false);
        mLineChart.setValueSelectionEnabled(true);
        // 设置y轴最大值
        // float maxChart = Float.valueOf((vo.getMaxchart()));
        float maxChart = 0.0f;
        if ("".equals(vo.getInfo().getMaxchart())) {
            maxChart = 0.0f;
        } else {
            maxChart = Float.valueOf((vo.getInfo().getMaxchart()));
        }
        if (maxChart <= 15) {
            mLineChart.getCurrentViewport().top = 15;
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
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(this, CrmPeopleAddDepartSelectActivity.class);
        startActivityForResult(intent, 2015);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 2015) {
            if (data == null)
                return;
            mDid = data.getStringExtra("did");
            mUid = data.getStringExtra("userid");
            mDate = "";
            mListView.showHead(this, true);
            new ThreadUtil(this, this).start();
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
