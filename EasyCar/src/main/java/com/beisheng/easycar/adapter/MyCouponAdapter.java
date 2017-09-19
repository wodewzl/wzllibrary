package com.beisheng.easycar.adapter;

import android.support.v7.widget.RecyclerView;

import com.beisheng.easycar.R;
import com.beisheng.easycar.mode.MyCouponVO;
import com.wuzhanglong.library.adapter.RecyclerBaseAdapter;

import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * Created by ${Wuzhanglong} on 2017/5/16.
 */

public class MyCouponAdapter extends RecyclerBaseAdapter {
    public MyCouponAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.adapter_my_coupon);
    }

    @Override
    public void initData(BGAViewHolderHelper helper, int position, Object model) {
        MyCouponVO vo = (MyCouponVO) model;

        if("1".equals(vo.getStatus())){
            helper.setImageResource(R.id.status_img01,R.drawable.my_coupon_01);
            helper.setImageResource(R.id.status_img02,R.drawable.my_coupon_04);
        }else if("2".equals(vo.getStatus())){
            helper.setImageResource(R.id.status_img01,R.drawable.my_coupon_02);
            helper.setImageResource(R.id.status_img02,R.drawable.my_coupon_05);
        }else{
            helper.setImageResource(R.id.status_img01,R.drawable.my_coupon_03);
            helper.setImageResource(R.id.status_img02,R.drawable.my_coupon_06);
        }

        helper.setText(R.id.name_tv,vo.getName());
        helper.setText(R.id.time_tv,vo.getEndtime());
    }

    @Override
    public int getItemCount() {
        return 30;
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.adapter_my_coupon;
    }
}
