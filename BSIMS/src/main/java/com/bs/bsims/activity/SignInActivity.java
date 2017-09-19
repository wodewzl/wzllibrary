
package com.bs.bsims.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.SignInAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.database.DBHelper;
import com.bs.bsims.model.SignInFather;
import com.bs.bsims.model.SignInVO;
import com.bs.bsims.ui.alert.YusAlertDialog;
import com.bs.bsims.utils.BsPermissionUtils;
import com.bs.bsims.utils.CommFileUtils;
import com.bs.bsims.utils.CommonImageUtils;
import com.bs.bsims.utils.CustomLog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.LocationUtils;
import com.bs.bsims.view.BSLoadingView;
import com.bs.bsims.view.BSRefreshListView;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author peck
 * @Description: 移动打卡页面 从讯科中拿过来的
 * @date 2015-5-13 下午3:18:51
 * @email 971371860@qq.com
 * @version V1.0
 */
public class SignInActivity extends Activity {

    private static final String TAG = "SignInActivity";

    private WindowManager windowManager;
    private View tipsView;

    private BSRefreshListView listView;
    private SignInAdapter adapter;
    private List<SignInVO> dataList = new ArrayList<SignInVO>();
    private SignInFather singfather = new SignInFather();
    @ViewInject(R.id.txt_sign_in_list_count)
    private TextView txt_signInCount;
    private int signInCount; // 签到
    private LinearLayout romvelistcard;
    @ViewInject(R.id.txt_sign_in_list_year)
    private TextView txt_year;
    private TextView txt_comm_head_right;
    @ViewInject(R.id.txt_sign_in_list_month)
    private TextView txt_month;

    @ViewInject(R.id.bg_sgin_in_list_head_line)
    private ImageView bg_sgin_in_list_head_line;

    private boolean isLocation = false;
    private View mFootLayout;
    private TextView mMoreTextView, loadingfile1;
    private ProgressBar mProgressBar;
    private String mRefresh = "";
    // 0为首次,1为上拉刷新 ，2为下拉刷新
    private int mState = 0;
    /** 上拉ID */
    private String firstid;

    /** 下拉ID */
    private String lastid;
    /* 下拉控制多次点击 */
    private boolean lastPressS = true;
    /** 记录最后一条的id */
    private String saveLastId;
    /**
     * @author peck
     */
    private Context mContext;
    private SharedPreferences mSettings;

    private DBHelper dbHelper;

    private boolean fBoolean = true;

