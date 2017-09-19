
package com.bs.bsims.activity;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts.Data;
import android.provider.ContactsContract.RawContacts;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.UploadAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.image.selector.ImageActivityUtils;
import com.bs.bsims.image.selector.MultiImageSelectorActivity;
import com.bs.bsims.model.ContactDepTabResultVO;
import com.bs.bsims.model.CrmOptionsVO;
import com.bs.bsims.time.ScreenInfo;
import com.bs.bsims.time.WheelMain;
import com.bs.bsims.utils.CommonImageUtils;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.view.BSCircleImageView;
import com.bs.bsims.view.BSDialog;
import com.bs.bsims.view.BSListView;
import com.bs.bsims.view.BSMoveLayout;
import com.bs.bsims.view.BSMoveLayout.DeleteListeren;
import com.bs.bsims.view.BSUPloadPopWindows;
import com.bs.bsims.view.BSUPloadPopWindows.ResultCallback;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrmClientAddContactsActivity extends BaseActivity implements OnClickListener {
    private static final int TAKE_PICTURE = 0x000001;// 拍照返回码
    private static final int RESULT_LOAD_IMAGE = 0x000002;

    private CrmOptionsVO mCrmOptionsVO;
    private EditText mEditText01, mEditText02, mEditText03, mEditText05, mEditText06, mEditText07, mEditText08, mEditText12, mEditText13;
    private TextView mTextView04, mTextView10, mTextView11, mTextView14, mTextView15;
    private LinearLayout mLinerLayout09, mLinerLayout16;
    private String mCid;
    private String mLid;
    private BSCircleImageView mAddHeadImg;

    private BSDialog mBSDialog, mBSDialogSex;

    // 上传图片
    private GridView mGrideviewUpload;
    private UploadAdapter mAdapter;
    private List<String> mPicturePathList;
    private LinearLayout mParentView;
    private BSUPloadPopWindows mPop;
    private String cname = "";
    private String type = ""; // 0是联系人列表添加跳转 1详情编辑跳转 2是客户列表添加跳转
    private ContactDepTabResultVO mContactResultVo, mContactsDetailEditVO;
    private BSListView mListView;
    private MoreItmeAdapter mMoreItmeAdapter;

    private List<ContactDepTabResultVO> mRelationshipsList = new ArrayList<ContactDepTabResultVO>();
    private List<ContactDepTabResultVO> mIntimacysList = new ArrayList<ContactDepTabResultVO>();
    private List<ContactDepTabResultVO> mCustomersList = new ArrayList<ContactDepTabResultVO>();
    private CheckBox mCheckBox;
    private EditText mEditText04;
    private String mStatus = "0";// 0默认什么都不是，1代表点击手动添加，2代表选择添加
    private BSDialog mDialog;
    private String mHeadIconPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.crm_client_add_contacts, mContentLayout);
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    public boolean getData() {
        try {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            Gson gson = new Gson();
            String jsonUrlStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.CRM_CONTACTS_ADD_INFO, map);
            mContactResultVo = gson.fromJson(jsonUrlStr, ContactDepTabResultVO.class);
            // if (Constant.RESULT_CODE.equals(mContactResultVo.getCode())) {
            // return true;
            // } else {
            // return false;
            // }
            if (HttpClientUtil.isNetworkConnected(this)) {
                if (mContactResultVo == null) {
                    getData();
                }
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void updateUi() {
        if ("1".equals(type)) {
            ImageLoader imageLoader = ImageLoader.getInstance();
            DisplayImageOptions options = CommonUtils.initImageLoaderOptions();
            imageLoader.displayImage(mContactsDetailEditVO.getLheadpic(), mAddHeadImg, options);
            if (CommonUtils.isNormalString(mContactsDetailEditVO.getLname()))
                mEditText01.setText(mContactsDetailEditVO.getLname());
            if (CommonUtils.isNormalString(mContactsDetailEditVO.getPost()))
                mEditText02.setText(mContactsDetailEditVO.getPost());
            if (CommonUtils.isNormalString(mContactsDetailEditVO.getDepartment()))
                mEditText03.setText(mContactsDetailEditVO.getDepartment());
            if (CommonUtils.isNormalString(mContactsDetailEditVO.getCname()))
                mTextView04.setText(mContactsDetailEditVO.getCname());
            if (CommonUtils.isNormalString(mContactsDetailEditVO.getWebsite()))
                mEditText05.setText(mContactsDetailEditVO.getWebsite());

            editUpdata(mContactsDetailEditVO.getPhone(), 0);
            editUpdata(mContactsDetailEditVO.getTel(), 1);
            editUpdata(mContactsDetailEditVO.getFax(), 2);
            editUpdata(mContactsDetailEditVO.getWechat(), 3);
            editUpdata(mContactsDetailEditVO.getQq(), 4);
            editUpdata(mContactsDetailEditVO.getEmail(), 5);
            editUpdata(mContactsDetailEditVO.getWeibo(), 6);

            if (CommonUtils.isNormalString(mContactsDetailEditVO.getSex()))
                mTextView10.setText(mContactsDetailEditVO.getSex());
            if (CommonUtils.isNormalString(mContactsDetailEditVO.getBirthday()))
                mTextView11.setText(mContactsDetailEditVO.getBirthday());
            if (CommonUtils.isNormalString(mContactsDetailEditVO.getHobby()))
                mEditText12.setText(mContactsDetailEditVO.getHobby());
            if (CommonUtils.isNormalString(mContactsDetailEditVO.getAddress()))
                mEditText13.setText(mContactsDetailEditVO.getAddress());
            if (CommonUtils.isNormalString(mContactsDetailEditVO.getIntimacy())) {
                mTextView14.setText(mContactsDetailEditVO.getRelationship());
                mTextView14.setTag(mContactsDetailEditVO.getIntimacy_id());
            }

            if (CommonUtils.isNormalString(mContactsDetailEditVO.getRelationship())) {
                mTextView15.setText(mContactsDetailEditVO.getIntimacy());
                mTextView15.setTag(mContactsDetailEditVO.getRelationship_id());
            }

        }

        if (mContactResultVo.getRelationships() != null)
            mRelationshipsList = mContactResultVo.getRelationships();
        if (mContactResultVo.getIntimacys() != null)
            mIntimacysList = mContactResultVo.getIntimacys();
        if (mContactResultVo.getCustomers() != null)
            mCustomersList = mContactResultVo.getCustomers();
    }

    public void editUpdata(String[] array, int position) {
        if (array == null) {
            mMoreItmeAdapter.list.add(addModel(position, ""));
            mMoreItmeAdapter.notifyDataSetChanged();
            return;
        }

        for (int i = 0; i < array.length; i++) {
            mMoreItmeAdapter.list.add(addModel(position, array[i]));
            mMoreItmeAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void initView() {

        mOkTv.setText("保存");
        mListView = (BSListView) findViewById(R.id.list_view);
        mMoreItmeAdapter = new MoreItmeAdapter(this);
        mListView.setAdapter(mMoreItmeAdapter);
        mEditText01 = (EditText) findViewById(R.id.edittext_01);
        mEditText02 = (EditText) findViewById(R.id.edittext_02);
        mEditText03 = (EditText) findViewById(R.id.edittext_03);
        mTextView04 = (TextView) findViewById(R.id.textview_04);
        mEditText04 = (EditText) findViewById(R.id.edit_text_04);
        mEditText05 = (EditText) findViewById(R.id.edittext_05);
        mLinerLayout09 = (LinearLayout) findViewById(R.id.add_more_09);
        mTextView10 = (TextView) findViewById(R.id.type_content);
        mTextView11 = (TextView) findViewById(R.id.textview_11);
        mEditText12 = (EditText) findViewById(R.id.edittext_12);
        mEditText13 = (EditText) findViewById(R.id.edittext_13);
        mTextView14 = (TextView) findViewById(R.id.textview_14);
        mTextView15 = (TextView) findViewById(R.id.textview_15);
        mLinerLayout16 = (LinearLayout) findViewById(R.id.save_to_contacts_16);

        mAddHeadImg = (BSCircleImageView) findViewById(R.id.add_head);
        mCheckBox = (CheckBox) findViewById(R.id.save_to_contacts);
        initData();
        if (type.equals("1")) {
            mTitleTv.setText("编辑联系人");
        } else {
            mTitleTv.setText("新增联系人");
        }
    }

    public ContactDepTabResultVO addModel(int position, String value) {
        ContactDepTabResultVO vo = new ContactDepTabResultVO();
        switch (position) {
            case 0:
                vo.setTypehint("手机（必填）");
                vo.setDrawableid(R.drawable.crm_client_tel);
                break;
            case 1:
                vo.setTypehint("电话");
                vo.setDrawableid(R.drawable.crm_client_phone);
                break;
            case 2:
                vo.setTypehint("传真");
                vo.setDrawableid(R.drawable.crm_client_fax);
                break;
            case 3:
                vo.setTypehint("微信");
                vo.setDrawableid(R.drawable.crm_client_weixin);
                break;
            case 4:
                vo.setTypehint("QQ");

                vo.setDrawableid(R.drawable.crm_client_qq_black);
                break;
            case 5:
                vo.setTypehint("邮箱");

                vo.setDrawableid(R.drawable.crm_client_email);
                break;
            case 6:
                vo.setTypehint("微博");
                vo.setDrawableid(R.drawable.crm_weibo);
                break;
            default:
                break;
        }
        vo.setType(position);
        vo.setTypename(value);
        return vo;
    }

    public void initData() {

        type = getIntent().getStringExtra("type");
        // 12客户不修改不可选择
        if (type.equals("2")) {
            mCid = this.getIntent().getStringExtra("cid");
            cname = getIntent().getStringExtra("cname");
            mTextView04.setText(cname);
        } else if (type.equals("1")) {
            mContactsDetailEditVO = (ContactDepTabResultVO) this.getIntent().getSerializableExtra("vo");
            mLid = mContactsDetailEditVO.getLid();
            mCid = mContactsDetailEditVO.getCid();

            // cname不一定有cid，能不能选根据cid做判断
            if (!CommonUtils.isNormalString(mCid))
                mTextView04.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.common_ic_arrow_right), null);
        } else {
            mTextView04.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.common_ic_arrow_right), null);
        }

        if (!"1".equals(type)) {
            List<ContactDepTabResultVO> list = new ArrayList<ContactDepTabResultVO>();
            for (int i = 0; i < 7; i++) {
                ContactDepTabResultVO vo = addModel(i, "");
                vo.setItmeid(i + 1);
                list.add(vo);
            }
            mMoreItmeAdapter.updateDataLast(list);
        }
    }

    @Override
    public void bindViewsListener() {
        mTextView04.setOnClickListener(this);
        mTextView10.setOnClickListener(this);
        mTextView11.setOnClickListener(this);
        mTextView14.setOnClickListener(this);
        mTextView15.setOnClickListener(this);
        mLinerLayout09.setOnClickListener(this);
        mLinerLayout16.setOnClickListener(this);
        mOkTv.setOnClickListener(this);
        mAddHeadImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_comm_head_right:
                mListView.setFocusable(true);
                mListView.setFocusableInTouchMode(true);
                mListView.requestFocus();
                if ("".equals(mEditText01.getText().toString())) {
                    CustomToast.showLongToast(this, "请填写姓名");
                    return;
                }
                commit();
                break;
            case R.id.type_content:
                String[] arraySex = {
                        "1,男", "2,女"
                };
                CommonUtils.initSimpleListDialog(this, "请选择性别", arraySex, mTextView10);
                break;
            case R.id.textview_11:
                initDateView();
                break;
            case R.id.add_head:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive())
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                // if (mPop != null && mPop.getFilePath() != null)
                // mHeadIconPath = mPop.getFilePath();
                // mPop = new BSUPloadPopWindows(this, mAddHeadImg, null, null, 0);

                Intent intent = new Intent(CrmClientAddContactsActivity.this, MultiImageSelectorActivity.class);
                // 是否显示拍摄图片
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                // 最大可选择图片数量
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
                // 选择模式
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, 1);
                // // 默认选择
                // if (mSelectPath != null && mSelectPath.size() > 0) {
                // intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST,
                // mSelectPath);
                // }
                startActivityForResult(intent, ImageActivityUtils.REQUEST_IMAGE);
                break;
            case R.id.add_more_09:
                String[] array = {
                        "添加手机", "添加电话", "添加传真", "添加微信", "添加QQ", "添加邮箱", "添加微博"
                };
                new BSUPloadPopWindows(this, mAddHeadImg, array, mCallBack);
                break;

            case R.id.textview_14:
                String[] relationships = CommonUtils.getStingArray(mRelationshipsList);
                CommonUtils.initSimpleListDialog(this, "请选择角色关系", relationships, mTextView14);
                break;
            case R.id.textview_15:
                String[] intimacys = CommonUtils.getStingArray(mIntimacysList);
                CommonUtils.initSimpleListDialog(this, "请选择亲密度", intimacys, mTextView15);
                break;

            case R.id.textview_04:

                if ("0".equals(mStatus)) {
                    if (!CommonUtils.isNormalString(mCid)) {
                        String[] options = {
                                "1,手动添加", "2,选择添加"
                        };
                        CommonUtils.initSimpleListDialog(this, "请选择方式", options, mClinetCallback);
                    }
                } else if ("2".equals(mStatus)) {
                    String[] customers = CommonUtils.getStingArray(mCustomersList);
                    CommonUtils.initSimpleListDialog(CrmClientAddContactsActivity.this, "请选择客户",
                            customers, mTextView04);
                }

                break;

            case R.id.save_to_contacts_16:
                if (mCheckBox.isChecked()) {
                    mCheckBox.setChecked(false);
                    mCheckBox.setButtonDrawable(R.drawable.common_ic_unselect);
                } else {
                    mCheckBox.setChecked(true);
                    mCheckBox.setButtonDrawable(R.drawable.common_ic_selected);
                }
                break;
            default:
                break;
        }
    }

    // 客户选择方式回调函数
    com.bs.bsims.utils.CommonUtils.ResultCallback mClinetCallback = new com.bs.bsims.utils.CommonUtils.ResultCallback() {
        @Override
        public void callback(String str, int position) {
            if (position == 0) {
                mEditText04.setVisibility(View.VISIBLE);
                mTextView04.setVisibility(View.GONE);

                mEditText04.setFocusable(true);
                mEditText04.setFocusableInTouchMode(true);
                mEditText04.requestFocus();
                InputMethodManager imm = (InputMethodManager) CrmClientAddContactsActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);

                mStatus = "1";
            } else {
                mEditText04.setVisibility(View.GONE);
                mTextView04.setVisibility(View.VISIBLE);
                mStatus = "2";

                String[] customers = CommonUtils.getStingArray(mCustomersList);
                initClinetListDialog(CrmClientAddContactsActivity.this, "请选择客户",
                        customers, mTextView04);
            }
        }
    };

    public void initClinetListDialog(Context context, String title, final String[] array,
            final TextView textview) {

        List<Map<String, Object>> list = CommonUtils.getListItem(array);
        SimpleAdapter adapter = new SimpleAdapter(context, list, R.layout.dialog_lv_item,
                new String[] {
                        "option"
                }, new int[] {
                        R.id.textview
                });
        ListView listView = new ListView(context);
        listView.setAdapter(adapter);
        listView.setDivider(new ColorDrawable(Color.parseColor("#EEEEEE")));
        listView.setDividerHeight(1);
        LinearLayout linearLayout = new LinearLayout(context);
        int width;
        if (array.length > 6) {
            width = 800;
        } else {
            width = LinearLayout.LayoutParams.WRAP_CONTENT;
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                width);// 定义文本显示组件
        listView.setLayoutParams(params);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String id = array[(int) arg3].split(",")[0];
                String name = array[(int) arg3].split(",")[1];
                textview.setText(name);
                textview.setTag(id);
                mDialog.dismiss();

            }
        });
        mDialog = new BSDialog(context, title, listView, new OnClickListener() {
            @Override
            public void onClick(View arg0) {
            }
        });

        mDialog.show();
        mDialog.setButtonVisible(false);
    }

    public void commit() {
        CustomDialog.showProgressDialog(this, "正在提交数据...");
        RequestParams params = new RequestParams();

        try {
            params.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());

            // 编辑联系人的时候才有lid
            params.put("lid", mLid);
            // if (!"1".equals(type) && !"2".equals(type) && mTextView04.getTag() != null)
            if (!CommonUtils.isNormalString(mCid) && mTextView04.getTag() != null)
                mCid = mTextView04.getTag().toString();

            params.put("userid", BSApplication.getInstance().getUserId());
            params.put("name", mEditText01.getText().toString());
            params.put("post", mEditText02.getText().toString());
            params.put("department", mEditText03.getText().toString());
            if (mStatus.equals("1")) {
                mCid = "";
            }
            params.put("company", mEditText04.getText().toString());
            params.put("cid", mCid);
            params.put("website", mEditText05.getText().toString());

            List<String> list0 = new ArrayList<String>();
            List<String> list1 = new ArrayList<String>();
            List<String> list2 = new ArrayList<String>();
            List<String> list3 = new ArrayList<String>();
            List<String> list4 = new ArrayList<String>();
            List<String> list5 = new ArrayList<String>();
            List<String> list6 = new ArrayList<String>();
            for (int i = 0; i < mMoreItmeAdapter.list.size(); i++) {
                ContactDepTabResultVO vo = mMoreItmeAdapter.list.get(i);

                if (vo.getTypename() != null && vo.getTypename().trim().length() > 0) {
                    switch (vo.getType()) {
                        case 0:
                            list0.add(vo.getTypename());
                            break;
                        case 1:
                            list1.add(vo.getTypename());
                            break;
                        case 2:
                            list2.add(vo.getTypename());
                            break;
                        case 3:
                            list3.add(vo.getTypename());
                            break;
                        case 4:
                            list4.add(vo.getTypename());
                            break;
                        case 5:

                            if (vo.getTypename() != null && !CommonUtils.isEmail(vo.getTypename())) {
                                CustomToast.showLongToast(CrmClientAddContactsActivity.this, "请填写正确的邮箱");
                                CustomDialog.closeProgressDialog();
                                return;
                            }

                            list5.add(vo.getTypename());
                            break;
                        case 6:
                            list6.add(vo.getTypename());
                            break;

                        default:
                            break;
                    }
                }
            }

            params.put("phone", list0.toArray());
            params.put("tel", list1.toArray());
            params.put("fax", list2.toArray());
            params.put("wechat", list3.toArray());
            params.put("qq", list4.toArray());
            params.put("email", list5.toArray());
            params.put("weibo", list6.toArray());

            if ("男".equals(mTextView10.getText().toString())) {
                params.put("sex", "1");
            } else if ("女".equals(mTextView10.getText().toString())) {
                params.put("sex", "2");
            } else {
                params.put("sex", "0");
            }
            params.put("birthday", mTextView11.getText().toString());
            params.put("hobby", mEditText12.getText().toString());
            params.put("address", mEditText13.getText().toString());
            if (mTextView14.getTag() != null)
                params.put("relationship", mTextView14.getTag().toString());
            if (mTextView14.getTag() != null)
                params.put("intimacy", mTextView15.getTag().toString());

            // mPop 每次都new出来的，这里的判断是处理点击头像选择一个后在点击头像，然后点击取消，file丢失问题
            if (mPop != null) {
                File file;
                if (!"".equals(mHeadIconPath))
                    file = CommonUtils.bitmapToString(this, mHeadIconPath);
                else
                    file = CommonUtils.bitmapToString(this, mPop.getFilePath());

                params.put("headpic", file);
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        String url = BSApplication.getInstance().getHttpTitle() + Constant.CRM_CLIENT_ADD_CONTACTS;
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                CustomDialog.closeProgressDialog();
                CustomToast.showShortToast(CrmClientAddContactsActivity.this, "网络连接超时");
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
                        CustomToast.showShortToast(CrmClientAddContactsActivity.this, str);

                        if (mCheckBox.isChecked()) {
                            String name = mEditText01.getText().toString();
                            String phone = mMoreItmeAdapter.list.get(0).getTypename().trim();
                            String compay = "";
                            if ("1".equals(mStatus)) {
                                compay = mTextView04.getText().toString();
                            } else {
                                compay = mEditText04.getText().toString();
                            }

                            insert(name, phone, compay);
                        }

                        if ("1".equals(type)) {
                            setResult(520, new Intent());// 联系人详情刷新

                        } else {

                            Intent intent = new Intent();
                            CrmClientAddContactsActivity.this.setResult(1, intent);
                        }
                        CustomDialog.closeProgressDialog();
                        CrmClientAddContactsActivity.this.finish();

                    } else {
                        CustomDialog.closeProgressDialog();
                        CustomToast.showShortToast(CrmClientAddContactsActivity.this, str);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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

        mBSDialog = new BSDialog(this, "请选择日期", timepickerview, new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    if (DateUtils.stringToLong(wheelMain.getTime(),"yyyy-MM-dd") > DateUtils.stringToLong(DateUtils.getCurrentDate(),"yyyy-MM-dd")) {
                        CustomToast.showShortToast(CrmClientAddContactsActivity.this, "请选择有效时间");
                        mTextView11.setText("");
                    }
                    else {
                        mTextView11.setText(wheelMain.getTime());
                        mTextView11.setTextColor(getResources().getColor(R.color.C4));
                        mBSDialog.dismiss();
                    }
                } catch (NotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        });
        mBSDialog.show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ImageActivityUtils.REQUEST_IMAGE:
                if (data != null) {
                    ArrayList<String> mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    Bitmap bitmap = CommonImageUtils.getImageFromLocal(mSelectPath.get(0));
                    mAddHeadImg.setImageBitmap(bitmap);
                }
                break;
        // case TAKE_PICTURE:
        // if (resultCode == RESULT_OK) {
        // if (data == null) {
        // File file = new File(mPop.getFilePath());
        // Bitmap bitmap = BitmapFactory.decodeFile(mPop.getFilePath(),
        // CommonUtils.getBitmapOption(2)); // 将图
        // mAddHeadImg.setImageBitmap(bitmap);
        // }
        // }
        // break;
        // case RESULT_LOAD_IMAGE:
        //
        // if (resultCode == RESULT_OK && null != data) {
        // // Uri selectedImage = data.getData();
        // // String[] filePathColumn = {
        // // MediaStore.Images.Media.DATA
        // // };
        // //
        // // String picturePath = selectedImage.getPath();
        // // mPop.setFilePath(picturePath);
        // // Bitmap bitmap = BitmapFactory.decodeFile(mPop.getFilePath(),
        // // CommonUtils.getBitmapOption(2)); // 将图
        // // mAddHeadImg.setImageBitmap(bitmap);
        //
        // try {
        // Uri selectedImage = data.getData();
        // String[] filePathColumn = {
        // MediaStore.Images.Media.DATA
        // };
        // String picturePath;
        // Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null,
        // null);
        // if (cursor == null) {
        // picturePath = selectedImage.getPath();
        // } else {
        // cursor.moveToFirst();
        // int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        // picturePath = cursor.getString(columnIndex);
        // cursor.close();
        // }
        //
        // Bitmap bitmap = BitmapFactory.decodeFile(picturePath, CommonUtils.getBitmapOption(2));
        // mPop.setFilePath(picturePath);
        // mAddHeadImg.setImageBitmap(bitmap);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // }
        //
        // break;
        }
    }

    ResultCallback mCallBack = new ResultCallback() {

        @Override
        public void callback(String str, int position) {
            List<ContactDepTabResultVO> list = new ArrayList<ContactDepTabResultVO>();
            ContactDepTabResultVO vo = addModel(position, "");
            vo.setItmeid(mMoreItmeAdapter.list.size() + 1);
            list.add(vo);
            mMoreItmeAdapter.updateDataLast(list);
        }
    };

    class MoreItmeAdapter extends BaseAdapter {
        private Context context;
        public List<ContactDepTabResultVO> list;
        private BSMoveLayout mBSMLyaout;

        public MoreItmeAdapter(Context context) {
            this.context = context;
            list = new ArrayList<ContactDepTabResultVO>();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                View view = View.inflate(CrmClientAddContactsActivity.this, R.layout.crm_contacts_add_more_lv_item, null);
                convertView = new BSMoveLayout(CrmClientAddContactsActivity.this, true, view, listeren);
                // convertView = mBSMLyaout;
                holder.icon = (ImageView) convertView.findViewById(R.id.icon);
                holder.typeName = (TextView) convertView.findViewById(R.id.type_name);
                holder.deleteTv = (TextView) convertView.findViewById(R.id.delete_tv);
                holder.delete = (TextView) convertView.findViewById(R.id.delete);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final ContactDepTabResultVO vo = list.get(position);
            if (0 == vo.getType() || 1 == vo.getType() || 2 == vo.getType() || 4 == vo.getType()) {
                holder.typeName.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
            } else if (5 == vo.getType()) {
                holder.typeName.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            } else {
                holder.typeName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
            }
            holder.typeName.setHint(vo.getTypehint());
            holder.icon.setImageResource(vo.getDrawableid());
            holder.typeName.setTag(vo.getItmeid());
            holder.typeName.setText(vo.getTypename());
            holder.delete.setTag(position + "");

            holder.typeName.setOnFocusChangeListener(new OnFocusChangeListener() {

                @Override
                public void onFocusChange(View arg0, boolean arg1) {
                    if (vo.getItmeid() == (Integer) holder.typeName.getTag()) {
                        // if (holder.typeName.getText().toString().trim().length() > 0)
                        vo.setTypename(holder.typeName.getText().toString());
                    }
                }
            });

            if (vo.getItmeid() < 7) {
                holder.deleteTv.setVisibility(View.GONE);
            } else {
                holder.deleteTv.setVisibility(View.VISIBLE);
            }
            holder.deleteTv.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View arg0, MotionEvent arg1) {
                    if (vo.getItmeid() < 7) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });

            return convertView;
        }

        DeleteListeren listeren = new DeleteListeren() {

            @Override
            public void deleteItem(int position) {
                list.remove(position);
                notifyDataSetChanged();
            }
        };

        public void updateDataLast(List<ContactDepTabResultVO> listvo) {
            list.addAll(listvo);
            sortList();
            this.notifyDataSetChanged();
        }

        public void sortList() {
            Comparator comp = new Comparator() {
                public int compare(Object o1, Object o2) {
                    int one = 0;
                    int two = 0;
                    ContactDepTabResultVO p1 = (ContactDepTabResultVO) o1;
                    ContactDepTabResultVO p2 = (ContactDepTabResultVO) o2;
                    one = p1.getType();
                    two = p2.getType();
                    if (one > two) {
                        return 1;
                    } else if (one == two) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            };
            Collections.sort(list, comp);
        }
    }

    static class ViewHolder {
        private ImageView icon;
        private TextView typeName, deleteTv, delete;
    }

    public boolean insert(String name, String phone, String compay) {
        try {
            ContentValues values = new ContentValues();

            // 下面的操作会根据RawContacts表中已有的rawContactId使用情况自动生成新联系人的rawContactId
            Uri rawContactUri = getContentResolver().insert(
                    RawContacts.CONTENT_URI, values);
            long rawContactId = ContentUris.parseId(rawContactUri);

            // 向data表插入姓名数据
            if (name != "") {
                values.clear();
                values.put(Data.RAW_CONTACT_ID, rawContactId);
                values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
                values.put(StructuredName.GIVEN_NAME, name);
                getContentResolver().insert(ContactsContract.Data.CONTENT_URI,
                        values);
            }

            // 向data表插入电话数据
            if (phone != "") {
                values.clear();
                values.put(Data.RAW_CONTACT_ID, rawContactId);
                values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
                values.put(Phone.NUMBER, phone);
                values.put(Phone.TYPE, Phone.TYPE_MOBILE);
                getContentResolver().insert(ContactsContract.Data.CONTENT_URI,
                        values);
            }

            // 向data表插入公司名字
            if (compay != "") {
                values.clear();
                values.put(Data.RAW_CONTACT_ID, rawContactId);
                values.put(Data.MIMETYPE, Organization.CONTENT_ITEM_TYPE);
                values.put(Organization.COMPANY, compay);
                values.put(Organization.TYPE, Organization.TYPE_CUSTOM);
                getContentResolver().insert(ContactsContract.Data.CONTENT_URI,
                        values);
            }

            // 向data表插入Email数据
            // if (email != "") {
            // values.clear();
            // values.put(Data.RAW_CONTACT_ID, rawContactId);
            // values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
            // values.put(Email.DATA, email);
            // values.put(Email.TYPE, Email.TYPE_WORK);
            // getContentResolver().insert(ContactsContract.Data.CONTENT_URI,
            // values);
            // }
            //
            // // 向data表插入QQ数据
            // if (qq != "") {
            // values.clear();
            // values.put(Data.RAW_CONTACT_ID, rawContactId);
            // values.put(Data.MIMETYPE, Im.CONTENT_ITEM_TYPE);
            // values.put(Im.DATA, qq);
            // values.put(Im.PROTOCOL, Im.PROTOCOL_QQ);
            // getContentResolver().insert(ContactsContract.Data.CONTENT_URI,
            // values);
            // }
            // 向data表插入头像数据

            Bitmap bitmap = BitmapFactory.decodeFile(mPop.getFilePath(), CommonUtils.getBitmapOption(2)); // 将图
            final ByteArrayOutputStream os = new ByteArrayOutputStream();
            // 将Bitmap压缩成PNG编码，质量为100%存储
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            byte[] avatar = os.toByteArray();
            values.put(Data.RAW_CONTACT_ID, rawContactId);
            values.put(Data.MIMETYPE, Photo.CONTENT_ITEM_TYPE);
            values.put(Photo.PHOTO, avatar);
            getContentResolver().insert(ContactsContract.Data.CONTENT_URI,
                    values);
        }

        catch (Exception e) {
            return false;
        }
        return true;
    }
}
