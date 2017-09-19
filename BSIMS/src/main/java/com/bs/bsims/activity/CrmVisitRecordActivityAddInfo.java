
package com.bs.bsims.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.interfaces.RecordResult;
import com.bs.bsims.model.CrmVisitorVo;
import com.bs.bsims.time.ScreenInfo;
import com.bs.bsims.time.WheelMain;
import com.bs.bsims.utils.AnimationUtil;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.utils.SoundRecordUtil;
import com.bs.bsims.view.BSDialog;
import com.bs.bsims.xutils.impl.HttpUtilsByPC;
import com.bs.bsims.xutils.impl.RequestCallBackPC;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.ex.HttpException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class CrmVisitRecordActivityAddInfo extends BaseActivity implements OnClickListener
{

    private Context mContext;
    private String cid;// 客户的id
    private TextView visitor_customer;// 客户名称
    private EditText visitor_edit_info;// 拜访记录的信息
    private TextView visitor_selectperson;// 客户名称选填
    private TextView visitor_change;// 拜访方式
    private TextView visitor_change_dretion;// 拜访位置
    private TextView mStratTimeTv;// 拜访时间
    private TextView bs_td_name;// 存放商机或者合同的名称

    private LinearLayout visitor_change_dretion_r5, start_time_layout, visitor_selectperson_ly;
    private ImageView visitor_customerimg;

    private String editinfo;
    private String selectpersoninfo;
    private String selectpersoninfo1;
    private String selectpersoninfo2;
    private String selectpersoninfo3;
    private String selectpersoninfo4;// 位置信息
    private String selectpersoninfo5;// 时间

    private String vIsid;// 拜访方式的id
    private String vIpsid;// 拜访联系人的id

    private WheelMain wheelMain;
    private BSDialog dialog;

    private List<String> mPicturePathList;// 存储图片路径

    private LinearLayout visitor_add_state, bs_td_allly;

    private boolean mFalge = true;// 控制多次点击提交表单
    /*
     * 如果是让拜访记录列表过来的
     */
    private String vistorSate = "";

    private String hid;// 合同的id
    private String bid;// 商机的id
    private String bstatus;// 提交的商机的状态值
    private String bstatuskey;// 保存商机的状态的值
    // 切换到商机取商机的id，切换到合同取合同id，切换到无关联取两个都不取
    private String tabKeyValue = "3";
    private String tabKey = "3";

    /*
     * 点击拜访方式 显示4中类型
     */

    private LinearLayout r3_chilrd, r3;
    private TextView r3_chilrd_1, r3_chilrd_2, r3_chilrd_3, r3_chilrd_4;

    /*
     * 如果关联签到，返回过来显示的布局
     */
    private LinearLayout sign_info;
    private ImageView sign_imgindexone;
    private TextView sign_time, sign_address;
    private ImageLoader imageLoader;
    private String csid;// 签到的id

    public boolean postState = true;// 控制多次提交按钮

    private PopupWindows popupWindows;// 关联商机的弹出层

    /*
     * 关联商机和关联合同弹出层需要的布局
     */
    private TextView bs_name, visitor_chilrd_1, visitor_chilrd_2, visitor_chilrd_3,
            visitor_chilrd_4, visitor_chilrd_5, visitor_chilrd_6;
    private LinearLayout visitor_all_state;
    private TextView td_name, visitor_chilrd_td_1, visitor_chilrd_td_2, visitor_chilrd_td_3,
            visitor_chilrd_td_4, visitor_chilrd_td_5;
    private LinearLayout visitor_all_state_td;

    private SoundRecordUtil mRecordUtil;
    private TextView mRemindTimeTv;

    @Override
    public void baseSetContentView() {
        // TODO Auto-generated method stub
        View layout = View.inflate(this, R.layout.crmvisitrecord_add_visitedinfos, null);
        mContentLayout.addView(layout);
        mContext = this;
        imageLoader = ImageLoader.getInstance();
        mPicturePathList = new ArrayList<String>();
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

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        mLoadingLayout.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);
        visitor_customer = (TextView) findViewById(R.id.visitor_customer);
        visitor_edit_info = (EditText) findViewById(R.id.visitor_edit_info);
        visitor_selectperson = (TextView) findViewById(R.id.visitor_selectperson);
        visitor_change = (TextView) findViewById(R.id.visitor_change);
        visitor_change_dretion = (TextView) findViewById(R.id.visitor_change_dretion);
        mStratTimeTv = (TextView) findViewById(R.id.start_time_tv);
        visitor_customerimg = (ImageView) findViewById(R.id.visitor_customerimg);
        bs_td_allly = (LinearLayout) findViewById(R.id.bs_td_allly);
        bs_td_name = (TextView) findViewById(R.id.bs_td_name);
        visitor_add_state = (LinearLayout) findViewById(R.id.visitor_add_state);
        start_time_layout = (LinearLayout) findViewById(R.id.start_time_layout);
        visitor_change_dretion_r5 = (LinearLayout) findViewById(R.id.visitor_change_dretion_r5);
        visitor_selectperson_ly = (LinearLayout) findViewById(R.id.visitor_selectperson_ly);
        mTitleTv.setText("添加跟单记录");
        if (getIntent().getStringExtra("vistorSate").equals("1")) {
            vistorSate = getIntent().getStringExtra("vistorSate");
            visitor_customer.setText("关联客户：");
            visitor_customerimg.setVisibility(View.VISIBLE);
            bs_td_allly.setOnClickListener(this);
        }
        else {
            if (null != getIntent().getStringExtra("cname"))
                visitor_customer.setText("客户:" + getIntent().getStringExtra("cname"));
            else {
                visitor_customer.setText("");
            }
            bs_td_allly.setOnClickListener(this);
            cid = getIntent().getStringExtra("cid");// 必穿
            visitor_customerimg.setVisibility(View.GONE);
            vistorSate = getIntent().getStringExtra("vistorSate");
            if (null == getIntent().getStringExtra("hid")) {
                hid = "";
            }
            else {
                hid = getIntent().getStringExtra("hid"); // 必穿
            }
            if (null == getIntent().getStringExtra("bid")) {
                bid = "";
            }
            else {
                bid = getIntent().getStringExtra("bid"); // 必穿
                bs_td_allly.setOnClickListener(null);
                tabKey = "1";
                if (null != getIntent().getStringExtra("bstatus")
                        && null != getIntent().getStringExtra("bname")) {
                    bs_td_name.setText("商机:" + getIntent().getStringExtra("bname"));
                    bs_td_name.setTextColor(Color.BLACK);
                    bstatus = getIntent().getStringExtra("bstatus");
                    bs_td_allly.setOnClickListener(null);
                    // bs_td_name.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null,
                    // null);
                    bs_td_name.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                }

            }

        }
        r3 = (LinearLayout) findViewById(R.id.r3);
        r3_chilrd = (LinearLayout) findViewById(R.id.r3_chilrd);
        r3_chilrd_1 = (TextView) findViewById(R.id.r3_chilrd_1);
        r3_chilrd_2 = (TextView) findViewById(R.id.r3_chilrd_2);
        r3_chilrd_3 = (TextView) findViewById(R.id.r3_chilrd_3);
        r3_chilrd_4 = (TextView) findViewById(R.id.r3_chilrd_4);

        sign_info = (LinearLayout) findViewById(R.id.sign_info);
        sign_imgindexone = (ImageView) findViewById(R.id.sign_imgindexone);
        sign_time = (TextView) findViewById(R.id.sign_time);
        sign_address = (TextView) findViewById(R.id.sign_address);
        popupWindows = new PopupWindows(mContext, visitor_edit_info);

        mRemindTimeTv = (TextView) findViewById(R.id.remind_time_tv);
    }

    @Override
    public void bindViewsListener() {
        // TODO Auto-generated method stub
        start_time_layout.setOnClickListener(this);
        visitor_add_state.setOnClickListener(this);
        sign_info.setOnClickListener(this);
        sign_imgindexone.setOnClickListener(this);
        r3.setOnClickListener(this);
        visitor_change_dretion_r5.setOnClickListener(this);
        visitor_selectperson_ly.setOnClickListener(this);
        mOkTv.setText("添加");
        mOkTv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (checkFromInfoMsg()) {
                    if (null != mRecordUtil)
                        mRecordUtil.removeView();
                    CustomDialog.showProgressDialog(mContext, "正在提交拜访记录..");
                    if (vistorSate.equals("1")) {
                        if (checkCustomer() && postState) {
                            postCustomerInfo();
                        }
                        return;
                    }
                    // 如果不是重客户列表跳转过阿里的
                    else {
                        postCustomerInfo();
                    }
                }

            }
        });
        selectVistorByWay();

        visitor_edit_info.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    mRecordUtil = new SoundRecordUtil(mContext, new RecordResult() {
                        @Override
                        public void getRecordResult(String result) {
                            String str = visitor_edit_info.getText().toString().trim()
                                    + result.toString().trim();
                            visitor_edit_info.setText(str);
                        }
                    }, 900, 480);
                } else {
                    // 此处为失去焦点时的处理内容
                    if (null != mRecordUtil)
                        mRecordUtil.removeView();
                }

            }
        });
        mRemindTimeTv.setOnClickListener(this);
        mRemindTimeTv.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (mRemindTimeTv.getText().toString().length() > 0 && DateUtils.getStringToDate(mRemindTimeTv.getText().toString(), "yyyy-MM-dd HH:mm")
                        < DateUtils.getStringToDate(DateUtils.getCurrentDate(), "yyyy-MM-dd HH:mm")) {
                    CustomToast.showLongToast(CrmVisitRecordActivityAddInfo.this, "请选择有效时间");
                    mRemindTimeTv.setText("");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.visitor_selectperson_ly:
                // 跳转到联系人
                if (checkCustomer()) {
                    // popupWindows.showAtLocation(r5, Gravity.BOTTOM, 0, 0);
                    Intent intent = new Intent();
                    intent.putExtra("proKey", "7");
                    intent.putExtra("typekey", "7");
                    intent.putExtra("cid", cid);
                    intent.putExtra("cname", visitor_customer.getText().toString().trim());
                    intent.setClass(CrmVisitRecordActivityAddInfo.this,
                            AddByPersonCRMActivity.class);
                    intent.putExtra("requst_number", 2020);
                    startActivityForResult(intent, 2020);
                }

                break;

            case R.id.r3:
                // 选择拜访方式
                if (checkCustomer()) {
                    // AnimationUtil.setStartTranlteInAnimation(mContext, r3_chilrd);r3_chilrd
                    r3_chilrd.setVisibility(View.VISIBLE);
                    r3_chilrd.startAnimation(AnimationUtils.loadAnimation(mContext,
                            R.anim.kcalendar_push_right_in));
                }

                break;

            case R.id.visitor_change_dretion_r5:
                Intent i = new Intent();
                i.setClass(mContext, CrmVisitorSignActivity.class);
                i.putExtra("SelectKey", "1");
                startActivityForResult(i, 1111);

                break;
            case R.id.start_time_layout:
                initDateView();
                break;

            case R.id.visitor_add_state:
                /***
                 * 冲拜访记录大列表跳转过来的就要点击添加客户
                 */
                if (vistorSate.equals("1")) {

                    // if (!visitor_customer.getText().toString().equals("关联客户：")) {
                    // CustomToast.showShortToast(mContext, "您已经选择客户了");
                    // return;
                    // }

                    // Intent intent1 = new Intent();
                    // intent1.putExtra("proKey", "7");
                    // intent1.putExtra("typekey", "-1");
                    // intent1.setClass(CrmVisitRecordActivityAddInfo.this,
                    // AddByPersonCRMActivity.class);
                    // intent1.putExtra("requst_number", 2014);// 拜访记录新选择客户
                    // startActivityForResult(intent1, 2014);
                    Intent intent1 = new Intent();
                    intent1.putExtra("type", "2");
                    intent1.putExtra("name", "选择客户");
                    intent1.putExtra("resulut_code", 2014);
                    intent1.setClass(this, CrmOptionsListActivity.class);
                    startActivityForResult(intent1, 2014);
                }

                break;

            case R.id.bs_td_allly:
                if (checkCustomer()) {
                    popupWindows.showAsDropDown(visitor_edit_info);
                    CustomDialog.backgroundAlphaStart(CrmVisitRecordActivityAddInfo.this);
                }

                break;

            // 点击叉 把带过来的签到记录清除
            case R.id.sign_info:
                sign_info.setVisibility(View.GONE);
                csid = "";
                selectpersoninfo = "";
                mPicturePathList.clear();
                break;

            // 点击显示签到记录的图片

            case R.id.sign_imgindexone:
                if (mPicturePathList != null && mPicturePathList.size() > 0) {
                    Intent intent = new Intent();
                    // intent.putStringArrayListExtra("piclist", (ArrayList<String>) strlist);
                    intent.putStringArrayListExtra("piclist", (ArrayList<String>) mPicturePathList);
                    intent.setClass(mContext, ImagePreviewActivity.class);
                    intent.putExtra("imgIndex", 0);
                    mContext.startActivity(intent);
                }

                break;

            case R.id.remind_time_tv:
                CommonUtils.initDateView(this, "请选择日期", mRemindTimeTv, 1);
                break;

        }

    }

    public boolean checkFromInfoMsg() {

        /*
         * @param userid 用户id--
         * @param customer 客户ID-- cid
         * @param contacts 联系人ID-- vIpsid
         * @param info 拜访内容-- editinfo
         * @param mode 拜访方式ID-- vIsid
         * @param signin 关联位置签到-- csid(选传)
         * @param time 拜访时间 -- selectpersoninfo5
         * @param bid 关联商机ID-- bid(选传)
         * @param hid 关联合同ID-- hid(选传)
         * @param bstatus 商机状态-- bstatus(选传)
         */

        editinfo = visitor_edit_info.getText().toString().trim();
        if (null == vIpsid || vIpsid.equals("")) {
            CustomToast.showShortToast(mContext, "请选择拜访人");
            return false;
        }
        if (null == editinfo || editinfo.equals("")) {
            CustomToast.showShortToast(mContext, "请填跟单内容");
            return false;
        }
        if (null == vIsid || vIsid.equals("")) {
            CustomToast.showShortToast(mContext, "请选择拜访方式");
            return false;
        }

        if (!bid.equals("")) {
            if (null == bstatus || bstatus.equals("")) {
                CustomToast.showShortToast(mContext, "请选择商机状态");
                return false;
            }
        }
        // 如果等于1的时候（上门拜访）
        if ("1".equals(vIsid)) {
            if (null == csid || csid.equals("")) {
                CustomToast.showShortToast(mContext, "请选择签到位置");
                return false;
            }
        }
        else {
            if (null == selectpersoninfo5 || selectpersoninfo5.equals("")) {
                CustomToast.showShortToast(mContext, "请选择拜访时间");
                return false;
            }
        }

        return true;
    }

    /**
     * 提交联系人的详细信息
     */
    public void postCustomerInfo() {
        postState = false;
        final String url = BSApplication.getInstance().getHttpTitle()
                + Constant.CRM_VISTIORADDINFO;

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("ftoken", BSApplication.getInstance().getmCompany());
        map.put("userid", BSApplication.getInstance().getUserId());
        map.put("customer", cid);
        map.put("info", editinfo);
        map.put("mode", vIsid);
        map.put("contacts", vIpsid);
        if ("1".equals(vIsid)) {
            map.put("signin", csid);
        }
        else {
            map.put("time", selectpersoninfo5);
        }

        if (tabKey.equals("1")) {
            map.put("bid", bid);
            map.put("bstatus", bstatus);
        }
        else if (tabKey.equals("2")) {
            map.put("hid", hid);
        }

        map.put("remindtime", mRemindTimeTv.getText().toString());

        new HttpUtilsByPC().sendPostBYPC(url, map,
                new RequestCallBackPC() {
                    @Override
                    public void onFailurePC(HttpException arg0, String arg1) {
                        // TODO Auto-generated method stub
                        CustomDialog.closeProgressDialog();
                        CustomToast.showShortToast(mContext, "网络似乎断开了哦");
                        postState = true;
                    }
                           @Override
                    public void onSuccessPC(ResponseInfo rstr) {
                        // TODO Auto-generated method stub
                        // CustomLog.e("filenpathtrue",
                        postState = true;
                        JSONObject jsonObject;
                        cid = "";
                        try {
                            jsonObject = new JSONObject(new String(rstr.result.toString()));
                            String code = (String) jsonObject.get("code");
                            String str = (String) jsonObject.get("retinfo");
                            CustomDialog.closeProgressDialog();
                            if (Constant.RESULT_CODE.equals(code)) {
                                // CommonUtils.sendBroadcast(CreativeIdeaNewActivity.this,
                                // Constant.HOME_MSG);
                                // CreativeIdeaNewActivity.this.finish();
                                CustomToast.showLongToast(mContext, str);
                                mOkTv.setVisibility(View.VISIBLE);
                                // 这个判断没用了，但是不敢删
                                if (vistorSate.equals("2")) {
                                    // CrmBusinessHomeIndexOneInfo.instance1.finish();
                                    Intent intent = new Intent();
                                    intent.putExtra("bid", bid);
                                    intent.putExtra("typekey", 0);
                                    intent.putExtra("stateUtilthread", "1");
                                    // startActivity(intent);
                                    setResult(2015, intent);
                                }
                                // 从客户那跳转过来
                                else if (vistorSate.equals("3")) {
                                    Intent intent = new Intent();
                                    // intent.putExtra("bid", bid);
                                    // intent.putExtra("typekey", 0);
                                    intent.putExtra("stateUtilthread", "1");
                                    // startActivity(intent);
                                    setResult(2015, intent);
                                }

                                // 从列表上添加过来
                                else if (vistorSate.equals("1")) {
                                    Intent intent = new Intent();
                                    // intent.putExtra("bid", bid);
                                    // intent.putExtra("typekey", 0);
                                    // startActivity(intent);
                                    intent.putExtra("boolean", true);
                                    setResult(2015, intent);
                                }
                                // 如果是从商机那跳转过来的
                                if (vistorSate.equals("4")) {
                                    Intent intent = new Intent();
                                    intent.putExtra("refreshFragment", "1");
                                    setResult(2013, intent);
                                }

                                CrmVisitRecordActivityAddInfo.this.finish();
                            } else {
                                CustomToast.showShortToast(mContext, str);
                                mOkTv.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                });

    }

    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {

        switch (arg0) {
            case 2014:
                /*
                 * 客户选择完成
                 */
                if (arg1 == 2014) {
                    if (arg2 != null) {
                        /** 采用post请求 */
                        visitor_customer.setText(arg2.getStringExtra("name"));
                        visitor_customer.setTextColor(getResources().getColor(R.color.C4));
                        cid = arg2.getStringExtra("id");
                        clearAllTextValue();
                    }
                }
                break;

            case 2020:
                if (arg1 == 2020) {
                    if (arg2 != null) {
                        visitor_selectperson.setText(arg2.getStringExtra("name"));
                        visitor_selectperson.setTextColor(getResources().getColor(R.color.C4));
                        vIpsid = arg2.getStringExtra("pid");
                    }
                }
                break;
            // 关联商机返回
            case 84:
                if (arg1 == 84) {
                    if (arg2 != null) {
                        bstatuskey = arg2.getStringExtra("bstatus");
                        bstatus = bstatuskey;
                        int state = Integer.parseInt(bstatus);
                        SetColorBussinesYes(state);
                        visitor_all_state.setVisibility(View.VISIBLE);
                        bid = arg2.getStringExtra("bs_id");
                        bs_name.setText(arg2.getStringExtra("bs_name"));
                        visitor_selectperson.setTextColor(getResources().getColor(R.color.C4));

                    }
                }
                break;
            // 关联合同返回
            case 85:
                if (arg1 == 85) {
                    if (arg2 != null) {
                        td_name.setText(arg2.getStringExtra("td_name"));
                        td_name.setTextColor(getResources().getColor(R.color.C4));
                        hid = arg2.getStringExtra("td_id");
                        visitor_all_state_td.setVisibility(View.VISIBLE);
                        SetColorTrande(Integer.parseInt(arg2.getStringExtra("bstatus")));
                    }
                }
                break;
            // 关联签到的信息
            case 1111:
                if (arg1 == 1111) {
                    if (arg2 != null) {
                        mPicturePathList.clear();
                        // AnimationUtil.setShowAnimation(sign_info);
                        sign_info.setVisibility(View.VISIBLE);
                        sign_imgindexone.setVisibility(View.VISIBLE);
                        selectpersoninfo4 = ((CrmVisitorVo) arg2.getSerializableExtra("vIsitor"))
                                .getAddress();
                        String time = ((CrmVisitorVo) arg2.getSerializableExtra("vIsitor"))
                                .getAddtime();
                        csid = ((CrmVisitorVo) arg2.getSerializableExtra("vIsitor"))
                                .getCsid();
                        sign_address.setText(selectpersoninfo4);
                        sign_time.setText(time);
                        // sign_info.setVisibility(View.VISIBLE);
                        if (null != ((CrmVisitorVo) arg2.getSerializableExtra("vIsitor"))
                                .getImgs()) {
                            mPicturePathList = ((CrmVisitorVo) arg2.getSerializableExtra("vIsitor"))
                                    .getImgs();
                            imageLoader.displayImage(mPicturePathList.get(0), sign_imgindexone,
                                    CommonUtils.initImageLoaderOptions1());
                        }
                        else {
                            sign_imgindexone.setVisibility(View.GONE);
                        }

                    }
                }
                break;

            default:
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
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        wheelMain.initDateTimePicker(year, month, day, hour, minute);

        dialog = new BSDialog(this, "请选择日期", timepickerview, new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (ClearTimeProblem(wheelMain.getTime())) {
                    mStratTimeTv.setText(wheelMain.getTime());
                    mStratTimeTv.setTextColor(getResources().getColor(R.color.C4));
                    selectpersoninfo5 = wheelMain.getTime().toString();
                    csid = "";
                    dialog.dismiss();
                }

            }
        });
        dialog.show();

    }

    public boolean checkCustomer() {
        if (null == cid || cid.equals("")) {
            CustomToast.showShortToast(mContext, "请选择客户");
            return false;
        }
        return true;
    }

    // 拜访时间判断是否清除
    public boolean ClearTimeProblem(String time) {
        if (DateUtils.getStringToDate(time) > System.currentTimeMillis()) {
            CustomToast.showShortToast(mContext, "不能大于当前时间");
            mStratTimeTv.setText("");
            return false;
        }
        else {
            return true;
        }
    }

    /*
     * 选择哪种拜访目的
     */
    public void selectVistorByWay() {

        r3_chilrd_1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {

                mStratTimeTv.setText("");
                selectpersoninfo5 = "";
                start_time_layout.setVisibility(View.GONE);
                AnimationUtil.setStartTranlteOutAnimation(mContext, r3_chilrd);
                visitor_change_dretion_r5.setVisibility(View.VISIBLE);
                vIsid = "1";
                visitor_change.setText(r3_chilrd_1.getText().toString().trim());
                visitor_change.setVisibility(View.VISIBLE);
                if (null != csid && !"".equals(csid)) {
                    sign_info.setVisibility(View.VISIBLE);
                }
                else {
                    sign_info.setVisibility(View.GONE);
                }

            }
        });
        r3_chilrd_2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                AnimationUtil.setStartTranlteOutAnimation(mContext, r3_chilrd);
                visitor_change_dretion_r5.setVisibility(View.GONE);
                vIsid = "2";
                visitor_change.setText(r3_chilrd_2.getText().toString().trim());
                visitor_change.setVisibility(View.VISIBLE);
                if (sign_info.getVisibility() == View.VISIBLE) {
                    // AnimationUtil.setHideAnimation(sign_info);
                    sign_info.setVisibility(View.GONE);
                }
                else {
                    sign_info.setVisibility(View.GONE);
                }

                start_time_layout.setVisibility(View.VISIBLE);
            }
        });
        r3_chilrd_3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                AnimationUtil.setStartTranlteOutAnimation(mContext, r3_chilrd);
                visitor_change_dretion_r5.setVisibility(View.GONE);
                vIsid = "3";
                visitor_change.setText(r3_chilrd_3.getText().toString().trim());
                visitor_change.setVisibility(View.VISIBLE);
                if (sign_info.getVisibility() == View.VISIBLE) {
                    sign_info.setVisibility(View.GONE);
                }
                else {
                    sign_info.setVisibility(View.GONE);
                }

                start_time_layout.setVisibility(View.VISIBLE);
            }
        });
        r3_chilrd_4.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                AnimationUtil.setStartTranlteOutAnimation(mContext, r3_chilrd);
                visitor_change_dretion_r5.setVisibility(View.GONE);
                vIsid = "4";
                visitor_change.setText(r3_chilrd_4.getText().toString().trim());
                visitor_change.setVisibility(View.VISIBLE);
                if (sign_info.getVisibility() == View.VISIBLE) {
                    sign_info.setVisibility(View.GONE);
                }
                else {
                    sign_info.setVisibility(View.GONE);
                }

                start_time_layout.setVisibility(View.VISIBLE);
            }
        });

    }

    public class PopupWindows extends PopupWindow implements OnClickListener {
        private LinearLayout bs_allview, bs_ly, td_ly, td_allview;
        private TextView detailinfo, detailinfo2, shoukuan_record, canle, sure;

        @SuppressLint("NewApi")
        public PopupWindows(Context mContext, View parent) {

            super(mContext);
            /*
             * 原来的圆角布局 <!-- android:background="@drawable/linearystork_bule"
             * android:background="@drawable/corners_tab_left_normal"
             * android:background="@drawable/corners_tab_center_normal"
             * android:background="@drawable/corners_tab_right_select" -->
             */
            View view = View
                    .inflate(mContext, R.layout.crm_visitaddinfo_pop, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.fade_ins));
            /* 商机布局 */
            bs_allview = (LinearLayout) view.findViewById(R.id.bs_allview);
            detailinfo = (TextView) view.findViewById(R.id.detailinfo);
            detailinfo.setOnClickListener(this);
            detailinfo2 = (TextView) view.findViewById(R.id.detailinfo2);
            detailinfo2.setOnClickListener(this);
            shoukuan_record = (TextView) view.findViewById(R.id.shoukuan_record);
            shoukuan_record.setOnClickListener(this);
            LinearLayout ll_popup = (LinearLayout) view
                    .findViewById(R.id.ll_popup);
            bs_name = (TextView) view
                    .findViewById(R.id.bs_name);
            visitor_chilrd_1 = (TextView) view
                    .findViewById(R.id.visitor_chilrd_bs_1);
            visitor_chilrd_1.setOnClickListener(this);
            visitor_chilrd_2 = (TextView) view
                    .findViewById(R.id.visitor_chilrd_bs_2);
            visitor_chilrd_2.setOnClickListener(this);
            visitor_chilrd_3 = (TextView) view
                    .findViewById(R.id.visitor_chilrd_bs_3);
            visitor_chilrd_3.setOnClickListener(this);
            visitor_chilrd_4 = (TextView) view
                    .findViewById(R.id.visitor_chilrd_bs_4);
            visitor_chilrd_4.setOnClickListener(this);
            visitor_chilrd_5 = (TextView) view
                    .findViewById(R.id.visitor_chilrd_bs_5);
            visitor_chilrd_5.setOnClickListener(this);
            visitor_chilrd_6 = (TextView) view
                    .findViewById(R.id.visitor_chilrd_bs_6);
            visitor_chilrd_6.setOnClickListener(this);
            visitor_all_state = (LinearLayout) view
                    .findViewById(R.id.visitor_all_state_bs);
            bs_ly = (LinearLayout) view.findViewById(R.id.bs_ly);
            bs_ly.setOnClickListener(this);
            canle = (TextView) view.findViewById(R.id.canle);
            canle.setOnClickListener(this);
            sure = (TextView) view.findViewById(R.id.sure);
            sure.setOnClickListener(this);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.push_bottom_in_2));

            /* 合同布局 */
            td_allview = (LinearLayout) view.findViewById(R.id.td_allview);
            td_ly = (LinearLayout) view.findViewById(R.id.td_ly);
            td_ly.setOnClickListener(this);
            td_name = (TextView) view.findViewById(R.id.td_name);
            visitor_all_state_td = (LinearLayout) view.findViewById(R.id.visitor_all_state_td);
            visitor_chilrd_td_1 = (TextView) view.findViewById(R.id.visitor_chilrd_td_1);
            visitor_chilrd_td_2 = (TextView) view.findViewById(R.id.visitor_chilrd_td_2);
            visitor_chilrd_td_3 = (TextView) view.findViewById(R.id.visitor_chilrd_td_3);
            visitor_chilrd_td_4 = (TextView) view.findViewById(R.id.visitor_chilrd_td_4);
            visitor_chilrd_td_5 = (TextView) view.findViewById(R.id.visitor_chilrd_td_5);

            setWidth(LayoutParams.FILL_PARENT);
            setHeight(LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setOnDismissListener(new OnDismissListener() {

                @Override
                public void onDismiss() {
                    // TODO Auto-generated method stub
                    CustomDialog.backgroundAlphaEnd(CrmVisitRecordActivityAddInfo.this);
                }
            });
            setContentView(view);
            update();

        }

        public void initview() {

        }

        @SuppressLint("NewApi")
        public void createBussinesView() {
            tabKeyValue = "1";
            ClearColorText();
            bs_allview.setVisibility(View.VISIBLE);
            td_allview.setVisibility(View.GONE);
            detailinfo.setCompoundDrawablesWithIntrinsicBounds(
                    getResources().getDrawable(R.drawable.common_ic_selected), null, null, null);
            detailinfo.setTextColor(getResources().getColor(R.color.bule_go));

        }

        @SuppressLint("NewApi")
        public void createTrandeView() {
            tabKeyValue = "2";
            ClearColorText();
            bs_allview.setVisibility(View.GONE);
            td_allview.setVisibility(View.VISIBLE);
            detailinfo2.setCompoundDrawablesWithIntrinsicBounds(
                    getResources().getDrawable(R.drawable.common_ic_selected), null, null, null);
            detailinfo2.setTextColor(getResources().getColor(R.color.bule_go));

        }

        @SuppressLint("NewApi")
        public void destoryView() {
            tabKeyValue = "3";
            ClearColorText();
            bs_allview.setVisibility(View.GONE);
            td_allview.setVisibility(View.GONE);
            shoukuan_record.setCompoundDrawablesWithIntrinsicBounds(
                    getResources().getDrawable(R.drawable.common_ic_selected), null, null, null);
            shoukuan_record.setTextColor(getResources().getColor(R.color.bule_go));

            // shoukuan_record.setBackgroundResource(R.drawable.corners_tab_right_select);
            // shoukuan_record.setTextColor(getResources().getColor(R.color.white));
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.detailinfo:
                    createBussinesView();
                    break;
                case R.id.detailinfo2:
                    createTrandeView();
                    break;
                case R.id.shoukuan_record:
                    destoryView();
                    break;
                // 选择商机列表

                case R.id.bs_ly:
                    Intent intent1 = new Intent();
                    intent1.putExtra("proKey", "8");
                    intent1.putExtra("typekey", "4");
                    intent1.putExtra("cid", cid);
                    intent1.setClass(CrmVisitRecordActivityAddInfo.this,
                            AddByPersonCRMActivity.class);
                    intent1.putExtra("requst_number", 84);
                    startActivityForResult(intent1, 84);
                    break;

                case R.id.td_ly:
                    Intent intent11 = new Intent();
                    intent11.putExtra("proKey", "8");
                    intent11.putExtra("typekey", "5");
                    intent11.putExtra("cid", cid);
                    intent11.setClass(CrmVisitRecordActivityAddInfo.this,
                            AddByPersonCRMActivity.class);
                    intent11.putExtra("requst_number", 85);
                    startActivityForResult(intent11, 85);
                    break;

                case R.id.visitor_chilrd_bs_1:
                    if (checkOnBussinesView()) {
                        SetColorBussinesYes(1);
                    }

                    break;
                case R.id.visitor_chilrd_bs_2:
                    if (checkOnBussinesView()) {
                        SetColorBussinesYes(2);
                    }
                    break;
                case R.id.visitor_chilrd_bs_3:
                    if (checkOnBussinesView()) {
                        SetColorBussinesYes(3);
                    }
                    break;
                case R.id.visitor_chilrd_bs_4:
                    if (checkOnBussinesView()) {
                        SetColorBussinesYes(4);
                    }
                    break;
                case R.id.visitor_chilrd_bs_5:
                    if (!bstatus.equals("5")) {
                        CustomToast.showShortToast(mContext, "不能直接更改赢单状态");
                    }

                    break;
                case R.id.visitor_chilrd_bs_6:
                    if (checkOnBussinesView()) {
                        SetColorBussinesYes(6);
                    }

                    break;

                case R.id.canle:
                    popupWindows.dismiss();
                    CustomDialog.backgroundAlphaEnd(CrmVisitRecordActivityAddInfo.this);
                    break;
                case R.id.sure:
                    tabKey = tabKeyValue;
                    if (tabKey.equals("1")) {
                        if (null != bs_name.getText().toString()
                                && !bs_name.getText().toString().equals("")) {
                            bs_td_name.setText("商机: " + bs_name.getText().toString());
                            bs_td_name.setTextColor(getResources().getColor(R.color.C4));
                        }
                        else {
                            tabKey = "3";// 证明用户没有选择商机，只是切换过来
                        }

                    }
                    else if (tabKey.equals("2")) {
                        if (null != td_name.getText().toString()
                                && !td_name.getText().toString().equals("")) {
                            bs_td_name.setText("合同: " + td_name.getText().toString());
                            bs_td_name.setTextColor(getResources().getColor(R.color.C4));
                        }
                        else {
                            tabKey = "3";// 证明用户没有选择合同，只是切换过来
                        }
                    }
                    else {
                        bs_td_name.setText("");
                    }
                    popupWindows.dismiss();
                    CustomDialog.backgroundAlphaEnd(CrmVisitRecordActivityAddInfo.this);
                    break;

                default:
                    break;
            }
        }

        /*
         * 清除背景颜色和字体的颜色
         */
        // public void ClearColorText() {
        // detailinfo.setTextColor(getResources().getColor(R.color.bule_go));
        // detailinfo2.setTextColor(getResources().getColor(R.color.bule_go));
        // shoukuan_record.setTextColor(getResources().getColor(R.color.bule_go));
        // detailinfo.setBackgroundResource(R.drawable.corners_tab_left_normal);
        // detailinfo2.setBackgroundResource(R.drawable.corners_tab_center_normal);
        // shoukuan_record.setBackgroundResource(R.drawable.corners_tab_right_normal);
        // }
        public void ClearColorText() {
            detailinfo.setTextColor(getResources().getColor(R.color.black));
            detailinfo2.setTextColor(getResources().getColor(R.color.black));
            shoukuan_record.setTextColor(getResources().getColor(R.color.black));
            detailinfo.setCompoundDrawablesWithIntrinsicBounds(
                    getResources().getDrawable(R.drawable.common_ic_unselect), null, null, null);
            detailinfo2.setCompoundDrawablesWithIntrinsicBounds(
                    getResources().getDrawable(R.drawable.common_ic_unselect), null, null, null);
            shoukuan_record.setCompoundDrawablesWithIntrinsicBounds(
                    getResources().getDrawable(R.drawable.common_ic_unselect), null, null, null);

        }

    }

    /*
     * 清除商机状态的色块
     */
    public void ClearColorBussines() {
        visitor_chilrd_1.setTextColor(getResources().getColor(R.color.C5));
        visitor_chilrd_2.setTextColor(getResources().getColor(R.color.C5));
        visitor_chilrd_3.setTextColor(getResources().getColor(R.color.C5));
        visitor_chilrd_4.setTextColor(getResources().getColor(R.color.C5));
        visitor_chilrd_5.setTextColor(getResources().getColor(R.color.C5));
        visitor_chilrd_6.setTextColor(getResources().getColor(R.color.C5));
        visitor_chilrd_1.setBackgroundResource(R.color.white);
        visitor_chilrd_2.setBackgroundResource(R.color.white);
        visitor_chilrd_3.setBackgroundResource(R.color.white);
        visitor_chilrd_4.setBackgroundResource(R.color.white);
        visitor_chilrd_5.setBackgroundResource(R.color.white);
        visitor_chilrd_6.setBackgroundResource(R.color.white);
    }

    /*
     * 清除合同状态的色块
     */
    public void ClearColorTrande() {
        visitor_chilrd_td_1.setTextColor(getResources().getColor(R.color.C5));
        visitor_chilrd_td_2.setTextColor(getResources().getColor(R.color.C5));
        visitor_chilrd_td_3.setTextColor(getResources().getColor(R.color.C5));
        visitor_chilrd_td_4.setTextColor(getResources().getColor(R.color.C5));
        visitor_chilrd_td_5.setTextColor(getResources().getColor(R.color.C5));
        visitor_chilrd_td_1.setBackgroundResource(R.color.white);
        visitor_chilrd_td_2.setBackgroundResource(R.color.white);
        visitor_chilrd_td_3.setBackgroundResource(R.color.white);
        visitor_chilrd_td_4.setBackgroundResource(R.color.white);
        visitor_chilrd_td_5.setBackgroundResource(R.color.white);
    }

    /*
     * 清除商机状态的色块
     */
    public void SetColorTrande(int key) {
        ClearColorTrande();
        switch (key) {
            case 1:
                visitor_chilrd_td_1.setTextColor(getResources().getColor(R.color.white));
                visitor_chilrd_td_1.setBackgroundResource(R.color.C6);
                break;
            case 2:
                visitor_chilrd_td_2.setTextColor(getResources().getColor(R.color.white));
                visitor_chilrd_td_2.setBackgroundResource(R.color.C6);
                break;
            case 3:
                visitor_chilrd_td_3.setTextColor(getResources().getColor(R.color.white));
                visitor_chilrd_td_3.setBackgroundResource(R.color.C6);
                break;
            case 4:
                visitor_chilrd_td_4.setTextColor(getResources().getColor(R.color.white));
                visitor_chilrd_td_4.setBackgroundResource(R.color.C6);
                break;
            case 5:
                visitor_chilrd_td_5.setTextColor(getResources().getColor(R.color.white));
                visitor_chilrd_td_5.setBackgroundResource(R.color.C6);
                break;

            default:
                break;
        }
    }

    /*
     * 清除商机状态的色块
     */
    public void SetColorBussinesYes(int key) {
        ClearColorBussines();
        switch (key) {
            case 1:
                visitor_chilrd_1.setTextColor(getResources().getColor(R.color.white));
                visitor_chilrd_1.setBackgroundResource(R.color.bule_go);
                bstatus = "1";
                break;
            case 2:
                visitor_chilrd_2.setTextColor(getResources().getColor(R.color.white));
                visitor_chilrd_2.setBackgroundResource(R.color.bule_go);
                bstatus = "2";
                break;
            case 3:
                visitor_chilrd_3.setTextColor(getResources().getColor(R.color.white));
                visitor_chilrd_3.setBackgroundResource(R.color.bule_go);
                bstatus = "3";
                break;
            case 4:
                visitor_chilrd_4.setTextColor(getResources().getColor(R.color.white));
                visitor_chilrd_4.setBackgroundResource(R.color.bule_go);
                bstatus = "4";
                break;
            case 5:
                visitor_chilrd_5.setTextColor(getResources().getColor(R.color.white));
                visitor_chilrd_5.setBackgroundResource(R.color.bule_go);
                bstatus = "5";
                break;
            case 6:
                visitor_chilrd_6.setTextColor(getResources().getColor(R.color.white));
                visitor_chilrd_6.setBackgroundResource(R.color.bule_go);
                bstatus = "6";
                break;

            default:
                break;
        }
    }

    // 根据商机当前状态来判断是否可以点击其他项目

    public boolean checkOnBussinesView() {

        if (bstatuskey.equals("5")) {
            CustomToast.showShortToast(mContext, "赢单商机无法更改状态");
            return false;
        }
        if (bstatuskey.equals("6")) {
            CustomToast.showShortToast(mContext, "输单商机无法更改状态");
            return false;
        }
        return true;

    }

    @Override
    public void onDestroy() {
        if (null != mRecordUtil) {
            mRecordUtil.removeView();
        }
        super.onDestroy();
    }

    // 如果是重新选择了客户 需要清除内容
    public void clearAllTextValue() {
        vIpsid = "";// 联系人id
        visitor_selectperson.setText("");
        // 清除关联商机和关联合同的情况
        bid = "";
        bstatus = "";
        hid = "";
        bs_td_name.setText("");
        popupWindows = new PopupWindows(mContext, visitor_edit_info);
    }

}
