
package com.bs.bsims.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.BossStatiscsDocumentaryVo;
import com.bs.bsims.model.BossStatisticsClientVO;
import com.bs.bsims.model.CrmOptionsVO;
import com.bs.bsims.model.TreeVO;
import com.bs.bsims.pc.chart.piechart.BossStatisticsPieChart;
import com.bs.bsims.pc.chart.piechart.BossStatisticsPieChart.ItemOnClickCallback;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CommonUtils.ResultCallback;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSPopupWindowsTitle;
import com.bs.bsims.view.BSPopupWindowsTitle.TreeCallBack;
import com.bs.bsims.view.chart.Axis;
import com.bs.bsims.view.chart.AxisValue;
import com.bs.bsims.view.chart.Chart;
import com.bs.bsims.view.chart.Column;
import com.bs.bsims.view.chart.ColumnChartData;
import com.bs.bsims.view.chart.ColumnChartView;
import com.bs.bsims.view.chart.SubcolumnValue;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BossStatisticsTradeActivity extends BaseActivity implements OnClickListener {
    private BossStatisticsClientVO mBossStatisticsClientVO;
    private List<BossStatisticsClientVO> mCharts1;
    private List<BossStatisticsClientVO> mCharts2;
    private float mCharts1Max;
    private TextView mTotalMoney, mTotalCount;
    private TextView mLevelNum1, mLevelNum2, mLevelNum3, mLevelNum4, mLevelNum5;

    // 定义菜单相关变量
    private boolean mFlag = true;
    private CrmOptionsVO mCrmOptionsVO;
    private List<BossStatiscsDocumentaryVo> bVoList;
    private LinearLayout mOneTitle, mTwoTitle;
    private TextView mOneTitleTv, mTwoTitleTv;
    private BSPopupWindowsTitle mBsPopupWindowsTitle, mBsPopupWindowsTitleDep;
    private PopupWindow mTimePop;// 时间筛选弹出框
    private int mSelectOne = 0;
    private String mDid = "0";// 0为默认全部部门
    private String mDateType = "";// 1：近半年，2：近三个月，3：上半年，4：下半年
    private String[] mTimeArray = {
            "近半年", "上半年", "下半年"
    };

    // 客户来源分析，定义条形图相关变量
    private ColumnChartView mStatusChart;
    private ColumnChartData mData;
    private boolean hasAxes = true;
    private boolean hasAxesNames = false;// 控制是否显示横纵坐标的name（代表的意思）
    private boolean hasLabels = true;// 控制是否在条条上显示对应的值
    private boolean hasLabelForSelected = false;
    private static final int DEFAULT_DATA = 0;
    private int dataType = DEFAULT_DATA;

    // 定义饼状图相关变量
    private LinearLayout mTypeChartLayout;
    private BossStatisticsPieChart mTypeChart;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.boss_statistics_trade_view, mContentLayout);

    }

    @Override
    public boolean getDataResult() {
        // TODO Auto-generated method stub
        return getData();
    }

    private boolean getData() {
        Gson gson = new Gson();
        if (mFlag) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.BOSS_STATISTICS_CRMOPTIONSBYDP, map);
            mCrmOptionsVO = gson.fromJson(jsonStr, CrmOptionsVO.class);
        }
        try {
            Map map = new HashMap<String, String>();
            map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put("did", mDid);
            map.put("datetype", mDateType);
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.BOSS_STATISTICS_TRADE, map);
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
        if (mFlag) {
            if (mCrmOptionsVO == null)
                return;
            createLeftPop();
            mFlag = false;
        }
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
        mTotalCount.setText(mBossStatisticsClientVO.getTotalCount());
        mTotalMoney.setText(mBossStatisticsClientVO.getTotalMoney());
        setLevelData(mCharts2);
        setCulumnChartData();
        setPieChartData();
    }

    public void setCulumnChartData() {
        if (mBossStatisticsClientVO.getTotalCount().equals("0")) {
            initNoColumnData();
        } else {
            generateData();
            prepareDataAnimation();
            mStatusChart.startDataAnimation();
        }
    }

    public void setLevelData(List<BossStatisticsClientVO> mList) {
        mLevelNum1.setText(CommonUtils.countNumberSecond(mList.get(0).getMoney()) + "/" + mList.get(0).getCount() + "个");
        mLevelNum2.setText(CommonUtils.countNumberSecond(mList.get(1).getMoney()) + "/" + mList.get(1).getCount() + "个");
        mLevelNum3.setText(CommonUtils.countNumberSecond(mList.get(2).getMoney()) + "/" + mList.get(2).getCount() + "个");
        mLevelNum4.setText(CommonUtils.countNumberSecond(mList.get(3).getMoney()) + "/" + mList.get(3).getCount() + "个");
        mLevelNum5.setText(CommonUtils.countNumberSecond(mList.get(4).getMoney()) + "/" + mList.get(4).getCount() + "个");
    }

    @Override
    public void initView() {
        initTitleView();
        initPopData();// 部门、 时间Pop初始化

        mTitleTv.setText("合同分析");
        mOkTv.setText("排行");
        mTotalMoney = (TextView) findViewById(R.id.total_money);
        mTotalCount = (TextView) findViewById(R.id.total_count);
        mLevelNum1 = (TextView) findViewById(R.id.level_num1);
        mLevelNum2 = (TextView) findViewById(R.id.level_num2);
        mLevelNum3 = (TextView) findViewById(R.id.level_num3);
        mLevelNum4 = (TextView) findViewById(R.id.level_num4);
        mLevelNum5 = (TextView) findViewById(R.id.level_num5);
        mStatusChart = (ColumnChartView) findViewById(R.id.status_chart);
        mTypeChart = new BossStatisticsPieChart(this);
        mTypeChartLayout = (LinearLayout) findViewById(R.id.type_chart_layout);
        mTypeChartLayout.addView(mTypeChart);
    }

    public void initPopData() {
        // 时间筛选
        // ArrayList<TreeVO> list1 = CommonUtils.getOneLeveTreeVo(mTimeArray);
        // mBsPopupWindowsTitle = new BSPopupWindowsTitle(this, list1, callback,
        // ViewGroup.LayoutParams.WRAP_CONTENT);
        if (mTimePop == null) {
            mTimePop = CommonUtils.initPopView(BossStatisticsTradeActivity.this, 6, ViewGroup.LayoutParams.WRAP_CONTENT, timeCallback);
        }
        // if (!mTimePop.isShowing()) {
        // mTimePop.showAsDropDown(mHeadLayout);
        // } else {
        // mTimePop.dismiss();
        // }

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

    // 部门筛选
    public void createLeftPop() {
        ArrayList<TreeVO> list1 = CommonUtils.getOneLeveTreeVo(mCrmOptionsVO.getArray());
        mBsPopupWindowsTitleDep = new BSPopupWindowsTitle(this, list1, callback, CommonUtils.getScreenHigh(BossStatisticsTradeActivity.this) / 3);
    }

    // 菜单点击回调函数
    TreeCallBack callback = new TreeCallBack() {

        @Override
        public void callback(TreeVO vo) {
            if (mSelectOne == 0) {
                // 审批一级菜单
                mOneTitleTv.setText(vo.getName());
                match(1, vo.getSearchId() + "");
            }
            // else if (mSelectOne == 1) {
            // mTwoTitleTv.setText(vo.getName());
            // match(2, vo.getParentSerachId() + "");
            // }
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

    @Override
    public void bindViewsListener() {
        mOneTitle.setOnClickListener(this);
        mTwoTitle.setOnClickListener(this);
        mOkTv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent in = new Intent(BossStatisticsTradeActivity.this, BossStatisticsTradeSignRankActivity.class);
                startActivity(in);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title01:
                if (mBsPopupWindowsTitleDep != null) {
                    mSelectOne = 0;
                    mBsPopupWindowsTitleDep.showPopupWindow(mOneTitle);
                }
                break;
            case R.id.title02:
                // if (mTimePop != null) {
                // mSelectOne = 1;
                // mTimePop.showAsDropDown(mHeadLayout);// (mOneTitle);
                // }

                if (!mTimePop.isShowing()) {
                    mTimePop.showAsDropDown(mOneTitle);
                } else {
                    mTimePop.dismiss();
                }
                break;

            default:
                break;
        }
    }

    // 客户来源分析开始：条形图相关方法设置
    private void reset() {
        hasAxes = true;
        hasAxesNames = true;
        hasLabels = false;
        hasLabelForSelected = false;
        dataType = DEFAULT_DATA;
        mStatusChart.setValueSelectionEnabled(hasLabelForSelected);

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

        mStatusChart.setColumnChartData(mData);
        mStatusChart.setZoomEnabled(false);
        mStatusChart.setClickable(false);
        mStatusChart.setScrollEnabled(false);
        mStatusChart.setViewportCalculationEnabled(false);

        if (mCharts1Max < 15) {
            mStatusChart.getCurrentViewport().top = 20;
        } else {
            mStatusChart.getCurrentViewport().top = mCharts1Max + mCharts1Max / 5;
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
            values.add(new SubcolumnValue((float) Math.random() * 100, this.getResources().getColor(R.color.C3)));
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
        mStatusChart.setColumnChartData(mData);
        mStatusChart.setZoomEnabled(false);
        mStatusChart.setClickable(false);
        mStatusChart.setScrollEnabled(false);
        mStatusChart.setViewportCalculationEnabled(false);
        mStatusChart.getCurrentViewport().top = 100;
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
            mStatusChart.setValueSelectionEnabled(hasLabelForSelected);
        }

        generateData();
    }

    private void toggleLabelForSelected() {
        hasLabelForSelected = !hasLabelForSelected;
        mStatusChart.setValueSelectionEnabled(hasLabelForSelected);

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

    // 类型分布开始：饼状图相关方法设置
    public void setPieChartData() {

        if (null == mCharts2 || Integer.valueOf(mBossStatisticsClientVO.getTotalCount()) == 0) {// 饼子图没得数据的时候
            mTypeChart.setAllTotal(-1);
            mTypeChart.setItems(new float[] {
                    100
            });
            // 作用是在条件刷选下，避免上次饼状图值对下次无数据值情况下颜色块造成影响， 无数据时饼状图为灰色，但是有时第一次总为其他色（eg:绿色）
            mTypeChart.setFirstItemSizes();
            mTypeChart.setRadiocontentMultiLine("无数据", false);
            mTypeChart.intitPieChart(null);

        } else {
            String colors[] = new String[mCharts2.size()];
            for (int i = 0; i < mCharts2.size(); i++) {
                String str = mCharts2.get(i).getId();
                if (str.equals("1")) {
                    colors[i] = "#FFB30F";
                } else if (str.equals("2")) {
                    colors[i] = "#76CBF4";
                } else if (str.equals("3")) {
                    colors[i] = "#D1CA33";
                } else if (str.equals("4")) {
                    colors[i] = "#07C339";
                } else if (str.equals("5")) {
                    colors[i] = "#00A8FF";
                }
            }

            mTypeChart.setAllTotal(Float.parseFloat(mBossStatisticsClientVO.getTotalCount()));
            mTypeChart.setColors(colors);
            final float iFloat[] = new float[mCharts2.size()];
            for (int i = 0; i < iFloat.length; i++) {
                iFloat[i] = Float.parseFloat(mCharts2.get(i).getRate()) * 100;
            }
            mTypeChart.setItems(iFloat);
            mTypeChart.intitPieChart(new ItemOnClickCallback() {

                @Override
                public void itemClickCallback(int position) {
                    DecimalFormat decimalFormat = new DecimalFormat("#.00");// 构造方法的字符格式这里如果小数不足2位,会以0补足.
                    String rateStr = decimalFormat.format(iFloat[position]);
                    mTypeChart.setRadiocontentMultiLine(mCharts2.get(position).getName() + "," + rateStr + "%", true);
                }

            });

        }
        mTypeChart.setPiechartBackgroundColor(Color.parseColor("#FFFFFF"));
    }
    // 类型分布结束：饼状图相关方法设置

}
