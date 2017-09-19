
package com.bs.bsims.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
public class BSLinearLayout extends LinearLayout {
    private Context mContext;
    private int mHight;

    public BSLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    public BSLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;

    }

    public BSLinearLayout(Context context) {
        super(context);
        mContext = context;

    }

    public void initView(Context context) {
        TextView tv = new TextView(context);
        tv.setText("全文······");
        this.addView(tv);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode != MeasureSpec.EXACTLY) {
            widthSpecSize = 0;
        }

        if (heightSpecMode != MeasureSpec.EXACTLY) {
            heightSpecSize = 0;
        }

        if (widthSpecMode == MeasureSpec.UNSPECIFIED
                || heightSpecMode == MeasureSpec.UNSPECIFIED) {
        }

        // 控件的最大高度，就是下边tab的背景最大高度
        // 控件的最大高度，就是下边tab的背景最大高度
        int width = Math.max(getMeasuredWidth(), widthSpecSize);
        int height = Math.max(getMeasuredHeight(), heightSpecSize);

        if (height > 320) {
            setMeasuredDimension(width, 320);
            mHight = 320;
        } else {
            setMeasuredDimension(width, height);
            mHight = height;
        }
    }

    public int getmHight() {
        return mHight;
    }

    public void setmHight(int mHight) {
        this.mHight = mHight;
    }

}
