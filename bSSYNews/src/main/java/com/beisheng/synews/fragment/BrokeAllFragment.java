
package com.beisheng.synews.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.fragment.BaseFragment;
import com.beisheng.base.http.HttpClientUtil;
import com.beisheng.base.interfaces.UpdateCallback;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.ThreadUtil;
import com.beisheng.base.view.BSAutoSwipeRefreshLayout;
import com.beisheng.synews.activity.KeyWordActivity;
import com.beisheng.synews.activity.NewsPhotoDetailActivity;
import com.beisheng.synews.activity.WebViewActivity;
import com.beisheng.synews.adapter.BrokeAdapter;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.interfaces.LoadMoreListener;
import com.beisheng.synews.mode.BrokeVO;
import com.beisheng.synews.mode.NewsVO;
import com.beisheng.synews.view.BSListViewLoadMore;
import com.google.gson.Gson;
import com.im.zhsy.R;

import java.util.HashMap;

public class BrokeAllFragment extends BaseFragment implements OnClickListener, OnRefreshListener,
        LoadMoreListener, UpdateCallback, OnItemClickListener {
    private String TAG = "HomeNewsPhotoFragment";
    private BaseActivity mActivity;
    protected BSAutoSwipeRefreshLayout mSwipeLayout;
    protected BSListViewLoadMore mListView;
    private BrokeAdapter mAdapter;
    private BrokeVO mBrokeVO;
    private int mState = 0; // 0为首次,1为下拉刷新 ，2为加载更多
    public String mPage = "1";// 用来存储数据的，1为默认第一页，不是只有一页
    private boolean mLodMore = true;// 控制多次加载，即当前加载就不在会加载直到结束
    private ImageView mHeadImg;
    private LinearLayout mRootLayout;
    private TextView mBrokeNoteTv;

    public static HomeNewsFragment newInstance() {
        HomeNewsFragment liveFragment = new HomeNewsFragment();
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
        View view = inflater.inflate(R.layout.broke_fragment, container, false);
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
        // new ThreadUtil(mActivity, this).start();
    }

    private void initViews(View view) {
        mHeadImg = (ImageView) view.findViewById(R.id.head_img);
        mBrokeNoteTv = (TextView) view.findViewById(R.id.broke_note_tv);
        mSwipeLayout = (BSAutoSwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mActivity.setSwipeRefreshLayoutColors(mSwipeLayout);
        mListView = (BSListViewLoadMore) view.findViewById(R.id.list_view);
        mAdapter = new BrokeAdapter(mActivity);
        mListView.setAdapter(mAdapter);
        mSwipeLayout.autoRefresh();
    }

    public void bindViewsListener() {
        mListView.setOnItemClickListener(this);
        mListView.setLoadMoreListener(this);
        mSwipeLayout.setOnRefreshListener(this);
        mHeadImg.setOnClickListener(this);
        mBrokeNoteTv.setOnClickListener(this);
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
            case R.id.search_view:
                mActivity.openActivity(KeyWordActivity.class);
                break;

            case R.id.head_img:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + mBrokeVO.getMobile());
                intent.setData(data);
                startActivity(intent);
                break;
            case R.id.broke_note_tv:
                Intent brokeIntent = new Intent();
                brokeIntent.putExtra("url", Constant.DOMAIN_NAME + Constant.BROKE_NOTE_URL + "/id/64");
                brokeIntent.putExtra("name", "爆料须知");
                brokeIntent.setClass(mActivity, WebViewActivity.class);
                mActivity.startActivity(brokeIntent);
                break;
            default:
                break;
        }
    }

    public boolean getData() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("page", mPage);

            if (mActivity.hasNetWork()) {
                String jsonStr = HttpClientUtil.getRequest(mActivity, Constant.DOMAIN_NAME + Constant.BROKE_LIST, map);
                mBrokeVO = gson.fromJson(jsonStr, BrokeVO.class);
                mActivity.saveJsonCache(Constant.DOMAIN_NAME + Constant.BROKE_LIST, map, jsonStr);
            } else {
                String oldStr = mActivity.getCacheFromDatabase(Constant.DOMAIN_NAME + Constant.BROKE_LIST, map);
                mBrokeVO = gson.fromJson(oldStr, BrokeVO.class);
            }

            if (Constant.RESULT_SUCCESS_CODE.equals(mBrokeVO.getCode())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
    public boolean execute() {
        return getData();
    }

    @Override
    public void executeSuccess() {
        mLodMore = true;
        mBrokeNoteTv.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.VISIBLE);
        mSwipeLayout.setRefreshing(false);
        mActivity.mImageLoader.displayImage(mBrokeVO.getThumb(), mHeadImg, mActivity.mOptions);
        if (BaseCommonUtils.parseInt(mBrokeVO.getTotal()) > BaseCommonUtils.parseInt(mBrokeVO.getPage())) {
            mListView.showFooterView(true);
        } else {
            mListView.showFooterView(false);
        }
        if (1 == mState) {
            mAdapter.mList.size();
            mAdapter.updateDataFrist(mBrokeVO.getList());
        } else if (2 == mState) {
            mAdapter.mList.size();
            mAdapter.updateDataLast(mBrokeVO.getList());
        } else {
            mAdapter.updateData(mBrokeVO.getList());
        }

    }

    @Override
    public void executeFailure() {
        mLodMore = true;
        mListView.showFooterView(false);
        mSwipeLayout.setRefreshing(false);
        if (mBrokeVO != null) {
            mActivity.showCustomToast(mBrokeVO.getRetinfo());
        } else {
            mActivity.showCustomToast("亲，请检查网络哦");
            mListView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
        if (adapterView.getAdapter().getCount() == 0)
            return;
        NewsVO vo = (NewsVO) adapterView.getAdapter().getItem(position);
        if (vo == null)
            return;
        Bundle bundle = new Bundle();
        bundle.putString("id", vo.getContentid());
        mActivity.openActivity(NewsPhotoDetailActivity.class, bundle, 0);

    }

    public String getFragmentName() {
        return TAG;// 不知道该方法有没有用
    }
}
