package com.bs.bsims.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommDateFormat {
	
	
	/**
	 * 将时间戳，按指定格式转换为时间
	 * @param title
	 * @param format
	 * @return
	 */
	public static String getDateFromStamp(long stamp, String pattern) {
		Date date = new Date(stamp);
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		String format = dateFormat.format(date);
		return format;
	}
	
	
	
	/**
	 * 通过给定的format，将制定的时间，转为时间戳
	 * @param time
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static long getShortTimeStamp(String time, String format) throws ParseException {
		long timeStamp = 0;
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		Date date = dateFormat.parse(time);
		timeStamp = date.getTime();
		return timeStamp/1000;
	}
	
	
	/**
	 * 通过给定的format，将制定的时间，转为时间戳
	 * @param time
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static long getLongTimeStamp(String time, String format) throws ParseException {
		long timeStamp = 0;
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		Date date = dateFormat.parse(time);
		timeStamp = date.getTime();
		return timeStamp;
	}
	
	
	/**
	 * 获取当前日期
	 * @return
	 */
	public static String getCurrentTodayDate() {
		long currentTimeMillis = System.currentTimeMillis();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String date = format.format(new Date(currentTimeMillis));
		return date;
	}
	
	
	/**
	 * 获取当前日期
	 * @param pattern 格式化的标准
	 * @return
	 */
	public static String getCurrentTodayDate(String pattern) {
		long currentTimeMillis = System.currentTimeMillis();
		DateFormat format = new SimpleDateFormat(pattern);
		String date = format.format(new Date(currentTimeMillis));
		return date;
	}
	
	
	/**
	 * 获取当前日期，包括秒
	 */
	public static String getTodayDateSeconds() {
		long currentTimeMillis = System.currentTimeMillis();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = format.format(new Date(currentTimeMillis));
		return date;
	}
	
	
	/**
	 * 通过毫秒值，得到对应的时间，时间包含秒
	 * @param TimeMillis
	 * @param isShort 是否是10位的时间戳
	 */
	public static String getDateSeconds4TimeMillis(long timeMillis, boolean isShort) {
		if(isShort)
			timeMillis *= 1000; 
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = format.format(new Date(timeMillis));
		return date;
	}
	
	
	/**
	 * 通过毫秒值，得到对应的时间字符串
	 * @param TimeMillis
	 * @param isShort 是否是10位的时间戳
	 */
	public static String getDate4TimeMillis(long timeMillis, boolean isShort) {
		if(isShort)
			timeMillis *= 1000; 
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String date = format.format(new Date(timeMillis));
		return date;
	}
	
	
	public static void getTimeMillis(String dateString) {
		
	}
	
	//1405673601522
	//1405673612427
	//1405673871189
	//1405674057180
	/**
	 * 计算两个毫秒值时间的时差
	 * @param timeMillis
	 * @param currentTimeMillis
	 */
	public static TimeLag getTimeLag(long timeMillis, long currentTimeMillis) {
		
		long long0 = 1000 * 60 * 20;
		//long secondsTime = Math.abs(long0)/1000;	//秒
		long secondsTime = Math.abs(timeMillis-currentTimeMillis)/1000;	//秒
		
		TimeLag timeLag = new TimeLag();
		
		timeLag.day = secondsTime / (24 * 60 * 60);
		timeLag.hour = (secondsTime % (24 * 60 * 60)) / (60 * 60);
		timeLag.minute = ((secondsTime % (24 * 60 * 60)) % (60 * 60)) / 60;
		timeLag.seconds = ((secondsTime % (24 * 60 * 60)) % (60 * 60)) % 60;
		
		//System.out.println(timeLag);
		
		return timeLag;
		
	}
	
	
	/**
	 * 获取星期几
	 * @param date 
	 * @return
	 */
	public static int getWeekDay(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = format.format(date);
		return weekDayAlgorithm(dateStr);
	}
	
	
	/**
	 * 获取星期几
	 * @param timeMillis 时间戳
	 * @return
	 */
	public static int getWeekDay(long timeMillis) {
		Date date = new Date(timeMillis);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = format.format(date);
		return weekDayAlgorithm(dateStr);
	}
	
	/**
	 * 获取星期几
	 * @param dateStr 以"yyyy-MM-dd"为格式的日期字符串
	 * @return
	 */
	public static int getWeekDay(String dateStr) {
		return weekDayAlgorithm(dateStr);
	}
	
	
	/**
	 * 核心算法
	 * w=y+[y/4]+[c/4]-2c+[26(m+1)/10]+d-1,
	 * [ ]代表取整,
	 * d：日,
	 * C: 世纪数减一,
	 * y: 年份后两位,
	 * M: 月份,
	 * d: 日数,
	 * 1月和2月要按上一年的13月和 14月来算,这时C和y均按上一年取值。
	 * 算出来的W除以7,余数是几就是星期几。如果余数是0,则为星期日。
	 * 1970 -- 2100.
	 * @param dateStr 以"yyyy-MM-dd"为格式的日期字符串
	 * @return 1代表星期一，依次类推，0代表星期天
	 */
	private static int weekDayAlgorithm(String dateStr) {
		int century = 0;
		int year = 0;
		int month = 0;
		int day = 0;
		
		String[] split = dateStr.split("-");
		
		//判断日
		day = Integer.parseInt(split[2]);
		
		//判断月
		int parseMonth = Integer.parseInt(split[1]);
		if(parseMonth == 1)
			month = 13;
		else if(parseMonth == 2)
			month = 14;
		else
			month = parseMonth;
		
		//判断年
		int parseYear = Integer.parseInt(split[0]);
		if(parseMonth==1 || parseMonth==2)
			year = parseYear - 1;
		else
			year = parseYear;
		
		//判断世纪
		if(year < 2000) {
			century = 19;
			year = year - 1900;
		} else if(year>=2000 && year<2100) {
			century = 20;
			year = year - 2000;
		}
		
		// y+[y/4]+[c/4]-2c+[26(m+1)/10]+d-1;
		int w = year + ((int)(year/4)) + ((int)(century/4)) - 2*century + ((int)(26*(month+1)/10)) + day - 1;
		int d = w % 7;
		if(d < 0)
			d += 7;
		return d;
	}
	
	public static class TimeLag {
		public long day;
		public long hour;
		public long minute;
		public long seconds;
		
		@Override
		public String toString() {
			return "TimeLag [day=" + day + ", hour=" + hour + ", minute=" + minute + ", seconds=" + seconds + "]";
		}
		
	}
	
	
}
