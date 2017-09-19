
package com.bs.bsims.appzone;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
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
import com.bs.bsims.activity.DanganIndextwoActivity;
import com.bs.bsims.activity.EXTTaskEventDetailsActivity;
import com.bs.bsims.activity.ScheduleDetailActivity;
import com.bs.bsims.adapter.BSBaseAdapter;
import com.bs.bsims.application.BSApplication;

public class ALLZoneApdater extends BSBaseAdapter<ALLZoneModel> {
    private Context mContext;

    public ALLZoneApdater(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (mIsEmpty) {
            return super.getView(position, convertView, parent);
        }
        final ALLZoneModel vo = mList.get(position);
        if (convertView != null && convertView.getTag() == null)
            convertView = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.app_zone_tableselect_apdater, null);
            holder.mTimeTv = (TextView) convertView.findViewById(R.id.zone_type);// 审批类型
            holder.mText02Tv = (TextView) convertView.findViewById(R.id.zone_timed);// 时间
            holder.mTypeIv = (ImageView) convertView.findViewById(R.id.zone_type_img);// 类型图片
            holder.mText01Tv = (TextView) convertView.findViewById(R.id.zone_content);// 内容
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        /*
         * '1' => '15天转正提醒（延时）', '2' => '员工转正提醒', '3' => '员工调岗提醒', '4' => '员工调薪提醒', '5' =>
         * '3天任务到期提醒', '6' => '任务进度提醒', '7' => '意见采纳提醒（原创意/建议）', '8' => '员工请假审批', '9' => '员工物资审批',
         * '10' => '员工加班审批', '11' => '员工费用审批', '12' => '员工考勤申诉', '13' => '员工其他审批', '14' => '任务到期提醒',
         * '15' => '培训消息', '16' => '员工日程提醒', '23' => 'CRM商机提醒', '24' => 'CRM合同提醒', '25' =>
         * 'CRM客户提醒', '26' => 'CRM回款审批', '27' => 'CRM回款计划提醒', '28' => 'CRM跟单提醒', '29' => 'CRM联系人提醒',
         */
        holder.mTimeTv.setText(vo.getAttr());
        holder.mText02Tv.setText(vo.getM_seetime());
        // holder.mText01Tv.setText(Html.fromHtml("<font size=\"14\" color=\"gray\">"+vo.getM_title()+"\n"+"11111111"+"</font>"));
        holder.mText01Tv.setText(vo.getM_title());
        final Intent intent = new Intent();
        int ValueKey = Integer.parseInt(vo.getM_type());
        switch (ValueKey) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 15:
                intent.setClass(mContext, DanganIndextwoActivity.class);
                holder.mTypeIv.setImageResource(R.drawable.ic_main_message_general_11);// 人事通知图标
                break;
            case 5:
            case 6:
            case 14:
                intent.putExtra("id", vo.getM_typeid());
                intent.setClass(mContext, EXTTaskEventDetailsActivity.class);
                holder.mTypeIv.setImageResource(R.drawable.ic_main_message_general_04);
                break;
            case 7:
                holder.mTypeIv.setImageResource(R.drawable.ic_main_message_general_08);
                intent.putExtra("type", "1");
                intent.putExtra("isboss", BSApplication.getInstance().getUserFromServerVO().getIsboss());
                intent.putExtra("id", vo.getM_typeid());
                intent.setClass(mContext, CreativeIdeaDetailActivity.class);
                break;
            case 8:
                holder.mTypeIv.setImageResource(R.drawable.ic_main_message_general_03_nowork);
                intent.putExtra("type", "1");
                intent.putExtra("uid", BSApplication.getInstance().getUserId());
                intent.putExtra("alid", vo.getM_typeid());
                intent.setClass(mContext, ApprovalLeaveDetailActivity.class);
                break;
            case 9:
                holder.mTypeIv.setImageResource(R.drawable.ic_main_message_general_03_product);
                intent.putExtra("type", "2");
                intent.putExtra("uid", BSApplication.getInstance().getUserId());
                intent.putExtra("alid", vo.getM_typeid());
                intent.setClass(mContext, ApprovalSuppliesDetailActivity.class);
                break;
            case 10:
                holder.mTypeIv.setImageResource(R.drawable.ic_main_message_general_03_work);
                intent.putExtra("type", "3");
                intent.putExtra("uid", BSApplication.getInstance().getUserId());
                intent.putExtra("alid", vo.getM_typeid());
                intent.setClass(mContext, ApprovalOvertimeDetailActivity.class);
                break;
            case 11:
                holder.mTypeIv.setImageResource(R.drawable.ic_main_message_general_03_money);
                intent.putExtra("type", "4");
                intent.putExtra("uid", BSApplication.getInstance().getUserId());
                intent.putExtra("alid", vo.getM_typeid());
                intent.setClass(mContext, ApprovalFeeApplyDetailActivity.class);
                break;
            case 12:
                holder.mTypeIv.setImageResource(R.drawable.ic_main_message_general_03_allege);
                intent.putExtra("type", "5");
                intent.putExtra("uid", BSApplication.getInstance().getUserId());
                intent.putExtra("alid", vo.getM_typeid());
                intent.setClass(mContext, ApprovalAttendanceDetailActivity.class);
                break;
            case 13:
                intent.putExtra("type", "6");
                intent.putExtra("uid", BSApplication.getInstance().getUserId());
                intent.putExtra("alid", vo.getM_typeid());
                intent.setClass(mContext, ApprovalCustomDetailActivity.class);
                holder.mTypeIv.setImageResource(R.drawable.ic_main_message_general_03_other);

