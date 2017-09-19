
package com.bs.bsims.view;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.bs.bsims.R;

public class BSSwitchView extends RelativeLayout implements OnTouchListener {
    private ImageView ivSlipBg, ivSlipBtn, ivSlipFrame;
    public boolean onSlip = false, onAnim = false;
    public boolean nowChoose = false;
    private OnChangedListener ChgLsn;
    private TranslateAnimation ta;
    private int left = 0;
    private Point downPoint, originalPoint;
    private ScrollView scrollView = null;

    public BSSwitchView(Context context) {
        this(context, null);
    }

    public BSSwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.switch_button, this, true);
        ivSlipBg = (ImageView) findViewById(R.id.slip_bg);
        ivSlipBtn = (ImageView) findViewById(R.id.slip_btn);
        ivSlipFrame = (ImageView) findViewById(R.id.slip_frame);
        downPoint = new Point();
        originalPoint = new Point();
        setOnTouchListener(this);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onAnim = false;
                onSlip = false;
                downPoint.x = (int) event.getX();
                downPoint.y = (int) event.getY();
                originalPoint = downPoint;
                break;
            case MotionEvent.ACTION_MOVE:
                if (scrollView != null)
                    scrollView.requestDisallowInterceptTouchEvent(true);
                onAnim = false;
                if (!onSlip) {
                    float x = event.getX() - originalPoint.x;
                    float y = event.getY() - originalPoint.y;
                    if (x * x + y * y < 400) {
                        return false;
                    }
                }
                onSlip = true;
                left += event.getX(0) - downPoint.x;
                if (left < 0) {
                    left = 0;
                }
                if (left > ivSlipFrame.getWidth() - ivSlipBtn.getWidth()) {
                    left = ivSlipFrame.getWidth() - ivSlipBtn.getWidth();
                }
                downPoint.x = (int) event.getX();
                break;
            case MotionEvent.ACTION_UP:
                if (scrollView != null)
                    scrollView.requestDisallowInterceptTouchEvent(false);
                onAnim = true;
                if (!onSlip) {
                    nowChoose = !nowChoose;
                    if (ChgLsn != null) {
                        ChgLsn.OnChanged(nowChoose, v);
                    }
                    break;
                }
                int buttonCenterPoint = (ivSlipBtn.getLeft() + ivSlipBtn.getRight()) / 2;
                int bgCenterPoint = ivSlipFrame.getWidth() / 2;
                if (nowChoose && buttonCenterPoint < bgCenterPoint ||
                        !nowChoose && buttonCenterPoint > bgCenterPoint) {
                    nowChoose = !nowChoose;
                }
                if (ChgLsn != null) {
                    ChgLsn.OnChanged(nowChoose, v);
                }
                onSlip = false;
                break;
            case MotionEvent.ACTION_CANCEL:
                if (scrollView != null)
                    scrollView.requestDisallowInterceptTouchEvent(false);
        }
        change();
        invalidate();
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        change();
    }

    private void change() {
        if (onAnim) {
            int originalLeft = left;
            left = nowChoose ? ivSlipFrame.getWidth() - ivSlipBtn.getWidth() : 0;

            ivSlipBtn.layout(
                    left,
                    ivSlipBtn.getTop(),
                    left + ivSlipBtn.getWidth(),
                    ivSlipBtn.getTop() + ivSlipBtn.getBottom());
            ivSlipBg.layout(
                    left + ivSlipBtn.getWidth() / 2 - ivSlipBg.getWidth() / 2,
                    ivSlipBtn.getTop(),
                    left + ivSlipBtn.getWidth() / 2 + ivSlipBg.getWidth() / 2,
                    ivSlipBtn.getTop() + ivSlipBtn.getBottom());

            ta = new TranslateAnimation(
                    TranslateAnimation.ABSOLUTE, originalLeft - left,
                    TranslateAnimation.RELATIVE_TO_SELF, 0,
                    TranslateAnimation.RELATIVE_TO_SELF, 0,
                    TranslateAnimation.RELATIVE_TO_SELF, 0);
            ta.setDuration(250 * Math.abs(originalLeft - left) / ivSlipFrame.getWidth());
            ivSlipBtn.startAnimation(ta);
            ivSlipBg.startAnimation(ta);

            return;
        }

        if (!onSlip) {
            left = nowChoose ? ivSlipFrame.getWidth() - ivSlipBtn.getWidth() : 0;
        }

        ivSlipBtn.layout(
                left,
                ivSlipBtn.getTop(),
                left + ivSlipBtn.getWidth(),
                ivSlipBtn.getTop() + ivSlipBtn.getBottom());
        ivSlipBg.layout(
                left + ivSlipBtn.getWidth() / 2 - ivSlipBg.getWidth() / 2,
                ivSlipBtn.getTop(),
                left + ivSlipBtn.getWidth() / 2 + ivSlipBg.getWidth() / 2,
                ivSlipBtn.getTop() + ivSlipBtn.getBottom());
    }

    public void setScrollView(ScrollView sv) {
        scrollView = sv;
    }

    public void setCheckState(boolean checkState) {
        nowChoose = checkState;
    }

    public boolean getCheckState() {
        return nowChoose;
    }

    public void SetOnChangedListener(OnChangedListener l) {// 设置监听器,当状态修改的时候
        ChgLsn = l;
    }

    public interface OnChangedListener {
        abstract void OnChanged(boolean checkState, View v);
    }
}
