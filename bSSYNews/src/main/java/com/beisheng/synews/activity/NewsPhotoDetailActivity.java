package com.beisheng.synews.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.http.HttpClientUtil;
import com.beisheng.base.photoview.PhotoView;
import com.beisheng.base.utils.BitmapUtil;
import com.beisheng.base.view.BSPopWindowsBottom;
import com.beisheng.base.view.BSPopWindowsBottom.PopCallback;
import com.beisheng.synews.application.AppApplication;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.fragment.BottomFragmentPhoto;
import com.beisheng.synews.mode.NewsVO;
import com.beisheng.synews.utils.StartViewUitl;
import com.google.gson.Gson;
import com.im.zhsy.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint("SetJavaScriptEnabled")
public class NewsPhotoDetailActivity extends BaseActivity implements OnPageChangeListener {
	private NewsVO mNewsVO;
	private ViewPager mViewPager;
	private TextView mTitleTv, mCountTv, mContentTv;
	private SamplePagerAdapter mAdapter;
	private String mId;
	private BSPopWindowsBottom mPop;
	private RelativeLayout mTextLayout;
	private String[] array = { "保存图片", "取消" };
	private String mCurrentUrl = "";
	private boolean mIsShow = false;
	private String mNewType;

	@Override
	public void baseSetContentView() {
		View.inflate(this, R.layout.news_photo_detail_activity, mBaseContentLayout);
	}

