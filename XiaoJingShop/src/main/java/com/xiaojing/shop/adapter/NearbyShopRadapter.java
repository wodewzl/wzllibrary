package com.xiaojing.shop.adapter;

import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.wuzhanglong.library.adapter.RecyclerBaseAdapter;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.xiaojing.shop.R;
import com.xiaojing.shop.mode.NearbyShopVO;

import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Created by Administrator on 2017/2/13.
 */

public class NearbyShopRadapter extends RecyclerBaseAdapter<NearbyShopVO> {

    public NearbyShopRadapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.nearby_shop_adapter);
    }

    @Override
    public void initData(BGAViewHolderHelper helper, int position, Object model) {
        NearbyShopVO vo= (NearbyShopVO) model;
        ImageView img = helper.getImageView(R.id.shop_img);
        Picasso.with(mContext).load(vo.getImg_url()).into(img);
        helper.setText(R.id.title_tv,vo.getMerchant_name());
        MaterialRatingBar ratingBar= helper.getView(R.id.rating_bar);
        ratingBar.setRating(BaseCommonUtils.parseInt(vo.getMerchant_star()));
//        ratingBar.setEnabled(false);
        helper.setText(R.id.desc_tv,vo.getMerchant_class_text());
        helper.setText(R.id.distance_tv,vo.getDistance()+"KM");

    }

}
