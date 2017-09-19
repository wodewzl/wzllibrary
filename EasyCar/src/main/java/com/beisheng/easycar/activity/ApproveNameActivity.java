package com.beisheng.easycar.activity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beisheng.easycar.R;
import com.beisheng.easycar.application.AppApplication;
import com.beisheng.easycar.constant.Constant;
import com.loopj.android.http.RequestParams;
import com.nanchen.compresshelper.CompressHelper;
import com.rey.material.widget.Button;
import com.squareup.picasso.Picasso;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.constant.BaseConstant;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.interfaces.PostCallback;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.wuzhanglong.library.utils.PhotoSelectUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class ApproveNameActivity extends BaseActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks,PostCallback {
    private static final int REQUEST_CODE_PERMISSION_PHOTO_PICKER = 1;
    private static final int REQUEST_CODE_CHOOSE_PHOTO = 1;
    private static final int REQUEST_CODE_PHOTO_PREVIEW = 2;
    private TextView mTv01, mTv02, mTv03, mTv04, mTv05;
    private ImageView mImg01, mImg02, mImg03, mImg04, mImg05;
    private List<File> mFileList = new ArrayList<>();
    private int mCurrentType = 0;//1234一次为4个 mTv01, mTv02, mTv03, mTv04
    private File mFile01, mFile02, mFile03, mFile04, mFile05;
    private Button mOkBt;

    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.activity_approve_name);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initView() {
        mBaseTitleTv.setText("实名认证");
        mBaseHeadLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.Car2));
        mTv01 = getViewById(R.id.tv_01);
        mTv02 = getViewById(R.id.tv_02);
        mTv03 = getViewById(R.id.tv_03);
        mTv04 = getViewById(R.id.tv_04);
        mTv05 = getViewById(R.id.tv_05);
        mImg01 = getViewById(R.id.img_01);
        mImg02 = getViewById(R.id.img_02);
        mImg03 = getViewById(R.id.img_03);
        mImg04 = getViewById(R.id.img_04);
        mImg05 = getViewById(R.id.img_05);
        mOkBt = getViewById(R.id.ok_bt);
        mOkBt.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.Car2, R.color.Car2));
    }

    @Override
    public void bindViewsListener() {
        mTv01.setOnClickListener(this);
        mTv02.setOnClickListener(this);
        mTv03.setOnClickListener(this);
        mTv04.setOnClickListener(this);
        mTv05.setOnClickListener(this);
        mOkBt.setOnClickListener(this);
    }

    @Override
    public void getData() {
        HttpClientUtil.show(mThreadUtil);
    }

    @Override
    public void hasData(BaseVO vo) {

    }

    @Override
    public void noData(BaseVO vo) {

    }

    @Override
    public void noNet() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_01:
                mCurrentType = 1;
                PhotoSelectUtil.selectPhoto(this, 1, Constant.SDCARD_CACHE);
                break;
            case R.id.tv_02:
                mCurrentType = 2;
                PhotoSelectUtil.selectPhoto(this, 1, Constant.SDCARD_CACHE);
                break;
            case R.id.tv_03:
                mCurrentType = 3;
                PhotoSelectUtil.selectPhoto(this, 1, Constant.SDCARD_CACHE);
                break;
            case R.id.tv_04:
                mCurrentType = 4;
                PhotoSelectUtil.selectPhoto(this, 1, Constant.SDCARD_CACHE);
                break;
            case R.id.tv_05:
                mCurrentType = 5;
                PhotoSelectUtil.selectPhoto(this, 1, Constant.SDCARD_CACHE);
                break;
            case R.id.ok_bt:
                commit();
                break;
            default:
                break;
        }
    }

    @Override
    public void onPermissionsGranted(int i, List<String> list) {

    }

    @Override
    public void onPermissionsDenied(int i, List<String> list) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (intent == null)
            return;
        List<String> list = null;
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_PHOTO) {
            //是否单选，单选走true 语句，多选走false语句，这么默认false
            list = intent.getStringArrayListExtra("EXTRA_SELECTED_IMAGES");
        } else if (requestCode == REQUEST_CODE_PHOTO_PREVIEW) {
            list = intent.getStringArrayListExtra("EXTRA_SELECTED_IMAGES");
        }

        for (int i = 0; i < list.size(); i++) {
            File file = new File(list.get(i));
            File newFile = CompressHelper.getDefault(ApproveNameActivity.this).compressToFile(file);
            switch (mCurrentType) {
                case 1:
                    mFile01 = newFile;
                    Picasso.with(this).load(mFile01).rotate(-90f).into(mImg01);
                    mTv01.setVisibility(View.GONE);
                    mImg01.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    mFile02 = newFile;
                    Picasso.with(this).load(mFile02).rotate(-90f).into(mImg02);
                    mTv02.setVisibility(View.GONE);
                    mImg02.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    mFile03 = newFile;
                    Picasso.with(this).load(mFile03).rotate(-90f).into(mImg03);
                    mTv03.setVisibility(View.GONE);
                    mImg03.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    mFile04 = newFile;

                    Picasso.with(this).load(mFile04).rotate(-90f).into(mImg04);
                    mTv04.setVisibility(View.GONE);
                    mImg04.setVisibility(View.VISIBLE);
                    break;
                case 5:
                    mFile05 = newFile;
                    Picasso.with(this).load(mFile05).rotate(-90f).into(mImg05);
                    mTv05.setVisibility(View.GONE);
                    mImg05.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }
    }

    public void commit() {
        try {
            showProgressDialog();
            RequestParams params = new RequestParams();
            String mUrl = Constant.APPROVE_NAME_URL;
            if (AppApplication.getInstance().getUserInfoVO() != null)
                params.put("uin", AppApplication.getInstance().getUserInfoVO().getUin());

            params.put("card_positive", mFile01);
            params.put("card_negative", mFile02);
            params.put("drive_positive", mFile03);
            params.put("drive_negative", mFile04);
            params.put("hand_card", mFile05);
            HttpClientUtil.post(mActivity, this, mUrl, params, null, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void success(BaseVO vo) {
        if (BaseConstant.RESULT_SUCCESS_CODE.equals(vo.getCode())) {
            showCustomToast("提交成功");
            finishActivity(this);
        }
    }
}
