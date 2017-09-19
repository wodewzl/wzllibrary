
package com.beisheng.synews.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.http.HttpClientUtil;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.DateUtils;
import com.beisheng.base.utils.Options;
import com.beisheng.synews.adapter.DiscussAdapter;
import com.beisheng.synews.application.AppApplication;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.fragment.BottomFragment;
import com.beisheng.synews.mode.NewsVO;
import com.beisheng.synews.utils.StartViewUitl;
import com.beisheng.synews.videoplay.MediaHelp;
import com.beisheng.synews.videoplay.VideoSuperPlayer;
import com.beisheng.synews.videoplay.VideoSuperPlayer.VideoPlayCallbackImpl;
import com.beisheng.synews.view.BSListViewConflict;
import com.google.gson.Gson;
import com.im.zhsy.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.HashMap;

@SuppressLint("SetJavaScriptEnabled")
public class NewsVideoDetailActivity extends BaseActivity implements OnClickListener, OnItemClickListener {
    private NewsVO mNewsVO;
    private ViewPager mViewPager;
    private BSListViewConflict mListView;
    private DiscussAdapter mAdapter;
    private String mId;
    private BottomFragment mBottomFragment;
    // 视频播放
    private ImageView mVideoImg;
    private boolean mIsPlaying;
    private ImageView mPlayBtnView;
    private VideoSuperPlayer mSuperVideoPlayer;
    private RelativeLayout mVideoLayout;

