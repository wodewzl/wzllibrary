
package com.bs.bsims.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.CrmVisitRecordDetailActivity;
import com.bs.bsims.activity.ImagePreviewActivity;
import com.bs.bsims.model.CrmVisitorVo;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.view.BSCircleImageView;

import java.util.ArrayList;

public class CrmVisitorIndexListAdapter extends BSBaseAdapter<CrmVisitorVo> {

    public static String PhoneVistorColor = "#ffae00";
    public static String DoorVistorColor = "#00aeff";
    public static String OnlieVistorColor = "#2eb3bf";
    public static String OtherVistorColor = "#9733ec";
    public static final String PHONE = "电话拜访";
    public static final String DOOR = "上门拜访";
    public static final String ONLIE = "在线沟通";
    public static final String OTHER = "其他方式";

    /*
     * 保存数据源
     */
    public CrmVisitorVo cVo;

    private String state;// 区别使用的布局

    /***
     * 拜访记录的列表适配器
     */
    private Context mContext;

    public CrmVisitorIndexListAdapter(Context context) {
        super(context);
        this.mContext = context;

    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(final int postion, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (mIsEmpty) {
            return super.getView(postion, convertView, parent);
        }

        if (convertView != null && convertView.getTag() == null)
            convertView = null;
        if ("1".equals(state)) {
            GradientDrawable bgdraw = null;
            String date = "";
            // RelativeLayout.LayoutParams layoutParams = new
            // RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            // ViewGroup.LayoutParams.WRAP_CONTENT);
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View
                        .inflate(mContext, R.layout.crmvisitrecord_index_listadpter, null);

                holder.head_iconbasic = (BSCircleImageView) convertView
                        .findViewById(R.id.head_iconbasic);
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.pame = (TextView) convertView.findViewById(R.id.pame);
                holder.department = (TextView) convertView.findViewById(R.id.department);// 放时间
                holder.detail = (TextView) convertView.findViewById(R.id.detail);// 放部门信息
                holder.record_time = (TextView) convertView.findViewById(R.id.record_time);
                holder.c_title = (TextView) convertView.findViewById(R.id.c_title);
                holder.item_layout = (LinearLayout) convertView.findViewById(R.id.item_layout);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // 拿当前条的date与上一条进行比对

            if (postion == 0) {
                holder.record_time.setVisibility(View.VISIBLE);
                date = mList.get(postion).getDate();
                holder.record_time.setText(date);
            }
            else {
                if (mList.get(postion).getDate().equals(mList.get(postion - 1).getDate())) {
                    holder.record_time.setVisibility(View.GONE);
                }
                else {
                    holder.record_time.setText(mList.get(postion).getDate());
                    holder.record_time.setVisibility(View.VISIBLE);
                }

            }

            /*
             * 颜色值
             */

            if (mList.get(postion).getMode().equals(PHONE)) {
                bgdraw = CommonUtils.setBackgroundShap(mContext, 5, PhoneVistorColor, "#ffffff");
                holder.c_title.setTextColor(Color.parseColor(PhoneVistorColor));
            }
            else if (mList.get(postion).getMode().equals(DOOR)) {
                bgdraw = CommonUtils.setBackgroundShap(mContext, 5, DoorVistorColor, "#ffffff");
                holder.c_title.setTextColor(Color.parseColor(DoorVistorColor));
            }
            else if (mList.get(postion).getMode().equals(ONLIE)) {
                bgdraw = CommonUtils.setBackgroundShap(mContext, 5, OnlieVistorColor, "#ffffff");
                holder.c_title.setTextColor(Color.parseColor(OnlieVistorColor));
            }
            else {
                bgdraw = CommonUtils.setBackgroundShap(mContext, 5, OtherVistorColor, "#ffffff");
                holder.c_title.setTextColor(Color.parseColor(OtherVistorColor));
            }

            holder.c_title.setText(mList.get(postion).getMode());
            holder.c_title.setBackgroundDrawable(bgdraw);
            mImageLoader.displayImage(mList.get(postion).getHeadpic(), holder.head_iconbasic,
                    CommonUtils.initImageLoaderOptions());
            holder.head_iconbasic.setUserId(mList.get(postion).getUserid());//HL:获取头像对应的用户ID，以便响应跳转
            holder.head_iconbasic.setUrl(mList.get(postion).getHeadpic());
            holder.head_iconbasic.setUserName(mList.get(postion).getFullName());
            if ("1".equals(mList.get(postion).getIsread())) {
                holder.head_iconbasic.setIsread("1");
            } else {
                holder.head_iconbasic.setIsread("0");
            }
            holder.name.setText(mList.get(postion).getFullName());
            holder.pame.setText(mList.get(postion).getDepartmentName());
            holder.department.setText(DateUtils.parseHour(mList.get(postion).getTime()));
            holder.detail.setText(mList.get(postion).getCname());
            convertView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    Intent i = new Intent();
                    i.setClass(mContext, CrmVisitRecordDetailActivity.class);
                    i.putExtra("vid", mList.get(postion).getVid());
                    mList.get(postion).setIsread("1");
                    notifyDataSetChanged();
                    mContext.startActivity(i);
                }
            });

