
package com.bs.bsims.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.constant.Constant4Statistics;
import com.bs.bsims.model.DanganLineChartVO;
import com.bs.bsims.time.ScreenInfo;
import com.bs.bsims.time.WheelMain;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomLog;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.utils.UrlUtil;
import com.bs.bsims.view.BSDialog;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @author peck
 * @Description: 获取出勤情况各项线形图 实现线形图切换 初始只显示一条数据
 * @date 2015-7-11 下午3:11:38
 * @email 971371860@qq.com
 * @version V1.1 Color.rgb(132, 76, 125) 紫色 2015/7/22 10:08 初始 首次进来的时候，显示所有的数据
 */

public class DanganChuQingInfoLineChart2Activity extends BaseActivity implements
        OnClickListener, LineChartOnValueSelectListener {

    private static String TAG = "DanganChuQingInfoLineChart2Activity";
    private Context context;
    private String uid;
    // private int[] mColors = new int[] {
    // Color.RED, Color.BLUE,
    // Color.rgb(89, 199, 250), Color.GRAY, Color.GREEN
    // };

    private BSDialog timedialog;
    private String timestr;
    private String datetypetime;
    /**
     * 部门进行选择的时候，需要显示的部门名称的文本框 K:部门ID V:显示部门名称的文本框，这个时候的TAG 也会设置为了当前部门的DID
     */
    private Map<String, TextView> mKDidVTextShowMap;
    private Map<String, View> mKDidVTextShowColorMap;

    private ImageView dangan_chuqing_info_ss_linechart_4cv1;
    private ImageView dangan_chuqing_info_ss_linechart_4cv2;
    private TextView dangan_chuqing_info_ss_linechart_4c1;
    private TextView ac_ext_dangan_chuqing_info_linechart_top_txt,
            txt_comm_head_right;
    private TextView dangan_chuqing_info_ss_linechart_4c2;
    private LinearLayout dangan_chuqing_info_ss_linechart_4ly1;
    private LinearLayout dangan_chuqing_info_ss_linechart_4ly2, dangan_chuqing_info_ss_linechart_4_all_ly;

    private List<LinearLayout> mDeapartLayout = new ArrayList<LinearLayout>();
    /**
     * 日期选择(1：近半年，2：近三个月，3：上半年，4：下半年)
     */
    private String datetype;
    /**
     * type 1缺卡、矿工 2迟到、早退 3未写日志 4请假
     */
    private String type;

    /**
     * 顶部显示的内容，由之前的界面传递
     */
    private String topTitleTxt;

    private Intent getIntent;

    /**
     * 用来控制显示几条线
     */
    private HashSet<String> showLineHashSet;

    private int runCount;

    // 定义折线图相关变量
    private List<DanganLineChartVO> mChartLine = new ArrayList<DanganLineChartVO>();
    private DanganLineChartVO mChuQingVo;
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
            Color.rgb(255, 172, 0), Color.rgb(0, 169, 254), Color.rgb(255, 174, 30),
            Color.GREEN
    };
    // mFlag的用意在于：在迟到早退这种情况下，当取消一个选择时，筛选上半年出勤，保证之前没选中的“折线不会出现”；
    // mFlag判断折线是显示还是隐藏
    private Boolean[] mFlag = {
            true, true
    };

    // 折线图新增
    private TextView mTitleName1, mTitletime1, mContentCount1, mContentChinese1;
    private TextView mTitleName2, mTitletime2, mContentCount2, mContentChinese2;
    private LinearLayout mType2Ly;
    private View mView1, mView2;

    @Override
    public void baseSetContentView() {
        // TODO Auto-generated method stub
        context = this;
        View layout = View.inflate(this,
                R.layout.ac_ext_dangan_chuqing_info_linechart2, null);
        mContentLayout.addView(layout);
        runCount = 0;
    }

    @Override
    public void updateUi() {
        mChartLine = mChuQingVo.getChart();
        // 默认显示当前月的信息
        if (getIntent.getStringExtra("currentType").equals("1")) {
            mContentCount1.setVisibility(View.GONE);
            mContentChinese1.setVisibility(View.GONE);
            mTitleName1.setText("缺卡");
            mTitleName1.setPadding(CommonUtils.dip2px(context, 15), CommonUtils.dip2px(context, 10), 0, CommonUtils.dip2px(context, 10));
            mTitleName1.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.statistics_mscard), null, null, null);
            mTitletime1.setText(mChartLine.get(0).getList().get(mChartLine.get(0).getList().size() - 1).getNum() + "次");
            mTitletime1.setTextColor(Color.parseColor("#B78BDA"));
            mColors = new int[] {
                    Color.parseColor("#B78BDA"), Color.rgb(0, 169, 254), Color.rgb(255, 174, 30),
                    Color.GREEN
            };
        } else if (getIntent.getStringExtra("currentType").equals("2")) {
            ac_ext_dangan_chuqing_info_linechart_top_txt.setText("近半年迟到、早退的情况");
            mTitleName1.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.statistics_worklate), null, null, null);
            mTitleName1.setText("迟到");
            mContentCount1.setVisibility(View.GONE);
            mContentChinese1.setVisibility(View.GONE);
            mTitleName1.setPadding(CommonUtils.dip2px(context, 15), CommonUtils.dip2px(context, 10), 0, CommonUtils.dip2px(context, 10));
            mTitletime1.setText(mChartLine.get(0).getList().get(mChartLine.get(0).getList().size() - 1).getNum() + "次");
            mTitletime1.setTextColor(Color.parseColor("#EA4A4A"));
            mTitleName2.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.statistics_leave_early), null, null, null);
            mTitleName2.setText("早退");
            mContentCount2.setVisibility(View.GONE);
            mContentChinese2.setVisibility(View.GONE);
            mTitleName2.setPadding(CommonUtils.dip2px(context, 15), CommonUtils.dip2px(context, 10), 0, CommonUtils.dip2px(context, 10));
            mTitletime2.setText(mChartLine.get(1).getList().get(mChartLine.get(1).getList().size() - 1).getNum() + "次");
            mTitletime2.setTextColor(Color.parseColor("#2BA8D9"));
            mView1.setBackgroundColor(Color.parseColor("#EA4A4A"));
            mView2.setBackgroundColor(Color.parseColor("#2BA8D9"));
            mColors = new int[] {
                    Color.parseColor("#EA4A4A"), Color.parseColor("#2BA8D9"), Color.rgb(255, 174, 30),
                    Color.GREEN
            };
        } else if (getIntent.getStringExtra("currentType").equals("3")) {
            ac_ext_dangan_chuqing_info_linechart_top_txt.setText("近半年未写日志的情况");
            mColors = new int[] {
                    Color.parseColor("#5E97F6"), Color.parseColor("#2BA8D9"), Color.rgb(255, 174, 30), Color.GREEN
            };
            mTitleName1.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.statistics_nodatework), null, null, null);
            mTitleName1.setText("缺日志");
            mContentCount1.setVisibility(View.GONE);
            mContentChinese1.setVisibility(View.GONE);
            mTitleName1.setPadding(CommonUtils.dip2px(context, 15), CommonUtils.dip2px(context, 10), 0, CommonUtils.dip2px(context, 10));
            mTitletime1.setText(mChartLine.get(0).getList().get(mChartLine.get(0).getList().size() - 1).getNum() + "次");
            mTitletime1.setTextColor(Color.parseColor("#5E97F6"));
        } else {
            ac_ext_dangan_chuqing_info_linechart_top_txt.setText("近半年请假的情况");
            mColors = new int[] {
                    Color.parseColor("#16C686"), Color.parseColor("#FC6F6D"), Color.rgb(255, 174, 30), Color.GREEN
            };
            mTitletime1.setText(mChartLine.get(0).getList().get(mChartLine.get(0).getList().size() - 1).getDuration());
            mTitletime1.setTextColor(Color.parseColor("#16C686"));
            mContentCount1.setText("本月事假次数：" + mChartLine.get(0).getList().get(mChartLine.get(0).getList().size() - 1).getNum() + "次");
            mTitletime2.setText(mChartLine.get(1).getList().get(mChartLine.get(1).getList().size() - 1).getDuration());
            mTitletime2.setTextColor(Color.parseColor("#FC6F6D"));
            mContentCount2.setText("本月病假次数：" + mChartLine.get(1).getList().get(mChartLine.get(1).getList().size() - 1).getNum() + "次");
            mView1.setBackgroundColor(Color.parseColor("#16C686"));
            mView2.setBackgroundColor(Color.parseColor("#FC6F6D"));
        }
        initLineChartData(mChartLine);
    }

    @Override
    public void initView() {
        txt_comm_head_right = (TextView) findViewById(R.id.txt_comm_head_right);
        txt_comm_head_right.setVisibility(View.GONE);
        txt_comm_head_right.setCompoundDrawablesWithIntrinsicBounds(this
                .getResources().getDrawable(R.drawable.cq_timepick), null,
                null, null);
        // TODO Auto-generated method stub
        mTitleTv.setText("员工档案");
        dangan_chuqing_info_ss_linechart_4cv1 = (ImageView) findViewById(R.id.dangan_chuqing_info2_ss_linechart_4cv1);
        dangan_chuqing_info_ss_linechart_4cv2 = (ImageView) findViewById(R.id.dangan_chuqing_info2_ss_linechart_4cv2);
        dangan_chuqing_info_ss_linechart_4c1 = (TextView) findViewById(R.id.dangan_chuqing_info2_ss_linechart_4c1);
        dangan_chuqing_info_ss_linechart_4c2 = (TextView) findViewById(R.id.dangan_chuqing_info2_ss_linechart_4c2);
        dangan_chuqing_info_ss_linechart_4ly1 = (LinearLayout) findViewById(R.id.dangan_chuqing_info2_ss_linechart_4ly1);
        dangan_chuqing_info_ss_linechart_4ly2 = (LinearLayout) findViewById(R.id.dangan_chuqing_info2_ss_linechart_4ly2);
        dangan_chuqing_info_ss_linechart_4_all_ly = (LinearLayout) findViewById(R.id.dangan_chuqing_info2_ss_linechart_4_all_ly);

        mTitleName1 = (TextView) findViewById(R.id.attendance_type1);
        mTitletime1 = (TextView) findViewById(R.id.attendance_type1_time);
        mContentCount1 = (TextView) findViewById(R.id.attendance_type1_count);
        mContentChinese1 = (TextView) findViewById(R.id.attendance_type1_chinses);
        mTitleName2 = (TextView) findViewById(R.id.attendance_type2);
        mTitletime2 = (TextView) findViewById(R.id.attendance_type2_time);
        mContentCount2 = (TextView) findViewById(R.id.attendance_type2_count);
        mContentChinese2 = (TextView) findViewById(R.id.attendance_type2_chinses);
        mType2Ly = (LinearLayout) findViewById(R.id.type2_ly);
        mView1 = findViewById(R.id.mview1);
        mView2 = findViewById(R.id.mview2);
        mKDidVTextShowMap = new HashMap<String, TextView>();
        mKDidVTextShowColorMap = new HashMap<String, View>();

        Intent getIntent = getIntent();
        if (TextUtils.isEmpty(topTitleTxt)) {
            topTitleTxt = getIntent.getStringExtra("titleTxt");
        }
        if (TextUtils.isEmpty(topTitleTxt)) {
            topTitleTxt = "员工档案";
        }
        mTitleTv.setText(topTitleTxt);
        ac_ext_dangan_chuqing_info_linechart_top_txt = (TextView) findViewById(R.id.ac_ext_dangan_chuqing_info2_linechart_top_txt);
        if (getIntent.getStringExtra("currentType").equals("1")) {
            ac_ext_dangan_chuqing_info_linechart_top_txt.setText("近半年缺卡的情况");
            dangan_chuqing_info_ss_linechart_4_all_ly.setVisibility(View.GONE);
            mType2Ly.setVisibility(View.GONE);
        } else if (getIntent.getStringExtra("currentType").equals("2")) {
            ac_ext_dangan_chuqing_info_linechart_top_txt.setText("近半年迟到、早退的情况");
            mType2Ly.setVisibility(View.VISIBLE);
        } else if (getIntent.getStringExtra("currentType").equals("3")) {
            ac_ext_dangan_chuqing_info_linechart_top_txt.setText("近半年未写日志的情况");
            mType2Ly.setVisibility(View.GONE);
            dangan_chuqing_info_ss_linechart_4_all_ly.setVisibility(View.GONE);
        } else {
            ac_ext_dangan_chuqing_info_linechart_top_txt.setText("近半年请假的情况");
            mType2Ly.setVisibility(View.VISIBLE);
            // dangan_chuqing_info_ss_linechart_4_all_ly.setVisibility(View.GONE);
        }
        showLineHashSet = new HashSet<String>();
        initLineChartView();
    }

    public void initLineChartView() {

        mLineChart = (LineChartView) this.findViewById(R.id.chart);
        mLineChart.setOnValueTouchListener(this);
        mLineChart.setValueSelectionEnabled(true);

    }

    @Override
    public void bindViewsListener() {
        // TODO Auto-generated method stub
        txt_comm_head_right.setOnClickListener(this);

        // dangan_chuqing_info_ss_linechart_4ly1
        // .setOnClickListener(mOnClickListener);
        // dangan_chuqing_info_ss_linechart_4ly2
        // .setOnClickListener(mOnClickListener);

    }

    // 折线图开始：
    private void initLineChartData(List<DanganLineChartVO> chartLine) {
        if (chartLine.size() == 2) {
            chartLine.get(0).setVisible(mFlag[0]);
            chartLine.get(1).setVisible(mFlag[1]);
        }

        List<Line> lines = new ArrayList<Line>();
        for (int i = 0; i < chartLine.size(); ++i) {
            List<DanganLineChartVO> lineValues = chartLine.get(i).getList();
            List<PointValue> values = new ArrayList<PointValue>();
            String name = null;
            if (!chartLine.get(i).isVisible())
                continue;
            for (int j = 0; j < lineValues.size(); ++j) {
                float num = Float.parseFloat(lineValues.get(j).getNum());
                // values.add(new PointValue(j, num));
                values.add(new PointValue(j, (float) Math.random() * 100));
                name = lineValues.get(j).getName();
            }
            setTagAndColor(name, mColors[i]);

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

        mLineChart.getCurrentViewport().top =
                Integer.parseInt(mChuQingVo.getMaxchart()) +
                        Integer.parseInt(mChuQingVo.getMaxchart()) / 6;
        mLineChart.getCurrentViewport().bottom = 0;

        if (Integer.parseInt(mChuQingVo.getMaxchart()) < 18) {
            mLineChart.getCurrentViewport().top = 18;
        }

        int count = 0;// 统计实际存在的折线条数
        for (int i = 0; i < chartLine.size(); ++i) {
            if (!chartLine.get(i).isVisible())
                continue;
            List<DanganLineChartVO> lineValues = chartLine.get(i).getList();
            for (int j = 0; j < lineValues.size(); ++j) {
                float num = Float.parseFloat(lineValues.get(j).getNum());
                PointValue value = mLineData.getLines().get(count).getValues().get(j);
                value.setTarget(value.getX(), num);
            }
            count++;
        }
        mLineChart.startDataAnimation(1000);
    }

    // 折线图结束

    private boolean getData() {

        // http://cp.beisheng.wang/api.php/AddressList/getDeparts/ftoken/RBDTZXGUMNDKkEwMkZFN0UyMTA1RgO0O0OO0O0O/type/1/datetype/1/date/2015-05/userid/22/uid/22
        try {

            getIntent = getIntent();
            uid = getIntent.getStringExtra("uid");
            if (TextUtils.isEmpty(datetype)) {
                datetype = getIntent.getStringExtra("datetype");
            }
            if (TextUtils.isEmpty(type)) {
                type = getIntent.getStringExtra("currentType");
            }
            Map<String, String> paramsMap = new HashMap<String, String>();
            paramsMap.put("type", type);
            // paramsMap.put("type", "currentType");
            paramsMap.put("datetype", datetype);
            /**
             * 查询用户的ID
             */
            paramsMap.put("uid", uid);
            String strUlr = UrlUtil.getUrlByMap1(
                    Constant4Statistics.DANGAN_CHUQING_INFOL_PATH, paramsMap);
            CustomLog.e("WorkUrl1", strUlr);
            String jsonStr = HttpClientUtil.get(strUlr, Constant.ENCODING)
                    .trim();
            Gson gson = new Gson();
            mChuQingVo = gson.fromJson(jsonStr,
                    DanganLineChartVO.class);
            if (Constant.RESULT_CODE.equals(mChuQingVo
                    .getCode())) {
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
        CustomDialog.closeProgressDialog();
        mLoadingLayout.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);
        txt_comm_head_right.setVisibility(View.VISIBLE);
        if (mChuQingVo.getChart() != null) {
            updateUi();
        }
    }

    @Override
    public void executeFailure() {
        super.executeFailure();
    }

    private void setTagAndColor(String tName, int color) {
        // TODO Auto-generated method stub
        if ("迟到".equalsIgnoreCase(tName) || "事假".equalsIgnoreCase(tName)) {
            // dangan_chuqing_info_ss_linechart_4cv1.setBackgroundColor(color);
            dangan_chuqing_info_ss_linechart_4c1.setText(tName);
            dangan_chuqing_info_ss_linechart_4ly1.setTag(tName);
            if (runCount == 0) {
                /**
                 * 首次进来的时候，只显示一条数据
                 */
                // showLineHashSet.add(tName);
            }
        }
        if ("早退".equalsIgnoreCase(tName) || "病假".equalsIgnoreCase(tName)) {
            // dangan_chuqing_info_ss_linechart_4cv2.setBackgroundColor(color);
            dangan_chuqing_info_ss_linechart_4ly2.setTag(tName);
            dangan_chuqing_info_ss_linechart_4c2.setText(tName);
            // showLineHashSet.add(tName);
        } else {
            // showLineHashSet.add(tName);
        }

        if (runCount == 0) {
            /**
             * 首次进来的时候，显示所有的数据
             */
            showLineHashSet.add(tName);
        }
    }

    @Override
    public void onClick(View v) {
        View timepickerview = getLayoutInflater().inflate(R.layout.timepicker,
                null);
        ScreenInfo screenInfo = new ScreenInfo(this);
        final WheelMain wheelMain = new WheelMain(timepickerview);
        wheelMain.screenheight = screenInfo.getHeight();
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.txt_comm_head_right:
                showDateDialog();
                break;

        }

    }

    public void showDateDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View timepickerview = inflater.inflate(R.layout.timepicker, null);
        ScreenInfo screenInfo = new ScreenInfo(this);
        final WheelMain wheelMain = new WheelMain(timepickerview);
        wheelMain.screenheight = screenInfo.getHeight();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        // wheelMain.initDateTimePicker(year, "上", 0, 0, 0, 0);
        wheelMain.initDateTimePickerOneY(year, "上", 0, 0, 0, 0, context);
        timedialog = new BSDialog(this, "选择时间", timepickerview,
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 上半年是0 下半年是1
                        timestr = wheelMain.getHalfYear();
                        datetypetime = Integer.parseInt(timestr.split("-")[1])
                                + 2 + "";
                        CustomLog.e("time", datetypetime);
                        datetype = datetypetime;
                        CustomDialog.showProgressDialog(DanganChuQingInfoLineChart2Activity.this);
                        new ThreadUtil(DanganChuQingInfoLineChart2Activity.this,
                                DanganChuQingInfoLineChart2Activity.this)
                                .start();
                        if (getIntent.getStringExtra("currentType").equals("1")) {

                            if (datetype.equals("3")) {
                                ac_ext_dangan_chuqing_info_linechart_top_txt.setText("下半年缺卡、旷工的情况");
                            }
                            else {
                                ac_ext_dangan_chuqing_info_linechart_top_txt.setText("上半年缺卡、旷工的情况");
                            }
                            dangan_chuqing_info_ss_linechart_4_all_ly.setVisibility(View.GONE);

                        } else if (getIntent.getStringExtra("currentType").equals("2")) {

                            if (datetype.equals("3")) {
                                ac_ext_dangan_chuqing_info_linechart_top_txt.setText("下半年迟到、早退的情况");
                            }
                            else {
                                ac_ext_dangan_chuqing_info_linechart_top_txt.setText("上半年迟到、早退的情况");
                            }

                        } else if (getIntent.getStringExtra("currentType").equals("3")) {
                            if (datetype.equals("3")) {
                                ac_ext_dangan_chuqing_info_linechart_top_txt.setText("下半年未写日志的情况");
                            }
                            else {
                                ac_ext_dangan_chuqing_info_linechart_top_txt.setText("上半年未写日志的情况");
                            }
                            dangan_chuqing_info_ss_linechart_4_all_ly.setVisibility(View.GONE);
                        } else {
                            if (datetype.equals("3")) {
                                ac_ext_dangan_chuqing_info_linechart_top_txt.setText("下半年请假的情况");
                            }
                            else {
                                ac_ext_dangan_chuqing_info_linechart_top_txt.setText("上半年请假的情况");
                            }

                        }

                        timedialog.dismiss();
                    }

                });
        timedialog.show();
    }

    private void viewOnClickListenerByView(View v, ImageView image, int sort) {
        // TODO Auto-generated method stub
        if (!showLineHashSet.contains((String) v.getTag())) {
            image.setImageResource(R.drawable.gou);
            showLineHashSet.add((String) v.getTag());
            mChartLine.get(sort).setVisible(true);
            // 保存当前折线状态
            mFlag[sort] = true;

        } else {
            image.setImageDrawable(null);
            showLineHashSet.remove(v.getTag());
            mChartLine.get(sort).setVisible(false);
            mFlag[sort] = false;
        }
        initLineChartData(mChartLine);
    }

    OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.dangan_chuqing_info2_ss_linechart_4ly1:
                    viewOnClickListenerByView(dangan_chuqing_info_ss_linechart_4ly1, dangan_chuqing_info_ss_linechart_4cv1, 0);
                    break;
                case R.id.dangan_chuqing_info2_ss_linechart_4ly2:
                    viewOnClickListenerByView(dangan_chuqing_info_ss_linechart_4ly2, dangan_chuqing_info_ss_linechart_4cv2, 1);
                    break;
            }
        }
    };

    @Override
    public boolean getDataResult() {
        // TODO Auto-generated method stub
        return getData();
    }

    @Override
    public void onValueDeselected() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
        // TODO Auto-generated method stub
        // 默认显示当前月的信息
        if (getIntent.getStringExtra("currentType").equals("1")) {
            mTitletime1.setText(mChartLine.get(0).getList().get(pointIndex).getNum() + "次");
        } else if (getIntent.getStringExtra("currentType").equals("2")) {
            ac_ext_dangan_chuqing_info_linechart_top_txt.setText("近半年迟到、早退的情况");
            mTitletime1.setText(mChartLine.get(0).getList().get(pointIndex).getNum() + "次");
            mTitletime2.setText(mChartLine.get(1).getList().get(pointIndex).getNum() + "次");
        } else if (getIntent.getStringExtra("currentType").equals("3")) {
            ac_ext_dangan_chuqing_info_linechart_top_txt.setText("近半年未写日志的情况");
            mTitletime1.setText(mChartLine.get(0).getList().get(pointIndex).getNum() + "次");
        } else {
            mTitletime1.setText(mChartLine.get(0).getList().get(pointIndex).getDuration());
            mContentCount1.setText("本月事假次数：" + mChartLine.get(0).getList().get(pointIndex).getNum() + "次");
            mTitletime2.setText(mChartLine.get(1).getList().get(pointIndex).getDuration());
            mContentCount2.setText("本月病假次数：" + mChartLine.get(1).getList().get(pointIndex).getNum() + "次");
            ac_ext_dangan_chuqing_info_linechart_top_txt.setText("近半年请假的情况");
        }

    }
}
