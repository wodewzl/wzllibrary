
package com.bs.bsims.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.bs.bsims.R;

public class CrmActivity extends BaseActivity implements OnClickListener {
    private LinearLayout mCrmMsgLayout02;
    private LinearLayout mCrmMsgLayout03;
    private LinearLayout mCrmMsgLayout04;
    private LinearLayout mCrmMsgLayout05;
    private LinearLayout mCrmMsgLayout06;
    private LinearLayout mCrmMsgLayout07;
    private LinearLayout mCrmMsgLayout08;
    private LinearLayout mCrmMsgLayout09;
    private LinearLayout mCrmMsgLayout10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.crm, mContentLayout);
    }

    @Override
    public boolean getDataResult() {
        return true;
    }

    @Override
    public void updateUi() {

    }

    @Override
    public void initView() {
        mTitleTv.setText("CRM");
        mCrmMsgLayout02 = (LinearLayout) findViewById(R.id.crm_msg_layout_02);
        mCrmMsgLayout03 = (LinearLayout) findViewById(R.id.crm_msg_layout_03);
        mCrmMsgLayout04 = (LinearLayout) findViewById(R.id.crm_msg_layout_04);
        mCrmMsgLayout05 = (LinearLayout) findViewById(R.id.crm_msg_layout_05);
        mCrmMsgLayout06 = (LinearLayout) findViewById(R.id.crm_msg_layout_06);
        mCrmMsgLayout07 = (LinearLayout) findViewById(R.id.crm_msg_layout_07);
        mCrmMsgLayout08 = (LinearLayout) findViewById(R.id.crm_msg_layout_08);
        mCrmMsgLayout09 = (LinearLayout) findViewById(R.id.crm_msg_layout_09);
        mCrmMsgLayout10 = (LinearLayout) findViewById(R.id.crm_msg_layout_10);
    }

    @Override
    public void bindViewsListener() {
        mCrmMsgLayout02.setOnClickListener(this);
        mCrmMsgLayout03.setOnClickListener(this);
        mCrmMsgLayout04.setOnClickListener(this);
        mCrmMsgLayout05.setOnClickListener(this);
        mCrmMsgLayout06.setOnClickListener(this);
        mCrmMsgLayout07.setOnClickListener(this);
        mCrmMsgLayout08.setOnClickListener(this);
        mCrmMsgLayout09.setOnClickListener(this);
        mCrmMsgLayout10.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.crm_msg_layout_02:
                intent.setClass(this, CrmClientListActivity.class);// 客户
                break;
            case R.id.crm_msg_layout_03:
                intent.setClass(this, CrmBusinessHomeListActivity.class);// 销售机会
                break;
            case R.id.crm_msg_layout_04:
                intent.setClass(this, CrmTradeContantHomeListActivity.class);// 合同
                break;
            case R.id.crm_msg_layout_05:
                intent.setClass(this, CrmProductManagementListActivity.class);// 产品
                break;
            case R.id.crm_msg_layout_06:
                intent.setClass(this, CrmSalesTargetListActivity.class);// 销售目标
                break;
            case R.id.crm_msg_layout_07:
                intent.setClass(this, CrmVisitRecordListActivity.class);// 拜访记录
                break;
            case R.id.crm_msg_layout_08:
                intent.setClass(this, CrmApprovalPaymentActivity.class);// 回款审批
                break;
            case R.id.crm_msg_layout_09:
                intent.setClass(this, CrmApprovalTradeContantActivity.class);// 合同列表
                break;
            case R.id.crm_msg_layout_10:
                intent.setClass(this, CrmContactListActivity.class);// 联系人列表
                break;

            default:
                break;
        }
        startActivity(intent);
    }

}
