package com.xiaojing.shop.activity;

import android.support.v7.widget.RecyclerView;

import com.wuzhanglong.library.ItemDecoration.StickyHeaderDecoration;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.mode.BaseVO;
import com.xiaojing.shop.R;
import com.xiaojing.shop.adapter.ShopCatLRAdapter;
import com.xiaojing.shop.adapter.TestRAdapter;
import com.xiaojing.shop.mode.CategoryVO;

public class TestActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private ShopCatLRAdapter mAdapter;
    private CategoryVO mCategoryVO;
    //    private MyJBAdapter mMyJBAdapter;
    private StickyHeaderDecoration decor;
    private TestRAdapter mTestAdapter;

    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.activity_test);
    }

    @Override
    public void initView() {

    }

    @Override
    public void bindViewsListener() {

    }

    @Override
    public void getData() {
        HttpClientUtil.show(mThreadUtil);
    }

    @Override
    public void hasData(BaseVO vo) {

    }

    @Override
    public void noData(BaseVO vo) {

    }

    @Override
    public void noNet() {

    }
}
