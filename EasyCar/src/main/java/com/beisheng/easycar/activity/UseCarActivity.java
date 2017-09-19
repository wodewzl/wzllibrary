package com.beisheng.easycar.activity;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beisheng.easycar.R;
import com.beisheng.easycar.application.AppApplication;
import com.beisheng.easycar.constant.Constant;
import com.beisheng.easycar.mode.CarVO;
import com.beisheng.easycar.mode.UserInfoVO;
import com.loopj.android.http.RequestParams;
import com.rey.material.widget.CheckBox;
import com.squareup.picasso.Picasso;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.constant.BaseConstant;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.interfaces.PostCallback;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.mode.EBMessageVO;
import com.wuzhanglong.library.utils.BaseCommonUtils;

import org.greenrobot.eventbus.EventBus;

public class UseCarActivity extends BaseActivity implements PostCallback, View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private CarVO mCarVO;
    private TextView mTv01, mTv02, mTv03, mTv04, mTv05, mTv06, mTv07, mTv08;
    private ImageView mCarImg, mBatteryImg;
    private com.rey.material.widget.TextView mOkBt;
    private CheckBox mCheck01, mCheck02, mCheck03;
    private ProgressBar mProgressBar;

    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.activity_use_car);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initView() {
        mBaseHeadLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.Car2));
        mBaseTitleTv.setText("确认用车");
        mTv01 = getViewById(R.id.tv_01);
        mTv02 = getViewById(R.id.tv_02);
        mTv03 = getViewById(R.id.tv_03);
        mTv04 = getViewById(R.id.tv_04);
        mTv05 = getViewById(R.id.tv_05);
        mCarImg = getViewById(R.id.car_img);
        mTv06 = getViewById(R.id.tv_06);
        mTv07 = getViewById(R.id.tv_07);
        mTv08 = getViewById(R.id.tv_08);
        mBatteryImg = getViewById(R.id.battery_img);

        mOkBt = getViewById(R.id.ok_bt);
        mOkBt.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.Car2, R.color.Car2));
        mCheck01 = getViewById(R.id.check_01);
        mCheck02 = getViewById(R.id.check_02);
        mCheck03 = getViewById(R.id.check_03);
        mCheck03.setChecked(true);
        mProgressBar = getViewById(R.id.progress_bar);
    }

    @Override
    public void bindViewsListener() {
        mOkBt.setOnClickListener(this);
        mCheck03.setOnCheckedChangeListener(this);
    }

    @Override
    public void getData() {
        RequestParams params = new RequestParams();
        String url = Constant.USER_CAR_URL;
        if (AppApplication.getInstance().getUserInfoVO() != null)
            params.put("key", AppApplication.getInstance().getUserInfoVO().getUin());
        params.put("carid", this.getIntent().getStringExtra("id"));
        HttpClientUtil.get(mActivity, mThreadUtil, url, params, CarVO.class);
    }

    @Override
    public void hasData(BaseVO vo) {
        CarVO carVO = (CarVO) vo;
        mCarVO = carVO.getData();
        Picasso.with(this).load(mCarVO.getPic()).into(mCarImg);
        mTv01.setText(mCarVO.getNumber());
        mTv02.setText(mCarVO.getTitle() + " | " + mCarVO.getSeating());
        BaseCommonUtils.setTextTwoBefore(this, mTv03, mCarVO.getBattery(), mCarVO.getKm(), R.color.Car3, 1.23f);
        BaseCommonUtils.setTextThree(this, mTv04, "时长 (", mCarVO.getTime_cost(), "元/分钟)", R.color.Car1, 1.23f);
        BaseCommonUtils.setTextThree(this, mTv05, "+ 里程 (", mCarVO.getKm_cost(), "元/公里)", R.color.Car1, 1.23f);
        mTv06.setText(mCarVO.getYidi_cost());
        BaseCommonUtils.setTextThree(this, mTv07, "不计免赔 (", mCarVO.getNo_cost(), ")", R.color.Car1, 1.23f);
        BaseCommonUtils.setTextThree(this, mTv08, "乘客座位保障 (", mCarVO.getEnsure_cost(), ")", R.color.Car1, 1.23f);

        if(BaseCommonUtils.parseInt(mCarVO.getBattery().split("%")[0])>30){
            mBatteryImg.setColorFilter(ContextCompat.getColor(this, R.color.Car9));
            mProgressBar.setProgressDrawable(ContextCompat.getDrawable(this,R.drawable.progressbar_horizontal_green));
        }else{
            mBatteryImg.setColorFilter(ContextCompat.getColor(this, R.color.Car1));
            mProgressBar.setProgressDrawable(ContextCompat.getDrawable(this,R.drawable.progressbar_horizontal_red));
        }
        mProgressBar.setProgress(BaseCommonUtils.parseInt(mCarVO.getBattery().split("%")[0]));


    }

    @Override
    public void noData(BaseVO vo) {

    }

    @Override
    public void noNet() {

    }


    public void checkUserInfo() {
        final RequestParams params = new RequestParams();
        if (AppApplication.getInstance().getUserInfoVO() != null)
            params.put("uin", AppApplication.getInstance().getUserInfoVO().getUin());
        String mUrl = Constant.USER_INFO_URL;
        HttpClientUtil.post(mActivity, this, mUrl, params, UserInfoVO.class, this);
    }

    @Override
    public void success(BaseVO vo) {
        dismissProgressDialog();

        if( vo instanceof  UserInfoVO){
            UserInfoVO userInfoVO = (UserInfoVO) vo;
            AppApplication.getInstance().saveUserInfoVO(userInfoVO.getData());
            String userStatus = userInfoVO.getData().getUser_status();
            String moneyStatus = userInfoVO.getData().getMoney_status();
            if ("0".equals(userStatus)) {
                openActivity(ApproveNameActivity.class);
            } else if ("1".equals(userStatus)) {
                showCustomToast("正在审核中，不能租车");
            } else if ("2".equals(userStatus)) {
                openActivity(ApproveNameActivity.class);
            } else {

                if("1".equals(moneyStatus)){
                    userCar();
                }else{
                    openActivity(MyDepositActivity.class);
                }

            }
        }else if(vo instanceof CarVO){
            CarVO carVO= (CarVO) vo;
            if(BaseConstant.RESULT_SUCCESS_CODE.equals(vo.getCode())){
                EBMessageVO ebMessageVO=new EBMessageVO("user_car");
                String[] params ={carVO.getData().getOrderid()};
                ebMessageVO.setParams(params);
                EventBus.getDefault().post(ebMessageVO);
                this.showCustomToast("用车成功");
                mBaseHeadLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        UseCarActivity.this.finish();
                    }
                },1000);
            }else {

            }

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok_bt:
                if (!mCheck03.isChecked()) {
                    showCustomToast("请同意订单说明");
                    return;
                }
                checkUserInfo();
                break;
            default:
                break;
        }
    }

    public void userCar() {
        final RequestParams params = new RequestParams();
        if (AppApplication.getInstance().getUserInfoVO() != null)
            params.put("uin", AppApplication.getInstance().getUserInfoVO().getUin());
        params.put("carid", this.getIntent().getStringExtra("id"));
        params.put("noDeductible", mCheck01.isChecked() ? "1" : "0");
        params.put("seatEnsure", mCheck01.isChecked() ? "1" : "0");
        String mUrl = Constant.USE_CAR_URL;
        HttpClientUtil.post(mActivity, this, mUrl, params, CarVO.class, this);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            mOkBt.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.Car2, R.color.Car2));

        } else {
            mOkBt.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.C6, R.color.C6));

        }
    }
}
