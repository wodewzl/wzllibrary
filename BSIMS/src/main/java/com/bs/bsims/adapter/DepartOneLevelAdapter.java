
package com.bs.bsims.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.DepartmentAndEmployeeVO;

public class DepartOneLevelAdapter extends BSBaseAdapter<DepartmentAndEmployeeVO> {
    private Context mContext;

    public DepartOneLevelAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (mIsEmpty) {
            return super.getView(position, convertView, parent);
        }

        if (convertView != null && convertView.getTag() == null)
            convertView = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.crm_lv_options, null);
            holder.mTileTv = (TextView) convertView.findViewById(R.id.title_tv);
            holder.mStatusImg = (ImageView) convertView.findViewById(R.id.status_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DepartmentAndEmployeeVO vo = mList.get(position);
        holder.mTileTv.setText(vo.getDname());

        if (vo.isSelected()) {
            holder.mStatusImg.setImageResource(R.drawable.common_ic_selected);
        } else {
            holder.mStatusImg.setImageResource(R.drawable.common_ic_unselect);
        }

        return convertView;
    }

    static class ViewHolder {
        private TextView mTileTv;
        private ImageView mStatusImg;
    }

}
