package com.xiaojing.shop.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cpoopc.scrollablelayoutlib.ScrollableHelper;
import com.cpoopc.scrollablelayoutlib.ScrollableLayout;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.wuzhanglong.library.ItemDecoration.DividerDecoration;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.utils.DividerUtil;
import com.xiaojing.shop.R;
import com.xiaojing.shop.adapter.GameAdapter;
import com.xiaojing.shop.constant.Constant;
import com.xiaojing.shop.mode.GameVO;

import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;

public class GameActivity extends BaseActivity implements BGAOnRVItemClickListener, ScrollableHelper.ScrollableContainer {
    private LuRecyclerView mRecyclerView;
    private GameAdapter mAdapter;
    private ScrollableLayout mScrollableLayout;
    private ImageView mImg;
    private GameVO mGameVO;

    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.game_activity);
    }

    @Override
    public void initView() {
        mBaseTitleTv.setText("游戏");
        mImg = getViewById(R.id.img);
        mScrollableLayout = getViewById(R.id.scrollable_Layout);
        mRecyclerView = getViewById(R.id.recycler_view);
        mScrollableLayout.getHelper().setCurrentScrollableContainer(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        DividerDecoration divider = DividerUtil.linnerDivider(this, R.dimen.dp_1, R.color.C3);
        mRecyclerView.addItemDecoration(divider);
        mAdapter = new GameAdapter(mRecyclerView);
        LuRecyclerViewAdapter adapter = new LuRecyclerViewAdapter(mAdapter);
//        adapter.addHeaderView(initHeadView());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLoadMoreEnabled(false);

    }

    @Override
    public void bindViewsListener() {
        mAdapter.setOnRVItemClickListener(this);
        mScrollableLayout.setOnScrollListener(new ScrollableLayout.OnScrollListener() {
            @Override
            public void onScroll(int currentY, int maxY) {
                mImg.setTranslationY(currentY / 2);

            }
        });
        mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("url", mGameVO.getGame_banner_url());
                open(WebViewActivity.class, bundle, 0);
            }
        });
    }

    @Override
    public void getData() {
        RequestParams params = new RequestParams();
        String mUrl = Constant.GAME_LIST_URL;
        HttpClientUtil.get(mActivity, mThreadUtil, mUrl, params, GameVO.class);
    }


    @Override
    public void hasData(BaseVO vo) {
        mGameVO = (GameVO) vo;

        Picasso.with(this).load(mGameVO.getGame_banner()).into(mImg);
        mAdapter.updateData(mGameVO.getDatas().getList());

    }

    @Override
    public void noData(BaseVO vo) {

    }

    @Override
    public void noNet() {

    }

    public View initHeadView() {
        View view = LayoutInflater.from(this).inflate(R.layout.game_header, (ViewGroup) findViewById(android.R.id.content), false);
        return view;
    }

    @Override
    public void onRVItemClick(ViewGroup viewGroup, View view, int i) {
        if (mAdapter.getData().size() == 0)
            return;
        GameVO vo = (GameVO) mAdapter.getData().get(i);
        Bundle bundle = new Bundle();
        bundle.putString("id", vo.getGame_id());
        open(GameDetailActivity.class, bundle, 0);
    }

    @Override
    public View getScrollableView() {
        return mRecyclerView;
    }
}
