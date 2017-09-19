
package com.bs.bsims.activity;

import android.content.Intent;
import android.graphics.drawable.PaintDrawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.StatisticsArticlesAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.StatisticsArticlesVO;
import com.bs.bsims.time.ScreenInfo;
import com.bs.bsims.time.WheelMain;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CommonUtils.ResultCallback;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSDialog;
import com.bs.bsims.view.BSRefreshListView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class StatisticsArticlesActivity extends BaseActivity implements OnClickListener {
    private ImageView mLineImg;
    private TextView mImgBack;
    private TextView mTextView01, mTextView02, mMonth, mOutTv, mInTv, mPieChart, mYear;
    private int one = 0;
    private int two;
    private int index = 1;
    private String mState = "1";
    private String mDate;
    private StatisticsArticlesVO mStatisticsArticlesVO;
    private BSRefreshListView mListView;
    private StatisticsArticlesAdapter mAdapter;
    private List<StatisticsArticlesVO> mOutList, mInList;
    private boolean mRefresh = true;
    private RelativeLayout mSelectMonth;
    private PopupWindow mPopView;

    private String mInMonth, mOutMonth;
    public WheelMain wheelMain;
    private BSDialog mBSDialog;

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.statistics_articles, null);
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
        mHeadLayout.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);
        mListView.onRefreshComplete();
        StatisticsArticlesVO vo = mStatisticsArticlesVO.getArray();

        mOutTv.setText(CommonUtils.countNumber(vo.getStorage_total()).split("元")[0]);
        mInTv.setText(CommonUtils.countNumber(vo.getCollar_total()).split("元")[0]);

        if ("1".equals(mState)) {
            mInList = vo.getGoods();
            mAdapter.updateData(mInList);
        } else {
            mOutList = vo.getGoods();
            mAdapter.updateData(mOutList);
        }
    }

    @Override
    public void executeFailure() {
        mListView.onRefreshComplete();
        mHeadLayout.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);

        mOutTv.setText("0.00");
        mInTv.setText("0.00");
        if ("1".equals(mState)) {
            mInList = new ArrayList<StatisticsArticlesVO>();
            mAdapter.updateData(mInList);
        } else {
            mOutList = new ArrayList<StatisticsArticlesVO>();
            mAdapter.updateData(mOutList);
        }
    }

    @Override
    public void initView() {
        mTitleTv.setText("用品统计");
        mImgBack = (TextView) findViewById(R.id.head_back);
        // mPieChart = (TextView) findViewById(R.id.txt_comm_head_right1);
        mTextView01 = (TextView) findViewById(R.id.text_01);
        mTextView02 = (TextView) findViewById(R.id.text_02);
        mLineImg = (ImageView) findViewById(R.id.line_img);
        mMonth = (TextView) findViewById(R.id.month);
        mYear = (TextView) findViewById(R.id.year);

        mDate = DateUtils.getCurrentDate().substring(0, DateUtils.getCurrentDate().lastIndexOf("-"));
        mOutMonth = DateUtils.getCurrentDate().split("-")[1];
        mInMonth = DateUtils.getCurrentDate().split("-")[1];
        mMonth.setText(DateUtils.getCurrentDate().split("-")[1]);
        mOutTv = (TextView) findViewById(R.id.out_tv);
        mInTv = (TextView) findViewById(R.id.in_tv);

        mListView = (BSRefreshListView) findViewById(R.id.listview);
        mAdapter = new StatisticsArticlesAdapter(this);
        mListView.setAdapter(mAdapter);

        mInList = new ArrayList<StatisticsArticlesVO>();
        mOutList = new ArrayList<StatisticsArticlesVO>();

        mSelectMonth = (RelativeLayout) findViewById(R.id.select_month);
        mPopView = new PopupWindow(this);
        showPopView(mSelectMonth);
        Calendar calendar = Calendar.getInstance();
        mYear.setText(String.valueOf(calendar.get(Calendar.YEAR)) + "年");
        initData();
    }

    @Override
    public void bindViewsListener() {
        mTextView01.setOnClickListener(this);
        mTextView02.setOnClickListener(this);
        mImgBack.setOnClickListener(this);
        mSelectMonth.setOnClickListener(this);
        // mPieChart.setOnClickListener(this);
    }

    public boolean getData() {
        try {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put("state", mState);
            map.put("d", mDate);
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() +
                    Constant.STATISTICS_ARTICLES, map);
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
            case R.id.text_01:
                if (index != 1) {
                    startAnim(1);
                    index = 1;
                    mAdapter.updateData(mInList);
                    mState = "1";
                    mTextView01.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.statistics_in_select), null, null, null);
                    mTextView02.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.statistics_out_normal), null, null, null);
                }

                break;
            case R.id.text_02:
                if (index != 2) {
                    startAnim(2);
                    index = 2;
                    mState = "2";
                    mAdapter.updateData(mOutList);
                    mTextView01.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.statistics_in_normal), null, null, null);
                    mTextView02.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.statistics_out_select), null, null, null);

                }

                if (mRefresh) {
                    mListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
                    new ThreadUtil(this, this).start();
                    mRefresh = false;
                }

                break;
            case R.id.select_month:
                // if (mPopView.isShowing()) {
                // mPopView.dismiss();
                // } else {
                // mPopView.showAsDropDown(mSelectMonth);
                // }

                if (mBSDialog == null) {
                    mBSDialog = CommonUtils.initDateViewCallback(this, "请选择时间", mOkTv, 3, callback);
                } else {
                    mBSDialog.show();
                }
                break;

            case R.id.head_back:
                this.finish();
                break;

            case R.id.txt_comm_head_right1:
                Intent intent = new Intent();
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    public void startAnim(int dex) {
        Animation animation = null;
        switch (dex) {
            case 1:
                if (index == 2) {
                    animation = new TranslateAnimation(one, 0, 0, 0);
                }
                break;
            case 2:
                if (index == 1) {
                    animation = new TranslateAnimation(one, two, 0, 0);
                }
                break;

            default:
                break;
        }

        animation.setFillAfter(true);
        animation.setDuration(150);
        mLineImg.startAnimation(animation);
    }

    public void initData() {
        Display currDisplay = getWindowManager().getDefaultDisplay();// 获取屏幕当前分辨率
        int displayWidth = currDisplay.getWidth();
        int displayHeight = currDisplay.getHeight();
        two = displayWidth / 2; // 设置水平动画平移大小
        LayoutParams para = mLineImg.getLayoutParams();
        para.width = displayWidth / 2;
        mLineImg.setLayoutParams(para);

    }

    ResultCallback callback = new ResultCallback() {

        @Override
        public void callback(String str, int position) {
            mOkTv.setText(str);
            String date = str.substring(0, wheelMain.getTime().lastIndexOf("-"));
            String month = str.split("-")[1];

            if (month.length() == 1) {
                month = "0" + month;
            }
            if ("1".equals(mState)) {
                mInMonth = month;
            } else {
                mOutMonth = month;
            }
            mMonth.setText(month);
            mYear.setText(date.split("-")[0] + "年");

            mDate = str;
            mListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
            new ThreadUtil(StatisticsArticlesActivity.this, StatisticsArticlesActivity.this).start();
            mPopView.dismiss();
        }
    };

    public void showPopView(View anchor) {
        View view = View.inflate(this, R.layout.approval_pop_item_03, null);
        view.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                mPopView.dismiss();
                return false;
            }
        });
        view.findViewById(R.id.title_layout_01).setVisibility(View.GONE);
        view.findViewById(R.id.title_layout_02).setVisibility(View.GONE);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.title_layout_03);
        layout.addView(initDateView());
        Button okBt = (Button) view.findViewById(R.id.ok_bt);
        okBt.setVisibility(View.VISIBLE);
        okBt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String date = wheelMain.getTime().substring(0, wheelMain.getTime().lastIndexOf("-"));
                String month = wheelMain.getTime().split("-")[1];

                if (month.length() == 1) {
                    month = "0" + month;
                }
                if ("1".equals(mState)) {
                    mInMonth = month;
                } else {
                    mOutMonth = month;
                }
                mMonth.setText(month);
                mYear.setText(date.split("-")[0] + "年");

                mDate = wheelMain.getTime();
                mListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
                new ThreadUtil(StatisticsArticlesActivity.this, StatisticsArticlesActivity.this).start();
                mPopView.dismiss();
            }
        });
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mSelectMonth.measure(w, h);
        int height = mSelectMonth.getMeasuredHeight();
        int width = mSelectMonth.getMeasuredWidth();

        mPopView.setWidth(LayoutParams.MATCH_PARENT);
        mPopView.setHeight(LayoutParams.WRAP_CONTENT);
        mPopView.setContentView(view);
        mPopView.setFocusable(false);
        mPopView.setOutsideTouchable(true);
        mPopView.setBackgroundDrawable(new PaintDrawable());
    }

    public View initDateView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View timepickerview = inflater.inflate(R.layout.timepicker, null);
        ScreenInfo screenInfo = new ScreenInfo(this);
        // final WheelMain wheelMain = new WheelMain(timepickerview, false, false);
        wheelMain = new WheelMain(timepickerview, false, false);
        wheelMain.screenheight = screenInfo.getHeight();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        wheelMain.initDateTimePicker(year, month, 0, 0, 0);
        return timepickerview;
    }
}
