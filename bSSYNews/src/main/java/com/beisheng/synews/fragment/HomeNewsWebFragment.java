package com.beisheng.synews.fragment;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.fragment.BaseFragment;
import com.beisheng.base.utils.WebviewUtil;
import com.bs.bsims.observer.BSHtmlObserver.Watcher;
import com.bs.bsims.observer.MessageHtml;
import com.beisheng.synews.activity.LoginActivity;
import com.beisheng.synews.activity.MoreWebViewActivity;
import com.beisheng.synews.activity.RegisteredActivtiy;
import com.beisheng.synews.application.AppApplication;
import com.beisheng.synews.mode.NewsVO;
import com.beisheng.synews.utils.LogUtil;
import com.beisheng.synews.utils.ShareUtil;
import com.im.zhsy.R;

public class HomeNewsWebFragment extends BaseFragment implements OnLongClickListener, Watcher {
	private String TAG = "HomeNewsPublishFragment";
	private BaseActivity mActivity;
	private String mCid;
	private NewsVO mNewsVO;
	private WebView mWebView;
	private ProgressBar progressbar;
	private String mLink;
	private String mTitle;
	private String share_title = "测试";
	private String share_image = "测试";
	private String share_description = "测试";
	private String share_url = "测试";
	private LinearLayout mWebLayout;
	private String mStatus = "";// 1是关闭，2是返回

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = (BaseActivity) activity;
		mLink = this.getArguments().getString("link");
		mTitle = this.getArguments().getString("title");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_news_web_fragment, container, false);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initViews(view);
		bindViewsListener();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	private void initViews(View view) {
		mWebView = (WebView) view.findViewById(R.id.webview);
		mWebView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
		mWebView.setWebViewClient(new MyWebViewClient());
		progressbar = new ProgressBar(mActivity, null, android.R.attr.progressBarStyleHorizontal);
		progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 5));
		Drawable drawable = this.getResources().getDrawable(R.drawable.webview_progress);
		progressbar.setProgressDrawable(drawable);
		mWebView.addView(progressbar);
		WebviewUtil.SetWebview(mWebView);
		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					progressbar.setVisibility(View.INVISIBLE);
				} else {
					if (View.INVISIBLE == progressbar.getVisibility()) {
						progressbar.setVisibility(View.VISIBLE);
					}
					progressbar.setProgress(newProgress);
				}
				super.onProgressChanged(view, newProgress);
			}

		});

	}

	public void bindViewsListener() {
		MessageHtml.getInstance().add(this);

		mWebView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent arg1) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public String getFragmentName() {
		return TAG;// 不知道该方法有没有用
	}

	@Override
	public boolean onLongClick(View arg0) {
		return true;
	}

	@Override
	public void updateNotify(Object content, String status) {
		if (content instanceof String) {
			String str = (String) content;
			if (!status.equals(mLink))
				return;
			if ("2".equals(str)) {
				mActivity.mBaseBackTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_head_back, 0, 0, 0);
				mActivity.base_back_tv_finish.setVisibility(View.VISIBLE);
				mWebView.loadUrl(mLink);

				mActivity.mBaseBackTv.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						if (mWebView.canGoBack()) {
							mWebView.goBack(); // goBack()表示返回WebView的上一页面
						} else {
							MessageHtml.getInstance().notifyWatcher("1", mLink);
						}
					}
				});
				mActivity.base_back_tv_finish.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						MessageHtml.getInstance().notifyWatcher("1", mLink);
					}
				});
			} else if ("3".equals(str)) {
				if (mWebView.canGoBack()) {
					mWebView.goBack(); // goBack()表示返回WebView的上一页面
				} else {
					MessageHtml.getInstance().notifyWatcher("1", mLink);
				}
			}
		}

	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		MessageHtml.getInstance().remove(this);
	}

	private class MyWebViewClient extends WebViewClient {

		private Intent intent;

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			LogUtil.e("shouldOverrideUrlLoading", url);
			Map<String, String> Headers = new HashMap<String, String>();
			// webview 保持header
			if (AppApplication.getInstance().getUserInfoVO() != null) {
				Headers.put("TOKENID", AppApplication.getInstance().getUserInfoVO().getUid());
				Headers.put("TOKEN", AppApplication.getInstance().getUserInfoVO().getToken());
				Headers.put("TOKENTIME", AppApplication.getInstance().getUserInfoVO().getTime());
				Headers.put("TOKENOS", AppApplication.getInstance().getUserInfoVO().getDevice());
			} else {
				Headers.put("TOKENID", AppApplication.getInstance().getUid());
				Headers.put("TOKEN", "");
				Headers.put("TOKENTIME", "");
				Headers.put("TOKENOS", "10yan.android");
			}

			view.loadUrl(url, Headers);
			if (url != null && url.contains("referer=close")) {
				MessageHtml.getInstance().notifyWatcher("1", mLink);
				return true;
			} else if (url != null && url.contains("referer=back")) {
				mWebView.goBack();
				return true;
			} else if (url != null && url.contains("referer=_blank")) {
				intent = new Intent(getActivity(), MoreWebViewActivity.class);
				intent.putExtra("url", url);
				startActivity(intent);

				mWebView.stopLoading();
				return true;

			} else if (url != null && url.contains("referer=share")) {
				LogUtil.e(".......share", "11111111111111");
				mWebView.stopLoading();
				LogUtil.e(".......share", "222222222222222");
				ShareUtil.share(getActivity(), share_image, share_title, share_description, share_url);
				LogUtil.e(".......share", "333333333333333");
				return true;
			} else if (url != null && url.contains("referer=user_login")) {
				startActivity(new Intent(getActivity(), LoginActivity.class));

				return true;
			} else if (url != null && url.contains("referer=user_register")) {

				startActivity(new Intent(getActivity(), RegisteredActivtiy.class));
				return true;
			} else if (url.startsWith("tel:")) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				startActivity(intent);
				return true;
			}

			return super.shouldOverrideUrlLoading(view, url);

		}

		@Override
		public void onPageFinished(WebView view, String url) {

			view.getSettings().setJavaScriptEnabled(true);
			super.onPageFinished(view, url);
			view.loadUrl("javascript:window.local_obj.showSource(" + "document.querySelector('input[name=\"share_title\"]').getAttribute('value')" + ");");
			view.loadUrl("javascript:window.local_obj.showSource1(" + "document.querySelector('input[name=\"share_image\"]').getAttribute('value')" + ");");
			view.loadUrl("javascript:window.local_obj.showSource2(" + "document.querySelector('input[name=\"share_description\"]').getAttribute('value')" + ");");
			view.loadUrl("javascript:window.local_obj.showSource3(" + "document.querySelector('input[name=\"share_url\"]').getAttribute('value')" + ");");

		}
	}

	final class InJavaScriptLocalObj {
		@JavascriptInterface
		public void showSource(String share_title) {
			LogUtil.d("share_title", share_title);
			HomeNewsWebFragment.this.share_title = share_title;
		}

		@JavascriptInterface
		public void showSource1(String share_image) {
			LogUtil.d("share_image", share_image);
			HomeNewsWebFragment.this.share_image = share_image;
		}

		@JavascriptInterface
		public void showSource2(String share_description) {
			LogUtil.d("share_description", share_description);
			HomeNewsWebFragment.this.share_description = share_description;
		}

		@JavascriptInterface
		public void showSource3(String share_url) {
			LogUtil.d("share_url", share_url);
			HomeNewsWebFragment.this.share_url = share_url;
		}

	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (!hidden) {
			mActivity.mBaseBackTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_head_back, 0, 0, 0);
			mActivity.base_back_tv_finish.setVisibility(View.VISIBLE);

			// MessageHtml.getInstance().notifyWatcher("2", mLink);

			mActivity.mBaseBackTv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if (mWebView.canGoBack()) {
						mWebView.goBack(); // goBack()表示返回WebView的上一页面
					} else {
						MessageHtml.getInstance().notifyWatcher("1", mLink);
					}
				}
			});
		}
	}

}
