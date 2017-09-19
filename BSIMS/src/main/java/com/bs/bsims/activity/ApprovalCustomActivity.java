
package com.bs.bsims.activity;

import android.content.Context;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.LoginActivity.MyAdapter;
import com.bs.bsims.adapter.HeadAdapter;
import com.bs.bsims.adapter.UploadAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.image.selector.ImageActivityUtils;
import com.bs.bsims.model.CrmOptionsVO;
import com.bs.bsims.model.CustomApprovalListVO;
import com.bs.bsims.model.CustomApprovalVO;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.model.TreeVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.JsonUtil;
import com.bs.bsims.utils.ThreadUtil;
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
import java.util.HashMap;
import java.util.List;

public class ApprovalCustomActivity extends BaseActivity implements OnItemClickListener, OnClickListener {
    public static final int CUSTOM_TYPE_ONE = 1;// TextView 类型
    public static final int CUSTOM_TYPE_TWO = 2;// EditText 类型
    public static final int CUSTOM_TYPE_THREE = 3;// Button点击弹出时间控件
    public static final int CUSTOM_TYPE_FOUR = 4;// EditText 只能输入数字
    public static final int CUSTOM_TYPE_FIVE = 5;// 下拉列表

    private static final int TAKE_PICTURE = 0x000001;// 拍照返回码
    private static final int RESULT_LOAD_IMAGE = 0x000002;
    private static final int ADD_PERSON = 2014;
    private static final int ADD_INFORM_PERSON = 10;

    // 添加员工使用
    protected List<EmployeeVO> mDataList = new ArrayList<EmployeeVO>();
    public static final String EDITTEXT_HINT = "请输入";
    private CustomApprovalVO mCustomApprovalVO;
    private String mAtid;
    private LinearLayout mLayout, mCustomLyaoutOne, mCustomLyaoutTwo, mCustomLyaoutThree, mCustomLyaoutFour, mCustomLyaoutFive;
    private BSDialog mDialog;
    private PopupWindow mPopView;
    private MyAdapter dropDownAdapter;

    // 上传图片
    private GridView mGrideviewUpload;
    private UploadAdapter mAdapter;
    private List<String> mPicturePathList;
    private LinearLayout mParentView;
    private BSUPloadPopWindows mPop;

    // 知会人，审批人
    private BSGridView mApproverGv, mInformGv;
    private HeadAdapter mApproverAdapter, mInformAdapter;
    private TextView mApproverTv, mApprovalGoTv, mInformTv, mInformGoTv, mCancel, mSure, mApprovalType;
    private LinearLayout mApproverLayout, mInformLayout, mLeavly;
    private StringBuffer mApprovalPerson, mInformPerson;

    private String mInfoJson;
    private String[] mTypeVale;

