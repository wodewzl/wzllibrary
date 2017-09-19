
package com.bs.bsims.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.bs.bsims.utils.LogUtils;

public class BSScrollView extends ScrollView {
    private static final String TAG = "ScrollviewEdit";
    private ScrollView parent_scrollview;
    private onScrollListener mOnScrollChangedCallback;

    public ScrollView getParent_scrollview() {
        return parent_scrollview;
    }
    
    public void setOnScrollListener(onScrollListener scrollListener){
        this.mOnScrollChangedCallback =scrollListener;
    }

    public void setParent_scrollview(ScrollView parent_scrollview) {
        this.parent_scrollview = parent_scrollview;
    }

    public BSScrollView(Context context) {
        super(context);
    }

    public BSScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    int currentY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        LogUtils.i(TAG, "onInterceptTouchEvent--------");
        if (parent_scrollview == null) {
            return super.onInterceptTouchEvent(ev);
        } else {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                // 将父scrollview的滚动事件拦截
                currentY = (int) ev.getY();
                setParentScrollAble(false);
                LogUtils.i(TAG, "将父scrollview的滚动事件拦截-----");
                return super.onInterceptTouchEvent(ev);
            } else if (ev.getAction() == MotionEvent.ACTION_UP) {
                // 把滚动事件恢复给父Scrollview
                setParentScrollAble(true);
                LogUtils.i(TAG, "把滚动事件恢复给父Scrollview-----");
            } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 是否把滚动事件交给父scrollview
     * 
     * @param flag
     */
    private void setParentScrollAble(boolean flag) {
        parent_scrollview.requestDisallowInterceptTouchEvent(!flag);
    }
    
    @Override
    protected void onScrollChanged(final int l, final int t, final int oldl, final int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangedCallback != null) {
            mOnScrollChangedCallback.onScroll(l, t, oldl, oldt);
            if (oldt - t > 0 || t < 100) {

                mOnScrollChangedCallback.onScrollDown();

            } else if (t - oldt > 0) {
                mOnScrollChangedCallback.onScrollUp();

            }

        }

    }
    
    public interface onScrollListener{
        void onScroll(int line,int tran,int oldLine,int oldTran);
        void onScrollUp();
        void onScrollDown();
    }
    
}
