
package com.wuzhanglong.library.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wuzhanglong.library.R;
import com.wuzhanglong.library.activity.BaseActivity;
import com.wuzhanglong.library.interfaces.UpdateCallback;
import com.wuzhanglong.library.mode.BaseVO;
import com.wuzhanglong.library.utils.ThreadUtil;

import java.io.Serializable;


public abstract class BaseFragment extends Fragment implements UpdateCallback, Serializable {
    public abstract void setContentView();

    public abstract void initView(View view);

    public abstract void bindViewsListener();

    public abstract void getData();

    public abstract void hasData(BaseVO vo);

    public abstract void noData(BaseVO vo);

    public abstract void noNet();


    public BaseActivity mActivity;
    private boolean add = false;
    public LinearLayout mBaseContentLayout;
    public TextView mNoContentTv, mNoNetTv;
    protected View mContentView;
    public ThreadUtil mThreadUtil;
    private LayoutInflater mInflater;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = (BaseActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

//        View view = inflater.inflate(R.layout.base_fragment, container, false);
//        return view;

        // 避免多次从xml中加载布局文件
//        if (mContentView == null) {
//            mContentView = LayoutInflater.from(mActivity).inflate(R.layout.base_fragment, null);
//            baseInitView(mContentView);
//            baseBindViewsListener();
//            setContentView();
//            initView(mContentView);
//            bindViewsListener();
//        } else {
//            ViewGroup parent = (ViewGroup) mContentView.getParent();
//            if (parent != null) {
//                parent.removeView(mContentView);
//            }
//        }
//        mContentView = LayoutInflater.from(mActivity).inflate(R.layout.base_fragment, false);
        mContentView = inflater.inflate(R.layout.base_fragment, container, false);
        return mContentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        baseInitView(view);
        baseBindViewsListener();
        setContentView();
        initView(view);
        bindViewsListener();
    }

    protected <VT extends View> VT getViewById(@IdRes int id) {
        return (VT) mContentView.findViewById(id);
    }

    public void contentInflateView(int id) {
        View.inflate(mActivity, id, mBaseContentLayout);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity.showProgressDialog();
        mThreadUtil = new ThreadUtil(mActivity, this);
        mThreadUtil.start();
    }

    public void baseInitView(View view) {
        mNoNetTv = (TextView) view.findViewById(R.id.no_net_tv);
        mNoContentTv = (TextView) view.findViewById(R.id.no_content_tv);
        mBaseContentLayout = (LinearLayout) view.findViewById(R.id.base_content_layout);
    }

    public void baseBindViewsListener() {
        mNoNetTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mThreadUtil = new ThreadUtil(mActivity, BaseFragment.this);
                mThreadUtil.start();
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd(getFragmentName());
    }

    @Override
    public void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart(getFragmentName());
    }

    @Override
    public void execute() {
        getData();
    }

    public void baseHasData(BaseVO vo) {
//        mActivity.dismissProgressDialog();
        mBaseContentLayout.setVisibility(View.VISIBLE);
        mNoContentTv.setVisibility(View.GONE);
        mNoNetTv.setVisibility(View.GONE);
        hasData(vo);
    }

    public void baseNoData(BaseVO vo) {
//        mActivity.dismissProgressDialog();
        mBaseContentLayout.setVisibility(View.GONE);
        mNoContentTv.setVisibility(View.VISIBLE);
        mNoNetTv.setVisibility(View.GONE);
        noData(vo);
    }

    public void baseNoNet() {
        mActivity.dismissProgressDialog();
        mBaseContentLayout.setVisibility(View.GONE);
        mNoNetTv.setVisibility(View.VISIBLE);
        mNoContentTv.setVisibility(View.GONE);
        noNet();
    }

    @Override
    public void show() {
        showView();
    }

    public void showView() {
//        mActivity.dismissProgressDialog();
        mBaseContentLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void finishActivity(Activity activity, int code) {

    }

    public boolean isAdd() {
        return add;
    }

    public void setAdd(boolean add) {
        this.add = add;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

        } else {
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
        }
    }
}
