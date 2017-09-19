
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

public class ApprovalOvertimeDetailActivity extends BaseActivity {
    private String mUid;
    private String mAlid;
    private String mType;
    private TextView mOvertimeReasonContentTv, mOverTimeContentTv;

    private TextView mStratTimeTv, mEndTimeTv, mDurationTimeTv;

    private BSGridView mTransferGv, mApproverGv, mInformGv;
    private HeadAdapter mApproverAdapter, mInformAdapter;
    private TextView mApproverTv, mInformTv;
    private LinearLayout mApproverLayout, mInformLayout;

    private OvertimeDetailVO mOvertimeDetialVO;

    private TextView mPersonTitle01, mPersonTitle02, mPersonTitle03, mPersonTitle04;
    private BSCircleImageView mHeadIcon;

    private ListView mListView;
    private ApprovlaNewIdeaAdapter mApprovlaIdeaAdapter;
    private TextView mApprovalIdeaTv;
    private ApprovalViewFragment mApprovalViewFragment;
    private int mCurrent = 0;
    private String mMessageid;
    private OvertimeDetailResultVO mOvertimeDetailResultVO;

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.approval_overtime_detail, null);
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
        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = CommonUtils.initImageLoaderOptions();
        imageLoader.displayImage(mOvertimeDetialVO.getHeadpic(), mHeadIcon, options);
        mHeadIcon.setUserId(mOvertimeDetialVO.getUserid());//获取头像对应的用户ID
        mHeadIcon.setUserName(mOvertimeDetialVO.getFullname());
        mHeadIcon.setUrl(mOvertimeDetialVO.getHeadpic());
        mPersonTitle01.setText(mOvertimeDetialVO.getFullname());
        mPersonTitle02.setText(mOvertimeDetialVO.getPname() + "/" + mOvertimeDetialVO.getDname());

        long time = Long.parseLong(mOvertimeDetialVO.getTime()) * 1000;
        mPersonTitle03.setText(DateUtils.parseDate(time));
        // mPersonTitle04.setText();
        mStratTimeTv.setText(DateUtils.parseDate(Long.valueOf(mOvertimeDetialVO.getStime()) * 1000));
        mEndTimeTv.setText(DateUtils.parseDate(Long.valueOf(mOvertimeDetialVO.getEtime()) * 1000));
        mDurationTimeTv.setText(mOvertimeDetialVO.getDuration() + "小时");
        mOvertimeReasonContentTv.setText(mOvertimeDetialVO.getReason());
        mOverTimeContentTv.setText(mOvertimeDetialVO.getInfo());

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
                mApprovlaIdeaAdapter.setApprovalId(mOvertimeDetialVO.getOtid());
            }
            else{
                mApprovlaIdeaAdapter.updateData(mOvertimeDetialVO.getAppUser());
                mApprovalIdeaTv.setVisibility(View.VISIBLE);
                mApprovlaIdeaAdapter.setApprovalType(mOvertimeDetialVO.getGenre());
                mApprovlaIdeaAdapter.setApprovalId(mOvertimeDetialVO.getOtid());
            }
            
            if(!BSApplication.getInstance().getUserId().equals(mOvertimeDetialVO.getUserid())){
                mApprovlaIdeaAdapter.setViewCui(true);//不显示
            }
            else{
                mApprovlaIdeaAdapter.setViewCui(false);//显示
            }
        }
        if (mOvertimeDetialVO.getInsUser() != null) {
            mInformAdapter.updateData(mOvertimeDetialVO.getInsUser());
            mInformTv.setVisibility(View.VISIBLE);
            mInformLayout.setVisibility(View.VISIBLE);
        } else {
            mInformTv.setVisibility(View.GONE);
            mInformLayout.setVisibility(View.GONE);
        }

        

        try {
            if ("1".equals(mOvertimeDetialVO.getApproval())) {
                // 显示界面
                mApprovalViewFragment = new ApprovalViewFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.bottom_view, mApprovalViewFragment);
                transaction.commitAllowingStateLoss();
                // mApprovalViewFragment.setStatus(mOvertimeDetialVO.getStatus());
                mApprovalViewFragment.setApprovalid(mAlid);
                mApprovalViewFragment.setType(mType);
                mApprovalViewFragment.setCount(mCurrent);
            }
        } catch (Exception e) {
        }
        // 判断审核状态
        CommonUtils.setApprovalImg(Integer.parseInt(mOvertimeDetialVO.getStatus()), (ImageView) findViewById(R.id.approval_status), this);

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
        mTitleTv.setText("加班申请");
        mHeadIcon = (BSCircleImageView) findViewById(R.id.head_icon);
        mPersonTitle01 = (TextView) findViewById(R.id.person_title01);
        mPersonTitle02 = (TextView) findViewById(R.id.person_title02);
        mPersonTitle03 = (TextView) findViewById(R.id.person_title03);
        mPersonTitle04 = (TextView) findViewById(R.id.person_title04);

        mStratTimeTv = (TextView) findViewById(R.id.start_time_tv);
        mEndTimeTv = (TextView) findViewById(R.id.end_time_tv);
        mDurationTimeTv = (TextView) findViewById(R.id.duration_time_tv);

        mOvertimeReasonContentTv = (TextView) findViewById(R.id.overtime_reason_content);
        mOverTimeContentTv = (TextView) findViewById(R.id.overtime_content);

        mTransferGv = (BSGridView) findViewById(R.id.transfer_gv);
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
        initData();

        mListView = (ListView) findViewById(R.id.list_view);
        mApprovlaIdeaAdapter = new ApprovlaNewIdeaAdapter(this);
        mListView.setAdapter(mApprovlaIdeaAdapter);
        mApprovalIdeaTv = (TextView) findViewById(R.id.approval_idea_tv);
    }

    public void initData() {
        Intent intent = getIntent();
        mUid = intent.getStringExtra("uid");
        mAlid = intent.getStringExtra("alid");
        mType = intent.getStringExtra("type");
        if ("3".equals(mType)) {
            mTitleTv.setText("加班详情");
        }

        mMessageid = intent.getStringExtra("messageid");
    }

    @Override
    public void bindViewsListener() {

    }

    public boolean getData() {

        try {

            String strUlr = UrlUtil.getApprovalDetailUrl(Constant.APPROVAL_OVERTIME_DETAIL, mUid, mAlid, mMessageid);
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
}
