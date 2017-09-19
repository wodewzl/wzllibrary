
package com.bs.bsims.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.DanganWorkRewards;
import com.bs.bsims.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class DanganAllWorkChilrdRreawdsApdater extends BaseAdapter {
    private Context mContext;
    public List<DanganWorkRewards> mList;

    public DanganAllWorkChilrdRreawdsApdater(Context context) {
        mContext = context;
        mList = new ArrayList<DanganWorkRewards>();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext,
                    R.layout.da_workallchrildreawards_apdater, null);
            holder.rewardstime = (TextView) convertView
                    .findViewById(R.id.rewardstime);
            holder.rewardsinfohead = (TextView) convertView
                    .findViewById(R.id.rewardsinfohead);
            holder.rewardsinfo = (TextView) convertView
                    .findViewById(R.id.rewardsinfo);
            holder.rewardscausehead = (TextView) convertView
                    .findViewById(R.id.rewardscausehead);
            holder.rewardscause = (TextView) convertView
                    .findViewById(R.id.rewardscause);
            holder.rclass = (ImageView) convertView.findViewById(R.id.rclass);
            holder.rclass1 = (TextView) convertView.findViewById(R.id.rclass1);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 给值

        holder.rewardstime.setText(DateUtils.parseDateDay(mList.get(position)
                .getTime()));
        // 如果是奖励
        if (mList.get(position).getRclass().toString().equals("2")) {
            holder.rewardsinfohead.setText("奖励内容:");
            holder.rewardsinfo.setText(mList.get(position).getContent());
            holder.rewardscausehead.setText("奖励原因:");
            holder.rewardscause.setText(mList.get(position).getCause());
            holder.rclass.setBackgroundResource(R.drawable.da_jiangli);
            holder.rclass1.setText("奖励");
            holder.rclass1.setTextColor(mContext.getResources().getColor(
                    R.color.red));
        }
        // 如果是惩罚
        else {
            holder.rewardsinfohead.setText("惩罚内容:");
            holder.rewardsinfo.setText(mList.get(position).getInfo());
            holder.rewardscausehead.setText("惩罚原因:");
            holder.rewardscause.setText(mList.get(position).getCause());
            holder.rclass.setBackgroundResource(R.drawable.da_chenfa);
            holder.rclass1.setText("惩罚");
            holder.rclass1.setTextColor(mContext.getResources().getColor(
                    R.color.quanqing));
        }

        return convertView;
    }

    static class ViewHolder {
        private TextView rewardstime;
        private TextView rewardsinfohead;
        private TextView rewardsinfo;
        private TextView rewardscausehead;
        private TextView rewardscause;
        private ImageView rclass;
        private TextView rclass1;

    }

}
