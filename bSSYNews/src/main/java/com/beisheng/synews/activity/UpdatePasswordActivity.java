
package com.beisheng.synews.activity;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.utils.BaseCommonUtils;
import com.bs.bsims.observer.MessageMyHeadImp;
import com.beisheng.synews.application.AppApplication;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.mode.UserInfoVO;
import com.im.zhsy.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class UpdatePasswordActivity extends BaseActivity implements OnClickListener {
    private LinearLayout mContentLayout;
    private EditText mPasswordEt, mPasswordComfirEt, mEmailEt, mOldPasswordEt;
    private Button mRegisteredBt;
    private boolean mCommitFlag = true;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.update_password_activity, mBaseContentLayout);
    }

    @Override
    public boolean getDataResult() {
        return true;
    }

    @SuppressLint("NewApi")
    @Override
    public void initView() {
        mBaseTitleTv.setText("修改密码");
        mContentLayout = (LinearLayout) findViewById(R.id.content_layout);
        mPasswordComfirEt = (EditText) findViewById(R.id.password_comfir_et);
        mPasswordEt = (EditText) findViewById(R.id.password_et);
        mOldPasswordEt = (EditText) findViewById(R.id.old_password_et);
        mEmailEt = (EditText) findViewById(R.id.email_et);
        mRegisteredBt = (Button) findViewById(R.id.registered_bt);
        mRegisteredBt.setText("立即修改");
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

                if (AppApplication.getInstance().getUserInfoVO() == null) {
                    this.openActivity(LoginActivity.class);
                    // showCustomToast("亲，请先登录哦");
                    return;
                }
                if (mOldPasswordEt.getText().toString().trim().length() == 0) {
                    showCustomToast("请输入原始密码");
                    return;
                }

                if (mPasswordComfirEt.getText().toString().trim().length() == 0) {
                    showCustomToast("请输入新密码");
                    return;
                }

                if (mPasswordEt.getText().toString().trim().length() == 0) {
                    showCustomToast("请确认密码");
                    return;
                }

                if (!mPasswordComfirEt.getText().toString().trim().equals(mPasswordEt.getText().toString().trim())) {
                    showCustomToast("两次密码不一样，请重新输入");
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
            params.put("email", AppApplication.getInstance().getUserInfoVO().getEmail());
            params.put("username", AppApplication.getInstance().getUserInfoVO().getUsername());
            params.put("password", mPasswordEt.getText().toString());
            params.put("oldpwd", mOldPasswordEt.getText().toString());
            params.put("token", AppApplication.getInstance().getUserInfoVO().getToken());

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        String url = Constant.DOMAIN_NAME + Constant.UPDATE_PASSWORD_URL;
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                mCommitFlag = true;
                dismissProgressDialog();
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                dismissProgressDialog();
                mCommitFlag = true;
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(new String(arg2));
                    final String str = (String) jsonObject.get("retinfo");
                    String code = (String) jsonObject.get("code");
                    if (Constant.RESULT_SUCCESS_CODE.equals(code)) {

                        UserInfoVO vo = AppApplication.getInstance().getUserInfoVO();
                        vo.setToken((String) jsonObject.get("token"));
                        vo.setSessionid((String) jsonObject.get("sessionid"));
                        AppApplication.getInstance().saveUserInfoVO(vo);
                        MessageMyHeadImp.getInstance().notifyWatcher(vo);
                        mBaseContentLayout.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showCustomToast(str);
                                UpdatePasswordActivity.this.finish();
                            }
                        }, 1000);
                    } else {
                        showCustomToast(str);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
