/**
 * 
 */

package com.bs.bsims.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.LocusListAdaper;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.model.LocusLineVO;
import com.bs.bsims.model.TreeVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CommonUtils.ResultCallback;
import com.bs.bsims.utils.AnimationUtil;
import com.bs.bsims.utils.BsPermissionUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSIndexEditText;
import com.bs.bsims.view.BSPopupWindowsTitle;
import com.bs.bsims.view.BSPopupWindowsTitle.TreeCallBack;
import com.bs.bsims.view.BSRefreshListView;
import com.bs.bsims.view.BSRefreshListView.OnRefreshListener;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 轨迹列表界面
 * 
 * @author huliang
 */
public class LocusLineAcitvity extends BaseActivity implements OnClickListener, OnItemClickListener {

    private static final String TAG = "LocusLineAcitvity";
    private BSRefreshListView mRefreshListView;// 轨迹listView
    private LocusListAdaper mLocusListAdaper;
    private LocusLineVO mListVO;
    private List<LocusLineVO> mLocusList;
    private String mType;
    private int mState = 0; // 0为首次,1为上拉刷新 ，2为下拉刷新
    Context context = LocusLineAcitvity.this;
    private View mFootLayout;
    private TextView mMoreTextView;
    private ProgressBar mProgressBar;
    private BSPopupWindowsTitle mTpyePop, mDatePop, mTimeSelect;
    private String[] mTypeArray = {
            "全部", "位置签到", "移动打卡"
    };
    private boolean mCanClickFlag = true;

    private TextView mLoading, mSearbar,mTimeBar,mSearchBar;
    private RelativeLayout mTitleMenuLy;
    private TextView mAllType;// 全部类型
    private String mFristid, mLastid;
    private String mDate = "";
    private String mDtype = "";

