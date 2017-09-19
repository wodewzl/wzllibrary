
package com.beisheng.synews.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.activity.ImagePreviewActivity;
import com.beisheng.base.fragment.BaseFragment;
import com.beisheng.base.http.HttpClientUtil;
import com.beisheng.base.interfaces.UpdateCallback;
import com.beisheng.base.utils.ThreadUtil;
import com.beisheng.base.utils.WebViewPictureUtil;
import com.beisheng.base.utils.WebViewPictureUtil.JSCallBack;
import com.beisheng.base.utils.WebviewUtil;
import com.beisheng.base.view.BSAutoSwipeRefreshLayout;
import com.beisheng.synews.activity.WebViewActivity;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.mode.NewsVO;
import com.google.gson.Gson;
import com.im.zhsy.R;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeNewsPublishFragment extends BaseFragment implements UpdateCallback, OnLongClickListener, OnRefreshListener {
    private String TAG = "HomeNewsPublishFragment";
    private BaseActivity mActivity;
    protected BSAutoSwipeRefreshLayout mSwipeLayout;
    private String mCid;
    private NewsVO mNewsVO;
    private WebView mWebView;
    private ProgressBar progressbar;

    public static HomeNewsFragment newInstance() {
        HomeNewsFragment liveFragment = new HomeNewsFragment();
        return liveFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = (BaseActivity) activity;
        mCid = this.getArguments().getInt("id") + "";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_news_publish_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        bindViewsListener();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // new ThreadUtil(mActivity, this).start();
    }

    private void initViews(View view) {
        mSwipeLayout = (BSAutoSwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mActivity.setSwipeRefreshLayoutColors(mSwipeLayout);
        mSwipeLayout.autoRefresh();
        mWebView = (WebView) view.findViewById(R.id.webview);
        progressbar = new ProgressBar(mActivity, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 5));
        Drawable drawable = this.getResources().getDrawable(R.drawable.webview_progress);
        progressbar.setProgressDrawable(drawable);
        mWebView.addView(progressbar);
    }

    public void bindViewsListener() {
        mSwipeLayout.setOnRefreshListener(this);
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

        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public boolean getData() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("cid", mCid);
            if (mActivity.hasNetWork()) {
                String jsonStr = HttpClientUtil.getRequest(mActivity, Constant.DOMAIN_NAME + Constant.HOME_NEWS_URL, map);
                mNewsVO = gson.fromJson(jsonStr, NewsVO.class);
                mActivity.saveJsonCache(Constant.HOME_NEWS_URL, map, jsonStr);
            } else {
                String oldStr = mActivity.getCacheFromDatabase(Constant.HOME_NEWS_URL, map);
                mNewsVO = gson.fromJson(oldStr, NewsVO.class);
            }
            if (Constant.RESULT_SUCCESS_CODE.equals(mNewsVO.getCode())) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean execute() {
        return getData();
    }

    @Override
    public void executeSuccess() {
        mSwipeLayout.setVisibility(View.VISIBLE);
        mSwipeLayout.setRefreshing(false);
        WebviewUtil.SetWebview(mWebView);
        WebViewPictureUtil wp = new WebViewPictureUtil(mActivity, mWebView, jsCallBack);
        mWebView.setWebViewClient(wp);
        mWebView.loadUrl(mNewsVO.getUrl());
    }

    JSCallBack jsCallBack = new JSCallBack() {
        @Override
        public void jsCallBack(String url) {
        }

        @Override
        public void jsCallBack(String str, String status) {
            // 1是超链接跳转2是图片
            if ("1".equals(status)) {
                if (!"".equals(str) && str != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("url", str);
                    bundle.putString("name", "微博");
                    // bundle.putString("result", "1");// 有返回结果
                    mActivity.openActivity(WebViewActivity.class, bundle, 0);
                }
            } else {
                Intent intent = new Intent();
                String[] imgUrls = str.split(",");
                ArrayList<String> list = new ArrayList<String>();
                for (int i = 0; i < imgUrls.length; i++) {
                    list.add(imgUrls[i]);
                }
                intent.putStringArrayListExtra("piclist", list);
                intent.setClass(mActivity, ImagePreviewActivity.class);
                intent.putExtra("imgIndex", 0);
                mActivity.startActivity(intent);
            }
        }
    };

    @Override
    public void executeFailure() {
        mSwipeLayout.setVisibility(View.VISIBLE);
        mSwipeLayout.setRefreshing(false);
    }

    public String getFragmentName() {
        return TAG;// 不知道该方法有没有用
    }

    @Override
    public boolean onLongClick(View arg0) {
        return true;
    }

    @Override
    public void onRefresh() {
        new ThreadUtil(mActivity, this).start();
    }
}
