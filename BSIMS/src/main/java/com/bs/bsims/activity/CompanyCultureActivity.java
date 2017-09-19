
package com.bs.bsims.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.CompanyCultureAdapter;
import com.bs.bsims.model.CompanyCultureVO;

import java.util.ArrayList;
import java.util.List;

public class CompanyCultureActivity extends BaseActivity implements OnItemClickListener {
    private ListView mListView;
    private List<CompanyCultureVO> mList;
    private CompanyCultureAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // baseSetContentView();
        // initView();
        // initData();
        // bindViewsListener();
        // new ThreadUtil(callBack)
    }

    @Override
    public void initView() {
        mTitleTv.setText(R.string.company_culture);
        mListView = (ListView) findViewById(R.id.lv_cmopany_culture);

        mList = new ArrayList<CompanyCultureVO>();
        mAdapter = new CompanyCultureAdapter(this);
        mListView.setAdapter(mAdapter);
    }

    public boolean getData() {
        for (int i = 0; i < 6; i++) {
            CompanyCultureVO vo = new CompanyCultureVO();
            vo.setTitle("第" + i);
            vo.setState(i + "");
            vo.setTime("2015-04-21 18:39");
            mList.add(vo);
        }
        return true;
    }

    @Override
    public void bindViewsListener() {
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.setClass(this, CompanyCultureDetailActivity.class);
        this.startActivity(intent);
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    @Override
    public void updateUi() {
        mAdapter.updateData(mList);
    }

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.company_culture, null);
        mContentLayout.addView(layout);
    }
}
