
package com.bs.bsims.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.MessageVO;
import com.bs.bsims.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class MessageBossAdapter extends BaseAdapter {
    private Context mContext;
    public List<MessageVO> mList;

    public MessageBossAdapter(Context context) {
        this.mContext = context;
        this.mList = new ArrayList<MessageVO>();
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
            convertView = View.inflate(mContext, R.layout.lv_message_boss_item, null);
            holder.mTileTv = (TextView) convertView.findViewById(R.id.title_tv);
            holder.mStateTv = (TextView) convertView.findViewById(R.id.state_tv);
            holder.mTypeTv = (TextView) convertView.findViewById(R.id.type_tv);
            holder.mTimeTv = (TextView) convertView.findViewById(R.id.time_tv);
            holder.mReadBt = (ImageView) convertView.findViewById(R.id.read_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MessageVO vo = mList.get(position);
        holder.mTileTv.setText(vo.getTitle());
        holder.mTypeTv.setText(vo.getSortname());
        long time = Long.valueOf(mList.get(position).getTime()) * 1000;
        holder.mTimeTv.setText(DateUtils.parseDate(time));
        if ("1".equals(vo.getIsread())) {
            holder.mStateTv.setBackgroundResource(R.drawable.corners_notice_read);
            holder.mStateTv.setText("已读");
        } else {
            holder.mStateTv.setBackgroundResource(R.drawable.corners_notice_unread);
            holder.mStateTv.setText("未读");
        }

        return convertView;
    }

    static class ViewHolder {
        private TextView mTileTv, mStateTv, mTypeTv, mTimeTv;
        private ImageView mReadBt;
    }

    public void updateData(List<MessageVO> list) {
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataFrist(List<MessageVO> list) {
        list.addAll(mList);
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataLast(List<MessageVO> list) {
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

}
