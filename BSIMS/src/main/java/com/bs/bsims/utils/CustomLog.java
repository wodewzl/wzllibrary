package com.bs.bsims.utils;

import android.util.Log;

public class CustomLog {
	
	private static int LOG_LEVEL = 6;
	
	private static final int ERROR = 1;
	private static final int WARN = 2;
	private static final int INFO = 3;
	private static final int DEBUG = 4;
	private static final int VERBOSE = 5;
	
	public static void e(String tag, String msg) {		// 1
		if (LOG_LEVEL > ERROR)
			Log.e(tag, msg);
	}
	public static void w(String tag, String msg) {		// 2
		if (LOG_LEVEL > WARN)
			Log.w(tag, msg);
	}
	public static void w(String tag, String msg, Throwable tr) {		// 2
		if (LOG_LEVEL > WARN)
			Log.w(tag, msg, tr);
	}
	public static void i(String tag, String msg) {		// 3
		if (LOG_LEVEL > INFO)
			Log.i(tag, msg);
	}
	public static void d(String tag, String msg) {		// 4
		if (LOG_LEVEL > DEBUG)
			Log.d(tag, msg);
	}
	public static void v(String tag, String msg) {		// 5
		if (LOG_LEVEL > VERBOSE)
			Log.v(tag, msg);
	}
	
	
}
