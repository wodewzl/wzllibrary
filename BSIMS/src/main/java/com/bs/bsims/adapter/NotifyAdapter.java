
package com.bs.bsims.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.ApprovalAttendanceDetailActivity;
import com.bs.bsims.activity.ApprovalCustomDetailActivity;
import com.bs.bsims.activity.ApprovalFeeApplyDetailActivity;
import com.bs.bsims.activity.ApprovalLeaveDetailActivity;
import com.bs.bsims.activity.ApprovalOvertimeDetailActivity;
import com.bs.bsims.activity.ApprovalSuppliesDetailActivity;
import com.bs.bsims.activity.CreativeIdeaDetailActivity;
import com.bs.bsims.activity.EXTTaskEventDetailsActivity;
import com.bs.bsims.activity.ScheduleDetailActivity;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.ExtrasBSVO;
import com.bs.bsims.model.NotifyVO;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class NotifyAdapter extends BaseAdapter {
    private Context mContext;
    public List<NotifyVO> mList;

    public NotifyAdapter(Context context) {
        this.mContext = context;
        this.mList = new ArrayList<NotifyVO>();
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
            convertView = View.inflate(mContext, R.layout.lv_notify_item, null);
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            holder.type = (TextView) convertView.findViewById(R.id.type);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            // holder.username_content = (TextView) convertView
            // .findViewById(R.id.username_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Intent getdata = new Intent();

        getdata.putExtra("messageid", mList.get(position).getMessageid());
        holder.type.setVisibility(View.VISIBLE);
        switch (Integer.parseInt(mList.get(position).getType())) {
            case 1:
                holder.type.setText("【转正提醒】");
                holder.icon.setImageResource(R.drawable.notify_01);
                // 员工档案下的转正
                break;
            case 2:
                holder.type.setText("【转正提醒】");
                holder.icon.setImageResource(R.drawable.notify_01);
                // 员工档案下的转正
                break;
            case 3:
                holder.type.setText("【调岗提醒】");
                holder.icon.setImageResource(R.drawable.notify_02);
                // 员工档案下的调岗位
                break;
            case 4:
                holder.type.setText("【调薪提醒】");
                holder.icon.setImageResource(R.drawable.notify_03);
                // 员工档案下的薪资情况

                break;
            case 5:
                holder.type.setText("【任务提醒】");
                holder.icon.setImageResource(R.drawable.notify_05);
                // 任务详情
                getdata.putExtra(ExtrasBSVO.Push.BREAK_ID, mList.get(position)
                        .getTypeid());
                getdata.setClass(mContext, EXTTaskEventDetailsActivity.class);
                break;
            case 6:
                holder.type.setText("【任务提醒】");
                holder.icon.setImageResource(R.drawable.notify_05);
                // 任务详情
                getdata.putExtra(ExtrasBSVO.Push.BREAK_ID, mList.get(position)
                        .getTypeid());
                getdata.setClass(mContext, EXTTaskEventDetailsActivity.class);
                break;
            case 7:
                holder.type.setText("【创意/建议提醒】");
                holder.icon.setImageResource(R.drawable.notify_06);
                getdata.putExtra("type", "1");
                getdata.putExtra("isboss", BSApplication.getInstance().getUserFromServerVO().getIsboss());
                getdata.putExtra("id", mList.get(position).getTypeid());
                getdata.setClass(mContext, CreativeIdeaDetailActivity.class);
                // 创意意见详情
                break;
            case 8:
                holder.type.setText("【请假审批提醒】");
                holder.icon.setImageResource(R.drawable.notify_04_01);
                getdata.putExtra("type", "1");
                getdata.putExtra("uid", BSApplication.getInstance().getUserId());
                getdata.putExtra("alid", mList.get(position).getTypeid());
                getdata.setClass(mContext, ApprovalLeaveDetailActivity.class);
                break;
            case 9:
                holder.type.setText("【物资审批提醒】");
                holder.icon.setImageResource(R.drawable.notify_04_02);
                getdata.putExtra("type", "2");
                getdata.putExtra("uid", BSApplication.getInstance().getUserId());
                getdata.putExtra("alid", mList.get(position).getTypeid());
                getdata.setClass(mContext, ApprovalSuppliesDetailActivity.class);
                break;
            case 10:
                holder.type.setText("【加班审批提醒】");
                holder.icon.setImageResource(R.drawable.notify_04_03);
                getdata.putExtra("type", "3");
                getdata.putExtra("uid", BSApplication.getInstance().getUserId());
                getdata.putExtra("alid", mList.get(position).getTypeid());
                getdata.setClass(mContext, ApprovalOvertimeDetailActivity.class);
                break;
            case 11:
                holder.type.setText("【费用审批提醒】");
                holder.icon.setImageResource(R.drawable.notify_04_04);
                getdata.putExtra("type", "4");
                getdata.putExtra("uid", BSApplication.getInstance().getUserId());
                getdata.putExtra("alid", mList.get(position).getTypeid());
                getdata.setClass(mContext, ApprovalFeeApplyDetailActivity.class);
                break;
            case 12:
                holder.type.setText("【考勤申诉提醒】");
                holder.icon.setImageResource(R.drawable.notify_04_05);
                getdata.putExtra("type", "5");
                getdata.putExtra("uid", BSApplication.getInstance().getUserId());
                getdata.putExtra("alid", mList.get(position).getTypeid());
                getdata.setClass(mContext, ApprovalAttendanceDetailActivity.class);
                break;
            case 13:
                holder.type.setText("【其他审批提醒】");
                holder.icon.setImageResource(R.drawable.notify_04_06);
                getdata.putExtra("type", "6");
                getdata.putExtra("uid", BSApplication.getInstance().getUserId());
                getdata.putExtra("alid", mList.get(position).getTypeid());
                getdata.setClass(mContext, ApprovalCustomDetailActivity.class);
                break;
            case 14:
                holder.type.setText("【任务到期提醒】");
                holder.icon.setImageResource(R.drawable.notify_05);
                // 任务详情
                getdata.putExtra(ExtrasBSVO.Push.BREAK_ID, mList.get(position)
                        .getTypeid());
                getdata.setClass(mContext, EXTTaskEventDetailsActivity.class);
                break;

            case 16:
                holder.type.setText("【日程提醒】");
                holder.icon.setImageResource(R.drawable.notify_08);
                getdata.putExtra("id", mList.get(position).getTypeid());
                getdata.setClass(mContext, ScheduleDetailActivity.class);
                break;

            default:
                holder.type.setVisibility(View.GONE);
                break;
        }
        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                int mostkey = Integer.parseInt(mList.get(position).getType());
                switch (mostkey) {
                    case 16:
                    case 5:
                    case 6:
                    case 14:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 7:
                        mContext.startActivity(getdata);
                        break;

                    default:
                        CustomToast.showLongToast(mContext, "暂时没有此类消息~");
                        break;
                }
            }
        });
        // holder.username_content
        // .setText("发布人:" + mList.get(position).getUname());
        holder.content.setText(mList.get(position).getTitle());
        holder.time.setText((DateUtils.parseDateDay(mList.get(position)
                .getSeetime())));

        return convertView;
    }

    static class ViewHolder {
        private ImageView icon;
        private TextView type, time, content, username_content;
    }

    public void updateData(List<NotifyVO> list) {
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataFrist(List<NotifyVO> list) {
        list.addAll(mList);
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataLast(List<NotifyVO> list) {
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

}
