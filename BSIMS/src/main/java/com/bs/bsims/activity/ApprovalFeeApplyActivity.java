
package com.bs.bsims.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import com.bs.bsims.model.OvertimeResultVO;
import com.bs.bsims.model.OvertimeVO;
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

public class ApprovalFeeApplyActivity extends Activity implements OnClickListener, OnItemClickListener, UpdateCallback {
    private static final int ADD_INFORM_PERSON = 10;

    private static final int TAKE_PICTURE = 0x000001;
    private static final int RESULT_LOAD_IMAGE = 0x000002;
    private static final int BORROW_LIST = 0x000003;
    private static final int ADD_PERSON = 2014;

    // 上传图片使用
    private List<String> mPicturePathList;
    private LinearLayout mParentView;
    private GridView mGrideviewUpload;
    private UploadAdapter mAdapter;
    private BSUPloadPopWindows mPop;

    private BSGridView mApproverGv, mInformGv;
    private HeadAdapter mApproverAdapter, mInformAdapter;
    private TextView mApproverTv, mApprovalGoTv, mInformTv, mInformGoTv, mCancel, mSure, mOkBtn;
    private LinearLayout mApproverLayout, mInformLayout;
    private StringBuffer mApprovalPerson, mInformPerson;

    private TextView mStartTimeTv;
    private LinearLayout mStartTimeLayout;

    private String mApprovalType;
    private TextView mApprovalTypeTv;
    private String mUid;
    private String mType;

    private OvertimeVO mOvertimeVO;
    private EditText mFeeTotal, mRealFeeTotal, mContentReason, mContentMark;
    // 添加员工使用
    protected List<EmployeeVO> mDataList = new ArrayList<EmployeeVO>();

    private TextView mTitleTv;
    private TextView mBackImg;

    private BSDialog dialog;
    private LinearLayout mLinkedLayout, mTitleNamely;
    private String mDelid, mMoney;
    private LinearLayout mBorrowItmeLayout, mRealFeelTotalLayout;
    private TextView mBorrowTitleTv, mBorrowFeeTv, mBorrowTimeTv, mMoneyStatus, mFeeTotalTv;

    private String mBorrowTitle;
    private boolean mFlag = true;
    private OvertimeResultVO mOvertimeResultVO;

