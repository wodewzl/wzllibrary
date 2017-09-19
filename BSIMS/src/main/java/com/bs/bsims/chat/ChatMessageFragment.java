
package com.bs.bsims.chat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.chatutils.ConcatInfoUtils;
import com.bs.bsims.chatutils.IObserverListener;
import com.bs.bsims.fragment.BaseFragment;
import com.bs.bsims.model.DepartmentAndEmployeeVO;
import com.bs.bsims.observer.BSApplictionObserver.Watcher;
import com.bs.bsims.observer.ImSdkGetInfoImp;
import com.bs.bsims.onekey.remove.DropCover.OnDragCompeteListener;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.view.BSCircleImageView;
import com.bs.bsims.view.BSRrefshViewDelete;
import com.bs.bsims.view.RippleView;
import com.bs.bsims.view.RippleView.OnRippleCompleteListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yzxIM.IMManager;
import com.yzxIM.data.CategoryId;
import com.yzxIM.data.MSGTYPE;
import com.yzxIM.data.db.ChatMessage;
import com.yzxIM.data.db.ConversationInfo;
import com.yzxIM.data.db.DiscussionInfo;
import com.yzxIM.listener.IConversationListener;
import com.yzxIM.listener.MessageListener;
import com.yzxtcp.tools.CustomLog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * BS北盛最帅程序员 Copyright (c) 2016 湖北北盛科技有限公司
 * 
 * @author 梁骚侠
 * @date 2016-4-26
 * @version 2.0
 */
