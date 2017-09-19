
package com.bs.bsims.chatutils;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.media.MediaRecorder;
import android.widget.ImageView;

import com.bs.bsims.utils.BsPermissionUtils;
import com.bs.bsims.utils.CommonUtils;

public class AudioManagers {

    private MediaRecorder mRecorder;
    private String mDirString;
    private String mCurrentFilePathString;

    private boolean isPrepared;// 是否准备好了

    /**
     * 单例化的方法 1 先声明一个static 类型的变量a 2 在声明默认的构造函数 3 再用public synchronized static 类名 getInstance() {
     * if(a==null) { a=new 类();} return a; } 或者用以下的方法
     */

    /**
     * 单例化这个类
     */
    private static AudioManagers mInstance;

    private AudioManagers(String dir) {
        mDirString = dir;
    }

    public static AudioManagers getInstance(String dir) {
        if (mInstance == null) {
            synchronized (AudioManagers.class) {
                if (mInstance == null) {
                    mInstance = new AudioManagers(dir);

                }
            }
        }
        return mInstance;

    }

    /**
     * 回调函数，准备完毕，准备好后，button才会开始显示录音框
     * 
     * @author nickming
     */
    public interface AudioStageListener {
        void wellPrepared();
    }

    public AudioStageListener mListener;

    public void setOnAudioStageListener(AudioStageListener listener) {
        mListener = listener;
    }

    // 准备方法
    public void prepareAudio(Activity activity) {
        try {
            // 一开始应该是false的
            isPrepared = false;

            File dir = new File(mDirString);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileNameString = generalFileName();
            File file = new File(dir, fileNameString);

            mCurrentFilePathString = file.getAbsolutePath();

            mRecorder = new MediaRecorder();
            // 设置输出文件
            mRecorder.setOutputFile(file.getAbsolutePath());
            // 设置meidaRecorder的音频源是麦克风
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            // 设置文件音频的输出格式为amr
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
            // 设置音频的编码格式为amr
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            // 严格遵守google官方api给出的mediaRecorder的状态流程图
            mRecorder.prepare();

            mRecorder.start();
            // 准备结束
            isPrepared = true;
            // 已经准备好了，可以录制了
            if (mListener != null) {
                mListener.wellPrepared();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            BsPermissionUtils.checkIsOpenVoice(activity);
        }

    }

    /**
     * 随机生成文件的名称
     * 
     * @return
     */
    private String generalFileName() {
        // TODO Auto-generated method stub

        return System.currentTimeMillis() + "_voice.amr";
    }

    // 获得声音的level
    public int getVoiceLevel(int maxLevel) {
        // mRecorder.getMaxAmplitude()这个是音频的振幅范围，值域是1-32767
        if (isPrepared) {
            try {
                // 取证+1，否则去不到7
                return maxLevel * mRecorder.getMaxAmplitude() / 32768 + 1;
            } catch (Exception e) {
                // TODO Auto-generated catch block

            }
        }

        return 1;
    }

    // 释放资源
    public void release() {

        try {
            // 严格按照api流程进行
            mRecorder.setOnErrorListener(null);
            mRecorder.setOnInfoListener(null);
            mRecorder.setPreviewDisplay(null);
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 取消,因为prepare时产生了一个文件，所以cancel方法应该要删除这个文件，
    // 这是与release的方法的区别
    public void cancel() {
        release();
        if (mCurrentFilePathString != null) {
            File file = new File(mCurrentFilePathString);
            file.delete();
            mCurrentFilePathString = null;
        }

    }

    public String getCurrentFilePath() {
        // TODO Auto-generated method stub
        return mCurrentFilePathString;
    }

    // 判断语音长度
    public static void byVoiceLenthView(int lenth, ImageView imageView, Context context) {
        float voice = lenth / 10;
        if (voice <= 1) {
            // 不超过10s
            if (voice < 0.5) {
                imageView.getLayoutParams().width = CommonUtils.dip2px(context, 80);
            }
            else {
                imageView.getLayoutParams().width = CommonUtils.dip2px(context, 100);
            }

        }
        else if (1 < voice && voice <= 2) {
            // 10-20s
            if (voice < 1.5) {
                imageView.getLayoutParams().width = CommonUtils.dip2px(context, 110);
            }
            else {
                imageView.getLayoutParams().width = CommonUtils.dip2px(context, 120);
            }
        }
        else if (2 < voice && voice <= 3) {
            // 20-30s
            if (voice < 2.5) {
                imageView.getLayoutParams().width = CommonUtils.dip2px(context, 130);
            }
            else {
                imageView.getLayoutParams().width = CommonUtils.dip2px(context, 140);
            }
        }
        else if (3 < voice && voice <= 4) {
            // 30-40s
            if (voice < 3.5) {
                imageView.getLayoutParams().width = CommonUtils.dip2px(context, 150);
            }
            else {
                imageView.getLayoutParams().width = CommonUtils.dip2px(context, 160);
            }

        }
        else if (4 < voice && voice <= 5) {
            // 40-50s
            if (voice < 4.5) {
                imageView.getLayoutParams().width = CommonUtils.dip2px(context, 170);
            }
            else {
                imageView.getLayoutParams().width = CommonUtils.dip2px(context, 180);
            }
        }
        else {
            // 50-60s
            if (voice < 5.5) {
                imageView.getLayoutParams().width = CommonUtils.dip2px(context, 190);
            }
            else {
                imageView.getLayoutParams().width = CommonUtils.dip2px(context, 200);
            }
        }

    }

}
