
package com.bs.bsims.adapter;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.CreativeIdeaDetailActivity;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.ApprovalVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.view.BSCircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class ApprovalAdapter extends BaseAdapter {

    private Context mContext;
    public List<ApprovalVO> mList;
    public ImageLoader mImageLoader;
    public DisplayImageOptions mOptions;

    public static String STARTAPRROVAL = "#E9E9E9";// 未审核
    public static String NOAPRROVAL = "#F1333D";// 驳回
    public static String GOAPRROVAL = "#009B3F";// 已审核
    public static String INAPRROVAL = "#FFA200";// 审核中
    public static final String STARTAPRROVALSTR = "未审核";
    public static final String NOAPRROVALSTR = "驳回";
    public static final String GOAPRROVALSTR = "已审核";
    public static final String INAPRROVALSTR = "审核中";

    public ApprovalAdapter(Context context) {
        this.mContext = context;
        this.mList = new ArrayList<ApprovalVO>();
        mImageLoader = ImageLoader.getInstance();
        mOptions = CommonUtils.initImageLoaderOptions();
        registBroadcast();
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

    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        GradientDrawable bgdraw = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.lv_approval, null);
            holder.mName = (TextView) convertView.findViewById(R.id.name_tv);
            holder.mTime = (TextView) convertView.findViewById(R.id.time_tv);
            holder.mContent = (TextView) convertView.findViewById(R.id.content_tv);
            holder.mHead = (BSCircleImageView) convertView.findViewById(R.id.head_icon);
            holder.mItem = convertView.findViewById(R.id.item_layout);
            holder.mType = (TextView) convertView.findViewById(R.id.type_tv);
            holder.mStateTv =(TextView) convertView.findViewById(R.id.state_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.mHead.setIsread("");
        }
        ApprovalVO approvalVO = mList.get(position);
        holder.mHead.setUserId(approvalVO.getUserid());// 获取头像对应的用户ID.
        holder.mHead.setUserName(approvalVO.getFullname());
        holder.mHead.setmImageLoader(mImageLoader);
        holder.mHead.setUrl(approvalVO.getHeadpic());
        if ("0".equals(approvalVO.getIsread())) {
            holder.mHead.setIsread("0");
        }
        mImageLoader.displayImage(approvalVO.getHeadpic(), holder.mHead, mOptions);
        holder.mName.setText(approvalVO.getFullname());
        Drawable manDrawable = mContext.getResources().getDrawable(R.drawable.sex_man);
        Drawable womanDrawable = mContext.getResources().getDrawable(R.drawable.sex_woman);

        holder.mTime.setText(approvalVO.getTime());
        holder.mContent.setText(approvalVO.getTitle());
        holder.mType.setText("[" + approvalVO.getTypename() + "]");
        String checks = approvalVO.getStatus();
        if ("0".equals(checks)) {// 未审核
            bgdraw = CommonUtils.setBackgroundShap(mContext, 10, STARTAPRROVAL, STARTAPRROVAL);
            holder.mStateTv.setText(STARTAPRROVALSTR);
            holder.mStateTv.setTextColor(mContext.getResources().getColor(R.color.C6));
        } else if ("1".equals(checks)) {// 审核中
            bgdraw = CommonUtils.setBackgroundShap(mContext, 10, INAPRROVAL, INAPRROVAL);
            holder.mStateTv.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.mStateTv.setText(INAPRROVALSTR);
        }
        else if ("2".equals(checks)) {// 驳回
            bgdraw = CommonUtils.setBackgroundShap(mContext, 10, NOAPRROVAL, NOAPRROVAL);
            holder.mStateTv.setText(NOAPRROVALSTR);
            holder.mStateTv.setTextColor(mContext.getResources().getColor(R.color.white));
            
        }
        else if ("3".equals(checks)) {// 已审核
            bgdraw = CommonUtils.setBackgroundShap(mContext, 10, GOAPRROVAL, GOAPRROVAL);
            holder.mStateTv.setText(GOAPRROVALSTR);
            holder.mStateTv.setTextColor(mContext.getResources().getColor(R.color.white));
        }
        holder.mStateTv.setBackground(bgdraw);
        return convertView;
    }

    static class ViewHolder {
        private TextView mName, mContent, mTime, mType,mStateTv;
        private BSCircleImageView mHead;
        private View mItem;
    }

    public void updateData(List<ApprovalVO> list) {
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataFrist(List<ApprovalVO> list) {
        list.addAll(mList);
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataLast(List<ApprovalVO> list) {
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
            // intent.putExtra("type", mApprovalVO.getType());
            // intent.putExtra("id", mApprovalVO.getArticleid());
            mContext.startActivity(intent);
        }
    }

    public void registBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.HOME_MSG);
        mContext.registerReceiver(msgBroadcast, filter);
    }

    private void unRegistExitReceiver() {
        mContext.unregisterReceiver(msgBroadcast);
    }

    private BroadcastReceiver msgBroadcast = new BroadcastReceiver() {
        private long mChangeTime = 0;

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constant.HOME_MSG.equals(intent.getAction())) {
                String isread = intent.getStringExtra("isread");
                String approvalid = intent.getStringExtra("approvalid");
                String status = intent.getStringExtra("status");
                String count = intent.getStringExtra("count");
                if (isread != null) {
                    for (int i = 0; i < mList.size(); i++) {
                        if (approvalid.equals(mList.get(i).getId())) {
                            mList.get(i).setIsread(isread);
                            notifyDataSetChanged();
                        }
                    }
                }

                if (status != null) {
                    for (int i = 0; i < mList.size(); i++) {
                        if (approvalid.equals(mList.get(i).getId())) {
                            if ("1".equals(status)) {
                                if (Integer.parseInt(count) == 1) {
                                    mList.get(i).setStatus("3");
                                } else {
                                    mList.get(i).setStatus("1");
                                }
                            } else {
                                mList.get(i).setStatus("2");
                            }
                            notifyDataSetChanged();
                        }
                    }
                }

            }

        }
    };

}
