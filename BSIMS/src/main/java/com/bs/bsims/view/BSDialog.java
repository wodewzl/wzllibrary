
package com.bs.bsims.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;

public class BSDialog extends Dialog {

    Context context;
    View view;
    View.OnClickListener onClickListener;
    String title;
    private Button btn_confir;
    private Button btn_cancel;
    private LinearLayout bottom_view;
    private View mLine;

    public BSDialog(Context context, String title, View view, View.OnClickListener onClickListener) {
        super(context, R.style.MyDialog);
        this.context = context;
        this.title = title;
        this.view = view;
        this.onClickListener = onClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.my_dialog);
        btn_confir = (Button) findViewById(R.id.btn_confir);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        bottom_view = (LinearLayout) findViewById(R.id.bottom_view);
        TextView textv_title = (TextView) findViewById(R.id.textv_title);
        mLine = findViewById(R.id.btn_confir_line);
        textv_title.setText(title);
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.relayout_addview);
        mainLayout.addView(view);
        btn_confir.setOnClickListener(onClickListener);
        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                BSDialog.this.dismiss();
            }
        });
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setButtonVisible(boolean visible) {
        if (visible) {
            mLine.setVisibility(View.VISIBLE);
            bottom_view.setVisibility(View.VISIBLE);
        } else {
            mLine.setVisibility(View.GONE);
            bottom_view.setVisibility(View.GONE);
        }
    }
    public void setButtonCanleVisible(boolean visible) {
        if (visible) {
            mLine.setVisibility(View.VISIBLE);
            btn_cancel.setVisibility(View.VISIBLE);
        } else {
            mLine.setVisibility(View.GONE);
            btn_cancel.setVisibility(View.GONE);
        }
    }
    public void setButtonTwoGone(boolean visible) {
        if (visible) {
            mLine.setVisibility(View.GONE);
            btn_cancel.setVisibility(View.GONE);
        } else {
            mLine.setVisibility(View.VISIBLE);
            btn_cancel.setVisibility(View.VISIBLE);
        }

    }
}
