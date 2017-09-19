
package com.bs.bsims.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.view.BSCircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class AttendanceAdapter extends BaseAdapter {
    public List<EmployeeVO> mList = null;
    private Context mContext;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;
    private Boolean mAddPerson = false;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AttendanceAdapter(Context mContext) {
        this.mContext = mContext;
        this.mList = new ArrayList<EmployeeVO>();
        mImageLoader = ImageLoader.getInstance();
        mOptions = CommonUtils.initImageLoaderOptions();

    }

    public int getCount() {
        return this.mList.size();
    }

    public Object getItem(int position) {
        return mList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        final EmployeeVO pVo = mList.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.lv_attendance, null);
            viewHolder.iconImage = (BSCircleImageView) view.findViewById(R.id.head_icon);
            viewHolder.nameTv = (TextView) view.findViewById(R.id.name);
            viewHolder.detailTv = (TextView) view.findViewById(R.id.detail);
            viewHolder.departmentTv = (TextView) view.findViewById(R.id.department);
            viewHolder.groupTv = (TextView) view.findViewById(R.id.group);
            viewHolder.mItemLayout = (LinearLayout) view.findViewById(R.id.item_layout);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        mImageLoader.displayImage(pVo.getHeadpic(), viewHolder.iconImage, mOptions);
        viewHolder.iconImage.setUserId(pVo.getUserid());//HL:获取跟单详情界面头像对应的用户ID，以便响应跳转
        viewHolder.nameTv.setText(pVo.getFullname());

        if ("1".equals(getType())) {
            CommonUtils.setDifferentTextColor(viewHolder.detailTv, "未写日志：", pVo.getTitle(), "#ff0000");
        } else if ("2".equals(getType())) {
            CommonUtils.setDifferentTextColor(viewHolder.detailTv, "缺卡次数：", pVo.getTitle(), "#ff0000");
        } else {
            CommonUtils.setDifferentTextColor(viewHolder.detailTv, "打卡时间：", pVo.getTitle(), "#ff0000");
        }

        viewHolder.departmentTv.setText(pVo.getPname());
        viewHolder.groupTv.setText(pVo.getDname());

        return view;

    }

    static class ViewHolder {
        TextView tvLetter;
        // TextView tvTitle;

        private TextView nameTv, detailTv, departmentTv, groupTv;
        private BSCircleImageView iconImage;
        private LinearLayout mItemLayout;
    }

    public void updateData(List<EmployeeVO> list) {
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataFrist(List<EmployeeVO> list) {
        list.addAll(mList);
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataLast(List<EmployeeVO> list) {
        mList.addAll(list);
        this.notifyDataSetChanged();
    }
}
