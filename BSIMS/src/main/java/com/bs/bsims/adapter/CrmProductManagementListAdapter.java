
package com.bs.bsims.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.CrmProductManagementVO;
import com.bs.bsims.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class CrmProductManagementListAdapter extends BaseAdapter {
    private Context mContext;
    public static List<CrmProductManagementVO> mList;

    public CrmProductManagementListAdapter(Context context) {
        this.mContext = context;
        this.mList = new ArrayList<CrmProductManagementVO>();
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
    public View getView(final int arg0, View arg1, ViewGroup arg2) {
        // TODO Auto-generated method stub
        final ViewHolder holder;
        if (arg1 == null) {
            holder = new ViewHolder();
            arg1 = View.inflate(mContext, R.layout.crm_product_list_adapter, null);
            holder.crmProductName = (TextView) arg1.findViewById(R.id.crm_product_name);
            holder.crmProductMoney = (TextView) arg1.findViewById(R.id.crm_product_money);
            holder.crmProductAllmoney = (TextView) arg1.findViewById(R.id.crm_product_allmoney);
            arg1.setTag(holder);
        } else {
            holder = (ViewHolder) arg1.getTag();
        }
        CrmProductManagementVO vo = mList.get(arg0);
        holder.crmProductName.setText(vo.getName());
        holder.crmProductName.setTextSize(16);
        // holder.crmProductMoney.setText(vo.getMoney());
        // holder.crmProductMoney.setText(Html.fromHtml("<font size=\"14\" color=\"gray\">单价:  </font><font size=\"4\" color=\"#00AAFE\">"
        // + "￥" + vo.getMoney()
        // + "</font><font size=\"14\" color=\"gray\">" + "/" + vo.getUnit() + "</font>"));
        holder.crmProductMoney.setText(CommonUtils.formatDetailMoney(vo.getMoney()) + "/" + vo.getUnit());
        holder.crmProductAllmoney.setText(vo.getRemark());
        return arg1;
    }

    static class ViewHolder {
        private TextView crmProductName, crmProductMoney, crmProductAllmoney, crmProductNum;
    }

    public void updateData(List<CrmProductManagementVO> list) {
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataFrist(List<CrmProductManagementVO> list) {
        list.addAll(mList);
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataLast(List<CrmProductManagementVO> list) {
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

}
