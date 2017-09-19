
package com.bs.bsims.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;

import com.bs.bsims.R;
import com.bs.bsims.adapter.BossStatisticsAttendanceAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.BossStatisticsAttendanceVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CommonUtils.ResultCallback;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSDialog;
import com.bs.bsims.view.BSListView;
import com.bs.bsims.view.chart.Axis;
import com.bs.bsims.view.chart.AxisValue;
import com.bs.bsims.view.chart.Column;
import com.bs.bsims.view.chart.ColumnChartData;
import com.bs.bsims.view.chart.ColumnChartView;
import com.bs.bsims.view.chart.SubcolumnValue;
import com.bs.bsims.view.chart.Viewport;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BossStatisticsAttendanceRankActivity extends BaseActivity implements OnClickListener {

    private ColumnChartView mColumnChartView;
    private ColumnChartData mColumnChartData;;
    private boolean hasLabels = true;// 控制是否在条条上显示对应的值
    private boolean hasLabelForSelected = false;

    private String mType = "1";
    private String mDate;
    private BossStatisticsAttendanceVO mRankAttendanceVO;
    private BSListView mListView;
    private BossStatisticsAttendanceAdapter mAdapter;
    private BSDialog mBSDialog;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.boss_statistics_attendance_rank, mContentLayout);
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
        mListView.showHead(this, false);
        mListView.setVisibility(View.VISIBLE);
        List<BossStatisticsAttendanceVO> list = mRankAttendanceVO.getInfo().getShow();
        List<BossStatisticsAttendanceVO> chartList = mRankAttendanceVO.getInfo().getChart();

        if (chartList != null) {
            intColumnData(chartList);
        } else {
            initNoColumnData();
            mListView.setVisibility(View.GONE);
        }
        mAdapter.updateData(list, "2");

    }

    @Override
    public void executeFailure() {
        super.executeFailure();
        // 列表展示的时候不能调用父类
        super.isRequestFinish();
        mAdapter.notifyDataSetChanged();
        mListView.showHead(this, false);
        // 不适合只隐藏列表，适合隐藏整个布局;
        if (mRankAttendanceVO == null) {
            super.showNoNetView();
        } else {
            mAdapter.updateData(null, "3");
        }
    }

    @Override
    public void initView() {
        mOkTv.setTextSize(16);
        mOkTv.setText(DateUtils.getCurrentDate11113());
        mListView = (BSListView) findViewById(R.id.list_view);
        mListView.showHead(BossStatisticsAttendanceRankActivity.this, true);
        mAdapter = new BossStatisticsAttendanceAdapter(this);
        mListView.setAdapter(mAdapter);
        initColumnChartView();
        initData();
    }

    public void initData() {
        Intent intent = this.getIntent();
        String titleName = intent.getStringExtra("title_name");
        mTitleTv.setText(titleName);
        if (intent.getStringExtra("type") != null)
            mType = intent.getStringExtra("type");
    }

    private void initColumnChartView() {
        mColumnChartView = (ColumnChartView) findViewById(R.id.chart);
        mColumnChartView.setZoomEnabled(false);
        mColumnChartView.setClickable(false);
        mColumnChartView.setScrollEnabled(false);
    }

    private void intColumnData(List<BossStatisticsAttendanceVO> chartList) {
        // int mColor = this.getResources().getColor(R.color.C9);// 条形图颜色

        int color = Color.rgb(0, 168, 254);// 条形图颜色
        int numColumns = chartList.size();// 条形图中条条个数
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        List<Axis> nameValue;
        List<AxisValue> axisXValues = new ArrayList<AxisValue>();// 保存横坐标名称
        for (int i = 0; i < numColumns; ++i) {
            values = new ArrayList<SubcolumnValue>();
            nameValue = new ArrayList<Axis>();
            // values.add(new SubcolumnValue((float) Math.random() * 50f + 5,
            // ChartUtils.pickColor()));
            // 纵坐标设值
            values.add(new SubcolumnValue(0.0f, color));
            // 横坐标设值
            axisXValues.add(new AxisValue(i).setLabel(chartList.get(i).getFullname()));
            Column column = new Column(values);
            column.setHasLabels(hasLabels);
            column.setHasLabelsOnlyForSelected(hasLabelForSelected);
            columns.add(column);
        }
        mColumnChartData = new ColumnChartData(columns);

        // 隐藏不显示坐标名称
        Axis axisX = new Axis();
        axisX.setHasTiltedLabels(true);// 设置文字是否倾斜
        axisX.setValues(axisXValues);// 显示横坐标各个值
        axisX.setMaxLabelChars(4);
        Axis axisY = new Axis().setHasLines(true);
        mColumnChartData.setAxisXBottom(axisX);
        mColumnChartData.setAxisYLeft(axisY);
        mColumnChartView.setColumnChartData(mColumnChartData);
        mColumnChartView.setViewportCalculationEnabled(false);

        resetViewport(numColumns);
        mColumnChartView.getCurrentViewport().top = Float.parseFloat(chartList.get(0).getNum()) + Integer.parseInt(chartList.get(0).getNum()) / 4;
        mColumnChartView.getCurrentViewport().bottom = 0;
        if (mColumnChartView.getCurrentViewport().top < 15)
            mColumnChartView.getCurrentViewport().top = 20;

        prepareDataAnimation(chartList);
        if (numColumns > 5)
            mColumnChartView.startDataAnimation(3000);
        else
            mColumnChartView.startDataAnimation();

    }

    private void initNoColumnData() {
        // int mColor = this.getResources().getColor(R.color.C9);// 条形图颜色
        int numColumns = 10;// 条形图中条条个数
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        List<Axis> nameValue;
        List<AxisValue> axisXValues = new ArrayList<AxisValue>();// 保存横坐标名称
        for (int i = 0; i < numColumns; ++i) {
            values = new ArrayList<SubcolumnValue>();
            nameValue = new ArrayList<Axis>();
            // 纵坐标设值
            values.add(new SubcolumnValue(new SubcolumnValue((float) Math.random() * 50f + 5, this.getResources().getColor(R.color.C3))));
            // 横坐标设值
            // axisXValues.add(new AxisValue(i).setLabel("暂无"));
            Column column = new Column(values);
            column.setHasLabels(false);
            column.setHasLabelsOnlyForSelected(hasLabelForSelected);
            columns.add(column);
        }
        mColumnChartData = new ColumnChartData(columns);

        // 隐藏不显示坐标名称
        Axis axisX = new Axis();
        axisX.setName("没有数据哦");
        axisX.setHasTiltedLabels(true);// 设置文字是否倾斜
        axisX.setValues(axisXValues);// 显示横坐标各个值

        axisX.setMaxLabelChars(4);

        Axis axisY = new Axis().setHasLines(true);
        mColumnChartData.setAxisXBottom(axisX);
        mColumnChartData.setAxisYLeft(axisY);
        mColumnChartView.setColumnChartData(mColumnChartData);
        mColumnChartView.setViewportCalculationEnabled(false);
        mColumnChartView.setValueSelectionEnabled(true);
        resetViewport(numColumns);
        mColumnChartView.getCurrentViewport().top = 80;
        mColumnChartView.getCurrentViewport().bottom = 0;
    }

    @Override
    public void bindViewsListener() {
        mOkTv.setOnClickListener(this);
    }

    public boolean getData() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> mapList = new HashMap<String, String>();
            mapList.put("userid", BSApplication.getInstance().getUserId());
            mapList.put("type", mType);
            mapList.put("date", mDate);
            mapList.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.BOSS_STATISTICS_ATTENDANCE_RANK, mapList);
            mRankAttendanceVO = gson.fromJson(jsonStrList, BossStatisticsAttendanceVO.class);
            if (Constant.RESULT_CODE.equals(mRankAttendanceVO.getCode()))
                return true;

            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
        }
    }

    ResultCallback callback = new ResultCallback() {
        @Override
        public void callback(String str, int position) {
            mDate = str;
            mOkTv.setText(str);
            mListView.showHead(BossStatisticsAttendanceRankActivity.this, true);
            new ThreadUtil(BossStatisticsAttendanceRankActivity.this, BossStatisticsAttendanceRankActivity.this).start();
        }
    };

    @Override
    public void onClick(View arg0) {
        if (mBSDialog == null) {
            mBSDialog = CommonUtils.initDateViewCallback(this, "请选择时间", mOkTv, 3, callback);
        } else {
            mBSDialog.show();
        }
    }

    public void prepareDataAnimation(List<BossStatisticsAttendanceVO> chartList) {
        for (int i = 0; i < mColumnChartData.getColumns().size(); i++) {
            Column column = mColumnChartData.getColumns().get(i);
            SubcolumnValue value = column.getValues().get(0);
            value.setTarget(Float.valueOf(chartList.get(i).getNum()));
        }
    }

    private void resetViewport(int num) {
        final Viewport v = new Viewport(mColumnChartView.getMaximumViewport());
        v.right = num;
        mColumnChartView.setMaximumViewport(v);
        mColumnChartView.setCurrentViewport(v);
        // mColumnChartView.setCurrentViewportWithAnimation(v);
    }

}
