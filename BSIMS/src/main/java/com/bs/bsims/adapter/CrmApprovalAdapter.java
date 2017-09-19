
package com.bs.bsims.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.CreativeIdeaDetailActivity;
import com.bs.bsims.model.ApprovalVO;
import com.bs.bsims.model.CrmApprovalVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.view.BSCircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class CrmApprovalAdapter extends BaseAdapter {

    private Context mContext;
    public List<CrmApprovalVO> mList;
    public ImageLoader mImageLoader;
    public DisplayImageOptions mOptions;

    public CrmApprovalAdapter(Context context) {
        this.mContext = context;
        this.mList = new ArrayList<CrmApprovalVO>();
        mImageLoader = ImageLoader.getInstance();
        mOptions = CommonUtils.initImageLoaderOptions();
        // registBroadcast();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
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
            convertView = View.inflate(mContext, R.layout.crm_lv_approval, null);
            holder.mName = (TextView) convertView.findViewById(R.id.name_tv);
            holder.mTime = (TextView) convertView.findViewById(R.id.time_tv);
            holder.mContent = (TextView) convertView.findViewById(R.id.content_tv);
            holder.mState = (ImageView) convertView.findViewById(R.id.state_img);
            holder.mHead = (BSCircleImageView) convertView.findViewById(R.id.head_icon);
            holder.mItem = convertView.findViewById(R.id.item_layout);
            holder.mType = (TextView) convertView.findViewById(R.id.type_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.mHead.setIsread("");
        }
        CrmApprovalVO approvalVO = mList.get(position);
        if ("0".equals(approvalVO.getIsread())) {
            holder.mHead.setIsread("0");
        } else {
            holder.mHead.setIsread("1");
        }
        mImageLoader.displayImage(approvalVO.getHeadpic(), holder.mHead, mOptions);
        holder.mHead.setUserId(approvalVO.getUid());//HL:获取跟单详情界面头像对应的用户ID，以便响应跳转
        holder.mHead.setUrl(approvalVO.getHeadpic());
        holder.mHead.setUserName(approvalVO.getFullname());
        Drawable manDrawable = mContext.getResources().getDrawable(R.drawable.sex_man);
        Drawable womanDrawable = mContext.getResources().getDrawable(R.drawable.sex_woman);
        holder.mContent.setText(approvalVO.getTitle());
        holder.mTime.setText(DateUtils.parseDateDay(approvalVO.getPlanned_date()));
        holder.mType.setText(approvalVO.getFullname());
        // holder.mName.setText(" 回款：￥" + approvalVO.getMoney());
        CommonUtils.setDifferentTextColor(holder.mName, "回款：", "￥" + approvalVO.getMoney(), "#ff0000");

        String checks = approvalVO.getStatus();
        if ("0".equals(checks) || "1".equals(checks)) {
            holder.mState.setImageResource(R.drawable.approval_status_01);
        }
        else if ("2".equals(checks)) {
            holder.mState.setImageResource(R.drawable.approval_status_03);
        }
        else if ("3".equals(checks)) {
            holder.mState.setImageResource(R.drawable.approval_status_02);
        }
        return convertView;
    }

    static class ViewHolder {
        private TextView mName, mContent, mTime, mType;
        private ImageView mState;
        private BSCircleImageView mHead;
        private View mItem;
    }

    public void updateData() {
        this.notifyDataSetChanged();
    }

    public void updateData(List<CrmApprovalVO> list) {
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataFrist(List<CrmApprovalVO> list) {
        list.addAll(mList);
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataLast(List<CrmApprovalVO> list) {
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    private class IdeaListeners implements OnClickListener {
        private Context mContext;
        private ApprovalVO mApprovalVO;

        public IdeaListeners(Context context, ApprovalVO approvalVO) {
            this.mContext = context;
            this.mApprovalVO = approvalVO;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(mContext, CreativeIdeaDetailActivity.class);
            mContext.startActivity(intent);
        }
    }

}
