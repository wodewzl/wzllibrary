package com.beisheng.easycar.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.beisheng.easycar.R;
import com.beisheng.easycar.mode.NeaybyVO;
import com.wuzhanglong.library.adapter.RecyclerBaseAdapter;

import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * Created by ${Wuzhanglong} on 2017/5/16.
 */

public class NearbyLeftAdapter extends RecyclerBaseAdapter {
    public NearbyLeftAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.adapter_nearby_left);
    }

    public interface OnLeftSelectedListener {
        void onLeftItemSelected(NeaybyVO neaybyVO);
        void moveToTop(View view, int position);
    }

    private NeaybyVO mSelectVO;
    public OnLeftSelectedListener listener;


    @Override
    public void initData(final BGAViewHolderHelper helper, final int position, Object model) {
        final NeaybyVO vo = (NeaybyVO) model;
        helper.setText(R.id.left_menu_textview, vo.getName());
        final FrameLayout leftMenu = (FrameLayout) helper.getConvertView();
        if (mSelectVO == null && position == 0) {
            mSelectVO = vo;
            vo.setSelect(true);
            helper.setVisibility(R.id.left_view, View.VISIBLE);
            leftMenu.setBackgroundColor(mContext.getResources().getColor(R.color.C3));
        }

        if (vo.isSelect()) {
            helper.setVisibility(R.id.left_view, View.VISIBLE);
            helper.setBackgroundColorRes(R.id.left_menu_textview, R.color.C1);
            helper.setTextColorRes(R.id.left_menu_textview, R.color.C4);
        } else {
            helper.setVisibility(R.id.left_view, View.GONE);
            helper.setBackgroundColorRes(R.id.left_menu_textview, R.color.C3);
            helper.setTextColorRes(R.id.left_menu_textview, R.color.C5);
        }

        leftMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectVO != null)
                    mSelectVO.setSelect(false);
                mSelectVO = vo;
                mSelectVO.setSelect(true);
                notifyDataSetChanged();
                listener.onLeftItemSelected(vo);
                listener.moveToTop(view,position);
            }
        });

    }

    public void setListener(OnLeftSelectedListener listener) {
        this.listener = listener;
    }

    public OnLeftSelectedListener getListener() {
        return listener;
    }

}
