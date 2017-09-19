
package com.bs.bsims.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.DanganWorkInterview;
import com.bs.bsims.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class DanganAllWorkChilrdinterviewApdater extends BaseAdapter {
    private Context mContext;
    public List<DanganWorkInterview> mList;

    public DanganAllWorkChilrdinterviewApdater(Context context) {
        mContext = context;
        mList = new ArrayList<DanganWorkInterview>();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext,
                    R.layout.da_workallchrildinterview_apdater, null);
            holder.interviewtime = (TextView) convertView
                    .findViewById(R.id.interviewtime);
            holder.typename = (TextView) convertView
                    .findViewById(R.id.typename);
            holder.interviewcontent = (TextView) convertView
                    .findViewById(R.id.interviewcontent);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 给值

        holder.interviewtime.setText(DateUtils.parseDateDay(mList.get(position).getInterviewtime()));
        holder.typename.setText(mList.get(position).getTypename());
        holder.interviewcontent.setText("约谈记录:" + mList.get(position).getInterviewcontent());
        return convertView;
    }

    static class ViewHolder {
        private TextView interviewtime;
        private TextView typename;
        private TextView interviewcontent;

    }
}
