
package com.xiaojing.shop.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.mode.EBMessageVO;
import com.wuzhanglong.library.utils.ShareUtil;
import com.wuzhanglong.library.utils.WebViewPictureUtil;
import com.xiaojing.shop.R;
import com.xiaojing.shop.application.AppApplication;

import org.greenrobot.eventbus.EventBus;

import static com.wuzhanglong.library.utils.WebviewUtil.SetWebview;


public class WebViewActivity extends BaseActivity implements OnClickListener, OnLongClickListener, WebViewPictureUtil.JSCallBack {
    private static final int FILE_SELECT_CODE = 0;
    private static final int LOGIN_CODE = 1;
    private WebView mWebView;
    private ProgressBar progressbar;
    private String mResult;
    public JsObject mJsObject;
    private ValueCallback<Uri> mUploadMessage;//回调图片选择，4.4以下
    private ValueCallback<Uri[]> mUploadCallbackAboveL;//回调图片选择，5.0以上

    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.webview_activity);
    }

    @Override
    public void initView() {

        mWebView = (WebView) this.findViewById(R.id.webview);

        SetWebview(mWebView);
////    mWebView.setWebViewClient(new WebViewPictureUtil(this, mWebView, "img", "this.src"));

        progressbar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 5));
        Drawable drawable = this.getResources().getDrawable(R.drawable.webview_progress);
        progressbar.setProgressDrawable(drawable);
        mWebView.addView(progressbar);
        initDada();

        mJsObject = new JsObject();
        mWebView.addJavascriptInterface(mJsObject, "jsObject");

//        WebViewPictureUtil wp = new WebViewPictureUtil(mActivity, mWebView, this);
//        mWebView.setWebViewClient(wp);
    }

    public void initDada() {
        Intent intent = this.getIntent();
        mResult = this.getIntent().getStringExtra("result");

        if (this.getIntent().hasExtra("title")) {
            mBaseHeadLayout.setVisibility(View.VISIBLE);
            mBaseTitleTv.setText(this.getIntent().getStringExtra("title"));
        } else {
            mBaseHeadLayout.setVisibility(View.GONE);
        }
        if (intent.getStringExtra("url") != null && !"".equals(intent.getStringExtra("url"))) {
            String url = this.getIntent().getStringExtra("url");
            mWebView.loadUrl(url);
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
            public void onGeolocationPermissionsShowPrompt(String origin, Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

            // For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILE_SELECT_CODE);

            }

            // For Android 3.0+
            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "File Browser"), FILE_SELECT_CODE);
            }

            // For Android 4.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILE_SELECT_CODE);

            }

            // For Android 5.0+
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                mUploadCallbackAboveL = filePathCallback;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(
                        Intent.createChooser(i, "File Browser"),
                        FILE_SELECT_CODE);
                return true;
            }

        });

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if ((url.startsWith("http:") || url.startsWith("https:")) && !url.endsWith(".apk")) {
                    return super.shouldOverrideUrlLoading(view, url);
                } else if (url.startsWith("itms-services:")) {
                    return true;
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dismissProgressDialog();
            }
        });

    }

    @Override
    public void getData() {
//        HttpClientUtil.show(mThreadUtil);
    }

    @Override
    public void hasData(BaseVO vo) {

    }

    @Override
    public void noData(BaseVO vo) {

    }

    @Override
    public void noNet() {

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
//        return false;
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

    @Override
    public String jsCallBack(int type, String[] array, WebViewPictureUtil.JsObject jsObject) {

        String str = AppApplication.getInstance().getUserInfoVO().getKey();
        return str;
    }


    class JsObject {
        @JavascriptInterface
        public String onViewClick(String[] array) {
            int type = Integer.parseInt(array[0]);
            if (type == 1) {
                if (AppApplication.getInstance().getUserInfoVO() == null) {
                    Intent intent = new Intent();
                    intent.putExtra("type", "2");//type 是post 结束类型
                    intent.setClass(WebViewActivity.this, LoginActivity.class);
                    WebViewActivity.this.startActivity(intent);
                } else {
                    return AppApplication.getInstance().getUserInfoVO().getKey();
                }
            } else if (type == 2) {
                EventBus.getDefault().post(new EBMessageVO("back_webview"));
                WebViewActivity.this.finish();
            } else if (type == 3) {
                if (AppApplication.getInstance().getUserInfoVO() == null) {
                    Intent intent = new Intent();
                    intent.putExtra("type", "2");//type 是post 结束类型
                    intent.setClass(WebViewActivity.this, LoginActivity.class);
                    WebViewActivity.this.startActivityForResult(intent, LOGIN_CODE);
                    return null;
                }
                Bundle bundle = new Bundle();
                bundle.putString("cart_info", array[1]);
                bundle.putString("ifcart", "0");
                open(OrderSureActivity.class, bundle, 0);
            } else if (type == 4) {
                EventBus.getDefault().post(new EBMessageVO("select_shop_cart"));
                WebViewActivity.this.finish();
            } else if (type == 5) {
                if (AppApplication.getInstance().getUserInfoVO() == null) {
                    return null;
                }
                return AppApplication.getInstance().getUserInfoVO().getKey();
            } else if (type == 6) {
                ShareUtil.share(WebViewActivity.this, array[3], array[1], array[2], array[4]);
            } else if (type == 7) {
                EventBus.getDefault().post(new EBMessageVO("kuaiqian_pay"));
                WebViewActivity.this.finish();
            } else if (type == 8) {
                Bundle bundle = new Bundle();
                bundle.putString("is_merchant", "1");
                open(CardRechargeActivity.class, bundle, 0);
            } else if (type == 9) {
                EventBus.getDefault().post(new EBMessageVO("back_webview"));
            } else if (type == 10) {
                Bundle bundle = new Bundle();
                bundle.putString("keyword", array[1]);
                mActivity.open(ShopListActivity.class, bundle, 0);
            }
            return null;
        }

        public void callHtmlJS(final String str1) {
            WebViewActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    mWebView.loadUrl("javascript:getKey(" + str1 + "," + str2 + ")");
                    mWebView.loadUrl("javascript:getKey(" + str1 + ")");
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (intent == null) {
            return;
        }

        if (resultCode == RESULT_OK && requestCode == FILE_SELECT_CODE) {
            if (Build.VERSION.SDK_INT >= 21) {//5.0以上版本处理
                Uri uri = intent.getData();
                Uri[] uris = new Uri[]{uri};
                mUploadCallbackAboveL.onReceiveValue(uris);//回调给js
            } else {//4.4以下处理
                Uri uri = intent.getData();
                mUploadMessage.onReceiveValue(uri);
            }
        } else {
            mWebView.reload();
        }
    }

    private void emulateShiftHeld(KeyEvent.Callback view) {
        try {
            KeyEvent shiftPressEvent = new KeyEvent(0, 0, KeyEvent.ACTION_DOWN,
                    KeyEvent.KEYCODE_SHIFT_LEFT, 0, 0);
            shiftPressEvent.dispatch(view);
        } catch (Exception e) {
        }
    }
}
