
package com.bs.bsims.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.EXTWorkAttendanceEveryoneDetailAdapter;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.view.BSRefreshListView;

import java.util.List;

/**
 * @author peck
 * @Description: 考勤详情 中的单项详情
 * @date 2015-5-31 上午11:33:29
 * @email 971371860@qq.com
 * @version V1.0
 */

public class EXTWorkAttendanceEveryoneDetailActivity extends BaseActivity {

    private TextView mEXTWAEveryoneDetailTxt;

    private BSRefreshListView mEXTWAEveryoneDetailRefreshListview;
    private EXTWorkAttendanceEveryoneDetailAdapter mEXTWAEveryoneDetailAdapter;
    private Context mContext;
    private List<String> mEXTWAEveryoneList;// 组列表
    private String topDate;
    private String topTitleType;
    private String topTitleTypeInfo;

    @Override
    public void baseSetContentView() {
        // TODO Auto-generated method stub
        mContext = this;
        View layout = View.inflate(this,
                R.layout.ac_ext_work_attendance_everyone_detail, null);
        mContentLayout.addView(layout);
    }

    @Override
    public boolean getDataResult() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void updateUi() {
        // TODO Auto-generated method stub

    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        mTitleTv.setText("聂世明");
        mEXTWAEveryoneDetailTxt = (TextView) findViewById(R.id.work_attendance_everyone_detail_txt);
        mEXTWAEveryoneDetailRefreshListview = (BSRefreshListView) findViewById(R.id.work_attendance_everyone_detail_refreshlistview);
        mEXTWAEveryoneDetailAdapter = new EXTWorkAttendanceEveryoneDetailAdapter(
                mContext);

        // CommonDateUtils.stringToDate(dateString)

        mEXTWAEveryoneDetailRefreshListview
                .setAdapter(mEXTWAEveryoneDetailAdapter);
    }

    @Override
    public void bindViewsListener() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Intent getIntent = getIntent();
        topDate = getIntent.getStringExtra("topDate");
        topTitleType = getIntent.getStringExtra("topTitleType");
        topTitleTypeInfo = getIntent.getStringExtra("topTitleTypeInfo");
        mEXTWAEveryoneList = getIntent.getStringArrayListExtra("data");
        mEXTWAEveryoneList = getIntent.getStringArrayListExtra("detailList");

        mEXTWAEveryoneDetailAdapter
                .setmEXTWAEveryoneItemList(mEXTWAEveryoneList);
        String date = DateUtils.getLastMonth();
        date = DateUtils.getCurrentDate1MY1111N();
        date = DateUtils.getLastMonthYYYYMM();
        date = DateUtils.getCurrentDate111122();

        mEXTWAEveryoneDetailTxt.setText(date);
        // mTitleTv.setText(topTitleType + topTitleTypeInfo);
        mTitleTv.setText(topTitleType);
    }

}
