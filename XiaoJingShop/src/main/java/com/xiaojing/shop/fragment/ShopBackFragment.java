package com.xiaojing.shop.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.vondear.rxtools.view.sidebar.PinnedHeaderDecoration;
import com.wuzhanglong.library.constant.BaseConstant;
import com.wuzhanglong.library.fragment.BaseFragment;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.mode.EBMessageVO;
import com.wuzhanglong.library.utils.ThreadUtil;
import com.wuzhanglong.library.view.AutoSwipeRefreshLayout;
import com.xiaojing.shop.R;
import com.xiaojing.shop.activity.WebViewActivity;
import com.xiaojing.shop.adapter.ShopBackAdapter;
import com.xiaojing.shop.application.AppApplication;
import com.xiaojing.shop.constant.Constant;
import com.xiaojing.shop.mode.OrderVO;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.Header;

public class ShopBackFragment extends BaseFragment implements OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, ShopBackAdapter.onTypeClickListener, BGAOnRVItemClickListener {
    private AutoSwipeRefreshLayout mAutoSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView;
    private ShopBackAdapter mAdapter;
    private int state;//1-退款 2-退货 默认为1
    private OrderVO mOrderVO;
    private int mCurrentPage = 1;
    private boolean isLoadMore = false;

    public static BaseFragment newInstance() {
        BaseFragment fragment = new ShopBackFragment();
        return fragment;
    }

    @Override
    public void getData() {
        RequestParams paramsMap = new RequestParams();
        String mUrl = Constant.SHOP_BACK_URL;
        if (AppApplication.getInstance().getUserInfoVO() != null)
            paramsMap.put("key", AppApplication.getInstance().getUserInfoVO().getKey());
        paramsMap.put("type", getState());
        paramsMap.put("curpage", mCurrentPage + "");
        HttpClientUtil.get(mActivity, mThreadUtil, mUrl, paramsMap, OrderVO.class);
    }

    @Override
    public void hasData(BaseVO vo) {
        mActivity.dismissProgressDialog();
        OrderVO orderVO = (OrderVO) vo;
        mOrderVO = orderVO.getDatas();
        List<OrderVO> list = new ArrayList<>();
        for (int i = 0; i < mOrderVO.getOrder_list().size(); i++) {
            OrderVO orderStaeVO = new OrderVO();
            orderStaeVO.setOrder_id(mOrderVO.getOrder_list().get(i).getOrder_id());
            orderStaeVO.setStore_name(mOrderVO.getOrder_list().get(i).getStore_name());
            orderStaeVO.setState_desc(mOrderVO.getOrder_list().get(i).getState_desc());
            orderStaeVO.setOrder(true);
            list.add(orderStaeVO);
            for (int j = 0; j < mOrderVO.getOrder_list().get(i).getGoods_list().size(); j++) {
                list.add(mOrderVO.getOrder_list().get(i).getGoods_list().get(j));
            }
            list.add(mOrderVO.getOrder_list().get(i));
        }

        if (vo.getHasmore() != null && "1".equals(vo.getHasmore())) {
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

    }

    @Override
    public void noData(BaseVO vo) {

    }

    @Override
    public void noNet() {

    }

    @Override
    public void setContentView() {
        contentInflateView(R.layout.shop_back_fragment);
    }

    @Override
    public void initView(View view) {
        mAutoSwipeRefreshLayout = getViewById(R.id.swipe_refresh_layout);
        mActivity.setSwipeRefreshLayoutColors(mAutoSwipeRefreshLayout);
        mRecyclerView = getViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

        final PinnedHeaderDecoration decoration = new PinnedHeaderDecoration();
        decoration.registerTypePinnedHeader(R.layout.order_adapter_type1, new PinnedHeaderDecoration.PinnedHeaderCreator() {
            @Override
            public boolean create(RecyclerView parent, int adapterPosition) {
                return true;
            }
        });
        mRecyclerView.addItemDecoration(decoration);
        mAdapter = new ShopBackAdapter(mRecyclerView);
        LuRecyclerViewAdapter adapter = new LuRecyclerViewAdapter(mAdapter);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLoadMoreEnabled(true);
    }

    @Override
    public void bindViewsListener() {
        mRecyclerView.setOnLoadMoreListener(this);
        mAutoSwipeRefreshLayout.setOnRefreshListener(this);

        mAdapter.setOnRVItemClickListener(this);
        mAdapter.setOnTypeClickListener(this);
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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

    @Override
    public void onRVItemClick(ViewGroup viewGroup, View view, int i) {
        if (mAdapter.getData().size() == 0)
            return;
        OrderVO vo = (OrderVO) mAdapter.getData().get(i);
        Bundle bundle = new Bundle();
        bundle.putString("url", vo.getDetail_url());
        mActivity.open(WebViewActivity.class, bundle, 0);
    }


    @Override
    public void typeClick(String type, final OrderVO vo) {
        if ("2".equals(type)) {
            new SweetAlertDialog(mActivity, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("注意")
                    .setContentText(vo.getDelay_confirm_text())
                    .setConfirmText("确定")
                    .setCancelText("取消")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            commit(vo.getRefund_id());
                            sDialog.dismissWithAnimation();//直接消失
                        }
                    })
                    .show();
        }

    }

    public void commit(final String orderId) {
        RequestParams params = new RequestParams();
        if (AppApplication.getInstance().getUserInfoVO() != null)
            params.put("key", AppApplication.getInstance().getUserInfoVO().getKey());
        params.put(" refund_id", orderId);

        final String allUrl = BaseConstant.DOMAIN_NAME + Constant.SHOP_BACK_ACTION_TWO_URL;
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(allUrl, params, new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                mActivity.dismissProgressDialog();
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                try {
                    mActivity.dismissProgressDialog();
                    String result = new String(arg2);
                    JSONObject jsonObject = new JSONObject(result);
                    String code = (String) jsonObject.get("code");
                    if ("200".equals(code)) {
                        mActivity.showCustomToast("操作成功");
                       mAutoSwipeRefreshLayout.autoRefresh();
                    } else {
                        String error = (String) jsonObject.get("error");
                        mActivity.showCustomToast(error);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EBMessageVO event) {
        if ("back_webview".equals(event.getMessage())) {
            mAutoSwipeRefreshLayout.autoRefresh();
        }
    }


}
