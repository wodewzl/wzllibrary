
package com.beisheng.synews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.http.HttpClientUtil;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.ThreadUtil;
import com.beisheng.base.view.BSAutoSwipeRefreshLayout;
import com.beisheng.synews.adapter.HomeNewsFragmentAdapter;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.interfaces.LoadMoreListener;
import com.beisheng.synews.mode.NewsVO;
import com.beisheng.synews.view.BSListViewLoadMore;
import com.google.gson.Gson;
import com.im.zhsy.R;

import java.util.HashMap;

public class ZhengWuActivity extends BaseActivity implements LoadMoreListener, OnRefreshListener, OnItemClickListener {
    private boolean mLodMore = true;// 控制多次加载，即当前加载就不在会加载直到结束
    protected BSAutoSwipeRefreshLayout mSwipeLayout;
    protected BSListViewLoadMore mListView;
    private HomeNewsFragmentAdapter mAdapter;
    private int mState = 0; // 0为首次,1为下拉刷新 ，2为加载更多
    public String mPage = "1";// 用来存储数据的，1为默认第一页，不是只有一页
    private NewsVO mNewsVO;
    private String mContentId;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.zheng_wu_activity, mBaseContentLayout);
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    @Override
    public void initView() {
        mSwipeLayout = (BSAutoSwipeRefreshLayout) findViewById(R.id.swipe_container);
        setSwipeRefreshLayoutColors(mSwipeLayout);
        mListView = (BSListViewLoadMore) findViewById(R.id.list_view);
        mAdapter = new HomeNewsFragmentAdapter(this);
        mListView.setAdapter(mAdapter);
        initData();
    }

    public void initData() {
        Intent intent = this.getIntent();
        mContentId = intent.getStringExtra("id");
    }

    @Override
    public void bindViewsListener() {
        mListView.setLoadMoreListener(this);
        mListView.setOnItemClickListener(this);
        mSwipeLayout.setOnRefreshListener(this);
    }

    public boolean getData() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("page", mPage);
            map.put("contentid", mContentId);
            if (hasNetWork()) {
                String jsonStr = HttpClientUtil.getRequest(this, Constant.DOMAIN_NAME + Constant.ZHENG_WU_ITEM, map);
                mNewsVO = gson.fromJson(jsonStr, NewsVO.class);
                saveJsonCache(Constant.ZHENG_WU_ITEM, map, jsonStr);

            } else {
                String oldStr = getCacheFromDatabase(Constant.ZHENG_WU_ITEM, map);
                mNewsVO = gson.fromJson(oldStr, NewsVO.class);
            }

            if (Constant.RESULT_SUCCESS_CODE.equals(mNewsVO.getCode())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void onRefresh() {
        mState = 1;
        mPage = "1";
        new ThreadUtil(this, this).start();
    }

    @Override
    public void loadMore() {
        if (mLodMore) {
            mLodMore = false;
            mState = 2;
            mPage = (BaseCommonUtils.parseInt(mPage) + 1) + "";
            new ThreadUtil(this, this).start();
        }
    }

    @Override
    public void executeSuccess() {
        super.executeSuccess();
        mLodMore = true;
        mListView.setVisibility(View.VISIBLE);
        mSwipeLayout.setRefreshing(false);

        mBaseTitleTv.setText(mNewsVO.getSubtitle());

        if (BaseCommonUtils.parseInt(mNewsVO.getTotal()) > BaseCommonUtils.parseInt(mNewsVO.getPage())) {
            mListView.showFooterView(true);
        } else {
            mListView.showFooterView(false);
        }

        if (1 == mState) {
            mAdapter.mList.size();
            mAdapter.updateDataFrist(mNewsVO.getList());
        } else if (2 == mState) {
            mAdapter.mList.size();
            mAdapter.updateDataLast(mNewsVO.getList());
        } else {
            mAdapter.updateData(mNewsVO.getList());
        }

    }

    @Override
    public void executeFailure() {

        mLodMore = true;
        mListView.setVisibility(View.VISIBLE);
        mSwipeLayout.setRefreshing(false);
        mListView.showFooterView(false);
        if (mNewsVO != null) {
            super.executeSuccess();
            showCustomToast(mNewsVO.getRetinfo());
        } else {
            super.executeFailure();
            showCustomToast("亲，请检查网络哦");
            mListView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
        if (adapterView.getAdapter().getCount() == 0)
            return;
        NewsVO vo = (NewsVO) adapterView.getAdapter().getItem(position);
        if (vo == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putString("isReplay", "0");
        bundle.putString("id", vo.getContentid());
        bundle.putString("govermentid", vo.getGovermentid());
        bundle.putString("new_type", "government");
        bundle.putString("suburl", "9");
        openActivity(NewsDetailActivity.class, bundle, 0);
    }
}
