/**
 * 
 */

package com.bs.bsims.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.utils.UrlUtil;
import com.bs.bsims.view.BsWebview;

import java.util.HashMap;

/**
 * BS北盛最帅程序员 Copyright (c) 2016 湖北北盛科技有限公司
 * 
 * @author 梁骚侠
 * @date 2016-9-2
 * @version 2.0
 */
public class AppOnlineActivity extends BaseActivity {
    private WebView webView;
    private ProgressBar progressbar;
    private String webViewurl;
    private HashMap<String, String> paramsMap;
    private ImageView imageView;
    private TextView mTitleName,mOnlineTv,mTitleContent;
    private RatingBar roomRatingBar;
    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.app_online_index, null);
        mContentLayout.addView(layout);

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
        mTitleTv.setText("应用详情");
        webView = (WebView) findViewById(R.id.market_webview);
        imageView = (ImageView) findViewById(R.id.makert_inco);
        roomRatingBar = (RatingBar) findViewById(R.id.rat_bar);
        mTitleName = (TextView) findViewById(R.id.title_name);
        progressbar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 5));
        mOnlineTv = (TextView) findViewById(R.id.online_view);
        mTitleContent =(TextView) findViewById(R.id.title_content);
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
        mTitleName.setText(getIntent().getStringExtra("name_title"));
        imageView.setBackgroundResource(Integer.parseInt(getIntent().getStringExtra("img_title")));
        mTitleContent.setText(getIntent().getStringExtra("name_content"));
        roomRatingBar.setRating((float) getIntent().getDoubleExtra("num_rat", 1));
        String mdid = getIntent().getStringExtra("mdid");
        paramsMap = new HashMap<String, String>();
        paramsMap.put("mdid", mdid);
        webViewurl = UrlUtil.getMaptoAllWebviewUrl(Constant.APPMARKETONLINEDETAIL, paramsMap);
        BsWebview.SetWebview(webView, webViewurl);
        if(getIntent().getStringExtra("ismode").equals("0")){
            mOnlineTv.setVisibility(View.GONE);
        }
    }

    public void goOnline(View view) {
        String otherName = getIntent().getStringExtra("otherName");
        Intent i = new Intent(this, AppOnlineTrackActivity.class);
        i.putExtra("otherName", otherName);
        startActivity(i);
    }
}
