package com.xiaojing.shop.adapter;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.daidingkang.SnapUpCountDownTimerView;
import com.squareup.picasso.Picasso;
import com.wuzhanglong.library.adapter.RecyclerBaseAdapter;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.xiaojing.shop.R;
import com.xiaojing.shop.mode.OneShopVO;

import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * Created by Administrator on 2017/2/13.
 */

public class OneShopLotteryingAdapter extends RecyclerBaseAdapter<OneShopVO> {
    public OneShopLotteryingAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.one_shop_lotterying_adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initData(BGAViewHolderHelper helper, int position, Object model) {

        OneShopVO vo= (OneShopVO) model;
        Picasso.with(mContext).load(vo.getGoods_image()).into(helper.getImageView(R.id.img));
        helper.setText(R.id.title_tv,vo.getGoods_name());
        helper.setText(R.id.number_tv,"本期期号："+vo.getOd_no());
        SnapUpCountDownTimerView downTimerView =  helper.getView(R.id.timer_view);
        int hour = Math.abs(BaseCommonUtils.parseInt(vo.getCountdown())) / 3600;
        int min =Math.abs(BaseCommonUtils.parseInt(vo.getCountdown())) / 60 - hour * 60;
        int sec = Math.abs(BaseCommonUtils.parseInt(vo.getCountdown())) - min * 60 - hour * 60 * 60;
        downTimerView.setTime(hour, min, sec);
        downTimerView.start();
        View view =helper.getView(R.id.item_layout);
        view.setBackground(BaseCommonUtils.setBackgroundShap(mContext,8,R.color.C3_1,R.color.C1));
    }


}
