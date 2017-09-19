
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
import com.beisheng.synews.activity.DisscussActivity;
import com.beisheng.synews.application.AppApplication;
import com.beisheng.synews.mode.NewsVO;
import com.im.zhsy.R;

public class DiscussAdapter extends BSBaseAdapter<NewsVO> {
    public String isShow = "0";// 1为展示一级，不现实2及

    public DiscussAdapter(Context context) {
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
            convertView = View.inflate(mContext, R.layout.disscuss_adapter, null);
            holder.nameTv = (TextView) convertView.findViewById(R.id.name_tv);
            holder.headIcon = (BSCircleImageView) convertView.findViewById(R.id.head_icon);
            holder.timeTv = (TextView) convertView.findViewById(R.id.time_tv);
            holder.detailTv = (TextView) convertView.findViewById(R.id.detail_tv);
            holder.childLayout = (LinearLayout) convertView.findViewById(R.id.child_layout);
            holder.child01NameTv = (TextView) convertView.findViewById(R.id.child01_name_tv);
            holder.child02NameTv = (TextView) convertView.findViewById(R.id.child02_name_tv);
            holder.childMoreTv = (TextView) convertView.findViewById(R.id.child_more_tv);
            holder.parentMoreTv = (TextView) convertView.findViewById(R.id.parent_more_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final NewsVO vo = (NewsVO) mList.get(position);
        holder.nameTv.setText(vo.getNickname());
        if (vo.getReplyname() != null && !"".equals(vo.getReplyname())) {
            // BaseCommonUtils.setTextTwoBefore(mContext, holder.detailTv, "回复#" + vo.getReplyname()
            // + "：", vo.getContent(), R.color.sy_title_color, 1.0f);
            holder.detailTv.setText(vo.getContent());
        } else {
            holder.detailTv.setText(vo.getContent());
        }
        holder.timeTv.setText(DateUtils.parseMDHM(vo.getCreatime()));
        if (vo.getUserid().equals(AppApplication.getInstance().getUid())) {
            mImageLoader.displayImage(AppApplication.getInstance().getUserInfoVO().getHeadpic(), holder.headIcon, Options.getOptionsHead(R.drawable.user_icon));
        } else {
            mImageLoader.displayImage(vo.getHeadpic(), holder.headIcon, Options.getOptionsHead(R.drawable.user_icon));
        }

        if (BaseCommonUtils.parseInt(vo.getReplyNum()) > 0 && "1".equals(getIsShow())) {
            holder.parentMoreTv.setVisibility(View.VISIBLE);
            holder.childLayout.setVisibility(View.GONE);
            holder.parentMoreTv.setText("查看共" + vo.getReplyNum() + "条回复 >");
        } else {
            holder.parentMoreTv.setVisibility(View.GONE);
            holder.childLayout.setVisibility(View.VISIBLE);
        }

        if (vo.getChildren() != null) {
            if (vo.getChildren().size() >= 1) {
                holder.child01NameTv.setVisibility(View.VISIBLE);
                holder.child01NameTv.setText(vo.getChildren().get(0).getNickname());
                String name = "";
                if (vo.getChildren().get(0).getNickname().equals(vo.getChildren().get(0).getReplyname()) || vo.getNickname().equals(vo.getChildren().get(0).getReplyname())) {
                    name = vo.getChildren().get(0).getNickname() + "：";
                } else {
                    name = vo.getChildren().get(0).getNickname() + " 回复# " + vo.getChildren().get(0).getReplyname() + "：";
                }
                BaseCommonUtils.setTextTwoBefore(mContext, holder.child01NameTv, name, vo.getChildren().get(0).getContent(), R.color.sy_title_color, 1.0f);
                holder.child01NameTv.setOnClickListener(new ChildItemClick(vo.getChildren().get(0)));
            } else {
                holder.child01NameTv.setVisibility(View.GONE);
            }

            if (vo.getChildren().size() >= 2) {
                holder.child02NameTv.setVisibility(View.VISIBLE);
                String name = "";
                if (vo.getChildren().get(1).getNickname().equals(vo.getChildren().get(1).getReplyname()) || vo.getNickname().equals(vo.getChildren().get(1).getReplyname())) {
                    name = vo.getChildren().get(1).getNickname() + "：";
                } else {
                    name = vo.getChildren().get(1).getNickname() + " 回复# " + vo.getChildren().get(1).getReplyname() + "：";
                }
                BaseCommonUtils.setTextTwoBefore(mContext, holder.child02NameTv, name, vo.getChildren().get(1).getContent(), R.color.sy_title_color, 1.0f);
                holder.child02NameTv.setOnClickListener(new ChildItemClick(vo.getChildren().get(1)));
            } else {
                holder.child02NameTv.setVisibility(View.GONE);
            }

            if (vo.getChildren().size() > 2) {
                holder.childMoreTv.setVisibility(View.VISIBLE);
                holder.childMoreTv.setText("查看共" + vo.getReplyNum() + "条回复 >");

            } else {
                holder.childMoreTv.setVisibility(View.GONE);
            }

        } else {
            holder.childLayout.setVisibility(View.GONE);
        }
        return convertView;
    }

    static class ViewHolder {
        private BSCircleImageView headIcon;
        private TextView nameTv, timeTv, detailTv;
        private LinearLayout childLayout;
        private TextView child01NameTv, child02NameTv, parentMoreTv, childMoreTv;
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
                case R.id.child02_name_tv:
                    if (mContext instanceof DisscussActivity) {
                        DisscussActivity activity = (DisscussActivity) mContext;
                        activity.disscussChild(mNewsVO);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

}
