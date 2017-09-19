
package com.bs.bsims.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.CrmBussinesList;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class CrmBusinessHomeListAdapter extends BaseAdapter {
    private Context mContext;
    public List<CrmBussinesList> mList;

    public CrmBusinessHomeListAdapter(Context context,
            List<CrmBussinesList> list1) {
        this.mContext = context;
        this.mList = list1;
    }

    public CrmBusinessHomeListAdapter(Context context) {
        this.mContext = context;
        this.mList = new ArrayList<CrmBussinesList>();
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
    public View getView(final int postion, View convertView, ViewGroup arg2) {
        final ViewHolder holder;
        // RelativeLayout.LayoutParams layoutParams = new
        // RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
        // ViewGroup.LayoutParams.WRAP_CONTENT);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.crm_bussines_listapdater, null);
            holder.work_info_why_state = (TextView) convertView.findViewById(R.id.work_info_why_state);
            holder.work_info_suclv = (TextView) convertView.findViewById(R.id.work_info_suclv);
            holder.work_info_why = (TextView) convertView.findViewById(R.id.work_info_why);
            holder.work_info_companyname = (TextView) convertView.findViewById(R.id.work_info_companyname);
            holder.crm_bussines_listtime = (TextView) convertView.findViewById(R.id.crm_bussines_listtime);
            holder.crm_bussines_listcsn = (TextView) convertView.findViewById(R.id.crm_bussines_listcsn);
            holder.crm_bussines_listmoney = (TextView) convertView.findViewById(R.id.crm_bussines_listmoney);
            holder.isread = (ImageView) convertView.findViewById(R.id.isread);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.work_info_why_state.setText(mList.get(postion).getStatusName());
        holder.work_info_suclv.setText(Html.fromHtml("成功率: " + "<font size=\"2\" color=\"red\">" + mList.get(postion).getPercent() + "</font>"));
        holder.work_info_why.setText(mList.get(postion).getBname());
        holder.work_info_companyname.setText("客户:" + mList.get(postion).getCname());
        holder.crm_bussines_listtime.setText(DateUtils.parseDateDay(mList.get(postion).getAddtime()));
        holder.crm_bussines_listcsn.setText(mList.get(postion).getFullname());
        // holder.crm_bussines_listmoney.setText("￥" +
        // CommonUtils.countNumberSecond(mList.get(postion).getMoney()));
        holder.crm_bussines_listmoney.setText(CommonUtils.formatDetailMoney(mList.get(postion).getMoney()));

        // layoutParams.setMargins(0,20,0,10);//4个参数按顺序分别是左上右下
        // holder.work_get_money.setLayoutParams(layoutParams); "￥"
        // holder.work_info_companyname.setLayoutParams(layoutParams);
        /*
         * array("1"=>"初步接洽","2"=>"需求确定","3"=>"方案报价","4"=>"谈判审核","5"=>"赢单","6"=>"放弃","7"=>"结束关闭");
         */
        int puttype = Integer.parseInt(CommonUtils.isNormalData(mList.get(postion).getStatus()));
        switch (puttype) {
            case 1:
                holder.work_info_why_state.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.frame_shixing_blue));
                break;
            case 2:
                holder.work_info_why_state.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.frame_shixing_yellow));
                break;
            case 3:
                holder.work_info_why_state.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.corners_greenlow));
                break;
            case 4:
                holder.work_info_why_state.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.frame_shixing_green));
                break;
            case 5:
                holder.work_info_why_state.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.frame_shixing_red));
                break;
            case 6:
                holder.work_info_why_state.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.frame_shixing_gray));
                break;
        }

        if (mList.get(postion).getIsread() != null) {
            if ("1".equals(mList.get(postion).getIsread())) {
                holder.isread.setVisibility(View.GONE);
            } else {
                holder.isread.setVisibility(View.VISIBLE);
            }
        }
        return convertView;
    }

    static class ViewHolder {
        private ProgressBar item_taskeventlistadapter_seekbar;
        private TextView work_info_why_state, work_info_suclv, work_info_why, work_info_companyname, crm_bussines_listtime,
                crm_bussines_listcsn, crm_bussines_listmoney;
        private ImageView isread;
    }

    public void updateData(List<CrmBussinesList> list) {
        if (null == mList) {
            mList = new ArrayList<CrmBussinesList>();
        }
        if (null == list) {
            return;
        }
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void updateDataFrist(List<CrmBussinesList> list) {
        list.addAll(mList);
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void updateDataLast(List<CrmBussinesList> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

}
