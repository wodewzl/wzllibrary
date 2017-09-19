
package com.bs.bsims.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.CrmClientHomeAdapter;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.interfaces.UpdateCallback;
import com.bs.bsims.model.CrmClientHomeDetailVO;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.utils.UrlUtil;
import com.bs.bsims.view.BSListView;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

@SuppressLint("ValidFragment")
// 商机详情
public class CrmBussinesFragment2 extends BaseFragment implements
        OnClickListener, UpdateCallback {
    private static final String TAG = "CrmBussinesFragment2";
    private BSListView fragment_sharedfilesd_home_all_refreshlistview;
    private CrmClientHomeAdapter mRecordAdapter;
    private Activity activity;
    private View mFootLayout;
    private TextView mMoreTextView;
    private ProgressBar mProgressBar;
    private String mRefresh = "";
    private TextView loaTextView;
    // 0为首次,1为上拉刷新 ，2为下拉刷新
    private int mState = 0;

    /** 上拉ID */
    private String firstid;

    /** 下拉ID */
    private String lastid;

    /** 记录最后一条的id */
    private String saveLastId;

    private CrmClientHomeDetailVO mCrmClientHomeDetailVO;

    private String bid = "";
    private Boolean canClickFlag = true;// 解决连续点“更多”可能会出现的异常

    public CrmBussinesFragment2(String uid, String str) {
        this.bid = uid;
    }

    public CrmBussinesFragment2() {
    }

    // DanganChuQingInfoLineChartActivity
    @Override
    public String getFragmentName() {
        // TODO Auto-generated method stub
        return TAG;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.crmbussines_fragment2, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        bindListers();
    }

    private void bindListers() {
 

        mFootLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (canClickFlag) {
                    canClickFlag = false;
                mMoreTextView.setText("正在加载...");
                mProgressBar.setVisibility(View.VISIBLE);
                mState = 2;
                mRefresh = Constant.LASTID;
                saveLastId = mRecordAdapter.mList
                        .get(mRecordAdapter.mList.size() - 1).getVid();
                new ThreadUtil(activity,
                        CrmBussinesFragment2.this).start();
                }
            }
        });

    }

    // 加载更多数据
    public void initFoot() {
        mFootLayout = LayoutInflater.from(activity).inflate(
                R.layout.listview_bottom_more, null);
        mMoreTextView = (TextView) mFootLayout.findViewById(R.id.txt_loading);
        mMoreTextView.setText("更多");
        mProgressBar = (ProgressBar) mFootLayout.findViewById(R.id.progressBar);
        mFootLayout.setVisibility(View.GONE);
        fragment_sharedfilesd_home_all_refreshlistview.addFooterView(mFootLayout);
    }

    protected void footViewIsVisibility() {
        if (mCrmClientHomeDetailVO == null) {
            return;
        }
        if (mCrmClientHomeDetailVO.getCount() == null) {
            return;
        }
        if (Integer.parseInt(mCrmClientHomeDetailVO.getCount()) <= 15) {
            mFootLayout.setVisibility(View.GONE);
            // listView.removeFooterView(mFootLayout);
        } else {
            mFootLayout.setVisibility(View.VISIBLE);
            mMoreTextView.setText("更多");
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void initViews(View view) {
        // TODO Auto-generated method stub
        loaTextView = (TextView) view.findViewById(R.id.loadingfile1);
        loaTextView.setText("没有相关信息");
        fragment_sharedfilesd_home_all_refreshlistview = (BSListView) view
                .findViewById(R.id.fragment_sharedfilesd_home_all_refreshlistview1);
        mRecordAdapter = new CrmClientHomeAdapter(activity);
        mRecordAdapter.setmType(1);
        mRecordAdapter.setBusIsShow("0");
        fragment_sharedfilesd_home_all_refreshlistview.setAdapter(mRecordAdapter);
        fragment_sharedfilesd_home_all_refreshlistview.showHead(activity, true);
        initFoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // initData();
        new ThreadUtil(activity, this).start();
    }

    @Override
    public boolean execute() {
        // TODO Auto-generated method stub
        return getdata();
    }

    @Override
    public void executeSuccess() {
        // TODO Auto-generated method stub
        loaTextView.setVisibility(View.GONE);
        if (1 == mState) {
            mRecordAdapter.updateDataFrist(mCrmClientHomeDetailVO.getArray());
        } else if (2 == mState) {
            mRecordAdapter.updateDataLast(mCrmClientHomeDetailVO.getArray());
        } else {
            mRecordAdapter.updateData(mCrmClientHomeDetailVO.getArray());
            // mRecordAdapter.mList =mCrmClientHomeDetailVO.getArray();
        }
        if (mState != 1) 
            footViewIsVisibility();
        mState = 0;
        fragment_sharedfilesd_home_all_refreshlistview.setVisibility(View.VISIBLE);
        fragment_sharedfilesd_home_all_refreshlistview.showHead(activity, false);
        mRecordAdapter.notifyDataSetChanged();
        canClickFlag = true;
    }

    @Override
    public void executeFailure() {
        // TODO Auto-generated method stub
        if (mRecordAdapter.mList != null && mRecordAdapter.mList.size() > 0) {
            loaTextView.setVisibility(View.GONE);
            fragment_sharedfilesd_home_all_refreshlistview.setVisibility(View.VISIBLE);
        }
        else {
            loaTextView.setVisibility(View.VISIBLE);
            fragment_sharedfilesd_home_all_refreshlistview.setVisibility(View.GONE);
        }
        if (mState != 1)
            footViewIsVisibility();
        mState = 0;
        canClickFlag = true;
        fragment_sharedfilesd_home_all_refreshlistview.showHead(activity, false);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

    public boolean getdata() {
        Gson gson = new Gson();
        Map<String, String> map;
        if (mState == 0) {
            map = new HashMap<String, String>();
        } else if (mState == 1) {
            if (mRefresh.equals("")) {
                map = new HashMap<String, String>();
            } else {
                // firstid = crmbussinesApdater.mList.get(0).getBid();
                map = new HashMap<String, String>();
                map.put("firstid", firstid);
            }

        } else {
            lastid = saveLastId;
            map = new HashMap<String, String>();
            map.put("lastid", lastid);
        }
        map.put("bid", bid);
        try {
            String urlStr;
            String jsonUrlStr;
            urlStr = UrlUtil.getUrlByMap1(
                    Constant.CRM_BUSSINES_HOMEINDEXANDDETAILSOFVISITOR, map);
            jsonUrlStr = HttpClientUtil.get(urlStr, Constant.ENCODING).trim();
            mCrmClientHomeDetailVO = gson.fromJson(jsonUrlStr, CrmClientHomeDetailVO.class);
            if (mCrmClientHomeDetailVO.getCode().equals("200")) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    // 请求添加好的拜访记录
    public void GetNewCrmVisitorInfo() {
        mState = 1;
        mRefresh = Constant.FIRSTID;
        if (mRecordAdapter.mList.size() == 0)
            mRefresh = "";
        else
            firstid = mRecordAdapter.mList.get(0).getVid();
        fragment_sharedfilesd_home_all_refreshlistview.showHead(activity, true);
        new ThreadUtil(activity, this).start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // TODO Auto-generated method stub
        if (intent != null) {
            if (intent.hasExtra("vid")) {
                for (int i = 0; i < mRecordAdapter.mList.size(); i++) {
                    if (intent.getStringExtra("vid").equals(mRecordAdapter.mList.get(i).getVid())) {
                        mRecordAdapter.mList.get(i).setComment(intent.getStringExtra("count"));
                        mRecordAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        }
    }
}
