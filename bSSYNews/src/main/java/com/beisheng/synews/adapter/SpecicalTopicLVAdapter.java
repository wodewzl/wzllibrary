
package com.beisheng.synews.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beisheng.base.adapter.BSBaseAdapter;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.DateUtils;
import com.beisheng.synews.mode.SpecialTopicVO;
import com.im.zhsy.R;

public class SpecicalTopicLVAdapter extends BSBaseAdapter<SpecialTopicVO> {

    public SpecicalTopicLVAdapter(Context context) {
        super(context);
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (mIsEmpty) {
            return super.getView(position, convertView, parent);
        }

        if (convertView != null && convertView.getTag() == null)
            convertView = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.specical_topic_lv_adapter, null);
            holder.typeNameTv = (TextView) convertView.findViewById(R.id.type_name_tv);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.tileTv = (TextView) convertView.findViewById(R.id.title_tv);
            holder.agreeCommentLayout = (LinearLayout) convertView.findViewById(R.id.agree_comment_layout);
            holder.agreeTv = (TextView) convertView.findViewById(R.id.agree_tv);
            holder.commentTv = (TextView) convertView.findViewById(R.id.comment_tv);
            holder.timeTv = (TextView) convertView.findViewById(R.id.time_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SpecialTopicVO vo = (SpecialTopicVO) mList.get(position);
        mImageLoader.displayImage(vo.getThumb(), holder.img, mOptions);
        holder.typeNameTv.setText(vo.getName());
        holder.typeNameTv.setBackgroundColor(Color.parseColor(vo.getColor()));
        if (vo.getName() != null) {
            if (position == 0) {
                holder.typeNameTv.setVisibility(View.VISIBLE);
            } else if (position != 0 && !vo.getName().equals((mList.get(position - 1).getName()))) {
                holder.typeNameTv.setVisibility(View.VISIBLE);
            } else {
                holder.typeNameTv.setVisibility(View.GONE);
            }
        } else {
            holder.typeNameTv.setVisibility(View.GONE);
        }

        holder.tileTv.setText(vo.getTitle());
        holder.timeTv.setText(DateUtils.parseMDHM(vo.getCreatime()));
        holder.agreeCommentLayout.setBackground(BaseCommonUtils.setBackgroundShap(mContext, 20, R.color.C3, R.color.C3));
        if (vo.getRead() != null) {
            holder.agreeTv.setText(vo.getRead());
            holder.agreeTv.setVisibility(View.VISIBLE);
        } else {
            holder.agreeTv.setVisibility(View.GONE);
        }
        if (vo.getComments() != null) {
            holder.commentTv.setText(vo.getComments());
            holder.commentTv.setVisibility(View.VISIBLE);
        } else {
            holder.commentTv.setVisibility(View.GONE);
        }
        return convertView;
    }

    static class ViewHolder {
        private TextView typeNameTv;
        private TextView tileTv, agreeTv, commentTv, timeTv;
        private ImageView img;
        private LinearLayout agreeCommentLayout;
    }
}
