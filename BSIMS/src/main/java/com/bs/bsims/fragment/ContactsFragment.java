
package com.bs.bsims.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.CrmClientAddContactsActivity;
import com.bs.bsims.activity.CrmContactsPhoneListActivity;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.chatutils.ConcatInfoUtils;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.interfaces.UpdateCallback;
import com.bs.bsims.model.ContactDepTabVO;
import com.bs.bsims.model.ResultVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CommonUtils.ResultCallback;
import com.bs.bsims.utils.ContactUtils;
import com.bs.bsims.utils.ContactUtils.ContactInfo;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.utils.UrlUtil;
import com.bs.bsims.view.BSTopIndicator;
import com.bs.bsims.view.BSTopIndicator.OnTopIndicatorListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class ContactsFragment extends BaseFragment implements OnTopIndicatorListener, UpdateCallback, OnClickListener {
    public static final String CONTACTS = "1"; // 1涓洪�璁綍
    public static final String ATTENDANCE = "2";// 2涓鸿�鍕�
    private static final String TAG = "CollectFragment";
    private TextView mTitleTv;
    private Activity mActivity;
    private ViewPager mViewPager;
    private TabPagerAdapter mPagerAdapter;
    private BSTopIndicator mTopIndicator;
    private ResultVO mResultInfoVO;
    private ResultVO mResultVO;
    private TextView mTextView, mRTextView;
    private String[] array = {
            "手动添加", "通讯录导入"
    };
    // 1涓烘樉绀篵oss
    private String mType = "1";

    // 判断是否是CRM
    private String isCrm = "0";

    private TextView mLoading;
    private LinearLayout mLoadingLayout;
    private ContactDepTabVO contactDepResultVo;
    private ContactsDepTabFragment client;
    private View mTitleLayout;
    private boolean mShowTitle = true;

    public static ContactsFragment newInstance() {
        ContactsFragment collectFragment = new ContactsFragment(CONTACTS);
        return collectFragment;
    }

    public ContactsFragment(String type) {
        this.mType = type;
    }

    public ContactsFragment(String type, boolean showTitle) {
        this.mType = type;
        this.mShowTitle = showTitle;
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
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        bindViewsListeners();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDisplay();
        registBroadcast();
    }

    private void initViews(View view) {
        mTitleLayout = view.findViewById(R.id.title_layout);
        if (!mShowTitle) {
            mTitleLayout.setVisibility(View.GONE);
        }
        mTitleTv = (TextView) view.findViewById(R.id.txt_comm_head_activityName);
        view.findViewById(R.id.img_head_back).setVisibility(View.GONE);
        mTitleTv.setText(R.string.tab_phone);
        mTextView = (TextView) view.findViewById(R.id.loading);
        mTextView.setVisibility(View.VISIBLE);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mPagerAdapter = new TabPagerAdapter(getFragmentManager());
        mTopIndicator = (BSTopIndicator) view.findViewById(R.id.top_indicator);
        mRTextView = (TextView) view.findViewById(R.id.txt_comm_head_right);
        isCrm = BSApplication.getInstance().getUserFromServerVO().getIsCrm();

        if (null != isCrm && isCrm.equals("0")) {
            mTopIndicator.setmLabels(new CharSequence[] {
                    "按首字母", "按公司部门"
            });
        }
        else {
            mTopIndicator.setmLabels(new CharSequence[] {
                    "公司通讯录", "企业名录"
            });
        }
        mTopIndicator.updateUI(mActivity);
        mTopIndicator.setOnTopIndicatorListener(this);
        mLoading = (TextView) view.findViewById(R.id.loading);
        mLoadingLayout = (LinearLayout) view.findViewById(R.id.loading_layout);
        mResultInfoVO = BSApplication.getInstance().getResultVO();
        mResultVO = BSApplication.getInstance().getResultVO();
    }

    private void initDisplay() {
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.invalidate();
        // new DataAsyncTask().execute();
        new ThreadUtil(mActivity, this).start();
    }

    public void bindViewsListeners() {
        mRTextView.setOnClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public String getFragmentName() {
        return TAG;
    }

    private class TabPagerAdapter extends FragmentStatePagerAdapter implements
            ViewPager.OnPageChangeListener {
        private List<Fragment> mList;

        public TabPagerAdapter(FragmentManager fm) {
            super(fm);
            mViewPager.setOnPageChangeListener(this);
            mList = new ArrayList<Fragment>();
        }

        @Override
        public Fragment getItem(int position) {
            return mList.get(position);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int position, float positionOffset,
                int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mTopIndicator.setTabsDisplay(mActivity, position);
            if (isCrm != null && isCrm.equals("1") && position == 1) {
                mRTextView.setVisibility(View.VISIBLE);
                mRTextView.setText("添加");
            }
            else {
                mRTextView.setVisibility(View.GONE);
            }

        }

        public void addFragment() {
            ContactsLetterTabFragment contacts = new ContactsLetterTabFragment(mResultInfoVO);
            contacts.setLikeIsGone("1");// 我收藏的显示
            ContactsDepartmentTabFragment departmnet = new ContactsDepartmentTabFragment(mResultVO);
            client = new ContactsDepTabFragment(contactDepResultVo);
            mList.add(contacts);
            // mList.add(departmnet);
            if ("1".equals(isCrm)) {
                mList.add(client);
            } else {
                mList.add(departmnet);
            }

            notifyDataSetChanged();
        }
    }

    @Override
    public void onIndicatorSelected(int index) {
        mViewPager.setCurrentItem(index);
    }

    public boolean getData() {
        Gson gson = new Gson();
        try {
            if ("1".equals(isCrm)) {
                String url1 = UrlUtil.getUrlByMap1(Constant.CRM_CONTACTS_FRAGMENT, null);
                String jsonUrlStr1 = HttpClientUtil.get(url1, Constant.ENCODING).trim();
                contactDepResultVo = gson.fromJson(jsonUrlStr1, ContactDepTabVO.class);
                if (contactDepResultVo != null && mResultVO != null) {
                    return true;
                }
            }
            else{
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean execute() {
        return getData();
    }

    @Override
    public void executeSuccess() {
        // 体验服由于直接返回的true
        if (mResultInfoVO == null || mResultVO == null) {
            mResultInfoVO = BSApplication.getInstance().getResultVO();
            mResultVO = BSApplication.getInstance().getResultVO();
        }
//        if (contactDepResultVo.getCode().equals("400") || mResultInfoVO == null || mResultVO == null || mResultInfoVO.getCode().equals("400") || mResultVO.getCode().equals("400")) {
//            CommonUtils.setNonetContent(mActivity, mLoading, "无相关内容");
//            return;
//        }
        mPagerAdapter.addFragment();
        mTextView.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.GONE);

    }

    @Override
    public void executeFailure() {
        mTextView.setText("加载失败");
        CommonUtils.setNonetIcon(mActivity, mLoading, this);
    }

    // 添加回调函数
    ResultCallback mCallback = new ResultCallback() {

        @Override
        public void callback(String str, int position) {
            if (position == 0) {
                Intent intent = new Intent();
                intent.setClass(mActivity, CrmClientAddContactsActivity.class);
                // 0是联系人列表添加跳转 1详情编辑跳转 2是客户列表添加跳转
                intent.putExtra("type", "0");
                startActivityForResult(intent, 1);
            } else if (position == 1) {
                // 将手机联系人的姓名、电话号码保存到ArrayList<ContactInfo>中
                if (ContactUtils.getContactsList(mActivity) == null) {
                    CustomToast.showShortToast(mActivity, "通讯录没有联系人");
                } else {
                    ArrayList<ContactInfo> contacts = ContactUtils.getContactsList(mActivity);
                    Intent in = new Intent(mActivity, CrmContactsPhoneListActivity.class);
                    in.putExtra("contactInfo", contacts);
                    startActivityForResult(in, 101);
                }

            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_head_back:
                mActivity.finish();
                break;
            case R.id.txt_comm_head_right:
                CommonUtils.initPopViewBg(mActivity, array, mRTextView, mCallback, CommonUtils.getScreenWidth(mActivity) / 3);
                break;

            default:
                break;
        }

    }

    /**
     * 解决网络问题首次加载不出来Crm的漏斗图形的三个页面
     */

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (null == mResultInfoVO || null == contactDepResultVo || null == mResultVO) {
                new ThreadUtil(mActivity, ContactsFragment.this).start();
                ConcatInfoUtils.getInstance().getDepartmentData(mActivity);
            }
        }
    }

    @Override
    public void onDestroy() {
        mActivity.unregisterReceiver(msgBroadcast);
        super.onDestroy();
    }

    public void registBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.UPLOAD_HEAD_ICON_MSG);
        mActivity.registerReceiver(msgBroadcast, filter);
    }

    private void unRegistExitReceiver() {
        mActivity.unregisterReceiver(msgBroadcast);
    }

    private BroadcastReceiver msgBroadcast = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constant.UPLOAD_HEAD_ICON_MSG.equals(intent.getAction())) {
                new ThreadUtil(mActivity, ContactsFragment.this).start();
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (client != null && resultCode == 1)
                    client.onActivityResult(requestCode, resultCode, data);
                break;
            case 101:
                if (client != null && resultCode == 101)
                    client.onActivityResult(requestCode, resultCode, data);
                break;
            default:
                break;
        }

    };

}
