
package com.bs.bsims.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.CrmContactVo;

import java.util.ArrayList;
import java.util.List;

public class CrmOneTreeCheckAdapter extends BaseAdapter {

    /*
     * 商机阶段的列表
     */
    private Context mContext;
    public List<CrmContactVo> mList;

    private String productId[];// 产品id
    private String proname;// 产品名称
    private String money;// 产品金额

    public String[] getProductId() {
        return productId;
    }

    public String getProname() {
        return proname;
    }

    public void setProname(String proname) {
        this.proname = proname;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    private String statekey;// 判断是什么要用这个适配器

    public String getStatekey() {
        return statekey;
    }

    public void setStatekey(String statekey) {
        this.statekey = statekey;
    }

    public CrmOneTreeCheckAdapter(Context context,
            List<CrmContactVo> list1) {
        this.mContext = context;
        this.mList = list1;
        productId = new String[list1.size()];

    }

    public CrmOneTreeCheckAdapter(Context context) {
        this.mContext = context;
        this.mList = new ArrayList<CrmContactVo>();

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

        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.crm_changeinfo_list, null);
            holder.contants_name = (TextView) convertView.findViewById(R.id.text_name);
            holder.checkBox_up_check_selectall = (ImageView) convertView
                    .findViewById(R.id.checkBox_up_check_selectall);
            holder.text_phone = (TextView) convertView.findViewById(R.id.text_phone);
            holder.allliney = (LinearLayout) convertView.findViewById(R.id.allliney);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.contants_name.setText(mList.get(position).getName());

        if (mList.get(position).getFalgecontant().equals("1")) {
            holder.checkBox_up_check_selectall.setImageResource(R.drawable.common_ic_selected);
        }
        else {
            holder.checkBox_up_check_selectall.setImageResource(R.drawable.common_ic_unselect);
        }

        holder.allliney.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                // for (int i = 0; i < mList.size(); i++) {
                // mList.get(i).setFalgecontant("0");
                // }
                //
                // mList.get(position).setFalgecontant("1");
                if (mList.get(position).getFalgecontant().equals("1")) {
                    mList.get(position).setFalgecontant("0");
                    holder.checkBox_up_check_selectall
                            .setImageResource(R.drawable.common_ic_unselect);
                }
                else {
                    mList.get(position).setFalgecontant("1");
                    holder.checkBox_up_check_selectall
                            .setImageResource(R.drawable.common_ic_selected);
                }

                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    static class ViewHolder {
        private TextView contants_name, text_phone;
        private ImageView checkBox_up_check_selectall;
        private LinearLayout allliney;
    }

}
