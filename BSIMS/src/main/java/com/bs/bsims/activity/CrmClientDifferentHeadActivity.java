
package com.bs.bsims.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.HeadAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.view.BSGridView;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CrmClientDifferentHeadActivity extends BaseActivity implements OnClickListener {
    private static final int ADD_INFORM_PERSON = 10;
    private static final int ADD_PERSON = 2014;
    private BSGridView mApproverGv, mInformGv;
    private HeadAdapter mApproverAdapter, mInformAdapter;
    private TextView mApproverTv, mApprovalGoTv, mInformTv, mInformGoTv, mCancel, mSure;
    private LinearLayout mApproverLayout, mInformLayout;
    private StringBuffer mApprovalPerson, mInformPerson;

    private EmployeeVO mEmployeeVOSelectOne, mEmployeeVOExist;
    private List<EmployeeVO> mVOSelectOneList;
    private List<EmployeeVO> mVOSelectMoreList;
    protected List<EmployeeVO> mExistChargeList = new ArrayList<EmployeeVO>();
    protected List<EmployeeVO> mExistFollowList = new ArrayList<EmployeeVO>();
    private String mCid;
    private boolean mCommitFlag = true;
    /*
     * type用于区分功能执行权限，因为共用同一个界面，有的功能其它模块可能不需要执行 ； type=1,对应于共享联系人功能，从CrmContactDetailActivity过来
     * type=2,对应于添加联合跟进人功能，从CrmTradeContantDeatilsHomeTop3Activity过来
     * type=3,对应于变更负责人功能，从CrmTradeContantDeatilsHomeTop3Activity过来
     */
    private String CurrentType = "0";

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.crm_client_different_head, mContentLayout);
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    @Override
    public void updateUi() {
        if ("0".equals(CurrentType)) {
            // 公海特权时userid 为0
            if (!"0".equals(mEmployeeVOExist.getInfo().getUserid())) {
                ArrayList<EmployeeVO> list = new ArrayList<EmployeeVO>();
                list.add(mEmployeeVOExist.getInfo());
                mApproverAdapter.updateData(list);
            }

            if (mEmployeeVOExist.getInfo().getFollow() != null) {
                mInformAdapter.updateData(mEmployeeVOExist.getInfo().getFollow());
            }
        }
    }

    @Override
    public void initView() {
        mTitleTv.setText("责任人与跟进人");
        mOkTv.setText("确定");
        mApproverGv = (BSGridView) findViewById(R.id.approver_gv);
        mInformGv = (BSGridView) findViewById(R.id.inform_gv);
        mApproverAdapter = new HeadAdapter(this, true, "1", "1");
        mInformAdapter = new HeadAdapter(this, true, "0", "1");
        mApproverGv.setAdapter(mApproverAdapter);
        mInformGv.setAdapter(mInformAdapter);

        mApproverTv = (TextView) findViewById(R.id.approver_tv);
        mInformTv = (TextView) findViewById(R.id.inform_people_tv);
        mApproverLayout = (LinearLayout) findViewById(R.id.approver_layout);
        mInformLayout = (LinearLayout) findViewById(R.id.inform_people_layout);
        mApprovalGoTv = (TextView) findViewById(R.id.approver_go_tv);
        mInformGoTv = (TextView) findViewById(R.id.inform_go_tv);
        mApproverTv.setText("负责人");
        mInformTv.setText("联合跟进人");
        mApproverTv.setVisibility(View.VISIBLE);
        mInformTv.setVisibility(View.VISIBLE);
        mApproverLayout.setVisibility(View.VISIBLE);
        mInformLayout.setVisibility(View.VISIBLE);

        mApprovalPerson = new StringBuffer();
        mInformPerson = new StringBuffer();

        initData();
    }

    public void initData() {

        Intent intent = this.getIntent();

        if (intent.hasExtra("cid"))
            mCid = intent.getStringExtra("cid");

        // type=1,表示从CrmContactDetailActivity跳过来的；因为共用一个界面
        if (intent.hasExtra("CurrentType"))
            CurrentType = intent.getStringExtra("CurrentType");

        // CrmContactDetailActivity共享联系人(type=1)，以及合同详情主页添加相关人(type=2)，要隐藏“负责人”;
        if (CurrentType.equals("1") || CurrentType.equals("2")) {
            if (CurrentType.equals("1")) {
                mTitleTv.setText("共享联系人");
            }
            if (CurrentType.equals("2")) {
                mTitleTv.setText("添加跟进人");
            }
            mInformTv.setVisibility(View.GONE);
            mApproverTv.setVisibility(View.GONE);
            mApproverLayout.setVisibility(View.GONE);
            // 共享联系人、添加相关人对应的显示联合跟进人
            ArrayList<EmployeeVO> eInformList = (ArrayList<EmployeeVO>) getIntent().getSerializableExtra("relation");
            if (eInformList != null) {
                mInformAdapter.updateData(eInformList);
            }
        }

        // 合同详情主页变更负责人,显示“负责人”，隐藏“联合跟进人”
        if (CurrentType.equals("3")) {
            mTitleTv.setText("变更负责人");
            mApproverTv.setVisibility(View.GONE);
            mInformTv.setVisibility(View.GONE);
            mInformLayout.setVisibility(View.GONE);
            ArrayList<EmployeeVO> eInformList = (ArrayList<EmployeeVO>) getIntent().getSerializableExtra("relation");
            if (eInformList != null) {
                mApproverAdapter.updateData(eInformList);
            }
        }

        // 冯程程要求放弃客户要隐藏下边的相关人
        if (getIntent().getBooleanExtra("hiddenbottom", false)) {
            mInformTv.setVisibility(View.GONE);
            mInformLayout.setVisibility(View.GONE);
        }
        else {
            mInformTv.setVisibility(View.VISIBLE);
            mInformLayout.setVisibility(View.VISIBLE);
        }

        // 冯程程要求放弃客户要隐藏上面的负责人
        if (getIntent().getBooleanExtra("hiddentop", false)) {
            mApproverTv.setVisibility(View.GONE);
            mApproverLayout.setVisibility(View.GONE);
        }
        else {
            mApproverTv.setVisibility(View.VISIBLE);
            mApproverLayout.setVisibility(View.VISIBLE);
        }

        if (getIntent().getStringExtra("cid") != null && getIntent().getStringExtra("isfrom") != null) {
            mApproverAdapter.setIsTrade("2");
            mInformAdapter.setIsTrade("2");
            mInformAdapter.setIsBussines(null);
            mApproverAdapter.setIsBussines(null);
            mInformAdapter.setmCids(getIntent().getStringExtra("cid"));
            mApproverAdapter.setmCids(getIntent().getStringExtra("cid"));
        }

    }

    @Override
    public void bindViewsListener() {
        mOkTv.setOnClickListener(this);
    }

    public boolean getData() {
        try {
            Gson gson = new Gson();
            if (CurrentType.equals("1") || CurrentType.equals("2") || CurrentType.equals("3")) {
                return true;
            } else {

                HashMap<String, String> mapExist = new HashMap<String, String>();
                mapExist.put("userid", BSApplication.getInstance().getUserId());
                mapExist.put("cid", mCid);
                mapExist.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
                String jsonStrExist = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.CRM_CLIENT_CHARGE_USER, mapExist);
                mEmployeeVOExist = gson.fromJson(jsonStrExist, EmployeeVO.class);

                if (Constant.RESULT_CODE.equals(mEmployeeVOExist.getCode())) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        // 多选
            case ADD_INFORM_PERSON:
                if (data == null)
                    return;
                mExistFollowList.clear();
                mExistFollowList = (List<EmployeeVO>) data.getSerializableExtra("checkboxlist");
                mInformAdapter.updateData(mExistFollowList);

                break;
            // 单选
            case ADD_PERSON:
                if (data == null)
                    return;
                mExistChargeList.clear();
                mExistChargeList = (List<EmployeeVO>) data.getSerializableExtra("checkboxlist");
                mApproverAdapter.updateData(mExistChargeList);

                break;

        }
    }

    public void commit() {
        // 负责人id拼接起来
        mApprovalPerson.setLength(0);
        for (int i = 0; i < mApproverAdapter.mList.size(); i++) {
            mApprovalPerson.append(mApproverAdapter.mList.get(i).getUserid());
            if (i != mApproverAdapter.mList.size() - 1) {
                mApprovalPerson.append(",");
            }
        }

        // 联合跟进人id拼接起来
        mInformPerson.setLength(0);
        for (int i = 0; i < mInformAdapter.mList.size(); i++) {
            mInformPerson.append(mInformAdapter.mList.get(i).getUserid());
            if (i != mInformAdapter.mList.size() - 1) {
                mInformPerson.append(",");
            }
        }

        if (mCommitFlag) {
            RequestParams params = new RequestParams();
            if (CurrentType.equals("0")) {
                params.put("cid", mCid);
                params.put("uid", mApprovalPerson);
                params.put("follow", mInformPerson);
                commit(this, params, Constant.CRM_CLIENT_CHANGE_CHARGE_USER);
            } else if (CurrentType.equals("1")) {
                // 共享联系人接口
                params.put("touids", mInformPerson);
                params.put("lid", getIntent().getStringExtra("lid"));
                commit(this, params, Constant.CRM_CONTACT_SHARE);
            } else if (CurrentType.equals("2")) {
                // 合同主页添加相关人接口
                params.put("relation", mInformPerson);
                params.put("hid", getIntent().getStringExtra("hid"));
                commit(this, params, Constant.CRM_TRADE_ADDRELATION);
            } else if (CurrentType.equals("3")) {
                // 合同主页变更负责人接口
                params.put("type", getIntent().getStringExtra("type"));
                params.put("hid", getIntent().getStringExtra("hid"));
                params.put("uid", mApprovalPerson);
                commit(this, params, Constant.CRM_TRADE_EDIT);
            }
        }
    }

    @Override
    public void onClick(View arg0) {

        // 负责人id拼接起来
        mApprovalPerson.setLength(0);
        for (int i = 0; i < mApproverAdapter.mList.size(); i++) {
            mApprovalPerson.append(mApproverAdapter.mList.get(i).getUserid());
            if (i != mApproverAdapter.mList.size() - 1) {
                mApprovalPerson.append(",");
            }
        }

        // 当前界面从联系人详情跳转进来时，不显示该“提示框”
        if (CurrentType.equals("0")) {
            if (mApprovalPerson.length() == 0)
                CustomToast.showLongToast(this, "请选择负责人");
        }

        // 联合跟进人id拼接起来
        mInformPerson.setLength(0);
        for (int i = 0; i < mInformAdapter.mList.size(); i++) {
            mInformPerson.append(mInformAdapter.mList.get(i).getUserid());
            if (i != mInformAdapter.mList.size() - 1) {
                mInformPerson.append(",");
            }
        }
        commit();
    }

    public void commit(final Activity activity, RequestParams params, String interfaceUrl) {
        CustomDialog.showProgressDialog(activity, "正在提交数据...");

        try {
            params.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            params.put("userid", BSApplication.getInstance().getUserId());

            String url = BSApplication.getInstance().getHttpTitle() + interfaceUrl;
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(url, params, new AsyncHttpResponseHandler() {

                @Override
                public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                }

                @Override
                public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                    try {
                        JSONObject jsonObject = new JSONObject(new String(arg2));
                        String str = (String) jsonObject.get("retinfo");
                        String code = (String) jsonObject.get("code");
                        if (Constant.RESULT_CODE.equals(code)) {
                            if (!CurrentType.equals("0")) {
                                // 回传刷新数据
                                setResult(520, new Intent());
                            }
                            CrmClientDifferentHeadActivity.this.finish();
                        }
                        CustomToast.showLongToast(activity, str);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        CustomDialog.closeProgressDialog();
                    }

                }
            });
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
