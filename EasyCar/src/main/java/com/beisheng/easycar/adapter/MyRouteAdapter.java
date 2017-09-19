package com.beisheng.easycar.adapter;

import android.support.v7.widget.RecyclerView;

import com.beisheng.easycar.R;
import com.beisheng.easycar.mode.MyRouteVO;
import com.wuzhanglong.library.adapter.RecyclerBaseAdapter;

import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * Created by ${Wuzhanglong} on 2017/5/16.
 */

public class MyRouteAdapter extends RecyclerBaseAdapter {
    public MyRouteAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.adapter_my_route);
    }

    @Override
    public void initData(BGAViewHolderHelper helper, int position, Object model) {
        MyRouteVO vo = (MyRouteVO) model;
        helper.setText(R.id.time_tv, vo.getAddtime());
        helper.setText(R.id.moeny_tv, vo.getTotal());
        helper.setText(R.id.get_car_tv, vo.getStartplace());
        helper.setText(R.id.back_car_tv, vo.getEndplace());
        helper.setText(R.id.status_tv, vo.getStatusname());
    }


}
