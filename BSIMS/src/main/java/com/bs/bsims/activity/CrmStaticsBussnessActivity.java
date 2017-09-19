
package com.bs.bsims.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.CrmStaticsBussnessAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.CrmStaticsBussnessVO;
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

public class CrmStaticsBussnessActivity extends BaseActivity implements LineChartOnValueSelectListener, OnClickListener, OnItemClickListener {

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
    private List<CrmStaticsBussnessVO> mChartLine = new ArrayList<CrmStaticsBussnessVO>();

    private CrmStaticsBussnessVO mCrmStaticsBussnessVO, mLineVO;
    private BSListView mListView;
    private CrmStaticsBussnessAdapter mAdapter;
    private TextView mBussnessTotalCountTv, mWinCountTv, mLoseCountTv;
    private TextView mDateTv, mNumberTv;
    private String mDid, mUid, mDate;
    private TextView mMenuTv, mHeadBackTv;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.crm_statics_bussness_activity, mContentLayout);
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
        CrmStaticsBussnessVO vo = mCrmStaticsBussnessVO.getInfo();

        if (vo.getChart() != null) {
            mChartLine = vo.getChart();
            initLineChartData(mCrmStaticsBussnessVO);
            mBussnessTotalCountTv.setText(vo.getTotal());
            mWinCountTv.setText(vo.getWinCount());
            mLoseCountTv.setText(vo.getDropCount());
        }

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
        CommonUtils.setTextThree(this, mDateTv, vo.getYear() + "年 ", vo.getMonth(), " 月", R.color.C4, 1.5f);
        mDateTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.miss_date, 0, 0, 0);
        mDateTv.setCompoundDrawablePadding(CommonUtils.dip2px(this, 5));

        CommonUtils.setTextTwoBefore(this, mNumberTv, vo.getAllnum(), "个", R.color.C10, 1.5f);
        mNumberTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.boss_static_icon_people, 0, 0, 0);
        mNumberTv.setCompoundDrawablePadding(CommonUtils.dip2px(this, 5));
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
        mBussnessTotalCountTv = (TextView) findViewById(R.id.bussness_total_count);
        mWinCountTv = (TextView) findViewById(R.id.win_count);
        mLoseCountTv = (TextView) findViewById(R.id.lose_count);
        mListView = (BSListView) findViewById(R.id.list_view);
        mNumberTv = (TextView) findViewById(R.id.number_tv);
        mDateTv = (TextView) findViewById(R.id.date_tv);
        mAdapter = new CrmStaticsBussnessAdapter(this);
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
            String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.CRM_STATICS_BUESSNESS, mapList);
            mCrmStaticsBussnessVO = gson.fromJson(jsonStrList, CrmStaticsBussnessVO.class);
            if (Constant.RESULT_CODE.equals(mCrmStaticsBussnessVO.getCode())) {
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

    private void initLineChartData(CrmStaticsBussnessVO vo) {
        List<CrmStaticsBussnessVO> chartLine = vo.getInfo().getChart();
        List<Line> lines = new ArrayList<Line>();
        for (int i = 0; i < chartLine.size(); ++i) {
            List<CrmStaticsBussnessVO> lineValues = chartLine.get(i).getList();
            List<PointValue> values = new ArrayList<PointValue>();
            for (int j = 0; j < lineValues.size(); ++j) {
                float num = 0.0f;
                values.add(new PointValue(j, num));
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

        // 准备动画数据
        int count = 0;// 统计实际存在的折线条数
        for (int i = 0; i < chartLine.size(); ++i) {
            List<CrmStaticsBussnessVO> lineValues = chartLine.get(i).getList();
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
        CrmStaticsBussnessVO vo = (CrmStaticsBussnessVO) listView.getAdapter().getItem(arg2);
        Intent intent = new Intent();
        intent.putExtra("uid", vo.getUserid());
        intent.putExtra("date", mDate);
        intent.putExtra("type", "1");
        intent.setClass(this, SalesmanHomeActivity.class);
        startActivity(intent);
    }

}
