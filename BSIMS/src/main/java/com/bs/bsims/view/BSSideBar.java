
package com.bs.bsims.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.bs.bsims.utils.CommonUtils;

public class BSSideBar extends View {
    // 触摸事件
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    // 26个字母
    public String[] letters = {
            "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"
    };
    private int choose = -1;// 选中
    private Paint paint = new Paint();

    private TextView mTextDialog;
    private Context mContext;
    private String[] newLetter;

    public void setTextView(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }

    public BSSideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
    }

    public BSSideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public BSSideBar(Context context) {
        super(context);
        this.mContext = context;
    }

    /**
     * 重写这个方法
     */
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 获取焦点改变背景颜色.
        int height = getHeight();// 获取对应高度
        int width = getWidth(); // 获取对应宽度
        int singleHeight = 0;
        // int singleHeight = height / letters.length;// 获取每一个字母的高度
        if (newLetter != null) {
            singleHeight = CommonUtils.dip2px(mContext, 17);// 获取每一个字母的高度
        } else {
            singleHeight = height / letters.length;// 获取每一个字母的高度
        }
        for (int i = 0; i < letters.length; i++) {
            paint.setColor(Color.rgb(146, 211, 254));
            // paint.setColor(Color.WHITE);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);
            paint.setTextSize(26);
            // 选中的状态
            if (i == choose) {
                paint.setColor(Color.parseColor("#3399ff"));
                paint.setFakeBoldText(true);
            }
            // x坐标等于中间-字符串宽度的一半.
            float xPos = width / 2 - paint.measureText(letters[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(letters[i], xPos, yPos, paint);
            paint.reset();// 重置画笔
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();// 点击y坐标
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int c = (int) (y / getHeight() * letters.length);// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.

        switch (action) {
            case MotionEvent.ACTION_UP:
                setBackgroundDrawable(new ColorDrawable(0x00000000));
                choose = -1;//
                invalidate();
                if (mTextDialog != null) {
                    mTextDialog.setVisibility(View.INVISIBLE);
                }
                break;

            // case MotionEvent.ACTION_MOVE:
            // setBackgroundDrawable(new ColorDrawable(0x00000000));
            // choose = -1;//
            // invalidate();
            // if (mTextDialog != null) {
            // mTextDialog.setVisibility(View.INVISIBLE);
            // }
            // break;

            case MotionEvent.ACTION_DOWN:
                if (oldChoose != c) {
                    if (c >= 0 && c < letters.length) {
                        if (listener != null) {
                            listener.onTouchingLetterChanged(letters[c]);
                        }
                        if (mTextDialog != null) {
                            mTextDialog.setText(letters[c]);
                            mTextDialog.setVisibility(View.VISIBLE);
                        }

                        choose = c;
                        invalidate();
                    }
                }

                break;
            default:
                // setBackgroundResource(R.drawable.sidebar_background);
                // if (oldChoose != c) {
                // if (c >= 0 && c < b.length) {
                // if (listener != null) {
                // listener.onTouchingLetterChanged(b[c]);
                // }
                // if (mTextDialog != null) {
                // mTextDialog.setText(b[c]);
                // mTextDialog.setVisibility(View.VISIBLE);
                // }
                //
                // choose = c;
                // invalidate();
                // }
                // }
                //
                setBackgroundDrawable(new ColorDrawable(0x00000000));
                choose = -1;//
                invalidate();
                if (mTextDialog != null) {
                    mTextDialog.setVisibility(View.INVISIBLE);
                }
                break;
        }
        return true;
    }

    /**
     * 向外公开的方法
     * 
     * @param onTouchingLetterChangedListener
     */
    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    /**
     * 接口
     * 
     * @author coder
     */
    public interface OnTouchingLetterChangedListener {
        public void onTouchingLetterChanged(String s);
    }

    public String[] getNewLetter() {
        return newLetter;
    }

    public void setNewLetter(String[] newLetter) {
        this.newLetter = newLetter;
        letters = newLetter;
    }

    // @Override
    // protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    // int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
    // int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
    //
    // int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
    // int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
    //
    // if (widthSpecMode != MeasureSpec.EXACTLY) {
    // widthSpecSize = 0;
    // }
    //
    // if (heightSpecMode != MeasureSpec.EXACTLY) {
    // heightSpecSize = 0;
    // }
    //
    // if (widthSpecMode == MeasureSpec.UNSPECIFIED
    // || heightSpecMode == MeasureSpec.UNSPECIFIED) {
    // }
    //
    // // 控件的最大高度，就是下边tab的背景最大高度
    // // 控件的最大高度，就是下边tab的背景最大高度
    // int width = Math.max(getMeasuredWidth(), widthSpecSize);
    // int height = Math.max(getMeasuredHeight(), heightSpecSize);
    // setMeasuredDimension(width, height);
    // }

}
