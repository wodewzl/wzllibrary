
package com.beisheng.synews.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.http.HttpClientUtil;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.ThreadUtil;
import com.beisheng.base.view.BSAutoSwipeRefreshLayout;
import com.beisheng.base.view.BSIndexEditText;
import com.beisheng.base.view.BSReboundScrollView;
import com.beisheng.synews.adapter.KeyWordAdapter;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.interfaces.LoadMoreListener;
import com.beisheng.synews.mode.KeyWordVO;
import com.beisheng.synews.view.BSListViewLoadMore;
import com.google.gson.Gson;
import com.im.zhsy.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KeyWordActivity extends BaseActivity implements OnRefreshListener, LoadMoreListener {
    private KeyWordVO mKeyWordVO;
    private LinearLayout mKeywordLayout;
    private BSReboundScrollView mKeywordView;
    public String mPage = "1";// 用来存储数据的，1为默认第一页，不是只有一页
    private TextView mLastOnclickTv;
    private String mKeyword;
    private BSListViewLoadMore mListView;
    protected BSAutoSwipeRefreshLayout mSwipeLayout;
    private KeyWordAdapter mAdapter;
    private int mState = 0; // 0为首次,1为下拉刷新 ，2为加载更多
    private BSIndexEditText mBSBsIndexEditText;
    private boolean mLodMore = true;// 控制多次加载，即当前加载就不在会加载直到结束

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.key_word_activity, mBaseContentLayout);
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    @Override
    public void initView() {
        mBaseTitleTv.setText("搜索");
        mBSBsIndexEditText = (BSIndexEditText) findViewById(R.id.edit_single_search);
        mKeywordView = (BSReboundScrollView) findViewById(R.id.keyword_view);
        mKeywordLayout = (LinearLayout) findViewById(R.id.keyword_layout);
        mSwipeLayout = (BSAutoSwipeRefreshLayout) findViewById(R.id.swipe_container);
        setSwipeRefreshLayoutColors(mSwipeLayout);
        mListView = (BSListViewLoadMore) findViewById(R.id.list_view);
        mAdapter = new KeyWordAdapter(this);
        mListView.setAdapter(mAdapter);
        initData();
    }

    public void initData() {
        mKeyword = this.getIntent().getStringExtra("keyword");
        mAdapter.setKeyword(mKeyword);
    }

    @Override
    public void bindViewsListener() {
        mSwipeLayout.setOnRefreshListener(this);
        mListView.setLoadMoreListener(this);

        mBSBsIndexEditText.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) mBSBsIndexEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(KeyWordActivity.this
                            .getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    mKeyword = mBSBsIndexEditText.getText().toString();
                    mAdapter.setKeyword(mKeyword);
                    mState = 0;
                    showProgressDialog();
                    new ThreadUtil(KeyWordActivity.this, KeyWordActivity.this).start();
                    return true;
                }
                return false;
            }
        });

        mBSBsIndexEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ("".equals(s.toString())) {
                    mSwipeLayout.setVisibility(View.GONE);
                    mKeywordView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    public boolean getData() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("keyword", mKeyword);
            map.put("page", mPage);
            String url = "";
            if (mKeyword == null) {
                url = Constant.KEY_WORD_ULR;
            } else {
                url = Constant.KEY_WORD_RESULT_ULR;
            }

            if (hasNetWork()) {
                String jsonStr = HttpClientUtil.getRequest(this, Constant.DOMAIN_NAME + url, map);
                mKeyWordVO = gson.fromJson(jsonStr, KeyWordVO.class);
                saveJsonCache(url, map, jsonStr);

            } else {
                String oldStr = getCacheFromDatabase(url, map);
                mKeyWordVO = gson.fromJson(oldStr, KeyWordVO.class);
            }

            if (Constant.RESULT_SUCCESS_CODE.equals(mKeyWordVO.getCode())) {
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
    public void executeSuccess() {
        super.executeSuccess();
        mLodMore = true;
        if (mKeyWordVO.getKlist() != null) {
            List<KeyWordVO> list = new ArrayList<KeyWordVO>();
            for (int i = 0; i < mKeyWordVO.getKlist().length; i++) {
                KeyWordVO vo = new KeyWordVO();
                vo.setName(mKeyWordVO.getKlist()[i]);
                list.add(vo);
            }
            initKeyWordView(list);
            mSwipeLayout.setVisibility(View.GONE);
            mKeywordView.setVisibility(View.VISIBLE);
        } else {
            mSwipeLayout.setRefreshing(false);
            if (BaseCommonUtils.parseInt(mKeyWordVO.getTotal()) > BaseCommonUtils.parseInt(mKeyWordVO.getPage())) {
                mListView.showFooterView(true);
            } else {
                mListView.showFooterView(false);
            }

            if (1 == mState) {
                mAdapter.mList.size();
                mAdapter.updateDataFrist(mKeyWordVO.getList());
            } else if (2 == mState) {
                mAdapter.mList.size();
                mAdapter.updateDataLast(mKeyWordVO.getList());
            } else {
                mAdapter.updateData(mKeyWordVO.getList());
            }
            mSwipeLayout.setVisibility(View.VISIBLE);
            mKeywordView.setVisibility(View.GONE);
        }

    }

    @Override
    public void executeFailure() {
        mLodMore = true;
        mSwipeLayout.setRefreshing(false);
        mListView.showFooterView(false);
        if (mKeyWordVO != null) {
            showCustomToast(mKeyWordVO.getRetinfo());
            dismissProgressDialog();
        } else {
            super.executeFailure();
            showCustomToast("亲，请检查网络哦");
        }

    }

    @SuppressLint("NewApi")
    public void initKeyWordView(List<KeyWordVO> localList) {
        List<KeyWordVO> list = new ArrayList<KeyWordVO>();
        list.addAll(localList);
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        LinearLayout rowLayout = new LinearLayout(this);
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);
        float screenWidth = BaseCommonUtils.getScreenWidth(this) - BaseCommonUtils.dip2px(this, 15);
        float height = BaseCommonUtils.getScreenHigh(this);
        float currentWidth = 0;
        float everyWidth = 0;
        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < localList.size(); i++) {
            textViewParams.setMargins(0, 0, BaseCommonUtils.dip2px(this, 15), BaseCommonUtils.dip2px(this, 15));
            final TextView childTv = new TextView(this);
            childTv.setPadding(BaseCommonUtils.dip2px(this, 15), BaseCommonUtils.dip2px(this, 7), BaseCommonUtils.dip2px(this, 15), BaseCommonUtils.dip2px(this, 7));
            childTv.setLayoutParams(textViewParams);
            childTv.setText(localList.get(i).getName());
            childTv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    childTv.setBackground(BaseCommonUtils.setBackgroundShap(KeyWordActivity.this, 20, "#B10304", "#B10304"));
                    childTv.setTextColor(Color.parseColor("#ffffff"));
                    if (mLastOnclickTv != null) {
                        mLastOnclickTv.setBackground(BaseCommonUtils.setBackgroundShap(KeyWordActivity.this, 20, "#DDDDDD", "#F8F8F8"));
                        mLastOnclickTv.setTextColor(Color.parseColor("#818181"));
                    }
                    mLastOnclickTv = childTv;
                    mKeyword = childTv.getText().toString();
                    mAdapter.setKeyword(childTv.getText().toString());
                    showProgressDialog();
                    new ThreadUtil(KeyWordActivity.this, KeyWordActivity.this).start();
                }
            });
            childTv.setBackground(BaseCommonUtils.setBackgroundShap(this, 20, "#DDDDDD", "#F8F8F8"));
            childTv.setTextColor(Color.parseColor("#818181"));
            Paint paint = childTv.getPaint();
            float textViewWidth = BaseCommonUtils.getViewWidth(childTv);
            everyWidth = textViewWidth + BaseCommonUtils.dip2px(this, 15);
            if (screenWidth - currentWidth < everyWidth) {
                mKeywordLayout.addView(rowLayout);
                initKeyWordView(list);
                break;
            } else {
                list.remove(localList.get(i));
            }
            currentWidth = everyWidth + currentWidth;
            rowLayout.addView(childTv);
            if (localList.size() - 1 == i) {
                mKeywordLayout.addView(rowLayout);
                break;
            }
        }
    }

    @Override
    public void onRefresh() {
        if (mLodMore) {
            mLodMore = false;
            mState = 1;
            mPage = "1";
            new ThreadUtil(this, this).start();
        }

    }

    @Override
    public void loadMore() {
        mState = 2;
        mPage = (BaseCommonUtils.parseInt(mPage) + 1) + "";
        new ThreadUtil(this, this).start();
    }
}
