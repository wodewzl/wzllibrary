package com.xiaojing.shop.activity;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.loopj.android.http.RequestParams;
import com.rey.material.widget.Button;
import com.wuzhanglong.library.ItemDecoration.DividerDecoration;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.wuzhanglong.library.utils.DividerUtil;
import com.xiaojing.shop.R;
import com.xiaojing.shop.adapter.AddressRAdapter;
import com.xiaojing.shop.application.AppApplication;
import com.xiaojing.shop.constant.Constant;
import com.xiaojing.shop.mode.AddressVO;

public class AddressActivity extends BaseActivity implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private AddressRAdapter mAdapter;
    private Button mAddBt;

    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.address_activity);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initView() {
        mBaseTitleTv.setText("地址管理");
        mRecyclerView = getViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new AddressRAdapter(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        DividerDecoration divider = DividerUtil.linnerDivider(this, R.dimen.dp_10, R.color.C3);
        mRecyclerView.addItemDecoration(divider);
        mAddBt = getViewById(R.id.add_bt);
        mAddBt.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.C7, R.color.C7));
    }

    @Override
    public void bindViewsListener() {
        mAddBt.setOnClickListener(this);
    }

    @Override
    public void getData() {
        RequestParams paramsMap = new RequestParams();
        String mUrl = Constant.GET_ADDRESS_URL;
        if (AppApplication.getInstance().getUserInfoVO() != null)
            paramsMap.put("key", AppApplication.getInstance().getUserInfoVO().getKey());
        HttpClientUtil.get(mActivity, mThreadUtil, mUrl, paramsMap, AddressVO.class);
    }

    @Override
    public void hasData(BaseVO vo) {
        AddressVO addressVO = (AddressVO) vo;
        mAdapter.updateData(addressVO.getDatas().getAddress_list());
    }

    @Override
    public void noData(BaseVO vo) {

    }

    @Override
    public void noNet() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_bt:
                openActivity(AddressAddActivity.class);
                break;
            default:
                break;
        }
    }
}
