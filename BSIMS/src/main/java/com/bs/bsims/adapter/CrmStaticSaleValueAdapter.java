
package com.bs.bsims.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.CrmStaticSaleValueVO;

import java.util.ArrayList;
import java.util.List;

public class CrmStaticSaleValueAdapter extends BaseAdapter {

    private Context mContext;
    public List<CrmStaticSaleValueVO> salesList = null;

    public CrmStaticSaleValueAdapter(Context context, List<CrmStaticSaleValueVO> list) {
        this.mContext = context;
        this.salesList = list;
    }

    @Override
    public int getCount() {
        return salesList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return salesList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        ViewHolder holder = null;
        final CrmStaticSaleValueVO vo = salesList.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.crm_static_sale_value_adapter, null);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.huikuan_money = (TextView) convertView.findViewById(R.id.huikuan_money);
            holder.complete_value = (TextView) convertView.findViewById(R.id.complete_value);
            holder.trade_value_text = (TextView) convertView.findViewById(R.id.trade_value_text);
            holder.trade_value_image = (ImageView) convertView.findViewById(R.id.trade_value_image);
            holder.show_name_layout = (LinearLayout) convertView.findViewById(R.id.show_name_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(vo.getFullname());
        holder.huikuan_money.setText("￥" + vo.getPayment());
        holder.complete_value.setText(vo.getCompPercent());
        if (salesList != null && salesList.size() == 1) {
            holder.show_name_layout.setGravity(Gravity.CENTER);
            holder.trade_value_image.setVisibility(View.GONE);
            holder.trade_value_text.setVisibility(View.GONE);
        } else if (salesList != null && salesList.size() > 1) {
            holder.show_name_layout.setGravity(Gravity.NO_GRAVITY);
            if (0 == position) {
                holder.trade_value_image.setVisibility(View.VISIBLE);
                holder.trade_value_text.setVisibility(View.GONE);
                holder.complete_value.setTextColor(mContext.getResources().getColor(R.color.red));
                holder.trade_value_image.setBackgroundResource(R.drawable.static_num_01);
            } else if (1 == position) {
                holder.trade_value_image.setVisibility(View.VISIBLE);
                holder.trade_value_text.setVisibility(View.GONE);
                holder.complete_value.setTextColor(mContext.getResources().getColor(R.color.C13));
                holder.trade_value_image.setBackgroundResource(R.drawable.static_num_02);
            } else if (2 == position) {
                holder.trade_value_image.setVisibility(View.VISIBLE);
                holder.trade_value_text.setVisibility(View.GONE);
                holder.complete_value.setTextColor(mContext.getResources().getColor(R.color.C10));
                holder.trade_value_image.setBackgroundResource(R.drawable.static_num_03);
            } else {
                holder.trade_value_image.setVisibility(View.GONE);
                holder.trade_value_text.setVisibility(View.VISIBLE);
                holder.complete_value.setTextColor(mContext.getResources().getColor(R.color.C6));
                // GradientDrawable drawable = CommonUtils.setBackgroundShap(mContext, 10,
                // "#aaaaaa",
                // "#aaaaaa");
                holder.trade_value_text.setBackgroundResource(R.drawable.static_sale_value_rank);
                holder.trade_value_text.setText(String.valueOf(position + 1));
            }
        }

        return convertView;
    }

    class ViewHolder {
        private TextView name, huikuan_money, complete_value, trade_value_text;
        private ImageView trade_value_image;
        private LinearLayout show_name_layout;
    }

    public void updateData(List<CrmStaticSaleValueVO> list) {
        if (list == null)
            list = new ArrayList<CrmStaticSaleValueVO>();
        salesList.clear();
        salesList.addAll(list);
        this.notifyDataSetChanged();
    }
}
