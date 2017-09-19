
package com.bs.bsims.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.CrmVisitorIndexListAdapter;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.CrmOptionsVO;
import com.bs.bsims.model.CrmVisitorVo;
import com.bs.bsims.ui.datepicker.WheelView;
import com.bs.bsims.utils.BsPermissionUtils;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.utils.UrlUtil;
import com.bs.bsims.view.BSPopupWindowsTitle;
import com.bs.bsims.view.BSRefreshListView;
import com.bs.bsims.view.BSRefreshListView.OnRefreshListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrmVisitorSignActivity extends BaseActivity implements OnClickListener {

    private CrmVisitorIndexListAdapter crmIndexListAdapter;

    private Context context;

    private CrmVisitorVo mVisitor;

    private BSRefreshListView crm_business_indexlistview;
    private TextView mLoading;
    // listView
    private View mFootLayout;
    private TextView mMoreTextView;
    private ProgressBar mProgressBar;
    private String mRefresh = "";
    // 0为首次,1为上拉刷新 ，2为下拉刷新
    private int mState = 0;

    private LinearLayout visitor_add_state, one_title, two_title;
 

    /** 上拉ID */
    private String firstid;

    /** 下拉ID */
    private String lastid;

    /** 记录最后一条的id */
    private String saveLastId;

    private BSPopupWindowsTitle mBsPopupWindowsTitle;

    private TextView one_title_tv, two_title_tv;// 第一个筛选框

    private CrmOptionsVO mCrmOptionsVO;

    private boolean canClickFlag = true;

    private WheelView year;
    private WheelView month;
    private WheelView day;

    private int norYear;
    private int curMonth;
    private int curDate;

    private TextView back, sure, txt_comm_head_right;

    private LinearLayout date_task_changedate_select_ly;
    /**
     * 重新选择的时间
     */
    private String time;
    /**
     * 部门筛选的id
     */
    private String did;
    private String date;// 时间

    /**
     * 保存每次用户操作的数据源
     */
    private List<CrmVisitorVo> UserSaveList = new ArrayList<CrmVisitorVo>();

    private String Pagecount = "";// 保存的分页的count

    private String mSelectKey = "";

    @Override
    public void baseSetContentView() {
        // TODO Auto-generated method stub
        View view = View.inflate(this, R.layout.crmbussines_fragment2_zone, mContentLayout);
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

    @SuppressLint("NewApi")
    @Override
    public void initView() {

        // TODO Auto-generated method stub
        txt_comm_head_right = (TextView) findViewById(R.id.txt_comm_head_right);
        txt_comm_head_right.setVisibility(View.VISIBLE);
        // 说明与拜访记录捆绑
        if (null != getIntent().getStringExtra("SelectKey")
                && getIntent().getStringExtra("SelectKey").equals("1")) {
            crmIndexListAdapter = new CrmVisitorIndexListAdapter(context);
            crmIndexListAdapter.setState("3");
            mTitleTv.setText("关联位置");
            mSelectKey = getIntent().getStringExtra("SelectKey");
        }

        // 显示列表，不显示多选
        else {
            mTitleTv.setText("上报位置");
            txt_comm_head_right.setCompoundDrawablesWithIntrinsicBounds(
                    getResources().getDrawable(R.drawable.crm_visitor_sign), null, null, null);
            crmIndexListAdapter = new CrmVisitorIndexListAdapter(context);
            crmIndexListAdapter.setState("2");
        }

        crm_business_indexlistview = (BSRefreshListView) findViewById(R.id.fragment_sharedfilesd_home_all_refreshlistview1);
        crm_business_indexlistview.setAdapter(crmIndexListAdapter);
        initFoot();
 

    }

    @Override
    public void bindViewsListener() {
        // TODO Auto-generated method stub
        mOkTv.setOnClickListener(this);
        bindRefreshListener();

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.txt_comm_head_right:
                Intent i = new Intent();
                if (mSelectKey.equals("1")) {
                    if (txt_comm_head_right.getText().toString().trim().equals("确定")) {
                        if (null == crmIndexListAdapter.cVo
                                || null == crmIndexListAdapter.cVo.getAddress()) {
                            CustomToast.showShortToast(context, "请关联位置!");
                            return;
                        }

                        i.putExtra("vIsitor", crmIndexListAdapter.cVo);
                        setResult(1111, i);
                        this.finish();
                    }
                    else {

                        if (!BsPermissionUtils.isOPenGps(this)) {
                            BsPermissionUtils.openGPS(this);
                        }
                        else {
                            i.putExtra("bol", true);
                            i.setClass(context, CrmVisitorSignAddInfoActivity.class);
                            startActivityForResult(i, 2015);
                        }

                    }
                }
                else {
                    if (!BsPermissionUtils.isOPenGps(this)) {
                        BsPermissionUtils.openGPS(this);
                    }
                    else {
                        i.putExtra("bol", true);
                        i.setClass(context, CrmVisitorSignAddInfoActivity.class);
                        startActivityForResult(i, 2015);
                    }
                }

                break;

            default:
                break;
        }

    }

    @Override
    public void executeSuccess() {
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
        for (int i = 0; i < crmIndexListAdapter.mList.size(); i++) {
            crmIndexListAdapter.mList.get(i).setFalgecontant("0");
        }
        if (mState != 1)
            footViewIsVisibility();
        mState = 0;
        crm_business_indexlistview.setVisibility(View.VISIBLE);
        crm_business_indexlistview.onRefreshComplete();

        if (mSelectKey.equals("1")) {
            if ((mVisitor.getArray() != null && mVisitor.getArray().size() > 0)
                    || (null != crmIndexListAdapter.mList && crmIndexListAdapter.mList.size() > 0)) {
                txt_comm_head_right.setText("确定");
            }
            else {
                txt_comm_head_right.setText("添加");
            }
        }
        canClickFlag = true;
    }

    @Override
    public void executeFailure() {
        // TODO Auto-generated method stub

        // 网络异常
        if (null == mVisitor) {
            super.showNoNetView();
            return;
        }

        mContentLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
        if (null == crmIndexListAdapter.mList) {
            if (mSelectKey.equals("1"))
                txt_comm_head_right.setText("添加");
            return;
        }
        if (crmIndexListAdapter.mList.size() > 0) {
            if (mSelectKey.equals("1")) {
                txt_comm_head_right.setText("确定");
            }

        } else {
            if (mSelectKey.equals("1"))
                txt_comm_head_right.setText("添加");
        }
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
        } else if (mState == 1) {
            if (mRefresh.equals("")) {
                paramsMap = new HashMap<String, String>();
            } else {
                // firstid = crmbussinesApdater.mList.get(0).getBid();
                paramsMap = new HashMap<String, String>();
                paramsMap.put("firstid", firstid);
            }

        } else {
            lastid = saveLastId;// mEXTSharedfilesdHomeMyUpdateAdapter.mList.get(mEXTSharedfilesdHomeMyUpdateAdapter.mList.size()
                                // - 1).getSharedid();
            paramsMap = new HashMap<String, String>();
            paramsMap.put("lastid", lastid);
        }
        // Map map = new HashMap<String, String>();
        paramsMap.put("current", mSelectKey);
        String urlStr = UrlUtil.getUrlByMap1(Constant.CRM_VISIT_SIGNGETDATA,
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
                            firstid = crmIndexListAdapter.mList.get(0).getCsid();
                        new ThreadUtil(context,
                                CrmVisitorSignActivity.this).start();
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
                            .get(crmIndexListAdapter.mList.size() - 1).getCsid();
                    new ThreadUtil(context, CrmVisitorSignActivity.this)
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

    // 从列表点击添加拜访记录之后 回来之后调用下拉刷新
    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        switch (arg0) {
            case 2015:
                if (arg1 == 2016) {
                    if (arg2 != null) {
                        /*
                         * 返回过来直接调用下拉刷新
                         */
                        mState = 1;
                        if (crmIndexListAdapter.mList.size() == 0)
                            mRefresh = "";
                        else {
                            mRefresh = 1 + "";
                            firstid = crmIndexListAdapter.mList.get(0).getCsid();
                        }
                        crm_business_indexlistview
                                .changeHeaderViewByState(BSRefreshListView.REFRESHING);
                        new ThreadUtil(context, CrmVisitorSignActivity.this).start();

                    }
                }
                break;

        }
    }

}
