package com.xiaojing.shop.adapter;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.squareup.picasso.Picasso;
import com.wuzhanglong.library.adapter.RecyclerBaseAdapter;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.xiaojing.shop.R;
import com.xiaojing.shop.activity.OneShopShowListActivity;
import com.xiaojing.shop.mode.OneShopVO;

import java.util.ArrayList;

import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;

/**
 * Created by Administrator on 2017/2/13.
 */

public class OneShopShowListAdapter extends RecyclerBaseAdapter<OneShopVO> {

    public OneShopShowListAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.show_shop_adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initData(BGAViewHolderHelper helper, int position, Object model) {
        OneShopVO vo = (OneShopVO) model;
        Picasso.with(mContext).load(vo.getMember_avatar_url()).into(helper.getImageView(R.id.head_img));
        helper.setText(R.id.tv_01, vo.getMember_name());
        helper.setText(R.id.tv_02, vo.getShare_date());
        helper.setText(R.id.tv_03, vo.getGoods_name());
        helper.setText(R.id.tv_04, "期号：" + vo.getOd_no());
        helper.setText(R.id.tv_05, vo.getShare_content());
        View view = helper.getView(R.id.content_layout);
        view.setBackground(BaseCommonUtils.setBackgroundShap(mActivity, 8, R.color.C3, R.color.C3));
        BGANinePhotoLayout photoLayout=helper.getView(R.id.photo_layout);
        photoLayout.setData((ArrayList<String>) vo.getImgs());
        photoLayout.setDelegate((OneShopShowListActivity)mContext);
    }


}
