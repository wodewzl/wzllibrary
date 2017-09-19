
package com.beisheng.synews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.http.HttpClientUtil;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.ThreadUtil;
import com.beisheng.base.view.BSAutoSwipeRefreshLayout;
import com.beisheng.synews.adapter.DiscussAdapter;
import com.beisheng.synews.application.AppApplication;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.interfaces.LoadMoreListener;
import com.beisheng.synews.mode.NewsVO;
import com.beisheng.synews.view.BSListViewLoadMore;
import com.beisheng.synews.view.BSPopwindowEditText;
import com.beisheng.synews.view.BSPopwindowEditText.CommitCallback;
import com.google.gson.Gson;
import com.im.zhsy.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

public class DisscussActivity extends BaseActivity implements OnRefreshListener, LoadMoreListener, OnItemClickListener, OnClickListener {
    private NewsVO mNewsVO;
    private BSListViewLoadMore mListView;
    private DiscussAdapter mAdapter;
    protected BSAutoSwipeRefreshLayout mSwipeLayout;
    private int mState = 0; // 0为首次,1为下拉刷新 ，2为加载更多
    private String mPage = "1";
    private String mContentId;
    private String mType;// suburl
    private String mUserid;
    private boolean mCommitFlag = false;
    private BSPopwindowEditText mPopEditText;
    private String mPid;
    private String mTitle;
    private boolean mLodMore = true;// 控制多次加载，即当前加载就不在会加载直到结束
    private TextView mCommitTv;

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
        mBaseTitleTv.setText("评论");
        mSwipeLayout = (BSAutoSwipeRefreshLayout) findViewById(R.id.swipe_container);
        setSwipeRefreshLayoutColors(mSwipeLayout);
        mSwipeLayout.autoRefresh();
        mPopEditText = new BSPopwindowEditText(this, mCallback);
        mListView = (BSListViewLoadMore) findViewById(R.id.list_view);
        mAdapter = new DiscussAdapter(this);
        mListView.setAdapter(mAdapter);
        initData();
        mCommitTv = (TextView) findViewById(R.id.comment_et);
        mCommitTv.setBackground(BaseCommonUtils.setBackgroundShap(this, 20, R.color.devider_bg, R.color.C1));
    }

    @Override
    public void bindViewsListener() {
        mSwipeLayout.setOnRefreshListener(this);
        mListView.setLoadMoreListener(this);
        mListView.setOnItemClickListener(this);
        mCommitTv.setOnClickListener(this);
    }

    CommitCallback mCallback = new CommitCallback() {

        @Override
        public void commtiCallback(String content) {
            commit(mContentId, content, mType, mPid, mTitle, mUserid);
        }
    };

    public void initData() {
        Intent intent = this.getIntent();
        mContentId = intent.getStringExtra("id");
        mType = intent.getStringExtra("type");
        mTitle = intent.getStringExtra("title");
    }

    public boolean getData() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("contentid", mContentId);
            map.put("type", mType);
            map.put("page", mPage);
            if (hasNetWork()) {
                String jsonStr = HttpClientUtil.getRequest(this, Constant.DOMAIN_NAME + Constant.VIEW_DISSCUSS_URL, map);
                mNewsVO = gson.fromJson(jsonStr, NewsVO.class);
                saveJsonCache(Constant.VIEW_DISSCUSS_URL, map, jsonStr);

            } else {
                String oldStr = getCacheFromDatabase(Constant.VIEW_DISSCUSS_URL, map);
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
    public void executeSuccess() {
        super.executeSuccess();
        mLodMore = true;
        mSwipeLayout.setRefreshing(false);
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
        super.executeSuccess();
        mLodMore = true;
        mSwipeLayout.setRefreshing(false);
        mListView.showFooterView(false);
        if (mNewsVO != null)
            showCustomToast(mNewsVO.getRetinfo());
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

    public void commit(String conttentid, String content, String suburl, String pid, String title, String userid) {
        mCommitFlag = false;
        showProgressDialog();
        RequestParams params = new RequestParams();
        try {
            params.put("uid", AppApplication.getInstance().getUid());
            params.put("sessionid", AppApplication.getInstance().getSessionid());
            params.put("contentid", conttentid);
            params.put("type", suburl);

            params.put("reply", content);
            params.put("pid", pid);
            params.put("title", title);
            params.put("userid", userid);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        String allUrl = Constant.DOMAIN_NAME + Constant.DISSCUSS_URL;
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(allUrl, params, new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                mCommitFlag = true;
                dismissProgressDialog();
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                String result = new String(arg2);
                dismissProgressDialog();
                mCommitFlag = true;
                try {
                    JSONObject jsonObject = new JSONObject(new String(arg2));
                    String str = (String) jsonObject.get("retinfo");
                    String code = (String) jsonObject.get("code");
                    if (Constant.RESULT_SUCCESS_CODE.equals(code)) {
                        showCustomToast(str);
                        new ThreadUtil(DisscussActivity.this, DisscussActivity.this).start();
                    } else {
                        showCustomToast(str);
                    }
                } catch (Exception e) {
                }

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
        if (adapterView.getAdapter().getCount() == 0)
            return;
        NewsVO vo = (NewsVO) adapterView.getAdapter().getItem(position);
        if (vo == null)
            return;

        if (vo.getChildren() == null) {
            if (AppApplication.getInstance().getUserInfoVO() == null) {
                openActivity(LoginActivity.class);
                return;
            }
            String hint = "回复#" + vo.getNickname() + "#：";
            mPopEditText.setSecondDisscuss(hint);
            mPid = vo.getPid();
            mUserid = vo.getUserid();
            mPopEditText.showPopupWindow(adapterView);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("id", mContentId);
            bundle.putString("type", mType);
            bundle.putSerializable("vo", (Serializable) vo);
            bundle.putString("title", mNewsVO.getTitle());
            openActivity(DisscussDetailActivity.class, bundle, 0);
        }

    }

    public void disscussChild(NewsVO vo) {
        if (AppApplication.getInstance().getUserInfoVO() == null) {
            openActivity(LoginActivity.class);
            return;
        }
        String hint = "回复#" + vo.getNickname() + "#：";
        mPopEditText.setSecondDisscuss(hint);
        // mPid = vo.getPid();
        mPid = vo.getParentid();
        mUserid = vo.getUserid();
        mPopEditText.showPopupWindow(mListView);
    }

    @Override
    public void onClick(View arg0) {
        if (AppApplication.getInstance().getUserInfoVO() == null) {
            openActivity(LoginActivity.class);
            return;
        }
        // String hint = "回复#" + mNewsVO.getNickname() + "#：";
        // mPopEditText.setSecondDisscuss(hint);
        // mPid = mNewsVO.getPid();
        // mUserid = mNewsVO.getUserid();
        mPopEditText.showPopupWindow(mListView);
        // mDisscussType = "1";
    }
}
