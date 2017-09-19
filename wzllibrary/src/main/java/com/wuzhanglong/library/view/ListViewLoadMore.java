
package com.wuzhanglong.library.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.wuzhanglong.library.R;
import com.wuzhanglong.library.interfaces.LoadMoreListener;


public class ListViewLoadMore extends ListView implements OnScrollListener {
    private int lastItemIndex;// 当前ListView中最后一个Item的索引
    private LoadMoreListener loadMoreListener;
    private Context mContext;
    private LinearLayout mFooterLayout;

    public ListViewLoadMore(Context context) {
        super(context);
    }

    public ListViewLoadMore(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.setOnScrollListener(this);
        addFooterView();
    }

    public ListViewLoadMore(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        try {
            super.dispatchDraw(canvas);
        } catch (IndexOutOfBoundsException e) {
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
        lastItemIndex = firstVisibleItem + visibleItemCount - 1 - 1;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 停止状态的判断条件，此处给去掉，影响体验 && mHasMoreData
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && lastItemIndex == view.getAdapter().getCount() - (getHeaderViewsCount()+getFooterViewsCount())-1) {
            // 加载数据代码，此处省略了
            if (mFooterLayout.getVisibility() == View.VISIBLE)
                loadMoreListener.loadMore();
        }
    }

    public LoadMoreListener getLoadMoreListener() {
        return loadMoreListener;
    }

    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    @SuppressLint("ResourceAsColor")
    public void addFooterView() {
        View footerView = View.inflate(mContext, R.layout.listview_footer, null);
        mFooterLayout = (LinearLayout) footerView.findViewById(R.id.footer_layout);
        this.addFooterView(footerView);
        mFooterLayout.setVisibility(View.GONE);
    }

    public void showFooterView(boolean isShow) {
        if (isShow) {
            mFooterLayout.setVisibility(View.VISIBLE);
        } else {
            mFooterLayout.setVisibility(View.GONE);
        }
    }

}
