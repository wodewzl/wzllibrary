package com.xiaojing.shop.activity;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.wuzhanglong.library.utils.MapUtil;
import com.wuzhanglong.library.utils.ShareUtil;
import com.xiaojing.shop.R;
import com.xiaojing.shop.application.AppApplication;
import com.xiaojing.shop.constant.Constant;
import com.xiaojing.shop.mode.NearbyShopVO;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class NearbyShopStoreDetailActivity extends BaseActivity implements ObservableScrollViewCallbacks, View.OnClickListener {
    private Banner mBanner;
    private NearbyShopVO mNearbyShopVO;
    private TextView mAddressTv, mPhoneTv, mTimeTv, mDescTv, mTypeTv, mBackTv, mTitleTv;
    private LinearLayout mTitleLayout;
    private ObservableScrollView mScrollView;
    private ImageView mShareImg;
    private LinearLayout mPhoneLayout;

    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.shop_store_activity);
    }

    @Override
    public void initView() {
        mBaseHeadLayout.setVisibility(View.GONE);
        mTitleLayout = getViewById(R.id.title_layout);
        mBackTv = getViewById(R.id.back_tv);
        mTimeTv = getViewById(R.id.title_tv);
        mTimeTv.setText("商家店铺");
        mBanner = getViewById(R.id.banner);
        mBanner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE);
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        mAddressTv = getViewById(R.id.address_tv);
        mTimeTv = getViewById(R.id.time_tv);
        mPhoneTv = getViewById(R.id.phone_tv);
        mDescTv = getViewById(R.id.desc_tv);
        mTypeTv = getViewById(R.id.type_tv);
        mScrollView = (ObservableScrollView) findViewById(R.id.scroll_view);
        mShareImg = getViewById(R.id.share_img);
        mPhoneLayout = getViewById(R.id.phone_layout);
    }

    @Override
    public void bindViewsListener() {
        mAddressTv.setOnClickListener(this);
        mBackTv.setOnClickListener(this);
//        mScrollView.setScrollViewCallbacks(this);
        mBanner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object o, ImageView imageView) {
                String url = (String) o;
//                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                Picasso.with(context).load(url).into(imageView);
            }
        });
        mShareImg.setOnClickListener(this);
        mPhoneLayout.setOnClickListener(this);
    }

    @Override
    public void getData() {
        RequestParams params = new RequestParams();
        if (AppApplication.getInstance().getUserInfoVO() != null)
            params.put("key", AppApplication.getInstance().getUserInfoVO().getKey());
        params.put("merchant_id", this.getIntent().getStringExtra("merchant_id"));
        String urlCity = Constant.NERBY_SHOP_DETAIL_URL;
        HttpClientUtil.get(mActivity, mThreadUtil, urlCity, params, NearbyShopVO.class);
    }

    @Override
    public void hasData(BaseVO vo) {
        NearbyShopVO nearbyShopVO = (NearbyShopVO) vo;
        mNearbyShopVO = nearbyShopVO.getDatas().getMerchant();
        if (mNearbyShopVO.getImgs() == null) {
            List<String> list = new ArrayList<>();
        } else {
            mBanner.setImages(mNearbyShopVO.getImgs());
            List<String> titleList = new ArrayList<>();
            for (int i = 0; i < mNearbyShopVO.getImgs().size(); i++) {
                titleList.add(mNearbyShopVO.getMerchant_name());
            }
            mBanner.setBannerTitles(titleList);
        }
        mBanner.start();
        mAddressTv.setText(mNearbyShopVO.getMerchant_address());
        mTimeTv.setText(mNearbyShopVO.getMerchant_worktime());
        mPhoneTv.setText(mNearbyShopVO.getMerchant_mobile());
        mDescTv.setText(mNearbyShopVO.getMerchant_introduce());
        mTypeTv.setText(mNearbyShopVO.getMerchant_class_text());
    }

    @Override
    public void noData(BaseVO vo) {

    }

    @Override
    public void noNet() {

    }

    @Override
    public void onScrollChanged(int i, boolean b, boolean b1) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        if (scrollState == ScrollState.UP) {
            mTitleLayout.setBackgroundResource(R.color.C7);
        } else if (scrollState == ScrollState.DOWN) {
            mTitleLayout.setBackgroundResource(R.color.C15);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.address_tv:
                MapUtil.guide(this, mNearbyShopVO.getMerchant_lat(), mNearbyShopVO.getMerchant_lng(), mNearbyShopVO.getMerchant_address());
                break;
            case R.id.back_tv:
                finish();
                break;

            case R.id.share_img:
                ShareUtil.share(this, mNearbyShopVO.getShare_data().getImage(), mNearbyShopVO.getShare_data().getTitle(), mNearbyShopVO.getShare_data().getDesc(), mNearbyShopVO.getShare_data()
                        .getUrl());
                break;

            case R.id.phone_layout:
                BaseCommonUtils.call(this,mPhoneTv.getText().toString());
                break;
            default:
                break;
        }
    }
}
