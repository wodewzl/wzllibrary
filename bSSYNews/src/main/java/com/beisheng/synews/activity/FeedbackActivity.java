
package com.beisheng.synews.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.synews.application.AppApplication;
import com.beisheng.synews.constant.Constant;
import com.im.zhsy.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

@SuppressLint("NewApi")
public class FeedbackActivity extends BaseActivity implements OnClickListener, TextWatcher {
    private boolean mCommitFlag = true;
    private TextView mCountTv, mCommitTv;
    private EditText mContentEt, mContactEt;
    private LinearLayout mContentLayout;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.feedback_activity, mBaseContentLayout);
    }

    @Override
    public boolean getDataResult() {
        return true;
    }

    @Override
    public void initView() {
        mBaseTitleTv.setText("意见反馈");
        mCountTv = (TextView) findViewById(R.id.count_tv);
        mCommitTv = (TextView) findViewById(R.id.commit_tv);
        mContentEt = (EditText) findViewById(R.id.content_et);
        mContactEt = (EditText) findViewById(R.id.contact_et);
        mContentLayout = (LinearLayout) findViewById(R.id.content_layout);
        mContactEt.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.devider_bg, R.color.C1));
        mContentLayout.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.devider_bg, R.color.C1));
        mCommitTv.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.sy_title_color, R.color.sy_title_color));
    }

    @Override
    public void bindViewsListener() {
        mContentEt.addTextChangedListener(this);
        mCommitTv.setOnClickListener(this);
    }

    public void commit() {
        mCommitFlag = false;
        showProgressDialog();
        RequestParams params = new RequestParams();
        try {
            params.put("email", mContactEt.getText().toString());
            params.put("content", mContentEt.getText().toString());
            params.put("uid", AppApplication.getInstance().getUid());
            params.put("sessionid", AppApplication.getInstance().getSessionid());
            params.put("phone", android.os.Build.MODEL);
            TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String imei = TelephonyMgr.getDeviceId();
            params.put("mobile", imei);
            params.put("platform", "android");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        String url = Constant.DOMAIN_NAME + Constant.IDEA_FEEDBACK_URL;
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
                try {
                    JSONObject jsonObject = new JSONObject(new String(arg2));
                    String str = (String) jsonObject.get("retinfo");
                    String code = (String) jsonObject.get("code");
                    showCustomToast(str);
                    mContentEt.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            FeedbackActivity.this.finish();
                        }
                    }, 1000);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View arg0) {
        if (mContentEt.getText().toString().trim().length() == 0) {
            showCustomToast("请填写您的宝贵意见");
            return;
        }
        commit();
    }

    @Override
    public void afterTextChanged(Editable arg0) {
        mCountTv.setText(mContentEt.getText().toString().length() + "/1000");
    }

    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

    }

    @Override
    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

    }

}
