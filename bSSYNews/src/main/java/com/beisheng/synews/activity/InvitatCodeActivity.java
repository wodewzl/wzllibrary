package com.beisheng.synews.activity;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.http.HttpClientUtil;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.ThreadUtil;
import com.beisheng.base.view.BSTopIndicator;
import com.beisheng.base.view.BSTopIndicator.OnTopIndicatorListener;
import com.beisheng.synews.adapter.InvitatCodeAdapter;
import com.beisheng.synews.application.AppApplication;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.interfaces.LoadMoreListener;
import com.beisheng.synews.mode.InvitatCodeVO;
import com.beisheng.synews.utils.ShareUtil;
import com.beisheng.synews.view.BSListViewLoadMore;
import com.bs.bsims.observer.BSApplictionObserver.Watcher;
import com.bs.bsims.observer.MessageMyHeadImp;
import com.google.gson.Gson;
import com.im.zhsy.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.HashMap;

public class InvitatCodeActivity extends BaseActivity implements OnTopIndicatorListener, OnClickListener, Watcher, LoadMoreListener {
	private BSTopIndicator mTopIndicator;
	private int[] mDrawableIds = new int[] { 0, 0, 0 };
	private CharSequence[] mLabels = new CharSequence[] { "输入邀请码", "我的邀请码", "邀请记录" };
	private LinearLayout mTabLayout01, mTabLayout0101, mTabLayout0102, mTabLayout02, mTabLayout0201, mTabLayout0202, mTabLayout03, mTabLayout0301, mTabLayout0302;
	private TextView mCommitTv, mMyCodeTv, mShareTv, mLoginTv, mCountTv, mTab01ShareTv, mTab03ShareTv;
	private EditText mCodeEt;
	private BSListViewLoadMore mListView;
	private InvitatCodeAdapter mAdapter;
	private boolean mCommitFlag = true;
	private int mCurrentIndex = 0;
	private InvitatCodeVO mMyCode, mIsInput, mListVO;
	private ImageView mMyCodeImg;
	private String mPage = "1";
	private int mState = 0; // 0为首次,1为下拉刷新 ，2为加载更多
	private boolean mLodMore = true;// 控制多次加载，即当前加载就不在会加载直到结束

	@Override
	public void baseSetContentView() {
		View.inflate(this, R.layout.invite_code_activity, mBaseContentLayout);
	}

	@Override
	public boolean getDataResult() {
		return getData();
	}

	@SuppressLint("NewApi")
	@Override
	public void initView() {
		mBaseTitleTv.setText("邀请安装");
		mTopIndicator = (BSTopIndicator) findViewById(R.id.top_indicator);
		mTopIndicator.setmLabels(mLabels);
		mTopIndicator.setDrawableIds(mDrawableIds);
		mTopIndicator.setCheckColor(this.getResources().getColor(R.color.sy_title_color));
		mTopIndicator.updateUI(this);

		mTabLayout01 = (LinearLayout) this.findViewById(R.id.tab_layout_01);
		mTabLayout0101 = (LinearLayout) this.findViewById(R.id.tab_layout_01_01);
		mTabLayout0102 = (LinearLayout) this.findViewById(R.id.tab_layout_01_02);
		mTabLayout02 = (LinearLayout) this.findViewById(R.id.tab_layout_02);
		mTabLayout0201 = (LinearLayout) this.findViewById(R.id.tab_layout_02_01);
		mTabLayout0202 = (LinearLayout) this.findViewById(R.id.tab_layout_02_02);
		mTabLayout03 = (LinearLayout) this.findViewById(R.id.tab_layout_03);
		mTabLayout0301 = (LinearLayout) this.findViewById(R.id.tab_layout_03_01);
		mTabLayout0302 = (LinearLayout) this.findViewById(R.id.tab_layout_03_02);
		mCodeEt = (EditText) findViewById(R.id.code_et);
		mMyCodeTv = (TextView) findViewById(R.id.my_code_tv);
		mMyCodeImg = (ImageView) findViewById(R.id.my_code_img);
		mCommitTv = (TextView) this.findViewById(R.id.commit_tv);
		mLoginTv = (TextView) findViewById(R.id.login_tv);
		mShareTv = (TextView) findViewById(R.id.share_tv);
		mCountTv = (TextView) findViewById(R.id.count_tv);
		mTab01ShareTv = (TextView) findViewById(R.id.tab01_share_tv);
		mTab03ShareTv = (TextView) findViewById(R.id.tab03_share_tv);

		mListView = (BSListViewLoadMore) findViewById(R.id.list_view);
		mAdapter = new InvitatCodeAdapter(this);
		mListView.setAdapter(mAdapter);
		// mCommitTv.setBackground(BaseCommonUtils.setBackgroundShapTwo(this,
		// 10, "#ff0000",
		// "#00ff00", "#0000ff"));

		mCommitTv.setBackground(BaseCommonUtils.setBackgroundShap(this, 10, R.color.devider_bg, R.color.C1));
		mShareTv.setBackground(BaseCommonUtils.setBackgroundShap(this, 10, R.color.devider_bg, R.color.C1));

		initData();
	}

