
package com.bs.bsims.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.DanganChuQingInfoLineChart2Activity;
import com.bs.bsims.activity.SalesmanHomeActivity;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.constant.Constant4Statistics;
import com.bs.bsims.interfaces.UpdateCallback;
import com.bs.bsims.model.DanganWorkCount;
import com.bs.bsims.utils.CustomLog;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.utils.ToastUtil;
import com.bs.bsims.utils.UrlUtil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

@SuppressLint("ValidFragment")
public class DanganChuQingInfoFragment extends BaseFragment implements
        OnClickListener, UpdateCallback {
    private String uid;
    private Activity mActivity;

    private RelativeLayout chuqingr1, chuqingr2, chuqingr3, chuqingr4,chuqingr5,chuqingr6,chuqingr7,chuqingr8;

    private DanganWorkCount workcount;

    private TextView cq_queka, cq_zaotui, cq_weixierizi, cq_qingjia, loading;
    
    private TextView mCcount,mVcount,mRmoney,mDayCustomer;
    
    private LinearLayout mIsCrmLayout;

    public DanganChuQingInfoFragment(String uid) {
        this.uid = uid;
    }

    public DanganChuQingInfoFragment() {
    }

    // DanganChuQingInfoLineChartActivity
    @Override
    public String getFragmentName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.da_chuqinginfo, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        bindListers();
    }

    private void bindListers() {
        // TODO Auto-generated method stub
        chuqingr1.setOnClickListener(this);
        chuqingr2.setOnClickListener(this);
        chuqingr3.setOnClickListener(this);
        chuqingr4.setOnClickListener(this);
        chuqingr5.setOnClickListener(this);
        chuqingr6.setOnClickListener(this);
        chuqingr7.setOnClickListener(this);
        chuqingr8.setOnClickListener(this);

    }

    private void initViews(View view) {
        // TODO Auto-generated method stub
        chuqingr1 = (RelativeLayout) view.findViewById(R.id.chuqingr1);
        chuqingr2 = (RelativeLayout) view.findViewById(R.id.chuqingr2);
        chuqingr3 = (RelativeLayout) view.findViewById(R.id.chuqingr3);
        chuqingr4 = (RelativeLayout) view.findViewById(R.id.chuqingr4);
        chuqingr5 = (RelativeLayout) view.findViewById(R.id.chuqingr5);
        chuqingr6 = (RelativeLayout) view.findViewById(R.id.chuqingr6);
        chuqingr7 = (RelativeLayout) view.findViewById(R.id.chuqingr7);
        chuqingr8 = (RelativeLayout) view.findViewById(R.id.chuqingr8);
        cq_queka = (TextView) view.findViewById(R.id.cq_queka);
        cq_zaotui = (TextView) view.findViewById(R.id.cq_zaotui);
        cq_weixierizi = (TextView) view.findViewById(R.id.cq_weixierizi);
        cq_qingjia = (TextView) view.findViewById(R.id.cq_qingjia);
        mCcount = (TextView) view.findViewById(R.id.crm_ccount);
        mVcount = (TextView) view.findViewById(R.id.crm_vcount);
        mRmoney = (TextView) view.findViewById(R.id.crm_rmoney);
        mDayCustomer = (TextView) view.findViewById(R.id.crm_daycustomer);
        mIsCrmLayout = (LinearLayout) view.findViewById(R.id.is_crm); 
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // initData();
        new ThreadUtil(mActivity, this).start();
    }

    @Override
    public boolean execute() {
        // TODO Auto-generated method stub
        return getdata();
    }

    @Override
    public void executeSuccess() {
        // TODO Auto-generated method stub
        cq_queka.setText("(" + workcount.getAbsence() + "次" + ")");
        cq_zaotui.setText("(" + workcount.getBelate() + "次" + ")");
        cq_weixierizi.setText("(" + workcount.getNowrlog() + "次" + ")");
        cq_qingjia.setText("(" + workcount.getAskleave() + "次" + ")");
        mCcount.setText("(" + workcount.getCustomer() + "人" + ")");
        mVcount.setText("(" + workcount.getContract() + "单" + ")");
        mRmoney.setText("(" + workcount.getPayment() + ")");
        mDayCustomer.setText("(" + workcount.getVisitPerday() + "次" + ")");
        if(!workcount.getIsSalesman().equals("1")){
            mIsCrmLayout.setVisibility(View.GONE);//不是业务员
        }
    }

    @Override
    public void executeFailure() {
        // TODO Auto-generated method stub
 
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.chuqingr1:
                intent.putExtra("titleTxt", "缺卡");// 跳转activity的名字
                intent.putExtra("currentType", "1");// 什么类型
                intent.putExtra("datetype", "1");
                if (cq_queka.getText().toString().trim().equals("(0次)") || cq_queka.getText().toString().trim().equals("(0)")) {
                    ToastUtil.show(mActivity, "暂时还没有此类消息");
                    return;
                }
                intent.putExtra("uid", uid);
                // intent.setClass(mActivity, DanganChuQingInfoLineChartActivity.class);
                intent.setClass(mActivity, DanganChuQingInfoLineChart2Activity.class);
                break;

            case R.id.chuqingr2:
                intent.putExtra("titleTxt", "迟到");// 跳转activity的名字
                intent.putExtra("currentType", "2");// 什么类型
                intent.putExtra("datetype", "1");
                if (cq_zaotui.getText().toString().trim().equals("(0次)") || cq_zaotui.getText().toString().trim().equals("(0)")) {

                    ToastUtil.show(mActivity, "暂时还没有此类消息");
                    return;
                }
                intent.putExtra("uid", uid);
                // intent.setClass(mActivity, DanganChuQingInfoLineChartActivity.class);
                intent.setClass(mActivity, DanganChuQingInfoLineChart2Activity.class);
                break;
            case R.id.chuqingr3:
                intent.putExtra("titleTxt", "未写日志");// 跳转activity的名字
                intent.putExtra("currentType", "3");// 什么类型
                intent.putExtra("datetype", "1");
                if (cq_weixierizi.getText().toString().trim().equals("(0次)") || cq_weixierizi.getText().toString().trim().equals("(0)")) {
                    ToastUtil.show(mActivity, "暂时还没有此类消息");
                    return;
                }
                intent.putExtra("uid", uid);
                // intent.setClass(mActivity, DanganChuQingInfoLineChartActivity.class);
                intent.setClass(mActivity, DanganChuQingInfoLineChart2Activity.class);
                break;
            case R.id.chuqingr4:
                intent.putExtra("titleTxt", "请假");// 跳转activity的名字
                intent.putExtra("currentType", "4");// 什么类型
                intent.putExtra("datetype", "1");
                if (cq_qingjia.getText().toString().trim().equals("(0次)") || cq_qingjia.getText().toString().trim().equals("(0)")) {
                    ToastUtil.show(mActivity, "暂时还没有此类消息");
                    return;
                }
                intent.putExtra("uid", uid);
                // intent.setClass(mActivity, DanganChuQingInfoLineChartActivity.class);
                intent.setClass(mActivity, DanganChuQingInfoLineChart2Activity.class);
                break;
            case R.id.chuqingr5:
                intent.putExtra("uid", uid);
                intent.putExtra("type", "5");
                intent.setClass(mActivity, SalesmanHomeActivity.class);
                break;
            case R.id.chuqingr6:
                intent.putExtra("uid", uid);
                intent.putExtra("type", "6");
                intent.setClass(mActivity, SalesmanHomeActivity.class);
                break;
            case R.id.chuqingr7:
                intent.putExtra("uid", uid);
                intent.putExtra("type", "7");
                intent.setClass(mActivity, SalesmanHomeActivity.class);
                break;
            case R.id.chuqingr8:
                intent.putExtra("uid", uid);
                intent.putExtra("type", "8");//没有作用
                intent.setClass(mActivity, SalesmanHomeActivity.class);
                break;

        }
        mActivity.startActivity(intent);

    }

    public boolean getdata() {
        Gson gson = new Gson();
        Map<String, String> map = new HashMap<String, String>();
        try {
            String urlStr;
            String jsonUrlStr;
            map.put("uid", uid);
            urlStr = UrlUtil.getUrlByMap1(
                    Constant4Statistics.DANGAN_CHUQING_INFOL_PATH, map);
            CustomLog.e("WorkUrl", urlStr);
            jsonUrlStr = HttpClientUtil.get(urlStr, Constant.ENCODING).trim();
            CustomLog.e("UserURL", urlStr);
            workcount = gson.fromJson(jsonUrlStr, DanganWorkCount.class);
            if (!workcount.getCode().equals("200")) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

}
