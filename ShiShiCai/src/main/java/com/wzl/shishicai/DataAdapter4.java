package com.wzl.shishicai;

import android.support.v7.widget.RecyclerView;

import com.wuzhanglong.library.adapter.RecyclerBaseAdapter;

import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

public class DataAdapter4 extends RecyclerBaseAdapter {
    public DataAdapter4(RecyclerView recyclerView) {
        super(recyclerView, R.layout.data_item_adapter);
    }

    @Override
    public void initData(BGAViewHolderHelper helper, int position, Object model) {
        DataVO dataVO = (DataVO) model;
        helper.setText(R.id.text_02, dataVO.getResult());
        helper.setText(R.id.text_03, dataVO.getYuce3()+dataVO.getNumber4());
        helper.setText(R.id.text_04, dataVO.getYuce4());
        helper.setText(R.id.text_05, dataVO.getYuce5());
        helper.setText(R.id.text_06, dataVO.getYuce6());
    }

}
