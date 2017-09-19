
package com.beisheng.synews.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.im.zhsy.R;

public class BSPopwindowEditText extends PopupWindow implements OnClickListener {
    private Context mContext;
    private CommitCallback mCallback;
    private CommitTwoCallback mTwoCallback;
    private EditText mContentEditText, mContanctEt;
    private TextView mCommitTv, mCannelTv;
    private LinearLayout mBlankLayout, mContentLayout;
    private int type = 0;// 0没有其他输入框，1有其他输入框

    public void showPopupWindow(View parent) {

        if (!this.isShowing()) {
            mContentLayout.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_bottom_in));
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        } else {
            this.dismiss();
        }

        mContentEditText.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) mContentEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 0);

    }

    public interface CommitCallback {
        public abstract void commtiCallback(String content);
    }

    public interface CommitTwoCallback {
        public abstract void commtiCallback(String content, String contanct);
    }

    public BSPopwindowEditText(Context context, CommitCallback callback) {
        this.mContext = context;
        this.mCallback = callback;
        initView();
    }

    public BSPopwindowEditText(Context context, CommitTwoCallback callback, int type) {
        this.mContext = context;
        this.mTwoCallback = callback;
        this.type = type;
        initView();
    }

    public void initView() {
        View view = View.inflate(mContext, R.layout.pop_edittext, null);
        mContentEditText = (EditText) view.findViewById(R.id.content_edittext);
        mContanctEt = (EditText) view.findViewById(R.id.contact_edittext);
        if (type == 1) {
            mContanctEt.setVisibility(View.VISIBLE);
            mContentEditText.setHint("您的意见是我们不断改进的动力");
        } else {
            mContanctEt.setVisibility(View.GONE);
        }
        mCommitTv = (TextView) view.findViewById(R.id.commit_tv);
        mCannelTv = (TextView) view.findViewById(R.id.cannel_tv);
        mContentLayout = (LinearLayout) view.findViewById(R.id.content_layout);
        mBlankLayout = (LinearLayout) view.findViewById(R.id.blank_layout);
        mCommitTv.setOnClickListener(this);
        mCannelTv.setOnClickListener(this);
        mBlankLayout.setOnClickListener(this);

        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.MATCH_PARENT);

        this.setFocusable(true);
        this.setOutsideTouchable(true);
        ColorDrawable drawable = new ColorDrawable(Color.parseColor("#40000000"));
        setBackgroundDrawable(drawable);
        setFocusable(true);
        this.setContentView(view);
        this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.commit_tv:
                if (type == 0) {
                    mCallback.commtiCallback(mContentEditText.getText().toString().trim());
                } else {
                    mTwoCallback.commtiCallback(mContentEditText.getText().toString().trim(), mContanctEt.getText().toString());
                }

                popDismiss();
                break;
            case R.id.cannel_tv:
                popDismiss();
                break;
            case R.id.blank_layout:
                popDismiss();
                break;
            default:
                break;
        }
    }

    public void popDismiss() {
        mContentLayout.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_bottom_out));
        mContentLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, 150);
    }

    public void setSecondDisscuss(String hint) {
        mContentEditText.setText("");
        mContentEditText.setHint(hint);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
