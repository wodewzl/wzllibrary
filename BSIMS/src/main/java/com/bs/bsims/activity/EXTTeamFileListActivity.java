
package com.bs.bsims.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.EXTSharedfilesdHomeMyUpdateAdapter;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.constant.Constant4Sharedfiles;
import com.bs.bsims.download.domain.DownloadFile;
import com.bs.bsims.model.FileDetailResultVO;
import com.bs.bsims.utils.BsFileMathUtils;
import com.bs.bsims.utils.CharacterParser;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomLog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.utils.UrlUtil;
import com.bs.bsims.view.BSIndexEditText;
import com.bs.bsims.view.BSRefreshListView;
import com.bs.bsims.view.BSRefreshListView.OnRefreshListener;
import com.bs.bsims.xutils.impl.DownloadManager;
import com.google.gson.Gson;

import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author peck
 * @Description: 部门文档列表
 * @date 2015-6-12 下午6:41:09
 * @email 971371860@qq.com
 * @version V1.0
 */

public class EXTTeamFileListActivity extends BaseActivity {

    private String TAG = "EXTTeamFileListActivity";
    private Context mContext;

    private BSRefreshListView mTeamFileRListview;
    // private EXTTeamFileListAdapter mEXTTeamFileListAdapter;
    private List<DownloadFile> mFileSaveVoDonwload = new ArrayList<DownloadFile>();
    private ImageView imgSex;
    private FileDetailResultVO mFileDetailVO;
    private List<String> groupArray;// 组列表
    private EXTSharedfilesdHomeMyUpdateAdapter mEXTSharedfilesdHomeMyUpdateAdapter;
    // listView
    private View mFootLayout;
    private TextView mMoreTextView, mLoadings;
    private ProgressBar mProgressBar;
    private String mRefresh = "";
    // 0为首次,1为上拉刷新 ，2为下拉刷新
    private int mState = 0;

    private String mKeyword;
    private String type;

    private List<DownloadFile> mFileListVOs;
    private BSIndexEditText mClearEditText;
    private CharacterParser mCharacterParser;

    private PopupWindow mOkPop;
    private List<PopupWindow> mListpop = new ArrayList<PopupWindow>();

    @Override
    public void baseSetContentView() {
        // TODO Auto-generated method stub
        View layout = View.inflate(this, R.layout.ac_ext_teamfilelist, mContentLayout);
        mContext = this;
    }

    @Override
    public boolean getDataResult() {
        // TODO Auto-generated method stub
        return getData();
        // return true;
    }

