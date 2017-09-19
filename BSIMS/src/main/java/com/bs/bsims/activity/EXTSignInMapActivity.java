
package com.bs.bsims.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.bs.bsims.R;
import com.bs.bsims.adapter.UploadAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.database.DBHelper;
import com.bs.bsims.utils.BsFileMathUtils;
import com.bs.bsims.utils.BsPermissionUtils;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.view.BSProgressDialog;
import com.bs.bsims.view.BSUPloadPopWindows;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author peck
 * @Description: 移动打卡带地图页面 UI settings一些选项设置响应事件
 * @date 2015-5-13 下午3:18:51 2015/5/13 16:51
 * @email 971371860@qq.com
 * @version V1.0
 */
public class EXTSignInMapActivity extends Activity implements OnCheckedChangeListener, LocationSource, AMapLocationListener {
    private static final String TAG = "EXTSignInMapActivity";
    private AMap aMap;
    private MapView mapView;
    private UiSettings mUiSettings;
    private OnLocationChangedListener mListener;

    @ViewInject(R.id.btn_signin_map_submit)
    private Button btnSignInSubmit;

    private final int image_capture = 500;
    private final int image_local = 501;

    private Context appContext;
    private Context mContext;
    private SharedPreferences mSettings;

    private TextView txt_localDes;
    private TextView txt_localTime;

    private EditText mRemark;

    private DBHelper dbHelper;
    /**
     * 签到历史记录
     */

    // 声明AMapLocationClient类对象
    public static AMapLocationClient mLocationClient = null;

    // 声明mLocationOption对象
    public static AMapLocationClientOption mLocationOption = null;

    /*
     * 上传图片
     */
    private BSUPloadPopWindows mPop;
    private GridView mGrideviewUpload;
    private UploadAdapter mAdapter;
    private LinearLayout mParentView;
    private List<String> mPicturePathList;
    private BSProgressDialog mDialog;
    private static final int TAKE_PICTURE = 0x000001;
    private static final int RESULT_LOAD_IMAGE = 0x000002;

