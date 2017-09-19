
package com.bs.bsims.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.model.JournalListVO1;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.List;

@SuppressLint("NewApi")
public class DateAdapter extends BaseAdapter {
    private static String TAG = "ZzL";
    private boolean isLeapyear = false;
    private int daysOfMonth = 0;
    private int dayOfWeek = 0;
    private int nextDayOfWeek = 0;
    private int lastDayOfWeek = 0;
    private int lastDaysOfMonth = 0;
    private int eachDayOfWeek = 0;
    private Context context;
    private Resources res = null;
    private Drawable drawable = null;
    private String[] dayNumber = new String[7];
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
    private int currentFlag = -1; //
    private String sysDate = "";
    private String sys_year = "";
    private String sys_month = "";
    private String sys_day = "";
    private String currentYear = "";
    private String currentMonth = "";
    private String currentWeek = "";
    private String currentDay = "";
    private int weeksOfMonth;
    private int default_postion;
    private int clickTemp = -1;
    private int week_num = 0;
    private int week_c = 0;
    private int month = 0;
    private int jumpWeek = 0;
    private int c_month = 0;
    private int c_day_week = 0;
    private int n_day_week = 0;
    private boolean isStart;
    private List<JournalListVO1> logs;
    private String[] logDay;
    private String[] preLogDay;
    private List<JournalListVO1> preLogs;
    private boolean isFisrt = false;
    private int preMonth;

    // ��ʶѡ���Item
    public void setSeclection(int position) {
        clickTemp = position;
    }

    public int getSeclection() {
        return clickTemp;
    }

    public DateAdapter(Context context) {
        this.context = context;
    }

    public void refreshData(int year_c, int month_c, int day_c,
            int week_c, int week_num, int default_postion, boolean isStart) {
        this.default_postion = default_postion;
        this.week_c = week_c;
        this.isStart = isStart;

        lastDayOfWeek = DateUtils.getWeekDayOfLastMonth(year_c, month_c,
                DateUtils.getDaysOfMonth(DateUtils.isLeapYear(year_c), month_c));
        Log.i(TAG, "week_c:" + week_c);
        currentYear = String.valueOf(year_c);
        currentMonth = String.valueOf(month_c);
        currentDay = String.valueOf(day_c);
        getCalendar(Integer.parseInt(currentYear),
                Integer.parseInt(currentMonth));
        currentWeek = String.valueOf(week_c);
        getWeek(Integer.parseInt(currentYear), Integer.parseInt(currentMonth),
                Integer.parseInt(currentWeek));

        sys_year = currentYear;
        sys_month = currentMonth;
        sys_day = currentDay;
        month = month_c;
    }

    public int getTodayPosition() {
        int todayWeek = 0;

        if (Integer.parseInt(sys_day) > Integer.parseInt(dayNumber[dayNumber.length - 1])) {
            todayWeek = DateUtils.getWeekDayOfLastMonth(Integer.parseInt(sys_year),
                    Integer.parseInt(sys_month) - 1, Integer.parseInt(sys_day));
        } else {
            todayWeek = DateUtils.getWeekDayOfLastMonth(Integer.parseInt(sys_year),
                    Integer.parseInt(sys_month), Integer.parseInt(sys_day));
        }

        if (todayWeek == 7) {
            clickTemp = 0;
        } else {
            clickTemp = todayWeek;
        }
        return clickTemp;
    }

    public void setTodayPosition() {

    }

    public int getCurrentMonth(int position) {
        int thisDayOfWeek = DateUtils.getWeekdayOfMonth(Integer.parseInt(currentYear), Integer.parseInt(currentMonth));
        if (isStart) {
            if (thisDayOfWeek != 7) {
                if (position < thisDayOfWeek) {
                    return Integer.parseInt(currentMonth) - 1 == 0 ? 12
                            : Integer.parseInt(currentMonth) - 1;
                } else {
                    return Integer.parseInt(currentMonth);
                }
            } else {
                return Integer.parseInt(currentMonth);
            }
        } else {
            return Integer.parseInt(currentMonth);
        }
    }

    public int getCurrentYear(int position) {
        int thisDayOfWeek = DateUtils.getWeekdayOfMonth(Integer.parseInt(currentYear),
                Integer.parseInt(currentMonth));
        if (isStart) {
            if (thisDayOfWeek != 7) {
                if (position < thisDayOfWeek) {
                    return Integer.parseInt(currentMonth) - 1 == 0 ? Integer
                            .parseInt(currentYear) - 1 : Integer
                            .parseInt(currentYear);
                } else {
                    return Integer.parseInt(currentYear);
                }
            } else {
                return Integer.parseInt(currentYear);
            }
        } else {
            return Integer.parseInt(currentYear);
        }
    }

    public void getCalendar(int year, int month) {
        isLeapyear = DateUtils.isLeapYear(year);
        daysOfMonth = DateUtils.getDaysOfMonth(isLeapyear, month);
        dayOfWeek = DateUtils.getWeekdayOfMonth(year, month);
        if (month == 1) {
            lastDaysOfMonth = DateUtils.getDaysOfMonth(isLeapyear, 12);
        } else {
            lastDaysOfMonth = DateUtils.getDaysOfMonth(isLeapyear, month - 1);
        }
        if (month == 12) {
            nextDayOfWeek = DateUtils.getDaysOfMonth(isLeapyear, 1);
        } else {
            nextDayOfWeek = DateUtils.getDaysOfMonth(isLeapyear, month + 1);
        }
    }

