
package com.bs.bsims.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.DanganBasicinfo;
import com.bs.bsims.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class DanganBasicinfolearnexpapdater extends BaseAdapter {
    /**
     * 个人档案页面的下的教育经历
     */

    private Context mContext;
    public List<DanganBasicinfo> eduexp;

    public DanganBasicinfolearnexpapdater(Context context) {
        mContext = context;
        eduexp = new ArrayList<DanganBasicinfo>();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return eduexp.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return eduexp.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext,
                    R.layout.da_basicinfolearnexpadapter, null);
            holder.basicinfoschool = (TextView) convertView
                    .findViewById(R.id.basicinfoschool);
            holder.basicinfoeducation = (TextView) convertView
                    .findViewById(R.id.basicinfoeducation);
            holder.basicinfoendtime = (TextView) convertView
                    .findViewById(R.id.basicinfoendtime);
            holder.imageView = (ImageView) convertView
                    .findViewById(R.id.footer_type);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
      
        holder.basicinfoschool.setText(eduexp
                .get(position).getSchool());
        holder.basicinfoeducation.setText(eduexp
                .get(position).getEducation() + "·" + eduexp.get(position).getSpecialty());
        holder.basicinfoendtime.setText(DateUtils.parseYearMonth(eduexp.get(position).getStarttime()).split("-")[0] + "年" + DateUtils.parseYearMonth(eduexp.get(position).getStarttime()).split("-")[1] + "月" + "--"
                + DateUtils.parseYearMonth(eduexp.get(position).getEndtime()).split("-")[0] + "年"+DateUtils.parseYearMonth(eduexp.get(position).getEndtime()).split("-")[1] + "月");
        return convertView;
    }

    static class ViewHolder {
        private TextView basicinfoschool;
        private TextView basicinfoeducation;
        private TextView basicinfoendtime;
        private ImageView imageView;
    }

}
