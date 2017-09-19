package com.beisheng.easycar.activity;

import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.beisheng.easycar.R;
import com.beisheng.easycar.adapter.ShopPromotionsAdapter;
import com.beisheng.easycar.constant.Constant;
import com.beisheng.easycar.mode.PromotionsVO;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.wuzhanglong.library.ItemDecoration.DividerDecoration;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.http.HttpGetDataUtil;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.wuzhanglong.library.utils.DividerUtil;
import com.wuzhanglong.library.utils.ThreadUtil;
import com.wuzhanglong.library.view.AutoSwipeRefreshLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;

public class ShopPromotionsActivity extends BaseActivity implements OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, BGAOnRVItemClickListener {
    private AutoSwipeRefreshLayout mAutoSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView;
    private ShopPromotionsAdapter mAdapter;
    private int mCurrentPage = 1;
    private boolean isLoadMore = true;
    private PromotionsVO mPromotionsVO;

    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.shop_promotions_activity);
    }

    @Override
    public void initView() {
        mBaseTitleTv.setText("商家活动");
        mBaseHeadLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.Car2));
        mAutoSwipeRefreshLayout = getViewById(R.id.swipe_refresh_layout);
        mActivity.setSwipeRefreshLayoutColors(mAutoSwipeRefreshLayout);

        mRecyclerView = getViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mAdapter = new ShopPromotionsAdapter(mRecyclerView);
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
    }

    @Override
    public void getData() {
//        RequestParams params = new RequestParams();
        String mUrl = Constant.PROMOTIONS_URL;
//        HttpClientUtil.get(mActivity, mThreadUtil, mUrl, params, PromotionsVO.class);
        Map<String, Object> parameters = new HashMap<>();
        HttpGetDataUtil.get(this,mUrl,parameters,PromotionsVO.class);
    }

    @Override
    public void hasData(BaseVO vo) {
        PromotionsVO promotionsVO = (PromotionsVO) vo;
        mPromotionsVO = promotionsVO.getData();
        List<PromotionsVO> list = mPromotionsVO.getList();
        if (BaseCommonUtils.parseInt(vo.getCount())>BaseCommonUtils.parseInt(vo.getPage())) {
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
//        if (mAdapter.getData().size() == 0)
//            return;
//        ShopVO vo = (ShopVO) mAdapter.getData().get(i);
//        Bundle bundle = new Bundle();
//        bundle.putString("url", vo.getArticle_url());
//        open(WebViewActivity.class, bundle, 0);
    }
}
