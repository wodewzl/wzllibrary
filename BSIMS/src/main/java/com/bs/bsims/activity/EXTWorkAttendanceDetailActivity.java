
package com.bs.bsims.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.EXTWorkAttendanceDetailAdapter;
import com.bs.bsims.adapter.TwoTree4WorkAttendanceDetailAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.database.DBHelper;
import com.bs.bsims.model.UserFromServerVO;
import com.bs.bsims.model.WorkAttendanceDetailInLeaveDaysInVO;
import com.bs.bsims.model.WorkAttendanceDetailVO1;
import com.bs.bsims.model.WorkAttendanceDetailVO_;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomLog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.view.BSCircleImageView;
import com.bs.bsims.view.BSRefreshListView;
import com.bs.bsims.xutils.impl.HttpUtilsByPC;
import com.bs.bsims.xutils.impl.RequestCallBackPC;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;
import org.xutils.ex.HttpException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author peck
 * @Description: 考勤详情
 * @date 2015-5-15 下午4:07:17
 * @email 971371860@qq.com
 * @version V1.1 2015/5/29 22:49 原有的get 换成自己的
 */
public class EXTWorkAttendanceDetailActivity extends BaseActivity {

    private String TAG = "EXTWorkAttendanceDetailActivity";
    private Context mContext;

    private DBHelper dbHelper;

    private TextView mName, mTypeTv, mDepartment, mTime;
    /**
     * 本月考工220
     */
    private TextView itemWorkAttendanceDetailTotalAwork;
    private Context context;
    private BSRefreshListView workAttendanceDetailRefreshListview;
    private EXTWorkAttendanceDetailAdapter workAttendanceDetailRefreshAdapter;

    private BSCircleImageView imgSex;
    private BSCircleImageView myIcon;
    private UserFromServerVO userFromServerVO;
    private ExpandableListView mExpandableListView;
    private List<String> groupArray;// 组列表
    private List<List<String>> childArray;// 子列表
    private LinearLayout work_attendance_detail_father;
    private String userid;
    private LinearLayout work_attendance_layout;

