
package com.beisheng.synews.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.adapter.BSBaseAdapter;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.DateUtils;
import com.beisheng.synews.activity.WebViewActivity;
import com.beisheng.synews.mode.BrokeVO;
import com.beisheng.synews.mode.LiveVO;
import com.im.zhsy.R;
import com.im.zhsy.R.id;

public class BrokeAdapter extends BSBaseAdapter<BrokeVO> {
    private int currentPosition = -1;

    public BrokeAdapter(Context context) {
        super(context);
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (mIsEmpty) {
            return super.getView(position, convertView, parent);
        }

        if (convertView != null && convertView.getTag() == null)
            convertView = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.broke_adapter, null);
            holder.titleTv = (TextView) convertView.findViewById(R.id.title_tv);
            holder.titleContentTv = (TextView) convertView.findViewById(R.id.title_content_tv);
            holder.timeTv = (TextView) convertView.findViewById(R.id.time_tv);
            holder.rewardTv = (TextView) convertView.findViewById(R.id.reward_tv);
            holder.statusTv = (TextView) convertView.findViewById(id.status_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final BrokeVO vo = (BrokeVO) mList.get(position);

        holder.titleContentTv.setText("                 " + vo.getTitle());
        if (vo.getCreatime() != null) {
            holder.timeTv.setText(DateUtils.parseMDHM(vo.getCreatime()));
        }
        if ("1".equals(vo.getState())) {
            holder.statusTv.setText("未处理");
            holder.statusTv.setBackground(BaseCommonUtils.setBackgroundShap(mContext, 5, R.color.sy_title_color, R.color.C1));
            holder.statusTv.setTextColor(mContext.getResources().getColor(R.color.sy_title_color));
        } else {
            holder.statusTv.setText("已处理");
            holder.statusTv.setBackground(BaseCommonUtils.setBackgroundShap(mContext, 5, R.color.C12, R.color.C1));
            holder.statusTv.setTextColor(mContext.getResources().getColor(R.color.C12));

        }

        if (vo.getReward() != null) {
            holder.rewardTv.setBackground(BaseCommonUtils.setBackgroundShap(mContext, 5, R.color.sy_title_color, R.color.C1));
            holder.rewardTv.setText(vo.getReward());
            holder.rewardTv.setVisibility(View.VISIBLE);
        } else {
            holder.rewardTv.setVisibility(View.GONE);
        }

        // BaseCommonUtils.setTextThree(mContext, holder.titleTv, "【", "爆料", "】", R.color.C6, 1.0f);

        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Bundle bundle = new Bundle();
                LiveVO livevo = new LiveVO();
                livevo.setSuburl("8");
                livevo.setLid(vo.getId());
                livevo.setTitle(vo.getTitle());
                livevo.setLink(vo.getLink());
                livevo.setComments(vo.getComments());
                livevo.setShare_tit(vo.getTitle());
                livevo.setShare_url(vo.getLink());
                bundle.putString("url", vo.getLink());
                bundle.putSerializable("livevo", livevo);
                bundle.putString("name", "爆料详情");
                ((BaseActivity) mContext).openActivity(WebViewActivity.class, bundle, 0);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        private TextView titleTv, titleContentTv, timeTv, rewardTv, statusTv;
    }

}
