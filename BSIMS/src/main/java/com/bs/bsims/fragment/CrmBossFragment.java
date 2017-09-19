
package com.bs.bsims.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.CrmApprovalPaymentActivity;
import com.bs.bsims.activity.CrmBusinessHomeListActivity;
import com.bs.bsims.activity.CrmClientListActivity;
import com.bs.bsims.activity.CrmProductManagementListActivity;
import com.bs.bsims.activity.CrmSalesTargetListActivity;
import com.bs.bsims.activity.CrmTradeContantHomeListActivity;
import com.bs.bsims.activity.CrmVisitRecordListActivity;
import com.bs.bsims.activity.LocusLineAcitvity;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.interfaces.UpdateCallback;
import com.bs.bsims.model.CrmBossVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.FlowIndicator;
import com.bs.bsims.viewpager.ViewPagerAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CrmBossFragment extends BaseFragment implements OnPageChangeListener, UpdateCallback,
        OnClickListener {

    private Activity mActivity;
    private ViewPager mViewPager;
    private FlowIndicator mFlowIndicator;
    private List<Fragment> mListFragment;
    private PagerAdapter mPgAdapter;
    private CrmBossVO mCrmBossVO;

    private CrmBossFragment1 c_Top1;
    private CrmBossFragment2 c_Top2;
    private CrmBossFragment3 c_Top3;
    private TextView mLoading;
    private LinearLayout mLoadingLayout;
    private TextView new_Contract;

    private LinearLayout rLayoutVistored, rLayoutContact, rLayoutCustomer, rLayoutBussines,
            rLayoutTrade, rLayoutProduct, rLayoutSalePlan, rLayoutReceviable;

    public static CrmBossFragment newInstance() {
        CrmBossFragment collectFragment = new CrmBossFragment();
        return collectFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.crm_boss_index_top3, container,
                false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        new ThreadUtil(mActivity, this).start();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public String getFragmentName() {
        return "";
    }

    public void initView(View view) {
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager1);
        mViewPager.getLayoutParams().height = CommonUtils.getScreenWidth(mActivity) / 2 + 50;
        mFlowIndicator = (FlowIndicator) view.findViewById(R.id.messageIndicator1);
        mListFragment = new ArrayList<Fragment>();

        mPgAdapter = new ViewPagerAdapter(
                ((FragmentActivity) mActivity).getSupportFragmentManager(), mListFragment);
        mViewPager.setAdapter(mPgAdapter);
        mViewPager.setOnPageChangeListener(this);
        new_Contract = (TextView) view.findViewById(R.id.new_contract);// 合同是否有新单
        mLoading = (TextView) view.findViewById(R.id.loading);
        mLoadingLayout = (LinearLayout) view.findViewById(R.id.loading_layout);

        rLayoutVistored = (LinearLayout) view.findViewById(R.id.rLayoutVistored);
        rLayoutContact = (LinearLayout) view.findViewById(R.id.rLayoutContact);
        rLayoutCustomer = (LinearLayout) view.findViewById(R.id.rLayoutCustomer);
        rLayoutBussines = (LinearLayout) view.findViewById(R.id.rLayoutBussines);
        rLayoutTrade = (LinearLayout) view.findViewById(R.id.rLayoutTrade);
        rLayoutProduct = (LinearLayout) view.findViewById(R.id.rLayoutProduct);
        rLayoutSalePlan = (LinearLayout) view.findViewById(R.id.rLayoutSalePlan);
        rLayoutReceviable = (LinearLayout) view.findViewById(R.id.rLayoutReceviable);
        bindListener();
        // CommonSystemUtils.changeHeadFourFragment((RelativeLayout)view.findViewById(R.id.comm_head_layout));''
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        mFlowIndicator.setSeletion(arg0);
    }

    public boolean getData() {
        try {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle()
                    + Constant.CRM_BOSS_INDEX, map);
            Gson gson = new Gson();
            mCrmBossVO = gson.fromJson(jsonStr, CrmBossVO.class);
            if (Constant.RESULT_CODE.equals(mCrmBossVO.getCode())) {
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
    public boolean execute() {
        return getData();
    }

    @Override
    public synchronized void executeSuccess() {

        CrmBossVO ctop1vo = mCrmBossVO.getInfo().getTarget();
        if (ctop1vo != null) {
            c_Top1 = new CrmBossFragment1(ctop1vo);
        }

        CrmBossVO ctop2vo = mCrmBossVO.getInfo();
        if (ctop2vo != null) {
            c_Top2 = new CrmBossFragment2(ctop2vo);
        }

        CrmBossVO ctop3vo = mCrmBossVO.getInfo().getFunnel();
        if (ctop3vo != null) {
            c_Top3 = new CrmBossFragment3(ctop3vo);
        }
        if (mListFragment.size() == 0) {
            mListFragment.add(c_Top1);
            mListFragment.add(c_Top2);
            mListFragment.add(c_Top3);
            mFlowIndicator.setCount(mListFragment.size());
            mFlowIndicator.setSeletion(0);
            mPgAdapter.notifyDataSetChanged();

            mLoading.setVisibility(View.GONE);
            mLoadingLayout.setVisibility(View.GONE);
            if (null != mCrmBossVO.getInfo().getNewContract()
                    && !mCrmBossVO.getInfo().getNewContract().equals("0")) {
                new_Contract.setVisibility(View.VISIBLE);
            }
            else {
                new_Contract.setVisibility(View.VISIBLE);
                new_Contract.setText("");
            }
        }

    }

    @Override
    public void executeFailure() {

        if (mCrmBossVO != null) {
            mLoading.setText(mCrmBossVO.getRetinfo());
            CustomToast.showShortToast(mActivity, mCrmBossVO.getRetinfo());
        } else {
            CommonUtils.setNonetIcon(mActivity, mLoading,this);
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.rLayoutVistored: 
                intent.setClass(mActivity, CrmVisitRecordListActivity.class);// 拜访记录
                break;
            case R.id.rLayoutCustomer:
                intent.setClass(mActivity, CrmClientListActivity.class);// 客户

                break;
            case R.id.rLayoutBussines:
                intent.setClass(mActivity, CrmBusinessHomeListActivity.class);// 销售机会

                break;
            case R.id.rLayoutTrade:
                intent.setClass(mActivity, CrmTradeContantHomeListActivity.class);// 合同

                break;
            case R.id.rLayoutProduct:
                intent.setClass(mActivity, CrmProductManagementListActivity.class);// 产品

                break;
            case R.id.rLayoutSalePlan:
                intent.setClass(mActivity, CrmSalesTargetListActivity.class);// 销售目标
                break;

            case R.id.rLayoutReceviable:
                intent.setClass(mActivity, CrmApprovalPaymentActivity.class);// 回款审批
                break;
            case R.id.rLayoutContact:
                intent.setClass(mActivity, LocusLineAcitvity.class);// 联系人
                break;
            default:
                break;
        }

        startActivity(intent);

    }

    public void bindListener() {
        rLayoutVistored.setOnClickListener(this);
        rLayoutCustomer.setOnClickListener(this);
        rLayoutBussines.setOnClickListener(this);
        rLayoutTrade.setOnClickListener(this);
        rLayoutProduct.setOnClickListener(this);
        rLayoutSalePlan.setOnClickListener(this);
        rLayoutReceviable.setOnClickListener(this);
        rLayoutContact.setOnClickListener(this);
    }

    /**
     * 解决网络问题首次加载不出来Crm的漏斗图形的三个页面
     */

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (mListFragment.size() == 0) {
                new ThreadUtil(mActivity, CrmBossFragment.this).start();
            }

        }

    }

}
