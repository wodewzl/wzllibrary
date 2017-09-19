package com.xiaojing.shop.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.rey.material.widget.Button;
import com.rey.material.widget.CheckBox;
import com.suke.widget.SwitchButton;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.constant.BaseConstant;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.interfaces.PayCallback;
import com.wuzhanglong.library.interfaces.PostCallback;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.mode.EBMessageVO;
import com.wuzhanglong.library.mode.PayResult;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.wuzhanglong.library.utils.NumberTypeUtil;
import com.wuzhanglong.library.utils.PayUtis;
import com.wuzhanglong.library.utils.ThreadUtil;
import com.xiaojing.shop.R;
import com.xiaojing.shop.application.AppApplication;
import com.xiaojing.shop.constant.Constant;
import com.xiaojing.shop.mode.OrderVO;
import com.xiaojing.shop.view.SercurityDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.xiaojing.shop.R.id.commit_bt;

public class OrderSureActivity extends BaseActivity implements SwitchButton.OnCheckedChangeListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener, PostCallback {
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    private SwitchButton mSwitchButton1, mSwitchButton2, mSwitchButton3;
    private OrderVO mOrderVO;
    private TextView mNameTv, mPhoneTv, mAddressTv, mJinDouTv, mJinBiTv, mYuETv, mPriceTv, mJinDouNameTv, mJinBiNameTv, mOhterPriceTv, mYuENameTv;
    private LinearLayout mOtherLayout;
    private CheckBox mPayCb1, mPayCb2, mPayCb3;
    private Button mCommitBt;
    private RelativeLayout mAddressLayout;
    private String mType = "1";//1普通商品2一元购商品
    private String mAddressId;
    private String mPayType;
    private double mOtherPay = 0;
    private LinearLayout mPayLayout01, mPayLayout02, mPayLayout03;
    private boolean mIsPaying = false, mFistFlag = false;
    private Button mAddBt;
    View xuxian1, xuxian2;

    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.order_sure_activity);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initView() {
        mBaseTitleTv.setText("确认订单");
        xuxian1 = getViewById(R.id.xuxian_1);
        xuxian2 = getViewById(R.id.xuxian_2);
        xuxian1.setBackground(BaseCommonUtils.setBackgroundShap(this, 0, R.color.XJColor7, R.color.XJColor7, 1));
        xuxian2.setBackground(BaseCommonUtils.setBackgroundShap(this, 0, R.color.XJColor7, R.color.XJColor7, 1));
        mAddressLayout = getViewById(R.id.address_layout);

        mNameTv = getViewById(R.id.name_tv);
        mPhoneTv = getViewById(R.id.phone_tv);
        mAddressTv = getViewById(R.id.address_tv);
        mPriceTv = getViewById(R.id.price_tv);
        mJinDouTv = getViewById(R.id.jin_dou_tv);
        mJinDouNameTv = getViewById(R.id.jin_dou_name_tv);
        mJinBiTv = getViewById(R.id.jin_bi_tv);
        mJinBiNameTv = getViewById(R.id.jin_bi_name_tv);
        mYuETv = getViewById(R.id.yu_e_tv);
        mYuENameTv = getViewById(R.id.yu_e_name_tv);
        mOtherLayout = getViewById(R.id.other_layout);
        mOhterPriceTv = getViewById(R.id.other_price_tv);

        mSwitchButton1 = getViewById(R.id.switch_button1);
        mSwitchButton2 = getViewById(R.id.switch_button2);
        mSwitchButton3 = getViewById(R.id.switch_button3);
//        mSwitchButton.setChecked(true);
//        mSwitchButton1.isChecked();
//        mSwitchButton1.toggle();     //switch state
//        mSwitchButton.toggle(false);//switch without animation
//        mSwitchButton.setShadowEffect(true);//disable shadow effect
//        mSwitchButton1.setEnabled(false);//disable button

        mPayCb1 = getViewById(R.id.pay_cb_1);
        mPayCb2 = getViewById(R.id.pay_cb_2);
        mPayCb3 = getViewById(R.id.pay_cb_3);

        mCommitBt = getViewById(commit_bt);
        mCommitBt.setBackgroundDrawable(BaseCommonUtils.setBackgroundShap(this, 5, R.color.C7, R.color.C7));
        mPayLayout01 = getViewById(R.id.pay_layout_01);
        mPayLayout02 = getViewById(R.id.pay_layout_02);
        mPayLayout03 = getViewById(R.id.pay_layout_03);
        EventBus.getDefault().register(this);
        mAddBt = getViewById(R.id.address_bt);
        mAddBt.setBackgroundDrawable(BaseCommonUtils.setBackgroundShap(this, 5, R.color.C7, R.color.C7));
    }

    @Override
    public void bindViewsListener() {
        mOtherLayout.setOnClickListener(this);
        mSwitchButton1.setOnCheckedChangeListener(this);
        mSwitchButton2.setOnCheckedChangeListener(this);
        mSwitchButton3.setOnCheckedChangeListener(this);
        mPayCb1.setOnCheckedChangeListener(this);
        mPayCb2.setOnCheckedChangeListener(this);
        mPayCb3.setOnCheckedChangeListener(this);
        mAddressLayout.setOnClickListener(this);
        mCommitBt.setOnClickListener(this);
        mBaseBackTv.setOnClickListener(this);
        mAddBt.setOnClickListener(this);
    }

    @Override
    public void getData() {

        RequestParams params = new RequestParams();
        if (AppApplication.getInstance().getUserInfoVO() != null)
            params.put("key", AppApplication.getInstance().getUserInfoVO().getKey());
        params.put("ifcart", this.getIntent().getStringExtra("ifcart"));
        params.put("cart_info", this.getIntent().getStringExtra("cart_info"));
        params.put("address_id", mAddressId);
        params.put("all_remain", this.getIntent().getStringExtra("all_remain"));
        if ("2".equals(this.getIntent().getStringExtra("type"))) {
            String url = Constant.ONE_SHOP_ORDER_SURE_URL;
            HttpClientUtil.get(mActivity, mThreadUtil, url, params, OrderVO.class);
        } else if ("3".equals(this.getIntent().getStringExtra("type"))) {
            String url = Constant.GAME_ORDER_URL;
            HttpClientUtil.get(mActivity, mThreadUtil, url, params, OrderVO.class);
        } else {
            String url = Constant.ORDER_SURE_URL;
            HttpClientUtil.get(mActivity, mThreadUtil, url, params, OrderVO.class);
        }
    }

    @Override
    public void hasData(BaseVO vo) {
        mFistFlag = true;
        OrderVO orderVO = (OrderVO) vo;
        mOrderVO = orderVO.getDatas();
        if (mOrderVO.getAddress_info() != null) {
            mNameTv.setText(mOrderVO.getAddress_info().getTrue_name());
            mPhoneTv.setText(mOrderVO.getAddress_info().getMob_phone());
            mAddressTv.setText(mOrderVO.getAddress_info().getAddress());
            mAddressId = mOrderVO.getAddress_info().getAddress_id();
            mAddressLayout.setVisibility(View.VISIBLE);
            mAddBt.setVisibility(View.GONE);
            xuxian1.setVisibility(View.VISIBLE);
            xuxian2.setVisibility(View.VISIBLE);
        } else {

            if ("2".equals(this.getIntent().getStringExtra("type"))) {
                mAddressLayout.setVisibility(View.GONE);
                xuxian1.setVisibility(View.GONE);
                xuxian2.setVisibility(View.GONE);
                mAddBt.setVisibility(View.GONE);
            } else if ("3".equals(this.getIntent().getStringExtra("type"))) {
                mAddressLayout.setVisibility(View.GONE);
                xuxian1.setVisibility(View.GONE);
                xuxian2.setVisibility(View.GONE);
                mAddBt.setVisibility(View.GONE);
            } else {
                if (AppApplication.getInstance().getUserInfoVO() == null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "2");
                    mActivity.open(LoginActivity.class, bundle, 1);
                }
                mAddressLayout.setVisibility(View.GONE);
                xuxian1.setVisibility(View.GONE);
                xuxian2.setVisibility(View.GONE);
                mAddBt.setVisibility(View.VISIBLE);
            }
        }
