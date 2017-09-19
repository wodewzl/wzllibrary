
package com.beisheng.synews.activity;

import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckedTextView;
import android.widget.ImageView;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.synews.application.AppApplication;
import com.beisheng.synews.fragment.BrokeAllFragment;
import com.beisheng.synews.fragment.BrokeMyFragment;
import com.im.zhsy.R;

public class BrokeActivity extends BaseActivity implements OnClickListener {
    public String mPage = "1";// 用来存储数据的，1为默认第一页，不是只有一页
    private CheckedTextView mNewTv, mMyTv;
    private ImageView mPublishImg;
    private BrokeAllFragment mBrokeAllFragment;
    private BrokeMyFragment mBrokeMyFragment;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.broke_activity, mBaseContentLayout);
    }

    @Override
    public boolean getDataResult() {
        return true;
    }

    @Override
    public void initView() {
        mBaseTitleTv.setText("爆料");
        mNewTv = (CheckedTextView) findViewById(R.id.new_tv);
        mMyTv = (CheckedTextView) findViewById(R.id.my_tv);
        mPublishImg = (ImageView) findViewById(R.id.publish_img);
        showFragment(R.id.new_tv);
    }

    @Override
    public void bindViewsListener() {
        mNewTv.setOnClickListener(this);
        mMyTv.setOnClickListener(this);
        mPublishImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_tv:
                mNewTv.setChecked(true);
                mMyTv.setChecked(false);
                mNewTv.setTextColor(getResources().getColor(R.color.sy_title_color));
                mMyTv.setTextColor(getResources().getColor(R.color.C5));
                showFragment(v.getId());
                break;
            case R.id.my_tv:

                mNewTv.setChecked(false);
                mMyTv.setChecked(true);
                mNewTv.setTextColor(getResources().getColor(R.color.C5));
                mMyTv.setTextColor(getResources().getColor(R.color.sy_title_color));
                if (AppApplication.getInstance().getUserInfoVO() == null) {
                    openActivity(LoginActivity.class);
                    return;
                }
                showFragment(v.getId());
                break;
            case R.id.publish_img:
                if (AppApplication.getInstance().getUserInfoVO() == null) {
                    openActivity(LoginActivity.class);
                    return;
                }
                openActivityForResult(BrokeAddActivity.class, 1);
                break;

            default:
                break;
        }
    }

    public void showFragment(int id) {
        try {
            FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
            switch (id) {
                case R.id.new_tv:
                    if (null == mBrokeAllFragment) {
                        mBrokeAllFragment = new BrokeAllFragment();
                        transaction.add(R.id.fragment_content_layout, mBrokeAllFragment);
                    }
                    transaction.show(mBrokeAllFragment);
                    if (mBrokeMyFragment != null)
                        transaction.hide(mBrokeMyFragment);
                    break;
                case R.id.my_tv:

                    if (null == mBrokeMyFragment) {
                        mBrokeMyFragment = new BrokeMyFragment();
                        transaction.add(R.id.fragment_content_layout, mBrokeMyFragment);
                    }
                    transaction.show(mBrokeMyFragment);
                    if (mBrokeAllFragment != null)
                        transaction.hide(mBrokeAllFragment);
                    break;

                default:
                    break;
            }
            transaction.commitAllowingStateLoss();
        } catch (Exception e) {
        }
    }

}
