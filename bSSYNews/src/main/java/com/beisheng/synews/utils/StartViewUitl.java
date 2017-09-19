package com.beisheng.synews.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.synews.activity.CommunityDetailActivity;
import com.beisheng.synews.activity.LiveDetailActivity;
import com.beisheng.synews.activity.NewsDetailActivity;
import com.beisheng.synews.activity.NewsPhotoDetailActivity;
import com.beisheng.synews.activity.NewsVideoDetailActivity;
import com.beisheng.synews.activity.SpecialTopicActivity;
import com.beisheng.synews.activity.WebViewActivity;
import com.beisheng.synews.activity.ZhengWuActivity;
import com.beisheng.synews.mode.LiveVO;

public class StartViewUitl {
	//
	public static void startView(BaseActivity activity, String suburl, String contentid, String link, String govermentid, String type) {
		LogUtil.e("startView", "=================1");
		if (!"".equals(link) && link != null && !"8".equals(suburl) && !"7".equals(suburl)) {
			LogUtil.e("startView", "=================2");
			Bundle bundle = new Bundle();
			bundle.putString("url", link);
			bundle.putString("name", "头条推荐");
			activity.openActivity(WebViewActivity.class, bundle, 0);
			// 8点击广告 7回复帖子 6发表帖子 4分享新闻
			PointsAddUtil.commitdPoints("8", contentid, "头条推荐");
			return;
		}
		LogUtil.e("startView", "=================3");
		// suburl 0 普通新闻，1论坛，2图片，3专题，4视频，5直播,6论坛
		Bundle bundle = new Bundle();
		bundle.putString("id", contentid);
		bundle.putString("suburl", suburl);
		bundle.putString("new_type", type);
		if ("0".equals(suburl)) {
			LogUtil.e("startView", "=================4");
			activity.openActivity(NewsDetailActivity.class, bundle, 0);
		} else if ("1".equals(suburl)) {
			activity.openActivity(CommunityDetailActivity.class, bundle, 0);
		} else if ("2".equals(suburl)) {
			activity.openActivity(NewsPhotoDetailActivity.class, bundle, 0);
		} else if ("3".equals(suburl)) {
			activity.openActivity(SpecialTopicActivity.class, bundle, 0);
		} else if ("4".equals(suburl)) {
			// activity.openActivity(NewsVideoDetailActivity.class, bundle, 0);
		} else if ("5".equals(suburl)) {
			activity.openActivity(LiveDetailActivity.class, bundle, 0);
		} else if ("6".equals(suburl)) {
			bundle.putString("isReplay", "0");
			activity.openActivity(ZhengWuActivity.class, bundle, 0);
		} else if ("9".equals(suburl)) {
			bundle.putString("govermentid", govermentid);
			bundle.putString("isReplay", "1");
			activity.openActivity(NewsDetailActivity.class, bundle, 0);
		} else if ("7".equals(suburl)) {
			// bundle.putString("url", link);
			// bundle.putString("name", "日报");
			// activity.openActivity(WebViewActivity.class, bundle, 0);
			bundle.putString("govermentid", govermentid);
			bundle.putString("isReplay", "0");
			bundle.putString("link", link);
			activity.openActivity(NewsDetailActivity.class, bundle, 0);
		} else if ("8".equals(suburl)) {
			bundle.putString("url", link);
			bundle.putString("name", "爆料详情");
			activity.openActivity(WebViewActivity.class, bundle, 0);
		}
	}

	public static void startView(BaseActivity activity, String suburl, String contentid, String link, String govermentid, String type, LiveVO content) {
		LogUtil.e("startView", "=================1");
		if (!"".equals(link) && link != null && !"8".equals(suburl) && !"7".equals(suburl)) {
			LogUtil.e("startView", "=================2");
			Bundle bundle = new Bundle();
			bundle.putString("url", link);
			bundle.putString("name", "头条推荐");
			activity.openActivity(WebViewActivity.class, bundle, 0);
			// 8点击广告 7回复帖子 6发表帖子 4分享新闻
			PointsAddUtil.commitdPoints("8", contentid, "头条推荐");
			return;
		}
		LogUtil.e("startView", "=================3");
		// suburl 0 普通新闻，1论坛，2图片，3专题，4视频，5直播,6论坛
		Bundle bundle = new Bundle();
		bundle.putString("id", contentid);
		bundle.putString("suburl", suburl);
		bundle.putString("new_type", type);
		if ("0".equals(suburl)) {
			LogUtil.e("startView", "=================4");
			activity.openActivity(NewsDetailActivity.class, bundle, 0);
		} else if ("1".equals(suburl)) {
			activity.openActivity(CommunityDetailActivity.class, bundle, 0);
		} else if ("2".equals(suburl)) {
			activity.openActivity(NewsPhotoDetailActivity.class, bundle, 0);
		} else if ("3".equals(suburl)) {
			activity.openActivity(SpecialTopicActivity.class, bundle, 0);
		} else if ("4".equals(suburl)) {
			// activity.openActivity(NewsVideoDetailActivity.class, bundle, 0);
		} else if ("5".equals(suburl)) {
			activity.openActivity(LiveDetailActivity.class, bundle, 0);
		} else if ("6".equals(suburl)) {
			bundle.putString("isReplay", "0");
			activity.openActivity(ZhengWuActivity.class, bundle, 0);
		} else if ("9".equals(suburl)) {
			bundle.putString("govermentid", govermentid);
			bundle.putString("isReplay", "1");
			activity.openActivity(NewsDetailActivity.class, bundle, 0);
		} else if ("7".equals(suburl)) {
			// bundle.putString("url", link);
			// bundle.putString("name", "日报");
			// activity.openActivity(WebViewActivity.class, bundle, 0);
			bundle.putString("govermentid", govermentid);
			bundle.putString("link", link);
			bundle.putString("content", content.getContent());
			activity.openActivity(NewsDetailActivity.class, bundle, 0);
		} else if ("8".equals(suburl)) {
			bundle.putString("url", link);
			bundle.putString("name", "爆料详情");
			activity.openActivity(WebViewActivity.class, bundle, 0);
		}
	}

