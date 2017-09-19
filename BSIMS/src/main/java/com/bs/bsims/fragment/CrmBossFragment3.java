
package com.bs.bsims.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.CrmBossVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.view.BSCrmTrapezoidalFunnelView;

@SuppressLint("ValidFragment")
public class CrmBossFragment3 extends Fragment {

    private Activity mActivity;
    // 漏斗图
    private BSCrmTrapezoidalFunnelView crmfunnelview;
    private CrmBossVO mCrmBossVO;
    private TextView mTextView01, mTextView02, mTextView03, mTextView04, mTextView05, mPredictedMoney, mTargetMoney;

    public CrmBossFragment3(CrmBossVO vo) {
        this.mCrmBossVO = vo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.crm_boss_fragment3, container, false);
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
        crmfunnelview = (BSCrmTrapezoidalFunnelView) view.findViewById(R.id.crmfunnelview);
        mTextView01 = (TextView) view.findViewById(R.id.text01);
        mTextView02 = (TextView) view.findViewById(R.id.text02);
        mTextView03 = (TextView) view.findViewById(R.id.text03);
        mTextView04 = (TextView) view.findViewById(R.id.text04);
        mTextView05 = (TextView) view.findViewById(R.id.text05);
        mPredictedMoney = (TextView) view.findViewById(R.id.predicted_money);
        mTargetMoney = (TextView) view.findViewById(R.id.target_money);
    }

    public void updateData() {
        //
        int flagecount = 100;
        float i = Float.parseFloat(CommonUtils.isNormalData(mCrmBossVO.getArray().get(0).getMoney()));
        float i1 = Float.parseFloat(CommonUtils.isNormalData(mCrmBossVO.getArray().get(1).getMoney()));
        float i2 = Float.parseFloat(CommonUtils.isNormalData(mCrmBossVO.getArray().get(2).getMoney()));
        float i3 = Float.parseFloat(CommonUtils.isNormalData(mCrmBossVO.getArray().get(3).getMoney()));
        float i4 = Float.parseFloat(CommonUtils.isNormalData(mCrmBossVO.getArray().get(4).getMoney()));
        if (i == 0 && i1 == 0 && i2 == 0 && i3 == 0) {
            crmfunnelview.setFlag(true);
        } else {
            crmfunnelview.setFlag(false);
        }

        crmfunnelview.setPrice1(i);
        crmfunnelview.setPrice2(i1);
        crmfunnelview.setPrice3(i2);
        crmfunnelview.setPrice4(i3);
        crmfunnelview.setPrice5(i4);

        crmfunnelview.invalidate();

        mTextView01.setText("初步接洽（" + mCrmBossVO.getArray().get(0).getMoney() + "）");
        mTextView02.setText("需求确定（" + mCrmBossVO.getArray().get(1).getMoney() + "）");
        mTextView03.setText("方案报价（" + mCrmBossVO.getArray().get(2).getMoney() + "）");
        mTextView04.setText("谈判审核（" + mCrmBossVO.getArray().get(3).getMoney() + "）");
        mTextView05.setText("赢单（" + mCrmBossVO.getArray().get(4).getMoney() + "）");

        // mTextView01.setText("初步接洽（" +
        // CommonUtils.countNumber(mCrmBossVO.getArray().get(0).getMoney()) + "）");
        // mTextView02.setText("需求确定（" +
        // CommonUtils.countNumber(mCrmBossVO.getArray().get(1).getMoney()) + "）");
        // mTextView03.setText("方案报价（" +
        // CommonUtils.countNumber(mCrmBossVO.getArray().get(2).getMoney()) + "）");
        // mTextView04.setText("谈判审核（" +
        // CommonUtils.countNumber(mCrmBossVO.getArray().get(3).getMoney()) + "）");
        // mTextView05.setText("赢单（" +
        // CommonUtils.countNumber(mCrmBossVO.getArray().get(4).getMoney()) + "）");
        //
        CommonUtils.setDifferentTextColor(mPredictedMoney, "预测金额：", CommonUtils.countNumber(mCrmBossVO.getPredictedMoney()), "#ff0000");
        CommonUtils.setDifferentTextColor(mTargetMoney, "VS 目标金额：", CommonUtils.countNumber(mCrmBossVO.getTargetMoney()), "#ff0000");
    }

    public CrmBossVO getmCrmBossVO() {
        return mCrmBossVO;
    }

    public void setmCrmBossVO(CrmBossVO mCrmBossVO) {
        this.mCrmBossVO = mCrmBossVO;
    }
}
