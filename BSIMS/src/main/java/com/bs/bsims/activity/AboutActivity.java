
package com.bs.bsims.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.bs.bsims.R;
import com.bs.bsims.utils.CommonUtils;

public class AboutActivity extends com.bs.bsims.activity.BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.about, null);
        mContentLayout.addView(layout);

    }

    @Override
    public boolean getDataResult() {
        if (!CommonUtils.isNetworkAvailable(AboutActivity.this))
            return false;
        return true;
    }

    @Override
    public void updateUi() {

    }

    @Override
    public void initView() {
        mTitleTv.setText("关于我们");
        WebView webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl("http://cp.beisheng.wang/about_us.html");
    }

    @Override
    public void bindViewsListener() {

    }
}
