
package com.bs.bsims.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.JournalListAdapter;
import com.bs.bsims.adapter.SortPeopleAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.DepartmentAndEmployeeVO;
import com.bs.bsims.model.JournalListVO1;
import com.bs.bsims.utils.CharacterParser;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSIndexEditText;
import com.bs.bsims.view.BSRefreshListView;
import com.bs.bsims.view.BSRefreshListView.OnRefreshListener;
import com.bs.bsims.view.BSSideBar;
import com.bs.bsims.view.BSSideBar.OnTouchingLetterChangedListener;
import com.bs.bsims.view.PinnedSectionListView;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class JournalSerachActivity extends BaseActivity implements OnItemClickListener {
    private ListView mListView;
    private BSSideBar mBSSideBar;
    private CharacterParser mCharacterParser;
    private List<DepartmentAndEmployeeVO> mList;
    private TextView mDialog;
    private BSIndexEditText mBSIndexEditText;
    private SortComparatorVO mSortComparatorVO;
    private SortPeopleAdapter mSortPeopleAdapter;
    private LinearLayout mJournalLayout;
    private FrameLayout mPeopleLayout;
    // 下拉上拉刷新部分
    private View mFootLayout;
    private TextView mMoreTextView;
    private ProgressBar mProgressBar;
    private PinnedSectionListView mRefreshListView;
    private JournalListAdapter mAdapter;
    private String mFristid, mLastid;
    private int mState = 0; // 0为首次,1为上拉刷新 ，2为下拉刷新
    private String mLoguid;
    private JournalListVO1 mListVO;
    private boolean mCanClickFlag = true;
    private boolean mFristRequest = true;
    private String mType, mDid, mIsMyFavor, mIsremind;
    private String mDefferentGoin = "1";// 1为搜索跳进，2为首页日志提醒，3为我的日志
    private String mUnread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.journal_search, mContentLayout);
    }

    @Override
    public boolean getDataResult() {
        if (mFristRequest) {
            return true;
        } else {
            return getData();
        }
    }

    @Override
    public void executeSuccess() {
        super.isRequestFinish();
        if (mFristRequest) {
            mFristRequest = false;
            return;
        }
        mRefreshListView.setVisibility(View.VISIBLE);
        mRefreshListView.onRefreshComplete();
        if (1 == mState) {
            mAdapter.updateDataFrist(mListVO.getArray());
        } else if (2 == mState) {
            mAdapter.updateDataLast(mListVO.getArray());
        } else {
            mAdapter.updateData(mListVO.getArray());
        }

        if ("".equals(mFristid) || mState != 1) {
            footViewIsVisibility();
        }
        mState = 0;
        mCanClickFlag = true;
    }

    @Override
    public void executeFailure() {
        // 列表展示的时候不能调用父类
        super.isRequestFinish();
        mAdapter.notifyDataSetChanged();
        mRefreshListView.onRefreshComplete();
        footViewIsVisibility();

        if (mListVO == null) {
            super.showNoNetView();
        } else {
            if (mState == 0) {
                mAdapter.updateData(new ArrayList<JournalListVO1>());
                mFootLayout.setVisibility(View.GONE);
            }
        }

        mState = 0;
        mCanClickFlag = true;

    }

    @Override
    public void updateUi() {
    }

    @Override
    public void initView() {
        mTitleTv.setText("日志搜索");
        mJournalLayout = (LinearLayout) findViewById(R.id.journal_layout);
        mPeopleLayout = (FrameLayout) findViewById(R.id.people_layout);
        mListView = (ListView) findViewById(R.id.lv_contacts_sort);
        mBSSideBar = (BSSideBar) findViewById(R.id.sidrbar);
        mDialog = (TextView) findViewById(R.id.dialog);
        mBSSideBar.setTextView(mDialog);
        mBSIndexEditText = (BSIndexEditText) findViewById(R.id.filter_edit);
        mBSIndexEditText.setHint("请输入姓名");
        mBSIndexEditText.setFocusable(true);
        mBSIndexEditText.setFocusableInTouchMode(true);
        mBSIndexEditText.requestFocus();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInputFromInputMethod(mBSIndexEditText.getWindowToken(), 0);
        mRefreshListView = (PinnedSectionListView) findViewById(R.id.refresh_listview);
        mRefreshListView = (PinnedSectionListView) findViewById(R.id.refresh_listview);
        mPeopleLayout.setVisibility(View.GONE);
        mAdapter = new JournalListAdapter(this);
        mRefreshListView.setAdapter(mAdapter);
        initFoot();

        initData();
    }

    @Override
    public void bindViewsListener() {
        mListView.setOnItemClickListener(this);
        mRefreshListView.setOnItemClickListener(this);

        mBSSideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = mSortPeopleAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mListView.setSelection(position);
                }
            }
        });

        mBSIndexEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0) {
                    mJournalLayout.setVisibility(View.VISIBLE);
                    mPeopleLayout.setVisibility(View.GONE);

                } else {
                    mJournalLayout.setVisibility(View.GONE);
                    mPeopleLayout.setVisibility(View.VISIBLE);
                    filterData(s.toString());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // mJournalLayout.setVisibility(View.GONE);
                // mPeopleLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mRefreshListView.setonRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mAdapter.mList.size() > 0) {
                    match(1, mAdapter.mList.get(0).getLogid());
                } else {
                    mFristid = "";
                    match(1, "");
                }
            }
        });
        mFootLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCanClickFlag) {
                    mCanClickFlag = false;
                    mMoreTextView.setText("正在加载...");
                    mProgressBar.setVisibility(View.VISIBLE);
                    match(2, mAdapter.mList.get(mAdapter.mList.size() - 1).getLogid());
                }
            }
        });

    }

    public void initData() {
        Intent intent = this.getIntent();
        mIsremind = intent.getStringExtra("isremind");
        mUnread = intent.getStringExtra("unread");
        // 首页日志提醒
        if (mIsremind != null)
            mTitleTv.setText("日志提醒");
        mLoguid = intent.getStringExtra("loguid");
        // 我的日志
        if (mLoguid != null)
            mTitleTv.setText("我的日志");
        if (!"1".equals(intent.getStringExtra("defferent_goin"))) {
            mFristRequest = false;
            mJournalLayout.setVisibility(View.VISIBLE);
            mPeopleLayout.setVisibility(View.GONE);
            mBSIndexEditText.setVisibility(View.GONE);
            return;
        }

        // 日志列表搜索进入
        mCharacterParser = CharacterParser.getInstance();
        mList = new ArrayList<DepartmentAndEmployeeVO>();
        mSortPeopleAdapter = new SortPeopleAdapter(this);
        mListView.setAdapter(mSortPeopleAdapter);
        mSortComparatorVO = new SortComparatorVO();
        List<DepartmentAndEmployeeVO> list = BSApplication.getInstance().getResultVO().getUsers();
        sortData(list);
        Collections.sort(mList, mSortComparatorVO);
        mSortPeopleAdapter.updateData(mList);

        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
            public void run()

            {
                InputMethodManager inputManager =
                        (InputMethodManager)
                        mBSIndexEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(mBSIndexEditText, 0);
            }
        },
                500);

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
            map.put("loguid", mLoguid);
            map.put("isremind", mIsremind);
            map.put("unread", mUnread);

            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.JOURNAL_LIST_NEW, map);
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

    private void sortData(List<DepartmentAndEmployeeVO> list) {
        for (int i = 0; i < list.size(); i++) {
            DepartmentAndEmployeeVO personnelVO = list.get(i);

            // 汉字转换成拼音List<BusinessVisit> visitList
            String pinyin = mCharacterParser.getSelling(personnelVO.getFullname());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                personnelVO.setSortLetters(sortString.toUpperCase());
            } else {
                personnelVO.setSortLetters("#");
            }
            mList.add(personnelVO);
        }
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     * 
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<DepartmentAndEmployeeVO> filterDateList = new ArrayList<DepartmentAndEmployeeVO>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = mList;
        } else {
            filterDateList.clear();
            for (DepartmentAndEmployeeVO personnelVO : mList) {
                String name = personnelVO.getFullname();
                if (name.indexOf(filterStr.toString()) != -1 || mCharacterParser.getSelling(name).startsWith(filterStr.toString())) {
                    filterDateList.add(personnelVO);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, mSortComparatorVO);
        mSortPeopleAdapter.updateData(filterDateList);
    }

    class SortComparatorVO implements Comparator<DepartmentAndEmployeeVO> {
        public int compare(DepartmentAndEmployeeVO o1, DepartmentAndEmployeeVO o2) {
            if (o1.getSortLetters().equals("@")
                    || o2.getSortLetters().equals("#")) {
                return -1;
            } else if (o1.getSortLetters().equals("#")
                    || o2.getSortLetters().equals("@")) {
                return 1;
            } else {
                return o1.getSortLetters().compareTo(o2.getSortLetters());
            }
        }
    }

    public void footViewIsVisibility() {
        if (mListVO == null || mListVO.getCount() == null) {
            return;
        }
        if (Integer.parseInt(mListVO.getCount()) <= 15) {
            mFootLayout.setVisibility(View.GONE);
        } else {
            mFootLayout.setVisibility(View.VISIBLE);
            mMoreTextView.setText("更多");
            mProgressBar.setVisibility(View.GONE);
        }
    }

    // 加载更多数据
    public void initFoot() {
        mFootLayout = LayoutInflater.from(this).inflate(R.layout.listview_bottom_more, null);
        mMoreTextView = (TextView) mFootLayout.findViewById(R.id.txt_loading);
        mMoreTextView.setText("更多");
        mProgressBar = (ProgressBar) mFootLayout.findViewById(R.id.progressBar);
        mFootLayout.setVisibility(View.GONE);
        mRefreshListView.addFooterView(mFootLayout);
    }

    public void match(int key, String value) {
        switch (key) {
            case 1:
                // if ("1".equals(this.getIntent().getStringExtra("defferent_goin")))
                // mBSIndexEditText.setText("");
                mFristid = value;
                mLastid = "";
                mState = 1;
                break;
            case 2:
                mLastid = value;
                mFristid = "";
                mState = 2;
                break;
            case 3:
                mLoguid = value;
                break;
            default:
                break;
        }
        mRefreshListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
        new ThreadUtil(this, this).start();
    }

    @Override
    public void onItemClick(AdapterView<?> listView, View arg1, int arg2, long arg3) {

        if (listView == mListView) {
            DepartmentAndEmployeeVO daeVo = (DepartmentAndEmployeeVO) listView.getAdapter().getItem(arg2);
            mJournalLayout.setVisibility(View.VISIBLE);
            mPeopleLayout.setVisibility(View.GONE);
            match(3, daeVo.getUserid());
        } else {
            if (mAdapter.mList.size() > 0 && mAdapter.getItemViewType((int) arg3) != -10) {
                JournalListVO1 vo = mAdapter.mList.get(mAdapter.mPinnedList.get((int) arg3));
                Intent intent = new Intent();
                // JournalListVO1 vo = (JournalListVO1) listView.getAdapter().getItem(arg2);
                intent.putExtra("logid", vo.getLogid());
                intent.putExtra("loguid", mLoguid);
                intent.putExtra("isremind", mIsremind);
                intent.putExtra("listvo", (Serializable) mAdapter.mList);
                intent.setClass(this, JournalPublishDetailActivity.class);
                this.startActivityForResult(intent, 2);
            }
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
