package com.beisheng.synews.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.adapter.BSBaseAdapter;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.DateUtils;
import com.beisheng.base.utils.Options;
import com.beisheng.base.view.BSBadgeView;
import com.beisheng.synews.activity.DisscussActivity;
import com.beisheng.synews.activity.VideoFullActivity;
import com.beisheng.synews.mode.NewsVO;
import com.beisheng.synews.utils.LogUtil;
import com.beisheng.synews.videoplay.MediaHelp;
import com.beisheng.synews.videoplay.VideoSuperPlayer;
import com.beisheng.synews.videoplay.VideoSuperPlayer.VideoPlayCallbackImpl;
import com.im.zhsy.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.Date;

public class HomeNewsFragmentAdapter extends BSBaseAdapter<NewsVO> {
	public boolean isPlaying;
	public int indexPostion = -1;
	private String status = "0";// 0为其它，1我i猜你喜欢

	public HomeNewsFragmentAdapter(Context context) {
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
			convertView = View.inflate(mContext, R.layout.home_news_fragment_adapter, null);
			holder.img = (ImageView) convertView.findViewById(R.id.img);

			holder.smallImgbadge = new BSBadgeView(mContext, holder.img);
			holder.smallImgLayout = (LinearLayout) convertView.findViewById(R.id.small_img_layout);
			holder.largeImg = (ImageView) convertView.findViewById(R.id.large_img);
			holder.largeImgbadge = new BSBadgeView(mContext, holder.largeImg);
			holder.tileTv = (TextView) convertView.findViewById(R.id.title_tv);
			holder.agreeCommentLayout = (LinearLayout) convertView.findViewById(R.id.agree_comment_layout);
			holder.agreeTv = (TextView) convertView.findViewById(R.id.agree_tv);
			holder.readLayout = (LinearLayout) convertView.findViewById(R.id.read_layout);
			holder.commentTv = (TextView) convertView.findViewById(R.id.comment_tv);
			holder.timeTv = (TextView) convertView.findViewById(R.id.time_tv);
			holder.zhengWuBadge = new BSBadgeView(mContext, holder.timeTv);

			// 视频部分
			holder.mPlayBtnView = (ImageView) convertView.findViewById(R.id.play_btn);
			holder.videoImg = (ImageView) convertView.findViewById(R.id.video_img);
			holder.VideoSuperPlayer = (VideoSuperPlayer) convertView.findViewById(R.id.video);
			holder.videoLayout = (RelativeLayout) convertView.findViewById(R.id.video_layout);
			holder.noteImg = (ImageView) convertView.findViewById(R.id.note_img);
			holder.specailTv = (TextView) convertView.findViewById(R.id.special_tv);
			holder.advType = (TextView) convertView.findViewById(R.id.adv_type);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final NewsVO vo = mList.get(position);
		LogUtil.e("vo", "=======" + vo.toString());
		// 大小图及视频区分
		if ("2".equals(vo.getModelid())) {
			holder.largeImg.setVisibility(View.VISIBLE);
			holder.smallImgLayout.setVisibility(View.GONE);
			holder.videoLayout.setVisibility(View.GONE);
			mImageLoader.displayImage(vo.getThumb(), holder.largeImg, mOptions);
		} else if ("4".equals(vo.getSuburl())) {
			// 视频
			holder.videoLayout.setVisibility(View.VISIBLE);
			holder.largeImg.setVisibility(View.GONE);
			holder.smallImgLayout.setVisibility(View.GONE);
			DisplayImageOptions videoOptions = Options.getOptionsDefaultIcon(R.drawable.video_def);
			mImageLoader.displayImage(vo.getThumb(), holder.videoImg, videoOptions);
			holder.mPlayBtnView.setOnClickListener(new MyOnclick(holder.mPlayBtnView, holder.VideoSuperPlayer, position));
			if (indexPostion == position) {
				holder.VideoSuperPlayer.setVisibility(View.VISIBLE);
			} else {
				holder.VideoSuperPlayer.setVisibility(View.GONE);
				holder.VideoSuperPlayer.close();
			}
		} else {
			holder.smallImgLayout.setVisibility(View.VISIBLE);
			holder.largeImg.setVisibility(View.GONE);
			holder.videoLayout.setVisibility(View.GONE);
			mImageLoader.displayImage(vo.getThumb(), holder.img, mOptions);
			// 字段没返的表示不显示图片
			if (vo.getThumb() == null) {
				holder.smallImgLayout.setVisibility(View.GONE);
			} else {
				holder.smallImgLayout.setVisibility(View.VISIBLE);
			}
		}

		if ("1".equals(vo.getAdv())) {
			holder.advType.setVisibility(View.VISIBLE);
			holder.readLayout.setVisibility(View.VISIBLE);
			holder.agreeCommentLayout.setVisibility(View.GONE);
			holder.specailTv.setVisibility(View.GONE);
			holder.timeTv.setVisibility(View.GONE);
			// 广告
			if ("2".equals(vo.getModelid().trim())) {
				// 大图
				holder.advType.setText(vo.getAdv_type());
				// holder.largeImgbadge.setText(vo.getAdv_type());
				// holder.largeImgbadge.setBadgePosition(BSBadgeView.POSITION_TOP_LEFT);
				// holder.largeImgbadge.setTextSize(10);
				// holder.largeImgbadge.setBadgeMargin(0,
				// BaseCommonUtils.dip2px(mContext, 10));
				// holder.largeImgbadge.setBackground(BaseCommonUtils.setBackgroundShap(mContext,
				// 0, R.color.sy_title_color, R.color.sy_title_color));
				// TranslateAnimation anim = new TranslateAnimation(-100, 0, 0,
				// 0);
				// anim.setInterpolator(new BounceInterpolator());
				// anim.setDuration(1000);
				// holder.largeImgbadge.toggle(anim, null);
				// holder.largeImgbadge.show();
			} else {
				// 小图
				holder.advType.setText(vo.getAdv_type());
				// holder.smallImgbadge.setText(vo.getAdv_type());
				// holder.smallImgbadge.setBadgePosition(BSBadgeView.POSITION_TOP_LEFT);
				// holder.smallImgbadge.setTextSize(10);
				// holder.smallImgbadge.setBadgeMargin(0);
				// holder.smallImgbadge.setBackground(BaseCommonUtils.setBackgroundShap(mContext,
				// 0, R.color.sy_title_color, R.color.sy_title_color));
				// TranslateAnimation anim = new TranslateAnimation(-100, 0, 0,
				// 0);
				// anim.setInterpolator(new BounceInterpolator());
				// anim.setDuration(1000);
				// holder.smallImgbadge.toggle(anim, null);
				// holder.smallImgbadge.show();

			}

		} else if ("3".equals(vo.getSuburl())) {
			// 专题
			if ("2".equals(vo.getModelid())) {
				holder.advType.setText(vo.getAdv_type());
				// holder.largeImgbadge.setText(vo.getAdv_type());
				// holder.largeImgbadge.setBadgePosition(BSBadgeView.POSITION_TOP_LEFT);
				// holder.largeImgbadge.setTextSize(10);
				// holder.largeImgbadge.setBadgeMargin(0);
				// holder.largeImgbadge.setBackground(BaseCommonUtils.setBackgroundShap(mContext,
				// 0, R.color.sy_title_color, R.color.sy_title_color));
				// TranslateAnimation anim = new TranslateAnimation(-100, 0, 0,
				// 0);
				// anim.setInterpolator(new BounceInterpolator());
				// anim.setDuration(1000);
				// holder.largeImgbadge.toggle(anim, null);
				// holder.largeImgbadge.show();
			} else {

				if ("special".equals(vo.getTitletype())) {
					// 专题列表
					holder.largeImg.setVisibility(View.VISIBLE);
					holder.smallImgLayout.setVisibility(View.GONE);
					holder.videoLayout.setVisibility(View.GONE);
					mImageLoader.displayImage(vo.getThumb(), holder.largeImg, mOptions);
				} else {
					// 头条个别专题
					holder.advType.setText("专题");
					// holder.smallImgbadge.setText("专题");
					// holder.smallImgbadge.setBadgePosition(BSBadgeView.POSITION_TOP_LEFT);
					// holder.smallImgbadge.setTextSize(10);
					// holder.smallImgbadge.setBadgeMargin(0);
					// holder.smallImgbadge.setBackground(BaseCommonUtils.setBackgroundShap(mContext,
					// 0, R.color.sy_title_color, R.color.sy_title_color));
					// TranslateAnimation anim = new TranslateAnimation(-100, 0,
					// 0, 0);
					// anim.setInterpolator(new BounceInterpolator());
					// anim.setDuration(1000);
					// holder.smallImgbadge.toggle(anim, null);
					// holder.smallImgbadge.show();
				}
			}

		} else {
			holder.advType.setVisibility(View.GONE);
			holder.smallImgbadge.hide();
			holder.largeImgbadge.hide();
			holder.readLayout.setVisibility(View.VISIBLE);
		}

		holder.tileTv.setText(vo.getTitle());

		if (vo.getCreatime() != null && vo.getTitletype() != null) {

			if ("special".equals(vo.getTitletype())) {
				holder.timeTv.setText(DateUtils.parseDateDay(vo.getCreatime()));
			} else if ("local".equals(vo.getTitletype())) {
				holder.timeTv.setText(forDate(vo.getCreatime(), vo.getTitletype()));
			} else {
				holder.timeTv.setText(DateUtils.parseMDHM(vo.getCreatime()));
			}
			holder.timeTv.setVisibility(View.VISIBLE);
		} else {
			holder.timeTv.setVisibility(View.GONE);
		}
		holder.agreeCommentLayout.setBackground(BaseCommonUtils.setBackgroundShap(mContext, 20, R.color.C3, R.color.C3));

		// 是否能够评论
		// if ("1".equals(vo.getIsReply())) {
		// holder.agreeTv.setText(vo.getRead());
		// holder.commentTv.setText(vo.getComments());
		// holder.agreeCommentLayout.setVisibility(View.VISIBLE);
		// } else if ("0".equals(vo.getIsReply())) {
		// holder.agreeCommentLayout.setVisibility(View.GONE);
		// } else {
		// holder.agreeTv.setText(vo.getRead());
		// holder.commentTv.setText(vo.getComments());
		// holder.agreeCommentLayout.setVisibility(View.VISIBLE);
		// }

		if (vo.getComments() == null && vo.getRead() == null) {
			holder.agreeCommentLayout.setVisibility(View.GONE);
		} else {
			holder.agreeCommentLayout.setVisibility(View.VISIBLE);
		}

		if (vo.getRead() != null) {
			holder.agreeTv.setText(vo.getRead());
			holder.agreeTv.setVisibility(View.VISIBLE);
		} else {
			holder.agreeTv.setVisibility(View.GONE);
		}

		if (vo.getComments() != null) {
			holder.commentTv.setText(vo.getComments());
			holder.commentTv.setVisibility(View.VISIBLE);
		} else {
			holder.commentTv.setVisibility(View.GONE);
		}

		if ("3".equals(vo.getSuburl())) {
			if ("special".equals(vo.getTitletype())) {
				// holder.readLayout.setVisibility(View.VISIBLE);
				// holder.specailTv.setVisibility(View.VISIBLE);

				holder.advType.setVisibility(View.GONE);
				holder.readLayout.setVisibility(View.VISIBLE);
				holder.specailTv.setVisibility(View.VISIBLE);
				holder.timeTv.setVisibility(View.VISIBLE);
			} else {
				// holder.readLayout.setVisibility(View.VISIBLE);
				// holder.specailTv.setVisibility(View.GONE);

				holder.advType.setVisibility(View.VISIBLE);
				holder.readLayout.setVisibility(View.VISIBLE);
				holder.specailTv.setVisibility(View.GONE);
				holder.timeTv.setVisibility(View.GONE);
				holder.agreeCommentLayout.setVisibility(View.GONE);
			}
		}
		if ("1".equals(vo.getAdv())) {
			holder.advType.setVisibility(View.VISIBLE);
			holder.readLayout.setVisibility(View.VISIBLE);
			holder.agreeCommentLayout.setVisibility(View.GONE);
			holder.specailTv.setVisibility(View.GONE);
			holder.timeTv.setVisibility(View.GONE);
		}

		// 猜你喜欢用的标识
		if ("1".equals(getStatus())) {
			holder.noteImg.setVisibility(View.VISIBLE);
		} else {
			holder.noteImg.setVisibility(View.GONE);
		}

		holder.agreeCommentLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if ("4".equals(vo.getSuburl())) {
					Bundle bundle = new Bundle();
					bundle.putString("id", vo.getContentid());
					bundle.putString("type", vo.getSuburl());
					bundle.putString("title", vo.getTitle());
					BaseActivity activity = (BaseActivity) mContext;
					activity.openActivity(DisscussActivity.class, bundle, 0);
				}

			}
		});

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
			// mList.get(position).getOuturl()

			mSuperVideoPlayer.loadAndPlay(MediaHelp.getInstance(), mList.get(position).getOuturl(), position, false);
			mSuperVideoPlayer.setVideoPlayCallback(new MyVideoPlayCallback(mPlayBtnView, mSuperVideoPlayer, mList.get(position).getOuturl()));
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
		private TextView tileTv, agreeTv, commentTv, timeTv, specailTv;
		private ImageView img, largeImg;
		private LinearLayout agreeCommentLayout, readLayout, smallImgLayout;
		private BSBadgeView smallImgbadge, largeImgbadge, zhengWuBadge;
		private ImageView videoImg;
		private VideoSuperPlayer VideoSuperPlayer;
		private ImageView mPlayBtnView, noteImg;
		private RelativeLayout videoLayout;
		private TextView noticeTv;
		private TextView advType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String forDate(String timeStr, String vo) {
		String currentDay = DateUtils.parseDateDay(System.currentTimeMillis() / 1000 + "");
		long time = Long.parseLong(timeStr) * 1000;

		if (currentDay.equals(DateUtils.parseDateDay(timeStr)) && vo.equals("local") || vo.equals("travel")) {
			return "今天";
		} else if (DateUtils.parseDateDay(timeStr).equals(DateUtils.ConverToString(DateUtils.getPreDay(new Date(System.currentTimeMillis())))) && vo.equals("local") || vo.equals("travel")) {
			return "昨天";
		} else {
			return DateUtils.parseMDHM(timeStr);
		}
	}

}