    // tem_work_attendance_detail_count" tem_work_attendance_detail_info"
    // BSCircleImageView item_work_attendance_detail_icon

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userid = getIntent().getStringExtra("userid");
        getDataFromServerOnStart();
    }

    @Override
    public void baseSetContentView() {
        // TODO Auto-generated method stub
        View layout = View.inflate(this,
                R.layout.ac_ext_work_attendance_detail, null);
        mContentLayout.addView(layout);

        mContext = this;
        dbHelper = new DBHelper(this);
    }

    @Override
    public boolean getDataResult() {
        // TODO Auto-generated method stub
        return getData();
    }

    @Override
    public void updateUi() {
        // TODO Auto-generated method stub

    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        mTitleTv.setText("考勤详情");
        mName = (TextView) findViewById(R.id.work_attendance_detail_top_name_tv);
        mDepartment = (TextView) findViewById(R.id.work_attendance_detail_top_department_tv);
        mTime = (TextView) findViewById(R.id.work_attendance_detail_top_time_tv);
        // long time = Long.parseLong(mCreativeIdeaDetailVO.getTime()) * 1000;
        // mTime.setText(DateUtils.getDispalyDate(new
        // Timestamp(Long.valueOf(time))));
        mName.setText("考勤详情");
        work_attendance_detail_father = (LinearLayout) findViewById(R.id.work_attendance_detail_father);
        userFromServerVO = BSApplication.getInstance().getUserFromServerVO();
        imgSex = (BSCircleImageView) findViewById(R.id.work_attendance_detail_top_sex_icon);
        myIcon = (BSCircleImageView) findViewById(R.id.work_attendance_detail_top_head_icon);

        itemWorkAttendanceDetailTotalAwork = (TextView) findViewById(R.id.tem_work_attendance_detail_count);
        workAttendanceDetailRefreshListview = (BSRefreshListView) findViewById(R.id.work_attendance_detail_refreshlistview);
        itemWorkAttendanceDetailTotalAwork.setCompoundDrawables(null, null, null, null); // 设置
        groupArray = new LinkedList<String>();
        childArray = new ArrayList<List<String>>();
        mExpandableListView = (ExpandableListView) findViewById(R.id.work_attendance_detail_exdlistview);
        mExpandableListView.setAdapter(new TwoTree4WorkAttendanceDetailAdapter(
                mContext, groupArray, childArray));
        mExpandableListView.setGroupIndicator(null);

        workAttendanceDetailRefreshAdapter = new EXTWorkAttendanceDetailAdapter(
                mContext);
        workAttendanceDetailRefreshListview
                .setAdapter(workAttendanceDetailRefreshAdapter);
        context = this;

        work_attendance_layout = (LinearLayout) findViewById(R.id.work_attendance_layout);
        work_attendance_layout.setVisibility(View.GONE);
    }

    @Override
    public void bindViewsListener() {
        // TODO Auto-generated method stub

    }

    public boolean getData() {
        return true;
    }

    private void getDataFromServerOnStart() {
        CustomDialog.showProgressDialog(mContext);
        String url = BSApplication.getInstance().getHttpTitle()
                + Constant.WORK_ATTENDANCE_DETAIL;
        String date = DateUtils.getLastMonth();
        date = DateUtils.getLastMonthYYYYM();
        /**
         * 后台的统计数据 改为每天都能统计后，直接获取当月的数据
         */
        date = DateUtils.getCurrentDate111122();

        if (this.getIntent().getStringExtra("date") != null) {
            date = this.getIntent().getStringExtra("date");
        }

        if (TextUtils.isEmpty(userid)) {
            userid = BSApplication.getInstance().getUserId();
        }

        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("ftoken", BSApplication.getInstance().getmCompany());
        paramsMap.put("userid", userid);
        paramsMap.put("uid", userid);
        paramsMap.put("d", date);

        new HttpUtilsByPC().sendPostBYPC(url, paramsMap,
                new RequestCallBackPC() {

                    @Override
                    public void onSuccessPC(ResponseInfo arg0) {
                        // TODO Auto-generated method stub
                        work_attendance_detail_father.setVisibility(View.VISIBLE);
                        String result = (String) arg0.result;
                        String arrayStr = null;
                        String leave_days = null;
                        String code = null;
                        String retinfo = null;

                        try {
                            JSONObject jsonObject = new JSONObject(new String(result));
                            retinfo = (String) jsonObject.get("retinfo");
                            code = (String) jsonObject.get("code");
                            if (Constant.RESULT_CODE.equals(code)) {
                                arrayStr =jsonObject.getJSONObject("array").toString();
                                Gson gson = new Gson();
                                List<WorkAttendanceDetailVO_> parseArrayWADVOList =  new ArrayList<WorkAttendanceDetailVO_>();
                                   parseArrayWADVOList.add(gson.fromJson(arrayStr, WorkAttendanceDetailVO_.class));

                                if (null != parseArrayWADVOList
                                        && parseArrayWADVOList.size() == 1) {
                                    WorkAttendanceDetailVO_ workAttendanceDetailVO_ = parseArrayWADVOList
                                            .get(0);
                                    List<String> parseWADVOList = new LinkedList<String>();
                                    Map<String, String> valueMap = new HashMap<String, String>();

                                    Map<String, List<String>> valueEveryOneMap = new HashMap<String, List<String>>();
                                    itemWorkAttendanceDetailTotalAwork.setTextColor(context
                                            .getResources().getColor(R.color.red));
                                    itemWorkAttendanceDetailTotalAwork
                                            .setText(workAttendanceDetailVO_
                                                    .getTotal_awork() + "元");

                                    String getTime = workAttendanceDetailVO_
                                            .getS_month();
                                    if (!TextUtils.isEmpty(getTime)) {
                                        // if (!getTime.startsWith("0")) {
                                        // getTime = "0" + getTime;
                                        // }
                                        //
                                        if (Integer.parseInt(getTime) < 10) {
                                            getTime = "0" + getTime;
                                        }
                                    }
                                    getTime = workAttendanceDetailVO_.getS_year()
                                            + "-" + getTime;
                                    mTime.setText(getTime);

                                    mName.setText(workAttendanceDetailVO_
                                            .getU_fullname());
                                    mDepartment.setText(workAttendanceDetailVO_
                                            .getDept_name());

                                    mDepartment.setText(workAttendanceDetailVO_
                                            .getDept_name()
                                            + "/"
                                            + workAttendanceDetailVO_.getPname());
                                    if ("男".equalsIgnoreCase(workAttendanceDetailVO_
                                            .getSex())) {
                                        // imgSex.setImageResource(R.drawable.sex_man);
                                        Drawable img_off = getResources()
                                                .getDrawable(R.drawable.sex_man);
                                        // 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
                                        img_off.setBounds(0, 0,
                                                img_off.getMinimumWidth(),
                                                img_off.getMinimumHeight());
                                        mName.setCompoundDrawables(null, null,
                                                img_off, null); // 设置左图标
                                    }
                                    final String imgPath = workAttendanceDetailVO_
                                            .getHeadpic();
                                    CustomLog.e(TAG, imgPath);
                                    if (!TextUtils.isEmpty(imgPath)) {
                                        if (!"暂无".equalsIgnoreCase(imgPath)) {

                                            ImageLoader
                                                    .getInstance()
                                                    .displayImage(
                                                            imgPath,
                                                            myIcon,
                                                            CommonUtils
                                                                    .initImageLoaderOptions());

                                        }
                                    }

                                    if (!CommonUtils.hasJsonKey(arrayStr, "leave_days")) {
                                        CustomDialog.closeProgressDialog();
                                        return;
                                    }
                                    JSONObject jsonObjects = new JSONObject(new String(arrayStr));
                                    leave_days =jsonObjects.getJSONObject("leave_days").toString();
                                    Gson gsons = new Gson();
                                    Map<String, WorkAttendanceDetailInLeaveDaysInVO> retMap = gsons
                                            .fromJson(
                                                    leave_days,
                                                    new TypeToken<Map<String, WorkAttendanceDetailInLeaveDaysInVO>>() {
                                                    }.getType());
                                    if (!TextUtils.isEmpty(workAttendanceDetailVO_
                                            .getAbsence_num())) {
                                        // "缺卡";
                                        parseWADVOList.add("absence_num");
                                        valueMap.put("absence_num",
                                                workAttendanceDetailVO_
                                                        .getAbsence_num());

                                        WorkAttendanceDetailVO_ mWorkAttendanceDetailVO1AttnDays = workAttendanceDetailVO_ .getAttn_days();
                                              
                                        // String absence_numStr =
                                        // mWorkAttendanceDetailVO1AttnDays
                                        // .getAbsence_days();
                                        String strQuekaDate = workAttendanceDetailVO_
                                                .getQuekaDate();
                                        List<String> absence_numStrList = splitStr2List(strQuekaDate);
                                        valueEveryOneMap.put("absence_num",
                                                absence_numStrList);
                                    }

                                    String mapKey = "1";
                                    WorkAttendanceDetailInLeaveDaysInVO mWADLDaysInVO;
                                    List<WorkAttendanceDetailVO1> mWADetailVO1List;

                                    String tempStr = retMap.get(mapKey).getDays();

                                    // if (!TextUtils.isEmpty(tempStr)) {
                                    // if (!"0".equalsIgnoreCase(workAttendanceDetailVO_
                                    // .getBelate_num())) {
                                    if (!TextUtils.isEmpty(tempStr)) {
                                        if (!"0".equalsIgnoreCase(tempStr)) {
                                            // showInfo = "事假";
                                            mWADLDaysInVO = retMap.get(mapKey);
                                            parseWADVOList.add(mapKey);
                                            // String tempStr = retMap.get(mapKey)
                                            // .getDays();
                                            valueMap.put(mapKey, tempStr);
                                            mWADetailVO1List = mWADLDaysInVO.getList();
                                            if (null != mWADetailVO1List
                                                    && !mWADetailVO1List.isEmpty()) {
                                                List<String> absence_numStrList = new ArrayList<String>();
                                                for (int i = 0; i < mWADetailVO1List
                                                        .size(); i++) {
                                                    WorkAttendanceDetailVO1 mWADetailVO1 = mWADetailVO1List
                                                            .get(i);
                                                    absence_numStrList.add(mWADetailVO1
                                                            .toString());
                                                }
                                                valueEveryOneMap.put(mapKey,
                                                        absence_numStrList);
                                            }
                                        }
                                    }

                                    if (!TextUtils.isEmpty(workAttendanceDetailVO_
                                            .getLeavearly_num())) {
                                        if (!"0".equalsIgnoreCase(workAttendanceDetailVO_
                                                .getLeavearly_num())) {
                                            // showInfo = "早退";
                                            parseWADVOList.add("leavearly_num");
                                            valueMap.put("leavearly_num",
                                                    workAttendanceDetailVO_
                                                            .getLeavearly_num());
                                            String strZaotuiDate = workAttendanceDetailVO_
                                                    .getZaotuiDate();
                                            List<String> absence_numStrList = splitStr2List(strZaotuiDate);
                                            valueEveryOneMap.put("leavearly_num",
                                                    absence_numStrList);
                                        }
                                    }

                                    if (!TextUtils.isEmpty(workAttendanceDetailVO_
                                            .getBelate_num())) {
                                        if (!"0".equalsIgnoreCase(workAttendanceDetailVO_
                                                .getBelate_num())) {
                                            // showInfo = "迟到";
                                            parseWADVOList.add("belate_num");
                                            valueMap.put("belate_num",
                                                    workAttendanceDetailVO_
                                                            .getBelate_num());

                                            String strChidaoDate = workAttendanceDetailVO_
                                                    .getChidaoDate();
                                            List<String> absence_numStrList = splitStr2List(strChidaoDate);
                                            valueEveryOneMap.put("belate_num",
                                                    absence_numStrList);
                                        }
                                    }

                                    if (!TextUtils.isEmpty(workAttendanceDetailVO_
                                            .getNowrlog_num())) {
                                        if (!"0".equalsIgnoreCase(workAttendanceDetailVO_
                                                .getNowrlog_num())) {
                                            // showInfo = "未写日志";
                                            parseWADVOList.add("nowrlog_num");
                                            valueMap.put("nowrlog_num",
                                                    workAttendanceDetailVO_
                                                            .getNowrlog_num());

                                            String strLogDate = workAttendanceDetailVO_
                                                    .getLogDate();
                                            List<String> absence_numStrList = splitStr2List(strLogDate);
                                            valueEveryOneMap.put("nowrlog_num",
                                                    absence_numStrList);

                                        }
                                    }

                                    mapKey = "2";
                                    // showInfo = "病假";
                                    tempStr = retMap.get(mapKey).getDays();
                                    if (!TextUtils.isEmpty(tempStr)) {
                                        if (!"0".equalsIgnoreCase(tempStr)) {
                                            parseWADVOList.add(mapKey);
                                            valueMap.put(mapKey, tempStr);
                                            mWADLDaysInVO = retMap.get(mapKey);
                                            mWADetailVO1List = mWADLDaysInVO.getList();
                                            if (null != mWADetailVO1List
                                                    && !mWADetailVO1List.isEmpty()) {
                                                List<String> absence_numStrList = new ArrayList<String>();
                                                for (int i = 0; i < mWADetailVO1List.size(); i++) {
                                                    WorkAttendanceDetailVO1 mWADetailVO1 = mWADetailVO1List
                                                            .get(i);
                                                    absence_numStrList.add(mWADetailVO1
                                                            .toString());
                                                }
                                                valueEveryOneMap.put(mapKey,
                                                        absence_numStrList);
                                            }
                                        }
                                    }

                                    mapKey = "7";
                                    // showInfo = "丧假";
                                    tempStr = retMap.get(mapKey).getDays();
                                    if (!TextUtils.isEmpty(tempStr)) {
                                        if (!"0".equalsIgnoreCase(tempStr)) {
                                            parseWADVOList.add(mapKey);
                                            valueMap.put(mapKey, tempStr);
                                            mWADLDaysInVO = retMap.get(mapKey);
                                            mWADetailVO1List = mWADLDaysInVO.getList();
                                            if (null != mWADetailVO1List
                                                    && !mWADetailVO1List.isEmpty()) {
                                                List<String> absence_numStrList = new ArrayList<String>();
                                                for (int i = 0; i < mWADetailVO1List.size(); i++) {
                                                    WorkAttendanceDetailVO1 mWADetailVO1 = mWADetailVO1List
                                                            .get(i);
                                                    absence_numStrList.add(mWADetailVO1
                                                            .toString());
                                                }
                                                valueEveryOneMap.put(mapKey,
                                                        absence_numStrList);
                                            }
                                        }
                                    }

                                    mapKey = "3";
                                    // showInfo = "(陪)产假";
                                    tempStr = retMap.get(mapKey).getDays();
                                    if (!TextUtils.isEmpty(tempStr)) {
                                        if (!"0".equalsIgnoreCase(tempStr)) {
                                            parseWADVOList.add(mapKey);
                                            valueMap.put(mapKey, tempStr);
                                            mWADLDaysInVO = retMap.get(mapKey);
                                            mWADetailVO1List = mWADLDaysInVO.getList();
                                            if (null != mWADetailVO1List
                                                    && !mWADetailVO1List.isEmpty()) {
                                                List<String> absence_numStrList = new ArrayList<String>();
                                                for (int i = 0; i < mWADetailVO1List.size(); i++) {
                                                    WorkAttendanceDetailVO1 mWADetailVO1 = mWADetailVO1List
                                                            .get(i);
                                                    absence_numStrList.add(mWADetailVO1
                                                            .toString());
                                                }
                                                valueEveryOneMap.put(mapKey,
                                                        absence_numStrList);
                                            }
                                        }
                                    }

                                    mapKey = "5";
                                    // showInfo = "调休假";
                                    tempStr = retMap.get(mapKey).getDays();
                                    if (!TextUtils.isEmpty(tempStr)) {
                                        if (!"0".equalsIgnoreCase(tempStr)) {
                                            parseWADVOList.add(mapKey);
                                            valueMap.put(mapKey, tempStr);
                                            mWADLDaysInVO = retMap.get(mapKey);
                                            mWADetailVO1List = mWADLDaysInVO.getList();
                                            if (null != mWADetailVO1List
                                                    && !mWADetailVO1List.isEmpty()) {
                                                List<String> absence_numStrList = new ArrayList<String>();
                                                for (int i = 0; i < mWADetailVO1List.size(); i++) {
                                                    WorkAttendanceDetailVO1 mWADetailVO1 = mWADetailVO1List
                                                            .get(i);
                                                    absence_numStrList.add(mWADetailVO1
                                                            .toString());
                                                }
                                                valueEveryOneMap.put(mapKey,
                                                        absence_numStrList);
                                            }
                                        }
                                    }

                                    mapKey = "4";
                                    // showInfo = "调休假";
                                    tempStr = retMap.get(mapKey).getDays();
                                    if (!TextUtils.isEmpty(tempStr)) {
                                        if (!"0".equalsIgnoreCase(tempStr)) {
                                            parseWADVOList.add(mapKey);
                                            valueMap.put(mapKey, tempStr);
                                            mWADLDaysInVO = retMap.get(mapKey);
                                            mWADetailVO1List = mWADLDaysInVO.getList();
                                            if (null != mWADetailVO1List
                                                    && !mWADetailVO1List.isEmpty()) {
                                                List<String> absence_numStrList = new ArrayList<String>();
                                                for (int i = 0; i < mWADetailVO1List.size(); i++) {
                                                    WorkAttendanceDetailVO1 mWADetailVO1 = mWADetailVO1List
                                                            .get(i);
                                                    absence_numStrList.add(mWADetailVO1
                                                            .toString());
                                                }
                                                valueEveryOneMap.put(mapKey,
                                                        absence_numStrList);
                                            }
                                        }
                                    }

                                    // workAttendanceDetailRefreshAdapter.updateData(
                                    // parseWADVOList, valueMap);

                                    workAttendanceDetailRefreshAdapter.updateData(
                                            parseWADVOList, valueMap,
                                            valueEveryOneMap);

                                    CustomDialog.closeProgressDialog();
                                }
                            } else if (Constant.RESULT_CODE400
                                    .equals(code)) {
                                mContentLayout.removeAllViews();
                                CustomDialog.closeProgressDialog();
                                CustomToast.showShortToast(mContext, retinfo);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mContentLayout.removeAllViews();
                            CustomDialog.closeProgressDialog();
                        }
                    }

                    @Override
                    public void onFailurePC(HttpException arg0, String arg1) {
                        CustomToast.showShortToast(mContext, "网络似乎断开了哦");
                        mContentLayout.removeAllViews();
                        CustomDialog.closeProgressDialog();
                    }
                });
    }

    private List<String> splitStr2List(String absence_numStr) {
        if (TextUtils.isEmpty(absence_numStr)
                || Constant.nil.equalsIgnoreCase(absence_numStr)) {
            return null;
        }
        String absence_numStrArr[] = absence_numStr.split(",");
        List<String> absence_numStrList = new ArrayList<String>();
        for (int i = 0; i < absence_numStrArr.length; i++) {
            // absence_numStrList
            // .add(absence_numStrArr[i]
            // + "次缺卡");
            absence_numStrList.add(absence_numStrArr[i]);
        }
        return absence_numStrList;
    }

}
