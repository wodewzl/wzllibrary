
package com.bs.bsims.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.DiscussLeadVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.DateUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class DiscussLeadAdapter extends BaseAdapter {
    private Context mContext;
    public ArrayList<DiscussLeadVO> mList;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;

    public DiscussLeadAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<DiscussLeadVO>();
        this.mImageLoader = ImageLoader.getInstance();
        mOptions = CommonUtils.initImageLoaderOptions();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.lv_lead_discuss_item, null);
            holder.personHead = (ImageView) convertView.findViewById(R.id.person_head);
            holder.personName = (TextView) convertView.findViewById(R.id.name);
            holder.discussTime = (TextView) convertView.findViewById(R.id.discuss_tiem);
            holder.discussContent = (TextView) convertView.findViewById(R.id.discuss_content);
            holder.recordLayout = (LinearLayout) convertView.findViewById(R.id.record_layout);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DiscussLeadVO discussVO = mList.get(position);
        // holder.personHead.setImageResource(R.drawable.head_icon);
        // mImageCache.displayImage(holder.personHead, discussVO.getHeadpic(),
        // R.drawable.head_icon);
        mImageLoader.displayImage(discussVO.getHeadpic(), holder.personHead, mOptions);
        holder.personName.setText(discussVO.getFullname());
        long time = Long.parseLong(discussVO.getTime()) * 1000;
        holder.discussTime.setText(DateUtils.parseDate(time));
        if ("0".equals(discussVO.getSort())) {
            holder.discussContent.setText(discussVO.getContent());
            holder.discussContent.setVisibility(View.VISIBLE);
            holder.recordLayout.setVisibility(View.GONE);
        } else {
            holder.discussContent.setVisibility(View.GONE);
            holder.recordLayout.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    static class ViewHolder
    {
        private ImageView personHead;
        private TextView personName;
        private TextView discussTime;
        private TextView discussContent;
        private LinearLayout recordLayout;

    }

}
