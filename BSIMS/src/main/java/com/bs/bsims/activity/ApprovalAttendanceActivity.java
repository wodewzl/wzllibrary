
package com.bs.bsims.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.HeadAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.AttendanceResultVO;
import com.bs.bsims.model.AttendanceVO;
import com.bs.bsims.model.CrmOptionsVO;
import com.bs.bsims.model.CustomApprovalListVO;
import com.bs.bsims.model.EmployeeVO;
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
import com.bs.bsims.view.BSPopupWindowsTitle.TreeCallBack;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ApprovalAttendanceActivity extends BaseActivity implements OnClickListener, OnItemClickListener {
    private static final int ADD_INFORM_PERSON = 10;
    // 添加员工使用
    private String mApprovalType;
    private TextView mApprovalTypeTv;
    private String mUid;
    private String mType;

    private LinearLayout mMissCardLayout, mStartTimeLayout, mMonthDayLy;

    private BSGridView mTransferGv, mApproverGv, mInformGv;
    private HeadAdapter mTransferAdapter, mApproverAdapter, mInformAdapter;

    private TextView mTransferTv, mApproverTv, mApprovalGoTv, mInformTv, mInformGoTv, mCancel, mSure;
    private LinearLayout mTransferLayout, mApproverLayout, mInformLayout, mApprovalLy;

    private AttendanceVO mAttendanceVO;

    private StringBuffer mTransferPerson, mApprovalPerson, mInformPerson;

    private int mStatus = 0;// 0为获取知会人接口请求，1为选择时间后获取申诉日期请求

    // 添加员工使用
    protected List<EmployeeVO> mDataList = new ArrayList<EmployeeVO>();
    private EditText mMissCardContent;

    private TextView mMissCardTitle, mMissCardDate, mCardDate2;

    private BSDialog dialog;
    private String mMissDateStr;
    private boolean mFlag = true;

    private BSPopupWindowsTitle mBsPopupWindowsTitle;
    private CustomApprovalListVO mOptionsVo;

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.approval_attendance, null);
        mContentLayout.addView(layout);
    }

    @Override
    public boolean getDataResult() {
        if (mStatus == 0) {
            return getData();
        } else {
            return getDateApplay();
        }

    }

    @Override
    public void updateUi() {

        if (mStatus == 1) {
            CustomDialog.closeProgressDialog();
            new AttendancePopupWindwos(this, mCardDate2);
            return;
        }

        mTransferTv.setVisibility(View.VISIBLE);
        mTransferTv.setText(R.string.proof_people);
        mTransferLayout.setVisibility(View.VISIBLE);
        // mTransferGoTv.setVisibility(View.VISIBLE);
        if (mAttendanceVO == null) {
            return;
        }
        if (mAttendanceVO.getAppUser() == null && mAttendanceVO.getInsUser() == null)
            return;
        if (mAttendanceVO.getAppUser() != null) {
            mApprovalPerson.setLength(0);
            mApproverTv.setVisibility(View.VISIBLE);
            mApproverLayout.setVisibility(View.VISIBLE);
            for (int i = 0; i < mAttendanceVO.getAppUser().size(); i++) {
                mApprovalPerson.append(mAttendanceVO.getAppUser().get(i).getUserid());
                if (i != mAttendanceVO.getAppUser().size() - 1) {
                    mApprovalPerson.append(",");
                }
            }
            mApproverAdapter.setApproval(true);
            mApproverAdapter.updateData(mAttendanceVO.getAppUser());
        } else {
            mApproverTv.setVisibility(View.GONE);
            mApproverLayout.setVisibility(View.GONE);
        }

        mInformPerson.setLength(0);
        mInformAdapter.mList.clear();
        mInformAdapter.notifyDataSetChanged();
        if (mAttendanceVO.getInsUser() != null) {
            for (int i = 0; i < mAttendanceVO.getInsUser().size(); i++) {
                mInformPerson.append(mAttendanceVO.getInsUser().get(i).getUserid());
                if (i != mAttendanceVO.getInsUser().size() - 1) {
                    mInformPerson.append(",");
                }
            }
            mInformTv.setVisibility(View.VISIBLE);
            mInformLayout.setVisibility(View.VISIBLE);
            mInformAdapter.updateData(mAttendanceVO.getInsUser());
        }

    }

    @Override
    public void initView() {
        mMissCardContent = (EditText) findViewById(R.id.missing_card_content);

        mStartTimeLayout = (LinearLayout) findViewById(R.id.start_time_layout);
        mApprovalTypeTv = (TextView) findViewById(R.id.approval_type);
        mTransferGv = (BSGridView) findViewById(R.id.transfer_gv);
        mApproverGv = (BSGridView) findViewById(R.id.approver_gv);
        mInformGv = (BSGridView) findViewById(R.id.inform_gv);
        mCardDate2 = (TextView) findViewById(R.id.start_time_tv);
        mApprovalLy = (LinearLayout) findViewById(R.id.leave_layout);
        mMonthDayLy = (LinearLayout) findViewById(R.id.monthtranfer);
        mTransferAdapter = new HeadAdapter(this, true, "1");
        mApproverAdapter = new HeadAdapter(this, false);
        mInformAdapter = new HeadAdapter(this, true);
        mTransferGv.setAdapter(mTransferAdapter);
        mApproverGv.setAdapter(mApproverAdapter);
        mInformGv.setAdapter(mInformAdapter);

        mTransferTv = (TextView) findViewById(R.id.transfer_tv);
        mApproverTv = (TextView) findViewById(R.id.approver_tv);
        mInformTv = (TextView) findViewById(R.id.inform_people_tv);
        mTransferLayout = (LinearLayout) findViewById(R.id.transfer_layout);
        mApproverLayout = (LinearLayout) findViewById(R.id.approver_layout);
        mInformLayout = (LinearLayout) findViewById(R.id.inform_people_layout);
        mApprovalGoTv = (TextView) findViewById(R.id.approver_go_tv);
        mInformGoTv = (TextView) findViewById(R.id.inform_go_tv);

        mCancel = (TextView) findViewById(R.id.cancel);
        mSure = (TextView) findViewById(R.id.sure);

        mApprovalPerson = new StringBuffer();
        mInformPerson = new StringBuffer();
        mTransferPerson = new StringBuffer();

        mMissCardTitle = (TextView) findViewById(R.id.missing_card_title);
        mMissCardDate = (TextView) findViewById(R.id.missing_card_date);
        initData();
    }

    public void initData() {
        // mApprovalType = getIntent().getStringExtra("approvalType");
        mUid = getIntent().getStringExtra("uid");
        mTitleTv.setText("考勤申诉");
        mOkTv.setText("确定");
        mOptionsVo = (CustomApprovalListVO) getIntent().getSerializableExtra("options");
        mType = getIntent().getStringExtra("apid");
        mApprovalTypeTv.setText(getIntent().getStringExtra("approvalType"));
        mMissDateStr = DateUtils.getTureMonthYYYYM();
        mCardDate2.setText(mMissDateStr);
        // mType = getIntent().getStringExtra("type");
        // mApprovalTypeTv.setText(mApprovalType);
        // if ("1".equals(mType)) {
        // mTitleTv.setText("缺卡申诉");
        // mMissCardTitle.setText("缺卡原因");
        // mMissCardDate.setText("缺卡日期：");
        // } else if ("2".equals(mType)) {
        // mTitleTv.setText("缺日志申诉");
        // mMissCardTitle.setText("缺日志原因");
        // mMissCardDate.setText("缺日志日期：");
        // } else if ("3".equals(mType)) {
        // mTitleTv.setText("迟到申诉");
        // mMissCardTitle.setText("迟到原因");
        // mMissCardDate.setText("迟到日期：");
        // } else {
        // mTitleTv.setText("早退申诉");
        // mMissCardTitle.setText("早退原因");
        // mMissCardDate.setText("早退日期：");
        // }
    }

    @Override
    public void bindViewsListener() {
        // mTransferGv.setOnItemClickListener(this);
        mSure.setOnClickListener(this);
        mOkTv.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        mStartTimeLayout.setOnClickListener(this);
        mMonthDayLy.setOnClickListener(this);
        mApprovalLy.setOnClickListener(this);
        // mInformGv.setOnItemClickListener(this);
    }

    public boolean getData() {
        try {
            String strUlr = UrlUtil.getApprovalAttendanceUrl(Constant.APPROVAL_ATTENDANCE, mUid, mType);
            String jsonStr = HttpClientUtil.get(strUlr, Constant.ENCODING).trim();
            Gson gson = new Gson();
            AttendanceResultVO attendanceResultVO = gson.fromJson(jsonStr, AttendanceResultVO.class);

            if (Constant.RESULT_CODE.equals(attendanceResultVO.getCode())) {
                mAttendanceVO = attendanceResultVO.getArray();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public void initDateView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View timepickerview = inflater.inflate(R.layout.timepicker, null);
        ScreenInfo screenInfo = new ScreenInfo(this);
        final WheelMain wheelMain = new WheelMain(timepickerview, false, false);
        wheelMain.screenheight = screenInfo.getHeight();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        wheelMain.initDateTimePicker(year, month, 0, 0, 0);

        dialog = new BSDialog(this, "请选择日期", timepickerview, new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mMissDateStr = wheelMain.getTime();
                new ThreadUtil(ApprovalAttendanceActivity.this,
                        ApprovalAttendanceActivity.this).start();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View arg0, int arg1, long arg2) {
        if (parent == mTransferGv) {

            if (mTransferAdapter.mList.size() > 0) {
                Intent intent = new Intent();
                intent.setClass(ApprovalAttendanceActivity.this, AddByPersonActivity.class);
                intent.putExtra("requst_number", 2014);
                startActivityForResult(intent, 2014);
                return;
            }
            if (arg2 == mTransferAdapter.mList.size()) {
                Intent intent = new Intent();
                intent.setClass(ApprovalAttendanceActivity.this, AddByPersonActivity.class);
                intent.putExtra("requst_number", 2014);
                startActivityForResult(intent, 2014);
            }
        }

        if (parent == mInformGv) {
            if (arg2 == mInformAdapter.mList.size()) {
                Intent intent = new Intent();
                intent.setClass(ApprovalAttendanceActivity.this, AddByDepartmentActivity.class);
                intent.putExtra("employ_name", JournalPublishActivity.class);
                // intent.putExtra("checkboxlist", (Serializable) mDataList);
                intent.putExtra("requst_number", 10);
                startActivityForResult(intent, 10);
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.start_time_layout:
                initDateView();
                mStatus = 1;
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
                        mCrmOptionsVO2.setId(mOptionsVo.getOption().get(i).getAtid());
                        mCrmOptionsVO2.setName(mOptionsVo.getOption().get(i).getName() + "");
                        parentList.add(mCrmOptionsVO2);
                    }
                    ArrayList<TreeVO> list = getOneLeveTreeVo(parentList);
                    mBsPopupWindowsTitle = new BSPopupWindowsTitle(this, list, callback,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                mBsPopupWindowsTitle.showPopupWindow(mApprovalLy);
                break;

            case R.id.txt_comm_head_right:

                if (mTransferPerson.length() == 0) {
                    CustomToast.showLongToast(this, "请选证明人接人");
                    return;
                }
                if (mApprovalPerson.length() == 0) {
                    CustomToast.showLongToast(this, "由于你的权限过高，无法发布此审批");
                    return;
                }
                if (mCardDate2.length() == 0 || mCardDate2.length() < 9) {
                    CustomToast.showLongToast(this, "缺卡时间不能为空");
                    return;
                }
                if (mMissCardContent.length() == 0) {
                    CustomToast.showLongToast(this, "缺卡原因不能为空");
                    return;
                }

                if (mFlag) {
                    mFlag = false;
                    commit();
                }
                break;

            case R.id.monthtranfer:
                mStatus = 1;
                CustomDialog.showProgressDialog(this, "正在加载..");
                new ThreadUtil(this, this).start();
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
            vo.setParentSerachId(parentList.get(i).getId());
            vo.setLevel(1);
            list.add(vo);
        }
        return list;
    }

    // 菜单点击回调函数
    TreeCallBack callback = new TreeCallBack() {
        @Override
        public void callback(TreeVO vo) {
            mType = vo.getParentSerachId();
            mApprovalTypeTv.setText(vo.getName());
        }
    };

    public boolean getDateApplay() {
        try {
            String strUlr = UrlUtil.gettApprovalAttendanceProofUrl(Constant.APPROVAL_ATTENDANCE_PROOF, mUid, mType, mMissDateStr);
            String jsonStr = HttpClientUtil.get(strUlr, Constant.ENCODING).trim();
            Gson gson = new Gson();
            AttendanceResultVO attendanceResultVO = gson.fromJson(jsonStr, AttendanceResultVO.class);
            if (Constant.RESULT_CODE.equals(attendanceResultVO.getCode())) {
                mAttendanceVO = attendanceResultVO.getArray();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 2014) {
            mDataList.clear();
            // mParent.removeAllViews();
            mDataList = (List<EmployeeVO>) data.getSerializableExtra("checkboxlist");
            mTransferAdapter.updateData(mDataList);
            mTransferPerson.setLength(0);
            for (int i = 0; i < mDataList.size(); i++) {
                mTransferPerson.append(mDataList.get(i).getUserid());
                if (i != mDataList.size() - 1) {
                    mTransferPerson.append(",");
                }
            }
        }

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
    }

    public boolean commit() {
        CustomDialog.showProgressDialog(this, "正在提交数据...");
        RequestParams params = new RequestParams();

        try {
            params.put("ftoken", BSApplication.getInstance().getmCompany());
            params.put("userid", BSApplication.getInstance().getUserId());
            params.put("sptime", mCardDate2.getText().toString());
            params.put("witness", mTransferPerson.toString());
            params.put("info", mMissCardContent.getText().toString());
            params.put("approver", mApprovalPerson.toString());
            params.put("insider", mInformPerson.toString());
            params.put("type", mType);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        String url = BSApplication.getInstance().getHttpTitle() + Constant.APPROVAL_ATTENDANCE_PUSH;
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
                        ApprovalAttendanceActivity.this.finish();
                        CustomToast.showShortToast(ApprovalAttendanceActivity.this, str);
                    } else {
                        CustomToast.showShortToast(ApprovalAttendanceActivity.this, str);
                    }
                    CustomDialog.closeProgressDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    mFlag = true;
                }
            }

        });
        return true;
    }

    private class AttendancePopupWindwos extends PopupWindow {
        private Context mContext;

        public AttendancePopupWindwos(Context context, View parent) {
            this.mContext = context;
            View view = View.inflate(context, R.layout.miss_card_pop, null);
            LinearLayout missCardLayout = (LinearLayout) view.findViewById(R.id.miss_card_layout);
            if (mAttendanceVO.getRowDate().length() > 1) {
                String[] date = mAttendanceVO.getRowDate().split(",");
                for (int i = 0; i < date.length; i++) {
                    if ("暂无".equals(date[i])) {
                        CustomToast.showLongToast(ApprovalAttendanceActivity.this, "没有日期可申诉");
                        return;
                    }
                    missCardLayout.addView(getMissLayout(date[i]));
                }

            } else if (mAttendanceVO.getRowDate().length() == 1) {
                if ("暂无".equals(mAttendanceVO.getRowDate())) {
                    CustomToast.showLongToast(ApprovalAttendanceActivity.this, "没有日期可申诉");
                    return;
                } else {
                    missCardLayout.addView(getMissLayout(mAttendanceVO.getRowDate()));
                }

            }
            view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in));
            // LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
            // ll_popup.startAnimation(AnimationUtils.loadAnimation(context,
            // R.anim.push_bottom_in_1));
            setWidth(CommonUtils.getScreenWid(ApprovalAttendanceActivity.this) / 2);
            setHeight(CommonUtils.getScreenWid(ApprovalAttendanceActivity.this) / 2 + CommonUtils.getScreenWid(ApprovalAttendanceActivity.this) / 3);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(false);
            setContentView(view);
            showAtLocation(parent, Gravity.CENTER, 15, -35);
            // showAsDropDown(parent, 0, 0);
            update();

        }

        public View getMissLayout(String oneDay) {
            StringBuffer sb = new StringBuffer();
            // String currentDate = DateUtils.getCurrentDate();
            // String currentDate = mStartTimeTv.getText().toString();
            String currentDate = mMissDateStr;
            View missView = View.inflate(mContext, R.layout.miss_card_itme, null);
            final TextView textDate = (TextView) missView.findViewById(R.id.text_date);
            textDate.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mCardDate2.setText(textDate.getText().toString());
                    dismiss();
                }
            });
            if (oneDay.length() == 1) {
                oneDay = "0" + oneDay;
            }
            // sb.append(currentDate.substring(0, 5)).append(oneDay).append(currentDate.substring(7,
            // 10));
            sb.append(currentDate).append("-").append(oneDay);
            textDate.setText(sb.toString());
            return missView;
        }
    }

    /** 下个月 ***/
    public void onGetNextMonth(View view) {
        mStatus = 1;
        if (mMissDateStr.equals(DateUtils.getTureMonthYYYYM())) {
            return;
        }
        else {
            mMissDateStr = DateUtils.getLastOrNextMonth(1, mCardDate2.getText().toString().trim());
            mCardDate2.setText(mMissDateStr);
            CustomDialog.showProgressDialog(this, "正在加载..");
            new ThreadUtil(this, this).start();
        }

    }

    /** 上个月 **/
    public void onGetLastMonth(View view) {
        mStatus = 1;
        // 上个月
        mMissDateStr = DateUtils.getLastOrNextMonth(-1, mCardDate2.getText().toString().trim());
        mCardDate2.setText(mMissDateStr);
        CustomDialog.showProgressDialog(this, "正在加载..");
        new ThreadUtil(this, this).start();
    }
}
