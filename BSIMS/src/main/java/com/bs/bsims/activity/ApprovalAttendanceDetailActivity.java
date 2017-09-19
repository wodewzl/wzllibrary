
package com.bs.bsims.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
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
import com.bs.bsims.model.AttendanceResultVO;
import com.bs.bsims.model.AttendanceVO;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.utils.CommonDateUtils;
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

public class ApprovalAttendanceDetailActivity extends BaseActivity {
    private String mUid;
    private String mAlid;
    private String mType;

    private AttendanceVO mAttendanceVO;
    private BSGridView mTransferGv, mApproverGv, mInformGv;
    private HeadAdapter mTransferAdapter, mApproverAdapter, mInformAdapter;
    private TextView mTransferTv, mApproverTv, mApprovalGoTv, mInformTv, mInformGoTv, mTransferGoTv;
    private LinearLayout mTransferLayout, mApproverLayout, mInformLayout;

    private TextView mPersonTitle01, mPersonTitle02, mPersonTitle03, mPersonTitle04;
    private BSCircleImageView mHeadIcon;

    private TextView mInfoTv, mMissDateTv;
    private ApprovalViewFragment fragment;

    private ListView mListView;
    private ApprovlaNewIdeaAdapter mApprovlaIdeaAdapter;
    private TextView mApprovalIdeaTv;
    private ApprovalViewFragment mApprovalViewFragment;

