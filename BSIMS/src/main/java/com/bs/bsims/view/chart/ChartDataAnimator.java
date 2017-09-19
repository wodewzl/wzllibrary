
package com.bs.bsims.view.chart;

public interface ChartDataAnimator {

    public static final long DEFAULT_ANIMATION_DURATION = 500;

    public void startAnimation(long duration);

    public void cancelAnimation();

    public boolean isAnimationStarted();

    public void setChartAnimationListener(ChartAnimationListener animationListener);

}
