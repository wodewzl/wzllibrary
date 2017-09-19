
package com.bs.bsims.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.CrmBossVO;
import com.bs.bsims.view.BSRoundProgressBarToCrm;

@SuppressLint("ValidFragment")
public class CrmBossFragment1 extends Fragment {

    private Activity mActivity;
    private BSRoundProgressBarToCrm bsroundgressbar;
    private CrmBossVO mCrmBossVO;
    private TextView mTextview01, mMonthTargetTv, mMonthSingTv, mMonthPaymentTv;

    private Bitmap bitmap;
    private int x = 0, y = 0, xx = 800, yy = 480;
    Paint paint;
    Canvas canvas;

    public CrmBossFragment1(CrmBossVO vo) {
        this.mCrmBossVO = vo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.crm_boss_fragment1, container, false);
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
        bsroundgressbar = (BSRoundProgressBarToCrm) view.findViewById(R.id.progress_bar_crm);
//        bsroundgressbar.getLayoutParams().height=CommonUtils.getScreenWidth(mActivity)/2;
//           view.findViewById(R.id.downprogressly).getLayoutParams().height=CommonUtils.getScreenWidth(mActivity)/2;
        bsroundgressbar.setTextColor(Color.parseColor("#04ADFF"));
        // mMonthTargetTv = (TextView) view.findViewById(R.id.month_target);
        // mMonthSingTv = (TextView) view.findViewById(R.id.month_sign);
        // mMonthPaymentTv = (TextView) view.findViewById(R.id.month_payment);
    }

    public void updateData() {
        bsroundgressbar.setPrice1("￥ " + mCrmBossVO.getMonthTarget());
        bsroundgressbar.setPrice2("￥ " + mCrmBossVO.getMonthSign());
        bsroundgressbar.setPrice3("￥ " + mCrmBossVO.getMonthPayment());
        bsroundgressbar.setProgress(Integer.parseInt(mCrmBossVO.getCompPercent().split("%")[0]), "2");
       try{
            if(Integer.parseInt(mCrmBossVO.getCompPercent().split("%")[0])>100){
                bsroundgressbar.setPrenct(100+"%");
            }
            else{
                bsroundgressbar.setPrenct(mCrmBossVO.getCompPercent());
            }
        }
        catch(Exception e){
            bsroundgressbar.setPrenct(mCrmBossVO.getCompPercent());
        }
//        mTextview01.setText(mCrmBossVO.getCompPercent());
    }

    public CrmBossVO getmCrmBossVO() {
        return mCrmBossVO;
    }

    public void setmCrmBossVO(CrmBossVO mCrmBossVO) {
        this.mCrmBossVO = mCrmBossVO;
    }

}
