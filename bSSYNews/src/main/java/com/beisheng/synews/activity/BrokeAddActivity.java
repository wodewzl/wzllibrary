package com.beisheng.synews.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.adapter.UploadAdapter;
import com.beisheng.base.imgselector.ImageActivityUtils;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.BitmapUtil;
import com.beisheng.synews.application.AppApplication;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.interfaces.RecordResult;
import com.beisheng.synews.videoplay.MediaHelp;
import com.beisheng.synews.videoplay.VideoSuperPlayer;
import com.beisheng.synews.videoplay.VideoSuperPlayer.VideoPlayCallbackImpl;
import com.beisheng.synews.view.MyRecorder;
import com.im.zhsy.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.File;

public class BrokeAddActivity extends BaseActivity implements OnItemClickListener, OnClickListener, OnTouchListener {
	private static int MAX_TIME = 0; // 最长录制时间，单位秒，0为无时间限制
	private static int MIX_TIME = 1; // 最短录制时间，单位秒，0为无时间限制，建议设为1
	private static int RESULT_VIDEO = 3; // 最短录制时间，单位秒，0为无时间限制，建议设为1
	private static int RECORD_NO = 0; // 不在录音
	private static int RECORD_ING = 1; // 正在录音
	private static int RECODE_ED = 2; // 完成录音
	private int RECODE_STATE = 0; // 录音的状态

	private GridView mGrideviewUpload;
	private UploadAdapter mAdapter;
	private EditText mContentEt, mPhoneEt;
	private boolean mCommitFlag = true;
	private String mLid;
	private String mFile;
	private ImageView mVideoBg;
	private boolean mIsPlaying;
	private ImageView mPlayBtnView;
	private VideoSuperPlayer mSuperVideoPlayer;
	private RelativeLayout mVideoLayout;
	private ImageView mPhotoImg, mVoiceImg, mVideoImg, mVoicePlayImg;
	private TextView mTitleEt;

	// 录音
	private Dialog dialog;
	private MediaPlayer media;
	private MyRecorder recorder;
	private ImageButton dialog_image;
	private static float recodeTime = 0.0f; // 录音的时间
	private static double voiceValue = 0.0; // 麦克风获取的音量值
	/** 更新时间的线程 */
	private Thread timeThread;
	private static boolean playState = false; // 播放状态

	@Override
	public void baseSetContentView() {
		View.inflate(this, R.layout.broke_add_activity, mBaseContentLayout);
	}

	@Override
	public boolean getDataResult() {
		return true;
	}

	@Override
	public void initView() {
		mBaseTitleTv.setText("添加爆料");
		mBaseOkTv.setText("发布");
		mTitleEt = (TextView) findViewById(R.id.title_et);
		mPhoneEt = (EditText) findViewById(R.id.phone_et);
		mContentEt = (EditText) findViewById(R.id.content_et);
		mGrideviewUpload = (GridView) findViewById(R.id.grideview_upload);
		mAdapter = new UploadAdapter(this);
		mGrideviewUpload.setAdapter(mAdapter);
		mPlayBtnView = (ImageView) findViewById(R.id.play_btn);
		mSuperVideoPlayer = (VideoSuperPlayer) findViewById(R.id.video);
		mVideoBg = (ImageView) findViewById(R.id.video_bg);
		mVideoLayout = (RelativeLayout) findViewById(R.id.video_layout);
		mPhotoImg = (ImageView) findViewById(R.id.photo_img);
		mVoiceImg = (ImageView) findViewById(R.id.voice_img);
		mVideoImg = (ImageView) findViewById(R.id.video_img);
		mVoicePlayImg = (ImageView) findViewById(R.id.voice_play_img);
		initData();
	}

	@Override
	public void bindViewsListener() {
		mGrideviewUpload.setOnItemClickListener(this);
		mBaseOkTv.setOnClickListener(this);
		mPlayBtnView.setOnClickListener(new MyOnclick());
		mPhotoImg.setOnClickListener(this);
		mVideoImg.setOnClickListener(this);
		mVoiceImg.setOnTouchListener(this);
		mVoicePlayImg.setOnClickListener(this);
	}

