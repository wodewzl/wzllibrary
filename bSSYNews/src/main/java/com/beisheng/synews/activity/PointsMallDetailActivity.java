
package com.beisheng.synews.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.http.HttpClientUtil;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.FileUtil;
import com.beisheng.base.utils.WebviewUtil;
import com.beisheng.base.view.BSDialog;
import com.beisheng.base.view.BSFlowIndicator;
import com.beisheng.synews.application.AppApplication;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.mode.PointsMallVO;
import com.google.gson.Gson;
import com.im.zhsy.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.HashMap;

@SuppressLint("NewApi")
public class PointsMallDetailActivity extends BaseActivity implements OnPageChangeListener, OnClickListener {
    private PointsMallVO mPointsMallVO;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;
    private SamplePagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private TextView mTitleTv, mPriceTv, mRedeemTv;
    private String mSid;
    private BSFlowIndicator mFlowIndicator;
    private int mImgIndex = 0;
    private WebView mWebView;
    private boolean mCommitFlag = true;
    private BSDialog mDialog;
    private TextView mAllTv, mDuiTv;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.points_mall_detail_activity, mBaseContentLayout);
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    @SuppressLint("NewApi")
    @Override
    public void initView() {
        mBaseTitleTv.setText("商品详情");
        mImageLoader = ImageLoader.getInstance();
        mOptions = new DisplayImageOptions.Builder().showStubImage(0).showImageForEmptyUri(R.drawable.base_article_bigimage)
                .showImageOnFail(R.drawable.base_article_bigimage).cacheInMemory().cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mPagerAdapter = new SamplePagerAdapter(this);
        mFlowIndicator = (BSFlowIndicator) findViewById(R.id.galleryIndicator);
        mTitleTv = (TextView) findViewById(R.id.title_tv);
        mPriceTv = (TextView) findViewById(R.id.price_tv);
        mRedeemTv = (TextView) findViewById(R.id.redeem_tv);
        mRedeemTv.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.sy_title_color, R.color.sy_title_color));
        mWebView = (WebView) findViewById(R.id.content_wb);
        mAllTv = (TextView) findViewById(R.id.all_tv);
        mDuiTv = (TextView) findViewById(R.id.dui_tv);
        initData();
    }

    public void initData() {
        mSid = this.getIntent().getStringExtra("sid");
    }

    @Override
    public void bindViewsListener() {
        mViewPager.setOnPageChangeListener(this);
        mRedeemTv.setOnClickListener(this);
    }

    public boolean getData() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("sid", mSid);
            if (hasNetWork()) {
                String jsonStr = HttpClientUtil.getRequest(this, Constant.DOMAIN_NAME + Constant.SHOP_DETAIL_URL, map);
                mPointsMallVO = gson.fromJson(jsonStr, PointsMallVO.class);
                saveJsonCache(Constant.SHOP_DETAIL_URL, map, jsonStr);

            } else {
                String oldStr = getCacheFromDatabase(Constant.SHOP_DETAIL_URL, map);
                mPointsMallVO = gson.fromJson(oldStr, PointsMallVO.class);
            }

            if (Constant.RESULT_SUCCESS_CODE.equals(mPointsMallVO.getCode())) {
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
        if (mPointsMallVO != null && mPointsMallVO.getThumb() != null) {
            mPagerAdapter.updateData(mPointsMallVO.getThumb());
            mViewPager.setAdapter(mPagerAdapter);
            mFlowIndicator.setCount(mPointsMallVO.getThumb().length);
            mFlowIndicator.setSeletion(mImgIndex);
        }

        mTitleTv.setText(mPointsMallVO.getTitle());
        mPriceTv.setText(mPointsMallVO.getScore());
        // mAllTv.setText("剩余" + mPointsMallVO.getNum());
        BaseCommonUtils.setDifferentTextColor(mAllTv, "剩余 ", mPointsMallVO.getNum(), "#B10304");
        // mDuiTv.setText("以兑换" + mPointsMallVO.getExchangeNum());
        BaseCommonUtils.setDifferentTextColor(mDuiTv, "已兑换 ", mPointsMallVO.getExchangeNum(), "#B10304");
        WebviewUtil.SetWebview(mWebView);
        String content = mPointsMallVO.getContent();
        mWebView.loadDataWithBaseURL(FileUtil.getSaveFilePath(this), content, "text/html", "utf-8", null);
        if ("0".equals(mPointsMallVO.getNum())) {
            mRedeemTv.setText("兑换结束");
            mRedeemTv.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.C6, R.color.C6));
        }
    }

    @Override
    public void executeFailure() {
        super.executeFailure();
        if (mPointsMallVO != null)
            showCustomToast(mPointsMallVO.getRetinfo());
        else
            showCustomToast("亲，请检查网络哦");

    }

    class SamplePagerAdapter extends PagerAdapter {
        private Context mContext;
        private String[] mPhotoUrls;

        public SamplePagerAdapter(Context context) {
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return mPhotoUrls.length;
        }

        @SuppressLint("NewApi")
        @Override
        public View instantiateItem(ViewGroup container, final int position) {
            final ImageView imageView = new ImageView(container.getContext());
            imageView.setScaleType(ScaleType.FIT_XY);
            container.addView(imageView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            mImageLoader.displayImage(mPhotoUrls[position], imageView, mOptions);
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

        public void updateData(String[] photoUrl) {
            this.mPhotoUrls = photoUrl;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.redeem_tv:
                if (AppApplication.getInstance().getUserInfoVO() == null) {
                    showCustomToast("亲，请先登录帐号哦~");
                    return;
                }

                if ("0".equals(mPointsMallVO.getNum())) {
                    showCustomToast("亲，商品已兑换完~");
                    return;
                }
                NotifyDialog();
                break;

            default:
                break;
        }
    }

    public void NotifyDialog() {
        View v = LayoutInflater.from(this).inflate(R.layout.pop_bottom_item, null);
        final TextView textView = (TextView) v.findViewById(R.id.textview);
        textView.setText("您确定要使用积分兑换吗？");
        int color = this.getResources().getColor(R.color.sy_title_color);
        mDialog = new BSDialog(this, "兑换商品", v, color, new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mDialog.dismiss();
                commit();
            }
        });
        mDialog.show();
    }

    public void commit() {
        mCommitFlag = false;
        showProgressDialog();
        RequestParams params = new RequestParams();
        try {
            params.put("uid", AppApplication.getInstance().getUid());
            params.put("sessionid", AppApplication.getInstance().getSessionid());
            params.put("sid", mPointsMallVO.getSid());

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        String url = Constant.DOMAIN_NAME + Constant.SHOP_REDEEM_URL;
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
                    } else {
                        showCustomToast(str);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
