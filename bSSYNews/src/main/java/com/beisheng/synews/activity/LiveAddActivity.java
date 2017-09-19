
package com.beisheng.synews.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.adapter.UploadAdapter;
import com.beisheng.base.imgselector.ImageActivityUtils;
import com.beisheng.base.utils.BitmapUtil;
import com.beisheng.synews.application.AppApplication;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.interfaces.RecordResult;
import com.beisheng.synews.videoplay.MediaHelp;
import com.beisheng.synews.videoplay.VideoSuperPlayer;
import com.beisheng.synews.videoplay.VideoSuperPlayer.VideoPlayCallbackImpl;
import com.im.zhsy.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.File;

public class LiveAddActivity extends BaseActivity implements OnItemClickListener, OnClickListener {
    private GridView mGrideviewUpload;
    private UploadAdapter mAdapter;
    private EditText mContentEt;
    private boolean mCommitFlag = true;
    private String mLid;
    private String mFile;
    private ImageView mVideoBg;
    private boolean mIsPlaying;
    private ImageView mPlayBtnView;
    private VideoSuperPlayer mSuperVideoPlayer;
    private RelativeLayout mVideoLayout;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.live_add_activity, mBaseContentLayout);
    }

    @Override
    public boolean getDataResult() {
        return true;
    }

    @Override
    public void initView() {
        mBaseTitleTv.setText("添加直播间");
        mBaseOkTv.setText("发布");
        mContentEt = (EditText) findViewById(R.id.content_et);
        mGrideviewUpload = (GridView) findViewById(R.id.grideview_upload);
        mAdapter = new UploadAdapter(this);
        mGrideviewUpload.setAdapter(mAdapter);
        mPlayBtnView = (ImageView) findViewById(R.id.play_btn);
        mSuperVideoPlayer = (VideoSuperPlayer) findViewById(R.id.video);
        mVideoBg = (ImageView) findViewById(R.id.video_bg);
        mVideoLayout = (RelativeLayout) findViewById(R.id.video_layout);
        initData();
    }

    @Override
    public void bindViewsListener() {
        mGrideviewUpload.setOnItemClickListener(this);
        mBaseOkTv.setOnClickListener(this);
        mPlayBtnView.setOnClickListener(new MyOnclick());
    }

    public void initData() {
        Intent intent = this.getIntent();
        mLid = intent.getStringExtra("id");
        if (intent.getStringExtra("path") != null) {
            mFile = intent.getStringExtra("path");
            mVideoLayout.setVisibility(View.VISIBLE);
            mGrideviewUpload.setVisibility(View.GONE);
            // mVideoBg.setImageBitmap(getVideoThumbnail(mFile, BaseCommonUtils.getScreenWid(this),
            // BaseCommonUtils.dip2px(this, 180), ThumbnailUtils.OPTIONS_RECYCLE_INPUT));
        } else {
            mVideoLayout.setVisibility(View.GONE);
            mGrideviewUpload.setVisibility(View.VISIBLE);
        }
    }

    private Bitmap getVideoThumbnail(String videoPath, int width, int height,
            int kind) {
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
        RequestParams params = new RequestParams();
        try {
            params.put("content", mContentEt.getText().toString());
            params.put("uid", AppApplication.getInstance().getUid());
            params.put("sessionid", AppApplication.getInstance().getSessionid());
            params.put("liveid", mLid);

            for (int i = 0; i < mAdapter.mPicList.size(); i++) {
                // File file = BaseCommonUtils.compressPicture(this, mAdapter.mPicList.get(i));
                File file = BitmapUtil.bitmapToFile(mAdapter.mPicList.get(i));
                // File file = new File(mAdapter.mPicList.get(i));
                params.put("img" + i, file);
            }

            File vdeoFile = new File(mFile);
            params.put("vodeo", vdeoFile);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        String url = Constant.DOMAIN_NAME + Constant.LIVE_ADD_URL;
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
                                LiveAddActivity.this.finish();
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
        switch (requestCode) {
            case ImageActivityUtils.REQUEST_IMAGE_BYSDCARD:
                if (resultCode == RESULT_OK) {
                    ImageActivityUtils.imgPreviewDelteActivity(intent, mAdapter);
                }
                break;

            case ImageActivityUtils.REQUEST_IMAGE:
                if (resultCode == RESULT_OK) {
                    ImageActivityUtils.setImageGetActivity(intent, mAdapter);
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

        switch (v.getId()) {
            case R.id.base_ok_tv:
                if (mContentEt.getText().toString().trim().length() == 0) {
                    showCustomToast("标题不能为空");
                    return;
                }
                if (mCommitFlag) {
                    showProgressDialog();
                    commit();
                }

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
            if (LiveAddActivity.this.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                Intent intent = new Intent(LiveAddActivity.this, VideoFullActivity.class);
                intent.putExtra("url", Uri.fromFile(new File(mFile)).toString());
                intent.putExtra("position", mSuperVideoPlayer.getCurrentPosition());
                LiveAddActivity.this.startActivityForResult(intent, 1);
            }
        }

        @Override
        public void onPlayFinish() {
            closeVideo();
        }

        private void closeVideo() {
            mIsPlaying = false;
            mSuperVideoPlayer.close();
            MediaHelp.release();
            mPlayBtnView.setVisibility(View.VISIBLE);
            mSuperVideoPlayer.setVisibility(View.GONE);
        }
    }

}
