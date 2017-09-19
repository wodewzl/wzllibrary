
package com.beisheng.synews.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.beisheng.base.utils.BaseCommonUtils;

public class BSImageViewAutoScale extends ImageView {
    private final String TAG = "MainActivity";

    private int screenWidth;
    private int screenHeight;
    private int displayWidth;
    private int displayHeight;

    public BSImageViewAutoScale(Context context) {
        this(context, null);
    }

    public BSImageViewAutoScale(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    BSImageViewAutoScale(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // init();
    }

    private void init() {
        screenWidth = BaseCommonUtils.getScreenWidth(getContext());
        screenHeight = BaseCommonUtils.getScreenHigh(getContext());
    }

    public void setSize(int bitmapWidth, int bitmapHeight) {
        // displayWidth = screenWidth;
        // displayHeight = screenHeight;

        // 计算出按比例拉伸后的宽度和高度
        // displayWidth = screenHeight * bitmapWidth / bitmapHeight;
        displayHeight = BaseCommonUtils.getViewWidth(this) * bitmapHeight / bitmapWidth;
        // 判断如果以图片高度拉伸到屏幕的高度,按照相应的拉伸比是否能够拉伸超过屏幕宽度或者等于屏幕宽度,否则以另一边为基准

        // if (displayWidth >= screenWidth) {
        // displayHeight = screenHeight;
        // } else {
        // displayWidth = screenWidth;
        // }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, displayHeight);
    }
}
