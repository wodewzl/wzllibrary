
package com.bs.bsims.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.CrmTradeContantAddPaymentPlanActivity;
import com.bs.bsims.activity.CrmTradeContantAddPaymentRecordActivity;
import com.bs.bsims.adapter.CrmTradeContantDetailRecordAdapter;
import com.bs.bsims.model.CrmTranctVos;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.view.BSListView;

import java.util.List;

@SuppressLint("ValidFragment")
public class CrmTradeFragment2 extends BaseFragment {

    private Context context;
    private TextView addPlan1, addRecord1, addPlan2, addRecord2;
    private List<CrmTranctVos> crmPlanList, crmPaymentList;
    private LinearLayout noPlanLayout, noRecordLayout;
    private BSListView list1, list2;
    private CrmTradeContantDetailRecordAdapter mRecoderAdapter;
    private CrmTranctVos crmVO;
    private String crmEdit;

    public CrmTradeFragment2(Context context, CrmTranctVos crmVO) {
        this.context = context;
        this.crmVO = crmVO;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.crmtradecontant_main_shoukuan_view, container, false);
        initViews(view);
        updateData();
        bindViewListener();
        return view;
    }

    private void initViews(View view) {
        list1 = (BSListView) view.findViewById(R.id.list1);
        list2 = (BSListView) view.findViewById(R.id.list2);
        addPlan1 = (TextView) view.findViewById(R.id.add_plan1);
        addRecord1 = (TextView) view.findViewById(R.id.add_record1);
        addPlan2 = (TextView) view.findViewById(R.id.add_plan2);
        addRecord2 = (TextView) view.findViewById(R.id.add_record2);
        noPlanLayout = (LinearLayout) view.findViewById(R.id.no_plan_layout);
        noRecordLayout = (LinearLayout) view.findViewById(R.id.no_record_layout);

    }

    private void updateData() {
        crmEdit = crmVO.getCrmEdit();
        crmPlanList = crmVO.getPlanList();
        crmPaymentList = crmVO.getPaymentList();
        if ("1".equals(crmEdit)) {
            addPlan1.setVisibility(View.VISIBLE);
            addPlan2.setVisibility(View.VISIBLE);
            addRecord1.setVisibility(View.VISIBLE);
            addRecord2.setVisibility(View.VISIBLE);
        } else {
            addPlan1.setVisibility(View.GONE);
            addPlan2.setVisibility(View.GONE);
            addRecord1.setVisibility(View.GONE);
            addRecord2.setVisibility(View.GONE);
        }

        if (null != crmPlanList) {
            mRecoderAdapter = new CrmTradeContantDetailRecordAdapter(context, crmPlanList, 1);
            list1.setAdapter(mRecoderAdapter);
            noPlanLayout.setVisibility(View.GONE);

        } else if (null == crmPlanList) {
            noPlanLayout.setVisibility(View.VISIBLE);
            addPlan1.setVisibility(View.GONE);

        }

        if (null != crmPaymentList) {
            mRecoderAdapter = new CrmTradeContantDetailRecordAdapter(context, crmPaymentList, 2);
            list2.setAdapter(mRecoderAdapter);
            noRecordLayout.setVisibility(View.GONE);

        } else if (null == crmPaymentList) {
            noRecordLayout.setVisibility(View.VISIBLE);
            addRecord1.setVisibility(View.GONE);
        }

    }

    private void bindViewListener() {
        addPlan1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if ("1".equals(crmEdit)) {
                    toAddPlanActivity();
                } else {
                    CustomToast.showShortToast(context, "没有添加权限");
                }
            }
        });

        addRecord1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if ("1".equals(crmEdit)) {
                    toAddRecordActivity();
                } else {
                    CustomToast.showShortToast(context, "没有添加权限");
                }
            }
        });
        addPlan2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if ("1".equals(crmEdit)) {
                    toAddPlanActivity();
                } else {
                    CustomToast.showShortToast(context, "没有添加权限");
                }
            }
        });

        addRecord2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if ("1".equals(crmEdit)) {
                    toAddRecordActivity();
                } else {
                    CustomToast.showShortToast(context, "没有添加权限");
                }
            }
        });

        bindListener();

    }

    private void bindListener() {
        list1.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if ("1".equals(crmEdit)) {
                    CrmTranctVos vo = crmPlanList.get((int) arg3);
                    Intent intent = new Intent();
                    intent.setClass(context, CrmTradeContantAddPaymentPlanActivity.class);
                    intent.putExtra("pid", vo.getPid());
                    intent.putExtra("money", vo.getMoney());
                    intent.putExtra("planned_date", vo.getPlanned_date());
                    intent.putExtra("reminder_time", vo.getReminder_time());
                    intent.putExtra("remark", vo.getRemark());
                    intent.putExtra("hid", crmVO.getHid());
                    intent.putExtra("totalMoney", crmVO.getMoney());
                    intent.putExtra("edit", true);
                    startActivityForResult(intent, 1);
                }

            }
        });

    }

    public void toAddPlanActivity() {
        Intent intent = new Intent(context, CrmTradeContantAddPaymentPlanActivity.class);
        intent.putExtra("hid", crmVO.getHid());
        intent.putExtra("totalMoney", crmVO.getMoney());
        startActivityForResult(intent, 520);
    }

    public void toAddRecordActivity() {
        Intent intent = new Intent(context, CrmTradeContantAddPaymentRecordActivity.class);
        intent.putExtra("hid", crmVO.getHid());
        intent.putExtra("remain_money", crmVO.getRemain_money());
        startActivityForResult(intent, 520);
    }

    @Override
    public String getFragmentName() {
        // TODO Auto-generated method stub
        return null;
    }

    public CrmTranctVos getCrmVO() {
        return crmVO;
    }

    // 根据数据，更新界面内容
    public void setCrmVO(CrmTranctVos crmVO) {
        this.crmVO = crmVO;
        updateData();
    }

}
