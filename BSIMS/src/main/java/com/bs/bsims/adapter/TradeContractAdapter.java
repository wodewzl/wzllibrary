
package com.bs.bsims.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.utils.DateUtils;

import java.util.List;

public class TradeContractAdapter extends BaseAdapter {

    private Context mContext;
    private List<EmployeeVO> dataList;

    public TradeContractAdapter(Context c, List<EmployeeVO> dataList) {

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
            view.setTag(holder);

        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();

        }

        holder.gaode_location_address.setText(dataList.get(position).getT_address());
        holder.gaode_location_times.setText(DateUtils.parseDateDayAndHour(dataList.get(position)
                .getT_addtime()));

        return view;
    }

    class ViewHolder {

        TextView gaode_location_address;
        TextView gaode_location_times;
    }

}
