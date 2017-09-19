
package com.bs.bsims.time;

import android.content.Context;
import android.view.View;

import com.bs.bsims.R;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.DateUtils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class WheelMain {

    private View view;
    private WheelView wv_year;
    private WheelView wv_month;
    private WheelView wv_day;
    private WheelView wv_hours;
    private WheelView wv_mins;
    private WheelView wv_year_half;
    public int screenheight;
    private boolean hasSelectTime;
    private boolean showDay = true;
    private boolean onlyTime = false;
    private static int START_YEAR = 1950, END_YEAR = 2200;
    private int mStatus = 0;// 0全部 1年月日 2为年月 3月日 4 时分
    /**
     * 年份改变后的值 2015/7/22 12:19
     * 
     * @author peck 刘鹏程
     */
    private int afterChangedYear;
    /**
     * 月份改变后的值 2015/7/22 12:19
     * 
     * @author peck 刘鹏程
     */
    private int afterChangedMonth;

    /**
     * 显示到小时 在审批中的需求 比如请假时长 只到小时不到分钟
     * 
     * @author peck 刘鹏程
     * @since 2015/7/23 16:35
     */
    private boolean hasShowHour;

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public static int getSTART_YEAR() {
        return START_YEAR;
    }

    public static void setSTART_YEAR(int sTART_YEAR) {
        START_YEAR = sTART_YEAR;
    }

    public static int getEND_YEAR() {
        return END_YEAR;
    }

    public static void setEND_YEAR(int eND_YEAR) {
        END_YEAR = eND_YEAR;
    }

    public WheelMain(View view) {
        super();
        this.view = view;
        hasSelectTime = true;
        setView(view);
    }

    public WheelMain(View view, boolean hasSelectTime) {
        super();
        this.view = view;
        this.hasSelectTime = hasSelectTime;
        setView(view);
    }

    public WheelMain(View view, boolean hasSelectTime, boolean showDay) {
        super();
        this.view = view;
        this.hasSelectTime = hasSelectTime;
        this.showDay = showDay;
        setView(view);
    }

    public void initDateTimePicker(int year, int month, int day) {
        this.hasSelectTime = false;
        this.initDateTimePicker(year, month, day, 0, 0);
    }

    public void initDateTimePicker(int year, int month, int day, int hour) {
        this.hasSelectTime = false;
        this.initDateTimePicker(year, month, day, hour, 0);
    }

    public void initDateTimePicker(int year, int month) {
        this.initDateTimePicker(year, month, 0, 0, 0);
    }

    public void initDateTimePicker(int hour, int mintue, boolean onlyTime) {
        this.onlyTime = onlyTime;
        this.initDateTimePicker(0, 0, 0, hour, mintue);

    }

    /**
     * @Description: TODO 弹出日期时间选择器
     */
    public void initDateTimePicker(int year, String half_year, int month, int day, int h, int m) {
        // int year = calendar.get(Calendar.YEAR);
        // int month = calendar.get(Calendar.MONTH);
        // int day = calendar.get(Calendar.DATE);
        // 添加大小月月份并将其转换为list,方便之后的判断
        String[] months_big = {
                "1", "3", "5", "7", "8", "10", "12"
        };
        String[] months_little = {
                "4", "6", "9", "11"
        };

        final List<String> list_big = Arrays.asList(months_big);
        final List<String> list_little = Arrays.asList(months_little);
        showDay = false;

        // 年
        wv_year = (WheelView) view.findViewById(R.id.year);
        wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
        wv_year.setCyclic(false);// 可循环滚动
        wv_year.setLabel("年");// 添加文字
        wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据
        wv_year.setVisibleItems(3);

        // //上半年、下半年
        // wv_year_half=(WheelView) view.findViewById(R.id.half_year);
        // String [] half = new String[]{"上","下"};
        // wv_year_half.setAdapter(new ArrayWheelAdapter(half,4));// 设置"年"的显示数据
        // wv_year_half.setCyclic(true);// 可循环滚动
        // wv_year_half.setLabel("半年");// 添加文字
        // wv_year_half.setCurrentItem(1);// 初始化时显示的数据

        // 月
        wv_month = (WheelView) view.findViewById(R.id.month);
        // wv_month.setAdapter(new NumericWheelAdapter(1, 12));
        // wv_month.setCyclic(true);
        // wv_month.setLabel("月");
        // wv_month.setCurrentItem(month);

        // 上半年、下半年
        String[] half = new String[] {
                "上", "下"
        };
        wv_month.setAdapter(new ArrayWheelAdapter(half, 2));// 设置"年"的显示数据
        wv_month.setCyclic(false);// 不可循环滚动
        wv_month.setLabel("半年");// 添加文字
        wv_month.setCurrentItem(0);// 初始化时显示的数据
        wv_month.setVisibleItems(3);

        // 日
        wv_day = (WheelView) view.findViewById(R.id.day);
        if (showDay) {

            wv_day.setCyclic(true);
            // 判断大小月及是否闰年,用来确定"日"的数据
            if (list_big.contains(String.valueOf(month + 1))) {
                wv_day.setAdapter(new NumericWheelAdapter(1, 31));
            } else if (list_little.contains(String.valueOf(month + 1))) {
                wv_day.setAdapter(new NumericWheelAdapter(1, 30));
            } else {
                // 闰年
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
                    wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                else
                    wv_day.setAdapter(new NumericWheelAdapter(1, 28));
            }
            wv_day.setLabel("日");
            wv_day.setCurrentItem(day - 1);
        } else {
            wv_day.setVisibility(View.GONE);
        }

        if (onlyTime) {
            wv_year.setVisibility(View.GONE);
            wv_month.setVisibility(View.GONE);
            wv_day.setVisibility(View.GONE);

        }

        wv_hours = (WheelView) view.findViewById(R.id.hour);
        wv_mins = (WheelView) view.findViewById(R.id.min);

        if (hasSelectTime) {
            wv_hours.setVisibility(View.VISIBLE);
            wv_mins.setVisibility(View.VISIBLE);

            wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
            wv_hours.setCyclic(true);// 可循环滚动
            wv_hours.setLabel("时");// 添加文字
            wv_hours.setCurrentItem(h);

            wv_mins.setAdapter(new NumericWheelAdapter(0, 59));
            wv_mins.setCyclic(true);// 可循环滚动
            wv_mins.setLabel("分");// 添加文字
            wv_mins.setCurrentItem(m);
        } else {
            wv_hours.setVisibility(View.GONE);
            wv_mins.setVisibility(View.GONE);
        }

        // 添加"年"监听
        OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
            }
        };
        // 添加"月"监听
        OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
            }
        };
        // wv_year.addChangingListener(wheelListener_year);
        wv_month.addChangingListener(wheelListener_month);

        // 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
        int textSize = 0;
        if (hasSelectTime)
            textSize = (screenheight / 100) * 2;
        else
            textSize = (screenheight / 100) * 3;
        wv_day.TEXT_SIZE = textSize;
        wv_month.TEXT_SIZE = textSize;
        wv_year.TEXT_SIZE = textSize;
        wv_hours.TEXT_SIZE = textSize;
        wv_mins.TEXT_SIZE = textSize;

        wv_day.setVisibility(View.GONE);
        wv_hours.setVisibility(View.GONE);
        wv_mins.setVisibility(View.GONE);

    }

    /**
     * 只能选择当前年 年份可以滑动，但是对年份进行了限制
     * 
     * @author peck 刘鹏程
     * @since 2015/7/22 12:19
     * @param year
     * @param half_year
     * @param month
     * @param day
     * @param h
     * @param m
     * @param context
     */
    public void initDateTimePickerOneY(final int year, String half_year, int month, int day, int h, int m, final Context context) {
        // 添加大小月月份并将其转换为list,方便之后的判断
        String[] months_big = {
                "1", "3", "5", "7", "8", "10", "12"
        };
        String[] months_little = {
                "4", "6", "9", "11"
        };

        final List<String> list_big = Arrays.asList(months_big);
        final List<String> list_little = Arrays.asList(months_little);
        showDay = false;

        // 年
        wv_year = (WheelView) view.findViewById(R.id.year);
        wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
        // wv_year.setCyclic(false);// 可循环滚动
        wv_year.setCyclic(true);// 可循环滚动
        wv_year.setLabel("年");// 添加文字
        wv_year.setScroller(true);
        wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据
        wv_year.setVisibleItems(3);

        // //上半年、下半年

        // 月
        wv_month = (WheelView) view.findViewById(R.id.month);

        // 上半年、下半年
        String[] half = new String[] {
                "上", "下"
        };
        wv_month.setAdapter(new ArrayWheelAdapter(half, 2));// 设置"年"的显示数据
        // wv_month.setCyclic(false);// 不可循环滚动
        wv_month.setCyclic(true);// 不可循环滚动
        wv_month.setLabel("半年");// 添加文字
        wv_month.setCurrentItem(0);// 初始化时显示的数据
        wv_month.setVisibleItems(3);

        // 日
        wv_day = (WheelView) view.findViewById(R.id.day);
        if (showDay) {

            wv_day.setCyclic(true);
            // 判断大小月及是否闰年,用来确定"日"的数据
            if (list_big.contains(String.valueOf(month + 1))) {
                wv_day.setAdapter(new NumericWheelAdapter(1, 31));
            } else if (list_little.contains(String.valueOf(month + 1))) {
                wv_day.setAdapter(new NumericWheelAdapter(1, 30));
            } else {
                // 闰年
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
                    wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                else
                    wv_day.setAdapter(new NumericWheelAdapter(1, 28));
            }
            wv_day.setLabel("日");
            wv_day.setCurrentItem(day - 1);
        } else {
            wv_day.setVisibility(View.GONE);
        }

        if (onlyTime) {
            wv_year.setVisibility(View.GONE);
            wv_month.setVisibility(View.GONE);
            wv_day.setVisibility(View.GONE);

        }

        wv_hours = (WheelView) view.findViewById(R.id.hour);
        wv_mins = (WheelView) view.findViewById(R.id.min);

        if (hasSelectTime) {
            wv_hours.setVisibility(View.VISIBLE);
            wv_mins.setVisibility(View.VISIBLE);

            wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
            wv_hours.setCyclic(true);// 可循环滚动
            wv_hours.setLabel("时");// 添加文字
            wv_hours.setCurrentItem(h);

            wv_mins.setAdapter(new NumericWheelAdapter(0, 59));
            wv_mins.setCyclic(true);// 可循环滚动
            wv_mins.setLabel("分");// 添加文字
            wv_mins.setCurrentItem(m);
        } else {
            wv_hours.setVisibility(View.GONE);
            wv_mins.setVisibility(View.GONE);
        }

        // 添加"年"监听
        OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int year_num = newValue + START_YEAR;
                if (year != year_num) {
                    year_num = year;
                    wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据
                    CustomToast.showShortToast(context, "现在支持当前年份的查询哦，亲！");
                }
            }
        };

        // 添加"月"监听
        OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
            }
        };
        wv_year.addChangingListener(wheelListener_year);
        wv_month.addChangingListener(wheelListener_month);

        // 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
        int textSize = 0;
        if (hasSelectTime)
            textSize = (screenheight / 100) * 2;
        else
            textSize = (screenheight / 100) * 3;
        wv_day.TEXT_SIZE = textSize;
        wv_month.TEXT_SIZE = textSize;
        wv_year.TEXT_SIZE = textSize;
        wv_hours.TEXT_SIZE = textSize;
        wv_mins.TEXT_SIZE = textSize;

        wv_day.setVisibility(View.GONE);
        wv_hours.setVisibility(View.GONE);
        wv_mins.setVisibility(View.GONE);

    }

    /**
     * @Description: TODO 弹出日期时间选择器 带第一季度，第二季度，第三季度，第四季度
     */

    public void initDateTimePickerWithJD(int year, int month, int day, int h, int m) {

        // 年
        wv_year = (WheelView) view.findViewById(R.id.year);
        wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
        wv_year.setCyclic(true);// 可循环滚动
        wv_year.setLabel("年");// 添加文字
        wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据

        // 月
        wv_month = (WheelView) view.findViewById(R.id.month);
        // wv_month.setAdapter(new NumericWheelAdapter(1, 12));
        // wv_month.setCyclic(true);
        // wv_month.setLabel("月");
        // wv_month.setCurrentItem(month);
        //

        // 上半年、下半年
        String[] half = new String[] {
                "1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", "第一季度", "第二季度", "第三季度", "第四季度"
        };
        wv_month.setAdapter(new ArrayWheelAdapter(half, 16));// 设置"年"的显示数据
        wv_month.setCyclic(true);// 不可循环滚动
        // wv_month.setLabel("月");// 添加文字
        wv_month.setCurrentItem(DateUtils.getCurrentMonth());// 初始化时显示的数据
        wv_month.setVisibleItems(5);

        // 日
        wv_day = (WheelView) view.findViewById(R.id.day);
        wv_hours = (WheelView) view.findViewById(R.id.hour);
        wv_mins = (WheelView) view.findViewById(R.id.min);

        if (year == 0)
            wv_year.setVisibility(View.GONE);
        else
            wv_year.setVisibility(View.VISIBLE);
        if (month == 0)
            wv_month.setVisibility(View.GONE);
        else
            wv_month.setVisibility(View.VISIBLE);
        if (day == 0)
            wv_day.setVisibility(View.GONE);
        else
            wv_day.setVisibility(View.VISIBLE);
        if (h == 0)
            wv_hours.setVisibility(View.GONE);
        else
            wv_hours.setVisibility(View.VISIBLE);
        if (m == 0)
            wv_mins.setVisibility(View.GONE);
        else
            wv_mins.setVisibility(View.VISIBLE);
        //
        // // 添加"年"监听
        // OnWheelChangedListener wheelListener_year = new
        // OnWheelChangedListener() {
        // public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // // int year_num = newValue + START_YEAR;
        // // 判断大小月及是否闰年,用来确定"日"的数据
        // // if (list_big.contains(String.valueOf(wv_month.getCurrentItem() +
        // 1))) {
        // // wv_day.setAdapter(new NumericWheelAdapter(1, 31));
        // // } else if
        // (list_little.contains(String.valueOf(wv_month.getCurrentItem() + 1)))
        // {
        // // wv_day.setAdapter(new NumericWheelAdapter(1, 30));
        // // } else {
        // // if ((year_num % 4 == 0 && year_num % 100 != 0) || year_num % 400
        // == 0)
        // // wv_day.setAdapter(new NumericWheelAdapter(1, 29));
        // // else
        // // wv_day.setAdapter(new NumericWheelAdapter(1, 28));
        // // }
        // }
        // };
        // 添加"月"监听
        // OnWheelChangedListener wheelListener_month = new
        // OnWheelChangedListener() {
        // public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // int month_num = newValue + 1;
        // // 判断大小月及是否闰年,用来确定"日"的数据
        // if (list_big.contains(String.valueOf(month_num))) {
        // wv_day.setAdapter(new NumericWheelAdapter(1, 31));
        // } else if (list_little.contains(String.valueOf(month_num))) {
        // wv_day.setAdapter(new NumericWheelAdapter(1, 30));
        // } else {
        // if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 &&
        // (wv_year.getCurrentItem() + START_YEAR) % 100 != 0)
        // || (wv_year.getCurrentItem() + START_YEAR) % 400 == 0)
        // wv_day.setAdapter(new NumericWheelAdapter(1, 29));
        // else
        // wv_day.setAdapter(new NumericWheelAdapter(1, 28));
        // }
        // }
        // };
        // wv_year.addChangingListener(wheelListener_year);
        // wv_month.addChangingListener(wheelListener_month);

        // 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
        int textSize = 0;
        if (hasSelectTime)
            textSize = (screenheight / 100) * 2;
        else
            textSize = (screenheight / 100) * 2;
        wv_day.TEXT_SIZE = textSize;
        wv_month.TEXT_SIZE = textSize;
        wv_year.TEXT_SIZE = textSize;
        wv_hours.TEXT_SIZE = textSize;
        wv_mins.TEXT_SIZE = textSize;

    }

    /**
     * 只显示年月 并且最大值 只能是当前年 当前月
     * 
     * @param year
     * @param month
     * @author peck 刘鹏程
     * @since 2015/7/22 12:19
     */
    public void initDateTimePickerMaxCMonth(final int year, final int month, final Context context) {
        // 添加大小月月份并将其转换为list,方便之后的判断
        String[] months_big = {
                "1", "3", "5", "7", "8", "10", "12"
        };
        String[] months_little = {
                "4", "6", "9", "11"
        };

        // 年
        wv_year = (WheelView) view.findViewById(R.id.year);
        wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, year));// 设置"年"的显示数据
        wv_year.setCyclic(true);// 可循环滚动
        wv_year.setLabel("年");// 添加文字
        wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据

        // 月
        wv_month = (WheelView) view.findViewById(R.id.month);
        wv_month.setAdapter(new NumericWheelAdapter(1, 12));
        wv_month.setCyclic(true);
        wv_month.setLabel("月");
        wv_month.setCurrentItem(month - 1);

        if (onlyTime) {
            wv_year.setVisibility(View.GONE);
            wv_month.setVisibility(View.GONE);
            wv_day.setVisibility(View.GONE);
        }
        wv_day = (WheelView) view.findViewById(R.id.day);
        wv_hours = (WheelView) view.findViewById(R.id.hour);
        wv_mins = (WheelView) view.findViewById(R.id.min);

        wv_day.setVisibility(View.GONE);
        wv_hours.setVisibility(View.GONE);
        wv_mins.setVisibility(View.GONE);

        afterChangedYear = year;
        afterChangedMonth = month;

        // 添加"年"监听
        OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int year_num = newValue + START_YEAR;
                afterChangedYear = year_num;
                if (year == afterChangedYear) {
                    if (month < afterChangedMonth) {
                        wv_month.setCurrentItem(month - 1);
                        CustomToast.showShortToast(context, "现在支持最多到上个月的查询哦，亲！");
                    }
                }
            }
        };
        // 添加"月"监听
        OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int month_num = newValue + 1;
                afterChangedMonth = month_num;
                if (month < month_num) {
                    if (year == afterChangedYear) {
                        wv_month.setCurrentItem(month - 1);
                        CustomToast.showShortToast(context, "现在支持最多到上个月的查询哦，亲！");
                    }
                }
            }
        };
        wv_year.addChangingListener(wheelListener_year);
        wv_month.addChangingListener(wheelListener_month);

        // 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
        int textSize = 0;
        if (hasSelectTime)
            textSize = (screenheight / 100) * 2;
        else
            textSize = (screenheight / 100) * 3;
        wv_month.TEXT_SIZE = textSize;
        wv_year.TEXT_SIZE = textSize;

    }

    /**
     * @Description: TODO 弹出日期时间选择器
     */
    public void initDateTimePicker(int year, int month, int day, int h, int m) {
        String[] months_big = {
                "1", "3", "5", "7", "8", "10", "12"
        };
        String[] months_little = {
                "4", "6", "9", "11"
        };

        final List<String> list_big = Arrays.asList(months_big);
        final List<String> list_little = Arrays.asList(months_little);

        // 年
        wv_year = (WheelView) view.findViewById(R.id.year);
        wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
        wv_year.setCyclic(true);// 可循环滚动
        wv_year.setLabel("年");// 添加文字
        wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据

        // 月
        wv_month = (WheelView) view.findViewById(R.id.month);
        wv_month.setAdapter(new NumericWheelAdapter(1, 12));
        wv_month.setCyclic(true);
        wv_month.setLabel("月");
        wv_month.setCurrentItem(month);

        // 日
        wv_day = (WheelView) view.findViewById(R.id.day);
        if (showDay) {

            wv_day.setCyclic(true);
            // 判断大小月及是否闰年,用来确定"日"的数据
            if (list_big.contains(String.valueOf(month + 1))) {
                wv_day.setAdapter(new NumericWheelAdapter(1, 31));
            } else if (list_little.contains(String.valueOf(month + 1))) {
                wv_day.setAdapter(new NumericWheelAdapter(1, 30));
            } else {
                // 闰年
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
                    wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                else
                    wv_day.setAdapter(new NumericWheelAdapter(1, 28));
            }
            wv_day.setLabel("日");
            wv_day.setCurrentItem(day - 1);
        } else {
            wv_day.setVisibility(View.GONE);
        }

        if (onlyTime) {
            wv_year.setVisibility(View.GONE);
            wv_month.setVisibility(View.GONE);
            wv_day.setVisibility(View.GONE);

        }

        wv_hours = (WheelView) view.findViewById(R.id.hour);
        wv_mins = (WheelView) view.findViewById(R.id.min);
        if (hasSelectTime) {
            wv_hours.setVisibility(View.VISIBLE);
            wv_mins.setVisibility(View.VISIBLE);

            wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
            wv_hours.setCyclic(true);// 可循环滚动
            wv_hours.setLabel("时");// 添加文字
            wv_hours.setCurrentItem(h);

            wv_mins.setAdapter(new NumericWheelAdapter(0, 59));
            wv_mins.setCyclic(true);// 可循环滚动
            wv_mins.setLabel("分");// 添加文字
            wv_mins.setCurrentItem(m);
        } else {
            wv_hours.setVisibility(View.GONE);
            wv_mins.setVisibility(View.GONE);
        }

        if (hasShowHour) {
            wv_hours.setVisibility(View.VISIBLE);
            wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
            wv_hours.setCyclic(true);// 可循环滚动
            wv_hours.setLabel("时");// 添加文字
            wv_hours.setCurrentItem(h);
            wv_mins.setVisibility(View.GONE);
        }

        if (year == 0)
            wv_year.setVisibility(View.GONE);
        if (month == 0)
            wv_month.setVisibility(View.GONE);
        if (day == 0)
            wv_day.setVisibility(View.GONE);
        if (h == 0)
            wv_hours.setVisibility(View.GONE);
        if (m == 0)
            wv_mins.setVisibility(View.GONE);

        // 一月份导致月份无法显示
        Calendar calendar = Calendar.getInstance();
        int month1 = calendar.get(Calendar.MONTH);
        if (month1 == month && month1 == 0) {
            wv_month.setVisibility(View.VISIBLE);
        }

        // 添加"年"监听
        OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int year_num = newValue + START_YEAR;
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (list_big.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 31));
                } else if (list_little.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 30));
                } else {
                    if ((year_num % 4 == 0 && year_num % 100 != 0) || year_num % 400 == 0)
                        wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                    else
                        wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                }
            }
        };
        // 添加"月"监听
        OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int month_num = newValue + 1;
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (list_big.contains(String.valueOf(month_num))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 31));
                } else if (list_little.contains(String.valueOf(month_num))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 30));
                } else {
                    if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year.getCurrentItem() + START_YEAR) % 100 != 0)
                            || (wv_year.getCurrentItem() + START_YEAR) % 400 == 0)
                        wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                    else
                        wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                }
            }
        };
        wv_year.addChangingListener(wheelListener_year);
        wv_month.addChangingListener(wheelListener_month);

        // 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
        int textSize = 0;
        if (hasSelectTime)
            textSize = (screenheight / 100) * 2;
        else
            textSize = (screenheight / 100) * 2;
        wv_day.TEXT_SIZE = textSize;
        wv_month.TEXT_SIZE = textSize;
        wv_year.TEXT_SIZE = textSize;
        wv_hours.TEXT_SIZE = textSize;
        wv_mins.TEXT_SIZE = textSize;

    }

    public String getTime() {
        StringBuffer sb = new StringBuffer();
        String month = (wv_month.getCurrentItem() + 1) + "";
        if (month.length() == 1) {
            month = "0" + month;
        }
        String day = (wv_day.getCurrentItem() + 1) + "";

        if (day.length() == 1) {
            day = "0" + day;
        }

        String hour = wv_hours.getCurrentItem() + "";
        if (hour.length() == 1) {
            hour = "0" + hour;
        }
        String min = wv_mins.getCurrentItem() + "";

        if (min.length() == 1) {
            min = "0" + min;
        }

        // if (!hasSelectTime) {
        // sb.append((wv_year.getCurrentItem() + START_YEAR)).append("-")
        // .append(month).append("-")
        // .append(day);
        // } else {
        // sb.append((wv_year.getCurrentItem() + START_YEAR)).append("-")
        //
        // .append(month).append("-")
        // .append(day).append(" ")
        // .append(hour).append(":")
        // .append(min);
        // }
        //
        // if (wv_year.getVisibility() == View.VISIBLE &&
        // wv_month.getVisibility() == View.VISIBLE
        // && wv_day.getVisibility() == View.VISIBLE
        // && wv_hours.getVisibility() == View.VISIBLE &&
        // wv_mins.getVisibility() == View.VISIBLE) {
        //
        // sb.append((wv_year.getCurrentItem() + START_YEAR)).append("-")
        // .append((month)).append("-")
        // .append((day)).append(" ")
        // .append((hour)).append("：")
        // .append((min));
        // }

        if (wv_year.getVisibility() == View.VISIBLE && wv_month.getVisibility() == View.VISIBLE && wv_day.getVisibility() == View.VISIBLE
                && wv_hours.getVisibility() == View.VISIBLE && wv_mins.getVisibility() == View.VISIBLE) {
            sb.append((wv_year.getCurrentItem() + START_YEAR)).append("-").append((month)).append("-").append((day)).append(" ").append((hour)).append(":").append((min));
            return sb.toString();
        }

        if (wv_year.getVisibility() == View.VISIBLE && wv_month.getVisibility() == View.VISIBLE && wv_day.getVisibility() == View.VISIBLE
                && wv_hours.getVisibility() == View.VISIBLE) {
            sb.append((wv_year.getCurrentItem() + START_YEAR)).append("-").append((month)).append("-").append((day)).append(" ").append((hour)).append(":").append(("00"));
            return sb.toString();
        }

        if (wv_year.getVisibility() == View.VISIBLE && wv_month.getVisibility() == View.VISIBLE && wv_day.getVisibility() == View.VISIBLE) {
            sb.append((wv_year.getCurrentItem() + START_YEAR)).append("-").append((month)).append("-").append((day));
            return sb.toString();
        }

        if (wv_year.getVisibility() == View.VISIBLE && wv_month.getVisibility() == View.VISIBLE) {
            sb.append((wv_year.getCurrentItem() + START_YEAR)).append("-").append((month));
            return sb.toString();
        }

        if (wv_month.getVisibility() == View.VISIBLE && wv_day.getVisibility() == View.VISIBLE) {
            sb.append((month)).append("-").append((day));
            return sb.toString();
        }
        if (wv_hours.getVisibility() == View.VISIBLE && wv_mins.getVisibility() == View.VISIBLE) {
            sb.append((hour)).append("：").append((min));
            return sb.toString();
        }
        return sb.toString();
    }

    public String getOnlyTime() {
        StringBuffer sb = new StringBuffer();

        String hour = wv_hours.getCurrentItem() + "";
        if (hour.length() == 1) {
            hour = "0" + hour;
        }
        String min = wv_mins.getCurrentItem() + "";

        if (min.length() == 1) {
            min = "0" + min;
        }

        sb.append(hour).append(":").append(min);
        return sb.toString();
    }

    public String getDate() {
        StringBuffer sb = new StringBuffer();
        String month = (wv_month.getCurrentItem() + 1) + "";
        if (month.length() == 1) {
            month = "0" + month;
        }
        String day = (wv_day.getCurrentItem() + 1) + "";

        if (day.length() == 1) {
            day = "0" + day;
        }

        if (!hasSelectTime) {
            sb.append((wv_year.getCurrentItem() + START_YEAR)).append("-").append(month).append("-").append(day);
        }
        return sb.toString();
    }

    public String getYearAndMonth() {
        StringBuffer sb = new StringBuffer();
        String month = (wv_month.getCurrentItem() + 1) + "";
        if (month.length() == 1) {
            month = "0" + month;
        }

        if (!hasSelectTime) {
            sb.append((wv_year.getCurrentItem() + START_YEAR)).append("-").append(month);
        }
        return sb.toString();
    }

    public WheelView getWv_year() {
        return wv_year;
    }

    public void setWv_year(WheelView wv_year) {
        this.wv_year = wv_year;
    }

    public String getHalfYear() {
        StringBuffer sb = new StringBuffer();
        sb.append((wv_year.getCurrentItem() + START_YEAR)).append("-").append(wv_month.getCurrentItem());
        return sb.toString();
    }

    /**
     * 显示到小时 在审批中的需求
     * 
     * @param year
     * @param month
     * @param day
     * @param hour
     * @author peck 刘鹏程
     * @since 2015/7/23 16:35
     */
    public void initDateTimePickerEndHour(int year, int month, int day, int hour) {
        this.hasShowHour = true;
        this.initDateTimePicker(year, month, day, hour, 0);
    }
}
