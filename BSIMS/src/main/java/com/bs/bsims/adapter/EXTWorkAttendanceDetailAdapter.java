
package com.bs.bsims.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.EXTWorkAttendanceEveryoneDetailActivity;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.view.BSCircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author peck
 * @Description:
 * @date 2015-5-16 下午4:22:22
 * @email 971371860@qq.com
 * @version V1.0
 */

public class EXTWorkAttendanceDetailAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mList;
    private ImageLoader mImageLoader;
    private Map<String, String> valueMap;
    private DisplayImageOptions mOptions;
    private Map<String, List<String>> valueEveryOneMap;

    public EXTWorkAttendanceDetailAdapter(Context context) {
        this.mContext = context;
        this.mList = new LinkedList<String>();
        mImageLoader = ImageLoader.getInstance();
        mOptions = CommonUtils.initImageLoaderOptions();
        valueMap = new HashMap<String, String>();
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext,
                    R.layout.item_work_attendance_detail, null);
            holder.mName = (TextView) convertView
                    .findViewById(R.id.tem_work_attendance_detail_info);
            holder.mDName = (TextView) convertView
                    .findViewById(R.id.tem_work_attendance_detail_count);
            holder.mIcon = (BSCircleImageView) convertView
                    .findViewById(R.id.item_work_attendance_detail_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        convertView.setVisibility(View.VISIBLE);
        String info = mList.get(position);
        holder.keyV = info;
        // if (Integer.parseInt(joVo.getCommentCount()) > 0)
        // holder.mIcon
        // .setBadgeNumber(Integer.parseInt(joVo.getCommentCount()));
        // mImageLoader.displayImage(joVo.getHeadpic(), holder.mIcon, mOptions);
        //
        // holder.mName.setText(joVo.getFullname());
        // holder.mDName.setText(joVo.getDname());

        // array('1'=>'事假','2'=>'病假','3'=>'(陪)产假','4'=>'公休假','5'=>'调休假','6'=>'婚假','7'=>'丧假');

        String showInfo = "";
        String showInfoNumEnd = "";
        String showInfoNumEndC = "次";
        String showInfoNumEndD = "天";
        String showInfoNum = valueMap.get(info);
        int imgId = R.drawable.nsm_attendance_pig;
        if ("absence_num".equalsIgnoreCase(info)) {
            showInfo = "缺卡";
            showInfoNumEnd = showInfoNumEndC;
            holder.mDName.setText(showInfoNum + showInfoNumEnd);
            imgId = R.drawable.nsm_attendance_punch;
        } else if ("belate_num".equalsIgnoreCase(info)) {
            showInfo = "迟到";
            showInfoNumEnd = showInfoNumEndC;
            holder.mDName.setText(showInfoNum + showInfoNumEnd);
            imgId = R.drawable.nsm_attendance_late;
        } else if ("leavearly_num".equalsIgnoreCase(info)) {
            showInfo = "早退";
            showInfoNumEnd = showInfoNumEndC;
            imgId = R.drawable.start_time;
            holder.mDName.setText(showInfoNum + showInfoNumEnd);
        } else if ("nowrlog_num".equalsIgnoreCase(info)) {
            showInfo = "未写日志";
            showInfoNumEnd = showInfoNumEndC;
            imgId = R.drawable.nsm_attendance_log;
            holder.mDName.setText(showInfoNum + showInfoNumEnd);
        } else if ("1".equalsIgnoreCase(info)) {
            showInfo = "事假";
            showInfoNumEnd = showInfoNumEndD;
            imgId = R.drawable.nsm_attendance_vacate_01;
            if (valueEveryOneMap.containsKey("1")) {
                holder.mDName.setText(valueEveryOneMap.get("1").size() + showInfoNumEndC);
            } else {
                holder.mDName.setText(0 + showInfoNumEndC);
            }
            // showInfoNum = showInfoNum + "天";
        } else if ("2".equalsIgnoreCase(info)) {
            showInfo = "病假";
            showInfoNumEnd = showInfoNumEndC;
            imgId = R.drawable.nsm_attendance_vacate_02;
            if (valueEveryOneMap.containsKey("2")) {
                holder.mDName.setText(valueEveryOneMap.get("2").size() + showInfoNumEndC);
            } else {
                holder.mDName.setText(0 + showInfoNumEndC);
            }

        } else if ("3".equalsIgnoreCase(info)) {
            showInfo = "陪产假";
            showInfoNumEnd = showInfoNumEndD;
            imgId = R.drawable.nsm_attendance_vacate_03;
            if (valueEveryOneMap.containsKey("3")) {
                holder.mDName.setText(valueEveryOneMap.get("3").size() + showInfoNumEndC);
            } else {
                holder.mDName.setText(0 + showInfoNumEndC);
            }

        } else if ("4".equalsIgnoreCase(info)) {
            showInfo = "公休";
            showInfoNumEnd = showInfoNumEndD;
            imgId = R.drawable.nsm_attendance_vacate_04;
            if (valueEveryOneMap.containsKey("4")) {
                holder.mDName.setText(valueEveryOneMap.get("4").size() + showInfoNumEndC);
            } else {
                holder.mDName.setText(0 + showInfoNumEndC);
            }

        } else if ("5".equalsIgnoreCase(info)) {
            showInfo = "调休";
            showInfoNumEnd = showInfoNumEndD;
            imgId = R.drawable.nsm_attendance_vacate_05;
            if (valueEveryOneMap.containsKey("5")) {
                holder.mDName.setText(valueEveryOneMap.get("5").size() + showInfoNumEndC);
            } else {
                holder.mDName.setText(0 + showInfoNumEndC);
            }

        } else if ("6".equalsIgnoreCase(info)) {
            showInfo = "婚假";
            showInfoNumEnd = showInfoNumEndD;
            imgId = R.drawable.nsm_attendance_vacate_03;
            if (valueEveryOneMap.containsKey("6")) {
                holder.mDName.setText(valueEveryOneMap.get("6").size() + showInfoNumEndC);
            } else {
                holder.mDName.setText(0 + showInfoNumEndC);
            }

        } else if ("7".equalsIgnoreCase(info)) {
            showInfo = "丧假";
            showInfoNumEnd = showInfoNumEndD;
            imgId = R.drawable.nsm_attendance_vacate_07;
            if (valueEveryOneMap.containsKey("7")) {
                holder.mDName.setText(valueEveryOneMap.get("7").size() + showInfoNumEndC);
            } else {
                holder.mDName.setText(0 + showInfoNumEndC);
            }

        }
        // holder.mIcon.setImageResource(imgId);
        holder.mIcon.setBackgroundResource(imgId);
        holder.mName.setText(showInfo);

        // Integer.parseInt(showInfoNum);
        // if (!"0".equalsIgnoreCase(showInfoNum)) {

        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub

                ViewHolder holder = (ViewHolder) view.getTag();
                if ("0".equalsIgnoreCase(valueMap.get(holder.keyV))) {
                    return;
                }
                ArrayList<String> showInfoNum = (ArrayList<String>) valueEveryOneMap
                        .get(holder.keyV);
                if (null == showInfoNum || showInfoNum.isEmpty()) {
                    return;
                }

                Intent intent = new Intent();
                intent.setClass(mContext,
                        EXTWorkAttendanceEveryoneDetailActivity.class);
                intent.putExtra("userid", BSApplication.getInstance()
                        .getUserId());

                intent.putExtra("detailList", showInfoNum);
                // intent.putExtra("topTitleType", holder.keyV);
                intent.putExtra("topTitleType", holder.mName.getText());
                intent.putExtra("topTitleTypeInfo", valueMap.get(holder.keyV));
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        private TextView mName, mDName;
        private BSCircleImageView mIcon;
        private String keyV;
    }

    public void updateData(List<String> list, Map<String, String> valueMap) {
        mList.clear();
        mList.addAll(list);
        this.valueMap = valueMap;
        this.notifyDataSetChanged();
    }

    public void updateData(List<String> list, Map<String, String> valueMap,
            Map<String, List<String>> valueEveryOneMap) {
        for (int i = 0; i < list.size(); i++) {
            String info = list.get(i);
            String num = valueMap.get(info);
            if (num.equals("0")) {
                list.remove(i);
            }
        }

        mList.clear();
        mList.addAll(list);
        this.valueMap = valueMap;
        this.valueEveryOneMap = valueEveryOneMap;
        this.notifyDataSetChanged();
    }

}
