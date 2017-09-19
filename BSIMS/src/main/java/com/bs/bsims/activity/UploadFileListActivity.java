
package com.bs.bsims.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;


import com.bs.bsims.R;
import com.bs.bsims.adapter.UploadFileListAdapter;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.download.domain.LocalFilePathObj;
import com.bs.bsims.utils.CustomLog;
import com.bs.bsims.utils.CustomToast;

import org.xutils.x;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;

/**
 * 展示文件上传列表
 * 
 * @author Administrator
 */
public class UploadFileListActivity extends BaseActivity {

    private static final String TAG = "UploadFileListActivity";

    private Context context;

    @ViewInject(R.id.lv_upload_file_list)
    private ListView listView;

    private static final String rootPath = Environment
            .getExternalStorageDirectory().getAbsolutePath();
    private static final String Separator = "/"; // 分隔符
    private LocalFilePathObj obj = new LocalFilePathObj();

    private UploadFileListAdapter adapter;

    @ViewInject(R.id.txt_upload_file_list_path)
    private TextView txt_path;

    private boolean flag_fujian;

    @Override
    public void initView() {
        findViewById(R.id.img_head_back).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showBack();
                    }
                });
        mTitleTv.setText("选择上传的文件");
        mTitleTv.setVisibility(View.VISIBLE);

        initObj(rootPath);

        adapter = new UploadFileListAdapter(this, obj);
        listView.setAdapter(adapter);

    }

    @Event(R.id.txt_upload_file_list_back)
    public void backOnclick(View v) {
        showBack();
    }

    /**
     * 通过路径，获取此路径下面的文件和文件夹
     * 
     * @param path
     */
    private void initObj(String path) {
        File rootFile = new File(path);
        String[] list = rootFile.list();

        if (null == list) {
            list = new String[] {};
        }

        obj.setPrePath(path + Separator);
        obj.setLists(list);

        txt_path.setText(obj.getPrePath());
    }

    /**
     * 点击的是返回
     */
    protected void showBack() {
        LocalFilePathObj obj2 = adapter.getObj();
        String prePath = obj2.getPrePath();

        Log.e(TAG, "返回键 ：" + prePath);
        if (obj2.getPrePath().equals(rootPath + Separator)) {
            finish();
            return;
        }

        String previousPath = getPreviousPath(prePath);
        initObj(previousPath);

        adapter.setObj(obj);
        adapter.notifyDataSetChanged();
    }

    /**
     * 点击的是文件
     */
    protected void showFile(File file) {
        // TODO 文件要显示dialog
        // Toast.makeText(getApplicationContext(), file.getAbsolutePath(),
        // 0).show();
        CustomLog.e(TAG, "上传的文件路径：" + file.getAbsolutePath());

        if (flag_fujian) {

            long KB = 1024;
            long MB = KB * 1024;

            long filesize = 0;
            filesize = file.length();
            filesize /= MB;
            if (filesize > 8) {
                CustomToast.showShortToast(UploadFileListActivity.this,
                        "上传文件不能超过8M");
                // finish();
                return;
            }
            Intent intent = new Intent(Constant.ACTION_NAME_FUJIAN);

            intent.putExtra("url", file.getAbsolutePath());
            sendBroadcast(intent);

            finish();
            return;
        }

        Intent intent = new Intent();
        intent.setClass(UploadFileListActivity.this, UploadDetailActivity.class);
        intent.putExtra("url", file.getAbsolutePath());
        startActivity(intent);

    }

    /**
     * 点击的是文件夹
     * 
     * @param file 当前文件夹的绝对路径File
     * @param fileName 当前文件夹的名称
     */
    private void showFolder(File file, String fileName) {
        CustomLog.e(TAG, file.getAbsolutePath() + "..." + fileName);
        String[] list = file.list();
        obj.setLists(list);
        obj.setPrePath(obj.getPrePath() + fileName + Separator);
        adapter.setObj(obj);
        adapter.notifyDataSetChanged();

        txt_path.setText(obj.getPrePath());
    }

    /**
     * 获取当前文件夹的上层路径
     * 
     * @param path
     */
    private String getPreviousPath(String path) {
        int lastIndexOf = path.lastIndexOf(Separator);
        String substring = path.substring(0, lastIndexOf);
        System.out.println("第1次截取上层路径 ：" + substring);

        lastIndexOf = substring.lastIndexOf(Separator);
        substring = path.substring(0, lastIndexOf);
        System.out.println("第2次截取上层路径 ：" + substring);

        return substring;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                showBack();
                return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void baseSetContentView() {
        // TODO Auto-generated method stub
        context = this;
        // setContentView(R.layout.ac_upload_file_list);

        View.inflate(this, R.layout.ac_upload_file_list, mContentLayout);
        // mHeadLayout.setVisibility(View.GONE);
        // View.inflate(this, R.layout.ac_taskevent_release_tasks, mContentLayout);
        x.view().inject(this);

        Intent intent = getIntent();
        if (intent.hasExtra("flag_fujian")) // 附件上传的标识
            flag_fujian = intent.getBooleanExtra("flag_fujian", false);
    }

    @Override
    public boolean getDataResult() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void updateUi() {
        // TODO Auto-generated method stub

    }

    @Override
    public void bindViewsListener() {
        // TODO Auto-generated method stub
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {

                String fileName = obj.getLists()[position];
                String fileAbsolutePath = obj.getPrePath() + fileName; // 文件的绝对路径
                Log.e(TAG, "点击的文件的绝对路径 ： " + fileAbsolutePath);

                File file = new File(fileAbsolutePath);
                if (file.isDirectory()) {
                    showFolder(file, fileName);
                } else {
                    showFile(file);
                }

            }
        });

    }

}
