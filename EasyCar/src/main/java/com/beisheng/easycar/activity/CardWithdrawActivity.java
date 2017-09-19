package com.beisheng.easycar.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beisheng.easycar.R;
import com.beisheng.easycar.application.AppApplication;
import com.beisheng.easycar.constant.Constant;
import com.beisheng.easycar.mode.PayTypeVO;
import com.beisheng.easycar.view.SercurityDialog;
import com.loopj.android.http.RequestParams;
import com.rey.material.widget.Button;
import com.squareup.picasso.Picasso;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.constant.BaseConstant;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.interfaces.PostCallback;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.mode.EBMessageVO;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.wuzhanglong.library.utils.ThreadUtil;

import org.greenrobot.eventbus.EventBus;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CardWithdrawActivity extends BaseActivity implements View.OnClickListener, PostCallback {
    private TextView mNameTv, mDescTv, mOverTv, mAllMoneyTv;
    private ImageView mImg;
    private Button mCommitBt;
    private LinearLayout mBlankLayout;
    private String mBlankName = "", mBlankCode, mBlankImg, mCardNo, mCardId;
    private int mPostType = 1;
    private PayTypeVO mPayTypeVO, mPayPasswordVO;
    private EditText mMoneyEt;


    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.card_withdraw_activity);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initView() {
        mBaseTitleTv.setText("提现");
        mNameTv = getViewById(R.id.name_tv);
        mDescTv = getViewById(R.id.desc_tv);
        mImg = getViewById(R.id.img);
        mOverTv = getViewById(R.id.over_tv);
        mAllMoneyTv = getViewById(R.id.all_money_tv);
        mMoneyEt = getViewById(R.id.money_et);

        Intent intent = this.getIntent();
        mPayTypeVO = (PayTypeVO) intent.getSerializableExtra("card_info");
        mCardId = mPayTypeVO.getCard_id();
        mOverTv.setText("可用余额" + intent.getStringExtra("over") + "元");
        mNameTv.setText(mPayTypeVO.getBank_name());
        mDescTv.setText("尾号" + mPayTypeVO.getCard_no() + "储蓄卡");
        Picasso.with(this).load(mPayTypeVO.getBank_image()).into(mImg);
        mCommitBt = getViewById(R.id.commit_bt);
        mCommitBt.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.C7, R.color.C7));
        mBlankLayout = getViewById(R.id.blank_layout);
    }

    @Override
    public void bindViewsListener() {
        mBlankLayout.setOnClickListener(this);
        mCommitBt.setOnClickListener(this);
        mAllMoneyTv.setOnClickListener(this);
    }

    @Override
    public void getData() {
        RequestParams paramsMap = new RequestParams();
//        String url = Constant.CHECK_PAY_PASSWORD_URL;
        if (AppApplication.getInstance().getUserInfoVO() != null)
            paramsMap.put("uin", AppApplication.getInstance().getUserInfoVO().getUin());
//        HttpClientUtil.get(mActivity, mThreadUtil, url, paramsMap, PayTypeVO.class);
    }

    @Override
    public void hasData(BaseVO vo) {
        PayTypeVO payTypeVO = (PayTypeVO) vo;
        mPayPasswordVO = payTypeVO.getDatas();
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
            case R.id.blank_layout:
                openActivityForResult(CardShowActivity.class, 1);
                break;
            case R.id.commit_bt:

                if (Double.parseDouble(mMoneyEt.getText().toString()) > Double.parseDouble(this.getIntent().getStringExtra("over"))) {
                    showCustomToast("余额不足");
                    return;
                }

                if ("1".equals(mPayPasswordVO.getPaypwd_state())) {

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
                    bundle.putString("url", mPayTypeVO.getSet_paypwd_url());
//                    open(WebViewActivity.class, bundle, 0);
                }


                break;

            case R.id.all_money_tv:
                if (mMoneyEt.getText().toString().equals(this.getIntent().getStringExtra("over"))) {
                    mMoneyEt.setText("0");
                    mAllMoneyTv.setTextColor(ContextCompat.getColor(this, R.color.C5));
                } else {
                    mMoneyEt.setText(this.getIntent().getStringExtra("over"));
                    mAllMoneyTv.setTextColor(ContextCompat.getColor(this, R.color.C7));
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)
            return;
        switch (requestCode) {
            case 1:
                mBlankName = data.getStringExtra("name");
                mBlankCode = data.getStringExtra("code");
                mBlankImg = data.getStringExtra("img");
                mCardNo = data.getStringExtra("no");
                mCardId = data.getStringExtra("id");
                mNameTv.setText(mBlankName);
                mDescTv.setText("尾号" + mCardNo + "储蓄卡");
                Picasso.with(this).load(mBlankImg).into(mImg);
                break;
            default:
                break;
        }
    }

    public void checkPayPassword(String pwd) {
        mPostType = 1;
        showProgressDialog();
        RequestParams paramsMap = new RequestParams();
//        String mUrl = Constant.CHECK_PAY_PASSWROD_URL;
        if (AppApplication.getInstance().getUserInfoVO() != null)
            paramsMap.put("uin", AppApplication.getInstance().getUserInfoVO().getUin());
        paramsMap.put("password", pwd);
//        HttpClientUtil.post(mActivity, mActivity, mUrl, paramsMap, null, this);

    }

    public void withdraw() {
        mPostType = 2;
        showProgressDialog();
        RequestParams paramsMap = new RequestParams();
//        String mUrl = Constant.WITHDRAW_URL;
        if (AppApplication.getInstance().getUserInfoVO() != null)
            paramsMap.put("uin", AppApplication.getInstance().getUserInfoVO().getUin());
        paramsMap.put("pdc_amount", mMoneyEt.getText().toString());
        paramsMap.put("card_id", mCardId);
//        HttpClientUtil.post(mActivity, mActivity, mUrl, paramsMap, null, this);
    }

    @Override
    public void success(BaseVO vo) {
        if (BaseConstant.RESULT_SUCCESS_CODE.equals(vo.getCode())) {
            if (mPostType == 1) {
                withdraw();
            } else {
//                showCustomToast("提现成功");

                new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("恭喜您提现成功")
                        .setContentText("系统将在1-7个工作日内将退款返还到您的账户")
                        .show();
                EventBus.getDefault().post(new EBMessageVO("withdraw"));
            }
        } else {
            showCustomToast(vo.getError());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mThreadUtil = new ThreadUtil(mActivity, this);
        mThreadUtil.start();
    }
}
