
package com.beisheng.synews.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.beisheng.base.adapter.BSBaseAdapter;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.ThreadUtil;
import com.beisheng.synews.activity.SpecialTopicActivity;
import com.beisheng.synews.mode.SpecialTopicVO;
import com.im.zhsy.R;

public class SpecicalTopicGVAdapter extends BSBaseAdapter<SpecialTopicVO> {

    public SpecicalTopicGVAdapter(Context context) {
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
            convertView = View.inflate(mContext, R.layout.specical_topic_gv_adapter, null);
            holder.typeNameTv = (CheckedTextView) convertView.findViewById(R.id.type_name_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final SpecialTopicVO vo = (SpecialTopicVO) mList.get(position);
        holder.typeNameTv.setBackground(BaseCommonUtils.setBackgroundShap(mContext, 20, R.color.devider_bg, R.color.C3));
        holder.typeNameTv.setText(vo.getTitle());
        holder.typeNameTv.setChecked(false);
        holder.typeNameTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                notifyDataSetChanged();
                SpecialTopicActivity activity = (SpecialTopicActivity) mContext;
                activity.mTypeid = vo.getId();
                if (position == 0)
                    activity.mIsFrist = true;
                activity.showProgressDialog();
                new ThreadUtil(activity, activity).start();
                holder.typeNameTv.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        holder.typeNameTv.setChecked(true);

                    }
                }, 100);

            }
        });

        return convertView;
    }

    static class ViewHolder {
        private CheckedTextView typeNameTv;
    }
}
