package com.xiaojing.shop.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daidingkang.SnapUpCountDownTimerView;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.wuzhanglong.library.ItemDecoration.DividerDecoration;
import com.wuzhanglong.library.ItemDecoration.StickyHeaderDecoration;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.wuzhanglong.library.utils.DividerUtil;
import com.wuzhanglong.library.utils.ShareUtil;
import com.wuzhanglong.library.utils.ThreadUtil;
import com.xiaojing.shop.R;
import com.xiaojing.shop.adapter.OneShopDetailadapter;
import com.xiaojing.shop.application.AppApplication;
import com.xiaojing.shop.constant.Constant;
import com.xiaojing.shop.mode.OneShopVO;
import com.xiaojing.shop.view.NumberButton;

import java.util.List;

public class OneShopDetailActivity extends BaseActivity implements OnLoadMoreListener, View.OnClickListener, NumberButton.OnTextChangeListener {
    private static final int LOGIN_CODE = 1;
    //    private AutoSwipeRefreshLayout mAutoSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView;
    private OneShopDetailadapter mAdapter;

    private String mOdId;
    private OneShopVO mOneShopVO;
    private ImageView mDetailImg, mShareImg;
    private boolean mFlag = true;
    private int mCurrentPage = 1;
    private boolean isLoadMore = true;
    private TextView mBuyAllTv, mQuickTv;
    private NumberButton mNumberBt;
    private int mCurrentCount = 1;
    private String mAllRemain = "0";
    private LinearLayout mBottomLayout, mHeadLayout;
    private TextView mTitleTv, mHeadTv, mBackTv;
    private int mDistanceY;

    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.one_shop_detail_activity);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initView() {

        SetTranslanteBar();
        mHeadLayout = getViewById(R.id.head_layout);
        mHeadTv = getViewById(R.id.head_tv);
        mHeadTv.setText("鲸喜购详情");
        mBackTv = getViewById(R.id.back_tv);
        mShareImg = getViewById(R.id.share_img);
        mRecyclerView = getViewById(R.id.recycler_view);
        mBaseHeadLayout.setVisibility(View.GONE);
        DividerDecoration divider = DividerUtil.linnerDivider(this, R.dimen.dp_1, R.color.C3);
        mRecyclerView.addItemDecoration(divider);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new OneShopDetailadapter(this);
        mOdId = this.getIntent().getStringExtra("od_id");


        mBottomLayout = getViewById(R.id.bottom_layout);
        mBuyAllTv = getViewById(R.id.buy_all_tv);
        mBuyAllTv.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.XJColor2, R.color.XJColor2));
        mBuyAllTv.setTextColor(ContextCompat.getColor(this, R.color.C1));

        mQuickTv = getViewById(R.id.quick_tv);
        mQuickTv.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.XJColor2, R.color.XJColor2));
        mNumberBt = getViewById(R.id.number_bt);

        ;

    }


    @Override
    public void bindViewsListener() {
        mRecyclerView.setOnLoadMoreListener(this);
        mBuyAllTv.setOnClickListener(this);
        mQuickTv.setOnClickListener(this);
        mNumberBt.setmOnTextChangeListener(this);
        mBackTv.setOnClickListener(this);
        mShareImg.setOnClickListener(this);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy == 0)
                    return;
                //滑动的距离
                mDistanceY += dy;
                //toolbar的高度
