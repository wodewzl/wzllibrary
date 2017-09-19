
package com.bs.bsims.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bs.bsims.R;
import com.bs.bsims.utils.CommonUtils;

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

    public UploadAdapter(Context context, String type) {
        mList = new ArrayList<Bitmap>();
        this.mContext = context;
        this.mPicList = new ArrayList<String>();
        this.mType = type;
    }

    @Override
    public int getCount() {
        if (mList.size() >= 3) {
            return 3;
        }
        if ("1".equals(mType) || "2".equals(mType)) {
            return mList.size();
        } else {
            return (mList.size() + 1);
        }
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_published_grida, null);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
            holder.delete = (ImageView) convertView.findViewById(R.id.delete_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (!"1".equals(mType) && !"2".equals(mType)) {
            if (position == mList.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),
                        R.drawable.upload_picture));

                if (position == 3) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(CommonUtils.zoomImg(mList.get(position),
                        CommonUtils.getViewWidth(holder.image), CommonUtils.getViewHigh(holder.image)));
            }
        } else {
            holder.image.setImageBitmap(CommonUtils.zoomImg(mList.get(position),
                    CommonUtils.getViewWidth(holder.image), CommonUtils.getViewHigh(holder.image)));
            if ("2".equals(mType)) {
                holder.delete.setVisibility(View.VISIBLE);
            } else {
                holder.delete.setVisibility(View.GONE);
            }
        }

        return convertView;
    }

    static class ViewHolder {
        private ImageView image, delete;
    }
}
