
package com.bs.bsims.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.bs.bsims.R;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.fragment.ContactsLetterTabFragment;
import com.bs.bsims.model.ResultVO;
import com.bs.bsims.utils.HttpClientUtil;
import com.google.gson.Gson;

import java.util.HashMap;

public class AddByPersonActivity extends BaseActivity {
    private ContactsLetterTabFragment mFragment;
    private ResultVO mResultInfoVO;
    private String mType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.add_by_person, null);
        mContentLayout.addView(layout);
    }

    @Override
    public boolean getDataResult() {
        if (this.getIntent() != null && this.getIntent().getBooleanExtra("is_new_data", false))
            return true;
        else
            return getData();
    }

    @Override
    public void updateUi() {
        initData();
    }

    @Override
    public void initView() {
        mTitleTv.setText("选择联系人");
    }

    @Override
    public void bindViewsListener() {

    }

    public void initData() {
        try {
            mFragment = new ContactsLetterTabFragment(mResultInfoVO, true);
            if (this.getIntent().getStringExtra("state") != null) {
                mFragment.setCrmState("1");
            }

            FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.add_fragment, mFragment);
            transaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getData() {
        try {
            String url;
            String jsonUrlStr;
            Gson gson = new Gson();
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("userid", BSApplication.getInstance().getUserId());
            if (getIntent().getStringExtra("isfrom") != null) {//判断是否来自于合同的新参数接口（商机负责人或跟进人只能在商机所属客户下的跟进人中选择）
                params.put("isfrom", getIntent().getStringExtra("isfrom"));
                params.put("cid", getIntent().getStringExtra("cid"));
            }
            params.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            // 1为crm负责人 else 为通讯录
            if ("1".equals(this.getIntent().getStringExtra("state"))) {
                url = Constant.CRM_CLIENT_USER_SELET_ONE;
                jsonUrlStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + url, params);
                mResultInfoVO = gson.fromJson(jsonUrlStr, ResultVO.class);
            } else {
                mResultInfoVO = BSApplication.getInstance().getResultVO();
            }

            if (Constant.RESULT_CODE400.equals(mResultInfoVO.getCode()))
                return false;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
