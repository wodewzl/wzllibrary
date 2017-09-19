
package com.bs.bsims.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.bs.bsims.R;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.utils.LocationUtils;
import com.bs.bsims.viewpager.ViewPagerActivity;

public class LogoActivity extends Activity {
    public static final String APP_VERSION = "V2.0";
    private ImageView mLogoImageView;
    private String mImageUrl;
    private TextView mVersionTv;
    private TextView mCompanyName;
    private static final String SHAREDPREFERENCES_NAME = "my_pref";
    private static final String KEY_GUIDE_ACTIVITY = "guide_activity";

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            Intent intent = new Intent();
            switch (msg.what) {

                case 0:
                    break;
                case 1:
                    if (BSApplication.getInstance().getUserFromServerVO() != null) {
                        intent.setClass(LogoActivity.this, MainActivity.class);
                        // intent.setClass(LogoActivity.this,
                        // CrmPeopleAddDepartSelectActivity.class);
                    } else {
                        intent.setClass(LogoActivity.this, LoginActivity.class);
                    }

                    startActivity(intent);
                    finish();
                    break;
                case 2:

                    intent.setClass(LogoActivity.this, ViewPagerActivity.class);
                    startActivity(intent);
                    finish();
                    break;

                case 3:
                    Display display = LogoActivity.this.getWindowManager()
                            .getDefaultDisplay();
                    Bitmap bitmap = BitmapFactory
                            .decodeResource(LogoActivity.this.getResources(),
                                    R.drawable.logo_image);
                    Bitmap newBitmap = zoomImg(bitmap, display.getWidth(),
                            display.getHeight());
                    Drawable d = new BitmapDrawable(newBitmap);
                    mLogoImageView.setImageBitmap(newBitmap);
                    break;
                default:
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo);
        initView();
        initData();

        //用于每隔几分钟后台定位
//        initLoaction();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void initView() {
        mLogoImageView = (ImageView) findViewById(R.id.logo_image);
        Display display = this.getWindowManager().getDefaultDisplay();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                display.getWidth(), display.getHeight());
        mLogoImageView.setLayoutParams(params);
        mLogoImageView.setAdjustViewBounds(true);

    }

    public void initData() {
        Display display = LogoActivity.this.getWindowManager()
                .getDefaultDisplay();
        Bitmap bitmap = BitmapFactory.decodeResource(
                LogoActivity.this.getResources(), R.drawable.logo_image);
        Bitmap newBitmap = zoomImg(bitmap, display.getWidth(),
                display.getHeight());
        Drawable d = new BitmapDrawable(newBitmap);
        mLogoImageView.setImageBitmap(newBitmap);
        // final boolean mFirst = isFirst();
        // if (mFirst) {
        // mHandler.sendEmptyMessageDelayed(2, 2000);
        // } else {
        // mHandler.sendEmptyMessageDelayed(1, 2000);
        // }

        final Intent intent = new Intent();
        final ScaleAnimation scaleAnim = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        scaleAnim.setFillAfter(true);
        scaleAnim.setDuration(1500);
        scaleAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!isFirst()) {
                    if (BSApplication.getInstance().getUserFromServerVO() != null) {
                        intent.setClass(LogoActivity.this, MainActivity.class);
                    } else {
                        intent.setClass(LogoActivity.this, LoginActivity.class);
                    }

                    startActivity(intent);
                    finish();

                } else {
                    intent.setClass(LogoActivity.this, ViewPagerActivity.class);
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mLogoImageView.startAnimation(scaleAnim);

    }

    private boolean isFirstEnter(Context context, String className) {
        if (context == null || className == null
                || "".equalsIgnoreCase(className))
            return false;
        String mResultStr = context.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE).getString(
                KEY_GUIDE_ACTIVITY, "");// 取得所有类名 如 com.my.MainActivity
        if (mResultStr.equalsIgnoreCase("false"))
            return false;
        else
            return true;
    }

    public boolean isFirst() {

        if (BSApplication.getInstance().getUserFromServerVO() != null)
            return false;
        else
            return true;
    }

    // 缩放图片
    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                true);
        return newbm;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void initLoaction() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                // 初始化定位
                AMapLocationClient locationClient = new AMapLocationClient(getApplicationContext());
                // 初始化定位参数 // mPop = new BSUPloadPopWindows(mActivity, v, null, null, 0);
                AMapLocationClientOption locationOption = new AMapLocationClientOption();
                LocationUtils.getLatLonForOneLoaction(getApplicationContext(), locationClient, locationOption);
            }
        }.start();

    }

}
