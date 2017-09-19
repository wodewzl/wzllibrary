
package com.bs.bsims.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
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
import com.bs.bsims.adapter.BossStatisticsLeaveAdapter;
import com.bs.bsims.adapter.DepartOneLevelAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.BossStatisticsAttendanceVO;
import com.bs.bsims.model.DepartmentAndEmployeeVO;
import com.bs.bsims.model.ResultVO;
import com.bs.bsims.model.TreeVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CommonUtils.ResultCallback;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSListView;
import com.bs.bsims.view.BSPopupWindowsTitle;
import com.bs.bsims.view.BSPopupWindowsTitle.TreeCallBack;
import com.bs.bsims.view.BSTopIndicator;
import com.bs.bsims.view.BSTopIndicator.OnTopIndicatorListener;
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

public class BossStatisticsLeaveActivity extends BaseActivity implements OnTopIndicatorListener, OnClickListener, LineChartOnValueSelectListener, OnItemClickListener {
    private String[] mArray = {
            "分布", "排行"
    };
    private BossStatisticsAttendanceVO mAttendanceVO;
    private BossStatisticsAttendanceVO mNumberAttendanceVO;
    private BossStatisticsAttendanceVO mTimeAttendanceVO;
    private BossStatisticsAttendanceVO mAllAttendanceVO;// 最开始请求时不分种类；
    private String mType = "1";// 请假分类（'1'=>'事假','2'=>'病假','3'=>'(陪)产假','4'=>'公休假','5'=>'调休假','6'=>'婚假','7'=>'丧假'
    private String mDid = "0";// 0为默认全部部门
    private String mMode = "1";// 查询方式1按人，2按次
    private String mDateType = "";// 1：近半年，2：近三个月，3：上半年，4：下半年
    private String[] mTimeArray = {
            "近半年", "上半年", "下半年"
    };
    private BSPopupWindowsTitle mBsPopupWindowsTitle;

    private LinearLayout mOneTitle, mTwoTitle;
    private TextView mOneTitleTv, mTwoTitleTv;
    private BSTopIndicator mTopIndicator;
    private int[] mDrawableIds = new int[] {
            R.drawable.boss_static_bg_people,
            R.drawable.boss_static_bg_number,
            R.drawable.boss_static_bg_time
    };

    private CharSequence[] mLabels = new CharSequence[] {
            "按人", "按次", "按时长"
    };

    private BSPopupWindwos mDepartPop;

    private List<LinearLayout> mDeapartLayout = new ArrayList<LinearLayout>();
    private int[] mDeapartLayoutIds = new int[] {
            R.id.depart_layout01, R.id.depart_layout02, R.id.depart_layout03,
            R.id.depart_layout04, R.id.depart_layout05
    };
    private int[] mDeapartTvIds = new int[] {
            R.id.depart_text01, R.id.depart_text02, R.id.depart_text03,
            R.id.depart_text04, R.id.depart_text05
    };
    private int[] mDeapartImgIds = new int[] {
            R.id.depart_status01, R.id.depart_status02, R.id.depart_status03,
            R.id.depart_status04, R.id.depart_status05
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
            Color.rgb(0, 169, 254), Color.rgb(255, 174, 30),
            Color.GREEN, Color.RED, Color.rgb(132, 76, 125)
    };

    private LineChartData mLineData;
    private List<BossStatisticsAttendanceVO> mChartLine = new ArrayList<BossStatisticsAttendanceVO>();
    private BSListView mListView;
    private BossStatisticsLeaveAdapter mAttendanceAdapter;
    private TextView mDateTv, mNumberTv;
    private String mCurrentNumber = "0";
    private String mCrrentPeople = "0";
    private String mCurrentTime = "0";
    // private boolean mRefresh = true;// 判断安人，按次是否请求数据
    private ArrayList<String> mModeArray = new ArrayList<String>();// 判断安人，按次,按时长是否请求数据
    private int numberOfLines = 1;
    private int maxNumberOfLines = 4;
    private int numberOfPoints = 12;
    float[][] randomNumbersTab = new float[maxNumberOfLines][numberOfPoints];
    private LineChartView chart;
    private LineChartData data;
    private boolean pointsHaveDifferentColor;