	@Override
	public void bindViewsListener() {
		mTopIndicator.setOnTopIndicatorListener(this);
		mCommitTv.setOnClickListener(this);
		mLoginTv.setOnClickListener(this);
		mShareTv.setOnClickListener(this);
		mListView.setLoadMoreListener(this);
		mTab01ShareTv.setOnClickListener(this);
		mTab03ShareTv.setOnClickListener(this);
		MessageMyHeadImp.getInstance().add(this);

	}

	@Override
	public void onIndicatorSelected(int index) {
		mTopIndicator.setTabsDisplay(this, index);
		mCurrentIndex = index;
		switch (index) {
		case 0:
			mTabLayout01.setVisibility(View.VISIBLE);
			mTabLayout02.setVisibility(View.GONE);
			mTabLayout03.setVisibility(View.GONE);
			break;
		case 1:
			mTabLayout01.setVisibility(View.GONE);
			mTabLayout02.setVisibility(View.VISIBLE);
			mTabLayout03.setVisibility(View.GONE);
			if (AppApplication.getInstance().getUserInfoVO() == null) {
				mTabLayout0201.setVisibility(View.GONE);
				mTabLayout0202.setVisibility(View.VISIBLE);
			} else {
				mTabLayout0201.setVisibility(View.VISIBLE);
				mTabLayout0202.setVisibility(View.GONE);
				getLocalMyCode();
			}
			break;
		case 2:
			mTabLayout01.setVisibility(View.GONE);
			mTabLayout02.setVisibility(View.GONE);
			mTabLayout03.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
	}

	public void commitCode() {
		mCommitFlag = false;
		showProgressDialog();
		RequestParams params = new RequestParams();
		params.put("inicode", mCodeEt.getText().toString());
		params.put("mobile", BaseCommonUtils.getPhoneImei(this));
		String url = Constant.DOMAIN_NAME + Constant.INVITAT_CODE_COMMIT;
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				mCommitFlag = true;
				dismissProgressDialog();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				String result = new String(arg2);
				dismissProgressDialog();
				mCommitFlag = true;
				try {
					JSONObject jsonObject = new JSONObject(new String(arg2));
					String str = (String) jsonObject.get("retinfo");
					String code = (String) jsonObject.get("code");
					if (Constant.RESULT_SUCCESS_CODE.equals(code)) {
						showCustomToast(str);
						mTabLayout0101.setVisibility(View.GONE);
						mTabLayout0102.setVisibility(View.VISIBLE);
					} else {
						showCustomToast(str);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.commit_tv:
			if (mCodeEt.getText().toString().trim().length() == 0) {
				showCustomToast("请填写邀请码！");
				return;
			}

			commitCode();
			break;
		case R.id.tab03_share_tv:
		case R.id.tab01_share_tv:
			if (AppApplication.getInstance().getUserInfoVO() == null) {
				openActivity(LoginActivity.class);
			} else {
				if (mMyCode != null)
					ShareUtil.share(this, mMyCode.getShare_img(), mMyCode.getShare_tit(), mMyCode.getShare_des(), mMyCode.getShare_url());
			}
			break;

		case R.id.login_tv:
			openActivity(LoginActivity.class);
			break;

		case R.id.share_tv:
			ShareUtil.share(this, mMyCode.getShare_img(), mMyCode.getShare_tit(), mMyCode.getShare_des(), mMyCode.getShare_url());
			break;

		default:
			break;
		}
	}

	@Override
	public void updateNotify(Object content) {
		if (content == null)
			return;
		if (mCurrentIndex == 1) {
			mTabLayout0201.setVisibility(View.VISIBLE);
			mTabLayout0202.setVisibility(View.GONE);
		}

		getMyCode();
	}

	public boolean getLocalMyCode() {
		Gson gson = new Gson();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("uid", AppApplication.getInstance().getUid());
		String oldStr = getCacheFromDatabase(Constant.DOMAIN_NAME + Constant.INVITAT_CODE_MY, map);
		mMyCode = gson.fromJson(oldStr, InvitatCodeVO.class);
		if (mMyCode != null && Constant.RESULT_SUCCESS_CODE.equals(mMyCode.getCode())) {
			mImageLoader.displayImage(mMyCode.getThumb(), mMyCodeImg, mOptions);
			BaseCommonUtils.setTextTwoBefore(InvitatCodeActivity.this, mMyCodeTv, "邀请码：", mMyCode.getInicode(), R.color.C4, 1.2f);
			return true;
		}
		return false;
	}

	public void initData() {
		// 后台获取是否加输入过邀请码
		new Thread() {
			public void run() {
				getIsInput();
			};
		}.start();

		// 后台加载我的邀请码
		if (AppApplication.getInstance().getUid() != null) {
			new Thread() {
				public void run() {
					getMyCode();
				};
			}.start();
		}

	}

	// 获取邀请码
	public void getMyCode() {
		if (getLocalMyCode()) {
			return;
		}
		Gson gson = new Gson();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("uid", AppApplication.getInstance().getUid());
		String jsonStr = HttpClientUtil.getRequest(this, Constant.DOMAIN_NAME + Constant.INVITAT_CODE_MY, map);
		mMyCode = gson.fromJson(jsonStr, InvitatCodeVO.class);
		saveJsonCache(Constant.DOMAIN_NAME + Constant.INVITAT_CODE_MY, map, jsonStr);
		if (mMyCode != null && Constant.RESULT_SUCCESS_CODE.equals(mMyCode.getCode())) {
			mTabLayout02.post(new Runnable() {
				@Override
				public void run() {
					mImageLoader.displayImage(mMyCode.getThumb(), mMyCodeImg, mOptions);
					BaseCommonUtils.setTextTwoBefore(InvitatCodeActivity.this, mMyCodeTv, "邀请码：", mMyCode.getInicode(), R.color.C4, 1.2f);
				}

			});
		}
	}

	// 获取邀请列表
	public boolean getData() {
		Gson gson = new Gson();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("uid", AppApplication.getInstance().getUid());
		map.put("page", mPage);
		if (hasNetWork()) {
			String jsonStr = HttpClientUtil.getRequest(this, Constant.DOMAIN_NAME + Constant.INVITAT_CODE_LIST, map);
			mListVO = gson.fromJson(jsonStr, InvitatCodeVO.class);
			saveJsonCache(Constant.DOMAIN_NAME + Constant.INVITAT_CODE_LIST, map, jsonStr);
		} else {
			String oldStr = getCacheFromDatabase(Constant.DOMAIN_NAME + Constant.INVITAT_CODE_LIST, map);
			mListVO = gson.fromJson(oldStr, InvitatCodeVO.class);
		}

		if (mListVO != null && Constant.RESULT_SUCCESS_CODE.equals(mListVO.getCode())) {
			return true;
		}
		return false;
	}

	// 获取是否输入过邀请码
	public void getIsInput() {
		Gson gson = new Gson();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("mobile", BaseCommonUtils.getPhoneImei(this));

		// String oldStr = getCacheFromDatabase(Constant.DOMAIN_NAME +
		// Constant.INVITAT_CODE_MY, map);
		// mIsInput = gson.fromJson(oldStr, InvitatCodeVO.class);
		// if (mIsInput != null &&
		// Constant.RESULT_SUCCESS_CODE.equals(mIsInput.getCode())) {
		// if ("2".equals(mIsInput.getInput())) {
		// mTabLayout0101.setVisibility(View.GONE);
		// mTabLayout0102.setVisibility(View.VISIBLE);
		// } else {
		// mTabLayout0101.setVisibility(View.VISIBLE);
		// mTabLayout0102.setVisibility(View.GONE);
		// }
		// return;
		// }

		String jsonStr = HttpClientUtil.getRequest(this, Constant.DOMAIN_NAME + Constant.INVITAT_CODE_IS_INPUT, map);
		mIsInput = gson.fromJson(jsonStr, InvitatCodeVO.class);
		saveJsonCache(Constant.DOMAIN_NAME + Constant.INVITAT_CODE_MY, map, jsonStr);
		if (mIsInput != null && Constant.RESULT_SUCCESS_CODE.equals(mIsInput.getCode())) {
			mTabLayout01.post(new Runnable() {
				@Override
				public void run() {
					if ("2".equals(mIsInput.getInput())) {
						mTabLayout0101.setVisibility(View.GONE);
						mTabLayout0102.setVisibility(View.VISIBLE);
					} else {
						mTabLayout0101.setVisibility(View.VISIBLE);
						mTabLayout0102.setVisibility(View.GONE);
					}
				}

			});
			return;
		}
	}

	@Override
	public void executeSuccess() {
		super.executeSuccess();

		if (mListVO == null)
			return;
		mLodMore = true;
		mCountTv.setText(mListVO.getCount());

		if (BaseCommonUtils.parseInt(mListVO.getTotal()) > BaseCommonUtils.parseInt(mListVO.getPage())) {
			mListView.showFooterView(true);
		} else {
			mListView.showFooterView(false);
		}
		if (mListVO.getList() == null) {
			mTabLayout0301.setVisibility(View.GONE);
			mTabLayout0302.setVisibility(View.VISIBLE);
		} else {
			if (1 == mState) {
				mAdapter.mList.size();
				mAdapter.updateDataFrist(mListVO.getList());
			} else if (2 == mState) {
				mAdapter.mList.size();
				mAdapter.updateDataLast(mListVO.getList());
			} else {
				mAdapter.updateData(mListVO.getList());
			}
			mTabLayout0301.setVisibility(View.VISIBLE);
			mTabLayout0302.setVisibility(View.GONE);
		}

	}

	@Override
	public void executeFailure() {
		super.executeSuccess();
		mLodMore = true;
		mTabLayout0301.setVisibility(View.GONE);
		mTabLayout0302.setVisibility(View.VISIBLE);
	}

	@Override
	public void loadMore() {
		if (BaseCommonUtils.parseInt(mListVO.getTotal()) > BaseCommonUtils.parseInt(mListVO.getPage()) && mLodMore) {
			mLodMore = false;
			mState = 2;
			mPage = (BaseCommonUtils.parseInt(mPage) + 1) + "";
			new ThreadUtil(this, this).start();
		}
	}
}
