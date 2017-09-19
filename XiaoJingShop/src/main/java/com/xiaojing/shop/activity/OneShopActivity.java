package com.xiaojing.shop.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cpoopc.scrollablelayoutlib.ScrollableLayout;
import com.daidingkang.SnapUpCountDownTimerView;
import com.loopj.android.http.RequestParams;
import com.rey.material.widget.TextView;
import com.squareup.picasso.Picasso;
import com.vondear.rxtools.view.RxTextviewVertical;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.wuzhanglong.library.utils.WidthHigthUtil;
import com.xiaojing.shop.R;
import com.xiaojing.shop.application.AppApplication;
import com.xiaojing.shop.constant.Constant;
import com.xiaojing.shop.fragment.OneShopFragment;
import com.xiaojing.shop.mode.OneShopVO;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

public class OneShopActivity extends BaseActivity implements View.OnClickListener {
    //    private String[] mTitleDataList = {"人气", "进度", "最新揭晓"};
    private ViewPager mViewPager;
    private ArrayList<OneShopFragment> mFragmentList;
    private RxTextviewVertical mRxText;
    //    private HeaderViewPager scrollableLayout;
    private ScrollableLayout scrollableLayout;
    private LinearLayout mHeadLayout, mTextLayout, mImgLayout;
    private TextView mLotteryingTv, mLotteryedTv, mShowOrderTv;
    private OneShopVO mOneShopVO;
    private ImageView mImg01, mImg02, mImg03;
    //    private android.widget.TextView mText01, mText02, mText03;
    private LinearLayout mImgLayout01, mImgLayout02, mImgLayout03;
    private boolean mFlag = true;
    private SnapUpCountDownTimerView mCountDownTimerOne, mCountDownTimerTwo, mCountDownTimerThree;

    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.one_shop_activity);
    }

    @Override
    public void initView() {
        mBaseTitleTv.setText("鲸喜购");
        mLotteryingTv = getViewById(R.id.lotterying_tv);
        mLotteryedTv = getViewById(R.id.lotteryed_tv);
        mShowOrderTv = getViewById(R.id.show_order_tv);
//        scrollableLayout = (HeaderViewPager) findViewById(R.id.scrollableLayout);
        scrollableLayout = (ScrollableLayout) findViewById(R.id.scrollableLayout);
        mHeadLayout = getViewById(R.id.head_layout);
        mTextLayout = getViewById(R.id.text_layout);
        mImgLayout = getViewById(R.id.img_layout);
        mViewPager = getViewById(R.id.view_pager);
        mRxText = (RxTextviewVertical) findViewById(R.id.rx_text);
        mImg01 = getViewById(R.id.img_01);
        mImg02 = getViewById(R.id.img_02);
        mImg03 = getViewById(R.id.img_03);
//        mText01 = getViewById(R.id.text_01);
//        mText02 = getViewById(R.id.text_02);
//        mText03 = getViewById(R.id.text_03);
        mCountDownTimerOne = getViewById(R.id.timer_01);
        mCountDownTimerTwo = getViewById(R.id.timer_02);
        mCountDownTimerThree = getViewById(R.id.timer_03);

        mImgLayout01 = getViewById(R.id.img_layout_01);
        mImgLayout02 = getViewById(R.id.img_layout_02);
        mImgLayout03 = getViewById(R.id.img_layout_03);
    }

    private void initMagicIndicator() {
        MagicIndicator magicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator);
        magicIndicator.setBackgroundColor(Color.WHITE);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(false);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mOneShopVO.getType_list() == null ? 0 : mOneShopVO.getType_list().size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setText(mOneShopVO.getType_list().get(index).getType_name());
                simplePagerTitleView.setWidth(WidthHigthUtil.getScreenWidth(OneShopActivity.this) / 4);
                simplePagerTitleView.setTextSize(14);
                simplePagerTitleView.setNormalColor(Color.parseColor("#616161"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#f57c00"));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(1.6f));
//                indicator.setYOffset(UIUtil.dip2px(context, 39));
                indicator.setLineHeight(UIUtil.dip2px(context, 2));
                indicator.setColors(Color.parseColor("#f57c00"));
//                LinePagerIndicator indicator = new LinePagerIndicator(context);
//                indicator.setColors(Color.parseColor("#40c4ff"));
                return indicator;
            }

            @Override
            public float getTitleWeight(Context context, int index) {
                if (index == 0) {
                    return 1.0f;
                } else if (index == 1) {
                    return 1.0f;
                } else {
                    return 1.0f;
                }
            }
        });

        initViewPagerData();
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);

    }

    public void initViewPagerData() {
        mFragmentList = new ArrayList<>();
        for (int i = 0; i < mOneShopVO.getType_list().size(); i++) {
            OneShopFragment fragment = OneShopFragment.newInstance();
            fragment.setType(mOneShopVO.getType_list().get(i).getType_id());
            mFragmentList.add(fragment);
        }
        mViewPager.setOffscreenPageLimit(mOneShopVO.getType_list().size());
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            //            private String[] titles = {"人气", "进度", "最新揭晓"};
            private List<OneShopVO> titles = mOneShopVO.getType_list();

            @Override
            public CharSequence getPageTitle(int position) {
                return titles.get(position).getType_name();
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        });

    }

    @Override
    public void bindViewsListener() {
        mLotteryingTv.setOnClickListener(this);
        mLotteryedTv.setOnClickListener(this);
        mShowOrderTv.setOnClickListener(this);
        mImgLayout01.setOnClickListener(this);
        mImgLayout02.setOnClickListener(this);
        mImgLayout03.setOnClickListener(this);
    }

    @Override
    public void getData() {
        RequestParams paramsMap = new RequestParams();
        String url = Constant.ONE_SHOP_URL;
        if (AppApplication.getInstance().getUserInfoVO() != null)
            paramsMap.put("key", AppApplication.getInstance().getUserInfoVO().getKey());
        paramsMap.put("type_id", "1");
        paramsMap.put("curpage", "1");
        HttpClientUtil.get(mActivity, mThreadUtil, url, paramsMap, OneShopVO.class);
    }

    @Override
    public void hasData(BaseVO vo) {
        mOneShopVO = (OneShopVO) vo;
        initMagicIndicator();
//        scrollableLayout.setCurrentScrollableContainer(mFragmentList.get(0));
        scrollableLayout.getHelper().setCurrentScrollableContainer(mFragmentList.get(0));
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                scrollableLayout.getHelper().setCurrentScrollableContainer(mFragmentList.get(position));
            }
        });
        scrollableLayout.setOnScrollListener(new ScrollableLayout.OnScrollListener() {
            @Override
            public void onScroll(int currentY, int maxY) {
                mHeadLayout.setTranslationY(currentY / 2);

            }
        });

        if (mOneShopVO.getHeadline_list().size() == 0) {
            mTextLayout.setVisibility(View.GONE);
        } else {
            mTextLayout.setVisibility(View.VISIBLE);
            ArrayList<String> list = new ArrayList<>();
            for (int i = 0; i < mOneShopVO.getHeadline_list().size(); i++) {
                list.add(mOneShopVO.getHeadline_list().get(i).getText());
            }
            if (mFlag) {
                mRxText.setTextList(list);
                mRxText.setText(14, 5, 0xff766156);//设置属性
                mRxText.setTextStillTime(3000);//设置停留时长间隔
                mRxText.setAnimTime(300);//设置进入和退出的时间间隔
                mRxText.setOnItemClickListener(new RxTextviewVertical.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Bundle bundle = new Bundle();
                        bundle.putString("od_id", mOneShopVO.getHeadline_list().get(position).getOd_id());
                        mActivity.open(OneShopDetailActivity.class, bundle, 0);
                    }
                });
                mRxText.startAutoScroll();
                mFlag = false;
            }
        }

        if (mOneShopVO.getPre_list().size() == 0) {
            mImgLayout.setVisibility(View.GONE);
        } else {
            mImgLayout.setVisibility(View.VISIBLE);
            for (int i = 0; i < mOneShopVO.getPre_list().size(); i++) {
                switch (i) {
                    case 0:
                        mImgLayout01.setVisibility(View.VISIBLE);
                        Picasso.with(this).load(mOneShopVO.getPre_list().get(i).getGoods_image()).placeholder(R.drawable.img_default).into(mImg01);


                        int hour = Math.abs(BaseCommonUtils.parseInt(mOneShopVO.getPre_list().get(i).getCountdown())) / 3600;
                        int min = Math.abs(BaseCommonUtils.parseInt(mOneShopVO.getPre_list().get(i).getCountdown())) / 60 - hour * 60;
                        int sec = Math.abs(BaseCommonUtils.parseInt(mOneShopVO.getPre_list().get(i).getCountdown())) - min * 60 - hour * 60 * 60;
                        mCountDownTimerOne.setTime(hour, min, sec);
                        mCountDownTimerOne.start();

//                        new CountDownTimer(BaseCommonUtils.parseInt(mOneShopVO.getPre_list().get(i).getCountdown()), 1) {
//                            @Override
//                            public void onTick(long millisUntilFinished) {
//                                BaseCommonUtils.setTextTwoLast(OneShopActivity.this, mText01, "倒计时: ", millisUntilFinished + "", R.color.XJColor2, 1.2f);
//                            }
//
//                            @Override
//                            public void onFinish() {
//                                if(!OneShopActivity.this.isFinishing()){
//                                    BaseCommonUtils.setTextTwoLast(OneShopActivity.this, mText01, "倒计时： ", 0 + "", R.color.XJColor2, 1.2f);
//                                }
//                            }
//                        }.start();

                        break;
                    case 1:
                        mImgLayout02.setVisibility(View.VISIBLE);
                        Picasso.with(this).load(mOneShopVO.getPre_list().get(i).getGoods_image()).placeholder(R.drawable.img_default).into(mImg02);

                        int hourTwo = Math.abs(BaseCommonUtils.parseInt(mOneShopVO.getPre_list().get(i).getCountdown())) / 3600;
                        int minTwo = Math.abs(BaseCommonUtils.parseInt(mOneShopVO.getPre_list().get(i).getCountdown())) / 60 - hourTwo * 60;
                        int secTwo = Math.abs(BaseCommonUtils.parseInt(mOneShopVO.getPre_list().get(i).getCountdown())) - minTwo * 60 - hourTwo * 60 * 60;
                        mCountDownTimerTwo.setTime(hourTwo, minTwo, secTwo);
                        mCountDownTimerTwo.start();
                        break;
                    case 2:
                        mImgLayout03.setVisibility(View.VISIBLE);
                        Picasso.with(this).load(mOneShopVO.getPre_list().get(i).getGoods_image()).placeholder(R.drawable.img_default).into(mImg03);
                        int hourThree = Math.abs(BaseCommonUtils.parseInt(mOneShopVO.getPre_list().get(i).getCountdown())) / 3600;
                        int minThree = Math.abs(BaseCommonUtils.parseInt(mOneShopVO.getPre_list().get(i).getCountdown())) / 60 - hourThree * 60;
                        int secThree = Math.abs(BaseCommonUtils.parseInt(mOneShopVO.getPre_list().get(i).getCountdown())) - minThree * 60 - hourThree * 60 * 60;
                        mCountDownTimerThree.setTime(hourThree, minThree, secThree);
                        mCountDownTimerThree.start();

                        break;
                    default:
                        break;
                }
            }
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCountDownTimerOne.stop();
        mCountDownTimerTwo.stop();
        mCountDownTimerThree.stop();
    }

    @Override
    public void noData(BaseVO vo) {

    }

    @Override
    public void noNet() {

    }


    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.lotterying_tv:
                openActivity(OneShopLotteryingActivity.class);
                break;
            case R.id.lotteryed_tv:
                openActivity(OneShopLotteryedActivity.class);
                break;
            case R.id.show_order_tv:
                openActivity(OneShopShowListActivity.class);
                break;
            case R.id.img_layout_01:
                bundle.putString("od_id", mOneShopVO.getPre_list().get(0).getOd_id());
                mActivity.open(OneShopDetailActivity.class, bundle, 0);
                break;
            case R.id.img_layout_02:
                bundle.putString("od_id", mOneShopVO.getPre_list().get(1).getOd_id());
                mActivity.open(OneShopDetailActivity.class, bundle, 0);
                break;
            case R.id.img_layout_03:
                bundle.putString("od_id", mOneShopVO.getPre_list().get(2).getOd_id());
                mActivity.open(OneShopDetailActivity.class, bundle, 0);
                break;
            default:
                break;
        }
    }

}
