package com.xiaojing.shop.adapter;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;

import com.rey.material.widget.Button;
import com.squareup.picasso.Picasso;
import com.wuzhanglong.library.adapter.RecyclerBaseAdapter;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.xiaojing.shop.R;
import com.xiaojing.shop.mode.HomeVO;
import com.xiaojing.shop.mode.OneShopVO;

import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * Created by Administrator on 2017/2/13.
 */

public class OneShopOrderAdapter2 extends RecyclerBaseAdapter<HomeVO> {
    public OneShopOrderAdapter2(RecyclerView recyclerView) {
        super(recyclerView, R.layout.one_shop_order_adapter2);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initData(BGAViewHolderHelper helper, int position, Object model) {
        OneShopVO vo = (OneShopVO) model;
        Picasso.with(mContext).load(vo.getGoods_image()).placeholder(R.drawable.img_default).into(helper.getImageView(R.id.img));
        helper.setText(R.id.title_tv, vo.getGoods_name());
        helper.setText(R.id.number_tv, vo.getOd_no());
        BaseCommonUtils.setTextThree(mContext, helper.getTextView(R.id.buy_cout), "我已参与：", vo.getOd_buy_count(), "人次", R.color.XJColor2, 1.0f);
        Button button = helper.getView(R.id.state_bt);
        button.setBackground(BaseCommonUtils.setBackgroundShap(mContext,5,R.color.XJColor2,R.color.XJColor2));

    }
    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, Object model) {
        if (mData.size() > 0) {
            super.fillData(helper, position, model);
        } else {
            mBaseAdapterTv = helper.getTextView(R.id.adapter_no_content_tv);
            mBaseAdapterTv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.img_default, 0, 0);
            mBaseAdapterTv.setText("暂无相关订单哦");
        }

    }
}
