
package com.bs.bsims.activity;

import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.bs.bsims.R;
import com.bs.bsims.adapter.CrmSaleDetailsAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.CrmOptionsVO;
import com.bs.bsims.model.CrmProductVo;
import com.bs.bsims.model.TreeVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.utils.UrlUtil;
import com.bs.bsims.utils.CommonUtils.ResultCallback;
import com.bs.bsims.view.BSIndexEditText;
import com.bs.bsims.view.BSPopupWindowsTitle;
import com.bs.bsims.view.BSPopupWindowsTitle.TreeCallBack;
import com.bs.bsims.view.BSRefreshListView;
import com.bs.bsims.view.BSRefreshListView.OnRefreshListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//合同主页
public class CrmSalesTargetListActivity extends BaseActivity implements OnClickListener {

    private Context context;
    private CrmProductVo crmtranctsbussinesVo;
    private BSRefreshListView crm_business_indexlistview;
    private TextView mLoading;
    // listView
    private View mFootLayout;
    private TextView mMoreTextView, loadingfile1;
    private ProgressBar mProgressBar;
    private String mRefresh = "";
    // 0为首次,1为上拉刷新 ，2为下拉刷新
    private int mState = 0;
    private CrmSaleDetailsAdapter crmbussinesApdater;

    private BSIndexEditText mClearEditText;
    /** 关键词搜索（选传） */
    private String keyword = "";

    /** 上拉ID */
    private String firstid;

    /** 下拉ID */
    private String lastid;

    /** 记录最后一条的id */
    private String saveLastId;

    private String tyepkey = "0";

    private int mStatus;// 1为第一个弹出框，2为第二个弹出框
    private boolean mFlag = true;

    private CrmOptionsVO mCrmOptionsVO;
    private BSPopupWindowsTitle mBsPopupWindowsTitle;
    /**
     * 定义一个数据源 在每一次搜索之前记录当前listview中的mlist
     */
    private LinearLayout one_titleall;
    private LinearLayout mOneTitle, mTwoTitle;
    private TextView mOneTitleTv, mTwoTitleTv;
    private List<View> mViewList;// 筛选选中的布局
    private String mSourcekey, mStatuskey, mDatekey, mOrderkey;
    private LinearLayout mOptionsSelected;
    private String popstate;

    private int itmepostion = -1;

    /* 筛选参数 */
    private String did;// 部门id
    private String year;// 年份
    private String quarter;// 季度
    private String type;// 类型
    private String month;// 月份

    private Boolean canClickFlag = true;// 解决连续点“更多”可能会出现的异常

    private PopupWindow mTimePop;// 时间筛选弹出框

    @Override
    public void baseSetContentView() {
        // TODO Auto-generated method stub
        View layout = View.inflate(this, R.layout.crmsale_index,
                mContentLayout);
        context = this;
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

        mTitleTv.setText("销售目标");
        mOkTv.setVisibility(View.GONE);
        crm_business_indexlistview = (BSRefreshListView) findViewById(R.id.crm_business_indexlistview);
        crmbussinesApdater = new CrmSaleDetailsAdapter(context, "1");
        crm_business_indexlistview.setAdapter(crmbussinesApdater);
        initFoot();
        loadingfile1 = (TextView) findViewById(R.id.loadingfile1);
        mClearEditText = (BSIndexEditText) findViewById(R.id.edit_single_search);
        mOneTitle = (LinearLayout) findViewById(R.id.one_title);
        mTwoTitle = (LinearLayout) findViewById(R.id.two_title);
        mOneTitleTv = (TextView) findViewById(R.id.one_title_tv);
        mTwoTitleTv = (TextView) findViewById(R.id.two_title_tv);
        one_titleall = (LinearLayout) findViewById(R.id.one_titleall);

        mOptionsSelected = (LinearLayout) findViewById(R.id.options_selected);
        mTwoTitleTv.setText(DateUtils.parseYearMonth(System.currentTimeMillis() / 1000 + "").replace("-", "年") + "月");
    }

