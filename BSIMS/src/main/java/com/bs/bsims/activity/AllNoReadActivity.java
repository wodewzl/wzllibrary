
package com.bs.bsims.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.ApprovalAdapter;
import com.bs.bsims.adapter.CrmApprovalAdapter;
import com.bs.bsims.adapter.CrmBusinessHomeListAdapter;
import com.bs.bsims.adapter.CrmBussinesTranctAdapter;
import com.bs.bsims.adapter.CrmContactAdapter;
import com.bs.bsims.adapter.CrmListAdapter;
import com.bs.bsims.adapter.CrmVisitorIndexListAdapter;
import com.bs.bsims.adapter.NoticeAdapter;
import com.bs.bsims.adapter.TaskEventListLAVAAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.constant.Constant4TaskEventPath;
import com.bs.bsims.constant.ExtrasBSVO;
import com.bs.bsims.model.ApprovalResultVO;
import com.bs.bsims.model.ContactDepTabResultVO;
import com.bs.bsims.model.CrmApprovalVO;
import com.bs.bsims.model.CrmBussinesList;
import com.bs.bsims.model.CrmContactComparatorVO;
import com.bs.bsims.model.CrmListVO;
import com.bs.bsims.model.CrmTranctVo;
import com.bs.bsims.model.CrmVisitorVo;
import com.bs.bsims.model.PublishResultVO;
import com.bs.bsims.model.TaskEventItem;
import com.bs.bsims.model.TaskEventItemVO;
import com.bs.bsims.utils.CharacterParser;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSRefreshListView;
import com.bs.bsims.view.BSRefreshListView.OnRefreshListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class AllNoReadActivity extends BaseActivity implements OnItemClickListener {

    private BSRefreshListView mRefreshListView;

    // 下拉上拉刷新部分
    private View mFootLayout;
    private TextView mMoreTextView;
    private ProgressBar mProgressBar;
    private int mState = 0; // 0为首次,1为上拉刷新 ，2为下拉刷新

    private boolean mFlag = true;
    private String mFristid, mLastid;
    private String mCid;

    private int mType;// 类型，详细看请求数据类型
    private int mCurrentClickPosition;

    // 审批部分
    private ApprovalAdapter mApprovalAdapter;
    private ApprovalResultVO mApprovalResultVO;
    private int mCount = 0;
    private boolean mResult = false;// false 请求失败，true成功
    private HashMap<String, String> map = new HashMap<String, String>();

    // 通知，公文，制度，企业文化部分
    private NoticeAdapter mNoticeAdapter;
    private PublishResultVO mPublishResultVO;

    // 销售商机部分
    private CrmBusinessHomeListAdapter mCrmBusinessHomeListAdapter;
    private CrmBussinesList mCrmbulistVO;

    // crm合同部分
    private CrmBussinesTranctAdapter mCrmBussinesTranctAdapter;
    private CrmTranctVo mCrmTranctVo;

    // crm客户部分
    private CrmListAdapter mCrmListAdapter;
    private CrmListVO mCrmListVO;

    // crm 回款审批
    private CrmApprovalAdapter mCrmApprovalAdapter;
    private CrmApprovalVO mCrmApprovalVO;

    // crm跟单记录
    private CrmVisitorIndexListAdapter mCrmVisitorIndexListAdapter;
    private CrmVisitorVo mVisitor;

    // 任务管理
    private TaskEventItemVO mTaskEventItemVO;
    private TaskEventListLAVAAdapter mTaskEventListLAVAAdapter;

    // 联系人
    private ContactDepTabResultVO mContactDepTabResultVO;
    private CrmContactAdapter mSortAdapter;
    private CharacterParser mCharacterParser;
    private CrmContactComparatorVO mSortComparatorVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.all_no_read, mContentLayout);
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    @Override
    public void updateUi() {

    }

    @Override
    public void initView() {
        mRefreshListView = (BSRefreshListView) findViewById(R.id.lv_refresh);
        initData();
        initFoot();

    }

    public void initData() {
        Intent intent = this.getIntent();
        mType = intent.getIntExtra("type", 0);
        initDefferentType(1);
    }

    @Override
    public void bindViewsListener() {
        mRefreshListView.setonRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                initDefferentType(5);
            }
        });
        mFootLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                initDefferentType(6);
            }
        });
        if (mType != 18)
            mRefreshListView.setOnItemClickListener(this);
    }

    @Override
    public void executeSuccess() {
        super.executeSuccess();

        initDefferentType(3);
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
        mRefreshListView.onRefreshComplete();
        footViewIsVisibility();
        initDefferentType(4);
        mState = 0;
    }

    public void initDefferentType(int stauts) {
        // 1设置适配器,2请求数据参数，3设置请求成攻 4失败，5下拉刷新6点击加载更多
        Intent intent = new Intent();
        switch (mType) {
            case 1:
            case 2:
            case 3:
                String sortid = this.getIntent().getStringExtra("sortid");
                if (stauts == 1) {
                    if ("3".equals(sortid)) {
                        mTitleTv.setText("通知");
                    } else if ("11".equals(sortid)) {
                        mTitleTv.setText("公文");
                    } else if ("19".equals(sortid)) {
                        mTitleTv.setText("企业风采");
                    } else if ("12".equals(sortid)) {
                        mTitleTv.setText("制度");
                    }

                    mNoticeAdapter = new NoticeAdapter(this, sortid);
                    mRefreshListView.setAdapter(mNoticeAdapter);
                } else if (stauts == 2) {
                    Gson gson = new Gson();
                    map.put("sortid", sortid);
                    String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.PUBLISH_LIST, map);
                    mPublishResultVO = gson.fromJson(jsonStrList, PublishResultVO.class);
                    if (Constant.RESULT_CODE.equals(mPublishResultVO.getCode())) {
                        mCount = Integer.parseInt(mPublishResultVO.getCount());
                        mResult = true;
                    } else {
                        mResult = false;
                    }
                } else if (stauts == 3) {
                    if (1 == mState) {
                        mNoticeAdapter.updateDataFrist(mPublishResultVO.getArray());
                    } else if (2 == mState) {
                        mNoticeAdapter.updateDataLast(mPublishResultVO.getArray());
                    } else {
                        mNoticeAdapter.updateData(mPublishResultVO.getArray());
                    }
                } else if (stauts == 4) {
                    if (mPublishResultVO == null) {
                        super.showNoNetView();
                    } else {
                        if (mState == 0) {
                            super.showNoContentView();
                        }
                    }
                } else if (stauts == 5) {
                    if (mNoticeAdapter.mList.size() > 0) {
                        match(1, mNoticeAdapter.mList.get(0).getArticleid());
                    } else {
                        mFristid = "";
                        match(1, "");
                    }
                } else if (stauts == 6) {
                    mMoreTextView.setText("正在加载...");
                    mProgressBar.setVisibility(View.VISIBLE);
                    match(2, mNoticeAdapter.mList.get(mNoticeAdapter.mList.size() - 1).getArticleid());
                } else if (stauts == 7) {
                    intent.putExtra("articleid", mNoticeAdapter.mList.get(mCurrentClickPosition).getArticleid());
                    intent.putExtra("sortid", sortid);
                    intent.setClass(this, NoticeDetailActivity.class);
                    mNoticeAdapter.mList.get(mCurrentClickPosition).setIsread("1");
                    mNoticeAdapter.notifyDataSetChanged();
                    startActivity(intent);
                }
                break;
            case 7:
                if (stauts == 1) {
                    mTitleTv.setText("查阅审批");
                    mApprovalAdapter = new ApprovalAdapter(this);
                    mRefreshListView.setAdapter(mApprovalAdapter);
                } else if (stauts == 2) {
                    Gson gson = new Gson();
                    map.put("isall", CommonUtils.getLimitsSpecial(Constant.LIMITS_SPECIAL001));
                    map.put("modeid", "0");
                    String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.MY_APPROVAL_URL, map);
                    mApprovalResultVO = gson.fromJson(jsonStrList, ApprovalResultVO.class);
                    if (Constant.RESULT_CODE.equals(mApprovalResultVO.getCode())) {
                        mCount = Integer.parseInt(mApprovalResultVO.getCount());
                        mResult = true;
                    } else {
                        mResult = false;
                    }
                } else if (stauts == 3) {
                    if (1 == mState) {
                        mApprovalAdapter.updateDataFrist(mApprovalResultVO.getArray());
                    } else if (2 == mState) {
                        mApprovalAdapter.updateDataLast(mApprovalResultVO.getArray());
                    } else {
                        mApprovalAdapter.updateData(mApprovalResultVO.getArray());
                    }
                } else if (stauts == 4) {
                    if (mApprovalResultVO == null) {
                        super.showNoNetView();
                    } else {
                        if (mState == 0) {
                            super.showNoContentView();
                        }
                    }
                } else if (stauts == 5) {
                    if (mApprovalAdapter.mList.size() > 0) {
                        match(1, mApprovalAdapter.mList.get(0).getTaid());
                    } else {
                        mFristid = "";
                        match(1, "");
                    }
                } else if (stauts == 6) {
                    mMoreTextView.setText("正在加载...");
                    mProgressBar.setVisibility(View.VISIBLE);
                    match(2, mApprovalAdapter.mList.get(mApprovalAdapter.mList.size() - 1).getTaid());
                } else if (stauts == 7) {
                    intent.putExtra("alid", mApprovalAdapter.mList.get(mCurrentClickPosition).getId());
                    intent.putExtra("uid", BSApplication.getInstance().getUserId());
                    String type = mApprovalAdapter.mList.get(mCurrentClickPosition).getType();
                    intent.putExtra("type", type);
                    if ("1".equals(type)) {
                        intent.setClass(this, ApprovalLeaveDetailActivity.class);
                    } else if ("2".equals(type)) {
                        intent.setClass(this, ApprovalSuppliesDetailActivity.class);
                    } else if ("3".equals(type)) {
                        intent.setClass(this, ApprovalOvertimeDetailActivity.class);
                    } else if ("4".equals(type)) {
                        intent.setClass(this, ApprovalFeeApplyDetailActivity.class);
                    } else if ("5".equals(type)) {
                        intent.setClass(this, ApprovalAttendanceDetailActivity.class);
                    } else {
                        intent.setClass(this, ApprovalCustomDetailActivity.class);
                    }
                    this.startActivity(intent);
                }
                break;
            case 8:
                if (stauts == 1) {
                    mTitleTv.setText("任务管理");
                    mTaskEventListLAVAAdapter = new TaskEventListLAVAAdapter(this);
                    mRefreshListView.setAdapter(mTaskEventListLAVAAdapter);
                } else if (stauts == 2) {
                    Gson gson = new Gson();
                    map.put("isall", CommonUtils.getLimitsSpecial(Constant.LIMITS_SPECIAL002));
                    String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant4TaskEventPath.TASKEVENTLIST_PATH_T, map);
                    mTaskEventItemVO = gson.fromJson(jsonStrList, TaskEventItemVO.class);
                    if (Constant.RESULT_CODE.equals(mTaskEventItemVO.getCode())) {
                        mCount = Integer.parseInt(mTaskEventItemVO.getCount());
                        mResult = true;
                    } else {
                        mResult = false;
                    }
                } else if (stauts == 3) {
                    if (1 == mState) {
                        mTaskEventListLAVAAdapter.updateDataFrist(mTaskEventItemVO.getArray());
                    } else if (2 == mState) {
                        mTaskEventListLAVAAdapter.updateDataLast(mTaskEventItemVO.getArray());
                    } else {
                        mTaskEventListLAVAAdapter.updateData(mTaskEventItemVO.getArray());
                    }
                } else if (stauts == 4) {
                    if (mTaskEventItemVO == null) {
                        super.showNoNetView();
                    } else {
                        if (mState == 0) {
                            super.showNoContentView();
                        }
                    }
                } else if (stauts == 5) {
                    if (mTaskEventListLAVAAdapter.mList.size() > 0) {
                        match(1, mTaskEventListLAVAAdapter.mList.get(0).getTaskid());
                    } else {
                        mFristid = "";
                        match(1, "");
                    }
                } else if (stauts == 6) {
                    mMoreTextView.setText("正在加载...");
                    mProgressBar.setVisibility(View.VISIBLE);
                    match(2, mTaskEventListLAVAAdapter.mList.get(mTaskEventListLAVAAdapter.mList.size() - 1).getTaskid());
                } else if (stauts == 7) {

                    TaskEventItem mTaskEventItem10 = mTaskEventListLAVAAdapter.mList.get(mCurrentClickPosition);
                    mTaskEventItem10.setIsnoread("0");
                    mTaskEventListLAVAAdapter.notifyDataSetChanged();
                    intent.setClass(this, EXTTaskEventDetailsActivity.class);
                    intent.putExtra(ExtrasBSVO.Push.BREAK_ID, mTaskEventItem10.getTaskid());
                    startActivity(intent);
                }
                break;
            case 13:

                if (stauts == 1) {
                    mTitleTv.setText("销售商机");
                    mCrmBusinessHomeListAdapter = new CrmBusinessHomeListAdapter(this);
                    mRefreshListView.setAdapter(mCrmBusinessHomeListAdapter);
                } else if (stauts == 2) {
                    Gson gson = new Gson();
                    // map.put("isall", CommonUtils.getLimitsSpecial(Constant.LIMITS_SPECIAL001));
                    String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.CRM_BUSSINES_GETLIST, map);
                    mCrmbulistVO = gson.fromJson(jsonStrList, CrmBussinesList.class);
                    if (Constant.RESULT_CODE.equals(mCrmbulistVO.getCode())) {
                        mCount = Integer.parseInt(mCrmbulistVO.getCount());
                        mResult = true;
                    } else {
                        mResult = false;
                    }
                } else if (stauts == 3) {
                    if (1 == mState) {
                        mCrmBusinessHomeListAdapter.updateDataFrist(mCrmbulistVO.getArray());
                    } else if (2 == mState) {
                        mCrmBusinessHomeListAdapter.updateDataLast(mCrmbulistVO.getArray());
                    } else {
                        mCrmBusinessHomeListAdapter.updateData(mCrmbulistVO.getArray());
                    }
                } else if (stauts == 4) {
                    if (mCrmbulistVO == null) {
                        super.showNoNetView();
                    } else {
                        if (mState == 0) {
                            super.showNoContentView();
                        }
                    }
                } else if (stauts == 5) {
                    if (mCrmBusinessHomeListAdapter.mList.size() > 0) {
                        match(1, mCrmBusinessHomeListAdapter.mList.get(0).getBid());
                    } else {
                        mFristid = "";
                        match(1, "");
                    }
                } else if (stauts == 6) {
                    mMoreTextView.setText("正在加载...");
                    mProgressBar.setVisibility(View.VISIBLE);
                    match(2, mCrmBusinessHomeListAdapter.mList.get(mCrmBusinessHomeListAdapter.mList.size() - 1).getBid());
                } else if (stauts == 7) {
                    mCrmBusinessHomeListAdapter.mList.get(mCurrentClickPosition).setIsread("1");
                    mCrmBusinessHomeListAdapter.notifyDataSetChanged();
                    intent.setClass(this, CrmBusinessHomeIndexOneInfo.class);
                    intent.putExtra("bid", mCrmBusinessHomeListAdapter.mList.get(mCurrentClickPosition).getBid());
                    intent.putExtra("stateUtilthread", "3");// 回来更改状态
                    startActivity(intent);
                }
                break;

            case 14:
                if (stauts == 1) {
                    mTitleTv.setText("合同管理");
                    mCrmBussinesTranctAdapter = new CrmBussinesTranctAdapter(this);
                    mRefreshListView.setAdapter(mCrmBussinesTranctAdapter);
                } else if (stauts == 2) {
                    Gson gson = new Gson();
                    String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.CRM_BUSSINES_HOMEINDEXANDDETAILSOFTRANTS, map);
                    mCrmTranctVo = gson.fromJson(jsonStrList, CrmTranctVo.class);
                    if (Constant.RESULT_CODE.equals(mCrmTranctVo.getCode())) {
                        mCount = Integer.parseInt(mCrmTranctVo.getCount());
                        mResult = true;
                    } else {
                        mResult = false;
                    }
                } else if (stauts == 3) {
                    if (1 == mState) {
                        mCrmBussinesTranctAdapter.updateDataFrist(mCrmTranctVo.getArray());
                    } else if (2 == mState) {
                        mCrmBussinesTranctAdapter.updateDataLast(mCrmTranctVo.getArray());
                    } else {
                        mCrmBussinesTranctAdapter.updateData(mCrmTranctVo.getArray());
                    }
                } else if (stauts == 4) {
                    if (mCrmTranctVo == null) {
                        super.showNoNetView();
                    } else {
                        if (mState == 0) {
                            super.showNoContentView();
                        }
                    }
                } else if (stauts == 5) {
                    if (mCrmBussinesTranctAdapter.mList.size() > 0) {
                        match(1, mCrmBussinesTranctAdapter.mList.get(0).getHid());
                    } else {
                        mFristid = "";
                        match(1, "");
                    }
                } else if (stauts == 6) {
                    mMoreTextView.setText("正在加载...");
                    mProgressBar.setVisibility(View.VISIBLE);
                    match(2, mCrmBussinesTranctAdapter.mList.get(mCrmBussinesTranctAdapter.mList.size() - 1).getHid());
                } else if (stauts == 7) {
                    CrmTranctVo vo = mCrmBussinesTranctAdapter.mList.get(mCurrentClickPosition);
                    vo.setIsread("1");
                    mCrmBussinesTranctAdapter.notifyDataSetChanged();
                    String status = vo.getStatus();
                    if ("2".equals(vo.getDirection())) {
                        intent.setClass(this, CrmTradeContantDetailsIndexActivity.class);
                        intent.putExtra("hid", mCrmBussinesTranctAdapter.mList.get(mCurrentClickPosition).getHid());
                        startActivityForResult(intent, 1);
                    } else {
                        intent.setClass(this, CrmTradeContantDeatilsHomeTop3Activity.class);
                        intent.putExtra("hid", mCrmBussinesTranctAdapter.mList.get(mCurrentClickPosition).getHid());
                        intent.putExtra("title", mCrmBussinesTranctAdapter.mList.get(mCurrentClickPosition).getTitle());
                        intent.putExtra("money", mCrmBussinesTranctAdapter.mList.get(mCurrentClickPosition).getMoney());
                        intent.putExtra("payment", mCrmBussinesTranctAdapter.mList.get(mCurrentClickPosition).getPayment());
                        intent.putExtra("cname", mCrmBussinesTranctAdapter.mList.get(mCurrentClickPosition).getCname());
                        intent.putExtra("statusName", mCrmBussinesTranctAdapter.mList.get(mCurrentClickPosition).getStatusName());
                        intent.putExtra("receiptMoney", mCrmBussinesTranctAdapter.mList.get(mCurrentClickPosition).getReceipt_money());
                        intent.putExtra("changeStatus", mCrmBussinesTranctAdapter.mList.get(mCurrentClickPosition).getChangeStatus());
                        intent.putExtra("changeStatusName", mCrmBussinesTranctAdapter.mList.get(mCurrentClickPosition).getChangeStatusName());
                        intent.putExtra("status", mCrmBussinesTranctAdapter.mList.get(mCurrentClickPosition).getStatus());
                        startActivity(intent);
                    }
                }
                break;

            case 15:
                if (stauts == 1) {
                    mTitleTv.setText("客户管理");
                    mCrmListAdapter = new CrmListAdapter(this);
                    mRefreshListView.setAdapter(mCrmListAdapter);
                } else if (stauts == 2) {
                    Gson gson = new Gson();
                    String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.CRM_CLIENT_LIST, map);
                    mCrmListVO = gson.fromJson(jsonStrList, CrmListVO.class);
                    if (Constant.RESULT_CODE.equals(mCrmListVO.getCode())) {
                        mCount = Integer.parseInt(mCrmListVO.getCount());
                        mResult = true;
                    } else {
                        mResult = false;
                    }
                } else if (stauts == 3) {
                    if (1 == mState) {
                        mCrmListAdapter.updateDataFrist(mCrmListVO.getArray());
                    } else if (2 == mState) {
                        mCrmListAdapter.updateDataLast(mCrmListVO.getArray());
                    } else {
                        mCrmListAdapter.updateData(mCrmListVO.getArray());
                    }
                } else if (stauts == 4) {
                    if (mCrmListVO == null) {
                        super.showNoNetView();
                    } else {
                        if (mState == 0) {
                            super.showNoContentView();
                        }
                    }
                } else if (stauts == 5) {
                    if (mCrmListAdapter.mList.size() > 0) {
                        match(1, mCrmListAdapter.mList.get(0).getCid());
                    } else {
                        mFristid = "";
                        match(1, "");
                    }
                } else if (stauts == 6) {
                    mMoreTextView.setText("正在加载...");
                    mProgressBar.setVisibility(View.VISIBLE);
                    match(2, mCrmListAdapter.mList.get(mCrmListAdapter.mList.size() - 1).getCid());
                } else if (stauts == 7) {
                    if (mCrmListAdapter.mList.size() > 0) {
                        CrmListVO vo = mCrmListAdapter.mList.get(mCurrentClickPosition);
                        vo.setIsread("1");
                        mCrmListAdapter.notifyDataSetChanged();
                        intent.putExtra("cid", vo.getCid());
                        intent.putExtra("address", vo.getAddress());
                        intent.putExtra("name", vo.getName());
                        intent.putExtra("level", vo.getLevel());
                        intent.putExtra("souce", vo.getSource());
                        intent.putExtra("oldfullname", vo.getOldfullname());
                        intent.putExtra("fullname", vo.getFullname());
                        intent.putExtra("crmEdit", vo.getCrmEdit());
                        if ("1".equals(vo.getIsPub())) {
                            intent.setClass(this, CrmHighseasClientsHomeActivity.class);
                            startActivityForResult(intent, 1);
                        } else {
                            intent.putExtra("visit_count", vo.getVisitCount());
                            intent.putExtra("contacts_count", vo.getContactsCount());
                            intent.putExtra("business_count", vo.getBusinessCount());
                            intent.putExtra("contract_count", vo.getContractCount());
                            intent.setClass(this, CrmClientHomeActivity.class);
                            startActivity(intent);
                        }
                    }
                }
                break;
            case 16:
                if (stauts == 1) {
                    mTitleTv.setText("联系人");
                    mSortAdapter = new CrmContactAdapter(this, new ArrayList<ContactDepTabResultVO>());
                    mRefreshListView.setAdapter(mSortAdapter);
                    mRefreshListView.setRefreshable(false);
                    mCharacterParser = CharacterParser.getInstance();
                    mSortComparatorVO = new CrmContactComparatorVO();
                } else if (stauts == 2) {
                    Gson gson = new Gson();
                    map.put("type", "0");
                    String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.CRM_CONTACT_LIST, map);
                    mContactDepTabResultVO = gson.fromJson(jsonStrList, ContactDepTabResultVO.class);
                    if (Constant.RESULT_CODE.equals(mContactDepTabResultVO.getCode())) {
                        mCount = Integer.parseInt(mContactDepTabResultVO.getCount());
                        mResult = true;
                    } else {
                        mResult = false;
                    }
                } else if (stauts == 3) {
                    mCharacterParser = CharacterParser.getInstance();
                    mSortComparatorVO = new CrmContactComparatorVO();
                    List<ContactDepTabResultVO> list = mContactDepTabResultVO.getList();
                    List<ContactDepTabResultVO> sortList = sortData(list);
                    Collections.sort(sortList, mSortComparatorVO);// 给list列表排序
                    mSortAdapter.updateListView(sortList);// 刷新数据
                } else if (stauts == 4) {
                    if (mContactDepTabResultVO == null) {
                        super.showNoNetView();
                    } else {
                        if (mState == 0) {
                            super.showNoContentView();
                        }
                    }
                } else if (stauts == 7) {
                    mSortAdapter.list.get(mCurrentClickPosition).setIsread("1");
                    mSortAdapter.notifyDataSetChanged();
                    intent.setClass(this, CrmContactDetailActivity.class);
                    intent.putExtra("lid", mSortAdapter.list.get(mCurrentClickPosition).getLid());
                    startActivity(intent);
                }
                break;
            case 17:
                if (stauts == 1) {
                    mTitleTv.setText("回款管理");
                    mCrmApprovalAdapter = new CrmApprovalAdapter(this);
                    mRefreshListView.setAdapter(mCrmApprovalAdapter);
                } else if (stauts == 2) {
                    Gson gson = new Gson();
                    String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.CRM_APPROVAL_LIST, map);
                    mCrmApprovalVO = gson.fromJson(jsonStrList, CrmApprovalVO.class);
                    if (Constant.RESULT_CODE.equals(mCrmApprovalVO.getCode())) {
                        mCount = Integer.parseInt(mCrmApprovalVO.getCount());
                        mResult = true;
                    } else {
                        mResult = false;
                    }
                } else if (stauts == 3) {
                    if (1 == mState) {
                        mCrmApprovalAdapter.updateDataFrist(mCrmApprovalVO.getArray());
                    } else if (2 == mState) {
                        mCrmApprovalAdapter.updateDataLast(mCrmApprovalVO.getArray());
                    } else {
                        mCrmApprovalAdapter.updateData(mCrmApprovalVO.getArray());
                    }
                } else if (stauts == 4) {
                    if (mCrmApprovalVO == null) {
                        super.showNoNetView();
                    } else {
                        if (mState == 0) {
                            super.showNoContentView();
                        }
                    }
                } else if (stauts == 5) {
                    if (mCrmApprovalAdapter.mList.size() > 0) {
                        match(1, mCrmApprovalAdapter.mList.get(0).getMid());
                    } else {
                        mFristid = "";
                        match(1, "");
                    }
                } else if (stauts == 6) {
                    mMoreTextView.setText("正在加载...");
                    mProgressBar.setVisibility(View.VISIBLE);
                    match(2, mCrmApprovalAdapter.mList.get(mCrmApprovalAdapter.mList.size() - 1).getMid());
                } else if (stauts == 7) {
                    CrmApprovalVO vo = mCrmApprovalAdapter.mList.get(mCurrentClickPosition);
                    vo.setIsread("1");
                    mCrmApprovalAdapter.notifyDataSetChanged();
                    intent.putExtra("mid", vo.getMid());
                    intent.setClass(this, CrmApprovalDetailActivity.class);
                    startActivity(intent);
                }
                break;

            case 18:
                if (stauts == 1) {
                    mTitleTv.setText("跟单记录");
                    mCrmVisitorIndexListAdapter = new CrmVisitorIndexListAdapter(this);
                    mCrmVisitorIndexListAdapter.setState("1");
                    mRefreshListView.setAdapter(mCrmVisitorIndexListAdapter);
                } else if (stauts == 2) {
                    Gson gson = new Gson();
                    String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.CRM_VISIT_RECORD_LISTINDEX, map);
                    mVisitor = gson.fromJson(jsonStrList, CrmVisitorVo.class);
                    if (Constant.RESULT_CODE.equals(mVisitor.getCode())) {
                        mCount = Integer.parseInt(mVisitor.getCount());
                        mResult = true;
                    } else {
                        mResult = false;
                    }
                } else if (stauts == 3) {
                    if (1 == mState) {
                        mCrmVisitorIndexListAdapter.updateDataFrist(mVisitor.getArray());
                    } else if (2 == mState) {
                        mCrmVisitorIndexListAdapter.updateDataLast(mVisitor.getArray());
                    } else {
                        mCrmVisitorIndexListAdapter.updateData(mVisitor.getArray());
                    }
                } else if (stauts == 4) {
                    if (mVisitor == null) {
                        super.showNoNetView();
                    } else {
                        if (mState == 0) {
                            super.showNoContentView();
                        }
                    }
                } else if (stauts == 5) {
                    if (mCrmVisitorIndexListAdapter.mList.size() > 0) {
                        match(1, mCrmVisitorIndexListAdapter.mList.get(0).getVid());
                    } else {
                        mFristid = "";
                        match(1, "");
                    }
                } else if (stauts == 6) {
                    mMoreTextView.setText("正在加载...");
                    mProgressBar.setVisibility(View.VISIBLE);
                    match(2, mCrmVisitorIndexListAdapter.mList.get(mCrmVisitorIndexListAdapter.mList.size() - 1).getVid());
                }
                break;

            default:
                break;
        }
    }

    public void footViewIsVisibility() {
        if (mCount == 0) {
            return;
        }
        if (mCount <= 15) {
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

    public boolean getData() {
        try {
            Intent intent = new Intent();
            String jsonStrList = "";
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            if (0 == mState) {
                mFristid = "";
                mLastid = "";
            }
            map.put("firstid", mFristid);
            map.put("lastid", mLastid);
            map.put("unread", "1");
            map.put("isboss", BSApplication.getInstance().getUserFromServerVO().getIsboss());
            initDefferentType(2);
            return mResult;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        mCurrentClickPosition = (int) arg3;
        initDefferentType(7);

    }

    // list每个元素增加字段sortLetters,并把list值传给mList
    private List<ContactDepTabResultVO> sortData(List<ContactDepTabResultVO> list) {
        List<ContactDepTabResultVO> sortList = new ArrayList<ContactDepTabResultVO>();
        for (int i = 0; i < list.size(); i++) {
            ContactDepTabResultVO personnelVO = list.get(i);

            // 汉字转换成拼音List<BusinessVisit> visitList
            String pinyin = mCharacterParser.getSelling(personnelVO.getLname());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                personnelVO.setSortLetters(sortString.toUpperCase());
            } else {
                personnelVO.setSortLetters("#");
            }
            sortList.add(personnelVO);
        }
        return sortList;
    }
}
