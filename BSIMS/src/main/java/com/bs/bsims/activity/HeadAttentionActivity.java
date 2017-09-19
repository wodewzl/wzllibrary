
package com.bs.bsims.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.HeadAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.model.JournalDetailResltVO;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.HttpClientUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HeadAttentionActivity extends BaseActivity implements OnClickListener {
    private GridView mHeadGridView;
    private HeadAdapter mHeadAdapter;
    private JournalDetailResltVO mJournalDetailVO;
    private int mType = 0;
    private EmployeeVO mEmployeeVO;
    private static final int ADD_INFORM_PERSON = 10;
    // 添加员工使用
    protected List<EmployeeVO> mDataList = new ArrayList<EmployeeVO>();
    private StringBuffer mAttentionSb = new StringBuffer();

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.head_attention, mContentLayout);
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    public boolean getData() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());

            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.ATTENTION_USERS, map);
            mEmployeeVO = gson.fromJson(jsonStrList, EmployeeVO.class);
            if (Constant.RESULT_CODE.equals(mEmployeeVO.getCode())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
        }
    }

    @Override
    public void updateUi() {

    }

    @Override
    public void executeSuccess() {
        super.executeSuccess();
        mHeadAdapter.updateData(mEmployeeVO.getArray());
    }

    @Override
    public void executeFailure() {
        super.executeSuccess();
        // mHeadAdapter.updateData(mEmployeeVO.getArray());
    }

    @Override
    public void bindViewsListener() {
        mHeadBackImag.setOnClickListener(this);
        mOkTv.setOnClickListener(this);
    }

    @Override
    public void initView() {
        mTitleTv.setText("管理关注");
        mOkTv.setText("确定");
        mHeadGridView = (GridView) findViewById(R.id.send_second_person_gv);
        // mHeadAdapter = new HeadAdapter(this, false);
        mHeadAdapter = new HeadAdapter(this, true, "2");
        mHeadGridView.setAdapter(mHeadAdapter);
        // initData();
    }

    public void initData() {
        List<EmployeeVO> list = (List<EmployeeVO>) this.getIntent().getSerializableExtra("head_list");
        mHeadAdapter.updateData(list);
        String title = (String) this.getIntent().getSerializableExtra("title");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_head_back:
                this.finish();
                break;
            case R.id.txt_head_left_back:
                this.finish();
                break;

            case R.id.txt_comm_head_right:
                mAttentionSb.setLength(0);
                for (int i = 0; i < mHeadAdapter.mList.size(); i++) {
                    mAttentionSb.append(mHeadAdapter.mList.get(i).getUserid());
                    if (i != mDataList.size() - 1) {
                        mAttentionSb.append(",");
                    }
                }

                commit();
                break;

            default:
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ADD_INFORM_PERSON:
                if (requestCode == 10) {
                    if (data == null)
                        return;
                    mDataList.clear();
                    mDataList = (List<EmployeeVO>) data.getSerializableExtra("checkboxlist");
                    mHeadAdapter.mList.clear();
                    // mHeadAdapter.mList.addAll(mEmployeeVO.getArray());

                    // for (int i = 0; i < mEmployeeVO.getArray().size(); i++) {
                    // for (int j = 0; j < mDataList.size(); j++) {
                    // if
                    // (mDataList.get(j).getUserid().equals(mEmployeeVO.getArray().get(i).getUserid()))
                    // {
                    // mDataList.remove(j);
                    // }
                    // }
                    // }
                    mHeadAdapter.mList.addAll(mDataList);
                    mHeadAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    public void commit() {
        CustomDialog.showProgressDialog(this, "正在提交数据...");
        RequestParams params = new RequestParams();

        try {
            params.put("ftoken", BSApplication.getInstance().getmCompany());
            params.put("userid", BSApplication.getInstance().getUserId());
            params.put("favoruids", mAttentionSb.toString());
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        String url = BSApplication.getInstance().getHttpTitle() + Constant.ATTENTION_ADD_USERS;
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
                        Intent intent = new Intent();
                        HeadAttentionActivity.this.setResult(1, intent);
                        HeadAttentionActivity.this.finish();
                        CustomToast.showShortToast(HeadAttentionActivity.this, str);
                    } else {
                        CustomToast.showShortToast(HeadAttentionActivity.this, str);
                    }
                    CustomDialog.closeProgressDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
