package com.xiaojing.shop.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaojing.shop.R;
import com.xiaojing.shop.mode.HomeVO;

import java.util.List;

import cc.solart.turbo.BaseTurboAdapter;
import cc.solart.turbo.BaseViewHolder;


public class TestRAdapter extends BaseTurboAdapter<HomeVO, BaseViewHolder> {

    public TestRAdapter(Context context) {
        super(context);
    }

    public TestRAdapter(Context context, List<HomeVO> data) {
        super(context, data);
    }


    @Override
    protected int getDefItemViewType(int position) {
        HomeVO city = getItem(position);
//        return city.type;

        if(position==0||position==10||position==15){
            return 1;
        }else{
            return 0;
        }
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0)
            return new CityHolder(inflateItemView(R.layout.shop_cat_adapter_type2, parent));
        else
            return new PinnedHolder(inflateItemView(R.layout.shop_cat_adapter_type1, parent));
    }

    @Override
    protected void convert(BaseViewHolder holder, HomeVO homeVO) {
        if (holder instanceof CityHolder) {
//            ((CityHolder) holder).city_name.setText(item.name);
        }else {
//            String letter = item.pys.substring(0, 1);
//            ((PinnedHolder) holder).city_tip.setText(letter);
            ((PinnedHolder) holder).header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println();
                }
            });
        }
    }


    class CityHolder extends BaseViewHolder {

        TextView city_name;

        public CityHolder(View view) {
            super(view);

        }
    }


    class PinnedHolder extends BaseViewHolder {

        LinearLayout header;

        public PinnedHolder(View view) {
            super(view);
            header = findViewById(R.id.header);
        }
    }
}
