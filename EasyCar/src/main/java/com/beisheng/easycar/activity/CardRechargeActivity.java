package com.beisheng.easycar.activity;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.beisheng.easycar.R;
import com.beisheng.easycar.application.AppApplication;
import com.beisheng.easycar.constant.Constant;
import com.beisheng.easycar.mode.PayTypeVO;
import com.loopj.android.http.RequestParams;
import com.rey.material.widget.Button;
import com.rey.material.widget.CheckBox;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.interfaces.PayCallback;
import com.wuzhanglong.library.interfaces.PostCallback;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.mode.EBMessageVO;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.wuzhanglong.library.utils.PayUtis;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class CardRechargeActivity extends BaseActivity implements PostCallback{
    private CheckBox mPayCb1, mPayCb2;
    private Button mCommitBt;
    private LinearLayout mPayLayout01, mPayLayout02;
    private EditText mMoneyEt;
    private String mPayType = "alipay_app";

    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.card_recharge_activity);
    }

    @Override
    public void initView() {
        mBaseHeadLayout.setBackgroundResource(R.color.Car2);
        mBaseTitleTv.setText("充值");
        mPayCb1 = (CheckBox) findViewById(R.id.pay_cb_1);
        mPayCb2 = (CheckBox) findViewById(R.id.pay_cb_2);
        mCommitBt = (Button) findViewById(R.id.commit_bt);
        mCommitBt.setBackgroundDrawable(BaseCommonUtils.setBackgroundShap(mActivity, 5, R.color.C7, R.color.C7));
        mPayLayout01 = (LinearLayout) findViewById(R.id.pay_layout_01);
        mPayLayout02 = (LinearLayout) findViewById(R.id.pay_layout_02);
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

                }
            }
        });
        mPayCb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mPayCb1.setChecked(false);

                }
            }
        });


        mCommitBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPayCb1.isChecked()) {
                    mPayType = "1";
                } else if (mPayCb2.isChecked()) {
                    mPayType = "2";
                } else {
                    mPayType = "";
                }
                commit(mPayType);
            }
        });
    }

    @Override
    public void getData() {
        HttpClientUtil.show(mThreadUtil);
//        RequestParams params = new RequestParams();
//        String url = Constant.PAY_TYPE_URL;
//        HttpClientUtil.get(mActivity, mThreadUtil, url, params, PayTypeVO.class);
    }

    @Override
    public void hasData(BaseVO vo) {
        PayTypeVO payTypeVO = (PayTypeVO) vo;
        for (int i = 0; i < payTypeVO.getDatas().getList().size(); i++) {
            if ("alipay_app".equals(payTypeVO.getDatas().getList().get(i).getPayment_code())) {
                mPayLayout01.setVisibility(View.VISIBLE);
            } else if ("wxpay_app".equals(payTypeVO.getDatas().getList().get(i).getPayment_code())) {
                mPayLayout02.setVisibility(View.VISIBLE);
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
            params.put("uin", AppApplication.getInstance().getUserInfoVO().getUin());
        params.put("pay_type", payType);
        params.put("money", mMoneyEt.getText().toString());
        params.put("type", "1");
        final String mUrl = Constant.RECHARGE_URL;
        HttpClientUtil.post(mActivity, this, mUrl, params, PayTypeVO.class, this);
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

    @Override
    public void success(BaseVO vo) {
        PayTypeVO payTypeVO= (PayTypeVO) vo;
        try {
            mActivity.dismissProgressDialog();
            if ("200".equals(payTypeVO.getCode())) {
                final String payInfo = payTypeVO.getData().getPayOrder();
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



//                if (vo.getDatas().getWxpay_param() != null) {
//                    PayUtis.weiXinPay(mActivity, vo.getDatas().getWxpay_param());
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
