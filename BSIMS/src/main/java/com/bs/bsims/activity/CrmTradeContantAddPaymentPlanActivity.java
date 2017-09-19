
package com.bs.bsims.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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

public class CrmTradeContantAddPaymentPlanActivity extends BaseActivity implements OnClickListener {
    private LinearLayout mPaymentLayout;
    private List<LinearLayout> mListLayout = new ArrayList<LinearLayout>();
    private boolean mCommitFlag = true;
    private ListView mListView;
    private ArrayList<Object> mList;
    private int downX;
    private String mHid = "";
    private String mPid = "";
    private String mTotalMoney;
    private TextView mRemoveLayout, mAddLayout, mPlanTitle;

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
        mTitleTv.setText("添加回款计划");
        initData();
    }

    public void initData() {
        Intent intent = this.getIntent();
        mHid = intent.getStringExtra("hid");
        mTotalMoney = intent.getStringExtra("totalMoney");

        LinearLayout layout = addLayout();
        if (intent.getBooleanExtra("edit", false)) {
            mTitleTv.setText("编辑回款计划");
            mAddLayout.setVisibility(View.GONE);
            TextView tvOne = (TextView) layout.findViewById(R.id.panned_date);
            EditText tvTwo = (EditText) layout.findViewById(R.id.money);
            TextView tvThree = (TextView) layout.findViewById(R.id.reminder_time);
            EditText tvFour = (EditText) layout.findViewById(R.id.remark);

            tvOne.setText(DateUtils.parseDateDay(intent.getStringExtra("planned_date")));
            tvTwo.setText(intent.getStringExtra("money"));
            tvThree.setText(DateUtils.parseDateDayAndHour(intent.getStringExtra("reminder_time")));
            tvFour.setText(intent.getStringExtra("remark"));
            mPid = intent.getStringExtra("pid");
        }
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

            case R.id.panned_date:
                TextView pannedDate = (TextView) view;
                CommonUtils.initDateView(this, "请选择日期", pannedDate, 2);
                break;
            case R.id.reminder_time:
                TextView reminderTime = (TextView) view;
                CommonUtils.initDateView(this, "请选择日期", reminderTime, 1);
                break;

            default:
                break;
        }
    }

    public LinearLayout addLayout() {
        LinearLayout layout = (LinearLayout) View.inflate(this, R.layout.crm_tradecontant_add_payment_item, null);
        mPaymentLayout.addView(layout);
        mListLayout.add(layout);
        TextView tvOne = (TextView) layout.findViewById(R.id.panned_date);
        tvOne.setOnClickListener(this);
        TextView tvThree = (TextView) layout.findViewById(R.id.reminder_time);
        tvThree.setOnClickListener(this);
        TextView tvTitle = (TextView) layout.findViewById(R.id.plan_title);
        tvTitle.setText("回款计划—" + mListLayout.size());
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
            params.put("pid", mPid);
            int size = mListLayout.size();
            String[] plannedDate = new String[size];
            String[] money = new String[size];
            String[] reminderTime = new String[size];
            String[] remark = new String[size];
            double totalMoney = 0;
            for (int i = 0; i < mListLayout.size(); i++) {
                TextView tvOne = (TextView) mListLayout.get(i).findViewById(R.id.panned_date);
                EditText tvTwo = (EditText) mListLayout.get(i).findViewById(R.id.money);
                TextView tvThree = (TextView) mListLayout.get(i).findViewById(R.id.reminder_time);
                EditText tvFour = (EditText) mListLayout.get(i).findViewById(R.id.remark);
                totalMoney += Double.parseDouble(tvTwo.getText().toString());

                if (tvOne.getText().toString().trim().length() == 0) {
                    CustomToast.showLongToast(CrmTradeContantAddPaymentPlanActivity.this, "请填写回款日期");
                    return;
                }
                if (tvTwo.getText().toString().trim().length() == 0) {
                    CustomToast.showLongToast(CrmTradeContantAddPaymentPlanActivity.this, "请填写回款金额");
                    return;
                } else if (totalMoney > Double.parseDouble(mTotalMoney)) {
                    CustomToast.showLongToast(CrmTradeContantAddPaymentPlanActivity.this, "回款金额要小于合同金额");
                    return;
                }

                plannedDate[i] = tvOne.getText().toString();
                money[i] = tvTwo.getText().toString();
                reminderTime[i] = tvThree.getText().toString();
                remark[i] = tvFour.getText().toString();
            }
            CustomDialog.showProgressDialog(this, "正在提交数据...");
            params.put("planned_date", plannedDate);
            params.put("money", money);
            params.put("reminder_time", reminderTime);
            params.put("remark", remark);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        mCommitFlag = false;

        String url = BSApplication.getInstance().getHttpTitle() + Constant.CRM_TRADECONTANT_PLAN_ADD;
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
                        CustomToast.showShortToast(CrmTradeContantAddPaymentPlanActivity.this, str);
                        // 返回到CrmTradeContantDeatilsHomeTop3Activity，刷新数据
                        CrmTradeContantAddPaymentPlanActivity.this.setResult(520, new Intent());

                        CrmTradeContantAddPaymentPlanActivity.this.finish();
                    } else {
                        CustomToast.showShortToast(CrmTradeContantAddPaymentPlanActivity.this, str);
                    }
                    CustomDialog.closeProgressDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
