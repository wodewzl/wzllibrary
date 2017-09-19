
package com.bs.bsims.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.StatisticsArticlesDetailAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.StatisticsArticlesVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.view.BSRefreshListView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StatisticsArticlesDetailActivity extends BaseActivity implements OnClickListener {

    private ImageView mLineImg;
    private TextView mImgBack;
    private TextView mNmae, mType, mMoney, mNumber;
    private String mState = "1";
    private String mGid;
    private String mDate;
    private StatisticsArticlesVO mStatisticsArticlesVO;
    private BSRefreshListView mListView;
    private StatisticsArticlesDetailAdapter mAdapter;
    private List<StatisticsArticlesVO> mList;
    private boolean mRefresh = true;

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.statistics_articles_detail, null);
        mContentLayout.addView(layout);
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    @Override
    public void updateUi() {

    }

    @Override
    public void executeSuccess() {
        StatisticsArticlesVO vo = mStatisticsArticlesVO.getArray();
        mList = vo.getGoods();
        mAdapter.updateData(mList);

        mHeadLayout.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);

        mNmae.setText(mList.get(0).getG_name());
        // mType.setText();
        mMoney.setText("总计：" + CommonUtils.countNumber(vo.getTotalprice()));
        mNumber.setText(vo.getTotalnum() + mList.get(0).getG_company());
        mType.setText(vo.getGoodtype());
    }

    @Override
    public void executeFailure() {
        mListView.onRefreshComplete();
        mHeadLayout.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void initView() {
        mTitleTv.setText("用品统计");
        mImgBack = (TextView) findViewById(R.id.head_back);

        mNmae = (TextView) findViewById(R.id.name);
        mType = (TextView) findViewById(R.id.type);
        mMoney = (TextView) findViewById(R.id.money);
        mNumber = (TextView) findViewById(R.id.number);

        mListView = (BSRefreshListView) findViewById(R.id.listview);
        mAdapter = new StatisticsArticlesDetailAdapter(this);
        mListView.setAdapter(mAdapter);
        mList = new ArrayList<StatisticsArticlesVO>();

        Intent intent = this.getIntent();
        mState = intent.getStringExtra("state");
        mGid = intent.getStringExtra("gid");
        mDate = intent.getStringExtra("date");
    }

    @Override
    public void bindViewsListener() {
        mImgBack.setOnClickListener(this);
    }

    public boolean getData() {
        try {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put("state", mState);
            map.put("gid", mGid);
            map.put("d", mDate);
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() +
                    Constant.STATISTICS_ARTICLES_DETAIL, map);
            Gson gson = new Gson();
            mStatisticsArticlesVO = gson.fromJson(jsonStr, StatisticsArticlesVO.class);
            if (Constant.RESULT_CODE.equals(mStatisticsArticlesVO.getCode())) {
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_back:
                this.finish();
                break;
            default:
                break;
        }
    }

}