    @Override
    public void bindViewsListener() {
        mOkTv.setOnClickListener(this);
        mOneTitle.setOnClickListener(this);
        mTwoTitle.setOnClickListener(this);
        bindRefreshListener();
        crm_business_indexlistview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

            }

        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_comm_head_right:
                // Intent i = new Intent(CrmTradeContantHomeListActivity.this,
                // CrmBusinessAddInfoMsgActivity.class); // 跳转到添加商机的页面
                // startActivity(i);
                break;
            case R.id.one_title:
                mStatus = 1;
                initPopData();
                break;
            case R.id.two_title:
                mStatus = 2;
                initPopData();
                break;
            default:
                break;
        }

    }

    @Override
    public void executeSuccess() {
        canClickFlag = true;
        if (mFlag) {
            mFlag = false;
        }
        if (null != mCrmOptionsVO && null != mCrmOptionsVO.getYears())
            mCrmOptionsVO.getYears().clear();
        else {
            mCrmOptionsVO = new CrmOptionsVO();
        }
        mCrmOptionsVO.setYears(crmtranctsbussinesVo.getYears());
        tyepkey = "0";
        loadingfile1.setVisibility(View.GONE);
        crm_business_indexlistview.setVisibility(View.VISIBLE);
        mContentLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
        // crmbussinesApdater = new CrmBusinessHomeListAdapter(context,
        // crmbulist.getArray());
        // crm_business_indexlistview.setAdapter(crmbussinesApdater);
        // crmbussinesApdater.notifyDataSetChanged();
        if (1 == mState) {
            crmbussinesApdater.updateDataFrist(crmtranctsbussinesVo.getArray());
        } else if (2 == mState) {
            crmbussinesApdater.updateDataLast(crmtranctsbussinesVo.getArray());
        } else {
            crmbussinesApdater.updateData(crmtranctsbussinesVo.getArray());
        }
        mState = 0;
        crm_business_indexlistview.setVisibility(View.VISIBLE);
        crm_business_indexlistview.onRefreshComplete();
        if (mState != 1)
            footViewIsVisibility();
    }

    @Override
    public void executeFailure() {
        canClickFlag = true;
        if (mFlag) {
            if (null != mCrmOptionsVO && null != mCrmOptionsVO.getYears())
                mCrmOptionsVO.getYears().clear();
            else {
                mCrmOptionsVO = new CrmOptionsVO();
            }
            mCrmOptionsVO.setYears(crmtranctsbussinesVo.getYears());
            mFlag = false;
        }
        // 网络异常
        if (null == crmtranctsbussinesVo) {
            super.showNoNetView();
            return;
        }

        tyepkey = "0";
        mContentLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
        if (null == crmbussinesApdater.mList) {
            crm_business_indexlistview.setVisibility(View.GONE);
            loadingfile1.setVisibility(View.VISIBLE);
            return;
        }
        if (crmbussinesApdater.mList.size() > 0) {
            crm_business_indexlistview.setVisibility(View.VISIBLE);
            loadingfile1.setVisibility(View.GONE);
        } else {
            crm_business_indexlistview.setVisibility(View.GONE);
            loadingfile1.setVisibility(View.VISIBLE);
        }
        mState = 0;
        crm_business_indexlistview.onRefreshComplete();
        if (mState != 1)
            footViewIsVisibility();
    }

    public boolean getData() {

        Gson gson = new Gson();
        if (mFlag) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put("type", "3");
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle()
                    + Constant.CRM_GETVISTITORTOBUSSINES, map);
            mCrmOptionsVO = gson.fromJson(jsonStr, CrmOptionsVO.class);
            mFlag = false;
        }

        Map paramsMap = null;
        if (mState == 0) {
            paramsMap = new HashMap<String, String>();
        } else if (mState == 1) {
            if (mRefresh.equals("")) {
                paramsMap = new HashMap<String, String>();
            } else {
                // firstid = crmbussinesApdater.mList.get(0).getBid();
                paramsMap = new HashMap<String, String>();
                paramsMap.put("firstid", firstid);
            }

        } else {
            lastid = saveLastId;// mEXTSharedfilesdHomeMyUpdateAdapter.mList.get(mEXTSharedfilesdHomeMyUpdateAdapter.mList.size()
                                // - 1).getSharedid();
            paramsMap = new HashMap<String, String>();
            paramsMap.put("lastid", lastid);
        }
        paramsMap.put("type", type);
        paramsMap.put("year", year);
        paramsMap.put("month", month);
        paramsMap.put("did", did);
        try {
            String urlStr1 = UrlUtil.getUrlByMap1(Constant.CRM_SALEDETAIL,
                    paramsMap);
            String jsonUrlStr1;
            jsonUrlStr1 = HttpClientUtil.get(urlStr1, Constant.ENCODING).trim();
            crmtranctsbussinesVo = gson.fromJson(jsonUrlStr1,
                    CrmProductVo.class);
            if (crmtranctsbussinesVo.getCode().equals("200")) {

                return true;
            } else {
                if (tyepkey.equals("1")) {
                    crmbussinesApdater.mList.clear();
                }
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;

    }

    public void bindRefreshListener() {
        crm_business_indexlistview
                .setonRefreshListener(new OnRefreshListener() {

                    @Override
                    public void onRefresh() {
                        mState = 1;
                        mRefresh = Constant.FIRSTID;
                        if (crmbussinesApdater.mList.size() == 0)
                            mRefresh = "";
                        else
                            firstid = crmbussinesApdater.mList.get(0).getTid();
                        new ThreadUtil(context,
                                CrmSalesTargetListActivity.this).start();
                    }
                });
        mFootLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (canClickFlag) {
                    canClickFlag = false;
                    mMoreTextView.setText("正在加载...");
                    mProgressBar.setVisibility(View.VISIBLE);
                    saveLastId = crmbussinesApdater.mList.get(crmbussinesApdater.mList.size() - 1)
                            .getTid();
                    mState = 2;
                    mRefresh = Constant.LASTID;
                    new ThreadUtil(context, CrmSalesTargetListActivity.this)
                            .start();
                }
            }
        });

    }

    // 加载更多数据
    public void initFoot() {
        mFootLayout = LayoutInflater.from(context).inflate(
                R.layout.listview_bottom_more, null);
        mMoreTextView = (TextView) mFootLayout.findViewById(R.id.txt_loading);
        mMoreTextView.setText("更多");
        mProgressBar = (ProgressBar) mFootLayout.findViewById(R.id.progressBar);
        mFootLayout.setVisibility(View.GONE);
        crm_business_indexlistview.addFooterView(mFootLayout);
    }

    protected void footViewIsVisibility() {
        if (crmtranctsbussinesVo == null) {
            return;
        }
        if (crmtranctsbussinesVo.getCount() == null) {
            return;
        }
        if (Integer.parseInt(crmtranctsbussinesVo.getCount()) <= 15) {
            mFootLayout.setVisibility(View.GONE);
            // listView.removeFooterView(mFootLayout);
        } else {
            mFootLayout.setVisibility(View.VISIBLE);
            mMoreTextView.setText("更多");
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void filterData(String filterStr) {
        if (TextUtils.isEmpty(filterStr)) {
            keyword = "";
            return;
        } else {
            keyword = filterStr;
            mClearEditText
                    .setOnEditorActionListener(new OnEditorActionListener() {

                        @Override
                        public boolean onEditorAction(TextView v, int actionId,
                                KeyEvent event) {
                            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                                // 先隐藏键盘
                                ((InputMethodManager) mClearEditText
                                        .getContext().getSystemService(
                                                Context.INPUT_METHOD_SERVICE))
                                        .hideSoftInputFromWindow(
                                                CrmSalesTargetListActivity.this
                                                        .getCurrentFocus()
                                                        .getWindowToken(),
                                                InputMethodManager.HIDE_NOT_ALWAYS);

                                // cleanData();
                                // new ThreadUtil(context,
                                // CrmSalesTargetListActivity.this).start();
                                CustomToast.showLongToast(context, "此页面不支持搜索！");
                                return true;
                            }
                            return false;
                        }
                    });
        }
    }

    public void initPopData() {
        if (null != mCrmOptionsVO) {
            List<CrmOptionsVO> parentList = new ArrayList<CrmOptionsVO>();

            String popName[] = {
                    getResources().getString(R.string.sale_pop1),
                    getResources().getString(R.string.sale_pop2)
            };
            if (mStatus == 1) {
                /* 自己创建筛选菜单 */
                CrmOptionsVO mCrmOptionsVO2;
                for (int i = 0; i < 2; i++) {
                    mCrmOptionsVO2 = new CrmOptionsVO();
                    mCrmOptionsVO2.setId(i + "");
                    mCrmOptionsVO2.setName(popName[i] + "");
                    parentList.add(mCrmOptionsVO2);
                }
                ArrayList<TreeVO> list = getOneLeveTreeVo(parentList);
                mBsPopupWindowsTitle = new BSPopupWindowsTitle(this, list, callback,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                mBsPopupWindowsTitle.showPopupWindow(one_titleall);
            }
            else if (mStatus == 2 && null != mCrmOptionsVO.getYears()) {
                // String Month[] = {
                // "1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"
                // };
                // String[] parentStr = new String[mCrmOptionsVO.getYears().size()];
                // String[][] childStr = new String[mCrmOptionsVO.getYears().size()][12];
                // for (int i = 0; i < mCrmOptionsVO.getYears().size(); i++) {
                // parentStr[i] = mCrmOptionsVO.getYears().get(i) + "年";
                // for (int j = 0; j < Month.length; j++) {
                // childStr[i][j] = Month[j];
                // }
                // }
                //
                // ArrayList<TreeVO> list = CommonUtils.getTreeVOListNoAll(parentStr, childStr);
                // mBsPopupWindowsTitle = new BSPopupWindowsTitle(this, list, callback);
                // mBsPopupWindowsTitle.showPopupWindow(mOneTitle);
                if (mTimePop == null) {
                    mTimePop = CommonUtils.initPopView(CrmSalesTargetListActivity.this, 3, ViewGroup.LayoutParams.WRAP_CONTENT, timeCallback);
                }
                if (!mTimePop.isShowing()) {
                    mTimePop.showAsDropDown(mOneTitle);
                } else {
                    mTimePop.dismiss();
                }

            }

        }
        else {
            return;
        }

    }

    ResultCallback timeCallback = new ResultCallback() {

        @Override
        public void callback(String str, int position) {
            try {
                mTwoTitleTv.setText(str);
                year = str.split("-")[0];
                month = str.split("-")[1];
                crm_business_indexlistview.changeHeaderViewByState(BSRefreshListView.REFRESHING);
                tyepkey = "1";
                mState = 0;
                new ThreadUtil(context, CrmSalesTargetListActivity.this).start();
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }

        }
    };

    public ArrayList<TreeVO> getOneLeveTreeVo(List<CrmOptionsVO> parentList) {
        ArrayList<TreeVO> list = new ArrayList<TreeVO>();
        for (int i = 0; i < parentList.size(); i++) {
            TreeVO vo = new TreeVO();
            vo.setName(parentList.get(i).getName());
            vo.setParentSerachId((i + 1) + "");
            vo.setLevel(1);
            list.add(vo);
        }
        return list;
    }

    // 菜单点击回调函数
    TreeCallBack callback = new TreeCallBack() {

        @Override
        public void callback(TreeVO vo) {
            if (mStatus == 1) {
                type = vo.getParentSerachId();
                mOneTitleTv.setText(vo.getName());
            }
            // else if (mStatus == 2) {
            // year = vo.getParentName().split("年")[0];
            // month = vo.getName().split("月")[0];
            // mTwoTitleTv.setText(vo.getParentName() + vo.getName());
            // }

            crm_business_indexlistview.changeHeaderViewByState(BSRefreshListView.REFRESHING);
            tyepkey = "1";
            mState = 0;
            new ThreadUtil(context, CrmSalesTargetListActivity.this).start();
        }
    };

}
