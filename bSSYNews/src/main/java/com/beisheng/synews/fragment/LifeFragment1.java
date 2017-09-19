package com.beisheng.synews.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.adapter.BSBaseAdapter;
import com.beisheng.base.fragment.BaseFragment;
import com.beisheng.base.http.HttpClientUtil;
import com.beisheng.base.interfaces.UpdateCallback;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.Options;
import com.beisheng.base.utils.ThreadUtil;
import com.beisheng.base.view.BSFlowIndicator;
import com.beisheng.base.view.BSGridView;
import com.beisheng.synews.activity.BrokeActivity;
import com.beisheng.synews.activity.WebViewActivity;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.mode.LifeVO;
import com.beisheng.synews.utils.StartViewUitl;
import com.beisheng.synews.viewimage.animations.DescriptionAnimation;
import com.beisheng.synews.viewimage.animations.SliderLayout;
import com.beisheng.synews.viewimage.slidertypes.BaseSliderView;
import com.beisheng.synews.viewimage.slidertypes.BaseSliderView.OnSliderClickListener;
import com.beisheng.synews.viewimage.slidertypes.TextSliderView;
import com.google.gson.Gson;
import com.im.zhsy.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LifeFragment1 extends BaseFragment implements OnClickListener, UpdateCallback, OnSliderClickListener {
	private String TAG = "LifeFragment";
	private BaseActivity mActivity;
	private int[] mColorArray = new int[] { R.color.life_fragment_color1, R.color.life_fragment_color2, R.color.life_fragment_color3 };
	private LinearLayout mRootLayout, mFragmentLayout;
	private LifeVO mLifeVO, mLifeOtherVO;
	private BSGridView mGridView01, mGridView02, mGridView03;
	private LifeItmeAdapter mLifeItmeAdapter;
	private MenuItmeAdapter mMenuItmeAdapter01, mMenuItmeAdapter02;
	private ViewPager mViewPager;
	private SamplePagerAdapter mPagerAdapter;
	private BSFlowIndicator mFlowIndicator;
	private TextView mLifeNotifyTv;
	private LifeItemFragment mLifeItemFragment;

	public static LifeFragment1 newInstance() {
		LifeFragment1 lifeFragment = new LifeFragment1();
		return lifeFragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = (BaseActivity) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.life_fragment, container, false);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initViews(view);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		new ThreadUtil(mActivity, this).start();
	}

	private void initViews(View view) {
		mActivity.mBaseOkTv.setVisibility(View.GONE);
		mActivity.mBaseHeadLayout.setVisibility(View.VISIBLE);
		mRootLayout = (LinearLayout) view.findViewById(R.id.root_layout);
		mActivity.showProgressDialog();
		mGridView03 = (BSGridView) view.findViewById(R.id.gridview_03);
		mLifeItmeAdapter = new LifeItmeAdapter(mActivity);
		mGridView03.setAdapter(mLifeItmeAdapter);
		mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
		mPagerAdapter = new SamplePagerAdapter(mActivity);
		mFlowIndicator = (BSFlowIndicator) view.findViewById(R.id.galleryIndicator);
		mLifeNotifyTv = (TextView) view.findViewById(R.id.life_notify_tv);
		mFragmentLayout = (LinearLayout) view.findViewById(R.id.fragment_layout);
	}

	public boolean getData() {
		try {
			Gson gson = new Gson();
			HashMap<String, String> map = new HashMap<String, String>();
			if (mActivity.hasNetWork()) {
				String jsonStr = HttpClientUtil.getRequest(mActivity, Constant.DOMAIN_NAME + Constant.LIFE_FRAGMENT_URL1, map);
				mLifeVO = gson.fromJson(jsonStr, LifeVO.class);
				mActivity.saveJsonCache(Constant.LIFE_FRAGMENT_URL, map, jsonStr);
			} else {
				String oldStr = mActivity.getCacheFromDatabase(Constant.LIFE_FRAGMENT_URL, map);
				mLifeVO = gson.fromJson(oldStr, LifeVO.class);
			}

			HashMap<String, String> map2 = new HashMap<String, String>();
			if (mActivity.hasNetWork()) {
				String jsonStr = HttpClientUtil.getRequest(mActivity, Constant.LIFE_FRAGMENT_URL2, map);
				mLifeOtherVO = gson.fromJson(jsonStr, LifeVO.class);
				mActivity.saveJsonCache(Constant.LIFE_FRAGMENT_URL2, map, jsonStr);
			} else {
				String oldStr = mActivity.getCacheFromDatabase(Constant.LIFE_FRAGMENT_URL2, map2);
				mLifeOtherVO = gson.fromJson(oldStr, LifeVO.class);
			}

			if (Constant.RESULT_SUCCESS_CODE.equals(mLifeVO.getCode())) {
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean execute() {
		return getData();
	}

	@Override
	public void executeSuccess() {
		mActivity.dismissProgressDialog();
		createGroup();

		if (mLifeOtherVO != null) {
			mLifeNotifyTv.setVisibility(View.VISIBLE);
			mLifeItmeAdapter.updateData(mLifeOtherVO.getList());
			mLifeItmeAdapter.notifyDataSetChanged();
		}

	}

	@Override
	public void executeFailure() {
		mActivity.dismissProgressDialog();
		if (mLifeVO != null)
			mActivity.showCustomToast(mLifeVO.getRetinfo());
		else
			mActivity.showCustomToast("亲，请检查网络哦");
	}

	@SuppressLint("ResourceAsColor")
	public void createGroup() {
		ArrayList<HashMap<String, Object>> item = new ArrayList<HashMap<String, Object>>();

		List<LifeVO> listVo = mLifeVO.getList();
		for (int i = 0; i < listVo.size(); i++) {
			LinearLayout titleLayout = new LinearLayout(mActivity);
			titleLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.C1));
			LinearLayout.LayoutParams titleLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
			titleLayout.setLayoutParams(titleLayoutParams);

			// 添加左边的
			ImageView leftImg = new ImageView(mActivity);
			LinearLayout.LayoutParams leftImgParams = new LinearLayout.LayoutParams(BaseCommonUtils.dip2px(mActivity, 5), LinearLayout.LayoutParams.MATCH_PARENT);
			leftImg.setLayoutParams(leftImgParams);
			titleLayout.addView(leftImg);
			leftImg.setBackgroundColor(mActivity.getResources().getColor(mColorArray[i % 3]));

			// 添加标题
			TextView tv = new TextView(mActivity);
			LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
			tvParams.weight = 1;
			tv.setLayoutParams(tvParams);
			tv.setPadding(BaseCommonUtils.dip2px(mActivity, 15), BaseCommonUtils.dip2px(mActivity, 10), BaseCommonUtils.dip2px(mActivity, 15), BaseCommonUtils.dip2px(mActivity, 10));
			tv.setTextColor(mActivity.getResources().getColor(R.color.C5));
			tv.setBackgroundColor(mActivity.getResources().getColor(R.color.C3));
			tv.setText(listVo.get(i).getTitle());
			titleLayout.addView(tv);

//			 添加上边的两个
//			 mRootLayout.addView(titleLayout);

			if (i == 0) {
				try {
					// 底部View
					List<List<LifeVO>> parentList = new ArrayList<List<LifeVO>>();
					List<LifeVO> chilrdList = new ArrayList<LifeVO>();
					List<LifeVO> list = new ArrayList<LifeVO>();

					for (int j = 0; j < listVo.get(0).getChildren().size(); j++) {
						int count = listVo.get(0).getChildren().size() / 10;

						list.add(listVo.get(0).getChildren().get(j));
						if ((j + 1) % 10 == 0 && j > 0) {
							List<LifeVO> childList = new ArrayList<LifeVO>();
							childList.addAll(list);
							list.clear();
							parentList.add(childList);
						}

						if (j == listVo.get(0).getChildren().size() - 1) {
							parentList.add(list);
						}

					}
					mLifeItemFragment = new LifeItemFragment(parentList);
					FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
					FragmentTransaction transaction = fragmentManager.beginTransaction();
					transaction.add(R.id.fragment_layout, mLifeItemFragment);
					transaction.commitAllowingStateLoss();
				} catch (Exception e) {
				}
			} else {
				BSGridView gv = new BSGridView(mActivity);
				gv.setNumColumns(5);
				gv.setVerticalSpacing(BaseCommonUtils.dip2px(mActivity, 30));
				gv.setPadding(0, BaseCommonUtils.dip2px(mActivity, 15), 0, BaseCommonUtils.dip2px(mActivity, 15));
				gv.setBackgroundColor(mActivity.getResources().getColor(R.color.C1));
				mRootLayout.addView(gv);


				// 填出数据
				MenuItmeAdapter adapter = new MenuItmeAdapter(mActivity);
				gv.setAdapter(adapter);

				adapter.updateData(listVo.get(i).getChildren());
			}

			if (i == 0) {
				SliderLayout sl = new SliderLayout(mActivity);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, BaseCommonUtils.dip2px(mActivity, 120));
				sl.setLayoutParams(params);
				sl.setBackgroundColor(mActivity.getResources().getColor(R.color.C1));
				sl.setPresetTransformer(SliderLayout.Transformer.Default);
				sl.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
				sl.getPagerIndicator().setDefaultIndicatorColor(mActivity.getResources().getColor(R.color.sy_title_color), mActivity.getResources().getColor(R.color.C1));
				sl.setCustomAnimation(new DescriptionAnimation(false));
				// 头部幻灯片
					for (int k = 0; k < mLifeVO.getBanner().size(); k++) {
						LifeVO vo = mLifeVO.getBanner().get(k);
					TextSliderView textSliderView = new TextSliderView(getActivity());
					textSliderView.setOnSliderClickListener(this);
					// textSliderView.setDescriptionLayoutVisible(false);
					textSliderView.description(vo.getTitle()).image(vo.getThumb());
					textSliderView.getBundle().putString("link", vo.getLink());
					textSliderView.getBundle().putString("contentid", vo.getContentid());
					textSliderView.getBundle().putString("suburl", vo.getSuburl());
					sl.addSlider(textSliderView);
				}
				mRootLayout.addView(sl);
			}

		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
	}

	private class ImtemOnclick implements OnClickListener {
		private LifeVO mClickVO;

		public ImtemOnclick(LifeVO vo) {
			this.mClickVO = vo;
		}

		@Override
		public void onClick(View arg0) {
			Bundle bundle = new Bundle();
			bundle.putString("url", mClickVO.getLink());
			bundle.putString("name", mClickVO.getTitle());
			if ("rebellion".equals(mClickVO.getLink())) {
				((BaseActivity) mActivity).openActivity(BrokeActivity.class);
			} else {
				((BaseActivity) mActivity).openActivity(WebViewActivity.class, bundle, 0);
			}
		}
	}

	class MenuItmeAdapter extends BSBaseAdapter {
		private Context context;

		public MenuItmeAdapter(Context context) {
			super(context);
			mOptions = Options.getOptionsDefaultIcon(R.drawable.ic_launcher);
		}

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
				convertView = View.inflate(mActivity, R.layout.life_menu_item, null);
				holder.itemImage = (ImageView) convertView.findViewById(R.id.item_image);
				holder.itemName = (TextView) convertView.findViewById(R.id.item_name);
				holder.itemLayout = (LinearLayout) convertView.findViewById(R.id.item_layout);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			LifeVO vo = (LifeVO) mList.get(position);

			holder.itemName.setText(vo.getTitle());
			mImageLoader.displayImage(vo.getImg(), holder.itemImage, mOptions);
			holder.itemLayout.setOnClickListener(new ImtemOnclick(vo));
			return convertView;
		}
	}

	class LifeItmeAdapter extends BSBaseAdapter {
		private Context context;

		public LifeItmeAdapter(Context context) {
			super(context);
			mOptions = Options.getOptionsDefaultIcon(R.drawable.ic_launcher);
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
				convertView = View.inflate(mActivity, R.layout.life_item, null);
				holder.itemImage = (ImageView) convertView.findViewById(R.id.item_image);
				holder.itemName = (TextView) convertView.findViewById(R.id.item_name);
				holder.typeTv = (TextView) convertView.findViewById(R.id.type_tv);
				holder.pricesTv = (TextView) convertView.findViewById(R.id.price_tv);
				holder.mLifeLayout = (RelativeLayout) convertView.findViewById(R.id.item_layout);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final LifeVO vo = (LifeVO) mList.get(position);

			holder.itemName.setText(vo.getTitle());
			mImageLoader.displayImage(vo.getThumb(), holder.itemImage, mOptions);
			if ("热销".equals(vo.getType())) {
				holder.typeTv.setBackground(BaseCommonUtils.setBackgroundShap(mActivity, 0, R.color.sy_title_color, R.color.sy_title_color));
			} else if ("试吃".equals(vo.getType())) {
				holder.typeTv.setBackground(BaseCommonUtils.setBackgroundShap(mActivity, 0, R.color.C7, R.color.C7));
			} else {
				holder.typeTv.setBackground(BaseCommonUtils.setBackgroundShap(mActivity, 0, R.color.C12, R.color.C12));
			}
			holder.typeTv.setText(vo.getType());
			if(vo.getPrice().equals("免费")){
				holder.pricesTv.setText(vo.getPrice());
			}else{
				holder.pricesTv.setText(vo.getPrice());
			}

			holder.mLifeLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Bundle bundle = new Bundle();
					bundle.putString("url", vo.getUrl());
					bundle.putString("name", vo.getTitle());
					((BaseActivity) mActivity).openActivity(WebViewActivity.class, bundle, 0);
				}
			});
			return convertView;
		}
	}

	class ViewHolder {
		private ImageView itemImage;
		private TextView itemName, typeTv, pricesTv;
		private LinearLayout itemLayout;
		private RelativeLayout mLifeLayout;
	}

	public String getFragmentName() {
		return TAG;// 不知道该方法有没有用
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (!hidden) {
			mActivity.mBaseOkTv.setVisibility(View.GONE);
			mActivity.mBaseHeadLayout.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onSliderClick(BaseSliderView slider) {
		Bundle bundle = slider.getBundle();
		String link = (String) bundle.get("link");
		String contentid = bundle.getString("contentid");
		String suburl = bundle.getString("suburl");
		StartViewUitl.startView(mActivity, suburl, contentid, link, null, null);
	}

	class SamplePagerAdapter extends PagerAdapter {
		private Context mContext;
		// private String[] mPhotoUrls;
		private List<LifeVO> mList;

		public SamplePagerAdapter(Context context) {
			this.mContext = context;
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@SuppressLint("NewApi")
		@Override
		public View instantiateItem(ViewGroup container, final int position) {
			final ImageView imageView = new ImageView(container.getContext());
			imageView.setScaleType(ScaleType.FIT_XY);
			container.addView(imageView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			mActivity.mImageLoader.displayImage(mList.get(position).getThumb(), imageView, mActivity.mOptions);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		public void updateData(List<LifeVO> list) {
			this.mList = list;
			notifyDataSetChanged();

		}

	}
}
