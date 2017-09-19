/**
 * 
 */

package com.bs.bsims.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.JournalFootPrintModel;
import com.bs.bsims.utils.DateUtils;

/**
 * BS北盛最帅程序员 Copyright (c) 2016 湖北北盛科技有限公司
 * 
 * @author 梁骚侠
 * @date 2016-3-1
 * @version 1.22
 */
public class JournalFootPrintAdapter extends BSBaseAdapter<JournalFootPrintModel> {
    private Context mContext;

    /**
     * @param context
     */
    public JournalFootPrintAdapter(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        this.mContext = context;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        JournalFootPrintModel footPrintModel = mList.get(position);
        if (mIsEmpty) {
            return super.getView(position, convertView, parent);
        }

        if (convertView != null && convertView.getTag() == null)
            convertView = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.journal_footprint_adapter, null);
            holder.footer_type = (ImageView) convertView.findViewById(R.id.footer_type);
            holder.day_time = (TextView) convertView.findViewById(R.id.day_time);
            holder.footer_title = (TextView) convertView.findViewById(R.id.footer_title);
            holder.footer_content = (TextView) convertView.findViewById(R.id.footer_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // holder.footer_type.setBackgroundDrawable(CommonUtils.setBackgroundShap(mContext, 40,
        // "#00000000", "#33BAFE"));
        holder.day_time.setText(DateUtils.parseHour(footPrintModel.getLog_createtime()));
        holder.footer_title.setText(footPrintModel.getLog_title());
        holder.footer_content.setText(footPrintModel.getLog_content());

        /*
         * '1' => '发布意见', '2' => '发布通知', '3' => '发布公文', '4' => '发布制度', '5' => '发布企业风采', '6' =>
         * '添加客户', '7' => '添加合同', '8' => '添加商机', '9' => '上报位置', '10' => '添加日程', '11' => '发布任务', '12'
         * => '发布审批', '13' => '添加跟单记录', '14' => '添加联系人', '15' => '移动打卡', '16' => '新增部门', '17' =>
         * '添加产品', '18' => '上传文档', '19' => '发布奖惩', '20' => '发布培训',
         */

        switch (Integer.parseInt(footPrintModel.getLog_class())) {
            case 1:
                holder.footer_type.setBackgroundResource(R.drawable.idea_msg);
                break;
            case 2:
                holder.footer_type.setBackgroundResource(R.drawable.idea_notice);
                break;
            case 3:
                holder.footer_type.setBackgroundResource(R.drawable.idea_archives);
                break;
            case 4:
                holder.footer_type.setBackgroundResource(R.drawable.idea_institution);
                break;
            case 5:
                holder.footer_type.setBackgroundResource(R.drawable.idea_corporatestyle);
                break;
            case 6:
                holder.footer_type.setBackgroundResource(R.drawable.statistics_customer);
                break;
            case 7:
                holder.footer_type.setBackgroundResource(R.drawable.statistics_trade);
                break;
            case 8:
                holder.footer_type.setBackgroundResource(R.drawable.statistics_bussines);
                break;
            case 9:
                holder.footer_type.setBackgroundResource(R.drawable.statistics_gaode);
                break;
            case 10:
                holder.footer_type.setBackgroundResource(R.drawable.idea_schedule);
                break;
            case 11:
                holder.footer_type.setBackgroundResource(R.drawable.statistics_task);
                break;
            case 12:
                holder.footer_type.setBackgroundResource(R.drawable.idea_approal);
                break;
            case 13:
                holder.footer_type.setBackgroundResource(R.drawable.statistics_visitor);
                break;
            case 14:
                holder.footer_type.setBackgroundResource(R.drawable.idea_customer);
                break;
            case 15:
                holder.footer_type.setBackgroundResource(R.drawable.idea_sign);
                break;
            case 16:
                holder.footer_type.setBackgroundResource(R.drawable.idea_depaterment);
                break;
            case 17:
                holder.footer_type.setBackgroundResource(R.drawable.idea_product);
                break;
            case 18:
                holder.footer_type.setBackgroundResource(R.drawable.idea_filelpost);
                break;
            case 19:
                holder.footer_type.setBackgroundResource(R.drawable.idea_bonus_penalty);
                break;
            case 20:
                holder.footer_type.setBackgroundResource(R.drawable.idea_train);
                break;

        }

        return convertView;
    }

    static class ViewHolder {
        private TextView day_time, footer_title, footer_content;
        private ImageView footer_type;

    }

}
