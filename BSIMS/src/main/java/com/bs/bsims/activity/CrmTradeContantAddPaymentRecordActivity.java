
package com.bs.bsims.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.view.BSSwitchView;
import com.bs.bsims.view.BSSwitchView.OnChangedListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class CrmTradeContantAddPaymentRecordActivity extends BaseActivity implements OnClickListener, OnChangedListener {
    private boolean mCommitFlag = true;
    private String mHid = "";
    private TextView mReceiveDateTv, mPayTypeTv, mIsOpenTitle;
    private BSSwitchView mSwitchView;
    private EditText mMoney, mRemark;
    private String mReceipt = "1";
    private String mPayTypeId;// 支付方式
    private String mRemainMoney;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.crm_tradecontant_add_payment_record, mContentLayout);
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
        mTitleTv.setText("添加收款记录");
        mOkTv.setText("保存");
        mReceiveDateTv = (TextView) findViewById(R.id.receive_date);
        mPayTypeTv = (TextView) findViewById(R.id.pay_type);
        mSwitchView = (BSSwitchView) findViewById(R.id.is_open_status);
        mSwitchView.setCheckState(true);
        mMoney = (EditText) findViewById(R.id.money);
        mRemark = (EditText) findViewById(R.id.remark);
        mIsOpenTitle = (TextView) findViewById(R.id.is_open_title);
        initData();
    }

    public void initData() {
        mHid = this.getIntent().getStringExtra("hid");
        mRemainMoney = this.getIntent().getStringExtra("remain_money");
    }

    @Override
    public void bindViewsListener() {
        mReceiveDateTv.setOnClickListener(this);
        mPayTypeTv.setOnClickListener(this);
        mSwitchView.SetOnChangedListener(this);
        mOkTv.setOnClickListener(this);

        mReceiveDateTv.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (mReceiveDateTv.getText().toString().length() > 0 && DateUtils.getStringToDate(mReceiveDateTv.getText().toString(), "yyyy-MM-dd")
                        > DateUtils.getStringToDate(DateUtils.getCurrentDate(), "yyyy-MM-dd")) {
                    CustomToast.showLongToast(CrmTradeContantAddPaymentRecordActivity.this, "请选择正确时间");
                    mReceiveDateTv.setText("");
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_comm_head_right:
                if (mReceiveDateTv.getText().toString().trim().length() == 0) {
                    CustomToast.showLongToast(this, "请选择收款日期");
                    return;
                }
                if (mMoney.getText().toString().trim().length() == 0) {
                    CustomToast.showLongToast(this, "请填写收款金额");
                    return;
                }
                if (mPayTypeTv.getText().toString().trim().length() == 0) {
                    CustomToast.showLongToast(this, "请选择付款方式");
                    return;
                }

//                if (Double.parseDouble(CommonUtils.isNormalData(mRemainMoney)) < Double.parseDouble(CommonUtils.isNormalData(mMoney.getText().toString()))) {
//                    CustomToast.showLongToast(this, "收款金额要小于合同剩余金额");
//                    return;
//                }

                if (mCommitFlag) {
                    mCommitFlag = false;
                    commit();
                }

                break;

            case R.id.receive_date:
                TextView pannedDate = (TextView) view;
                CommonUtils.initDateView(this, "请选择日期", pannedDate, 2);
                break;
            case R.id.pay_type:
                Intent intent = new Intent(this, AddByPersonCRMActivity.class);
                intent.putExtra("proKey", "7");
                intent.putExtra("typekey", "9");
                intent.putExtra("requst_number", 2019);// 支付方式

                startActivityForResult(intent, 2019);

                break;

            default:
                break;
        }
    }

    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        if (arg1 == 2019) {
            if (arg2 != null) {
                mPayTypeTv.setText(arg2.getStringExtra("name"));
                mPayTypeId = arg2.getStringExtra("id");
            }
        }
    }

    public void commit() {
        CustomDialog.showProgressDialog(this, "正在提交数据...");
        RequestParams params = new RequestParams();

        try {
            params.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            params.put("userid", BSApplication.getInstance().getUserId());
            params.put("contract", mHid);// 合同id

            params.put("planned_date", mReceiveDateTv.getText().toString());// 回款日期
            params.put("money", mMoney.getText().toString());// 回款金额
            params.put("payment_mode", mPayTypeId);// 支付方式
            params.put("receipt", mReceipt);// 是否开票
            params.put("remark", mRemark.getText().toString());// 备注

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        String url = BSApplication.getInstance().getHttpTitle() + Constant.CRM_RECORDE_ADDINFOMSG;
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                mCommitFlag = true;
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(new String(arg2));
                    String str = (String) jsonObject.get("retinfo");
                    String code = (String) jsonObject.get("code");
                    if (Constant.RESULT_CODE.equals(code)) {
                        CustomToast.showShortToast(CrmTradeContantAddPaymentRecordActivity.this, str);
                        // 返回到CrmTradeContantDeatilsHomeTop3Activity，刷新数据
                        CrmTradeContantAddPaymentRecordActivity.this.setResult(520, new Intent());
                        CrmTradeContantAddPaymentRecordActivity.this.finish();
                    }
                    CustomDialog.closeProgressDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void OnChanged(boolean checkState, View v) {
        switch (v.getId()) {
            case R.id.is_open_status:
                if (mSwitchView.getCheckState()) {
                    mReceipt = "1";
                    mIsOpenTitle.setText("是");
                } else {
                    mReceipt = "0";
                    mIsOpenTitle.setText("否");
                }
                break;

            default:
                break;
        }
    }
}
