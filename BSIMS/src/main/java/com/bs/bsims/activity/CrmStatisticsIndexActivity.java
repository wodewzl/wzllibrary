
package com.bs.bsims.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.CrmBossStatisitVo;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSCrmTrapezoidalFunnelView;
import com.google.gson.Gson;

import java.util.HashMap;

public class CrmStatisticsIndexActivity extends BaseActivity implements OnClickListener {

    private TextView mTextView01, mTextView02, mTextView03, mTextView04, mTextView05;
    private BSCrmTrapezoidalFunnelView mCrmTrapezoidalFunnelView;
    private TextView mMaxMoneyTv, mDopeMoneyTv, mTargtMoenyTv;
    private TextView mNewCmTv, mNewBsTv, mNewTdTv;
    private TextView mGetMoneyTv, mTargetMoenyTv, mSalePercetTv;
    private TextView mVistCmTv, mVistCrmCountTvd, mDayVistTv;
    private TextView last_month, this_month, this_season;

    private CrmBossStatisitVo mBossStatisitVo, mBossStatisitVo2;
    private LinearLayout crm_statics_sales_layout, new_add_trades_layout, crm_statics_client_layout,
            crm_statiics_bussness_layout, static_vistor_layout, crm_statics_trande_layout;
    private String type;

    // 合同
    private TextView mTrandeMoney, mTrandeGetMoney, mTradeCount;
    // 客户分析
    private TextView mCustomerCount, mCustomerAdd, mCustomerDrop;
    // 商机
    private TextView mBussinesCount, mBussinesAdd, mBussinesDrop;
    private String mUid, mDid;
    private TextView mSaleTitleTv;
    private LinearLayout mSelectLayout;

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.crm_boss_statistics_index, mContentLayout);
        type = "2";
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    @Override
    public void updateUi() {
        // 赛选人和部门时，num为0
        if (!"-1".equals(mBossStatisitVo2.getNum())) {
            CommonUtils.setTextTwoBefore(this, mSaleTitleTv, "我的下属", "  |  " + mBossStatisitVo2.getNum(), R.color.C21, 1.0f);
            mSaleTitleTv.setTextColor(this.getResources().getColor(R.color.C6));
        }

        if ("1".equals(type)) {
            mBossStatisitVo = mBossStatisitVo2.getInfo().getLastMonth();
        } else if ("2".equals(type)) {
            mBossStatisitVo = mBossStatisitVo2.getInfo().getThisMonth();
        } else if ("3".equals(type)) {
            mBossStatisitVo = mBossStatisitVo2.getInfo().getThisSeason();
        }
        float crmfunnelview_one = Float.parseFloat(CommonUtils.isNormalData(mBossStatisitVo
                .getFunnel().getArray().get(0).getMoney()));
        float crmfunnelview_two = Float.parseFloat(CommonUtils.isNormalData(mBossStatisitVo
                .getFunnel().getArray().get(1).getMoney()));
        float crmfunnelview_three = Float.parseFloat(CommonUtils.isNormalData(mBossStatisitVo
                .getFunnel().getArray().get(2).getMoney()));
        float crmfunnelview_four = Float.parseFloat(CommonUtils.isNormalData(mBossStatisitVo
                .getFunnel().getArray().get(3).getMoney()));
        float crmfunnelview_five = Float.parseFloat(CommonUtils.isNormalData(mBossStatisitVo
                .getFunnel().getArray().get(4).getMoney()));

        if (crmfunnelview_one == 0 && crmfunnelview_two == 0 && crmfunnelview_three == 0 && crmfunnelview_four == 0) {
            mCrmTrapezoidalFunnelView.setFlag(true);
        } else {
            mCrmTrapezoidalFunnelView.setFlag(false);
        }
        mCrmTrapezoidalFunnelView.setPrice1(crmfunnelview_one);
        mCrmTrapezoidalFunnelView.setPrice2(crmfunnelview_two);
        mCrmTrapezoidalFunnelView.setPrice3(crmfunnelview_three);
        mCrmTrapezoidalFunnelView.setPrice4(crmfunnelview_four);
        mCrmTrapezoidalFunnelView.setPrice5(crmfunnelview_five);

        mCrmTrapezoidalFunnelView.invalidate();

        mTextView01.setText(mBossStatisitVo.getFunnel().getArray().get(0).getStatusName() + "(￥ "
                + CommonUtils.countNumberSecond(mBossStatisitVo.getFunnel().getArray().get(0).getMoney())
                + ")");
        mTextView02.setText(mBossStatisitVo.getFunnel().getArray().get(1).getStatusName() + "( ￥"
                + CommonUtils.countNumberSecond(mBossStatisitVo.getFunnel().getArray().get(1).getMoney())
                + ")");
        mTextView03.setText(mBossStatisitVo.getFunnel().getArray().get(2).getStatusName() + "(￥ "
                + CommonUtils.countNumberSecond(mBossStatisitVo.getFunnel().getArray().get(2).getMoney())
                + ")");
        mTextView04.setText(mBossStatisitVo.getFunnel().getArray().get(3).getStatusName() + "(￥ "
                + CommonUtils.countNumberSecond(mBossStatisitVo.getFunnel().getArray().get(3).getMoney())
                + ")");
        mTextView05.setText(mBossStatisitVo.getFunnel().getArray().get(4).getStatusName() + "(￥"
                + CommonUtils.countNumberSecond(mBossStatisitVo.getFunnel().getArray().get(4).getMoney())
                + ")");

        mMaxMoneyTv.setText(CommonUtils.formatDetailMoney(mBossStatisitVo.getFunnel().getMaxMoney()));// 最大成交额
        mDopeMoneyTv.setText(CommonUtils.formatDetailMoney(mBossStatisitVo.getFunnel().getPredictedMoney()));// 预测总金额
        mTargtMoenyTv.setText(CommonUtils.formatDetailMoney(mBossStatisitVo.getFunnel().getTargetMoney()));// 目标金额

        mGetMoneyTv.setText("回款金额：" + CommonUtils.formatDetailMoney(mBossStatisitVo.getTargets().getPayment()));
        mTargetMoenyTv.setText("累计回款：" + CommonUtils.formatDetailMoney(mBossStatisitVo.getTargets().getPaymentTotal()));
        mSalePercetTv.setText(mBossStatisitVo.getTargets().getCompPercent());

        mVistCmTv.setText("跟进客户：" + mBossStatisitVo.getVisit().getCustomerCount());
        mVistCrmCountTvd.setText("拜访次数：" + mBossStatisitVo.getVisit().getVisitCount());
        mDayVistTv.setText(mBossStatisitVo.getVisit().getVisitPerday());

        mTrandeMoney.setText(CommonUtils.countNumber(mBossStatisitVo.getContract().getReceivable()));
        mTrandeGetMoney.setText("签约金额：" + CommonUtils.formatDetailMoney(mBossStatisitVo.getContract().getMoney()));
        mTradeCount.setText("本月签单：" + mBossStatisitVo.getContract().getCount());

        mCustomerCount.setText(mBossStatisitVo.getCustomer().getCount());
        mCustomerAdd.setText(mBossStatisitVo.getCustomer().getAddcount());
        mCustomerDrop.setText(mBossStatisitVo.getCustomer().getDropcount());

        mBussinesCount.setText(mBossStatisitVo.getBusiness().getCount());
        mBussinesAdd.setText(mBossStatisitVo.getBusiness().getAddcount());
        mBussinesDrop.setText(mBossStatisitVo.getBusiness().getDropcount());

    }

    @Override
    public void initView() {
        mTitleTv.setText("仪表盘");
        mSaleTitleTv = (TextView) findViewById(R.id.sale_title_tv);
        mSelectLayout = (LinearLayout) findViewById(R.id.select_layout);
        mCrmTrapezoidalFunnelView = (BSCrmTrapezoidalFunnelView) findViewById(R.id.crmfunnelview);
        mTextView01 = (TextView) findViewById(R.id.text01);
        mTextView02 = (TextView) findViewById(R.id.text02);
        mTextView03 = (TextView) findViewById(R.id.text03);
        mTextView04 = (TextView) findViewById(R.id.text04);
        mTextView05 = (TextView) findViewById(R.id.text05);
        mMaxMoneyTv = (TextView) findViewById(R.id.crm_boos_statistics_maxmoney);
        mDopeMoneyTv = (TextView) findViewById(R.id.crm_boos_statistics_dopemoney);
        mTargtMoenyTv = (TextView) findViewById(R.id.crm_boos_statistics_targetmoney);
        mNewCmTv = (TextView) findViewById(R.id.crm_boos_statistics_newcm);
        mNewBsTv = (TextView) findViewById(R.id.crm_boos_statistics_newbs);
        mNewTdTv = (TextView) findViewById(R.id.crm_boos_statistics_newtd);
        mGetMoneyTv = (TextView) findViewById(R.id.crm_boos_statistics_getmoney);
        mTargetMoenyTv = (TextView) findViewById(R.id.crm_boos_statistics_target_money);
        mSalePercetTv = (TextView) findViewById(R.id.crm_boos_statistics_percent);
        mVistCmTv = (TextView) findViewById(R.id.crm_boos_statistics_vcnum);
        mVistCrmCountTvd = (TextView) findViewById(R.id.crm_boos_statistics_vvnum);
        mDayVistTv = (TextView) findViewById(R.id.crm_boos_statistics_visitorpercent);
        crm_statics_sales_layout = (LinearLayout) findViewById(R.id.crm_statics_sales_layout);
        new_add_trades_layout = (LinearLayout) findViewById(R.id.new_add_trades_layout);
        crm_statics_client_layout = (LinearLayout) findViewById(R.id.crm_statics_client_layout);
        crm_statiics_bussness_layout = (LinearLayout) findViewById(R.id.crm_statiics_bussness_layout);
        crm_statics_trande_layout = (LinearLayout) findViewById(R.id.crm_statics_trande_layout);
        static_vistor_layout = (LinearLayout) findViewById(R.id.static_vistor_layout);
        last_month = (TextView) findViewById(R.id.last_month);
        this_month = (TextView) findViewById(R.id.this_month);
        this_season = (TextView) findViewById(R.id.this_season);
        mTrandeMoney = (TextView) findViewById(R.id.crm_boos_statistics_trade_percent);
        mTradeCount = (TextView) findViewById(R.id.crm_boos_statistics_gettrade);
        mTrandeGetMoney = (TextView) findViewById(R.id.crm_boos_statistics_trade_money);

        mCustomerCount = (TextView) findViewById(R.id.board_customerall);
        mCustomerAdd = (TextView) findViewById(R.id.board_customeradd);
        mCustomerDrop = (TextView) findViewById(R.id.board_customergive);

        mBussinesCount = (TextView) findViewById(R.id.board__bussinesall);
        mBussinesAdd = (TextView) findViewById(R.id.board__bussinesadd);
        mBussinesDrop = (TextView) findViewById(R.id.board__bussinesgive);
    }

    @Override
    public void bindViewsListener() {
        static_vistor_layout.setOnClickListener(this);
        crm_statics_client_layout.setOnClickListener(this);
        crm_statiics_bussness_layout.setOnClickListener(this);
        crm_statics_sales_layout.setOnClickListener(this);
        crm_statics_trande_layout.setOnClickListener(this);
        new_add_trades_layout.setOnClickListener(this);
        last_month.setOnClickListener(this);
        this_month.setOnClickListener(this);
        this_season.setOnClickListener(this);
        mSelectLayout.setOnClickListener(this);
    }

    public boolean getData() {
        try {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put("ver", Constant.PHPVERSIONPARMERS);
            map.put("uid", mUid);
            map.put("did", mDid);
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle()
                    + Constant.CRM_STATICTIS_INDEX, map);
            Gson gson = new Gson();
            mBossStatisitVo2 = gson.fromJson(jsonStr, CrmBossStatisitVo.class);
            if (Constant.RESULT_CODE.equals(mBossStatisitVo2.getCode())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void onClick(View arg0) {
        Intent intent = new Intent();
        switch (arg0.getId()) {
            case R.id.last_month:// 上月
                type = "1";
                ClearColorText();
                last_month.setTextColor(getResources().getColor(R.color.C1));
                last_month.setBackgroundResource(R.drawable.corners_tab_left_select);
                updateUi();
                break;
            case R.id.this_month:// 本月
                type = "2";
                ClearColorText();
                this_month.setTextColor(getResources().getColor(R.color.C1));
                this_month.setBackgroundResource(R.drawable.corners_tab_center_select);
                updateUi();
                break;

            case R.id.this_season:// 本季度
                type = "3";
                ClearColorText();
                this_season.setTextColor(getResources().getColor(R.color.C1));
                this_season.setBackgroundResource(R.drawable.corners_tab_right_select);
                updateUi();
                break;
            case R.id.crm_statics_client_layout:// 客户分析折线图
                intent.setClass(CrmStatisticsIndexActivity.this, CrmStaticsClientActivity.class);
                intent.putExtra("uid", mUid);
                intent.putExtra("did", mDid);
                startActivity(intent);
                break;
            case R.id.crm_statiics_bussness_layout:// 商机分析折线图
                intent.setClass(CrmStatisticsIndexActivity.this, CrmStaticsBussnessActivity.class);
                intent.putExtra("uid", mUid);
                intent.putExtra("did", mDid);
                startActivity(intent);
                break;
            case R.id.new_add_trades_layout:// 跳转到新增合同
                intent.setClass(CrmStatisticsIndexActivity.this, CrmStaticsNewAddTradeActivity.class);
                intent.putExtra("type", type);
                startActivity(intent);
                break;
            case R.id.crm_statics_sales_layout:// 跳转到销售业绩
                intent.setClass(CrmStatisticsIndexActivity.this, CrmStaticsSaleActivity.class);
                intent.putExtra("uid", mUid);
                intent.putExtra("did", mDid);
                startActivity(intent);
                break;
            case R.id.static_vistor_layout:// 跳转到跟单记录
                intent.setClass(CrmStatisticsIndexActivity.this, CrmStaticsVisitorActivity.class);
                intent.putExtra("uid", mUid);
                intent.putExtra("did", mDid);
                startActivity(intent);
                break;

            case R.id.crm_statics_trande_layout:
                intent.setClass(CrmStatisticsIndexActivity.this, CrmStaticsTradeActivity.class);
                intent.putExtra("uid", mUid);
                intent.putExtra("did", mDid);
                startActivity(intent);
                break;

            case R.id.select_layout:
                intent.setClass(this, CrmPeopleAddDepartSelectActivity.class);
                startActivityForResult(intent, 2015);
                break;

            default:
                break;
        }

    }

    /*
     * 清除背景颜色和字体的颜色
     */
    public void ClearColorText() {
        last_month.setTextColor(getResources().getColor(R.color.C5));
        this_month.setTextColor(getResources().getColor(R.color.C5));
        this_season.setTextColor(getResources().getColor(R.color.C5));
        last_month.setBackgroundResource(R.drawable.corners_tab_trade_left_normal);
        this_month.setBackgroundResource(R.drawable.corners_tab_trade_center_normal);
        this_season.setBackgroundResource(R.drawable.corners_tab_trade_right_normal);
    }

    // 查看客户总量
    public void readALLCcount(View view) {
        Intent intent = new Intent();
        intent.putExtra("uid", mUid);
        intent.putExtra("did", mDid);
        intent.putExtra("type", type);
        intent.putExtra("option", "0");
        intent.setClass(this, CrmClientListActivity.class);// 客户
        startActivity(intent);
    }

    // 查看新增客户
    public void readMonthAddCcount(View view) {
        Intent intent = new Intent();
        intent.putExtra("userid", mUid);
        intent.putExtra("did", mDid);
        intent.setClass(this, CrmStaticsNewAddClientActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("option", "1");
        startActivity(intent);
    }

    // 查看放弃客户
    public void readMonthGiveCcount(View view) {
        Intent intent = new Intent();
        intent.putExtra("userid", mUid);
        intent.putExtra("did", mDid);
        intent.setClass(this, CrmStaticsNewAddClientActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("option", "2");
        startActivity(intent);
    }

    // 查看商机总量
    public void readALLBcount(View view) {
        Intent intent = new Intent();
        intent.putExtra("uid", mUid);
        intent.putExtra("did", mDid);
        intent.putExtra("type", type);
        intent.putExtra("option", "0");
        intent.setClass(this, CrmBusinessHomeListActivity.class);// 客户
        startActivity(intent);
    }

    // 查看新增商机
    public void readMonthAddBcount(View view) {
        Intent intent = new Intent();
        intent.putExtra("userid", mUid);
        intent.putExtra("did", mDid);
        intent.putExtra("type", type);
        intent.putExtra("option", "1");
        intent.setClass(this, CrmStaticsNewAddBussinessActivity.class);// 客户
        startActivity(intent);
    }

    // 查看放弃商机
    public void readMonthGiveBcount(View view) {
        Intent intent = new Intent();
        intent.putExtra("userid", mUid);
        intent.putExtra("did", mDid);
        intent.putExtra("type", type);
        intent.putExtra("option", "2");
        intent.setClass(this, CrmStaticsNewAddBussinessActivity.class);// 客户
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 2015) {
            if (data == null)
                return;
            String dname = data.getStringExtra("dname");
            if (data.hasExtra("userid")) {
                mUid = data.getStringExtra("userid");
                String name = data.getStringExtra("name");
                mSaleTitleTv.setText(dname + " " + name);
                mDid = "";
            } else {
                mDid = data.getStringExtra("did");
                mSaleTitleTv.setText(dname);
                mUid = "";
            }
            mSaleTitleTv.setTextColor(this.getResources().getColor(R.color.C21));
            new ThreadUtil(this, this).start();
        }
    }
}
