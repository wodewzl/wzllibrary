package com.wuzhanglong.library.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
/**版本工具类
 * @author BXQ
 *
 */
public class VersionUtils {
	
	public static String getversionName(Context context) {
		
		PackageManager manager = context.getPackageManager();
		
		try {
			PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	
	public static int getversionCode(Context context) {
		
		PackageManager manager = context.getPackageManager();
		
		try {
			PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
}
