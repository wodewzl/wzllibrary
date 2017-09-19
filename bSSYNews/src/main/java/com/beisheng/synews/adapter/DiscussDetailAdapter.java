
package com.beisheng.synews.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beisheng.base.adapter.BSBaseAdapter;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.DateUtils;
import com.beisheng.base.utils.Options;
import com.beisheng.base.view.BSCircleImageView;
import com.beisheng.synews.activity.DisscussDetailActivity;
import com.beisheng.synews.application.AppApplication;
import com.beisheng.synews.mode.NewsVO;
import com.beisheng.synews.view.BSPopwindowEditText;
import com.im.zhsy.R;

import java.util.List;

public class DiscussDetailAdapter extends BSBaseAdapter<NewsVO> {
    private int currentPosition = -1;
    private BSPopwindowEditText mPopEditText;
    private NewsVO mParentVO;

    public DiscussDetailAdapter(Context context, NewsVO vo, List<NewsVO> list) {
        super(context);
        this.mList = list;
        this.mParentVO = vo;
    }

    @Override
    public int getCount() {
        if (mList.size() == 0) {
            return 0;
        } else {
            return mList.size();
        }
    }

    public void updateDataFrist(List<NewsVO> list) {
        if (list == null)
            return;

        list.addAll(mList);
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
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
            convertView = View.inflate(mContext, R.layout.disscuss_detail_adapter, null);
            holder.parentLayout = (LinearLayout) convertView.findViewById(R.id.parent_layout);
            holder.nameTv = (TextView) convertView.findViewById(R.id.name_tv);
            holder.headIcon = (BSCircleImageView) convertView.findViewById(R.id.head_icon);
            holder.timeTv = (TextView) convertView.findViewById(R.id.time_tv);
            holder.detailTv = (TextView) convertView.findViewById(R.id.detail_tv);
            holder.childLayout = (LinearLayout) convertView.findViewById(R.id.child_layout);
            holder.child01NameTv = (TextView) convertView.findViewById(R.id.child01_name_tv);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final NewsVO vo = (NewsVO) mList.get(position);
        if (position == 0) {
            holder.parentLayout.setVisibility(View.VISIBLE);
            holder.nameTv.setText(mParentVO.getNickname());
            holder.detailTv.setText(mParentVO.getContent());
            holder.timeTv.setText(DateUtils.parseMDHM(mParentVO.getCreatime()));
            if (mParentVO.getUserid().equals(AppApplication.getInstance().getUid())) {
                mImageLoader.displayImage(AppApplication.getInstance().getUserInfoVO().getHeadpic(), holder.headIcon, Options.getOptionsHead(R.drawable.user_icon));
            } else {
                mImageLoader.displayImage(mParentVO.getHeadpic(), holder.headIcon, Options.getOptionsHead(R.drawable.user_icon));
            }
        } else {
            holder.parentLayout.setVisibility(View.GONE);
        }

        holder.child01NameTv.setVisibility(View.VISIBLE);
        holder.child01NameTv.setText(vo.getNickname());
        String name = "";
        if (vo.getNickname().equals(vo.getReplyname()) || mParentVO.getNickname().equals(vo.getReplyname())) {
            name = vo.getNickname() + "：";
        } else {
            name = vo.getNickname() + " 回复# " + vo.getReplyname() + "：";
        }
        BaseCommonUtils.setTextTwoBefore(mContext, holder.child01NameTv, name, vo.getContent(), R.color.sy_title_color, 1.0f);

        holder.child01NameTv.setOnClickListener(new ChildItemClick(vo));

        return convertView;
    }

    static class ViewHolder {
        private BSCircleImageView headIcon;
        private TextView nameTv, timeTv, detailTv;
        private LinearLayout childLayout, parentLayout;
        private TextView child01NameTv, child02NameTv, childMoreTv;
    }

    class ChildItemClick implements OnClickListener {
        private NewsVO mNewsVO;

        public ChildItemClick(NewsVO vo) {
            mNewsVO = vo;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.child01_name_tv:

                    if (mContext instanceof DisscussDetailActivity) {
                        DisscussDetailActivity activity = (DisscussDetailActivity) mContext;
                        activity.disscussChild(mNewsVO);
                    }
                    break;
                default:
                    break;
            }
        }
    }

}
