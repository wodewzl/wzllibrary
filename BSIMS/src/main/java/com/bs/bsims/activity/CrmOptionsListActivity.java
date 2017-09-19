
package com.bs.bsims.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.CrmOptionsAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.CrmOptionsListVO;
import com.bs.bsims.utils.CharacterParser;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.view.BSIndexEditText;
import com.bs.bsims.view.BSRefreshListView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CrmOptionsListActivity extends BaseActivity implements OnClickListener, OnItemClickListener {
    private CrmOptionsListVO mCrmOptionsListVO;
    private BSRefreshListView mRefreshListView;
    private CrmOptionsAdapter mAdapter;
    private BSIndexEditText mBSBsIndexEditText;
    private String mType = "";
    private String mKeyword = "";
    private int mResultCode;
    private CharacterParser mCharacterParser;
    private List<CrmOptionsListVO> mList;
    private StringBuffer pidBuffer, pNamebuffer;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.crm_options_list, mContentLayout);
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    @Override
    public void updateUi() {
    }

    @Override
    public void executeSuccess() {
        super.executeSuccess();
        mRefreshListView.onRefreshComplete();
        if (mCrmOptionsListVO.getArray() != null) {
            mAdapter.updateData(mCrmOptionsListVO.getArray());
        }
    }

    @Override
    public void executeFailure() {
        super.isRequestFinish();
        mRefreshListView.onRefreshComplete();

        if (mCrmOptionsListVO == null) {
            super.showNoNetView();
        } else {
            mAdapter.updateData(mCrmOptionsListVO.getArray());
        }
    }

    @Override
    public void initView() {
        mRefreshListView = (BSRefreshListView) findViewById(R.id.lv_refresh);
        mAdapter = new CrmOptionsAdapter(this);
        mRefreshListView.setAdapter(mAdapter);
        mBSBsIndexEditText = (BSIndexEditText) findViewById(R.id.edit_single_search);
        initData();
        mCharacterParser = CharacterParser.getInstance();
        pidBuffer = new StringBuffer();
        pNamebuffer = new StringBuffer();
    }

    @Override
    public void bindViewsListener() {
        mOkTv.setOnClickListener(this);
        mRefreshListView.setOnItemClickListener(this);
        mBSBsIndexEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void initData() {
        mType = this.getIntent().getStringExtra("type");
        String name = this.getIntent().getStringExtra("name");
        mTitleTv.setText(name);
        mResultCode = this.getIntent().getIntExtra("resulut_code", 10);
    }

    public boolean getData() {
        try {
            Gson gson = new Gson();
            String url = "";

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put("type", mType);
            map.put("keyword", mKeyword);
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            if ("1".equals(mType)) {
                // 合同选择客户
                url = Constant.CRM_GETPEROSONTOBUSSINES;
            } else if ("2".equals(mType)) {
                // 拜访记录
                url = Constant.CRM_VISIT_GETCUSTOMER;
            } else if ("4".equals(mType)) {
                // 客户关联商机
                url = Constant.CRM_GETVISTITORTOBUSSINES;
                map.put("cid", this.getIntent().getStringExtra("cid"));
            } else if ("5".equals(mType)) {
                url = Constant.CRM_CREATE_CLIENT_FROM_CONTACTS;
            }

            String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + url, map);
            mCrmOptionsListVO = gson.fromJson(jsonStrList, CrmOptionsListVO.class);
            if (Constant.RESULT_CODE.equals(mCrmOptionsListVO.getCode())) {
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
    public void onClick(View arg0) {

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int arg2, long position) {
        if (mAdapter.mList.size() == 0)
            return;
        CrmOptionsListVO vo = (CrmOptionsListVO) mAdapter.getItem((int) position);
        ImageView stautsImg = (ImageView) view.findViewById(R.id.status_img);
        stautsImg.setImageResource(R.drawable.common_ic_selected);
        Intent intent = new Intent();
        if (vo.getLid() == null) {
            intent.putExtra("id", vo.getId());
            intent.putExtra("name", vo.getName());

        } else {
            intent.putExtra("id", vo.getLid());
            intent.putExtra("name", vo.getLname());
        }

        // 客户关联商机的时候有money
        for (int i = 0; i < mAdapter.mList.size(); i++) {
            if (mCrmOptionsListVO.getArray().get(i).getMoney() != null && mCrmOptionsListVO.getArray().get(i).getId().equals(vo.getId())) {
                intent.putExtra("money", mCrmOptionsListVO.getArray().get(i).getMoney());
            }

        }

        if (vo.getProduct() != null) {
            pidBuffer.setLength(0);
            pNamebuffer.setLength(0);
            for (int i = 0; i < vo.getProduct().size(); i++) {
                pidBuffer.append(vo.getProduct().get(i).getPid());
                pNamebuffer.append(vo.getProduct().get(i).getName());
                if (i != vo.getProduct().size() - 1) {
                    pidBuffer.append(",");
                    pNamebuffer.append(",");
                }
            }

            intent.putExtra("pr_name", pNamebuffer.toString());
            intent.putExtra("pr_id", pidBuffer.toString());
        }

        this.setResult(mResultCode, intent);
        this.finish();

    }

    // 根据输入框中的值来过滤数据并更新ListView
    private void filterData(String filterStr) {
        if (mCrmOptionsListVO.getArray() == null)
            return;
        List<CrmOptionsListVO> filterDateList = new ArrayList<CrmOptionsListVO>();
        if (TextUtils.isEmpty(filterStr) && mCrmOptionsListVO.getArray() != null) {
            filterDateList = mCrmOptionsListVO.getArray();
        } else {
            filterDateList.clear();
            for (CrmOptionsListVO vo : mCrmOptionsListVO.getArray()) {
                String name = "";
                if (vo.getLname() != null) {
                    name = vo.getLname();
                } else {
                    name = vo.getName();
                }

                if (name.indexOf(filterStr.toString()) != -1 || mCharacterParser.getSelling(name).startsWith(filterStr.toString())) {
                    filterDateList.add(vo);
                }
            }
        }

        // 根据a-z进行排序
        mAdapter.updateData(filterDateList);
    }
}
