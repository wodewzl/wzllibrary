
package com.wuzhanglong.library.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("JavascriptInterface")
public class WebViewPictureUtil extends WebViewClient {
    private Context mContext;
    private JSCallBack mJSCallback;
    private String mTable;
    private String mValue;
    private WebView mWebView;
    private JsObject mJsObject;

    public interface JSCallBack {
        public String jsCallBack(int type, String[] array,JsObject jsObject);
    }

    public WebViewPictureUtil(Context context, WebView webView, String table, String value) {
        this.mContext = context;
        this.mTable = table;
        this.mValue = value;
        webView.addJavascriptInterface(new JsObject(context), "viewlistner");
    }

    public WebViewPictureUtil(Context context, WebView webView, JSCallBack jsCallBack) {
        this.mContext = context;
        this.mWebView = webView;
        this.mJSCallback = jsCallBack;
        mJsObject=new JsObject(mContext);
        mWebView.addJavascriptInterface(mJsObject, "JsObject");
    }

    public JsObject getJsObject(Context context) {
        return new JsObject(context);
    }

    // 注入js函数监听
    private void addImageClickListner(WebView webView) {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        // "var obj = document.getElementById("testbtn");" +
        webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"" +
                mTable +
                "\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.viewlistner.onViewClick(" +
                mValue +
                ");  " +
                "    }  " +
                "}" +
                "})()");
    }

    // js通信接口
    public class JsObject {

        private Context context;

        public JsObject(Context context) {
            this.context = context;
        }


        @JavascriptInterface
        public String onViewClick(int type, String[] array) {
            // 1规定的类型 //2参数
            return  mJSCallback.jsCallBack(type, array,mJsObject);

        }

        public void callHtmlJS(final String str) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript:getKey(" + str + ")");
                }
            });
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {

        view.getSettings().setJavaScriptEnabled(true);

        super.onPageFinished(view, url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        view.getSettings().setJavaScriptEnabled(true);

        super.onPageStarted(view, url, favicon);
        // html加载完成之后，添加监听图片的点击js函数，添加JS,并添加监听回调
        if (mJSCallback == null) {
            addImageClickListner(view);
        }
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

        super.onReceivedError(view, errorCode, description, failingUrl);

    }

    public JSCallBack getmJSCallback() {
        return mJSCallback;
    }

    public void setmJSCallback(JSCallBack mJSCallback) {
        this.mJSCallback = mJSCallback;
    }


}
