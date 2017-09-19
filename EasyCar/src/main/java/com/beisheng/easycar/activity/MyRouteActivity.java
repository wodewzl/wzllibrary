package com.beisheng.easycar.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.beisheng.easycar.R;
import com.beisheng.easycar.adapter.MyRouteAdapter;
import com.beisheng.easycar.application.AppApplication;
import com.beisheng.easycar.constant.Constant;
import com.beisheng.easycar.mode.MyRouteVO;
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

import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;

public class MyRouteActivity extends BaseActivity implements OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener,BGAOnRVItemClickListener {
    private AutoSwipeRefreshLayout mAutoSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView;
    private MyRouteAdapter mAdapter;
    private int mCurrentPage = 1;
    private boolean isLoadMore = true;
    private MyRouteVO mMyRouteVO;
    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.activity_my_route);
    }

    @Override
    public void initView() {
        mBaseHeadLayout.setBackgroundResource(R.color.Car2);
        mBaseTitleTv.setText("我的行程");
        mAutoSwipeRefreshLayout = getViewById(R.id.swipe_refresh_layout);
//        setSwipeRefreshLayoutColors(mAutoSwipeRefreshLayout);
        mRecyclerView = getViewById(R.id.recycler_view);
        DividerDecoration divider = DividerUtil.linnerDivider(this, R.dimen.dp_10, R.color.C3);
        mRecyclerView.addItemDecoration(divider);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyRouteAdapter(mRecyclerView);
        LuRecyclerViewAdapter luAdapter = new LuRecyclerViewAdapter(mAdapter);
        mRecyclerView.setAdapter(luAdapter);
        dismissProgressDialog();
        mAutoSwipeRefreshLayout.autoRefresh();
    }

    @Override
    public void bindViewsListener() {
        mRecyclerView.setOnLoadMoreListener(this);
        mAutoSwipeRefreshLayout.setOnRefreshListener(this);
        mAdapter.setOnRVItemClickListener(this);
    }

    @Override
    public void getData() {
//        HttpClientUtil.show(mThreadUtil);

        RequestParams params = new RequestParams();
        if (AppApplication.getInstance().getUserInfoVO() != null)
            params.put("uin", AppApplication.getInstance().getUserInfoVO().getUin());
        params.put("page", mCurrentPage);
        String urlList = Constant.MY_ROUTE_URL;
        HttpClientUtil.get(mActivity, mThreadUtil, urlList, params, MyRouteVO.class);
    }

    @Override
    public void hasData(BaseVO vo) {
        MyRouteVO myRouteVO = (MyRouteVO) vo;
        mMyRouteVO = myRouteVO.getData();
        List<MyRouteVO> list = mMyRouteVO.getList();
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

    }

    @Override
    public void noNet() {

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
    public void onRVItemClick(ViewGroup viewGroup, View view, int i) {
        if (mAdapter.getData().size() == 0)
            return;
        MyRouteVO vo = (MyRouteVO) mAdapter.getData().get(i);
        if("5".equals(vo.getStatus())){
            Bundle bundle= new Bundle();
            bundle.putString("orderid",vo.getOrderid());
            open(BillingCarActivity.class,bundle,0);
        }else{
            EBMessageVO ebMessageVO=new EBMessageVO("user_car");
            String[] params ={vo.getOrderid()};
            ebMessageVO.setParams(params);
            EventBus.getDefault().post(ebMessageVO);
        }

        this.finish();
    }
}
