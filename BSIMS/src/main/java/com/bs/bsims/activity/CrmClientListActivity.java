
package com.bs.bsims.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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
import com.bs.bsims.adapter.CrmListAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.CrmCreateClinetFromContactsVO;
import com.bs.bsims.model.CrmListVO;
import com.bs.bsims.model.CrmOptionsVO;
import com.bs.bsims.model.TreeVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CommonUtils.ResultCallback;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSIndexEditText;
import com.bs.bsims.view.BSPopupWindowsTitle;
import com.bs.bsims.view.BSPopupWindowsTitle.TreeCallBack;
import com.bs.bsims.view.BSRefreshListView;
import com.bs.bsims.view.BSRefreshListView.OnRefreshListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CrmClientListActivity extends BaseActivity implements OnClickListener, OnItemClickListener {
    public static final String DETAIL_EDIT = "detail_edit";
    public static final String CLIENT_ADD = "client_add";
    private static final int SELECT_CONTACTS = 5;
    private String[] array = {
            "手工输入", "通过联系人创建"
    };

    private BSRefreshListView mRefreshListView;
    private CrmListAdapter mCrmListAdapter;
    private CrmListVO mCrmListVO;
    private CrmOptionsVO mCrmOptionsVO;

    // 下拉上拉刷新部分
    private View mFootLayout;
    private TextView mMoreTextView;
    private ProgressBar mProgressBar;
    private int mState = 0; // 0为首次,1为上拉刷新 ，2为下拉刷新

    private boolean mFlag = true;
    private BSPopupWindowsTitle mBsPopupWindowsTitleLeft, mBsPopupWindowsTitleRight;
    private LinearLayout mOneTitle, mTwoTitle;
    private TextView mOneTitleTv, mTwoTitleTv;
    private int mStatus;// 1为第一个弹出框，2为第二个弹出框
    private String mFristid, mLastid, mKeyword, mType, mSource, mLevel, mOrder;
    private LinearLayout mOptionsSelected;
    private List<View> mViewList;// 筛选选中的布局
    private BSIndexEditText mBSBsIndexEditText;

    private CrmListVO mCurrentClickVO;
    private List<CrmOptionsVO> mListLeft = new ArrayList<CrmOptionsVO>();
    private List<CrmOptionsVO> mListRight = new ArrayList<CrmOptionsVO>();
    private CrmCreateClinetFromContactsVO mContactsVo;
    private Boolean canClickFlag = true;// 解决连续点“更多”可能会出现的异常
    private TextView mNoReadTv, mCustomerCount;
    private String mUnread;
    private LinearLayout mSearchLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.crm_client_list, mContentLayout);

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
        mTitleTv.setText("客户管理");
        mOkTv.setText("添加");

        mOneTitle = (LinearLayout) findViewById(R.id.one_title);
        mTwoTitle = (LinearLayout) findViewById(R.id.two_title);
        mOneTitleTv = (TextView) findViewById(R.id.one_title_tv);
        mTwoTitleTv = (TextView) findViewById(R.id.two_title_tv);

        mRefreshListView = (BSRefreshListView) findViewById(R.id.lv_refresh);
        mCrmListAdapter = new CrmListAdapter(this);
        mRefreshListView.setAdapter(mCrmListAdapter);
        mBSBsIndexEditText = (BSIndexEditText) findViewById(R.id.edit_single_search);
        mOptionsSelected = (LinearLayout) findViewById(R.id.options_selected);
        mNoReadTv = (TextView) findViewById(R.id.no_read_tv);
        View listHeadView = getLayoutInflater().inflate(R.layout.nn, null);
        mCustomerCount = (TextView) listHeadView.findViewById(R.id.customer_head);
        mRefreshListView.addHeaderView(listHeadView);
        mSearchLayout = (LinearLayout) findViewById(R.id.client_title_layout);
        initData();
        initFoot();
        createOptionsItems();
        registBroadcast();
    }

    public void initData() {
        getOptionsData();
        mUnread = this.getIntent().getStringExtra("unread");

        // 首页进来隐藏收索条件
        if (this.getIntent().getStringExtra("msg") != null) {
            mBSBsIndexEditText.setVisibility(View.GONE);
            mSearchLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void bindViewsListener() {
        mOkTv.setOnClickListener(this);
        mRefreshListView.setOnItemClickListener(this);
        mOneTitle.setOnClickListener(this);
        mTwoTitle.setOnClickListener(this);

        mRefreshListView.setonRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {

                if (mCrmListAdapter.mList.size() > 0) {
                    match(5, mCrmListAdapter.mList.get(0).getCid());
                } else {
                    mFristid = "";
                    match(5, "");
                }

            }
        });
        mFootLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canClickFlag) {
                    canClickFlag = false;
                    mMoreTextView.setText("正在加载...");
                    mProgressBar.setVisibility(View.VISIBLE);
                    match(6, mCrmListAdapter.mList.get(mCrmListAdapter.mList.size() - 1).getCid());
                }
            }
        });

        mBSBsIndexEditText.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) mBSBsIndexEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(CrmClientListActivity.this
                            .getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    match(7, mBSBsIndexEditText.getText().toString());
                    return true;
                }
                return false;
            }
        });

        mBSBsIndexEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ("".equals(s.toString()))
                    match(7, s.toString());
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_comm_head_right:
                CommonUtils.initPopViewBg(CrmClientListActivity.this, array, mOkTv, mCallback, CommonUtils.getScreenWidth(CrmClientListActivity.this) / 3);
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
                noReadIntent.putExtra("type", 15);
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
            if (position == 0) {
                Intent intent = new Intent();
                intent.setClass(CrmClientListActivity.this, CrmClientAddActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent();
                intent.putExtra("type", "5");
                intent.putExtra("name", "选择联系人");
                intent.putExtra("resulut_code", SELECT_CONTACTS);
                intent.setClass(CrmClientListActivity.this, CrmOptionsListActivity.class);
                startActivityForResult(intent, SELECT_CONTACTS);

            }

        }
    };

    // 联系人创建回调函数
    ResultCallback mContactsCallback = new ResultCallback() {

        @Override
        public void callback(String str, int position) {
            Intent intent = new Intent();
            intent.setClass(CrmClientListActivity.this, CrmClientAddActivity.class);
            String id = str.split(",")[0];
            String name = str.split(",")[1];

            intent.putExtra("id", id);
            for (int i = 0; i < mContactsVo.getArray().size(); i++) {
                if (id.equals(mContactsVo.getArray().get(i).getLid())) {
                    intent.putExtra("name", mContactsVo.getArray().get(i).getName());
                    break;
                }
            }

            // 设置结果，并进行传送
            startActivity(intent);
        }
    };

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        if (((int) arg3) < 0) {
            return;
        }
        if (mCrmListAdapter.mList.size() > 0) {
            mCurrentClickVO = mCrmListAdapter.mList.get((int) arg3);
            mCurrentClickVO.setIsread("1");
            mCrmListAdapter.notifyDataSetChanged();
            Intent intent = new Intent();
            intent.putExtra("cid", mCurrentClickVO.getCid());
            intent.putExtra("address", mCurrentClickVO.getAddress());
            intent.putExtra("name", mCurrentClickVO.getName());
            intent.putExtra("level", mCurrentClickVO.getLevel());
            intent.putExtra("souce", mCurrentClickVO.getSource());
            intent.putExtra("oldfullname", mCurrentClickVO.getOldfullname());
            intent.putExtra("fullname", mCurrentClickVO.getFullname());
            intent.putExtra("crmEdit", mCurrentClickVO.getCrmEdit());
            if ("1".equals(mCurrentClickVO.getIsPub())) {

                if ("1".equals(CommonUtils.getLimitsSpecial(Constant.LIMITS_SPECIAL004))) {
                    intent.setClass(this, CrmClientHomeActivity.class);
                    // 公海特殊权限
                    intent.putExtra("publish_sp", "1");
                    intent.putExtra("visit_count", mCurrentClickVO.getVisitCount());
                    intent.putExtra("contacts_count", mCurrentClickVO.getContactsCount());
                    intent.putExtra("business_count", mCurrentClickVO.getBusinessCount());
                    intent.putExtra("contract_count", mCurrentClickVO.getContractCount());
                } else {
                    intent.setClass(this, CrmHighseasClientsHomeActivity.class);
                }

                startActivityForResult(intent, 1);
            } else {
                intent.putExtra("visit_count", mCurrentClickVO.getVisitCount());
                intent.putExtra("contacts_count", mCurrentClickVO.getContactsCount());
                intent.putExtra("business_count", mCurrentClickVO.getBusinessCount());
                intent.putExtra("contract_count", mCurrentClickVO.getContractCount());
                intent.setClass(this, CrmClientHomeActivity.class);
                startActivityForResult(intent, 1);
            }
        }
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent intent) {
        super.onActivityResult(arg0, arg1, intent);
        if (intent == null)
            return;

        if (arg0 == SELECT_CONTACTS) {

            Intent intentAdd = new Intent();
            intentAdd.setClass(CrmClientListActivity.this, CrmClientAddActivity.class);
            String id = intent.getStringExtra("id");
            intentAdd.putExtra("id", id);
            for (int i = 0; i < mContactsVo.getArray().size(); i++) {
                if (id.equals(mContactsVo.getArray().get(i).getLid())) {
                    intentAdd.putExtra("name", mContactsVo.getArray().get(i).getName());
                    break;
                }
            }
            // 设置结果，并进行传送
            startActivity(intentAdd);
        } else {
            if (intent != null && mCurrentClickVO != null && mCrmListAdapter.mList.contains(mCurrentClickVO)) {
                if (intent.hasExtra("cid")) {
                    for (int i = 0; i < mCrmListAdapter.mList.size(); i++) {
                        if (intent.getStringExtra("cid").equals(mCrmListAdapter.mList.get(i).getCid())) {
                            mCrmListAdapter.mList.get(i).setContactsCount(intent.getStringExtra("contactsCount"));
                            mCrmListAdapter.mList.get(i).setBusinessCount(intent.getStringExtra("bussnessCount"));
                            mCrmListAdapter.mList.get(i).setContractCount(intent.getStringExtra("tradeCount"));
                            mCrmListAdapter.mList.get(i).setVisitCount(intent.getStringExtra("visitorCount"));
                            break;
                        }
                    }

                } else {
                    mCrmListAdapter.mList.remove(mCurrentClickVO);
                }
                mCrmListAdapter.notifyDataSetChanged();

            }
        }

    }

    @Override
    public void executeSuccess() {
        super.executeSuccess();
        if (mCrmOptionsVO != null && Constant.RESULT_CODE.equals(mCrmOptionsVO.getCode())) {
            mListRight = mCrmOptionsVO.getRight();
            mListLeft = mCrmOptionsVO.getLeft();
            mBsPopupWindowsTitleLeft = new BSPopupWindowsTitle(this, getTreeVOList(mListLeft), callback);
            mBsPopupWindowsTitleRight = new BSPopupWindowsTitle(this, getTreeVOList(mListRight), callback);
        }

        if (1 == mState) {
            mCrmListAdapter.updateDataFrist(mCrmListVO.getArray());
        } else if (2 == mState) {
            mCrmListAdapter.updateDataLast(mCrmListVO.getArray());
        } else {
            mCrmListAdapter.updateData(mCrmListVO.getArray());
        }

        mCrmListAdapter.notifyDataSetChanged();
        mRefreshListView.onRefreshComplete();

        if ("".equals(mFristid) || mState != 1) {
            footViewIsVisibility();
        }
        mState = 0;
        canClickFlag = true;
        try {
            CommonUtils.setDifferentTextColor(mCustomerCount, "当前客户总量  ", mCrmListVO.getTotalCount(), getResources().getColor(R.color.red));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void executeFailure() {
        if (mCrmOptionsVO != null && Constant.RESULT_CODE.equals(mCrmOptionsVO.getCode())) {
            mListRight = mCrmOptionsVO.getRight();
            mListLeft = mCrmOptionsVO.getLeft();
            mBsPopupWindowsTitleLeft = new BSPopupWindowsTitle(this, getTreeVOList(mListLeft), callback);
            mBsPopupWindowsTitleRight = new BSPopupWindowsTitle(this, getTreeVOList(mListRight), callback);
        }
        // 列表展示的时候不能调用父类
        super.isRequestFinish();
        mCrmListAdapter.notifyDataSetChanged();
        mRefreshListView.onRefreshComplete();
        footViewIsVisibility();
        try {
            if (mCustomerCount.getText().equals("")) {
                mCustomerCount.setText("");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        // 不适合只隐藏列表，适合隐藏整个布局;
        if (mCrmListVO == null) {
            mCustomerCount.setText("");
            super.showNoNetView();

        } else {
            if (mState == 0) {
                mCustomerCount.setText("");
                mCrmListAdapter.updateData(new ArrayList<CrmListVO>());
                mFootLayout.setVisibility(View.GONE);
            }
        }

        mState = 0;
        canClickFlag = true;
    }

    public void footViewIsVisibility() {
        if (mCrmListVO == null || mCrmListVO.getCount() == null) {
            return;
        }
        if (Integer.parseInt(mCrmListVO.getCount()) <= 15) {
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

    public boolean getData() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> mapList = new HashMap<String, String>();
            mapList.put("userid", BSApplication.getInstance().getUserId());

            if (0 == mState) {
                mFristid = "";
                mLastid = "";
            }
            mapList.put("firstid", mFristid);
            mapList.put("lastid", mLastid);
            mapList.put("keyword", mKeyword);
            mapList.put("type", mType);
            mapList.put("source", mSource);
            mapList.put("level", mLevel);
            mapList.put("order", mOrder);
            mapList.put("unread", mUnread);
            if (getIntent().getStringExtra("uid") != null) {// 仪表盘盘跳转传递所需的参数（人员筛选）
                mapList.put("uid", getIntent().getStringExtra("uid"));
            }
            if (getIntent().getStringExtra("did") != null) {
                mapList.put("did", getIntent().getStringExtra("did"));
            }
            mapList.put("mode", getIntent().getStringExtra("type"));
            mapList.put("option", getIntent().getStringExtra("option"));
            mapList.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.CRM_CLIENT_LIST, mapList);
            mCrmListVO = gson.fromJson(jsonStrList, CrmListVO.class);
            if (Constant.RESULT_CODE.equals(mCrmListVO.getCode())) {
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

    // 菜单点击回调函数
    TreeCallBack callback = new TreeCallBack() {

        @Override
        public void callback(TreeVO vo) {
            if (vo.getLevel() == 1) {
                // 审批一级菜单
                match(4, vo.getParentSerachId());
            } else {

                // 审批二级菜单
                if (mStatus == 1) {
                    // 左边的2个菜单 由于菜单有所改动，为了改的方便
                    if ("0".equals(vo.getChildSearchId())) {
                        // 隐藏“全部”弹出框
                        mViewList.get(vo.getParentId()).setVisibility(View.GONE);
                    } else {
                        mViewList.get(vo.getParentId()).setVisibility(View.VISIBLE);
                    }
                    TextView typeTv = (TextView) mViewList.get(vo.getParentId()).findViewById(R.id.type_tv);
                    typeTv.setVisibility(View.GONE);// 隐藏一级菜单
                    TextView typeContentTv = (TextView) mViewList.get(vo.getParentId()).findViewById(R.id.type_content_tv);
                    switch (vo.getParentId()) {
                        case 1:
                            match(2, vo.getChildSearchId());
                            typeTv.setText("客户级别");
                            mViewList.get(vo.getParentId()).setTag((vo.getParentId() + 1) + "");
                            break;
                        case 2:
                            match(3, vo.getChildSearchId());
                            typeTv.setText("客户来源");
                            mViewList.get(vo.getParentId()).setTag((vo.getParentId() + 1) + "");
                            break;

                        default:
                            break;
                    }
                    typeContentTv.setText(vo.getName());
                } else {
                    // 左边的1个菜单

                    TextView typeTv = (TextView) mViewList.get(0).findViewById(R.id.type_tv);
                    typeTv.setVisibility(View.GONE);// 隐藏一级菜单
                    TextView typeContentTv = (TextView) mViewList.get(0).findViewById(R.id.type_content_tv);
                    typeTv.setText("客户分类");
                    mViewList.get(0).setTag("1");
                    if ("0".equals(vo.getChildSearchId())) {
                        // 隐藏“全部”弹出框
                        mViewList.get(0).setVisibility(View.GONE);
                    } else {
                        mViewList.get(0).setVisibility(View.VISIBLE);
                    }
                    match(1, vo.getChildSearchId());
                    typeContentTv.setText(vo.getName());
                }
            }
        }
    };

    public List<View> createOptionsItems() {
        mViewList = new ArrayList<View>();
        for (int i = 1; i <= 3; i++) {
            // 添加3个布局存放选择的菜单
            final View optionsItem = View.inflate(CrmClientListActivity.this, R.layout.options_selected, null);
            mViewList.add(optionsItem);
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
        return mViewList;
    }

    public void initPopData() {
        if (mStatus == 1 && mBsPopupWindowsTitleLeft != null) {
            mBsPopupWindowsTitleLeft.showPopupWindow(mOneTitle);
        } else if (mStatus == 2 && mBsPopupWindowsTitleRight != null) {
            mBsPopupWindowsTitleRight.showPopupWindow(mOneTitle);
        }
    }

    public ArrayList<TreeVO> getTreeVOList(List<CrmOptionsVO> parentList) {
        String[] parentStr = new String[parentList.size()];
        String[][] childStr = new String[parentList.size()][6];
        for (int i = 0; i < parentList.size(); i++) {
            parentStr[i] = parentList.get(i).getName();

            for (int j = 0; j < parentList.get(i).getOption().size(); j++) {
                childStr[i][j] = parentList.get(i).getOption().get(j).getName();
            }
        }

        ArrayList<TreeVO> list = CommonUtils.getTreeVOListNoAll(parentStr, childStr);

        return list;
    }

    public void match(int key, String value) {
        mKeyword = mBSBsIndexEditText.getText().toString();

        switch (key) {

            case 0:
                mLevel = "";
                mSource = "";
                break;

            case 1:
                mType = value;
                break;
            case 2:
                mLevel = value;
                break;
            case 3:
                mSource = value;
                break;
            case 4:
                mOrder = value;
                break;

            case 5:
                mFristid = value;
                mLastid = "";
                mState = 1;
                break;
            case 6:
                mLastid = value;
                mFristid = "";
                mState = 2;
                break;
            default:
                break;
        }
        mRefreshListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
        new ThreadUtil(this, this).start();
    }

    public void registBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(DETAIL_EDIT);
        filter.addAction(CLIENT_ADD);
        this.registerReceiver(msgBroadcast, filter);
    }

    private void unRegistExitReceiver() {
        this.unregisterReceiver(msgBroadcast);
    }

    private BroadcastReceiver msgBroadcast = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (DETAIL_EDIT.equals(intent.getAction())) {
                if (intent.getStringExtra("address") != null) {
                    mCurrentClickVO.setAddress(intent.getStringExtra("address"));
                }
                if (intent.getStringExtra("name") != null) {
                    mCurrentClickVO.setName(intent.getStringExtra("name"));
                }

                if (intent.getStringExtra("level") != null) {
                    mCurrentClickVO.setLevel(intent.getStringExtra("level"));
                }

                mCrmListAdapter.notifyDataSetChanged();
            } else if (CLIENT_ADD.equals(intent.getAction())) {
                if (mCrmListAdapter.mList.size() > 0) {
                    match(5, mCrmListAdapter.mList.get(0).getCid());
                } else {
                    mFristid = "";
                    match(5, "");
                }
            }
        }
    };

    protected void onDestroy() {
        super.onDestroy();
        unRegistExitReceiver();
    };

    public void getOptionsData() {
        new Thread() {
            public void run() {
                try {
                    Gson gson = new Gson();
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("userid", BSApplication.getInstance().getUserId());
                    map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
                    String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.CRM_CLIENT_OPTION, map);
                    mCrmOptionsVO = gson.fromJson(jsonStr, CrmOptionsVO.class);

                    HashMap<String, String> paramsMap = new HashMap<String, String>();
                    paramsMap.put("userid", BSApplication.getInstance().getUserId());
                    paramsMap.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
                    String jsonUrlStr1 = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.CRM_CREATE_CLIENT_FROM_CONTACTS, paramsMap);
                    mContactsVo = gson.fromJson(jsonUrlStr1, CrmCreateClinetFromContactsVO.class);

                    if (HttpClientUtil.isNetworkConnected(CrmClientListActivity.this)) {
                        if (mContactsVo == null) {
                            // if (Constant.RESULT_CODE400.equals(mContactsVo.getCode()))
                            // return;
                            getOptionsData();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
