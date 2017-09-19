
package com.bs.bsims.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.bs.bsims.R;
import com.bs.bsims.adapter.CrmBussinesTranctAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.CrmOptionsVO;
import com.bs.bsims.model.CrmTranctVo;
import com.bs.bsims.model.TreeVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.utils.UrlUtil;
import com.bs.bsims.view.BSIndexEditText;
import com.bs.bsims.view.BSPopupWindowsTitle;
import com.bs.bsims.view.BSPopupWindowsTitle.TreeCallBack;
import com.bs.bsims.view.BSRefreshListView;
import com.bs.bsims.view.BSRefreshListView.OnRefreshListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//合同主页
public class CrmTradeContantHomeListActivity extends BaseActivity implements OnClickListener, OnItemClickListener {

    private Context context;
    private CrmTranctVo crmtranctsbussinesVo;
    private BSRefreshListView crm_business_indexlistview;
    private TextView mLoading;
    // listView
    private View mFootLayout;
    private TextView mMoreTextView, loadingfile1;
    private ProgressBar mProgressBar;
    private String mRefresh = "";
    // 0为首次,1为上拉刷新 ，2为下拉刷新
    private int mState = 0;
    private CrmBussinesTranctAdapter crmbussinesApdater;

    private BSIndexEditText mClearEditText;
    /** 关键词搜索（选传） */
    private String keyword = "";

    /** 上拉ID */
    private String firstid;

    /** 下拉ID */
    private String lastid;

    /** 记录最后一条的id */
    private String saveLastId;

    private String tyepkey = "0";

    private int mStatus;// 1为第一个弹出框，2为第二个弹出框
    private boolean mFlag = true;

    private CrmOptionsVO mCrmOptionsVO;
    private BSPopupWindowsTitle mBsPopupWindowsTitle;
    /**
     * 定义一个数据源 在每一次搜索之前记录当前listview中的mlist
     */
    private LinearLayout mOneTitle, mTwoTitle;
    private TextView mOneTitleTv, mTwoTitleTv;
    private List<View> mViewRightList, mViewLeftList;// 筛选选中的布局
    private LinearLayout mOptionsSelected;
    private String status = "", my = "";

    private int itmepostion = -1;
    public static final String TRADES_EDIT = "trades_edit";// 合同状态改变刷新广播
    private int mCurrentVoIndex;
    private Boolean canClickFlag = true;// 解决连续点“更多”可能会出现的异常

