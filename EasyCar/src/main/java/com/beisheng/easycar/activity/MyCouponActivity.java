package com.beisheng.easycar.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;

import com.beisheng.easycar.R;
import com.beisheng.easycar.adapter.MyCouponAdapter;
import com.beisheng.easycar.mode.MyCouponVO;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.wuzhanglong.library.ItemDecoration.DividerDecoration;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.wuzhanglong.library.utils.DividerUtil;
import com.wuzhanglong.library.utils.ThreadUtil;
import com.wuzhanglong.library.view.AutoSwipeRefreshLayout;

import java.util.List;

public class MyCouponActivity extends BaseActivity implements OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener{


    private AutoSwipeRefreshLayout mAutoSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView;
    private MyCouponAdapter mAdapter;
    private int mCurrentPage = 1;
    private boolean isLoadMore = true;
    private MyCouponVO mMyCouponVO;
    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.activity_my_coupon);
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
        mAdapter = new MyCouponAdapter(mRecyclerView);
        LuRecyclerViewAdapter luAdapter = new LuRecyclerViewAdapter(mAdapter);
        mRecyclerView.setAdapter(luAdapter);
        dismissProgressDialog();
        mAutoSwipeRefreshLayout.autoRefresh();
    }

    @Override
    public void bindViewsListener() {
        mRecyclerView.setOnLoadMoreListener(this);
        mAutoSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void getData() {
        HttpClientUtil.show(mThreadUtil);

//        RequestParams params = new RequestParams();
//        if (AppApplication.getInstance().getUserInfoVO() != null)
//            params.put("uin", AppApplication.getInstance().getUserInfoVO().getUin());
//        params.put("page", mCurrentPage);
//        params.put("status", "1");
//        String urlList = Constant.MY_COUPON_URL;
//        HttpClientUtil.get(mActivity, mThreadUtil, urlList, params, MyCouponVO.class);
    }

    @Override
    public void hasData(BaseVO vo) {
        MyCouponVO myCouponVO = (MyCouponVO) vo;
        mMyCouponVO = myCouponVO.getData();
        List<MyCouponVO> list = mMyCouponVO.getList();
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
}
