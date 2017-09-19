
package com.bs.bsims.activity;

import android.view.View;
import android.widget.LinearLayout;

import com.bs.bsims.R;
import com.bs.bsims.adapter.WarnAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.WarnVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.view.BSRefreshListView;
import com.google.gson.Gson;

import java.util.HashMap;

public class WarnActivity extends BaseActivity {
    private BSRefreshListView mRefreshListView;
    private WarnVO mWarnVO;
    private WarnAdapter mWarnAdapter;
    private LinearLayout mNoContentLyaout;

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.notify, mContentLayout);
        // mContentLayout.addView(layout);
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    @Override
    public void updateUi() {
        // if (Constant.RESULT_CODE.equals(mWarnVO.getCode())) {
        // mWarnAdapter.updateData(mWarnVO.getArray());
        // } else {
        // if (mWarnVO != null) {
        // mNoContentLyaout.setVisibility(View.VISIBLE);
        //
        // mLoading.setVisibility(View.GONE);
        // mContentLayout.setVisibility(View.VISIBLE);
        // mLoadingLayout.setVisibility(View.GONE);
        //
        // } else {
        // CommonUtils.setNonetIcon(this, mLoading);
        // }
        // }

    }

    @Override
    public void executeSuccess() {
        super.executeSuccess();
        mWarnAdapter.updateData(mWarnVO.getArray());
    }

    @Override
    public void executeFailure() {
        if (mWarnVO != null) {
            mNoContentLyaout.setVisibility(View.VISIBLE);

            mLoading.setVisibility(View.GONE);
            mContentLayout.setVisibility(View.VISIBLE);
            mLoadingLayout.setVisibility(View.GONE);

        } else {
            CommonUtils.setNonetIcon(this, mLoading,this);
        }
    }

    @Override
    public void initView() {
        mRefreshListView = (BSRefreshListView) findViewById(R.id.lv_refresh);
        mTitleTv.setText("预警");

        mWarnAdapter = new WarnAdapter(this);
        mRefreshListView.setAdapter(mWarnAdapter);
        mNoContentLyaout = (LinearLayout) findViewById(R.id.no_content_layout);
    }

    @Override
    public void bindViewsListener() {

    }

    public boolean getData() {
        try {
            HashMap<String, String> warnmap = new HashMap<String, String>();
            warnmap.put("userid", BSApplication.getInstance().getUserId());
            warnmap.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonWarnStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.WARN_URL, warnmap);
            Gson gson = new Gson();
            mWarnVO = gson.fromJson(jsonWarnStr, WarnVO.class);

            if (Constant.RESULT_CODE.equals(mWarnVO.getCode())) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
