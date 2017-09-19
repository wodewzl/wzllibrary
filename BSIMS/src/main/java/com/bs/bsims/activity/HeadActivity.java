
package com.bs.bsims.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.HeadAdapter;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.model.JournalDetailResltVO;

import java.util.List;

public class HeadActivity extends BaseActivity implements OnClickListener {
    private GridView mHeadGridView;
    private HeadAdapter mHeadAdapter;
    private JournalDetailResltVO mJournalDetailVO;
    private int mType = 0;

    public void initView() {
        mHeadGridView = (GridView) findViewById(R.id.send_second_person_gv);
        mHeadAdapter = new HeadAdapter(this, false);
        mHeadGridView.setAdapter(mHeadAdapter);
        initData();
    }

    public void initData() {
        List<EmployeeVO> list = (List<EmployeeVO>) this.getIntent().getSerializableExtra("head_list");
        mHeadAdapter.updateData(list);
        String title = (String) this.getIntent().getSerializableExtra("title");
        mTitleTv.setText("人员列表");
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

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.read_state, mContentLayout);
        Intent intent = getIntent();
        mJournalDetailVO = (JournalDetailResltVO) intent.getSerializableExtra("JournalDetailVO");
        mType = intent.getIntExtra("type", 1);

    }

    @Override
    public boolean getDataResult() {
        return true;
    }

    @Override
    public void updateUi() {

    }

    @Override
    public void bindViewsListener() {
        mHeadBackImag.setOnClickListener(this);

    }
}
