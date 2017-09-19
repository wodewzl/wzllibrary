
package com.bs.bsims.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.ApprovlaIdeaAdapter;
import com.bs.bsims.adapter.HeadAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.CrmApprovalDetailVO;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.view.BSCircleImageView;
import com.bs.bsims.view.BSDialog;
import com.bs.bsims.view.BSGridView;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
/**
 * 回款详情界面
 */
public class CrmApprovalDetailActivity extends BaseActivity implements OnClickListener {
    private String mMid;
    private CrmApprovalDetailVO mCrmApprovalDetailVO;
    private TextView mPaymentMoney, mPaymentDate, mTitle, mCompanyName, mStatus, mContractMoney, mPaymentedMoney, mBillingMoney;

    private TextView mPersonTitle01, mPersonTitle02, mPersonTitle03, mPersonTitle04;
    private BSCircleImageView mHeadIcon;

    // 知会人，审批人
    private BSGridView mApproverGv, mInformGv;
    private HeadAdapter mApproverAdapter, mInformAdapter;
    private TextView mApproverTv, mApprovalGoTv, mInformTv, mInformGoTv, mCancel, mSure;
    private LinearLayout mApproverLayout, mInformLayout;

    // 底部审批
    private BSDialog mDialog;
    private Button mApprovalBt, mUnapprovalBt;
    private LinearLayout mBottomLayout;

    // 审批已经
    private ListView mListView;
    private ApprovlaIdeaAdapter mApprovlaIdeaAdapter;
    private TextView mApprovalIdeaTv;

