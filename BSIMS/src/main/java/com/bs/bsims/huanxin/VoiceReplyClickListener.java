
package com.bs.bsims.huanxin;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bs.bsims.R;
import com.easemob.chat.EMChatManager;

import java.io.File;
import java.io.IOException;

/**
 * 语音回复点击事件
 * 
 * @author Administrator
 */
public class VoiceReplyClickListener implements View.OnClickListener {

    private ImageView imgVoiceIconView;
    private AnimationDrawable voiceAnimation = null;

    private MediaPlayer mediaPlayer = null;
    private ImageView iv_read_status;
    private Activity activity;
    private String filename;
    private BaseAdapter adapter;

    public static boolean isPlaying = false;
    public static VoiceReplyClickListener currentPlayListener = null;

    /**
     * @param message
     * @param v
     * @param iv_read_status
     * @param context
     * @param activity
     * @param user
     * @param chatType
     */
    public VoiceReplyClickListener(String filename, ImageView v, Activity activity) {
        imgVoiceIconView = v;
        this.filename = filename;
        this.activity = activity;

    }

    private void stopPlayVoice() {
        imgVoiceIconView.setImageResource(R.anim.voice_from_icon);
        voiceAnimation = (AnimationDrawable) imgVoiceIconView.getDrawable();
        voiceAnimation.stop();
        imgVoiceIconView.setImageResource(R.drawable.chatfrom_voice_playing);
        // stop play voice
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        isPlaying = false;
    }

    private void playVoice(String filePath) {
        if (!(new File(filePath).exists())) {
            return;
        }
        AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);

        mediaPlayer = new MediaPlayer();
        if (EMChatManager.getInstance().getChatOptions().getUseSpeaker()) {
            audioManager.setMode(AudioManager.MODE_NORMAL);
            audioManager.setSpeakerphoneOn(true);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
        }
        else {
            audioManager.setSpeakerphoneOn(false); // 关闭扬声器
            // 把声音设定成Earpiece（听筒）出来，设定为正在通话中
            audioManager.setMode(AudioManager.MODE_IN_CALL);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
        }

        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            isPlaying = true;
            currentPlayListener = this;
            mediaPlayer.start();
            showAnimation();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                    stopPlayVoice(); // stop animation
                }
            });

        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            // mediaPlayer.release();
            // mediaPlayer = null;
            // stopPlayVoice(); // stop animation
        }

    }

    // show the voice playing animation
    private void showAnimation() {
        // play voice, and start animation
        imgVoiceIconView.setImageResource(R.anim.voice_from_icon);
        voiceAnimation = (AnimationDrawable) imgVoiceIconView.getDrawable();
        voiceAnimation.start();
    }

    @Override
    public void onClick(View v) {
        if (isPlaying) {
            currentPlayListener.stopPlayVoice();
        } else {
            File file = new File(filename);
            if (file.exists() && file.isFile())
                playVoice(filename);
            else {
                Log.d("file not exist", filename);
                System.err.println("file not exist");
            }
        }
    }

    interface OnVoiceStopListener {
        void onStop();

        void onStart();
    }

}
