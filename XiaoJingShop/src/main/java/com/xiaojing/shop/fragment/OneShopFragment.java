package com.xiaojing.shop.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.cpoopc.scrollablelayoutlib.ScrollableHelper;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.loopj.android.http.RequestParams;
import com.wuzhanglong.library.ItemDecoration.DividerDecoration;
import com.wuzhanglong.library.fragment.BaseFragment;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.utils.DividerUtil;
import com.wuzhanglong.library.utils.ThreadUtil;
import com.wuzhanglong.library.view.AutoSwipeRefreshLayout;
import com.xiaojing.shop.R;
import com.xiaojing.shop.activity.OneShopDetailActivity;
import com.xiaojing.shop.adapter.OneShopAdapter;
import com.xiaojing.shop.application.AppApplication;
import com.xiaojing.shop.constant.Constant;
import com.xiaojing.shop.mode.OneShopVO;

import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;

public class OneShopFragment extends BaseFragment implements ScrollableHelper.ScrollableContainer, OnLoadMoreListener, BGAOnRVItemClickListener {
    private AutoSwipeRefreshLayout mAutoSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView;
    private OneShopAdapter mAdapter;
    private String type = "1";
    private OneShopVO mOneShopVO;
    private int mCurrentPage = 1;
    private boolean isLoadMore = true;


    public static OneShopFragment newInstance() {
        OneShopFragment fragment = new OneShopFragment();
        return fragment;
    }

    @Override
    public View getScrollableView() {
        return mRecyclerView;
    }

    @Override
    public void getData() {

        RequestParams paramsMap = new RequestParams();
        String url = Constant.ONE_SHOP_URL;
        if (AppApplication.getInstance().getUserInfoVO() != null)
            paramsMap.put("key", AppApplication.getInstance().getUserInfoVO().getKey());
        paramsMap.put("type_id", getType());
        paramsMap.put("curpage", mCurrentPage);
        HttpClientUtil.get(mActivity, mThreadUtil, url, paramsMap, OneShopVO.class);
    }

    @Override
    public void hasData(BaseVO vo) {
        mActivity.dismissProgressDialog();
        OneShopVO oneShopVO = (OneShopVO) vo;
        mOneShopVO = oneShopVO.getDatas();
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
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void noData(BaseVO vo) {

    }

    @Override
    public void noNet() {

    }

    @Override
    public void setContentView() {
        contentInflateView(R.layout.one_shop_fragment);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void initView(View view) {
        mRecyclerView = getViewById(R.id.recycler_view);
//        mRecyclerView.setLayoutManager(new GridLayoutManager(mActivity,2));

        mAdapter = new OneShopAdapter(mRecyclerView);
        LuRecyclerViewAdapter luAdapter = new LuRecyclerViewAdapter(mAdapter);
        mRecyclerView.setAdapter(luAdapter);
        DividerDecoration divider = DividerUtil.linnerDivider(mActivity, R.dimen.dp_4, R.color.C3);
        mRecyclerView.addItemDecoration(divider);
//        mRecyclerView.addItemDecoration(DividerUtil.bgaGridDivider(R.dimen.dp_2));
        mRecyclerView.setLoadMoreEnabled(true);

        GridLayoutManager layoutManager = new GridLayoutManager(mActivity, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mAdapter.getData().size() == 0) {

                    return 2;
                } else {

                    return 1;
                }
            }
        });
    }

    @Override
    public void bindViewsListener() {
        mRecyclerView.setOnLoadMoreListener(this);
        mAdapter.setOnRVItemClickListener(this);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void onLoadMore() {
        isLoadMore = true;
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
        mActivity.open(OneShopDetailActivity.class, bundle, 0);
    }


}
