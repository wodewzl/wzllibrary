
package com.bs.bsims.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.BossNeedDoAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.NeedDoVO;
import com.bs.bsims.utils.HttpClientUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class BossNeedDoActivity extends BaseActivity implements OnClickListener {

    private ListView mListView;
    private BossNeedDoAdapter mAdapter;
    private NeedDoVO mNeedDoVO;

    private boolean mFlag = true;
    private String mFristid, mLastid;
    private Boolean canClickFlag = true;// 解决连续点“更多”可能会出现的异常

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.boss_need_do, mContentLayout);
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    @Override
    public void updateUi() {

    }

    @Override
    public void initView() {
        mTitleTv.setText("待办事项");
        mListView = (ListView) findViewById(R.id.list_view);
        mAdapter = new BossNeedDoAdapter(this);
        mListView.setAdapter(mAdapter);
        initData();
    }

    public void initData() {
        Intent intent = this.getIntent();
    }

    @Override
    public void bindViewsListener() {

        mOkTv.setOnClickListener(this);
    }

    @Override
    public void executeSuccess() {
        super.executeSuccess();
        mAdapter.updateData(mNeedDoVO.getArray());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void executeFailure() {

        // 列表展示的时候不能调用父类
        super.isRequestFinish();
        mAdapter.notifyDataSetChanged();

        // 不适合只隐藏列表，适合隐藏整个布局;
        if (mNeedDoVO == null) {
            super.showNoNetView();
        } else {
            mAdapter.updateData(new ArrayList<NeedDoVO>());
        }
    }

    public boolean getData() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.BOSS_NEED_DO, map);
            mNeedDoVO = gson.fromJson(jsonStrList, NeedDoVO.class);
            if (Constant.RESULT_CODE.equals(mNeedDoVO.getCode())) {
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
        intent.setClass(this, ScheduleActivity.class);// 日程
        this.startActivity(intent);
    }
}