                break;
            case 16:
                holder.mTypeIv.setImageResource(R.drawable.ic_main_message_general_09);
                intent.putExtra("id", vo.getM_typeid());
                intent.setClass(mContext, ScheduleDetailActivity.class);
                break;
            case 23:
                holder.mTypeIv.setImageResource(R.drawable.home_crm_bussness);
                intent.putExtra("bid", vo.getM_typeid());
                intent.putExtra("stateUtilthread", "3");
                intent.setClass(mContext, CrmBusinessHomeIndexOneInfo.class);
                break;
            case 24:
                holder.mTypeIv.setImageResource(R.drawable.home_crm_traded);
                intent.putExtra("hid", vo.getM_typeid());
                if ("2".equals(vo.getDirection())) {
                    intent.setClass(mContext, CrmTradeContantDetailsIndexActivity.class);
                } else {
                    intent.setClass(mContext, CrmTradeContantDeatilsHomeTop3Activity.class);
                }
                break;
            case 25:
                holder.mTypeIv.setImageResource(R.drawable.home_crm_client);
                intent.putExtra("cid", vo.getM_typeid());
                intent.setClass(mContext, CrmHighseasClientsHomeActivity.class);

                String isPub = vo.getIsPub();
                intent.putExtra("jpush", true);
                if ("1".equals(isPub)) {
                    intent.setClass(mContext, CrmHighseasClientsHomeActivity.class);
                } else {
                    intent.setClass(mContext, CrmClientHomeActivity.class);
                }
                break;
            case 26:
                holder.mTypeIv.setImageResource(R.drawable.home_crm_repayment);
                intent.putExtra("mid", vo.getM_typeid());
                intent.setClass(mContext, CrmApprovalDetailActivity.class);
                break;
            case 27:
                holder.mTypeIv.setImageResource(R.drawable.home_crm_repayment);
                intent.putExtra("hid", vo.getM_typeid());
                if ("2".equals(vo.getDirection())) {
                    intent.setClass(mContext, CrmTradeContantDetailsIndexActivity.class);
                } else {
                    intent.setClass(mContext, CrmTradeContantDeatilsHomeTop3Activity.class);
                }
                break;
            case 28:
                holder.mTypeIv.setImageResource(R.drawable.home_crm_visitor);
                intent.putExtra("vid", vo.getM_typeid());
                intent.setClass(mContext, CrmVisitRecordDetailActivity.class);
                break;
            case 29:
                holder.mTypeIv.setImageResource(R.drawable.home_crm_contacts);
                intent.putExtra("lid", vo.getM_typeid());
                intent.setClass(mContext, CrmContactDetailActivity.class);
                break;

            default:
                break;
        }

        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (Integer.parseInt(vo.getM_type()) == 24 && "2".equals(vo.getDirection())) {
                    ((FragmentActivity) mContext).startActivityForResult(intent, 1);
                } else if (intent.getComponent() != null) {
                    mContext.startActivity(intent);
                }
            }
        });

        return convertView;
    }

    static class ViewHolder {
        private TextView mTextView, mText01Tv, mText02Tv, mText03Tv, mTimeTv;
        private ImageView mTypeIv;
    }

}