    @Override
    public void updateUi() {
        // TODO Auto-generated method stub

    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        // mOkTv.setText(R.string.ac_extteamfilelist_right_update);
        Intent getIntent = getIntent();
        mTitleTv.setText(getIntent.getStringExtra("actyname"));
        mLoadings = (TextView) findViewById(R.id.loadingfile1);
        mTeamFileRListview = (BSRefreshListView) findViewById(R.id.bslistview_teamfilelist_approval);
        mEXTSharedfilesdHomeMyUpdateAdapter = new EXTSharedfilesdHomeMyUpdateAdapter(
                EXTTeamFileListActivity.this, "1", mTeamFileRListview);
        mTeamFileRListview
                .setAdapter(mEXTSharedfilesdHomeMyUpdateAdapter);
        mClearEditText = (BSIndexEditText) this.findViewById(R.id.edit_teamfilelist_single_search);
        // 根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mLoadings.setVisibility(View.GONE);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mTeamFileRListview.setVisibility(View.VISIBLE);
                filterData(s.toString());
            }
        });
        mCharacterParser = CharacterParser.getInstance();
        initFoot();

    }

    @Override
    public void bindViewsListener() {
        // TODO Auto-generated method stub
        bindRefreshListener();
    }

    public void clearData() {
        mEXTSharedfilesdHomeMyUpdateAdapter.mList.clear();
        mFootLayout.setVisibility(View.GONE);
    }

    public void bindRefreshListener() {
        mTeamFileRListview.setonRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                mState = 1;
                mRefresh = Constant.FIRSTID;
                if (mEXTSharedfilesdHomeMyUpdateAdapter.mList.size() == 0)
                    mRefresh = "";
                new ThreadUtil(mContext, EXTTeamFileListActivity.this).start();
            }
        });
        mFootLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mMoreTextView.setText("正在加载...");
                mProgressBar.setVisibility(View.VISIBLE);
                mState = 2;
                mRefresh = Constant.LASTID;
                new ThreadUtil(mContext, EXTTeamFileListActivity.this).start();
            }
        });

    }

    // 加载更多数据
    public void initFoot() {
        mFootLayout = LayoutInflater.from(mContext).inflate(R.layout.listview_bottom_more, null);
        mMoreTextView = (TextView) mFootLayout.findViewById(R.id.txt_loading);
        mMoreTextView.setText("更多");
        mProgressBar = (ProgressBar) mFootLayout.findViewById(R.id.progressBar);
        mFootLayout.setVisibility(View.GONE);
        mTeamFileRListview.addFooterView(mFootLayout);
    }

    protected void footViewIsVisibility(List<DownloadFile> datas) {
        if (mFileDetailVO == null) {
            return;
        }
        if (mFileDetailVO.getCount() == null) {
            return;
        }
        if (Integer.parseInt(mFileDetailVO.getCount()) <= 15) {
            mFootLayout.setVisibility(View.GONE);
            // listView.removeFooterView(mFootLayout);
        } else {
            mFootLayout.setVisibility(View.VISIBLE);
            mMoreTextView.setText("更多");
            mProgressBar.setVisibility(View.GONE);
        }

    }

    public boolean getData() {
        if (0 == mState) {
            return getData(mKeyword, "", "");
        } else if (1 == mState) {
            if (mEXTSharedfilesdHomeMyUpdateAdapter.mList.size() > 0) {
                String id = mEXTSharedfilesdHomeMyUpdateAdapter.mList.get(0).getSharedid();
                return getData(mKeyword, Constant.FIRSTID, id);
            } else {
                return getData("", "", "");
            }

        } else if (2 == mState) {
            String id = mEXTSharedfilesdHomeMyUpdateAdapter.mList.get(mEXTSharedfilesdHomeMyUpdateAdapter.mList.size() - 1).getSharedid();
            return getData(mKeyword, Constant.LASTID, id);
        }
        return false;
    }

    public boolean getData(String keyword, String refresh, String id) {
        Intent getIntent = getIntent();
        if (TextUtils.isEmpty(type)) {
            type = getIntent.getStringExtra("currentDid");
        }
        if (TextUtils.isEmpty(type)) {
            type = "-2";
        }

        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("type", type);
        paramsMap.put("keyword", keyword);
        // paramsMap.put("firstid", firstid);
        try {
            String urlStr = UrlUtil.getUrlByMap4Refresh(
                    Constant4Sharedfiles.TYPE_LIST_PATH, paramsMap, mRefresh, id);
            String jsonUrlStr = HttpClientUtil.get(urlStr, Constant.ENCODING).trim();
            Gson gson = new Gson();
            mFileDetailVO = gson.fromJson(jsonUrlStr, FileDetailResultVO.class);
            if (Constant.RESULT_CODE.equals(mFileDetailVO.getCode())) {
                if (Constant.FIRSTID.equals(refresh)) {
                    if (mFileDetailVO.getCount() != null) {
                        // mEXTTeamFileListAdapter.mList = mFileDetailVO.getArray();
                    }

                } else if (Constant.LASTID.equals(refresh)) {
                    // mEXTTeamFileListAdapter.mList.addAll(mFileDetailVO.getArray());
                } else {
                    // mEXTTeamFileListAdapter.mList = mFileDetailVO.getArray();
                    // mListVOs = mFileDetailVO.getArray();

                }
                mFileSaveVoDonwload.addAll(mFileDetailVO.getArray());
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
    public void executeSuccess() {
        // mHeadLayout.setVisibility(View.GONE);
        mLoadingLayout.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);
        mTeamFileRListview.setVisibility(View.VISIBLE);
        mLoadings.setVisibility(View.GONE);
        // mListView.onRefreshComplete();
        // mSsBossDepartmentDetailArray = mStatisticsBossDepartmentDetailVO
        // .getArray();
        //
        // mInList = mSsBossDepartmentDetailArray.getList();
        // mEXTAttendanceSsDepartmentDetailAdapter.updateData(mInList);
        // // footViewIsVisibility(mAdapter.mList);
        if (1 == mState) {
            mEXTSharedfilesdHomeMyUpdateAdapter.updateDataFrist(mFileDetailVO.getArray(), "1");
        } else if (2 == mState) {
            mEXTSharedfilesdHomeMyUpdateAdapter.updateDataLast(mFileDetailVO.getArray(), "1");
        } else {
            mEXTSharedfilesdHomeMyUpdateAdapter.updateData(mFileDetailVO.getArray(), "1");
        }
        mState = 0;
        mTeamFileRListview.setVisibility(View.VISIBLE);
        mTeamFileRListview.onRefreshComplete();
        if (mState != 1)
            footViewIsVisibility(mFileDetailVO.getArray());
        // initLeadView();
    }

    @Override
    public void executeFailure() {
        mLoadingLayout.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);
        // CustomLog.e("bb", mFileDetailVO.getCode());
        // CustomLog.e("bb", mFileDetailVO.getCount());
        // CustomLog.e("bb", mFileDetailVO.getRetinfo());
        // mListView.onRefreshComplete();
        if (mFileDetailVO != null) {
            if (mFileDetailVO.getArray() == null && mEXTSharedfilesdHomeMyUpdateAdapter.mList.size() == 0) {
                mTeamFileRListview.setVisibility(View.GONE);
                mLoadings.setVisibility(View.VISIBLE);
                CustomDialog.closeProgressDialog();
                return;
            }
        }
        else {
            mLoadings.setVisibility(View.VISIBLE);
            CommonUtils.setNonetIcon(mContext, mLoadings,this);
            CustomDialog.closeProgressDialog();

        }

        // mContentLayout.setVisibility(View.VISIBLE);
        // mInList = mSsBossDepartmentDetailArray.getList();
        // mEXTAttendanceSsDepartmentDetailAdapter.updateData(mInList);
        mTeamFileRListview.onRefreshComplete();
        if (mState != 1)
            if (mFileDetailVO != null) {
                footViewIsVisibility(mFileDetailVO.getArray());
            }
    }

    // 过滤搜索
    private void filterData(String filterStr) {
        List<DownloadFile> mFileListVOs = new ArrayList<DownloadFile>();
        if (TextUtils.isEmpty(filterStr)) {
            mFileListVOs = mFileSaveVoDonwload;
            if (mFileListVOs == null) {
                CustomToast.showShortToast(mContext, "没有查询此文档");
                return;
            }
            mEXTSharedfilesdHomeMyUpdateAdapter.updateData(mFileListVOs, "1");
            if (mState != 1 && null != mFileDetailVO)
                footViewIsVisibility(mFileDetailVO.getArray());

        }
        else {
            if (mEXTSharedfilesdHomeMyUpdateAdapter.mList.size() == 0 || mFileDetailVO == null) {
                return;
            }
            else {
                int countflage = 0;
                mFileListVOs.clear();
                for (DownloadFile personnelVO : mEXTSharedfilesdHomeMyUpdateAdapter.mList) {
                    String name = BsFileMathUtils.SetShowFileNames(personnelVO.getExtension(), personnelVO.getTitle(), personnelVO.getFilepath());
                    // if (name.indexOf(filterStr.toString()) != -1
                    // || mCharacterParser.getSelling(name).startsWith(filterStr.toString())) {
                    // mFileListVOs.add(personnelVO);
                    // }
                    if (name.contains(filterStr) || name.contains(filterStr.toUpperCase()) || name.contains(filterStr.toLowerCase())) {
                        mFileListVOs.add(personnelVO);
                        countflage = 1;
                    }

                }
                if (countflage == 0) {
                    mTeamFileRListview.setVisibility(View.GONE);
                    mLoadings.setVisibility(View.VISIBLE);
                }
                else {
                    mEXTSharedfilesdHomeMyUpdateAdapter.updateData(mFileListVOs, "1");
                    mTeamFileRListview.setRefreshable(false);
                    mFootLayout.setVisibility(View.GONE);
                }

            }

        }

        // 根据a-z进行排序
        // Collections.sort(filterDateList, mSortComparatorVO);

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        if (mEXTSharedfilesdHomeMyUpdateAdapter == null) {
            CustomLog.e("fd", "不走");
        }
        else {
            if (mEXTSharedfilesdHomeMyUpdateAdapter.downcount != 0 &&
                    mEXTSharedfilesdHomeMyUpdateAdapter.downcount > 0) {
                for (int i = 0; i < mEXTSharedfilesdHomeMyUpdateAdapter.downcount; i++) {
                    // mEXTSharedfilesdHomeMyUpdateAdapter.listhttphandler.get(i).cancel();
                    try {
                        DownloadManager.getInstance().removeDownload(i);
                    } catch (DbException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        super.onDestroy();
    }

    public void initLeadView() {
        LinearLayout.LayoutParams okTvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);// 定义文本显示组件
        okTvParams.rightMargin = CommonUtils.dip2px(this, 10);
        LinearLayout.LayoutParams okImgParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, CommonUtils.dip2px(this, 180));// 定义文本显示组件
        okImgParams.gravity = Gravity.RIGHT;
        View view = mEXTSharedfilesdHomeMyUpdateAdapter.getView(0, null, mTeamFileRListview);
        ImageView imageView = (ImageView) view.findViewById(R.id.item_sharedfilesd_grouphome_more_img);
        okImgParams.rightMargin = CommonUtils.dip2px(this, 25) + CommonUtils.getViewWidth(imageView) / 2;
        mOkPop = CommonUtils.leadPop(this, view, "亲，有需要了可以发布一下哦", okTvParams, okImgParams, 1);
        mListpop.add(mOkPop);
        CommonUtils.okLeadPop(this, mContentLayout, mListpop, 0);
    }

}
