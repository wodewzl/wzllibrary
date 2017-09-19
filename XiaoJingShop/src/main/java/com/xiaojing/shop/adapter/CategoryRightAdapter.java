package com.xiaojing.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiaojing.shop.R;
import com.xiaojing.shop.activity.ShopListActivity;
import com.xiaojing.shop.mode.CategoryVO;

import java.util.List;

import cc.solart.turbo.BaseTurboAdapter;
import cc.solart.turbo.BaseViewHolder;

/**
 * Created by cheng on 16-11-10.
 */

public class CategoryRightAdapter extends BaseTurboAdapter<CategoryVO, BaseViewHolder> {

    public CategoryRightAdapter(Context context) {
        super(context);
    }

    public CategoryRightAdapter(Context context, List<CategoryVO> data) {
        super(context, data);
    }

    @Override
    protected int getDefItemViewType(int position) {
        CategoryVO city = getItem(position);
        if (city.getGrandson() != null) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return new HeadHolder(inflateItemView(R.layout.category_adapter_right_type1, parent));
        } else {
            return new ViewHolder(inflateItemView(R.layout.category_adapter_right_type2, parent));
        }

    }

    @Override
    protected void convert(BaseViewHolder holder, final CategoryVO categoryVO) {
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).nameTv.setText(categoryVO.getGc_name());
            Picasso.with(mContext).load(categoryVO.getGc_image()).placeholder(R.drawable.img_default).into(((ViewHolder) holder).img);
        } else {
            ((HeadHolder) holder).header.setText(categoryVO.getGc_name());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("gc_id",categoryVO.getGc_id());
                intent.setClass(mContext, ShopListActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    class ViewHolder extends BaseViewHolder {
        TextView nameTv;
        ImageView img;

        public ViewHolder(View view) {
            super(view);
            nameTv = findViewById(R.id.name_tv);
            img = findViewById(R.id.img);
        }
    }
    
    class HeadHolder extends BaseViewHolder {
        TextView header;

        public HeadHolder(View view) {
            super(view);
            header = findViewById(R.id.header);
        }
    }
}