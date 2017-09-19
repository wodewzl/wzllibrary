package com.xiaojing.shop.adapter;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.rey.material.widget.Button;
import com.squareup.picasso.Picasso;
import com.wuzhanglong.library.adapter.RecyclerBaseAdapter;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.xiaojing.shop.R;
import com.xiaojing.shop.activity.OneShopDetailActivity;
import com.xiaojing.shop.mode.OneShopVO;

import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * Created by Administrator on 2017/2/13.
 */

public class OneShopOrderAdapter1 extends RecyclerBaseAdapter<OneShopVO> {
    OnPayClick onPayClick;

    public interface OnPayClick {
        //type 1 if_cancel 2 if_payment 3 if_receive 4 if_deliver 5 if_delete 6 if_evaluation 7if_refund_cancel
        void pay(OneShopVO vo);
    }

    public OneShopOrderAdapter1(RecyclerView recyclerView) {
        super(recyclerView, R.layout.one_shop_order_adapter1);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initData(BGAViewHolderHelper helper, int position, Object model) {
        final OneShopVO vo = (OneShopVO) model;
        Picasso.with(mContext).load(vo.getGoods_image()).placeholder(R.drawable.img_default).into(helper.getImageView(R.id.img));
        helper.setText(R.id.title_tv, vo.getGoods_name());
        helper.setText(R.id.number_tv, vo.getOd_no());
        BaseCommonUtils.setTextThree(mContext, helper.getTextView(R.id.buy_cout), "我已参与：", vo.getMy_count(), "人次", R.color.XJColor2, 1.0f);
        BaseCommonUtils.setTextThree(mContext, helper.getTextView(R.id.un_pay), "未付款：", vo.getUnpay_count(), "人次", R.color.XJColor2, 1.0f);

        helper.setText(R.id.progress_desc, "抢宝进度 " + vo.getOd_progress() + "%");
        ProgressBar progressBar = helper.getView(R.id.progress_bar);
        progressBar.setProgress(BaseCommonUtils.parseInt(vo.getOd_progress()));

        Button button = helper.getView(R.id.state_bt);

        if ("0".equals(vo.getUnpay_count())) {
            button.setText("继续抢购");
            button.setBackground(BaseCommonUtils.setBackgroundShap(mContext, 5, R.color.XJColor2, R.color.XJColor2));
        } else {
            button.setText("去付款");
            button.setBackground(BaseCommonUtils.setBackgroundShap(mContext, 5, R.color.XJColor4, R.color.XJColor4));
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("0".equals(vo.getUnpay_count())) {
                    Bundle bundle = new Bundle();
                    bundle.putString("od_id", vo.getOd_id());
                    mActivity.open(OneShopDetailActivity.class, bundle, 0);
                } else {
                    onPayClick.pay(vo);
                }

            }
        });
    }


    public OnPayClick getOnPayClick() {
        return onPayClick;
    }

    public void setOnPayClick(OnPayClick onPayClick) {
        this.onPayClick = onPayClick;
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
