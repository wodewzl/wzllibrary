
package com.bs.bsims.chatutils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.bs.bsims.R;
import com.bs.bsims.chatutils.AudioManagers.AudioStageListener;
import com.bs.bsims.utils.FileUtil;
import com.yzxIM.IMManager;

import java.util.Timer;
import java.util.TimerTask;

public class AudioRecordButton extends Button {

    private static final int STATE_NORMAL = 1;// 默认的状态
    private static final int STATE_RECORDING = 2;// 正在录音
    private static final int STATE_WANT_TO_CANCEL = 3;// 希望取消

    private int mCurrentState = STATE_NORMAL; // 当前的状态
    private boolean isRecording = false;// 已经开始录音

    private static final int DISTANCE_Y_CANCEL = 50;

    private DialogManager mDialogManager;
    private AudioManagers mAudioManager;

    private IMManager imManager;// 聊天管理器
    // private VoiceStatus voiceStatus;

    private float mTime;
    // 是否触发longClick
    private boolean mReady;

    private static final int MSG_AUDIO_PREPARED = 0x110;
    private static final int MSG_VOICE_CHANGED = 0x111;
    private static final int MSG_DIALOG_DIMISS = 0x112;
    private Timer timer = null;
    private int num = 0;

    private Handler mUIHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 59) {
                setBackgroundResource(R.drawable.im_button_recordnormal);
                setText("按住 说话");
                stopTimer();
                mAudioManager.release();
                if (audioFinishRecorderListener != null) {
                    audioFinishRecorderListener.onFinish(num, mAudioManager.getCurrentFilePath());
                }
                return;
            }
            if (msg.what > 50) {
                mDialogManager.NoTimeRe("您还有" + (60 - msg.what) + "秒可以说话");
            }
        }
    };

    // 语音定时器
    private void startRecordTimer() {
        stopTimer();
        if (timer == null) {
            timer = new Timer();
        }
        num = 0;
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                mUIHandler.obtainMessage(num).sendToTarget();
                num++;
            }
        }, 0, 1000);
    }

    // 语音定时器
    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /*
     * 获取音量大小的线程
     */
    private Runnable mGetVoiceLevelRunnable = new Runnable() {

        public void run() {
            while (isRecording) {
                try {
                    Thread.sleep(100);
                    mTime += 0.1f;
                    mHandler.sendEmptyMessage(MSG_VOICE_CHANGED);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_AUDIO_PREPARED:
                    // 显示對話框在开始录音以后
                    mDialogManager.showRecordingDialog();
                    isRecording = true;
                    // 开启一个线程
                    new Thread(mGetVoiceLevelRunnable).start();
                    break;

                case MSG_VOICE_CHANGED:
                    mDialogManager.updateVoiceLevel(mAudioManager.getVoiceLevel(7));
                    break;

                case MSG_DIALOG_DIMISS:
                    mDialogManager.dimissDialog();
                    break;

            }

            super.handleMessage(msg);
        }
    };

    /**
     * 以下2个方法是构造方法
     */
    public AudioRecordButton(final Context context, AttributeSet attrs) {
        super(context, attrs);
        mDialogManager = new DialogManager(context);
        imManager = IMManager.getInstance(context);
        String dir = FileUtil.getSaveFilePath(context);
        mAudioManager = AudioManagers.getInstance(dir);
        mAudioManager.setOnAudioStageListener(new AudioStageListener() {

            @Override
            public void wellPrepared() {
                // TODO Auto-generated method stub
                mHandler.sendEmptyMessage(MSG_AUDIO_PREPARED);
            }
        });

        // 由于这个类是button所以在构造方法中添加监听事件
        setOnLongClickListener(new OnLongClickListener() {

            public boolean onLongClick(View v) {
                mReady = true;

                mAudioManager.prepareAudio((Activity)context);

                return false;
            }
        });
    }

    public AudioRecordButton(Context context) {
        this(context, null);
    }

    /**
     * 录音完成后的回调
     */
    public interface AudioFinishRecorderListener {
        void onFinish(float seconds, String filePath);
    }

    private AudioFinishRecorderListener audioFinishRecorderListener;

    public void setAudioFinishRecorderListener(AudioFinishRecorderListener listener) {
        audioFinishRecorderListener = listener;
    }
    
    private AudioRecorderDownListener audioRecorderDownListener;
    /*语音按下动画*/
    public interface AudioRecorderDownListener {
        void onAudioDown();
    }
    public void setAudioRecorderDownListener(AudioRecorderDownListener listener) {
        audioRecorderDownListener = listener;
    }
    /**
     * 屏幕的触摸事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        int x = (int) event.getX();// 获得x轴坐标
        int y = (int) event.getY();// 获得y轴坐标

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 如果正在播放声音，停止播放声音
                audioRecorderDownListener.onAudioDown();
                startRecordTimer();
                changeState(STATE_RECORDING);
                break;
            case MotionEvent.ACTION_MOVE:

                if (isRecording) {
                    // 如果想要取消，根据x,y的坐标看是否需要取消
                    if (wantToCancle(x, y)) {
                        changeState(STATE_WANT_TO_CANCEL);
                    } else {
                        changeState(STATE_RECORDING);
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                stopTimer();
                if (!mReady) {
                    reset();
                    return super.onTouchEvent(event);
                }
                if (!isRecording || mTime < 0.6f) {
                    mDialogManager.tooShort();
                    mAudioManager.cancel();
                    mHandler.sendEmptyMessageDelayed(MSG_DIALOG_DIMISS, 1000);// 延迟显示对话框
                } else if (mCurrentState == STATE_RECORDING) { // 正在录音的时候，结束
                    mDialogManager.dimissDialog();
                    mAudioManager.release();

                    if (audioFinishRecorderListener != null) {
                        audioFinishRecorderListener.onFinish(num, mAudioManager.getCurrentFilePath());
                    }

                } else if (mCurrentState == STATE_WANT_TO_CANCEL) { // 想要取消
                    mDialogManager.dimissDialog();
                    mAudioManager.cancel();
                }
                reset();
                break;

        }
        return super.onTouchEvent(event);
    }

    /**
     * 恢复状态及标志位
     */
    private void reset() {
        isRecording = false;
        mTime = 0;
        mReady = false;
        changeState(STATE_NORMAL);
    }

    private boolean wantToCancle(int x, int y) {
        if (x < 0 || x > getWidth()) { // 超过按钮的宽度
            return true;
        }
        // 超过按钮的高度
        if (y < -DISTANCE_Y_CANCEL || y > getHeight() + DISTANCE_Y_CANCEL) {
            return true;
        }

        return false;
    }

    /**
     * 改变
     */
    private void changeState(int state) {
        if (mCurrentState != state) {
            mCurrentState = state;
            switch (state) {
                case STATE_NORMAL:
                    setBackgroundResource(R.drawable.im_button_recordnormal);
                    setText("按住 说话");
                    break;

                case STATE_RECORDING:
                    setBackgroundResource(R.drawable.im_button_recording);
                    setText("松开 结束");
                    if (isRecording) {
                        mDialogManager.recording();
                    }
                    break;

                case STATE_WANT_TO_CANCEL:
                    setBackgroundResource(R.drawable.im_button_recording);
                    setText("松开手指，取消发送");

                    mDialogManager.wantToCancel();
                    break;
            }
        }
    }
}
