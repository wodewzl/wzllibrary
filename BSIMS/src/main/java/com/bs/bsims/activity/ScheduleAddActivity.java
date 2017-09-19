
package com.bs.bsims.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.HeadAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.time.ScreenInfo;
import com.bs.bsims.time.WheelMain;
import com.bs.bsims.utils.AnimationUtil;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.view.BSDialog;
import com.bs.bsims.view.BSGridView;
import com.bs.bsims.view.BSSwitchView;
import com.bs.bsims.view.BSSwitchView.OnChangedListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ScheduleAddActivity extends Activity implements OnClickListener, OnChangedListener, OnItemClickListener {
    private static final int ADD_INFORM_PERSON = 10;

    private LinearLayout mAllLayout, mStartLayout, mEndLayout, mNotifyLayout, mNotifyTimeLaout, mInformLayout;
    private BSDialog mDialog;
    private TextView mNotifyTv;
    private HeadAdapter mInformAdapter;
    private BSGridView mInformGv;
    private BSSwitchView mStatus;
    private TextView mTitleTv, mOkTv;
    private TextView mBackImg;
    private StringBuffer mInformPerson;
    private EditText mScheduleTitle;
    private Button mOkBt;
    private Context context;
    private String mStartTime;

    private TextView mAllDayTv, mStartTimeTv, mEndTimeTv, mSpecifiedTime;
    private String mRemindType = "0";

    // 添加员工使用
    protected List<EmployeeVO> mDataList = new ArrayList<EmployeeVO>();
    private String mOpen = "1";
    private String mSpecifiedDate;
    private boolean mFlag = true;
    private int falgeselect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_add);
        initView();
        bindViewsListeners();
        context = this;
    }

    public void initView() {
        mTitleTv = (TextView) findViewById(R.id.txt_comm_head_activityName);
        mTitleTv.setText("添加日程");
        mBackImg = (TextView) findViewById(R.id.img_head_back);
        mAllLayout = (LinearLayout) findViewById(R.id.all_layout);
        mStartLayout = (LinearLayout) findViewById(R.id.start_layout);
        mEndLayout = (LinearLayout) findViewById(R.id.end_layout);
        mNotifyLayout = (LinearLayout) findViewById(R.id.notify_layout);
        mNotifyTimeLaout = (LinearLayout) findViewById(R.id.notify_time_layout);
        mNotifyTv = (TextView) findViewById(R.id.notify_tv);
        mInformLayout = (LinearLayout) findViewById(R.id.inform_layout);
        mInformGv = (BSGridView) findViewById(R.id.inform_gv);
        mInformAdapter = new HeadAdapter(this, true);
        mInformGv.setAdapter(mInformAdapter);
        mStatus = (BSSwitchView) findViewById(R.id.status);
        mStatus.setCheckState(true);
        mInformPerson = new StringBuffer();
        mScheduleTitle = (EditText) findViewById(R.id.schedule_title);
        mOkBt = (Button) findViewById(R.id.ok_bt);
        mAllDayTv = (TextView) mAllLayout.findViewById(R.id.time);
        mAllDayTv.setText(DateUtils.getCurrentDate());
        mStartTimeTv = (TextView) mStartLayout.findViewById(R.id.time); // 开始时间
        mEndTimeTv = (TextView) mEndLayout.findViewById(R.id.time);// 结束时间
        mSpecifiedTime = (TextView) mNotifyTimeLaout.findViewById(R.id.time);

        if (this.getIntent().getStringExtra("date") != null) {
            mAllDayTv.setText(this.getIntent().getStringExtra("date"));
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    public void bindViewsListeners() {
        mAllLayout.setOnClickListener(this);
        mStartLayout.setOnClickListener(this);
        mEndLayout.setOnClickListener(this);
        mNotifyLayout.setOnClickListener(this);
        mStatus.SetOnChangedListener(this);
        mBackImg.setOnClickListener(this);
        mInformGv.setOnItemClickListener(this);
        mOkBt.setOnClickListener(this);
        mNotifyTimeLaout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_layout:
                showDateDialog(v.getId());
                break;
            case R.id.start_layout:
                showDateDialog(v.getId());
                break;
            case R.id.end_layout:
                showDateDialog(v.getId());
                break;
            case R.id.notify_layout:
                showPopupWindow(mNotifyLayout);
                break;

            case R.id.notify_time_layout:
                showDateDialog(v.getId());
                break;
            case R.id.img_head_back:
                this.finish();
                break;
            case R.id.ok_bt:

                if (mScheduleTitle.getText().toString().length() == 0) {
                    CustomToast.showLongToast(this, "日程标题不能为空");
                    return;
                }

                if ("00:00".equals(mStartTimeTv.getText().toString())) {
                    CustomToast.showLongToast(this, "请选择开始时间");
                    return;
                }

                if (!"1".equals(mRemindType) && !"0".equals(mRemindType)) {
                    long startTime = DateUtils.getStringToDate(mAllDayTv.getText().toString() + " " + mStartTimeTv.getText().toString());
                    long notifyTime = startTime - Long.parseLong(mRemindType) * 1000;
                    if (notifyTime <= System.currentTimeMillis()) {
                        CustomToast.showLongToast(this, "提醒时间要大于开始时间");
                        return;
                    }
                }

                if ("1".equals(mRemindType)) {
                    // 指定提醒时间
                    if (DateUtils.getStringToDate(mSpecifiedDate) < System.currentTimeMillis()) {
                        CustomToast.showLongToast(ScheduleAddActivity.this, "指定提醒时间已过");
                        return;
                    }
                    if (DateUtils.getStringToDate(mSpecifiedDate) > DateUtils.getStringToDate(mAllDayTv.getText().toString() + " " + mStartTimeTv.getText().toString())) {
                        CustomToast.showLongToast(ScheduleAddActivity.this, "指定提醒时间要小于开始时间");
                        return;
                    }
                }

                if ("1".equals(mOpen)) {
                    if (mInformPerson.length() == 0) {
                        CustomToast.showLongToast(this, "请选择知会人");
                        return;
                    }
                }

                if (DateUtils.getStringToDate(mAllDayTv.getText().toString() + " " + mStartTimeTv.getText().toString()) < System.currentTimeMillis()) {
                    CustomToast.showLongToast(ScheduleAddActivity.this, "开始时间已过");
                    return;
                }

                // 判断开始时间必须小于结束时间
                if (!DateUtils.MacthTimeForSchedule(mStartTimeTv.getText().toString(), mEndTimeTv.getText().toString()) && !"00:00".equals(mEndTimeTv.getText().toString())) {
                    CustomToast.showLongToast(this, "开始时间要小于结束时间哦~");
                    return;
                }

                if (mFlag) {
                    CustomDialog.showProgressDialog(context, "正在为您添加此日程...");
                    commit();
                    mFlag = false;
                }

                break;
            default:
                break;
        }
    }

    public void showDateDialog(final int id) {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View timepickerview = inflater.inflate(R.layout.timepicker, null);
        ScreenInfo screenInfo = new ScreenInfo(this);
        final WheelMain wheelMain = new WheelMain(timepickerview);
        wheelMain.screenheight = screenInfo.getHeight();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        switch (id) {
            case R.id.all_layout:
                wheelMain.initDateTimePicker(year, month, day);
                break;
            case R.id.start_layout:
                wheelMain.initDateTimePicker(hour, minute, true);
                break;
            case R.id.end_layout:
                wheelMain.initDateTimePicker(hour, minute, true);
                break;
            case R.id.notify_time_layout:
                wheelMain.initDateTimePicker(year, month, day, hour, minute);
                break;
            default:
                break;
        }

        mDialog = new BSDialog(this, "选择时间", timepickerview, new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                TextView tv = (TextView) ScheduleAddActivity.this.findViewById(id).findViewById(R.id.time);
                String startDate = "";
                String startTime = "00:00";
                switch (id) {
                    case R.id.all_layout:
                        mStartTimeTv.setText(DateUtils.parseHour(System.currentTimeMillis() / 1000 + ""));
                        tv.setText(wheelMain.getTime());
                        break;
                    case R.id.start_layout:
                        tv.setText(wheelMain.getOnlyTime());

                        break;
                    case R.id.end_layout:
                        tv.setText(wheelMain.getOnlyTime());

                        break;
                    case R.id.notify_time_layout:
                        String week = DateUtils.getDayOfWeek(wheelMain.getTime());
                        mSpecifiedDate = wheelMain.getTime();
                        tv.setText(wheelMain.getTime() + "    " + week);
                        break;
                    default:
                        break;
                }

                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    public void showPopupWindow(View anchor) {
        final PopupWindow popupWindow = new PopupWindow(ScheduleAddActivity.this);
        final View contentView = LayoutInflater.from(ScheduleAddActivity.this).inflate(R.layout.pop_notify_item, null);

        contentView.findViewById(R.id.item_01).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mNotifyTv.setText(((TextView) contentView.findViewById(R.id.item_01)).getText());

                if (mNotifyTimeLaout.getVisibility() == View.VISIBLE) {
                    AnimationUtil.hideView(mNotifyTimeLaout);
                }
                mRemindType = "0";
                popupWindow.dismiss();
            }
        });
        contentView.findViewById(R.id.item_02).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mNotifyTv.setText(((TextView) contentView.findViewById(R.id.item_02)).getText());
                if (mNotifyTimeLaout.getVisibility() == View.VISIBLE) {
                    AnimationUtil.hideView(mNotifyTimeLaout);
                }
                mRemindType = "900";
                popupWindow.dismiss();
            }
        });
        contentView.findViewById(R.id.item_03).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mNotifyTv.setText(((TextView) contentView.findViewById(R.id.item_03)).getText());
                if (mNotifyTimeLaout.getVisibility() == View.VISIBLE) {
                    AnimationUtil.hideView(mNotifyTimeLaout);
                }
                mRemindType = "1800";
                popupWindow.dismiss();
            }
        });
        contentView.findViewById(R.id.item_04).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mNotifyTv.setText(((TextView) contentView.findViewById(R.id.item_04)).getText());
                if (mNotifyTimeLaout.getVisibility() == View.VISIBLE) {
                    AnimationUtil.hideView(mNotifyTimeLaout);
                }
                mRemindType = "3600";
                popupWindow.dismiss();
            }
        });
        contentView.findViewById(R.id.item_05).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mNotifyTv.setText(((TextView) contentView.findViewById(R.id.item_05)).getText());
                if (mNotifyTimeLaout.getVisibility() == View.VISIBLE) {
                    AnimationUtil.hideView(mNotifyTimeLaout);
                }
                mRemindType = "86400";
                popupWindow.dismiss();
            }
        });
        contentView.findViewById(R.id.item_06).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mNotifyTv.setText(((TextView) contentView.findViewById(R.id.item_06)).getText());
                popupWindow.dismiss();
                AnimationUtil.showView(mNotifyTimeLaout);
                mRemindType = "1";
            }
        });
        popupWindow.setWidth(anchor.getWidth());
        popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(contentView);
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new PaintDrawable());
        popupWindow.showAsDropDown(anchor);

    }

    @Override
    public void OnChanged(boolean checkState, View v) {
        if (mStatus.getCheckState()) {
            AnimationUtil.showView(mInformLayout);
            mOpen = "1";
        } else {
            AnimationUtil.hideView(mInformLayout);
            mOpen = "0";
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long arg2) {

        if (parent == mInformGv) {
            if (arg2 == mInformAdapter.mList.size()) {
                Intent intent = new Intent();
                intent.setClass(this, AddByDepartmentActivity.class);
                intent.putExtra("employ_name", ScheduleAddActivity.class);
                intent.putExtra("selectsure", falgeselect);
                intent.putExtra("requst_number", 10);
                startActivityForResult(intent, 10);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case ADD_INFORM_PERSON:
                if (requestCode == 10) {
                    if (data == null)
                        return;
                    mDataList.clear();
                    mDataList = (List<EmployeeVO>) data.getSerializableExtra("checkboxlist");
                    falgeselect = data.getIntExtra("selectsure", 0);
                    mInformAdapter.mList.clear();
                    mInformAdapter.mList.addAll(mDataList);
                    mInformAdapter.notifyDataSetChanged();
                    mInformPerson = new StringBuffer();
                    for (int i = 0; i < mDataList.size(); i++) {
                        mInformPerson.append(mDataList.get(i).getUserid());
                        if (i != mDataList.size() - 1) {
                            mInformPerson.append(",");
                        }
                    }
                }
                break;
        }
    }

    public boolean commit() {
        RequestParams params = new RequestParams();

        try {
            params.put("ftoken", BSApplication.getInstance().getmCompany());
            params.put("userid", BSApplication.getInstance().getUserId());
            params.put("title", mScheduleTitle.getText().toString());
            params.put("stime", mAllDayTv.getText().toString() + " " + mStartTimeTv.getText().toString());
            if ("00:00".equals(mEndTimeTv.getText().toString())) {
                params.put("etime", mAllDayTv.getText().toString() + " " + mStartTimeTv.getText().toString());
            } else {
                params.put("etime", mAllDayTv.getText().toString() + " " + mEndTimeTv.getText().toString());
            }

            params.put("remindtype", mRemindType);
            if ("1".equals(mRemindType)) {
                params.put("remindtime", mSpecifiedDate);
            }
            params.put("open", mOpen);
            if ("1".equals(mOpen)) {
                params.put("relatedid", mInformPerson.toString());
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        String url = BSApplication.getInstance().getHttpTitle() + Constant.SCHEDULE_ADD;
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(60000);
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                CustomToast.showShortToast(context, "日程提交失败...");
                CustomDialog.closeProgressDialog();
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                System.out.println(new String(arg2));

                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(new String(arg2));
                    String str = (String) jsonObject.get("retinfo");
                    String code = (String) jsonObject.get("code");
                    String id = (String) jsonObject.get("id");
                    if (Constant.RESULT_CODE.equals(code)) {
                        mFlag = true;
                        if (Integer.parseInt(mRemindType) == 1) {
                            long time = DateUtils.getStringToDate(mSpecifiedTime.getText().toString());
                            CommonUtils.setAlarm(ScheduleAddActivity.this, time, mScheduleTitle.getText().toString(), id);
                        } else if (Integer.parseInt(mRemindType) > 1) {
                            long startTime = DateUtils.getStringToDate(mAllDayTv.getText().toString() + " " + mStartTimeTv.getText().toString());
                            long notifyTime = startTime - Long.parseLong(mRemindType) * 1000;
                            CommonUtils.setAlarm(ScheduleAddActivity.this, notifyTime, mScheduleTitle.getText().toString(), id);

                        }

                        Intent intent = new Intent();
                        intent.putExtra("star_time", mAllDayTv.getText().toString());
                        ScheduleAddActivity.this.setResult(2, intent);
                        ScheduleAddActivity.this.finish();
                        CustomDialog.closeProgressDialog();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                } finally {
                    CustomDialog.closeProgressDialog();
                }
            }

        });
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        return super.onTouchEvent(event);
    }

}
