
package com.beisheng.synews.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.fragment.BaseFragment;
import com.beisheng.base.http.HttpClientUtil;
import com.beisheng.base.interfaces.UpdateCallback;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.ThreadUtil;
import com.beisheng.base.view.BSAutoSwipeRefreshLayout;
import com.beisheng.synews.activity.LiveAddThemeActivity;
import com.beisheng.synews.activity.LiveDetailActivity;
import com.beisheng.synews.adapter.LiveFragmentAdapter;
import com.beisheng.synews.application.AppApplication;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.interfaces.LoadMoreListener;
import com.beisheng.synews.mode.LiveVO;
import com.beisheng.synews.view.BSListViewLoadMore;
import com.google.gson.Gson;
import com.im.zhsy.R;

import java.util.HashMap;

public class LiveFragment extends BaseFragment implements OnClickListener, UpdateCallback, OnRefreshListener, LoadMoreListener, OnItemClickListener {
    private String TAG = "LiveFragment";
    private BaseActivity mActivity;
    private LiveVO mLiveVO;
    private LiveFragmentAdapter mAdapter;
    protected BSAutoSwipeRefreshLayout mSwipeLayout;
    protected BSListViewLoadMore mListView;
    private String mPage = "1";
    private int mState = 0; // 0为首次,1为下拉刷新 ，2为加载更多
    private boolean mLodMore = true;// 控制多次加载，即当前加载就不在会加载直到结束

    public static LiveFragment newInstance() {
        LiveFragment liveFragment = new LiveFragment();
        return liveFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = (BaseActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.live_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        bindViewsListener();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new ThreadUtil(mActivity, this).start();
    }

    private void initViews(View view) {
        // if (AppApplication.getInstance().getUserInfoVO() != null &&
        // "1".equals(AppApplication.getInstance().getUserInfoVO().getReporter())) {
        // mActivity.mBaseOkTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.live_add,
        // 0);
        // mActivity.mBaseOkTv.setVisibility(View.VISIBLE);
        // mActivity.mBaseOkTv.setText("");
        // } else {
        // mActivity.mBaseOkTv.setVisibility(View.GONE);
        // }
        // mActivity.mBaseHeadLayout.setVisibility(View.VISIBLE);
        // mActivity.mBaseTitleTv.setText("现场直播");
        mSwipeLayout = (BSAutoSwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mActivity.setSwipeRefreshLayoutColors(mSwipeLayout);
        mListView = (BSListViewLoadMore) view.findViewById(R.id.list_view);
        mAdapter = new LiveFragmentAdapter(mActivity);
        // AnimationAdapter animationAdapter = new CardsAnimationAdapter(mAdapter);
        // animationAdapter.setAbsListView(mListView);
        mListView.setAdapter(mAdapter);
        mSwipeLayout.autoRefresh();
    }

    public void bindViewsListener() {
        mSwipeLayout.setOnRefreshListener(this);
        mListView.setOnItemClickListener(this);
        mListView.setLoadMoreListener(this);
        mActivity.mBaseOkTv.setOnClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.base_ok_tv:
                Bundle bundle = new Bundle();
                bundle.putString("title", "直播现场");
                mActivity.openActivity(LiveAddThemeActivity.class, bundle, 0);
                break;
        }
    }

    public boolean getData() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("page", mPage);
            if (mActivity.hasNetWork()) {
                String jsonStr = HttpClientUtil.getRequest(mActivity, Constant.DOMAIN_NAME + Constant.LIVE_FRAGMENT_URL, map);
                mLiveVO = gson.fromJson(jsonStr, LiveVO.class);
                mActivity.saveJsonCache(Constant.LIVE_FRAGMENT_URL, map, jsonStr);
            } else {
                String oldStr = mActivity.getCacheFromDatabase(Constant.LIVE_FRAGMENT_URL, map);
                mLiveVO = gson.fromJson(oldStr, LiveVO.class);
            }
            if (Constant.RESULT_SUCCESS_CODE.equals(mLiveVO.getCode())) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean execute() {
        return getData();
    }

    @Override
    public void executeSuccess() {
        mLodMore = true;
        mSwipeLayout.setRefreshing(false);
        mListView.setVisibility(View.VISIBLE);
        if (BaseCommonUtils.parseInt(mLiveVO.getTotal()) > BaseCommonUtils.parseInt(mLiveVO.getPage())) {
            mListView.showFooterView(true);
        } else {
            mListView.showFooterView(false);
        }
        if (1 == mState) {
            mAdapter.mList.size();
            mAdapter.updateDataFrist(mLiveVO.getList());
        } else if (2 == mState) {
            mAdapter.mList.size();
            mAdapter.updateDataLast(mLiveVO.getList());
        } else {
            mAdapter.updateData(mLiveVO.getList());
        }
    }

    @Override
    public void executeFailure() {
        mLodMore = true;
        mSwipeLayout.setRefreshing(false);
        mListView.showFooterView(false);
        mListView.setVisibility(View.VISIBLE);
        if (mLiveVO != null)
            mActivity.showCustomToast(mLiveVO.getRetinfo());
        else
            mActivity.showCustomToast("亲，请检查网络哦");
    }

    @Override
    public void onRefresh() {
        mState = 1;
        mPage = "1";
        new ThreadUtil(mActivity, this).start();
    }

    @Override
    public void loadMore() {
        if (mLodMore) {
            mLodMore = false;
            mState = 2;
            mPage = (BaseCommonUtils.parseInt(mPage) + 1) + "";
            new ThreadUtil(mActivity, this).start();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
        if (adapterView.getAdapter().getCount() == 0)
            return;
        LiveVO vo = (LiveVO) adapterView.getAdapter().getItem(position);
        if (vo == null)
            return;
        Bundle bundle = new Bundle();
        bundle.putString("id", vo.getId());
        mActivity.openActivity(LiveDetailActivity.class, bundle, 0);
    }

    public String getFragmentName() {
        return TAG;// 不知道该方法有没有用
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (AppApplication.getInstance().getUserInfoVO() != null &&
                    "1".equals(AppApplication.getInstance().getUserInfoVO().getReporter())) {
                mActivity.mBaseOkTv.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                        R.drawable.live_add,
                        0);
                mActivity.mBaseOkTv.setVisibility(View.VISIBLE);
            } else {
                mActivity.mBaseOkTv.setVisibility(View.GONE);
            }
            mActivity.mBaseOkTv.setText("");
            mActivity.mBaseOkTv.setOnClickListener(this);
            mActivity.mBaseHeadLayout.setVisibility(View.VISIBLE);
        }
    }
}
