
package com.bs.bsims.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.CompanyCultureVO;

import java.util.ArrayList;
import java.util.List;

public class CompanyCultureAdapter extends BaseAdapter {
    private Context mContext;
    private List<CompanyCultureVO> mList;

    public CompanyCultureAdapter(Context context) {
        this.mContext = context;
        this.mList = new ArrayList<CompanyCultureVO>();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.lv_company_culture, null);
            holder.mTileTv = (TextView) convertView.findViewById(R.id.title_tv);
            holder.mStateTv = (TextView) convertView.findViewById(R.id.state_tv);
            holder.mTimeTv = (TextView) convertView.findViewById(R.id.time_tv);
            // holder.mReadBt = (ImageView) convertView.findViewById(R.id.read_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTileTv.setText(mList.get(position).getTitle());
        if ("1".endsWith(mList.get(position).getState())) {
            holder.mStateTv.setText("已读");
            holder.mStateTv.setBackgroundResource(R.drawable.corners_notice_unread);
        } else {
            holder.mStateTv.setText("未读");
            holder.mStateTv.setBackgroundResource(R.drawable.corners_notice_read);
        }
        holder.mTimeTv.setText(mList.get(position).getTime());

        return convertView;
    }

    static class ViewHolder {
        private TextView mTileTv, mStateTv, mTypeTv, mTimeTv;
        private ImageView mReadBt;
    }

    public void updateData(List<CompanyCultureVO> list) {
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

}
