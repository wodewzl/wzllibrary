/**
 * 
 */

package com.bs.bsims.activity;

import android.view.View;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.PerSonModel;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.google.gson.Gson;

import java.util.HashMap;

/**
 * BS北盛最帅程序员 Copyright (c) 2016 湖北北盛科技有限公司
 * 
 * @author 梁骚侠
 * @date 2016-7-27
 * @version 2.0
 * @author Administrator 新增考勤页面
 */

public class AttendanceActivity extends BaseActivity {

    private TextView mLateTv, mLeaveTv, mCardMiss;
    private TextView mWriteLog, mNoWriteLog;
    private TextView mLevaeHour, mLeaveCount;
    private TextView mWorkUpdateHour, mWorkUpdateCount;
    private TextView mTimeTv;
    private String type;
    private PerSonModel perSonModel, sonModelInfo;

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.attendance_index, null);
        mContentLayout.addView(layout);
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    @Override
    public void updateUi() {
        sonModelInfo = perSonModel.getInfo();
        CustomDialog.closeProgressDialog();
        CommonUtils.setTextTwoBefore(this, mLateTv, sonModelInfo.getBelate(), " 次", R.color.yellow, 1.5f);
        CommonUtils.setTextTwoBefore(this, mLeaveTv, sonModelInfo.getLeaveearly(), " 次", R.color.yellow, 1.5f);
        CommonUtils.setTextTwoBefore(this, mCardMiss, sonModelInfo.getAbsence(), " 次", R.color.yellow, 1.5f);

        CommonUtils.setTextTwoBefore(this, mWriteLog, sonModelInfo.getLogTimes(), " 次", R.color.C12, 1.5f);
        CommonUtils.setTextTwoBefore(this, mNoWriteLog, sonModelInfo.getNolog(), " 次", R.color.C12, 1.5f);

        CommonUtils.setTextFourBefore(this, mLevaeHour, sonModelInfo.getAskleaveHours(), " 小时", sonModelInfo.getAskleaveMins(), " 分", R.color.my_attence_top, 1.5f);
        CommonUtils.setTextTwoBefore(this, mLeaveCount, sonModelInfo.getAskleave(), " 次", R.color.my_attence_top, 1.5f);

        CommonUtils.setTextFourBefore(this, mWorkUpdateHour, sonModelInfo.getOvertimeHours(), " 小时", sonModelInfo.getOvertimeMins(), " 分", R.color.pink, 1.5f);
        CommonUtils.setTextTwoBefore(this, mWorkUpdateCount, sonModelInfo.getOvertime(), " 次", R.color.pink, 1.5f);
    }

    @Override
    public void executeFailure() {
        // TODO Auto-generated method stub
        CustomDialog.closeProgressDialog();
        super.executeFailure();
    }

    @Override
    public void initView() {
        mTitleTv.setText("我的考勤");
        mLateTv = (TextView) findViewById(R.id.late);
        mLeaveTv = (TextView) findViewById(R.id.workout);
        mCardMiss = (TextView) findViewById(R.id.misscard);
        mWriteLog = (TextView) findViewById(R.id.jourlist_yes);
        mNoWriteLog = (TextView) findViewById(R.id.jourlist_no);
        mLevaeHour = (TextView) findViewById(R.id.allpleave_time);
        mLeaveCount = (TextView) findViewById(R.id.allpleave_count);
        mWorkUpdateHour = (TextView) findViewById(R.id.allwork_time);
        mWorkUpdateCount = (TextView) findViewById(R.id.allwork_count);
        mTimeTv = (TextView) findViewById(R.id.work_attendance_everyone_detail_txt);
        initData();

    }

    public void initData() {
        type = DateUtils.getTureMonthYYYYM();
        mTimeTv.setText(type);
    }

    @Override
    public void bindViewsListener() {

    }

    /** 下个月 ***/
    public void onGetNextMonth(View view) {
        if (type.equals(DateUtils.getTureMonthYYYYM())) {
            return;
        }
        else {
            type = DateUtils.getLastOrNextMonth(1, mTimeTv.getText().toString().trim());
            mTimeTv.setText(type);
            CustomDialog.showProgressDialog(this, "正在加载..");
            new ThreadUtil(this, this).start();
        }

    }

    /** 上个月 **/
    public void onGetLastMonth(View view) {
        // 上个月
        type = DateUtils.getLastOrNextMonth(-1, mTimeTv.getText().toString().trim());
        mTimeTv.setText(type);
        CustomDialog.showProgressDialog(this, "正在加载..");
        new ThreadUtil(this, this).start();
    }

    public boolean getData() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put("month", type);
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.MYATTENDEANCE, map);
            perSonModel = gson.fromJson(jsonStrList, PerSonModel.class);
            if (Constant.RESULT_CODE.equals(perSonModel.getCode())) {
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

}
