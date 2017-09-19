
package com.bs.bsims.activity;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.bs.bsims.R;
import com.bs.bsims.adapter.NoticeAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.PublishResultVO;
import com.bs.bsims.model.PublishVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSIndexEditText;
import com.bs.bsims.view.BSRefreshListView;
import com.bs.bsims.view.BSRefreshListView.OnRefreshListener;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

public class NoticeActivity extends BaseActivity implements OnItemClickListener, OnClickListener {

    private NoticeAdapter mNoticeAdapter;
    private List<PublishVO> mList;

    // listView
    private BSRefreshListView mRefreshListView;
    private View mFootLayout;
    private TextView mMoreTextView;
    private ProgressBar mProgressBar;
    // 0为首次,1为下拉刷新 ，2为加载跟多
    private int mState = 0;

    private String mSortid, mNoticeid;

    private PublishResultVO mPublishResultVO;

    private BSIndexEditText mBSBsIndexEditText;
    private String mKeyword = "";
    private int mCurrentVO;
    private String mIsBoss = "0";
    private String mIsShow = "0";
    private View mNoContentView;
    private Boolean canClickFlag = true;// 解决连续点“更多”可能会出现的异常
    private TextView mNoReadTv;
    private String mFirstId, mLastId;
    private String mUnread;

    @Override
    public void initView() {

        mRefreshListView = (BSRefreshListView) findViewById(R.id.lv_refresh);
        Intent intent = getIntent();
        mUnread = intent.getStringExtra("unread");
        mNoticeid = intent.getStringExtra("noticeid");
        mSortid = intent.getStringExtra("sortid");
        mIsBoss = intent.getStringExtra("isboss");
        mIsShow = intent.getStringExtra("isshow");
        if (intent.getStringExtra("isboss") != null) {
            mIsBoss = intent.getStringExtra("isboss");
        }
        if ("3".equals(mSortid)) {
            mTitleTv.setText("通知");
            if (CommonUtils.getLimitsPublish(Constant.LIMITS_PUBLISH004) || CommonUtils.getLimitsPublish(Constant.LIMITS_PUBLISH005)) {
                mOkTv.setText("发布");
                mOkTv.setVisibility(View.VISIBLE);
            } else {
                mOkTv.setVisibility(View.GONE);
            }

        } else if ("11".equals(mSortid)) {
            mTitleTv.setText("公文");
            if (CommonUtils.getLimitsPublish(Constant.LIMITS_PUBLISH006)) {
                mOkTv.setText("发布");
                mOkTv.setVisibility(View.VISIBLE);
            } else {
                mOkTv.setVisibility(View.GONE);
            }

        } else if ("19".equals(mSortid)) {
            mOkTv.setVisibility(View.GONE);
            mTitleTv.setText("企业风采");

        } else if ("12".equals(mSortid)) {
            mTitleTv.setText("制度");
            if (CommonUtils.getLimitsPublish(Constant.LIMITS_PUBLISH007)) {
                mOkTv.setText("发布");
                mOkTv.setVisibility(View.VISIBLE);
            } else {
                mOkTv.setVisibility(View.GONE);
            }

        }
        mNoticeAdapter = new NoticeAdapter(this, mSortid);
        mRefreshListView.setAdapter(mNoticeAdapter);

        mBSBsIndexEditText = (BSIndexEditText) findViewById(R.id.edit_single_search);
        mNoContentView = findViewById(R.id.no_content_view);

        initFoot();
        mNoReadTv = (TextView) findViewById(R.id.no_read_tv);

        // 首页进来隐藏收索条件
        if (intent.getStringExtra("msg") != null) {
            mBSBsIndexEditText.setVisibility(View.GONE);
        }

        setLeadClass("NoticeActivity");
    }

