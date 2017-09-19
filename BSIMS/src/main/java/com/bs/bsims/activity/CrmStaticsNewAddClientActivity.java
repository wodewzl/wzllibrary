
package com.bs.bsims.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.CrmListAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.CrmListVO;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSRefreshListView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
/***
 * 
 * 此类是业务员主页的5个跳转（新增）  mine（自己） datime（2014-06日期） 参数
 * 此类是仪表盘外的商机跳转（新增 放弃）mode（本月 上月 季度） option（新增 放弃 全部） 参数
 * 
 * 
 * **/
public class CrmStaticsNewAddClientActivity extends BaseActivity {
    private String type;
    private CrmListVO mCrmListVO;
    private LinearLayout edit_layout, client_title_layout;
    private BSRefreshListView mRefreshListView;
    private CrmListAdapter mCrmListAdapter;
    private CrmListVO mCurrentClickVO;
    public static final String DETAIL_EDIT = "detail_edit";
    // 下拉上拉刷新部分
    private View mFootLayout;
    private TextView mMoreTextView;
    private ProgressBar mProgressBar;
    private int mState = 0; // 0为首次,1为上拉刷新 ，2为下拉刷新
    private String mLastid;
    private Boolean canClickFlag = true;// 解决连续点“更多”可能会出现的异常
    private String uid,did;
    private String isAdd, mMine,mDateTime; 
    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.crm_client_list, mContentLayout);
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
            map.put("uid", uid);
            map.put("did", did);
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put("mode", type);
            map.put("mine", mMine);
            map.put("option", isAdd);
            map.put("datetime", mDateTime);
            map.put("lastid", mLastid);
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.CRM_CLIENT_LIST, map);
            mCrmListVO = gson.fromJson(jsonStr, CrmListVO.class);
            if (Constant.RESULT_CODE.equals(mCrmListVO.getCode())) {
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
            mCrmListAdapter.updateDataLast(mCrmListVO.getArray());
        } else if (0 == mState) {
            mCrmListAdapter.updateData(mCrmListVO.getArray());
        }

        mCrmListAdapter.notifyDataSetChanged();
        mRefreshListView.onRefreshComplete();

        if (mState != 1) {
            footViewIsVisibility();
        }
        mState = 0;
        canClickFlag = true;
    }

    public void footViewIsVisibility() {
        if (mCrmListVO == null || mCrmListVO.getCount() == null) {
            return;
        }
        if (Integer.parseInt(mCrmListVO.getCount()) <= 15) {
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
        mCrmListAdapter.notifyDataSetChanged();
        mRefreshListView.onRefreshComplete();
        footViewIsVisibility();

        // 不适合只隐藏列表，适合隐藏整个布局;
        if (mCrmListVO == null) {
            super.showNoNetView();
        } else {
            if (mState == 0) {
                mCrmListAdapter.updateData(new ArrayList<CrmListVO>());
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
        edit_layout = (LinearLayout) findViewById(R.id.edit_layout);
        client_title_layout = (LinearLayout) findViewById(R.id.client_title_layout);
        edit_layout.setVisibility(View.GONE);
        client_title_layout.setVisibility(View.GONE);
        mRefreshListView = (BSRefreshListView) findViewById(R.id.lv_refresh);
        mCrmListAdapter = new CrmListAdapter(this);
        mRefreshListView.setAdapter(mCrmListAdapter);
        mMine = null == getIntent().getStringExtra("mine") ? "" : getIntent().getStringExtra("mine");
        if (getIntent().getStringExtra("userid") != null) {
            uid = getIntent().getStringExtra("userid");
        }
         if(getIntent().getStringExtra("did") != null){
            did =getIntent().getStringExtra("did");
        }
        //业务员跳转
        if(getIntent().getStringExtra("datetime")!=null){
            mDateTime =getIntent().getStringExtra("datetime");
            mTitleTv.setText("新增客户");
        }
        else{
            type = getIntent().getStringExtra("type");
            isAdd = getIntent().getStringExtra("option");// 是否是新增或者放弃
            if ("1".equals(type) && isAdd.equals("1")) {
                mTitleTv.setText("上月新增客户");
            } else if ("2".equals(type) && isAdd.equals("1")) {
                mTitleTv.setText("本月新增客户");
            } else if ("3".equals(type) && isAdd.equals("1")) {
                mTitleTv.setText("本季度新增客户");
            }
            else if ("1".equals(type) && isAdd.equals("2")) {
                mTitleTv.setText("上月放弃客户");
            }
            else if ("2".equals(type) && isAdd.equals("2")) {
                mTitleTv.setText("本月放弃客户");
            }
            else if ("3".equals(type) && isAdd.equals("2")) {
                mTitleTv.setText("本季度放弃客户");
            }
        }
        initFoot();
        registBroadcast();// 注册广播刷新数据
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
                if (mCrmListAdapter.mList.size() > 0) {
                    mCurrentClickVO = mCrmListAdapter.mList.get((int) arg3);
                    mCurrentClickVO.setIsread("1");
                    mCrmListAdapter.notifyDataSetChanged();
                    Intent intent = new Intent();
                    intent.putExtra("cid", mCurrentClickVO.getCid());
                    intent.putExtra("address", mCurrentClickVO.getAddress());
                    intent.putExtra("name", mCurrentClickVO.getName());
                    intent.putExtra("level", mCurrentClickVO.getLevel());
                    intent.putExtra("souce", mCurrentClickVO.getSource());
                    intent.putExtra("oldfullname", mCurrentClickVO.getOldfullname());
                    intent.putExtra("fullname", mCurrentClickVO.getFullname());
                    intent.putExtra("crmEdit", mCurrentClickVO.getCrmEdit());
                    if ("1".equals(mCurrentClickVO.getIsPub())) {
                        intent.setClass(CrmStaticsNewAddClientActivity.this, CrmHighseasClientsHomeActivity.class);
                        startActivityForResult(intent, 1);
                    } else {
                        intent.putExtra("visit_count", mCurrentClickVO.getVisitCount());
                        intent.putExtra("contacts_count", mCurrentClickVO.getContactsCount());
                        intent.putExtra("business_count", mCurrentClickVO.getBusinessCount());
                        intent.putExtra("contract_count", mCurrentClickVO.getContractCount());
                        intent.setClass(CrmStaticsNewAddClientActivity.this, CrmClientHomeActivity.class);
                        startActivityForResult(intent, 1);
                    }
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
                    match(6, mCrmListAdapter.mList.get(mCrmListAdapter.mList.size() - 1).getCid());
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

    public void registBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(DETAIL_EDIT);
        this.registerReceiver(msgBroadcast, filter);
    }

    private void unRegistExitReceiver() {
        this.unregisterReceiver(msgBroadcast);
    }

    private BroadcastReceiver msgBroadcast = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (DETAIL_EDIT.equals(intent.getAction())) {
                if (intent.getStringExtra("address") != null) {
                    mCurrentClickVO.setAddress(intent.getStringExtra("address"));
                }
                if (intent.getStringExtra("name") != null) {
                    mCurrentClickVO.setName(intent.getStringExtra("name"));
                }

                if (intent.getStringExtra("level") != null) {
                    mCurrentClickVO.setLevel(intent.getStringExtra("level"));
                }

                mCrmListAdapter.notifyDataSetChanged();
            }
        }
    };

    protected void onDestroy() {
        super.onDestroy();
        unRegistExitReceiver();
    };

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent intent) {
        super.onActivityResult(arg0, arg1, intent);
        if (intent == null)
            return;

        if (intent != null && mCurrentClickVO != null && mCrmListAdapter.mList.contains(mCurrentClickVO)) {
            if (intent.hasExtra("cid")) {
                for (int i = 0; i < mCrmListAdapter.mList.size(); i++) {
                    if (intent.getStringExtra("cid").equals(mCrmListAdapter.mList.get(i).getCid())) {
                        mCrmListAdapter.mList.get(i).setContactsCount(intent.getStringExtra("contactsCount"));
                        mCrmListAdapter.mList.get(i).setBusinessCount(intent.getStringExtra("bussnessCount"));
                        mCrmListAdapter.mList.get(i).setContractCount(intent.getStringExtra("tradeCount"));
                        mCrmListAdapter.mList.get(i).setVisitCount(intent.getStringExtra("visitorCount"));
                        break;
                    }
                }

            }
            mCrmListAdapter.notifyDataSetChanged();

        }

    }

}
