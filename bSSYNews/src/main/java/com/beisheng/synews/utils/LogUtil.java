package com.beisheng.synews.utils;



import android.util.Log;

/**
 * LOG管理类
 * @author Administrator
 *
 */
public class LogUtil {
	private static boolean isDebug = true;//当app开发完成之后要置为false
	private static boolean isDDebug = true;
	private static boolean isIDebug = true;
	private static boolean isEDebug = true;

	/**
	 * 打印d级别的log
	 * @param tag
	 * @param msg
	 */
	public static void d(String tag, String msg){
		if(isDebug && isDDebug){
			Log.d(tag, msg);
		}
	}
	
	/**
	 * 打印d级别的log
	 * @param tag
	 * @param msg
	 */
	public static void d(Object object, String msg){
		if(isDebug && isDDebug){
			Log.d(object.getClass().getSimpleName(), msg);
		}
	}
	
	/**
	 * 打印i级别的log
	 * @param tag
	 * @param msg
	 */
	public static void i(String tag, String msg){
		if(isDebug && isIDebug){
			Log.i(tag, msg);
		}
	}

	/**
	 * 打印i级别的log
	 * @param tag
	 * @param msg
	 */
	public static void i(Object object, String msg){
		if(isDebug && isIDebug){
			Log.i(object.getClass().getSimpleName(), msg);
		}
	}


	/**
	 * 打印e级别的log
	 * @param tag
	 * @param msg
	 */
	public static void e(String tag, String msg){
		if(isDebug && isEDebug){
			Log.e(tag, msg);
		}
	}
	
	/**
	 * 打印e级别的log
	 * @param tag
	 * @param msg
	 */
	public static void e(Object object, String msg){
		if(isDebug && isEDebug){
			Log.e(object.getClass().getSimpleName(), msg);
		}
	}
	
	
}
