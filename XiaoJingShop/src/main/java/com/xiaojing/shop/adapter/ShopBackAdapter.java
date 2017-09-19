package com.xiaojing.shop.adapter;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wuzhanglong.library.adapter.RecyclerBaseAdapter;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.xiaojing.shop.R;
import com.xiaojing.shop.activity.WebViewActivity;
import com.xiaojing.shop.mode.OrderVO;

import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * Created by Administrator on 2017/2/13.
 */

public class ShopBackAdapter extends RecyclerBaseAdapter<OrderVO> {
    onTypeClickListener onTypeClickListener;

    public interface onTypeClickListener {
        //type 1 ship_state 2 delay_state
        void typeClick(String type, OrderVO vo);
    }


    public ShopBackAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.order_adapter_type2);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initData(BGAViewHolderHelper helper, int position, final Object model) {
        final OrderVO vo = (OrderVO) model;
        if (vo.isOrder()) {
            helper.setText(R.id.shop_people_name_tv, vo.getStore_name());
            helper.setText(R.id.order_state, vo.getState_desc());
        } else if (vo.getGoods_list() != null) {
            BaseCommonUtils.setTextTwoLast(mContext, (TextView) helper.getView(R.id.price_tv), "退款金额：", "￥" + vo.getRefund_amount(), R.color.XJColor2, 1.2f);
            helper.setText(R.id.time_tv, vo.getAdd_time());


            if ("1".equals(vo.getShip_state())) {
                helper.getTextView(R.id.action_tv_01).setBackground(BaseCommonUtils.setBackgroundShap(mContext, 5, R.color.XJColor2, R.color.C3));
                helper.getTextView(R.id.action_tv_01).setVisibility(View.VISIBLE);
            } else {
                helper.getTextView(R.id.action_tv_01).setVisibility(View.GONE);
            }
            if ("1".equals(vo.getDelay_state())) {
                helper.getTextView(R.id.action_tv_02).setBackground(BaseCommonUtils.setBackgroundShap(mContext, 5, R.color.XJColor2, R.color.C3));
                helper.getTextView(R.id.action_tv_02).setVisibility(View.VISIBLE);
            } else {
                helper.getTextView(R.id.action_tv_02).setVisibility(View.GONE);
            }
            helper.setItemChildClickListener(R.id.action_tv_01);
            helper.setItemChildClickListener(R.id.action_tv_02);
            helper.setOnItemChildClickListener(new BGAOnItemChildClickListener() {
                @Override
                public void onItemChildClick(ViewGroup viewGroup, View v, int i) {
                    switch (v.getId()) {
                        case R.id.action_tv_01:
                            Bundle bundle= new Bundle();
                            bundle.putString("url",vo.getShip_url());
                            mActivity.open(WebViewActivity.class,bundle,0);
                            break;
                        case R.id.action_tv_02:
                            onTypeClickListener.typeClick("2",vo);
                            break;
                        default:
                            break;
                    }
                }
            });
        } else {
            Picasso.with(mContext).load(vo.getGoods_img()).placeholder(R.drawable.img_default).into(helper.getImageView(R.id.img));
            helper.setText(R.id.name_tv, vo.getGoods_name());
            helper.setText(R.id.price_tv, "单价：￥" + vo.getGoods_price());
//            helper.setText(R.id.time_tv, "X" + vo.getGoods_num());
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (mData.size() == 0)
            return super.getItemViewType(position);
        OrderVO vo = (OrderVO) mData.get(position);
        if (vo.isOrder()) {
            return R.layout.order_adapter_type1;
        } else if (vo.getGoods_list() != null) {
            return R.layout.shop_back_adapter_type3;
        } else {
            return R.layout.shop_back_adapter;
        }

    }

    public ShopBackAdapter.onTypeClickListener getOnTypeClickListener() {
        return onTypeClickListener;
    }

    public void setOnTypeClickListener(ShopBackAdapter.onTypeClickListener onTypeClickListener) {
        this.onTypeClickListener = onTypeClickListener;
    }
}
