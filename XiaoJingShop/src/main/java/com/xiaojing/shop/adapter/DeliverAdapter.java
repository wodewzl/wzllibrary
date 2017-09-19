package com.xiaojing.shop.adapter;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.wuzhanglong.library.adapter.RecyclerBaseAdapter;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.xiaojing.shop.R;
import com.xiaojing.shop.mode.AddressVO;
import com.xiaojing.shop.mode.ShopVO;

import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * Created by Administrator on 2017/2/13.
 */

public class DeliverAdapter extends RecyclerBaseAdapter<ShopVO> {
    private AddressVO mDefalutVO;

    public DeliverAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.deliver_adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initData(BGAViewHolderHelper helper, int position, Object model) {
        ShopVO vo = (ShopVO) model;
        helper.setText(R.id.address_tv, vo.getDeliver_info()[position]);
        TextView tv = helper.getTextView(R.id.state_bg);
        tv.setBackground(BaseCommonUtils.setBackgroundShap(mContext, 100, R.color.C7, R.color.C7));
    }


}
