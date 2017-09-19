
package com.bs.bsims.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.activity.HeadAttentionActivity;
import com.bs.bsims.activity.JournalPublishActivity;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.interfaces.UpdateCallback;
import com.bs.bsims.model.JournalListVO1;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CommonUtils.ResultCallback;
import com.bs.bsims.utils.HttpClientUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JournalListFragment extends BaseFragment implements OnClickListener, UpdateCallback {
    public static final int EDIT_ATTENTION = 1;// 编辑关注人返回参数
    public static final int JOURANL_ALL_AND_ATTENTION = 2;// 全部和关注日志返回码
    public static final int JOURNAL_PUBLISH = 3;// 日志发布
    private String mFristid, mLastid;
    private int mState = 0; // 0为首次,1为上拉刷新 ，2为下拉刷新
    private JournalListVO1 mListVO;
    private Activity mActivity;
    private TextView mAllTv, mAttentionTv, mAiteTv, mOkTv;
    private JournalListFragmentAll mFragmentAll;
    private JournalListFragmentAttention mFragmentAttention;
    private JournalListFragmentAiTeInfo mFragmentAiTeInfo;
    FragmentManager mFragmentManager;
    private LinearLayout mContentLayout;
    public Bundle bundle;
    private String[] mJournalType = {
            "日报", "周报", "月报"
    };
    private int mType = 1;// 1全部，2关注

    private PopupWindow mOkPop;
    private List<PopupWindow> mListpop = new ArrayList<PopupWindow>();

    public static JournalListFragment newInstance() {
        JournalListFragment collectFragment = new JournalListFragment();
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
        View view = inflater.inflate(R.layout.fragment_journal_list1, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        bindViewsListener();
     
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
    public boolean execute() {
        return true;
    }

    @Override
    public void executeSuccess() {

    }

    @Override
    public void executeFailure() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_tv:
                CommonUtils.hideSoftKeybord(mActivity);
                showFragment(v.getId());
                mType = 1;
                mOkTv.setText("发布");
                break;
            case R.id.attention_tv:
                showFragment(v.getId());
                mType = 2;
                mOkTv.setText("编辑");
                break;

            case R.id.aite_tv:
                showFragment(v.getId());
                mType = 3;
                mOkTv.setText("");
                break;
            case R.id.ok_tv:
                if (mType == 1) {
                    CommonUtils.initPopViewBg(mActivity, mJournalType, mOkTv, mCallback, CommonUtils.dip2px(mActivity, 90));
                } else {
                    Intent intent = new Intent();
                    intent.setClass(mActivity, HeadAttentionActivity.class);
                    mActivity.startActivityForResult(intent, EDIT_ATTENTION);
                }

                break;
            default:
                break;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;

        switch (resultCode) {
            case EDIT_ATTENTION:
                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                transaction.remove(mFragmentAttention);
                mFragmentAttention = new JournalListFragmentAttention();
                transaction.add(R.id.fragment_content_layout, mFragmentAttention);
                transaction.show(mFragmentAttention);
                transaction.commitAllowingStateLoss();
                break;
            case JOURNAL_PUBLISH:

                if (mFragmentAll != null && mFragmentAll.isVisible())
                    mFragmentAll.onActivityResult(requestCode, resultCode, data);
                break;

            case JOURANL_ALL_AND_ATTENTION:
                if (mFragmentAll != null && mFragmentAll.isVisible())
                    mFragmentAll.onActivityResult(requestCode, resultCode, data);
                if (mFragmentAttention != null && mFragmentAttention.isVisible())
                    mFragmentAttention.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    // 右上角添加回调函数
    ResultCallback mCallback = new ResultCallback() {
        @Override
        public void callback(String str, int position) {
            Intent intent = new Intent();
            intent.putExtra("type", position);
            intent.setClass(mActivity, JournalPublishActivity.class);
            startActivityForResult(intent, JOURNAL_PUBLISH);
        }
    };

    public void showFragment(int id) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (id) {
            case R.id.all_tv:
                mAllTv.setTextColor(getResources().getColor(R.color.bule_go));
                mAttentionTv.setTextColor(getResources().getColor(R.color.white));
                mAiteTv.setTextColor(getResources().getColor(R.color.white));
                mAllTv.setBackgroundDrawable(getResources().getDrawable(R.drawable.corners_tab_left_select1));
                mAttentionTv.setBackgroundDrawable(getResources().getDrawable(R.drawable.corners_tab_middle_normal));
                mAiteTv.setBackgroundDrawable(getResources().getDrawable(R.drawable.corners_tab_right_normal1));
                if (null == mFragmentAll) {
                    mFragmentAll = new JournalListFragmentAll();
                    transaction.add(R.id.fragment_content_layout, mFragmentAll);
                }
                transaction.show(mFragmentAll);
                break;
            case R.id.attention_tv:
                mAttentionTv.setTextColor(getResources().getColor(R.color.bule_go));
                mAllTv.setTextColor(getResources().getColor(R.color.white));
                mAiteTv.setTextColor(getResources().getColor(R.color.white));
                mAttentionTv.setBackgroundDrawable(getResources().getDrawable(R.drawable.corners_tab_middle_select2));
                mAllTv.setBackgroundDrawable(getResources().getDrawable(R.drawable.corners_tab_left_normal1));
                mAiteTv.setBackgroundDrawable(getResources().getDrawable(R.drawable.corners_tab_right_normal1));
                if (null == mFragmentAttention) {
                    mFragmentAttention = new JournalListFragmentAttention();
                    transaction.add(R.id.fragment_content_layout, mFragmentAttention);
                }

                transaction.show(mFragmentAttention);

                break;

            case R.id.aite_tv:
                mAttentionTv.setTextColor(getResources().getColor(R.color.white));
                mAllTv.setTextColor(getResources().getColor(R.color.white));
                mAiteTv.setTextColor(getResources().getColor(R.color.bule_go));
                mAttentionTv.setBackgroundDrawable(getResources().getDrawable(R.drawable.corners_tab_middle_normal));
                mAllTv.setBackgroundDrawable(getResources().getDrawable(R.drawable.corners_tab_left_normal1));
                mAiteTv.setBackgroundDrawable(getResources().getDrawable(R.drawable.corners_tab_right_select2));
                if (null == mFragmentAiTeInfo) {
                    mFragmentAiTeInfo = new JournalListFragmentAiTeInfo();
                    transaction.add(R.id.fragment_content_layout, mFragmentAiTeInfo);
                }
                transaction.show(mFragmentAiTeInfo);

                break;

        }
        transaction.commitAllowingStateLoss();
    }

    public void initView(View view) {
        mAllTv = (TextView) view.findViewById(R.id.all_tv);
        mAttentionTv = (TextView) view.findViewById(R.id.attention_tv);
        mAiteTv = (TextView) view.findViewById(R.id.aite_tv);
        mOkTv = (TextView) view.findViewById(R.id.ok_tv);
        mContentLayout = (LinearLayout) view.findViewById(R.id.fragment_content_layout);
        FragmentActivity activity = (FragmentActivity) mActivity;
        mFragmentManager = activity.getSupportFragmentManager();
        showFragment(R.id.all_tv);
    }

    public void bindViewsListener() {
        mAllTv.setOnClickListener(this);
        mAttentionTv.setOnClickListener(this);
        mOkTv.setOnClickListener(this);
        mAiteTv.setOnClickListener(this);
    }

    public boolean getData() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> mapList = new HashMap<String, String>();
            mapList.put("userid", BSApplication.getInstance().getUserId());

            if (0 == mState) {
                mFristid = "";
                mLastid = "";
            }
            mapList.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.JOURNAL_LIST_NEW, mapList);
            mListVO = gson.fromJson(jsonStrList, JournalListVO1.class);
            if (Constant.RESULT_CODE.equals(mListVO.getCode())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
        }
    }

    @Override
    public String getFragmentName() {
        return null;
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (null != mFragmentAll) {
            transaction.hide(mFragmentAll);
        }
        if (null != mFragmentAttention) {
            transaction.hide(mFragmentAttention);
        }
        if (null != mFragmentAiTeInfo) {
            transaction.hide(mFragmentAiTeInfo);
        }
    }

 

}
