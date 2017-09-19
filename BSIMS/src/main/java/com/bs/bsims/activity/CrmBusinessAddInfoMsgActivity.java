
package com.bs.bsims.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.CrmBussinesListindexVo;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.model.ResultVO;
import com.bs.bsims.time.ScreenInfo;
import com.bs.bsims.time.WheelMain;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.view.BSDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CrmBusinessAddInfoMsgActivity extends BaseActivity implements OnClickListener {

    private TextView bussines_name;// 商机名称
    private TextView bussines_customer_ic;// 客户名称的后箭头
    private TextView bussines_customer;// 客户
    private TextView bussines_money;// 金额
    private TextView bussines_street;// 销售阶段
    private TextView bussines_dateend;// 结单日期
    // private TextView bussines_state;// 商机类型
    private TextView bussines_chance_from;// 商机来源
    private EditText business_work_details;// 商机备注
    private TextView bussines_chance_getproduct;// 产品选择
    private LinearLayout ly_customer;// 客户布局点击
    private LinearLayout ly_product;// 产品布局点击
    private LinearLayout ly_bsstate;// 阶段布局点击
    private LinearLayout ly_bsrousre;// 来源布局点击
    private LinearLayout ly_bsdate;// 结单日期布局点击

    private String bussines_names, bussines_customers, bussines_moneys, bussines_streets,
            bussines_dateends, bussines_visitor_persons, bussines_chance_phones,
            bussines_chance_froms,
            business_work_detailss, bussines_chance_getproducts, bussines_chance_deptarms,
            txt_taskevent_releasetask_fuzerens, txt_taskevent_releasetask_xiangguanrens;

    private Context mContext;


    private WheelMain wheelMain;
    private BSDialog dialog;
    private String contactskey = "0";
    private String contactskeys = "00";
    private ImageLoader imageloader;

    /** 相关人的集合 */
    private List<EmployeeVO> mDataList = new ArrayList<EmployeeVO>();
    /**
     * 提交的id
     */
    private String bid = "";// 商机id
    private String cid = "";// 客户id
    private String stateid;// 销售阶段的id
    private String vIpsid;// 拜访人的id
    private String bfIpsid;// 商机来源的id
    private StringBuffer pdIpsids = new StringBuffer();// 产品的id
    private String open;// 部门权限的id
    private String personid;// 负责的id
    private StringBuffer mInformPerson;// 存放相关人
    private ResultVO mResultInfoVO;

    public boolean postState = true;// 控制多次提交按钮

    private CrmBussinesListindexVo vo;

    @Override
    public void baseSetContentView() {
        // TODO Auto-generated method stub
        View layout = View.inflate(this, R.layout.crmbusiness_details_addcustomer, mContentLayout);
        mContentLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
        mContext = this;
    }

    @Override
    public boolean getDataResult() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void updateUi() {
        // TODO Auto-generated method stub

    }

    @SuppressLint("NewApi")
    @Override
    public void initView() {
        // TODO Auto-generated method stub
        mTitleTv.setText("添加商机");
        mOkTv.setText("保存");
        bussines_name = (TextView) findViewById(R.id.bussines_name);
        bussines_name.setFocusable(true);
        bussines_name.setFocusableInTouchMode(true);
        bussines_name.requestFocus();
        bussines_customer = (TextView) findViewById(R.id.bussines_customer);
        bussines_money = (TextView) findViewById(R.id.bussines_money);
        bussines_street = (TextView) findViewById(R.id.bussines_street);
        bussines_dateend = (TextView) findViewById(R.id.bussines_dateend);
        bussines_chance_from = (TextView) findViewById(R.id.bussines_chance_from);
        business_work_details = (EditText) findViewById(R.id.business_work_details);
        bussines_chance_getproduct = (TextView) findViewById(R.id.bussines_chance_getproduct);
        bussines_customer_ic = (TextView) findViewById(R.id.bussines_customer_ic);


        open = "1";

        ly_customer = (LinearLayout) findViewById(R.id.ly_customer);
        ly_product = (LinearLayout) findViewById(R.id.ly_product);
        ly_bsstate = (LinearLayout) findViewById(R.id.ly_bsstate);
        ly_bsrousre = (LinearLayout) findViewById(R.id.ly_bsrousre);
        ly_bsdate = (LinearLayout) findViewById(R.id.ly_bsdate);

        // 这里就是编辑商机
        if (null != getIntent().getSerializableExtra("vBussinesListindexVo")) {
            mTitleTv.setText("编辑商机");
            vo = (CrmBussinesListindexVo) getIntent().getSerializableExtra(
                    "vBussinesListindexVo");
            bussines_name.setText(vo.getBname());
            bid = vo.getBid();
            cid = vo.getCid();
            bussines_customers = vo.getCname();
            bussines_customer_ic.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                    null,
                    null);
            bussines_customer.setText(bussines_customers);
            bussines_money.setText(vo.getMoney());
            bussines_dateend.setText(DateUtils.parseDateDay(vo.getExpirationDate()));
            bussines_street.setText(vo.getStatusName());
            bussines_chance_from.setText(vo.getSource());
            if (null != vo.getProduct()) {
                String str = "";
                pdIpsids.setLength(0);
                for (int i = 0; i < vo.getProduct().size(); i++) {
                    str += vo.getProduct().get(i).getPname() + ",";
                    pdIpsids.append(vo.getProduct().get(i).getPid() + ",");
                }
                bussines_chance_getproduct.setText(str);
            }
            if (CommonUtils.isNormalString(vo.getRemark())) {
                business_work_details.setText(vo.getRemark());
            }

            stateid = vo.getStatus();
            bfIpsid = vo.getSourceId();
            bussines_street.setTextColor(getResources().getColor(R.color.C4));
            if (stateid.equals("5") || stateid.equals("6")) {
                ly_bsstate.setOnClickListener(null);
                TextView bstateTv = (TextView) findViewById(R.id.bussines_street_right);
                bstateTv.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
            }
            else {
                ly_bsstate.setOnClickListener(this);
            }
            bussines_chance_from.setTextColor(getResources().getColor(R.color.C4));

        }
        else {
            // 如果有客户id，则不用选择客户
            if (null != getIntent().getStringExtra("addstate")
                    && null != getIntent().getStringExtra("cname")
                    && null != getIntent().getStringExtra("cid")) {
                ly_customer.setOnClickListener(null);
                bussines_customer.setText(getIntent().getStringExtra("cname") + "");
                bussines_customers = getIntent().getStringExtra("cname") + "";
                cid = getIntent().getStringExtra("cid");
                bussines_customer_ic.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
                        null,
                        null);
            }
            else {
                ly_customer.setOnClickListener(this);
            }
            ly_bsstate.setOnClickListener(this);
            bussines_street.setText("初步接洽");
            bussines_street.setTextColor(getResources().getColor(R.color.C4));
            stateid = "1";
            bussines_chance_from.setText("商机来源");
            bussines_chance_from.setTextColor(getResources().getColor(R.color.C4));
            bfIpsid = "1";
        }

    }

    @Override
    public void bindViewsListener() {
        // TODO Auto-generated method stub

        ly_product.setOnClickListener(this);
        ly_bsrousre.setOnClickListener(this);
        ly_bsdate.setOnClickListener(this);

        mOkTv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (checkFromInfoMsg() && postState) {
                    CustomDialog.showProgressDialog(mContext, "正在添加商机...");
                    PostInfoBussines();
                }

            }
        });

    }

    public boolean checkFromInfoMsg() {
        bussines_names = bussines_name.getText().toString().trim();
        bussines_customers = bussines_customer.getText().toString().trim();
        bussines_moneys = bussines_money.getText().toString().trim();
        bussines_streets = bussines_street.getText().toString().trim();
        bussines_dateends = bussines_dateend.getText().toString().trim();
        bussines_chance_froms = bussines_chance_from.getText().toString().trim();
        business_work_detailss = business_work_details.getText().toString().trim();
        bussines_chance_getproducts = bussines_chance_getproduct.getText().toString().trim();
        if (null == bussines_names || bussines_names.equals("")) {
            CustomToast.showShortToast(mContext, "请填写商机名称!");
            return false;
        }
        else if (null == bussines_customers || bussines_customers.equals("")) {
            CustomToast.showShortToast(mContext, "请填写客户!");
            return false;
        }
        else if (null == bussines_moneys || bussines_moneys.equals("")) {
            CustomToast.showShortToast(mContext, "请填写预计金额!");
            return false;
        }
        else if (null == bussines_streets || bussines_streets.equals("")) {
            CustomToast.showShortToast(mContext, "请填写销售阶段!");
            return false;
        } else if (null == bussines_dateends || bussines_dateends.equals("")) {
            CustomToast.showShortToast(mContext, "请填写成单日期!");
            return false;
        }
        else if (null == bussines_chance_froms || bussines_chance_froms.equals("")) {
            CustomToast.showShortToast(mContext, "请填写商机来源!");
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent1 = new Intent(CrmBusinessAddInfoMsgActivity.this,
                AddByPersonCRMActivity.class);
        switch (v.getId()) {
        // 必须先选择客户
            case R.id.ly_customer:
                contactskeys = "01";
                Intent intent = new Intent();
                intent.putExtra("type", "1");
                intent.putExtra("name", "选择客户");
                intent.putExtra("resulut_code", 2014);
                intent.setClass(this, CrmOptionsListActivity.class);
                startActivityForResult(intent, 2014);
                break;
            case R.id.ly_bsstate:
                intent1.putExtra("proKey", "1");
                intent1.putExtra("typekey", "2");
                intent1.putExtra("requst_number", 2015);// 选择销售阶段
                if (checkIsCustomerSelect()) {
                    startActivityForResult(intent1, 2015);
                }
                break;

            case R.id.ly_bsdate:
                initDateView();
                break;

            case R.id.ly_bsrousre:// 选择机会来源
                intent1.putExtra("proKey", "7");
                intent1.putExtra("typekey", "4");
                intent1.putExtra("requst_number", 2017);// 机会来源
                if (checkIsCustomerSelect()) {
                    startActivityForResult(intent1, 2017);
                }
                break;
            case R.id.ly_product:// 选择产品
                intent1.putExtra("proKey", "7");
                intent1.putExtra("typekey", "5");
                intent1.putExtra("cid", cid);
                intent1.putExtra("proselect", pdIpsids.toString());
                intent1.putExtra("requst_number", 2018);
                if (checkIsCustomerSelect()) {
                    startActivityForResult(intent1, 2018);
                }
                break;
            case R.id.image_taskevent_releasetask_fuzeren:// 选择相关负责人
                contactskeys = "03";
                intent1.putExtra("proKey", "0");
                intent1.putExtra("typekey", "6");
                intent1.putExtra("cid", cid);
                intent1.setClass(CrmBusinessAddInfoMsgActivity.this, AddByPersonCRMActivity.class);
                intent1.putExtra("requst_number", 2014);
                if (checkIsCustomerSelect()) {
                    startActivityForResult(intent1, 2014);
                }
                break;
            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {

        switch (arg0) {

            case 2014:
                /*
                 * 瀹㈡埛鍚嶇О
                 */
                if (arg1 == 2014) {
                    if (arg2 != null) {
                        bussines_customer.setText(arg2.getStringExtra("name"));
                        bussines_customers = arg2.getStringExtra("name").trim().toString();
                        cid = arg2.getStringExtra("id");
                    }
                }
            case 2015:

                /*
                 * 销售阶段
                 */
                if (arg1 == 2015) {
                    if (arg2 != null) {
                        bussines_street.setText(arg2.getStringExtra("name"));
                        stateid = arg2.getStringExtra("id");
                    }
                }
                break;

            case 2017:
                /*
                 * 商机来源
                 */
                if (arg1 == 2017) {
                    if (arg2 != null) {
                        bussines_chance_from.setText(arg2.getStringExtra("name"));
                        bfIpsid = arg2.getStringExtra("id");
                    }
                }
                break;
            case 2018:
                /*
                 * 产品选择
                 */
                if (arg1 == 2018) {
                    if (arg2 != null) {
                        StringBuffer proName = new StringBuffer();
                        proName.setLength(0);
                        proName.append(arg2.getStringExtra("name"));
                        bussines_chance_getproduct.setText(proName.toString());
                        pdIpsids.setLength(0);
                        pdIpsids.append(arg2.getStringExtra("id"));
                    }
                }
                break;

            default:
                break;
        }

    }

    // 判断客户是否首先被选中
    public boolean checkIsCustomerSelect() {
        if (null == bussines_customers || bussines_customers.toString().trim().equals("")
                || cid.equals("")) {
            CustomToast.showLongToast(mContext, "请先选择客户!");
            return false;
        } else {
            return true;
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
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        wheelMain.initDateTimePicker(year, month, day, hour, minute);

        dialog = new BSDialog(this, "请选择日期", timepickerview, new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (DateUtils.getStringToDate(wheelMain.getTime()) < System.currentTimeMillis()) {
                    CustomToast.showLongToast(mContext, "预计成单时间不能小于当前时间");
                }
                else {
                    bussines_dateend.setText(wheelMain.getTime());
                    dialog.dismiss();
                }

            }
        });
        dialog.show();

    }

    /***
     * post提交当前的表单
     */
    public void PostInfoBussines() {
        postState = false;
        RequestParams params = new RequestParams();
        try {
            params.put("ftoken", BSApplication.getInstance().getmCompany());
            params.put("userid", BSApplication.getInstance().getUserId());
            params.put("name", bussines_names);
            params.put("customer", cid);
            params.put("money", bussines_moneys);
            params.put("status", stateid);
            params.put("expirationDate", bussines_dateends);
            params.put("source", bfIpsid);
            params.put("remark", business_work_detailss);
            params.put("product", pdIpsids);
            params.put("bid", bid);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        // params.put("name", "woshishishi");// 传输的字符数据
        String url = BSApplication.getInstance().getHttpTitle()
                + Constant.CRM_BUSSINES_BUSSISINFOADD;
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                postState = true;
                CustomDialog.closeProgressDialog();
                CustomToast.showShortToast(mContext, "网络错误");
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                postState = true;
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(new String(arg2));
                    String str = (String) jsonObject.get("retinfo");
                    String code = (String) jsonObject.get("code");
                    if (Constant.RESULT_CODE.equals(code)) {
                        CustomToast.showShortToast(mContext, str);
                        if (null != getIntent().getSerializableExtra("vBussinesListindexVo")) {
                            CrmBussinesDetailActivity.aCrmBussinesDetailActivity.finish();
                            CrmBusinessHomeIndexOneInfo.aBusinessHomeIndexOneInfo.finish();
                            Intent i = new Intent();
                            i.putExtra("bid", vo.getBid());
                            i.setClass(mContext, CrmBusinessHomeIndexOneInfo.class);
                            if (null != getIntent().getStringExtra("stateUtilthread")) {
                                i.putExtra("stateUtilthread", getIntent().getStringExtra("stateUtilthread"));
                            }
                            startActivity(i);
                        }
                        else {
                            Intent intent = new Intent();
                            intent.putExtra("stateUtilthread", "1");
                            setResult(2015, intent);
                        }
                        CrmBusinessAddInfoMsgActivity.this.finish();
                    } else {
                        CustomToast.showShortToast(mContext, str);
                    }
                    CustomDialog.closeProgressDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });

    }

}
