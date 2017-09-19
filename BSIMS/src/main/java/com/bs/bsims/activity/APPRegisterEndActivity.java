/**
 * 
 */

package com.bs.bsims.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.ContactUtils.RegexUtils;
import com.bs.bsims.view.BSImgDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * BS北盛最帅程序员 Copyright (c) 2016 湖北北盛科技有限公司
 * 
 * @author 梁骚侠
 * @date 2016-8-30
 * @version 2.0
 */
public class APPRegisterEndActivity extends BaseActivity implements OnClickListener {
    private Context mContext;

    private EditText mUserPhone, mUserPwd, mUserRpwd, mCname, mUserName, mUserWork, mUserCom;
    private Button mRegisterBtn;
    private TextView mSubmitUrlTv;
    public boolean postState = true;// 控制多次提交按钮
    private String mPhoneStr, mPwdStr, mRpwdStr, mCanmeStr, mUsernameStr, mUserWorkStr, mUserComStr;

    private String mRegid, mComEiditStr, mUserPhoneStr;

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.appliaction_register, null);
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
        mUserPhone = (EditText) findViewById(R.id.register_phone);
        mUserPwd = (EditText) findViewById(R.id.register_pwd);
        mUserRpwd = (EditText) findViewById(R.id.register_rpwd);
        mCname = (EditText) findViewById(R.id.register_cname);
        mUserName = (EditText) findViewById(R.id.register_name);
        mUserWork = (EditText) findViewById(R.id.register_work);
        mUserCom = (EditText) findViewById(R.id.register_com);
        mRegisterBtn = (Button) findViewById(R.id.register_bt);
        mSubmitUrlTv = (TextView) findViewById(R.id.submit_url);
        mContext = this;
        initData();
    }

    public void initData() {
        Intent i = getIntent();
        mUserPhoneStr = i.getStringExtra("phone");
        mRegid = i.getStringExtra("regid");
        mSubmitUrlTv.setText("---" + i.getStringExtra("domain_suffix"));
        mUserPhone.setText(mUserPhoneStr);
        mUserPhone.setEnabled(false);
    }

    @Override
    public void bindViewsListener() {
        mRegisterBtn.setOnClickListener(this);
    }

    public void registerInfoPost() {

    }

    public boolean checkInfoPost() {
        mPhoneStr = mUserPhone.getText().toString().trim();
        mPwdStr = mUserPwd.getText().toString().trim();
        mRpwdStr = mUserRpwd.getText().toString().trim();
        mCanmeStr = mCname.getText().toString().trim();
        mUsernameStr = mUserName.getText().toString().trim();
        mUserWorkStr = mUserWork.getText().toString().trim();
        mUserComStr = mUserCom.getText().toString().trim();

        if (!RegexUtils.isMoblieNo(mPhoneStr)) {
            CustomToast.showShortToast(mContext, "电话码号输入有误");
            return false;
        }

        if (mPwdStr.equals("")) {
            return false;
        }

        if (mRpwdStr.equals("")) {
            return false;
        }
        if (!mPwdStr.equals(mRpwdStr)) {
            CustomToast.showShortToast(mContext, "两次密码输入不一致");
            return false;
        }

        if (mCanmeStr.equals("")) {
            CustomToast.showShortToast(mContext, "公司名称不能为空");
            return false;
        }

        if (mUsernameStr.equals("")) {
            CustomToast.showShortToast(mContext, "注册姓名不能为空");
            return false;
        }

        if (mUserWorkStr.equals("")) {
            CustomToast.showShortToast(mContext, "注册职称不能为空");
            return false;
        }
//        if (mUserComStr.equals("")) {
//            CustomToast.showShortToast(mContext, "二级域名不能为空");
//            return false;
//        }

        return true;
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
            params.put("regid", mRegid);
            params.put("phone", mUserPhoneStr);
            params.put("username", mPhoneStr);// 电话
            params.put("password", mPwdStr);
            params.put("company", mCanmeStr);
            params.put("fullname", mUsernameStr);
            params.put("post", mUserWorkStr);
            params.put("domain", mUserComStr);
            params.put("domain_suffix", mSubmitUrlTv.getText());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        // params.put("name", "woshishishi");// 传输的字符数据
        String url = Constant.APPREGISTERPOSTINFO;
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                postState = true;
                CustomDialog.closeProgressDialog();
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
                        BSImgDialog bsImgDialog = new BSImgDialog(mContext, null);
                        bsImgDialog.show();
                        APPRegisterEndActivity.this.finish();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_bt:
                if (checkInfoPost()) {
                    PostInfoBussines();
                }
                break;

            default:
                break;
        }

    }

}
