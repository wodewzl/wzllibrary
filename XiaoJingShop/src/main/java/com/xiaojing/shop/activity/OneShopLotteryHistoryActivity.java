package com.xiaojing.shop.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.loopj.android.http.RequestParams;
import com.wuzhanglong.library.ItemDecoration.DividerDecoration;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.utils.DividerUtil;
import com.wuzhanglong.library.utils.ThreadUtil;
import com.wuzhanglong.library.view.AutoSwipeRefreshLayout;
import com.xiaojing.shop.R;
import com.xiaojing.shop.adapter.OneShopLotteryHistoryAdapter;
import com.xiaojing.shop.application.AppApplication;
import com.xiaojing.shop.constant.Constant;
import com.xiaojing.shop.mode.OneShopVO;

import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;

public class OneShopLotteryHistoryActivity extends BaseActivity implements OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, BGAOnRVItemClickListener, View.OnClickListener {

    private AutoSwipeRefreshLayout mAutoSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView;
    private OneShopLotteryHistoryAdapter mAdapter;
    private int mCurrentPage = 1;
    private boolean isLoadMore = true;
    private OneShopVO mOneShopVO;
    private TextView mNumberTv;
    private LinearLayout mHeadLayout;
    private String mOdId;

    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.one_shop_lottery_activity);
    }

    @Override
    public void initView() {
        mBaseTitleTv.setText("往期揭晓");
        mHeadLayout = getViewById(R.id.head_layout);
        mAutoSwipeRefreshLayout = getViewById(R.id.swipe_refresh_layout);
        mActivity.setSwipeRefreshLayoutColors(mAutoSwipeRefreshLayout);
        mNumberTv = getViewById(R.id.number_tv);
        mRecyclerView = getViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mAdapter = new OneShopLotteryHistoryAdapter(mRecyclerView);
        LuRecyclerViewAdapter adapter = new LuRecyclerViewAdapter(mAdapter);
        mRecyclerView.setAdapter(adapter);
        DividerDecoration divider = DividerUtil.linnerDivider(this, R.dimen.dp_10, R.color.C3);
        mRecyclerView.addItemDecoration(divider);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadMoreEnabled(true);

    }

    @Override
    public void bindViewsListener() {
        mAutoSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setOnLoadMoreListener(this);
        mAdapter.setOnRVItemClickListener(this);
        mHeadLayout.setOnClickListener(this);
    }

    @Override
    public void getData() {
        RequestParams paramsMap = new RequestParams();
        String mUrl = Constant.ONE_SHOP_HISTORY_LOTTERY_URL;
        if (AppApplication.getInstance().getUserInfoVO() != null)
            paramsMap.put("key", AppApplication.getInstance().getUserInfoVO().getKey());
        paramsMap.put("curpage", mCurrentPage);
        paramsMap.put("goods_id", this.getIntent().getStringExtra("goods_id"));
        HttpClientUtil.get(mActivity, mThreadUtil, mUrl, paramsMap, OneShopVO.class);
    }

    @Override
    public void hasData(BaseVO vo) {
        OneShopVO oneShopVO = (OneShopVO) vo;
        mOneShopVO = oneShopVO.getDatas();
        if (oneShopVO.getLastest_publish() == null) {
            mHeadLayout.setVisibility(View.GONE);
        } else {
            mNumberTv.setText(oneShopVO.getLastest_publish().getOd_no());
            mHeadLayout.setVisibility(View.VISIBLE);
            mOdId = oneShopVO.getLastest_publish().getOd_id();
        }

        if (mOneShopVO.getLastest_publish() != null)
            mNumberTv.setText(mOneShopVO.getLastest_publish().getOd_no());

        List<OneShopVO> list = mOneShopVO.getList();
        if (vo.getHasmore() != null && "1".equals(vo.getHasmore())) {
            mRecyclerView.setNoMore(false);
        } else {
            mRecyclerView.setNoMore(true);
        }
        if (isLoadMore) {
            mAdapter.updateDataLast(list);
            isLoadMore = false;
            mCurrentPage++;
        } else {
            mCurrentPage++;
            mAdapter.updateData(list);
        }
        mAutoSwipeRefreshLayout.setRefreshing(false);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void noData(BaseVO vo) {

    }

    @Override
    public void noNet() {

    }

    @Override
    public void onLoadMore() {
        isLoadMore = true;
        mThreadUtil = new ThreadUtil(mActivity, this);
        mThreadUtil.start();
    }

    @Override
    public void onRefresh() {
        mCurrentPage = 1;
        mThreadUtil = new ThreadUtil(mActivity, this);
        mThreadUtil.start();
    }

    @Override
    public void onRVItemClick(ViewGroup viewGroup, View view, int i) {
        if (mAdapter.getData().size() == 0)
            return;
        OneShopVO vo = (OneShopVO) mAdapter.getData().get(i);
        Bundle bundle = new Bundle();
        bundle.putString("od_id", vo.getOd_id());
        open(OneShopDetailActivity.class, bundle, 0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.name_tv:
                Bundle bundle = new Bundle();
                bundle.putString("od_id", mOdId);
                open(OneShopDetailActivity.class, bundle, 0);
                break;
            default:
                break;
        }
    }
}
