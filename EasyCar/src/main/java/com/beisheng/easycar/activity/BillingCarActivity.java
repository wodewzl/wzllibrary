package com.beisheng.easycar.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beisheng.easycar.R;
import com.beisheng.easycar.application.AppApplication;
import com.beisheng.easycar.constant.Constant;
import com.beisheng.easycar.mode.CarVO;
import com.beisheng.easycar.mode.NeaybyVO;
import com.loopj.android.http.RequestParams;
import com.rey.material.widget.Button;
import com.rey.material.widget.CheckBox;
import com.squareup.picasso.Picasso;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.constant.BaseConstant;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.interfaces.PostCallback;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.mode.EBMessageVO;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.wuzhanglong.library.utils.MapUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class BillingCarActivity extends BaseActivity implements View.OnClickListener, PostCallback, CompoundButton.OnCheckedChangeListener {
    private TextView mNumberTv, mKmTv, mTimeTv, mDdistanceTv, mMoneyType01Tv, mMoneyType02Tv, mMoneyType03Tv, mMoneyType04Tv, mMoney01Tv, mMoney02Tv, mMoney03Tv, mMoney04Tv;
    private CarVO mCarVO;
    private ImageView mCarImg;
    private Button mOkBt;
    private CheckBox mCheck01, mCheck02;
    private String mBackStatus = "1";
    private TextView mLeadTv, mBackTv;
    private RelativeLayout mBackLayout;
    private TextView mPositionTv,mDetailTv;
    private LinearLayout mLeadLayout;
    private NeaybyVO mNeaybyVO;

    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.activity_billing_car);
    }

    @Override
    public void initView() {
        mBaseTitleTv.setText("用车计费");
        mBaseHeadLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.Car2));
        mNumberTv = getViewById(R.id.number_tv);
        mKmTv = getViewById(R.id.km_tv);
        mTimeTv = getViewById(R.id.time_tv);
        mDdistanceTv = getViewById(R.id.distance_tv);
        mMoneyType01Tv = getViewById(R.id.money_type_01_tv);
        mMoneyType02Tv = getViewById(R.id.money_type_02_tv);
        mMoneyType03Tv = getViewById(R.id.money_type_03_tv);
        mMoneyType04Tv = getViewById(R.id.money_type_04_tv);
        mMoney01Tv = getViewById(R.id.money_01_tv);
        mMoney02Tv = getViewById(R.id.money_02_tv);
        mMoney03Tv = getViewById(R.id.money_03_tv);
        mMoney04Tv = getViewById(R.id.money_04_tv);
        mCarImg = getViewById(R.id.car_img);
        mOkBt = getViewById(R.id.ok_bt);
        mOkBt.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.Car2, R.color.Car2));
        mCheck01 = getViewById(R.id.check_01);
        mCheck02 = getViewById(R.id.check_02);
        mLeadLayout = getViewById(R.id.lead_layout);
        mBackLayout = getViewById(R.id.back_layout);
        mPositionTv=getViewById(R.id.position_tv);
        mDetailTv=getViewById(R.id.detail_tv);
        mLeadLayout=getViewById(R.id.lead_layout);
    }

    @Override
    public void bindViewsListener() {
        mCheck01.setOnCheckedChangeListener(this);
        mCheck02.setOnCheckedChangeListener(this);
        mOkBt.setOnClickListener(this);
        mBackLayout.setOnClickListener(this);
        EventBus.getDefault().register(this);
        mLeadLayout.setOnClickListener(this);
    }

    @Override
    public void getData() {
//        HttpClientUtil.show(mThreadUtil);


        RequestParams params = new RequestParams();
        String url = Constant.COST_CAR_URL;
        if (AppApplication.getInstance().getUserInfoVO() != null)
            params.put("uin", AppApplication.getInstance().getUserInfoVO().getUin());
        params.put("orderid", this.getIntent().getStringExtra("orderid"));
        HttpClientUtil.get(mActivity, mThreadUtil, url, params, CarVO.class);
    }

    @Override
    public void hasData(BaseVO vo) {
        CarVO carVO = (CarVO) vo;
        mCarVO = ((CarVO) vo).getData();
        Picasso.with(this).load(mCarVO.getPic()).into(mCarImg);
        mNumberTv.setText(mCarVO.getNumber());
        mKmTv.setText(mCarVO.getKm());
        mTimeTv.setText(mCarVO.getTime() + "分钟");
        mDdistanceTv.setText(mCarVO.getDistance() + "公里");
        BaseCommonUtils.setTextThree(this, mMoneyType01Tv, "里程费用 (", mCarVO.getKm_cost(), "/公里)", R.color.Car1, 1.3f);
        BaseCommonUtils.setTextThree(this, mMoneyType02Tv, "时间费用 (", mCarVO.getKm_cost(), "/分钟)", R.color.Car1, 1.3f);
        mMoney01Tv.setText("￥" + mCarVO.getKm_fee());
        mMoney02Tv.setText("￥" + mCarVO.getTime_fee());
        mMoney03Tv.setText("￥" + mCarVO.getYidi_cost());
        mMoney04Tv.setText("￥" + mCarVO.getNo_cost());
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
            case R.id.ok_bt:
                //正常逻辑 打开下面的  backCar() 关闭 dialog 逻辑已处理好
                if(mNeaybyVO==null){
                    showCustomToast("请选择还车点");
                    return;
                }
                backCar();
//                new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
//                        .setTitleText("友情提示")
//                        .setContentText("当前位置不在还车范围，停车将收取异地还车费用")
//                        .setConfirmText("确定")
//                        .setCancelText("取消")
//                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sDialog) {
//                                Bundle bundle = new Bundle();
//                                bundle.putString("orderid", BillingCarActivity.this.getIntent().getStringExtra("orderid"));
//                                open(BackCarActivity.class, bundle, 0);
//                            }
//                        })
//                        .show();
                break;
            case R.id.back_layout:
                openActivity(NearbyActivity.class);
                break;

            case R.id.lead_layout:
                MapUtil.guide(this, mNeaybyVO.getLat(), mNeaybyVO.getLng(), mNeaybyVO.getAddress());
                break;
            default:
                break;
        }
    }

    public void backCar() {
        RequestParams params = new RequestParams();
        String url = Constant.SURE_BACK_CAR_URL;
        if (AppApplication.getInstance().getUserInfoVO() != null)
            params.put("uin", AppApplication.getInstance().getUserInfoVO().getUin());
        params.put("orderid", this.getIntent().getStringExtra("orderid"));
        params.put("yidi", mCheck01.isChecked() ? "1" : "2");
        params.put("shopid", mNeaybyVO.getId());
        HttpClientUtil.post(mActivity, this, url, params, CarVO.class, this);
    }

    @Override
    public void success(BaseVO vo) {
        CarVO carVO = (CarVO) vo;

        if ("1".equals(carVO.getData().getStatus())) {
            Bundle bundle = new Bundle();
            bundle.putString("orderid", this.getIntent().getStringExtra("orderid"));
            open(BackCarActivity.class, bundle, 0);

        } else {
            new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                    .setTitleText("友情提示")
                    .setContentText("当前位置不在还车范围，停车将收取异地还车费用")
                    .setConfirmText("确定")
                    .setCancelText("取消")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();//直接消失
                            Bundle bundle = new Bundle();
                            bundle.putString("orderid", BillingCarActivity.this.getIntent().getStringExtra("orderid"));
                            open(BackCarActivity.class, bundle, 0);
                        }
                    })
                    .show();
        }


        if (BaseConstant.RESULT_SUCCESS_CODE.equals(carVO.getCode())) {


        }
        showCustomToast(carVO.getInfo());

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        if (compoundButton.getId() == R.id.check_01) {
            if (b)
                mCheck02.setChecked(false);
        } else if (compoundButton.getId() == R.id.check_02) {
            if (b)
                mCheck01.setChecked(false);
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EBMessageVO event) {
        if ("back_car_address".equals(event.getMessage())) {
            mNeaybyVO= (NeaybyVO) event.getObject();
            mPositionTv.setText(mNeaybyVO.getName());
            mDetailTv.setText(mNeaybyVO.getAddress());

        }
    }
}
