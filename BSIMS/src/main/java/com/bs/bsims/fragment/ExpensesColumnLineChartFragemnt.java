/**
 * 
 */

package com.bs.bsims.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.BossStatisticsAttendanceAdapter;
import com.bs.bsims.model.BossStatisticsAttendanceVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.view.BSListView;
import com.bs.bsims.view.chart.Axis;
import com.bs.bsims.view.chart.AxisValue;
import com.bs.bsims.view.chart.Column;
import com.bs.bsims.view.chart.ColumnChartData;
import com.bs.bsims.view.chart.ColumnChartView;
import com.bs.bsims.view.chart.SubcolumnValue;
import com.bs.bsims.view.chart.Viewport;

import java.util.ArrayList;
import java.util.List;

/**
 * BS北盛最帅程序员 Copyright (c) 2016 湖北北盛科技有限公司
 * 
 * @author 梁骚侠
 * @date 2016-2-15
 * @version 1.22 费用条形图
 */
@SuppressLint("ValidFragment")
public class ExpensesColumnLineChartFragemnt extends BaseFragment {

    private Activity activity;

    private ColumnChartView mColumnChartView;
    private ColumnChartData mColumnChartData;;
    private boolean hasLabels = true;// 控制是否在条条上显示对应的值
    private boolean hasLabelForSelected = false;

    private String mType = "1";
    private String mDate;
    private BSListView mListView;
    private BossStatisticsAttendanceAdapter mAdapter;
    private PopupWindow mTimePop;

    private LinearLayout headView;
    private LayoutInflater inflater;
    private RelativeLayout head_contentLayout;
    private BossStatisticsAttendanceVO mVo;

    @Override
    public String getFragmentName() {
        // TODO Auto-generated method stub
        return null;
    }

    public ExpensesColumnLineChartFragemnt(BossStatisticsAttendanceVO mVo) {
        this.mVo = mVo;
    }

    public BossStatisticsAttendanceVO getmVo() {
        return mVo;
    }

    public void setmVo(BossStatisticsAttendanceVO mVo) {
        this.mVo = mVo;
        updateUI();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.boss_statistics_attendance_rank, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view) {
        // TODO Auto-generated method stub
        mListView = (BSListView) view.findViewById(R.id.list_view);
        mListView.showHead(activity, true);
        mAdapter = new BossStatisticsAttendanceAdapter(activity);
        mListView.setAdapter(mAdapter);
        mColumnChartView = (ColumnChartView) view.findViewById(R.id.chart);
        LayoutParams layoutParams = mColumnChartView.getLayoutParams();
        layoutParams.height = CommonUtils.getScreenHigh(activity) / 3;
        layoutParams.width = layoutParams.FILL_PARENT;
        mColumnChartView.setLayoutParams(layoutParams);
        initColumnChartView();
        updateUI();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // initData();

    }

    public void updateUI() {
        if (mVo.getChart() == null || mVo.getShow() == null) {
            mListView.setVisibility(View.GONE);
            // mColumnChartView.setVisibility(View.GONE);
            initNoColumnData();
            CustomToast.showLongToast(activity, "没有数据");
            return;
        }
        mListView.setVisibility(View.VISIBLE);
        mColumnChartView.setVisibility(View.VISIBLE);
        List<BossStatisticsAttendanceVO> list = mVo.getShow();
        List<BossStatisticsAttendanceVO> chartList = mVo.getChart();

        if (chartList != null) {
            intColumnData(chartList);
        } else {
            initNoColumnData();
        }
        mListView.showHead(activity, false);
        mAdapter.updateData(list, "4");
        mListView.setAdapter(mAdapter);

    }

