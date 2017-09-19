
package com.bs.bsims.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.HeadAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.ScheduleDetailVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.view.BSCircleImageView;
import com.bs.bsims.view.BSGridView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
/**
 * 日程详情界面
 */
@SuppressLint("NewApi")
public class ScheduleDetailActivity extends BaseActivity {

    private TextView mPersonTitle01, mPersonTitle02, mPersonTitle03, mPersonTitle04;
    private BSCircleImageView mHeadIcon;

    private BSGridView mInformGv;
    private HeadAdapter mInformAdapter;

    private String mId;
    private ScheduleDetailVO mScheduleDetailVO;

    private TextView mSecheduleTitle, mSecheduleType, mStartTime, mEndTime, mNotifyType;
    private LinearLayout mInformLayout;
    private TextView mInformTv;
    private String mMessageid;

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.sechedule_detail, null);
        mContentLayout.addView(layout);
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    @Override
    public void updateUi() {
        ScheduleDetailVO vo = mScheduleDetailVO.getArray();
        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = CommonUtils.initImageLoaderOptions();
        imageLoader.displayImage(vo.getHeadpic(), mHeadIcon, options);
        mHeadIcon.setUserId(vo.getUserid());//HL:获取日程详情title头像对应的用户ID，便于做点击跳转
        mHeadIcon.setUrl(vo.getHeadpic());
        mHeadIcon.setUserName(vo.getFullname());
        mPersonTitle01.setText(vo.getFullname());
        mPersonTitle02.setText(vo.getDname() + "/" + vo.getPname());

        if (vo.getRemindtime() != null) {
            long time = Long.parseLong(vo.getRemindtime()) * 1000;
            mPersonTitle03.setText(DateUtils.parseDate(time));
        } else {
            mPersonTitle03.setText(DateUtils.getCurrentDay());
        }

        mPersonTitle03.setVisibility(View.GONE);
        mPersonTitle04.setVisibility(View.GONE);

        mSecheduleTitle.setText(vo.getTitle());
        if ("1".equals(vo.getOpen())) {
            mSecheduleType.setText("公开");
            mSecheduleType.setCompoundDrawablesRelativeWithIntrinsicBounds(this.getResources().getDrawable(R.drawable.eye02), null, null, null);
        } else {
            mSecheduleType.setText("私有");
            mSecheduleType.setCompoundDrawablesRelativeWithIntrinsicBounds(this.getResources().getDrawable(R.drawable.eye01), null, null, null);

        }

        if ("1".equals(vo.getAllday())) {
            mEndTime.setVisibility(View.GONE);
            String day = DateUtils.parseDateDay(vo.getStarttime());
            String hour = DateUtils.parseHour(vo.getStarttime());
            String week = DateUtils.getDayOfWeek(vo.getStarttime());

            mStartTime.setText(day + " " + week + " " + hour);
        } else {
            String startday = DateUtils.parseDateDay(vo.getStarttime());
            String starthour = DateUtils.parseHour(vo.getStarttime());
            String week = DateUtils.getDayOfWeek(DateUtils.parseDateDay(vo.getStarttime()));

            String endday = DateUtils.parseDateDay(vo.getEndtime());
            String endhour = DateUtils.parseHour(vo.getEndtime());
            String endweek = DateUtils.getDayOfWeek(DateUtils.parseDateDay(vo.getEndtime()));
            mStartTime.setText(startday + " " + week + " " + starthour);
            mEndTime.setText(endday + " " + week + " " + endhour);
        }

        if ("0".equals(vo.getRemindtime())) {
            // mNotifyType.setText(text) ;
            mNotifyType.setText("不提醒");
        } else {
            String remindDay = DateUtils.parseDateDay(vo.getRemindtime());
            String remindHour = DateUtils.parseHour(vo.getRemindtime());
            String remindWeek = DateUtils.getDayOfWeek(DateUtils.parseDateDay(vo.getEndtime()));
            mNotifyType.setText(remindDay + " " + remindWeek + " " + remindHour);
        }

        if (vo.getInsUser() != null) {
            mInformAdapter.updateData(vo.getInsUser());
            mInformTv.setVisibility(View.VISIBLE);
            mInformLayout.setVisibility(View.VISIBLE);
        } else {
            mInformTv.setVisibility(View.GONE);
            mInformLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public void initView() {
        mTitleTv.setText("日程详情");
        mHeadIcon = (BSCircleImageView) findViewById(R.id.head_icon);
        mPersonTitle01 = (TextView) findViewById(R.id.person_title01);
        mPersonTitle02 = (TextView) findViewById(R.id.person_title02);
        mPersonTitle03 = (TextView) findViewById(R.id.person_title03);
        mPersonTitle04 = (TextView) findViewById(R.id.person_title04);

        mSecheduleTitle = (TextView) findViewById(R.id.schedule_title);
        mSecheduleType = (TextView) findViewById(R.id.sechedule_type);

        mStartTime = (TextView) findViewById(R.id.start_time);
        mEndTime = (TextView) findViewById(R.id.end_time);
        mNotifyType = (TextView) findViewById(R.id.notify_type);

        mInformLayout = (LinearLayout) findViewById(R.id.inform_people_layout);
        mInformTv = (TextView) findViewById(R.id.inform_people_tv);
        mInformGv = (BSGridView) findViewById(R.id.inform_gv);
        mInformAdapter = new HeadAdapter(this, false, true);
        mInformGv.setAdapter(mInformAdapter);

        Intent intent = this.getIntent();
        mId = intent.getStringExtra("id");
        mMessageid = intent.getStringExtra("messageid");
    }

    @Override
    public void bindViewsListener() {

    }

    public boolean getData() {
        try {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put("id", mId);
            map.put("messageid", mMessageid);
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.SCHEDULE_DETAIL, map);
            Gson gson = new Gson();
            mScheduleDetailVO = gson.fromJson(jsonStr, ScheduleDetailVO.class);
            if (Constant.RESULT_CODE.equals(mScheduleDetailVO.getCode())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
