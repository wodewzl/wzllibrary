
package com.bs.bsims.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.bs.bsims.R;
import com.bs.bsims.activity.BossNeedDoActivity;
import com.bs.bsims.activity.BossNotifyActivity;
import com.bs.bsims.activity.NoticeActivity;
import com.bs.bsims.activity.SignInActivity;
import com.bs.bsims.adapter.MessageAdapter;
import com.bs.bsims.adapter.MessageAdapter.DragCallback;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.chat.ChatMessageHomeActivity;
import com.bs.bsims.chatutils.IMJavaBean;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.interfaces.UpdateCallback;
import com.bs.bsims.model.MainResultVO;
import com.bs.bsims.model.MainVO;
import com.bs.bsims.model.RequestResultVO;
import com.bs.bsims.model.UserFromServerVO;
import com.bs.bsims.observer.BSApplictionObserver.Watcher;
import com.bs.bsims.observer.MessageMyHeadImp;
import com.bs.bsims.observer.MessageRedPointsImp;
import com.bs.bsims.onekey.remove.DropCover.OnDragCompeteListener;
import com.bs.bsims.onekey.remove.WaterDrop;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomLog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSCircleImageView;
import com.bs.bsims.view.BSPointImageView;
import com.bs.bsims.xutils.impl.HttpUtilsByPC;
import com.bs.bsims.xutils.impl.RequestCallBackPC;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yzxIM.IMManager;
import com.yzxIM.data.db.ChatMessage;
import com.yzxtcp.UCSManager;
import com.yzxtcp.data.UcsErrorCode;
import com.yzxtcp.data.UcsReason;
import com.yzxtcp.listener.ILoginListener;

import org.xutils.ex.HttpException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint("NewApi")
public class MessageFragment extends BaseFragment implements OnClickListener, UpdateCallback, OnDragCompeteListener, Watcher {

    public static final String TAG = "HomeFragment";
    private Activity mActivity;
    private TextView mTitleTv;
    private ViewPager mViewPager;
    private WaterDrop mBSPonitTitleTwo, mBSPonitTitleThree, mBSPonitTitleFour, mBSPonitTitleFive;
    private ImageView mHideBt;
    private View mView;
    private LinearLayout mNoticeLayout, mNotifyLayoout, mDocumentLayout, mInstitutionLayout;
    private List<MainVO> mMainVOList;
    private BSPointImageView mMessage01, mMessage02, mMessage03, mMessage04, mMessage05, mMessage06;

    private TextView mNameTv, mAgeTv, mPostionTv;
    private BSCircleImageView mBsCircleImageView;
    private ImageView mImgBack, mPunchImg;
    private TextView mCompanyNameTv;
    private ImageView mBkgHeadIcon;
    // 0为一键拖拽 1为初始请求列表 2为侧滑删除 3为置顶，4为取消置顶
    private String mType, mFlag = "1";
    private SwipeMenuListView mListView;
    private boolean isNewData = false;
    private LinearLayout mTitleLayout;
    private MessageAdapter mMessageAdapter;
    private int potions = 0;
    private MenuNormalSelected clickHeadShowMenu;
    private PopupWindow mPop;
    private List<PopupWindow> mListpop = new ArrayList<PopupWindow>();

