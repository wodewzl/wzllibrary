
package com.bs.bsims.activity;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.bs.bsims.R;
import com.bs.bsims.adapter.ApprovalBorrowAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.ApprovalBorrowVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSIndexEditText;
import com.google.gson.Gson;

import java.util.HashMap;

public class ApprovalBorrowActivity extends BaseActivity {
    private ApprovalBorrowVO mApprovalBorrowVO;
    private ListView mListView;
    private ApprovalBorrowAdapter mAdapter;
    private BSIndexEditText mBSBsIndexEditText;
    private String mKeyword;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.approval_borrow, mContentLayout);
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    @Override
    public void updateUi() {

    }

    @Override
    public void initView() {
        mListView = (ListView) findViewById(R.id.lv_refresh);
        mAdapter = new ApprovalBorrowAdapter(this);
        mListView.setAdapter(mAdapter);
        mBSBsIndexEditText = (BSIndexEditText) findViewById(R.id.edit_single_search);
        mTitleTv.setText("关联借支单");
    }

    @Override
    public void bindViewsListener() {
        mBSBsIndexEditText.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) mBSBsIndexEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    ApprovalBorrowActivity.this
                                            .getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);

                    mKeyword = mBSBsIndexEditText.getText().toString();
                    new ThreadUtil(ApprovalBorrowActivity.this, ApprovalBorrowActivity.this).start();
                    return true;
                }
                return false;
            }
        });
    }

    public boolean getData() {
        try {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put("uid", BSApplication.getInstance().getUserId());
            map.put("keyword", mKeyword);
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.APPROVAL_FEEL_BORROW_URL, map);
            Gson gson = new Gson();
            mApprovalBorrowVO = gson.fromJson(jsonStr, ApprovalBorrowVO.class);
            if (Constant.RESULT_CODE.equals(mApprovalBorrowVO.getCode())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void executeSuccess() {
        mAdapter.updateData(mApprovalBorrowVO.getArray());
        mLoading.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
    }

    @Override
    public void executeFailure() {

        mAdapter.notifyDataSetChanged();

        if (mApprovalBorrowVO != null) {
            CommonUtils.setNonetContent(this, mLoading, "没有相关信息~");
            mLoading.setVisibility(View.VISIBLE);
            mContentLayout.setVisibility(View.GONE);
            mLoadingLayout.setVisibility(View.VISIBLE);
        } else {
            CommonUtils.setNonetIcon(this, mLoading, this);
        }
    }

}
