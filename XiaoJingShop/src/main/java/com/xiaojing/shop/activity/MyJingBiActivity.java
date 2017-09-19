package com.xiaojing.shop.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.loopj.android.http.RequestParams;
import com.vondear.rxtools.view.tooltips.RxToolTip;
import com.vondear.rxtools.view.tooltips.RxToolTipsManager;
import com.wuzhanglong.library.ItemDecoration.DividerDecoration;
import com.wuzhanglong.library.ItemDecoration.StickyHeaderDecoration;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.wuzhanglong.library.utils.DividerUtil;
import com.wuzhanglong.library.utils.ThreadUtil;
import com.wuzhanglong.library.view.AutoSwipeRefreshLayout;
import com.xiaojing.shop.R;
import com.xiaojing.shop.adapter.MyJBAdapter;
import com.xiaojing.shop.application.AppApplication;
import com.xiaojing.shop.constant.Constant;
import com.xiaojing.shop.mode.MoneyVO;

import java.util.List;

public class MyJingBiActivity extends BaseActivity implements RxToolTipsManager.TipListener, View.OnClickListener, OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    RxToolTipsManager mRxToolTipsManager;
    private TextView mAvailableTv, mAllTv, mFreezeTv, mAllTv01, mAllTv02, mFreezeTv01, mFreezeTv02;
    private LinearLayout mAllLaout, mFreezeLayout,mOverLayout;
    private com.rey.material.widget.TextView mShopTv;
    private LinearLayout mRootLayout;
    int mAlign = RxToolTip.ALIGN_CENTER;

    private AutoSwipeRefreshLayout mAutoSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView;
    private MyJBAdapter mAdapter;
    private StickyHeaderDecoration decor;
    private LuRecyclerViewAdapter mLuAdapter;
    private int mCurrentPage = 1;
    private boolean isLoadMore = true;
    private MoneyVO mMoneyVO;
    private String mType = "1";
    private TextView mTypeTv;


    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.my_jing_bi_activity);
    }

    @Override
    public void initView() {
        mAutoSwipeRefreshLayout = getViewById(R.id.swipe_refresh_layout);
        setSwipeRefreshLayoutColors(mAutoSwipeRefreshLayout);
        mRecyclerView = getViewById(R.id.recycler_view);
//      mRecyclerView.setPullRefreshEnabled(false);
        DividerDecoration divider = DividerUtil.linnerDivider(this, R.dimen.dp_1, R.color.C3);
        mRecyclerView.addItemDecoration(divider);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyJBAdapter(this);
        mLuAdapter = new LuRecyclerViewAdapter(mAdapter);
//      CommonHeader headerView = new CommonHeader(this, R.layout.my_jingbi_head);
        mLuAdapter.addHeaderView(initHeadView());
        decor = new StickyHeaderDecoration(mAdapter);
        mRecyclerView.setAdapter(mLuAdapter);
        mRecyclerView.addItemDecoration(decor, 1);
        mType = this.getIntent().getStringExtra("type");
        mAdapter.setType(Integer.parseInt(mType));
    }

    @Override
    public void bindViewsListener() {
        mAutoSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setOnLoadMoreListener(this);
    }


    @Override
    public void getData() {
        RequestParams params = new RequestParams();
        String url = "";
        if ("1".equals(mType)) {
            url = Constant.JING_BI_ULR_URL;
        } else {
            url = Constant.JING_DOU_ULR_URL;
        }
        if (AppApplication.getInstance().getUserInfoVO() != null)
            params.put("key", AppApplication.getInstance().getUserInfoVO().getKey());
        params.put("curpage", mCurrentPage);
        HttpClientUtil.get(mActivity, mThreadUtil, url, params, MoneyVO.class);
    }

    @Override
    public void hasData(BaseVO vo) {
        MoneyVO moneyVO = (MoneyVO) vo;
        mMoneyVO = moneyVO.getDatas();

        List<MoneyVO> list = mMoneyVO.getList();
        if (vo.getHasmore() != null && "1".equals(vo.getHasmore())) {
            mRecyclerView.setNoMore(false);
        } else {
            mRecyclerView.setNoMore(true);
        }
        if (isLoadMore) {
            isLoadMore = false;
            mCurrentPage++;
            mAdapter.updateDataLast(list);
        } else {
            mCurrentPage++;
            mAdapter.updateData(list);
        }

        mAutoSwipeRefreshLayout.setRefreshing(false);
        mAdapter.notifyDataSetChanged();


        if ("1".equals(mType)) {
            mBaseTitleTv.setText("我的鲸币");
            mTypeTv.setText("可用鲸币");
            mAvailableTv.setText( mMoneyVO.getGold_info().getAvailable_gold());
            mAllTv01.setText("鲸币总额：");
            mAllTv02.setText(mMoneyVO.getGold_info().getTotal_gold());
            mFreezeTv01.setText("鲸币余额：");
            mFreezeTv02.setText(mMoneyVO.getGold_info().getFreeze_gold());
            mRootLayout.setVisibility(View.VISIBLE);

        } else {
            mBaseTitleTv.setText("我的小鲸豆");
            mTypeTv.setText("可用小鲸豆");
            mAvailableTv.setText( mMoneyVO.getBean_info().getAvailable_bean());


            mAllTv01.setText("总小鲸豆：");
            mAllTv02.setText(mMoneyVO.getBean_info().getTotal_bean());
            mFreezeTv01.setText("可用小鲸豆余额：");
            mFreezeTv02.setText(mMoneyVO.getBean_info().getFreeze_bean());
            mRootLayout.setVisibility(View.GONE);
        }


    }

    @Override
    public void noData(BaseVO vo) {

    }

    @Override
    public void noNet() {

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public View initHeadView() {
        View header = LayoutInflater.from(this).inflate(R.layout.my_jingbi_head, (ViewGroup) findViewById(android.R.id.content), false);
        mAvailableTv = (TextView) header.findViewById(R.id.jingbi_tv);
        mAllLaout = (LinearLayout) header.findViewById(R.id.all_layout);
        mAllTv = (TextView) header.findViewById(R.id.all_tv);
        mAllTv01 = (TextView) header.findViewById(R.id.all_tv_01);
        mAllTv02 = (TextView) header.findViewById(R.id.all_tv_02);
        mFreezeLayout = (LinearLayout) header.findViewById(R.id.freeze_layout);
        mFreezeTv = (TextView) header.findViewById(R.id.freeze_tv);
        mFreezeTv01 = (TextView) header.findViewById(R.id.freeze_tv_01);
        mFreezeTv02 = (TextView) header.findViewById(R.id.freeze_tv_02);
        mShopTv = (com.rey.material.widget.TextView) header.findViewById(R.id.shop_tv);
        mRxToolTipsManager = new RxToolTipsManager(this);
        mAllLaout.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.XJColor11, R.color.XJColor11));
        mAllTv.setBackground(BaseCommonUtils.setBackgroundShap(this, 100, R.color.XJColor4, R.color.XJColor4));
        mFreezeTv.setBackground(BaseCommonUtils.setBackgroundShap(this, 100, R.color.XJColor5, R.color.XJColor5));
        mFreezeLayout.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.XJColor12, R.color.XJColor12));
        mShopTv.setBackground(BaseCommonUtils.setBackgroundShap(this, 50, R.color.XJColor6, R.color.C1));
        mShopTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(OneShopActivity.class);
            }
        });
        mTypeTv= (TextView) header.findViewById(R.id.type_tv);
        mOverLayout= (LinearLayout) header.findViewById(R.id.over_layout);
        mRootLayout= (LinearLayout) header.findViewById(R.id.root_layout);
        return header;
    }

    @Override
    public void onTipDismissed(View view, int i, boolean b) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_tv:

                break;
            default:
                break;
        }
    }

    @Override
    public void onLoadMore() {
        isLoadMore = true;
        mThreadUtil = new ThreadUtil(this, this);
        mThreadUtil.start();
    }

    @Override
    public void onRefresh() {
        mCurrentPage = 1;
        mThreadUtil = new ThreadUtil(this, this);
        mThreadUtil.start();
    }
}
