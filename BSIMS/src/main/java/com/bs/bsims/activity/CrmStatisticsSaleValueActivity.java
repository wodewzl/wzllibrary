
package com.bs.bsims.activity;

import android.view.View;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.CrmStaticSaleValueAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.CrmStaticSaleValueVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.view.BSCrmMonthSaleValue;
import com.bs.bsims.view.BSRefreshListView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CrmStatisticsSaleValueActivity extends BaseActivity {

    private TextView complte_value, target_money, huikuan_money;
    private BSRefreshListView static_lv_refresh;
    private CrmStaticSaleValueVO crmSaleVo;
    private CrmStaticSaleValueAdapter saleAdapter;
    private List<CrmStaticSaleValueVO> crmStaticList = new ArrayList<CrmStaticSaleValueVO>();
    private BSCrmMonthSaleValue BsSaleValue;
    private String type;// 1--上月，2--本月，3--本季度

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.crm_month_sale_value_view, mContentLayout);
    }

    @Override
    public boolean getDataResult() {
        // TODO Auto-generated method stub
        return getData();
    }

    private boolean getData() {
        try {
            Gson gson = new Gson();

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put("type", type);
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.CRM_STATIC_SALE_VALUE, map);
            crmSaleVo = gson.fromJson(jsonStr, CrmStaticSaleValueVO.class);

            if (Constant.RESULT_CODE.equals(crmSaleVo.getCode())) {
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
    public void updateUi() {
        crmStaticList = (List<CrmStaticSaleValueVO>) crmSaleVo.getArray();
        saleAdapter.updateData(crmStaticList);

    }

    @Override
    public void initView() {
        BsSaleValue = (BSCrmMonthSaleValue) findViewById(R.id.static_sale_value);
        complte_value = (TextView) findViewById(R.id.complete_value);
        target_money = (TextView) findViewById(R.id.target_money);
        huikuan_money = (TextView) findViewById(R.id.huikuan_money);
        static_lv_refresh = (BSRefreshListView) findViewById(R.id.static_lv_refresh);
        saleAdapter = new CrmStaticSaleValueAdapter(CrmStatisticsSaleValueActivity.this, crmStaticList);
        static_lv_refresh.setAdapter(saleAdapter);

        complte_value.setText(getIntent().getStringExtra("percent"));// 完成率
        target_money.setText("￥" + getIntent().getStringExtra("target"));// 目标金额
        huikuan_money.setText("￥" + getIntent().getStringExtra("payment"));// 回款金额

        float data = Float.parseFloat(CommonUtils.isNormalData(getIntent().getStringExtra("rate")));
        // 超额完成任务，就显示100%，对应于1
        if (data > 1) {
            BsSaleValue.setData1(1);
        } else {
            BsSaleValue.setData1(data);
        }
        BsSaleValue.invalidate();

        type = getIntent().getStringExtra("type");
        if ("1".equals(type)) {
            mTitleTv.setText("上月销售业绩");
        } else if ("2".equals(type)) {
            mTitleTv.setText("本月销售业绩");
        } else if ("3".equals(type)) {
            mTitleTv.setText("本季度销售业绩");
        }

    }

    @Override
    public void bindViewsListener() {
        // TODO Auto-generated method stub

    }

}
