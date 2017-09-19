package com.beisheng.synews.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.adapter.BSBaseAdapter;
import com.beisheng.base.fragment.BaseFragment;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.Options;
import com.beisheng.base.view.BSFlowIndicator;
import com.beisheng.base.view.BSGridView;
import com.beisheng.synews.activity.BrokeActivity;
import com.beisheng.synews.activity.WebViewActivity;
import com.beisheng.synews.mode.LifeVO;
import com.im.zhsy.R;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class LifeItemFragment extends BaseFragment implements OnPageChangeListener {
	private String TAG = "LifeFragment";
	private BaseActivity mActivity;

	private BSFlowIndicator mFlowIndicator;
	private SamplePagerAdapter mPagerAdapter;
	private ViewPager mViewPager;
	private int mImgIndex = 0;
	private List<List<LifeVO>> mList = new ArrayList<List<LifeVO>>();

	public LifeItemFragment(List<List<LifeVO>> list) {
		this.mList = list;
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
		View view = inflater.inflate(R.layout.life_item_fragment, container, false);
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
		// new ThreadUtil(mActivity, this).start();
	}

	@Override
	public String getFragmentName() {
		return null;
	}

	public void initViews(View view) {
		mFlowIndicator = (BSFlowIndicator) view.findViewById(R.id.galleryIndicator);
		mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
		mPagerAdapter = new SamplePagerAdapter(mActivity, mList);
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setOnPageChangeListener(this);
	}

	class SamplePagerAdapter extends PagerAdapter {
		private Context mContext;
		// private String[] mPhotoUrls;
		private List<List<LifeVO>> mList;

		public SamplePagerAdapter(Context context, List<List<LifeVO>> list) {
			this.mContext = context;
			mList = list;
			mFlowIndicator.setCount(mList.size());
			mFlowIndicator.setSeletion(0);
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@SuppressLint("NewApi")
		@Override
		public View instantiateItem(ViewGroup container, final int position) {
			BSGridView gv = new BSGridView(mActivity);
			gv.setNumColumns(5);
			gv.setVerticalSpacing(BaseCommonUtils.dip2px(mActivity, 30));
			gv.setPadding(0, BaseCommonUtils.dip2px(mActivity, 15), 0, 15);
			gv.setBackgroundColor(mActivity.getResources().getColor(R.color.C1));
			container.addView(gv, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			container.setBackgroundColor(mActivity.getResources().getColor(R.color.C1));
			MenuItmeAdapter adapter = new MenuItmeAdapter(mActivity);
			gv.setAdapter(adapter);
			adapter.updateData(mList.get(position));
			return gv;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		public void updateData(String[] photoUrl) {
			notifyDataSetChanged();

		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		mFlowIndicator.setSeletion(arg0);
		mImgIndex = arg0;
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

	class ViewHolder {
		private ImageView itemImage;
		private TextView itemName, typeTv, pricesTv;
		private LinearLayout itemLayout;
		private RelativeLayout mLifeLayout;
	}

	class ImtemOnclick implements OnClickListener {
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

}
