package com.xiaojing.shop.activity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.RequestParams;
import com.rey.material.widget.Button;
import com.wuzhanglong.library.ItemDecoration.DividerDecoration;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.wuzhanglong.library.utils.DividerUtil;
import com.xiaojing.shop.R;
import com.xiaojing.shop.adapter.CardShowAdapter;
import com.xiaojing.shop.application.AppApplication;
import com.xiaojing.shop.constant.Constant;
import com.xiaojing.shop.mode.PayTypeVO;

import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;

public class CardShowActivity extends BaseActivity implements View.OnClickListener, BGAOnRVItemClickListener {
    private RecyclerView mRecyclerView;
    private CardShowAdapter mAdapter;
    private Button mAddBt;
    private PayTypeVO mCurrentSelectVO;

    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.card_show_activity);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initView() {
        mBaseTitleTv.setText("银行卡");
        mRecyclerView = getViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CardShowAdapter(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        DividerDecoration divider = DividerUtil.linnerDivider(this, R.dimen.dp_10, R.color.C3);
        mRecyclerView.addItemDecoration(divider);
        mAddBt = getViewById(R.id.add_bt);
        mAddBt.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.C7, R.color.C7));
    }

    @Override
    public void bindViewsListener() {
        mAddBt.setOnClickListener(this);
        mAdapter.setOnRVItemClickListener(this);
    }

    @Override
    public void getData() {
        if ("1".equals(this.getIntent().getStringExtra("type"))) {
            //所有银行卡类型列表
            RequestParams params = new RequestParams();
            String mUrl = Constant.BLANK_TYPE_URL;
            HttpClientUtil.get(mActivity, mThreadUtil, mUrl, params, PayTypeVO.class);
        } else {
            //我的银行卡类型列表
            RequestParams params = new RequestParams();
            String mUrl = Constant.MY_BLANK_URL;
            if (AppApplication.getInstance().getUserInfoVO() != null)
                params.put("key", AppApplication.getInstance().getUserInfoVO().getKey());
            HttpClientUtil.get(mActivity, mThreadUtil, mUrl, params, PayTypeVO.class);
        }

    }

    @Override
    public void hasData(BaseVO vo) {
        PayTypeVO payTypeVO = (PayTypeVO) vo;
        if ("1".equals(this.getIntent().getStringExtra("type"))) {
            mAdapter.updateData(payTypeVO.getDatas().getList());
        } else {
            mAdapter.updateData(payTypeVO.getDatas().getCard_list());
        }

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
                openActivity(CardBindActivity.class);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRVItemClick(ViewGroup viewGroup, View view, int i) {
        if (mAdapter.getData().size() == 0)
            return;
//
//        for (int j = 0; j <mAdapter.getData().size() ; j++) {
//            PayTypeVO vo= (PayTypeVO) mAdapter.getData().get(j);
//            vo.setCheck(false);
//        }
        PayTypeVO selectVO = (PayTypeVO) mAdapter.getData().get(i);
        selectVO.setCheck(true);
        mAdapter.notifyDataSetChanged();
        Intent intent = new Intent();
        intent.putExtra("name", selectVO.getBank_name());
        intent.putExtra("code", selectVO.getBank_code());
        intent.putExtra("no", selectVO.getCard_no());
        intent.putExtra("img", selectVO.getBank_image());
        intent.putExtra("id", selectVO.getCard_id());
        this.setResult(1, intent);
        this.finish();
    }
}
