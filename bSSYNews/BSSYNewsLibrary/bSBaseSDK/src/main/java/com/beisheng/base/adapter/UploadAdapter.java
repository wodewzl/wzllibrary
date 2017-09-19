
package com.beisheng.base.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.beisheng.base.R;

import java.util.ArrayList;
import java.util.List;

public class UploadAdapter extends BaseAdapter {
    public List<Bitmap> mList;
    public List<String> mPicList;
    private Context mContext;
    private String mType = "0"; // 0为默认， 1为打卡，2为通知;

    public UploadAdapter(Context context) {
        mList = new ArrayList<Bitmap>();
        this.mContext = context;
        this.mPicList = new ArrayList<String>();
    }

    @Override
    public int getCount() {
        if (mList.size() >= 9) {
            return 9;
        }
        return (mList.size() + 1);
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("NewApi")
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_published_grida, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == mList.size()) {
            holder.image.setBackgroundResource(R.drawable.upload_picture);
            if (position == 9) {
                holder.image.setVisibility(View.GONE);
            }
        } else {
            Drawable drawable = new BitmapDrawable(mList.get(position));
            holder.image.setBackground(drawable);
        }

        return convertView;
    }

    static class ViewHolder {
        private ImageView image;
    }
}
