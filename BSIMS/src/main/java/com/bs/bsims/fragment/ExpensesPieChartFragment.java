/**
 * 
 */

package com.bs.bsims.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.BossStatisticsExpensesAdapter;
import com.bs.bsims.model.BossStatisticsAttendanceVO;
import com.bs.bsims.model.BossStatisticsExpensesVo;
import com.bs.bsims.pc.chart.piechart.BossStatisticsPieChart;
import com.bs.bsims.pc.chart.piechart.BossStatisticsPieChart.ItemOnClickCallback;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomLog;
import com.bs.bsims.view.BSListView;

import java.util.ArrayList;
import java.util.List;

/**
 * BS北盛最帅程序员 Copyright (c) 2016 湖北北盛科技有限公司
 * 
 * @author 梁骚侠
 * @date 2016-2-15
 * @version 1.22 费用饼子图
 */
@SuppressLint("ValidFragment")
public class ExpensesPieChartFragment extends BaseFragment {

    private static final String TAG = "ExpensesPieChartFragmentTableOne";
    private BossStatisticsPieChart bossStatisticsPieChart;// 饼子图
    private List<BossStatisticsExpensesVo> bVoList;// 饼子图数据源
    private TextView text_item_info, mPiechart_Count, mPiechart_Day_Count;// 三角形的中的三个数据
    private TextView mViewHead;// 列表的headview
    private ImageView mText_item;
    private BossStatisticsExpensesAdapter mAttendanceAdapter;
    private BSListView mListView;
    private Activity activity;
    private BossStatisticsExpensesVo mStatisticsExpensesVo, mVo;
    private LinearLayout mSanCilreView;

    @Override
    public String getFragmentName() {
        // TODO Auto-generated method stub
        return TAG;
    }

    public ExpensesPieChartFragment(BossStatisticsExpensesVo mVo) {
        this.mVo = mVo;
    }

    public BossStatisticsExpensesVo getmVo() {
        return mVo;
    }

    public void setmVo(BossStatisticsExpensesVo mVo) {
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
        View view = inflater.inflate(R.layout.expenses_piechartview_table_one, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view) {
        // TODO Auto-generated method stub

        bossStatisticsPieChart = new BossStatisticsPieChart(activity);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.piechart);
        layout.addView(bossStatisticsPieChart);
        mListView = (BSListView) view.findViewById(R.id.bslistview);
        mAttendanceAdapter = new BossStatisticsExpensesAdapter(activity);
        mListView.setAdapter(mAttendanceAdapter);
        mText_item = (ImageView) view.findViewById(R.id.text_item);
        text_item_info = (TextView) view.findViewById(R.id.text_item_info);
        mPiechart_Count = (TextView) view.findViewById(R.id.piechart_count);
        mPiechart_Day_Count = (TextView) view.findViewById(R.id.piechart_day_count);
        mViewHead = (TextView) view.findViewById(R.id.piechart_donw_view);
        mSanCilreView = (LinearLayout) view.findViewById(R.id.piechart_text);
        updateUI();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // initData();

    }

    public void updateUI() {
        if (null == mVo.getChart()) {// 饼子图没得数据的时候
            bossStatisticsPieChart.setAllTotal(-1);
            bossStatisticsPieChart.setItems(new float[] {
                100
            });
            // 作用是在条件刷选下，避免上次饼状图值对下次无数据值情况下颜色块造成影响， 无数据时饼状图为灰色，但是有时第一次总为其他色（eg:绿色）
            bossStatisticsPieChart.setFirstItemSizes();
            bossStatisticsPieChart.intitPieChart(new ItemOnClickCallback() {

                @Override
                public void itemClickCallback(int position) {
                    // TODO Auto-generated method stub
                    bossStatisticsPieChart.setRadiocontent("无数据");
                    mAttendanceAdapter.mList.clear();
                    mAttendanceAdapter.notifyDataSetChanged();
                    mSanCilreView.setVisibility(View.GONE);
                    mViewHead.setVisibility(View.GONE);
                    if (mListView.getVisibility() == View.VISIBLE) {
                        mListView.setVisibility(View.GONE);
                        mListView.setVisibility(View.GONE);
                    }
                }
            });

        } else {
            bVoList = new ArrayList<BossStatisticsExpensesVo>();
            bVoList.clear();
            bVoList.addAll(mVo.getChart());
            CustomLog.e("eero", bVoList.size() + "size");
            // bossStatisticsPieChart.setColors(colors);
            bossStatisticsPieChart.setAllTotal(Float.parseFloat(20 + ""));
            float iFloat[] = new float[bVoList.size()];
            for (int i = 0; i < iFloat.length; i++) {
                iFloat[i] = Float.parseFloat(bVoList.get(i).getPercentnum()) * 100;
            }
            bossStatisticsPieChart.setItems(iFloat);
            bossStatisticsPieChart.intitPieChart(new ItemOnClickCallback() {

                /*
                 * (non-Javadoc)
                 * @see BossStatisticsPieChart
                 * .ItemOnClickCallback#itemClickCallback(int)
                 */
                @Override
                public void itemClickCallback(int position) {
                    // TODO Auto-generated method stub
                    mSanCilreView.setVisibility(View.VISIBLE);
                    try {
                        String sColor[] = bossStatisticsPieChart.getPieNomarlColors();
                        mText_item.setBackgroundDrawable(CommonUtils.setBackgroundShap(activity, 40, "#ffffff", sColor[position]));
                    } catch (Exception e) {
                        mText_item.setBackgroundDrawable(CommonUtils.setBackgroundShap(activity, 40, "#ffffff", "#00A8FF"));
                    }

                    bossStatisticsPieChart.setRadiocontent(bVoList.get(position).getName());
                    mViewHead.setVisibility(View.VISIBLE);
                    mViewHead.setText("部门" + bVoList.get(position).getName() + "费用排行");
                    text_item_info.setText(bVoList.get(position).getName() + " " + bVoList.get(position).getPercent());
                    mPiechart_Count.setText(CommonUtils.countNumber((bVoList.get(position).getNum())));
                    mAttendanceAdapter.mList.clear();
                    List<BossStatisticsAttendanceVO> list = new ArrayList<BossStatisticsAttendanceVO>();
                    for (int i = 0; i < bVoList.get(position).getList().size(); i++) {
                        BossStatisticsAttendanceVO bossStatisticsAttendanceVO = new BossStatisticsAttendanceVO();
                        bossStatisticsAttendanceVO.setDname(bVoList.get(position).getList().get(i).getDname());
                        bossStatisticsAttendanceVO.setNum(bVoList.get(position).getList().get(i).getNum());
                        bossStatisticsAttendanceVO.setContrast(bVoList.get(position).getList().get(i).getContrast());
                        list.add(bossStatisticsAttendanceVO);
                    }
                    mAttendanceAdapter.mList.addAll(list);
                    mAttendanceAdapter.notifyDataSetChanged();
                    if (mListView.getVisibility() == View.GONE) {
                        mListView.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.VISIBLE);
                    }

                }

            });

        }

    }

}
