package com.xiaojing.shop.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.rey.material.widget.Button;
import com.rey.material.widget.CheckBox;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.constant.BaseConstant;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.interfaces.PayCallback;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.mode.EBMessageVO;
import com.wuzhanglong.library.mode.PayResult;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.wuzhanglong.library.utils.PayUtis;
import com.xiaojing.shop.R;
import com.xiaojing.shop.application.AppApplication;
import com.xiaojing.shop.constant.Constant;
import com.xiaojing.shop.mode.PayTypeVO;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class CardRechargeActivity extends BaseActivity {
    private CheckBox mPayCb1, mPayCb2, mPayCb3;
    private Button mCommitBt;
    private LinearLayout mPayLayout01, mPayLayout02, mPayLayout03;
    private EditText mMoneyEt;
    private String mPayType = "alipay_app";

    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.recharge_activity);
    }

    @Override
    public void initView() {
        mBaseTitleTv.setText("充值");
        mPayCb1 = (CheckBox) findViewById(R.id.pay_cb_1);
        mPayCb2 = (CheckBox) findViewById(R.id.pay_cb_2);
        mPayCb3 = (CheckBox) findViewById(R.id.pay_cb_3);
        mCommitBt = (Button) findViewById(R.id.commit_bt);
        mCommitBt.setBackgroundDrawable(BaseCommonUtils.setBackgroundShap(mActivity, 5, R.color.C7, R.color.C7));
        mPayLayout01 = (LinearLayout) findViewById(R.id.pay_layout_01);
        mPayLayout02 = (LinearLayout) findViewById(R.id.pay_layout_02);
        mPayLayout03 = (LinearLayout) findViewById(R.id.pay_layout_03);
        mMoneyEt = getViewById(R.id.money_et);
        mPayCb1.setChecked(true);
    }

    @Override
    public void bindViewsListener() {
        EventBus.getDefault().register(this);
        mPayCb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mPayCb2.setChecked(false);
                    mPayCb3.setChecked(false);
                }
            }
        });
        mPayCb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mPayCb1.setChecked(false);
                    mPayCb3.setChecked(false);
                }
            }
        });
        mPayCb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mPayCb1.setChecked(false);
                    mPayCb2.setChecked(false);
                }
            }
        });

        mCommitBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mPayCb1.isChecked()) {
                    mPayType = "alipay_app";
                } else if (mPayCb2.isChecked()) {
                    mPayType = "wxpay_app";
                } else if (mPayCb3.isChecked()) {
                    mPayType = "99bill_app";
                } else {
                    mPayType = "alipay_app";
                }
                commit(mPayType);
            }
        });
    }

    @Override
    public void getData() {
        RequestParams params = new RequestParams();
        String url = Constant.PAY_TYPE_URL;
        HttpClientUtil.get(mActivity, mThreadUtil, url, params, PayTypeVO.class);
    }

    @Override
    public void hasData(BaseVO vo) {
        PayTypeVO payTypeVO = (PayTypeVO) vo;
        for (int i = 0; i < payTypeVO.getDatas().getList().size(); i++) {
            if ("alipay_app".equals(payTypeVO.getDatas().getList().get(i).getPayment_code())) {
                mPayLayout01.setVisibility(View.VISIBLE);
            } else if ("wxpay_app".equals(payTypeVO.getDatas().getList().get(i).getPayment_code())) {
                mPayLayout02.setVisibility(View.VISIBLE);
            } else if ("99bill_app".equals(payTypeVO.getDatas().getList().get(i).getPayment_code())) {
                mPayLayout03.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void noData(BaseVO vo) {

    }

    @Override
    public void noNet() {

    }

    public void commit(final String payType) {
        showProgressDialog();
        RequestParams params = new RequestParams();
        if (AppApplication.getInstance().getUserInfoVO() != null)
            params.put("key", AppApplication.getInstance().getUserInfoVO().getKey());
        params.put("payment_code", payType);
        params.put("pdr_amount", mMoneyEt.getText().toString());
        params.put("is_merchant", this.getIntent().getStringExtra("is_merchant"));
        final String allUrl = BaseConstant.DOMAIN_NAME + Constant.RECHARGE_URL;
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
                                bundle.putString("title", "快钱充值");
                                mActivity.open(WebViewActivity.class, bundle, 0);
                            } else if ("alipay_app".equals(payType)) {
                                PayUtis.zhiFuBaoPay(mActivity, payInfo, new PayCallback() {
                                    @Override
                                    public void payResult(int type) {
                                        if (type == 1) {
                                            mActivity.showCustomToast("充值成功");
                                            EventBus.getDefault().post(new EBMessageVO("recharge"));
                                        } else {
                                            mActivity.showCustomToast("充值失败");
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EBMessageVO event) {

        if ("kuaiqian_pay".equals(event.getMessage())) {
            mActivity.showCustomToast("支付成功");
            EventBus.getDefault().post(new EBMessageVO("recharge"));
        }

        if ("weixin_pay".equals(event.getMessage())) {
            mActivity.showCustomToast("支付成功");
            EventBus.getDefault().post(new EBMessageVO("recharge"));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
