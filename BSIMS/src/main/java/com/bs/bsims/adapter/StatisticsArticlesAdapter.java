
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
import com.bs.bsims.activity.StatisticsArticlesDetailActivity;
import com.bs.bsims.model.StatisticsArticlesVO;

import java.util.ArrayList;
import java.util.List;

public class StatisticsArticlesAdapter extends BaseAdapter {

    private Context mContext;
    public List<StatisticsArticlesVO> mList;

    public StatisticsArticlesAdapter(Context context) {
        this.mContext = context;
        this.mList = new ArrayList<StatisticsArticlesVO>();

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
            convertView = View.inflate(mContext, R.layout.lv_statistics_articles, null);
            holder.noContent = (LinearLayout) convertView.findViewById(R.id.no_content_layout);
            holder.content = (LinearLayout) convertView.findViewById(R.id.content);
            holder.rank = (TextView) convertView.findViewById(R.id.rank);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            holder.articles = (TextView) convertView.findViewById(R.id.articels);
            holder.total_price = (TextView) convertView.findViewById(R.id.total_price);
            holder.unit_price_title = (TextView) convertView.findViewById(R.id.unit_price_title);
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

        int rank = position + 1;
        if (rank == 1) {
            holder.rank.setBackgroundResource(R.drawable.corners_notice_unread);
        } else if (rank == 2) {
            holder.rank.setBackgroundResource(R.drawable.corners_brown);
        } else if (rank == 3) {
            holder.rank.setBackgroundResource(R.drawable.corners_greenlow);
        } else {
            holder.rank.setBackgroundResource(R.drawable.corners_blue);
        }

        holder.rank.setText(rank + "");

        StatisticsArticlesVO vo = mList.get(position);
        holder.name.setText(vo.getG_name());
        if ("1".equals(vo.getState())) {
            holder.articles.setText("入" + vo.getTotalnum() + vo.getG_company());
            holder.price.setVisibility(View.GONE);
            holder.unit_price_title.setVisibility(View.GONE);
            holder.name.setTextSize(16);
        } else {
            holder.articles.setText("出" + vo.getTotalnum() + vo.getG_company());
            holder.price.setVisibility(View.VISIBLE);
            holder.unit_price_title.setVisibility(View.VISIBLE);
            holder.name.setTextSize(14);
        }
        int currentCount = Integer.parseInt(vo.getTotalnum());
        int lastCount = Integer.parseInt(vo.getLast_totalnum());
        if (currentCount > lastCount) {
            holder.articles.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.statistics_up), null);
        } else if (currentCount == lastCount) {
            holder.articles.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.statistics_same), null);

        } else {
            holder.articles.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.statistics_down), null);
        }

        holder.price.setText(vo.getGh_price() + "元");
        holder.total_price.setText("共" + vo.getTotal().trim()+"元");
        convertView.setOnClickListener(new StatisticsListeners(mContext, vo));
        return convertView;
    }

    static class ViewHolder {
        private TextView rank, name, price, articles, total_price, unit_price_title;
        private LinearLayout noContent, content;
    }

    public void updateData(List<StatisticsArticlesVO> list) {
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataFrist(List<StatisticsArticlesVO> list) {
        list.addAll(mList);
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataLast(List<StatisticsArticlesVO> list) {
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    private class StatisticsListeners implements OnClickListener {
        private Context mContext;
        private StatisticsArticlesVO mVo;

        public StatisticsListeners(Context context, StatisticsArticlesVO vo) {
            this.mContext = context;
            this.mVo = vo;
        }

        @Override
        public void onClick(View v) {
            if (mList.size() != 0) {
                Intent intent = new Intent();
                intent.setClass(mContext, StatisticsArticlesDetailActivity.class);
                intent.putExtra("state", mVo.getState());
                intent.putExtra("gid", mVo.getGh_gid());
                intent.putExtra("date", mVo.getDate());
                mContext.startActivity(intent);
            }
        }
    }

}
