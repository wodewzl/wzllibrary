/**
 * 
 */

package com.bs.bsims.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.bs.bsims.R;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.utils.UrlUtil;
import com.bs.bsims.view.BsWebview;

import java.util.HashMap;

/**
 * BS北盛最帅程序员 Copyright (c) 2016 湖北北盛科技有限公司
 * 
 * @author 梁骚侠
 * @date 2016-9-2 在线演示
 * @version 2.0
 */
public class AppOnlineTrackActivity extends BaseActivity {
    private WebView webView;
    private ProgressBar progressbar;
    private String webViewurl;
    private HashMap<String, String> paramsMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.about, mContentLayout);
//        mContentLayout.addView(layout);

    }

    @Override
    public boolean getDataResult() {

        return true;
    }

    @Override
    public void updateUi() {

    }

    @Override
    public void initView() {
        mTitleTv.setText("在线演示");
        webView = (WebView) findViewById(R.id.webview);
        progressbar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 5));
        Drawable drawable = this.getResources().getDrawable(R.drawable.webview_progress);
        progressbar.setProgressDrawable(drawable);
        webView.addView(progressbar);
        inItData();
    }

    @Override
    public void bindViewsListener() {
        webView.setWebChromeClient(new WebChromeClient() {
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

        });
    }

    public void inItData() {
        String mdid = getIntent().getStringExtra("otherName");
        paramsMap = new HashMap<String, String>();
        paramsMap.put("malias", mdid);
        webViewurl = UrlUtil.getMaptoAllWebviewUrl(Constant.APPMARKETONLINEYAN, paramsMap);
        BsWebview.SetWebview(webView, webViewurl);
    }

}
