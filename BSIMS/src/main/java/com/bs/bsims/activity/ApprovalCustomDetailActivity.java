
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
import com.bs.bsims.model.CustomApprovalDetailVO;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.view.BSCircleImageView;
import com.bs.bsims.view.BSGridView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ApprovalCustomDetailActivity extends BaseActivity implements OnClickListener {
    public static final int CUSTOM_TYPE_ONE = 1;// EditText 类型 单行输入
    public static final int CUSTOM_TYPE_TWO = 2;// EditText 类型 多行输入
    public static final int CUSTOM_TYPE_THREE = 3;// Button点击弹出时间控件
    public static final int CUSTOM_TYPE_FOUR = 4;// EditText 只能输入数字
    public static final int CUSTOM_TYPE_FIVE = 5;// 下拉列表

    private String mUid;
    private String mAlid;
    private String mType;

    private CustomApprovalDetailVO mCustomApprovalDetailVO;

    private TextView mPersonTitle01, mPersonTitle02, mPersonTitle03, mPersonTitle04;
    private BSCircleImageView mHeadIcon;

    // 显示上传的图片
    private ImageView mDetailImg01, mDetailImg02, mDetailImg03;
    private List<ImageView> mListImag;
    private LinearLayout mPictureLayout;

    // 知会人，审批人
    private BSGridView mApproverGv, mInformGv;
    private HeadAdapter mApproverAdapter, mInformAdapter;
    private TextView mApproverTv, mApprovalGoTv, mInformTv, mInformGoTv, mCancel, mSure;
    private LinearLayout mApproverLayout, mInformLayout;

    private ListView mListView;
    private ApprovlaNewIdeaAdapter mApprovlaIdeaAdapter;
    private TextView mApprovalIdeaTv;
    private ApprovalViewFragment mApprovalViewFragment;

    private TextView mFeeTv;
    private int mCurrent = 0;
    private LinearLayout mLayout, mCustomLyaoutOne, mCustomLyaoutTwo, mCustomLyaoutThree, mCustomLyaoutFour, mCustomLyaoutFive;
    private String mMessageid;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.approval_custom_detail, mContentLayout);
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    @Override
    public void updateUi() {

    }

    // 显示自定义审批类型
    public void showCustomLayout() {
        List<CustomApprovalDetailVO> list = mCustomApprovalDetailVO.getArray().getOptions();
        for (int i = 0; i < list.size(); i++) {
            switch (Integer.parseInt(CommonUtils.isNormalData(list.get(i).getOtype()))) {
                case CUSTOM_TYPE_ONE:
                    mCustomLyaoutOne = (LinearLayout) View.inflate(this, R.layout.approval_custom_detail_1, null);
                    mCustomLyaoutOne.setTag(i);
                    mLayout.addView(mCustomLyaoutOne);

                    TextView tv_1 = (TextView) mCustomLyaoutOne.findViewById(R.id.type_title);
                    tv_1.setText(list.get(i).getOname() + "：");
                    TextView et_1 = (TextView) mCustomLyaoutOne.findViewById(R.id.type_content);
                    et_1.setText(list.get(i).getOvalue());

                    break;
                case CUSTOM_TYPE_TWO:
                    mCustomLyaoutTwo = (LinearLayout) View.inflate(this, R.layout.approval_custom_detail_2, null);
                    mLayout.addView(mCustomLyaoutTwo);
                    mCustomLyaoutTwo.setTag(i);

                    TextView tv_2 = (TextView) mCustomLyaoutTwo.findViewById(R.id.type_title);
                    tv_2.setText(list.get(i).getOname());

                    TextView et_2 = (TextView) mCustomLyaoutTwo.findViewById(R.id.type_content);
                    et_2.setText(list.get(i).getOvalue());
                    break;
                case CUSTOM_TYPE_THREE:
                    mCustomLyaoutThree = (LinearLayout) View.inflate(this, R.layout.approval_custom_detail_3, null);
                    mLayout.addView(mCustomLyaoutThree);
                    mCustomLyaoutThree.setTag(i);

                    TextView tv_3 = (TextView) mCustomLyaoutThree.findViewById(R.id.type_title);
                    tv_3.setText(list.get(i).getOname() + "：");

                    TextView et_3 = (TextView) mCustomLyaoutThree.findViewById(R.id.type_content);
                    et_3.setText(list.get(i).getOvalue());
                    break;
                case CUSTOM_TYPE_FOUR:
                    mCustomLyaoutFour = (LinearLayout) View.inflate(this, R.layout.approval_custom_detail_4, null);
                    mLayout.addView(mCustomLyaoutFour);
                    mCustomLyaoutFour.setTag(i);

                    TextView tv_4 = (TextView) mCustomLyaoutFour.findViewById(R.id.type_title);
                    tv_4.setText(list.get(i).getOname() + "：");
                    TextView et_4 = (TextView) mCustomLyaoutFour.findViewById(R.id.type_content);
                    et_4.setText(list.get(i).getOvalue());
                    break;
                case CUSTOM_TYPE_FIVE:
                    mCustomLyaoutFive = (LinearLayout) View.inflate(this, R.layout.approval_custom_detail_5, null);
                    mLayout.addView(mCustomLyaoutFive);
                    mCustomLyaoutFive.setTag(i);

                    TextView tv_5 = (TextView) mCustomLyaoutFive.findViewById(R.id.type_title);
                    tv_5.setText(list.get(i).getOname() + "：");

                    TextView et_5 = (TextView) mCustomLyaoutFive.findViewById(R.id.type_content);
                    et_5.setText(list.get(i).getOvalue());
                    break;

                default:

                    break;
            }
        }
    }

    @Override
    public void executeSuccess() {
        super.executeSuccess();
        CustomApprovalDetailVO customApprovalDetailVO = mCustomApprovalDetailVO.getArray();

        mTitleTv.setText(customApprovalDetailVO.getTypename() + "详情");
        mFeeTv.setText(customApprovalDetailVO.getTypename());

        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = CommonUtils.initImageLoaderOptions();
        imageLoader.displayImage(customApprovalDetailVO.getHeadpic(), mHeadIcon, options);
        mHeadIcon.setUserId(customApprovalDetailVO.getUserid());// 获取头像对应的用户ID
        mHeadIcon.setUserName(customApprovalDetailVO.getFullname());
        mHeadIcon.setUrl(customApprovalDetailVO.getHeadpic());
        mPersonTitle01.setText(customApprovalDetailVO.getFullname());
        mPersonTitle02.setText(customApprovalDetailVO.getPname() + "/" + customApprovalDetailVO.getDname());

        long time = Long.parseLong(customApprovalDetailVO.getTime()) * 1000;
        mPersonTitle03.setText(DateUtils.parseDate(time));

        showCustomLayout();

        DisplayImageOptions optionsPic = new DisplayImageOptions.Builder().showStubImage(R.drawable.common_ic_image_default).showImageForEmptyUri(R.drawable.common_ic_image_default)
                .showImageOnFail(R.drawable.common_ic_image_default).cacheInMemory().cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
        if (customApprovalDetailVO.getAnnexs() != null) {
            mPictureLayout.setVisibility(View.VISIBLE);
            List<CustomApprovalDetailVO> list = customApprovalDetailVO.getAnnexs();
            for (int i = 0; i < list.size(); i++) {
                if (i < 3) {
                    mListImag.get(i).setOnClickListener(this);
                    imageLoader.displayImage(list.get(i).getUrl(), mListImag.get(i), optionsPic);
                }

            }
        }

        if (customApprovalDetailVO.getAppUser() != null) {
            mApproverAdapter.updateData(customApprovalDetailVO.getAppUser());
            // mApproverLayout.setVisibility(View.VISIBLE);
            // mApproverTv.setVisibility(View.VISIBLE);
            // mApproverAdapter.setApproval(true);// 圆圈显示类型判断
            // mApproverAdapter.setStatus(mLeaveDetailVO.getStatus());

            // 刷新状态列表
            for (int i = 0; i < customApprovalDetailVO.getAppUser().size(); i++) {
                EmployeeVO vo = customApprovalDetailVO.getAppUser().get(i);
                if ("0".equals(vo.getStatus())) {
                    mCurrent++;
                }
            }

            if (customApprovalDetailVO.getOpinion() != null) {
                List<EmployeeVO> appUserList = new ArrayList<EmployeeVO>();
                appUserList.addAll(customApprovalDetailVO.getAppUser());
                for (int i = 0; i < customApprovalDetailVO.getAppUser().size(); i++) {
                    for (int j = 0; j < customApprovalDetailVO.getOpinion().size(); j++) {
                        if (customApprovalDetailVO.getOpinion().get(j).getUserid().equals(customApprovalDetailVO.getAppUser().get(i).getUserid())) {
                            appUserList.remove(i);
                            appUserList.add(i, customApprovalDetailVO.getOpinion().get(j));
                            continue;
                        }
                    }
                }
                mApprovlaIdeaAdapter.updateData(appUserList);
                mApprovalIdeaTv.setVisibility(View.VISIBLE);
                mApprovlaIdeaAdapter.setApprovalType(customApprovalDetailVO.getGetGenre());
                mApprovlaIdeaAdapter.setApprovalId(customApprovalDetailVO.getApprovalid());
            }
            else {
                mApprovlaIdeaAdapter.updateData(customApprovalDetailVO.getAppUser());
                mApprovalIdeaTv.setVisibility(View.VISIBLE);
                mApprovlaIdeaAdapter.setApprovalType(customApprovalDetailVO.getGetGenre());
                mApprovlaIdeaAdapter.setApprovalId(customApprovalDetailVO.getApprovalid());
            }
            
            if(!BSApplication.getInstance().getUserId().equals(customApprovalDetailVO.getUserid())){
                mApprovlaIdeaAdapter.setViewCui(true);//不显示
            }
            else{
                mApprovlaIdeaAdapter.setViewCui(false);//显示
            }
        }
        if (customApprovalDetailVO.getInsUser() != null) {
            mInformTv.setVisibility(View.VISIBLE);
            mInformLayout.setVisibility(View.VISIBLE);
            mInformAdapter.updateData(customApprovalDetailVO.getInsUser());
        } else {
            mInformTv.setVisibility(View.GONE);
            mInformLayout.setVisibility(View.GONE);
        }

        if ("1".equals(customApprovalDetailVO.getApproval())) {
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
        // 判断审核状态
        CommonUtils.setApprovalImg(Integer.parseInt(customApprovalDetailVO.getStatus()), (ImageView) findViewById(R.id.approval_status), this);
    }

    @Override
    public void executeFailure() {
        if (mCustomApprovalDetailVO == null) {
            super.executeFailure();
            return;
        } else {
            super.setNonetIcon(mCustomApprovalDetailVO.getRetinfo());
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

        mLayout = (LinearLayout) findViewById(R.id.custom_layout);
        initData();
    }

    public void initData() {
        Intent intent = this.getIntent();
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
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put("alid", mAlid);
            map.put("messageid", mMessageid);
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.CUSTOM_APPROVAL_DETAIL, map);
            Gson gson = new Gson();
            mCustomApprovalDetailVO = gson.fromJson(jsonStr, CustomApprovalDetailVO.class);
            if (Constant.RESULT_CODE.equals(mCustomApprovalDetailVO.getCode())) {
                HashMap<String, String> isreadMap = new HashMap<String, String>();
                isreadMap.put("isread", "1");
                isreadMap.put("approvalid", mAlid);
                CommonUtils.sendBroadcast(this, Constant.HOME_MSG, isreadMap);
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
    public void onClick(View arg0) {
        Intent intent = new Intent();
        List<CustomApprovalDetailVO> list = mCustomApprovalDetailVO.getArray().getAnnexs();
        ArrayList<String> urlList = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            urlList.add(list.get(i).getUrl());
        }
        intent.putStringArrayListExtra("piclist", urlList);
        intent.setClass(this, ImagePreviewActivity.class);
        startActivity(intent);
    }
}