    @Override
    public void bindViewsListener() {

        mOkTv.setOnClickListener(this);
        bindRefreshListener();
        mRefreshListView.setOnItemClickListener(this);
        mNoReadTv.setOnClickListener(this);
        mBSBsIndexEditText.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) mBSBsIndexEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    NoticeActivity.this
                                            .getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);

                    mKeyword = mBSBsIndexEditText.getText().toString();
                    clearData();
                    mRefreshListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
                    new ThreadUtil(NoticeActivity.this, NoticeActivity.this).start();
                    return true;
                }
                return false;
            }
        });
    }

    private void clearData() {
        mNoticeAdapter.mList.clear();
        mFootLayout.setVisibility(View.GONE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mBSBsIndexEditText.getWindowToken(), 0);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    @Override
    public void updateUi() {
    }

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.notice, mContentLayout);
    }

    public void geRefrsh() {

        if (0 == mState) {
            mFirstId = "";
            mLastId = "";
        } else if (1 == mState) {
            if (mNoticeAdapter.mList.size() > 0) {
                String id = mNoticeAdapter.mList.get(0).getArticleid();
                mFirstId = id;
                mLastId = "";
            } else {
                mFirstId = "";
                mLastId = "";
            }
        } else if (2 == mState) {
            String id = mNoticeAdapter.mList.get(mNoticeAdapter.mList.size() - 1).getArticleid();
            mLastId = id;
            mFirstId = "";
        }
    }

    public boolean getData() {
        geRefrsh();
        try {
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            map.put("sortid", mSortid);
            map.put("keyword", mKeyword);
            map.put("notice", mNoticeid);
            map.put("isboss", mIsBoss);
            map.put("lastid", mLastId);
            map.put("firstid", mFirstId);
            map.put("unread", mUnread);
            String jsonStr1 = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.PUBLISH_LIST, map);
            mPublishResultVO = gson.fromJson(jsonStr1, PublishResultVO.class);

            if (Constant.RESULT_CODE.equals(mPublishResultVO.getCode())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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

    /**
     * 加载更多是否隐藏
     * 
     * @param datas
     */
    protected void footViewIsVisibility(List<PublishVO> datas) {
        if (mPublishResultVO == null) {
            return;
        }
        if (mPublishResultVO.getCount() == null) {
            return;
        }
        if (Integer.parseInt(mPublishResultVO.getCount()) <= 15) {
            mFootLayout.setVisibility(View.GONE);
        } else {
            mFootLayout.setVisibility(View.VISIBLE);
            mMoreTextView.setText("更多");
            mProgressBar.setVisibility(View.GONE);
        }
    }

    public void bindRefreshListener() {
        mRefreshListView.setonRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                mState = 1;
                new ThreadUtil(NoticeActivity.this, NoticeActivity.this).start();
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
                    new ThreadUtil(NoticeActivity.this, NoticeActivity.this).start();
                }
            }
        });
    }

    @Override
    public void executeSuccess() {
        super.executeSuccess();
        if (1 == mState) {
            mNoticeAdapter.updateDataFrist(mPublishResultVO.getArray());
        } else if (2 == mState) {
            mNoticeAdapter.updateDataLast(mPublishResultVO.getArray());
        } else {
            mNoticeAdapter.updateData(mPublishResultVO.getArray());
        }
        mNoticeAdapter.notifyDataSetChanged();
        mRefreshListView.onRefreshComplete();
        mLoading.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
        mNoContentView.setVisibility(View.GONE);
        if (mState != 1) {
            footViewIsVisibility(mPublishResultVO.getArray());
        }
        mState = 0;
        canClickFlag = true;
    }

    @Override
    public void executeFailure() {

        mNoticeAdapter.notifyDataSetChanged();
        mRefreshListView.onRefreshComplete();
        footViewIsVisibility(mNoticeAdapter.mList);

        if (mPublishResultVO != null) {
            if (mState == 0) {
                CommonUtils.setNonetIcon(this, mLoading, this);
                mLoading.setVisibility(View.VISIBLE);
                mContentLayout.setVisibility(View.VISIBLE);
                mLoadingLayout.setVisibility(View.GONE);
                mNoContentView.setVisibility(View.VISIBLE);
            }
        } else {
            CommonUtils.setNonetIcon(this, mLoading, this);
        }
        mState = 0;
        canClickFlag = true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        mCurrentVO = (int) id;
        intent.putExtra("articleid", mNoticeAdapter.mList.get(mCurrentVO).getArticleid());
        intent.putExtra("sortid", mSortid);
        intent.setClass(this, NoticeDetailActivity.class);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        if (arg2 != null && mNoticeAdapter.mList.size() > mCurrentVO) {
            mNoticeAdapter.mList.get(mCurrentVO).setIsread("1");
            mNoticeAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_comm_head_right:
                Intent intent = new Intent();
                intent.setClass(this, NoticePublishActivity.class);
                intent.putExtra("sortid", mSortid);
                startActivity(intent);
                break;
            case R.id.no_read_tv:
                Intent noReadIntent = new Intent();
                noReadIntent.putExtra("type", 1);
                noReadIntent.putExtra("sortid", mSortid);
                noReadIntent.putExtra("isboss", BSApplication.getInstance().getUserFromServerVO().getIsboss());
                noReadIntent.setClass(this, AllNoReadActivity.class);

                startActivity(noReadIntent);
                break;
            default:
                break;
        }

    }

}
