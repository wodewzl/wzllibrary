package com.beisheng.easycar.adapter;

import android.support.v7.widget.RecyclerView;

import com.beisheng.easycar.R;
import com.beisheng.easycar.mode.MyMoneyVO;
import com.wuzhanglong.library.adapter.RecyclerBaseAdapter;

import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * Created by Administrator on 2017/2/13.
 */

public class MyMoneyAdapter extends RecyclerBaseAdapter {

    public MyMoneyAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.adapter_my_money_type2);
    }

    @Override
    public void initData(final BGAViewHolderHelper helper, int position, Object model) {
        final MyMoneyVO vo = (MyMoneyVO) model;
        if (vo.getId() != null) {
            helper.setText(R.id.type_tv, vo.getTitle());
            helper.setText(R.id.desc_tv, vo.getUpdate_time());
            if (Double.parseDouble(vo.getMoney()) > 0) {
                helper.setText(R.id.money_tv, "+" + vo.getMoney());
                helper.setTextColorRes(R.id.money_tv, R.color.Car7);
            } else {
                helper.setText(R.id.money_tv, "-" + vo.getMoney());
                helper.setTextColorRes(R.id.money_tv, R.color.Car6);
            }
            helper.setText(R.id.over_tv, "余额：￥" + vo.getBalance());

        } else {
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (mData.size() == 0)
            return super.getItemViewType(position);
        MyMoneyVO vo = (MyMoneyVO) mData.get(position);
        if (vo.getId() != null) {
            return R.layout.adapter_my_over__type1;
        } else {
            return R.layout.adapter_my_money_type2;
        }
    }

}
