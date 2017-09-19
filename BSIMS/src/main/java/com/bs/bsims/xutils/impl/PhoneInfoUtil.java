
package com.bs.bsims.xutils.impl;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.bs.bsims.utils.CustomLog;

import java.util.HashMap;
import java.util.Map;

/**
 * @author peck
 * @Description: 手机信息
 * @date 2015-6-3 下午2:50:57
 * @email 971371860@qq.com
 * @version V1.0
 * @version V1.1 增加手机网络状态
 */

public class PhoneInfoUtil {

    /** 没有网络 */
    public static final int NETWORKTYPE_INVALID = 0;
    /** wap网络 */
    public static final int NETWORKTYPE_WAP = 1;
    /** 2G网络 */
    public static final int NETWORKTYPE_2G = 2;

    /** 3G和3G以上网络，或统称为快速网络 */
    public static final int NETWORKTYPE_3G = 3;

    /** wifi网络 */
    public static final int NETWORKTYPE_WIFI = 4;
    /** 用来保存当前网络状态，并且用来决定是否压缩图片 */
    public static boolean NETWORKTYPE_4IMG = false;

    /** 屏幕宽度 */
    public static int width;

    /**
     * model_number : HUAWEI A199 OS_Ver : 4.1.2
     * 
     * @return
     */
    public static Map<String, String> getPhoneInfo(Context context) {
        // ,Activity mActivity
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("model", Build.MODEL);
        paramsMap.put("system", Build.VERSION.RELEASE);
        paramsMap.put("network", getNetWorkType(context));
        paramsMap.put("platform", "1");

        StringBuilder sb = new StringBuilder();
        sb.append("\nmodel_number : " + Build.MODEL);
        sb.append("\nOS_Ver : " + Build.VERSION.RELEASE);
        sb.append("\nSDK_Ver : " + Build.VERSION.SDK);
        CustomLog.e("PhoneInfoUtil", sb.toString());
        return paramsMap;
    }

    /**
     * 获取网络状态，wifi,wap,2g,3g.
     * 
     * @param context 上下文
     * @return int 网络状态 {@link #NETWORKTYPE_2G},{@link #NETWORKTYPE_3G}, *
     *         {@link #NETWORKTYPE_INVALID},{@link #NETWORKTYPE_WAP}*
     *         <p>
     *         {@link #NETWORKTYPE_WIFI}
     */

    public static String getNetWorkType(Context context) {
        int mNetWorkType = 4;
        String type = "";

        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            type = networkInfo.getTypeName();

            if (type.equalsIgnoreCase("WIFI")) {
                mNetWorkType = NETWORKTYPE_WIFI;
                type = "4";
                NETWORKTYPE_4IMG = true;
            } else if (type.equalsIgnoreCase("MOBILE")) {
                int typeInt = getFastMobileNetwork(context);
                if (typeInt > 2) {
                    NETWORKTYPE_4IMG = true;
                }
                type = getFastMobileNetwork(context) + "";
            }
        } else {
            mNetWorkType = NETWORKTYPE_INVALID;
        }
        return type;
    }

    private static boolean isFastMobileNetwork(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return false; // ~ 14-64 kbps
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return true; // ~ 400-1000 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return true; // ~ 600-1400 kbps
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return false; // ~ 100 kbps
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return true; // ~ 2-14 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return true; // ~ 700-1700 kbps
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return true; // ~ 1-23 Mbps
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return true; // ~ 400-7000 kbps
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return true; // ~ 1-2 Mbps
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return true; // ~ 5 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return true; // ~ 10-20 Mbps
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return false; // ~25 kbps
            case TelephonyManager.NETWORK_TYPE_LTE:
                return true; // ~ 10+ Mbps
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return false;
            default:
                return false;
        }
    }

    private static int getFastMobileNetwork(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        int typeInt = 4;
        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                typeInt = 1; // ~ 50-100 kbps
                break;
            case TelephonyManager.NETWORK_TYPE_CDMA:
                typeInt = 1; // ~ 14-64 kbps
                break;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                typeInt = 1; // ~ 50-100 kbps
                break;
            case TelephonyManager.NETWORK_TYPE_GPRS:
                typeInt = 1; // ~ 100 kbps
                break;
            case TelephonyManager.NETWORK_TYPE_IDEN:
                typeInt = 1; // ~25 kbps

                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                typeInt = 2; // ~ 400-1000 kbps
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                typeInt = 2; // ~ 600-1400 kbps
                break;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                typeInt = 2; // ~ 2-14 Mbps
                break;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                typeInt = 2; // ~ 700-1700 kbps
                break;
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                typeInt = 2; // ~ 1-23 Mbps
                break;
            case TelephonyManager.NETWORK_TYPE_UMTS:
                typeInt = 2; // ~ 400-7000 kbps
                break;
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                typeInt = 2; // ~ 1-2 Mbps
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                typeInt = 2; // ~ 5 Mbps
                break;
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                typeInt = 2; // ~ 10-20 Mbps

                break;
            case TelephonyManager.NETWORK_TYPE_LTE:
                typeInt = 3; // ~ 10+ Mbps

            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                typeInt = 0;
                break;
            default:
                typeInt = 4;
        }
        return typeInt;
    }
    
    
    public static String getPhoneNetInfo(int key){
        String keyValue="";
        switch (key) {
            //定位类型 1 登陆 2 考勤机 3 移动打卡 4 CRM 5后台定位
            case 1:
                keyValue ="账号登陆";
                break;
            case 2:
                keyValue ="考勤机";
                break;
            case 3:
                keyValue ="移动打卡 ";
                break;
            case 4:
                keyValue ="CRM签到";
                break;
            case 5:
                keyValue ="后台定位";
                break;
        }
        return keyValue;
        
    }
 
    
}
