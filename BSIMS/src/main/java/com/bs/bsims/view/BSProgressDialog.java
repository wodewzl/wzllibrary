
package com.bs.bsims.view;

import android.app.ProgressDialog;
import android.content.Context;

//public class BSProgressDialog extends Dialog {
//    // public BSProgressDialog(Context context, String strMessage) {
//    // // this(context, R.style.BSStyle strMessage);
//    // }
//
//    public BSProgressDialog(Context context, int theme, String strMessage) {
//        super(context, theme);
//        this.setContentView(R.layout.customprogressdialog);
//        this.getWindow().getAttributes().gravity = Gravity.CENTER;
//        // TextView tvMsg = (TextView) this.findViewById(R.id.id_tv_loadingmsg);
//        // if (tvMsg != null) {
//        // tvMsg.setText(strMessage);
//        // }
//    }
//
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//
//        if (!hasFocus) {
//            dismiss();
//        }
//    }
// }

public class BSProgressDialog {
    private ProgressDialog mDialog;

    public BSProgressDialog(Context contxt) {
        ProgressDialog dialog = new ProgressDialog(contxt);
        dialog.setMessage("请稍等..");
        // dialog.show();
    }

    public void showDialog() {
        mDialog.show();
    }

    public void dismissDialog() {
        mDialog.dismiss();
    }
}
