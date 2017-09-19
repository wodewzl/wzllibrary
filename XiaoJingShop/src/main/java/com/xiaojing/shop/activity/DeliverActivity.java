package com.xiaojing.shop.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.loopj.android.http.RequestParams;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.mode.BaseVO;
import com.xiaojing.shop.R;
import com.xiaojing.shop.adapter.DeliverAdapter;
import com.xiaojing.shop.application.AppApplication;
import com.xiaojing.shop.constant.Constant;
import com.xiaojing.shop.mode.ShopVO;
import com.xyz.step.FlowViewVertical;

import java.util.ArrayList;
import java.util.List;

public class DeliverActivity extends BaseActivity {
    private FlowViewVertical mFlowViewVertical;
    private ShopVO mShopVO;
    private DeliverAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private String type = "1";//1为普通物流2为一元购物流

    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.deliver_activity);
    }

    @Override
    public void initView() {
        mBaseTitleTv.setText("物流");
        mRecyclerView = getViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mAdapter = new DeliverAdapter(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

//        mFlowViewVertical = getViewById(R.id.vflow);
//        mFlowViewVertical = getViewById(R.id.vflow);
    }

    @Override
    public void bindViewsListener() {

    }

    @Override
    public void getData() {
        RequestParams params = new RequestParams();
        String url="";
        if("2".equals(this.getIntent().getStringExtra("type"))){
            url  = Constant.ONE_SHOP_DILIVER_URL;
        }else {
            url  = Constant.SHOP_DELIVER_URL;
        }

        if (AppApplication.getInstance().getUserInfoVO() != null)
            params.put("key", AppApplication.getInstance().getUserInfoVO().getKey());
        params.put("order_id", this.getIntent().getStringExtra("order_id"));
        HttpClientUtil.get(mActivity, mThreadUtil, url, params, ShopVO.class);
    }

    @Override
    public void hasData(BaseVO vo) {
        ShopVO shopVO = (ShopVO) vo;
        mShopVO = shopVO.getDatas();
//        mFlowViewVertical.setProgress(mShopVO.getDeliver_info().length, mShopVO.getDeliver_info().length, mShopVO.getDeliver_info(), null);


        List<ShopVO> list = new ArrayList<>();
        for (int i = 0; i < mShopVO.getDeliver_info().length; i++) {
            list.add(mShopVO);
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
