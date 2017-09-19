package com.xiaojing.shop.adapter;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.wuzhanglong.library.adapter.RecyclerBaseAdapter;
import com.wuzhanglong.library.mode.EBMessageVO;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.wuzhanglong.library.utils.SharePreferenceUtil;
import com.xiaojing.shop.R;
import com.xiaojing.shop.activity.GameActivity;
import com.xiaojing.shop.activity.HistoryShopActivity;
import com.xiaojing.shop.activity.LoginActivity;
import com.xiaojing.shop.activity.MyJingBiActivity;
import com.xiaojing.shop.activity.NearbyShopStoreActivity;
import com.xiaojing.shop.activity.OneShopActivity;
import com.xiaojing.shop.activity.ShopListActivity;
import com.xiaojing.shop.activity.ShopPromotionsActivity;
import com.xiaojing.shop.activity.WebViewActivity;
import com.xiaojing.shop.application.AppApplication;
import com.xiaojing.shop.mode.HomeVO;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.EventBus;

import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

import static com.xiaojing.shop.R.id.type3_img;
import static com.xiaojing.shop.R.id.type4_img1;
import static com.xiaojing.shop.R.id.type4_img2;
import static com.xiaojing.shop.R.id.type4_img3;
import static com.xiaojing.shop.R.id.type4_img4;
import static com.xiaojing.shop.R.id.type4_img5;
import static com.xiaojing.shop.R.layout.home_adapter_type1;

/**
 * Created by Administrator on 2017/2/13.
 */

public class HomeRAdapter extends RecyclerBaseAdapter<HomeVO> {
    private String signUrl;

    public HomeRAdapter(RecyclerView recyclerView) {
        super(recyclerView, home_adapter_type1);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initData(BGAViewHolderHelper helper, int position, Object model) {
        final HomeVO homeVO = (HomeVO) model;
        switch (getItemViewType(position)) {
            case R.layout.home_adapter_type0:
                Banner banner = helper.getView(R.id.banner);

                banner.setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object o, ImageView imageView) {
                        final HomeVO bannerVO = (HomeVO) o;
                        Picasso.with(context).load(bannerVO.getImage()).into(imageView);
                    }
                });

                banner.setImages(homeVO.getItem_data().getItem());
                banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                banner.setBannerAnimation(Transformer.CubeIn);
                banner.setIndicatorGravity(BannerConfig.CENTER);

