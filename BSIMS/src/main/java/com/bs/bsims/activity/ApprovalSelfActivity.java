
package com.bs.bsims.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.ApprovalAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.ApprovalResultVO;
import com.bs.bsims.model.ApprovalVO;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.utils.UrlUtil;
import com.bs.bsims.view.BSRefreshListView;
import com.bs.bsims.view.BSRefreshListView.OnRefreshListener;
import com.google.gson.Gson;

import java.util.List;

public class ApprovalSelfActivity extends BaseActivity implements OnItemClickListener {
    private ApprovalAdapter mAdapter;
    private BSRefreshListView mBsRefreshListView;
    private String mUid;
    private String mLargeclass;
    private String mSmallclass;
    private ApprovalResultVO mApprovalResultVO;

    // listView
    private View mFootLayout;
    private TextView mMoreTextView;
    private ProgressBar mProgressBar;
    // 0为首次,1为上拉刷新 ，2为下拉刷新
    private int mState = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        mTitleTv.setText(R.string.view_approval);
        mBsRefreshListView = (BSRefreshListView) findViewById(R.id.lv_approval);
        mAdapter = new ApprovalAdapter(this);
        mBsRefreshListView.setAdapter(mAdapter);
        initData();
        initFoot();
    }

    public void initData() {
        Intent intent = getIntent();
        mUid = intent.getStringExtra("uid");
        mLargeclass = intent.getStringExtra("largeclass");
        mSmallclass = intent.getStringExtra("smallclass");

    }

    @Override
    public void bindViewsListener() {
        bindRefreshListener();
        mBsRefreshListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra("alid", mAdapter.mList.get((int) id).getId());
        intent.putExtra("uid", BSApplication.getInstance().getUserId());
        mLargeclass = mAdapter.mList.get((int) id).getType();
        intent.putExtra("type", mLargeclass);
        if ("1".equals(mLargeclass)) {
            intent.setClass(this, ApprovalLeaveDetailActivity.class);
        } else if ("2".equals(mLargeclass)) {
            intent.setClass(this, ApprovalSuppliesDetailActivity.class);
        } else if ("3".equals(mLargeclass)) {
            intent.setClass(this, ApprovalOvertimeDetailActivity.class);
        } else if ("4".equals(mLargeclass)) {
            intent.setClass(this, ApprovalFeeApplyDetailActivity.class);
        } else {
            intent.setClass(this, ApprovalAttendanceDetailActivity.class);
        }
        this.startActivity(intent);
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    @Override
    public void updateUi() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.approval_self, null);
        mContentLayout.addView(layout);
    }

    @Override
    public void executeSuccess() {
        mAdapter.notifyDataSetChanged();
        mBsRefreshListView.onRefreshComplete();
        mLoading.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);
        footViewIsVisibility(mAdapter.mList);
    };

    @Override
    public void executeFailure() {
        mAdapter.notifyDataSetChanged();
        mBsRefreshListView.onRefreshComplete();
        footViewIsVisibility(mAdapter.mList);
        if (mApprovalResultVO.getCode() != null) {
            mLoading.setText(mApprovalResultVO.getRetinfo());
        } else {
            mLoading.setText("网路错误");
        }
    }

    // 加载更多数据
    public void initFoot() {
        mFootLayout = LayoutInflater.from(this).inflate(R.layout.listview_bottom_more, null);
        mMoreTextView = (TextView) mFootLayout.findViewById(R.id.txt_loading);
        mMoreTextView.setText("更多");
        mProgressBar = (ProgressBar) mFootLayout.findViewById(R.id.progressBar);
        mFootLayout.setVisibility(View.GONE);
        mBsRefreshListView.addFooterView(mFootLayout);
    }

    protected void footViewIsVisibility(List<ApprovalVO> datas) {
        if (mApprovalResultVO == null) {
            return;
        }
        if (mApprovalResultVO.getCount() == null) {
            return;
        }
        if (Integer.parseInt(mApprovalResultVO.getCount()) <= 15) {
            mFootLayout.setVisibility(View.GONE);
            // listView.removeFooterView(mFootLayout);
        } else {
            mFootLayout.setVisibility(View.VISIBLE);
            mMoreTextView.setText("更多");
            mProgressBar.setVisibility(View.GONE);
        }
    }

    public void bindRefreshListener() {
        mBsRefreshListView.setonRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                mState = 1;
                new ThreadUtil(ApprovalSelfActivity.this, ApprovalSelfActivity.this).start();
            }
        });
        mFootLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mMoreTextView.setText("正在加载...");
                mProgressBar.setVisibility(View.VISIBLE);
                mState = 2;
                new ThreadUtil(ApprovalSelfActivity.this, ApprovalSelfActivity.this).start();
            }
        });
    }

    public boolean getData() {

        if (0 == mState) {
            return getData("", "");
        } else if (1 == mState) {
            if (mAdapter.mList.size() > 0) {
                String id = mAdapter.mList.get(0).getId();
                return getData(Constant.FIRSTID, id);
            } else {
                return getData("", "");
            }

        } else if (2 == mState) {
            String id = mAdapter.mList.get(mAdapter.mList.size() - 1).getId();
            return getData(Constant.LASTID, id);
        }
        return false;
    }

    public boolean getData(String refresh, String id) {

        try {
            String strUlr = UrlUtil.getMyApprovalUrl(Constant.MY_APPROVAL_URL, BSApplication.getInstance().getUserId(), refresh, id);
            String jsonStr = HttpClientUtil.get(strUlr, Constant.ENCODING).trim();
            Gson gson = new Gson();
            mApprovalResultVO = gson.fromJson(jsonStr, ApprovalResultVO.class);

            if (Constant.RESULT_CODE.equals(mApprovalResultVO.getCode())) {

                if (Constant.FIRSTID.equals(refresh)) {
                    if (mApprovalResultVO.getCount() != null) {
                        mAdapter.mList = mApprovalResultVO.getArray();
                    }

                } else if (Constant.LASTID.equals(refresh)) {
                    mAdapter.mList.addAll(mApprovalResultVO.getArray());
                } else {
                    mAdapter.mList = mApprovalResultVO.getArray();
                }
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
