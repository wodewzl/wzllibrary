
package com.bs.bsims.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.CreativeIdeaDetailActivity;
import com.bs.bsims.model.ApprovalVO;
import com.bs.bsims.model.StatisticsBossAttendanceIndexShow;
import com.bs.bsims.utils.CommonUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author peck
 * @Description: 10.各部门下详细的考勤统计接口
 * @date 2015-6-25 下午3:40:17
 * @email 971371860@qq.com
 * @version V1.0
 */
public class EXTAttendanceStatisticsDepartmentEAdapter extends BaseAdapter {

    private Context mContext;
    public List<StatisticsBossAttendanceIndexShow> mList;
    public ImageLoader mImageLoader;
    public DisplayImageOptions mOptions;

    private Map<String, String> mFixedMap;

    private String currentDate;
    private String did;
    private String type;
    public boolean isShowDepart = false;

    public boolean isShowDepart() {
        return isShowDepart;
    }

    public void setShowDepart(boolean isShowDepart) {
        this.isShowDepart = isShowDepart;
    }

    public EXTAttendanceStatisticsDepartmentEAdapter(Context context) {
        this.mContext = context;
        this.mList = new ArrayList<StatisticsBossAttendanceIndexShow>();
        mImageLoader = ImageLoader.getInstance();
        mOptions = CommonUtils.initImageLoaderOptions();

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
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext,
                    R.layout.item_attendance_statistics_departmente, null);
            holder.rank = (TextView) convertView
                    .findViewById(R.id.item_attendance_ss_departmente_rank);
            holder.name = (TextView) convertView
                    .findViewById(R.id.item_attendance_ss_departmente_name);
            holder.type = (TextView) convertView
                    .findViewById(R.id.item_attendance_ss_departmente_type);
            holder.sort = (TextView) convertView
                    .findViewById(R.id.item_attendance_ss_departmente_sort);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        StatisticsBossAttendanceIndexShow mData = mList.get(position);

        int rank = position + 1;
        if (rank == 1) {
            holder.rank.setBackgroundResource(R.drawable.corners_notice_unread);
        } else if (rank == 2) {
            holder.rank.setBackgroundResource(R.drawable.corners_brown);
        } else if (rank == 3) {
            holder.rank.setBackgroundResource(R.drawable.corners_yellow);
        } else {
            holder.rank.setBackgroundResource(R.drawable.corners_blue);
        }

        holder.rank.setText(rank + "");

        holder.name.setText(mData.getFullname());

        if (isShowDepart) {
            holder.type.setText(mData.getDname());
        } else {
            holder.type.setText(mFixedMap.get(mData.getType()));
        }

        holder.sort.setText(mData.getNum() + mData.getUnit());
        setDrable(holder.sort, mData.getContrast());

        // convertView.setOnClickListener(new OnClickListener() {
        //
        // @Override
        // public void onClick(View view) {
        // // TODO Auto-generated method stub
        // StatisticsBossAttendanceIndexShow mSBossAttendanceIndexShow = mList
        // .get(position);
        // did = mSBossAttendanceIndexShow.getDid();
        // type = mSBossAttendanceIndexShow.getType();
        // // Intent intent = new Intent();
        // // intent.putExtra("currentDate", currentDate);
        // // intent.putExtra("nextTitle", tStatusStr);
        // // intent.putExtra("currentStatusid", tStatus);
        // // intent.setClass(context,
        // // EXTTaskStatisticsEveryoneListActivity.class);
        // // mContext.startActivity(intent);
        // }
        // });

        return convertView;
    }

    static class ViewHolder {
        private TextView rank, name, sort, type;
    }

    public void updateData(List<StatisticsBossAttendanceIndexShow> list) {
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataFrist(List<StatisticsBossAttendanceIndexShow> list) {
        list.addAll(mList);
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataLast(List<StatisticsBossAttendanceIndexShow> list) {
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    private class IdeaListeners implements OnClickListener {
        private Context mContext;
        private ApprovalVO mApprovalVO;

        public IdeaListeners(Context context, ApprovalVO approvalVO) {
            this.mContext = context;
            this.mApprovalVO = approvalVO;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(mContext, CreativeIdeaDetailActivity.class);
            // intent.putExtra("type", mApprovalVO.getType());
            // intent.putExtra("id", mApprovalVO.getArticleid());
            mContext.startActivity(intent);
        }
    }

    public void setDrable(TextView tv, String contrast) {

        if ("1".equals(contrast)) {
            tv.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext
                    .getResources().getDrawable(R.drawable.statistics_down),
                    null);
        } else if ("2".equals(contrast)) {
            tv.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext
                    .getResources().getDrawable(R.drawable.statistics_same),
                    null);
        } else {
            tv.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext
                    .getResources().getDrawable(R.drawable.statistics_up), null);

        }
        tv.setCompoundDrawablePadding(5);
    }

    /**
     * 初始化 类型（0全部，1缺日志，2缺卡,3迟到，4早退，5事假，6病假，7(陪)产假，8公休假，9调休假，10婚假，11丧假）
     */
    private void setFixedMap() {
        // TODO Auto-generated method stub management
        mFixedMap = new HashMap<String, String>();
        mFixedMap.put("0", "全部");
        mFixedMap.put("1", "缺日志");
        mFixedMap.put("2", "缺卡");
        mFixedMap.put("3", "迟到");
        mFixedMap.put("4", "早退");
        mFixedMap.put("5", "事假");
        mFixedMap.put("6", "病假");
        mFixedMap.put("7", "(陪)产假");
        mFixedMap.put("8", "公休假");
        mFixedMap.put("9", "调休假");
        mFixedMap.put("10", "婚假");
        mFixedMap.put("11", "丧假");
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }
}
