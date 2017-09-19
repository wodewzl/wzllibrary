package com.xiaojing.shop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wuzhanglong.library.ItemDecoration.StickyHeaderAdapter;
import com.wuzhanglong.library.adapter.LRecyclerBaseAdapter;
import com.xiaojing.shop.R;
import com.xiaojing.shop.mode.MoneyVO;

/**
 * Created by Administrator on 2017/2/13.
 */

public class MyJBAdapter extends LRecyclerBaseAdapter<MoneyVO> implements
        StickyHeaderAdapter<MyJBAdapter.HeaderHolder> {
    private int type =1;

    public MyJBAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

            final View view = mInflater.inflate(R.layout.my_jingbi_adapter_type1, viewGroup, false);
            return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if(mList.size()==0){
            holder.itemView.setVisibility(View.GONE);
            return ;

        }

        ViewHolder viewHolder = (ViewHolder) holder;
        MoneyVO vo = mList.get(position);
        viewHolder.typeTv.setText(vo.getStagetext());
        viewHolder.descTv.setText(vo.getPl_addtime());
        if(Double.parseDouble(vo.getPl_beans())>0){
            viewHolder.moneyTv.setText("+"+vo.getPl_beans());
        }else{
            viewHolder.moneyTv.setText(vo.getPl_beans());
        }

    }

    @Override
    public int getItemCount() {
        if(mList.size()==0){
            return super.getItemCount();
        }else{
            return mList.size();
        }

    }

    @Override
    public long getHeaderId(int position) {
        //原理是这样的 11112222333334444444 同样字段是一组
        if (position > 0) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public HeaderHolder onCreateHeaderViewHolder(ViewGroup parent) {
        final View view = mInflater.inflate(R.layout.my_jingbi_adapter_type2, parent, false);
        return new HeaderHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(final HeaderHolder viewholder, final int position) {

//        viewholder.header.setText("Header " + getHeaderId(position));
        viewholder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "onBindHeaderViewHolder item clicked position = " + position, Toast.LENGTH_SHORT).show();
            }
        });
        if (getHeaderId(position) == 2) {
            viewholder.header.setVisibility(View.GONE);
        } else {
            viewholder.header.setVisibility(View.VISIBLE);
        }
        if(this.getType()==1){
            viewholder.header.setText("消费鲸币记录");
        }else{
            viewholder.header.setText("消费小鲸豆记录");
        }

    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView typeTv, descTv, moneyTv;

        public ViewHolder(View itemView) {
            super(itemView);
            typeTv = (TextView) itemView.findViewById(R.id.type_tv);
            descTv = (TextView) itemView.findViewById(R.id.desc_tv);
            moneyTv = (TextView) itemView.findViewById(R.id.money_tv);
        }
    }

    static class HeaderHolder extends RecyclerView.ViewHolder {
        public TextView header;

        public HeaderHolder(View itemView) {
            super(itemView);
            header = (TextView) itemView.findViewById(R.id.header);
        }
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
