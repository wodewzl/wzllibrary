package com.xiaojing.shop.adapter;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.wuzhanglong.library.adapter.RecyclerBaseAdapter;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.xiaojing.shop.R;
import com.xiaojing.shop.mode.ShopVO;

import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * Created by Administrator on 2017/2/13.
 */

public class ShopPromotionsAdapter extends RecyclerBaseAdapter<ShopVO> {

    public ShopPromotionsAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.shop_promotions_adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initData(BGAViewHolderHelper helper, int position, Object model) {
        ShopVO vo= (ShopVO) model;
        Picasso.with(mContext).load(vo.getArticle_cover()).into(helper.getImageView(R.id.img));
        helper.setText(R.id.title_tv,vo.getArticle_title());
        helper.setText(R.id.desc_tv,vo.getArticle_desc());
//        helper.setText(R.id.time_tv,"活动时间："+vo.getStart_time()+" 至 "+vo.getEnd_time());
//        helper.setText(R.id.read_tv,"阅读量："+vo.getArticle_view());
        BaseCommonUtils.setTextTwoLast(mActivity,helper.getTextView(R.id.read_tv),"阅读量：",vo.getArticle_view(),R.color.C7,1.0f);
        BaseCommonUtils.setTextTwoLast(mActivity,helper.getTextView(R.id.time_tv),"活动时间：",vo.getStart_time()+" 至 "+vo.getEnd_time(),R.color.C7,1.0f);
    }


}
