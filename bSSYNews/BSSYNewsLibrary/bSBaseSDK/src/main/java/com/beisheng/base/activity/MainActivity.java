
package com.beisheng.base.activity;

import android.view.View;

import com.beisheng.base.R;
import com.beisheng.base.constant.BaseConstant;
import com.beisheng.base.database.CacheDbHelper;
import com.beisheng.base.http.HttpClientUtil;
import com.beisheng.base.mode.CrmOptionsVO;

import java.util.HashMap;

public class MainActivity extends BaseActivity {
    private CacheDbHelper mDbHelper;
    CrmOptionsVO mCrmOptionsVO;
    HashMap<String, String> mparamsMap = new HashMap<String, String>();

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.main_activity, mBaseContentLayout);
    }

    @Override
    public boolean getDataResult() {
        return getOptionsData();
    }

    @Override
    public void initView() {
        mDbHelper = new CacheDbHelper(this);
        initData();
    }

    @Override
    public void bindViewsListener() {

    }

    public void initData() {
        mparamsMap.put("userid", "49");
        mparamsMap.put("ftoken", "RBDTZXGUMNDKkEwMkZFN0UyMTA1RgO0O0OO0O0O");
        mUrl = "http://cp.beisheng.wang/dev_1.2.5/" + BaseConstant.CRM_CLIENT_OPTION;
        // String jsonOld = getCacheFromDatabase(mUrl, mparamsMap);
        // setCacheStrTime(mUrl, mparamsMap, value, time)
        String jsonOld = getCacheStr(mUrl, mparamsMap);
        mCrmOptionsVO = mGson.fromJson(jsonOld, CrmOptionsVO.class);
        if (mCrmOptionsVO != null && "200".equals(mCrmOptionsVO.getCode())) {
            executeSuccess();
        }

    }

    public boolean getOptionsData() {
        String jsonStr = HttpClientUtil.getRequest(this, mUrl, mparamsMap);
        mCrmOptionsVO = mGson.fromJson(jsonStr, CrmOptionsVO.class);
        // saveJsonCache("http://cp.beisheng.wang/dev_1.2.5/", mparamsMap, jsonStr);
        setCacheStrTime(mUrl, mparamsMap, jsonStr, 60);
        return true;
    }

    @Override
    public void executeSuccess() {
        super.executeSuccess();
    }

    @Override
    public void executeFailure() {
        super.executeFailure();
    }

}
