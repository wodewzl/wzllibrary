
package com.bs.bsims.activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.interfaces.UpdateCallback;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSLoadingView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseActivity extends FragmentActivity implements UpdateCallback {
    public TextView mTitleTv, mOkTv, mLoading, mHeadBackImag;
    public ImageView mLoadingImg, mAtivityName_img;
    public LinearLayout mContentLayout, mLoadingLayout, txt_comm_head_activityNamefather, baseHeadLayout;
    public View mHeadLayout;
    public BSLoadingView mLoadingaAimation;

    private RotateAnimation mAnim;
    private static final int CHANGE_TITLE_WHAT = 1;
    private static final int CHNAGE_TITLE_DELAYMILLIS = 300;
    private static final int MAX_SUFFIX_NUMBER = 3;
    private static final char SUFFIX = '·';
    private int num = 0;

    private boolean mFlage;
    private PopupWindow mOkPop;
    private List<PopupWindow> mListpop = new ArrayList<PopupWindow>();

    public abstract void baseSetContentView();

    public abstract boolean getDataResult();

    public abstract void updateUi();

    public abstract void initView();

    public abstract void bindViewsListener();

    private View mBottomLine;

    public String leadClass;

    // private FlakeView flakeView;
    // private LinearLayout mLinearLayoutSonw;
    // Handler handlerRain = new Handler();
    // Runnable runnableRain = new Runnable() {
    // @Override
    // public void run() {
    // // TODO Auto-generated method stub
    // flakeView.addFlakes(15);
    // handlerRain.postDelayed(runnableRain, 2000);
    // if (flakeView.getNumFlakes() > 70)
    // {
    // handlerRain.removeCallbacks(runnableRain);
    // }
    // }
    // };

    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            if (msg.what == CHANGE_TITLE_WHAT) {
                // StringBuilder builder = new StringBuilder();
                // builder.append("正在加载");
                // if (num >= MAX_SUFFIX_NUMBER) {
                // num = 0;
                // }
                // num++;
                // for (int i = 0; i < num; i++) {
                // builder.append(SUFFIX);
                // }
                // mLoading.setText(builder.toString());
                // handler.sendEmptyMessageDelayed(CHANGE_TITLE_WHAT,
                // CHNAGE_TITLE_DELAYMILLIS);
                mLoadingaAimation.setVisibility(View.GONE);
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base);
        baseInitView();
        baseBindListeners();
        baseSetContentView();
        initView();
        bindViewsListener();
        baseGetData();
//        addSwipBak();
    }

    public void baseInitView() {
        mLoadingaAimation = (BSLoadingView) findViewById(R.id.loading_animation);
        mTitleTv = (TextView) findViewById(R.id.txt_comm_head_activityName);
        mHeadBackImag = (TextView) findViewById(R.id.img_head_back);
        mAtivityName_img = (ImageView) findViewById(R.id.txt_comm_head_activityName_img);
        mOkTv = (TextView) findViewById(R.id.txt_comm_head_right);
        mBottomLine = findViewById(R.id.bottom_line);
        mLoading = (TextView) findViewById(R.id.loading);
        mContentLayout = (LinearLayout) findViewById(R.id.content_layout);
        mLoadingLayout = (LinearLayout) findViewById(R.id.loading_layout);
        mHeadLayout = findViewById(R.id.head_layout);
        mLoadingImg = (ImageView) findViewById(R.id.iv_route);
        txt_comm_head_activityNamefather = (LinearLayout) findViewById(R.id.txt_comm_head_activityNamefather);
        baseHeadLayout = (LinearLayout) findViewById(R.id.base_view);
        try {
            // 透明状态栏 4.4以后才可以显示
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        // startAnim();

        // flakeView = new FlakeView(this);
        // mLinearLayoutSonw = (LinearLayout)
        // findViewById(R.id.ly_content_sonw);
        // mLinearLayoutSonw.addView(flakeView);
        // mLinearLayoutSonw.setVisibility(View.VISIBLE);
        // handlerRain.postDelayed(runnableRain, 0);
    }

    public void baseBindListeners() {
        mHeadBackImag.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity.this.finish();
            }
        });
    }

    public void baseGetData() {
        new ThreadUtil(this, this).start();
    }

    @Override
    public boolean execute() {
        mFlage = getDataResult();
        if (!mFlage) {
            // mLoadingaAimation.setVisibility(View.GONE);
            handler.sendEmptyMessage(1);
        }
        return mFlage;
    }

    @Override
    public void executeSuccess() {
        mLoadingLayout.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);
        // num = 0;
        updateUi();
    }

    @Override
    public void executeFailure() {
        mLoadingLayout.setVisibility(View.VISIBLE);
        mContentLayout.setVisibility(View.GONE);
        mLoadingaAimation.setVisibility(View.GONE);
        CommonUtils.setNonetIcon(this, mLoading, this);
    }

    public void setNonetIcon() {
        Drawable drawable = this.getResources().getDrawable(R.drawable.no_net);
        mLoadingaAimation.setVisibility(View.GONE);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); // 设置边界
        mLoading.setCompoundDrawables(null, drawable, null, null);
        mLoading.setCompoundDrawablePadding(10);
        mLoading.setText("请检查网络，重新尝试！");
        mLoading.setTextColor(Color.parseColor("#666666"));
    }

    public void setNonetIcon(String text) {
        Drawable drawable = this.getResources().getDrawable(R.drawable.no_net);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); // 设置边界
        mLoadingaAimation.setVisibility(View.GONE);
        mLoading.setCompoundDrawables(null, drawable, null, null);
        mLoading.setCompoundDrawablePadding(10);
        mLoading.setText(text);
        mLoading.setTextColor(Color.parseColor("#666666"));
    }

    private void startAnim() {
        mAnim = new RotateAnimation(360, 0, Animation.RESTART, 0.5f, Animation.RESTART, 0.5f);
        mAnim.setDuration(2000);
        mAnim.setRepeatCount(Animation.INFINITE);
        mAnim.setRepeatMode(Animation.RESTART);
        mAnim.setStartTime(Animation.START_ON_FIRST_FRAME);
        mLoadingImg.startAnimation(mAnim);
        handler.sendEmptyMessage(CHANGE_TITLE_WHAT);
    }

    public void showContentView() {
        mLoadingaAimation.setVisibility(View.GONE);
        mLoading.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
    }

    public void showNoContentView() {
        mLoadingaAimation.setVisibility(View.GONE);
        mLoading.setVisibility(View.VISIBLE);
        mContentLayout.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.VISIBLE);
        CommonUtils.setNonetContent(this, mLoading, "没有相关信息");
    }

    public void showNoNetView() {
        mLoadingaAimation.setVisibility(View.GONE);
        mLoading.setVisibility(View.VISIBLE);
        mContentLayout.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.VISIBLE);
        CommonUtils.setNonetIcon(this, mLoading, this);
    }

    public void isRequestFinish() {
        mContentLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
    }

    public void commit(final Activity activity, RequestParams params, String interfaceUrl) {
        CustomDialog.showProgressDialog(activity, "正在提交数据...");

        try {
            params.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            params.put("userid", BSApplication.getInstance().getUserId());
            String url = BSApplication.getInstance().getHttpTitle() + interfaceUrl;
            AsyncHttpClient client = new AsyncHttpClient();
            client.post(url, params, new AsyncHttpResponseHandler() {

                @Override
                public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                    CustomDialog.closeProgressDialog();
                }

                @Override
                public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                    try {
                        JSONObject jsonObject = new JSONObject(new String(arg2));
                        String str = (String) jsonObject.get("retinfo");
                        String code = (String) jsonObject.get("code");
                        if (Constant.RESULT_CODE.equals(code)) {
                            updateUi();
                            activity.finish();
                        }
                        CustomToast.showLongToast(activity, str);
                    } catch (Exception e) {
                        activity.finish();
                        e.printStackTrace();
                    } finally {
                        CustomDialog.closeProgressDialog();
                    }

                }
            });
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void initLeadView() {
        LinearLayout.LayoutParams okTvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);// 定义文本显示组件
        okTvParams.rightMargin = CommonUtils.dip2px(this, 10);
        LinearLayout.LayoutParams okImgParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 300);// 定义文本显示组件
        okImgParams.gravity = Gravity.RIGHT;
        okImgParams.rightMargin = CommonUtils.dip2px(this, 25) + CommonUtils.getViewWidth(mBottomLine) / 2;
        mOkPop = CommonUtils.leadPop(this, mBottomLine, "亲，有需要了可以发布一下哦", okTvParams, okImgParams, 1);
        mListpop.add(mOkPop);
        CommonUtils.okLeadPop(this, mContentLayout, mListpop, 0);
    }

    public String getLeadClass() {
        return leadClass;
    }

    public void setLeadClass(String leadClass) {
        this.leadClass = leadClass;
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

//    public void addSwipBak() {
//        Slidr.attach(this);
//    }
}
