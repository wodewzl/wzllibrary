
package com.beisheng.base.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beisheng.base.R;

public class BSDialog extends Dialog {

    Context context;
    View view;
    View.OnClickListener onClickListener;
    String title;
    private Button mBtnConfir;
    private Button mBtnCancel;
    private LinearLayout mBottomView;
    private ImageView mDiverImg;
    private TextView mTitleTv;
    private int mColor = -1;// 默认值

    public BSDialog(Context context, String title, View view, int color, View.OnClickListener onClickListener) {
        super(context, R.style.MyDialog);
        this.context = context;
        this.title = title;
        this.view = view;
        this.mColor = color;
        this.onClickListener = onClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.my_dialog);
        mBtnConfir = (Button) findViewById(R.id.btn_confir);
        mBtnCancel = (Button) findViewById(R.id.btn_cancel);
        mBottomView = (LinearLayout) findViewById(R.id.bottom_view);
        mDiverImg = (ImageView) findViewById(R.id.diver_img);
        mTitleTv = (TextView) findViewById(R.id.textv_title);
        mTitleTv.setText(title);
        if (mColor != -1) {
            setColorStyle(mColor);
        }
        mTitleTv.setTextColor(context.getResources().getColor(R.color.sy_title_color));
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.relayout_addview);
        mainLayout.addView(view);
        mBtnConfir.setOnClickListener(onClickListener);
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                BSDialog.this.dismiss();
            }
        });
    }

    public void setButtonVisible(boolean visible) {
        if (visible) {
            mBottomView.setVisibility(View.VISIBLE);
        } else {
            mBottomView.setVisibility(View.GONE);
        }
    }

    public void setButtonTwoGone(boolean visible) {
        if (visible) {
            mBtnCancel.setVisibility(View.GONE);
        } else {
            mBtnCancel.setVisibility(View.VISIBLE);
        }
    }

    public void setColorStyle(int color) {
        mDiverImg.setBackgroundColor(color);
        mBtnConfir.setBackgroundColor(color);
        mTitleTv.setTextColor(color);
    }
}