    private BSLoadingView mBsLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        /**
         * @author peck
         * @Description: 从讯科中拿过来的 原来代码是继承其自有的basicActivity，这里没有继承，所以需要自己写
         * @date 2015-5-13 下午3:18:51
         * @email 971371860@qq.com
         * @version V1.0
         */
        setContentLayout(savedInstanceState);
        initHeadView();
        initView();
        bodymethod();
        bindRefreshListener();
    }

    protected void setContentLayout(Bundle savedInstanceState) {
        setContentView(R.layout.ac_sign_in);
        x.view().inject(this);
        mContext = this;
        dbHelper = new DBHelper(this);
        romvelistcard = (LinearLayout) findViewById(R.id.romvelistcard);
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        tipsView = View.inflate(mContext, R.layout.comm_dialog_tips, null);
    	try {
			// 透明状态栏 5.0以后才可以显示
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// 透明导航栏
			// getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    }

    protected void initHeadView() {
        findViewById(R.id.img_head_back).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        // TODO Auto-generated method stub
                        finish();
                    }
                });
        ((TextView) findViewById(R.id.txt_comm_head_activityName))
                .setText("移动打卡");

        /*
         * ((TextView) findViewById(R.id.txt_comm_head_right)).setText("测试");
         * findViewById(R.id.txt_comm_head_right).setOnClickListener(new OnClickListener() {
         * @Override public void onClick(View v) { //Intent cameraIntent =
         * IntentFactory.getInstance().getSignInCameraIntent(mContext, true);
         * //startActivityForResult(cameraIntent, 1); //openActivity(new Intent(mContext,
         * SignInMapActivity.class)); openActivity(new Intent(mContext,
         * DialogSelectDepartmentActivity.class)); } });
         */

    }

    @SuppressLint("NewApi")
    protected void initView() {
        listView = (BSRefreshListView) findViewById(R.id.listview_sing_in);
        listView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        txt_comm_head_right = (TextView) findViewById(R.id.txt_comm_head_right);
        txt_comm_head_right.setText("打卡");
        if ("1".equals(BSApplication.getInstance().getUserFromServerVO().getIsmobile())) {
            txt_comm_head_right.setVisibility(View.VISIBLE);
        } else {
            txt_comm_head_right.setVisibility(View.GONE);
        }

        mBsLoadingView = (BSLoadingView) findViewById(R.id.loading_animation);
        mBsLoadingView.setText("正在加载···");
        // // 是否有发布权限
        // List<MenuVO> listVo = BSApplication.getInstance().getUserFromServerVO().getMenu();
        // if (CommonUtils.isLimits(listVo.get(0).getMenu(), Constant.LIMITS_OFFICE001)) {
        // txt_comm_head_right.setText("打卡");
        // txt_comm_head_right.setVisibility(View.VISIBLE);
        // } else {
        // txt_comm_head_right.setVisibility(View.GONE);
        // }

        View head = View.inflate(this, R.layout.item_sign_in_listview_head1,
                null);
        listView.addHeaderView(head);
        loadingfile1 = (TextView) findViewById(R.id.loadingfile1);
        adapter = new SignInAdapter(this, dataList);
        listView.setAdapter(adapter);

        View foot = View.inflate(mContext, R.layout.listview_bottom_more, null);
        foot.findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
        foot.findViewById(R.id.txt_loading).setVisibility(View.INVISIBLE);
        foot.setBackgroundColor(Color.TRANSPARENT);
        foot.setFocusable(false);
        foot.setClickable(false);
        // listView.addFooterView(foot);

        txt_signInCount = (TextView) findViewById(R.id.txt_sign_in_list_count);

        txt_comm_head_right.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                // isLocation = true;
                // startLocation();
                if(!BsPermissionUtils.isOPenGps(mContext)){
                    BsPermissionUtils.openGPS(mContext);
                }
                else{
                    startActivity(new Intent(mContext, EXTSignInMapActivity.class));
                }
             
            }
        });
        initFoot();
        LayoutParams layoutParamsRealy = findViewById(R.id.rl_item_sign_month).getLayoutParams();
        LayoutParams layoutParamsRealys = findViewById(R.id.sign_work_state).getLayoutParams();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.FILL_PARENT);
        layoutParams.setMargins(layoutParamsRealy.width / 2 - layoutParamsRealys.width / 8, 0, 0,
                15);
        bg_sgin_in_list_head_line.setLayoutParams(layoutParams);
    }

    public void bindRefreshListener() {
        // listView.setonRefreshListener(new OnRefreshListener() {
        //
        // @Override
        // public void onRefresh() {
        // mState = 1;
        // mRefresh = Constant.FIRSTID;
        // if (adapter.dataList.size() == 0)
        // mRefresh = "";
        // RequestParams paramsMap = new RequestParams();
        // firstid = dataList.get(0).getAttendanceid();
        // paramsMap.put("firstid", firstid);
        // getDataFromServerBy(paramsMap);
        // // new ThreadUtil(SignInActivity.this, SignInActivity.this).start();
        // }
        // });
        mFootLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (lastPressS) {
                    lastPressS = false;
                    mMoreTextView.setText("正在加载...");
                    mProgressBar.setVisibility(View.VISIBLE);
                    mState = 2;
                    mRefresh = Constant.LASTID;
                    RequestParams paramsMap = new RequestParams();
                    lastid = dataList.get(dataList.size() - 1).getAttendanceid();
                    paramsMap.put("lastid", lastid);
                    getDataFromServerBy(paramsMap);
                    // new ThreadUtil(mActivity, SharedfilesdHomeMyUploadFragment.this).start();
                }
            }
        });

    }

    protected void bodymethod() {
        IntentFilter filter = new IntentFilter(
                LocationUtils.LOCATION_RECEIVE_ACTION);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // getDataFromServer();
        RequestParams paramsMap = new RequestParams();
        getDataFromServerBy(paramsMap);
        fBoolean = true;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (!isLocation)
                return;

            String address = intent.getStringExtra("address");
            // CustomToast.showShortToast(context, address);
            windowManager.removeView(tipsView);
            YusAlertDialog.Builder builder = new YusAlertDialog.Builder(context);
            builder.setTitle("签到");
            builder.setMessage("您当前的位置是：" + address);
            builder.setNegativeButton("取消", null);
            builder.setPositiveButton("签到",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            builder.create().show();
            isLocation = false;
        }
    };

    private void createTip() {
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        int widthPixels = outMetrics.widthPixels;

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        // params.gravity = Gravity.TOP | Gravity.LEFT; //屏幕左上角
        params.gravity = Gravity.CENTER | Gravity.CENTER; // 屏幕中间
        // params.width = LayoutParams.WRAP_CONTENT;
        // params.height = LayoutParams.WRAP_CONTENT;
        params.width = widthPixels / 3;
        params.height = widthPixels / 3;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE; // 不可获取焦点
        params.format = PixelFormat.TRANSPARENT;
        params.x = 0;
        params.y = 0;
        windowManager.addView(tipsView, params);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                if (isLocation) {
                    windowManager.removeView(tipsView);
                    isLocation = false;
                } else {
                    finish();
                }

                return true;

            default:
                break;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fBoolean = true;
        if (resultCode == Activity.RESULT_OK) {
            String sdStatus = Environment.getExternalStorageState();
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                Log.i("TestFile",
                        "SD card is not avaiable/writeable right now.");
                return;
            }

            String path = CommFileUtils.getExternalCacheDir(mContext)
                    + File.separator + "signin.jpg";
            Bitmap bitmap = CommonImageUtils.getSmallBitmap(path);

            // 将图片显示在ImageView里
            ((ImageView) findViewById(R.id.imageView)).setImageBitmap(bitmap);
        }

    }

    private void getDataFromServerBy(RequestParams paramsMap) {
        // TODO Auto-generated method stub

        String url = BSApplication.getInstance().getHttpTitle() + Constant.SignIn.SIGN_IN_LIST;
        // RequestParams paramsMap = new RequestParams();

        try {
            paramsMap.put("ftoken", BSApplication.getInstance()
                    .getmCompany());
            paramsMap.put("userid", BSApplication.getInstance()
                    .getUserId());
            // paramsMap.put("firstid", "");
            // paramsMap.put("lastid", "");

        } catch (Exception e1) {
            mBsLoadingView.setVisibility(View.GONE);
            loadingfile1.setVisibility(View.VISIBLE);
            loadingfile1.setText("网络问题,获取打卡记录失败~");
            e1.printStackTrace();
        }

        CustomLog.e(TAG, url);
        CustomLog.e(TAG, paramsMap.toString());

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, paramsMap, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                    Throwable arg3) {
                // CustomToast.showShortToast(mContext, "");
                lastPressS = true;
                mBsLoadingView.setVisibility(View.GONE);
                romvelistcard.setVisibility(View.GONE);
                loadingfile1.setVisibility(View.VISIBLE);
                loadingfile1.setText("网络问题,获取打卡记录失败~");
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                lastPressS = true;
                mBsLoadingView.setVisibility(View.GONE);
                loadingfile1.setVisibility(View.GONE);
                romvelistcard.setVisibility(View.VISIBLE);
                JSONObject jsonObject;
                String jsonStr = new String(arg2);
                CustomLog.e(TAG, jsonStr);
                try {
                    jsonObject = new JSONObject(jsonStr);
                    String str = (String) jsonObject.get("retinfo");

                    // new Handler().postDelayed(new Runnable() {
                    // @Override
                    // public void run() {
                    // finish();
                    // }
                    // }, 700);

                    int resultCode = 0;
                    try {
                        resultCode = Integer.parseInt((String) jsonObject.get("code"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        CustomToast.showShortToast(mContext, "网络数据错误");
                    }

                    switch (resultCode) {
                        case 200:
                            String arrayStr = null;
                            Gson gson = new Gson();
                            singfather = gson.fromJson(jsonStr, SignInFather.class);
                            List<SignInVO> parseArray = singfather.getArray();
                            if (fBoolean) {
                                txt_year.setText(parseArray.get(0).getStime().substring(0, 4));
                                txt_month.setText(parseArray.get(0).getStime().substring(5) + "月");
                                txt_signInCount.setText(parseArray.get(0).getStime() + " 打卡记录");
                                fBoolean = false;
                            }
                            /** 下拉刷新 上拉 **/
                            saveLastId = parseArray.get(parseArray.size() - 1).getAttendanceid();
                            // mEXTSharedfilesdHomeMyUpdateAdapter.updateData(mSharedfilesdHomeMyUploadVO.getArray());
                            // mListView.onRefreshComplete();
                            // // footViewIsVisibility(mAdapter.mList);
                            if (1 == mState) {
                                adapter.updateDataFrist(parseArray);
                            } else if (2 == mState) {
                                adapter.updateDataLast(parseArray);
                            } else {
                                adapter.updateData(parseArray);
                            }
                            mState = 0;
                            listView.setVisibility(View.VISIBLE);
                            listView.onRefreshComplete();
                            if (mState != 1)
                                footViewIsVisibility(singfather.getArray());
                            /** 下拉刷新 上拉 **/
                            // dataList.clear();
                            // dataList.addAll(parseArray);
                            adapter.notifyDataSetChanged();
                            // listView.setVisibility(View.VISIBLE);
                            break;

                        case 400:
                            romvelistcard.setVisibility(View.GONE);
                            loadingfile1.setVisibility(View.VISIBLE);
                            loadingfile1.setText("暂时还没有打卡数据哦~");
                            break;
                        default:
                            // CustomToast.showShortToast(appContext, "签到失败");
                            break;
                    }

                    // CustomToast.showShortToast(mContext, "签到成功");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });

    }

    // 加载更多数据
    public void initFoot() {
        mFootLayout = LayoutInflater.from(SignInActivity.this).inflate(
                R.layout.listview_bottom_more, null);
        mMoreTextView = (TextView) mFootLayout.findViewById(R.id.txt_loading);
        mMoreTextView.setText("更多");
        mProgressBar = (ProgressBar) mFootLayout.findViewById(R.id.progressBar);
        mFootLayout.setVisibility(View.GONE);
        listView.addFooterView(mFootLayout);
    }

    protected void footViewIsVisibility(List<SignInVO> datas) {
        if (singfather == null) {
            return;
        }
        if (singfather.getCount() == null) {
            return;
        }
        if (Integer.parseInt(singfather.getCount()) <= 15) {
            mFootLayout.setVisibility(View.GONE);
            // listView.removeFooterView(mFootLayout);
        } else {
            mFootLayout.setVisibility(View.VISIBLE);
            mMoreTextView.setText("更多");
            mProgressBar.setVisibility(View.GONE);
        }
    }

}
