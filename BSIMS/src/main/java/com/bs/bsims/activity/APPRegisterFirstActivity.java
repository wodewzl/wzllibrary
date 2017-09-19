/**
 * 
 */

package com.bs.bsims.activity;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.ContactUtils.RegexUtils;
import com.bs.bsims.xutils.impl.HttpUtilsByPC;
import com.bs.bsims.xutils.impl.RequestCallBackPC;

import org.json.JSONObject;
import org.xutils.ex.HttpException;

import java.util.HashMap;
import java.util.Map;

/**
 * BS北盛最帅程序员 Copyright (c) 2016 湖北北盛科技有限公司
 * 
 * @author 梁骚侠
 * @date 2016-8-30
 * @version 2.0
 */
public class APPRegisterFirstActivity extends BaseActivity {

    private EditText mUserPhone, mUserCode;
    private TextView mGetPhoneCode;
    private TimeCount time;
    private Context mContext;

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.register_getphone, null);
        mContentLayout.addView(layout);
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
        mTitleTv.setText("注册");
        mUserPhone = (EditText) findViewById(R.id.user_phone);
        mUserCode = (EditText) findViewById(R.id.user_code);
        mGetPhoneCode = (TextView) findViewById(R.id.no_read_tv);
        time = new TimeCount(60000, 1000);
        mContext =this;
    }

    @Override
    public void bindViewsListener() {
        mGetPhoneCode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RegexUtils.isMoblieNo(mUserPhone.getText().toString().trim())) {
                    PostGetCode(mUserPhone.getText().toString().trim());
                }
                else{
                    CustomToast.showShortToast(mContext, "电话号码输入不正确");
                }

            }
        });
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

    // 获取验证码
    public void PostGetCode(String phone) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        new HttpUtilsByPC().sendPostBYPC(Constant.APPREGISTERGETCODE, map,
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

    // 下一步
    public void PostLoginTwo(final String phone, String code) {
        Map<String, String> paramsMap1 = new HashMap<String, String>();
        paramsMap1.put("phone", phone);
        paramsMap1.put("code", code);
        new HttpUtilsByPC().sendPostBYPC(Constant.APPREGISTERPOSTPHONECODE, paramsMap1,
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
                        CustomDialog.closeProgressDialog();
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(new String(rstr.result.toString()));
                            String str = (String) jsonObject.get("retinfo");
                            String code = (String) jsonObject.get("code");
                            String regid = (String) jsonObject.get("regid");
                            String domain_suffix = (String) jsonObject.get("domain_suffix");
                            if (Constant.RESULT_CODE.equals(code)) {
                                Intent i = new Intent(APPRegisterFirstActivity.this, APPRegisterEndActivity.class);
                                i.putExtra("regid", regid);
                                i.putExtra("domain_suffix", domain_suffix);
                                i.putExtra("phone", phone);
                                mContext.startActivity(i);
                                APPRegisterFirstActivity.this.finish();
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

    public void nextGetInfo(View view) {
        String userPhone = mUserPhone.getText().toString().trim();
        String userCode = mUserCode.getText().toString().trim();
        if (RegexUtils.isMoblieNo(userPhone) && !userCode.equals("")) {
            CustomDialog.showProgressDialog(mContext, "验证中...");
            PostLoginTwo(userPhone, userCode);
        }
        else{
            CustomToast.showShortToast(mContext, "电话号码输入不正确");
        }

    }
}
