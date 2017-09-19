
package com.bs.bsims.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.bs.bsims.R;
import com.bs.bsims.adapter.CrmVisitRecordNewAdapter;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.CrmOptionsVO;
import com.bs.bsims.model.CrmVisitorVo;
import com.bs.bsims.model.TreeVO;
import com.bs.bsims.ui.datepicker.OnWheelScrollListener;
import com.bs.bsims.ui.datepicker.WheelView;
import com.bs.bsims.ui.datepicker.adapter.NumericWheelAdapter;
import com.bs.bsims.utils.CommonUtils.ResultCallback;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.utils.UrlUtil;
import com.bs.bsims.view.BSIndexEditText;
import com.bs.bsims.view.BSPopupWindowsTitle;
import com.bs.bsims.view.BSPopupWindowsTitle.TreeCallBack;
import com.bs.bsims.view.BSRefreshListView;
import com.bs.bsims.view.BSRefreshListView.OnRefreshListener;
import com.bs.bsims.view.PinnedSectionListView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrmVisitRecordListActivity extends BaseActivity implements OnClickListener,OnItemClickListener {

    private CrmVisitRecordNewAdapter crmIndexListAdapter;

    private Context context;

    private CrmVisitorVo mVisitor;

    private PinnedSectionListView crm_business_indexlistview;
    private TextView mLoading;
    // listView
    private View mFootLayout;
    private TextView mMoreTextView;
    private ProgressBar mProgressBar;
    private String mRefresh = "";
    // 0为首次,1为上拉刷新 ，2为下拉刷新
    private int mState = 0;

    private LinearLayout visitor_add_state, one_title, two_title;

    private BSIndexEditText mClearEditText;
    /** 关键词搜索（选传） */
    private String keyword = "";

    /** 上拉ID */
    private String firstid;

    /** 下拉ID */
    private String lastid;

    /** 记录最后一条的id */
    private String saveLastId;

    /** 匹配搜索的key */
    private String tyepkey = "0";

    private BSPopupWindowsTitle mBsPopupWindowsTitle;

    private TextView one_title_tv, two_title_tv;// 第一个筛选框

    private CrmOptionsVO mCrmOptionsVO;

    private boolean mFlag = true;

    private WheelView year;
    private WheelView month;
    private WheelView day;

    private int norYear;
    private int curMonth;
    private int curDate;
    private String mUnread;

    private LinearLayout date_task_changedate_select_ly;
    /**
     * 重新选择的时间
     */
    private String time;
    /**
     * 部门筛选的id
     */
    private String did = "0";
    private String date;// 时间
    private Boolean canClickFlag = true;// 解决连续点“更多”可能会出现的异常

    private BSPopupWindowsTitle mTimeSelect;
    // /**
    // * 保存每次用户操作的数据源
    // */
    // private List<CrmVisitorVo> UserSaveList = new ArrayList<CrmVisitorVo>();
    //
    // private String Pagecount = "";// 保存的分页的count
    private TextView mNoReadTv;
    private View mHeaView;

    @Override
    public void baseSetContentView() {
        // TODO Auto-generated method stub
        View view = View.inflate(this, R.layout.crmvisitrecord_index, mContentLayout);
        mHeaView = View.inflate(this, R.layout.crm_visitlist_headview, null);
        context = this;
    }

    @Override
    public boolean getDataResult() {
        // TODO Auto-generated method stub
        return getData();
    }

    @Override
    public void updateUi() {
        // TODO Auto-generated method stub
    }

    @Override
    public void initView() {

        // TODO Auto-generated method stub
        mTitleTv.setText("跟单记录");
        mOkTv.setText("添加");
        crm_business_indexlistview = (PinnedSectionListView) findViewById(R.id.refresh_listview);
        crmIndexListAdapter = new CrmVisitRecordNewAdapter(context);
        // crmIndexListAdapter.setState("1");
        crm_business_indexlistview.addHeaderView(mHeaView);
        crm_business_indexlistview.setAdapter(crmIndexListAdapter);
        initFoot();
        mClearEditText = (BSIndexEditText) findViewById(R.id.edit_single_search1);

        one_title_tv = (TextView) mHeaView.findViewById(R.id.one_title_tv);
        two_title_tv = (TextView) mHeaView.findViewById(R.id.two_title_tv);
        one_title = (LinearLayout) mHeaView.findViewById(R.id.one_title);
        two_title = (LinearLayout) mHeaView.findViewById(R.id.two_title);
        mTimeSelect = new BSPopupWindowsTitle((Activity) context, 2, LayoutParams.WRAP_CONTENT, timecallback, true);
        mClearEditText.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) mClearEditText.getContext().getSystemService(
                            Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    CrmVisitRecordListActivity.this
                                            .getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);

                    keyword = mClearEditText.getText().toString().trim();
                    if (keyword.equals("")) {
                        keyword = "";
                        return false;
                    }
                    mState = 0;
                    tyepkey = "1";
                    crm_business_indexlistview
                            .changeHeaderViewByState(BSRefreshListView.REFRESHING);
                    new ThreadUtil(CrmVisitRecordListActivity.this, CrmVisitRecordListActivity.this)
                            .start();
                    return true;
                }
                return false;
            }
        });
        mNoReadTv = (TextView) findViewById(R.id.no_read_tv);
        mUnread = this.getIntent().getStringExtra("unread");
        if (this.getIntent().getStringExtra("msg") != null) {
            one_title.setVisibility(View.GONE);
            mClearEditText.setVisibility(View.GONE);
        }
    }

    ResultCallback timecallback = new ResultCallback() {

        @Override
        public void callback(String str, int position) {
            // TODO Auto-generated method stub
            date = str;
            crm_business_indexlistview.changeHeaderViewByState(BSRefreshListView.REFRESHING);
            two_title_tv.setText(str);
            new ThreadUtil(context, CrmVisitRecordListActivity.this).start();
        }

    };

    @Override
    public void bindViewsListener() {
        // TODO Auto-generated method stub
        mOkTv.setOnClickListener(this);
        one_title.setOnClickListener(this);
        two_title.setOnClickListener(this);
        crm_business_indexlistview.setOnItemClickListener(this);
        bindRefreshListener();

        // 根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // 当搜索框为空的时候 也就是点叉的时候
                // if (s.toString().equals("") && UserSaveList.size() > 0) {
                // keyword = "";
                // tyepkey = "0";
                // loadingfile1.setVisibility(View.GONE);
                // crm_business_indexlistview.setVisibility(View.VISIBLE);
                // crmIndexListAdapter.updateData(UserSaveList);
                // if (Integer.parseInt(Pagecount) <= 15) {
                // mFootLayout.setVisibility(View.GONE);
                // // listView.removeFooterView(mFootLayout);
                // } else {
                // mFootLayout.setVisibility(View.VISIBLE);
                // mMoreTextView.setText("更多");
                // mProgressBar.setVisibility(View.GONE);
                // }
                // }

                if (s.toString().equals("")) {
                    keyword = "";
                    mState = 0;
                    crm_business_indexlistview
                            .changeHeaderViewByState(BSRefreshListView.REFRESHING);
                    new ThreadUtil(CrmVisitRecordListActivity.this, CrmVisitRecordListActivity.this)
                            .start();
                }

            }
        });
        mNoReadTv.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.txt_comm_head_right:
                Intent i = new Intent(CrmVisitRecordListActivity.this,
                        CrmVisitRecordActivityAddInfo.class); //
                i.putExtra("vistorSate", "1");
                startActivityForResult(i, 2015);
                break;
            case R.id.one_title:
                initPopData();
                break;
            case R.id.two_title:
                mTimeSelect.showPopupWindow(v);
                break;
            case R.id.no_read_tv:
                Intent noReadIntent = new Intent();
                noReadIntent.putExtra("type", 18);
                noReadIntent.setClass(this, AllNoReadActivity.class);
                startActivity(noReadIntent);
                break;

            default:
                break;
        }

    }

    @Override
    public void executeSuccess() {

        if (null == mVisitor) {
            return;
        }

        if (mFlag) {
            mFlag = false;
        }
        // TODO Auto-generated method stub
        // if (keyword.equals("")) {
        // if (mState != 1) {
        // if(mState!=2){
        // UserSaveList.clear();
        // }
        // UserSaveList.addAll(mVisitor.getArray());
        // Pagecount = mVisitor.getCount();
        // }
        // else {
        // UserSaveList.addAll(0, mVisitor.getArray());
        // }
        //
        // }

        tyepkey = "0";

        crm_business_indexlistview.setVisibility(View.VISIBLE);
        mContentLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
        // crmbussinesApdater = new CrmBusinessHomeListAdapter(context,
        // crmbulist.getArray());
        // crm_business_indexlistview.setAdapter(crmbussinesApdater);
        // crmbussinesApdater.notifyDataSetChanged();
        if (1 == mState) {
            crmIndexListAdapter.updateDataFrist(mVisitor.getArray());
        } else if (2 == mState) {
            crmIndexListAdapter.updateDataLast(mVisitor.getArray());
        } else {
            crmIndexListAdapter.updateData(mVisitor.getArray());
        }

        if (mState != 1)
            footViewIsVisibility();
        mState = 0;
        crm_business_indexlistview.setVisibility(View.VISIBLE);
        crm_business_indexlistview.onRefreshComplete();
        canClickFlag = true;
    }

    @Override
    public void executeFailure() {
        // TODO Auto-generated method stub

        // 网络异常
        if (null == mVisitor) {
            super.showNoNetView();
            return;
        } else {
            if (mState == 0) {
                crmIndexListAdapter.updateData(mVisitor.getArray());
                mFootLayout.setVisibility(View.GONE);
            }
        }
        tyepkey = "0";
        mContentLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
        mState = 0;
        crm_business_indexlistview.onRefreshComplete();
        if (mState != 1)
            footViewIsVisibility();
        canClickFlag = true;
    }

    public boolean getData() {

        Gson gson = new Gson();
        Map<String, String> paramsMap = null;
        if (mState == 0) {
            paramsMap = new HashMap<String, String>();
            paramsMap.put("keyword", keyword);
        } else if (mState == 1) {
            if (mRefresh.equals("")) {
                paramsMap = new HashMap<String, String>();
            } else {
                // firstid = crmbussinesApdater.mList.get(0).getBid();
                paramsMap = new HashMap<String, String>();
                paramsMap.put("keyword", keyword);
                paramsMap.put("firstid", firstid);
            }

        } else {
            lastid = saveLastId;// mEXTSharedfilesdHomeMyUpdateAdapter.mList.get(mEXTSharedfilesdHomeMyUpdateAdapter.mList.size()
                                // - 1).getSharedid();
            paramsMap = new HashMap<String, String>();
            paramsMap.put("keyword", keyword);
            paramsMap.put("lastid", lastid);
        }
        // Map map = new HashMap<String, String>();
        paramsMap.put("type", did);
        paramsMap.put("date", date);

        paramsMap.put("unread", mUnread);

        String urlStr = UrlUtil.getUrlByMap1(Constant.CRM_VISIT_RECORD_LISTINDEX,
                paramsMap);
        String jsonUrlStr;
        try {
            jsonUrlStr = HttpClientUtil.get(urlStr, Constant.ENCODING).trim();
            mVisitor = gson.fromJson(jsonUrlStr, CrmVisitorVo.class);
            if (mVisitor.getCode().equals("200")) {
                return true;
            } else {

                return false;
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return false;

    }

    public void bindRefreshListener() {
        crm_business_indexlistview
                .setonRefreshListener(new OnRefreshListener() {

                    @Override
                    public void onRefresh() {
                        mState = 1;
                        mRefresh = Constant.FIRSTID;
                        if (crmIndexListAdapter.mList.size() == 0)
                            mRefresh = "";
                        else
                            firstid = crmIndexListAdapter.mList.get(0).getVid();
                        new ThreadUtil(context,
                                CrmVisitRecordListActivity.this).start();
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
                    mRefresh = Constant.LASTID;
                    saveLastId = crmIndexListAdapter.mList
                            .get(crmIndexListAdapter.mList.size() - 1).getVid();
                    new ThreadUtil(context, CrmVisitRecordListActivity.this)
                            .start();
                }
            }
        });

    }

    // 加载更多数据
    public void initFoot() {
        mFootLayout = LayoutInflater.from(context).inflate(
                R.layout.listview_bottom_more, null);
        mMoreTextView = (TextView) mFootLayout.findViewById(R.id.txt_loading);
        mMoreTextView.setText("更多");
        mProgressBar = (ProgressBar) mFootLayout.findViewById(R.id.progressBar);
        mFootLayout.setVisibility(View.GONE);
        crm_business_indexlistview.addFooterView(mFootLayout);
    }

    protected void footViewIsVisibility() {
        if (mVisitor == null) {
            return;
        }
        if (mVisitor.getCount() == null) {
            return;
        }
        if (Integer.parseInt(mVisitor.getCount()) <= 15) {
            mFootLayout.setVisibility(View.GONE);
            // listView.removeFooterView(mFootLayout);
        } else {
            mFootLayout.setVisibility(View.VISIBLE);
            mMoreTextView.setText("更多");
            mProgressBar.setVisibility(View.GONE);
        }
    }

    public void initPopData() {
        String str[] = {
                "全部记录", "我负责的", "我下属的"
        };
        List<CrmOptionsVO> parentList = new ArrayList<CrmOptionsVO>();
        CrmOptionsVO mCrmOptionsVO2;
        for (int i = 0; i < 3; i++) {
            mCrmOptionsVO2 = new CrmOptionsVO();
            mCrmOptionsVO2.setId(i + "");
            mCrmOptionsVO2.setName(str[i] + "");
            parentList.add(mCrmOptionsVO2);
        }
        ArrayList<TreeVO> list = getOneLeveTreeVo(parentList);
        mBsPopupWindowsTitle = new BSPopupWindowsTitle(this, list, callback,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mBsPopupWindowsTitle.showPopupWindow(one_title);
    }

    public ArrayList<TreeVO> getOneLeveTreeVo(List<CrmOptionsVO> parentList) {
        ArrayList<TreeVO> list = new ArrayList<TreeVO>();
        for (int i = 0; i < parentList.size(); i++) {
            TreeVO vo = new TreeVO();
            vo.setName(parentList.get(i).getName());
            vo.setParentSerachId(parentList.get(i).getId());
            vo.setLevel(1);
            list.add(vo);
        }
        return list;
    }

    // 菜单点击回调函数
    TreeCallBack callback = new TreeCallBack() {

        @Override
        public void callback(TreeVO vo) {
            if (vo.getLevel() == 1) {
                // 审批一级菜单
                one_title_tv.setText(vo.getName());
                did = vo.getParentSerachId();
                crm_business_indexlistview.changeHeaderViewByState(BSRefreshListView.REFRESHING);
                // if (did.equals("2")) {
                // did = "0";
                // date = "";
                // keyword = "";
                // two_title_tv.setText("选择时间");
                //
                // }
                new ThreadUtil(context, CrmVisitRecordListActivity.this).start();
            }
        }
    };

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
            // boolean flag = CommonDateUtils.isOutDate(time);
            // if (flag) {
            // year.setCurrentItem(0);
            // month.setCurrentItem(curMonth - 1);
            // day.setCurrentItem(curDate - 1);
            //
            // time = DateUtils.getCurrentDate();
            // two_title_tv.setText(time);
            // return;
            // }
            // two_title_tv.setText(today);
        }
    };

    /**
     */
    private void initDay(int year, int month) {
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(this, 1,
                DateUtils.getDayAtYM(year, month), "%02d");
        numericWheelAdapter.setLabel("日");
        day.setViewAdapter(numericWheelAdapter);
    }

    // 从列表点击添加拜访记录之后 回来之后调用下拉刷新
    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        switch (arg0) {
            case 2015:
                if (arg1 == 2015) {
                    if (arg2 != null) {
                        /*
                         * 返回过来直接调用下拉刷新
                         */
                        mState = 1;
                        if (crmIndexListAdapter.mList.size() == 0)
                            mRefresh = "";
                        else {
                            mRefresh = 1 + "";
                            firstid = crmIndexListAdapter.mList.get(0).getVid();
                        }
                        crm_business_indexlistview
                                .changeHeaderViewByState(BSRefreshListView.REFRESHING);
                        new ThreadUtil(context, CrmVisitRecordListActivity.this).start();

                    }
                }

                break;

        }
    }

 
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
        // TODO Auto-generated method stub
        if (crmIndexListAdapter.mList.size() > 0 && crmIndexListAdapter.getItemViewType((int) arg3) != -10) {
            Intent i = new Intent();
            i.setClass(context, CrmVisitRecordDetailActivity.class);
            i.putExtra("vid", crmIndexListAdapter.mList.get(crmIndexListAdapter.mPinnedList.get((int) arg3)).getVid());
            crmIndexListAdapter.mList.get(crmIndexListAdapter.mPinnedList.get((int) arg3)).setIsread("1");
            crmIndexListAdapter.notifyDataSetChanged();
            context.startActivity(i);
        }
    }

}
