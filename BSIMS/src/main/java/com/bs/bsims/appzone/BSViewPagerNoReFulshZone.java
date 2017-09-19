
package com.bs.bsims.appzone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * 解决viewpager调整切换速率 当viewpager和titleTop配合时，在A页面点击D页面有闪屏效果，为了避免这个情况，重写这个pager，避免出现。
 */

public class BSViewPagerNoReFulshZone extends Scroller {
    private int mDuration = 0;

    public BSViewPagerNoReFulshZone(Context context) {
        super(context);
    }

    public BSViewPagerNoReFulshZone(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    @SuppressLint("NewApi")
    public BSViewPagerNoReFulshZone(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }
}
