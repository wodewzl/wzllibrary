package com.xiaojing.shop.activity;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.constant.BaseConstant;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.interfaces.PostCallback;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.wuzhanglong.library.utils.ShareUtil;
import com.wuzhanglong.library.utils.ThreadUtil;
import com.xiaojing.shop.R;
import com.xiaojing.shop.application.AppApplication;
import com.xiaojing.shop.constant.Constant;
import com.xiaojing.shop.mode.MyVO;

import de.hdodenhof.circleimageview.CircleImageView;

public class PrivilegeActivity extends BaseActivity implements View.OnClickListener, PostCallback {
    private TextView mJinDoutv, mJinBiTv, mPrivilegeTv01, mPrivilegeTv02, mPrivilegeTv03, mClickTv01, mClickTv02, mClickTv03;
    private MyVO mMyVO;
    private TextView mNameTv;
    private CircleImageView mHeadImg;

    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.privilege_activity);
    }

    @Override
    public void initView() {
        mBaseHeadLayout.setVisibility(View.GONE);
        mJinDoutv = getViewById(R.id.tv_jindou);
        mJinBiTv = getViewById(R.id.tv_jinbi);
        mPrivilegeTv01 = getViewById(R.id.privilege_tv_01);
        mPrivilegeTv02 = getViewById(R.id.privilege_tv_02);
        mPrivilegeTv03 = getViewById(R.id.privilege_tv_03);
        mClickTv01 = getViewById(R.id.click_tv_01);
        mClickTv02 = getViewById(R.id.click_tv_02);
        mClickTv03 = getViewById(R.id.click_tv_03);
        mNameTv = getViewById(R.id.name_tv);
        mHeadImg = getViewById(R.id.head_img);
    }

    @Override
    public void bindViewsListener() {
        mClickTv01.setOnClickListener(this);
        mClickTv02.setOnClickListener(this);
        mClickTv03.setOnClickListener(this);
    }

    @Override
    public void getData() {
        RequestParams params = new RequestParams();
        if (AppApplication.getInstance().getUserInfoVO() != null)
            params.put("key", AppApplication.getInstance().getUserInfoVO().getKey());
        String mUrl = Constant.PRIVILEGE_URL;
        HttpClientUtil.get(mActivity, mThreadUtil, mUrl, params, MyVO.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void hasData(BaseVO vo) {
        MyVO myVO = (MyVO) vo;
        mMyVO = myVO.getDatas().getMember_info();
        mJinDoutv.setText(mMyVO.getBean());
        mJinBiTv.setText(mMyVO.getGold());
        BaseCommonUtils.setTextThree(this, mPrivilegeTv01, "奖励 ", mMyVO.getInvite_bean(), " 小鲸豆", R.color.XJColor13, 1.3f);
        BaseCommonUtils.setTextThree(this, mPrivilegeTv02, "奖励 ", mMyVO.getSignin_bean(), " 小鲸豆", R.color.XJColor13, 1.3f);
        BaseCommonUtils.setTextThree(this, mPrivilegeTv03, "奖励 ", mMyVO.getAuthentic_gold(), " 小鲸豆", R.color.XJColor13, 1.3f);
        Picasso.with(this).load(mMyVO.getAvator()).into(mHeadImg);
        if ("1".equals(mMyVO.getCan_authentic())) {
            mNameTv.setText(mMyVO.getNickname() + " | " + "未认证");
        } else {
            mNameTv.setText(mMyVO.getNickname() + " | " + "已认证");
        }
        if ("0".equals(mMyVO.getAuthentic_gold())) {
            mClickTv01.setBackground(BaseCommonUtils.setBackgroundShap(this, 30, R.color.C3, R.color.C3));
            mClickTv01.setTextColor(ContextCompat.getColor(this, R.color.C5));
            mClickTv01.setText("已邀请");
        } else {
            mClickTv01.setBackground(BaseCommonUtils.setBackgroundShap(this, 30, R.color.XJColor13, R.color.XJColor13));
            mClickTv01.setTextColor(ContextCompat.getColor(this, R.color.C1));
            mClickTv01.setText("去邀请");
        }
        if ("0".equals(mMyVO.getCan_signin())) {
            mClickTv02.setBackground(BaseCommonUtils.setBackgroundShap(this, 30, R.color.C3, R.color.C3));
            mClickTv02.setTextColor(ContextCompat.getColor(this, R.color.C5));
            mClickTv02.setText("已签到");
        } else {
            mClickTv02.setBackground(BaseCommonUtils.setBackgroundShap(this, 30, R.color.XJColor13, R.color.XJColor13));
            mClickTv02.setTextColor(ContextCompat.getColor(this, R.color.C1));
            mClickTv02.setText("去签到");
        }
        if ("0".equals(mMyVO.getCan_authentic())) {
            mClickTv03.setBackground(BaseCommonUtils.setBackgroundShap(this, 30, R.color.C3, R.color.C3));
            mClickTv03.setTextColor(ContextCompat.getColor(this, R.color.C5));
            mClickTv03.setText("已认证");
        } else {
            mClickTv03.setBackground(BaseCommonUtils.setBackgroundShap(this, 30, R.color.XJColor13, R.color.XJColor13));
            mClickTv03.setTextColor(ContextCompat.getColor(this, R.color.C1));
            mClickTv03.setText("去认证");
        }
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
            case R.id.click_tv_01:
                if ("0".equals(mMyVO.getAuthentic_gold())) {
                    showCustomToast("已邀请");
                    return;
                }
                ShareUtil.share(mActivity, mMyVO.getShare_data().getImage(), mMyVO.getShare_data().getTitle(), mMyVO.getShare_data().getDesc(), mMyVO.getShare_data().getUrl());
                break;
            case R.id.click_tv_02:
                if ("0".equals(mMyVO.getCan_signin())) {
                    showCustomToast("已签到");
                    return;
                }
                sign();
                break;
            case R.id.click_tv_03:
                if ("0".equals(mMyVO.getCan_authentic())) {
                    showCustomToast("已认证");
                    return;
                }
                mActivity.openActivity(ApproveNameActivity.class);
                break;
            default:
                break;
        }
    }

    public void sign() {
        showProgressDialog();
        RequestParams paramsMap = new RequestParams();
        String mUrl = Constant.PRIVILEGE_SIGIN_URL;
        if (AppApplication.getInstance().getUserInfoVO() != null)
            paramsMap.put("key", AppApplication.getInstance().getUserInfoVO().getKey());

        HttpClientUtil.post(mActivity, mActivity, mUrl, paramsMap, null, this);
    }

    @Override
    public void success(BaseVO vo) {
        dismissProgressDialog();
        if (BaseConstant.RESULT_SUCCESS_CODE.equals(vo.getCode())) {
            showCustomToast("签到成功");
            mThreadUtil = new ThreadUtil(this, this);
            mThreadUtil.start();
        } else {
            showCustomToast("签到失败");
        }
    }
}