    private TextView mReceiptTv, mPayTypeTv;
    private TextView approval_remark;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.crm_approval_detail, mContentLayout);
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    @Override
    public void updateUi() {

        CrmApprovalDetailVO detailVO = mCrmApprovalDetailVO.getArray();

        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = CommonUtils.initImageLoaderOptions();
        imageLoader.displayImage(detailVO.getHeadpic(), mHeadIcon, options);
        mHeadIcon.setUserId(detailVO.getUserid());//HL:获取回款详情界面title头像对应的用户ID，以便实现跳转
        mHeadIcon.setUrl(detailVO.getHeadpic());
        mHeadIcon.setUserName(detailVO.getFullname());
        mPersonTitle01.setText(detailVO.getFullname());
        mPersonTitle02.setText(detailVO.getDname() + "/" + detailVO.getPname());
        mPersonTitle03.setText(DateUtils.parseDateDay(detailVO.getAddtime()));

        mPaymentMoney.setText("￥" + detailVO.getMoney());
        mPaymentDate.setText(DateUtils.parseDateDay(detailVO.getPlanned_date()));
        mTitle.setText(detailVO.getTitle());
        mCompanyName.setText("客户：" + detailVO.getCname());
        mStatus.setText(detailVO.getStatusName());
        mContractMoney.setText(detailVO.getHmoney());
        mPaymentedMoney.setText(detailVO.getPayment());
        mBillingMoney.setText(detailVO.getReceipt_money());
        mReceiptTv.setText(detailVO.getReceipt());
        mPayTypeTv.setText(detailVO.getPayment_mode());
        if (CommonUtils.isNormalString(detailVO.getRemark()))
            approval_remark.setText(detailVO.getRemark());

        if (mCrmApprovalDetailVO.getArray().getAppUser() != null) {
            mApproverAdapter.updateData(mCrmApprovalDetailVO.getArray().getAppUser());
            mApproverTv.setVisibility(View.VISIBLE);
            mApproverLayout.setVisibility(View.VISIBLE);
            mApproverAdapter.setApproval(true);
            // 刷新状态列表
            for (int i = 0; i < mCrmApprovalDetailVO.getArray().getAppUser().size(); i++) {
                EmployeeVO vo = mCrmApprovalDetailVO.getArray().getAppUser().get(i);
                if ("0".equals(vo.getStatus())) {
                    // mCurrent++;
                }
            }
        }
        if (mCrmApprovalDetailVO.getArray().getInsUser() != null) {
            mInformTv.setVisibility(View.VISIBLE);
            mInformLayout.setVisibility(View.VISIBLE);
            mInformAdapter.updateData(mCrmApprovalDetailVO.getArray().getInsUser());
        } else {
            mInformTv.setVisibility(View.GONE);
            mInformLayout.setVisibility(View.GONE);
        }

        if ("1".equals(mCrmApprovalDetailVO.getArray().getApproval())) {
            mBottomLayout.setVisibility(View.VISIBLE);
        } else {
            mBottomLayout.setVisibility(View.GONE);
        }

        if (mCrmApprovalDetailVO.getArray().getOpinion() != null) {
            mApprovlaIdeaAdapter.updateData(mCrmApprovalDetailVO.getArray().getOpinion());
            mApprovalIdeaTv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initView() {
        mTitleTv.setText("回款详情");
        mHeadIcon = (BSCircleImageView) findViewById(R.id.head_icon);
        mPersonTitle01 = (TextView) findViewById(R.id.person_title01);
        mPersonTitle02 = (TextView) findViewById(R.id.person_title02);
        mPersonTitle03 = (TextView) findViewById(R.id.person_title03);
        mPersonTitle04 = (TextView) findViewById(R.id.person_title04);

        mApproverGv = (BSGridView) findViewById(R.id.approver_gv);
        mInformGv = (BSGridView) findViewById(R.id.inform_gv);
        mApproverAdapter = new HeadAdapter(this, false);
        mInformAdapter = new HeadAdapter(this, false, true);
        mApproverGv.setAdapter(mApproverAdapter);
        mInformGv.setAdapter(mInformAdapter);
        mApproverTv = (TextView) findViewById(R.id.approver_tv);
        mInformTv = (TextView) findViewById(R.id.inform_people_tv);
        mApproverLayout = (LinearLayout) findViewById(R.id.approver_layout);
        mInformLayout = (LinearLayout) findViewById(R.id.inform_people_layout);

        // 审批意见
        mListView = (ListView) findViewById(R.id.list_view);
        mApprovlaIdeaAdapter = new ApprovlaIdeaAdapter(this);
        mListView.setAdapter(mApprovlaIdeaAdapter);
        mApprovalIdeaTv = (TextView) findViewById(R.id.approval_idea_tv);
        mApprovalIdeaTv.setText("审核意见");
        mApprovlaIdeaAdapter.setStatusType("1");

        mPaymentMoney = (TextView) findViewById(R.id.payment_money);
        mPaymentDate = (TextView) findViewById(R.id.payment_date);
        mTitle = (TextView) findViewById(R.id.title);
        mCompanyName = (TextView) findViewById(R.id.company_name);
        mContractMoney = (TextView) findViewById(R.id.contract_money);
        mPaymentedMoney = (TextView) findViewById(R.id.paymented_money);
        mBillingMoney = (TextView) findViewById(R.id.billing_money);
        mStatus = (TextView) findViewById(R.id.state);

        mBottomLayout = (LinearLayout) findViewById(R.id.bottom_layout);
        mApprovalBt = (Button) findViewById(R.id.approval_bt);
        mUnapprovalBt = (Button) findViewById(R.id.unapproval_bt);

        mReceiptTv = (TextView) findViewById(R.id.receipt);
        mPayTypeTv = (TextView) findViewById(R.id.pay_type);
        approval_remark = (TextView) findViewById(R.id.approval_remark);
        initData();
    }

    public void initData() {
        mMid = this.getIntent().getStringExtra("mid");
    }

    @Override
    public void bindViewsListener() {
        mApprovalBt.setOnClickListener(this);
        mUnapprovalBt.setOnClickListener(this);
    }

    public boolean getData() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put("mid", mMid);
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.CRM_APPROVAL_DETAIL, map);
            mCrmApprovalDetailVO = gson.fromJson(jsonStr, CrmApprovalDetailVO.class);
            if (Constant.RESULT_CODE.equals(mCrmApprovalDetailVO.getCode())) {
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

    public void showDialog(final String status) {

        View v = LayoutInflater.from(this).inflate(R.layout.dialog_edittext, null);
        final EditText textView = (EditText) v.findViewById(R.id.edit_content);
        mDialog = new BSDialog(this, "请输入内容", v, new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                uploadAdopt(textView.getText().toString(), status);
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    public void uploadAdopt(String content, final String status) {
        RequestParams params = new RequestParams();

        try {
            params.put("ftoken", BSApplication.getInstance().getmCompany());
            params.put("userid", BSApplication.getInstance().getUserId());
            params.put("status", status);
            params.put("content", content);
            params.put("approvalid", mMid);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        String url = BSApplication.getInstance().getHttpTitle() + Constant.CRM_APPROVAL;
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
                    if ("200".equals(code)) {
                        Intent intent = new Intent();
                        intent.putExtra("mid", mMid);
                        intent.putExtra("status", status);
                        CrmApprovalDetailActivity.this.setResult(1, intent);
                        CrmApprovalDetailActivity.this.finish();
                    } else {

                    }
                    CustomToast.showShortToast(CrmApprovalDetailActivity.this, str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.approval_bt:
                showDialog("1");
                break;
            case R.id.unapproval_bt:
                showDialog("2");
                break;

            default:
                break;
        }
    }

}
