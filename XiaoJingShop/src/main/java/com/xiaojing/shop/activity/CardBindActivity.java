package com.xiaojing.shop.activity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.rey.material.widget.Button;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.constant.BaseConstant;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.interfaces.PostCallback;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.mode.EBMessageVO;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.xiaojing.shop.R;
import com.xiaojing.shop.application.AppApplication;
import com.xiaojing.shop.constant.Constant;

import net.anumbrella.customedittext.FloatLabelView;

import org.greenrobot.eventbus.EventBus;

public class CardBindActivity extends BaseActivity implements View.OnClickListener, PostCallback {
    private FloatLabelView mPhoneView, mNameView, mNumberView, mBlankNameView;
    private TextView mBlankNameTv;
    private String mBlankName = "", mBlankCode;
    private Button mCommitBt;

    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.card_bind_activity);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initView() {
        mBaseTitleTv.setText("绑定银行卡");
        mNameView = getViewById(R.id.name_view);
        mNumberView = getViewById(R.id.number_view);
        mBlankNameView = getViewById(R.id.blank_name_view);
        mBlankNameTv = getViewById(R.id.blank_name_tv);
        mPhoneView = getViewById(R.id.phone_view);
        mCommitBt = getViewById(R.id.commit_bt);
        mCommitBt.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.C7, R.color.C7));
    }

    @Override
    public void bindViewsListener() {
        mBlankNameTv.setOnClickListener(this);
        mCommitBt.setOnClickListener(this);
    }

    @Override
    public void getData() {
        HttpClientUtil.show(mThreadUtil);
    }

    @Override
    public void hasData(BaseVO vo) {

    }

    @Override
    public void noData(BaseVO vo) {

    }

    @Override
    public void noNet() {

    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.blank_name_tv:
                Intent intent = new Intent();
                intent.putExtra("type","1");
                intent.setClass(this,CardShowActivity.class);
                this.startActivityForResult(intent,1);
                break;
            case R.id.commit_bt:
                commit();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)
            return;
        switch (requestCode) {
            case 1:
                mBlankName = data.getStringExtra("name");
                mBlankCode = data.getStringExtra("code");
                mBlankNameTv.setText(mBlankName);
                break;
            default:
                break;
        }
    }

    public void commit() {
        if (mPhoneView.getEditText().getText().toString().length() == 0) {
            showCustomToast("请填写联系电话");
            return;
        }
        if (mNameView.getEditText().getText().toString().length() == 0) {
            showCustomToast("请填写持卡人姓名");
            return;
        }
        if (mBlankName.length() == 0) {
            showCustomToast("请选择银行类型");
            return;
        }
        if (mNumberView.getEditText().getText().toString().length() == 0) {
            showCustomToast("请填写银行卡号");
            return;
        }

        showProgressDialog();
        RequestParams paramsMap = new RequestParams();
        String mUrl = Constant.ADD_BLANK_URL;
        if (AppApplication.getInstance().getUserInfoVO() != null)
            paramsMap.put("key", AppApplication.getInstance().getUserInfoVO().getKey());
        paramsMap.put("true_name", mNameView.getEditText().getText().toString());
        paramsMap.put("mob_phone", mPhoneView.getEditText().getText().toString());
        paramsMap.put("bank_code", mBlankCode);
        paramsMap.put("bank_name", mBlankName);
        paramsMap.put("card_no", mNumberView.getEditText().getText().toString());

        HttpClientUtil.post(mActivity, mActivity, mUrl, paramsMap,null, this);
    }


    @Override
    public void success(BaseVO vo) {
        if (BaseConstant.RESULT_SUCCESS_CODE.equals(vo.getCode())) {
            showCustomToast("绑定功");
            EventBus.getDefault().post(new EBMessageVO("withdraw"));
            this.finish();
        } else {
            showCustomToast(vo.getError());
        }
    }
}
