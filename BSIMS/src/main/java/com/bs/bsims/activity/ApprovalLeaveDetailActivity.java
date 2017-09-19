
package com.bs.bsims.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
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
import com.bs.bsims.model.LeaveDetailResultVO;
import com.bs.bsims.model.LeaveDetailVO;
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

public class ApprovalLeaveDetailActivity extends BaseActivity implements OnClickListener {
    private String mUid;
    private String mAlid;
    private String mType;

    private TextView mStratTimeTv, mEndTimeTv, mDurationTimeTv, mContentTv;
    private LeaveDetailVO mLeaveDetailVO;
    private ImageView mDetailImg01, mDetailImg02, mDetailImg03;
    private List<ImageView> mListImag;

    private BSGridView mTransferGv, mApproverGv, mInformGv;
    private HeadAdapter mTransferAdapter, mApproverAdapter, mInformAdapter;
    private TextView mApproverTv, mInformTv;
    private LinearLayout mApproverLayout, mInformLayout;

    private TextView mPersonTitle01, mPersonTitle02, mPersonTitle03, mPersonTitle04;
    private BSCircleImageView mHeadIcon;
    private LinearLayout mPictureLayout;

    private TextView mInforGoToTv;

    private ListView mListView;
    private ApprovlaNewIdeaAdapter mApprovlaIdeaAdapter;
    private TextView mApprovalIdeaTv;
    private ApprovalViewFragment mApprovalViewFragment;
    private View mPicturLayout;
    private int mCurrent = 0;

