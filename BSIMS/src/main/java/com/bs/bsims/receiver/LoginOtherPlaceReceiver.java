/**
 * 
 */

package com.bs.bsims.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bs.bsims.activity.LoginActivity;
import com.bs.bsims.utils.DataCleanUtil;

/**
 * BS北盛最帅程序员 Copyright (c) 2016 湖北北盛科技有限公司
 * 
 * @author 梁骚侠
 * @date 2016-7-11
 * @version 2.0
 */
public class LoginOtherPlaceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        /**
         * IM登陆 当前账号被踢下线显示弹窗
         */
        intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("isOtherPlace", true);
        new DataCleanUtil(context).cleanApplicationCache(context);// 不清除数据库
        context.startActivity(intent);

    }
}
