
package com.bs.bsims.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.CrmClientHomeAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.interfaces.RecordResult;
import com.bs.bsims.model.CrmBussinesListindexVo;
import com.bs.bsims.model.CrmClientDetailVO;
import com.bs.bsims.model.CrmClientHomeDetailVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CommonUtils.ResultCallback;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.SoundRecordUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSBottomWindow;
import com.bs.bsims.view.BSBottomWindow.MenuCallback;
import com.bs.bsims.view.BSDialog;
import com.bs.bsims.view.BSRefreshListView;
import com.bs.bsims.view.BSRefreshListView.OnRefreshListener;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint("NewApi")
public class CrmClientHomeActivity extends BaseActivity implements OnClickListener {

    public static final String DETAIL_EDIT = "detail_edit";// 客户详情编辑
    public static final String BUSSNESS_EDIT = "bussness_edit";
    public static final String CONTACT_EDIT = "contact_edit";// 联系人编辑之后回来刷新
    public static final String TRADES_EDIT = "trades_edit";// 合同状态改变之后回来刷新

    private TextView mTitleTv, mMneuTv;
    private TextView mBackTv;

    private ImageLoader mImageLoader;

    public int mType = 1;// mType有四个值，1为拜访记录，2为联系人，3为商机，4为合同
    private CrmClientHomeAdapter mRecordAdapter, mContactsAdapter, mBusinessAdapter, mTradeContractAdapter;
    private ArrayList<CrmClientHomeDetailVO> mRecordList, mContactsList, mBusinessList, mTradeList;
    private TextView mTabTv01, mTabTv02, mTabTv03, mTabTv04, mTabTv01Count, mTabTv02Count, mTabTv03Count, mTabTv04Count;
    private LinearLayout mLayout01, mLayout02, mLayout03, mLayout04;
    private String mFristid, mLastid, mCid;

    // 下拉上拉刷新部分
    private View mFootLayout;
    private TextView mMoreTextView;
    private ProgressBar mProgressBar;
    private int mState = 0; // 0为首次,1为上拉刷新 ，2为下拉刷新

    private CrmClientHomeDetailVO mCrmClientHomeDetailVO;
    private BSRefreshListView mRefreshListView;
    private String mUrl;
    private List<TextView> mTextViewList = new ArrayList<TextView>();
    private TextView mClientCompanyName, mClientCompanyAddress, mClientCompanyLevel, mClientSource;
    private boolean mFlag = true;

    private View mNoContentView;
    private String mCrmEdit = "0";

    private int mCurrentRequestType = 1;
    private boolean mCommitFlag = true;// 提交识别
    private BSDialog mDialog;
    private boolean mJPush = true;// 之前为级别推送识别，现在为详情上部数据识别
    private CrmClientDetailVO mClientDetailVO;
    private LinearLayout mTitleLayout;
    private TextView mPublicClient;

    private String[] mArrayOne = {
            "变更负责人", "添加跟进人", "放弃客户", "客户动向"
    };

    // private String[] mArrayTwo = {
    // "变更负责人", "添加跟进人"
    // };
    private String[] mArrayTwo = {
            "客户动向"
    };
    private String[] mArrayThree = {
            "变更负责人", "添加跟进人", "抢客户", "客户动向"
    };

    private String[] mArray;
    private LinearLayout mTitleDetailLayout;
    private SoundRecordUtil mRecordUtil;

    private BSBottomWindow mBottomWindow;

