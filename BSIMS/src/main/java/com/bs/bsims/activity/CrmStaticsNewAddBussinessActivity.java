
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
import com.bs.bsims.adapter.CrmBusinessHomeListAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.CrmBussinesList;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSIndexEditText;
import com.bs.bsims.view.BSRefreshListView;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/***
 * 此类是业务员主页的5个跳转（新增） mine（自己） datime（2014-06日期） 参数 此类是仪表盘外的商机跳转（新增 放弃）mode（本月 上月 季度） option（新增 放弃
 * 全部） 参数
 **/
public class CrmStaticsNewAddBussinessActivity extends BaseActivity {
    private String type, mMine;
    private BSIndexEditText edit_single_search;
    private LinearLayout one_title;
    private BSRefreshListView crm_business_indexlistview;
    private CrmBusinessHomeListAdapter crmbussinesApdater;
    private CrmBussinesList crmbulist;
    private int itmepostion = -1;
    private View mFootLayout;
    private TextView mMoreTextView, loadingfile1;
    private ProgressBar mProgressBar;
    // 0为首次,1为上拉刷新 ，2为下拉刷新
    private int mState = 0;
    /** 记录最后一条的id */
    private String saveLastId;
    /** 下拉ID */
    private String lastid;

    private String STATISTICS_NEW_BUSSINESS_DETAIL = "statistics_new_bussiness_detail";
    private Boolean canClickFlag = true;// 解决连续点“更多”可能会出现的异常