//            if (!"3".equals(this.getIntent().getStringExtra("type"))) {
//
//                mAddressLayout.setVisibility(View.GONE);
//                xuxian1.setVisibility(View.GONE);
//                xuxian2.setVisibility(View.GONE);
//                mAddBt.setVisibility(View.VISIBLE);
//
//            } else {
//                if (AppApplication.getInstance().getUserInfoVO() == null) {
//                    Bundle bundle = new Bundle();
//                    bundle.putString("type", "2");
//                    mActivity.open(LoginActivity.class, bundle, 1);
//                }
//                mAddressLayout.setVisibility(View.GONE);
//                mAddBt.setVisibility(View.GONE);
//            }
//        }

        mPriceTv.setText(mOrderVO.getOrder_amount() + "元");
        mJinDouTv.setText(Html.fromHtml(mOrderVO.getBean_desc()));
        BaseCommonUtils.setTextTwoLast(this, mJinDouNameTv, "使用小鲸豆", "（可用小鲸豆" + mOrderVO.getAvailable_bean() + "个）", R.color.C5, 1.0f);
        mJinBiTv.setText(Html.fromHtml(mOrderVO.getGold_desc()));
        BaseCommonUtils.setTextTwoLast(this, mJinBiNameTv, "使用鲸币", "（可用鲸币" + mOrderVO.getAvailable_gold() + "个）", R.color.C5, 1.0f);
        mYuETv.setText(Html.fromHtml(mOrderVO.getPredeposit_desc()));
        BaseCommonUtils.setTextTwoLast(this, mYuENameTv, "使用余额", "（可用余额" + mOrderVO.getAvailable_predeposit() + "元）", R.color.C5, 1.0f);
        getRealMoney();

        if (Double.parseDouble(mOrderVO.getAvailable_bean())==0) {
            mSwitchButton1.setEnabled(false);
        }

        if (Double.parseDouble(mOrderVO.getAvailable_gold())==0) {
            mSwitchButton2.setEnabled(false);
        }

        if (Double.parseDouble(mOrderVO.getAvailable_predeposit())==0) {
            mSwitchButton3.setEnabled(false);

        }
    }

    @Override
    public void noData(BaseVO vo) {

    }

    @Override
    public void noNet() {

    }

    @Override
    public void onCheckedChanged(SwitchButton v, boolean b) {
        getRealMoney();
    }

    public void getRealMoney() {
        double allMoney = Double.parseDouble(mOrderVO.getOrder_amount());
        if (mSwitchButton1.isChecked()) {
            allMoney = NumberTypeUtil.sub(allMoney, Double.parseDouble(mOrderVO.getAvailable_bean_amount()));
            if (allMoney <= 0) {
                mOhterPriceTv.setText(0 + "元");
                setCheckEnable(false);
                setCheck();
                return;
            } else {
                setCheckEnable(true);
            }
        }
        if (mSwitchButton2.isChecked()) {
            allMoney = NumberTypeUtil.sub(allMoney, Double.parseDouble(mOrderVO.getAvailable_gold_amount()));
            if (allMoney <= 0) {
                mOhterPriceTv.setText(0 + "元");
                setCheckEnable(false);
                setCheck();
                return;
            } else {
                setCheckEnable(true);
            }
        }
        if (mSwitchButton3.isChecked()) {
            allMoney = NumberTypeUtil.sub(allMoney, Double.parseDouble(mOrderVO.getAvailable_predeposit_amount()));
            if (allMoney <= 0) {
                mOhterPriceTv.setText(0 + "元");
                setCheckEnable(false);
                setCheck();
                return;
            } else {
                setCheckEnable(true);
            }
        }
        if (allMoney > 0) {
            setCheckEnable(true);
        }
        mOhterPriceTv.setText(allMoney + "元");

        for (int i = 0; i < mOrderVO.getPayment_list().size(); i++) {
            if ("alipay_app".equals(mOrderVO.getPayment_list().get(i).getPayment_code())) {
                mPayLayout01.setVisibility(View.VISIBLE);
            } else if ("wxpay_app".equals(mOrderVO.getPayment_list().get(i).getPayment_code())) {
                mPayLayout02.setVisibility(View.VISIBLE);
            } else if ("99bill_app".equals(mOrderVO.getPayment_list().get(i).getPayment_code())) {
                mPayLayout03.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setCheckEnable(boolean enable) {
        mPayCb1.setEnabled(enable);
        mPayCb2.setEnabled(enable);
        mPayCb3.setEnabled(enable);
    }

    public void setCheck() {
        mPayCb1.setChecked(false);
        mPayCb2.setChecked(false);
        mPayCb3.setChecked(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.address_bt:
                if (mOrderVO.getAddress_info() == null && !"3".equals(this.getIntent().getStringExtra("type'"))) {
                    openActivity(AddressAddActivity.class);
                } else {
                    openActivity(AddressSelectActivity.class);
                }
                break;
            case R.id.address_layout:
                if (mOrderVO.getAddress_info() == null && !"3".equals(this.getIntent().getStringExtra("type'"))) {
                    openActivity(AddressAddActivity.class);
                } else {
                    openActivity(AddressSelectActivity.class);
                }

                break;
            case R.id.commit_bt:
                if (Double.parseDouble(mOhterPriceTv.getText().toString().split("元")[0]) > 0 && !mPayCb1.isChecked() && !mPayCb2.isChecked() && !mPayCb3.isChecked()) {
                    showCustomToast("请选择支付方式");
                    return;
                }

                if (!mSwitchButton1.isChecked() && !mSwitchButton2.isChecked() && !mSwitchButton3.isChecked()) {
                    commit();
                    return;
                }

                if ("1".equals(mOrderVO.getPaypwd_state())) {
                    SercurityDialog dialog = new SercurityDialog(mActivity);
                    dialog.setOnInputCompleteListener(new SercurityDialog.InputCompleteListener() {
                        @Override
                        public void inputComplete(String password) {
                            checkPayPassword(password.replaceAll(",", ""));
                        }
                    });
                    dialog.show();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("url", mOrderVO.getSet_paypwd_url());
                    open(WebViewActivity.class, bundle, 0);
                }
                break;
            case R.id.base_back_tv:
                EventBus.getDefault().post(new EBMessageVO("back_webview"));
                this.finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton v, boolean b) {
        switch (v.getId()) {
            case R.id.pay_cb_1:
                if (b) {
//                    mPayType = mOrderVO.getPayment_list().get(1).getPayment_code();
                    mPayCb2.setChecked(false);
                    mPayCb3.setChecked(false);
                }

                break;
            case R.id.pay_cb_2:
                if (b) {
//                    mPayType = mOrderVO.getPayment_list().get(0).getPayment_code();
                    mPayCb1.setChecked(false);
                    mPayCb3.setChecked(false);
                }
                break;
            case R.id.pay_cb_3:
                if (b) {
//                    mPayType = mOrderVO.getPayment_list().get(2).getPayment_code();
                    mPayCb2.setChecked(false);
                    mPayCb1.setChecked(false);
                }
                break;

            default:
                break;
        }

        if(mPayCb1.isChecked()){
            mPayType = mOrderVO.getPayment_list().get(1).getPayment_code();
        }else if(mPayCb2.isChecked()){
            mPayType = mOrderVO.getPayment_list().get(0).getPayment_code();
        }else if(mPayCb3.isChecked()) {
            mPayType = mOrderVO.getPayment_list().get(2).getPayment_code();
        }else{
            mPayType = "";
        }
    }

    public void commit() {
        RequestParams params = new RequestParams();
        params.put("ifcart", this.getIntent().getStringExtra("ifcart"));
        params.put("cart_info", this.getIntent().getStringExtra("cart_info"));
        params.put("address_id", mAddressId);
        params.put("bean_pay", mSwitchButton1.isChecked() ? "1" : "0");
        params.put("gold_pay", mSwitchButton2.isChecked() ? "1" : "0");
        params.put("pd_pay", mSwitchButton3.isChecked() ? "1" : "0");
        params.put("payment_code", mPayType);
        params.put("all_remain", this.getIntent().getStringExtra("all_remain"));
        params.put("game_userid", this.getIntent().getStringExtra("game_userid"));

        if (AppApplication.getInstance().getUserInfoVO() != null)
            params.put("key", AppApplication.getInstance().getUserInfoVO().getKey());

        String allUrl = "";
        if ("2".equals(this.getIntent().getStringExtra("type"))) {
            //一元购
            allUrl = BaseConstant.DOMAIN_NAME + Constant.ONE_SHOP_ORDER_PAY_URL;
        } else if ("3".equals(this.getIntent().getStringExtra("type"))) {
            //游戏
            allUrl = BaseConstant.DOMAIN_NAME + Constant.GAME_ORDER_PAY_URL;
        } else {
            //普通商品
            allUrl = BaseConstant.DOMAIN_NAME + Constant.ORDER_PAY_URL;
        }

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(allUrl, params, new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                dismissProgressDialog();
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                try {
                    dismissProgressDialog();
                    String result = new String(arg2);
                    JSONObject jsonObject = new JSONObject(result);
                    String code = (String) jsonObject.get("code");
                    if ("200".equals(code)) {
                        JSONObject data = jsonObject.getJSONObject("datas");
                        final String payInfo = (String) data.get("orderString");
                        final String payState = (String) data.get("order_payment_state");
                        if ("1".equals(payState) && !"".equals(payState)) {
                            payFinish();
                        } else {
                            if (!"".equals(payInfo)) {
                                if ("99bill_app".equals(mPayType)) {
                                    mIsPaying = true;
                                    Bundle bundle = new Bundle();
                                    bundle.putString("url", payInfo);
                                    bundle.putString("title", "快钱支付");
                                    mActivity.open(WebViewActivity.class, bundle, 0);
                                } else if ("alipay_app".equals(mPayType)) {
                                    mIsPaying = true;
                                    PayUtis.zhiFuBaoPay(OrderSureActivity.this, payInfo, new PayCallback() {
                                        @Override
                                        public void payResult(int type) {
                                            mIsPaying = false;
                                            if (type == 1) {
                                                payFinish();
                                            } else {
                                                showCustomToast("支付失败");
                                            }
                                        }
                                    });
                                }
                            } else {
                                Gson gson = new Gson();
                                final PayResult vo = gson.fromJson(result, PayResult.class);
                                if (vo.getDatas().getWxpay_param() != null) {
                                    PayUtis.weiXinPay(OrderSureActivity.this, vo.getDatas().getWxpay_param());
                                }
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EBMessageVO event) {
        if ("order".equals(event.getMessage())) {
            String[] address = event.getParams();
            mNameTv.setText(address[0]);
            mPhoneTv.setText(address[1]);
            mAddressTv.setText(address[2]);
            mAddressId = address[3];
            mAddressLayout.setVisibility(View.VISIBLE);
            mAddBt.setVisibility(View.GONE);
            xuxian1.setVisibility(View.VISIBLE);
            xuxian2.setVisibility(View.VISIBLE);
        }

        if ("kuaiqian_pay".equals(event.getMessage())) {
            payFinish();
        }

        if ("weixin_pay".equals(event.getMessage())) {
            payFinish();
        }
    }

    public void checkPayPassword(String pwd) {
        showProgressDialog();
        RequestParams paramsMap = new RequestParams();
        String mUrl = Constant.CHECK_PAY_PASSWROD_URL;
        if (AppApplication.getInstance().getUserInfoVO() != null)
            paramsMap.put("key", AppApplication.getInstance().getUserInfoVO().getKey());
        paramsMap.put("password", pwd);
        HttpClientUtil.post(mActivity, mActivity, mUrl, paramsMap, null, this);

    }

    @Override
    public void success(BaseVO vo) {
        if (BaseConstant.RESULT_SUCCESS_CODE.equals(vo.getCode())) {
            commit();
        } else {
            showCustomToast(vo.getError());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mIsPaying && mFistFlag) {
            mThreadUtil = new ThreadUtil(mActivity, OrderSureActivity.this);
            mThreadUtil.start();

        }
    }

    public void payFinish() {
        showCustomToast("支付成功");
        mCommitBt.postDelayed(new Runnable() {
            @Override
            public void run() {
                Bundle bundle = new Bundle();
                if ("2".equals(OrderSureActivity.this.getIntent().getStringExtra("type"))) {
                    mActivity.openActivity(OneShopOrderActivity.class);
                } else if ("3".equals(OrderSureActivity.this.getIntent().getStringExtra("type"))) {
                    mActivity.openActivity(GameOrderListActivity.class);
                } else {
                    bundle.putString("type", "2");
                    mActivity.open(OrderActivity.class, bundle, 0);
                    EventBus.getDefault().post(new EBMessageVO("back_webview"));
                }
                OrderSureActivity.this.finish();
            }


        }, 1000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EventBus.getDefault().post(new EBMessageVO("back_webview"));
        this.finish();
    }
}
