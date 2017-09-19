
package com.bs.bsims.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;

import com.bs.bsims.R;
import com.bs.bsims.fragment.CrmBussinesFragment1;
import com.bs.bsims.fragment.CrmBussinesFragment3;
import com.bs.bsims.model.CrmBussinesListindexVo;

public class CrmBussinesDetailActivity extends BaseActivity {

    private CrmBussinesFragment1 mAllFragment = null;
    private CrmBussinesListindexVo vBussinesListindexVo;
    private Context mContext;
    private String mBussinesKey;// 判断是否是商机或者是动态
    private CrmBussinesFragment3 myCollectionFragment = null;
    public static CrmBussinesDetailActivity aCrmBussinesDetailActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        View layout = View.inflate(this, R.layout.ac_ext_sharedfilesd_grouphome,
                mContentLayout);
        aCrmBussinesDetailActivity = this;
        findViewById(R.id.edit_single_search).setVisibility(View.GONE);
        findViewById(R.id.ac_ext_sharedfilesd_grouphome_top_indicator).setVisibility(View.GONE);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        mBussinesKey = getIntent().getStringExtra("mBussinesKey");
        // 商机详情

        if (mBussinesKey.equals("1")) {
            vBussinesListindexVo = (CrmBussinesListindexVo) getIntent().getSerializableExtra(
                    "crmBussineLivo1");
            mAllFragment = new CrmBussinesFragment1(vBussinesListindexVo);
            mTitleTv.setText("商机详情");
            if (vBussinesListindexVo.getCrmEdit().equals("2")) {
                mOkTv.setText("编辑");
            }
            mContext = this;
            mOkTv.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    Intent i = new Intent();
                    i.putExtra("vBussinesListindexVo", vBussinesListindexVo);
                    if (null != getIntent().getStringExtra("stateUtilthread")) {
                        i.putExtra("stateUtilthread", getIntent().getStringExtra("stateUtilthread"));
                    }
                    i.setClass(mContext, CrmBusinessAddInfoMsgActivity.class);
                    startActivity(i);
                }
            });
            transaction.add(R.id.id_content, mAllFragment);
        }

        else if (mBussinesKey.equals("2")) {
            myCollectionFragment = new CrmBussinesFragment3(getIntent().getStringExtra("bid"));
            transaction.add(R.id.id_content, myCollectionFragment);
            mTitleTv.setText("商机动态");
        }

        try {
            transaction.commitAllowingStateLoss();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    @Override
    public void baseSetContentView() {
        // TODO Auto-generated method stub

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

    }

    @Override
    public void bindViewsListener() {
        // TODO Auto-generated method stub

    }

}
