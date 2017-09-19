
package com.beisheng.base.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beisheng.base.R;
import com.beisheng.base.cache.ACache;
import com.beisheng.base.crouton.Crouton;
import com.beisheng.base.crouton.Style;
import com.beisheng.base.database.CacheDbHelper;
import com.beisheng.base.http.HttpUtils;
import com.beisheng.base.interfaces.UpdateCallback;
import com.beisheng.base.sliding.IntentUtils;
import com.beisheng.base.sliding.SlidingActivity;
import com.beisheng.base.utils.DialogUtil;
import com.beisheng.base.utils.Options;
import com.beisheng.base.utils.StringUtils;
import com.beisheng.base.utils.ThreadUtil;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;
import java.util.Map;

public abstract class BaseActivity extends SlidingActivity implements UpdateCallback {
    public TextView mBaseBackTv, mBaseOkTv, mBaseTitleTv, base_back_tv_finish,mNoContentTv, mNoNetTv;
    public LinearLayout mBaseContentLayout;
    public RelativeLayout mBaseHeadLayout;
    private CacheDbHelper mDbHelper;
    private Dialog mProgressDialog;
    private boolean mNeedBackGesture = false;
    public ImageLoader mImageLoader;
    public DisplayImageOptions mOptions;
    public Gson mGson = new Gson();
    public String mUrl;

    public abstract void baseSetContentView();

    public abstract boolean getDataResult();

    public abstract void initView();

    public abstract void bindViewsListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.base_activity);
        showProgressDialog();
        baseInitView();
        baseBindViewListener();
        baseSetContentView();
        initView();
        bindViewsListener();
        baseGetData();
    }

    public void baseInitView() {
        mBaseHeadLayout = (RelativeLayout) findViewById(R.id.base_head_layout);
        mBaseBackTv = (TextView) findViewById(R.id.base_back_tv);
        mBaseOkTv = (TextView) findViewById(R.id.base_ok_tv);
        mBaseTitleTv = (TextView) findViewById(R.id.base_title_tv);
        base_back_tv_finish = (TextView) findViewById(R.id.base_back_tv_finish);

        mBaseContentLayout = (LinearLayout) findViewById(R.id.base_content_layout);
        mNoContentTv = (TextView) findViewById(R.id.no_content_tv);
        mNoNetTv = (TextView) findViewById(R.id.no_net_tv);
        mDbHelper = new CacheDbHelper(this);
        mOptions = Options.getListOptions();
        mImageLoader = ImageLoader.getInstance();

    }

    public void baseBindViewListener() {
        mBaseBackTv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                BaseActivity.this.finish();
            }
        });

        // mNoNetTv.setOnClickListener(new OnClickListener() {
        //
        // @Override
        // public void onClick(View arg0) {
        // new ThreadUtil(BaseActivity.this, BaseActivity.this).start();
        // }
        // });
    }

    public void baseGetData() {
        new ThreadUtil(this, this).start();
    }

    @Override
    public boolean execute() {
        return getDataResult();
    }

    @Override
    public void executeSuccess() {
        dismissProgressDialog();
        mBaseContentLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void executeFailure() {
        dismissProgressDialog();
        mBaseContentLayout.setVisibility(View.GONE);
    }

    // 没有网络从数据库获取缓存
    public String getCacheFromDatabase(String url, Map<String, String> map) {

        return mDbHelper.queryJsonByKeyName(getKey(url, map));
    }

    // 有网路的时候保存jsonstrl
    public void saveJsonCache(String url, Map<String, String> map, String jsonStr) {
        mDbHelper.insertOrUpdate(getKey(url, map), jsonStr);
    }

    public void deleteByKey(String key) {
        mDbHelper.delete(key);
    }

    public List<String> queryLike(String likeKey) {
        return mDbHelper.queryLike(likeKey);
    }

    public void deleteLike(String likeKey) {
        mDbHelper.deleteLike(likeKey);
    }

    private String getKey(String url, Map<String, String> map) {
        StringBuffer sb = new StringBuffer();
        sb.append(url);
        if (map == null || map.size() == 0)
            return url;
        for (String key : map.keySet()) {
            if ("".equals(map.get(key)) || map.get(key) == null)
                continue;
            sb.append("/").append(key).append("/").append(map.get(key));
        }
        return sb.toString();
    }

    // 显示dialog
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
    public void openActivity(Class<?> pClass) {
        openActivity(pClass, null, 0);

    }

    public void openActivityForResult(Class<?> pClass, int requestCode) {
        openActivity(pClass, null, requestCode);
    }

    // 更具类打开acitvity,并携带参数
    public void openActivity(Class<?> pClass, Bundle pBundle, int requestCode) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        if (requestCode == 0) {
            Log.e("openActivity","pClass"+pClass.getSimpleName().toString());
            IntentUtils.startPreviewActivity(this, intent, 0);
            // startActivity(intent);
        } else {
            IntentUtils.startPreviewActivity(this, intent, requestCode);
            // startActivityForResult(intent, requestCode);
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
            IntentUtils.startPreviewActivity(this, intent, 0);
            // startActivity(intent);
        } else {
            IntentUtils.startPreviewActivity(this, intent, requestCode);
            // startActivityForResult(intent, requestCode);
        }
        actityAnim();
    }

    public void actityAnim() {
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    // 判断是否有网络

    public boolean hasNetWork() {
        return HttpUtils.isNetworkAvailable(this);
    }

    // 显示LongToast

    public void showShortToast(String pMsg) {
        Toast.makeText(this, pMsg, Toast.LENGTH_SHORT).show();
    }

    // 显示ShortToast
    public void showCustomToast(String pMsg) {
        Log.e("showCustomToast","showCustomToast");
        Crouton.makeText(this, pMsg, Style.ALERT, R.id.toast_conten).show();
    }

    public void setSwipeRefreshLayoutColors(SwipeRefreshLayout layout) {
        layout.setColorSchemeResources(R.color.C7, R.color.C9, R.color.C10);
    }

    /**
     * 设置缓存数据（key,value）
     */
    public void setCacheStr(String key, String value) {
        if (!StringUtils.isEmpty(value)) {
            ACache.get(this).put(key, value);
        }
    }

    /**
     * 设置缓存数据（key,value）
     */
    public void setCacheStrTime(String url, Map<String, String> map, String value, int time) {
        if (!StringUtils.isEmpty(value)) {
            ACache.get(this).put(getKey(url, map), value, time);
        }
    }

    /**
     * 获取缓存数据更具key
     */
    public String getCacheStr(String url, Map<String, String> map) {
        return ACache.get(this).getAsString(getKey(url, map));
    }

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
            int[] l = {
                    0, 0
            };
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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
}