    private int mCurrentIndex = 5;// 当前显示的下标，默认为5；
    private boolean mAllRefresh = false;// 当为全部部门时，点击某个月需要重新请求数据
    private BossStatisticsAttendanceVO mAllDepartDataVO;// 某个月所有部门数据
    private boolean mIsRefresh = true;// 每次刷新数据，上一次不成功，不进行下一次刷数据
    private String mDate;
    private int[] mIcon = new int[] {
            R.drawable.piechart_icon, R.drawable.common_icon
    };
    private TextView mListTitleTv;
    private boolean mDepartBoolean = true;// 选择部门后请求数据，未完成时，不能点击部门，不然会出现问题

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.boss_statistics_leave, mContentLayout);
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
        mTopIndicator.setmLabels(mLabels);
        mTopIndicator.setmDrawableIds(mDrawableIds);
        mTopIndicator.updateUI(this);
        mDepartPop = new BSPopupWindwos(this);// 部门Pop初始化
        initTimePopData();// 时间Pop初始化
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
        mAttendanceAdapter = new BossStatisticsLeaveAdapter(this);
        mListView.setAdapter(mAttendanceAdapter);
        mDateTv = (TextView) findViewById(R.id.date_tv);
        mNumberTv = (TextView) findViewById(R.id.number_tv);
        mOkTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.crm_menu, 0);
        mListTitleTv = (TextView) findViewById(R.id.list_title_tv);
        mModeArray.add("1");
        initData();
    }

    public void initData() {
        Intent intent = this.getIntent();
        String titleName = intent.getStringExtra("title_name");
        mTitleTv.setText(titleName);
        mListTitleTv.setText("部门" + titleName + "排行");

        if (intent.hasExtra("type")) {
            int type = intent.getIntExtra("type", 1);
            switch (type) {
                case 4:
                    mType = "1";
                    break;
                case 10:
                    mType = "7";
                    break;
                case 11:
                    mType = "2";
                    break;
                case 12:
                    mType = "3";
                    break;
                case 13:
                    mType = "6";
                    break;
                case 14:
                    mType = "5";
                    break;
                case 15:
                    mType = "4";
                    break;
                case 9:
                    mType = "9";
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void bindViewsListener() {
        mOkTv.setOnClickListener(this);
        mTopIndicator.setOnTopIndicatorListener(this);
        mOneTitle.setOnClickListener(this);
        mTwoTitle.setOnClickListener(this);
        mDepartLayout01.setOnClickListener(this);
        mDepartLayout02.setOnClickListener(this);
        mDepartLayout03.setOnClickListener(this);
        mDepartLayout04.setOnClickListener(this);
        mDepartLayout05.setOnClickListener(this);
        mListView.setOnItemClickListener(this);
    }

    public void initTitleView() {
        mOneTitle = (LinearLayout) findViewById(R.id.title01);
        mTwoTitle = (LinearLayout) findViewById(R.id.title02);
        findViewById(R.id.title03).setVisibility(View.GONE);
        mOneTitleTv = (TextView) findViewById(R.id.title_name_01);
        mTwoTitleTv = (TextView) findViewById(R.id.title_name_02);
        mOneTitleTv.setText("全部部门");
        mTwoTitleTv.setText("时间筛选");
        initLineChartView();
    }

    public void initLineChartView() {
        mLineChart = (LineChartView) this.findViewById(R.id.chart);
        mLineChart.setOnValueTouchListener(this);
        mLineChart.setZoomEnabled(false);
        mLineChart.setClickable(false);
        mLineChart.setScrollEnabled(false);
    }

    private void initLineChartData(BossStatisticsAttendanceVO vo) {
        List<BossStatisticsAttendanceVO> chartLine = vo.getInfo().getChart();
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
        mLineData.setBaseValue(Float.NEGATIVE_INFINITY);
        mLineChart.setLineChartData(mLineData);
        mLineChart.setViewportCalculationEnabled(false);
        mLineChart.setValueSelectionEnabled(true);
        // 设置y轴最大值
        if ("1".equals(vo.getInfo().getMode())) {
            mLineChart.getCurrentViewport().top =
                    Float.parseFloat(mAttendanceVO.getInfo().getMaxchart()) +
                            Float.parseFloat(mAttendanceVO.getInfo().getMaxchart()) / 6;
        } else if ("2".equals(vo.getInfo().getMode())) {
            mLineChart.getCurrentViewport().top =
                    Float.parseFloat(mNumberAttendanceVO.getInfo().getMaxchart()) +
                            Float.parseFloat(mNumberAttendanceVO.getInfo().getMaxchart()) / 6;
        } else {
            mLineChart.getCurrentViewport().top =
                    (int) Float.parseFloat(mTimeAttendanceVO.getInfo().getMaxchart()) +
                            (int) Float.parseFloat(mTimeAttendanceVO.getInfo().getMaxchart()) / 6;
        }
        mLineChart.getCurrentViewport().bottom = 0;
        if (mLineChart.getCurrentViewport().top < 15)
            mLineChart.getCurrentViewport().top = 15;

        // 准备动画数据
        int count = 0;
        for (int i = 0; i < mChartLine.size(); ++i) {
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

    @Override
    public void executeFailure() {
        super.executeFailure();
        mDepartBoolean = true;
        mListView.showHead(this, false);
    }

    @Override
    public void executeSuccess() {
        super.executeSuccess();
        mDepartBoolean = true;
        mListView.showHead(this, false);

        // 代表的是全部部门，不含折线图
        if (mAllAttendanceVO.getInfo().getChart() == null) {
            mAllDepartDataVO = mAllAttendanceVO;
            mAttendanceAdapter.updateData(mAllDepartDataVO.getInfo().getShow(), mMode);
            setTitleData(mCurrentIndex);
            mAllRefresh = false;
        } else {
            if ("1".equals(mAllAttendanceVO.getInfo().getMode())) {
                mAttendanceVO = mAllAttendanceVO;
                mCrrentPeople = mAttendanceVO.getInfo().getAllnum();
                updateData(mAttendanceVO);
            } else if ("2".equals(mAllAttendanceVO.getInfo().getMode())) {
                mNumberAttendanceVO = mAllAttendanceVO;
                mCurrentNumber = mNumberAttendanceVO.getInfo().getAllnum();
                updateData(mNumberAttendanceVO);
            } else {
                mTimeAttendanceVO = mAllAttendanceVO;
                mCurrentTime = mTimeAttendanceVO.getInfo().getAllnum();
                updateData(mTimeAttendanceVO);
            }
        }
        if (!mModeArray.contains(mAllAttendanceVO.getInfo().getMode())) {
            mModeArray.add(mAllAttendanceVO.getInfo().getMode());
        }
    }

    public void updataByMode(BossStatisticsAttendanceVO vo) {
        if ("1".equals(vo.getInfo().getMode())) {
            mAttendanceVO = vo;
            mCrrentPeople = mAttendanceVO.getInfo().getAllnum();
            updateData(mAttendanceVO);
        } else if ("2".equals(vo.getInfo().getMode())) {
            mNumberAttendanceVO = vo;
            mCurrentNumber = mNumberAttendanceVO.getInfo().getAllnum();
            updateData(mNumberAttendanceVO);
        } else {
            mTimeAttendanceVO = vo;
            mCurrentTime = mTimeAttendanceVO.getInfo().getAllnum();
            updateData(mTimeAttendanceVO);
        }
    }

    public void updateData(BossStatisticsAttendanceVO vo) {
        if (vo.getInfo() == null)
            return;
        // 按人切换后，部门勾选保持 一致
        if (!"0".equals(mDid) && mModeArray.size() != 0) {
            for (int i = 0; i < mChartLine.size(); i++) {
                if (mChartLine.size() == vo.getInfo().getChart().size())
                    vo.getInfo().getChart().get(i).setVisible(mChartLine.get(i).isVisible());
            }
        }

        mChartLine = vo.getInfo().getChart();
        initLineChartData(vo);
        // mDid 为0是全部部门
        if ("0".equals(mDid)) {
            mAttendanceAdapter.updateData(vo.getInfo().getShow(), vo.getInfo().getMode(), mChartLine.get(0).getList().size() - 1);
        } else {
            mAttendanceAdapter.updateData(mChartLine, mChartLine.get(0).getList().size() - 1, vo.getInfo().getMode());
        }
        setTitleData(mChartLine.get(0).getList().size() - 1);
    }

    // 更新数据但不更新折线图
    public void updateDataNoChart(BossStatisticsAttendanceVO vo, int position) {
        mChartLine = vo.getInfo().getChart();
        mAttendanceAdapter.updateData(mChartLine, position, vo.getInfo().getMode());
        setTitleData(position);
    }

    // 设置折线图下面标题的数据
    public void setTitleData(int position) {
        String currentYear = mChartLine.get(0).getList().get(position).getDateTime().split("-")[0];
        String currentMonth = mChartLine.get(0).getList().get(position).getDateTime().split("-")[1];
        CommonUtils.setTextThree(this, mDateTv, currentYear + "年 ", currentMonth, " 月", R.color.C4, 1.5f);
        mDate = currentYear + "-" + currentMonth;

        if (mAllRefresh) {
            // 全部部门每次都是重新请求获取的数据
            mCrrentPeople = mAllDepartDataVO.getInfo().getAllnum();
            mCurrentNumber = mAllDepartDataVO.getInfo().getAllnum();
            mCurrentTime = mAllDepartDataVO.getInfo().getAllnum();
        }

        if ("1".equals(mMode)) {
            CommonUtils.setTextTwoBefore(this, mNumberTv, mCrrentPeople, "人", R.color.C10, 1.5f);
            mNumberTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.boss_static_icon_people, 0, 0, 0);
        } else if ("2".equals(mMode)) {
            CommonUtils.setTextTwoBefore(this, mNumberTv, mCurrentNumber, " 次", R.color.C10, 1.5f);
            mNumberTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.boss_static_icon_number, 0, 0, 0);
        } else {
            CommonUtils.setTextTwoBefore(this, mNumberTv, mCurrentTime, " 小时", R.color.C10, 1.5f);
            mNumberTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.boss_static_icon_time, 0, 0, 0);
        }

        mNumberTv.setCompoundDrawablePadding(CommonUtils.dip2px(this, 5));

    }

    public boolean getData() {
        mDepartBoolean = false;
        try {
            Gson gson = new Gson();
            HashMap<String, String> mapList = new HashMap<String, String>();
            mapList.put("userid", BSApplication.getInstance().getUserId());
            mapList.put("type", mType);
            mapList.put("did", mDid);
            mapList.put("mode", mMode);
            mapList.put("datetype", mDateType);
            mapList.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String url = "";
            if (!"9".equals(mType)) {
                url = Constant.BOSS_STATISTICS_LEAVE;
            } else {
                // 9为加班特殊处理
                url = Constant.BOSS_STATISTICS_OVERTIME;
            }
            String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + url, mapList);
            mAllAttendanceVO = gson.fromJson(jsonStrList, BossStatisticsAttendanceVO.class);
            if (Constant.RESULT_CODE.equals(mAllAttendanceVO.getCode())) {
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
    public void onIndicatorSelected(int index) {
        mTopIndicator.setTabsDisplay(this, index);
        mMode = (index + 1) + "";
        // 安人，按次，是否刷新数据
        if (!mModeArray.contains(mMode)) {
            match(3, (index + 1) + "");
        } else {
            if (index == 0)
                updateData(mAttendanceVO);
            else if (index == 1)
                updateData(mNumberAttendanceVO);
            else
                updateData(mTimeAttendanceVO);

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
            mOkBt.setOnClickListener(new
                    OnClickListener() {
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
                CommonUtils.initPopViewBg(BossStatisticsLeaveActivity.this, mArray, mOkTv, mCallback, mIcon);
                break;
            default:
                break;
        }
    }

    // 右上角添加回调函数
    ResultCallback mCallback = new ResultCallback() {
        @Override
        public void callback(String str, int position) {
            Intent intent = new Intent();
            intent.putExtra("type", mType);
            if (position == 0) {
                intent.putExtra("title_name", mTitleTv.getText().toString() + "分布");
                intent.setClass(BossStatisticsLeaveActivity.this, BossStatisticsleaveDistributedActivity.class);
                startActivity(intent);
            } else {
                intent.putExtra("title_name", mTitleTv.getText().toString() + "排行");
                intent.setClass(BossStatisticsLeaveActivity.this, BossStatisticsLeaveRankActivity.class);
                startActivity(intent);
            }
        }
    };

    public void initTimePopData() {
        ArrayList<TreeVO> list = CommonUtils.getOneLeveTreeVo(mTimeArray);
        mBsPopupWindowsTitle = new BSPopupWindowsTitle(this, list, callback, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    // 菜单点击回调函数
    TreeCallBack callback = new TreeCallBack() {
        @Override
        public void callback(TreeVO vo) {

            if (vo.getLevel() == 1) {
                // 审批一级菜单
                mTwoTitleTv.setText(vo.getName());
                match(2, vo.getSearchId() + "");
            }
        }
    };

    public void match(int key, String value) {
        mListView.showHead(this, true);
        switch (key) {
            case 1:
                mModeArray.clear();
                break;
            case 2:
                mDateType = value;
                mModeArray.clear();
                break;
            case 3:
                mMode = value;
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
        if (mDepartBoolean && mChartLine.size() > sort) {
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
            if ("1".equals(mMode)) {
                mCrrentPeople = getCurrentNumber(mAttendanceVO, mCurrentIndex);
                updateData(mAttendanceVO);
            } else if ("2".equals(mMode)) {
                mCurrentNumber = getCurrentNumber(mNumberAttendanceVO, mCurrentIndex);
                updateData(mNumberAttendanceVO);
            } else {
                mCurrentTime = getCurrentNumber(mTimeAttendanceVO, mCurrentIndex);
                updateData(mTimeAttendanceVO);
            }
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
            } else if ("2".equals(mMode)) {
                mCurrentNumber = getCurrentNumber(mNumberAttendanceVO, arg1);
                updateDataNoChart(mNumberAttendanceVO, arg1);
            } else {
                mCurrentTime = getCurrentNumber(mTimeAttendanceVO, arg1);
                updateDataNoChart(mTimeAttendanceVO, arg1);
            }
        }

    }

    public String getCurrentNumber(BossStatisticsAttendanceVO vo, int sort) {
        List<BossStatisticsAttendanceVO> list = vo.getInfo().getChart();
        float allCount = 0;
        for (int i = 0; i < list.size(); i++) {
            allCount += Float.parseFloat(list.get(i).getList().get(sort).getNum());
        }
        return ((int) allCount) + "";
    }

    // 全部部门点击某个月所显示部门数据和
    public String getCurrentNumberAllDepart(BossStatisticsAttendanceVO vo, int sort) {
        List<BossStatisticsAttendanceVO> list = vo.getInfo().getShow();
        float allCount = 0;
        for (int i = 0; i < list.size(); i++) {
            allCount += Float.parseFloat(list.get(i).getNum());
        }
        return ((int) allCount) + "";
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
            mapList.put("mode", mMode);
            mapList.put("datetype", mDateType);
            String currentYear = mChartLine.get(0).getList().get(mCurrentIndex).getDateTime().split("-")[0];
            String currentMonth = mChartLine.get(0).getList().get(mCurrentIndex).getDateTime().split("-")[1];
            String date = currentYear + "-" + currentMonth;
            mapList.put("date", date);
            mapList.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String url = "";
            if (!"9".equals(mType)) {
                url = Constant.BOSS_STATISTICS_LEAVE_ALL;
            } else {
                // 9为加班特殊处理
                url = Constant.BOSS_STATISTICS_OVERTIME_ALL;
            }
            String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + url, mapList);
            mAllAttendanceVO = gson.fromJson(jsonStrList, BossStatisticsAttendanceVO.class);
            if (Constant.RESULT_CODE.equals(mAllAttendanceVO.getCode())) {
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
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long id) {
        Intent intent = new Intent();
        String did = mAttendanceAdapter.mList.get((int) id).getDid();
        String d = mDateTv.getText().toString();
        intent.putExtra("did", did);
        intent.putExtra("d", mDate);
        if (!"9".equals(mType)) {
            intent.putExtra("class", "1");
        } else {
            intent.putExtra("class", "3");
        }

        intent.putExtra("type", mType);
        intent.putExtra("dname", mAttendanceAdapter.mList.get((int) id).getDname());
        intent.putExtra("type_name", mTitleTv.getText().toString());
        intent.setClass(this, StatisticsApprovalDetailActivity.class);
        startActivity(intent);
    }
}
