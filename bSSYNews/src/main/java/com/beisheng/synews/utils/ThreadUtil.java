
package com.beisheng.synews.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.beisheng.base.interfaces.UpdateCallback;

public class ThreadUtil extends Thread {
    private UpdateCallback mUpdateCallback;
    private Context mContext;

    private ProgressDialog mDialog;

    public ThreadUtil(Context context, UpdateCallback updateCallback) {
        this.mUpdateCallback = updateCallback;
        this.mContext = context;
    }

    @Override
    public void run() {
        super.run();

        boolean result = mUpdateCallback.execute();
        if (result) {
            handler.sendEmptyMessage(0);
        } else {
            handler.sendEmptyMessage(1);
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    mUpdateCallback.executeSuccess();
                    break;
                case 1:
                    mUpdateCallback.executeFailure();
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
}
