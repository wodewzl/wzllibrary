
package com.bs.bsims.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.CrmStatisticsVisitorCustomersAdapter;
import com.bs.bsims.adapter.CrmStatisticsVisitorUsersAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.CrmStatisticsVisitorVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.google.gson.Gson;

import java.util.HashMap;

@SuppressLint("NewApi")
public class CrmStatisticsVisitorActivity extends BaseActivity implements OnTouchListener, OnScrollListener, OnItemClickListener {
    private CrmStatisticsVisitorVO mVisitorVO;
    private ListView mUsersLv, mCustomersLv;
    private CrmStatisticsVisitorUsersAdapter mUsersAdapter;
    private CrmStatisticsVisitorCustomersAdapter mCustomersAdapter;
    private ImageView mPositionIv;
    private int downY;
    private float mItemHight;// 每条的高度
    private float mDrableHight;// 游标的高度
    private float mCurrentY;// 当前游标停留的Y值
    private boolean mMoveState = true;// true 为下滑的，false为上滑动
    private int mFirstVisiblePosition;
    private int mClickPosition;
    private int mItemPosition;
    private TextView mAllCountTv, mDayCountTv;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.crm_statistics_visitor, mContentLayout);

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
        super.executeSuccess();
        mUsersAdapter.updateData(mVisitorVO.getArray());
        mCustomersAdapter.updateData(mVisitorVO.getArray().get(0).getCustomers());
    }

    @Override
    public void executeFailure() {
        super.executeFailure();
        if (mVisitorVO == null) {
            super.showNoNetView();
        } else {
            super.showNoContentView();
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void initView() {

        mTitleTv.setText("本月跟单统计");
        mUsersLv = (ListView) findViewById(R.id.users_lv);
        mCustomersLv = (ListView) findViewById(R.id.customers_lv);
        mUsersAdapter = new CrmStatisticsVisitorUsersAdapter(this);
        mCustomersAdapter = new CrmStatisticsVisitorCustomersAdapter(this);
        mUsersLv.setAdapter(mUsersAdapter);
        mCustomersLv.setAdapter(mCustomersAdapter);
        mPositionIv = (ImageView) findViewById(R.id.position_iv);
        mDrableHight = (float) (this.getResources().getDrawable(R.drawable.crm_potion).getMinimumHeight() * 1.0 / 2);
        mItemHight = (float) (CommonUtils.getViewHigh(mUsersLv) * 1.0 / 5 + mDrableHight);
        mPositionIv.setTranslationY(mItemHight - mDrableHight * 2);
        mCurrentY = mItemHight - mDrableHight * 2;
        mAllCountTv = (TextView) findViewById(R.id.all_count_tv);
        mDayCountTv = (TextView) findViewById(R.id.day_count_tv);
        initData();
    }

    public void initData() {
        Intent intent = this.getIntent();
        if ("1".equals(intent.getStringExtra("type"))) {
            mTitleTv.setText("上月跟单统计");
        } else if ("2".equals(intent.getStringExtra("type"))) {
            mTitleTv.setText("本月跟单统计");
        } else {
            mTitleTv.setText("本季度跟单统计");
        }

        if (intent.getStringExtra("visitCount") != null)
            mAllCountTv.setText(intent.getStringExtra("visitCount"));
        if (intent.getStringExtra("visitPerday") != null)
            mDayCountTv.setText(intent.getStringExtra("visitPerday"));
    }

    @Override
    public void bindViewsListener() {
        mPositionIv.setOnTouchListener(this);
        mUsersLv.setOnScrollListener(this);
        // mUsersLv.setOnTouchListener(new OnTouchListener() {
        //
        // @Override
        // public boolean onTouch(View v, MotionEvent arg1) {
        // v.getParent().requestDisallowInterceptTouchEvent(true);
        // return false;
        // }
        // });
        mUsersLv.setOnItemClickListener(this);
    }

    public void updateCustomers() {
        int count = Math.abs((int) (mCurrentY / mItemHight));
        if (mUsersAdapter.mList.size() > mFirstVisiblePosition + count) {
            mCustomersAdapter.updateData(mUsersAdapter.mList.get(mFirstVisiblePosition + count).getCustomers());
            mClickPosition = mFirstVisiblePosition + count;
        } else {
            mCustomersAdapter.updateData(mUsersAdapter.mList.get(mUsersAdapter.mList.size() - 1).getCustomers());
            mClickPosition = mUsersAdapter.mList.size() - 1;
            // mPositionIv.setTranslationY(mCurrentY - Math.abs(mItemHight) * (count -
            // mUsersAdapter.mList.size() + 1));
            ObjectAnimator oa1 = ObjectAnimator.ofFloat(mPositionIv, "translationY", mCurrentY, mCurrentY - Math.abs(mItemHight) * (count - mUsersAdapter.mList.size() + 1)).setDuration(500);
            oa1.start();
            mCurrentY = mCurrentY - Math.abs(mItemHight) * (count - mUsersAdapter.mList.size() + 1);
        }
    }

    public boolean getData() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> mapList = new HashMap<String, String>();
            mapList.put("userid", BSApplication.getInstance().getUserId());
            mapList.put("type", getIntent().getStringExtra("type"));
            mapList.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.CRM_STATISTICS_VISITOR, mapList);
            mVisitorVO = gson.fromJson(jsonStrList, CrmStatisticsVisitorVO.class);
            if (Constant.RESULT_CODE.equals(mVisitorVO.getCode())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int everyHight = mUsersLv.getHeight();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) event.getRawY();

                break;

            case MotionEvent.ACTION_MOVE:
                float dy = event.getRawY() - downY;

                if (dy < 0) {
                    mMoveState = false;
                    v.setTranslationY(mCurrentY + dy);
                } else {
                    mMoveState = true;
                    v.setTranslationY(mCurrentY + dy);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_UP:
                // 获取已经移动的
                float ddy = v.getTranslationY();
                // if (ddy > mUsersLv.getMeasuredHeight())
                // ddy = mUsersLv.getMeasuredHeight();
                // if (ddy < 0)
                // ddy = Math.abs(mItemHight) - mDrableHight * 2;
                if (ddy + mDrableHight > mUsersLv.getMeasuredHeight() || ddy < 0) {
                    mPositionIv.setTranslationY(mCurrentY);
                    return true;
                }

                float count = Math.abs((Math.abs(ddy - mCurrentY) / mItemHight));
                if (mMoveState) {
                    mItemHight = Math.abs(mItemHight);
                } else {
                    mItemHight = 0 - Math.abs(mItemHight);
                }

                if (0 <= count && count < 0.5) {
                    mPositionIv.setTranslationY(mCurrentY);
                } else if (0.5 <= count && count < 1.5) {
                    mPositionIv.setTranslationY(mCurrentY + mItemHight * 1);
                    mCurrentY = mCurrentY + mItemHight * 1;
                } else if (1.5 <= count && count < 2.5) {
                    mPositionIv.setTranslationY(mCurrentY + mItemHight * 2);
                    mCurrentY = mCurrentY + mItemHight * 2;
                } else if (2.5 <= count && count < 3.5) {
                    mPositionIv.setTranslationY(mCurrentY + mItemHight * 3);
                    mCurrentY = mCurrentY + mItemHight * 3;
                } else {
                    mPositionIv.setTranslationY(mCurrentY + mItemHight * 3);
                    mCurrentY = mCurrentY + mItemHight * 3;
                }
                updateCustomers();

                break;
        }
        return true;
    }

    @Override
    public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {

    }

    @Override
    public void onScrollStateChanged(AbsListView arg0, int scrollState) {
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
            // 获取可见位置
            mFirstVisiblePosition = mUsersLv.getFirstVisiblePosition();
            mUsersLv.setSelection(mFirstVisiblePosition);
            updateCustomers();
            // 判断是否是最后一页
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        int position = (int) arg3;
        if (mClickPosition == 0) {
            mClickPosition = position - mFirstVisiblePosition;
        } else {
            mClickPosition = position - Math.abs(mClickPosition);
        }
        mCustomersAdapter.updateData(mUsersAdapter.mList.get(position).getCustomers());
        ObjectAnimator oa1 = ObjectAnimator.ofFloat(mPositionIv, "translationY", mCurrentY, mCurrentY + Math.abs(mItemHight) * mClickPosition).setDuration(500);
        oa1.start();
        mCurrentY = mCurrentY + Math.abs(mItemHight) * mClickPosition;
        mClickPosition = position;

    }
}
