
package com.bs.bsims.activity;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.BossStatisticsTaskStatisticsAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.BossStatisticsAttendanceVO;
import com.bs.bsims.model.TreeVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSListView;
import com.bs.bsims.view.BSPopupWindowsTitle;
import com.bs.bsims.view.BSPopupWindowsTitle.TreeCallBack;
import com.bs.bsims.view.chart.Axis;
import com.bs.bsims.view.chart.AxisValue;
import com.bs.bsims.view.chart.Chart;
import com.bs.bsims.view.chart.Line;
import com.bs.bsims.view.chart.LineChartData;
import com.bs.bsims.view.chart.LineChartOnValueSelectListener;
import com.bs.bsims.view.chart.LineChartView;
import com.bs.bsims.view.chart.PointValue;
import com.bs.bsims.view.chart.ValueShape;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class BossStatisticsTaskStatisticsActivity extends BaseActivity implements OnClickListener, LineChartOnValueSelectListener {
    private TextView mTaskData1, mTaskData2, mTaskData3, mTaskData4, mTaskData5;
    private List<LinearLayout> mDeapartLayout = new ArrayList<LinearLayout>();
    private LinearLayout mDepartLayout01, mDepartLayout02, mDepartLayout03, mDepartLayout04, mDepartLayout05;
    private TextView mDepartTv01, mDepartTv02, mDepartTv03, mDepartTv04, mDepartTv05;
    private ImageView mImageView01, mImageView02, mImageView03, mImageView04, mImageView05;
    private TextView mDateTv, mNumberTv;
    private BSListView mTaskList;
    private BossStatisticsTaskStatisticsAdapter mTaskAdapter;

    private String mDate;
    private String mCurrentNumber;

    private BossStatisticsAttendanceVO mNumberAttendanceVO;
    private BossStatisticsAttendanceVO mNumberMonthAttendanceVO;
    private List<BossStatisticsAttendanceVO> mNumList;
    private List<BossStatisticsAttendanceVO> mNext;
    private List<BossStatisticsAttendanceVO> mLast;
    private Boolean monthFlag = true;// 数据加载出来后，才可以再次请求数据
    private int one;// 判断某种数据只可以加载一次
    // 弹出框
    private BSPopupWindowsTitle mBsPopupWindowsTitle;
    private String mDateType = "";// 1：近半年，2：近三个月，3：上半年，4：下半年
    private String[] mTimeArray = {
            "近半年", "上半年", "下半年"
    };

    // 定义折线图相关变量
    private List<BossStatisticsAttendanceVO> mCompareLine = new ArrayList<BossStatisticsAttendanceVO>();
    private List<BossStatisticsAttendanceVO> mChartLine = new ArrayList<BossStatisticsAttendanceVO>();
    private LineChartView mLineChart;
    private LineChartData mLineData;
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
            Color.rgb(0, 166, 255), Color.rgb(252, 0, 0),
            Color.rgb(255, 171, 0), Color.rgb(0, 154, 37), Color.rgb(180, 180, 180)
    };

    private List<String> mStatusList = new ArrayList<String>();

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.boss_statistics_task_statistics_view, mContentLayout);
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
            mapList.put("datetype", mDateType);
            // mapList.put("userid", "22");
            // mapList.put("datetype", "1");
            mapList.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle()
                    + Constant.BOSS_STATISTICS_TASK_STATISTICS, mapList);

            mNumberAttendanceVO = gson.fromJson(jsonStrList, BossStatisticsAttendanceVO.class);
            if (Constant.RESULT_CODE.equals(mNumberAttendanceVO.getCode())) {
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
        mLoadingLayout.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);
        updateUi();
    }

    @Override
    public void executeFailure() {
        mLoadingLayout.setVisibility(View.VISIBLE);
        mContentLayout.setVisibility(View.GONE);
        if (mNumberAttendanceVO == null) {
            super.showNoNetView();
        } else {
            super.showNoContentView();
        }

    }

    @Override
    public void updateUi() {
        one = 0;
        BossStatisticsAttendanceVO vo = mNumberAttendanceVO.getInfo();
        mDate = mNumberAttendanceVO.getInfo().getYear() + "-" + mNumberAttendanceVO.getInfo().getMonth();
        mNumList = vo.getNumlist();
        mChartLine = vo.getChart();
        mNext = vo.getNext();
        mLast = vo.getLast();
        setTaskAdapterData();
        setTaskData(mNumList);
        initLineChartData(mChartLine);
    }

    private void setTaskAdapterData() {
        if (mNumberAttendanceVO == null || mNumberAttendanceVO.getInfo() == null) {
            return;
        }
        CommonUtils.setTextThree(this, mDateTv, mNumberAttendanceVO.getInfo().getYear() + "年", mNumberAttendanceVO.getInfo().getMonth(), "月", R.color.black, 1.5f);
        CommonUtils.setTextTwoBefore(this, mNumberTv, mNumberAttendanceVO.getInfo().getAllnum(), "项", R.color.C10, 1.5f);
        mTaskAdapter.setmCurrentDate(mDate);
        mTaskAdapter.setmMaxCount(Float.valueOf(mNumberAttendanceVO.getInfo().getMaxnum()));
        mTaskAdapter.setList(refreshListData(mNumberAttendanceVO.getInfo().getNext()));
        mTaskAdapter.setmLastList(refreshListData(mNumberAttendanceVO.getInfo().getLast()));
        mTaskAdapter.notifyDataSetChanged();
    }

    private void setTaskData(List<BossStatisticsAttendanceVO> list) {
        mTaskData1.setText(list.get(0).getNum());
        mTaskData2.setText(list.get(1).getNum());
        mTaskData3.setText(list.get(2).getNum());
        mTaskData4.setText(list.get(3).getNum());
        mTaskData5.setText(list.get(4).getNum());
    }

    @Override
    public void initView() {
        mTitleTv.setText("任务统计");
        mTaskData1 = (TextView) findViewById(R.id.task_data1);
        mTaskData2 = (TextView) findViewById(R.id.task_data2);
        mTaskData3 = (TextView) findViewById(R.id.task_data3);
        mTaskData4 = (TextView) findViewById(R.id.task_data4);
        mTaskData5 = (TextView) findViewById(R.id.task_data5);
        mDepartLayout01 = (LinearLayout) findViewById(R.id.depart_layout01);
        mDepartLayout02 = (LinearLayout) findViewById(R.id.depart_layout02);
        mDepartLayout03 = (LinearLayout) findViewById(R.id.depart_layout03);
        mDepartLayout04 = (LinearLayout) findViewById(R.id.depart_layout04);
        mDepartLayout05 = (LinearLayout) findViewById(R.id.depart_layout05);
        mDepartTv01 = (TextView) findViewById(R.id.depart_text01);
        mDepartTv02 = (TextView) findViewById(R.id.depart_text02);
        mDepartTv03 = (TextView) findViewById(R.id.depart_text03);
        mDepartTv04 = (TextView) findViewById(R.id.depart_text04);
        mDepartTv05 = (TextView) findViewById(R.id.depart_text05);
        mImageView01 = (ImageView) findViewById(R.id.depart_status01);
        mImageView02 = (ImageView) findViewById(R.id.depart_status02);
        mImageView03 = (ImageView) findViewById(R.id.depart_status03);
        mImageView04 = (ImageView) findViewById(R.id.depart_status04);
        mImageView05 = (ImageView) findViewById(R.id.depart_status05);
        mDateTv = (TextView) findViewById(R.id.date_tv);
        mNumberTv = (TextView) findViewById(R.id.number_tv);
        mTaskList = (BSListView) findViewById(R.id.task_List);
        mNext = new ArrayList<BossStatisticsAttendanceVO>();
        mLast = new ArrayList<BossStatisticsAttendanceVO>();
        mTaskAdapter = new BossStatisticsTaskStatisticsAdapter(BossStatisticsTaskStatisticsActivity.this, mNext, mLast);
        mTaskList.setAdapter(mTaskAdapter);

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        // mOkTv.setText(String.valueOf(year) + "-" + String.valueOf(month));
        mOkTv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.cq_timepick), null, null, null);
        addStatusList();
        addLayout();
        initLineChartView();
    }

    public void initLineChartView() {

        mLineChart = (LineChartView) this.findViewById(R.id.chart);
        mLineChart.setOnValueTouchListener(this);
        mLineChart.setValueSelectionEnabled(true);

    }

    @Override
    public void bindViewsListener() {
        mDepartLayout01.setOnClickListener(this);
        mDepartLayout02.setOnClickListener(this);
        mDepartLayout03.setOnClickListener(this);
        mDepartLayout04.setOnClickListener(this);
        mDepartLayout05.setOnClickListener(this);

        mOkTv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // 时间筛选
                ArrayList<TreeVO> list1 = CommonUtils.getOneLeveTreeVo(mTimeArray);
                mBsPopupWindowsTitle = new BSPopupWindowsTitle(BossStatisticsTaskStatisticsActivity.this, list1, callback, ViewGroup.LayoutParams.WRAP_CONTENT);
                mBsPopupWindowsTitle.showPopupWindow(mOkTv);
            }
        });
    }

    // 菜单点击回调函数
    TreeCallBack callback = new TreeCallBack() {

        @Override
        public void callback(TreeVO vo) {
            // 审批一级菜单
            mOkTv.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            mOkTv.setText(vo.getName());
            mDateType = vo.getSearchId();
            new ThreadUtil(BossStatisticsTaskStatisticsActivity.this, BossStatisticsTaskStatisticsActivity.this).start();

        }
    };

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {

            case R.id.depart_layout01:
                departOnclick(mDepartLayout02, mDepartTv01, mImageView01, 0);
                break;
            case R.id.depart_layout02:
                departOnclick(mDepartLayout02, mDepartTv02, mImageView02, 1);
                break;
            case R.id.depart_layout03:
                departOnclick(mDepartLayout03, mDepartTv03, mImageView03, 2);
                break;
            case R.id.depart_layout04:
                departOnclick(mDepartLayout04, mDepartTv04, mImageView04, 3);
                break;
            case R.id.depart_layout05:
                departOnclick(mDepartLayout05, mDepartTv05, mImageView05, 4);
                break;

            default:
                break;
        }
    }

    public void addLayout() {
        mDeapartLayout.add(mDepartLayout01);
        mDeapartLayout.add(mDepartLayout02);
        mDeapartLayout.add(mDepartLayout03);
        mDeapartLayout.add(mDepartLayout04);
        mDeapartLayout.add(mDepartLayout05);
        mImageView01.setImageResource(R.drawable.gou);
        mImageView02.setImageResource(R.drawable.gou);
        mImageView03.setImageResource(R.drawable.gou);
        mImageView04.setImageResource(R.drawable.gou);
        mImageView05.setImageResource(R.drawable.gou);
    }

    public void addStatusList() {
        mStatusList.add("1");
        mStatusList.add("2");
        mStatusList.add("3");
        mStatusList.add("4");
        mStatusList.add("5");
    }

    public void departOnclick(LinearLayout layout, TextView tv, ImageView image, int sort) {
        if (mDeapartLayout.contains(layout)) {
            // if (mDeapartLayout.size() < 2)
            // return;
            mDeapartLayout.remove(layout);
            tv.setTextColor(Color.parseColor("#666666"));
            image.setImageDrawable(null);
            mChartLine.get(sort).setVisible(false);
            if (mStatusList.contains(String.valueOf(sort + 1))) {
                mStatusList.remove(String.valueOf(1 + sort));
            }
        } else {
            mDeapartLayout.add(layout);
            tv.setTextColor(Color.parseColor("#00a9fe"));
            image.setImageResource(R.drawable.gou);
            mChartLine.get(sort).setVisible(true);
            if (!mStatusList.contains(String.valueOf(sort + 1))) {
                mStatusList.add(String.valueOf(1 + sort));
            }
        }
        mCompareLine.clear();
        mCompareLine.addAll(mChartLine);
        initLineChartData(mChartLine);
        if (monthFlag && one == 0) {
            mTaskList.showHead(this, true);
            one = 1;
            monthFlag = false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Boolean result = getMonthData();
                    if (result) {
                        handler.sendEmptyMessage(0);
                    } else {
                        handler.sendEmptyMessage(1);
                    }
                }
            }).start();
        } else {
            setMonthTaskAdapterData();
        }
    }

    // 折线图开始：
    private void initLineChartData(List<BossStatisticsAttendanceVO> chartLine) {
        if (mCompareLine != null && mCompareLine.size() > 0) {
            for (int i = 0; i < mCompareLine.size(); i++) {
                chartLine.get(i).setVisible(mCompareLine.get(i).isVisible());
            }
        }
        List<Line> lines = new ArrayList<Line>();
        for (int i = 0; i < chartLine.size(); ++i) {
            List<BossStatisticsAttendanceVO> lineValues = chartLine.get(i).getList();
            List<PointValue> values = new ArrayList<PointValue>();
            if (!chartLine.get(i).isVisible())
                continue;
            for (int j = 0; j < lineValues.size(); ++j) {
                // float num = Float.parseFloat(lineValues.get(j).getNum());
                // values.add(new PointValue(j, num));
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
        // final Viewport v = new Viewport(mLineChart.getMaximumViewport());
        mLineChart.setZoomEnabled(false);
        mLineChart.setClickable(false);
        mLineChart.setScrollEnabled(false);
        mLineChart.setViewportCalculationEnabled(false);

        float maxChart = Float.valueOf((mNumberAttendanceVO.getInfo().getMaxchart()));
        if (maxChart <= 12) {
            mLineChart.getCurrentViewport().top = 12;
        } else {
            mLineChart.getCurrentViewport().top = maxChart + maxChart / 6;
        }
        mLineChart.getCurrentViewport().bottom = 0;

        int count = 0;// 统计实际存在的折线条数
        for (int i = 0; i < chartLine.size(); ++i) {
            if (!chartLine.get(i).isVisible())
                continue;
            List<BossStatisticsAttendanceVO> lineValues = chartLine.get(i).getList();
            for (int j = 0; j < lineValues.size(); ++j) {
                float num = Float.parseFloat(lineValues.get(j).getNum());
                PointValue value = mLineData.getLines().get(count).getValues().get(j);
                value.setTarget(value.getX(), num);
            }
            count++;
        }
        mLineChart.startDataAnimation(1000);
    }

    /**
     * To animate values you have to change targets values and then call
     * {@link Chart#startDataAnimation()} method(don't confuse with View.animate()).
     */
    private void prepareDataAnimation() {
        // for (int i = 0; i < mChartLine.size(); ++i) {
        // List<BossStatisticsAttendanceVO> lineValues = mChartLine.get(i).getList();
        // for (int j = 0; j < lineValues.size(); ++j) {
        // float num = Float.parseFloat(lineValues.get(j).getNum());
        // PointValue value = mLineData.getLines().get(i).getValues().get(j);
        // value.setTarget(value.getX(), num);
        // }
        // }
    }

    // 折线图结束

    @Override
    public void onValueDeselected() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
        mDate = mChartLine.get(0).getList().get(pointIndex).getDateTime();
        // updateDataNoChart(mNumberAttendanceVO);
        if (monthFlag) {
            mTaskList.showHead(this, true);
            monthFlag = false;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean result = getMonthData();
                    if (result) {
                        handler.sendEmptyMessage(0);
                    } else {
                        handler.sendEmptyMessage(1);
                    }
                }
            }).start();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mTaskList.showHead(BossStatisticsTaskStatisticsActivity.this, false);
            switch (msg.what) {
                case 0:
                    setMonthTaskAdapterData();
                    monthFlag = true;
                    break;
                case 1:
                    monthFlag = true;
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public boolean getMonthData() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> mapList = new HashMap<String, String>();
            mapList.put("userid", BSApplication.getInstance().getUserId());
            mapList.put("date", mDate);
            mapList.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle()
                    + Constant.BOSS_STATISTICS_TASK_MONTH_STATISTICS, mapList);

            mNumberMonthAttendanceVO = gson.fromJson(jsonStrList, BossStatisticsAttendanceVO.class);
            if (Constant.RESULT_CODE.equals(mNumberMonthAttendanceVO.getCode())) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
        }
    }

    private void setMonthTaskAdapterData() {
        if (mNumberMonthAttendanceVO == null || mNumberMonthAttendanceVO.getInfo() == null) {
            return;
        }
        CommonUtils.setTextThree(this, mDateTv, mNumberMonthAttendanceVO.getInfo().getYear() + "年", mNumberMonthAttendanceVO.getInfo().getMonth(), "月", R.color.black, 1.5f);
        CommonUtils.setTextTwoBefore(this, mNumberTv, mNumberMonthAttendanceVO.getInfo().getAllnum(), "项", R.color.C10, 1.5f);
        if (mNumberMonthAttendanceVO.getInfo().getNext() != null
                && mNumberMonthAttendanceVO.getInfo().getLast() != null) {
            mTaskAdapter.setmCurrentDate(mDate);
            mTaskAdapter.setmMaxCount(Float.valueOf(mNumberMonthAttendanceVO.getInfo().getMaxnum()));
            mTaskAdapter.setList(refreshListData(mNumberMonthAttendanceVO.getInfo().getNext()));
            mTaskAdapter.setmLastList(refreshListData(mNumberMonthAttendanceVO.getInfo().getLast()));
            mTaskAdapter.notifyDataSetChanged();
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

    // 更新数据但不更新折线图
    // public void updateDataNoChart(BossStatisticsAttendanceVO vo) {
    // mChartLine = vo.getInfo().getChart();
    //
    // }

    public String getCurrentNumber(BossStatisticsAttendanceVO vo, int sort) {
        List<BossStatisticsAttendanceVO> list = vo.getInfo().getChart();
        int allCount = 0;
        for (int i = 0; i < list.size(); i++) {
            allCount += Integer.parseInt(list.get(i).getList().get(sort).getNum());
        }
        return allCount + "";
    }

}
