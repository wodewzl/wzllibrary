
package com.bs.bsims.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnInfoWindowClickListener;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.bs.bsims.R;
import com.bs.bsims.adapter.AddByDepartmentAdapter;
import com.bs.bsims.adapter.GaodeLocationItemApdater;
import com.bs.bsims.adapter.HeadAdapter;
import com.bs.bsims.calendarmanager.ui.datedialog.BSCalendarPopupWindowUtils;
import com.bs.bsims.calendarmanager.ui.datedialog.BSCalendarPopupWindowUtils.KcalendarCallback;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.interfaces.EmployeeOnclickCallback;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.model.GaoIndexViewInfo;
import com.bs.bsims.model.PDFOutlineElementVO;
import com.bs.bsims.model.ResultVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.utils.DepartmentMoreUtis;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.utils.UrlUtil;
import com.bs.bsims.view.BSCircleImageView;
import com.bs.bsims.view.BSIndexEditText;
import com.bs.bsims.view.BSRefreshListView;
import com.bs.bsims.view.BSRefreshListView.OnRefreshListener;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GaoDeMapLoactionIndexActivity extends BaseActivity implements OnMarkerClickListener,
        OnInfoWindowClickListener, InfoWindowAdapter, OnMapClickListener, OnClickListener,
        EmployeeOnclickCallback {
    private List<Bitmap> listBitmaps = new ArrayList<Bitmap>();
    private MapView mapView;
    private AMap aMap;
    private GaoIndexViewInfo viewInfos;
    private TextView img_head_back;
    private Bitmap bms;
    Bitmap bm1;
    Bitmap bm2;
    Bitmap bm3;
    Canvas canvas = null;
    private Marker markers = null;// ��ǰ�켣��ͼ��
    private Marker currentMarker = null;// ��¼��ǰ��������ĸ�������
    private JournalListPopupWindwos mJpop;
    private AddByDepartmentAdapter mAddByDepartmentAdapter;
    private TextView gaode_location_online, gaode_location_onlineno, location_adressinfo,
            gaode_location_all,
            location_departinfo,
            location_timeinfo, txt_comm_head_right;
    private String mDid;
    private DepartmentMoreUtis mDepartmentUtis;
    private RelativeLayout comm_head_layout;
    private List<EmployeeVO> mCcVOList;
    private DisplayImageOptions mOptions;
    private int TabelIndeX = 0;

    private TextView detailinfo, loadingfile1;// 第一个选项卡地图
    private TextView shoukuan_record;// 第二个选项卡人员列表
    private LinearLayout gaode_tablehostone;// 第一个选项卡的布局
    private LinearLayout gaode_tablehostwo;// 第二个选项卡的布局
    private BSRefreshListView crm_business_indexlistview;// 第二个选项卡的下拉刷新
    private String dateTime = "";// 首页时间筛选
    private LinearLayout location_all_ly;
    List<EmployeeVO> allList1;// 第二个人员的数据源

    private String getState = "0";// 拿哪个接口的数据

    private String dateTimeYearAndMonth;

    private BSCalendarPopupWindowUtils pop;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            mAddByDepartmentAdapter.notifyDataSetChanged();
        };
    };
    // private GridView head_loaction;
    private Gallery head_loaction;

    private HeadAdapter headAdapter;

    private ImageView imageView, imageView1;
    private String dpartentId = "0";

    private List<EmployeeVO> e_listEmployeeVOs = new ArrayList<EmployeeVO>();;
    private Activity mactivity;
    private TextView mAllPeople;
    // 添加员工使用
    protected List<EmployeeVO> mDataList = new ArrayList<EmployeeVO>();
    private GaodeLocationItemApdater mAdapter;
    private BSIndexEditText mClearEditText;

    // 日程集合
    private List<String> mDatelist = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapView.onCreate(savedInstanceState);
        mOptions = CommonUtils.initImageLoaderOptions();
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
        ImageLoader.getInstance().init(configuration);
        mactivity = this;
        baseHeadLayout.setBackgroundColor(getResources().getColor(R.color.transparent));
    }

    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.getUiSettings().setZoomControlsEnabled(false);
        }
    }

    private void moveToForbiddenCity() {
        if (!viewInfos.getOnlinenum().equals("0")) {
            CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(
                    new LatLng(Double.parseDouble(viewInfos.getOnlines().get(0).getT_lat()), Double
                            .parseDouble(viewInfos.getOnlines().get(0).getT_lon())),
                    13.0f);
            aMap.moveCamera(cu);
        }
        else {
            CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(
                    new LatLng(Double.parseDouble(viewInfos.getOfflines().get(0).getT_lat()),
                            Double
                                    .parseDouble(viewInfos.getOfflines().get(0).getT_lon())),
                    13.0f);
            aMap.moveCamera(cu);
        }
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

    @Override
    public boolean onMarkerClick(Marker marker) {
        currentMarker = marker;
        currentMarker.showInfoWindow();

        for (int i = 0; i < e_listEmployeeVOs.size(); i++) {
            if (e_listEmployeeVOs.get(i).getE_markers().toString().equals(marker.toString())) {
                head_loaction.setSelection(i);
            }
        }
        return false;// 返回false表示点击marker自动移动地图

    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View infoWindow = getLayoutInflater()
                .inflate(R.layout.gaode_location_showindown_info, null);
        location_adressinfo = (TextView) infoWindow.findViewById(R.id.location_adressinfo);
        location_timeinfo = (TextView) infoWindow.findViewById(R.id.location_timeinfo);
        location_departinfo = (TextView) infoWindow.findViewById(R.id.location_departinfo);
        location_departinfo.setText(((EmployeeVO) marker.getObject()).getFullname() + " |"
                + ((EmployeeVO) marker.getObject()).getDname() + " |"
                + ((EmployeeVO) marker.getObject()).getPname());
        location_adressinfo.setText(((EmployeeVO) marker.getObject()).getT_address());
        location_timeinfo.setText(DateUtils.parseDateDayAndHour(((EmployeeVO) marker
                .getObject())
                .getT_addtime()));
        return infoWindow;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent i = new Intent(GaoDeMapLoactionIndexActivity.this,
                GaoDeMapPersonTrajectoryActivity.class);
        i.putExtra("sid", ((EmployeeVO) marker.getObject()).getUserid());
        i.putExtra("username", ((EmployeeVO) marker.getObject()).getFullname());
        i.putExtra("date", dateTime);
        startActivity(i);
    }

    @Override
    public void onMapClick(LatLng arg0) {
        if (currentMarker != null) {
            currentMarker.hideInfoWindow();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.gaode_location_all:
                gaode_location_all.setBackgroundResource(R.drawable.linearystorkpush);
                gaode_location_online.setBackgroundResource(R.drawable.linearystorkpush4);
                gaode_location_onlineno.setBackgroundResource(R.drawable.linearystorkpush3);

                ClearALLmarker();// 清除所有覆盖物
                List<EmployeeVO> allList = new ArrayList<EmployeeVO>();
                if (viewInfos.getOfflines() != null)
                    allList.addAll(viewInfos.getOfflines());
                if (viewInfos.getOnlines() != null)
                    allList.addAll(viewInfos.getOnlines());
                AddGridViewByDpartHead1(allList);

                break;

            case R.id.gaode_location_online:
                // gaode_location_online.setBackgroundResource(R.drawable.linearystorkpush);
                // gaode_location_onlineno.setBackgroundResource(R.drawable.linearystorkpush3);
                // 点击获取在线人数信息

                gaode_location_all.setBackgroundResource(R.drawable.linearystorkpush1);
                gaode_location_online.setBackgroundResource(R.drawable.linearystorkpush5);
                gaode_location_onlineno.setBackgroundResource(R.drawable.linearystorkpush3);
                ClearALLmarker();// 清除所有覆盖物
                if (!viewInfos.getOnlinenum().equals("0")) {
                    AddGridViewByDpartHead(viewInfos.getOnlines(), "");
                }
                else {
                    CustomToast.showShortToast(GaoDeMapLoactionIndexActivity.this, "当前没有在线员工~");
                    AddGridViewByDpartHead(new ArrayList<EmployeeVO>(), "");
                }
                // intent.setClass(this, AddByPersonActivity.class);
                // mDataList.clear();
                //
                // List<EmployeeVO> onlinelist = new ArrayList<EmployeeVO>();
                // for (int i = 0; i < viewInfos.getOnlines().size(); i++) {
                // EmployeeVO onlineVO = new EmployeeVO();
                // onlineVO.setFullname(viewInfos.getOnlines().get(i).getFullname());
                // onlineVO.setPname(viewInfos.getOnlines().get(i).getPname());
                // onlineVO.setDname(viewInfos.getOnlines().get(i).getDname());
                // onlineVO.setUserid(viewInfos.getOnlines().get(i).getUserid());
                // onlinelist.add(onlineVO);
                // }
                //
                // mDataList.addAll(onlinelist);
                // intent.putExtra("checkboxlist", (Serializable) mDataList);
                // startActivityForResult(intent, 2014);

                break;
            case R.id.gaode_location_onlineno:
                // gaode_location_online.setBackgroundResource(R.drawable.linearystorkpush1);
                // gaode_location_onlineno.setBackgroundResource(R.drawable.linearystorkpush2);
                gaode_location_all.setBackgroundResource(R.drawable.linearystorkpush1);
                gaode_location_online.setBackgroundResource(R.drawable.linearystorkpush4);
                gaode_location_onlineno.setBackgroundResource(R.drawable.linearystorkpush2);
                ClearALLmarker();// 清除所有覆盖物
                if (!viewInfos.getOfflinenum().equals("0")) {
                    AddGridViewByDpartHead(viewInfos.getOfflines(), "2");
                }

                else {
                    CustomToast.showShortToast(GaoDeMapLoactionIndexActivity.this, "当前没有离线员工~");
                    AddGridViewByDpartHead(new ArrayList<EmployeeVO>(), "2");
                }

                break;

            case R.id.txt_comm_head_activityName:
                mDid = "";
                if (!mJpop.isShowing()) {
                    mJpop.showAsDropDown(txt_comm_head_activityNamefather,
                            txt_comm_head_activityNamefather.getLayoutParams().width / 2, 1);
                } else {
                    mJpop.dismiss();
                }
                break;
            case R.id.all_people:

                intent.setClass(this, AddByPersonActivity.class);
                mDataList.clear();

                List<EmployeeVO> list = new ArrayList<EmployeeVO>();
                for (int i = 0; i < e_listEmployeeVOs.size(); i++) {
                    EmployeeVO offlineVO = new EmployeeVO();
                    offlineVO.setFullname(e_listEmployeeVOs.get(i).getFullname());
                    offlineVO.setPname(e_listEmployeeVOs.get(i).getPname());
                    offlineVO.setTel(e_listEmployeeVOs.get(i).getPhone());
                    offlineVO.setHeadpic(e_listEmployeeVOs.get(i).getHeadpic());
                    offlineVO.setDname(e_listEmployeeVOs.get(i).getDname());
                    offlineVO.setUserid(e_listEmployeeVOs.get(i).getUserid());
                    list.add(offlineVO);
                }
                mDataList.addAll(list);

                intent.putExtra("is_new_data", true);
                intent.putExtra("checkboxlist", (Serializable) mDataList);
                intent.putExtra("state", "2");
                // startActivityForResult(intent, 2014);
                startActivity(intent);

                break;

            case R.id.detailinfo:

                if (null != viewInfos && viewInfos.getCode().equals("200")) {
                    gaode_tablehostone.setVisibility(View.VISIBLE);
                    gaode_tablehostwo.setVisibility(View.GONE);
                }

                TabelIndeX = 0;
                detailinfo.setTextColor(getResources().getColor(R.color.bule_go));
                shoukuan_record.setTextColor(getResources().getColor(R.color.white));
                detailinfo.setBackgroundDrawable(getResources().getDrawable(
                        R.drawable.corners_tab_left_select1));
                shoukuan_record.setBackgroundDrawable(getResources().getDrawable(
                        R.drawable.corners_tab_right_normal1));
                break;

            case R.id.shoukuan_record:

                if (null != viewInfos && viewInfos.getCode().equals("200")) {
                    gaode_tablehostwo.setVisibility(View.VISIBLE);
                    gaode_tablehostone.setVisibility(View.GONE);
                }
                PersonList p = new PersonList();
                allList1 = new ArrayList<EmployeeVO>();
                if (null != viewInfos.getOnlines())
                    allList1.addAll(viewInfos.getOnlines());
                if (null != viewInfos.getOfflines())
                    allList1.addAll(viewInfos.getOfflines());
                if (null != viewInfos.getOthers())
                    allList1.addAll(viewInfos.getOthers());
                p.allLists = allList1;
                crm_business_indexlistview.setAdapter(p);
                TabelIndeX = 1;

                shoukuan_record.setTextColor(getResources().getColor(R.color.bule_go));
                detailinfo.setTextColor(getResources().getColor(R.color.white));
                detailinfo.setBackgroundDrawable(getResources().getDrawable(
                        R.drawable.corners_tab_left_normal1));
                shoukuan_record.setBackgroundDrawable(getResources().getDrawable(
                        R.drawable.corners_tab_right_select2));

                crm_business_indexlistview
                        .setonRefreshListener(new OnRefreshListener() {

                            @Override
                            public void onRefresh() {
                                new ThreadUtil(mactivity,
                                        GaoDeMapLoactionIndexActivity.this).start();
                            }
                        });
                break;

            case R.id.img_head_backnew:
                this.finish();
                break;

        }

    }

    // protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    // if (resultCode == 2014) {
    // if (data == null)
    // return;
    // mDataList.clear();
    // mDataList = (List<EmployeeVO>) data.getSerializableExtra("checkboxlist");
    //
    // if (null != e_listEmployeeVOs) {
    // for (int i = 0; i < e_listEmployeeVOs.size(); i++) {
    // if (mDataList.get(0).getUserid().equals(e_listEmployeeVOs.get(i).getUserid())) {
    // Marker marker = e_listEmployeeVOs.get(i).getE_markers();// 拿到对应的覆盖物
    // if (null != currentMarker) {
    // currentMarker.hideInfoWindow();
    // }
    //
    // currentMarker = marker;// 标记为当前的maker
    // marker.showInfoWindow();
    // CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(
    // new LatLng(
    // Double.parseDouble(e_listEmployeeVOs.get(i).getT_lat()),
    // Double
    // .parseDouble(e_listEmployeeVOs.get(i).getT_lon())),
    // 14.0f);
    // aMap.moveCamera(cu);
    // head_loaction.setSelection(i);
    // break;
    // }
    //
    // }
    // }
    // }
    // }

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.gaode_location_activiry_index, mContentLayout);
        mHeadLayout.setVisibility(View.GONE);
        dateTime = DateUtils.getCurrentDate();

    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    private boolean getDataTableHostOne() {
        Gson gson = new Gson();
        Map map = new HashMap<String, String>();
        map.put("datetime", dateTime);
        String urlStr = UrlUtil.getUrlByMap1(Constant.GAODE_LOACION_INDEX_BYDEPART, map);
        String jsonUrlStr;
        try {
            jsonUrlStr = HttpClientUtil.get(urlStr, Constant.ENCODING).trim();
            viewInfos = gson.fromJson(jsonUrlStr, GaoIndexViewInfo.class);
            if (viewInfos.getCode().equals("400")) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            viewInfos = null;
        }

        return false;
    }

    public boolean getData() {
        if (getState.equals("1")) {
            return getDataClarDates();
        }
        else {
            return getDataTableHostOne();
        }

    }

    private boolean getDataClarDates() {
        Gson gson = new Gson();
        Map map = new HashMap<String, String>();
        map.put("datetime", dateTimeYearAndMonth);
        String urlStr = UrlUtil.getUrlByMap1(Constant.GAODE_LOACION_INDEX_CLEADENRDATERED, map);
        String jsonUrlStr;
        try {
            jsonUrlStr = HttpClientUtil.get(urlStr, Constant.ENCODING).trim();
            viewInfos = gson.fromJson(jsonUrlStr, GaoIndexViewInfo.class);
            if (viewInfos.getCode().equals("400")) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            viewInfos = null;
        }

        return false;
    }

    @Override
    public void updateUi() {

    }

    @Override
    public void initView() {
        mapView = (MapView) findViewById(R.id.map);
        gaode_location_online = (TextView) findViewById(R.id.gaode_location_online);
        gaode_location_onlineno = (TextView) findViewById(R.id.gaode_location_onlineno);
        gaode_location_all = (TextView) findViewById(R.id.gaode_location_all);
        gaode_location_online.setOnClickListener(this);
        gaode_location_onlineno.setOnClickListener(this);
        gaode_location_all.setOnClickListener(this);
        loadingfile1 = (TextView) findViewById(R.id.loadingfile1);
        // head_loaction = (GridView) findViewById(R.id.send_second_person_gv);
        head_loaction = (Gallery) findViewById(R.id.send_second_person_gv);
        txt_comm_head_right = (TextView) findViewById(R.id.txt_comm_head_rightnew);
        txt_comm_head_right.setText(DateUtils.getCurrentDate());
        init();
        if (markers != null) {
            markers.destroy();
        }
        mTitleTv.setText("即时定位");
        comm_head_layout = (RelativeLayout) findViewById(R.id.comm_head_layout);
        // mTitleTv.setCompoundDrawablesWithIntrinsicBounds(null, null,
        // getResources().getDrawable(R.drawable.pop_down), null);
        // mTitleTv.setCompoundDrawablePadding(30);

        mAllPeople = (TextView) findViewById(R.id.all_people);
        // mAllPeople.setVisibility(View.GONE);
        mAdapter = new GaodeLocationItemApdater(this);
        head_loaction.setAdapter(mAdapter);
        img_head_back = (TextView) findViewById(R.id.img_head_backnew);
        detailinfo = (TextView) findViewById(R.id.detailinfo);// 第一个选项卡地图
        shoukuan_record = (TextView) findViewById(R.id.shoukuan_record);// 第二哥选项卡列表
        gaode_tablehostone = (LinearLayout) findViewById(R.id.gaode_tablehostone);// 第一个布局
        gaode_tablehostwo = (LinearLayout) findViewById(R.id.gaode_tablehostwo);// 第二个布局
        crm_business_indexlistview = (BSRefreshListView) findViewById(R.id.crm_business_indexlistview);// 第二个布局的下拉刷新控件
        location_all_ly = (LinearLayout) findViewById(R.id.location_all_ly);

        // 第二个列表页的搜索
        mClearEditText = (BSIndexEditText) this.findViewById(R.id.edit_single_search);
        // 根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null)
                    filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    // 过滤搜索
    private void filterData(String filterStr) {
        PersonList p = new PersonList();
        if (filterStr.equals("")) {
            allList1 = new ArrayList<EmployeeVO>();
            if (null != viewInfos.getOnlines())
                allList1.addAll(viewInfos.getOnlines());
            if (null != viewInfos.getOfflines())
                allList1.addAll(viewInfos.getOfflines());
            if (null != viewInfos.getOthers())
                allList1.addAll(viewInfos.getOthers());
            p.allLists.clear();
            p.allLists = allList1;
            crm_business_indexlistview.setAdapter(p);
            TabelIndeX = 1;
        }
        else {
            List<EmployeeVO> allListSearch = new ArrayList<EmployeeVO>();
            // 第二个人员的数据源
            // 这个搜索算法很愚 ，但是吴张龙支持
            for (int i = 0; i < allList1.size(); i++) {
                if (allList1.get(i).getFullname().contains(filterStr.trim().toString())) {
                    allListSearch.add(allList1.get(i));
                }
                else {
                    continue;
                }
            }
            if (allListSearch.size() == 0) {
                CustomToast.showLongToast(mactivity, "没有搜索结果~");
            }
            else {
                p.allLists.clear();
                p.allLists = allListSearch;
                crm_business_indexlistview.setAdapter(p);
                TabelIndeX = 1;
            }

        }
    }

    @Override
    public void bindViewsListener() {
        aMap.setOnMarkerClickListener(this);
        aMap.setInfoWindowAdapter(this);
        aMap.setOnInfoWindowClickListener(this);
        aMap.setOnMapClickListener(this);
        head_loaction.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int postion1, long postion) {

                if (null != e_listEmployeeVOs) {
                    Marker marker = e_listEmployeeVOs.get(postion1).getE_markers();// 拿到对应的覆盖物

                    if (null != currentMarker) {
                        currentMarker.hideInfoWindow();
                    }

                    currentMarker = marker;// 标记为当前的maker
                    marker.showInfoWindow();
                    CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(
                            new LatLng(
                                    Double.parseDouble(e_listEmployeeVOs.get(postion1).getT_lat()),
                                    Double
                                            .parseDouble(e_listEmployeeVOs.get(postion1).getT_lon())),
                            14.0f);
                    aMap.moveCamera(cu);
                }
            }
        });

        head_loaction.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int postion1, long postion) {
                if (null != e_listEmployeeVOs && e_listEmployeeVOs.size() > 0
                        && e_listEmployeeVOs.size() > postion) {
                    Marker marker = e_listEmployeeVOs.get((int) postion).getE_markers();// 拿到对应的覆盖物

                    if (null != currentMarker) {
                        currentMarker.hideInfoWindow();
                    }

                    currentMarker = marker;// 标记为当前的maker
                    marker.showInfoWindow();
                    CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(
                            new LatLng(
                                    Double.parseDouble(e_listEmployeeVOs.get(postion1).getT_lat()),
                                    Double
                                            .parseDouble(e_listEmployeeVOs.get(postion1).getT_lon())),
                            14.0f);
                    aMap.moveCamera(cu);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        txt_comm_head_right.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (pop != null) {
                    if (pop.isShowing()) {
                        return;
                    }
                }
                pop = new BSCalendarPopupWindowUtils(
                        GaoDeMapLoactionIndexActivity.this, comm_head_layout,
                        new KcalendarCallback() {

                            @Override
                            public void kcalendarViewClick(String date) {
                                // TODO Auto-generated method stub
                                // 切换日历
                                if (date.length() < 8) {
                                    getState = "1";
                                    dateTimeYearAndMonth = date;
                                }
                                // 点击日期
                                else {
                                    getState = "0";
                                    dateTime = date;
                                    ClearALLmarker();
                                    CustomDialog.showProgressDialog(mactivity, "正在获取最新数据..");
                                }
                                new ThreadUtil(mactivity, GaoDeMapLoactionIndexActivity.this)
                                        .start();
                            }
                        }, true, null, mDatelist, "");

            }
        });
        mAllPeople.setOnClickListener(this);
        detailinfo.setOnClickListener(this);
        shoukuan_record.setOnClickListener(this);
        img_head_back.setOnClickListener(this);
    }

    @Override
    public void executeSuccess() {
        baseHeadLayout.setBackgroundColor(getResources().getColor(R.color.title_father));
        // 如果是获取日历红点
        if (getState.equals("1")) {
            if (null != viewInfos && null != viewInfos.getDateTime()) {
                // 标记日历红点
                String[] str = viewInfos.getDateTime().split(",");
                List mDatelistCopy = new ArrayList<String>();
                for (int i = 0; i < str.length; i++) {
                    mDatelistCopy.add(str[i]);
                }
                pop.UpdateMaker(mDatelistCopy);
            }
            return;
        }
        // 如果没有数据然后日历切换回来的情况下
        CustomDialog.closeProgressDialog();
        loadingfile1.setVisibility(View.GONE);
        mLoading.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
        txt_comm_head_right.setText(dateTime);
        // txt_comm_head_right.setVisibility(View.VISIBLE);
        // mTitleTv.setOnClickListener(this);
        gaode_location_all.setBackgroundResource(R.drawable.linearystorkpush);
        gaode_location_online.setBackgroundResource(R.drawable.linearystorkpush4);
        gaode_location_onlineno.setBackgroundResource(R.drawable.linearystorkpush3);
        if (!viewInfos.getOnlinenum().equals("0") && !viewInfos.getOfflinenum().equals("0")) {
            List<EmployeeVO> list = new ArrayList<EmployeeVO>();
            list.addAll(viewInfos.getOfflines());
            list.addAll(viewInfos.getOnlines());
            AddGridViewByDpartHead1(list);

        }
        else if (!viewInfos.getOnlinenum().equals("0") && viewInfos.getOfflinenum().equals("0")) {
            AddGridViewByDpartHead(viewInfos.getOnlines(), "");

        }
        else {
            if (viewInfos.getOfflinenum().equalsIgnoreCase("0")) {
                CustomToast.showLongToast(GaoDeMapLoactionIndexActivity.this, "没有最新的位置信息~");
            }
            AddGridViewByDpartHead(viewInfos.getOfflines(), "2");

        }

        if (TabelIndeX == 1) {
            gaode_tablehostone.setVisibility(View.GONE);
            gaode_tablehostwo.setVisibility(View.VISIBLE);
        }
        else {
            gaode_tablehostone.setVisibility(View.VISIBLE);
            gaode_tablehostwo.setVisibility(View.GONE);
        }

        PersonList p = new PersonList();
        List<EmployeeVO> allList1 = new ArrayList<EmployeeVO>();
        if (viewInfos.getOnlines() != null)
            allList1.addAll(viewInfos.getOnlines());
        if (viewInfos.getOfflines() != null)
            allList1.addAll(viewInfos.getOfflines());
        if (viewInfos.getOthers() != null)
            allList1.addAll(viewInfos.getOthers());
        p.allLists = allList1;
        crm_business_indexlistview.setAdapter(p);
        crm_business_indexlistview.onRefreshComplete();

        gaode_location_online.setText("在线(" + viewInfos.getOnlinenum() + ")");
        gaode_location_onlineno.setText("离线(" + viewInfos.getOfflinenum() + ")");
        moveToForbiddenCity();// 地图移动到中心点

        // 标记日历红点
        if (viewInfos.getDateTime() != null) {
            String[] str = viewInfos.getDateTime().split(",");
            for (int i = 0; i < str.length; i++) {
                mDatelist.add(str[i]);
            }
        }

    }

    @Override
    public void executeFailure() {
        CustomDialog.closeProgressDialog();
        CustomDialog.backgroundAlphaEnd(mactivity);
        if (viewInfos == null) {
            mLoading.setVisibility(View.VISIBLE);
            mContentLayout.setVisibility(View.GONE);
            mLoadingLayout.setVisibility(View.VISIBLE);
            loadingfile1.setVisibility(View.GONE);
            CommonUtils.setNonetIcon(GaoDeMapLoactionIndexActivity.this, mLoading, this);
        }
        else {
            baseHeadLayout.setBackgroundColor(getResources().getColor(R.color.title_father));
            mLoading.setVisibility(View.GONE);
            mContentLayout.setVisibility(View.VISIBLE);
            mLoadingLayout.setVisibility(View.GONE);
            txt_comm_head_right.setText(dateTime);
            gaode_tablehostwo.setVisibility(View.GONE);
            gaode_tablehostone.setVisibility(View.GONE);
            loadingfile1.setVisibility(View.VISIBLE);
        }

    }

    private class JournalListPopupWindwos extends PopupWindow implements OnClickListener {
        private Context mContext;
        private Activity mActivity;
        private ListView mListView;
        private LinearLayout mMoreLayout, mPostLayout;
        private Button mOkBt;
        private int mType;
        private TextView textViw01, textViw02, textViw03, textViw04, textViw05;
        private boolean mOne = true;
        private ExpandableListView mExpandableListView;

        public JournalListPopupWindwos(Context context, View parent, int type) {
            this.mContext = context;
            this.mActivity = (Activity) context;
            View view = View.inflate(context, R.layout.locationdepart_list_title, null);
            view.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View arg0, MotionEvent arg1) {
                    dismiss();
                    return false;
                }
            });
            mListView = (ListView) view.findViewById(R.id.lv_department);
            mListView.setAdapter(mAddByDepartmentAdapter);
            mOkBt = (Button) view.findViewById(R.id.ok_bt);
            mOkBt.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<PDFOutlineElementVO> list = new ArrayList<PDFOutlineElementVO>();
                    for (int i = 0; i < mAddByDepartmentAdapter.mfilelist.size(); i++) {
                        if (mAddByDepartmentAdapter.mfilelist.get(i).isSelect()) {
                            list.add(mAddByDepartmentAdapter.mfilelist.get(i));
                        }
                    }
                    if (list.size() == 1) {
                        mDid = list.get(0).getDepartmentandwmployee().getDepartmentid();
                        if (mDid.equals("-1")) {
                            dpartentId = "0";
                            mTitleTv.setText("全部部门位置信息");
                        }
                        else {
                            mTitleTv.setText(list.get(0).getDepartmentandwmployee().getDname()
                                    + "位置信息");
                            dpartentId = mDid;
                        }

                    } else if (list.size() == 2) {
                        mDid = list.get(1).getDepartmentandwmployee().getDepartmentid();
                        dpartentId = mDid;
                        mTitleTv.setText(list.get(1).getDepartmentandwmployee().getDname() + "位置信息");
                    } else if (list.size() > 2) {
                        mDid = list.get(0).getDepartmentandwmployee().getDepartmentid();
                        dpartentId = mDid;
                        mTitleTv.setText(list.get(0).getDepartmentandwmployee().getDname() + "位置信息");

                    } else if (list.size() == 0) {
                        dpartentId = "0";
                        mTitleTv.setText("全部部门位置信息");
                    }
                    ClearALLmarker();
                    new ThreadUtil(mActivity, GaoDeMapLoactionIndexActivity.this).start();
                    CustomDialog.showProgressDialog(GaoDeMapLoactionIndexActivity.this,
                            "正在获最新位置信息...");
                    dismiss();

                }
            });
            view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in));
            setWidth(LayoutParams.FILL_PARENT);
            setHeight(LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            update();

        }

        @Override
        public void onClick(View arg0) {

        }

    }

    @Override
    public void employeeOnclick(int arg2, int viewType, PDFOutlineElementVO vo) {
        mDepartmentUtis.employeeOnclick(arg2, viewType);
    }

    public void AddGridViewByDpartHead(List<EmployeeVO> list, final String online_state) {

        mAddByDepartmentAdapter = new AddByDepartmentAdapter(this, this,
                R.layout.item_contacts_department_tree_view, false);
        mDepartmentUtis = new DepartmentMoreUtis(this, ResultVO.getInstance(), mHandler, false, "1");

        mAddByDepartmentAdapter.mfilelist = mDepartmentUtis.getPdfOutlinesCount();
        mJpop = new JournalListPopupWindwos(GaoDeMapLoactionIndexActivity.this,
                txt_comm_head_activityNamefather, 1);
        // 抄送人
        // headAdapter = new HeadAdapter(this, false);

        // headAdapter.updateData(list);
        mAdapter.updateData(list);
        mAdapter.setBossSelectTime(dateTime);
        if (list.size() > 2)
            head_loaction.setSelection(1);
        // head_loaction.setAdapter(headAdapter);

        // int size = list.size();
        //
        // WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        // int allWidth = wm.getDefaultDisplay().getWidth();
        // int itemWidth = (allWidth - 50) / 5;
        // LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
        // allWidth * size, LinearLayout.LayoutParams.FILL_PARENT);
        // head_loaction.setLayoutParams(params);
        // head_loaction.setColumnWidth(itemWidth);
        // head_loaction.setStretchMode(GridView.NO_STRETCH);
        // head_loaction.setNumColumns(size);
        // 画覆盖物
        e_listEmployeeVOs.clear();
        for (int j = 0; j < list.size(); j++) {
            EmployeeVO e = new EmployeeVO();
            e = list.get(j);
            final MarkerOptions mo = new MarkerOptions();
            mo.position(new LatLng(Double.parseDouble(list.get(j).getT_lat()), Double
                    .parseDouble(list.get(j).getT_lon())));
            mo.draggable(false);// �����϶�
            mo.visible(true);
            mo.title(list.get(j).getFullname());
            mo.snippet("" + j);

            if (online_state.equals("2"))
            {

                bm2 = BitmapFactory.decodeResource(getResources(),
                        R.drawable.gaode_map_loc_ing_gray);
            }
            else {
                bm2 = BitmapFactory.decodeResource(getResources(), R.drawable.gaode_map_loc_ing1);

            }

            bm1 = bm2.copy(Config.ARGB_8888, true);
            try {
                canvas = new Canvas(bm1);
                GetHeadPic(e, mo);

            } catch (Exception e2) {
                canvas.drawBitmap(bm1, 0, 0, null);
            }

        }
    }

    public void AddGridViewByDpartHead1(List<EmployeeVO> list) {

        mAddByDepartmentAdapter = new AddByDepartmentAdapter(this, this,
                R.layout.item_contacts_department_tree_view, false);
        mDepartmentUtis = new DepartmentMoreUtis(this, ResultVO.getInstance(), mHandler, false, "1");

        mAddByDepartmentAdapter.mfilelist = mDepartmentUtis.getPdfOutlinesCount();
        mJpop = new JournalListPopupWindwos(GaoDeMapLoactionIndexActivity.this,
                txt_comm_head_activityNamefather, 1);
        // 抄送人
        // headAdapter = new HeadAdapter(this, false);

        // 添加员工的姓名和头像
        mAdapter.updateData(list);
        mAdapter.setBossSelectTime(dateTime);
        if (list.size() > 2)
            head_loaction.setSelection(1);

        // 画覆盖物
        e_listEmployeeVOs.clear();
        for (int j = 0; j < list.size(); j++) {
            EmployeeVO e = new EmployeeVO();
            e = list.get(j);
            final MarkerOptions mo = new MarkerOptions();
            mo.position(new LatLng(Double.parseDouble(list.get(j).getT_lat()), Double
                    .parseDouble(list.get(j).getT_lon())));
            mo.draggable(false);// �����϶�
            mo.visible(true);
            mo.title(list.get(j).getFullname());
            mo.snippet("" + j);

            if (e.getT_status().equals("0"))
            {
                bm2 = BitmapFactory.decodeResource(getResources(),
                        R.drawable.gaode_map_loc_ing_gray);

            }
            else {
                bm2 = BitmapFactory.decodeResource(getResources(), R.drawable.gaode_map_loc_ing1);
            }
            bm1 = bm2.copy(Config.ARGB_8888, true);
            try {
                canvas = new Canvas(bm1);
                GetHeadPic(e, mo);
            } catch (Exception e2) {
                canvas.drawBitmap(bm2, 0, 0, null);
            }

        }
    }

    public void ClearALLmarker() {
        if (null != aMap)
            aMap.clear();
    }

    /**
     * 转换图片成圆形
     * 
     * @param bitmap 传入Bitmap对象
     * @return
     */
    public Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }
        Bitmap output = Bitmap.createBitmap(width,
                height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }

    public void GetHeadPic(EmployeeVO e, MarkerOptions mo) {

        ImageAsyncTask imageAsyncTask = new ImageAsyncTask(e, mo);
        imageAsyncTask.execute(e.getHeadpic());

    }

    class ImageAsyncTask extends AsyncTask<String, Integer, Bitmap> {
        private MarkerOptions mo;
        private EmployeeVO e;

        public ImageAsyncTask(EmployeeVO employeeVO, MarkerOptions markerOptions) {
            this.mo = markerOptions;
            this.e = employeeVO;
        }

        @Override
        protected Bitmap doInBackground(String... arg0) {
            int size = 68 * CommonUtils.getScreenWidth(GaoDeMapLoactionIndexActivity.this) / 1080;
            ImageSize i = new ImageSize(size, size);
            return ImageLoader.getInstance().loadImageSync(arg0[0], i,
                    CommonUtils.initImageLoaderOptions1());
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            int size = 68 * CommonUtils.getScreenWidth(GaoDeMapLoactionIndexActivity.this) / 1080;
            ImageSize i = new ImageSize(size, size);
            Bitmap newBitmap;
            if (bitmap == null) {
                Bitmap bmp = BitmapFactory.decodeResource(getResources(),
                        R.drawable.ic_default_portrait_s);
                newBitmap = CommonUtils.zoomImg(bmp, size, size);
            } else {
                newBitmap = toRoundBitmap(bitmap);
            }

            canvas.drawBitmap(newBitmap, (bm2.getWidth() - newBitmap.getWidth()) / 2,
                    (bm2.getWidth() - newBitmap.getWidth()) / 2, null);

            mo.icon(BitmapDescriptorFactory.fromBitmap(bm1));
            markers = aMap.addMarker(mo);
            e.setE_markers(markers);
            if (null == markers.getObject()) {
                markers.setObject(e);
            }
            e_listEmployeeVOs.add(e);
        }

    }

    /*
     * 第三次改动 员工列表
     */
    public class PersonList extends BaseAdapter {
        private List<EmployeeVO> allLists;

        public PersonList() {
            allLists = new ArrayList<EmployeeVO>();
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return allLists.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return allLists.get(arg0);
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
                convertView = View.inflate(mactivity, R.layout.lv_journal, null);
                holder.mName = (TextView) convertView.findViewById(R.id.name_tv);
                holder.mTime = (TextView) convertView.findViewById(R.id.time_tv);
                holder.mDName = (TextView) convertView.findViewById(R.id.content_tv);
                holder.mHead = (BSCircleImageView) convertView.findViewById(R.id.journal_head_icon);
                holder.mIsRead = (TextView) convertView.findViewById(R.id.state_tv);
                holder.mCTitle = (TextView) convertView.findViewById(R.id.c_title);
                holder.mCName = (TextView) convertView.findViewById(R.id.c_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ImageLoader.getInstance().displayImage(allLists.get(position).getHeadpic(),
                    holder.mHead, mOptions);
            holder.mName.setText(allLists.get(position).getFullname());
            holder.mDName.setText("定位时间:"
                    + DateUtils.parseMDHM(allLists.get(position).getT_addtime()));
            holder.mDName.setTextSize(14);
            if (allLists.get(position).getT_status().equals("1")) {
                holder.mName.setTextColor(getResources().getColor(R.color.C4));
                holder.mCName.setTextColor(getResources().getColor(R.color.C4));
                holder.mTime.setTextColor(getResources().getColor(R.color.C4));
                holder.mDName.setTextColor(getResources().getColor(R.color.red));
                holder.mName.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources()
                        .getDrawable(R.drawable.crm_highsas_clients_far), null);

            }
            else if (allLists.get(position).getT_status().equals("0")) {
                holder.mName.setTextColor(getResources().getColor(R.color.C5));
                holder.mTime.setTextColor(getResources().getColor(R.color.C5));
                holder.mCName.setTextColor(getResources().getColor(R.color.C5));
                holder.mDName.setTextColor(getResources().getColor(R.color.C5));
                holder.mName.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources()
                        .getDrawable(R.drawable.crm_lianxiren_detail_maphui), null);

            }
            else {
                holder.mTime.setTextColor(getResources().getColor(R.color.C6));
                holder.mName.setTextColor(getResources().getColor(R.color.C6));
                holder.mCName.setTextColor(getResources().getColor(R.color.C6));
                holder.mDName.setTextColor(getResources().getColor(R.color.C5));
                holder.mDName.setText("无定位信息");
                holder.mName.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

            }

            holder.mTime.setText(allLists.get(position).getPname());
            holder.mTime.setTextSize(14);
            holder.mCTitle.setVisibility(View.GONE);
            holder.mCName.setText(allLists.get(position).getDname());
            holder.mCName.setTextSize(14);
            holder.mCName.setBackgroundResource(0);// 去掉背景

            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    Intent i = new Intent(GaoDeMapLoactionIndexActivity.this,
                            GaoDeMapPersonTrajectoryActivity.class);
                    i.putExtra("sid", allLists.get(position).getUserid());
                    i.putExtra("username", allLists.get(position).getFullname());
                    i.putExtra("date", dateTime);
                    startActivity(i);
                }
            });

            return convertView;
        }
    }

    static class ViewHolder {
        private TextView mName, mDName, mTime, mIsRead, mCTitle, mCName;
        private BSCircleImageView mHead;
    }

}
