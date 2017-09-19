package com.xiaojing.shop.fragment;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.loopj.android.http.RequestParams;
import com.wuzhanglong.library.fragment.BaseFragment;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.utils.SharePreferenceUtil;
import com.wuzhanglong.library.utils.ThreadUtil;
import com.xiaojing.shop.R;
import com.xiaojing.shop.activity.KeyWrodActivity;
import com.xiaojing.shop.activity.LoginActivity;
import com.xiaojing.shop.activity.WebViewActivity;
import com.xiaojing.shop.adapter.HomeRAdapter;
import com.xiaojing.shop.application.AppApplication;
import com.xiaojing.shop.constant.Constant;
import com.xiaojing.shop.mode.HomeVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/9.
 */

public class TabOneFragment extends BaseFragment implements OnLoadMoreListener, OnRefreshListener, View.OnClickListener {
    RequestParams mParamsMap = new RequestParams();
    private RecyclerView mRecyclerView;
    //    private HomeRAdapter2 mRAdapter;
    private HomeRAdapter mRAdapter;
    private LinearLayout mTitleLayout;
    private int mDistanceY;
    private TextView mSerachEt;
    private TextView mCity;
    private ImageView mMsgImg,mStateImg;

    @Override
    public void setContentView() {
        contentInflateView(R.layout.tab_one_fragment);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initView(View view) {
        mActivity.SetTranslanteBar();
        mTitleLayout = (LinearLayout) view.findViewById(R.id.title_view);
        mCity = getViewById(R.id.city_tv);
        if (SharePreferenceUtil.getSharedpreferenceValue(mActivity, "address", "city") != null) {
            mCity.setText(SharePreferenceUtil.getSharedpreferenceValue(mActivity, "address", "city"));
        } else {
            mCity.setText("襄阳市");
        }

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        GridLayoutManager mLayoutManager = new GridLayoutManager(mActivity, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mRAdapter.getItemViewType(position) == R.layout.home_adapter_type7) {
                    return 1;
                } else {
                    return 2;
                }
            }
        });
        mRAdapter = new HomeRAdapter(mRecyclerView);
        mSerachEt = (TextView) view.findViewById(R.id.search_et);
//        mSerachEt.setBackground(BaseCommonUtils.setBackgroundShap(mActivity, 5, R.color.C19, R.color.C19));
        mMsgImg=getViewById(R.id.msg_img);
        mStateImg=getViewById(R.id.state_img);
    }


    @Override
    public void bindViewsListener() {
        mSerachEt.setOnClickListener(this);
        /**
         * 滑动标题栏渐变
         */
//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                //滑动的距离
//                mDistanceY += dy;
//                //toolbar的高度
////                int toolbarHeight = mTitleLayout.getBottom();
//                int toolbarHeight = BaseCommonUtils.dip2px(mActivity, 200);
//                //当滑动的距离 <= toolbar高度的时候，改变Toolbar背景色的透明度，达到渐变的效果
//                if (mDistanceY <= toolbarHeight) {
//                    float scale = (float) mDistanceY / toolbarHeight;
//                    float alpha = scale * 255;
//                    mTitleLayout.setBackgroundColor(Color.argb((int) alpha, 0, 153, 223));
////                    RxAnimationUtils.animationColorGradient(R.color.C15, R.color.C7, new onUpdateListener() {
////                        @Override
////                        public void onUpdate(int i) {
////
////                        }
////                    });
//                } else {
//                    //将标题栏的颜色设置为完全不透明状态
//                    mTitleLayout.setBackgroundResource(R.color.C7);
//                }
//            }
//        });

        mMsgImg.setOnClickListener(this);
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
        if (!(vo instanceof HomeVO)) {
            mThreadUtil = new ThreadUtil(mActivity, this);
            mThreadUtil.start();
            return;
        }
        mActivity.dismissProgressDialog();
        HomeVO homeVO = (HomeVO) vo;
        SharePreferenceUtil.putSharedpreferences(mActivity, "game_tag", "tag", homeVO.getTag());

        List<HomeVO> list = new ArrayList<>();
        for (int i = 0; i < homeVO.getDatas().size(); i++) {
            if (i == 0) {
                list.add(homeVO.getDatas().get(i));
                HomeVO home = new HomeVO();
                home.setSign_url(homeVO.getSign_url());
                home.setType("6");//8 个icon 模块
                list.add(home);
            } else if (i == homeVO.getDatas().size() - 1) {
                for (int j = 0; j < homeVO.getDatas().get(i).getItem_data().getItem().size(); j++) {
                    if (j == 0) {
                        HomeVO likeTitleVO = new HomeVO();
                        likeTitleVO.setType("7");//猜你喜欢标题;
                        list.add(likeTitleVO);
                    }
                    HomeVO likeVO = new HomeVO();
                    HomeVO itemData = homeVO.getDatas().get(i).getItem_data().getItem().get(j);
                    likeVO.setItem_data(itemData);
                    likeVO.setType("5");
                    list.add(likeVO);
                }
            } else {
                list.add(homeVO.getDatas().get(i));
            }
        }
        mRAdapter.updateData(list);
//        mRAdapter.updateData(homeVO.getDatas());
        mRecyclerView.setAdapter(mRAdapter);
        mRAdapter.setSignUrl(homeVO.getSign_url());
        SharePreferenceUtil.putSharedpreferences(mActivity, "reset_login_password", "password_url", homeVO.getFind_pwd_url());
        SharePreferenceUtil.putSharedpreferences(mActivity, "jpush", "msg", homeVO.getMessage_url());
        SharePreferenceUtil.putSharedpreferences(mActivity, "jpush", "msg_state", homeVO.getUnread_message());
        SharePreferenceUtil.putSharedpreferences(mActivity, "agreement", "agreement_url", homeVO.getAgreement_url());
        SharePreferenceUtil.putSharedpreferences(mActivity, "aboutus_url", "url", homeVO.getAboutus_url());
        SharePreferenceUtil.putSharedpreferences(mActivity, "help_url", "url", homeVO.getHelp_url());
        if("1".equals(homeVO.getUnread_message())){
            mStateImg.setVisibility(View.VISIBLE);
        }else{
            mStateImg.setVisibility(View.GONE);
        }
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
            case R.id.msg_img:
                Bundle bundle =new Bundle();
                if(AppApplication.getInstance().getUserInfoVO() ==null){
                    bundle.putString("type","2");
                    mActivity.open(LoginActivity.class,bundle,1);
                    return;
                }
                bundle.putString("url", SharePreferenceUtil.getSharedpreferenceValue(mActivity,"jpush","msg"));
                mActivity.open(WebViewActivity.class,bundle,0);
                mStateImg.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }
}
