
package com.bs.bsims.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.CrmVisitorImgSelectAdapter;
import com.bs.bsims.adapter.HeadAdapter;
import com.bs.bsims.adapter.NewDiscussAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.fragment.NewCommentFragment;
import com.bs.bsims.model.DiscussResultVO;
import com.bs.bsims.model.DiscussVO;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.model.JournalListVO1;
import com.bs.bsims.model.JournalPublishDetailVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CommonUtils.ResultCallback;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.utils.UrlUtil;
import com.bs.bsims.view.BSCircleImageView;
import com.bs.bsims.view.BSGridView;
import com.bs.bsims.view.BSListView;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JournalPublishDetailActivity extends BaseActivity implements OnClickListener, OnTouchListener, OnGestureListener {
    private String TAG = "JournalDetailActivity";
    public static final int SUCCESS = 1;
    public static final int FAIL = 2;
    private TextView mShowGrayBackground;
    private LinearLayout mTimeLayout;
    private TextView mStartTime, mEndTime;
    private BSCircleImageView mHeadIcon;
    private TextView mNameTv, mDepartmentTv, mTimeTv;// 姓名、部门、时间
    private ImageView mSexView;//人员性别
    private TextView mFootMark, mAttention;// 足迹、关注
    private LinearLayout mCompleteLayout, mExperienceLayout;
    private TextView mWorkExperienceTitle, mWorkPlanTitle;
    private TextView mWorkExperience, mWorkPlan, mCompleteContent, mExperienceContent;// 工作经验、工作计划
    private BSGridView mPictureGride;// 图片
    private List<String> mResetImageList;// 避免发布、详情中图片顺序不一致
    private CrmVisitorImgSelectAdapter crmVimgsapdater;
    private TextView mShowLine;
    private LinearLayout mShareLayout;
    private LinearLayout mCcLayout, mCczLayout;
    private TextView mCcTitleTv, mCczTitleTv;
    private ImageView mLineImg;// 抄送者、抄送对象切换下标线
    private int moveDistance;// two用于标记动画偏移位置
    private int index = 1; // 作为一种判断依据，1表示左边，2表示右边
    private BSGridView mCcGv, mCczGv;// 第一个是抄送对象，第二个是抄送者
    private HeadAdapter mCcAdapter, mCczAdapter;
    private TextView mDiscussTv;
    private BSListView mDiscussLv;
    private NewDiscussAdapter mDiscussAdapter;

    private JournalPublishDetailVO mJournalDetailResltVO;
    private ArrayList<EmployeeVO> mBossCcVOList;
    private ArrayList<EmployeeVO> mCcVOList, mCczVOList;
    private DiscussResultVO mDiscussResultVO;
    private List<DiscussVO> mDisscussVOList = new ArrayList<DiscussVO>();

    private NewCommentFragment fragment;
    protected List<EmployeeVO> mDataList = new ArrayList<EmployeeVO>();// 抄送人集合，在newCommentFragment界面用到
    private StringBuffer mSb = new StringBuffer();

    // 加载更多
    private View mFootLayout;
    private TextView mMoreTextView;
    private ProgressBar mProgressBar;
    private int mState = 0;// 0为首次,1为上拉刷新 ，2为下拉刷新

    private boolean mFlag = true;

    // 滑动上下篇
    private ScrollView mDetailLayout;
    private GestureDetector detector;
    /** 触摸时按下的点 **/
    PointF downP = new PointF();
    /** 触摸时当前的点 **/
    PointF curP = new PointF();

    // 上下滑翻页查看详情
    private List<JournalListVO1> mListVo;
    private String mLogid;
    private String mType;// 类型(接收传过来的)
    private String mDid;// 部门id
    private String mIsMyFavor;// 我关注的
    private String mIsRelated;//关于我的
    private String mLoguid;// 用户的ID
    private String mIsremind;// 日志提醒
    private String mKeyWord;// 关键字

    private String mAttentionType = "1";// 提交关注与否时用：1,关注；2,取消关注
    private String mFootType = "";// 跳转到足迹界面时用到
    private String mPersonal = "0";// 表明详情界面展现的是个人日志列表详情数据
    private boolean mFinishFlag = false;// 避免数据未加载完，返回出现空指针
    private View mCurrentLayout;
    private String UPDATE_ATTENTION = "update_attention";// 当改变关注状态时，发送广播
    private boolean mIsAttention = false;

    private PopupWindow mOkPop, mFootPop, mAttentionPop, mLeftPop;
    private List<PopupWindow> mListpop = new ArrayList<PopupWindow>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("NewApi")
    @Override
    public void initView() {
        mOkTv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.jounal_detail_to_person), null, null, null);
        mTimeLayout = (LinearLayout) findViewById(R.id.time_layout);
        mStartTime = (TextView) findViewById(R.id.start_time);
        mEndTime = (TextView) findViewById(R.id.end_time);
        mShowGrayBackground = (TextView) findViewById(R.id.show_gray_background);
        mHeadIcon = (BSCircleImageView) findViewById(R.id.head_icon);
        mNameTv = (TextView) findViewById(R.id.name_tv);
        mDepartmentTv = (TextView) findViewById(R.id.department_tv);
        mTimeTv = (TextView) findViewById(R.id.time_tv);
        mFootMark = (TextView) findViewById(R.id.foot_mark);
        mAttention = (TextView) findViewById(R.id.attention);
        mWorkExperienceTitle = (TextView) findViewById(R.id.work_experience_title);
        mWorkExperience = (TextView) findViewById(R.id.work_experience_content);
        mWorkPlanTitle = (TextView) findViewById(R.id.tomorrow_plan_title);
        mWorkPlan = (TextView) findViewById(R.id.tomorrow_plan_content);
        mCompleteLayout = (LinearLayout) findViewById(R.id.complete_layout);
        mExperienceLayout = (LinearLayout) findViewById(R.id.experience_layout);
        mCompleteContent = (TextView) findViewById(R.id.complete_content);
        mExperienceContent = (TextView) findViewById(R.id.experience_content);
        mShowLine = (TextView) findViewById(R.id.show_line);
        mPictureGride = (BSGridView) findViewById(R.id.image_gride);
        mSexView = (ImageView) findViewById(R.id.person_sex);
        crmVimgsapdater = new CrmVisitorImgSelectAdapter(this);
        mPictureGride.setAdapter(crmVimgsapdater);
        mLineImg = (ImageView) findViewById(R.id.line_img);

        mShareLayout = (LinearLayout) findViewById(R.id.share_layout);
        mCczGv = (BSGridView) findViewById(R.id.ccz_gv);
        mCczLayout = (LinearLayout) findViewById(R.id.ccz_layout);
        mCczTitleTv = (TextView) findViewById(R.id.ccz_title_tv);

        mCcGv = (BSGridView) findViewById(R.id.send_second_person_gv);
        mCcLayout = (LinearLayout) findViewById(R.id.cc_layout);
        mCcTitleTv = (TextView) findViewById(R.id.cc_title_tv);

        // 抄送者
        mCczAdapter = new HeadAdapter(this, false, true);
        mCczGv.setAdapter(mCczAdapter);

        // 抄送对象
        mCcAdapter = new HeadAdapter(this, false, true);
        mCcGv.setAdapter(mCcAdapter);

        mDiscussTv = (TextView) findViewById(R.id.discuss);
        mDiscussLv = (BSListView) findViewById(R.id.list_view);
        initFoot();
        initData();
        // 评论
        mDiscussAdapter = new NewDiscussAdapter(this, mLogid, fragment, mDiscussTv, "1");
        mDiscussLv.setAdapter(mDiscussAdapter);
        mDiscussAdapter.setContentUrl(BSApplication.getInstance().getHttpTitle() + Constant.JOURNAL_COMMIT_DISSCUSS_URL);

        mDetailLayout = (ScrollView) findViewById(R.id.detial_layout);
        detector = new GestureDetector(this);
        mDetailLayout.setOnTouchListener(this);

        // 下面两个要记得设哦，不然就没法处理轻触以外的事件了，例如抛掷动作。

        mDetailLayout.setLongClickable(true);
        detector.setIsLongpressEnabled(true);
    }

    public void initLeadView() {
        LinearLayout.LayoutParams fmTvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);// 定义文本显示组件
        LinearLayout.LayoutParams fmTvImgParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, CommonUtils.dip2px(this, 170));// 定义文本显示组件
        mFootPop = CommonUtils.leadPop(JournalPublishDetailActivity.this, mFootMark, "查看一天的工作行程", fmTvParams, fmTvImgParams, 1);

        if (mAttention.getVisibility() == View.VISIBLE) {
            LinearLayout.LayoutParams attentionTvParams = new LinearLayout.LayoutParams(CommonUtils.dip2px(this, 180), LinearLayout.LayoutParams.WRAP_CONTENT);// 定义文本显示组件
            LinearLayout.LayoutParams attentionImgParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, CommonUtils.dip2px(this, 90));// 定义文本显示组件
            mAttentionPop = CommonUtils.leadPop(JournalPublishDetailActivity.this, mAttention, "关注此人，可以从关注列表快速查看此人日志", attentionTvParams, attentionImgParams, 1);
        }

        LinearLayout.LayoutParams okTvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);// 定义文本显示组件
        okTvParams.rightMargin = CommonUtils.dip2px(this, 10);
        LinearLayout.LayoutParams okImgParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, CommonUtils.dip2px(this, 80));// 定义文本显示组件
        okImgParams.gravity = Gravity.RIGHT;
        okImgParams.rightMargin = CommonUtils.dip2px(this, 10) + CommonUtils.getViewWidth(mOkTv) / 2;
        mOkPop = CommonUtils.leadPop(JournalPublishDetailActivity.this, mOkTv, "查看个人历史日志", okTvParams, okImgParams, 1);

        LinearLayout.LayoutParams leftParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);// 定义文本显示组件
        // leftParams.rightMargin = CommonUtils.dip2px(this, 10);
        LinearLayout.LayoutParams leftImgParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 200);// 定义文本显示组件
        okImgParams.gravity = Gravity.RIGHT;
        okImgParams.rightMargin = CommonUtils.dip2px(this, 10) + CommonUtils.getViewWidth(mOkTv) / 2;
        mLeftPop = CommonUtils.leadPop(JournalPublishDetailActivity.this, mOkTv, "左右滑动，可以查看上一篇，下一篇日志", leftParams, leftImgParams, 3);

        mListpop.add(mFootPop);
        if (mAttention.getVisibility() == View.VISIBLE)
            mListpop.add(mAttentionPop);
        mListpop.add(mOkPop);
        mListpop.add(mLeftPop);
        CommonUtils.okLeadPop(this, mContentLayout, mListpop, 0);
    }

    public void initFragment() {
        try {
            // 显示界面
            fragment = new NewCommentFragment(mJournalDetailResltVO, mDataList);
            Bundle bundle = new Bundle();
            /*
             * 这里的key必须是ReceiverAction.Key value就是你注册广播时候的actiion，
             * 手动在org.baiteng.oa.setting.ReceiverAction中添加此activity的action
             */
            bundle.putString(Constant.DocKey, Constant.NoticeDetailsAction);
            fragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.bottom_view, fragment);
            transaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initData() {
        Intent intent = getIntent();
        // 接收日志列表传过来的参数
        mListVo = (List<JournalListVO1>) intent.getSerializableExtra("listvo");
        mType = intent.getStringExtra("type");
        mLogid = getIntent().getStringExtra("logid");
        mDid = getIntent().getStringExtra("did");
        mIsMyFavor = getIntent().getStringExtra("ismyfavor");
        mIsRelated = getIntent().getStringExtra("isrelated");
        mLoguid = getIntent().getStringExtra("loguid");
        mIsremind = getIntent().getStringExtra("isremind");
        mKeyWord = getIntent().getStringExtra("keyword");

        mFootMark.setBackgroundDrawable(CommonUtils.setBackgroundShap(this, 5, "#33B7FF", "#33B7FF"));
        // 接收筛选个人日志列表界面传过来的参数
        mPersonal = getIntent().getStringExtra("personal");
        if ("1".equals(mPersonal)) {
            mOkTv.setVisibility(View.GONE);
        }
        initLineMove();
    }

    public void initLineMove() {
        Display currDisplay = getWindowManager().getDefaultDisplay();// 获取屏幕当前分辨率
        int displayWidth = currDisplay.getWidth();
        int displayHeight = currDisplay.getHeight();
        moveDistance = displayWidth / 3; // 设置水平动画平移大小

        LayoutParams para = mLineImg.getLayoutParams();
        para.width = displayWidth / 3;
        mLineImg.setLayoutParams(para);
    }

    @Override
    public void bindViewsListener() {
        mOkTv.setOnClickListener(this);
        mHeadBackImag.setOnClickListener(this);
        mFootMark.setOnClickListener(this);
        mAttention.setOnClickListener(this);
        mCcTitleTv.setOnClickListener(this);
        mCczTitleTv.setOnClickListener(this);

        bindRefreshListener();

        mDiscussTv.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                String str = mDiscussTv.getText().toString();
                if (mListVo == null)
                    return;
                for (int i = 0; i < mListVo.size(); i++) {
                    if (mJournalDetailResltVO.getInfo().getLogid().equals(mListVo.get(i).getLogid())) {
                        mListVo.get(i).setCommentCount(str.substring(str.indexOf("(") + 1, str.indexOf(")")));
                    }
                }
            }
        });

        // 点击图片，调到另一个浏览
        mPictureGride.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.putStringArrayListExtra("piclist", (ArrayList<String>) mResetImageList);
                intent.setClass(JournalPublishDetailActivity.this, ImagePreviewActivity.class);
                intent.putExtra("imgIndex", arg2);
                startActivity(intent);
            }
        });

    }

    public boolean getData() {
        if (0 == mState) {
            return getData("", "");
        } else if (2 == mState) {
            String id = mDiscussAdapter.mList.get(mDiscussAdapter.mList.size() - 1).getCommentid();
            return getData(Constant.LASTID, id);
        }
        return false;
    }

    public boolean getData(String refresh, String id) {
        try {
            Gson gson = new Gson();
            if (mFlag) {
                Map map = new HashMap<String, String>();
                map.put("userid", BSApplication.getInstance().getUserId());
                map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
                map.put("logid", mLogid);
                map.put("type", mType);
                map.put("did", mDid);
                map.put("ismyfavor", mIsMyFavor);
                map.put("isrelated", mIsRelated);
                map.put("loguid", mLoguid);
                map.put("isremind", mIsremind);
                map.put("keyword", mKeyWord);

                String jsonUrlStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.JOURNAL_PUBLISH_DETAIL, map);
                mJournalDetailResltVO = gson.fromJson(jsonUrlStr, JournalPublishDetailVO.class);

                // 抄送者
                if (mJournalDetailResltVO.getInfo().getCcz() != null) {
                    mCczVOList = new ArrayList<EmployeeVO>();
                    for (int i = 0; i < mJournalDetailResltVO.getInfo().getCcz().size(); i++) {
                        EmployeeVO vo = new EmployeeVO();
                        vo.setFullname(mJournalDetailResltVO.getInfo().getCcz().get(i).getFullname());
                        vo.setHeadpic(mJournalDetailResltVO.getInfo().getCcz().get(i).getHeadpic());
                        vo.setIsread(mJournalDetailResltVO.getInfo().getCcz().get(i).getIsread());
                        vo.setUserid(mJournalDetailResltVO.getInfo().getCcz().get(i).getUserid());// HL加字段
                        mCczVOList.add(vo);
                    }
                    mCczAdapter.mList.clear();
                    mCczAdapter.mList.addAll(mCczVOList);
                } else {
                    mCczVOList = null;
                }
                // boss抄送
                if (mJournalDetailResltVO.getInfo().getBoss_cc() != null) {
                    mBossCcVOList = new ArrayList<EmployeeVO>();
                    for (int i = 0; i < mJournalDetailResltVO.getInfo().getBoss_cc().size(); i++) {
                        EmployeeVO vo = new EmployeeVO();
                        vo.setFullname(mJournalDetailResltVO.getInfo().getBoss_cc().get(i).getFullname());
                        vo.setHeadpic(mJournalDetailResltVO.getInfo().getBoss_cc().get(i).getHeadpic());
                        vo.setIsread(mJournalDetailResltVO.getInfo().getBoss_cc().get(i).getIsread());
                        vo.setUserid(mJournalDetailResltVO.getInfo().getBoss_cc().get(i).getUserid());// HL加字段
                        mBossCcVOList.add(vo);
                    }
                } else {
                    mBossCcVOList = null;
                }
                // 抄送对象
                if (mJournalDetailResltVO.getInfo().getCc() != null) {
                    mCcVOList = new ArrayList<EmployeeVO>();
                    for (int i = 0; i < mJournalDetailResltVO.getInfo().getCc().size(); i++) {
                        EmployeeVO vo = new EmployeeVO();
                        vo.setFullname(mJournalDetailResltVO.getInfo().getCc().get(i).getFullname());
                        vo.setHeadpic(mJournalDetailResltVO.getInfo().getCc().get(i).getHeadpic());
                        vo.setIsread(mJournalDetailResltVO.getInfo().getCc().get(i).getIsread());
                        vo.setUserid(mJournalDetailResltVO.getInfo().getCc().get(i).getUserid());// HL加字段
                        mCcVOList.add(vo);
                    }

                } else {
                    mCcVOList = null;
                }
                // 将boss抄送对象合并到一起
                if (mBossCcVOList != null) {
                    if (mCcVOList == null) {
                        mCcVOList = new ArrayList<EmployeeVO>();
                    }
                    mCcVOList.addAll(mBossCcVOList);
                }
                if (mCcVOList != null) {
                    mCcAdapter.mList.clear();
                    mCcAdapter.mList.addAll(mCcVOList);
                }

            }
            // 评论数据接口
            String discussUrl = UrlUtil.getJournalDisscussUrl(Constant.JOURNAL_DISCUSS, mLogid, refresh, id);
            String discussJson = HttpClientUtil.get(discussUrl, Constant.ENCODING).trim();
            mDiscussResultVO = gson.fromJson(discussJson, DiscussResultVO.class);

            if (Constant.RESULT_CODE.equals(mDiscussResultVO.getCode())) {
                mDisscussVOList.clear();
                if (Constant.LASTID.equals(refresh)) {
                    mDisscussVOList.addAll(mDiscussResultVO.getArray());
                } else {
                    mDisscussVOList = mDiscussResultVO.getArray();
                }
            }

            if (Constant.RESULT_CODE.equals(mJournalDetailResltVO.getCode())) {
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
    public void executeFailure() {
        mLoadingLayout.setVisibility(View.VISIBLE);
        mContentLayout.setVisibility(View.GONE);
        if (mJournalDetailResltVO == null) {
            super.showNoNetView();
        } else {
            super.showNoContentView();
        }

    }

    public void updateData() {

        CustomDialog.closeProgressDialog();
        if (mFlag) {
            initFragment();
            ImageLoader imageloader = ImageLoader.getInstance();
            JournalPublishDetailVO contentVO = mJournalDetailResltVO.getInfo();
            String type = contentVO.getType();
            mFootType = type;
            if ("0".equals(type)) {
                mTitleTv.setText("日志详情");
                mWorkExperienceTitle.setText("今日工作总结");
                mWorkPlanTitle.setText("明日工作计划");
                mTimeLayout.setVisibility(View.GONE);
                mShowGrayBackground.setVisibility(View.GONE);
            } else if ("1".equals(type) || "2".equals(type)) {
                if ("1".equals(type)) {
                    mTitleTv.setText("周报详情");
                    mWorkExperienceTitle.setText("本周工作总结");
                    mWorkPlanTitle.setText("下周工作计划");
                } else if ("2".equals(type)) {
                    mTitleTv.setText("月报详情");
                    mWorkExperienceTitle.setText("本月工作总结");
                    mWorkPlanTitle.setText("下月工作计划");
                }
                mTimeLayout.setVisibility(View.VISIBLE);
                mShowGrayBackground.setVisibility(View.VISIBLE);
                mStartTime.setText(DateUtils.parseDateDay(contentVO.getStarttime()));
                mEndTime.setText(DateUtils.parseDateDay(contentVO.getEndtime()));
            }

            if(contentVO.getSex().equals("男")){
                mSexView.setImageResource(R.drawable.sex_man);
            }
            else{
                mSexView.setImageResource(R.drawable.sex_woman);
            }
            mNameTv.setText(contentVO.getFullname());
            mHeadIcon.setUserName(contentVO.getFullname());
            mHeadIcon.setUserId(contentVO.getLoguid());// HL
            mHeadIcon.setmImageLoader(imageloader);
            mHeadIcon.setUrl(contentVO.getHeadpic());
            mDepartmentTv.setText(contentVO.getDname() + "/" + contentVO.getPositionname());
            imageloader.displayImage(contentVO.getHeadpic(), mHeadIcon, CommonUtils.initImageLoaderOptions());
            mTimeTv.setText(DateUtils.parseMDHM(contentVO.getTime()));
            mWorkExperience.setText(contentVO.getContent1());
            replaceText(mWorkExperience);
            mWorkPlan.setText(contentVO.getContent4());
            replaceText(mWorkPlan);
            mCompleteContent.setText(contentVO.getContent2());
            replaceText(mCompleteContent);
            mExperienceContent.setText(contentVO.getContent3());
            replaceText(mExperienceContent);
            if (CommonUtils.isNormalString(contentVO.getContent2())) {
                mCompleteLayout.setVisibility(View.VISIBLE);
            } else {
                mCompleteLayout.setVisibility(View.GONE);
            }
            if (CommonUtils.isNormalString(contentVO.getContent3())) {
                mExperienceLayout.setVisibility(View.VISIBLE);
            } else {
                mExperienceLayout.setVisibility(View.GONE);
            }

            if ("0".equals(contentVO.getIsfavor())) {
                mAttention.setVisibility(View.VISIBLE);
                mAttention.setText("关注");
                mAttention.setBackgroundDrawable(CommonUtils.setBackgroundShap(this, 5, "#FFA200", "#FFA200"));
                mAttentionType = "1";
            } else if ("1".equals(contentVO.getIsfavor())) {
                mAttention.setVisibility(View.VISIBLE);
                mAttention.setText("取消关注");
                mAttention.setBackgroundDrawable(CommonUtils.setBackgroundShap(this, 5, R.color.C3_1, R.color.C3_1));
                mAttentionType = "2";
            } else if ("2".equals(contentVO.getIsfavor())) {
                mAttention.setVisibility(View.GONE);
            }

            // 展现备注下面的图片
            if (contentVO.getImgs() == null) {
                mPictureGride.setVisibility(View.GONE);
                mShowLine.setVisibility(View.GONE);
            } else {
                mPictureGride.setVisibility(View.VISIBLE);
                mShowLine.setVisibility(View.VISIBLE);
                crmVimgsapdater.updateList(reSetImage(contentVO.getImgs()));
            }

            if (mCczVOList != null) {
                mShareLayout.setVisibility(View.VISIBLE);
                // mCczLayout.setVisibility(View.VISIBLE);
                mCczTitleTv.setVisibility(View.VISIBLE);
                mCczAdapter.updateData(mCczVOList);
                mCczAdapter.notifyDataSetChanged();
            } else {
                mCczTitleTv.setVisibility(View.GONE);
                mCczLayout.setVisibility(View.GONE);
                mShareLayout.setVisibility(View.GONE);
            }

            if (mCcVOList != null) {
                mCczLayout.setVisibility(View.GONE);
                mCcLayout.setVisibility(View.VISIBLE);
                mCcTitleTv.setVisibility(View.VISIBLE);
                mCcAdapter.updateData(mCcVOList);
                mCcAdapter.notifyDataSetChanged();

            } else {
                mCcTitleTv.setVisibility(View.GONE);
                mCcLayout.setVisibility(View.GONE);
            }

            if (mCcVOList != null && mCczVOList != null) {
                mCcTitleTv.setText("抄送对象(" + mCcVOList.size() + ")");
                mCczTitleTv.setText("抄送者(" + mCczVOList.size() + ")");
                startAnim(3);
                index = 1;
            }

            if (mDiscussResultVO.getArray() != null) {
                mDiscussLv.setVisibility(View.VISIBLE);
                mDiscussTv.setVisibility(View.VISIBLE);
                String discussStr = String.format(getResources().getString(R.string.discuss), Integer.parseInt(mJournalDetailResltVO.getInfo().getCommentCount()));
                mDiscussTv.setText(discussStr);
                mDiscussAdapter.updateData(mDisscussVOList);
                footViewIsVisibility(mDiscussAdapter.mList);
            } else {
                mDiscussTv.setVisibility(View.GONE);
                mDisscussVOList.clear();
                mDiscussAdapter.updateData(mDisscussVOList);
                footViewIsVisibility(mDiscussAdapter.mList);
            }

            if (mListVo != null) {
                for (int i = 0; i < mListVo.size(); i++) {
                    if (mJournalDetailResltVO.getInfo().getLogid().equals(mListVo.get(i).getLogid())) {
                        mListVo.get(i).setIsread("1");
                    }
                }
            }

            mFlag = false;
        } else {
            if (mDiscussResultVO.getArray() != null) {
                mDiscussLv.setVisibility(View.VISIBLE);
                mDiscussTv.setVisibility(View.VISIBLE);
                mDiscussAdapter.updateDataLast(mDisscussVOList);
                footViewIsVisibility(mDiscussAdapter.mList);
            } else {
                mDiscussTv.setVisibility(View.GONE);
            }
        }

        mFinishFlag = true;
    }

    // 发布图片中的第三张图片总跑到第一位
    public List<String> reSetImage(List<String> imageList) {
        mResetImageList = new ArrayList<String>();
        int count = imageList.size();
        if (count == 3) {
            for (int i = 0; i < count; i++) {
                if (i == 0 || i == 1) {
                    mResetImageList.add(imageList.get(i + 1));
                } else if (i == 2) {
                    mResetImageList.add(imageList.get(0));
                }
            }
        } else {
            mResetImageList.addAll(imageList);
        }
        return mResetImageList;

    }

    public void replaceText(TextView tv) {
        String str = tv.getText().toString();
        for (int i = 1; i <= 8; i++) {
            if (str.contains(i + ".")) {
                str = str.replace(i + ".", i + ". ");
            }
        }
        tv.setText(str);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.img_head_back:
                if (mListVo != null) {
                    setNewPraiseData();
                    intent.putExtra("listvo", (Serializable) mListVo);
                    this.setResult(2, intent);
                }
                finish();
                break;

            case R.id.foot_mark:
                // 跳转到足迹界面
                intent.setClass(this, JournalFootPrintActivity.class);
                intent.putExtra("type", mFootType);
                intent.putExtra("uid", mJournalDetailResltVO.getInfo().getLoguid());
                intent.putExtra("time", DateUtils.parseDateDay(mJournalDetailResltVO.getInfo().getTime()));
                startActivity(intent);
                break;
            case R.id.attention:
                mIsAttention = true;
                commitOtherData();
                break;
            // 抄送对象
            case R.id.cc_title_tv:
                if (index == 2) {
                    startAnim(1);// 下划线动画
                    index = 1;
                    mCczTitleTv.setTextColor(Color.parseColor("#96A6A7"));
                    mCcTitleTv.setTextColor(getResources().getColor(R.color.C5));
                    mCcLayout.setVisibility(View.VISIBLE);
                    mCczLayout.setVisibility(View.GONE);
                }
                break;
            // 抄送者
            case R.id.ccz_title_tv:
                if (index == 1) {
                    startAnim(2);// 下划线动画
                    index = 2;
                    mCcTitleTv.setTextColor(Color.parseColor("#96A6A7"));
                    mCczTitleTv.setTextColor(getResources().getColor(R.color.C5));
                    mCczLayout.setVisibility(View.VISIBLE);
                    mCcLayout.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }

    }

    public void startAnim(int dex) {
        Animation animation = null;
        switch (dex) {
            case 1:
                animation = new TranslateAnimation(moveDistance, 0, 0, 0);
                break;
            case 2:
                animation = new TranslateAnimation(0, moveDistance, 0, 0);
                break;
            case 3:
                animation = new TranslateAnimation(0, 0, 0, 0);
                break;
            default:
                break;
        }

        animation.setFillAfter(true);
        animation.setDuration(150);
        mLineImg.startAnimation(animation);
    }

    public void commitOtherData() {
        RequestParams params = new RequestParams();
        String url = null;
        try {
            params.put("ftoken", BSApplication.getInstance().getmCompany());
            params.put("userid", BSApplication.getInstance().getUserId());
            params.put("loguid", mJournalDetailResltVO.getInfo().getLoguid());// 日志的用户id"171"mLoguid
            params.put("type", mAttentionType);
            url = BSApplication.getInstance().getHttpTitle() + Constant.JOURNAL_Attention;

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                CustomToast.showShortToast(JournalPublishDetailActivity.this, "失败~");
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(new String(arg2));
                    String code = (String) jsonObject.get("code");
                    String str = (String) jsonObject.get("retinfo");
                    if (Constant.RESULT_CODE.equals(code)) {
                        Intent in = new Intent();
                        in.setAction(UPDATE_ATTENTION);
                        in.putExtra("is_attention", mIsAttention);
                        sendBroadcast(in);
                        commitHandler.sendEmptyMessage(0);
                        CustomToast.showShortToast(JournalPublishDetailActivity.this, str);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private Handler commitHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    // 关注与取消关注来回切换
                    if ("1".equals(mAttentionType)) {
                        mAttention.setText("取消关注");
                        mAttention.setBackgroundDrawable(CommonUtils.setBackgroundShap(JournalPublishDetailActivity.this, 5, R.color.C3_1, R.color.C3_1));
                        mAttentionType = "2";
                    } else if ("2".equals(mAttentionType)) {
                        mAttention.setText("关注");
                        mAttention.setBackgroundDrawable(CommonUtils.setBackgroundShap(JournalPublishDetailActivity.this, 5, "#FFA200", "#FFA200"));
                        mAttentionType = "1";
                    }
                    setNewAttentionData();
                    break;

            }
            super.handleMessage(msg);
        }
    };

    public void setNewAttentionData() {
        if (mListVo == null)
            return;
        for (int i = 0; i < mListVo.size(); i++) {
            if (mJournalDetailResltVO != null && mJournalDetailResltVO.getInfo() != null) {
                if (mJournalDetailResltVO.getInfo().getLoguid().equals(mListVo.get(i).getLoguid())) {
                    if ("1".equals(mAttentionType)) {
                        mListVo.get(i).setIsfavor("0");
                    } else if ("2".equals(mAttentionType)) {
                        mListVo.get(i).setIsfavor("1");
                    }
                }
            }
        }
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    @Override
    public void updateUi() {

        updateData();
        if (CommonUtils.showLead(this, "JournalPublishDetailActivity")) {
            initLeadView();
        }
    }

    ResultCallback mCallback = new ResultCallback() {

        @Override
        public void callback(String str, int position) {
            mOkPop.dismiss();
            mAttentionPop.dismiss();
            mFootPop.dismiss();
        }
    };

    @Override
    public void baseSetContentView() {
        mCurrentLayout = View.inflate(this, R.layout.journal_publish_detail_view, null);
        mContentLayout.addView(mCurrentLayout);
        mContentLayout.requestFocus();
    }

    // 加载更多数据
    public void initFoot() {
        mFootLayout = LayoutInflater.from(this).inflate(R.layout.listview_bottom_more, null);
        mMoreTextView = (TextView) mFootLayout.findViewById(R.id.txt_loading);
        mMoreTextView.setText("更多");
        mProgressBar = (ProgressBar) mFootLayout.findViewById(R.id.progressBar);
        mFootLayout.setVisibility(View.GONE);
        mDiscussLv.addFooterView(mFootLayout);
    }

    protected void footViewIsVisibility(List<DiscussVO> datas) {
        if (mDiscussResultVO == null) {
            mFootLayout.setVisibility(View.GONE);
            return;
        }
        if (mDiscussResultVO.getCount() == null) {
            mFootLayout.setVisibility(View.GONE);
            return;
        }

        if (Integer.parseInt(mDiscussResultVO.getCount()) <= 15) {
            mFootLayout.setVisibility(View.GONE);
            mDiscussLv.removeFooterView(mFootLayout);// 避免list列表下面有太多空白
        } else {
            mDiscussLv.removeFooterView(mFootLayout);// 避免mDiscussLv中有多个mFootLayout，而造成空白
            mFootLayout.setVisibility(View.VISIBLE);
            mMoreTextView.setText("加载更多");
            mProgressBar.setVisibility(View.GONE);
            mDiscussLv.addFooterView(mFootLayout);
        }
    }

    public void bindRefreshListener() {
        mFootLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mMoreTextView.setText("正在加载...");
                mProgressBar.setVisibility(View.VISIBLE);
                mState = 2;
                new ThreadUtil(JournalPublishDetailActivity.this, JournalPublishDetailActivity.this).start();

            }
        });

        mOkTv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(JournalPublishDetailActivity.this, JournalPersonalDetailActivity.class);
                if (mJournalDetailResltVO == null)
                    return;
                intent.putExtra("date", DateUtils.parseDateDay(mJournalDetailResltVO.getInfo().getTime()));
                intent.putExtra("loguid", mJournalDetailResltVO.getInfo().getLoguid());
                intent.putExtra("title_name", mJournalDetailResltVO.getInfo().getFullname());
                startActivityForResult(intent, 10);
            }
        });
    }

    @Override
    public boolean onDown(MotionEvent e) {
        curP.x = e.getX();
        curP.y = e.getY();
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1 == null || e2 == null)
            return true;
        if (mListVo == null)
            return true;

        if ("1".equals(mPersonal)) {
            return false;
        }
        if (Math.abs(velocityX) > 50 && Math.abs(velocityX) - Math.abs(velocityY) > 400) {
            float distanceX = curP.x - downP.x;
            float distanceY = curP.y - downP.y;
            if (velocityX < 0 && e1.getX() - e2.getX() > 20) {
                mLogid = mJournalDetailResltVO.getInfo().getNextid();
                if ("暂无".equals(mLogid) || "".equals(mLogid)) {
                    CustomToast.showShortToast(JournalPublishDetailActivity.this, "最后一篇了哦~");
                    return true;
                }
                // CustomDialog.showProgressDialog(this);圆圈加载
                mFlag = true;
                mState = 0;
                mCurrentLayout.startAnimation(AnimationUtils.loadAnimation(this,
                        R.anim.push_left_out));

                mContentLayout.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mLoadingLayout.setVisibility(View.VISIBLE);
                        mContentLayout.setVisibility(View.GONE);
                        new ThreadUtil(JournalPublishDetailActivity.this, JournalPublishDetailActivity.this).start();
                    }
                }, 500);

            } else if (velocityX > 0 && e2.getX() - e1.getX() > 20) {

                mLogid = mJournalDetailResltVO.getInfo().getPreid();

                if ("暂无".equals(mLogid) || "".equals(mLogid)) {
                    CustomToast.showShortToast(JournalPublishDetailActivity.this, "第一篇了哦~");
                    return true;
                }
                // CustomDialog.showProgressDialog(this);
                mFlag = true;
                mState = 0;
                mCurrentLayout.startAnimation(AnimationUtils.loadAnimation(this,
                        R.anim.push_right_out));
                mContentLayout.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mLoadingLayout.setVisibility(View.VISIBLE);
                        mContentLayout.setVisibility(View.GONE);
                        new ThreadUtil(JournalPublishDetailActivity.this, JournalPublishDetailActivity.this).start();
                    }
                }, 500);
            }

        }

        return false;
    }

    // 在mDetailLayout触摸事件中监听手势
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        detector.onTouchEvent(event);
        return false;
    }

    public boolean commit() {
        CustomDialog.showProgressDialog(this, "正在提交数据...");
        RequestParams params = new RequestParams();

        try {
            params.put("ftoken", BSApplication.getInstance().getmCompany());
            params.put("userid", BSApplication.getInstance().getUserId());
            params.put("logid", mLogid);
            params.put("ccuids", mSb.toString());

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        // params.put("name", "woshishishi");// 传输的字符数据
        String url = BSApplication.getInstance().getHttpTitle() + Constant.JOURNAL_BOSS_CC;
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                System.out.println(new String(arg2));

                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(new String(arg2));
                    String str = (String) jsonObject.get("retinfo");
                    String code = (String) jsonObject.get("code");
                    if (Constant.RESULT_CODE.equals(code)) {
                        CustomToast.showLongToast(JournalPublishDetailActivity.this, str);
                        mFlag = true;
                        new ThreadUtil(JournalPublishDetailActivity.this, JournalPublishDetailActivity.this).start();
                    } else {
                        CustomToast.showLongToast(JournalPublishDetailActivity.this, "网络错误");
                    }
                    CustomDialog.closeProgressDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2014) {
            mDataList.clear();
            mSb.setLength(0);
            mDataList = (List<EmployeeVO>) data.getSerializableExtra("checkboxlist");
            for (int i = 0; i < mDataList.size(); i++) {
                mSb.append(mDataList.get(i).getUserid());
                if (i != mDataList.size() - 1) {
                    mSb.append(",");
                }
            }
            if (mSb.length() == 0) {
                CustomToast.showLongToast(this, "请选择抄送人");
                return;
            }
            commit();
            return;
        }

        if (resultCode == 10) {
            finish();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDiscussAdapter.stopSound();
    }

    public void setNewPraiseData() {
        if (mFinishFlag) {
            for (int i = 0; i < mListVo.size(); i++) {
                if (mJournalDetailResltVO != null && mJournalDetailResltVO.getInfo() != null) {
                    if (mJournalDetailResltVO.getInfo().getLogid().equals(mListVo.get(i).getLogid())) {
                        mListVo.get(i).setPraise(fragment.getmPraiseCount() + "");
                    }
                }
            }
            mFinishFlag = false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mListVo != null) {
                setNewPraiseData();
                Intent intent = new Intent();
                intent.putExtra("listvo", (Serializable) mListVo);
                this.setResult(2, intent);
            }
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
