package com.xiaojing.shop.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.rey.material.widget.Button;
import com.squareup.picasso.Picasso;
import com.vondear.rxtools.RxPhotoUtils;
import com.vondear.rxtools.RxSPUtils;
import com.vondear.rxtools.view.dialog.RxDialogChooseImage;
import com.vondear.rxtools.view.dialog.RxDialogEditSureCancel;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.mode.EBMessageVO;
import com.wuzhanglong.library.utils.ThreadUtil;
import com.xiaojing.shop.R;
import com.xiaojing.shop.application.AppApplication;
import com.xiaojing.shop.constant.Constant;
import com.xiaojing.shop.mode.MyVO;
import com.xiaojing.shop.mode.UserInfoVO;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.vondear.rxtools.view.dialog.RxDialogChooseImage.LayoutType.NO_TITLE;
import static com.wuzhanglong.library.http.NetWorkHelper.uri;

public class UserInfoActivity extends BaseActivity implements OnClickListener {
    private Uri resultUri;
    private UserInfoVO mUserInfo;
    private TextView mTextView01, mTextView02, mTextView03, mTextView04, mTextView05;
    private LinearLayout mLayout01, mLayout02, mLayout03;
    private CircleImageView mHeadImg;
    private Button mBt;
    private MyVO mMyVO;


    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.user_info_activity);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initView() {
        mBaseTitleTv.setText("个人信息");
        mLayout01 = (LinearLayout) findViewById(R.id.layout_01);
        mLayout02 = (LinearLayout) findViewById(R.id.layout_02);
        mLayout03 = (LinearLayout) findViewById(R.id.layout_03);

        mTextView01 = (TextView) findViewById(R.id.tv_01);
        mTextView02 = (TextView) findViewById(R.id.tv_02);
        mTextView03 = (TextView) findViewById(R.id.tv_03);
        mHeadImg = (CircleImageView) findViewById(R.id.head_img);
        if (AppApplication.getInstance().getUserInfoVO() != null)
            mTextView02.setText(AppApplication.getInstance().getUserInfoVO().getUsername());


    }

    @Override
    public void bindViewsListener() {
        mHeadImg.setOnClickListener(this);
        mLayout01.setOnClickListener(this);
        mLayout02.setOnClickListener(this);
        mLayout03.setOnClickListener(this);
    }

    @Override
    public void getData() {
        RequestParams paramsMap = new RequestParams();
        String mUrl = Constant.MY_URL;
        if (AppApplication.getInstance().getUserInfoVO() != null)
            paramsMap.put("key", AppApplication.getInstance().getUserInfoVO().getKey());
        HttpClientUtil.get(mActivity, mThreadUtil, mUrl, paramsMap, MyVO.class);
    }


    @Override
    public void hasData(BaseVO vo) {
        MyVO myVO = (MyVO) vo;
        mMyVO = myVO.getDatas().getMember_info();
        mTextView02.setText(mMyVO.getUser_name());
        mTextView03.setText(mMyVO.getNickname());
        Picasso.with(mActivity).load(mMyVO.getAvator()).into(mHeadImg);
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
            case R.id.layout_01:
                initDialogChooseImage();
                break;
            case R.id.layout_02:
                showCustomToast("用户名无法修改！");
                break;
            case R.id.layout_03:
                final RxDialogEditSureCancel rxDialogEditTextSureCancle = new RxDialogEditSureCancel(this);//提示弹窗
                rxDialogEditTextSureCancle.setTitle("修改昵称");
                rxDialogEditTextSureCancle.getTvTitle().setBackgroundColor(ContextCompat.getColor(this, R.color.C1));
                rxDialogEditTextSureCancle.getTvTitle().setTextSize(16);
                rxDialogEditTextSureCancle.getEditText().setHint("请输入昵称");
                rxDialogEditTextSureCancle.getEditText().setTextSize(14);
                rxDialogEditTextSureCancle.getEditText().setHintTextColor(ContextCompat.getColor(this, R.color.C6));
                rxDialogEditTextSureCancle.getTvSure().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateNickName(rxDialogEditTextSureCancle.getEditText().getText().toString());
                        rxDialogEditTextSureCancle.cancel();
                    }
                });
                rxDialogEditTextSureCancle.getTvCancel().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rxDialogEditTextSureCancle.cancel();
                    }
                });
                rxDialogEditTextSureCancle.show();
                break;
            case R.id.layout_04:

                break;
            case R.id.layout_05:

                break;


            default:
                break;
        }
    }

    private void initDialogChooseImage() {
//        RxDialogChooseImage dialogChooseImage = new RxDialogChooseImage(mActivity, NO_TITLE);
//        dialogChooseImage.getLayoutParams().gravity = Gravity.BOTTOM;
//        dialogChooseImage.show();

        RxDialogChooseImage dialogChooseImage = new RxDialogChooseImage(mActivity, NO_TITLE);
        dialogChooseImage.show();

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RxPhotoUtils.GET_IMAGE_FROM_PHONE://选择相册之后的处理
                if (resultCode == RESULT_OK) {
                    initUCrop(data.getData());
                }

                break;
            case RxPhotoUtils.GET_IMAGE_BY_CAMERA://选择照相机之后的处理
                if (resultCode == RESULT_OK) {
                    initUCrop(RxPhotoUtils.imageUriFromCamera);
                }

                break;
            case RxPhotoUtils.CROP_IMAGE://普通裁剪后的处理
//				Picasso.with(mActivity).load(uri).into(mHeadImg);
                roadImageView(uri, mHeadImg);
                break;

            case UCrop.REQUEST_CROP://UCrop裁剪之后的处理
                if (resultCode == RESULT_OK) {
                    resultUri = UCrop.getOutput(data);
                    roadImageView(resultUri, mHeadImg);
                    RxSPUtils.putContent(mActivity, "AVATAR", resultUri.toString());
                } else if (resultCode == UCrop.RESULT_ERROR) {
                    final Throwable cropError = UCrop.getError(data);
                }
                break;
            case UCrop.RESULT_ERROR://UCrop裁剪错误之后的处理
                final Throwable cropError = UCrop.getError(data);
                break;

            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initUCrop(Uri uri) {
        SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        long time = System.currentTimeMillis();
        String imageName = timeFormatter.format(new Date(time));
        Uri destinationUri = Uri.fromFile(new File(mActivity.getCacheDir(), imageName + ".jpeg"));
        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //设置隐藏底部容器，默认显示
        //options.setHideBottomControls(true);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(mActivity, R.color.colorPrimary));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(mActivity, R.color.colorPrimaryDark));

        //开始设置
        //设置最大缩放比例
        options.setMaxScaleMultiplier(5);
        //设置图片在切换比例时的动画
        options.setImageToCropBoundsAnimDuration(666);
        //设置裁剪窗口是否为椭圆
//        options.setOvalDimmedLayer(true);
        //设置是否展示矩形裁剪框
//        options.setShowCropFrame(false);
        //设置裁剪框横竖线的宽度
//        options.setCropGridStrokeWidth(20);
        //设置裁剪框横竖线的颜色
//        options.setCropGridColor(Color.GREEN);
        //设置竖线的数量
//        options.setCropGridColumnCount(2);
        //设置横线的数量
//        options.setCropGridRowCount(1);

        UCrop.of(uri, destinationUri)
                .withAspectRatio(1, 1)
                .withMaxResultSize(1000, 1000)
                .withOptions(options)
                .start(mActivity);
    }

    //从Uri中加载图片 并将其转化成File文件返回
    private File roadImageView(Uri uri, CircleImageView imageView) {
        Picasso.with(mActivity).load(uri).into(imageView);
        File file = (new File(RxPhotoUtils.getImageAbsolutePath(mActivity, uri)));
        uplaodHeadImg(file);
        return file;
    }

    public void uplaodHeadImg(File file) {
        try {
            RequestParams paramsMap = new RequestParams();
            String mUrl = Constant.UPLOAD_HEAD_URL;
            if (AppApplication.getInstance().getUserInfoVO() != null)
                paramsMap.put("key", AppApplication.getInstance().getUserInfoVO().getKey());
            paramsMap.put("file", file);
            HttpClientUtil.post(mActivity, this, mUrl, paramsMap, null, "1");
        } catch (Exception e) {

        }

        mBaseContentLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new EBMessageVO("update_info"));
            }
        },1000);

    }

    public void updateNickName(String nickname) {
        if (nickname.length() == 0) {
            showCustomToast("请填写昵称再尝试");
            return;
        }
        RequestParams paramsMap = new RequestParams();
        String mUrl = Constant.UPDATE_NICK_URL;
        if (AppApplication.getInstance().getUserInfoVO() != null)
            paramsMap.put("key", AppApplication.getInstance().getUserInfoVO().getKey());
        paramsMap.put("nickname", nickname);
        HttpClientUtil.post(mActivity, this, mUrl, paramsMap, null, "1");
        new ThreadUtil(this, this).start();


        mBaseContentLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new EBMessageVO("update_info"));
            }
        },1000);

    }

}
