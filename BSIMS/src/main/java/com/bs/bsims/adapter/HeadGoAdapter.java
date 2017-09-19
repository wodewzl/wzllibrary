
package com.bs.bsims.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.view.BSCircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class HeadGoAdapter extends BaseAdapter {
    public List<EmployeeVO> mList;
    private int mState;
    private Context mContext;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;
    private boolean mAdd;
    private boolean approval = false;
    private boolean proven = false;
    private String status;
    private String isone;
    private boolean mIsmore;

    public boolean isProven() {
        return proven;
    }

    public void setProven(boolean proven) {
        this.proven = proven;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isApproval() {
        return approval;
    }

    public void setApproval(boolean approval) {
        this.approval = approval;
    }

    // CcAdapter state 参数为图片边缘的提醒的小图样式
    public HeadGoAdapter(Context context, int state) {
        // TODO Auto-generated constructor stub
        mList = new ArrayList<EmployeeVO>();
        mState = state;
        mContext = context;
        mImageLoader = ImageLoader.getInstance();
    }

    public HeadGoAdapter(Context context, boolean add) {
        mContext = context;
        mAdd = add;
        mList = new ArrayList<EmployeeVO>();
        mImageLoader = ImageLoader.getInstance();
        mOptions = CommonUtils.initImageLoaderOptions();
    }

    public HeadGoAdapter(Context context, boolean add, boolean ismore) {
        mContext = context;
        mAdd = add;
        mIsmore = ismore;
        mList = new ArrayList<EmployeeVO>();
        mImageLoader = ImageLoader.getInstance();
        mOptions = CommonUtils.initImageLoaderOptions();
    }

    public HeadGoAdapter(Context context, boolean add, String isone) {
        mContext = context;
        mAdd = add;
        mList = new ArrayList<EmployeeVO>();
        mImageLoader = ImageLoader.getInstance();
        mOptions = CommonUtils.initImageLoaderOptions();
        this.isone = isone;
    }

    @Override
    public int getCount() {
        if (mAdd) {
            return (mList.size() + 1);
        } else {
            return mList.size();
        }
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
            convertView = View.inflate(mContext, R.layout.gv_head_icon_item, null);

            holder.personHead = (BSCircleImageView) convertView.findViewById(R.id.head_icon);
            holder.personName = (TextView) convertView.findViewById(R.id.person_name);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (mAdd) {
            if (mList.size() == position) {
                if ("1".equals(isone)) {
                    if (mList.size() == 1) {
                        holder.personHead.setVisibility(View.GONE);
                        holder.personName.setVisibility(View.GONE);
                    } else {
                        holder.personHead.setImageResource(R.drawable.add_persion);
                        holder.personHead.setBackground(null);
                        holder.personName.setVisibility(View.GONE);
                    }
                } else {
                    holder.personHead.setBackground(null);
                    holder.personHead.setImageResource(R.drawable.add_persion);
                    holder.personName.setVisibility(View.GONE);
                }

            } else {
                EmployeeVO infoVO = mList.get(position);
                mImageLoader.displayImage(infoVO.getHeadpic(), holder.personHead, CommonUtils.initImageLoaderOptions());
                holder.personHead.setUserId(infoVO.getUserid());//HL
                holder.personName.setText(infoVO.getFullname());
                holder.personName.setVisibility(View.VISIBLE);
            }

        } else {

            if (mList.size() == position) {
                if ("1".equals(isone)) {
                    if (mList.size() == 1) {
                        holder.personHead.setVisibility(View.GONE);
                        holder.personName.setVisibility(View.GONE);
                    } else {
                        holder.personHead.setImageResource(R.drawable.add_persion);
                        holder.personHead.setBackground(null);
                        holder.personName.setVisibility(View.GONE);
                    }
                } else {
                    holder.personHead.setBackground(null);
                    holder.personHead.setImageResource(R.drawable.add_persion);
                    holder.personName.setVisibility(View.GONE);
                }

            } else {
                EmployeeVO infoVO = mList.get(position);
                mImageLoader.displayImage(infoVO.getHeadpic(), holder.personHead, mOptions);
                holder.personHead.setUserId(infoVO.getUserid());//HL
                holder.personName.setText(infoVO.getFullname());
                if (infoVO.getIsread() != null) {
                    if ("1".equals(infoVO.getIsread())) {
                        holder.personHead.setColor(3);
                        holder.personHead.setSemicircleNumber("阅");
                    }
                }

                if (approval) {

                    if ("1".equals(infoVO.getStatus())) {
                        holder.personHead.setColor(1);
                        holder.personHead.setSemicircleNumber("批");
                    } else if ("2".equals(infoVO.getStatus())) {
                        holder.personHead.setColor(2);
                        holder.personHead.setSemicircleNumber("否");
                    } else {
                        holder.personHead.setColor(3);
                        holder.personHead.setSemicircleNumber((position + 1) + "");
                    }

                }

                if (proven) {
                    if ("1".equals(infoVO.getProven())) {
                        holder.personHead.setColor(1);
                        holder.personHead.setSemicircleNumber("证");
                    }
                }

            }

        }

        return convertView;
    }

    static class ViewHolder
    {
        private BSCircleImageView personHead;
        private TextView personName;
    }

    public void updateData(List<EmployeeVO> list) {
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

}
