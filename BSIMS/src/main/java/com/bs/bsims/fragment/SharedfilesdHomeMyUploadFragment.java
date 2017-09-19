
package com.bs.bsims.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.EXTSharedfilesdHomeMyUpdateAdapter;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.constant.Constant4Sharedfiles;
import com.bs.bsims.download.domain.DownloadFile;
import com.bs.bsims.interfaces.UpdateCallback;
import com.bs.bsims.model.SharedfilesdHomeMyUploadVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomLog;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.utils.UrlUtil;
import com.bs.bsims.view.BSRrefshViewDelete;
import com.bs.bsims.view.BSRrefshViewDelete.OnRefreshListener;
import com.bs.bsims.xutils.impl.DownloadManager;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author peck
 * @Description: 2. 文档列表页面接口 我上传的文档
 * @date 2015-7-4 下午4:24:05
 * @email 971371860@qq.com
 * @version V1.0
 */
public class SharedfilesdHomeMyUploadFragment extends BaseFragment implements
        UpdateCallback, OnClickListener {
    private static final String TAG = "SharedfilesdHomeMyUpdateFragment";
    private Activity mActivity;
    private BSRrefshViewDelete fragment_sharedfilesd_home_all_refreshlistview;
    private SharedfilesdHomeMyUploadVO mSharedfilesdHomeMyUploadVO;
    private EXTSharedfilesdHomeMyUpdateAdapter mEXTSharedfilesdHomeMyUpdateAdapter;
    private TextView mLoading;
    // listView
    private View mFootLayout;
    private TextView mMoreTextView;
    private ProgressBar mProgressBar;
    private String mRefresh = "";
    // 0为首次,1为上拉刷新 ，2为下拉刷新
    private int mState = 0;

    /** 类型（0:全部文档，-1:我收藏的文档，-2:我上传的文档，1-N：一级部门ID下的文档）（必传） */
    private String type;

    /** 关键词搜索（选传） */
    private String keyword;

    /** 上拉ID */
    private String firstid;

    /** 下拉ID */
    private String lastid;

    /** 记录最后一条的id */
    private String saveLastId;

    // type 类型（0:全部文档，-1:我收藏的文档，-2:我上传的文档，1-N：一级部门ID下的文档）（必传）
    // keyword 关键词搜索（选传）
    // firstid 上拉ID
    // lastid 下拉ID

    @Override
    public String getFragmentName() {
        // TODO Auto-generated method stub
        return TAG;
    }

    @Override
    public boolean execute() {
        // TODO Auto-generated method stub
        return getData();
    }

    @Override
    public void executeSuccess() {
        mLoading.setVisibility(View.GONE);
        saveLastId = mSharedfilesdHomeMyUploadVO.getArray().get(mSharedfilesdHomeMyUploadVO.getArray().size() - 1).getSharedid();
        // mEXTSharedfilesdHomeMyUpdateAdapter.updateData(mSharedfilesdHomeMyUploadVO.getArray());
        // mListView.onRefreshComplete();
        // // footViewIsVisibility(mAdapter.mList);
        if (1 == mState) {
            mEXTSharedfilesdHomeMyUpdateAdapter.updateDataFrist(mSharedfilesdHomeMyUploadVO.getArray(), "-2");
        } else if (2 == mState) {
            mEXTSharedfilesdHomeMyUpdateAdapter.updateDataLast(mSharedfilesdHomeMyUploadVO.getArray(), "-2");
        } else {
            mEXTSharedfilesdHomeMyUpdateAdapter.updateData(mSharedfilesdHomeMyUploadVO.getArray(), "-2");
        }
        fragment_sharedfilesd_home_all_refreshlistview.setVisibility(View.VISIBLE);
        fragment_sharedfilesd_home_all_refreshlistview.onRefreshComplete();
        if (mState != 1)
            footViewIsVisibility(mSharedfilesdHomeMyUploadVO.getArray());
        mState = 0;
        CustomDialog.closeProgressDialog();
        // mLoadingLayout.setVisibility(View.GONE);
    }

    @Override
    public void executeFailure() {
        // mTextView.setText("加载失败");
        if (mSharedfilesdHomeMyUploadVO != null) {
            // mLoading.setText(mSharedfilesdHomeMyUploadVO.getRetinfo());
            // CustomToast.showShortToast(mActivity, mSharedfilesdHomeMyUploadVO.getRetinfo());
            CustomLog.e("mActivity", mSharedfilesdHomeMyUploadVO.getRetinfo());
        } else {
            CommonUtils.setNonetIcon(mActivity, mLoading, this);
            CustomDialog.closeProgressDialog();
            return;
        }
        if (mSharedfilesdHomeMyUploadVO.getArray() == null && mEXTSharedfilesdHomeMyUpdateAdapter.mList.size() == 0) {
            fragment_sharedfilesd_home_all_refreshlistview.setVisibility(View.GONE);
            mLoading.setVisibility(View.VISIBLE);
            CustomDialog.closeProgressDialog();
            return;
        }

        fragment_sharedfilesd_home_all_refreshlistview.onRefreshComplete();
        if (mState != 1)
            footViewIsVisibility(mSharedfilesdHomeMyUploadVO.getArray());
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
        View view = inflater.inflate(R.layout.fragment_sharedfilesd_home_all, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        bindViewsListener();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // initData();
        CustomLog.e("shafile2", "列表更新了1111");
        CustomDialog.showProgressDialog(mActivity);
        new ThreadUtil(mActivity, this).start();
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub

    }

    public void bindViewsListener() {
        // mJournalListView.setOnItemClickListener(this);
        // mDepartTitleLayout.setOnClickListener(this);
        // mPostTitleLayout.setOnClickListener(this);
        // mMoreLayout.setOnClickListener(this);
        // mOkBt.setOnClickListener(this);
        bindRefreshListener();
    }

    // 加载更多数据
    public void initFoot() {
        mFootLayout = LayoutInflater.from(mActivity).inflate(R.layout.listview_bottom_more, null);
        mMoreTextView = (TextView) mFootLayout.findViewById(R.id.txt_loading);
        mMoreTextView.setText("更多");
        mProgressBar = (ProgressBar) mFootLayout.findViewById(R.id.progressBar);
        mFootLayout.setVisibility(View.GONE);
        fragment_sharedfilesd_home_all_refreshlistview.addFooterView(mFootLayout);
    }

    protected void footViewIsVisibility(List<DownloadFile> datas) {
        if (mSharedfilesdHomeMyUploadVO == null) {
            return;
        }
        if (mSharedfilesdHomeMyUploadVO.getCount() == null) {
            return;
        }
        if (Integer.parseInt(mSharedfilesdHomeMyUploadVO.getCount()) <= 15) {
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
        fragment_sharedfilesd_home_all_refreshlistview = (BSRrefshViewDelete) view
                .findViewById(R.id.fragment_sharedfilesd_home_all_refreshlistview);
        mEXTSharedfilesdHomeMyUpdateAdapter = new EXTSharedfilesdHomeMyUpdateAdapter(
                mActivity, "-1", fragment_sharedfilesd_home_all_refreshlistview);
        fragment_sharedfilesd_home_all_refreshlistview
                .setAdapter(mEXTSharedfilesdHomeMyUpdateAdapter);
        mLoading = (TextView) view.findViewById(R.id.loadingfile1);
        mLoading.setText("您还没有上传文档哦~");
        mLoading.setVisibility(View.GONE);
        initFoot();
    }

    public void bindRefreshListener() {
        fragment_sharedfilesd_home_all_refreshlistview.setonRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                mState = 1;
                mRefresh = Constant.FIRSTID;
                if (mEXTSharedfilesdHomeMyUpdateAdapter.mList.size() == 0)
                    mRefresh = "";
                new ThreadUtil(mActivity, SharedfilesdHomeMyUploadFragment.this).start();
            }
        });
        mFootLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mMoreTextView.setText("正在加载...");
                mProgressBar.setVisibility(View.VISIBLE);
                mState = 2;
                mRefresh = Constant.LASTID;
                new ThreadUtil(mActivity, SharedfilesdHomeMyUploadFragment.this).start();
            }
        });

    }

    public boolean getData() {
        Gson gson = new Gson();
        // http://cp.beisheng.wang/api.php/Sharedfiles/getDepList/ftoken/RBDTZXGUMNDKkEwMkZFN0UyMTA1RgO0O0OO0O0O/
        // userid 用户ID（必传）
        // type 类型（0:全部文档，-1:我收藏的文档，-2:我上传的文档，1-N：一级部门ID下的文档）（必传）
        // keyword 关键词搜索（选传）
        // firstid 上拉ID
        // lastid 下拉ID

        if (TextUtils.isEmpty(type)) {
            type = "1";
            type = "-2";
        }
        Map<String, String> paramsMap = null;
        if (mState == 0) {
            paramsMap = new HashMap<String, String>();
            paramsMap.put("type", type);
            // paramsMap.put("keyword", keyword);
        }
        else if (mState == 1) {
            if (mRefresh.equals("")) {
                paramsMap = new HashMap<String, String>();
                paramsMap.put("type", type);
            }
            else {
                firstid = mEXTSharedfilesdHomeMyUpdateAdapter.mList.get(0).getSharedid();
                paramsMap = new HashMap<String, String>();
                paramsMap.put("type", type);
                // paramsMap.put("keyword", keyword);
                paramsMap.put("firstid", firstid);
            }

        }
        else {
            lastid = saveLastId;// mEXTSharedfilesdHomeMyUpdateAdapter.mList.get(mEXTSharedfilesdHomeMyUpdateAdapter.mList.size()
                                // - 1).getSharedid();
            paramsMap = new HashMap<String, String>();
            paramsMap.put("type", type);
            // paramsMap.put("keyword", keyword);
            paramsMap.put("lastid", lastid);
        }

        try {
            String urlStr = UrlUtil.getUrlByMap(
                    Constant4Sharedfiles.TYPE_LIST_PATH, paramsMap);
            String jsonUrlStr = HttpClientUtil.get(urlStr, Constant.ENCODING).trim();
            CustomLog.e("download2", urlStr);
            mSharedfilesdHomeMyUploadVO = gson.fromJson(jsonUrlStr, SharedfilesdHomeMyUploadVO.class);
            if (null != mSharedfilesdHomeMyUploadVO) {
                if (Constant.RESULT_CODE.equals(mSharedfilesdHomeMyUploadVO
                        .getCode())) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if (!hidden) {
            CustomDialog.showProgressDialog(mActivity);
            new ThreadUtil(mActivity, SharedfilesdHomeMyUploadFragment.this).start();
        } else {
            // 如果有下载 把下载的线程结束掉
            if (mEXTSharedfilesdHomeMyUpdateAdapter == null) {
                CustomLog.e("fd", "不走");
            }
            else {
                try {
                    if (mEXTSharedfilesdHomeMyUpdateAdapter.downcount != 0 &&
                            mEXTSharedfilesdHomeMyUpdateAdapter.downcount > 0) {
                        for (int i = 0; i < mEXTSharedfilesdHomeMyUpdateAdapter.downcount; i++) {
                            // mEXTSharedfilesdHomeMyUpdateAdapter.listhttphandler.get(i).cancel();

                            DownloadManager.getInstance().removeDownload(i);
                        }
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

}
