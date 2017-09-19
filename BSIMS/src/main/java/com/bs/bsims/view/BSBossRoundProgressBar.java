
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

public class BSBossRoundProgressBar extends View {
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

    private int allLength;// 圆直径
    private int roundY;// 高度

    public BSBossRoundProgressBar(Context context) {
        this(context, null);
    }

    public BSBossRoundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BSBossRoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
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
        // int radius = (int) (centre - roundWidth / 2); // 圆环的半径
        int radius = (int) (allLength / 2 - roundWidth / 2);
        if ("1".equals(status)) {
            paint.setColor(Color.parseColor("#00a9fe")); // 设置圆环的颜色00a9fe
        } else {
            paint.setColor(Color.parseColor("#336A91")); // 设置圆环的颜色ffffff
        }

        paint.setStyle(Paint.Style.FILL); // 设置实心
        // paint.setStrokeWidth(roundWidth); // 设置圆环的宽度
        paint.setAntiAlias(true); // 消除锯齿
        canvas.drawCircle(centre, roundY / 2, radius, paint); // 画出圆环

        /**
         * 画最外层的大圆环
         */
        centre = CommonUtils.getScreenWidth(mContext) / 4; // 获取圆心的x坐标
        // radius = (int) (centre - roundWidth); // 圆环的半径
        radius = (int) (allLength / 2 - roundWidth);
        paint.setColor(roundColor); // 设置圆环的颜色
        paint.setStyle(Paint.Style.STROKE); // 设置空心
        paint.setStrokeWidth(roundWidth); // 设置圆环的宽度
        paint.setAntiAlias(true); // 消除锯齿
        canvas.drawCircle(centre, roundY / 2, radius, paint); // 画出圆环

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
            canvas.drawText(prenct, centre - textWidth / 2, centre - radius + radius / 4, paint); //

            // 画出进度百分比
        }

        /**
         * 画目标完成率和中间的view线条
         **/

        paint.setStrokeWidth(1);
        paint.setColor(getResources().getColor(R.color.white));
        paint.setTextSize(getHeight() / 10);
        paint.setTypeface(Typeface.DEFAULT);
        paint.setAntiAlias(true); // 消除锯齿

        float textWidth2 = paint.measureText("目标完成率");
        canvas.drawLine(centre - (textWidth2 * 3) / 4, roundY / 2,
                centre + (textWidth2 * 3) / 4, roundY / 2, paint);
        canvas.drawText("目标完成率", centre - textWidth2 / 2, roundY / 2 + roundY / 5, paint);

        /**
         * 画圆弧 ，画圆环的进度
         */

        // 设置进度是实心还是空心
        paint.setStrokeWidth(roundWidth); // 设置圆环的宽度
        paint.setColor(roundProgressColor); // 设置进度的颜色
        RectF oval = new RectF(centre - radius, roundY / 2 - radius, centre
                + radius, roundY / 2 + radius); // 用于定义的圆弧的形状和大小的界限

        switch (style) {
            case STROKE: {
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(oval, 0, 360 * progress / max, false, paint); // 根据进度画圆弧
                break;
            }
            case FILL: {
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                if (progress != 0)
                    // canvas.rotate(360 * progress / max, centre, roundY / 2);
                    canvas.drawArc(oval, 0, 360 * progress / max, true, paint); // 根据进度画圆弧
                break;
            }
        }
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
            heightSpecSize = 0;
        }

        if (widthSpecMode == MeasureSpec.UNSPECIFIED) {
        }

        int width, height;
        width = Math.max(getMeasuredWidth(), widthSpecSize);
        height = Math.max(getMeasuredHeight(), heightSpecSize);
        roundY = height;
        if (width > height) {
            allLength = height;
        } else {
            allLength = width;
        }
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
