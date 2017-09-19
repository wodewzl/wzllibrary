
package com.bs.bsims.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.BossStatiscsDocumentaryAnalysisActivity;
import com.bs.bsims.activity.BossStatisticsAttendanceActivity;
import com.bs.bsims.activity.BossStatisticsBussinessActivity;
import com.bs.bsims.activity.BossStatisticsClientActivity;
import com.bs.bsims.activity.BossStatisticsExpensesLineChartActivity;
import com.bs.bsims.activity.BossStatisticsLeaveActivity;
import com.bs.bsims.activity.BossStatisticsSaleValueActivity;
import com.bs.bsims.activity.BossStatisticsSalesFunnelActivity;
import com.bs.bsims.activity.BossStatisticsTaskStatisticsActivity;
import com.bs.bsims.activity.BossStatisticsTradeActivity;
import com.bs.bsims.activity.GaoDeMapLoactionIndexActivity;
import com.bs.bsims.activity.HumanResoureActivity;
import com.bs.bsims.activity.StatisticsArticlesActivity;
import com.bs.bsims.model.CrmProductVo;
import com.bs.bsims.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class CrmSaleDetailsAdapter extends BaseAdapter {
    private Context mContext;
    public List<CrmProductVo> mList;

    // crm销售计划为 1
    // boss统计首页为 2
    private String mStateKey = "";// 判断是否是crm销售计划还是boss统计首页

    public CrmSaleDetailsAdapter(Context context, List<CrmProductVo> list1) {
        this.mContext = context;
        this.mList = list1;
    }

    public CrmSaleDetailsAdapter(Context context, String stateKey) {
        this.mContext = context;
        this.mList = new ArrayList<CrmProductVo>();
        this.mStateKey = stateKey;

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
        CrmProductVo vo = mList.get(position);
        ViewHolder holder;
        // Crm的销售计划
        if (mStateKey.equals("1")) {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.crm_saledetails_listadpter1, null);
                holder.sale_cname = (TextView) convertView.findViewById(R.id.sale_cname);
                holder.sale_percent = (TextView) convertView.findViewById(R.id.sale_percent);
                holder.sale_finshmoney = (TextView) convertView.findViewById(R.id.sale_finshmoney);
                holder.sale_goalmoney = (TextView) convertView.findViewById(R.id.sale_goalmoney);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.sale_cname.setText(vo.getName() + "(" + vo.getTitle() + ")");
            int numInt = (int) Float.parseFloat(vo.getRatio());
            if (numInt < 30) {
                holder.sale_percent.setTextColor(Color.parseColor("#ff8745"));
            } else if (numInt >= 30 && numInt < 80) {
                holder.sale_percent.setTextColor(Color.parseColor("#21b2ff"));
            } else {
                holder.sale_percent.setTextColor(Color.parseColor("#1d8800"));
            }
            holder.sale_percent.setText(vo.getPercent());
            holder.sale_finshmoney.setText("已完成: " + CommonUtils.formatDetailMoney(vo.getActual()));
            holder.sale_goalmoney.setText("目标: " + CommonUtils.formatDetailMoney(vo.getTarget()));
        }

        // boss统计的boss首页
        else {

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.crm_saledetails_listadpter, null);
                holder.sale_cname = (TextView) convertView.findViewById(R.id.sale_cname);
                holder.sale_percent = (TextView) convertView.findViewById(R.id.sale_percent);
                holder.sale_finshmoney = (TextView) convertView.findViewById(R.id.sale_finshmoney);
                holder.sale_goalmoney = (TextView) convertView.findViewById(R.id.sale_goalmoney);
                holder.item_name = (TextView) convertView.findViewById(R.id.item_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            /*
             * '1' => '人资报告'#45C0F0, '2' => '员工定位'#EA4A4A, '3' => '费用统计'#F5C52F, '4' => '事假'#16C686,
             * '5' => '缺日志'#5E97F6, '6' => '缺卡'#B78BDA, '7' => '迟到'#EA4A4A, '8' => '早退'#2BA8D9, '9'
             * => '加班', '10' => '丧假'#9F96FB, '11' => '病假'#FC6F6D, '12' => '陪产假'#6FA6F4, '13' =>
             * '婚假'#16C1C3, '14' => '调休假'#59C7FC, '15' => '公休假'#F6CF32, '16' => '员工培训'#F6CF32, '17'
             * => '招聘概况'#16C1C3, '18' => '离职情况'#9F97FC, '19' => '用品'#F5C52F, '20' => '任务'#40D7C7,
             * '21' => '客户分析'#B78BDA, '22' => '商机分析'#16C686, '23' => '销售漏斗'#F5C52F, '24' =>
             * '销售业绩'#40D7C7, '25' => '跟单分析'#EA4A4A, '26' => '合同分析'#2BA8D9_, *
             */
            holder.sale_cname.setText(vo.getName());
            // holder.sale_cname.setTextSize(16);
            holder.sale_finshmoney.setTextSize(12);
            holder.sale_goalmoney.setTextSize(12);
            holder.item_name.setTextSize(12);
            if (vo.getData_left().size() > 1) {
                // 说明有两个
                holder.sale_finshmoney.setText(vo.getData_left().get(0));
                holder.sale_goalmoney.setVisibility(View.VISIBLE);
                holder.sale_goalmoney.setText(vo.getData_left().get(1));
            } else {
                holder.sale_finshmoney.setText(vo.getData_left().get(0));
                holder.sale_goalmoney.setVisibility(View.GONE);
            }

            holder.sale_percent.setText(vo.getData_right());

            holder.item_name.setText(vo.getRight_name());

            String id = vo.getId();
            int s_id = Integer.parseInt(id);
            final Intent intent = new Intent();
            intent.putExtra("type", s_id);
            intent.putExtra("title_name", vo.getName());

            switch (s_id) {
                case 1:
                    intent.setClass(mContext, HumanResoureActivity.class);// 人力资源
                    intent.putExtra("key", 0);
                    holder.sale_percent.setTextColor(Color.parseColor("#45C0F0"));
                    holder.sale_cname.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.statistics_human), null, null, null);
                    break;
                case 2:
                    intent.setClass(mContext, GaoDeMapLoactionIndexActivity.class);
                    holder.sale_percent.setTextColor(Color.parseColor("#EA4A4A"));
                    holder.sale_cname.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.statistics_gaode), null, null, null);
                    break;
                case 3:// 费用统计
                    intent.setClass(mContext, BossStatisticsExpensesLineChartActivity.class);
                    holder.sale_percent.setTextColor(Color.parseColor("#F5C52F"));
                    holder.sale_cname.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.statistics_allmoney), null, null, null);
                    break;
                case 4://
                    intent.setClass(mContext, BossStatisticsLeaveActivity.class);
                    holder.sale_percent.setTextColor(Color.parseColor("#16C686"));
                    holder.sale_cname.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.statictis_thingsleave), null, null, null);

                    break;
                case 5:// 缺日志

                    intent.setClass(mContext, BossStatisticsAttendanceActivity.class);
                    holder.sale_percent.setTextColor(Color.parseColor("#5E97F6"));
                    holder.sale_cname.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.statistics_nodatework), null, null, null);
                    break;
                case 6:// 缺卡
                    intent.setClass(mContext, BossStatisticsAttendanceActivity.class);
                    holder.sale_percent.setTextColor(Color.parseColor("#B78BDA"));
                    holder.sale_cname.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.statistics_mscard), null, null, null);

                    break;
                case 7:// 迟到
                    intent.setClass(mContext, BossStatisticsAttendanceActivity.class);
                    holder.sale_percent.setTextColor(Color.parseColor("#EA4A4A"));
                    holder.sale_cname.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.statistics_worklate), null, null, null);

                    break;
                case 8:
                    intent.setClass(mContext, BossStatisticsAttendanceActivity.class);
                    holder.sale_percent.setTextColor(Color.parseColor("#2BA8D9"));
                    holder.sale_cname.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.statistics_leave_early), null, null, null);

                    break;
                case 9:// 加班
                    intent.setClass(mContext, BossStatisticsLeaveActivity.class);
                    holder.sale_percent.setTextColor(Color.parseColor("#FC97C4"));
                    holder.sale_cname.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.statistics_workup), null, null, null);

                    break;
                case 10:
                    intent.setClass(mContext, BossStatisticsLeaveActivity.class);
                    holder.sale_percent.setTextColor(Color.parseColor("#9F96FB"));
                    holder.sale_cname.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.statistics_dieleave), null, null, null);

                    break;

                case 11:// 病假
                    intent.setClass(mContext, BossStatisticsLeaveActivity.class);
                    holder.sale_percent.setTextColor(Color.parseColor("#FC6F6D"));
                    holder.sale_cname.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.statistics_sickleave), null, null, null);

                    break;

                case 12:
                    intent.setClass(mContext, BossStatisticsLeaveActivity.class);
                    holder.sale_percent.setTextColor(Color.parseColor("#6FA6F4"));
                    holder.sale_cname.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.statistics_paternitleave), null, null, null);

                    break;
                case 13:// 婚假
                    intent.setClass(mContext, BossStatisticsLeaveActivity.class);
                    holder.sale_percent.setTextColor(Color.parseColor("#16C1C3"));
                    holder.sale_cname.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.statistics_marriyed), null, null, null);

                    break;
                case 14:// 调休
                    intent.setClass(mContext, BossStatisticsLeaveActivity.class);
                    holder.sale_percent.setTextColor(Color.parseColor("#59C7FC"));
                    holder.sale_cname.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.statistics_take_working), null, null, null);

                    break;
                case 15:
                    intent.setClass(mContext, BossStatisticsLeaveActivity.class);
                    holder.sale_percent.setTextColor(Color.parseColor("#F6CF32"));
                    holder.sale_cname.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.statistics_publicleave), null, null, null);

                    break;
                case 16:
                    intent.setClass(mContext, HumanResoureActivity.class);// 人力资源
                    intent.putExtra("key", 3);
                    holder.sale_percent.setTextColor(Color.parseColor("#F6CF32"));
                    holder.sale_cname.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.statistics_tran), null, null, null);

                    break;
                case 17:
                    holder.sale_percent.setTextColor(Color.parseColor("#16C1C3"));
                    holder.sale_cname.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.statistics_advertise), null, null, null);
                    intent.setClass(mContext, HumanResoureActivity.class);// 人力资源
                    intent.putExtra("key", 1);
                    break;
                case 18:
                    holder.sale_percent.setTextColor(Color.parseColor("#9F97FC"));
                    holder.sale_cname.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.statistics_workleave), null, null, null);
                    intent.setClass(mContext, HumanResoureActivity.class);// 人力资源
                    intent.putExtra("key", 2);
                    break;
                case 19:
                    intent.setClass(mContext, StatisticsArticlesActivity.class);
                    holder.sale_percent.setTextColor(Color.parseColor("#F5C52F"));
                    holder.sale_cname.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.statistics_product), null, null, null);
                    break;
                case 20:
                    intent.setClass(mContext, BossStatisticsTaskStatisticsActivity.class);
                    holder.sale_percent.setTextColor(Color.parseColor("#40D7C7"));
                    holder.sale_cname.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.statistics_task), null, null, null);
                    break;
                case 21:
                    intent.setClass(mContext, BossStatisticsClientActivity.class);
                    holder.sale_percent.setTextColor(Color.parseColor("#B78BDA"));
                    holder.sale_cname.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.statistics_customer), null, null, null);

                    break;
                case 22:
                    intent.setClass(mContext, BossStatisticsBussinessActivity.class);
                    holder.sale_percent.setTextColor(Color.parseColor("#16C686"));
                    holder.sale_cname.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.statistics_bussines), null, null, null);
                    break;
                case 23:
                    intent.setClass(mContext, BossStatisticsSalesFunnelActivity.class);
                    holder.sale_percent.setTextColor(Color.parseColor("#F5C52F"));
                    holder.sale_cname.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.statistics_salefunnel), null, null, null);
                    break;
                case 24:
                    intent.setClass(mContext, BossStatisticsSaleValueActivity.class);
                    holder.sale_percent.setTextColor(Color.parseColor("#40D7C7"));
                    holder.sale_cname.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.statistics_sale), null, null, null);
                    break;
                case 25:
                    intent.setClass(mContext, BossStatiscsDocumentaryAnalysisActivity.class);
                    holder.sale_percent.setTextColor(Color.parseColor("#EA4A4A"));
                    holder.sale_cname.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.statistics_visitor), null, null, null);
                    break;
                case 26:
                    intent.setClass(mContext, BossStatisticsTradeActivity.class);
                    holder.sale_percent.setTextColor(Color.parseColor("#2BA8D9"));
                    holder.sale_cname.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.statistics_trade), null, null, null);
                    break;

            }
            convertView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (intent.getComponent() != null)
                        mContext.startActivity(intent);
                }
            });
        }

        return convertView;
    }

    static class ViewHolder {
        private TextView sale_cname, sale_percent, sale_finshmoney, sale_goalmoney, item_name;
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
