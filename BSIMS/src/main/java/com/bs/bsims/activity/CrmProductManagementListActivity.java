
package com.bs.bsims.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.bs.bsims.R;
import com.bs.bsims.adapter.CrmProductManagementListAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.CrmProductManagementResultVO;
import com.bs.bsims.model.CrmProductManagementVO;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSIndexEditText;
import com.bs.bsims.view.BSRefreshListView;
import com.bs.bsims.view.BSRefreshListView.OnRefreshListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CrmProductManagementListActivity extends BaseActivity {
    private BSRefreshListView mJournalListView;
    private BSIndexEditText mBSBsIndexEditText;
    private CrmProductManagementListAdapter crmProductManagementListAdapter;
    private CrmProductManagementVO crmProductManagementVo;
    private CrmProductManagementResultVO crmProductManagementResultVo;
    private View mFootLayout;
    private TextView mMoreTextView;
    private ProgressBar mProgressBar;

    private List<CrmProductManagementVO> crmListVo;
    private String mFristid, mLastid, mKeyword;

    // 0为首次,1为上拉刷新 ，2为下拉刷新
    private int mState = 0;
    private View mNoContentView;

    // private String tyepkey = "0";

    // 保存的数据源
    private List<CrmProductManagementVO> crmListVoSaveByuser;

    private String editString = "";

    private String Pagecount = "";// 保存的分页的count

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.crmproduct_management_list, null);
        mContentLayout.addView(layout);
        crmListVoSaveByuser = new ArrayList<CrmProductManagementVO>();

    }

    @Override
    public void updateUi() {
        // crmProductManagementListAdapter.mList=crmProductManagementResultVo.getArray();
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    public boolean getData() {
        try {
            HashMap<String, String> map = new HashMap<String, String>();
            if (0 == mState) {
                mFristid = "";
                mLastid = "";
            }
            map.put("firstid", mFristid);
            map.put("lastid", mLastid);
            map.put("userid", BSApplication.getInstance().getUserId());
            // map.put("userid", "26");
            map.put("keyword", mKeyword);
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            // map.put(Constant.FTOKEN_PARAMS, "RBDTZXGUMNDKkEwMkZFN0UyMTA1RgO0O0OO0O0O");
            String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle()
                    + Constant.CRM_PRODUCT_MANAGEMENT_LIST, map);
            Gson gson = new Gson();
            crmProductManagementResultVo = gson.fromJson(jsonStr,
                    CrmProductManagementResultVO.class);

            if (Constant.RESULT_CODE.equals(crmProductManagementResultVo.getCode())) {
                // mJournalListView.setAdapter(crmProductManagementListAdapter);
                return true;
            } else {
                // if (tyepkey.equals("1")) {
                // crmProductManagementListAdapter.mList.clear();
                // }
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void initView() {
        mTitleTv.setText("产品管理");
        mOkTv.setText("添加");

        mJournalListView = (BSRefreshListView) findViewById(R.id.crmproduct_list);
        mBSBsIndexEditText = (BSIndexEditText) findViewById(R.id.edit_single_search);
        crmProductManagementListAdapter = new CrmProductManagementListAdapter(this);
        // crmProductManagementListAdapter.notifyDataSetChanged();
        mJournalListView.setAdapter(crmProductManagementListAdapter);

        List<BaseAdapter> list = new ArrayList<BaseAdapter>();
        list.add(crmProductManagementListAdapter);
        initFoot();

        mNoContentView = findViewById(R.id.no_content_view);

    }

    public void initFoot() {
        mFootLayout = LayoutInflater.from(this).inflate(R.layout.listview_bottom_more, null);
        mMoreTextView = (TextView) mFootLayout.findViewById(R.id.txt_loading);
        mMoreTextView.setText("更多");
        mProgressBar = (ProgressBar) mFootLayout.findViewById(R.id.progressBar);
        // mProgressBar.setVisibility(View.GONE);
        mFootLayout.setVisibility(View.GONE);
        mJournalListView.addFooterView(mFootLayout);
    }

    @Override
    public void bindViewsListener() {

        // 根据输入框输入值的改变来过滤搜索
        mBSBsIndexEditText.addTextChangedListener(new TextWatcher() {

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
                if (s.toString().equals("") && crmListVoSaveByuser.size() > 0) {
                    mKeyword = "";
                    editString = "";
                    // loadingfile1.setVisibility(View.GONE);
                    // crm_business_indexlistview.setVisibility(View.VISIBLE)
                    mNoContentView.setVisibility(View.GONE);
                    crmProductManagementListAdapter.updateData(crmListVoSaveByuser);
                    if (Integer.parseInt(Pagecount) <= 15) {
                        mFootLayout.setVisibility(View.GONE);
                        // listView.removeFooterView(mFootLayout);
                    } else {
                        mFootLayout.setVisibility(View.VISIBLE);
                        mMoreTextView.setText("更多");
                        mProgressBar.setVisibility(View.GONE);
                    }
                }

            }
        });

        mOkTv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent in = new Intent(CrmProductManagementListActivity.this,
                        CrmProductManagementAddInfoActivity.class);
                startActivityForResult(in, 1001);
            }
        });

        mJournalListView.setonRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                if (crmProductManagementListAdapter.mList.size() > 0) {
                    match(5, crmProductManagementListAdapter.mList.get(0).getPid());
                } else {
                    mFristid = "";
                    match(5, "");
                }

            }
        });

        bindRefreshListener();
    }

    public void bindRefreshListener() {

        mFootLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mMoreTextView.setText("正在加载...");
                mProgressBar.setVisibility(View.VISIBLE);
                mState = 2;
                match(6, CrmProductManagementListAdapter.mList.
                        get(CrmProductManagementListAdapter.mList.size() - 1).getPid());
                // new ThreadUtil(CrmProductManagementListActivity.this,
                // CrmProductManagementListActivity.this).start();
            }
        });

        mBSBsIndexEditText.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) mBSBsIndexEditText.getContext().getSystemService(
                            Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    CrmProductManagementListActivity.this
                                            .getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    editString = mBSBsIndexEditText.getText().toString();
                    if (null == editString || editString.equals("")) {
                        editString = "";
                        return false;
                    }
                    match(7, mBSBsIndexEditText.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    public void executeSuccess() {
        if (editString.equals("")) {
            if (mState != 1) {
                crmListVoSaveByuser.addAll(crmProductManagementResultVo.getArray());
                Pagecount = crmProductManagementResultVo.getCount();
            } else {
                crmListVoSaveByuser.addAll(0, crmProductManagementResultVo.getArray());
            }
        }
        // crmListVo.size();
        mContentLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
        if (1 == mState) {
            crmProductManagementListAdapter
                    .updateDataFrist(crmProductManagementResultVo.getArray());
        } else if (2 == mState) {
            crmProductManagementListAdapter.updateDataLast(crmProductManagementResultVo.getArray());
        } else {
            crmProductManagementListAdapter.updateData(crmProductManagementResultVo.getArray());
        }

        // crmListVo.clear();
        // crmListVo.addAll(crmProductManagementListAdapter.mList);

        if (mState != 1) {
            footViewIsVisibility();
        }
        mState = 0;
        mLoading.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
        mNoContentView.setVisibility(View.GONE);
        crmProductManagementListAdapter.notifyDataSetChanged();
        mJournalListView.onRefreshComplete();

    }

    protected void footViewIsVisibility() {

        if (crmProductManagementResultVo == null || crmProductManagementResultVo.getCount() == null) {
            // mFootLayout.setVisibility(View.GONE);
            return;
        }

        if (Integer.parseInt(crmProductManagementResultVo.getCount()) <= 15) {
            mFootLayout.setVisibility(View.GONE);
        } else {
            mFootLayout.setVisibility(View.VISIBLE);
            mMoreTextView.setText("更多");
            mProgressBar.setVisibility(View.GONE);
        }
    }

    public void match(int key, String value) {
        switch (key) {
            case 5:
                mFristid = value;
                mLastid = "";
                mState = 1;
                break;
            case 6:
                mFristid = "";
                mLastid = value;
                mState = 2;
                break;
            case 7:
                mKeyword = value;
                break;
            default:
                break;
        }
        mJournalListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
        new ThreadUtil(this, this).start();
    }

    @Override
    public void executeFailure() {
        crmProductManagementListAdapter.notifyDataSetChanged();
        mJournalListView.onRefreshComplete();
        footViewIsVisibility();

        // 不适合只隐藏列表，适合隐藏整个布局;
        if (crmProductManagementResultVo == null) {
            super.showNoNetView();
        } else {
            if (mState == 0) {
                crmProductManagementListAdapter.updateData(new ArrayList<CrmProductManagementVO>());
                mLoading.setVisibility(View.GONE);
                mContentLayout.setVisibility(View.VISIBLE);
                mLoadingLayout.setVisibility(View.GONE);
                mNoContentView.setVisibility(View.VISIBLE);
                mFootLayout.setVisibility(View.GONE);
            }
        }

        // tyepkey = "0";
        // mContentLayout.setVisibility(View.VISIBLE);
        // mLoadingLayout.setVisibility(View.GONE);
        // if (null == crmProductManagementListAdapter.mList) {
        // mJournalListView.setVisibility(View.GONE);
        // // mMoreTextView.setVisibility(View.GONE);
        // return;
        // }
        // if (crmProductManagementListAdapter.mList.size() > 0) {
        // mJournalListView.setVisibility(View.VISIBLE);
        // } else {
        // mJournalListView.setVisibility(View.GONE);
        // // mMoreTextView.setVisibility(View.GONE);
        // }
        // mJournalListView.onRefreshComplete();
        //
        mState = 0;
    }

    /**
     * 添加完合同之后添加下拉刷新事件
     */

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        switch (arg0) {
            case 1001:
                if (arg1 == 1001) {
                    if (arg2 != null) {
                        /*
                         * 返回过来直接调用下拉刷新
                         */
                        mState = 1;
                        if (crmProductManagementListAdapter.mList.size() == 0)
                            mState = 0;
                        else {
                            mFristid = crmProductManagementListAdapter.mList.get(0).getPid();
                        }
                        mJournalListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
                        new ThreadUtil(CrmProductManagementListActivity.this,
                                CrmProductManagementListActivity.this).start();

                    }
                }
                break;

        }

    }

}
