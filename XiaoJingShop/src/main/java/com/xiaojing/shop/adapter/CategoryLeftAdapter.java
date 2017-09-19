package com.xiaojing.shop.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.wuzhanglong.library.adapter.RecyclerBaseAdapter;
import com.xiaojing.shop.R;
import com.xiaojing.shop.mode.CategoryVO;

import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * Created by cheng on 16-11-10.
 */
public class CategoryLeftAdapter extends RecyclerBaseAdapter<CategoryVO> {

    public interface OnLeftSelectedListener {
         void onLeftItemSelected(CategoryVO menu);
        void moveToTop(View view,int position);
    }

    private CategoryVO mSelectVO;
    public OnLeftSelectedListener listener;


    public CategoryLeftAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.category_adapter_left);
    }

    @Override
    public void initData(final BGAViewHolderHelper helper, final int position, Object model) {
        final CategoryVO vo = (CategoryVO) model;
        helper.setText(R.id.left_menu_textview, vo.getGc_name());
        final LinearLayout leftMenu = (LinearLayout) helper.getConvertView();
        if (mSelectVO == null && position == 0) {
            mSelectVO = vo;
            vo.setSelect(true);
            helper.setVisibility(R.id.left_view, View.VISIBLE);
            leftMenu.setBackgroundColor(mContext.getResources().getColor(R.color.C3));
        }

        if (vo.isSelect()) {
            helper.setVisibility(R.id.left_view, View.VISIBLE);
            helper.setBackgroundColorRes(R.id.left_menu_textview, R.color.C3);
            helper.setTextColorRes(R.id.left_menu_textview, R.color.XJColor2);
        } else {
            helper.setVisibility(R.id.left_view, View.GONE);
            helper.setBackgroundColorRes(R.id.left_menu_textview, R.color.C1);
            helper.setTextColorRes(R.id.left_menu_textview, R.color.C4);
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
