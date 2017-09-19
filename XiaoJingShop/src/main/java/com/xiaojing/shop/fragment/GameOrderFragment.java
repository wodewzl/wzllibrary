package com.xiaojing.shop.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.rey.material.app.BottomSheetDialog;
import com.rey.material.widget.Button;
import com.rey.material.widget.CheckBox;
import com.vondear.rxtools.view.sidebar.PinnedHeaderDecoration;
import com.wuzhanglong.library.constant.BaseConstant;
import com.wuzhanglong.library.fragment.BaseFragment;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.interfaces.PayCallback;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.mode.EBMessageVO;
import com.wuzhanglong.library.mode.PayResult;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.wuzhanglong.library.utils.PayUtis;
import com.wuzhanglong.library.utils.ThreadUtil;
import com.wuzhanglong.library.view.AutoSwipeRefreshLayout;
import com.xiaojing.shop.R;
import com.xiaojing.shop.activity.WebViewActivity;
import com.xiaojing.shop.adapter.GameOrderAdapter;
import com.xiaojing.shop.application.AppApplication;
import com.xiaojing.shop.constant.Constant;
import com.xiaojing.shop.mode.OrderVO;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cz.msebera.android.httpclient.Header;

public class GameOrderFragment extends BaseFragment implements OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, GameOrderAdapter.onTypeClickListener, BGAOnRVItemClickListener {
    private AutoSwipeRefreshLayout mAutoSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView;
    private GameOrderAdapter mAdapter;
    private int state;//1-退款 2-退货 默认为1
    private OrderVO mOrderVO;
    private int mCurrentPage = 1;
    private boolean isLoadMore = false;
    private OrderVO mPayVO;
    private BottomSheetDialog mDialog;

    public static BaseFragment newInstance() {
        BaseFragment fragment = new GameOrderFragment();
        return fragment;
    }

    @Override
    public void getData() {
        RequestParams paramsMap = new RequestParams();
        String mUrl = Constant.GAME_ORDER_LIST_URL;
        if (AppApplication.getInstance().getUserInfoVO() != null)
            paramsMap.put("key", AppApplication.getInstance().getUserInfoVO().getKey());
        paramsMap.put("state_type", getState());
        paramsMap.put("curpage", mCurrentPage + "");
        HttpClientUtil.get(mActivity, mThreadUtil, mUrl, paramsMap, OrderVO.class);
    }

