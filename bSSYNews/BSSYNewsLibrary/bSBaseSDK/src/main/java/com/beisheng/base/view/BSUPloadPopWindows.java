
package com.beisheng.base.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.beisheng.base.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BSUPloadPopWindows extends PopupWindow {
    private LinearLayout mLayoutRoot;
    private static final int TAKE_PICTURE = 0x000001;
    private static final int RESULT_LOAD_IMAGE = 0x000002;
    private Bitmap mBitmap;
    private String filePath;
    private int position;

    // 简单弹出框点击后回调接口
    public interface ResultCallback {
        public void callback(String str, int position);
    }

    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        } else {
            this.dismiss();
        }
    }

    public BSUPloadPopWindows(final Activity activity, String[] items, View parentView) {
        // this.piclist = new ArrayList<String>();
        // this.mActivity = activity;
        // this.mBitmap = bitmap;
        // this.position = positon;
        // View view = View.inflate(activity, R.layout.item_popupwindows, null);
        // mLayoutRoot = (LinearLayout) view.findViewById(R.id.ll_popup);
        // mLayoutRoot.startAnimation(AnimationUtils.loadAnimation(activity,
        // R.anim.activity_translate_in));
        // setWidth(LayoutParams.MATCH_PARENT);
        // setHeight(LayoutParams.WRAP_CONTENT);
        // setBackgroundDrawable(new BitmapDrawable());
        // setFocusable(true);
        // setOutsideTouchable(true);
        // setContentView(view);
        //
        // RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        // Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
        // Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
        // Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
        // Button bt4 = (Button) view.findViewById(R.id.item_popupwindows_delete);
        // LinearLayout delete = (LinearLayout) view.findViewById(R.id.delete_layout);
        // bt4.setOnClickListener(new OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // mAdapter.mList.remove(mBitmap);
        // mAdapter.mPicList.remove(positon);
        // mAdapter.notifyDataSetChanged();
        // dismiss();
        // mLayoutRoot.startAnimation(AnimationUtils.loadAnimation(activity,
        // R.anim.activity_translate_out));
        // mLayoutRoot.clearAnimation();
        // }
        // });
        // if (mAdapter != null) {
        // delete.setVisibility(View.VISIBLE);
        // }
        // parent.setOnClickListener(new OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // mLayoutRoot.startAnimation(AnimationUtils.loadAnimation(activity,
        // R.anim.activity_translate_out));
        // mLayoutRoot.postDelayed(new Runnable() {
        //
        // @Override
        // public void run() {
        // dismiss();
        // }
        // }, 300);
        //
        // }
        // });
        // bt1.setOnClickListener(new OnClickListener() {
        // public void onClick(View v) {
        // photo(mActivity);
        // dismiss();
        // }
        // });
        // bt2.setOnClickListener(new OnClickListener() {
        // public void onClick(View v) {
        //
        // Intent i = new Intent(Intent.ACTION_PICK,
        // android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // mActivity.startActivityForResult(i, RESULT_LOAD_IMAGE);
        // dismiss();
        // }
        // });
        // bt3.setOnClickListener(new OnClickListener() {
        // public void onClick(View v) {
        // mLayoutRoot.startAnimation(AnimationUtils.loadAnimation(activity,
        // R.anim.activity_translate_out));
        // mLayoutRoot.postDelayed(new Runnable() {
        //
        // @Override
        // public void run() {
        // dismiss();
        // }
        // }, 300);
        // }
        // });
        //
        // showAtLocation(parentView, Gravity.BOTTOM, 0, 0);

        final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < items.length; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("option", items[i]);
            list.add(map);
        }

        LinearLayout layout = new LinearLayout(activity);
        layout.setId(18);
        TextView textView = new TextView(activity);
        textView.setId(186);
        layout.addView(textView);

        SimpleAdapter adapter = new SimpleAdapter(activity, list, 18,
                new String[] {
                        "option"
                }, new int[] {
                        186
                });
        ListView listView = new ListView(activity);
        listView.setAdapter(adapter);
        listView.setDivider(new ColorDrawable(activity.getResources().getColor(R.color.C3)));
        listView.setDividerHeight(1);
        listView.setVerticalScrollBarEnabled(true);
        LinearLayout linearLayout = new LinearLayout(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);// 定义文本显示组件
        listView.setLayoutParams(params);
        linearLayout.addView(listView);

        this.setContentView(linearLayout);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.MATCH_PARENT);

        this.setFocusable(true);
        this.setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(Color.parseColor("#40000000"));
        this.setBackgroundDrawable(dw);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TreeVO childVo = array.get((int) id);
                // mCallBack.callback(childVo);
                dismiss();
            }
        });

        linearLayout.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                dismiss();
                return true;
            }
        });

    }
}
