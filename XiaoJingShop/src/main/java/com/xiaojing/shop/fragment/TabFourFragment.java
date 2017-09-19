package com.xiaojing.shop.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.rey.material.widget.ImageView;
import com.squareup.picasso.Picasso;
import com.vondear.rxtools.RxPhotoUtils;
import com.vondear.rxtools.RxSPUtils;
import com.vondear.rxtools.activity.ActivityScanerCode;
import com.vondear.rxtools.view.dialog.RxDialogChooseImage;
import com.wuzhanglong.library.fragment.BaseFragment;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.mode.EBMessageVO;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.wuzhanglong.library.utils.ShareUtil;
import com.wuzhanglong.library.utils.ThreadUtil;
import com.xiaojing.shop.R;
import com.xiaojing.shop.activity.ApproveNameActivity;
import com.xiaojing.shop.activity.GameOrderListActivity;
import com.xiaojing.shop.activity.HistoryShopActivity;
import com.xiaojing.shop.activity.LoginActivity;
import com.xiaojing.shop.activity.MyJingBiActivity;
import com.xiaojing.shop.activity.MyOverActivity;
import com.xiaojing.shop.activity.OneShopOrderActivity;
import com.xiaojing.shop.activity.OrderActivity;
import com.xiaojing.shop.activity.PrivilegeActivity;
import com.xiaojing.shop.activity.RegistActivity;
import com.xiaojing.shop.activity.SettingActivity;
import com.xiaojing.shop.activity.ShopBackActivity;
import com.xiaojing.shop.activity.WebViewActivity;
import com.xiaojing.shop.application.AppApplication;
import com.xiaojing.shop.constant.Constant;
import com.xiaojing.shop.mode.MyVO;
import com.xiaojing.shop.mode.UserInfoVO;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import q.rorbin.badgeview.QBadgeView;

import static android.app.Activity.RESULT_OK;
import static com.vondear.rxtools.view.dialog.RxDialogChooseImage.LayoutType.NO_TITLE;
import static com.wuzhanglong.library.http.NetWorkHelper.uri;
import static com.xiaojing.shop.R.id.name_tv;

//import io.github.xudaojie.qrcodelib.CaptureActivity;


/**
 * Created by Administrator on 2017/2/9.
 */

public class TabFourFragment extends BaseFragment implements View.OnClickListener, View.OnLongClickListener {
    public static final int SCANER_CODE = 2;
    public static final int LOGIN_TYPE = 1;
    private com.rey.material.widget.TextView mOrderTv01, mOrderTv02, mOrderTv03, mOrderTv04, mOrderTv05, mToolsTv01, mToolsTv02, mToolsTv03, mToolsTv04,mToolsTv05;
    private CircleImageView mHeadImg;
    private Uri resultUri;
    private com.rey.material.widget.LinearLayout mOrderLayout, mOneShopLayout, mOverLayout, mJinDouLayout, mJinBiLayout, mGameLayout;
    private ImageView mSettingImg;
    private MyVO mMyVO;
    private android.widget.TextView mNameTv, mMoneyTv01, mMoneyTv02, mMoneyTv03, mCollectTv, mFootTv;
    private TextView mDescTv;
    private QBadgeView mQBadgeView01, mQBadgeView02, mQBadgeView03, mQBadgeView04, mQBadgeView05;
    private TextView mNameSureTv, mPrivilegeTv;


    @Override
    public void setContentView() {
        View.inflate(mActivity, R.layout.tab_four_fragment, mBaseContentLayout);
    }

