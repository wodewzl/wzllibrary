
package com.bs.bsims.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.BossStatisticsSalesFunnelVO;
import com.bs.bsims.utils.DateUtils;

public class BossStatisticsSalesFunnelAdapter extends BSBaseAdapter<BossStatisticsSalesFunnelVO> {
    private Context mContext;

    public BossStatisticsSalesFunnelAdapter(Context context) {
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
            convertView = View.inflate(mContext, R.layout.boss_statistics_sales_funnel_adapter, null);
            holder.bussinesNameTv = (TextView) convertView.findViewById(R.id.bussines_name_tv);
            holder.companyNameTv = (TextView) convertView.findViewById(R.id.company_name_tv);
            holder.addTimeTv = (TextView) convertView.findViewById(R.id.add_time_tv);
            holder.clientNameTv = (TextView) convertView.findViewById(R.id.client_name_tv);
            holder.moneyTv = (TextView) convertView.findViewById(R.id.money_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        BossStatisticsSalesFunnelVO vo = mList.get(position);
        holder.bussinesNameTv.setText(vo.getBname());
        holder.companyNameTv.setText(vo.getCname());
        holder.addTimeTv.setText(DateUtils.parseDateDay(vo.getAddtime()));
        holder.clientNameTv.setText(vo.getFullname());
        holder.moneyTv.setText("￥" + vo.getMoney());
        return convertView;
    }

    static class ViewHolder {
        public TextView bussinesNameTv, companyNameTv, addTimeTv, clientNameTv, moneyTv;
    }

}
