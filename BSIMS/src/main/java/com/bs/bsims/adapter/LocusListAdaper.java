package com.bs.bsims.adapter;

import com.bs.bsims.R;
import com.bs.bsims.model.LocusLineVO;
import com.bs.bsims.view.BSCircleImageView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LocusListAdaper extends BSBaseAdapter<LocusLineVO> {

	public LocusListAdaper(Context context) {
		super(context);
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
        if (mIsEmpty) {
            View view = super.getView(position, convertView, parent);
            return view;
        }

        if (convertView != null && convertView.getTag() == null)
            convertView = null;

        final LocusLineVO vo = mList.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.locusline_adapter, null);
            holder.dateTv = (TextView) convertView.findViewById(R.id.tv_locus_item_date);
            holder.nameTv = (TextView) convertView.findViewById(R.id.tv_locus_item_name);
            holder.jobTv = (TextView) convertView.findViewById(R.id.tv_locus_item_job);
            holder.department = (TextView) convertView.findViewById(R.id.tv_locus_item_department);
            holder.addressTv = (TextView) convertView.findViewById(R.id.tv_locus_item_address);
//            holder.reportLocationTv = (TextView) convertView.findViewById(R.id.tv_locus_item_reportLocation);
            holder.timeTv = (TextView) convertView.findViewById(R.id.tv_locus_item_time);
            holder.headIcon = (BSCircleImageView) convertView.findViewById(R.id.img_locus_item_head_iconbasic);
            
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //判断日期
        holder.dateTv.setText(vo.getDate());
        if (position != 0 && vo.getDate().equals(mList.get(position - 1).getDate())) {
            holder.dateTv.setVisibility(View.GONE);
        } else {
            holder.dateTv.setVisibility(View.VISIBLE);
        }
        //获取头像
        mImageLoader.displayImage(vo.getHeadpic(), holder.headIcon, mOptions);
        //获取姓名
        holder.nameTv.setText(mList.get(position).getFullName());
        //获取性别
        String sex = mList.get(position).getSex();
        
        holder.headIcon.setUserId(vo.getUserid());
        holder.headIcon.setUrl(vo.getHeadpic());
        holder.headIcon.setUserName(vo.getFullName());
        if("男".equals(sex)){
        	//添加性别图片
        	holder.nameTv.setCompoundDrawablesWithIntrinsicBounds(null, null,mContext.getResources().getDrawable(R.drawable.sex_man) , null);
        }else{
        	holder.nameTv.setCompoundDrawablesWithIntrinsicBounds(null, null,mContext.getResources().getDrawable(R.drawable.sex_woman) , null);
        }
        //获取岗位
        holder.jobTv.setText(mList.get(position).getPname());
        //获取部门
//        holder.department.setText(mList.get(position).getDname());
        //获取具体地理位置
        holder.addressTv.setText(mList.get(position).getAddress());
        holder.addressTv.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.crm_client_address_black), null, null, null);
        holder.addressTv.setCompoundDrawablePadding(5);
        //上报位置or移动打卡
        String typeName = mList.get(position).getTypename();
//        holder.reportLocationTv.setCompoundDrawablePadding(5);
//        if("移动打卡".equals(typeName)){
//        	holder.reportLocationTv.setText("移动打卡");
//        	holder.reportLocationTv.setTextColor(mContext.getResources().getColor(R.color.H1));
//        	holder.reportLocationTv.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
//        }else{
//        	holder.reportLocationTv.setText("上报位置");
//        	holder.reportLocationTv.setTextColor(mContext.getResources().getColor(R.color.H2));
//         }
        
//        if(null!=vo.getImgs()){
//            holder.reportLocationTv.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.is_topaddress), null, null, null);
//        }
//        else{
//            holder.reportLocationTv.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
//        }
        
        //时间
        holder.timeTv.setText(mList.get(position).getDatetime());
        return convertView;
	}
	
	static class ViewHolder {
		//日期、姓名、岗位、部门、详细地理地址、上报位置/移动打卡、时间
        public TextView dateTv, nameTv, jobTv, department,addressTv, reportLocationTv, timeTv;
        public BSCircleImageView headIcon;//头像图片
    }

}