public class ChatMessageFragment extends BaseFragment implements
        IConversationListener, MessageListener, Watcher {
    public interface refreshUnReadMessageListener {
        public void onRefreshUnReadMessage();
    }

    private static final String TAG = "ChatMessageFragment";

    private static final int NOTIFAY_VOICE_VIBRATOR = 406;// 收到消息推送
    private boolean isDestoryView;// 判断是否处理当前的view
    // 没网络错误
    private static final int CONNECT_SUCCESS = 400;
    // 没网络错误
    private static final int NET_ERROR = 402;
    // 网络链接成功
    private static final int NET_CNNECT = 404;
    // SDK正在链接
    private static final int SDK_CONNECTING = 406;
    // 服务器不可用
    private static final int SERVER_ERROR = 408;
    private List<ConversationInfo> conversationLists = new ArrayList<ConversationInfo>();
    private ChatMessageHomeAdapter chatMessageHomeAdapter;
    private BSRrefshViewDelete bsListView;
    private Activity mContext;
    private int topNum = 0;
    private refreshUnReadMessageListener mListener;
    private LinearLayout ll_network;
    private TextView mNoNetWorkTv;
    private List<IObserverListener> observes = new ArrayList<IObserverListener>();
    private int status;
    private View mView;
    private LinearLayout.LayoutParams mParams;
    private String mNoHeadColors[] = {
            "#7A929E", "#6194FF", "#65BEE6", "#F75E8C", "#39C3B4", "#FD953C", "#9B89B9"
    };
    private int delpostion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IMManager.getInstance(getActivity()).setConversationListener(this);
        IMManager.getInstance(getActivity()).setSendMsgListener(this);
        // IMManager.getInstance(getActivity()).clearAllConversations();
        ImSdkGetInfoImp.getInstance().add(this);// 监听sdk的变化
        initdata();
    }

    private void initdata() {
        conversationLists = IMManager.getInstance(
                getActivity().getApplicationContext()).getConversationList();
        List<ConversationInfo> removeinfos = new ArrayList<ConversationInfo>();
        List<ConversationInfo> lists = new ArrayList<ConversationInfo>();
        lists.addAll(conversationLists);
        int size = lists.size();
        for (int i = 0; i < size; i++) {
            // 移出没有消息的会话
            if (null == lists.get(i).getLastestMessages(0, 1)) {
                removeinfos.add(lists.get(i));
                break;
            }

        }
        conversationLists.removeAll(removeinfos);
    }

    public void initEvets() {
        bsListView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub

                for (int i = 1; i < conversationLists.size() + 1; i++) {
                    if (view.getChildAt(i) != null) {
                        chatMessageHomeAdapter.scrollView((HorizontalScrollView) view.getChildAt(i), HorizontalScrollView.FOCUS_LEFT);
                    }
                }
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        mContext = activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mView = inflater.inflate(R.layout.im_messagehome_activity, container, false);
        isDestoryView = false;
        chatMessageHomeAdapter = new ChatMessageHomeAdapter(this.getActivity());
        bsListView = (BSRrefshViewDelete) mView.findViewById(R.id.im_message_list);
        bsListView.setAdapter(chatMessageHomeAdapter);
        ll_network = (LinearLayout) mView.findViewById(R.id.ll_network);
        mNoNetWorkTv = (TextView) mView.findViewById(R.id.sdk_status_text);
        mParams = new LinearLayout.LayoutParams(CommonUtils.getScreenWid(getActivity()),
                LinearLayout.LayoutParams.WRAP_CONTENT);
        updateSdkStatusUI(status);
        initEvets();
        return mView;
    }

    // 更新sdk状态提示UI
    private void updateSdkStatusUI(int sdkStatus) {
        // if (ll_network == null) {
        // CustomLog.e("ll_network == null");
        // return;
        // }
        // ll_network.setOnClickListener(null);
        switch (sdkStatus) {
            case NET_ERROR:
                ll_network.setVisibility(View.VISIBLE);
                break;
            case SERVER_ERROR:
                ll_network.setVisibility(View.VISIBLE);
                mNoNetWorkTv.setText("服务不可用，请点击连接");
                ll_network.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        updateSdkStatusUI(SDK_CONNECTING);
                        ConcatInfoUtils.getInstance().getIMJavaBean();
                    }
                });
                break;
            case SDK_CONNECTING:
                ll_network.setVisibility(View.VISIBLE);
                mView.findViewById(R.id.sdk_connected_error).setVisibility(View.GONE);
                mView.findViewById(R.id.sdk_connecting).setVisibility(View.VISIBLE);
                mNoNetWorkTv.setText("正在连接，请稍后…");
                break;
            case NET_CNNECT:
            case CONNECT_SUCCESS:
                ll_network.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    // 重新加载
                    if (conversationLists.size() == 0) {
                        mView.invalidate();
                        topNum = 0;
                    }
                    break;
                case 2:
                    int sdkStatus = (Integer) msg.obj;
                    updateSdkStatusUI(sdkStatus);
                    break;

                case 4:
                    ConversationInfo delinfo = (ConversationInfo) msg.obj;
                    List<ConversationInfo> lists = new ArrayList<ConversationInfo>();
                    lists.addAll(conversationLists);
                    synchronized (conversationLists) {
                        for (int position = 0; position < lists.size(); position++) {
                            ConversationInfo conversation = lists.get(position);
                            if (conversation.getTargetId().equals(
                                    delinfo.getTargetId())) {
                                conversationLists.remove(position);
                                chatMessageHomeAdapter.notifyDataSetChanged();
                                break;
                            }
                        }
                    }

                    mHandler.sendEmptyMessage(1);
                    break;
                case 5:
                    // 要更新的会话集合
                    List<ConversationInfo> updateInfos = (List<ConversationInfo>) msg.obj;
                    // 临时总会话集合
                    List<ConversationInfo> cuInfos = new ArrayList<ConversationInfo>();
                    if (conversationLists != null && conversationLists.size() > 0) {
                        cuInfos.addAll(conversationLists);
                    }
                    // 将所有更新和添加的会话都放到cuInfos集合临时存储
                    for (ConversationInfo info : updateInfos) {
                        ConversationInfo item = getInfo(info);
                        if (item == null) {
                            // 为新创建的会话设置title
                            if (info.getCategoryId() == CategoryId.GROUP) {
                                // List<GroupBean> groups = RestTools.getGroupInfo();
                                // if (groups != null) {
                                // for (GroupBean group : groups) {
                                // if (info.getTargetId().equals(group.getGroupID())
                                // && !group.getGroupName().equals(
                                // info.getConversationTitle())) {
                                // info.setConversationTitle(group.getGroupName());
                                // }
                                // }
                                // }
                            } else if (info.getCategoryId() == CategoryId.PERSONAL) {
                                // String title = ContactTools.getConTitle(info.getTargetId());
                                // if(!TextUtils.isEmpty(title)){
                                // info.setConversationTitle(title);
                                // }
                            }
                            // 添加到临时集合
                            cuInfos.add(topNum, info);
                        } else {
                            if (info.getCategoryId() == CategoryId.DISCUSSION) {
                                DiscussionInfo discussion = IMManager.getInstance(getActivity())
                                        .getDiscussionInfo(info.getTargetId());
                                if (discussion != null) {
                                    info.setConversationTitle(discussion.getDiscussionName());
                                }
                            }
                            // 更新临时集合
                            updataCinfo(cuInfos, info, item);
                        }
                    }
                    synchronized (conversationLists) {
                        conversationLists.clear();
                        conversationLists.addAll(cuInfos);
                        chatMessageHomeAdapter.notifyDataSetChanged();
                    }

                    sendEmptyMessage(1);
                    break;
                case 6:
                    chatMessageHomeAdapter.notifyDataSetChanged();
                    break;
                case 7:
                    Map<String, ConversationInfo> datas = (Map<String, ConversationInfo>) msg.obj;
                    // Log.i(TAG, "收到一次性更新会话消息 size = " + datas.size());

                    Iterator<Entry<String, ConversationInfo>> iterator = datas
                            .entrySet().iterator();
                    int delayIndex = 0;
                    while (iterator.hasNext()) {
                        Entry<String, ConversationInfo> entry = iterator.next();
                        ConversationInfo conversationInfo = entry.getValue();
                        Message message = mHandler.obtainMessage();
                        message.what = 5;
                        message.obj = conversationInfo;
                        mHandler.sendMessageDelayed(message, delayIndex * 50);
                        delayIndex++;
                    }
                    break;

            }
        }
    };

    private void updataCinfo(List<ConversationInfo> infos, ConversationInfo cinfoSrc,
            ConversationInfo cinfoDest) {
        synchronized (infos) {
            infos.remove(cinfoDest);
            cinfoDest.setDraftMsg(cinfoSrc.getDraftMsg());
            cinfoDest.setLastTime(cinfoSrc.getLastTime());
            if (cinfoSrc.getCategoryId() == CategoryId.DISCUSSION) {
                cinfoDest.setConversationTitle(cinfoSrc.getConversationTitle());
            } else if (cinfoSrc.getCategoryId() == CategoryId.PERSONAL) {
                List<ChatMessage> mgs = cinfoDest.getLastestMessages(0, 1);
                // 会话title为sendId的时候
                if (mgs != null && mgs.size() > 0 && !TextUtils.isEmpty(mgs.get(0).getNickName())
                        && cinfoDest.getConversationTitle().equals(mgs.get(0).getSenderId())) {
                    cinfoDest.setConversationTitle(mgs.get(0).getNickName());
                }
            }
            if (cinfoDest.getIsTop()) {
                infos.add(0, cinfoDest);
            } else {
                infos.add(topNum, cinfoDest);
            }
        }
    }

    /**
     * 根据会话在会话集合里面找到相同会话Id的会话
     * 
     * @param cinfo
     * @return
     */
    private ConversationInfo getInfo(ConversationInfo cinfo) {
        ConversationInfo info = null;
        List<ConversationInfo> clists = new ArrayList<ConversationInfo>();
        clists.addAll(conversationLists);
        // 为群组会话设置title
        synchronized (conversationLists) {
            for (ConversationInfo conversation : clists) {
                if (conversation.getTargetId().equals(cinfo.getTargetId())) {
                    info = conversation;
                    // 当群主title为空的时候更新title
                    if (info.getCategoryId() == CategoryId.GROUP
                            && TextUtils.isEmpty(info.getConversationTitle())) {
                        // List<GroupBean> groups = RestTools.getGroupInfo();
                        // if (null != groups) {
                        // for (GroupBean group : groups) {
                        // if (info.getTargetId().equals(
                        // group.getGroupID())
                        // && !group.getGroupName().equals(
                        // info.getConversationTitle())) {
                        // info.setConversationTitle(group
                        // .getGroupName());
                        // }
                        // }
                        // }
                    }
                    break;
                }
            }
        }
        return info;
    }

    @Override
    public String getFragmentName() {
        // TODO Auto-generated method stub
        return TAG;
    }

    public void handSdkStatus(Object status) {
        Message message = mHandler.obtainMessage();
        message.what = 2;
        message.obj = status;
        this.status = (Integer) status;
        mHandler.sendMessage(message);
    }

    @Override
    public void onDownloadAttachedProgress(String arg0, String arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onReceiveMessage(List arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSendMsgRespone(ChatMessage arg0) {
        // TODO Auto-generated method stub
        mHandler.sendEmptyMessage(6);
    }

    @Override
    public void onCreateConversation(ConversationInfo cinfo) {
        // TODO Auto-generated method stub

    }

    public void onDeleteConversation(ConversationInfo cinfo) {
        // TODO Auto-generated method stub
        Message message = mHandler.obtainMessage();
        message.obj = cinfo;
        message.what = 4;
        mHandler.sendMessage(message);
    }

    @Override
    public void onUpdateConversation(List arg0) {
        // 遍历回调集合，区分是更新还是增加会话
        if (arg0 == null || arg0.size() <= 0) {
            CustomLog.e("onUpdateConversation lists size == 0 ?");
            return;
        }
        List<ConversationInfo> infos = (List<ConversationInfo>) arg0;
        Log.i(TAG, "onUpdateConversation size = " + arg0.size());
        Message message = mHandler.obtainMessage();
        message.obj = infos;
        message.what = 5;
        mHandler.sendMessage(message);
    }

    // 更新发送失败UI
    public void updateErrorMsgUI() {
        if (mListener != null) {
            mListener.onRefreshUnReadMessage();
        }
        chatMessageHomeAdapter.notifyDataSetChanged();
    }

    public void onResume() {
        super.onResume();
        updateErrorMsgUI();
        checkNoConnect();
    }

    public void checkNoConnect() {
        if (!BSApplication.getInstance().isAppliactionChat()) {
            updateSdkStatusUI(SERVER_ERROR);
        }

    }

    @Override
    public void onDestroyView() {
        isDestoryView = true;
        super.onDestroyView();

    }

    public void setRefreshUnReadMessageListener(refreshUnReadMessageListener cl) {
        mListener = cl;
    }

    @Override
    public void onDestroy() {
        ImSdkGetInfoImp.getInstance().remove(this);
        super.onDestroy();
    }

    /**
     * BS北盛最帅程序员 Copyright (c) 2016 湖北北盛科技有限公司
     * 
     * @author 梁骚侠
     * @date 2016-4-14
     * @version 2.0
     */
    public class ChatMessageHomeAdapter extends BaseAdapter implements
            OnDragCompeteListener {
        public ImageLoader mImageLoader;
        public DisplayImageOptions mOptions;
        private Activity mActivity;
        /** HorizontalScrollView左右滑动事件 */
        private ScrollViewScrollImpl mScrollImpl;

        private int finalitempstion;
        /** 记录滑动出删除按钮的itemView */
        public HorizontalScrollView mScrollView;

        /** touch事件锁定,如果已经有滑动出删除按钮的itemView,就屏蔽下一整次(down,move,up)的onTouch操作 */
        public boolean mLockOnTouch = false;

        public ChatMessageHomeAdapter(Context context) {
            this.mActivity = (Activity) context;
            if (conversationLists.size() != 0) {
                topNum = getTopNum(conversationLists);
            }

            if (null != mListener) {
                mListener.onRefreshUnReadMessage();
            }
            mImageLoader = ImageLoader.getInstance();
            mOptions = CommonUtils.initImageLoaderOptions();
        }

        public void notifyDataSetChanged() {
            if (null != mListener) {
                mListener.onRefreshUnReadMessage();
            }
            List<ConversationInfo> list = new ArrayList<ConversationInfo>();
            list.addAll(conversationLists);
            if (isDestoryView) {
                return;
            }
            super.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return conversationLists.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return conversationLists.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @SuppressLint("NewApi")
        @SuppressWarnings("null")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            final ConversationInfo chatMessageHomeBean = conversationLists.get(position);
            if (convertView == null) {
                holder = new ViewHolder();
                // convertView = setContentView(chatMessageHomeBean);
                convertView = View.inflate(mActivity,
                        R.layout.im_chatmessage_homeadapter_group1, null);
                holder.mItemImg = (BSCircleImageView) convertView
                        .findViewById(R.id.item_icon);
                holder.mItemHeadvi = (TextView) convertView
                        .findViewById(R.id.item_icon_tv);
                holder.mItemTitleTv = (TextView) convertView
                        .findViewById(R.id.item_title);
                holder.mItemDepictTv = (TextView) convertView
                        .findViewById(R.id.item_depict);
                holder.mItemWaterDrop = (TextView) convertView
                        .findViewById(R.id.item_water_drop);
                holder.mItemLayout = (LinearLayout) convertView
                        .findViewById(R.id.item_layout);
                holder.mTimeTv = (TextView) convertView.findViewById(R.id.item_tv);
                holder.mItemTop = (TextView) convertView.findViewById(R.id.item_top);
                holder.mDeleteItem = (ImageButton) convertView.findViewById(R.id.item_delete);
                holder.mRippleView = (RippleView) convertView.findViewById(R.id.item_layout_1);
                holder.mRippleView.setLayoutParams(mParams);
                holder.scrollView = (HorizontalScrollView) convertView;
                holder.scrollView.setOnTouchListener(mScrollImpl);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (DateUtils.parseYMDLong(chatMessageHomeBean.getLastTime()).equals(DateUtils.getCurrentDate())) {
                holder.mTimeTv.setText(DateUtils.parseHMLong(chatMessageHomeBean.getLastTime()));
            }
            else {
                if (DateUtils.parseYLong(chatMessageHomeBean.getLastTime()).equals(String.valueOf(DateUtils.getCurrentYear()))) {
                    holder.mTimeTv.setText(DateUtils.parseMDLong(chatMessageHomeBean.getLastTime()));
                }
                else {
                    holder.mTimeTv.setText(DateUtils.parseDate(chatMessageHomeBean.getLastTime()));
                }

            }

            // 单聊才可以这样加载头像
            if (chatMessageHomeBean.getCategoryId() == CategoryId.PERSONAL) {
                holder.mItemHeadvi.setVisibility(View.GONE);
                holder.mItemImg.setVisibility(View.VISIBLE);
                // 当会话消息的id不等于当前登录人的id的话则会话消息显示接收方的id
                if (!chatMessageHomeBean.getTargetId().equals(BSApplication.getInstance().getIMjavaBean().getClient().get(0).getUserId()))
                {
                    try {
                        DepartmentAndEmployeeVO vAndEmployeeVO = ConcatInfoUtils.getInstance().getUserByIM(chatMessageHomeBean.getTargetId());
                        mImageLoader.displayImage(vAndEmployeeVO.getHeadpic(),
                                holder.mItemImg, mOptions);
                        holder.mItemTitleTv.setText(vAndEmployeeVO.getFullname());
                    } catch (Exception e) {
                        mImageLoader.displayImage("",
                                holder.mItemImg, mOptions);
                        holder.mItemTitleTv.setText("");
                    }

                }
                // 否则显示发送方的id也是就是自己的id
                else {
                    mImageLoader.displayImage(BSApplication.getInstance().getUserFromServerVO().getHeadpic(),
                            holder.mItemImg, mOptions);
                    holder.mItemTitleTv.setText(BSApplication.getInstance().getUserFromServerVO().getFullname());
                }
                holder.mItemDepictTv.setText(chatMessageHomeBean.getDraftMsg().trim());
            }
            // 讨论组
            else if (chatMessageHomeBean.getCategoryId() == CategoryId.DISCUSSION) {
                holder.mItemHeadvi.setVisibility(View.VISIBLE);
                holder.mItemImg.setVisibility(View.GONE);
                String color = mNoHeadColors[position % mNoHeadColors.length];
                holder.mItemHeadvi.setBackground(CommonUtils.setBackgroundShap(mContext, 100, color, color));
                holder.mItemHeadvi.setText("讨论组");
                holder.mItemHeadvi.setTextSize(12);
                holder.mItemHeadvi.setTextColor(getResources().getColor(R.color.white));
                holder.mItemTitleTv.setText(chatMessageHomeBean.getConversationTitle().trim());

                if (chatMessageHomeBean.getDraftMsg().contains("加入讨论组")) {
                    holder.mItemDepictTv.setText("加入讨论成功,大家开始发言吧");
                }
                else if (chatMessageHomeBean.getDraftMsg().contains("退出讨论组")) {
                    holder.mItemDepictTv.setText(ConcatInfoUtils.getInstance().trantleStr(chatMessageHomeBean.getDraftMsg()));
                }

                else if (chatMessageHomeBean.getDraftMsg().contains("你已经移除")) {
                    holder.mItemDepictTv.setText(ConcatInfoUtils.getInstance().trantleStr(chatMessageHomeBean.getDraftMsg()));
                }
                else if (chatMessageHomeBean.getDraftMsg().contains("讨论组名修改为")) {
                    holder.mItemDepictTv.setText(ConcatInfoUtils.getInstance().trantleStr(chatMessageHomeBean.getDraftMsg()));
                }
                else if (chatMessageHomeBean.getDraftMsg().contains("修改讨论组名为")) {
                    holder.mItemDepictTv.setText(ConcatInfoUtils.getInstance().trantleStr(chatMessageHomeBean.getDraftMsg()));
                }
                else if (chatMessageHomeBean.getDraftMsg().contains("移除")) {
                    holder.mItemDepictTv.setText(ConcatInfoUtils.getInstance().trantleStr(chatMessageHomeBean.getDraftMsg()));
                }
                else {
                    holder.mItemDepictTv.setText(chatMessageHomeBean.getDraftMsg().trim());
                }
            }
            /* 126版本屏蔽系统消息 由于无法去掉小红点，还是加上 */
            else if (chatMessageHomeBean.getCategoryId() == CategoryId.BROADCAST) {
                holder.mItemHeadvi.setVisibility(View.VISIBLE);
                holder.mItemImg.setVisibility(View.GONE);
                String color = mNoHeadColors[position % mNoHeadColors.length];
                holder.mItemHeadvi.setBackground(CommonUtils.setBackgroundShap(mContext, 100, color, color));
                holder.mItemHeadvi.setText("广播");
                holder.mItemHeadvi.setTextSize(12);
                holder.mItemHeadvi.setTextColor(getResources().getColor(R.color.white));
                holder.mItemTitleTv.setText(chatMessageHomeBean.getConversationTitle().trim());
                holder.mItemDepictTv.setText(chatMessageHomeBean.getDraftMsg().trim());
            }

            int unreadcount = chatMessageHomeBean.getUnreadCount();
            if (unreadcount != 0) {
                if (unreadcount > 99) {
                    holder.mItemWaterDrop.setText("99+");
                } else {
                    holder.mItemWaterDrop.setText(String
                            .valueOf(unreadcount));
                }
                holder.mItemWaterDrop.setVisibility(View.VISIBLE);
            } else {
                holder.mItemWaterDrop.setVisibility(View.INVISIBLE);
                holder.mItemWaterDrop.setText("");
            }

            final Boolean isTop = chatMessageHomeBean.getIsTop();
            if (isTop) {
                holder.mItemTop.setText("取消置顶");
            }
            else {
                holder.mItemTop.setText("置顶聊天");
            }
            holder.mItemTop.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            scrollView(holder.scrollView, HorizontalScrollView.FOCUS_LEFT);
                            conversationLists.get(position).setIsTop(!isTop);
                            IMManager.getInstance(getActivity()).sortConversationList(conversationLists);
                            topNum = getTopNum(conversationLists);
                            chatMessageHomeAdapter.notifyDataSetChanged();
                        }
                    });
                }
            });
            holder.mDeleteItem.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            scrollView(holder.scrollView, HorizontalScrollView.FOCUS_LEFT);
                            chatMessageHomeBean.delConversationInfo();
                            synchronized (conversationLists) {
                                conversationLists
                                        .remove(position);
                            }
                            chatMessageHomeAdapter.notifyDataSetChanged();
                            if (chatMessageHomeBean.getIsTop() && topNum > 0) {
                                topNum--;
                            }
                            if (conversationLists.size() == 0) {
                                topNum = 0;
                            }
                        }
                    });
                }
            });
            List<ChatMessage> msgs = chatMessageHomeBean.getLastestMessages(0, 1);
            if (msgs != null && msgs.size() > 0) {
                if (null != msgs && msgs.size() > 0) {
                    // 如果最后一条消息是自定义消息，颜色重置
                    if (msgs.get(0).getMsgType() == MSGTYPE.MSG_DATA_CUSTOMMSG) {
                        holder.mItemDepictTv.setTextColor(getResources().getColor(R.color.C5));
                    }
                    switch (msgs.get(0).getSendStatus()) {
                        case ChatMessage.MSG_STATUS_INPROCESS:
                        case ChatMessage.MSG_STATUS_RETRY:
                            if (msgs.get(0).getIsFromMyself()) {
                                holder.mTimeTv.setText("正在发送中");
                            } else {
                                holder.mTimeTv.setText("正在接收中");
                            }
                            // 发送中

                            // viewHolder.conversation_sendstatus
                            // .setBackgroundResource(R.drawable.send_ing);
                            // viewHolder.conversation_sendstatus
                            // .setVisibility(View.VISIBLE);
                            break;
                        case ChatMessage.MSG_STATUS_SUCCESS:
                            // 发送成功
                            // viewHolder.conversation_sendstatus
                            // .setVisibility(View.GONE);
                            holder.mItemDepictTv.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                            break;
                        case ChatMessage.MSG_STATUS_FAIL:
                        case ChatMessage.MSG_STATUS_NETERROR:
                        case ChatMessage.MSG_STATUS_TIMEOUT:
                            holder.mItemDepictTv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.im_nosend), null, null, null);
                            // 发送失败
                            // viewHolder.conversation_sendstatus
                            // .setBackgroundResource(R.drawable.send_fail);
                            // viewHolder.conversation_sendstatus
                            // .setVisibility(View.VISIBLE);
                            break;
                    }
                } else {
                    holder.mItemDepictTv.setText("");
                }
            }
            holder.mRippleView.setOnRippleCompleteListener(new OnRippleCompleteListener() {

                @Override
                public void onComplete(RippleView rippleView) {
                    // TODO Auto-generated method stub
                    Intent i = new Intent();
                    i.putExtra("MsgUserID", chatMessageHomeBean.getTargetId());
                    chatMessageHomeBean.setConversationTitle(holder.mItemTitleTv.getText().toString().trim());
                    i.putExtra("conversation", chatMessageHomeBean);
                    CustomLog.e("chatTitle", holder.mItemTitleTv.getText().toString().trim() + "---------------" + holder.mItemTitleTv.getText());
                    i.setClass(mActivity, ChatMessageWithPerson.class);
                    mActivity.startActivity(i);
                }
            });

            holder.mItemLayout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub

                }
            });

            return convertView;
        }

        private View setContentView(ConversationInfo viewId) {
            View view = null;
            switch (viewId.getCategoryId()) {
            // 单聊消息
                case PERSONAL:
                    view = View.inflate(mActivity,
                            R.layout.im_chatmessage_homeadapter_group1, null);
                    break;
                // 群组消息
                case DISCUSSION:
                    view = View.inflate(mActivity,
                            R.layout.im_chatmessage_homeadapter_group2, null);
                    break;
            // case 3:
            // view = View.inflate(mActivity,
            // R.layout.im_chatmessage_homeadapter_group3, null);
            // break;
            // case 4:
            // view = View.inflate(mActivity,
            // R.layout.im_chatmessage_homeadapter_group4, null);
            // break;
            // case 5:
            // view = View.inflate(mActivity,
            // R.layout.im_chatmessage_homeadapter_group5, null);
            // break;
            // case 6:
            // view = View.inflate(mActivity,
            // R.layout.im_chatmessage_homeadapter_group6, null);
            // break;

            }
            return view;
        }

        @Override
        public void onDrag(View view) {
            // TODO Auto-generated method stub

        }

        /** HorizontalScrollView的滑动事件 */
        private class ScrollViewScrollImpl implements OnTouchListener {
            /** 记录开始时的坐标 */
            private float startX = 0;

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 如果有划出删除按钮的itemView,就让他滑回去并且锁定本次touch操作,解锁会在父组件的dispatchTouchEvent中进行
                        if (mScrollView != null) {
                            scrollView(mScrollView, HorizontalScrollView.FOCUS_LEFT);
                            mScrollView = null;
                            mLockOnTouch = true;
                            return true;
                        }
                        startX = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        HorizontalScrollView view = (HorizontalScrollView) v;
                        // 如果滑动了>50个像素,就显示出删除按钮
                        if (startX > event.getX() + 50) {
                            startX = 0;// 因为公用一个事件处理对象,防止错乱,还原startX值
                            scrollView(view, HorizontalScrollView.FOCUS_RIGHT);
                            mScrollView = view;
                        } else {
                            scrollView(view, HorizontalScrollView.FOCUS_LEFT);
                        }
                        break;
                }
                return mLockOnTouch;
            }
        }

        /** HorizontalScrollView左右滑动 */
        public void scrollView(final HorizontalScrollView view, final int parameter) {
            view.post(new Runnable() {
                @Override
                public void run() {
                    view.pageScroll(parameter);
                }
            });
        }
    }

    static class ViewHolder {
        private BSCircleImageView mItemImg;
        private TextView mItemWaterDrop;
        private TextView mItemTitleTv, mItemDepictTv, mTimeTv, mItemTop, mItemHeadvi;
        private LinearLayout mItemLayout;
        private RippleView mRippleView;
        private ImageButton mDeleteItem;
        private HorizontalScrollView scrollView;
    }

    private int getTopNum(List<ConversationInfo> cinfos) {
        int num = 0;
        for (ConversationInfo cinfo : cinfos) {
            if (cinfo.getIsTop()) {
                num++;
            } else {
                break;
            }
        }
        return num;
    }

    public void onUpdateConversation(ConversationInfo arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateNotify(Object content) {
        // TODO Auto-generated method stub
        handSdkStatus(content);
    }

}
