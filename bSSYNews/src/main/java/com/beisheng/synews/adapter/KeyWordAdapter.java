
package com.beisheng.synews.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.adapter.BSBaseAdapter;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.DateUtils;
import com.beisheng.synews.mode.KeyWordVO;
import com.beisheng.synews.utils.StartViewUitl;
import com.im.zhsy.R;

public class KeyWordAdapter extends BSBaseAdapter<KeyWordVO> {
    public String keyword;

    public KeyWordAdapter(Context context) {
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
            convertView = View.inflate(mContext, R.layout.cache_list_adapter, null);
            holder.titleTv = (TextView) convertView.findViewById(R.id.title_tv);
            holder.timeTv = (TextView) convertView.findViewById(R.id.time_tv);
            holder.readTv = (TextView) convertView.findViewById(R.id.read_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final KeyWordVO vo = (KeyWordVO) mList.get(position);
        BaseActivity activity = (BaseActivity) mContext;
        if (getKeyword() != null && vo.getTitle().contains(getKeyword())) {
            String[] splitArray = vo.getTitle().split(getKeyword());
            if (splitArray.length >= 2) {
                String beforeStr = splitArray[0];
                String lastStr = splitArray[1];
                BaseCommonUtils.setDifferentTextColorMiddle(holder.titleTv, beforeStr, getKeyword(), lastStr, "#B10304");
            } else {
                String lastStr = splitArray[0];
                BaseCommonUtils.setDifferentTextColorMiddle(holder.titleTv, "", getKeyword(), lastStr, "#B10304");
            }

        } else {
            holder.titleTv.setText(vo.getTitle());
        }
        holder.timeTv.setText(DateUtils.parseMDHM(vo.getCreatime()));
        convertView.setOnClickListener(new OnClickListener() {
            BaseActivity activity = (BaseActivity) mContext;

            @Override
            public void onClick(View arg0) {
                StartViewUitl.startView(activity, vo.getSuburl(), vo.getContentid(), null, null, vo.getTypename());
            }
        });
        return convertView;
    }

    static class ViewHolder {
        private TextView titleTv, timeTv, readTv;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

}
