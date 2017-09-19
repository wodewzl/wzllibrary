package com.beisheng.easycar.adapter;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beisheng.easycar.R;
import com.beisheng.easycar.activity.UseCarActivity;
import com.beisheng.easycar.mode.CarVO;
import com.squareup.picasso.Picasso;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.adapter.RecyclerBaseAdapter;
import com.wuzhanglong.library.utils.BaseCommonUtils;

import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * Created by ${Wuzhanglong} on 2017/5/16.
 */

public class NearbyCarAdapter extends RecyclerBaseAdapter {
    public NearbyCarAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.adapter_nearby_car);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initData(BGAViewHolderHelper helper, int position, Object model) {
        final CarVO vo= (CarVO) model;
        ImageView img = helper.getImageView(R.id.car_img);
        Picasso.with(mContext).load(vo.getPic()).into(img);
        helper.setText(R.id.car_name_tv,vo.getTitle());
        helper.setText(R.id.car_number_tv,vo.getNumber());
        helper.setText(R.id.car_lucheng_tv,vo.getKm());
        TextView userTv=helper.getTextView(R.id.car_use_tv);
        userTv.setBackground(BaseCommonUtils.setBackgroundShap(mContext, 5, R.color.Car2, R.color.Car2));
        helper.setItemChildClickListener(R.id.car_use_tv);
        helper.setOnItemChildClickListener(new BGAOnItemChildClickListener() {
            @Override
            public void onItemChildClick(ViewGroup viewGroup, View view, int i) {
                Bundle bundle= new Bundle();
                bundle.putString("carid",vo.getId());
                BaseActivity activity= (BaseActivity) mContext;
                activity.open(UseCarActivity.class,bundle,0);
            }
        });
    }


}
