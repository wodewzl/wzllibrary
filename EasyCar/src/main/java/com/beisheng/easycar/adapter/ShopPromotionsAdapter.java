package com.beisheng.easycar.adapter;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;

import com.beisheng.easycar.R;
import com.beisheng.easycar.mode.PromotionsVO;
import com.squareup.picasso.Picasso;
import com.wuzhanglong.library.adapter.RecyclerBaseAdapter;

import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * Created by Administrator on 2017/2/13.
 */

public class ShopPromotionsAdapter extends RecyclerBaseAdapter {

    public ShopPromotionsAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.shop_promotions_adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initData(BGAViewHolderHelper helper, int position, Object model) {
        PromotionsVO vo= (PromotionsVO) model;
        Picasso.with(mContext).load(vo.getImg()).into(helper.getImageView(R.id.img));
        helper.setText(R.id.title_tv,vo.getTitle());
        helper.setText(R.id.desc_tv,vo.getDesc());
//        BaseCommonUtils.setTextTwoLast(mActivity,helper.getTextView(R.id.time_tv),"活动时间：",vo.getStart_time()+" 至 "+vo.getEnd_time(),R.color.C7,1.0f);
        helper.setText(R.id.time_tv,"活动时间："+vo.getActivity());
    }


}
