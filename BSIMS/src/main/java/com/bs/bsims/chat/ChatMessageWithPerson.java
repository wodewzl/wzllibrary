
package com.bs.bsims.chat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.bs.bsims.R;
import com.bs.bsims.activity.CrmGaoDeMapWithShowActivity;
import com.bs.bsims.activity.ImagePreviewActivity;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.chatutils.AudioManagers;
import com.bs.bsims.chatutils.AudioRecordButton;
import com.bs.bsims.chatutils.AudioRecordButton.AudioFinishRecorderListener;
import com.bs.bsims.chatutils.AudioRecordButton.AudioRecorderDownListener;
import com.bs.bsims.chatutils.ConversationBg;
import com.bs.bsims.chatutils.ConversationBgDbManager;
import com.bs.bsims.chatutils.MessagePop.IMessageHandlerListener;
import com.bs.bsims.chatutils.BitmapTools;
import com.bs.bsims.chatutils.ConcatInfoUtils;
import com.bs.bsims.chatutils.IMChatImageView;
import com.bs.bsims.chatutils.MessagePop;
import com.bs.bsims.chatutils.NotificationTools;
import com.bs.bsims.chatutils.VoiceStatus;
import com.bs.bsims.emoji.Emojicon;
import com.bs.bsims.emoji.EmojiconTextView;
import com.bs.bsims.emoji.EmojiconsFragment;
import com.bs.bsims.emoji.EmojiconGridFragment.OnEmojiconClickedListener;
import com.bs.bsims.emoji.EmojiconsFragment.OnEmojiconBackspaceClickedListener;
import com.bs.bsims.image.selector.ImageActivityUtils;
import com.bs.bsims.image.selector.MultiImageSelectorActivity;
import com.bs.bsims.model.DepartmentAndEmployeeVO;

