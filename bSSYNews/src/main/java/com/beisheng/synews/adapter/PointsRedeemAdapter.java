
package com.beisheng.synews.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beisheng.base.adapter.BSBaseAdapter;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.synews.mode.PointsMallVO;
import com.im.zhsy.R;

public class PointsRedeemAdapter extends BSBaseAdapter<PointsMallVO> {

    public PointsRedeemAdapter(Context context) {
        super(context);
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (mIsEmpty) {
            return super.getView(position, convertView, parent);
        }

        if (convertView != null && convertView.getTag() == null)
            convertView = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.redeem_records_adapter, null);
            holder.nameTv = (TextView) convertView.findViewById(R.id.name_tv);
            holder.descTv = (TextView) convertView.findViewById(R.id.desc_tv);
            holder.pointsTv = (TextView) convertView.findViewById(R.id.points_tv);
            holder.timeTv = (TextView) convertView.findViewById(R.id.time_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PointsMallVO vo = (PointsMallVO) mList.get(position);
        holder.nameTv.setText(vo.getTitle());
        // holder.descTv.setText(vo.getTitle());
        holder.pointsTv.setText("-" + vo.getScore() + "积分");
        // holder.timeTv.setText(DateUtils.parseMDHM(vo.getAddtime()));
        holder.timeTv.setText(vo.getStatusText());
        if ("1".equals(vo.getStatus())) {
            holder.timeTv.setBackground(BaseCommonUtils.setBackgroundShap(mContext, 5, R.color.C10, R.color.C10));
        } else {
            holder.timeTv.setBackground(BaseCommonUtils.setBackgroundShap(mContext, 5, R.color.C12, R.color.C12));
        }
        holder.timeTv.setTextColor(mContext.getResources().getColor(R.color.C1));
        return convertView;
    }

    static class ViewHolder {
        private TextView descTv, nameTv, pointsTv, timeTv;
    }
}
