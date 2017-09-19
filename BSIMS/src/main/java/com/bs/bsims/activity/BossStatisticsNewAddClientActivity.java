
package com.bs.bsims.activity;

import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.BossStatisticsHighSeasAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.BossStatisticsClientVO;
import com.bs.bsims.pc.chart.piechart.BossStatisticsPieChart;
import com.bs.bsims.pc.chart.piechart.BossStatisticsPieChart.ItemOnClickCallback;
import com.bs.bsims.utils.HttpClientUtil;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BossStatisticsNewAddClientActivity extends BaseActivity {
    private BossStatisticsClientVO mBossStatisticsClientVO;
    private List<BossStatisticsClientVO> mCharts1;
    private List<BossStatisticsClientVO> mCharts2;
    private List<BossStatisticsClientVO> mCharts3;
    private float mCharts2Max;
    private TextView mRankName;
    private TextView mNewAddClient;
    private TextView mLevelNum1, mLevelNum2, mLevelNum3, mLevelNum4;
    private TextView mLevelPercent1, mLevelPercent2, mLevelPercent3, mLevelPercent4;
    private BSListView mNewClientList;
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

    // 定义饼状图相关变量
    private LinearLayout mLevelChartLayout;
    private BossStatisticsPieChart mLevelChart;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.boss_statistics_new_add_client_view, mContentLayout);
        mCharts3 = new ArrayList<BossStatisticsClientVO>();
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
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.BOSS_STATISTICS_NEW_ADD_CLIENT, map);
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
        mNewAddClient.setText("+" + mBossStatisticsClientVO.getCount());
        mCharts2Max = Float.parseFloat(mBossStatisticsClientVO.getCharts2_max());
        mCharts1 = mBossStatisticsClientVO.getCharts1();
        mCharts2 = mBossStatisticsClientVO.getCharts2();
        mCharts3 = mBossStatisticsClientVO.getCharts3();
        if (mBossStatisticsClientVO.getCount().equals("0")) {
            // 当新增客户为0时，隐藏排名列表
            mRankName.setVisibility(View.GONE);
            mNewClientList.setVisibility(View.GONE);
        }
        setLevelData(mCharts1);
        // 客户人员排名要用到最大值，一种是遍历，另一种是直接传最大值
        // highseasAdapter.setmMaxCount(Float.parseFloat(CommonUtils.isNormalData(mBossStatisticsClientVO.getCharts3_max())));
        mHighseasAdapter.updateData(mCharts3);
        setPieChartData();
        setCulumnChartData();
    }

    public void setCulumnChartData() {
        if (mBossStatisticsClientVO.getCount().equals("0")) {
            initNoColumnData();
        } else {
            generateData();
            prepareDataAnimation();
            mResourceChart.startDataAnimation();
        }
    }

    public void setLevelData(List<BossStatisticsClientVO> mList) {
        mLevelNum1.setText(mList.get(0).getCount());
        mLevelNum2.setText(mList.get(1).getCount());
        mLevelNum3.setText(mList.get(2).getCount());
        mLevelNum4.setText(mList.get(3).getCount());
        mLevelPercent1.setText(mList.get(0).getPercent());
        mLevelPercent2.setText(mList.get(1).getPercent());
        mLevelPercent3.setText(mList.get(2).getPercent());
        mLevelPercent4.setText(mList.get(3).getPercent());
    }

    @Override
    public void initView() {
        mTitleTv.setText("新增客户分析");
        mRankName = (TextView) findViewById(R.id.rank_name);
        mNewAddClient = (TextView) findViewById(R.id.new_add_client);
        mLevelNum1 = (TextView) findViewById(R.id.level_num1);
        mLevelNum2 = (TextView) findViewById(R.id.level_num2);
        mLevelNum3 = (TextView) findViewById(R.id.level_num3);
        mLevelNum4 = (TextView) findViewById(R.id.level_num4);
        mLevelPercent1 = (TextView) findViewById(R.id.level_percent1);
        mLevelPercent2 = (TextView) findViewById(R.id.level_percent2);
        mLevelPercent3 = (TextView) findViewById(R.id.level_percent3);
        mLevelPercent4 = (TextView) findViewById(R.id.level_percent4);
        mResourceChart = (ColumnChartView) this.findViewById(R.id.resource_chart);
        mNewClientList = (BSListView) findViewById(R.id.new_client_List);
        mHighseasAdapter = new BossStatisticsHighSeasAdapter(BossStatisticsNewAddClientActivity.this);
        mNewClientList.setAdapter(mHighseasAdapter);
        mLevelChart = new BossStatisticsPieChart(this);
        mLevelChartLayout = (LinearLayout) findViewById(R.id.level_chart_layout);
        mLevelChartLayout.addView(mLevelChart);// 级别分析
    }

    @Override
    public void bindViewsListener() {

    }

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
        int numColumns = 5;// 条形图中条条个数
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
            axisXValues.add(new AxisValue(i).setLabel(mCharts2.get(i).getName()));

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
            axisX.setMaxLabelChars(8);
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
        if (mCharts2Max < 15) {
            mResourceChart.getCurrentViewport().top = 20;
        } else {
            mResourceChart.getCurrentViewport().top = mCharts2Max + mCharts2Max / 5;
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
            value.setTarget(Float.valueOf(mCharts2.get(i).getCount()));
        }
    }

    // 结束：条形图相关方法设置

    // 级别分析开始：饼状图相关方法设置
    public void setPieChartData() {

        if (null == mCharts1 || Integer.valueOf(mBossStatisticsClientVO.getCount()) == 0) {// 饼子图没得数据的时候
            mLevelChart.setAllTotal(-1);
            mLevelChart.setItems(new float[] {
                    100
            });
            // 作用是在条件刷选下，避免上次饼状图值对下次无数据值情况下颜色块造成影响， 无数据时饼状图为灰色，但是有时第一次总为其他色（eg:绿色）
            mLevelChart.setFirstItemSizes();
            mLevelChart.setRadiocontentMultiLine("无数据", false);
            mLevelChart.intitPieChart(null);

        } else {
            String colors[] = new String[mCharts1.size()];
            for (int i = 0; i < mCharts1.size(); i++) {
                String str = mCharts1.get(i).getId();
                if (str.equals("1")) {
                    colors[i] = "#FFB30F";
                } else if (str.equals("2")) {
                    colors[i] = "#82DB9B";
                } else if (str.equals("3")) {
                    colors[i] = "#D1CA33";
                } else {
                    colors[i] = "#00A8FF";
                }
            }

            mLevelChart.setColors(colors);
            mLevelChart.setAllTotal(Float.parseFloat(mBossStatisticsClientVO.getCount()));
            float iFloat[] = new float[mCharts1.size()];
            for (int i = 0; i < iFloat.length; i++) {
                iFloat[i] = Float.parseFloat(mCharts1.get(i).getRate()) * 100;
            }
            mLevelChart.setItems(iFloat);
            mLevelChart.intitPieChart(new ItemOnClickCallback() {

                @Override
                public void itemClickCallback(int position) {
                    mLevelChart.setRadiocontentMultiLine(mCharts1.get(position).getName() + "," + mCharts1.get(position).getPercent(), true);
                }

            });

        }
        mLevelChart.setPiechartBackgroundColor(Color.parseColor("#FFFFFF"));
    }

    // 级别分析结束：饼状图相关方法设置

}
