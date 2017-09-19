
package com.bs.bsims.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.bs.bsims.R;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.view.BsWebview;

public class RedMoneyActivity extends BaseActivity implements OnClickListener {

    private WebView mWebView;
    private String mTitle = "";
    private boolean mFlag = false;;

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.red_money, mContentLayout);
        // 状态栏改变成红色
        baseHeadLayout.setBackgroundColor(Color.parseColor("#D85445"));
        // mContentLayout.addView(layout);
        // mWebView = new WebView(getApplicationContext());
        // mContentLayout.addView(mWebView);
    }

    // @Override
    // protected void onDestroy() {
    // super.onDestroy();
    // // mContentLayout.removeAllViews();
    // // mWebView.stopLoading();
    // // mWebView.removeAllViews();
    // // mWebView.destroy();
    // // mWebView = null;
    //
    // }

    @Override
    public boolean getDataResult() {
        if (!CommonUtils.isNetworkAvailable(RedMoneyActivity.this))
            return false;
        return true;
    }

    @Override
    public void updateUi() {

    }

    @SuppressLint("NewApi")
    @Override
    public void initView() {
        mWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = mWebView.getSettings();

        mHeadLayout.setBackgroundColor(Color.parseColor("#D85445"));
        mOkTv.setText("发红包");
        if ("1".equals(BSApplication.getInstance().getUserFromServerVO().getIsboss()))
            mOkTv.setVisibility(View.VISIBLE);
        else
            mOkTv.setVisibility(View.GONE);
        mOkTv.setTextSize(16);
        mTitleTv.setText("企业红包");
        String url = "http://manager.beisheng.wang/wap.php/RedPackets/rplist/" + "uid/" + BSApplication.getInstance().getUserId() + "/ftoken/" + BSApplication.getInstance().getmCompany() + ".html";
        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                // mTitle = title;
                if (title.equals("企业红包1")) {
                    if (mFlag) {
                        mFlag = false;
                        mWebView.reload();
                    }
                }

                if (title.equals("企业红包3")) {
                    mOkTv.setText("红包列表");
                } else {
                    mOkTv.setText("发红包");
                }
            }
        };
        // 设置setWebChromeClient对象
        mWebView.setWebChromeClient(wvcc);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        BsWebview.SetWebview(mWebView, url);
    }

    @Override
    public void bindViewsListener() {
        mHeadBackImag.setOnClickListener(this);
        mOkTv.setOnClickListener(this);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
        // mWebView.goBack();
        // return false;
        // }

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void goBack() {

        if (mWebView.getTitle().equals("企业红包2")) {
            mWebView.goBack();
            mFlag = true;
        } else if (mWebView.getTitle().equals("企业红包3")) {
            moneList();
            // mWebView.goBack();
        } else if (mWebView.getTitle().equals("企业红包4")) {
            mWebView.goBack();
        } else if (mWebView.getTitle().equals("企业红包5")) {
            mWebView.goBack();
        } else {
            this.finish();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_head_back:
                goBack();
                break;

            case R.id.txt_comm_head_right:
                String url;
                if (mWebView.getTitle().equals("企业红包3")) {
                    moneList();
                } else {
                    sendMoney();
                }
                break;

            default:
                break;
        }
    }

    // 发红包
    public void sendMoney() {
        String url = "http://manager.beisheng.wang/wap.php/RedPackets/issue/" + "uid/" + BSApplication.getInstance().getUserId() + "/ftoken/" + BSApplication.getInstance().getmCompany() + ".html";
        BsWebview.SetWebview(mWebView, url);
    }

    // 红包列表
    public void moneList() {

        String url = "http://manager.beisheng.wang/wap.php/RedPackets/rplist/" + "uid/" + BSApplication.getInstance().getUserId() + "/ftoken/" + BSApplication.getInstance().getmCompany() + ".html";
        BsWebview.SetWebview(mWebView, url);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {
            mWebView.clearCache(true);
            mWebView.clearHistory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
