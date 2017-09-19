
package com.bs.bsims.activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.CrmVisitRecordNewAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.CrmVisitorVo;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSRefreshListView;
import com.bs.bsims.view.PinnedSectionListView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

/***
 * 此类是业务员主页的5个跳转（新增） mine（自己） datime（2014-06日期） 参数 此类是仪表盘外的商机跳转（新增 放弃）mode（本月 上月 季度） option（新增 放弃
 * 全部） 参数
 **/
public class CrmStaticsNewAddVistorActivity extends BaseActivity {
    private String  mMine, mDateTime;
    private LinearLayout edit_layout, client_title_layout;
    private PinnedSectionListView mRefreshListView;
    private CrmVisitRecordNewAdapter crmIndexListAdapter;
    private CrmVisitorVo mVisitor;
    public static final String DETAIL_EDIT = "detail_edit";
    // 下拉上拉刷新部分
    private View mFootLayout;
    private TextView mMoreTextView;
    private ProgressBar mProgressBar;
    private int mState = 0; // 0为首次,1为上拉刷新 ，2为下拉刷新
    private String mLastid;
    private Boolean canClickFlag = true;// 解决连续点“更多”可能会出现的异常
    private String uid;
    private Context context;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.crmvisitrecord_index, mContentLayout);
    }

    @Override
    public boolean getDataResult() {
        // TODO Auto-generated method stub
        return getData();
    }

    private boolean getData() {
        try {
            Gson gson = new Gson();

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put("uid", uid);
            map.put("datetime", mDateTime);
            map.put("mine", mMine);
            map.put("lastid", mLastid);
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.CRM_VISIT_RECORD_LISTINDEX, map);
            mVisitor = gson.fromJson(jsonStr, CrmVisitorVo.class);
            if (Constant.RESULT_CODE.equals(mVisitor.getCode())) {
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
        if (2 == mState) {
            crmIndexListAdapter.updateDataLast(mVisitor.getArray());
        } else if (0 == mState) {
            crmIndexListAdapter.updateData(mVisitor.getArray());
        }

        crmIndexListAdapter.notifyDataSetChanged();
        mRefreshListView.onRefreshComplete();

        if (mState != 1) {
            footViewIsVisibility();
        }
        mState = 0;
        canClickFlag = true;
    }

    public void footViewIsVisibility() {
        if (mVisitor == null || mVisitor.getCount() == null) {
            return;
        }
        if (Integer.parseInt(mVisitor.getCount()) <= 15) {
            mFootLayout.setVisibility(View.GONE);
        } else {
            mFootLayout.setVisibility(View.VISIBLE);
            mMoreTextView.setText("更多");
            mProgressBar.setVisibility(View.GONE);
        }

    }

    @Override
    public void executeFailure() {
        // if (mCrmListVO == null) {
        // super.showNoNetView();
        // } else {
        // super.showNoContentView();
        // }

        // 列表展示的时候不能调用父类
        super.isRequestFinish();
        crmIndexListAdapter.notifyDataSetChanged();
        mRefreshListView.onRefreshComplete();
        footViewIsVisibility();

        // 不适合只隐藏列表，适合隐藏整个布局;
        if (mVisitor == null) {
            super.showNoNetView();
        } else {
            if (mState == 0) {
                crmIndexListAdapter.updateData(new ArrayList<CrmVisitorVo>());
                mFootLayout.setVisibility(View.GONE);
            }
        }

        mState = 0;
        canClickFlag = true;
    }

    @Override
    public void updateUi() {
        // mCrmListAdapter.updateData(mCrmListVO.getArray());

    }

    @Override
    public void initView() {
        context = this;
        edit_layout = (LinearLayout) findViewById(R.id.title_bar);
        edit_layout.setVisibility(View.GONE);
        mRefreshListView = (PinnedSectionListView) findViewById(R.id.refresh_listview);
        crmIndexListAdapter = new CrmVisitRecordNewAdapter(this);
        mRefreshListView.setAdapter(crmIndexListAdapter);
        mMine = null == getIntent().getStringExtra("mine") ? "" : getIntent().getStringExtra("mine");
        if (getIntent().getStringExtra("userid") != null) {
            uid = getIntent().getStringExtra("userid");
        }
        else {
            uid = BSApplication.getInstance().getUserId();
        }
        // 业务员跳转
        if (getIntent().getStringExtra("datetime") != null) {
            mDateTime = getIntent().getStringExtra("datetime");
            mTitleTv.setText("新增跟单");
        }
        initFoot();
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

    @Override
    public void bindViewsListener() {
        mRefreshListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                if (crmIndexListAdapter.mList.size() > 0 && crmIndexListAdapter.getItemViewType((int) arg3) != -10) {
                    Intent i = new Intent();
                    i.setClass(context, CrmVisitRecordDetailActivity.class);
                    i.putExtra("vid", crmIndexListAdapter.mList.get(crmIndexListAdapter.mPinnedList.get((int) arg3)).getVid());
                    crmIndexListAdapter.mList.get(crmIndexListAdapter.mPinnedList.get((int) arg3)).setIsread("1");
                    crmIndexListAdapter.notifyDataSetChanged();
                    context.startActivity(i);
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
                    match(6, crmIndexListAdapter.mList.get(crmIndexListAdapter.mList.size() - 1).getVid());
                }
            }
        });

    }

    public void match(int key, String value) {
        switch (key) {
            case 6:
                mLastid = value;
                mState = 2;
                break;
            default:
                break;
        }
        mRefreshListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
        new ThreadUtil(this, this).start();
    }

}
