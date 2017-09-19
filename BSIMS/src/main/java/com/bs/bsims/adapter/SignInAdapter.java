
package com.bs.bsims.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.SignInVO;
import com.bs.bsims.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author peck
 * @Description: 移动打卡页面 从讯科中拿过来修改
 * @date 2015-5-13 下午3:18:51
 * @email 971371860@qq.com
 * @version V1.0
 */
public class SignInAdapter extends BaseAdapter {

    private Context context;

    public List<SignInVO> dataList;

    private int count;

    public SignInAdapter(Context context, List<SignInVO> dataList) {
        super();
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    public void setDataList(List<SignInVO> dataList) {
        this.dataList = dataList;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    // public void updateData(List<SignInVO> list) {
    // dataList.clear();
    // dataList.addAll(list);
    // this.notifyDataSetChanged();
    // }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            view = View.inflate(context, R.layout.item_sign_in_listview, null);
            holder.des = (TextView) view
                    .findViewById(R.id.txt_item_sgin_in_desa);
            holder.txt_top_item_monthy = (RelativeLayout) view
                    .findViewById(R.id.txt_top_item_monthy);
            holder.txt_sign_in_list_year = (TextView) view.findViewById(R.id.txt_sign_in_list_year);
            holder.txt_sign_in_list_month = (TextView) view
                    .findViewById(R.id.txt_sign_in_list_month);
            holder.txt_sign_in_list_count = (TextView) view
                    .findViewById(R.id.txt_sign_in_list_count);
            holder.txt_item_sgin_down_state = (TextView) view
                    .findViewById(R.id.txt_item_sgin_down_state);
            holder.txt_signin_map_local_des = (TextView) view
                    .findViewById(R.id.txt_signin_map_local_des);
            holder.txt_signin_map_locastate = (TextView) view
                    .findViewById(R.id.txt_signin_map_locastate);
            holder.txt_item_sgin_remark = (TextView) view.findViewById(R.id.txt_item_sgin_remark);
            holder.mWokeState = (TextView) view.findViewById(R.id.sign_work_state);
            holder.view_sign_view = view.findViewById(R.id.view_sign_view);
            holder.txt_item_sgin_remark_ly = (LinearLayout) view
                    .findViewById(R.id.txt_item_sgin_remark_ly);
            holder.sign_work_state1 = (ImageView) view.findViewById(R.id.sign_work_state1);
            holder.rl_item_sign_month = (RelativeLayout) view.findViewById(R.id.rl_item_sign_month);
            holder.txt_signin_map_local_des_img = (ImageView) view
                    .findViewById(R.id.txt_signin_map_local_des_img);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        SignInVO signIn = dataList.get(position);
        count = position + 1;
        if (count == dataList.size()) {
            count = dataList.size() - 1;
        }
        SignInVO signIn1 = dataList.get(count);
        String time = signIn.getTime();
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                holder.mWokeState.getLayoutParams().width,
                holder.mWokeState.getLayoutParams().height);

        layoutParams
                .setMargins(holder.rl_item_sign_month.getLayoutParams().width / 2
                        - holder.mWokeState.getLayoutParams().width / 8,
                        holder.mWokeState.getLayoutParams().width / 4,
                        0, 0);
        holder.mWokeState.setLayoutParams(layoutParams);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.ic_sgin_in_list_head_point);
        FrameLayout.LayoutParams layoutParams1 = new FrameLayout.LayoutParams(
                bitmap.getWidth(), bitmap.getHeight());
        layoutParams1
                .setMargins(holder.rl_item_sign_month.getLayoutParams().width / 2
                        - holder.mWokeState.getLayoutParams().width / 4,
                        holder.mWokeState.getLayoutParams().width / 5, 0, 0);

