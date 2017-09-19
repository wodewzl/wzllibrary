package com.beisheng.easycar.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beisheng.easycar.R;
import com.beisheng.easycar.adapter.MyMoneyAdapter;
import com.beisheng.easycar.application.AppApplication;
import com.beisheng.easycar.constant.Constant;
import com.beisheng.easycar.mode.MyMoneyVO;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.loopj.android.http.RequestParams;
import com.wuzhanglong.library.ItemDecoration.DividerDecoration;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.mode.EBMessageVO;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.wuzhanglong.library.utils.DividerUtil;
import com.wuzhanglong.library.utils.ThreadUtil;
import com.wuzhanglong.library.view.AutoSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class MyMoneyActivity extends BaseActivity implements OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    private TextView mWithdrawTv, mRechargeTv, mOverTv;
    private AutoSwipeRefreshLayout mAutoSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView;
    private MyMoneyAdapter mAdapter;
    private LuRecyclerViewAdapter mLuAdapter;
    private MyMoneyVO mMyMoneyVO;
    private String mType;
    private int mCurrentPage = 1;
    private boolean isLoadMore = false;

    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.activity_my_money);
    }

    @Override
    public void initView() {
        mBaseBackTv.setText("");
        mBaseHeadLayout.setBackgroundResource(R.color.Car2);
        mBaseTitleTv.setText("");
        mAutoSwipeRefreshLayout = getViewById(R.id.swipe_refresh_layout);
        setSwipeRefreshLayoutColors(mAutoSwipeRefreshLayout);
        mRecyclerView = getViewById(R.id.recycler_view);
        DividerDecoration divider = DividerUtil.linnerDivider(this, R.dimen.dp_1, R.color.C3);
        mRecyclerView.addItemDecoration(divider);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyMoneyAdapter(mRecyclerView);
        mLuAdapter = new LuRecyclerViewAdapter(mAdapter);
        mLuAdapter.addHeaderView(initHeadView());
        mRecyclerView.setAdapter(mLuAdapter);


    }

    @Override
    public void bindViewsListener() {
        mAutoSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setOnLoadMoreListener(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void getData() {
        RequestParams params = new RequestParams();
        String url = Constant.MY_MONEY_URL;
        if (AppApplication.getInstance().getUserInfoVO() != null)
            params.put("uin", AppApplication.getInstance().getUserInfoVO().getUin());
        params.put("curpage", mCurrentPage);
        HttpClientUtil.get(mActivity, mThreadUtil, url, params, MyMoneyVO.class);

    }

    @Override
    public void hasData(BaseVO vo) {

        List<MyMoneyVO> list = new ArrayList<>();
        MyMoneyVO MyMoneyVO = (MyMoneyVO) vo;
        mMyMoneyVO = MyMoneyVO.getData();
        mOverTv.setText("￥" + mMyMoneyVO.getBalance());
        if (!isLoadMore) {
            MyMoneyVO title = new MyMoneyVO();
            list.add(title);
        }
        if (mMyMoneyVO.getList() != null)
            list.addAll(mMyMoneyVO.getList());

        if (BaseCommonUtils.parseInt(vo.getCount())>BaseCommonUtils.parseInt(vo.getPage())) {
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
        showView();
        mRecyclerView.setNoMore(true);
        mAutoSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void noNet() {

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public View initHeadView() {
        View header = LayoutInflater.from(this).inflate(R.layout.activity_my_money_head, (ViewGroup) findViewById(android.R.id.content), false);
        mRechargeTv = (TextView) header.findViewById(R.id.recharge_tv);
        mWithdrawTv = (TextView) header.findViewById(R.id.withdraw_tv);
        mOverTv = (TextView) header.findViewById(R.id.over_tv);
        mRechargeTv.setBackground(BaseCommonUtils.setBackgroundShap(this, 50, R.color.Car8, R.color.Car2));
        mRechargeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(CardRechargeActivity.class);
            }
        });
        mWithdrawTv.setBackground(BaseCommonUtils.setBackgroundShap(this, 100, R.color.Car8, R.color.Car2));
        mWithdrawTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if ("1".equals(mMyMoneyVO.getPredeposit_info().getHave_card())) {
//                    Intent intent = new Intent();
//                    intent.putExtra("card_info", (PayTypeVO) mMyMoneyVO.getPredeposit_info().getCard_info());
//                    intent.putExtra("over", mMyMoneyVO.getPredeposit_info().getAvailable_predeposit());
//                    intent.setClass(MyOverActivity.this, CardWithdrawActivity.class);
//                    MyOverActivity.this.startActivity(intent);
//                } else {
//                    openActivity(CardBindActivity.class);
//                }
            }
        });
        return header;
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
        if ("withdraw".equals(event.getMessage()) || "recharge".equals(event.getMessage())) {
            mAutoSwipeRefreshLayout.autoRefresh();
        }
    }

}
