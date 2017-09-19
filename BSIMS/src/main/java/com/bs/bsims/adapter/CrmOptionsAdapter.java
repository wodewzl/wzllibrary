
package com.bs.bsims.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.CrmOptionsListVO;

public class CrmOptionsAdapter extends BSBaseAdapter<CrmOptionsListVO> {
    private Context mContext;
    private int mStatus;// 1代表选择框在左边

    public CrmOptionsAdapter(Context context) {
        super(context);
        mContext = context;
    }

    public CrmOptionsAdapter(Context context, int status) {
        super(context);
        mContext = context;
        this.mStatus = status;
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
            holder.mStatusImgLeft = (ImageView) convertView.findViewById(R.id.status_img_left);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CrmOptionsListVO vo = mList.get(position);

        if (vo.getLname() != null)
            holder.mTileTv.setText(vo.getLname());
        else
            holder.mTileTv.setText(vo.getName());

        if (mStatus == 1) {
            holder.mStatusImgLeft.setVisibility(View.VISIBLE);
            holder.mStatusImg.setVisibility(View.GONE);
        } else {
            holder.mStatusImgLeft.setVisibility(View.GONE);
            holder.mStatusImg.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    static class ViewHolder {
        private TextView mTileTv;
        private ImageView mStatusImg, mStatusImgLeft;
    }

}
