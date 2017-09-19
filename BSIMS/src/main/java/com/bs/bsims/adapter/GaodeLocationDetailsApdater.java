
package com.bs.bsims.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.xutils.impl.PhoneInfoUtil;

import java.util.List;

public class GaodeLocationDetailsApdater extends BaseAdapter {

    private Context mContext;
    private List<EmployeeVO> dataList;

    public GaodeLocationDetailsApdater(Context c, List<EmployeeVO> dataList) {

        mContext = c;
        this.dataList = dataList;

    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // ImageView imageView;
        ViewHolder holder;
        View view;
        if (convertView == null) {
            /*
             * imageView = new ImageView(mContext); imageView.setLayoutParams(new
             * GridView.LayoutParams( GridView.LayoutParams.MATCH_PARENT, dipToPx(65)));
             * imageView.setAdjustViewBounds(true);
             * imageView.setScaleType(ImageView.ScaleType.FIT_XY);
             */
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(
                    R.layout.gaode_hoscroll_gridview_detail, null);
            holder.gaode_location_address = (TextView) view
                    .findViewById(R.id.gaode_location_address);
            holder.gaode_location_times = (TextView) view
                    .findViewById(R.id.gaode_location_times);
            holder.icon = (ImageView) view.findViewById(R.id.icon);
            holder.gaode_location_mobile = (TextView) view
                    .findViewById(R.id.gaode_location_mobile);
            holder.icon = (ImageView) view.findViewById(R.id.icon);
            holder.line1 = (TextView) view.findViewById(R.id.line1);
            holder.line2 = (TextView) view.findViewById(R.id.line2);
            view.setTag(holder);

        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();

        }

        holder.gaode_location_address.setText(dataList.get(position).getT_address());
        holder.gaode_location_times.setText(DateUtils.parseDateDayAndHour(dataList.get(position)
                .getT_addtime()));
        String Tm_name = "";
        String Tm_wan = "";
        String Tm_loatype = "";
        if (!dataList.get(position).getT_mobilename().equals(R.string.error_php_interface)) {
            Tm_name = dataList.get(position).getT_mobilename();
        }
        else {
            Tm_name = "";
        }
        if (!dataList.get(position).getT_wantype().equals(R.string.error_php_interface)) {
            Tm_wan = "(" + dataList.get(position).getT_wantype() + ")";
            if(dataList.get(position).getT_wantype().equals(mContext.getString(R.string.error_php_interface))){
                Tm_wan="";
            }
        }
        else {
            Tm_wan = "";
        }
        if (!dataList.get(position).getT_locationtype().equals(R.string.error_php_interface)) {
            Tm_loatype = PhoneInfoUtil.getPhoneNetInfo(Integer.parseInt(dataList.get(position)
                    .getT_locationtype()));
            if (Tm_loatype.equals("后台定位")) {
                Tm_loatype = "";
                Tm_name = "";
                Tm_wan = "";
            }

        }
        else {
            Tm_loatype = "";
        }

        holder.gaode_location_mobile.setText(Tm_name + Tm_wan + " " + Tm_loatype);

        if (position == 0 && dataList.size() > 0) {
            holder.icon.setScaleType(ScaleType.FIT_START);
            holder.icon.setImageResource(R.drawable.gaode_location_start1);
            holder.line1.setVisibility(view.VISIBLE);
            holder.line2.setVisibility(view.VISIBLE);
            holder.line2.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }
        if (position < dataList.size() - 1 && position > 0) {
            holder.icon.setScaleType(ScaleType.FIT_CENTER);
            holder.icon.setImageResource(R.drawable.gaode_direction_todown);
            holder.line1.setVisibility(view.VISIBLE);
            holder.line2.setVisibility(view.GONE);

        }
        if (position == (dataList.size() - 1) && dataList.size() >= 2) {
            // holder.icon.setScaleType(ScaleType.FIT_END);
            holder.icon.setScaleType(ScaleType.FIT_START);
            holder.icon.setImageResource(R.drawable.gaode_location_end1);
            holder.line1.setVisibility(view.GONE);
            holder.line2.setVisibility(view.VISIBLE);
            holder.line2.setBackgroundColor(mContext.getResources().getColor(R.color.blug_bg));
        }

        return view;
    }

    public void setPicture(int position) {
        if (position == 0 && dataList.size() > 0) {

        }

    }

    class ViewHolder {

        TextView gaode_location_address, gaode_location_mobile;
        TextView gaode_location_times;
        TextView line1;
        TextView line2;
        ImageView icon;
    }
}
