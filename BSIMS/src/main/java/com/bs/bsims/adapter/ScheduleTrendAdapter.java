
package com.bs.bsims.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.ScheduleDetailActivity;
import com.bs.bsims.model.ScheduleTrendVO;
import com.bs.bsims.utils.DateUtils;

import java.util.List;

public class ScheduleTrendAdapter extends BSBaseAdapter<ScheduleTrendVO> {
    private Context mContext;
    private String mCuurentTime = "";
    private String mPreviousTime = "";

    public ScheduleTrendAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (mIsEmpty) {
            return super.getView(position, convertView, parent);
        }
        final ScheduleTrendVO vo = mList.get(position);
        if (convertView != null && convertView.getTag() == null)
            convertView = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.schedule_trend_lv, null);
            holder.mTimeTv = (TextView) convertView.findViewById(R.id.time_tv);
            holder.mContentTv = (TextView) convertView.findViewById(R.id.content_tv);
            holder.mStartTime = (TextView) convertView.findViewById(R.id.start_time);
            holder.mIsread = (ImageView) convertView.findViewById(R.id.isread);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mStartTime.setText(vo.getStart());
        if (!vo.getStart().equals(vo.getEnd())) {
            holder.mStartTime.setText(vo.getStart() + "\n" + " -" + "\n" + vo.getEnd());
        }
        holder.mContentTv.setText(vo.getTitle());

        if ("1".equals(vo.getState())) {
            holder.mIsread.setVisibility(View.INVISIBLE);
            holder.mStartTime.setTextColor(mContext.getResources().getColor(R.color.C6));
            holder.mContentTv.setTextColor(mContext.getResources().getColor(R.color.C6));
        } else {
            holder.mIsread.setVisibility(View.VISIBLE);
            holder.mStartTime.setTextColor(mContext.getResources().getColor(R.color.C4));
            holder.mContentTv.setTextColor(mContext.getResources().getColor(R.color.C4));
        }

        mCuurentTime = vo.getDate();
        if (vo.getIsShowTime() == null) {
            if (mPreviousTime.equals(mCuurentTime)) {
                vo.setIsShowTime("1");
            } else {

                if (mCuurentTime.equals(DateUtils.getCurrentDate())) {
                    vo.setIsShowTime("今天");
                } else {
                    vo.setIsShowTime(mCuurentTime + " " + vo.getWeek());
                }
            }
        }

        if (vo.getIsShowTime() != null && !"1".equals(vo.getIsShowTime())) {
            holder.mTimeTv.setVisibility(View.VISIBLE);
            holder.mTimeTv.setText(vo.getIsShowTime());
        } else {
            holder.mTimeTv.setVisibility(View.GONE);
        }

        mPreviousTime = mCuurentTime;

        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                vo.setState("1");
                intent.putExtra("id", vo.getId());
                intent.setClass(mContext, ScheduleDetailActivity.class);
                mContext.startActivity(intent);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    static class ViewHolder {
        private TextView mTimeTv, mContentTv, mStartTime;
        private ImageView mIsread;
    }

    @Override
    public void updateDataFrist(List<ScheduleTrendVO> list) {
        for (int i = 0; i < this.mList.size(); i++) {
            mList.get(i).setIsShowTime(null);
        }
        mCuurentTime = "";
        mPreviousTime = "";
        super.updateDataFrist(list);
    }
}
