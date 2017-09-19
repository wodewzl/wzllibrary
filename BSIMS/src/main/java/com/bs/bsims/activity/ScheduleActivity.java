
package com.bs.bsims.activity;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.ScheduleAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.constant.ExtrasBSVO;
import com.bs.bsims.model.ScheduleVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSCalendarCurrentItemView;
import com.bs.bsims.view.BSCalendarView;
import com.bs.bsims.view.BSCalendarView.OnCalendarClickListener;
import com.bs.bsims.view.BSCalendarView.OnCalendarDateChangedListener;
import com.bs.bsims.view.BSRefreshListView;
import com.google.gson.Gson;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ScheduleActivity extends BaseActivity implements OnClickListener, OnItemClickListener {
    private BSCalendarView mCalendar;
    private BSCalendarCurrentItemView mOneItme;
    private int downY;
    String mDate;// 设置默认选中的日期 格式为 “2014-04-05” 标准DATE格式

    private ScheduleVO mScheduleVO;
    // private ListView mListView;
    private ScheduleAdapter mAdapter;
    private LinearLayout mContent;
    private TextView mDayTv;
    private TextView mScheduleTitle;
    private String mCurrentDay;
    private float bo = 0;
    private ImageView mStateImg;
    private BSRefreshListView mListView;
    private TextView popupwindow_calendar_month;

    private boolean result = false;
    private boolean isOpen = false;

    private String mYear, Month, mWeek, mDay;
    private int mPreRowIndex = 5;
    private float prebo;
    private PopupWindow mOkPop;
    private List<PopupWindow> mListpop = new ArrayList<PopupWindow>();

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.schedule, null);
        mContentLayout.addView(layout);
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    @Override
    public void updateUi() {
    }

    @Override
    public void initView() {
        // content_layout = (TextView) findViewById(R.id.s_layout);
        mTitleTv.setText("日程管理");
        mOkTv.setText("添加");
        mOneItme = (BSCalendarCurrentItemView) findViewById(R.id.one_itme);
        mCalendar = (BSCalendarView) findViewById(R.id.popupwindow_calendar);
        mContent = (LinearLayout) findViewById(R.id.contents_layout);
        mDayTv = (TextView) findViewById(R.id.day);
        mScheduleTitle = (TextView) findViewById(R.id.schedule_title);

        mStateImg = (ImageView) findViewById(R.id.state_img);

        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mCalendar.measure(w, h);

        if (getMonthDayRowIndex() == 5) {
            bo = (float) (mCalendar.getMeasuredHeight() * 0.62);
        } else {
            bo = (float) (mCalendar.getMeasuredHeight() * 0.79);
        }

        mDate = DateUtils.getCurrentDate();
        StringBuffer sb = new StringBuffer();
        mContent.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return handlerTouch(v, event);
            }
        });

        popupwindow_calendar_month = (TextView) findViewById(R.id.popupwindow_calendar_month);
        popupwindow_calendar_month.setText(mCalendar.getCalendarYear() + "年" + mCalendar.getCalendarMonth() + "月");
        mWeek = mCalendar.getCurrentWeek();
        sb.append(mCalendar.getCalendarYear()).append(".").append(mCalendar.getCalendarMonth()).append("\n").append(mWeek);
        // mScheduleTitle.setText(sb.toString() + "      今天");
        mScheduleTitle.setText(sb.toString());
        String date = DateUtils.getCurrentDate();
        mCurrentDay = date.substring(date.lastIndexOf("-") + 1, date.length());
        mDayTv.setText(mCurrentDay);

        // 监听所选中的日期
        mCalendar.setOnCalendarClickListener(new OnCalendarClickListener() {

            public void onCalendarClick(int row, int col, String dateFormat) {
                if (dateFormat == null)
                    return;
                StringBuffer sb = new StringBuffer();
                int month = Integer.parseInt(dateFormat.substring(dateFormat.indexOf("-") + 1, dateFormat.lastIndexOf("-")));
                int day = Integer.parseInt(dateFormat.substring(dateFormat.lastIndexOf("-") + 1, dateFormat.length()));
                mWeek = DateUtils.getDayOfWeek(dateFormat);
                mDay = day + "";
                mDay = day + "";
                String holeDate = dateFormat.replace("-", ".");
                String date = holeDate.substring(0, holeDate.lastIndexOf("."));
                if (mCalendar.getCalendarMonth() - month == 1// 跨年跳转
                        || mCalendar.getCalendarMonth() - month == -11) {
                    // mCalendar.lastMonth();

                } else if (month - mCalendar.getCalendarMonth() == 1 // 跨年跳转
                        || month - mCalendar.getCalendarMonth() == -11) {
                    // mCalendar.nextMonth();

                } else {
                    mOneItme.removeAllBgColor();
                    mOneItme.setCalendarDayBgColor(dateFormat,
                            R.drawable.calendar_date_click);
                    mCalendar.removeAllBgColor();
                    mCalendar.setCalendarDayBgColor(dateFormat,
                            R.drawable.calendar_date_click);
                    mDate = dateFormat;// 最后返回给全局 dates

                    mCalendar.removeAllMarks();
                    mOneItme.removeAllMarks();
                    if (day - Integer.parseInt(mCurrentDay) > 0) {

                        sb.append(date).append("\n").append(mWeek);

                        mScheduleTitle.setText(sb.toString());

                    } else if (day - Integer.parseInt(mCurrentDay) < 0) {
                        sb.append(date).append("\n").append(mWeek);
                        mScheduleTitle.setText(sb.toString());
                    }
                    mDayTv.setText(day + "");
                    mListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
                    new ThreadUtil(ScheduleActivity.this, ScheduleActivity.this).start();
                }
            }
        });

        mOneItme.setOnCalendarClickListener(new com.bs.bsims.view.BSCalendarCurrentItemView.OnCalendarClickListener() {

            @Override
            public void onCalendarClick(int row, int col, String dateFormat) {
                StringBuffer sb = new StringBuffer();
                int month = Integer.parseInt(dateFormat.substring(dateFormat.indexOf("-") + 1, dateFormat.lastIndexOf("-")));
                int day = Integer.parseInt(dateFormat.substring(dateFormat.lastIndexOf("-") + 1, dateFormat.length()));
                mWeek = DateUtils.getDayOfWeek(dateFormat);
                mDay = day + "";
                String holeDate = dateFormat.replace("-", ".");
                String date = holeDate.substring(0, holeDate.lastIndexOf("."));
                if (mOneItme.getCalendarMonth() - month == 1// 跨年跳转
                        || mOneItme.getCalendarMonth() - month == -11) {
                    // mOneItme.lastMonth();
                    // mCalendar.lastMonth();

                } else if (month - mOneItme.getCalendarMonth() == 1 // 跨年跳转
                        || month - mOneItme.getCalendarMonth() == -11) {
                    // mOneItme.nextMonth();
                    // mCalendar.nextMonth();

                } else {
                    mOneItme.removeAllBgColor();
                    mOneItme.setCalendarDayBgColor(dateFormat,
                            R.drawable.calendar_date_click);

                    mCalendar.removeAllBgColor();
                    mCalendar.setCalendarDayBgColor(dateFormat,
                            R.drawable.calendar_date_click);

                    mCalendar.removeAllMarks();
                    mOneItme.removeAllMarks();

                    mDate = dateFormat;// 最后返回给全局 date
                    if (day - Integer.parseInt(mCurrentDay) > 0) {

                        sb.append(date).append("\n").append(mWeek);

                        mScheduleTitle.setText(sb.toString());

                    } else if (day - Integer.parseInt(mCurrentDay) < 0) {
                        sb.append(date).append("\n").append(mWeek);
                        mScheduleTitle.setText(sb.toString());
                    }
                    mDayTv.setText(day + "");
                    mListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
                    new ThreadUtil(ScheduleActivity.this, ScheduleActivity.this).start();
                }
            }
        });

        // 监听当前月份
        mCalendar.setOnCalendarDateChangedListener(new OnCalendarDateChangedListener() {
            public void onCalendarDateChanged(int year, int month) {
                popupwindow_calendar_month.setText(year + "年" + month + "月");
                mOneItme.setCalendarYear(year);
                mOneItme.setCalendarMonth(month);
                mOneItme.init();

                mCalendar.removeAllBgColor();
                mCalendar.removeAllMarks();
                mOneItme.removeAllBgColor();
                mOneItme.removeAllMarks();

                mDate = mCalendar.getCurrentItem();
                mListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
                new ThreadUtil(ScheduleActivity.this, ScheduleActivity.this).start();

                if (!isOpen) {
                    if (getMonthDayRowIndex() == 5) {
                        bo = (float) (mCalendar.getMeasuredHeight() * 0.62);
                    } else {
                        bo = (float) (mCalendar.getMeasuredHeight() * 0.77);
                    }
                    return;
                }
                float currentY = 0;
                if (getMonthDayRowIndex() == 5) {
                    currentY = (float) (mCalendar.getMeasuredHeight() * 0.62);
                    if (bo != currentY) {
                        ObjectAnimator oa1 = ObjectAnimator.ofFloat(mContent, "translationY", bo, currentY).setDuration(1000);
                        oa1.start();
                        oa1.addListener(new AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                bo = (float) (mCalendar.getMeasuredHeight() * 0.62);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {
                            }
                        });

                    }

                } else {
                    currentY = (float) (mCalendar.getMeasuredHeight() * 0.77);
                    if (bo != currentY) {
                        ObjectAnimator oa1 = ObjectAnimator.ofFloat(mContent, "translationY", bo, currentY).setDuration(1000);
                        oa1.start();
                        oa1.start();
                        oa1.addListener(new AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                bo = (float) (mCalendar.getMeasuredHeight() * 0.77);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {
                            }
                        });

                    }

                }

            }
        });

        // 上月监听按钮
        RelativeLayout popupwindow_calendar_last_month = (RelativeLayout) findViewById(R.id.popupwindow_calendar_last_month);
        popupwindow_calendar_last_month.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                mCalendar.lastMonth();
            }

        });

        // 下月监听按钮
        RelativeLayout popupwindow_calendar_next_month = (RelativeLayout) findViewById(R.id.popupwindow_calendar_next_month);
        popupwindow_calendar_next_month.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // if (isOpen)
                mCalendar.nextMonth();
            }
        });

        mListView = (BSRefreshListView) findViewById(R.id.listview);
        mAdapter = new ScheduleAdapter(this);
        mListView.setAdapter(mAdapter);

        mListView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if (isOpen) {
                    ObjectAnimator oa1 = ObjectAnimator.ofFloat(mContent, "translationY", bo,
                            0).setDuration(1000);
                    oa1.start();
                    ObjectAnimator oa2 = ObjectAnimator.ofFloat(mOneItme, "translationY", -bo,
                            0).setDuration(1000);
                    oa2.start();
                    mStateImg.setImageResource(R.drawable.calendar_open);
                    isOpen = false;
                    result = false;
                    mCalendar.setVisibility(View.GONE);
                }

                return false;
            }
        });
        selectCalendar();

    }

    public int getMonthDayRowIndex() {
        String date = DateUtils.getDateByYearAndMonth(mCalendar.getCalendarYear(), mCalendar.getCalendarMonth(), 2);
        String day = date.split("-")[2];
        String str = date.substring(0, date.lastIndexOf("-"));
        int number = DateUtils.getDayOfWeekNumber(str + "-01");
        int total = Integer.parseInt(day) + number;
        return (int) Math.ceil(total / 7.0);
    }

    protected boolean handlerTouch(View v, MotionEvent event) {

        // int bottomHight = mCalendar.getHeight();
        // int bottomHight = mCalendar.getMeasuredHeight();
        int bottomHight = (int) bo;
        mCalendar.setVisibility(View.VISIBLE);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) event.getRawY();

                break;

            case MotionEvent.ACTION_MOVE:
                int dy = (int) (event.getRawY() - downY);
                if (isOpen) {
                    // 打开状上滑动
                    if (dy < 0 && Math.abs(dy) < bottomHight) {
                        // one_itme.setY(0);
                        v.setTranslationY(bottomHight + dy);
                        // one_itme.setTranslationY(-dy);
                        // 允许移动，阻止点击
                        result = true;
                    }
                } else {
                    // 闭合状态
                    // 向下移动
                    if (dy > 0 && dy < bottomHight) {
                        v.setTranslationY(dy);
                        mOneItme.setTranslationY(0 - dy);
                        // 允许移动，阻止点击
                        result = true;
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_UP:

                // 获取已经移动的
                float ddy = v.getTranslationY();

                // 判断打开还是关闭

                if (ddy >= 0 && ddy < (bottomHight / 2)) {
                    // 关闭
                    ObjectAnimator oa1 = ObjectAnimator.ofFloat(v, "translationY", ddy, 0).setDuration(100);
                    oa1.start();
                    ObjectAnimator oa2 = ObjectAnimator.ofFloat(mOneItme, "translationY", ddy,
                            0).setDuration(100);
                    oa2.start();
                    oa1.addListener(new AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            isOpen = false;
                            result = false;
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                            isOpen = false;
                            result = false;
                        }
                    });
                    mCalendar.setVisibility(View.GONE);
                }
                if (ddy >= (bottomHight / 2) && ddy > 0) {
                    // 打开
                    ObjectAnimator oa1 = ObjectAnimator.ofFloat(v, "translationY", ddy, bottomHight)
                            .setDuration(100);
                    oa1.start();
                    result = true;
                    isOpen = true;
                    mCalendar.setVisibility(View.VISIBLE);
                }
                break;
        }
        return true;
    }

    @Override
    public void bindViewsListener() {
        mOkTv.setOnClickListener(this);
        mListView.setOnItemClickListener(this);
        mStateImg.setOnClickListener(this);
        popupwindow_calendar_month.setOnClickListener(this);
    }

    public boolean getData() {
        try {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put("date", mDate);
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.BOSS_SCHEDULE, map);
            Gson gson = new Gson();
            mScheduleVO = gson.fromJson(jsonStr, ScheduleVO.class);
            if (Constant.RESULT_CODE.equals(mScheduleVO.getCode())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void executeSuccess() {
        mListView.onRefreshComplete();
        mAdapter.mList.clear();
        mContentLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
        if (mScheduleVO == null) {
            List<ScheduleVO> list = new ArrayList<ScheduleVO>();
            mAdapter.updateData(list);
            return;
        }
        if (mScheduleVO.getArray() == null) {
            List<ScheduleVO> list = new ArrayList<ScheduleVO>();
            mAdapter.updateData(list);
            return;
        }
        if (mScheduleVO.getArray().getSchedule() != null)
            mAdapter.updateDataLast(mScheduleVO.getArray().getSchedule());
        if (mScheduleVO.getArray().getTask() != null)
            mAdapter.updateDataLast(mScheduleVO.getArray().getTask());

        if (mScheduleVO.getArray().getSchedule() == null && mScheduleVO.getArray().getTask() == null) {

            List<ScheduleVO> list = new ArrayList<ScheduleVO>();
            mAdapter.updateData(list);
        }

        if (mScheduleVO.getArray().getDate() != null) {
            String[] str = mScheduleVO.getArray().getDate().split(",");
            String year = mCalendar.getCalendarYear() + "";
            String month = mCalendar.getCalendarMonth() + "";
            if (month.length() == 1)
                month = "0" + month;
            List<String> list = new ArrayList<String>();

            for (int i = 0; i < str.length; i++) {
                String date = year + "-" + month + "-" + str[i];
                list.add(date);
            }
            mCalendar.addMarks(list, R.drawable.bg_circle_noread);
            mOneItme.addMarks(list, R.drawable.bg_circle_noread);
        }

        mStateImg.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (CommonUtils.showLead(ScheduleActivity.this, "ScheduleActivity")) {
                    initLeadView();
                }

            }
        }, 1000);

    }

    private String format(Timestamp timestamp) {
        return null;
    }

    @Override
    public void executeFailure() {
        executeSuccess();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_comm_head_right:
                Intent intent = new Intent();
                intent.putExtra("date", mDate);
                intent.setClass(this, ScheduleAddActivity.class);
                startActivityForResult(intent, 2);
                break;

            case R.id.state_img:
                selectCalendar();
                break;
            case R.id.popupwindow_calendar_month:
                selectCalendar();
                break;
            default:
                break;
        }
    }

    public void selectCalendar() {
        if (isOpen) {
            mStateImg.setImageResource(R.drawable.calendar_open);
            ObjectAnimator oa1 = ObjectAnimator.ofFloat(mContent, "translationY", bo, 0).setDuration(1000);
            oa1.start();
            ObjectAnimator oa2 = ObjectAnimator.ofFloat(mOneItme, "translationY", -bo, 0).setDuration(1000);

            oa2.start();
            isOpen = false;
            result = false;
            mCalendar.setVisibility(View.GONE);
        } else {
            mStateImg.setImageResource(R.drawable.calendar_close);
            ObjectAnimator oa1 = ObjectAnimator.ofFloat(mContent, "translationY", 0, bo).setDuration(1000);
            oa1.start();
            ObjectAnimator oa2 = ObjectAnimator.ofFloat(mOneItme, "translationY", 0, -bo).setDuration(1000);

            oa2.start();
            isOpen = true;
            result = true;
            mCalendar.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mAdapter.mList.size() != 0) {
            Intent intent = new Intent();
            if (mAdapter.mList.get((int) id).getMyself() != null) {
                intent.putExtra("id", mAdapter.mList.get((int) id).getId());
                intent.setClass(ScheduleActivity.this, ScheduleDetailActivity.class);
            } else {
                intent.putExtra(ExtrasBSVO.Push.BREAK_ID, mAdapter.mList.get((int) id).getId());
                intent.setClass(ScheduleActivity.this, EXTTaskEventDetailsActivity.class);
            }
            this.startActivity(intent);

        }
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        if (arg2 != null) {
            mDate = arg2.getStringExtra("star_time");
            mDayTv.setText(mDate.split("-")[2]);
            String week = DateUtils.getDayOfWeek(mDate);
            StringBuffer sb = new StringBuffer();
            String holeDate = mDate.replace("-", ".");
            String date = holeDate.substring(0, holeDate.lastIndexOf("."));
            sb.append(date).append("\n").append(week);
            mScheduleTitle.setText(sb.toString());

            mOneItme.removeAllBgColor();
            mOneItme.setCalendarDayBgColor(mDate, R.drawable.calendar_date_click);

            mCalendar.removeAllBgColor();
            mCalendar.setCalendarDayBgColor(mDate, R.drawable.calendar_date_click);

            mListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
            new ThreadUtil(this, this).start();
        }

    }

    public void initLeadView() {
        LinearLayout.LayoutParams okTvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);// 定义文本显示组件
        okTvParams.rightMargin = CommonUtils.dip2px(this, 10);
        LinearLayout.LayoutParams okImgParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, CommonUtils.dip2px(this, 90));// 定义文本显示组件
        okImgParams.gravity = Gravity.RIGHT;
        okImgParams.rightMargin = CommonUtils.dip2px(this, 0) + CommonUtils.getViewWidth(mStateImg) / 2;
        mOkPop = CommonUtils.leadPop(this, mStateImg, "亲，可以点击按钮展开日历，也可上下滑动展开日历哦", okTvParams, okImgParams, 2);
        mListpop.add(mOkPop);
        CommonUtils.okLeadPop(this, mContentLayout, mListpop, 0);
    }

}