    public void showHead() {
        inflater = LayoutInflater.from(activity);
        headView = (LinearLayout) inflater.inflate(R.layout.item_list_down_refresh, null);
        head_contentLayout = (RelativeLayout) headView.findViewById(R.id.head_contentLayout);

        ImageView arrowImageView = (ImageView) headView.findViewById(R.id.head_arrowImageView);
        arrowImageView.setMinimumWidth(70);
        arrowImageView.setMinimumHeight(50);
        arrowImageView.setVisibility(View.GONE);
        ProgressBar progressBar = (ProgressBar) headView.findViewById(R.id.head_progressBar);
        progressBar.setVisibility(View.VISIBLE);
        TextView tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);
        tipsTextview.setVisibility(View.GONE);
        TextView lastUpdatedTextView = (TextView) headView.findViewById(R.id.head_lastUpdatedTextView);
        lastUpdatedTextView.setText("正在刷新，请稍后…");
        lastUpdatedTextView.setVisibility(View.VISIBLE);
        mListView.addHeaderView(headView);
        headView.setVisibility(View.GONE);
        head_contentLayout.setVisibility(View.GONE);
    }

    private void initColumnChartView() {
        mColumnChartView.setZoomEnabled(false);
        mColumnChartView.setClickable(false);
        mColumnChartView.setScrollEnabled(false);
    }

    private void intColumnData(List<BossStatisticsAttendanceVO> chartList) {
        // int mColor = this.getResources().getColor(R.color.C9);// 条形图颜色

        int color = Color.rgb(0, 168, 254);// 条形图颜色
        int numColumns = chartList.size();// 条形图中条条个数
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        List<Axis> nameValue;
        List<AxisValue> axisXValues = new ArrayList<AxisValue>();// 保存横坐标名称
        for (int i = 0; i < numColumns; i++) {
            values = new ArrayList<SubcolumnValue>();
            nameValue = new ArrayList<Axis>();
            // values.add(new SubcolumnValue((float) Math.random() * 50f + 5,
            // ChartUtils.pickColor()));
            // 纵坐标设值
            values.add(new SubcolumnValue(Float.valueOf(chartList.get(i).getNum()), color));
            // 横坐标设值
            axisXValues.add(new AxisValue(i).setLabel(chartList.get(i).getDname()));
            Column column = new Column(values);
            column.setHasLabels(hasLabels);
            column.setHasLabelsOnlyForSelected(hasLabelForSelected);
            columns.add(column);
        }
        mColumnChartData = new ColumnChartData(columns);

        // 隐藏不显示坐标名称
        Axis axisX = new Axis();
        axisX.setHasTiltedLabels(true);// 设置文字是否倾斜
        axisX.setValues(axisXValues);// 显示横坐标各个值
        axisX.setMaxLabelChars(numColumns);
        Axis axisY = new Axis().setHasLines(true);
        mColumnChartData.setAxisXBottom(axisX);
        mColumnChartData.setAxisYLeft(axisY);
        mColumnChartView.setColumnChartData(mColumnChartData);
        mColumnChartView.setViewportCalculationEnabled(false);
        resetViewport(numColumns, chartList.get(0).getNum());

    }

    /**
	 * 
	 */
    private void initNoColumnData() {
        // int mColor = this.getResources().getColor(R.color.C9);// 条形图颜色

        int numColumns = 10;// 条形图中条条个数
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        List<Axis> nameValue;
        List<AxisValue> axisXValues = new ArrayList<AxisValue>();// 保存横坐标名称
        for (int i = 0; i < numColumns; i++) {
            values = new ArrayList<SubcolumnValue>();
            nameValue = new ArrayList<Axis>();
            // 纵坐标设值
            values.add(new SubcolumnValue(new SubcolumnValue((float) Math.random() * 100, this.getResources().getColor(R.color.C3))));
            // 横坐标设值
            axisXValues.add(new AxisValue(i).setLabel(""));
            Column column = new Column(values);
            column.setHasLabels(false);
            column.setHasLabelsOnlyForSelected(hasLabelForSelected);
            columns.add(column);
        }
        mColumnChartData = new ColumnChartData(columns);

        // 隐藏不显示坐标名称
        Axis axisX = new Axis();
        axisX.setHasTiltedLabels(true);// 设置文字是否倾斜
        axisX.setValues(axisXValues);// 显示横坐标各个值
        // axisX.setMaxLabelChars(4);
        Axis axisY = new Axis().setHasLines(true);
        mColumnChartData.setAxisXBottom(axisX);
        mColumnChartData.setAxisYLeft(axisY);
        mColumnChartView.setColumnChartData(mColumnChartData);
        mColumnChartView.setZoomEnabled(false);
        mColumnChartView.setClickable(false);
        mColumnChartView.setScrollEnabled(false);
        mColumnChartView.setViewportCalculationEnabled(false);
        mColumnChartView.getCurrentViewport().top = 100;
    }

    private void resetViewport(int num, String str) {
        final Viewport v = new Viewport(mColumnChartView.getMaximumViewport());
        v.right = num;
        v.top = Float.parseFloat(str) + (int)(Float.parseFloat(str) / 4);
        v.bottom = 0;
        mColumnChartView.setMaximumViewport(v);
        mColumnChartView.setCurrentViewport(v);
    }

}
