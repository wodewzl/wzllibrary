
package com.bs.bsims.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.CrmTranctVo;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class CrmBussinesTranctAdapter extends BaseAdapter {

    private Context mContext;
    public List<CrmTranctVo> mList;
    public String mSortid;
    private int width;
    private boolean falge;

    public CrmBussinesTranctAdapter(Context context) {
        this.mContext = context;
        this.mList = new ArrayList<CrmTranctVo>();
    }

    public CrmBussinesTranctAdapter(Context context, boolean falge) {
        this.mContext = context;
        this.mList = new ArrayList<CrmTranctVo>();
        this.falge = falge;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.crmtradecontract_index_adpter_copylist, null);
            holder.item_taskeventlistadapter_seekbar = (ProgressBar) convertView.findViewById(R.id.item_taskeventlistadapter_seekbar);
            holder.work_info_why = (TextView) convertView.findViewById(R.id.work_info_why);
            holder.work_info_why_state = (TextView) convertView.findViewById(R.id.work_info_why_state);
            holder.work_info_companyname = (TextView) convertView.findViewById(R.id.work_info_companyname);
            holder.work_get_money = (TextView) convertView.findViewById(R.id.work_get_money);
            holder.work_get_money1 = (TextView) convertView.findViewById(R.id.work_get_money1);
            holder.item_taskeventlistadapter_content_tv = (TextView) convertView.findViewById(R.id.item_taskeventlistadapter_content_tv);
            holder.work_pay_money1 = (TextView) convertView.findViewById(R.id.work_pay_money1);
            holder.work_pay_money = (TextView) convertView.findViewById(R.id.work_pay_money);
            holder.client_name = (TextView) convertView.findViewById(R.id.client_name);

            holder.responsible_name = (TextView) convertView.findViewById(R.id.responsible_name);
            holder.add_time = (TextView) convertView.findViewById(R.id.add_time);
            holder.mIsread = (ImageView) convertView.findViewById(R.id.isread);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (mList.get(position).getPercent().equals("暂无")) {
            CommonUtils.setDifferentTextColor(holder.item_taskeventlistadapter_content_tv, "收款率", "0%", "#ff0000");
        }
        else {
            CommonUtils.setDifferentTextColor(holder.item_taskeventlistadapter_content_tv, "收款率：", mList.get(position).getPercent() + "%", "#ff0000");
        }

        holder.client_name.setText(mList.get(position).getCname());

        // CommonUtils.setDifferentTextColor(holder.work_pay_money, "实收款：", "￥" +
        // CommonUtils.countNumberSecond(mList.get(position).getPayment()), "#169200");
        CommonUtils.setDifferentTextColor(holder.work_pay_money, "实收款：", CommonUtils.formatDetailMoney(mList.get(position).getPayment()), "#169200");

        holder.work_info_why.setText(mList.get(position).getTitle());
        holder.work_info_companyname.setText(mList.get(position).getCname());
        // CommonUtils.setDifferentTextColor(holder.work_get_money, "应收款：", "￥" +
        // CommonUtils.countNumberSecond(mList.get(position).getMoney()), "#ff0000");

        CommonUtils.setDifferentTextColor(holder.work_get_money, "应收款：", CommonUtils.formatDetailMoney(mList.get(position).getMoney()), "#ff0000");

        /*
         * array("1"=>"初步接洽","2"=>"需求确定","3"=>"方案报价","4"=>"谈判审核","5"=>"赢单","6"=>"放弃","7"=>"结束关闭") ;
         */
        int puttype = Integer.parseInt(mList.get(position).getStatus());
        holder.work_info_why_state.setText(mList.get(position).getStatusName() + " ");
        switch (puttype) {
            case 1:
                holder.work_info_why_state.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.frame_shixing_blue));
                break;
            case 2:
                holder.work_info_why_state.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.frame_shixing_yellow));
                break;
            case 3:
                holder.work_info_why_state.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.frame_shixing_red));
                break;
            case 4:
                holder.work_info_why_state.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.frame_shixing_gray));
                break;
            case 5:
                holder.work_info_why_state.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.frame_shixing_red));
                break;
            default:
                holder.work_info_why_state.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.frame_shixing_gray));
                break;
        }

        holder.responsible_name.setText(mList.get(position).getFullname());
        holder.add_time.setText(DateUtils.parseDateDay(mList.get(position).getEndtime()));
        if (mList.get(position).getIsread() != null) {
            if ("1".equals(mList.get(position).getIsread())) {
                holder.mIsread.setVisibility(View.GONE);
            } else {
                holder.mIsread.setVisibility(View.VISIBLE);
            }
        }

        return convertView;
    }

    static class ViewHolder {
        private ProgressBar item_taskeventlistadapter_seekbar;
        private TextView work_info_why, work_info_why_state,
                work_info_companyname, work_get_money1, work_get_money,
                item_taskeventlistadapter_content_tv, work_pay_money1,
                work_pay_money, client_name, responsible_name, add_time;
        private LinearLayout client_layout;
        private ImageView mIsread;

    }

    public void updateData(List<CrmTranctVo> list) {
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataFrist(List<CrmTranctVo> list) {
        list.addAll(mList);
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataLast(List<CrmTranctVo> list) {
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

}
