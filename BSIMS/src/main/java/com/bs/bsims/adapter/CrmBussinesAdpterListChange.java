
package com.bs.bsims.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.CrmContactVo;
import com.bs.bsims.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class CrmBussinesAdpterListChange extends BaseAdapter {

    /*
     * 商机阶段的列表
     */
    private Context mContext;
    private Activity mActivity;
    public List<CrmContactVo> mList;
    private String id = "";// 商机的id

    private String name;// 姓名
    private String phone;// 电话
    private String product;// 产品id
    private String proname;// 产品名称
    private String money;// 产品金额
    private String bstatus;
    private boolean isVperson =false;

    public String getBstatus() {
        return bstatus;
    }

    public void setBstatus(String bstatus) {
        this.bstatus = bstatus;
    }

    public String getName() {
        return name;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
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

    public String getPhone() {
        return phone;
    }

    private String statekey = "1"; // 判断是什么要用这个适配器

    public String getStatekey() {
        return statekey;
    }

    public void setStatekey(String statekey) {
        this.statekey = statekey;
    }

    public String getId() {
        return id;
    }

    public CrmBussinesAdpterListChange(Context context,
            List<CrmContactVo> list1) {
        this.mContext = context;
        this.mList = list1;

    }
    public CrmBussinesAdpterListChange(Context context,
            List<CrmContactVo> list1,Activity activity) {
        this.mContext = context;
        this.mActivity = activity;
        this.mList = list1;
        
    }

    public boolean isVperson() {
        return isVperson;
    }

    public void setVperson(boolean isVperson) {
        this.isVperson = isVperson;
    }

    public CrmBussinesAdpterListChange(Context context) {
        this.mContext = context;
        this.mList = new ArrayList<CrmContactVo>();

    }

    @Override
    public int getCount() {

        return mList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return mList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {

        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.crm_changeinfo_list, null);
            holder.contants_name = (TextView) convertView.findViewById(R.id.text_name);
            holder.checkBox_up_check_selectall = (ImageView) convertView.findViewById(R.id.checkBox_up_check_selectall);
            holder.allliney = (LinearLayout) convertView.findViewById(R.id.allliney);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 表示销售阶段
        if (statekey.equals("1")) {
            holder.contants_name.setText(mList.get(position).getName());
        } else if (statekey.equals("-1")) {

            if (CommonUtils.isNormalString(mList.get(position).getLname())) {
                holder.contants_name.setText(mList.get(position).getLname());

            } else {
                holder.contants_name.setText(mList.get(position).getName());
            }
        }
        else {
            holder.contants_name.setText(mList.get(position).getName());
        }
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
                for (int i = 0; i < mList.size(); i++) {
                    mList.get(i).setFalgecontant("0");
                }
                if (statekey.equals("-1")) {
                    id = mList.get(position).getLid();
                } else {
                    id = mList.get(position).getId();
                }

                name = mList.get(position).getName();
                bstatus = mList.get(position).getStatus();
                product = mList.get(position).getProduct();
                proname = mList.get(position).getProname();
                money = mList.get(position).getMoney();
                phone = mList.get(position).getPhone();

                
                if(null==name||name.equals("")){
                    name="";
                }
                if(null==bstatus||bstatus.equals("")){
                    bstatus="";
                }
                if(null==product||product.equals("")){
                    product="";
                }
                if(null==proname||proname.equals("")){
                    proname="";
                }
                if(null==money||money.equals("")){
                    money="";
                }
                if(null==phone||phone.equals("")){
                    phone="";
                }
                mList.get(position).setFalgecontant("1");
                notifyDataSetChanged();
                
                if(isVperson){
                    Intent mIntent = new Intent();
                    mIntent.putExtra("name", name);
                    mIntent.putExtra("pid", id);
                    mActivity.setResult(2020, mIntent);
                   mActivity.finish();
                }
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
