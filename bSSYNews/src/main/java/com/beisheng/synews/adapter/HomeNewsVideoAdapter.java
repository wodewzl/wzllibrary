package com.beisheng.synews.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.adapter.BSBaseAdapter;
import com.beisheng.base.utils.DateUtils;
import com.beisheng.base.utils.JsonUtil;
import com.beisheng.base.utils.Options;
import com.beisheng.synews.activity.DisscussActivity;
import com.beisheng.synews.activity.VideoFullActivity;
import com.beisheng.synews.mode.CacheListVO;
import com.beisheng.synews.mode.NewsVO;
import com.beisheng.synews.utils.PointsAddUtil;
import com.beisheng.synews.utils.ShareUtil;
import com.beisheng.synews.videoplay.MediaHelp;
import com.beisheng.synews.videoplay.VideoSuperPlayer;
import com.beisheng.synews.videoplay.VideoSuperPlayer.VideoPlayCallbackImpl;
import com.im.zhsy.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

public class HomeNewsVideoAdapter extends BSBaseAdapter<NewsVO> {
    public int indexPostion = -1;
    public boolean isPlaying = false;
    public VideoSuperPlayer mCurrentVideoSuperPlayer;

    public HomeNewsVideoAdapter(Context context) {
        super(context);
        mOptions = Options.getOptionsDefaultIcon(R.drawable.video_def);
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (mIsEmpty) {
            return super.getView(position, convertView, parent);
        }

        if (convertView != null && convertView.getTag() == null)
            convertView = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.home_news_video_adapter, null);
            holder.titleTv = (TextView) convertView.findViewById(R.id.title_tv);
            holder.countTv = (TextView) convertView.findViewById(R.id.count_tv);
            holder.shareImg = (ImageView) convertView.findViewById(R.id.share_img);
            holder.favorImg = (ImageView) convertView.findViewById(R.id.favor_img);
            holder.commentTv = (TextView) convertView.findViewById(R.id.comment_tv);
            holder.mPlayBtnView = (ImageView) convertView.findViewById(R.id.play_btn);
            holder.videoImg = (ImageView) convertView.findViewById(R.id.video_img);
            holder.VideoSuperPlayer = (VideoSuperPlayer) convertView.findViewById(R.id.video);
            holder.timeTv = (TextView) convertView.findViewById(R.id.time_tv);
            holder.mMediaPlayer = new MediaPlayer();
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final NewsVO vo = (NewsVO) mList.get(position);
        holder.titleTv.setText(vo.getTitle());
        holder.timeTv.setText(DateUtils.parseMDHM(vo.getCreatime()));
        holder.countTv.setText(vo.getRead() + "次播放");
        if (vo.getComments() == null) {
            holder.commentTv.setVisibility(View.GONE);
        } else {
            holder.commentTv.setText(vo.getComments());
            holder.commentTv.setVisibility(View.VISIBLE);
        }

        mImageLoader.displayImage(vo.getThumb(), holder.videoImg, mOptions);
        holder.mPlayBtnView.setOnClickListener(new MyOnclick(holder.mPlayBtnView, holder
                .VideoSuperPlayer, position, holder.mMediaPlayer));

        if (indexPostion == position) {
            holder.VideoSuperPlayer.setVisibility(View.VISIBLE);
        } else {
            holder.VideoSuperPlayer.setVisibility(View.GONE);
            // holder.VideoSuperPlayer.pausePlay();
        }

