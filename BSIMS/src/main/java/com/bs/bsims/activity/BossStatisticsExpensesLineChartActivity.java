/**
 * 
 */

package com.bs.bsims.activity;

/**

 *          BS北盛最帅程序员

 *         Copyright (c) 2016

 *        湖北北盛科技有限公司

 *        @author 梁骚侠

 *        @date 2016-1-27

 *        @version 1.22  Boss统计费用统计
 *        
 *        @expersion:2-14折线图有问题，没做完 到时候统一弄好

 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.BossStatisticsExpensesAdapter;
import com.bs.bsims.adapter.DepartOneLevelAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.BossStatisticsAttendanceVO;
import com.bs.bsims.model.CrmOptionsVO;
import com.bs.bsims.model.DepartmentAndEmployeeVO;
import com.bs.bsims.model.ResultVO;
import com.bs.bsims.model.TreeVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSListView;
import com.bs.bsims.view.BSPopupWindowsTitle;
import com.bs.bsims.view.BSPopupWindowsTitle.TreeCallBack;
import com.bs.bsims.view.BSTopIndicator;
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

public class BossStatisticsExpensesLineChartActivity extends BaseActivity implements OnClickListener, LineChartOnValueSelectListener {
    private BossStatisticsAttendanceVO mAttendanceVO;
    private BossStatisticsAttendanceVO mNumberAttendanceVO;
    private String mType = "0";// 1.缺卡，2缺日志，3迟到，4早退，5加班
    private String mDid = "0";// 0为默认全部部门
    private String mMode = "1";// 查询方式1按人，2按次
    private String mDateType = "";// 1：近半年，2：近三个月，3：上半年，4：下半年
    private BSPopupWindowsTitle mBsPopupWindowsTitle;

    private LinearLayout mOneTitle, mTwoTitle;
    private TextView mOneTitleTv, mTwoTitleTv;
    private BSTopIndicator mTopIndicator;
    private int[] mDrawableIds = new int[] {
            R.drawable.boss_static_bg_people, R.drawable.boss_static_bg_number
    };

    private BSPopupWindwos mDepartPop;

    private List<LinearLayout> mDeapartLayout = new ArrayList<LinearLayout>();
    private int[] mDeapartLayoutIds = new int[] {
            R.id.depart_layout01, R.id.depart_layout02, R.id.depart_layout03, R.id.depart_layout04, R.id.depart_layout05
    };
    private int[] mDeapartTvIds = new int[] {
            R.id.depart_text01, R.id.depart_text02, R.id.depart_text03, R.id.depart_text04, R.id.depart_text05
    };
    private int[] mDeapartImgIds = new int[] {
            R.id.depart_status01, R.id.depart_status02, R.id.depart_status03, R.id.depart_status04, R.id.depart_status05
    };

    private LinearLayout mDepartLayout01;
    private LinearLayout mDepartLayout02;
    private LinearLayout mDepartLayout03;
    private LinearLayout mDepartLayout04;
    private LinearLayout mDepartLayout05;
    private TextView mDepartTv01, mDepartTv02, mDepartTv03, mDepartTv04, mDepartTv05;
    private ImageView mImageView01, mImageView02, mImageView03, mImageView04, mImageView05;

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
            Color.rgb(0, 169, 254), Color.rgb(255, 174, 30), Color.GREEN, Color.RED, Color.rgb(132, 76, 125)
    };

    private LineChartData mLineData;
    private List<BossStatisticsAttendanceVO> mChartLine = new ArrayList<BossStatisticsAttendanceVO>();
    private BSListView mListView;
    private BossStatisticsExpensesAdapter mAttendanceAdapter;
    private TextView mDateTv, mNumberTv,list_title_tv;
    private String mCurrentNumber;
    private String mCrrentPeople;
    private int numberOfLines = 1;
    private int maxNumberOfLines = 4;
    private int numberOfPoints = 12;
    float[][] randomNumbersTab = new float[maxNumberOfLines][numberOfPoints];
    private LineChartView chart;
    private LineChartData data;
    private boolean pointsHaveDifferentColor;

    private boolean mFlag = true;
    private CrmOptionsVO mCrmOptionsVO;
    private Context mContext;
    private int mCurrentIndex = 5;// 当前显示的下标，默认为5；
    private boolean mAllRefresh = false;// 当为全部部门时，点击某个月需要重新请求数据
    private BossStatisticsAttendanceVO mAllDepartDataVO;// 某个月所有部门数据

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.boss_statistics_attendance, mContentLayout);
        mContext = this;
    }

    @Override
    public boolean getDataResult() {
        if (mAllRefresh) {
            return getAllDepartByMonth();
        } else {
            return getData();
        }
    }

    @Override
    public void updateUi() {

    }

    @Override
    public void initView() {
        initTitleView();
        mTopIndicator = (BSTopIndicator) findViewById(R.id.top_indicator);
        mTopIndicator.setVisibility(View.GONE);
        // mTopIndicator.setmLabels(mLabels);
        // mTopIndicator.setmDrawableIds(mDrawableIds);
        // mTopIndicator.updateUI(this);
        mTitleTv.setText("费用统计");
        mDepartPop = new BSPopupWindwos(this);// 部门Pop初始化
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
        mListView = (BSListView) findViewById(R.id.list_view);
        list_title_tv= (TextView) findViewById(R.id.list_title_tv);
        mAttendanceAdapter = new BossStatisticsExpensesAdapter(this);
        mListView.setAdapter(mAttendanceAdapter);
        mDateTv = (TextView) findViewById(R.id.date_tv);
        mNumberTv = (TextView) findViewById(R.id.number_tv);
        mOkTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.pie, 0);//
        list_title_tv.setText("部门费用排行");
    }

    @Override
    public void bindViewsListener() {
        mOkTv.setOnClickListener(this);
        mOneTitle.setOnClickListener(this);
        mTwoTitle.setOnClickListener(this);
        mDepartLayout01.setOnClickListener(this);
        mDepartLayout02.setOnClickListener(this);
        mDepartLayout03.setOnClickListener(this);
        mDepartLayout04.setOnClickListener(this);
        mDepartLayout05.setOnClickListener(this);

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub

                if (mAttendanceAdapter.mList.size() > 0 && null != mAttendanceAdapter) {
                    Intent intent = new Intent();
                    String did = mAttendanceAdapter.mList.get((int) arg3).getDid();
                    String d1[] = mDateTv.getText().toString().split("年");
                    String d = d1[0] + "-" + d1[1].trim().split("月")[0].trim();
                    intent.putExtra("did", did);
                    intent.putExtra("class", "4");
                    intent.putExtra("type", mType);
                    intent.putExtra("dname", mAttendanceAdapter.mList.get((int) arg3).getDname());
                    intent.putExtra("type_name", mTitleTv.getText().toString());
                    intent.putExtra("d", d);
                    intent.setClass(mContext, StatisticsApprovalDetailActivity.class);
                    startActivity(intent);
                }

            }
        });

    }

    public void initTitleView() {
        mOneTitle = (LinearLayout) findViewById(R.id.title01);
        mTwoTitle = (LinearLayout) findViewById(R.id.title02);
        findViewById(R.id.title03).setVisibility(View.GONE);
        mOneTitleTv = (TextView) findViewById(R.id.title_name_01);
        mTwoTitleTv = (TextView) findViewById(R.id.title_name_02);
        mOneTitleTv.setText("全部部门");
        mTwoTitleTv.setText("全部类别");
        initLineChartView();
    }

    public void initLineChartView() {
        mLineChart = (LineChartView) this.findViewById(R.id.chart);
        mLineChart.setOnValueTouchListener(this);
        mLineChart.setValueSelectionEnabled(true);
        mLineChart.setZoomEnabled(false);
        mLineChart.setClickable(false);
        mLineChart.setScrollEnabled(false);

    }

    private void initLineChartData(List<BossStatisticsAttendanceVO> chartLine) {

        List<Line> lines = new ArrayList<Line>();
        for (int i = 0; i < chartLine.size(); ++i) {
            List<BossStatisticsAttendanceVO> lineValues = chartLine.get(i).getList();
            List<PointValue> values = new ArrayList<PointValue>();
            if (!chartLine.get(i).isVisible())
                continue;
            for (int j = 0; j < lineValues.size(); ++j) {
                // float num = Float.parseFloat(lineValues.get(j).getNum());
                float num = 0.0f;
                values.add(new PointValue(j, num));
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
        mLineChart.setViewportCalculationEnabled(false);
        // 设置y轴最大值
        if ("1".equals(mMode)) {
            mLineChart.getCurrentViewport().top = Float.parseFloat(mAttendanceVO.getInfo().getMaxchart()) + Float.parseFloat(mAttendanceVO.getInfo().getMaxchart()) / 6;
        } else {
            mLineChart.getCurrentViewport().top = Float.parseFloat(mNumberAttendanceVO.getInfo().getMaxchart()) + Float.parseFloat(mNumberAttendanceVO.getInfo().getMaxchart()) / 6;
        }
        mLineChart.getCurrentViewport().bottom = 0;

        // 准备动画数据
        int count = 0;
        for (int i = 0; i < mChartLine.size(); ++i) {
            if (!chartLine.get(i).isVisible())
                continue;
            List<BossStatisticsAttendanceVO> lineValues = chartLine.get(i).getList();

            for (int j = 0; j < lineValues.size(); ++j) {
                float num = Float.parseFloat(lineValues.get(j).getNum());
                System.out.println("=========>: " + num);
                PointValue value = mLineData.getLines().get(count).getValues().get(j);
                value.setTarget(value.getX(), num);
            }
            count++;
        }
        mLineChart.startDataAnimation(1000);

    }

    @Override
    public void executeFailure() {
        super.executeFailure();
    }

    @Override
    public void executeSuccess() {
        super.executeSuccess();

        if (mFlag) {
            if (mCrmOptionsVO == null)
                return;
            createLeftPop();
            mFlag = false;
        }

        mListView.showHead(this, false);
        if (mAllRefresh) {
            mAttendanceAdapter.updateData(mAllDepartDataVO.getInfo().getShow(), mMode, mCurrentIndex);
            setTitleData(mCurrentIndex);
            mAllRefresh = false;
        } else {
            mCrrentPeople = mAttendanceVO.getInfo().getAllnum();
            updateData(mAttendanceVO);

        }

    }

    public void createLeftPop() {
        ArrayList<TreeVO> list1 = CommonUtils.getOneLeveTreeVo(mCrmOptionsVO.getInfo());
        mBsPopupWindowsTitle = new BSPopupWindowsTitle(this, list1, callback, CommonUtils.getScreenHigh(mContext) / 3);
    }

    // 菜单点击回调函数
    TreeCallBack callback = new TreeCallBack() {

        @Override
        public void callback(TreeVO vo) {
            // 审批一级菜单
            mTwoTitleTv.setText(vo.getName());
            list_title_tv.setText("部门"+vo.getName()+"排行");
            match(1, vo.getSearchId() + "");
        }
    };

    public void updateData(BossStatisticsAttendanceVO vo) {
        mChartLine = vo.getInfo().getChart();
        initLineChartData(mChartLine);
        // mDid 为0是全部部门
        if ("0".equals(mDid)) {
            mAttendanceAdapter.updateData(vo.getInfo().getShow(), mMode, mChartLine.get(0).getList().size() - 1);
        } else {
            mAttendanceAdapter.updateData(mChartLine, mChartLine.get(0).getList().size() - 1, mMode);
        }

        setTitleData(mCurrentIndex);
    }

    // 更新数据但不更新折线图
    public void updateDataNoChart(BossStatisticsAttendanceVO vo, int position) {
        mChartLine = vo.getInfo().getChart();
        mAttendanceAdapter.updateData(mChartLine, position, mMode);
        setTitleData(position);
    }

    // 设置折线图下面标题的数据
    public void setTitleData(int position) {
        if (mAllRefresh) {
            // 全部部门每次都是重新请求获取的数据
            String currentYear = mChartLine.get(0).getList().get(position).getDateTime().split("-")[0];
            String currentMonth = mChartLine.get(0).getList().get(position).getDateTime().split("-")[1];
            CommonUtils.setTextThree(this, mDateTv, currentYear + "年 ", currentMonth, " 月", R.color.C4, 1.5f);
            CommonUtils.setTextTwoBefore(this, mNumberTv, getCurrentNumberAllDepart(mAllDepartDataVO, mCurrentIndex), "元", R.color.C10, 1.5f);
            mNumberTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.boss_static_icon_money, 0, 0, 0);
        } else {
            String currentYear = mChartLine.get(0).getList().get(position).getDateTime().split("-")[0];
            String currentMonth = mChartLine.get(0).getList().get(position).getDateTime().split("-")[1];
            CommonUtils.setTextThree(this, mDateTv, currentYear + "年 ", currentMonth, " 月", R.color.C4, 1.5f);

            CommonUtils.setTextTwoBefore(this, mNumberTv, mCrrentPeople, "元", R.color.C10, 1.5f);
            mNumberTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.boss_static_icon_money, 0, 0, 0);

            mNumberTv.setCompoundDrawablePadding(CommonUtils.dip2px(this, 5));
        }

    }

    public boolean getData() {

        Gson gson = new Gson();
        if (mFlag) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.BOSS_STATISTICS_EXPENSE_INDEXOPTIONS, map);
            mCrmOptionsVO = gson.fromJson(jsonStr, CrmOptionsVO.class);
        }

        try {
            HashMap<String, String> mapList = new HashMap<String, String>();
            mapList.put("userid", BSApplication.getInstance().getUserId());
            mapList.put("type", mType);
            mapList.put("did", mDid);
            mapList.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.BOSS_STATISTICS_EXPENSE_INDEX, mapList);
            if ("1".equals(mMode)) {
                mAttendanceVO = gson.fromJson(jsonStrList, BossStatisticsAttendanceVO.class);
                if (Constant.RESULT_CODE.equals(mAttendanceVO.getCode())) {
                    return true;
                }
            } else {
                mNumberAttendanceVO = gson.fromJson(jsonStrList, BossStatisticsAttendanceVO.class);
                if (Constant.RESULT_CODE.equals(mNumberAttendanceVO.getCode())) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
        }
    }

    // 部门多选弹出框
    private class BSPopupWindwos extends PopupWindow {
        private Context mContext;
        private Activity mActivity;
        private ListView mDepartListView;
        private DepartOneLevelAdapter mDepartAdapter;
        private Button mOkBt;
        private int mType;
        private Button mBt;
        private CheckedTextView mAllDepart;
        private List<DepartmentAndEmployeeVO> mSelcetVOList;

        public BSPopupWindwos(Context context) {
            this.mContext = context;
            this.mActivity = (Activity) context;
            View view = View.inflate(context, R.layout.approval_pop_item_02, null);
            view.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View arg0, MotionEvent arg1) {
                    dismiss();
                    return false;
                }
            });
            mSelcetVOList = new ArrayList<DepartmentAndEmployeeVO>();
            mDepartListView = (ListView) view.findViewById(R.id.lv_department);
            mDepartAdapter = new DepartOneLevelAdapter(context);
            mDepartListView.setAdapter(mDepartAdapter);
            List<DepartmentAndEmployeeVO> list = new ArrayList<DepartmentAndEmployeeVO>();
            List<DepartmentAndEmployeeVO> allList = ResultVO.getInstance().getDepartments();
            final DepartmentAndEmployeeVO allDepartVO = new DepartmentAndEmployeeVO();
            allDepartVO.setHaschild(false);
            allDepartVO.setDepartmentid("0");
            allDepartVO.setDname("全部");
            allDepartVO.setSelected(true);
            list.add(allDepartVO);
            mSelcetVOList.add(allDepartVO);
            for (int i = 0; i < allList.size(); i++) {
                if ("0".equals(allList.get(i).getBelong())) {
                    list.add(allList.get(i));
                }
            }
            mDepartAdapter.updateData(list);
            mDepartListView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> view, View arg1, int arg2, long position) {
                    if (mDepartAdapter.mList.size() == 0)
                        return;
                    DepartmentAndEmployeeVO vo = (DepartmentAndEmployeeVO) mDepartAdapter.getItem((int) position);
                    if (vo.isSelected()) {
                        vo.setSelected(false);
                        if (mSelcetVOList.contains(vo))
                            mSelcetVOList.remove(vo);
                    } else {
                        if (mSelcetVOList.size() >= 5) {
                            CustomToast.showLongToast(mContext, "最多选择5个部门");
                            return;
                        }
                        vo.setSelected(true);
                        if (!mSelcetVOList.contains(vo)) {
                            mSelcetVOList.add(vo);
                        }
                        if ("0".equals(vo.getDepartmentid())) {
                            // 点击全部部门若其它的有选择的要先清除掉，只留下全部部门
                            for (int i = 0; i < mSelcetVOList.size(); i++) {
                                mSelcetVOList.get(i).setSelected(false);
                            }
                            mSelcetVOList.clear();
                            vo.setSelected(true);
                            mSelcetVOList.add(vo);
                            // 全部部门不用ok直接查询
                            setDepartCheckView(mSelcetVOList);
                            match(1, "");
                            dismiss();
                        } else {
                            // 点其它部门要清除全部部门
                            if (mSelcetVOList.contains(allDepartVO)) {
                                mSelcetVOList.remove(allDepartVO);
                                allDepartVO.setSelected(false);
                            }
                        }
                    }
                    mDepartAdapter.notifyDataSetChanged();
                }
            });
            mOkBt = (Button) view.findViewById(R.id.ok_bt);
            mOkBt.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSelcetVOList.size() == 0) {
                        allDepartVO.setSelected(true);
                        mSelcetVOList.add(allDepartVO);
                        mDepartAdapter.notifyDataSetChanged();
                    }
                    setDepartCheckView(mSelcetVOList);
                    match(1, "");
                    dismiss();
                }
            });
            view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in));
            setWidth(LayoutParams.MATCH_PARENT);
            setHeight(LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            update();
        }

        public void showPopupWindow(View parent) {
            if (!this.isShowing()) {
                this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 0);
            } else {
                this.dismiss();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title01:
                if (mDepartPop != null)
                    mDepartPop.showAsDropDown(v);
                break;
            case R.id.title02:
                if (mBsPopupWindowsTitle != null) {
                    mBsPopupWindowsTitle.showPopupWindow(mOneTitle);
                }
                break;

            case R.id.depart_layout01:
                if (!"全部部门".equals(mDepartTv01.getText().toString()))
                    departOnclick(mDepartLayout01, mDepartTv01, mImageView01, 0);
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
            case R.id.txt_comm_head_right:
                Intent i = new Intent();
                i.setClass(BossStatisticsExpensesLineChartActivity.this, BossStatisticsExpensesPieChartActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }

    public void match(int key, String value) {
        mListView.showHead(this, true);
        switch (key) {
            case 1:
                // 类型的菜单id
                mType = value;
                break;
            case 2:
                break;

            default:
                break;
        }
        new ThreadUtil(this, this).start();
    }

    // 选择部门后显示部门的圆圈
    public void setDepartCheckView(List<DepartmentAndEmployeeVO> list) {
        StringBuffer sb = new StringBuffer();
        for (int k = 0; k < mDeapartLayoutIds.length; k++) {
            if (k < list.size()) {
                LinearLayout layout = (LinearLayout) this.findViewById(mDeapartLayoutIds[k]);
                layout.setVisibility(View.VISIBLE);
                mDeapartLayout.add(layout);
                TextView tv = (TextView) this.findViewById(mDeapartTvIds[k]);
                tv.setText(list.get(k).getDepartmentid());
                tv.setText(list.get(k).getDname());
                tv.setTag(list.get(k).getDepartmentid());
                tv.setTextColor(this.getResources().getColor(R.color.C7));
                ImageView img = (ImageView) this.findViewById(mDeapartImgIds[k]);
                img.setImageResource(R.drawable.gou);
                sb.append(list.get(k).getDepartmentid());
                if (k != list.size() - 1) {
                    sb.append(",");
                }
            } else {
                this.findViewById(mDeapartLayoutIds[k]).setVisibility(View.GONE);
            }
        }
        mDid = sb.toString();
    }

    public void departOnclick(LinearLayout layout, TextView tv, ImageView image, int sort) {
        try {
            if (mDeapartLayout.contains(layout)) {
                if (mDeapartLayout.size() < 2)
                    return;
                mDeapartLayout.remove(layout);
                tv.setTextColor(Color.parseColor("#666666"));
                image.setImageDrawable(null);
                mChartLine.get(sort).setVisible(false);
            } else {
                mDeapartLayout.add(layout);
                tv.setTextColor(Color.parseColor("#00a9fe"));
                image.setImageResource(R.drawable.gou);
                mChartLine.get(sort).setVisible(true);
            }
            // initLineChartData(mChartLine);
            updateData(mAttendanceVO);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onValueDeselected() {

    }

    @Override
    public void onValueSelected(int arg0, int arg1, PointValue arg2) {

        mCurrentIndex = arg1;
        if ("0".equals(mDid)) {
            mAllRefresh = true;
            mListView.showHead(this, true);
            new ThreadUtil(this, this).start();
        } else {
            mAllRefresh = false;
            if ("1".equals(mMode)) {
                mCrrentPeople = getCurrentNumber(mAttendanceVO, arg1);
                updateDataNoChart(mAttendanceVO, arg1);
            } else {
                mCurrentNumber = getCurrentNumber(mNumberAttendanceVO, arg1);
                updateDataNoChart(mNumberAttendanceVO, arg1);
            }
        }

    }

    public String getCurrentNumber(BossStatisticsAttendanceVO vo, int sort) {
        List<BossStatisticsAttendanceVO> list = vo.getInfo().getChart();
        int allCount = 0;
        for (int i = 0; i < list.size(); i++) {
            allCount += Float.parseFloat(list.get(i).getList().get(sort).getNum());
        }
        return allCount + "";
    }

    // 全部部门点击某个月所显示部门数据和
    public String getCurrentNumberAllDepart(BossStatisticsAttendanceVO vo, int sort) {
        List<BossStatisticsAttendanceVO> list = vo.getInfo().getShow();
        int allCount = 0;
        for (int i = 0; i < list.size(); i++) {
            allCount += Float.parseFloat(list.get(i).getNum());
        }
        return allCount + "";
    }

    private void prepareDataAnimation() {
        for (Line line : mLineData.getLines()) {
            for (PointValue value : line.getValues()) {
                value.setTarget(value.getX(), (float) Math.random() * 100);
            }
        }
    }

    // 部门为全部时，点击某个月份，单独请求数据获取全部部门
    public boolean getAllDepartByMonth() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> mapList = new HashMap<String, String>();
            mapList.put("userid", BSApplication.getInstance().getUserId());
            mapList.put("type", mType);
            mapList.put("did", mDid);
            mapList.put("datetype", mDateType);
            String currentYear = mChartLine.get(0).getList().get(mCurrentIndex).getDateTime().split("-")[0];
            String currentMonth = mChartLine.get(0).getList().get(mCurrentIndex).getDateTime().split("-")[1];
            String date = currentYear + "-" + currentMonth;
            mapList.put("date", date);
            mapList.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.BOSS_STATISTICS_EXPENSE_INDEXE_ALL, mapList);
            mAllDepartDataVO = gson.fromJson(jsonStrList, BossStatisticsAttendanceVO.class);
            if (Constant.RESULT_CODE.equals(mAllDepartDataVO.getCode())) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
        }
    }

}