    public void setClickHeadShowMenu(MenuNormalSelected clickHeadShowMenu) {
        this.clickHeadShowMenu = clickHeadShowMenu;
    }

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    synchronized (msg.obj) {
                        if (mBSPonitTitleTwo != null && mBSPonitTitleTwo.getText() != null) {
                            potions = Integer.parseInt(mBSPonitTitleTwo.getText());
                        }
                        potions += (Integer) msg.obj;
                        if (potions > 99)
                            potions = 99;
                        mBSPonitTitleTwo.setText(potions + "");
                        mBSPonitTitleTwo.setVisibility(View.VISIBLE);
                        mBSPonitTitleTwo.setTag(1);
                    }
                    break;

            }

        }
    };
    DragCallback mDragCallback = new DragCallback() {
        @Override
        public void callback(int status, String type) {
            mFlag = status + "";
            mType = type;
            switch (status) {
                case 1:
                    cleanMessage(mType);
                    break;
                case 0:
                case 2:
                case 3:
                case 4:
                    new ThreadUtil(mActivity, MessageFragment.this).start();
                    break;

                default:
                    break;
            }

        }
    };

    public static MessageFragment newInstance() {
        MessageFragment homeFragment = new MessageFragment();

        return homeFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MessageMyHeadImp.getInstance().add(this);
        MessageRedPointsImp.getInstance().add(this);
//        new ThreadUtil(mActivity, this).start();
    }

    private void initViews(View view) {
        mPunchImg = (ImageView) view.findViewById(R.id.txt_comm_head_right);
        mCompanyNameTv = (TextView) view.findViewById(R.id.txt_head_left_back);
        mCompanyNameTv.setText(BSApplication.getInstance().getUserFromServerVO().getFirmcname());
        mNameTv = (TextView) view.findViewById(R.id.name_tv);
        mAgeTv = (TextView) view.findViewById(R.id.age_tv);
        mPostionTv = (TextView) view.findViewById(R.id.position_tv);
        mBsCircleImageView = (BSCircleImageView) view.findViewById(R.id.head_icon);
        mBkgHeadIcon = (ImageView) view.findViewById(R.id.bkg_head_icon);
        mBsCircleImageView.setBackgroundResource(R.drawable.ic_default_portrait_s);

        mBSPonitTitleTwo = (WaterDrop) view.findViewById(R.id.img_pmmm_02);
        mBSPonitTitleThree = (WaterDrop) view.findViewById(R.id.img_pmmm_03);
        mBSPonitTitleFour = (WaterDrop) view.findViewById(R.id.img_pmmm_04);
        mBSPonitTitleFive = (WaterDrop) view.findViewById(R.id.img_pmmm_05);

        mNoticeLayout = (LinearLayout) view.findViewById(R.id.quick_layout_01);
        mNotifyLayoout = (LinearLayout) view.findViewById(R.id.quick_layout_02);
        mDocumentLayout = (LinearLayout) view.findViewById(R.id.quick_layout_03);
        mInstitutionLayout = (LinearLayout) view.findViewById(R.id.quick_layout_04);
        mNoticeLayout.setOnClickListener(this);
        mNotifyLayoout.setOnClickListener(this);
        mDocumentLayout.setOnClickListener(this);
        mInstitutionLayout.setOnClickListener(this);

        mPunchImg.setOnClickListener(this);
        mMessageAdapter = new MessageAdapter(mActivity, mDragCallback);
        mListView = (SwipeMenuListView) view.findViewById(R.id.message_list);
        mListView.setAdapter(mMessageAdapter);
        mMessageAdapter.setListView(mListView);
        mTitleLayout = (LinearLayout) view.findViewById(R.id.title_layout);
        initData();
        bindViewsListener(view);
    }

    public void bindViewsListener(View view) {
        mBSPonitTitleTwo.setOnDragCompeteListener(this);
        mBSPonitTitleThree.setOnDragCompeteListener(this);
        mBSPonitTitleFour.setOnDragCompeteListener(this);
        mBSPonitTitleFive.setOnDragCompeteListener(this);
        mBsCircleImageView.setOnClickListener(this);

    }

    private void initData() {
        ImageLoader imageLoader = ImageLoader.getInstance();
        UserFromServerVO vo = BSApplication.getInstance().getUserFromServerVO();
        imageLoader.displayImage(vo.getHeadpic(), mBsCircleImageView, CommonUtils.initImageLoaderOptions());
        mNameTv.setText(vo.getFullname());
        Drawable drawable;
        if (Constant.SEX_MAN.equals(vo.getSex())) {
            drawable = this.getResources().getDrawable(R.drawable.sex_man);
        } else {
            drawable = this.getResources().getDrawable(R.drawable.sex_woman);

        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); // 设置边界
        mAgeTv.setCompoundDrawables(drawable, null, null, null);
        mAgeTv.setCompoundDrawablePadding(5);
        StringBuffer sb = new StringBuffer();
        sb.append(vo.getDname()).append(" | ").append(vo.getPname());
        mPostionTv.setText(sb.toString());
        mBkgHeadIcon.setVisibility(View.VISIBLE);

        if (Constant.SEX_MAN.equals(BSApplication.getInstance().getUserFromServerVO().getSex())) {
            mAgeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.set_title_man, 0, 0, 0);
        } else {
            mAgeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.set_title_woman, 0, 0, 0);
        }
        mAgeTv.setCompoundDrawablePadding(10);
        mAgeTv.setText(BSApplication.getInstance().getUserFromServerVO().getAge());
        updateNonetData();

    }

    // 没有网络的死数据
    public void updateNonetData() {
        List<MainVO> list = new ArrayList<MainVO>();
        for (int i = 7; i <= 19; i++) {
            if (i == 9 || i == 13 || i == 14 || i == 15 || i == 16 || i == 17 || i == 18)
                continue;

            MainVO mainVO = new MainVO();
            mainVO.setTime("0");
            mainVO.setCount("0");
            mainVO.setType(i + "");
            switch (i) {
                case 5:
                    mainVO.setTypename("日志评论");
                    mainVO.setTitle("我的日志被评论");

                    break;
                case 6:
                    mainVO.setTypename("日志抄送");
                    mainVO.setTitle("抄送给我的日志");
                    break;
                case 7:
                    mainVO.setTypename("审批事务");
                    mainVO.setTitle("与我相关的审批");
                    break;
                case 8:
                    mainVO.setTypename("任务跟进");
                    mainVO.setTitle("与我相关的任务");
                    break;
                case 10:
                    mainVO.setTypename("意见分享");
                    mainVO.setTitle("给公司的建议");
                    break;

                case 11:
                    mainVO.setTypename("日程管理");
                    mainVO.setTitle("与我相关的日程安排");
                    break;

                case 12:
                    mainVO.setTypename("企业云盘");
                    mainVO.setTitle("企业分享的文档");
                    break;

                case 19:
                    mainVO.setTypename("日志提醒");
                    mainVO.setTitle("相关日志提醒");
                    break;
                default:
                    break;
            }

            list.add(mainVO);
        }
        mMessageAdapter.updateData(list);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MessageMyHeadImp.getInstance().remove(this);
        MessageRedPointsImp.getInstance().remove(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public String getFragmentName() {
        return TAG;
    }

    @Override
    public boolean execute() {
        if ("0".equals(mFlag)) {
            return oneKeyCancel();
        } else {
            return getData();
        }

    }

    @Override
    public void executeSuccess() {
        if (!"1".equals(mFlag)) {
            if ("0".equals(mFlag))
                new ThreadUtil(mActivity, MessageFragment.this).start();
            mFlag = "1";
            return;
        }

        List<MainVO> list = new ArrayList<MainVO>();
        for (int i = 0; i < mMainVOList.size(); i++) {
            // if ("1".equals(mMainVOList.get(i).getType())
            // || "2".equals(mMainVOList.get(i).getType())
            // || "3".equals(mMainVOList.get(i).getType())
            // || "4".equals(mMainVOList.get(i).getType())
            // || "9".equals(mMainVOList.get(i).getType())
            // || "20".equals(mMainVOList.get(i).getType())
            // || "21".equals(mMainVOList.get(i).getType())
            // || "22".equals(mMainVOList.get(i).getType())
            // || "23".equals(mMainVOList.get(i).getType())
            // || "24".equals(mMainVOList.get(i).getType()))

            // 不过滤type为1 2 3 9的类型 通知 公文 制度 风采
            if ("4".equals(mMainVOList.get(i).getType())
                    || "20".equals(mMainVOList.get(i).getType())
                    || "21".equals(mMainVOList.get(i).getType())
                    || "22".equals(mMainVOList.get(i).getType())
                    || "23".equals(mMainVOList.get(i).getType())
                    || "24".equals(mMainVOList.get(i).getType()))
                continue;
            list.add(mMainVOList.get(i));
        }
        isNewData = true;
        mMessageAdapter.updateData(list);

        for (int i = 0; i < mMainVOList.size(); i++) {
            int type = Integer.parseInt(mMainVOList.get(i).getType());
            switch (type) {
                case 1:
                    // if ("0".equals(mMainVOList.get(i).getCount())) {
                    // mBSPonitTitleTwo.setVisibility(View.GONE);
                    // } else {
                    // mBSPonitTitleTwo.setText(CommonUtils.isNormalCount(mMainVOList.get(i).getCount()));
                    // mBSPonitTitleTwo.setVisibility(View.VISIBLE);
                    // mBSPonitTitleTwo.setTag(type);
                    // }
                    mBSPonitTitleTwo.setText(IMManager.getInstance(mActivity).getUnreadCountAll() + "");
                    break;

                case 20:
                    if ("0".equals(mMainVOList.get(i).getCount())) {
                        mBSPonitTitleThree.setVisibility(View.GONE);
                    } else {
                        mBSPonitTitleThree.setText(CommonUtils.isNormalCount(mMainVOList.get(i).getCount()));
                        mBSPonitTitleThree.setVisibility(View.VISIBLE);
                        mBSPonitTitleThree.setTag(type);
                    }

                    break;
                case 21:
                    if ("0".equals(mMainVOList.get(i).getCount())) {
                        mBSPonitTitleFour.setVisibility(View.GONE);
                    } else {
                        mBSPonitTitleFour.setText(CommonUtils.isNormalCount(mMainVOList.get(i).getCount()));
                        mBSPonitTitleFour.setVisibility(View.VISIBLE);
                        mBSPonitTitleFour.setTag(type);
                    }
                    // break;
                    // case 3:
                    // if ("0".equals(mMainVOList.get(i).getCount())) {
                    // mBSPonitTitleFour.setVisibility(View.GONE);
                    // } else {
                    // mBSPonitTitleFour.setText(CommonUtils.isNormalCount(mMainVOList.get(i).getCount()));
                    // mBSPonitTitleFour.setVisibility(View.VISIBLE);
                    // mBSPonitTitleFour.setTag(type);
                    // }
                    //
                    // break;
                    //
                    // case 9:
                    // if ("0".equals(mMainVOList.get(i).getCount())) {
                    // mBSPonitTitleFive.setVisibility(View.GONE);
                    // } else {
                    // mBSPonitTitleFive.setText(CommonUtils.isNormalData(mMainVOList.get(i).getCount()));
                    // mBSPonitTitleFive.setVisibility(View.VISIBLE);
                    // mBSPonitTitleFive.setTag(type);
                    // }

                default:
                    break;
            }

        }

        if (CommonUtils.showLead(mActivity, "MessageFragment")) {
            if(!mActivity.isFinishing()){
                initLeadView();
            }
         
        }

    }

    @Override
    public void executeFailure() {
        mFlag = "1";
    }

    public boolean getData() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            map.put("isboss", "0");
            map.put("isapp", CommonUtils.getLimitsSpecial(Constant.LIMITS_SPECIAL001));// 查看全部审批
            map.put("istask", CommonUtils.getLimitsSpecial(Constant.LIMITS_SPECIAL002));// 查看全部任务
            map.put("isidea", CommonUtils.getLimitsSpecial(Constant.LIMITS_SPECIAL003));// 查看全部意见
            map.put("iscont", CommonUtils.getLimitsSpecial(Constant.LIMITS_OFFICE010));// 是否合同审批权限
            map.put("ispay", CommonUtils.getLimitsSpecial(Constant.LIMITS_OFFICE011));// 回款审批
            map.put("type", mType);
            String url = "";
            if ("1".equals(mFlag)) {
                url = Constant.MAIN_MESSAGE_URL;
            } else if ("2".equals(mFlag)) {
                url = Constant.MAIN_MESSAGE_DELETE_URL;
            } else if ("3".equals(mFlag)) {
                url = Constant.MAIN_MESSAGE_TOP_URL;
            } else if ("4".equals(mFlag)) {
                url = Constant.MAIN_MESSAGE_REMOVE_TOP_URL;
            }

            String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + url, map);
            MainResultVO vo = gson.fromJson(jsonStr, MainResultVO.class);

            if (Constant.RESULT_CODE.equals(vo.getCode())) {
                mMainVOList = vo.getArray();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean oneKeyCancel() {
        try {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put("type", mType);
            map.put("isapp", CommonUtils.getLimitsSpecial(Constant.LIMITS_SPECIAL001));// 查看全部审批
            map.put("istask", CommonUtils.getLimitsSpecial(Constant.LIMITS_SPECIAL002));// 查看全部任务
            map.put("isidea", CommonUtils.getLimitsSpecial(Constant.LIMITS_SPECIAL003));// 查看全部意见
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.MAIN_ONEKEY_CANCEL, map);
            Gson gson = new Gson();
            RequestResultVO vo = gson.fromJson(jsonStr, RequestResultVO.class);
            if (Constant.RESULT_CODE.equals(vo.getCode())) {
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
    public void onDrag(View view) {
        int type = 0;
        if (view.getTag() != null) {
            type = Integer.parseInt(view.getTag().toString());
        } else {
            type = view.getId();
        }

        cleanMessage(type + "");
    }

    public void cleanMessage(String type) {
        mFlag = "0";
        mType = type + "";
        new ThreadUtil(mActivity, MessageFragment.this).start();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {

            case R.id.txt_comm_head_right:
                intent.setClass(mActivity, SignInActivity.class);
                break;

            case R.id.quick_layout_01:
                // intent.putExtra("noticeid", "");
                // intent.putExtra("sortid", "3");
                // intent.setClass(mActivity, NoticeActivity.class);
                if (BSApplication.getInstance().isAppliactionChat()) {
                    intent.setClass(mActivity, ChatMessageHomeActivity.class);
                    startActivity(intent);
                }
                else {
                    CustomToast.showShortToast(mActivity, "系统正在初始化聊天信息~");
                    onConnectIm();
                    return;
                }
                break;
            case R.id.quick_layout_04:
                intent.putExtra("noticeid", "");
                intent.putExtra("sortid", "19");
                intent.setClass(mActivity, NoticeActivity.class);
                break;
            case R.id.quick_layout_02:
                // intent.putExtra("noticeid", "");
                // intent.putExtra("sortid", "11");
                // intent.setClass(mActivity, NoticeActivity.class);
                intent.setClass(mActivity, BossNeedDoActivity.class);
                break;

            case R.id.quick_layout_03:
                // intent.putExtra("noticeid", "");
                // intent.putExtra("sortid", "12");
                // intent.setClass(mActivity, NoticeActivity.class);
                intent.setClass(mActivity, BossNotifyActivity.class);
                break;
            case R.id.head_icon:
                clickHeadShowMenu.showMenu();
                return;

            default:
                break;
        }
        this.startActivity(intent);
    }

    @Override
    public void updateNotify(Object content) {
        if (content instanceof String) {
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage((String) content, mBsCircleImageView, CommonUtils.initImageLoaderOptions());
        }
        // Im消息红点
        else if (content instanceof List) {
            Message message = mHandler.obtainMessage();
            message.what = 1;
            List<ChatMessage> list = (List<ChatMessage>) content;
            message.obj = list.size();
            mHandler.sendMessage(message);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            new ThreadUtil(mActivity, MessageFragment.this).start();
        }
    }

    public interface MenuNormalSelected {
        public void showMenu();
    }

    public void initLeadView() {
        LinearLayout.LayoutParams headTvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);// 定义文本显示组件
        headTvParams.leftMargin = CommonUtils.getViewWidth(mBsCircleImageView) / 2;
        LinearLayout.LayoutParams headImgParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, CommonUtils.dip2px(mActivity, 150));// 定义文本显示组件
        headImgParams.gravity = Gravity.LEFT;
        headImgParams.rightMargin = CommonUtils.dip2px(mActivity, 20) + CommonUtils.getViewWidth(mNoticeLayout) / 2;
        headImgParams.topMargin = CommonUtils.dip2px(mActivity, 10);
        PopupWindow headPop = CommonUtils.leadPop(mActivity, mBsCircleImageView, "个人中心搬家了，点击头像或向右滑动，就会出现侧滑菜单。", headTvParams, headImgParams, 1);
        mListpop.add(headPop);

        LinearLayout.LayoutParams msgTvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);// 定义文本显示组件
        msgTvParams.leftMargin = CommonUtils.dip2px(mActivity, 30);
        LinearLayout.LayoutParams msgImgParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, CommonUtils.dip2px(mActivity, 150));// 定义文本显示组件
        msgImgParams.gravity = Gravity.LEFT;
        msgImgParams.rightMargin = CommonUtils.dip2px(mActivity, 20);
        msgImgParams.topMargin = CommonUtils.dip2px(mActivity, 20);
        PopupWindow msgPop = CommonUtils.leadPop(mActivity, mNoticeLayout, "新增即时聊天了，聊天消息都在这里，快来和小伙伴聊天吧！", msgTvParams, msgImgParams, 1);
        mListpop.add(msgPop);
        CommonUtils.okLeadPop(mActivity, mNoticeLayout, mListpop, 0);
    }

    public void onConnectIm() {
        final String url = BSApplication.getInstance().getHttpTitle() + Constant.IMLOGIN;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", BSApplication.getInstance().getUserId());
        map.put("uids", BSApplication.getInstance().getUserId());
        map.put("ftoken", BSApplication.getInstance().getmCompany());
        new HttpUtilsByPC().sendPostBYPC(url, map,
                new RequestCallBackPC() {
                    @Override
                    public void onFailurePC(HttpException arg0, String arg1) {
                        // TODO Auto-generated method stub
                        CustomLog.e("TAG", "账号或平台未注册聊天通讯功能");
                    }

                    @Override
                    public void onSuccessPC(ResponseInfo rstr) {
                        // TODO Auto-generated method stub
                        Gson gson = new Gson();
                        try {
                            IMJavaBean imJavaBean = gson.fromJson(rstr.result.toString(), IMJavaBean.class);
                            if (Constant.RESULT_CODE.equals(imJavaBean.getCode()) && null != imJavaBean.getClient() && null != imJavaBean.getClient().get(0)) {
                                BSApplication.getInstance().saveIMjavaBean(imJavaBean);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        UCSManager.connect(BSApplication.getInstance().getIMjavaBean().getClient().get(0).getLoginToken(), new ILoginListener() {
                                            @Override
                                            public void onLogin(UcsReason reason) {
                                                // TODO Auto-generated method stub
                                                if (reason.getReason() == UcsErrorCode.NET_ERROR_CONNECTOK) {
                                                    BSApplication.getInstance().setAppliactionChat(true);
                                                    // inMainPage();
                                                }
                                                else {
                                                    BSApplication.getInstance().setAppliactionChat(false);
                                                    CustomLog.e("resss", "login fail errorCode = " + reason.getReason() + ",errorMsg = " + reason.getMsg());
                                                }
                                            }
                                        });
                                    }
                                }).start();
                            }
                            else {
                                BSApplication.getInstance().setAppliactionChat(false);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            BSApplication.getInstance().setAppliactionChat(false);
                        }
                    }
                });
    }
}
