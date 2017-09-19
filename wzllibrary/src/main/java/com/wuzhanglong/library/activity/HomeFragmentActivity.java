package com.wuzhanglong.library.activity;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.wuzhanglong.library.R;
import com.wuzhanglong.library.fragment.BaseFragment;
import com.wuzhanglong.library.http.HttpClientUtil;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.utils.SharePreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public abstract class HomeFragmentActivity extends BaseActivity implements  EasyPermissions.PermissionCallbacks{

    public ViewPager mVpHome;
    public BottomNavigationBar mBottomNavigationBar;
    public ArrayList<BaseFragment> mFragmentList = new ArrayList<>();
    private static int tabLayoutHeight;
    private double mBackPressed;
    public abstract void initBottomBar();
    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;
    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.activity_home_fragment, mBaseContentLayout);
    }


    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void initView() {
        mFragmentList = (ArrayList<BaseFragment>) this.getIntent().getSerializableExtra("fragment_list");

        mVpHome = (ViewPager) findViewById(R.id.vp_home);
        mVpHome.setOffscreenPageLimit(4);
        mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        initBottomBar();

        mVpHome.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        });

//        tabLayoutHeight=mBottomNavigationBar.getMeasuredHeight();

        initPermissions();
    }


    @Override
    public void bindViewsListener() {
//        EventBus.getDefault().register(this);

        mBottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                mVpHome.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(int position) {
            }

            @Override
            public void onTabReselected(int position) {
            }
        });



        mVpHome.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mBottomNavigationBar.selectTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void getData() {
        HttpClientUtil.show(mThreadUtil);
    }

    @Override
    public void hasData(BaseVO vo) {

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
//        EventBus.getDefault().unregister(this);
        if(mLocationClient!=null)
            mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
    }

//    /**
//     * 菜单显示隐藏动画
//     * @param showOrHide
//     */
//    private void startAnimation(boolean showOrHide){
//        final ViewGroup.LayoutParams layoutParams = mBottomNavigationBar.getLayoutParams();
//        ValueAnimator valueAnimator;
//        ObjectAnimator alpha;
//        if(!showOrHide){
//            valueAnimator = ValueAnimator.ofInt(tabLayoutHeight, 0);
//            alpha = ObjectAnimator.ofFloat(mBottomNavigationBar, "alpha", 1, 0);
//        }else{
//            valueAnimator = ValueAnimator.ofInt(0, tabLayoutHeight);
//            alpha = ObjectAnimator.ofFloat(mBottomNavigationBar, "alpha", 0, 1);
//        }
//        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                layoutParams.height= (int) valueAnimator.getAnimatedValue();
//                mBottomNavigationBar.setLayoutParams(layoutParams);
//            }
//        });
//        AnimatorSet animatorSet=new AnimatorSet();
//        animatorSet.setDuration(500);
//        animatorSet.playTogether(valueAnimator,alpha);
//        animatorSet.start();
//    }

//    @Subscribe
//    public void onEventMainThread(EBMessageVO event) {
//        if ("hide_tab".equals(event.getMessage())) {
//            startAnimation(false);
//        }else {
//            startAnimation(true);
//        }
//    }

//
//    @Override
//    public boolean supportSlideBack() {
//        return false;
//    }

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

    public void initPermissions() {
        String[] perms = {android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // 县市定位
            initMap();
        } else {
            EasyPermissions.requestPermissions(this, "请允许打开权限权限", 1, perms);
        }

    }

    public void initMap() {
        mLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationClient.setLocationListener(locationListener);
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        mLocationOption.setOnceLocation(true);
        mLocationOption.setInterval(0);
        mLocationOption.setNeedAddress(true);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
    }

    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                try {
                    double lat = amapLocation.getLatitude();//获取纬度
                    double lo = amapLocation.getLongitude();//获取经度
                    String address = amapLocation.getAddress();
                    String city=amapLocation.getCity();
                    // CoordType.GPS 待转换坐标类型
//                    CoordinateConverter converter = new CoordinateConverter(LogoActivity.this);
//                    DPoint dPoint = new DPoint();
//                    dPoint.setLatitude(lat);
//                    dPoint.setLongitude(lo);
//                    converter.from(CoordinateConverter.CoordType.GPS);
//                    converter.coord(dPoint);
//                    DPoint newDpoint = converter.convert();

                    SharePreferenceUtil.putSharedpreferences(HomeFragmentActivity.this, "address", "lat", lat + "");
                    SharePreferenceUtil.putSharedpreferences(HomeFragmentActivity.this, "address", "lo", lo + "");
                    SharePreferenceUtil.putSharedpreferences(HomeFragmentActivity.this, "address", "detail_address", address);
                    SharePreferenceUtil.putSharedpreferences(HomeFragmentActivity.this, "address", "city", city);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void onPermissionsGranted(int i, List<String> list) {
        initMap();
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
}
