package com.xiaojing.shop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wuzhanglong.library.ItemDecoration.StickyHeaderAdapter;
import com.wuzhanglong.library.adapter.LRecyclerBaseAdapter;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.xiaojing.shop.R;
import com.xiaojing.shop.mode.OneShopVO;

import cc.solart.turbo.BaseViewHolder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/2/13.
 */

public class OneShopDetailadapter extends LRecyclerBaseAdapter<OneShopVO> implements
        StickyHeaderAdapter<OneShopDetailadapter.HeaderHolder> {
    private String time;

    public OneShopDetailadapter(Context context) {
        super(context);
        mContext = context;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if(mList.size()==0)
            return super.onCreateViewHolder(viewGroup, i);
        final View view = mInflater.inflate(R.layout.one_shop_detail_adapter_type2, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if(mList.size()==0)
            return;
        ViewHolder viewHolder = (ViewHolder) holder;
        OneShopVO vo = mList.get(position);
        BaseCommonUtils.setTextTwoLast(mContext, viewHolder.tv01, "参与者：", vo.getMember_name(), R.color.C7, 1.1f);
        viewHolder.tv02.setText(vo.getProvince()+"IP："+vo.getMember_ip());
        viewHolder.tv03.setText(vo.getPayment_time());
        BaseCommonUtils.setTextThree(mContext, viewHolder.tv04, "本期购买：", vo.getBuy_count(), "人次", R.color.XJColor2, 1.1f);
        Picasso.with(mContext).load(vo.getMember_avatar_url()).into(viewHolder.headImg);
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

//    @Override
//    public int getItemCount() {
//        return 15;
//    }

    @Override
    public HeaderHolder onCreateHeaderViewHolder(ViewGroup parent) {
        final View view = mInflater.inflate(R.layout.one_shop_detail_adapter_type1, parent, false);
        return new HeaderHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(final HeaderHolder viewholder, final int position) {
        if (getHeaderId(position) == 2) {
            viewholder.header.setVisibility(View.GONE);
        } else {
            viewholder.header.setVisibility(View.VISIBLE);
        }
        viewholder.timeTv.setText(this.getTime()+" 开始");
    }


    static class ViewHolder extends BaseViewHolder {
        public TextView tv01, tv02, tv03, tv04;
        public CircleImageView headImg;

        public ViewHolder(View itemView) {
            super(itemView);
            tv01 = (TextView) itemView.findViewById(R.id.tv_01);
            tv02 = (TextView) itemView.findViewById(R.id.tv_02);
            tv03 = (TextView) itemView.findViewById(R.id.tv_03);
            tv04 = (TextView) itemView.findViewById(R.id.tv_04);
            headImg = (CircleImageView) itemView.findViewById(R.id.head_img);
        }
    }

    static class HeaderHolder extends BaseViewHolder {
        public LinearLayout header;
        public TextView timeTv;

        public HeaderHolder(View itemView) {
            super(itemView);
            header = (LinearLayout) itemView.findViewById(R.id.header);
            timeTv= (TextView) itemView.findViewById(R.id.time_tv);
        }
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
