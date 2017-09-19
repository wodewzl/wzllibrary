
package com.bs.bsims.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.AppOnlineActivity;
import com.bs.bsims.model.AppMakertModel;

import java.util.ArrayList;
import java.util.List;

public class AppMarketAdapter extends BaseAdapter {
    public List<AppMakertModel> mList = null;
    private Context mContext;
    private Animation animation;
    private String isRight = "";

    public AppMarketAdapter(Context mContext) {
        this.mContext = mContext;
        this.mList = new ArrayList<AppMakertModel>();
        animation = AnimationUtils.loadAnimation(mContext, R.anim.view_shake);
    }

    public int getCount() {
        return this.mList.size();
    }

    public Object getItem(int position) {
        return mList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        final ViewHolder holder;
        final AppMakertModel pVo = mList.get(position);
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.appmakert_adapter, null);
            holder.mItemImg = (ImageView) view.findViewById(R.id.makert_inco);
            holder.mTitleName = (TextView) view.findViewById(R.id.title_name);
            holder.mTitleState = (TextView) view.findViewById(R.id.title_state);
            holder.mTitleisOpTv = (TextView) view.findViewById(R.id.isopen);
            holder.mCotentTv = (TextView) view.findViewById(R.id.content);
            holder.mTitleState1 = (TextView) view.findViewById(R.id.title_state1);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.mTitleName.setText(pVo.getMname());

        holder.mCotentTv.setText(pVo.getMdes().toString().trim());

        if (isRight.equals("1")) {

            if (pVo.getMexpire().equals("")) {
                holder.mTitleisOpTv.setVisibility(View.GONE);
            }
            else {
                holder.mTitleisOpTv.setVisibility(View.VISIBLE);
                if (Integer.parseInt(pVo.getMexpire()) > 0) {
                    holder.mTitleisOpTv.setText("还剩" + pVo.getMexpire() + "天到期");
                }
                else {
                    holder.mTitleisOpTv.setText("已过期" + pVo.getMexpire() + "天");
                }

            }

        }
        else {
            if (pVo.getMfree().equals("1")) {
                holder.mTitleState1.setVisibility(View.VISIBLE);
                holder.mTitleState1.setBackgroundResource(R.drawable.frame_shixing_blue);
                holder.mTitleState1.setTextColor(mContext.getResources().getColor(R.color.white));
                holder.mTitleState1.setText("免费");
            }
            else {
                holder.mTitleState1.setVisibility(View.GONE);
            }
            if (pVo.getMnew().equals("1")) {
                holder.mTitleState.setVisibility(View.VISIBLE);
                holder.mTitleState.setBackgroundResource(R.drawable.frame_shixing_yellow);
                holder.mTitleState.setTextColor(mContext.getResources().getColor(R.color.white));
                holder.mTitleState.setText("NEW");
                holder.mTitleState.startAnimation(animation);
            }
            else {
                holder.mTitleState.clearAnimation();
                holder.mTitleState.setVisibility(View.GONE);
            }

            // 已开通
            if (pVo.getMopen().endsWith("1")) {
                holder.mTitleisOpTv.setVisibility(View.VISIBLE);
                holder.mTitleisOpTv.setBackgroundResource(R.drawable.frame_kongxin_green);
                holder.mTitleisOpTv.setTextColor(mContext.getResources().getColor(R.color.C12));
                holder.mTitleisOpTv.setText("已开通");
            }
            else if(pVo.getMopen().endsWith("2")){
                holder.mTitleisOpTv.setVisibility(View.VISIBLE);
                holder.mTitleisOpTv.setBackgroundResource(R.drawable.frame_kongxin_zise);
                holder.mTitleisOpTv.setTextColor(mContext.getResources().getColor(R.color.liacl));
                holder.mTitleisOpTv.setText("已试用");
            }
            else if(pVo.getMopen().endsWith("0")){
                holder.mTitleisOpTv.setVisibility(View.VISIBLE);
                holder.mTitleisOpTv.setBackgroundResource(R.drawable.frame_kongxin_blue);
                holder.mTitleisOpTv.setTextColor(mContext.getResources().getColor(R.color.bule_go));
                if(pVo.getMfree().equals("1")){
                    holder.mTitleisOpTv.setText("开通");
                }
                else{
                    holder.mTitleisOpTv.setText("申请试用");
                }
            }
            else if(pVo.getMopen().endsWith("3")){
                holder.mTitleisOpTv.setVisibility(View.VISIBLE);
                holder.mTitleisOpTv.setBackgroundResource(R.drawable.frame_kongxin_yellow);
                holder.mTitleisOpTv.setTextColor(mContext.getResources().getColor(R.color.yellow));
                holder.mTitleisOpTv.setText("试用中");
            }
            else if(pVo.getMopen().endsWith("4")){
                holder.mTitleisOpTv.setVisibility(View.VISIBLE);
                holder.mTitleisOpTv.setBackgroundResource(R.drawable.frame_kongxin_gray);
                holder.mTitleisOpTv.setTextColor(mContext.getResources().getColor(R.color.gray_bg));
                holder.mTitleisOpTv.setText("已关闭");
            }
            else if(pVo.getMopen().endsWith("5")){
                holder.mTitleisOpTv.setVisibility(View.VISIBLE);
                holder.mTitleisOpTv.setBackgroundResource(R.drawable.frame_kongxin_red);
                holder.mTitleisOpTv.setTextColor(mContext.getResources().getColor(R.color.red));
                holder.mTitleisOpTv.setText("已到期");
            }

        }

