
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
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

public class BossStatisticsAttendanceDistributedActivity extends BaseActivity implements OnClickListener {
    private String[] mColors = {
            "#FFD900", "#44C1F1", "#00A1E9", "#9DD7F7",
            "#00B1ED", "#6Ac9F3", "#00A1E9", "#44C1F1", "#00B1ED", "#9DD7F7"
    };
    private String mType = "1";
    private String mDate;
    private BossStatisticsAttendanceVO mAttendanceVO;
    private BSListView mListView;
    private BossStatisticsAttendanceAdapter mAdapter;
    private BSDialog mBSDialog;

    private LinearLayout mPiechartLayout;
    private BossStatisticsPieChart mPieChart;
    private TextView mSelectDepartTv, mSelectTotalNumTv, mSelectPersonNumTv;
    private TextView mAllNumTv, mAllPersonTv;
    private LinearLayout mTitleLayout;
    private TextView mListTitleTv;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.boss_statistics_attendance_distributed, mContentLayout);
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
        mTitleLayout.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.VISIBLE);
        mListTitleTv.setVisibility(View.VISIBLE);
        mListView.showHead(this, false);
        BossStatisticsAttendanceVO vo = mAttendanceVO.getInfo();
        List<BossStatisticsAttendanceVO> chartList = vo.getChart();
        mAllNumTv.setText(vo.getAllnum());
        mAllPersonTv.setText(vo.getAllperson());
        updatePieChartData(chartList);

        if (chartList != null) {
            mAdapter.updateData(chartList.get(0).getList(), "2");
            mSelectDepartTv.setText(chartList.get(0).getDname() + " " + chartList.get(0).getPercent());
            CommonUtils.setTextThree(this, mSelectTotalNumTv, "共 ", chartList.get(0).getNum(), " 次", R.color.C4, 1.5f);
            CommonUtils.setTextThree(this, mSelectPersonNumTv, "人均 ", chartList.get(0).getAverage(), " 次", R.color.C4, 1.5f);
        } else {
            mTitleLayout.setVisibility(View.GONE);
            mListView.setVisibility(View.GONE);
            mListTitleTv.setVisibility(View.GONE);
        }
    }

    @Override
    public void executeFailure() {
        super.executeFailure();
        // 列表展示的时候不能调用父类
        super.isRequestFinish();
        mListTitleTv.setVisibility(View.GONE);
        mAdapter.notifyDataSetChanged();
        mListView.showHead(this, false);

        // 不适合只隐藏列表，适合隐藏整个布局;
        if (mAttendanceVO == null) {
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

    public void updatePieChartData(final List<BossStatisticsAttendanceVO> chartList) {
        if (null == chartList) {// 饼子图没得数据的时候
            mPieChart.setAllTotal(-1);
            mPieChart.setItems(new float[] {
                    100
            });
            // 作用是在条件刷选下，避免上次饼状图值对下次无数据值情况下颜色块造成影响， 无数据时饼状图为灰色，但是有时第一次总为其他色（eg:绿色）
            mPieChart.setFirstItemSizes();
            mPieChart.setRadiocontentMultiLine("无数据", false);
            mPieChart.intitPieChart(null);

        } else {
            // mPieChart.setColors(mColors);
            mPieChart.setAllTotal(Float.parseFloat(mAttendanceVO.getInfo().getAllnum()));
            float iFloat[] = new float[chartList.size()];
            for (int i = 0; i < iFloat.length; i++) {
                iFloat[i] = Float.parseFloat(chartList.get(i).getPercentnum()) * 100;
            }
            mPieChart.setItems(iFloat);
            mPieChart.intitPieChart(new ItemOnClickCallback() {

                @Override
                public void itemClickCallback(int position) {
                    mPieChart.setRadiocontentMultiLine(chartList.get(position).getDname() + "," + chartList.get(position).getPercent(), true);
                    mSelectDepartTv.setText(chartList.get(position).getDname() + " " + chartList.get(position).getPercent());
                    CommonUtils.setTextThree(BossStatisticsAttendanceDistributedActivity.this, mSelectTotalNumTv, "共 ",
                            chartList.get(position).getNum(), " 次", R.color.C4, 1.5f);
                    CommonUtils.setTextThree(BossStatisticsAttendanceDistributedActivity.this, mSelectPersonNumTv, "人均 ",
                            chartList.get(position).getAverage(), " 次", R.color.C4, 1.5f);
                    mAdapter.updateData(chartList.get(position).getList(), "2");
                }
            });

        }
        // mPieChart.setPiechartBackgroundColor(Color.parseColor("#EEEEEE"));
    }

    @Override
    public void bindViewsListener() {
        mOkTv.setOnClickListener(this);
    }

    public boolean getData() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> mapList = new HashMap<String, String>();
            mapList.put("userid", BSApplication.getInstance().getUserId());
            mapList.put("type", mType);
            mapList.put("date", mDate);
            mapList.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.BOSS_STATISTICS_ATTENDANCE_DISTRIBUTED, mapList);
            mAttendanceVO = gson.fromJson(jsonStrList, BossStatisticsAttendanceVO.class);
            if (Constant.RESULT_CODE.equals(mAttendanceVO.getCode()))
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
            mListView.showHead(BossStatisticsAttendanceDistributedActivity.this, true);
            new ThreadUtil(BossStatisticsAttendanceDistributedActivity.this, BossStatisticsAttendanceDistributedActivity.this).start();
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
}
