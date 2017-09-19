package com.wuzhanglong.library.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.vondear.rxtools.view.RxToast;
import com.wuzhanglong.library.R;
import com.wuzhanglong.library.interfaces.UpdateCallback;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.utils.DialogUtil;
import com.wuzhanglong.library.utils.Options;
import com.wuzhanglong.library.utils.StatusBarCompat;
import com.wuzhanglong.library.utils.ThreadUtil;

/**
 * Created by Administrator on 2017/3/7.
 */
//SwipeBackActivity
public abstract class BaseActivity extends AppCompatActivity implements UpdateCallback {
    public abstract void baseSetContentView();

    public abstract void initView();

    public abstract void bindViewsListener();

    public abstract void getData();

    public abstract void hasData(BaseVO vo);

    public abstract void noData(BaseVO vo);

    public abstract void noNet();


    public BaseActivity mActivity;
    public TextView mBaseBackTv, mBaseOkTv, mBaseTitleTv;
    public RelativeLayout mBaseHeadLayout;
    //    public Toolbar mBaseHeadLayout;
    public TextView mNoContentTv, mNoNetTv;
    public LinearLayout mBaseContentLayout;
    private Dialog mProgressDialog;
    public ImageLoader mImageLoader;
    public DisplayImageOptions mOptions;
    public ThreadUtil mThreadUtil;
    public  Bundle mSavedInstanceState;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSavedInstanceState=savedInstanceState;
        mActivity = this;
        setContentView(R.layout.base_activity);
//      addContentView(R.layout.base_activity);
        setStatusBar();
        showProgressDialog();
        baseInitView();
        baseBindViewListener();
        baseSetContentView();
        initView();
        bindViewsListener();

        mThreadUtil = new ThreadUtil(this, this);
        mThreadUtil.start();

    }


    public void baseInitView() {
        mBaseHeadLayout = (RelativeLayout) findViewById(R.id.base_head_layout);
        mBaseBackTv = (TextView) findViewById(R.id.base_back_tv);
        mBaseOkTv = (TextView) findViewById(R.id.base_ok_tv);
        mBaseTitleTv = (TextView) findViewById(R.id.base_title_tv);
        mBaseContentLayout = (LinearLayout) findViewById(R.id.base_content_layout);
        mNoContentTv = (TextView) findViewById(R.id.no_content_tv);
        mNoNetTv = (TextView) findViewById(R.id.no_net_tv);
        mOptions = Options.getListOptions();
        mImageLoader = ImageLoader.getInstance();

        //滑动返回
//        addSwipBak();
    }

//    public void addSwipBak() {
//        Slidr.attach(this);
//    }

    protected void setStatusBar() {

//        StatusBarUtil.setColor(this, getResources().getColor(R.color.C7),0);
//        StatusBarUtil.setTranslucentForImageView(this, 0, mBaseHeadLayout);
        SetTranslanteBar();
        //我草你妈妈的面对面的面对面的
    }

    public void baseBindViewListener() {
        mBaseBackTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                BaseActivity.this.finish();
            }
        });
        mNoNetTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
//                getData();
//                mThreadUtil.start();
                mThreadUtil = new ThreadUtil(mActivity, BaseActivity.this);
//                mThreadUtil.start();
            }
        });
    }

    protected void contentInflateView(int id) {
        View.inflate(this, id, mBaseContentLayout);
    }

    /**
     * 查找View
     *
     * @param id   控件的id
     * @param <VT> View类型
     * @return
     */
    protected <VT extends View> VT getViewById(@IdRes int id) {
        return (VT) findViewById(id);
    }

    //     显示dialog
    public void showProgressDialog() {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = DialogUtil.createLoadingDialog(this);
            }
            mProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 隐藏dialog
    public void dismissProgressDialog() {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // dialog是否显示
    public boolean isShow() {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 更具类打开acitvity
    public void openActivity(final Class<?> pClass) {
        open(pClass, null, 0);
//        mBaseHeadLayout.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }, 1000);


    }

    public void openActivityForResult(Class<?> pClass, int requestCode) {
        open(pClass, null, requestCode);
    }

    //     更具类打开acitvity,并携带参数
    public void open(Class<?> pClass, Bundle pBundle, int requestCode) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        if (requestCode == 0) {
            startActivity(intent);
        } else {
            startActivityForResult(intent, requestCode);
        }
        actityAnim();
    }

    public void openNewTaskActivity(Class<?> pClass, Bundle pBundle, int requestCode) {
        Intent intent = new Intent(this, pClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        if (requestCode == 0) {
            startActivity(intent);
        } else {
            startActivityForResult(intent, requestCode);
        }
        actityAnim();
    }

    public void actityAnim() {
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    // 显示ShortToast
    public void showCustomToast(String pMsg) {
//        Crouton.makeText(this, pMsg, Style.ALERT, R.id.toast_conten).show();
        RxToast.info(this, pMsg, Toast.LENGTH_LONG, true).show();

//        RxToast.error(this, "这是一个提示错误的Toast！", Toast.LENGTH_SHORT, true).show();
//
//        RxToast.success(this, "这是一个提示成功的Toast!", Toast.LENGTH_SHORT, true).show();
//
//        RxToast.info(this, "这是一个提示信息的Toast.", Toast.LENGTH_SHORT, true).show();
//
//        RxToast.warning(this, "这是一个提示警告的Toast.", Toast.LENGTH_SHORT, true).show();

//        RxToast.normal(this, "这是一个普通的没有ICON的Toast").show();

//        Drawable icon = getResources().getDrawable(R.drawable.set);
//        RxToast.normal(this, "这是一个普通的包含ICON的Toast", icon).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public void setSwipeRefreshLayoutColors(SwipeRefreshLayout layout) {
        layout.setColorSchemeResources(R.color.C7, R.color.C9, R.color.C10);
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY()
                    < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context
                    .INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }


    public void baseHasData(BaseVO vo) {
        dismissProgressDialog();
        mBaseContentLayout.setVisibility(View.VISIBLE);
        if (vo != null)
            hasData(vo);
    }

    public void baseNoData(BaseVO vo) {
        dismissProgressDialog();
        mBaseContentLayout.setVisibility(View.GONE);
        mNoContentTv.setVisibility(View.VISIBLE);
        noData(vo);
    }

    @Override
    public void execute() {
        getData();
    }

    public void baseNoNet() {
        dismissProgressDialog();
        mBaseContentLayout.setVisibility(View.GONE);
        mNoNetTv.setVisibility(View.VISIBLE);
        noNet();
    }

    @Override
    public void show() {
        showView();
    }

    @Override
    public void finishActivity(final Activity activity, final int code) {
        mBaseContentLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (code == 1) {
                    activity.setResult(1);
                    activity.finish();
                } else {
                    activity.finish();
                }

            }
        }, 1000);
    }

    public void finishActivity(final Activity activity) {
        mBaseContentLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                activity.finish();

            }
        }, 1000);
    }

    public void showView() {
        dismissProgressDialog();
        mBaseContentLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 着色状态栏（4.4以上系统有效）
     */
    protected void SetStatusBarColor() {
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorPrimary));
    }

    /**
     * 着色状态栏（4.4以上系统有效）
     */
    protected void SetStatusBarColor(int color) {
        StatusBarCompat.setStatusBarColor(this, color);
    }

    /**
     * 沉浸状态栏（4.4以上系统有效）
     */
    public void SetTranslanteBar() {
        StatusBarCompat.translucentStatusBar(this);
    }


}
