
package com.beisheng.synews.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Toast;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.synews.videoplay.MovieRecorderView;
import com.beisheng.synews.videoplay.MovieRecorderView.OnRecordFinishListener;
import com.im.zhsy.R;

@SuppressLint("NewApi")
public class LiveRecordActivity extends BaseActivity {

    private MovieRecorderView mRecorderView;// 视频录制控件
    private Button mShootBtn;// 视频开始录制按钮
    private boolean isFinish = true;
    private boolean success = false;// 防止录制完成后出现多次跳转事件
    private String mLid;

    @Override
    public void baseSetContentView() {
        // mBaseHeadLayout.setVisibility(View.GONE);
        View.inflate(this, R.layout.live_record_activity, mBaseContentLayout);
    }

    @Override
    public void initView() {
        mBaseTitleTv.setText("录制视频");
        mRecorderView = (MovieRecorderView) findViewById(R.id.movieRecorderView);
        mShootBtn = (Button) findViewById(R.id.shoot_button);
        mShootBtn.setBackground(BaseCommonUtils.setBackgroundShap(this, 100, R.color.sy_title_color, R.color.sy_title_color));
        initData();
    }

    public void initData() {
        mLid = this.getIntent().getStringExtra("id");
    }

    @Override
    public void bindViewsListener() {
        // 用户长按事件监听
        mShootBtn.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {// 用户按下拍摄按钮
                    // mShootBtn.setBackgroundResource(R.drawable.bg_movie_add_shoot_select);
                    mRecorderView.record(new OnRecordFinishListener() {

                        @Override
                        public void onRecordFinish() {
                            if (!success && mRecorderView.getTimeCount() < 10) {// 判断用户按下时间是否大于10秒
                                success = true;
                                handler.sendEmptyMessage(1);
                            }
                        }
                    });
                } else if (event.getAction() == MotionEvent.ACTION_UP) {// 用户抬起拍摄按钮
                    // mShootBtn.setBackgroundResource(R.drawable.bg_movie_add_shoot);
                    if (mRecorderView.getTimeCount() > 3) {// 判断用户按下时间是否大于3秒
                        if (!success) {
                            success = true;
                            handler.sendEmptyMessage(1);
                        }
                    } else {
                        success = false;
                        if (mRecorderView.getmVecordFile() != null)
                            mRecorderView.getmVecordFile().delete();// 删除录制的过短视频
                        mRecorderView.stop();// 停止录制
                        Toast.makeText(LiveRecordActivity.this, "视频录制时间太短", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        // WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean execute() {
        return super.execute();
    }

    @Override
    public boolean getDataResult() {
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        isFinish = true;
        if (mRecorderView.getmVecordFile() != null)
            mRecorderView.getmVecordFile().delete();// 视频使用后删除
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        isFinish = false;
        success = false;
        mRecorderView.stop();// 停止录制
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (success) {
                finishActivity();
            }
        }
    };

    // 视频录制结束后，跳转的函数
    private void finishActivity() {
        if (isFinish) {
            mRecorderView.stop();
            Intent intent = new Intent();
            intent.putExtra("id", mLid);
            intent.putExtra("path", mRecorderView.getmVecordFile().toString());
            if (mLid == null) {
                // intent.setClass(this, BrokeAddActivity.class);
                setResult(3, intent);
            } else {
                intent.setClass(this, LiveAddActivity.class);
                startActivity(intent);
            }

            this.finish();
        }
        success = false;
    }

    /**
     * 录制完成回调
     */
    public interface OnShootCompletionListener {
        public void OnShootSuccess(String path, int second);

        public void OnShootFailure();
    }

}
