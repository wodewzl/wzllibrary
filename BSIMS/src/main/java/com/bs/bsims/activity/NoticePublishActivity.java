
package com.bs.bsims.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.UploadAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.image.selector.ImageActivityUtils;
import com.bs.bsims.interfaces.RecordResult;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.model.NoticeObjectVO;
import com.bs.bsims.model.PDFOutlineElementVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.SoundRecordUtil;
import com.bs.bsims.view.BSDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NoticePublishActivity extends BaseActivity implements OnClickListener, OnItemClickListener {
    private static final int TAKE_PICTURE = 0x000001;
    private static final int RESULT_LOAD_IMAGE = 0x000002;
    private static final int BY_DEPARTMENTS = 2015;
    private LinearLayout mDepartmentLayout;
    private List<LinearLayout> list;
    private EditText mNoticeTextEt, mNumber;
    private SoundRecordUtil mRecordUtil;
    private List mTextViewId;
    // private TextView mTitleTv, mOkTv, mBackTv;
    private TextView mHeadBackImag;
    private View mReceiverLayout;

    private List<NoticeObjectVO> mTwoList = new ArrayList<NoticeObjectVO>();
    private List<EmployeeVO> mOneList = new ArrayList<EmployeeVO>();

    private EditText mTitleEt;
    private StringBuffer mSb = new StringBuffer();
    private StringBuffer mNoticeSB = new StringBuffer();
    private String mIscomment = "1";
    private TextView mDocumentTypeTv, mTypeId, mNoticePerson;

    private String mSortid, mClassid;

    private LinearLayout mDocumentTypeLayout, mTypeIdLayout;
    private String[] mNoticeTypeArray = {
            "2,行政通知", "1,人事通知"
    };
    private String[] mDocumentTypeArray = {
            "1,会议记录", "2,公告", "3,工作报告"
    };

    // 上传图片使用
    private List<String> mPicturePathList;
    private GridView mGrideviewUpload;
    private UploadAdapter mAdapter;
    private ImageView mUploadImg, mVoiceImg, mNumberImg, mInformImg;
    private BSDialog mDialog;
    private String mAll = "0"; // 1为全部

    private PopupWindow mOkPop;
    private List<PopupWindow> mListpop = new ArrayList<PopupWindow>();
    private View mBottomLine;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        // mTitleTv = (TextView) findViewById(R.id.txt_comm_head_activityName);
        // mOkTv = (TextView) findViewById(R.id.txt_comm_head_right);
        mBottomLine = findViewById(R.id.bottom_line);
        mHeadBackImag = (TextView) findViewById(R.id.img_head_back);
        mTitleEt = (EditText) findViewById(R.id.title_et);
        mDepartmentLayout = (LinearLayout) findViewById(R.id.publish_notice_departments);
        mNoticeTextEt = (EditText) findViewById(R.id.edit_notice_text);
        mReceiverLayout = findViewById(R.id.receiver_layout);
        mNumber = (EditText) findViewById(R.id.number);
        mTitleTv.setText(R.string.publish_notice);
        mOkTv.setText(R.string.ok);

        mDocumentTypeLayout = (LinearLayout) findViewById(R.id.document_type_layout);
        mDocumentTypeTv = (TextView) findViewById(R.id.doucment_type_tv);

        mTypeId = (TextView) findViewById(R.id.type_id);
        mTypeIdLayout = (LinearLayout) findViewById(R.id.type_id_layout);
        mNoticePerson = (TextView) findViewById(R.id.notice_persontv);
        mUploadImg = (ImageView) findViewById(R.id.upload_img);
        mVoiceImg = (ImageView) findViewById(R.id.voice_img);
        mNumberImg = (ImageView) findViewById(R.id.number_img);
        mInformImg = (ImageView) findViewById(R.id.inform_img);

        // 上传图片使用
        mGrideviewUpload = (GridView) findViewById(R.id.grideview_upload);
        mAdapter = new UploadAdapter(this, "2");
        mGrideviewUpload.setAdapter(mAdapter);
        mPicturePathList = new ArrayList<String>();
        initData();
    }

    public void initData() {
        mRecordUtil = new SoundRecordUtil(this, recordResult);
        list = new ArrayList<LinearLayout>();
        mTextViewId = new ArrayList<Integer>();
        Intent intent = getIntent();
        mSortid = intent.getStringExtra("sortid");
        if ("3".equals(mSortid)) {
            mNumber.setText(Constant.TYPE_NOTICE + System.currentTimeMillis() / 1000);
            mTypeId.setText("通知编号：");

            // 公文类型
            mDocumentTypeLayout.setVisibility(View.VISIBLE);
            if (CommonUtils.getLimitsPublish(Constant.LIMITS_PUBLISH004) && CommonUtils.getLimitsPublish(Constant.LIMITS_PUBLISH005)) {
                mDocumentTypeTv.setText("行政通知");
                mDocumentTypeTv.setTag("2");
                mTitleTv.setText("发布通知");
            } else if (CommonUtils.getLimitsPublish(Constant.LIMITS_PUBLISH004)) {
                mDocumentTypeTv.setText("行政通知");
                mDocumentTypeTv.setTag("2");
                mDocumentTypeTv.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                mTitleTv.setText("发布行政通知");
            } else {
                mDocumentTypeTv.setText("人事通知");
                mDocumentTypeTv.setTag("1");
                mDocumentTypeTv.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                mTitleTv.setText("发布人事通知");
            }

        } else if ("11".equals(mSortid)) {
            mNumber.setText(Constant.TYPE_DOCUMENT + System.currentTimeMillis() / 1000);
            mTitleTv.setText("发布公文");
            mDocumentTypeLayout.setVisibility(View.VISIBLE);
            mDocumentTypeTv.setText("会议记录");
            mDocumentTypeTv.setTag("1");
            mClassid = "1";
            mTypeId.setText("公文编号：");
        } else if ("19".equals(mSortid)) {
            mTitleTv.setText("风采发布");
            mReceiverLayout.setVisibility(View.GONE);
            mTypeIdLayout.setVisibility(View.GONE);
        }

    }

    public void bindViewsListener() {
        mReceiverLayout.setOnClickListener(this);
        mHeadBackImag.setOnClickListener(this);
        mOkTv.setOnClickListener(this);
        mDocumentTypeLayout.setOnClickListener(this);
        mGrideviewUpload.setOnItemClickListener(this);
        mUploadImg.setOnClickListener(this);
        mVoiceImg.setOnClickListener(this);
        mNumberImg.setOnClickListener(this);
        mInformImg.setOnClickListener(this);
        mNoticePerson.setOnClickListener(this);
    }

    RecordResult recordResult = new RecordResult() {

        @Override
        public void getRecordResult(String result) {
            mNoticeSB.append(result);
            mNoticeTextEt.setText(mNoticeTextEt.getText() + result);
        }

    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case ImageActivityUtils.REQUEST_IMAGE:
                if (resultCode == RESULT_OK) {
                    ImageActivityUtils.setImageGetActivity(data, mAdapter);
                }
                break;

            /* 图片预览之后返回删除图片了 piclist */

            case ImageActivityUtils.REQUEST_IMAGE_BYSDCARD:
                if (resultCode == RESULT_OK) {
                    ImageActivityUtils.imgPreviewDelteActivity(data, mAdapter);
                }
                break;

            case BY_DEPARTMENTS:
                if (data == null)
                    return;
                if (resultCode == BY_DEPARTMENTS) {

                    if (data.getSerializableExtra("users_id") != null) {
                        mSb.setLength(0);
                        String usersId = (String) data.getSerializableExtra("users_id");
                        mSb.append(usersId);
                        mAll = "0";
                    } else if (data.getSerializableExtra("all_id") != null) {
                        mSb.setLength(0);
                        mAll = "1";
                    }

                    if (mSb.length() > 0||"1".equals(mAll)) {
                        if ("1".equals(mAll))
                            mNoticePerson.setText("全部人员");
                        else
                            mNoticePerson.setText(mSb.toString().split(",").length+"人");
                    } else {
                        mInformImg.setImageResource(R.drawable.notice_person_normal);
                    }
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ImageActivityUtils.crashBoBitmap(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.img_head_back:
                this.finish();
                break;
            case R.id.txt_comm_head_right:
                if (mTitleEt.getText().length() == 0) {
                    CustomToast.showLongToast(this, "请填写标题");
                    return;
                }

                if (!"19".equals(mSortid)) {
                    if (mSb.length() == 0 && "0".equals(mAll)) {
                        CustomToast.showLongToast(this, "请选择通知对象");
                        return;
                    }
                }

                if (mNoticeTextEt.getText().length() == 0) {
                    CustomToast.showLongToast(this, "请填写内容");
                    return;
                }
                upload();
                break;

            case R.id.receiver_layout:
                intent.setClass(this, NoticeObjectActivity.class);
                intent.putExtra("notice_publish", NoticePublishActivity.class);
                intent.putExtra("oneList", (Serializable) mOneList);
                intent.putExtra("twoList", (Serializable) mTwoList);
                intent.putExtra("requst_number", BY_DEPARTMENTS);
                startActivityForResult(intent, BY_DEPARTMENTS);
                break;
            case R.id.document_type_layout:
                if ("3".equals(mSortid)) {
                    if (CommonUtils.getLimitsPublish(Constant.LIMITS_PUBLISH004) && CommonUtils.getLimitsPublish(Constant.LIMITS_PUBLISH005))
                        CommonUtils.initSimpleListDialog(this, "请选择通知类型", mNoticeTypeArray, mDocumentTypeTv);
                } else {
                    CommonUtils.initSimpleListDialog(this, "请选择公文类型", mDocumentTypeArray, mDocumentTypeTv);
                }
                break;

            case R.id.upload_img:
                // if (mAdapter.mList.size() == 3) {
                // CustomToast.showLongToast(this, "最多只能上传3张");
                // return;
                // }
                //
                // CommonUtils.systemPhoto(this);
                ImageActivityUtils.setImageForActivity(v, NoticePublishActivity.this, mAdapter, mAdapter.mList.size());
                break;
            case R.id.voice_img:
                mRecordUtil.startVoice();
                break;
            case R.id.number_img:
                showDialog(this);
                break;
            case R.id.notice_persontv:
                intent.setClass(this, NoticePersonActivity.class);

                if ("1".equals(mAll)) {
                    intent.putExtra("all_id", "1");
                } else if (mSb.length() > 0) {
                    intent.putExtra("users_id", mSb.toString());
                }

                intent.putExtra("requst_number", BY_DEPARTMENTS);
                startActivityForResult(intent, BY_DEPARTMENTS);
                break;

            default:
                break;
        }

    }

    // 弹出edittex对话框
    public void showDialog(final Context context) {

        View v = LayoutInflater.from(context).inflate(R.layout.dialog_edittext, null);
        final EditText editText = (EditText) v.findViewById(R.id.edit_content);
        editText.setText(mNumber.getText().toString());
        mDialog = new BSDialog(context, "请填写编号", v, new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mDialog.dismiss();
                if (editText.getText().toString().trim().length() > 0) {
                    mNumber.setText(editText.getText().toString());
                }
            }
        });
        mDialog.show();
    }

    private class NoticePublishListener implements OnClickListener {
        private TextView textView;
        private LinearLayout layout;
        private PDFOutlineElementVO employeeVo;

        public NoticePublishListener(TextView tv, LinearLayout layout, PDFOutlineElementVO employeeVo) {
            this.textView = tv;
            this.layout = layout;
            this.employeeVo = employeeVo;
        }

        @Override
        public void onClick(View v) {
        }

    }

    private class NoticePostPublishListener implements OnClickListener {
        private TextView textView;
        private LinearLayout layout;
        private NoticeObjectVO noticeObjectVO;

        public NoticePostPublishListener(TextView tv, LinearLayout layout, NoticeObjectVO noticeObjectVO) {
            this.textView = tv;
            this.layout = layout;
            this.noticeObjectVO = noticeObjectVO;
        }

        @Override
        public void onClick(View v) {
            layout.removeView(textView);
            mTwoList.remove(noticeObjectVO);
            mSb.setLength(0);
            for (int i = 0; i < mTwoList.size(); i++) {
                mSb.append(mTwoList.get(i).getPositionsid());
                if (i != mTwoList.size() - 1) {
                    mSb.append(",");
                }
            }

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View arg0, int arg1, long arg2) {
        // int position = (int) arg2;
        // mAdapter.mList.remove(position);
        // mAdapter.mPicList.remove(position);
        // mAdapter.notifyDataSetChanged();

        ImageActivityUtils.setImageForActivity(arg0, NoticePublishActivity.this, mAdapter, (int) arg2);
    }

    public boolean upload() {
        CustomDialog.showProgressDialog(this, "正在提交数据...");
        RequestParams params = new RequestParams();

        try {
            params.put("ftoken", BSApplication.getInstance().getmCompany());
            params.put("userid", BSApplication.getInstance().getUserId());
            params.put("sortid", mSortid);
            if ("11".equals(mSortid))
                params.put("classid", mDocumentTypeTv.getTag().toString());// 公文类型区分
            if ("3".equals(mSortid))
                params.put("noticeid", mDocumentTypeTv.getTag().toString());// 通知类型区分
            params.put("title", mTitleEt.getText().toString());
            params.put("content", mNoticeTextEt.getText().toString());

            // 部门（部门岗位只能有一个）
            if (mSb.length() > 0)
                params.put("receivingobj", "users");

            // 岗位

            if ("1".equals(mAll)) {
                params.put("receivingobj", "all");
            }

            params.put("receivingid", mSb.toString());
            params.put("serial_number", mNumber.getText().toString());

            if ("19".equals(mSortid)) {
                params.put("receivingobj", "all");
            }
            params.put("iscomment", mIscomment);

            // 上传附件
            for (int i = 0; i < mAdapter.mPicList.size(); i++) {
                File file = CommonUtils.bitmapToString(this, mAdapter.mPicList.get(i));
                params.put("file" + i, file);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        String url = BSApplication.getInstance().getHttpTitle() + Constant.NOTICE_PUBILSH;
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
                    String code = (String) jsonObject.get("code");
                    String str = (String) jsonObject.get("retinfo");
                    if (Constant.RESULT_CODE.equals("200")) {
                        CustomToast.showShortToast(NoticePublishActivity.this, str);
                        NoticePublishActivity.this.finish();
                    } else {
                        CustomToast.showShortToast(NoticePublishActivity.this, "提交失败");
                    }
                    CustomDialog.closeProgressDialog();

                } catch (Exception e) {
                    e.printStackTrace();
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

    public void showPopupWindow(View anchor) {
        final PopupWindow popupWindow = new PopupWindow(this);
        View contentView = LayoutInflater.from(this).inflate(R.layout.document_type, null);
        contentView.findViewById(R.id.type01).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mDocumentTypeTv.setText("会议记录");
                mClassid = "1";
                popupWindow.dismiss();
            }
        });
        contentView.findViewById(R.id.type02).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mDocumentTypeTv.setText("公告");
                mClassid = "2";
                popupWindow.dismiss();
            }
        });
        contentView.findViewById(R.id.type03).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mDocumentTypeTv.setText("工作报告");
                mClassid = "3";
                popupWindow.dismiss();
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
    public void baseSetContentView() {
        // setContentView(R.layout.notice_publish);
        View.inflate(this, R.layout.notice_publish, mContentLayout);
    }

    @Override
    public boolean getDataResult() {
        return true;
    }

    @Override
    public void updateUi() {

    }

}
