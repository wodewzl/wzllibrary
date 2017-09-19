/**
 * 
 */

package com.bs.bsims.activity;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.R.layout;
import com.bs.bsims.adapter.JournalFootPrintAdapter;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.JournalFootPrintModel;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.UrlUtil;
import com.bs.bsims.view.BSRefreshListView;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * BS北盛最帅程序员 Copyright (c) 2016 湖北北盛科技有限公司
 * 
 * @author 梁骚侠
 * @date 2016-3-1
 * @version 1.22
 */
public class JournalFootPrintActivity extends BaseActivity {

    private TextView mTextView;
    private BSRefreshListView lSListView;
    private JournalFootPrintAdapter mJournalAdapter;
    private Context mContext;
    private JournalFootPrintModel footPrintModel;
    private String mTime="" ;
    private String uid="";

    @Override
    public void baseSetContentView() {
        // TODO Auto-generated method stub
        View view = getLayoutInflater().inflate(R.layout.ac_ext_home_task, mContentLayout);
        mContext = this;
    }

    @Override
    public boolean getDataResult() {
        // TODO Auto-generated method stub
        return getData();
    }

    /**
     * @return
     */
    private boolean getData() {
        // TODO Auto-generated method stub
        Gson gson = new Gson();
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("time", mTime);
        paramsMap.put("uid", uid);
        String urlStr = UrlUtil.getUrlByMap1(Constant.JOURNAL_FOOTERPRINT,
                paramsMap);
        String jsonUrlStr;
        try {
            jsonUrlStr = HttpClientUtil.get(urlStr, Constant.ENCODING).trim();
            footPrintModel = gson.fromJson(jsonUrlStr, JournalFootPrintModel.class);
            if (footPrintModel.getCode().equals("200")) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void updateUi() {
        // TODO Auto-generated method stub
        if (null != footPrintModel && null != footPrintModel.getArray()) {

            mJournalAdapter.updateData(footPrintModel.getArray());
        }
    }

    /*
     * (non-Javadoc)
     * @see BaseActivity#executeFailure()
     */
    @Override
    public void executeFailure() {
        // TODO Auto-generated method stub
        super.executeFailure();
        if (null != footPrintModel) {
            CommonUtils.setNonetContent(mContext, mLoading,"没有相关信息");
        }

    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        lSListView = (BSRefreshListView) findViewById(R.id.listView_tasklist_add);
        mJournalAdapter = new JournalFootPrintAdapter(mContext);
        lSListView.setAdapter(mJournalAdapter);
        mTextView = (TextView) findViewById(R.id.record_time);
        mTextView.setVisibility(View.VISIBLE);
        mTime =getIntent().getStringExtra("time");
        uid = getIntent().getStringExtra("uid");
        mTextView.setText(mTime);
        mTitleTv.setText("个人足迹");
    }

    @Override
    public void bindViewsListener() {
        // TODO Auto-generated method stub

    }

}
