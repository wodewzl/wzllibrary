
package com.bs.bsims.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.BossNotityAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.NotifyResultVO;
import com.bs.bsims.model.NotifyVO;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSIndexEditText;
import com.bs.bsims.view.BSRefreshListView;
import com.bs.bsims.view.BSRefreshListView.OnRefreshListener;
import com.bs.bsims.view.PinnedSectionListView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BossNotifyActivity extends BaseActivity {

    private PinnedSectionListView mRefreshListView;
    private BossNotityAdapter mAdapter;
    private NotifyResultVO mNotifyVO;

    // 下拉上拉刷新部分
    private View mFootLayout;
    private TextView mMoreTextView;
    private ProgressBar mProgressBar;
    private int mState = 0; // 0为首次,1为上拉刷新 ，2为下拉刷新

    private boolean mFlag = true;
    private String mFristid, mLastid;
    private List<View> mViewList;// 筛选选中的布局
    private BSIndexEditText mBSBsIndexEditText;
    private Boolean canClickFlag = true;// 解决连续点“更多”可能会出现的异常

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.boss_notify, mContentLayout);
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    @Override
    public void updateUi() {

    }

    @Override
    public void initView() {
        mTitleTv.setText("系统消息");
        mRefreshListView = (PinnedSectionListView) findViewById(R.id.lv_refresh);
        mAdapter = new BossNotityAdapter(this);
        mRefreshListView.setAdapter(mAdapter);
        initData();
        initFoot();
    }

    public void initData() {
        Intent intent = this.getIntent();
    }

    @Override
    public void bindViewsListener() {
        mRefreshListView.setonRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mAdapter.mList.size() > 0) {
                    match(1, mAdapter.mList.get(0).getRid());
                } else {
                    mFristid = "";
                    match(1, "");
                }
            }
        });
        mFootLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canClickFlag) {
                    canClickFlag = false;
                    mMoreTextView.setText("正在加载...");
                    mProgressBar.setVisibility(View.VISIBLE);
                    match(2, mAdapter.mList.get(mAdapter.mList.size() - 1).getRid());
                }
            }
        });

    }

    @Override
    public void executeSuccess() {
        super.executeSuccess();

        if (1 == mState) {
            mAdapter.updateDataFrist(mNotifyVO.getArray());
        } else if (2 == mState) {
            mAdapter.updateDataLast(mNotifyVO.getArray());
        } else {
            mAdapter.updateData(mNotifyVO.getArray());
        }

        mAdapter.notifyDataSetChanged();
        mRefreshListView.onRefreshComplete();
        if ("".equals(mFristid) || mState != 1) {
            footViewIsVisibility();
        }
        mState = 0;
        canClickFlag = true;
    }

    @Override
    public void executeFailure() {

        // 列表展示的时候不能调用父类
        super.isRequestFinish();
        mAdapter.notifyDataSetChanged();
        mRefreshListView.onRefreshComplete();
        footViewIsVisibility();

        // 不适合只隐藏列表，适合隐藏整个布局;
        if (mNotifyVO == null) {
            super.showNoNetView();
        }
        else if (mAdapter.mList != null && mAdapter.mList.size() == 0 && mNotifyVO.getArray() == null) {
            super.showNoContentView();
        }
        else {
            if (mState == 0) {
                mAdapter.updateData(new ArrayList<NotifyVO>());
                mFootLayout.setVisibility(View.GONE);
            }
        }
        mState = 0;
        canClickFlag = true;
    }

    public void footViewIsVisibility() {
        if (mNotifyVO == null || mNotifyVO.getCount() == null) {
            return;
        }
        if (Integer.parseInt(mNotifyVO.getCount()) <= 15) {
            mFootLayout.setVisibility(View.GONE);
        } else {
            mFootLayout.setVisibility(View.VISIBLE);
            mMoreTextView.setText("更多");
            mProgressBar.setVisibility(View.GONE);
        }
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

    public boolean getData() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            if (0 == mState) {
                mFristid = "";
                mLastid = "";
            }
            map.put("firstid", mFristid);
            map.put("lastid", mLastid);
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.BOSS_NOTIFY, map);
            mNotifyVO = gson.fromJson(jsonStrList, NotifyResultVO.class);
            if (Constant.RESULT_CODE.equals(mNotifyVO.getCode())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void match(int key, String value) {
        switch (key) {
            case 1:
                mFristid = value;
                mLastid = "";
                mState = 1;
                break;
            case 2:
                mLastid = value;
                mFristid = "";
                mState = 2;
                break;
            default:
                break;
        }
        mRefreshListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
        new ThreadUtil(this, this).start();
    }

}