        holder.sign_work_state1.setLayoutParams(layoutParams1);
        holder.mWokeState.setText(signIn.getAtname());
        if (signIn.getIs_cloud().equals("1")) {
            holder.txt_item_sgin_down_state.setText("云考勤机");
            holder.des.setVisibility(View.VISIBLE);
            holder.des.setText(DateUtils.parseMDHMS(time));
            holder.des.setTextColor(Color.parseColor("#02B0FC"));
            holder.view_sign_view.setVisibility(View.GONE);
            holder.des.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            holder.txt_signin_map_local_des.setVisibility(View.GONE);
            holder.txt_signin_map_locastate.setVisibility(View.GONE);
            holder.txt_item_sgin_remark.setVisibility(View.GONE);
            holder.txt_signin_map_local_des_img.setVisibility(View.GONE);
            holder.txt_item_sgin_remark_ly.setVisibility(View.GONE);
            // 考勤状态：0正常 1迟到 2早退
            int status = Integer.parseInt(signIn.getStatus());
            if (status == 1) {
                holder.des.setTextColor(context.getResources().getColor(R.color.red));
                holder.des.setCompoundDrawablesWithIntrinsicBounds(null, null, context
                        .getResources()
                        .getDrawable(R.drawable.item_sgin_in_list_ic_late), null);
            }
            else if (status == 2) {
                holder.des.setTextColor(context.getResources().getColor(R.color.yellow));
                holder.des.setCompoundDrawablesWithIntrinsicBounds(null, null, context
                        .getResources()
                        .getDrawable(R.drawable.item_sgin_in_list_ic_leaveearly), null);
            } else {
                holder.des.setTextColor(Color.parseColor("#00a9fe"));
                holder.des.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }

        } else {
            holder.txt_item_sgin_down_state.setText("移动打卡");
            holder.des.setVisibility(View.VISIBLE);
            holder.des.setText(DateUtils.parseMDHMS(time));
            holder.view_sign_view.setVisibility(View.VISIBLE);
            holder.txt_signin_map_local_des.setVisibility(View.VISIBLE);
            holder.txt_signin_map_locastate.setVisibility(View.VISIBLE);
            holder.txt_item_sgin_remark.setVisibility(View.VISIBLE);
            holder.txt_signin_map_local_des_img.setVisibility(View.VISIBLE);
            // 考勤状态：0正常 1迟到 2早退
            int status = Integer.parseInt(signIn.getStatus());
            if (status == 1) {
                holder.des.setTextColor(context.getResources().getColor(R.color.red));
                holder.des.setCompoundDrawablesWithIntrinsicBounds(null, null, context
                        .getResources()
                        .getDrawable(R.drawable.item_sgin_in_list_ic_late), null);
            }
            else if (status == 2) {
                holder.des.setTextColor(context.getResources().getColor(R.color.yellow));
                holder.des.setCompoundDrawablesWithIntrinsicBounds(null, null, context
                        .getResources()
                        .getDrawable(R.drawable.item_sgin_in_list_ic_leaveearly), null);
            } else {
                holder.des.setTextColor(Color.parseColor("#00a9fe"));
                holder.des.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
            holder.txt_signin_map_local_des.setText(signIn.getAddress());
            if (signIn.getMalposition().equals("0")) {
                holder.txt_signin_map_locastate.setVisibility(View.GONE);
            }
            else {
                holder.txt_signin_map_locastate.setVisibility(View.VISIBLE);
            }

            if (null != signIn.getRemark() && !signIn.getRemark().equals("")) {
                holder.txt_item_sgin_remark_ly.setVisibility(View.VISIBLE);
                holder.txt_item_sgin_remark.setVisibility(View.VISIBLE);
                holder.txt_item_sgin_remark.setText(signIn.getRemark());
            } else {
                holder.txt_item_sgin_remark.setVisibility(View.GONE);
                holder.txt_item_sgin_remark_ly.setVisibility(View.GONE);
            }

        }

        if (Integer.parseInt(signIn.getStime().substring(5)) > Integer.parseInt(signIn1.getStime()
                .substring(5))
                || Integer.parseInt(signIn.getStime().substring(0, 4)) > Integer.parseInt(signIn1
                        .getStime()
                        .substring(0, 4))) {
            holder.txt_top_item_monthy.setVisibility(View.VISIBLE);
            holder.txt_sign_in_list_year.setText(signIn1.getStime().substring(0, 4));
            holder.txt_sign_in_list_month.setText(signIn1.getStime().substring(5) + "月");
            holder.txt_sign_in_list_count.setText(signIn1.getStime() + " 打卡记录");
        }

        else {
            holder.txt_top_item_monthy.setVisibility(View.GONE);
        }

        
        
        
        return view;

    }

    class ViewHolder {
        TextView des;
        TextView address, txt_item_sgin_remark, txt_sign_in_list_year, txt_sign_in_list_month,
                txt_sign_in_list_count, mWokeState, txt_item_sgin_down_state,
                txt_signin_map_local_des, txt_signin_map_locastate;
        ImageView status, txt_signin_map_local_des_img, sign_work_state1;
        RelativeLayout txt_top_item_monthy, rl_item_sign_month;
        View view_sign_view;
        LinearLayout txt_item_sgin_remark_ly;

    }

    public void updateData(List<SignInVO> list) {

        if (null == dataList) {
            dataList = new ArrayList<SignInVO>();
        }
        if (null == list) {
            return;
        }
        dataList.clear();
        dataList.addAll(list);
        notifyDataSetChanged();
    }

    public void updateDataFrist(List<SignInVO> list) {
        list.addAll(dataList);
        dataList.clear();
        dataList.addAll(list);
        notifyDataSetChanged();
    }

    public void updateDataLast(List<SignInVO> list) {
        dataList.addAll(list);
        notifyDataSetChanged();
    }

}
