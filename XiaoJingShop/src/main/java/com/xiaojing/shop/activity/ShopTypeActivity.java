package com.xiaojing.shop.activity;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.RequestParams;
import com.wuzhanglong.library.ItemDecoration.DividerDecoration;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.mode.EBMessageVO;
import com.wuzhanglong.library.utils.DividerUtil;
import com.xiaojing.shop.R;
import com.xiaojing.shop.adapter.ShopTypeAdapter;
import com.xiaojing.shop.constant.Constant;
import com.xiaojing.shop.mode.OptionVO;

import org.greenrobot.eventbus.EventBus;

import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;

public class ShopTypeActivity extends BaseActivity implements View.OnClickListener, BGAOnRVItemClickListener {
    private RecyclerView mRecyclerView;
    private ShopTypeAdapter mAdapter;
    private OptionVO mOptionVO;

    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.shop_type_activity);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initView() {
        mBaseTitleTv.setText("商品类型");
        mBaseOkTv.setText("确定");
        mBaseOkTv.setVisibility(View.VISIBLE);
        mRecyclerView = getViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ShopTypeAdapter(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        DividerDecoration divider = DividerUtil.linnerDivider(this, R.dimen.dp_1, R.color.C3);
        mRecyclerView.addItemDecoration(divider);
    }

    @Override
    public void bindViewsListener() {
        mAdapter.setOnRVItemClickListener(this);
        mBaseOkTv.setOnClickListener(this);
    }

    @Override
    public void getData() {
            //所有银行卡类型列表
            RequestParams params = new RequestParams();
            String mUrl = Constant.SHOP_TYPE_URL;
            HttpClientUtil.get(mActivity, mThreadUtil, mUrl, params, OptionVO.class);
    }

    @Override
    public void hasData(BaseVO vo) {
        OptionVO optionVO = (OptionVO) vo;
        mAdapter.updateData(optionVO.getDatas().getList());
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
            case R.id.base_ok_tv:
                StringBuffer sbName=new StringBuffer();
                StringBuffer sbId=new StringBuffer();
                for (int i = 0; i <mAdapter.getData().size() ; i++) {
                    OptionVO vo= (OptionVO) mAdapter.getData().get(i);
                    if(vo.isCheck()){
                        sbName=sbName.append(vo.getSc_name()).append("、");
                        sbId=sbId.append(vo.getSc_id()).append(",");
                    }
                }
                EBMessageVO messageVO=new EBMessageVO("shop_type");
                String[] params= new String [2];
                params[0]=sbName.toString();
                params[1]=sbId.toString();
                messageVO.setParams(params);
                EventBus.getDefault().post(messageVO);
                this.finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRVItemClick(ViewGroup viewGroup, View view, int i) {
        if (mAdapter.getData().size() == 0)
            return;

        OptionVO selectVO = (OptionVO) mAdapter.getData().get(i);
        if(selectVO.isCheck()){
            selectVO.setCheck(false);
        }else{
            selectVO.setCheck(true);
        }

        mAdapter.notifyDataSetChanged();
    }
}
