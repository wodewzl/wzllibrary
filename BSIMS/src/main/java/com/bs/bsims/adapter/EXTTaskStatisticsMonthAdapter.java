
package com.bs.bsims.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.EXTTaskStatisticsEveryoneListActivity;
import com.bs.bsims.constant.Constant4TaskEventPath;
import com.bs.bsims.model.StatisticsBossTaskIndex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author peck
 * @Description:
 * @date 2015-6-22 下午3:53:05
 * @email 971371860@qq.com
 * @version V1.0
 */

public class EXTTaskStatisticsMonthAdapter extends BaseAdapter {

    private Context context;
    public List<StatisticsBossTaskIndex> mList;
    public List<StatisticsBossTaskIndex> mLastList;

    private String currentDate;
    private String tStatusStr;
    private String tStatus;

    private Map<String, String> mFixedMap;

    /**
     * 用来控制进度条显示的长度的 比例
     */
    private double processValue;
    private WindowManager wm;

    public EXTTaskStatisticsMonthAdapter(Context context) {
        this.context = context;
        this.mList = new ArrayList<StatisticsBossTaskIndex>();
        this.mLastList = new ArrayList<StatisticsBossTaskIndex>();
        setFixedMap();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context,
                    R.layout.item_task_statistics_month_contrast, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView
                    .findViewById(R.id.img_item_task_statistics_month_contrast);
            holder.mTime = (TextView) convertView
                    .findViewById(R.id.txt_item_task_statistics_month_contrast);
            holder.mColor = (TextView) convertView
                    .findViewById(R.id.txt_item_task_statistics_month_contrast_color);
            holder.mNum = (TextView) convertView
                    .findViewById(R.id.txt_item_task_statistics_month_contrast_num);
            // 上个月
            holder.mName1 = (TextView) convertView
                    .findViewById(R.id.txt_last_item_task_statistics_month_contrast);
            holder.mTime1 = (TextView) convertView
                    .findViewById(R.id.txt_last_item_task_statistics_month_contrast_month);
            holder.mColor1 = (TextView) convertView
                    .findViewById(R.id.txt_last_item_task_statistics_month_contrast_color);
            holder.mNum1 = (TextView) convertView
                    .findViewById(R.id.txt_last_item_task_statistics_month_contrast_num);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        StatisticsBossTaskIndex mData = mList.get(position);
        StatisticsBossTaskIndex mLastData = mLastList.get(position);

        int tempNumL = Integer.parseInt(mData.getNum());
        // if (tempNumL < 5) {
        // tempNumL = tempNumL * 2;
        // }
        int tempLastNumL = Integer.parseInt(mLastData.getNum());
        // if (tempLastNumL < 5) {
        // tempLastNumL = tempLastNumL * 2;
        // }

        holder.mTime.setText(mData.getDate());
        // holder.mColor.setWidth(tempNumL);
        holder.mNum.setText(mData.getNum() + "项");

        holder.mTime1.setText(mLastData.getDate());
        // holder.mColor1.setWidth(tempLastNumL);
        holder.mNum1.setText(mLastData.getNum() + "项");
        holder.mName1.setText(mLastData.getStatus());

        double tempNumWidth = Integer.parseInt(mData.getNum());

        /**
         * 具体到每一项 则用每一项值乘于该比例 进行显示
         */
        if (processValue > 0) {
            // tempNumWidth = (double) (tempNumL * processValue) - 150;
            tempNumWidth = (double) (tempNumL * processValue);
            holder.mColor.setWidth((int) tempNumWidth / 20);
            // tempNumWidth = (double) (tempLastNumL * processValue) - 150;
            tempNumWidth = (double) (tempLastNumL * processValue);
            holder.mColor1.setWidth((int) tempNumWidth / 20);
        }
        else{
             holder.mColor.setWidth(0);
             // tempNumWidth = (double) (tempLastNumL * processValue) - 150;
             holder.mColor1.setWidth(0);
        }

        tStatus = mLastData.getStatus();
        tStatusStr = mLastData.getStatus();
        // task_statistics_month_statusid01
        int imgId = R.drawable.task_statistics_month_statusid01;
        if (Constant4TaskEventPath.TASKEVENTLIST_STATUSID1
                .equalsIgnoreCase(tStatus)) {
            tStatusStr = "进行中";
            imgId = R.drawable.task_statistics_month_statusid01;
        }
        if (Constant4TaskEventPath.TASKEVENTLIST_STATUSID2
                .equalsIgnoreCase(tStatus)) {
            tStatusStr = "待初审";
            imgId = R.drawable.task_statistics_month_statusid02;
        }
        if (Constant4TaskEventPath.TASKEVENTLIST_STATUSID3
                .equalsIgnoreCase(tStatus)) {
            tStatusStr = "待定审";
            imgId = R.drawable.task_statistics_month_statusid03;
        }
        if (Constant4TaskEventPath.TASKEVENTLIST_STATUSID4
                .equalsIgnoreCase(tStatus)) {
            tStatusStr = "完成";
            imgId = R.drawable.task_statistics_month_statusid04;
        }
        if (Constant4TaskEventPath.TASKEVENTLIST_STATUSID5
                .equalsIgnoreCase(tStatus)) {
            tStatusStr = "超期";
            imgId = R.drawable.task_statistics_month_statusid05;
        }
        holder.mName1.setText(tStatusStr);
        holder.image.setBackgroundResource(imgId);
        // convertView.setTag(tStatus);

        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                StatisticsBossTaskIndex mStatisticsBossTaskIndex = mList
                        .get(position);
                tStatus = mStatisticsBossTaskIndex.getStatus();
                tStatusStr = mFixedMap.get(tStatus);
                Intent intent = new Intent();
                intent.putExtra("currentDate", currentDate);
                intent.putExtra("nextTitle", tStatusStr);
                intent.putExtra("currentStatusid", tStatus);
                intent.setClass(context,
                        EXTTaskStatisticsEveryoneListActivity.class);
                context.startActivity(intent);

            }
        });

        return convertView;
    }

    static class ViewHolder {
        private TextView mTime, mType, mColor, mNum;
        private TextView mName1, mContent1, mTime1, mType1, mColor1, mNum1;
        private ImageView image;
    }

    public void updateData(List<StatisticsBossTaskIndex> list,
            List<StatisticsBossTaskIndex> lastList) {

        int i = 0;
        for (; i < list.size(); i++) {
            if (list.get(i).getNum().equals("0") && lastList.get(i).getNum().equals("0")) {
                list.remove(i);
                lastList.remove(i);
                i = 0;
            }
            else {
                continue;
            }
        }
        mList.clear();
        mLastList.clear();
        mList.addAll(list);
        mLastList.addAll(lastList);
        this.notifyDataSetChanged();
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    /**
     * 初始化
     */
    private void setFixedMap() {
        // TODO Auto-generated method stub management
        mFixedMap = new HashMap<String, String>();
        // 状态（0"全部状态" ,1"进行中", 2"待初审", 3"待定审", 4"已完成", 5"已超期）
        // 0全部 1我发布的 2我负责的 3我跟进的 4知会我的
        mFixedMap.put("0", "全部状态");
        mFixedMap.put("1", "进行中");
        mFixedMap.put("2", "待初审");
        mFixedMap.put("3", "待定审");
        mFixedMap.put("4", "已完成");
        mFixedMap.put("5", "已超期");
    }

    public double getProcessValue() {
        return processValue;
    }

    public void setProcessValue(double processValue) {
        this.processValue = processValue;
    }
}
