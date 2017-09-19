
package com.bs.bsims.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.EXTTaskStatisticsEveryoneListActivity;
import com.bs.bsims.model.BossStatisticsAttendanceVO;
import com.bs.bsims.utils.CommonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("NewApi")
public class BossStatisticsTaskStatisticsAdapter extends BSBaseAdapter<BossStatisticsAttendanceVO> {

    private float mMaxCount = 1;
    public List<BossStatisticsAttendanceVO> mLastList = new ArrayList<BossStatisticsAttendanceVO>();
    private String mStatus;
    private String mStatusStr;
    private String mCurrentDate;
    private Map<String, String> mFixedMap;

    public String getmCurrentDate() {
        return mCurrentDate;
    }

    public void setmCurrentDate(String mCurrentDate) {
        this.mCurrentDate = mCurrentDate;
    }

    public List<BossStatisticsAttendanceVO> getmLastList() {
        return mLastList;
    }

    public void setmLastList(List<BossStatisticsAttendanceVO> mLastList) {
        this.mLastList.clear();
        this.mLastList = mLastList;
    }

    public BossStatisticsTaskStatisticsAdapter(Context context, List<BossStatisticsAttendanceVO> list1, List<BossStatisticsAttendanceVO> list2) {
        super(context);
        mList = list1;
        this.mLastList = list2;
        setFixedMap();
    }

    public float getmMaxCount() {
        return mMaxCount;
    }

    public void setmMaxCount(float mMaxCount) {
        this.mMaxCount = mMaxCount;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (mIsEmpty) {
            return super.getView(position, convertView, parent);
        }
        if (convertView != null && convertView.getTag() == null)
            convertView = null;
        BossStatisticsAttendanceVO nextVo = mList.get(position);
        BossStatisticsAttendanceVO lastVo = mLastList.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.boss_statistics_task_statistics_adapter, null);
            holder.mTaskStatus = (TextView) convertView.findViewById(R.id.task_status);
            holder.mTaskImage = (ImageView) convertView.findViewById(R.id.task_image);
            holder.mThisMonth = (TextView) convertView.findViewById(R.id.this_month);
            holder.mThisProgress = (TextView) convertView.findViewById(R.id.this_progress_bar);
            holder.mProgressCount1 = (TextView) convertView.findViewById(R.id.progress_count1);
            holder.mLastMonth = (TextView) convertView.findViewById(R.id.last_month);
            holder.mLastProgress = (TextView) convertView.findViewById(R.id.last_progress_bar);
            holder.mProgressCount2 = (TextView) convertView.findViewById(R.id.progress_count2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mThisMonth.setText(nextVo.getDate());
        holder.mLastMonth.setText(lastVo.getDate());
        holder.mProgressCount1.setText(nextVo.getNum() + "项");
        holder.mProgressCount2.setText(lastVo.getNum() + "项");
        int maxWidth = CommonUtils.getScreenWidth(mContext) / 2;// 手机屏幕宽度的一半
        int nextNum = Integer.valueOf(nextVo.getNum());
        int lastNum = Integer.valueOf(lastVo.getNum());
        int nextWidth = 0;
        int lastWidth = 0;
        if (mMaxCount != 0) {
            nextWidth = (int) ((maxWidth * nextNum) / mMaxCount);
            lastWidth = (int) ((maxWidth * lastNum) / mMaxCount);
        } else {
            nextWidth = 0;
            lastWidth = 0;// maxWidth
        }
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(nextWidth, CommonUtils.dip2px(mContext, 7));
        holder.mThisProgress.setLayoutParams(params1);
        GradientDrawable drawable1 = CommonUtils.setBackgroundShap(mContext, 10, "#00A9FC", "#00A9FC");
        holder.mThisProgress.setBackground(drawable1);

        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(lastWidth, CommonUtils.dip2px(mContext, 7));
        holder.mLastProgress.setLayoutParams(params2);
        GradientDrawable drawable2 = CommonUtils.setBackgroundShap(mContext, 10, "#A1C0D2", "#A1C0D2");
        holder.mLastProgress.setBackground(drawable2);

        mStatus = nextVo.getStatus();
        if (mStatus.equals("1")) {
            mStatusStr = "进行中";
            holder.mTaskImage.setImageResource(R.drawable.boss_statistics_task1);
        } else if (mStatus.equals("2")) {
            mStatusStr = "待初审";
            holder.mTaskImage.setImageResource(R.drawable.boss_statistics_task2);
        } else if (mStatus.equals("3")) {
            mStatusStr = "待定审";
            holder.mTaskImage.setImageResource(R.drawable.boss_statistics_task3);
        } else if (mStatus.equals("4")) {
            mStatusStr = "完成";
            holder.mTaskImage.setImageResource(R.drawable.boss_statistics_task4);
        } else if (mStatus.equals("5")) {
            mStatusStr = "超期";
            holder.mTaskImage.setImageResource(R.drawable.boss_statistics_task5);
        }
        holder.mTaskStatus.setText(mStatusStr);

        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                BossStatisticsAttendanceVO listVo = mList.get(position);
                String status = listVo.getStatus();
                String statusName = mFixedMap.get(status);
                Intent intent = new Intent();
                intent.putExtra("currentDate", mCurrentDate);
                intent.putExtra("nextTitle", statusName);
                intent.putExtra("currentStatusid", status);
                intent.setClass(mContext,
                        EXTTaskStatisticsEveryoneListActivity.class);
                mContext.startActivity(intent);

            }
        });
        return convertView;
    }

    /**
     * 初始化
     */
    private void setFixedMap() {
        // TODO Auto-generated method stub management
        mFixedMap = new HashMap<String, String>();
        // 状态（1"进行中", 2"待初审", 3"待定审", 4"已完成", 5"已超期）
        // 0全部 1我发布的 2我负责的 3我跟进的 4知会我的
        mFixedMap.put("1", "进行中");
        mFixedMap.put("2", "待初审");
        mFixedMap.put("3", "待定审");
        mFixedMap.put("4", "完成");
        mFixedMap.put("5", "超期");
    }

    static class ViewHolder {
        private TextView mTaskStatus, mThisMonth, mProgressCount1, mLastMonth, mProgressCount2;
        private ImageView mTaskImage;
        private TextView mThisProgress, mLastProgress;
    }

    @Override
    public void updateData(List<BossStatisticsAttendanceVO> list) {
        if (list == null) {
            super.updateData(list);
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            if (Float.parseFloat(CommonUtils.isNormalData(list.get(i).getNum())) > mMaxCount) {
                mMaxCount = Float.parseFloat(CommonUtils.isNormalData(list.get(i).getNum()));
            }
        }

        super.updateData(list);
    }

}
