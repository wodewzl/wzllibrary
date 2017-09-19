package com.xiaojing.shop.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.loopj.android.http.RequestParams;
import com.rey.material.widget.TextView;
import com.wuzhanglong.library.ItemDecoration.DividerDecoration;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.mode.EBMessageVO;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.wuzhanglong.library.utils.DividerUtil;
import com.wuzhanglong.library.utils.ThreadUtil;
import com.wuzhanglong.library.view.AutoSwipeRefreshLayout;
import com.xiaojing.shop.R;
import com.xiaojing.shop.adapter.MyOverAdapter;
import com.xiaojing.shop.application.AppApplication;
import com.xiaojing.shop.constant.Constant;
import com.xiaojing.shop.mode.MoneyVO;
import com.xiaojing.shop.mode.PayTypeVO;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class MyOverActivity extends BaseActivity implements MyOverAdapter.onTypeClickListener, OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    private TextView mWithdrawTv, mRechargeTv, mOverTv;
    private AutoSwipeRefreshLayout mAutoSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView;
    private MyOverAdapter mAdapter;
    private LuRecyclerViewAdapter mLuAdapter;
    private MoneyVO mMoneyVO;
    private String mType;
    private int mCurrentPage = 1;
    private boolean isLoadMore = false;

    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.my_over_activity);
    }

    @Override
    public void initView() {
        mBaseTitleTv.setText("我的余额");
        mAutoSwipeRefreshLayout = getViewById(R.id.swipe_refresh_layout);
        setSwipeRefreshLayoutColors(mAutoSwipeRefreshLayout);
        mRecyclerView = getViewById(R.id.recycler_view);
        DividerDecoration divider = DividerUtil.linnerDivider(this, R.dimen.dp_1, R.color.C3);
        mRecyclerView.addItemDecoration(divider);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyOverAdapter(mRecyclerView);
        mLuAdapter = new LuRecyclerViewAdapter(mAdapter);
        mLuAdapter.addHeaderView(initHeadView());
        mRecyclerView.setAdapter(mLuAdapter);



    }

    @Override
    public void bindViewsListener() {
        mAdapter.setOnTypeClickListener(this);
        mAutoSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setOnLoadMoreListener(this);

        EventBus.getDefault().register(this);
    }

    @Override
    public void getData() {
        RequestParams params = new RequestParams();
        String url = Constant.MY_OVER_URL;
        if (AppApplication.getInstance().getUserInfoVO() != null)
            params.put("key", AppApplication.getInstance().getUserInfoVO().getKey());
        params.put("type", mType);
        params.put("curpage", mCurrentPage);
        HttpClientUtil.get(mActivity, mThreadUtil, url, params, MoneyVO.class);

    }

    @Override
    public void hasData(BaseVO vo) {

        List<MoneyVO> list = new ArrayList<>();
        MoneyVO moneyVO = (MoneyVO) vo;
        mMoneyVO = moneyVO.getDatas();
        mOverTv.setText("￥" + mMoneyVO.getPredeposit_info().getAvailable_predeposit());
        if (!isLoadMore&&mMoneyVO.getList().size()>0) {
            MoneyVO title = new MoneyVO();
            list.add(title);
        }
        list.addAll(mMoneyVO.getList());

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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public View initHeadView() {
        View header = LayoutInflater.from(this).inflate(R.layout.my_over_head, (ViewGroup) findViewById(android.R.id.content), false);
        mRechargeTv = (TextView) header.findViewById(R.id.recharge_tv);
        mWithdrawTv = (TextView) header.findViewById(R.id.withdraw_tv);
        mOverTv = (TextView) header.findViewById(R.id.over_tv);
        mRechargeTv.setBackground(BaseCommonUtils.setBackgroundShap(this, 50, R.color.XJColor6, R.color.C1));
        mRechargeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(CardRechargeActivity.class);

            }
        });
        mWithdrawTv.setBackground(BaseCommonUtils.setBackgroundShap(this, 100, R.color.XJColor5, R.color.C1));
        mWithdrawTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("1".equals(mMoneyVO.getPredeposit_info().getHave_card())) {
                    Intent intent = new Intent();
                    intent.putExtra("card_info", (PayTypeVO) mMoneyVO.getPredeposit_info().getCard_info());
                    intent.putExtra("over", mMoneyVO.getPredeposit_info().getAvailable_predeposit());
                    intent.setClass(MyOverActivity.this, CardWithdrawActivity.class);
                    MyOverActivity.this.startActivity(intent);
                } else {
                    openActivity(CardBindActivity.class);
                }
            }
        });
        return header;
    }

    @Override
    public void typeClick(String type) {
        mType = type;
        mCurrentPage = 1;
        mAutoSwipeRefreshLayout.setEnabled(true);
        mAutoSwipeRefreshLayout.autoRefresh();
        mThreadUtil = new ThreadUtil(this, this);
        mThreadUtil.start();
    }

    @Override
    public void onLoadMore() {
        isLoadMore = true;
        mThreadUtil = new ThreadUtil(this, this);
        mThreadUtil.start();
    }

    @Override
    public void onRefresh() {
        mCurrentPage = 1;
        mThreadUtil = new ThreadUtil(this, this);
        mThreadUtil.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EBMessageVO event) {
        if ("withdraw".equals(event.getMessage()) ||"recharge".equals(event.getMessage())) {
            mAutoSwipeRefreshLayout.autoRefresh();
        }
    }
}
