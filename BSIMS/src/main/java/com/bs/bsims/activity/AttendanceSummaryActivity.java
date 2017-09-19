
package com.bs.bsims.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.fragment.ContactsFragment;
import com.bs.bsims.interfaces.UpdateCallback;
import com.bs.bsims.model.ResultVO;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.UrlUtil;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AttendanceSummaryActivity extends FragmentActivity implements UpdateCallback {
    private ResultVO mResultVO;
    private TextView mTitleTv, mOkTv, mBackTv;
    private ContactsFragment mContactsFragment;
    private TextView mLoadingTv;
    private LinearLayout mAddFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_summary);
        // initData();
        initView();
    }

    public void initView() {
        // mTitleTv = (TextView) findViewById(R.id.txt_comm_head_activityName);
        // mOkTv = (TextView) findViewById(R.id.txt_comm_head_right);
        // mBackTv = (TextView) findViewById(R.id.txt_head_left_back);
        // mTitleTv.setText("选择抄送人");

        mContactsFragment = new ContactsFragment(ContactsFragment.ATTENDANCE);
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.add_fragment, mContactsFragment);
        transaction.commitAllowingStateLoss();
        // contactsFragment.setTitle("考勤总汇");
        // contactsFragment.getView().findViewById(R.id.contact_title);
        // LinearLayout layout = (LinearLayout)
        // contactsFragment.getView().findViewById(R.id.contact_title);
        // layout.setVisibility(View.GONE);
        mLoadingTv = (TextView) findViewById(R.id.loading);
        mAddFragment = (LinearLayout) findViewById(R.id.add_fragment);
    }

    public void initData() {
        getData();
    }

    public boolean getData() {
        try {
            Gson gson = new Gson();
            // String jsonUrlStr = HttpClientUtil.get(Constant.USER_INFO, Constant.ENCODING).trim();
            String urlStr = UrlUtil.getAttendanceUrl(Constant.ATTENDANCE_SUMMARY_USER,
                    Constant.COMPANY, "");
            String jsonUrlStr = HttpClientUtil.get(urlStr, Constant.ENCODING).trim();
            mResultVO = gson.fromJson(jsonUrlStr, ResultVO.class);
            // String jsonUrlStr = getFromAssets("json.txt");

            mResultVO = gson.fromJson(jsonUrlStr, ResultVO.class);
            if (Constant.RESULT_CODE.equals(mResultVO.getCode())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getFromAssets(String name) {
        try {
            InputStreamReader reader = new InputStreamReader(getResources().getAssets().open(name));
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = "";
            String result = "";
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
            return result;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public boolean execute() {
        return true;
    }

    @Override
    public void executeSuccess() {
        // mLoadingTv.setVisibility(View.GONE);
        // mAddFragment.setVisibility(View.VISIBLE);
    }

    @Override
    public void executeFailure() {

    }

}
