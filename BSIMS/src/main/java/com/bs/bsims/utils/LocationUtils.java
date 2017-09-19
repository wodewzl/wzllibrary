
package com.bs.bsims.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * 地理位置工具 1
 * 
 * @author Administrator
 */
public class LocationUtils {

    private static final String TAG = "LoactionUtils";

    public static final String LOCATION_RECEIVE_ACTION = "gdLocation";

    public static boolean postFlage = true;

    public static void getLatLonForService(final Context context,
            AMapLocationClient mLocationClient, AMapLocationClientOption mLocationOption) {

        // 设置定位回调监听
        // 设置定位回调监听
        mLocationClient.setLocationListener(new AMapLocationListener() {

            @Override
            public void onLocationChanged(final AMapLocation amapLocation) {
                // TODO Auto-generated method stub
                if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                    SharedPreferences sp_service = context.getSharedPreferences("location_service",
                            Activity.MODE_PRIVATE);

                    // 当定位一回调的时候获取当前的系统的时间 // 如果在1分中内时间和位置都相同的则不提交
                    if (postFlage) {
                        if (System.currentTimeMillis()
                                - (sp_service.getLong("service_gbLocationTime", 0)) < 3 * 60 * 1000
                                && sp_service.getString("service_address", "").equals(
                                        amapLocation.getAddress())) {
                            return;
                        }
                        else {
                            // .getDefaultSharedPreferences(contextlocation); 1451203045368
                            // 1451203308187
                            postFlage = false;
                            final String url = BSApplication.getInstance().getHttpTitle()
                                    + Constant.GAODE_LOACION_INDEX_POST_LOCATION;
                            RequestParams map = new RequestParams();
                            map.put("ftoken", BSApplication.getInstance().getmCompany());
                            map.put("userid", BSApplication.getInstance().getUserId());
                            map.put("lon", amapLocation.getLongitude() + "");
                            map.put("lat", amapLocation.getLatitude() + "");
                            map.put("address", amapLocation.getAddress());
                            map.put("mobilename", android.os.Build.MODEL);// 手机类型
                            map.put("wantype", CommonUtils.GetNetworkType(context));// 网络状态
                            if (CommonUtils.ReadSharedpreferencesLocations(context).equals("0")) {// 设置为0表示第一次登陆
                                map.put("locationtype", "1");// 定位类型 1 登陆 2 考勤机 3 移动打卡 4 CRM 5后台定位
                                CommonUtils.PutSharedpreferencesLocations("1", context);
                            }
                            else {
                                map.put("locationtype", "5");// 定位类型 1 登陆 2 考勤机 3 移动打卡 4 CRM 5后台定位
                            }

                            AsyncHttpClient client = new AsyncHttpClient();
                            client.post(url, map, new AsyncHttpResponseHandler() {

                                @Override
                                public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                        Throwable arg3) {
                                    // TODO Auto-generated method stub
                                    postFlage = true;

                                    Log.e("TAGs", "发送定位失败");
                                }

                                @Override
                                public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

                                    JSONObject jsonObject;
                                    try {
                                        jsonObject = new JSONObject(new String(arg2));
                                        String str = (String) jsonObject.get("retinfo");
                                        String code = (String) jsonObject.get("code");
                                        // TODO Auto-generated method stub
                                        if (code.equals(Constant.RESULT_CODE)) {
                                            SharedPreferences sp_service = context
                                                    .getSharedPreferences(
                                                            "location_service",
                                                            Activity.MODE_PRIVATE);
                                            Editor edit = sp_service.edit();
                                            edit.putString("service_lat",
                                                    amapLocation.getLatitude()
                                                            + "");
                                            edit.putString("service_lon",
                                                    amapLocation.getLongitude()
                                                            + "");
                                            edit.putString("service_city", amapLocation.getCity()
                                                    + "");
                                            edit.putString("service_address",
                                                    amapLocation.getAddress());

                                            edit.putLong("service_gbLocationTime",
                                                    System.currentTimeMillis());
                                            edit.commit();
                                            Log.e("TAGs", "发送定位成功" + str);
                                        }
                                    }
                                    catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    postFlage = true;
                                }

                            });

                        }
                    }

                    else {
                        return;
                    }

                }
                else {
                    // 显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    CustomLog.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }

            }
        });
        // 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
        // 设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        // 设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        // 设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        // 设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        if(mLocationOption.isOnceLocationLatest()){
            mLocationOption.setOnceLocationLatest(true);
         //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。
         //如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会。
         }
        // // 设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(60 * 1000);// 3分钟定位一次
        // 给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        // 设置不杀死service
        mLocationOption.setKillProcess(false);
        // 启动定位
        mLocationClient.startLocation();
    }

    public static void getLatLonForOneLoaction(final Context context,
            AMapLocationClient mLocationClient, AMapLocationClientOption mLocationOption) {

        // 设置定位回调监听
        // 设置定位回调监听




        mLocationClient.setLocationListener(new AMapLocationListener() {

            @Override
            public void onLocationChanged(final AMapLocation amapLocation) {
                // TODO Auto-generated method stub
                if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                    SharedPreferences sp_service = context.getSharedPreferences("location_service",
                            Activity.MODE_PRIVATE);

                    // 当定位一回调的时候获取当前的系统的时间 // 如果在1分中内时间和位置都相同的则不提交
                    if (postFlage) {
                        // .getDefaultSharedPreferences(contextlocation); 1451203045368
                        // 1451203308187
                        postFlage = false;
                        final String url = BSApplication.getInstance().getHttpTitle()
                                + Constant.GAODE_LOACION_INDEX_POST_LOCATION;
                        RequestParams map = new RequestParams();
                        map.put("ftoken", BSApplication.getInstance().getmCompany());
                        map.put("userid", BSApplication.getInstance().getUserId());
                        map.put("lon", amapLocation.getLongitude() + "");
                        map.put("lat", amapLocation.getLatitude() + "");
                        map.put("address", amapLocation.getAddress());
                        map.put("mobilename", android.os.Build.MODEL);// 手机类型
                        map.put("wantype", CommonUtils.GetNetworkType(context));// 网络状态
                        map.put("locationtype", "1");// 定位类型 1 登陆 2 考勤机 3 移动打卡 4 CRM 5后台定位
                        AsyncHttpClient client = new AsyncHttpClient();
                        client.post(url, map, new AsyncHttpResponseHandler() {

                            @Override
                            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                    Throwable arg3) {
                                // TODO Auto-generated method stub
                                postFlage = true;

                                Log.e("TAGs", "发送定位失败");
                            }

                            @Override
                            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

                                JSONObject jsonObject;
                                try {
                                    jsonObject = new JSONObject(new String(arg2));
                                    String str = (String) jsonObject.get("retinfo");
                                    String code = (String) jsonObject.get("code");
                                    // TODO Auto-generated method stub
                                    if (code.equals(Constant.RESULT_CODE)) {
                                        SharedPreferences sp_service = context
                                                .getSharedPreferences(
                                                        "location_service",
                                                        Activity.MODE_PRIVATE);
                                        Editor edit = sp_service.edit();
                                        edit.putString("service_lat",
                                                amapLocation.getLatitude()
                                                        + "");
                                        edit.putString("service_lon",
                                                amapLocation.getLongitude()
                                                        + "");
                                        edit.putString("service_city", amapLocation.getCity()
                                                + "");
                                        edit.putString("service_address",
                                                amapLocation.getAddress());

                                        edit.putLong("service_gbLocationTime",
                                                System.currentTimeMillis());
                                        edit.commit();
                                        Log.e("TAGs", "发送定位成功" + str);
                                    }
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }
                                postFlage = true;
                            }

                        });

                    }
                }
                else {
                    // 显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    CustomLog.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }

        });
        // 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
        // 设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        // 设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        // 设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        // 设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        // // 设置定位间隔,单位毫秒,默认为2000ms
        // mLocationOption.setInterval(60 * 1000);// 3分钟定位一次
        // 给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        if(mLocationOption.isOnceLocationLatest()){
            mLocationOption.setOnceLocationLatest(true);
         //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。
         //如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会。
         }
        // 设置不杀死service
        mLocationOption.setKillProcess(false);
        // 启动定位
        mLocationClient.startLocation();
    }

    public static void closeLoaction(AMapLocationClient mLocationClient) {
        if (null != mLocationClient) {
            mLocationClient.stopLocation();// 停止定位
        }

    }

    public static void destoryLocation(AMapLocationClient mLocationClient) {
        if (null != mLocationClient) {
            mLocationClient.onDestroy();// 销毁定位客户端。
        }
    }

    // 存入时间
    public static boolean setLoactionModel(Context context, String service_lat, String service_lon, String service_city, String service_address) {
        SharedPreferences sp_service = context
                .getSharedPreferences(
                        "location_service",
                        Activity.MODE_PRIVATE);
        Editor edit = sp_service.edit();
        edit.putString("service_lat",
                service_lat);
        edit.putString("service_lon",
                service_lon);
        edit.putString("service_city", service_city);
        edit.putString("service_address",
                service_address);

        edit.putLong("service_gbLocationTime",
                System.currentTimeMillis());
        return edit.commit();
    }

    // 获取后台定位的最新一次时间
    public static String getServiceTime(Context context) {
        SharedPreferences sp_service = context.getSharedPreferences("location_service",
                Activity.MODE_PRIVATE);
        String serviceTime = "";
        try {
            serviceTime = sp_service.getLong("service_gbLocationTime", 0) + "";
        } catch (Exception e) {
            // TODO: handle exception
            serviceTime = "0";
        }
        return serviceTime;
    }

    // 获取最后一次定位的位置 如果最后一次没有就去拿第一次登陆的位置 如果还是没有就去空支付串
    public static String getServiceAddress(Context context) {

        SharedPreferences sp_service = context.getSharedPreferences("location_service",
                Activity.MODE_PRIVATE);
        String serviceAddress = "";
        try {
            serviceAddress = sp_service.getString("service_address", "");
        } catch (Exception e) {
            // TODO: handle exception

            if (serviceAddress.equals("")) {
                serviceAddress = "(定位失败) 请重新选择位置";
            }
        }
        return serviceAddress;
    }

    // 获取最后一次定位的City
    public static String getServiceCity(Context context) {

        SharedPreferences sp_service = context.getSharedPreferences("location_service",
                Activity.MODE_PRIVATE);
        String serviceAddress = "";
        try {
            serviceAddress = sp_service.getString("service_city", "");
        } catch (Exception e) {
            // TODO: handle exception
            if (serviceAddress.equals("")) {
                serviceAddress = "(定位失败)";
            }
        }
        return serviceAddress;
    }

    // 获取最后一次定位的位置 如果最后一次没有就去拿第一次登陆的位置 如果还是没有就去空支付串
    public static String getServiceLat(Context context) {
        String serviceLat = "";
        SharedPreferences sp_service = context.getSharedPreferences("location_service",
                Activity.MODE_PRIVATE);
        try {
            serviceLat = sp_service.getString("service_lat", "");
        } catch (Exception e) {
            // TODO: handle exception
            serviceLat = "";
        }
        return serviceLat;
    }

    // 获取最后一次定位的位置 如果最后一次没有就去拿第一次登陆的位置 如果还是没有就去空支付串
    public static String getServiceLon(Context context) {
        String serviceLon = "";
        SharedPreferences sp_service = context.getSharedPreferences("location_service",
                Activity.MODE_PRIVATE);
        try {
            serviceLon = sp_service.getString("service_lon", "");
        } catch (Exception e) {
            // TODO: handle exception\
            serviceLon = "";
        }
        return serviceLon;
    }

}
