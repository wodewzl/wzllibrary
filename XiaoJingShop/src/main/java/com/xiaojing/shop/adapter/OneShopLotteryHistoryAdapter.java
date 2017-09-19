package com.xiaojing.shop.adapter;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.wuzhanglong.library.adapter.RecyclerBaseAdapter;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.xiaojing.shop.R;
import com.xiaojing.shop.mode.OneShopVO;

import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * Created by Administrator on 2017/2/13.
 */

public class OneShopLotteryHistoryAdapter extends RecyclerBaseAdapter<OneShopVO> {

    public OneShopLotteryHistoryAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.one_shop_lottery_adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initData(BGAViewHolderHelper helper, int position, Object model) {
        OneShopVO vo = (OneShopVO) model;
        Picasso.with(mContext).load(vo.getMember_avatar_url()).into(helper.getImageView(R.id.head_img));
        helper.setText(R.id.tv_01, "期号：" + vo.getOd_no());
        helper.setText(R.id.tv_02, "揭晓时间：" + vo.getPublish_date());
        BaseCommonUtils.setTextThree(mContext, helper.getTextView(R.id.tv_03), "获奖者：", vo.getMember_name(), " (ID:" + vo.getMember_id() + ")", R.color.XJColor9, 1.0f);
        BaseCommonUtils.setTextTwoLast(mContext, helper.getTextView(R.id.tv_04), "用户IP：" + vo.getMember_ip(), "(" + vo.getProvince() + ")", R.color.XJColor10, 1.0f);
        BaseCommonUtils.setTextTwoLast(mContext, helper.getTextView(R.id.tv_05), "幸运号码："  , vo.getPrize_code(), R.color.XJColor10, 1.0f);
        BaseCommonUtils.setTextThree(mContext, helper.getTextView(R.id.tv_06), "本期参与：", vo.getBuy_count(), "人次", R.color.XJColor2, 1.0f);
    }

}
