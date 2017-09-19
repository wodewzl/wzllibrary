
package com.bs.bsims.activity;

import android.os.Bundle;
import android.view.View;

import com.bs.bsims.R;
import com.bs.bsims.adapter.DiscussLeadAdapter;
import com.bs.bsims.model.DiscussLeadVO;
import com.bs.bsims.view.BSListView;

public class CompanyCultureDetailActivity extends BaseActivity {
    private BSListView mDiscussLv;
    private DiscussLeadAdapter mDiscussAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.nn);
        // baseSetContentView();
        // initView();
        // initData();
        // bindViewsListeners();
    }

    @Override
    public void initView() {
        mDiscussLv = (BSListView) findViewById(R.id.list_view);
        mDiscussAdapter = new DiscussLeadAdapter(this);
        mDiscussLv.setAdapter(mDiscussAdapter);

        mTitleTv.setText(R.string.company_culture);
    }

    private boolean getData() {

        // 评论内容
        for (int i = 0; i < 5; i++) {
            DiscussLeadVO discussVO = new DiscussLeadVO();
            discussVO.setFullname("李四" + i);
            discussVO.setPraise(i + "");
            discussVO.setDecline((i + 1) + "");
            discussVO.setTime("1429008963");
            discussVO.setContent("找房子找房子找房子找房子找房子找房子找房子找房子找房子找房子找房子找房子找房子找房子找房子找房子找房子找房子找房子找房子");
            mDiscussAdapter.mList.add(discussVO);
        }
        return true;
    }

    @Override
    public void bindViewsListener() {

    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    @Override
    public void updateUi() {
        // mDiscussAdapter.notifyDataSetChanged();
    }

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.company_culture_detail, null);
        mContentLayout.addView(layout);
    }
}