        Class<com.bs.bsims.R.drawable> cls = R.drawable.class;
        int imageId = 0;
        switch (Integer.parseInt(pVo.getMdid())) {

            case 2:
                // 通知
                try {
                    imageId = cls.getDeclaredField("office003").getInt(null);
                    holder.mItemImg.setImageResource(imageId);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case 109:
                // 公文
                try {
                    imageId = cls.getDeclaredField("office004").getInt(null);
                    holder.mItemImg.setImageResource(imageId);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case 108:
                // 制度
                try {
                    imageId = cls.getDeclaredField("office005").getInt(null);
                    holder.mItemImg.setImageResource(imageId);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case 5:
                // 审批事务
                holder.mItemImg.setImageResource(R.drawable.affair001);
                imageId = R.drawable.affair001;
                break;
            case 4:
                // 任务跟进
                holder.mItemImg.setImageResource(R.drawable.affair002);
                imageId = R.drawable.affair002;
                break;
            case 110:
                // 企业风采
                try {
                    imageId = cls.getDeclaredField("office006").getInt(null);
                    holder.mItemImg.setImageResource(imageId);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case 3:
                // 意见分享
                holder.mItemImg.setImageResource(R.drawable.affair003);
                imageId = R.drawable.affair003;
                break;

            case 101:
                // 日程管理
                holder.mItemImg.setImageResource(R.drawable.ic_main_message_general_09);
                imageId = R.drawable.ic_main_message_general_09;
                break;

            case 6:
                // 企业云盘
                holder.mItemImg.setImageResource(R.drawable.ic_main_message_general_10);
                imageId = R.drawable.ic_main_message_general_10;
                break;
            case 7:
                // 销售机会
                holder.mItemImg.setImageResource(R.drawable.marketing005);
                imageId = R.drawable.marketing005;
                break;
            case 8:
                // 考勤管理
                holder.mItemImg.setImageResource(R.drawable.ic_main_message_general_03);
                imageId = R.drawable.ic_main_message_general_03;
                break;
            case 12:
                // 数据统计管理
                holder.mItemImg.setImageResource(R.drawable.ic_main_message_general_04);
                imageId = R.drawable.ic_main_message_general_04;
                break;

            case 16:
                // 联系人列表
                holder.mItemImg.setImageResource(R.drawable.home_crm_contacts);
                imageId = R.drawable.home_crm_contacts;
                break;
            case 17:
                // 回款审批
                holder.mItemImg.setImageResource(R.drawable.home_crm_repayment);
                imageId = R.drawable.home_crm_repayment;
                break;
            case 13:
                // 员工定位
                holder.mItemImg.setImageResource(R.drawable.affair012);
                imageId = R.drawable.affair012;
                break;

            case 18:
                // 跟单记录
                holder.mItemImg.setImageResource(R.drawable.home_crm_visitor);
                imageId = R.drawable.home_crm_visitor;
                break;
            case 10:
                // 奖惩管理
                holder.mItemImg.setImageResource(R.drawable.ic_main_message_general_03_money);
                imageId = R.drawable.ic_main_message_general_03_money;
                break;
            case 11:
                // 人资管理
                holder.mItemImg.setImageResource(R.drawable.affair010);
                imageId = R.drawable.affair010;
                break;

            case 1:
                // 日志提醒
                holder.mItemImg.setImageResource(R.drawable.ic_main_message_general_02);
                imageId = R.drawable.ic_main_message_general_02;
                break;
            case 9:
                // 用品管理
                holder.mItemImg.setImageResource(R.drawable.ic_main_message_general_03_product);
                imageId = R.drawable.ic_main_message_general_03_product;
                break;
            // IM聊天
            case 20:
                holder.mItemImg.setImageResource(R.drawable.ic_main_message_im);
                imageId = R.drawable.ic_main_message_im;
                break;
            // 自定义审批
            case 118:
                // 审批事务
                holder.mItemImg.setImageResource(R.drawable.affair001);
                imageId = R.drawable.affair001;
                break;

        }

        final String imgId = imageId + "";
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(mContext, AppOnlineActivity.class);
                i.putExtra("img_title", imgId);
                i.putExtra("name_title", pVo.getMname());
                i.putExtra("name_content", pVo.getMdes());
                i.putExtra("mdid", pVo.getMdid());
                i.putExtra("otherName", pVo.getMalias());
                i.putExtra("ismode", pVo.getMdemo());// 是否显示在线演示
                if (position % 2 == 0) {// 除的尽
                    i.putExtra("num_rat", 4.0);
                }
                else {
                    i.putExtra("num_rat", 5.0);
                }
                mContext.startActivity(i);
            }
        });

        holder.mTitleisOpTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (holder.mTitleisOpTv.getText().equals("申请试用")||holder.mTitleisOpTv.getText().equals("开通")) {
                    Intent i = new Intent();
                    i.setClass(mContext, AppOnlineActivity.class);
                    i.putExtra("img_title", imgId);
                    i.putExtra("name_title", pVo.getMname());
                    i.putExtra("name_content", pVo.getMdes());
                    i.putExtra("mdid", pVo.getMdid());
                    i.putExtra("otherName", pVo.getMalias());
                    i.putExtra("ismode", pVo.getMdemo());// 是否显示在线演示
                    if (position % 2 == 0) {// 除的尽
                        i.putExtra("num_rat", 4.0);
                    }
                    else {
                        i.putExtra("num_rat", 5.0);
                    }
                    mContext.startActivity(i);
                }

            }
        });
        return view;

    }

    static class ViewHolder {
        private ImageView mItemImg;
        private TextView mTitleName, mTitleState, mTitleState1, mTitleisOpTv, mCotentTv;
    }

    public void updateData(List<AppMakertModel> list) {
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataFrist(List<AppMakertModel> list) {
        list.addAll(mList);
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataLast(List<AppMakertModel> list) {
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public String getIsRight() {
        return isRight;
    }

    public void setIsRight(String isRight) {
        this.isRight = isRight;
    }

}