    private TextView mNoReadTv;
    private String uid, did;
    private String isAdd, mDateTime;// 更改成月份切换，

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.crmbussines_indexlist, mContentLayout);
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    private boolean getData() {
        try {
            Gson gson = new Gson();

            Map<String, String> map = null;
            if (mState == 0) {
                map = new HashMap<String, String>();
            } else if (mState == 2) {
                lastid = saveLastId;// mEXTSharedfilesdHomeMyUpdateAdapter.mList.get(mEXTSharedfilesdHomeMyUpdateAdapter.mList.size()
                                    // - 1).getSharedid();
                map = new HashMap<String, String>();
                map.put("lastid", lastid);
            }

            // HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put("uid", uid);
            map.put("did", did);
            map.put("mode", type);
            map.put("mine", mMine);
            map.put("option", isAdd);
            map.put("datetime", mDateTime);
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.CRM_BUSSINES_GETLIST, map);
            crmbulist = gson.fromJson(jsonStr, CrmBussinesList.class);
            if (Constant.RESULT_CODE.equals(crmbulist.getCode())) {
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
        crm_business_indexlistview.setVisibility(View.VISIBLE);
        mContentLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
        if (2 == mState) {
            crmbussinesApdater.updateDataLast(crmbulist.getArray());
        } else if (0 == mState) {
            crmbussinesApdater.updateData(crmbulist.getArray());
        }
        if (mState != 1)
            footViewIsVisibility();
        mState = 0;
        crm_business_indexlistview.onRefreshComplete();
        canClickFlag = true;
    }

    protected void footViewIsVisibility() {
        if (crmbulist == null) {
            return;
        }
        if (crmbulist.getCount() == null) {
            return;
        }
        if (Integer.parseInt(crmbulist.getCount()) <= 15) {
            mFootLayout.setVisibility(View.GONE);
            // listView.removeFooterView(mFootLayout);
        } else {
            mFootLayout.setVisibility(View.VISIBLE);
            mMoreTextView.setText("更多");
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void executeFailure() {
        // 网络异常
        if (null == crmbulist) {
            super.showNoNetView();
            return;
        }

        mContentLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
        if (null == crmbussinesApdater.mList) {
            crm_business_indexlistview.setVisibility(View.GONE);
            loadingfile1.setVisibility(View.VISIBLE);
            return;
        }
        if (crmbussinesApdater.mList.size() > 0) {
            crm_business_indexlistview.setVisibility(View.VISIBLE);
            loadingfile1.setVisibility(View.GONE);
        } else {
            crm_business_indexlistview.setVisibility(View.GONE);
            loadingfile1.setVisibility(View.VISIBLE);
        }
        if (mState != 1)
            footViewIsVisibility();
        mState = 0;
        crm_business_indexlistview.onRefreshComplete();
        canClickFlag = true;
    }

    @Override
    public void updateUi() {
        // crmbussinesApdater.updateData(crmbulist.getArray());
    }

    @Override
    public void initView() {
        this.findViewById(R.id.search_title_layout).setVisibility(View.GONE);

        mMine = null == getIntent().getStringExtra("mine") ? "" : getIntent().getStringExtra("mine");
        if (getIntent().getStringExtra("userid") != null) {
            uid = getIntent().getStringExtra("userid");
        }
        if (getIntent().getStringExtra("did") != null) {
            did = getIntent().getStringExtra("did");
        }
        // 业务员跳转
        if (getIntent().getStringExtra("datetime") != null) {
            mDateTime = getIntent().getStringExtra("datetime");
            mTitleTv.setText("新增商机");
        }
        else {
            type = getIntent().getStringExtra("type");
            isAdd = getIntent().getStringExtra("option");// 是否是新增或者放弃
            if ("1".equals(type) && isAdd.equals("1")) {
                mTitleTv.setText("上月新增商机");
            } else if ("2".equals(type) && isAdd.equals("1")) {
                mTitleTv.setText("本月新增商机");
            } else if ("3".equals(type) && isAdd.equals("1")) {
                mTitleTv.setText("本季度新增商机");
            }
            else if ("1".equals(type) && isAdd.equals("2")) {
                mTitleTv.setText("上月放弃商机");
            }
            else if ("2".equals(type) && isAdd.equals("2")) {
                mTitleTv.setText("本月放弃商机");
            }
            else if ("3".equals(type) && isAdd.equals("2")) {
                mTitleTv.setText("本季度放弃商机");
            }
        }
        one_title = (LinearLayout) findViewById(R.id.one_title);
        edit_single_search = (BSIndexEditText) findViewById(R.id.edit_single_search);
        one_title.setVisibility(View.GONE);
        edit_single_search.setVisibility(View.GONE);
        crm_business_indexlistview = (BSRefreshListView) findViewById(R.id.crm_business_indexlistview);
        crmbussinesApdater = new CrmBusinessHomeListAdapter(CrmStaticsNewAddBussinessActivity.this);
        crm_business_indexlistview.setAdapter(crmbussinesApdater);
        loadingfile1 = (TextView) findViewById(R.id.loadingfile1);
        mNoReadTv = (TextView) findViewById(R.id.no_read_tv);
        mNoReadTv.setVisibility(View.GONE);
        initFoot();
        registBroadcast();// 注册广播刷新数据
    }

    // 加载更多数据
    public void initFoot() {
        mFootLayout = LayoutInflater.from(this).inflate(
                R.layout.listview_bottom_more, null);
        mMoreTextView = (TextView) mFootLayout.findViewById(R.id.txt_loading);
        mMoreTextView.setText("更多");
        mProgressBar = (ProgressBar) mFootLayout.findViewById(R.id.progressBar);
        mFootLayout.setVisibility(View.GONE);
        crm_business_indexlistview.addFooterView(mFootLayout);
    }

    @Override
    public void bindViewsListener() {
        crm_business_indexlistview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                crmbussinesApdater.mList.get((int) arg3).setIsread("1");
                crmbussinesApdater.notifyDataSetChanged();
                Intent i = new Intent(CrmStaticsNewAddBussinessActivity.this, CrmBusinessHomeIndexOneInfo.class);
                i.putExtra("bid", crmbussinesApdater.mList.get((int) arg3).getBid());
                // i.putExtra("from_static_add_bussiness", "1");// 回来更改状态
                i.putExtra("stateUtilthread", "4");
                itmepostion = arg2 - 1;
                startActivityForResult(i, 520);
                // CrmStaticsNewAddBussinessActivity.this.finish();
                // SharedPreferences myPreference = getSharedPreferences("static_add_bussiness",
                // Activity.MODE_PRIVATE);
                // SharedPreferences.Editor editor = myPreference.edit();
                // editor.putString("from_static_add_bussiness", "1");
                // editor.commit();
            }

        });

        mFootLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (canClickFlag) {
                    canClickFlag = false;
                    mMoreTextView.setText("正在加载...");
                    mProgressBar.setVisibility(View.VISIBLE);
                    mState = 2;
                    // mRefresh = Constant.LASTID;
                    saveLastId = crmbussinesApdater.mList
                            .get(crmbussinesApdater.mList.size() - 1).getBid();
                    new ThreadUtil(CrmStaticsNewAddBussinessActivity.this, CrmStaticsNewAddBussinessActivity.this)
                            .start();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent intent) {
        super.onActivityResult(arg0, arg1, intent);
        if (intent == null)
            return;

        // if (intent != null && crmbussinesApdater.mList != null && crmbussinesApdater.mList.size()
        // > 0) {
        // for (int i = 0; i < crmbussinesApdater.mList.size(); i++) {
        // if (i == itmepostion) {
        // crmbussinesApdater.mList.get(itmepostion).setStatus(intent.getStringExtra("bidstate"));
        // crmbussinesApdater.mList.get(itmepostion).setStatusName(intent.getStringExtra("bidstateName"));
        // crmbussinesApdater.mList.get(itmepostion).setFullname(intent.getStringExtra("bid_fuzerenname"));
        // crmbussinesApdater.mList.get(itmepostion).setBname(intent.getStringExtra("bid_name"));
        // crmbussinesApdater.mList.get(itmepostion).setMoney(intent.getStringExtra("bid_money"));
        // break;
        // }
        // }
        // crmbussinesApdater.notifyDataSetChanged();
        // }

    }

    public void registBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(STATISTICS_NEW_BUSSINESS_DETAIL);
        this.registerReceiver(msgBroadcast, filter);
    }

    private void unRegistExitReceiver() {
        this.unregisterReceiver(msgBroadcast);
    }

    private BroadcastReceiver msgBroadcast = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (STATISTICS_NEW_BUSSINESS_DETAIL.equals(intent.getAction())) {
                if (null != intent.getStringExtra("bid_fuzerenname")
                        && null != intent.getStringExtra("bidstate")
                        && null != intent.getStringExtra("bidstateName") && -1 != itmepostion) {
                    if (intent != null && crmbussinesApdater.mList != null && crmbussinesApdater.mList.size() > 0) {
                        for (int i = 0; i < crmbussinesApdater.mList.size(); i++) {
                            if (i == itmepostion) {
                                crmbussinesApdater.mList.get(itmepostion).setStatus(intent.getStringExtra("bidstate"));
                                crmbussinesApdater.mList.get(itmepostion).setStatusName(intent.getStringExtra("bidstateName"));
                                crmbussinesApdater.mList.get(itmepostion).setFullname(intent.getStringExtra("bid_fuzerenname"));
                                crmbussinesApdater.mList.get(itmepostion).setBname(intent.getStringExtra("bid_name"));
                                crmbussinesApdater.mList.get(itmepostion).setMoney(intent.getStringExtra("bid_money"));
                                break;
                            }
                        }
                        crmbussinesApdater.notifyDataSetChanged();
                    }
                }
            }
        }
    };

    protected void onDestroy() {
        super.onDestroy();
        unRegistExitReceiver();
    };
}
