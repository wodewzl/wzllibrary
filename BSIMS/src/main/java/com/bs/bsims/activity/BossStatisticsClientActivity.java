
package com.bs.bsims.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.BossStatisticsClientVO;
import com.bs.bsims.pc.chart.piechart.BossStatisticsPieChart;
import com.bs.bsims.pc.chart.piechart.BossStatisticsPieChart.ItemOnClickCallback;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CommonUtils.ResultCallback;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.view.chart.Axis;
import com.bs.bsims.view.chart.AxisValue;
import com.bs.bsims.view.chart.Chart;
import com.bs.bsims.view.chart.Column;
import com.bs.bsims.view.chart.ColumnChartData;
import com.bs.bsims.view.chart.ColumnChartView;
import com.bs.bsims.view.chart.SubcolumnValue;
import com.bs.bsims.view.chart.Viewport;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BossStatisticsClientActivity extends BaseActivity {

    private TextView mAllClient, mNewAddClient, mNewAddHighseas;
    private TextView mLevelNum1, mLevelNum2, mLevelNum3, mLevelNum4;
    private TextView mLevelPercent1, mLevelPercent2, mLevelPercent3, mLevelPercent4;
    private BossStatisticsClientVO mBossStatisticsClientVO;
    private List<BossStatisticsClientVO> mCharts1;
    private List<BossStatisticsClientVO> mCharts2;
    private float mCharts1Max;
    // 客户来源分析，定义条形图相关变量
    private ColumnChartView mResourceChart;
    private ColumnChartData mData;
    private boolean hasAxes = true;
    private boolean hasAxesNames = false;// 控制是否显示横纵坐标的name（代表的意思）
    private boolean hasLabels = true;// 控制是否在条条上显示对应的值
    private boolean hasLabelForSelected = false;
    private static final int DEFAULT_DATA = 0;
    private int dataType = DEFAULT_DATA;
    // 客户级别分析， 定义饼状图相关变量
    private BossStatisticsPieChart mLevelChart;
    private String[] mArray = {
            "新增客户分析", "公海客户分析"
    };
    private int[] mColor = {
            Color.parseColor("#FFB30F"), Color.parseColor("#82DB9B"), Color.parseColor("#D1CA33"),
            Color.parseColor("#00A8FF")
    };

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.boss_statistics_client_view, mContentLayout);

    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    private boolean getData() {
        try {
            Gson gson = new Gson();
            Map map = new HashMap<String, String>();
            map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.BOSS_STATISTICS_CLIENT, map);
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
        mCharts1Max = Float.parseFloat(mBossStatisticsClientVO.getCharts1_max());
        mCharts1 = mBossStatisticsClientVO.getCharts1();
        mCharts2 = mBossStatisticsClientVO.getCharts2();
        mAllClient.setText(mBossStatisticsClientVO.getTotalCount());
        mNewAddClient.setText("+" + mBossStatisticsClientVO.getNewCount());
        mNewAddHighseas.setText(mBossStatisticsClientVO.getPublicCount());
        setLevelData(mCharts2);
        setPieChartData();
        setCulumnChartData();
    }

    public void setCulumnChartData() {
        if (mBossStatisticsClientVO.getTotalCount().equals("0")) {
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
        mTitleTv.setText("客户总量分析");
        mOkTv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.crm_menu), null, null, null);
        mAllClient = (TextView) findViewById(R.id.all_client);
        mNewAddClient = (TextView) findViewById(R.id.new_add_client);
        mNewAddHighseas = (TextView) findViewById(R.id.new_add_highseas);
        mLevelNum1 = (TextView) findViewById(R.id.level_num1);
        mLevelNum2 = (TextView) findViewById(R.id.level_num2);
        mLevelNum3 = (TextView) findViewById(R.id.level_num3);
        mLevelNum4 = (TextView) findViewById(R.id.level_num4);
        mLevelPercent1 = (TextView) findViewById(R.id.level_percent1);
        mLevelPercent2 = (TextView) findViewById(R.id.level_percent2);
        mLevelPercent3 = (TextView) findViewById(R.id.level_percent3);
        mLevelPercent4 = (TextView) findViewById(R.id.level_percent4);
        mResourceChart = (ColumnChartView) findViewById(R.id.resource_chart);// 条形图
        mLevelChart = new BossStatisticsPieChart(this);
        LinearLayout layout = (LinearLayout) findViewById(R.id.level_chart);
        layout.addView(mLevelChart);

    }

    @Override
    public void bindViewsListener() {
        mOkTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                CommonUtils.initPopViewBg(BossStatisticsClientActivity.this, mArray, mOkTv, mCallback, CommonUtils.getScreenWidth(BossStatisticsClientActivity.this) / 3);
            }
        });
    }

    // 添加回调函数
    ResultCallback mCallback = new ResultCallback() {
        @Override
        public void callback(String str, int position) {
            Intent intent = new Intent();
            if (position == 0) {
                intent.setClass(BossStatisticsClientActivity.this, BossStatisticsNewAddClientActivity.class);
                startActivity(intent);
            } else if (position == 1) {
                intent.setClass(BossStatisticsClientActivity.this, BossStatisticsHighSeasClientActivity.class);
                startActivity(intent);
            }
        }
    };

    // 客户来源分析开始：条形图相关方法设置
    private void reset() {
        hasAxes = true;
        hasAxesNames = true;
        hasLabels = false;
        hasLabelForSelected = false;
        dataType = DEFAULT_DATA;
        mResourceChart.setValueSelectionEnabled(hasLabelForSelected);

    }

    private void resetViewport() {
        // Reset viewport height range to (0,100)
        final Viewport v = new Viewport(mResourceChart.getMaximumViewport());
        v.bottom = 0;
        v.top = 100;
        v.left = 0;
        mResourceChart.setMaximumViewport(v);
        mResourceChart.setCurrentViewport(v);
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
            // Float.valueOf(charts1.get(i).getCount()
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

    // 客户来源分析结束：条形图相关方法设置

    // 客户级别分析开始：饼状图相关方法设置
    public void setPieChartData() {
        if (null == mCharts2 || Integer.valueOf(mBossStatisticsClientVO.getTotalCount()) == 0) {// 饼子图没得数据的时候
            mLevelChart.setAllTotal(-1);
            mLevelChart.setItems(new float[] {
                    100
            });
            // 作用是在条件刷选下，避免上次饼状图值对下次无数据值情况下颜色块造成影响， 无数据时饼状图为灰色，但是有时第一次总为其他色（eg:绿色）
            mLevelChart.setFirstItemSizes();
            mLevelChart.setRadiocontentMultiLine("无数据", false);
            mLevelChart.intitPieChart(null);

        } else {
            String colors[] = new String[mCharts2.size()];
            for (int i = 0; i < mCharts2.size(); i++) {
                String str = mCharts2.get(i).getId();
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
            mLevelChart.setAllTotal(Float.parseFloat(mBossStatisticsClientVO.getTotalCount()));
            float iFloat[] = new float[mCharts2.size()];
            for (int i = 0; i < iFloat.length; i++) {
                iFloat[i] = Float.parseFloat(mCharts2.get(i).getRate()) * 100;
            }
            mLevelChart.setItems(iFloat);
            mLevelChart.intitPieChart(new ItemOnClickCallback() {

                @Override
                public void itemClickCallback(int position) {
                    mLevelChart.setRadiocontentMultiLine(mCharts2.get(position).getName() + "," + mCharts2.get(position).getPercent(), true);
                }

            });
        }
        mLevelChart.setPiechartBackgroundColor(Color.parseColor("#FFFFFF"));
    }
    // 客户级别分析结束：饼状图相关方法设置

}
