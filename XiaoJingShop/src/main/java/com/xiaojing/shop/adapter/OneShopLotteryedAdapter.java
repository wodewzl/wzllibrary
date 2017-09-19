package com.xiaojing.shop.adapter;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.squareup.picasso.Picasso;
import com.wuzhanglong.library.adapter.RecyclerBaseAdapter;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.xiaojing.shop.R;
import com.xiaojing.shop.mode.OneShopVO;

import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * Created by Administrator on 2017/2/13.
 */

public class OneShopLotteryedAdapter extends RecyclerBaseAdapter<OneShopVO> {
    public OneShopLotteryedAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.one_shop_lotteryed_adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initData(BGAViewHolderHelper helper, int position, Object model) {
        OneShopVO vo= (OneShopVO) model;
        Picasso.with(mContext).load(vo.getGoods_image()).into(helper.getImageView(R.id.img));
        helper.setText(R.id.tv_01,vo.getGoods_name());
        helper.setText(R.id.tv_02,"本期期号："+vo.getOd_no());
        BaseCommonUtils.setTextTwoLast(mContext,helper.getTextView(R.id.tv_03),"获奖者：",vo.getMember_name(),R.color.C7,1.2f);
        BaseCommonUtils.setTextThree(mContext,helper.getTextView(R.id.tv_04),"本期参与：",vo.getBuy_count(),"人次",R.color.XJColor4,1.2f);
        helper.setText(R.id.tv_05,"揭晓时间："+vo.getPublish_date());
        View view =helper.getView(R.id.item_layout);
        view.setBackground(BaseCommonUtils.setBackgroundShap(mContext,8,R.color.C3_1,R.color.C1));
    }


}
