
package com.bs.bsims.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.CrmProductVo;

import java.util.ArrayList;
import java.util.List;

public class CrmBussinesProductAdapter extends BaseAdapter {
    private Context mContext;
    public List<CrmProductVo> mList;

    public CrmBussinesProductAdapter(Context context,
            List<CrmProductVo> list1) {
        this.mContext = context;
        this.mList = list1;

    }

    public CrmBussinesProductAdapter(Context context) {
        this.mContext = context;
        this.mList = new ArrayList<CrmProductVo>();

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
    public View getView(final int position, View convertView, ViewGroup arg2) {
        // TODO Auto-generated method stub

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.crmproduct_management_listadapter, null);
            holder.crm_product_name = (TextView) convertView.findViewById(R.id.crm_product_name);
            holder.crm_product_money = (TextView) convertView.findViewById(R.id.crm_product_money);
            holder.crm_product_num = (TextView) convertView.findViewById(R.id.crm_product_num);
            holder.crm_product_allmoney = (TextView) convertView.findViewById(R.id.crm_product_allmoney);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.crm_product_name.setText(Html.fromHtml("<font size=\"2\" color=\"gray\">产品: </font><font size=\"4\" color=\"#000000\">" + mList.get(position).getName() + "</font>"));
        holder.crm_product_money.setText("单价: ￥" + mList.get(position).getMoney());
        holder.crm_product_num.setText("数量: " + mList.get(position).getCount());
        holder.crm_product_allmoney.setText(Html.fromHtml("<font size=\"2\" color=\"gray\">总价:  </font><font size=\"4\" color=\"#00AAFE\"><big>" + "￥" + mList.get(position).getTotalPrice() + "</big></font>"));

        return convertView;
    }

    static class ViewHolder {
        private TextView crm_product_name, crm_product_money, crm_product_num, crm_product_allmoney;
    }

    public void updateData(List<CrmProductVo> list) {
        if (null == mList) {
            mList = new ArrayList<CrmProductVo>();
        }
        if (null == list) {
            return;
        }
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void updateDataFrist(List<CrmProductVo> list) {
        list.addAll(mList);
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void updateDataLast(List<CrmProductVo> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }
}
