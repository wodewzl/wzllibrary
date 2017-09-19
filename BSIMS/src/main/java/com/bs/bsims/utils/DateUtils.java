
package com.bs.bsims.utils;

import android.annotation.SuppressLint;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint("SimpleDateFormat")
public class DateUtils {
    public static final SimpleDateFormat dateFormatYY_MM = new SimpleDateFormat(
            "yyyy-MM");
    public static final SimpleDateFormat dateFormatYY_MM_DD = new SimpleDateFormat(
            "yyyy-MM-dd");

    /**
     * 获取上个月分
     * 
     * @return
     */
    public static String getLastMonthDate() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // cal.add(Calendar.MONTH, -1);
        cal.add(Calendar.MONTH, -1);

        String yyyy_mm = dateFormatYY_MM_DD.format(cal.getTime());
        return yyyy_mm;
    }

    public static String getLastDay(int i) {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // cal.add(Calendar.MONTH, -1);
        cal.add(Calendar.DAY_OF_MONTH, -i);

        String yyyy_mm = dateFormatYY_MM_DD.format(cal.getTime());
        return yyyy_mm;
    }

    public static String getLastMonth() {

        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        Calendar lastDate = (Calendar) cal.clone();
        lastDate.add(Calendar.MONTH, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(lastDate.getTime());
    }

    /**
     * 返回上个月的日期
     * 
     * @return 格式为2015-5
     */
    public static String getLastMonthYYYYM() {

        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        Calendar lastDate = (Calendar) cal.clone();
        lastDate.add(Calendar.MONTH, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        return sdf.format(lastDate.getTime());
    }

    /**
     * 返回上个月的日期
     * 
     * @return 格式为2015-5
     */
    public static String getLast2MonthYYYYM() {

        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        Calendar lastDate = (Calendar) cal.clone();
        lastDate.add(Calendar.MONTH, -2);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        return sdf.format(lastDate.getTime());
    }

    /**
     * 返回上个月的日期
     * 
     * @return 格式为2015-5
     */
    public static String getNext2MonthYYYYM() {

        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        Calendar lastDate = (Calendar) cal.clone();
        lastDate.add(Calendar.MONTH, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        return sdf.format(lastDate.getTime());
    }

    public static String getTureMonthYYYYM() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        Calendar lastDate = (Calendar) cal.clone();
        lastDate.add(Calendar.MONTH, 0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        return sdf.format(lastDate.getTime());
    }

    /**
     * 返回上个月的日期
     * 
     * @return 格式为2015-05
     */
    public static String getLastMonthYYYYMM() {

        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        Calendar lastDate = (Calendar) cal.clone();
        lastDate.add(Calendar.MONTH, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        return sdf.format(lastDate.getTime());
    }

    public static String getLastMonth(int i) {

        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        Calendar lastDate = (Calendar) cal.clone();
        lastDate.add(Calendar.MONTH, -i);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(lastDate.getTime());
    }

    public static String getLastOrNextMonth(int i,String datetime) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");  
        Date date = null;  
        try {  
            date = sdf.parse(datetime);  
        } catch (ParseException e) {  
            e.printStackTrace();  
        }  
        Calendar cl = Calendar.getInstance();  
        cl.setTime(date);  
        cl.add(Calendar.MONTH, i);  
        date = cl.getTime();  
        return sdf.format(date);  
    }

    /**
     * 获取当前年
     * 
     * @return
     */
    public static int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取当前月份
     * 
     * @return
     */
    public static int getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH);
    }

    /**
     * 获取当前日期
     * 
     * @return
     */
    public static int getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获得指定日期的下一天
     * 
     * @param date
     * @return
     */
    public static Date getNextDay(java.util.Date date) {
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        gc.add(Calendar.DATE, 1);
        return new Date(gc.getTimeInMillis());
    }

    /**
     * 获得年度申请申报年份
     */
    public static int getYearPlanYear() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        if (month > 8) {
            return calendar.get(Calendar.YEAR) + 1;
        } else {
            return calendar.get(Calendar.YEAR);
        }
    }

    /**
     * 获得传入月的第一天和最后一天
     * 
     * @param year
     * @param month
     * @param type 1为第一天，2为最后1天
     * @return
     * @throws Exception
     */
    public static String getDateByYearAndMonth(int year, int month, int type) {
        String dateTime = "";
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        int day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);// 本月份的天数
        if (1 == type) {// 得到当前第一天
            calendar.set(Calendar.DATE, 1);
            dateTime = (new SimpleDateFormat("yyyy-MM-dd")).format(calendar
                    .getTime());
        } else { // 得到当月最后一天
            calendar.set(Calendar.DATE, day);
            dateTime = (new SimpleDateFormat("yyyy-MM-dd")).format(calendar
                    .getTime());
        }
        return dateTime;
    }

    /**
     * 日期格式的转换
     * <p>
     * 
     * @param ts Timestamp格式的时间
     * @return 日期字符串
     */
    public static Timestamp getTimestamp(String strDate) {
        if (null == strDate || "".equals(strDate)) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date date = null;

        try {
            date = sdf.parse(strDate);
        } catch (ParseException pe) {
            throw new RuntimeException("错误的日期字符串！");
        }

        return new Timestamp(date.getTime());
    }

    /**
     * 日期格式的转换
     * <p>
     * 
     * @param ts Timestamp格式的时间
     * @return 日期字符串
     */
    public static String getDispalyDate(Timestamp ts) {
        if (ts == null) {
            return "";
        }
        return MessageFormat.format("{0,time,yyyy-MM-dd hh:mm}",
                new Object[] {
                    ts
                });
    }

    public static String getDispalyDate(String strDate) {
        if (strDate == null) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        strDate = sdf.format(strDate);

        return strDate;
    }

    public static String getDispalyTime(String strTime) {
        if (strTime == null) {
            return "";
        }
        return MessageFormat.format("{0,time,yyyy-MM-dd}",
                new Object[] {
                    strTime
                });
    }

    /**
     * 获取当前日期
     * <p>
     * 
     * @return String当前日期
     */
    public static String getCurrentDate() {
        SimpleDateFormat objSimpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd");
        return objSimpleDateFormat.format(new Timestamp(System
                .currentTimeMillis()));
    }

    public static String getCurrentDate11112() {
        SimpleDateFormat objSimpleDateFormat = new SimpleDateFormat("yyyy-M");
        return objSimpleDateFormat.format(new Timestamp(System
                .currentTimeMillis()));
    }

    public static String getCurrentDate11113() {
        SimpleDateFormat objSimpleDateFormat = new SimpleDateFormat("yyyy-MM");
        return objSimpleDateFormat.format(new Timestamp(System
                .currentTimeMillis()));
    }

    /**
     * 获取当前日期
     * <p>
     * 
     * @return String当前日期yyyy-MM-dd hh:mm
     */
    public static String getCurrentTimeMM() {
        SimpleDateFormat objSimpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm");
        return objSimpleDateFormat.format(new Timestamp(System
                .currentTimeMillis()));
    }

    /**
     * 获取当前日期
     * <p>
     * 
     * @return String当前日期 yy-MM-dd hh:mm
     */
    public static String getCurrentTimeYYMMddhhmm() {
        SimpleDateFormat objSimpleDateFormat = new SimpleDateFormat(
                "yy-MM-dd hh:mm");
        return objSimpleDateFormat.format(new Timestamp(System
                .currentTimeMillis()));
    }

    /**
     * 获取当前日期
     * <p>
     * 
     * @return String当前日期 MM-dd hh:mm
     */
    public static String getCurrentTimeMMddhhmm() {
        SimpleDateFormat objSimpleDateFormat = new SimpleDateFormat(
                "MM-dd hh:mm");
        return objSimpleDateFormat.format(new Timestamp(System
                .currentTimeMillis()));
    }

    /**
     * 获取当前日期
     * <p>
     * 
     * @return String当前日期 yyyy-MM-dd hh:mm:ss
     */
    public static String getCurrentTimeSS() {
        SimpleDateFormat objSimpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd hh:mm:ss");
        return objSimpleDateFormat.format(new Timestamp(System
                .currentTimeMillis()));
    }

    /**
     * 获取当前日期
     * <p>
     * 
     * @return String当前日期 yyyy-MM-dd hh:mm:ss
     */
    public static String getCurrentTimess() {
        SimpleDateFormat objSimpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        return objSimpleDateFormat.format(new Timestamp(System
                .currentTimeMillis()));
    }

    /**
     * 获取当前日期
     * <p>
     * 
     * @return Timestamp当前日期
     */
    public static Timestamp getCurrentDateTimestamp() {
        SimpleDateFormat objSimpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd");
        String strDate = objSimpleDateFormat.format(new Timestamp(System
                .currentTimeMillis()));
        return getTimestamp(strDate);
    }

    /**
     * <p>
     * 比较时间tmBegin和tmEnd大小
     * </p>
     * 
     * @param tmBegin
     * @param tmEnd
     * @return boolean
     */
    public static boolean compareTime(Date tmBegin, Date tmEnd) {
        boolean bReturn = false;

        if (null == tmBegin || null == tmEnd) {
            return bReturn;
        }

        long lBegin = tmBegin.getTime();
        long lEnd = tmEnd.getTime();

        if (lBegin < lEnd) {
            bReturn = true;
        }
        return bReturn;
    }

    /**
     * 获取2个日期之间的工作日，日期传入格式("yyyy-MM-dd")
     * 
     * @param dateBegin
     * @param dateEnd
     * @return
     * @throws Exception
     */
    public static int getWorkDay(String dateBegin, String dateEnd)
            throws Exception {
        // 设置时间格式
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // 开始日期
        Date dateFrom = dateFormat.parse(dateBegin);
        // 结束日期
        Date dateTo = dateFormat.parse(dateEnd);
        // 工作天数
        int workdays = 0;

        while (dateFrom.before(dateTo)) {
            Calendar cal = Calendar.getInstance();
            // 设置日期
            cal.setTime(dateFrom);
            if ((cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY)
                    && (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)) {
                // 进行比较
                workdays++;
            }
            // 日期加1
            cal.add(Calendar.DAY_OF_MONTH, 1);
            dateFrom = cal.getTime();
            cal = null;
        }
        if (0 != workdays) {
            workdays++;
        }
        return workdays;
    }

    public static long getStringToDate(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date();
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static long getStringToDate(String time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date();
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static String getCurrentDate1MY1111N() {
        SimpleDateFormat objSimpleDateFormat = new SimpleDateFormat("yyyy年-MM月");
        // else formatter = new SimpleDateFormat ("yyyy年MM月dd日");
        // Date date1 = new Date();
        // String time = String.format("%tB", date1);
        return objSimpleDateFormat.format(new Timestamp(System
                .currentTimeMillis()));
    }

    public static String runMatcher(String text) {
        String dateStr = text.replaceAll("r?n", " ");
        try {
            List matches = null;
            Pattern p = Pattern.compile(
                    "(\\d{1,4}[-|\\/]\\d{1,2}[-|\\/]\\d{1,2})",
                    Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
            // Pattern p =
            // Pattern.compile("(\\d{1,4}[-|\\/]\\d{1,2}[-|\\/]\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2})",
            // Pattern.CASE_INSENSITIVE|Pattern.MULTILINE);
            Matcher matcher = p.matcher(dateStr);
            if (matcher.find() && matcher.groupCount() >= 1) {
                matches = new ArrayList();
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    String temp = matcher.group(i);
                    matches.add(temp);
                }
            } else {
                matches = Collections.EMPTY_LIST;
            }

            if (matches.size() > 0) {
                return ((String) matches.get(0)).trim();
            } else {
                return "";
            }

        } catch (Exception e) {
            return "";
        }
    }

    public static String parseDate(long milliseconds) {
        String str = milliseconds + "";
        if (str.equals("暂无") || str.equals("")) {
            return "";
        }
        Date date = new Date(milliseconds);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm");
        String format = simpleDateFormat.format(date);
        return format;
    }

    public static String parseHour(String milliseconds) {
        if (milliseconds.equals("暂无")||milliseconds.equals("")) {
            return "";
        }
        long time = Long.parseLong(milliseconds) * 1000;
        Date date = new Date(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String format = simpleDateFormat.format(date);
        return format;
    }

    public static String parseMonth(String milliseconds) {
        if (milliseconds.equals("暂无")||milliseconds.equals("")) {
            return "";
        }
        long time = Long.parseLong(milliseconds) * 1000;
        Date date = new Date(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
        String format = simpleDateFormat.format(date);
        return format;
    }

    public static String parseDateDay(String milliseconds) {
        if (null == milliseconds || milliseconds.equals("暂无") || "".equals(milliseconds)) {
            return "";
        }
        long time = Long.parseLong(milliseconds) * 1000;
        Date date = new Date(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(date);
        return format;
    }

    public static String parseDateDayAndHour(String milliseconds) {
        if (null == milliseconds || milliseconds.equals("暂无") || "".equals(milliseconds)) {
            return "";
        }

        long time = Long.parseLong(milliseconds) * 1000;
        Date date = new Date(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String format = simpleDateFormat.format(date);
        return format;
    }

    public static String parseYearMonth(String milliseconds) {
        if (milliseconds.equals("暂无")||milliseconds.equals("")) {
            return "";
        }

        long time = Long.parseLong(milliseconds) * 1000;
        Date date = new Date(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        String format = simpleDateFormat.format(date);
        return format;
    }

    public static String parseMDHM(String milliseconds) {
        if (milliseconds.equals("暂无")||milliseconds.equals("")) {
            return "";
        }
        long time = Long.parseLong(milliseconds) * 1000;
        Date date = new Date(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
        String format = simpleDateFormat.format(date);
        return format;
    }

    public static String parseMDHMS(String milliseconds) {
        if (milliseconds.equals("暂无")||milliseconds.equals("")) {
            return "";
        }
        long time = Long.parseLong(milliseconds) * 1000;
        Date date = new Date(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(date);
        return format;
    }

    // string类型转换为long类型
    // strTime要转换的String类型的时间
    // formatType时间格式
    // strTime的时间格式和formatType的时间格式必须相同
    /**
     * string类型转换为long类型 strTime的时间格式和formatType的时间格式必须相同
     * 
     * @param strTime 2015-06-05
     * @param formatType yyyy-MM-dd yyyy-MM-dd HH:mm
     * @return
     * @throws ParseException
     * @throws java.text.ParseException
     */
    public static long stringToLong(String strTime, String formatType)
            throws ParseException, java.text.ParseException {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = date.getTime();// date类型转成long类型
            return currentTime / 1000;
        }
    }

    public static Date stringToDate(String strTime, String formatType)
            throws ParseException, java.text.ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    public static String getDayOfWeek(String date) {

        if (null == date || "".equals(date)) {
            return "";
        }

        String weekStr = "";
        try {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            calendar.setTime(formatter.parse(date));
            int dayOfweek = calendar.get(Calendar.DAY_OF_WEEK);

            dayOfweek = dayOfweek - 1;
            switch (dayOfweek) {
                case 0:
                    weekStr = "星期天";
                    break;
                case 1:
                    weekStr = "星期一";
                    break;
                case 2:
                    weekStr = "星期二";
                    break;
                case 3:
                    weekStr = "星期三";
                    break;
                case 4:
                    weekStr = "星期四";
                    break;
                case 5:
                    weekStr = "星期五";
                    break;
                case 6:
                    weekStr = "星期六";
                    break;

                default:
                    break;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return weekStr;
    }

    public static int getDayOfWeekNumber(String date) {
        int dayOfweek = 1;
        try {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            calendar.setTime(formatter.parse(date));
            dayOfweek = calendar.get(Calendar.DAY_OF_WEEK);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dayOfweek - 1;
    }

    public static int getDaysInMonth(int monthNumber, int year) {
        int days = 0;
        if (monthNumber >= 0 && monthNumber < 12) {
            try {
                Calendar calendar = Calendar.getInstance();
                int date = 1;
                calendar.set(year, monthNumber, date);
                days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            } catch (Exception e) {
                if (e != null)
                    e.printStackTrace();
            }
        }
        return days;
    }

    /**
     * @return yyyy-MM
     */
    public static String getCurrentDate111122() {
        SimpleDateFormat objSimpleDateFormat = new SimpleDateFormat("yyyy-MM");
        return objSimpleDateFormat.format(new Timestamp(System
                .currentTimeMillis()));
    }

    /**
     * 返回上个月的日期
     * 
     * @param date 日期格式
     * @param formatType 希望返回的格式 2015-06
     * @return 字符串 2015-06
     */
    public static String getLastMonthByCYYYYMM(Date date, String formatType) {

        // Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        Calendar lastDate = (Calendar) cal.clone();
        lastDate.add(Calendar.MONTH, -1);
        SimpleDateFormat sdf = new SimpleDateFormat(formatType);
        return sdf.format(lastDate.getTime());
    }

    /***
     * 返回上个月的日期
     * 
     * @param dateStr 字符串格式参数 比如 2015-06
     * @param formatType 希望返回的格式 2015-06
     * @return 字符串 2015-06
     */
    public static String getLastMonthByCYYYYMM(String dateStr, String formatType) {

        Date date = new Date();
        try {
            date = stringToDate(dateStr, formatType);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return getLastMonthByCYYYYMM(date, formatType);
    }

    /**
     * 返回下个月的日期
     * 
     * @param date 日期格式
     * @param formatType 希望返回的格式 2015-06
     * @return 字符串 2015-06
     */
    public static String getNextMonthByCYYYYMM(Date date, String formatType) {

        // Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        Calendar lastDate = (Calendar) cal.clone();
        lastDate.add(Calendar.MONTH, 1);
        SimpleDateFormat sdf = new SimpleDateFormat(formatType);
        return sdf.format(lastDate.getTime());
    }

    /**
     * 返回下个月的日期
     * 
     * @param dateStr 字符串格式参数 比如 2015-06
     * @param formatType 希望返回的格式 2015-06
     * @return 字符串 2015-06
     */
    public static String getNextMonthByCYYYYMM(String dateStr, String formatType) {

        Date date = new Date();
        try {
            date = stringToDate(dateStr, formatType);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return getNextMonthByCYYYYMM(date, formatType);
    }

    /**
     * 时间格式转换 2015-5 转换为 2015-05
     * 
     * @param dateStr 2015-5
     * @param formatType "yyyy-MM"
     * @return
     */
    public static String getString2formatType(String dateStr, String formatType) {

        Date date = new Date();
        try {
            date = stringToDate(dateStr, formatType);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        SimpleDateFormat sdf = new SimpleDateFormat(formatType);
        sdf.format(cal.getTime());
        return sdf.format(cal.getTime());
    }

    /**
     * 某年某月的天数 获取该月日数 比如 1月31天 2月 28天 或者 29天
     * 
     * @author peck 刘鹏程
     * @date 2015/7/17 17:17
     * @param year 2015
     * @param month 7 阳历
     * @return 比如 1月31天 2月 28天 或者 29天
     */
    public static int getDayAtYM(int year, int month) {
        int day = 30;
        boolean flag = false;
        switch (year % 4) {
            case 0:
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        switch (month) {
            case 1:
                day = 31;
                break;
            case 3:
                day = 31;
                break;
            case 5:
                day = 31;
                break;
            case 7:
                day = 31;
                break;
            case 8:
                day = 31;
                break;
            case 10:
                day = 31;
                break;
            case 12:
                day = 31;
                break;
            case 2:
                day = flag ? 29 : 28;
                break;
            default:
                day = 30;
                break;
        }
        return day;
    }

    // 比较时间
    public static boolean MacthTimeForSchedule(String time1, String time2) {
        if (time1.equals("") || time2.equals(""))
            return false;
        int starttime = Integer.parseInt(time1.split(":")[0]) * 100
                + Integer.parseInt(time1.split(":")[1]);
        int endtime = Integer.parseInt(time2.split(":")[0]) * 100
                + Integer.parseInt(time2.split(":")[1]);
        if (starttime >= endtime)
            return false;
        else
            return true;

    }

    // 获取当前月第一天
    public static String getMonthFirst() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
        String monthFirst = format.format(c.getTime());
        return monthFirst;
    }

    // 获取当前月最后一天
    public static String getMonthLast() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String monthLast = format.format(ca.getTime());
        return monthLast;
    }

    // 获取本周的星期一
    public static String getMonday() {
        String monday;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        int dayofweek = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayofweek == 0)
            dayofweek = 7;
        c.add(Calendar.DATE, -dayofweek + 1);
        monday = sdf.format(c.getTime());
        return monday;
    }

    // 获取本周的星期日
    public static String getSunday() {
        String sunday;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        int dayofweek = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (dayofweek == 0)
            dayofweek = 7;
        c.add(Calendar.DATE, -dayofweek + 7);
        sunday = sdf.format(c.getTime());
        return sunday;
    }

    // 判断是否为闰年
    public static boolean isLeapYear(int year) {
        if (year % 100 == 0 && year % 400 == 0) {
            return true;
        } else if (year % 100 != 0 && year % 4 == 0) {
            return true;
        }
        return false;
    }

    // 得到某月有多少天数
    public static int getDaysOfMonth(boolean isLeapyear, int month) {
        int daysOfMonth = 0;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                daysOfMonth = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                daysOfMonth = 30;
                break;
            case 2:
                if (isLeapyear) {
                    daysOfMonth = 29;
                } else {
                    daysOfMonth = 28;
                }
                break;
        }
        return daysOfMonth;
    }

    // 指定某年中的某月的第一天是星期几
    public static int getWeekdayOfMonth(int year, int month) {
        int dayOfWeek = 0;
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1);
        dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return dayOfWeek;
    }

    public static int getWeekDayOfLastMonth(int year, int month, int day) {
        int eachDayOfWeek = 0;
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day);
        eachDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return eachDayOfWeek;
    }
    public static String parseYMDLong(long milliseconds) {
        String str = milliseconds + "";
        if (str.equals("暂无") || str.equals("")) {
            return "";
        }
        Date date = new Date(milliseconds);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd");
        String format = simpleDateFormat.format(date);
        return format;
    }
    public static String parseMDLong(long milliseconds) {
        String str = milliseconds + "";
        if (str.equals("暂无") || str.equals("")) {
            return "";
        }
        Date date = new Date(milliseconds);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "MM-dd");
        String format = simpleDateFormat.format(date);
        return format;
    }
    public static String parseYLong(long milliseconds) {
        String str = milliseconds + "";
        if (str.equals("暂无") || str.equals("")) {
            return "";
        }
        Date date = new Date(milliseconds);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyy");
        String format = simpleDateFormat.format(date);
        return format;
    }
    public static String parseHMLong(long milliseconds) {
        String str = milliseconds + "";
        if (str.equals("暂无") || str.equals("")) {
            return "";
        }
        Date date = new Date(milliseconds);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "HH:mm");
        String format = simpleDateFormat.format(date);
        return format;
    }
}
