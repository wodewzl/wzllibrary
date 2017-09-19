package com.beisheng.easycar.activity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.beisheng.easycar.R;
import com.beisheng.easycar.application.AppApplication;
import com.wuzhanglong.library.activity.BaseLogoActivity;
import com.wuzhanglong.library.mode.EBMessageVO;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Administrator on 2017/3/8.
 */

public class LogoActivity extends BaseLogoActivity {
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initLogo() {
        EventBus.getDefault().register(this);
        mLogoImageView.setBackgroundResource(R.drawable.logo);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EBMessageVO event) {
        if ("guide".equals(event.getMessage())) {
            Intent intent = new Intent();
//            intent.setClass(this, HomeActivity.class);
//            intent.setClass(this, MyRouteActivity.class);
//            intent.setClass(this, PayActivity.class);
//            intent.setClass(this, PayDepositActivity.class);
//            intent.setClass(this, ReportCarActivity.class);
//            intent.setClass(this, UseCarActivity.class);
//            intent.setClass(this, ApproveNameActivity.class);
//            intent.setClass(this, BillingCarActivity.class);
//            intent.setClass(this, LockCarActivity.class);
//            intent.setClass(this, BackCarActivity.class);

//            intent.setClass(this, RegistActivity.class);
            if (AppApplication.getInstance().getUserInfoVO() != null) {
                intent.setClass(this, HomeActivity.class);
            } else {
                intent.setClass(this, LoginActivity.class);
            }
//            intent.setClass(this, LoginActivity.class);
//            intent.setClass(this,NearbyCarActivity.class);
//            intent.setClass(this,NearbyActivity.class);

//            intent.setClass(this,ShopPromotionsActivity.class);
            startActivity(intent);
            this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
