
package com.bs.bsims.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.BossStatisticsAttendanceAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.BossStatisticsAttendanceVO;
import com.bs.bsims.pc.chart.piechart.BossStatisticsPieChart;
import com.bs.bsims.pc.chart.piechart.BossStatisticsPieChart.ItemOnClickCallback;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CommonUtils.ResultCallback;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSDialog;
import com.bs.bsims.view.BSListView;
import com.bs.bsims.view.BSTopIndicator;
import com.bs.bsims.view.BSTopIndicator.OnTopIndicatorListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BossStatisticsleaveDistributedActivity extends BaseActivity implements OnClickListener, OnTopIndicatorListener {
    private String[] mColors = {
            "#FFD900", "#44C1F1", "#00A1E9", "#9DD7F7",
            "#00B1ED", "#6Ac9F3", "#00A1E9", "#44C1F1", "#00B1ED", "#9DD7F7"
    };
    private int[] mDrawableIds = new int[] {
            R.drawable.boss_static_bg_number,
            R.drawable.boss_static_bg_time
    };
    private CharSequence[] mLabels = new CharSequence[] {
            "按次", "按时长"
    };
    private String mDate;
    private String mType = "1";//
    private String mMode = "1";// 查询方式1按人，2按次
    private BossStatisticsAttendanceVO mAttendanceVO;
    private BossStatisticsAttendanceVO mTimeAttendanceVO;
    private BossStatisticsAttendanceVO mAllAttendanceVO;// 最开始请求时不分种类；
    private BSListView mListView;
    private BossStatisticsAttendanceAdapter mAdapter;
    private BSDialog mBSDialog;

    private LinearLayout mPiechartLayout;
    private BossStatisticsPieChart mPieChart;
    private TextView mSelectDepartTv, mSelectTotalNumTv, mSelectPersonNumTv;
    private TextView mAllNumTv, mAllPersonTv, mAllTimeTv;
    private BSTopIndicator mTopIndicator;
    private List<String> mModeArray = new ArrayList<String>();
    private String mCurrentTime;
    private List<BossStatisticsAttendanceVO> mChartList;
    private LinearLayout mTitleLayout;
    private TextView mListTitleTv;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.boss_statistics_leave_distributed, mContentLayout);
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
        mListTitleTv.setVisibility(View.VISIBLE);

        mTitleLayout.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.VISIBLE);
        mListView.showHead(this, false);
        if ("1".equals(mAllAttendanceVO.getInfo().getMode())) {
            mAttendanceVO = mAllAttendanceVO;
            BossStatisticsAttendanceVO vo = mAttendanceVO.getInfo();
            updateData(vo);
        } else {
            mTimeAttendanceVO = mAllAttendanceVO;
            BossStatisticsAttendanceVO vo = mTimeAttendanceVO.getInfo();
            updateData(vo);
        }

        if (!mModeArray.contains(mAllAttendanceVO.getInfo().getMode())) {
            mModeArray.add(mAllAttendanceVO.getInfo().getMode());
        }
    }

    public void updateData(BossStatisticsAttendanceVO vo) {
        mAllNumTv.setText(vo.getAllnum());
        mAllPersonTv.setText(vo.getAllperson());
        mAllTimeTv.setText(vo.getAllnum());
        List<BossStatisticsAttendanceVO> chartList = vo.getChart();
        if (chartList != null) {
            updatePieChartData(vo);
            mAdapter.updateData(chartList.get(0).getList(), vo.getMode());
            mSelectDepartTv.setText(chartList.get(0).getDname() + " " + chartList.get(0).getPercent());
            if ("1".equals(vo.getMode())) {
                mAdapter.updateData(chartList.get(0).getList(), "2");
                CommonUtils.setTextThree(this, mSelectTotalNumTv, "共 ", chartList.get(0).getNum(), " 次", R.color.C4, 1.5f);
                CommonUtils.setTextThree(this, mSelectPersonNumTv, "人均 ", chartList.get(0).getAverage(), " 次", R.color.C4, 1.5f);
            } else {
                mAdapter.updateData(chartList.get(0).getList(), "3");
                CommonUtils.setTextThree(this, mSelectTotalNumTv, "共 ", chartList.get(0).getNum(), " 小时", R.color.C4, 1.5f);
                CommonUtils.setTextThree(this, mSelectPersonNumTv, "人均 ", chartList.get(0).getAverage(), " 小时", R.color.C4, 1.5f);
            }
            mSelectDepartTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.boss_static_icon_number, 0, 0, 0);
            // 0, 0, 0);
        } else {
            updatePieChartData(null);
            mTitleLayout.setVisibility(View.GONE);
            mListView.setVisibility(View.GONE);
            mListTitleTv.setVisibility(View.GONE);
        }
    }

    @Override
    public void executeFailure() {
        super.executeFailure();
        mListTitleTv.setVisibility(View.GONE);
        // 列表展示的时候不能调用父类
        super.isRequestFinish();
        mAdapter.notifyDataSetChanged();
        mListView.showHead(this, false);
        // 不适合只隐藏列表，适合隐藏整个布局;
        if (mAllAttendanceVO == null) {
            super.showNoNetView();
        } else {
            updatePieChartData(null);
            mAdapter.updateData(null, "3");
        }
    }

    @Override
    public void initView() {
        mOkTv.setTextSize(16);
        mOkTv.setText(DateUtils.getCurrentDate11113());
        mAllNumTv = (TextView) findViewById(R.id.all_num_tv);
        mAllPersonTv = (TextView) findViewById(R.id.all_person_tv);
        mAllTimeTv = (TextView) findViewById(R.id.all_time_tv);
        mTopIndicator = (BSTopIndicator) findViewById(R.id.top_indicator);
        mTopIndicator.setmLabels(mLabels);
        mTopIndicator.setmDrawableIds(mDrawableIds);
        mTopIndicator.updateUI(this);
        mSelectDepartTv = (TextView) findViewById(R.id.select_depart);
        mSelectPersonNumTv = (TextView) findViewById(R.id.select_person_num);
        mSelectTotalNumTv = (TextView) findViewById(R.id.select_total_num);
        mListView = (BSListView) findViewById(R.id.list_view);
        mListView.showHead(this, true);
        mAdapter = new BossStatisticsAttendanceAdapter(this);
        mListView.setAdapter(mAdapter);
        LinearLayout piechartLayout = (LinearLayout) findViewById(R.id.piechart_layout);
        mPieChart = new BossStatisticsPieChart(this);
        piechartLayout.addView(mPieChart);
        mModeArray.add("1");
        mListTitleTv = (TextView) findViewById(R.id.list_title_tv);
        mTitleLayout = (LinearLayout) findViewById(R.id.title_layout);
        initData();

    }

    public void initData() {
        Intent intent = this.getIntent();
        String titleName = intent.getStringExtra("title_name");
        mListTitleTv.setText("部门" + titleName + "排行");
        mTitleTv.setText(titleName);
        if (intent.getStringExtra("type") != null)
            mType = intent.getStringExtra("type");
    }

    public void updatePieChartData(final BossStatisticsAttendanceVO vo) {
        mChartList = new ArrayList<BossStatisticsAttendanceVO>();
        if (null == vo) {// 饼子图没得数据的时候
            mPieChart.setAllTotal(-1);
            mPieChart.setItems(new float[] {
                    100
            });
            // 作用是在条件刷选下，避免上次饼状图值对下次无数据值情况下颜色块造成影响， 无数据时饼状图为灰色，但是有时第一次总为其他色（eg:绿色）
            mPieChart.setFirstItemSizes();
            mPieChart.setRadiocontentMultiLine("无数据", false);
            mPieChart.intitPieChart(null);
        } else {
            List<BossStatisticsAttendanceVO> chartList = vo.getChart();
            mChartList.addAll(chartList);
            if ("1".equals(vo.getMode())) {
                mPieChart.setAllTotal(Float.parseFloat(mAttendanceVO.getInfo().getAllnum()));
            } else {
                mPieChart.setAllTotal(Float.parseFloat(mTimeAttendanceVO.getInfo().getAllhours()));
            }
            float iFloat[] = new float[mChartList.size()];
            for (int i = 0; i < iFloat.length; i++) {
                iFloat[i] = Float.parseFloat(mChartList.get(i).getPercentnum()) * 100;
            }
            mPieChart.setItems(iFloat);
            mPieChart.intitPieChart(new ItemOnClickCallback() {
                @Override
                public void itemClickCallback(int position) {
                    mPieChart.setRadiocontentMultiLine(mChartList.get(position).getDname() + "," + mChartList.get(position).getPercent(), true);
                    mSelectDepartTv.setText(mChartList.get(position).getDname() + " " + mChartList.get(position).getPercent());
                    if ("1".equals(vo.getMode())) {
                        mAdapter.updateData(mChartList.get(position).getList(), "3");
                        CommonUtils.setTextThree(BossStatisticsleaveDistributedActivity.this, mSelectTotalNumTv, "共 ", mChartList.get(position).getNum(), " 次", R.color.C4, 1.5f);
                        CommonUtils.setTextThree(BossStatisticsleaveDistributedActivity.this, mSelectPersonNumTv, "人均 ", mChartList.get(position).getAverage(), " 次", R.color.C4, 1.5f);
                    } else {
                        mAdapter.updateData(mChartList.get(position).getList(), "3");
                        CommonUtils.setTextThree(BossStatisticsleaveDistributedActivity.this, mSelectTotalNumTv, "共 ", mChartList.get(position).getNum(), " 小时", R.color.C4, 1.5f);
                        CommonUtils.setTextThree(BossStatisticsleaveDistributedActivity.this, mSelectPersonNumTv, "人均 ", mChartList
                                .get(position).getAverage(), " 小时", R.color.C4, 1.5f);
                    }
                }
            });
        }
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
                url = Constant.BOSS_STATISTICS_LEAVE_DISTRIBUTED;
            } else {
                // 9为加班特殊处理
                url = Constant.BOSS_STATISTICS_OVERTIME_DISTRIBUTED;
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

    @Override
    public void onIndicatorSelected(int index) {
        mTopIndicator.setTabsDisplay(this, index);
        mMode = (index + 1) + "";
        // 安人，按次，是否刷新数据
        if (!mModeArray.contains(mMode)) {
            match(3, (index + 1) + "");
        } else {
            if (index == 0)
                updateData(mAttendanceVO.getInfo());
            else
                updateData(mTimeAttendanceVO.getInfo());
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
        mListView.showHead(BossStatisticsleaveDistributedActivity.this, true);
        new ThreadUtil(this, this).start();
    }

}
