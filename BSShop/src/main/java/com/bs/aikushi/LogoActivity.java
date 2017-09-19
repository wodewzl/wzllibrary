package com.bs.aikushi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import cz.msebera.android.httpclient.Header;

public class LogoActivity extends Activity implements OnClickListener {
    public static final String APP_VERSION = "V2.0";
    private ImageView mLogoImageView, mAvdImg;
    private String mImageUrl;
    private LogoVO mLogoVO;
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
        getData();
    }

    public void bindViewsListener() {
        mLogoImageView.setOnClickListener(this);
        // mTimeLayout.setOnClickListener(this);
        mTimeRootLayout.setOnClickListener(this);
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

        mTimeTv = (TextView) findViewById(R.id.time_tv);
        mTimeLayout = (LinearLayout) findViewById(R.id.time_layout);
        mTimeLayout.setBackground(setBackgroundShap(this, 20, "#30000000", "#30000000"));
        mTimeRootLayout = (LinearLayout) findViewById(R.id.time_root_layout);

    }

    public void getData() {
        final RequestParams params = new RequestParams();
        final Gson gson = new Gson();
        final String url = "http://aikushi.test.beisheng.wang/mobile/index.php?act=zt&op=startPage";
        final String cacheStr = ACache.get(this).getAsString(url);
        mLogoVO = (LogoVO) gson.fromJson(cacheStr, LogoVO.class);
        if (mLogoVO != null)
            Picasso.with(LogoActivity.this).load(mLogoVO.getDatas().getImg()).into(mLogoImageView);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                String result = new String(arg2);
                if (result.equals(cacheStr)) {
                    initData();
                    return;
                }
                mLogoVO = gson.fromJson(result, LogoVO.class);
                if ("200".equals(mLogoVO.getCode())) {
                    Picasso.with(LogoActivity.this).load(mLogoVO.getDatas().getImg()).into(mLogoImageView);
                    try {
                        ACache.get(LogoActivity.this).put(url, result);
                        initData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                }
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

            }
        });

    }

    // 先使用本地数据，
    public void initData() {

        mTimer = new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long arg0) {
                mTimeTv.setText(arg0 / 1000 + "");
                Animation animation = AnimationUtils.loadAnimation(LogoActivity.this, R.anim.anim_textview_time);
                animation.reset();
                mTimeTv.startAnimation(animation);
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent();
                intent.setClass(LogoActivity.this, MainActivity.class);
                LogoActivity.this.startActivity(intent);
                LogoActivity.this.finish();
            }
        };
        mTimer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null)
            mTimer.cancel();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.logo_image:
                if ("".equals(mLogoVO.getUrl()))
                    return;

                if (mTimer != null) {
                    if (mLogoVO == null) {
                        intent.putExtra("url", "http://aikushi.test.beisheng.wang/");
                    } else {
                        intent.putExtra("url", mLogoVO.getDatas().getUrl());
                    }
                    intent.putExtra("result", "1");// 有返回结果
                    intent.setClass(this, MainActivity.class);
                    startActivityForResult(intent, 1);
                    mTimer.cancel();
                }
                break;

            case R.id.time_root_layout:
                if (mTimer != null) {

                    intent.setClass(LogoActivity.this, MainActivity.class);
                    LogoActivity.this.startActivity(intent);
                    this.finish();
                    mTimer.cancel();
                }
                break;

            default:
                break;
        }
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
