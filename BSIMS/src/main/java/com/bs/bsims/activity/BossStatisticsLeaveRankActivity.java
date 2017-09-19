
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
import com.bs.bsims.view.BSTopIndicator;
import com.bs.bsims.view.BSTopIndicator.OnTopIndicatorListener;
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

public class BossStatisticsLeaveRankActivity extends BaseActivity implements OnClickListener, OnTopIndicatorListener {

    private ColumnChartView mColumnChartView;
    private ColumnChartData mColumnChartData;;
    private boolean hasLabels = true;// 控制是否在条条上显示对应的值
    private boolean hasLabelForSelected = false;

    private String mType = "1";
    private String mDate;
    // private BossStatisticsAttendanceVO mRankAttendanceVO;
    private BossStatisticsAttendanceVO mAttendanceVO;
    private BossStatisticsAttendanceVO mTimeAttendanceVO;
    private BossStatisticsAttendanceVO mAllAttendanceVO;// 最开始请求时不分种类；
    private BSListView mListView;
    private BossStatisticsAttendanceAdapter mAdapter;
    private BSDialog mBSDialog;
    private BSTopIndicator mTopIndicator;
    private String mMode = "1";// 查询方式1按人，2按次
    private int[] mDrawableIds = new int[] {
            R.drawable.boss_static_bg_number,
            R.drawable.boss_static_bg_time
    };
    private CharSequence[] mLabels = new CharSequence[] {
            "按次", "按时长"
    };
    private List<String> mModeArray = new ArrayList<String>();

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

        mListView.setVisibility(View.VISIBLE);
        mListView.showHead(this, false);
        if ("1".equals(mAllAttendanceVO.getInfo().getMode())) {
            mAttendanceVO = mAllAttendanceVO;
            BossStatisticsAttendanceVO vo = mAttendanceVO.getInfo();
            List<BossStatisticsAttendanceVO> chartList = vo.getShow();
            if (chartList != null) {
                updateData(vo, "2");
            } else {
                initNoColumnData();
                mListView.setVisibility(View.GONE);
            }
        } else {
            mTimeAttendanceVO = mAllAttendanceVO;
            BossStatisticsAttendanceVO vo = mTimeAttendanceVO.getInfo();
            List<BossStatisticsAttendanceVO> chartList = vo.getChart();
            if (chartList != null) {
                updateData(vo, "3");
            } else {
                initNoColumnData();
                mListView.setVisibility(View.GONE);
            }
        }

        if (!mModeArray.contains(mAllAttendanceVO.getInfo().getMode())) {
            mModeArray.add(mAllAttendanceVO.getInfo().getMode());
        }
    }

    public void updateData(BossStatisticsAttendanceVO vo, String status) {

        intColumnData(vo);
        mAdapter.updateData(vo.getShow(), status);
    }

    @Override
    public void executeFailure() {
        super.executeFailure();
        // 列表展示的时候不能调用父类
        super.isRequestFinish();
        mAdapter.notifyDataSetChanged();
        mListView.showHead(this, false);
        mListView.setVisibility(View.VISIBLE);

        // 不适合只隐藏列表，适合隐藏整个布局;
        if (mAllAttendanceVO == null) {
            super.showNoNetView();
        } else {
            mAdapter.updateData(null, "3");
        }
    }

    @Override
    public void initView() {
        mOkTv.setTextSize(16);
        mOkTv.setText(DateUtils.getCurrentDate11113());
        mTopIndicator = (BSTopIndicator) findViewById(R.id.top_indicator);
        mTopIndicator.setVisibility(View.VISIBLE);
        mTopIndicator.setmLabels(mLabels);
        mTopIndicator.setmDrawableIds(mDrawableIds);
        mTopIndicator.updateUI(this);
        mListView = (BSListView) findViewById(R.id.list_view);
        mListView.showHead(BossStatisticsLeaveRankActivity.this, true);
        mAdapter = new BossStatisticsAttendanceAdapter(this);
        mListView.setAdapter(mAdapter);
        mColumnChartView = (ColumnChartView) findViewById(R.id.chart);
        mModeArray.add("1");
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
        mColumnChartView.setZoomEnabled(false);
        mColumnChartView.setClickable(false);
        mColumnChartView.setScrollEnabled(false);
    }

    private void intColumnData(BossStatisticsAttendanceVO vo) {
        // int mColor = this.getResources().getColor(R.color.C9);// 条形图颜色

        List<BossStatisticsAttendanceVO> chartList = vo.getChart();
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
        mColumnChartView.getCurrentViewport().top = Float.parseFloat(chartList.get(0).getNum()) + Float.parseFloat(chartList.get(0).getNum()) / 4;
        mColumnChartView.getCurrentViewport().bottom = 0;
        if (mColumnChartView.getCurrentViewport().top < 15)
            mColumnChartView.getCurrentViewport().top = 20;
        prepareDataAnimation(chartList);
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
        mTopIndicator.setOnTopIndicatorListener(this);
    }

    public boolean getData() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> mapList = new HashMap<String, String>();
            mapList.put("userid", BSApplication.getInstance().getUserId());
            mapList.put("type", mType);
            mapList.put("date", mDate);
            mapList.put("mode", mMode);
            mapList.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String url = "";
            if (!"9".equals(mType)) {
                url = Constant.BOSS_STATISTICS_LEAVE_RANK;
            } else {
                // 9为加班特殊处理
                url = Constant.BOSS_STATISTICS_OVERTIME_RANK;
            }
            String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + url, mapList);
            mAllAttendanceVO = gson.fromJson(jsonStrList, BossStatisticsAttendanceVO.class);
            if (Constant.RESULT_CODE.equals(mAllAttendanceVO.getCode()))
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
            match(2, mDate);
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
    }

    @Override
    public void onIndicatorSelected(int index) {
        mTopIndicator.setTabsDisplay(this, index);
        mMode = (index + 1) + "";
        // 安人，按次，是否刷新数据
        if (!mModeArray.contains(mMode)) {
            match(3, (index + 1) + "");
        } else {
            if (index == 0) {
                if (mAttendanceVO.getInfo().getChart() == null)
                    initNoColumnData();
                else
                    updateData(mAttendanceVO.getInfo(), "2");
            } else {
                if (mTimeAttendanceVO.getInfo().getChart() == null)
                    initNoColumnData();
                else
                    updateData(mTimeAttendanceVO.getInfo(), "3");
            }
        }
    }

    public void match(int key, String value) {
        switch (key) {
            case 1:
                mModeArray.clear();
                break;
            case 2:
                mDate = value;
                mModeArray.clear();
                break;
            case 3:
                mMode = value;
                break;

            default:
                break;
        }
        mListView.showHead(BossStatisticsLeaveRankActivity.this, true);
        new ThreadUtil(this, this).start();
    }

}
