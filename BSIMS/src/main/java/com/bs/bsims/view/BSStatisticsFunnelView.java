
package com.bs.bsims.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.bs.bsims.utils.CommonUtils;

public class BSStatisticsFunnelView extends View {

    private float price1 = 100;// 梯形第一层高度
    private float price2 = 1000;// 梯形第二层高度
    private float price3 = 1000;// 梯形第三层高度
    private float price4 = 100;// 梯形第四层高度
    private float price5 = 220;// 梯形底部矩形高度
    private Boolean flag = false;// 主要用于在5个值都为空的情况，填充颜色为灰色
    private int leftWidth = CommonUtils.dip2px(getContext(), 5);
    private int mlineWidth;

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public float getPrice1() {
        return price1;
    }

    public void setPrice1(float price1) {
        this.price1 = price1;
    }

    public float getPrice2() {
        return price2;
    }

    public void setPrice2(float price2) {
        this.price2 = price2;
    }

    public float getPrice3() {
        return price3;
    }

    public void setPrice3(float price3) {
        this.price3 = price3;
    }

    public float getPrice4() {
        return price4;
    }

    public void setPrice4(float price4) {
        this.price4 = price4;
    }

    public float getPrice5() {
        return price5;
    }

    public void setPrice5(float price5) {
        this.price5 = price5;
    }

    public BSStatisticsFunnelView(Context context) {
        super(context);
    }

    public BSStatisticsFunnelView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int averHeight = getHeight() / 5;
        int contentWidth = getWidth() - mlineWidth;

        if (flag) {
            price1 = 100;
            price2 = 100;
            price3 = 100;
            price4 = 100;
        }

