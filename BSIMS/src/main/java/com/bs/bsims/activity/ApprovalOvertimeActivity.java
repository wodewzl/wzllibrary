
package com.bs.bsims.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.bsims.R;
import com.bs.bsims.adapter.HeadAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.model.OvertimeResultVO;
import com.bs.bsims.model.OvertimeVO;
import com.bs.bsims.time.ScreenInfo;
import com.bs.bsims.time.WheelMain;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.utils.UrlUtil;
import com.bs.bsims.view.BSDialog;
import com.bs.bsims.view.BSGridView;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ApprovalOvertimeActivity extends BaseActivity implements OnClickListener, OnItemClickListener {
    private static final int ADD_INFORM_PERSON = 10;
    private LinearLayout mStartTimeLayout, mEndTimeLayout;
    private TextView mStratTimeTv, mEndTimeTv;
    private EditText mDurationTimeTv;

    private WheelMain wheelMain;
    private int mStatus = 0;

    private BSGridView mApproverGv, mInformGv;
    private HeadAdapter mApproverAdapter, mInformAdapter;
    private TextView mApproverTv, mApprovalGoTv, mInformTv, mInformGoTv, mCancel, mSure;
    private LinearLayout mApproverLayout, mInformLayout;

    private String mTypeId;
    private String mApprovalType;
    private TextView mApprovalTypeTv;
    private OvertimeVO mOvertimeVO;
    private OvertimeResultVO mOvertimeResultVO;

    private StringBuffer mApprovalPerson, mInformPerson;
    private EditText mOvertimeReasonContentEt, mOverTimeContentEt;

    private String mUid;
    private String mHours;
    private boolean mFlag = true;
    private TextView mDurationTimeUnit;

    // 添加员工使用
    protected List<EmployeeVO> mDataList = new ArrayList<EmployeeVO>();
    private BSDialog dialog;

    // 一开始进来不需要请求数据
    private boolean mFirst = false;

 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.approval_overtime, null);
        mContentLayout.addView(layout);
    }

    @Override
    public boolean getDataResult() {
        // 一开始进来不需要请求数据
        if (mFirst)
            return getData();
        else
            return true;
    }

    @Override
    public void updateUi() {
        mFirst = true;
        if (mOvertimeResultVO == null) {
            return;
        }
        mOvertimeVO = mOvertimeResultVO.getArray();
        if (mOvertimeVO == null) {
            CustomToast.showLongToast(this, mOvertimeResultVO.getRetinfo());
            return;
        }

        mApprovalPerson.setLength(0);
        if (mOvertimeVO.getAppUser() != null) {
            mApproverTv.setVisibility(View.VISIBLE);
            mApproverLayout.setVisibility(View.VISIBLE);
            for (int i = 0; i < mOvertimeVO.getAppUser().size(); i++) {
                mApprovalPerson.append(mOvertimeVO.getAppUser().get(i).getUserid());
                if (i != mOvertimeVO.getAppUser().size() - 1) {
                    mApprovalPerson.append(",");
                }
            }
            mApproverAdapter.setApproval(true);
            mApproverAdapter.updateData(mOvertimeVO.getAppUser());
        } else {
            mApproverTv.setVisibility(View.GONE);
            mApproverLayout.setVisibility(View.GONE);
        }

        mInformPerson.setLength(0);
        mInformAdapter.mList.clear();
        mInformAdapter.notifyDataSetChanged();
        if (mOvertimeVO.getInsUser() != null) {

            for (int i = 0; i < mOvertimeVO.getInsUser().size(); i++) {
                mInformPerson.append(mOvertimeVO.getInsUser().get(i).getUserid());
                if (i != mOvertimeVO.getInsUser().size() - 1) {
                    mInformPerson.append(",");
                }
            }
            mInformTv.setVisibility(View.VISIBLE);
            mInformLayout.setVisibility(View.VISIBLE);
            mInformAdapter.updateData(mOvertimeVO.getInsUser());
        }
    }

    @Override
    public void bindViewsListener() {
        mStartTimeLayout.setOnClickListener(this);
        mEndTimeLayout.setOnClickListener(this);
        mSure.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        mOkTv.setOnClickListener(this);
        // mInformGv.setOnItemClickListener(this);
        mDurationTimeTv.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                mHours = mDurationTimeTv.getText().toString();
                new ThreadUtil(ApprovalOvertimeActivity.this, ApprovalOvertimeActivity.this).start();
            }
        });
    }

    @Override
    public void initView() {
        mTitleTv.setText("加班申请");
        mOkTv.setText("确定");
        mApprovalTypeTv = (TextView) findViewById(R.id.approval_type);
        mStartTimeLayout = (LinearLayout) findViewById(R.id.start_time_layout);
        mEndTimeLayout = (LinearLayout) findViewById(R.id.end_time_layout);
        mStratTimeTv = (TextView) findViewById(R.id.start_time_tv);
        mEndTimeTv = (TextView) findViewById(R.id.end_time_tv);
        mDurationTimeTv = (EditText) findViewById(R.id.duration_time_tv);
        mDurationTimeUnit = (TextView) findViewById(R.id.duration_time_unit);

        mOvertimeReasonContentEt = (EditText) findViewById(R.id.overtime_reason_content);
        mOverTimeContentEt = (EditText) findViewById(R.id.overtime_content);

        mApproverGv = (BSGridView) findViewById(R.id.approver_gv);
        mInformGv = (BSGridView) findViewById(R.id.inform_gv);
        mApproverAdapter = new HeadAdapter(this, false);
        mInformAdapter = new HeadAdapter(this, true);
        mApproverGv.setAdapter(mApproverAdapter);
        mInformGv.setAdapter(mInformAdapter);

        mApproverTv = (TextView) findViewById(R.id.approver_tv);
        mInformTv = (TextView) findViewById(R.id.inform_people_tv);
        mApproverLayout = (LinearLayout) findViewById(R.id.approver_layout);
        mInformLayout = (LinearLayout) findViewById(R.id.inform_people_layout);
        mApprovalGoTv = (TextView) findViewById(R.id.approver_go_tv);
        mInformGoTv = (TextView) findViewById(R.id.inform_go_tv);
        mCancel = (TextView) findViewById(R.id.cancel);
        mSure = (TextView) findViewById(R.id.sure);

        mApprovalPerson = new StringBuffer();
        mInformPerson = new StringBuffer();

        initData();
    }

    public void initData() {
        mApprovalType = getIntent().getStringExtra("approvalType");
        mUid = getIntent().getStringExtra("uid");
        mApprovalTypeTv.setText(mApprovalType);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.start_time_layout:
                mStatus = 1;
                initDateView();
                break;
            case R.id.end_time_layout:
                mStatus = 2;
                initDateView();
                break;

            case R.id.cancel:
                this.finish();
                break;
            case R.id.txt_comm_head_right:
                if (mStratTimeTv.getText().length() == 0) {
                    CustomToast.showLongToast(this, "开始时间不能为空");
                    return;
                }
                if (mEndTimeTv.getText().length() == 0) {
                    CustomToast.showLongToast(this, "结束时间不能为空");
                    return;
                }
                if (mDurationTimeTv.getText().length() == 0) {
                    CustomToast.showLongToast(this, "加班时长不能为空");
                    return;
                }
                if (mOvertimeReasonContentEt.getText().length() == 0) {
                    CustomToast.showLongToast(this, "加班说明不能为空");
                    return;
                }
//                if (mOverTimeContentEt.getText().length() == 0) {
//                    CustomToast.showLongToast(this, "工作完成情况不能为空");
//                    return;
//                }

                if (mApprovalPerson.length() == 0) {
                    CustomToast.showLongToast(this, "由于你的权限过高，无法发布此审批");
                    return;
                }
                if (mFlag) {
                    mFlag = false;
                    commit();
                }
                break;
            default:
                break;
        }

    }

    public void initDateView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View timepickerview = inflater.inflate(R.layout.timepicker, null);
        ScreenInfo screenInfo = new ScreenInfo(this);
        wheelMain = new WheelMain(timepickerview);
        wheelMain.screenheight = screenInfo.getHeight();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        wheelMain.initDateTimePicker(year, month, day, hour, minute);

        dialog = new BSDialog(this, "请选择日期", timepickerview, new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mStatus == 1) {
                    mStratTimeTv.setText(wheelMain.getTime());
                } else if (mStatus == 2) {
                    mEndTimeTv.setText(wheelMain.getTime());
                }

                if (mStratTimeTv.getText().toString().length() > 0 && mEndTimeTv.getText().toString().length() > 0) {
                    long startTime = DateUtils.getStringToDate(mStratTimeTv.getText().toString());
                    long endTime = DateUtils.getStringToDate(mEndTimeTv.getText().toString());
                    long durationTime = (endTime - startTime);
                    if (durationTime < 0) {
                        Toast.makeText(ApprovalOvertimeActivity.this, "结束时间要比开始时间大", Toast.LENGTH_LONG).show();
                    } else {
                        double aprrovlTime = (double)durationTime / 60 / 60 / 1000;
                        mDurationTimeTv.setText(CommonUtils.roundNumber(aprrovlTime) + "");
                        mDurationTimeUnit.setVisibility(View.VISIBLE);
                    }
                }
                dialog.dismiss();

            }
        });
        dialog.show();

    }

    public boolean getData() {
        try {
            String strUlr = UrlUtil.getApprovalOvertimeUrk(Constant.APPROVAL_OVERTIME, mUid, mHours);
            String jsonStr = HttpClientUtil.get(strUlr, Constant.ENCODING).trim();
            Gson gson = new Gson();
            mOvertimeResultVO = gson.fromJson(jsonStr, OvertimeResultVO.class);

            return true;
            // if (Constant.RESULT_CODE.equals(mOvertimeResultVO.getCode())) {
            //
            // } else {
            // return false;
            // }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean commit() {
        CustomDialog.showProgressDialog(this, "正在提到数据...");
        RequestParams params = new RequestParams();

        try {
            params.put("ftoken", BSApplication.getInstance().getmCompany());
            params.put("userid", BSApplication.getInstance().getUserId());
            params.put("typeid", mTypeId);
            params.put("starttime", mStratTimeTv.getText().toString());
            params.put("endtime", mEndTimeTv.getText().toString());
            params.put("duration", mDurationTimeTv.getText().toString());
            params.put("reason", mOvertimeReasonContentEt.getText().toString());
            params.put("info", "");
            params.put("approver", mApprovalPerson.toString());
            params.put("insider", mInformPerson.toString());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        // params.put("name", "woshishishi");// 传输的字符数据
        String url = BSApplication.getInstance().getHttpTitle() + Constant.APPROVAL_OVERTIME_PUSH;
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                System.out.println(new String(arg2));

                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(new String(arg2));
                    String str = (String) jsonObject.get("retinfo");
                    String code = (String) jsonObject.get("code");
                    if (Constant.RESULT_CODE.equals(code)) {
                        ApprovalOvertimeActivity.this.finish();
                        CustomToast.showShortToast(ApprovalOvertimeActivity.this, str);
                    } else {
                        CustomToast.showShortToast(ApprovalOvertimeActivity.this, str);
                    }
                    CustomDialog.closeProgressDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    mFlag = true;
                }
            }

        });
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View arg0, int arg1, long arg2) {

        if (parent == mInformGv) {
            if (arg2 == mInformAdapter.mList.size()) {
                Intent intent = new Intent();
                intent.setClass(ApprovalOvertimeActivity.this, AddByDepartmentActivity.class);
                intent.putExtra("employ_name", JournalPublishActivity.class);
                // intent.putExtra("checkboxlist", (Serializable) mDataList);
                intent.putExtra("requst_number", 10);
                startActivityForResult(intent, 10);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ADD_INFORM_PERSON:
                if (requestCode == 10) {
                    if (data == null)
                        return;
                    mDataList.clear();
                    mDataList = (List<EmployeeVO>) data.getSerializableExtra("checkboxlist");
                    mInformAdapter.mList.clear();
                    mInformAdapter.mList.addAll(mDataList);
                    mInformAdapter.notifyDataSetChanged();
                    mInformPerson.setLength(0);
                    for (int i = 0; i < mDataList.size(); i++) {
                        mInformPerson.append(mDataList.get(i).getUserid());
                        if (i != mDataList.size() - 1) {
                            mInformPerson.append(",");
                        }
                    }
                }
                break;

        }
    }

}
