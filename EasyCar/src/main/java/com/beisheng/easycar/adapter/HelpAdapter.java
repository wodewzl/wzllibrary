package com.beisheng.easycar.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beisheng.easycar.R;
import com.beisheng.easycar.mode.HelpVO;
import com.wuzhanglong.library.adapter.RecyclerBaseAdapter;
import com.wuzhanglong.library.utils.BaseCommonUtils;

import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * Created by Administrator on 2017/2/13.
 */

public class HelpAdapter extends RecyclerBaseAdapter {

    public HelpAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.adapter_help_type1);
    }

    @Override
    public void initData(BGAViewHolderHelper helper, int position, Object model) {
        final HelpVO vo = (HelpVO) model;
        if (getItemViewType(position) == R.layout.adapter_help_type1) {
            helper.setText(R.id.type1_tv, vo.getName());
            TextView textView = helper.getTextView(R.id.type1_tv);
            switch (BaseCommonUtils.parseInt(vo.getId())) {
                case 2:
                    if (vo.isExpend()) {
                        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.help_01, 0, R.drawable.help_06, 0);
                    } else {
                        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.help_01, 0, R.drawable.help_07, 0);
                    }
                    break;
                case 3:
                    if (vo.isExpend()) {
                        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.help_02, 0, R.drawable.help_06, 0);
                    } else {
                        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.help_02, 0, R.drawable.help_07, 0);
                    }
                    break;
                case 4:
                    if (vo.isExpend()) {
                        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.help_03, 0, R.drawable.help_06, 0);
                    } else {
                        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.help_03, 0, R.drawable.help_07, 0);
                    }
                    ;
                    break;
                case 5:
                    if (vo.isExpend()) {
                        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.help_04, 0, R.drawable.help_06, 0);
                    } else {
                        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.help_04, 0, R.drawable.help_07, 0);
                    }
                    break;
                case 6:
                    if (vo.isExpend()) {
                        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.help_05, 0, R.drawable.help_06, 0);
                    } else {
                        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.help_05, 0, R.drawable.help_07, 0);
                    }
                    break;
                default:
                    break;
            }

            helper.setItemChildClickListener(R.id.type1_tv);
            helper.setOnItemChildClickListener(new BGAOnItemChildClickListener() {
                @Override
                public void onItemChildClick(ViewGroup viewGroup, View view, int i) {
                    if (vo.getNewsList().size() > 0) {
                        for (int j = 0; j <vo.getNewsList().size() ; j++) {
                            HelpVO helpVO = vo.getNewsList().get(j);
                            if (helpVO.isExpend()) {
                                helpVO.setExpend(false);
                                vo.setExpend(false);
                            } else {
                                helpVO.setExpend(true);
                                vo.setExpend(true);
                            }
                        }

                    }

                    notifyDataSetChanged();
                }
            });


        } else {
            helper.setText(R.id.type2_tv, vo.getTitle());
            View itemView=helper.getConvertView();
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams)itemView.getLayoutParams();
            if (vo.isExpend()) {
                param.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                param.width = LinearLayout.LayoutParams.MATCH_PARENT;
                itemView.setVisibility(View.VISIBLE);
            } else {
                itemView.setVisibility(View.GONE);
                param.height = 0;
                param.width = 0;
            }
            itemView.setLayoutParams(param);
        }


    }

    @Override
    public int getItemViewType(int position) {
        if (mData.size() == 0)
            return super.getItemViewType(position);
        HelpVO vo = (HelpVO) mData.get(position);
        if (vo.getNewsList() != null) {
            return R.layout.adapter_help_type1;
        } else {
            return R.layout.adapter_help_type2;
        }
    }
}
