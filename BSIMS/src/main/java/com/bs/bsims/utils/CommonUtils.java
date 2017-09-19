
package com.bs.bsims.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.interfaces.UpdateCallback;
import com.bs.bsims.model.DepartmentAndEmployeeVO;
import com.bs.bsims.model.MenuVO;
import com.bs.bsims.model.ResultVO;
import com.bs.bsims.model.TreeVO;
import com.bs.bsims.receiver.LogintsReceiver;
import com.bs.bsims.receiver.ScheduleReceiver;
import com.bs.bsims.time.ScreenInfo;
import com.bs.bsims.time.WheelMain;
import com.bs.bsims.view.BSDialog;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
public class CommonUtils {

    private static final String TAG = "CommonUtils";

    private static final int TAKE_PICTURE = 0x000001;
    private static final int RESULT_LOAD_IMAGE = 0x000002;

    private final float s = 1.70158f;
    float mDuration = 0f;
    private static BSDialog mDialog;
    // 用于匹配手机号码
    private final static String REGEX_MOBILEPHONE = "^0?1[3458]\\d{9}$";

    // 用于匹配固定电话号码
    private final static String REGEX_FIXEDPHONE = "^(010|02\\d|0[3-9]\\d{2})?\\d{6,8}$";

    // 用于获取固定电话中的区号
    private final static String REGEX_ZIPCODE = "^(010|02\\d|0[3-9]\\d{2})\\d{6,8}$";

    // 简单弹出框点击后回调接口
    public interface ResultCallback {
        public void callback(String str, int position);

    }

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
            LogUtils.i(TAG, "bad json: " + jsonContent);
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
     * 抖动动画
     */
    public static void startShakeAnim(Context context, View view) {
        Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);
        view.startAnimation(shake);
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

    /**
     * 显示进度
     * 
     * @param context
     * @param title
     * @param message
     * @param indeterminate
     * @param cancelable
     * @return
     */
    public static ProgressDialog showProgress(Context context, CharSequence title, CharSequence message, boolean indeterminate, boolean cancelable) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setIndeterminate(indeterminate);
        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(false);
        // dialog.setDefaultButton(false);
        dialog.show();
        return dialog;
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

    //
    public static String getJsonStr(String JsonStr, String key) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(JsonStr);
            return jsonObject.get(key).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    // public static void putData(String key, String value) {
    // JSONObject jsonObject = new JSONObject();
    // jsonObject.put(key, value);
    // ret
    // }

