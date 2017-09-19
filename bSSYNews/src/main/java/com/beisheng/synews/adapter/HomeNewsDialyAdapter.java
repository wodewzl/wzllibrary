
package com.beisheng.synews.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beisheng.base.adapter.BSBaseAdapter;
import com.beisheng.synews.mode.NewsVO;
import com.beisheng.synews.view.PinnedSectionListView.PinnedSectionListAdapter;
import com.im.zhsy.R;

import java.util.ArrayList;
import java.util.List;

public class HomeNewsDialyAdapter extends BSBaseAdapter<NewsVO> implements PinnedSectionListAdapter {
    // public List<Integer> mPinnedList = new ArrayList<Integer>();
    public List<NewsVO> mAllList = new ArrayList<NewsVO>();

    public HomeNewsDialyAdapter(Context context) {
        super(context);
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (mIsEmpty) {
            return super.getView(position, convertView, parent);
        }

        if (convertView != null && convertView.getTag() == null)
            convertView = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.home_news_dialy_adapter, null);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.titleTv = (TextView) convertView.findViewById(R.id.title_tv);
            holder.contentTv = (TextView) convertView.findViewById(R.id.content_tv);
            holder.typeLayout = (LinearLayout) convertView.findViewById(R.id.type_layout);
            holder.contentLayout = (LinearLayout) convertView.findViewById(R.id.content_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        NewsVO vo = (NewsVO) mList.get(position);
        if (this.getItemViewType(position) == -10) {
            holder.typeLayout.setVisibility(View.VISIBLE);
            holder.contentLayout.setVisibility(View.GONE);
            holder.titleTv.setText(vo.getTitle());
        } else {
            holder.typeLayout.setVisibility(View.GONE);
            holder.contentLayout.setVisibility(View.VISIBLE);
            mImageLoader.displayImage(vo.getThumb(), holder.img, mOptions);
            holder.contentTv.setText(vo.getTitle());
        }

        return convertView;
    }

    static class ViewHolder {
        private ImageView img;
        private TextView titleTv, contentTv;
        private LinearLayout typeLayout, contentLayout;
    }

    public void sortData(List<NewsVO> list) {
        mAllList.clear();
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setType(-10);
            mAllList.add(list.get(i));
            for (int j = 0; j < list.get(i).getList().size(); j++) {
                mAllList.add(list.get(i).getList().get(j));
            }
        }
    }

    @Override
    public void updateData(List<NewsVO> list) {
        sortData(list);
        super.updateData(mAllList);
    }

    @Override
    public int getItemViewType(int position) {
        if (mList.size() == 0)
            return 0;
        return mList.get(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return mList.size() + 2;
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == -10;
    }

}
