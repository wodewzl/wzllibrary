package com.beisheng.easycar.adapter;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;

import com.beisheng.easycar.R;
import com.beisheng.easycar.mode.NeaybyVO;
import com.wuzhanglong.library.adapter.RecyclerBaseAdapter;

import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * Created by ${Wuzhanglong} on 2017/5/16.
 */

public class NearbyRightAdapter extends RecyclerBaseAdapter {
    public NearbyRightAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.adapter_nearby_right);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initData(BGAViewHolderHelper helper, int position, Object model) {
        NeaybyVO vo= (NeaybyVO) model;
        helper.setText(R.id.title_tv,vo.getName());
        helper.setText(R.id.desc_tv,vo.getAddress());
        helper.setText(R.id.km_tv,vo.getDistance());
    }


}
