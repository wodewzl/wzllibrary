
package com.beisheng.synews.activity;

import android.view.View;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.http.HttpClientUtil;
import com.beisheng.synews.adapter.MessageAdapter;
import com.beisheng.synews.application.AppApplication;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.mode.MessageVO;
import com.beisheng.synews.view.BSListViewConflict;
import com.google.gson.Gson;
import com.im.zhsy.R;

import java.util.HashMap;

public class MessageActivity extends BaseActivity {

    private MessageVO mMessageVO;
    private BSListViewConflict mListView;
    private MessageAdapter mAdapter;
    private int mState = 0; // 0为首次,1为下拉刷新 ，2为加载更多
    private String mPage = "1";

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.message_activity, mBaseContentLayout);
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    @Override
    public void initView() {
        mBaseTitleTv.setText("消息");
        mListView = (BSListViewConflict) findViewById(R.id.list_view);
        mAdapter = new MessageAdapter(this);
        // AnimationAdapter animationAdapter = new CardsAnimationAdapter(mAdapter);
        // animationAdapter.setAbsListView(mListView);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void bindViewsListener() {
    }

    public boolean getData() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("uid", AppApplication.getInstance().getUid());
            map.put("mobile", android.os.Build.MODEL);
            if (hasNetWork()) {
                String jsonStr = HttpClientUtil.getRequest(this, Constant.DOMAIN_NAME + Constant.MYSELF_MESSAGE_URL, map);
                mMessageVO = gson.fromJson(jsonStr, MessageVO.class);
                saveJsonCache(Constant.MYSELF_MESSAGE_URL, map, jsonStr);

            } else {
                String oldStr = getCacheFromDatabase(Constant.MYSELF_MESSAGE_URL, map);
                mMessageVO = gson.fromJson(oldStr, MessageVO.class);
            }

            if (Constant.RESULT_SUCCESS_CODE.equals(mMessageVO.getCode())) {
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
        super.executeSuccess();
        mAdapter.updateData(mMessageVO.getList());
    }

    @Override
    public void executeFailure() {
        super.executeFailure();
        if (mMessageVO != null)
            showCustomToast(mMessageVO.getRetinfo());
        else
            showCustomToast("亲，请检查网络哦");
    }
}
