
package com.bs.bsims.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.ApprovalAttendanceDetailActivity;
import com.bs.bsims.activity.ApprovalCustomDetailActivity;
import com.bs.bsims.activity.ApprovalFeeApplyDetailActivity;
import com.bs.bsims.activity.ApprovalLeaveDetailActivity;
import com.bs.bsims.activity.ApprovalOvertimeDetailActivity;
import com.bs.bsims.activity.ApprovalSuppliesDetailActivity;
import com.bs.bsims.activity.CreativeIdeaDetailActivity;
import com.bs.bsims.activity.CrmApprovalDetailActivity;
import com.bs.bsims.activity.CrmBusinessHomeIndexOneInfo;
import com.bs.bsims.activity.CrmClientHomeActivity;
import com.bs.bsims.activity.CrmContactDetailActivity;
import com.bs.bsims.activity.CrmHighseasClientsHomeActivity;
import com.bs.bsims.activity.CrmTradeContantDeatilsHomeTop3Activity;
import com.bs.bsims.activity.CrmTradeContantDetailsIndexActivity;
import com.bs.bsims.activity.CrmVisitRecordDetailActivity;
import com.bs.bsims.activity.EXTTaskEventDetailsActivity;
import com.bs.bsims.activity.NoticeDetailActivity;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.constant.ExtrasBSVO;
import com.bs.bsims.model.MessageListVO;
import com.bs.bsims.utils.CommonUtils;

public class MessageListAdapter extends BSBaseAdapter<MessageListVO> {
    private Context mContext;
    private String mType;
    private TextView textView;

