
package com.beisheng.base.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.beisheng.base.R;
import com.beisheng.base.imgselector.MultiImageSelectorFragment;

import java.io.File;
import java.util.ArrayList;

/**
 * 多图选择 Created by Nereo on 2015/4/7. Updated by nereo on 2016/1/19.
 */
public class PhotoSelectorActivity extends BaseActivity implements MultiImageSelectorFragment.Callback {

    /** �?��图片选择次数，int类型，默�? */
    public static final String EXTRA_SELECT_COUNT = "max_select_count";
    /** 图片选择模式，默认多�? */
    public static final String EXTRA_SELECT_MODE = "select_count_mode";
    /** 是否显示相机，默认显�? */
    public static final String EXTRA_SHOW_CAMERA = "show_camera";
    /** 选择结果，返回为 ArrayList&lt;String&gt; 图片路径集合 */
    public static final String EXTRA_RESULT = "select_result";
    /** 默认选择�? */
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_list";

    /** 单�? */
    public static final int MODE_SINGLE = 0;
    /** 多�? */
    public static final int MODE_MULTI = 1;

    private ArrayList<String> resultList = new ArrayList<String>();
    private int mDefaultCount;

    private void updateDoneText() {
        mBaseOkTv.setText(String.format("%s(%d/%d)", "完成", resultList.size(), mDefaultCount));
    }

    @Override
    public void onSingleImageSelected(String path) {
        Intent data = new Intent();
        resultList.add(path);
        data.putStringArrayListExtra(EXTRA_RESULT, resultList);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onImageSelected(String path) {
        if (!resultList.contains(path)) {
            resultList.add(path);
        }
        // 有图片之后，改变按钮状�?
        if (resultList.size() > 0) {
            updateDoneText();
            if (!mBaseOkTv.isEnabled()) {
                mBaseOkTv.setEnabled(true);
            }
        }
    }

    @Override
    public void onImageUnselected(String path) {
        if (resultList.contains(path)) {
            resultList.remove(path);
        }
        updateDoneText();
        // 当为选择图片时�?的状�?
        if (resultList.size() == 0) {
            mBaseOkTv.setText("完成");
            mBaseOkTv.setEnabled(false);
        }
    }

    @Override
    public void onCameraShot(File imageFile) {
        if (imageFile != null) {
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(imageFile)));
            Intent data = new Intent();
            resultList.add(imageFile.getAbsolutePath());
            data.putStringArrayListExtra(EXTRA_RESULT, resultList);
            setResult(RESULT_OK, data);
            finish();
        }
    }

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.activity_default, mBaseContentLayout);
        Intent intent = getIntent();
        mDefaultCount = intent.getIntExtra(EXTRA_SELECT_COUNT, 9);
        int mode = intent.getIntExtra(EXTRA_SELECT_MODE, MODE_MULTI);
        boolean isShow = intent.getBooleanExtra(EXTRA_SHOW_CAMERA, true);
        if (mode == MODE_MULTI && intent.hasExtra(EXTRA_DEFAULT_SELECTED_LIST)) {
            resultList = intent.getStringArrayListExtra(EXTRA_DEFAULT_SELECTED_LIST);
        }
        if (intent.getStringExtra("activity") != null)
            mBaseTitleTv.setText(intent.getStringExtra("activity"));
        else
            mBaseTitleTv.setText("选择图片");
        Bundle bundle = new Bundle();
        bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_COUNT, mDefaultCount);
        bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_MODE, mode);
        bundle.putBoolean(MultiImageSelectorFragment.EXTRA_SHOW_CAMERA, isShow);
        bundle.putStringArrayList(MultiImageSelectorFragment.EXTRA_DEFAULT_SELECTED_LIST, resultList);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.image_grid, Fragment.instantiate(this, MultiImageSelectorFragment.class.getName(), bundle))
                .commit();

        // 返回按钮
        mBaseBackTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        // 完成按钮
        if (resultList == null || resultList.size() <= 0) {
            mBaseOkTv.setText("完成");
            mBaseOkTv.setEnabled(false);
        } else {
            updateDoneText();
            mBaseOkTv.setEnabled(true);
        }
    }

    public boolean getDataResult() {
        return true;
    }

    @Override
    public void initView() {

    }

    @Override
    public void bindViewsListener() {
        mBaseOkTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resultList != null && resultList.size() > 0) {
                    // 返回已�?择的图片数据
                    Intent data = new Intent();
                    data.putStringArrayListExtra(EXTRA_RESULT, resultList);
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });
    }
}
