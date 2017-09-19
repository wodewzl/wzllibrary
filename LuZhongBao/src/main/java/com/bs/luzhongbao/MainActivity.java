package com.bs.luzhongbao;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import static com.bs.luzhongbao.R.id.webview;

public class MainActivity extends AppCompatActivity {
    private static final int FILE_SELECT_CODE = 0;
    // 显示定位
    private WebView mWebView;
    private ProgressBar progressbar;
    private ValueCallback<Uri> mUploadMessage;//回调图片选择，4.4以下
    private ValueCallback<Uri[]> mUploadCallbackAboveL;//回调图片选择，5.0以上
    public JsObject mJsObject;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = (WebView) findViewById(webview);
        SetWebview(mWebView);
        progressbar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.FILL_PARENT, 5));
        Drawable drawable = this.getResources().getDrawable(R.drawable.webview_progress);
        progressbar.setProgressDrawable(drawable);
        mWebView.addView(progressbar);
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
        EventBus.getDefault().register(this);
        if (this.getIntent().getStringExtra("url") != null) {
            String url = this.getIntent().getStringExtra("url");
            mWebView.loadUrl(url);
        } else {
            mWebView.loadUrl("http://www.lechuyigou.com/wap/");
        }

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    return super.shouldOverrideUrlLoading(view, url);
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
            }
        });

        mJsObject = new JsObject();
        mWebView.addJavascriptInterface(mJsObject, "jsObject");
        mWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });
    }

    class JsObject {
        @JavascriptInterface
        public String onViewClick(String[] array) {
            int type = Integer.parseInt(array[0]);
            if (type == 1) {
                JPushInterface.setAlias(MainActivity.this, array[1], new TagAliasCallback() {
                    @Override
                    public void gotResult(int i, String s, Set<String> set) {
                        System.out.println("===========>");
                    }
                });
            } else if (type == 2) {
                JPushInterface.setAlias(MainActivity.this, "", new TagAliasCallback() {
                    @Override
                    public void gotResult(int i, String s, Set<String> set) {
                        System.out.println("===========>");
                    }
                });
            }
            return null;
        }

        public void callHtmlJS(final String str1) {
            MainActivity.this.runOnUiThread(new Runnable() {
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

        if (intent == null) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK && requestCode == FILE_SELECT_CODE) {
            if (Build.VERSION.SDK_INT >= 21) {//5.0以上版本处理
                Uri uri = intent.getData();
                Uri[] uris = new Uri[]{uri};
                mUploadCallbackAboveL.onReceiveValue(uris);//回调给js
            } else {//4.4以下处理
                Uri uri = intent.getData();
                mUploadMessage.onReceiveValue(uri);
            }
        }
    }

    public void guide(String latitude, String longitude, String desName) {
        Intent intent;
        String url;
        //com.autonavi.minimap  高德包 com.baidu.BaiduMap百度报名
        try {
            if (isInstallByread("com.baidu.BaiduMap")) {
                intent = new Intent();
                url = "baidumap://map/direction?destination=" + latitude + "," + longitude + "&mode=driving&coord_type=gcj02&output=";
                Uri uri = Uri.parse(url);
                intent.setData(uri);
//                StringBuffer stringBuffer = new StringBuffer("baidumap://map/navi?location=")
//                        .append(latitude).append(",").append(longitude).append("&type=TIME");
//                 intent = new Intent(Intent.ACTION_VIEW, Uri.parse(stringBuffer.toString()));
//                intent.setPackage("com.baidu.BaiduMap");
            } else {
                url = "http://uri.amap.com/navigation?to=" + longitude + "," + latitude + "," + desName + "&&mode=car";
                intent = Intent.getIntent(url);
            }
        } catch (Exception e) {
            Uri uri = Uri.parse("http://m.amap.com/?to=" + latitude + "," + longitude + "(" + desName + ")&type=0&opt=1");
            intent = new Intent(Intent.ACTION_VIEW, uri);
        }
        startActivity(intent); //启动调用
    }

    /**
     * 判断是否安装目标应用
     *
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    @Subscribe
    public void onEventMainThread(EBMessageVO event) {
        if ("push".equals(event.getMessage())) {
            mWebView.loadUrl(event.getUrl());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //按下返回键并且webview界面可以返回
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void SetWebview(WebView webView) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // 支持js
        webSettings.setUseWideViewPort(false); // 将图片调整到适合webview的大小
        webSettings.setSupportZoom(true); // 支持缩放
        webSettings.supportMultipleWindows(); // 多窗口
        // webSettings.setAppCacheEnabled(false);// 设置不进行缓存
        // webSettings.setAppCachePath("");
        webSettings.setDatabaseEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 关闭webview中缓存
        webSettings.setAllowFileAccess(true); // 设置可以访问文件
        webSettings.setNeedInitialFocus(true); // 当webview调用requestFocus时为webview设置节点
        webSettings.setBuiltInZoomControls(false); // 设置支持缩放
        // webSettings.setJavaScriptCanOpenWindowsAutomatically(false); // 支持通过JS打开新窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); // 支持通过JS打开新窗口
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadsImagesAutomatically(true); // 支持自动加载图片

        webView.setScrollContainer(false);
        webView.setScrollbarFadingEnabled(false);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        // setWebViewFontSize(webView);
        webView.setVerticalScrollBarEnabled(false);
    }

    public void showTotast(String msg) {
        Crouton.makeText(this, msg, Style.ALERT, R.id.toast_conten).show();
    }

    private void emulateShiftHeld(KeyEvent.Callback view) {
        try {
            KeyEvent shiftPressEvent = new KeyEvent(0, 0, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_SHIFT_LEFT, 0, 0);
            shiftPressEvent.dispatch(view);
        } catch (Exception e) {
        }
    }
}
