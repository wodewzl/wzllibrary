package com.beisheng.easycar.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.beisheng.easycar.R;
import com.beisheng.easycar.adapter.HelpAdapter;
import com.beisheng.easycar.constant.Constant;
import com.beisheng.easycar.mode.HelpVO;
import com.beisheng.easycar.mode.MyCouponVO;
import com.loopj.android.http.RequestParams;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.view.AutoSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGADivider;

public class HelpActivity extends BaseActivity {

    private AutoSwipeRefreshLayout mAutoSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private HelpAdapter mAdapter;
    private int mCurrentPage = 1;
    private boolean isLoadMore = true;
    private MyCouponVO mMyCouponVO;


    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.activity_help);
    }

    @Override
    public void initView() {
        mBaseHeadLayout.setBackgroundResource(R.color.Car2);
        mBaseHeadLayout.setBackgroundResource(R.color.Car2);
        mBaseTitleTv.setText("我的行程");
        mAutoSwipeRefreshLayout = getViewById(R.id.swipe_refresh_layout);
//        setSwipeRefreshLayoutColors(mAutoSwipeRefreshLayout);
        mRecyclerView = getViewById(R.id.recycler_view);
//        DividerDecoration divider = DividerUtil.linnerDivider(this, R.dimen.dp_10, R.color.C3);



        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new HelpAdapter(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        BGADivider divider = BGADivider.newShapeDivider() //设置分割线,用BGADivider 可以去掉分类顶部的分割线
                .setSizeDp(10)
                .setColorResource(R.color.C3,false)
                .setDelegate(new BGADivider.SimpleDelegate() {
                    @Override
                    public boolean isNeedSkip(int position, int itemCount) {
                        // 如果是分类的话就跳过，顶部不绘制分隔线
                        if (mAdapter.getItemViewType(position)==R.layout.adapter_help_type2) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                });
        mRecyclerView.addItemDecoration(divider);
        dismissProgressDialog();
//        mAutoSwipeRefreshLayout.autoRefresh();
    }

    @Override
    public void bindViewsListener() {

    }

    @Override
    public void getData() {
        RequestParams params = new RequestParams();
        String urlList = Constant.HELP_URL;
        HttpClientUtil.get(mActivity, mThreadUtil, urlList, params, HelpVO.class);
    }

    @Override
    public void hasData(BaseVO vo) {
        HelpVO helpVO= (HelpVO) vo;
        List<HelpVO> allList=((HelpVO) vo).getData();
        List<HelpVO> list = new ArrayList<>();
        for (int i = 0; i <allList.size() ; i++) {
            list.add(allList.get(i));
            for (int j = 0; j <allList.get(i).getNewsList().size() ; j++) {
                list.add(allList.get(i).getNewsList().get(j));
            }
        }

        mAdapter.updateData(list);
    }

    @Override
    public void noData(BaseVO vo) {

    }

    @Override
    public void noNet() {

    }
}
