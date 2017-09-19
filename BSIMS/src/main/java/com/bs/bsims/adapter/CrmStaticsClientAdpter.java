
package com.bs.bsims.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.CrmStaticsClinetVO;
import com.bs.bsims.utils.CommonUtils;

import java.util.List;

public class CrmStaticsClientAdpter extends BSBaseAdapter<CrmStaticsClinetVO> {

    private Context mContext;
    private String mMode = "1";

    public CrmStaticsClientAdpter(Context context) {
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
            convertView = View.inflate(mContext, R.layout.lv_statistics_leave, null);
            holder.rank = (TextView) convertView.findViewById(R.id.rank_text);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.type = (TextView) convertView.findViewById(R.id.type);
            holder.sort = (TextView) convertView.findViewById(R.id.sort);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CrmStaticsClinetVO vo = mList.get(position);

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

        holder.type.setText(vo.getFullname());

        if ("1".equals(mMode)) {
            holder.sort.setText(vo.getNum());
        } else if ("2".equals(mMode)) {
            holder.sort.setText(CommonUtils.formatDetailMoney(vo.getNum()));
        } else {
            holder.sort.setText(vo.getNum() + "小时");
        }
        holder.sort.setCompoundDrawablePadding(CommonUtils.dip2px(mContext, 5));
        if ("1".equals(vo.getContrast())) {
            holder.sort.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.statistics_down), null);
        } else if ("2".equals(vo.getContrast())) {
            holder.sort.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.statistics_same), null);
        } else if ("3".equals(vo.getContrast())) {
            holder.sort.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.statistics_up), null);
        }
        return convertView;
    }

    static class ViewHolder {
        private TextView rank, name, sort, type;
        private ImageView img;
    }

    public void updateData(List<CrmStaticsClinetVO> list, String mode) {
        this.mMode = mode;
        super.updateData(list);
    }

    // 全部部门时sort 为默认最后一个
    public void updateData(List<CrmStaticsClinetVO> list, String mode, int sort) {
        this.mMode = mode;
        super.updateData(list);
    }

}
