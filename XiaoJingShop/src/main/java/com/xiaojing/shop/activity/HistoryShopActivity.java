package com.xiaojing.shop.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

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
import com.xiaojing.shop.adapter.HistoryShopRadapter;
import com.xiaojing.shop.application.AppApplication;
import com.xiaojing.shop.constant.Constant;
import com.xiaojing.shop.mode.ShopVO;

import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;

public class HistoryShopActivity extends BaseActivity implements OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, BGAOnRVItemClickListener {
    private AutoSwipeRefreshLayout mAutoSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView;
    private HistoryShopRadapter mAdapter;
    private ShopVO mHistoryShopVO;
    private int mCurrentPage = 1;
    private boolean isLoadMore = true;
    private String mType = "1";//type 1 收藏 2足迹

    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.history_shop_activity);
    }

    @Override
    public void initView() {
        mAutoSwipeRefreshLayout = getViewById(R.id.swipe_refresh_layout);
        mActivity.setSwipeRefreshLayoutColors(mAutoSwipeRefreshLayout);
        mRecyclerView = getViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mAdapter = new HistoryShopRadapter(mRecyclerView);
        LuRecyclerViewAdapter adapter = new LuRecyclerViewAdapter(mAdapter);
        mRecyclerView.setAdapter(adapter);
        DividerDecoration divider = DividerUtil.linnerDivider(this, R.dimen.dp_1, R.color.C3);
        mRecyclerView.addItemDecoration(divider);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadMoreEnabled(true);
        mType = this.getIntent().getStringExtra("type");
        if ("1".equals(mType)) {
            mBaseTitleTv.setText("我的收藏");
        } else {
            mBaseTitleTv.setText("我的足迹");
        }
    }

    @Override
    public void bindViewsListener() {
        mAutoSwipeRefreshLayout.setOnRefreshListener(this);
        mAdapter.setOnRVItemClickListener(this);
        mRecyclerView.setOnLoadMoreListener(this);
    }

    @Override
    public void getData() {
        if ("1".equals(mType)) {
            RequestParams paramsMap = new RequestParams();
            String url = Constant.FAVORITES_SFHOP_URL;
            if (AppApplication.getInstance().getUserInfoVO() != null)
                paramsMap.put("key", AppApplication.getInstance().getUserInfoVO().getKey());
            paramsMap.put("curpage", mCurrentPage);
            HttpClientUtil.get(mActivity, mThreadUtil, url, paramsMap, ShopVO.class);
        } else {
            RequestParams paramsMap = new RequestParams();
            String url = Constant.HISTORY_SHOP_URL;
            if (AppApplication.getInstance().getUserInfoVO() != null)
                paramsMap.put("key", AppApplication.getInstance().getUserInfoVO().getKey());
            paramsMap.put("curpage", mCurrentPage);
            HttpClientUtil.get(mActivity, mThreadUtil, url, paramsMap, ShopVO.class);
        }

    }


    @Override
    public void hasData(BaseVO vo) {
        ShopVO shopVO = (ShopVO) vo;
        mHistoryShopVO = shopVO.getDatas();
        List<ShopVO> list;
        if ("1".equals(mType)) {
            list = mHistoryShopVO.getFavorites_list();
        } else {
            list = mHistoryShopVO.getGoodsbrowse_list();
        }

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
    public void onRVItemClick(ViewGroup viewGroup, View view, int i) {
        if (mAdapter.getData().size() == 0)
            return;
        Bundle bundle = new Bundle();
        ShopVO vo = (ShopVO) mAdapter.getData().get(i);
        bundle.putString("url", vo.getDetail_url());
        open(WebViewActivity.class, bundle, 0);
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
}
