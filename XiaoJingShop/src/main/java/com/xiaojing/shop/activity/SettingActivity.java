package com.xiaojing.shop.activity;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.TextView;

import com.rey.material.widget.Button;
import com.rey.material.widget.LinearLayout;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.constant.BaseConstant;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.interfaces.PostCallback;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.mode.EBMessageVO;
import com.wuzhanglong.library.mode.UpdateVersionVO;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.wuzhanglong.library.utils.DataCleanUtil;
import com.wuzhanglong.library.utils.FileUtil;
import com.wuzhanglong.library.utils.SharePreferenceUtil;
import com.wuzhanglong.library.utils.VersionUtils;
import com.xiaojing.shop.R;
import com.xiaojing.shop.application.AppApplication;
import com.xiaojing.shop.constant.Constant;
import com.xiaojing.shop.updateApp.DownloadService;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.xiaojing.shop.R.id.layout_01;
import static com.xiaojing.shop.R.id.layout_02;
import static com.xiaojing.shop.R.id.layout_04;

public class SettingActivity extends BaseActivity implements View.OnClickListener, PostCallback {
    private LinearLayout mLayout01, mLayout02, mLayout03, mLayout05, mLayout04, mLayout06, mLayout07,mLayout08;
    private Button mBt;
    private DataCleanUtil mDataCleanUtil;
    private TextView mCacheTv, mVersonTv;
    private boolean isClean = true;
    private UpdateVersionVO mUpdateVersionVO;

    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.setting_activity);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initView() {
        mBaseTitleTv.setText("设置");
        mLayout01 = getViewById(R.id.layout_01);
        mLayout02 = getViewById(R.id.layout_02);
        mLayout03 = getViewById(R.id.layout_03);
        mLayout04 = getViewById(layout_04);
        mLayout05 = getViewById(R.id.layout_05);
        mLayout06 = getViewById(R.id.layout_06);
        mLayout07 = getViewById(R.id.layout_07);
        mLayout08=getViewById(R.id.layout_08);
        mBt = getViewById(R.id.bt);
        mBt.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.C7, R.color.C7));


        mDataCleanUtil = new DataCleanUtil(this);
        mCacheTv = getViewById(R.id.cache_tv);
        mCacheTv.setText(mDataCleanUtil.getCacheSize(this, new File(FileUtil.getSaveFilePath(this, Constant.SDCARD_CACHE))));
        mVersonTv = getViewById(R.id.verson_tv);
        mVersonTv.setText("当前版本"+VersionUtils.getversionName(this));

    }

    @Override
    public void bindViewsListener() {
        mLayout01.setOnClickListener(this);
        mLayout03.setOnClickListener(this);
        mLayout02.setOnClickListener(this);
        mLayout04.setOnClickListener(this);
        mLayout05.setOnClickListener(this);
        mLayout06.setOnClickListener(this);
        mBt.setOnClickListener(this);
        mLayout07.setOnClickListener(this);
        mLayout08.setOnClickListener(this);
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
        if (AppApplication.getInstance().getUserInfoVO() == null && v.getId() != R.id.layout_05 && v.getId() != layout_04&& v.getId() != R.id.layout_07&& v.getId() != R.id.layout_08) {
            Intent intent = new Intent();
            intent.putExtra("type", "2");
            intent.setClass(mActivity, LoginActivity.class);
            mActivity.startActivityForResult(intent, 1);
            return;
        }

        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case layout_01:
                bundle.putString("url", this.getIntent().getStringExtra("password_url"));
                open(WebViewActivity.class, bundle, 0);
                break;
            case layout_02:
                openActivity(UserInfoActivity.class);
                break;
            case R.id.layout_03:
                openActivity(AddressActivity.class);
                break;
            case layout_04:
                bundle.putString("url", SharePreferenceUtil.getSharedpreferenceValue(this, "aboutus_url", "url"));

                open(WebViewActivity.class, bundle, 0);
                break;
            case R.id.layout_05:
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("确定要清除缓存吗?")
                        .setConfirmText("确定")
                        .setCancelText("取消")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {

                                if (isClean) {
                                    mDataCleanUtil.cleanApplicationData(SettingActivity.this, FileUtil.getSaveFilePath(SettingActivity.this, Constant.SDCARD_CACHE));
                                    isClean = false;
                                    sDialog.setTitleText("删除成功")
                                            .setContentText("当前清除" + mCacheTv.getText().toString() + "文件缓存")
                                            .setConfirmText("确定")
                                            .setConfirmClickListener(null)
                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                } else {
                                    sDialog.setTitleText("删除成功")
                                            .setContentText("当前清除0KB文件缓存")
                                            .setConfirmText("确定")
                                            .setConfirmClickListener(null)
                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                }
                                mCacheTv.setText("0KB");
                            }


                        })
                        .show();


//                mDataCleanUtil.cleanDataCache(this, Constant.SDCARD_CACHE);
                break;
            case R.id.layout_06:
                bundle.putString("url", this.getIntent().getStringExtra("set_paypwd_url"));
                open(WebViewActivity.class, bundle, 0);
                break;
            case R.id.bt:
                if (AppApplication.getInstance().getUserInfoVO() == null) {
                    openActivity(LoginActivity.class);
                } else {
                    AppApplication.getInstance().saveUserInfoVO(null);
//                    Intent intent = new Intent();
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.setClass(this, LoginActivity.class);
//                    this.startActivity(intent);
                    JPushInterface.setAlias(this, "", new TagAliasCallback() {
                        @Override
                        public void gotResult(int i, String s, Set<String> set) {
                        }
                    });
                    EventBus.getDefault().post(new EBMessageVO("login_out"));
                    this.finish();
                }
                break;
            case R.id.layout_07:
                checkUpdate();
                break;

            case R.id.layout_08:
                bundle.putString("url", SharePreferenceUtil.getSharedpreferenceValue(this, "help_url", "url"));
                open(WebViewActivity.class, bundle, 0);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (AppApplication.getInstance().getUserInfoVO() == null) {
            mBt.setText("立即登录");
        } else {
            mBt.setText("退出登录");
        }
    }


    public void checkUpdate() {
        HttpClientUtil.post(this, this, Constant.CHECK_UPDATE_URL, null, UpdateVersionVO.class, this);
    }


    @Override
    public void success(BaseVO vo) {
        UpdateVersionVO updateVersionVO = (UpdateVersionVO) vo;
        mUpdateVersionVO = updateVersionVO.getDatas();
        if (BaseConstant.RESULT_SUCCESS_CODE.equals(updateVersionVO.getCode())) {
            if (BaseCommonUtils.parseInt(updateVersionVO.getDatas().getAndroid_version()) > VersionUtils.getversionCode(this)) {
                updateDialog();
            } else {
                showCustomToast("亲，已是最新版本哦");
            }
        }
    }

    public void updateDialog() {
        new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText("友情提示")
                .setContentText("发现有新的版本，赶紧下来看看吧")
                .setConfirmText("确定")
                .setCancelText("取消")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                        Intent updateIntent = new Intent(SettingActivity.this, DownloadService.class);
                        updateIntent.putExtra("url", mUpdateVersionVO.getAndroid_url());
                        updateIntent.putExtra("drawableId", R.drawable.ic_launcher);
                        SettingActivity.this.startService(updateIntent);
                    }
                })
                .show();
    }
}
