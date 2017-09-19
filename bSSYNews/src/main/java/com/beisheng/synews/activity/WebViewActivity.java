
package com.beisheng.synews.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.utils.WebViewPictureUtil;
import com.beisheng.base.utils.WebviewUtil;
import com.beisheng.synews.fragment.BottomFragment;
import com.beisheng.synews.mode.LiveVO;
import com.im.zhsy.R;

public class WebViewActivity extends BaseActivity implements OnClickListener, OnLongClickListener {
    private WebView mWebView;
    private ProgressBar progressbar;
    private String mResult;
    private BottomFragment mBottomFragment;

    @Override
    public void baseSetContentView() {

        View.inflate(this, R.layout.webview_activity, mBaseContentLayout);

    }

    @Override
    public boolean getDataResult() {
        return true;
    }

    @Override
    public void initView() {
        mWebView = (WebView) this.findViewById(R.id.webview);
        WebviewUtil.SetWebview(mWebView);
        mWebView.setWebViewClient(new WebViewPictureUtil(this, mWebView, "img", "this.src"));

        progressbar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 5));
        Drawable drawable = this.getResources().getDrawable(R.drawable.webview_progress);
        progressbar.setProgressDrawable(drawable);
        mWebView.addView(progressbar);
        WebviewUtil.SetWebview(mWebView);
        WebSettings webSettings = mWebView.getSettings();
        initDada();
    }

    public void initDada() {
        Intent intent = this.getIntent();
        mBaseTitleTv.setText(intent.getStringExtra("name"));
        mResult = this.getIntent().getStringExtra("result");
        if (intent.getStringExtra("url") != null && !"".equals(intent.getStringExtra("url"))) {
            String url = this.getIntent().getStringExtra("url");
            mWebView.loadUrl(url);
        }

        LiveVO vo = (LiveVO) intent.getSerializableExtra("livevo");
        if (vo != null) {

            if (intent.getStringExtra("name") == null) {
                if ("7".equals(vo.getSuburl())) {
                    mBaseTitleTv.setText("日报");
                } else {
                    mBaseTitleTv.setText("爆料");
                }
            }

            try {
                // 底部View
                mBottomFragment = new BottomFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.bottom_layout, mBottomFragment);
                mBottomFragment.setLiveVo(vo);
                transaction.commit();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void bindViewsListener() {
        mBaseBackTv.setOnClickListener(this);
        mWebView.setOnLongClickListener(this);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressbar.setVisibility(View.INVISIBLE);
                } else {
                    if (View.INVISIBLE == progressbar.getVisibility()) {
                        progressbar.setVisibility(View.VISIBLE);
                    }
                    progressbar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin,
                    Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

        });

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                } else {
                    return super.shouldOverrideUrlLoading(view, url);
                }

                // view.loadUrl(url);
                // return true;
            }
        });

    }

    @Override
    public void onBackPressed() {

        if (mResult != null) {
            Intent intent = new Intent();
            this.setResult(1, intent);
            super.onBackPressed();
        } else {
            if (mWebView.canGoBack()) {
                mWebView.goBack(); // goBack()表示返回WebView的上一页面
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (mResult != null) {
            Intent intent = new Intent();
            this.setResult(1, intent);
            this.finish();
        } else {
            if (mWebView.canGoBack()) {
                mWebView.goBack(); // goBack()表示返回WebView的上一页面
            } else {
                this.finish();
            }
        }

    }

    @Override
    public boolean onLongClick(View arg0) {
        return true;
    }

    public void onPause() {// 继承自Activity
        super.onPause();
        mWebView.onPause();
    }

    public void onResume() {// 继承自Activity
        super.onResume();
        mWebView.onResume();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
