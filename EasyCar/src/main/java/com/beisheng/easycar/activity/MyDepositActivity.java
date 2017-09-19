package com.beisheng.easycar.activity;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

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

public class MyDepositActivity extends BaseActivity implements PostCallback {
    private CheckBox mPayCb1, mPayCb2;
    private Button mCommitBt;
    private String mPayType = "";
    private TextView mMoneyTv;

    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.activity_my_deposit);
    }

    @Override
    public void initView() {
        mBaseHeadLayout.setBackgroundResource(R.color.Car2);
        mBaseTitleTv.setText("充值");
        mPayCb1 = (CheckBox) findViewById(R.id.pay_cb_1);
        mPayCb2 = (CheckBox) findViewById(R.id.pay_cb_2);
        mMoneyTv = getViewById(R.id.moeny_tv);
        mCommitBt = (Button) findViewById(R.id.ok_bt);
        mCommitBt.setBackgroundDrawable(BaseCommonUtils.setBackgroundShap(mActivity, 5, R.color.Car2, R.color.Car2));
    }

    @Override
    public void bindViewsListener() {
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

        EventBus.getDefault().register(this);
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


    public void commit(final String payType) {
        showProgressDialog();
        RequestParams params = new RequestParams();
        if (AppApplication.getInstance().getUserInfoVO() != null)
            params.put("uin", AppApplication.getInstance().getUserInfoVO().getUin());
        params.put("pay_type", payType);
        params.put("money", mMoneyTv.getText().toString().split("￥")[1]);
        params.put("type", "2");
        final String mUrl = Constant.RECHARGE_URL;
        HttpClientUtil.post(mActivity, this, mUrl, params, PayTypeVO.class, this);
    }


    @Override
    public void success(BaseVO vo) {
        PayTypeVO payTypeVO = (PayTypeVO) vo;
        try {
            mActivity.dismissProgressDialog();
            if ("200".equals(payTypeVO.getCode())) {
                if (mPayCb1.isChecked()) {
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
                }

                if (mPayCb2.isChecked()) {
                    PayUtis.weiXinPay(mActivity, payTypeVO.getData().getOrder());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EBMessageVO event) {

        if ("weixin_pay".equals(event.getMessage())) {
            mActivity.showCustomToast("支付成功");
            EventBus.getDefault().post(new EBMessageVO("recharge"));
            mBaseHeadLayout.postDelayed(new Runnable() {
                @Override
                public void run() {

                    MyDepositActivity.this.finish();
                }
            },1000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
