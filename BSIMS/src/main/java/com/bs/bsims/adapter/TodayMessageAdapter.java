
package com.bs.bsims.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.MessageVO;

import java.util.ArrayList;
import java.util.List;

public class TodayMessageAdapter extends BaseAdapter {
    public ArrayList<MessageVO> mList;
    private Context mContext;

    // CcAdapter state 参数为图片边缘的提醒的小图样式
    public TodayMessageAdapter(Context context) {
        // TODO Auto-generated constructor stub
        mList = new ArrayList<MessageVO>();
        mContext = context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
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
            convertView = View.inflate(mContext, R.layout.today_message, null);

            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            holder.divider = convertView.findViewById(R.id.divider);

            holder.duration = (TextView) convertView.findViewById(R.id.duration);
            holder.endTime = (TextView) convertView.findViewById(R.id.end_time);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.divider.setVisibility(View.VISIBLE);
        }

        MessageVO vo = mList.get(position);

        // holder.time.setText(vo.getTime());

        // if (vo.getTime().split(",").length == 2) {
        // holder.time.setText(vo.getTime().split(",")[0]);
        // holder.duration.setVisibility(View.VISIBLE);
        // holder.endTime.setVisibility(View.VISIBLE);
        // holder.endTime.setText(vo.getTime().split(",")[1]);
        // } else {
        // holder.time.setText(vo.getTime());
        // holder.duration.setVisibility(View.GONE);
        // holder.endTime.setVisibility(View.INVISIBLE);
        // }
        //
        // holder.title.setText(vo.getTitle());
        // if ("1".equals(vo.getMyself())) {
        // holder.title.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.update_hint),
        // null, null, null);
        // holder.title.setCompoundDrawablePadding(10);
        // } else {
        // holder.title.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.calendar_bg_tag),
        // null, null, null);
        // holder.title.setCompoundDrawablePadding(10);
        // }
        // if (vo.getFromName() != null) {
        // StringBuffer sb = new StringBuffer();
        // sb.append(vo.getFromName()).append(": ").append(vo.getFromDname());
        // holder.content.setVisibility(View.VISIBLE);
        // holder.content.setText(sb.toString());
        // }

        if (mList.size() - 1 == position) {
            holder.divider.setVisibility(View.GONE);
        }

        return convertView;
    }

    static class ViewHolder
    {
        private TextView time, title, content, duration, endTime;
        private View divider;
    }

    public void updateData(List<MessageVO> list) {
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

}
