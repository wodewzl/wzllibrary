
package com.bs.bsims.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.NotifyVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.view.PinnedSectionListView.PinnedSectionListAdapter;

import java.util.ArrayList;
import java.util.List;

public class BossNotityAdapter extends BSBaseAdapter<NotifyVO> implements PinnedSectionListAdapter {
    private Context mContext;
    public List<Integer> mPinnedList = new ArrayList<Integer>();

    public BossNotityAdapter(Context context) {
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
            convertView = View.inflate(mContext, R.layout.boss_notify_adapter, null);
            holder.timeTv = (TextView) convertView.findViewById(R.id.time_tv);
            holder.notify_layout_01 = (LinearLayout) convertView.findViewById(R.id.notify_layout_01);
            holder.notify_tv_01 = (TextView) convertView.findViewById(R.id.notify_tv_01);
            holder.notify_time_tv_01 = (TextView) convertView.findViewById(R.id.notify_time_tv_01);
            holder.notify_content_tv_01 = (TextView) convertView.findViewById(R.id.notify_content_tv_01);
            holder.notify_layout_02 = (LinearLayout) convertView.findViewById(R.id.notify_layout_02);
            holder.notify_tv_02 = (TextView) convertView.findViewById(R.id.notify_tv_02);
            holder.notify_content_tv_02 = (TextView) convertView.findViewById(R.id.notify_content_tv_02);
            holder.notify_type_img_01 = (ImageView) convertView.findViewById(R.id.notify_type_img_01);
            holder.notify_type_img_02 = (ImageView) convertView.findViewById(R.id.notify_type_img_02);
            holder.notify_content_layout_02 = (LinearLayout) convertView.findViewById(R.id.notify_content_layout_02);
            holder.root_layout = (LinearLayout) convertView.findViewById(R.id.root_layout);
            holder.pinned_layout = (LinearLayout) convertView.findViewById(R.id.pinned_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (this.getItemViewType(position) == -10) {
            holder.pinned_layout.setVisibility(View.VISIBLE);
            holder.timeTv.setText(mList.get(mPinnedList.get(position + 1)).getDate());
            holder.root_layout.setVisibility(View.GONE);
        } else {
            holder.pinned_layout.setVisibility(View.GONE);
            holder.root_layout.setVisibility(View.VISIBLE);
            final NotifyVO vo = mList.get(mPinnedList.get(position));
            if ("1".equals(vo.getType())) {
                holder.notify_type_img_01.setVisibility(View.GONE);
                holder.notify_type_img_02.setVisibility(View.VISIBLE);
                holder.notify_type_img_02.setImageResource(R.drawable.notify_icon_02);
                holder.notify_layout_02.setVisibility(View.VISIBLE);
                holder.notify_layout_01.setVisibility(View.GONE);
                holder.notify_tv_02.setText(vo.getTypename());
                String[] str = vo.getContent().split("@");
                String str1 = str[0];
                holder.notify_content_tv_02.setText(Html.fromHtml(str1));
                holder.notify_content_layout_02.removeAllViews();
                for (int i = 1; i < str.length; i++) {
                    LinearLayout layout = new LinearLayout(mContext);
                    layout.setPadding(CommonUtils.dip2px(mContext, 15), 0, 0, 0);

                    TextView numberTv = new TextView(mContext);
                    numberTv.setBackground(CommonUtils.setBackgroundShap(mContext, 100, R.color.C9, R.color.C9));
                    numberTv.setText(i + "");
                    numberTv.setTextSize(10);
                    numberTv.setHeight(CommonUtils.dip2px(mContext, 14));
                    numberTv.setWidth(CommonUtils.dip2px(mContext, 14));
                    numberTv.setGravity(Gravity.CENTER);
                    numberTv.setTextColor(mContext.getResources().getColor(R.color.C1));

                    TextView contentTv = new TextView(mContext);
                    contentTv.setPadding(CommonUtils.dip2px(mContext, 5), 0, 0, 0);
                    contentTv.setText(Html.fromHtml(str[i]));
                    contentTv.setLineSpacing(CommonUtils.dip2px(mContext, 26), 0);

                    TextView lineTv = new TextView(mContext);
                    LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, CommonUtils.dip2px(mContext, 0.5f));
                    lineParams.setMargins(CommonUtils.dip2px(mContext, 15), CommonUtils.dip2px(mContext, 5), 0, CommonUtils.dip2px(mContext, 5));
                    lineTv.setBackgroundColor(mContext.getResources().getColor(R.color.C3));
                    lineTv.setLayoutParams(lineParams);

                    layout.addView(numberTv);
                    layout.addView(contentTv);
                    holder.notify_content_layout_02.addView(layout);
                    holder.notify_content_layout_02.addView(lineTv);
                    if (i == str.length - 1)
                        lineTv.setVisibility(View.INVISIBLE);
                }

            } else {
                holder.notify_type_img_02.setVisibility(View.GONE);
                holder.notify_type_img_01.setVisibility(View.VISIBLE);
                holder.notify_type_img_01.setImageResource(R.drawable.notify_icon_01);
                holder.notify_layout_01.setVisibility(View.VISIBLE);
                holder.notify_layout_02.setVisibility(View.GONE);

                holder.notify_tv_01.setText(vo.getTypename());
                holder.notify_time_tv_01.setText(vo.getDatetime());
                holder.notify_content_tv_01.setText(vo.getContent());
                holder.notify_content_tv_01.setText(Html.fromHtml(vo.getContent()));
            }
        }

        return convertView;
    }

    static class ViewHolder {
        private TextView timeTv, notify_tv_01, notify_time_tv_01, notify_content_tv_01, notify_tv_02, notify_content_tv_02;
        private LinearLayout pinned_layout, notify_layout_01, notify_layout_02, notify_content_layout_02, root_layout;
        private ImageView notify_type_img_01, notify_type_img_02;
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
    public void updateData(List<NotifyVO> list) {
        super.updateData(list);
        sortTime(mList);
    }

    @Override
    public void updateDataFrist(List<NotifyVO> list) {
        super.updateDataFrist(list);
        sortTime(mList);
    }

    @Override
    public void updateDataLast(List<NotifyVO> list) {
        super.updateDataLast(list);
        sortTime(mList);
    }

    @Override
    public NotifyVO getItem(int position) {
        return mList.get(mPinnedList.get(position));
    }

    public void sortTime(List<NotifyVO> list) {
        mPinnedList.clear();
        for (int i = 0; i < list.size(); i++) {
            NotifyVO vo = list.get(i);
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

}
