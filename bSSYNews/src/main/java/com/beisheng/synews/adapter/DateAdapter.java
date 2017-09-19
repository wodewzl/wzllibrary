
package com.beisheng.synews.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beisheng.base.utils.BaseCommonUtils;
import com.im.zhsy.R;

import java.util.Date;

@SuppressLint("NewApi")
public class DateAdapter extends BaseAdapter {
    private Context mContext;
    private Date mDate;
    private String selectDate;
    private int mCurrentSelectPosition = 15;
    private DateCallback mDateCallback;
    private String[] mDateArray;

    public interface DateCallback {
        public abstract void dateCallback();
    }

    public DateAdapter(Context context, Date date, DateCallback dateCallback) {
        this.mContext = context;
        this.mDate = date;
        this.mDateCallback = dateCallback;
    }

    public DateAdapter(Context context, String[] dateArray, DateCallback dateCallback) {
        this.mContext = context;
        this.mDateCallback = dateCallback;
        this.mDateArray = dateArray;
    }

    @Override
    public int getCount() {
        // return 16;
        return mDateArray.length;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_calendar, null);
            holder.dayTv = (TextView) convertView.findViewById(R.id.day_tv);
            holder.weekTv = (TextView) convertView.findViewById(R.id.week_tv);
            holder.itemLayout = (LinearLayout) convertView.findViewById(R.id.item_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // String week = DateUtils.getDayOfWeek(DateUtils.getDateBefore(mDate, 4 - position));
        // String day = DateUtils.ConverToString(DateUtils.getDateBefore(mDate, 4 -
        // position)).split("-")[2];
        // String week = DateUtils.getDayOfWeek(DateUtils.getDateBefore(mDate, 15 - position));
        // String day = DateUtils.ConverToString(DateUtils.getDateBefore(mDate, 15 -
        // position)).split("-")[2];
        // String day = DateUtils.ConverToString(DateUtils.getDateBefore(mDate, 15 - position));
        // holder.weekTv.setText(week);
        holder.dayTv.setText(mDateArray[position]);
        if (position == mCurrentSelectPosition) {
            holder.itemLayout.setBackground(BaseCommonUtils.setBackgroundShap(mContext, 5, R.color.sy_title_color, R.color.sy_title_color));
            holder.dayTv.setTextColor(mContext.getResources().getColor(R.color.C1));
            holder.weekTv.setTextColor(mContext.getResources().getColor(R.color.C1));
            // this.selectDate = DateUtils.ConverToString(DateUtils.getDateBefore(mDate, 4 -
            // position));
            // this.selectDate = DateUtils.ConverToString(DateUtils.getDateBefore(mDate, 15 -
            // position));
            selectDate = mDateArray[position];
        } else {
            holder.itemLayout.setBackground(null);
            holder.weekTv.setTextColor(mContext.getResources().getColor(R.color.C6));
            holder.dayTv.setTextColor(mContext.getResources().getColor(R.color.C4));
        }
        holder.itemLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mCurrentSelectPosition = position;
                // selectDate = DateUtils.ConverToString(DateUtils.getDateBefore(mDate, 4 -
                // position));
                // selectDate = DateUtils.ConverToString(DateUtils.getDateBefore(mDate, 15 -
                // position));
                selectDate = mDateArray[position];
                notifyDataSetChanged();
                mDateCallback.dateCallback();
            }
        });
        return convertView;
    }

    static class ViewHolder
    {
        private TextView dayTv, weekTv;
        private LinearLayout itemLayout;
    }

    public String getSelectDate() {
        return selectDate;
    }

    public void setSelectDate(String selectDate) {
        this.selectDate = selectDate;
    }

}
