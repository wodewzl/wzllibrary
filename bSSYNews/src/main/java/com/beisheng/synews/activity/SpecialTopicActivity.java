package com.beisheng.synews.activity;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckedTextView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.http.HttpClientUtil;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.BitmapUtil;
import com.beisheng.base.utils.ThreadUtil;
import com.beisheng.synews.adapter.SpecicalTopicGVAdapter;
import com.beisheng.synews.adapter.SpecicalTopicLVAdapter;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.interfaces.LoadMoreListener;
import com.beisheng.synews.mode.SpecialTopicVO;
import com.beisheng.synews.utils.StartViewUitl;
import com.google.gson.Gson;
import com.im.zhsy.R;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class SpecialTopicActivity extends BaseActivity implements OnRefreshListener, LoadMoreListener, OnItemClickListener {
	private GridView mGridView;
	private SpecicalTopicGVAdapter mGVAdapter;
	private ListView mListView;
	private SpecicalTopicLVAdapter mLVAdapter;
	private SpecialTopicVO mSpecialTopicVO;
	public String mPage = "1";// 用来存储数据的，1为默认第一页，不是只有一页
	private int mState = 0; // 0为首次,1为下拉刷新 ，2为加载更多
	private ImageView mHeadImg;
	public boolean mIsFrist = true;// 第一次加载是一个接口，点击类型是另外个接口
	private String mContentid;
	public String mTypeid;
	private boolean mLodMore = true;// 控制多次加载，即当前加载就不在会加载直到结束
	private TextView mDescTv;
	private LinearLayout mKeywordLayout;
	private TextView mLastOnclickTv;

	@Override
	public void baseSetContentView() {
		View.inflate(this, R.layout.special_topic_activity, mBaseContentLayout);
	}

	@Override
	public boolean getDataResult() {
		return getData();
	}

	@Override
	public void initView() {
		mBaseTitleTv.setText("专题");
		mHeadImg = (ImageView) findViewById(R.id.head_img);
		mDescTv = (TextView) findViewById(R.id.desc_tv);
		mGridView = (GridView) findViewById(R.id.type_gv);
		mGVAdapter = new SpecicalTopicGVAdapter(this);
		mGridView.setAdapter(mGVAdapter);
		mListView = (ListView) findViewById(R.id.type_lv);
		mLVAdapter = new SpecicalTopicLVAdapter(this);
		// AnimationAdapter animationAdapter = new
		// CardsAnimationAdapter(mLVAdapter);
		// animationAdapter.setAbsListView(mListView);
		mListView.setAdapter(mLVAdapter);
		mKeywordLayout = (LinearLayout) findViewById(R.id.keyword_layout);
		initData();

	}

	public void initData() {
		mContentid = this.getIntent().getStringExtra("id");
	}

	@Override
	public void bindViewsListener() {
		mGridView.setOnItemClickListener(this);
		mListView.setOnItemClickListener(this);
	}

	public boolean getData() {
		try {
			Gson gson = new Gson();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("page", mPage);

			String url = "";
			if (mIsFrist) {
				url = Constant.SPECIAL_TOPIC_URL;
				map.put("contentid", mContentid);
			} else {
				url = Constant.SPECIAL_TOPIC_TYPE_URL;
				map.put("typeid", mTypeid);
			}

			if (hasNetWork()) {
				String jsonStr = HttpClientUtil.getRequest(this, Constant.DOMAIN_NAME + url, map);
				mSpecialTopicVO = gson.fromJson(jsonStr, SpecialTopicVO.class);
				saveJsonCache(url, map, jsonStr);
			} else {
				String oldStr = getCacheFromDatabase(url, map);
				mSpecialTopicVO = gson.fromJson(oldStr, SpecialTopicVO.class);
			}
			if (Constant.RESULT_SUCCESS_CODE.equals(mSpecialTopicVO.getCode())) {
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void onRefresh() {
		mState = 1;
		mPage = "1";
		new ThreadUtil(this, this).start();
	}

	@Override
	public void loadMore() {
		if (mLodMore) {
			mLodMore = false;
			mState = 2;
			mPage = (BaseCommonUtils.parseInt(mPage) + 1) + "";
			new ThreadUtil(this, this).start();
		}
	}

	@Override
	public void executeSuccess() {
		super.executeSuccess();
		mLodMore = true;
		if (mIsFrist) {
			mIsFrist = false;
			mDescTv.setText(mSpecialTopicVO.getInfo());
			mImageLoader.displayImage(mSpecialTopicVO.getThumb(), mHeadImg, mOptions, new ImageLoadingListener() {

				@Override
				public void onLoadingStarted(String arg0, View arg1) {

				}

				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {

				}

				@Override
				public void onLoadingComplete(String arg0, View arg1, Bitmap bitmap) {
					mHeadImg.setImageBitmap(BitmapUtil.zoomImg(bitmap, BaseCommonUtils.getScreenWid(SpecialTopicActivity.this)));
				}

				@Override
				public void onLoadingCancelled(String arg0, View arg1) {

				}
			});

			// mGVAdapter.updateData(mSpecialTopicVO.getCate());
			String[] arrayItem = new String[mSpecialTopicVO.getCate().size()];
			for (int i = 0; i < mSpecialTopicVO.getCate().size(); i++) {
				arrayItem[i] = mSpecialTopicVO.getCate().get(i).getId() + "," + mSpecialTopicVO.getCate().get(i).getTitle();
			}
			showKeywordLayout(arrayItem);

			mLVAdapter.updateData(mSpecialTopicVO.getList());
		} else {
			mLVAdapter.updateData(mSpecialTopicVO.getList());
		}

	}

	@Override
	public void executeFailure() {
		super.executeFailure();
		mLodMore = true;
		if (mSpecialTopicVO != null)
			showCustomToast(mSpecialTopicVO.getRetinfo());
		else
			showCustomToast("亲，请检查网络哦");
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int arg2, long arg3) {
		if (adapterView.getAdapter().getCount() == 0)
			return;
		if (adapterView == mGridView) {
			SpecialTopicVO vo = mGVAdapter.mList.get((int) arg3);
			if (vo == null)
				return;
			mTypeid = vo.getId();
			mGVAdapter.notifyDataSetChanged();
			CheckedTextView tv = (CheckedTextView) view.findViewById(R.id.type_name_tv);
			tv.setChecked(true);
		} else {
			SpecialTopicVO vo = (SpecialTopicVO) adapterView.getAdapter().getItem(arg2);
			if (vo == null)
				return;
			StartViewUitl.startView(this, vo.getSuburl(), vo.getContentid(), null, null, vo.getTypename());
		}

	}

	@SuppressLint("NewApi")
	public void showKeywordLayout(String[] keywordArray) {
		mKeywordLayout.removeAllViews();
		LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		for (int i = 0; i < keywordArray.length; i++) {
			textViewParams.setMargins(0, 0, BaseCommonUtils.dip2px(SpecialTopicActivity.this, 15), BaseCommonUtils.dip2px(SpecialTopicActivity.this, 15));
			final TextView childTv = new TextView(SpecialTopicActivity.this);
			childTv.setPadding(BaseCommonUtils.dip2px(SpecialTopicActivity.this, 10), BaseCommonUtils.dip2px(SpecialTopicActivity.this, 5), BaseCommonUtils.dip2px(SpecialTopicActivity.this, 10),
					BaseCommonUtils.dip2px(SpecialTopicActivity.this, 5));
			childTv.setLayoutParams(textViewParams);
			childTv.setText(keywordArray[i].split(",")[1]);
			childTv.setBackground(BaseCommonUtils.setBackgroundShap(SpecialTopicActivity.this, 20, "#DDDDDD", "#F8F8F8"));
			childTv.setTextColor(Color.parseColor("#818181"));
			final String typeId = keywordArray[i].split(",")[0];
			childTv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					childTv.setBackground(BaseCommonUtils.setBackgroundShap(SpecialTopicActivity.this, 20, "#B10304", "#B10304"));
					childTv.setTextColor(Color.parseColor("#ffffff"));
					if (mLastOnclickTv != null) {
						mLastOnclickTv.setBackground(BaseCommonUtils.setBackgroundShap(SpecialTopicActivity.this, 20, "#DDDDDD", "#F8F8F8"));
						mLastOnclickTv.setTextColor(Color.parseColor("#818181"));
					}
					mLastOnclickTv = childTv;

					// Bundle bundle = new Bundle();
					// bundle.putString("keyword",
					// childTv.getText().toString());
					// openActivity(KeyWordActivity.class, bundle, 0);
					mTypeid = typeId;
					SpecialTopicActivity.this.showProgressDialog();
					new ThreadUtil(SpecialTopicActivity.this, SpecialTopicActivity.this).start();
				}
			});
			mKeywordLayout.addView(childTv);
		}
	}
}
