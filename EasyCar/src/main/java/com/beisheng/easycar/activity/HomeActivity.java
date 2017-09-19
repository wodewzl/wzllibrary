package com.beisheng.easycar.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.beisheng.easycar.R;
import com.beisheng.easycar.application.AppApplication;
import com.beisheng.easycar.constant.Constant;
import com.beisheng.easycar.mode.CarVO;
import com.beisheng.easycar.mode.HomeVO;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.interfaces.PostCallback;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.mode.EBMessageVO;
import com.wuzhanglong.library.utils.BaseCommonUtils;
import com.wuzhanglong.library.utils.BitmapUtil;
import com.wuzhanglong.library.utils.SharePreferenceUtil;
import com.wuzhanglong.library.utils.ShareUtil;
import com.wuzhanglong.library.utils.ThreadUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.EasyPermissions;

public class HomeActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks, AMap.OnMarkerClickListener, View.OnClickListener, PostCallback, TextView.OnEditorActionListener,
        TextWatcher, AMap.OnMyLocationChangeListener, GeocodeSearch.OnGeocodeSearchListener {
    //声明相关变量
    private DrawerLayout mDrawerLayout;
    private ImageView mMenuImg, mPhoneImg;
    private MapView mMapView = null;
    private AMap mAMap;
    MyLocationStyle myLocationStyle;
    private String mCityCode = "";
    private TextView mTitleTv01, mTitleTv02, mSelfTv01, mSelfTv02, mSelfTv03, mSelfTv04, mSelfTv05, mSelfTv06, mSelfTv07, mUseCarBottomTv03;
    private com.rey.material.widget.TextView mHomeTv01, mHomeTv02, mHomeTv03;
    private TextView mAddressTitleTv, mAddressDetailTv;
    private LinearLayout mUserCarTitleLayout, mUserCarBottomLayout, mCarRentalLayout, mCarSearchLayout;
    private EditText mSearchEt;
    private TextView mCancelOrderTv, mCarTitleTv, mCarNumberTv;
    private CircleImageView mCarImg;
    private double mBackPressed;
    private String mOrderId;
    private String mKeyword;
    private boolean mFlag = false, mMove = false;
    private GeocodeSearch mGeocoderSearch;
    private ImageView mImg01, mImg02, mImg03, mImg04;

    @Override
    public void baseSetContentView() {
        contentInflateView(R.layout.home_activity);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initView() {
        mBaseHeadLayout.setVisibility(View.GONE);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_left);
        mMenuImg = getViewById(R.id.menu_img);
        mTitleTv01 = getViewById(R.id.title_tv01);
        mTitleTv02 = getViewById(R.id.title_tv02);
        mTitleTv02.setBackground(BaseCommonUtils.setBackgroundShap(this, 10, R.color.Car7, R.color.Car7));
        mSelfTv01 = getViewById(R.id.self_tv01);
        mSelfTv02 = getViewById(R.id.self_tv02);
        mSelfTv03 = getViewById(R.id.self_tv03);
        mSelfTv04 = getViewById(R.id.self_tv04);
        mSelfTv05 = getViewById(R.id.self_tv05);
        mSelfTv06= getViewById(R.id.self_tv06);
        mSelfTv07 = getViewById(R.id.self_tv07);
        mHomeTv01 = getViewById(R.id.home_tv01);
        mHomeTv01.setBackground(BaseCommonUtils.setBackgroundShap(this, 15, R.color.Car10, R.color.Car10));
        mHomeTv02 = getViewById(R.id.home_tv02);
        mAddressTitleTv = getViewById(R.id.address_title_tv);
        mAddressDetailTv = getViewById(R.id.address_detail_tv);
        mUserCarTitleLayout = getViewById(R.id.use_car_title_layout);
        mUserCarBottomLayout = getViewById(R.id.use_car_bottom_layout);
        mCarRentalLayout = getViewById(R.id.car_rental_layout);
        mCarSearchLayout = getViewById(R.id.car_seach_layout);
        mPhoneImg = getViewById(R.id.phone_img);
        mCancelOrderTv = getViewById(R.id.cancel_order_tv);
        mCarTitleTv = getViewById(R.id.car_title_tv);
        mCarNumberTv = getViewById(R.id.car_number_tv);
        mCarImg = getViewById(R.id.car_img);
        mUseCarBottomTv03 = getViewById(R.id.use_car_bottom_tv03);

        mSearchEt = getViewById(R.id.search_et);
        mSearchEt.setBackground(BaseCommonUtils.setBackgroundShap(this, 5, R.color.C1, R.color.C1));
        mSearchEt.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mSearchEt.setInputType(EditorInfo.TYPE_CLASS_TEXT);

        mImg01 = getViewById(R.id.img_01);
        mImg02 = getViewById(R.id.img_02);
        mImg03 = getViewById(R.id.img_03);
        mImg04 = getViewById(R.id.img_04);

        //获取地图控件引用
        mMapView = getViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(mSavedInstanceState);

        if (mAMap == null) {
            mAMap = mMapView.getMap();
        }
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(12));
        mAMap.getUiSettings().setZoomControlsEnabled(false);
        initPermissions();

    }

    @Override
    public void bindViewsListener() {
        mMenuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);

            }
        });
        mAMap.setOnMarkerClickListener(this);
        mAMap.setOnMyLocationChangeListener(this);
