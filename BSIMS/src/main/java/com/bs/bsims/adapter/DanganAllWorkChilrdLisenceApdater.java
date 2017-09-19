
package com.bs.bsims.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.ImagePreviewActivity;
import com.bs.bsims.model.DanganWorkLicense;
import com.bs.bsims.utils.CommonUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class DanganAllWorkChilrdLisenceApdater extends BaseAdapter {
    private Context mContext;
    public List<DanganWorkLicense> mList;
    private DisplayImageOptions options;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public DanganAllWorkChilrdLisenceApdater(Context context) {
        mContext = context;
        mList = new ArrayList<DanganWorkLicense>();
        options = CommonUtils.initImageLoaderOptions();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext,
                    R.layout.da_workallchrildlisence_apdater, null);
            holder.lisencetype = (TextView) convertView
                    .findViewById(R.id.lisencetype);
            holder.lisencetime = (TextView) convertView
                    .findViewById(R.id.lisencetime);
            holder.lisencephoto = (ImageView) convertView
                    .findViewById(R.id.lisencephoto);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 给值
        holder.lisencetype.setText(mList.get(position).getName());
        imageLoader.displayImage(mList.get(position).getPhoto(),
                holder.lisencephoto, options);
        holder.lisencetime.setVisibility(View.INVISIBLE);
        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                ArrayList<String> list = new ArrayList<String>();
                list.add(mList.get(position).getPhoto());
                Intent i = new Intent();
                i.putStringArrayListExtra("piclist", list);
                i.setClass(mContext, ImagePreviewActivity.class);
                mContext.startActivity(i);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        private TextView lisencetype;
        private TextView lisencetime;
        private ImageView lisencephoto;

    }
}
