package com.xiaojing.shop.adapter;

import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.wuzhanglong.library.adapter.RecyclerBaseAdapter;
import com.xiaojing.shop.R;
import com.xiaojing.shop.mode.ShopVO;

import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * Created by Administrator on 2017/2/13.
 */

public class ShopListRadapter extends RecyclerBaseAdapter<ShopVO> {

    public ShopListRadapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.shop_list_adapter);
    }

    @Override
    public void initData(BGAViewHolderHelper helper, int position, Object model) {
        ShopVO vo= (ShopVO) model;
        ImageView img = helper.getImageView(R.id.shop_img);
        Picasso.with(mContext).load(vo.getGoods_image_url()).placeholder(R.drawable.img_default).into(img);
        helper.setText(R.id.title_tv,vo.getGoods_name());
        helper.setText(R.id.price_tv,"￥"+vo.getGoods_price());
        helper.setText(R.id.number_tv,"销量："+vo.getGoods_salenum());

    }


}
