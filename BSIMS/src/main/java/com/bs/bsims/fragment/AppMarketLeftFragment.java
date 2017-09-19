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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.AppMarketAdapter;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.interfaces.UpdateCallback;
import com.bs.bsims.model.AppMakertModel;
import com.bs.bsims.observer.AppLeftFragmentImp;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.utils.UrlUtil;
import com.bs.bsims.view.BSRefreshListView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BS北盛最帅程序员 Copyright (c) 2016 湖北北盛科技有限公司
 * 
 * @author 梁骚侠
 * @date 2016-8-30
 * @version 2.0
 */
public class AppMarketLeftFragment extends BaseFragment implements UpdateCallback, OnClickListener {

    private static final String TAG = "AppMarketLeftFragment";

    private BSRefreshListView fragment_sharedfilesd_home_all_refreshlistview;
    private Activity mActivity;
    // 0为首次,1为上拉刷新 ，2为下拉刷新
    private int mState = 0;
    /** 关键词搜索（选传） */
    private String keyword;

    /** 上拉ID */
    private String firstid;

    /** 下拉ID */
    private String lastid;

    /** 记录最后一条的id */
    private String saveLastId;

    // listView
    private View mFootLayout;
    private TextView mMoreTextView;
    private ProgressBar mProgressBar;
    private String mRefresh = "";
    private TextView mLoading;

    // type 类型（0:全部文档，-1:我收藏的文档，-2:我上传的文档，1-N：一级部门ID下的文档）（必传）
    // keyword 关键词搜索（选传）
    // firstid 上拉ID
    // lastid 下拉ID

    private AppMakertModel mCrmtranctsbussinesVo;
    private AppMarketAdapter mAppMarketAdapter;
    private TextView no_content_layout_content;
    private LinearLayout no_content_layout;

    @Override
    public String getFragmentName() {
        return TAG;
    }

    @Override
    public boolean execute() {
        // TODO Auto-generated method stub
        return getData();
    }

    @Override
    public void executeSuccess() {

        mAppMarketAdapter.updateData(mCrmtranctsbussinesVo.getArray());
        mAppMarketAdapter.notifyDataSetChanged();
        fragment_sharedfilesd_home_all_refreshlistview.onRefreshComplete();
        AppLeftFragmentImp.getInstance().notifyWatcher(getAppMakertModel());
    }

    @Override
    public void executeFailure() {
        // mTextView.setText("加载失败");
        if (null == mCrmtranctsbussinesVo && mAppMarketAdapter.mList.size() == 0) {
            fragment_sharedfilesd_home_all_refreshlistview.setVisibility(View.GONE);
            no_content_layout.setVisibility(View.VISIBLE);
            no_content_layout_content.setVisibility(View.VISIBLE);
            CommonUtils.setNonetIcon(mActivity, no_content_layout_content, this);
        }
        fragment_sharedfilesd_home_all_refreshlistview.onRefreshComplete();
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
        // initData();
        fragment_sharedfilesd_home_all_refreshlistview.changeHeaderViewByState(BSRefreshListView.REFRESHING);
        new ThreadUtil(mActivity, this).start();
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub

    }

    private void initViews(View view) {
        // TODO Auto-generated method stub
        fragment_sharedfilesd_home_all_refreshlistview = (BSRefreshListView) view
                .findViewById(R.id.fragment_sharedfilesd_home_all_refreshlistview);
        no_content_layout = (LinearLayout) view.findViewById(R.id.no_content_layout);
        no_content_layout_content = (TextView) view.findViewById(R.id.no_content_layout_content);
        mAppMarketAdapter = new AppMarketAdapter(mActivity);
        fragment_sharedfilesd_home_all_refreshlistview.setAdapter(mAppMarketAdapter);

    }

    public boolean getData() {
        Gson gson = new Gson();
        Map<String, String> map = new HashMap<String, String>();
        try {
            String urlStr;
            String jsonUrlStr;
            urlStr = UrlUtil.getUrlByMap1(Constant.APPLACTIONLIST_INDEX, map);
            jsonUrlStr = HttpClientUtil.get(urlStr, Constant.ENCODING).trim();
            mCrmtranctsbussinesVo = gson.fromJson(jsonUrlStr, AppMakertModel.class);
            if (mCrmtranctsbussinesVo.getCode().equals("200")) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    public AppMakertModel getAppMakertModel() {
        AppMakertModel appMakertModel = new AppMakertModel();
        List<AppMakertModel> modelList = new ArrayList<AppMakertModel>();
        if (mCrmtranctsbussinesVo != null) {
            modelList.addAll(mCrmtranctsbussinesVo.getArray());
            for (int i = 0; i < modelList.size(); i++) {
                if (modelList.get(i).getMopen().equals("0")) {
                    modelList.remove(i);
                }
            }
        }
        appMakertModel.setArray(modelList);
        return appMakertModel;
    }

}