    private TextView mNoReadTv;
    private String mUnread;
    private LinearLayout mSerachTitleLayout;
    private String mToDo;

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.crmtradecontract_index,
                mContentLayout);
        context = this;
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

        mTitleTv.setText("合同管理");
        mOkTv.setText("添加");
        crm_business_indexlistview = (BSRefreshListView) findViewById(R.id.crm_business_indexlistview);
        crmbussinesApdater = new CrmBussinesTranctAdapter(context, true);
        crm_business_indexlistview.setAdapter(crmbussinesApdater);
        initFoot();
        loadingfile1 = (TextView) findViewById(R.id.loadingfile1);
        mClearEditText = (BSIndexEditText) findViewById(R.id.edit_single_search);
        mOneTitle = (LinearLayout) findViewById(R.id.one_title);
        mTwoTitle = (LinearLayout) findViewById(R.id.two_title);
        mOneTitleTv = (TextView) findViewById(R.id.one_title_tv);
        mTwoTitleTv = (TextView) findViewById(R.id.two_title_tv);
        mOptionsSelected = (LinearLayout) findViewById(R.id.options_selected);
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
                if (s.toString().trim().equals("")) {
                    keyword = "";
                    tyepkey = "0";
                }

            }
        });

        registBroadcast();
        mNoReadTv = (TextView) findViewById(R.id.no_read_tv);
        mSerachTitleLayout = (LinearLayout) findViewById(R.id.search_title_layout);
        mUnread = this.getIntent().getStringExtra("unread");
        mToDo = this.getIntent().getStringExtra("todo");

        // 首页进来隐藏收索条件
        if (this.getIntent().getStringExtra("msg") != null) {
            mClearEditText.setVisibility(View.GONE);
            mSerachTitleLayout.setVisibility(View.GONE);

        }
    }

    @Override
    public void bindViewsListener() {
        mOkTv.setOnClickListener(this);
        mOneTitle.setOnClickListener(this);
        mTwoTitle.setOnClickListener(this);
        bindRefreshListener();
        crm_business_indexlistview.setOnItemClickListener(this);

        mClearEditText.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) mClearEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    CrmTradeContantHomeListActivity.this
                                            .getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    keyword = mClearEditText.getText().toString().trim();
                    if (keyword.equals("")) {
                        return false;
                    }
                    tyepkey = "1";
                    crm_business_indexlistview.changeHeaderViewByState(BSRefreshListView.REFRESHING);
                    new ThreadUtil(CrmTradeContantHomeListActivity.this, CrmTradeContantHomeListActivity.this).start();
                    return true;
                }
                return false;
            }
        });

        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ("".equals(s.toString())) {
                    tyepkey = "1";
                    crm_business_indexlistview.changeHeaderViewByState(BSRefreshListView.REFRESHING);
                    new ThreadUtil(CrmTradeContantHomeListActivity.this, CrmTradeContantHomeListActivity.this).start();
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mNoReadTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_comm_head_right:
                Intent i = new Intent(CrmTradeContantHomeListActivity.this,
                        CrmTradeContantAddInfo.class);
                startActivityForResult(i, 2012);
                break;
            case R.id.one_title:
                mStatus = 1;
                initPopData();
                break;
            case R.id.two_title:
                mStatus = 2;
                initPopData();
                break;

            case R.id.no_read_tv:
                Intent noReadIntent = new Intent();
                noReadIntent.putExtra("type", 14);
                noReadIntent.setClass(this, AllNoReadActivity.class);
                startActivity(noReadIntent);
                break;
            default:
                break;
        }

    }

    @Override
    public void executeSuccess() {
        if (mFlag && mCrmOptionsVO != null && mCrmOptionsVO.getCode() != null) {
            if (Constant.RESULT_CODE.equals(mCrmOptionsVO.getCode())) {
                createOptionsLeftItems();
                createOptionsRightItems();
                mFlag = false;
            }
        }
        tyepkey = "0";
        loadingfile1.setVisibility(View.GONE);
        crm_business_indexlistview.setVisibility(View.VISIBLE);
        mContentLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
        if (1 == mState) {
            crmbussinesApdater.updateDataFrist(crmtranctsbussinesVo.getArray());
        } else if (2 == mState) {
            crmbussinesApdater.updateDataLast(crmtranctsbussinesVo.getArray());
        } else {
            crmbussinesApdater.updateData(crmtranctsbussinesVo.getArray());
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

        if (mFlag && mCrmOptionsVO != null && mCrmOptionsVO.getCode() != null) {
            if (Constant.RESULT_CODE.equals(mCrmOptionsVO.getCode())) {
                createOptionsLeftItems();
                createOptionsRightItems();
                mFlag = false;
            }
        }

        // 网络异常
        if (null == crmtranctsbussinesVo) {
            super.showNoNetView();
            return;
        }

        if (tyepkey.equals("1")) {
            crmbussinesApdater.mList.clear();
        }

        if (mFlag) {
            mFlag = false;
            if (null == mCrmOptionsVO)
                return;
            createOptionsLeftItems();
            createOptionsRightItems();
        }
        tyepkey = "0";
        mContentLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
        if (null == crmbussinesApdater.mList) {
            crm_business_indexlistview.setVisibility(View.GONE);
            loadingfile1.setVisibility(View.VISIBLE);
            return;
        }
        if (crmbussinesApdater.mList.size() > 0) {
            crm_business_indexlistview.setVisibility(View.VISIBLE);
            loadingfile1.setVisibility(View.GONE);
        } else {
            crm_business_indexlistview.setVisibility(View.GONE);
            loadingfile1.setVisibility(View.VISIBLE);
        }
        if (mState != 1)
            footViewIsVisibility();
        mState = 0;
        crm_business_indexlistview.onRefreshComplete();
        canClickFlag = true;
    }

    public boolean getData() {

        Gson gson = new Gson();
        if (mFlag) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.CRM_TRANDETRACNT_OPTION, map);
            mCrmOptionsVO = gson.fromJson(jsonStr, CrmOptionsVO.class);
        }

        Map paramsMap = null;
        if (mState == 0) {
            paramsMap = new HashMap<String, String>();
        } else if (mState == 1) {
            if (mRefresh.equals("")) {
                paramsMap = new HashMap<String, String>();
            } else {
                paramsMap = new HashMap<String, String>();
                paramsMap.put("firstid", firstid);
            }

        } else {
            lastid = saveLastId;
            paramsMap = new HashMap<String, String>();
            paramsMap.put("lastid", lastid);
        }
        paramsMap.put("status", status);
        paramsMap.put("my", my);
        paramsMap.put("keyword", keyword);
        paramsMap.put("unread", mUnread);
        paramsMap.put("todo", mToDo);
        try {
            String urlStr1 = UrlUtil.getUrlByMap1(Constant.CRM_BUSSINES_HOMEINDEXANDDETAILSOFTRANTS,
                    paramsMap);
            String jsonUrlStr1;
            jsonUrlStr1 = HttpClientUtil.get(urlStr1, Constant.ENCODING).trim();
            crmtranctsbussinesVo = gson.fromJson(jsonUrlStr1,
                    CrmTranctVo.class);
            if (crmtranctsbussinesVo.getCode().equals("200")) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
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
                        if (crmbussinesApdater.mList.size() == 0)
                            mRefresh = "";
                        else
                            firstid = crmbussinesApdater.mList.get(0).getHid();
                        new ThreadUtil(context,
                                CrmTradeContantHomeListActivity.this).start();
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
                    saveLastId = crmbussinesApdater.mList
                            .get(crmbussinesApdater.mList.size() - 1).getHid();
                    new ThreadUtil(context, CrmTradeContantHomeListActivity.this)
                            .start();
                }
            }
        });

        crm_business_indexlistview.setOnItemClickListener(this);
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
        if (crmtranctsbussinesVo == null) {
            return;
        }
        if (crmtranctsbussinesVo.getCount() == null) {
            return;
        }
        if (Integer.parseInt(crmtranctsbussinesVo.getCount()) <= 15) {
            mFootLayout.setVisibility(View.GONE);
        } else {
            mFootLayout.setVisibility(View.VISIBLE);
            mMoreTextView.setText("更多");
            mProgressBar.setVisibility(View.GONE);
        }
    }

    public void initPopData() {

        if (null == mCrmOptionsVO)
            return;

        // left
        if (mStatus == 1) {
            List<CrmOptionsVO> parentList = mCrmOptionsVO.getLeft();
            String[] parentStr = new String[parentList.size()];
            String[] parentId = new String[parentList.size()];
            String[][] childStr = new String[parentList.size()][8];
            String[][] childId = new String[parentList.size()][8];
            for (int i = 0; i < parentList.size(); i++) {
                parentStr[i] = parentList.get(i).getName();
                parentId[i] = "0";

                for (int j = 0; j < parentList.get(i).getOption().size(); j++) {
                    childStr[i][j] = parentList.get(i).getOption().get(j).getName();
                    childId[i][j] = parentList.get(i).getOption().get(j).getId();
                }
            }

            ArrayList<TreeVO> list = CommonUtils.getTreeVOListNoAll(parentStr, parentId, childStr, childId);
            mBsPopupWindowsTitle = new BSPopupWindowsTitle(this, list, callback);
            mBsPopupWindowsTitle.showPopupWindow(mOneTitle);

        } else {
            List<CrmOptionsVO> parentList = mCrmOptionsVO.getRight();
            String[] parentStr = new String[parentList.size()];
            String[] parentId = new String[parentList.size()];
            String[][] childStr = new String[parentList.size()][8];
            String[][] childId = new String[parentList.size()][8];
            for (int i = 0; i < parentList.size(); i++) {
                parentStr[i] = parentList.get(i).getName();
                parentId[i] = "0";

                for (int j = 0; j < parentList.get(i).getOption().size(); j++) {
                    childStr[i][j] = parentList.get(i).getOption().get(j).getName();
                    childId[i][j] = parentList.get(i).getOption().get(j).getId();
                }
            }

            ArrayList<TreeVO> list = CommonUtils.getTreeVOListNoAll(parentStr, childStr);
            mBsPopupWindowsTitle = new BSPopupWindowsTitle(this, list, callback);
            mBsPopupWindowsTitle.showPopupWindow(mTwoTitle);
        }
    }

    // 菜单点击回调函数
    TreeCallBack callback = new TreeCallBack() {

        @Override
        public void callback(TreeVO vo) {
            View optionsItem;
            TextView typeTv, typeContentTv;
            if (mStatus == 1) {// 左侧
                optionsItem = mViewLeftList.get(vo.getParentId() - 1);
                if ("-1".equals(vo.getSearchId())) {
                    // 隐藏“全部”弹出框
                    optionsItem.setVisibility(View.GONE);
                } else {
                    optionsItem.setVisibility(View.VISIBLE);
                }
                typeTv = (TextView) optionsItem.findViewById(R.id.type_tv);
                typeTv.setVisibility(View.GONE);// 隐藏一级菜单
                typeContentTv = (TextView) optionsItem.findViewById(R.id.type_content_tv);

                match(1, vo.getSearchId() + "");
                typeTv.setText("合同状态");
                optionsItem.setTag("1");
            }
            else {
                optionsItem = mViewRightList.get(vo.getParentId() - 1);
                if ("0".equals(vo.getChildSearchId())) {
                    // 隐藏“全部”弹出框
                    optionsItem.setVisibility(View.GONE);
                } else {
                    optionsItem.setVisibility(View.VISIBLE);
                }
                typeTv = (TextView) optionsItem.findViewById(R.id.type_tv);
                typeTv.setVisibility(View.GONE);// 隐藏一级菜单
                typeContentTv = (TextView) optionsItem.findViewById(R.id.type_content_tv);
                match(2, vo.getChildSearchId());
                typeTv.setText("合同分类");
                optionsItem.setTag("2");
            }

            typeContentTv.setText(vo.getName());

        }
    };

    public void match(int key, String value) {
        keyword = mClearEditText.getText().toString().trim();
        switch (key) {
            case 1:
                status = value;
                break;
            case 2:
                my = value;
                break;
            default:
                break;
        }
        tyepkey = "1";
        crm_business_indexlistview.changeHeaderViewByState(BSRefreshListView.REFRESHING);
        new ThreadUtil(this, this).start();
    }

    public List<View> createOptionsLeftItems() {
        mViewLeftList = new ArrayList<View>();
        if (mCrmOptionsVO.getLeft() != null) {
            for (int i = 1; i <= mCrmOptionsVO.getLeft().size(); i++) {
                // 添加3个布局存放选择的菜单
                final View optionsItem = View.inflate(CrmTradeContantHomeListActivity.this, R.layout.options_selected, null);
                optionsItem.setTag(i + "");
                mViewLeftList.add(optionsItem);
                optionsItem.setVisibility(View.GONE);
                // 选中模块点击事件
                optionsItem.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // 根据tag匹配找到点击的菜单
                        if ("1".equals(optionsItem.getTag().toString())) {
                            match(1, "");
                        } else if ("2".equals(optionsItem.getTag().toString())) {
                            match(2, "");
                        } else {
                            match(3, "");
                        }
                        optionsItem.setVisibility(View.GONE);
                    }
                });
                mOptionsSelected.addView(optionsItem);
            }
        }
        return mViewLeftList;
    }

    public List<View> createOptionsRightItems() {
        mViewRightList = new ArrayList<View>();
        if (mCrmOptionsVO.getRight() != null) {
            for (int i = 1; i <= mCrmOptionsVO.getRight().size(); i++) {
                // 添加3个布局存放选择的菜单
                final View optionsItem = View.inflate(CrmTradeContantHomeListActivity.this, R.layout.options_selected, null);
                optionsItem.setTag(i + "");
                mViewRightList.add(optionsItem);
                optionsItem.setVisibility(View.GONE);
                // 选中模块点击事件
                optionsItem.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // 根据tag匹配找到点击的菜单
                        if ("2".equals(optionsItem.getTag().toString())) {
                            match(2, "");
                        }
                        optionsItem.setVisibility(View.GONE);
                    }
                });
                mOptionsSelected.addView(optionsItem);
            }
        }
        return mViewRightList;
    }

    @SuppressLint("NewApi")
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Intent intent = new Intent();
        CrmTranctVo vo = crmbussinesApdater.mList.get((int) arg3);
        mCurrentVoIndex = (int) arg3;
        vo.setIsread("1");
        crmbussinesApdater.notifyDataSetChanged();
        String status = vo.getStatus();
        if ("2".equals(vo.getDirection())) {
            intent.setClass(this, CrmTradeContantDetailsIndexActivity.class);
            intent.putExtra("hid", crmbussinesApdater.mList.get((int) arg3).getHid());
            startActivityForResult(intent, 1);
        } else {
            intent.setClass(this, CrmTradeContantDeatilsHomeTop3Activity.class);
            intent.putExtra("hid", crmbussinesApdater.mList.get((int) arg3).getHid());
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(arg1, (int) arg1.getX(), (int) arg1.getY(), 0, 0);
            ActivityCompat.startActivity(this, intent, optionsCompat.toBundle());
        }

    }

    /**
     * 添加完合同之后添加下拉刷新事件
     */

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        switch (arg0) {
            case 2012:
                if (arg1 == 2012) {
                    if (arg2 != null) {
                        /*
                         * 返回过来直接调用下拉刷新
                         */
                        mState = 1;
                        if (crmbussinesApdater.mList.size() == 0)
                            mRefresh = "";
                        else {
                            mRefresh = 1 + "";
                            firstid = crmbussinesApdater.mList.get(0).getHid();
                        }
                        crm_business_indexlistview.changeHeaderViewByState(BSRefreshListView.REFRESHING);
                        new ThreadUtil(context, CrmTradeContantHomeListActivity.this).start();
                    }
                }
                break;

            case 1:
                if (arg2 != null) {
                    String status = arg2.getStringExtra("status");
                    // crmbussinesApdater.mList.get(mCurrentVoIndex).setStatus(status);
                    long startTime = Long.parseLong(crmbussinesApdater.mList.get(mCurrentVoIndex).getStarttime()) * 1000;
                    long endTime = Long.parseLong(crmbussinesApdater.mList.get(mCurrentVoIndex).getEndtime()) * 1000;
                    long time = System.currentTimeMillis();
                    if ("1".equals(status)) {
                        if (time < startTime) {
                            crmbussinesApdater.mList.get(mCurrentVoIndex).setStatus("1");
                            crmbussinesApdater.mList.get(mCurrentVoIndex).setStatusName("执行前");
                        } else if (time >= startTime && time <= endTime) {
                            crmbussinesApdater.mList.get(mCurrentVoIndex).setStatus("2");
                            crmbussinesApdater.mList.get(mCurrentVoIndex).setStatusName("执行中");
                        } else {
                            crmbussinesApdater.mList.get(mCurrentVoIndex).setStatus("4");
                            crmbussinesApdater.mList.get(mCurrentVoIndex).setStatusName("结束");
                        }
                    } else {
                        crmbussinesApdater.mList.get(mCurrentVoIndex).setStatus("5");
                        crmbussinesApdater.mList.get(mCurrentVoIndex).setStatusName("驳回");
                    }
                    crmbussinesApdater.notifyDataSetChanged();
                }

                break;
        }

    }

    public void registBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(TRADES_EDIT);
        this.registerReceiver(msgBroadcast, filter);
    }

    private void unRegistExitReceiver() {
        this.unregisterReceiver(msgBroadcast);
    }

    private BroadcastReceiver msgBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (TRADES_EDIT.equals(intent.getAction())) {
                String hid = intent.getStringExtra("hid");
                String status = intent.getStringExtra("status");
                String statusName = intent.getStringExtra("statusName");
                for (int i = 0; i < crmbussinesApdater.mList.size(); i++) {
                    if (hid.equals(crmbussinesApdater.mList.get(i).getHid())) {
                        crmbussinesApdater.mList.get(i).setStatus(status);
                        crmbussinesApdater.mList.get(i).setStatusName(statusName);
                        break;
                    }
                }
                crmbussinesApdater.notifyDataSetChanged();
            }
        }
    };

    protected void onDestroy() {
        super.onDestroy();
        unRegistExitReceiver();
    };

}
