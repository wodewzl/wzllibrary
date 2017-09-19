
package com.beisheng.synews.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beisheng.base.adapter.BSBaseAdapter;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.DateUtils;
import com.beisheng.synews.mode.MessageVO;
import com.im.zhsy.R;

public class MessageAdapter extends BSBaseAdapter<MessageVO> {
    private int currentPosition = -1;

    public MessageAdapter(Context context) {
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
            convertView = View.inflate(mContext, R.layout.message_adapter, null);
            holder.timeTv = (TextView) convertView.findViewById(R.id.time_tv);
            holder.titleTv = (TextView) convertView.findViewById(R.id.title_tv);
            holder.content_tv = (TextView) convertView.findViewById(R.id.content_tv);
            holder.msgLayout = (LinearLayout) convertView.findViewById(R.id.msg_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final MessageVO vo = (MessageVO) mList.get(position);
        holder.timeTv.setText(DateUtils.parseMDHM(vo.getCreatime()));
        holder.titleTv.setText(vo.getTitle());
        holder.content_tv.setText(vo.getContent());
        holder.msgLayout.setBackground(BaseCommonUtils.setBackgroundShap(mContext, 5, R.color.devider_bg, R.color.C1));
        return convertView;
    }

    static class ViewHolder {
        private TextView titleTv, timeTv, content_tv;
        private LinearLayout msgLayout;
    }

}
