
package com.beisheng.synews.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beisheng.base.adapter.BSBaseAdapter;
import com.beisheng.base.utils.DateUtils;
import com.beisheng.synews.mode.PointsMallVO;
import com.im.zhsy.R;

public class PointsRecordAdapter extends BSBaseAdapter<PointsMallVO> {

    public PointsRecordAdapter(Context context) {
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
            convertView = View.inflate(mContext, R.layout.points_record_adapter, null);
            holder.nameTv = (TextView) convertView.findViewById(R.id.name_tv);
            holder.descTv = (TextView) convertView.findViewById(R.id.desc_tv);
            holder.pointsTv = (TextView) convertView.findViewById(R.id.points_tv);
            holder.timeTv = (TextView) convertView.findViewById(R.id.time_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PointsMallVO vo = (PointsMallVO) mList.get(position);
        holder.nameTv.setText(vo.getTypename());
        holder.descTv.setText(vo.getTitle());
        holder.pointsTv.setText(vo.getScore() + "积分");
        holder.timeTv.setText(DateUtils.parseMDHM(vo.getAddtime()));
        return convertView;
    }

    static class ViewHolder {
        private TextView descTv, nameTv, pointsTv, timeTv;
    }
}