    @Override
    public void initView(View view) {
        mActivity.SetTranslanteBar();
        mNameTv = getViewById(name_tv);
        mCollectTv = getViewById(R.id.collect_tv);
        mFootTv = getViewById(R.id.foot_tv);
        mOrderTv01 = getViewById(R.id.order_tv_01);
        mOrderTv02 = getViewById(R.id.order_tv_02);
        mOrderTv03 = getViewById(R.id.order_tv_03);
        mOrderTv04 = getViewById(R.id.order_tv_04);
        mOrderTv05 = getViewById(R.id.order_tv_05);
        mMoneyTv01 = getViewById(R.id.money_tv_01);
        mMoneyTv02 = getViewById(R.id.money_tv_02);
        mMoneyTv03 = getViewById(R.id.money_tv_03);
        mDescTv = getViewById(R.id.desc_tv);
        mHeadImg = getViewById(R.id.head_img);
        mOrderLayout = getViewById(R.id.order_layout);
        mOneShopLayout = getViewById(R.id.one_shop_layout);
        mGameLayout = getViewById(R.id.game_layout);
        mJinDouLayout = getViewById(R.id.jindou_layout);
        mJinBiLayout = getViewById(R.id.jinbi_layout);
        mOverLayout = getViewById(R.id.over_layout);
        mNameSureTv = getViewById(R.id.name_sure_tv);
        mPrivilegeTv = getViewById(R.id.privilege_tv);
        mSettingImg = getViewById(R.id.setting_mig);
        mToolsTv01 = getViewById(R.id.tools_tv01);
        mToolsTv02 = getViewById(R.id.tools_tv02);
        mToolsTv03 = getViewById(R.id.tools_tv03);
        mToolsTv04 = getViewById(R.id.tools_tv04);
        mToolsTv05=getViewById(R.id.tools_tv05);
        mQBadgeView01 = (QBadgeView) new QBadgeView(mActivity).bindTarget(mOrderTv01).setBadgeGravity(Gravity.END | Gravity
                .TOP).setShowShadow(true).setBadgeTextSize(10,true)
                .setGravityOffset(8, 0, true);
        mQBadgeView02 = (QBadgeView) new QBadgeView(mActivity).bindTarget(mOrderTv02).setBadgeGravity(Gravity.END | Gravity
                .TOP).setShowShadow(true).setBadgeTextSize(10,true)
                .setGravityOffset(8, 0, true);
        mQBadgeView03 = (QBadgeView) new QBadgeView(mActivity).bindTarget(mOrderTv03).setBadgeGravity(Gravity.END | Gravity
                .TOP).setShowShadow(true).setBadgeTextSize(10,true)
                .setGravityOffset(8, 0, true);
        mQBadgeView04 = (QBadgeView) new QBadgeView(mActivity).bindTarget(mOrderTv04).setBadgeGravity(Gravity.END | Gravity.TOP)
                .setShowShadow(true).setBadgeTextSize(10,true)
                .setGravityOffset(8, 0, true);
        mQBadgeView05 = (QBadgeView) new QBadgeView(mActivity).bindTarget(mOrderTv05).setBadgeGravity(Gravity.END | Gravity
                .TOP).setShowShadow(true).setBadgeTextSize(10,true)
                .setGravityOffset(8, 0, true);
    }

    @Override
    public void bindViewsListener() {
        mHeadImg.setOnClickListener(this);
        mHeadImg.setOnLongClickListener(this);
        mJinDouLayout.setOnClickListener(this);
        mJinBiLayout.setOnClickListener(this);
        mOverLayout.setOnClickListener(this);
        mOrderLayout.setOnClickListener(this);
        mNameSureTv.setOnClickListener(this);
        mPrivilegeTv.setOnClickListener(this);
        mDescTv.setOnClickListener(this);
        mSettingImg.setOnClickListener(this);
        mFootTv.setOnClickListener(this);
        mCollectTv.setOnClickListener(this);
        mOneShopLayout.setOnClickListener(this);
        mOrderTv01.setOnClickListener(this);
        mOrderTv02.setOnClickListener(this);
        mOrderTv03.setOnClickListener(this);
        mOrderTv04.setOnClickListener(this);
        mOrderTv05.setOnClickListener(this);
        mToolsTv01.setOnClickListener(this);
        mToolsTv02.setOnClickListener(this);
        mToolsTv03.setOnClickListener(this);
        mToolsTv04.setOnClickListener(this);
        mNameTv.setOnClickListener(this);
        mGameLayout.setOnClickListener(this);
        EventBus.getDefault().register(this);
        mToolsTv05.setOnClickListener(this);
    }

