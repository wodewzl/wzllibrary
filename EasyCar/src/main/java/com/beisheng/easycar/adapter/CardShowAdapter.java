package com.beisheng.easycar.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.beisheng.easycar.R;
import com.beisheng.easycar.mode.PayTypeVO;
import com.squareup.picasso.Picasso;
import com.wuzhanglong.library.adapter.RecyclerBaseAdapter;

import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * Created by Administrator on 2017/2/13.
 */

public class CardShowAdapter extends RecyclerBaseAdapter{

    public CardShowAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.card_show_adapter);
    }

    @Override
    public void initData(BGAViewHolderHelper helper, int position, Object model) {
        final PayTypeVO vo= (PayTypeVO) model;
        helper.setText(R.id.name_tv,vo.getBank_name());
        Picasso.with(mContext).load(vo.getBank_image()).into(helper.getImageView(R.id.img));

//        helper.setChecked(R.id.check_box,vo.isCheck());
        if(vo.getCard_no() !=null){
            helper.setText(R.id.number_tv,"尾号"+vo.getCard_no()+"储蓄卡");
            helper.setVisibility(R.id.number_tv, View.VISIBLE);
        }else{
            helper.setVisibility(R.id.number_tv, View.GONE);
        }
        if(vo.isCheck()){
            helper.setImageResource(R.id.check_img,R.drawable.check_select);
        }else{
            helper.setImageResource(R.id.check_img,R.drawable.check_normal);
        }

    }

}
