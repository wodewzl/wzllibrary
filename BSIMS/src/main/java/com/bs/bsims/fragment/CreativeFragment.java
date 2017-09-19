
package com.bs.bsims.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.interfaces.UpdateCallback;
import com.bs.bsims.model.IdeaResultVO;
import com.bs.bsims.model.ResultVO;
import com.bs.bsims.model.UserInfoResultVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.utils.UrlUtil;
import com.bs.bsims.view.BSCreativeIdea;
import com.bs.bsims.view.BSCreativeIdeaSuggest;
import com.bs.bsims.view.BSCreativeSuggest;
import com.bs.bsims.view.BSLoadingView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class CreativeFragment extends BaseFragment implements UpdateCallback, OnClickListener {
    public static final String CONTACTS = "1"; // 1为通讯录
    public static final String ATTENDANCE = "2";// 2为考勤
    private static final String TAG = "CollectFragment";
    private TextView mTitleTv;
    private Activity mActivity;
    private ViewPager mViewPager;
    private TabPagerAdapter mPagerAdapter;
    private BSCreativeIdea mTopIndicator;
    private BSCreativeSuggest mSuggest;
    private BSCreativeIdeaSuggest mIdeaSuggest;
    private UserInfoResultVO mResultInfoVO;
    private ResultVO mResultVO;
    private TextView mTextView;
    private TextView mBackImage;
    private BSLoadingView mLoadingView;

    private IdeaResultVO mAllResultVO, mMyResultVO, mIdeaAndSuggestVO;

    private String mType = "1";
    private String mIsboos = "0";
    private String mIsall = "0";

    private int[] mDrawableIds;
    private CharSequence[] mLabels;

    private TextView mLoading;
    private LinearLayout mLoadingLayout;
    private String mUnread;
    private String mToDo;

    // boss第一次进来看到的是全部，点击上面的按钮后，切换创意或建议
    private boolean mFlag = true;

    CreativeIdeaAllFragment contacts;
    CreativeIdeaMyFragment departmnet;
    
    public CreativeFragment(){
        
    }

    public CreativeFragment(String type, String isboss, String isall) {
        this.mType = type;
        this.mIsboos = isboss;
        this.mIsall = isall;
    }

    public CreativeFragment(String type, String isboss, String isall, String unread, String todo) {
        this.mType = type;
        this.mIsboos = isboss;
        this.mIsall = isall;
        this.mUnread = unread;
        this.mToDo = todo;
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
        View view;
        if ("1".equals(mType)) {
            view = inflater.inflate(R.layout.fragment_creative_idea, container,
                    false);
        } else {
            view = inflater.inflate(R.layout.fragment_creative_suggest, container,
                    false);
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        bindViewsListeners();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDisplay();
    }

    private void initViews(View view) {
        mTitleTv = (TextView) view.findViewById(R.id.txt_comm_head_activityName);
        mBackImage = (TextView) view.findViewById(R.id.img_head_back);

        mTextView = (TextView) view.findViewById(R.id.loading);
        mTextView.setVisibility(View.VISIBLE);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mPagerAdapter = new TabPagerAdapter(getFragmentManager());
        if ("1".equals(mType)) {
            mTopIndicator = (BSCreativeIdea) view.findViewById(R.id.top_indicator);
            mTopIndicator.setOnTopIndicatorListener(new com.bs.bsims.view.BSCreativeIdea.OnTopIndicatorListener() {

                @Override
                public void onIndicatorSelected(int index) {
                    mViewPager.setCurrentItem(index);

                }
            });
        } else if ("2".equals(mType)) {
            mSuggest = (BSCreativeSuggest) view.findViewById(R.id.top_indicator);
            mSuggest.setOnTopIndicatorListener(new com.bs.bsims.view.BSCreativeSuggest.OnTopIndicatorListener() {

                @Override
                public void onIndicatorSelected(int index) {
                    mViewPager.setCurrentItem(index);
                }
            });
        }

        mLoading = (TextView) view.findViewById(R.id.loading);
        mLoadingView = (BSLoadingView) view.findViewById(R.id.loading_animation);
        mLoadingLayout = (LinearLayout) view.findViewById(R.id.loading_layout);
    }

    private void initDisplay() {
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.invalidate();
        new ThreadUtil(mActivity, this).start();
    }

    public void bindViewsListeners() {
        mBackImage.setOnClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public String getFragmentName() {
        return TAG;
    }

    private class TabPagerAdapter extends FragmentStatePagerAdapter implements
            ViewPager.OnPageChangeListener {
        private List<Fragment> mList;

        public TabPagerAdapter(FragmentManager fm) {
            super(fm);
            mViewPager.setOnPageChangeListener(this);
            mList = new ArrayList<Fragment>();
        }

        @Override
        public Fragment getItem(int position) {
            return mList.get(position);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int position, float positionOffset,
                int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if ("1".equals(mType)) {
                mTopIndicator.setTabsDisplay(mActivity, position);
            } else if ("2".equals(mType)) {
                mSuggest.setTabsDisplay(mActivity, position);
            }
        }

        public void addFragment() {
            mList.clear();
            contacts = new CreativeIdeaAllFragment(mAllResultVO, "1", mIsboos, mIsall, mUnread, mToDo);
            if (!"1".equals(mType)) {
                departmnet = new CreativeIdeaMyFragment(mMyResultVO, mType, mIsboos, "0", mUnread, mToDo);
            }
            if (contacts != null) {
                mList.add(contacts);
            }

            if (departmnet != null) {
                mList.add(departmnet);
            }

            notifyDataSetChanged();
        }
    }

    public boolean getData() {
        Gson gson = new Gson();
        String allStr = "";
        String myIdeaUrl = "";
        String ideaAndSuggestStr = "";
        try {
            allStr = UrlUtil.getIdeaUrl(Constant.CREATIVE_IDEA, "", "", BSApplication.getInstance().getUserId(), "1", mIsboos, mIsall, mUnread, mToDo);
            String jsonUrlStr = HttpClientUtil.get(allStr, Constant.ENCODING).trim();
            mAllResultVO = gson.fromJson(jsonUrlStr, IdeaResultVO.class);

            // 第二个标签
            if (!"1".equals(mType)) {
                // 从首页进来的
                myIdeaUrl = UrlUtil.getIdeaUrl(Constant.CREATIVE_IDEA, "", "", BSApplication.getInstance().getUserId(), mType, mIsboos, "0", mUnread, mToDo);
                String jsonMyidea = HttpClientUtil.get(myIdeaUrl, Constant.ENCODING).trim();
                mMyResultVO = gson.fromJson(jsonMyidea, IdeaResultVO.class);
            }

            if (Constant.RESULT_CODE.equals(mAllResultVO.getCode())) {
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
    public boolean execute() {
        return getData();
    }

    @Override
    public void executeSuccess() {
        mPagerAdapter.addFragment();
        mTextView.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.GONE);
    }

    @Override
    public void executeFailure() {
        mPagerAdapter.addFragment();
        mTextView.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.VISIBLE);
        mLoadingView.setVisibility(View.GONE);
        mLoading.setTextColor(Color.parseColor("#666666"));
        if (mAllResultVO != null) {
            // mLoading.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.no_content, 0, 0);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, 250);
            mLoading.setLayoutParams(layoutParams);
            CommonUtils.setNonetContent(mActivity, mLoading, "没有相关信息");
        } else {
            mLoading.setText("网络不佳,点击图标重新加载哦~");
            mLoading.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.no_net, 0, 0);
        }

    }

    @Override
    public void onClick(View v) {
        mActivity.finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void registBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.HOME_MSG);
        mActivity.registerReceiver(msgBroadcast, filter);
    }

    private void unRegistReceiver() {
        mActivity.unregisterReceiver(msgBroadcast);
    }

    private BroadcastReceiver msgBroadcast = new BroadcastReceiver() {
        private long mChangeTime = 0;

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constant.HOME_MSG.equals(intent.getAction())) {
                new ThreadUtil(mActivity, CreativeFragment.this).start();
            }
        }
    };

}
