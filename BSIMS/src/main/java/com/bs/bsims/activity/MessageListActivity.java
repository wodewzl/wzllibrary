
package com.bs.bsims.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.MessageListAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.fragment.MessageAllListFragment;
import com.bs.bsims.fragment.MessageMyListFragment;
import com.bs.bsims.model.MessageListVO;
import com.bs.bsims.utils.CommonUtils;

public class MessageListActivity extends BaseActivity implements OnClickListener {

    private MessageListAdapter mMyAdapter, mAllAdapter;
    private int mIndex = 1;
    private ImageView mLineImg;
    private int mOne = 0;
    private int mTwo;
    private TextView mTextView01, mTextView02;
    private TextView mText01Count, mText02Count;
    private String mType;
    private RelativeLayout mLayout01, mLayout02;

    private MessageListVO mMyVO, mAllVO;
    private boolean mCanClickFlag = true;

    private String mRelated = "1";
    private MessageMyListFragment mFragmentMy;
    private MessageAllListFragment mFragmentAll;
    private FragmentManager mFragmentManager;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.message_list, mContentLayout);
    }

    @Override
    public boolean getDataResult() {
        return true;
    }

    @SuppressLint("NewApi")
    @Override
    public void initView() {
        mTextView01 = (TextView) findViewById(R.id.text_01);
        mTextView02 = (TextView) findViewById(R.id.text_02);
        mLineImg = (ImageView) findViewById(R.id.line_img);
        mText01Count = (TextView) findViewById(R.id.text_01_count);
        mText02Count = (TextView) findViewById(R.id.text_02_count);
        mText01Count.setBackground(CommonUtils.setBackgroundShap(this, 100, R.color.C9, R.color.C9));
        mText02Count.setBackground(CommonUtils.setBackgroundShap(this, 100, R.color.C9, R.color.C9));
        mLayout01 = (RelativeLayout) findViewById(R.id.layout_01);
        mLayout02 = (RelativeLayout) findViewById(R.id.layout_02);

        Display currDisplay = getWindowManager().getDefaultDisplay();// 获取屏幕当前分辨率
        int displayWidth = currDisplay.getWidth();
        int displayHeight = currDisplay.getHeight();
        mTwo = displayWidth / 2; // 设置水平动画平移大小
        LayoutParams para = mLineImg.getLayoutParams();
        para.width = displayWidth / 2;
        mLineImg.setLayoutParams(para);
        mFragmentManager = getSupportFragmentManager();
        initData();
        showFragment(R.id.layout_01);

        if ("0".equals(BSApplication.getInstance().getUserFromServerVO().getIsboss())) {
            this.findViewById(R.id.type_layout).setVisibility(View.GONE);
            mLineImg.setVisibility(View.GONE);
        }
    }

    private void initData() {
        Intent intent = this.getIntent();
        mType = intent.getStringExtra("type");
        String titleName = intent.getStringExtra("name");
        mTitleTv.setText(titleName);
        String relatedCount = CommonUtils.isNormalCount(intent.getStringExtra("relatedCount"));
        if ("0".equals(relatedCount) || relatedCount == null) {
            mText01Count.setVisibility(View.GONE);
        }
        mText01Count.setText(relatedCount);

        String allCount = CommonUtils.isNormalCount(intent.getStringExtra("allCount"));
        if ("0".equals(allCount) || allCount == null) {
            mText02Count.setVisibility(View.GONE);
        }
        mText02Count.setText(allCount);
    }

    public void startAnim(int dex) {
        Animation animation = null;
        switch (dex) {
            case 1:
                if (mIndex == 2) {
                    animation = new TranslateAnimation(mOne, 0, 0, 0);
                }
                break;
            case 2:
                if (mIndex == 1) {
                    animation = new TranslateAnimation(mOne, mTwo, 0, 0);
                }
                break;

            default:
                break;
        }

        animation.setFillAfter(true);
        animation.setDuration(150);
        mLineImg.startAnimation(animation);
    }

    @Override
    public void bindViewsListener() {
        mLayout01.setOnClickListener(this);
        mLayout02.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_01:
                if (mIndex != 1) {
                    startAnim(1);
                    mIndex = 1;
                    showFragment(v.getId());
                    mTextView01.setTextColor(this.getResources().getColor(R.color.C7));
                    mTextView02.setTextColor(this.getResources().getColor(R.color.C4));
                }

                break;
            case R.id.layout_02:
                if (mIndex != 2) {
                    startAnim(2);
                    mIndex = 2;
                    showFragment(v.getId());
                    mTextView01.setTextColor(this.getResources().getColor(R.color.C4));
                    mTextView02.setTextColor(this.getResources().getColor(R.color.C7));
                    mText02Count.setVisibility(View.GONE);
                }

                break;
        }
    }

    @Override
    public void updateUi() {

    }

    public void showFragment(int id) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        switch (id) {
            case R.id.layout_02:

                if (null == mFragmentAll) {
                    mFragmentAll = new MessageAllListFragment(mType);
                    transaction.add(R.id.fragment_content_layout, mFragmentAll);
                }
                transaction.show(mFragmentAll);
                if (mFragmentMy != null)
                    transaction.hide(mFragmentMy);
                break;
            case R.id.layout_01:

                if (null == mFragmentMy) {
                    mFragmentMy = new MessageMyListFragment(mType);
                    transaction.add(R.id.fragment_content_layout, mFragmentMy);
                    mFragmentMy.setTextView(mText01Count);
                }
                transaction.show(mFragmentMy);
                if (mFragmentAll != null)
                    transaction.hide(mFragmentAll);

                break;

            default:
                break;
        }
        transaction.commitAllowingStateLoss();
    }
}
