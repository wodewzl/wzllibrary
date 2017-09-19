
package com.bs.bsims.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.fragment.CreativeFragment;
import com.bs.bsims.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class CreativeActivity extends FragmentActivity implements OnClickListener {
    private CreativeFragment mCreativeFragment;
    private TextView mTitleTv;
    private TextView mBackImage;
    private String mType;
    private String mIsboss;
    private String mIsall;
    private String mIsAdd;
    private String mUnread;
    private String mToDo;

    private TextView mRightTv;
    private PopupWindow mOkPop;
    private List<PopupWindow> mListpop = new ArrayList<PopupWindow>();
    private View mBottomLine;
    private LinearLayout mAddFragment;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.creative);
        try {
            // 透明状态栏 5.0以后才可以显示
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        initData();
        mCreativeFragment = new CreativeFragment(mType, mIsboss, mIsall, mUnread, mToDo);
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.add_fragment, mCreativeFragment);
        transaction.commitAllowingStateLoss();

        mTitleTv = (TextView) findViewById(R.id.txt_comm_head_activityName);

        mTitleTv.setText("意见分享");
        mBackImage = (TextView) findViewById(R.id.img_head_back);
        mRightTv = (TextView) findViewById(R.id.txt_comm_head_right);
        mBottomLine = findViewById(R.id.bottom_line);

        if (CommonUtils.getLimitsPublish(Constant.LIMITS_PUBLISH003)) {
            mRightTv.setText("发布");
            mRightTv.setVisibility(View.VISIBLE);
        } else {
            mRightTv.setVisibility(View.GONE);
        }

        bindViewsListener();

        // mAddFragment = (LinearLayout) this.findViewById(R.id.add_fragment);
        // mAddFragment.postDelayed(new Runnable() {
        //
        // @Override
        // public void run() {
        // if (CommonUtils.showLead(CreativeActivity.this, "CreativeActivity")) {
        // initLeadView();
        // }
        // }
        // }, 500);

    }

    public void bindViewsListener() {
        mBackImage.setOnClickListener(this);
        mRightTv.setOnClickListener(this);
    }

    public void initData() {
        Intent intent = this.getIntent();
        mType = intent.getStringExtra("type");
        mIsboss = intent.getStringExtra("isboss");
        mIsall = intent.getStringExtra("isall");
        mIsAdd = intent.getStringExtra("isadd");
        mUnread = intent.getStringExtra("unread");
        mToDo = intent.getStringExtra("todo");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_head_back:
                this.finish();
                break;
            case R.id.txt_comm_head_right:
                Intent intent = new Intent();
                intent.putExtra("type", mType);
                intent.setClass(this, CreativeIdeaNewActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

    }

    public void initLeadView() {
        LinearLayout.LayoutParams okTvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);// 定义文本显示组件
        okTvParams.rightMargin = CommonUtils.dip2px(this, 10);
        LinearLayout.LayoutParams okImgParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, CommonUtils.dip2px(this, 180));// 定义文本显示组件
        okImgParams.gravity = Gravity.RIGHT;
        okImgParams.rightMargin = CommonUtils.dip2px(this, 25) + CommonUtils.getViewWidth(mBottomLine) / 2;
        mOkPop = CommonUtils.leadPop(this, mBottomLine, "亲，有需要了可以发布一下哦", okTvParams, okImgParams, 1);
        mListpop.add(mOkPop);
        CommonUtils.okLeadPop(this, mAddFragment, mListpop, 0);
    }
}
