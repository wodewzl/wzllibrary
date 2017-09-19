
package com.beisheng.synews.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.http.HttpClientUtil;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.ThreadUtil;
import com.beisheng.base.view.BSAutoSwipeRefreshLayout;
import com.beisheng.synews.adapter.PointsRecordAdapter;
import com.beisheng.synews.application.AppApplication;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.interfaces.LoadMoreListener;
import com.beisheng.synews.mode.PointsMallVO;
import com.beisheng.synews.view.BSListViewLoadMore;
import com.google.gson.Gson;
import com.im.zhsy.R;

import java.util.HashMap;

public class PointsRecordActivity extends BaseActivity implements OnRefreshListener, LoadMoreListener {
    private PointsMallVO mPointsMallVO;
    private BSListViewLoadMore mListView;
    private PointsRecordAdapter mAdapter;

    public BSAutoSwipeRefreshLayout mSwipeLayout;
    private int mState = 0; // 0为首次,1为下拉刷新 ，2为加载更多
    private String mPage = "1";
    private String mContentId;
    private String mType;// 1为新闻，2为直播
    private boolean mLodMore = true;// 控制多次加载，即当前加载就不在会加载直到结束

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.points_record_activity, mBaseContentLayout);
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    @Override
    public void initView() {
        mBaseTitleTv.setText("积分记录");
        mSwipeLayout = (BSAutoSwipeRefreshLayout) findViewById(R.id.swipe_container);
        setSwipeRefreshLayoutColors(mSwipeLayout);
        mListView = (BSListViewLoadMore) findViewById(R.id.list_view);
        mAdapter = new PointsRecordAdapter(this);
        mListView.setAdapter(mAdapter);
        initData();

    }

    @Override
    public void bindViewsListener() {
        mSwipeLayout.setOnRefreshListener(this);
        mListView.setLoadMoreListener(this);
    }

    public void initData() {
        Intent intent = this.getIntent();
        mContentId = intent.getStringExtra("id");
        mType = intent.getStringExtra("type");
    }

    public boolean getData() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("uid", AppApplication.getInstance().getUid());
            map.put("sessionid", AppApplication.getInstance().getSessionid());
            map.put("page", mPage);

            if (hasNetWork()) {
                String jsonStr = HttpClientUtil.getRequest(this, Constant.DOMAIN_NAME + Constant.SHOP_RECORD_URL, map);
                mPointsMallVO = gson.fromJson(jsonStr, PointsMallVO.class);
                saveJsonCache(Constant.SHOP_RECORD_URL, map, jsonStr);

            } else {
                String oldStr = getCacheFromDatabase(Constant.SHOP_RECORD_URL, map);
                mPointsMallVO = gson.fromJson(oldStr, PointsMallVO.class);
            }

            if (Constant.RESULT_SUCCESS_CODE.equals(mPointsMallVO.getCode())) {
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
        mLodMore = true;
        mSwipeLayout.setRefreshing(false);
        if (BaseCommonUtils.parseInt(mPointsMallVO.getTotal()) > BaseCommonUtils.parseInt(mPointsMallVO.getPage())) {

            mListView.showFooterView(true);
        } else {
            mListView.showFooterView(false);
        }
        if (1 == mState) {
            mAdapter.mList.size();
            mAdapter.updateDataFrist(mPointsMallVO.getList());
        } else if (2 == mState) {
            mAdapter.mList.size();
            mAdapter.updateDataLast(mPointsMallVO.getList());
        } else {
            mAdapter.updateData(mPointsMallVO.getList());
        }
    }

    @Override
    public void executeFailure() {
        super.executeFailure();
        mLodMore = true;
        mSwipeLayout.setRefreshing(false);
        mListView.showFooterView(false);
        if (mPointsMallVO != null)
            showCustomToast(mPointsMallVO.getRetinfo());
        else
            showCustomToast("亲，请检查网络哦");
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

}