	public void initData() {
		Intent intent = this.getIntent();
		mLid = intent.getStringExtra("id");
		if (intent.getStringExtra("path") != null) {
			mFile = intent.getStringExtra("path");
			mVideoLayout.setVisibility(View.VISIBLE);
			mVideoBg.setImageBitmap(getVideoThumbnail(mFile, BaseCommonUtils.getScreenWid(this), BaseCommonUtils.dip2px(this, 180), ThumbnailUtils.OPTIONS_RECYCLE_INPUT));
		}
	}

	private Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
		Bitmap bitmap = null;
		// 获取视频的缩略图
		bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
		if (bitmap != null) {
			bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		} else {
			bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.photo_def);
		}
		return bitmap;
	}

	public void commit() {
		mCommitFlag = false;
		showProgressDialog();
		RequestParams params = new RequestParams();
		try {
			params.put("title", mTitleEt.getText().toString());
			params.put("mobile", mPhoneEt.getText().toString());
			params.put("content", mContentEt.getText().toString());
			params.put("anonymous", "2");
			params.put("uid", AppApplication.getInstance().getUid());
			params.put("sessionid", AppApplication.getInstance().getSessionid());

			for (int i = 0; i < mAdapter.mPicList.size(); i++) {
				// File file = BaseCommonUtils.bitmapCompress(this,
				// mAdapter.mPicList.get(i));
				File file = BitmapUtil.bitmapToFile(mAdapter.mPicList.get(i));
				// File file = new File(mAdapter.mPicList.get(i));
				params.put("img" + i, file);
			}

			if (mFile != null) {
				File videoFile = new File(mFile);
				if (videoFile.exists())
					params.put("vodeo", videoFile);
			}

			// File voiceFile = new
			// File(Environment.getExternalStorageDirectory(),
			// "myvoice/voice.amr");
			//
			// if (voiceFile.exists())
			// params.put("voice", voiceFile);

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		String url = Constant.DOMAIN_NAME + Constant.BROKE_ADD;
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				mCommitFlag = true;
				dismissProgressDialog();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				String result = new String(arg2);
				dismissProgressDialog();
				mCommitFlag = true;
				try {
					JSONObject jsonObject = new JSONObject(new String(arg2));
					String str = (String) jsonObject.get("retinfo");
					String code = (String) jsonObject.get("code");
					if (Constant.RESULT_SUCCESS_CODE.equals(code)) {
						showCustomToast(str);
						mBaseContentLayout.postDelayed(new Runnable() {

							@Override
							public void run() {
								BrokeAddActivity.this.finish();
							}
						}, 1000);

					} else {
						showCustomToast(str);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		ImageActivityUtils.setImageForActivity(this, mAdapter, (int) arg2);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (intent == null)
			return;
		switch (requestCode) {
		case ImageActivityUtils.REQUEST_IMAGE_BYSDCARD:
			if (resultCode == RESULT_OK) {
				ImageActivityUtils.imgPreviewDelteActivity(intent, mAdapter);
				mGrideviewUpload.setVisibility(View.VISIBLE);
			}
			break;

		case ImageActivityUtils.REQUEST_IMAGE:
			if (resultCode == RESULT_OK) {
				ImageActivityUtils.setImageGetActivity(intent, mAdapter);
				mGrideviewUpload.setVisibility(View.VISIBLE);
			}
			break;

		case 3:
			if (intent.getStringExtra("path") != null) {
				mFile = intent.getStringExtra("path");
				mVideoLayout.setVisibility(View.VISIBLE);
				mVideoBg.setImageBitmap(getVideoThumbnail(mFile, BaseCommonUtils.getScreenWid(this), BaseCommonUtils.dip2px(this, 180), ThumbnailUtils.OPTIONS_RECYCLE_INPUT));
			}
			break;

		case 1:
			MediaHelp.getInstance().seekTo(intent.getIntExtra("position", 0));
			break;

		default:
			break;
		}
	}

	RecordResult recordResult = new RecordResult() {

		@Override
		public void getRecordResult(String result) {
			mContentEt.setText(mContentEt.getText() + result);
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ImageActivityUtils.crashBoBitmap(mAdapter);
	}

	@Override
	public void onClick(View v) {
		Bundle bundle = new Bundle();
		switch (v.getId()) {
		case R.id.base_ok_tv:
			if (mTitleEt.getText().toString().trim().length() == 0) {
				showCustomToast("标题不能为空");
				return;
			}

			if (mPhoneEt.getText().toString().trim().length() == 0) {
				showCustomToast("爆料电话必填");
				return;
			}
			if (mCommitFlag)
				commit();
			break;

		case R.id.photo_img:
			ImageActivityUtils.setImageForActivity(this, mAdapter, mAdapter.mList.size());
			break;
		case R.id.voice_play_img:
			// 如果不是正在播放
			if (!playState) {
				// 实例化MediaPlayer对象
				media = new MediaPlayer();
				File file = new File(Environment.getExternalStorageDirectory(), "myvoice/voice.amr");
				// File file = new File(FileUtil.getSaveFilePath(this) +
				// "voice.amr");
				try {
					// 设置播放资源
					media.setDataSource(file.getAbsolutePath());
					// 准备播放
					media.prepare();
					// 开始播放
					media.start();
					// 改变播放的标志位
					playState = true;
					// 设置播放结束时监听
					media.setOnCompletionListener(new OnCompletionListener() {

						@Override
						public void onCompletion(MediaPlayer mp) {
							if (playState) {
								playState = false;
							}
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				// 如果正在播放
				if (media.isPlaying()) {
					media.stop();
					playState = false;
				} else {
					playState = false;
				}

			}

			break;
		case R.id.video_img:
			openActivity(LiveRecordActivity.class, bundle, 3);
			break;

		default:
			break;
		}
	}

	class MyOnclick implements OnClickListener, VideoPlayCallbackImpl {
		@Override
		public void onClick(View v) {
			MediaHelp.release();
			mIsPlaying = true;
			mSuperVideoPlayer.setVisibility(View.VISIBLE);
			mSuperVideoPlayer.loadAndPlay(MediaHelp.getInstance(), Uri.fromFile(new File(mFile)).toString(), 0, false);
			mSuperVideoPlayer.setVideoPlayCallback(this);
		}

		@Override
		public void onCloseVideo() {
			closeVideo();
		}

		@Override
		public void onSwitchPageType() {
			if (BrokeAddActivity.this.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
				Intent intent = new Intent(BrokeAddActivity.this, VideoFullActivity.class);
				intent.putExtra("url", Uri.fromFile(new File(mFile)).toString());
				intent.putExtra("position", mSuperVideoPlayer.getCurrentPosition());
				BrokeAddActivity.this.startActivityForResult(intent, 1);
			}
		}

		@Override
		public void onPlayFinish() {
			closeVideo();
		}

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:// 按下
			// 如果当前不是正在录音状态，开始录音
			if (RECODE_STATE != RECORD_ING) {
				recorder = new MyRecorder("voice");
				RECODE_STATE = RECORD_ING;
				// 显示录音情况
				showVoiceDialog();
				// 开始录音
				recorder.start();
				// timeText.setVisibility(View.VISIBLE);
				// bar.setVisibility(View.GONE);
				// 计时线程
				myThread();
			}
			break;

		case MotionEvent.ACTION_UP:// 离开
			// 如果是正在录音
			if (RECODE_STATE == RECORD_ING) {
				RECODE_STATE = RECODE_ED;
				// 如果录音图标正在显示,关闭
				if (dialog.isShowing()) {
					dialog.dismiss();
				}

				// 停止录音
				recorder.stop();
				voiceValue = 0.0;

				if (recodeTime < MIX_TIME) {
					showCustomToast("请按住不放录音");
					// recordBt.setText("按住录音");
					RECODE_STATE = RECORD_NO;
				} else {
					// recordBt.setText("按住录音");
					MediaPlayer player = new MediaPlayer();
					File file = new File(Environment.getExternalStorageDirectory(), "myvoice/voice.amr");
					mVoicePlayImg.setVisibility(View.VISIBLE);
					// File file = new File(FileUtil.getSaveFilePath(this) +
					// "voice.amr");
					// File file = new File(FileUtil.getSaveFilePath(this) +
					// "voice.amr");
					try {
						player.setDataSource(file.getAbsolutePath());
					} catch (Exception e) {
						e.printStackTrace();
					}
					// timeText.setText("录音时间：" + ((int) recodeTime));
				}
			}
			break;

		}
		return true;

	}

	/** 显示正在录音的图标 */
	private void showVoiceDialog() {
		dialog = new Dialog(BrokeAddActivity.this, R.style.DialogStyle);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		dialog.setContentView(R.layout.talk_layout);
		dialog_image = (ImageButton) dialog.findViewById(R.id.talk_log);
		dialog.show();
	}

	/** 录音计时线程 */
	private void myThread() {
		timeThread = new Thread(ImageThread);
		timeThread.start();
	}

	/** 录音线程 */
	private Runnable ImageThread = new Runnable() {

		@Override
		public void run() {
			recodeTime = 0.0f;
			// 如果是正在录音状态
			while (RECODE_STATE == RECORD_ING) {
				if (recodeTime >= MAX_TIME && MAX_TIME != 0) {
					handler.sendEmptyMessage(0x10);
				} else {
					try {
						Thread.sleep(200);

						recodeTime += 0.2;
						if (RECODE_STATE == RECORD_ING) {
							voiceValue = recorder.getAmplitude();
							handler.sendEmptyMessage(0x11);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}

		}

		Handler handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case 0x10:
					// 录音超过15秒自动停止,录音状态设为语音完成
					if (RECODE_STATE == RECORD_ING) {
						RECODE_STATE = RECODE_ED;
						// 如果录音图标正在显示的话,关闭显示图标
						if (dialog.isShowing()) {
							dialog.dismiss();
						}

						// 停止录音
						recorder.stop();
						voiceValue = 0.0;

						// 如果录音时长小于1秒，显示录音失败的图标
						if (recodeTime < 1.0) {
							showCustomToast("请按住不放录音");
							// timeText.setText("");
							// recordBt.setText("按住录音");
							RECODE_STATE = RECORD_NO;
						} else {
							// recordBt.setText("按住录音");
							// timeText.setText("录音时间:" + ((int) recodeTime));
						}
					}
					break;

				case 0x11:
					// timeText.setText("");
					// recordBt.setText("正在录音");
					setDialogImage();
					break;
				}
			};
		};
	};

	// 录音Dialog图片随声音大小切换
	void setDialogImage() {

		if (voiceValue < 200.0) {
			dialog_image.setImageResource(R.drawable.record_animate_01);
		} else if (voiceValue > 200.0 && voiceValue < 400) {
			dialog_image.setImageResource(R.drawable.record_animate_02);
		} else if (voiceValue > 400.0 && voiceValue < 800) {
			dialog_image.setImageResource(R.drawable.record_animate_03);
		} else if (voiceValue > 800.0 && voiceValue < 1600) {
			dialog_image.setImageResource(R.drawable.record_animate_04);
		} else if (voiceValue > 1600.0 && voiceValue < 3200) {
			dialog_image.setImageResource(R.drawable.record_animate_05);
		} else if (voiceValue > 3200.0 && voiceValue < 5000) {
			dialog_image.setImageResource(R.drawable.record_animate_06);
		} else if (voiceValue > 5000.0 && voiceValue < 7000) {
			dialog_image.setImageResource(R.drawable.record_animate_07);
		} else if (voiceValue > 7000.0 && voiceValue < 10000.0) {
			dialog_image.setImageResource(R.drawable.record_animate_08);
		} else if (voiceValue > 10000.0 && voiceValue < 14000.0) {
			dialog_image.setImageResource(R.drawable.record_animate_09);
		} else if (voiceValue > 14000.0 && voiceValue < 17000.0) {
			dialog_image.setImageResource(R.drawable.record_animate_10);
		} else if (voiceValue > 17000.0 && voiceValue < 20000.0) {
			dialog_image.setImageResource(R.drawable.record_animate_11);
		} else if (voiceValue > 20000.0 && voiceValue < 24000.0) {
			dialog_image.setImageResource(R.drawable.record_animate_12);
		} else if (voiceValue > 24000.0 && voiceValue < 28000.0) {
			dialog_image.setImageResource(R.drawable.record_animate_13);
		} else if (voiceValue > 28000.0) {
			dialog_image.setImageResource(R.drawable.record_animate_14);
		}
	}

	private void closeVideo() {
		mIsPlaying = false;
		mSuperVideoPlayer.close();
		MediaHelp.release();
		mPlayBtnView.setVisibility(View.VISIBLE);
		mSuperVideoPlayer.setVisibility(View.GONE);
	}

	@Override
	public void onPause() {
		super.onPause();
		closeVideo();
	}
}
