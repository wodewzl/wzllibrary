
package com.bs.bsims.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

@SuppressLint("DrawAllocation")
public class BSImgViewFliePoint extends ImageView {

    private int measureWidht;
    private int measureHeight;
    private int radius;
    private int badgeNumber;
    private Context mContext;
    private int mScreenWid;

    /**
     * 控制红点是否显示
     */
    private boolean mShowFlag;

    public BSImgViewFliePoint(Context context) {
        super(context);
        this.mContext = context;
    }

    public BSImgViewFliePoint(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public BSImgViewFliePoint(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
    }

    public void getScreenWid() {
        // 方法1 Android获得屏幕的宽和高
        Activity ac = (Activity) mContext;
        WindowManager windowManager = ac.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        mScreenWid = display.getWidth();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        measureWidht = measureWidht(widthMeasureSpec);
        measureHeight = measureHeight(heightMeasureSpec);
        setMeasuredDimension(measureWidht, measureHeight);
        // Log.e("PointImageView", "onMeasure() -> width = " + measureWidht + " , height = " +
        // measureHeight);

        // radius = measureWidht * 1 / 4;
        getScreenWid();
        // if(mScreenWid>)
        radius = 22 * mScreenWid / 1080;
        // Log.e("PointImageView", "radius = " + radius);
    }

    private int measureWidht(int widthMeasureSpec) {
        int result = 0;

        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY) { // 如果是精确的尺寸
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            // Log.e("PointImageView", "测量宽度时，测量出的mode是MeasureSpec.AT_MOST");
            Drawable drawable = getBackground();
            if (null != drawable) {
                result = drawable.getMinimumWidth();
            } else {
                result = getDrawable().getMinimumWidth();
            }
        }

        return result;
    }

    private int measureHeight(int heightMeasureSpec) {
        int result = 0;

        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY) { // 如果是精确的尺寸
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            // Log.e("PointImageView", "测量宽度时，测量出的mode是MeasureSpec.AT_MOST");
            Drawable drawable = getBackground();
            if (null != drawable) {
                result = drawable.getMinimumHeight();
            } else {
                result = getDrawable().getMinimumHeight();
            }
        }

        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (badgeNumber != 0) {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            // 绘制右上角的圆圈
            paint.setColor(Color.RED);
            int circleX = measureWidht - radius;
            int circleY = radius;
            canvas.drawCircle(circleX, circleY, radius, paint);

            // 画文�?
            paint.setColor(Color.WHITE);
            paint.setTextSize(1.2f * radius);
            int textWidth = getTextWidth(paint, String.valueOf(badgeNumber));
            int textHeight = getTextHeight(paint, String.valueOf(badgeNumber));

            FontMetrics fontMetrics = paint.getFontMetrics();
            float bottomY = textHeight /*- 1.0f*fontMetrics.bottom*/; // 计算bottom线的位置，测试出来的值，不标�?

            // 绘制文本
            // Log.e("PointImageView", "测量字符串的高度，textHeight = " + textHeight);
            // canvas.drawText(String.valueOf(badgeNumber), circleX - (textWidth / 2), circleY +
            // (bottomY / 2), paint);
            canvas.drawText(String.valueOf(badgeNumber), circleX - (textWidth / 2), circleY + (bottomY / 3) + bottomY / 7, paint);

        }

        if (mShowFlag) {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            // 绘制右上角的圆圈
            paint.setColor(Color.RED);
            int circleX = measureWidht - radius + 10;
            int circleY = radius;
            int circleSize = radius / 3;
            canvas.drawCircle(circleX, circleY, circleSize, paint);

            // 画文�?
            paint.setColor(Color.WHITE);
            paint.setTextSize(1.2f * radius);
            int textWidth = getTextWidth(paint, String.valueOf(badgeNumber));
            int textHeight = getTextHeight(paint, String.valueOf(badgeNumber));

            FontMetrics fontMetrics = paint.getFontMetrics();
            float bottomY = textHeight /*- 1.0f*fontMetrics.bottom*/; // 计算bottom线的位置，测试出来的值，不标�?

            // 绘制文本
            canvas.drawText("", circleX - (textWidth / 2), circleY + (bottomY / 3) + bottomY / 7, paint);
        }
    }

    public void setBadgeNumber(int badgeNumber) {
        this.badgeNumber = badgeNumber;
        invalidate();
    }

    /**
     * 精确计算宽度
     * 
     * @param paint
     * @param str
     * @return
     */
    private int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    /**
     * 粗略计算宽高
     * 
     * @param paint
     * @param str
     * @return
     */
    private int getTextHeight(Paint paint, String str) {
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        int w = rect.width();
        int h = rect.height();
        return h;
    }

    public boolean ismShowFlag() {
        return mShowFlag;
    }

    public void setmShowFlag(boolean mShowFlag) {
        this.mShowFlag = mShowFlag;
    }
}
