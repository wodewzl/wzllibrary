
package com.bs.bsims.appzone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.fragment.BaseFragment;
import com.bs.bsims.interfaces.UpdateCallback;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.utils.UrlUtil;
import com.bs.bsims.view.BSRefreshListView;
import com.bs.bsims.view.BSRefreshListView.OnRefreshListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressLint("ValidFragment")
// 商机详情
public class ALLZoneFragment extends BaseFragment implements
        OnClickListener, UpdateCallback {
    private static final String TAG = "ALLZoneFragment";
    public BSRefreshListView fragment_sharedfilesd_home_all_refreshlistview;
    private Activity activity;
    private View mFootLayout;
    private TextView mMoreTextView;
    private ProgressBar mProgressBar;
    private String mRefresh = "";
    // 0为首次,1为上拉刷新 ，2为下拉刷新
    private int mState = 0;

    /** 上拉ID */
    private String firstid;

    /** 下拉ID */
    private String lastid;
    /* 下拉控制多次点击 */
    private boolean lastPressS = true;

    /** 记录最后一条的id */
    private String saveLastId;

    private ALLZoneModel allZoneModel;

    private ALLZoneApdater mAllZoneAdater;

    private String type = "";

    public ALLZoneFragment(String uid) {
        this.type = uid;
    }

    public ALLZoneFragment() {
    }

    // DanganChuQingInfoLineChartActivity
    @Override
    public String getFragmentName() {
        // TODO Auto-generated method stub
        return TAG;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.crmbussines_fragment2_zone, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        bindListers();
    }

    private void bindListers() {
        // TODO Auto-generated method stub
        fragment_sharedfilesd_home_all_refreshlistview
                .setonRefreshListener(new OnRefreshListener() {

                    @Override
                    public void onRefresh() {
                        mState = 1;
                        mRefresh = Constant.FIRSTID;
                        if (mAllZoneAdater.mList.size() == 0)
                            mRefresh = "";
                        else
                            firstid = mAllZoneAdater.mList.get(0).getM_id();
                        new ThreadUtil(activity,
                                ALLZoneFragment.this).start();
                    }
                });

        mFootLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (lastPressS) {
                    lastPressS = false;
                    mMoreTextView.setText("正在加载...");
                    mProgressBar.setVisibility(View.VISIBLE);
                    mState = 2;
                    mRefresh = Constant.LASTID;
                    saveLastId = mAllZoneAdater.mList
                            .get(mAllZoneAdater.mList.size() - 1).getM_id();
                    new ThreadUtil(activity,
                            ALLZoneFragment.this).start();
                }
            }
        });

    }

    // 加载更多数据
    public void initFoot() {
        mFootLayout = LayoutInflater.from(activity).inflate(
                R.layout.listview_bottom_more, null);
        mMoreTextView = (TextView) mFootLayout.findViewById(R.id.txt_loading);
        mMoreTextView.setText("更多");
        mProgressBar = (ProgressBar) mFootLayout.findViewById(R.id.progressBar);
        mFootLayout.setVisibility(View.GONE);
        fragment_sharedfilesd_home_all_refreshlistview.addFooterView(mFootLayout);
    }

    protected void footViewIsVisibility() {
        if (allZoneModel == null) {
            return;
        }
        if (allZoneModel.getCount() == null) {
            return;
        }
        if (Integer.parseInt(allZoneModel.getCount()) <= 15) {
            mFootLayout.setVisibility(View.GONE);
            // listView.removeFooterView(mFootLayout);
        } else {
            mFootLayout.setVisibility(View.VISIBLE);
            mMoreTextView.setText("更多");
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private void initViews(View view) {
        // TODO Auto-generated method stub

        fragment_sharedfilesd_home_all_refreshlistview = (BSRefreshListView) view
                .findViewById(R.id.fragment_sharedfilesd_home_all_refreshlistview1);
        mAllZoneAdater = new ALLZoneApdater(activity);
        fragment_sharedfilesd_home_all_refreshlistview.setAdapter(mAllZoneAdater);
        initFoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // initData();
        // fragment_sharedfilesd_home_all_refreshlistview
        // .changeHeaderViewByState(BSRefreshListView.REFRESHING);
        new ThreadUtil(activity, this).start();
    }

    @Override
    public boolean execute() {
        // TODO Auto-generated method stub
        return getdata();
    }

    @Override
    public void executeSuccess() {
        // TODO Auto-generated method stub
        if (1 == mState) {
            mAllZoneAdater.updateDataFrist(allZoneModel.getList());
        } else if (2 == mState) {
            mAllZoneAdater.updateDataLast(allZoneModel.getList());
        } else {
            mAllZoneAdater.updateData(allZoneModel.getList());
            // mRecordAdapter.mList =mCrmClientHomeDetailVO.getArray();
        }
        if (mState != 1)
            footViewIsVisibility();
        lastPressS=true;
        mState = 0;
        fragment_sharedfilesd_home_all_refreshlistview.setVisibility(View.VISIBLE);
        fragment_sharedfilesd_home_all_refreshlistview.onRefreshComplete();
        mAllZoneAdater.notifyDataSetChanged();
    }

    @Override
    public void executeFailure() {
        // TODO Auto-generated method stub
        fragment_sharedfilesd_home_all_refreshlistview.onRefreshComplete();
        footViewIsVisibility();
        lastPressS=true;
        // 不适合只隐藏列表，适合隐藏整个布局;
        if (allZoneModel == null) {
            mAllZoneAdater.mList.clear();
            mAllZoneAdater.notifyDataSetChanged();
            CustomToast.showLongToast(activity, "网络错误");
        } else {
            if (mState == 0) {
                mAllZoneAdater.updateData(new ArrayList<ALLZoneModel>());
                mFootLayout.setVisibility(View.GONE);
            }
        }

        mState = 0;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

    public boolean getdata() {
        Gson gson = new Gson();
        Map<String, String> map;
        if (mState == 0) {
            map = new HashMap<String, String>();
        } else if (mState == 1) {
            if (mRefresh.equals("")) {
                map = new HashMap<String, String>();
            } else {
                // firstid = crmbussinesApdater.mList.get(0).getBid();
                map = new HashMap<String, String>();
                map.put("firstid", firstid);
            }

        } else {
            lastid = saveLastId;
            map = new HashMap<String, String>();
            map.put("lastid", lastid);
        }
        map.put("type", type);
        try {
            String urlStr;
            String jsonUrlStr;
            urlStr = UrlUtil.getUrlByMap1(
                    Constant.APP_ZONECONTENT, map);
            jsonUrlStr = HttpClientUtil.get(urlStr, Constant.ENCODING).trim();
            allZoneModel = gson.fromJson(jsonUrlStr, ALLZoneModel.class);
            if (allZoneModel.getCode().equals("200")) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

}
