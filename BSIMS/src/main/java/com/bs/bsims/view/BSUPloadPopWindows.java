
package com.bs.bsims.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import com.bs.bsims.R;
import com.bs.bsims.adapter.UploadAdapter;
import com.bs.bsims.utils.BsPermissionUtils;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BSUPloadPopWindows extends PopupWindow {
    private LinearLayout mLayoutRoot;
    private static final int TAKE_PICTURE = 0x000001;
    private static final int RESULT_LOAD_IMAGE = 0x000002;
    private Activity mActivity;
    private Bitmap mBitmap;
    private UploadAdapter mAdapter;
    private String filePath;
    private int position;

    // 简单弹出框点击后回调接口
    public interface ResultCallback {
        public void callback(String str, int position);
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public List<String> piclist;

    public List<String> getPiclist() {
        return piclist;
    }

    public void setPiclist(List<String> piclist) {
        this.piclist = piclist;
    }

    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        } else {
            this.dismiss();
        }
    }

    public BSUPloadPopWindows(final Activity activity, View parentView, UploadAdapter adapter, Bitmap bitmap, final int positon) {
        this.piclist = new ArrayList<String>();
        this.mActivity = activity;
        this.mBitmap = bitmap;
        this.mAdapter = adapter;
        this.position = positon;
        View view = View.inflate(activity, R.layout.item_popupwindows, null);
        mLayoutRoot = (LinearLayout) view.findViewById(R.id.ll_popup);
        mLayoutRoot.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.activity_translate_in));
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setOutsideTouchable(true);
        setContentView(view);

        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
        Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
        Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
        Button bt4 = (Button) view.findViewById(R.id.item_popupwindows_delete);
        LinearLayout delete = (LinearLayout) view.findViewById(R.id.delete_layout);
        bt4.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mAdapter.mList.remove(mBitmap);
                mAdapter.mPicList.remove(positon);
                mAdapter.notifyDataSetChanged();
                dismiss();
                mLayoutRoot.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.activity_translate_out));
                mLayoutRoot.clearAnimation();
            }
        });
        if (mAdapter != null) {
            delete.setVisibility(View.VISIBLE);
        }
        parent.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mLayoutRoot.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.activity_translate_out));
                mLayoutRoot.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        dismiss();
                    }
                }, 300);

            }
        });
        bt1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                photo(mActivity);
                dismiss();
            }
        });
        bt2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                mActivity.startActivityForResult(i, RESULT_LOAD_IMAGE);
                dismiss();
            }
        });
        bt3.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mLayoutRoot.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.activity_translate_out));
                mLayoutRoot.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        dismiss();
                    }
                }, 300);
            }
        });

        showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 打开系统相册
     */
    public void systemPhoto() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivity.startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    public void photo(Activity activity) {
        // 如果没有开启相机权限
        if (BsPermissionUtils.checkCameraHardware(mActivity)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            String path = FileUtil.getSaveFilePath(activity);
            // String path = Environment.getExternalStorageDirectory().toString() + "/bs";
            File path1 = new File(path);
            if (!path1.exists()) {
                path1.mkdirs();
            }
            File file = new File(path1, System.currentTimeMillis() + ".jpg");
            try {
                if (file.exists()) {
                    file.delete();
                }
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.setFilePath(file.getAbsolutePath());
            this.piclist.add(file.getAbsolutePath());
            Uri mOutPutFileUri = Uri.fromFile(file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutPutFileUri);
            activity.startActivityForResult(intent, TAKE_PICTURE);

        }
        else {
                 CustomToast.showShortToast(activity, "请在设置里打开应用照相权限");
        }

    }

    public BSUPloadPopWindows(final Activity activity, View parentView, final String[] array, final ResultCallback callback) {
        final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < array.length; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("option", array[i]);
            list.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(activity, list, R.layout.crm_contacts_add_more_item, new String[] {
                "option"
        }, new int[] {
                R.id.textview
        });
        ListView listView = new ListView(activity);
        listView.setAdapter(adapter);
        listView.setDivider(null);

        View view = View.inflate(activity, R.layout.crm_contacts_add_more_layout, null);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.add_item);
        layout.addView(listView);
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setOutsideTouchable(true);
        setContentView(view);

        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        parent.startAnimation(AnimationUtils.loadAnimation(activity, R.anim.activity_translate_in));
        parent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                int position = (int) arg3;
                callback.callback(array[position], position);
                dismiss();
            }
        });
        showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
    }
}
