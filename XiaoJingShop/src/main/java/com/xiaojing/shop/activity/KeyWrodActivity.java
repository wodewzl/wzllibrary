package com.xiaojing.shop.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.mode.BaseVO;
import com.xiaojing.shop.R;
import com.xiaojing.shop.application.AppApplication;
import com.xiaojing.shop.constant.Constant;
import com.xiaojing.shop.mode.KeyWordVO;
import com.yang.flowlayoutlibrary.FlowLayout;

import java.util.Arrays;

public class KeyWrodActivity extends BaseActivity {
    private FlowLayout mFlowLayout;
    private EditText mSearchEt;
    private String mKeyword = "";

    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.key_wrod_activity);
    }

    @Override
    public void initView() {
        mBaseTitleTv.setText("关键字");
        mFlowLayout = (FlowLayout) findViewById(R.id.fl_keyword);
        mSearchEt = getViewById(R.id.search_et);
    }

    @Override
    public void bindViewsListener() {
        mSearchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                mKeyword = textView.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("keyword", mKeyword);
                open(ShopListActivity.class, bundle, 0);
                return false;
            }
        });

    }

    @Override
    public void getData() {
        RequestParams paramsMap = new RequestParams();
        String mUrl = Constant.KEYWORD_URL;
        if (AppApplication.getInstance().getUserInfoVO() != null)
            paramsMap.put("key", AppApplication.getInstance().getUserInfoVO().getKey());
        HttpClientUtil.get(mActivity, mThreadUtil, mUrl, paramsMap, KeyWordVO.class);
    }

    @Override
    public void hasData(BaseVO vo) {
        KeyWordVO keyWordVO = (KeyWordVO) vo;
        mFlowLayout.setFlowLayout(Arrays.asList(((KeyWordVO) vo).getDatas().getList()), new FlowLayout.OnItemClickListener() {
            @Override
            public void onItemClick(String content) {
                mKeyword = content;
                Bundle bundle = new Bundle();
                bundle.putString("keyword", mKeyword);
                open(ShopListActivity.class, bundle, 0);
            }
        });
    }

    @Override
    public void noData(BaseVO vo) {

    }

    @Override
    public void noNet() {

    }


}
