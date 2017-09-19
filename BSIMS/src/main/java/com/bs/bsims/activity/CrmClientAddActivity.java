
package com.bs.bsims.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.CrmBussinesAdpterListChange;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.CrmContactVo;
import com.bs.bsims.model.CrmOptionsVO;
import com.bs.bsims.utils.BsPermissionUtils;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.view.BSDialog;
import com.bs.bsims.xutils.impl.HttpUtilsByPC;
import com.bs.bsims.xutils.impl.RequestCallBackPC;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.ex.HttpException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CrmClientAddActivity extends BaseActivity implements OnClickListener {
    public static final String DETAIL_EDIT = "detail_edit";
    public static final String CLIENT_ADD = "client_add";
    private CrmContactVo mCrmOptionsVO, mCrmCheckVO;
    private EditText mClientName, mRemark, mClientWebsite, mAbbreviation, mClientAddress;
    private TextView mClientLevel, mClientSource, mClientIndustry;
    private ImageView mClientIsRepeat;
    private String mClientNameStr = "";
    private boolean mClientNameIsCg = false;
    private BSDialog bsd;

    private String[] array = {
            "名片扫描", "手工输入"
    };

    private String[] mArraySex = {
            "1,男", "2,女"
    };

    private CrmBussinesAdpterListChange crmbussines;
    private String industryId;
    private String levelId;
    private String sourceId;
    private String numberId;
    private String lid;
    private boolean mCommitFlag = true;
    private int mStatus = 1;// 1为条件选择的数据，2为检查客户是否存在
    private String mLon;
    private String mLat;
    private ImageView mLocationImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.crm_client_add, mContentLayout);
    }

    @Override
    public boolean getDataResult() {
        return getData();

    }

    @Override
    public void updateUi() {
        if (this.getIntent().getBooleanExtra("edit", false))
            return;
        if (mStatus == 1) {
            mStatus = 2;
            if (mCrmOptionsVO.getLevel() != null) {
                mClientLevel.setText(mCrmOptionsVO.getLevel().get(0).getName());
            }
            if (mCrmOptionsVO.getSource() != null) {
                mClientSource.setText(mCrmOptionsVO.getSource().get(0).getName());
            }

        } else {
            if ("1".equals(mCrmCheckVO.getIsDuplicate())) {
                CustomToast.showLongToast(this, "亲，客户已存在，请重新填写客户");
            }
        }
    }

    @Override
    public void initView() {
        mTitleTv.setText("新增客户");
        mOkTv.setText("保存");
        mClientName = (EditText) findViewById(R.id.client_name);
        mClientIndustry = (TextView) findViewById(R.id.client_idustry);
        mClientLevel = (TextView) findViewById(R.id.client_level);
        mClientSource = (TextView) findViewById(R.id.client_source);
        mClientAddress = (EditText) findViewById(R.id.client_address);
        mRemark = (EditText) findViewById(R.id.remark);
        mAbbreviation = (EditText) findViewById(R.id.client_abbreviation);
        mClientWebsite = (EditText) findViewById(R.id.client_website);
        mClientIsRepeat = (ImageView) findViewById(R.id.customer_searbar);
        mLocationImg = (ImageView) findViewById(R.id.location_img);
        initData();
    }

    public void initData() {
        Intent intent = this.getIntent();

        if (intent.getStringExtra("name") != null && CommonUtils.isNormalString(intent.getStringExtra("name"))) {
            mClientName.setText(intent.getStringExtra("name"));
        }

        // 冲联系人创建客户需要传入联系人lid
        if (intent.getStringExtra("id") != null) {
            lid = intent.getStringExtra("id");
        }

        if (intent.getBooleanExtra("edit", false)) {
            mTitleTv.setText("编辑客户");

            mClientName.setText(intent.getStringExtra("name"));
            mAbbreviation.setText(intent.getStringExtra("abbreviation"));
            mClientIndustry.setText(intent.getStringExtra("industry"));
            mClientIndustry.setTag(intent.getStringExtra("industryId"));
            mClientSource.setText(intent.getStringExtra("source"));
            mClientSource.setTag(intent.getStringExtra("sourceId"));
            mClientLevel.setText(intent.getStringExtra("level"));
            mClientLevel.setTag(intent.getStringExtra("levelId"));
            mClientAddress.setText(intent.getStringExtra("address"));
            mClientWebsite.setText(intent.getStringExtra("website"));
            mRemark.setText(intent.getStringExtra("remark"));
        }

    }

    public String trimNum(String telNum) {
        if (telNum.substring(0, 3).equals("+86")) {
            return telNum = telNum.substring(3);
        } else {
            return telNum;
        }
    }

    @Override
    public void bindViewsListener() {
        mClientLevel.setOnClickListener(this);
        mClientSource.setOnClickListener(this);
        mOkTv.setOnClickListener(this);
        mClientIndustry.setOnClickListener(this);
        mClientAddress.setOnClickListener(this);
        mClientIsRepeat.setOnClickListener(this);
        mLocationImg.setOnClickListener(this);
        mClientName.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                mOkTv.setVisibility(View.VISIBLE);
                mClientName.setTextColor(getResources().getColor(R.color.black_01));
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (mClientNameIsCg) {
                    if (arg0.toString().equals(mClientNameStr)) {
                        mOkTv.setVisibility(View.GONE);
                        mClientName.setTextColor(getResources().getColor(R.color.red));
                    }

                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent mIntent = new Intent(CrmClientAddActivity.this, AddByPersonCRMActivity.class);
        switch (view.getId()) {
            case R.id.txt_comm_head_right:
                if (mClientName.getText().toString().trim().length() == 0) {
                    CustomToast.showLongToast(this, "请填客户信息");
                    return;
                }

                if (mCommitFlag) {
                    mCommitFlag = true;
                    commit();
                }

                break;
            case R.id.client_idustry:
                mIntent.putExtra("customerlist", mCrmOptionsVO);
                mIntent.putExtra("proKey", "-1");
                mIntent.putExtra("typekey", "1");
                mIntent.putExtra("requst_number", 2015);
                String[] industrys = CommonUtils.getStingArray(mCrmOptionsVO.getIndustry());
                CommonUtils.initSimpleListDialog(this, "请选择行业", industrys, mClientIndustry);
                break;
            case R.id.client_level:
                mIntent.putExtra("customerlist", mCrmOptionsVO);
                mIntent.putExtra("proKey", "-1");
                mIntent.putExtra("typekey", "2");
                mIntent.putExtra("requst_number", 2016);
                String[] levels = CommonUtils.getStingArray(mCrmOptionsVO.getLevel());
                CommonUtils.initSimpleListDialog(this, "请选择级别", levels, mClientLevel);
                break;
            case R.id.client_source:
                mIntent.putExtra("customerlist", mCrmOptionsVO);
                mIntent.putExtra("proKey", "-1");
                mIntent.putExtra("typekey", "3");
                mIntent.putExtra("requst_number", 2017);
                String[] sources = CommonUtils.getStingArray(mCrmOptionsVO.getSource());
                CommonUtils.initSimpleListDialog(this, "请选择来源", sources, mClientSource);
                break;

            case R.id.client_address:
                break;
            // 客户查重
            case R.id.customer_searbar:
                checkClientIsRepeat();
                break;

            case R.id.location_img:
                
                if(!BsPermissionUtils.isOPenGps(this)){
                    BsPermissionUtils.openGPS(this);
                }
                else{
                    Intent intent = new Intent();
                    intent.putExtra("key", "1");
                    intent.setClass(this, CrmVisitorFromGaodeMap.class);
                    startActivityForResult(intent, 1015);
                }
             
              
                break;
            default:
                break;
        }

    }

    public boolean getData() {
        try {

            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.CRM_CLIENT_OPTIONS, map);
            mCrmOptionsVO = gson.fromJson(jsonStr, CrmContactVo.class);

            if (HttpClientUtil.isNetworkConnected(this)) {
                if (mCrmOptionsVO == null) {
                    getData();
                }
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {

        }
    }

    // 客户行业，级别，来源的下拉选项菜单
    public void initPopView(Activity activity, final List<CrmOptionsVO> objList, final TextView tv) {
        final PopupWindow popView;
        final List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < objList.size(); i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("option", objList.get(i).getName());
            list.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(activity, list, R.layout.dropdown_approval_month_item, new String[] {
                "option"
        }, new int[] {
                R.id.textview
        });
        ListView listView = new ListView(activity);
        listView.setAdapter(adapter);
        listView.setDivider(null);

        popView = new PopupWindow(listView, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popView.setFocusable(true);
        popView.setOutsideTouchable(true);
        popView.setBackgroundDrawable(activity.getResources().getDrawable(R.color.white));
        popView.showAsDropDown(tv);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                tv.setText(list.get((int) arg3).get("option"));
                tv.setTag(objList.get((int) arg3).getId());
                popView.dismiss();
            }
        });
    }

    // 客户查重
    public void checkClientIsRepeat() {
        final String url = BSApplication.getInstance().getHttpTitle() + Constant.CRM_CLIENT_ISEXIST;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("ftoken", BSApplication.getInstance().getmCompany());
        map.put("userid", BSApplication.getInstance().getUserId());
        map.put("name", mClientName.getText().toString().trim());
        new HttpUtilsByPC().sendPostBYPC(url, map,
                /**
                 * @author Administrator
                 */
                new RequestCallBackPC() {
                    @Override
                    public void onFailurePC(HttpException arg0, String arg1) {
                        // TODO Auto-generated method stub
                        CustomToast.showShortToast(CrmClientAddActivity.this, "查询失败,请重新查询！");
                        CustomDialog.closeProgressDialog();
                    }

                    @Override
                    public void onSuccessPC(ResponseInfo rstr) {
                        // TODO Auto-generated method stub
                        CustomDialog.closeProgressDialog();
                        // editKey1 = "0";// 假设定 提高体验不刷新数据
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(new String(rstr.result.toString()));
                            String str = (String) jsonObject.get("msg");
                            String code = (String) jsonObject.get("code");
                            String isRepeat = (String) jsonObject.get("isDuplicate");
                            // 不重复
                            if (Constant.RESULT_CODE.equals(code) && isRepeat.equals("0")) {
                                CustomToast.showShortToast(CrmClientAddActivity.this, "客户名称没有重复,可以使用!");
                            } else {
                                // 记录当前输入重复的客户名称
                                mClientNameIsCg = true;
                                mClientNameStr = mClientName.getText().toString().trim();
                                mClientName.setTextColor(getResources().getColor(R.color.red));
                                mOkTv.setVisibility(View.GONE);
                                View isoffice = View.inflate(CrmClientAddActivity.this, R.layout.ishaveofficeapp, null);
                                final TextView tv_content = (TextView) isoffice.findViewById(R.id.tv_text);
                                isoffice.findViewById(R.id.office_logo).setVisibility(View.GONE);
                                tv_content.setGravity(Gravity.CENTER);
                                tv_content.setTextSize(16);
                                // 这里来个系统提醒
                                tv_content.setText("提醒:" + str);
                                bsd = new BSDialog(CrmClientAddActivity.this, "客户重复", isoffice, new OnClickListener() {
                                    @Override
                                    public void onClick(View arg0) {
                                        // TODO Auto-generated method stub
                                        bsd.dismiss();
                                    }
                                });
                                bsd.show();
                                bsd.setButtonTwoGone(true);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                });
    }

    public void commit() {
        CustomDialog.showProgressDialog(this, "正在提交数据...");
        RequestParams params = new RequestParams();

        try {
            params.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            params.put("userid", BSApplication.getInstance().getUserId());
            params.put("name", mClientName.getText().toString());
            params.put("lid", lid);
            params.put("abbreviation", mAbbreviation.getText().toString());
            // params.put("industry", industryId);
            // params.put("level", levelId);
            // params.put("source", sourceId);
            if (mClientIndustry.getTag() != null) {
                params.put("industry", mClientIndustry.getTag().toString());
            }
            if (mClientLevel.getTag() != null) {
                params.put("level", mClientLevel.getTag().toString());
            }
            if (mClientSource.getTag() != null) {
                params.put("source", mClientSource.getTag().toString());
            }
            params.put("address", mClientAddress.getText().toString());
            params.put("remark", mRemark.getText().toString());
            params.put("website", mClientWebsite.getText().toString());
            params.put("number", numberId);
            params.put("lon", mLon);
            params.put("lat", mLat);
            if (this.getIntent().getBooleanExtra("edit", false)) {
                params.put("cid", this.getIntent().getStringExtra("cid"));
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        String url = BSApplication.getInstance().getHttpTitle() + Constant.CRM_CLIENT_ADD;
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                mCommitFlag = true;
                CustomDialog.closeProgressDialog();// 关闭对话框
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                mCommitFlag = true;
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(new String(arg2));
                    String str = (String) jsonObject.get("retinfo");
                    String code = (String) jsonObject.get("code");
                    if (Constant.RESULT_CODE.equals(code)) {
                        if (CrmClientAddActivity.this.getIntent().getBooleanExtra("edit", false)) {
                            HashMap<String, String> refreshMap = new HashMap<String, String>();
                            refreshMap.put("name", mClientName.getText().toString());
                            refreshMap.put("level", mClientLevel.getText().toString());
                            refreshMap.put("address", mClientAddress.getText().toString());
                            refreshMap.put("source", mClientSource.getText().toString());
                            CommonUtils.sendBroadcast(CrmClientAddActivity.this, DETAIL_EDIT, refreshMap);
                        } else {
                            HashMap<String, String> refreshMap = new HashMap<String, String>();
                            CommonUtils.sendBroadcast(CrmClientAddActivity.this, CLIENT_ADD, refreshMap);
                        }
                        CustomToast.showShortToast(CrmClientAddActivity.this, str);
                        CrmClientAddActivity.this.finish();
                    } else {
                        CustomToast.showShortToast(CrmClientAddActivity.this, str);
                    }
                    CustomDialog.closeProgressDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        switch (arg0) {
            case 2015:
                if (arg1 == 2015) {
                    if (arg2 != null) {
                        mClientIndustry.setText(arg2.getStringExtra("name"));
                        industryId = arg2.getStringExtra("id");
                    }
                }
                break;
            case 2016:
                if (arg1 == 2016) {
                    if (arg2 != null) {
                        mClientLevel.setText(arg2.getStringExtra("name"));
                        levelId = arg2.getStringExtra("id");

                    }
                }
                break;
            case 2017:
                if (arg1 == 2017) {
                    if (arg2 != null) {
                        mClientSource.setText(arg2.getStringExtra("name"));
                        sourceId = arg2.getStringExtra("id");
                    }
                }
                break;
            case 1015:
                if (arg2 != null) {
                    mClientAddress.setText(arg2.getStringExtra("adress"));
                    mLon = arg2.getStringExtra("Lng");
                    mLat = arg2.getStringExtra("Lat");
                }
                break;
        }
    }

}
