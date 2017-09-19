
package com.bs.bsims.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.BaseAdapter;

import java.util.List;

public class HandlerUtil extends Handler {
    public static final int SUCCESS = 1;
    public static final int FAIL = 0;
    private Context mContext;
    private BaseAdapter adapter;
    private List<BaseAdapter> mList;

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case SUCCESS:
                for (int i = 0; i < mList.size(); i++) {
                    mList.get(i).notifyDataSetChanged();
                }
                break;
            case FAIL:
                break;
            default:
                break;
        }
    }

    public HandlerUtil(Context context, List<BaseAdapter> list) {
        this.mContext = context;
        this.mList = list;
    }

    public BaseAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(BaseAdapter adapter) {
        this.adapter = adapter;
    }

}
