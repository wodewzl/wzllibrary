
package com.beisheng.base.application;

import android.app.Application;

public class BaseAppApplication extends Application {
    private static BaseAppApplication mAppApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppApplication = this;
        // isPay();
    }

    /** 获取Application */
    public static BaseAppApplication getInstance() {
        return mAppApplication;
    }

    // public String getFontSize() {
    // return SharePreferenceUtil.getSharedpreferenceValue(this, "webview_font_size", "size");
    // }
    //
    // public void setFontSize(String fontSize) {
    // SharePreferenceUtil.putSharedpreferences(this, "webview_font_size", "size", fontSize);
    // }

}
