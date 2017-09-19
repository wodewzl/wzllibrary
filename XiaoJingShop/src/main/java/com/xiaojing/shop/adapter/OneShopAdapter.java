package com.xiaojing.shop.adapter;

import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;
import com.wuzhanglong.library.adapter.RecyclerBaseAdapter;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.xiaojing.shop.R;
import com.xiaojing.shop.mode.OneShopVO;

import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * Created by Administrator on 2017/2/13.
 */

public class OneShopAdapter extends RecyclerBaseAdapter<OneShopVO> {
    public OneShopAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.one_shop_adapter);
    }

    @Override
    public void initData(BGAViewHolderHelper helper, int position, Object model) {
        OneShopVO vo = (OneShopVO) model;
        Picasso.with(mContext).load(vo.getGoods_info().getGoods_image()).placeholder(R.drawable.img_default).into(helper.getImageView(R.id.img));
        helper.setText(R.id.name, vo.getGoods_info().getGoods_name());
        BaseCommonUtils.setTextTwoBefore(mContext, helper.getTextView(R.id.progress_tv), "抢宝进度", vo.getOd_progress(), R.color.C5, 1.0f);
        ProgressBar pb = helper.getView(R.id.progress_bar);
        pb.setProgress(Integer.parseInt(vo.getOd_progress().split("%")[0]));
    }


}
