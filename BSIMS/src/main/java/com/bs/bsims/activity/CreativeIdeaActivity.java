
package com.bs.bsims.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.IdeaAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.IdeaResultVO;
import com.bs.bsims.model.IdeaVO;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.utils.UrlUtil;
import com.bs.bsims.view.BSRefreshListView;
import com.bs.bsims.view.BSRefreshListView.OnRefreshListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class CreativeIdeaActivity extends BaseActivity implements OnClickListener {
    private BSRefreshListView mListView;
    private IdeaAdapter mAdapter;
    private TextView mAllTv, mMyIdea, mMySuggest;

    private List<IdeaVO> mMyIdeaList;
    private List<IdeaVO> mMysuggestList;
    private List<IdeaVO> mAllList;
    private IdeaResultVO mIdeaResultVO;

    // listView
    private View mFootLayout;
    private TextView mMoreTextView;
    private ProgressBar mProgressBar;
    // 0为首次,1为上拉刷新 ，2为下拉刷新
    private int mState = 0;
    public static final String FIRSTID = "firstid";
    public static final String LASTID = "lastid";
    private boolean mMyIdeaFlag = true;
    private boolean mMySuggestFlag = true;
    private String mType = "0";
    private String mCount;
    private String mIsBoss;

    private int screenWidth;
    private int screenHeight;
    private ImageView manageChooser;// 选择动画
    private MANAGEUI UISTATE = MANAGEUI.UPDATE;
    private MANAGEUI LAST_STATE = MANAGEUI.UPDATE;
    private String INTENT_TYPE = "type";// 其他界面跳转的键名
    private int uiType = 3;// 其他界面跳转的键键

    private TextView manageInstallTv;// 选择可卸载界面
    private TextView manageUninstallTv;// 选择可安装界面
    private String mUnread;
    private String mToDo;

    /**
     * 管理界面的枚举
     */
    public enum MANAGEUI {
        INSTALLED, UNINSTALL, UPDATE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        initData();
        mTitleTv.setText(R.string.creative_idea);
        mOkTv.setText(R.string.add);
        mAllTv = (TextView) findViewById(R.id.all_tv);
        mMyIdea = (TextView) findViewById(R.id.my_idea);
        mMySuggest = (TextView) findViewById(R.id.my_suggest);
        mListView = (BSRefreshListView) findViewById(R.id.lv_crate_idea);
        mAdapter = new IdeaAdapter(this, mIsBoss);
        mListView.setAdapter(mAdapter);

        mMyIdeaList = new ArrayList<IdeaVO>();
        mMysuggestList = new ArrayList<IdeaVO>();
        mAllList = new ArrayList<IdeaVO>();
        initFoot();
        manageInstallTv = (TextView) findViewById(R.id.manageInstall);
    }

    public void initData() {
        mIsBoss = this.getIntent().getStringExtra("isboss");
        mUnread = this.getIntent().getStringExtra("unread");
        mToDo = this.getIntent().getStringExtra("todo");
    }

    public boolean getData() {

        if (0 == mState) {
            return getData(Constant.CREATIVE_IDEA, "", "", "", "");
        } else if (1 == mState) {
            if (mAdapter.mList.size() > 0) {
                String id = mAdapter.mList.get(0).getArticleid();
                return getData(Constant.CREATIVE_IDEA, FIRSTID, id, "", "");
            } else {
                return getData(Constant.CREATIVE_IDEA, "", "", "", "");
            }
        } else if (2 == mState) {
            String id = mAdapter.mList.get(mAdapter.mList.size() - 1).getArticleid();
            return getData(Constant.CREATIVE_IDEA, LASTID, id, "", "");
        }

        return false;
    }

    public boolean getData(String interfaceurl, String refresh, String id, String myuid, String type) {
        Gson gson = new Gson();
        try {

            if ("0".equals(mType)) {
                String urlStr = UrlUtil.getIdeaUrl(interfaceurl, refresh, id, "", "", "0", "1", "mUnread", mToDo);
                String jsonUrlStr = HttpClientUtil.get(urlStr, Constant.ENCODING).trim();
                mIdeaResultVO = gson.fromJson(jsonUrlStr, IdeaResultVO.class);
                if (Constant.RESULT_CODE.equals(mIdeaResultVO.getCode())) {
                    if (FIRSTID.equals(refresh)) {
                        if (mIdeaResultVO.getCount() != null) {
                            mAllList = mIdeaResultVO.getArray();
                        }

                    } else if (LASTID.equals(refresh)) {
                        mAllList.addAll(mIdeaResultVO.getArray());
                    } else {
                        mAllList = mIdeaResultVO.getArray();
                    }
                    return true;
                } else {
                    return false;
                }

            } else if ("1".equals(mType)) {
                // 创意
                String myIdeaUrl;
                if ("0".equals(mIsBoss)) {
                    myIdeaUrl = UrlUtil.getIdeaUrl(interfaceurl, refresh, id, BSApplication.getInstance().getUserId(), "1", "0", "1", "mUnread", mToDo);
                } else {
                    myIdeaUrl = UrlUtil.getIdeaUrl(interfaceurl, refresh, id, "", "1", "0", "1", "mUnread", mToDo);
                }
                String jsonMyidea = HttpClientUtil.get(myIdeaUrl, Constant.ENCODING).trim();
                mIdeaResultVO = gson.fromJson(jsonMyidea, IdeaResultVO.class);
                if (Constant.RESULT_CODE.equals(mIdeaResultVO.getCode())) {

                    String userid = BSApplication.getInstance().getUserId();
                    if (mIdeaResultVO.getArray() != null) {
                        if (FIRSTID.equals(refresh)) {
                            if (mIdeaResultVO.getCount() != null) {
                                mMyIdeaList = mIdeaResultVO.getArray();
                            }

                        } else if (LASTID.equals(refresh)) {
                            mMyIdeaList.addAll(mIdeaResultVO.getArray());
                        } else {
                            mMyIdeaList = mIdeaResultVO.getArray();
                        }

                    }
                    return true;
                } else {
                    return false;
                }
            } else {
                // 建议
                String mySuggestUrl;
                if ("0".equals(mIsBoss)) {
                    mySuggestUrl = UrlUtil.getIdeaUrl(interfaceurl, refresh, id, BSApplication.getInstance().getUserId(), "2", "0", "1", "mUnread", mToDo);
                } else {
                    mySuggestUrl = UrlUtil.getIdeaUrl(interfaceurl, refresh, id, "", "2", "0", "1", "mUnread", mToDo);
                }
                String jsonMySuggest = HttpClientUtil.get(mySuggestUrl, Constant.ENCODING).trim();
                mIdeaResultVO = gson.fromJson(jsonMySuggest, IdeaResultVO.class);
                if (Constant.RESULT_CODE.equals(mIdeaResultVO.getCode())) {
                    if (mIdeaResultVO.getArray() != null) {

                        if (FIRSTID.equals(refresh)) {
                            if (mIdeaResultVO.getCount() != null) {
                                mMysuggestList = mIdeaResultVO.getArray();
                            }

                        } else if (LASTID.equals(refresh)) {
                            mMysuggestList.addAll(mIdeaResultVO.getArray());
                        } else {
                            mMysuggestList = mIdeaResultVO.getArray();
                        }
                    }
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public void bindViewsListener() {
        mAllTv.setOnClickListener(this);
        mMyIdea.setOnClickListener(this);
        mMySuggest.setOnClickListener(this);
        mOkTv.setOnClickListener(this);
        // mListView.setOnItemClickListener(this);

        mListView.setonRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                mState = 1;
                new ThreadUtil(CreativeIdeaActivity.this, CreativeIdeaActivity.this).start();
            }
        });
        mListView.setRefreshable(false);
        mFootLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mMoreTextView.setText("正在加载...");
                mProgressBar.setVisibility(View.VISIBLE);
                mState = 2;
                new ThreadUtil(CreativeIdeaActivity.this, CreativeIdeaActivity.this).start();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.all_tv:
                mAllTv.setBackgroundResource(R.drawable.corners_tab_left_select);
                mAllTv.setTextColor(this.getResources().getColor(R.color.white));

                mMyIdea.setBackgroundResource(R.drawable.corners_tab_middle_normal);
                mMyIdea.setTextColor(this.getResources().getColor(R.color.blug_bg));
                mMySuggest.setBackgroundResource(R.drawable.corners_tab_right_normal);
                mMySuggest.setTextColor(this.getResources().getColor(R.color.blug_bg));

                mListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
                new ThreadUtil(this, this).start();
                mMyIdeaFlag = false;

                mAdapter.updateData(mAllList);
                mFootLayout.setVisibility(View.GONE);
                mType = "0";
                mOkTv.setVisibility(View.VISIBLE);
                break;
            case R.id.my_idea:
                mFootLayout.setVisibility(View.GONE);
                mMyIdea.setBackgroundResource(R.drawable.corners_tab_middle_select);
                mMyIdea.setTextColor(this.getResources().getColor(R.color.white));

                mAllTv.setBackgroundResource(R.drawable.corners_tab_left_normal);
                mAllTv.setTextColor(this.getResources().getColor(R.color.blug_bg));
                mMySuggest.setBackgroundResource(R.drawable.corners_tab_right_normal);
                mMySuggest.setTextColor(this.getResources().getColor(R.color.blug_bg));
                mListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
                new ThreadUtil(this, this).start();
                mMyIdeaFlag = false;
                mAdapter.updateData(mMyIdeaList);

                mType = "1";
                break;
            case R.id.my_suggest:
                mFootLayout.setVisibility(View.GONE);
                mMySuggest.setBackgroundResource(R.drawable.corners_tab_right_select);
                mMySuggest.setTextColor(this.getResources().getColor(R.color.white));

                mMyIdea.setBackgroundResource(R.drawable.corners_tab_middle_normal);
                mMyIdea.setTextColor(this.getResources().getColor(R.color.blug_bg));
                mAllTv.setBackgroundResource(R.drawable.corners_tab_left_normal);
                mAllTv.setTextColor(this.getResources().getColor(R.color.blug_bg));
                mListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
                new ThreadUtil(this, this).start();
                mMyIdeaFlag = false;
                mAdapter.updateData(mMysuggestList);

                mType = "2";
                break;

            case R.id.txt_comm_head_right:
                Intent intent = new Intent();
                intent.putExtra("type", mType);
                intent.setClass(this, CreativeIdeaNewActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    @Override
    public void executeSuccess() {
        if ("0".equals(mType)) {
            mAdapter.updateData(mAllList);
        } else if ("1".equals(mType)) {
            mAdapter.updateData(mMyIdeaList);
        } else {
            mAdapter.updateData(mMysuggestList);
        }

        mListView.onRefreshComplete();
        mLoading.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);

        if (mState != 1) {
            footViewIsVisibility(mIdeaResultVO.getArray());
        }
    }

    @Override
    public void executeFailure() {
        mAdapter.notifyDataSetChanged();
        mListView.onRefreshComplete();
        footViewIsVisibility(mAdapter.mList);
        if (mIdeaResultVO != null) {
            mLoading.setText(mIdeaResultVO.getRetinfo());
        } else {
            mLoading.setText("网络不佳,点击图标重新加载哦~");
        }

    }

    @Override
    public void updateUi() {

    }

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.creative_idea, null);
        mContentLayout.addView(layout);
    }

    // 加载更多数据
    public void initFoot() {
        mFootLayout = LayoutInflater.from(this).inflate(R.layout.listview_bottom_more, null);
        mMoreTextView = (TextView) mFootLayout.findViewById(R.id.txt_loading);
        mMoreTextView.setText("更多");
        mProgressBar = (ProgressBar) mFootLayout.findViewById(R.id.progressBar);
        mFootLayout.setVisibility(View.GONE);
        mListView.addFooterView(mFootLayout);
    }

    /**
     * 加载更多是否隐藏
     * 
     * @param datas
     */
    protected void footViewIsVisibility(List<IdeaVO> datas) {
        if (mIdeaResultVO == null || mIdeaResultVO.getCount() == null) {
            return;
        }
        if (Integer.parseInt(mIdeaResultVO.getCount()) <= 15) {
            mFootLayout.setVisibility(View.GONE);
            // mListView.removeFooterView(mFootLayout);
        } else {
            mFootLayout.setVisibility(View.VISIBLE);
            mMoreTextView.setText("更多");
            mProgressBar.setVisibility(View.GONE);
        }
    }

}
