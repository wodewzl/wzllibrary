
package com.bs.bsims.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.CrmStatisticsVisitorVO;

public class CrmStatisticsVisitorCustomersAdapter extends BSBaseAdapter<CrmStatisticsVisitorVO> {
    private Context mContext;

    public CrmStatisticsVisitorCustomersAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (mIsEmpty) {
            return super.getView(position, convertView, parent);
        }
        CrmStatisticsVisitorVO vo = mList.get(position);
        if (convertView != null && convertView.getTag() == null)
            convertView = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.crm_statistics_lv_visitor_customers, null);
            holder.mTitleTv = (TextView) convertView.findViewById(R.id.title_tv);
            holder.mChargePeopleTv = (TextView) convertView.findViewById(R.id.charge_people_tv);
            holder.mCountTv = (TextView) convertView.findViewById(R.id.count_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTitleTv.setText(vo.getCname());
        holder.mChargePeopleTv.setText("联系人：" + vo.getName());
        holder.mCountTv.setText(vo.getVisitCount());

        return convertView;
    }

    static class ViewHolder {
        private TextView mTitleTv, mChargePeopleTv, mCountTv;
    }
}
