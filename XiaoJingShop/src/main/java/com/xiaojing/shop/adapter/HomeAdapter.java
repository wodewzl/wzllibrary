package com.xiaojing.shop.adapter;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wuzhanglong.library.adapter.RecyclerBaseAdapter;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.xiaojing.shop.R;
import com.xiaojing.shop.activity.GameActivity;
import com.xiaojing.shop.activity.HistoryShopActivity;
import com.xiaojing.shop.activity.MyJingBiActivity;
import com.xiaojing.shop.activity.NearbyShopStoreActivity;
import com.xiaojing.shop.activity.OneShopActivity;
import com.xiaojing.shop.activity.ShopListActivity;
import com.xiaojing.shop.activity.ShopPromotionsActivity;
import com.xiaojing.shop.activity.WebViewActivity;
import com.xiaojing.shop.mode.HomeVO;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

import static com.xiaojing.shop.R.id.type3_img;
import static com.xiaojing.shop.R.id.type4_img1;
import static com.xiaojing.shop.R.id.type4_img2;
import static com.xiaojing.shop.R.id.type4_img3;
import static com.xiaojing.shop.R.id.type4_img4;
import static com.xiaojing.shop.R.id.type4_img5;

/**
 * Created by Administrator on 2017/2/13.
 */

public class HomeAdapter extends RecyclerBaseAdapter<HomeVO> {
    public HomeAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.home_adapter_type1);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initData(BGAViewHolderHelper helper, int position, Object model) {
        final HomeVO homeVO = (HomeVO) model;
        switch (getItemViewType(position)) {
            case R.layout.home_adapter_type0:
                Banner banner = helper.getView(R.id.banner);
                banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                banner.setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object o, ImageView imageView) {
                        final HomeVO homeVO = (HomeVO) o;
                        Picasso.with(context).load(homeVO.getImage()).into(imageView);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Bundle bundle = new Bundle();
                                if ("keyword".equals(homeVO.getType())) {
                                    bundle.putString("keyword", homeVO.getData());
                                    mActivity.open(ShopListActivity.class, bundle, 0);
                                } else if ("goods".equals(homeVO.getType())) {
                                    bundle.putString("url", homeVO.getData());
                                    mActivity.open(WebViewActivity.class, bundle, 0);
                                } else if ("url".equals(homeVO.getType())) {
                                    bundle.putString("url", homeVO.getData());
                                    mActivity.open(WebViewActivity.class, bundle, 0);
                                }
                            }
                        });
                    }
                });


                banner.setIndicatorGravity(BannerConfig.CENTER);
                banner.setImages(homeVO.getItem_data().getItem());
                banner.start();

                helper.setItemChildClickListener(R.id.type_01_tv);
                helper.setItemChildClickListener(R.id.type_02_tv);
                helper.setItemChildClickListener(R.id.type_03_tv);
                helper.setItemChildClickListener(R.id.type_04_tv);
                helper.setItemChildClickListener(R.id.type_05_tv);
                helper.setItemChildClickListener(R.id.type_06_tv);
                helper.setItemChildClickListener(R.id.type_07_tv);
                helper.setItemChildClickListener(R.id.type_08_tv);

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
                LinearLayout rootLayout = helper.getView(R.id.type5_root_layout);
                if (homeVO.getItem_data().getItem().size() % 2 == 0) {
                    if (rootLayout.getChildCount() == homeVO.getItem_data().getItem().size() / 2) {
                        return;
                    }
                } else {
                    if (rootLayout.getChildCount() == (homeVO.getItem_data().getItem().size() / 2 + 1)) {
                        return;
                    }
                }
                rootLayout.removeAllViews();
                for (int i = 0; i < homeVO.getItem_data().getItem().size(); i++) {
                    if (homeVO.getItem_data().getItem().size() % 2 == 0) {
                        if (i % 2 == 0) {
                            View view = View.inflate(mContext, R.layout.home_adapter_type5_2, null);
                            rootLayout.addView(view);
                            TextView type5_title1 = (TextView) view.findViewById(R.id.type5_title1);
                            type5_title1.setText(homeVO.getItem_data().getItem().get(i).getGoods_name());
                            TextView type5_price1 = (TextView) view.findViewById(R.id.type5_price1);
                            type5_price1.setText("￥"+homeVO.getItem_data().getItem().get(i).getGoods_price());
                            ImageView type5_img1 = (ImageView) view.findViewById(R.id.type5_img1);
                            Picasso.with(mContext).load(homeVO.getItem_data().getItem().get(i).getGoods_image()).into(type5_img1);
                            TextView type5_same1 = (TextView) view.findViewById(R.id.type5_same1);
                            type5_same1.setBackground(BaseCommonUtils.setBackgroundShap(mContext, 5, R.color.C3_1, R.color.C1));
                            RelativeLayout type5_layout01 = (RelativeLayout) view.findViewById(R.id.type5_layout01);
                            final int finalI3 = i;
                            type5_layout01.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("url", homeVO.getItem_data().getItem().get(finalI3).getDetail_url());
                                    mActivity.open(WebViewActivity.class, bundle, 0);
                                }
                            });
                            type5_same1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("gc_id", homeVO.getItem_data().getItem().get(finalI3).getGc_id());
                                    mActivity.open(ShopListActivity.class, bundle, 0);
                                }
                            });

                            TextView type5_title2 = (TextView) view.findViewById(R.id.type5_title2);
                            type5_title2.setText(homeVO.getItem_data().getItem().get(i + 1).getGoods_name());
                            TextView type5_price2 = (TextView) view.findViewById(R.id.type5_price2);
                            type5_price2.setText("￥"+homeVO.getItem_data().getItem().get(i + 1).getGoods_price());
                            ImageView type5_img2 = (ImageView) view.findViewById(R.id.type5_img2);
                            Picasso.with(mContext).load(homeVO.getItem_data().getItem().get(i + 1).getGoods_image()).into(type5_img2);
                            TextView type5_same2 = (TextView) view.findViewById(R.id.type5_same2);
                            type5_same2.setBackground(BaseCommonUtils.setBackgroundShap(mContext, 5, R.color.C3_1, R.color.C1));
                            RelativeLayout type5_layout02 = (RelativeLayout) view.findViewById(R.id.type5_layout02);
                            final int finalI4 = i;
                            type5_layout02.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("url", homeVO.getItem_data().getItem().get(finalI4 + 1).getDetail_url());
                                    mActivity.open(WebViewActivity.class, bundle, 0);
                                }
                            });
                            type5_same2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("gc_id", homeVO.getItem_data().getItem().get(finalI4 + 1).getGc_id());
                                    mActivity.open(ShopListActivity.class, bundle, 0);
                                }
                            });
                        }
                    } else {
                        if (i % 2 == 0 && i < homeVO.getItem_data().getItem().size() - 1) {
                            View view = View.inflate(mContext, R.layout.home_adapter_type5_2, null);
                            rootLayout.addView(view);
                            TextView type5_title1 = (TextView) view.findViewById(R.id.type5_title1);
                            type5_title1.setText(homeVO.getItem_data().getItem().get(i).getGoods_name());
                            TextView type5_price1 = (TextView) view.findViewById(R.id.type5_price1);
                            type5_price1.setText("￥"+homeVO.getItem_data().getItem().get(i).getGoods_price());
                            ImageView type5_img1 = (ImageView) view.findViewById(R.id.type5_img1);
                            Picasso.with(mContext).load(homeVO.getItem_data().getItem().get(i).getGoods_image()).into(type5_img1);
                            TextView type5_same1 = (TextView) view.findViewById(R.id.type5_same1);
                            type5_same1.setBackground(BaseCommonUtils.setBackgroundShap(mContext, 5, R.color.C3_1, R.color.C1));
                            RelativeLayout type5_layout01 = (RelativeLayout) view.findViewById(R.id.type5_layout01);
                            final int finalI1 = i;
                            type5_layout01.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("url", homeVO.getItem_data().getItem().get(finalI1).getDetail_url());
                                    mActivity.open(WebViewActivity.class, bundle, 0);
                                }
                            });
                            type5_same1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("gc_id", homeVO.getItem_data().getItem().get(finalI1).getGc_id());
                                    mActivity.open(ShopListActivity.class, bundle, 0);
                                }
                            });

                            TextView type5_title2 = (TextView) view.findViewById(R.id.type5_title2);
                            type5_title2.setText(homeVO.getItem_data().getItem().get(i + 1).getGoods_name());
                            TextView type5_price2 = (TextView) view.findViewById(R.id.type5_price2);
                            type5_price2.setText("￥"+homeVO.getItem_data().getItem().get(i + 1).getGoods_price());
                            ImageView type5_img2 = (ImageView) view.findViewById(R.id.type5_img2);
                            Picasso.with(mContext).load(homeVO.getItem_data().getItem().get(i + 1).getGoods_image()).into(type5_img2);
                            TextView type5_same2 = (TextView) view.findViewById(R.id.type5_same2);
                            type5_same2.setBackground(BaseCommonUtils.setBackgroundShap(mContext, 5, R.color.C3_1, R.color.C1));
                            RelativeLayout type5_layout02 = (RelativeLayout) view.findViewById(R.id.type5_layout02);
                            final int finalI2 = i;
                            type5_layout02.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("url", homeVO.getItem_data().getItem().get(finalI2+1).getDetail_url());
                                    mActivity.open(WebViewActivity.class, bundle, 0);
                                }
                            });
                            type5_same2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("gc_id", homeVO.getItem_data().getItem().get(finalI2+1).getGc_id());
                                    mActivity.open(ShopListActivity.class, bundle, 0);
                                }
                            });
                        } else {
                            if (i == homeVO.getItem_data().getItem().size() - 1) {
                                View view = View.inflate(mContext, R.layout.home_adapter_type5_1, null);
                                rootLayout.addView(view);
                                TextView type5_title1 = (TextView) view.findViewById(R.id.type5_title1);
                                type5_title1.setText(homeVO.getItem_data().getItem().get(i).getGoods_name());
                                TextView type5_price1 = (TextView) view.findViewById(R.id.type5_price1);
                                type5_price1.setText("￥"+homeVO.getItem_data().getItem().get(i).getGoods_price());
                                ImageView type5_img1 = (ImageView) view.findViewById(R.id.type5_img1);
                                Picasso.with(mContext).load(homeVO.getItem_data().getItem().get(i).getGoods_image()).into(type5_img1);
                                TextView type5_same1 = (TextView) view.findViewById(R.id.type5_same1);
                                type5_same1.setBackground(BaseCommonUtils.setBackgroundShap(mContext, 5, R.color.C3_1, R.color.C1));
                                RelativeLayout type5_layout01 = (RelativeLayout) view.findViewById(R.id.type5_layout01);
                                final int finalI = i;
                                type5_layout01.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("url", homeVO.getItem_data().getItem().get(finalI).getDetail_url());
                                        mActivity.open(WebViewActivity.class, bundle, 0);
                                    }
                                });
                                type5_same1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("gc_id", homeVO.getItem_data().getItem().get(finalI).getGc_id());
                                        mActivity.open(ShopListActivity.class, bundle, 0);
                                    }
                                });
                            }
                        }
                    }
                }
                break;
            default:
                break;
        }

        helper.setOnItemChildClickListener(new BGAOnItemChildClickListener() {
            Bundle bundle = new Bundle();

            @Override
            public void onItemChildClick(ViewGroup viewGroup, View v, int i) {
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
                        mActivity.openActivity(ShopListActivity.class);
                        break;
                    case R.id.type_06_tv:
                        mActivity.openActivity(MyJingBiActivity.class);
                        break;
                    case R.id.type_07_tv:

                        break;
                    case R.id.type_08_tv:
                        mActivity.openActivity(HistoryShopActivity.class);
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
                viewType = R.layout.home_adapter_type5;
                break;
            default:
                break;
        }
        return viewType;
    }
}