	@Override
	public boolean getDataResult() {
		return getData();
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public void initView() {
		mBaseTitleTv.setText("图片新闻");
		mBaseHeadLayout.setBackgroundColor(this.getResources().getColor((R.color.C4)));
		mTextLayout = (RelativeLayout) findViewById(R.id.text_layout);
		mViewPager = (ViewPager) findViewById(R.id.view_pager);
		mTitleTv = (TextView) findViewById(R.id.title_tv);
		mCountTv = (TextView) findViewById(R.id.count_tv);
		mContentTv = (TextView) findViewById(R.id.content_tv);
		mAdapter = new SamplePagerAdapter(this);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(this);
		mViewPager.setCurrentItem(0);
		mId = this.getIntent().getStringExtra("id");
		mNewType = this.getIntent().getStringExtra("new_type");

		initPop();

		// 获取本地数据，优化速度
		try {
			Gson gson = new Gson();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("uid", AppApplication.getInstance().getUid());
			map.put("contentid", mId);
			map.put("suburl", "2");// 图片详情
			map.put("new_type", mNewType);
			map.put("uid", AppApplication.getInstance().getUid());
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
	}

	public boolean getData() {
		try {
			Gson gson = new Gson();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("uid", AppApplication.getInstance().getUid());
			map.put("contentid", mId);
			map.put("suburl", "2");// 图片详情
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
		mTitleTv.setText(mNewsVO.getTitle());
		mContentTv.setText(mNewsVO.getList().get(0).getNote());
		mCountTv.setText(1 + "/" + mNewsVO.getList().size());
		// mAdapter.updateData(mNewsVO.getList());
		mAdapter.updateData(mNewsVO);
		try {
			// 底部View
			BottomFragmentPhoto bottomFragment = new BottomFragmentPhoto();
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction transaction = fragmentManager.beginTransaction();
			transaction.replace(R.id.bottom_layout, bottomFragment);
			bottomFragment.setNewsVo(mNewsVO);
			transaction.commit();
		} catch (Exception e) {
		}

	}

	@Override
	public void executeFailure() {
		super.executeFailure();
		if (mNewsVO != null)
			showCustomToast(mNewsVO.getRetinfo());
		else
			showCustomToast("亲，请检查网络哦");
	}

	class SamplePagerAdapter extends PagerAdapter {
		private Context mContext;
		private List<NewsVO> mList;
		private List<NewsVO> mLastList;

		public SamplePagerAdapter(Context context) {
			this.mContext = context;
			mList = new ArrayList<NewsVO>();
			mLastList = new ArrayList<NewsVO>();
		}

		@Override
		public int getCount() {
			if (mLastList != null && mLastList.size() != 0) {
				return mList.size() + 1;
			} else {
				return mList.size();
			}
		}

		public void updateData(NewsVO vo) {
			this.mList = vo.getList();
			this.mLastList = vo.getRelatedlist();
			this.notifyDataSetChanged();
		}

		@SuppressLint("NewApi")
		@Override
		public View instantiateItem(ViewGroup container, final int position) {
			if (position == mList.size() && mLastList.size() != 0) {
				View view = View.inflate(container.getContext(), R.layout.photo_last_item, null);
				ImageView img01 = (ImageView) view.findViewById(R.id.img_01);
				ImageView img02 = (ImageView) view.findViewById(R.id.img_02);
				ImageView img03 = (ImageView) view.findViewById(R.id.img_03);
				ImageView img04 = (ImageView) view.findViewById(R.id.img_04);
				ImageView img05 = (ImageView) view.findViewById(R.id.img_05);
				TextView tv01 = (TextView) view.findViewById(R.id.tv_01);
				TextView tv02 = (TextView) view.findViewById(R.id.tv_02);
				TextView tv03 = (TextView) view.findViewById(R.id.tv_03);
				TextView tv04 = (TextView) view.findViewById(R.id.tv_04);
				TextView tv05 = (TextView) view.findViewById(R.id.tv_05);
				LinearLayout layout01 = (LinearLayout) view.findViewById(R.id.layout_01);
				LinearLayout layout02 = (LinearLayout) view.findViewById(R.id.layout_02);
				LinearLayout layout03 = (LinearLayout) view.findViewById(R.id.layout_03);
				LinearLayout layout04 = (LinearLayout) view.findViewById(R.id.layout_04);
				LinearLayout layout05 = (LinearLayout) view.findViewById(R.id.layout_05);
				if (mLastList.size() > 0) {
					mImageLoader.displayImage(mLastList.get(0).getThumb(), img01, mOptions);
					tv01.setText(mLastList.get(0).getTitle());
					layout01.setVisibility(View.VISIBLE);
				}
				if (mLastList.size() > 1) {
					mImageLoader.displayImage(mLastList.get(1).getThumb(), img02, mOptions);
					tv02.setText(mLastList.get(1).getTitle());
					layout02.setVisibility(View.VISIBLE);
				}
				if (mLastList.size() > 2) {
					mImageLoader.displayImage(mLastList.get(2).getThumb(), img03, mOptions);
					tv03.setText(mLastList.get(2).getTitle());
					layout03.setVisibility(View.VISIBLE);
				}
				if (mLastList.size() > 3) {
					mImageLoader.displayImage(mLastList.get(3).getThumb(), img04, mOptions);
					tv04.setText(mLastList.get(3).getTitle());
					layout04.setVisibility(View.VISIBLE);
				}
				if (mLastList.size() > 4) {
					mImageLoader.displayImage(mLastList.get(4).getThumb(), img05, mOptions);
					tv05.setText(mLastList.get(4).getTitle());
					layout05.setVisibility(View.VISIBLE);
				}

				container.addView(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				img01.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						StartViewUitl.startView(NewsPhotoDetailActivity.this, mLastList.get(0).getSuburl(), mLastList.get(0).getContentid(), null, null, null);
						NewsPhotoDetailActivity.this.finish();
					}
				});
				img02.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {

						StartViewUitl.startView(NewsPhotoDetailActivity.this, mLastList.get(1).getSuburl(), mLastList.get(1).getContentid(), null, null, null);
						NewsPhotoDetailActivity.this.finish();
					}
				});
				img03.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						StartViewUitl.startView(NewsPhotoDetailActivity.this, mLastList.get(2).getSuburl(), mLastList.get(2).getContentid(), null, null, null);
						NewsPhotoDetailActivity.this.finish();
					}
				});
				img04.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						StartViewUitl.startView(NewsPhotoDetailActivity.this, mLastList.get(3).getSuburl(), mLastList.get(3).getContentid(), null, null, null);
						NewsPhotoDetailActivity.this.finish();
					}
				});
				img05.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						StartViewUitl.startView(NewsPhotoDetailActivity.this, mLastList.get(4).getSuburl(), mLastList.get(4).getContentid(), null, null, null);
						NewsPhotoDetailActivity.this.finish();
					}
				});
				return view;
			} else {
				final PhotoView img = new PhotoView(container.getContext());
				container.addView(img, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				mImageLoader.displayImage(mNewsVO.getList().get(position).getThumb(), img, mOptions);
				img.setOnLongClickListener(new OnLongClickListener() {

					@Override
					public boolean onLongClick(View arg0) {
						mCurrentUrl = mNewsVO.getList().get(position).getThumb();
						mPop.showPopupWindow(img);
						return false;
					}
				});
				return img;
			}

		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int position) {
		if (position != mNewsVO.getList().size()) {
			mContentTv.setText(mNewsVO.getList().get(position).getNote());
			mCountTv.setText((position + 1) + "/" + mNewsVO.getList().size());
			mBaseTitleTv.setText("图片新闻");
		} else {
			mTextLayout.setVisibility(View.GONE);
			mBaseTitleTv.setText("推荐图集");
		}

	}

	public void initPop() {
		PopCallback callback = new PopCallback() {

			@Override
			public void callback(String str, int position) {
				switch (position) {
				case 0:
					Bitmap bitmap = mImageLoader.loadImageSync(mCurrentUrl);
					if (bitmap != null) {
						BitmapUtil.saveImageToGallery(NewsPhotoDetailActivity.this, bitmap);
						showCustomToast("图片保存成功");
					}

					break;
				case 1:
					mPop.dismiss();
					break;

				default:
					break;
				}
			}
		};
		mPop = new BSPopWindowsBottom(this, array, callback);
	}

}
