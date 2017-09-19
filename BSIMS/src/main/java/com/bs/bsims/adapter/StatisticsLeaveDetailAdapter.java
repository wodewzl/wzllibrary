
package com.bs.bsims.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.CreativeIdeaDetailActivity;
import com.bs.bsims.model.ApprovalVO;
import com.bs.bsims.model.StatisticsApprovalVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.view.BSCircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class StatisticsLeaveDetailAdapter extends BaseAdapter {

    private Context mContext;
    public List<StatisticsApprovalVO> mList;
    public ImageLoader mImageLoader;
    public DisplayImageOptions mOptions;
    private int type = 1;

    public boolean isShowDepart = false;

    public boolean isShowDepart() {
        return isShowDepart;
    }

    public void setShowDepart(boolean isShowDepart) {
        this.isShowDepart = isShowDepart;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public StatisticsLeaveDetailAdapter(Context context) {
        this.mContext = context;
        this.mList = new ArrayList<StatisticsApprovalVO>();
        mImageLoader = ImageLoader.getInstance();
        mOptions = CommonUtils.initImageLoaderOptions();
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
            convertView = View.inflate(mContext, R.layout.lv_statistics_leave_detail, null);
            holder.rank = (TextView) convertView.findViewById(R.id.rank);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.sort = (TextView) convertView.findViewById(R.id.sort);
            holder.noContent = (LinearLayout) convertView.findViewById(R.id.no_content_layout);
            holder.content = (LinearLayout) convertView.findViewById(R.id.content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.noContent.setVisibility(View.GONE);
        holder.content.setVisibility(View.VISIBLE);
        if (mList.size() == 0) {
            holder.noContent.setVisibility(View.VISIBLE);
            holder.content.setVisibility(View.GONE);
            return convertView;
        }

        StatisticsApprovalVO vo = mList.get(position);
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

        holder.name.setText(vo.getFullname());

        switch (this.getType()) {
            case 1:
                CommonUtils.setTextTwoBefore(mContext, holder.sort, vo.getSecond_num() + "次/", vo.getDay_num(), R.color.C6, 1.0f);
                break;
            case 3:
                CommonUtils.setTextTwoBefore(mContext, holder.sort, vo.getSecond() + "次/", vo.getDuration(), R.color.C6, 1.0f);
                break;
            case 5:
                holder.sort.setText(vo.getSecond() + "次");
                break;

            default:
                break;
        }
        holder.sort.setCompoundDrawablePadding(CommonUtils.dip2px(mContext, 5));

        if ("1".equals(vo.getContrast())) {
            holder.sort.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.statistics_down), null);
        } else if ("2".equals(vo.getContrast())) {
            holder.sort.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.statistics_same), null);

        } else if ("3".equals(vo.getContrast())) {
            holder.sort.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.statistics_up), null);

        }

        // holder.sort.setCompoundDrawablePadding(10);
        return convertView;

    }

    static class ViewHolder {
        private TextView rank, name, sort, title, time, money;
        private BSCircleImageView icon;
        private LinearLayout noContent, content;
    }

    public void updateData(List<StatisticsApprovalVO> list) {
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataFrist(List<StatisticsApprovalVO> list) {
        list.addAll(mList);
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataLast(List<StatisticsApprovalVO> list) {
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

}
