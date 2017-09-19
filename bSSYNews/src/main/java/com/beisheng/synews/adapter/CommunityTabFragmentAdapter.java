
package com.beisheng.synews.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beisheng.base.adapter.BSBaseAdapter;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.DateUtils;
import com.beisheng.base.utils.Options;
import com.beisheng.base.view.BSCircleImageView;
import com.beisheng.synews.mode.CommunityVO;
import com.im.zhsy.R;

public class CommunityTabFragmentAdapter extends BSBaseAdapter<CommunityVO> {

    public CommunityTabFragmentAdapter(Context context) {
        super(context);
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
            convertView = View.inflate(mContext, R.layout.community_fragment_adapter, null);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.tileTv = (TextView) convertView.findViewById(R.id.title_tv);
            holder.contentTv = (TextView) convertView.findViewById(R.id.content_tv);
            holder.agreeCommentLayout = (LinearLayout) convertView.findViewById(R.id.agree_comment_layout);
            holder.agreeTv = (TextView) convertView.findViewById(R.id.agree_tv);
            holder.commentTv = (TextView) convertView.findViewById(R.id.comment_tv);
            holder.timeTv = (TextView) convertView.findViewById(R.id.time_tv);
            holder.smallImgLayout = (LinearLayout) convertView.findViewById(R.id.small_img_layout);
            holder.headLayout = (RelativeLayout) convertView.findViewById(R.id.head_layout);
            holder.headIcon = (BSCircleImageView) convertView.findViewById(R.id.head_icon);
            holder.nameTv = (TextView) convertView.findViewById(R.id.name_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CommunityVO vo = mList.get(position);
        if (vo.getImage() != null && "1".equals(vo.getIsHead())) {
            mImageLoader.displayImage(vo.getImage(), holder.headIcon, Options.getOptionsHead(R.drawable.user_icon));
            holder.smallImgLayout.setVisibility(View.GONE);
            holder.headLayout.setVisibility(View.VISIBLE);
            holder.nameTv.setVisibility(View.VISIBLE);
            holder.nameTv.setText(vo.getAuthor());
        } else if (vo.getImage() != null && vo.getIsHead() == null) {
            mImageLoader.displayImage(vo.getImage(), holder.img, mOptions);
            holder.smallImgLayout.setVisibility(View.VISIBLE);
            holder.headLayout.setVisibility(View.GONE);
            holder.nameTv.setVisibility(View.GONE);
        } else {
            holder.smallImgLayout.setVisibility(View.GONE);
            holder.headLayout.setVisibility(View.GONE);
            holder.nameTv.setVisibility(View.GONE);
        }

        holder.tileTv.setText(vo.getTitle());
        if (vo.getDescrip() != null) {
            holder.contentTv.setText(vo.getDescrip());
            holder.contentTv.setVisibility(View.VISIBLE);
        } else {
            holder.contentTv.setVisibility(View.GONE);
        }

        if (vo.getCreatime() != null) {
            holder.timeTv.setText(DateUtils.parseMDHM(vo.getCreatime()));
        }
        holder.agreeCommentLayout.setBackground(BaseCommonUtils.setBackgroundShap(mContext, 20, R.color.C3, R.color.C3));
        if (vo.getRead() != null && vo.getComments() != null) {
            holder.agreeTv.setText(vo.getRead());
            holder.commentTv.setText(vo.getComments());
            holder.agreeCommentLayout.setVisibility(View.VISIBLE);
        } else {
            holder.agreeCommentLayout.setVisibility(View.GONE);
        }

        return convertView;
    }

    static class ViewHolder {
        private TextView tileTv, agreeTv, commentTv, timeTv, contentTv, nameTv;
        private ImageView img;
        private LinearLayout agreeCommentLayout, smallImgLayout;
        private RelativeLayout headLayout;
        private BSCircleImageView headIcon;
    }

}
