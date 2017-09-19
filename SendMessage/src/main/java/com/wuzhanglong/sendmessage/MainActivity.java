package com.wuzhanglong.sendmessage;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.rey.material.widget.TextView;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.interfaces.PostCallback;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.mode.EBMessageVO;
import com.wuzhanglong.library.utils.BaseCommonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends BaseActivity implements PostCallback, View.OnClickListener {
    private EditText mEt;
    private TextView mTv01, mTv02, mTv03;
    private double mBackPressed;
    private MessageInfo mMessageInfo;


    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.activity_main);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initView() {
        mBaseTitleTv.setText("消息");
        mEt = getViewById(R.id.et);
        mTv01 = getViewById(R.id.tv_01);
        mTv02 = getViewById(R.id.tv_02);
        mTv03 = getViewById(R.id.tv_03);
        mTv01.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.C7, R.color.C7));
        mTv02.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.C3, R.color.C3));
//        getMessgaeInfo();
    }

    @Override
    public void bindViewsListener() {
        mTv01.setOnClickListener(this);
        mTv02.setOnClickListener(this);

        EventBus.getDefault().register(this);
    }

    @Override
    public void getData() {
        HttpClientUtil.show(mThreadUtil);
    }

    @Override
    public void hasData(BaseVO vo) {
        MessageInfo messageInfo = (MessageInfo) vo;
    }

    @Override
    public void noData(BaseVO vo) {

    }

    @Override
    public void noNet() {

    }

    @Override
    public void success(BaseVO vo) {

    }


    public void getMessgaeInfo() {
        final RequestParams params = new RequestParams();
        String url = "http://www.xyue99.com/api.php?c=mobile&a=getinfo";
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                dismissProgressDialog();
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                dismissProgressDialog();
                final Gson gson = new Gson();
                String result = new String(arg2);
                mMessageInfo = (MessageInfo) gson.fromJson(result, MessageInfo.class);
                if (mMessageInfo.getDatas() == null)
                    return;

                mTv03.setText("");
                for (int i = 0; i < mMessageInfo.getDatas().size(); i++) {
                    mTv03.setText(mMessageInfo.getDatas().get(i).getMobile() + "," + mTv03.getText().toString());
                }


                for (int i = 0; i < mMessageInfo.getDatas().size(); i++) {
                    String phone = mMessageInfo.getDatas().get(i).getMobile();
                    String context = mMessageInfo.getDatas().get(i).getContent();
                    SmsManager manager = SmsManager.getDefault();
                    if (context.length() > 70) {
                        ArrayList<String> list = manager.divideMessage(context);  //因为一条短信有字数限制，因此要将长短信拆分
                        for (String text : list) {
                            if (phone.length() > 0)
                                manager.sendTextMessage(phone, null, text, null, null);
                        }
                    } else {
                        if (phone.length() > 0)
                            manager.sendTextMessage(phone, null, context, null, null);
                    }
                }

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_01:
                sartAlarmManager();
                mTv01.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.C3, R.color.C3));
                mTv02.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.C7, R.color.C7));
                mTv01.setTextColor(ContextCompat.getColor(this, R.color.C5));
                mTv02.setTextColor(ContextCompat.getColor(this, R.color.C1));
                break;
            case R.id.tv_02:
                stopAlarmManager();
                mTv01.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.C7, R.color.C7));
                mTv02.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.C3, R.color.C3));
                mTv01.setTextColor(ContextCompat.getColor(this, R.color.C1));
                mTv02.setTextColor(ContextCompat.getColor(this, R.color.C5));
                break;
            default:
                break;
        }
    }

    public void sartAlarmManager() {
        Intent intent = new Intent("send.msg.ACTION_SEND");
        PendingIntent sendIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        am.cancel(sendIntent);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), BaseCommonUtils.parseInt(mEt.getText().toString()) > 0 ? Integer.parseInt(mEt.getText().toString()) * 1000 : 60 * 1000,
                sendIntent);
    }

    public void stopAlarmManager() {
        Intent intent = new Intent("send.msg.ACTION_SEND");
        PendingIntent sendIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        am.cancel(sendIntent);
//        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), Integer.parseInt(mEt.getText().toString())>0  ? Integer.parseInt(mEt.getText().toString())*1000 : 60* 1000, sendIntent);
    }

    @Override
    public void onBackPressed() {
        if (isShow()) {
//            dismissProgressDialog();
        } else {
            if (mBackPressed + 3000 > System.currentTimeMillis()) {
                finish();
                super.onBackPressed();
            } else
                showCustomToast("再次点击，退出" + this.getResources().getString(R.string.app_name));
            mBackPressed = System.currentTimeMillis();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EBMessageVO event) {
        getMessgaeInfo();
    }


}
