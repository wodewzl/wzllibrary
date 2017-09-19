
package com.beisheng.base.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;

import com.beisheng.base.R;
import com.beisheng.base.utils.BaseCommonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BSPopWindowsBottom extends PopupWindow {
    private ListView mListView;
    private Context mContext;
    public PopCallback mCallback;

    // 简单弹出框点击后回调接口
    public interface PopCallback {
        public void callback(String str, int position);
    }

    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            mListView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_bottom_in));
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        } else {
            this.dismiss();
        }
    }

    public BSPopWindowsBottom(final Activity activity, final String[] items, final PopCallback callback) {
        this.mContext = activity;
        final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < items.length; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("option", items[i]);
            list.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(activity, list, R.layout.dialog_lv_item,
                new String[] {
                        "option"
                }, new int[] {
                        R.id.textview
                });
        mListView = new ListView(activity);
        mListView.setAdapter(adapter);
        mListView.setDivider(new ColorDrawable(activity.getResources().getColor(R.color.devider_bg)));
        mListView.setDividerHeight(BaseCommonUtils.dip2px(activity, (float) 0.8));
        mListView.setVerticalScrollBarEnabled(true);
        final LinearLayout linearLayout = new LinearLayout(activity);
        linearLayout.setGravity(Gravity.BOTTOM);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);// 定义文本显示组件
        mListView.setLayoutParams(params);
        linearLayout.addView(mListView);
        this.setContentView(linearLayout);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(Color.parseColor("#40000000"));
        this.setBackgroundDrawable(dw);
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                mListView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_bottom_out));
                linearLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        callback.callback(items[position], position);
                        dismiss();
                    }
                }, 150);
            }
        });

        linearLayout.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                mListView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_bottom_out));
                linearLayout.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        dismiss();
                    }
                }, 150);

                return true;
            }
        });

    }
}
