package com.xiaojing.shop.adapter;

import android.support.v7.widget.RecyclerView;

import com.wuzhanglong.library.adapter.RecyclerBaseAdapter;
import com.xiaojing.shop.R;
import com.xiaojing.shop.mode.AddressVO;

import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * Created by Administrator on 2017/2/13.
 */

public class AddressSelectAdapter extends RecyclerBaseAdapter<AddressVO> {
    private AddressVO mDefalutVO;

    public AddressSelectAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.address_select_adapter);
    }

    @Override
    public void initData(BGAViewHolderHelper helper, int position, Object model) {
        AddressVO vo = (AddressVO) model;
        helper.setText(R.id.name_tv, vo.getTrue_name());
        helper.setText(R.id.phone_tv, vo.getMob_phone());
        helper.setText(R.id.address_tv, vo.getArea_info() + vo.getAddress());

    }


}
