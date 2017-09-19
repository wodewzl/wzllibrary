package com.beisheng.synews.activity;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.synews.application.AppApplication;
import com.beisheng.synews.utils.LogUtil;
import com.beisheng.synews.utils.ShareUtil;
import com.im.zhsy.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Map;

/**
 * created：gaoyao on 2016/11/26 15:22
 */
public class MoreWebViewActivity extends BaseActivity {


    private WebView webview;
    private String url;
    private Map<String, String> headers;
    private ProgressBar progressbar;

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
        webview = (WebView) this.findViewById(R.id.webview);
        url = getIntent().getStringExtra("url");
        LogUtil.e("MoreWebViewActivity",url);
        headers = new HashMap<String, String>();
        progressbar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 5));
        Drawable drawable = this.getResources().getDrawable(R.drawable.webview_progress);
        progressbar.setProgressDrawable(drawable);
        webview.addView(progressbar);

        //webview 保持header
        if(AppApplication.getInstance().getUserInfoVO()!=null){
            headers.put("TOKENID", AppApplication.getInstance().getUserInfoVO().getUid());
            headers.put("TOKEN", AppApplication.getInstance().getUserInfoVO().getToken());
            headers.put("TOKENTIME", AppApplication.getInstance().getUserInfoVO().getTime());
            headers.put("TOKENOS", AppApplication.getInstance().getUserInfoVO().getDevice());
        }else{
            headers.put("TOKENID", AppApplication.getInstance().getUid());
            headers.put("TOKEN", "");
            headers.put("TOKENTIME", "");
            headers.put("TOKENOS","10yan.android");
        }
        load(url,headers);
    }

    @Override
    public void bindViewsListener() {
        mBaseHeadLayout.setVisibility(View.GONE);
    }


    // 监听 所有点击的链接，如果拦截到我们需要的，就跳转到相对应的页面。
    // 防止调用系统自带的浏览器显示内容
    private class MyWebViewClient extends WebViewClient {

        private Intent intent;


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogUtil.e("shouldOverrideUrlLoading",url);


            view.loadUrl(url,headers);

            if (url != null && url.contains("referer=close")) {
                destroyWebView(webview);
                finish();
                return true;
            }else if(url != null && url.contains("referer=back")){
                webview.goBack();
                return true;
            }else if(url != null && url.contains("referer=_blank")){
                intent = new Intent(MoreWebViewActivity.this, MoreWebViewActivity.class);
                intent.putExtra("url",url);
                startActivity(intent);
                webview.stopLoading();
                return true;
            }else if(url != null && url.contains("referer=_share")){
                ShareUtil.share(MoreWebViewActivity.this, share_image,share_title, share_description, share_url);
            }else if(url != null && url.contains("referer=user_login")){
                startActivity(new Intent(MoreWebViewActivity.this, LoginActivity.class));
                return true;

            }else if(url != null && url.contains("referer=user_register")){

                startActivity( new Intent(MoreWebViewActivity.this, RegisteredActivtiy.class));
                return true;
            }else if (url.startsWith("tel:")) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }


            return super.shouldOverrideUrlLoading(view, url);
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.loadUrl("javascript:window.local_obj.showSource('<html>'+" +
                    "document.getElementsByTagName('html')[0].innerHTML+'</html>');");

        }
    }
    final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html) {
            LogUtil.d("HTML", html);
            refreshHtmlContent(html);
        }
    }
    private String share_title;
    private String share_image;
    private String share_description;
    private String share_url;
    private void refreshHtmlContent(final String html){
        LogUtil.d("网页内容",html);

                //解析html字符串为对象
                Document document = Jsoup.parse(html);

                //通过ID获取
                share_title = document.getElementById("share_title").text();
                share_image = document.getElementById("share_image").text();
                share_description = document.getElementById("share_description").text();
                share_url = document.getElementById("share_url").text();

    }
    private void load(String Url,Map<String, String> additionalHttpHeaders) {
        webview.getSettings().setJavaScriptEnabled(true);
        // 防止调用系统自带的浏览器显示内容
        webview.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
         webview.setWebViewClient(new MyWebViewClient());
        webview.setWebChromeClient(new WebChromeClient() {
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
                                                           GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

        });

        webview.loadUrl(Url,additionalHttpHeaders);

    }

    public void destroyWebView(WebView webView) {
        if (webView != null) {
            webView.clearHistory();
            webView.clearCache(true);
            webView.loadUrl("about:blank");
            webView.freeMemory();
            webView.pauseTimers();
        }
    }
    @Override
    public void onBackPressed() {

        if (getIntent() != null) {
            super.onBackPressed();
        } else {
            if (webview.canGoBack()) {
                webview.goBack(); // goBack()表示返回WebView的上一页面
            } else {
                super.onBackPressed();
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }else if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}
