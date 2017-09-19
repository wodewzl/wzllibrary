
package com.beisheng.base.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.beisheng.base.mode.TreeVO;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通用工具类
 * 
 * @author dewyze
 */
@SuppressWarnings("deprecation")
public class BaseCommonUtils {

    private static final String TAG = "CommonUtils";

    private static final int TAKE_PICTURE = 0x000001;
    private static final int RESULT_LOAD_IMAGE = 0x000002;

    private final float s = 1.70158f;
    float mDuration = 0f;
    // 用于匹配手机号码
    private final static String REGEX_MOBILEPHONE = "^0?1[3458]\\d{9}$";

    // 用于匹配固定电话号码
    private final static String REGEX_FIXEDPHONE = "^(010|02\\d|0[3-9]\\d{2})?\\d{6,8}$";

    // 用于获取固定电话中的区号
    private final static String REGEX_ZIPCODE = "^(010|02\\d|0[3-9]\\d{2})\\d{6,8}$";

    /**
     * 隐藏软键盘
     */
    public static void hideSoftKeybord(Activity activity) {

        if (null == activity) {
            return;
        }
        try {
            final View v = activity.getWindow().peekDecorView();
            if (v != null && v.getWindowToken() != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        } catch (Exception e) {

        }
    }

    /**
     * 判断是否为合法的json
     * 
     * @param jsonContent 需判断的字串
     */
    public static boolean isJsonFormat(String jsonContent) {
        try {
            new JsonParser().parse(jsonContent);
            return true;
        } catch (JsonParseException e) {
            return false;
        }
    }

    /**
     * 判断字符串是否为空
     * 
     * @param text
     * @return true null false !null
     */
    public static boolean isNull(String text) {
        if (text == null || "".equals(text.trim()) || "null".equals(text))
            return true;
        return false;
    }

    /**
     * 判断网络是否可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static String softVersion(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return info.versionName;
    }

    /** 判断一个字符串是否是汉字 */
    public static boolean isCNMark(String str) {

        char[] cs = str.toCharArray();

        ArrayList<Boolean> list = new ArrayList<Boolean>();

        for (int i = 0; i < cs.length; i++) {
            if (isChinese(cs[i])) {
                list.add(true);
            } else {
                list.add(false);
            }
        }
        if (list.contains(false)) {
            return false;

        } else {
            return true;
        }
    }

    /** 判断一个字符是否是汉字 */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    // 打电话
    public static void call(Context context, String phone) {
        String str = "tel:" + phone;
        Uri uri = Uri.parse(str);
        Intent intent = new Intent(Intent.ACTION_CALL, uri);
        context.startActivity(intent);
    }

    // 发短信
    public static void sendMsg(Context context, String phone) {
        String str = "smsto:" + phone;
        Uri uri = Uri.parse(str);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", "");
        context.startActivity(intent);

    }

    // 发邮件
    public static void sendEmail(Context context, String email) {
        String str = "mailto:" + email;
        Intent data = new Intent(Intent.ACTION_SENDTO);
        data.setData(Uri.parse(str));
        context.startActivity(data);
    }

    // 替换html里的字符
    public static String replaceWebview(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\r\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("<br />");
        }
        return dest;
    }

