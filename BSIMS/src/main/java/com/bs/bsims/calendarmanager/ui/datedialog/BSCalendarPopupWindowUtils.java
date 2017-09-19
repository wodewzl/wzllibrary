
package com.bs.bsims.calendarmanager.ui.datedialog;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.calendarmanager.ui.datedialog.CalendarView.OnCalendarClickListener;
import com.bs.bsims.calendarmanager.ui.datedialog.CalendarView.OnCalendarDateChangedListener;
import com.bs.bsims.utils.CommDateFormat;

import java.util.List;

public class BSCalendarPopupWindowUtils extends PopupWindow {
    String date = null;
    private String mDateFormat = CommDateFormat.getCurrentTodayDate("yyyy-MM-dd");
    private CalendarView mKCalendar;

    // 不用每次都new 一个pop

    public BSCalendarPopupWindowUtils(final Context mContext,
            final KcalendarCallback callback) {

        View view = initAndShow(mContext);
        update();

        if (null == mKCalendar) {
            mDateFormat = null;
            mKCalendar = (CalendarView) view.findViewById(R.id.popupwindow_calendar_show);
        }
        final Button popupwindow_calendar_bt_enter = (Button) view
                .findViewById(R.id.popupwindow_calendar_bt_enter);
        final TextView popupwindow_calendar_month = (TextView) view
                .findViewById(R.id.popupwindow_calendar_month);

        popupwindow_calendar_month.setText(mKCalendar.getCalendarYear() + "年"
                + mKCalendar.getCalendarMonth() + "月");

        if (null != date) {
            int years = Integer.parseInt(date.substring(0, date.indexOf("-")));
            int month = Integer.parseInt(date.substring(date.indexOf("-") + 1,
                    date.lastIndexOf("-")));
            popupwindow_calendar_month.setText(years + "年" + month + "月");

            mKCalendar.showCalendar(years, month);
            mKCalendar.setCalendarDayBgColor(date, R.drawable.calendar_date_click);
        }

        // 监听所选中的日期
        mKCalendar.setOnCalendarClickListener(new OnCalendarClickListener() {

            public void onCalendarClick(int row, int col, String dateFormat) {
                Log.e("日历的点击监听", "dateFormat >>> " + dateFormat);
                mDateFormat = dateFormat;
                int month = Integer.parseInt(dateFormat.substring(
                        dateFormat.indexOf("-") + 1,
                        dateFormat.lastIndexOf("-")));

                if (mKCalendar.getCalendarMonth() - month == 1// 跨年跳转
                        || mKCalendar.getCalendarMonth() - month == -11) {
                    mKCalendar.lastMonth();
                } else if (month - mKCalendar.getCalendarMonth() == 1 // 跨年跳转
                        || month - mKCalendar.getCalendarMonth() == -11) {
                    mKCalendar.nextMonth();
                } else {
                    mKCalendar.removeAllBgColor();
                    mKCalendar.setCalendarDayBgColor(dateFormat,
                            R.drawable.calendar_date_click);
                    date = dateFormat;// 最后返回给全局 date
                    callback.kcalendarViewClick(dateFormat);
                    dismiss();
                }
            }
        });

        // 监听当前月份
        mKCalendar.setOnCalendarDateChangedListener(new OnCalendarDateChangedListener() {
            public void onCalendarDateChanged(int year, int month) {
                popupwindow_calendar_month.setText(year + "年" + month + "月");
            }
        });

        // 上月监听按钮
        RelativeLayout popupwindow_calendar_last_month = (RelativeLayout) view
                .findViewById(R.id.popupwindow_calendar_last_month);
        popupwindow_calendar_last_month.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mKCalendar.lastMonth();
            }
        });

        // 下月监听按钮
        RelativeLayout popupwindow_calendar_next_month = (RelativeLayout) view
                .findViewById(R.id.popupwindow_calendar_next_month);
        popupwindow_calendar_next_month.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mKCalendar.nextMonth();
            }
        });

        // 关闭窗口
        popupwindow_calendar_bt_enter.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public BSCalendarPopupWindowUtils(final Context mContext, View parent,
            final KcalendarCallback callback) {

        View view = initAndShow(mContext, parent);
        update();

        if (null == mKCalendar) {
            mDateFormat = null;
            mKCalendar = (CalendarView) view.findViewById(R.id.popupwindow_calendar_show);
        }
        final Button popupwindow_calendar_bt_enter = (Button) view
                .findViewById(R.id.popupwindow_calendar_bt_enter);
        final TextView popupwindow_calendar_month = (TextView) view
                .findViewById(R.id.popupwindow_calendar_month);

        popupwindow_calendar_month.setText(mKCalendar.getCalendarYear() + "年"
                + mKCalendar.getCalendarMonth() + "月");

        if (null != date) {
            int years = Integer.parseInt(date.substring(0, date.indexOf("-")));
            int month = Integer.parseInt(date.substring(date.indexOf("-") + 1,
                    date.lastIndexOf("-")));
            popupwindow_calendar_month.setText(years + "年" + month + "月");

            mKCalendar.showCalendar(years, month);
            mKCalendar.setCalendarDayBgColor(date, R.drawable.calendar_date_click);
        }

        // 监听所选中的日期
        mKCalendar.setOnCalendarClickListener(new OnCalendarClickListener() {

            public void onCalendarClick(int row, int col, String dateFormat) {
                Log.e("日历的点击监听", "dateFormat >>> " + dateFormat);
                mDateFormat = dateFormat;
                int month = Integer.parseInt(dateFormat.substring(
                        dateFormat.indexOf("-") + 1,
                        dateFormat.lastIndexOf("-")));

                if (mKCalendar.getCalendarMonth() - month == 1// 跨年跳转
                        || mKCalendar.getCalendarMonth() - month == -11) {
                    mKCalendar.lastMonth();
                } else if (month - mKCalendar.getCalendarMonth() == 1 // 跨年跳转
                        || month - mKCalendar.getCalendarMonth() == -11) {
                    mKCalendar.nextMonth();
                } else {
                    mKCalendar.removeAllBgColor();
                    mKCalendar.setCalendarDayBgColor(dateFormat,
                            R.drawable.calendar_date_click);
                    date = dateFormat;// 最后返回给全局 date
                    callback.kcalendarViewClick(dateFormat);
                    dismiss();
                }
            }
        });

        // 监听当前月份
        mKCalendar.setOnCalendarDateChangedListener(new OnCalendarDateChangedListener() {
            public void onCalendarDateChanged(int year, int month) {
                popupwindow_calendar_month.setText(year + "年" + month + "月");
            }
        });

        // 上月监听按钮
        RelativeLayout popupwindow_calendar_last_month = (RelativeLayout) view
                .findViewById(R.id.popupwindow_calendar_last_month);
        popupwindow_calendar_last_month.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mKCalendar.lastMonth();
            }
        });

        // 下月监听按钮
        RelativeLayout popupwindow_calendar_next_month = (RelativeLayout) view
                .findViewById(R.id.popupwindow_calendar_next_month);
        popupwindow_calendar_next_month.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mKCalendar.nextMonth();
            }
        });

        // 关闭窗口
        popupwindow_calendar_bt_enter.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /**
     * 用于日志详情的日志标红展示
     * 
     * @param mContext
     * @param parent
     * @param callback
     * @param requestLogWritten
     * @param uid
     */
    public BSCalendarPopupWindowUtils(final Context mContext, View parent,
            final KcalendarCallback callback,
            boolean requestLogWritten, final String uid, List<String> list, String dateway) {

        if (!"".equals(dateway)) {
            date = dateway;
        }

        View view = initAndShow(mContext, parent);
        update();

        if (null == mKCalendar)
            mKCalendar = (CalendarView) view.findViewById(R.id.popupwindow_calendar_show);
        final Button popupwindow_calendar_bt_enter = (Button) view
                .findViewById(R.id.popupwindow_calendar_bt_enter);
        final TextView popupwindow_calendar_month = (TextView) view
                .findViewById(R.id.popupwindow_calendar_month);

        popupwindow_calendar_month.setText(mKCalendar.getCalendarYear() + "年"
                + mKCalendar.getCalendarMonth() + "月");

        if (null != date) {
            int years = 0;
            int month = 0;
            if (date.length() > 8) {
                years = Integer.parseInt(date.substring(0, date.indexOf("-")));
                month = Integer.parseInt(date.substring(date.indexOf("-") + 1,
                        date.lastIndexOf("-")));
            }
            else {
                years = Integer.parseInt(date.substring(0, 4));
                month = Integer.parseInt(date.substring(5));

            }

            popupwindow_calendar_month.setText(years + "年" + month + "月");

            mKCalendar.showCalendar(years, month);
            mKCalendar.setCalendarDayBgColor(date, R.drawable.calendar_date_click);
        }

        // 监听所选中的日期
        mKCalendar.setOnCalendarClickListener(new OnCalendarClickListener() {

            public void onCalendarClick(int row, int col, String dateFormat) {
                Log.e("日历的点击监听", "dateFormat >>> " + dateFormat);
                mDateFormat = dateFormat;

                int month = Integer.parseInt(dateFormat.substring(
                        dateFormat.indexOf("-") + 1,
                        dateFormat.lastIndexOf("-")));
                if (mKCalendar.getCalendarMonth() - month == 1// 跨年跳转
                        || mKCalendar.getCalendarMonth() - month == -11) {
                    mKCalendar.lastMonth();
                    // CustomToast.showShortToast(mContext, "请求上月的数据");
                    // getDataFromServer(mContext, mKCalendar, dateFormat, uid);
                } else if (month - mKCalendar.getCalendarMonth() == 1 // 跨年跳转
                        || month - mKCalendar.getCalendarMonth() == -11) {
                    mKCalendar.nextMonth();
                    // CustomToast.showShortToast(mContext, "请求下月的数据");
                    // getDataFromServer(mContext, mKCalendar, dateFormat, uid);
                } else {
                    mKCalendar.removeAllBgColor();
                    mKCalendar.setCalendarDayBgColor(dateFormat,
                            R.drawable.calendar_date_click);
                    date = dateFormat;// 最后返回给全局 date
                    callback.kcalendarViewClick(dateFormat);
                    dismiss();
                    // mKCalendar = null;
                    // mDateFormat = null;
                }
            }
        });

        // 监听当前月份
        mKCalendar.setOnCalendarDateChangedListener(new OnCalendarDateChangedListener() {
            public void onCalendarDateChanged(int year, int month) {
                popupwindow_calendar_month.setText(year + "年" + month + "月");
                mDateFormat = year + "-" + month + "-01";
                callback.kcalendarViewClick(year + "-" + month);
            }
        });

        // 上月监听按钮
        RelativeLayout popupwindow_calendar_last_month = (RelativeLayout) view
                .findViewById(R.id.popupwindow_calendar_last_month);
        popupwindow_calendar_last_month.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mKCalendar.lastMonth();
                // CustomToast.showShortToast(mContext, "点击按钮：请求上月的数据");
                // getDataFromServer(mContext, mKCalendar, mDateFormat, uid);
            }
        });

        // 下月监听按钮
        RelativeLayout popupwindow_calendar_next_month = (RelativeLayout) view
                .findViewById(R.id.popupwindow_calendar_next_month);
        popupwindow_calendar_next_month.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mKCalendar.nextMonth();
                // CustomToast.showShortToast(mContext, "点击按钮：请求下月的数据");
                // getDataFromServer(mContext, mKCalendar, mDateFormat, uid);
            }
        });

        // 关闭窗口
        popupwindow_calendar_bt_enter.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dismiss();
                // mKCalendar = null;
                // mDateFormat = null;
            }
        });

        // getDataFromServer(mContext, mKCalendar, mDateFormat, uid);

        UpdateMaker(list);
    }

    private View initAndShow(final Context mContext, View parent) {
        View view = View.inflate(mContext, R.layout.popupwindow_calendar_show, null);
        view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.kcalendar_fade_in));
        LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
        ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.kcalendar_push_bottom_in_1));

        setWidth(LayoutParams.FILL_PARENT);
        setHeight(LayoutParams.FILL_PARENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setOutsideTouchable(true);
        setContentView(view);
        if (!isShowing()) {
            // showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            this.showAsDropDown(parent, parent.getLayoutParams().width, 0);
        }

        return view;
    }

    private View initAndShow(final Context mContext) {
        View view = View.inflate(mContext, R.layout.popupwindow_calendar_show, null);
        view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.kcalendar_fade_in));
        LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
        ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.kcalendar_push_bottom_in_1));

        setWidth(LayoutParams.FILL_PARENT);
        setHeight(LayoutParams.FILL_PARENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setOutsideTouchable(true);
        setContentView(view);
        return view;
    }

    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 0);
        } else {
            this.dismiss();
        }
    }

    public interface KcalendarCallback {
        void kcalendarViewClick(String date);

    }

    public void UpdateMaker(List<String> listmark) {
        mKCalendar.removeAllMarks();
        mKCalendar.addMarks(listmark, R.drawable.bg_circle_noread);
    }

}