                banner.setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int i) {
                        final HomeVO bannerVO;
                        if (homeVO.getItem_data().getItem().size() - 1 == i) {
                            bannerVO = homeVO.getItem_data().getItem().get(0);
                        } else {
                            bannerVO = homeVO.getItem_data().getItem().get(i + 1);
                        }
                        Bundle bundle = new Bundle();
                        if ("keyword".equals(bannerVO.getType())) {
                            bundle.putString("keyword", bannerVO.getData());
                            mActivity.open(ShopListActivity.class, bundle, 0);
                        } else if ("goods".equals(bannerVO.getType())) {
                            bundle.putString("url", bannerVO.getData());
                            mActivity.open(WebViewActivity.class, bundle, 0);
                        } else if ("url".equals(bannerVO.getType())) {
                            bundle.putString("url", bannerVO.getData());
                            mActivity.open(WebViewActivity.class, bundle, 0);
                        }
                    }
                });
                banner.start();


                break;

            case R.layout.home_adapter_type2:
                helper.setText(R.id.type2, homeVO.getItem_data().getTitle());
                Picasso.with(mContext).load(homeVO.getItem_data().getRectangle1_image()).into(helper.getImageView(R.id.type1_img1));
                Picasso.with(mContext).load(homeVO.getItem_data().getRectangle2_image()).into(helper.getImageView(R.id.type1_img2));
                Picasso.with(mContext).load(homeVO.getItem_data().getRectangle3_image()).into(helper.getImageView(R.id.type1_img3));
                Picasso.with(mContext).load(homeVO.getItem_data().getRectangle4_image()).into(helper.getImageView(R.id.type1_img4));
                Picasso.with(mContext).load(homeVO.getItem_data().getRectangle5_image()).into(helper.getImageView(R.id.type1_img5));
                helper.setItemChildClickListener(R.id.type1_img1);
                helper.setItemChildClickListener(R.id.type1_img2);
                helper.setItemChildClickListener(R.id.type1_img3);
                helper.setItemChildClickListener(R.id.type1_img4);
                helper.setItemChildClickListener(R.id.type1_img5);
                break;
            case R.layout.home_adapter_type3:
                Picasso.with(mContext).load(homeVO.getItem_data().getImage()).into(helper.getImageView(type3_img));
                helper.setItemChildClickListener(type3_img);
                break;

            case R.layout.home_adapter_type4:
                helper.setText(R.id.type4, homeVO.getItem_data().getTitle());
                Picasso.with(mContext).load(homeVO.getItem_data().getRectangle1_image()).into(helper.getImageView(type4_img1));
                Picasso.with(mContext).load(homeVO.getItem_data().getRectangle2_image()).into(helper.getImageView(type4_img2));
                Picasso.with(mContext).load(homeVO.getItem_data().getRectangle3_image()).into(helper.getImageView(type4_img3));
                Picasso.with(mContext).load(homeVO.getItem_data().getRectangle4_image()).into(helper.getImageView(type4_img4));
                Picasso.with(mContext).load(homeVO.getItem_data().getRectangle5_image()).into(helper.getImageView(type4_img5));

                helper.setItemChildClickListener(type4_img1);
                helper.setItemChildClickListener(type4_img2);
                helper.setItemChildClickListener(type4_img3);
                helper.setItemChildClickListener(type4_img4);
                helper.setItemChildClickListener(type4_img5);
                break;
            case R.layout.home_adapter_type5:

                break;
            case R.layout.home_adapter_type7:
                helper.setText(R.id.type5_title1, homeVO.getItem_data().getGoods_name());
                helper.setText(R.id.type5_price1, "￥" + homeVO.getItem_data().getGoods_price());
                Picasso.with(mContext).load(homeVO.getItem_data().getGoods_image()).into(helper.getImageView(R.id.type5_img1));
                helper.getTextView(R.id.type5_same1).setBackground(BaseCommonUtils.setBackgroundShap(mContext, 5, R.color.C3_1, R.color.C1));
                helper.setItemChildClickListener(R.id.type5_layout01);
                helper.setItemChildClickListener(R.id.type5_same1);
                break;

            case R.layout.home_adapter_type1:
                helper.setItemChildClickListener(R.id.type_01_tv);
                helper.setItemChildClickListener(R.id.type_02_tv);
                helper.setItemChildClickListener(R.id.type_03_tv);
                helper.setItemChildClickListener(R.id.type_04_tv);
                helper.setItemChildClickListener(R.id.type_05_tv);
                helper.setItemChildClickListener(R.id.type_06_tv);
                helper.setItemChildClickListener(R.id.type_07_tv);
                helper.setItemChildClickListener(R.id.type_08_tv);

              if("0".equals(SharePreferenceUtil.getSharedpreferenceValue(mActivity, "game_tag", "tag"))){
                  helper.getTextView(R.id.type_04_tv).setVisibility(View.INVISIBLE);
              }else{
                  helper.getTextView(R.id.type_04_tv).setVisibility(View.VISIBLE);
              }
