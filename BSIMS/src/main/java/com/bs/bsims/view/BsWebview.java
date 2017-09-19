
package com.bs.bsims.view;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BsWebview {

    public static void SetWebview(WebView view, String url) {
        WebSettings webSettings = view.getSettings();
        webSettings.setJavaScriptEnabled(true); // 支持js
        webSettings.setUseWideViewPort(false); // 将图片调整到适合webview的大小
        webSettings.setSupportZoom(true); // 支持缩放
        webSettings.supportMultipleWindows(); // 多窗口
        webSettings.setAppCacheEnabled(false);// 设置不进行缓存
        webSettings.setAppCachePath("");
        webSettings.setDatabaseEnabled(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 关闭webview中缓存
        webSettings.setAllowFileAccess(true); // 设置可以访问文件
        webSettings.setNeedInitialFocus(true); // 当webview调用requestFocus时为webview设置节点
        webSettings.setBuiltInZoomControls(true); // 设置支持缩放
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false); // 支持通过JS打开新窗口
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        view.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        webSettings.setLoadsImagesAutomatically(true); // 支持自动加载图片
        webSettings.setDomStorageEnabled(true);
        view.loadUrl(url);
    }
}
