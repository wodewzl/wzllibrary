
package com.beisheng.synews.activity;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.synews.application.AppApplication;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.mode.UserInfoVO;
import com.bs.bsims.observer.MessageMyHeadImp;
import com.google.gson.Gson;
import com.im.zhsy.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

public class RegisteredActivtiy extends BaseActivity implements OnClickListener {
    private LinearLayout mContentLayout;
    private EditText mUserEt, mPasswordEt, mEmailEt, mPhoneEt;
    private Button mRegisteredBt;
    private boolean mCommitFlag = true;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.registered_activity, mBaseContentLayout);
    }

    @Override
    public boolean getDataResult() {
        return true;
    }

    @SuppressLint("NewApi")
    @Override
    public void initView() {
        mBaseTitleTv.setText("注册");
        mContentLayout = (LinearLayout) findViewById(R.id.content_layout);
        mUserEt = (EditText) findViewById(R.id.user_et);
        mPasswordEt = (EditText) findViewById(R.id.password_et);
        mEmailEt = (EditText) findViewById(R.id.email_et);
        mRegisteredBt = (Button) findViewById(R.id.registered_bt);
        mPhoneEt = (EditText) findViewById(R.id.phone_et);

        mContentLayout.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.devider_bg, R.color.C1));
        mRegisteredBt.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.sy_title_color, R.color.sy_title_color));
    }

    @Override
    public void bindViewsListener() {
        mRegisteredBt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // 初始话对话框
        switch (v.getId()) {
            case R.id.registered_bt:
                if (mUserEt.getText().toString().trim().length() == 0) {
                    showCustomToast("请输入帐号");
                    return;
                }

                if (mPasswordEt.getText().toString().trim().length() == 0) {
                    showCustomToast("请输入密码");
                    return;
                }

                if (mEmailEt.getText().toString().trim().length() == 0) {
                    showCustomToast("请输入邮箱");
                    return;
                }

                commit();

                break;

            default:
                break;
        }
    }

    public void commit() {
        mCommitFlag = false;
        showProgressDialog();
        RequestParams params = new RequestParams();
        try {
            params.put("useracc", mUserEt.getText().toString());
            params.put("userpw", mPasswordEt.getText().toString());
            params.put("email", mEmailEt.getText().toString());
            params.put("phone", mPhoneEt.getText().toString());

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        String url = Constant.DOMAIN_NAME + Constant.REGISTER_URL;
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                mCommitFlag = true;
                dismissProgressDialog();
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                String result = new String(arg2);
                dismissProgressDialog();
                mCommitFlag = true;
                JSONObject jsonObject;
                try {
                    Gson gson = new Gson();
                    UserInfoVO vo = gson.fromJson(result, UserInfoVO.class);
                    if (Constant.RESULT_SUCCESS_CODE.equals(vo.getCode())) {
                        AppApplication.getInstance().saveUserInfoVO(vo);
                        MessageMyHeadImp.getInstance().notifyWatcher(vo);
                        RegisteredActivtiy.this.finish();
                    } else {
                        showCustomToast(vo.getRetinfo());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
