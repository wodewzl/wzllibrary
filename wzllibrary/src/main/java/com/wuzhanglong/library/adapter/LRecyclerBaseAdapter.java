package com.wuzhanglong.library.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wuzhanglong.library.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Administrator on 2017/3/13.
 */
// CommonAdapter RecyclerView.Adapter
public  class LRecyclerBaseAdapter<M> extends RecyclerView.Adapter {
    protected List<M> mList = new ArrayList<>();
    protected Context mContext;
    public boolean mIsEmpty = false;
    public static final int EMPTY_VIEW = 1;
    //    public static final int CONTENT_VIEW=2;
    public LayoutInflater mInflater;

    public LRecyclerBaseAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        final View view = mInflater.inflate(R.layout.base_adapter, parent, false);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }


//
    @Override
    public int getItemViewType(int position) {
        if(mList.size() == 0){
            return R.layout.base_adapter;
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public int getItemCount() {
//        return mList.size();
        if (mList == null || mList.size() == 0) {
            mIsEmpty = true;
            return 1;
        } else {
            mIsEmpty = false;
            return mList.size();
        }
    }

    public List<M> getDataList() {
        return mList;
    }

    public void setDataList(Collection<M> list) {
        this.mList.clear();
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public void addAll(Collection<M> list) {
        int lastIndex = this.mList.size();
        if (this.mList.addAll(list)) {
            notifyItemRangeInserted(lastIndex, list.size());
        }
    }

    public void updateData(List<M> list) {
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void updateDataFrist(List<M> list) {
        if (this.mList.addAll(list)) {
            notifyItemRangeInserted(0, list.size());
        }
    }

    public void updateDataLast(List<M> list) {
        int lastIndex = this.mList.size();
        if (this.mList.addAll(list)) {
            notifyItemRangeInserted(lastIndex, list.size());
        }

    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }


    static class BaseViewHolder extends RecyclerView.ViewHolder {


        public BaseViewHolder(View itemView) {
            super(itemView);

        }
    }

}
