package com.beisheng.synews.view;

import android.content.Context;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * created：gaoyao on 2016/12/5 22:24
 */
public class MyWebView extends WebView
{
    private boolean mPreventParentTouch=false;
    public MyWebView(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean ret = super.onTouchEvent(ev);
        if (mPreventParentTouch) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    requestDisallowInterceptTouchEvent(true);
                    ret = true;
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    requestDisallowInterceptTouchEvent(false);
                    mPreventParentTouch = false;
                    break;
            }
        }
        return ret;
    }

    public void preventParentTouchEvent () {
        mPreventParentTouch = true;
    }
}
