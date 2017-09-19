package com.beisheng.synews.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.activity.MainActivity;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.synews.adapter.BranchAdapter;
import com.beisheng.synews.adapter.CityAdapter;
import com.beisheng.synews.adapter.DragAdapter;
import com.beisheng.synews.adapter.OtherAdapter;
import com.beisheng.synews.application.AppApplication;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.mode.ChannelItem;
import com.beisheng.synews.mode.ChannelManage;
import com.beisheng.synews.utils.LogUtil;
import com.beisheng.synews.view.BranchGridView;
import com.beisheng.synews.view.CityGridView;
import com.beisheng.synews.view.DragGrid;
import com.beisheng.synews.view.OtherGridView;
import com.google.gson.Gson;
import com.im.zhsy.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 频道管理
 */
@SuppressLint("NewApi")
public class ChannelActivity extends BaseActivity implements OnItemClickListener, OnClickListener {
	public static String TAG = "ChannelActivity";
	/** 用户栏目的GRIDVIEW */
	private DragGrid mUserGridView;
	/** 其它栏目的GRIDVIEW */
	private OtherGridView mOtherGridView;
	/**
	 * 城市栏目对应的适配器，可以拖动
	 */
	private CityGridView mCityGridView;
	/**
	 * 部门栏目对应的适配器，可以拖动
	 */
	private BranchGridView mBranchGridView;
	/** 用户栏目对应的适配器，可以拖动 */
	DragAdapter mUserAdapter;
	/** 其它栏目对应的适配器 */
	OtherAdapter mOtherAdapter;
	/** 城市栏目对应的适配器 */
	private CityAdapter mCityAdapter;
	/** 部门栏目对应的适配器 */
	private BranchAdapter mBranchAdapter;
	/**
	 * 其它栏目列表
	 */
	ArrayList<ChannelItem.ListvBean.ListBeanX> otherChannelList = new ArrayList<ChannelItem.ListvBean.ListBeanX>();
	/**
	 * 用户栏目列表
	 */
	ArrayList<ChannelItem.ListvBean.ListBeanX> userChannelList = new ArrayList<ChannelItem.ListvBean.ListBeanX>();
	/**
	 * 城市栏目列表
	 */
	ArrayList<ChannelItem.ListvBean.ListBeanX> cityChannelList = new ArrayList<ChannelItem.ListvBean.ListBeanX>();
	/**
	 * 部门栏目列表
	 */
	ArrayList<ChannelItem.ListvBean.ListBeanX> branchChannelList = new ArrayList<ChannelItem.ListvBean.ListBeanX>();
	/** 是否在移动，由于这边是动画结束后才进行的数据更替，设置这个限制为了避免操作太频繁造成的数据错乱。 */
	boolean isMove = false;
	private ChannelItem mChannelItem;
	private TextView mDeleteTv;
	private boolean mIsDelete = false;
	private List<ChannelItem.ListvBean.ListBeanX> list;
	private TextView city_category_text;
	private TextView branch_category_text;
	private TextView more_category_text;
	private TextView my_category_text;
	private LinearLayout delete_ll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initLocalChannelJson();
		initView();

	}

	private void initLocalChannelJson() {
		Gson gson = new Gson();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("uid", AppApplication.getInstance().getUid());
		String oldStr = "";
		oldStr = getCacheFromDatabase(Constant.CHANNEL_CATEGORY_ULR, map);
		if ("".equals(oldStr)) {
			map.put("uid", null);
			oldStr = getCacheFromDatabase(Constant.CHANNEL_CATEGORY_ULR, map);
		} else {
			oldStr = getCacheFromDatabase(Constant.CHANNEL_CATEGORY_ULR, map);
		}
		mChannelItem = gson.fromJson(oldStr, ChannelItem.class);
		LogUtil.e("initLocalData+mChannelItem", oldStr);
		// my_category_text.setText(mChannelItem.getListv().get(0).getTitle());
		more_category_text.setText(mChannelItem.getListv().get(0).getTitle());
		city_category_text.setText(mChannelItem.getListv().get(1).getTitle());
		branch_category_text.setText(mChannelItem.getListv().get(2).getTitle());
	}

	@Override
	public void baseSetContentView() {
		View.inflate(this, R.layout.channel_activity, mBaseContentLayout);
	}

	@Override
	public boolean getDataResult() {
		return true;
	}

	@Override
	public void initView() {
		mBaseTitleTv.setText("更多栏目");
		mDeleteTv = (TextView) findViewById(R.id.delete_tv);
		mDeleteTv.setBackground(BaseCommonUtils.setBackgroundShap(this, 20, R.color.sy_title_color, R.color.C1));
		delete_ll = (LinearLayout) findViewById(R.id.delete_ll);
		mUserGridView = (DragGrid) findViewById(R.id.userGridView);
		mOtherGridView = (OtherGridView) findViewById(R.id.otherGridView);
		mCityGridView = (CityGridView) findViewById(R.id.cityGridView);
		mBranchGridView = (BranchGridView) findViewById(R.id.branchGridView);
		my_category_text = (TextView) findViewById(R.id.my_category_text);
		more_category_text = (TextView) findViewById(R.id.more_category_text);
		city_category_text = (TextView) findViewById(R.id.city_category_text);
		branch_category_text = (TextView) findViewById(R.id.branch_category_text);
		mUserAdapter = new DragAdapter(this);
		mUserGridView.setAdapter(mUserAdapter);
		mOtherAdapter = new OtherAdapter(this);
		mOtherGridView.setAdapter(mOtherAdapter);
		mCityAdapter = new CityAdapter(this);
		mCityGridView.setAdapter(mCityAdapter);
		mBranchAdapter = new BranchAdapter(this);
		mBranchGridView.setAdapter(mBranchAdapter);
		initData();
	}

	@Override
	public void bindViewsListener() {
		delete_ll.setOnClickListener(this);
		// 设置GRIDVIEW的ITEM的点击监听
		mOtherGridView.setOnItemClickListener(this);
		mUserGridView.setOnItemClickListener(this);
		mBaseBackTv.setOnClickListener(this);
		mCityGridView.setOnItemClickListener(this);
		mBranchGridView.setOnItemClickListener(this);

	}

	/** 初始化数据 */
	private void initData() {
		Intent intent = this.getIntent();
		userChannelList = ((ArrayList<ChannelItem.ListvBean.ListBeanX>) ChannelManage.getManage().getUserChannel());
		otherChannelList = ((ArrayList<ChannelItem.ListvBean.ListBeanX>) ChannelManage.getManage().getOtherChannel());
		cityChannelList = ((ArrayList<ChannelItem.ListvBean.ListBeanX>) ChannelManage.getManage().getCityChannel());
		branchChannelList = ((ArrayList<ChannelItem.ListvBean.ListBeanX>) ChannelManage.getManage().getBranchChannel());
		LogUtil.e("cityChannelList", cityChannelList.toString());
		mUserAdapter.updateData(userChannelList);
		mOtherAdapter.updateData(otherChannelList);
		if (cityChannelList.size() != 0) {

			if (TextUtils.isEmpty(cityChannelList.get(0).getTitle())) {

			} else {
				mCityAdapter.updateData(cityChannelList);
				//
			}

		}
		if (branchChannelList.size() != 0) {
			if (TextUtils.isEmpty(branchChannelList.get(0).getTitle()) || branchChannelList.size() == 0) {
				// 空

			} else {
				mBranchAdapter.updateData(branchChannelList);

			}

		}

	}

	/** GRIDVIEW对应的ITEM点击监听接口 */
	@Override
	public void onItemClick(AdapterView<?> adapterView, final View view, final int position, long id) {
		if (adapterView.getAdapter().getCount() == 0)
			return;
		// 如果点击的时候，之前动画还没结束，那么就让点击事件无效
		ChannelItem.ListvBean.ListBeanX vo = (ChannelItem.ListvBean.ListBeanX) adapterView.getAdapter().getItem(position);
		if (vo == null)
			return;
		if (isMove) {
			return;
		}
		switch (adapterView.getId()) {
		case R.id.userGridView:
			// position为 0，1 的不可以进行任何操作

			if ("0".equals(vo.getFixed()) && mIsDelete) {
				if ("1".equals(vo.getIsdel())) {

				} else {

					final ImageView moveImageView = getView(view);
					if (moveImageView != null) {
						TextView newTextView = (TextView) view.findViewById(R.id.text_item);
						final int[] startLocation = new int[2];
						newTextView.getLocationInWindow(startLocation);
						final ChannelItem.ListvBean.ListBeanX channel = ((DragAdapter) adapterView.getAdapter()).getItem(position);// 获取点击的频道内容
						if (getUserChangeCityAdd(channel)) {
							mCityAdapter.setVisible(false);
							// 添加到最后一个
							mCityAdapter.addItem(channel);
							new Handler().postDelayed(new Runnable() {
								public void run() {
									try {
										int[] endLocation = new int[2];
										// 获取终点的坐标
										mCityGridView.getChildAt(mCityGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
										MoveAnim(moveImageView, startLocation, endLocation, channel, mUserGridView);
										mUserAdapter.setRemove(position);
									} catch (Exception localException) {
									}
								}
							}, 50L);
						} else if (getUserChangeBranchAdd(channel)) {
							mBranchAdapter.setVisible(false);
							// 添加到最后一个
							mBranchAdapter.addItem(channel);
							new Handler().postDelayed(new Runnable() {
								public void run() {
									try {
										int[] endLocation = new int[2];
										// 获取终点的坐标
										mBranchGridView.getChildAt(mBranchGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
										MoveAnim(moveImageView, startLocation, endLocation, channel, mUserGridView);
										mUserAdapter.setRemove(position);
									} catch (Exception localException) {
									}
								}
							}, 50L);
						} else {
							mOtherAdapter.setVisible(false);
							// 添加到最后一个
							mOtherAdapter.addItem(channel);
							new Handler().postDelayed(new Runnable() {
								public void run() {
									try {
										int[] endLocation = new int[2];
										// 获取终点的坐标
										mOtherGridView.getChildAt(mOtherGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
										MoveAnim(moveImageView, startLocation, endLocation, channel, mUserGridView);
										mUserAdapter.setRemove(position);
									} catch (Exception localException) {
									}
								}
							}, 50L);
						}
					}
				}
			} else if ("0".equals(vo.getFixed()) && !mIsDelete) {
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				if (mUserAdapter.isListChanged()) {
					ChannelManage.getManage().saveUserChannel(mUserAdapter.channelList);
					ChannelManage.getManage().saveOtherChannel(mOtherAdapter.channelList);
					ChannelManage.getManage().saveCityChannel(mCityAdapter.channelList);
					ChannelManage.getManage().saveBranchChannel(mBranchAdapter.channelList);
					intent.putExtra("ischange", true);
				}
				intent.putExtra("islink", mUserAdapter.channelList.get(position).getIslink());
				intent.putExtra("linkurl", mUserAdapter.channelList.get(position).getLinkurl());
				intent.putExtra("position", position);
				setResult(1, intent);
				finish();
			}
			break;
		case R.id.otherGridView:
			final ImageView moveImageView = getView(view);
			if (moveImageView != null) {
				TextView newTextView = (TextView) view.findViewById(R.id.text_item);
				final int[] startLocation = new int[2];
				newTextView.getLocationInWindow(startLocation);
				final ChannelItem.ListvBean.ListBeanX channel = ((OtherAdapter) adapterView.getAdapter()).getItem(position);
				channel.setSelected(1);
				mUserAdapter.setVisible(false);
				// 添加到最后一个
				mUserAdapter.addItem(channel);
				new Handler().postDelayed(new Runnable() {
					public void run() {
						try {
							int[] endLocation = new int[2];
							// 获取终点的坐标
							mUserGridView.getChildAt(mUserGridView.getLastVisiblePosition() - 4).getLocationInWindow(endLocation);
							MoveAnim(moveImageView, startLocation, endLocation, channel, mOtherGridView);
							mOtherAdapter.setRemove(position);
						} catch (Exception localException) {
						}
					}
				}, 50L);
			}
			break;
		case R.id.cityGridView:
			LogUtil.e("cityGridView", "|||||||||||||||");
			final ImageView cityImageView = getView(view);
			if (cityImageView != null) {
				TextView newTextView = (TextView) view.findViewById(R.id.text_item);
				final int[] startLocation = new int[2];
				newTextView.getLocationInWindow(startLocation);
				final ChannelItem.ListvBean.ListBeanX channel = ((CityAdapter) adapterView.getAdapter()).getItem(position);
				channel.setSelected(1);
				mUserAdapter.setVisible(false);
				// 添加到最后一个
				mUserAdapter.addItem(channel);
				new Handler().postDelayed(new Runnable() {
					public void run() {
						try {
							int[] endLocation = new int[2];
							// 获取终点的坐标
							// for(int
							// i=0;i<=mCityAdapter.getChannnelLst().size();i++){
							// count=i
							// } int count;
							//
							mUserGridView.getChildAt(mUserGridView.getLastVisiblePosition() - 4).getLocationInWindow(endLocation);
							MoveAnim(cityImageView, startLocation, endLocation, channel, mCityGridView);
							mCityAdapter.setRemove(position);
						} catch (Exception localException) {
						}
					}
				}, 50L);
			}
			break;
		case R.id.branchGridView:
			LogUtil.e("branchGridView", "|||||||||||||||");
			final ImageView branchImageView = getView(view);
			if (branchImageView != null) {
				TextView newTextView = (TextView) view.findViewById(R.id.text_item);
				final int[] startLocation = new int[2];
				newTextView.getLocationInWindow(startLocation);
				final ChannelItem.ListvBean.ListBeanX channel = ((BranchAdapter) adapterView.getAdapter()).getItem(position);
				channel.setSelected(1);
				mUserAdapter.setVisible(false);
				// 添加到最后一个
				mUserAdapter.addItem(channel);
				new Handler().postDelayed(new Runnable() {
					public void run() {
						try {
							int[] endLocation = new int[2];
							// 获取终点的坐标
							mUserGridView.getChildAt(mUserGridView.getLastVisiblePosition() - 4).getLocationInWindow(endLocation);
							MoveAnim(branchImageView, startLocation, endLocation, channel, mBranchGridView);
							mBranchAdapter.setRemove(position);
						} catch (Exception localException) {
						}
					}
				}, 50L);
			}
			break;
		default:
			break;
		}
	}

	private String title;

	public boolean getUserChangeCityAdd(ChannelItem.ListvBean.ListBeanX channel) {
		list = mChannelItem.getListv().get(1).getList();
		for (int i = 0; i < list.size(); i++) {
			title = list.get(i).getTitle();
			LogUtil.e("getUserChangeCityAdd", title);
			if (channel.getTitle().equals(title)) {

				return true;
			}
		}

		return false;
	}

	public boolean getUserChangeBranchAdd(ChannelItem.ListvBean.ListBeanX channel) {
		list = mChannelItem.getListv().get(2).getList();
		for (int i = 0; i < list.size(); i++) {
			title = list.get(i).getTitle();
			LogUtil.e("getUserChangeBranchAdd", title);
			if (channel.getTitle().equals(title)) {

				return true;
			}

		}
		return false;
	}

	// 点击ITEM移动动画
	private void MoveAnim(View moveView, int[] startLocation, int[] endLocation, final ChannelItem.ListvBean.ListBeanX moveChannel, final GridView clickGridView) {
		int[] initLocation = new int[2];
		// 获取传递过来的VIEW的坐标
		moveView.getLocationInWindow(initLocation);
		// 得到要移动的VIEW,并放入对应的容器中
		final ViewGroup moveViewGroup = getMoveViewGroup();
		final View mMoveView = getMoveView(moveViewGroup, moveView, initLocation);
		// 创建移动动画
		TranslateAnimation moveAnimation = new TranslateAnimation(startLocation[0], endLocation[0], startLocation[1], endLocation[1]);
		moveAnimation.setDuration(300L);// 动画时间
		// 动画配置
		AnimationSet moveAnimationSet = new AnimationSet(true);
		moveAnimationSet.setFillAfter(false);// 动画效果执行完毕后，View对象不保留在终止的位置
		moveAnimationSet.addAnimation(moveAnimation);
		mMoveView.startAnimation(moveAnimationSet);
		moveAnimationSet.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				isMove = true;
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				moveViewGroup.removeView(mMoveView);
				// instanceof 方法判断2边实例是不是一样，判断点击的是DragGrid还是OtherGridView
				if (clickGridView instanceof DragGrid) {
					mOtherAdapter.setVisible(true);
					mOtherAdapter.notifyDataSetChanged();
					mCityAdapter.setVisible(true);
					mCityAdapter.notifyDataSetChanged();
					mBranchAdapter.setVisible(true);
					mBranchAdapter.notifyDataSetChanged();
					mUserAdapter.remove();
				} else if (clickGridView instanceof OtherGridView) {
					mUserAdapter.setVisible(true);
					mUserAdapter.notifyDataSetChanged();
					mOtherAdapter.remove();

				} else if (clickGridView instanceof CityGridView) {
					mUserAdapter.setVisible(true);
					mUserAdapter.notifyDataSetChanged();
					mCityAdapter.remove();
					LogUtil.e("cityGridView", "|||||||||||||||");

				} else {
					mUserAdapter.setVisible(true);
					mUserAdapter.notifyDataSetChanged();
					mBranchAdapter.remove();
					LogUtil.e("branchGridView", "|||||||||||||||");
				}
				isMove = false;
			}
		});
	}

	// 获取移动的VIEW，放入对应ViewGroup布局容器
	private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
		int x = initLocation[0];
		int y = initLocation[1];
		viewGroup.addView(view);
		LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mLayoutParams.leftMargin = x;
		mLayoutParams.topMargin = y;
		view.setLayoutParams(mLayoutParams);
		return view;
	}

	// 创建移动的ITEM对应的ViewGroup布局容器
	private ViewGroup getMoveViewGroup() {
		ViewGroup moveViewGroup = (ViewGroup) getWindow().getDecorView();
		LinearLayout moveLinearLayout = new LinearLayout(this);
		moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		moveViewGroup.addView(moveLinearLayout);
		return moveLinearLayout;
	}

	// 获取点击的Item的对应View，
	private ImageView getView(View view) {
		view.destroyDrawingCache();
		view.setDrawingCacheEnabled(true);
		Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
		view.setDrawingCacheEnabled(false);
		ImageView iv = new ImageView(this);
		iv.setImageBitmap(cache);
		return iv;
	}

	@Override
	public void onBackPressed() {
		if (mUserAdapter.isListChanged()) {
			ChannelManage.getManage().saveUserChannel(mUserAdapter.channelList);
			ChannelManage.getManage().saveOtherChannel(mOtherAdapter.channelList);
			ChannelManage.getManage().saveCityChannel(mCityAdapter.channelList);
			ChannelManage.getManage().saveBranchChannel(mBranchAdapter.channelList);
			Intent intent = new Intent();
			intent.putExtra("ischange", true);
			setResult(1, intent);
			finish();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.delete_ll:
			if (mIsDelete) {
				mIsDelete = false;
				mUserAdapter.setDelete(mIsDelete);
				mDeleteTv.setText("点击编辑");
			} else {
				mIsDelete = true;
				mUserAdapter.setDelete(mIsDelete);
				mDeleteTv.setText("   完成   ");
			}
			mUserAdapter.notifyDataSetChanged();
			break;

		case R.id.base_back_tv:
			if (mUserAdapter.isListChanged()) {
				ChannelManage.getManage().saveUserChannel(mUserAdapter.channelList);
				ChannelManage.getManage().saveOtherChannel(mOtherAdapter.channelList);
				ChannelManage.getManage().saveCityChannel(mCityAdapter.channelList);
				ChannelManage.getManage().saveBranchChannel(mBranchAdapter.channelList);
				Intent intent = new Intent();
				intent.putExtra("ischange", true);
				setResult(1, intent);
			}
			finish();
		default:
			break;
		}

	}

}
