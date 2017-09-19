
package com.bs.bsims.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.calendarmanager.ui.datedialog.CalendarPopupWindowUtils;
import com.bs.bsims.calendarmanager.ui.datedialog.CalendarPopupWindowUtils.KcalendarCallback;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.constant.Constant4TaskEventPath;
import com.bs.bsims.model.TaskEventItemVO;
import com.bs.bsims.receiver.TaskActionReceiver;
import com.bs.bsims.time.ScreenInfo;
import com.bs.bsims.time.WheelMain;
import com.bs.bsims.ui.datepicker.OnWheelScrollListener;
import com.bs.bsims.ui.datepicker.WheelView;
import com.bs.bsims.ui.datepicker.adapter.NumericWheelAdapter;
import com.bs.bsims.utils.CommonDateUtils;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.utils.WindowUtils;
import com.bs.bsims.xutils.impl.HttpUtilsByPC;
import com.bs.bsims.xutils.impl.RequestCallBackPC;
import com.google.gson.Gson;

import org.xutils.x;
import org.xutils.ex.HttpException;
import org.xutils.view.annotation.ViewInject;
 

import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @author peck
 * @Description: 改时间 任务查看者是否有编辑任务结束时间权限（1有0无）
 * @date 2015-6-5 上午9:45:20
 * @email 971371860@qq.com
 * @version V1.0
 */

