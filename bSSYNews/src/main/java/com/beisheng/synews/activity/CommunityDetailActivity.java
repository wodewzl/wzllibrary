
package com.beisheng.synews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.http.HttpClientUtil;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.ThreadUtil;
import com.beisheng.base.view.BSAutoSwipeRefreshLayout;
import com.beisheng.synews.adapter.CommunityDetailAdapter;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.fragment.BottomFragment;
import com.beisheng.synews.interfaces.LoadMoreListener;
import com.beisheng.synews.mode.CommunityVO;
import com.beisheng.synews.view.BSListViewLoadMore;
import com.google.gson.Gson;
import com.im.zhsy.R;

import java.util.HashMap;

public class CommunityDetailActivity extends BaseActivity implements LoadMoreListener, OnClickListener, OnRefreshListener {
    private String mPage = "1";
    private int mState = 0; // 0为首次,1为下拉刷新 ，2为加载更多
    private CommunityVO mCommunityVO;
    private BSListViewLoadMore mListView;
    private CommunityDetailAdapter mAdapter;
    private String mFid;
    private TextView mCommentEt;
    private LinearLayout mBottomLayout;
    private BottomFragment mBottomFragment;
    private TextView mTitleTv;
    private boolean mLodMore = true;// 控制多次加载，即当前加载就不在会加载直到结束
    protected BSAutoSwipeRefreshLayout mSwipeLayout;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.community_detail_activity, mBaseContentLayout);
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    @Override
    public void initView() {
        mBaseOkTv.setText("回帖");
        mBaseOkTv.setVisibility(View.GONE);
        mSwipeLayout = (BSAutoSwipeRefreshLayout) findViewById(R.id.swipe_container);
        setSwipeRefreshLayoutColors(mSwipeLayout);
        mListView = (BSListViewLoadMore) findViewById(R.id.list_view);
        mAdapter = new CommunityDetailAdapter(this);
        mListView.setAdapter(mAdapter);
        mBottomLayout = (LinearLayout) findViewById(R.id.bottom_layout);
        mTitleTv = (TextView) findViewById(R.id.title_tv);

        initData();
    }

    @Override
    public void bindViewsListener() {
        mListView.setLoadMoreListener(this);
        mBaseOkTv.setOnClickListener(this);
        mSwipeLayout.setOnRefreshListener(this);
    }

    public void initData() {
        Intent intent = this.getIntent();
        mFid = intent.getStringExtra("id");
        if (intent.getStringExtra("title") != null) {
            mBaseTitleTv.setText(intent.getStringExtra("title"));
        } else {
            mBaseTitleTv.setText("秦楚论坛");
        }

    }

    public boolean getData() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("fid", mFid);
            map.put("page", mPage);

            if (hasNetWork()) {
                String jsonStr = HttpClientUtil.getRequest(this, Constant.DOMAIN_NAME + Constant.COMMUNITY_DETAIL_URL, map);
                mCommunityVO = gson.fromJson(jsonStr, CommunityVO.class);
                saveJsonCache(Constant.COMMUNITY_DETAIL_URL, map, jsonStr);
            } else {
                String oldStr = getCacheFromDatabase(Constant.COMMUNITY_DETAIL_URL, map);
                mCommunityVO = gson.fromJson(oldStr, CommunityVO.class);
            }

            if (Constant.RESULT_SUCCESS_CODE.equals(mCommunityVO.getCode())) {
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
    public void executeSuccess() {
        super.executeSuccess();
        mSwipeLayout.setRefreshing(false);
        mLodMore = true;
        if (mCommunityVO.getShare_tit() != null && !"".equals(mCommunityVO.getShare_tit()))
            mTitleTv.setText(mCommunityVO.getShare_tit());
        if (BaseCommonUtils.parseInt(mCommunityVO.getTotal()) > BaseCommonUtils.parseInt(mCommunityVO.getPage())) {
            mListView.showFooterView(true);
        } else {
            mListView.showFooterView(false);
        }
        if (1 == mState) {
            mAdapter.mList.size();
            mAdapter.updateDataFrist(mCommunityVO.getForum());
        } else if (2 == mState) {
            mAdapter.mList.size();
            mAdapter.updateDataLast(mCommunityVO.getForum());
        } else {
            mAdapter.updateData(mCommunityVO.getForum());
        }

        try {
            // 底部View
            mBottomFragment = new BottomFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.bottom_layout, mBottomFragment);
            mBottomFragment.setCommunityvo(mCommunityVO);
            transaction.commit();
        } catch (Exception e) {
        }
    }

    @Override
    public void executeFailure() {
        super.executeFailure();
        mSwipeLayout.setRefreshing(false);
        mLodMore = true;
        mListView.showFooterView(false);
        if (mCommunityVO != null)
            showCustomToast(mCommunityVO.getRetinfo());
        else
            showCustomToast("亲，请检查网络哦");
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.base_ok_tv:
                Bundle bundle = new Bundle();
                bundle.putString("fid", mCommunityVO.getFid());
                bundle.putString("tid", mCommunityVO.getTid());
                bundle.putString("title", mCommunityVO.getTitle());
                bundle.putString("type", "2");
                openActivity(CommunityAddActivity.class, bundle, 0);
                break;

            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        mState = 1;
        mPage = "1";
        new ThreadUtil(this, this).start();
    }
}