    private BSPopupWindowsTitle mBsPopupWindowsTitle;
    private CustomApprovalListVO mOptionsVo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.approval_fee_apply);
        initView();
        initData();
        bindViewsListener();
    }

    public void initView() {
        try {
            // 透明状态栏 5.0以后才可以显示
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        mTitleTv = (TextView) findViewById(R.id.txt_comm_head_activityName);
        mBackImg = (TextView) findViewById(R.id.img_head_back);
        mLinkedLayout = (LinearLayout) findViewById(R.id.linked_layout);
        mBorrowItmeLayout = (LinearLayout) findViewById(R.id.item_layout);
        mTitleNamely = (LinearLayout) findViewById(R.id.leave_layout);
        mApprovalTypeTv = (TextView) findViewById(R.id.approval_type);
        mStartTimeLayout = (LinearLayout) findViewById(R.id.start_time_layout);
        mStartTimeTv = (TextView) findViewById(R.id.start_time_tv);
        mFeeTotal = (EditText) findViewById(R.id.fee_total);
        mRealFeelTotalLayout = (LinearLayout) findViewById(R.id.real_fee_total_layout);
        mRealFeeTotal = (EditText) findViewById(R.id.real_fee_total);
        mContentReason = (EditText) findViewById(R.id.content_reason);
        mApproverGv = (BSGridView) findViewById(R.id.approver_gv);
        mInformGv = (BSGridView) findViewById(R.id.inform_gv);
        mOkBtn = (TextView) findViewById(R.id.txt_comm_head_right);
        mApproverAdapter = new HeadAdapter(this, false);
        mInformAdapter = new HeadAdapter(this, true);
        mApproverGv.setAdapter(mApproverAdapter);
        mInformGv.setAdapter(mInformAdapter);

        mApproverTv = (TextView) findViewById(R.id.approver_tv);
        mInformTv = (TextView) findViewById(R.id.inform_people_tv);
        mApproverLayout = (LinearLayout) findViewById(R.id.approver_layout);
        mInformLayout = (LinearLayout) findViewById(R.id.inform_people_layout);
        mApprovalGoTv = (TextView) findViewById(R.id.approver_go_tv);
        mInformGoTv = (TextView) findViewById(R.id.inform_go_tv);
        mCancel = (TextView) findViewById(R.id.cancel);
        mSure = (TextView) findViewById(R.id.sure);

        mApprovalPerson = new StringBuffer();
        mInformPerson = new StringBuffer();

        // 上传图片使用
        mGrideviewUpload = (GridView) findViewById(R.id.grideview_upload);
        mAdapter = new UploadAdapter(this);
        mGrideviewUpload.setAdapter(mAdapter);
        mParentView = (LinearLayout) getLayoutInflater().inflate(R.layout.creative_idea_new, null);
        mPicturePathList = new ArrayList<String>();

        mBorrowTitleTv = (TextView) findViewById(R.id.borrow_title_tv);
        mBorrowFeeTv = (TextView) findViewById(R.id.borrow_fee_tv);
        mBorrowTimeTv = (TextView) findViewById(R.id.borrow_time_tv);
        mMoneyStatus = (TextView) findViewById(R.id.money_status);
        mContentMark = (EditText) findViewById(R.id.content_mark);
        mFeeTotalTv = (TextView) findViewById(R.id.fee_total_tv);
        initData();

    }

    public void initData() {
        // mApprovalType = getIntent().getStringExtra("approvalType");
        mUid = getIntent().getStringExtra("uid");
        // mType = getIntent().getStringExtra("type");
        // mApprovalTypeTv.setText(mApprovalType);
        mTitleTv.setText("费用申请");

        mOkBtn.setText("确定");
        mOptionsVo = (CustomApprovalListVO) getIntent().getSerializableExtra("options");
        mType = getIntent().getStringExtra("apid");
        mApprovalTypeTv.setText(getIntent().getStringExtra("approvalType"));

    }

    public void bindViewsListener() {
        mLinkedLayout.setOnClickListener(this);
        mGrideviewUpload.setOnItemClickListener(this);
        mStartTimeLayout.setOnClickListener(this);
        mSure.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        mBackImg.setOnClickListener(this);
        mBorrowItmeLayout.setOnClickListener(this);
        mTitleNamely.setOnClickListener(this);
        mOkBtn.setOnClickListener(this);
        mFeeTotal.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!mFeeTotal.hasFocus() && Float.parseFloat(CommonUtils.isNormalData(mFeeTotal.getText().toString())) > 0) {
                    new ThreadUtil(ApprovalFeeApplyActivity.this, ApprovalFeeApplyActivity.this).start();
                }
            }
        });
        mRealFeeTotal.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                if (!mRealFeeTotal.hasFocus() && Float.parseFloat(CommonUtils.isNormalData(mRealFeeTotal.getText().toString())) > 0) {
                    new ThreadUtil(ApprovalFeeApplyActivity.this, ApprovalFeeApplyActivity.this).start();
                    float money = Float.parseFloat(CommonUtils.isNormalData(mFeeTotalTv.getText().toString())) - Float.parseFloat(CommonUtils.isNormalData(mRealFeeTotal.getText().toString()));
                    mMoneyStatus.setVisibility(View.VISIBLE);
                    if (money > 0) {
                        mRealFeeTotal.setText(mRealFeeTotal.getText());
                        mMoneyStatus.setText("(应退款" + Math.abs(money) + "元)");
                    } else {
                        mMoneyStatus.setText("(应补款" + Math.abs(money) + "元)");
                    }
                }
            }
        });
    }

    public void initDateView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        final View timepickerview = inflater.inflate(R.layout.timepicker, null);
        ScreenInfo screenInfo = new ScreenInfo(this);
        final WheelMain wheelMain = new WheelMain(timepickerview, true);
        wheelMain.screenheight = screenInfo.getHeight();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        wheelMain.initDateTimePicker(year, month, day, 0, 0);

        dialog = new BSDialog(this, "请选择日期", timepickerview, new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mStartTimeTv.setText(wheelMain.getTime());

                mStartTimeTv.setText(wheelMain.getTime());
                dialog.dismiss();

            }
        });
        dialog.show();

    }

    public boolean getData() {
        try {

            String total = "";
            if (mFeeTotal.getText().toString().length() > 0) {
                total = mFeeTotal.getText().toString();
            } else {
                total = mRealFeeTotal.getText().toString();
            }
            String strUlr = UrlUtil.getApprovalFeeApplyUlr(Constant.APPROVAL_FEE_APPLY, mUid, mType, total);
            String jsonStr = HttpClientUtil.get(strUlr, Constant.ENCODING).trim();
            Gson gson = new Gson();
            mOvertimeResultVO = gson.fromJson(jsonStr, OvertimeResultVO.class);

            if (Constant.RESULT_CODE.equals(mOvertimeResultVO.getCode())) {
                mOvertimeVO = mOvertimeResultVO.getArray();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void executeSuccess() {
        if (mOvertimeVO != null) {
            mApprovalPerson.setLength(0);
            if (mOvertimeVO.getAppUser() != null) {
                mApproverTv.setVisibility(View.VISIBLE);
                mApproverLayout.setVisibility(View.VISIBLE);
                for (int i = 0; i < mOvertimeVO.getAppUser().size(); i++) {
                    mApprovalPerson.append(mOvertimeVO.getAppUser().get(i).getUserid());
                    if (i != mOvertimeVO.getAppUser().size() - 1) {
                        mApprovalPerson.append(",");
                    }
                }
                mApproverAdapter.setApproval(true);
                mApproverAdapter.updateData(mOvertimeVO.getAppUser());
            } else {
                mApproverTv.setVisibility(View.GONE);
                mApproverLayout.setVisibility(View.GONE);
            }
        }

        mInformPerson.setLength(0);
        mInformAdapter.mList.clear();
        mInformAdapter.notifyDataSetChanged();
        if (mOvertimeVO != null) {
            if (mOvertimeVO.getInsUser() != null) {
                for (int i = 0; i < mOvertimeVO.getInsUser().size(); i++) {
                    mInformPerson.append(mOvertimeVO.getInsUser().get(i).getUserid());
                    if (i != mOvertimeVO.getInsUser().size() - 1) {
                        mInformPerson.append(",");
                    }
                }
                mInformTv.setVisibility(View.VISIBLE);
                mInformLayout.setVisibility(View.VISIBLE);
                mInformAdapter.updateData(mOvertimeVO.getInsUser());
            }
        }

    }

    @Override
    public void executeFailure() {
        if (mOvertimeResultVO != null)
            CustomToast.showLongToast(this, mOvertimeResultVO.getRetinfo());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_time_layout:
                initDateView();

                break;

            case R.id.cancel:
                this.finish();
                break;
            case R.id.sure:

                break;
            case R.id.img_head_back:
                finish();
                break;

            case R.id.linked_layout:
                Intent intent = new Intent();
                intent.setClass(this, ApprovalBorrowActivity.class);
                this.startActivityForResult(intent, 0x000003);
                mRealFeeTotal.setVisibility(View.VISIBLE);
                mMoneyStatus.setVisibility(View.VISIBLE);
                break;

            case R.id.item_layout:
                mLinkedLayout.setVisibility(View.VISIBLE);
                mBorrowItmeLayout.setVisibility(View.GONE);

                mContentMark.setVisibility(View.GONE);
                mFeeTotalTv.setVisibility(View.GONE);
                mFeeTotal.setVisibility(View.VISIBLE);
                mRealFeelTotalLayout.setVisibility(View.GONE);

                mRealFeeTotal.setText("");
                mMoneyStatus.setText("");
                mMoney = "";
                mRealFeeTotal.setVisibility(View.GONE);
                mMoneyStatus.setVisibility(View.GONE);
                break;

            case R.id.leave_layout:

                if (mBsPopupWindowsTitle == null) {
                    List<CrmOptionsVO> parentList = new ArrayList<CrmOptionsVO>();
                    /* 自己创建筛选菜单 */
                    CrmOptionsVO mCrmOptionsVO2;
                    for (int i = 0; i < mOptionsVo.getOption().size(); i++) {
                        mCrmOptionsVO2 = new CrmOptionsVO();
                        mCrmOptionsVO2.setId(mOptionsVo.getOption().get(i).getAtid() + "");
                        mCrmOptionsVO2.setName(mOptionsVo.getOption().get(i).getName() + "");
                        parentList.add(mCrmOptionsVO2);
                    }
                    ArrayList<TreeVO> list = getOneLeveTreeVo(parentList);
                    mBsPopupWindowsTitle = new BSPopupWindowsTitle(this, list, callback,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                mBsPopupWindowsTitle.showPopupWindow(mTitleNamely);

                break;

            case R.id.txt_comm_head_right:

                if (mContentReason.getText().length() == 0) {
                    CustomToast.showLongToast(this, "请输入申请原因");
                    return;
                }

                if (mFeeTotal.getVisibility() == View.VISIBLE && mFeeTotal.getText().length() == 0) {
                    CustomToast.showLongToast(this, "请输入申请费用");
                    return;
                }
                if (mContentReason.getText().length() == 0) {
                    CustomToast.showLongToast(this, "请输入内容");
                    return;
                }
                if (mApprovalPerson.length() == 0) {
                    CustomToast.showLongToast(this, "由于你的权限过高，无法发布此审批");
                    return;
                }

                if (mContentMark.getVisibility() == View.VISIBLE) {
                    if (mContentMark.getText().toString().length() > 0) {
                        CustomToast.showLongToast(this, "请填写备注");
                    }
                }
                if (mFlag) {
                    mFlag = false;
                    commit();
                }

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
            if ("7".equals(mType)) {
                mLinkedLayout.setVisibility(View.GONE);
                mRealFeelTotalLayout.setVisibility(View.GONE);
                mFeeTotalTv.setVisibility(View.GONE);
            }
            else {
                mLinkedLayout.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long arg2) {
        if (parent == mGrideviewUpload) {
            // if (arg2 == mAdapter.mList.size()) {
            // mPop = new BSUPloadPopWindows(this, mParentView, null, null, 0);
            //
            // } else {
            // mPop = new BSUPloadPopWindows(this, mParentView, mAdapter, mAdapter.mList.get((int)
            // arg2), (int) arg2);
            // }

            ImageActivityUtils.setImageForActivity(view, ApprovalFeeApplyActivity.this, mAdapter, (int) arg2);
        }

        if (parent == mInformGv) {
            if (arg2 == mInformAdapter.mList.size()) {
                Intent intent = new Intent();
                intent.setClass(this, AddByDepartmentActivity.class);
                intent.putExtra("employ_name", JournalPublishActivity.class);
                intent.putExtra("requst_number", 10);
                startActivityForResult(intent, 10);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

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

            case BORROW_LIST:
                if (data == null)
                    return;
                mDelid = data.getStringExtra("fdid");
                mMoney = data.getStringExtra("money");
                mBorrowTitle = data.getStringExtra("borrow_title");
                String time = data.getStringExtra("time");
                mBorrowItmeLayout.setVisibility(View.VISIBLE);
                mLinkedLayout.setVisibility(View.GONE);

                mFeeTotalTv.setText(mMoney);
                mBorrowTitleTv.setText(mBorrowTitle);
                mBorrowFeeTv.setText(mMoney + "元");
                mBorrowTimeTv.setText(DateUtils.parseDateDay(time));
                mStartTimeTv.setText(DateUtils.parseDateDay(time));

                mFeeTotalTv.setVisibility(View.VISIBLE);
                mFeeTotal.setVisibility(View.INVISIBLE);
                mRealFeelTotalLayout.setVisibility(View.VISIBLE);
                new ThreadUtil(ApprovalFeeApplyActivity.this, ApprovalFeeApplyActivity.this).start();
                break;
        }
    }

    public boolean commit() {
        CustomDialog.showProgressDialog(this, "正在提交数据...");
        RequestParams params = new RequestParams();

        try {
            params.put("ftoken", BSApplication.getInstance().getmCompany());
            params.put("userid", BSApplication.getInstance().getUserId());
            params.put("type", mType);
            params.put("atime", mStartTimeTv.getText().toString());
            params.put("reason", mContentReason.getText().toString());
            params.put("remarks", mBorrowTitle);

            if (mFeeTotal.getVisibility() == View.VISIBLE) {
                // 输入的
                params.put("totalprice", mFeeTotal.getText().toString());
            } else {
                // TextView不可输入的
                params.put("totalprice", mRealFeeTotal.getText().toString());
            }

            params.put("approver", mApprovalPerson.toString());
            params.put("insider", mInformPerson.toString());

            if (mRealFeeTotal.getVisibility() == View.VISIBLE) {
                params.put("money", mMoney);
                params.put("delid", mDelid);
            }
            for (int i = 0; i < mAdapter.mPicList.size(); i++) {
                File file = CommonUtils.bitmapToString(this, mAdapter.mPicList.get(i));
                params.put("insider" + i, file);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        // params.put("name", "woshishishi");// 传输的字符数据
        String url = BSApplication.getInstance().getHttpTitle() + Constant.APPROVAL_FEE_APPLY_PUSH;
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
                        ApprovalFeeApplyActivity.this.finish();
                        CustomToast.showShortToast(ApprovalFeeApplyActivity.this, str);
                    } else {
                        CustomToast.showShortToast(ApprovalFeeApplyActivity.this, str);
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

    @Override
    public boolean execute() {
        return getData();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        return super.onTouchEvent(event);
    }
}
