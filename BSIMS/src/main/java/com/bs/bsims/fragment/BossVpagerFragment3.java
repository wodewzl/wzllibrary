
package com.bs.bsims.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.ApprovalViewActivity;
import com.bs.bsims.activity.BossStatisticsAttendanceActivity;

public class BossVpagerFragment3 extends Fragment implements OnClickListener {
    private Activity mActivity;
    private TextView mChiDaoCount;
    private TextView mQueKaCount;
    private TextView mQingJiaCount;
    private ImageView mImage01, mImage02, mImage03;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.boss_view_pager3, container, false);
        initViews(view);
        // updateData();
        return view;
    }

    public void initViews(View view) {
        mChiDaoCount = (TextView) view.findViewById(R.id.chidao_count);
        mQueKaCount = (TextView) view.findViewById(R.id.queka_count);
        mQingJiaCount = (TextView) view.findViewById(R.id.qingjia_count);
        mImage01 = (ImageView) view.findViewById(R.id.image_01);
        mImage02 = (ImageView) view.findViewById(R.id.image_02);
        mImage03 = (ImageView) view.findViewById(R.id.image_03);
        bindViewsListener();
    }

    public void bindViewsListener() {
        mImage01.setOnClickListener(this);
        mImage02.setOnClickListener(this);
        mImage03.setOnClickListener(this);
    }

    public void updateData(String[] data) {
        mChiDaoCount.setText(data[0]);
        mQueKaCount.setText(data[1]);
        mQingJiaCount.setText(data[2]);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.image_01:// 迟到
                intent.putExtra("type", 7);
                intent.putExtra("title_name", "迟到");
                intent.setClass(mActivity, BossStatisticsAttendanceActivity.class);
                break;
            case R.id.image_02:// 缺卡
                intent.putExtra("type", 6);
                intent.putExtra("title_name", "缺卡");
                intent.setClass(mActivity, BossStatisticsAttendanceActivity.class);
                break;
            case R.id.image_03:// 请假
                // i.setClass(mActivity, BossStatisticsAttendanceActivity.class);
                intent.setClass(mActivity, ApprovalViewActivity.class);
                intent.putExtra("is_qj", "请假");
                // CustomToast.showShortToast(mActivity, "模块开发中，后续更新~");
                break;

        }
        startActivity(intent);
    }
}