            return convertView;
        }

        // 选择签到位置（带圈单选）
        else if (state.equals("3")) {
            String date = "";
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.crm_visitor_sign_select, null);
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.pame = (TextView) convertView.findViewById(R.id.pame);
                holder.record_time = (TextView) convertView.findViewById(R.id.record_time);
                holder.checkBox_up_check_selectall = (ImageView) convertView
                        .findViewById(R.id.checkBox_up_check_selectall);
                holder.item_layout = (LinearLayout) convertView.findViewById(R.id.item_layout);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.checkBox_up_check_selectall.setVisibility(View.VISIBLE);
            // 拿当前条的date与上一条进行比对

            if (postion == 0) {
                holder.record_time.setVisibility(View.VISIBLE);
                date = mList.get(postion).getDate();
                holder.record_time.setText(date + " " + DateUtils.getDayOfWeek(date));
            }
            else {
                if (mList.get(postion).getDate().equals(mList.get(postion - 1).getDate())) {
                    holder.record_time.setVisibility(View.GONE);
                }
                else {
                    holder.record_time.setText(mList.get(postion).getDate() + " "
                            + DateUtils.getDayOfWeek(mList.get(postion).getDate()));
                    holder.record_time.setVisibility(View.VISIBLE);
                }

            }

            holder.name.setText(mList.get(postion).getAddress());

            if (null != mList.get(postion).getImgs()) {
                holder.name.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext
                        .getResources().getDrawable(R.drawable.item_sgin_in_list_ic_n), null);
            }
            else {
                holder.name.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }

            if (mList.get(postion).getFalgecontant().equals("1")) {
                holder.checkBox_up_check_selectall.setImageResource(R.drawable.common_ic_selected);
            }
            else {
                holder.checkBox_up_check_selectall.setImageResource(R.drawable.common_ic_unselect);
            }
            holder.pame.setText(DateUtils.parseHour(mList.get(postion).getAddtime()));
            holder.item_layout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    for (int i = 0; i < mList.size(); i++) {
                        mList.get(i).setFalgecontant("0");
                    }
                    cVo = new CrmVisitorVo();
                    cVo.setCsid(mList.get(postion).getCsid());
                    cVo.setAddress(mList.get(postion).getAddress());
                    cVo.setAddtime(DateUtils.parseDateDayAndHour(mList.get(postion).getAddtime()));
                    cVo.setImgs(mList.get(postion).getImgs());
                    mList.get(postion).setFalgecontant("1");
                    notifyDataSetChanged();
                }
            });

            return convertView;
        }

        // 签到位置列表
        else {
            String date = "";
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.crm_visitor_sign, null);
                holder.record_time = (TextView) convertView.findViewById(R.id.record_time);// 标题时间
                holder.name = (TextView) convertView.findViewById(R.id.user_name);// 用户姓名
                holder.pame = (TextView) convertView.findViewById(R.id.user_pname);// 用户岗位
                holder.detail = (TextView) convertView.findViewById(R.id.time);// 用户时间
                holder.head_iconbasic = (BSCircleImageView) convertView
                        .findViewById(R.id.head_iconbasic);// 用户头像
                holder.c_title = (TextView) convertView.findViewById(R.id.address_info);// 地址信息
                holder.department = (TextView) convertView.findViewById(R.id.img_getinfo);// 图片点击
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // 拿当前条的date与上一条进行比对
            if (postion == 0) {
                holder.record_time.setVisibility(View.VISIBLE);
                date = mList.get(postion).getDate();
                holder.record_time.setText(date + " " + DateUtils.getDayOfWeek(date));
            }
            else {
                if (mList.get(postion).getDate().equals(mList.get(postion - 1).getDate())) {
                    holder.record_time.setVisibility(View.GONE);
                }
                else {
                    holder.record_time.setText(mList.get(postion).getDate() + " "
                            + DateUtils.getDayOfWeek(mList.get(postion).getDate()));
                    holder.record_time.setVisibility(View.VISIBLE);
                }

            }
            mImageLoader.displayImage(mList.get(postion).getHeadpic(), holder.head_iconbasic,
                    CommonUtils.initImageLoaderOptions());
            holder.head_iconbasic.setUserId(mList.get(postion).getCid());//HL:获取头像对应的用户ID，以便响应跳转
            holder.head_iconbasic.setUserName(mList.get(postion).getFullName());
            holder.head_iconbasic.setUrl(mList.get(postion).getHeadpic());
            holder.name.setText(mList.get(postion).getFullName());
            holder.pame.setText(mList.get(postion).getPname());
            holder.detail.setText(DateUtils.parseHour(mList.get(postion).getAddtime()));
            holder.c_title.setText(mList.get(postion).getAddress());

            if (mList.get(postion).getSex().equals("男")) {
                holder.name.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext
                        .getResources().getDrawable(R.drawable.sex_man), null);
            }
            else {
                holder.name.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext
                        .getResources().getDrawable(R.drawable.sex_woman), null);

            }

            if (null != mList.get(postion).getImgs()) {
                holder.department.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext
                        .getResources().getDrawable(R.drawable.item_sgin_in_list_ic_n), null);
            }
            else {
                holder.department.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }

            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    if (null != mList.get(postion).getImgs()) {
                        Intent intent = new Intent();
                        intent.putStringArrayListExtra("piclist", (ArrayList<String>) mList.get(postion).getImgs());
                        intent.setClass(mContext, ImagePreviewActivity.class);
                        intent.putExtra("imgIndex", 0);
                        mContext.startActivity(intent);
                    }
                    else {
                        return;
                    }

                }
            });

            return convertView;
        }

    }

    static class ViewHolder {
        private BSCircleImageView head_iconbasic;
        private TextView name, department, detail, record_time, c_title, pame;
        private LinearLayout item_layout;
        private ImageView checkBox_up_check_selectall;
    }

}
