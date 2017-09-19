
package com.beisheng.synews.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beisheng.base.adapter.BSBaseAdapter;
import com.beisheng.base.utils.DateUtils;
import com.beisheng.base.utils.Options;
import com.beisheng.base.view.BSCircleImageView;
import com.beisheng.synews.activity.VideoFullActivity;
import com.beisheng.synews.mode.LiveVO;
import com.beisheng.synews.videoplay.MediaHelp;
import com.beisheng.synews.videoplay.VideoSuperPlayer;
import com.beisheng.synews.videoplay.VideoSuperPlayer.VideoPlayCallbackImpl;
import com.beisheng.synews.view.NineGridlayout;
import com.im.zhsy.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.Arrays;

public class LiveDetailAdapter extends BSBaseAdapter<LiveVO> {
    public boolean isPlaying;
    public int indexPostion = -1;

    public LiveDetailAdapter(Context context) {
        super(context);
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (mIsEmpty) {
            return super.getView(position, convertView, parent);
        }

        if (convertView != null && convertView.getTag() == null)
            convertView = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.live_detail_adapter, null);
            holder.titleTv = (TextView) convertView.findViewById(R.id.title_tv);
            holder.headIcon = (BSCircleImageView) convertView.findViewById(R.id.head_icon);
            holder.timeTv = (TextView) convertView.findViewById(R.id.time_tv);
            holder.detailTv = (TextView) convertView.findViewById(R.id.detail_tv);
            holder.photoLayout = (NineGridlayout) convertView.findViewById(R.id.photo_layout);
            holder.oneImg = (ImageView) convertView.findViewById(R.id.one_img);
            holder.videoLayout = (RelativeLayout) convertView.findViewById(R.id.video_layout);
            holder.mVideoViewLayout = (VideoSuperPlayer) convertView.findViewById(R.id.video);
            holder.mPlayBtnView = (ImageView) convertView.findViewById(R.id.play_btn);
            holder.videoImg = (ImageView) convertView.findViewById(R.id.video_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final LiveVO vo = (LiveVO) mList.get(position);
        holder.titleTv.setText(vo.getNickname());
        holder.detailTv.setText(vo.getContent());
        holder.timeTv.setText(DateUtils.parseMDHM(vo.getAddtime()));
        mImageLoader.displayImage(vo.getHeadpic(), holder.headIcon, Options.getOptionsHead(R.drawable.user_icon));

        if (vo.getFile() != null && vo.getFile().length > 1) {
            holder.photoLayout.setImagesData(Arrays.asList(vo.getFile()));
            holder.photoLayout.setVisibility(View.VISIBLE);
            holder.oneImg.setVisibility(View.GONE);
        } else if (vo.getFile() != null && vo.getFile().length == 1) {
            holder.photoLayout.setVisibility(View.GONE);
            holder.oneImg.setVisibility(View.VISIBLE);
            mImageLoader.displayImage(vo.getFile()[0], holder.oneImg, mOptions);
        } else {
            holder.photoLayout.setVisibility(View.GONE);
            holder.oneImg.setVisibility(View.GONE);
        }

        if (vo.getVideo() != null) {
            holder.videoLayout.setVisibility(View.VISIBLE);
            DisplayImageOptions videoOptions = Options.getOptionsDefaultIcon(R.drawable.video_def);
            mImageLoader.displayImage(vo.getVideo().get(0).getThumb(), holder.videoImg, videoOptions);
            holder.mPlayBtnView.setOnClickListener(new MyOnclick(holder.mPlayBtnView, holder.mVideoViewLayout, position));
            if (indexPostion == position) {
                holder.mVideoViewLayout.setVisibility(View.VISIBLE);
            } else {
                holder.mVideoViewLayout.setVisibility(View.GONE);
                holder.mVideoViewLayout.close();
            }
        } else {
            holder.videoLayout.setVisibility(View.GONE);
        }
        return convertView;
    }

    class MyOnclick implements OnClickListener {
        VideoSuperPlayer mSuperVideoPlayer;
        ImageView mPlayBtnView;
        int position;

        public MyOnclick(ImageView mPlayBtnView, VideoSuperPlayer mSuperVideoPlayer, int position) {
            this.position = position;
            this.mSuperVideoPlayer = mSuperVideoPlayer;
            this.mPlayBtnView = mPlayBtnView;
        }

        @Override
        public void onClick(View v) {
            MediaHelp.release();
            indexPostion = position;
            isPlaying = true;
            mSuperVideoPlayer.setVisibility(View.VISIBLE);
            mSuperVideoPlayer.loadAndPlay(MediaHelp.getInstance(), mList.get(position).getVideo().get(0).getUrl(), 0, false);
            mSuperVideoPlayer.setVideoPlayCallback(new MyVideoPlayCallback(mPlayBtnView, mSuperVideoPlayer, mList.get(position).getVideo().get(0).getUrl()));
            notifyDataSetChanged();
        }
    }

    class MyVideoPlayCallback implements VideoPlayCallbackImpl {
        ImageView mPlayBtnView;
        VideoSuperPlayer mSuperVideoPlayer;
        String mUrl;

        public MyVideoPlayCallback(ImageView mPlayBtnView, VideoSuperPlayer mSuperVideoPlayer, String url) {
            this.mPlayBtnView = mPlayBtnView;
            this.mUrl = url;
            this.mSuperVideoPlayer = mSuperVideoPlayer;
        }

        @Override
        public void onCloseVideo() {
            closeVideo();
        }

        @Override
        public void onSwitchPageType() {
            if (((Activity) mContext).getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
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
            mSuperVideoPlayer.close();
            MediaHelp.release();
            mPlayBtnView.setVisibility(View.VISIBLE);
            mSuperVideoPlayer.setVisibility(View.GONE);
        }

    }

    static class ViewHolder {
        private ImageView playImg, videoImg;
        private BSCircleImageView headIcon;
        private TextView titleTv, timeTv, detailTv;
        private FrameLayout showLayout;
        private NineGridlayout photoLayout;
        private RelativeLayout videoLayout;
        private VideoSuperPlayer mVideoViewLayout;
        private ImageView mPlayBtnView, oneImg;
    }

}