import com.bs.bsims.utils.CommonImageUtils;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.WindowUtils;
import com.bs.bsims.view.BSCircleImageView;
import com.bs.bsims.view.BsPasteEditTextIM;
import com.bs.bsims.view.ChatListView;
import com.bs.bsims.view.ChatListView.OnRefreshListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import com.yzxIM.IMManager;
import com.yzxIM.data.CategoryId;
import com.yzxIM.data.MSGTYPE;
import com.yzxIM.data.db.ChatMessage;
import com.yzxIM.data.db.ConversationInfo;
import com.yzxIM.data.db.DiscussionChat;
import com.yzxIM.data.db.DiscussionInfo;
import com.yzxIM.data.db.GroupChat;
import com.yzxIM.data.db.LocationMapMsg;
import com.yzxIM.data.db.SingleChat;
import com.yzxIM.listener.MessageListener;
import com.yzxIM.listener.RecordListener;
import com.yzxtcp.UCSManager;
import com.yzxtcp.tools.CustomLog;
import com.yzxtcp.tools.NetWorkTools;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ChatMessageWithPerson extends FragmentActivity implements OnEmojiconClickedListener, OnEmojiconBackspaceClickedListener, OnClickListener, MessageListener, RecordListener, AudioFinishRecorderListener, AudioRecorderDownListener,
        SensorEventListener,
        IMessageHandlerListener
{

    public ImageLoader mImageLoader;
    public DisplayImageOptions mOptions;
    public List<ChatMessage> currentMsgList = new ArrayList<ChatMessage>();// 消息集合
    private List<String> msgTimeList = new ArrayList<String>();// 存放每条消息发送的时间
    private HashMap<Integer, String> msgTimeMap = new HashMap<Integer, String>();
    private AnimationDrawable voiceAnimation = null;// 语音动画
    private String mySelfHead, mySelfName, mySelfId;
    private AnimationDrawable animation;

    private static final int EMJOSELECT = 1;
    private static final int MORESELECT = 2;
    private Context mContext;
    private InputMethodManager manager;
    private Intent intent;
    private TextView mUserName;
    private BsPasteEditTextIM mEditTextContent;// 聊天输入框
    private Button mAddButton, mSendButton, mVoiceButton;// 更多和发送按钮
    private LinearLayout more, btnContainer;
    private View buttonSetModeKeyboard;
    private ImageView iv_emoticons_normal, mUserInfo;
    public String playMsgId;
    private AudioRecordButton mRecordButton;// 语音按钮 mRecordButton.setVoiceStatus(voiceStatus);
    private FrameLayout mEmjoListLy;// 表情 更多
    private InputMethodManager inputManager;// 键盘
    private ConversationInfo conversationinfo;// 当前会话消息类针对某一条

    private AudioManager mAudioManager;
    private EmojiconsFragment emojiconsFragment;
    private ChatMessageMoreFragment chatMessageMoreFragment;

    private ChatListView mMsgListView;
    private ChatMessageAdapter chatMessageHomeAdapter;

    private IMManager imManager;// 聊天管理器

    private int unreadcount = 0;// 当前聊天未读消息数量
    private TextView mImUread;// 未读tv

    private String shot_path;// 拍照的路径

    private String mChatMyId;
    private MessagePop pop;
    // 语音状态
    public VoiceStatus voiceStatus;
    private String voicePath = "";

    private DiscussionInfo discussionInfo;// 讨论组消息

    private RelativeLayout messge_bg;

    private String mNickName;

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private boolean mNearFace;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    // 接收消息回调处理
                    long startTime = System.currentTimeMillis();
                    List<ChatMessage> newMsgList = (List<ChatMessage>) msg.obj;
                    // 判断输入法状态
                    if ((mEmjoListLy.getVisibility() == View.VISIBLE || mMsgListView
                            .getLastVisiblePosition() == mMsgListView
                            .getCount() - 1)) {
                        mMsgListView
                                .setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    } else {
                        mMsgListView
                                .setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
                    }
                    synchronized (currentMsgList) {
                        if (newMsgList.size() > 20) {
                            currentMsgList.clear();
                        }
                        currentMsgList.addAll(newMsgList);
                        chatMessageHomeAdapter.notifyDataSetChanged();
                    }
                    // Log.i(TAG, "来新消息共花费 = "+(System.currentTimeMillis() - startTime)+"毫秒");
                    mHandler.sendEmptyMessage(10);
                    break;

                // 网路中断
                case 5:
                    // 网络异常界面显示
                    if ((Boolean) msg.obj) {
                        mUserName.setText(conversationinfo.getConversationTitle());
                    } else {
                        String netNoc = conversationinfo.getConversationTitle() + " (未连接)";
                        mUserName.setText(netNoc);
                    }
                    break;
                case 6:
                    // 发送消息时增加消息到消息列表
                    synchronized (currentMsgList) {
                        currentMsgList.add((ChatMessage) msg.obj);
                        chatMessageHomeAdapter.notifyDataSetChanged();
                        mHandler.sendEmptyMessage(10);
                    }
                    mEditTextContent.setText("");
                    break;

                case 7:
                    chatMessageHomeAdapter.notifyDataSetChanged();
                    if (unreadcount > 99) {
                        mImUread.setText("消息 (99+)");
                    } else if (unreadcount > 0) {
                        mImUread.setText("消息 (" + unreadcount + ")");
                    } else {
                        mImUread.setText("");
                    }
                    break;
                // case 9:
                // // 关闭定时器
                // stopTimer();
                // // 复原录音按钮
                // mRecordButton.setBackgroundResource(R.drawable.im_button_recordnormal);
                // mRecordButton.setText("按住 说话");
                // break;
                case 10:
                    conversationinfo.clearMessagesUnreadStatus();
                    unreadcount = imManager.getUnreadCountAll();
                    if (unreadcount > 99) {
                        mImUread.setText("消息 (99+)");
                    } else if (unreadcount > 0) {
                        mImUread.setText("消息 (" + unreadcount + ")");
                    } else {
                        mImUread.setText("");
                    }
                    chatMessageHomeAdapter.notifyDataSetChanged();
                    break;

                case 12:
                    // 退出
                    Intent intent = new Intent(ChatMessageWithPerson.this,
                            ChatMessageHomeActivity.class);
                    startActivity(intent);
                    ChatMessageWithPerson.this.finish();
                    break;
                case 15:
                    // 更新错误UI
                    CustomLog.d("handler update error msgs ");
                    mMsgListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
                    chatMessageHomeAdapter.notifyDataSetChanged();
                    break;

                case 18:
                    // 发送消息
                    CustomLog.e("发送延迟图片消息");
                    ChatMessage message = (ChatMessage) msg.obj;
                    if (message != null && imManager.sendmessage(message)) {
                        Message addMessage = mHandler.obtainMessage();
                        addMessage.obj = message;
                        addMessage.what = 6;
                        sendMessage(addMessage);
                    }
                    break;
                // 消息不合法
                case 19:
                    CustomToast.showShortToast(mContext, "消息发送失败~");
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.im_person_chat);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);// TYPE_PROXIMITY是距离传感器类型，当然你还可以换成其他的，比如光线传感器
        intent = getIntent();
        mContext = this;
        inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        initWeight();
    }

    public void initWeight() {
        mUserName = (TextView) findViewById(R.id.txt_comm_head_activityName);
        mEditTextContent = (BsPasteEditTextIM) findViewById(R.id.et_sendmessage);
        mAddButton = (Button) findViewById(R.id.btn_more);
        mSendButton = (Button) findViewById(R.id.btn_send);
        more = (LinearLayout) findViewById(R.id.more);
        mImUread = (TextView) findViewById(R.id.tv_number);
        buttonSetModeKeyboard = findViewById(R.id.btn_set_mode_keyboard);
        iv_emoticons_normal = (ImageView) findViewById(R.id.iv_emoticons_normal);
        btnContainer = (LinearLayout) findViewById(R.id.ll_btn_container);
        mRecordButton = (AudioRecordButton) findViewById(R.id.recordButton);
        mVoiceButton = (Button) findViewById(R.id.btn_set_mode_voice);
        mEmjoListLy = (FrameLayout) findViewById(R.id.ll_face_container);
        mUserInfo = (ImageView) findViewById(R.id.txt_comm_head_right);
        mMsgListView = (ChatListView) findViewById(R.id.list);
        messge_bg = (RelativeLayout) findViewById(R.id.messge_bg);
        initData();
        bindWeight();
        isSystemChat();
    }

    // 判断是不是北企星服务号
    public void isSystemChat() {
        // 说明是北企星服务号
        if (conversationinfo.getCategoryId() == CategoryId.BROADCAST) {
            findViewById(R.id.rl_bottom).setVisibility(View.INVISIBLE);
        }

    }

    public void initData() {
        imManager = IMManager.getInstance(mContext);
        imManager.setSendMsgListener(this);
        mChatMyId = BSApplication.getInstance().getIMjavaBean().getClient().get(0).getUserId();
        voiceStatus = new VoiceStatus();
        if ((ConversationInfo) intent
                .getSerializableExtra("conversation") != null) {
            conversationinfo = (ConversationInfo) intent
                    .getSerializableExtra("conversation");
            mUserName.setText(conversationinfo.getConversationTitle());

        }

        // 判断是讨论组还是单聊
        // 判断是讨论组还是单聊
        discussionInfo = imManager.getDiscussionInfo(conversationinfo.getTargetId());
        if (null != discussionInfo) {
            mUserInfo.setVisibility(View.VISIBLE);
        }
        else {
            mUserInfo.setVisibility(View.GONE);
        }

        mNickName = BSApplication.getInstance().getUserFromServerVO().getFullname();
        mySelfHead = BSApplication.getInstance().getUserFromServerVO().getHeadpic();
        mySelfId = BSApplication.getInstance().getUserFromServerVO().getUserid();
        mMsgListView.setDivider(null);
        mMsgListView.setTopRefresh(true);
        chatMessageHomeAdapter = new ChatMessageAdapter(mContext);
        mMsgListView.setAdapter(chatMessageHomeAdapter);
        mMsgListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        chatMessageHomeAdapter.queryMsgHistory(this, conversationinfo);
        mMsgListView.setSelection(mMsgListView.getCount() - 1);
        unreadcount = IMManager.getInstance(this).getUnreadCountAll();
        if (unreadcount > 99) {
            mImUread.setText("消息(99+)");
        } else if (unreadcount > 0) {
            mImUread.setText("消息(" + unreadcount + ")");
        } else {
            mImUread.setText("");
        }
        IntentFilter ift = new IntentFilter();
        ift.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(br, ift);
    }

    public void initBgcks() {
        ConversationBg conversationBg = ConversationBgDbManager.getInstance().getById(conversationinfo.getTargetId());
        if (conversationBg == null) {
            messge_bg.setBackgroundColor(Color.parseColor("#ebebeb"));
            return;
        }
        String path = conversationBg.getBgPath();
        Bitmap bitmap = CommonImageUtils.loadImageBitmap(path, CommonUtils.getScreenWidth(mContext),
                CommonUtils.getScreenHigh(mContext));
        if (null != bitmap) {
            Drawable drawable = new BitmapDrawable(bitmap);
            messge_bg.setBackgroundDrawable(drawable);
        } else {
            messge_bg.setBackgroundColor(Color.parseColor("#ebebeb"));
        }
    }

    public void bindWeight() {
        // 监听文字框
        mEditTextContent.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                if (!TextUtils.isEmpty(s)) {
                    mAddButton.setVisibility(View.GONE);
                    mSendButton.setVisibility(View.VISIBLE);
                } else {
                    mAddButton.setVisibility(View.VISIBLE);
                    mSendButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mRecordButton.setAudioFinishRecorderListener(this);
        mRecordButton.setAudioRecorderDownListener(this);
        iv_emoticons_normal.setOnClickListener(this);
        mEditTextContent.setOnClickListener(this);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mUserInfo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
            }
        });

        mMsgListView.setonRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                new AsyncTask<Void, Void, Void>() {
                    protected Void doInBackground(Void... params) {
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    protected void onPostExecute(Void result) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                List<ChatMessage> msgs = conversationinfo
                                        .getLastestMessages(
                                                currentMsgList.size(), 20);
                                CustomLog.d("new refresh msg size = " + msgs.size());
                                if (msgs != null && msgs.size() > 0) {
                                    synchronized (currentMsgList) {
                                        currentMsgList.addAll(0, msgs);
                                        mMsgListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
                                        chatMessageHomeAdapter.notifyDataSetChanged();
                                        mMsgListView.setSelection(msgs.size());
                                    }
                                    if (voiceStatus != null && voiceStatus.isStatus()) {
                                        voiceStatus.put(voiceStatus.getPos() + msgs.size(), voiceStatus.isStatus());
                                    }
                                }
                                mHandler.sendEmptyMessage(10);
                                mMsgListView.onRefreshComplete();
                            }
                        });
                    }
                }.execute(null, null, null);
            }
        });

        mMsgListView.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    hideSystemKeyBoard();// 隐藏键盘
                    mEmjoListLy.setVisibility(View.GONE);
                }
                return false;
            }
        });

        // 滑动时关闭弹出界面
        mMsgListView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                    int visibleItemCount, int totalItemCount) {

            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    hideSystemKeyBoard();// 隐藏键盘
                    mEmjoListLy.setVisibility(View.GONE);
                }
                if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
                    // 判断滚动到底部
                    // if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                    //
                    // }
                    // 判断滚动到顶部
                    if (view.getFirstVisiblePosition() == 0) {
                        mMsgListView.onIsRefresh(true);
                    }
                }
            }

        });
        mUserInfo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onGetDuciss();
            }
        });

    }

    /**
     * 发送消息
     * 
     * @param view
     */
    public void sendMsgInfo(View view) {
        mMsgListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);// listview设置可以滚动
        final String text = ConcatInfoUtils.convertToMsg(mEditTextContent.getText(), mContext);
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                ChatMessage msg = null;
                /** 设置消息类型为单聊 */
                switch (conversationinfo.getCategoryId()) {
                    case PERSONAL:
                        msg = new SingleChat();
                        break;
                    case GROUP:
                        msg = new GroupChat();
                        break;
                    case DISCUSSION:
                        msg = new DiscussionChat();
                        break;
                }
                msg.setTargetId(conversationinfo.getTargetId());// 会话消息列表跳过来 接收方的userid
                msg.setNickName(mNickName);
                msg.setSenderId(mChatMyId);// 自己的id
                msg.setContent(text);
                msg.setMsgType(MSGTYPE.MSG_DATA_TEXT);
                msg.setFromMyself(true);
                if (imManager.sendmessage(msg)) {// 发送消息成功则更新会话
                    Message message = new Message();
                    message.obj = msg;
                    message.what = 6;
                    mHandler.sendMessage(message);
                }
                else {
                    mHandler.sendEmptyMessage(19);
                }
            }
        }).start();

    }

    /**
     * 显示语音图标按钮
     * 
     * @param view
     */
    public void setModeVoice(View view) {
        WindowUtils.hideKeyboard(((Activity) mContext));
        findViewById(R.id.edittext_layout).setVisibility(View.GONE);
        mRecordButton.setVisibility(View.VISIBLE);
        mVoiceButton.setVisibility(View.GONE);// 语音图标隐藏
        mEmjoListLy.setVisibility(View.GONE);// 表情或者更多界面隐藏
        buttonSetModeKeyboard.setVisibility(view.VISIBLE);// 键盘图片显示
        mAddButton.setVisibility(view.VISIBLE);// 更多图片显示
        mSendButton.setVisibility(View.GONE);// 发送按钮隐藏

    }

    /**
     * 显示键盘图标
     * 
     * @param view
     */
    public void setModeKeyboard(View view) {
        mEmjoListLy.setVisibility(View.GONE);
        findViewById(R.id.edittext_layout).setVisibility(View.VISIBLE);
        mRecordButton.setVisibility(View.GONE);
        mVoiceButton.setVisibility(View.VISIBLE);// 语音图标隐藏
        buttonSetModeKeyboard.setVisibility(View.GONE);// 键盘图片显示
        mEditTextContent.requestFocus();
        if (TextUtils.isEmpty(mEditTextContent.getText())) {
            mAddButton.setVisibility(View.VISIBLE);
            mSendButton.setVisibility(View.GONE);
        } else {
            mSendButton.setVisibility(View.GONE);
            mAddButton.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 显示更多界面
     * 
     * @param view
     */

    public void setModeMoreInfo(View view) {
        hideSystemKeyBoard();
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                EmjoAndMoreInfoSelect(MORESELECT);
            }
        }, 200);

    }

    /**
     * 添加表情
     **/

    @Override
    public void onEmojiconBackspaceClicked(View arg0) {
        // TODO Auto-generated method stub
        EmojiconsFragment.backspace(mEditTextContent);
    }

    @Override
    public void onEmojiconClicked(Emojicon arg0) {
        // TODO Auto-generated method stub
        EmojiconsFragment.input(mEditTextContent, arg0);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.iv_emoticons_normal:
                hideSystemKeyBoard();
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        EmjoAndMoreInfoSelect(EMJOSELECT);
                    }
                }, 200);

                break;
            case R.id.et_sendmessage:
                mEmjoListLy.setVisibility(View.GONE);// 表情或者更多页面隐藏
                break;

        }
    }

    public void EmjoAndMoreInfoSelect(int index) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.setTransition(transaction.TRANSIT_FRAGMENT_OPEN);
        hideFragment(transaction);
        switch (index) {
            case EMJOSELECT:
                if (emojiconsFragment == null) {
                    emojiconsFragment = new EmojiconsFragment();
                    transaction.add(R.id.ll_face_container, emojiconsFragment);
                } else {
                    transaction.show(emojiconsFragment);
                }
                break;
            case MORESELECT:
                if (chatMessageMoreFragment == null) {
                    chatMessageMoreFragment = ChatMessageMoreFragment.newInstance();
                    transaction.add(R.id.ll_face_container, chatMessageMoreFragment);
                } else {
                    transaction.show(chatMessageMoreFragment);
                }
                break;
        }
        // 有时这行会报错
        try {
            transaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mEmjoListLy.setVisibility(View.VISIBLE);
    }

    private void hideFragment(FragmentTransaction transaction)
    {
        if (emojiconsFragment != null) {
            transaction.hide(emojiconsFragment);
        }
        if (chatMessageMoreFragment != null) {
            transaction.hide(chatMessageMoreFragment);
        }
    }

    public void hideSystemKeyBoard() {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditTextContent.getWindowToken(), 0);
    }

    @Override
    public void onFinishedPlayingVoice() {
        // TODO Auto-generated method stub
        mMsgListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
        voiceStatus.clear();
        mHandler.sendEmptyMessage(7);
    }

    // 当按下语音按钮
    @Override
    public void onAudioDown() {
        // TODO Auto-generated method stub
        UIStopVoice();
    }

    public void UIStopVoice() {
        // 如果正在播放声音，停止播放声音
        if (voiceStatus.getPos() >= 0 && voiceStatus.isStatus()) {
            imManager.stopPlayerVoice();
            animation.selectDrawable(0);
            animation.stop();
            voiceStatus.clear();
        }

        imManager.stopPlayerVoice();
        mHandler.sendEmptyMessage(7);
    }

    @Override
    public void onFinishedRecordingVoice(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDownloadAttachedProgress(String arg0, String arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onReceiveMessage(List msglist) {
        // TODO Auto-generated method stub
        // Log.i(TAG,
        // "onReceiveMessage msg size = "+msglist.size()+",threadid == "+Thread.currentThread().getName());

        List<ChatMessage> messages = (List<ChatMessage>) msglist;
        List<ChatMessage> newMsgList = new ArrayList<ChatMessage>();
        int otherConvertMsgCount = 0;

        for (int i = messages.size() - 1; i >= 0; i--) {
            ChatMessage message = messages.get(i);
            if ((message.getParentID()).equals(conversationinfo
                    .getTargetId())) {
                // 当前会话
                newMsgList.add(0, message);
                CustomLog.d("onReceiveMessage content:"
                        + message.getContent());
                if (newMsgList.size() > 20) {
                    // 当前会话消息超过20条
                    CustomLog.e("接受到当前会话消息太多，只添加20条");
                    break;
                }
            } else {
                // 别的会话
                otherConvertMsgCount++;
                if (otherConvertMsgCount > 20) {
                    // 其他会话消息超过20条
                    CustomLog.e("接受到其他会话消息太多，break 防止Handle 卡死");
                    break;
                }
            }
            if (message.getMsgType() == MSGTYPE.MSG_DATA_TEXT) {
                CustomLog.d("收到文字消息:" + message.getContent());
            }
            if (message.getMsgType() == MSGTYPE.MSG_DATA_IMAGE) {
                CustomLog.d("收到图片消息:" + message.getPath());
            }
        }
        Message message = new Message();
        message.obj = newMsgList;
        message.what = 1;

        mHandler.sendMessage(message);
    }

    @Override
    public void onSendMsgRespone(ChatMessage arg0) {
        // TODO Auto-generated method stub
        mMsgListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        mHandler.sendEmptyMessage(7);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        String path = "";
        switch (requestCode) {
        // 发送图片消息
            case ImageActivityUtils.REQUEST_IMAGE:
                if (resultCode == RESULT_OK) {
                    ArrayList<String> mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    try {
                        // path =
                        // BitmapTools.getNewPath(mSelectPath.get(0),
                        // 0, CommonUtils.getScreenWid((Activity) mContext),
                        // CommonUtils.getScreenWid((Activity) mContext),
                        // conversationinfo.getTargetId());
                        path = mSelectPath.get(0);
                        if (TextUtils.isEmpty(path)) {
                            CustomToast.showShortToast(mContext, "图片选择错误");
                            return;
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    /** 设置消息类型为单聊 */
                    ChatMessage msg = null;
                    switch (conversationinfo.getCategoryId()) {
                        case PERSONAL:
                            msg = new SingleChat();
                            break;
                        case GROUP:
                            msg = new GroupChat();
                            break;
                        case DISCUSSION:
                            msg = new DiscussionChat();
                            break;
                    }
                    msg.setTargetId(conversationinfo.getTargetId());
                    msg.setNickName(mNickName);
                    msg.setMsgType(MSGTYPE.MSG_DATA_IMAGE);
                    msg.setContent(path);
                    msg.setSenderId(mChatMyId);// 自己的id
                    msg.setPath(path);
                    if (imManager.sendmessage(msg)) {// 发送消息成功则更新会话
                        Message message = new Message();
                        message.obj = msg;
                        message.what = 6;
                        mHandler.sendMessage(message);
                    }
                    else {
                        mHandler.sendEmptyMessage(19);
                    }
                }
                break;
            case 101:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        // 防止获取到的图片始终是横屏
                        // Uri uri = data.getData();
                        shot_path = chatMessageMoreFragment.getShot_path();
                        if (TextUtils.isEmpty(shot_path)) {
                            CustomLog.e("------拍摄异常-------");
                            return;
                        }
                        int degree = BitmapTools.readImageDegree(shot_path);// /storage/emulated/0/com.beiseng/files/1468978440421.jpg
                        shot_path = BitmapTools.getNewPath(shot_path, degree,
                                CommonUtils.getScreenHigh(mContext), CommonUtils.getScreenWidth(mContext),
                                conversationinfo.getTargetId());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // /sdcard/yunzhixun/image/1/yzx_image_1468922373023.jpg
                    CustomLog.i("RESULT_PATH:" + shot_path);
                    String sendPath = shot_path;
                    File file = new File(sendPath);
                    CustomLog.i("RESULT_PATH_EXISTS:" + file.exists());
                    if (file.exists()) {
                        ChatMessage msg = null;
                        switch (conversationinfo.getCategoryId()) {
                            case PERSONAL:
                                msg = new SingleChat();
                                break;
                            case GROUP:
                                msg = new GroupChat();
                                break;
                            case DISCUSSION:
                                msg = new DiscussionChat();
                                break;
                        }
                        if (null != msg) {
                            msg.setTargetId(conversationinfo.getTargetId());
                            msg.setNickName(mNickName);
                            msg.setMsgType(MSGTYPE.MSG_DATA_IMAGE);
                            msg.setContent(sendPath);
                            msg.setPath(sendPath);
                            msg.setSenderId(mChatMyId);
                            // msg.setNickName(loginUser);
                            CustomLog.d("getCategoryId:"
                                    + conversationinfo.getCategoryId());
                            if (UCSManager.isConnect() && imManager.sendmessage(msg)) {
                                Message message = mHandler.obtainMessage();
                                message.obj = msg;
                                message.what = 6;
                                mHandler.sendMessage(message);
                            } else {
                                if (!UCSManager.isConnect()) {
                                    CustomLog.e("---------拍照后，页面被回收，延迟发送------");
                                    Message message = mHandler.obtainMessage();
                                    message.obj = msg;
                                    message.what = 18;
                                    mHandler.sendMessageDelayed(message, 800);
                                }
                            }
                        }
                    }
                    // 隐藏更多操作栏
                    // 发送完成。将图片路径设为""
                    shot_path = "";
                }
                break;

            // mapLocation地图消息
            case 102:
                if (resultCode == Activity.RESULT_OK) {
                    LocationMapMsg locationMsg = new LocationMapMsg(Double.parseDouble(data.getStringExtra("Lat")), Double.parseDouble(data.getStringExtra("Lng")), data.getStringExtra("adress"), data.getStringExtra("path"));
                    ChatMessage msg = null;
                    switch (conversationinfo.getCategoryId()) {
                        case PERSONAL:
                            msg = new SingleChat();
                            break;
                        case GROUP:
                            msg = new GroupChat();
                            break;
                        case DISCUSSION:
                            msg = new DiscussionChat();
                            break;
                    }
                    if (null != msg) {
                        msg.setTargetId(conversationinfo.getTargetId()).
                                setNickName(mNickName)
                                .setSenderId(mChatMyId)
                                .setMsgType(MSGTYPE.MSG_DATA_LOCALMAP)
                                .setLocationMapMsg(locationMsg)
                                .setContent(locationMsg.getThumbnailPath())
                                .setFromMyself(true);
                        if (imManager.sendmessage(msg)) {
                            Message message = new Message();
                            message.obj = msg;
                            message.what = 6;
                            mHandler.sendMessage(message);
                        }
                        else {
                            mHandler.sendEmptyMessage(19);
                        }

                    }
                }
                break;

            case 104:
                if (resultCode == RESULT_FIRST_USER) {
                    this.finish();
                }
                else if (resultCode == 2) {
                    if (data != null) {
                        try {
                            Bitmap bitmap = BitmapFactory.decodeFile(data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT).get(0));
                            if (null != bitmap) {
                                Drawable drawable = new BitmapDrawable(bitmap);
                                messge_bg.setBackgroundDrawable(drawable);
                                insertBkg(data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT).get(0));
                            } else {
                                // 拍摄或者选择图片出错
                                CustomToast.showShortToast(mContext, "更换背景失败");
                                return;
                            }
                        } catch (Exception e) {
                            CustomToast.showShortToast(mContext, "更换背景失败");
                            e.printStackTrace();
                        }

                    }

                }
                else if (resultCode == 3) {
                    currentMsgList.clear();
                    chatMessageHomeAdapter.notifyDataSetChanged();
                }
                break;

        }
    }

    private void insertBkg(String path) {
        // 保存背景记录到数据库
        ConversationBg bg = new ConversationBg();
        bg.setAccount(mChatMyId);
        bg.setTargetId(conversationinfo.getTargetId());
        bg.setBgPath(path);
        ConversationBgDbManager.getInstance().insert(bg);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("conversation", conversationinfo);
        if (!TextUtils.isEmpty(shot_path)) {
            outState.putString("shot_path", shot_path);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null
                && savedInstanceState.getSerializable("conversation") != null && conversationinfo == null) {
            conversationinfo = (ConversationInfo) savedInstanceState
                    .getSerializable("conversation");
        }
        if (savedInstanceState != null && !TextUtils.isEmpty(savedInstanceState.getString("shot_path"))) {
            shot_path = savedInstanceState.getString("shot_path");
        }
    }

    // 网络中断广播
    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (TextUtils.equals(intent.getAction(),
                    ConnectivityManager.CONNECTIVITY_ACTION)) {
                CustomLog
                        .e("网络连接判断"
                                + NetWorkTools
                                        .isNetWorkConnect(ChatMessageWithPerson.this));
                Message msg = new Message();
                msg.what = 5;
                msg.obj = NetWorkTools.isNetWorkConnect(ChatMessageWithPerson.this);
                mHandler.sendMessage(msg);
            }
        }
    };

    @Override
    protected void onDestroy() {
        unregisterReceiver(br);
        imManager.removeSendMsgListener(this);
        if (voiceStatus != null)
        {
            voiceStatus.clear();
            voiceStatus = null;
        }

        super.onDestroy();
    }

    @Override
    protected void onResume() {
        NotificationTools.clearUnreadNum();
        if (conversationinfo.getCategoryId() == CategoryId.DISCUSSION) {
            DiscussionInfo info = imManager.getDiscussionInfo(conversationinfo
                    .getTargetId());
            if (null != info && TextUtils.isEmpty(mUserName.getText().toString())) {
                mUserName.setText(imManager.getDiscussionInfo(
                        conversationinfo.getTargetId()).getDiscussionName());
            }
        }

        else if (!BSApplication.getInstance().isAppliactionChat()) {

            Message message = new Message();
            message.obj = false;
            message.what = 5;
            mHandler.sendMessage(message);
        }
        initBgcks();
        super.onResume();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            imManager.stopPlayerVoice();
            conversationinfo.clearMessagesUnreadStatus();
            mHandler.sendEmptyMessage(12);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void goBack(View view) {
        imManager.stopPlayerVoice();
        conversationinfo.clearMessagesUnreadStatus();
        mHandler.sendEmptyMessage(12);
    }

    // 发送语音消息
    @Override
    public void onFinish(float seconds, String filePath) {

        ChatMessage msg = null;
        switch (conversationinfo.getCategoryId()) {
            case PERSONAL:
                msg = new SingleChat();
                break;
            case GROUP:
                msg = new GroupChat();
                break;
            case DISCUSSION:
                msg = new DiscussionChat();
                break;
        }
        if (null != msg) {
            msg.setTargetId(conversationinfo.getTargetId());
            msg.setNickName(mNickName);
            msg.setMsgType(MSGTYPE.MSG_DATA_VOICE);
            msg.setPath(filePath);
            msg.setContent(String.valueOf((int) seconds));
            msg.setFromMyself(true);
            msg.setSenderId(mChatMyId);
            CustomLog.d("getCategoryId:"
                    + conversationinfo.getCategoryId());
            if (imManager.sendmessage(msg)) {
                Message message = new Message();
                message.obj = msg;
                message.what = 6;
                mHandler.sendMessage(message);
            }
            else {
                mHandler.sendEmptyMessage(19);
            }
        }
    }

    @Override
    protected void onPause() {
        // 暂停
        if (voiceStatus != null && voiceStatus.getPos() >= 0) {
            imManager.stopPlayerVoice();
            mMsgListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
            voiceStatus.clear();
            animation = null;
            mHandler.sendEmptyMessage(7);
        }
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
        }
        super.onPause();
    }

    /**
     * BS北盛最帅程序员 Copyright (c) 2016 湖北北盛科技有限公司
     * 
     * @author 梁骚侠
     * @date 2016-4-22
     * @version 2.0
     */
    public class ChatMessageAdapter extends BaseAdapter {
        private HolderView hv = null;
        private final static int LEFTVIEW = 1;// 他人发消息
        private final static int RIGHTVIEW = 0;// 我发消息

        public void queryMsgHistory(Context context,
                ConversationInfo conversationinfo) {
            mContext = context;
            if (null != conversationinfo) {
                if (null != conversationinfo.getConversationTitle()) {
                    currentMsgList = conversationinfo.getLastestMessages(0, 20);
                    conversationinfo.clearMessagesUnreadStatus();
                    initMsgTimeList();
                }
            }

            super.notifyDataSetChanged();
        }

        /**
         * 
         */
        public ChatMessageAdapter(Context context) {
            // TODO Auto-generated constructor stub
            mContext = context;
            mImageLoader = ImageLoader.getInstance();
            mOptions = CommonUtils.initImageLoaderOptions();
        }

        @Override
        public int getCount() {
            return currentMsgList.size();
        }

        @Override
        public Object getItem(int position) {
            return currentMsgList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final ChatMessage message = currentMsgList.get(position);

            HolderViewDirection hvDirection;
            if (position == 0) {
                mMsgListView.onIsRefresh(true);

            } else {
                mMsgListView.onIsRefresh(false);
            }
            final int type = getItemViewType(position);
            if (convertView == null) {
                hvDirection = new HolderViewDirection();
                if (type == RIGHTVIEW) {
                    convertView = LayoutInflater.from(mContext).inflate(
                            R.layout.im_chatmysendmsg_adapter, null);
                    hv = setHolderViewMy(hvDirection.rightHolderView, convertView);
                } else {
                    convertView = LayoutInflater.from(mContext).inflate(
                            R.layout.im_chatyousendmsg_adapter, null);
                    hv = setHolderViewYou(hvDirection.leftHolderView, convertView);
                }
                convertView.setTag(hv);
            } else {
                hv = (HolderView) convertView.getTag();
            }

            if (message.getIsFromMyself()) {
                switch (message.getMsgType()) {
                // 文本消息
                    case MSG_DATA_TEXT:
                        hv.mUserHead.setVisibility(View.VISIBLE);
                        hv.mContentTv.setVisibility(View.VISIBLE);
                        hv.mVoiceImg.setVisibility(View.GONE);
                        hv.mVoiceState.setVisibility(View.GONE);
                        hv.mVoiceAnim.setVisibility(View.GONE);
                        hv.mImgView.setVisibility(View.GONE);
                        hv.mContentTv.setText(message.getContent());
                        hv.mContentTv.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                pop = new MessagePop(mContext, ChatMessageWithPerson.this, conversationinfo, message, 2);
                                pop.setText(((TextView) view).getText().toString());
                                int[] location = new int[2];
                                view.getLocationOnScreen(location);
                                pop.showAtLocation(view, Gravity.NO_GRAVITY, location[0] + view.getWidth() / 2 - pop.getWidth() / 2, location[1] - pop.getHeight());
                                return false;
                            }
                        });
                        break;
                    // 图片
                    case MSG_DATA_IMAGE:
                        hv.mUserHead.setVisibility(View.VISIBLE);
                        hv.mVoiceImg.setVisibility(View.GONE);
                        hv.mVoiceState.setVisibility(View.GONE);
                        hv.mVoiceAnim.setVisibility(View.GONE);
                        hv.mContentTv.setVisibility(View.GONE);
                        hv.mImgView.setVisibility(View.VISIBLE);
                        hv.mImgView.setLocation(false);
                        BitmapTools.loadChatImageBitmap(mContext,
                                message.getPath(), hv.mImgView, CommonUtils.getScreenWid((Activity) mContext),
                                CommonUtils.getScreenHigh((Activity) mContext), message.getIsFromMyself());

                        hv.mImgView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                pop = new MessagePop(ChatMessageWithPerson.this, ChatMessageWithPerson.this, conversationinfo, message, 1);
                                int[] location = new int[2];
                                view.getLocationOnScreen(location);
                                pop.showAtLocation(view, Gravity.NO_GRAVITY, location[0] + view.getWidth() / 2 - pop.getWidth() / 2, location[1] - pop.getHeight());
                                return false;
                            }
                        });
                        hv.mImgView.setOnClickListener(new View.OnClickListener() {
                            @SuppressLint("NewApi")
                            @Override
                            public void onClick(View arg0) {
                                Intent intent = new Intent(mContext,
                                        ImagePreviewActivity.class);
                                ArrayList<String> list = new ArrayList<String>();
                                if (message.getIsFromMyself()) {
                                    list.add(message.getContent());
                                    intent.putExtra("isScard", "true");
                                } else {
                                    // intent.putExtra("imagedownpath", message.getPath());
                                    list.add(message.getPath());
                                }
                                intent.putExtra("isChat", "true");
                                intent.putStringArrayListExtra("piclist", list);
                                ActivityOptions activityOptions = ActivityOptions.makeScaleUpAnimation(hv.mImgView, hv.mImgView.getWidth() / 2, hv.mImgView.getHeight() / 2, 0, 0);
                                ActivityCompat.startActivity((Activity) mContext, intent, activityOptions.toBundle());
                            }
                        });

                        break;

                    // 语音消息
                    case MSG_DATA_VOICE:
                        hv.mUserHead.setVisibility(View.VISIBLE);
                        hv.mContentTv.setVisibility(View.GONE);
                        hv.mImgView.setVisibility(View.GONE);
                        hv.mVoiceImg.setVisibility(View.VISIBLE);
                        hv.mVoiceState.setVisibility(View.VISIBLE);
                        hv.mVoiceAnim.setVisibility(View.VISIBLE);
                        final AnimationDrawable anima = (AnimationDrawable) hv.mVoiceAnim
                                .getBackground();
                        if (voiceStatus.getPos() >= 0
                                && voiceStatus.getPos() == position
                                && voiceStatus.isStatus()) {
                            anima.selectDrawable(0);
                            anima.start();
                            animation = anima;
                        } else {
                            anima.selectDrawable(0);
                            anima.stop();
                        }
                        hv.mVoiceState.setText(message.getContent() + "''");// 语音消息时间
                        AudioManagers.byVoiceLenthView(Integer.parseInt(message.getContent()), hv.mVoiceImg, mContext);
                        /*
                         * 下周一来了 继续语音消息，先继续我发送的处理 然后在弄别的，那个mMsgListView滚动处理还没弄完。voiceStatus还没弄完。
                         */
                        hv.mVoiceImg.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                pop = new MessagePop(ChatMessageWithPerson.this, ChatMessageWithPerson.this, conversationinfo, message, 1);
                                int[] location = new int[2];
                                view.getLocationOnScreen(location);
                                pop.showAtLocation(view, Gravity.NO_GRAVITY, location[0] + view.getWidth() / 2 - pop.getWidth() / 2, location[1] - pop.getHeight());
                                return false;
                            }
                        });
                        hv.mVoiceImg.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                message.setVoiceReadStatus(ChatMessage.MSG_STATUS_READED);

                                if (voiceStatus.getPos() >= 0
                                        && voiceStatus.getPos() == position
                                        && voiceStatus.isStatus()) {
                                    mMsgListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
                                    imManager.stopPlayerVoice();
                                    animation.selectDrawable(0);
                                    animation.stop();
                                    voiceStatus.put(voiceStatus.getPos(), false);
                                } else {
                                    if (voiceStatus.getPos() > 0) {
                                        imManager.stopPlayerVoice();
                                        if (animation != null) {
                                            animation.selectDrawable(0);
                                            animation.stop();
                                        }
                                    }
                                    animation = anima;

                                    imManager.startPlayerVoice(message.getPath(),
                                            (ChatMessageWithPerson) mContext);
                                    animation.start();
                                    voicePath = message.getPath();
                                    voiceStatus.put(position, true);
                                }
                            }
                        });

                        break;

                    // 位置消息
                    case MSG_DATA_LOCALMAP:
                        hv.mUserHead.setVisibility(View.VISIBLE);
                        hv.mVoiceImg.setVisibility(View.GONE);
                        hv.mVoiceState.setVisibility(View.GONE);
                        hv.mVoiceAnim.setVisibility(View.GONE);
                        hv.mContentTv.setVisibility(View.GONE);
                        hv.mImgView.setVisibility(View.VISIBLE);
                        hv.mImgView.setLocation(true);
                        hv.mImgView.setLocationText(message.getLocationMapMsg().getDetailAddr());
                        BitmapTools.loadChatLocationBitmap(ChatMessageWithPerson.this, message.getLocationMapMsg().getThumbnailPath(), hv.mImgView, CommonUtils.getScreenWid((Activity) mContext),
                                CommonUtils.getScreenHigh((Activity) mContext));
                        hv.mImgView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                pop = new MessagePop(ChatMessageWithPerson.this, ChatMessageWithPerson.this, conversationinfo, message, 1);
                                int[] location = new int[2];
                                view.getLocationOnScreen(location);
                                pop.showAtLocation(view, Gravity.NO_GRAVITY, location[0] + view.getWidth() / 2 - pop.getWidth() / 2, location[1] - pop.getHeight());
                                return false;
                            }
                        });
                        hv.mImgView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mEmjoListLy.setVisibility(View.GONE);// 表情或者更多界面隐藏
                                hideSystemKeyBoard();
                                Intent intent = new Intent(ChatMessageWithPerson.this, CrmGaoDeMapWithShowActivity.class);
                                intent.putExtra("mLat", message.getLocationMapMsg().getLatitude() + "");
                                intent.putExtra("mLon", message.getLocationMapMsg().getLongitude() + "");
                                intent.putExtra("mAddress", message.getLocationMapMsg().getDetailAddr());
                                intent.putExtra("cName", "");
                                startActivity(intent);

                            }
                        });
                        break;
                // // 系统消息
                // case MSG_DATA_SYSTEM:
                // hv.mContentTv.setVisibility(View.GONE);
                // hv.mVoiceImg.setVisibility(View.GONE);
                // hv.mVoiceState.setVisibility(View.GONE);
                // hv.mVoiceAnim.setVisibility(View.GONE);
                // hv.mImgView.setVisibility(View.GONE);
                // hv.mUserHead.setVisibility(View.GONE);
                // break;

                }
                switch (message.getSendStatus()) {
                // 网络错误，尝试继续发送

                    case ChatMessage.MSG_STATUS_INPROCESS:
                    case ChatMessage.MSG_STATUS_RETRY:
                        // 发送中
                        hv.mNoSend.setVisibility(View.GONE);
                        hv.pb_sending.setVisibility(View.VISIBLE);
                        break;
                    case ChatMessage.MSG_STATUS_SUCCESS:
                        hv.pb_sending.setVisibility(View.GONE);
                        hv.mNoSend.setVisibility(View.GONE);
                        // 发送成功
                        break;
                    case ChatMessage.MSG_STATUS_FAIL:
                    case ChatMessage.MSG_STATUS_NETERROR:
                    case ChatMessage.MSG_STATUS_TIMEOUT:
                        CustomLog.d("MSG_STATUS_FAIL");
                        hv.pb_sending.setVisibility(View.GONE);
                        hv.mNoSend.setVisibility(View.VISIBLE);
                        // 发送失败
                        break;

                }
                mImageLoader.displayImage(mySelfHead, hv.mUserHead, mOptions);
                hv.mUserHead.setUserId(mySelfId);// 获取头像对应的用户ID
                hv.mUserHead.setUserName(mNickName);
                hv.mUserHead.setUrl(mySelfHead);
                hv.mNoSend.setTag(message);
                hv.mNoSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 用户可以选择两种重发效果。
                        // 1，重发的消息显示在最下面。那么得先删除之前发送失败的消息，再当做新消息来发送
                        // 2，重发的消息显示在原来位置，那么直接设置setSendStatus(ChatMessage.MSG_STATUS_RETRY);，
                        // 下面只演示第一种实现方式
                        ChatMessage message = (ChatMessage) view.getTag();
                        message.deleteMessage();
                        message.setSendStatus(ChatMessage.MSG_STATUS_RETRY);
                        Boolean sendstatus = false;
                        switch (message.getCategoryId()) {
                            case PERSONAL:
                                sendstatus = imManager
                                        .sendmessage((SingleChat) message);
                                break;
                            case GROUP:
                                sendstatus = imManager.sendmessage((GroupChat) message);
                                break;
                            case DISCUSSION:
                                sendstatus = imManager
                                        .sendmessage((DiscussionChat) message);
                                break;
                        }
                        /*
                         * // 如果该条消息是语音消息并且是当前正在播放的语音，停止动画 if (message.getMsgType() ==
                         * MSGTYPE.MSG_DATA_VOICE && voiceStatus != null && voiceStatus.getPos() ==
                         * position) { imManager.stopPlayerVoice(); voiceStatus.clear(); }
                         */
                        if (sendstatus) {
                            synchronized (currentMsgList) {
                                currentMsgList.remove(message);
                                currentMsgList.add(message);
                                notifyDataSetChanged();
                            }
                        }
                    }
                });

            }
            // 如果是收到消息
            else {
                if (ConcatInfoUtils.getInstance().getUserByIM(message.getSenderId()) != null) {
                    DepartmentAndEmployeeVO mDepartmentAndEmployeeVO = ConcatInfoUtils.getInstance().getUserByIM(message.getSenderId());
                    mImageLoader.displayImage(mDepartmentAndEmployeeVO.getHeadpic(), hv.mUserHead, mOptions);
                    hv.mUserHead.setUserId(mDepartmentAndEmployeeVO.getUserid());// 获取头像对应的用户ID
                    hv.mUserHead.setUserName(mDepartmentAndEmployeeVO.getFullname());
                    hv.mUserHead.setUrl(mDepartmentAndEmployeeVO.getHeadpic());
                }
                else {
                    hv.mUserHead.setImageResource(R.drawable.ic_launcher);
                }
                switch (message.getMsgType()) {
                // 文本消息
                    case MSG_DATA_TEXT:
                        hv.mUserHead.setVisibility(View.VISIBLE);
                        hv.mContentTv.setVisibility(View.VISIBLE);
                        hv.mVoiceImg.setVisibility(View.GONE);
                        hv.mVoiceState.setVisibility(View.GONE);
                        hv.mVoiceAnim.setVisibility(View.GONE);
                        hv.mImgView.setVisibility(View.GONE);
                        hv.mSystemTv.setVisibility(View.GONE);
                        hv.mContentTv.setText(message.getContent());
                        hv.mContentTv.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                pop = new MessagePop(mContext, ChatMessageWithPerson.this, conversationinfo, message, 2);
                                pop.setText(((TextView) view).getText().toString());
                                int[] location = new int[2];
                                view.getLocationOnScreen(location);
                                pop.showAtLocation(view, Gravity.NO_GRAVITY, location[0] + view.getWidth() / 2 - pop.getWidth() / 2, location[1] - pop.getHeight());
                                return false;
                            }
                        });
                        break;
                    // 图片
                    case MSG_DATA_IMAGE:
                        hv.mUserHead.setVisibility(View.VISIBLE);
                        hv.mVoiceImg.setVisibility(View.GONE);
                        hv.mVoiceState.setVisibility(View.GONE);
                        hv.mVoiceAnim.setVisibility(View.GONE);
                        hv.mContentTv.setVisibility(View.GONE);
                        hv.mImgView.setVisibility(View.VISIBLE);
                        hv.mSystemTv.setVisibility(View.GONE);
                        BitmapTools.loadChatImageBitmap(mContext,
                                message.getContent(), hv.mImgView, CommonUtils.getScreenWid((Activity) mContext),
                                CommonUtils.getScreenHigh((Activity) mContext), message.getIsFromMyself());
                        hv.mImgView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                pop = new MessagePop(ChatMessageWithPerson.this, ChatMessageWithPerson.this, conversationinfo, message, 1);
                                int[] location = new int[2];
                                view.getLocationOnScreen(location);
                                pop.showAtLocation(view, Gravity.NO_GRAVITY, location[0] + view.getWidth() / 2 - pop.getWidth() / 2, location[1] - pop.getHeight());
                                return false;
                            }
                        });
                        hv.mImgView.setOnClickListener(new View.OnClickListener() {
                            @SuppressLint("NewApi")
                            @Override
                            public void onClick(View arg0) {
                                Intent intent = new Intent(mContext,
                                        ImagePreviewActivity.class);
                                ArrayList<String> list = new ArrayList<String>();
                                if (message.getIsFromMyself()) {
                                    list.add(message.getContent());
                                    intent.putExtra("isScard", "true");
                                } else {
                                    list.add(message.getPath());
                                }
                                intent.putExtra("isChat", "chat");
                                intent.putStringArrayListExtra("piclist", list);
                                ActivityOptions activityOptions = ActivityOptions.makeScaleUpAnimation(hv.mImgView, hv.mImgView.getWidth() / 2, hv.mImgView.getHeight() / 2, 0, 0);
                                ActivityCompat.startActivity((Activity) mContext, intent, activityOptions.toBundle());
                            }
                        });
                        break;

                    // 语音消息
                    case MSG_DATA_VOICE:
                        hv.mUserHead.setVisibility(View.VISIBLE);
                        hv.mContentTv.setVisibility(View.GONE);
                        hv.mImgView.setVisibility(View.GONE);
                        hv.mVoiceImg.setVisibility(View.VISIBLE);
                        hv.mVoiceState.setVisibility(View.VISIBLE);
                        hv.mVoiceAnim.setVisibility(View.VISIBLE);
                        hv.mSystemTv.setVisibility(View.GONE);
                        if (message.getSendStatus() == ChatMessage.MSG_STATUS_INPROCESS) {
                            // 语音正在接收
                            hv.pb_sending.setVisibility(View.VISIBLE);// 小菊花转起来

                        } else if (message.getSendStatus() == ChatMessage.MSG_STATUS_SUCCESS) { // 语音接受成功
                            if (message.getReadStatus() == ChatMessage.MSG_STATUS_UNREAD) {
                                hv.mVoiceState.setCompoundDrawablesWithIntrinsicBounds(null, mContext.getResources().getDrawable(R.drawable.update_hint), null, null);// 消息未读则小红点显示
                            } else {
                                hv.mVoiceState.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);// 消息已读则只显示时间
                            }
                            hv.mVoiceState.setVisibility(View.VISIBLE);
                            hv.mVoiceAnim.setVisibility(View.VISIBLE);
                            hv.mNoSend.setVisibility(View.GONE);
                            hv.pb_sending.setVisibility(View.GONE);
                        } else if (message.getSendStatus() == ChatMessage.MSG_STATUS_TIMEOUT || message.getSendStatus() == ChatMessage.MSG_STATUS_FAIL
                                || message.getSendStatus() == ChatMessage.MSG_STATUS_NETERROR) {
                            hv.mVoiceState.setVisibility(View.GONE);
                            hv.mVoiceAnim.setVisibility(View.GONE);
                            hv.pb_sending.setVisibility(View.GONE);
                            hv.mNoSend.setVisibility(View.VISIBLE);
                            hv.mNoSend.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    message.setSendStatusTodb(ChatMessage.MSG_STATUS_INPROCESS);
                                    // 显示下载
                                    if (imManager.downloadVoice(message)) {
                                        v.setVisibility(View.GONE);
                                        hv.mVoiceAnim.setVisibility(View.VISIBLE);
                                        hv.pb_sending.setVisibility(View.VISIBLE);
                                        // ((RelativeLayout)
                                        // v.getParent()).getChildAt(3).setVisibility(View.VISIBLE);
                                    } else {
                                        message.setSendStatusTodb(ChatMessage.MSG_STATUS_FAIL);
                                        CustomToast.showShortToast(mContext, "接受语音消息失败");
                                    }
                                }
                            });
                        }
                        hv.mVoiceState.setText(message.getContent());// 语音消息时间
                        AudioManagers.byVoiceLenthView(Integer.parseInt(message.getContent()), hv.mVoiceImg, mContext);
                        final AnimationDrawable anima = (AnimationDrawable) hv.mVoiceAnim.getBackground();
                        if (voiceStatus.getPos() >= 0
                                && voiceStatus.getPos() == position
                                && voiceStatus.isStatus()) {
                            anima.selectDrawable(0);
                            anima.start();
                            animation = anima;
                        } else {
                            anima.selectDrawable(0);
                            anima.stop();
                        }

                        if (!message.getIsFromMyself() && (message.getSendStatus() == ChatMessage.MSG_STATUS_INPROCESS
                                || message.getSendStatus() == ChatMessage.MSG_STATUS_FAIL)) {
                            hv.mVoiceImg.setOnClickListener(null);
                        } else {

                            hv.mVoiceImg.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View view) {
                                    pop = new MessagePop(ChatMessageWithPerson.this, ChatMessageWithPerson.this, conversationinfo, message, 1);
                                    int[] location = new int[2];
                                    view.getLocationOnScreen(location);
                                    pop.showAtLocation(view, Gravity.NO_GRAVITY, location[0] + view.getWidth() / 2 - pop.getWidth() / 2, location[1] - pop.getHeight());
                                    return false;
                                }
                            });
                            hv.mVoiceImg.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    message.setVoiceReadStatus(ChatMessage.MSG_STATUS_READED);
                                    if (message.getSendStatus() == ChatMessage.MSG_STATUS_FAIL ||
                                            message.getSendStatus() == ChatMessage.MSG_STATUS_INPROCESS
                                            || message.getSendStatus() == ChatMessage.MSG_STATUS_TIMEOUT) {
                                        CustomToast.showShortToast(mContext, "语音下载失败，请点击下载!");
                                        return;
                                    }
                                    hv.mVoiceState.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);// 消息已读则只显示时间
                                    if (voiceStatus.getPos() >= 0
                                            && voiceStatus.getPos() == position
                                            && voiceStatus.isStatus()) {
                                        mMsgListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
                                        imManager.stopPlayerVoice();
                                        animation.selectDrawable(0);
                                        animation.stop();
                                        voiceStatus.put(voiceStatus.getPos(), false);
                                    } else {

                                        if (voiceStatus.getPos() > 0) {
                                            imManager.stopPlayerVoice();
                                            if (animation != null) {
                                                animation.selectDrawable(0);
                                                animation.stop();
                                            }
                                        }
                                        animation = anima;

                                        imManager.startPlayerVoice(message.getPath(),
                                                (ChatMessageWithPerson) mContext);
                                        animation.start();
                                        voicePath = message.getPath();
                                        voiceStatus.put(position, true);
                                        notifyDataSetChanged();
                                    }
                                }

                            });
                        }
                        break;
                    // 位置消息
                    case MSG_DATA_LOCALMAP:
                        hv.mUserHead.setVisibility(View.VISIBLE);
                        hv.mVoiceImg.setVisibility(View.GONE);
                        hv.mVoiceState.setVisibility(View.GONE);
                        hv.mVoiceAnim.setVisibility(View.GONE);
                        hv.mContentTv.setVisibility(View.GONE);
                        hv.mImgView.setVisibility(View.VISIBLE);
                        hv.mSystemTv.setVisibility(View.GONE);
                        hv.mImgView.setLocation(true);
                        hv.mImgView.setLocationText(message.getLocationMapMsg().getDetailAddr());
                        BitmapTools.loadChatLocationBitmap(ChatMessageWithPerson.this, message.getLocationMapMsg().getThumbnailPath(), hv.mImgView, CommonUtils.getScreenWid((Activity) mContext),
                                CommonUtils.getScreenHigh((Activity) mContext));
                        hv.mImgView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                pop = new MessagePop(ChatMessageWithPerson.this, ChatMessageWithPerson.this, conversationinfo, message, 2);
                                int[] location = new int[2];
                                view.getLocationOnScreen(location);
                                pop.showAtLocation(view, Gravity.NO_GRAVITY, location[0] + view.getWidth() / 2 - pop.getWidth() / 2, location[1] - pop.getHeight());
                                return false;
                            }
                        });
                        hv.mImgView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mEmjoListLy.setVisibility(View.GONE);// 表情或者更多界面隐藏
                                hideSystemKeyBoard();
                                Intent intent = new Intent(ChatMessageWithPerson.this, CrmGaoDeMapWithShowActivity.class);
                                intent.putExtra("mLat", message.getLocationMapMsg().getLatitude() + "");
                                intent.putExtra("mLon", message.getLocationMapMsg().getLongitude() + "");
                                intent.putExtra("mAddress", message.getLocationMapMsg().getDetailAddr());
                                intent.putExtra("cName", "");
                                startActivity(intent);

                            }
                        });
                        break;

                    // 系统消息
                    case MSG_DATA_SYSTEM:
                        hv.mUserHead.setVisibility(View.GONE);
                        hv.mVoiceImg.setVisibility(View.GONE);
                        hv.mVoiceState.setVisibility(View.GONE);
                        hv.mVoiceAnim.setVisibility(View.GONE);
                        hv.mImgView.setVisibility(View.GONE);
                        hv.mContentTv.setVisibility(View.GONE);
                        hv.mSystemTv.setVisibility(View.VISIBLE);
                        hv.mSystemTv.getBackground().setAlpha(20);
                        CustomLog.e(message.getContent());
                        String str1 = "<font size=\"10\" color=\"#ffffff\">" + "系统消息:" + ConcatInfoUtils.getInstance().trantleStr(message.getContent()) + "</font>";
                        hv.mSystemTv.setText(Html.fromHtml(str1));
                        break;
                }

            }

            if (null != msgTimeMap.get(position)) {
                hv.mTimeTv.setVisibility(View.VISIBLE);
                hv.mTimeTv.setText(msgTimeMap.get(position));
            } else {
                hv.mTimeTv.setVisibility(View.GONE);
            }

            if (position == currentMsgList.size() - 1) {
                convertView.setPadding(0, 20, 0, 20);
            }
            else {
                convertView.setPadding(0, 20, 0, 0);
            }

            return convertView;

        }

        private HolderView setHolderViewMy(HolderView hv, View view) {
            hv = new HolderView();
            hv.mTimeTv = (TextView) view.findViewById(R.id.timestamp);
            hv.mContentTv = (EmojiconTextView) view.findViewById(R.id.tv_chatcontent);
            hv.pb_sending = (ProgressBar) view.findViewById(R.id.pb_sending);
            hv.mUserHead = (BSCircleImageView) view.findViewById(R.id.head_icon_s);
            hv.mNoSend = (TextView) view.findViewById(R.id.tv_delivered);
            hv.mImgView = (IMChatImageView) view.findViewById(R.id.message_list_iv);
            hv.mVoiceImg = (ImageView) view.findViewById(R.id.iv_voice);
            hv.mVoiceState = (TextView) view.findViewById(R.id.voice_state);
            hv.mVoiceAnim = (ImageView) view.findViewById(R.id.iv_voice_anim);
            return hv;
        }

        private HolderView setHolderViewYou(HolderView hv, View view) {
            hv = new HolderView();
            hv.mTimeTv = (TextView) view.findViewById(R.id.timestamp);
            hv.mContentTv = (EmojiconTextView) view.findViewById(R.id.tv_chatcontent);
            hv.mUserHead = (BSCircleImageView) view.findViewById(R.id.head_icon_s);
            hv.mImgView = (IMChatImageView) view.findViewById(R.id.message_list_iv);
            hv.mVoiceImg = (ImageView) view.findViewById(R.id.iv_voice);
            hv.pb_sending = (ProgressBar) view.findViewById(R.id.pb_sending);
            hv.mVoiceState = (TextView) view.findViewById(R.id.voice_state);
            hv.mSystemTv = (TextView) view.findViewById(R.id.system_info);
            hv.mNoSend = (TextView) view.findViewById(R.id.tv_delivered);
            hv.mVoiceAnim = (ImageView) view.findViewById(R.id.iv_voice_anim);
            return hv;
        }

        class HolderView {
            public TextView mTimeTv, mNoSend, mVoiceState, mSystemTv;
            public EmojiconTextView mContentTv;
            public ProgressBar pb_sending;
            public BSCircleImageView mUserHead;
            public IMChatImageView mImgView;
            public ImageView mVoiceImg, mVoiceAnim;
        }

        class HolderViewDirection {
            public HolderView leftHolderView;
            public HolderView rightHolderView;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            // TODO Auto-generated method stub
            ChatMessage message = currentMsgList.get(position);
            if (message.getIsFromMyself()) {
                return RIGHTVIEW;
            } else {
                return LEFTVIEW;
            }
        }

        private void initMsgTimeList() {
            msgTimeList.clear();
            msgTimeMap.clear();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String nowtime = format.format(new Date());
            int oldtime = 0;
            synchronized (currentMsgList) {
                int size = currentMsgList.size();
                for (int i = 0; i < size; i++) {
                    Date d1 = new Date(currentMsgList.get(i).getSendTime());
                    String time = format.format(d1);
                    msgTimeList.add(time);
                    if (i > 0
                            && msgTimeList.get(i - 1).substring(0, 10)
                                    .equals(time.substring(0, 10))) {
                        // 同一天
                        int newtime = Integer.valueOf(time.substring(11, 13))
                                * 60 + Integer.valueOf(time.substring(14, 16));
                        // 大于四分钟的才显示时间
                        if ((newtime - oldtime) > 4) {
                            oldtime = newtime;
                            if (time.subSequence(0, 4).equals(nowtime.substring(0, 4))) {
                                if (time.subSequence(0, 10).equals(nowtime.subSequence(0, 10))) {
                                    msgTimeMap.put(i, time.substring(11));
                                } else {
                                    msgTimeMap.put(i, time.substring(5));
                                }
                            } else {
                                msgTimeMap.put(i, time);
                            }
                        }
                    } else {
                        // 不同天
                        oldtime = Integer.valueOf(time.substring(11, 13)) * 60
                                + Integer.valueOf(time.substring(14, 16));
                        if (time.subSequence(0, 4).equals(
                                nowtime.substring(0, 4))) {
                            if (time.subSequence(0, 10).equals(
                                    nowtime.subSequence(0, 10))) {
                                msgTimeMap.put(i, time.substring(11));
                            } else {
                                msgTimeMap.put(i, time.substring(5));
                            }
                        } else {
                            // 不同年
                            CustomLog.d("不同年：" + time);
                            msgTimeMap.put(i, time);
                        }
                    }
                }
            }
        }

        public void startAnimation(ImageView v, int resId) {
            v.setImageResource(resId);
            voiceAnimation = (AnimationDrawable) v.getDrawable();
            voiceAnimation.start();
        }

        public void stopAnimation(ImageView v, int resId) {
            v.setImageResource(resId);
            voiceAnimation = (AnimationDrawable) v.getDrawable();
            voiceAnimation.stop();
        }

    }

    @Override
    public void msgHandle(ChatMessage msg) {
        if (voiceStatus != null && voiceStatus.isStatus()) {
            for (int i = 0; i < currentMsgList.size(); i++) {
                if (currentMsgList.get(i).equals(msg)
                        && i <= voiceStatus.getPos()) {
                    if (i == voiceStatus.getPos()) {
                        imManager.stopPlayerVoice();
                        voiceStatus.clear();
                    } else {
                        voiceStatus.put(voiceStatus.getPos() - 1,
                                voiceStatus.isStatus());
                    }
                }
            }
        }
        synchronized (currentMsgList) {
            currentMsgList.remove(msg);
            mMsgListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);
            chatMessageHomeAdapter.notifyDataSetChanged();
        }
    }

    // 跳转到群聊详情
    public void onGetDuciss() {
        Intent i = new Intent();
        i.setClass(mContext, ChatGroupDetail.class);
        i.putExtra("discussion", getIntent().getSerializableExtra("discussion"));
        i.putExtra("conversation", conversationinfo);
        startActivityForResult(i, 104);
    }

    private void setInCallBySdk() {
        if (mAudioManager == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (mAudioManager.getMode() != AudioManager.MODE_IN_COMMUNICATION) {
                mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            }
            try {
                Class clazz = Class.forName("android.media.AudioSystem");
                Method m = clazz.getMethod("setForceUse", new Class[] {
                        int.class, int.class
                });
                m.invoke(null, 1, 1);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            if (mAudioManager.getMode() != AudioManager.MODE_IN_CALL) {
                mAudioManager.setMode(AudioManager.MODE_IN_CALL);
            }
        }
        if (mAudioManager.isSpeakerphoneOn()) {
            mAudioManager.setSpeakerphoneOn(false);
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            mAudioManager.adjustStreamVolume(AudioManager.STREAM_VOICE_CALL, AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Float mProximiny = event.values[0];

        if (mProximiny >= 0.0 && mProximiny < 5.0f &&
                mProximiny < mSensor.getMaximumRange()) {
            mNearFace = true;
            setInCallBySdk();
            if (!TextUtils.isEmpty(voicePath)) {
                imManager.stopPlayerVoice();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        imManager.startPlayerVoice(voicePath, ChatMessageWithPerson.this);
                    }

                }, 1000);

            }
        }
        else {
            mNearFace = false;
            if (mAudioManager == null) {
                return;
            }
            setVolumeControlStream(AudioManager.STREAM_SYSTEM);
            mAudioManager.setSpeakerphoneOn(true);
            mAudioManager.setMode(AudioManager.MODE_NORMAL);
            if (!mAudioManager.isSpeakerphoneOn()) {
                mAudioManager.setSpeakerphoneOn(true);
                mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                        mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),
                        AudioManager.STREAM_VOICE_CALL);
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
