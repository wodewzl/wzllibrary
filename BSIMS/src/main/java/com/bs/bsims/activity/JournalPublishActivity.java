
package com.bs.bsims.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.HeadAdapter;
import com.bs.bsims.adapter.UploadAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.image.selector.ImageActivityUtils;
import com.bs.bsims.interfaces.RecordResult;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.time.WheelMain;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CommonUtils.ResultCallback;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.utils.SoundRecordUtil;
import com.bs.bsims.view.BSDialog;
import com.bs.bsims.view.BSGridView;
import com.bs.bsims.view.BSUPloadPopWindows;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.yzxtcp.service.YzxIMCoreService.mContext;

public class JournalPublishActivity extends BaseActivity implements OnItemClickListener, OnClickListener {
    private SoundRecordUtil mRecordUtil;
    private EditText mWorkExperience;
    private EditText mTomorrowPlan;
    // 抄送人相关变量
    private static final int ADD_INFORM_PERSON = 10;
    private List<EmployeeVO> mExistFollowList = new ArrayList<EmployeeVO>();
    private BSGridView mSharedGride;
    private HeadAdapter mSharedAdapter;
    private StringBuffer mSharedPerson = null;
    private TextView mAddPerson;

    // 上传图片使用
    private static final int TAKE_PICTURE = 0x000001;
    private static final int RESULT_LOAD_IMAGE = 0x000002;
    private List<String> mPicturePathList;
    private LinearLayout mParentView;
    private BSGridView mGrideviewUpload;
    private UploadAdapter mAdapter;
    private BSUPloadPopWindows mPop;
    private TextView mAddPicture;

    private LinearLayout mWeekMonthLayout;
    private LinearLayout mStartLayout, mEndLayout;
    private TextView mStartTimeText, mEndTimeText;
    private TextView mStartWeek, mEndWeek;
    private BSDialog mBSDialogStart, mBSDialogEnd;
    private String mType = "1";// 0代表日报，1代表周报，2代表月报
    private String mStartTime;
    private String mEndTime;
    private String mTimeType;

