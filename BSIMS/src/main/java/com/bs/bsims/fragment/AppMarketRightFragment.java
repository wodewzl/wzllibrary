/**
 * 
 */

package com.bs.bsims.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import com.bs.bsims.R;
import com.bs.bsims.adapter.AppMarketAdapter;
import com.bs.bsims.interfaces.UpdateCallback;
import com.bs.bsims.model.AppMakertModel;
import com.bs.bsims.observer.AppLeftFragmentImp;
import com.bs.bsims.observer.BSApplictionObserver.Watcher;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.view.BSRefreshListView;

/**
 * BS北盛最帅程序员 Copyright (c) 2016 湖北北盛科技有限公司
 * 
 * @author 梁骚侠
 * @date 2016-8-30
 * @version 2.0
 */
public class AppMarketRightFragment extends BaseFragment implements UpdateCallback, OnClickListener, Watcher {

    private static final String TAG = "AppMarketLeftFragment";

    private BSRefreshListView fragment_sharedfilesd_home_all_refreshlistview;
    private Activity mActivity;

    private AppMakertModel appMakertModel;
    private AppMarketAdapter mAppMarketAdapter;

    @Override
    public String getFragmentName() {
        return TAG;
    }

    @Override
    public boolean execute() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void executeSuccess() {

        CustomDialog.closeProgressDialog();
    }

    @Override
    public void executeFailure() {
        // mTextView.setText("加载失败");

        CustomDialog.closeProgressDialog();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.peck_allgroup_listview_refshnodelte, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub

    }

    public AppMakertModel getAppMakertModel() {
        return appMakertModel;
    }

    public void setAppMakertModel(AppMakertModel appMakertModel) {
        this.appMakertModel = appMakertModel;
    }

    private void initViews(View view) {
        // TODO Auto-generated method stub
        AppLeftFragmentImp.getInstance().add(this);
        fragment_sharedfilesd_home_all_refreshlistview = (BSRefreshListView) view
                .findViewById(R.id.fragment_sharedfilesd_home_all_refreshlistview);
        mAppMarketAdapter = new AppMarketAdapter(mActivity);
        mAppMarketAdapter.setIsRight("1");
        fragment_sharedfilesd_home_all_refreshlistview.setAdapter(mAppMarketAdapter);
    }

    @Override
    public void updateNotify(Object content) {

        if (content != null) {
            mAppMarketAdapter.updateData(((AppMakertModel) content).getArray());
            mAppMarketAdapter.notifyDataSetChanged();
        }
    }

}
