
package com.bs.bsims.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.CrmClientTrendAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.CrmClientDetailVO;
import com.bs.bsims.model.CrmClientTrendVO;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSDialog;
import com.bs.bsims.view.BSRefreshListView;
import com.bs.bsims.view.BSRefreshListView.OnRefreshListener;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CrmHighseasClientsHomeActivity extends BaseActivity implements OnClickListener {
    private BSRefreshListView mRefreshListView;
    private CrmClientTrendAdapter mCrmListAdapter;
    private CrmClientTrendVO mClientTrendVO;

    private String mCid;

    private View mFootLayout;
    private TextView mMoreTextView;
    private ProgressBar mProgressBar;
    private int mState = 0; // 0为首次,1为上拉刷新 ，2为下拉刷新
    private String mFristid, mLastid;

    private ImageView detailInfo, mQiang;
    private TextView mback;
    private TextView mName, mAddress, mLevel, mFullName;
    private String name, address, oldfullname, level;
    public static final String DETAIL_EDIT = "detail_edit";// 公海客户详情编辑

    private boolean mJPush = false;// 极光推送识别
    private CrmClientDetailVO mClientDetailVO;
    private BSDialog mDialog;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.crm_highseas_home_view, mContentLayout);
        name = getIntent().getStringExtra("name");
        address = getIntent().getStringExtra("address");
        oldfullname = getIntent().getStringExtra("oldfullname");
        level = getIntent().getStringExtra("level");

    }

    @Override
    public boolean getDataResult() {
        // TODO Auto-generated method stub
        return getData();
    }

    @Override
    public void updateUi() {

        if (mJPush) {
            mJPush = false;
            mClientDetailVO = mClientDetailVO.getInfo();
            name = mClientDetailVO.getName();
            address = mClientDetailVO.getAddress();
            oldfullname = mClientDetailVO.getFullname();
            level = mClientDetailVO.getLevel();
        }

        mName.setText(name);
        mAddress.setText(address);
        if ("A级客户".equals(level)) {
            mLevel.setText("A级客户");
            mLevel.setBackgroundResource(R.drawable.frame_shixing_yellow);
        } else if ("B级客户".equals(level)) {
            mLevel.setText("B级客户");
            mLevel.setBackgroundResource(R.drawable.frame_shixing_blue);
        } else if ("C级客户".equals(level)) {
            mLevel.setText("C级客户");
            mLevel.setBackgroundResource(R.drawable.frame_shixing_green);
        } else {
            mLevel.setText("D级客户");
            mLevel.setBackgroundResource(R.drawable.frame_shixing_blue_light);
        }

    }

    @Override
    public void initView() {
        mHeadLayout.setVisibility(View.GONE);
        mRefreshListView = (BSRefreshListView) findViewById(R.id.lv_refresh);
        mCrmListAdapter = new CrmClientTrendAdapter(this);
        mRefreshListView.setAdapter(mCrmListAdapter);
        mName = (TextView) findViewById(R.id.name);
        mAddress = (TextView) findViewById(R.id.address);
        mLevel = (TextView) findViewById(R.id.clevel);
        mFullName = (TextView) findViewById(R.id.fullname);
        mback = (TextView) findViewById(R.id.mback);
        detailInfo = (ImageView) findViewById(R.id.detail_info);
        mQiang = (ImageView) findViewById(R.id.qiang);
        initData();
        initFoot();
        registBroadcast();
    }

    public void initData() {
        Intent intent = this.getIntent();
        mCid = intent.getStringExtra("cid");
        mFullName.setText("上次负责人：" + intent.getStringExtra("fullname"));
        if (intent.hasExtra("jpush")) {
            mJPush = intent.getBooleanExtra("jpush", false);
        }
        this.findViewById(R.id.qiang).setVisibility(View.VISIBLE);

        // if (intent.hasExtra("crmEdit")) {
        // String crmEdit = intent.getStringExtra("crmEdit");
        // if ("1".equals(crmEdit)) {
        // this.findViewById(R.id.qiang).setVisibility(View.VISIBLE);
        // } else {
        // this.findViewById(R.id.qiang).setVisibility(View.GONE);
        // }
        // }

    }

    // 加载更多数据
    public void initFoot() {
        mFootLayout = LayoutInflater.from(this).inflate(R.layout.listview_bottom_more, null);
        mMoreTextView = (TextView) mFootLayout.findViewById(R.id.txt_loading);
        mMoreTextView.setText("更多");
        mProgressBar = (ProgressBar) mFootLayout.findViewById(R.id.progressBar);
        mFootLayout.setVisibility(View.GONE);
        mRefreshListView.addFooterView(mFootLayout);
    }

    public boolean getData() {
        try {
            Gson gson = new Gson();

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put("cid", mCid);
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.CRM_CLIENT_DETAIL, map);
            if (mJPush) {
                mClientDetailVO = gson.fromJson(jsonStr, CrmClientDetailVO.class);
            }

            HashMap<String, String> map1 = new HashMap<String, String>();
            map1.put("userid", BSApplication.getInstance().getUserId());

            if (0 == mState) {
                mFristid = "";
                mLastid = "";
            }
            map1.put("firstid", mFristid);
            map1.put("lastid", mLastid);
            map1.put("cid", mCid);

            map1.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.CRM_CLIENT_TREND_LIST, map1);
            mClientTrendVO = gson.fromJson(jsonStrList, CrmClientTrendVO.class);
            if (Constant.RESULT_CODE.equals(mClientTrendVO.getCode())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void executeSuccess() {
        super.executeSuccess();
        if (1 == mState) {
            mCrmListAdapter.updateDataFrist(mClientTrendVO.getArray());
        } else if (2 == mState) {
            mCrmListAdapter.updateDataLast(mClientTrendVO.getArray());
        } else {
            mCrmListAdapter.updateData(mClientTrendVO.getArray());
        }

        mCrmListAdapter.notifyDataSetChanged();
        mRefreshListView.onRefreshComplete();
        if ("".equals(mFristid) || mState != 1) {
            footViewIsVisibility();
        }
        mState = 0;
    }

    @Override
    public void executeFailure() {

        // 列表展示的时候不能调用父类
        super.isRequestFinish();
        mCrmListAdapter.notifyDataSetChanged();
        mRefreshListView.onRefreshComplete();
        footViewIsVisibility();

        // 不适合只隐藏列表，适合隐藏整个布局;
        if (mClientTrendVO == null) {
            super.showNoNetView();
        } else {
            if (mState == 0) {
                mCrmListAdapter.updateData(new ArrayList<CrmClientTrendVO>());
                mFootLayout.setVisibility(View.GONE);
            }
        }
        mState = 0;
    }

    public void footViewIsVisibility() {
        if (mClientTrendVO == null || mClientTrendVO.getCount() == null) {
            return;
        }
        if (Integer.parseInt(mClientTrendVO.getCount()) <= 15) {
            mFootLayout.setVisibility(View.GONE);
        } else {
            mFootLayout.setVisibility(View.VISIBLE);
            mMoreTextView.setText("更多");
            mProgressBar.setVisibility(View.GONE);
        }

    }

    @Override
    public void bindViewsListener() {
        mback.setOnClickListener(this);
        detailInfo.setOnClickListener(this);
        mQiang.setOnClickListener(this);
        mRefreshListView.setonRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mCrmListAdapter.mList.size() > 0) {
                    match(1, mCrmListAdapter.mList.get(0).getTlid());
                } else {
                    mFristid = "";
                    match(1, "");
                }
            }
        });
        mFootLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mMoreTextView.setText("正在加载...");
                mProgressBar.setVisibility(View.VISIBLE);
                match(2, mCrmListAdapter.mList.get(mCrmListAdapter.mList.size() - 1).getTlid());
            }
        });

    }

    public void match(int key, String value) {

        switch (key) {
            case 1:
                mFristid = value;
                mLastid = "";
                mState = 1;
                break;
            case 2:
                mLastid = value;
                mFristid = "";
                mState = 2;
                break;
            default:
                break;
        }
        mRefreshListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
        new ThreadUtil(this, this).start();
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.mback:
                CrmHighseasClientsHomeActivity.this.finish();
                break;
            case R.id.detail_info:
                Intent in = new Intent(CrmHighseasClientsHomeActivity.this, CrmClientDetailActivity.class);
                in.putExtra("cid", mCid);
                in.putExtra("ispub", "1");
                startActivity(in);
                break;
            case R.id.qiang:
                // sumbitData();
                showDialog();
                break;

            default:
                break;
        }

    }

    public void showDialog() {
        View view = View.inflate(this, R.layout.dialog_lv_item, null);
        TextView tv = (TextView) view.findViewById(R.id.textview);
        tv.setText("确定要抢该客户？");
        mDialog = new BSDialog(this, "友情提醒", view, new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                sumbitData();
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    // 将公海客户转换为我的客户
    public void sumbitData() {
        CustomDialog.showProgressDialog(this, "正在提交数据...");
        RequestParams params = new RequestParams();

        try {
            params.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            params.put("userid", BSApplication.getInstance().getUserId());
            params.put("cid", mCid);

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        String url = BSApplication.getInstance().getHttpTitle() + Constant.CRM_HIGHSEAS_GET_CLIENT;
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                System.out.println(new String(arg2));

                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(new String(arg2));
                    String str = (String) jsonObject.get("retinfo");
                    String code = (String) jsonObject.get("code");
                    if (Constant.RESULT_CODE.equals(code)) {
                        setResult(1, new Intent());
                        CrmHighseasClientsHomeActivity.this.finish();
                    } else {
                        CustomToast.showShortToast(CrmHighseasClientsHomeActivity.this, str);
                    }

                    CustomDialog.closeProgressDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void registBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(DETAIL_EDIT);
        this.registerReceiver(msgBroadcast, filter);
    }

    private void unRegistExitReceiver() {
        this.unregisterReceiver(msgBroadcast);
    }

    private BroadcastReceiver msgBroadcast = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (DETAIL_EDIT.equals(intent.getAction())) {
                if (intent.getStringExtra("address") != null) {
                    address = intent.getStringExtra("address");
                }
                if (intent.getStringExtra("name") != null) {
                    name = intent.getStringExtra("name");
                }

                if (intent.getStringExtra("level") != null) {
                    level = intent.getStringExtra("level");
                }
                updateUi();
            }
        }
    };

    protected void onDestroy() {
        super.onDestroy();
        unRegistExitReceiver();
    };

}
