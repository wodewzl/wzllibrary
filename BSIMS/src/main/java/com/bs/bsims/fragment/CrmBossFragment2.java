
package com.bs.bsims.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.CrmBossVO;

@SuppressLint("ValidFragment")
public class CrmBossFragment2 extends Fragment {

    private Activity mActivity;
    private ProgressBar progarbar;
    private ProgressBar progarbar1;
    private ProgressBar progarbar2;
    private CrmBossVO mCrmBossVO;
    private TextView mContractTv, mContractStatusTv, mCustomerTv, mCustomerStatusTv, mBusinessTv, mBusinessStatusTv;

    public CrmBossFragment2(CrmBossVO vo) {
        this.mCrmBossVO = vo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.crm_boss_fragment2, container, false);
        initViews(view);
        updateData();
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void initViews(View view) {
        progarbar = (ProgressBar) view.findViewById(R.id.item_taskeventlistadapter_seekbar);
        progarbar1 = (ProgressBar) view.findViewById(R.id.item_taskeventlistadapter_seekbar1);
        progarbar2 = (ProgressBar) view.findViewById(R.id.item_taskeventlistadapter_seekbar2);
        mContractTv = (TextView) view.findViewById(R.id.contact_tv);
        mContractStatusTv = (TextView) view.findViewById(R.id.contact_status_tv);
        mCustomerTv = (TextView) view.findViewById(R.id.customer_tv);
        mCustomerStatusTv = (TextView) view.findViewById(R.id.customer_status_tv);
        mBusinessTv = (TextView) view.findViewById(R.id.business_tv);
        mBusinessStatusTv = (TextView) view.findViewById(R.id.business_status_tv);
    }

    public void updateData() {
        // 合同
        mContractTv.setText(mCrmBossVO.getContract().getThisWeek());
        if ("1".equals(mCrmBossVO.getContract().getStatus())) {
            mContractStatusTv.setText("↑ " + mCrmBossVO.getContract().getChangeNum());
        } else if ("2".equals(mCrmBossVO.getContract().getStatus())) {
            mContractStatusTv.setText("↓  " + mCrmBossVO.getContract().getChangeNum());
        }
        else{
            mContractStatusTv.setText("-  " + mCrmBossVO.getContract().getChangeNum());
        }
        if (!"0".equals(mCrmBossVO.getContract().getChangeNum())) {
            progarbar.setProgress(100);
        } else {
            progarbar.setProgress(0);
        }

        // 客户
        mCustomerTv.setText(mCrmBossVO.getCustomer().getThisWeek());
        if ("1".equals(mCrmBossVO.getCustomer().getStatus())) {
            mCustomerStatusTv.setText("↑ " + mCrmBossVO.getCustomer().getChangeNum());
        } else if ("2".equals(mCrmBossVO.getCustomer().getStatus())) {
            mCustomerStatusTv.setText("↓  " + mCrmBossVO.getCustomer().getChangeNum());
        }
        else{
            mCustomerStatusTv.setText("-  " + mCrmBossVO.getCustomer().getChangeNum());
        }
        if (!"0".equals(mCrmBossVO.getCustomer().getChangeNum())) {
            progarbar1.setProgress(100);
        } else {
            progarbar1.setProgress(0);
        }

        // 商机
        mBusinessTv.setText(mCrmBossVO.getBusiness().getThisWeek());
        if ("1".equals(mCrmBossVO.getBusiness().getStatus())) {
            mBusinessStatusTv.setText(Html.fromHtml("<font size=\"2\">↑</font>") + mCrmBossVO.getBusiness().getChangeNum());
        } else if ("2".equals(mCrmBossVO.getBusiness().getStatus())) {
            mBusinessStatusTv.setText("↓  " + mCrmBossVO.getBusiness().getChangeNum());
        }
        else{
            mBusinessStatusTv.setText("-  " + mCrmBossVO.getBusiness().getChangeNum());
        }
        if (!"0".equals(mCrmBossVO.getBusiness().getChangeNum())) {
            progarbar2.setProgress(100);
        } else {
            progarbar2.setProgress(0);
        }

    }

    public CrmBossVO getmCrmBossVO() {
        return mCrmBossVO;
    }

    public void setmCrmBossVO(CrmBossVO mCrmBossVO) {
        this.mCrmBossVO = mCrmBossVO;
    }
}
