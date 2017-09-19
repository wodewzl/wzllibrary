
package com.bs.bsims.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.fragment.CrmTradeFragment1;
import com.bs.bsims.fragment.CrmTradeFragment2;
import com.bs.bsims.fragment.CrmTradeFragment3;
import com.bs.bsims.model.CrmTranctVos;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CommonUtils.ResultCallback;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.utils.UrlUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint("ResourceAsColor")
public class CrmTradeContantDeatilsHomeTop3Activity extends BaseActivity implements OnClickListener {

    private TextView companyName, clientType, customer, textview1, textview2, textview3,
            detailInfo, shoukuanRecord, TradeTongtai, head_momtv, headRight;
    private TextView headBack;
    private LinearLayout tradeToCustomer;
    private CrmTranctVos crmTranctVo;
    private List<CrmTranctVos> crmPlanList;
    private List<CrmTranctVos> crmPaymentList;

    /**
     * moveEdit 新添字段，表示三个点下面的转移他人的显示问题
     */
    private int pagecount;

    // 信息详情、收款记录、合同动态三个界面
    private CrmTradeFragment1 mInfoFragment = null;
    private CrmTradeFragment2 mShoukuanFragment = null;
    private CrmTradeFragment3 mDongtaiFragment = null;
    private String CurrentType = "0";
    // flag用于进那个Fragment
    private boolean flag = true;
    private String changeStatus;
    private String changeStatusName;
    private String status;
    private String statusName;
    // 该字段作为判定合同状态是否改变， true为改变，此情况当返回时，要刷新数据；false为不改变
    private Boolean isChangeStatus = false;
    public static final String TRADES_EDIT = "trades_edit";// 合同状态改变；用于广播

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.crmtradecontant_main_view, mContentLayout);
        baseHeadLayout.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    private boolean getData() {
        Gson gson = new Gson();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("hid", getIntent().getStringExtra("hid"));
        try {
            String urlStr1 = UrlUtil.getUrlByMap1(Constant.CRM_RECORDE_DETAILS,
                    map);
            String jsonUrlStr1;
            jsonUrlStr1 = HttpClientUtil.get(urlStr1, Constant.ENCODING).trim();
            crmTranctVo = gson.fromJson(jsonUrlStr1,
                    CrmTranctVos.class);
            if (crmTranctVo.getCode().equals("200")) {
                crmTranctVo = crmTranctVo.getArray();
                crmPlanList = crmTranctVo.getPlanList();
                crmPaymentList = crmTranctVo.getPaymentList();
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
    public void updateUi() {
        baseHeadLayout.setBackgroundColor(Color.parseColor("#5D6FC5"));
        changeStatus = crmTranctVo.getChangeStatus();
        changeStatusName = crmTranctVo.getChangeStatusName();
        status = crmTranctVo.getStatus();
        statusName = crmTranctVo.getStatusName();
        newUpdateUi();

    }

    // 从updateUi()分离出来的原因是：当合同状态改变时，及时更新数据
    public void newUpdateUi() {
        companyName.setText(crmTranctVo.getTitle());
        clientType.setText(statusName);
        int puttype = Integer.parseInt(status);
        setStatus(puttype);
        customer.setText(crmTranctVo.getCname());
        textview1.setText("￥" + crmTranctVo.getMoney());
        textview2.setText("￥" + crmTranctVo.getPayment());
        textview3.setText("￥" + crmTranctVo.getRemain_money());
        if (crmTranctVo.getCrmEdit().equals("1")) {
            headRight.setVisibility(View.VISIBLE);
        } else {
            headRight.setVisibility(View.GONE);
        }
        if (flag) {
            setSelect(0);// 默认情况展示“详情信息”
        } else {
            setSelect(1);
            flag = true;
        }
    }

    public void setStatus(int puttype) {
        switch (puttype) {
            case 1:
                clientType.setBackgroundDrawable(getResources().getDrawable(R.drawable.frame_shixing_blue));
                break;
            case 2:
                clientType.setBackgroundDrawable(getResources().getDrawable(R.drawable.frame_shixing_yellow));
                break;
            case 3:
                clientType.setBackgroundDrawable(getResources().getDrawable(R.drawable.frame_shixing_red));
                break;
            case 4:
                clientType.setBackgroundDrawable(getResources().getDrawable(R.drawable.frame_shixing_gray));
                break;
            default:
                clientType.setBackgroundDrawable(getResources().getDrawable(R.drawable.frame_shixing_gray));
                break;
        }
    }

    // 合同状态背景颜色的设置
    public void showStatus() {
        int statusType = Integer.parseInt(crmTranctVo.getStatus());
        switch (statusType) {
            case 1:
                clientType.setBackgroundDrawable(getResources().getDrawable(R.drawable.frame_shixing_blue));
                break;
            case 2:
                clientType.setBackgroundDrawable(getResources().getDrawable(R.drawable.frame_shixing_yellow));
                break;
            case 3:
                clientType.setBackgroundDrawable(getResources().getDrawable(R.drawable.frame_shixing_red));
                break;
            case 4:
                clientType.setBackgroundDrawable(getResources().getDrawable(R.drawable.frame_shixing_gray));
                break;
            default:
                clientType.setBackgroundDrawable(getResources().getDrawable(R.drawable.frame_shixing_gray));
                break;
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void initView() {

        mHeadLayout.setVisibility(View.GONE);
        headBack = (TextView) findViewById(R.id.head_back);// 合同名称
        companyName = (TextView) findViewById(R.id.company_name);// 合同名称
        clientType = (TextView) findViewById(R.id.type);// 合同进行的阶段
        customer = (TextView) findViewById(R.id.customerse);// 客户
        // textview1 textview2，textview3分别对应于合同金额、回款金额、开票金额
        textview1 = (TextView) findViewById(R.id.textview_01);
        textview2 = (TextView) findViewById(R.id.textview_02);
        textview3 = (TextView) findViewById(R.id.textview_03);
        // addShouRecord = (TextView) findViewById(R.id.add_tv);// 添加回款记录
        detailInfo = (TextView) findViewById(R.id.detailinfo);// 详细信息
        shoukuanRecord = (TextView) findViewById(R.id.shoukuan_record);// 收款记录
        TradeTongtai = (TextView) findViewById(R.id.trade_dongtai);// 合同动态
        headRight = (TextView) findViewById(R.id.head_right);
        headRight.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.crm_menu), null, null, null);
        tradeToCustomer = (LinearLayout) findViewById(R.id.trade_to_customer);

    }

    @Override
    public void bindViewsListener() {
        detailInfo.setOnClickListener(this);
        shoukuanRecord.setOnClickListener(this);
        TradeTongtai.setOnClickListener(this);
        headBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (isChangeStatus) {
                    Intent in = new Intent(TRADES_EDIT);
                    in.putExtra("hid", getIntent().getStringExtra("hid"));
                    in.putExtra("status", status);
                    in.putExtra("statusName", statusName);
                    CrmTradeContantDeatilsHomeTop3Activity.this.sendBroadcast(in);// 广播，刷新客户主页的合同列表数据,以及我的合同列表
                }
                CrmTradeContantDeatilsHomeTop3Activity.this.finish();
            }
        });

        headRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // 当状态为未执行时，对应“意外终止”
                String[] array = {
                        "变更负责人", "添加跟进人", changeStatusName, "提醒时间"
                };
                CommonUtils.initPopViewBg(CrmTradeContantDeatilsHomeTop3Activity.this, array, headRight, mCallback, CommonUtils.getScreenWidth(CrmTradeContantDeatilsHomeTop3Activity.this) / 3);
            }
        });

        tradeToCustomer.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent in = new Intent(CrmTradeContantDeatilsHomeTop3Activity.this, CrmClientDetailActivity.class);
                in.putExtra("cid", crmTranctVo.getCid());
                in.putExtra("crmEdit", crmTranctVo.getCrmEdit());
                in.putExtra("is_from_trade", "1");// 1表示来自合同详情
                startActivity(in);
            }
        });

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (isChangeStatus) {
                Intent in = new Intent(TRADES_EDIT);
                in.putExtra("hid", getIntent().getStringExtra("hid"));
                in.putExtra("status", status);
                in.putExtra("statusName", statusName);
                CrmTradeContantDeatilsHomeTop3Activity.this.sendBroadcast(in);// 广播，刷新客户主页的合同列表数据,以及我的合同列表
            }
            CrmTradeContantDeatilsHomeTop3Activity.this.finish();
        }
        return false;
    }

    // 添加回调函数
    ResultCallback mCallback = new ResultCallback() {

        @Override
        public void callback(String str, int position) {
            if (position == 0) {
                CurrentType = "3";
                // 将负责人的名字、头像封装起来
                ArrayList<EmployeeVO> list = new ArrayList<EmployeeVO>();
                EmployeeVO eVo = new EmployeeVO();
                eVo.setFullname(crmTranctVo.getFullname());
                eVo.setHeadpic(crmTranctVo.getHeadpic());
                list.add(eVo);
                // 变更负责人
                Intent in2 = new Intent(CrmTradeContantDeatilsHomeTop3Activity.this, CrmClientDifferentHeadActivity.class);
                in2.putExtra("CurrentType", CurrentType);// CurrentType用于区分功能
                in2.putExtra("type", "2");// type用于区分是改变合同状态，还是变更负责人，因为共用一个接口
                in2.putExtra("hid", crmTranctVo.getHid());
                in2.putExtra("hiddenbottom", true);
                in2.putExtra("relation", list);// 是为了在另一个界面展现头像
                in2.putExtra("cid", crmTranctVo.getCid());
                in2.putExtra("isfrom", "2");
                startActivityForResult(in2, 520);

            } else if (position == 1) {
                CurrentType = "2";
                // 添加相关人
                Intent in1 = new Intent(CrmTradeContantDeatilsHomeTop3Activity.this, CrmClientDifferentHeadActivity.class);
                in1.putExtra("hiddentop", true);
                in1.putExtra("CurrentType", CurrentType);
                in1.putExtra("hid", crmTranctVo.getHid());
                in1.putExtra("cid", crmTranctVo.getCid());
                in1.putExtra("isfrom", "2");
                if (crmTranctVo.getInsUser() == null) {

                } else {
                    in1.putExtra("relation", (Serializable) crmTranctVo.getInsUser());
                }
                startActivityForResult(in1, 520);
            } else if (position == 2) {
                commit(changeStatus);
            } else if (position == 3) {
                Intent intent = new Intent();
                intent.putExtra("hid", crmTranctVo.getHid());
                intent.setClass(CrmTradeContantDeatilsHomeTop3Activity.this, CrmTradeContantRemindActivity.class);
                CrmTradeContantDeatilsHomeTop3Activity.this.startActivity(intent);
            }
        }
    };

    public void commit(String statStr) {
        CustomDialog.showProgressDialog(this, "正在提交数据...");
        RequestParams params = new RequestParams();

        try {
            params.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            params.put("userid", BSApplication.getInstance().getUserId());
            params.put("type", "1");
            params.put("hid", crmTranctVo.getHid());
            params.put("status", statStr);

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        String url = BSApplication.getInstance().getHttpTitle() + Constant.CRM_TRADE_EDIT;
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
                        isChangeStatus = true;
                        CustomToast.showShortToast(CrmTradeContantDeatilsHomeTop3Activity.this, str);
                        String cStatus = changeStatus;
                        String cStatusName = changeStatusName;
                        changeStatus = status;
                        changeStatusName = statusName;
                        status = cStatus;
                        statusName = cStatusName;
                        newUpdateUi();
                        // // 当合同状态改变时，刷新合同列表中的状态
                        // setResult(1, new Intent());
                    } else {
                        CustomToast.showShortToast(CrmTradeContantDeatilsHomeTop3Activity.this, str);
                    }
                    CustomDialog.closeProgressDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.detailinfo:
                setSelect(0);
                break;
            case R.id.shoukuan_record:
                setSelect(1);
                break;
            case R.id.trade_dongtai:
                setSelect(2);
                break;

            default:
                break;
        }

    }

    public void setSelect(int i) {
        ClearColorText();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);
        // 把图片设置为亮的
        // 设置内容区域
        switch (i) {
            case 0:
                detailInfo.setTextColor(getResources().getColor(R.color.white));
                detailInfo.setBackgroundResource(R.drawable.corners_tab_left_select);
                pagecount = 0;
                if (mInfoFragment == null) {
                    mInfoFragment = new CrmTradeFragment1(CrmTradeContantDeatilsHomeTop3Activity.this, crmTranctVo);
                    transaction.add(R.id.id_content, mInfoFragment);
                } else {
                    transaction.show(mInfoFragment);
                    // 由于再次进入Fragment时不会走构造方法以及再次重新加载界面（onCreateView方法），所以自定义一个方法
                    mInfoFragment.setCrmTranctVo(crmTranctVo);
                }
                break;
            case 1:
                shoukuanRecord.setTextColor(getResources().getColor(R.color.white));
                shoukuanRecord.setBackgroundResource(R.drawable.corners_tab_center_select);
                pagecount = 1;
                if (mShoukuanFragment == null) {
                    mShoukuanFragment = new CrmTradeFragment2(CrmTradeContantDeatilsHomeTop3Activity.this, crmTranctVo);
                    transaction.add(R.id.id_content, mShoukuanFragment);
                } else {
                    transaction.show(mShoukuanFragment);
                    mShoukuanFragment.setCrmVO(crmTranctVo);
                }
                break;
            case 2:
                TradeTongtai.setTextColor(getResources().getColor(R.color.white));
                TradeTongtai.setBackgroundResource(R.drawable.corners_tab_right_select);
                pagecount = 2;
                if (mDongtaiFragment == null) {
                    mDongtaiFragment = new CrmTradeFragment3(CrmTradeContantDeatilsHomeTop3Activity.this, crmTranctVo.getHid());
                    transaction.add(R.id.id_content, mDongtaiFragment);
                } else {
                    transaction.show(mDongtaiFragment);
                }
                break;

            default:
                break;

        }
        // 有时这行会报错
        try {
            transaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
     * 清除背景颜色和字体的颜色
     */
    public void ClearColorText() {
        detailInfo.setTextColor(getResources().getColor(R.color.C5));
        shoukuanRecord.setTextColor(getResources().getColor(R.color.C5));
        TradeTongtai.setTextColor(getResources().getColor(R.color.C5));
        detailInfo.setBackgroundResource(R.drawable.corners_tab_left_normal);
        shoukuanRecord.setBackgroundResource(R.drawable.corners_tab_center_normal);
        TradeTongtai.setBackgroundResource(R.drawable.corners_tab_right_normal);
    }

    private void hideFragment(FragmentTransaction transaction)
    {
        if (mInfoFragment != null) {
            transaction.hide(mInfoFragment);
        }
        if (mShoukuanFragment != null) {
            transaction.hide(mShoukuanFragment);
        }
        if (mDongtaiFragment != null) {
            transaction.hide(mDongtaiFragment);
        }

    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        // if (arg2.hasExtra("BOOLEAN")){
        // boolean = getIntent().getBooleanExtra("BOOLEAN", false);

        if (arg2 != null && (CurrentType.equals("2") || CurrentType.equals("3"))) {
            // 变更负责人、添加相关人时，更新数据，重新请求数据
            flag = true;
            new ThreadUtil(this, this).start();
            CurrentType = "0";
        } else if (arg2 != null && CurrentType.equals("0")) {
            // 当添加回款计划时，更新数据，重新请求数据
            flag = false;
            new ThreadUtil(this, this).start();
        }
    }
}
