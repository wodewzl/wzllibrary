
package com.bs.bsims.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.bs.bsims.R;
import com.bs.bsims.adapter.ApprovalAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.interfaces.UpdateCallback;
import com.bs.bsims.model.ApprovalResultVO;
import com.bs.bsims.model.ApprovalVO;
import com.bs.bsims.model.CustomApprovalListVO;
import com.bs.bsims.model.DepartmentAndEmployeeVO;
import com.bs.bsims.model.ResultVO;
import com.bs.bsims.model.TreeVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.utils.CommonUtils.ResultCallback;
import com.bs.bsims.view.BSIndexEditText;
import com.bs.bsims.view.BSPopupWindowsTitle;
import com.bs.bsims.view.BSPopupWindowsTitle.TreeCallBack;
import com.bs.bsims.view.BSRefreshListView;
import com.bs.bsims.view.BSRefreshListView.OnRefreshListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ApprovalViewActivity extends BaseActivity implements OnItemClickListener, UpdateCallback, OnClickListener {
    private ApprovalAdapter mAdapter;
    private BSRefreshListView mBsRefreshListView;
    private String mUid;
    private ApprovalResultVO mApprovalResultVO;
    // listView
    private View mFootLayout;
    private TextView mMoreTextView;
    private ProgressBar mProgressBar;
    // 0为首次,1为上拉刷新 ，2为下拉刷新
    private int mState = 0;

    private String mShowTitle = "false";
    private LinearLayout mThreeTitleLayout;

    private int mType = 0;

    private View mDivider02;
    private LinearLayout mTitle02, mTitle03, mTitle01, mTitleLayout;
    private TextView mTitleName02, mTitleName03, mTitleName01;
    private ImageView mSelectOne, mSelectTwo, mSelectThree;
    private BSPopupWindwos mPop;

    private String modeid, isall, bigtypeid, smalltypeid, statusid, refresh, mKeyword, mIsBoss, mBossIndex = "", findUid, date;

    private BSIndexEditText mBSBsIndexEditText;
    private LinearLayout mNoContentLyaout;

    private LinearLayout mSerachLayout;
    private View mThreeTitleOne;
    private CustomApprovalListVO mCustomApprovalVO;
    private String[] mCustomArray;

    private BSPopupWindowsTitle mBsPopupWindowsTitle;
    private boolean mFlag = true;
    private Boolean canClickFlag = true;// 解决连续点“更多”可能会出现的异常
    private TextView mNoReadTv;
    private String mToDo;
    private String mFirstId, mLastId;
    private String mUnread;

    private String mArray[];
    private int[] mIcon;
    private boolean mPopFlag = true;
    private boolean mPopLeftFlag = true;
    private String[] mArrayPopName = {
            "请假", "物资", "加班", "费用", "考勤", "其他"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.approval_view, null);
        mContentLayout.addView(layout);
    }

    @Override
    public void initView() {

        mTitleTv.setText(R.string.view_approval);
        mThreeTitleLayout = (LinearLayout) findViewById(R.id.three_title_layout);

        mBsRefreshListView = (BSRefreshListView) findViewById(R.id.lv_approval);
        mAdapter = new ApprovalAdapter(this);
        mBsRefreshListView.setAdapter(mAdapter);
        initFoot();

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
        mTitleName01.setText("全部审批");
        mTitleName02.setText("全部类型");
        mTitleName03.setText("全部状态");

        mBSBsIndexEditText = (BSIndexEditText) findViewById(R.id.edit_single_search);

        mNoContentLyaout = (LinearLayout) findViewById(R.id.no_content_layout);

        mSerachLayout = (LinearLayout) findViewById(R.id.serach_layout);
        mThreeTitleOne = findViewById(R.id.three_titie_one);
        mNoReadTv = (TextView) findViewById(R.id.no_read_tv);
        mTitleLayout = (LinearLayout) findViewById(R.id.title_layout);
        initData();
        setLeadClass("ApprovalViewActivity");

        mPop = new BSPopupWindwos(this, mTitleLayout);
    }

    public void initData() {
        Intent intent = getIntent();
        mUid = intent.getStringExtra("uid");
        mShowTitle = intent.getStringExtra("showTitle");
        modeid = intent.getStringExtra("modeid");
        mIsBoss = intent.getStringExtra("isboss");
        if (intent.getStringExtra("modeid") != null) {
            modeid = "0";
        }
        isall = intent.getStringExtra("isall");
        if (isall == null) {
            isall = BSApplication.getInstance().getUserFromServerVO().getManagement();
        }

        // boss首界面下方进入
        if (intent.getStringExtra("bossIndex") != null) {
            mBossIndex = intent.getStringExtra("bossIndex");
            mSerachLayout.setVisibility(View.GONE);
            mThreeTitleOne.setVisibility(View.GONE);
            mTitleTv.setText("昨日请假");
        }

        if (intent.getStringExtra("bigtypeid") != null) {
            bigtypeid = intent.getStringExtra("bigtypeid");
            mSerachLayout.setVisibility(View.GONE);
            mThreeTitleOne.setVisibility(View.GONE);

            if (intent.getStringExtra("date") != null) {
                date = intent.getStringExtra("date");
                if ("1".equals(bigtypeid)) {
                    if (date.equals(DateUtils.getCurrentDate().substring(0, DateUtils.getCurrentDate().lastIndexOf("-")))) {
                        mTitleTv.setText("本月请假");
                    } else {
                        mTitleTv.setText("请假");
                    }

                } else if ("3".equals(bigtypeid)) {
                    if (date.equals(DateUtils.getCurrentDate().substring(0, DateUtils.getCurrentDate().lastIndexOf("-")))) {
                        mTitleTv.setText("本月加班");
                    } else {
                        mTitleTv.setText("加班");
                    }

                }
            }
        }

        if (intent.getStringExtra("smalltypeid") != null) {
            smalltypeid = intent.getStringExtra("smalltypeid");
        }

        if (intent.getStringExtra("findUid") != null) {
            findUid = intent.getStringExtra("findUid");
        }

        // 首页进来隐藏收索条件
        if (this.getIntent().getStringExtra("msg") != null) {
            mBSBsIndexEditText.setVisibility(View.GONE);
            mTitleLayout.setVisibility(View.GONE);
        }

        mToDo = this.getIntent().getStringExtra("todo");
        mUnread = this.getIntent().getStringExtra("unread");
        getApprovalTypeList();

        // 如果是boss跳转查看请假
        if (intent.getStringExtra("is_qj") != null) {
            mTitleName02.setText("请假");
            mTitleName02.setTextColor(Color.parseColor("#00A9FE"));
            mSelectTwo.setBackgroundResource(R.drawable.ic_contacts_department_fragment_arrow_selected);
            bigtypeid ="1";
            isall="1";
        }
    }

    @Override
    public void bindViewsListener() {
        bindRefreshListener();
        mBsRefreshListView.setOnItemClickListener(this);
        mOkTv.setOnClickListener(this);
        mTitle01.setOnClickListener(this);
        mTitle02.setOnClickListener(this);
        mTitle03.setOnClickListener(this);
        mNoReadTv.setOnClickListener(this);

        mBSBsIndexEditText.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) mBSBsIndexEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    ApprovalViewActivity.this
                                            .getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);

                    mKeyword = mBSBsIndexEditText.getText().toString();
                    match(5, mKeyword);
                    cleanData();
                    mBsRefreshListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
                    new ThreadUtil(ApprovalViewActivity.this, ApprovalViewActivity.this).start();
                    return true;
                }
                return false;
            }
        });
        if (CommonUtils.getLimitsPublish(Constant.LIMITS_PUBLISH001)) {
            mOkTv.setText("发布");
            mOkTv.setVisibility(View.VISIBLE);
        } else {
            mOkTv.setVisibility(View.GONE);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra("alid", mAdapter.mList.get((int) id).getId());
        intent.putExtra("uid", BSApplication.getInstance().getUserId());
        String type = mAdapter.mList.get((int) id).getType();
        intent.putExtra("type", type);
        if ("1".equals(type)) {
            intent.setClass(this, ApprovalLeaveDetailActivity.class);
        } else if ("2".equals(type)) {
            intent.setClass(this, ApprovalSuppliesDetailActivity.class);
        } else if ("3".equals(type)) {
            intent.setClass(this, ApprovalOvertimeDetailActivity.class);
        } else if ("4".equals(type)) {
            intent.setClass(this, ApprovalFeeApplyDetailActivity.class);
        } else if ("5".equals(type)) {
            intent.setClass(this, ApprovalAttendanceDetailActivity.class);
        } else {
            intent.setClass(this, ApprovalCustomDetailActivity.class);
        }
        this.startActivity(intent);
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    @Override
    public void updateUi() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void executeSuccess() {
        super.executeSuccess();
        if (1 == mState) {
            mAdapter.updateDataFrist(mApprovalResultVO.getArray());
        } else if (2 == mState) {
            mAdapter.updateDataLast(mApprovalResultVO.getArray());
        } else {
            mAdapter.updateData(mApprovalResultVO.getArray());
        }

        mAdapter.notifyDataSetChanged();
        mBsRefreshListView.onRefreshComplete();
        mLoading.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
        if (mState != 1) {
            footViewIsVisibility(mApprovalResultVO.getArray());
        }
        mState = 0;
        canClickFlag = true;
        mFlag = false;
    }

    @Override
    public void executeFailure() {

        if (mApprovalResultVO != null) {
            if (mState == 0) {
                mBsRefreshListView.setVisibility(View.GONE);
                mNoContentLyaout.setVisibility(View.VISIBLE);

                mLoading.setVisibility(View.GONE);
                mContentLayout.setVisibility(View.VISIBLE);
                mLoadingLayout.setVisibility(View.GONE);
            }

        } else {
            CommonUtils.setNonetIcon(this, mLoading, this);
        }

        mAdapter.notifyDataSetChanged();
        mBsRefreshListView.onRefreshComplete();
        footViewIsVisibility(mAdapter.mList);
        canClickFlag = true;
        if (mCustomApprovalVO == null || mCustomApprovalVO.getArray() == null) {
            mFlag = true;
        }
        else {
            mFlag = false;
        }
    }

    // 加载更多数据
    public void initFoot() {
        mFootLayout = LayoutInflater.from(this).inflate(R.layout.listview_bottom_more, null);
        mMoreTextView = (TextView) mFootLayout.findViewById(R.id.txt_loading);
        mMoreTextView.setText("更多");
        mProgressBar = (ProgressBar) mFootLayout.findViewById(R.id.progressBar);
        mFootLayout.setVisibility(View.GONE);
        mBsRefreshListView.addFooterView(mFootLayout);
    }

    protected void footViewIsVisibility(List<ApprovalVO> datas) {
        if (mApprovalResultVO == null) {
            return;
        }
        if (mApprovalResultVO.getCount() == null) {
            return;
        }
        if (Integer.parseInt(mApprovalResultVO.getCount()) <= 15) {
            mFootLayout.setVisibility(View.GONE);
        } else {
            mFootLayout.setVisibility(View.VISIBLE);
            mMoreTextView.setText("更多");
            mProgressBar.setVisibility(View.GONE);
        }
    }

    public void bindRefreshListener() {
        mBsRefreshListView.setonRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                mState = 1;
                refresh = Constant.FIRSTID;
                new ThreadUtil(ApprovalViewActivity.this, ApprovalViewActivity.this).start();
            }
        });
        mFootLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (canClickFlag) {
                    canClickFlag = false;
                    mMoreTextView.setText("正在加载...");
                    mProgressBar.setVisibility(View.VISIBLE);
                    mState = 2;
                    refresh = Constant.LASTID;
                    new ThreadUtil(ApprovalViewActivity.this, ApprovalViewActivity.this).start();
                }
            }
        });
    }

    public void geRefrsh() {

        if (0 == mState) {
            mFirstId = "";
            mLastId = "";
        } else if (1 == mState) {
            if (mAdapter.mList.size() > 0) {
                String id = mAdapter.mList.get(0).getTaid();
                mFirstId = id;
                mLastId = "";
            } else {
                mFirstId = "";
                mLastId = "";
            }
        } else if (2 == mState) {
            String id = mAdapter.mList.get(mAdapter.mList.size() - 1).getTaid();
            mLastId = id;
            mFirstId = "";
        }
    }

    public boolean getData() {
        geRefrsh();
        try {
            if (mFlag) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("userid", BSApplication.getInstance().getUserId());
                map.put("iscustom", "1");
                map.put("isappeal", "1");
                map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
                String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.APPROVAL_PUBLIC_LIST, map);
                Gson gson = new Gson();
                mCustomApprovalVO = gson.fromJson(jsonStr, CustomApprovalListVO.class);
            }

            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            map.put("isall", isall);
            map.put("statusid", statusid);
            map.put("modeid", modeid);
            map.put("bigtypeid", bigtypeid);
            map.put("smalltypeid", smalltypeid);
            map.put("keyword", mKeyword);
            map.put("lastid", mLastId);
            map.put("firstid", mFirstId);

            map.put("isboss", mIsBoss);
            map.put("bossIndex", mBossIndex);
            map.put("findUid", findUid);
            map.put("date", date);

            map.put("unread", mUnread);
            map.put("todo", mToDo);

            String jsonStr1 = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.MY_APPROVAL_URL, map);
            mApprovalResultVO = gson.fromJson(jsonStr1, ApprovalResultVO.class);
            // if (mFlag) {
            // getApprovalTypeList();
            // }
            if (Constant.RESULT_CODE.equals(mApprovalResultVO.getCode()) && Constant.RESULT_CODE.equals(mCustomApprovalVO.getCode())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void getApprovalTypeList() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("userid", BSApplication.getInstance().getUserId());
                    map.put("iscustom", "1");
                    map.put("isappeal", "1");
                    map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
                    String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.APPROVAL_PUBLIC_LIST, map);
                    Gson gson = new Gson();
                    mCustomApprovalVO = gson.fromJson(jsonStr, CustomApprovalListVO.class);

                    if (HttpClientUtil.isNetworkConnected(ApprovalViewActivity.this)) {
                        if (mCustomApprovalVO == null) {
                            if (Constant.RESULT_CODE400.equals(mCustomApprovalVO.getCode()))
                                return;
                            getApprovalTypeList();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.title01:
                mTitleName01.setTextColor(Color.parseColor("#00A9FE"));
                mTitleName02.setTextColor(Color.parseColor("#999999"));
                mTitleName03.setTextColor(Color.parseColor("#999999"));

                mSelectOne.setBackgroundResource(R.drawable.ic_contacts_department_fragment_arrow_selected);
                mSelectTwo.setBackgroundResource(R.drawable.ic_contacts_department_fragment_arrow_default1);
                mSelectThree.setBackgroundResource(R.drawable.ic_contacts_department_fragment_arrow_default1);

                mPop.currentView(this, 1);
                if (!mPop.isShowing()) {
                    mPop.showAsDropDown(mTitleLayout, mTitleLayout.getLayoutParams().width / 2, 0);
                } else {
                    mPop.dismiss();
                }
                mTitleName01.setText("全部审批");
                mTitleName02.setText("全部类型");
                mTitleName03.setText("全部状态");
                modeid = "";
                break;
            case R.id.title02:

                mTitleName02.setTextColor(Color.parseColor("#00A9FE"));
                mSelectTwo.setBackgroundResource(R.drawable.ic_contacts_department_fragment_arrow_selected);

                if (mBsPopupWindowsTitle != null) {
                    mBsPopupWindowsTitle.showPopupWindow(mTitleLayout);
                } else {
                    if (Constant.RESULT_CODE.equals(mCustomApprovalVO.getCode())) {
                        mBsPopupWindowsTitle = new BSPopupWindowsTitle(this, initPopDowns(), callback);
                        mBsPopupWindowsTitle.showPopupWindow(mTitleLayout);
                    }
                }

                bigtypeid = "";
                smalltypeid = "";
                break;
            case R.id.title03:
                mTitleName03.setTextColor(Color.parseColor("#00A9FE"));
                mSelectThree.setBackgroundResource(R.drawable.ic_contacts_department_fragment_arrow_selected);
                mPop.currentView(this, 3);
                if (!mPop.isShowing()) {
                    mPop.showAsDropDown(mTitleLayout, mTitleLayout.getLayoutParams().width / 2, 0);
                } else {
                    mPop.dismiss();
                }
                statusid = "";

                break;

            case R.id.txt_comm_head_right:
                // Intent intent = new Intent();
                // intent.putExtra("type", "1");
                // intent.setClass(this, ApprovalListActivity.class);
                // startActivity(intent);
                initPopLeft();
                break;

            case R.id.no_read_tv:
                Intent noReadIntent = new Intent();
                noReadIntent.putExtra("isall", CommonUtils.getLimitsSpecial(Constant.LIMITS_SPECIAL001));
                noReadIntent.putExtra("isboss", BSApplication.getInstance().getUserFromServerVO().getIsboss());
                noReadIntent.putExtra("modeid", "0");
                noReadIntent.putExtra("type", 7);
                noReadIntent.setClass(this, AllNoReadActivity.class);
                startActivity(noReadIntent);
                break;
            default:
                break;
        }
    }

    // 右上角添加回调函数
    ResultCallback mCallback = new ResultCallback() {
        @Override
        public void callback(String str, int position) {
            Intent intent = new Intent();
            if (str.equals(mArrayPopName[0])) {
                intent.setClass(ApprovalViewActivity.this, ApprovalLeaveActivity.class);
                intent.putExtra("apid", mCustomApprovalVO.getArray().get(position).getOption().get(0).getAtid());
            }
            else if (str.equals(mArrayPopName[1])) {
                intent.setClass(ApprovalViewActivity.this, ApprovalSuppliesActivity.class);
            }
            else if (str.equals(mArrayPopName[2])) {
                intent.setClass(ApprovalViewActivity.this, ApprovalOvertimeActivity.class);
            }
            else if (str.equals(mArrayPopName[3])) {
                intent.setClass(ApprovalViewActivity.this, ApprovalFeeApplyActivity.class);
                intent.putExtra("apid", mCustomApprovalVO.getArray().get(position).getOption().get(0).getAtid());
            }
            else if (str.equals(mArrayPopName[4])) {
                intent.setClass(ApprovalViewActivity.this, ApprovalAttendanceActivity.class);
                intent.putExtra("apid", mCustomApprovalVO.getArray().get(position).getOption().get(0).getAtid());
            }
            else {
                intent.setClass(ApprovalViewActivity.this, ApprovalCustomActivity.class);
                intent.putExtra("apid", mCustomApprovalVO.getArray().get(position).getOption().get(0).getAtid());
            }
            intent.putExtra("options", mCustomApprovalVO.getArray().get(position));
            intent.putExtra("approvalType", str);
            intent.putExtra("uid", BSApplication.getInstance().getUserFromServerVO().getUserid());
            startActivity(intent);

        }
    };

    private class BSPopupWindwos extends PopupWindow implements OnClickListener {
        private Context mContext;
        private Activity mActivity;
        private ExpandableListView mExpandableListView;
        private LinearLayout mTitleLayout01, mTitleLayout02, mTitleLayout03;
        private Button mOkBt;
        private int mType;
        private TextView textViw01, textViw02, textViw03, textViw04, textViw05, textViw06, textViw07, textViw08, textViw09;
        private boolean mOne = true;

        private List<String> groupArray;// 组列表
        private List<List<String>> childArray;// 子列表

        public BSPopupWindwos(Context context, View parent) {
            this.mContext = context;
            this.mActivity = (Activity) context;
            View view = View.inflate(context, R.layout.approval_pop_item_01, null);
            view.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View arg0, MotionEvent arg1) {
                    dismiss();
                    return false;
                }
            });
            mTitleLayout01 = (LinearLayout) view.findViewById(R.id.title_layout_01);
            mTitleLayout02 = (LinearLayout) view.findViewById(R.id.title_layout_02);
            mExpandableListView = (ExpandableListView) view.findViewById(R.id.list);
            mTitleLayout03 = (LinearLayout) view.findViewById(R.id.title_layout_03);
            textViw01 = (TextView) view.findViewById(R.id.text_01);
            textViw02 = (TextView) view.findViewById(R.id.text_02);
            textViw03 = (TextView) view.findViewById(R.id.text_03);
            textViw04 = (TextView) view.findViewById(R.id.text_04);
            textViw05 = (TextView) view.findViewById(R.id.text_05);
            textViw06 = (TextView) view.findViewById(R.id.text_06);
            textViw07 = (TextView) view.findViewById(R.id.text_07);
            textViw08 = (TextView) view.findViewById(R.id.text_08);
            textViw09 = (TextView) view.findViewById(R.id.text_09);
            textViw01.setOnClickListener(this);
            textViw02.setOnClickListener(this);
            textViw03.setOnClickListener(this);
            textViw04.setOnClickListener(this);
            textViw05.setOnClickListener(this);
            textViw06.setOnClickListener(this);
            textViw07.setOnClickListener(this);
            textViw08.setOnClickListener(this);
            textViw09.setOnClickListener(this);
            groupArray = new ArrayList<String>();
            childArray = new ArrayList<List<String>>();
            view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in));
            setWidth(LayoutParams.FILL_PARENT);
            setHeight(LayoutParams.FILL_PARENT);
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
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.text_01:
                    match(0, "0");
                    mTitleName01.setText(textViw01.getText());
                    dismiss();
                    break;
                case R.id.text_02:
                    match(0, "1");
                    mTitleName01.setText(textViw02.getText());
                    dismiss();
                    break;
                case R.id.text_03:
                    match(0, "2");
                    mTitleName01.setText(textViw03.getText());
                    dismiss();
                    break;
                case R.id.text_04:
                    match(0, "3");
                    mTitleName01.setText(textViw04.getText());
                    dismiss();
                    break;
                case R.id.text_05:
                    match(4, "0");
                    mTitleName03.setText(textViw05.getText());
                    dismiss();
                    break;
                case R.id.text_06:
                    match(4, "1");
                    mTitleName03.setText(textViw06.getText());
                    dismiss();
                    break;
                case R.id.text_07:
                    match(4, "2");
                    mTitleName03.setText(textViw07.getText());
                    dismiss();
                    break;
                case R.id.text_08:
                    match(4, "3");
                    mTitleName03.setText(textViw08.getText());
                    dismiss();
                    break;
                case R.id.text_09:
                    match(4, "4");
                    mTitleName03.setText(textViw09.getText());
                    dismiss();
                    break;
                default:
                    break;
            }
            cleanData();
            mBsRefreshListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
            new ThreadUtil(ApprovalViewActivity.this, ApprovalViewActivity.this).start();
        }

    }

    public void match(int key, String value) {
        if (key == 0) {
            modeid = "";
            bigtypeid = "";
            smalltypeid = "";
            statusid = "";
            mKeyword = "";
        }
        mState = 0;
        refresh = "";
        switch (key) {
            case 0:
                modeid = value;
                break;
            case 1:
                isall = value;
                break;
            case 2:
                bigtypeid = value;
                break;
            case 3:
                bigtypeid = value.split(",")[0];
                smalltypeid = value.split(",")[1];
                break;
            case 4:
                statusid = value;
                break;
            case 5:
                mKeyword = value;
                break;
            default:
                break;
        }

    }

    public void cleanData() {
        mAdapter.mList.clear();
        mFootLayout.setVisibility(View.GONE);
        mNoContentLyaout.setVisibility(View.GONE);
        mBsRefreshListView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void registBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.HOME_MSG);
        this.registerReceiver(msgBroadcast, filter);
    }

    private void unRegistReceiver() {
        this.unregisterReceiver(msgBroadcast);
    }

    private BroadcastReceiver msgBroadcast = new BroadcastReceiver() {
        private long mChangeTime = 0;

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constant.HOME_MSG.equals(intent.getAction())) {
                new ThreadUtil(ApprovalViewActivity.this, ApprovalViewActivity.this).start();
            }
        }
    };

    private ArrayList<TreeVO> initPopDowns() {

        ArrayList<TreeVO> list = new ArrayList<TreeVO>();
        TreeVO allTreeVo = new TreeVO();
        allTreeVo.setId(-1);
        allTreeVo.setParentId(0);
        allTreeVo.setName("全部");
        allTreeVo.setLevel(1);
        allTreeVo.setHaschild(false);
        allTreeVo.setDepartmentid("-1");
        allTreeVo.setDname("全部");
        list.add(allTreeVo);

        int cout = 0;
        for (int i = 0; i < mCustomApprovalVO.getArray().size(); i++) {
            TreeVO parentVo = new TreeVO();
            parentVo.setId(i + 1);
            parentVo.setParentId(0);
            parentVo.setName(mCustomApprovalVO.getArray().get(i).getName());
            parentVo.setParentSerachId(mCustomApprovalVO.getArray().get(i).getId());
            parentVo.setLevel(1);
            if (mCustomApprovalVO.getArray().get(i).getOption() != null && mCustomApprovalVO.getArray().get(i).getOption().size() > 0) {
                parentVo.setHaschild(true);
            } else {
                parentVo.setHaschild(false);
            }
            list.add(parentVo);

            if (mCustomApprovalVO.getArray().get(i).getOption() != null && mCustomApprovalVO.getArray().get(i).getOption().size() > 0) {
                for (int j = 0; j < mCustomApprovalVO.getArray().get(i).getOption().size(); j++) {

                    if (j == 0) {
                        TreeVO childAllVo = new TreeVO();

                        childAllVo.setId(cout + mCustomApprovalVO.getArray().size());
                        childAllVo.setParentId(i + 1);
                        childAllVo.setName("全部");
                        childAllVo.setLevel(2);
                        childAllVo.setChildSearchId(null);
                        childAllVo.setParentSerachId(mCustomApprovalVO.getArray().get(i).getId());
                        childAllVo.setParentName(mCustomApprovalVO.getArray().get(i).getName());
                        list.add(childAllVo);
                        cout++;
                    }

                    cout++;
                    TreeVO childVo = new TreeVO();
                    childVo.setId(cout + mCustomApprovalVO.getArray().size());
                    childVo.setParentId(i + 1);
                    childVo.setName(mCustomApprovalVO.getArray().get(i).getOption().get(j).getName());
                    childVo.setChildSearchId(mCustomApprovalVO.getArray().get(i).getOption().get(j).getAtid());
                    childVo.setParentSerachId(mCustomApprovalVO.getArray().get(i).getId());
                    childVo.setLevel(2);
                    list.add(childVo);
                }
            }
        }
        return list;
    }

    // 发布按钮菜单
    public void initPopLeft() {
        if (!mPopLeftFlag && mArray != null) {
            CommonUtils.initPopViewBg(this, mArray, mOkTv, mCallback, mIcon);
            return;
        }
        mArray = new String[mCustomApprovalVO.getArray().size()];
        mIcon = new int[mCustomApprovalVO.getArray().size()];
        for (int i = 0; i < mCustomApprovalVO.getArray().size(); i++) {
            if (mCustomApprovalVO.getArray().get(i).getName().length() > 2) {
                mArray[i] = mCustomApprovalVO.getArray().get(i).getName().substring(0, 2);
            }
            else {
                mArray[i] = mCustomApprovalVO.getArray().get(i).getName();
            }

            switch (Integer.parseInt(mCustomApprovalVO.getArray().get(i).getId())) {
                case 1:
                    mIcon[i] = R.drawable.approval_pop1;
                    break;
                case 2:
                    mIcon[i] = R.drawable.approval_pop2;
                    break;
                case 3:
                    mIcon[i] = R.drawable.approval_pop3;
                    break;
                case 4:
                    mIcon[i] = R.drawable.approval_pop4;
                    break;
                case 5:
                    mIcon[i] = R.drawable.approval_pop5;
                    break;
                case 6:
                    mIcon[i] = R.drawable.approval_pop6;
                    break;

            }
        }
        CommonUtils.initPopViewBg(this, mArray, mOkTv, mCallback, mIcon);
        mPopLeftFlag = false;
    }

    public static ArrayList<TreeVO> getTreeVOList() {
        ArrayList<TreeVO> list = new ArrayList<TreeVO>();
        TreeVO allTreeVo = new TreeVO();
        allTreeVo.setId(-1);
        allTreeVo.setParentId(0);
        allTreeVo.setName("全部");
        allTreeVo.setLevel(1);
        allTreeVo.setHaschild(false);
        allTreeVo.setDepartmentid("-1");
        allTreeVo.setDname("全部");
        list.add(allTreeVo);

        ResultVO resultVO = ResultVO.getInstance();
        ArrayList<DepartmentAndEmployeeVO> departments = resultVO.getDepartments();

        // 每个二级菜单添加一个全部，为了让全部排在第一，故拿出来重新写了一遍
        for (int i = 0; i < departments.size(); i++) {
            DepartmentAndEmployeeVO vo = departments.get(i);
            if ("0".equals(vo.getBelong())) {
                TreeVO childTreeVo = new TreeVO();
                childTreeVo.setId(Integer.parseInt(vo.getDepartmentid()));
                childTreeVo.setParentId(Integer.parseInt(vo.getDepartmentid()));
                childTreeVo.setName("全部");
                childTreeVo.setLevel(2);
                childTreeVo.setHaschild(false);
                childTreeVo.setDepartmentid(vo.getDepartmentid());
                childTreeVo.setDname(vo.getDname());
                list.add(childTreeVo);
            }
        }
        for (int i = 0; i < departments.size(); i++) {
            DepartmentAndEmployeeVO vo = departments.get(i);
            for (int j = 0; j < departments.size(); j++) {
                if (vo.getDepartmentid().equals(departments.get(j).getBelong())) {
                    vo.setHaschild(true);
                    break;
                } else {
                    vo.setHaschild(false);
                }
            }

            TreeVO treeVo = new TreeVO();
            treeVo.setId(Integer.parseInt(vo.getDepartmentid()));
            treeVo.setParentId(Integer.parseInt(vo.getBelong()));
            treeVo.setName(vo.getDname());
            treeVo.setLevel(Integer.parseInt(vo.getLevel()));
            treeVo.setHaschild(vo.isHaschild());
            treeVo.setDepartmentid(vo.getDepartmentid());
            treeVo.setDname(vo.getDname());
            list.add(treeVo);
        }
        return list;
    }

    TreeCallBack callback = new TreeCallBack() {

        @Override
        public void callback(TreeVO vo) {
            if (vo.getLevel() == 1) {
                // 审批一级菜单
                mTitleName02.setText(vo.getName());
                match(2, vo.getParentSerachId() + "");
                mPop.dismiss();
                cleanData();
                mBsRefreshListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
                new ThreadUtil(ApprovalViewActivity.this, ApprovalViewActivity.this).start();
            } else {

                // 审批二级菜单

                if ("全部".equals(vo.getName())) {
                    mTitleName02.setText(vo.getParentName());
                    match(2, vo.getParentId() + "");
                    cleanData();
                    mBsRefreshListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
                    new ThreadUtil(ApprovalViewActivity.this, ApprovalViewActivity.this).start();
                    return;
                } else {
                    mTitleName02.setText(vo.getName());
                }

                // 审批自定义审批菜单
                match(3, vo.getParentSerachId() + "," + vo.getChildSearchId());
                cleanData();
                mBsRefreshListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
                new ThreadUtil(ApprovalViewActivity.this, ApprovalViewActivity.this).start();

            }
        }
    };

}
