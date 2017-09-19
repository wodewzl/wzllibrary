
package com.bs.bsims.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CircleOptions;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.bs.bsims.R;
import com.bs.bsims.xutils.impl.HttpUtilsByPC;
import com.bs.bsims.xutils.impl.RequestCallBackPC;

import org.json.JSONObject;
import org.xutils.ex.HttpException;

import java.util.HashMap;

public class CrmGaoDeMapWithShowActivity extends BaseActivity implements
        OnClickListener,
        OnMarkerClickListener, InfoWindowAdapter, AMapLocationListener, LocationSource {

    private MapView mapView;
    private AMap aMap;
    private UiSettings mUiSettings;
    private Context mContext;

    private String mLat = "";// 纬度
    private String mLon = "";// 经度
    private String mAddress = "";// 地址
    private String mCname = "";// 客户名称

    private String myLat = "";// 自己当前位置的纬度
    private String myLon = "";// 自己当前位置的经度

    // 声明AMapLocationClient类对象
    public static AMapLocationClient mLocationClient = null;
    // 声明mLocationOption对象
    public static AMapLocationClientOption mLocationOption = null;
    private OnLocationChangedListener mListener;

    @Override
    public boolean onMarkerClick(Marker arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mapView.onCreate(savedInstanceState);
        mContext = this;
        mLat = getIntent().getStringExtra("mLat");
        mLon = getIntent().getStringExtra("mLon");
        mAddress = getIntent().getStringExtra("mAddress");
        mCname = getIntent().getStringExtra("cName");
        mTitleTv.setText("位置详情");
        mOkTv.setText("导航");
        mOkTv.setOnClickListener(this);
        init();
    }

    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            mUiSettings = aMap.getUiSettings();
            aMap.setOnMarkerClickListener(this);// 设置大头针点击无效
            aMap.setLocationSource(this);// 设置定位监听
            aMap.clear();// 清除地图所有东西
        }

        locationAtNow();
        activate(mListener);// 开始定位
    }

    /**
     * 定位地图 并且隐藏不必要的按钮
     */
    private void locationAtNow() {
        // TODO Auto-generated method stub
        boolean flag = false;
        aMap.setMyLocationEnabled(!flag);// 是否可触发定位并显示定位层
        aMap.getUiSettings().setMyLocationButtonEnabled(!flag);// 设置默认定位按钮是否显示
        mUiSettings.setMyLocationButtonEnabled(!flag);// 设置默认定位按钮是否显示
        mUiSettings.setScrollGesturesEnabled(flag);

        /**
         * 设置地图是否可以手势缩放大小
         */
        mUiSettings.setZoomGesturesEnabled(true);
        mUiSettings.setZoomControlsEnabled(true);
        mUiSettings.setScrollGesturesEnabled(true);// 设置地图拖动yes
        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(
                new LatLng(Double.parseDouble(mLat), Double.parseDouble(mLon)),
                16.0f);
        aMap.moveCamera(cu);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(Double.parseDouble(mLat), Double.parseDouble(mLon)));
        markerOptions.title("");
        markerOptions.snippet("");
        markerOptions.visible(true);
        markerOptions.draggable(false);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.crm_highsas_clients_far)));

        // 绘制一个圆形
        aMap.addCircle(new CircleOptions()
                .center(new LatLng(Double.parseDouble(mLat), Double.parseDouble(mLon)))
                .radius(100).strokeColor(Color.parseColor("#00A9FE"))
                .strokeWidth(5));
        aMap.setInfoWindowAdapter(this);
        aMap.addMarker(markerOptions).showInfoWindow();
        /**
         * 设置显示地图的默认比例尺
         */
        // mUiSettings.setScaleControlsEnabled(!flag);

        // 方法设置地图的缩放级别

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.txt_comm_head_right:

                try {
                    Intent intent = new Intent("android.intent.action.VIEW",
                            android.net.Uri.parse("androidamap://navi?sourceApplication=appname&poiname=fangheng&lat="
                                    + mLat + "&lon=" + mLon + "&dev=1&style=2"));
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setPackage("com.autonavi.minimap");
                    startActivity(intent);
                    // 移动APP调起Android百度地图方式举例 b5P3jtqj3Cok1ZiQTKjrxyo7 百度的秘钥
                } catch (Exception e) {
                    // TODO: handle exception
                    getBaiDuPoints(myLat, myLon, mLat, mLon);
                }

                break;
            default:
                break;
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        // TODO Auto-generated method stub
        View infoWindow = getLayoutInflater().inflate(R.layout.gaode_location_showindown_infos,
                null);
        // LinearLayout location_ly = (LinearLayout) infoWindow.findViewById(R.id.location_ly);
        // LayoutParams layoutParams = location_ly.getLayoutParams();
        // // Drawable drawable =
        // getResources().getDrawable(R.drawable.gaode_loaction_side_map_info);
        // // layoutParams.width = drawable.getIntrinsicWidth();
        // layoutParams.width = CommonUtils.getScreenWid((Activity) mContext) -
        // CommonUtils.getScreenWid((Activity) mContext) / 4;
        TextView gaode_location_onlineno = (TextView) infoWindow
                .findViewById(R.id.location_adressinfo);//
        TextView location_adressinfo = (TextView) infoWindow.findViewById(R.id.location_timeinfo);//
        location_adressinfo.setText("地址: " + mAddress);
        location_adressinfo.setTextColor(Color.BLACK);
        if (TextUtils.isEmpty(mCname)) {
            gaode_location_onlineno.setVisibility(View.GONE);
        }
        else{
            gaode_location_onlineno.setText(Html.fromHtml("<font color='#aaaaaa'>客户: </font>") + mCname);
            gaode_location_onlineno.setTextColor(Color.parseColor("#4CB0EC"));
        }
      
        return infoWindow;
    }

    @Override
    public void baseSetContentView() {
        // TODO Auto-generated method stub
        View layout = View.inflate(this, R.layout.crm_visitor_map, null);
        mContentLayout.addView(layout);
        mapView = (MapView) findViewById(R.id.crm_map);
        findViewById(R.id.map_listid).setVisibility(View.GONE);
        // mapView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
        // LayoutParams.MATCH_PARENT));
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mapView
                .getLayoutParams();
        layoutParams.width = layoutParams.MATCH_PARENT;
        layoutParams.height = layoutParams.MATCH_PARENT;
        mapView.setLayoutParams(layoutParams);
        mapView.setVisibility(View.VISIBLE);
        findViewById(R.id.edit_single_search).setVisibility(View.GONE);
    }

    @Override
    public boolean getDataResult() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void updateUi() {
        // TODO Auto-generated method stub

    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
    }

    @Override
    public void bindViewsListener() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        // addmarker();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        deactivate();
    }

    @Override
    public View getInfoContents(Marker arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (null == mLocationClient) {
            mLocationClient = new AMapLocationClient(mContext.getApplicationContext());
        }
        // 设置定位回调监听
        mLocationClient.setLocationListener(this);
        // 初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
        // 设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        // 设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);// 只定位一次
        if (mLocationOption.isOnceLocationLatest()) {
            mLocationOption.setOnceLocationLatest(true);
            // 设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。
            // 如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会。
        }
        // 设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        // 设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        // 给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        // 启动定位
        mLocationClient.startLocation();
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (null != mLocationClient) {
            mLocationClient.stopLocation();// 停止定位
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * com.amap.api.location.AMapLocationListener#onLocationChanged(com.amap.api.location.AMapLocation
     * )
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        // TODO Auto-generated method stub
        if (mListener != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                myLat = amapLocation.getLatitude() + "";
                myLon = amapLocation.getLongitude() + "";
            }
        }
    }

    public void getBaiDuPoints(String lat1, String lon1, String lat, String lon) {
        final String url = "http://api.map.baidu.com/geoconv/v1/?coords=" + lon1 + "," + lat1 + ";" + lon + "," + lat
                + "&from=3&to=5&output=json&ak=Z3KYux4TcVAhFSTjH05tmpvr&mcode=35:47:FC:49:36:93:B6:BE:73:04:64:03:04:5A:97:41:32:F8:C0:90;com.bs.bsims";
        HashMap<String, String> map = new HashMap<String, String>();
        new HttpUtilsByPC().sendPostBYPC(url, map,
                new RequestCallBackPC() {
                    @Override
                    public void onFailurePC(HttpException arg0, String arg1) {
                        // TODO Auto-generated method stub
                    }

                    @SuppressWarnings("deprecation")
                    @Override
                    public void onSuccessPC(ResponseInfo rstr) {
                        // TODO Auto-generated method stub
                        try {
                            JSONObject jsonObject = new JSONObject(rstr.result.toString());
                            jsonObject = (JSONObject) jsonObject.getJSONArray("result").get(0);
                            String baiDuMyLon = jsonObject.getString("x");
                            String baiDuMyLat = jsonObject.getString("y");
                            JSONObject jsonObject1 = new JSONObject(rstr.result.toString());
                            jsonObject1 = (JSONObject) jsonObject1.getJSONArray("result").get(1);
                            String baiDuMLon = jsonObject1.getString("x");
                            String baiDuMLat = jsonObject1.getString("y");

                            Intent intent = Intent.getIntent("intent://map/direction?origin=latlng:" +
                                    baiDuMyLat + "," + baiDuMyLon + "|name:起点位置&destination=latlng:" + baiDuMLat + "," + baiDuMLon +
                                    "|name:终点位置&mode=driving&src=com.bs.bsims#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                            // 32.050490371697 112.15141273616
                            // Intent intent =
                            // Intent.getIntent("intent://map/direction?origin=latlng:34.264642646862,108.95108518068|name:我家&destination=大雁塔&mode=driving&region=西安&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                            startActivity(intent); // 启动调用

                        } catch (Exception e) {
                            Uri uri = Uri.parse("http://m.amap.com/?from=" + myLat + "," + myLon +
                                    "(from)&to=" + mLat + "," + mLon + "(to)&type=0&opt=1");
                            Intent it = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(it);
                            e.printStackTrace();
                        }

                    }

                });
    }

}
