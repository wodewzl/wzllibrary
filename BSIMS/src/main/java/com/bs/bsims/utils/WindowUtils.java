package com.bs.bsims.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * 和屏幕相关的方法
 * @author Administrator
 *
 */
public class WindowUtils {
	
	
	/**
	 * 获取屏幕分辨率
	 * @param activity
	 * @return
	 */
	public static DisplayMetrics getScreenSize(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();   
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);   
		//Log.d(TAG, dm.widthPixels + "*" + dm.heightPixels);
		//Log.d(TAG, dm.density + " :: " + dm.densityDpi);
		return dm;
	}
	
	
	
	
	
	
	public static int dip2px(Context context, float dipValue){  
		/*
        final float scale = context.getResources().getDisplayMetrics().density;   
        return (int)(dipValue * scale + 0.5f);   
        */
        final float scale = context.getResources().getDisplayMetrics().densityDpi;
        return (int)(dipValue*(scale/160)+0.5f);
	}   
 
	
	
	public static int px2dip(Context context, float pxValue){   
        final float scale = context.getResources().getDisplayMetrics().density;   
        return (int)(pxValue / scale + 0.5f);   
	}
	
	
	/**
	 * 关闭软键盘
	 * @param activity
	 */
	public static void hideKeyboard(Activity activity) {
		InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);;
		if (activity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (activity.getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	
	/**
	 * 显示/隐藏软键盘
	 * 输入法在窗口上切换显示，如果输入法在窗口上已经显示，则隐藏，如果隐藏，则显示输入法到窗口上
	 * @param context
	 */
	public static void showKeyboard(Context context) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);  
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	
	
}
