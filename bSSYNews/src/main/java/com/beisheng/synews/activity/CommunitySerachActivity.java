
package com.beisheng.synews.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.http.HttpClientUtil;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.ThreadUtil;
import com.beisheng.base.view.BSAutoSwipeRefreshLayout;
import com.beisheng.base.view.BSIndexEditText;
import com.beisheng.synews.adapter.CommunityTabFragmentAdapter;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.interfaces.LoadMoreListener;
import com.beisheng.synews.mode.CommunityVO;
import com.beisheng.synews.view.BSListViewLoadMore;
import com.google.gson.Gson;
import com.im.zhsy.R;

import java.util.HashMap;

public class CommunitySerachActivity extends BaseActivity implements OnRefreshListener, LoadMoreListener, OnItemClickListener {
    // private KeyWordVO mKeyWordVO;
    private LinearLayout mKeywordLayout;
    public String mPage = "1";// 用来存储数据的，1为默认第一页，不是只有一页
    private TextView mLastOnclickTv;
    private String mKeyword;
    private BSListViewLoadMore mListView;
    protected BSAutoSwipeRefreshLayout mSwipeLayout;
    private int mState = 0; // 0为首次,1为下拉刷新 ，2为加载更多
    private BSIndexEditText mBSBsIndexEditText;
    private boolean mLodMore = true;// 控制多次加载，即当前加载就不在会加载直到结束
    private String mStatus = "0";// 1为社区收索 其它为新闻收索
    private CommunityTabFragmentAdapter mAdapter;
    private CommunityVO mCommunityVO;
    private TextView mListViewHead;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.community_serach_activity, mBaseContentLayout);
    }

    @Override
    public boolean getDataResult() {
        if (mKeyword != null)
            return getData();
        else
            return true;
    }

    @Override
    public void initView() {
        mBaseTitleTv.setText("搜索");
        mBSBsIndexEditText = (BSIndexEditText) findViewById(R.id.edit_single_search);
        mSwipeLayout = (BSAutoSwipeRefreshLayout) findViewById(R.id.swipe_container);
        setSwipeRefreshLayoutColors(mSwipeLayout);
        mListView = (BSListViewLoadMore) findViewById(R.id.list_view);
        mListViewHead = new TextView(this);
        mListViewHead.setVisibility(View.GONE);
        mListViewHead.setTextColor(this.getResources().getColor(R.color.sy_title_color));
        mListViewHead.setBackgroundColor(this.getResources().getColor(R.color.C3));
        int padding = BaseCommonUtils.dip2px(this, 10);
        mListViewHead.setPadding(padding, padding, padding, 0);
        mListView.addHeaderView(mListViewHead);
        mAdapter = new CommunityTabFragmentAdapter(this);
        mListView.setAdapter(mAdapter);
        initData();
    }

    public void initData() {
        mKeyword = this.getIntent().getStringExtra("keyword");
        mStatus = this.getIntent().getStringExtra("status");
    }

    @Override
    public void bindViewsListener() {
        mSwipeLayout.setOnRefreshListener(this);
        mListView.setLoadMoreListener(this);
        mListView.setOnItemClickListener(this);

        mBSBsIndexEditText.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) mBSBsIndexEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(CommunitySerachActivity.this
                            .getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    mKeyword = mBSBsIndexEditText.getText().toString();
                    mState = 0;
                    showProgressDialog();
                    new ThreadUtil(CommunitySerachActivity.this, CommunitySerachActivity.this).start();
                    return true;
                }
                return false;
            }
        });

        mBSBsIndexEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ("".equals(s.toString())) {
                    mSwipeLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    public boolean getData() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("keyword", mKeyword);
            map.put("page", mPage);
            if (hasNetWork()) {
                String jsonStr = HttpClientUtil.getRequest(this, Constant.DOMAIN_NAME + Constant.COMMUNITY_SEARCH, map);
                mCommunityVO = gson.fromJson(jsonStr, CommunityVO.class);
                saveJsonCache(Constant.DOMAIN_NAME + Constant.COMMUNITY_SEARCH, map, jsonStr);
            } else {
                String oldStr = getCacheFromDatabase(Constant.DOMAIN_NAME + Constant.COMMUNITY_SEARCH, map);
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
        if (mKeyword == null)
            return;
        mLodMore = true;
        mSwipeLayout.setRefreshing(false);
        if (BaseCommonUtils.parseInt(mCommunityVO.getTotal()) > BaseCommonUtils.parseInt(mCommunityVO.getPage())) {
            mListView.showFooterView(true);
        } else {
            mListView.showFooterView(false);
        }
        mListViewHead.setVisibility(View.VISIBLE);
        mListViewHead.setText("共搜索" + BaseCommonUtils.parseInt(mCommunityVO.getTotal()) * BaseCommonUtils.parseInt(mCommunityVO.getPerpage()) + "条数据");

        if (1 == mState) {
            mAdapter.mList.size();
            mAdapter.updateDataFrist(mCommunityVO.getForum());
        } else if (2 == mState) {
            mAdapter.mList.size();
            mAdapter.updateDataLast(mCommunityVO.getForum());
        } else {
            mAdapter.updateData(mCommunityVO.getForum());
        }
        mSwipeLayout.setVisibility(View.VISIBLE);

    }

    @Override
    public void executeFailure() {
        mLodMore = true;
        mSwipeLayout.setRefreshing(false);
        mListView.showFooterView(false);
        if (mCommunityVO != null) {
            showCustomToast(mCommunityVO.getRetinfo());
            dismissProgressDialog();
        } else {
            super.executeFailure();
            showCustomToast("亲，请检查网络哦");
        }

    }

    @Override
    public void onRefresh() {
        if (mLodMore) {
            mLodMore = false;
            mState = 1;
            mPage = "1";
            new ThreadUtil(this, this).start();
        }

    }

    @Override
    public void loadMore() {
        mState = 2;
        mPage = (BaseCommonUtils.parseInt(mPage) + 1) + "";
        new ThreadUtil(this, this).start();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
        if (adapterView.getAdapter().getCount() == 0)
            return;
        CommunityVO vo = (CommunityVO) adapterView.getAdapter().getItem(position);
        if (vo == null)
            return;
        Bundle bundle = new Bundle();
        bundle.putString("id", vo.getTid());
        bundle.putString("title", "论坛详情");

        openActivity(CommunityDetailActivity.class, bundle, 0);
    }
}
