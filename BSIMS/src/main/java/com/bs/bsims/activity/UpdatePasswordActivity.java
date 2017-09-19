
package com.bs.bsims.activity;

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
import com.bs.bsims.utils.MD5;
import com.bs.bsims.view.BSDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

public class UpdatePasswordActivity extends BaseActivity implements OnClickListener {
    private EditText mPassword01, mPassword02, mPassword03;
    private Button mOkBt;
    private TextView mNotifyTv;
    private BSDialog mDialog;

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.update_password, null);
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
        mTitleTv.setText("修改密码");
        mPassword01 = (EditText) findViewById(R.id.password_01);
        mPassword02 = (EditText) findViewById(R.id.password_02);
        mPassword03 = (EditText) findViewById(R.id.password_03);
        mOkBt = (Button) findViewById(R.id.ok_bt);
        mNotifyTv = (TextView) findViewById(R.id.notify_tv);

        if (this.getIntent().hasExtra("first_password_update")) {
            mNotifyTv.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void bindViewsListener() {
        mOkBt.setOnClickListener(this);
        mHeadBackImag.setOnClickListener(this);
    }

    public void upload() {
        CustomDialog.showProgressDialog(this, "正在提交数据...");

        RequestParams params = new RequestParams();

        try {

            params.put("ftoken", BSApplication.getInstance().getmCompany());
            params.put("oldpwd", MD5.Md5(mPassword01.getText().toString().trim()));
            params.put("pwd", MD5.Md5(mPassword02.getText().toString()).trim());
            params.put("conpwd", MD5.Md5(mPassword03.getText().toString().trim()));
            params.put("userid", BSApplication.getInstance().getUserId());
            if (this.getIntent().hasExtra("first_password_update")) {
                params.put("addlogin", "1");
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        String url = BSApplication.getInstance().getHttpTitle() + Constant.UPDATE_PASSWORD;
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                if (!UpdatePasswordActivity.this.getIntent().hasExtra("first_password_update")) {
                    CustomDialog.closeProgressDialog();
                }

            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

                if (!UpdatePasswordActivity.this.getIntent().hasExtra("first_password_update")) {
                    CustomDialog.closeProgressDialog();
                }
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(new String(arg2));
                    String code = (String) jsonObject.get("code");
                    String str = (String) jsonObject.get("retinfo");
                    if (Constant.RESULT_CODE.equals("200")) {
                        CustomToast.showShortToast(UpdatePasswordActivity.this, str);
                        showDialog();
                    } else {
                        CustomToast.showShortToast(UpdatePasswordActivity.this, "提交失败");

                    }
                } catch (Exception e) {
                    if (!UpdatePasswordActivity.this.getIntent().hasExtra("first_password_update")) {
                        CustomDialog.closeProgressDialog();
                    }
                    e.printStackTrace();
                    CustomToast.showShortToast(UpdatePasswordActivity.this, "提交失败");
                }
            }

        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.img_head_back:
                if (this.getIntent().hasExtra("first_password_update")) {
                    Intent intent = new Intent();
                    intent.setClass(this, MainActivity.class);
                    this.startActivity(intent);
                    break;
                }
                this.finish();
                break;

            case R.id.ok_bt:
                if (mPassword01.getText().toString().equals(mPassword02.getText().toString())) {
                    CustomToast.showShortToast(this, "新密码不能与旧密码不一致");
                    return;
                }
                if (!mPassword02.getText().toString().equals(mPassword03.getText().toString())) {
                    CustomToast.showShortToast(this, "新密码与确认密码不一致");
                    return;
                }
                if (mPassword02.getText().length() < 6) {
                    CustomToast.showShortToast(this, "密码至少六位数");
                    return;
                }

                if (mPassword01.getText().length() == 0 || mPassword02.getText().length() == 2 || mPassword03.getText().length() == 0) {
                    CustomToast.showShortToast(this, "不能为空");
                    return;
                }
                upload();
                break;

            default:
                break;
        }

    }

    public void showDialog() {
        View view = View.inflate(this, R.layout.dialog_lv_item, null);
        TextView tv = (TextView) view.findViewById(R.id.textview);
        tv.setText("密码已被修改，请重新登录");
        mDialog = new BSDialog(this, "警告", view, new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent newIntent = new Intent();
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                newIntent.putExtra("hiddenpwd", true);
                newIntent.setClass(UpdatePasswordActivity.this, LoginActivity.class);
                UpdatePasswordActivity.this.startActivity(newIntent);
                UpdatePasswordActivity.this.finish();
                mDialog.dismiss();
            }
        });
        mDialog.show();
        mDialog.setButtonTwoGone(true);
    }
}
