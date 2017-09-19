
package com.bs.bsims.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.HeadAdapter;
import com.bs.bsims.adapter.UploadAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.image.selector.ImageActivityUtils;
import com.bs.bsims.interfaces.UpdateCallback;
import com.bs.bsims.model.CrmOptionsVO;
import com.bs.bsims.model.CustomApprovalListVO;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.model.LeaveResultVO;
import com.bs.bsims.model.LeaveVO;
import com.bs.bsims.model.TreeVO;
import com.bs.bsims.time.ScreenInfo;
import com.bs.bsims.time.WheelMain;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.utils.UrlUtil;
import com.bs.bsims.view.BSDialog;
import com.bs.bsims.view.BSGridView;
import com.bs.bsims.view.BSPopupWindowsTitle;
import com.bs.bsims.view.BSUPloadPopWindows;
import com.bs.bsims.view.BSPopupWindowsTitle.TreeCallBack;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ApprovalLeaveActivity extends Activity implements OnItemClickListener, OnClickListener, UpdateCallback {
    private String mApprovalType;
    private TextView mApprovalTypeTv;

    private BSGridView mTransferGv, mApproverGv, mInformGv;
    private HeadAdapter mTransferAdapter, mApproverAdapter, mInformAdapter;

    // 上传图片使用
    private BSUPloadPopWindows mPop;
    private LinearLayout mParentView;
    private static final int TAKE_PICTURE = 0x000001;
    private static final int RESULT_LOAD_IMAGE = 0x000002;
    private static final int ADD_PERSON = 2014;
    private static final int ADD_INFORM_PERSON = 10;
    private GridView mGrideviewUpload;
    private UploadAdapter mAdapter;
    private EditText mLeaveContent;
    private TextView mTitleTv;
    private TextView mBackImg,mOkBtn;
    private TextView mApproverTv, mApprovalGoTv, mInformTv, mInformGoTv, mCancel, mSure;
    private LinearLayout mApproverLayout, mInformLayout, mLeavly;

    private LinearLayout mStartTimeLayout, mEndTimeLayout;
    // private Dialog dialog;
    private static int START_YEAR = 1990, END_YEAR = 2100;
    private TextView mStratTimeTv, mEndTimeTv, mDurationTimeTv;
    private int mStatus = 0;
    private String mTypeId;
    private LeaveVO mLeaveVO;
    private StringBuffer mTransferPerson, mApprovalPerson, mInformPerson;
    private List<String> mPicturePathList;

    // 添加员工使用
    protected List<EmployeeVO> mDataList = new ArrayList<EmployeeVO>();

    private WheelMain wheelMain;

    private boolean mFlag = true;

    private Bitmap bmp;
    private BSDialog dialog;
    private LeaveResultVO mLeaveResultVO;

    private String mStartTime = "";
    private String mEndTime = "";
    private BSPopupWindowsTitle mBsPopupWindowsTitle;
    private CustomApprovalListVO mOptionsVo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.approval_leave);
        initView();
        initData();
        bindViewsListener();
    }

    @Override
    public boolean execute() {
        return getData();
    }

    @Override
    public void executeSuccess() {
        if (mLeaveVO.getTimeShow() != null)
            mDurationTimeTv.setText(mLeaveVO.getTimeShow());
        mApprovalPerson.setLength(0);
        if (mLeaveVO.getAppUser() != null) {
            mApproverTv.setVisibility(View.VISIBLE);
            mApproverLayout.setVisibility(View.VISIBLE);
            for (int i = 0; i < mLeaveVO.getAppUser().size(); i++) {
                mApprovalPerson.append(mLeaveVO.getAppUser().get(i).getUserid());
                if (i != mLeaveVO.getAppUser().size() - 1) {
                    mApprovalPerson.append(",");
                }
            }
            mApproverAdapter.setApproval(true);
            mApproverAdapter.updateData(mLeaveVO.getAppUser());
        } else {
            mApproverTv.setVisibility(View.GONE);
            mApproverLayout.setVisibility(View.GONE);
        }

        // 每次请求，如果没有知会人就清0 ,因为不同的时间段可能必须知会人不一样
        mInformPerson.setLength(0);
        mInformAdapter.mList.clear();
        mInformAdapter.notifyDataSetChanged();
        if (mLeaveVO.getInsUser() != null) {
            for (int i = 0; i < mLeaveVO.getInsUser().size(); i++) {
                mInformPerson.append(mLeaveVO.getInsUser().get(i).getUserid());
                if (i != mLeaveVO.getInsUser().size() - 1) {
                    mInformPerson.append(",");
                }
            }
            mInformTv.setVisibility(View.VISIBLE);
            mInformLayout.setVisibility(View.VISIBLE);
            mInformAdapter.updateData(mLeaveVO.getInsUser());
        }

    }

    @Override
    public void executeFailure() {
        if (mLeaveResultVO != null) {
            mDurationTimeTv.setText("");
            CustomToast.showLongToast(this, mLeaveResultVO.getRetinfo());
        }

    }

    public void initView() {
        mTitleTv = (TextView) findViewById(R.id.txt_comm_head_activityName);
        mBackImg = (TextView) findViewById(R.id.img_head_back);
        mApprovalTypeTv = (TextView) findViewById(R.id.approval_type);
        mLeavly = (LinearLayout) findViewById(R.id.leave_layout);

        // 上传图片使用
        mGrideviewUpload = (GridView) findViewById(R.id.grideview_upload);
        mAdapter = new UploadAdapter(this);
        mGrideviewUpload.setAdapter(mAdapter);
        mParentView = (LinearLayout) getLayoutInflater().inflate(R.layout.creative_idea_new, null);
        mOkBtn = (TextView) findViewById(R.id.txt_comm_head_right);
        mTransferGv = (BSGridView) findViewById(R.id.transfer_gv);
        mApproverGv = (BSGridView) findViewById(R.id.approver_gv);
        mInformGv = (BSGridView) findViewById(R.id.inform_gv);
        mTransferAdapter = new HeadAdapter(this, true, "1");
        mApproverAdapter = new HeadAdapter(this, false);
        mInformAdapter = new HeadAdapter(this, true);
        mTransferGv.setAdapter(mTransferAdapter);
        mApproverGv.setAdapter(mApproverAdapter);
        mInformGv.setAdapter(mInformAdapter);

        mStartTimeLayout = (LinearLayout) findViewById(R.id.start_time_layout);
        mEndTimeLayout = (LinearLayout) findViewById(R.id.end_time_layout);

        mStratTimeTv = (TextView) findViewById(R.id.start_time_tv);
        mEndTimeTv = (TextView) findViewById(R.id.end_time_tv);
        mDurationTimeTv = (TextView) findViewById(R.id.duration_time_tv);

        mApproverTv = (TextView) findViewById(R.id.approver_tv);
        mInformTv = (TextView) findViewById(R.id.inform_people_tv);
        mApproverLayout = (LinearLayout) findViewById(R.id.approver_layout);
        mInformLayout = (LinearLayout) findViewById(R.id.inform_people_layout);
        mApprovalGoTv = (TextView) findViewById(R.id.approver_go_tv);
        mInformGoTv = (TextView) findViewById(R.id.inform_go_tv);
        mCancel = (TextView) findViewById(R.id.cancel);
        mSure = (TextView) findViewById(R.id.sure);
        mLeaveContent = (EditText) findViewById(R.id.leave_content);
        mApprovalPerson = new StringBuffer();
        mInformPerson = new StringBuffer();
        mTransferPerson = new StringBuffer();
        mPicturePathList = new ArrayList<String>();
        try {
            // 透明状态栏 5.0以后才可以显示
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public void initData() {
        // mApprovalType = getIntent().getStringExtra("approvalType");
        // mTypeId = getIntent().getStringExtra("typeId");
        // mApprovalTypeTv.setText(mApprovalType);
        // mTitleTv.setText(mApprovalType + "申请");

        // 审批改版，默认进来是事假申请，
        mTitleTv.setText("请假审批");
        mOkBtn.setText("确定");
        mOptionsVo = (CustomApprovalListVO) getIntent().getSerializableExtra("options");
        mTypeId = getIntent().getStringExtra("apid");
        mApprovalTypeTv.setText(getIntent().getStringExtra("approvalType"));

    }

    public void bindViewsListener() {
        mGrideviewUpload.setOnItemClickListener(this);
        mStartTimeLayout.setOnClickListener(this);
        mOkBtn.setOnClickListener(this);
        mLeavly.setOnClickListener(this);
        mEndTimeLayout.setOnClickListener(this);
        mBackImg.setOnClickListener(this);
        mSure.setOnClickListener(this);
        mCancel.setOnClickListener(this);
    }

    // 上传图片部分
    @Override
    public void onItemClick(AdapterView<?> parent, View arg0, int arg1, long arg2) {
        if (parent == mTransferGv) {
            if (arg2 == mTransferAdapter.mList.size()) {
                Intent intent = new Intent();
                intent.setClass(ApprovalLeaveActivity.this, AddByPersonActivity.class);
                intent.putExtra("requst_number", 2014);
                startActivityForResult(intent, 2014);
            }
        }

        if (parent == mInformGv) {
            if (arg2 == mInformAdapter.mList.size()) {
                Intent intent = new Intent();
                intent.setClass(ApprovalLeaveActivity.this, AddByDepartmentActivity.class);
                intent.putExtra("employ_name", JournalPublishActivity.class);
                intent.putExtra("requst_number", 10);
                startActivityForResult(intent, 10);
            }
        }

        if (parent == mGrideviewUpload) {
            ImageActivityUtils.setImageForActivity(arg0, ApprovalLeaveActivity.this, mAdapter, (int) arg2);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case ADD_PERSON:
                if (resultCode == 2014) {
                    if (data == null)
                        return;
                    mDataList.clear();
                    mDataList = (List<EmployeeVO>) data.getSerializableExtra("checkboxlist");
                    mTransferAdapter.mList.clear();
                    mTransferAdapter.mList.add(mDataList.get(0));
                    mTransferAdapter.notifyDataSetChanged();
                    mTransferPerson.setLength(0);
                    for (int i = 0; i < mDataList.size(); i++) {
                        mTransferPerson.append(mDataList.get(i).getUserid());
                        if (i != mDataList.size() - 1) {
                            mTransferPerson.append(",");
                        }
                    }
                }

                break;
            /* 图片预览之后返回删除图片了 piclist */

            case ImageActivityUtils.REQUEST_IMAGE_BYSDCARD:
                if (resultCode == RESULT_OK) {
                    ImageActivityUtils.imgPreviewDelteActivity(data, mAdapter);
                }

                break;

            case ImageActivityUtils.REQUEST_IMAGE:
                if (resultCode == RESULT_OK) {
                    ImageActivityUtils.setImageGetActivity(data, mAdapter);
                }

                break;
            case ADD_INFORM_PERSON:
                if (requestCode == 10) {
                    if (data == null)
                        return;
                    mDataList.clear();
                    mDataList = (List<EmployeeVO>) data.getSerializableExtra("checkboxlist");

                    mInformAdapter.mList.clear();
                    mInformAdapter.mList.addAll(mDataList);
                    mInformAdapter.notifyDataSetChanged();
                    mInformPerson.setLength(0);
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

    public void initDateView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View timepickerview = inflater.inflate(R.layout.timepicker, null);
        ScreenInfo screenInfo = new ScreenInfo(this);
        wheelMain = new WheelMain(timepickerview);
        wheelMain.screenheight = screenInfo.getHeight();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        String str = calendar.get(Calendar.DAY_OF_MONTH) + "";
        if (str.length() == 1) {
            str = "0" + str;
        }

        int day = Integer.parseInt(str);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        /**
         * 显示到小时 在审批中的需求 比如请假时长 只到小时不到分钟
         * 
         * @author peck 刘鹏程
         * @since 2015/7/23 16:35
         */
        wheelMain.initDateTimePicker(year, month, day, hour, minute);
        // wheelMain.initDateTimePickerEndHour(year, month, day, hour, minute);

        dialog = new BSDialog(this, "选择时间", timepickerview, new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mStatus == 1) {
                    mStratTimeTv.setText(wheelMain.getTime().split(" ")[0] + "\n   " + wheelMain.getTime().split(" ")[1]);
                    mStartTime = wheelMain.getTime();
                } else if (mStatus == 2) {
                    mEndTimeTv.setText(wheelMain.getTime().split(" ")[0] + "\n   " + wheelMain.getTime().split(" ")[1]);
                    mEndTime = wheelMain.getTime();
                }

                if (mStratTimeTv.getText().toString().length() > 0 && mEndTimeTv.getText().toString().length() > 0) {
                    long startTime = DateUtils.getStringToDate(mStartTime);
                    long endTime = DateUtils.getStringToDate(mEndTime);
                    if (endTime <= startTime) {
                        CustomToast.showShortToast(ApprovalLeaveActivity.this, "结束时间要比开始时间大");
                        return;
                    }
                    if (mFlag) {
                        mDurationTimeTv.setText("");
                        new ThreadUtil(ApprovalLeaveActivity.this, ApprovalLeaveActivity.this).start();
                    }

                }
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_time_layout:
                mStatus = 1;
                initDateView();
                break;
            case R.id.end_time_layout:
                mStatus = 2;
                initDateView();
                break;
            case R.id.img_head_back:
                finish();
                break;

            case R.id.cancel:
                this.finish();
                break;
            case R.id.sure:

             
                break;

            case R.id.leave_layout:
                if (mBsPopupWindowsTitle == null) {
                    List<CrmOptionsVO> parentList = new ArrayList<CrmOptionsVO>();
                    /* 自己创建筛选菜单 */
                    CrmOptionsVO mCrmOptionsVO2;
                    for (int i = 0; i < mOptionsVo.getOption().size(); i++) {
                        mCrmOptionsVO2 = new CrmOptionsVO();
                        mCrmOptionsVO2.setId(i + "");
                        mCrmOptionsVO2.setName(mOptionsVo.getOption().get(i).getName() + "");
                        parentList.add(mCrmOptionsVO2);
                    }
                    ArrayList<TreeVO> list = getOneLeveTreeVo(parentList);
                    mBsPopupWindowsTitle = new BSPopupWindowsTitle(this, list, callback,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                mBsPopupWindowsTitle.showPopupWindow(mLeavly);
                break;
                
            case R.id.txt_comm_head_right:
                
                if (mLeaveContent.length() == 0) {
                    CustomToast.showLongToast(this, "请填写请假原因");
                    return;
                }
                if (mTransferPerson.length() == 0) {
                    CustomToast.showLongToast(this, "请选择交接人");
                    return;
                }
                if (mApprovalPerson.length() == 0) {
                    CustomToast.showLongToast(this, "由于你的权限过高，无法发布此审批");
                    return;
                }
                if (mStratTimeTv.getText().toString().length() < 0 || mEndTimeTv.getText().toString().length() < 0) {
                    CustomToast.showShortToast(this, "结束、开始时间不能为空");
                    return;
                }

                if (mStratTimeTv.getText().toString().length() > 0 && mEndTimeTv.getText().toString().length() > 0) {
                    long startTime = DateUtils.getStringToDate(mStratTimeTv.getText().toString());
                    long endTime = DateUtils.getStringToDate(mEndTimeTv.getText().toString());
                    if (endTime <= startTime) {
                        CustomToast.showShortToast(this, "结束时间要比开始时间大");
                        return;
                    }
                }

                if (mDurationTimeTv.getText().length() == 0 || "共0小时".equals(mDurationTimeTv.getText().toString())) {
                    CustomToast.showLongToast(this, "请假时长不能为空");
                    return;
                }
                upload();
                break;
            default:
                break;
        }
    }
    
    public ArrayList<TreeVO> getOneLeveTreeVo(List<CrmOptionsVO> parentList) {
        ArrayList<TreeVO> list = new ArrayList<TreeVO>();
        for (int i = 0; i < parentList.size(); i++) {
            TreeVO vo = new TreeVO();
            vo.setName(parentList.get(i).getName());
            vo.setParentSerachId((i + 1) + "");
            vo.setLevel(1);
            list.add(vo);
        }
        return list;
    }
    // 菜单点击回调函数
    TreeCallBack callback = new TreeCallBack() {
        @Override
        public void callback(TreeVO vo) {
            mTypeId =vo.getParentSerachId();
            mApprovalTypeTv.setText(vo.getName());
        }
    };

    public boolean getData() {
        try {
            mFlag = false;
            String startTime = mStartTime.replace(" ", "");
            String endTime = mEndTime.replace(" ", "");
            String strUlr = UrlUtil.getApprovalInformUrl(Constant.APPROVAL_INFORM, BSApplication.getInstance().getUserId(), startTime, endTime, mTypeId);
            String jsonStr = HttpClientUtil.get(strUlr, Constant.ENCODING).trim();
            Gson gson = new Gson();
            mLeaveResultVO = gson.fromJson(jsonStr, LeaveResultVO.class);
            mLeaveVO = mLeaveResultVO.getArray();
            if (Constant.RESULT_CODE.equals(mLeaveResultVO.getCode())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {

            e.printStackTrace();
            return false;
        } finally {
            mFlag = true;
        }
    }

    public boolean upload() {
        CustomDialog.showProgressDialog(this, "正在提交数据...");
        RequestParams params = new RequestParams();

        try {
            params.put("ftoken", BSApplication.getInstance().getmCompany());
            params.put("userid", BSApplication.getInstance().getUserId());
            params.put("typeid", mTypeId);
            params.put("starttime", mStratTimeTv.getText().toString());
            params.put("endtime", mEndTimeTv.getText().toString());
            params.put("hours", mLeaveVO.getHours());
            params.put("minutes", mLeaveVO.getMinutes());
            params.put("handover", mTransferPerson.toString());
            params.put("content", mLeaveContent.getText().toString());
            params.put("approver", mApprovalPerson.toString());
            params.put("insider", mInformPerson.toString());
            for (int i = 0; i < mAdapter.mPicList.size(); i++) {
                File file = CommonUtils.bitmapToString(this, mAdapter.mPicList.get(i));
                params.put("insider" + i, file);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        String url = BSApplication.getInstance().getHttpTitle() + Constant.APPROVAL_PUSH;
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                System.out.println(new String(arg2));

                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(new String(arg2));
                    String str = (String) jsonObject.get("retinfo");
                    String code = (String) jsonObject.get("code");
                    if (Constant.RESULT_CODE.equals(code)) {
                        ApprovalLeaveActivity.this.finish();
                        CustomToast.showShortToast(ApprovalLeaveActivity.this, str);
                    } else {
                        CustomToast.showShortToast(ApprovalLeaveActivity.this, str);
                    }
                    CustomDialog.closeProgressDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
        return true;
    }

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onDestroy()
     */
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        ImageActivityUtils.crashBoBitmap(mAdapter);
    }

}
