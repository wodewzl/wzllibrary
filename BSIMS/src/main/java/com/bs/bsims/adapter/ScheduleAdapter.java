
package com.bs.bsims.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.ScheduleVO;
import com.bs.bsims.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class ScheduleAdapter extends BaseAdapter {
    public ArrayList<ScheduleVO> mList;
    private Context mContext;

    public ScheduleAdapter(Context context) {
        mList = new ArrayList<ScheduleVO>();
        mContext = context;
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
            convertView = View.inflate(mContext, R.layout.lv_schedule, null);

            holder.text01 = (TextView) convertView.findViewById(R.id.text01);
            holder.text02 = (TextView) convertView.findViewById(R.id.text02);
            holder.text03 = (TextView) convertView.findViewById(R.id.text03);
            holder.text04 = (TextView) convertView.findViewById(R.id.text04);
            holder.noContent = (LinearLayout) convertView.findViewById(R.id.no_content_layout);
            holder.content = (LinearLayout) convertView.findViewById(R.id.content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.text01.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

        }

        holder.noContent.setVisibility(View.GONE);
        holder.content.setVisibility(View.VISIBLE);
        if (mList.size() == 0) {
            holder.noContent.setVisibility(View.VISIBLE);
            holder.content.setVisibility(View.GONE);
            return convertView;
        }

        ScheduleVO vo = mList.get(position);
        if (vo.getAllday() != null) {
            if ("1".equals(vo.getAllday())) {
                holder.text01.setText("全天");
            } else {

            }

            if (vo.getStartime().equals(vo.getEndtime())) {
                holder.text02.setText(DateUtils.parseHour(vo.getStartime()));

            } else {
                holder.text02.setText(DateUtils.parseHour(vo.getStartime()) + "~" + DateUtils.parseHour(vo.getEndtime()));
            }
            if ("1".equals(vo.getOpen())) {
                holder.text03.setText("公开日程");
                holder.text03.setBackgroundResource(R.drawable.frame_shixing_yellow);
                holder.text01.setText(vo.getFullname());
                holder.text01.setCompoundDrawablePadding(10);
                holder.text01.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.schedule_03), null, null, null);
                holder.text01.setVisibility(View.VISIBLE);
            } else {
                holder.text03.setText("私有日程");
                holder.text03.setBackgroundResource(R.drawable.frame_shixing_green);
                holder.text01.setVisibility(View.GONE);

            }

        } else {
            holder.text01.setCompoundDrawablePadding(10);
            holder.text01.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.schedule_03), null, null, null);
            holder.text01.setText(vo.getFullname());
            holder.text02.setText(DateUtils.parseMonth(vo.getStarttime()) + "至" + DateUtils.parseMonth(vo.getEndtime()));
            holder.text03.setText("我的任务");
            holder.text03.setBackgroundResource(R.drawable.frame_shixing_blue);
            holder.text01.setVisibility(View.VISIBLE);
        }

        holder.text04.setText(vo.getTitle());

        return convertView;
    }

    static class ViewHolder
    {
        private TextView text01, text02, text03, text04;
        private LinearLayout noContent, content;
    }

    public void updateData(List<ScheduleVO> list) {
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataLast(List<ScheduleVO> list) {
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

}
