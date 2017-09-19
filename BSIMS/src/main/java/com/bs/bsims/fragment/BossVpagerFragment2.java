
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
import com.bs.bsims.activity.DanganIndexoneActivity;
import com.bs.bsims.model.ResultVO;

public class BossVpagerFragment2 extends Fragment implements OnClickListener {

    private Activity mActivity;
    private TextView mAllPeopleTv;
    private TextView mCurrentMontInTv;
    private TextView mCurrentMontOutTv;
    private ImageView mImage01, mImage02, mImage03;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.boss_view_pager2, container, false);
        initViews(view);
        return view;
    }

    public void initViews(View view) {
        mAllPeopleTv = (TextView) view.findViewById(R.id.all_count);
        mCurrentMontInTv = (TextView) view.findViewById(R.id.new_count);
        mCurrentMontOutTv = (TextView) view.findViewById(R.id.resign_count);
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
        mAllPeopleTv.setText(data[0]);
        mCurrentMontInTv.setText(data[1]);
        mCurrentMontOutTv.setText(data[2]);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.image_01:
                intent.setClass(mActivity, DanganIndexoneActivity.class);
                break;
            case R.id.image_02:
                intent.putExtra("isentry","1");
                intent.setClass(mActivity, DanganIndexoneActivity.class);
                break;
            case R.id.image_03:
                intent.putExtra("isquit", "1");
                intent.setClass(mActivity, DanganIndexoneActivity.class);
                break;

            default:
                break;
        }

        if (ResultVO.getInstance() != null)
            mActivity.startActivity(intent);
    }

    
   
}
