
package com.bs.bsims.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.bs.bsims.R;
import com.bs.bsims.activity.BossNeedDoActivity;
import com.bs.bsims.activity.BossNotifyActivity;
import com.bs.bsims.adapter.MessageAdapter;
import com.bs.bsims.adapter.MessageAdapter.DragCallback;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.interfaces.UpdateCallback;
import com.bs.bsims.model.MainResultVO;
import com.bs.bsims.model.MainVO;
import com.bs.bsims.model.RequestResultVO;
import com.bs.bsims.observer.MessageRedPointsImp;
import com.bs.bsims.observer.BSApplictionObserver.Watcher;
import com.bs.bsims.onekey.remove.DropCover.OnDragCompeteListener;
import com.bs.bsims.onekey.remove.WaterDrop;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSViewPager;
import com.bs.bsims.view.FlowIndicator;
import com.bs.bsims.viewpager.ViewPagerAdapter;
import com.google.gson.Gson;
import com.yzxIM.IMManager;
import com.yzxIM.data.db.ChatMessage;
import com.yzxIM.data.db.ConversationInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint("NewApi")
public class BossMessageFragment extends BaseFragment implements UpdateCallback, OnPageChangeListener, OnDragCompeteListener, OnClickListener, OnScrollListener, Watcher {
    private Activity mActivity;
    private FlowIndicator mFlowDot;
    private BSViewPager mViewPager;
    private PagerAdapter mSurveyPageAdapter;
    private List<Fragment> mListFragment;
    private BossVpagerFragment1 mViewPagerFragment1;
    private BossVpagerFragment2 mViewPagerFragment2;
    private BossVpagerFragment3 mViewPagerFragment3;

    private TextView mNoReadInfoTv, mReadAllTv, mReadAllPopTv;
    private SwipeMenuListView mMessageList;
    private MessageAdapter mMessageAdapter;
    // 0为一键拖拽 1为初始请求列表 2为侧滑删除 3为置顶
    private String mType, mFlag = "1";
    private MainResultVO mainResultVO;
    private List<MainVO> mMainVOList;
    private LinearLayout mPopLayout;
    private TextView mNotifyCountTv, mNeedDoCountTv;
    private TextView mNeedDoTv, mNotifyTv;
    private PopupWindow mPop;
    private List<PopupWindow> mListpop = new ArrayList<PopupWindow>();
    private List<WaterDrop> mDropList = new ArrayList<WaterDrop>();
    private List<String> mTypeList = new ArrayList<String>();
    private MenuBossSelected menuSelected;
    private String TAG = "BossInformationFragment";
    private ImageButton mMenuBtn;
    private MainVO mainVO;
    private List<MainVO> list;
    DragCallback mDragCallback = new DragCallback() {
        @Override
        public void callback(int status, String type) {
            mFlag = status + "";
            mType = type;
            if (mType.equals("20"))
            {
                if (status == 0) {
                    mHandler.sendEmptyMessage(2);
                }
                return;// 如果是聊天就不请求接口了
            }
            switch (status) {
                case 1:
                    cleanMessage();
                    break;
                case 0:
                case 2:
                case 3:
                case 4:
                    new ThreadUtil(mActivity, BossMessageFragment.this).start();
                    break;

                default:
                    break;
            }

        }
    };

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (mainVO == null) {
                        mainVO = new MainVO();
                    }
                    List<ChatMessage> mChatMessgae = (List<ChatMessage>) msg.obj;
                    mainVO.setType("20");
                    mainVO.setTypename("消息沟通");
                    mainVO.setIstop("0");
                    mainVO.setTime(mChatMessgae.get(mChatMessgae.size() - 1).getSendTime() / 1000 + "");
                    if (list != null && mMessageAdapter != null) {
                        if (list.get(0).getType().equals("20")) {
                            mainVO.setCount((Integer.parseInt(list.get(0).getCount()) + mChatMessgae.size()) + "");
                            mainVO.setTitle(mainVO.getCount() + "条新的消息,赶快打开看吧");
                            list.remove(0);
                        }
                        else {
                            mainVO.setCount(mChatMessgae.size() + "");
                            mainVO.setTitle(mChatMessgae.size() + "条新的消息,赶快打开看吧");
                        }
                        list.add(0, mainVO);
                        mMessageAdapter.updateData(list);
                        mReadAllTv.setText("标记全部为已读");
                        mReadAllPopTv.setText("标记全部为已读");
                    }
                    break;

