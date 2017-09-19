
package com.bs.bsims.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.UploadAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.image.selector.ImageActivityUtils;
import com.bs.bsims.model.CrmContactVo;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.time.WheelMain;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.view.BSCircleImageView;
import com.bs.bsims.view.BSDialog;
import com.bs.bsims.view.BSUPloadPopWindows;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrmTradeContantAddInfo extends BaseActivity implements OnClickListener, OnItemClickListener {
    private static final int ADD_INFORM_PERSON = 10;
    private static final int ADD_PERSON = 2014;

    // 上传图片
    private static final int TAKE_PICTURE = 0x000001;
    private static final int RESULT_LOAD_IMAGE = 0x000002;

    private static final int SELECT_CLIENT = 3;// 选择客户
    private static final int SELECT_BUSSNESS = 4;// 选择商机

    private Context mContext;
    private List<CrmContactVo> mList;
    private TextView money, aprrov_name;
    private BSCircleImageView image_taskevent_releasetask_fuzeren;
    private TextView shenpirenname;
    private TextView txt_taskevent_releasetask_fuzeren;
    private TextView inform_go_tv;
    private LinearLayout r5, ly_customer, product_info_all, bs_ly, ac_ly;
    private WheelMain wheelMain;
    private BSDialog dialog;

    // checkboxlist
    private String hid;
    private String cid = "";
    private String pid = "";// 产品id
    private String bs_id = "";
    private String bname = "";// 商机名称（穿过来的）
    private String contantkeys = "0";// 选择期次 1表示选择负责人
    private ImageLoader imageloader;
    /** 相关人的集合 */
    private List<EmployeeVO> mDataList = new ArrayList<EmployeeVO>();

    /* 提交接口的参数 */
    private String remark;// 备注

    private EditText mHetongName, mMoney, mSaleBack, mClientSigin, mRemark;
    private TextView mSelectProduct, mStartTime, mEndTime, mSiginTime, select_customer,
            bs_name;
    private boolean mCommitFlag = true;
    private ImageView bussines_ze, fuzeren_arrow_right;
    private CrmContactVo mClientSelectVo, mLinkBusinessSelectVo, mTradecontactTypeVo;
    private TextView mTradeTypeTv, mTradeSiginPersonTv;
    private String mType = "0";// 0为直接负责人、1为选择客户
    private LinearLayout mAddMoreLayout, mMoreLayout;

    // 上传图片使用
    private List<String> mPicturePathList;
    private LinearLayout mParentView;
    private GridView mGrideviewUpload;
    private UploadAdapter mAdapter;
    private BSUPloadPopWindows mPop;
    private TextView mProductName;

    private StringBuffer pdIpsids = new StringBuffer();// 产品的id

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.crmvisitrecord_add_money, mContentLayout);
        mContext = this;
    }

    @Override
    public boolean getDataResult() {
        return getDataUserFuZe();
    }

    @Override
    public void updateUi() {
        // 负责人只有一个的时候。
        if (Constant.RESULT_CODE.equals(mTradecontactTypeVo.getCode())) {
            // 默认
            if (mTradecontactTypeVo.getArray() != null) {
                mTradeTypeTv.setText(mTradecontactTypeVo.getArray().get(0).getName());
                mTradeTypeTv.setTag(mTradecontactTypeVo.getArray().get(0).getId());
            }

            mTradeSiginPersonTv.setText(BSApplication.getInstance().getUserFromServerVO().getFullname());
            mTradeSiginPersonTv.setTag(BSApplication.getInstance().getUserFromServerVO().getUserid());
        }
    }

    @Override
    public void initView() {

        mTitleTv.setText("添加合同");
        money = (TextView) findViewById(R.id.money);
        mOkTv.setText("确认");
        imageloader = ImageLoader.getInstance();
        mHetongName = (EditText) findViewById(R.id.hetong_name);
        mSelectProduct = (TextView) findViewById(R.id.select_product);
        mMoney = (EditText) findViewById(R.id.money);
        mStartTime = (TextView) findViewById(R.id.start_time);
        mEndTime = (TextView) findViewById(R.id.end_time);
        mSiginTime = (TextView) findViewById(R.id.sigin_time);
        mSaleBack = (EditText) findViewById(R.id.sale_back);
        mClientSigin = (EditText) findViewById(R.id.client_sigin);
        mRemark = (EditText) findViewById(R.id.remark);
        ly_customer = (LinearLayout) findViewById(R.id.ly_customer);
        select_customer = (TextView) findViewById(R.id.select_customer);
        product_info_all = (LinearLayout) findViewById(R.id.product_info_all);
        bs_ly = (LinearLayout) findViewById(R.id.bs_ly);
        bs_name = (TextView) findViewById(R.id.bs_name);
        mProductName = (TextView) findViewById(R.id.product_name);

        if (null != getIntent().getStringExtra("cid")
                && null != getIntent().getStringExtra("cname")) {
            select_customer.setCompoundDrawables(null, null, null, null);
            select_customer.setText(getIntent().getStringExtra("cname"));
            cid = getIntent().getStringExtra("cid");
            select_customer.setTag(cid);

            ly_customer.setOnClickListener(null);
            getLinkData();
        }
        else {
            ly_customer.setOnClickListener(this);
        }

        if (null != getIntent().getStringExtra("bid")
                && null != getIntent().getStringExtra("bname")) {
            bs_name.setText(getIntent().getStringExtra("bname"));
            bs_name.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            bs_id = getIntent().getStringExtra("bid");
            bs_name.setTag(bs_id);
            bs_ly.setOnClickListener(null);
        }
        else {
            bs_ly.setOnClickListener(this);
        }

        mTradeTypeTv = (TextView) findViewById(R.id.trade_type);
        mTradeSiginPersonTv = (TextView) findViewById(R.id.trade_sigin_person);

        List<EmployeeVO> list = new ArrayList<EmployeeVO>();
        EmployeeVO vo = new EmployeeVO();
        vo.setHeadpic(BSApplication.getInstance().getUserFromServerVO().getHeadpic());
        vo.setFullname(BSApplication.getInstance().getUserFromServerVO().getFullname());
        vo.setUserid(BSApplication.getInstance().getUserFromServerVO().getUserid());
        list.add(vo);
        mAddMoreLayout = (LinearLayout) findViewById(R.id.add_more_layout);
        mMoreLayout = (LinearLayout) findViewById(R.id.more_layout);
        ac_ly = (LinearLayout) findViewById(R.id.ac_ly);

        // 上传图片使用
        mGrideviewUpload = (GridView) findViewById(R.id.grideview_upload);
        mAdapter = new UploadAdapter(this);
        mGrideviewUpload.setAdapter(mAdapter);
        mParentView = (LinearLayout) getLayoutInflater().inflate(R.layout.creative_idea_new, null);
        mPicturePathList = new ArrayList<String>();

    }

    @Override
    public void bindViewsListener() {
        mStartTime.setOnClickListener(this);
        mEndTime.setOnClickListener(this);
        mSiginTime.setOnClickListener(this);
        product_info_all.setOnClickListener(this);
        mOkTv.setOnClickListener(this);
        mAddMoreLayout.setOnClickListener(this);
        mGrideviewUpload.setOnItemClickListener(this);
        ac_ly.setOnClickListener(this);

        select_customer.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                mLinkBusinessSelectVo = null;
                bs_name.setText("");
                mMoney.setText("");
                // getLinkData();

            }
        });

        mTradeTypeTv.setOnClickListener(this);
        mTradeSiginPersonTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.txt_comm_head_right:
                if (mHetongName.getText().toString().trim().length() == 0) {
                    CustomToast.showLongToast(this, "合同名称不能为空");
                    return;
                }
                if (select_customer.getText().toString().trim().length() == 0) {
                    CustomToast.showLongToast(this, "客户名称不能为空");
                    return;
                }
                if (mMoney.getText().toString().trim().length() == 0) {
                    CustomToast.showLongToast(this, "合同金额不能为空");
                    return;
                }
                if (mStartTime.getText().toString().trim().length() == 0) {
                    CustomToast.showLongToast(this, "开始时间不能为空");
                    return;
                }
                if (mEndTime.getText().toString().trim().length() == 0) {
                    CustomToast.showLongToast(this, "结束时间不能为空");
                    return;
                }

                if (mStartTime.getText().toString().length() > 0
                        && mEndTime.getText().toString().length() > 0) {
                    long startTime = DateUtils.getStringToDate(mStartTime.getText().toString());
                    long endTime = DateUtils.getStringToDate(mEndTime.getText().toString());
                    if (endTime <= startTime) {
                        CustomToast.showShortToast(this, "结束时间要比开始时间大");
                        return;
                    }
                }

                if (mSiginTime.getText().toString().trim().length() == 0) {
                    CustomToast.showLongToast(this, "签约时间不能为空");
                    return;
                }

                if (mTradeSiginPersonTv.getText().toString().trim().length() == 0) {
                    CustomToast.showLongToast(this, "我方签约不能为空");
                    return;
                }

                if (mCommitFlag) {
                    mCommitFlag = false;
                    commit();
                }
                break;

            case R.id.start_time:
                CommonUtils.initDateView(this, mStartTime);
                break;

            case R.id.end_time:
                CommonUtils.initDateView(this, mEndTime);
                break;

            case R.id.sigin_time:
                CommonUtils.initDateView(this, mSiginTime);
                break;

            // 选择客户
            case R.id.ly_customer:
                intent.putExtra("type", "1");
                intent.putExtra("name", "选择客户");
                intent.putExtra("resulut_code", SELECT_CLIENT);
                intent.setClass(this, CrmOptionsListActivity.class);
                startActivityForResult(intent, SELECT_CLIENT);
                break;

            case R.id.bs_ly:
                intent.putExtra("type", "4");
                intent.putExtra("name", "选择商机");
                if (select_customer.getTag() != null) {
                    cid = select_customer.getTag().toString();
                }
                intent.putExtra("cid", cid);
                intent.setClass(this, CrmOptionsListActivity.class);
                intent.putExtra("resulut_code", SELECT_BUSSNESS);
                startActivityForResult(intent, SELECT_BUSSNESS);

                break;

            case R.id.trade_type:
                if (mTradecontactTypeVo == null || Constant.RESULT_CODE400.equals(mTradecontactTypeVo.getCode())) {
                    return;
                }
                String[] tradeTypes = CommonUtils.getStingArray(mTradecontactTypeVo.getArray());
                CommonUtils.initSimpleListDialog(this, "请选择合同类型", tradeTypes, mTradeTypeTv);
                break;

            case R.id.trade_sigin_person:
                intent.setClass(this, AddByPersonActivity.class);
                intent.putExtra("requst_number", 2014);
                intent.putExtra("state", "1");
                this.startActivityForResult(intent, 2014);
                break;
            case R.id.add_more_layout:
                mMoreLayout.setVisibility(View.VISIBLE);
                mAddMoreLayout.setVisibility(View.GONE);
                break;

            case R.id.ac_ly:
                Intent intent1 = new Intent(CrmTradeContantAddInfo.this,
                        AddByPersonCRMActivity.class);
                intent1.putExtra("proKey", "7");
                intent1.putExtra("typekey", "5");
                intent1.putExtra("cid", cid);
                intent1.putExtra("proselect", pdIpsids.toString());
                intent1.putExtra("requst_number", 2018);
                if (checkIsCustomerSelect()) {
                    startActivityForResult(intent1, 2018);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int arg0, int resultCode, Intent intent) {
        switch (arg0) {
        // 关联的商机
            case 84:
                if (resultCode == 84) {
                    if (intent != null) {// common_ic_arrow_right
                        //
                        bs_name.setText(intent.getStringExtra("bs_name"));
                        bs_name.setTextColor(getResources().getColor(R.color.C4));
                        bs_id = intent.getStringExtra("bs_id");
                        pid = intent.getStringExtra("pr_id");
                        mSelectProduct.setText(intent.getStringExtra("pr_name"));
                        money.setText(intent.getStringExtra("pr_money"));
                    }
                }
                break;
            case 2018:
                /*
                 * 产品选择
                 */
                if (resultCode == 2018) {
                    if (intent != null) {
                        StringBuffer proName = new StringBuffer();
                        proName.setLength(0);
                        proName.append(intent.getStringExtra("name"));
                        mProductName.setText(proName.toString());
                        pdIpsids.setLength(0);
                        pdIpsids.append(intent.getStringExtra("id"));
                    }
                }
                break;
            // 单选
            case ADD_PERSON:
                if (intent == null)
                    return;
                List<EmployeeVO> list = (List<EmployeeVO>) intent.getSerializableExtra("checkboxlist");
                mTradeSiginPersonTv.setText(list.get(0).getFullname());
                mTradeSiginPersonTv.setTag(list.get(0).getUserid());
                break;

            case ImageActivityUtils.REQUEST_IMAGE:
                if (resultCode == RESULT_OK) {
                    ImageActivityUtils.setImageGetActivity(intent, mAdapter);
                }

                break;

            /* 图片预览之后返回删除图片了 piclist */

            case ImageActivityUtils.REQUEST_IMAGE_BYSDCARD:
                if (resultCode == RESULT_OK) {
                    ImageActivityUtils.imgPreviewDelteActivity(intent, mAdapter);
                }

                break;

            case SELECT_CLIENT:
                if (intent == null)
                    return;
                select_customer.setText(intent.getStringExtra("name"));
                select_customer.setTag(intent.getStringExtra("id"));
                break;

            case SELECT_BUSSNESS:
                if (intent == null)
                    return;
                bs_name.setText(intent.getStringExtra("name"));
                bs_name.setTag(intent.getStringExtra("id"));
                mMoney.setText(intent.getStringExtra("money"));
                pdIpsids.append(intent.getStringExtra("pr_id"));
                mProductName.setText(intent.getStringExtra("pr_name"));
                break;
        }

    }

    public void commit() {

        mOkTv.setVisibility(View.GONE);
        CustomDialog.showProgressDialog(this, "正在提交数据...");
        RequestParams params = new RequestParams();

        try {
            params.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            params.put("userid", BSApplication.getInstance().getUserId());
            params.put("title", mHetongName.getText().toString());
            if (select_customer.getTag() != null)
                params.put("customer", select_customer.getTag().toString());
            if (bs_name.getTag() != null)
                params.put("business", bs_name.getTag().toString());
            params.put("money", mMoney.getText().toString());
            params.put("starttime", mStartTime.getText().toString());
            params.put("endtime", mEndTime.getText().toString());
            params.put("signing_date", mSiginTime.getText().toString());
            params.put("type", mTradeTypeTv.getTag().toString());
            params.put("my_side", mTradeSiginPersonTv.getTag().toString());

            params.put("discount", mSaleBack.getText().toString());
            params.put("client_side", mClientSigin.getText().toString());
            params.put("remark", mRemark.getText().toString());
            params.put("product", pdIpsids);

            for (int i = 0; i < mAdapter.mPicList.size(); i++) {
                File file = CommonUtils.bitmapToString(this, mAdapter.mPicList.get(i));
                params.put("file" + i, file);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        String url = BSApplication.getInstance().getHttpTitle() + Constant.CRM_TRADEMSGINFOADD;
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                mOkTv.setVisibility(View.VISIBLE);
                CustomDialog.closeProgressDialog();
                CustomToast.showShortToast(mContext, "提交失败...");
                mCommitFlag = true;
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                mCommitFlag = true;
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(new String(arg2));
                    String str = (String) jsonObject.get("retinfo");
                    String code = (String) jsonObject.get("code");
                    mOkTv.setVisibility(View.VISIBLE);
                    if (Constant.RESULT_CODE.equals(code)) {
                        CustomToast.showShortToast(CrmTradeContantAddInfo.this, str);
                        Intent i = new Intent();
                        i.putExtra("1", "2");
                        setResult(2012, i);
                        CrmTradeContantAddInfo.this.finish();
                    }
                    CustomToast.showShortToast(CrmTradeContantAddInfo.this, str);
                    CustomDialog.closeProgressDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // 判断客户是否首先被选中
    public boolean checkIsCustomerSelect() {
        if (null == select_customer || select_customer.getText().toString().trim().length() == 0) {
            CustomToast.showLongToast(mContext, "请先选择客户!");
            return false;
        } else {
            return true;
        }
    }

    // 进来直接获取负责人
    public boolean getDataUserFuZe() {
        try {
            Gson gson = new Gson();
            Map<String, String> params = new HashMap<String, String>();
            params.put("cid", cid);
            params.put("userid", BSApplication.getInstance().getUserId());
            params.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStr = "";

            // 选择客户
            params.put("type", "1");

            jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.CRM_TRADECONTACT_TYPE, params);
            mTradecontactTypeVo = gson.fromJson(jsonStr, CrmContactVo.class);

            if (HttpClientUtil.isNetworkConnected(this)) {
                if (mTradecontactTypeVo == null) {
                    getDataUserFuZe();
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

    // 关联商机需要先选取客户后，生成cid;
    public void getLinkData() {
        new Thread() {
            public void run() {
                try {
                    Gson gson = new Gson();
                    Map<String, String> paramsMap = new HashMap<String, String>();
                    if (select_customer.getTag() != null) {
                        paramsMap.put("cid", select_customer.getTag().toString());
                    } else {
                        paramsMap.put("cid", cid);
                    }

                    paramsMap.put("userid", BSApplication.getInstance().getUserId());
                    paramsMap.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
                    mType = "4";
                    paramsMap.put("type", mType);
                    String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.CRM_GETVISTITORTOBUSSINES, paramsMap);
                    mLinkBusinessSelectVo = gson.fromJson(jsonStr, CrmContactVo.class);

                    if (HttpClientUtil.isNetworkConnected(CrmTradeContantAddInfo.this)) {
                        if (mLinkBusinessSelectVo == null) {
                            // if (Constant.RESULT_CODE400.equals(mLinkBusinessSelectVo.getCode()))
                            // return;
                            getLinkData();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long arg2) {
        if (parent == mGrideviewUpload) {
            ImageActivityUtils.setImageForActivity(view, CrmTradeContantAddInfo
                    .this, mAdapter, (int) arg2);
        }
    }

}
