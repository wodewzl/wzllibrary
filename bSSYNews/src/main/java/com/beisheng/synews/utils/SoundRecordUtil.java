
package com.beisheng.synews.utils;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.beisheng.synews.interfaces.RecordResult;
import com.beisheng.synews.view.BSFloatView;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.im.zhsy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class SoundRecordUtil implements OnClickListener {
    private RecognizerDialog mIatDialog;
    private Context mContext;
    private WindowManager wm = null;
    private WindowManager.LayoutParams wmParams = null;
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private BSFloatView mBSFloatView = null;
    private RecordResult mRecordResult;
    private int x, y;

    public SoundRecordUtil(Context context, RecordResult recordResult, int x, int y) {
        this.mContext = context;
        this.mRecordResult = recordResult;
        this.x = x;
        this.y = y;
        initSound();
        createView();
    }

    public SoundRecordUtil(Context context, RecordResult recordResult) {
        this.mContext = context;
        this.mRecordResult = recordResult;
        initSound();
    }

    public void initSound() {
        SpeechUtility.createUtility(mContext, SpeechConstant.APPID + "=555942de");
        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer mInitListener
        mIatDialog = new RecognizerDialog(mContext, null);
        mIatDialog.setListener(recognizerDialogListener);
    }

    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener recognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            // if (isLast)
            printResult(results, isLast);
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
        }

    };

    private void printResult(RecognizerResult results, boolean isLast) {
        String text = parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }
        if (isLast)
            mRecordResult.getRecordResult(resultBuffer.toString());
    }

    public String parseIatResult(String json) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);

            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                // 转写结果词，默认使用第一个结果
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
                // 如果需要多候选结果，解析数组其他字段
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }

    private void createView() {
        mBSFloatView = new BSFloatView(mContext);
        mBSFloatView.setId(R.id.setid);
        mBSFloatView.setOnClickListener(this);
        // mBSFloatView.setImageResource(R.drawable.record); // 这里简单的用自带的icon来做演示

        mBSFloatView.setBackgroundResource(R.drawable.ic_launcher);
        wm = mBSFloatView.windowManager;
        // 设置LayoutParams(全局变量）相关参数
        wmParams = mBSFloatView.windowManagerParams;
        wmParams.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
        // 设置Window flag
        wmParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
        /*
         * 注意，flag的值可以为： LayoutParams.FLAG_NOT_TOUCH_MODAL 不影响后面的事件 LayoutParams.FLAG_NOT_FOCUSABLE
         * 不可聚焦 LayoutParams.FLAG_NOT_TOUCHABLE 不可触摸
         */
        // 调整悬浮窗口至左上角，便于调整坐标
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值
        wmParams.x = x;
        wmParams.y = y;
        // 设置悬浮窗口长宽数据
        wmParams.width = LayoutParams.WRAP_CONTENT;
        wmParams.height = LayoutParams.WRAP_CONTENT;
        // wmParams.width = width;
        // wmParams.height = width;
        // 显示myFloatView图像
        wm.addView(mBSFloatView, wmParams);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case 123:
                mIatDialog.show();
                break;
            default:
                break;
        }
    }

    public void removeView() {
        try {
            if (mBSFloatView != null)
                wm.removeView(mBSFloatView);
        } catch (Exception e) {
        }
    }

    public void startVoice() {
        mIatDialog.show();
    }
}
