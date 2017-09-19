
package com.bs.bsims.appzone;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.BaseActivity;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.UrlUtil;
import com.bs.bsims.view.ColumnHorizontalScrollView;
import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BeiShengALLZoneAcitvity extends BaseActivity {

    /** 自定义HorizontalScrollView */
    private ColumnHorizontalScrollView mColumnHorizontalScrollView;
    LinearLayout mRadioGroup_content;
    LinearLayout ll_more_columns;
    RelativeLayout rl_column;
    private ViewPager mViewPager;
    private ImageView button_more_columns;
    /** 新闻分类列表 */
    private ArrayList<ALLZoneClassify> newsClassify = new ArrayList<ALLZoneClassify>();
    /** 当前选中的栏目 */
    private int columnSelectIndex = 0;
    /** 左阴影部分 */
    public ImageView shade_left;
    /** 右阴影部分 */
    public ImageView shade_right;
    /** 屏幕宽度 */
    private int mScreenWidth = 0;
    /** Item宽度 */
    private int mItemWidth = 0;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

    private Context mContext;
    private ALLZoneFragment all;
    private ALLZoneModel allZoneModel;

    /**
     * 当栏目项发生变化时候调用
     */
    private void setChangelView() {
        initColumnData();
        initTabColumn();
        initFragment();
    }

    /** 获取Column栏目 数据 */
    private void initColumnData() {
        if (ALLZoneConstants.getData() != null) {
            newsClassify = ALLZoneConstants.getData();
        }

    }

    /**
     * 初始化Column栏目项
     */
    private void initTabColumn() {
        mRadioGroup_content.removeAllViews();
        int count = newsClassify.size();
        mColumnHorizontalScrollView.setParam(this, mScreenWidth, mRadioGroup_content, shade_left,
                shade_right, ll_more_columns, rl_column);
        for (int i = 0; i < count; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mItemWidth,
                    LayoutParams.WRAP_CONTENT);
            params.leftMargin = 25;
            params.rightMargin = 25;
            // TextView localTextView = (TextView) mInflater.inflate(R.layout.column_radio_item,
            // null);
            TextView localTextView = new TextView(this);
            localTextView.setTextSize(14);
            localTextView.setGravity(Gravity.CENTER);
            // localTextView.setBackground(getResources().getDrawable(R.drawable.top_category_scroll_text_view_bg));
            localTextView.setBackgroundResource(R.drawable.radio_buttong_bg);
            localTextView.setGravity(Gravity.CENTER);
            localTextView.setPadding(15, 5, 15, 5);
            localTextView.setId(i);
            localTextView.setText(newsClassify.get(i).getTitle());
            localTextView.setTextColor(getResources().getColorStateList(
                    R.drawable.top_category_scroll_text_color_day));
            if (columnSelectIndex == i) {
                localTextView.setSelected(true);
            }
            localTextView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
                        View localView = mRadioGroup_content.getChildAt(i);
                        if (localView != v)
                            localView.setSelected(false);
                        else {
                            localView.setSelected(true);
                            mViewPager.setCurrentItem(i);
                        }
                    }
                }
            });
            mRadioGroup_content.addView(localTextView, i, params);
        }
    }

    /**
     * 选择的Column里面的Tab
     */
    private void selectTab(int tab_postion) {
        columnSelectIndex = tab_postion;
        for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
            View checkView = mRadioGroup_content.getChildAt(tab_postion);
            int k = checkView.getMeasuredWidth();
            int l = checkView.getLeft();
            int i2 = l + k / 2 - mScreenWidth / 2;
            // rg_nav_content.getParent()).smoothScrollTo(i2, 0);
            mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
            // mColumnHorizontalScrollView.smoothScrollTo((position - 2) *
            // mItemWidth , 0);
        }
        // 判断是否选中
        for (int j = 0; j < mRadioGroup_content.getChildCount(); j++) {
            View checkView = mRadioGroup_content.getChildAt(j);
            boolean ischeck;
            if (j == tab_postion) {
                ischeck = true;
            } else {
                ischeck = false;
            }
            checkView.setSelected(ischeck);
        }
    }

    /**
     * 初始化Fragment
     */
    private void initFragment() {

        for (int i = 0; i < allZoneModel.getMtype().split(",").length; i++) {
            all = new ALLZoneFragment(i + "");
            fragments.add(all);
        }

        ALLZoneFragmentPagerAdapter mAdapetr = new ALLZoneFragmentPagerAdapter(
                getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mAdapetr);
        mViewPager.setOnPageChangeListener(pageListener);
    }

    /**
     * ViewPager切换监听方法
     */
    public OnPageChangeListener pageListener = new OnPageChangeListener() {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {
            // TODO Auto-generated method stub
            mViewPager.setCurrentItem(position);
            selectTab(position);
        }
    };

    @Override
    public void baseSetContentView() {
        // TODO Auto-generated method stub
        View layout = View.inflate(this, R.layout.beishengallzone_index, null);
        mContentLayout.addView(layout);
        mContext = this;
    }

    @Override
    public boolean getDataResult() {
        // TODO Auto-generated method stub
        return GetData();
    }

    @Override
    public void updateUi() {
        // TODO Auto-generated method stub
        ALLZoneConstants.SetData(allZoneModel);
        setChangelView();
        mViewPager.setOffscreenPageLimit(ALLZoneConstants.GetDataSize());
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        mTitleTv.setText("动向");
        mScreenWidth = CommonUtils.getScreenWidth(mContext);
        mItemWidth = mScreenWidth / 7;// 一个Item宽度为屏幕的1/7
        mColumnHorizontalScrollView = (ColumnHorizontalScrollView) findViewById(R.id.mColumnHorizontalScrollView);
        mRadioGroup_content = (LinearLayout) findViewById(R.id.mRadioGroup_content);
        ll_more_columns = (LinearLayout) findViewById(R.id.ll_more_columns);
        rl_column = (RelativeLayout) findViewById(R.id.rl_column);
        button_more_columns = (ImageView) findViewById(R.id.button_more_columns);
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        shade_left = (ImageView) findViewById(R.id.shade_left);
        shade_right = (ImageView) findViewById(R.id.shade_right);

        /*
         * 设置ViewPager的滑动速度
         */

        try {
            Field mScroller = null;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            BSViewPagerNoReFulshZone scroller = new BSViewPagerNoReFulshZone(
                    mViewPager.getContext());
            mScroller.set(mViewPager, scroller);
        } catch (NoSuchFieldException e) {

        } catch (IllegalArgumentException e) {

        } catch (IllegalAccessException e) {

        }
    }

    @Override
    public void bindViewsListener() {
        // TODO Auto-generated method stub

    }

    /*
     * 创建动态的headTitile
     */
    public boolean GetData() {
        Gson gson = new Gson();
        Map<String, String> map = new HashMap<String, String>();
        try {
            String urlStr;
            String jsonUrlStr;
            urlStr = UrlUtil.getUrlByMap1(
                    Constant.APP_ZONECONTENT, map);
            jsonUrlStr = HttpClientUtil.get(urlStr, Constant.ENCODING).trim();
            allZoneModel = gson.fromJson(jsonUrlStr, ALLZoneModel.class);
            if (allZoneModel.getCode().equals("200")) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
