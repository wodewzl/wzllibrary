
package com.bs.bsims.activity;

import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.BossStatisticsHighSeasAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.BossStatisticsClientVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CommonUtils.ResultCallback;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSDialog;
import com.bs.bsims.view.BSListView;
import com.bs.bsims.view.chart.Axis;
import com.bs.bsims.view.chart.AxisValue;
import com.bs.bsims.view.chart.Chart;
import com.bs.bsims.view.chart.Column;
import com.bs.bsims.view.chart.ColumnChartData;
import com.bs.bsims.view.chart.ColumnChartView;
import com.bs.bsims.view.chart.SubcolumnValue;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BossStatisticsBussinessActivity extends BaseActivity {
    private BossStatisticsClientVO mBossStatisticsClientVO;
    private List<BossStatisticsClientVO> mCharts1;
    private List<BossStatisticsClientVO> mCharts2;
    private float mCharts1Max;
    private TextView mBussinessRankName;
    private TextView mAllBussiness, mNewAddBussiness, mFailBussiness;
    private BSListView mNewAddList;
    private BossStatisticsHighSeasAdapter mHighseasAdapter;

    // 定义条形图相关变量
    private ColumnChartView mResourceChart;
    private ColumnChartData mData;
    private boolean hasAxes = true;
    private boolean hasAxesNames = false;// 控制是否显示横纵坐标的name（代表的意思）
    private boolean hasLabels = true;// 控制是否在条条上显示对应的值
    private boolean hasLabelForSelected = false;
    private static final int DEFAULT_DATA = 0;
    private int dataType = DEFAULT_DATA;

    // 弹出框
    // private BSPopupWindowsTitle mBSDialog;
    private BSDialog mBSDialog;
    private String mDateType = "";

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.boss_statistics_bussiness_view, mContentLayout);

    }

    @Override
    public boolean getDataResult() {
        // TODO Auto-generated method stub
        return getData();
    }

    private boolean getData() {
        try {
            Gson gson = new Gson();
            Map map = new HashMap<String, String>();
            map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put("date", mDateType);
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.BOSS_STATISTICS_BUSSINESS, map);
            mBossStatisticsClientVO = gson.fromJson(jsonStr,
                    BossStatisticsClientVO.class);

            if (Constant.RESULT_CODE.equals(mBossStatisticsClientVO.getCode())) {
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
        mLoadingLayout.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);
        updateUi();
    }

    @Override
    public void executeFailure() {
        mLoadingLayout.setVisibility(View.VISIBLE);
        mContentLayout.setVisibility(View.GONE);
        if (mBossStatisticsClientVO == null) {
            super.showNoNetView();
        } else {
            super.showNoContentView();
        }

    }

    @Override
    public void updateUi() {
        mBossStatisticsClientVO = mBossStatisticsClientVO.getInfo();
        mAllBussiness.setText(mBossStatisticsClientVO.getTotalCount());
        mNewAddBussiness.setText("+" + mBossStatisticsClientVO.getNewCount());
        mFailBussiness.setText(mBossStatisticsClientVO.getLoseCount());
        mCharts1Max = Float.parseFloat(mBossStatisticsClientVO.getCharts1_max());
        mCharts1 = mBossStatisticsClientVO.getCharts1();
        mCharts2 = mBossStatisticsClientVO.getCharts2();
        if (mBossStatisticsClientVO.getNewCount().equals("0")) {
            mBussinessRankName.setVisibility(View.GONE);
            mNewAddList.setVisibility(View.GONE);
        } else {
            mBussinessRankName.setVisibility(View.VISIBLE);
            mNewAddList.setVisibility(View.VISIBLE);
        }
        mHighseasAdapter.updateData(mCharts2);
        setCulumnChartData();
    }

    public void setCulumnChartData() {
        if (mBossStatisticsClientVO.getNewCount().equals("0")) {
            initNoColumnData();
        } else {
            generateData();
            prepareDataAnimation();
            mResourceChart.startDataAnimation();
        }
    }

    @Override
    public void initView() {
        mTitleTv.setText("商机分析");
        mBussinessRankName = (TextView) findViewById(R.id.bussiness_rank_name);
        mAllBussiness = (TextView) findViewById(R.id.all_bussiness);
        mNewAddBussiness = (TextView) findViewById(R.id.new_add_bussiness);
        mFailBussiness = (TextView) findViewById(R.id.fail_bussiness);
        mResourceChart = (ColumnChartView) findViewById(R.id.resource_chart);
        mNewAddList = (BSListView) findViewById(R.id.new_add_List);
        mHighseasAdapter = new BossStatisticsHighSeasAdapter(BossStatisticsBussinessActivity.this);
        mNewAddList.setAdapter(mHighseasAdapter);

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        // mOkTv.setText(String.valueOf(year) + "-" + String.valueOf(month));
        mOkTv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.cq_timepick), null, null, null);
    }

    @Override
    public void bindViewsListener() {
        mOkTv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // 时间筛选
                if (mBSDialog == null) {
                    mBSDialog = CommonUtils.initDateViewCallback(BossStatisticsBussinessActivity.this, "请选择时间", mOkTv, 3, callback);
                } else {
                    mBSDialog.show();
                }

            }
        });
    }

    ResultCallback callback = new ResultCallback() {

        @Override
        public void callback(String str, int position) {
            mOkTv.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            mOkTv.setText(str);
            mDateType = str;
            new ThreadUtil(BossStatisticsBussinessActivity.this, BossStatisticsBussinessActivity.this).start();

        }
    };

    // 菜单点击回调函数
    // TreeCallBack callback = new TreeCallBack() {
    //
    // @Override
    // public void callback(TreeVO vo) {
    // // 审批一级菜单
    // mOkTv.setText(vo.getName());
    // mDateType = vo.getSearchId();
    // new ThreadUtil(BossStatisticsBussinessActivity.this,
    // BossStatisticsBussinessActivity.this).start();
    //
    // }
    // };

    // 开始：条形图相关方法设置
    private void reset() {
        hasAxes = true;
        hasAxesNames = true;
        hasLabels = false;
        hasLabelForSelected = false;
        dataType = DEFAULT_DATA;
        mResourceChart.setValueSelectionEnabled(hasLabelForSelected);

    }

    private void generateDefaultData() {
        int mColor = Color.rgb(0, 168, 254);// 条形图颜色
        int numColumns = 6;// 条形图中条条个数
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8
        // columns.
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
            values.add(new SubcolumnValue(0, mColor));
            // 横坐标设值
            axisXValues.add(new AxisValue(i).setLabel(mCharts1.get(i).getName()));

            Column column = new Column(values);
            column.setHasLabels(hasLabels);
            column.setHasLabelsOnlyForSelected(hasLabelForSelected);
            columns.add(column);
        }

        mData = new ColumnChartData(columns);

        // 隐藏不显示坐标名称
        if (hasAxes) {
            Axis axisX = new Axis();
            axisX.setHasTiltedLabels(true);// 设置文字是否倾斜
            axisX.setValues(axisXValues);// 显示横坐标各个值
            axisX.setMaxLabelChars(6);
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("Axis X");
                axisY.setName("Axis Y");
            }
            mData.setAxisXBottom(axisX);
            mData.setAxisYLeft(axisY);
        } else {
            mData.setAxisXBottom(null);
            mData.setAxisYLeft(null);
        }

        mResourceChart.setColumnChartData(mData);
        mResourceChart.setZoomEnabled(false);
        mResourceChart.setClickable(false);
        mResourceChart.setScrollEnabled(false);
        mResourceChart.setViewportCalculationEnabled(false);
        if (mCharts1Max < 15) {
            mResourceChart.getCurrentViewport().top = 20;
        } else {
            mResourceChart.getCurrentViewport().top = mCharts1Max + mCharts1Max / 5;
        }
    }

    private void initNoColumnData() {
        int numColumns = 6;// 条形图中条条个数
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        List<Axis> nameValue;
        List<AxisValue> axisXValues = new ArrayList<AxisValue>();// 保存横坐标名称
        for (int i = 0; i < numColumns; ++i) {

            values = new ArrayList<SubcolumnValue>();
            nameValue = new ArrayList<Axis>();
            // 纵坐标设值
            values.add(new SubcolumnValue((float) Math.random() * 50f + 5, this.getResources().getColor(R.color.C3)));
            // // 横坐标设值
            axisXValues.add(new AxisValue(i).setLabel(""));

            Column column = new Column(values);
            column.setHasLabels(false);
            column.setHasLabelsOnlyForSelected(hasLabelForSelected);
            columns.add(column);
        }

        mData = new ColumnChartData(columns);

        // 隐藏不显示坐标名称
        if (hasAxes) {
            Axis axisX = new Axis();
            axisX.setHasTiltedLabels(true);// 设置文字是否倾斜
            axisX.setValues(axisXValues);// 显示横坐标各个值
            // axisX.setMaxLabelChars(6);
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("暂无");
            }
            mData.setAxisXBottom(axisX);
            mData.setAxisYLeft(axisY);
        } else {
            mData.setAxisXBottom(null);
            mData.setAxisYLeft(null);
        }
        mResourceChart.setColumnChartData(mData);
        mResourceChart.setZoomEnabled(false);
        mResourceChart.setClickable(false);
        mResourceChart.setScrollEnabled(false);
        mResourceChart.setViewportCalculationEnabled(false);
        mResourceChart.getCurrentViewport().top = 100;
    }

    private int getSign() {
        int[] sign = new int[] {
                -1, 1
        };
        return sign[Math.round((float) Math.random())];
    }

    private void generateData() {
        generateDefaultData();
    }

    private void toggleLabels() {
        hasLabels = !hasLabels;

        if (hasLabels) {
            hasLabelForSelected = false;
            mResourceChart.setValueSelectionEnabled(hasLabelForSelected);
        }

        generateData();
    }

    private void toggleLabelForSelected() {
        hasLabelForSelected = !hasLabelForSelected;
        mResourceChart.setValueSelectionEnabled(hasLabelForSelected);

        if (hasLabelForSelected) {
            hasLabels = false;
        }

        generateData();
    }

    private void toggleAxes() {
        hasAxes = !hasAxes;

        generateData();
    }

    private void toggleAxesNames() {
        hasAxesNames = !hasAxesNames;

        generateData();
    }

    /**
     * To animate values you have to change targets values and then call
     * {@link Chart#startDataAnimation()} method(don't confuse with View.animate()).
     */
    private void prepareDataAnimation() {
        for (int i = 0; i < mData.getColumns().size(); i++) {
            Column column = mData.getColumns().get(i);
            SubcolumnValue value = column.getValues().get(0);
            value.setTarget(Float.valueOf(mCharts1.get(i).getCount()));
        }
    }

    // 结束：条形图相关方法设置

}
