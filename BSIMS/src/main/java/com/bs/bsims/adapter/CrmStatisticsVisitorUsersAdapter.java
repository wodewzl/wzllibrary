
package com.bs.bsims.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.CrmStatisticsVisitorVO;
import com.bs.bsims.utils.CommonUtils;

import java.util.List;

public class CrmStatisticsVisitorUsersAdapter extends BSBaseAdapter<CrmStatisticsVisitorVO> {
    private Context mContext;
    private String mCuurentTime = "";
    private String mPreviousTime = "";
    private float mMaxCount = 1;

    public CrmStatisticsVisitorUsersAdapter(Context context) {
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
        CrmStatisticsVisitorVO vo = mList.get(position);
        if (convertView != null && convertView.getTag() == null)
            convertView = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.crm_statistics_lv_visitor_users, null);
            holder.mNameTv = (TextView) convertView.findViewById(R.id.name_tv);
            holder.mCountTv = (TextView) convertView.findViewById(R.id.count_tv);
            holder.mCountPb = (ProgressBar) convertView.findViewById(R.id.count_pb);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mNameTv.setText(vo.getFullname());
        holder.mCountTv.setText(vo.getVisitCount() + " 次跟进");

        holder.mCountPb.setProgress((int) ((Float.parseFloat(CommonUtils.isNormalData(vo.getVisitCount())) / mMaxCount) * 100));
        // holder.mCountPb.setBackground(CommonUtils.setBackgroundShap(mContext, 10, R.color.C3,
        // R.color.C3));
        // if (position < 3) {
        // holder.mCountPb.setProgressDrawable(CommonUtils.setBackgroundShap(mContext, 10,
        // R.color.C19, R.color.C19));
        // } else {
        // holder.mCountPb.setProgressDrawable(CommonUtils.setBackgroundShap(mContext, 10,
        // R.color.C7, R.color.C7));
        // }

        if (position < 3) {
            holder.mCountPb.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.progress_drawable_c19));
        } else {
            holder.mCountPb.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.item_taskeventlistadapter_progressdrawable));
        }

        return convertView;
    }

    static class ViewHolder {
        private TextView mNameTv, mCountTv;
        private ProgressBar mCountPb;
    }

    @Override
    public void updateData(List<CrmStatisticsVisitorVO> list) {
        if (list == null) {
            super.updateData(list);
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            if (Float.parseFloat(CommonUtils.isNormalData(list.get(i).getVisitCount())) > mMaxCount) {
                mMaxCount = Float.parseFloat(CommonUtils.isNormalData(list.get(i).getVisitCount()));
            }
        }

        super.updateData(list);
    }
}