    public MessageListAdapter(Context context, String type) {
        super(context);
        mContext = context;
        this.mType = type;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (mIsEmpty) {
            return super.getView(position, convertView, parent);
        }
        final MessageListVO vo = mList.get(position);
        if (convertView != null && convertView.getTag() == null)
            convertView = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.message_list_adapter, null);
            holder.nameTv = (TextView) convertView.findViewById(R.id.name_tv);
            holder.timeTv = (TextView) convertView.findViewById(R.id.time_tv);
            holder.typeTitleTv = (TextView) convertView.findViewById(R.id.type_title_tv);
            holder.contentTv = (TextView) convertView.findViewById(R.id.content_tv);
            holder.itemImg = (ImageView) convertView.findViewById(R.id.type_img);
            holder.isreadTv = (TextView) convertView.findViewById(R.id.isread_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.nameTv.setText(vo.getSubtitle());
        holder.timeTv.setText(vo.getDate());
        holder.typeTitleTv.setText(vo.getTitle());
        holder.contentTv.setText(Html.fromHtml(vo.getContent()));
        holder.isreadTv.setBackground(CommonUtils.setBackgroundShap(mContext, 100, R.color.C9, R.color.C9));
        if ("0".equals(vo.getIsread())) {
            holder.isreadTv.setVisibility(View.VISIBLE);
        } else {
            holder.isreadTv.setVisibility(View.GONE);
        }
        final Intent intent = new Intent();
        Class<com.bs.bsims.R.drawable> cls = R.drawable.class;
        int imageId;
        switch (Integer.parseInt(mType)) {
            case 1:
                try {
                    imageId = cls.getDeclaredField("office003").getInt(null);
                    holder.itemImg.setImageResource(imageId);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                intent.putExtra("sortid", "3");
                intent.putExtra("articleid", vo.getDetailid());
                intent.setClass(mContext, NoticeDetailActivity.class);
                break;
            case 2:
                // 公文
                try {
                    imageId = cls.getDeclaredField("office004").getInt(null);
                    holder.itemImg.setImageResource(imageId);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                intent.putExtra("sortid", "11");
                intent.putExtra("articleid", vo.getDetailid());
                intent.setClass(mContext, NoticeDetailActivity.class);
                break;
            case 3:
                // 制度
                try {
                    imageId = cls.getDeclaredField("office005").getInt(null);
                    holder.itemImg.setImageResource(imageId);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                intent.putExtra("sortid", "12");
                intent.putExtra("articleid", vo.getDetailid());
                intent.setClass(mContext, NoticeDetailActivity.class);
                break;
            case 7:
                // 审批事务
                holder.itemImg.setImageResource(R.drawable.affair001);
                intent.putExtra("alid", mList.get(position).getDetailid());
                intent.putExtra("uid", BSApplication.getInstance().getUserId());
                String type = mList.get(position).getType();
                intent.putExtra("type", type);
                if ("1".equals(type)) {
                    intent.setClass(mContext, ApprovalLeaveDetailActivity.class);
                } else if ("2".equals(type)) {
                    intent.setClass(mContext, ApprovalSuppliesDetailActivity.class);
                } else if ("3".equals(type)) {
                    intent.setClass(mContext, ApprovalOvertimeDetailActivity.class);
                } else if ("4".equals(type)) {
                    intent.setClass(mContext, ApprovalFeeApplyDetailActivity.class);
                } else if ("5".equals(type)) {
                    intent.setClass(mContext, ApprovalAttendanceDetailActivity.class);
                } else {
                    intent.setClass(mContext, ApprovalCustomDetailActivity.class);
                }
                break;
            case 8:
                // 任务跟进
                holder.itemImg.setImageResource(R.drawable.affair002);

                intent.setClass(mContext, EXTTaskEventDetailsActivity.class);
                intent.putExtra(ExtrasBSVO.Push.BREAK_ID, mList.get(position).getDetailid());
                break;
            case 9:
                // 企业风采
                try {
                    imageId = cls.getDeclaredField("office006").getInt(null);
                    holder.itemImg.setImageResource(imageId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                intent.putExtra("sortid", "19");
                intent.putExtra("articleid", vo.getDetailid());
                intent.setClass(mContext, NoticeDetailActivity.class);
                // mContext.startActivity(intent);
                break;
            case 10:
                // 意见分享
                holder.itemImg.setImageResource(R.drawable.affair003);
                intent.setClass(mContext, CreativeIdeaDetailActivity.class);
                intent.putExtra("id", vo.getDetailid());
                intent.putExtra("isboss", BSApplication.getInstance().getUserFromServerVO().getIsboss());
                // mContext.startActivity(intent);
                break;

            case 13:
                // 销售机会
                holder.itemImg.setImageResource(R.drawable.marketing005);
                intent.putExtra("bid", vo.getDetailid());
                intent.putExtra("stateUtilthread", "3");
                intent.setClass(mContext, CrmBusinessHomeIndexOneInfo.class);
                break;
            case 14:
                // 合同
                holder.itemImg.setImageResource(R.drawable.home_crm_traded);
                intent.putExtra("hid", vo.getDetailid());
                if ("2".equals(vo.getDirection())) {
                    intent.setClass(mContext, CrmTradeContantDetailsIndexActivity.class);
                } else {
                    intent.setClass(mContext, CrmTradeContantDeatilsHomeTop3Activity.class);
                }
                break;
            case 15:
                // 客户
                holder.itemImg.setImageResource(R.drawable.marketing004);
                intent.putExtra("cid", vo.getDetailid());

                String isPub = vo.getIsPub();
                if ("1".equals(isPub)) {
                    intent.putExtra("jpush", true);
                    if ("1".equals(CommonUtils.getLimitsSpecial(Constant.LIMITS_SPECIAL004))) {
                        intent.setClass(mContext, CrmClientHomeActivity.class);
                        // 公海特殊权限
                        intent.putExtra("publish_sp", "1");
                    } else {
                        intent.setClass(mContext, CrmHighseasClientsHomeActivity.class);
                    }

                } else {
                    intent.setClass(mContext, CrmClientHomeActivity.class);
                }

                break;
            case 16:
                // 联系人列表
                holder.itemImg.setImageResource(R.drawable.home_crm_contacts);
                intent.putExtra("lid", vo.getDetailid());
                intent.setClass(mContext, CrmContactDetailActivity.class);
                break;
            case 17:
                // 回款审批
                holder.itemImg.setImageResource(R.drawable.home_crm_repayment);
                intent.putExtra("mid", vo.getDetailid());
                intent.setClass(mContext, CrmApprovalDetailActivity.class);
                break;

            case 18:
                // 跟单记录
                holder.itemImg.setImageResource(R.drawable.home_crm_visitor);
                intent.putExtra("vid", vo.getDetailid());
                intent.setClass(mContext, CrmVisitRecordDetailActivity.class);
                break;

            default:
                break;
        }

        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (intent.getComponent() != null) {
                    mContext.startActivity(intent);

                    if (getTextView() != null && "0".equals(vo.getIsread())) {
                        vo.setIsread("1");
                        getTextView().setText((Integer.parseInt(getTextView().getText().toString()) - 1) + "");
                    }
                    notifyDataSetChanged();

                }

            }
        });

        return convertView;
    }

    static class ViewHolder {
        private TextView nameTv, timeTv, typeTitleTv, contentTv, isreadTv;
        private ImageView itemImg;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

}