//        mAMap.setOnCameraChangeListener(this);

        mSelfTv01.setOnClickListener(this);
        mSelfTv02.setOnClickListener(this);
        mSelfTv03.setOnClickListener(this);
        mSelfTv04.setOnClickListener(this);
        mSelfTv05.setOnClickListener(this);
        mSelfTv06.setOnClickListener(this);
        mSelfTv07.setOnClickListener(this);
        mHomeTv01.setOnClickListener(this);
        mHomeTv02.setOnClickListener(this);
        mUseCarBottomTv03.setOnClickListener(this);
        mCancelOrderTv.setOnClickListener(this);
        mImg01.setOnClickListener(this);
        mImg02.setOnClickListener(this);
        mImg03.setOnClickListener(this);
        mImg04.setOnClickListener(this);
        mPhoneImg.setOnClickListener(this);
        EventBus.getDefault().register(this);


        mSearchEt.setOnEditorActionListener(this);
//        mSearchEt.addTextChangedListener(this);
        //根据输入地址搜索
        mGeocoderSearch = new GeocodeSearch(this);
        mGeocoderSearch.setOnGeocodeSearchListener(this);
    }

    @Override
    public void getData() {
        if ("".equals(mCityCode)) {
            HttpClientUtil.show(mThreadUtil);
        } else {
            RequestParams params = new RequestParams();
            String url = Constant.HOME_URL;
            if (AppApplication.getInstance().getUserInfoVO() != null)
                params.put("key", AppApplication.getInstance().getUserInfoVO().getUin());
            params.put("citycode", mCityCode);
            HttpClientUtil.get(mActivity, mThreadUtil, url, params, HomeVO.class);
        }

    }

    @Override
    public void hasData(BaseVO vo) {
        HomeVO homeVO = (HomeVO) vo;

        showCar(homeVO.getData());
    }

    @Override
    public void noData(BaseVO vo) {

    }

    @Override
    public void noNet() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onPermissionsGranted(int i, List<String> list) {
        //显示当前定位位置
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        // 连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        mAMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        mAMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);//定位一次，且将视角移动到地图中心点。
        myLocationStyle.showMyLocation(true);
        Bitmap bitmap = BitmapUtil.DrawableToBitMap(this, R.drawable.home_03);
        mAMap.setMyLocationStyle(myLocationStyle);
    }

    @Override
    public void onPermissionsDenied(int i, List<String> list) {
        Toast.makeText(this, "请允许权限", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    public void initPermissions() {
        String[] perms = {android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // 县市定位
            //显示当前定位位置
            myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
            // 连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
            myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
            mAMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
            mAMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);//定位一次，且将视角移动到地图中心点。
            myLocationStyle.showMyLocation(true);
            mAMap.setMyLocationStyle(myLocationStyle);

        } else {
            EasyPermissions.requestPermissions(this, "请允许打开权限权限", 1, perms);
        }
    }

    public void showCar(List<HomeVO> list) {
        for (int i = 0; i < list.size(); i++) {
            MarkerOptions markerOption = new MarkerOptions();
            LatLng latLng = new LatLng(Double.parseDouble(list.get(i).getLat()), Double.parseDouble(list.get(i).getLng()));
            markerOption.position(latLng);
//            markerOption.title("西安市").snippet("西安市：34.341568, 108.940174");

            markerOption.draggable(true);//设置Marker可拖动
            markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                    .decodeResource(getResources(), R.drawable.home_03)));
            // 将Marker设置为贴地显示，可以双指下拉地图查看效果
            markerOption.setFlat(true);//设置marker平贴地图效果
            Marker marker = mAMap.addMarker(markerOption);

            marker.setPeriod(BaseCommonUtils.parseInt(list.get(i).getId()));


        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Bundle bundle = new Bundle();
        bundle.putString("id", marker.getPeriod() + "");
        open(UseCarActivity.class, bundle, 0);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.self_tv01:
                openActivity(MyRouteActivity.class);
                break;
            case R.id.self_tv02:
                openActivity(MyMoneyActivity.class);
                break;
            case R.id.self_tv03:
                openActivity(MyCouponActivity.class);
                break;
            case R.id.self_tv04:
                openActivity(MyInvoiceActivity.class);
                break;
            case R.id.self_tv05:
                openActivity(ApproveNameActivity.class);
                break;
            case R.id.self_tv06:
            ShareUtil.share(this, "http://zuche.test.beisheng.wang//uploads//20170524/zmlcms_1495619972994.png", "汽车", "来租车呀", "https://www.baidu.com/");
                break;
            case R.id.self_tv07:
                openActivity(SettingActivity.class);
                break;
            case R.id.home_tv01:
                mHomeTv01.setBackground(BaseCommonUtils.setBackgroundShap(this, 15, R.color.Car10, R.color.Car10));
                mHomeTv02.setBackgroundDrawable(BaseCommonUtils.setBackgroundShap(this, 0, R.color.Car2, R.color.Car2));
                openActivity(NearbyActivity.class);
                break;
            case R.id.home_tv02:
                mHomeTv02.setBackground(BaseCommonUtils.setBackgroundShap(this, 15, R.color.Car10, R.color.Car10));
                mHomeTv01.setBackgroundDrawable(BaseCommonUtils.setBackgroundShap(this, 0, R.color.Car2, R.color.Car2));
                openActivity(NearbyCarActivity.class);
                break;
            case R.id.use_car_bottom_tv03:
                startUseCar();
                break;
            case R.id.phone_img:
                BaseCommonUtils.call(this,"18602727134");
                break;
            case R.id.cancel_order_tv:
                mUserCarBottomLayout.setVisibility(View.GONE);
                mUserCarTitleLayout.setVisibility(View.GONE);
                mCancelOrderTv.setVisibility(View.GONE);
                mCarRentalLayout.setVisibility(View.VISIBLE);
                mCarSearchLayout.setVisibility(View.VISIBLE);
                mPhoneImg.setVisibility(View.VISIBLE);
                mBaseTitleTv.setText("玛儿特出行");
                break;
            case R.id.img_01:
                openActivity(ShopPromotionsActivity.class);
                break;
            case R.id.img_02:
                openActivity(MessageActivity.class);
                break;
            case R.id.img_03:

                break;
            case R.id.img_04:
                mAMap.setMyLocationStyle(myLocationStyle);
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EBMessageVO event) {
        if ("user_car".equals(event.getMessage())) {
            waitUseCar(event.getParams()[0]);
//            mDrawerLayout.openDrawer(GravityCompat.END);
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void waitUseCar(String orderid) {
        mOrderId = orderid;
        final RequestParams params = new RequestParams();
        if (AppApplication.getInstance().getUserInfoVO() != null)
            params.put("uin", AppApplication.getInstance().getUserInfoVO().getUin());

        params.put("orderid", orderid);
        String mUrl = Constant.WAIT_CAR_URL;
        HttpClientUtil.post(mActivity, this, mUrl, params, CarVO.class, this);
    }

    public void startUseCar() {
        final RequestParams params = new RequestParams();
        if (AppApplication.getInstance().getUserInfoVO() != null)
            params.put("uin", AppApplication.getInstance().getUserInfoVO().getUin());

        params.put("orderid", mOrderId);
        String mUrl = Constant.START_USER_CAR_URL;
        HttpClientUtil.post(mActivity, this, mUrl, params, CarVO.class, this);
    }

    @Override
    public void success(BaseVO vo) {
        CarVO carVO = (CarVO) vo;
        if (Constant.RESULT_SUCCESS_CODE.equals(carVO.getCode())) {
            if (carVO.getData() != null) {
                mUserCarBottomLayout.setVisibility(View.VISIBLE);
                mUserCarTitleLayout.setVisibility(View.VISIBLE);
                mCancelOrderTv.setVisibility(View.VISIBLE);
                mCarRentalLayout.setVisibility(View.GONE);
                mCarSearchLayout.setVisibility(View.GONE);
                mPhoneImg.setVisibility(View.GONE);
                mBaseTitleTv.setText("等待用车");

//            mAddressTitleTv.setText(location.getExtras().getString("AoiName"));
                mAddressDetailTv.setText(carVO.getData().getAddress());
                Picasso.with(this).load(carVO.getData().getPic()).into(mCarImg);
                mCarTitleTv.setText(carVO.getData().getTitle());
                mCarNumberTv.setText(carVO.getData().getNumber());
            } else {
                Bundle bundle = new Bundle();
                bundle.putString("orderid", mOrderId);
                open(BillingCarActivity.class, bundle, 0);
            }
        } else {
            showCustomToast(carVO.getInfo());
        }
    }

    @Override
    public void onBackPressed() {
        if (isShow()) {
//            dismissProgressDialog();
        } else {
            if (mBackPressed + 3000 > System.currentTimeMillis()) {
                finish();
                super.onBackPressed();
            } else
                showCustomToast("再次点击，退出" + this.getResources().getString(R.string.app_name));
            mBackPressed = System.currentTimeMillis();
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (keyEvent != null) {
            mKeyword = textView.getText().toString();
            GeocodeQuery query = new GeocodeQuery(mKeyword, "");
            mGeocoderSearch.getFromLocationNameAsyn(query);
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence s, int i, int i1, int i2) {
        if (!"".equals(s.toString())) {
            mMove = false;
            mKeyword = s.toString();
//            getDataByKeyword();

            GeocodeQuery query = new GeocodeQuery(mKeyword, "");
            mGeocoderSearch.getFromLocationNameAsyn(query);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onMyLocationChange(Location location) {
        mCityCode = location.getExtras().getString("CityCode");
        SharePreferenceUtil.putSharedpreferences(HomeActivity.this, "address", "lat", location.getLatitude() + "");
        SharePreferenceUtil.putSharedpreferences(HomeActivity.this, "address", "lo", location.getLongitude() + "");
        SharePreferenceUtil.putSharedpreferences(HomeActivity.this, "address", "cityCode", mCityCode);
        SharePreferenceUtil.putSharedpreferences(HomeActivity.this, "address", "city", location.getExtras().getString("City"));
        SharePreferenceUtil.putSharedpreferences(HomeActivity.this, "address", "district", location.getExtras().getString("District"));
        SharePreferenceUtil.putSharedpreferences(HomeActivity.this, "address", "aoiName", location.getExtras().getString("AoiName"));
//                mAddressTitleTv.setText(location.getExtras().getString("AoiName"));
//                mAddressDetailTv.setText(location.getExtras().getString("Address"));
        mThreadUtil = new ThreadUtil(HomeActivity.this, HomeActivity.this);
        mThreadUtil.start();
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        if (geocodeResult.getGeocodeAddressList().size() > 0) {
            GeocodeAddress geocodeAddress = geocodeResult.getGeocodeAddressList().get(0);
            LatLng latLng1 = new LatLng(geocodeAddress.getLatLonPoint().getLatitude(), geocodeAddress.getLatLonPoint().getLongitude());
            //设置中心点和缩放比例
            mAMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng1));
            mAMap.moveCamera(CameraUpdateFactory.zoomTo(15));
//            mAMap.setMyLocationStyle(myLocationStyle);


            SharePreferenceUtil.putSharedpreferences(HomeActivity.this, "address", "lat", geocodeAddress.getLatLonPoint().getLatitude() + "");
            SharePreferenceUtil.putSharedpreferences(HomeActivity.this, "address", "lo", geocodeAddress.getLatLonPoint().getLongitude() + "");
            SharePreferenceUtil.putSharedpreferences(HomeActivity.this, "address", "cityCode", mCityCode);

            SharePreferenceUtil.putSharedpreferences(HomeActivity.this, "address", "city", geocodeAddress.getCity());
            SharePreferenceUtil.putSharedpreferences(HomeActivity.this, "address", "district", geocodeAddress.getDistrict());
//            SharePreferenceUtil.putSharedpreferences(HomeActivity.this, "address", "aoiName", geocodeAddress.get);
//                mAddressTitleTv.setText(location.getExtras().getString("AoiName"));

//                mAddressDetailTv.setText(location.getExtras().getString("Address"));
            mCityCode = geocodeAddress.getAdcode();
            mThreadUtil = new ThreadUtil(HomeActivity.this, HomeActivity.this);
            mThreadUtil.start();
        }

    }
}