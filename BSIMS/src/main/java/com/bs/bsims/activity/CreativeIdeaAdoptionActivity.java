
package com.bs.bsims.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bs.bsims.R;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomToast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class CreativeIdeaAdoptionActivity extends BaseActivity implements OnClickListener {
    private Button mApprovalBt, mUnapprovalBt;
    private String mType;
    private String mId;
    private EditText mEditText;
    private String mStatus = "0";
    private String mFlag = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // baseSetContentView();
        //
        // initView();
        // initData();
        // bindViewsListener();
    }

    public void initView() {
        mType = this.getIntent().getStringExtra("type");
        mId = this.getIntent().getStringExtra("id");
        mFlag = this.getIntent().getStringExtra("flag");
        if ("1".equals(mType)) {
            mTitleTv.setText("审核意见");
        } else {
            mTitleTv.setText("采纳意见");
        }
        mApprovalBt = (Button) findViewById(R.id.approval_bt);
        mUnapprovalBt = (Button) findViewById(R.id.unapproval_bt);
        mEditText = (EditText) findViewById(R.id.content);
    }

    public void initData() {

    }

    public void bindViewsListener() {
        mApprovalBt.setOnClickListener(this);
        mUnapprovalBt.setOnClickListener(this);
        mHeadBackImag.setOnClickListener(this);
    }

    @Override
    public boolean getDataResult() {
        return true;
    }

    @Override
    public void updateUi() {

    }

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.idea_view, null);
        mContentLayout.addView(layout);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.approval_bt:
                if (mEditText.getText().length() == 0) {
                    Toast.makeText(this, "请提输入内容", Toast.LENGTH_LONG).show();
                    return;
                }
                upload();
                break;

            case R.id.unapproval_bt:
                intent.putExtra("status", "2");
                this.setResult(1, intent);
                finish();
                break;
            case R.id.img_head_back:
                intent.putExtra("status", "2");
                this.setResult(1, intent);
                finish();
                break;
            default:
                break;
        }
    }

    public void upload() {
        RequestParams params = new RequestParams();

        try {
            params.put("ftoken", BSApplication.getInstance().getmCompany());
            params.put("userid", BSApplication.getInstance().getUserId());
            params.put("accept", mFlag);

            params.put("id", mId);
            params.put("content", mEditText.getText().toString());

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        String url;
        if ("1".equals(mType)) {
            url = BSApplication.getInstance().getHttpTitle() + Constant.CREATIVE_CHECK_URL;

        } else {
            url = BSApplication.getInstance().getHttpTitle() + Constant.CREATIVE_ADOPTION_URL;
        }
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
                    if ("200".equals(code)) {
                        Intent intent = new Intent();
                        intent.putExtra("status", "1");
                        CreativeIdeaAdoptionActivity.this.setResult(1, intent);
                        CreativeIdeaAdoptionActivity.this.finish();

                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("id", mId);
                        map.put("accept", mFlag);
                        CommonUtils.sendBroadcast(CreativeIdeaAdoptionActivity.this, Constant.HOME_MSG, map);
                    } else {
                        mStatus = "0";
                    }
                    CustomToast.showShortToast(CreativeIdeaAdoptionActivity.this, str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.putExtra("status", "2");
            this.setResult(1, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
