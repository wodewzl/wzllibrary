package com.wuzhanglong.library.interfaces;

import android.app.Activity;

import com.wuzhanglong.library.mode.BaseVO;

public interface UpdateCallback  {

    public abstract  void execute();
//    public abstract void executeSuccess();
//    public abstract void executeFailure();
    public abstract void baseHasData(BaseVO v);
    public abstract void baseNoData(BaseVO vo);
    public abstract void baseNoNet();
    public abstract void show();
    public abstract void finishActivity(Activity activity,int code);
}
