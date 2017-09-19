
package com.beisheng.synews.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beisheng.base.adapter.BSBaseAdapter;
import com.beisheng.synews.mode.InvitatCodeVO;
import com.im.zhsy.R;

public class InvitatCodeAdapter extends BSBaseAdapter<InvitatCodeVO> {
    private int currentPosition = -1;

    public InvitatCodeAdapter(Context context) {
        super(context);
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
            convertView = View.inflate(mContext, R.layout.invitat_code_adapter, null);
            holder.dateTv = (TextView) convertView.findViewById(R.id.date_tv);
            holder.numberTv = (TextView) convertView.findViewById(R.id.number_tv);
            holder.realNumberTv = (TextView) convertView.findViewById(R.id.real_number_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final InvitatCodeVO vo = (InvitatCodeVO) mList.get(position);
        holder.dateTv.setText(vo.getMonths());
        holder.numberTv.setText(vo.getNum());
        holder.realNumberTv.setText(vo.getNum());
        return convertView;
    }

    static class ViewHolder {
        private TextView dateTv, numberTv, realNumberTv;
    }

}
