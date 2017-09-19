
package com.bs.bsims.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.bs.bsims.R;
import com.bs.bsims.utils.CommonUtils;

/**
 * 仿iphone带进度的进度条，线程安全的View，可直接在线程中更新进度
 * 
 * @author xiaanming
 */
public class BSRoundProgressBarToCrm extends View {
    /**
     * 画笔对象的引用
     */
    private Paint paint;

    /**
     * 圆环的颜色
     */
    private int roundColor;

    /**
     * 圆环进度的颜色
     */
    private int roundProgressColor;

    /**
     * 中间进度百分比的字符串的颜色
     */
    private int textColor;

    /**
     * 中间进度百分比的字符串的字体
     */
    private float textSize;
    private float textSizeCopty;

    /**
     * 圆环的宽度
     */
    private float roundWidth;

    /**
     * 最大进度
     */
    private int max;

    /**
     * 当前进度
     */
    private int progress;
    /**
     * 是否显示中间的进度
     */
    private boolean textIsDisplayable;

    /**
     * 设置圆形进度条内的进度的数值是接口返回的数据，
     **/

    private String prenct;

    /**
     * 进度的风格，实心或者空心
     */
    private int style;

    public static final int STROKE = 0;
    public static final int FILL = 1;

    public String status = "1";// 1为点击2为非点击状态
    private Context mContext;

    private String price1 = "1230000000";// 本月目标
    private String price2 = "4000000001";// 本月签单
    private String price3 = "5433333000";// 本月回款

    public String getPrice1() {
        return price1;
    }

    public void setPrice1(String price1) {
        this.price1 = price1;
    }

    public String getPrice2() {
        return price2;
    }

    public void setPrice2(String price2) {
        this.price2 = price2;
    }

    public String getPrice3() {
        return price3;
    }

    public void setPrice3(String price3) {
        this.price3 = price3;
    }

    public BSRoundProgressBarToCrm(Context context) {
        this(context, null);
    }

