
package com.bs.bsims.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.bs.bsims.R;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.CrmProductManagementResultVO;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomToast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class CrmProductManagementAddInfoActivity extends BaseActivity implements OnClickListener {

    private EditText productInfoName, productInfoPrice, productInfoUnit, writeTodayWork;
    private CrmProductManagementResultVO crmProductManagementResultVo;
    private Context mContext;

    @Override
    public void baseSetContentView() {
        // TODO Auto-generated method stub
        View layout = View.inflate(this, R.layout.crmproduct_management_addinfo, null);
        mLoadingLayout.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);
        mContentLayout.addView(layout);
        mContext = this;

    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        mTitleTv.setText("添加产品");
        mOkTv.setText("保存");
        productInfoName = (EditText) findViewById(R.id.product_info_name);
        productInfoPrice = (EditText) findViewById(R.id.product_info_price);
        productInfoUnit = (EditText) findViewById(R.id.product_info_unit);
        writeTodayWork = (EditText) findViewById(R.id.write_today_work);
    }

    @Override
    public boolean getDataResult() {
        // TODO Auto-generated method stub
        return true;
    }

    public boolean CheckInfo() {
        if (productInfoName.getText().toString().trim().equals("")) {
            CustomToast.showLongToast(mContext, "请填写产品名称!");
            return false;
        }
        else if (productInfoPrice.getText().toString().trim().equals("")) {
            CustomToast.showLongToast(mContext, "请填写产品单价!");
            return false;
        }
        else if (productInfoUnit.getText().toString().trim().equals("")) {
            CustomToast.showLongToast(mContext, "请填写产品单位!");
            return false;
        }
        else if (writeTodayWork.getText().toString().trim().equals("")) {
            CustomToast.showLongToast(mContext, "请填写产品信息!");
            return false;
        }
        return true;
    }

    @Override
    public void updateUi() {
        // TODO Auto-generated method stub

    }

    @Override
    public void bindViewsListener() {
        // TODO Auto-generated method stub
        productInfoName.setOnClickListener(this);
        productInfoPrice.setOnClickListener(this);
        productInfoUnit.setOnClickListener(this);
        writeTodayWork.setOnClickListener(this);
        mOkTv.setOnClickListener(this);

    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.txt_comm_head_right:
                if (CheckInfo()) {
                    commit();
                }

                break;

            default:
                break;
        }
    }

    public void commit() {
        CustomDialog.showProgressDialog(this, "正在提交数据...");
        RequestParams params = new RequestParams();

        try {
            params.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            params.put("userid", BSApplication.getInstance().getUserId());
            // params.put("userid", "26");
            params.put("name", productInfoName.getText().toString());
            params.put("unit", productInfoUnit.getText().toString());
            params.put("remark", writeTodayWork.getText().toString());
            params.put("money", productInfoPrice.getText().toString());
            // params.put("pid","18");

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        // params.put("name", "woshishishi");// 传输的字符数据
        String url = BSApplication.getInstance().getHttpTitle() + Constant.CRM_PRODUCT_MANAGEMENT_ADDINFO;
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                System.out.println(new String(arg2));

                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(new String(arg2));
                    String str = (String) jsonObject.get("retinfo");
                    String code = (String) jsonObject.get("code");
                    if (Constant.RESULT_CODE.equals(code)) {
                        Intent i = new Intent();
                        i.putExtra("fs", true);
                        setResult(1001, i);
                        CrmProductManagementAddInfoActivity.this.finish();
                        CustomToast.showShortToast(CrmProductManagementAddInfoActivity.this, str);
                    } else {
                        CustomToast.showShortToast(CrmProductManagementAddInfoActivity.this, str);
                    }
                    CustomDialog.closeProgressDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
