package com.xiaojing.shop.adapter;

import android.support.v7.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.wuzhanglong.library.adapter.RecyclerBaseAdapter;
import com.xiaojing.shop.R;

import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * Created by Administrator on 2017/2/13.
 */

public class GameDetailAdapter extends RecyclerBaseAdapter<String> {

    public GameDetailAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.game_detail_adapter);
    }

    @Override
    public void initData(BGAViewHolderHelper helper, int position, Object model) {
        String url= (String) model;
        Picasso.with(mActivity).load(url).into(helper.getImageView(R.id.img));

    }


}