    public BSRoundProgressBarToCrm(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BSRoundProgressBarToCrm(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // paint.setAntiAlias(true); // 消除锯齿
        this.mContext = context;
        paint = new Paint();

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.RoundProgressBar);

        // 获取自定义属性和默认值
        roundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.RED);
        roundProgressColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor,
                Color.GREEN);
        textColor = mTypedArray.getColor(R.styleable.RoundProgressBar_textColor, Color.GREEN);
        textSize = mTypedArray.getDimension(R.styleable.RoundProgressBar_textSize, 15);
        roundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 5);
        max = mTypedArray.getInteger(R.styleable.RoundProgressBar_max, 100);
        textIsDisplayable = mTypedArray.getBoolean(R.styleable.RoundProgressBar_textIsDisplayable,
                true);
        style = mTypedArray.getInt(R.styleable.RoundProgressBar_style, 0);

        mTypedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /**
         * 画实圆
         */
        int centre = CommonUtils.getScreenWidth(mContext) / 4; // 获取圆心的x坐标
        int radius = (int) (centre - roundWidth / 2); // 圆环的半径
        if ("1".equals(status)) {
            paint.setColor(Color.parseColor("#00a9fe")); // 设置圆环的颜色
        } else {
            paint.setColor(Color.parseColor("#ffffff")); // 设置圆环的颜色
        }

        paint.setStyle(Paint.Style.FILL); // 设置实心
        // paint.setStrokeWidth(roundWidth); // 设置圆环的宽度
        paint.setAntiAlias(true); // 消除锯齿
        canvas.drawCircle(centre, centre, radius, paint); // 画出圆环

        /**
         * 画最外层的大圆环
         */
        centre = CommonUtils.getScreenWidth(mContext) / 4; // 获取圆心的x坐标
        radius = (int) (centre - roundWidth); // 圆环的半径
        paint.setColor(roundColor); // 设置圆环的颜色
        paint.setStyle(Paint.Style.STROKE); // 设置空心
        paint.setStrokeWidth(roundWidth); // 设置圆环的宽度
        paint.setAntiAlias(true); // 消除锯齿
        canvas.drawCircle(centre, centre, radius, paint); // 画出圆环

        Log.e("log", centre + "");

        /**
         * 画进度百分比
         */
        paint.setStrokeWidth(2);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setTypeface(Typeface.DEFAULT_BOLD); // 设置字体
        int percent = (int) (((float) progress / (float) max) * 100); // 中间的进度百分比，先转换成float在进行除法运算，不然都为0
        float textWidth = paint.measureText(percent + "%"); // 测量字体宽度，我们需要根据字体的宽度设置在圆环中间
        if (textIsDisplayable && percent != 0 && style == STROKE) {
            paint.setStyle(Paint.Style.FILL); // 设置实心
            // canvas.drawText(percent + "%", centre - textWidth / 2, centre - textWidth, paint); //
            // 这里就先不用这个了
            canvas.drawText(prenct, centre - textWidth / 2, centre - textWidth, paint); //

            // 画出进度百分比
        }

        /**
         * 画本月完成和下面的view线条
         **/

        paint.setStrokeWidth(1);
        paint.setColor(getResources().getColor(R.color.black_01));
        paint.setTextSize(getHeight() / 13);
        paint.setTypeface(Typeface.DEFAULT);
        paint.setAntiAlias(true); // 消除锯齿
        canvas.drawText("本月完成", centre - textWidth / 2 - textWidth / 8,
                centre - textWidth / 2, paint); //
        canvas.drawLine(CommonUtils.getScreenWidth(mContext) / 9, centre - textWidth / 4,
                getWidth() / 2 - getWidth() / 8 + 30, centre - textWidth / 4, paint);

        /**
         * 画圆弧 ，画圆环的进度
         */

        // 设置进度是实心还是空心
        paint.setStrokeWidth(roundWidth); // 设置圆环的宽度
        paint.setColor(roundProgressColor); // 设置进度的颜色
        RectF oval = new RectF(centre - radius, centre - radius, centre
                + radius, centre + radius); // 用于定义的圆弧的形状和大小的界限

        switch (style) {
            case STROKE: {
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(oval, 0, 360 * progress / max, false, paint); // 根据进度画圆弧
                break;
            }
            case FILL: {
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                if (progress != 0)
                    canvas.drawArc(oval, 0, 360 * progress / max, true, paint); // 根据进度画圆弧
                break;
            }
        }

        /**
         * 画圆点和签单记录
         **/
        paint.setAntiAlias(true); // 消除锯齿
        paint.setStyle(Paint.Style.FILL); // 设置实心
        paint.setStrokeWidth(1);
        paint.setColor(Color.parseColor("#FFC100"));
        canvas.drawCircle(centre - textWidth / 2 - textWidth / 8, getHeight() / 2, CommonUtils.dip2px(mContext, 5), paint);
        canvas.drawText("本月签单", centre - textWidth / 2 + textWidth / 6, getHeight() / 2 + CommonUtils.dip2px(mContext, 6), paint);

        /**
         * 画圆点和本月回款s
         **/
        paint.setAntiAlias(true); // 消除锯齿
        paint.setStyle(Paint.Style.FILL); // 设置实心
        paint.setStrokeWidth(1);
        paint.setColor(Color.parseColor("#067E00"));
        canvas.drawCircle(centre - textWidth / 2 - textWidth / 8,
                getHeight() / 2 + getHeight() / 6, CommonUtils.dip2px(mContext, 5), paint);
        canvas.drawText("本月回款", centre - textWidth / 2 + textWidth / 6, getHeight() / 2
                + getHeight() / 5, paint);
        /**
         * 画短部分斜线 黄色 （本月签单）
         */

        String test = "￥1234567890元";// 测试数据，仅供粗略求每个文字的宽
        int testLeng = test.length();
        float avlength = (3 * getWidth()) / (8 * testLeng);// 每个文字的宽
        int lenPrice2 = price2.length();
        float xPrice2 = lenPrice2 * avlength;// 传过来的字符串所占的宽

        paint = new Paint();
        paint.setColor(Color.parseColor("#FFC100"));// 设置黄色
        // paint.setStyle(Paint.Style.STROKE); // 设置空心

        paint.setStrokeWidth(CommonUtils.getScreenHigh(mContext) * 4 / 1920);
        paint.setAntiAlias(true); // 消除锯齿
        // 折线拐角处x坐标=末端坐标-传过来字符串所占的宽（拐角坐标是可变的）
        canvas.drawLine(getWidth() / 4 + getWidth() / 8, getHeight() / 2 + 12,
                getWidth() - getWidth() / 16 - xPrice2, getHeight() / 2 + getHeight() / 8
                        - getHeight() / 20, paint);
        canvas.drawLine(getWidth() - getWidth() / 16 - xPrice2, getHeight() / 2 + getHeight() / 8
                - getHeight() / 20, getWidth() - getWidth() / 16, getHeight() / 2 + getHeight() / 8
                - getHeight()
                / 20,
                paint);

        paint.setTextSize(getHeight() / 10);
        canvas.drawText(price2, getWidth() - getWidth() / 16 - xPrice2, getHeight() / 2
                + getHeight() / 8 - getHeight() / 20 - getHeight() / 32, paint);

        /**
         * 画短部分斜线 绿色 （本月回款）
         */

        int lenPrice3 = price3.length();
        float xPrice3 = lenPrice3 * avlength;

        paint = new Paint();
        paint.setColor(Color.parseColor("#067E00"));// 设置绿色
        // paint.setStyle(Paint.Style.STROKE); // 设置空心
        paint.setStrokeWidth(CommonUtils.getScreenHigh(mContext) * 4 / 1920);
        paint.setAntiAlias(true); // 消除锯齿
        // canvas.drawLine((getWidth() / 2 + getWidth() / 4), getHeight() - getHeight() / 5,
        // getWidth() + getWidth() * 3, 1 + getHeight() / 2 - getHeight() / 4, paint);
        // canvas.drawLine(getWidth() / 4 + getWidth() / 8, getHeight() / 2 + getHeight() / 3,
        // getWidth() / 2 + getWidth() / 16, getHeight() / 2 + getHeight() / 3 - getHeight() / 20,
        // paint);
        // canvas.drawLine(getWidth() / 2 + getWidth() / 16, getHeight() / 2 + getHeight() / 3 -
        // getHeight() / 20, getWidth() - getWidth() / 16, getHeight() / 2 + getHeight() / 3 -
        // getHeight() / 20,
        // paint);
        // paint.setTextSize(getHeight() / 10);
        // canvas.drawText(price3, getWidth() / 2 + getWidth() / 14, getHeight() / 2 + getHeight() /
        // 3 - getHeight() / 20 - getHeight() / 32, paint);

        canvas.drawLine(getWidth() / 4 + getWidth() / 8, getHeight() / 2 + getHeight() / 5,
                getWidth() - getWidth() / 16 - xPrice3, getHeight() / 2 + getHeight() / 3
                        - getHeight() / 20, paint);
        canvas.drawLine(getWidth() - getWidth() / 16 - xPrice3, getHeight() / 2 + getHeight() / 3
                - getHeight() / 20, getWidth() - getWidth() / 16, getHeight() / 2 + getHeight() / 3
                - getHeight()
                / 20,
                paint);
        paint.setTextSize(getHeight() / 10);
        canvas.drawText(price3, getWidth() - getWidth() / 16 - xPrice3, getHeight() / 2
                + getHeight() / 3 - getHeight() / 20 - getHeight() / 32, paint);

        /**
         * 画短部分斜线 蓝色 (本月完成)
         */
        paint = new Paint();
        paint.setColor(Color.parseColor("#00a9fe"));// 设置黄色
        // paint.setStyle(Paint.Style.STROKE); // 设置空心

        paint.setStrokeWidth(CommonUtils.getScreenHigh(mContext) * 4 / 1920);
        paint.setAntiAlias(true); // 消除锯齿
        // canvas.drawLine(getWidth() / 4 + getWidth() / 8, getHeight() / 2 - getHeight() / 8,
        // getWidth() / 2 + getWidth() / 16, getHeight() / 2 - getHeight() / 8 - getHeight() / 12,
        // paint);
        // canvas.drawLine(getWidth() / 2 + getWidth() / 16, getHeight() / 2 - getHeight() / 8 -
        // getHeight() / 12, getWidth(), getHeight() / 2 - getHeight() / 8 - getHeight() / 12,
        // paint);

        int lenPrice1 = price1.length();
        float xPrice1 = lenPrice1 * avlength;
        paint.setTextSize(getHeight() / 10);
        canvas.drawText(price1, getWidth() - getWidth() / 16 - xPrice1, getHeight() / 5, paint);
        paint.setColor(Color.BLACK);
        String textStr = "本月目标";
        int strLength = textStr.length();
        float xText = strLength * avlength;
        paint.setTextSize(getHeight() / 13);
        // 由于字符串是由数字和文字组成的，所以求出的每个字符所占的宽有误差，以至于不能达到完全对齐
        canvas.drawText(textStr, getWidth() / 2 + getWidth() / 4 - getWidth() / 80, getHeight() / 5
                + getHeight() / 8, paint);
    }

    public synchronized int getMax() {
        return max;
    }

    /**
     * 设置进度的最大值
     * 
     * @param max
     */
    public synchronized void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    /**
     * 获取进度.需要同步
     * 
     * @return
     */
    public synchronized int getProgress() {
        return progress;
    }

    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步 刷新界面调用postInvalidate()能在非UI线程刷新
     * 
     * @param progress
     */
    public synchronized void setProgress(int progress, String status) {
        this.status = status;
        if (progress < 0) {
            throw new IllegalArgumentException("progress not less than 0");
        }
        if (progress > max) {
            progress = max;
        }
        if (progress <= max) {
            this.progress = progress;
            postInvalidate();
        }

    }

    public int getCricleColor() {
        return roundColor;
    }

    public void setCricleColor(int cricleColor) {
        this.roundColor = cricleColor;
    }

    public int getCricleProgressColor() {
        return roundProgressColor;
    }

    public void setCricleProgressColor(int cricleProgressColor) {
        this.roundProgressColor = cricleProgressColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public float getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }

    public String getPrenct() {
        return prenct;
    }

    public void setPrenct(String prenct) {
        this.prenct = prenct;
    }

}
