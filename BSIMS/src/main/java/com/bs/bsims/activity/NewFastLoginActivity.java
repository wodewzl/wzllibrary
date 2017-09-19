/**
 * 
 */

package com.bs.bsims.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.Login4GetHostVO;
import com.bs.bsims.model.LoginUser;
import com.bs.bsims.model.UserFromServerVO;
import com.bs.bsims.utils.ContactUtils.RegexUtils;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.UserManager;
import com.bs.bsims.xutils.impl.HttpUtilsByPC;
import com.bs.bsims.xutils.impl.RequestCallBackPC;
import com.bs.bsims.xutils.impl.RequestCallBackPC.ResponseInfo;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.xutils.ex.HttpException;

import java.util.HashMap;
import java.util.Map;

/**
 * BS北盛最帅程序员 Copyright (c) 2016 湖北北盛科技有限公司
 * 
 * @author 梁骚侠
 * @date 2016-3-10
 * @version 1.23 快速体验登陆页面
 */
public class NewFastLoginActivity extends BaseActivity implements OnClickListener {

    private final String GETCODEPATH = "http://tiyan.bqixing.com/api.php/Login/getCode";
    // phone/18086132936/code/161380

    private EditText mUserPhone, mUserCode, mIsPhone;
    private TextView mGetPhoneCode;
    private Button mLButton;
    private TimeCount time;
    private TextView mOneTable, mTwoTable;
    private LinearLayout mAllLinearLayout, mCodeLayout, mIsPhoneLayout;
    private View mView;
    private Context mContext;
    private int mTypeKey = 0;
    private String mCodePhone, mCode;
    private String isPhone;
    private LoginUser user;
    protected SharedPreferences mSettings;

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.fast_experience_login, null);
        mContentLayout.addView(layout);
        mContext = this;
    }

    @Override
    public boolean getDataResult() {
        return true;
    }

    @Override
    public void updateUi() {

    }

    @Override
    public void initView() {
        mTitleTv.setText("快速体验");
        mUserPhone = (EditText) findViewById(R.id.user_phone);
        mUserCode = (EditText) findViewById(R.id.user_code);
        mGetPhoneCode = (TextView) findViewById(R.id.no_read_tv);
        mLButton = (Button) findViewById(R.id.login);
        mOneTable = (TextView) findViewById(R.id.detailinfo);
        mTwoTable = (TextView) findViewById(R.id.trade_dongtai);
        mAllLinearLayout = (LinearLayout) findViewById(R.id.text_all);
        mCodeLayout = (LinearLayout) findViewById(R.id.allcode_ly);
        mView = (View) findViewById(R.id.view);
        mIsPhoneLayout = (LinearLayout) findViewById(R.id.is_phone);
        mIsPhone = (EditText) findViewById(R.id.is_phone_text);
        time = new TimeCount(60000, 1000);
        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    public void bindViewsListener() {
        mGetPhoneCode.setOnClickListener(this);
        mOneTable.setOnClickListener(this);
        mTwoTable.setOnClickListener(this);
        mLButton.setOnClickListener(this);
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            mGetPhoneCode.setText("获取验证码");
            mGetPhoneCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.frame_shixing_yellow));
            mGetPhoneCode.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            mGetPhoneCode.setClickable(false);// 防止重复点击
            mGetPhoneCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.frame_shixing_gray));
            mGetPhoneCode.setText("(" + millisUntilFinished / 1000 + "s重新获取)");
        }
    }

    /*
     * (non-Javadoc)
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.no_read_tv:
                if (RegexUtils.isMoblieNo(mUserPhone.getText().toString().trim())) {
                    PostGetCode(mUserPhone.getText().toString().trim());
                }
                else {
                    CustomToast.showShortToast(getApplicationContext(), "您输入的电话号码不正确");
                }
                break;
            case R.id.detailinfo:
                tableSelect(1);
                break;
            case R.id.trade_dongtai:
                tableSelect(2);
                break;
            case R.id.login:
                switch (mTypeKey) {
                    case 0:
                        mCodePhone = mUserPhone.getText().toString().trim();
                        mCode = mUserCode.getText().toString().trim();
                        if (null != mCodePhone && !mCodePhone.equals("") && null != mCode && !mCode.equals("")) {
                            CustomDialog.showProgressDialog(mContext, "正在登陆");
                            PostLoginTwo(mCodePhone, mCode);
                        }
                        else {
                            CustomToast.showShortToast(mContext, "非法输入");
                        }
                        break;
                    case 1:
                        isPhone = mIsPhone.getText().toString().trim();
                        if (isPhone != null && !isPhone.equals("")) {
                            if (RegexUtils.isMoblieNo(isPhone))
                                PostLoginOne(isPhone);
                        }
                        else {
                            CustomToast.showShortToast(mContext, "您输入的电话号码不正确");
                        }

                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }

    }

    // 获取验证码
    public void PostGetCode(String phone) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        new HttpUtilsByPC().sendPostBYPC(GETCODEPATH, map,
                /**
                 * @author Administrator
                 */
                new RequestCallBackPC() {
                    @Override
                    public void onFailurePC(HttpException arg0, String arg1) {
                        // TODO Auto-generated method stub
                        CustomToast.showShortToast(getApplicationContext(), "请求失败");
                    }

                    @Override
                    public void onSuccessPC(ResponseInfo rstr) {
                        // TODO Auto-generated method stub
                        // editKey1 = "0";// 假设定 提高体验不刷新数据
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(new String(rstr.result.toString()));
                            String str = (String) jsonObject.get("retinfo");
                            String code = (String) jsonObject.get("code");
                            if (Constant.RESULT_CODE.equals(code)) {
                                time.start();// 开始计时
                            }
                            else {
                                CustomToast.showShortToast(mContext, str);
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                });
    }

    // 快速登陆
    public void PostLoginOne(String phone) {
        CustomDialog.showProgressDialog(mContext, "正在登陆");
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("phone", phone);
        new HttpUtilsByPC().sendPostBYPC(Constant.LOGINFAST, paramsMap,
                /**
                 * @author Administrator
                 */
                new RequestCallBackPC() {
                    @Override
                    public void onFailurePC(HttpException arg0, String arg1) {
                        // TODO Auto-generated method stub
                        CustomDialog.closeProgressDialog();
                        CustomToast.showShortToast(getApplicationContext(), "请求失败");
                    }

                    @Override
                    public void onSuccessPC(ResponseInfo rstr) {
                        // TODO Auto-generated method stub
                        CustomDialog.closeProgressDialog();
                        parseJosonLogin(rstr);
                    }

                });
    }

    // 快速登陆
    public void PostLoginTwo(String phone, String code) {
        Map<String, String> paramsMap1 = new HashMap<String, String>();
        paramsMap1.put("phone", phone);
        paramsMap1.put("code", code);
        String path = new String();
        new HttpUtilsByPC().sendPostBYPC(Constant.LOGINFAST, paramsMap1,
                /**
                 * @author Administrator
                 */
                new RequestCallBackPC() {
                    @Override
                    public void onFailurePC(HttpException arg0, String arg1) {
                        // TODO Auto-generated method stub
                        CustomDialog.closeProgressDialog();
                        CustomToast.showShortToast(getApplicationContext(), "请求失败");
                    }

                    @Override
                    public void onSuccessPC(ResponseInfo rstr) {
                        // TODO Auto-generated method stub
                        CustomDialog.closeProgressDialog();
                        // editKey1 = "0";// 假设定 提高体验不刷新数据
                        parseJosonLogin(rstr);
                    }

                });
    }

    public void inMainPage() {
        Intent intent = new Intent();
        intent.putExtra("isFastLogin", 1);// 证明是体验登陆的
        intent.setClass(this, MainActivity.class);
        this.startActivity(intent);
        this.finish();
    }

    /**
     * 选项卡切换
     **/
    public void tableSelect(int key) {
        switch (key) {
            case 1:
                /* 第二还原 */
                mTwoTable.setTextColor(getResources().getColor(R.color.bule_go));
                mTwoTable.setBackgroundResource(R.drawable.corners_tab_right_normal);
                /* 第一变色 */
                mOneTable.setTextColor(getResources().getColor(R.color.white));
                mOneTable.setBackgroundResource(R.drawable.corners_tab_left_select);
                break;
            case 2:
                /* 第二变色 */
                mTwoTable.setTextColor(getResources().getColor(R.color.white));
                mTwoTable.setBackgroundResource(R.drawable.corners_tab_right_select);
                /* 第一还原 */
                mOneTable.setTextColor(getResources().getColor(R.color.bule_go));
                mOneTable.setBackgroundResource(R.drawable.corners_tab_left_normal);
                break;
        }

        setSelect(key);

    }

    public void saveData(String userid, String company, String httpTitle) {
        SharedPreferences preference = getSharedPreferences("user",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putString("userid", userid);
        editor.putString("company", company);
        editor.putString("http_title", httpTitle);
        editor.commit();
    }

    /**
     * @param key
     */
    private void setSelect(int key) {
        // TODO Auto-generated method stub
        switch (key) {
            case 1:
                mTypeKey = 0;
                mAllLinearLayout.setVisibility(View.VISIBLE);
                mIsPhoneLayout.setVisibility(View.GONE);
                break;
            case 2:
                mTypeKey = 1;
                mAllLinearLayout.setVisibility(View.GONE);
                mIsPhoneLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void parseJosonLogin(ResponseInfo rstr) {
        Gson gson = new Gson();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(new String(rstr.result.toString()));
            String str = (String) jsonObject.get("retinfo");
            String code = (String) jsonObject.get("code");
            if (Constant.RESULT_CODE.equals(code)) {
                String remind = null;
                user = gson.fromJson(rstr.result.toString(), LoginUser.class).getArray();
                // 保存用户信息
                UserManager.saveLoginInfo(mSettings, user);
                UserFromServerVO userFromServerVO = new UserFromServerVO();
                userFromServerVO = gson.fromJson(jsonObject.getJSONObject("array").toString(), UserFromServerVO.class);

                BSApplication.getInstance().setUserFromServerVO(userFromServerVO);
                String managementStatus = user.getManagement();

                Login4GetHostVO login4GetHostVO = new Login4GetHostVO();
                login4GetHostVO.setFtoken(userFromServerVO.getFtoken());
                login4GetHostVO.setIsinpost(userFromServerVO.getIsinpost());
                login4GetHostVO.setIslogin(userFromServerVO.getIslogin());
                login4GetHostVO.setSiteurl(userFromServerVO.getSiteurl());
                login4GetHostVO.setUserid(userFromServerVO.getUserid());

                // BSApplication.getInstance().setLogin4GetHostVO(login4GetHostVO);
                BSApplication.getInstance().setUserId(login4GetHostVO.getUserid());
                BSApplication.getInstance().setmCompany(login4GetHostVO.getFtoken());
                BSApplication.getInstance().setHttpTitle(login4GetHostVO.getSiteurl());
                Constant.LOGIN_MY_FTOKEN = login4GetHostVO.getFtoken();
                Constant.LOGIN_MY_HOST = login4GetHostVO.getSiteurl();

                saveData(login4GetHostVO.getUserid(), login4GetHostVO.getFtoken(),
                        login4GetHostVO.getSiteurl());

                /**
                 * 是否管理层，只有两个值1是管理层
                 */
                if ("0".equalsIgnoreCase(managementStatus)) {
                    Constant.WORKFRAGMENT = "WorkGeneralFragment";
                } else {
                    Constant.WORKFRAGMENT = "WorkBossFragment";
                }
                inMainPage();
            }
            else {
                CustomToast.showShortToast(mContext, str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
