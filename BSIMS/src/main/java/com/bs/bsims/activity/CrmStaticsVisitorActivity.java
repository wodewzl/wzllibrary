
package com.bs.bsims.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.BSBaseAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.BossStatisticsAttendanceVO;
import com.bs.bsims.model.CrmStatisticsVistedVo;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class CrmStaticsVisitorActivity extends BaseActivity implements LineChartOnValueSelectListener, OnClickListener, OnItemClickListener {

    private List<LinearLayout> mDeapartLayout = new ArrayList<LinearLayout>();
    private LinearLayout mDepartLayout01, mDepartLayout02, mDepartLayout03, mDepartLayout04, mDepartLayout05;
    private TextView mDepartTv01, mDepartTv02, mDepartTv03, mDepartTv04, mDepartTv05;
    private ImageView mImageView01, mImageView02, mImageView03, mImageView04, mImageView05;
    private TextView mDateTv, mNumberTv;
    private TextView mTitle_01, mTitle_02, mTitle_03;
    private BSListView mListView;
    private CrmStaticsVisitorAdapter mTaskAdapter;

    private CrmStatisticsVistedVo mNumberAttendanceVO;
    private CrmStatisticsVistedVo mNumberMonthAttendanceVO;
    private List<CrmStatisticsVistedVo> mNumList;
    private List<CrmStatisticsVistedVo> mNext;
    private List<CrmStatisticsVistedVo> mLast;
    private Boolean monthFlag = true;// 数据加载出来后，才可以再次请求数据
    // 弹出框
    private BSPopupWindowsTitle mBsPopupWindowsTitle;
    private String mDateType = "";// 1：近半年，2：近三个月，3：上半年，4：下半年
    private String[] mTimeArray = {
            "近半年", "上半年", "下半年"
    };

    // 定义折线图相关变量
    private List<CrmStatisticsVistedVo> mChartLine = new ArrayList<CrmStatisticsVistedVo>();
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
    private TextView mHeadBackTv, mMenuTv;
    private String mDid, mUid, mDate;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.crm_statics_visitor_activity, mContentLayout);
        baseHeadLayout.setBackgroundColor(getResources().getColor(R.color.translucent));
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
            mapList.put("did", mDid);
            mapList.put("uid", mUid);
            mapList.put("date", mDate);
            mapList.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.CRM_STATICS_VISTED, mapList);

            mNumberAttendanceVO = gson.fromJson(jsonStrList, CrmStatisticsVistedVo.class);
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
        super.executeSuccess();
        baseHeadLayout.setBackgroundColor(Color.parseColor("#336A91"));
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
        CrmStatisticsVistedVo vo = mNumberAttendanceVO.getInfo();
        mListView.showHead(this, false);
        if (vo.getChart() != null) {
            mChartLine = vo.getChart();
            mTitle_01.setText(vo.getAllCustomer());
            mTitle_02.setText(vo.getAllNum());
            mTitle_03.setText(vo.getAverage());
            initLineChartData(mChartLine);
        }
        mNext = vo.getNext();

        setTaskAdapterData();
    }

    private void setTaskAdapterData() {
        if (mNumberAttendanceVO == null || mNumberAttendanceVO.getInfo() == null) {
            return;
        }
        CommonUtils.setTextThree(this, mDateTv, mNumberAttendanceVO.getInfo().getMonth().split("-")[0] + "年", mNumberAttendanceVO.getInfo().getMonth().split("-")[1], "月", R.color.black, 1.5f);
        CommonUtils.setTextTwoBefore(this, mNumberTv, mNumberAttendanceVO.getInfo().getMonthNum(), "次", R.color.C10, 1.5f);

        if (mNext == null) {
            mListView.setVisibility(View.GONE);
            this.findViewById(R.id.list_title_tv).setVisibility(View.GONE);
            // this.findViewById(R.id.divider_tv).setVisibility(View.GONE);
        } else {
            mListView.setVisibility(View.VISIBLE);
            this.findViewById(R.id.list_title_tv).setVisibility(View.VISIBLE);
            // this.findViewById(R.id.divider_tv).setVisibility(View.VISIBLE);
            mTaskAdapter.updateData(mNext);
        }

        mTaskAdapter.notifyDataSetChanged();
    }

    @Override
    public void initView() {
        mTitleTv.setText("跟单分析");
        mHeadBackTv = (TextView) findViewById(R.id.head_back);
        mMenuTv = (TextView) findViewById(R.id.menu_tv);
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
        mTitle_01 = (TextView) findViewById(R.id.title_01);
        mTitle_02 = (TextView) findViewById(R.id.title_02);
        mTitle_03 = (TextView) findViewById(R.id.title_03);
        mDateTv = (TextView) findViewById(R.id.date_tv);
        mNumberTv = (TextView) findViewById(R.id.number_tv);
        mListView = (BSListView) findViewById(R.id.task_List);
        mNext = new ArrayList<CrmStatisticsVistedVo>();
        mLast = new ArrayList<CrmStatisticsVistedVo>();
        mHeadLayout.setVisibility(View.GONE);
        mTaskAdapter = new CrmStaticsVisitorAdapter(CrmStaticsVisitorActivity.this);
        mListView.setAdapter(mTaskAdapter);
        mTaskAdapter.updateData(mNext);
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        mOkTv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.cq_timepick), null, null, null);
        mNumberTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.boss_static_icon_people, 0, 0, 0);
        mNumberTv.setCompoundDrawablePadding(CommonUtils.dip2px(this, 5));
        // addStatusList();
        // addLayout();
        initLineChartView();
        initData();
    }

    public void initData() {
        Intent intent = this.getIntent();
        mUid = intent.getStringExtra("uid");
        mDid = intent.getStringExtra("did");
    }

    public void initLineChartView() {
        mLineChart = (LineChartView) this.findViewById(R.id.chart);
        mLineChart.setOnValueTouchListener(this);
        mLineChart.setValueSelectionEnabled(true);
    }

    @Override
    public void bindViewsListener() {
        // mDepartLayout01.setOnClickListener(this);
        // mDepartLayout02.setOnClickListener(this);
        // mDepartLayout03.setOnClickListener(this);
        // mDepartLayout04.setOnClickListener(this);
        // mDepartLayout05.setOnClickListener(this);]
        mHeadBackTv.setOnClickListener(this);
        mMenuTv.setOnClickListener(this);
        mListView.setOnItemClickListener(this);

    }

    // 菜单点击回调函数
    TreeCallBack callback = new TreeCallBack() {

        @Override
        public void callback(TreeVO vo) {
            // 审批一级菜单
            mOkTv.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            mOkTv.setText(vo.getName());
            mDateType = vo.getSearchId();
            new ThreadUtil(CrmStaticsVisitorActivity.this, CrmStaticsVisitorActivity.this).start();

        }
    };

    //
    // @Override
    // public void onClick(View arg0) {
    // switch (arg0.getId()) {
    //
    // case R.id.depart_layout01:
    // departOnclick(mDepartLayout02, mDepartTv01, mImageView01, 0);
    // break;
    // case R.id.depart_layout02:
    // departOnclick(mDepartLayout02, mDepartTv02, mImageView02, 1);
    // break;
    // case R.id.depart_layout03:
    // departOnclick(mDepartLayout03, mDepartTv03, mImageView03, 2);
    // break;
    // case R.id.depart_layout04:
    // departOnclick(mDepartLayout04, mDepartTv04, mImageView04, 3);
    // break;
    // case R.id.depart_layout05:
    // departOnclick(mDepartLayout05, mDepartTv05, mImageView05, 4);
    // break;
    //
    // default:
    // break;
    // }
    // }

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

    // 折线图开始：
    private void initLineChartData(List<CrmStatisticsVistedVo> chartLine) {

        List<Line> lines = new ArrayList<Line>();
        for (int i = 0; i < chartLine.size(); ++i) {
            List<CrmStatisticsVistedVo> lineValues = chartLine.get(i).getList();
            List<PointValue> values = new ArrayList<PointValue>();
            for (int j = 0; j < lineValues.size(); ++j) {
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
            List<CrmStatisticsVistedVo> lineValues = chartLine.get(i).getList();
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
    }

    // 折线图结束

    @Override
    public void onValueDeselected() {

    }

    @Override
    public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
        mListView.showHead(this, true);
        mDate = mChartLine.get(0).getList().get(pointIndex).getDateTime();
        new ThreadUtil(this, this).start();

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mListView.showHead(CrmStaticsVisitorActivity.this, false);
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
                    + Constant.CRM_STATICS_VISTED, mapList);

            mNumberMonthAttendanceVO = gson.fromJson(jsonStrList, CrmStatisticsVistedVo.class);
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
        CommonUtils.setTextThree(this, mDateTv, mNumberMonthAttendanceVO.getInfo().getMonth().split("-")[0] + "年", mNumberMonthAttendanceVO.getInfo().getMonth().split("-")[1], "月 （全部）", R.color.black, 1.5f);
        CommonUtils.setTextTwoBefore(this, mNumberTv, mNumberMonthAttendanceVO.getInfo().getMonthNum(), "次", R.color.C10, 1.5f);
        mTaskAdapter.updateData(mNumberMonthAttendanceVO.getInfo().getNext());
        mTaskAdapter.notifyDataSetChanged();
    }

    public String getCurrentNumber(BossStatisticsAttendanceVO vo, int sort) {
        List<BossStatisticsAttendanceVO> list = vo.getInfo().getChart();
        int allCount = 0;
        for (int i = 0; i < list.size(); i++) {
            allCount += Integer.parseInt(list.get(i).getList().get(sort).getNum());
        }
        return allCount + "";
    }

    public class CrmStaticsVisitorAdapter extends BSBaseAdapter<CrmStatisticsVistedVo> {
        private Context mContext;

        public CrmStaticsVisitorAdapter(Context context) {
            super(context);
            mContext = context;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;

            if (mIsEmpty) {
                return super.getView(position, convertView, parent);
            }

            if (convertView != null && convertView.getTag() == null)
                convertView = null;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.lv_statistics_leave, null);
                holder.rank = (TextView) convertView.findViewById(R.id.rank_text);
                holder.img = (ImageView) convertView.findViewById(R.id.img);
                holder.type = (TextView) convertView.findViewById(R.id.type);
                holder.sort = (TextView) convertView.findViewById(R.id.sort);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            CrmStatisticsVistedVo vo = mList.get(position);
            if (0 == position) {
                holder.img.setVisibility(View.VISIBLE);
                holder.rank.setVisibility(View.GONE);
                holder.img.setImageResource(R.drawable.static_num_01);
            } else if (1 == position) {
                holder.img.setVisibility(View.VISIBLE);
                holder.rank.setVisibility(View.GONE);
                holder.img.setImageResource(R.drawable.static_num_02);
            } else if (2 == position) {
                holder.img.setVisibility(View.VISIBLE);
                holder.rank.setVisibility(View.GONE);
                holder.img.setImageResource(R.drawable.static_num_03);
            } else {
                holder.img.setVisibility(View.GONE);
                holder.rank.setVisibility(View.VISIBLE);
                holder.rank.setBackgroundResource(R.drawable.static_sale_value_rank);
                holder.rank.setText(String.valueOf(position + 1));
            }

            holder.type.setText(vo.getFullname());
            holder.sort.setText(vo.getNum());
            holder.sort.setCompoundDrawablePadding(CommonUtils.dip2px(mContext, 5));
            if ("1".equals(vo.getContrast())) {
                holder.sort.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.statistics_down), null);
            } else if ("2".equals(vo.getContrast())) {
                holder.sort.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.statistics_same), null);
            } else if ("3".equals(vo.getContrast())) {
                holder.sort.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.statistics_up), null);
            }
            return convertView;
        }

        public void updateData(List<CrmStatisticsVistedVo> list, int sort, String mode) {
            List<CrmStatisticsVistedVo> sortList = new ArrayList<CrmStatisticsVistedVo>();
            for (int i = 0; i < list.size(); i++) {
                /*
                 * if (!list.get(i).isVisible()) continue;
                 */
                sortList.add(list.get(i).getList().get(sort));
            }
            sortList(sortList);
            super.updateData(sortList);
        }

        public void updateData(List<CrmStatisticsVistedVo> list) {
            super.updateData(list);
        }

        // 全部部门时sort 为默认最后一个
        public void updateData(List<CrmStatisticsVistedVo> list, String mode, int sort) {
            super.updateData(list);
        }

        public void sortList(List<CrmStatisticsVistedVo> list) {
            Comparator comp = new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    float one = 0;
                    float two = 0;
                    CrmStatisticsVistedVo p1 = (CrmStatisticsVistedVo) o1;
                    CrmStatisticsVistedVo p2 = (CrmStatisticsVistedVo) o2;
                    one = Float.parseFloat(p1.getNum());
                    two = Float.parseFloat(p2.getNum());
                    if (one < two) {
                        return 1;
                    } else if (one == two) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            };
            Collections.sort(list, comp);
        }
    }

    static class ViewHolder {
        private TextView rank, name, sort, type;
        private ImageView img;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_tv:
                Intent intent = new Intent();
                intent.setClass(this, CrmPeopleAddDepartSelectActivity.class);
                startActivityForResult(intent, 2015);
                break;

            case R.id.head_back:
                this.finish();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 2015) {
            if (data == null)
                return;
            mDate = "";
            mDid = data.getStringExtra("did");
            mUid = data.getStringExtra("userid");
            mListView.showHead(this, true);
            new ThreadUtil(this, this).start();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> listView, View arg1, int arg2, long arg3) {
        CrmStatisticsVistedVo vo = (CrmStatisticsVistedVo) listView.getAdapter().getItem(arg2);
        Intent intent = new Intent();
        intent.putExtra("uid", vo.getUserid());
        intent.putExtra("date", mDate);
        intent.putExtra("type", "1");
        intent.setClass(this, SalesmanHomeActivity.class);
        startActivity(intent);
    }
}
