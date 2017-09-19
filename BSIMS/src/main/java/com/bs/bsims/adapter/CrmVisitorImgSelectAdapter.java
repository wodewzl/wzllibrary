
package com.bs.bsims.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bs.bsims.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class CrmVisitorImgSelectAdapter extends BaseAdapter {
    private Context mContext;
    public List<String> mList;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;

    public CrmVisitorImgSelectAdapter(Context context) {
        this.mContext = context;
        this.mList = new ArrayList<String>();
        mImageLoader = ImageLoader.getInstance();
        mOptions = new DisplayImageOptions.Builder().showStubImage(R.drawable.common_ic_image_default).showImageForEmptyUri(R.drawable.common_ic_image_default)
                .showImageOnFail(R.drawable.common_ic_image_default).cacheInMemory().cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return mList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.crm_visitor_img_adpter, null);
            holder.detial_img_01 = (ImageView) convertView.findViewById(R.id.detial_img_01);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        mImageLoader.displayImage(mList.get(position), holder.detial_img_01, mOptions);

        return convertView;
    }

    static class ViewHolder {
        private ImageView detial_img_01;
    }

    public void updateList(List<String> list) {
        if (null == list)
            return;
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }
}