    private View timepickerview;
    private WheelMain wheelMain;
    // 当提交数据过程，点击提交按钮无效
    private boolean mCommitFlag = true;
    // 每次走OnDestroy()方法时缓存数据，当提交成功后则不再缓存，该变量用于判定是否缓存
    private boolean mFinishFlag = true;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.journal_publish_view, mContentLayout);
    }

    @Override
    public boolean getDataResult() {
        return true;
    }

    @Override
    public void updateUi() {
        // TODO Auto-generated method stub

    }

    @Override
    public void initView() {
        SpeechUtility.createUtility(mContext, SpeechConstant.APPID + "=58e8547d");
        mOkTv.setText("确认");
        mRecordUtil = new SoundRecordUtil(this, recordResult, 900, 480);
        mWeekMonthLayout = (LinearLayout) findViewById(R.id.week_or_month_layout);
        mStartLayout = (LinearLayout) findViewById(R.id.start_layout);
        mEndLayout = (LinearLayout) findViewById(R.id.end_layout);
        mStartTimeText = (TextView) findViewById(R.id.start_time);
        mStartWeek = (TextView) findViewById(R.id.start_week);
        mEndTimeText = (TextView) findViewById(R.id.end_time);
        mEndWeek = (TextView) findViewById(R.id.end_week);
        mWorkExperience = (EditText) findViewById(R.id.work_experience);
        mTomorrowPlan = (EditText) findViewById(R.id.tomorrow_plan);
        // 抄送人
        mAddPerson = (TextView) findViewById(R.id.add_person);
        mSharedGride = (BSGridView) findViewById(R.id.shared_gride);
        mSharedAdapter = new HeadAdapter(this, true);
        mSharedGride.setAdapter(mSharedAdapter);
        mSharedPerson = new StringBuffer();
        // 上传图片使用
        mAddPicture = (TextView) findViewById(R.id.add_picture);
        mGrideviewUpload = (BSGridView) findViewById(R.id.image_gride);
        mAdapter = new UploadAdapter(this);
        mGrideviewUpload.setAdapter(mAdapter);
        mParentView = (LinearLayout) getLayoutInflater().inflate(R.layout.creative_idea_new, null);
        mPicturePathList = new ArrayList<String>();
        mType = String.valueOf(getIntent().getIntExtra("type", 0));
        initData();
        initTitle(mType);
        mWorkExperience.requestFocus();
    }

    RecordResult recordResult = new RecordResult() {

        @Override
        public void getRecordResult(String result) {
            if (mWorkExperience.hasFocus()) {
                mWorkExperience.setText(mWorkExperience.getText() + result);
            } else if (mTomorrowPlan.hasFocus()) {
                mTomorrowPlan.setText(mTomorrowPlan.getText() + result);
            }
        }
    };

    public void initTitle(String type) {
        if ("0".equals(type)) {
            mTitleTv.setText("发布日志");
            mWeekMonthLayout.setVisibility(View.GONE);
            mWorkExperience.setHint("今日工作总结");
            mTomorrowPlan.setHint("明日工作计划");
        } else if ("1".equals(type)) {
            mTitleTv.setText("发布周报");
            mWeekMonthLayout.setVisibility(View.VISIBLE);
            mWorkExperience.setHint("本周工作总结");
            mTomorrowPlan.setHint("下周工作计划");
            getWeekDate();
        } else if ("2".equals(type)) {
            mTitleTv.setText("发布月报");
            mWeekMonthLayout.setVisibility(View.VISIBLE);
            mWorkExperience.setHint("本月工作总结");
            mTomorrowPlan.setHint("下月工作计划");
            getMonthDate();
        }

    }

    // 初始化月报开始结束时间
    public void getMonthDate() {
        String monthFirst = DateUtils.getMonthFirst();
        setTimeDate(monthFirst, mStartTimeText, mStartWeek);
        String monthLast = DateUtils.getMonthLast();
        setTimeDate(monthLast, mEndTimeText, mEndWeek);
        mStartTime = monthFirst;
        mEndTime = monthLast;
    }

    // 初始化周报开始结束时间
    public void getWeekDate() {
        String monday = DateUtils.getMonday();
        setTimeDate(monday, mStartTimeText, mStartWeek);
        String sunday = DateUtils.getSunday();
        setTimeDate(sunday, mEndTimeText, mEndWeek);
        mStartTime = monday;
        mEndTime = sunday;

    }

    public void setTimeDate(String date, TextView dayText, TextView weekText) {
        String year = date.split("-")[0];
        String month = date.split("-")[1];
        String day = date.split("-")[2];
        dayText.setText(month + "月" + day + "日");
        Calendar calendar = Calendar.getInstance();// 获得一个日历
        calendar.set(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));// 设置当前时间,月份是从0月开始计算
        int number = calendar.get(Calendar.DAY_OF_WEEK);// 星期表示1-7，是从星期日开始，
        String[] str = {
                "", "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六",
        };

        weekText.setText(str[number]);
    }

    @SuppressLint("NewApi")
    public void initData() {
        SharedPreferences preference = null;
        if ("0".equals(mType)) {
            preference = getSharedPreferences("journal_publish1", Context.MODE_PRIVATE);
        } else if ("1".equals(mType)) {
            preference = getSharedPreferences("journal_publish2", Context.MODE_PRIVATE);
        } else if ("2".equals(mType)) {
            preference = getSharedPreferences("journal_publish3", Context.MODE_PRIVATE);
        }
        mStartTime = preference.getString("start_time", "");
        mEndTime = preference.getString("end_time", "");
        String type = preference.getString("type", "");
        String start_text = preference.getString("start_text", "");
        String start_week = preference.getString("start_week", "");
        String end_text = preference.getString("end_text", "");
        String end_week = preference.getString("end_week", "");
        String work_experience = preference.getString("work_experience", "");
        String tomorrow_plan = preference.getString("tomorrow_plan", "");

        // mType.equals(type)用于区分是否是之前编写的界面
        if (mType.equals(type)) {
            if (!"".equals(start_text)) {
                mStartTimeText.setText(start_text);
            }
            if (!"".equals(start_week)) {
                mStartWeek.setText(start_week);
            }
            if (!"".equals(end_text)) {
                mEndTimeText.setText(end_text);
            }
            if (!"".equals(end_text)) {
                mEndWeek.setText(end_text);
            }
            if (!"".equals(work_experience)) {
                mWorkExperience.setText(work_experience);
            }
            if (!"".equals(tomorrow_plan)) {
                mTomorrowPlan.setText(tomorrow_plan);
            }

            try {
                FileInputStream stream1 = null;
                FileInputStream stream2 = null;
                if ("0".equals(mType)) {
                    stream1 = this.openFileInput("journal.headpicVO1");
                    stream2 = this.openFileInput("journal.pictureVO1");
                } else if ("1".equals(mType)) {
                    stream1 = this.openFileInput("journal.headpicVO2");
                    stream2 = this.openFileInput("journal.pictureVO2");
                } else if ("2".equals(mType)) {
                    stream1 = this.openFileInput("journal.headpicVO3");
                    stream2 = this.openFileInput("journal.pictureVO3");
                }
                // 展示头像
                ObjectInputStream ois1 = new ObjectInputStream(stream1);
                mExistFollowList = (List<EmployeeVO>) ois1.readObject();
                mSharedAdapter.updateData(mExistFollowList);

                // 展示图片
                ObjectInputStream ois2 = new ObjectInputStream(stream2);
                mPicturePathList = (List<String>) ois2.readObject();
                mAdapter.mPicList.addAll(mPicturePathList);
                for (int i = 0; i < mPicturePathList.size(); i++) {
                    Bitmap bitmap = BitmapFactory.decodeFile(mPicturePathList.get(i), CommonUtils.getBitmapOption(2));
                    mAdapter.mList.add(bitmap);
                    mAdapter.notifyDataSetChanged();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void bindViewsListener() {
        mStartLayout.setOnClickListener(this);
        mEndLayout.setOnClickListener(this);
        mGrideviewUpload.setOnItemClickListener(this);
        mHeadBackImag.setOnClickListener(this);
        editScrollListenter();

        mOkTv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if ("1".equals(mType) || "2".equals(mType)) {
                    if (mStartTime.length() == 0) {
                        CustomToast.showLongToast(JournalPublishActivity.this, "请选择开始时间！");
                        return;
                    }
                    if (mEndTime.length() == 0) {
                        CustomToast.showLongToast(JournalPublishActivity.this, "请选择结束时间！");
                        return;
                    }
                    // 比较开始、结束时间
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");// 小写的mm表示的是分钟
                    Date startDate = new Date();
                    Date endDate = new Date();
                    try {
                        startDate = sdf.parse(mStartTime);
                        endDate = sdf.parse(mEndTime);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    if (startDate.after(endDate) || startDate.equals(endDate)) {
                        CustomToast.showLongToast(JournalPublishActivity.this, "结束时间要大于开始时间！");
                        return;
                    }

                }
                if (mWorkExperience.getText().toString().length() == 0) {
                    CustomToast.showLongToast(JournalPublishActivity.this, "请填写工作总结！");
                    return;
                }
                if (mTomorrowPlan.getText().toString().length() == 0) {
                    CustomToast.showLongToast(JournalPublishActivity.this, "请填写工作计划！");
                    return;
                }

                if (mCommitFlag) {
                    mCommitFlag = false;
                    commit();
                }
            }
        });
    }

    public void editScrollListenter() {
        mWorkExperience.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int lineCount = mWorkExperience.getLineCount();
                if (lineCount > 7) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
                return false;

            }
        });
        mTomorrowPlan.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int lineCount = mTomorrowPlan.getLineCount();
                if (lineCount > 7) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
                return false;

            }
        });
    }

    public void commit() {
        // 清除上次的抄送人
        mSharedPerson.setLength(0);
        // 抄送人id拼接起来
        for (int i = 0; i < mSharedAdapter.mList.size(); i++) {
            String s = mSharedAdapter.mList.get(i).getUserid();
            mSharedPerson.append(mSharedAdapter.mList.get(i).getUserid());
            if (i != mSharedAdapter.mList.size() - 1) {
                mSharedPerson.append(",");
            }
        }

        CustomDialog.showProgressDialog(this, "正在提交数据...");
        RequestParams params = new RequestParams();
        try {

            params.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            params.put("userid", BSApplication.getInstance().getUserId());
            params.put("type", mType);
            params.put("content1", mWorkExperience.getText());
            params.put("content4", mTomorrowPlan.getText());
            params.put("ccuids", mSharedPerson);
            if ("1".equals(mType) || "2".equals(mType)) {
                params.put("starttime", mStartTime);
                params.put("endtime", mEndTime);
            }
            for (int i = 0; i < mAdapter.mPicList.size(); i++) {
                if (i <= 2) {
                    File file = CommonUtils.bitmapToString(this, mAdapter.mPicList.get(i));
                    params.put("file" + i, file);
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        String url = BSApplication.getInstance().getHttpTitle() + Constant.JOURNAL_PUBLISH;
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                mCommitFlag = true;
                CustomToast.showShortToast(JournalPublishActivity.this, "日志提交失败~");

            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                mCommitFlag = true;
                JSONObject jsonObject;
                CustomDialog.closeProgressDialog();
                try {
                    jsonObject = new JSONObject(new String(arg2));
                    String str = (String) jsonObject.get("retinfo");
                    String code = (String) jsonObject.get("code");
                    if (Constant.RESULT_CODE.equals(code)) {
                        calenJournalData();
                        mFinishFlag = false;
                        Intent intent = new Intent();
                        JournalPublishActivity.this.setResult(3, intent);
                        JournalPublishActivity.this.finish();
                    }
                    CustomToast.showShortToast(JournalPublishActivity.this, str);

                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int arg0, int resultCode, Intent intent) {
        switch (arg0) {
        // 抄送人
            case ADD_INFORM_PERSON:
                if (intent == null)
                    return;
                mExistFollowList.clear();
                mExistFollowList = (List<EmployeeVO>) intent.getSerializableExtra("checkboxlist");
                mSharedAdapter.updateData(mExistFollowList);
                break;
//            case TAKE_PICTURE:
//                if (mAdapter.mList.size() < 3 && resultCode == RESULT_OK) {
//                    if (intent == null) {
//                        File file = new File(mPop.getFilePath());
//                        mPicturePathList.add(mPop.getFilePath());
//                        mAdapter.mPicList.add(mPop.getFilePath());
//                        Bitmap bitmap = BitmapFactory.decodeFile(mPop.getFilePath(), CommonUtils.getBitmapOption(2)); // 将图
//                        mAdapter.mList.add(bitmap);
//                        mAdapter.notifyDataSetChanged();
//                    }
//                }
//                break;
//            case RESULT_LOAD_IMAGE:
//
//                if (resultCode == RESULT_OK && null != intent) {
//                    Uri selectedImage = intent.getData();
//                    String[] filePathColumn = {
//                            MediaStore.Images.Media.DATA
//                    };
//
//                    String picturePath;
//                    Cursor cursor = getContentResolver().query(selectedImage,
//                            filePathColumn, null, null, null);
//                    if (cursor == null) {
//                        picturePath = selectedImage.getPath();
//                    } else {
//                        cursor.moveToFirst();
//                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                        picturePath = cursor.getString(columnIndex);
//                        cursor.close();
//
//                    }
//
//                    mPicturePathList.add(picturePath);
//                    mAdapter.mPicList.add(picturePath);
//                    Bitmap bitmap = BitmapFactory.decodeFile(picturePath, CommonUtils.getBitmapOption(2)); // 将图
//                    mAdapter.mList.add(bitmap);
//                    mAdapter.notifyDataSetChanged();
//                }
//                break;
                
                /* 图片预览之后返回删除图片了 piclist */

            case ImageActivityUtils.REQUEST_IMAGE_BYSDCARD:
                if (resultCode == RESULT_OK) {
                    ImageActivityUtils.imgPreviewDelteActivity(intent, mAdapter);
                }

                break;
            case ImageActivityUtils.REQUEST_IMAGE:
                if (resultCode == RESULT_OK) {
                    ImageActivityUtils.setImageGetActivity(intent, mAdapter);
                }

                break;

        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long arg2) {
        if (parent == mGrideviewUpload) {
//            if (arg2 == mAdapter.mList.size()) {
//                mPop = new BSUPloadPopWindows(this, mParentView, null, null, 0);
//            } else {
//                mPop = new BSUPloadPopWindows(this, mParentView, mAdapter, mAdapter.mList.get((int) arg2), (int) arg2);
//            }
            
            ImageActivityUtils.setImageForActivity(view,JournalPublishActivity.this, mAdapter, (int)arg2);
            
        }
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.img_head_back:
                // saveJournalData();
                this.finish();
                break;
            case R.id.start_layout:
                mTimeType = "1";
                if (mBSDialogStart == null) {
                    mBSDialogStart = CommonUtils.initDateViewCallback(this, "请选择开始时间", mStartTimeText, 2, callback);
                } else {
                    mBSDialogStart.show();
                }
                break;
            case R.id.end_layout:
                mTimeType = "2";
                if (mBSDialogEnd == null) {
                    mBSDialogEnd = CommonUtils.initDateViewCallback(this, "请选择结束时间", mEndTimeText, 2, callback);
                } else {
                    mBSDialogEnd.show();
                }
                break;

            default:
                break;
        }

    }

    ResultCallback callback = new ResultCallback() {

        @Override
        public void callback(String str, int position) {
            String month = str.split("-")[1];
            String day = str.split("-")[2];
            String week = getWeek(str);
            if ("1".equals(mTimeType)) {
                mStartTime = str;
                mStartTimeText.setText(month + "月" + day + "日");
                mStartWeek.setText(week);
                mBSDialogStart.dismiss();
            } else if ("2".equals(mTimeType)) {
                mEndTime = str;
                mEndTimeText.setText(month + "月" + day + "日");
                mEndWeek.setText(week);
                mBSDialogEnd.dismiss();
            }
        }
    };

    private String getWeek(String time) {
        String Week = "星期";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");// 也可将此值当参数传进来
        // Date curDate = new Date(System.currentTimeMillis());
        // String pTime = format.format(curDate);
        String pTime = time;
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                Week += "天";
                break;
            case 2:
                Week += "一";
                break;
            case 3:
                Week += "二";
                break;
            case 4:
                Week += "三";
                break;
            case 5:
                Week += "四";
                break;
            case 6:
                Week += "五";
                break;
            case 7:
                Week += "六";
                break;
            default:
                break;
        }
        return Week;
    }

    // 缓存数据
    @SuppressWarnings("unchecked")
    @SuppressLint("NewApi")
    public void saveJournalData() {
        SharedPreferences preference = null;
        if ("0".equals(mType)) {
            preference = getSharedPreferences("journal_publish1", Context.MODE_PRIVATE);
        } else if ("1".equals(mType)) {
            preference = getSharedPreferences("journal_publish2", Context.MODE_PRIVATE);
        } else if ("2".equals(mType)) {
            preference = getSharedPreferences("journal_publish3", Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = preference.edit();
        editor.putString("type", mType);
        editor.putString("start_time", mStartTime);
        editor.putString("end_time", mEndTime);
        editor.putString("start_text", mStartTimeText.getText().toString());
        editor.putString("start_week", mStartWeek.getText().toString());
        editor.putString("end_text", mEndTimeText.getText().toString());
        editor.putString("end_week", mEndWeek.getText().toString());
        editor.putString("work_experience", mWorkExperience.getText().toString());
        editor.putString("tomorrow_plan", mTomorrowPlan.getText().toString());
        editor.commit();

        try {
            FileOutputStream stream1 = null;
            FileOutputStream stream2 = null;
            if ("0".equals(mType)) {
                stream1 = this.openFileOutput("journal.headpicVO1", MODE_PRIVATE);
                stream2 = this.openFileOutput("journal.pictureVO1", MODE_PRIVATE);
            } else if ("1".equals(mType)) {
                stream1 = this.openFileOutput("journal.headpicVO2", MODE_PRIVATE);
                stream2 = this.openFileOutput("journal.pictureVO2", MODE_PRIVATE);
            } else if ("2".equals(mType)) {
                stream1 = this.openFileOutput("journal.headpicVO3", MODE_PRIVATE);
                stream2 = this.openFileOutput("journal.pictureVO3", MODE_PRIVATE);
            }
            // 缓存头像
            ObjectOutputStream oos1 = new ObjectOutputStream(stream1);
            oos1.writeObject(mSharedAdapter.mList);
            // 缓存图片
            ObjectOutputStream oos2 = new ObjectOutputStream(stream2);
            oos2.writeObject(mPicturePathList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    public void calenJournalData() {
        SharedPreferences preference = null;
        if ("0".equals(mType)) {
            preference = getSharedPreferences("journal_publish1", Context.MODE_PRIVATE);
        } else if ("1".equals(mType)) {
            preference = getSharedPreferences("journal_publish2", Context.MODE_PRIVATE);
        } else if ("2".equals(mType)) {
            preference = getSharedPreferences("journal_publish3", Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = preference.edit();
        editor.putString("type", "");
        editor.putString("start_time", "");
        editor.putString("end_time", "");
        editor.putString("start_text", "");
        editor.putString("start_week", "");
        editor.putString("end_text", "");
        editor.putString("end_week", "");
        editor.putString("work_experience", "");
        editor.putString("tomorrow_plan", "");
        editor.putStringSet("pictures", null);
        editor.commit();
        try {
            FileOutputStream stream1 = null;
            FileOutputStream stream2 = null;
            if ("0".equals(mType)) {
                stream1 = this.openFileOutput("journal.headpicVO1", MODE_PRIVATE);
                stream2 = this.openFileOutput("journal.pictureVO1", MODE_PRIVATE);
            } else if ("1".equals(mType)) {
                stream1 = this.openFileOutput("journal.headpicVO2", MODE_PRIVATE);
                stream2 = this.openFileOutput("journal.pictureVO2", MODE_PRIVATE);
            } else if ("2".equals(mType)) {
                stream1 = this.openFileOutput("journal.headpicVO3", MODE_PRIVATE);
                stream2 = this.openFileOutput("journal.pictureVO3", MODE_PRIVATE);
            }
            // 缓存头像
            ObjectOutputStream oos1 = new ObjectOutputStream(stream1);
            oos1.reset();
            // 缓存图片
            ObjectOutputStream oos2 = new ObjectOutputStream(stream2);
            oos2.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageActivityUtils.crashBoBitmap(mAdapter);
        mRecordUtil.removeView();
        if (mFinishFlag) {
            saveJournalData();
        }
    }
}
