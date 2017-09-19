package com.xiaojing.shop.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.wuzhanglong.library.adapter.RecyclerBaseAdapter;
import com.xiaojing.shop.R;
import com.xiaojing.shop.mode.MoneyVO;

import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * Created by Administrator on 2017/2/13.
 */

public class MyOverAdapter extends RecyclerBaseAdapter<MoneyVO> {
    onTypeClickListener onTypeClickListener;

    public interface onTypeClickListener {
        void typeClick (String type);
    }
    public MyOverAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.my_over_adapter_type2);
    }

    @Override
    public void initData(final BGAViewHolderHelper helper, int position, Object model) {
        final MoneyVO vo = (MoneyVO) model;
        if(vo.getLg_id() !=null){
            helper.setText(R.id.type_tv, vo.getLg_desc());
            helper.setText(R.id.desc_tv, vo.getLg_add_time());
            if(Double.parseDouble(vo.getLg_av_amount())>0){
                helper.setText(R.id.money_tv, "+"+vo.getLg_av_amount());
            }else{
                helper.setText(R.id.money_tv, vo.getLg_av_amount());
            }
            helper.setText(R.id.over_tv, "余额：￥"+vo.getLg_balance());

        }else {
            helper.setItemChildClickListener(R.id.income_tv);
            helper.setItemChildClickListener(R.id.all_tv);
            helper.setItemChildClickListener(R.id.pay_tv);
            helper.setOnItemChildClickListener(new BGAOnItemChildClickListener() {
                @Override
                public void onItemChildClick(ViewGroup viewGroup, View v, int i) {
                    switch (v.getId()) {
                        case R.id.all_tv:
                            helper.setTextColorRes(R.id.all_tv, R.color.C7);
                            helper.setTextColorRes(R.id.income_tv, R.color.C4);
                            helper.setTextColorRes(R.id.pay_tv, R.color.C4);
                            onTypeClickListener.typeClick("0");
                            break;
                        case R.id.income_tv:
                            helper.setTextColorRes(R.id.all_tv, R.color.C4);
                            helper.setTextColorRes(R.id.income_tv, R.color.C7);
                            helper.setTextColorRes(R.id.pay_tv, R.color.C4);
                            onTypeClickListener.typeClick("1");
                            break;
                        case R.id.pay_tv:
                            helper.setTextColorRes(R.id.all_tv, R.color.C4);
                            helper.setTextColorRes(R.id.income_tv, R.color.C4);
                            helper.setTextColorRes(R.id.pay_tv, R.color.C7);
                            onTypeClickListener.typeClick("2");
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }



    @Override
    public int getItemViewType(int position) {
        if (mData.size() == 0)
            return super.getItemViewType(position);
        MoneyVO vo = (MoneyVO) mData.get(position);
        if (vo.getLg_id() != null) {
            return R.layout.my_over_adapter_type1;
        } else {
            return R.layout.my_over_adapter_type2;
        }
    }

    public MyOverAdapter.onTypeClickListener getOnTypeClickListener() {
        return onTypeClickListener;
    }

    public void setOnTypeClickListener(MyOverAdapter.onTypeClickListener onTypeClickListener) {
        this.onTypeClickListener = onTypeClickListener;
    }
}
