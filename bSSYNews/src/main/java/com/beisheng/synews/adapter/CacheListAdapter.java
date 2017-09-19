
package com.beisheng.synews.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beisheng.base.adapter.BSBaseAdapter;
import com.beisheng.base.utils.DateUtils;
import com.beisheng.synews.mode.CacheListVO;
import com.im.zhsy.R;

public class CacheListAdapter extends BSBaseAdapter<CacheListVO> {
    private int currentPosition = -1;

    public CacheListAdapter(Context context) {
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
            convertView = View.inflate(mContext, R.layout.cache_list_adapter, null);
            holder.titleTv = (TextView) convertView.findViewById(R.id.title_tv);
            holder.timeTv = (TextView) convertView.findViewById(R.id.time_tv);
            holder.readTv = (TextView) convertView.findViewById(R.id.read_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final CacheListVO vo = (CacheListVO) mList.get(position);

        if ("0".equals(vo.getSuburl())) {
            holder.titleTv.setText("  【" + "新闻" + "】" + vo.getTitle());
        } else if ("1".equals(vo.getSuburl())) {
            holder.titleTv.setText("  【" + "论坛" + "】" + vo.getTitle());
        } else if ("2".equals(vo.getSuburl())) {
            holder.titleTv.setText("  【" + "图片" + "】" + vo.getTitle());
        } else if ("3".equals(vo.getSuburl())) {
            holder.titleTv.setText("  【" + "专题" + "】" + vo.getTitle());
        } else if ("4".equals(vo.getSuburl())) {
            holder.titleTv.setText("  【" + "视频" + "】" + vo.getTitle());
        } else if ("5".equals(vo.getSuburl())) {
            holder.titleTv.setText("  【" + "直播" + "】" + vo.getTitle());
        } else if ("6".equals(vo.getSuburl())) {
            // holder.titleTv.setText("  【" + "社区" + "】" + vo.getTitle());
        } else if ("7".equals(vo.getSuburl())) {
            holder.titleTv.setText("  【" + "日报" + "】" + vo.getTitle());
        } else if ("8".equals(vo.getSuburl())) {
            holder.titleTv.setText("  【" + "爆料" + "】" + vo.getTitle());
        } else {
            holder.titleTv.setText("  【" + "新闻" + "】" + vo.getTitle());
        }

        if (vo.getCreatime() != null) {
            holder.timeTv.setText(DateUtils.parseMDHM(vo.getCreatime()));
        } else if (vo.getAddtime() != null) {
            holder.timeTv.setText(DateUtils.parseMDHM(vo.getAddtime()));
        } else {
            holder.timeTv.setText(DateUtils.getCurrentTimeMM());
        }

        holder.readTv.setText(vo.getRead());
        return convertView;
    }

    static class ViewHolder {
        private TextView titleTv, timeTv, readTv;
    }

}
