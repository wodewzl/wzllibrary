
package com.bs.bsims.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.DiscussVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.view.BSCircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class CreativeIdeaAdapter extends BaseAdapter {

    public List<DiscussVO> mList;
    private int mState;
    private Context mContext;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;
    private Boolean mAdd;
    private String mType;

    // CcAdapter state 参数为图片边缘的提醒的小图样式
    public CreativeIdeaAdapter(Context context) {
        // TODO Auto-generated constructor stub
        mList = new ArrayList<DiscussVO>();
        mContext = context;
        mImageLoader = ImageLoader.getInstance();
        mOptions = CommonUtils.initImageLoaderOptions();
    }

    public CreativeIdeaAdapter(Context context, String type) {
        // TODO Auto-generated constructor stub
        mList = new ArrayList<DiscussVO>();
        mContext = context;
        mImageLoader = ImageLoader.getInstance();
        mOptions = CommonUtils.initImageLoaderOptions();
        this.mType = type;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.approval_idea, null);
            holder.titleName = (TextView) convertView.findViewById(R.id.title_name);
            // tv.setText("当前采纳人：");
            holder.personHead = (BSCircleImageView) convertView.findViewById(R.id.head_icon);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.status = (TextView) convertView.findViewById(R.id.status);
            holder.content = (TextView) convertView.findViewById(R.id.content);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DiscussVO employeeVO = mList.get(position);
        holder.personHead.setUserId(employeeVO.getUserid());//获取头像对应用户ID，给圆形头像点击事件
        holder.titleName.setText("采纳人：");
        mImageLoader.displayImage(employeeVO.getHeadpic(), holder.personHead, mOptions);
        holder.name.setText(employeeVO.getFullname());
        holder.content.setText(employeeVO.getContent());
        if ("2".equals(employeeVO.getIfcheck())) {
            holder.status.setTextColor(Color.parseColor("#00A9FE"));
            holder.status.setText("已采纳");
        } else {
            holder.status.setText("未采纳");
            holder.status.setTextColor(Color.parseColor("#FD0102"));
        }
        return convertView;
    }

    static class ViewHolder
    {
        private BSCircleImageView personHead;
        private TextView name, status, content, titleName;
    }

    public void updateData(List<DiscussVO> list) {
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

}
