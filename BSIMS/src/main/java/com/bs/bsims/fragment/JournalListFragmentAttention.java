
package com.bs.bsims.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.bs.bsims.activity.JournalPublishDetailActivity;
import com.bs.bsims.adapter.JournalListAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.interfaces.UpdateCallback;
import com.bs.bsims.model.JournalListVO1;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSIndexEditText;
import com.bs.bsims.view.BSRefreshListView;
import com.bs.bsims.view.BSRefreshListView.OnRefreshListener;
import com.bs.bsims.view.PinnedSectionListView;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JournalListFragmentAttention extends BaseFragment implements OnItemClickListener, OnClickListener, UpdateCallback {
    private String mFristid, mLastid, mKeyWord;
    private int mState = 0; // 0为首次,1为上拉刷新 ，2为下拉刷新
    private JournalListVO1 mListVO;
    private Activity mActivity;
    private LinearLayout mOneTitle, mTwoTitle, mThreeTitle;
    private TextView mOneTitleTv, mTwoTitleTv, mThreeTitleTv;
    private TextView mAllTv, mAttentionTv;
    private PinnedSectionListView mRefreshListView;
    private JournalListAdapter mAdapter;

    // 下拉上拉刷新部分
    private View mFootLayout;
    private TextView mMoreTextView;
    private ProgressBar mProgressBar;
    private boolean mCanClickFlag = true;
    private BSIndexEditText mBSBsIndexEditText;
    private String UPDATE_ATTENTION = "update_attention";
    private boolean mAttentionFlag = false;

    public static JournalListFragmentAttention newInstance() {
        JournalListFragmentAttention fragmentAttention = new JournalListFragmentAttention();
        return fragmentAttention;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_journal_list_attention, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        bindViewsListener();
        registBroadcast();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRefreshListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
        new ThreadUtil(mActivity, this).start();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean execute() {
        return getData();
    }

    @Override
    public void executeSuccess() {
        mRefreshListView.setVisibility(View.VISIBLE);
        mRefreshListView.onRefreshComplete();
        // mLoadingLayout.setVisibility(View.GONE);
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
        // super.isRequestFinish();
        mAdapter.notifyDataSetChanged();
        mRefreshListView.onRefreshComplete();
        footViewIsVisibility();

        if (mListVO == null) {
            // super.showNoNetView();
            // CommonUtils.setNonetContent(mActivity, mLoading, "没有相关信息");
            mAdapter.updateData(new ArrayList<JournalListVO1>());
            mFootLayout.setVisibility(View.GONE);
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
    public void onClick(View v) {
    }

    @Override
    public void onItemClick(AdapterView<?> parentView, View arg1, int position, long arg3) {
        if (mAdapter.mList.size() > 0 && mAdapter.getItemViewType((int) arg3) != -10) {
            JournalListVO1 vo = mAdapter.mList.get(mAdapter.mPinnedList.get((int) arg3));
            // JournalListVO1 vo = (JournalListVO1) parentView.getAdapter().getItem(position);
            Intent intent = new Intent();
            intent.putExtra("ismyfavor", "1");
            intent.putExtra("logid", vo.getLogid());
            intent.putExtra("listvo", (Serializable) mAdapter.mList);
            intent.setClass(mActivity, JournalPublishDetailActivity.class);
            mAttentionFlag = false;
            this.startActivityForResult(intent, 2);
        }
    }

    public void initView(View view) {
        mBSBsIndexEditText = (BSIndexEditText) view.findViewById(R.id.edit_single_search);
        mRefreshListView = (PinnedSectionListView) view.findViewById(R.id.refresh_listview);
        mAdapter = new JournalListAdapter(mActivity);
        mRefreshListView.setAdapter(mAdapter);
        mRefreshListView.setAdapter(mAdapter);
        initFoot();

    }

    // 加载更多数据
    public void initFoot() {
        mFootLayout = LayoutInflater.from(mActivity).inflate(R.layout.listview_bottom_more, null);
        mMoreTextView = (TextView) mFootLayout.findViewById(R.id.txt_loading);
        mMoreTextView.setText("更多");
        mProgressBar = (ProgressBar) mFootLayout.findViewById(R.id.progressBar);
        mFootLayout.setVisibility(View.GONE);
        mRefreshListView.addFooterView(mFootLayout);
    }

    public void bindViewsListener() {
        mRefreshListView.setOnItemClickListener(this);
        mBSBsIndexEditText.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) mBSBsIndexEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    match(3, mBSBsIndexEditText.getText().toString());
                    return true;
                }
                return false;
            }
        });

        bindRefreshListener();
    }

    public void bindRefreshListener() {
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

    public boolean getData() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());

            if (0 == mState) {
                mFristid = "";
                mLastid = "";
            }
            map.put("ismyfavor", "1");
            map.put("firstid", mFristid);
            map.put("lastid", mLastid);
            map.put("keyword", mKeyWord);
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

    public void match(int key, String value) {
        mKeyWord = mBSBsIndexEditText.getText().toString();
        switch (key) {
            case 1:
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
                mKeyWord = value;
                break;
            default:
                break;
        }
        mRefreshListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
        new ThreadUtil(mActivity, this).start();
    }

    @Override
    public String getFragmentName() {
        return null;
    }

    @Override
    public void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        if (arg2 == null)
            return;
        if (!mAttentionFlag) {
            List<JournalListVO1> list = (List<JournalListVO1>) arg2.getSerializableExtra("listvo");
            mAdapter.updateData(list);
        }
    }

    @Override
    public void onDestroy() {
        mActivity.unregisterReceiver(msgBroadcast);
        super.onDestroy();
    }

    public void registBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.UPLOAD_HEAD_ICON_MSG);
        filter.addAction(UPDATE_ATTENTION);
        mActivity.registerReceiver(msgBroadcast, filter);
    }

    private void unRegistExitReceiver() {
        mActivity.unregisterReceiver(msgBroadcast);
    }

    private BroadcastReceiver msgBroadcast = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constant.UPLOAD_HEAD_ICON_MSG.equals(intent.getAction()) || UPDATE_ATTENTION.equals(intent.getAction())) {
                mAttentionFlag = intent.getBooleanExtra("is_attention", false);
                new ThreadUtil(mActivity, JournalListFragmentAttention.this).start();
            }
        }
    };

 
}