                case 2:
                    if (msg.what == 2) {
                        List<ConversationInfo> mConeverlist = IMManager.getInstance(mActivity).getConversationList();
                        for (int i = 0; i < mConeverlist.size(); i++) {
                            IMManager.getInstance(mActivity).clearMessagesUnreadStatus(mConeverlist.get(i));
                        }
                        if (mainVO != null) {
                            list.remove(0);
                            mainVO.setType("20");
                            mainVO.setTypename("消息沟通");
                            mainVO.setTitle("还没有最新的消息哦~");
                            mainVO.setIstop("0");
                            mainVO.setCount("0");
                            list.add(0, mainVO);
                            mMessageAdapter.updateData(list);
                        }

                    }
                    break;

            }

        }
    };

    @Override
    public String getFragmentName() {
        return TAG;// 不知道该方法有没有用
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.boss_info_one_view, container,
                false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        bindViewListenter();
        new ThreadUtil(mActivity, this).start();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MessageRedPointsImp.getInstance().add(this);
    }

    public void initView(View view) {
        mNoReadInfoTv = (TextView) view.findViewById(R.id.no_read_info_tv);
        mReadAllTv = (TextView) view.findViewById(R.id.read_all_tv);
        mMessageList = (SwipeMenuListView) view.findViewById(R.id.message_list);
        mMessageAdapter = new MessageAdapter(mActivity, mDragCallback);
        mMessageAdapter.setListView(mMessageList);
        mPopLayout = (LinearLayout) view.findViewById(R.id.pop_layout);
        initHeadView();
        updateNonetData();
    }

    public void bindViewListenter() {
        mViewPager.setOnPageChangeListener(this);
        mNoReadInfoTv.setOnClickListener(this);
        mReadAllTv.setOnClickListener(this);
        mMessageList.setOnScrollListener(this);
        mNeedDoTv.setOnClickListener(this);
        mNotifyTv.setOnClickListener(this);
        mMenuBtn.setOnClickListener(this);
    }

    @Override
    public boolean execute() {
        if ("0".equals(mFlag)) {
            return oneKeyCancel();
        } else {
            return getData();
        }
    }

    public boolean getData() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            // 得到客户、合同、商机等等
            map.put("isboss", "1");
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

            String jsonStr1 = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + url, map);
            mainResultVO = gson.fromJson(jsonStr1, MainResultVO.class);

            if (Constant.RESULT_CODE.equals(mainResultVO.getCode())) {
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
            map.put("isboss", "1");
            map.put("isapp", "1");// 查看全部审批
            map.put("istask", "1");// 查看全部任务
            map.put("isidea", "1");// 查看全部意见
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle()
                    + Constant.MAIN_ONEKEY_CANCEL, map);
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
    public void executeSuccess() {
        if ("0".equals(mFlag)) {
            if ("99".equals(mType)) {
                mReadAllTv.setText("全部已读");
                mReadAllPopTv.setText("全部已读");
                mReadAllTv.setTextColor(mActivity.getResources().getColor(R.color.C5));
                mReadAllPopTv.setTextColor(mActivity.getResources().getColor(R.color.C5));

            }
            mFlag = "1";
            new ThreadUtil(mActivity, BossMessageFragment.this).start();
            return;
        }

        if (!"1".equals(mFlag)) {
            mFlag = "1";
            new ThreadUtil(mActivity, BossMessageFragment.this).start();
            return;
        }

        mMainVOList = mainResultVO.getArray();
        list = new ArrayList<MainVO>();
        for (int i = 0; i < mMainVOList.size(); i++) {
            if ("20".equals(mMainVOList.get(i).getType())) {

                if (!"0".equals(CommonUtils.isNormalCount(mMainVOList.get(i).getCount()))) {
                    mNeedDoCountTv.setText(CommonUtils.isNormalCount(mMainVOList.get(i).getCount()));
                    mNeedDoCountTv.setBackground(CommonUtils.setBackgroundShap(mActivity, 100, R.color.C9, R.color.C9));
                }
                continue;
            }

            if ("21".equals(mMainVOList.get(i).getType())) {
                if (!"0".equals(CommonUtils.isNormalCount(mMainVOList.get(i).getCount()))) {
                    mNotifyCountTv.setText(CommonUtils.isNormalCount(mMainVOList.get(i).getCount()));
                    mNotifyCountTv.setBackground(CommonUtils.setBackgroundShap(mActivity, 100, R.color.C9, R.color.C9));
                }
                continue;
            }
            if ("24".equals(mMainVOList.get(i).getType())) {
                mViewPagerFragment1.updateData(mMainVOList.get(i).getData());
                continue;
            }
            if ("22".equals(mMainVOList.get(i).getType())) {
                mViewPagerFragment2.updateData(mMainVOList.get(i).getData());
                continue;
            }
            if ("23".equals(mMainVOList.get(i).getType())) {
                mViewPagerFragment3.updateData(mMainVOList.get(i).getData());
                continue;
            }

            if ("0".equals(mMainVOList.get(i).getType())
                    || "4".equals(mMainVOList.get(i).getType()))
                continue;
            list.add(mMainVOList.get(i));
            MainVO vo = mMainVOList.get(i);

            if (!"0".equals(CommonUtils.isNormalCount(mMainVOList.get(i).getCount()))) {
                mReadAllTv.setText("标记全部为已读");
                mReadAllPopTv.setText("标记全部为已读");
                mReadAllTv.setTextColor(mActivity.getResources().getColor(R.color.C7));
                mReadAllPopTv.setTextColor(mActivity.getResources().getColor(R.color.C7));
            }

        }
        mMessageAdapter.updateData(list);

        if (CommonUtils.showLead(mActivity, "BossMessageFragment")) {
            initLeadView();
        }
        

        initChatHistorydata();// 初始化聊天消息
    }

    @Override
    public void executeFailure() {
        mFlag = "1";
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        mFlowDot.setSeletion(arg0);
    }

    @Override
    public void onClick(View arg0) {
        Intent intent = new Intent();
        switch (arg0.getId()) {
            case R.id.read_all_tv:
                if ("全部已读".equals(mReadAllTv.getText().toString()))
                    return;
                mFlag = "0";
                mType = "99";
                new ThreadUtil(mActivity, BossMessageFragment.this).start();
                mMessageAdapter.setFlag(true);
                mMessageAdapter.notifyDataSetChanged();
                mHandler.sendEmptyMessage(2);
                break;
            case R.id.need_do_tv:
                intent.setClass(mActivity, BossNeedDoActivity.class);
                mActivity.startActivity(intent);
                break;
            case R.id.notify_tv:
                intent.setClass(mActivity, BossNotifyActivity.class);
                mActivity.startActivity(intent);
                break;
            case R.id.menu_inco:
                menuSelected.onMenuClick();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDrag(View view) {
    }

    public void cleanMessage() {
        mFlag = "1";
        new ThreadUtil(mActivity, BossMessageFragment.this).start();
    }

    public void initHeadView() {
        View headView = View.inflate(mActivity, R.layout.boss_msg_header, null);
        mMessageList.addHeaderView(headView);
        mFlowDot = (FlowIndicator) headView.findViewById(R.id.flow_dot);
        mViewPager = (BSViewPager) headView.findViewById(R.id.view_pager_bossone);
        mListFragment = new ArrayList<Fragment>();
        mSurveyPageAdapter = new ViewPagerAdapter(((FragmentActivity) mActivity).getSupportFragmentManager(), mListFragment);
        mViewPager.setAdapter(mSurveyPageAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mNotifyCountTv = (TextView) headView.findViewById(R.id.notify_count_tv);
        mNeedDoCountTv = (TextView) headView.findViewById(R.id.need_do_count_tv);
        mViewPagerFragment1 = new BossVpagerFragment1();
        mViewPagerFragment2 = new BossVpagerFragment2();
        mViewPagerFragment3 = new BossVpagerFragment3();
        mListFragment.add(mViewPagerFragment2);
        mListFragment.add(mViewPagerFragment3);
        mListFragment.add(mViewPagerFragment1);
        mSurveyPageAdapter.notifyDataSetChanged();
        mFlowDot.setCount(mListFragment.size());
        mFlowDot.setSeletion(0);
        mMenuBtn = (ImageButton) headView.findViewById(R.id.menu_inco);
        mNeedDoTv = (TextView) headView.findViewById(R.id.need_do_tv);
        mNotifyTv = (TextView) headView.findViewById(R.id.notify_tv);
        View popView = View.inflate(mActivity, R.layout.boss_msg_pop, null);
        mReadAllPopTv = (TextView) popView.findViewById(R.id.read_all_tv);
        mReadAllPopTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if ("全部已读".equals(mReadAllPopTv.getText().toString()))
                    return;
                mFlag = "0";
                mType = "99";
                mReadAllTv.setText("全部已读");
                mReadAllPopTv.setText("全部已读");
                mReadAllTv.setTextColor(mActivity.getResources().getColor(R.color.C5));
                mReadAllPopTv.setTextColor(mActivity.getResources().getColor(R.color.C5));
                new ThreadUtil(mActivity, BossMessageFragment.this).start();
                mMessageAdapter.setFlag(true);
                mMessageAdapter.notifyDataSetChanged();
                mHandler.sendEmptyMessage(2);
            }
        });
        mMessageList.addHeaderView(popView);// ListView条目中的悬浮部分
        // 添加到头部
        mMessageList.setAdapter(mMessageAdapter);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem >= 2) {
            mPopLayout.setVisibility(View.VISIBLE);
        } else {
            mPopLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView arg0, int arg1) {

    }

    // 没有网络的死数据
    public void updateNonetData() {
        List<MainVO> list = new ArrayList<MainVO>();
        for (int i = 7; i <= 19; i++) {
            if (i == 13 || i == 14 || i == 15 || i == 16 || i == 17 || i == 18)
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
                case 9:
                    mainVO.setTypename("企业风采");
                    mainVO.setTitle("企业风采");
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
                    mainVO.setTypename("企业云");
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
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            new ThreadUtil(mActivity, BossMessageFragment.this).start();
        }
    }

    public void initLeadView() {
        LinearLayout.LayoutParams okTvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);// 定义文本显示组件
        okTvParams.rightMargin = CommonUtils.dip2px(mActivity, 10);
        LinearLayout.LayoutParams okImgParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, CommonUtils.dip2px(mActivity, 75));// 定义文本显示组件
        okImgParams.gravity = Gravity.RIGHT;
        okImgParams.rightMargin = CommonUtils.dip2px(mActivity, 20) + CommonUtils.getViewWidth(mReadAllPopTv) / 2;
        okImgParams.topMargin = CommonUtils.dip2px(mActivity, 30);
        mPop = CommonUtils.leadPop(mActivity, mReadAllPopTv, "新增即时聊天了，聊天消息都在这里，快来和小伙伴聊天吧！", okTvParams, okImgParams, 1);

        mListpop.add(mPop);
        CommonUtils.okLeadPop(mActivity, mReadAllPopTv, mListpop, 1);
    }

    public interface MenuBossSelected {
        void onMenuClick();
    }

    public void setMenuSelected(MenuBossSelected menuSelected) {
        this.menuSelected = menuSelected;
    }

    @Override
    public void updateNotify(Object content) {
        Message message = mHandler.obtainMessage();
        message.what = 1;
        message.obj = content;
        mHandler.sendMessage(message);

    }

    private void initChatHistorydata() {
        if (list.get(0).equals("20")) {
            return;// 说明已经存在了
        }

        if (mainVO == null) {
            mainVO = new MainVO();
        }
        mainVO.setCount(IMManager.getInstance(mActivity).getUnreadCountAll() + "");
        mainVO.setType("20");
        mainVO.setTypename("消息沟通");
        mainVO.setIstop("0");
        if (IMManager.getInstance(mActivity).getUnreadCountAll() == 0) {
            mainVO.setTitle("还没有最新的消息哦~");
        }
        else {
            mainVO.setTitle(IMManager.getInstance(mActivity).getUnreadCountAll() + "条新的消息,赶快打开看吧");
        }
        mainVO.setTime(System.currentTimeMillis() / 1000 + "");
        if (list != null && mMessageAdapter != null) {
            list.add(0, mainVO);
            mMessageAdapter.updateData(list);
        }
    }

    @Override
    public void onDestroy() {
        MessageRedPointsImp.getInstance().remove(this);
        super.onDestroy();
    }
}