    @Override
    public void hasData(BaseVO vo) {
        mActivity.dismissProgressDialog();
        OrderVO orderVO = (OrderVO) vo;
        mPayVO = orderVO;
        mOrderVO = orderVO.getDatas();
        List<OrderVO> list = new ArrayList<>();
        for (int i = 0; i < mOrderVO.getOrder_list().size(); i++) {
            for (int j = 0; j < mOrderVO.getOrder_list().get(i).getExtend_order_goods().size(); j++) {
                list.add(mOrderVO.getOrder_list().get(i).getExtend_order_goods().get(j));
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
        contentInflateView(R.layout.game_order_fragment);
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
        mAdapter = new GameOrderAdapter(mRecyclerView);
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
        EventBus.getDefault().register(this);

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
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EBMessageVO event) {
        if ("back_webview".equals(event.getMessage()) && state==0) {
            mAutoSwipeRefreshLayout.autoRefresh();
        }
        if ("kuaiqian_pay".equals(event.getMessage())&& state==0) {
            mActivity.showCustomToast("支付成功");
            mAutoSwipeRefreshLayout.autoRefresh();
            mDialog.dismiss();
        }

        if ("weixin_pay".equals(event.getMessage())) {
            mActivity.showCustomToast("支付成功");
            mAutoSwipeRefreshLayout.autoRefresh();
            mDialog.dismiss();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    public void commit(String paySn, final String payType) {
        RequestParams params = new RequestParams();
        if (AppApplication.getInstance().getUserInfoVO() != null)
            params.put("key", AppApplication.getInstance().getUserInfoVO().getKey());
        params.put(" pay_sn", paySn);
        params.put("payment_code", payType);

        final String allUrl = BaseConstant.DOMAIN_NAME + Constant.GAME_ORDER_LIST_PAY_URL;
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
                        JSONObject data = jsonObject.getJSONObject("datas");
                        final String payInfo = (String) data.get("orderString");
                        if (!"".equals(payInfo)) {
                            if ("99bill_app".equals(payType)) {
                                Bundle bundle = new Bundle();
                                bundle.putString("url", payInfo);
                                bundle.putString("title", "快钱支付");
                                mActivity.open(WebViewActivity.class, bundle, 0);
                            } else if ("alipay_app".equals(payType)) {
                                PayUtis.zhiFuBaoPay(mActivity, payInfo, new PayCallback() {
                                    @Override
                                    public void payResult(int type) {

                                        if (type == 1) {
                                            mAutoSwipeRefreshLayout.autoRefresh();
                                            mActivity.showCustomToast("支付成功");
                                        } else {
                                            mActivity.showCustomToast("支付失败");
                                        }
                                    }
                                });
                            }
                        }else {
                            Gson gson = new Gson();
                            final PayResult vo = gson.fromJson(result, PayResult.class);
                            if (vo.getDatas().getWxpay_param() != null) {
                                PayUtis.weiXinPay(mActivity, vo.getDatas().getWxpay_param());
                            }
                        }
                    } else {
                        final String error = (String) jsonObject.get("error");
                        mActivity.showCustomToast(error);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void typeClick(String type, final OrderVO vo) {
        String orderId = vo.getOrder_id();
        String url = "";
        switch (BaseCommonUtils.parseInt(type)) {
            case 1:

                break;
            case 2:
                mDialog = new BottomSheetDialog(mActivity);
                View dialogView = View.inflate(mActivity, R.layout.pay_view, null);
                final CheckBox payCb1 = (CheckBox) dialogView.findViewById(R.id.pay_cb_1);
                final CheckBox payCb2 = (CheckBox) dialogView.findViewById(R.id.pay_cb_2);
                final CheckBox payCb3 = (CheckBox) dialogView.findViewById(R.id.pay_cb_3);
                Button payButton = (Button) dialogView.findViewById(R.id.commit_bt);
                payButton.setBackgroundDrawable(BaseCommonUtils.setBackgroundShap(mActivity, 5, R.color.C7, R.color.C7));
                LinearLayout payLayout01 = (LinearLayout) dialogView.findViewById(R.id.pay_layout_01);
                LinearLayout payLayout02 = (LinearLayout) dialogView.findViewById(R.id.pay_layout_02);
                LinearLayout payLayout03 = (LinearLayout) dialogView.findViewById(R.id.pay_layout_03);

                for (int i = 0; i < mPayVO.getPayment_list().size(); i++) {
                    if ("alipay_app".equals(mPayVO.getPayment_list().get(i).getPayment_code())) {
                        payLayout01.setVisibility(View.VISIBLE);
                    } else if ("wxpay_app".equals(mPayVO.getPayment_list().get(i).getPayment_code())) {
                        payLayout02.setVisibility(View.VISIBLE);
                    } else if ("99bill_app".equals(mPayVO.getPayment_list().get(i).getPayment_code())) {
                        payLayout03.setVisibility(View.VISIBLE);
                    }
                }

                mDialog.contentView(dialogView)
                        .heightParam(ViewGroup.LayoutParams.WRAP_CONTENT)
                        .inDuration(500)
                        .cancelable(true)
                        .show();

                payCb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            payCb2.setChecked(false);
                            payCb3.setChecked(false);
                        }
                    }
                });
                payCb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            payCb1.setChecked(false);
                            payCb3.setChecked(false);
                        }
                    }
                });
                payCb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            payCb1.setChecked(false);
                            payCb2.setChecked(false);
                        }
                    }
                });

                payButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String payType = "";
                        if (payCb1.isChecked()) {
                            payType = "alipay_app";
                        } else if (payCb2.isChecked()) {
                            payType = "wxpay_app";
                        } else {
                            payType = "99bill_app";
                        }

                        commit(vo.getPay_sn(), payType);
                    }
                });
                break;
            case 3:

                break;
            case 4:

                break;
            case 5:

                break;
            default:
                break;
        }
    }


}
