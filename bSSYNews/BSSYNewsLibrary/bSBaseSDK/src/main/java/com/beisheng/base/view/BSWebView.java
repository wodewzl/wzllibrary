
package com.beisheng.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

public class BSWebView extends WebView {

    public BSWebView(Context context) {
        super(context);
    }

    public BSWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (this.getScrollY() <= 0)
                    this.scrollTo(0, 1);
                break;
            case MotionEvent.ACTION_UP:
                // if(this.getScrollY() == 0)
                // this.scrollTo(0,-1);
                break;
        }
        return super.onTouchEvent(event);
    }
}
