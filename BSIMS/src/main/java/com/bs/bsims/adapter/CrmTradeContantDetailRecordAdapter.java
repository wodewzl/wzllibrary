
package com.bs.bsims.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.CrmTradeHuiKuanDetailActivity;
import com.bs.bsims.model.CrmTranctVos;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.DateUtils;

import java.util.List;

public class CrmTradeContantDetailRecordAdapter extends BaseAdapter {
    private List<CrmTranctVos> mList;
    private CrmTranctVos vos;
    private Context mContext;
    private int type;

    public CrmTradeContantDetailRecordAdapter(Context context,
            List<CrmTranctVos> list1, int i) {
        this.mContext = context;
        this.mList = list1;
        this.type = i;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return mList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int arg0, View convertView, ViewGroup arg2) {
        // TODO Auto-generated method stub
        vos = mList.get(arg0);
        ViewHolder holder;
        if (type == 1) {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.crm_trade_contant_adapter_list1, null);
                holder.money = (TextView) convertView.findViewById(R.id.plan_money);
                holder.time1 = (TextView) convertView.findViewById(R.id.time1);
                holder.time2 = (TextView) convertView.findViewById(R.id.time2);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.money.setText("￥" + vos.getMoney());
            holder.time1.setText(DateUtils.parseDateDay(vos.getPlanned_date()));
            holder.time2.setText(DateUtils.parseDateDay(vos.getReminder_time()));
        } else if (type == 2) {

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.crm_trade_contant_adapter_list2, null);
                holder.moneys = (TextView) convertView.findViewById(R.id.moneys);
                holder.isReceipt = (TextView) convertView.findViewById(R.id.is_receipt);
                holder.payType = (TextView) convertView.findViewById(R.id.pay_type);
                holder.payTime = (TextView) convertView.findViewById(R.id.paytime);
                holder.image = (ImageView) convertView.findViewById(R.id.image);
                holder.isApproval = (TextView) convertView.findViewById(R.id.is_approval);
                holder.remark = (TextView) convertView.findViewById(R.id.remark);
                holder.remark_layout = (LinearLayout) convertView.findViewById(R.id.remark_layout);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.moneys.setText("￥" + vos.getMoney());
            if (vos.getReceipt().equals("1")) {
                holder.isReceipt.setText("开票");
            } else {
                holder.image.setImageResource(R.drawable.crm_tradecontanct_billing2);
                holder.isReceipt.setText("不开票");
                holder.isReceipt.setTextColor(mContext.getResources().getColor(R.color.C6));
            }

            if (vos.getStatusName().equals("未审核")) {
                holder.isApproval.setText("未审核");
                // holder.isApproval.setBackgroundDrawable((mContext.getResources().getDrawable(R.drawable.frame_shixing_green)));
                GradientDrawable drawable = CommonUtils.setBackgroundShap(mContext, 10, "#CBCCCB", "#CBCCCB");
                holder.isApproval.setBackgroundDrawable(drawable);
            } else {
                holder.isApproval.setText("已审核");
                // holder.isApproval.setBsetBackgroundDrawable((mContext.getResources().getDrawable(R.drawable.frame_shixing_green_light)));
                GradientDrawable drawable = CommonUtils.setBackgroundShap(mContext, 10, "#009E4E", "#009E4E");
                holder.isApproval.setBackgroundDrawable(drawable);
            }
            holder.payType.setText(vos.getPayment_mode());
            holder.payTime.setText("付款日期：" + DateUtils.parseDateDay(vos.getPlanned_date()));
            if (CommonUtils.isNormalString(vos.getRemark())) {
                holder.remark_layout.setVisibility(View.VISIBLE);
                holder.remark.setText(vos.getRemark());
            } else {
                holder.remark_layout.setVisibility(View.GONE);
            }

            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent i = new Intent();
                    i.putExtra("vo_s", vos);
                    i.setClass(mContext, CrmTradeHuiKuanDetailActivity.class);
                    mContext.startActivity(i);
                }
            });

        }

        return convertView;
    }

    static class ViewHolder {
        private LinearLayout remark_layout;
        private TextView money, time1, time2;
        private TextView moneys, isReceipt, payType, payTime, isApproval, remark;
        private ImageView image;
    }

}
