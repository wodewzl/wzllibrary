package com.bs.bsims.utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



public class CommonDateUtils {
	
	/** 判断时间是否过期 */
	public static boolean isOutDate(String limited_time) {
		
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		String tempstr = year + "-" + month + "-" + day;
		
		int index1 = limited_time.indexOf("-");
		int index2 = limited_time.lastIndexOf("-");
		
		String str1 = limited_time.substring(0, index1);
		String str2 = limited_time.substring(index1 + 1, index2);
		String str3 = limited_time.substring(index2 + 1, limited_time.length());
		
		int mark_year = Integer.valueOf(str1);
		int mark_month = Integer.valueOf(str2);
		int mark_day = Integer.valueOf(str3);
		
		if (year > mark_year) {
			return true;
		}
		
		if (year < mark_year) {
			return false;
		}

		if (month > mark_month) {
			return true;
		}

		if (month < mark_month) {
			return false;
		}

		if (day > mark_day) {
			return true;
		} else {
			return false;
		}

	}
	
	
	/**
	 * 将字符串日期，转化为规定的格式
	 * @param dateString
	 * @return
	 */
	public static Date stringToDate(String dateString) {
		ParsePosition position = new ParsePosition(0);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dateValue = simpleDateFormat.parse(dateString, position);
		return dateValue;
	}
	
	/**
	 * 将字符串日期，转化为规定的格式 传入的日期格式为yyyy-MM-dd
	 * @param dateString
	 * @return
	 */
	public static Date string11112233ToDate(String dateString) {
		ParsePosition position = new ParsePosition(0);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd ");
		Date dateValue = simpleDateFormat.parse(dateString, position);
		return dateValue;
	}

	
	/**
	 * 将时间毫秒值，格式化成规定的字符串
	 * @param milliseconds
	 * @return
	 */
	public static String parseDate(long milliseconds) {
		Date date = new Date(milliseconds);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String format = simpleDateFormat.format(date);
		return format;
	}
	
	/**
	 * 将时间毫秒值，格式化成规定的字符串
	 * @param milliseconds
	 * @return
	 */
	public static String parseDate(long milliseconds, String pattern) {
		Date date = new Date(milliseconds);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String format = simpleDateFormat.format(date);
		return format;
	}
	
}
