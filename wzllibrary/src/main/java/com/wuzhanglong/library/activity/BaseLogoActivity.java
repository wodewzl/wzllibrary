package com.wuzhanglong.library.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.wuzhanglong.library.R;
import com.wuzhanglong.library.mode.EBMessageVO;
import com.wuzhanglong.library.utils.BaseCommonUtils;

import org.greenrobot.eventbus.EventBus;
public abstract class BaseLogoActivity extends Activity implements OnClickListener {
    public static final String APP_VERSION = "V2.0";
    public ImageView mLogoImageView, mAvdImg;
    public LinearLayout  mTimeRootLayout,mTimeLayout;
    public TextView mTimeTv;
    public abstract void initLogo();

    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
                .LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_logo_activity);
        initView();
        initLogo();
        bindViewsListener();
        mLogoImageView.postDelayed(new Runnable() {
            @Override
            public void run() {
//                Intent intent = new Intent();
//                int [] drawableId= new int[]{R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3};
//                intent.putExtra("drawableId",drawableId);
//                intent.setClass(BaseLogoActivity.this, GuideActivity.class);
//                BaseLogoActivity.this.startActivity(intent);
                EventBus.getDefault().post(new EBMessageVO("guide"));
            }
        }, 3000);

    }

    public void bindViewsListener() {
        mLogoImageView.setOnClickListener(this);
        mTimeRootLayout.setOnClickListener(this);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void initView() {
        mLogoImageView = (ImageView) findViewById(R.id.logo_image);
        mAvdImg = (ImageView) findViewById(R.id.avd_image);
        mTimeRootLayout = (LinearLayout) findViewById(R.id.time_root_layout);
        mTimeTv = (TextView) findViewById(R.id.time_tv);
        mTimeLayout = (LinearLayout) findViewById(R.id.time_layout);
        mTimeLayout.setBackground(BaseCommonUtils.setBackgroundShap(this, 20, "#30000000", "#30000000"));
    }

    @Override
    public void onClick(View v) {
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