    private BSIndexEditText mClearEditText;
    private String keyword="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View.inflate(this, R.layout.activity_locusline, mContentLayout);
        initWidget();
        bindRefreshListener();
        sarchList();
    }

    /**
     * 初始化控件
     */
    private void initWidget() {
        Log.i("TAG", "---initWidget");
        baseHeadLayout.setBackgroundColor(getResources().getColor(R.color.transparent));
        // title控件
        mHeadLayout.setVisibility(View.GONE);
        findViewById(R.id.img_head_backnew).setOnClickListener(this);
        mAllType = (TextView) findViewById(R.id.title_name_01);
        mTimeBar=(TextView) findViewById(R.id.title_name_02);
        mSearchBar=(TextView) findViewById(R.id.title_name_03);
        findViewById(R.id.img_comm_head_right).setOnClickListener(this);
        findViewById(R.id.img2_comm_head_right).setOnClickListener(this);
        mAllType.setWidth(CommonUtils.getScreenWidth(getApplicationContext())/4);
        mTimeBar.setWidth(CommonUtils.getScreenWidth(getApplicationContext())/4);
        mSearchBar.setWidth(CommonUtils.getScreenWidth(getApplicationContext())/5);
        mAllType.setOnClickListener(this);
        mTimeBar.setOnClickListener(this);
        mSearchBar.setOnClickListener(this);
        // 加载页面
        // mLoading = (TextView)findViewById(R.id.tv_locus_loading);
        // mLoadingLayout = (LinearLayout)findViewById(R.id.ll_locus_loading);
        // 列表
        mRefreshListView = (BSRefreshListView) findViewById(R.id.ls_locus_refresh);
        // mRefreshListView.setEmptyView(findViewById(R.id.ll_locus_loading));
        mLocusListAdaper = new LocusListAdaper(context);
        mRefreshListView.setAdapter(mLocusListAdaper);
        mRefreshListView.setOnItemClickListener(this);
        initFoot();
        initPopData();
        mTitleMenuLy = (RelativeLayout) findViewById(R.id.title_menu);
        mRefreshListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
        mTimeSelect = new BSPopupWindowsTitle((Activity) context, 2, LayoutParams.WRAP_CONTENT, timecallback,true);
        mClearEditText = (BSIndexEditText) findViewById(R.id.edit_single_search);
    }

    ResultCallback timecallback = new ResultCallback() {

        @Override
        public void callback(String str, int position) {
            // TODO Auto-generated method stub
            mDate = str;
            mRefreshListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
            mTimeBar.setText(str);
            new ThreadUtil(context, LocusLineAcitvity.this).start();
        }

    };

    /**
     * 点选菜单列表
     */
    public void initPopData() {
        ArrayList<TreeVO> listType = CommonUtils.getOneLeveTreeVoZero(mTypeArray);
        mTpyePop = new BSPopupWindowsTitle((Activity) context, listType, callback, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void match(int key, String value) {
        switch (key) {
            case 1:
                mFristid = value;
                mLastid = "";
                mState = 1;
                break;
            case 2:
                mLastid = value;
                mFristid = "";
                mState = 2;
                break;
            case 3:
                mType = value;
                break;
            // case 4:
            // mDid = value;
            // break;
            default:
                break;
        }
        mRefreshListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
        new ThreadUtil(context, this).start();
    }

    /**
     * 菜单点击回调函数
     */
    TreeCallBack callback = new TreeCallBack() {
        @Override
        public void callback(TreeVO vo) {
            match(3, vo.getSearchId());
            mAllType.setText(vo.getName());
        }
    };

    /**
     * 列表底部加载“更多”
     */
    public void initFoot() {
        mFootLayout = LayoutInflater.from(context).inflate(R.layout.listview_bottom_more, null);
        mMoreTextView = (TextView) mFootLayout.findViewById(R.id.txt_loading);
        mMoreTextView.setText("更多");
        mProgressBar = (ProgressBar) mFootLayout.findViewById(R.id.progressBar);
        mFootLayout.setVisibility(View.GONE);
        mRefreshListView.addFooterView(mFootLayout);
    }

    /**
     * 列表更多显示与否
     */
    public void footViewIsVisibility() {
        if (mListVO == null || mListVO.getCount() == null) {
            return;
        }
        if (Integer.parseInt(mListVO.getCount()) <= 15) {
            mFootLayout.setVisibility(View.GONE);
        } else {
            mFootLayout.setVisibility(View.VISIBLE);
            mMoreTextView.setText("更多");
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void executeSuccess() {
        super.executeSuccess();
        baseHeadLayout.setBackgroundColor(getResources().getColor(R.color.title_father));
        Log.i("TAG", "---executeSuccess");
        mRefreshListView.setVisibility(View.VISIBLE);
        mRefreshListView.onRefreshComplete();
        if (1 == mState) {
            mLocusListAdaper.updateDataFrist(mListVO.getArray());
        } else if (2 == mState) {
            mLocusListAdaper.updateDataLast(mListVO.getArray());
        } else {
            mLocusListAdaper.updateData(mListVO.getArray());
        }

        if ("".equals(mFristid) || mState != 1) {
            footViewIsVisibility();
        }
        mState = 0;
        mCanClickFlag = true;
        mDtype = "";
    }

    @Override
    public void executeFailure() {
        baseHeadLayout.setBackgroundColor(getResources().getColor(R.color.title_father));
        mLocusListAdaper.notifyDataSetChanged();
        mRefreshListView.onRefreshComplete();
        footViewIsVisibility();
        if (mListVO == null) {
            baseHeadLayout.setBackgroundColor(Color.parseColor("#eeeeee"));
            super.showNoNetView();
            return;
        } else {
            if (mState == 0) {
                mLoadingLayout.setVisibility(View.GONE);
                mContentLayout.setVisibility(View.VISIBLE);
                mLocusListAdaper.updateData(new ArrayList<LocusLineVO>());
                mFootLayout.setVisibility(View.GONE);
            }
        }
        mState = 0;
        mCanClickFlag = true;
        mDtype = "";
    }

    /**
     * 访问接口获取数据 /** 工作轨迹列表接口（上报位置和移动打卡） 例子：
     * http://cp.beisheng.wang/dev_1.2.3/api.php/Visit/workingTrack
     * /ftoken/RBDTZXGUMNDKkEwMkZFN0UyMTA1RgO0O0OO0O0O/userid/22/
     * 
     * @param string userid 用户id
     * @param string type 类型搜索（0-全部 1-上报位置 2-移动打卡）
     * @param string date 日期筛选（yyyy-mm-dd）
     * @param string keyword 人员姓名关键词搜索
     * @param string firstid 下拉刷新
     * @param string lastid 上拉翻页
     * @param string dtype *上拉分页和下拉刷新时需传入firstid或者lastid那条所对应的type
     * @return type description
     * @access public or private
     * @static makes the class property accessible without needing an instantiation of the class
     * @return
     */
    public boolean getData() {
        Log.i("TAG", "---getData");
        mLocusList = new ArrayList<LocusLineVO>();
        try {
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            // map.put("userid", BSApplication.getInstance().getUserId());
            map.put("userid", BSApplication.getInstance().getUserId());
            if (0 == mState) {
                mFristid = "";
                mLastid = "";
            }
            map.put("type", "1");
            map.put("date", mDate);
            map.put("keyword",keyword);
            // map.put("ismyfavor", mIsMyFavor);
            map.put("firstid", mFristid);
            map.put("lastid", mLastid);
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            String jsonStrList = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.LOCUS_WORKING_TRACK_LIST, map);
            mListVO = gson.fromJson(jsonStrList, LocusLineVO.class);
            if (Constant.RESULT_CODE.equals(mListVO.getCode())) {
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
    public void onItemClick(AdapterView<?> parentView, View arg1, int position, long arg3) {
        LocusLineVO vo = (LocusLineVO) parentView.getAdapter().getItem(position);
        Intent intent = new Intent();
        intent.putExtra("vo", (Serializable) vo);
        intent.setClass(context, LocusPersonActivity.class);
        startActivity(intent);
    }

    /**
     * 刷新监听
     */
    public void bindRefreshListener() {
        mRefreshListView.setonRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mLocusListAdaper.mList.size() > 0) {
                    mDtype = mLocusListAdaper.mList.get(0).getType();
                    match(1, mLocusListAdaper.mList.get(0).getCsid());
                } else {
                    mFristid = "";
                    match(1, "");
                }
            }
        });
        mFootLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCanClickFlag) {
                    mCanClickFlag = false;
                    mMoreTextView.setText("正在加载...");
                    mProgressBar.setVisibility(View.VISIBLE);
                    mDtype = mLocusListAdaper.mList.get(mLocusListAdaper.mList.size() - 1).getType();
                    match(2, mLocusListAdaper.mList.get(mLocusListAdaper.mList.size() - 1).getCsid());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent();
        switch (v.getId()) {
        // 返回
            case R.id.img_head_backnew:
                LocusLineAcitvity.this.finish();
                break;

            // 位置
            case R.id.img_comm_head_right:
                i.setClass(context, GaoDeMapLoactionIndexActivity.class);
                startActivity(i);
                break;
            // 加入
            case R.id.img2_comm_head_right:
                if(!BsPermissionUtils.isOPenGps(this)){
                    BsPermissionUtils.openGPS(this);
                }
                else{
                    i.setClass(context, CrmVisitorSignAddInfoActivity.class);
                    // startActivity(i);
                    startActivityForResult(i, 2016);
                }
                break;

            case R.id.title_name_01:
                mTpyePop.showPopupWindow(v);
                break;

            case R.id.title_name_02:
                mTimeSelect.showPopupWindow(v);
                break;
            case R.id.title_name_03:
                mTitleMenuLy.setBackgroundColor(getResources().getColor(R.color.app_bg));
                AnimationUtil.setStartTranlteOutAndInAnimation(context, mTitleMenuLy,mClearEditText);
                break;
        }
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    @Override
    public void updateUi() {

    }

    @Override
    public void initView() {
    }

    @Override
    public void bindViewsListener() {
    }

    @Override
    public void baseSetContentView() {
    }

    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        switch (arg0) {
            case 2016:
                if (arg1 == 2016 && null != arg2) {

                    if (mLocusListAdaper.mList.size() != 0) {
                        mDtype = mLocusListAdaper.mList.get(0).getType();
                        match(1, mLocusListAdaper.mList.get(0).getCsid());
                    }
                    else {
                        mRefreshListView
                                .changeHeaderViewByState(BSRefreshListView.REFRESHING);
                        new ThreadUtil(context, LocusLineAcitvity.this).start();
                    }
                }
                break;
        }
    }
    
    
    
    
    public void sarchList(){
        mClearEditText.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) mClearEditText.getContext().getSystemService(
                            Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    LocusLineAcitvity.this
                                            .getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                    keyword = mClearEditText.getText().toString().trim();
                    if (keyword.equals("")) {
                        return false;
                    }
                    mRefreshListView
                            .changeHeaderViewByState(BSRefreshListView.REFRESHING);
                    new ThreadUtil(LocusLineAcitvity.this,
                            LocusLineAcitvity.this).start();
                    return true;
                }
                return false;
            }
        });
        
        // 根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().equals("")) {
                    keyword = "";
                    mRefreshListView
                            .changeHeaderViewByState(BSRefreshListView.REFRESHING);
                    new ThreadUtil(LocusLineAcitvity.this,
                            LocusLineAcitvity.this).start();
//                    mTitleMenuLy.setBackgroundColor(getResources().getColor(R.color.white));
//                    AnimationUtil.setStartTranlteOutAndInAnimation(context,mClearEditText,mTitleMenuLy);
                    
                }

            }
        });
    }
    

}
