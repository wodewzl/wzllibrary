
package com.bs.bsims.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.HeadAdapter;

public class ReadStateActivity extends Activity implements OnClickListener {
    private GridView mSendScondPersonGv;
    private TextView mTitleTv, mBackTv;
    private TextView mHeadBackImag;
    private HeadAdapter mHeadAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_state);

        initView();
        initData();
        bindLinsters();
    }

    public void initView() {
        mSendScondPersonGv = (GridView) findViewById(R.id.send_second_person_gv);
        mTitleTv = (TextView) findViewById(R.id.txt_comm_head_activityName);
        mTitleTv.setText(R.string.sendTwoPerson);
        mBackTv = (TextView) findViewById(R.id.txt_head_left_back);
        mHeadBackImag = (TextView) findViewById(R.id.img_head_back);
    }

    public void initData() {
        mHeadAdapter = new HeadAdapter(this, 2);
        for (int i = 0; i < 7; i++) {
            // edit bypc 2015/5/11 10:35
            // PersonHeadVO headVO = new PersonHeadVO();
            // headVO.setName("张三" + i);
            // mHeadAdapter.mList.add(headVO);
        }
        mSendScondPersonGv.setAdapter(mHeadAdapter);
    }

    public void bindLinsters() {
        mBackTv.setOnClickListener(this);
        mHeadBackImag.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_head_back:
                this.finish();
                break;
            case R.id.txt_head_left_back:
                this.finish();
                break;

            default:
                break;
        }
    }
}
