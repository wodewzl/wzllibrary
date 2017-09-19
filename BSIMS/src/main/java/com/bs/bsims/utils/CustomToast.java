
package com.bs.bsims.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.bsims.R;

public class CustomToast {
    public static Toast toast;
    private static TextView txtToastContent;

    @SuppressLint("NewApi")
    private static Toast createToast(Context context, String message, int duration) {
        if (null == toast) {
            toast = new Toast(context);
//            View view = View.inflate(context, R.layout.custom_toast, null);
            TextView view= new TextView(context);
            view.setPadding(CommonUtils.dip2px(context, 8),CommonUtils.dip2px(context, 8),CommonUtils.dip2px(context, 8),CommonUtils.dip2px(context, 8));
            view.setBackground(CommonUtils.setBackgroundShap(context, 5, R.color.C7, R.color.C7));
//            txtToastContent = (TextView) view.findViewById(R.id.txt_toast_msg);
            view.setTextColor(context.getResources().getColor(R.color.C1));

            view.setText(message);
            toast.setView(view);
            toast.setDuration(duration);
            toast.setGravity(Gravity.BOTTOM, 0, CommonUtils.dip2px(context, 160));
        }
//        txtToastContent.setText(message);
        return toast;
    }

    public static void showLongToast(Context context, String message) {
        createToast(context, message, 1).show();
    }

    public static void showShortToast(Context context, String message) {
        createToast(context, message, 0).show();
    }

    /**
     * 请求网络时，onError方法中展示的吐司
     * 
     * @param context
     */
    public static void showNetErrorToast(Context context) {
        createToast(context, "网络出错，请检查网络再试", 0).show();
    }

    // private WindowManager mWdm;
    // private View mToastView;
    // private WindowManager.LayoutParams mParams;
    // private Timer mTimer;
    // private boolean mShowTime;// 记录Toast的显示长短类型
    // private boolean mIsShow;// 记录当前Toast的内容是否已经在显示
    //
    // public static final boolean LENGTH_LONG = true;
    // public static final boolean LENGTH_SHORT = false;
    //
    // private CustomToast(Context context, String text, boolean showTime) {
    // mShowTime = showTime;// 记录Toast的显示长短类型
    // mIsShow = false;// 记录当前Toast的内容是否已经在显示
    // mWdm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    // // 通过Toast实例获取当前android系统的默认Toast的View布局
    // // mToastView = Toast.makeText(context, text, Toast.LENGTH_SHORT).getView();
    // mToastView = View.inflate(context, R.layout.custom_toast, null);
    // TextView msg = (TextView) mToastView.findViewById(R.id.txt_toast_msg);
    // msg.setText(text);
    // mTimer = new Timer();
    // // 设置布局参数
    // setParams();
    // }
    //
    // private void setParams() {
    // mParams = new WindowManager.LayoutParams();
    // mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
    // mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
    // mParams.format = PixelFormat.TRANSLUCENT;
    // mParams.windowAnimations = R.style.anim_view;// 设置进入退出动画效果
    // mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
    // mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
    // | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
    // | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
    // mParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
    // mParams.y = 250;
    // }
    //
    // public static CustomToast createToast(Context context, String text, boolean showTime) {
    // CustomToast result = new CustomToast(context, text, showTime);
    // return result;
    // }
    //
    // public static void showLongToast(Context context, String message) {
    // createToast(context, message, true).show();
    // }
    //
    // public static void showShortToast(Context context, String message) {
    // createToast(context, message, false).show();
    // }
    //
    // public void show() {
    // if (!mIsShow) {// 如果Toast没有显示，则开始加载显示
    // mIsShow = true;
    // mWdm.addView(mToastView, mParams);// 将其加载到windowManager上
    // mTimer.schedule(new TimerTask() {
    // @Override
    // public void run() {
    // mWdm.removeView(mToastView);
    // mIsShow = false;
    // }
    // }, (long) (mShowTime ? 3000 : 2000));
    // }
    // }
    //
    // public void cancel() {
    // if (mTimer == null) {
    // mWdm.removeView(mToastView);
    // mTimer.cancel();
    // }
    // mIsShow = false;
    // }
    //
    // public static void showNetErrorToast(Context context) {
    // createToast(context, "网络出错，请检查网络再试", true).show();
    // }

}
