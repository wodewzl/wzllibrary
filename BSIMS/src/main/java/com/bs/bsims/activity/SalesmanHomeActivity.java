/**
 * 
 */

package com.bs.bsims.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.model.SalePersonInfo;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSCircleImageView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * BS北盛最帅程序员 Copyright (c) 2016 湖北北盛科技有限公司
 * 
 * @author 梁骚侠
 * @date 2016-5-20
 * @version 2.0
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class SalesmanHomeActivity extends BaseActivity implements OnClickListener {

    private SalePersonInfo salePersonInfo, salePersonInfoVo, saleMonthVo;

    private TextView mProgressV1, mProgressV2, mProgressV3, mProgressV4, mProgressV5;
    private Context mContext;
    private BSCircleImageView bsCircleImageView;
    private TextView mSaleName, mPdName, mChangeBut;
    private TextView mCcount, mVcount, mRcount;
    private ImageButton thisMonthBt, lastMonthBt;
    private TextView mPcotent1, mPcotent2, mPcotent3, mPcotent4, mPcotent5;
    private TextView gridCustomer, gridContact, gridBusiness, gridTrade, gridVisitor;
    private int mProgressV1Width;
    private int mMaxWidth;
    private TextView mCountPb1bg, mCountPb2bg, mCountPb3bg, mCountPb4bg, mCountPb5bg;
    private String uid;
    private String type = "2";//(原来是本月 上个月) 现在改成月份的切换
    private LinearLayout mMonthLayout;
    private TextView mMonthTv;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.salesman_homepage, mContentLayout);
        baseHeadLayout.setBackgroundColor(Color.TRANSPARENT);
        mContext = this;
    }

    @Override
    public boolean getDataResult() {
        // TODO Auto-generated method stub
        return getData();
    }

    @Override
    public void updateUi() {
        CustomDialog.closeProgressDialog();
        baseHeadLayout.setBackgroundColor(Color.parseColor("#2A76D4"));
        salePersonInfoVo = salePersonInfo.getInfo();
        ImageLoader.getInstance().displayImage(salePersonInfoVo.getHeadpic(), bsCircleImageView, CommonUtils.initImageLoaderOptions());
        mSaleName.setText(salePersonInfoVo.getFullname());
        if (salePersonInfoVo.getSex().equals(Constant.SEX_MAN)) {
            mSaleName.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.person_man), null);
        }
        else {
            mSaleName.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.person_woman), null);
        }
        mPdName.setText(salePersonInfoVo.getDname() + "/" + salePersonInfoVo.getPname());
        mCcount.setText(salePersonInfoVo.getCustomerTotal() + "\n客户总量");
        mVcount.setText(salePersonInfoVo.getContractTotal() + "\n签单量");
        mRcount.setText("￥" + salePersonInfoVo.getPaymentTotal() + "\n累计收款");
        initMonthData();
    }

    public void initMonthData() {
        // if (!isThisMonth) {
        saleMonthVo = salePersonInfoVo.getThisMonth();
        // }
        // else {
        // saleMonthVo = salePersonInfoVo.getLastMonth();
        // }
        CommonUtils.setTextThree(mContext, mPcotent1, "目标   ", CommonUtils.countNumberNoContent(saleMonthVo.getTargetMoney()), saleMonthVo.getTargetMoneyUnit(), R.color.black_01, 1.5f);
        CommonUtils.setTextThree(mContext, mPcotent2, "预测   ", CommonUtils.countNumberNoContent(saleMonthVo.getPredictedMoney()), saleMonthVo.getPredictedMoneyUnit(), R.color.black_01, 1.5f);
        CommonUtils.setTextThree(mContext, mPcotent3, "签单   ", CommonUtils.countNumberNoContent(saleMonthVo.getContractMoney()), saleMonthVo.getContractMoneyUnit(), R.color.black_01, 1.5f);
        CommonUtils.setTextThree(mContext, mPcotent4, "回款   ", CommonUtils.countNumberNoContent(saleMonthVo.getPaymentMoney()), saleMonthVo.getPaymentMoneyUnit(), R.color.black_01, 1.5f);
        CommonUtils.setTextThree(mContext, mPcotent5, "应收  ", CommonUtils.countNumberNoContent(saleMonthVo.getReceivableMoney()), saleMonthVo.getReceivableMoneyUnit(), R.color.black_01, 1.5f);
        CommonUtils.setTextTwoBefore(mContext, gridCustomer, saleMonthVo.getCustomerCount(), "个", R.color.dimgray, 1.5f);
        CommonUtils.setTextTwoBefore(mContext, gridContact, saleMonthVo.getContactsCount(), "个", R.color.dimgray, 1.5f);
        CommonUtils.setTextTwoBefore(mContext, gridBusiness, saleMonthVo.getBusinessCount(), "个", R.color.dimgray, 1.5f);
        CommonUtils.setTextTwoBefore(mContext, gridTrade, saleMonthVo.getContractCount(), "个", R.color.dimgray, 1.5f);
        CommonUtils.setTextFourBefore(mContext, gridVisitor, salePersonInfoVo.getThisMonth().getVisitCustomerCount(), "个/ ", saleMonthVo.getVisitCount(), "次", R.color.dimgray, 1.5f);
        initMonthColor();
        initDataProgress(saleMonthVo);
    }

    public void initMonthColor() {
        switch (Integer.parseInt(getIntent().getStringExtra("type"))) {
            case 5:
                mCcount.setTextColor(getResources().getColor(R.color.yellow1));
                break;
            case 6:
                mVcount.setTextColor(getResources().getColor(R.color.yellow1));
                break;
            case 7:
                mRcount.setTextColor(getResources().getColor(R.color.yellow1));
                break;

        }
    }
 
    @SuppressLint("ResourceAsColor")
    @Override
    public void executeFailure() {
        super.executeFailure();
        CustomDialog.closeProgressDialog();
        baseHeadLayout.setBackgroundColor(Color.TRANSPARENT);
    }

    @SuppressLint("NewApi")
    @Override
    public void initView() {
        mHeadLayout.setVisibility(View.GONE);
        mProgressV1 = (TextView) findViewById(R.id.count_pb1);
        mProgressV2 = (TextView) findViewById(R.id.count_pb2);
        mProgressV3 = (TextView) findViewById(R.id.count_pb3);
        mProgressV4 = (TextView) findViewById(R.id.count_pb4);
        mProgressV5 = (TextView) findViewById(R.id.count_pb5);
        mPcotent1 = (TextView) findViewById(R.id.pb_content1);
        mPcotent2 = (TextView) findViewById(R.id.pb_content2);
        mPcotent3 = (TextView) findViewById(R.id.pb_content3);
        mPcotent4 = (TextView) findViewById(R.id.pb_content4);
        mPcotent5 = (TextView) findViewById(R.id.pb_content5);
        bsCircleImageView = (BSCircleImageView) findViewById(R.id.head_icon);
        mSaleName = (TextView) findViewById(R.id.txt_head_left_back);
        mPdName = (TextView) findViewById(R.id.position_tv);
        mChangeBut = (TextView) findViewById(R.id.changge_bt);
        mCcount = (TextView) findViewById(R.id.customer_count);
        mVcount = (TextView) findViewById(R.id.visitor_count);
        mRcount = (TextView) findViewById(R.id.record_count);
        gridCustomer = (TextView) findViewById(R.id.add_ccount);
        gridContact = (TextView) findViewById(R.id.add_cocount);
        gridBusiness = (TextView) findViewById(R.id.add_bcount);
        gridTrade = (TextView) findViewById(R.id.add_tcount);
        gridVisitor = (TextView) findViewById(R.id.add_vcount);
        thisMonthBt = (ImageButton) findViewById(R.id.this_month);
        lastMonthBt = (ImageButton) findViewById(R.id.last_month);
        mProgressV1Width = CommonUtils.getScreenWid((Activity) mContext) - CommonUtils.getViewWidth(mPcotent5) - CommonUtils.dip2px(mContext, 20);
        mCountPb1bg = (TextView) findViewById(R.id.count_pb1_bg);
        mCountPb2bg = (TextView) findViewById(R.id.count_pb2_bg);
        mCountPb3bg = (TextView) findViewById(R.id.count_pb3_bg);
        mCountPb4bg = (TextView) findViewById(R.id.count_pb4_bg);
        mCountPb5bg = (TextView) findViewById(R.id.count_pb5_bg);
        mMonthLayout = (LinearLayout) findViewById(R.id.tv_location_father_ly);
        mMonthTv = (TextView) findViewById(R.id.month_name);
        uid = getIntent().getStringExtra("uid");
        type=DateUtils.getTureMonthYYYYM();
        mMonthTv.setText(type);
    }

    @Override
    public void bindViewsListener() {
        thisMonthBt.setOnClickListener(this);
        lastMonthBt.setOnClickListener(this);
    }

    public boolean getData() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put("uid", uid);
            map.put("date", type);
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.SALE_PERSONINFO, map);
            salePersonInfo = gson.fromJson(jsonStrList, SalePersonInfo.class);
            if (Constant.RESULT_CODE.equals(salePersonInfo.getCode())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.this_month:
                // isThisMonth = false;
                ChangTabInfo(0);
                break;

            case R.id.last_month:
                // isThisMonth = true;
                ChangTabInfo(1);
                break;
        }
    }

    public void onClickCustomer(View view) {
        Intent intent = new Intent();
        intent.setClass(SalesmanHomeActivity.this, CrmStaticsNewAddClientActivity.class);
        intent.putExtra("datetime", type);// 月份参数
        intent.putExtra("mine", "1");// 5月31号 小龙要求分辨业务员和仪表盘分开查询，特别加入该字段
        intent.putExtra("userid", uid);
        startActivity(intent);
    }

    public void onClickContact(View view) {
        Intent intent = new Intent();
        intent.setClass(SalesmanHomeActivity.this, CrmContactListActivity.class);
        intent.putExtra("datetime", type);// 月份参数
        intent.putExtra("mine", "1");
        intent.putExtra("userid", uid);
        startActivity(intent);
    }

    public void onClickBusiness(View view) {
        Intent intent = new Intent();
        intent.setClass(SalesmanHomeActivity.this, CrmStaticsNewAddBussinessActivity.class);
        intent.putExtra("datetime", type);// 月份参数
        intent.putExtra("mine", "1");
        intent.putExtra("userid", uid);
        startActivity(intent);
    }

    public void onClickTrade(View view) {
        Intent intent = new Intent();
        intent.setClass(SalesmanHomeActivity.this, CrmStaticsNewAddTradeActivity.class);
        intent.putExtra("datetime", type);// 月份参数
        intent.putExtra("userid", uid);
        intent.putExtra("mine", "1");
        startActivity(intent);
    }

    public void onClickVistor(View view) {
        Intent intent = new Intent();
        intent.setClass(SalesmanHomeActivity.this, CrmStaticsNewAddVistorActivity.class);
        intent.putExtra("datetime", type);// 月份参数
        intent.putExtra("userid", uid);
        intent.putExtra("mine", "1");
        startActivity(intent);
    }

    public void ChangTabInfo(int tabId) {

        switch (tabId) {
            case 0:
                // 上个月
                type = DateUtils.getLastOrNextMonth(-1, mMonthTv.getText().toString().trim());
                mMonthTv.setText(type);
                break;
            case 1:
                type = DateUtils.getLastOrNextMonth(1, mMonthTv.getText().toString().trim());
                mMonthTv.setText(type);
                break;
        }
        CustomDialog.showProgressDialog(mContext, "正在加载..");
        new ThreadUtil(mContext, this).start();
//        initMonthData();
    }

    public void onChangeClick(View view) {
        Intent intent1 = new Intent();
        intent1.putExtra("proKey", "0");
        intent1.putExtra("typekey", "6");
        intent1.putExtra("subordinate", "1");
        intent1.setClass(SalesmanHomeActivity.this, AddByPersonCRMActivity.class);
        intent1.putExtra("requst_number", 2014);
        startActivityForResult(intent1, 2014);

    }

    // 初始化五彩的进度条
    /**
     * @param salePersonInfo
     */
    public void initDataProgress(SalePersonInfo salePersonInfo) {
        int progressMoney[] = {
                (int) Float.parseFloat(salePersonInfo.getTargetMoney()), (int) Float.parseFloat(salePersonInfo.getPredictedMoney()), (int) Float.parseFloat(salePersonInfo.getContractMoney()), (int) Float.parseFloat(salePersonInfo.getPaymentMoney()),
                (int) Float.parseFloat(salePersonInfo.getReceivableMoney())
        };
        Arrays.sort(progressMoney);
        if (progressMoney[4] == 0) {
            mProgressV1.getLayoutParams().width = mProgressV1Width;
            mProgressV2.getLayoutParams().width = mProgressV1Width;
            mProgressV3.getLayoutParams().width = mProgressV1Width;
            mProgressV4.getLayoutParams().width = mProgressV1Width;
            mProgressV5.getLayoutParams().width = mProgressV1Width;
            mCountPb1bg.setVisibility(View.GONE);
            mCountPb2bg.setVisibility(View.GONE);
            mCountPb3bg.setVisibility(View.GONE);
            mCountPb4bg.setVisibility(View.GONE);
            mCountPb5bg.setVisibility(View.GONE);
            mProgressV1.setBackground(CommonUtils.setBackgroundShap(mContext, 10, "#FFFFFF", "#efefef"));
            mProgressV2.setBackground(CommonUtils.setBackgroundShap(mContext, 10, "#FFFFFF", "#efefef"));
            mProgressV3.setBackground(CommonUtils.setBackgroundShap(mContext, 10, "#FFFFFF", "#efefef"));
            mProgressV4.setBackground(CommonUtils.setBackgroundShap(mContext, 10, "#FFFFFF", "#efefef"));
            mProgressV5.setBackground(CommonUtils.setBackgroundShap(mContext, 10, "#FFFFFF", "#efefef"));
        }
        else {
            mCountPb1bg.setVisibility(View.VISIBLE);
            mCountPb2bg.setVisibility(View.VISIBLE);
            mCountPb3bg.setVisibility(View.VISIBLE);
            mCountPb4bg.setVisibility(View.VISIBLE);
            mCountPb5bg.setVisibility(View.VISIBLE);
            float mWidth1 = (Float.parseFloat(salePersonInfo.getTargetMoney()) / progressMoney[4]);
            float mWidth2 = (Float.parseFloat(salePersonInfo.getPredictedMoney()) / progressMoney[4]);
            float mWidth3 = (Float.parseFloat(salePersonInfo.getContractMoney()) / progressMoney[4]);
            float mWidth4 = (Float.parseFloat(salePersonInfo.getPaymentMoney()) / progressMoney[4]);
            float mWidth5 = (Float.parseFloat(salePersonInfo.getReceivableMoney()) / progressMoney[4]);
            if (mWidth1 == 0) {
                mProgressV1.getLayoutParams().width = mProgressV1Width;
                mCountPb1bg.setVisibility(View.GONE);
                mProgressV1.setBackground(CommonUtils.setBackgroundShap(mContext, 10, "#FFFFFF", "#efefef"));
            }
            else {
                mProgressV1.getLayoutParams().width = (int) (mWidth1 * (CommonUtils.getScreenWid(this) - CommonUtils.getViewWidth(mPcotent1) - CommonUtils.dip2px(SalesmanHomeActivity.this, 20)));
                mProgressV1.setBackground(CommonUtils.setBackgroundShap(mContext, 10, "#00000000", "#F6D56E"));
                mCountPb1bg.setBackground(CommonUtils.setBackgroundShap(mContext, 10, "#FFFFFF", "#efefef"));
                mCountPb1bg.getLayoutParams().width = CommonUtils.getScreenWid(this) - CommonUtils.getViewWidth(mPcotent1) - CommonUtils.dip2px(SalesmanHomeActivity.this, 20);
            }
            if (mWidth2 == 0) {
                mCountPb1bg.setVisibility(View.GONE);
                mProgressV2.getLayoutParams().width = mProgressV1Width;
                mProgressV2.setBackground(CommonUtils.setBackgroundShap(mContext, 10, "#FFFFFF", "#efefef"));
            }
            else {
                mProgressV2.getLayoutParams().width = (int) (mWidth2 * (CommonUtils.getScreenWid(this) - CommonUtils.getViewWidth(mPcotent2) - CommonUtils.dip2px(SalesmanHomeActivity.this, 20)));
                mProgressV2.setBackground(CommonUtils.setBackgroundShap(mContext, 10, "#00000000", "#AD91CB"));
                mCountPb2bg.getLayoutParams().width = CommonUtils.getScreenWid(this) - CommonUtils.getViewWidth(mPcotent2) - CommonUtils.dip2px(SalesmanHomeActivity.this, 20);
                mCountPb2bg.setBackground(CommonUtils.setBackgroundShap(mContext, 10, "#FFFFFF", "#efefef"));
            }
            if (mWidth3 == 0) {
                mCountPb1bg.setVisibility(View.GONE);
                mProgressV3.getLayoutParams().width = mProgressV1Width;
                mProgressV3.setBackground(CommonUtils.setBackgroundShap(mContext, 10, "#FFFFFF", "#efefef"));
            } else {
                mProgressV3.getLayoutParams().width = (int) (mWidth3 * (CommonUtils.getScreenWid(this) - CommonUtils.getViewWidth(mPcotent3) - CommonUtils.dip2px(SalesmanHomeActivity.this, 20)));
                mProgressV3.setBackground(CommonUtils.setBackgroundShap(mContext, 10, "#00000000", "#5CB4E8"));
                mCountPb3bg.getLayoutParams().width = CommonUtils.getScreenWid(this) - CommonUtils.getViewWidth(mPcotent3) - CommonUtils.dip2px(SalesmanHomeActivity.this, 20);
                mCountPb3bg.setBackground(CommonUtils.setBackgroundShap(mContext, 10, "#FFFFFF", "#efefef"));
            }
            if (mWidth4 == 0) {
                mCountPb1bg.setVisibility(View.GONE);
                mProgressV4.getLayoutParams().width = mProgressV1Width;
                mProgressV4.setBackground(CommonUtils.setBackgroundShap(mContext, 10, "#FFFFFF", "#efefef"));
            }
            else {
                mCountPb4bg.getLayoutParams().width = CommonUtils.getScreenWid(this) - CommonUtils.getViewWidth(mPcotent4) - CommonUtils.dip2px(SalesmanHomeActivity.this, 20);
                mCountPb4bg.setBackground(CommonUtils.setBackgroundShap(mContext, 10, "#FFFFFF", "#efefef"));
                mProgressV4.getLayoutParams().width = (int) (mWidth4 * (CommonUtils.getScreenWid(this) - CommonUtils.getViewWidth(mPcotent4) - CommonUtils.dip2px(SalesmanHomeActivity.this, 20)));
                mProgressV4.setBackground(CommonUtils.setBackgroundShap(mContext, 10, "#00000000", "#99E1D4"));
            }
            if (mWidth5 == 0) {
                mCountPb1bg.setVisibility(View.GONE);
                mProgressV5.getLayoutParams().width = mProgressV1Width;
                mProgressV5.setBackground(CommonUtils.setBackgroundShap(mContext, 10, "#FFFFFF", "#efefef"));
            }
            else {
                mCountPb5bg.setBackground(CommonUtils.setBackgroundShap(mContext, 10, "#FFFFFF", "#efefef"));
                mCountPb5bg.getLayoutParams().width = CommonUtils.getScreenWid(this) - CommonUtils.getViewWidth(mPcotent5) - CommonUtils.dip2px(SalesmanHomeActivity.this, 20);
                mProgressV5.getLayoutParams().width = (int) (mWidth5 * (CommonUtils.getScreenWid(this) - CommonUtils.getViewWidth(mPcotent5) - CommonUtils.dip2px(SalesmanHomeActivity.this, 20)));
                mProgressV5.setBackground(CommonUtils.setBackgroundShap(mContext, 10, "#00000000", "#F19EA1"));
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
        /* 负责人 */
            case 2014:
                if (resultCode == 2014) {
                    if (data != null) {
                        /** 采用post请求 */
                        EmployeeVO employee = (EmployeeVO) data
                                .getSerializableExtra("approve_activity");
                        if (employee == null) {
                            employee = ((List<EmployeeVO>) data
                                    .getSerializableExtra("checkboxlist")).get(0);
                        }
                        if (employee != null) {
                            uid = employee.getUserid();
                            new ThreadUtil(this, this).start();
                            CustomDialog.showProgressDialog(mContext, "正在切换信息..");
                        }
                        else {
                            CustomToast.showShortToast(mContext, "切换人员信息失败~");
                        }

                    }
                }
                break;

        }
    }
}
