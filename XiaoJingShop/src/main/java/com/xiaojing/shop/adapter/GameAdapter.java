package com.xiaojing.shop.adapter;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wuzhanglong.library.adapter.RecyclerBaseAdapter;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.xiaojing.shop.R;
import com.xiaojing.shop.mode.GameVO;

import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Created by Administrator on 2017/2/13.
 */

public class GameAdapter extends RecyclerBaseAdapter<GameVO> {

    public GameAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.game_adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initData(BGAViewHolderHelper helper, int position, Object model) {
        GameVO vo= (GameVO) model;
        MaterialRatingBar ratingBar=helper.getView(R.id.rating_bar);
        ratingBar.setEnabled(false);
        ratingBar.setRating(BaseCommonUtils.parseInt(vo.getGame_star()));
        helper.setText(R.id.name_tv,vo.getGame_name());
        BaseCommonUtils.setTextThree(mActivity,helper.getTextView(R.id.price_tv),"房卡：",vo.getGame_card_price(),"元/张",R.color.XJColor2,1.2f);
        Picasso.with(mActivity).load(vo.getGame_logo()).placeholder(R.drawable.img_default).into(helper.getImageView(R.id.img));
        TextView detailTv= helper.getTextView(R.id.detail_tv);
        detailTv.setBackground(BaseCommonUtils.setBackgroundShap(mActivity,5,R.color.XJColor5,R.color.C1));
    }

}
