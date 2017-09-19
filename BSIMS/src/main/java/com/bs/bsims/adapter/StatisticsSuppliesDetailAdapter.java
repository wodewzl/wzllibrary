
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
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.view.BSCircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class StatisticsSuppliesDetailAdapter extends BaseAdapter {

    private Context mContext;
    public List<StatisticsApprovalVO> mList;
    public ImageLoader mImageLoader;
    public DisplayImageOptions mOptions;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public StatisticsSuppliesDetailAdapter(Context context) {
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
            convertView = View.inflate(mContext, R.layout.lv_statistics_supplies, null);
            holder.icon = (BSCircleImageView) convertView.findViewById(R.id.icon);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.money = (TextView) convertView.findViewById(R.id.money);
            holder.noContent = (LinearLayout) convertView.findViewById(R.id.no_content_layout);
            holder.content = (LinearLayout) convertView.findViewById(R.id.content);
            holder.unit = (TextView) convertView.findViewById(R.id.unit);
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

        mImageLoader.displayImage(vo.getHeadpic(), holder.icon, mOptions);
        holder.icon.setUserId(vo.getUserid());//HL:获取头像对应的用户ID，以便响应跳转
        holder.title.setText(vo.getReason());
        holder.time.setText(vo.getFullname() + "   " + DateUtils.parseMDHM(vo.getAtime()));
        holder.money.setText(CommonUtils.countNumberSplitUnit(vo.getTotalprice()).split(",")[0]);
        holder.unit.setText(CommonUtils.countNumberSplitUnit(vo.getTotalprice()).split(",")[1]);
        return convertView;

    }

    static class ViewHolder {
        private TextView rank, name, sort, type, number, title, time, money, unit;
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
