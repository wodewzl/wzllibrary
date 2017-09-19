package com.xiaojing.shop.adapter;

import android.support.v7.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.wuzhanglong.library.adapter.RecyclerBaseAdapter;
import com.xiaojing.shop.R;
import com.xiaojing.shop.mode.ShopVO;

import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * Created by Administrator on 2017/2/13.
 */

public class HistoryShopRadapter extends RecyclerBaseAdapter<ShopVO> {

    public HistoryShopRadapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.history_shop_adapter);
    }

    @Override
    public void initData(BGAViewHolderHelper helper, int position, Object model) {
        ShopVO vo= (ShopVO) model;
        helper.setText(R.id.title_tv,vo.getGoods_name());
        Picasso.with(mContext).load(vo.getGoods_image_url()).placeholder(R.drawable.img_default).into(helper.getImageView(R.id.shop_img));
        helper.setText(R.id.price_tv,"￥"+vo.getGoods_price());
    }

}
