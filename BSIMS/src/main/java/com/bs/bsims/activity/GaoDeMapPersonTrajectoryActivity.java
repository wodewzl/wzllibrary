
package com.bs.bsims.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.amap.api.maps2d.model.PolylineOptions;
import com.bs.bsims.R;
import com.bs.bsims.adapter.GaodeLocationDetailsApdater;
import com.bs.bsims.calendarmanager.ui.datedialog.BSCalendarPopupWindowUtils;
import com.bs.bsims.calendarmanager.ui.datedialog.BSCalendarPopupWindowUtils.KcalendarCallback;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.utils.UrlUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GaoDeMapPersonTrajectoryActivity extends BaseActivity implements
        OnMarkerClickListener, OnInfoWindowClickListener, InfoWindowAdapter, OnMapClickListener,
        OnClickListener {
    private MapView mapView;
    private AMap aMap;
    private Bitmap bm;
    Bitmap bm1;
    Bitmap bm2;
    Bitmap bm3;
    Canvas canvas = null;
    private Marker markers = null;// ��ǰ�켣��ͼ��

    private Marker currentMarker = null;// ��¼��ǰ��������ĸ�������

    private TextView txt_comm_head_right, moreTrack;

    private Intent intent;

    private String datatime = "";

    private EmployeeVO employeeVO;

    private Context context;
    private String dateTimeYearAndMonth;

    private TextView gaode_location_online, gaode_location_onlineno, location_adressinfo;
    private GaodeLocationDetailsApdater details;
    private GridView send_second_person_gv1;
    private HorizontalScrollView scrollView;
    private ImageView showArrow;
    private LinearLayout moreTrackLayout;
    private boolean blean = true;

    private BSCalendarPopupWindowUtils pop;

    private List<EmployeeVO> e_listEmployeeVOs = new ArrayList<EmployeeVO>();

    private String getState = "0";// 拿哪个接口的数据

    // 日程集合
    private List<String> mDatelist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapView.onCreate(savedInstanceState);
        context = this;
    }

    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();

        }
    }

    private void moveToForbiddenCity() {
        if (employeeVO.getPoints().size() > 0) {
            CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(
                    new LatLng(Double.parseDouble(employeeVO.getPoints()
                            .get(0).getT_lat()), Double.parseDouble(employeeVO.getPoints().get(0)
                            .getT_lon())), 14.0f);
            aMap.moveCamera(cu);
        }
    }

    private void addPolyline(List<EmployeeVO> list) {
        try {
            PolylineOptions options = new PolylineOptions().color(Color.parseColor("#0099ff"))
                    .width(5);
            for (EmployeeVO info : list) {
                options.add(new LatLng(Double.parseDouble(info.getT_lat()), Double.parseDouble(info
                        .getT_lon())));
            }
            if (options != null)
                aMap.addPolyline(options);
        } catch (Exception e) {
        }

    }

    private void addmarker() {
        int i = 0;
        int len = employeeVO.getPoints().size();
        e_listEmployeeVOs.clear();
        for (EmployeeVO info : employeeVO.getPoints()) {
            MarkerOptions mo = new MarkerOptions();
            mo.position(new LatLng(Double.parseDouble(info.getT_lat()), Double.parseDouble(info
                    .getT_lon())));
            mo.draggable(false);// �����϶�
            mo.visible(true);
            mo.title(info.getName());
            // bm1 = bm.copy(Config.ARGB_8888, true);
            // canvas = new Canvas(bm1);
            // Paint paint = new Paint();
            // paint.setColor(Color.WHITE);
            // paint.setTextSize(35);

            if (i == 0) {
                // canvas.drawText("��", 13, 42, paint);
                mo.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(
                        getResources(), R.drawable.gaode_location_start1)));
                mo.title(info.getT_address());
                mo.snippet(info.getT_addtime());
                mo.anchor(0.5f, 0.95f);
            } else if (i == (len - 1)) {
                // canvas.drawText("��", 13, 42, paint);
                // mo.icon(BitmapDescriptorFactory.fromBitmap(bm1));

                mo.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(
                        getResources(), R.drawable.gaode_location_end1)));
                mo.title(info.getT_address());
                mo.snippet(info.getT_addtime());
                mo.anchor(0.5f, 0.95f);

            } else {
                // canvas.drawText("" + i, 18, 42, paint);
                // mo.icon(BitmapDescriptorFactory.fromBitmap(bm1));
                mo.title(info.getT_address());
                mo.snippet(info.getT_addtime());
                bm3 = bm2.copy(Config.ARGB_8888, true);
                mo.icon(BitmapDescriptorFactory.fromBitmap(bm3));
                mo.anchor(0.5f, 0.75f);
            }

            markers = aMap.addMarker(mo);
            info.setE_markers(markers);
            e_listEmployeeVOs.add(info);
            i++;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        // moveToForbiddenCity();
        // addmarker();
        // addmakerPtion();
        // addPolyline(viewInfos);
        // DrawbleMarkerPition(viewInfos);

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
        return false;

    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        // TODO Auto-generated method stub
        View infoWindow = getLayoutInflater().inflate(R.layout.gaode_location_showindown_infos,
                null);
        // render(marker, infoWindow);
        // location_adressinfo = (TextView) infoWindow.findViewById(R.id.location_adressinfo);
        // location_timeinfo = (TextView) infoWindow.findViewById(R.id.location_timeinfo);
        gaode_location_onlineno = (TextView) infoWindow.findViewById(R.id.location_adressinfo);//
        location_adressinfo = (TextView) infoWindow.findViewById(R.id.location_timeinfo);//

        gaode_location_onlineno.setText("地址:" + " " + marker.getTitle());
        location_adressinfo.setText("时间:" + " "
                + DateUtils.parseDateDayAndHour(marker.getSnippet()));
        return infoWindow;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onMapClick(LatLng arg0) {
        // TODO Auto-generated method stub
        if (currentMarker != null) {
            currentMarker.hideInfoWindow();

            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    dm.widthPixels, 120);
            send_second_person_gv1.setLayoutParams(params);
            // moreTrack.setVisibility(View.VISIBLE);
            // moreTrack.setText("更多");
            send_second_person_gv1.setVisibility(View.GONE);
            showArrow.setImageResource(R.drawable.calendar_close);
        }

    }

    public void addmakerPtion(List<EmployeeVO> list) {

        for (int i = 0; i < list.size(); i++) {

            MarkerOptions mo = new MarkerOptions();
            mo.position(new LatLng(Double.parseDouble(list.get(i).getT_lat()), Double
                    .parseDouble(list.get(i)
                            .getT_lon())));
            mo.draggable(false);// �����϶�
            mo.visible(true);
            mo.title(list.get(i).getT_address());
            mo.snippet(list.get(i).getT_addtime());
            mo.anchor(0.5f, 0.5f);
            // mo.setFlat(true);
            // mo.icon(BitmapDescriptorFactory.defaultMarker());

            // canvas = new Canvas(bm1);
            // Paint paint = new Paint();
            // paint.setColor(Color.WHITE);
            // paint.setTextSize(35);

            if (i == 0) {
                mo.visible(false);
            } else if (i == (list.size() - 1)) {
                mo.visible(false);
            } else {
                bm3 = bm2.copy(Config.ARGB_8888, true);
            }
            mo.icon(BitmapDescriptorFactory.fromBitmap(bm3));
            markers = aMap.addMarker(mo);
        }
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, float degrees) {
        if (degrees == 0 || null == bitmap) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                matrix, true);
        if (null != bitmap) {
            bitmap.recycle();
        }
        return bmp;
    }

    // ���㷽λ��pab��
    //
    private double gps2d(double lat_a, double lng_a, double lat_b, double lng_b) {
        // double d = 0;
        //
        // lat_a = lat_a * Math.PI / 180;
        //
        // lng_a = lng_a * Math.PI / 180;
        //
        // lat_b = lat_b * Math.PI / 180;
        //
        // lng_b = lng_b * Math.PI / 180;
        //
        // d = Math.sin(lat_a) * Math.sin(lat_b) + Math.cos(lat_a) *
        // Math.cos(lat_b) * Math.cos(lng_b - lng_a);
        //
        // d = Math.sqrt(1 - d * d);
        //
        // d = Math.cos(lat_b) * Math.sin(lng_b - lng_a) / d;
        //
        // d = Math.asin(d) * 180 / Math.PI;
        // //
        //
        // return d;

        // function getAngle(px1, py1, px2, py2) {
        // �����x��yֵ
        double x, y, hypotenuse, cos, radian, angle;
        x = lat_b - lat_a;
        y = lng_b - lng_a;
        hypotenuse = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        // б�߳���
        cos = x / hypotenuse;
        radian = Math.acos(cos);
        // �������
        angle = 180 / (Math.PI / radian);
        // �û�������Ƕ�
        if (y < 0) {
            angle = -angle;
        } else if ((y == 0) && (x < 0)) {
            angle = 180;
        }
        return angle;
        // }

    }

    public void DrawbleMarkerPition(List<EmployeeVO> list) {
        int dregss = 0;
        MarkerOptions mo = new MarkerOptions();
        // 画覆盖物
        for (int i = 0; i < list.size(); i++) {
            if (list.size() - 1 == i) {
                return;
            }

            // if (i == list.size() - 2) {
            // dregss = (int) gps2d(list.get(i).getLat(), list.get(i).getLng(),
            // list.get(i + 1).getLat(), list.get(i + 1).getLng());
            // if (dregss < 0) {
            // dregss = dregss - 90;
            // }
            //
            // } else {
            // dregss = (int) gps2d(list.get(i).getLat(), list.get(i).getLng(),
            // list.get(i + 1).getLat(), list.get(i + 1).getLng());
            // }

            dregss = (int) gps2d(Double.parseDouble(list.get(i).getT_lat()),
                    Double.parseDouble(list.get(i).getT_lon()), Double.parseDouble(list.get(i + 1)
                            .getT_lat()), Double.parseDouble(list.get(i + 1).getT_lon()));
            // if (i == list.size() - 1) {
            // dregss = (int) gps2d(list.get(list.size() - 2).getLat(),
            // list.get(list.size() - 2).getLng(), list.get(list.size() -
            // 1).getLat(), list.get(list.size() - 1).getLng());
            // mo.position(new LatLng((list.get(i - 2).getLat() + list.get(i -
            // 1).getLat()) / 2, (list.get(i - 2).getLng() + list.get(i -
            // 1).getLng()) / 2));
            // Log.e("msgLat", list.get(list.size() - 1).getLat() + "");
            // Log.e("msgLong", list.get(list.size() - 1).getLng() + "");
            // Log.e("msg", dregss + "");
            // Log.e("msgi", i + "");
            // } else {
            // �õ���ת�Ƕ�

            Log.e("msg1", dregss + "");

            mo.position(new LatLng((Double.parseDouble(list.get(i).getT_lat()) + Double
                    .parseDouble(list.get(i + 1).getT_lat())) / 2, (Double.parseDouble(list.get(i)
                    .getT_lon()) + Double.parseDouble(list.get(i + 1).getT_lon())) / 2));
            // }
            // �����ת�ǶȻ�ͼ
            Bitmap bitmap = rotateBitmap(
                    BitmapFactory.decodeResource(getResources(), R.drawable.gaode_direction_point),
                    dregss);

            mo.draggable(false);// �����϶�
            mo.visible(true);
            mo.title(list.get(i).getName());
            mo.anchor(0.5f, 0.5f);
            // mo.setFlat(true);
            // mo.icon(BitmapDescriptorFactory.defaultMarker());
            bm3 = bitmap.copy(Config.ARGB_8888, true);
            mo.icon(BitmapDescriptorFactory.fromBitmap(bm3));
            // canvas = new Canvas(bm1);
            // Paint paint = new Paint();
            // paint.setColor(Color.WHITE);
            // paint.setTextSize(35);

            // if (i == 0) {
            // canvas.drawText("��", 13, 42, paint);
            // mo.icon(BitmapDescriptorFactory.fromBitmap(bm1));
            // } else if (i == (len - 1)) {
            // canvas.drawText("��", 13, 42, paint);
            // mo.icon(BitmapDescriptorFactory.fromBitmap(bm1));
            //
            // } else {
            // canvas.drawText("" + i, 18, 42, paint);
            // mo.icon(BitmapDescriptorFactory.fromBitmap(bm1));
            // }
            markers = aMap.addMarker(mo);

        }

    }

    @Override
    public void baseSetContentView() {
        // TODO Auto-generated method stub
        View layout = View.inflate(this, R.layout.gaode_location_activiry, mContentLayout);
    }

    @Override
    public boolean getDataResult() {
        // TODO Auto-generated method stub
        return getData();
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
        map.put("uid", intent.getStringExtra("sid"));
        String urlStr = UrlUtil.getUrlByMap1(Constant.GAODE_LOACION_INDEX_CLEADENRDATERED, map);
        String jsonUrlStr;
        try {
            jsonUrlStr = HttpClientUtil.get(urlStr, Constant.ENCODING).trim();
            employeeVO = gson.fromJson(jsonUrlStr, EmployeeVO.class);
            if (employeeVO.getCode().equals("400")) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            employeeVO = null;
        }

        return false;
    }

    /*
     * http://cp.beisheng.wang/api.php/Trajectory/getUserTrajectory
     * /ftoken/RBDTZXGUMNDKkEwMkZFN0UyMTA1RgO0O0OO0O0O/sid/22/datetime/2015-09-12/userid/25
     */
    private boolean getDataTableHostOne() {
        // TODO Auto-generated method stub
        Gson gson = new Gson();
        Map map = new HashMap<String, String>();
        map.put("sid", intent.getStringExtra("sid"));
        map.put("datetime", datatime);
        String urlStr = UrlUtil.getUrlByMap1(Constant.GAODE_LOACION_INDEX_GETUSERTRA, map);
        String jsonUrlStr;
        try {
            List<EmployeeVO> lists = new ArrayList<EmployeeVO>();
            jsonUrlStr = HttpClientUtil.get(urlStr, Constant.ENCODING).trim();
            employeeVO = gson.fromJson(jsonUrlStr, EmployeeVO.class);
            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void updateUi() {
        // TODO Auto-generated method stub
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub

        mapView = (MapView) findViewById(R.id.map);
        bm2 = BitmapFactory.decodeResource(getResources(), R.drawable.cilrcle_has_blue20);
        bm1 = BitmapFactory.decodeResource(getResources(), R.drawable.gaode_pition_dot);
        txt_comm_head_right = (TextView) findViewById(R.id.txt_comm_head_right);
        moreTrack = (TextView) findViewById(R.id.more_track);
        // scrollView = (HorizontalScrollView) findViewById(R.id.scroll_view);
        showArrow = (ImageView) findViewById(R.id.show_arrow);
        moreTrackLayout = (LinearLayout) findViewById(R.id.more_track_layout);
        init();

        if (markers != null) {
            markers.destroy();
        }
        txt_comm_head_right.setVisibility(View.GONE);
        txt_comm_head_right.setCompoundDrawablesWithIntrinsicBounds(
                getResources().getDrawable(R.drawable.cq_timepick), null, null, null);
        // CameraUpdateFactory.zoomTo(5);

        intent = getIntent();

        if (null != intent.getStringExtra("date")) {
            datatime = intent.getStringExtra("date");
        }
        else {
            datatime = DateUtils.getCurrentDate();
        }

        mTitleTv.setText(intent.getStringExtra("username") + "的轨迹路线图");
        send_second_person_gv1 = (GridView) findViewById(R.id.send_second_person_gv1);
        aMap.getUiSettings().setZoomControlsEnabled(false);
    }

    /*
     * (non-Javadoc)
     * @see BaseActivity#bindViewsListener()
     */
    @Override
    public void bindViewsListener() {
        // TODO Auto-generated method stub
        aMap.setOnMarkerClickListener(this);
        aMap.setInfoWindowAdapter(this);
        aMap.setOnInfoWindowClickListener(this);
        aMap.setOnMapClickListener(this);
        txt_comm_head_right.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.txt_comm_head_right:

                if (pop != null) {
                    if (pop.isShowing()) {
                        return;
                    }
                }
                pop = new BSCalendarPopupWindowUtils(GaoDeMapPersonTrajectoryActivity.this,
                        mHeadLayout, new KcalendarCallback() {
                            @Override
                            public void kcalendarViewClick(String date) {

                                if (date.length() < 8) {
                                    getState = "1";
                                    dateTimeYearAndMonth = date;
                                }
                                // 点击日期
                                else {
                                    getState = "0";
                                    datatime = date;
                                    CustomToast.showShortToast(getApplicationContext(), "选择的时间"
                                            + date);
                                    CustomDialog.showProgressDialog(context, "正在获取最新轨迹路线...");
                                }
                                new ThreadUtil(context, GaoDeMapPersonTrajectoryActivity.this)
                                        .start();

                            }
                        }, true, null, mDatelist, datatime);
                break;

            default:
                break;
        }
    }

    @Override
    public void executeFailure() {
        // TODO Auto-generated method stub
        txt_comm_head_right.setVisibility(View.VISIBLE);
        mDatelist = new ArrayList<String>();
        CustomDialog.closeProgressDialog();
        mLoading.setVisibility(View.VISIBLE);
        mContentLayout.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.VISIBLE);
        CommonUtils.setNonetIcon(context, mLoading, this);
    }

    @Override
    public void executeSuccess() {
        // TODO Auto-generated method stub
        txt_comm_head_right.setVisibility(View.VISIBLE);
        CustomDialog.closeProgressDialog();
        // 如果是获取日历红点
        if (getState.equals("1")) {
            if (null != employeeVO && null != employeeVO.getDateTime()) {
                // 标记日历红点
                String[] str = employeeVO.getDateTime().split(",");
                mDatelist  = new ArrayList<String>();
                mDatelist.clear();
                for (int i = 0; i < str.length; i++) {
                    mDatelist.add(str[i]);
                }
                pop.UpdateMaker(mDatelist);
            }
            return;
        }
        if (null == employeeVO.getPoints() || null == employeeVO) {

            if (null == mDatelist) {
                mDatelist = new ArrayList<String>();
            }
         
            mLoading.setVisibility(View.VISIBLE);
            mContentLayout.setVisibility(View.GONE);
            mLoadingLayout.setVisibility(View.VISIBLE);
            mLoading.setText("没有最新轨迹信息~");
            return;
        }
        else {
            mLoading.setVisibility(View.GONE);
            mContentLayout.setVisibility(View.VISIBLE);
            mLoadingLayout.setVisibility(View.GONE);
            ClearALLmarker();// 先清除所有的marker
            moveToForbiddenCity();// 第一个点位移动点
            addPolyline(employeeVO.getPoints());// 画线
            addmarker();// 添加覆盖物
            // addmakerPtion(employeeVO.getPoints());
            DrawbleMarkerPition(employeeVO.getPoints());
            int size = employeeVO.getPoints().size();
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            float density = dm.density;
            int allWidth = (int) ((this.getWindowManager().getDefaultDisplay().getWidth() - 30)
                    * size + 20);
            int itemWidth = (int) ((this.getWindowManager().getDefaultDisplay().getWidth()));
            // LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            // allWidth, LayoutParams.FILL_PARENT);
            final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    dm.widthPixels, dm.heightPixels / 2);
            moreTrack.setText(datatime + "    轨迹");
            moreTrack.setVisibility(View.VISIBLE);
            moreTrack.setWidth(dm.widthPixels);
            moreTrackLayout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if (blean) {
                        send_second_person_gv1.setVisibility(View.VISIBLE);
                        send_second_person_gv1.setLayoutParams(params);
                        showArrow.setImageResource(R.drawable.calendar_open);

                        blean = false;
                    } else {
                        send_second_person_gv1.setVisibility(View.GONE);
                        showArrow.setImageResource(R.drawable.calendar_close);
                        blean = true;
                    }

                    // onMarkerClick(currentMarker);
                }
            });

            // 标记日历红点
            if (employeeVO.getDateTime() != null) {
                mDatelist = new ArrayList<String>();
                String[] str = employeeVO.getDateTime().split(",");
                for (int i = 0; i < str.length; i++) {
                    mDatelist.add(str[i]);
                }
            }
            else {
                mDatelist = new ArrayList<String>();
            }

            send_second_person_gv1.setColumnWidth(itemWidth);
            // send_second_person_gv1.setHorizontalSpacing(10);
            // send_second_person_gv1.setStretchMode(GridView.NO_STRETCH);
            send_second_person_gv1.setNumColumns(1);
            details = new GaodeLocationDetailsApdater(context, employeeVO.getPoints());
            send_second_person_gv1.setAdapter(details);
            send_second_person_gv1.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    send_second_person_gv1.setLayoutParams(params);

                    Marker marker = e_listEmployeeVOs.get(arg2).getE_markers();
                    if (null != currentMarker) {
                        currentMarker.hideInfoWindow();
                    }
                    currentMarker = marker;// 标记为当前的maker
                    marker.showInfoWindow();
                    CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(
                            new LatLng(
                                    Double.parseDouble(e_listEmployeeVOs.get(arg2).getT_lat()),
                                    Double
                                            .parseDouble(e_listEmployeeVOs.get(arg2).getT_lon())),
                            13.0f);
                    aMap.moveCamera(cu);
                }
            });

        }
    }

    public void ClearALLmarker() {
        if (null != aMap)
            aMap.clear();
    }

}
