
package com.bs.bsims.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bs.bsims.R;
import com.bs.bsims.chatutils.ConcatInfoUtils;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.emoji.EmojiAdapter;
import com.bs.bsims.emoji.Emojicon;
import com.bs.bsims.emoji.PeopleA;
import com.bs.bsims.emoji.PeopleB;
import com.bs.bsims.emoji.PeopleC;
import com.bs.bsims.huanxin.AudioRecorder;
import com.bs.bsims.huanxin.ChatResource;
import com.bs.bsims.huanxin.ExpressionPagerAdapter;
import com.bs.bsims.huanxin.VoicePlayClickListener;
import com.bs.bsims.model.DiscussVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.view.BsPasteEditTextIM;
import com.easemob.util.VoiceRecorder;

import org.xutils.x;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommentFragment extends Fragment implements OnClickListener {

    // private Context mActivity;
    private static final String TAG = "CommentFragment";

    @ViewInject(R.id.mic_image)
    private ImageView micImage;

    @ViewInject(R.id.et_sendmessages)
    private BsPasteEditTextIM mEditTextContent;

    @ViewInject(R.id.btn_set_mode_keyboard)
    private View buttonSetModeKeyboard;

    @ViewInject(R.id.btn_set_mode_voice)
    private View buttonSetModeVoice;

    @ViewInject(R.id.btn_send)
    private View btnSend;

    @ViewInject(R.id.btn_press_to_speak)
    private View buttonPressToSpeak;

    @ViewInject(R.id.vPager)
    private ViewPager expressionViewpager;

    @ViewInject(R.id.ll_face_container)
    private LinearLayout expressionContainer;

    @ViewInject(R.id.ll_btn_container)
    private LinearLayout btnContainer;

    @ViewInject(R.id.more1)
    private View more;

    @ViewInject(R.id.iv_emoticons_normal)
    private ImageView iv_emoticons_normal;
    @ViewInject(R.id.iv_emoticons_checked)
    private ImageView iv_emoticons_checked;

    @ViewInject(R.id.edittext_layout)
    private RelativeLayout edittext_layout;

    private ListView listView;
    private ImageView locationImgview;
    private int position;
    private InputMethodManager inputMethodManager;
    private Drawable[] micImages;
    private int chatType;
    private VoiceRecorder voiceRecorder;
    // private MessageAdapter adapter;
    static int resendPos;
    private Activity mActivity;

    @ViewInject(R.id.record)
    private TextView record;
    private Dialog dialog;
    private AudioRecorder mr;
    private MediaPlayer mediaPlayer;
    Button player;
    TextView luyin_txt, luyin_path;
    private Thread recordThread;

    private static int MAX_TIME = 15;
    private static int MIX_TIME = 1;
    private static int RECORD_NO = 0;
    private static int RECORD_ING = 1;
    private static int RECODE_ED = 2;

    private static int RECODE_STATE = 0;

    private static float recodeTime = 0.0f;
    private static double voiceValue = 0.0;

    private ImageView dialog_img;
    private static boolean playState = false;
    private String commentid;
    private DiscussVO mDiscussVO;
    private String mHint = "";
    private Emojicon[] emojicon;
    // 语音动画切换
    private Handler micImageHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            changeRecordingAnim(msg.what);
        }
    };

    // 广播的action
    private String filterAction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 获取activity的广播action
        Bundle bundle = getArguments();
        filterAction = bundle.getString(Constant.DocKey);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // mActivity = inflater.getContext();

        View view = inflater.inflate(R.layout.huanxin_part_bottom_reply, null);
        x.view().inject(this, view);

        // 切换成文本输入模式的按钮
        buttonSetModeKeyboard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // CustomToast.showShortToast(mActivity, "切换成文字输入");
                v.setVisibility(View.GONE); // 键盘图标-隐藏
                more.setVisibility(View.GONE); // 表情-隐藏
                buttonPressToSpeak.setVisibility(View.GONE); // 按住说话按钮-隐藏

                buttonSetModeVoice.setVisibility(View.VISIBLE); // 语音按钮-显示
                edittext_layout.setVisibility(View.VISIBLE);
                mEditTextContent.requestFocus();

                iv_emoticons_normal.setVisibility(View.VISIBLE);
            }
        });
        bindViewsListeners();
        return view;
    }

    /**
     * 初始化一些数据
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        inputMethodManager = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // 动画资源文件,用于录制语音时
        micImages = ChatResource.getInstance().getMicImages(mActivity);

        // 初始化表情viewpager
        List<View> views = new ArrayList<View>();
        View gv1 = getGridChildView(1);
        View gv2 = getGridChildView(2);
        View gv3 = getGridChildView(3);
        views.add(gv1);
        views.add(gv2);
        views.add(gv3);
        expressionViewpager.setAdapter(new ExpressionPagerAdapter(views));

        voiceRecorder = new VoiceRecorder(micImageHandler);
        // buttonPressToSpeak.setOnTouchListener(new PressToSpeakListen());
        buttonPressToSpeak.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        if (RECODE_STATE != RECORD_ING) {
                            scanOldFile();
                            mr = new AudioRecorder("voice");
                            RECODE_STATE = RECORD_ING;
                            showVoiceDialog();
                            try {
                                mr.start(mActivity);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            mythread();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (RECODE_STATE == RECORD_ING) {
                            RECODE_STATE = RECODE_ED;
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            try {
                                mr.stop();
                                voiceValue = 0.0;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (recodeTime < MIX_TIME) {
                                showWarnToast();
                                RECODE_STATE = RECORD_NO;
                            } else {
                                // record.setText("按住说话");
                                sendVoice(getAmrPath(), "111", (int) recodeTime + "", false);

                                // luyin_txt.setText("1" + ((int) recodeTime));
                                // luyin_path.setText("2" + getAmrPath());
                            }
                        }

                        break;
                }
                return true;
            }
        });
        // 监听文字框
        mEditTextContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    buttonSetModeVoice.setVisibility(View.GONE);
                    btnSend.setVisibility(View.VISIBLE);
                } else {
                    if (buttonSetModeKeyboard.getVisibility() != View.VISIBLE) {
                        buttonSetModeVoice.setVisibility(View.VISIBLE);
                        btnSend.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 语音的布局
        voiceView = View.inflate(mActivity, R.layout.chat_voice_dialog, null);

        // 获取windowManager
        windowManager = (WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE);

        // 发送按钮的监听
        btnSend.setOnClickListener(sendClickListener);
        registBroadcast();
    }

    private OnClickListener sendClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("send text", "begin to send");
            String s = ConcatInfoUtils.convertToMsg(mEditTextContent.getText(), mActivity);
            String text = null; // 评论的内容
            String hint = null; // 默认文本提示
            CharSequence hint2 = mEditTextContent.getHint();
            if (null != hint2) {
                hint = mEditTextContent.getHint().toString();
            }

            mEditTextContent.setText("");
            mEditTextContent.setHint("");

            // btnSend.setOnClickListener(null);

            if (null != hint && hint.length() > 0) {
                text = hint + s;
            } else {
                text = s;
            }

            sendText(text, s);

            iv_emoticons_normal.setVisibility(View.VISIBLE);
            iv_emoticons_checked.setVisibility(View.GONE);
            btnContainer.setVisibility(View.VISIBLE);
            expressionContainer.setVisibility(View.GONE);
            more.setVisibility(View.GONE);
        }
    };

    /**
     * 输入框设置默认的文本提示，并弹出键盘，适用于针对性回复
     * 
     * @param hint
     */
    public void setFrontText(String hint) {
        mEditTextContent.setText("");
        mEditTextContent.setHint(hint);
        mEditTextContent.requestFocus();

        InputMethodManager inputManager =
                (InputMethodManager) mEditTextContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.showSoftInput(mEditTextContent, 0);

    }

    public void setFrontString(SpannableString s) {
        mEditTextContent.setText("");
        mEditTextContent.setHint(s);
        mEditTextContent.requestFocus();

        InputMethodManager inputManager =
                (InputMethodManager) mEditTextContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.showSoftInput(mEditTextContent, 0);

    }

    /**
     * 点击发送按钮(发文字和表情)
     * 
     * @param v
     */
    // @OnClick(R.id.btn_send)
    public void sendTextSmilingOnclick(View v) {
        Log.d("send text", "begin to send");

        String hint = mEditTextContent.getHint().toString();
        String s =  ConcatInfoUtils.convertToMsg(mEditTextContent.getText(), mActivity);
        sendText(hint + s, s);

        iv_emoticons_normal.setVisibility(View.VISIBLE);
        iv_emoticons_checked.setVisibility(View.GONE);
        btnContainer.setVisibility(View.VISIBLE);
        expressionContainer.setVisibility(View.GONE);
        more.setVisibility(View.GONE);
    }

    /**
     * 点击隐藏表情框
     * 
     * @param v
     */
    public void emoticonsCheckedOnclick(View v) {
        iv_emoticons_normal.setVisibility(View.VISIBLE);
        iv_emoticons_checked.setVisibility(View.GONE);
        btnContainer.setVisibility(View.VISIBLE);
        expressionContainer.setVisibility(View.GONE);
        more.setVisibility(View.GONE);
    }

    /**
     * 切换到文字输入模式
     * 
     * @param v
     */
    // @OnClick(R.id.btn_set_mode_keyboard)
    public void setModeKeyboardOnclick(View v) {

        v.setVisibility(View.GONE); // 键盘图标-隐藏
        more.setVisibility(View.GONE); // 表情-隐藏
        buttonPressToSpeak.setVisibility(View.GONE); // 按住说话按钮-隐藏

        buttonSetModeVoice.setVisibility(View.VISIBLE); // 语音按钮-显示
        edittext_layout.setVisibility(View.VISIBLE);
        mEditTextContent.requestFocus();

    }

    /**
     * 点击文字输入框
     * 
     * @param v
     */
    public void editClick(View v) {
        if (more.getVisibility() == View.VISIBLE) {
            more.setVisibility(View.GONE);
            iv_emoticons_normal.setVisibility(View.VISIBLE);
            iv_emoticons_checked.setVisibility(View.GONE);
        }
    }

    /**
     * 隐藏软键盘
     */
    private void hideKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 当消息发送成功后，发送按钮可以点击，清空edit数据
     */
    public void successOpenSendClickListener() {
        btnSend.setOnClickListener(sendClickListener);
        mEditTextContent.setText("");
        mEditTextContent.setHint("");
    }

    /**
     * 当消息发送失败后，发送按钮可以点击，不清空edit数据！
     */
    public void errorOpenSendClickListener() {
        btnSend.setOnClickListener(sendClickListener);
    }

    /**
     * 发送文本消息到制定的广播接收者
     * 
     * @param content message content
     * @param isResend boolean resend
     */
    private void sendText(String content, String reContent) {
        // CustomToast.showShortToast(mActivity, "正在发送文本消息");
        Intent intent = new Intent(filterAction);

        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        Matcher m = p.matcher(content);
        String newStr = m.replaceAll("");
        if (newStr.length() == 0) {
            return;
        }

        if (!"".equals(mHint)) {
            intent.putExtra("recontent", reContent);
        }
        intent.putExtra("content", content);
        Log.d("sendtext in frament", "begin to send text");
        // 这里可以传递一个对象给activity用于列表显示，统一传1种类型

        intent.putExtra("length", "-1");
        if (!TextUtils.isEmpty(commentid)) {
            intent.putExtra("commentid", commentid);
        }
        if (null != mDiscussVO) {
            intent.putExtra("mDiscussVO", mDiscussVO);
        }
        mActivity.sendBroadcast(intent);
        hideKeyboard();
        mHint = "";
        buttonSetModeVoice.setClickable(true);
    }

    private View getGridChildView(int i) {
        View view = View.inflate(mActivity, R.layout.expression_gridview, null);
        GridView gridView = (GridView) view.findViewById(R.id.gridview);
        if (i == 1) {
            emojicon = PeopleA.DATA;
        }
        else if (i == 2) {
            emojicon = PeopleB.DATA;
        }
        else {
            emojicon = PeopleC.DATA;
        }

        final EmojiAdapter me = new EmojiAdapter((Context) mActivity, emojicon);
        gridView.setAdapter(me);
        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Emojicon filename = me.getItem(position);
                try {
                    if (mEditTextContent == null || filename == null) {
                        return;
                    }

                    int start = mEditTextContent.getSelectionStart();
                    int end = mEditTextContent.getSelectionEnd();
                    if (start < 0) {
                        mEditTextContent.append(filename.getEmoji());
                    } else {
                        mEditTextContent.getText().replace(Math.min(start, end), Math.max(start, end), filename.getEmoji(), 0, filename.getEmoji().length());
                    }
                } catch (Exception e) {
                }

            }
        });
        return view;
    }

    /**
     * 按住说话listener
     */
    class PressToSpeakListen implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    if (!CommonUtils.isExitsSdcard()) {
                        Toast.makeText(mActivity, "发送语音需要sdcard支持！", Toast.LENGTH_LONG);
                        // CustomToast.showShortToast(mActivity, "发送语音需要sdcard支持！");
                        return false;
                    }

                    try {
                        v.setPressed(true);
                        if (VoicePlayClickListener.isPlaying)
                            VoicePlayClickListener.currentPlayListener.stopPlayVoice();

                        // 显示录音动画
                        showRecordingAnim(1);

                        // 核心方法
                        voiceRecorder.startRecording(null, "1111232", mActivity.getApplicationContext());
                    } catch (Exception e) {
                        e.printStackTrace();
                        v.setPressed(false);
                        if (voiceRecorder != null) {
                            voiceRecorder.discardRecording();
                        }
                        // 这两条语句主要用来测试并对付有些手机的崩溃问题
                        // windowManager.removeView(inflateView);
                        hideKeyboard();

                        // CustomToast.showShortToast(mActivity,
                        // getResources().getString(R.string.recoding_fail));
                        return false;
                    }

                    return true;
                case MotionEvent.ACTION_MOVE: {
                    // CustomLog.v(getTag(), " >>>  ACTION_MOVE   移动 <<<");
                    TextView recordingHint = (TextView) voiceView.findViewById(R.id.recording_hint);
                    if (event.getY() < 0) {
                        recordingHint.setText(getString(R.string.release_to_cancel));
                        recordingHint.setBackgroundResource(R.drawable.recording_text_hint_bg);
                    } else {
                        recordingHint.setText(getString(R.string.move_up_to_cancel));
                        recordingHint.setBackgroundColor(Color.TRANSPARENT);
                    }

                    return true;
                }

                case MotionEvent.ACTION_UP:
                    // CustomLog.v(getTag(), " >>>  ACTION_UP   抬起 <<<");

                    windowManager.removeView(voiceView);

                    v.setPressed(false);

                    if (event.getY() < 0) {
                        // discard the recorded audio.
                        voiceRecorder.discardRecording();

                    } else {
                        // stop recording and send voice file
                        try {
                            int length = voiceRecorder.stopRecoding();
                            if (length > 0) {
                                Log.d("语音：", voiceRecorder.getVoiceFilePath() + ":" + length + "秒");
                                sendVoice(voiceRecorder.getVoiceFilePath(), voiceRecorder.getVoiceFileName("1111232"), Integer.toString(length), false);
                            } else {
                                // CustomToast.showShortToast(mActivity, "录音时间太短");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            // CustomToast.showShortToast(mActivity, "发送失败，请检测服务器是否连接");
                        }

                    }
                    return true;
                default:
                    return false;
            }
        }
    }

    private View voiceView;
    private WindowManager windowManager;

    /**
     * 显示录音
     * 
     * @param what 显示动画图片的标识
     */
    private void showRecordingAnim(int what) {
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        // params.gravity = Gravity.TOP | Gravity.LEFT; //屏幕左上角
        params.gravity = Gravity.CENTER | Gravity.CENTER;
        params.width = LayoutParams.WRAP_CONTENT;
        params.height = LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.format = PixelFormat.TRANSPARENT;
        // params.windowAnimations = R.style.task_list_obtain_points_anim;
        params.x = 0;
        params.y = 0;
        windowManager.addView(voiceView, params);
    }

    private void changeRecordingAnim(int what) {
        ImageView mic_image = (ImageView) voiceView.findViewById(R.id.mic_image);
        mic_image.setBackgroundDrawable(micImages[what]);

    }

    /**
     * 发送语音
     * 
     * @param filePath
     * @param fileName
     * @param length
     * @param isResend
     */
    private void sendVoice(String filePath, String fileName, String length, boolean isResend) {
        // CustomToast.showShortToast(mActivity, "我在发送语音");
        Log.d("sendvoice in frament", filePath + ":" + fileName);
        Intent intent = new Intent(filterAction);
        // 这里可以传递一个对象给activity用于列表显示，统一传1种类型
        intent.putExtra("content", filePath);
        intent.putExtra("length", length);
        mActivity.sendBroadcast(intent);
    }

    /**
     * 语音、图片或者文字发送成功后，告诉activity刷新界面
     */
    private void sendActivityMsg() {
        Intent intent = new Intent(filterAction);
        // 这里可以传递一个对象给activity用于列表显示，统一传1种类型
        intent.putExtra("list", System.currentTimeMillis() + "");
        mActivity.sendBroadcast(intent);
    }

    /**
     * 此方法是activity中点击了某进度或某个人后，fragment界面要相应的变化
     */
    public void activityClickEvent(View v) {
        // 这里的参数可以根据业务需求，改成能适合业务要求的参数
        editClick(v);
    }

    void mythread() {
        recordThread = new Thread(ImgThread);
        recordThread.start();
    }

    void setDialogImage() {
        if (voiceValue < 200.0) {
            dialog_img.setImageResource(R.drawable.record_animate_01);
        } else if (voiceValue > 200.0 && voiceValue < 400) {
            dialog_img.setImageResource(R.drawable.record_animate_02);
        } else if (voiceValue > 400.0 && voiceValue < 800) {
            dialog_img.setImageResource(R.drawable.record_animate_03);
        } else if (voiceValue > 800.0 && voiceValue < 1600) {
            dialog_img.setImageResource(R.drawable.record_animate_04);
        } else if (voiceValue > 1600.0 && voiceValue < 3200) {
            dialog_img.setImageResource(R.drawable.record_animate_05);
        } else if (voiceValue > 3200.0 && voiceValue < 5000) {
            dialog_img.setImageResource(R.drawable.record_animate_06);
        } else if (voiceValue > 5000.0 && voiceValue < 7000) {
            dialog_img.setImageResource(R.drawable.record_animate_07);
        } else if (voiceValue > 7000.0 && voiceValue < 10000.0) {
            dialog_img.setImageResource(R.drawable.record_animate_08);
        } else if (voiceValue > 10000.0 && voiceValue < 14000.0) {
            dialog_img.setImageResource(R.drawable.record_animate_09);
        } else if (voiceValue > 14000.0 && voiceValue < 17000.0) {
            dialog_img.setImageResource(R.drawable.record_animate_10);
        } else if (voiceValue > 17000.0 && voiceValue < 20000.0) {
            dialog_img.setImageResource(R.drawable.record_animate_11);
        } else if (voiceValue > 20000.0 && voiceValue < 24000.0) {
            dialog_img.setImageResource(R.drawable.record_animate_12);
        } else if (voiceValue > 24000.0 && voiceValue < 28000.0) {
            dialog_img.setImageResource(R.drawable.record_animate_13);
        } else if (voiceValue > 28000.0) {
            dialog_img.setImageResource(R.drawable.record_animate_14);
        }
    }

    private Runnable ImgThread = new Runnable() {

        @Override
        public void run() {
            recodeTime = 0.0f;
            while (RECODE_STATE == RECORD_ING) {
                if (recodeTime >= MAX_TIME && MAX_TIME != 0) {
                    imgHandle.sendEmptyMessage(0);
                } else {
                    try {
                        Thread.sleep(200);
                        recodeTime += 0.2;
                        Log.i("song", "recodeTime" + recodeTime);
                        if (RECODE_STATE == RECORD_ING) {
                            voiceValue = mr.getAmplitude();
                            imgHandle.sendEmptyMessage(1);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        Handler imgHandle = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                switch (msg.what) {
                    case 0:

                        if (RECODE_STATE == RECORD_ING) {
                            RECODE_STATE = RECODE_ED;
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            try {
                                mr.stop();
                                voiceValue = 0.0;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (recodeTime < 1.0) {
                                showWarnToast();
                                record.setText("11");
                                RECODE_STATE = RECORD_NO;
                            } else {
                                // record.setText("22");
                                // luyin_txt.setText("33" + ((int) recodeTime));
                                // luyin_path.setText("44" + getAmrPath());
                                sendVoice(getAmrPath(), "111", (int) recodeTime + "", false);
                            }
                        }
                        break;
                    case 1:
                        setDialogImage();
                        break;
                    default:
                        break;
                }

            }
        };
    };

    void showWarnToast() {
        Toast toast = new Toast(mActivity);
        LinearLayout linearLayout = new LinearLayout(mActivity);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(20, 20, 20, 20);

        // ImageView
        ImageView imageView = new ImageView(mActivity);
        imageView.setImageResource(R.drawable.voice_to_short);

        TextView mTv = new TextView(mActivity);
        mTv.setText("按下时间太短");
        mTv.setTextSize(14);
        mTv.setTextColor(Color.WHITE);
        // mTv.setPadding(0, 10, 0, 0);

        linearLayout.addView(imageView);
        linearLayout.addView(mTv);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setBackgroundResource(R.drawable.record_bg);

        toast.setView(linearLayout);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private String getAmrPath() {
        File file = new File(Environment
                .getExternalStorageDirectory(), "my/voice.amr");
        return file.getAbsolutePath();
    }

    void scanOldFile() {
        File file = new File(Environment
                .getExternalStorageDirectory(), "my/voice.amr");
        if (file.exists()) {
            file.delete();
        }
    }

    void showVoiceDialog() {
        dialog = new Dialog(mActivity, R.style.DialogStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.record_dialog);
        dialog_img = (ImageView) dialog.findViewById(R.id.dialog_img);
        dialog.show();
    }

    /**
     * 当前回复的id
     * 
     * @return
     */
    public String getCommentid() {
        return commentid;
    }

    /**
     * 当前回复的id
     */
    public void setCommentid(String commentid) {
        this.commentid = commentid;
    }

    public DiscussVO getmDiscussVO() {
        return mDiscussVO;
    }

    public void setmDiscussVO(DiscussVO mDiscussVO) {
        this.mDiscussVO = mDiscussVO;
    }

    public void registBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.DISCUSS_MSG);
        mActivity.registerReceiver(msgBroadcast, filter);
    }

    private void unRegistReceiver() {
        mActivity.unregisterReceiver(msgBroadcast);
    }

    private BroadcastReceiver msgBroadcast = new BroadcastReceiver() {
        private long mChangeTime = 0;

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constant.DISCUSS_MSG.equals(intent.getAction())) {
                mHint = intent.getStringExtra("hint");
                mEditTextContent.setHint(mHint);
                buttonSetModeVoice.setClickable(false);
            }

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegistReceiver();
    }

    public void bindViewsListeners() {
        iv_emoticons_normal.setOnClickListener(this);
        iv_emoticons_checked.setOnClickListener(this);
        buttonSetModeVoice.setOnClickListener(this);
        mEditTextContent.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_emoticons_normal:
                more.setVisibility(View.VISIBLE);
                iv_emoticons_normal.setVisibility(View.GONE);
                btnContainer.setVisibility(View.GONE);
                expressionContainer.setVisibility(View.VISIBLE);
                mEditTextContent.setOnClickListener(this);
                hideKeyboard();
                break;
            case R.id.btn_set_mode_voice:
                hideKeyboard();
                buttonSetModeVoice.setVisibility(View.GONE);
                edittext_layout.setVisibility(View.GONE);

                buttonSetModeKeyboard.setVisibility(View.VISIBLE);
                buttonPressToSpeak.setVisibility(View.VISIBLE);

                iv_emoticons_normal.setVisibility(View.INVISIBLE);

                break;
            case R.id.iv_emoticons_checked:
                emoticonsCheckedOnclick(v);
                break;
            case R.id.et_sendmessage:
                editClick(v);
                break;
            default:
                break;
        }
    }
}
