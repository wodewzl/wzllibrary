
package com.bs.bsims.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.JournalPublishDetailActivity;
import com.bs.bsims.activity.JournalSerachActivity;
import com.bs.bsims.adapter.JournalListAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.chatutils.ConcatInfoUtils;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.interfaces.UpdateCallback;
import com.bs.bsims.model.JournalListVO1;
import com.bs.bsims.model.TreeVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSPopupWindowsTitle;
import com.bs.bsims.view.BSPopupWindowsTitle.TreeCallBack;
import com.bs.bsims.view.BSRefreshListView;
import com.bs.bsims.view.BSRefreshListView.OnRefreshListener;
import com.bs.bsims.view.PinnedSectionListView;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JournalListFragmentAll extends BaseFragment implements OnItemClickListener, OnClickListener, UpdateCallback {
    private String mFristid, mLastid;
    private int mState = 0; // 0为首次,1为上拉刷新 ，2为下拉刷新
    private JournalListVO1 mListVO;
    private Activity mActivity;
    private LinearLayout mOneTitle, mTwoTitle, mThreeTitle;
    private TextView mOneTitleTv, mTwoTitleTv, mThreeTitleTv;
    private String mType, mDid, mIsMyFavor;

    // 下拉上拉刷新部分
    private View mFootLayout;
    private TextView mMoreTextView;
    private ProgressBar mProgressBar;

    private PinnedSectionListView mRefreshListView;
    private JournalListAdapter mAdapter;
    private String[] mTypeArray = {
            "全部", "日报", "周报", "月报"
    };
    private BSPopupWindowsTitle mTpyePop, mDepartPop;
    private boolean mCanClickFlag = true;
    private TextView mLoading;
    private LinearLayout mLoadingLayout;
    private int mPopStatus = 1;// 1为日，周，月 2为部门
    private LinearLayout mPinedLayout;
    private int mTranslateY; // 顶部悬浮框绘制的Y轴偏移量
    private TextView mTimeTv;
    private LinearLayout no_content_layout;
    private TextView no_content_layout_content;

    public static JournalListFragmentAll newInstance() {
        JournalListFragmentAll fragmentAll = new JournalListFragmentAll();
        return fragmentAll;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_journal_list_all, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        bindViewsListener();

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
        no_content_layout.setVisibility(View.GONE);
        no_content_layout_content.setVisibility(View.GONE);
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
        // super.isRequestFinish();
        mAdapter.notifyDataSetChanged();
        mRefreshListView.onRefreshComplete();
        footViewIsVisibility();

        if (mListVO == null) {
            mRefreshListView.setVisibility(View.GONE);
            no_content_layout.setVisibility(View.VISIBLE);
            no_content_layout_content.setVisibility(View.VISIBLE);
            CommonUtils.setNonetIcon(mActivity, no_content_layout_content, this);
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
        switch (v.getId()) {
            case R.id.title01:
                mTpyePop.showPopupWindow(v);
                break;
            case R.id.title02:
                if (mDepartPop == null && BSApplication.getInstance().getResultVO() != null) {
                    ArrayList<TreeVO> listDepart = CommonUtils.getTreeVOList();
                    mDepartPop = new BSPopupWindowsTitle(mActivity, listDepart, callback);
                }

                if (mDepartPop != null) {
                    mDepartPop.showPopupWindow(v);
                }
                break;

            case R.id.title03:
                Intent intent = new Intent();
                intent.putExtra("defferent_goin", "1");
                intent.setClass(mActivity, JournalSerachActivity.class);
                mActivity.startActivity(intent);
                break;

            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parentView, View arg1, int position, long arg3) {
        if (mAdapter.mList.size() > 0 && mAdapter.getItemViewType((int) arg3) != -10) {
            JournalListVO1 vo = mAdapter.mList.get(mAdapter.mPinnedList.get((int) arg3));
            Intent intent = new Intent();
            intent.putExtra("type", mType);
            intent.putExtra("did", mDid);
            intent.putExtra("logid", vo.getLogid());
            intent.putExtra("listvo", (Serializable) mAdapter.mList);
            intent.setClass(mActivity, JournalPublishDetailActivity.class);
            this.startActivityForResult(intent, 2);
        }
    }

    public void initView(View view) {
        // 3个title初始化
        mOneTitle = (LinearLayout) view.findViewById(R.id.title01);
        mTwoTitle = (LinearLayout) view.findViewById(R.id.title02);
        mThreeTitle = (LinearLayout) view.findViewById(R.id.title03);
        mOneTitleTv = (TextView) view.findViewById(R.id.title_name_01);
        mTwoTitleTv = (TextView) view.findViewById(R.id.title_name_02);
        mThreeTitleTv = (TextView) view.findViewById(R.id.title_name_03);
        mOneTitleTv.setText("全部类型");
        mTwoTitleTv.setText("全部部门");
        mThreeTitleTv.setText("搜索");
        no_content_layout = (LinearLayout) view.findViewById(R.id.no_content_layout);
        no_content_layout_content = (TextView) view.findViewById(R.id.no_content_layout_content);
        mRefreshListView = (PinnedSectionListView) view.findViewById(R.id.refresh_listview);
        mAdapter = new JournalListAdapter(mActivity);
        mRefreshListView.setAdapter(mAdapter);
        mLoading = (TextView) view.findViewById(R.id.loading);
        mLoadingLayout = (LinearLayout) view.findViewById(R.id.loading_layouta);
        mPinedLayout = (LinearLayout) view.findViewById(R.id.pinned_layout);
        mTimeTv = (TextView) view.findViewById(R.id.time_tv);
        initFoot();
        initPopData();
        registBroadcast();
    }

    public void initPopData() {
        ArrayList<TreeVO> listType = CommonUtils.getOneLeveTreeVoZero(mTypeArray);
        mTpyePop = new BSPopupWindowsTitle(mActivity, listType, callback, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (BSApplication.getInstance().getResultVO() != null && null != BSApplication.getInstance().getResultVO().getDepartments()) {
            ArrayList<TreeVO> listDepart = CommonUtils.getTreeVOList();
            mDepartPop = new BSPopupWindowsTitle(mActivity, listDepart, callback);
        }
        else {
            ConcatInfoUtils.getInstance().getDepartmentData(mActivity);
        }

    }

    // 菜单点击回调函数
    TreeCallBack callback = new TreeCallBack() {
        @Override
        public void callback(TreeVO vo) {

            if (vo.getDepartmentid() != null) {
                mDid = vo.getDepartmentid();
                // -1代表全部部门
                if ("-1".equals(mDid)) {
                    mDid = "";
                }
                match(4, mDid);
                mTwoTitleTv.setText(vo.getDname());
            } else {
                match(3, vo.getSearchId());
                mOneTitleTv.setText(vo.getName());
            }
        }
    };

    public void bindViewsListener() {
        mOneTitle.setOnClickListener(this);
        mTwoTitle.setOnClickListener(this);
        mThreeTitle.setOnClickListener(this);
        bindRefreshListener();
        mRefreshListView.setOnItemClickListener(this);
        // mRefreshListView.setOnScrollListener(this);
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

            map.put("type", mType);
            map.put("did", mDid);
            map.put("ismyfavor", mIsMyFavor);
            map.put("firstid", mFristid);
            map.put("lastid", mLastid);
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

    @Override
    public String getFragmentName() {
        return null;
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
        mFootLayout = LayoutInflater.from(mActivity).inflate(R.layout.listview_bottom_more, null);
        mMoreTextView = (TextView) mFootLayout.findViewById(R.id.txt_loading);
        mMoreTextView.setText("更多");
        mProgressBar = (ProgressBar) mFootLayout.findViewById(R.id.progressBar);
        mFootLayout.setVisibility(View.GONE);
        mRefreshListView.addFooterView(mFootLayout);
    }

    public void match(int key, String value) {
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
                mType = value;
                break;
            case 4:
                mDid = value;
                break;
            default:
                break;
        }
        mRefreshListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
        new ThreadUtil(mActivity, this).start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (intent == null)
            return;

        switch (requestCode) {
            case 2:
                List<JournalListVO1> list = (List<JournalListVO1>) intent.getSerializableExtra("listvo");
                mAdapter.updateData(list);
                break;
            case 3:
                if (mAdapter.mList.size() > 0) {
                    match(1, mAdapter.mList.get(0).getLogid());
                } else {
                    mFristid = "";
                    match(1, "");
                }
                break;

            default:
                break;
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
        mActivity.registerReceiver(msgBroadcast, filter);
    }

    private void unRegistExitReceiver() {
        mActivity.unregisterReceiver(msgBroadcast);
    }

    private BroadcastReceiver msgBroadcast = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constant.UPLOAD_HEAD_ICON_MSG.equals(intent.getAction())) {
                new ThreadUtil(mActivity, JournalListFragmentAll.this).start();
            }
        }
    };

}
