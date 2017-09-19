
package com.bs.bsims.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.ApprovlaNewIdeaAdapter;
import com.bs.bsims.adapter.HeadAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.fragment.ApprovalViewFragment;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.model.OvertimeDetailResultVO;
import com.bs.bsims.model.OvertimeDetailVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.UrlUtil;
import com.bs.bsims.view.BSCircleImageView;
import com.bs.bsims.view.BSGridView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ApprovalFeeApplyDetailActivity extends BaseActivity implements OnClickListener {
    private String mUid;
    private String mAlid;
    private String mType;

    private OvertimeDetailVO mOvertimeDetialVO;

    private TextView mPersonTitle01, mPersonTitle02, mPersonTitle03, mPersonTitle04;
    private BSCircleImageView mHeadIcon;

    // 显示上传的图片
    private ImageView mDetailImg01, mDetailImg02, mDetailImg03;
    private List<ImageView> mListImag;
    private LinearLayout mPictureLayout;

    private BSGridView mApproverGv, mInformGv;
    private HeadAdapter mApproverAdapter, mInformAdapter;
    private TextView mApproverTv, mApprovalGoTv, mInformTv, mInformGoTv, mCancel, mSure, mGuanlianStatus;
    private LinearLayout mApproverLayout, mInformLayout;

    private TextView mBrrowStratTimeTv, mRealStratTimeTv, mBrrowFeeTotal, mRealFeeTotal, mBrrowContentReason, mRealContentReason, mFeeTv, mBrrowFeeTv, mBrrowFeelReal, mRealFeelReal;
    private TextView mBackMoney, mMoneyStatus, mRemarkTv;
    private ListView mListView;
    private ApprovlaNewIdeaAdapter mApprovlaIdeaAdapter;
    private TextView mApprovalIdeaTv;
    private ApprovalViewFragment mApprovalViewFragment;
    private int mCurrent = 0;

    private View mPLayout;

    private LinearLayout mBrrowLayout, mRealLayout, mFeeTotalLayout;
    private ImageView mApprovalStatus;
    private String mMessageid;
    private OvertimeDetailResultVO mOvertimeDetailResultVO;

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.approval_feeapply_detail, null);
        mContentLayout.addView(layout);
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
        mTitleTv.setText(mOvertimeDetialVO.getTypename() + "详情");
        mFeeTv.setText(mOvertimeDetialVO.getTypename());

        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = CommonUtils.initImageLoaderOptions();
        imageLoader.displayImage(mOvertimeDetialVO.getHeadpic(), mHeadIcon, options);
        mHeadIcon.setUserId(mOvertimeDetialVO.getUserid());//获取头像对应的用户ID
        mHeadIcon.setUserName(mOvertimeDetialVO.getFullname());
        mHeadIcon.setUrl(mOvertimeDetialVO.getHeadpic());
        mPersonTitle01.setText(mOvertimeDetialVO.getFullname());
        mPersonTitle02.setText(mOvertimeDetialVO.getPname() + "/" + mOvertimeDetialVO.getDname());

        long time = Long.parseLong(mOvertimeDetialVO.getAddtime()) * 1000;
        mPersonTitle03.setText(DateUtils.parseDate(time));
        mPersonTitle03.setVisibility(View.GONE);
        // mPersonTitle04.setText();

        if ("0".equals(mOvertimeDetialVO.getStatus())) {
            mApprovalStatus.setImageResource(R.drawable.approval_detail_status_01);
        } else if ("1".equals(mOvertimeDetialVO.getStatus())) {
            mApprovalStatus.setImageResource(R.drawable.approval_detail_status_04);
        }
        else if ("2".equals(mOvertimeDetialVO.getStatus())) {
            mApprovalStatus.setImageResource(R.drawable.approval_detail_status_03);
        }
        else if ("3".equals(mOvertimeDetialVO.getStatus())) {
            mApprovalStatus.setImageResource(R.drawable.approval_detail_status_02);
        }

        if (!"1".equals(mOvertimeDetialVO.getIsguanlian()) || "7".equals(mOvertimeDetialVO.getType())) {
            mBrrowLayout.setVisibility(View.GONE);
            mRealLayout.findViewById(R.id.status_layout).setVisibility(View.GONE);
            mFeeTotalLayout.setVisibility(View.GONE);
        }

        if ("1".equals(mOvertimeDetialVO.getIsguanlian())) {
            mBrrowStratTimeTv.setText(DateUtils.parseDateDay(mOvertimeDetialVO.getBtime()));
            mGuanlianStatus.setText("实际金额：");
        } else {
            mGuanlianStatus.setText("申请金额：");
        }

        CommonUtils.setDifferentTextColorBefore(mBrrowFeelReal, mOvertimeDetialVO.getMoney1(),
                "元", "#FFBA00");

        if ("7".equals(mOvertimeDetialVO.getType())) {
            mRealStratTimeTv.setText(DateUtils.parseDateDay(mOvertimeDetialVO.getBtime()));
            CommonUtils.setDifferentTextColorBefore(mRealFeelReal, mOvertimeDetialVO.getMoney1(), "元", "#00a9fe");
        } else {
            CommonUtils.setDifferentTextColorBefore(mRealFeelReal, mOvertimeDetialVO.getTotalprice(), "元", "#00a9fe");
            mRealStratTimeTv.setText(DateUtils.parseDateDay(mOvertimeDetialVO.getAtime()));
        }

        // 1为应退款，2为应补款
        if ("1".equals(mOvertimeDetialVO.getMtype())) {
            mMoneyStatus.setText("应退金额：");
        } else {
            mMoneyStatus.setText("应补金额：");
        }
        CommonUtils.setDifferentTextColorBefore(mBackMoney, mOvertimeDetialVO.getPrice(), "元", "#00a9fe");
        CommonUtils.setDifferentTextColor(mBrrowContentReason, "申请原因：", mOvertimeDetialVO.getRemarks(), "#A5A5A5");

        // 单纯的借支单用的是普通报销的布局，所以要判断下
        if ("7".equals(mOvertimeDetialVO.getType())) {
            // CommonUtils.setDifferentTextColor(mRemarkTv, "备注说明：", mOvertimeDetialVO.getRemarks(),
            // "#A5A5A5");
            CommonUtils.setTextTwoBefore(this, mRemarkTv, "备注说明：", mOvertimeDetialVO.getRemarks(), R.color.approval_remark, 1.0f);
        } else {
            // CommonUtils.setDifferentTextColor(mRemarkTv, "备注说明：", mOvertimeDetialVO.getReason(),
            // "#A5A5A5");
            CommonUtils.setTextTwoBefore(this, mRemarkTv, "备注说明：", mOvertimeDetialVO.getReason(), R.color.approval_remark, 1.0f);
        }

        DisplayImageOptions optionsPic = new DisplayImageOptions.Builder().showStubImage(R.drawable.common_ic_image_default).showImageForEmptyUri(R.drawable.common_ic_image_default)
                .showImageOnFail(R.drawable.common_ic_image_default).cacheInMemory().cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
        if (mOvertimeDetialVO.getImgs() != null) {
            mPictureLayout.setVisibility(View.VISIBLE);
            List<String> list = mOvertimeDetialVO.getImgs();
            for (int i = 0; i < list.size(); i++) {
                if (i < 3) {
                    mListImag.get(i).setOnClickListener(this);
                    imageLoader.displayImage(list.get(i), mListImag.get(i), optionsPic);
                }
            }
        } else {
            mPictureLayout.setVisibility(View.GONE);
        }

        if (mOvertimeDetialVO.getAppUser() != null) {
            mApproverAdapter.updateData(mOvertimeDetialVO.getAppUser());
            // mApproverLayout.setVisibility(View.VISIBLE);
            // mApproverTv.setVisibility(View.VISIBLE);
            // mApproverAdapter.setApproval(true);// 圆圈显示类型判断
            // mApproverAdapter.setStatus(mLeaveDetailVO.getStatus());

            // 刷新状态列表
            for (int i = 0; i < mOvertimeDetialVO.getAppUser().size(); i++) {
                EmployeeVO vo = mOvertimeDetialVO.getAppUser().get(i);
                if ("0".equals(vo.getStatus())) {
                    mCurrent++;
                }
            }

            if (mOvertimeDetialVO.getOpinion() != null) {
                List<EmployeeVO> appUserList = new ArrayList<EmployeeVO>();
                appUserList.addAll(mOvertimeDetialVO.getAppUser());
                for (int i = 0; i < mOvertimeDetialVO.getAppUser().size(); i++) {
                    for (int j = 0; j < mOvertimeDetialVO.getOpinion().size(); j++) {
                        if (mOvertimeDetialVO.getOpinion().get(j).getUserid().equals(mOvertimeDetialVO.getAppUser().get(i).getUserid())) {
                            appUserList.remove(i);
                            appUserList.add(i, mOvertimeDetialVO.getOpinion().get(j));
                            continue;
                        }
                    }
                }
                mApprovlaIdeaAdapter.updateData(appUserList);
                mApprovalIdeaTv.setVisibility(View.VISIBLE);
                mApprovlaIdeaAdapter.setApprovalType(mOvertimeDetialVO.getGenre());
                mApprovlaIdeaAdapter.setApprovalId(mOvertimeDetialVO.getFdid());
            }
            else{
                mApprovlaIdeaAdapter.updateData(mOvertimeDetialVO.getAppUser());
                mApprovalIdeaTv.setVisibility(View.VISIBLE);
                mApprovlaIdeaAdapter.setApprovalType(mOvertimeDetialVO.getGenre());
                mApprovlaIdeaAdapter.setApprovalId(mOvertimeDetialVO.getFdid());
            }
            
            if(!BSApplication.getInstance().getUserId().equals(mOvertimeDetialVO.getUserid())){
                mApprovlaIdeaAdapter.setViewCui(true);//不显示
            }
            else{
                mApprovlaIdeaAdapter.setViewCui(false);//显示
            }
        }

        if (mOvertimeDetialVO.getInsUser() != null) {
            mInformTv.setVisibility(View.VISIBLE);
            mInformLayout.setVisibility(View.VISIBLE);
            mInformAdapter.updateData(mOvertimeDetialVO.getInsUser());
        } else {
            mInformTv.setVisibility(View.GONE);
            mInformLayout.setVisibility(View.GONE);
        }
 
        if ("1".equals(mOvertimeDetialVO.getApproval())) {
            // 显示界面
            try {
                mApprovalViewFragment = new ApprovalViewFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.bottom_view, mApprovalViewFragment);
                transaction.commitAllowingStateLoss();
                mApprovalViewFragment.setApprovalid(mAlid);
                mApprovalViewFragment.setType(mType);
                mApprovalViewFragment.setCount(mCurrent);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void executeFailure() {
        if (mOvertimeDetailResultVO == null) {
            super.executeFailure();
            return;
        } else {
            super.setNonetIcon(mOvertimeDetailResultVO.getRetinfo());
        }
    }

    @Override
    public void initView() {
        mHeadIcon = (BSCircleImageView) findViewById(R.id.head_icon);
        mPersonTitle01 = (TextView) findViewById(R.id.person_title01);
        mPersonTitle02 = (TextView) findViewById(R.id.person_title02);
        mPersonTitle03 = (TextView) findViewById(R.id.person_title03);
        mPersonTitle04 = (TextView) findViewById(R.id.person_title04);

        mPictureLayout = (LinearLayout) findViewById(R.id.picture_layout);
        mDetailImg01 = (ImageView) findViewById(R.id.detial_img_01);
        mDetailImg02 = (ImageView) findViewById(R.id.detial_img_02);
        mDetailImg03 = (ImageView) findViewById(R.id.detial_img_03);
        mListImag = new ArrayList<ImageView>();
        mListImag.add(mDetailImg01);
        mListImag.add(mDetailImg02);
        mListImag.add(mDetailImg03);

        mApprovalStatus = (ImageView) findViewById(R.id.approval_status);
        mBrrowLayout = (LinearLayout) findViewById(R.id.borrow_layout);
        mRealLayout = (LinearLayout) findViewById(R.id.real_layout);
        mBrrowFeeTv = (TextView) findViewById(R.id.borrow_fee_tv);
        mBrrowFeeTotal = (TextView) mBrrowLayout.findViewById(R.id.fee_total);
        mRealFeeTotal = (TextView) mRealLayout.findViewById(R.id.fee_total);
        mBrrowFeelReal = (TextView) mBrrowLayout.findViewById(R.id.real_fee);
        mRealFeelReal = (TextView) mRealLayout.findViewById(R.id.real_fee);
        mBrrowContentReason = (TextView) mBrrowLayout.findViewById(R.id.content_tv);
        mRealContentReason = (TextView) mRealLayout.findViewById(R.id.content_tv);
        mBackMoney = (TextView) findViewById(R.id.back_money);
        mMoneyStatus = (TextView) findViewById(R.id.money_status);
        mRemarkTv = (TextView) findViewById(R.id.remark_tv);

        mBrrowStratTimeTv = (TextView) mBrrowLayout.findViewById(R.id.start_time_tv);
        mRealStratTimeTv = (TextView) mRealLayout.findViewById(R.id.start_time_tv);
        mApproverGv = (BSGridView) findViewById(R.id.approver_gv);
        mInformGv = (BSGridView) findViewById(R.id.inform_gv);
        mApproverAdapter = new HeadAdapter(this, false);
        mInformAdapter = new HeadAdapter(this, false, true);
        mApproverGv.setAdapter(mApproverAdapter);
        mInformGv.setAdapter(mInformAdapter);

        mApproverTv = (TextView) findViewById(R.id.approver_tv);
        mInformTv = (TextView) findViewById(R.id.inform_people_tv);
        mApproverLayout = (LinearLayout) findViewById(R.id.approver_layout);
        mInformLayout = (LinearLayout) findViewById(R.id.inform_people_layout);

        mListView = (ListView) findViewById(R.id.list_view);
        mApprovlaIdeaAdapter = new ApprovlaNewIdeaAdapter(this);
        mListView.setAdapter(mApprovlaIdeaAdapter);
        mApprovalIdeaTv = (TextView) findViewById(R.id.approval_idea_tv);
        mFeeTv = (TextView) findViewById(R.id.fee_tv);
        mFeeTotalLayout = (LinearLayout) findViewById(R.id.fee_total_layout);
        mGuanlianStatus = (TextView) findViewById(R.id.guanlian_status);

        initData();
    }

    public void initData() {
        Intent intent = getIntent();
        mUid = intent.getStringExtra("uid");
        mAlid = intent.getStringExtra("alid");
        mType = intent.getStringExtra("type");
        mMessageid = intent.getStringExtra("messageid");
    }

    @Override
    public void bindViewsListener() {

    }

    public boolean getData() {

        try {

            String strUlr = UrlUtil.getApprovalDetailUrl(Constant.APPROVAL_FEE_APPLY_DETAIL, mUid, mAlid, mMessageid);
            String jsonStr = HttpClientUtil.get(strUlr, Constant.ENCODING).trim();
            Gson gson = new Gson();
            mOvertimeDetailResultVO = gson.fromJson(jsonStr, OvertimeDetailResultVO.class);

            if (Constant.RESULT_CODE.endsWith(mOvertimeDetailResultVO.getCode())) {
                mOvertimeDetialVO = mOvertimeDetailResultVO.getArray();
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("isread", "1");
                map.put("approvalid", mAlid);
                CommonUtils.sendBroadcast(this, Constant.HOME_MSG, map);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        ArrayList<String> list = mOvertimeDetialVO.getImgs();
        intent.putStringArrayListExtra("piclist", list);
        intent.setClass(this, ImagePreviewActivity.class);
        startActivity(intent);

    }

}
