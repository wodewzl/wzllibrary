
package com.bs.bsims.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.PaintDrawable;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.EXTAttendanceStatisticsDepartmentEAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.constant.Constant4DepName;
import com.bs.bsims.constant.Constant4Statistics;
import com.bs.bsims.interfaces.UpdateCallback;
import com.bs.bsims.model.StatisticsBossAttendanceIndexMonthVO;
import com.bs.bsims.model.StatisticsBossAttendanceIndexShow;
import com.bs.bsims.time.ScreenInfo;
import com.bs.bsims.time.WheelMain;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSRefreshListView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author peck
 * @Description: 10. 各部门下详细的考勤统计接口
 * @date 2015-6-25 下午2:09:54
 * @email 971371860@qq.com
 * @version V1.0
 */
public class EXTAttendanceStatisticsDepartmentEActivity extends BaseActivity
        implements OnClickListener, OnItemClickListener {

    private String TAG = "EXTAttendanceStatisticsDepartmentEActivity";
    private String jsonStr = "EXTAttendanceStatisticsDepartmentEActivity";
    private Context mContext;
    /**
     * 请求后台的参数 类型（0全部，1缺日志，2缺卡,3迟到，4早退，5事假，6病假，7(陪)产假，8公休假，9调休假，10婚假，11丧假）
     */
    private String type = "";
    /**
     * 10. 各部门下详细的考勤统计接口 日期选择(2015-06)
     */
    private String date = "";
    /**
     * 10. 各部门下详细的考勤统计接口 部门ID（必须传）不能为0
     */
    private String did = "";

    private StatisticsBossAttendanceIndexMonthVO mSBossAttendanceIndexMonthVO;

    private ArrayList<HashMap<String, Object>> listType;
    private ArrayList<HashMap<String, Object>> listDid;
    private ArrayList<HashMap<String, Object>> listDate;

    /**
     * 7. 考勤统计首页点击具体月份时下方展示接口
     */
    private EXTAttendanceStatisticsDepartmentEAdapter mEXTAttendanceStatisticsDepartmentEAdapter;

    private BSRefreshListView ac_ext_attendance_ss_departmente_listview;
    private TextView ac_ext_attendance_ss_departmente_allnum;
    private TextView ac_ext_attendance_ss_departmente_minus;

    private TextView mTitleName02, mTitleName03, mTitleName01;
    private LinearLayout mTitle02, mTitle03, mTitle01, mTitleLayout;
    private View mDivider02;
    private ImageView mSelectOne, mSelectTwo, mSelectThree;
    private BSPopupWindwos mPop;
    private BSPopupWindwos3 mPop3;

    private Map<String, String> mFixedMap;
    private Map<String, String> mValueKeyMap;
    public WheelMain wheelMain;
    private int mStatus = 0;
    List<StatisticsBossAttendanceIndexShow> mList = new ArrayList<StatisticsBossAttendanceIndexShow>();
    private TextView mListTitleTv;

    @Override
    public void baseSetContentView() {
        // TODO Auto-generated method stub
        mContext = this;
        View layout = View.inflate(this,
                R.layout.ac_ext_attendance_ss_departmente, null);
        mContentLayout.addView(layout);
        setFixedMap();
    }

    @Override
    public boolean getDataResult() {
        // TODO Auto-generated method stub
        return getDataMonth();
    }

    @Override
    public void updateUi() {
        // TODO Auto-generated method stub

    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        initTitle();
        mTitleTv.setText(this.getIntent().getStringExtra("type_name"));

        ac_ext_attendance_ss_departmente_listview = (BSRefreshListView) findViewById(R.id.ac_ext_attendance_ss_departmente_listview);

        mEXTAttendanceStatisticsDepartmentEAdapter = new EXTAttendanceStatisticsDepartmentEAdapter(
                mContext);
        ac_ext_attendance_ss_departmente_listview
                .setAdapter(mEXTAttendanceStatisticsDepartmentEAdapter);

        ac_ext_attendance_ss_departmente_allnum = (TextView) findViewById(R.id.ac_ext_attendance_ss_departmente_allnum);
        ac_ext_attendance_ss_departmente_minus = (TextView) findViewById(R.id.ac_ext_attendance_ss_departmente_minus);

        Intent getIntent = getIntent();
        if (TextUtils.isEmpty(date)) {
            date = getIntent.getStringExtra("date");
        }
        if (TextUtils.isEmpty(date)) {
            // date = DateUtils.getLastMonthYYYYMM();
            date = DateUtils.getTureMonthYYYYM();
        }

        String typeName = getIntent.getStringExtra("typeName");
        if (!TextUtils.isEmpty(typeName)) {
            mTitleName01.setText(typeName);
        }
        String dname = getIntent.getStringExtra("dname");
        if (!TextUtils.isEmpty(dname)) {
            mTitleName02.setText(dname);
        }

        mTitleName03.setText(date);

        initData();

        mTitleName01.setTextColor(Color.parseColor("#00A9FE"));
        mTitleName02.setTextColor(Color.parseColor("#00A9FE"));
        mTitleName03.setTextColor(Color.parseColor("#00A9FE"));
        mSelectThree
                .setBackgroundResource(R.drawable.ic_contacts_department_fragment_arrow_selected);
        mSelectTwo
                .setBackgroundResource(R.drawable.ic_contacts_department_fragment_arrow_selected);
        mSelectOne
                .setBackgroundResource(R.drawable.ic_contacts_department_fragment_arrow_selected);
        mListTitleTv = (TextView) findViewById(R.id.list_title_tv);
        mListTitleTv.setText("部门" + this.getIntent().getStringExtra("type_name") + "排行");
    }

    @Override
    public void bindViewsListener() {
        // TODO Auto-generated method stub

        mTitle01.setOnClickListener(this);
        mTitle02.setOnClickListener(this);
        mTitle03.setOnClickListener(this);
        ac_ext_attendance_ss_departmente_listview.setOnItemClickListener(this);

    }

    public void initData() {
        Intent getIntent = getIntent();
        if (TextUtils.isEmpty(type)) {
            if (getIntent.getStringExtra("type") != null) {
                type = getIntent.getStringExtra("type");
                if ("1".equals(type)) {
                    type = "2";
                } else if ("2".equals(type)) {
                    type = "1";
                }

                if (getIntent.getStringExtra("bossIndex") != null) {
                    if ("3".equals(type)) {
                        mTitleTv.setText("本月迟到");
                    } else if ("2".equals(type)) {
                        mTitleTv.setText("本月缺卡");
                    } else if ("1".equals(type)) {
                        mTitleTv.setText("本月缺日志");
                    }
                }

            }
        }
        if (TextUtils.isEmpty(did)) {
            if (getIntent.getStringExtra("did") != null) {
                did = getIntent.getStringExtra("did");
                if ("0".equals(did)) {
                    mEXTAttendanceStatisticsDepartmentEAdapter.setShowDepart(true);
                    this.findViewById(R.id.three_tv).setVisibility(View.GONE);
                    this.findViewById(R.id.total_tv).setVisibility(View.GONE);
                }
            }
        }

        if (TextUtils.isEmpty(date)) {
            if (getIntent.getStringExtra("date") != null) {
                date = getIntent.getStringExtra("date");
            }
        }

        if (TextUtils.isEmpty(date)) {
            // date = DateUtils.getLastMonthYYYYMM();
            date = DateUtils.getCurrentDate();
        }

    }

    public boolean getDataMonth() {
        // http://cp.beisheng.wang/api.php/Boss/attendanceList?date=2015-05&type=0&ftoken=RBDTZXGUMNDKkEwMkZFN0UyMTA1RgO0O0OO0O0O&did=1
        // type = "2";
        // date = "2015-05";
        // did = "1";

        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("type", type);
        paramsMap.put("did", did);
        paramsMap.put("date", date);
        paramsMap.put("userid", BSApplication.getInstance().getUserId());
        // paramsMap.put("ftoken", "RBDTZXGUMNDKkEwMkZFN0UyMTA1RgO0O0OO0O0O");
        paramsMap.put("ftoken", BSApplication.getInstance().getmCompany());
        String url = BSApplication.getInstance().getHttpTitle()
                + Constant4Statistics.ATTENDANCES_BOSS_DEPARTMENTE_PATH;
        // String url = "http://cp.beisheng.wang/"
        // + Constant4Statistics.ATTENDANCES_BOSS_DEPARTMENTE_PATH;

        try {
            // paramsMap.put("userid", BSApplication.getInstance().getUserId());
            // paramsMap.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance()
            // .getmCompany());
            String jsonStr = HttpClientUtil.getRequest(url, paramsMap);
            Gson gson = new Gson();
            mSBossAttendanceIndexMonthVO = gson.fromJson(jsonStr,
                    StatisticsBossAttendanceIndexMonthVO.class);
            if (Constant.RESULT_CODE.equals(mSBossAttendanceIndexMonthVO
                    .getCode())) {
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
        // mHeadLayout.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);
        ac_ext_attendance_ss_departmente_listview.onRefreshComplete();

        ac_ext_attendance_ss_departmente_allnum
                .setText(mSBossAttendanceIndexMonthVO.getAllnum());
        ac_ext_attendance_ss_departmente_minus.setText(getResources()
                .getString(R.string.attendances_ss_departmente_top2)
                + mSBossAttendanceIndexMonthVO.getMinus());

        CommonUtils.setDrableRight(ac_ext_attendance_ss_departmente_minus,
                mSBossAttendanceIndexMonthVO.getContrast(), mContext);

        mList = mSBossAttendanceIndexMonthVO
                .getArray();

        mEXTAttendanceStatisticsDepartmentEAdapter.updateData(mList);
        ac_ext_attendance_ss_departmente_listview.setVisibility(View.VISIBLE);
        ac_ext_attendance_ss_departmente_minus.setVisibility(View.VISIBLE);

    }

    @Override
    public void executeFailure() {
        ac_ext_attendance_ss_departmente_listview.onRefreshComplete();
        // mHeadLayout.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);
        mListTitleTv.setVisibility(View.GONE);

        if (mSBossAttendanceIndexMonthVO != null) {
            mLoading.setVisibility(View.GONE);
            mContentLayout.setVisibility(View.VISIBLE);
            mLoadingLayout.setVisibility(View.GONE);

            ac_ext_attendance_ss_departmente_listview.setVisibility(View.GONE);
            /**
             * 当没有数据的时候，合计不显示内容 排比不显示
             */
            ac_ext_attendance_ss_departmente_allnum.setText("");
            ac_ext_attendance_ss_departmente_minus.setVisibility(View.GONE);
        } else {
            super.executeFailure();
        }
    }

    public void initTitle() {

        mTitle01 = (LinearLayout) findViewById(R.id.title01);
        mDivider02 = findViewById(R.id.devider_02);
        mTitle01.setVisibility(View.VISIBLE);
        mDivider02.setVisibility(View.VISIBLE);
        mTitle02 = (LinearLayout) findViewById(R.id.title02);
        mTitle03 = (LinearLayout) findViewById(R.id.title03);
        mTitleLayout = (LinearLayout) findViewById(R.id.title_layout);
        mSelectOne = (ImageView) findViewById(R.id.select_icon01);
        mSelectTwo = (ImageView) findViewById(R.id.select_icon02);
        mSelectThree = (ImageView) findViewById(R.id.select_icon03);

        mTitleName01 = (TextView) findViewById(R.id.title_name_01);
        mTitleName02 = (TextView) findViewById(R.id.title_name_02);
        mTitleName03 = (TextView) findViewById(R.id.title_name_03);
        mTitleName01.setText("请假");
        mTitleName02.setText("全部部门");
        mTitleName03.setText("近半年");

        // mAddByDepartmentAdapter = new AddByDepartmentAdapter(this, this,
        // R.layout.item_contacts_department_tree_view, true, true);
        // mDepartmentUtis = new DepartmentMoreUtis(this,
        // ResultVO.getInstance(),
        // mHandler, false, true);
        // mAddByDepartmentAdapter.mfilelist = mDepartmentUtis
        // .getPdfOutlinesCount();
        //
        mPop = new BSPopupWindwos(mContext, mTitleLayout);

    }

    private class BSPopupWindwos extends PopupWindow {
        private Context mContext;
        private ListView popListView;
        private LinearLayout mTitleLayout03, mTitlePopLayout04;
        private List<HashMap<String, Object>> allList;
        private SimpleAdapter adapter;

        private int currentViewID;

        public BSPopupWindwos(Context context, View parent) {
            this.mContext = context;
            View view = View.inflate(context, R.layout.ext_pop_home_task_hp,
                    null);
            popListView = new ListView(mContext);

            int month = Integer
                    .parseInt(DateUtils.getCurrentDate().split("-")[1]);
            allList = new ArrayList<HashMap<String, Object>>();
            for (int i = 1; i <= month; i++) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("month", i + "月");
                allList.add(map);
            }

            adapter = new SimpleAdapter(mContext, allList,
                    R.layout.dropdown_month_item, new String[] {
                            "month"
                    },
                    new int[] {
                            R.id.textview
                    });

            popListView.setAdapter(adapter);
            popListView.setDivider(null);

            Display currDisplay = getWindowManager().getDefaultDisplay();
            // 获取屏幕当前分辨率
            int displayWidth = currDisplay.getWidth();
            int displayHeight = currDisplay.getHeight();
            // popupWindow.setWidth(displayWidth / 2);

            // this.setWidth(parent.getWidth());
            this.setWidth(LayoutParams.MATCH_PARENT);
            // this.setHeight(LayoutParams.WRAP_CONTENT);
            this.setHeight(displayHeight / 2);
            this.setContentView(popListView);
            // this.setFocusable(false);
            setFocusable(true);
            this.setOutsideTouchable(true);
            this.setBackgroundDrawable(new PaintDrawable());
            setBackgroundDrawable(new BitmapDrawable());

            popListView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                        int position, long id) {
                    TextView tv = (TextView) view.findViewById(R.id.textview);
                    // String month = tv.getText().toString().split("月")[0];
                    String month = tv.getText().toString();

                    int currentId = getCurrentViewID();
                    switch (currentId) {
                        case R.id.title01:
                            mTitleName01.setText(month);
                            type = mValueKeyMap.get(month);
                            break;
                        case R.id.title02:
                            mTitleName02.setText(month);
                            did = Constant4DepName.depValueKeyMap.get(month);
                            break;
                        case R.id.title03:
                            mTitleName03.setText(month);
                            break;
                    }
                    ac_ext_attendance_ss_departmente_listview
                            .changeHeaderViewByState(BSRefreshListView.REFRESHING);
                    new ThreadUtil(mContext, (UpdateCallback) mContext).start();
                    mPop.dismiss();
                }
            });
        }

        public void updateData(List<HashMap<String, Object>> list) {
            allList.clear();
            allList.addAll(list);
            adapter.notifyDataSetChanged();
        }

        public int getCurrentViewID() {
            return currentViewID;
        }

        public void setCurrentViewID(int currentViewID) {
            this.currentViewID = currentViewID;
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title01:
                // mTitleName01.setTextColor(Color.parseColor("#00A9FE"));
                // mTitleName02.setTextColor(Color.parseColor("#999999"));
                // mTitleName03.setTextColor(Color.parseColor("#999999"));
                // mSelectThree
                // .setBackgroundResource(R.drawable.ic_contacts_department_fragment_arrow_selected);
                // mSelectTwo
                // .setBackgroundResource(R.drawable.ic_contacts_department_fragment_arrow_default1);
                // mSelectOne
                // .setBackgroundResource(R.drawable.ic_contacts_department_fragment_arrow_default1);

                ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
                Object[] key = mFixedMap.keySet().toArray();
                Arrays.sort(key);
                // for (String objec : mFixedMap.values()) {
                // HashMap<String, Object> map = new HashMap<String, Object>();
                // map.put("month", objec);
                //
                // list.add(map);
                // }

                for (int i = 1; i <= mFixedMap.size(); i++) {

                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("month", mFixedMap.get(i + ""));
                    list.add(map);
                }

                mPop3 = new BSPopupWindwos3(this, mTitleLayout);
                mPop3.currentView(this, 1);
                if (!mPop3.isShowing()) {
                    mPop3.updateData(list);
                    mPop3.setCurrentViewID(v.getId());
                    mPop3.showAsDropDown(mTitleLayout,
                            mTitleLayout.getLayoutParams().width / 2, 0);
                }
                break;
            case R.id.title02:
                // mTitleName02.setTextColor(Color.parseColor("#00A9FE"));
                // mSelectOne
                // .setBackgroundResource(R.drawable.ic_contacts_department_fragment_arrow_selected);

                mPop3 = new BSPopupWindwos3(this, mTitleLayout);
                mPop3.currentView(this, 2);
                if (!mPop3.isShowing()) {
                    mPop3.updateData(listDid);
                    mPop3.setCurrentViewID(v.getId());
                    mPop3.showAsDropDown(mTitleLayout,
                            mTitleLayout.getLayoutParams().width / 2, 0);
                }
                break;
            case R.id.title03:
                // mTitleName03.setTextColor(Color.parseColor("#00A9FE"));
                // mSelectTwo
                // .setBackgroundResource(R.drawable.ic_contacts_department_fragment_arrow_selected);
                mPop3 = new BSPopupWindwos3(mContext, mTitleLayout);
                mStatus = 2;
                mPop3.currentView(this, 3);
                if (!mPop3.isShowing()) {
                    // mPop3.setCurrentViewID(v.getId());
                    mPop3.showAsDropDown(mTitleLayout,
                            mTitleLayout.getLayoutParams().width / 2, 0);
                }
                break;
        }
    }

    /**
     * 初始化 类型（0全部，1缺日志，2缺卡,3迟到，4早退，5事假，6病假，7(陪)产假，8公休假，9调休假，10婚假，11丧假）
     */
    private void setFixedMap() {
        // TODO Auto-generated method stub management
        mFixedMap = new HashMap<String, String>();
        mValueKeyMap = new HashMap<String, String>();
        // mFixedMap.put("0", "全部");
        mFixedMap.put("1", "缺日志");
        mFixedMap.put("2", "缺卡");
        mFixedMap.put("3", "迟到");
        mFixedMap.put("4", "早退");
        mFixedMap.put("5", "事假");
        mFixedMap.put("6", "病假");
        mFixedMap.put("7", "(陪)产假");
        mFixedMap.put("8", "公休假");
        mFixedMap.put("9", "调休假");
        mFixedMap.put("10", "婚假");
        mFixedMap.put("11", "丧假");
        for (String tring : mFixedMap.keySet()) {
            mValueKeyMap.put(mFixedMap.get(tring), tring);
        }

        /**
         * 部门选择中，不能有全部部门
         */
        // Constant4DepName.depKeyValueMap.put("0", "全部");
        // Constant4DepName.depValueKeyMap.put("全部", "0");
        listDid = new ArrayList<HashMap<String, Object>>();
        for (String objec : Constant4DepName.depKeyValueMap.values()) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            // map.put("month", objec + "月");
            if (getResources().getString(
                    R.string.attendances_ss_linechart_select1)
                    .equalsIgnoreCase(objec)) {
                continue;
            }
            map.put("month", objec);
            listDid.add(map);
        }

    }

    public View initDateView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View timepickerview = inflater.inflate(R.layout.timepicker, null);
        ScreenInfo screenInfo = new ScreenInfo(this);
        // final WheelMain wheelMain = new WheelMain(timepickerview, false,
        // false);
        wheelMain = new WheelMain(timepickerview, false, false);
        wheelMain.screenheight = screenInfo.getHeight();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        wheelMain.initDateTimePicker(year, month, 0, 0, 0);
        return timepickerview;
    }

    private class BSPopupWindwos3 extends PopupWindow implements
            OnClickListener {
        private ListView mListViewDepartment, mLVType, mLVDateType;

        private LinearLayout mTitleLayout01, mTitleLayout02, mTitleLayout03;
        private Button mOkBt;

        private List<HashMap<String, Object>> allList;
        private SimpleAdapter adapter;

        private int currentViewID;

        public BSPopupWindwos3(Context context, View parent) {
            // View view = View.inflate(context, R.layout.approval_pop_item_03,
            // null);
            View view = View.inflate(context, R.layout.ext_pop_statistics,
                    null);
            view.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View arg0, MotionEvent arg1) {

                    dismiss();
                    return false;
                }
            });
            mTitleLayout01 = (LinearLayout) view
                    .findViewById(R.id.ext_pop_statistics_title_layout_01);
            mTitleLayout02 = (LinearLayout) view
                    .findViewById(R.id.ext_pop_statistics_title_layout_02);
            mTitleLayout03 = (LinearLayout) view
                    .findViewById(R.id.ext_pop_statistics_title_layout_03);

            mTitleLayout03.addView(initDateView());

            mOkBt = (Button) view.findViewById(R.id.ok_bt);
            mOkBt.setOnClickListener(new OnClickListener() {
                // && !mAddByDepartmentAdapter.mfilelist.get(i).isExpanded()
                @Override
                public void onClick(View v) {

                    if (mStatus == 1) {
                    } else {
                        String date = wheelMain.getTime().substring(0,
                                wheelMain.getTime().lastIndexOf("-"));
                        mTitleName03.setText(date);
                        match(4, date);
                    }
                    dismiss();
                }

            });

            mLVType = new ListView(context);
            mLVDateType = new ListView(context);

            mTitleLayout01.addView(mLVType);
            mTitleLayout02.addView(mLVDateType);

            allList = new ArrayList<HashMap<String, Object>>();

            adapter = new SimpleAdapter(context, allList,
                    R.layout.dropdown_month_item, new String[] {
                            "month"
                    },
                    new int[] {
                            R.id.textview
                    });

            mLVType.setAdapter(adapter);
            mLVType.setDivider(null);
            mLVDateType.setAdapter(adapter);
            mLVDateType.setDivider(null);

            mLVType.setOnItemClickListener(myOnItemClickListener);
            mLVDateType.setOnItemClickListener(myOnItemClickListener);

            view.startAnimation(AnimationUtils.loadAnimation(context,
                    R.anim.fade_in));
            setWidth(LayoutParams.MATCH_PARENT);
            setHeight(LayoutParams.MATCH_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            update();
        }

        public void currentView(Context context, int type) {

            if (type == 1) {
                mTitleLayout01.setVisibility(View.VISIBLE);
                mTitleLayout02.setVisibility(View.GONE);
                mTitleLayout03.setVisibility(View.GONE);
            } else if (type == 2) {

                mTitleLayout02.setVisibility(View.VISIBLE);
                mTitleLayout03.setVisibility(View.GONE);
                mTitleLayout01.setVisibility(View.GONE);
            } else {
                mTitleLayout03.setVisibility(View.VISIBLE);
                mTitleLayout02.setVisibility(View.GONE);
                mTitleLayout01.setVisibility(View.GONE);
                mOkBt.setVisibility(View.VISIBLE);
            }
        }

        public void updateData(List<HashMap<String, Object>> list) {
            allList.clear();
            allList.addAll(list);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
        }

        public int getCurrentViewID() {
            return currentViewID;
        }

        public void setCurrentViewID(int currentViewID) {
            this.currentViewID = currentViewID;
        }

        OnItemClickListener myOnItemClickListener = new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                TextView tv = (TextView) view.findViewById(R.id.textview);
                String month = tv.getText().toString();
                int currentId = getCurrentViewID();
                switch (currentId) {
                    case R.id.title01:
                        mTitleName01.setText(month);
                        type = mValueKeyMap.get(month);
                        break;
                    case R.id.title02:
                        mTitleName02.setText(month);
                        did = Constant4DepName.depValueKeyMap.get(month);
                        break;
                    case R.id.title03:
                        mTitleName03.setText(month);
                        break;
                }
                ac_ext_attendance_ss_departmente_listview
                        .changeHeaderViewByState(BSRefreshListView.REFRESHING);
                new ThreadUtil(mContext, (UpdateCallback) mContext).start();
                dismiss();
            }
        };

    }

    public void match(int key, String value) {
        switch (key) {
            case 4:
                date = value;
                break;

            default:
                break;
        }
        ac_ext_attendance_ss_departmente_listview
                .changeHeaderViewByState(BSRefreshListView.REFRESHING);
        new ThreadUtil(mContext, (UpdateCallback) mContext).start();
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Intent intent = new Intent();
        intent.setClass(this, EXTWorkAttendanceDetailActivity.class);
        intent.putExtra("userid", mList.get((int) arg3).getUid());
        intent.putExtra("date", date);
        mContext.startActivity(intent);
    }

}
