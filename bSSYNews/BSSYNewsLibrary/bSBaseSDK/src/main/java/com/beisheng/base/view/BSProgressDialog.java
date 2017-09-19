
package com.beisheng.base.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.beisheng.base.R;

public class BSProgressDialog {
    private static final String TAG = "CustomDialog";
    public static Dialog waitDialog;
    public static ProgressBar mProgressBar;

    public static Dialog getInstance(Context context) {
        if (waitDialog == null) {
            waitDialog = new Dialog(context, R.style.new_circle_progress);
            View view = View.inflate(context, R.layout.progress_dialog, null);
            mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
            waitDialog.setContentView(view);
            waitDialog.setCanceledOnTouchOutside(false);
        }
        return waitDialog;
    }

    /**
     * 请求网络时，弹出dialog
     * 
     * @param context
     */
    // preference_category
    public static void showProgressDialog(Context context) {
        if (waitDialog != null) {
            if (!waitDialog.isShowing()) {
                waitDialog.show();
            }
        }
    }

    public static void closeProgressDialog() {
        if (waitDialog != null) {
            waitDialog.dismiss();
            waitDialog = null;
            System.gc();
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     * 
     * @param bgAlpha
     */
    public static void backgroundAlphaStart(Activity context)
    {

        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = (float) 0.5; // 0.0-1.0
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    /**
     * 设置添加屏幕的背景透明度
     * 
     * @param bgAlpha
     */
    public static void backgroundAlphaEnd(Activity context)
    {

        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = 1; // 0.0-1.0
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    public static void setProgressDrable(Drawable d) {
        mProgressBar.setIndeterminateDrawable(d);
    }

}
