package com.xiaojing.shop.fragment;


import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.loopj.android.http.RequestParams;
import com.wuzhanglong.library.fragment.BaseFragment;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.wuzhanglong.library.utils.SharePreferenceUtil;
import com.xiaojing.shop.R;
import com.xiaojing.shop.activity.KeyWrodActivity;
import com.xiaojing.shop.adapter.HomeRAdapter2;
import com.xiaojing.shop.application.AppApplication;
import com.xiaojing.shop.constant.Constant;
import com.xiaojing.shop.mode.HomeVO;

/**
 * Created by Administrator on 2017/2/9.
 */

public class TabOneFragment2 extends BaseFragment implements OnLoadMoreListener, OnRefreshListener, View.OnClickListener {
    RequestParams mParamsMap = new RequestParams();
    private RecyclerView mRecyclerView;
    private HomeRAdapter2 mRAdapter;
    private LinearLayout mTitleLayout;
    private int mDistanceY;
    private TextView mSerachEt;
    private TextView mCity;

    @Override
    public void setContentView() {
        contentInflateView(R.layout.tab_one_fragment);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initView(View view) {
        mTitleLayout = (LinearLayout) view.findViewById(R.id.title_view);
        mCity = getViewById(R.id.city_tv);
        if(SharePreferenceUtil.getSharedpreferenceValue(mActivity, "address", "city") !=null){
            mCity.setText(SharePreferenceUtil.getSharedpreferenceValue(mActivity, "address", "city"));
        }else{
            mCity.setText("襄阳市");
        }

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRAdapter = new HomeRAdapter2(mRecyclerView);
        mSerachEt = (TextView) view.findViewById(R.id.search_et);
//        mSerachEt.setBackground(BaseCommonUtils.setBackgroundShap(mActivity, 5, R.color.C19, R.color.C19));
    }


    @Override
    public void bindViewsListener() {
        mSerachEt.setOnClickListener(this);
        /**
         * 滑动标题栏渐变
         */
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //滑动的距离
                mDistanceY += dy;
                //toolbar的高度
//                int toolbarHeight = mTitleLayout.getBottom();
                int toolbarHeight = BaseCommonUtils.dip2px(mActivity, 200);
                //当滑动的距离 <= toolbar高度的时候，改变Toolbar背景色的透明度，达到渐变的效果
                if (mDistanceY <= toolbarHeight) {
                    float scale = (float) mDistanceY / toolbarHeight;
                    float alpha = scale * 255;
                    mTitleLayout.setBackgroundColor(Color.argb((int) alpha, 0, 153, 223));
//                    RxAnimationUtils.animationColorGradient(R.color.C15, R.color.C7, new onUpdateListener() {
//                        @Override
//                        public void onUpdate(int i) {
//
//                        }
//                    });
                } else {
                    //将标题栏的颜色设置为完全不透明状态
                    mTitleLayout.setBackgroundResource(R.color.C7);
                }
            }
        });


    }

    @Override
    public void getData() {
        RequestParams paramsMap = new RequestParams();
        String mUrl = Constant.HOME_URL;
        if (AppApplication.getInstance().getUserInfoVO() != null)
            paramsMap.put("key", AppApplication.getInstance().getUserInfoVO().getKey());
        HttpClientUtil.get(mActivity, mThreadUtil, mUrl, paramsMap, HomeVO.class);
    }

    @Override
    public void hasData(BaseVO vo) {
        HomeVO homeVO = (HomeVO) vo;
        mRAdapter.updateData(homeVO.getDatas());
        mRecyclerView.setAdapter(mRAdapter);
        mActivity.dismissProgressDialog();
        mRAdapter.setSignUrl(homeVO.getSign_url());
        SharePreferenceUtil.putSharedpreferences(mActivity,"reset_login_password","password_url",homeVO.getFind_pwd_url());
    }

    @Override
    public void noData(BaseVO vo) {
    }

    @Override
    public void noNet() {
    }


    @Override
    public void onLoadMore() {

    }

    @Override
    public void onRefresh() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_et:
                mActivity.openActivity(KeyWrodActivity.class);
                break;
            default:
                break;
        }
    }
}
