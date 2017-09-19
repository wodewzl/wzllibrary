
package com.bs.bsims.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.CrmStaticsBussnessVO;
import com.bs.bsims.utils.CommonUtils;

import java.util.List;

public class CrmStaticsBussnessAdapter extends BSBaseAdapter<CrmStaticsBussnessVO> {

    private Context mContext;
    private String mMode = "1";

    public CrmStaticsBussnessAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (mIsEmpty) {
            return super.getView(position, convertView, parent);
        }

        if (convertView != null && convertView.getTag() == null)
            convertView = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.crm_statics_bussness_adapter, null);
            holder.rank = (TextView) convertView.findViewById(R.id.rank_text);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.sort = (TextView) convertView.findViewById(R.id.sort);
            holder.monthAdd = (TextView) convertView.findViewById(R.id.month_add);
            holder.monthLose = (TextView) convertView.findViewById(R.id.month_lose);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CrmStaticsBussnessVO vo = mList.get(position);

        if (0 == position) {
            holder.img.setVisibility(View.VISIBLE);
            holder.rank.setVisibility(View.GONE);
            holder.img.setImageResource(R.drawable.static_num_01);
        } else if (1 == position) {
            holder.img.setVisibility(View.VISIBLE);
            holder.rank.setVisibility(View.GONE);
            holder.img.setImageResource(R.drawable.static_num_02);
        } else if (2 == position) {
            holder.img.setVisibility(View.VISIBLE);
            holder.rank.setVisibility(View.GONE);
            holder.img.setImageResource(R.drawable.static_num_03);
        } else {
            holder.img.setVisibility(View.GONE);
            holder.rank.setVisibility(View.VISIBLE);
            holder.rank.setBackgroundResource(R.drawable.static_sale_value_rank);
            holder.rank.setText(String.valueOf(position + 1));
        }

        holder.name.setText(vo.getFullname());
        holder.monthAdd.setText(vo.getNum());

        holder.sort.setText(vo.getWinPercent());
        holder.sort.setCompoundDrawablePadding(CommonUtils.dip2px(mContext, 5));
        if ("1".equals(vo.getContrast())) {
            holder.sort.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.statistics_down), null);
        } else if ("2".equals(vo.getContrast())) {
            holder.sort.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.statistics_same), null);
        } else if ("3".equals(vo.getContrast())) {
            holder.sort.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.statistics_up), null);
        }

        holder.monthAdd.setText(vo.getAddCount());
        holder.monthLose.setText(vo.getDropCount());
        return convertView;
    }

    static class ViewHolder {
        private TextView rank, name, sort, monthAdd, monthLose;

        private ImageView img;
    }

    public void updateData(List<CrmStaticsBussnessVO> list, String mode) {
        this.mMode = mode;
        super.updateData(list);
    }

    // 全部部门时sort 为默认最后一个
    public void updateData(List<CrmStaticsBussnessVO> list, String mode, int sort) {
        this.mMode = mode;
        super.updateData(list);
    }

}
