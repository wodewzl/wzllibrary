
package com.wuzhanglong.library.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wuzhanglong.library.R;
import com.wuzhanglong.library.utils.WidthHigthUtil;

import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

public abstract class ListBaseAdapter<M> extends BGAAdapterViewAdapter {
//    public Context mContext;
//    public List<T> mList;
    public ImageLoader mImageLoader;
    public DisplayImageOptions mOptions;
    public boolean mIsEmpty = true;
    public abstract void initData(BGAViewHolderHelper helper, int position, Object model);
    public ListBaseAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

        @Override
    public View getView(int arg0, View convertView, ViewGroup arg2) {
        if (mIsEmpty){
            convertView = View.inflate(mContext, R.layout.base_adapter, null);
            android.widget.AbsListView.LayoutParams params = new android.widget.AbsListView
                    .LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    WidthHigthUtil.getNoTitleScreenHigh(mContext));
            convertView.setLayoutParams(params);
            return convertView;
        }else{
            return super.getView( arg0, convertView,arg2);
        }
    }
    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, Object model) {
        initData(helper, position, model);
    }

        @Override
    public int getCount() {
        if (mData == null || mData.size() == 0) {
            mIsEmpty = true;
            return 1;
        } else {
            mIsEmpty = false;
            return mData.size();
        }
    }

    public void updateData(List<M> data) {
        if (data != null) {
            mData = data;
        } else {
            mData.clear();
        }
        notifyDataSetChanged();
    }

    public void updateDataFrist(List<M> data) {
        if (data != null) {
            mData.addAll(0, data);
            notifyDataSetChanged();
        }
    }

    public void updateDataLast(List<M> data) {
        if (data != null) {
            mData.addAll(mData.size(), data);
            notifyDataSetChanged();
        }
    }

//    public ListBaseAdapter(Context context) {
//        mList = new ArrayList<T>();
//        mContext = context;
//        mOptions = Options.getListOptions();
//        mImageLoader = ImageLoader.getInstance();
//    }
//
//    @Override
//    public int getCount() {
//        if (mList == null || mList.size() == 0) {
//            mIsEmpty = true;
//            return 1;
//        } else {
//            mIsEmpty = false;
//            return mList.size();
//        }
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return mList.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int arg0, View convertView, ViewGroup arg2) {
//        convertView = View.inflate(mContext, R.layout.base_adapter, null);
//        android.widget.AbsListView.LayoutParams params = new android.widget.AbsListView
//                .LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                WidthHigthUtil.getNoTitleScreenHigh(mContext));
//        convertView.setLayoutParams(params);
//
//        if (mIsFirst)
//            convertView.setVisibility(View.GONE);
//        else
//            convertView.setVisibility(View.VISIBLE);
//        return convertView;
//    }
//
//    public void updateData(List<T> list) {
//        mIsFirst = false;
//        if (list == null)
//            list = new ArrayList<T>();
//        mList.clear();
//        mList.addAll(list);
//        this.notifyDataSetChanged();
//    }
//
//    public void updateDataFrist(List<T> list) {
//        mIsFirst = false;
//        if (list == null)
//            list = new ArrayList<T>();
//        mList.clear();
//        mList.addAll(list);
//        this.notifyDataSetChanged();
//    }
//
//    public void updateDataLast(List<T> list) {
//        mIsFirst = false;
//        if (list == null)
//            list = new ArrayList<T>();
//        mList.addAll(list);
//        this.notifyDataSetChanged();
//    }
//
//    public void setList(List<T> paramList) {
//        mIsFirst = false;
//        if ((paramList == null) || (paramList.isEmpty())) {
//            notifyChangeEmpty();
//        } else {
//            this.mList = new ArrayList();
//            this.mList.addAll(paramList);
//            notifyDataSetChanged();
//        }
//    }
//
//    public void notifyChangeEmpty() {
//        this.mList = new ArrayList();
//        notifyDataSetChanged();
//    }
//
//    class ViewHolder {
//        private LinearLayout beaeContentLayout;
//        private TextView noContentLayout;
//    }

}