    //
    public static DisplayImageOptions initImageLoaderOptions() {
        //
        // DisplayImageOptions options;
        // options = new DisplayImageOptions.Builder()
        // .showImageOnLoading(R.drawable.ic_default_portrait_s) //
        // 设置图片在下载期间显示的图片
        // .showImageForEmptyUri(R.drawable.ic_default_portrait_s)//
        // 设置图片Uri为空或是错误的时候显示的图片
        // // .showImageOnFail(R.drawable.ic_default_portrait_s) //
        // 设置图片加载/解码过程中错误时候显示的图片
        // .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
        // .cacheOnDisc(true)// 设置下载的图片是否缓存在SD卡中
        // .considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
        // .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片以如何的编码方式显示
        // .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
        // // .delayBeforeLoading(int delayInMillis)//int
        // delayInMillis为你设置的下载前的延迟时间
        // // 设置图片加入缓存前，对bitmap进行设置
        // // .preProcessor(BitmapProcessor preProcessor)
        // .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
        // .displayer(new RoundedBitmapDisplayer(20))// 是否设置为圆角，弧度为多少
        // .displayer(new FadeInBitmapDisplayer(100))// 是否图片加载好后渐入的动画时间
        // .build();// 构建完成
        DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_default_portrait_s) // 设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.ic_default_portrait_s)// 设置图片Uri为空或是错误的时候显示的图片
                // .showImageOnFail(R.drawable.ic_default_portrait_s) //
                // 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true).cacheOnDisk(true).displayer(new RoundedBitmapDisplayer(20)).displayer(new FadeInBitmapDisplayer(0)).build();
        return options;

    }

    // 添加知会人头像使用的设置
    public static DisplayImageOptions initImageLoaderOptionsAdd() {
        DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.add_persion) // 设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.add_persion)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.add_persion) // 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true).cacheOnDisk(true).build();
        return options;
    }

    public static DisplayImageOptions initImageLoaderOptions1() {

        DisplayImageOptions options;
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.color.gray_bg_01) // 设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.color.gray_bg_01)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.common_ic_image_default) // 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)// 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
                // .delayBeforeLoading(int delayInMillis)//int
                // delayInMillis为你设置的下载前的延迟时间
                // 设置图片加入缓存前，对bitmap进行设置
                // .preProcessor(BitmapProcessor preProcessor)
                .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
                // .displayer(new RoundedBitmapDisplayer(20))// 是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(0))// 是否图片加载好后渐入的动画时间
                .build();// 构建完成
        return options;

    }

    // 打电话
    public static void call(Context context, String phone) {
        try {
            String str = "tel:" + phone;
            Uri uri = Uri.parse(str);
            Intent intent = new Intent(Intent.ACTION_CALL, uri);
            context.startActivity(intent);
        } catch (Exception e) {
            CustomToast.showShortToast(context, "未知错误,拨号失败");
            e.printStackTrace();
        }

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
        try {
            String str = "mailto:" + email;
            Intent data = new Intent(Intent.ACTION_SENDTO);
            data.setData(Uri.parse(str));
            context.startActivity(data);
        } catch (Exception e) {
            CustomToast.showLongToast(context, "在商店中下载邮箱就可以发送邮件啦！");
        }

    }

    private String initContent(String content, boolean night, boolean flag, Context context) {
        try {
            InputStream inputStream = context.getResources().getAssets().open("discover.html");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream), 16 * 1024);
            StringBuilder sBuilder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sBuilder.append(line + "\n");
            }
            String modelHtml = sBuilder.toString();
            inputStream.close();
            reader.close();

            String contentNew = modelHtml.replace("<--@#$%discoverContent@#$%-->", content);
            if (night) {
                contentNew = contentNew.replace("<--@#$%colorfontsize2@#$%-->", "color:#8f8f8f ;");
            } else {
                contentNew = contentNew.replace("<--@#$%colorfontsize2@#$%-->", "color:#333333 ;");
            }
            if (flag) {
                contentNew = contentNew.replace("<--@#$%colorbackground@#$%-->", "background:#B4CDE6");
            } else {
                contentNew = contentNew.replace("<--@#$%colorbackground@#$%-->", "background:#F9BADA");
            }
            return contentNew;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
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

    public static void setNonetIcon(Context context, TextView tv, UpdateCallback mCallback) {
        Drawable drawable = context.getResources().getDrawable(R.drawable.no_net);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); // 设置边界
        tv.setCompoundDrawables(null, drawable, null, null);
        tv.setCompoundDrawablePadding(10);
        tv.setText("网络不佳,点击图标重新加载哦~");
        tv.setTextColor(Color.parseColor("#666666"));
        CommonRloadNet.baseFragmentRload(tv, context, mCallback);
    }

    public static void setNonetIcon1(Context context, TextView tv, String text) {
        Drawable drawable = context.getResources().getDrawable(R.drawable.no_content);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); // 设置边界
        tv.setCompoundDrawables(null, drawable, null, null);
        tv.setCompoundDrawablePadding(10);
        tv.setText(text);
        tv.setTextColor(Color.parseColor("#666666"));
    }

    public static void setNonetContent(Context context, TextView tv, String text) {
        Drawable drawable = context.getResources().getDrawable(R.drawable.no_content);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); // 设置边界
        tv.setVisibility(View.VISIBLE);
        tv.setCompoundDrawables(null, drawable, null, null);
        tv.setCompoundDrawablePadding(10);
        tv.setText(text);
        tv.setTextColor(Color.parseColor("#666666"));
    }

    public static void setAlarm(Context context, long notifyTime, String info, String id) {
        Intent intent = new Intent(context, ScheduleReceiver.class);
        intent.putExtra("info", info + "");
        intent.putExtra("id", id);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(id), intent, 0);

        // 开始时间
        // long firstime = SystemClock.elapsedRealtime();

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // 5秒一个周期，不停的发送广播
        // am.setRepeating(AlarmManager.RTC_WAKEUP, startTime, notifyTime,
        // sender);
        am.set(AlarmManager.RTC_WAKEUP, notifyTime, pendingIntent);
    }

    public static void setReminder(Context context, long notifyTime, String rinfo) {
        // get the AlarmManager instance
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, LogintsReceiver.class);
        SharedPreferences preference = context.getSharedPreferences("schedule_count", Context.MODE_PRIVATE);
        int count = preference.getInt("count", 0);
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt("count", count++);
        editor.commit();

        i.putExtra("id", count + "");
        i.putExtra("rinfo", rinfo);
        // create a PendingIntent that will perform a broadcast
        PendingIntent pi = PendingIntent.getBroadcast(context, count, i, 0);

        // just use current time + 10s as the Alarm time.
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        // 可以根据项目要求修改，秒、分钟、提前、延后
        c.add(Calendar.SECOND, (int) notifyTime);
        // schedule an alarm
        am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
        // cancel current alarm
        am.cancel(pi);

    }

    public static String countNumberNoContent(String number) {
        number = isNormalData(number);

        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        double count = Double.parseDouble(number);
        // int count = Integer.parseInt(number);
        DecimalFormat df = new DecimalFormat("0.00");
        StringBuffer sb = new StringBuffer();
        String str = "";
        if (count < 10000) {
            return sb.append(number).append(str).toString();
        } else if (count >= 10000 && count < 100000000) {
            // if ("0.0".equals(String.valueOf(count % 10000))) {
            // return sb.append(Integer.parseInt(number) / 10000 + "万" +
            // str).toString();
            // }
            return sb.append(df.format(count / 10000)).toString();
        } else if (count >= 100000000) {
            // if ("0".equals(String.valueOf(count % 100000000))) {
            // return sb.append(Integer.parseInt(number) / 100000000 + "亿" +
            // str).toString();
            // }
            return sb.append(df.format(count / 100000000)).toString();
        }
        return sb.toString();
    }

    public static String countNumber(String number) {
        number = isNormalData(number);

        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        double count = Double.parseDouble(number);
        // int count = Integer.parseInt(number);
        DecimalFormat df = new DecimalFormat("0.00");
        StringBuffer sb = new StringBuffer();
        String str = "元";
        if (count < 10000) {
            return sb.append(number).append(str).toString();
        } else if (count >= 10000 && count < 100000000) {
            // if ("0.0".equals(String.valueOf(count % 10000))) {
            // return sb.append(Integer.parseInt(number) / 10000 + "万" +
            // str).toString();
            // }
            return sb.append(df.format(count / 10000) + "万" + str).toString();
        } else if (count >= 100000000) {
            // if ("0".equals(String.valueOf(count % 100000000))) {
            // return sb.append(Integer.parseInt(number) / 100000000 + "亿" +
            // str).toString();
            // }
            return sb.append(df.format(count / 100000000) + "亿" + str).toString();
        }
        return sb.toString();
    }

    // 去掉价格后面的“元”
    public static String countNumberSecond(String number) {
        number = isNormalData(number);

        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        double count = Double.parseDouble(number);
        // int count = Integer.parseInt(number);
        DecimalFormat df = new DecimalFormat("0.00");
        StringBuffer sb = new StringBuffer();
        if (count < 10000) {
            return sb.append(number).toString();
        } else if (count >= 10000 && count < 100000000) {
            // if ("0.0".equals(String.valueOf(count % 10000))) {
            // return sb.append(Integer.parseInt(number) / 10000 + "万" +
            // str).toString();
            // }
            return sb.append(df.format(count / 10000) + "万").toString();
        } else if (count >= 100000000) {
            // if ("0".equals(String.valueOf(count % 100000000))) {
            // return sb.append(Integer.parseInt(number) / 100000000 + "亿" +
            // str).toString();
            // }
            return sb.append(df.format(count / 100000000) + "亿").toString();
        }
        return sb.toString();
    }

    // 去掉价格后面的“元”,没有小数点
    public static String countNumberFive(String number) {
        number = isNormalData(number);

        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        double count = Double.parseDouble(number);
        // int count = Integer.parseInt(number);
        DecimalFormat df = new DecimalFormat("0");
        StringBuffer sb = new StringBuffer();
        if (count < 10000) {
            return sb.append(number).toString();
        } else if (count >= 10000 && count < 100000000) {
            // if ("0.0".equals(String.valueOf(count % 10000))) {
            // return sb.append(Integer.parseInt(number) / 10000 + "万" +
            // str).toString();
            // }
            return sb.append(df.format(count / 10000) + "万").toString();
        } else if (count >= 100000000) {
            // if ("0".equals(String.valueOf(count % 100000000))) {
            // return sb.append(Integer.parseInt(number) / 100000000 + "亿" +
            // str).toString();
            // }
            return sb.append(df.format(count / 100000000) + "亿").toString();
        }
        return sb.toString();
    }

    // 去掉价格后面的“元” 不计算亿单位的
    public static String countNumberThead(String number) {
        number = isNormalData(number);

        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        double count = Double.parseDouble(number);
        // int count = Integer.parseInt(number);
        DecimalFormat df = new DecimalFormat("0");
        StringBuffer sb = new StringBuffer();
        if (count < 10000) {
            return sb.append(number).toString();
        } else if (count >= 10000) {
            // if ("0.0".equals(String.valueOf(count % 10000))) {
            // return sb.append(Integer.parseInt(number) / 10000 + "万" +
            // str).toString();
            // }
            // count = (count%10000==0)?count/10000:(count/10000+1);
            // return sb.append(df.format(count) + "万").toString();

            if (count <= 99999) {
                DecimalFormat df1 = new DecimalFormat("0.0");
                return sb.append(df1.format(count / 10000) + "万").toString();
            }

            return sb.append(df.format(count / 10000) + "万").toString();
        }
        return sb.toString();
    }

    // 带元
    public static String countNumberSplitUnit(String number) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        double count = Double.parseDouble(number);
        // int count = Integer.parseInt(number);
        DecimalFormat df = new DecimalFormat("#.##");
        StringBuffer sb = new StringBuffer();
        String str = "元";
        if (count < 10000) {
            return sb.append(number).append("," + str).toString();
        } else if (count >= 10000 && count < 100000000) {
            // if ("0.0".equals(String.valueOf(count % 10000))) {
            // return sb.append(Integer.parseInt(number) / 10000 + "万" +
            // str).toString();
            // }
            return sb.append(df.format(count / 10000) + "万," + str).toString();
        } else if (count >= 100000000) {
            // if ("0".equals(String.valueOf(count % 100000000))) {
            // return sb.append(Integer.parseInt(number) / 100000000 + "亿" +
            // str).toString();
            // }
            return sb.append(df.format(count / 100000000) + "亿," + str).toString();
        }
        return sb.toString();
    }

    public static String countNumberSplitUnitTwo(String number) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        double count = Double.parseDouble(number);
        // int count = Integer.parseInt(number);
        DecimalFormat df = new DecimalFormat("0.00");
        StringBuffer sb = new StringBuffer();
        String str = "元";
        if (count < 10000) {
            return sb.append(number).append("," + str).toString();
        } else if (count >= 10000 && count < 100000000) {
            // if ("0.0".equals(String.valueOf(count % 10000))) {
            // return sb.append(Integer.parseInt(number) / 10000 + "万" +
            // str).toString();
            // }
            return sb.append(df.format(count / 10000) + ",万" + str).toString();
        } else if (count >= 100000000) {
            // if ("0".equals(String.valueOf(count % 100000000))) {
            // return sb.append(Integer.parseInt(number) / 100000000 + "亿" +
            // str).toString();
            // }
            return sb.append(df.format(count / 100000000) + ",亿" + str).toString();
        }
        return sb.toString();
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

    public static void setDifferentTextSizeColor(TextView tv, String str, String content, String color, String size) {
        tv.setText(Html.fromHtml(str + "<font color='" + color + "' style='font-size:18px'>" + content + "</font>"));

        // tv.setText(Html.fromHtml("<font size='20'>" + str + "</font>") + "" +
        // Html.fromHtml("<font color='" + color + "'>" + content + "</font>"));
    }

    // 设置背景透明度
    public static void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        context.getWindow().setAttributes(lp);
    }

    public static void SetWebview(WebView view) {
        WebSettings webSettings = view.getSettings();
        webSettings.setJavaScriptEnabled(true); // 支持js
        webSettings.setUseWideViewPort(false); // 将图片调整到适合webview的大小
        webSettings.setSupportZoom(true); // 支持缩放
        webSettings.supportMultipleWindows(); // 多窗口
        webSettings.setAppCacheEnabled(false);// 设置不进行缓存
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 关闭webview中缓存
        webSettings.setAllowFileAccess(true); // 设置可以访问文件
        webSettings.setNeedInitialFocus(true); // 当webview调用requestFocus时为webview设置节点
        webSettings.setBuiltInZoomControls(true); // 设置支持缩放
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); // 支持通过JS打开新窗口
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setLoadsImagesAutomatically(true); // 支持自动加载图片
    }

    // 保留2位小数
    public static String roundNumber(Double d) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
        return df.format(d);
    }

    public static Bitmap doBlur(Bitmap sentBitmap, int radius, boolean canReuseInBitmap) {
        Bitmap bitmap;
        if (canReuseInBitmap) {
            bitmap = sentBitmap;
        } else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        }

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
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

    public static void initDateView(Activity activity, final TextView tv) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        final View timepickerview = inflater.inflate(R.layout.timepicker, null);
        ScreenInfo screenInfo = new ScreenInfo(activity);
        final WheelMain wheelMain = new WheelMain(timepickerview);
        wheelMain.screenheight = screenInfo.getHeight();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        String str = calendar.get(Calendar.DAY_OF_MONTH) + "";
        if (str.length() == 1) {
            str = "0" + str;
        }

        int day = Integer.parseInt(str);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        /**
         * 显示到小时 在审批中的需求 比如请假时长 只到小时不到分钟
         * 
         * @author peck 刘鹏程
         * @since 2015/7/23 16:35
         */
        // wheelMain.initDateTimePicker(year, month, day, hour, minute);
        wheelMain.initDateTimePickerEndHour(year, month, day, hour);

        mDialog = new BSDialog(activity, "选择时间", timepickerview, new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                tv.setText(wheelMain.getTime());
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    // 老版本下拉弹出框
    public static void initPopView(Activity activity, String[] array, final View layout) {
        final PopupWindow popView;
        final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < array.length; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("option", array[i]);
            list.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(activity, list, R.layout.dropdown_approval_month_item, new String[] {
                "option"
        }, new int[] {
                R.id.textview
        });
        ListView listView = new ListView(activity);
        listView.setAdapter(adapter);
        listView.setDivider(null);

        popView = new PopupWindow(listView, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popView.setFocusable(true);
        popView.setOutsideTouchable(true);
        popView.setBackgroundDrawable(activity.getResources().getDrawable(R.color.white));
        popView.showAsDropDown(layout);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                TextView tv = (TextView) layout.findViewById(R.id.type_content);
                tv.setText(list.get((int) arg3).get("option"));
                popView.dismiss();
            }
        });
    }

    // 右上角下拉弹出带背景 widht 宽度
    public static void initPopViewBg(Activity activity, String[] array, final View layout, final ResultCallback callback, int width) {
        final PopupWindow popView;
        final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < array.length; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("option", array[i]);
            list.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(activity, list, R.layout.dropdown_nobg_item, new String[] {
                "option"
        }, new int[] {
                R.id.textview
        });

        ListView listView = new ListView(activity);
        listView.setAdapter(adapter);
        listView.setDivider(new ColorDrawable(Color.parseColor("#545D66")));
        listView.setDividerHeight(1);
        listView.setSelector(new ColorDrawable(Color.parseColor("#00000000")));

        listView.setPadding(dip2px(activity, 8), dip2px(activity, 8), dip2px(activity, 8), dip2px(activity, 8));
        // listView.setLayoutParams(params);
        listView.setBackgroundResource(R.drawable.popupwindow_bg);
        // 350
        popView = new PopupWindow(listView, width, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popView.setFocusable(true);
        popView.setOutsideTouchable(true);
        popView.setBackgroundDrawable(new BitmapDrawable());
        // popView.showAsDropDown(layout, CommonUtils.dip2px(activity, -75), 0);
        popView.showAsDropDown(layout);
        popView.setAnimationStyle(R.style.AnimationPreview);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                callback.callback(list.get((int) arg3).get("option"), (int) arg3);
                popView.dismiss();
            }
        });
    }

    // 右上角下拉弹出带背景,选项带图标的
    public static void initPopViewBg(Activity activity, String[] array, final View layout, final ResultCallback callback, int[] drables) {
        final PopupWindow popView;
        final List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

        for (int i = 0; i < array.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("option", array[i]);
            map.put("icon", drables[i]);
            list.add(map);

        }

        SimpleAdapter adapter = new SimpleAdapter(activity, list, R.layout.dropdown_icon_item,
                new String[] {
                        "icon", "option"
                }, new int[] {
                        R.id.icon, R.id.textview
                });
        ListView listView = new ListView(activity);
        listView.setAdapter(adapter);
        listView.setDivider(new ColorDrawable(Color.parseColor("#545D66")));
        listView.setDividerHeight(1);
        listView.setSelector(new ColorDrawable(Color.parseColor("#00000000")));
        listView.setPadding(15, 15, 15, 0);
        // listView.setLayoutParams(params);
        listView.setBackgroundResource(R.drawable.popupwindow_bg);
        // 350
        popView = new PopupWindow(listView, CommonUtils.getScreenWidth(activity) / 4, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popView.setFocusable(true);
        popView.setOutsideTouchable(true);
        popView.setBackgroundDrawable(new BitmapDrawable());
        popView.showAsDropDown(layout);
        popView.setAnimationStyle(R.style.AnimationPreview);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                callback.callback(list.get((int) arg3).get("option").toString(), (int) arg3);
                popView.dismiss();
            }
        });
    }

    // 获取时间控件 type 1全部 2年月日 3为年月 4月日 5 时分
    // public static View initDateView(Activity context, int type) {
    // }

    // 时间下拉弹出狂
    public static PopupWindow initPopView(Activity context, int type, int height, final ResultCallback callback) {

        LayoutInflater inflater = LayoutInflater.from(context);
        final View timepickerview = inflater.inflate(R.layout.timepicker, null);
        ScreenInfo screenInfo = new ScreenInfo(context);
        final WheelMain timeWheel = new WheelMain(timepickerview, false, false);
        timeWheel.screenheight = screenInfo.getHeight();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        switch (type) {
            case 1:
                timeWheel.initDateTimePicker(year, month, day, hour, minute);
                break;
            case 2:
                timeWheel.initDateTimePicker(year, month, day);
                break;
            case 3:
                timeWheel.initDateTimePicker(year, month);
                break;
            case 4:
                timeWheel.initDateTimePicker(month, day);
                break;
            case 5:
                timeWheel.initDateTimePicker(hour, minute);
                break;
            case 6:
                timeWheel.initDateTimePickerWithJD(year, month, 0, 0, 0);
                break;
            default:
                break;
        }

        // return timepickerview;

        final PopupWindow popView = new PopupWindow(timepickerview, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        LinearLayout linearLayout = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);// 定义文本显示组件
        timepickerview.setLayoutParams(params);
        // linearLayout.setLayoutParams(params);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(timepickerview);

        TextView textView = new TextView(context);
        int padding = CommonUtils.dip2px(context, 10);
        textView.setPadding(padding, padding, padding, padding);
        textView.setText("确定");
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(context.getResources().getColor(R.color.C1));
        textView.setBackgroundColor(context.getResources().getColor(R.color.C7));
        textView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                callback.callback(timeWheel.getYearAndMonth(), 0);
                popView.dismiss();
            }
        });
        linearLayout.addView(textView);

        popView.setContentView(linearLayout);
        popView.setWidth(LayoutParams.MATCH_PARENT);
        popView.setHeight(LayoutParams.MATCH_PARENT);

        popView.setFocusable(true);
        popView.setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(Color.parseColor("#40000000"));
        popView.setBackgroundDrawable(dw);

        linearLayout.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                popView.dismiss();
                return true;
            }
        });
        return popView;
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
        options.inSampleSize = calculateInSampleSize(options, 480, 800);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    // 把bitmap转换成String
    public static File bitmapToString(Activity activity, String filePath) {
        Bitmap bm = getSmallBitmap(activity, filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();
        // return Base64.encodeToString(b, Base64.DEFAULT);
        // return new String(b);
        return getFileFromBytes(b, filePath);
    }

    public static int getScreenWid(Activity activity) {
        // 方法1 Android获得屏幕的宽和高
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        return display.getWidth();
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

    private String writeToFile(Context context, InputStream is, String filename) {
        Log.d("LDS", "new write to file");
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(is);
            out = new BufferedOutputStream(context.openFileOutput(filename, Context.MODE_PRIVATE));
            byte[] buffer = new byte[1024];
            int l;
            while ((l = in.read(buffer)) != -1) {
                out.write(buffer, 0, l);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    Log.d("LDS", "new write to file to -> " + filename);
                    out.flush();
                    out.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return context.getFilesDir() + "/" + filename;
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

    // 缩放图片
    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    // 根据配置菜单获取权限
    public static boolean isLimits(String office, int type) {

        List<MenuVO> list = BSApplication.getInstance().getUserFromServerVO().getMenu();
        if (list == null) {
            return false;
        }
        if (type == 1) {
            List<MenuVO> listOne = list.get(0).getMenu();
            for (int i = 0; i < listOne.size(); i++) {
                String malias = listOne.get(i).getMalias();
                if (office.equals(malias)) {
                    return true;
                }
            }
        } else if (type == 2) {
            if (list.size() < 2) {
                return false;
            }
            List<MenuVO> listTwo = list.get(1).getMenu();
            for (int i = 0; i < listTwo.size(); i++) {
                String malias = listTwo.get(i).getMalias();
                if (office.equals(malias)) {
                    return true;
                }
            }
        }
        return false;
    }

    // 查看权限
    public static String getLimitsSpecial(String special) {
        List<MenuVO> list = BSApplication.getInstance().getUserFromServerVO().getMenu();
        if (list == null) {
            return "0";
        }
        for (int i = 0; i < list.size(); i++) {

            for (int j = 0; j < list.get(i).getMenu().size(); j++) {
                if (special.equals(list.get(i).getMenu().get(j).getMalias())) {
                    return "1";
                }
            }
        }
        return "0";
    }

    // 发布权限
    public static boolean getLimitsPublish(String publish) {
        List<MenuVO> list = BSApplication.getInstance().getUserFromServerVO().getMenu();
        if (list == null) {
            return false;
        }
        for (int i = 0; i < list.size(); i++) {
            if ("Publish".equals(list.get(i).getMalias())) {
                for (int j = 0; j < list.get(i).getMenu().size(); j++) {
                    if (publish.equals(list.get(i).getMenu().get(j).getMalias())) {
                        return true;
                    }
                }
            } else {
                continue;
            }
        }
        return false;
    }

    // 一级菜单带全部的是数组形式的多久菜单
    public static ArrayList<TreeVO> getTreeVOList(String[] parentItme, String[][] childItem, boolean oneLevel, boolean twoLevel) {
        ArrayList<TreeVO> list = new ArrayList<TreeVO>();
        if (parentItme.length <= 1)
            oneLevel = false;
        if (oneLevel) {
            TreeVO childAllVo = new TreeVO();
            childAllVo.setId(0);
            childAllVo.setParentId(0);
            childAllVo.setName("全部");
            childAllVo.setLevel(1);
            childAllVo.setChildSearchId(null);
            childAllVo.setParentName("全部");
            list.add(childAllVo);
        }
        int cout = 1;
        for (int i = 0; i < parentItme.length; i++) {
            TreeVO parentVo = new TreeVO();
            parentVo.setId(i + 1);
            parentVo.setParentId(0);
            parentVo.setName(parentItme[i]);
            parentVo.setLevel(1);
            parentVo.setParentSerachId((i + 1) + "");
            if (childItem[i].length > 0) {
                parentVo.setHaschild(true);
            } else {
                parentVo.setHaschild(false);
            }
            list.add(parentVo);

            for (int j = 0; j < childItem[i].length; j++) {

                if (twoLevel) {
                    if (j == 0) {
                        TreeVO childAllVo = new TreeVO();

                        childAllVo.setId(cout + 6);
                        childAllVo.setParentId(i + 1);
                        childAllVo.setName("全部");
                        childAllVo.setLevel(2);
                        childAllVo.setChildSearchId(null);
                        childAllVo.setParentName(parentItme[i]);
                        list.add(childAllVo);
                        cout++;
                    }
                }

                cout++;
                TreeVO childVo = new TreeVO();
                childVo.setId(cout + 6);
                childVo.setParentId(i + 1);
                childVo.setName(childItem[i][j]);
                childVo.setLevel(2);
                childVo.setChildSearchId((j + 1) + "");
                list.add(childVo);
            }
        }
        return list;
    }

    // 二级菜单带全部，数组形式的treevo
    public static ArrayList<TreeVO> getTreeVOList(String[] parentItme, String[][] childItem) {
        ArrayList<TreeVO> list = new ArrayList<TreeVO>();
        int cout = 1;
        for (int i = 0; i < parentItme.length; i++) {
            TreeVO parentVo = new TreeVO();
            parentVo.setId(i + 1);
            parentVo.setParentId(0);
            parentVo.setName(parentItme[i]);
            parentVo.setLevel(1);
            parentVo.setParentSerachId((i + 1) + "");
            if (childItem[i].length > 0) {
                parentVo.setHaschild(true);
            } else {
                parentVo.setHaschild(false);
            }
            list.add(parentVo);

            for (int j = 0; j < childItem[i].length; j++) {
                if (j == 0) {
                    TreeVO childAllVo = new TreeVO();

                    childAllVo.setId(cout + 6);
                    childAllVo.setParentId(i + 1);
                    childAllVo.setName("全部");
                    childAllVo.setLevel(2);
                    childAllVo.setChildSearchId(null);
                    childAllVo.setParentName(parentItme[i]);
                    list.add(childAllVo);
                    cout++;
                }

                cout++;
                TreeVO childVo = new TreeVO();
                childVo.setId(cout + 6);
                childVo.setParentId(i + 1);
                childVo.setName(childItem[i][j]);
                childVo.setLevel(2);
                childVo.setChildSearchId((j + 1) + "");
                list.add(childVo);
            }
        }
        return list;
    }

    // 二级菜单不带全部的数组形式,id去的是数据里自带id
    public static ArrayList<TreeVO> getTreeVOListNoAll(String[] parentItme, String[] parentId, String[][] childItem, String[][] childId) {
        ArrayList<TreeVO> list = new ArrayList<TreeVO>();
        int cout = 1;
        for (int i = 0; i < parentItme.length; i++) {
            TreeVO parentVo = new TreeVO();
            parentVo.setId(i + 1);
            parentVo.setParentId(0);
            parentVo.setName(parentItme[i]);
            parentVo.setLevel(1);
            parentVo.setSearchId(parentId[i]);
            if (childItem[i].length > 0) {
                parentVo.setHaschild(true);
            } else {
                parentVo.setHaschild(false);
            }
            list.add(parentVo);

            for (int j = 0; j < childItem[i].length; j++) {
                cout++;
                TreeVO childVo = new TreeVO();
                childVo.setParentName(parentItme[i]);
                childVo.setId(cout + parentItme.length);
                childVo.setParentId(i + 1);
                childVo.setName(childItem[i][j]);
                childVo.setLevel(2);
                childVo.setSearchId(childId[i][j]);
                childVo.setParentSerachId(parentId[i]);
                list.add(childVo);
            }
        }
        return list;
    }

    // 二级菜单不带全部的数组形式id去的是下标id
    public static ArrayList<TreeVO> getTreeVOListNoAll(String[] parentItme, String[][] childItem) {
        ArrayList<TreeVO> list = new ArrayList<TreeVO>();
        int cout = 1;
        for (int i = 0; i < parentItme.length; i++) {
            TreeVO parentVo = new TreeVO();
            parentVo.setId(i + 1);
            parentVo.setParentId(0);
            parentVo.setName(parentItme[i]);
            parentVo.setLevel(1);
            if (childItem[i].length > 0) {
                parentVo.setHaschild(true);
            } else {
                parentVo.setHaschild(false);
            }
            list.add(parentVo);

            for (int j = 0; j < childItem[i].length; j++) {
                cout++;
                TreeVO childVo = new TreeVO();
                childVo.setParentName(parentItme[i]);
                childVo.setId(cout + 6);
                childVo.setParentId(i + 1);
                childVo.setName(childItem[i][j]);
                childVo.setLevel(2);
                childVo.setChildSearchId(j + "");
                list.add(childVo);
            }
        }
        return list;
    }

    // 一级二级都带全部的菜单
    public static ArrayList<TreeVO> getTreeVOList() {
        ArrayList<TreeVO> list = new ArrayList<TreeVO>();
        TreeVO allTreeVo = new TreeVO();
        allTreeVo.setId(-1);
        allTreeVo.setParentId(0);
        allTreeVo.setName("全部");
        allTreeVo.setLevel(1);
        allTreeVo.setHaschild(false);
        allTreeVo.setDepartmentid("-1");
        allTreeVo.setDname("全部");
        list.add(allTreeVo);

        ResultVO resultVO = ResultVO.getInstance();
        ArrayList<DepartmentAndEmployeeVO> departments = resultVO.getDepartments();

        // 每个二级菜单添加一个全部，为了让全部排在第一，故拿出来重新写了一遍
        for (int i = 0; i < departments.size(); i++) {
            DepartmentAndEmployeeVO vo = departments.get(i);
            if ("0".equals(vo.getBelong())) {
                TreeVO childTreeVo = new TreeVO();
                childTreeVo.setId(Integer.parseInt(vo.getDepartmentid()));
                childTreeVo.setParentId(Integer.parseInt(vo.getDepartmentid()));
                childTreeVo.setName("全部");
                childTreeVo.setLevel(2);
                childTreeVo.setHaschild(false);
                childTreeVo.setDepartmentid(vo.getDepartmentid());
                childTreeVo.setDname(vo.getDname());
                list.add(childTreeVo);
            }
        }
        for (int i = 0; i < departments.size(); i++) {
            DepartmentAndEmployeeVO vo = departments.get(i);
            for (int j = 0; j < departments.size(); j++) {
                if (vo.getDepartmentid().equals(departments.get(j).getBelong())) {
                    vo.setHaschild(true);
                    break;
                } else {
                    vo.setHaschild(false);
                }
            }

            TreeVO treeVo = new TreeVO();
            treeVo.setId(Integer.parseInt(vo.getDepartmentid()));
            treeVo.setParentId(Integer.parseInt(vo.getBelong()));
            treeVo.setName(vo.getDname());
            treeVo.setLevel(Integer.parseInt(vo.getLevel()));
            treeVo.setHaschild(vo.isHaschild());
            treeVo.setDepartmentid(vo.getDepartmentid());
            treeVo.setDname(vo.getDname());
            list.add(treeVo);
        }
        return list;
    }

    // 下标从1开始的有序
    public static ArrayList<TreeVO> getOneLeveTreeVo(String[] array) {
        ArrayList<TreeVO> list = new ArrayList<TreeVO>();
        for (int i = 0; i < array.length; i++) {
            TreeVO vo = new TreeVO();
            vo.setName(array[i]);
            vo.setParentSerachId((i + 1) + "");
            vo.setSearchId((i + 1) + "");
            vo.setLevel(1);
            list.add(vo);
        }
        return list;
    }

    // 下标从0开始的有序，id是下标
    public static ArrayList<TreeVO> getOneLeveTreeVoZero(String[] array) {
        ArrayList<TreeVO> list = new ArrayList<TreeVO>();
        for (int i = 0; i < array.length; i++) {
            TreeVO vo = new TreeVO();
            vo.setName(array[i]);
            vo.setParentSerachId((i) + "");
            vo.setSearchId((i) + "");
            vo.setLevel(1);
            list.add(vo);
        }
        return list;
    }

    // 下标从0开始的有序，id是数据自带的
    public static ArrayList<TreeVO> getOneLeveTreeVoZero(String[] array, String[] itemId) {
        ArrayList<TreeVO> list = new ArrayList<TreeVO>();
        for (int i = 0; i < array.length; i++) {
            TreeVO vo = new TreeVO();
            vo.setName(array[i]);
            vo.setSearchId(itemId[i]);
            vo.setLevel(1);
            list.add(vo);
        }
        return list;
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

    // 简单的弹出对话框封装，如性别（男，女）
    public static void initSimpleListDialog(Context context, String title, final String[] array, final TextView textview) {

        List<Map<String, Object>> list = CommonUtils.getListItem(array);
        SimpleAdapter adapter = new SimpleAdapter(context, list, R.layout.dialog_lv_item, new String[] {
                "option"
        }, new int[] {
                R.id.textview
        });
        ListView listView = new ListView(context);
        listView.setAdapter(adapter);
        listView.setDivider(new ColorDrawable(Color.parseColor("#EEEEEE")));
        listView.setDividerHeight(1);
        LinearLayout linearLayout = new LinearLayout(context);
        int width;
        if (array.length > 6) {
            width = 800;
        } else {
            width = LinearLayout.LayoutParams.WRAP_CONTENT;
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, width);// 定义文本显示组件
        listView.setLayoutParams(params);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String id = array[(int) arg3].split(",")[0];
                String name = array[(int) arg3].split(",")[1];
                textview.setText(name);
                textview.setTag(id);
                mDialog.dismiss();

            }
        });
        mDialog = new BSDialog(context, title, listView, new OnClickListener() {
            @Override
            public void onClick(View arg0) {
            }
        });

        mDialog.show();
        mDialog.setButtonVisible(false);
    }

    // 简单的弹出对话框封装，如性别（男，女）,有点击后回调函数
    public static void initSimpleListDialog(Context context, String title, final String[] array, final ResultCallback callback) {

        List<Map<String, Object>> list = CommonUtils.getListItem(array);
        SimpleAdapter adapter = new SimpleAdapter(context, list, R.layout.dialog_lv_item, new String[] {
                "option"
        }, new int[] {
                R.id.textview
        });
        ListView listView = new ListView(context);
        listView.setAdapter(adapter);
        listView.setDivider(new ColorDrawable(Color.parseColor("#EEEEEE")));
        listView.setDividerHeight(1);
        LinearLayout linearLayout = new LinearLayout(context);
        int width;
        if (array.length > 6) {
            width = 800;
        } else {
            width = LinearLayout.LayoutParams.WRAP_CONTENT;
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, width);// 定义文本显示组件
        listView.setLayoutParams(params);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // String id = array[(int) arg3].split(",")[0];
                // String name = array[(int) arg3].split(",")[1];
                // id传过去
                callback.callback(array[(int) arg3], (int) arg3);
                mDialog.dismiss();
            }
        });
        mDialog = new BSDialog(context, title, listView, new OnClickListener() {
            @Override
            public void onClick(View arg0) {
            }
        });

        mDialog.show();
        mDialog.setButtonVisible(false);
    }

    // 获取时间控件 type 0全部 1年月日 2为年月 3月日 4 时分
    public static void initDateView(Activity context, String title, final TextView textview, int status) {
        LayoutInflater inflater = LayoutInflater.from(context);
        final View timepickerview = inflater.inflate(R.layout.timepicker, null);
        ScreenInfo screenInfo = new ScreenInfo(context);
        final WheelMain wheelMain = new WheelMain(timepickerview, true);
        wheelMain.screenheight = screenInfo.getHeight();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        switch (status) {
            case 1:
                wheelMain.initDateTimePicker(year, month, day, hour, minute);
                break;
            case 2:
                wheelMain.initDateTimePicker(year, month, day);
                break;
            case 3:
                wheelMain.initDateTimePicker(year, month);
                break;
            case 4:
                wheelMain.initDateTimePicker(month, day);
                break;
            case 5:
                wheelMain.initDateTimePicker(hour, minute);
                break;
            default:
                break;
        }

        mDialog = new BSDialog(context, title, timepickerview, new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                textview.setText(wheelMain.getTime());
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    // 获取时间控件 type 1全部 2年月日 3为年月 4月日 5时分
    public static BSDialog initDateViewCallback(Activity context, String title, final TextView textview, int status, final ResultCallback callback) {
        // , final ResultCallback callback
        LayoutInflater inflater = LayoutInflater.from(context);
        final View timepickerview = inflater.inflate(R.layout.timepicker, null);
        ScreenInfo screenInfo = new ScreenInfo(context);
        final WheelMain wheelMain = new WheelMain(timepickerview, true);
        wheelMain.screenheight = screenInfo.getHeight();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        switch (status) {
            case 1:
                wheelMain.initDateTimePicker(year, month, day, hour, minute);
                break;
            case 2:
                wheelMain.initDateTimePicker(year, month, day, 0, 0);
                break;
            case 3:
                wheelMain.initDateTimePicker(year, month, 0, 0, 0);
                break;
            case 4:
                wheelMain.initDateTimePicker(0, month, day, 0, 0);
                break;
            case 5:
                wheelMain.initDateTimePicker(0, 0, 0, hour, minute);
                break;
            case 6:
                wheelMain.initDateTimePicker(year, month, day, hour, 0);
            case 7:
                wheelMain.initDateTimePickerWithJD(year, month, 0, 0, 0);
            default:
                break;
        }

        mDialog = new BSDialog(context, title, timepickerview, new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                textview.setText(wheelMain.getTime());
                callback.callback(wheelMain.getTime(), 0);
                mDialog.dismiss();
            }
        });
        mDialog.show();
        return mDialog;
    }

    public static Options getBitmapOption(int inSampleSize) {
        System.gc();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return options;
    }

    public static boolean hasJsonKey(String str, String key) {
        try {
            JSONObject json = new JSONObject(str);
            if (json.has(key))
                return true;
            else
                return false;
        } catch (Exception e) {
            return false;
        }
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

    public static void PutSharedpreferencesLocations(String str, Context context) {

        SharedPreferences mySharedPreferences = context.getSharedPreferences("Location_isdenglu", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("Location_count", str);
        editor.commit();
    }

    public static String ReadSharedpreferencesLocations(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Location_isdenglu", Activity.MODE_PRIVATE);
        return sharedPreferences.getString("Location_count", "1");// 1表示后台定位

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

    /**
     * 打开系统相册
     */
    public static void systemPhoto(Activity activity) {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    // 弹出edittex对话框
    public static void showDialog(final Context context, final ResultCallback callback) {

        View v = LayoutInflater.from(context).inflate(R.layout.dialog_edittext, null);
        final EditText textView = (EditText) v.findViewById(R.id.edit_content);
        mDialog = new BSDialog(context, "请输入内容", v, new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mDialog.dismiss();
                if (textView.getText().toString().length() > 0) {
                    callback.callback(textView.getText().toString(), 200);
                }
            }
        });
        mDialog.show();
    }

    /**
     * 在TextView的右边添加 代表升降的箭头
     * 
     * @param tv
     * @param contrast 升降（1下降 绿色，2持平 黄色，3上升 红色） 箭头
     * @param mContext
     */
    public static void setDrableRight(TextView tv, String contrast, Context mContext) {

        if ("1".equals(contrast)) {
            tv.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.statistics_down), null);
        } else if ("2".equals(contrast)) {
            tv.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.statistics_same), null);
        } else {
            tv.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.statistics_up), null);

        }
        tv.setCompoundDrawablePadding(5);

    }

    /**
     * @param tv
     * @param contrast
     * @param mContext
     * @param drawableId 图片资源的ID R.drawable.sex_man
     */
    public static void setDrableRightById(TextView tv, String contrast, Context mContext, int drawableId) {
        Drawable img_off = mContext.getResources().getDrawable(drawableId);
        // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
        img_off.setBounds(0, 0, img_off.getMinimumWidth(), img_off.getMinimumHeight());
        tv.setCompoundDrawables(null, null, img_off, null); // 设置左图标
    }

    public static void setWebview(WebView view, String url) {
        WebSettings webSettings = view.getSettings();
        webSettings.setJavaScriptEnabled(true); // 支持js
        webSettings.setUseWideViewPort(false); // 将图片调整到适合webview的大小
        webSettings.setSupportZoom(true); // 支持缩放
        webSettings.supportMultipleWindows(); // 多窗口
        webSettings.setAppCacheEnabled(false);// 设置不进行缓存
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 关闭webview中缓存
        webSettings.setAllowFileAccess(true); // 设置可以访问文件
        webSettings.setNeedInitialFocus(true); // 当webview调用requestFocus时为webview设置节点
        webSettings.setBuiltInZoomControls(true); // 设置支持缩放
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false); // 支持通过JS打开新窗口
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        view.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        webSettings.setLoadsImagesAutomatically(true); // 支持自动加载图片

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

    // 右上角下拉弹出带背景 widht 宽度 final ResultCallback callback, position 为显示位置,1为view 下方，2为view上方
    public static PopupWindow leadPop(Activity activity, final View layout, String str, LinearLayout.LayoutParams textViewParams, LinearLayout.LayoutParams imageParams, int position) {
        LinearLayout rootLayout = new LinearLayout(activity);
        rootLayout.setBackgroundColor(activity.getResources().getColor(R.color.none_color));

        TextView textView = new TextView(activity);
        textView.setText(str);
        textView.setTextColor(activity.getResources().getColor(R.color.C1));
        textView.setBackgroundResource(R.drawable.lead_text);
        textView.setLayoutParams(textViewParams);
        textView.setPadding(CommonUtils.dip2px(activity, 10), CommonUtils.dip2px(activity, 10), CommonUtils.dip2px(activity, 10), CommonUtils.dip2px(activity, 10));

        ImageView imageView = new ImageView(activity);
        int w = CommonUtils.getViewWidth(layout);
        imageParams.leftMargin = w / 2;
        imageView.setLayoutParams(imageParams);

        PopupWindow popView = null;

        if (position == 1) {
            popView = new PopupWindow(rootLayout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            imageView.setBackgroundResource(R.drawable.dot);
            rootLayout.setOrientation(LinearLayout.VERTICAL);
            rootLayout.addView(imageView);
            rootLayout.addView(textView);
        } else if (position == 2) {
            popView = new PopupWindow(rootLayout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            imageView.setBackgroundResource(R.drawable.dot_up);
            rootLayout.setOrientation(LinearLayout.VERTICAL);
            rootLayout.addView(textView);
            rootLayout.addView(imageView);
        } else if (position == 3) {
            popView = new PopupWindow(rootLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);// 定义文本显示组件

            rootLayout.setOrientation(LinearLayout.HORIZONTAL);
            params.gravity = Gravity.CENTER;
            rootLayout.setGravity(Gravity.CENTER);
            rootLayout.setLayoutParams(params);
            TextView leftTv = new TextView(activity);
            LinearLayout.LayoutParams leftTvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);// 定义文本显示组件
            leftTvParams.weight = 1.0f;
            leftTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.left_bg, 0, 0, 0);
            leftTv.setLayoutParams(leftTvParams);
            rootLayout.addView(leftTv);

            rootLayout.addView(textView);
            TextView rightTv = new TextView(activity);
            LinearLayout.LayoutParams rightTvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);// 定义文本显示组件
            rightTvParams.weight = 1.0f;
            rightTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.right_bg, 0);
            rightTv.setLayoutParams(rightTvParams);
            rootLayout.addView(rightTv);

        }

        popView.setFocusable(false);
        popView.setOutsideTouchable(false);
        popView.setBackgroundDrawable(new BitmapDrawable());

        if (position == 1) {
            popView.showAsDropDown(layout);
        } else if (position == 2) {
            int[] location = new int[2];
            layout.getLocationOnScreen(location);
            popView.showAtLocation(layout, Gravity.NO_GRAVITY, location[0], location[1] -
                    CommonUtils.getViewHigh(rootLayout));
        } else if (position == 3) {
            popView.showAtLocation(layout, Gravity.CENTER, 0, CommonUtils.dip2px(activity, 85));
        }

        popView.setAnimationStyle(R.style.AnimationPreview);

        // listView.setOnItemClickListener(new OnItemClickListener() {
        // @Override
        // public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // callback.callback(list.get((int) arg3).get("option"), (int) arg3);
        // popView.dismiss();
        // }
        // });
        return popView;
    }

    public static void okLeadPop(Context activity, View view, final List<PopupWindow> listpop, int margTop) {
        LinearLayout rootLayout = new LinearLayout(activity);
        // rootLayout.setBackgroundResource(activity.getResources().getColor(R.color.C9));
        LinearLayout.LayoutParams rootParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);// 定义文本显示组件
        // rootParams.gravity = Gravity.CENTER;
        rootLayout.setGravity(Gravity.CENTER);
        rootLayout.setLayoutParams(rootParams);

        rootLayout.setBackgroundColor(activity.getResources().getColor(R.color.none_color));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(CommonUtils.dip2px(activity, 240), LinearLayout.LayoutParams.WRAP_CONTENT);// 定义文本显示组件
        params.gravity = Gravity.CENTER;
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        TextView textView = new TextView(activity);

        textView.setText("我知道了");
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(activity.getResources().getColor(R.color.C10));
        textView.setBackgroundResource(R.drawable.lead_text);
        textView.setLayoutParams(params);
        textView.setPadding(CommonUtils.dip2px(activity, 10), CommonUtils.dip2px(activity, 10), CommonUtils.dip2px(activity, 10), CommonUtils.dip2px(activity, 10));
        if (margTop == 0) {
            params.setMargins(0, CommonUtils.dip2px(activity, 300), 0, 0);
        } else {
            params.setMargins(0, CommonUtils.dip2px(activity, margTop), 0, 0);
        }
        rootLayout.addView(textView);

        final PopupWindow popView;
        popView = new PopupWindow(rootLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popView.setFocusable(false);
        popView.setOutsideTouchable(false);
        popView.setBackgroundDrawable(new BitmapDrawable());
        popView.showAtLocation(view, Gravity.CENTER, 0, 0);
        popView.setAnimationStyle(R.style.AnimationPreview);
        textView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // callback.callback("", 0);
                for (int i = 0; i < listpop.size(); i++) {
                    listpop.get(i).dismiss();
                }
                popView.dismiss();
            }
        });
    }

    public static boolean showLead(Context context, String module) {
        SharedPreferences leadSP = context.getSharedPreferences("lead_sp", Context.MODE_PRIVATE);
        Editor editor = leadSP.edit();
        if (leadSP.getBoolean(module, true)) {
            editor = leadSP.edit();
            // 将登录标志位设置为false，下次登录时不在显示首次登录界面
            editor.putBoolean(module, false);
            editor.commit();
            return true;
        }
        return false;
    }

    public static int getAndroidSDKVersion() {
        int version = 0;
        try {
            version = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
        }
        return version;
    }

    public static void setApprovalImg(int state, ImageView imageView,Context context) {

        switch (state) {
            case 0://未审
                imageView.setImageResource(R.drawable.approval_detail_status_01);
                break;
            case 1://审核中
                imageView.setImageResource(R.drawable.approval_detail_status_04);
                break;
            case 2://拒绝
                imageView.setImageResource(R.drawable.approval_detail_status_03);
                break;
            case 3://完结
                imageView.setImageResource(R.drawable.approval_detail_status_02);
                break;

           
        }

    }

}