    private String mPhotoWillDestoryPlath = "";
    private boolean mCommit = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_fbs_signin_map);
        mContext = this;
        appContext = getApplicationContext();
        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        mapView = (MapView) findViewById(R.id.map_signin_map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        findViewById(R.id.map_signin_frm).getLayoutParams().height = CommonUtils.getScreenHigh(mContext) / 3;
        mRemark = (EditText) findViewById(R.id.business_work_details);
        // 上传图片使用
        mGrideviewUpload = (GridView) findViewById(R.id.grideview_upload);
        mAdapter = new UploadAdapter(this, "1");
        mGrideviewUpload.setAdapter(mAdapter);
        mParentView = (LinearLayout) getLayoutInflater().inflate(R.layout.creative_idea_new, null);
        mPicturePathList = new ArrayList<String>();
        mDialog = new BSProgressDialog(this);
        txt_localDes = (TextView) findViewById(R.id.txt_signin_map_local_des);
        txt_localTime = (TextView) findViewById(R.id.txt_signin_map_local_time);
        init();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            mUiSettings = aMap.getUiSettings();
        }
        // Button buttonScale = (Button) findViewById(R.id.buttonScale);
        // buttonScale.setOnClickListener(this);
        // CheckBox scaleToggle = (CheckBox) findViewById(R.id.scale_toggle);
        // scaleToggle.setOnClickListener(this);
        // CheckBox zoomToggle = (CheckBox) findViewById(R.id.zoom_toggle);
        // zoomToggle.setOnClickListener(this);
        // CheckBox compassToggle = (CheckBox)
        // findViewById(R.id.compass_toggle);
        // compassToggle.setOnClickListener(this);
        // CheckBox mylocationToggle = (CheckBox)
        // findViewById(R.id.mylocation_toggle);
        // mylocationToggle.setOnClickListener(this);
        // CheckBox scrollToggle = (CheckBox) findViewById(R.id.scroll_toggle);
        // scrollToggle.setOnClickListener(this);
        // CheckBox zoom_gesturesToggle = (CheckBox)
        // findViewById(R.id.zoom_gestures_toggle);
        // zoom_gesturesToggle.setOnClickListener(this);
        // RadioGroup radioGroup = (RadioGroup)
        // findViewById(R.id.logo_position);
        // radioGroup.setOnCheckedChangeListener(this);

        locationAtNow();

        dbHelper = new DBHelper(this);

        findViewById(R.id.img_head_back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        findViewById(R.id.sign_photo).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                if (mAdapter.mList.size() < 1) {
                    mPop = new BSUPloadPopWindows(EXTSignInMapActivity.this, mParentView, null, null, 0);
                    mPop.dismiss();
                    try {
                        // sdcard可以使用
                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            // 保证有3MB的空间取存储图片
                            if (BsFileMathUtils.GetSDCardSize() >= 3) {
                                Log.e("str", BsFileMathUtils.GetSDCardSize() + ">>>>>>>>>>>>>>>>>");
                                mPop.photo(EXTSignInMapActivity.this);
                            }
                            else {
                                CustomToast.showLongToast(mContext, "请清理Sdcrad内存后,拍照。");
                            }

                        } else {
                            CustomToast.showLongToast(mContext, "SDCARD无法使用,无法拍照。");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    CustomToast.showShortToast(mContext, "只能上传一张哦~");
                }
            }
        });

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
        if (mPop != null && null != mPop.getFilePath()) {
            outState.putString("filePath", mPop.getFilePath());
        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (mPop == null || TextUtils.isEmpty(mPop.getFilePath())) {
            mPhotoWillDestoryPlath = savedInstanceState.getString("filePath");
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustomDialog.closeProgressDialog();// 当内存不足 当前view被kill后一定把dialog关闭
        mapView.onDestroy();
        if (mAdapter != null && mAdapter.mList.size() > 0) {
            for (int i = 0; i < mAdapter.mList.size(); i++) {
                if (!mAdapter.mList.get(i).isRecycled()) {
                    mAdapter.mList.get(i).recycle();
                    System.gc();
                }
            }
        }
    }

    /**
     * 设置logo位置，左下，底部居中，右下
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (aMap != null) {

        }

    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null) {
            mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    /**
                     * 打卡时间是由后台的服务器时间决定的，故这里不显示时间
                     */
                    if (!TextUtils.isEmpty(amapLocation.getAddress())) {
                        // 顶部标题设置
                        TextView ttop = (TextView) findViewById(R.id.txt_comm_head_activityName);
                        ttop.setText(getResources().getString(R.string.punch_the_clock_title));
                        txt_localDes.setText(amapLocation.getAddress());
                        txt_localTime.setText(DateUtils.getCurrentTimess());
                        // 写入到sp中
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                        Editor edit = sp.edit();
                        edit.putString("lat", amapLocation.getLatitude() + "");
                        edit.putString("lon", amapLocation.getLongitude() + "");
                        edit.putString("address", amapLocation.getAddress() + "");
                        // edit.putString("desc", desc + "");
                        edit.putLong("gbLocationTime", System.currentTimeMillis());
                        edit.commit();
                        btnSignInSubmit = (Button) findViewById(R.id.btn_signin_map_submit);
                        btnSignInSubmit.setOnClickListener(submintClick);
                        btnSignInSubmit.setVisibility(View.VISIBLE);
                        btnSignInSubmit.setBackgroundResource(R.drawable.corners_blue);
                    } else {
                        txt_localDes.setHint("(定位失败,请查看是否开启网络和定位权限)");
                        // txt_localDes.setText(amapLocation.getAddress());
                        txt_localTime.setHint(DateUtils.getCurrentTimess());
                    }

                }
                else if (amapLocation.getErrorCode() == 12) {
                    BsPermissionUtils.getAddressInfo(mContext);
                }

                else {
                    txt_localDes.setHint("(定位失败,请查看是否开启网络和定位权限)");
                    // txt_localDes.setText(amapLocation.getAddress());
                    txt_localTime.setHint(DateUtils.getCurrentTimess());
                }
            } else {
                txt_localDes.setHint("(获取位置信息失败,请查看是否开启网络和定位权限)");
                // txt_localDes.setText(amapLocation.getAddress());
                txt_localTime.setHint(DateUtils.getCurrentTimess());
            }
        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (null == mLocationClient) {
            mLocationClient = new AMapLocationClient(mContext.getApplicationContext());
        }
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
        // 给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        // 启动定位
        mLocationClient.startLocation();
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (null != mLocationClient) {
            mLocationClient.stopLocation();// 停止定位
        }
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
        /**
         * 设置地图是否可以手势缩放大小
         */
        mUiSettings.setZoomGesturesEnabled(!flag);
        mUiSettings.setZoomControlsEnabled(flag);
        /**
         * 设置显示地图的默认比例尺
         */
        // mUiSettings.setScaleControlsEnabled(!flag);

        // 方法设置地图的缩放级别
        aMap.moveCamera(CameraUpdateFactory.zoomTo(18));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (mAdapter.mList.size() < 1) {
                    if (resultCode == RESULT_OK) {

                        if (null != data) {
                            if (data.hasExtra("data")) {
                                mGrideviewUpload.setVisibility(View.VISIBLE);
                                try {
                                    Bitmap thumbnail = data.getParcelableExtra("data");
                                    mAdapter.mList.add(thumbnail);
                                    mAdapter.notifyDataSetChanged();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    CustomToast.showShortToast(mContext, "获取图片错误");
                                }

                            }
                        } else {
                            mGrideviewUpload.setVisibility(View.VISIBLE);
                            try {
                                Bitmap bitmap = null;
                                if (mPop != null && !TextUtils.isEmpty(mPop.getFilePath())) {
                                    File file = new File(mPop.getFilePath());
                                    mPicturePathList.add(mPop.getFilePath());
                                    mAdapter.mPicList.add(mPop.getFilePath());
                                    bitmap = ImageLoader.getInstance().loadImageSync("file://" + mPop.getFilePath());
                                    // bitmap= BitmapFactory.decodeFile(,
                                    // CommonUtils.getBitmapOption(4));
                                }
                                else {
                                    File file = new File(mPhotoWillDestoryPlath);
                                    mPicturePathList.add(mPhotoWillDestoryPlath);
                                    mAdapter.mPicList.add(mPhotoWillDestoryPlath);
                                    bitmap = ImageLoader.getInstance().loadImageSync("file://" + mPhotoWillDestoryPlath);
                                }
                                mAdapter.mList.add(bitmap);
                                mAdapter.notifyDataSetChanged();

                            } catch (Exception e) {
                                e.printStackTrace();
                                CustomToast.showShortToast(mContext, "拍照失败,请确认是否开启拍照访问权限或存储卡不存在或内存已满！");
                            }

                        }

                    }
                }
                break;

        }

    }

    private OnClickListener submintClick = new OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btn_signin_map_submit: {

                    if (null == mAdapter || mAdapter.mPicList.size() == 0) {
                        CustomToast.showShortToast(mContext, "请上传照片");
                        return;
                    }

                    if (mCommit) {
                        mCommit = false;
                        submintSignIn();
                    }

                }
                    break;

                default:
                    break;
            }
        }
    };

    /**
     * 签到，并且上传图片
     */
    private void submintSignIn() {
        // TODO Auto-generated method stub
        CustomDialog.showProgressDialog(mContext, "正在提交签到数据...");

        String url = BSApplication.getInstance().getHttpTitle() + Constant.SignIn.SIGN_IN;

        RequestParams paramsMap = new RequestParams();

        // com.loopj.android.http.RequestParams
        // com.lidroid.xutils.http.RequestParams

        try {
            paramsMap.put("ftoken", BSApplication.getInstance().getmCompany());

            /**
             * 这里获取当前用户名是拿现有的代码 从数据库获取
             */
            // String[] usernames = dbHelper.queryAllUserName();
            // if (usernames.length > 0) {
            // String tempName = usernames[usernames.length - 1];
            // if (!TextUtils.isEmpty(tempName)) {
            // String tempPwd = dbHelper.queryPasswordByName(tempName);
            // } else {
            // tempName = Constant.USERIDTEST + "";
            // }
            // paramsMap.addBodyParameter("userid", tempName);
            // }

            paramsMap.put("userid", BSApplication.getInstance().getUserId());
            paramsMap.put("lat", mSettings.getString("lat", ""));
            paramsMap.put("lng", mSettings.getString("lon", ""));
            paramsMap.put("address", mSettings.getString("address", ""));
            paramsMap.put("mobilename", android.os.Build.MODEL);// 手机类型
            paramsMap.put("wantype", CommonUtils.GetNetworkType(mContext));// 网络状态
            paramsMap.put("remark", mRemark.getText().toString().trim() + "");// 备注

            for (int i = 0; i < mAdapter.mPicList.size(); i++) {
                File file = com.bs.bsims.utils.CommonUtils.bitmapToString(this, mAdapter.mPicList.get(i));
                paramsMap.put("pictures" + i, file);
            }

        } catch (Exception e1) {
            CustomToast.showShortToast(appContext, "打卡失败");
            CustomDialog.closeProgressDialog();
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, paramsMap, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                // TODO Auto-generated method stub
                CustomDialog.closeProgressDialog();
                CustomToast.showShortToast(mContext, "打卡失败,网络错误!");
                mCommit = true;
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                // TODO Auto-generated method stub.
                mCommit = true;
                CustomDialog.closeProgressDialog();
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(new String(arg2));
                    String code = (String) jsonObject.get("code");
                    String str = (String) jsonObject.get("retinfo");
                    if (Constant.RESULT_CODE.equals(code)) {
                        CustomToast.showShortToast(mContext, str);
                        startActivity(new Intent(mContext, SignInActivity.class));
                        finish();
                    } else {
                        CustomToast.showShortToast(mContext, str);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });

    }

}
