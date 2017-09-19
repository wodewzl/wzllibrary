package com.bs.luzhongbao;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LogoActivity extends Activity  {
    public static final String APP_VERSION = "V2.0";
    private ImageView mLogoImageView, mAvdImg;
    private String mImageUrl;
    private CountDownTimer mTimer;
    private TextView mTimeTv;
    private LinearLayout mTimeLayout, mTimeRootLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo_activity);
        initView();
        bindViewsListener();

    }

    public void bindViewsListener() {
    }

    // corners 为边框圆角（单位为px ） frame 为边框颜色，content为内部填充颜色
    public static GradientDrawable setBackgroundShap(Context context, int corners, String frameColor, String contentColor) {
        int strokeWidth = 2; // 3dp 边框宽度
        int roundRadius = 10; // 8dp 圆角半径
        int strokeColor = Color.parseColor(frameColor);// 边框颜色
        int fillColor = Color.parseColor(contentColor);// 内部填充颜色
        GradientDrawable gd = new GradientDrawable();// 创建drawable
        gd.setColor(fillColor);
        gd.setCornerRadius(5);
        gd.setStroke(strokeWidth, strokeColor);
        return gd;
    }

    @SuppressLint("NewApi")
    public void initView() {
        mLogoImageView = (ImageView) findViewById(R.id.logo_image);
        mLogoImageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent= new Intent();
                intent.setClass(LogoActivity.this,MainActivity.class);
                LogoActivity.this.startActivity(intent);
                LogoActivity.this.finish();
            }
        },2000);

        mTimeTv = (TextView) findViewById(R.id.time_tv);
        mTimeLayout = (LinearLayout) findViewById(R.id.time_layout);
        mTimeLayout.setBackground(setBackgroundShap(this, 20, "#30000000", "#30000000"));
        mTimeRootLayout = (LinearLayout) findViewById(R.id.time_root_layout);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null)
            mTimer.cancel();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case 1:
                mTimer.start();
                break;

            default:
                break;
        }
    }


}
