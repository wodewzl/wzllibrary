
package com.beisheng.base.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class WebviewUtil {

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
        webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        // setWebViewFontSize(webView);
    }

    private String initContent(String content, boolean night, boolean flag, Context context) {
        try {
            InputStream inputStream = context.getResources().getAssets().open("discover.html");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream), 16 * 1024);
            StringBuilder sBuilder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sBuilder.append(line + "\n");
            }
            String modelHtml = sBuilder.toString();
            inputStream.close();
            reader.close();

            String contentNew = modelHtml.replace("<--@#$%discoverContent@#$%-->", content);
            if (night) {
                contentNew = contentNew.replace("<--@#$%colorfontsize2@#$%-->", "color:#8f8f8f ;");
            } else {
                contentNew = contentNew.replace("<--@#$%colorfontsize2@#$%-->", "color:#333333 ;");
            }
            if (flag) {
                contentNew = contentNew.replace("<--@#$%colorbackground@#$%-->", "background:#B4CDE6");
            } else {
                contentNew = contentNew.replace("<--@#$%colorbackground@#$%-->", "background:#F9BADA");
            }
            return contentNew;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置字体大小
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public static void setWebViewFontSize(Context context, final WebView mWebView) {
        // String fontSize = BaseAppApplication.getInstance().getFontSize();
        String fontSize = SharePreferenceUtil.getSharedpreferenceValue(context, "webview_font_size", "size");
        mWebView.getSettings().setDefaultFontSize(Integer.parseInt(fontSize));
    }
}
