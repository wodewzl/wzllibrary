
package com.bs.bsims.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bs.bsims.R;

import java.util.List;

public class CrmTradeContantAddPaymentAdapter extends BSBaseAdapter<Object> {
    private Context mContext;
    public String[] plannedDate;
    public String[] money;
    public String[] reminderTime;
    public String[] remark;

    public CrmTradeContantAddPaymentAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (mIsEmpty) {
            return super.getView(position, convertView, parent);
        }

        if (convertView != null && convertView.getTag() == null)
            convertView = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.crm_tradecontant_add_payment_item, null);
            holder.mPlannedDateTv = (TextView) convertView.findViewById(R.id.panned_date);
            holder.mMoneyEt = (EditText) convertView.findViewById(R.id.money);
            holder.mReminderTimeTv = (TextView) convertView.findViewById(R.id.reminder_time);
            holder.mRemarkEt = (EditText) convertView.findViewById(R.id.remark);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    static class ViewHolder {
        private TextView mPlannedDateTv, mReminderTimeTv;
        private EditText mMoneyEt, mRemarkEt;
    }

    @Override
    public void updateData(List<Object> list) {
        super.updateData(list);
        plannedDate = new String[list.size()];
        money = new String[list.size()];
        reminderTime = new String[list.size()];
        remark = new String[list.size()];
    }

}
