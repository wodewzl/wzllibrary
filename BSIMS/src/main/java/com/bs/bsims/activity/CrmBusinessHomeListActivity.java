
package com.bs.bsims.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.bs.bsims.adapter.CrmBusinessHomeListAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.CrmBussinesList;
import com.bs.bsims.model.CrmOptionsVO;
import com.bs.bsims.model.EmployeeVO;
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

public class CrmBusinessHomeListActivity extends BaseActivity implements
        OnClickListener {

    private Context context;
    public static CrmBusinessHomeListActivity instance = null;

    private BSRefreshListView crm_business_indexlistview;
    private TextView mLoading;
    // listView
    private View mFootLayout;
    private TextView mMoreTextView, loadingfile1;
    private ProgressBar mProgressBar;
    private String mRefresh = "";
    // 0为首次,1为上拉刷新 ，2为下拉刷新
    private int mState = 0;
    private CrmBussinesList crmbulist;

    private CrmBusinessHomeListAdapter crmbussinesApdater;

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

    private String mPopstate = "";
    /**
     * 定义一个数据源 在每一次搜索之前记录当前listview中的mlist
     */
    private LinearLayout mOneTitle, mTwoTitle;
    private TextView mOneTitleTv, mTwoTitleTv;
    private List<View> mViewRightList, mViewLeftList;// 筛选选中的布局
    private String mSourcekey, mStatuskey, mDatekey, mOrderkey;
    private LinearLayout mOptionsSelected;

    private int itmepostion = -1;
    private Boolean canClickFlag = true;// 解决连续点“更多”可能会出现的异常
    private TextView mNoReadTv;
    private View layout;
    private String mUnread;
    private LinearLayout mSearchTitleLayout;

    @Override
    public void baseSetContentView() {
        // TODO Auto-generated method stub
        layout = View.inflate(this, R.layout.crmbussines_indexlist,
                mContentLayout);
        context = this;
        instance = this;
    }

    @Override
    public boolean getDataResult() {
        // TODO Auto-generated method stub
        return getData();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        if (null != getIntent().getStringExtra("bid_fuzerenname")
                && null != getIntent().getStringExtra("bidstate")
                && null != getIntent().getStringExtra("bidstateName") && -1 != itmepostion) {
            // **做假设置状态*//
            // Intent i = new Intent(mContext, CrmBusinessHomeListActivity.class);
            // i.putExtra("bid", bid);
            // i.putExtra("bidstate", crmbussinesvisitorVo.getBusiness().getStatus());
            // startActivity(i);
            if (null != crmbussinesApdater.mList && crmbussinesApdater.mList.size() > 0) {
                List<CrmBussinesList> list = new ArrayList<CrmBussinesList>();
                // crmbussinesApdater.mList.get(itmepostion).setStatus(getIntent().getStringExtra("bidstate"));
                // crmbussinesApdater.
                list.clear();
                list.addAll(crmbussinesApdater.mList);
                list.get(itmepostion).setStatus(getIntent().getStringExtra("bidstate"));
                list.get(itmepostion).setStatusName(getIntent().getStringExtra("bidstateName"));
                list.get(itmepostion).setFullname(getIntent().getStringExtra("bid_fuzerenname"));
                list.get(itmepostion).setBname(getIntent().getStringExtra("bid_name"));
                list.get(itmepostion).setMoney(getIntent().getStringExtra("bid_money"));
                crmbussinesApdater.updateData(list);
            }

        }

        if (null != getIntent().getStringExtra("blistclear") && -1 != itmepostion) {
            if (null != crmbussinesApdater.mList && crmbussinesApdater.mList.size() > 0) {
                if (null != mStatuskey) {
                    if (!mStatuskey.equals("0") && keyword.equals("")) {
                        crmbussinesApdater.mList.remove(itmepostion);
                        crmbussinesApdater.notifyDataSetChanged();
                    }
                }

            }
        }

        super.onResume();

    }

    @Override
    public void updateUi() {
        // TODO Auto-generated method stub
    }

    @Override
    public void initView() {

        // TODO Auto-generated method stub
        mTitleTv.setText("销售商机");
        mOkTv.setText("添加");
        crm_business_indexlistview = (BSRefreshListView) findViewById(R.id.crm_business_indexlistview);
        crmbussinesApdater = new CrmBusinessHomeListAdapter(context);
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
                    tyepkey = "1";
                    crm_business_indexlistview
                            .changeHeaderViewByState(BSRefreshListView.REFRESHING);
                    new ThreadUtil(CrmBusinessHomeListActivity.this,
                            CrmBusinessHomeListActivity.this).start();
                }

            }
        });
        mClearEditText.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) mClearEditText.getContext().getSystemService(
                            Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    CrmBusinessHomeListActivity.this
                                            .getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    keyword = mClearEditText.getText().toString().trim();
                    if (keyword.equals("")) {
                        return false;
                    }
                    tyepkey = "1";
                    crm_business_indexlistview
                            .changeHeaderViewByState(BSRefreshListView.REFRESHING);
                    new ThreadUtil(CrmBusinessHomeListActivity.this,
                            CrmBusinessHomeListActivity.this).start();
                    return true;
                }
                return false;
            }
        });
        // mLoading.setVisibility(View.GONE);
        // mContentLayout.setVisibility(View.VISIBLE);
        // mLoadingLayout.setVisibility(View.GONE);
        // mHeadLayout.setVisibility(View.GONE);

        mNoReadTv = (TextView) findViewById(R.id.no_read_tv);
        mSearchTitleLayout = (LinearLayout) findViewById(R.id.search_title_layout);
        mUnread = this.getIntent().getStringExtra("unread");
        if (this.getIntent().getStringExtra("msg") != null) {
            mClearEditText.setVisibility(View.GONE);
            mSearchTitleLayout.setVisibility(View.GONE);
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void bindViewsListener() {
        // TODO Auto-generated method stub
        mOkTv.setOnClickListener(this);
        mOneTitle.setOnClickListener(this);
        mTwoTitle.setOnClickListener(this);
        bindRefreshListener();
        crm_business_indexlistview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                crmbussinesApdater.mList.get((int) arg3).setIsread("1");
                crmbussinesApdater.notifyDataSetChanged();
                Intent i = new Intent(context, CrmBusinessHomeIndexOneInfo.class);
                i.putExtra("bid", crmbussinesApdater.mList.get((int) arg3).getBid());
                i.putExtra("stateUtilthread", "1");// 回来更改状态
                itmepostion = arg2 - 1;
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(arg1, (int) arg1.getX(), (int) arg1.getY(), 0, 0);
                ActivityCompat.startActivity(CrmBusinessHomeListActivity.this, i, optionsCompat.toBundle());
                // startActivity(i);

            }

        });
        mNoReadTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.txt_comm_head_right:
                Intent i = new Intent(CrmBusinessHomeListActivity.this,
                        CrmBusinessAddInfoMsgActivity.class); // 跳转到添加商机的页面
                // startActivity(i);
                startActivityForResult(i, 2015);
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
                noReadIntent.putExtra("type", 13);
                noReadIntent.setClass(this, AllNoReadActivity.class);
                startActivity(noReadIntent);
                break;
            default:
                break;
        }

    }

    @Override
    public void executeSuccess() {
        // TODO Auto-generated method stub
        if (mFlag) {
            if (mCrmOptionsVO == null || null == mCrmOptionsVO.getLeft()
                    || null == mCrmOptionsVO.getRight()) {
                return;
            }
            createOptionsLeftItems();
            createOptionsRightItems();
            mFlag = false;
        }
        tyepkey = "0";
        loadingfile1.setVisibility(View.GONE);
        crm_business_indexlistview.setVisibility(View.VISIBLE);
        mContentLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
        // crmbussinesApdater = new CrmBusinessHomeListAdapter(context,
        // crmbulist.getArray());
        // crm_business_indexlistview.setAdapter(crmbussinesApdater);
        // crmbussinesApdater.notifyDataSetChanged();
        if (1 == mState) {
            crmbussinesApdater.updateDataFrist(crmbulist.getArray());
        } else if (2 == mState) {
            crmbussinesApdater.updateDataLast(crmbulist.getArray());
        } else {
            crmbussinesApdater.updateData(crmbulist.getArray());
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
        if (null == crmbulist) {
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
            String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle()
                    + Constant.CRM_BUSSINES_OPTION, map);
            try {
                mCrmOptionsVO = gson.fromJson(jsonStr, CrmOptionsVO.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

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
        paramsMap.put("source", mSourcekey);
        paramsMap.put("status", mStatuskey);
        paramsMap.put("date", mDatekey);
        paramsMap.put("type", mOrderkey);
        paramsMap.put("unread", mUnread);

        if (getIntent().getStringExtra("uid") != null) {// 仪表盘盘跳转传递所需的参数（人员筛选）
            paramsMap.put("uid", getIntent().getStringExtra("uid"));
        }
        if (getIntent().getStringExtra("did") != null) {
            paramsMap.put("did", getIntent().getStringExtra("did"));
        }
        paramsMap.put("mode", getIntent().getStringExtra("type"));
        paramsMap.put("option", getIntent().getStringExtra("option"));
        String urlStr = UrlUtil.getUrlByMap1(Constant.CRM_BUSSINES_GETLIST,
                paramsMap);
        String jsonUrlStr;
        try {
            List<EmployeeVO> lists = new ArrayList<EmployeeVO>();
            jsonUrlStr = HttpClientUtil.get(urlStr, Constant.ENCODING).trim();
            crmbulist = gson.fromJson(jsonUrlStr, CrmBussinesList.class);
            if (crmbulist.getCode().equals("200")) {
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
                        if (crmbussinesApdater.mList.size() == 0)
                            mRefresh = "";
                        else
                            firstid = crmbussinesApdater.mList.get(0).getBid();
                        new ThreadUtil(context,
                                CrmBusinessHomeListActivity.this).start();
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
                            .get(crmbussinesApdater.mList.size() - 1).getBid();
                    new ThreadUtil(context, CrmBusinessHomeListActivity.this)
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
        if (crmbulist == null) {
            return;
        }
        if (crmbulist.getCount() == null) {
            return;
        }
        if (Integer.parseInt(crmbulist.getCount()) <= 15) {
            mFootLayout.setVisibility(View.GONE);
            // listView.removeFooterView(mFootLayout);
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
            if (null == mCrmOptionsVO.getLeft())
                return;

            List<CrmOptionsVO> parentList = mCrmOptionsVO.getLeft();
            String[] parentStr = new String[parentList.size()];
            String[][] childStr = new String[parentList.size()][8];
            for (int i = 0; i < parentList.size(); i++) {
                parentStr[i] = parentList.get(i).getName();

                for (int j = 0; j < parentList.get(i).getOption().size(); j++) {
                    childStr[i][j] = parentList.get(i).getOption().get(j).getName();
                }
            }

            ArrayList<TreeVO> list = CommonUtils.getTreeVOListNoAll(parentStr, childStr);
            mBsPopupWindowsTitle = new BSPopupWindowsTitle(this, list, callback);
            mBsPopupWindowsTitle.showPopupWindow(mOneTitle);
        } else {
            if (null == mCrmOptionsVO.getRight())
                return;
            List<CrmOptionsVO> parentList = mCrmOptionsVO.getRight();
            String[] parentStr = new String[parentList.size()];
            String[][] childStr = new String[parentList.size()][8];
            for (int i = 0; i < parentList.size(); i++) {
                parentStr[i] = parentList.get(i).getName();

                for (int j = 0; j < parentList.get(i).getOption().size(); j++) {
                    childStr[i][j] = parentList.get(i).getOption().get(j).getName();
                }
            }

            ArrayList<TreeVO> list = CommonUtils.getTreeVOListNoAll(parentStr, childStr);
            mBsPopupWindowsTitle = new BSPopupWindowsTitle(this, list, callback);
            mBsPopupWindowsTitle.showPopupWindow(mOneTitle);
        }
    }

    // 菜单点击回调函数
    TreeCallBack callback = new TreeCallBack() {

        @Override
        public void callback(TreeVO vo) {
            // 审批二级菜单
            // mOneTitleTv.setText(vo.getName());
            View optionsItem;
            TextView typeTv, typeContentTv;
            if (mStatus == 1) {// 左侧
                optionsItem = mViewLeftList.get(vo.getParentId() - 1);
                if ("0".equals(vo.getChildSearchId())) {
                    // 隐藏“全部”弹出框
                    optionsItem.setVisibility(View.GONE);
                } else {
                    optionsItem.setVisibility(View.VISIBLE);
                }
                typeTv = (TextView) optionsItem.findViewById(R.id.type_tv);
                typeTv.setVisibility(View.GONE);// 隐藏一级菜单
                typeContentTv = (TextView) optionsItem.findViewById(R.id.type_content_tv);

                switch (vo.getParentId()) {
                    case 1:
                        match(1, vo.getChildSearchId());
                        typeTv.setText("商机来源");
                        optionsItem.setTag("1");
                        break;
                    case 2:
                        match(2, vo.getChildSearchId());
                        typeTv.setText("商机状态");
                        optionsItem.setTag("2");
                        break;
                    case 3:
                        match(3, vo.getChildSearchId());
                        typeTv.setText("结单日期");
                        optionsItem.setTag("3");
                        break;
                }
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
                match(4, vo.getChildSearchId());
                typeTv.setText("商机分类");
                optionsItem.setTag("4");
            }

            typeContentTv.setText(vo.getName());

        }
    };

    public void match(int key, String value) {
        switch (key) {
            case 1:
                mSourcekey = value;
                break;
            case 2:
                mStatuskey = value;
                break;
            case 3:
                mDatekey = value;
                break;
            case 4:
                mOrderkey = value;
                break;

            // case 5:
            // mFristid = value;
            // mState = 1;
            // break;
            // case 6:
            // mLastid = value;
            // mState = 2;
            // break;
            // case 7:
            // mKeyword = value;
            // break;
            default:
                break;
        }
        tyepkey = "1";
        crm_business_indexlistview.changeHeaderViewByState(BSRefreshListView.REFRESHING);
        new ThreadUtil(this, this).start();
    }

    public List<View> createOptionsLeftItems() {
        mViewLeftList = new ArrayList<View>();
        for (int i = 1; i <= mCrmOptionsVO.getLeft().size(); i++) {
            // 添加3个布局存放选择的菜单
            final View optionsItem = View.inflate(CrmBusinessHomeListActivity.this,
                    R.layout.options_selected, null);
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
        return mViewLeftList;
    }

    public List<View> createOptionsRightItems() {
        mViewRightList = new ArrayList<View>();
        for (int i = 1; i <= mCrmOptionsVO.getRight().size(); i++) {
            // 添加3个布局存放选择的菜单
            final View optionsItem = View.inflate(CrmBusinessHomeListActivity.this,
                    R.layout.options_selected, null);
            optionsItem.setTag(i + "");
            mViewRightList.add(optionsItem);
            optionsItem.setVisibility(View.GONE);
            // 选中模块点击事件
            optionsItem.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // 根据tag匹配找到点击的菜单
                    if ("4".equals(optionsItem.getTag().toString())) {
                        match(4, "");
                    }
                    optionsItem.setVisibility(View.GONE);
                }
            });
            mOptionsSelected.addView(optionsItem);
        }
        return mViewRightList;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);// must store the new intent unless getIntent() will
                          // return the old one
    }

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
                        if (crmbussinesApdater.mList.size() == 0)
                            mRefresh = "";
                        else {
                            mRefresh = 1 + "";
                            firstid = crmbussinesApdater.mList.get(0).getBid();
                        }
                        crm_business_indexlistview
                                .changeHeaderViewByState(BSRefreshListView.REFRESHING);
                        new ThreadUtil(context, CrmBusinessHomeListActivity.this).start();

                    }
                }

                break;

            default:
                break;
        }
    }

}
