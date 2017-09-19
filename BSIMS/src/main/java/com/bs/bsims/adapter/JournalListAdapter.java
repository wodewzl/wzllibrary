
package com.bs.bsims.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.JournalListVO1;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.view.BSCircleImageView;
import com.bs.bsims.view.PinnedSectionListView.PinnedSectionListAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JournalListAdapter extends BSBaseAdapter<JournalListVO1> implements PinnedSectionListAdapter {
    public List<Integer> mPinnedList = new ArrayList<Integer>();

    public JournalListAdapter(Context context) {
        super(context);
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (mIsEmpty) {
            View view = super.getView(position, convertView, parent);
            return view;
        }

        if (convertView != null && convertView.getTag() == null)
            convertView = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.journal_list_adapter, null);
            holder.timeTv = (TextView) convertView.findViewById(R.id.time_tv);
            holder.nameTv = (TextView) convertView.findViewById(R.id.name_tv);
            holder.departTv = (TextView) convertView.findViewById(R.id.depart_tv);
            holder.headIcon = (BSCircleImageView) convertView.findViewById(R.id.head_icon);
            holder.typeTv = (TextView) convertView.findViewById(R.id.type_tv);
            holder.todayTv = (TextView) convertView.findViewById(R.id.today_tv);
            holder.tomrrowTv = (TextView) convertView.findViewById(R.id.tomrrow_tv);
            holder.publishTimeTv = (TextView) convertView.findViewById(R.id.publish_time_tv);
            holder.replayTv = (TextView) convertView.findViewById(R.id.replay_tv);
            holder.contentLayout = (LinearLayout) convertView.findViewById(R.id.content_layout);
            holder.shengLue = (TextView) convertView.findViewById(R.id.sheng_lue);
            holder.imgAgree = (ImageView) convertView.findViewById(R.id.img_agree);
            holder.aggreeLayout = (LinearLayout) convertView.findViewById(R.id.aggree_layout);
            holder.agreeTv = (TextView) convertView.findViewById(R.id.praise_tv);
            holder.favorImg = (ImageView) convertView.findViewById(R.id.favor_img);
            holder.title01Tv = (TextView) convertView.findViewById(R.id.title01);
            holder.title02Tv = (TextView) convertView.findViewById(R.id.title02);
            holder.title03Tv = (TextView) convertView.findViewById(R.id.title03);
            holder.title04Tv = (TextView) convertView.findViewById(R.id.title04);
            holder.finishTv = (TextView) convertView.findViewById(R.id.finish_tv);
            holder.experienceTv = (TextView) convertView.findViewById(R.id.experience_tv);
            holder.frameLayout = (FrameLayout) convertView.findViewById(R.id.frame_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (this.getItemViewType(position) == -10) {
            holder.timeTv.setVisibility(View.VISIBLE);
            holder.timeTv.setText(mList.get(mPinnedList.get(position + 1)).getDate());
            holder.frameLayout.setVisibility(View.GONE);
        } else {
            holder.timeTv.setVisibility(View.GONE);
            holder.frameLayout.setVisibility(View.VISIBLE);
            final JournalListVO1 vo = mList.get(mPinnedList.get(position));

            if ("1".equals(vo.getIsfavor()))
                holder.favorImg.setVisibility(View.VISIBLE);
            else
                holder.favorImg.setVisibility(View.GONE);
            holder.nameTv.setText(vo.getFullname());
            holder.departTv.setText(vo.getDname() + " / " + vo.getPositionname());
            mImageLoader.displayImage(vo.getHeadpic(), holder.headIcon, mOptions);
            holder.typeTv.setText(vo.getTypename());
            if ("0".equals(vo.getType())) {
                holder.typeTv.setBackground(CommonUtils.setBackgroundShap(mContext, 5, "#6AC2F8", "#6AC2F8"));
                holder.title01Tv.setText("今日工作总结");
                holder.title02Tv.setText("明日工作计划");
            } else if ("1".equals(vo.getType())) {
                holder.typeTv.setBackground(CommonUtils.setBackgroundShap(mContext, 5, "#72D1AF", "#72D1AF"));
                holder.title01Tv.setText("本周工作总结");
                holder.title02Tv.setText("下周工作计划");
            } else {
                holder.typeTv.setBackground(CommonUtils.setBackgroundShap(mContext, 5, "#B78BDA", "#B78BDA"));
                holder.title01Tv.setText("本月工作总结");
                holder.title02Tv.setText("下月工作计划");
            }

            holder.todayTv.setText(vo.getContent1());
            holder.tomrrowTv.setText(vo.getContent4());
            if (vo.getContent2() != null && vo.getContent2().trim().length() != 0) {
                holder.finishTv.setText(vo.getContent2());
                holder.finishTv.setVisibility(View.VISIBLE);
                holder.title03Tv.setVisibility(View.VISIBLE);
            } else {
                holder.finishTv.setVisibility(View.GONE);
                holder.finishTv.setText("");
                holder.title03Tv.setVisibility(View.GONE);
            }
            if (vo.getContent3() != null && vo.getContent3().trim().length() != 0) {
                holder.experienceTv.setText(vo.getContent3());
                holder.experienceTv.setVisibility(View.VISIBLE);
                holder.title04Tv.setVisibility(View.VISIBLE);
            } else {
                holder.experienceTv.setVisibility(View.GONE);
                holder.experienceTv.setText("");
                holder.title04Tv.setVisibility(View.GONE);
            }

            holder.publishTimeTv.setText(vo.getDate() + " " + DateUtils.parseHour(vo.getTime()));
            holder.replayTv.setText(vo.getCommentCount());
            holder.agreeTv.setText(vo.getPraise());
            if ("1".equals(vo.getIspraised())) {
                holder.imgAgree.setImageResource(R.drawable.agree_select);
            } else {
                holder.imgAgree.setImageResource(R.drawable.agree);
            }

            if ("0".equals(vo.getIsread())) {
                holder.headIcon.setIsread("0");
            } else {
                holder.headIcon.setIsread("1");
            }

            holder.shengLue.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    vo.setAll(true);
                    notifyDataSetChanged();
                }
            });

            holder.aggreeLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Animation scaleAnimation = new ScaleAnimation(1.0f, 2.0f, 1.0f, 2.0f);
                    if ("1".equals(mList.get(mPinnedList.get(position)).getIspraised())) {
                        CustomToast.showLongToast(mContext, "已点过赞");
                        return;
                    }
                    holder.imgAgree.setImageResource(R.drawable.agree_select);
                    // 设置动画时间
                    scaleAnimation.setDuration(500);
                    holder.imgAgree.setAnimation(scaleAnimation);
                    holder.imgAgree.startAnimation(scaleAnimation);
                    int praise = Integer.parseInt(mList.get(mPinnedList.get(position)).getPraise()) + 1;
                    mList.get(mPinnedList.get(position)).setPraise(praise + "");
                    mList.get(mPinnedList.get(position)).setIspraised("1");
                    commit(Constant.JOURNAL_LIST_ITEM_AGREE, mList.get(mPinnedList.get(position)), 1);
                    notifyDataSetChanged();
                }
            });
            convertView.post(new Runnable() {
                @Override
                public void run() {
                    if (!vo.isAll() && (holder.todayTv.getLineCount() + holder.finishTv.getLineCount() + holder.experienceTv.getLineCount() + holder.tomrrowTv.getLineCount()) > 12) {
                        int hight = 0;
                        if (holder.finishTv.getVisibility() == View.VISIBLE) {
                            hight += CommonUtils.dip2px(mContext, 13);
                        }
                        if (holder.experienceTv.getVisibility() == View.VISIBLE) {
                            hight += CommonUtils.dip2px(mContext, 13);
                        }
                        if (holder.todayTv.getLineCount() + holder.finishTv.getLineCount() + holder.experienceTv.getLineCount() < 12) {
                            hight += CommonUtils.dip2px(mContext, 13);
                        }
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, CommonUtils.dip2px(mContext, 320) + hight);
                        params.topMargin = CommonUtils.dip2px(mContext, 15);
                        holder.contentLayout.setOrientation(LinearLayout.VERTICAL);
                        holder.contentLayout.setLayoutParams(params);
                        holder.shengLue.setVisibility(View.VISIBLE);
                    } else {
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.topMargin = CommonUtils.dip2px(mContext, 15);
                        // holder.contentLayout.setPadding(0, 0, 0, CommonUtils.dip2px(mContext,
                        // 15));
                        params.bottomMargin = CommonUtils.dip2px(mContext, 5);
                        holder.contentLayout.setOrientation(LinearLayout.VERTICAL);
                        holder.contentLayout.setLayoutParams(params);
                        holder.shengLue.setVisibility(View.GONE);
                    }
                }
            });

            // 获取item中头像的userID
            holder.headIcon.setUserName(vo.getFullname());
            // 获取item中头像的userID
            holder.headIcon.setUserId(vo.getLoguid());
            holder.headIcon.setmImageLoader(mImageLoader);
            holder.headIcon.setUrl(vo.getHeadpic());
        }

        return convertView;
    }

    public void commit(String url, JournalListVO1 vo, int status) {
        RequestParams params = new RequestParams();
        try {
            params.put("ftoken", BSApplication.getInstance().getmCompany());
            params.put("userid", BSApplication.getInstance().getUserId());
            params.put("logid", vo.getLogid());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        AsyncHttpClient client = new AsyncHttpClient();

        client.post(BSApplication.getInstance().getHttpTitle() + url, params,
                new AsyncHttpResponseHandler() {

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                    }

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(new String(arg2));
                            String code = (String) jsonObject.get("code");
                            if (Constant.RESULT_CODE.equals(code)) {
                            } else {
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public int getItemViewType(int position) {
        if (mPinnedList.size() == 0)
            return 0;
        if (position >= mPinnedList.size())
            return mPinnedList.size() - 1;
        return mPinnedList.get(position);
    }

    @Override
    public int getViewTypeCount() {
        return mList.size() + 1;
    }

    @Override
    public int getCount() {
        if (mList == null || mList.size() == 0) {
            mIsEmpty = true;
            return 1;
        } else {
            mIsEmpty = false;
            return mPinnedList.size();
        }

    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == -10;
    }

    @Override
    public void updateData(List<JournalListVO1> list) {
        super.updateData(list);
        sortTime(mList);
    }

    @Override
    public void updateDataFrist(List<JournalListVO1> list) {
        super.updateDataFrist(list);
        sortTime(mList);
    }

    @Override
    public void updateDataLast(List<JournalListVO1> list) {
        super.updateDataLast(list);
        sortTime(mList);
    }

    @Override
    public JournalListVO1 getItem(int position) {
        return mList.get(mPinnedList.get(position));
    }

    public void sortTime(List<JournalListVO1> list) {
        mPinnedList.clear();
        for (int i = 0; i < list.size(); i++) {
            JournalListVO1 vo = list.get(i);
            if (i == 0) {
                mPinnedList.add(-10);
            }
            if (i != 0 && !vo.getDate().equals((list.get(i - 1).getDate()))) {
                mPinnedList.add(-10);
                mPinnedList.add(i);
            } else {
                mPinnedList.add(i);
            }
        }
    }

    static class ViewHolder {
        public TextView timeTv, nameTv, typeTv, todayTv, finishTv, tomrrowTv, experienceTv, publishTimeTv, replayTv, agreeTv, departTv, title01Tv, title02Tv, title03Tv, title04Tv;
        public BSCircleImageView headIcon;
        private LinearLayout contentLayout, aggreeLayout;
        private TextView shengLue;
        private ImageView imgAgree, favorImg;
        private FrameLayout frameLayout;
    }
}
