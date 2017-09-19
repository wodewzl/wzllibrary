
package com.bs.bsims.pc.chart.piechart;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bs.bsims.R;

public class BossStatisticsPieChart extends ViewGroup {

    private Context context;

    /** 子View */
    private View view;
    private LinearLayout layout;
    private RelativeLayout layout_piechart_background;

    private PieChartView pieChart;
    // private String[] colors = { "#008cd6", "#0092d9", "#009add", "#00a3e1",
    // "#18ade5", "#4fb8e9", "#6dc2ee", "#87cef2", "#9ed8f6", "#ffc00" };

    private String[] colors;

    public String[] getColors() {
        return colors;
    }

    public String[] getPieNomarlColors() {// 获取自定义饼子图的颜色（如果不填充颜色 就获取的系统自己给添加颜色）
        return pieChart.getItemsColors();
    }

    public void setColors(String[] colors) {
        this.colors = colors;
    }

    private String[] noDatacolors = {
            "#BAC5D1"
    };

    private float animSpeed = 7f;
    private int total = 100;

    // private Map<String, String> mFixedMap;
    // private String[] mPopupWindowListArr;
    // private String[] mPopupWindowKeyListArr;

    // 每块扇形代表的类型
    private String[] type;
    private String[] content;

    private float allTotal;

    private String strCenterTxt = "1111";

    private String radiocontent = "";// 园内的数据

    // 简单弹出框点击后回调接口
    public interface ItemOnClickCallback {
        public void itemClickCallback(int position);
    }

    public ItemOnClickCallback mItemCallback;

    private float[] items;// 存放百分比

    public float getAllTotal() {
        return allTotal;
    }

    public void setAllTotal(float allTotal) {
        this.allTotal = allTotal;
    }

    public String[] getContent() {
        return content;
    }

    public void setContent(String[] content) {
        this.content = content;
    }

    public BossStatisticsPieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BossStatisticsPieChart(Context context) {
        super(context);
        this.context = context;
        initView();
        // setFixedMap();
    }

    private void initView() {
        view = LayoutInflater.from(context).inflate(R.layout.statistics_layout, null);
        // intitPieChart();
        layout_piechart_background = (RelativeLayout) view.findViewById(R.id.layout_piechart_background);
        pieChart = (PieChartView) view.findViewById(R.id.parbar_view);
        pieChart.setOnItemSelectedListener(new OnPieChartItemSelectedLinstener() {
            public void onPieChartItemSelected(PieChartView view, int position, String colorRgb, float size, float rate, boolean isFreePart, float rotateTime) {
                if (null != mItemCallback) {
                    mItemCallback.itemClickCallback(position);
                }
            }

            public void onTriggerClicked() {
            }

        });
        this.addView(view);
    }

    /**
     * 作用是在条件刷选下，避免上次饼状图值对下次无数据值情况下颜色块造成影响， 无数据时饼状图为灰色，但是有时第一次为其他色（eg:绿色）
     */
    public void setFirstItemSizes() {
        pieChart.setItemsSizes(items);// 设置各个块的值
        // 饼状图所表示的整体大小；如果用的百分比的，则为100
        pieChart.setTotal(100);
    }

    public void intitPieChart(final ItemOnClickCallback callback) {
        mItemCallback = callback;
        pieChart.setAnimEnabled(true);// 是否开启动画
        pieChart.setItemsSizes(items);// 设置各个块的值
        if (this.getAllTotal() <= 0) {
            pieChart.setItemsColors(noDatacolors);// 设置各个块的颜色
        } else {
            pieChart.setItemsColors(colors);// 设置各个块的颜色
        }
        pieChart.setRotateSpeed(animSpeed);// 设置旋转速度
        pieChart.setTotal(100);
        pieChart.setActualTotal(total);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        pieChart.setRaduis((int) (dm.widthPixels / 3));// 设置饼状图半径

        pieChart.setShowInfo(radiocontent);
        // pieChart.setShowItem(0, true, true);// 设置显示的块
        freshView();
    }

    public void setPiechartBackgroundColor(int color) {
        layout_piechart_background.setBackgroundColor(color);
    }

    public String getRadiocontent() {
        return radiocontent;
    }

    public void setRadiocontent(String radiocontent) {
        this.radiocontent = radiocontent;
        if (null != pieChart) {
            pieChart.setShowInfo(radiocontent);
        }

    }

    public void setRadiocontentMultiLine(String radiocontent, Boolean bool) {
        this.radiocontent = radiocontent;
        if (null != pieChart) {
            pieChart.setShowInfo(radiocontent, bool);
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View child = getChildAt(0);
        child.layout(l, t, r, b);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeigth = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(measureWidth, measureHeigth);
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            int widthSpec = 0;
            int heightSpec = 0;
            LayoutParams params = v.getLayoutParams();
            if (params.width > 0) {
                widthSpec = MeasureSpec.makeMeasureSpec(params.width, MeasureSpec.EXACTLY);
            } else if (params.width == -1) {
                widthSpec = MeasureSpec.makeMeasureSpec(measureWidth, MeasureSpec.EXACTLY);
            } else if (params.width == -2) {
                widthSpec = MeasureSpec.makeMeasureSpec(measureWidth, MeasureSpec.AT_MOST);
            }

            if (params.height > 0) {
                heightSpec = MeasureSpec.makeMeasureSpec(params.height, MeasureSpec.EXACTLY);
            } else if (params.height == -1) {
                heightSpec = MeasureSpec.makeMeasureSpec(measureHeigth, MeasureSpec.EXACTLY);
            } else if (params.height == -2) {
                heightSpec = MeasureSpec.makeMeasureSpec(measureHeigth, MeasureSpec.AT_MOST);
            }
            v.measure(widthSpec, heightSpec);
        }
    }

    public float[] getItems() {
        return items;
    }

    public void setItems(float[] items) {
        this.items = items;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
        if (total <= 0) {
            pieChart.setVisibility(View.GONE);
            pieChart.setTotal(0);
        } else {
            pieChart.setVisibility(View.VISIBLE);
            pieChart.setTotal(100);
            pieChart.setActualTotal(total);
        }
    }

    public void freshView() {
        pieChart.setShowItem(0, true, true);// 设置显示的块
        pieChart.invalidate();
        this.invalidate();
    }

    public void relaseTotal() {
        pieChart.relaseTotal(0);
    }

    public String[] getType() {
        return type;
    }

    public void setType(String[] type) {
        this.type = type;
    }

    public void setStrCenterTxt(String strCenterTxt) {
        this.strCenterTxt = strCenterTxt;
    }

}
