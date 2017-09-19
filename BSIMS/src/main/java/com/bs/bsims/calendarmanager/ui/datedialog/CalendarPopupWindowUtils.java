
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
import com.bs.bsims.calendarmanager.ui.datedialog.KCalendar.OnCalendarClickListener;
import com.bs.bsims.calendarmanager.ui.datedialog.KCalendar.OnCalendarDateChangedListener;
import com.bs.bsims.utils.CommDateFormat;

public class CalendarPopupWindowUtils {

    public static void showPopup(Context context, View parent, KcalendarCallback callback) {
        new PopupWindows(context, parent, callback);
    }

    public static void showPopup(Context context, View parent, KcalendarCallback callback,
            boolean requestLogWritten, String uid) {
        new PopupWindows(context, parent, callback, requestLogWritten, uid);
    }

    static class PopupWindows extends PopupWindow {
        String date = null;
        private String mDateFormat = CommDateFormat.getCurrentTodayDate("yyyy-MM-dd");
        private KCalendar mKCalendar;

        public PopupWindows(final Context mContext, View parent, final KcalendarCallback callback) {

            View view = initAndShow(mContext, parent);
            update();

            if (null == mKCalendar) {
                mDateFormat = null;
                mKCalendar = (KCalendar) view.findViewById(R.id.popupwindow_calendar_show);
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
                mKCalendar.setCalendarDayBgColor(date, R.drawable.kcalendar_date_focused);
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
                                R.drawable.kcalendar_date_focused);
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
        public PopupWindows(final Context mContext, View parent, final KcalendarCallback callback,
                boolean requestLogWritten, final String uid) {

            View view = initAndShow(mContext, parent);
            update();

            if (null == mKCalendar)
                mKCalendar = (KCalendar) view.findViewById(R.id.popupwindow_calendar_show);
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
                mKCalendar.setCalendarDayBgColor(date, R.drawable.kcalendar_date_focused);
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
                                R.drawable.kcalendar_date_focused);
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

        }

        private View initAndShow(final Context mContext, View parent) {
            View view = View.inflate(mContext, R.layout.popupwindow_calender_renwu, null);
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
                showAsDropDown(parent,parent.getLayoutParams().width / 2, 0);

//                mPop.showAsDropDown(mTitleLayout, mTitleLayout.getLayoutParams().width / 2, 0);

            }

            return view;
        }

   

    }

    public interface KcalendarCallback {
        void kcalendarViewClick(String date);
    }

}