    private TextView mVideoTitleTv, mVidoeReadTv;
    private TextView mViewMoreTv;
    private LinearLayout mDisscussLayout;
    private TextView mCountTv;
    private ImageView mAdvertImg;
    private TextView mTimeTv;
    private String mNewType;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.news_video_detail_activity, mBaseContentLayout);
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void initView() {
        mBaseTitleTv.setText("视频详情");
        mPlayBtnView = (ImageView) findViewById(R.id.play_btn);
        mSuperVideoPlayer = (VideoSuperPlayer) findViewById(R.id.video);
        mVideoImg = (ImageView) findViewById(R.id.video_img);
        mVideoLayout = (RelativeLayout) findViewById(R.id.video_layout);
        mVideoTitleTv = (TextView) findViewById(R.id.video_title_tv);
        mVidoeReadTv = (TextView) findViewById(R.id.video_read_tv);
        mDisscussLayout = (LinearLayout) findViewById(R.id.disscuss_layout);
        mViewMoreTv = (TextView) findViewById(R.id.view_more_tv);
        mCountTv = (TextView) findViewById(R.id.count_tv);
        mAdvertImg = (ImageView) findViewById(R.id.advert_img);
        mListView = (BSListViewConflict) findViewById(R.id.list_view);
        mAdapter = new DiscussAdapter(this);
        // AnimationAdapter animationAdapter = new CardsAnimationAdapter(mAdapter);
        // animationAdapter.setAbsListView(mListView);
        mListView.setAdapter(mAdapter);
        mTimeTv = (TextView) findViewById(R.id.time_tv);
        initData();
    }

    public void initData() {
        mId = this.getIntent().getStringExtra("id");
        mNewType = this.getIntent().getStringExtra("new_type");

        // 获取本地数据，优化速度
        try {
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("uid", AppApplication.getInstance().getUid());
            map.put("contentid", mId);
            map.put("suburl", "4");// 视频详情
            map.put("new_type", mNewType);
            String oldStr = getCacheFromDatabase(Constant.NEWS_DETAIL_URL, map);
            mNewsVO = gson.fromJson(oldStr, NewsVO.class);
            if (mNewsVO != null && Constant.RESULT_SUCCESS_CODE.equals(mNewsVO.getCode())) {
                executeSuccess();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void bindViewsListener() {
        mPlayBtnView.setOnClickListener(new MyOnclick());
        mListView.setOnItemClickListener(this);
        mAdvertImg.setOnClickListener(this);
        mViewMoreTv.setOnClickListener(this);
    }

    public boolean getData() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("uid", AppApplication.getInstance().getUid());
            map.put("contentid", mId);
            map.put("suburl", "4");// 视频详情
            map.put("new_type", mNewType);

            if (hasNetWork()) {
                String jsonStr = HttpClientUtil.getRequest(this, Constant.DOMAIN_NAME + Constant.NEWS_DETAIL_URL, map);
                mNewsVO = gson.fromJson(jsonStr, NewsVO.class);
                saveJsonCache(Constant.NEWS_DETAIL_URL, map, jsonStr);
            } else {
                String oldStr = getCacheFromDatabase(Constant.NEWS_DETAIL_URL, map);
                mNewsVO = gson.fromJson(oldStr, NewsVO.class);
            }

            if (Constant.RESULT_SUCCESS_CODE.equals(mNewsVO.getCode())) {
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
        super.executeSuccess();

        mAdapter.updateData(mNewsVO.getReply());
        mVideoTitleTv.setText(mNewsVO.getTitle());
        mVidoeReadTv.setText(mNewsVO.getRead() + "次播放");
        mTimeTv.setText(DateUtils.parseMDHM(mNewsVO.getCreatime()));
        DisplayImageOptions options = Options.getOptionsDefaultIcon(R.drawable.video_def);
        mImageLoader.displayImage(mNewsVO.getThumb(), mVideoImg, options);

        // 评论
        if (mNewsVO.getReply() != null && mNewsVO.getReply().size() > 0) {
            mDisscussLayout.setVisibility(View.VISIBLE);
            mAdapter.updateData(mNewsVO.getReply());
            mCountTv.setText("评论 " + mNewsVO.getComments());
            if (BaseCommonUtils.parseInt(mNewsVO.getComments()) > 3)
                mViewMoreTv.setVisibility(View.VISIBLE);
        } else {
            mDisscussLayout.setVisibility(View.GONE);
        }

        // 广告
        if ("".equals(mNewsVO.getBanner().getThum()) || mNewsVO.getBanner().getThum() == null) {
            mAdvertImg.setVisibility(View.GONE);
        } else {
            mImageLoader.displayImage(mNewsVO.getBanner().getThum(), mAdvertImg, mOptions);
            mAdvertImg.setVisibility(View.VISIBLE);
        }
        try {
            // 底部View
            mBottomFragment = new BottomFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.bottom_layout, mBottomFragment);
            mBottomFragment.setNewsvo(mNewsVO);
            transaction.commit();
        } catch (Exception e) {
        }

    }

    @Override
    public void executeFailure() {
        super.executeFailure();
        mAdapter.updateData(mNewsVO.getReply());
        if (mNewsVO != null)
            showCustomToast(mNewsVO.getRetinfo());
        else
            showCustomToast("亲，请检查网络哦");
    }

    class MyOnclick implements OnClickListener, VideoPlayCallbackImpl {
        @Override
        public void onClick(View v) {
            MediaHelp.release();
            mIsPlaying = true;
            mSuperVideoPlayer.setVisibility(View.VISIBLE);
            mSuperVideoPlayer.loadAndPlay(MediaHelp.getInstance(), mNewsVO.getOuturl(), 0, false);
            mSuperVideoPlayer.setVideoPlayCallback(this);
        }

        @Override
        public void onCloseVideo() {
            closeVideo();
        }

        @Override
        public void onSwitchPageType() {
            if (NewsVideoDetailActivity.this.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                Intent intent = new Intent(NewsVideoDetailActivity.this, VideoFullActivity.class);
                intent.putExtra("url", mNewsVO.getOuturl());
                intent.putExtra("position", mSuperVideoPlayer.getCurrentPosition());
                NewsVideoDetailActivity.this.startActivityForResult(intent, 1);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.advert_img:
                StartViewUitl.startView(this, "0", mNewsVO.getBanner().getContentid(), mNewsVO.getBanner().getLink(), mNewsVO.getGovermentid(), mNewsVO.getTypename());
                break;

            case R.id.view_more_tv:
                Bundle bundle = new Bundle();
                bundle.putString("id", mNewsVO.getContentid());
                bundle.putString("type", mNewsVO.getSuburl());
                bundle.putString("title", mNewsVO.getTitle());
                openActivity(DisscussActivity.class, bundle, 0);
                break;

            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        NewsVO vo = (NewsVO) arg0.getAdapter().getItem(arg2);
        if (vo == null)
            return;
        mBottomFragment.commitSecondDisscuss(vo);
    }

    public void stopVideo() {
        mSuperVideoPlayer.close();
        MediaHelp.release();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopVideo();
    }
}
