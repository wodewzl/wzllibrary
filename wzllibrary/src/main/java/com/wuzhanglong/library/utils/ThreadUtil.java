package com.wuzhanglong.library.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.wuzhanglong.library.interfaces.RequestMessageCallback;
import com.wuzhanglong.library.interfaces.UpdateCallback;
import com.wuzhanglong.library.mode.BaseVO;

public class ThreadUtil extends Thread implements RequestMessageCallback {
    private UpdateCallback mUpdateCallback;
    private Context mContext;
    public static final int HAS_DATA_CODE = 1;
    public static final int NO_DATA_CODE = 2;
    public static final int NO_NET_CODE = 3;
    public static final int SHOW_CODE = 4;//不用请求数据之间显示的
    private ProgressDialog mDialog;
    private BaseVO mBaseVO;

    public ThreadUtil(Context context, UpdateCallback updateCallback) {
        this.mUpdateCallback = updateCallback;
        this.mContext = context;
    }

    @Override
    public void run() {
        super.run();

        mUpdateCallback.execute();

//        sendMsgByModel(mBaseVO);
    }
    public void sendMsgByModel(BaseVO vo){
        mBaseVO=vo;
        if (mBaseVO == null) {
            handler.sendEmptyMessage(4);
            return;
        }
        if ("200".equals(mBaseVO.getCode())) {
//            mUpdateCallback.baseHasData(vo);
            handler.sendEmptyMessage(1);
        } else if ("400".equals(mBaseVO.getCode())) {
//            mUpdateCallback.baseHasData(vo);
            handler.sendEmptyMessage(2);
        } else if ("600".equals(mBaseVO.getCode())) {
            handler.sendEmptyMessage(3);
        }else if ("300".equals(mBaseVO.getCode())) {
            handler.sendEmptyMessage(2);
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HAS_DATA_CODE:
                    mUpdateCallback.baseHasData(mBaseVO);
                    break;
                case NO_DATA_CODE:
                    mUpdateCallback.baseNoData(mBaseVO);
                    break;
                case NO_NET_CODE:
                    mUpdateCallback.baseNoNet();
                    break;
                case SHOW_CODE:
                    mUpdateCallback.show();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 获取Thread
     *
     * @return
     */
    public Thread getThread() {
        return this;
    }


    @Override
    public void sendMessage(BaseVO vo) {
        sendMsgByModel(vo);


    }
}
