package com.xiaojing.shop.adapter;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.rey.material.widget.Button;
import com.squareup.picasso.Picasso;
import com.wuzhanglong.library.adapter.RecyclerBaseAdapter;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.xiaojing.shop.R;
import com.xiaojing.shop.activity.DeliverActivity;
import com.xiaojing.shop.activity.OneShopShowActivity;
import com.xiaojing.shop.mode.OneShopVO;

import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * Created by Administrator on 2017/2/13.
 */

public class OneShopOrderAdapter4 extends RecyclerBaseAdapter<OneShopVO> {
    public OneShopOrderAdapter4(RecyclerView recyclerView) {
        super(recyclerView, R.layout.one_shop_order_adapter4);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initData(final BGAViewHolderHelper helper, int position, Object model) {
        final OneShopVO vo = (OneShopVO) model;
        helper.setText(R.id.order_state, vo.getShip_state_text());
        Picasso.with(mContext).load(vo.getGoods_image()).placeholder(R.drawable.img_default).into(helper.getImageView(R.id.img));
        helper.setText(R.id.title_tv, vo.getGoods_name());
        helper.setText(R.id.number_tv, vo.getOd_no());
        BaseCommonUtils.setTextThree(mContext, helper.getTextView(R.id.buy_cout), "我已参与：", vo.getMy_count(), "人次", R.color.XJColor2, 1.0f);
//        helper.setText(R.id.win_name, "获奖者：" + vo.getMember_name());
        BaseCommonUtils.setTextThree(mContext, helper.getTextView(R.id.win_name), "参与人数：", vo.getJoin_member_count(), "人", R.color.XJColor2, 1.0f);

        Button button = helper.getView(R.id.state_bt);
        if ("0".equals(vo.getIf_share())) {
            button.setBackground(BaseCommonUtils.setBackgroundShap(mContext, 5, R.color.C3, R.color.C3));
            button.setText("已分享");
            button.setTextColor(ContextCompat.getColor(mContext, R.color.C5));
        } else {
            button.setBackground(BaseCommonUtils.setBackgroundShap(mContext, 5, R.color.XJColor4, R.color.XJColor4));
            button.setText("分享晒单");
            button.setTextColor(ContextCompat.getColor(mContext, R.color.C1));
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("0".equals(vo.getIf_share()))
                    return;
                OneShopShowActivity.startAction(mContext, helper.getImageView(R.id.img), vo.getOd_id(), vo.getGoods_image(), vo.getGoods_name(), vo.getOd_no());
            }
        });

        Button diliverBt = helper.getView(R.id.diliver_bt);
        if("1".equals(vo.getIf_deliver())){
            diliverBt.setVisibility(View.VISIBLE);
            diliverBt.setBackground(BaseCommonUtils.setBackgroundShap(mContext, 5, R.color.XJColor4, R.color.XJColor4));

            diliverBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "2");
                    bundle.putString("order_id", vo.getOrder_id());
                    mActivity.open(DeliverActivity.class, bundle, 0);
                }
            });
        }else {
            diliverBt.setVisibility(View.GONE);
        }
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
