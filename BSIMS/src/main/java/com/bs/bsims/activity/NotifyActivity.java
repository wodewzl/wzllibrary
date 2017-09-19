
package com.bs.bsims.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.NotifyAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.NotifyResultVO;
import com.bs.bsims.model.NotifyVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.utils.UrlUtil;
import com.bs.bsims.view.BSRefreshListView;
import com.bs.bsims.view.BSRefreshListView.OnRefreshListener;
import com.google.gson.Gson;

import java.util.List;

public class NotifyActivity extends BaseActivity {
    public static final String FIRSTID = "firstid";
    public static final String LASTID = "lastid";

    private NotifyAdapter mNotifyAdapter;

    // listView
    private BSRefreshListView mRefreshListView;
    private View mFootLayout;
    private TextView mMoreTextView;
    private ProgressBar mProgressBar;
    // 0为首次,1为上拉刷新 ，2为下拉刷新
    private int mState = 0;

    private NotifyResultVO mNotifyResultVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        mRefreshListView = (BSRefreshListView) findViewById(R.id.lv_refresh);
        if ("1".equals(BSApplication.getInstance().getUserFromServerVO().getIsboss())) {
            mTitleTv.setText("动向");
        } else {
            mTitleTv.setText("提醒");
        }

        mNotifyAdapter = new NotifyAdapter(this);
        mRefreshListView.setAdapter(mNotifyAdapter);
        initFoot();
    }

    public boolean getData() {

        if (0 == mState) {
            return getData("", "", "");
        } else if (1 == mState) {

            if (mNotifyAdapter.mList.size() > 0) {
                String id = mNotifyAdapter.mList.get(0).getMessageid();
                return getData("", FIRSTID, id);
            } else {
                return getData("", "", "");
            }

        } else if (2 == mState) {
            String id = mNotifyAdapter.mList.get(mNotifyAdapter.mList.size() - 1).getMessageid();
            return getData("", LASTID, id);
        }
        return false;
    }

    public boolean getData(String userId, String refresh, String id) {
        try {
            String url = UrlUtil.getNotifyUrl(Constant.NOTIFY_URL, BSApplication.getInstance().getUserId(), refresh, id);
            String jsonUrlStr = HttpClientUtil.get(url, Constant.ENCODING).trim();
            Gson gson = new Gson();
            mNotifyResultVO = gson.fromJson(jsonUrlStr, NotifyResultVO.class);
            if (Constant.RESULT_CODE.equals(mNotifyResultVO.getCode())) {
                if (Constant.FIRSTID.equals(refresh)) {
                    if (mNotifyResultVO.getCount() != null) {
                    }

                } else if (Constant.LASTID.equals(refresh)) {
                } else {
                }
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
    public void bindViewsListener() {
        bindRefreshListener();
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    @Override
    public void updateUi() {
        mNotifyAdapter.notifyDataSetChanged();
    }

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.notify, null);
        mContentLayout.addView(layout);
    }

    // 加载更多数据
    public void initFoot() {
        mFootLayout = LayoutInflater.from(this).inflate(R.layout.listview_bottom_more, null);
        mMoreTextView = (TextView) mFootLayout.findViewById(R.id.txt_loading);
        mMoreTextView.setText("更多");
        mProgressBar = (ProgressBar) mFootLayout.findViewById(R.id.progressBar);
        mFootLayout.setVisibility(View.GONE);
        mRefreshListView.addFooterView(mFootLayout);
    }

    /**
     * 加载更多是否隐藏
     * 
     * @param datas
     */
    protected void footViewIsVisibility(List<NotifyVO> datas) {
        if (mNotifyResultVO == null) {
            return;
        }
        if (mNotifyResultVO.getCount() == null) {
            return;
        }
        if (Integer.parseInt(mNotifyResultVO.getCount()) <= 15) {
            mFootLayout.setVisibility(View.GONE);
            // listView.removeFooterView(mFootLayout);
        } else {
            mFootLayout.setVisibility(View.VISIBLE);
            mMoreTextView.setText("更多");
            mProgressBar.setVisibility(View.GONE);
        }

    }

    public void bindRefreshListener() {
        mRefreshListView.setonRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                mState = 1;
                new ThreadUtil(NotifyActivity.this, NotifyActivity.this).start();
            }
        });
        mFootLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mMoreTextView.setText("正在加载...");
                mProgressBar.setVisibility(View.VISIBLE);
                mState = 2;
                new ThreadUtil(NotifyActivity.this, NotifyActivity.this).start();
            }
        });
    }

    @Override
    public void executeSuccess() {
        if (1 == mState) {
            mNotifyAdapter.updateDataFrist(mNotifyResultVO.getArray());
        } else if (2 == mState) {
            mNotifyAdapter.updateDataLast(mNotifyResultVO.getArray());
        } else {
            mNotifyAdapter.updateData(mNotifyResultVO.getArray());
        }

        mLoading.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
        mNotifyAdapter.notifyDataSetChanged();
        mRefreshListView.onRefreshComplete();

        if (mState != 1) {
            footViewIsVisibility(mNotifyResultVO.getArray());
        }
        mState = 0;
    }

    @Override
    public void executeFailure() {
        mNotifyAdapter.notifyDataSetChanged();
        mRefreshListView.onRefreshComplete();
        footViewIsVisibility(mNotifyAdapter.mList);
        // mLoading.setText(mNotifyResultVO.getRetinfo());

        if (mNotifyResultVO != null) {
            mLoading.setText(mNotifyResultVO.getRetinfo());
        } else {
            // mLoading.setText("网络异常");
            CommonUtils.setNonetIcon(this, mLoading,this);
        }
    }
}