    private BSPopupWindowsTitle mBsPopupWindowsTitle;
    private CustomApprovalListVO mOptionsVo;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.approval_custom, mContentLayout);
        ImageActivityUtils.setOutWindowAnimations(this);
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    @Override
    public void updateUi() {

        if (mCustomApprovalVO.getArray() != null) {
            List<CustomApprovalVO> list = mCustomApprovalVO.getArray().getInfo();
            mTypeVale = new String[list.size()];

            for (int i = 0; i < list.size(); i++) {
                switch (Integer.parseInt(CommonUtils.isNormalData(list.get(i).getOtype()))) {
                    case CUSTOM_TYPE_ONE:
                        mCustomLyaoutOne = (LinearLayout) View.inflate(this, R.layout.approval_custom_1, null);
                        mCustomLyaoutOne.setTag(i);
                        mLayout.addView(mCustomLyaoutOne);

                        TextView tv_1 = (TextView) mCustomLyaoutOne.findViewById(R.id.type_title);
                        tv_1.setText(list.get(i).getOname() + ":");
                        EditText et_1 = (EditText) mCustomLyaoutOne.findViewById(R.id.type_content);
                        et_1.setHint(EDITTEXT_HINT + list.get(i).getOname());
                        mCustomLyaoutOne.setOnClickListener(new ApprovalCustomListener());
                        mCustomLyaoutOne.setId(1);

                        break;
                    case CUSTOM_TYPE_TWO:
                        mCustomLyaoutTwo = (LinearLayout) View.inflate(this, R.layout.approval_custom_2, null);
                        mLayout.addView(mCustomLyaoutTwo);
                        mCustomLyaoutTwo.setTag(i);

                        TextView tv_2 = (TextView) mCustomLyaoutTwo.findViewById(R.id.type_title);
                        tv_2.setText(list.get(i).getOname());

                        EditText et_2 = (EditText) mCustomLyaoutTwo.findViewById(R.id.type_content);
                        et_2.setHint(EDITTEXT_HINT + list.get(i).getOname());
                        mCustomLyaoutTwo.setOnClickListener(new ApprovalCustomListener());
                        mCustomLyaoutTwo.setId(2);
                        break;
                    case CUSTOM_TYPE_THREE:
                        mCustomLyaoutThree = (LinearLayout) View.inflate(this, R.layout.approval_custom_3, null);
                        mLayout.addView(mCustomLyaoutThree);
                        mCustomLyaoutThree.setTag(i);

                        TextView tv_3 = (TextView) mCustomLyaoutThree.findViewById(R.id.type_title);
                        tv_3.setText(list.get(i).getOname() + ":");
                        mCustomLyaoutThree.setOnClickListener(new ApprovalCustomListener());
                        mCustomLyaoutThree.setId(3);
                        break;
                    case CUSTOM_TYPE_FOUR:
                        mCustomLyaoutFour = (LinearLayout) View.inflate(this, R.layout.approval_custom_4, null);
                        mLayout.addView(mCustomLyaoutFour);
                        mCustomLyaoutFour.setTag(i);

                        TextView tv_4 = (TextView) mCustomLyaoutFour.findViewById(R.id.type_title);
                        tv_4.setText(list.get(i).getOname() + ":");
                        EditText et_4 = (EditText) mCustomLyaoutFour.findViewById(R.id.type_content);
                        et_4.setHint(EDITTEXT_HINT + list.get(i).getOname());
                        mCustomLyaoutFour.setOnClickListener(new ApprovalCustomListener());
                        mCustomLyaoutFour.setId(4);
                        break;
                    case CUSTOM_TYPE_FIVE:
                        mCustomLyaoutFive = (LinearLayout) View.inflate(this, R.layout.approval_custom_5, null);
                        mLayout.addView(mCustomLyaoutFive);
                        mCustomLyaoutFive.setTag(i);

                        TextView tv_5 = (TextView) mCustomLyaoutFive.findViewById(R.id.type_title);
                        tv_5.setText(list.get(i).getOname() + ":");

                        mCustomLyaoutFive.setOnClickListener(new ApprovalCustomListener(list.get(i).getOptions().split(",")));
                        mCustomLyaoutFive.setId(5);
                        break;

                    default:

                        break;
                }
            }

            // 审批人
            mApprovalPerson.setLength(0);
            if (mCustomApprovalVO.getArray().getAppUser() != null) {
                mApproverTv.setVisibility(View.VISIBLE);
                mApproverLayout.setVisibility(View.VISIBLE);
                for (int i = 0; i < mCustomApprovalVO.getArray().getAppUser().size(); i++) {
                    mApprovalPerson.append(mCustomApprovalVO.getArray().getAppUser().get(i).getUserid());
                    if (i != mCustomApprovalVO.getArray().getAppUser().size() - 1) {
                        mApprovalPerson.append(",");
                    }
                }
                mApproverAdapter.setApproval(true);
                mApproverAdapter.updateData(mCustomApprovalVO.getArray().getAppUser());
            } else {
                mApproverTv.setVisibility(View.GONE);
                mApproverLayout.setVisibility(View.GONE);
            }

            mInformPerson.setLength(0);
            mInformAdapter.mList.clear();
            mInformAdapter.notifyDataSetChanged();
            if (mCustomApprovalVO.getArray().getInsUser() != null) {
                for (int i = 0; i < mCustomApprovalVO.getArray().getInsUser().size(); i++) {
                    mInformPerson.append(mCustomApprovalVO.getArray().getInsUser().get(i).getUserid());
                    if (i != mCustomApprovalVO.getArray().getInsUser().size() - 1) {
                        mInformPerson.append(",");
                    }
                }
                mInformTv.setVisibility(View.VISIBLE);
                mInformLayout.setVisibility(View.VISIBLE);
                mInformAdapter.updateData(mCustomApprovalVO.getArray().getInsUser());
            }
        }

    }

    @Override
    public void initView() {
        mLayout = (LinearLayout) findViewById(R.id.custom_layout);
        mPopView = new PopupWindow();

        // 上传图片使用
        mGrideviewUpload = (GridView) findViewById(R.id.grideview_upload);
        mAdapter = new UploadAdapter(this);
        mGrideviewUpload.setAdapter(mAdapter);
        mParentView = (LinearLayout) getLayoutInflater().inflate(R.layout.creative_idea_new, null);
        mPicturePathList = new ArrayList<String>();

        mApproverGv = (BSGridView) findViewById(R.id.approver_gv);
        mInformGv = (BSGridView) findViewById(R.id.inform_gv);
        mLeavly = (LinearLayout) findViewById(R.id.leave_layout);
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

        mApprovalType = (TextView) findViewById(R.id.approval_type);

        initData();
    }

    @Override
    public void bindViewsListener() {
        mGrideviewUpload.setOnItemClickListener(this);
        mSure.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        mLeavly.setOnClickListener(this);
    }

    public void initData() {
        Intent intent = this.getIntent();
        mTitleTv.setText("自定义审批");
        mOptionsVo = (CustomApprovalListVO) intent.getSerializableExtra("options");
        if (intent.getStringExtra("atid") != null) {
            mAtid = intent.getStringExtra("atid");
            mApprovalType.setText(intent.getStringExtra("title"));
        }
        else {
            mAtid = getIntent().getStringExtra("apid");
            mApprovalType.setText(getIntent().getStringExtra("approvalType"));
        }

    }

    public boolean getData() {
        try {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put("atid", mAtid);
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.CUSTOM_APPROVAL, map);
            Gson gson = new Gson();
            mCustomApprovalVO = gson.fromJson(jsonStr, CustomApprovalVO.class);
            if (Constant.RESULT_CODE.equals(mCustomApprovalVO.getCode())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    class ApprovalCustomListener implements OnClickListener {
        private String[] array;

        public ApprovalCustomListener() {
        }

        public ApprovalCustomListener(String[] array) {
            this.array = array;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case 1:

                    break;
                case 2:

                    break;
                case 3:
                    TextView tv = (TextView) v.findViewById(R.id.type_content);
                    CommonUtils.initDateView(ApprovalCustomActivity.this, tv);
                    break;
                case 4:

                    break;
                case 5:
                    TextView tv_5 = (TextView) v.findViewById(R.id.type_content);
                    CommonUtils.initPopView(ApprovalCustomActivity.this, array, v);
                    break;

                default:
                    break;
            }
        }
    }

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

            // case RESULT_LOAD_IMAGE:
            //
            // if (resultCode == RESULT_OK && null != data) {
            // Uri selectedImage = data.getData();
            // String[] filePathColumn = {
            // MediaStore.Images.Media.DATA
            // };
            //
            // String picturePath;
            // Cursor cursor = getContentResolver().query(selectedImage,
            // filePathColumn, null, null, null);
            // if (cursor == null) {
            // picturePath = selectedImage.getPath();
            // } else {
            // cursor.moveToFirst();
            // int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            // picturePath = cursor.getString(columnIndex);
            // cursor.close();
            // }
            //
            // mPicturePathList.add(picturePath);
            // mAdapter.mPicList.add(picturePath);
            // // mAdapter.mList.add(CommonUtils.getBitmapFromFile(file, 70, 70));
            // Bitmap bitmap = BitmapFactory.decodeFile(picturePath,
            // CommonUtils.getBitmapOption(2)); // 将图
            // mAdapter.mList.add(bitmap);
            // mAdapter.notifyDataSetChanged();
            // }
            // break;

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long arg2) {
        if (parent == mGrideviewUpload) {
            ImageActivityUtils.setImageForActivity(view, ApprovalCustomActivity.this, mAdapter, (int) arg2);
        }
    }

    public void commit(String info) {
        CustomDialog.showProgressDialog(this, "正在提交数据...");
        RequestParams params = new RequestParams();

        try {
            params.put("ftoken", BSApplication.getInstance().getmCompany());
            params.put("userid", BSApplication.getInstance().getUserId());
            params.put("info", info);
            params.put("typeid", mAtid);
            params.put("approver", mApprovalPerson.toString());
            params.put("insider", mInformPerson.toString());
            for (int i = 0; i < mAdapter.mPicList.size(); i++) {
                File file = CommonUtils.bitmapToString(this, mAdapter.mPicList.get(i));
                params.put("insider" + i, file);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        String url = BSApplication.getInstance().getHttpTitle() + Constant.CUSTOM_APPROVAL_ADD;
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
                        ApprovalCustomActivity.this.finish();
                        CustomToast.showShortToast(ApprovalCustomActivity.this, str);
                    } else {
                        CustomToast.showShortToast(ApprovalCustomActivity.this, str);
                    }
                    CustomDialog.closeProgressDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sure:
                List<CustomApprovalVO> list = mCustomApprovalVO.getArray().getInfo();
                mLayout.getChildCount();
                String typeValue = "";
                for (int i = 0; i < list.size(); i++) {
                    mLayout.getChildAt(i).findViewById(R.id.type_content);
                    if (mLayout.getChildAt(i).findViewById(R.id.type_content) instanceof TextView) {
                        TextView tv = (TextView) mLayout.getChildAt(i).findViewById(R.id.type_content);
                        typeValue = tv.getText().toString();
                    } else {
                        EditText ev = (EditText) mLayout.getChildAt(i).findViewById(R.id.type_content);
                        typeValue = ev.getText().toString();
                    }

                    if ("".equals(typeValue)) {
                        CustomToast.showLongToast(this, "请检查是否有没有填选项");
                        return;
                    }
                    list.get(i).setOvalue(typeValue);
                }

                if (mApprovalPerson.length() == 0) {
                    CustomToast.showLongToast(this, "由于你的权限过高，无法发布此审批");
                    return;
                }
                String jsonStr = JsonUtil.toJson(list);

                commit(jsonStr);

                break;

            case R.id.cancel:
                this.finish();
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
                    ArrayList<TreeVO> list1 = getOneLeveTreeVo(parentList);
                    mBsPopupWindowsTitle = new BSPopupWindowsTitle(this, list1, callback,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                mBsPopupWindowsTitle.showPopupWindow(mLeavly);
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
            mAtid = vo.getParentSerachId();
//            ApprovalCustomActivity.this.finish();
//            Intent i = new Intent();
//            i.putExtra("options", mOptionsVo);
//            i.putExtra("atid", mAtid);
//            i.putExtra("title", vo.getName());
//            i.setClass(ApprovalCustomActivity.this, ApprovalCustomActivity.class);
//            startActivity(i);
//     
            mApprovalType.setText( vo.getName());
            mLayout.removeAllViews();
            new ThreadUtil(ApprovalCustomActivity.this, ApprovalCustomActivity.this).start();
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null)
            im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        ImageActivityUtils.crashBoBitmap(mAdapter);
    }
}