    public void getWeek(int year, int month, int week) {
        for (int i = 0; i < dayNumber.length; i++) {
            if (dayOfWeek == 7) {
                dayNumber[i] = String.valueOf((i + 1) + 7 * (week - 1));
            } else {
                if (week == 1) {
                    if (i < dayOfWeek) {
                        dayNumber[i] = String.valueOf(lastDaysOfMonth
                                - (dayOfWeek - (i + 1)));
                    } else {
                        dayNumber[i] = String.valueOf(i - dayOfWeek + 1);
                    }
                } else {
                    dayNumber[i] = String.valueOf((7 - dayOfWeek + 1 + i) + 7
                            * (week - 2));
                }
            }

        }
    }

    public String[] getDayNumbers() {
        return dayNumber;
    }

    public int getWeeksOfMonth() {
        // getCalendar(year, month);
        int preMonthRelax = 0;
        if (dayOfWeek != 7) {
            preMonthRelax = dayOfWeek;
        }
        if ((daysOfMonth + preMonthRelax) % 7 == 0) {
            weeksOfMonth = (daysOfMonth + preMonthRelax) / 7;
        } else {
            weeksOfMonth = (daysOfMonth + preMonthRelax) / 7 + 1;
        }
        return weeksOfMonth;
    }

    public void getDayInWeek(int year, int month) {

    }

    @Override
    public int getCount() {
        return dayNumber.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_calendar, null);
            holder.day = (TextView) convertView.findViewById(R.id.tv_calendar);
            holder.isRead = (TextView) convertView.findViewById(R.id.isread);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // && logDay.length > 0
        if (clickTemp == position) {
            holder.day.setTextColor(context.getResources().getColor(R.color.C7));
            holder.day.setBackground(CommonUtils.setBackgroundShap(context, 100, "#9ED1EE",
                    "#9ED1EE"));
        } else {
            holder.day.setTextColor(Color.WHITE);
            holder.day.setBackgroundColor(Color.TRANSPARENT);
        }

        holder.day.setText(dayNumber[position]);

        holder.isRead.setBackgroundColor(Color.TRANSPARENT);
        // if (position < 6 && Integer.parseInt(dayNumber[position]) >
        // Integer.parseInt(dayNumber[dayNumber.length - 1])
        // && preLogDay != null) {
        // for (int i = 0; i < preLogDay.length; i++) {
        // if (Integer.parseInt(dayNumber[position]) == Integer.parseInt(preLogDay[i])) {
        // holder.isRead.setBackground(CommonUtils.setBackgroundShap(context, 100, "#FF0000",
        // "#FF0000"));
        // }
        // }
        // } else if (logDay != null && preMonth == Integer.parseInt(currentMonth) || preMonth + 1
        // == Integer.parseInt(currentMonth)) {
        // // 跨跃首次进入,不显示当月的红点，显示上月的，（当月是只有1号的为当月）
        // for (int i = 0; i < logDay.length; i++) {
        // if (Integer.parseInt(dayNumber[position]) == Integer.parseInt(logDay[i])) {
        // holder.isRead.setBackground(CommonUtils.setBackgroundShap(context, 100,
        // "#FF0000", "#FF0000"));
        // }
        // }
        // }

        if (position < 6 && Integer.parseInt(dayNumber[position]) > Integer.parseInt(dayNumber[dayNumber.length - 1])) {
            for (int i = 0; i < preLogDay.length; i++) {
                if (Integer.parseInt(dayNumber[position]) == Integer.parseInt(preLogDay[i])) {
                    holder.isRead.setBackground(CommonUtils.setBackgroundShap(context, 100, "#FF0000", "#FF0000"));
                }
            }
        } else if (logDay != null) {
            // 跨跃首次进入,不显示当月的红点，显示上月的，（当月是只有1号的为当月）
            for (int i = 0; i < logDay.length; i++) {
                if (Integer.parseInt(dayNumber[position]) == Integer.parseInt(logDay[i])) {
                    holder.isRead.setBackground(CommonUtils.setBackgroundShap(context, 100, "#FF0000", "#FF0000"));
                }
            }
        }
        return convertView;
    }

    static class ViewHolder
    {
        private TextView day, isRead;
    }

    public List<JournalListVO1> getLogs() {
        return logs;
    }

    public void setLogs(List<JournalListVO1> logs) {
        if (logs == null) {
            logDay = new String[0];
            return;
        }
        this.logs = logs;
        logDay = new String[logs.size()];
        for (int i = 0; i < logs.size(); i++) {
            logDay[i] = logs.get(i).getDate().split("-")[2];
        }
    }

    public String[] getLogDay() {
        return logDay;
    }

    public void setLogDay(String[] logDay) {
        this.logDay = logDay;
    }

    public int getClickTemp() {
        return clickTemp;
    }

    public void setClickTemp(int clickTemp) {
        this.clickTemp = clickTemp;
    }

    public boolean isFisrt() {
        return isFisrt;
    }

    public void setFisrt(boolean isFisrt) {
        this.isFisrt = isFisrt;
    }

    public String[] getPreLogDay() {
        return preLogDay;
    }

    public void setPreLogDay(String[] preLogDay) {
        this.preLogDay = preLogDay;
    }

    public List<JournalListVO1> getPreLogs() {
        return preLogs;
    }

    public void setPreLogs(List<JournalListVO1> preLogs) {
        if (preLogs == null) {
            preLogDay = new String[0];
            return;
        }
        this.preLogs = preLogs;
        preLogDay = new String[preLogs.size()];
        for (int i = 0; i < preLogs.size(); i++) {
            preLogDay[i] = preLogs.get(i).getDate().split("-")[2];
        }
    }

    public int getPreMonth() {
        return preMonth;
    }

    public void setPreMonth(int preMonth) {
        this.preMonth = preMonth;
    }

}