	// 推送消息专用
	public static void jPushStartView(Context context, String suburl, String contentid, String link, String govermentid, String type) {
		Intent intent = new Intent();
		// suburl 0 普通新闻，1论坛，2图片，3专题，4视频，5直播,6论坛
		intent.putExtra("id", contentid);
		intent.putExtra("suburl", suburl);
		intent.putExtra("new_type", type);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		if (!"".equals(link) && link != null && !"8".equals(suburl)) {
			intent.putExtra("url", link);
			intent.putExtra("name", "头条推荐");
			intent.setClass(context, WebViewActivity.class);
			// 8点击广告 7回复帖子 6发表帖子 4分享新闻
			PointsAddUtil.commitdPoints("8", contentid, "头条推荐");
		} else if ("0".equals(suburl)) {
			intent.setClass(context, NewsDetailActivity.class);
		} else if ("1".equals(suburl)) {
			intent.setClass(context, NewsDetailActivity.class);
		} else if ("2".equals(suburl)) {
			intent.setClass(context, NewsPhotoDetailActivity.class);
		} else if ("3".equals(suburl)) {
			intent.setClass(context, SpecialTopicActivity.class);
		} else if ("4".equals(suburl)) {
			intent.setClass(context, NewsVideoDetailActivity.class);
		} else if ("5".equals(suburl)) {
			intent.setClass(context, LiveDetailActivity.class);
		} else if ("6".equals(suburl)) {
			intent.putExtra("isReplay", "0");
			intent.setClass(context, ZhengWuActivity.class);
		} else if ("9".equals(suburl)) {
			intent.putExtra("govermentid", govermentid);
			intent.putExtra("isReplay", "1");
			intent.setClass(context, NewsDetailActivity.class);
		} else if ("7".equals(suburl)) {
			intent.putExtra("url", link);
			intent.putExtra("name", "日报");
			intent.setClass(context, WebViewActivity.class);
		} else if ("8".equals(suburl)) {
			intent.putExtra("url", link);
			intent.putExtra("name", "爆料详情");
			intent.setClass(context, WebViewActivity.class);
		}

		if (intent.getComponent() != null)
			context.startActivity(intent);
	}

	// 首页广告传入名字
	public static void startView(BaseActivity activity, String suburl, String contentid, String link, String govermentid, String type, String advName) {
		if (!"".equals(link) && link != null && !"8".equals(suburl) && !"7".equals(suburl)) {
			Bundle bundle = new Bundle();
			bundle.putString("url", link);
			bundle.putString("name", advName);
			activity.openActivity(WebViewActivity.class, bundle, 0);
			// 8点击广告 7回复帖子 6发表帖子 4分享新闻
			PointsAddUtil.commitdPoints("8", contentid, "头条推荐");
			return;
		}
		// suburl 0 普通新闻，1论坛，2图片，3专题，4视频，5直播,6论坛
		Bundle bundle = new Bundle();
		bundle.putString("id", contentid);
		bundle.putString("suburl", suburl);
		bundle.putString("new_type", type);
		if ("0".equals(suburl)) {
			activity.openActivity(NewsDetailActivity.class, bundle, 0);
		} else if ("1".equals(suburl)) {
			activity.openActivity(CommunityDetailActivity.class, bundle, 0);
		} else if ("2".equals(suburl)) {
			activity.openActivity(NewsPhotoDetailActivity.class, bundle, 0);
		} else if ("3".equals(suburl)) {
			activity.openActivity(SpecialTopicActivity.class, bundle, 0);
		} else if ("4".equals(suburl)) {
			// activity.openActivity(NewsVideoDetailActivity.class, bundle, 0);
		} else if ("5".equals(suburl)) {
			activity.openActivity(LiveDetailActivity.class, bundle, 0);
		} else if ("6".equals(suburl)) {
			bundle.putString("isReplay", "0");
			activity.openActivity(ZhengWuActivity.class, bundle, 0);
		} else if ("9".equals(suburl)) {
			bundle.putString("govermentid", govermentid);
			bundle.putString("isReplay", "1");
			activity.openActivity(NewsDetailActivity.class, bundle, 0);
		} else if ("7".equals(suburl)) {
			// bundle.putString("url", link);
			// bundle.putString("name", "日报");
			// activity.openActivity(WebViewActivity.class, bundle, 0);
			bundle.putString("link", link);
			activity.openActivity(NewsDetailActivity.class, bundle, 0);
		} else if ("8".equals(suburl)) {
			bundle.putString("url", link);
			bundle.putString("name", "爆料详情");
			activity.openActivity(WebViewActivity.class, bundle, 0);
		}
	}
}
