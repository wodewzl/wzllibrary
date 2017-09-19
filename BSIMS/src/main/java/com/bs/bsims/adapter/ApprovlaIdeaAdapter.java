
package com.bs.bsims.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
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

public class ApprovlaIdeaAdapter extends BaseAdapter {

    public List<EmployeeVO> mList;
    private int mState;
    private Context mContext;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;
    private Boolean mAdd;
    private String statusType = "0";// 0是默认1是合同审批，回款审批

    // CcAdapter state 参数为图片边缘的提醒的小图样式
    public ApprovlaIdeaAdapter(Context context) {
        // TODO Auto-generated constructor stub
        mList = new ArrayList<EmployeeVO>();
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

    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.approval_idea, null);
            holder.personHead = (BSCircleImageView) convertView.findViewById(R.id.head_icon);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.status = (TextView) convertView.findViewById(R.id.status);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            holder.titleName = (TextView) convertView.findViewById(R.id.title_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        EmployeeVO employeeVO = mList.get(position);
        mImageLoader.displayImage(employeeVO.getHeadpic(), holder.personHead, mOptions);
        holder.personHead.setUserId(employeeVO.getUserid());//HL:获取头像对应的用户ID，以便响应跳转
        holder.personHead.setUserName(employeeVO.getFullname());
        holder.personHead.setmImageLoader(mImageLoader);
        holder.personHead.setUrl(employeeVO.getHeadpic());
        holder.name.setText(employeeVO.getFullname());
        holder.content.setText(employeeVO.getContent());
        if ("1".equals(employeeVO.getStatus())) {
            holder.status.setTextColor(Color.parseColor("#00A9FE"));
            holder.status.setText("已批准");
            // 合同与回款
            if ("1".equals(this.getStatusType())) {
                holder.status.setText("已确定");
                holder.titleName.setText("审核人：");
            }
        } else {
            holder.status.setText("未批");
            holder.status.setTextColor(Color.parseColor("#FD0102"));
            // 合同与回款
            if ("1".equals(this.getStatusType())) {
                holder.status.setText("已否定");
                holder.titleName.setText("审核人：");
            }
        }

        return convertView;
    }

    static class ViewHolder
    {
        private BSCircleImageView personHead;
        private TextView name, status, content, titleName;
    }

    public void updateData(List<EmployeeVO> list) {
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }

}