    private boolean mIsFirst = true;

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.crm_client_home, mContentLayout);
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    public boolean getData() {
        try {
            mFlag = false;
            Gson gson = new Gson();
            if (mJPush) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("userid", BSApplication.getInstance().getUserId());
                map.put("cid", mCid);
                map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
                String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.CRM_CLIENT_DETAIL, map);
                mClientDetailVO = gson.fromJson(jsonStr, CrmClientDetailVO.class);
            }

            HashMap<String, String> mapList = new HashMap<String, String>();
            mapList.put("userid", BSApplication.getInstance().getUserId());

            if (0 == mState) {
                mFristid = "";
                mLastid = "";
            }
            mapList.put("firstid", mFristid);
            mapList.put("lastid", mLastid);
            mapList.put("cid", mCid);

            mapList.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + mUrl, mapList);
            mCrmClientHomeDetailVO = gson.fromJson(jsonStrList, CrmClientHomeDetailVO.class);

            if (Constant.RESULT_CODE.equals(mCrmClientHomeDetailVO.getCode())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            mFlag = true;
        }
    }

    @Override
    public void updateUi() {
        // 提交放弃返回结果回调函数
        mCommitFlag = true;
        this.setResult(1, new Intent());
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void initView() {
        mHeadLayout.setVisibility(View.GONE);
        mImageLoader = ImageLoader.getInstance();
        mRefreshListView = (BSRefreshListView) findViewById(R.id.lv_refresh);
        mRecordAdapter = new CrmClientHomeAdapter(this);
        mContactsAdapter = new CrmClientHomeAdapter(this);
        mBusinessAdapter = new CrmClientHomeAdapter(this);
        mTradeContractAdapter = new CrmClientHomeAdapter(this);
        baseHeadLayout.setBackgroundColor(getResources().getColor(R.color.transparent));
        mRefreshListView.setAdapter(mRecordAdapter);

        mOkTv = (TextView) findViewById(R.id.txt_comm_head_right);
        mTitleTv = (TextView) findViewById(R.id.txt_comm_head_activityName);
        mBackTv = (TextView) findViewById(R.id.head_back);

        mMneuTv = (TextView) findViewById(R.id.menu_tvv);

        mTabTv01 = (TextView) findViewById(R.id.textview_01);
        mTabTv02 = (TextView) findViewById(R.id.textview_02);
        mTabTv03 = (TextView) findViewById(R.id.textview_03);
        mTabTv04 = (TextView) findViewById(R.id.textview_04);

        mLayout01 = (LinearLayout) findViewById(R.id.layout_item_01);
        mLayout02 = (LinearLayout) findViewById(R.id.layout_item_02);
        mLayout03 = (LinearLayout) findViewById(R.id.layout_item_03);
        mLayout04 = (LinearLayout) findViewById(R.id.layout_item_04);

        mTextViewList.add(mTabTv01);
        mTextViewList.add(mTabTv02);
        mTextViewList.add(mTabTv03);
        mTextViewList.add(mTabTv04);
        mClientCompanyName = (TextView) findViewById(R.id.cilent_company_name);
        mClientCompanyAddress = (TextView) findViewById(R.id.cilent_company_address);
        mClientCompanyLevel = (TextView) findViewById(R.id.cilent_company_levle);
        mClientSource = (TextView) findViewById(R.id.client_source);
        mClientSource.setBackground(CommonUtils.setBackgroundShap(this, 10, "#3fffffff", "#3fffffff"));
        mNoContentView = findViewById(R.id.no_content_view);

        mUrl = Constant.CRM_BUSSINES_HOMEINDEXANDDETAILSOFVISITOR;
        mTabTv01Count = (TextView) findViewById(R.id.textview_01_count);
        mTabTv02Count = (TextView) findViewById(R.id.textview_02_count);
        mTabTv03Count = (TextView) findViewById(R.id.textview_03_count);
        mTabTv04Count = (TextView) findViewById(R.id.textview_04_count);
        mTitleLayout = (LinearLayout) findViewById(R.id.title_layout);
        mPublicClient = (TextView) findViewById(R.id.public_clinet);

        mTitleDetailLayout = (LinearLayout) findViewById(R.id.title_detail_layout);
        // mRecordUtil = new SoundRecordUtil(this, recordResult, 910, 660, 1);

        if (null == mBottomWindow) {
            mBottomWindow = new BSBottomWindow(this, mMenuCallback);
            mBottomWindow.init();
        }

        initFoot();
        initData();
        registBroadcast();
    }

    public void initData() {
        Intent intent = this.getIntent();

        if (intent.getStringExtra("crmEdit") != null) {
            mCrmEdit = intent.getStringExtra("crmEdit");
            if ("1".equals(mCrmEdit)) {
                mArray = mArrayOne;
            } else {
                mArray = mArrayTwo;
            }
        }
        if (intent.getStringExtra("cid") != null) {
            mCid = intent.getStringExtra("cid");
        }
        if (intent.getStringExtra("address") != null) {
            mClientCompanyAddress.setText(intent.getStringExtra("address"));
        }
        if (intent.getStringExtra("name") != null) {
            mClientCompanyName.setText(intent.getStringExtra("name"));
        }

        if (intent.getStringExtra("level") != null) {
            String level = intent.getStringExtra("level");
            mClientCompanyLevel.setText(level);
            if ("A级客户".equals(level)) {
                mClientCompanyLevel.setBackgroundResource(R.drawable.frame_shixing_yellow);
            } else if ("B级客户".equals(level)) {
                mClientCompanyLevel.setBackgroundResource(R.drawable.frame_shixing_blue);
            } else if ("C级客户".equals(level)) {
                mClientCompanyLevel.setBackgroundResource(R.drawable.frame_shixing_green);
            } else {
                mClientCompanyLevel.setBackgroundResource(R.drawable.frame_shixing_blue_light);
            }
        }

        if (intent.getStringExtra("fullname") != null) {
            mClientSource.setText("负责人：" + intent.getStringExtra("fullname"));
        }

        if (intent.getStringExtra("visit_count") != null) {
            mTabTv01Count.setText(intent.getStringExtra("visit_count"));
        }
        if (intent.getStringExtra("contacts_count") != null) {
            mTabTv02Count.setText(intent.getStringExtra("contacts_count"));
        }
        if (intent.getStringExtra("business_count") != null) {
            mTabTv03Count.setText(intent.getStringExtra("business_count"));
        }
        if (intent.getStringExtra("contract_count") != null) {
            mTabTv04Count.setText(intent.getStringExtra("contract_count"));
        }

        // 判断公海客户权限
        if (intent.hasExtra("publish_sp")) {
            mTitleLayout.setBackgroundResource(R.drawable.crm_publich_client_bg);
            mPublicClient.setVisibility(View.VISIBLE);
            mArray = mArrayThree;
            if (intent.getStringExtra("fullname") != null) {
                mClientSource.setText("上次负责人：" + intent.getStringExtra("fullname"));
            }
        }

    }

    public void bindViewsListener() {
        mLayout01.setOnClickListener(this);
        mLayout02.setOnClickListener(this);
        mLayout03.setOnClickListener(this);
        mLayout04.setOnClickListener(this);
        mBackTv.setOnClickListener(this);
        mClientCompanyAddress.setOnClickListener(this);
        mMneuTv.setOnClickListener(this);
        mTitleDetailLayout.setOnClickListener(this);
        mRefreshListView.setonRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                FirstData();
            }
        });

        mFootLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mMoreTextView.setText("正在加载...");
                mProgressBar.setVisibility(View.VISIBLE);

                switch (mType) {
                    case 1:
                        match(6, mRecordAdapter.mList.get(mRecordAdapter.mList.size() - 1).getVid());
                        break;
                    case 2:
                        match(6, mContactsAdapter.mList.get(mContactsAdapter.mList.size() - 1).getLid());
                        break;
                    case 3:
                        match(6, mBusinessAdapter.mList.get(mBusinessAdapter.mList.size() - 1).getBid());
                        break;
                    case 4:
                        match(6, mTradeContractAdapter.mList.get(mTradeContractAdapter.mList.size() - 1).getHid());
                        break;

                    default:
                        break;
                }
            }
        });

    }

    // 下拉刷新
    public void FirstData() {
        switch (mType) {
            case 1:
                if (mRecordAdapter.mList.size() > 0) {
                    match(5, mRecordAdapter.mList.get(0).getVid());
                } else {
                    mFristid = "";
                    match(5, "");
                }
                break;
            case 2:
                if (mContactsAdapter.mList.size() > 0) {
                    match(5, mContactsAdapter.mList.get(0).getLid());
                } else {
                    mFristid = "";
                    match(5, "");
                }
                break;
            case 3:
                if (mBusinessAdapter.mList.size() > 0) {
                    match(5, mBusinessAdapter.mList.get(0).getBid());
                } else {
                    mFristid = "";
                    match(5, "");
                }
                break;
            case 4:
                if (mTradeContractAdapter.mList.size() > 0) {
                    match(5, mTradeContractAdapter.mList.get(0).getHid());
                } else {
                    mFristid = "";
                    match(5, "");
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.menu_tvv:
                CommonUtils.initPopViewBg(this, mArray, mMneuTv, mCallback, CommonUtils.getScreenWidth(this) / 3);
                break;

            case R.id.title_detail_layout:
                intent.putExtra("cid", mCid);
                intent.putExtra("crmEdit", mCrmEdit);
                if (CrmClientHomeActivity.this.getIntent().hasExtra("publish_sp")) {
                    intent.putExtra("publish_sp", "1");
                }
                intent.putExtra("crmEdit", mCrmEdit);
                intent.setClass(CrmClientHomeActivity.this, CrmClientDetailActivity.class);
                CrmClientHomeActivity.this.startActivityForResult(intent, 1);
                break;

            case R.id.crm_client_notify_iv:
                intent.putExtra("cid", mCid);
                intent.setClass(this, CrmClientTrendActivity.class);
                this.startActivity(intent);
                break;
            case R.id.crm_client_giveup_iv:
                showDialog(1);
                break;
            case R.id.crm_client_contacts_iv:
                intent.putExtra("cid", mCid);
                intent.setClass(this, CrmClientDifferentHeadActivity.class);
                this.startActivity(intent);
                break;

            case R.id.head_back:
                intent.putExtra("contactsCount", mTabTv02Count.getText().toString());
                intent.putExtra("bussnessCount", mTabTv03Count.getText().toString());
                intent.putExtra("tradeCount", mTabTv04Count.getText().toString());
                intent.putExtra("visitorCount", mTabTv01Count.getText().toString());
                intent.putExtra("cid", mCid);
                this.setResult(1, intent);
                this.finish();
                break;
            case R.id.add_layout:
                if (mType == 1) {
                    intent.putExtra("cid", mCid);
                    intent.putExtra("vistorSate", "3");
                    intent.putExtra("cname", mClientCompanyName.getText().toString());
                    intent.setClass(this, CrmVisitRecordActivityAddInfo.class);
                    this.startActivityForResult(intent, 1);
                    return;
                } else if (mType == 2) {
                    intent.putExtra("cid", mCid);
                    intent.putExtra("cname", mClientCompanyName.getText().toString());
                    intent.putExtra("type", "2");// 2代表通过点击“添加联系人”跳转到“新增联系人”界面
                    intent.setClass(this, CrmClientAddContactsActivity.class);
                    this.startActivityForResult(intent, 1);
                } else if (mType == 3) {
                    intent.putExtra("cid", mCid);
                    intent.putExtra("addstate", "1");
                    intent.putExtra("cname", mClientCompanyName.getText().toString());
                    intent.setClass(this, CrmBusinessAddInfoMsgActivity.class);
                    this.startActivityForResult(intent, 1);
                    return;
                } else {
                    intent.putExtra("cid", mCid);
                    intent.putExtra("cname", mClientCompanyName.getText().toString());
                    intent.setClass(this, CrmTradeContantAddInfo.class);
                    this.startActivityForResult(intent, 1);
                }
                break;
            case R.id.layout_item_01:
                mUrl = Constant.CRM_BUSSINES_HOMEINDEXANDDETAILSOFVISITOR;
                setTabSelect(1);
                match(1, mUrl);
                break;
            case R.id.layout_item_02:
                mUrl = Constant.CRM_BUSSINES_HOMEINDEXANDDETAILSOFCONTANS;
                mFootLayout.setVisibility(View.GONE);
                setTabSelect(2);
                match(2, mUrl);
                break;
            case R.id.layout_item_03:
                mUrl = Constant.CRM_BUSSINES_GETLIST;
                setTabSelect(3);
                match(3, mUrl);
                break;
            case R.id.layout_item_04:
                mUrl = Constant.CRM_BUSSINES_HOMEINDEXANDDETAILSOFTRANTS;
                setTabSelect(4);
                match(4, mUrl);
                break;
            case R.id.cilent_company_address:
                if ("暂无".equals(mClientDetailVO.getInfo().getLat()) || "暂无".equals(mClientDetailVO.getInfo().getLon()))
                    return;
                intent.putExtra("mLat", mClientDetailVO.getInfo().getLat());
                intent.putExtra("mLon", mClientDetailVO.getInfo().getLon());
                intent.putExtra("mAddress", mClientDetailVO.getInfo().getAddress());
                intent.putExtra("cid", mCid);
                intent.putExtra("cName", mClientDetailVO.getInfo().getName());
                intent.setClass(this, CrmGaoDeMapWithShowActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }

        if (mType != 2) {
            footViewIsVisibility();
        }
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent intent) {
        super.onActivityResult(arg0, arg1, intent);
        if (mRecordUtil == null)
            mRecordUtil = new SoundRecordUtil(CrmClientHomeActivity.this, recordResult, 910, 660, 1);
        if (intent != null) {
            if (intent.hasExtra("vid")) {
                for (int i = 0; i < mRecordAdapter.mList.size(); i++) {
                    if (intent.getStringExtra("vid").equals(mRecordAdapter.mList.get(i).getVid())) {
                        mRecordAdapter.mList.get(i).setComment(intent.getStringExtra("count"));
                        mRecordAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
            FirstData();
        }

    }

    ResultCallback mCallback = new ResultCallback() {
        @Override
        public void callback(String str, int position) {

            Intent intent = new Intent();
            // "变更负责任", "添加相关人", "删除客户", "客户动向"
            if ("变更负责人".equals(str)) {
                intent.putExtra("cid", mCid);
                intent.putExtra("hiddenbottom", true);
                intent.setClass(CrmClientHomeActivity.this, CrmClientDifferentHeadActivity.class);
                CrmClientHomeActivity.this.startActivity(intent);
            } else if ("添加跟进人".equals(str)) {
                intent.putExtra("cid", mCid);
                intent.putExtra("hiddentop", true);
                intent.setClass(CrmClientHomeActivity.this, CrmClientDifferentHeadActivity.class);
                CrmClientHomeActivity.this.startActivity(intent);
            } else if ("放弃客户".equals(str)) {
                showDialog("1");
            } else if ("抢客户".equals(str)) {
                showDialog("2");
            } else if ("客户动向".equals(str)) {
                intent.putExtra("cid", mCid);
                intent.setClass(CrmClientHomeActivity.this, CrmClientTrendActivity.class);
                CrmClientHomeActivity.this.startActivity(intent);
            }

        }
    };

    /**
     * 
     */
    @Override
    public void executeSuccess() {

        // 第一次进来加载数据，马上按返回键会出现异常
        try {
            if (mRecordUtil == null)
                mRecordUtil = new SoundRecordUtil(CrmClientHomeActivity.this, recordResult, 910, 660, 1);

        } catch (Exception e) {
            return;
        }

        if (mJPush) {
            mJPush = false;
            CrmClientDetailVO detailVo = mClientDetailVO.getInfo();
            mCrmEdit = detailVo.getCrmEdit();
            if ("1".equals(mCrmEdit)) {
                mArray = mArrayOne;
            } else {
                mArray = mArrayTwo;
            }
            mCid = detailVo.getCid();
            mClientCompanyAddress.setText(detailVo.getAddress());
            mClientCompanyName.setText(detailVo.getName());

            String level = detailVo.getLevel();
            mClientCompanyLevel.setText(level);
            if ("A级客户".equals(level)) {
                mClientCompanyLevel.setBackgroundResource(R.drawable.frame_shixing_yellow);
            } else if ("B级客户".equals(level)) {
                mClientCompanyLevel.setBackgroundResource(R.drawable.frame_shixing_blue);
            } else if ("C级客户".equals(level)) {
                mClientCompanyLevel.setBackgroundResource(R.drawable.frame_shixing_green);
            } else {
                mClientCompanyLevel.setBackgroundResource(R.drawable.frame_shixing_blue_light);
            }

            mClientSource.setText("负责人：" + detailVo.getFullname());
            mTabTv01Count.setText(detailVo.getVisitCount());
            mTabTv02Count.setText(detailVo.getContactsCount());
            mTabTv03Count.setText(detailVo.getBusinessCount());
            mTabTv04Count.setText(detailVo.getContractCount());
        }

        mHeadLayout.setVisibility(View.GONE);
        if (mCrmClientHomeDetailVO.getArray().get(0).getPraise() != null) {
            mCurrentRequestType = 1;
        } else if (mCrmClientHomeDetailVO.getArray().get(0).getLid() != null) {
            mCurrentRequestType = 2;
        } else if (mCrmClientHomeDetailVO.getArray().get(0).getBid() != null) {
            mCurrentRequestType = 3;
        } else {
            mCurrentRequestType = 4;
        }

        switch (mCurrentRequestType) {
            case 1:
                if (1 == mState) {
                    mRecordAdapter.updateDataFrist(mCrmClientHomeDetailVO.getArray());
                    mTabTv01Count.setText((Integer.parseInt(mTabTv01Count.getText().toString()) + Integer.parseInt(mCrmClientHomeDetailVO.getCount())) + "");
                } else if (2 == mState) {
                    mRecordAdapter.updateDataLast(mCrmClientHomeDetailVO.getArray());
                    mRecordAdapter.setDetaiVo(mCrmClientHomeDetailVO);
                } else {
                    mRecordAdapter.updateData(mCrmClientHomeDetailVO.getArray());
                    mRecordAdapter.setDetaiVo(mCrmClientHomeDetailVO);
                }

                mRecordAdapter.setmType(mCurrentRequestType);

                break;
            case 2:
                mContactsAdapter.updateData(mCrmClientHomeDetailVO.getArray());
                mFootLayout.setVisibility(View.GONE);
                mTabTv02Count.setText(mCrmClientHomeDetailVO.getCount());
                mContactsAdapter.setmType(mCurrentRequestType);
                mContactsAdapter.setDetaiVo(mCrmClientHomeDetailVO);
                break;
            case 3:
                if (1 == mState) {
                    mBusinessAdapter.updateDataFrist(mCrmClientHomeDetailVO.getArray());
                    mTabTv03Count.setText(Integer.parseInt(mTabTv03Count.getText().toString()) + Integer.parseInt(mCrmClientHomeDetailVO.getCount()) + "");
                } else if (2 == mState) {
                    mBusinessAdapter.updateDataLast(mCrmClientHomeDetailVO.getArray());
                    mBusinessAdapter.setDetaiVo(mCrmClientHomeDetailVO);
                } else {
                    mBusinessAdapter.updateData(mCrmClientHomeDetailVO.getArray());
                    mBusinessAdapter.setDetaiVo(mCrmClientHomeDetailVO);
                }
                mBusinessAdapter.setmType(mCurrentRequestType);

                break;
            case 4:
                if (1 == mState) {
                    mTradeContractAdapter.updateDataFrist(mCrmClientHomeDetailVO.getArray());
                    mTabTv04Count.setText(Integer.parseInt(mTabTv04Count.getText().toString()) + Integer.parseInt(mCrmClientHomeDetailVO.getCount()) + "");
                } else if (2 == mState) {
                    mTradeContractAdapter.updateDataLast(mCrmClientHomeDetailVO.getArray());
                    mTradeContractAdapter.setDetaiVo(mCrmClientHomeDetailVO);
                } else {
                    mTradeContractAdapter.updateData(mCrmClientHomeDetailVO.getArray());
                    mTradeContractAdapter.setDetaiVo(mCrmClientHomeDetailVO);
                }
                mTradeContractAdapter.setmType(mCurrentRequestType);

                break;
            default:
                break;
        }

        mLoading.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
        mNoContentView.setVisibility(View.GONE);
        mRecordAdapter.notifyDataSetChanged();
        mRefreshListView.onRefreshComplete();
        if (mState != 1 && mType != 2) {
            footViewIsVisibility();
        }
        mState = 0;

        // 判断公海客户权限
        if (this.getIntent().hasExtra("publish_sp")) {
            baseHeadLayout.setBackgroundColor(Color.parseColor("#251A2A"));
            mArray = mArrayThree;
        } else {
            baseHeadLayout.setBackgroundColor(this.getResources().getColor(R.color.title_father));
            if (this.getIntent().getStringExtra("crmEdit") != null) {
                mCrmEdit = this.getIntent().getStringExtra("crmEdit");
                if ("1".equals(mCrmEdit)) {
                    mArray = mArrayOne;
                } else {
                    mArray = mArrayTwo;
                }
            }
        }
    }

    @Override
    public void executeFailure() {
        try {
            if (mRecordUtil == null)
                mRecordUtil = new SoundRecordUtil(CrmClientHomeActivity.this, recordResult, 910, 660, 1);

        } catch (Exception e) {
            return;
        }
        // 判断公海客户权限
        if (this.getIntent().hasExtra("publish_sp")) {
            baseHeadLayout.setBackgroundColor(Color.parseColor("#251A2A"));
        } else {
            baseHeadLayout.setBackgroundColor(this.getResources().getColor(R.color.title_father));
        }

        if (mJPush) {
            mJPush = false;
            CrmClientDetailVO detailVo = mClientDetailVO.getInfo();
            mCrmEdit = mClientDetailVO.getCrmEdit();
            if ("1".equals(mCrmEdit)) {
                mArray = mArrayOne;
            } else {
                mArray = mArrayTwo;
            }
            mCid = detailVo.getCid();
            mClientCompanyAddress.setText(detailVo.getAddress());
            mClientCompanyName.setText(detailVo.getName());

            String level = detailVo.getLevel();
            mClientCompanyLevel.setText(level);
            if ("A级客户".equals(level)) {
                mClientCompanyLevel.setBackgroundResource(R.drawable.frame_shixing_yellow);
            } else if ("B级客户".equals(level)) {
                mClientCompanyLevel.setBackgroundResource(R.drawable.frame_shixing_blue);
            } else if ("C级客户".equals(level)) {
                mClientCompanyLevel.setBackgroundResource(R.drawable.frame_shixing_green);
            } else {
                mClientCompanyLevel.setBackgroundResource(R.drawable.frame_shixing_blue_light);
            }

            mClientSource.setText("负责人：" + detailVo.getFullname());

            mTabTv01Count.setText(detailVo.getVisitCount());
            mTabTv02Count.setText(detailVo.getContactsCount());
            mTabTv03Count.setText(detailVo.getBusinessCount());
            mTabTv04Count.setText(detailVo.getContractCount());
        }
        mHeadLayout.setVisibility(View.GONE);
        mRefreshListView.onRefreshComplete();
        footViewIsVisibility();

        // 不适合只隐藏列表，适合隐藏整个布局;
        if (mCrmClientHomeDetailVO == null) {
            baseHeadLayout.setBackgroundColor(getResources().getColor(R.color.transparent));
            super.showNoNetView();
        } else {
            List<CrmClientHomeDetailVO> list = new ArrayList<CrmClientHomeDetailVO>();
            if (mState == 0) {
                mLoading.setVisibility(View.GONE);
                mContentLayout.setVisibility(View.VISIBLE);
                mLoadingLayout.setVisibility(View.GONE);
                mNoContentView.setVisibility(View.VISIBLE);
            }
        }

        mState = 0;

        // 判断公海客户权限
        if (this.getIntent().hasExtra("publish_sp")) {
            baseHeadLayout.setBackgroundColor(Color.parseColor("#251A2A"));

            mTitleLayout.setBackgroundResource(R.drawable.crm_publich_client_bg);
            mPublicClient.setVisibility(View.VISIBLE);
            if (mClientDetailVO != null) {
                mClientSource.setText("上次负责人：" + mClientDetailVO.getInfo().getFullname());
            }
            mArray = mArrayThree;
        } else {
            if (this.getIntent().getStringExtra("crmEdit") != null) {
                mCrmEdit = this.getIntent().getStringExtra("crmEdit");
                if ("1".equals(mCrmEdit)) {
                    mArray = mArrayOne;
                } else {
                    mArray = mArrayTwo;
                }
            }
            baseHeadLayout.setBackgroundColor(this.getResources().getColor(R.color.title_father));
        }
    }

    public void footViewIsVisibility() {
        CrmClientHomeDetailVO vo = null;
        switch (mType) {
            case 1:
                vo = mRecordAdapter.getDetaiVo();
                break;

            case 2:
                vo = mContactsAdapter.getDetaiVo();
                break;

            case 3:
                vo = mBusinessAdapter.getDetaiVo();
                break;

            case 4:
                vo = mTradeContractAdapter.getDetaiVo();
                break;

            default:
                break;
        }
        if (vo == null || vo.getCount() == null) {
            mFootLayout.setVisibility(View.GONE);
            return;
        }
        if (Integer.parseInt(vo.getCount()) <= 15) {
            mFootLayout.setVisibility(View.GONE);
        } else {
            mFootLayout.setVisibility(View.VISIBLE);
            mMoreTextView.setText("更多");
            mProgressBar.setVisibility(View.GONE);
        }

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

    public void match(int key, String value) {
        boolean flag = false;
        mNoContentView.setVisibility(View.GONE);

        switch (key) {
            case 1:
                mUrl = value;
                mType = key;
                mRefreshListView.setAdapter(mRecordAdapter);
                mRefreshListView.setRefreshable(true);
                if (mRecordAdapter.mList.size() == 0)
                    flag = true;
                break;
            case 2:
                mUrl = value;
                mType = key;
                mRefreshListView.setAdapter(mContactsAdapter);
                mRefreshListView.setRefreshable(false);
                if (mContactsAdapter.mList.size() == 0)
                    flag = true;
                break;
            case 3:
                mUrl = value;
                mType = key;
                mRefreshListView.setAdapter(mBusinessAdapter);
                mRefreshListView.setRefreshable(true);
                if (mBusinessAdapter.mList.size() == 0)
                    flag = true;
                break;
            case 4:
                mUrl = value;
                mType = key;
                mRefreshListView.setAdapter(mTradeContractAdapter);
                mRefreshListView.setRefreshable(true);
                if (mTradeContractAdapter.mList.size() == 0)
                    flag = true;
                break;

            case 5:
                mFristid = value;
                mLastid = "";
                mState = 1;
                flag = true;
                break;
            case 6:
                mLastid = value;
                mFristid = "";
                mState = 2;
                flag = true;
                break;

            default:
                break;
        }

        if (flag && mFlag) {
            if (key < 5)
                mRefreshListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
            new ThreadUtil(this, this).start();

        }

    }

    public void setTabSelect(int id) {
        for (int i = 0; i < 4; i++) {
            if (id - 1 == i) {
                mTextViewList.get(i).setTextColor(Color.parseColor("#FFEA01"));
            } else {
                mTextViewList.get(i).setTextColor(Color.parseColor("#ffffff"));
            }
        }
    }

    public void registBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(DETAIL_EDIT);
        filter.addAction(BUSSNESS_EDIT);
        filter.addAction(TRADES_EDIT);
        filter.addAction(CONTACT_EDIT);
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
                    mClientCompanyAddress.setText(intent.getStringExtra("address"));
                }
                if (intent.getStringExtra("name") != null) {
                    mClientCompanyName.setText(intent.getStringExtra("name"));
                }

                if (intent.getStringExtra("level") != null) {
                    mClientCompanyLevel.setText(intent.getStringExtra("level"));
                }
                if (intent.getStringExtra("source") != null) {
                    mClientSource.setText(intent.getStringExtra("source"));
                }
            } else if (BUSSNESS_EDIT.equals(intent.getAction())) {
                if (intent.getSerializableExtra("bvo") != null) {
                    CrmBussinesListindexVo vo = (CrmBussinesListindexVo) intent.getSerializableExtra("bvo");
                    for (int i = 0; i < mBusinessAdapter.mList.size(); i++) {
                        if (mBusinessAdapter.mList.get(i).getBid().equals(vo.getBid())) {
                            mBusinessAdapter.mList.get(i).setStatus(vo.getStatus());
                            mBusinessAdapter.mList.get(i).setFullname(vo.getFullname());
                            mBusinessAdapter.mList.get(i).setHeadpic(vo.getHeadpic());
                            mBusinessAdapter.mList.get(i).setDname(vo.getDname());
                            mBusinessAdapter.mList.get(i).setPname(vo.getPname());
                            mBusinessAdapter.mList.get(i).setBname(vo.getBname());
                            mBusinessAdapter.mList.get(i).setMoney(vo.getMoney());
                        }
                    }
                    mBusinessAdapter.notifyDataSetChanged();
                }
            } else if (CONTACT_EDIT.equals(intent.getAction())) {
                String lid = intent.getStringExtra("lid");
                String name = intent.getStringExtra("name");
                String post = intent.getStringExtra("post");
                String headpic = intent.getStringExtra("headpic");
                for (int i = 0; i < mContactsAdapter.mList.size(); i++) {
                    if (lid.equals(mContactsAdapter.mList.get(i).getLid())) {
                        mContactsAdapter.mList.get(i).setLname(name);
                        mContactsAdapter.mList.get(i).setPost(post);
                        mContactsAdapter.mList.get(i).setLheadpic(headpic);
                        break;
                    }
                }
                mContactsAdapter.notifyDataSetChanged();

            } else if (TRADES_EDIT.equals(intent.getAction())) {
                String hid = intent.getStringExtra("hid");
                String status = intent.getStringExtra("status");
                String statusName = intent.getStringExtra("statusName");
                for (int i = 0; i < mTradeContractAdapter.mList.size(); i++) {
                    if (hid.equals(mTradeContractAdapter.mList.get(i).getHid())) {
                        mTradeContractAdapter.mList.get(i).setStatus(status);
                        mTradeContractAdapter.mList.get(i).setStatusName(statusName);
                        break;
                    }
                }
                mTradeContractAdapter.notifyDataSetChanged();

            }
        }
    };

    protected void onDestroy() {
        super.onDestroy();
        unRegistExitReceiver();
        mImageLoader.clearMemoryCache();
        mImageLoader.clearDiskCache();
        if (mRecordUtil != null) {
            mRecordUtil.removeView();
            mRecordUtil = null;
        }

    };

    // 1放弃客户，2分配客户
    public void showDialog(final String status) {
        View view = View.inflate(this, R.layout.dialog_lv_item, null);
        TextView tv = (TextView) view.findViewById(R.id.textview);
        if ("1".equals(status)) {
            tv.setText("您确定要放弃该客户吗？");
        } else {
            tv.setText("您确定要抢该客户吗？");
        }

        mDialog = new BSDialog(this, "友情提醒", view, new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mCommitFlag) {
                    RequestParams params = new RequestParams();
                    params.put("cid", mCid);
                    if ("1".equals(status)) {
                        commit(CrmClientHomeActivity.this, params, Constant.CRM_CLIENT_GIVEUP);
                    } else {
                        commit(CrmClientHomeActivity.this, params, Constant.CRM_HIGHSEAS_GET_CLIENT);
                    }

                }
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.putExtra("contactsCount", mTabTv02Count.getText().toString());
            intent.putExtra("bussnessCount", mTabTv03Count.getText().toString());
            intent.putExtra("tradeCount", mTabTv04Count.getText().toString());
            intent.putExtra("visitorCount", mTabTv01Count.getText().toString());
            intent.putExtra("cid", mCid);
            this.setResult(1, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    RecordResult recordResult = new RecordResult() {

        @Override
        public void getRecordResult(String result) {
            mRecordUtil.removeView();
            mRecordUtil = null;
            showMoreWindow(mTitleLayout);

        }
    };

    private void showMoreWindow(View view) {
        mBottomWindow.showMoreWindow(view, 100);
    }

    MenuCallback mMenuCallback = new MenuCallback() {

        @Override
        public void menuCallback(int type) {
            Intent intent = new Intent();
            if (type == 1) {
                intent.putExtra("cid", mCid);
                intent.putExtra("vistorSate", "3");
                intent.putExtra("cname", mClientCompanyName.getText().toString());
                intent.setClass(CrmClientHomeActivity.this, CrmVisitRecordActivityAddInfo.class);
            } else if (type == 2) {
                intent.putExtra("cid", mCid);
                intent.putExtra("cname", mClientCompanyName.getText().toString());
                intent.putExtra("type", "2");// 2代表通过点击“添加联系人”跳转到“新增联系人”界面
                intent.setClass(CrmClientHomeActivity.this, CrmClientAddContactsActivity.class);
            } else if (type == 3) {
                intent.putExtra("cid", mCid);
                intent.putExtra("addstate", "1");
                intent.putExtra("cname", mClientCompanyName.getText().toString());
                intent.setClass(CrmClientHomeActivity.this, CrmBusinessAddInfoMsgActivity.class);
            } else if (type == 4) {
                intent.putExtra("cid", mCid);
                intent.putExtra("cname", mClientCompanyName.getText().toString());
                intent.setClass(CrmClientHomeActivity.this, CrmTradeContantAddInfo.class);
            } else {
                // 点击取消按钮
                if (mRecordUtil == null)
                    mRecordUtil = new SoundRecordUtil(CrmClientHomeActivity.this, recordResult, 910, 660, 1);
            }

            if (intent.getComponent() != null && mCid != null)
                CrmClientHomeActivity.this.startActivityForResult(intent, 1);
        }
    };

    public void onBackPressed() {
        if (mRecordUtil == null)
            mRecordUtil = new SoundRecordUtil(CrmClientHomeActivity.this, recordResult, 910, 660, 1);
    };

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
                        // setResult(1, new Intent());
                        CrmClientHomeActivity.this.finish();
                    } else {
                        CustomToast.showShortToast(CrmClientHomeActivity.this, str);
                    }

                    CustomDialog.closeProgressDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
