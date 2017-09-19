
package com.bs.bsims.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.NoticeObjectVO;

import java.util.ArrayList;
import java.util.List;

public class NoticeObjectAdapter extends BaseAdapter {
    private Context mContext;
    public List<NoticeObjectVO> mList;

    public NoticeObjectAdapter(Context context) {
        this.mContext = context;
        this.mList = new ArrayList<NoticeObjectVO>();
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
            convertView = View.inflate(mContext, R.layout.gv_notice_object_item, null);
            holder.mObjecttv = (TextView) convertView.findViewById(R.id.object_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // holder.mObjecttv.setText(mList.get(position).getName());
        return convertView;
    }

    static class ViewHolder {
        private TextView mObjecttv;
    }

}
