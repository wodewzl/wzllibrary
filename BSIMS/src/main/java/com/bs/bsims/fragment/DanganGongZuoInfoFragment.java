
package com.bs.bsims.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.DanganAllWorkChilrd;
import com.bs.bsims.constant.Constant;
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
public class DanganGongZuoInfoFragment extends BaseFragment implements
        OnClickListener, UpdateCallback {
    private String uid;
    private Activity mActivity;
    private RelativeLayout r1, r2, r3, r4, r5, r6;
    private TextView worktrains, worktransfer, workuserpay, workrewards,
            workinterview, worklicense, worktrainsname, worktransfername,
            workuserpayname, workrewardsname, workinterviewname,
            worklicensename, loading;
    private DanganWorkCount workcount, array;

    @Override
    public String getFragmentName() {
        // TODO Auto-generated method stub
        return null;
    }

    public DanganGongZuoInfoFragment(String uid) {
        this.uid = uid;
    }
    public DanganGongZuoInfoFragment() {

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
        View view = inflater.inflate(R.layout.da_gongzuoinfo, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        bindListers();

    }

    private void initViews(View view) {
        // TODO Auto-generated method stub
        r1 = (RelativeLayout) view.findViewById(R.id.r1);
        r2 = (RelativeLayout) view.findViewById(R.id.r2);
        r3 = (RelativeLayout) view.findViewById(R.id.r3);
        r4 = (RelativeLayout) view.findViewById(R.id.r4);
        r5 = (RelativeLayout) view.findViewById(R.id.r5);
        r6 = (RelativeLayout) view.findViewById(R.id.r6);
        worktrains = (TextView) view.findViewById(R.id.worktrains);
        worktransfer = (TextView) view.findViewById(R.id.worktransfer);
        workuserpay = (TextView) view.findViewById(R.id.workuserpay);
        workrewards = (TextView) view.findViewById(R.id.workrewards);
        workinterview = (TextView) view.findViewById(R.id.workinterview);
        worklicense = (TextView) view.findViewById(R.id.worklicense);

        worktrainsname = (TextView) view.findViewById(R.id.worktrainsname);
        worktransfername = (TextView) view.findViewById(R.id.worktransfername);
        workuserpayname = (TextView) view.findViewById(R.id.workuserpayname);
        workrewardsname = (TextView) view.findViewById(R.id.workrewardsname);
        workinterviewname = (TextView) view
                .findViewById(R.id.workinterviewname);
        worklicensename = (TextView) view.findViewById(R.id.worklicensename);
        loading = (TextView) view.findViewById(R.id.loading);
    }

    private void bindListers() {
        // TODO Auto-generated method stub
        r1.setOnClickListener(this);
        r2.setOnClickListener(this);
        r3.setOnClickListener(this);
        r4.setOnClickListener(this);
        r5.setOnClickListener(this);
        r6.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // initData();
        new ThreadUtil(mActivity, this).start();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.r1:
                intent.putExtra("isall", "1");
                intent.putExtra("isadd", "0");
                intent.putExtra("keyCONTACTSTRAIN", "1");
                if (worktrains.getText().toString().trim().equals("(0次)")
                        || worktrains.getText().toString().trim().equals("")) {
                    ToastUtil.show(mActivity, "暂时还没有此类消息");
                    return;
                }
                intent.putExtra("keyCONTACTSTACNAME", worktrainsname.getText()
                        .toString().trim());
                break;
            case R.id.r2:
                intent.putExtra("isall", "1");
                intent.putExtra("isadd", "0");
                intent.putExtra("keyCONTACTSTRAIN", "2");
                if (worktransfer.getText().toString().trim().equals("(0次)")
                        || worktransfer.getText().toString().trim().equals("")) {
                    ToastUtil.show(mActivity, "暂时还没有此类消息");
                    return;
                }
                intent.putExtra("keyCONTACTSTACNAME", worktransfername.getText()
                        .toString().trim());
                break;
            case R.id.r3:
                intent.putExtra("isall", "1");
                intent.putExtra("isadd", "0");
                intent.putExtra("keyCONTACTSTRAIN", "3");
                if (workuserpay.getText().toString().trim().equals("(0次)")
                        || workuserpay.getText().toString().trim().equals("")) {
                    ToastUtil.show(mActivity, "暂时还没有此类消息");
                    return;
                }
                intent.putExtra("keyCONTACTSTACNAME", workuserpayname.getText()
                        .toString().trim());
                break;
            case R.id.r4:
                intent.putExtra("isall", "1");
                intent.putExtra("isadd", "0");
                intent.putExtra("keyCONTACTSTRAIN", "4");
                if (workrewards.getText().toString().trim().equals("(0次)")
                        || workrewards.getText().toString().trim().equals("(0/0次)")) {
                    ToastUtil.show(mActivity, "暂时还没有此类消息");
                    return;
                }
                intent.putExtra("keyCONTACTSTACNAME", workrewardsname.getText()
                        .toString().trim());
                break;
            case R.id.r5:
                intent.putExtra("isall", "1");
                intent.putExtra("isadd", "0");
                intent.putExtra("keyCONTACTSTRAIN", "5");
                if (workinterview.getText().toString().trim().equals("(0次)")
                        || workinterview.getText().toString().trim().equals("")) {
                    ToastUtil.show(mActivity, "暂时还没有此类消息");
                    return;
                }
                intent.putExtra("keyCONTACTSTACNAME", workinterviewname.getText()
                        .toString().trim());
                break;
            case R.id.r6:
                intent.putExtra("isall", "1");
                intent.putExtra("isadd", "0");
                intent.putExtra("keyCONTACTSTRAIN", "6");
                if (worklicense.getText().toString().trim().equals("(0次)")
                        || worklicense.getText().toString().trim().equals("")) {
                    ToastUtil.show(mActivity, "暂时还没有此类消息");
                    return;
                }
                intent.putExtra("keyCONTACTSTACNAME", worklicensename.getText()
                        .toString().trim());
                break;
        }
        intent.putExtra("uid", uid);
        intent.setClass(mActivity, DanganAllWorkChilrd.class);
        mActivity.startActivity(intent);
    }

    @Override
    public boolean execute() {
        // TODO Auto-generated method stub
        return getData();
    }

    @Override
    public void executeSuccess() {
        // TODO Auto-generated method stub
        worktrains.setText("(" + array.getTrains() + "次" + ")");
        worktransfer.setText("(" + array.getTransfer() + "次" + ")");
        workuserpay.setText("(" + array.getUserpay() + "次" + ")");
        workrewards.setText("(" + array.getRewards().getTwo() + "/"
                + array.getRewards().getOne() + "次" + ")");
        workinterview.setText("(" + array.getInterview() + "次" + ")");
        worklicense.setText("(" + array.getLicense() + "次" + ")");
    }

    @Override
    public void executeFailure() {
        // TODO Auto-generated method stub
      
    }

    public boolean getData() {
        Gson gson = new Gson();
        Map<String, String> map = new HashMap<String, String>();
        try {
            String urlStr;
            String jsonUrlStr;
            map.put("uid", uid);
            urlStr = UrlUtil.getUrlByMap1(Constant.DANGANARCHIVESWORKING, map);
            CustomLog.e("WorkUrl", urlStr);
            jsonUrlStr = HttpClientUtil.get(urlStr, Constant.ENCODING).trim();
            CustomLog.e("UserURL", urlStr);
            workcount = gson.fromJson(jsonUrlStr, DanganWorkCount.class);
            array = workcount.getArray();
            if (workcount.getCode().equals("400")) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

}
