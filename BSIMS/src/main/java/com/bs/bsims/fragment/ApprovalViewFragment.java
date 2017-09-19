
package com.bs.bsims.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bs.bsims.R;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.view.BSDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ApprovalViewFragment extends Fragment implements OnClickListener {

    private static final String TAG = "SelfFragment";
    private Activity mActivity;
    private Button mOk, mCancel, mProvenedBt;
    private String status;
    private String type;
    private String content;
    private String approvalid;
    private View mBottomLayout;
    private BSDialog dialog;
    private int count;
    private String provened;

    public String getProvened() {
        return provened;
    }

    public void setProvened(String provened) {
        this.provened = provened;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getApprovalid() {
        return approvalid;
    }

    public void setApprovalid(String approvalid) {
        this.approvalid = approvalid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static ApprovalViewFragment newInstance() {
        ApprovalViewFragment fragment = new ApprovalViewFragment();

        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_approval_view, container,
                false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        bindListener();
    }

    public void initViews(View view) {

        mOk = (Button) view.findViewById(R.id.approval_bt);
        mCancel = (Button) view.findViewById(R.id.unapproval_bt);
        mBottomLayout = view.findViewById(R.id.bottom_layout);
        mProvenedBt = (Button) view.findViewById(R.id.provened_bt);
        if ("1".equals(getProvened())) {
            mCancel.setVisibility(View.GONE);
            mOk.setText("证  明");
        }
        // else {
        // mProvenedBt.setVisibility(View.GONE);
        // }
    }

    public void bindListener() {
        mOk.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        mProvenedBt.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void showDialog() {

        View v = LayoutInflater.from(mActivity).inflate(R.layout.dialog_edittext, null);
        final EditText textView = (EditText) v.findViewById(R.id.edit_content);
        dialog = new BSDialog(mActivity, "请输入内容", v, new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                upload(textView.getText().toString());
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.approval_bt:
                if ("1".equals(getProvened())) {
                    upload("");
                } else {
                    setStatus("1");
                    showDialog();
                }

                break;
            case R.id.unapproval_bt:
                setStatus("2");
                showDialog();
                break;
            case R.id.provened_bt:

                break;
            default:
                break;
        }
    }

    public boolean upload(String content) {
        RequestParams params = new RequestParams();
        CustomDialog.showProgressDialog(mActivity);
        try {
            params.put("ftoken", BSApplication.getInstance().getmCompany());
            params.put("uid", BSApplication.getInstance().getUserId());
            params.put("status", getStatus());
            params.put("type", getType());
            params.put("content", content);
            params.put("approvalid", getApprovalid());
            params.put("alid", getApprovalid());

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        String url = "";
        if ("1".equals(getProvened())) {
            url = BSApplication.getInstance().getHttpTitle() + Constant.PROVENED_URL;
        } else {
            url = BSApplication.getInstance().getHttpTitle() + Constant.APPROVAL_IDEA_COMMIT_URL;
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
                    if (Constant.RESULT_CODE.equals(code)) {
                        mBottomLayout.setVisibility(View.GONE);
                        sendHomeMsg();
                        if ("1".equals(getProvened())) {
                            CustomToast.showLongToast(mActivity, "证明成功");
                        } else {
                            CustomToast.showLongToast(mActivity, "审核成功");
                        }
                        mActivity.finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    CustomDialog.closeProgressDialog();
                }
            }

        });
        return true;
    }

    public void sendHomeMsg() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("approvalid", getApprovalid());
        map.put("status", getStatus());
        map.put("count", getCount() + "");
        CommonUtils.sendBroadcast(mActivity, Constant.HOME_MSG, map);
    }

}
