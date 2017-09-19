package com.xiaojing.shop.activity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.umeng.socialize.UMShareAPI;
import com.wuzhanglong.library.activity.HomeFragmentActivity;
import com.wuzhanglong.library.constant.BaseConstant;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.interfaces.PostCallback;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.mode.EBMessageVO;
import com.wuzhanglong.library.mode.UpdateVersionVO;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.wuzhanglong.library.utils.VersionUtils;
import com.xiaojing.shop.R;
import com.xiaojing.shop.constant.Constant;
import com.xiaojing.shop.updateApp.DownloadService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 2017/3/10.
 */
//HomeFragmentActivity
public class HomeActivity extends HomeFragmentActivity implements PostCallback {
    BottomNavigationItem mTabThree;
    private UpdateVersionVO mUpdateVersionVO;

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void initBottomBar() {
        EventBus.getDefault().register(this);

//        int[] drawableIds = new int[] {R.drawable.home_01, R.drawable.home_01, R.drawable.home_01, R.drawable.home_01};
//        mTabWidget.update(this,drawableIds);
//        onTabSelected(0);

//        MODE_FIXED+BACKGROUND_STYLE_STATIC
//        MODE_FIXED+BACKGROUND_STYLE_RIPPLE
//        MODE_SHIFTING+BACKGROUND_STYLE_STATIC
//        MODE_SHIFTING+BACKGROUND_STYLE_RIPPLE

        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);

//        mBottomNavigationBar.setMode(MODE_SHIFTING);
//        mBottomNavigationBar.setBackgroundStyle(BACKGROUND_STYLE_STATIC);

        mBottomNavigationBar
                .setActiveColor(R.color.C1)
                .setInActiveColor(R.color.XJColor8)
                .setBarBackgroundColor(R.color.XJColor2);
//                .setActiveColor(R.color.XJColor2)
//                .setInActiveColor(R.color.XJColor8)
//                .setBarBackgroundColor(R.color.C1);

        mTabThree = new BottomNavigationItem(R.drawable.ic_add_shopping_cart_black_48dp, "购物车");
        mBottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.ic_home_black_48dp, "首页"))
                .addItem(new BottomNavigationItem(R.drawable.ic_format_list_bulleted_white_48dp, "分类"))
                .addItem(mTabThree)
                .addItem(new BottomNavigationItem(R.drawable.ic_perm_identity_black_48dp, "个人中心"))
//                .setFirstSelectedPosition(0)
                .initialise();
//        mBottomNavigationBar.selectTab(0);
        mBaseHeadLayout.setVisibility(View.GONE);



        mBaseHeadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkUpdate();
            }
        },1000);
    }


    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        UMShareAPI.get(this).onActivityResult(arg0, arg1, arg2);
        if (arg2 == null)
            return;
        if (mFragmentList.get(3) != null && mFragmentList.get(3).getUserVisibleHint()) {
            /* 然后在碎片中调用重写的onActivityResult方法 */
            //头像处理
            mFragmentList.get(3).onActivityResult(arg0, arg1, arg2);
        }

        if (mFragmentList.get(1) != null && mFragmentList.get(1).getUserVisibleHint()) {
            /* 然后在碎片中调用重写的onActivityResult方法 */
            //头像处理
            mFragmentList.get(1).onActivityResult(arg0, arg1, arg2);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EBMessageVO event) {
        if ("shop_cart_count".equals(event.getMessage())) {
            BadgeItem numberBadgeItem = new BadgeItem()
                    .setBorderWidth(4)
                    .setBackgroundColorResource(R.color.XJColor2)
                    .setText(event.getParams()[0]);
            mTabThree.setBadgeItem(numberBadgeItem);
            mBottomNavigationBar.clearAll();
            mBottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.ic_home_black_48dp, "首页"))
                    .addItem(new BottomNavigationItem(R.drawable.ic_format_list_bulleted_white_48dp, "分类"))
                    .addItem(mTabThree)
                    .addItem(new BottomNavigationItem(R.drawable.ic_perm_identity_black_48dp, "个人中心"))

                    .initialise();

            mBottomNavigationBar.setFirstSelectedPosition(mVpHome.getCurrentItem());
        } else if ("select_shop_cart".equals(event.getMessage())) {
            mVpHome.setCurrentItem(2);
        }else if("select_shop_type".equals(event.getMessage())){
            mVpHome.setCurrentItem(1);
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
                // mActivity.showCustomToast("亲，已是最新版本哦");
//                Toast.makeText(this, "亲，已是最新版本哦", Toast.LENGTH_SHORT).show();
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
                        Intent updateIntent = new Intent(HomeActivity.this, DownloadService.class);
                        updateIntent.putExtra("url", mUpdateVersionVO.getAndroid_url());
                        updateIntent.putExtra("drawableId", R.drawable.ic_launcher);
                        HomeActivity.this.startService(updateIntent);
                    }
                })
                .show();
    }
}
