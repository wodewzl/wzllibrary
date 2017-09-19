
package com.bs.bsims.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.CrmClientHomeAdapter;
import com.bs.bsims.adapter.HeadAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.CrmClientDetailVO;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSGridView;
import com.bs.bsims.view.BSRefreshListView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CrmClientDetailActivity extends BaseActivity implements OnClickListener {
    public static final String DETAIL_EDIT = "detail_edit";
    private BSRefreshListView mListView;
    private CrmClientHomeAdapter mAdapter;
    private TextView mCheckedTv01, mCheckedTv02, mCheckedTv03, mCheckedTv04;
    private LinearLayout mTitleDetailLayout;
    private CrmClientDetailVO mClientDetailVO;

    // 知会人，审批人
    private BSGridView mApproverGv, mInformGv;
    private HeadAdapter mApproverAdapter, mInformAdapter;
    private TextView mApproverTv, mApprovalGoTv, mInformTv, mInformGoTv, mCancel, mSure;
    private LinearLayout mApproverLayout, mInformLayout;

    private TextView mCompanyName, mIndustry, mLevel, mWebsite, mRemark, mSource, mCompayAddress, mAbbreviation;
    private String mCid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.crm_client_detail, mContentLayout);
    }

    @Override
    public void initView() {
        mOkTv.setText("编辑");
        mTitleTv.setText("客户详情");

        mCompanyName = (TextView) findViewById(R.id.client_name);
        mIndustry = (TextView) findViewById(R.id.client_idustry);
        mLevel = (TextView) findViewById(R.id.client_level);
        mWebsite = (TextView) findViewById(R.id.client_website);
        mSource = (TextView) findViewById(R.id.client_source);
        mRemark = (TextView) findViewById(R.id.remark);
        mCompayAddress = (TextView) findViewById(R.id.client_address);
        mAbbreviation = (TextView) findViewById(R.id.client_abbreviation);

        mApproverGv = (BSGridView) findViewById(R.id.approver_gv);
        mInformGv = (BSGridView) findViewById(R.id.inform_gv);
        mApproverAdapter = new HeadAdapter(this, false);
        mInformAdapter = new HeadAdapter(this, false, true);
        mApproverGv.setAdapter(mApproverAdapter);
        mInformGv.setAdapter(mInformAdapter);
        mApproverTv = (TextView) findViewById(R.id.approver_tv);
        mInformTv = (TextView) findViewById(R.id.inform_people_tv);
        mInformTv.setText("联合跟进人");
        mApproverTv.setText("负责人");
        mApproverLayout = (LinearLayout) findViewById(R.id.approver_layout);
        mInformLayout = (LinearLayout) findViewById(R.id.inform_people_layout);

        initData();
        registBroadcast();
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    public boolean getData() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put("cid", mCid);
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.CRM_CLIENT_DETAIL, map);
            mClientDetailVO = gson.fromJson(jsonStr, CrmClientDetailVO.class);
            if (Constant.RESULT_CODE.equals(mClientDetailVO.getCode())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {

        }
    }

    @Override
    public void updateUi() {
        CrmClientDetailVO detailVO = mClientDetailVO.getInfo();
        mCompanyName.setText(detailVO.getName());

        if (CommonUtils.isNormalString(detailVO.getAbbreviation())) {
            mAbbreviation.setText(detailVO.getAbbreviation());
        } else {
            mAbbreviation.setText(null);
        }
        if (CommonUtils.isNormalString(detailVO.getIndustry()))
            mIndustry.setText(detailVO.getIndustry());
        if (CommonUtils.isNormalString(detailVO.getLevel()))
            mLevel.setText(detailVO.getLevel());
        if (CommonUtils.isNormalString(detailVO.getWebsite())) {
            mWebsite.setText(detailVO.getWebsite());
        } else {
            mWebsite.setText(null);
        }
        if (CommonUtils.isNormalString(detailVO.getSource()))
            mSource.setText(detailVO.getSource());
        if (CommonUtils.isNormalString(detailVO.getAddress()))
            mCompayAddress.setText(detailVO.getAddress());
        if (CommonUtils.isNormalString(detailVO.getRemark())) {
            mRemark.setText(detailVO.getRemark());
        } else {
            mRemark.setText(null);
        }

        if (this.getIntent().hasExtra("ispub")) {
            mInformTv.setVisibility(View.GONE);
            mInformLayout.setVisibility(View.GONE);
            mApproverTv.setVisibility(View.GONE);
            mApproverLayout.setVisibility(View.GONE);
            return;
        }

        List<EmployeeVO> list = new ArrayList<EmployeeVO>();
        EmployeeVO employeeVO = new EmployeeVO();
        employeeVO.setHeadpic(detailVO.getHeadpic());
        employeeVO.setFullname(detailVO.getFullname());
        employeeVO.setUserid(detailVO.getUserid());
        list.add(employeeVO);
        mApproverAdapter.updateData(list);
        mApproverTv.setVisibility(View.VISIBLE);
        mApproverLayout.setVisibility(View.VISIBLE);
        if (detailVO.getFollow() != null) {
            mInformTv.setVisibility(View.VISIBLE);
            mInformLayout.setVisibility(View.VISIBLE);
            mInformAdapter.updateData(detailVO.getFollow());
        } else {
            mInformTv.setVisibility(View.GONE);
            mInformLayout.setVisibility(View.GONE);
        }

    }

    public void initData() {
        Intent intent = this.getIntent();
        if (this.getIntent().getStringExtra("cid") != null) {
            mCid = intent.getStringExtra("cid");
        }
        if (this.getIntent().getStringExtra("crmEdit") != null) {
            if ("1".equals(this.getIntent().getStringExtra("crmEdit"))) {
                mOkTv.setVisibility(View.VISIBLE);
            } else {
                mOkTv.setVisibility(View.GONE);
            }
        }

        if (this.getIntent().hasExtra("publish_sp")) {
            mOkTv.setVisibility(View.VISIBLE);
        }
        // 从合同主页跳转过来的
        if (this.getIntent().hasExtra("is_from_trade")) {
            mOkTv.setVisibility(View.GONE);
        }

        // 公海客户
        if (this.getIntent().hasExtra("ispub")) {
            mOkTv.setVisibility(View.GONE);
        }

    }

    public void bindViewsListener() {
        mOkTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_comm_head_right:
                if (mClientDetailVO == null)
                    return;

                Intent intent = new Intent();
                intent.putExtra("edit", true);
                intent.putExtra("name", mCompanyName.getText().toString());
                intent.putExtra("abbreviation", mAbbreviation.getText().toString());
                intent.putExtra("industry", mIndustry.getText().toString());
                intent.putExtra("industryId", mClientDetailVO.getInfo().getIndustryId());
                intent.putExtra("source", mSource.getText().toString());
                intent.putExtra("sourceId", mClientDetailVO.getInfo().getSourceId());
                intent.putExtra("level", mLevel.getText().toString());
                intent.putExtra("levelId", mClientDetailVO.getInfo().getLevelId());
                intent.putExtra("address", mCompayAddress.getText().toString());
                intent.putExtra("website", mWebsite.getText().toString());
                intent.putExtra("remark", mRemark.getText().toString());
                intent.putExtra("abbreviation", mAbbreviation.getText().toString());
                intent.putExtra("cid", mCid);
                intent.setClass(this, CrmClientAddActivity.class);
                this.startActivity(intent);
                break;
            default:
                break;
        }
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
                new ThreadUtil(CrmClientDetailActivity.this, CrmClientDetailActivity.this).start();
            }
        }
    };

    protected void onDestroy() {
        super.onDestroy();
        unRegistExitReceiver();
    };
}
