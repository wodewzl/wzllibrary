package com.bs.bsims.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.bs.bsims.R;
import com.bs.bsims.model.LocusLineVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.view.BSCircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class LocusPersonActivity extends BaseActivity implements OnClickListener,OnMarkerClickListener {

    private MapView mMapView;
    private AMap mAMap;
    private TextView mPersonTitle01, mPersonTitle02, mPersonTitle03, mPersonTitle04;
    private BSCircleImageView mHeadIcon;
    // 显示上传的图片
    private ImageView mDetailImg01, mDetailImg02, mDetailImg03;
    private List<ImageView> mListImag;
    private LinearLayout mPictureLayout;
    private LinearLayout mLeftLayout, mRightLayout;
    private LocusLineVO mLocusLineVO;
    private TextView mAddressTv;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.person_locus, mContentLayout);

    }

    @Override
    public boolean getDataResult() {
        return true;
    }

    @Override
    public void updateUi() {

    }

    @Override
    public void executeSuccess() {
        super.executeSuccess();
    }

    @Override
    public void executeFailure() {
        super.executeFailure();
    }

    public void initData() {
        Intent intent = this.getIntent();
        mTitleTv.setText("位置详情");
        mLocusLineVO = (LocusLineVO) intent.getSerializableExtra("vo");
        locationAtNow();
        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = CommonUtils.initImageLoaderOptions();

        mHeadIcon = (BSCircleImageView) findViewById(R.id.head_icon);
        mHeadIcon.setUrl(mLocusLineVO.getHeadpic());
        mHeadIcon.setUserName(mLocusLineVO.getFullName());
        mHeadIcon.setUserId(mLocusLineVO.getUserid());
        mPersonTitle01 = (TextView) findViewById(R.id.person_title01);
        if ("男".equals(mLocusLineVO.getSex())) {
            // 添加性别图片
            mPersonTitle01.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.sex_man, 0);
        } else {
            mPersonTitle01.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.sex_woman, 0);
        }
        mPersonTitle01.setCompoundDrawablePadding(5);

        mPersonTitle02 = (TextView) findViewById(R.id.person_title02);
        mPersonTitle03 = (TextView) findViewById(R.id.person_title03);
        mPersonTitle04 = (TextView) findViewById(R.id.person_title04);
        imageLoader.displayImage(mLocusLineVO.getHeadpic(), mHeadIcon, options);
        mPersonTitle01.setText(mLocusLineVO.getFullName());
        mPersonTitle02.setText(mLocusLineVO.getDname() + "/" + mLocusLineVO.getPname());
        mPersonTitle03.setText(mLocusLineVO.getDate() + " " + mLocusLineVO.getDatetime());

        if ("1".equals(mLocusLineVO.getType())) {
            mPersonTitle04.setText("上报位置");
            mPersonTitle04.setTextColor(this.getResources().getColor(R.color.H2));
        } else {
            mPersonTitle04.setText("移动打卡");
            mPersonTitle04.setTextColor(this.getResources().getColor(R.color.H1));
        }

        DisplayImageOptions optionsPic = new DisplayImageOptions.Builder().showStubImage(R.drawable.common_ic_image_default).showImageForEmptyUri(R.drawable.common_ic_image_default)
                .showImageOnFail(R.drawable.common_ic_image_default).cacheInMemory().cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();

        if (mLocusLineVO.getImgs() != null) {
            mPictureLayout.setBackgroundColor(getResources().getColor(R.color.white));
            mPictureLayout.setVisibility(View.VISIBLE);
            final List<String> list = mLocusLineVO.getImgs();
            for ( int mImgIndex = 0; mImgIndex < list.size(); mImgIndex++) {
                if (mImgIndex < 3) {
                    mListImag.get(mImgIndex).setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            Intent intent = new Intent();
                            intent.putStringArrayListExtra("piclist", (ArrayList<String>) list);
                            intent.setClass(LocusPersonActivity.this, ImagePreviewActivity.class);
                            // intent.putExtra("imgIndex",mImgIndex);
                            startActivity(intent);
                        }
                    });
                    imageLoader.displayImage(list.get(mImgIndex), mListImag.get(mImgIndex), optionsPic);
                }

            }
        }
        mAddressTv.setText(mLocusLineVO.getAddress());
    }

    @Override
    public void initView() {
        mMapView = (MapView) findViewById(R.id.map);
        if (mAMap == null) {
            mAMap = mMapView.getMap();
            mAMap.getUiSettings().setZoomControlsEnabled(false);
        }

        mHeadIcon = (BSCircleImageView) findViewById(R.id.head_icon);
        mPersonTitle01 = (TextView) findViewById(R.id.person_title01);
        mPersonTitle02 = (TextView) findViewById(R.id.person_title02);
        mPersonTitle03 = (TextView) findViewById(R.id.person_title03);
        mPersonTitle04 = (TextView) findViewById(R.id.person_title04);

        mPictureLayout = (LinearLayout) findViewById(R.id.picture_layout);
        mDetailImg01 = (ImageView) findViewById(R.id.detial_img_01);
        mDetailImg02 = (ImageView) findViewById(R.id.detial_img_02);
        mDetailImg03 = (ImageView) findViewById(R.id.detial_img_03);
        mListImag = new ArrayList<ImageView>();
        mListImag.add(mDetailImg01);
        mListImag.add(mDetailImg02);
        mListImag.add(mDetailImg03);
        mAddressTv = (TextView) findViewById(R.id.address_tv);
        mLeftLayout = (LinearLayout) findViewById(R.id.left_layout);
        mRightLayout = (LinearLayout) findViewById(R.id.right_layout);
        initData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();

    }

    @Override
    public void bindViewsListener() {
        mLeftLayout.setOnClickListener(this);
        mRightLayout.setOnClickListener(this);
    }

    /**
     * 定位地图 并且隐藏不必要的按钮
     */
    private void locationAtNow() {
        // TODO Auto-generated method stub
        boolean flag = false;
        mAMap.setMyLocationEnabled(!flag);// 是否可触发定位并显示定位层
        mAMap.getUiSettings().setMyLocationButtonEnabled(!flag);// 设置默认定位按钮是否显示
        mAMap.setOnMarkerClickListener(this);// 设置大头针点击无效
        // mUiSettings.setMyLocationButtonEnabled(!flag);// 设置默认定位按钮是否显示
        // mUiSettings.setScrollGesturesEnabled(flag);

        /**
         * 设置地图是否可以手势缩放大小
         */
        // mUiSettings.setZoomGesturesEnabled(true);
        // mUiSettings.setZoomControlsEnabled(true);
        // mUiSettings.setScrollGesturesEnabled(true);// 设置地图拖动yes
        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(
                new LatLng(Double.parseDouble(mLocusLineVO.getLat()), Double.parseDouble(mLocusLineVO.getLng())),
                16.0f);
        mAMap.moveCamera(cu);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(Double.parseDouble(mLocusLineVO.getLat()), Double.parseDouble(mLocusLineVO.getLng())));
        markerOptions.title("");
        markerOptions.snippet("");
        markerOptions.visible(true);
        markerOptions.draggable(false);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.crm_highsas_clients_far)));

        // 绘制一个圆形
        // mAMap.addCircle(new CircleOptions()
        // .center(new LatLng(Double.parseDouble("122.344"), Double.parseDouble("33.44")))
        // .radius(100).strokeColor(Color.parseColor("#00A9FE"))
        // .strokeWidth(5));
        // mAMap.setInfoWindowAdapter(this);
        mAMap.addMarker(markerOptions).hideInfoWindow();
        /**
         * 设置显示地图的默认比例尺
         */
        // mUiSettings.setScaleControlsEnabled(!flag);

        // 方法设置地图的缩放级别

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.left_layout:
                //
                intent.setClass(this, ContactPersonActivity.class);
                intent.putExtra("uid", mLocusLineVO.getUserid());
                intent.putExtra("userid", mLocusLineVO.getUserid());
                intent.putExtra("cornet", mLocusLineVO.getCornet());
                break;
            case R.id.right_layout:
                intent.setClass(this, GaoDeMapPersonTrajectoryActivity.class);
                intent.putExtra("sid", mLocusLineVO.getUserid());
                intent.putExtra("username", mLocusLineVO.getFullName());
                intent.putExtra("date", mLocusLineVO.getDate());
                break;
            default:
                break;
        }
        if (intent.getComponent() != null)
            this.startActivity(intent);
    }

    @Override
    public boolean onMarkerClick(Marker arg0) {
        // TODO Auto-generated method stub
        arg0.hideInfoWindow();// 隐藏info的信息
        return true;// 不自动定位到地图中心
    }

}
