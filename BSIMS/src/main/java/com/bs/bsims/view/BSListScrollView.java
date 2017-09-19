/**
 * 
 */
package com.bs.bsims.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;
import android.widget.ScrollView;

import com.bs.bsims.utils.LogUtils;

/**

 *          BS北盛最帅程序员

 *         Copyright (c) 2016

 *        湖北北盛科技有限公司

 *        @author 梁骚侠
 
 *        @date 2016-2-23

 *        @version 1.22  ScrollView里嵌套listview，listview可以滑动

 */
public class BSListScrollView extends ListView {
   

	private static final String TAG = "ScrollviewEdit";
    private ScrollView parent_scrollview;

    public ScrollView getParent_scrollview() {
        return parent_scrollview;
    }

    public void setParent_scrollview(ScrollView parent_scrollview) {
        this.parent_scrollview = parent_scrollview;
    }

    public BSListScrollView(Context context) {
        super(context);
    }

    public BSListScrollView(Context context, AttributeSet attrs) {
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
}