        Paint p = new Paint();
        /**
         * 把底部的正方形固定。 如果为0的话就不显示，但是5个值都为0就显示 ， getHeight() / 2，底部固定高
         */
        float y1 = 0;
        if (price5 == 0 && !flag) {
            y1 = getHeight();
            p.setColor(Color.parseColor("#eeeeee"));
        } else if (price5 == 0 && flag) {
            p.setColor(Color.parseColor("#eeeeee"));
            y1 = getHeight() - getHeight() * 2 / 15;
        } else if (price5 != 0) {
            y1 = getHeight() - getHeight() * 2 / 15;
            p.setColor(Color.parseColor("#2BB0FF"));
        }
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.FILL);
        canvas.drawRect(contentWidth / 3, y1, contentWidth - contentWidth / 3, getHeight(), p);// 赢单
        p.setStrokeWidth(5);// 线宽
        if ((price5 == 0 && flag) || price5 != 0) {
            // 在5个值都为空或者赢单price5不为0情况才画线
            canvas.drawLine(contentWidth - contentWidth / 3 + leftWidth, getHeight() - (getHeight() - y1) / 2, getWidth(), getHeight() - averHeight / 2, p);// 指向直线
        }

        // 画白色直线
        p.setColor(Color.WHITE);
        p.setStrokeWidth(3);
        canvas.drawLine(contentWidth / 3, y1, contentWidth - contentWidth / 3, y1, p);// 画直线

        // 当赢单高度确定后，初步接洽、需求确定、方案报价、谈判审核的总高度；用于根据比例求各个高度
        float allHeight = y1;

        // 谈判审核
        float y = (price4 * allHeight) / (price1 + price2 + price3 + price4);// 价格比*总高度
        float y2 = y1 - y;// 赢单的纵坐标-谈判审核对应的高度
        float x = (y * contentWidth) / (3 * y1);
        float x1 = contentWidth / 3 - x;
        float x2 = contentWidth - contentWidth / 3 + x;
        p.reset();// ����
        if (flag) {
            p.setColor(Color.parseColor("#eeeeee"));
        } else {
            p.setColor(Color.parseColor("#7ECEFF"));
        }
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.FILL);
        Path path1 = new Path();
        path1.moveTo(contentWidth / 3, y1);
        path1.lineTo(contentWidth - contentWidth / 3, y1);
        path1.lineTo(x2, y2);
        path1.lineTo(x1, y2);
        path1.close();
        canvas.drawPath(path1, p);
        p.setStrokeWidth(5);// 线宽
        if (price4 != 0 || flag) {
            canvas.drawLine(x2 - (x2 - contentWidth + contentWidth / 3) / 2 + leftWidth, y1 - (y1 - y2) / 2, getWidth(), getHeight() - averHeight / 2 - averHeight, p);// 指向直线
        }

        // 画白色直线
        p.setColor(Color.WHITE);
        p.setStrokeWidth(3);
        canvas.drawLine(x1, y2, x2, y2, p);

        // 方案报价
        float yy = (price3 * allHeight) / (price1 + price2 + price3 + price4);
        float y3 = y2 - yy;
        float xx = (yy * contentWidth) / (3 * y1);
        float xx1 = x1 - xx;
        float xx2 = x2 + xx;
        p.reset();// ����
        if (flag) {
            p.setColor(Color.parseColor("#eeeeee"));
        } else {
            p.setColor(Color.parseColor("#E98F6C"));
        }
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.FILL);
        Path path2 = new Path();
        path2.moveTo(x1, y2);
        path2.lineTo(x2, y2);
        path2.lineTo(xx2, y3);
        path2.lineTo(xx1, y3);
        path2.close();
        canvas.drawPath(path2, p);
        p.setStrokeWidth(5);// 线宽
        if (price3 != 0 || flag) {
            canvas.drawLine(xx2 - (xx2 - x2) / 2 + leftWidth, y2 - (y2 - y3) / 2, getWidth(), getHeight() - averHeight / 2 - averHeight * 2, p);// 指向直线
        }

        // 画白色直线
        p.setColor(Color.WHITE);
        p.setStrokeWidth(3);
        canvas.drawLine(xx1, y3, xx2, y3, p);

        // 需求确定
        float yyy = (price2 * allHeight) / (price1 + price2 + price3 + price4);
        float y4 = y3 - yyy;
        float xxx = (yyy * contentWidth) / (3 * y1);
        float xxx1 = xx1 - xxx;
        float xxx2 = xx2 + xxx;
        p.reset();// ����
        if (flag) {
            p.setColor(Color.parseColor("#eeeeee"));
        } else {
            p.setColor(Color.parseColor("#4DC1C1"));
        }
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.FILL);
        Path path3 = new Path();
        path3.moveTo(xx1, y3);
        path3.lineTo(xx2, y3);
        path3.lineTo(xxx2, y4);
        path3.lineTo(xxx1, y4);
        path3.close();
        canvas.drawPath(path3, p);
        p.setStrokeWidth(5);// 线宽
        if (price2 != 0 || flag) {
            canvas.drawLine(xxx2 - (xxx2 - xx2) / 2 + leftWidth, y3 - (y3 - y4) / 2, getWidth(), getHeight() - averHeight / 2 - averHeight * 3, p);// 指向直线
        }

        // 画白色直线
        p.setColor(Color.WHITE);
        p.setStrokeWidth(3);
        canvas.drawLine(xxx1, y4, xxx2, y4, p);

        // 初步接洽
        p.reset();// ����
        if (flag) {
            p.setColor(Color.parseColor("#eeeeee"));
        } else {
            p.setColor(Color.parseColor("#FFCC00"));
        }
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.FILL);
        Path path4 = new Path();
        path4.moveTo(0, 0);
        path4.lineTo(contentWidth, 0);
        path4.lineTo(xxx2, y4);
        path4.lineTo(xxx1, y4);
        path4.close();
        canvas.drawPath(path4, p);
        p.setStrokeWidth(5);// 线宽
        if (price1 != 0 || flag) {
            canvas.drawLine(contentWidth - (contentWidth - xxx2) / 2 + leftWidth, y4 / 2, getWidth(), averHeight / 2, p);// 指向直线
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);

        if (widthSpecMode != MeasureSpec.EXACTLY) {
            widthSpecSize = 0;
        }

        if (widthSpecMode == MeasureSpec.UNSPECIFIED) {
        }

        int width;
        width = Math.max(getMeasuredWidth(), widthSpecSize);
        mlineWidth = width / leftWidth;
    }
}
