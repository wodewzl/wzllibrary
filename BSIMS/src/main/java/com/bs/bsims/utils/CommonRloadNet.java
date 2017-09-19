/**
 * 
 */

package com.bs.bsims.utils;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.bs.bsims.interfaces.UpdateCallback;

/**
 * BS北盛最帅程序员 Copyright (c) 2016 湖北北盛科技有限公司
 * 
 * @author 梁骚侠
 * @date 2016-7-16
 * @version 2.0
 */
public class CommonRloadNet {
    /**
     * Fragment 加载
     */
    public static void baseFragmentRload(TextView loadTextView, final Context context, final UpdateCallback updateCallBack) {
        loadTextView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new ThreadUtil(context, updateCallBack).start();
            }
        });

    }

    /**
     * Activity 加载
     */
    public static void baseActvitytRload(TextView loadTextView, final Context context, final UpdateCallback updateCallBack) {
        loadTextView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new ThreadUtil(context, updateCallBack).start();
            }
        });

    }
}
