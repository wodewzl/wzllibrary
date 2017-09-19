
package com.bs.bsims.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.BossStatisticsClientVO;
import com.bs.bsims.utils.CommonUtils;

import java.util.List;

public class BossStatisticsHighSeasAdapter extends BSBaseAdapter<BossStatisticsClientVO> {
    private Context mContext;
    private float mMaxCount = 1;

    public float getmMaxCount() {
        return mMaxCount;
    }

    public void setmMaxCount(float mMaxCount) {
        this.mMaxCount = mMaxCount;
    }

    public BossStatisticsHighSeasAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(int arg0, View convertView, ViewGroup arg2) {
        final ViewHolder holder;
        if (mIsEmpty) {
            return super.getView(arg0, convertView, arg2);
        }
        if (convertView != null && convertView.getTag() == null)
            convertView = null;

        BossStatisticsClientVO Vo = mList.get(arg0);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.boss_statistics_highseas_adapter, null);
            holder.person_name = (TextView) convertView.findViewById(R.id.person_name);
            holder.giveup_count = (TextView) convertView.findViewById(R.id.giveup_count);
            holder.line_text = (TextView) convertView.findViewById(R.id.line_text);
            holder.progress_text = (TextView) convertView.findViewById(R.id.progress_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.person_name.setText(Vo.getFullname());
        holder.giveup_count.setText(Vo.getCount());
        // 横条相关方法设置
        int maxWidth = (CommonUtils.getScreenWidth(mContext) * 3) / 5;// 手机屏幕宽度的五分之三
        int mCountWidth;
        if (mMaxCount != 0) {
            mCountWidth = (int) ((Integer.valueOf(Vo.getCount()) * maxWidth) / mMaxCount);// 求横条长度
        } else {
            mCountWidth = maxWidth;
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mCountWidth, CommonUtils.dip2px(mContext, 16));
        holder.progress_text.setLayoutParams(params);
        if (arg0 < 3) {
            GradientDrawable drawable = CommonUtils.setBackgroundShap(mContext, 0, "#FF776D", "#FF776D");
            holder.progress_text.setBackground(drawable);
        } else {
            GradientDrawable drawable = CommonUtils.setBackgroundShap(mContext, 0, "#00A8FF", "#00A8FF");
            holder.progress_text.setBackground(drawable);
        }

        if (arg0 == mList.size() - 1) {
            holder.line_text.setVisibility(View.GONE);
        } else {
            holder.line_text.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    static class ViewHolder {
        private TextView person_name, giveup_count, line_text, progress_text;
        private ProgressBar count_pb;
    }

    @Override
    public void updateData(List<BossStatisticsClientVO> list) {
        if (list == null) {
            super.updateData(list);
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            if (Float.parseFloat(CommonUtils.isNormalData(list.get(i).getCount())) > mMaxCount) {
                mMaxCount = Float.parseFloat(CommonUtils.isNormalData(list.get(i).getCount()));
            }
        }

        super.updateData(list);
    }

}