    @Override
    public void getData() {
        if (AppApplication.getInstance().getUserInfoVO() != null) {
            RequestParams paramsMap = new RequestParams();
            String mUrl = Constant.MY_URL;
            paramsMap.put("key", AppApplication.getInstance().getUserInfoVO().getKey());
            HttpClientUtil.get(mActivity, mThreadUtil, mUrl, paramsMap, MyVO.class);
        } else {
            HttpClientUtil.show(mThreadUtil);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void hasData(BaseVO vo) {
        if (!(vo instanceof MyVO)) {
            mThreadUtil = new ThreadUtil(mActivity, this);
            mThreadUtil.start();
            return;
        }
        MyVO myVO = (MyVO) vo;
        mMyVO = myVO.getDatas().getMember_info();
        mNameTv.setText(mMyVO.getNickname());
        resultUri = Uri.parse("http://xiaojingsc.test.beisheng.wang/data/upload/shop/store/goods/1/1_05464447230381854_360.jpg");
        Picasso.with(mActivity).load(mMyVO.getAvator()).into(mHeadImg);
        UserInfoVO userInfoVO = AppApplication.getInstance().getUserInfoVO();
        userInfoVO.setAvator(mMyVO.getAvator());
        AppApplication.getInstance().saveUserInfoVO(userInfoVO);
//        roadImageView(resultUri, mHeadImg);
        mCollectTv.setText("商品收藏（" + mMyVO.getFavorites_goods() + "）");
        mFootTv.setText("我的足迹（" + mMyVO.getFoot_prints() + "）");
        mQBadgeView01.setBadgeNumber(BaseCommonUtils.parseInt(mMyVO.getOrder_nopay_count()));
        mQBadgeView02.setBadgeNumber(BaseCommonUtils.parseInt(mMyVO.getOrder_nosend_count()));
        mQBadgeView03.setBadgeNumber(BaseCommonUtils.parseInt(mMyVO.getOrder_noeval_count()));
        mQBadgeView04.setBadgeNumber(BaseCommonUtils.parseInt(mMyVO.getReturn_count()));
        mQBadgeView05.setBadgeNumber(BaseCommonUtils.parseInt(mMyVO.getOrder_notakes_count()));
        mMoneyTv01.setText(mMyVO.getBalance());
        mMoneyTv02.setText(mMyVO.getGold());
        mMoneyTv03.setText(mMyVO.getBean());
        mDescTv.setVisibility(View.VISIBLE);
        if ("1".equals(mMyVO.getIs_authentic())) {
            mDescTv.setBackgroundResource(R.drawable.my_18);
            mDescTv.setVisibility(View.VISIBLE);
        } else {
            mDescTv.setBackgroundResource(R.drawable.my_17);
            mDescTv.setVisibility(View.VISIBLE);
        }


        if ("1".equals(mMyVO.getCan_apply_merchant())) {
            mToolsTv04.setVisibility(View.VISIBLE);
            if (mMyVO.getMerchant_objection() != null) {
                mToolsTv04.setText("加盟失败");
                mToolsTv04.setTextColor(ContextCompat.getColor(mActivity, R.color.XJColor2));
            } else {
                mToolsTv04.setText("申请加盟");
                mToolsTv04.setTextColor(ContextCompat.getColor(mActivity, R.color.C5));
            }
        } else {
            mToolsTv04.setVisibility(View.GONE);
        }

        if ("1".equals(mMyVO.getIs_merchant())) {
            mToolsTv02.setVisibility(View.VISIBLE);
            mToolsTv04.setVisibility(View.GONE);
        } else {
            mToolsTv02.setVisibility(View.GONE );
        }

        mPrivilegeTv.setVisibility(View.VISIBLE);
        mPrivilegeTv.setBackground(BaseCommonUtils.setBackgroundShap(mActivity, 10, R.color.XJColor7, R.color.XJColor7));

    }

    @Override
    public void noData(BaseVO vo) {
        if("0".equals(vo.getLogin())){
            AppApplication.getInstance().saveUserInfoVO(null);
            mBaseContentLayout.setVisibility(View.VISIBLE);
            mNoContentTv.setVisibility(View.GONE);
            mNoNetTv.setVisibility(View.GONE);
        }


    }

    @Override
    public void noNet() {

    }

    private void initDialogChooseImage() {
        RxDialogChooseImage dialogChooseImage = new RxDialogChooseImage(mActivity, NO_TITLE);
        dialogChooseImage.getLayoutParams().gravity = Gravity.BOTTOM;
        dialogChooseImage.show();
    }

    @Override
    public void onClick(View v) {
        if (AppApplication.getInstance().getUserInfoVO() == null && v.getId() !=R.id.setting_mig)  {
            Intent intent = new Intent();
            intent.putExtra("type", "2");
            intent.setClass(mActivity, LoginActivity.class);
            mActivity.startActivityForResult(intent, 1);
            return;
        }
        final Bundle bundle = new Bundle();
        switch (v.getId()) {
            case name_tv:
            case R.id.head_img:
//                initDialogChooseImage();
                if (AppApplication.getInstance().getUserInfoVO() == null) {
                    Intent intent = new Intent();
                    intent.putExtra("type", "2");
                    intent.setClass(mActivity, LoginActivity.class);
                    mActivity.startActivityForResult(intent, 1);
                } else {
                    bundle.putString("password_url", mMyVO.getModify_pwd_url());
                    bundle.putString("aboutus_url", mMyVO.getAboutus_url());
                    bundle.putString("set_paypwd_url", mMyVO.getSet_paypwd_url());
                    mActivity.open(SettingActivity.class, bundle, 0);
                }
                break;
            case R.id.order_layout:
                mActivity.openActivity(OrderActivity.class);
                break;
            case R.id.order_tv_01:
                bundle.putString("type", "1");
                mActivity.open(OrderActivity.class, bundle, 0);
                break;
            case R.id.order_tv_02:
                bundle.putString("type", "2");
                mActivity.open(OrderActivity.class, bundle, 0);
                break;
            case R.id.order_tv_03:
                bundle.putString("type", "4");
                mActivity.open(OrderActivity.class, bundle, 0);
                break;
            case R.id.order_tv_04:
//                bundle.putString("type", "4");
//                mActivity.open(OrderActivity.class, bundle, 0);
                mActivity.openActivity(ShopBackActivity.class);
                break;
            case R.id.order_tv_05:
                bundle.putString("type", "3");
                mActivity.open(OrderActivity.class, bundle, 0);
                break;

            case R.id.one_shop_layout:
                mActivity.openActivity(OneShopOrderActivity.class);
                break;

            case R.id.game_layout:
                mActivity.openActivity(GameOrderListActivity.class);
                break;
            case R.id.jinbi_layout:
                bundle.putString("type", "1");
                mActivity.open(MyJingBiActivity.class, bundle, 0);
                break;
            case R.id.jindou_layout:
                bundle.putString("type", "2");
                mActivity.open(MyJingBiActivity.class, bundle, 0);
                break;
            case R.id.over_layout:
                mActivity.openActivity(MyOverActivity.class);
                break;
            case R.id.name_sure_tv:
                mActivity.openActivity(ApproveNameActivity.class);
                break;
            case R.id.desc_tv:
                mActivity.openActivity(ApproveNameActivity.class);
                break;
            case R.id.privilege_tv:
                mActivity.openActivity(PrivilegeActivity.class);
                break;
            case R.id.setting_mig:
                if (AppApplication.getInstance().getUserInfoVO() != null) {
                    bundle.putString("password_url", mMyVO.getModify_pwd_url());
//                    bundle.putString("aboutus_url", mMyVO.getAboutus_url());
                    bundle.putString("set_paypwd_url", mMyVO.getSet_paypwd_url());
                }
                mActivity.open(SettingActivity.class, bundle, 0);
                break;
            case R.id.foot_tv:
                bundle.putString("type", "2");
                mActivity.open(HistoryShopActivity.class, bundle, 0);
                break;
            case R.id.collect_tv:
                bundle.putString("type", "1");
                mActivity.open(HistoryShopActivity.class, bundle, 0);
                break;
            case R.id.tools_tv01:
                mActivity.openActivityForResult(ActivityScanerCode.class, SCANER_CODE);
//                Intent i = new Intent(mActivity, CaptureActivity.class);
//                startActivityForResult(i, SCANER_CODE);
//                Intent intent = new Intent(mActivity, CaptureActivity.class);
//                startActivityForResult(intent, SCANER_CODE);

                break;
            case R.id.tools_tv02:
                bundle.putString("url", mMyVO.getMerchant_url());
                mActivity.open(WebViewActivity.class, bundle, 0);
                break;
            case R.id.tools_tv03:
                ShareUtil.share(mActivity, mMyVO.getShare_data().getImage(), mMyVO.getShare_data().getTitle(), mMyVO.getShare_data().getDesc(), mMyVO.getShare_data().getUrl());
                break;
            case R.id.tools_tv04:
                if (mMyVO.getMerchant_objection() != null) {
                    new SweetAlertDialog(mActivity, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("加盟失败")
                            .setContentText(mMyVO.getMerchant_objection())
                            .setConfirmText("确定")
                            .setCancelText("取消")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    bundle.putString("type", "2");
                                    mActivity.open(RegistActivity.class, bundle, 0);
                                    sDialog.dismissWithAnimation();//直接消失
                                }
                            })
                            .show();


//                    final RxDialogSureCancel dialogSureCancel = new RxDialogSureCancel(mActivity);//提示弹窗
//                    dialogSureCancel.setTitle("加盟失败");
//                    dialogSureCancel.getIv_logo().setBackgroundColor(ContextCompat.getColor(mActivity, R.color.C1));
//                    dialogSureCancel.getIv_logo().setTextSize(14);
//                    dialogSureCancel.getTvContent().setText(mMyVO.getMerchant_objection());
//                    dialogSureCancel.getTvContent().setTextSize(12);
//
//                    dialogSureCancel.getTvSure().setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            bundle.putString("type", "2");
//                            mActivity.open(RegistActivity.class, bundle, 0);
//                            dialogSureCancel.cancel();
//                        }
//                    });
//                    dialogSureCancel.getTvCancel().setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            dialogSureCancel.cancel();
//                        }
//                    });
//                    dialogSureCancel.show();
                } else {
                    bundle.putString("type", "2");
                    mActivity.open(RegistActivity.class, bundle, 0);
                }
                break;
            case R.id.tools_tv05:
                try {
                    BaseCommonUtils.call(mActivity,"4007667778");
                } catch (Exception e) {
                    e.printStackTrace();
                    mActivity.showCustomToast("请确认有打电话功能？");
                }

                break;
            default:
                break;
        }
    }

