
package com.bs.bsims.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.ApprovlaNewIdeaAdapter;
import com.bs.bsims.model.CrmTranctVos;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.view.BSCircleImageView;
import com.bs.bsims.view.BSSwitchView;

import java.util.ArrayList;
import java.util.List;

public class CrmTradeHuiKuanDetailActivity extends BaseActivity {
    private TextView mReceiveDateTv, mPayTypeTv, mIsOpenTitle, mApprovalName, mPdNameTv;
    private EditText mMoney, mRemark;
    private BSSwitchView mSwitchView;
    private BSCircleImageView mHeadBsCircleImageView;
    private CrmTranctVos vos;
    private ListView mListView;
    private ApprovlaNewIdeaAdapter mApprovlaIdeaAdapter;
    private TextView mApprovalIdeaTv;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.crm_tradecontant_detailinfo, mContentLayout);
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
        mTitleTv.setText("收款详情");
        mReceiveDateTv = (TextView) findViewById(R.id.receive_date);
        mPayTypeTv = (TextView) findViewById(R.id.pay_type);
        mMoney = (EditText) findViewById(R.id.money);
        mRemark = (EditText) findViewById(R.id.remark);
        mIsOpenTitle = (TextView) findViewById(R.id.is_open_title);
        mSwitchView = (BSSwitchView) findViewById(R.id.is_open_status);
        // 2.0合同详情 改成跟审批一样 添加催一下
        mListView = (ListView) findViewById(R.id.list_view);
        mApprovlaIdeaAdapter = new ApprovlaNewIdeaAdapter(this);
        mApprovlaIdeaAdapter.setViewCui(true);//不显示催办
        mListView.setAdapter(mApprovlaIdeaAdapter);
        mApprovalIdeaTv = (TextView) findViewById(R.id.approval_idea_tv);
        vos = (CrmTranctVos) getIntent().getSerializableExtra("vo_s");
        initData();
    }

    public void initData() {
        mReceiveDateTv.setText(DateUtils.parseDateDay(vos.getPlanned_date()));
        mMoney.setText("￥" + vos.getMoney());
        mPayTypeTv.setText(vos.getPayment_mode());
        if (vos.getReceipt().equals("1")) {
            mIsOpenTitle.setText("开票");
        } else {
            mIsOpenTitle.setText("不开票");
        }
        if (CommonUtils.isNormalString(vos.getRemark())) {
            mRemark.setVisibility(View.VISIBLE);
            mRemark.setText(vos.getRemark());
        } else {
            mRemark.setVisibility(View.GONE);
        }

        if (vos.getAppUser() != null) {
            if (vos.getOpinion() != null) {
                mApprovalIdeaTv.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.VISIBLE);
                List<EmployeeVO> appUserList = new ArrayList<EmployeeVO>();
                appUserList.addAll(vos.getAppUser());
                for (int i = 0; i < vos.getAppUser().size(); i++) {
                    for (int j = 0; j < vos.getOpinion().size(); j++) {
                        if (vos.getOpinion().get(j).getUserid().equals(vos.getAppUser().get(i).getUserid())) {
                            appUserList.remove(i);
                            appUserList.add(i, vos.getOpinion().get(j));
                            continue;
                        }
                    }
                }
                mApprovlaIdeaAdapter.updateData(appUserList);
                mApprovalIdeaTv.setVisibility(View.VISIBLE);

                
            }
            else {
                mApprovlaIdeaAdapter.updateData(vos.getAppUser());
                mApprovalIdeaTv.setVisibility(View.VISIBLE);
            }

        }
        else {
            mApprovalIdeaTv.setVisibility(View.GONE);
            mListView.setVisibility(View.GONE);
        }

    }

    @Override
    public void bindViewsListener() {

    }

}
