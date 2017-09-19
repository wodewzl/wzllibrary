
package com.bs.bsims.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.BossStatisticsSaleValueActivity;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.view.BSRoundProgressBar;

public class BossVpagerFragment1 extends Fragment implements OnClickListener {
    private Activity mActivity;
    private BSRoundProgressBar mProgressBar;
    private TextView mTargetMoneyTv, mSignMoneyTv, mTextView02;
    private TextView mProgressTv, mLine;
    private LinearLayout mScaleValueLy;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.boss_view_pager1, container, false);
        initViews(view);
        return view;
    }

    @SuppressLint("CutPasteId")
    public void initViews(View view) {
        mProgressBar = (BSRoundProgressBar) view.findViewById(R.id.progress_bar_02);
        mProgressBar.setProgress(0, "3");
        mTargetMoneyTv = (TextView) view.findViewById(R.id.target_money_tv);
        mSignMoneyTv = (TextView) view.findViewById(R.id.sign_money_tv);
        mProgressTv = (TextView) view.findViewById(R.id.text_02);
        mProgressTv.setText("0%");
        mLine = (TextView) view.findViewById(R.id.middle_text_02);
        mTextView02 = (TextView) view.findViewById(R.id.text_02_01);
        mScaleValueLy =(LinearLayout) view.findViewById(R.id.readsaclevalue);
        mLine.getLayoutParams().width = CommonUtils.getViewWidth(mTextView02);
        mLine.getLayoutParams().height = CommonUtils.getViewHigh(mTextView02);
        initData();
    }

    public void initData() {
        mScaleValueLy.setOnClickListener(this);
    }

    public void updateData(String[] data) {
        mProgressTv.setText(data[0]);
        final int possgress = Integer.parseInt(data[0].split("%")[0]);

        new Thread(new Runnable() {
            @Override
            public void run() {
                int currentPossgress = 0;
                while (currentPossgress <= possgress) {
                    mProgressBar.setProgress(currentPossgress, "3");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    currentPossgress += 10;
                }
            }

        }).start();
        mTargetMoneyTv.setText(CommonUtils.formatDetailMoney(data[1]));
        mSignMoneyTv.setText(CommonUtils.formatDetailMoney(data[2]));
    }
    
 
 
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.readsaclevalue:
                startActivity(new Intent(mActivity, BossStatisticsSaleValueActivity.class));
                break;

            default:
                break;
        }
        
    }
}
