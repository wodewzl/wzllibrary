
package com.bs.bsims.onekey.remove;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bs.bsims.onekey.remove.DropCover.OnDragCompeteListener;
import com.bs.bsims.utils.CommonUtils;

public class WaterDrop extends RelativeLayout {
    private Paint mPaint = new Paint();
    private TextView mTextView;
    private DropCover.OnDragCompeteListener mOnDragCompeteListener;
    private boolean mHolderEventFlag;
    private String mContent;
    private boolean mFlag = false;
    private Context mContext;

    public WaterDrop(Context context) {
        super(context);
        init();
        this.mContext = context;
    }

    public WaterDrop(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        this.mContext = context;
    }

    public void setText(String str) {
        mContent = str;
        invalidate();
    }

    public String getText() {
        return mContent;
    }

    public void setTextSize(int size) {
        mTextView.setTextSize(size);
    }

    @SuppressLint("NewApi")
    private void init() {
        mPaint.setAntiAlias(true);
        if (VERSION.SDK_INT > 11) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        mPaint.setColor(0xffff0000);

        if (mContent != null && Integer.parseInt(CommonUtils.isNormalData(mContent)) > 0) {
            mPaint.setColor(0xffff0000);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, mPaint);
            int textSize = 26 * CommonUtils.getScreenWidth(mContext) / 1080;
            mPaint.setTextSize(textSize);
            mPaint.setColor(Color.parseColor("#ffffff"));
            float textWidth = mPaint.measureText(mContent);
            canvas.drawText(mContent, getWidth() / 2 - textWidth / 2, getHeight() / 2 + getHeight() / 5, mPaint);
        }
        super.dispatchDraw(canvas);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        ViewGroup parent = getScrollableParent();
        if (Integer.parseInt(CommonUtils.isNormalData(mContent)) > 0) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mHolderEventFlag = !CoverManager.getInstance().isRunning();
                    if (mHolderEventFlag) {
                        if (parent != null)
                            parent.requestDisallowInterceptTouchEvent(true);

                        CoverManager.getInstance().start(this, event.getRawX(), event.getRawY(), mOnDragCompeteListener);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mHolderEventFlag) {
                        CoverManager.getInstance().update(event.getRawX(), event.getRawY());
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (mHolderEventFlag) {
                        if (parent != null)
                            parent.requestDisallowInterceptTouchEvent(false);
                        CoverManager.getInstance().finish(this, event.getRawX(), event.getRawY());
                    }
                    break;
            }
        }
        return true;
    }

    private ViewGroup getScrollableParent() {
        View target = this;
        while (true) {
            View parent;
            try {
                /**
                 * ViewRootImpl cannot be cast to android.view.View
                 */
                parent = (View) target.getParent();
            } catch (Exception e) {
                return null;
            }
            if (parent == null)
                return null;
            if (parent instanceof ListView || parent instanceof ScrollView) {
                return (ViewGroup) parent;
            }
            target = parent;
        }

    }

    public void setOnDragCompeteListener(OnDragCompeteListener onDragCompeteListener) {
        mOnDragCompeteListener = onDragCompeteListener;
    }
}
