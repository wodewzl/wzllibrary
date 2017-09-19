
package com.beisheng.synews.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beisheng.base.adapter.BSBaseAdapter;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.DateUtils;
import com.beisheng.synews.mode.NewsVO;
import com.im.zhsy.R;

public class HomeNewsPhotoAdapter extends BSBaseAdapter<NewsVO> {

    public HomeNewsPhotoAdapter(Context context) {
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
            convertView = View.inflate(mContext, R.layout.home_news_photo_adapter, null);
            holder.titleTv = (TextView) convertView.findViewById(R.id.title_tv);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.timeTv = (TextView) convertView.findViewById(R.id.time_tv);
            holder.readTv = (TextView) convertView.findViewById(R.id.read_tv);
            holder.commentTv = (TextView) convertView.findViewById(R.id.comment_tv);
            holder.readCommnetLayout = (LinearLayout) convertView.findViewById(R.id.read_comment_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        NewsVO vo = (NewsVO) mList.get(position);
        mImageLoader.displayImage(vo.getThumb(), holder.img, mOptions);
        holder.titleTv.setText(vo.getTitle());

        holder.timeTv.setText(DateUtils.parseMDHM(vo.getCreatime()));
        holder.readCommnetLayout.setBackground(BaseCommonUtils.setBackgroundShap(mContext, 20, R.color.C3, R.color.C3));
        if (vo.getRead() != null) {
            holder.readTv.setText(vo.getRead());
            holder.readTv.setVisibility(View.VISIBLE);
        } else {
            holder.readTv.setVisibility(View.GONE);
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
        private ImageView img;
        private TextView timeTv, titleTv, readTv, commentTv;
        private LinearLayout readCommnetLayout;
    }
}
