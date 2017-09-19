package com.beisheng.easycar.activity;

import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.beisheng.easycar.R;
import com.beisheng.easycar.application.AppApplication;
import com.beisheng.easycar.constant.Constant;
import com.beisheng.easycar.mode.CarVO;
import com.loopj.android.http.RequestParams;
import com.rey.material.widget.Button;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.utils.BaseCommonUtils;

public class BackCarActivity extends BaseActivity {
    private TextView mMoneyType01Tv, mMoneyType02Tv, mMoney01Tv, mMoney02Tv, mMoney03Tv, mMoney04Tv,mPayType01Tv,mPayType02Tv,mPayType03Tv,mNeedPay01Tv,mNeedPay02Tv,mAllMoneyTv;
    private CarVO mCarVO;
    private Button mOkBt;

    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.activity_back_car);
    }

    @Override
    public void initView() {
        mBaseTitleTv.setText("确认还车");
        mBaseHeadLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.Car2));

        mMoneyType01Tv = getViewById(R.id.money_type_01_tv);
        mMoneyType02Tv = getViewById(R.id.money_type_02_tv);
        mMoney01Tv = getViewById(R.id.money_01_tv);
        mMoney02Tv = getViewById(R.id.money_02_tv);
        mMoney03Tv = getViewById(R.id.money_03_tv);
        mMoney04Tv = getViewById(R.id.money_04_tv);
        mPayType01Tv=getViewById(R.id.pay_type_01_tv);
        mPayType02Tv=getViewById(R.id.pay_type_02_tv);
        mPayType03Tv=getViewById(R.id.pay_type_03_tv);
        mNeedPay01Tv=getViewById(R.id.need_pay_01_tv);
        mNeedPay02Tv=getViewById(R.id.need_pay_02_tv);
        mAllMoneyTv=getViewById(R.id.all_money_tv);
        mOkBt = getViewById(R.id.ok_bt);
        mOkBt.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.Car2, R.color.Car2));
    }

    @Override
    public void bindViewsListener() {

    }

    @Override
    public void getData() {
        HttpClientUtil.show(mThreadUtil);
        RequestParams params = new RequestParams();
        if (AppApplication.getInstance().getUserInfoVO() != null)
            params.put("uin", AppApplication.getInstance().getUserInfoVO().getUin());
        params.put("orderid", this.getIntent().getStringExtra("orderid"));
        String urlList = Constant.BACK_CAR_URL;
        HttpClientUtil.get(mActivity, mThreadUtil, urlList, params, CarVO.class);
    }

    @Override
    public void hasData(BaseVO vo) {
        CarVO carVO = (CarVO) vo;
        mCarVO = ((CarVO) vo).getData();
        BaseCommonUtils.setTextThree(this, mMoneyType01Tv, "里程费用 (", mCarVO.getKm_cost(), "/公里)", R.color.Car1, 1.3f);
        BaseCommonUtils.setTextThree(this, mMoneyType02Tv, "时间费用 (", mCarVO.getKm_cost(), "/分钟)", R.color.Car1, 1.3f);
        mMoney01Tv.setText("￥" + mCarVO.getKm_fee());
        mMoney02Tv.setText("￥" + mCarVO.getTime_fee());
        mMoney03Tv.setText("￥" + mCarVO.getYidi_cost());
        mMoney04Tv.setText("￥" + mCarVO.getNo_cost());
        mNeedPay01Tv.setText("￥" +mCarVO.getMoney());
        mNeedPay02Tv.setText("￥" +mCarVO.getMoney());
        mAllMoneyTv.setText(mCarVO.getAll_cost());
        mPayType01Tv.setText(mCarVO.getCoupon_num());

    }

    @Override
    public void noData(BaseVO vo) {

    }

    @Override
    public void noNet() {

    }
}