    private int mCurrent = 0;
    private String mMessageid;
    private AttendanceResultVO mAttendanceResultVO;
    private TextView mParentTitleTv;

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.approval_attendance_detail, null);
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
        mTitleTv.setText(mAttendanceVO.getTypename() + "详情");
        mParentTitleTv.setText(mAttendanceVO.getTypename() + "日期：");
        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = CommonUtils.initImageLoaderOptions();
        imageLoader.displayImage(mAttendanceVO.getHeadpic(), mHeadIcon, options);
        mHeadIcon.setUserId(mAttendanceVO.getUserid());// 获取头像对应的用户ID
        mHeadIcon.setUserName(mAttendanceVO.getFullname());
        mHeadIcon.setUrl(mAttendanceVO.getHeadpic());
        mPersonTitle01.setText(mAttendanceVO.getFullname());
        mPersonTitle02.setText(mAttendanceVO.getDname()+"/"+mAttendanceVO.getPname());

        long time = Long.parseLong(mAttendanceVO.getTime()) * 1000;
        mPersonTitle03.setText(CommonDateUtils.parseDate(time,
                "yyyy-MM-dd HH:mm"));
        mPersonTitle04.setVisibility(View.GONE);
        mInfoTv.setText(mAttendanceVO.getInfo());
        mMissDateTv.setText(DateUtils.parseDate(Long.valueOf(mAttendanceVO.getSptime()) * 1000));

        mTransferTv.setVisibility(View.VISIBLE);
        mTransferTv.setText(R.string.proof_people);
        mTransferLayout.setVisibility(View.VISIBLE);
        mTransferGoTv.setVisibility(View.VISIBLE);
        mTransferAdapter.updateData(mAttendanceVO.getWitUser());
        mTransferAdapter.setProven(true);

        if (mAttendanceVO.getAppUser() == null && mAttendanceVO.getInsUser() == null)
            return;
        if (mAttendanceVO.getAppUser() != null) {
            mApproverAdapter.updateData(mAttendanceVO.getAppUser());
            // mApproverLayout.setVisibility(View.VISIBLE);
            // mApproverTv.setVisibility(View.VISIBLE);
            // mApproverAdapter.setApproval(true);// 圆圈显示类型判断
            // mApproverAdapter.setStatus(mLeaveDetailVO.getStatus());

            // 刷新状态列表
            for (int i = 0; i < mAttendanceVO.getAppUser().size(); i++) {
                EmployeeVO vo = mAttendanceVO.getAppUser().get(i);
                if ("0".equals(vo.getStatus())) {
                    mCurrent++;
                }
            }

            if (mAttendanceVO.getOpinion() != null) {
                List<EmployeeVO> appUserList = new ArrayList<EmployeeVO>();
                appUserList.addAll(mAttendanceVO.getAppUser());
                for (int i = 0; i < mAttendanceVO.getAppUser().size(); i++) {
                    for (int j = 0; j < mAttendanceVO.getOpinion().size(); j++) {
                        if (mAttendanceVO.getOpinion().get(j).getUserid().equals(mAttendanceVO.getAppUser().get(i).getUserid())) {
                            appUserList.remove(i);
                            appUserList.add(i, mAttendanceVO.getOpinion().get(j));
                            continue;
                        }
                    }
                }
                mApprovlaIdeaAdapter.updateData(appUserList);
                mApprovalIdeaTv.setVisibility(View.VISIBLE);
                mApprovlaIdeaAdapter.setApprovalType(mAttendanceVO.getGenre());
                mApprovlaIdeaAdapter.setApprovalId(mAttendanceVO.getApid());
            }
            else {
                mApprovlaIdeaAdapter.updateData(mAttendanceVO.getAppUser());
                mApprovalIdeaTv.setVisibility(View.VISIBLE);
                mApprovlaIdeaAdapter.setApprovalType(mAttendanceVO.getGenre());
                mApprovlaIdeaAdapter.setApprovalId(mAttendanceVO.getApid());
            }
            
            if(!BSApplication.getInstance().getUserId().equals(mAttendanceVO.getUserid())){
                mApprovlaIdeaAdapter.setViewCui(true);//不显示
            }
            else{
                mApprovlaIdeaAdapter.setViewCui(false);//显示
            }
            
        }
        if (mAttendanceVO.getInsUser() != null) {
            mInformTv.setVisibility(View.VISIBLE);
            mInformLayout.setVisibility(View.VISIBLE);
            mInformAdapter.updateData(mAttendanceVO.getInsUser());
        } else {
            mInformTv.setVisibility(View.GONE);
            mInformLayout.setVisibility(View.GONE);
        }

        if ("1".equals(mAttendanceVO.getApproval()) || "1".equals(mAttendanceVO.getProvened())) {
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

                mApprovalViewFragment.setProvened(mAttendanceVO.getProvened());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 判断审核状态
        CommonUtils.setApprovalImg(Integer.parseInt(mAttendanceVO.getStatus()), (ImageView) findViewById(R.id.approval_status), this);

    }

    @Override
    public void executeFailure() {
        if (mAttendanceResultVO == null) {
            super.executeSuccess();
            return;
        } else {
            super.setNonetIcon(mAttendanceResultVO.getRetinfo());
        }
    }

    @Override
    public void initView() {

        mHeadIcon = (BSCircleImageView) findViewById(R.id.head_icon);
        mPersonTitle01 = (TextView) findViewById(R.id.person_title01);
        mPersonTitle02 = (TextView) findViewById(R.id.person_title02);
        mPersonTitle03 = (TextView) findViewById(R.id.person_title03);
        mPersonTitle04 = (TextView) findViewById(R.id.person_title04);
        mInfoTv = (TextView) findViewById(R.id.info_tv);
        mMissDateTv = (TextView) findViewById(R.id.miss_date_tv);
        mTransferGv = (BSGridView) findViewById(R.id.transfer_gv);
        mApproverGv = (BSGridView) findViewById(R.id.approver_gv);
        mInformGv = (BSGridView) findViewById(R.id.inform_gv);
        mTransferAdapter = new HeadAdapter(this, false);
        mApproverAdapter = new HeadAdapter(this, false);
        mInformAdapter = new HeadAdapter(this, false, true);
        mTransferGv.setAdapter(mTransferAdapter);
        mApproverGv.setAdapter(mApproverAdapter);
        mInformGv.setAdapter(mInformAdapter);

        mTransferTv = (TextView) findViewById(R.id.transfer_tv);
        mApproverTv = (TextView) findViewById(R.id.approver_tv);
        mInformTv = (TextView) findViewById(R.id.inform_people_tv);
        mApproverLayout = (LinearLayout) findViewById(R.id.approver_layout);
        mInformLayout = (LinearLayout) findViewById(R.id.inform_people_layout);
        mTransferLayout = (LinearLayout) findViewById(R.id.transfer_layout);
        mApprovalGoTv = (TextView) findViewById(R.id.approver_go_tv);
        mInformGoTv = (TextView) findViewById(R.id.inform_go_tv);
        mTransferGoTv = (TextView) findViewById(R.id.transfer_go_tv);
        initData();

        mListView = (ListView) findViewById(R.id.list_view);
        mApprovlaIdeaAdapter = new ApprovlaNewIdeaAdapter(this);
        mListView.setAdapter(mApprovlaIdeaAdapter);
        mApprovalIdeaTv = (TextView) findViewById(R.id.approval_idea_tv);
        mParentTitleTv = (TextView) findViewById(R.id.attendace_type);
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
            String strUlr = UrlUtil.getApprovalDetailUrl(Constant.APPROVAL_ATTENDANCE_DETAIL, mUid, mAlid, mMessageid);
            String jsonStr = HttpClientUtil.get(strUlr, Constant.ENCODING).trim();
            Gson gson = new Gson();
            mAttendanceResultVO = gson.fromJson(jsonStr, AttendanceResultVO.class);
            if (Constant.RESULT_CODE.equals(mAttendanceResultVO.getCode())) {
                mAttendanceVO = mAttendanceResultVO.getArray();
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

}
