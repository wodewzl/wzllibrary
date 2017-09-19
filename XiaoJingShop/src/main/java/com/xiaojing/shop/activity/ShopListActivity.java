package com.xiaojing.shop.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.loopj.android.http.RequestParams;
import com.rey.material.widget.TextView;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.mode.EBMessageVO;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.wuzhanglong.library.utils.DividerUtil;
import com.wuzhanglong.library.utils.ThreadUtil;
import com.wuzhanglong.library.view.AutoSwipeRefreshLayout;
import com.xiaojing.shop.R;
import com.xiaojing.shop.adapter.ShopListRadapter;
import com.xiaojing.shop.constant.Constant;
import com.xiaojing.shop.mode.ShopVO;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;

public class ShopListActivity extends BaseActivity implements View.OnClickListener, BGAOnRVItemClickListener, OnLoadMoreListener,
        SwipeRefreshLayout.OnRefreshListener,TextWatcher, android.widget.TextView.OnEditorActionListener {
    private LuRecyclerView mRecyclerView;
    private AutoSwipeRefreshLayout mAutoSwipeRefreshLayout;
    private ShopListRadapter mAdapter;
    private TextView mOpionsTv01, mOpionsTv02, mOpionsTv03;
    private int mPriceType = 0;//0默认1升2降
    private String mKeyword, mGoodsCategoryId, mPriceFrom, mPriceTo, mKey, mOrder;
    private ShopVO mShopVO;
    private int mCurrentPage = 1;
    private boolean isLoadMore = true;
    private EditText mStartPriceTv, mEndPriceTv;
    private android.widget.TextView mOkTv;
    private LinearLayout mPriceLayout;
    private EditText mSearchEt;
    private android.widget.TextView mBackTv;

    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.shop_list_activity);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initView() {
        mBaseHeadLayout.setVisibility(View.GONE);

        EventBus.getDefault().register(this);
        mAutoSwipeRefreshLayout = getViewById(R.id.swipe_refresh_layout);
        mActivity.setSwipeRefreshLayoutColors(mAutoSwipeRefreshLayout);
        mRecyclerView = getViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(DividerUtil.linnerDivider(this, R.dimen.dp_1, R.color.C3));
        mAdapter = new ShopListRadapter(mRecyclerView);
        LuRecyclerViewAdapter adapter = new LuRecyclerViewAdapter(mAdapter);
        mRecyclerView.setAdapter(adapter);
        mOpionsTv01 = getViewById(R.id.option_tv01);
        mOpionsTv02 = getViewById(R.id.option_tv02);
        mOpionsTv03 = getViewById(R.id.option_tv03);

        mStartPriceTv = getViewById(R.id.start_price_tv);
        mEndPriceTv = getViewById(R.id.end_price_tv);
        mStartPriceTv.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.C3_1, R.color.C1));
        mEndPriceTv.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.C3_1, R.color.C1));
        mPriceLayout = getViewById(R.id.price_layout);
        mOkTv = getViewById(R.id.ok_tv);
        mSearchEt = getViewById(R.id.search_et);
        mBackTv = getViewById(R.id.back_tv);
        Intent intent = this.getIntent();
        mGoodsCategoryId = intent.getStringExtra("gc_id");
        mKeyword = intent.getStringExtra("keyword");
        mSearchEt.setText(mKeyword);
    }


    @Override
    public void bindViewsListener() {
        mAdapter.setOnRVItemClickListener(this);
        mOpionsTv01.setOnClickListener(this);
        mOpionsTv02.setOnClickListener(this);
        mOpionsTv03.setOnClickListener(this);
        mRecyclerView.setOnLoadMoreListener(this);
        mAutoSwipeRefreshLayout.setOnRefreshListener(this);
        mPriceLayout.setOnClickListener(this);
        mOkTv.setOnClickListener(this);
        mSearchEt.addTextChangedListener(this);
        mSearchEt.setOnEditorActionListener(this);
        mBackTv.setOnClickListener(this);
    }

    @Override
    public void getData() {
        RequestParams paramsMap = new RequestParams();
        String mUrl = Constant.SHOP_LIST_URL;

        paramsMap.put("keyword", mKeyword);
        paramsMap.put("gc_id", mGoodsCategoryId);
        paramsMap.put("price_from", mPriceFrom);
        paramsMap.put("price_to", mPriceTo);
        paramsMap.put("key", mKey);
        paramsMap.put("order", mOrder);
        paramsMap.put("curpage", mCurrentPage);
        HttpClientUtil.get(mActivity, mThreadUtil, mUrl, paramsMap, ShopVO.class);
    }

    @Override
    public void hasData(BaseVO vo) {

        ShopVO shopVO = (ShopVO) vo;
        mShopVO = shopVO.getDatas();
        List<ShopVO> list = mShopVO.getGoods_list();
        if (vo.getHasmore() != null && "1".equals(vo.getHasmore())) {
            mRecyclerView.setNoMore(false);
        } else {
            mRecyclerView.setNoMore(true);
        }
        if (isLoadMore) {
            mAdapter.updateDataLast(list);
            isLoadMore = false;
            mCurrentPage++;
        } else {
            mCurrentPage++;
            mAdapter.updateData(list);
        }
        mAutoSwipeRefreshLayout.setRefreshing(false);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void noData(BaseVO vo) {

    }

    @Override
    public void noNet() {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.option_tv01:
                setOptionsTextColor(v);
                break;
            case R.id.option_tv02:
                setOptionsTextColor(v);
                break;
            case R.id.option_tv03:
                setOptionsTextColor(v);
                if (mPriceLayout.getVisibility() == View.VISIBLE) {
                    mPriceLayout.setVisibility(View.GONE);
                } else {
                    mPriceLayout.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.ok_tv:
                mPriceLayout.setVisibility(View.GONE);
                mPriceFrom = mStartPriceTv.getText().toString();
                mPriceTo = mEndPriceTv.getText().toString();
                mCurrentPage = 1;
                mAutoSwipeRefreshLayout.autoRefresh();
                break;
            case R.id.back_tv:
                EventBus.getDefault().post(new EBMessageVO("back_webview"));
                this.finish();
                break;

            default:
                break;
        }
    }

    public void setOptionsTextColor(View v) {
        cleanOptions();
        switch (v.getId()) {
            case R.id.option_tv01:
                mOpionsTv01.setTextColor(ContextCompat.getColor(this, R.color.XJColor2));
                mOpionsTv02.setTextColor(ContextCompat.getColor(this, R.color.C4));
                mOpionsTv03.setTextColor(ContextCompat.getColor(this, R.color.C4));
                mPriceType = 0;
                mOpionsTv02.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.shop_list_sort, 0);
                mKey = "1";
                mPriceLayout.setVisibility(View.GONE);
                break;
            case R.id.option_tv02:
                mOpionsTv01.setTextColor(ContextCompat.getColor(this, R.color.C4));
                mOpionsTv02.setTextColor(ContextCompat.getColor(this, R.color.XJColor2));
                mOpionsTv03.setTextColor(ContextCompat.getColor(this, R.color.C4));
                if (mPriceType == 0) {
                    mPriceType = 1;
                    mOpionsTv02.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.shop_list_up_sort, 0);
                    mOrder = "1";
                } else if (mPriceType == 1) {
                    mPriceType = 2;
                    mOpionsTv02.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.shop_list_low_sort, 0);
                    mOrder = "2";
                } else if (mPriceType == 2) {
                    mPriceType = 1;
                    mOpionsTv02.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.shop_list_up_sort, 0);
                    mOrder = "1";
                }
                mKey = "2";
                mPriceLayout.setVisibility(View.GONE);
                break;
            case R.id.option_tv03:
                mOpionsTv01.setTextColor(ContextCompat.getColor(this, R.color.C4));
                mOpionsTv02.setTextColor(ContextCompat.getColor(this, R.color.C4));
                mOpionsTv03.setTextColor(ContextCompat.getColor(this, R.color.XJColor2));
                mPriceType = 0;
                mOpionsTv02.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.shop_list_sort, 0);
                mKey = "";
                break;
            default:
                break;
        }
        if (v.getId() != R.id.option_tv03) {
            mAutoSwipeRefreshLayout.autoRefresh();
        }
    }

   public void cleanOptions(){
       mOrder="";
       mKey="";
       mPriceFrom="";
       mPriceTo="";
       mKeyword="";
   }


    @Override
    public void onRVItemClick(ViewGroup viewGroup, View view, int i) {
        if(mAdapter.getData().size()==0)
            return;
        Bundle bundle = new Bundle();
        ShopVO vo = (ShopVO) mAdapter.getData().get(i);
        bundle.putString("url", vo.getDetail_url());
        open(WebViewActivity.class, bundle, 0);
    }

    @Override
    public void onLoadMore() {
        isLoadMore = true;
        mThreadUtil = new ThreadUtil(mActivity, this);
        mThreadUtil.start();
    }

    @Override
    public void onRefresh() {
        mCurrentPage = 1;
        mThreadUtil = new ThreadUtil(mActivity, this);
        mThreadUtil.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EBMessageVO event) {
        if ("select_shop_cart".equals(event.getMessage())) {
            ShopListActivity.this.finish();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence s, int i, int i1, int i2) {
        if ("".equals(s.toString())) {
            mKeyword = "";
            mCurrentPage = 1;
            mThreadUtil = new ThreadUtil(mActivity, this);
            mThreadUtil.start();
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public boolean onEditorAction(android.widget.TextView textView, int i, KeyEvent keyEvent) {
        mKeyword = textView.getText().toString();
        mAutoSwipeRefreshLayout.autoRefresh();
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EventBus.getDefault().post(new EBMessageVO("back_webview"));
        this.finish();
    }
}
