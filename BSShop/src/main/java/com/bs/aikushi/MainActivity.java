package com.bs.aikushi;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.List;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import io.github.xudaojie.qrcodelib.CaptureActivity;
import pub.devrel.easypermissions.EasyPermissions;

import static com.bs.aikushi.R.id.webview;


public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    public static final int REQUEST_PERMISSIONS_CODE = 1;
    public static final int CAPTURE_CODE = 2;
    private static final int FILE_SELECT_CODE = 0;


    // 显示定位
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new AKSLocationListener();
    public static String TAG = "CordovaActivity";
    public JsObject mJsObject;
    private WebView mWebView;
    private ProgressBar progressbar;
    private ValueCallback<Uri> mUploadMessage;//回调图片选择，4.4以下
    private ValueCallback<Uri[]> mUploadCallbackAboveL;//回调图片选择，5.0以上


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = (WebView) findViewById(webview);

        String[] perms = {android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest
                .permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(MainActivity.this, perms)) {
            // 县市定位
//            mLocationClient = new LocationClient(MainActivity.this.getApplicationContext()); // 声明LocationClient类
//            mLocationClient.registerLocationListener(myListener); // 注册监听函数
//            initLocation();
//            mLocationClient.start();
            mWebView.reload();

        } else {
            EasyPermissions.requestPermissions(MainActivity.this, "定位需要需要权限", REQUEST_PERMISSIONS_CODE, perms);
        }
        SetWebview(mWebView);
        mWebView.setWebViewClient(new WebViewClient());
        progressbar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.FILL_PARENT, 5));
        Drawable drawable = this.getResources().getDrawable(R.drawable.webview_progress);
        progressbar.setProgressDrawable(drawable);
        mWebView.addView(progressbar);
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

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

            // For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILE_SELECT_CODE);

            }

            // For Android 3.0+
            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "File Browser"), FILE_SELECT_CODE);
            }

            // For Android 4.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILE_SELECT_CODE);
            }

            // For Android 5.0+
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                mUploadCallbackAboveL = filePathCallback;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(
                        Intent.createChooser(i, "File Browser"),
                        FILE_SELECT_CODE);
                return true;
            }

        });

        mJsObject = new JsObject();
        mWebView.addJavascriptInterface(mJsObject, "jsObject");

        EventBus.getDefault().register(this);


        if (this.getIntent().getStringExtra("url") != null) {
            String url = this.getIntent().getStringExtra("url");
            mWebView.loadUrl(url);
        } else {
            mWebView.loadUrl("http://aikushi.test.beisheng.wang/");
        }

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    return super.shouldOverrideUrlLoading(view, url);
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (intent == null) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_OK && requestCode == CAPTURE_CODE) {
            String result = intent.getStringExtra("result");
            mWebView.loadUrl(result);
//            Intent gotoIntent = new Intent();
//            gotoIntent.setAction("android.intent.action.VIEW");
//            Uri content_url = Uri.parse(result.toString());
//            gotoIntent.setData(content_url);
//            startActivity(gotoIntent);
        }
        if (resultCode == RESULT_OK && requestCode == FILE_SELECT_CODE) {
            if (Build.VERSION.SDK_INT >= 21) {//5.0以上版本处理
                Uri uri = intent.getData();
                Uri[] uris = new Uri[]{uri};
                mUploadCallbackAboveL.onReceiveValue(uris);//回调给js
            } else {//4.4以下处理
                Uri uri = intent.getData();
                mUploadMessage.onReceiveValue(uri);
            }
        }


    }


    class JsObject {
        @JavascriptInterface
        public void onViewClick(int type, String[] array) {
            // 1二维码 //2是图片 //3是分享
            System.out.println("======================================>");
            switch (type) {
                case 1:
                    String[] perms = {android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    if (EasyPermissions.hasPermissions(MainActivity.this, perms)) {
                        //二维码扫描
//                        Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
//                        //是不是显示历史记录按钮
//                        intent.putExtra(ZXingConstants.ScanIsShowHistory,true);
//                        MainActivity.this.startActivityForResult(intent, ZXingConstants.ScanRequestCode);
                        Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                        startActivityForResult(intent, CAPTURE_CODE);
                    } else {
                        EasyPermissions.requestPermissions(MainActivity.this, "定位需要需要权限",
                                CAPTURE_CODE, perms);
                    }
//                    Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
//                    startActivityForResult(intent, CAPTURE_CODE);

                    break;
                case 2:
                    guide(array[1], array[0], "终点位置");
                    break;
                case 3:
                    //分享
                    if (array.length == 2) {
                        ShareUtil.share(MainActivity.this, "", array[0], array[0], array[1]);
                    } else {
                        ShareUtil.share(MainActivity.this, "", array[0], array[1], array[2]);
                    }

                    break;
                default:
                    break;
            }
        }

        public void callHtmlJS(final String str1, final String str2) {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl("javascript:getLocation(" + str1 + "," + str2 + ")");
                }
            });
        }
    }

    public void guide(String latitude, String longitude, String desName) {
        Intent intent;
        String url;
        //com.autonavi.minimap  高德包 com.baidu.BaiduMap百度报名
        try {
            if (isInstallByread("com.baidu.BaiduMap")) {
                intent = new Intent();
                url = "baidumap://map/direction?destination=" + latitude + "," + longitude + "&mode=driving&coord_type=gcj02&output=";
                Uri uri = Uri.parse(url);
                intent.setData(uri);
//                StringBuffer stringBuffer = new StringBuffer("baidumap://map/navi?location=")
//                        .append(latitude).append(",").append(longitude).append("&type=TIME");
//                 intent = new Intent(Intent.ACTION_VIEW, Uri.parse(stringBuffer.toString()));
//                intent.setPackage("com.baidu.BaiduMap");
            } else {
                url = "http://uri.amap.com/navigation?to=" + longitude + "," + latitude + "," + desName + "&&mode=car";
                intent = Intent.getIntent(url);
            }
        } catch (Exception e) {
            Uri uri = Uri.parse("http://m.amap.com/?to=" + latitude + "," + longitude + "(" + desName + ")&type=0&opt=1");
            intent = new Intent(Intent.ACTION_VIEW, uri);
        }
        startActivity(intent); //启动调用
    }

    /**
     * 判断是否安装目标应用
     *
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
//        int span=1000;
        option.setScanSpan(0);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS_CODE:
                // 县市定位
//                mLocationClient = new LocationClient(this.getApplicationContext()); // 声明LocationClient类
//                mLocationClient.registerLocationListener(myListener); // 注册监听函数
//                initLocation();
//                mLocationClient.start();
                mWebView.reload();

                break;
            case CAPTURE_CODE:
                //二维码扫描
                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(intent, CAPTURE_CODE);
                break;
            default:
                break;
        }

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    public class AKSLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(final BDLocation location) {
            String str = location.getAddrStr();
            String str1 = location.getDistrict();
            mWebView.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    mJsObject.callHtmlJS(location.getLongitude() + "", location.getLatitude() + "");
                }
            }, 2000);
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
        }
    }

    @Subscribe
    public void onEventMainThread(EBMessageVO event) {
        if ("push".equals(event.getMessage())) {
            mWebView.loadUrl(event.getUrl());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //按下返回键并且webview界面可以返回
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack(); // goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void SetWebview(WebView webView) {
        WebSettings webSettings = webView.getSettings();
        //webview支持js脚本
        webSettings.setJavaScriptEnabled(true);
        //启用数据库
        webSettings.setDatabaseEnabled(true);
        //设置定位的数据库路径
        String dir = webView.getContext().getDir("database", Context.MODE_PRIVATE).getPath();
        webSettings.setGeolocationDatabasePath(dir);
        //启用地理定位
        webSettings.setGeolocationEnabled(true);
        webView.setVerticalScrollBarEnabled(false);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webSettings.setSupportMultipleWindows(true);
        // 启用地理定位
        webSettings.setGeolocationEnabled(true);
        // 最重要的方法，一定要设置，这就是出不来的主要原因 原因请看参考链接（Android WebView 无法打开天猫页面）
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true); // 支持js
        webSettings.setUseWideViewPort(false); // 将图片调整到适合webview的大小
        webSettings.setSupportZoom(true); // 支持缩放
        webSettings.supportMultipleWindows(); // 多窗口
        webSettings.setDatabaseEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 关闭webview中缓存
        webSettings.setAllowFileAccess(true); // 设置可以访问文件
        webSettings.setNeedInitialFocus(true); // 当webview调用requestFocus时为webview设置节点
        webSettings.setBuiltInZoomControls(false); // 设置支持缩放
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); // 支持通过JS打开新窗口
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadsImagesAutomatically(true); // 支持自动加载图片
        webView.setScrollContainer(false);
        webView.setScrollbarFadingEnabled(false);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.getSettings().setJavaScriptEnabled(true);
        // setWebViewFontSize(webView);
    }

    public void showTotast(String msg) {
        Crouton.makeText(this, msg, Style.ALERT, R.id.toast_conten).show();
    }

}