//                helper.getTextView(R.id.type_04_tv).setVisibility(View.INVISIBLE);

            default:
                break;
        }

        helper.setOnItemChildClickListener(new BGAOnItemChildClickListener() {
            @Override
            public void onItemChildClick(ViewGroup viewGroup, View v, int i) {
                Bundle bundle = new Bundle();
                switch (v.getId()) {
                    case type4_img1:
                    case R.id.type1_img1:
                        if ("keyword".equals(homeVO.getItem_data().getRectangle1_type())) {
                            bundle.putString("keyword", homeVO.getItem_data().getRectangle1_data());
                            mActivity.open(ShopListActivity.class, bundle, 0);
                        } else if ("goods".equals(homeVO.getItem_data().getRectangle1_type())) {
                            bundle.putString("url", homeVO.getItem_data().getRectangle1_data());
                            mActivity.open(WebViewActivity.class, bundle, 0);
                        } else if ("url".equals(homeVO.getItem_data().getRectangle1_type())) {
                            bundle.putString("url", homeVO.getItem_data().getRectangle1_data());
                            mActivity.open(WebViewActivity.class, bundle, 0);
                        }
                        break;
                    case type4_img2:
                    case R.id.type1_img2:
                        if ("keyword".equals(homeVO.getItem_data().getRectangle2_type())) {
                            bundle.putString("keyword", homeVO.getItem_data().getRectangle2_data());
                            mActivity.open(ShopListActivity.class, bundle, 0);
                        } else if ("goods".equals(homeVO.getItem_data().getRectangle2_type())) {
                            bundle.putString("url", homeVO.getItem_data().getRectangle2_data());
                            mActivity.open(WebViewActivity.class, bundle, 0);
                        } else if ("url".equals(homeVO.getItem_data().getRectangle2_type())) {
                            bundle.putString("url", homeVO.getItem_data().getRectangle2_data());
                            mActivity.open(WebViewActivity.class, bundle, 0);
                        }
                        break;
                    case type4_img3:
                    case R.id.type1_img3:
                        if ("keyword".equals(homeVO.getItem_data().getRectangle3_type())) {
                            bundle.putString("keyword", homeVO.getItem_data().getRectangle3_data());
                            mActivity.open(ShopListActivity.class, bundle, 0);
                        } else if ("goods".equals(homeVO.getItem_data().getRectangle3_type())) {
                            bundle.putString("url", homeVO.getItem_data().getRectangle3_data());
                            mActivity.open(WebViewActivity.class, bundle, 0);
                        } else if ("url".equals(homeVO.getItem_data().getRectangle3_type())) {
                            bundle.putString("url", homeVO.getItem_data().getRectangle3_data());
                            mActivity.open(WebViewActivity.class, bundle, 0);
                        }
                        break;
                    case type4_img4:
                    case R.id.type1_img4:
                        if ("keyword".equals(homeVO.getItem_data().getRectangle4_type())) {
                            bundle.putString("keyword", homeVO.getItem_data().getRectangle4_data());
                            mActivity.open(ShopListActivity.class, bundle, 0);
                        } else if ("goods".equals(homeVO.getItem_data().getRectangle4_type())) {
                            bundle.putString("url", homeVO.getItem_data().getRectangle4_data());
                            mActivity.open(WebViewActivity.class, bundle, 0);
                        } else if ("url".equals(homeVO.getItem_data().getRectangle4_type())) {
                            bundle.putString("url", homeVO.getItem_data().getRectangle4_data());
                            mActivity.open(WebViewActivity.class, bundle, 0);
                        }
                        break;
                    case type4_img5:
                    case R.id.type1_img5:
                        if ("keyword".equals(homeVO.getItem_data().getRectangle5_type())) {
                            bundle.putString("keyword", homeVO.getItem_data().getRectangle5_data());
                            mActivity.open(ShopListActivity.class, bundle, 0);
                        } else if ("goods".equals(homeVO.getItem_data().getRectangle5_type())) {
                            bundle.putString("url", homeVO.getItem_data().getRectangle5_data());
                            mActivity.open(WebViewActivity.class, bundle, 0);
                        } else if ("url".equals(homeVO.getItem_data().getRectangle5_type())) {
                            bundle.putString("url", homeVO.getItem_data().getRectangle5_data());
                            mActivity.open(WebViewActivity.class, bundle, 0);
                        }
                        break;

                    case R.id.type3_img:
                        if ("keyword".equals(homeVO.getItem_data().getType())) {
                            bundle.putString("keyword", homeVO.getData());
                            mActivity.open(ShopListActivity.class, bundle, 0);
                        } else if ("goods".equals(homeVO.getItem_data().getType())) {
                            bundle.putString("url", homeVO.getData());
                            mActivity.open(WebViewActivity.class, bundle, 0);
                        } else if ("url".equals(homeVO.getItem_data().getType())) {
                            bundle.putString("url", homeVO.getData());
                            mActivity.open(WebViewActivity.class, bundle, 0);
                        }
                        break;
                    case R.id.type_01_tv:
                        mActivity.openActivity(NearbyShopStoreActivity.class);
                        break;
                    case R.id.type_02_tv:
                        mActivity.openActivity(OneShopActivity.class);
                        break;
                    case R.id.type_03_tv:
                        mActivity.openActivity(ShopPromotionsActivity.class);
                        break;
                    case R.id.type_04_tv:
                        mActivity.openActivity(GameActivity.class);
                        break;
                    case R.id.type_05_tv:
//                        mActivity.openActivity(ShopListActivity.class);
                        EventBus.getDefault().post(new EBMessageVO("select_shop_type"));
                        break;
                    case R.id.type_06_tv:
                        if (AppApplication.getInstance().getUserInfoVO() == null) {
                            bundle.putString("type", "2");
                            mActivity.open(LoginActivity.class, bundle, 1);
                            return;
                        }
                        bundle.putString("type", "1");
                        mActivity.open(MyJingBiActivity.class, bundle, 0);
                        break;
                    case R.id.type_07_tv:
                        if (AppApplication.getInstance().getUserInfoVO() == null) {
                            bundle.putString("type", "2");
                            mActivity.open(LoginActivity.class, bundle, 1);
                            return;
                        }
                        bundle.putString("url", HomeRAdapter.this.getSignUrl());
                        mActivity.open(WebViewActivity.class, bundle, 0);
                        break;
                    case R.id.type_08_tv:
                        if (AppApplication.getInstance().getUserInfoVO() == null) {
                            bundle.putString("type", "2");
                            mActivity.open(LoginActivity.class, bundle, 1);
                            return;
                        }
                        mActivity.openActivity(HistoryShopActivity.class);
                        break;
                    case R.id.type5_layout01:
                        bundle.putString("url", homeVO.getItem_data().getDetail_url());
                        mActivity.open(WebViewActivity.class, bundle, 0);
                        break;
                    case R.id.type5_same1:
                        bundle.putString("gc_id", homeVO.getItem_data().getGc_id());
                        mActivity.open(ShopListActivity.class, bundle, 0);
                        break;
                    default:
                        break;
                }
            }
        });

    }


    @Override
    public int getItemViewType(int position) {
        HomeVO homeVO = (HomeVO) mData.get(position);
        int type = BaseCommonUtils.parseInt(homeVO.getType());
        return getViewByType(type);
    }

    public int getViewByType(int type) {
        int viewType = 0;
        switch (type) {
            case 1:
                viewType = R.layout.home_adapter_type0;
                break;
            case 2:
                viewType = R.layout.home_adapter_type3;
                break;
            case 3:
                viewType = R.layout.home_adapter_type2;
                break;
            case 4:
                viewType = R.layout.home_adapter_type4;
                break;
            case 5:
                viewType = R.layout.home_adapter_type7;
                break;
            case 6:
                viewType = R.layout.home_adapter_type1;
                break;
            case 7:
                viewType = R.layout.home_adapter_type5;
                break;
            default:
                break;
        }
        return viewType;
    }

    public String getSignUrl() {
        return signUrl;
    }

    public void setSignUrl(String signUrl) {
        this.signUrl = signUrl;
    }
}