    // 替换html里的字符
    // 替换html里的字符1
    public static String replaceWebview1(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\r\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    // 替换html里的空格
    public static String replaceWebview2(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("&nbsp;");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /**
     * 检测Sdcard是否存在
     * 
     * @return
     */
    public static boolean isExitsSdcard() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    /**
     * 根据下载路径得到文件名，含后缀
     * 
     * @param fileDownloadPath 下载路径
     * @return
     */
    public static String getFileNameCloudSuffix(String fileDownloadPath) {
        int lastIndex1 = fileDownloadPath.lastIndexOf("/");
        String fileName = fileDownloadPath.substring(lastIndex1 + 1, fileDownloadPath.length());
        return fileName;
    }

    public static Bitmap getBitmapFromFile(File dst, int width, int height) {
        if (null != dst && dst.exists()) {
            BitmapFactory.Options opts = null;
            if (width > 0 && height > 0) {
                opts = new BitmapFactory.Options(); // 设置inJustDecodeBounds为true后，decodeFile并不分配空间，此时计算原始图片的长度和宽度
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(dst.getPath(), opts);
                // 计算图片缩放比例
                final int minSideLength = Math.min(width, height);
                opts.inSampleSize = computeSampleSize(opts, minSideLength, width * height); // 这里一定要将其设置回false，因为之前我们将其设置成了true
                opts.inJustDecodeBounds = false;
                opts.inInputShareable = true;
                opts.inPurgeable = true;
            }
            try {
                return BitmapFactory.decodeFile(dst.getPath(), opts);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    public static void sendBroadcast(Context context, String action) {
        Intent intent = new Intent(action);
        context.sendBroadcast(intent);
    }

    public static void sendBroadcast(Context context, String action, HashMap<String, String> map) {
        Object[] obj = map.keySet().toArray();

        Intent intent = new Intent(action);
        if (map.size() > 0) {
            for (int i = 0; i < map.size(); i++) {
                intent.putExtra(obj[i].toString(), map.get(obj[i].toString()));
            }
        }
        context.sendBroadcast(intent);
    }

    public static Bitmap getBitmap(String filePaht) {

        BitmapFactory.Options bfOptions = new BitmapFactory.Options();
        bfOptions.inDither = false;
        bfOptions.inPurgeable = true;
        bfOptions.inTempStorage = new byte[12 * 1024];
        bfOptions.inJustDecodeBounds = true;
        File file = new File(filePaht);
        FileInputStream fs = null;
        Bitmap bmp = null;
        try {
            fs = new FileInputStream(file);
            if (fs != null)
                bmp = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, bfOptions);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }

    // 金额3位数隔开
    public static String formatDetailMoney(String number) {
        number = isNormalData(number);
        double count = Double.parseDouble(number);
        DecimalFormat df = new DecimalFormat();
        df.applyPattern("##,###.00");
        if (count == 0) {
            return "￥0";
        } else {
            return "￥" + df.format(count);
        }

    }

    // 设置背景透明度
    public static void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        context.getWindow().setAttributes(lp);
    }

    // 保留2位小数
    public static String roundNumber(Double d) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
        return df.format(d);
    }

    public static String isNormalData(String str) {
        if ("".equals(str) || "暂无".equals(str) || str == null) {
            return "0";
        } else {
            return str;
        }
    }

    public static String isNormalCount(String str) {
        if ("".equals(str) || "暂无".equals(str) || str == null) {
            return "0";
        } else if (Integer.parseInt(str) > 99) {
            return "99";
        } else {
            return str;
        }
    }

    public static boolean isNormalString(String str) {
        if ("".equals(str) || "暂无".equals(str) || str == null) {
            return false;
        } else {
            return true;
        }
    }

    // 计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(Activity ac, String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        // options.inSampleSize = calculateInSampleSize(options, 480, 800);
        options.inSampleSize = calculateInSampleSize(options, 1080, 1920);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static int getScreenWid(Activity activity) {
        // 方法1 Android获得屏幕的宽和高
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        return display.getWidth();
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }

    public static int getScreenHigh(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }

    // 获取控件的宽度
    public static int getViewWidth(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        int height = view.getMeasuredHeight();
        int width = view.getMeasuredWidth();
        return width;
    }

    // 获取控件的高度
    public static int getViewHigh(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        int height = view.getMeasuredHeight();
        int width = view.getMeasuredWidth();
        return height;
    }

    // id与name 通过toString 带过来
    public static <T> ArrayList<TreeVO> getOneLeveTreeVo(List<T> arrayList) {
        ArrayList<TreeVO> list = new ArrayList<TreeVO>();
        for (int i = 0; i < arrayList.size(); i++) {
            TreeVO vo = new TreeVO();
            vo.setName(arrayList.get(i).toString().split(",")[1]);
            vo.setSearchId(arrayList.get(i).toString().split(",")[0]);
            vo.setLevel(1);
            list.add(vo);
        }
        return list;
    }

    // 手机号
    public static boolean isCellPhone(String number) {
        Pattern patter = Pattern.compile(REGEX_MOBILEPHONE);
        Matcher match = patter.matcher(number);
        return match.matches();
    }

    // 固定电话
    public static boolean isFixedPhone(String number) {
        Pattern pattern = Pattern.compile(REGEX_FIXEDPHONE);
        Matcher match = pattern.matcher(number);
        return match.matches();
    }

    public static List<Map<String, Object>> getListItem(String[] array) {
        String[] tmpArray = array;
        List<Map<String, Object>> listems = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < array.length; i++) {
            Map<String, Object> listem = new HashMap<String, Object>();
            String id = tmpArray[i].split(",")[0];
            String name = tmpArray[i].split(",")[1];
            listem.put("option", name);
            listems.add(listem);
        }
        return listems;
    }

    public static Options getBitmapOption(int inSampleSize) {
        System.gc();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return options;
    }

    public static String GetNetworkType(Context context) {
        String strNetworkType = "";
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "WIFI";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();

                Log.e("cocos2d-x", "Network getSubtypeName : " + _strSubTypeName);

                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: // api<8 : replace by
                                                             // 11
                        strNetworkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: // api<9 : replace by
                                                               // 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD: // api<11 : replace by
                                                              // 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP: // api<13 : replace by
                                                              // 15
                        strNetworkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE: // api<11 : replace by
                                                            // 13
                        strNetworkType = "4G";
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = "3G";
                        } else {
                            strNetworkType = _strSubTypeName;
                        }

                        break;
                }

                Log.e("cocos2d-x", "Network getSubtype : " + Integer.valueOf(networkType).toString());
            }
        }

        Log.e("cocos2d-x", "Network Type : " + strNetworkType);

        return strNetworkType;
    }

    // corners 为边框圆角（单位为px ） frame 为边框颜色，content为内部填充颜色
    public static GradientDrawable setBackgroundShap(Context context, int corners, String frameColor, String contentColor) {
        int strokeWidth = 2; // 3dp 边框宽度
        int roundRadius = 10; // 8dp 圆角半径
        int strokeColor = Color.parseColor(frameColor);// 边框颜色
        int fillColor = Color.parseColor(contentColor);// 内部填充颜色
        GradientDrawable gd = new GradientDrawable();// 创建drawable
        gd.setColor(fillColor);
        gd.setCornerRadius(dip2px(context, corners));
        gd.setStroke(strokeWidth, strokeColor);
        return gd;
    }

    // corners 为边框圆角（单位为px ） frame 为边框颜色，content为内部填充颜色
    public static GradientDrawable setBackgroundShap(Context context, int corners, int frameColor, int contentColor) {
        int strokeWidth = 2; // 3dp 边框宽度
        int roundRadius = 10; // 8dp 圆角半径
        int strokeColor = frameColor;// 边框颜色
        int fillColor = contentColor;// 内部填充颜色
        GradientDrawable gd = new GradientDrawable();// 创建drawable
        gd.setColor(context.getResources().getColor(fillColor));
        gd.setCornerRadius(dip2px(context, corners));
        gd.setStroke(strokeWidth, context.getResources().getColor(strokeColor));
        return gd;
    }

    // corners 为边框圆角（单位为px ） frame 为边框颜色，content为内部填充颜色 status 1 为虚线圆角
    public static GradientDrawable setBackgroundShap(Context context, int corners, int frameColor, int contentColor, int status) {
        int strokeWidth = dip2px(context, 1); // 1dp 边框宽度
        int strokeColor = frameColor;// 边框颜色
        int fillColor = contentColor;// 内部填充颜色
        GradientDrawable gd = new GradientDrawable();// 创建drawable
        gd.setColor(context.getResources().getColor(fillColor));
        gd.setCornerRadius(dip2px(context, corners));
        // gd.setStroke(strokeWidth, context.getResources().getColor(strokeColor));
        int dashWidth = dip2px(context, 6);
        int dashGap = dip2px(context, 3);
        gd.setStroke(strokeWidth, context.getResources().getColor(strokeColor), dashWidth, dashGap);
        return gd;
    }

    // corners 为边框圆角（单位为px ） frame 为边框颜色，content为内部填充颜色
    public static GradientDrawable setBackgroundShapTwo(Context context, int corners, String frameColor, String endColor, String contentColor) {
        int strokeWidth = 2; // 3dp 边框宽度
        int roundRadius = 10; // 8dp 圆角半径
        int strokeColor = Color.parseColor(frameColor);// 边框颜色
        int fillColor = Color.parseColor(contentColor);// 内部填充颜色
        GradientDrawable gd = new GradientDrawable(Orientation.LEFT_RIGHT,
                new int[] {
                        Color.parseColor(frameColor), Color.parseColor(endColor)
                });// 创建drawable
        gd.setColor(fillColor);
        gd.setCornerRadius(dip2px(context, corners));
        gd.setStroke(strokeWidth, strokeColor);
        return gd;
    }

    public static <T> String[] getStingArray(List<T> list) {
        String[] array = new String[list.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = list.get(i).toString();
        }
        return array;
    }

    // 根据手机的分辨率从 dp 的单位 转成为 px(像素)
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    // 根据手机的分辨率从 px(像素) 的单位 转成为 dp
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static boolean isEmail(String email) {
        if (null == email || "".equals(email))
            return false;
        Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");// 复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }

    // 2015年 06 月 六月突出，即中间的字体突出来
    public static void setTextThree(Context context, TextView tv, String before, String center, String last, int color, float textSize) {
        SpannableString spanString = new SpannableString(before + center + last);
        spanString.setSpan(new ForegroundColorSpan(context.getResources().getColor(color)), before.length(), before.length() + center.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色
        spanString.setSpan(new RelativeSizeSpan(textSize), before.length(), before.length() + center.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 2.0f表示默认字体大小的两倍
        // spanString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
        // before.length(), before.length() + center.length(),
        // Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //
        // 粗体
        tv.setText(spanString);
    }

    // 58人 58突出，即前面的字体突出来
    public static void setTextTwoBefore(Context context, TextView tv, String before, String last, int color, float textSize) {
        SpannableString spanString = new SpannableString(before + last);
        spanString.setSpan(new ForegroundColorSpan(context.getResources().getColor(color)), 0, before.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色
        spanString.setSpan(new RelativeSizeSpan(textSize), 0, before.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 2.0f表示默认字体大小的两倍
        // spanString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
        // before.length(), before.length() + last.length(),
        // Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //
        // 粗体
        tv.setText(spanString);
    }

    // 10小时0分
    public static void setTextFourBefore(Context context, TextView tv, String one, String two, String three, String four, int color, float textSize) {
        SpannableString spanString = new SpannableString(one + two + three + four);
        spanString.setSpan(new ForegroundColorSpan(context.getResources().getColor(color)), 0, one.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色
        spanString.setSpan(new ForegroundColorSpan(context.getResources().getColor(color)), two.length() + one.length(), two.length() + one.length() + three.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 设置前景色为洋红色
        spanString.setSpan(new RelativeSizeSpan(textSize), 0, one.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 2.0f表示默认字体大小的两倍
        spanString.setSpan(new RelativeSizeSpan(textSize), two.length() + one.length(), two.length() + one.length() + three.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); // 2.0f表示默认字体大小的两倍
        // spanString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
        // before.length(), before.length() + last.length(),
        // Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //
        // 粗体
        tv.setText(spanString);
    }

    // 判断是否有虚拟键
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;
    }

    public static int getAndroidSDKVersion() {
        int version = 0;
        try {
            version = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
        }
        return version;
    }

    // 获取手机唯一标志
    public static String getPhoneImei(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        return imei;
    }

    // 把bitmap转换成String
    public static File bitmapCompress(Activity activity, String filePath) {
        Bitmap bm = getSmallBitmap(activity, filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        // int options = 100;
        // while (baos.toByteArray().length / 1024 > 300) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        // baos.reset();// 重置baos即清空baos
        // bm.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
        // options -= 20;// 每次都减少10
        // }

        byte[] b = baos.toByteArray();
        // return Base64.encodeToString(b, Base64.DEFAULT);
        // return new String(b);
        // return getFileFromBytes(b, FileUtil.getSaveFilePath(activity) + "upload.png");
        return getFileFromBytes(b, filePath);
    }

    public static File compressPicture(Activity activity, String filePath) {
        FileOutputStream fos = null;
        BitmapFactory.Options op = new BitmapFactory.Options();

        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        op.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, op);
        op.inJustDecodeBounds = false;

        // 缩放图片的尺寸
        float w = op.outWidth;
        float h = op.outHeight;
        float hh = 1920f;//
        float ww = 1080f;//
        // 最长宽度或高度1024
        float be = 1.0f;
        if (w > h && w > ww) {
            be = (float) (w / ww);
        } else if (w < h && h > hh) {
            be = (float) (h / hh);
        }
        if (be <= 0) {
            be = 1.0f;
        }
        op.inSampleSize = (int) be;// 设置缩放比例,这个数字越大,图片大小越小.
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(filePath, op);
        int desWidth = (int) (w / be);
        int desHeight = (int) (h / be);
        bitmap = Bitmap.createScaledBitmap(bitmap, desWidth, desHeight, true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        return getFileFromBytes(b, FileUtil.getSaveFilePath(activity) + "upload.png");
    }

    /**
     * 把字节数组保存为一个文件
     * 
     * @EditTime 2007-8-13 上午11:45:56
     */
    public static File getFileFromBytes(byte[] b, String outputFile) {
        BufferedOutputStream stream = null;
        File file = null;
        try {
            file = new File(outputFile);
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        return file;
    }

    public static void setDifferentTextColor(TextView tv, String content, String unit, String color) {
        if (tv != null)
            tv.setText(Html.fromHtml(content + "<font color='" + color + "'>" + unit + "</font>"));
    }

    public static void setDifferentTextColor(TextView tv, String content, String unit, int color) {
        if (tv != null)
            tv.setText(Html.fromHtml(content + "<font color='" + color + "'>" + unit + "</font>"));
    }

    public static void setDifferentTextColorBefore(TextView tv, String content, String unit, String color) {
        tv.setText(Html.fromHtml("<font color='" + color + "'>" + content + "</font>" + unit));
    }

    public static void setDifferentTextColorMiddle(TextView tv, String str, String content, String unit, String color) {
        tv.setText(Html.fromHtml(str + "<font color='" + color + "'>" + content + "</font>" + unit));
    }

    public static int parseInt(String str) {
        if ("".equals(str) || "暂无".equals(str) || str == null) {
            return 0;
        }
        return Integer.parseInt(str);
    }
}
