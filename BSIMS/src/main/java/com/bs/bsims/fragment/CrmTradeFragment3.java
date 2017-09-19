
package com.bs.bsims.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.CrmClientTrendAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.interfaces.UpdateCallback;
import com.bs.bsims.model.CrmClientTrendVO;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSRefreshListView;
import com.bs.bsims.view.BSRefreshListView.OnRefreshListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("ValidFragment")
public class CrmTradeFragment3 extends BaseFragment implements UpdateCallback {
    private BSRefreshListView mRefreshListView;
    private CrmClientTrendAdapter mCrmListAdapter;

    private View mFootLayout;
    private TextView mMoreTextView;
    private ProgressBar mProgressBar;

    private int mState = 0; // 0为首次,1为上拉刷新 ，2为下拉刷新
    private String mFristid, mLastid;
    private String mHid;
    private CrmClientTrendVO mClientTrendVO;
    private Activity mActivity;
    // 为了只让mRefreshListView.setAdapter(mCrmListAdapter)只运行一遍
    private boolean flag = true;

    public CrmTradeFragment3(Activity activity, String hid) {
        this.mActivity = activity;
        this.mHid = hid;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.crm_trade_dongtai_view, container, false);
        // updateData();
        // getData();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        bindViewListener();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDisplay();
    }

    private void initDisplay() {
        if (flag) {
            mRefreshListView.setAdapter(mCrmListAdapter);
            flag = false;
        }
        new ThreadUtil(mActivity, this).start();
    }

    public void initViews(View view) {
        mRefreshListView = (BSRefreshListView) view.findViewById(R.id.lv_refresh);
        mCrmListAdapter = new CrmClientTrendAdapter(mActivity);
        initFoot();
    }

    // 加载更多数据
    public void initFoot() {
        mFootLayout = LayoutInflater.from(mActivity).inflate(R.layout.listview_bottom_more, null);
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
            map.put("hid", mHid);

            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.CRM_TRADE_DONGTAI, map);

            // String urlStr1 = UrlUtil.getUrlByMap1(Constant.CRM_RECORDE_DETAILS,
            // map);
            // String jsonUrlStr1;
            // jsonUrlStr1 = HttpClientUtil.get(urlStr1, Constant.ENCODING).trim();

            mClientTrendVO = gson.fromJson(jsonStrList, CrmClientTrendVO.class);
            if (Constant.RESULT_CODE.equals(mClientTrendVO.getCode())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void executeSuccess() {

        if (1 == mState) {
            mCrmListAdapter.updateDataFrist(mClientTrendVO.getArray());
        } else if (2 == mState) {
            mCrmListAdapter.updateDataLast(mClientTrendVO.getArray());
        } else {
            mCrmListAdapter.updateData(mClientTrendVO.getArray());
        }

        mCrmListAdapter.notifyDataSetChanged();
        mRefreshListView.onRefreshComplete();
        if ("".equals(mFristid) || mState != 1) {
            footViewIsVisibility();
        }
        mState = 0;
    }

    public void executeFailure() {

        // 列表展示的时候不能调用父类
        // super.isRequestFinish();
        mCrmListAdapter.notifyDataSetChanged();
        mRefreshListView.onRefreshComplete();
        footViewIsVisibility();

        // 不适合只隐藏列表，适合隐藏整个布局;
        if (mClientTrendVO == null) {
            // showNoNetView();
        } else {
            if (mState == 0) {
                mCrmListAdapter.updateData(new ArrayList<CrmClientTrendVO>());
                mFootLayout.setVisibility(View.GONE);
            }
        }
        mState = 0;
    }

    public void footViewIsVisibility() {
        if (mClientTrendVO == null || mClientTrendVO.getCount() == null) {
            return;
        }
        if (Integer.parseInt(mClientTrendVO.getCount()) <= 15) {
            mFootLayout.setVisibility(View.GONE);
        } else {
            mFootLayout.setVisibility(View.VISIBLE);
            mMoreTextView.setText("更多");
            mProgressBar.setVisibility(View.GONE);
        }

    }

    public void updateData() {

    }

    public void bindViewListener() {
        mRefreshListView.setonRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mCrmListAdapter.mList.size() > 0) {
                    match(1, mCrmListAdapter.mList.get(0).getTlid());
                } else {
                    mFristid = "";
                    match(1, "");
                }
            }
        });
        mFootLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mMoreTextView.setText("正在加载...");
                mProgressBar.setVisibility(View.VISIBLE);
                match(2, mCrmListAdapter.mList.get(mCrmListAdapter.mList.size() - 1).getTlid());
            }
        });

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
        initDisplay();
    }

    @Override
    public String getFragmentName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean execute() {
        // TODO Auto-generated method stub
        return getData();
    }

}
