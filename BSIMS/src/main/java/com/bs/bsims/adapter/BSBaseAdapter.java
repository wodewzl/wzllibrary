
package com.bs.bsims.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bs.bsims.R;
import com.bs.bsims.utils.CommonUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class BSBaseAdapter<T> extends BaseAdapter {
    public Context mContext;
    public List<T> mList;
    public ImageLoader mImageLoader;
    public DisplayImageOptions mOptions;
    public boolean mIsEmpty = false;
    public boolean mIsFirst = true;

    public BSBaseAdapter(Context context) {
        mList = new ArrayList<T>();
        mContext = context;
        mImageLoader = ImageLoader.getInstance();
        mOptions = CommonUtils.initImageLoaderOptions();
    }

    @Override
    public int getCount() {
        if (mList == null || mList.size() == 0) {
            mIsEmpty = true;
            return 1;
        } else {
            mIsEmpty = false;
            return mList.size();
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

    @Override
    public View getView(int arg0, View convertView, ViewGroup arg2) {
        convertView = View.inflate(mContext, R.layout.no_content_layout, null);
        if (mIsFirst)
            convertView.setVisibility(View.GONE);
        else
            convertView.setVisibility(View.VISIBLE);
        return convertView;
    }

    public void updateData(List<T> list) {
        mIsFirst = false;
        if (list == null)
            list = new ArrayList<T>();
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataFrist(List<T> list) {
        mIsFirst = false;
        if (list == null)
            list = new ArrayList<T>();

        list.addAll(mList);
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataLast(List<T> list) {
        mIsFirst = false;
        if (list == null)
            list = new ArrayList<T>();

        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void setList(List<T> paramList)
    {
        mIsFirst = false;
        if ((paramList == null) || (paramList.isEmpty())) {
            notifyChangeEmpty();
        } else {
            this.mList = new ArrayList();
            this.mList.addAll(paramList);
            notifyDataSetChanged();
        }
    }

    public void notifyChangeEmpty()
    {
        this.mList = new ArrayList();
        notifyDataSetChanged();
    }

    public boolean ismIsEmpty() {
        return mIsEmpty;
    }

    public void setmIsEmpty(boolean mIsEmpty) {
        this.mIsEmpty = mIsEmpty;
    }

}