    @Override
    public boolean onLongClick(View view) {
        initDialogChooseImage();
        return false;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;
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
                Picasso.with(mActivity).load(uri).into(mHeadImg);
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
            case SCANER_CODE:
                String result = data.getStringExtra("result");
//                Intent gotoIntent = new Intent();
//                gotoIntent.setAction("android.intent.action.VIEW");
//                Uri content_url = Uri.parse(result.toString());
//                gotoIntent.setData(content_url);
//                startActivity(gotoIntent);

                Bundle bundle = new Bundle();
                bundle.putString("url", result);
                mActivity.open(WebViewActivity.class, bundle, 0);
                break;

            case LOGIN_TYPE:
                mThreadUtil = new ThreadUtil(mActivity, this);
                mThreadUtil.start();

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
        return (new File(RxPhotoUtils.getImageAbsolutePath(mActivity, uri)));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EBMessageVO event) {
        if ("update_info".equals(event.getMessage()) || "back_webview".equals(event.getMessage())) {
            mThreadUtil = new ThreadUtil(mActivity, this);
            mThreadUtil.start();
        }
        if ("login_out".equals(event.getMessage())) {
            mHeadImg.setImageResource(R.drawable.user_icon_def);
            mNameTv.setText("点击登录");
            mDescTv.setVisibility(View.GONE);
            mQBadgeView01.setBadgeNumber(0);
            mQBadgeView02.setBadgeNumber(0);
            mQBadgeView03.setBadgeNumber(0);
            mQBadgeView04.setBadgeNumber(0);
            mQBadgeView05.setBadgeNumber(0);
            mMoneyTv01.setText("");
            mMoneyTv02.setText("");
            mMoneyTv03.setText("");
            mPrivilegeTv.setVisibility(View.GONE);
            mCollectTv.setText("商品收藏（" + 0 + "）");
            mFootTv.setText("我的足迹（" +0 + "）");
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mMoneyTv01.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mThreadUtil = new ThreadUtil(mActivity, TabFourFragment.this);
                    mThreadUtil.start();
                }
            }, 500);
        }
    }
}
