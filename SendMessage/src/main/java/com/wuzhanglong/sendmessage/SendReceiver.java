package com.wuzhanglong.sendmessage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.wuzhanglong.library.mode.EBMessageVO;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by ${Wuzhanglong} on 2017/8/15.
 */

public class SendReceiver extends BroadcastReceiver {
    public final static String ACTION_SEND = "send.msg.ACTION_SEND";

    @Override
    public void onReceive(final Context context, Intent intent) {
        String action = intent.getAction();
        if (ACTION_SEND.equals(action)) {
            EventBus.getDefault().post(new EBMessageVO("msg"));
        }
    }
}