package com.beisheng.easycar.activity;

import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;

import com.beisheng.easycar.R;
import com.beisheng.easycar.adapter.NearbyCarAdapter;
import com.beisheng.easycar.application.AppApplication;
import com.beisheng.easycar.constant.Constant;
import com.beisheng.easycar.mode.CarVO;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.loopj.android.http.RequestParams;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.wuzhanglong.library.utils.DividerUtil;
import com.wuzhanglong.library.utils.SharePreferenceUtil;
import com.wuzhanglong.library.utils.ThreadUtil;
import com.wuzhanglong.library.view.AutoSwipeRefreshLayout;

import java.util.List;

public class NearbyCarActivity extends BaseActivity implements OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    private CarVO mCarVO;
    private LuRecyclerView mRecyclerView;
    private AutoSwipeRefreshLayout mAutoSwipeRefreshLayout;
    private NearbyCarAdapter mAdapter;
    private int mCurrentPage = 1;
    private boolean isLoadMore = true;


    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.activity_nearby_car);
    }

    @Override
    public void initView() {
        mBaseTitleTv.setText("附近车辆");
        mBaseHeadLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.Car2));
        mAutoSwipeRefreshLayout = getViewById(R.id.swipe_refresh_layout);
//        mActivity.setSwipeRefreshLayoutColors(mAutoSwipeRefreshLayout);
        mRecyclerView = getViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(DividerUtil.linnerDivider(this, R.dimen.dp_1, R.color.C3));
        mAdapter = new NearbyCarAdapter(mRecyclerView);
        LuRecyclerViewAdapter adapter = new LuRecyclerViewAdapter(mAdapter);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void bindViewsListener() {
        mRecyclerView.setOnLoadMoreListener(this);
        mAutoSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void getData() {
        RequestParams params = new RequestParams();
        if (AppApplication.getInstance().getUserInfoVO() != null)
            params.put("uin", AppApplication.getInstance().getUserInfoVO().getUin());
        String cityCode=SharePreferenceUtil.getSharedpreferenceValue(this, "address", "cityCode");
        params.put("citycode", cityCode);
        String lat = SharePreferenceUtil.getSharedpreferenceValue(this, "address", "lat");
        params.put("lat", lat);
        String lo = SharePreferenceUtil.getSharedpreferenceValue(this, "address", "lo");
        params.put("lng", lo);
        params.put("page", mCurrentPage);

       String  district =SharePreferenceUtil.getSharedpreferenceValue(this, "address", "district");
        params.put("district", district);
        params.put("nearest", "1");
        params.put("battery", "2");
        String urlList = Constant.USER_CAR_LIST_URL;
        HttpClientUtil.get(mActivity, mThreadUtil, urlList, params, CarVO.class);
    }

    @Override
    public void hasData(BaseVO vo) {
        CarVO carVO = (CarVO) vo;
        mCarVO = carVO.getData();
        List<CarVO> list = mCarVO.getList();
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
}
