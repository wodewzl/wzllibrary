
package com.bs.bsims.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.WarnVO;

import java.util.ArrayList;
import java.util.List;

public class WarnAdapter extends BaseAdapter {
    private Context mContext;
    public List<WarnVO> mList;

    public WarnAdapter(Context context) {
        this.mContext = context;
        this.mList = new ArrayList<WarnVO>();
    }

    @Override
    public int getCount() {
        if (mList.size() == 0) {
            return 1;
        } else {
            return mList.size();
        }
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
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.lv_warn_item, null);
            // convertView = View.inflate(mContext, R.layout.lv_warn_item, null);
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            holder.content_tv = (TextView) convertView.findViewById(R.id.content_tv);
            holder.type = (TextView) convertView.findViewById(R.id.type);
            holder.time = (TextView) convertView.findViewById(R.id.time);

            holder.noContent = (LinearLayout) convertView.findViewById(R.id.no_content_layout);
            holder.content = (LinearLayout) convertView.findViewById(R.id.content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.noContent.setVisibility(View.GONE);
        holder.content.setVisibility(View.VISIBLE);
        if (mList.size() == 0) {
            holder.noContent.setVisibility(View.VISIBLE);
            holder.content.setVisibility(View.GONE);
            return convertView;
        }

        holder.type.setText("【" + mList.get(position).getWname() + "】");
        holder.icon.setImageResource(R.drawable.warn);

        holder.content_tv.setText(mList.get(position).getWdescription());
        holder.time.setVisibility(View.GONE);
        return convertView;
    }

    static class ViewHolder {
        private ImageView icon;
        private TextView type, time, content_tv;
        private LinearLayout noContent, content;
    }

    public void updateData(List<WarnVO> list) {
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataFrist(List<WarnVO> list) {
        list.addAll(mList);
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataLast(List<WarnVO> list) {
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

}