//                int toolbarHeight = mTitleLayout.getBottom();
                int toolbarHeight = BaseCommonUtils.dip2px(mActivity, 256);
                //当滑动的距离 <= toolbar高度的时候，改变Toolbar背景色的透明度，达到渐变的效果
                if (mDistanceY <= toolbarHeight) {
                    float scale = (float) mDistanceY / toolbarHeight;
                    float alpha = scale * 255;
                    mHeadLayout.setBackgroundColor(Color.argb((int) alpha, 0, 153, 223));//0, 153, 223 为C7 RGB 值
                } else {
                    //将标题栏的颜色设置为完全不透明状态
                    mHeadLayout.setBackgroundResource(R.color.C7);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return true;
    }

    @Override
    public void getData() {
        RequestParams paramsMap = new RequestParams();
        String url = Constant.ONE_SHOP_DETAIL_URL;
        if (AppApplication.getInstance().getUserInfoVO() != null)
            paramsMap.put("key", AppApplication.getInstance().getUserInfoVO().getKey());
        paramsMap.put("od_id", mOdId);
        paramsMap.put("curpage", mCurrentPage);
        HttpClientUtil.get(mActivity, mThreadUtil, url, paramsMap, OneShopVO.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void hasData(BaseVO vo) {
        OneShopVO oneShopVO = (OneShopVO) vo;
        mOneShopVO = oneShopVO.getDatas();


        if ("0".equals(mOneShopVO.getOd_remain_count())) {
            mBottomLayout.setVisibility(View.GONE);
        } else {
            mNumberBt.setBuyMax(BaseCommonUtils.parseInt(mOneShopVO.getOd_remain_count())).setCurrentNumber(BaseCommonUtils.parseInt(mOneShopVO.getBuy_limit()));
            mNumberBt.setMultiple(BaseCommonUtils.parseInt(mOneShopVO.getBuy_limit()));
            mBottomLayout.setVisibility(View.VISIBLE);
        }


        if (mFlag) {
            mFlag = false;
            LuRecyclerViewAdapter adapter = new LuRecyclerViewAdapter(mAdapter);
            adapter.addHeaderView(initHeadView(mOneShopVO.getOd_state()));
            StickyHeaderDecoration decor = new StickyHeaderDecoration(mAdapter);
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.addItemDecoration(decor, 1);
            mRecyclerView.setLoadMoreEnabled(true);
            mAdapter.setTime(mOneShopVO.getOd_addtime());
//            mAdapter.updateData(mOneShopVO.getJoin_list());
            Picasso.with(this).load(mOneShopVO.getGoods_image()).into(mDetailImg);
            mTitleTv.setText(mOneShopVO.getGoods_name());
        }
        List<OneShopVO> list = mOneShopVO.getJoin_list();
        if (vo.getHasmore() != null && "1".equals(vo.getHasmore())) {
            mRecyclerView.setNoMore(false);
        } else {
            mRecyclerView.setNoMore(true);
            mRecyclerView.setLoadMoreEnabled(false);
        }
        if (isLoadMore) {
            mAdapter.updateDataLast(list);
            isLoadMore = false;
            mCurrentPage++;
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void noData(BaseVO vo) {

    }

    @Override
    public void noNet() {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public View initHeadView(String type) {
        com.rey.material.widget.TextView headerTv1, headerTv2, headerTv3;
        View header = null;
        if ("1".equals(type)) {
            header = LayoutInflater.from(this).inflate(R.layout.one_shop_detail_header1, (ViewGroup) findViewById(android.R.id.content), false);
            mDetailImg = (ImageView) header.findViewById(R.id.detail_img);
            mTitleTv = (TextView) header.findViewById(R.id.title_tv);

            ((TextView) header.findViewById(R.id.number_tv)).setText(mOneShopVO.getOd_no());
            ((TextView) header.findViewById(R.id.state_tv)).setBackgroundDrawable(BaseCommonUtils.setBackgroundShap(this, 5, R.color.XJColor2, R.color.XJColor2));
            BaseCommonUtils.setTextThree(this, ((TextView) header.findViewById(R.id.join_tv)), "已参与", mOneShopVO.getOd_buy_count(), "人次", R.color.C4, 1.3f);
            BaseCommonUtils.setTextThree(this, ((TextView) header.findViewById(R.id.total_tv)), "总计", mOneShopVO.getOd_count(), "人次", R.color.C4, 1.3f);
            BaseCommonUtils.setTextThree(this, ((TextView) header.findViewById(R.id.other_tv)), "剩余", mOneShopVO.getOd_remain_count(), "人次", R.color.XJColor2, 1.3f);
            ((ProgressBar) header.findViewById(R.id.progress_bar)).setProgress(BaseCommonUtils.parseInt(mOneShopVO.getOd_progress()));
        } else if ("2".equals(type)) {
            header = LayoutInflater.from(this).inflate(R.layout.one_shop_detail_header2, (ViewGroup) findViewById(android.R.id.content), false);
            mDetailImg = (ImageView) header.findViewById(R.id.detail_img);
            mTitleTv = (TextView) header.findViewById(R.id.title_tv);


            ((TextView) header.findViewById(R.id.number_tv)).setText(mOneShopVO.getOd_no());
            SnapUpCountDownTimerView downTimerView = (SnapUpCountDownTimerView) header.findViewById(R.id.timer_view);
            int hour = BaseCommonUtils.parseInt(mOneShopVO.getCountdown()) / 3600;
            int min = BaseCommonUtils.parseInt(mOneShopVO.getCountdown()) / 60 - hour * 60;
            int sec = BaseCommonUtils.parseInt(mOneShopVO.getCountdown()) - min * 60 - hour * 60 * 60;
            downTimerView.setTime(hour, min, sec);
            downTimerView.start();
        } else {
            header = LayoutInflater.from(this).inflate(R.layout.one_shop_detail_header3, (ViewGroup) findViewById(android.R.id.content), false);
            mDetailImg = (ImageView) header.findViewById(R.id.detail_img);
            mTitleTv = (TextView) header.findViewById(R.id.title_tv);

            Picasso.with(this).load(mOneShopVO.getMember_avatar_url()).into(((ImageView) header.findViewById(R.id.head_img)));
            ((TextView) header.findViewById(R.id.name_tv)).setText("获奖者：" + mOneShopVO.getMember_name());
            ((TextView) header.findViewById(R.id.number_tv)).setText("期号：" + mOneShopVO.getOd_no());
            ((TextView) header.findViewById(R.id.buy_cout)).setText("会员购买：" + mOneShopVO.getBuy_count() + "人次");
            ((TextView) header.findViewById(R.id.time_tv)).setText("揭晓时间：" + mOneShopVO.getPublish_date());
            ((TextView) header.findViewById(R.id.state_tv)).setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.XJColor5, R.color.XJColor5));
            ((TextView) header.findViewById(R.id.win_tv)).setText("幸运号码：" + mOneShopVO.getPrize_code());
            ((TextView) header.findViewById(R.id.run_tv)).setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.C1, R.color.XJColor2));
            final int[] numberType = {1};//1收起状态 2展开状态
            final TextView numberTv = ((TextView) header.findViewById(R.id.my_number_tv));
            final TextView allNumberTv = ((TextView) header.findViewById(R.id.all_number_tv));
            final TextView typeTv = ((TextView) header.findViewById(R.id.type_tv));

            header.findViewById(R.id.type_tv).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (numberType[0] == 1) {
                        numberType[0] = 2;
                        numberTv.setVisibility(View.GONE);
                        allNumberTv.setVisibility(View.VISIBLE);
                        typeTv.setText("收起");

                    } else {
                        numberType[0] = 1;
                        numberTv.setVisibility(View.VISIBLE);
                        allNumberTv.setVisibility(View.GONE);
                        typeTv.setText("展开");
                    }
                }
            });


            numberTv.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (numberTv.getLineCount() > 3) {
                        typeTv.setText("展开");
                        typeTv.setVisibility(View.VISIBLE);
                    } else {
                        typeTv.setVisibility(View.GONE);
                    }
                }
            }, 100);


            if (mOneShopVO.getLucky_nums() != null) {
                header.findViewById(R.id.my_number_layout).setVisibility(View.VISIBLE);
                numberTv.setText(mOneShopVO.getLucky_nums());
                allNumberTv.setText(mOneShopVO.getLucky_nums());
            } else {
                header.findViewById(R.id.my_number_layout).setVisibility(View.GONE);
            }
        }
        headerTv1 = (com.rey.material.widget.TextView) header.findViewById(R.id.header_tv1);
        headerTv2 = (com.rey.material.widget.TextView) header.findViewById(R.id.header_tv2);
        headerTv3 = (com.rey.material.widget.TextView) header.findViewById(R.id.header_tv3);
        headerTv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("url", mOneShopVO.getDetail_url());
                open(WebViewActivity.class, bundle, 0);
            }
        });
        headerTv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("goods_id", mOneShopVO.getGoods_id());
                open(OneShopLotteryHistoryActivity.class, bundle, 0);
            }
        });
        headerTv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("goods_id", mOneShopVO.getGoods_id());
                open(OneShopShowListActivity.class, bundle, 0);
            }
        });

        return header;
    }

    @Override
    public void onLoadMore() {
        isLoadMore = true;
        mThreadUtil = new ThreadUtil(mActivity, this);
        mThreadUtil.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buy_all_tv:
                if ("1".equals(mAllRemain)) {
                    mAllRemain = "0";
                    mBuyAllTv.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.XJColor2, R.color.XJColor2));
                    mBuyAllTv.setTextColor(ContextCompat.getColor(this, R.color.C1));
                    mNumberBt.setCurrentNumber(BaseCommonUtils.parseInt(mOneShopVO.getBuy_limit()));

                } else {
                    mAllRemain = "1";
                    mBuyAllTv.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.C3, R.color.C3));
                    mBuyAllTv.setTextColor(ContextCompat.getColor(this, R.color.C5));
                    mNumberBt.setCurrentNumber(BaseCommonUtils.parseInt(mOneShopVO.getOd_remain_count()));


                }

                break;
            case R.id.quick_tv:
                Bundle bundle = new Bundle();
                if (AppApplication.getInstance().getUserInfoVO() == null) {
                    Intent intent = new Intent();
                    intent.putExtra("type", "2");//type 是post 结束类型
                    intent.setClass(this, LoginActivity.class);
                    startActivityForResult(intent, LOGIN_CODE);
                } else {
                    bundle.putString("cart_info", mOneShopVO.getOd_id() + "|" + mCurrentCount);
                    bundle.putString("all_remain", mAllRemain);
                    bundle.putString("type", "2");//1普通商品2一元购商品
                    mActivity.open(OrderSureActivity.class, bundle, 0);
                }

                break;
            case R.id.back_tv:
                this.finish();
                break;
            case R.id.share_img:
                ShareUtil.share(OneShopDetailActivity.this, mOneShopVO.getShare_data().getImage(), mOneShopVO.getShare_data().getTitle(), mOneShopVO.getShare_data().getDesc(), mOneShopVO
                        .getShare_data().getUrl());
                break;
            default:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onTextChange(int count) {
        mCurrentCount = count;

        if (mCurrentCount != BaseCommonUtils.parseInt(mOneShopVO.getOd_remain_count())) {
            mBuyAllTv.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.XJColor2, R.color.XJColor2));
            mBuyAllTv.setTextColor(ContextCompat.getColor(this, R.color.C1));
//            if("1".equals(mAllRemain))
            mAllRemain = "0";
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case LOGIN_CODE:
                mThreadUtil = new ThreadUtil(mActivity, this);
                mThreadUtil.start();
                break;
            default:
                break;
        }
    }


}