public class EXTTaskChangeDateActivity extends BaseActivity implements
        OnClickListener {

    private Context context;

    /**
     * 重新选择的时间
     */
    private String time;

    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 到期时间
     */
    private String endTime;

    /**
     * 到期时间
     */
    private String alid;

    /** 开始时间 */
    @ViewInject(R.id.txt_task_changedate_starttime)
    private TextView txt_task_changedate_starttime;

    /** 到期时间 */
    @ViewInject(R.id.txt_task_changedate_endtime)
    private TextView txt_task_changedate_endtime;

    /** 新的结束时间 */
    @ViewInject(R.id.txt_task_changedate_newtime)
    private TextView txt_task_changedate_newtime;
    /** 新的结束时间 */
    @ViewInject(R.id.rlayout_task_changedate_newtime)
    private RelativeLayout rlayout_task_changedate_newtime;

    /** 新的结束时间 */
    @ViewInject(R.id.txt_comm_head_right)
    private TextView txt_comm_head_right;

    private LinearLayout date_task_changedate_select_ly;
    private LayoutInflater inflater;

    private WheelView year;
    private WheelView month;
    private WheelView day;

    private int norYear;
    private int curMonth;
    private int curDate;

    @Override
    public void baseSetContentView() {
        // TODO Auto-generated method stub
//        setContentView(R.layout.ac_task_changedate);
        View.inflate(this, R.layout.ac_task_changedate,mContentLayout);
        x.view().inject(this);
        context = this;
        inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

        Intent intent = getIntent();
        startTime = intent.getStringExtra("startTime");
        endTime = intent.getStringExtra("endTime");
        startTime = CommonDateUtils.parseDate(Long.parseLong(startTime) * 1000,
                "yyyy-MM-dd");
        endTime = CommonDateUtils.parseDate(Long.parseLong(endTime) * 1000,
                "yyyy-MM-dd");

        alid = intent.getStringExtra("id");
    }

    @Override
    public boolean getDataResult() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void updateUi() {
        // TODO Auto-generated method stub

    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        initHeadView();
        txt_task_changedate_starttime.setText(startTime);
        txt_task_changedate_endtime.setText(endTime);

        date_task_changedate_select_ly = (LinearLayout) findViewById(R.id.date_task_changedate_select_ly);
        date_task_changedate_select_ly.addView(getDataPickLikeIOS());
        time = DateUtils.getCurrentDate();
        txt_task_changedate_newtime.setText(time);
    }

    @Override
    public void bindViewsListener() {
        // TODO Auto-generated method stub
        // rlayout_task_changedate_newtime.setOnClickListener(this);
        txt_comm_head_right.setOnClickListener(this);
    }

    private void initHeadView() {
        // findViewById(R.id.relative_comm_head_back).setOnClickListener(headBackListener);
        findViewById(R.id.img_head_back).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

        ((TextView) findViewById(R.id.txt_comm_head_activityName))
                .setText("更改时间");

        txt_comm_head_right.setText("确定");
    }

    @Override
    public void onClick(View v) {
        WindowUtils.hideKeyboard(((Activity) context));
        switch (v.getId()) {
            case R.id.rlayout_task_changedate_newtime: {
                selectCalendarPopupWindow();
            }
                break;
            case R.id.txt_comm_head_right: {
                // 确定
                if (TextUtils.isEmpty(time)) {
                    CustomToast.showShortToast(context, "请选择新的结束时间");
                    // selectCalendarPopupWindow();
                    return;
                }
                getDataFromServerOnStart();

            }
                break;
        }
    }

    private void getDataFromServerOnStart() {
        CustomDialog.showProgressDialog(context);
        // http://cp.beisheng.wang//api.php/Task/changeTime?alid=127&ftoken=RBDTZXGUMNDKkEwMkZFN0UyMTA1RgO0O0OO0O0O&time=2015-06-05&userid=38
        String url = BSApplication.getInstance().getHttpTitle()
                + Constant4TaskEventPath.TASK_CHANGETIME_PATH;
        Map<String, String> paramsMap = new HashMap<String, String>();
        // paramsMap.put("ftoken", "RBDTZXGUMNDKkEwMkZFN0UyMTA1RgO0O0OO0O0O");
        // paramsMap.put("userid", "38");
        // paramsMap.put("alid", "127");
        // paramsMap.put("time", time);
        // url = "http://cp.beisheng.wang//api.php/Task/changeTime";
        paramsMap.put("ftoken", BSApplication.getInstance().getmCompany());
        paramsMap.put("userid", BSApplication.getInstance().getUserId());
        paramsMap.put("alid", alid);
        paramsMap.put("time", time);

        new HttpUtilsByPC().sendPostBYPC(url, paramsMap,
                new RequestCallBackPC() {

                    @Override
                    public void onSuccessPC(ResponseInfo arg0) {
                        // TODO Auto-generated method stub
                        String result = (String) arg0.result;
                        Gson gson = new Gson();
                        TaskEventItemVO taskEventItemVO = gson.fromJson(result,
                                TaskEventItemVO.class);
                        if (Constant.RESULT_CODE.equals(taskEventItemVO
                                .getCode())) {
                            sendNewTimeBoardMessage();
                            finish();
                        } else if (Constant.RESULT_CODE400
                                .equals(taskEventItemVO.getCode())) {
                            CustomToast.showShortToast(context,
                                    taskEventItemVO.getRetinfo());
                            CustomDialog.closeProgressDialog();
                        }
                    }

                    @Override
                    public void onFailurePC(HttpException arg0, String arg1) {
                        // TODO Auto-generated method stub
                        CustomToast.showShortToast(context, "网络似乎断开了哦");
                        CustomDialog.closeProgressDialog();
                    }
                });
    }

    private void selectCalendarPopupWindow() {
        // TODO Auto-generated method stub
        CalendarPopupWindowUtils.showPopup(context, getWindow().getDecorView(),
                new KcalendarCallback() {
                    @Override
                    public void kcalendarViewClick(String date) {
                        time = date;
                        boolean flag = CommonDateUtils.isOutDate(time);
                        if (flag) {
                            // selectCalendarPopupWindow();
                            time = DateUtils.getCurrentDate();
                            txt_task_changedate_newtime.setText(time);
                            CustomToast.showShortToast(context, "不能晚于今天");
                            return;
                        }
                        txt_task_changedate_newtime.setText(date);
                    }
                }, true, null);
    }

    public void sendNewTimeBoardMessage() {
        Intent intent = new Intent(TaskActionReceiver.CHANGEDATE);
        try {
            startTime = DateUtils.stringToLong(startTime, "yyyy-MM-dd") + "";
            time = DateUtils.stringToLong(time, "yyyy-MM-dd") + "";
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        intent.putExtra("startTime", startTime);
        intent.putExtra("newEndTime", time);
        intent.putExtra("task_id", alid);
        sendBroadcast(intent);
    }

    private View getDataPickLikeIOS() {
        // TODO Auto-generated method stub
        Calendar c = Calendar.getInstance();
        norYear = c.get(Calendar.YEAR);
        curMonth = c.get(Calendar.MONTH) + 1;// 通过Calendar算出的月数要+1
        curDate = c.get(Calendar.DATE);

        View timepickerview = inflater.inflate(R.layout.wheel_date_picker, null);
        ScreenInfo screenInfo = new ScreenInfo(this);
        WheelMain wheelMain = new WheelMain(timepickerview, true);
        timepickerview.findViewById(R.id.back_vpop).setVisibility(View.GONE);
        timepickerview.findViewById(R.id.sure_vpop).setVisibility(View.GONE);

        year = (WheelView) timepickerview.findViewById(R.id.datepicker_year);
        NumericWheelAdapter numericWheelAdapter1 = new NumericWheelAdapter(context, norYear, norYear + 10);
        numericWheelAdapter1.setLabel("年");
        year.setViewAdapter(numericWheelAdapter1);
        year.setCyclic(true);// 是否可循环滑动
        year.addScrollingListener(scrollListener);

        month = (WheelView) timepickerview.findViewById(R.id.datepicker_month);
        NumericWheelAdapter numericWheelAdapter2 = new NumericWheelAdapter(context, 1, 12, "%02d");
        numericWheelAdapter2.setLabel("月");
        month.setViewAdapter(numericWheelAdapter2);
        month.setCyclic(true);
        month.addScrollingListener(scrollListener);

        day = (WheelView) timepickerview.findViewById(R.id.datepicker_day);
        // NumericWheelAdapter numericWheelAdapter3 = new NumericWheelAdapter(this, 1, norYear,
        // curMonth, "%02d");
        initDay(norYear, curMonth);
        day.setCyclic(true);
        day.addScrollingListener(scrollListener);

        year.setVisibleItems(7);// 设置显示行数
        month.setVisibleItems(7);
        day.setVisibleItems(7);

        year.setCurrentItem(0);
        month.setCurrentItem(curMonth - 1);
        day.setCurrentItem(curDate - 1);

        return timepickerview;
    }

    OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {
        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            int n_year = year.getCurrentItem() + norYear;// 年
            int n_month = month.getCurrentItem() + 1;// 月
            int n_day = day.getCurrentItem() + 1;// 月
            initDay(n_year, n_month);

            String today = new StringBuilder().append(n_year).append("-")
                    .append(n_month < 10 ? "0" + n_month :
                            n_month).append("-")
                    .append((n_day < 10) ? "0" + n_day :
                            n_day).toString();

            time = today;
            boolean flag = CommonDateUtils.isOutDate(time);
            if (flag) {
                year.setCurrentItem(0);
                month.setCurrentItem(curMonth - 1);
                day.setCurrentItem(curDate - 1);

                time = DateUtils.getCurrentDate();
                txt_task_changedate_newtime.setText(time);
                return;
            }
            txt_task_changedate_newtime.setText(today);
        }
    };

    /**
     */
    private void initDay(int year, int month) {
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(this, 1, DateUtils.getDayAtYM(year, month), "%02d");
        numericWheelAdapter.setLabel("日");
        day.setViewAdapter(numericWheelAdapter);
    }
}
