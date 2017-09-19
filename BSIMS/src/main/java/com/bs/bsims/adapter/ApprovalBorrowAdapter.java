
package com.bs.bsims.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.ApprovalBorrowVO;
import com.bs.bsims.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class ApprovalBorrowAdapter extends BaseAdapter {
    private Activity mContext;
    public List<ApprovalBorrowVO> mList;

    public ApprovalBorrowAdapter(Activity context) {
        this.mContext = context;
        mList = new ArrayList<ApprovalBorrowVO>();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.lv_approval_borrow_item, null);
            holder.mTileTv = (TextView) convertView.findViewById(R.id.title_tv);
            holder.mStateTv = (TextView) convertView.findViewById(R.id.state_tv);
            holder.mTypeTv = (TextView) convertView.findViewById(R.id.type_tv);
            holder.mTimeTv = (TextView) convertView.findViewById(R.id.time_tv);
            holder.mSelectImg = (ImageView) convertView.findViewById(R.id.select_img);
            holder.mItemLayout = (LinearLayout) convertView.findViewById(R.id.item_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ApprovalBorrowVO vo = mList.get(position);
        holder.mTileTv.setText(vo.getReason());
        holder.mTypeTv.setText(vo.getTotalprice() + "元");
        holder.mTimeTv.setText(DateUtils.parseDateDay(vo.getAtime()));
        holder.mItemLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.putExtra("fdid", mList.get(position).getFdid());
                intent.putExtra("money", mList.get(position).getTotalprice());
                intent.putExtra("borrow_title", mList.get(position).getReason());
                intent.putExtra("time", mList.get(position).getAddtime());
                mContext.setResult(0x000003, intent);
                holder.mSelectImg.setImageResource(R.drawable.common_ic_selected);
                mContext.finish();
            }
        });
        return convertView;
    }

    static class ViewHolder {
        private TextView mTileTv, mStateTv, mTypeTv, mTimeTv;
        private ImageView mSelectImg;
        private LinearLayout mItemLayout;
    }

    public void updateData(List<ApprovalBorrowVO> list) {
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataFrist(List<ApprovalBorrowVO> list) {
        list.addAll(mList);
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataLast(List<ApprovalBorrowVO> list) {
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

}
