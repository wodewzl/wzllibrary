
package com.beisheng.base.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import com.beisheng.base.R;
import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.view.BSDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DialogUtil {
    // 简单弹出框点击后回调接口

    public interface DialogCallback {
        public void callback(String str, int position);

    }

    private static BSDialog mDialog;

    public static Dialog createLoadingDialog(Context context) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.progress_bar, null);
        RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.layout);
        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog_tran);
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        return loadingDialog;

    }

    public static List<Map<String, Object>> getListItem(String[] array) {
        String[] tmpArray = array;
        List<Map<String, Object>> listems = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < array.length; i++) {
            Map<String, Object> listem = new HashMap<String, Object>();
            String id = tmpArray[i].split(",")[0];
            String name = tmpArray[i].split(",")[1];
            listem.put("option", name);
            listems.add(listem);
        }
        return listems;
    }

    // 简单的弹出对话框封装，如性别（男，女）
    public static void initSimpleListDialog(Context context, String title, final String[] array, int color, final DialogCallback callback) {

        List<Map<String, Object>> list = getListItem(array);
        SimpleAdapter adapter = new SimpleAdapter(context, list, R.layout.dialog_lv_item, new String[] {
                "option"
        }, new int[] {
                R.id.textview
        });
        ListView listView = new ListView(context);
        listView.setAdapter(adapter);
        listView.setDivider(new ColorDrawable(Color.parseColor("#EEEEEE")));
        listView.setDividerHeight(1);
        LinearLayout linearLayout = new LinearLayout(context);
        int width;
        if (array.length > 6) {
            width = 800;
        } else {
            width = LinearLayout.LayoutParams.WRAP_CONTENT;
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, width);// 定义文本显示组件
        listView.setLayoutParams(params);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                String id = array[(int) arg3].split(",")[0];
                String name = array[(int) arg3].split(",")[1];
                callback.callback(id, position);
                mDialog.dismiss();

            }
        });
        BaseActivity activity = (BaseActivity) context;
        mDialog = new BSDialog(context, title, listView, activity.getResources().getColor(color), new OnClickListener() {
            @Override
            public void onClick(View arg0) {
            }
        });
        mDialog.show();
        mDialog.setButtonVisible(false);
    }
}
