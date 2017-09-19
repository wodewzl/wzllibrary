
package com.beisheng.synews.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.synews.activity.KeyWordActivity;
import com.im.zhsy.R;

public class NewsKeyGVAdapter extends BaseAdapter {
    private String[] mList = new String[0];
    private Context mContext;

    public NewsKeyGVAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mList.length;
    }

    @Override
    public Object getItem(int position) {
        return mList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.specical_topic_gv_adapter, null);
            holder.typeNameTv = (CheckedTextView) convertView.findViewById(R.id.type_name_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.typeNameTv.setBackground(BaseCommonUtils.setBackgroundShap(mContext, 100, R.color.devider_bg, R.color.C3));
        holder.typeNameTv.setText(mList[position]);
        holder.typeNameTv.setChecked(false);
        holder.typeNameTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                notifyDataSetChanged();

                Bundle bundle = new Bundle();
                bundle.putString("keyword", mList[position]);
                BaseActivity activity = (BaseActivity) mContext;
                activity.openActivity(KeyWordActivity.class, bundle, 0);

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

    public void updata(String[] tags) {
        mList = tags;
        this.notifyDataSetChanged();
    }

    static class ViewHolder {
        private CheckedTextView typeNameTv;
    }
}
