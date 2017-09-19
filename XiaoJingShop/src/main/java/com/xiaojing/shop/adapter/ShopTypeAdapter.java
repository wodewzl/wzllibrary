package com.xiaojing.shop.adapter;

import android.support.v7.widget.RecyclerView;

import com.wuzhanglong.library.adapter.RecyclerBaseAdapter;
import com.xiaojing.shop.R;
import com.xiaojing.shop.mode.OptionVO;

import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * Created by Administrator on 2017/2/13.
 */

public class ShopTypeAdapter extends RecyclerBaseAdapter<OptionVO> {

    public ShopTypeAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.shop_type_adapter);
    }

    @Override
    public void initData(BGAViewHolderHelper helper, int position, Object model) {
        final OptionVO vo = (OptionVO) model;
        helper.setText(R.id.name_tv, vo.getSc_name());
        if (vo.isCheck()) {
            helper.setImageResource(R.id.check_img, R.drawable.check_select);
        } else {
            helper.setImageResource(R.id.check_img, R.drawable.check_normal);
        }

    }
}