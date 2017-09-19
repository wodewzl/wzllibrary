
package com.bs.bsims.utils;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.bs.bsims.R;

import java.util.Timer;
import java.util.TimerTask;

public class CustomToasta {

    private WindowManager mWdm;
    private View mToastView;
    private WindowManager.LayoutParams mParams;
    private Timer mTimer;
    private boolean mShowTime;// 记录Toast的显示长短类型
    private boolean mIsShow;// 记录当前Toast的内容是否已经在显示

    public static final boolean LENGTH_LONG = true;
    public static final boolean LENGTH_SHORT = false;

    private CustomToasta(Context context, String text, boolean showTime) {
        mShowTime = showTime;// 记录Toast的显示长短类型
        mIsShow = false;// 记录当前Toast的内容是否已经在显示
        mWdm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        // 通过Toast实例获取当前android系统的默认Toast的View布局
        // mToastView = Toast.makeText(context, text, Toast.LENGTH_SHORT).getView();
        mToastView = View.inflate(context, R.layout.custom_toast, null);
        mTimer = new Timer();
        // 设置布局参数
        setParams();
    }

    private void setParams() {
        mParams = new WindowManager.LayoutParams();
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.windowAnimations = R.style.anim_view;// 设置进入退出动画效果
        mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        mParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
        mParams.y = 250;
    }

    public static CustomToasta createToast(Context context, String text, boolean showTime) {
        CustomToasta result = new CustomToasta(context, text, showTime);
        return result;
    }

    public static void showLongToast(Context context, String message) {
        createToast(context, message, true).show();
    }

    public static void showShortToast(Context context, String message) {
        createToast(context, message, true).show();
    }

    public void show() {
        if (!mIsShow) {// 如果Toast没有显示，则开始加载显示
            mIsShow = true;
            mWdm.addView(mToastView, mParams);// 将其加载到windowManager上
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mWdm.removeView(mToastView);
                    mIsShow = false;
                }
            }, (long) (mShowTime ? 2500 : 1000));
        }
    }

    public void cancel() {
        if (mTimer == null) {
            mWdm.removeView(mToastView);
            mTimer.cancel();
        }
        mIsShow = false;
    }
}
