
package com.bs.bsims.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bs.bsims.R;
import com.bs.bsims.adapter.DateAdapter;
import com.bs.bsims.adapter.JournalListAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.JournalListVO1;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSRefreshListView;
import com.bs.bsims.view.PinnedSectionListView;
import com.google.gson.Gson;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JournalPersonalDetailActivity extends BaseActivity implements OnGestureListener, OnClickListener, OnItemClickListener {
    private ViewFlipper mFlipper = null;
    private GridView mGridView = null;
    private GestureDetector mGestureDetector = null;
    private int mYear = 0;
    private int mMonth = 0;
    private int mDay = 0;
    private int mWeek = 0;
    private int mWeekNum = 0;
    private String mCurrentDate = "";
    private DateAdapter mDateAdapter;
    private int mDaysOfMonth = 0; // 某月的天数
    private int mDayOfWeek = 0; // 具体某一天是星期几
    private int mWeeksOfMonth = 0;
    private boolean mIsLeapyear = false; // 是否为闰年
    private int mSelectPostion = 0;
    private String mDayNumbers[] = new String[7];
    private TextView mDateTv;
    private int mCurrentYear;
    private int mCurrentMonth;
    private int mCurrentWeek;
    private int mCurrentDay;
    private int mCurrentNum;
    // private TextView mDateTv;

    private String mFristid, mLastid;
    private int mState = 0; // 0为首次,1为上拉刷新 ，2为下拉刷新
    private PinnedSectionListView mRefreshListView;
    private JournalListAdapter mAdapter;
    private JournalListVO1 mListVO;
    private ImageView mPreMonthIv, mNextMonthIv;
    private String mDate, mLoguid, mTitleName;
    String[] mMonths = {
            "一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"
    };

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.journal_personal_detail, mContentLayout);
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    @Override
    public void updateUi() {
    }

    @Override
    public void executeSuccess() {
        super.isRequestFinish();
        mRefreshListView.setVisibility(View.VISIBLE);
        mRefreshListView.onRefreshComplete();
        if (1 == mState) {
            mAdapter.updateDataFrist(mListVO.getInfo().getLoglist());
        } else {
            mAdapter.updateData(mListVO.getInfo().getLoglist());
        }

        // 返回3个月的，选取跨月是相邻的两个月
        if (Integer.parseInt(mDayNumbers[mDateAdapter.getSeclection()]) > Integer.parseInt(mDayNumbers[6])) {
            if (mListVO.getInfo().getDates() != null)
                mDateAdapter.setPreLogs(mListVO.getInfo().getDates());
            else
                mDateAdapter.setPreLogs(null);

            // 跨月加载下个月的
            if (mListVO.getNextdates() != null)
                mDateAdapter.setLogs(mListVO.getNextdates());
            else
                mDateAdapter.setLogs(null);
        } else {

            // 不跨月只用加载当月的
            if (mListVO.getLastdates() != null)
                mDateAdapter.setPreLogs(mListVO.getLastdates());
            else
                mDateAdapter.setPreLogs(null);

            if (mListVO.getInfo().getDates() != null)
                mDateAdapter.setLogs(mListVO.getInfo().getDates());
            else
                mDateAdapter.setLogs(null);

        }

        mDateAdapter.notifyDataSetChanged();

        mState = 0;
    }

    @Override
    public void executeFailure() {
        // 列表展示的时候不能调用父类
        super.isRequestFinish();
        mAdapter.notifyDataSetChanged();
        mRefreshListView.onRefreshComplete();

        if (mListVO == null) {
            mAdapter.updateData(new ArrayList<JournalListVO1>());
        } else {
            if (mState == 0) {
                mAdapter.updateData(new ArrayList<JournalListVO1>());
            }
        }

        if (Integer.parseInt(mDayNumbers[mDateAdapter.getSeclection()]) > Integer.parseInt(mDayNumbers[6])) {
            mDateAdapter.setPreLogs(null);
            if (mListVO.getNextdates() != null)
                mDateAdapter.setLogs(mListVO.getNextdates());
            else
                mDateAdapter.setLogs(null);
        } else {

            // 不跨月只用加载当月的
            if (mListVO.getLastdates() != null)
                mDateAdapter.setPreLogs(mListVO.getLastdates());
            else
                mDateAdapter.setPreLogs(null);

            mDateAdapter.setLogs(null);
        }
        mState = 0;
        mDateAdapter.notifyDataSetChanged();

    }

    @Override
    public void initView() {
        mOkTv.setText("关闭");
        mDateTv = (TextView) findViewById(R.id.date_tv);
        mPreMonthIv = (ImageView) findViewById(R.id.pre_month_iv);
        mNextMonthIv = (ImageView) findViewById(R.id.next_month_iv);
        mGestureDetector = new GestureDetector(this);
        mFlipper = (ViewFlipper) findViewById(R.id.flipper1);

        initData();

        mDateAdapter = new DateAdapter(this);
        mDateAdapter.refreshData(mCurrentYear,
                mCurrentMonth, mDay, mCurrentWeek, mCurrentNum, mSelectPostion,
                mCurrentWeek == 1 ? true : false);
        addGridView();
        mDateAdapter.setFisrt(true);
        mDateAdapter.setPreMonth(mMonth);
        mDayNumbers = mDateAdapter.getDayNumbers();
        mGridView.setAdapter(mDateAdapter);
        mSelectPostion = mDateAdapter.getTodayPosition();
        mGridView.setSelection(mSelectPostion);
        mFlipper.addView(mGridView, 0);
        this.mFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
        this.mFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));

        mDateTv.setText(mMonths[mMonth - 1] + "月 " + mYear);
        mRefreshListView = (PinnedSectionListView) findViewById(R.id.refresh_listview);
        mAdapter = new JournalListAdapter(this);
        mRefreshListView.setAdapter(mAdapter);
    }

    public void initData() {
        Intent intent = this.getIntent();
        mDate = intent.getStringExtra("date");
        mLoguid = intent.getStringExtra("loguid");
        mTitleName = intent.getStringExtra("title_name");
        mTitleTv.setText(mTitleName + "的日志");
        // Date date = new Date(mDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        // mCurrentDate = sdf.format(mDate);
        mCurrentDate = mDate;
        mYear = Integer.parseInt(mCurrentDate.split("-")[0]);
        mMonth = Integer.parseInt(mCurrentDate.split("-")[1]);
        mDay = Integer.parseInt(mCurrentDate.split("-")[2]);
        mCurrentYear = mYear;
        // mCurrentMonth = mMonth - 1;// 默认月份进来要减去1
        mCurrentMonth = mMonth;
        mCurrentDay = mDay;
        getCalendar(mYear, mMonth);
        mWeekNum = getWeeksOfMonth();
        mCurrentNum = mWeekNum;
        if (mDayOfWeek == 7) {
            mWeek = mCurrentDay / 7 + 1;
        } else {
            if (mCurrentDay <= (7 - mDayOfWeek)) {
                mWeek = 1;
            } else {
                if ((mCurrentDay - (7 - mDayOfWeek)) % 7 == 0) {
                    mWeek = (mCurrentDay - (7 - mDayOfWeek)) / 7 + 1;
                } else {
                    mWeek = (mCurrentDay - (7 - mDayOfWeek)) / 7 + 2;
                }
            }
        }
        mCurrentWeek = mWeek;
        getCurrent();

    }

    @Override
    public void bindViewsListener() {
        mPreMonthIv.setOnClickListener(this);
        mNextMonthIv.setOnClickListener(this);
        mRefreshListView.setOnItemClickListener(this);
        mOkTv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(JournalPersonalDetailActivity.this, JournalPublishDetailActivity.class);
                setResult(10, intent);
                finish();
            }
        });
    }

    /**
     * 判断某年某月所有的星期数
     * 
     * @param year
     * @param month
     */
    public int getWeeksOfMonth(int year, int month) {
        // 先判断某月的第一天为星期几
        int preMonthRelax = 0;
        int dayFirst = getWhichDayOfWeek(year, month);
        int days = DateUtils.getDaysOfMonth(DateUtils.isLeapYear(year), month);
        if (dayFirst != 7) {
            preMonthRelax = dayFirst;
        }
        if ((days + preMonthRelax) % 7 == 0) {
            mWeeksOfMonth = (days + preMonthRelax) / 7;
        } else {
            mWeeksOfMonth = (days + preMonthRelax) / 7 + 1;
        }
        return mWeeksOfMonth;

    }

    /**
     * 判断某年某月的第一天为星期几
     * 
     * @param year
     * @param month
     * @return
     */
    public int getWhichDayOfWeek(int year, int month) {
        return DateUtils.getWeekdayOfMonth(year, month);

    }

    /**
     * @param year
     * @param month
     */
    public int getLastDayOfWeek(int year, int month) {
        return DateUtils.getWeekDayOfLastMonth(year, month,
                DateUtils.getDaysOfMonth(mIsLeapyear, month));
    }

    public void getCalendar(int year, int month) {
        mIsLeapyear = DateUtils.isLeapYear(year); // 是否为闰年
        mDaysOfMonth = DateUtils.getDaysOfMonth(mIsLeapyear, month); // 某月的总天数
        mDayOfWeek = DateUtils.getWeekdayOfMonth(year, month); // 某月第一天为星期几
    }

    public int getWeeksOfMonth() {
        // getCalendar(year, month);
        int preMonthRelax = 0;
        if (mDayOfWeek != 7) {
            preMonthRelax = mDayOfWeek;
        }
        if ((mDaysOfMonth + preMonthRelax) % 7 == 0) {
            mWeeksOfMonth = (mDaysOfMonth + preMonthRelax) / 7;
        } else {
            mWeeksOfMonth = (mDaysOfMonth + preMonthRelax) / 7 + 1;
        }
        return mWeeksOfMonth;
    }

    private void addGridView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        mGridView = new GridView(this);
        mGridView.setNumColumns(7);
        mGridView.setGravity(Gravity.CENTER_VERTICAL);
        mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mGridView.setVerticalSpacing(1);
        mGridView.setHorizontalSpacing(1);
        mGridView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return JournalPersonalDetailActivity.this.mGestureDetector.onTouchEvent(event);
            }
        });

        mGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                mSelectPostion = position;
                mDateAdapter.setSeclection(position);
                if (Integer.parseInt(mDayNumbers[position]) <= Integer.parseInt(mDayNumbers[6])) {
                    mMonth = mCurrentMonth;
                } else {
                    mMonth = mCurrentMonth - 1;
                }
                int year = mCurrentYear;
                if (mMonth == 0) {
                    mDateTv.setText(mMonths[mMonths.length - 1] + "月 " + --year);
                } else {
                    mDateTv.setText(mMonths[mMonth - 1] + "月 " + mCurrentYear);
                }

                mDate = mCurrentYear + "-" + mMonth + "-" + mDayNumbers[mDateAdapter.getSeclection()];
                mRefreshListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
                new ThreadUtil(JournalPersonalDetailActivity.this, JournalPersonalDetailActivity.this).start();
            }
        });
        mGridView.setLayoutParams(params);
    }

    @Override
    public boolean onDown(MotionEvent arg0) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        int index = -1;// 跨月下标
        int gvFlag = 0;
        // mDateAdapter.setSelect(false);
        if (e1.getX() - e2.getX() > 80) {
            // 向左滑
            addGridView();
            mCurrentWeek++;
            getCurrent();
            // mDateAdapter = new DateAdapter(this);
            mDateAdapter.refreshData(mCurrentYear,
                    mCurrentMonth, mDay, mCurrentWeek, mCurrentNum, mSelectPostion,
                    mCurrentWeek == 1 ? true : false);
            mDayNumbers = mDateAdapter.getDayNumbers();
            mGridView.setAdapter(mDateAdapter);
            gvFlag++;
            mFlipper.addView(mGridView, gvFlag);

            this.mFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
                    R.anim.push_left_in));
            this.mFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
                    R.anim.push_left_out));
            this.mFlipper.showNext();
            mFlipper.removeViewAt(0);

            if (Integer.parseInt(mDayNumbers[0]) > Integer.parseInt(mDayNumbers[6])) {
                if (mDateAdapter.getPreLogDay() != null) {
                    for (int i = 0; i < mDayNumbers.length; i++) {
                        for (int j = 0; j < mDateAdapter.getPreLogDay().length; j++) {
                            if (mDayNumbers[i].equals(mDateAdapter.getPreLogDay()[j])) {
                                mDateAdapter.setSeclection(i);
                                break;
                            }
                        }
                    }
                } else {
                    if (mDateAdapter.getLogDay() != null) {
                        for (int i = 0; i < mDayNumbers.length; i++) {
                            for (int j = 0; j < mDateAdapter.getLogDay().length; j++) {
                                if (mDayNumbers[i].equals(mDateAdapter.getLogDay()[j])) {
                                    mDateAdapter.setSeclection(i);
                                    break;
                                }
                            }
                        }
                    }
                }

            } else {
                if (mDateAdapter.getLogDay() != null) {
                    for (int i = 0; i < mDayNumbers.length; i++) {
                        for (int j = 0; j < mDateAdapter.getLogDay().length; j++) {
                            if (mDayNumbers[i].equals(mDateAdapter.getLogDay()[j])) {
                                mDateAdapter.setSeclection(i);
                                break;
                            }
                        }
                    }
                }
            }

            if (Integer.parseInt(mDayNumbers[mDateAdapter.getSeclection()]) > Integer.parseInt(mDayNumbers[6])) {
                mMonth = mCurrentMonth - 1;
            } else {
                mMonth = mCurrentMonth;
            }
            mDate = mCurrentYear + "-" + mMonth + "-" + mDayNumbers[mDateAdapter.getSeclection()];
            int year = mCurrentYear;
            if (mMonth == 0) {

                mDateTv.setText(mMonths[mMonths.length - 1] + "月 " + --year);
            } else {
                mDateTv.setText(mMonths[mMonth - 1] + "月 " + mCurrentYear);
            }
            mRefreshListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
            new ThreadUtil(JournalPersonalDetailActivity.this, JournalPersonalDetailActivity.this).start();

            return true;

        } else if (e1.getX() - e2.getX() < -80) {
            addGridView();
            mCurrentWeek--;
            getCurrent();
            mDateAdapter.refreshData(mCurrentYear,
                    mCurrentMonth, mDay, mCurrentWeek, mCurrentNum, mSelectPostion,
                    mCurrentWeek == 1 ? true : false);
            mDayNumbers = mDateAdapter.getDayNumbers();
            mGridView.setAdapter(mDateAdapter);
            gvFlag++;
            mFlipper.addView(mGridView, gvFlag);
            this.mFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
                    R.anim.push_right_in));
            this.mFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
                    R.anim.push_right_out));
            this.mFlipper.showPrevious();
            mFlipper.removeViewAt(0);

            if (Integer.parseInt(mDayNumbers[0]) > Integer.parseInt(mDayNumbers[6])) {
                if (mDateAdapter.getPreLogDay() != null) {
                    for (int i = 0; i < mDayNumbers.length; i++) {
                        for (int j = 0; j < mDateAdapter.getPreLogDay().length; j++) {
                            if (mDayNumbers[i].equals(mDateAdapter.getPreLogDay()[j])) {
                                mDateAdapter.setSeclection(i);
                                break;
                            }
                        }
                    }
                }
            } else {
                if (mDateAdapter.getLogDay() != null) {
                    for (int i = 0; i < mDayNumbers.length; i++) {
                        for (int j = 0; j < mDateAdapter.getLogDay().length; j++) {
                            if (mDayNumbers[i].equals(mDateAdapter.getLogDay()[j])) {
                                mDateAdapter.setSeclection(i);
                                break;
                            }
                        }
                    }
                }
            }

            if (Integer.parseInt(mDayNumbers[mDateAdapter.getSeclection()]) > Integer.parseInt(mDayNumbers[6])) {
                mMonth = mCurrentMonth - 1;

            } else {
                mMonth = mCurrentMonth;

            }
            mDate = mCurrentYear + "-" + mMonth + "-" + mDayNumbers[mDateAdapter.getSeclection()];
            int year = mCurrentYear;
            if (mMonth == 0) {
                mDateTv.setText(mMonths[mMonths.length - 1] + "月 " + --year);
            } else {
                mDateTv.setText(mMonths[mMonth - 1] + "月 " + mCurrentYear);
            }

            mRefreshListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
            new ThreadUtil(JournalPersonalDetailActivity.this, JournalPersonalDetailActivity.this).start();

            return true;
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent arg0) {

    }

    @Override
    public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent arg0) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent arg0) {
        return false;
    }

    /**
     * 重新计算当前的年月
     */
    public void getCurrent() {
        if (mCurrentWeek > mCurrentNum) {
            if (mCurrentMonth + 1 <= 12) {
                mCurrentMonth++;
            } else {
                mCurrentMonth = 1;
                mCurrentYear++;
            }
            mCurrentWeek = 1;
            mCurrentNum = getWeeksOfMonth(mCurrentYear, mCurrentMonth);
        } else if (mCurrentWeek == mCurrentNum) {
            if (getLastDayOfWeek(mCurrentYear, mCurrentMonth) == 6) {
            } else {
                if (mCurrentMonth + 1 <= 12) {
                    mCurrentMonth++;
                } else {
                    mCurrentMonth = 1;
                    mCurrentYear++;
                }
                mCurrentWeek = 1;
                mCurrentNum = getWeeksOfMonth(mCurrentYear, mCurrentMonth);
            }

        } else if (mCurrentWeek < 1) {
            if (mCurrentMonth - 1 >= 1) {
                mCurrentMonth--;
            } else {
                mCurrentMonth = 12;
                mCurrentYear--;
            }
            mCurrentNum = getWeeksOfMonth(mCurrentYear, mCurrentMonth);
            mCurrentWeek = mCurrentNum - 1;
        }
    }

    // 按上，下重新获取当前日期，1为上个月，2为下个月
    public void getCurrentDate(int type) {
        if (type == 1) {
            if (mCurrentMonth - 1 >= 1) {
                mCurrentMonth--;
            } else {
                mCurrentMonth = 12;
                mCurrentYear--;
            }
            mCurrentWeek = 1;
            mCurrentNum = getWeeksOfMonth(mCurrentYear, mCurrentMonth);
        } else {
            if (mCurrentMonth + 1 <= 12) {
                mCurrentMonth++;
            } else {
                mCurrentMonth = 1;
                mCurrentYear++;
            }
            mCurrentWeek = 1;
            mCurrentNum = getWeeksOfMonth(mCurrentYear, mCurrentMonth);

        }

        addGridView();
        mDateAdapter.refreshData(mCurrentYear,
                mCurrentMonth, 1, mCurrentWeek, mCurrentNum, mSelectPostion,
                mCurrentWeek == 1 ? true : false);
        mDayNumbers = mDateAdapter.getDayNumbers();
        mSelectPostion = mDateAdapter.getTodayPosition();
        mGridView.setAdapter(mDateAdapter);
        mDateAdapter.setSeclection(mSelectPostion);
        mGridView.setSelection(mSelectPostion);

        mFlipper.removeAllViews();
        mFlipper.addView(mGridView, 0);
        this.mFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
        this.mFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));

        mDateTv.setText(mMonths[mCurrentMonth - 1] + "月 " + mCurrentYear);

        mDate = mCurrentYear + "-" + mCurrentMonth + "-01";
        mMonth = mCurrentMonth;
        mRefreshListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
        new ThreadUtil(JournalPersonalDetailActivity.this, JournalPersonalDetailActivity.this).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // jumpWeek = 0;
    }

    public boolean getData() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());

            if (0 == mState) {
                mFristid = "";
                mLastid = "";
            }

            map.put("firstid", mFristid);
            map.put("lastid", mLastid);
            map.put("date", mDate);
            map.put("loguid", mLoguid);

            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.JOURNAL_PERSON_DETAIL, map);
            mListVO = gson.fromJson(jsonStrList, JournalListVO1.class);
            if (Constant.RESULT_CODE.equals(mListVO.getCode())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pre_month_iv:
                getCurrentDate(1);
                break;
            case R.id.next_month_iv:
                if (mCurrentMonth != mMonth) {
                    mDateTv.setText(mMonths[mMonth] + "月 " + mYear);
                    for (int i = 0; i < mDayNumbers.length; i++) {
                        if ("1".equals(mDayNumbers[i])) {
                            mDateAdapter.setSeclection(i);
                            break;
                        }
                    }
                    mMonth = mMonth + 1;
                    mDate = mCurrentYear + "-" + mMonth + "-" + mDayNumbers[mDateAdapter.getSeclection()];
                    mRefreshListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
                    new ThreadUtil(JournalPersonalDetailActivity.this, JournalPersonalDetailActivity.this).start();
                } else {
                    getCurrentDate(2);
                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> listView, View arg1, int position, long arg3) {

        if (mAdapter.mList.size() > 0 && mAdapter.getItemViewType((int) arg3) != -10) {
            JournalListVO1 vo = mAdapter.mList.get(mAdapter.mPinnedList.get((int) arg3));
            Intent intent = new Intent();
            intent.setClass(this, JournalPersonalDetailActivity.class);
            intent.putExtra("personal", "1");
            intent.putExtra("listvo", (Serializable) mAdapter.mList);
            intent.putExtra("logid", vo.getLogid());
            intent.setClass(this, JournalPublishDetailActivity.class);
            this.startActivityForResult(intent, 2);
        }
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        if (arg2 == null)
            return;
        List<JournalListVO1> list = (List<JournalListVO1>) arg2.getSerializableExtra("listvo");
        mAdapter.updateData(list);
    }

}
