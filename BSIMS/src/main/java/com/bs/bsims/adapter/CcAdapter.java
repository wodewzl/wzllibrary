
package com.bs.bsims.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.CcVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.view.BSCircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class CcAdapter extends BaseAdapter {
    public ArrayList<CcVO> mList;
    private int mState;
    private Context mContext;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;

    // CcAdapter state 参数为图片边缘的提醒的小图样式
    public CcAdapter(Context context, int state) {
        // TODO Auto-generated constructor stub
        mList = new ArrayList<CcVO>();
        mState = state;
        mContext = context;
        mImageLoader = ImageLoader.getInstance();
        mOptions = CommonUtils.initImageLoaderOptions();
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.send_person_gv_item, null);

            holder.personHead = (BSCircleImageView) convertView.findViewById(R.id.person_head);
            holder.personName = (TextView) convertView.findViewById(R.id.person_name);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CcVO ccVO = mList.get(position);
        if ("1".equals(ccVO.getIsread())) {
            holder.personHead.setColor(3);
            holder.personHead.setSemicircleNumber("阅");
        }
        mImageLoader.displayImage(ccVO.getHeadpic(), holder.personHead, mOptions);
        holder.personHead.setUserId(ccVO.getLcuser());
        holder.personName.setText(ccVO.getFullname());
        return convertView;
    }

    static class ViewHolder
    {
        private BSCircleImageView personHead;
        private TextView personName;
        private ImageView notifyInfo;
    }

}
