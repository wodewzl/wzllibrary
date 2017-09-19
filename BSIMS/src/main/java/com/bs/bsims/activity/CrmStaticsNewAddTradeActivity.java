
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.CrmBussinesTranctAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.CrmTranctVo;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSRefreshListView;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
/***
 * 
 * 此类是业务员主页的5个跳转（新增）  mine（自己） datime（2014-06日期） 参数
 * 此类是仪表盘外的商机跳转（新增 放弃）mode（本月 上月 季度） option（新增 放弃 全部） 参数
 * 
 *   合同在仪表盘跳转的地方是折线图，顾不需要在这里跟业务员跳转区分
 * **/
public class CrmStaticsNewAddTradeActivity extends BaseActivity {
    private BSRefreshListView static_new_trade_list;
    private CrmBussinesTranctAdapter crmbussinesApdater;
    private CrmTranctVo crmtranctsbussinesVo;
    private String type;
    public static final String TRADES_EDIT = "trades_edit";// 合同状态改变刷新广播
    private View mFootLayout;
    private TextView mMoreTextView;
    private ProgressBar mProgressBar;
    /** 记录最后一条的id */
    private String saveLastId;
    /** 下拉ID */
    private String lastid;
    // 0为首次,1为上拉刷新 ，2为下拉刷新
    private int mState = 0;
    private Boolean mflag = true;
    private Boolean canClickFlag = true;// 解决连续点“更多”可能会出现的异常
    private String uid;
    private String mMine,mDateTime;
    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.peck_allgroup_listview_refshnodelte, mContentLayout);
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    private boolean getData() {
        try {
            Gson gson = new Gson();

            Map map = null;
            if (mState == 0) {
                map = new HashMap<String, String>();
            } else if (mState == 2) {
                lastid = saveLastId;
                map = new HashMap<String, String>();
                map.put("lastid", lastid);
            }
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put("uid", uid);
            map.put("mine", mMine);
            map.put("datetime", mDateTime);
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.CRM_BUSSINES_HOMEINDEXANDDETAILSOFTRANTS, map);
            crmtranctsbussinesVo = gson.fromJson(jsonStr,
                    CrmTranctVo.class);

            if (Constant.RESULT_CODE.equals(crmtranctsbussinesVo.getCode())) {
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
        static_new_trade_list.setVisibility(View.VISIBLE);
        mContentLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
        if (2 == mState) {
            crmbussinesApdater.updateDataLast(crmtranctsbussinesVo.getArray());
        } else if (0 == mState) {
            crmbussinesApdater.updateData(crmtranctsbussinesVo.getArray());
        }
        if (mState != 1)
            footViewIsVisibility();
        mState = 0;
        static_new_trade_list.onRefreshComplete();
        updateUi();
        canClickFlag = true;
    }

    @Override
    public void executeFailure() {

        mHeadLayout.setVisibility(View.VISIBLE);
        if (crmtranctsbussinesVo == null) {
            super.showNoNetView();
            return;
        }

        mContentLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
        if (null == crmbussinesApdater.mList) {
            super.showNoContentView();
            static_new_trade_list.setVisibility(View.GONE);
            return;
        }
        if (crmbussinesApdater.mList.size() > 0) {
            static_new_trade_list.setVisibility(View.VISIBLE);
        } else {
            super.showNoContentView();
            static_new_trade_list.setVisibility(View.GONE);
        }
        if (mState != 1)
            footViewIsVisibility();
        mState = 0;
        static_new_trade_list.onRefreshComplete();
        canClickFlag = true;
    }

    @Override
    public void updateUi() {
   

    }

    @Override
    public void initView() {
        static_new_trade_list = (BSRefreshListView) findViewById(R.id.fragment_sharedfilesd_home_all_refreshlistview);
        crmbussinesApdater = new CrmBussinesTranctAdapter(CrmStaticsNewAddTradeActivity.this, true);
        static_new_trade_list.setAdapter(crmbussinesApdater);
        if(getIntent().getStringExtra("userid")!=null){
            uid =getIntent().getStringExtra("userid");
        }
        else{
            uid = BSApplication.getInstance().getUserId();
        }
        mMine = null == getIntent().getStringExtra("mine") ? "" : getIntent().getStringExtra("mine");
        //业务员跳转
        if(getIntent().getStringExtra("datetime")!=null){
            mDateTime =getIntent().getStringExtra("datetime");
            mTitleTv.setText("新增合同");
        }
        initFoot();
        registBroadcast();// 注册广播

    }

    // 加载更多数据
    public void initFoot() {
        mFootLayout = LayoutInflater.from(this).inflate(
                R.layout.listview_bottom_more, null);
        mMoreTextView = (TextView) mFootLayout.findViewById(R.id.txt_loading);
        mMoreTextView.setText("更多");
        mProgressBar = (ProgressBar) mFootLayout.findViewById(R.id.progressBar);
        mFootLayout.setVisibility(View.GONE);
        static_new_trade_list.addFooterView(mFootLayout);
    }

    protected void footViewIsVisibility() {
        if (crmtranctsbussinesVo == null) {
            return;
        }
        if (crmtranctsbussinesVo.getCount() == null) {
            return;
        }
        if (Integer.parseInt(crmtranctsbussinesVo.getCount()) <= 15) {
            mFootLayout.setVisibility(View.GONE);
        } else {
            mFootLayout.setVisibility(View.VISIBLE);
            mMoreTextView.setText("更多");
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void bindViewsListener() {

        static_new_trade_list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Intent intent = new Intent();
                CrmTranctVo vo = crmbussinesApdater.mList.get((int) arg3);
                // mCurrentVoIndex = (int) arg3;
                vo.setIsread("1");
                crmbussinesApdater.notifyDataSetChanged();
                String status = vo.getStatus();
                if ("2".equals(vo.getDirection())) {
                    intent.setClass(CrmStaticsNewAddTradeActivity.this, CrmTradeContantDetailsIndexActivity.class);
                    intent.putExtra("hid", crmbussinesApdater.mList.get((int) arg3).getHid());
                    startActivityForResult(intent, 1);
                } else {
                    intent.setClass(CrmStaticsNewAddTradeActivity.this, CrmTradeContantDeatilsHomeTop3Activity.class);
                    intent.putExtra("hid", crmbussinesApdater.mList.get((int) arg3).getHid());
                    intent.putExtra("title", crmbussinesApdater.mList.get((int) arg3).getTitle());
                    intent.putExtra("money", crmbussinesApdater.mList.get((int) arg3).getMoney());
                    intent.putExtra("payment", crmbussinesApdater.mList.get((int) arg3).getPayment());
                    intent.putExtra("cname", crmbussinesApdater.mList.get((int) arg3).getCname());
                    intent.putExtra("statusName", crmbussinesApdater.mList.get((int) arg3).getStatusName());
                    intent.putExtra("receiptMoney", crmbussinesApdater.mList.get((int) arg3).getReceipt_money());
                    intent.putExtra("changeStatus", crmbussinesApdater.mList.get((int) arg3).getChangeStatus());
                    intent.putExtra("changeStatusName", crmbussinesApdater.mList.get((int) arg3).getChangeStatusName());
                    intent.putExtra("status", crmbussinesApdater.mList.get((int) arg3).getStatus());
                    startActivity(intent);
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
                    mState = 2;
                    saveLastId = crmbussinesApdater.mList
                            .get(crmbussinesApdater.mList.size() - 1).getHid();
                    new ThreadUtil(CrmStaticsNewAddTradeActivity.this, CrmStaticsNewAddTradeActivity.this)
                            .start();
                }
            }
        });

    }

    public void registBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(TRADES_EDIT);
        this.registerReceiver(msgBroadcast, filter);
    }

    private void unRegistExitReceiver() {
        this.unregisterReceiver(msgBroadcast);
    }

    private BroadcastReceiver msgBroadcast = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (TRADES_EDIT.equals(intent.getAction())) {
                String hid = intent.getStringExtra("hid");
                String status = intent.getStringExtra("status");
                String statusName = intent.getStringExtra("statusName");
                for (int i = 0; i < crmbussinesApdater.mList.size(); i++) {
                    if (hid.equals(crmbussinesApdater.mList.get(i).getHid())) {
                        crmbussinesApdater.mList.get(i).setStatus(status);
                        crmbussinesApdater.mList.get(i).setStatusName(statusName);
                        break;
                    }
                }
                crmbussinesApdater.notifyDataSetChanged();
            }
        }
    };

    protected void onDestroy() {
        super.onDestroy();
        unRegistExitReceiver();
    };

}
