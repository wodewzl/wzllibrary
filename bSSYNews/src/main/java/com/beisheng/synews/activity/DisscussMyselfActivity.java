
package com.beisheng.synews.activity;

import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.http.HttpClientUtil;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.ThreadUtil;
import com.beisheng.base.view.BSAutoSwipeRefreshLayout;
import com.beisheng.synews.adapter.DiscussMyselfAdapter;
import com.beisheng.synews.application.AppApplication;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.interfaces.LoadMoreListener;
import com.beisheng.synews.mode.DisscussMyselfVO;
import com.beisheng.synews.utils.StartViewUitl;
import com.beisheng.synews.view.BSListViewLoadMore;
import com.google.gson.Gson;
import com.im.zhsy.R;

import java.util.HashMap;

public class DisscussMyselfActivity extends BaseActivity implements OnRefreshListener, LoadMoreListener, OnItemClickListener {
    private DisscussMyselfVO mDisscussMyselfVO;
    private BSListViewLoadMore mListView;
    private DiscussMyselfAdapter mAdapter;
    protected BSAutoSwipeRefreshLayout mSwipeLayout;
    private int mState = 0; // 0为首次,1为下拉刷新 ，2为加载更多
    private String mPage = "1";
    private boolean mLodMore = true;// 控制多次加载，即当前加载就不在会加载直到结束
    private LinearLayout mCommentLayout;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.disscuss_activity, mBaseContentLayout);
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    @Override
    public void initView() {
        mBaseTitleTv.setText("我的评论");
        mSwipeLayout = (BSAutoSwipeRefreshLayout) findViewById(R.id.swipe_container);
        setSwipeRefreshLayoutColors(mSwipeLayout);
        mListView = (BSListViewLoadMore) findViewById(R.id.list_view);
        mAdapter = new DiscussMyselfAdapter(this);
        mListView.setAdapter(mAdapter);
        mCommentLayout = (LinearLayout) findViewById(R.id.comment_layout);
        mCommentLayout.setVisibility(View.GONE);
    }

    @Override
    public void bindViewsListener() {
        mSwipeLayout.setOnRefreshListener(this);
        mListView.setLoadMoreListener(this);
        mListView.setOnItemClickListener(this);
    }

    public boolean getData() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("uid", AppApplication.getInstance().getUid());
            map.put("page", mPage);
            if (hasNetWork()) {
                String jsonStr = HttpClientUtil.getRequest(this, Constant.DOMAIN_NAME + Constant.DISSCUSS_MYSELF, map);
                mDisscussMyselfVO = gson.fromJson(jsonStr, DisscussMyselfVO.class);
                saveJsonCache(Constant.DISSCUSS_MYSELF, map, jsonStr);

            } else {
                String oldStr = getCacheFromDatabase(Constant.DISSCUSS_MYSELF, map);
                mDisscussMyselfVO = gson.fromJson(oldStr, DisscussMyselfVO.class);
            }

            if (Constant.RESULT_SUCCESS_CODE.equals(mDisscussMyselfVO.getCode())) {
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
        if (BaseCommonUtils.parseInt(mDisscussMyselfVO.getTotal()) > BaseCommonUtils.parseInt(mDisscussMyselfVO.getPage())) {
            mListView.showFooterView(true);
        } else {
            mListView.showFooterView(false);
        }
        if (1 == mState) {
            mAdapter.mList.size();
            mAdapter.updateDataFrist(mDisscussMyselfVO.getList());
        } else if (2 == mState) {
            mAdapter.mList.size();
            mAdapter.updateDataLast(mDisscussMyselfVO.getList());
        } else {
            mAdapter.updateData(mDisscussMyselfVO.getList());
        }
    }

    @Override
    public void executeFailure() {
        super.executeFailure();
        mLodMore = true;
        mSwipeLayout.setRefreshing(false);
        mListView.showFooterView(false);
        if (mDisscussMyselfVO != null)
            showCustomToast(mDisscussMyselfVO.getRetinfo());
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

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        DisscussMyselfVO vo = (DisscussMyselfVO) arg0.getAdapter().getItem(arg2);
        if (vo == null)
            return;
        StartViewUitl.startView(this, vo.getSuburl(), vo.getContentid(), vo.getLink(), null, null);
    }

}
