
package com.bs.bsims.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.bs.bsims.view.BSDialog;
import com.bs.bsims.view.BSIndexEditText;
import com.bs.bsims.view.BSPopupWindowsTitle;
import com.bs.bsims.view.BSPopupWindowsTitle.TreeCallBack;
import com.bs.bsims.view.BSRefreshListView;
import com.bs.bsims.view.BSRefreshListView.OnRefreshListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CrmApprovalTradeContantActivity extends BaseActivity implements OnClickListener, OnItemClickListener {
    private String[] mTitleRightArray = {
            "已添加回款计划", "未添加回款计划"
    };
    private String[] mTitleLeftArray;
    private String[] mTitleLeftArrayId;
    private BSRefreshListView mRefreshListView;
    private CrmBussinesTranctAdapter mCrmApprovalAdapter;
    private CrmTranctVo mCrmApprovalVO;

    // 下拉上拉刷新部分
    private View mFootLayout;
    private TextView mMoreTextView;
    private ProgressBar mProgressBar;
    private int mState = 0; // 0为首次,1为上拉刷新 ，2为下拉刷新

    private boolean mFlag = true;
    private BSPopupWindowsTitle mBsPopupWindowsTitle;
    private LinearLayout mOneTitle, mTwoTitle;
    private TextView mOneTitleTv, mTwoTitleTv;
    private int mStatus;// 1为第一个弹出框，2为第二个弹出框
    private String mFristid, mLastid, mKeyword, mType, mSource, mLevel, mOrder;
    private LinearLayout mOptionsSelected;
    private List<View> mViewList;// 筛选选中的布局
    private BSIndexEditText mBSBsIndexEditText;
    private String mOneStr, mTwoStr;
    private View mNoContentView;
    private BSDialog mDialog;
    private CrmTranctVo mCurrentCrmApprovalVO;
    private CrmOptionsVO mCrmOptionsVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.crm_approval_payment, mContentLayout);

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
        mTitleTv.setText("合同列表");

        mOneTitle = (LinearLayout) findViewById(R.id.title01);
        mTwoTitle = (LinearLayout) findViewById(R.id.title02);
        findViewById(R.id.title03).setVisibility(View.GONE);
        mOneTitleTv = (TextView) findViewById(R.id.title_name_01);
        mTwoTitleTv = (TextView) findViewById(R.id.title_name_02);
        mOneTitleTv.setText("合同状态");
        mTwoTitleTv.setText("回款计划");
        mRefreshListView = (BSRefreshListView) findViewById(R.id.lv_refresh);
        mCrmApprovalAdapter = new CrmBussinesTranctAdapter(this);
        mRefreshListView.setAdapter(mCrmApprovalAdapter);
        mBSBsIndexEditText = (BSIndexEditText) findViewById(R.id.edit_single_search);
        mOptionsSelected = (LinearLayout) findViewById(R.id.options_selected);
        mNoContentView = findViewById(R.id.no_content_view);

        initData();
        initFoot();
    }

    public void initData() {
    }

    @Override
    public void bindViewsListener() {
        mRefreshListView.setOnItemClickListener(this);
        mOneTitle.setOnClickListener(this);
        mTwoTitle.setOnClickListener(this);

        mRefreshListView.setonRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {

                if (mCrmApprovalAdapter.mList.size() > 0) {
                    match(1, mCrmApprovalAdapter.mList.get(0).getHid());
                } else {
                    mFristid = "";
                    match(1, "");
                }

            }
        });

        mFootLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mMoreTextView.setText("正在加载...");
                mProgressBar.setVisibility(View.VISIBLE);
                match(2, mCrmApprovalAdapter.mList.get(mCrmApprovalAdapter.mList.size() - 1).getHid());
            }
        });

        mBSBsIndexEditText.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) mBSBsIndexEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    CrmApprovalTradeContantActivity.this
                                            .getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    match(3, mBSBsIndexEditText.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.title01:
                if (mTitleLeftArray == null)
                    return;
                mStatus = 1;
                initPopData();
                break;
            case R.id.title02:
                mStatus = 2;
                initPopData();
                break;
            default:
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Intent intent = new Intent();
        mCurrentCrmApprovalVO = mCrmApprovalAdapter.mList.get((int) arg3);
        intent.putExtra("hid", mCurrentCrmApprovalVO.getHid());
        intent.setClass(this, CrmTradeContantDetailsIndexActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent intent) {
        super.onActivityResult(arg0, arg1, intent);

        if (intent != null) {
            String status = intent.getStringExtra("status");
            if ("0".equals(status)) {
                mCurrentCrmApprovalVO.setStatus("2");
            } else {
                mCurrentCrmApprovalVO.setStatus("3");
            }
            mCrmApprovalAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void executeSuccess() {

        if (mCrmOptionsVO != null) {
            List<CrmOptionsVO> crmOptionsVOs = mCrmOptionsVO.getLeft().get(0).getOption();
            mTitleLeftArray = new String[crmOptionsVOs.size()];
            mTitleLeftArrayId = new String[crmOptionsVOs.size()];
            for (int i = 0; i < crmOptionsVOs.size(); i++) {
                mTitleLeftArray[i] = crmOptionsVOs.get(i).getName();
                mTitleLeftArrayId[i] = crmOptionsVOs.get(i).getId();
            }
        }

        if (1 == mState) {
            mCrmApprovalAdapter.updateDataFrist(mCrmApprovalVO.getArray());
        } else if (2 == mState) {
            mCrmApprovalAdapter.updateDataLast(mCrmApprovalVO.getArray());
        } else {
            mCrmApprovalAdapter.updateData(mCrmApprovalVO.getArray());
        }

        mLoading.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
        mNoContentView.setVisibility(View.GONE);
        mCrmApprovalAdapter.notifyDataSetChanged();
        mRefreshListView.onRefreshComplete();

        if (mState != 1) {
            footViewIsVisibility();
        }
        mState = 0;
    }

    @Override
    public void executeFailure() {
        if (mCrmOptionsVO != null && Constant.RESULT_CODE.equals(mCrmOptionsVO.getCode())) {
            List<CrmOptionsVO> crmOptionsVOs = mCrmOptionsVO.getLeft().get(0).getOption();
            mTitleLeftArray = new String[crmOptionsVOs.size()];
            mTitleLeftArrayId = new String[crmOptionsVOs.size()];
            for (int i = 0; i < crmOptionsVOs.size(); i++) {
                mTitleLeftArray[i] = crmOptionsVOs.get(i).getName();
                mTitleLeftArrayId[i] = crmOptionsVOs.get(i).getId();
            }
        }

        mCrmApprovalAdapter.notifyDataSetChanged();
        mRefreshListView.onRefreshComplete();
        footViewIsVisibility();

        // 不适合只隐藏列表，适合隐藏整个布局;
        if (mCrmApprovalVO == null) {
            super.showNoNetView();
        } else {
            if (mState == 0) {
                mCrmApprovalAdapter.updateData(new ArrayList<CrmTranctVo>());
                mLoading.setVisibility(View.GONE);
                mContentLayout.setVisibility(View.VISIBLE);
                mLoadingLayout.setVisibility(View.GONE);
                mNoContentView.setVisibility(View.VISIBLE);
                mFootLayout.setVisibility(View.GONE);
            }
        }

        mState = 0;
    }

    public void footViewIsVisibility() {
        if (mCrmApprovalVO == null || mCrmApprovalVO.getCount() == null) {
            return;
        }
        if (Integer.parseInt(mCrmApprovalVO.getCount()) <= 15) {
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

            if (mFlag) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("userid", BSApplication.getInstance().getUserId());
                map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
                String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.CRM_TRANDETRACNT_OPTION, map);
                mCrmOptionsVO = gson.fromJson(jsonStr, CrmOptionsVO.class);
                mFlag = false;
            }

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());

            if (0 == mState) {
                mFristid = "";
                mLastid = "";
            }
            map.put("firstid", mFristid);
            map.put("lastid", mLastid);
            map.put("keyword", mKeyword);
            map.put("status", mOneStr);
            map.put("isPlan", mTwoStr);

            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.CRM_APPROVAL_TRADECONTACT_LIST, map);
            mCrmApprovalVO = gson.fromJson(jsonStrList, CrmTranctVo.class);
            if (Constant.RESULT_CODE.equals(mCrmApprovalVO.getCode())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 菜单点击回调函数
    TreeCallBack callback = new TreeCallBack() {

        @Override
        public void callback(TreeVO vo) {
            if (vo.getLevel() == 1) {
                // 审批一级菜单
                if (mStatus == 1) {
                    mOneTitleTv.setText(vo.getName());
                    match(4, vo.getSearchId() + "");
                } else {
                    mTwoTitleTv.setText(vo.getName());
                    match(5, vo.getParentSerachId());
                }
            }
        }
    };

    public void initPopData() {
        ArrayList<TreeVO> list;
        if (mStatus == 1) {
            list = CommonUtils.getOneLeveTreeVoZero(mTitleLeftArray, mTitleLeftArrayId);
        } else {

            list = CommonUtils.getOneLeveTreeVo(mTitleRightArray);
        }
        mBsPopupWindowsTitle = new BSPopupWindowsTitle(this, list, callback, ViewGroup.LayoutParams.WRAP_CONTENT);

        mBsPopupWindowsTitle.showPopupWindow(mOneTitle);
    }

    public void match(int key, String value) {
        mKeyword = "";
        mFristid = "";
        mLastid = "";
        switch (key) {

            case 1:
                mFristid = value;
                mState = 1;
                break;
            case 2:
                mLastid = value;
                mState = 2;
                break;
            case 3:
                mKeyword = value;
                break;

            case 4:
                mOneStr = value;
                break;
            case 5:
                mTwoStr = value;
                break;

            default:
                break;
        }
        mRefreshListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
        new ThreadUtil(this, this).start();
    }

}
