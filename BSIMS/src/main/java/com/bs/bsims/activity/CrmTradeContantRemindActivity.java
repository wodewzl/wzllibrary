
package com.bs.bsims.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.DateUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CrmTradeContantRemindActivity extends BaseActivity implements OnClickListener {
    // private TextView mRemindTimeTv;
    // private EditText mRemarkTv;
    // private String mHid;
    // private boolean mCommitFlag = true;
    //
    // @Override
    // public void baseSetContentView() {
    // View.inflate(this, R.layout.crm_tradecontant_remind, mContentLayout);
    // }
    //
    // @Override
    // public boolean getDataResult() {
    // return true;
    // }
    //
    // @Override
    // public void updateUi() {
    //
    // }
    //
    // @Override
    // public void initView() {
    // mRemindTimeTv = (TextView) findViewById(R.id.remind_time_tv);
    // mRemarkTv = (EditText) findViewById(R.id.remark);
    // mHid = this.getIntent().getStringExtra("hid");
    // mOkTv.setVisibility(View.VISIBLE);
    // mOkTv.setText("确定");
    // mTitleTv.setText("合同提醒时间");
    // }
    //
    // @Override
    // public void bindViewsListener() {
    // mRemindTimeTv.setOnClickListener(this);
    // mOkTv.setOnClickListener(this);
    //
    // mRemindTimeTv.addTextChangedListener(new TextWatcher() {
    //
    // @Override
    // public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
    //
    // }
    //
    // @Override
    // public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
    //
    // }
    //
    // @Override
    // public void afterTextChanged(Editable arg0) {
    // if (mRemindTimeTv.getText().toString().length() > 0 &&
    // DateUtils.getStringToDate(mRemindTimeTv.getText().toString(), "yyyy-MM-dd HH:mm")
    // < DateUtils.getStringToDate(DateUtils.getCurrentDate(), "yyyy-MM-dd HH:mm")) {
    // CustomToast.showLongToast(CrmTradeContantRemindActivity.this, "请选择有效时间");
    // mRemindTimeTv.setText("");
    // }
    // }
    // });
    // }
    //
    // @Override
    // public void onClick(View v) {
    // switch (v.getId()) {
    // case R.id.remind_time_tv:
    // CommonUtils.initDateView(this, "请选择日期", mRemindTimeTv, 1);
    // break;
    //
    // case R.id.txt_comm_head_right:
    // if (mRemindTimeTv.toString().length() == 0)
    // return;
    // if (mCommitFlag) {
    // mCommitFlag = false;
    // commit();
    // }
    //
    // break;
    //
    // default:
    // break;
    // }
    //
    // }
    //
    // public void commit() {
    // CustomDialog.showProgressDialog(this, "正在提交数据...");
    // RequestParams params = new RequestParams();
    //
    // try {
    // params.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
    // params.put("userid", BSApplication.getInstance().getUserId());
    // params.put("hid", mHid);// 合同id
    //
    // params.put("remindtime", mRemindTimeTv.getText().toString());// 回款日期
    // params.put("content", mRemarkTv.getText().toString());// 备注
    //
    // } catch (Exception e1) {
    // e1.printStackTrace();
    // }
    //
    // String url = BSApplication.getInstance().getHttpTitle() + Constant.CRM_TRADECONTANT_REMIND;
    // AsyncHttpClient client = new AsyncHttpClient();
    // client.post(url, params, new AsyncHttpResponseHandler() {
    //
    // @Override
    // public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
    // mCommitFlag = true;
    // CustomDialog.closeProgressDialog();
    // }
    //
    // @Override
    // public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
    // mCommitFlag = true;
    // CustomDialog.closeProgressDialog();
    // JSONObject jsonObject;
    // try {
    // jsonObject = new JSONObject(new String(arg2));
    // String str = (String) jsonObject.get("retinfo");
    // String code = (String) jsonObject.get("code");
    // if (Constant.RESULT_CODE.equals(code)) {
    // CustomToast.showShortToast(CrmTradeContantRemindActivity.this, str);
    // CrmTradeContantRemindActivity.this.finish();
    // }
    //
    // } catch (JSONException e) {
    // e.printStackTrace();
    // }
    // }
    // });
    // }

    private LinearLayout mPaymentLayout;
    private List<LinearLayout> mListLayout = new ArrayList<LinearLayout>();
    private boolean mCommitFlag = true;
    private String mHid = "";
    private TextView mRemoveLayout, mAddLayout;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.crm_tradecontant_add_payment, mContentLayout);
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
        mPaymentLayout = (LinearLayout) findViewById(R.id.payment_layout);
        mAddLayout = (TextView) findViewById(R.id.add_plan_layout);
        mRemoveLayout = (TextView) findViewById(R.id.remove_panned_layout);
        mOkTv.setText("保存");
        mTitleTv.setText("添加合同提醒时间");
        initData();
    }

    public void initData() {
        Intent intent = this.getIntent();
        mHid = intent.getStringExtra("hid");

        LinearLayout layout = addLayout();
        // if (intent.getBooleanExtra("edit", false)) {
        // mTitleTv.setText("添加合同提醒时间");
        // mAddLayout.setVisibility(View.GONE);
        // TextView tvOne = (TextView) layout.findViewById(R.id.panned_date);
        // EditText tvTwo = (EditText) layout.findViewById(R.id.money);
        // TextView tvThree = (TextView) layout.findViewById(R.id.reminder_time);
        // EditText tvFour = (EditText) layout.findViewById(R.id.remark);
        //
        // tvOne.setText(DateUtils.parseDateDay(intent.getStringExtra("planned_date")));
        // tvTwo.setText(intent.getStringExtra("money"));
        // tvThree.setText(DateUtils.parseDateDayAndHour(intent.getStringExtra("reminder_time")));
        // tvFour.setText(intent.getStringExtra("remark"));
        // mPid = intent.getStringExtra("pid");
        // }
    }

    @Override
    public void bindViewsListener() {
        mAddLayout.setOnClickListener(this);
        mRemoveLayout.setOnClickListener(this);
        mOkTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_plan_layout:
                addLayout();
                break;
            case R.id.remove_panned_layout:
                removeLayout();
                break;
            case R.id.txt_comm_head_right:
                if (mCommitFlag) {
                    commit();
                }

                break;

            // case R.id.reminder_time:
            // TextView reminderTime = (TextView) view;
            // CommonUtils.initDateView(this, "请选择日期", reminderTime, 1);
            // break;

            default:
                break;
        }
    }

    public LinearLayout addLayout() {
        LinearLayout layout = (LinearLayout) View.inflate(this, R.layout.crm_tradecontant_remind_item, null);
        mPaymentLayout.addView(layout);
        mListLayout.add(layout);
        // TextView tvOne = (TextView) layout.findViewById(R.id.panned_date);
        // tvOne.setOnClickListener(this);
        final TextView tvThree = (TextView) layout.findViewById(R.id.reminder_time);
        tvThree.setOnClickListener(this);
        tvThree.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                CommonUtils.initDateView(CrmTradeContantRemindActivity.this, "请选择日期", tvThree, 1);
            }
        });

        tvThree.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (tvThree.getText().toString().length() > 0 &&
                        DateUtils.getStringToDate(tvThree.getText().toString(), "yyyy-MM-dd HH:mm")
                        < DateUtils.getStringToDate(DateUtils.getCurrentDate(), "yyyy-MM-dd HH:mm")) {
                    CustomToast.showLongToast(CrmTradeContantRemindActivity.this, "请选择有效时间");
                    tvThree.setText("");
                }
            }
        });
        TextView tvTitle = (TextView) layout.findViewById(R.id.plan_title);
        tvTitle.setText("提醒时间—" + mListLayout.size());
        if (mListLayout.size() >= 2)
            mRemoveLayout.setVisibility(View.VISIBLE);
        return layout;
    }

    public void removeLayout() {
        mPaymentLayout.removeView(mListLayout.get(mListLayout.size() - 1));
        mListLayout.remove(mListLayout.size() - 1);
        if (mListLayout.size() == 1)
            mRemoveLayout.setVisibility(View.GONE);
    }

    public void commit() {
        RequestParams params = new RequestParams();

        try {
            params.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            params.put("userid", BSApplication.getInstance().getUserId());
            params.put("hid", mHid);
            int size = mListLayout.size();
            String[] plannedDate = new String[size];
            String[] money = new String[size];
            String[] reminderTime = new String[size];
            String[] remark = new String[size];
            double totalMoney = 0;
            for (int i = 0; i < mListLayout.size(); i++) {
                TextView tvThree = (TextView) mListLayout.get(i).findViewById(R.id.reminder_time);
                EditText tvFour = (EditText) mListLayout.get(i).findViewById(R.id.remark);
                reminderTime[i] = tvThree.getText().toString();
                remark[i] = tvFour.getText().toString();

                if (tvThree.getText().toString().trim().length() == 0) {
                    CustomToast.showLongToast(CrmTradeContantRemindActivity.this, "请选择提醒时间");
                    return;
                }
            }
            CustomDialog.showProgressDialog(this, "正在提交数据...");
            params.put("remindtime", reminderTime);
            params.put("content", remark);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        mCommitFlag = false;

        String url = BSApplication.getInstance().getHttpTitle() + Constant.CRM_TRADECONTANT_REMIND;
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                mCommitFlag = true;
                JSONObject jsonObject;
                CustomDialog.closeProgressDialog();
                try {
                    jsonObject = new JSONObject(new String(arg2));
                    String str = (String) jsonObject.get("retinfo");
                    String code = (String) jsonObject.get("code");
                    if (Constant.RESULT_CODE.equals(code)) {
                        // CustomToast.showShortToast(CrmTradeContantRemindActivity.this, str);
                        // CrmTradeContantRemindActivity.this.setResult(520, new Intent());

                        CrmTradeContantRemindActivity.this.finish();
                    } else {
                        CustomToast.showShortToast(CrmTradeContantRemindActivity.this, str);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
