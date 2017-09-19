
package com.bs.bsims.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.bs.bsims.R;
import com.bs.bsims.adapter.UploadAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
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
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 签到提交页面
 */

public class CrmVisitorSignAddInfoActivity extends BaseActivity implements OnClickListener, OnItemClickListener, AMapLocationListener {

    private TextView sign_time;// 时间
    private TextView visitor_address_change;// 位置
    private LinearLayout r3;// 父布局
    private Context mContext;

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
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.obj != null) {
                sign_time.setText(msg.obj + "");
            }

        };
    };

    private boolean falge = true;
    private String mPhotoWillDestoryPlath = "";

    /*
     * 参数提交
     */

    private String php_Lng;
    private String php_Lat;
    private String php_Address;
    public boolean postState = true;// 控制多次提交按钮

    // 声明AMapLocationClient类对象
    public static AMapLocationClient mLocationClient = null;

    // 声明mLocationOption对象
    public static AMapLocationClientOption mLocationOption = null;

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.r3:
                Intent loactionAcIntent = new Intent();
                loactionAcIntent.setClass(mContext, CrmVisitorFromGaodeMap.class);
                startActivityForResult(loactionAcIntent, 1015);
                break;

            case R.id.txt_comm_head_right:
                if (null != php_Address && !php_Address.equals("") && postState && php_Lng != null && php_Lat != null) {

                    // if(mAdapter.mPicList.size()>0){
                    CustomDialog.showProgressDialog(mContext, "签到中...");
                    postCustomerInfo();
                    // }
                    // else{
                    // CustomToast.showShortToast(mContext, "请上传照片");
                    // }

                } else {
                    CustomToast.showShortToast(mContext, "请选择签到位置");
                }

            default:
                break;
        }
    }

    @Override
    public void baseSetContentView() {
        // TODO Auto-generated method stub
        View layout = View.inflate(this, R.layout.crm_vistor_signaddinfo, mContentLayout);
        mContext = this;
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
        r3 = (LinearLayout) findViewById(R.id.r3);
        sign_time = (TextView) findViewById(R.id.sign_time);
        visitor_address_change = (TextView) findViewById(R.id.visitor_address_change);
        // 上传图片使用
        mGrideviewUpload = (GridView) findViewById(R.id.grideview_upload);
        mAdapter = new UploadAdapter(this);
        mGrideviewUpload.setAdapter(mAdapter);
        mParentView = (LinearLayout) getLayoutInflater().inflate(R.layout.creative_idea_new, null);
        mPicturePathList = new ArrayList<String>();
        mDialog = new BSProgressDialog(this);
        sign_time.setText(DateUtils.getCurrentTimess());
        mTitleTv.setText("位置签到");
        activate();

        // MyThread m = new MyThread();
        // new Thread(m).start();
    }

    @Override
    public void bindViewsListener() {
        // TODO Auto-generated method stub
        mGrideviewUpload.setOnItemClickListener(this);
        r3.setOnClickListener(this);
        mOkTv.setOnClickListener(this);
    }

    class MyThread implements Runnable {
        public void run() {
            while (falge) {

                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.obj = DateUtils.getCurrentTimess();
                    handler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        falge = false;
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
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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

    public void postCustomerInfo() {
        postState = false;
        final String url = BSApplication.getInstance().getHttpTitle() + Constant.CRM_VISITOR_SIGNSUBMIT;

        RequestParams map = new RequestParams();
        try {
            map.put("ftoken", BSApplication.getInstance().getmCompany());
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put("lng", php_Lng);
            map.put("lat", php_Lat);
            map.put("address", php_Address);
            map.put("addtime", sign_time.getText().toString().trim());
            map.put("mobilename", android.os.Build.MODEL);
            map.put("wantype", CommonUtils.GetNetworkType(mContext));
            map.put("locationtype", "4");
            for (int i = 0; i < mAdapter.mPicList.size(); i++) {
                File file = CommonUtils.bitmapToString(this, mAdapter.mPicList.get(i));
                map.put("pictures" + i, file);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, map, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                // TODO Auto-generated method stub
                postState = true;
                CustomDialog.closeProgressDialog();
                CustomToast.showShortToast(mContext, "签到失败,网络错误!");
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                // TODO Auto-generated method stub.
                CustomDialog.closeProgressDialog();
                JSONObject jsonObject;
                try {
                    postState = true;
                    jsonObject = new JSONObject(new String(arg2));
                    String code = (String) jsonObject.get("code");
                    String str = (String) jsonObject.get("retinfo");
                    if (Constant.RESULT_CODE.equals(code)) {
                        CustomToast.showShortToast(mContext, str);
                        Intent i = new Intent();
                        i.putExtra("flage", true);
                        setResult(2016, i);
                        CrmVisitorSignAddInfoActivity.this.finish();
                    } else {
                        CustomToast.showShortToast(mContext, str);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        if (arg2 == mAdapter.mList.size()) {
            mPop = new BSUPloadPopWindows(this, mParentView, null, null, 0);
            mPop.dismiss();
            try {
                // sdcard可以使用
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    // 保证有3MB的空间取存储图片
                    if (BsFileMathUtils.GetSDCardSize() >= 3) {
                        Log.e("str", BsFileMathUtils.GetSDCardSize() + ">>>>>>>>>>>>>>>>>");
                        mPop.photo(CrmVisitorSignAddInfoActivity.this);
                    }

                } else {
                    CustomToast.showLongToast(mContext, "SDCARD无法使用,无法拍照。");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent data) {
        // TODO Auto-generated method stub

        switch (arg0) {
            case TAKE_PICTURE:
                if (mAdapter.mList.size() < 3) {
                    if (arg1 == RESULT_OK) {
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
                                Bitmap bitmap;
                                if (mPop != null && !TextUtils.isEmpty(mPop.getFilePath())) {
                                    File file = new File(mPop.getFilePath());
                                    mPicturePathList.add(mPop.getFilePath());
                                    mAdapter.mPicList.add(mPop.getFilePath());
                                    // bitmap = BitmapFactory.decodeFile(mPop.getFilePath(),
                                    // CommonUtils.getBitmapOption(4));
                                    bitmap = ImageLoader.getInstance().loadImageSync("file://" + mPop.getFilePath());
                                    ImageLoader.getInstance().loadImage("file://" + mPop.getFilePath(), new ImageLoadingListener() {

                                        @Override
                                        public void onLoadingStarted(String arg0, View arg1) {

                                        }

                                        @Override
                                        public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {

                                        }

                                        @Override
                                        public void onLoadingComplete(String arg0, View arg1, Bitmap bitmap) {
                                            mAdapter.mList.add(bitmap);
                                            mAdapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onLoadingCancelled(String arg0, View arg1) {

                                        }
                                    });
                                }
                                else {
                                    File file = new File(mPhotoWillDestoryPlath);
                                    mPicturePathList.add(mPhotoWillDestoryPlath);
                                    mAdapter.mPicList.add(mPhotoWillDestoryPlath);
                                    bitmap = ImageLoader.getInstance().loadImageSync("file://" +
                                            mPhotoWillDestoryPlath);
                                    ImageLoader.getInstance().loadImage("file://" + mPhotoWillDestoryPlath, new ImageLoadingListener() {

                                        @Override
                                        public void onLoadingStarted(String arg0, View arg1) {

                                        }

                                        @Override
                                        public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {

                                        }

                                        @Override
                                        public void onLoadingComplete(String arg0, View arg1, Bitmap bitmap) {
                                            mAdapter.mList.add(bitmap);
                                            mAdapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onLoadingCancelled(String arg0, View arg1) {

                                        }
                                    });
                                    // bitmap = BitmapFactory.decodeFile(,
                                    // CommonUtils.getBitmapOption(4));
                                }
                                // mAdapter.mList.add(CommonUtils.getBitmapFromFile(file,CAMERA_REQUESTz
                                // 70, 70));//getImageFromLocal如果还是闪退可以试用这个方法
                                // // 将图
                                // mAdapter.mList.add(bitmap);

                            } catch (Exception e) {
                                e.printStackTrace();
                                CustomToast.showShortToast(mContext, "拍照失败,请确认是否开启拍照访问权限或存储卡不存在或内存已满！");
                            }

                        }

                    }
                }
                break;

            case 1015:
                if (arg1 == 1015) {
                    if (data != null) {
                        visitor_address_change.setTextColor(getResources().getColor(R.color.C4));
                        visitor_address_change.setText(data.getStringExtra("adress"));
                        php_Lng = data.getStringExtra("Lng");
                        php_Lat = data.getStringExtra("Lat");
                        php_Address = data.getStringExtra("adress");
                        mOkTv.setText("完成");
                    }
                }
                break;
        }
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        // TODO Auto-generated method stub
        if (amapLocation != null && amapLocation.getErrorCode() == 0) {
            visitor_address_change.setText(amapLocation.getAddress());
            php_Address = amapLocation.getAddress();
            php_Lng = amapLocation.getLongitude() + "";
            php_Lat = amapLocation.getLatitude() + "";
            mOkTv.setText("完成");
        }
        else if (amapLocation.getErrorCode() == 12) {
            BsPermissionUtils.getAddressInfo(mContext);
        }
        else {
            visitor_address_change.setText("(定位失败) 请选择位置");
        }
        // AnimationUtil.setStopRotateAnimation(mAdressInco);// 停止旋转动画
        // mAdressInco.setVisibility(View.GONE);
    }

    public void activate() {
        // TODO Auto-generated method stub
        // AnimationUtil.setStartRotateAnimation(mContext, mAdressInco);
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
        if(mLocationOption.isOnceLocationLatest()){
            mLocationOption.setOnceLocationLatest(true);
         //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。
         //如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会。
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

    public void deactivate() {
        if (null != mLocationClient) {
            mLocationClient.stopLocation();// 停止定位
        }
    }

    /*
     * (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onPause()
     */
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        deactivate();
    }

}
