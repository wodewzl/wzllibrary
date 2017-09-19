
package com.bs.bsims.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.ui.alert.YusAlertDialog;

public class CustomDialog {
    private static final String TAG = "CustomDialog";
    public static Dialog waitDialog;

    /**
     * 请求网络时，弹出dialog
     * 
     * @param context
     */
    // preference_category
    public static void showProgressDialog(Context context) {

        if (((Activity) context).isFinishing()) {
            waitDialog = null;
            return;
        }
        if (waitDialog == null) {
            // waitDialog = new ProgressDialog(context);
            // View view = View.inflate(context, R.layout.progress_dialog, null);
            // waitDialog.setView(view);
            // waitDialog.setMessage("玩命加载中……");
            // waitDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            // waitDialog.setCanceledOnTouchOutside(false);
            // waitDialog.show();

            waitDialog = new Dialog(context, R.style.new_circle_progress);
            // View view = View.inflate(context, R.layout.progress_dialog, null);
            waitDialog.setContentView(R.layout.progress_dialog);
            waitDialog.setCanceledOnTouchOutside(false);
            if (!waitDialog.isShowing()) {
                waitDialog.show();
            }
        }
    }

    /**
     * 请求网络时，弹出dialog
     * 
     * @param context
     */
    public static void showProgressDialog(Context context, String msg) {
        if (waitDialog == null) {
            CustomLog.e(TAG, "showProgressDialog时，waitDialog为null");
            // waitDialog = new ProgressDialog(context);
            // View view = View.inflate(context, R.layout.progress_dialog, null);
            // waitDialog.setView(view);
            // // waitDialog.setMessage(msg);
            // waitDialog.setProgressStyle(R.style.new_circle_progress);
            // waitDialog.setCanceledOnTouchOutside(false);
            // waitDialog.show();
            waitDialog = new Dialog(context, R.style.new_circle_progress);
            View view = View.inflate(context, R.layout.progress_dialog, null);
            waitDialog.setContentView(view);
            TextView tx = (TextView) view.findViewById(R.id.emptyView);
            tx.setVisibility(View.VISIBLE);
            tx.setText(msg);
            waitDialog.setCanceledOnTouchOutside(false);
            waitDialog.show();
        } else {
            CustomLog.e(TAG, "showProgressDialog时，waitDialog不为null");
        }
    }

    public static void closeProgressDialog() {
        if (waitDialog != null) {
            waitDialog.dismiss();
            waitDialog = null;
            System.gc();
            CustomLog.e(TAG, "closeProgressDialog时，waitDialog重置为null了");
        }
    }

    public static void selectDialog(Context context, String dilogTitle, final TextView textView, final String[] strArray, final Handler handler, final int flagWhat) {

        new YusAlertDialog.Builder(context).setTitle(dilogTitle).setItems(strArray, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (textView != null) {
                    textView.setText(strArray[which]);
                }

                Message message = new Message();
                message.what = flagWhat;
                message.arg1 = which;
                message.obj = strArray[which];
                handler.sendMessage(message);

            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
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

}