        holder.favorImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                BaseActivity baseActivity = (BaseActivity) mContext;
                CacheListVO cacheVo = new CacheListVO();
                cacheVo.setContentid(vo.getContentid());
                cacheVo.setSuburl(vo.getSuburl());
                cacheVo.setTitle(vo.getTitle());
                cacheVo.setCreatime(vo.getCreatime());
                String jsonStr = JsonUtil.toJson(cacheVo);
                baseActivity.saveJsonCache("favor_" + cacheVo.getContentid(), null, jsonStr);
                baseActivity.showCustomToast("收藏成功");
            }
        });

        holder.shareImg.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                BaseActivity activity = (BaseActivity) mContext;
                ShareUtil.share(activity, vo.getShare_img(), vo.getShare_tit(), vo.getShare_des()
                        , vo.getShare_url());
                PointsAddUtil.commitdPoints("4", vo.getContentid(), vo.getTitle());
            }
        });

        holder.commentTv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Bundle bundle = new Bundle();
                bundle.putString("id", vo.getContentid());
                bundle.putString("type", vo.getSuburl());
                bundle.putString("title", vo.getTitle());
                BaseActivity activity = (BaseActivity) mContext;
                activity.openActivity(DisscussActivity.class, bundle, 0);
            }
        });

        return convertView;
    }

    class MyOnclick implements OnClickListener {
        VideoSuperPlayer mSuperVideoPlayer;
        ImageView mPlayBtnView;
        int position;
        private MediaPlayer mMediaPlayer;

        public MyOnclick(ImageView mPlayBtnView, VideoSuperPlayer mSuperVideoPlayer, int
                position, MediaPlayer mediaPlayer) {
            this.position = position;
            this.mSuperVideoPlayer = mSuperVideoPlayer;
            this.mPlayBtnView = mPlayBtnView;
            this.mMediaPlayer = mediaPlayer;
        }

        @Override
        public void onClick(View v) {
            if (mCurrentVideoSuperPlayer != null && isPlaying) {
                mList.get(indexPostion).setVideoPosition(mCurrentVideoSuperPlayer
                        .getCurrentPosition());
                mCurrentVideoSuperPlayer.setVisibility(View.GONE);
            }

            MediaHelp.release();
            indexPostion = position;
            isPlaying = true;
            mSuperVideoPlayer.setVisibility(View.VISIBLE);
            mSuperVideoPlayer.loadAndPlay(MediaHelp.getInstance(), mList.get(position).getOuturl
                    (), mList.get(position).getVideoPosition(), false);
            mSuperVideoPlayer.setVideoPlayCallback(new MyVideoPlayCallback(mPlayBtnView,
                    mSuperVideoPlayer, position, mList.get(position).getOuturl()));
            mCurrentVideoSuperPlayer = mSuperVideoPlayer;
            notifyDataSetChanged();
            commit(mList.get(position).getContentid());

        }
    }

    class MyVideoPlayCallback implements VideoPlayCallbackImpl {
        ImageView mPlayBtnView;
        VideoSuperPlayer mSuperVideoPlayer;
        String mUrl;
        int mPosition;

        public MyVideoPlayCallback(ImageView mPlayBtnView, VideoSuperPlayer mSuperVideoPlayer,
                                   int position, String url) {
            this.mPlayBtnView = mPlayBtnView;
            this.mUrl = url;
            this.mSuperVideoPlayer = mSuperVideoPlayer;
            this.mPosition = position;
        }

        @Override
        public void onCloseVideo() {
            closeVideo();
        }

        @Override
        public void onSwitchPageType() {
            if (((Activity) mContext).getRequestedOrientation() == ActivityInfo
                    .SCREEN_ORIENTATION_PORTRAIT) {
                Intent intent = new Intent(mContext, VideoFullActivity.class);
                intent.putExtra("url", mUrl);
                intent.putExtra("position", mSuperVideoPlayer.getCurrentPosition());
                ((Activity) mContext).startActivityForResult(intent, 1);
            }
        }

        @Override
        public void onPlayFinish() {
            closeVideo();
        }

        private void closeVideo() {
            isPlaying = false;
            indexPostion = -1;
            mSuperVideoPlayer.pausePlay();
            mList.get(mPosition).setVideoPosition(100);
//			mPlayBtnView.setVisibility(View.VISIBLE);
            mSuperVideoPlayer.setVisibility(View.GONE);
        }

    }

    static class ViewHolder {
        private ImageView videoImg, shareImg, favorImg;
        private TextView titleTv, commentTv, countTv, timeTv;
        private VideoSuperPlayer VideoSuperPlayer;
        private ImageView mPlayBtnView;
        private MediaPlayer mMediaPlayer;
    }

    public void commit(String contentid) {
        RequestParams params = new RequestParams();
        String url = "http://app.10yan.com.cn/appapi/index.php?c=indexv4&a=newsPv&contentid=" +
                contentid;
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
            }
        });
    }
}
