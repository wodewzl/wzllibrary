
package com.wuzhanglong.library.utils;

import android.annotation.SuppressLint;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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

    public static String getLastOrNextMonth(int i, String datetime) {

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
    public static Date getNextDay(Date date) {
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        gc.add(Calendar.DATE, 1);
        return new Date(gc.getTimeInMillis());
    }

    /**
     * 获得指定日期的上一天
     * 
     * @param date
     * @return
     */
    public static Date getPreDay(Date date) {
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        gc.add(Calendar.DATE, -1);
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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

    public static String parseHour(String milliseconds) {
        if (milliseconds.equals("暂无")) {
            return "";
        }
        long time = Long.parseLong(milliseconds) * 1000;
        Date date = new Date(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String format = simpleDateFormat.format(date);
        return format;
    }

    public static String parseMonth(String milliseconds) {
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


    public static String parseDateDayAndMin(String milliseconds) {
        if (null == milliseconds || milliseconds.equals("暂无") || "".equals(milliseconds)) {
            return "";
        }

        long time = Long.parseLong(milliseconds) * 1000;
        Date date = new Date(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(date);
        return format;
    }

    public static String parseYearMonth(String milliseconds) {
        if (milliseconds.equals("暂无")) {
            return "";
        }

        long time = Long.parseLong(milliseconds) * 1000;
        Date date = new Date(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        String format = simpleDateFormat.format(date);
        return format;
    }

    public static String parseMDHM(String milliseconds) {
        if (milliseconds.equals("暂无")) {
            return "";
        }
        long time = Long.parseLong(milliseconds) * 1000;
        Date date = new Date(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
        String format = simpleDateFormat.format(date);
        return format;
    }

    public static String parseMDHMS(String milliseconds) {
        if (milliseconds.equals("暂无")) {
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
     * @throws ParseException
     */
    public static long stringToLong(String strTime, String formatType)
            throws ParseException, ParseException {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = date.getTime();// date类型转成long类型
            return currentTime / 1000;
        }
    }

    public static Date stringToDate(String strTime, String formatType)
            throws ParseException, ParseException {
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
                    weekStr = "周七";
                    break;
                case 1:
                    weekStr = "周一";
                    break;
                case 2:
                    weekStr = "周二";
                    break;
                case 3:
                    weekStr = "周三";
                    break;
                case 4:
                    weekStr = "周四";
                    break;
                case 5:
                    weekStr = "周五";
                    break;
                case 6:
                    weekStr = "周六";
                    break;

                default:
                    break;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return weekStr;
    }

    public static String getDayOfWeek(Date date) {
        String weekStr = "";
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int dayOfweek = calendar.get(Calendar.DAY_OF_WEEK);

            dayOfweek = dayOfweek - 1;
            switch (dayOfweek) {
                case 0:
                    weekStr = "周七";
                    break;
                case 1:
                    weekStr = "周一";
                    break;
                case 2:
                    weekStr = "周二";
                    break;
                case 3:
                    weekStr = "周三";
                    break;
                case 4:
                    weekStr = "周四";
                    break;
                case 5:
                    weekStr = "周五";
                    break;
                case 6:
                    weekStr = "周六";
                    break;

                default:
                    break;
            }

        } catch (Exception e) {
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

    /**
     * 将时间戳转为代表"距现在多久之前"的字符串
     * 
     * @param timeStr 时间戳
     * @return
     */
    public static String getStandardDate(String timeStr) {

        StringBuffer sb = new StringBuffer();

        long t = Long.parseLong(timeStr);
        long time = System.currentTimeMillis() - (t * 1000);
        long mill = (long) Math.ceil(time / 1000);// 秒前

        long minute = (long) Math.ceil(time / 60 / 1000.0f);// 分钟前

        long hour = (long) Math.ceil(time / 60 / 60 / 1000.0f);// 小时

        long day = (long) Math.ceil(time / 24 / 60 / 60 / 1000.0f);// 天前

        if (day - 1 > 0) {
            sb.append(day + "天");
        } else if (hour - 1 > 0) {
            if (hour >= 24) {
                sb.append("1天");
            } else {
                sb.append(hour + "小时");
            }
        } else if (minute - 1 > 0) {
            if (minute == 60) {
                sb.append("1小时");
            } else {
                sb.append(minute + "分钟");
            }
        } else if (mill - 1 > 0) {
            if (mill == 60) {
                sb.append("1分钟");
            } else {
                sb.append(mill + "秒");
            }
        } else {
            sb.append("刚刚");
        }
        if (!sb.toString().equals("刚刚")) {
            sb.append("前");
        }
        return sb.toString();
    }

    /**
     * 得到几天前的时间
     * 
     * @param d
     * @param day
     * @return
     */
    public static Date getDateBefore(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
        return now.getTime();
    }

    /**
     * 得到几天后的时间
     * 
     * @param d
     * @param day
     * @return
     */
    public static Date getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

    // 把日期转为字符串
    public static String ConverToString(Date date)
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    // 把字符串转为日期
    public static Date ConverToDate(String strDate) throws Exception
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.parse(strDate);
    }

    public static int isYesterday(String date) {

        int day = 0;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {

            Date d1 = new Date();// 当前时间

            Date d2 = sdf.parse(date);// 传进的时间

            long cha = d2.getTime() - d1.getTime();

            day = new Long(cha / (1000 * 60 * 60 * 24)).intValue();

        } catch (ParseException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        }

        return day;

    }
}
