
package com.bs.bsims.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.bs.bsims.R;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CommonUtils.ResultCallback;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.utils.UrlUtil;
import com.bs.bsims.utils.WebViewPictureUtil;
import com.bs.bsims.utils.WebViewPictureUtil.JSCallBack;
import com.bs.bsims.view.BSDialog;
import com.bs.bsims.view.BsWebview;

import java.util.HashMap;

public class HumanResoureActivity extends BaseActivity {

    private BSDialog mBSDialog;
    private ProgressBar progressbar;
    private WebView webView;

    private Context context;
    private HashMap<String, String> paramsMap;
    private String timeSelect = "";
    private String webViewurl = "";
    private int whereKeyStore;

    private int loadKey;

    @Override
    public void baseSetContentView() {
        // TODO Auto-generated method stub
        context = this;
        View layout = View.inflate(this, R.layout.humanresoureproter, mContentLayout);

    }

    @Override
    public boolean getDataResult() {
        // TODO Auto-generated method stub
        if (!CommonUtils.isNetworkAvailable(this))
            return false;
        return true;
    }

    @Override
    public void updateUi() {
        // TODO Auto-generated method stub

    }

    @Override
    public void initView() {
        webView = (WebView) findViewById(R.id.reouseproterwebview);
        progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 5));
        Drawable drawable = context.getResources().getDrawable(R.drawable.webview_progress);
        progressbar.setProgressDrawable(drawable);
        webView.addView(progressbar);
        // Calendar calendar = Calendar.getInstance();
        // mOkTv.setText(calendar.get(Calendar.YEAR) + "-0" +
        // calendar.get(Calendar.MONTH)+1);
        mOkTv.setText(DateUtils.getCurrentDate11113());
        timeSelect = DateUtils.getCurrentDate11113();
        mOkTv.setPadding(5, 5, 0, 5);
        whereKeyStore = getIntent().getIntExtra("key", 0);
        getUrl(whereKeyStore);
    }

    @Override
    public void bindViewsListener() {
        // TODO Auto-generated method stub
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

        mOkTv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (mBSDialog == null) {
                    mBSDialog = CommonUtils.initDateViewCallback(HumanResoureActivity.this, "请选择时间", mOkTv, 3, callback);
                } else {
                    mBSDialog.show();
                }

            }
        });
    }

    public void getUrl(int id) {
        paramsMap = new HashMap<String, String>();
        paramsMap.put("d", timeSelect);
        switch (id) {
            case 0:
                // Constant
                loadKey = 0;
                mOkTv.setVisibility(View.GONE);
                webViewurl = UrlUtil.getMaptoAllWebviewUrl(Constant.HUMANRESOUREREPORT, paramsMap);
                mTitleTv.setText(getResources().getString(R.string.human_resource_report_monthlyoverview));
                break;
            case 1:
                // Constant
                loadKey = 1;
                webViewurl = UrlUtil.getMaptoAllWebviewUrl(Constant.HUMANRESOURERERECRUITMENT, paramsMap);
                mTitleTv.setText(getResources().getString(R.string.human_resource_report_monthlyrecruitment));
                break;
            case 2:
                // Constant
                loadKey = 2;
                webViewurl = UrlUtil.getMaptoAllWebviewUrl(Constant.HUMANRESOURERELEAVEWORK, paramsMap);
                mTitleTv.setText(getResources().getString(R.string.human_resource_report_monthlyleavework));
                break;
            case 3:// Constant
                loadKey = 3;
                webViewurl = UrlUtil.getMaptoAllWebviewUrl(Constant.HUMANRESOURERESALARYCHANGE, paramsMap);
                mTitleTv.setText(getResources().getString(R.string.human_resource_report_monthlysalarychange));
                break;

        }
        if (webViewurl.toString() != null && !webViewurl.equals("")) {
            BsWebview.SetWebview(webView, webViewurl);
            WebViewPictureUtil wp = new WebViewPictureUtil(this, webView, "li", "this.value");
            webView.setWebViewClient(wp);
            wp.setmJSCallback(jsCallBack);
        }
    }

    JSCallBack jsCallBack = new JSCallBack() {

        @Override
        public void jsCallBack(String status) {
            // TODO Auto-generated method stub
            Intent i = new Intent(context, DanganIndexoneActivity.class);
            switch (loadKey) {
                case 0://人资报告
                    setNewIntent(status, i);
                    break;
                    
                case 1:
                    setNewIntent(status, i);
                    i.putExtra("isentry","1");
                    i.putExtra("date",timeSelect);
                    break;
                case 2:
                    setNewIntent(status, i);
                    i.putExtra("isquit","1");
                    i.putExtra("date",timeSelect);
                    break;

                default:
                    return;
            }

            startActivity(i);

        }
    };
 
    ResultCallback callback = new ResultCallback() {

        @Override
        public void callback(String str, int position) {
            timeSelect = str;
            mOkTv.setText(str);
            getUrl(whereKeyStore);
        }
    };

    
    public static void setNewIntent(String status,Intent i){
        switch (Integer.parseInt(status)) {
            case 1:// 男
                i.putExtra("mMoreChildkey", "sex");
                i.putExtra("mMoreChildValue", "1");
                break;
            case 2:// 女
                i.putExtra("mMoreChildkey", "sex");
                i.putExtra("mMoreChildValue", "2");
                break;
            case 0:// 全部
                break;
        }
    }
}
