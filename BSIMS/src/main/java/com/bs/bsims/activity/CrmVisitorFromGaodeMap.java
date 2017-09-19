
package com.bs.bsims.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnCameraChangeListener;
import com.amap.api.maps2d.AMap.OnMapScreenShotListener;
import com.amap.api.maps2d.AMap.OnMapTouchListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.bs.bsims.R;
import com.bs.bsims.model.CrmContactVo;
import com.bs.bsims.utils.AnimationUtil;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomLog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.FileUtil;
import com.bs.bsims.view.BSIndexEditText;
import com.bs.bsims.view.BSRefreshListView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CrmVisitorFromGaodeMap extends BaseActivity implements
        OnCheckedChangeListener, OnClickListener, LocationSource,
        AMapLocationListener, OnPoiSearchListener, OnCameraChangeListener, OnMarkerClickListener, OnMapTouchListener, OnMapScreenShotListener {
    private MapView mapView;
    private AMap aMap;
    private UiSettings mUiSettings;
    private OnLocationChangedListener mListener;
    private Context mContext;
    private PoiSearch.Query mQuery;

    private String personCity;
    private int currentPage;
    private String keyWord = "";
    private BSRefreshListView map_listid;

    private TextView loadingfile1;
    private List<CrmContactVo> mList;
    GaodeContentList gaodelistadpter;

    private Marker locationMarker;
    private String aDress = "";// 地址上下缩略文字所有的拼接
    private String Lng = "";
    private String Lat = "";

    private String btmAdress = "";// 地址下面缩略文字
    private String btpAdress = "";// 地址上面的缩略文字

    private CrmContactVo rStartPioSearVo;

    // 声明AMapLocationClient类对象
    public static AMapLocationClient mLocationClient = null;

    // 声明mLocationOption对象
    public static AMapLocationClientOption mLocationOption = null;
    private BSIndexEditText mSerachEt;
    private String mSreenPath;// 截图路径
    private boolean mSreenSuOrFa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mapView.onCreate(savedInstanceState);
        mContext = this;

        init();
    }

    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            mUiSettings = aMap.getUiSettings();
            aMap.setOnCameraChangeListener(this);// 设置地图拖动事件
            aMap.setOnMarkerClickListener(this);// 设置大头针点击无效
            aMap.setOnMapTouchListener(this);
            aMap.clear();// 清除地图所有东西
        }

        locationAtNow();
        activate(mListener);// 开始定位
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();

        // addmarker();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /**
     * 往地图上添加marker
     * 
     * @param latLng
     */
    private void addMarker(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("");
        markerOptions.snippet("");
        markerOptions.visible(true);
        markerOptions.draggable(false);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.gaode_map_loc_ing)));
        if (locationMarker == null) {
            locationMarker = aMap.addMarker(markerOptions);
        }

    }

    @Override
    public void baseSetContentView() {
        // TODO Auto-generated method stub
        View layout = View.inflate(this, R.layout.crm_visitor_map, null);
        mContentLayout.addView(layout);
        mapView = (MapView) findViewById(R.id.crm_map);
        mapView.setVisibility(View.VISIBLE);
        loadingfile1 = (TextView) findViewById(R.id.loadingfile1);
        map_listid = (BSRefreshListView) findViewById(R.id.map_listid);
        gaodelistadpter = new GaodeContentList();
        map_listid.setAdapter(gaodelistadpter);
    }

    @Override
    public boolean getDataResult() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void updateUi() {
        // TODO Auto-generated method stub
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        mLoadingLayout.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);
        mTitleTv.setText("选择位置");
        // mTitleTv.setCompoundDrawablesWithIntrinsicBounds(null, null,
        // getResources().getDrawable(R.drawable.task_statistics_month_statusid01),
        // null);gaodeloaction_refresh
        mOkTv.setText("确认");
        mAtivityName_img.setVisibility(View.VISIBLE);
        mAtivityName_img.setBackgroundResource(R.drawable.gaodeloaction_refresh);
        mSerachEt = (BSIndexEditText) findViewById(R.id.edit_single_search);
    }

    @Override
    public void bindViewsListener() {
        // TODO Auto-generated method stub
        mOkTv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                /**
                 * 确认选中的位置
                 */

                // 消息不是来源于IM聊天，不需要截屏地图
                if (null != aDress && !aDress.equals("") && !getIntent().getBooleanExtra("isScreenShot", false)) {
                    Intent i = new Intent();
                    i.putExtra("adress", aDress.toString().trim());
                    i.putExtra("Lng", Lng);
                    i.putExtra("Lat", Lat);
                    setResult(1015, i);
                    CrmVisitorFromGaodeMap.this.finish();
                }
                else if (null != aDress && !aDress.equals("") && getIntent().getBooleanExtra("isScreenShot", false)) {
                    getMapScreenShot(arg0);
                }
                else {
                    CustomToast.showShortToast(mContext, "请选择位置!");
                }

            }
        });

        mAtivityName_img.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub mAtivityName_img
                deactivate();
                if (gaodelistadpter.mLists == null
                        || gaodelistadpter.mLists.size() == 0) {
                    if (null != aMap) {
                        aMap.clear();// 清除地图上大头针
                    }
                    AnimationUtil.setStartRotateAnimation(mContext, mAtivityName_img);
                    init();

                }
                else {
                    CustomToast.showLongToast(mContext, "当前已经是最新位置信息");
                }

            }
        });

        mSerachEt.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH && !v.getText().toString().trim().equals("")) {
                    // 先隐藏键盘
                    ((InputMethodManager) mSerachEt.getContext().getSystemService(
                            Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    CrmVisitorFromGaodeMap.this
                                            .getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);

                    Intent intent = new Intent();// pioValue
                    intent.putExtra("keyword", v.getText().toString().trim());
                    intent.setClass(mContext, GaoDeMapSearchActity.class);
                    startActivityForResult(intent, 2016);
                    return true;
                }
                return false;
            }
        });

    }

    /**
     * 对地图进行截屏
     */
    public void getMapScreenShot(View v) {
        aMap.getMapScreenShot(this);
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(mContext.getApplicationContext());
            // 设置定位回调监听
            mLocationClient.setLocationListener(this);
            // 初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            // 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
            mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
            // 设置是否返回地址信息（默认返回地址信息）
            mLocationOption.setNeedAddress(true);
            // 设置是否只定位一次,默认为false
            mLocationOption.setOnceLocation(true);// 只定位一次
            if (mLocationOption.isOnceLocationLatest()) {
                mLocationOption.setOnceLocationLatest(true);
                // 设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。
                // 如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会。
            }
            // 设置是否强制刷新WIFI，默认为强制刷新
            mLocationOption.setWifiActiveScan(true);
            // 设置是否允许模拟位置,默认为false，不允许模拟位置
            mLocationOption.setMockEnable(false);
            // // 设置定位间隔,单位毫秒,默认为2000ms
            // 给定位客户端对象设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            // 启动定位
            mLocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    /**
     * 定位地图 并且隐藏不必要的按钮
     */
    private void locationAtNow() {
        // TODO Auto-generated method stub
        boolean flag = false;
        aMap.setLocationSource(this);// 设置定位监听
        aMap.setMyLocationEnabled(!flag);// 是否可触发定位并显示定位层
        // aMap.getUiSettings().setMyLocationButtonEnabled(flag);// 设置默认定位按钮是否显示
        mUiSettings.setMyLocationButtonEnabled(flag);// 设置默认定位按钮是否显示
        mUiSettings.setScrollGesturesEnabled(flag);

        if (null != getIntent().getStringExtra("key")) {

            /**
             * 设置地图是否可以手势缩放大小
             */
            mUiSettings.setZoomGesturesEnabled(true);
            mUiSettings.setZoomControlsEnabled(false);
            mUiSettings.setMyLocationButtonEnabled(!flag);
            mUiSettings.setScrollGesturesEnabled(true);// 设置地图拖动yes
            aMap.moveCamera(CameraUpdateFactory.zoomTo(14));
            mSerachEt.setVisibility(View.VISIBLE);
        }
        else {

            /**
             * 设置地图是否可以手势缩放大小
             */
            mUiSettings.setZoomGesturesEnabled(false);
            mUiSettings.setZoomControlsEnabled(false);
            mUiSettings.setScrollGesturesEnabled(false);// 设置地图拖动no
            aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
            mSerachEt.setVisibility(View.GONE);
        }

        /**
         * 设置显示地图的默认比例尺
         */
        // mUiSettings.setScaleControlsEnabled(!flag);

        // 方法设置地图的缩放级别

    }

    public void onProviderEnabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCheckedChanged(RadioGroup arg0, int arg1) {
        // TODO Auto-generated method stub

    }

    // 定位完成之后回调函数
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        // TODO Auto-generated method stub
        if (mListener != null) {
            mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
            AnimationUtil.setStopRotateAnimation(mAtivityName_img);// 停止旋转动画
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {

                addMarker(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()));// 在中心点添加覆盖的大头针，移动大头针
                personCity = amapLocation.getCity();
                CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(
                        new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()),
                        16.0f);
                aMap.moveCamera(cu);
                currentPage = 0;
                rStartPioSearVo = null;
                // 第一个参数表示搜索字符串，第二个参数表示POI搜索类型
                // 第三个参数表示POI搜索区域（空字符串代表全国）
                mQuery = new PoiSearch.Query("", "", personCity);
                mQuery.setPageSize(20);// 设置每页最多返回多少条poiitem
                mQuery.setPageNum(currentPage);// 设置查第一页
                PoiSearch poiSearch = new PoiSearch(this, mQuery);
                // 搜索杭州市政府附件1000米范围
                poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(amapLocation
                        .getLatitude(), amapLocation.getLongitude()), 4000));
                poiSearch.setOnPoiSearchListener(this);
                CustomDialog.showProgressDialog(mContext, "位置信息搜索中...");
                poiSearch.searchPOIAsyn();// 发起异步搜索
            }

            // 设置查询条件
            else {
                // 获取位置失败
                CustomToast.showLongToast(mContext, "定位失败");

            }
        }
    }

    // 异步搜索pio数据之后回掉函数
    @Override
    public void onPoiSearched(PoiResult poiResult, int rCode) {
        CustomDialog.closeProgressDialog();
        // TODO Auto-generated method stub
        if (rCode == 0) {
            // 搜索POI的结果
            if (poiResult != null && poiResult.getQuery() != null) {
                // 是否是同一条
                if (poiResult.getQuery().equals(mQuery)) {
                    poiResult = poiResult;
                    // 取得搜索到的poiitems有多少页
                    int resultPages = poiResult.getPageCount();
                    // 取得第一页的poiitem数据，页数从数字0开始
                    List<PoiItem> poiItems = poiResult.getPois();
                    // 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
                    List suggestionCities = poiResult.getSearchSuggestionCitys();

                    if (poiItems != null && poiItems.size() > 0) {
                        mList = new ArrayList<CrmContactVo>();
                        for (int i = 0; i < poiItems.size(); i++) {
                            CustomLog.e("aaa", poiItems.get(i).getSnippet());
                            CustomLog.e("ccc", poiItems.get(i).getTitle());
                            // mList.get(i).setFullname(poiItems.get(i).getSnippet());
                            CrmContactVo v = new CrmContactVo();
                            v.setFullname(poiItems.get(i).getSnippet());
                            v.setHeadpic(poiItems.get(i).getTitle());
                            v.setLid(poiItems.get(i).getLatLonPoint().getLongitude() + "");// 经度
                            v.setLname(poiItems.get(i).getLatLonPoint().getLatitude() + "");// 纬度
                            v.setFalgecontant("0");
                            mList.add(v);
                        }
                    } else if (suggestionCities != null
                            && suggestionCities.size() > 0) {
                        mList = new ArrayList<CrmContactVo>();
                    } else {
                        // Toast.makeText(this, "没有搜索到相关数据", Toast.LENGTH_SHORT).show();
                        mList = new ArrayList<CrmContactVo>();
                    }
                    updateView1(mList);
                    return;
                }

            } else {
                // dissmissProgressDialog();// 隐藏对话框
                // Toast.makeText(this, "没有搜索到相关数据", Toast.LENGTH_SHORT).show();
                mList = new ArrayList<CrmContactVo>();
                updateView1(mList);
                return;
            }

        } else {
            // dissmissProgressDialog();// 隐藏对话框
            // Toast.makeText(this, "搜索失败,请检查网络连接！", Toast.LENGTH_SHORT).show();
            CustomToast.showShortToast(mContext, "网络延迟!");
        }

    }

    public class GaodeContentList extends BaseAdapter {
        private List<CrmContactVo> mLists;

        public GaodeContentList() {
            mLists = new ArrayList<CrmContactVo>();
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mLists.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return mLists.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup arg2) {
            // TODO Auto-generated method stub

            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.crm_visitor_map_apdater, null);
                holder.contants_name = (TextView) convertView.findViewById(R.id.text_name);
                holder.checkBox_up_check_selectall = (ImageView) convertView
                        .findViewById(R.id.checkBox_up_check_selectall);
                holder.allliney = (LinearLayout) convertView.findViewById(R.id.allliney);
                holder.text_phone = (TextView) convertView.findViewById(R.id.text_phone);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (mList.get(position).getFalgecontant().equals("1")) {
                holder.checkBox_up_check_selectall.setImageResource(R.drawable.common_ic_selected);
            }
            else {
                holder.checkBox_up_check_selectall.setImageResource(R.drawable.common_ic_unselect);
            }

            holder.allliney.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    for (int i = 0; i < mList.size(); i++) {
                        mLists.get(i).setFalgecontant("0");
                    }
                    aDress = mLists.get(position).getFullname() + mLists.get(position).getHeadpic();
                    Lng = mLists.get(position).getLid();
                    Lat = mLists.get(position).getLname();
                    mLists.get(position).setFalgecontant("1");
                    notifyDataSetChanged();
                }
            });

            holder.contants_name.setText(mLists.get(position).getFullname());
            holder.text_phone.setText(mLists.get(position).getHeadpic());
            return convertView;
        }
    }

    public static class ViewHolder
    {
        private ImageView checkBox_up_check_selectall;
        private TextView contants_name, text_phone;
        private LinearLayout allliney;
    }

    public void updateView1(List<CrmContactVo> mList1) {
        if (null != mList1 && mList1.size() > 0) {
            // 展示数据
            if (null != rStartPioSearVo) {
                mList1.add(0, rStartPioSearVo);
            }
            gaodelistadpter.mLists.clear();
            gaodelistadpter.mLists.addAll(mList1);
            for (int i = 0; i < gaodelistadpter.mLists.size(); i++) {
                gaodelistadpter.mLists.get(i).setFalgecontant("0");
            }
            gaodelistadpter.mLists.get(0).setFalgecontant("1");// 每条默认选中第一个
            aDress = gaodelistadpter.mLists.get(0).getFullname() + gaodelistadpter.mLists.get(0).getHeadpic();
            Lng = gaodelistadpter.mLists.get(0).getLid();
            Lat = gaodelistadpter.mLists.get(0).getLname();
            gaodelistadpter.notifyDataSetChanged();
            map_listid.setVisibility(View.VISIBLE);
        }
        else {
            map_listid.setVisibility(View.GONE);
            gaodelistadpter.mLists.clear();
            gaodelistadpter.notifyDataSetChanged();
            CustomToast.showLongToast(mContext, "没有搜索到相关数据!");
        }
    }

    // 移动地图转移大头针
    @Override
    public void onCameraChange(CameraPosition position) {
        // TODO Auto-generated method stub
        if (locationMarker != null) {
            LatLng latLng = position.target;
            locationMarker.setPosition(latLng);
        }
    }

    // 移动地图获取坐标
    @Override
    public void onCameraChangeFinish(CameraPosition position) {
        // TODO Auto-generated method stub
        if (locationMarker != null) {
            Lng = Lat = aDress = "";// 地图移动之后以前选择无效
            LatLng latLng = position.target;
            currentPage = 0;
            PoiSearch poiSearch = null;
            // 第一个参数表示搜索字符串，第二个参数表示POI搜索类型
            // 第三个参数表示POI搜索区域（空字符串代表全国）
            if (null != rStartPioSearVo) {
                // 说明是搜索之后回调的参数
                mQuery = new PoiSearch.Query("", "", rStartPioSearVo.getId());
                poiSearch = new PoiSearch(this, mQuery);
                poiSearch.setBound(new PoiSearch.SearchBound(new
                        LatLonPoint(Double.parseDouble(rStartPioSearVo.getLname())
                                , Double.parseDouble(rStartPioSearVo.getLid())), 4000));
                CustomDialog.showProgressDialog(mContext, "位置信息搜索中...");
            }else {
                mQuery = new PoiSearch.Query("", "", "");
                poiSearch = new PoiSearch(this, mQuery);
                poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(latLng.latitude,
                        latLng.longitude), 4000));
            }

            mQuery.setPageSize(20);// 设置每页最多返回多少条poiitem
            mQuery.setPageNum(currentPage);// 设置查第一页
            poiSearch.setOnPoiSearchListener(this);
            poiSearch.searchPOIAsyn();
        }
    }

    @Override
    public boolean onMarkerClick(Marker arg0) {
        // TODO Auto-generated method stub
        arg0.hideInfoWindow();// 隐藏info的信息
        return true;// 不自动定位到地图中心
    }

    /*
     * (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int,
     * android.content.Intent)
     */
    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        switch (arg0) {
            case 2016:
                if (arg1 == 2016) {
                    if (arg2 != null) {
                        Lng = Lat = aDress = "";// 地图移动之后以前选择无效
                        rStartPioSearVo = new CrmContactVo();
                        rStartPioSearVo = (CrmContactVo) arg2.getSerializableExtra("pioValue");
                        // v.setFullname(poiItems.get(i).getSnippet());
                        // v.setHeadpic(poiItems.get(i).getTitle());
                        // v.setLid(poiItems.get(i).getLatLonPoint().getLongitude() + "");// 经度
                        // v.setLname(poiItems.get(i).getLatLonPoint().getLatitude() + "");// 纬度
                        // v.setId(poiItems.get(i).getCityName());//城市
                        // currentPage = 0;
                        // 第一个参数表示搜索字符串，第二个参数表示POI搜索类型
                        // 第三个参数表示POI搜索区域（空字符串代表全国）
                        // mQuery = new PoiSearch.Query("", "", rStartPioSearVo.getId());
                        // mQuery.setPageSize(20);// 设置每页最多返回多少条poiitem
                        // mQuery.setPageNum(currentPage);// 设置查第一页
                        // PoiSearch poiSearch = new PoiSearch(this, mQuery);
                        // // 搜索杭州市政府附件1000米范围
                        // poiSearch.setBound(new PoiSearch.SearchBound(new
                        // LatLonPoint(Double.parseDouble(rStartPioSearVo.getLname())
                        // , Double.parseDouble(rStartPioSearVo.getLid())), 4000));
                        // poiSearch.setOnPoiSearchListener(this);
                        // CustomDialog.showProgressDialog(mContext, "位置信息搜索中...");
                        // poiSearch.searchPOIAsyn();// 发起异步搜索
                        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(
                                new LatLng(Double.parseDouble(rStartPioSearVo.getLname()), Double
                                        .parseDouble(rStartPioSearVo.getLid())),
                                16.0f);
                        aMap.moveCamera(cu);
                    }
                }
                break;
        }

    }

    /*
     * (non-Javadoc)
     * @see com.amap.api.maps2d.AMap.OnMapTouchListener#onTouch(android.view.MotionEvent)
     */
    @Override
    public void onTouch(MotionEvent arg0) {
        // TODO Auto-generated method stub
        boolean isMove = false;
        switch (arg0.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isMove = true;
                rStartPioSearVo = null;
                break;
            case MotionEvent.ACTION_MOVE:
                isMove = true;
                break;
            case MotionEvent.ACTION_UP:
                rStartPioSearVo = null;
                break;

            default:
                break;
        }
    }

    // 对地图区域截图
    @SuppressLint("NewApi")
    @Override
    public void onMapScreenShot(Bitmap bitmap) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        if (null == bitmap) {
            return;
        }
        try {
            int height = bitmap.getHeight();
            int width = bitmap.getWidth();
            float scaleWidth = ((float) 220) / width;
            float scaleHeight = ((float) 300) / height;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleHeight);// 使用后乘
            Bitmap newBM = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
            mSreenPath = FileUtil.getSaveFilePath(mContext) + sdf.format(new Date()) + ".png";
            FileOutputStream fos = new FileOutputStream(mSreenPath);
            mSreenSuOrFa = newBM.compress(CompressFormat.PNG, 80, fos);
            try {
                fos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (mSreenSuOrFa) {
                Intent i = new Intent();
                i.putExtra("adress", aDress.toString().trim());
                i.putExtra("Lng", Lng);
                i.putExtra("Lat", Lat);
                i.putExtra("path", mSreenPath);
                setResult(RESULT_OK, i);
                CrmVisitorFromGaodeMap.this.finish();
            }
            else {
                CustomToast.showShortToast(mContext, "位置消息确认失败!");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