    private TextView mTypeTv;
    private String mMessageid;
    private LeaveDetailResultVO mLeaveDetailResultVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.approval_leave_detail, null);
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
        switch (Integer.parseInt(mLeaveDetailVO.getTypeid())) {
            case 1:
                mTypeTv.setText("事假");
                mTitleTv.setText("事假详情");
                break;
            case 2:
                mTypeTv.setText("病假");
                mTitleTv.setText("病假详情");
                break;
            case 3:
                mTypeTv.setText("陪(产假)");
                mTitleTv.setText("陪(产假)详情");
                break;
            case 4:
                mTypeTv.setText("公休假");
                mTitleTv.setText("公休详情");
                break;
            case 5:
                mTypeTv.setText("调休假");
                mTitleTv.setText("调休详情");
                break;
            case 6:
                mTypeTv.setText("婚假");
                mTitleTv.setText("婚假详情");
                break;
            case 7:
                mTypeTv.setText("丧假");
                mTitleTv.setText("丧假详情");
                break;

            default:
                break;
        }

        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = CommonUtils.initImageLoaderOptions();
        DisplayImageOptions optionsPic = new DisplayImageOptions.Builder().showStubImage(R.drawable.common_ic_image_default).showImageForEmptyUri(R.drawable.common_ic_image_default)
                .showImageOnFail(R.drawable.common_ic_image_default).cacheInMemory().cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
        imageLoader.displayImage(mLeaveDetailVO.getHeadpic(), mHeadIcon, options);
        mHeadIcon.setUserId(mLeaveDetailVO.getUserid());// 获取头像对应的用户ID
        mHeadIcon.setUserName(mLeaveDetailVO.getFullname());
        mHeadIcon.setUrl(mLeaveDetailVO.getHeadpic());
        mPersonTitle01.setText(mLeaveDetailVO.getFullname());
        mPersonTitle02.setText(mLeaveDetailVO.getPname() + "/" + mLeaveDetailVO.getDname());

        long time = Long.parseLong(mLeaveDetailVO.getTime()) * 1000;
        mPersonTitle03.setText(DateUtils.parseDate(time));
        // mPersonTitle04.setText(mLeaveDetailVO.getDname());
        long startTime = Long.parseLong(mLeaveDetailVO.getStarttime()) * 1000;
        mStratTimeTv.setText(CommonDateUtils.parseDate(startTime,
                "yyyy-MM-dd HH:mm"));
        long endTime = Long.parseLong(mLeaveDetailVO.getEndtime()) * 1000;
        mEndTimeTv.setText(CommonDateUtils.parseDate(endTime,
                "yyyy-MM-dd HH:mm"));
        mDurationTimeTv.setText(mLeaveDetailVO.getTimeShow());
        mContentTv.setText(mLeaveDetailVO.getContent());
        if (mLeaveDetailVO.getImgs() != null) {
            mPictureLayout.setVisibility(View.VISIBLE);
            List<String> list = mLeaveDetailVO.getImgs();
            for (int i = 0; i < list.size(); i++) {
                if (i < 3) {
                    mListImag.get(i).setOnClickListener(this);
                    imageLoader.displayImage(list.get(i), mListImag.get(i), optionsPic);
                }

            }
        }

        if (mLeaveDetailVO.getHandUser() != null)
            mTransferAdapter.updateData(mLeaveDetailVO.getHandUser());
        if (mLeaveDetailVO.getAppUser() != null) {
            mApproverAdapter.updateData(mLeaveDetailVO.getAppUser());
            // mApproverLayout.setVisibility(View.VISIBLE);
            // mApproverTv.setVisibility(View.VISIBLE);
            // mApproverAdapter.setApproval(true);// 圆圈显示类型判断
            // mApproverAdapter.setStatus(mLeaveDetailVO.getStatus());

            // 刷新状态列表
            for (int i = 0; i < mLeaveDetailVO.getAppUser().size(); i++) {
                EmployeeVO vo = mLeaveDetailVO.getAppUser().get(i);
                if ("0".equals(vo.getStatus())) {
                    mCurrent++;
                }
            }

            if (mLeaveDetailVO.getOpinion() != null) {
                List<EmployeeVO> appUserList = new ArrayList<EmployeeVO>();
                appUserList.addAll(mLeaveDetailVO.getAppUser());
                for (int i = 0; i < mLeaveDetailVO.getAppUser().size(); i++) {
                    for (int j = 0; j < mLeaveDetailVO.getOpinion().size(); j++) {
                        if (mLeaveDetailVO.getOpinion().get(j).getUserid().equals(mLeaveDetailVO.getAppUser().get(i).getUserid())) {
                            appUserList.remove(i);
                            appUserList.add(i, mLeaveDetailVO.getOpinion().get(j));
                            continue;
                        }
                    }
                }
                mApprovlaIdeaAdapter.updateData(appUserList);
                mApprovalIdeaTv.setVisibility(View.VISIBLE);
                mApprovlaIdeaAdapter.setApprovalType(mLeaveDetailVO.getGenre());
                mApprovlaIdeaAdapter.setApprovalId(mLeaveDetailVO.getAskleaveid());
            }
            else{
                mApprovlaIdeaAdapter.updateData(mLeaveDetailVO.getAppUser());
                mApprovalIdeaTv.setVisibility(View.VISIBLE);
                mApprovlaIdeaAdapter.setApprovalType(mLeaveDetailVO.getGenre());
                mApprovlaIdeaAdapter.setApprovalId(mLeaveDetailVO.getAskleaveid());
            }
            
            if(!BSApplication.getInstance().getUserId().equals(mLeaveDetailVO.getUserid())){
                mApprovlaIdeaAdapter.setViewCui(true);//不显示
            }
            else{
                mApprovlaIdeaAdapter.setViewCui(false);//显示
            }
            
            
        }

        if (mLeaveDetailVO.getInsUser() != null) {
            mInformLayout.setVisibility(View.VISIBLE);
            mInformTv.setVisibility(View.VISIBLE);
            mInformAdapter.updateData(mLeaveDetailVO.getInsUser());
        } else {
            mInformLayout.setVisibility(View.GONE);
            mInformTv.setVisibility(View.GONE);
        }

        try {
            if ("1".equals(mLeaveDetailVO.getApproval())) {
                // 显示界面
                mApprovalViewFragment = new ApprovalViewFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.bottom_view, mApprovalViewFragment);
                transaction.commitAllowingStateLoss();
                // mApprovalViewFragment.setStatus(mLeaveDetailVO.getStatus());
                mApprovalViewFragment.setApprovalid(mAlid);
                mApprovalViewFragment.setType(mType);
                mApprovalViewFragment.setCount(mCurrent);
            }
        } catch (Exception e) {
        }
        
        // 判断审核状态
        CommonUtils.setApprovalImg(Integer.parseInt(mLeaveDetailVO.getStatus()), (ImageView) findViewById(R.id.approval_status), this);

    }

    @Override
    public void executeFailure() {
        if (mLeaveDetailResultVO == null) {
            super.executeFailure();
            return;
        } else {
            super.setNonetIcon(mLeaveDetailResultVO.getRetinfo());
        }
    }

    @Override
    public void initView() {

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);

        float screenWidth = wm.getDefaultDisplay().getWidth();
        float height = wm.getDefaultDisplay().getHeight();

        mPictureLayout = (LinearLayout) findViewById(R.id.picture_layout);
        mHeadIcon = (BSCircleImageView) findViewById(R.id.head_icon);
        mPersonTitle01 = (TextView) findViewById(R.id.person_title01);
        mPersonTitle02 = (TextView) findViewById(R.id.person_title02);
        mPersonTitle03 = (TextView) findViewById(R.id.person_title03);
        mPersonTitle04 = (TextView) findViewById(R.id.person_title04);

        mStratTimeTv = (TextView) findViewById(R.id.start_time_tv);
        mEndTimeTv = (TextView) findViewById(R.id.end_time_tv);
        mDurationTimeTv = (TextView) findViewById(R.id.duration_time_tv);
        mContentTv = (TextView) findViewById(R.id.content_tv);

        mDetailImg01 = (ImageView) findViewById(R.id.detial_img_01);
        mDetailImg02 = (ImageView) findViewById(R.id.detial_img_02);
        mDetailImg03 = (ImageView) findViewById(R.id.detial_img_03);
        mListImag = new ArrayList<ImageView>();
        mListImag.add(mDetailImg01);
        mListImag.add(mDetailImg02);
        mListImag.add(mDetailImg03);

        mTransferGv = (BSGridView) findViewById(R.id.transfer_gv);
        mApproverGv = (BSGridView) findViewById(R.id.approver_gv);
        mInformGv = (BSGridView) findViewById(R.id.inform_gv);
        mTransferAdapter = new HeadAdapter(this, false);
        mApproverAdapter = new HeadAdapter(this, false);
        mInformAdapter = new HeadAdapter(this, false, true);
        mTransferGv.setAdapter(mTransferAdapter);
        mApproverGv.setAdapter(mApproverAdapter);
        mInformGv.setAdapter(mInformAdapter);

        mApproverTv = (TextView) findViewById(R.id.approver_tv);
        mInformTv = (TextView) findViewById(R.id.inform_people_tv);
        mApproverLayout = (LinearLayout) findViewById(R.id.approver_layout);
        mInformLayout = (LinearLayout) findViewById(R.id.inform_people_layout);
        mInforGoToTv = (TextView) findViewById(R.id.inform_go_tv);
        mTypeTv = (TextView) findViewById(R.id.title_tv);
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
        mMessageid = intent.getStringExtra("messageid");
    }

    @Override
    public void bindViewsListener() {

    }

    public boolean getData() {

        try {
            String strUlr = UrlUtil.getApprovalLeaveDetailUrl(Constant.APPROVAL_LEAVE_DETAIL, mAlid, mUid, mMessageid);
            String jsonStr = HttpClientUtil.get(strUlr, Constant.ENCODING).trim();
            Gson gson = new Gson();
            mLeaveDetailResultVO = gson.fromJson(jsonStr, LeaveDetailResultVO.class);
            if (Constant.RESULT_CODE.endsWith(mLeaveDetailResultVO.getCode())) {
                mLeaveDetailVO = mLeaveDetailResultVO.getArray();
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
        ArrayList<String> list = mLeaveDetailVO.getImgs();
        intent.putStringArrayListExtra("piclist", list);
        intent.setClass(this, ImagePreviewActivity.class);
        startActivity(intent);
    }

}
