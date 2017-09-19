
package com.bs.bsims.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.DanganWorkTrains;
import com.bs.bsims.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class DanganAllWorkChilrdTranisApdater extends BaseAdapter {

    private Context mContext;
    public List<DanganWorkTrains> mList;

    public DanganAllWorkChilrdTranisApdater(Context context) {
        mContext = context;
        mList = new ArrayList<DanganWorkTrains>();
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
                    R.layout.da_workallchrildtrians_apdater, null);
            holder.trainTitle = (TextView) convertView
                    .findViewById(R.id.trainTitle);
            holder.Udscore = (TextView) convertView.findViewById(R.id.Udscore);
            holder.trainStartime = (TextView) convertView

                    .findViewById(R.id.trainStartime);
            holder.Udabsence = (TextView) convertView
                    .findViewById(R.id.Udabsence);
            holder.Udlateimg = (ImageView) convertView
                    .findViewById(R.id.Udlateimg);
            holder.usercodetitle = (LinearLayout) convertView
                    .findViewById(R.id.usercodetitle);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 给值
        holder.trainTitle.setText(mList.get(position).getTrainTitle());

        // 判断分数有作业 && !(
        if (mList.get(position).getTrainWork().equals("1")) {
            if (mList.get(position).getUdscore().equals("-1")) {
                holder.Udscore.setText("未评分");
            }
            else {
                holder.Udscore.setText(mList.get(position).getUdscore() + "分");
            }

        } else {
            holder.usercodetitle.setVisibility(View.GONE);
        }

        if (!mList.get(position).getTrainStartime().equals("")) {
            holder.trainStartime.setText(DateUtils.parseDateDay(mList.get(
                    position).getTrainStartime()));
        } else {
            holder.trainStartime.setVisibility(View.GONE);
        }

        // 在不缺勤的情况下判断有没有迟到
        if (mList.get(position).getUdabsence().equals("0")) {
            // 如果没有迟到
            if (mList.get(position).getUdlate().equals("0")) {
                // 设置全勤图标
                holder.Udlateimg
                        .setBackgroundResource(R.drawable.peixunquanqing);
                holder.Udabsence.setText("全勤");
                holder.Udabsence.setTextColor(mContext.getResources().getColor(
                        R.color.quanqing));
            }
            // 迟到
            else {
                // 设置迟到图标
                holder.Udlateimg.setBackgroundResource(R.drawable.peixunchidao);
                holder.Udabsence.setText("迟到"
                        + mList.get(position).getUdlatetime() + "分钟");
            }

        } else {
            // 设置缺勤迟到图标
            holder.Udlateimg.setBackgroundResource(R.drawable.peixunqueqing);
            holder.Udabsence.setText("缺勤");

        }

        return convertView;
    }

    static class ViewHolder {
        private TextView trainTitle;
        private TextView Udscore;
        private TextView trainStartime;
        private TextView Udabsence;
        private ImageView Udlateimg;
        private LinearLayout usercodetitle;

    }

}
