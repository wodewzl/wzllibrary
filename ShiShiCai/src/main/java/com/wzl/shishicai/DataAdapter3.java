package com.wzl.shishicai;

import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.wuzhanglong.library.adapter.RecyclerBaseAdapter;

import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

public class DataAdapter3 extends RecyclerBaseAdapter {
    public DataAdapter3(RecyclerView recyclerView) {
        super(recyclerView, R.layout.data_item_adapter);
    }

    @Override
    public void initData(BGAViewHolderHelper helper, int position, Object model) {
        DataVO dataVO = (DataVO) model;
        helper.setText(R.id.text_02, dataVO.getResult());
        helper.setText(R.id.text_03, dataVO.getYuce3()+dataVO.getNumber3());
//        helper.setText(R.id.text_03, dataVO.getMaxYiLou());
//                helper.setText(R.id.text_03, dataVO.getRepeat());
        helper.setText(R.id.text_04, dataVO.getYuce5()+dataVO.getNumber5());
//        helper.setText(R.id.text_04, dataVO.getYuce3()+dataVO.getNumber3());
//        helper.setText(R.id.text_04, dataVO.getNumber4Result());
//        helper.setText(R.id.text_05, dataVO.getNumberResult());
                helper.setText(R.id.text_05, dataVO.getYuce4()+dataVO.getNumber4());
//        helper.setText(R.id.text_06, dataVO.getNumber3Result());
        helper.setText(R.id.text_06, dataVO.getMaxYiLou());
        LinearLayout itemLayout= helper.getView(R.id.list_item);
        if("5".equals(dataVO.getMark())){
            itemLayout.setBackgroundResource(R.color.C12);
        }else if("3".equals(dataVO.getMark())){
            itemLayout.setBackgroundResource(R.color.C13);
        }else if("2".equals(dataVO.getMark())){
            itemLayout.setBackgroundResource(R.color.C10);
        }
        else{
            itemLayout.setBackgroundResource(R.color.C1);
        }
    }

}
