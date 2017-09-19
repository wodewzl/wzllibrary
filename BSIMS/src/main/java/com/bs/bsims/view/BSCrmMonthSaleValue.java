
package com.bs.bsims.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.bs.bsims.R;
import com.bs.bsims.utils.CommonUtils;

public class BSCrmMonthSaleValue extends View {
    private Context context;

    private float data1 = 0;// 百分率

    public float getData1() {
        return data1;
    }

    public void setData1(float data1) {
        this.data1 = data1;
    }

    public BSCrmMonthSaleValue(Context context) {
        super(context, null);
        this.context = context;
    }

    public BSCrmMonthSaleValue(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float a = data1 * 180;// 得到旋转角度
        // float b = dip2px(context, 10);// 该view到顶部的距离
        float b = 0;
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        RectF oval = new RectF(getWidth() / 6, b, 5 * getWidth() / 6, 2 * getWidth() / 3 + b);// 正方形，可以这样想，在正方形中画内切圆

        // 绘制外扇形1，r=getWidth() / 2,圆心（mWidth / 2s，getWidth() / 3 + b）,getWidth()是控件的宽度
        paint.setARGB(255, 0, 169, 254);// 蓝色
        canvas.drawArc(oval, 180, 180, true, paint);// 画扇形

        // 绘制外扇形2，oval扇形的正方形区域，顺时针画圆
        paint.setARGB(255, 254, 219, 93);// 黄色
        canvas.drawArc(oval, 180, a, true, paint);

        // 绘制外扇形3，r=getWidth() / 6,圆心（mWidth / 2s，getWidth() / 3 + b）
        paint.setARGB(255, 255, 255, 255);// 白色
        RectF oval1 = new RectF(getWidth() / 3, getWidth() / 6 + b, 2 * getWidth() / 3, getWidth() / 2 + b);// 正方形
        canvas.drawArc(oval1, 180, 180, true, paint);

        // // 一条测试线，确定是否有一半
        // paint.reset();
        // paint.setColor(Color.RED);
        // canvas.drawLine(0, getWidth() / 3 + b, getWidth(), getWidth() / 3 + b, paint);

        // 添加指针，并且可旋转
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        canvas.rotate(a - 90, getWidth() / 2, getWidth() / 3 + b);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.crm_statics_zhizheng);
        canvas.drawBitmap(bitmap, getWidth() / 2 - bitmap.getWidth() / 2, getWidth() / 3 + b - bitmap.getHeight(), paint);

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
        int width;
        int height;
        width = Math.max(getMeasuredWidth(), widthSpecSize);
        int m = CommonUtils.dip2px(context, 6);
        setMeasuredDimension(width, width / 3 + m);
    }
}
