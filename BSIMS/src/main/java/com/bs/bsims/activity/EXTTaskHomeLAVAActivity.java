
package com.bs.bsims.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.bs.bsims.R;
import com.bs.bsims.adapter.TaskEventListLAVAAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.constant.Constant4TaskEventPath;
import com.bs.bsims.constant.ExtrasBSVO;
import com.bs.bsims.interfaces.UpdateCallback;
import com.bs.bsims.model.TaskEventItem;
import com.bs.bsims.model.TaskEventItemVO;
import com.bs.bsims.model.UserFromServerVO;
import com.bs.bsims.receiver.TaskActionReceiver;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.utils.UrlUtil;
import com.bs.bsims.view.BSIndexEditText;
import com.bs.bsims.view.BSRefreshListView;
import com.bs.bsims.view.BSRefreshListView.OnRefreshListener;
import com.google.gson.Gson;

import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author peck
 * @Description:
 * @date 2015-6-4 下午2:46:05
 * @email 971371860@qq.com
 * @version V1.1 接口更新，根据这个字段判断是否可以读取全部的全部任务 2015/6/6 15:51
 */

public class EXTTaskHomeLAVAActivity extends BaseActivity implements
        OnItemClickListener, OnClickListener, UpdateCallback {

    private static final String TAG = "EXTTaskHomeLAVAActivity";

    private LinearLayout mTitle02, mTitle03, mTitle01;
    private RelativeLayout mTitleLayout;
    private TextView mTitleName02, mTitleName03, mTitleName01;
    private BSPopupWindwos mPop;
    private BSRefreshListView mBsRefreshListView;

    private String modeid, isall, bigtypeid, smalltypeid, statusid, refresh,
            mKeyword, mIsBoss, mBossIndex = "", date;

    private BSIndexEditText mBSBsIndexEditText;
    private Context context;

    private View mTwoTitleLayout;
    private LinearLayout mSearchLayout;

    /**
     * 获取任务列表 统一设置参数
     */
    private Map<String, String> paramsMap = new HashMap<String, String>();

    private Map<String, String> mFixedMap;

    private List<TaskEventItem> add_datas = new ArrayList<TaskEventItem>();
    // private TaskEventListAdapter add_adapter;
    // 0为首次,1为上拉刷新 ，2为下拉刷新
    private int mState = 0;
    private TaskEventItemVO taskEventItemVO;
    private TaskEventListLAVAAdapter mTELLAVAAdapter;

    // listView

    private View mFootLayout;
    private TextView mMoreTextView;
    private ProgressBar mProgressBar;
    private LinearLayout mNoContentLyaout;
    private Boolean canClickFlag = true;// 解决连续点“更多”可能会出现的异常
    private TextView mNoReadTv;

    private BroadcastReceiver my_TaskHomeLAVA_Receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (TaskActionReceiver.CHANGEDATE.equals(intent.getAction())) {
                String taskid = intent.getStringExtra("task_id");
                if (null != mTitleName01) {
                    mTitleName01.setText("全部任务");
                }
                if (null != mTitleName02) {
                    mTitleName02.setText("全部状态");
                }
                modeid = "";
                bigtypeid = "";
                smalltypeid = "";
                statusid = "";
                cleanData();
                new ThreadUtil(context, (UpdateCallback) context).start();
            }
            if (TaskActionReceiver.CHANGEPRELIMINARYSTATUS.equals(intent
                    .getAction())) {
                String taskid = intent.getStringExtra("task_id");
                if (null != mTitleName01) {
                    mTitleName01.setText("全部任务");
                }
                if (null != mTitleName02) {
                    mTitleName02.setText("全部状态");
                }
                modeid = "";
                bigtypeid = "";
                smalltypeid = "";
                statusid = "";
                cleanData();
                new ThreadUtil(context, (UpdateCallback) context).start();
            }
        }
    };

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.two_title01: {
                mPop.currentView(this, 1);
                if (!mPop.isShowing()) {
                    mPop.showAsDropDown(mTitle01);
                }
            }
                break;
            case R.id.two_title02: {
                mPop.currentView(this, 2);
                if (!mPop.isShowing()) {
                    mPop.showAsDropDown(mTitle01);
                }
            }
                break;
            case R.id.txt_comm_head_right:
                Intent intent = new Intent();
                intent.setClass(context, ReleaseTaskActivity.class);
                startActivity(intent);
                break;

            case R.id.no_read_tv:
                Intent noReadIntent = new Intent();
                noReadIntent.putExtra("isall", CommonUtils.getLimitsSpecial(Constant.LIMITS_SPECIAL002));
                noReadIntent.putExtra("isboss", BSApplication.getInstance().getUserFromServerVO().getIsboss());
                noReadIntent.putExtra("type", 8);
                noReadIntent.setClass(this, AllNoReadActivity.class);
                startActivity(noReadIntent);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void baseSetContentView() {
        // TODO Auto-generated method stub
        View layout = View.inflate(this, R.layout.ac_ext_home_task_hp, null);
        mContentLayout.addView(layout);
        x.view().inject(this);
        context = this;

        // 注册进度广播
        IntentFilter my_TaskHomeLAVA_Filter = new IntentFilter();
        my_TaskHomeLAVA_Filter.addAction(TaskActionReceiver.CHANGEDATE);
        my_TaskHomeLAVA_Filter.addAction(TaskActionReceiver.CHANGEPRELIMINARYSTATUS);
        registerReceiver(my_TaskHomeLAVA_Receiver, my_TaskHomeLAVA_Filter);
        setLeadClass("EXTTaskHomeLAVAActivity");
    }

    @Override
    public boolean getDataResult() {
        // TODO Auto-generated method stub
        return getData();
    }

    @Override
    public void updateUi() {
        // TODO Auto-generated method stub

    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub

        mTitleTv.setText("任务管理");
        mOkTv.setText(R.string.write_journal);

        mTitleName01 = (TextView) findViewById(R.id.two_title_name_01);
        mTitleName02 = (TextView) findViewById(R.id.two_title_name_02);
        mTitleName01.setText("全部任务");
        mTitleName02.setText("全部状态");
        mTitleLayout = (RelativeLayout) View.inflate(context,
                R.layout.two_titie_one, null);
        mTitle01 = (LinearLayout) findViewById(R.id.two_title01);
        mTitle02 = (LinearLayout) findViewById(R.id.two_title02);

        mBsRefreshListView = (BSRefreshListView) findViewById(R.id.bslistview_tasklist_hp_approval);
        mTELLAVAAdapter = new TaskEventListLAVAAdapter(context);
        mBsRefreshListView.setAdapter(mTELLAVAAdapter);

        mBSBsIndexEditText = (BSIndexEditText) findViewById(R.id.edit_tasklist_hp_single_search);
        initFoot();

        mPop = new BSPopupWindwos(this);
        mNoContentLyaout = (LinearLayout) findViewById(R.id.no_content_layout);
        setFixedMap();

        mTwoTitleLayout = findViewById(R.id.two_titie_one_layout);
        mSearchLayout = (LinearLayout) findViewById(R.id.tasklist_hp_serach_layout);
        mNoReadTv = (TextView) findViewById(R.id.no_read_tv);

        initData();
        // 首页进来隐藏收索条件
        if (this.getIntent().getStringExtra("msg") != null) {
            mSearchLayout.setVisibility(View.GONE);
            mTwoTitleLayout.setVisibility(View.GONE);
        }
    }

    public void initData() {
        Intent intent = this.getIntent();
        if (intent.getStringExtra("modeid") != null)
            modeid = intent.getStringExtra("modeid");

        // boss首界面下方进入
        if (intent.getStringExtra("bossIndex") != null) {
            mBossIndex = intent.getStringExtra("bossIndex");
            mSearchLayout.setVisibility(View.GONE);
            mTwoTitleLayout.setVisibility(View.GONE);
        }

        if (intent.getStringExtra("isall") != null) {
            isall = intent.getStringExtra("isall");

        }

        if (intent.getStringExtra("statusName") != null) {
            mTitleName02.setText(intent.getStringExtra("statusName"));
        }

        if (intent.getStringExtra("time") != null) {
            mTitleTv.setText(intent.getStringExtra("time") + intent.getStringExtra("statusName"));
        }

        if (intent.getStringExtra("date") != null) {
            date = intent.getStringExtra("date");
            mSearchLayout.setVisibility(View.GONE);
            mTwoTitleLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void bindViewsListener() {
        mTitle01.setOnClickListener(this);
        mTitle02.setOnClickListener(this);
        mOkTv.setOnClickListener(this);
        mNoReadTv.setOnClickListener(this);
        mBsRefreshListView.setonRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                mState = 1;
                refresh = Constant.FIRSTID;
                new ThreadUtil(context, (UpdateCallback) context).start();
            }
        });
        mFootLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (canClickFlag) {
                    canClickFlag = false;
                    mMoreTextView.setText("正在加载...");
                    mProgressBar.setVisibility(View.VISIBLE);
                    mState = 2;
                    refresh = Constant.LASTID;
                    new ThreadUtil(context, (UpdateCallback) context).start();
                }
            }
        });

        mBSBsIndexEditText
                .setOnEditorActionListener(new OnEditorActionListener() {

                    @Override
                    public boolean onEditorAction(TextView v, int actionId,
                            KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            // 先隐藏键盘
                            ((InputMethodManager) mBSBsIndexEditText
                                    .getContext().getSystemService(
                                            Context.INPUT_METHOD_SERVICE))
                                    .hideSoftInputFromWindow(
                                            EXTTaskHomeLAVAActivity.this
                                                    .getCurrentFocus()
                                                    .getWindowToken(),
                                            InputMethodManager.HIDE_NOT_ALWAYS);

                            mKeyword = mBSBsIndexEditText.getText().toString();
                            cleanData();
                            mBsRefreshListView
                                    .changeHeaderViewByState(BSRefreshListView.REFRESHING);
                            new ThreadUtil(context, (UpdateCallback) context)
                                    .start();
                            return true;
                        }
                        return false;
                    }
                });

        mBsRefreshListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                /**
                 * 任务未读，在点击后就处理为已读 因为是否有未读标记是，Isnoread==1，所以标记为已读只需要setIsnoread("0"); 不为1 即可
                 * 
                 * @date 2015/7/30 9:50
                 * @author peck 刘鹏程
                 */
                TaskEventItem mTaskEventItem10 = mTELLAVAAdapter.mList.get(position - 1);
                mTaskEventItem10.setIsnoread("0");
                mTELLAVAAdapter.notifyDataSetChanged();
                Intent intent = new Intent();
                intent.setClass(context, EXTTaskEventDetailsActivity.class);
                intent.putExtra(ExtrasBSVO.Push.BREAK_ID, mTaskEventItem10.getTaskid());
                startActivity(intent);
            }
        });

        /***
         * BOSS 需要有发布任务的功能
         * 
         * @date 2015/7/30 10:23
         * @author peck 刘鹏程
         */
        UserFromServerVO mUserFromServerVO10 = BSApplication.getInstance().getUserFromServerVO();
        String isBossStr = "";
        if (null != mUserFromServerVO10) {
            isBossStr = mUserFromServerVO10.getIsboss();
        }

        if (CommonUtils.getLimitsPublish(Constant.LIMITS_PUBLISH002)) {
            mOkTv.setText("发布");
            mOkTv.setVisibility(View.VISIBLE);
        } else {
            mOkTv.setVisibility(View.GONE);
        }

        // // 是否有发布权限
        // List<MenuVO> listVo = BSApplication.getInstance().getUserFromServerVO().getMenu();
        // if (CommonUtils.isLimits(listVo.get(0).getMenu(), Constant.LIMITS_OFFICE003)) {
        // mOkTv.setText("发布");
        // mOkTv.setOnClickListener(this);
        // mOkTv.setVisibility(View.VISIBLE);
        // }

    }

    private class BSPopupWindwos extends PopupWindow implements OnClickListener {
        private Context mContext;
        private LinearLayout mTitleLayout03, mTitlePopLayout04;
        private Button mOkBt;
        private TextView textViw01, textViw02, textViw03, textViw04, textViw05,
                extPopTextView010, extPopTextView011, textViw06, textViw07,
                textViw08, textViw09;
        private boolean mOne = true;

        public BSPopupWindwos(Context context) {
            this.mContext = context;
            View view = View.inflate(context, R.layout.ext_pop_home_task_hp,
                    null);
            mTitleLayout03 = (LinearLayout) view
                    .findViewById(R.id.title_layout_03);
            mTitlePopLayout04 = (LinearLayout) view
                    .findViewById(R.id.title_layout_pop_home_task);
            textViw01 = (TextView) view
                    .findViewById(R.id.pop_home_task_text_01);
            textViw02 = (TextView) view
                    .findViewById(R.id.pop_home_task_text_02);
            textViw03 = (TextView) view
                    .findViewById(R.id.pop_home_task_text_03);
            textViw04 = (TextView) view
                    .findViewById(R.id.pop_home_task_text_04);
            extPopTextView010 = (TextView) view
                    .findViewById(R.id.pop_home_task_text_05);
            extPopTextView011 = (TextView) view
                    .findViewById(R.id.pop_home_task_text_06);

            textViw05 = (TextView) view.findViewById(R.id.text_05);
            textViw06 = (TextView) view.findViewById(R.id.text_06);
            textViw07 = (TextView) view.findViewById(R.id.text_07);
            textViw08 = (TextView) view.findViewById(R.id.text_08);
            textViw09 = (TextView) view.findViewById(R.id.text_09);

            setPoP1InnerText();
            textViw01.setOnClickListener(this);
            textViw02.setOnClickListener(this);
            textViw03.setOnClickListener(this);
            textViw04.setOnClickListener(this);
            extPopTextView010.setOnClickListener(this);
            extPopTextView011.setOnClickListener(this);

            textViw05.setOnClickListener(this);
            textViw06.setOnClickListener(this);
            textViw07.setOnClickListener(this);
            textViw08.setOnClickListener(this);
            textViw09.setOnClickListener(this);

            view.startAnimation(AnimationUtils.loadAnimation(context,
                    R.anim.fade_in));
            setWidth(LayoutParams.MATCH_PARENT);
            setHeight(LayoutParams.WRAP_CONTENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            update();
        }

        public void currentView(Context context, int type) {

            if (type == 1) {
                mTitleLayout03.setVisibility(View.VISIBLE);
                mTitlePopLayout04.setVisibility(View.GONE);
            } else if (type == 2) {
                mTitlePopLayout04.setVisibility(View.VISIBLE);
                mTitleLayout03.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            String textLeft = mTitleName02.getText().toString();
            String textRight = mTitleName01.getText().toString();
            switch (v.getId()) {
                case R.id.pop_home_task_text_01:

                    textLeft = textViw01.getText().toString();
                    textLeft = textViw01.getText().toString();
                    dismiss();
                    break;
                case R.id.pop_home_task_text_02:

                    textLeft = textViw02.getText().toString();
                    dismiss();
                    break;
                case R.id.pop_home_task_text_03:

                    textLeft = textViw03.getText().toString();
                    dismiss();
                    break;
                case R.id.pop_home_task_text_04:

                    textLeft = textViw04.getText().toString();
                    dismiss();
                    break;
                case R.id.pop_home_task_text_05:

                    textLeft = extPopTextView010.getText().toString();
                    dismiss();
                    break;
                case R.id.pop_home_task_text_06:

                    textLeft = extPopTextView011.getText().toString();
                    dismiss();
                    break;
                case R.id.text_05:

                    textRight = textViw05.getText().toString();
                    dismiss();
                    break;
                case R.id.text_06:

                    textRight = textViw06.getText().toString();
                    dismiss();
                    break;
                case R.id.text_07:

                    textRight = textViw07.getText().toString();
                    dismiss();
                    break;
                case R.id.text_08:

                    textRight = textViw08.getText().toString();
                    dismiss();
                    break;
                case R.id.text_09:

                    textRight = textViw09.getText().toString();
                    dismiss();
                    break;
                default:
                    break;
            }
            mTitleName01.setText(textRight);
            mTitleName02.setText(textLeft);
            // mState = 1;
            cleanData();
            mKeyword = "";
            mBsRefreshListView
                    .changeHeaderViewByState(BSRefreshListView.REFRESHING);
            new ThreadUtil(context, (UpdateCallback) context).start();
        }

        private void setPoP1InnerText() {
            // TODO Auto-generated method stub
            /** 1 我发布的 2 我负责的 3 我跟进的 4 我相关的 */
            textViw05.setText("全部任务");
            textViw06.setText("我发布的");
            textViw07.setText("我负责的");
            textViw08.setText("我跟进的");
            textViw09.setText("知会我的");
        }

    }

    public boolean getData() {
        String id = "";
        if (0 == mState) {
            return getData("", "");
        } else if (1 == mState) {
            if (null != mTELLAVAAdapter.mList
                    && !mTELLAVAAdapter.mList.isEmpty()
                    && mTELLAVAAdapter.mList.size() > 0) {
                id = mTELLAVAAdapter.mList.get(0).getTaskid();
            }
            return getData(refresh, id);
        } else if (2 == mState) {
            if (null != mTELLAVAAdapter.mList
                    && !mTELLAVAAdapter.mList.isEmpty()
                    && mTELLAVAAdapter.mList.size() > 0) {
                id = mTELLAVAAdapter.mList
                        .get(mTELLAVAAdapter.mList.size() - 1).getTaskid();
            }
            return getData(refresh, id);
        }
        return false;
    }

    public boolean getData(String refresh, String id) {
        modeid = "";
        bigtypeid = "";
        smalltypeid = "";
        statusid = "";

        // mKeyword = "";

        String textRight = mTitleName01.getText().toString();
        String textLeft = mTitleName02.getText().toString();
        modeid = mFixedMap.get(textRight);
        statusid = mFixedMap.get(textLeft);
        /**
         * 接口更新，根据这个字段判断是否可以读取全部的全部任务 2015/6/6 15:51
         */
        // if ("1".equalsIgnoreCase(BSApplication.getInstance().getTaskBoss())) {
        // isall = BSApplication.getInstance().getUserFromServerVO()
        // .getManagement();
        // }
        /**
         * 为空出bug
         */
        isall = CommonUtils.getLimitsSpecial(Constant.LIMITS_SPECIAL002);
        mIsBoss = CommonUtils.getLimitsSpecial(Constant.LIMITS_SPECIAL002);
        String unread = this.getIntent().getStringExtra("unread");

        String strUlr = UrlUtil.getTaskHomeLAVAUlr(
                Constant4TaskEventPath.TASKEVENTLIST_PATH_T, isall, statusid,
                modeid, bigtypeid, smalltypeid, mKeyword, refresh, id, mIsBoss, mBossIndex, date, unread);

        try {
            String jsonStr = HttpClientUtil.get(strUlr, Constant.ENCODING)
                    .trim();
            Gson gson = new Gson();
            taskEventItemVO = gson.fromJson(jsonStr, TaskEventItemVO.class);
            if (Constant.RESULT_CODE.equals(taskEventItemVO.getCode())) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @Override
    public void executeSuccess() {
        super.executeSuccess();
        if (1 == mState) {
            mTELLAVAAdapter.updateDataFrist(taskEventItemVO.getArray());
        } else if (2 == mState) {
            mTELLAVAAdapter.updateDataLast(taskEventItemVO.getArray());
        } else {
            mTELLAVAAdapter.updateData(taskEventItemVO.getArray());
        }

        mState = 0;
        mTELLAVAAdapter.notifyDataSetChanged();
        mBsRefreshListView.onRefreshComplete();
        mLoading.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
        footViewIsVisibility(mTELLAVAAdapter.mList);
        canClickFlag = true;
    };

    @Override
    public void executeFailure() {
        if (taskEventItemVO != null) {
            if (mState == 0) {
                mLoading.setVisibility(View.GONE);
                mContentLayout.setVisibility(View.VISIBLE);
                mLoadingLayout.setVisibility(View.GONE);

                mBsRefreshListView.setVisibility(View.GONE);
                mNoContentLyaout.setVisibility(View.VISIBLE);
            }
        } else {
            CommonUtils.setNonetIcon(this, mLoading, this);
        }

        mTELLAVAAdapter.notifyDataSetChanged();
        mBsRefreshListView.onRefreshComplete();
        footViewIsVisibility(mTELLAVAAdapter.mList);
        canClickFlag = true;
    }

    // 加载更多数据
    public void initFoot() {
        mFootLayout = LayoutInflater.from(this).inflate(
                R.layout.listview_bottom_more, null);
        mMoreTextView = (TextView) mFootLayout.findViewById(R.id.txt_loading);
        mMoreTextView.setText("更多");
        mProgressBar = (ProgressBar) mFootLayout.findViewById(R.id.progressBar);
        mFootLayout.setVisibility(View.GONE);
        mBsRefreshListView.addFooterView(mFootLayout);
    }

    protected void footViewIsVisibility(List<TaskEventItem> datas) {
        if (taskEventItemVO == null) {
            return;
        }
        if (taskEventItemVO.getCount() == null) {
            return;
        }
        if (datas.size() >= Integer.parseInt(taskEventItemVO.getCount())) {
            mFootLayout.setVisibility(View.GONE);
            // listView.removeFooterView(mFootLayout);
        } else {
            mFootLayout.setVisibility(View.VISIBLE);
            mMoreTextView.setText("更多");
            mProgressBar.setVisibility(View.GONE);
        }
    }

    public void cleanData() {
        mTELLAVAAdapter.mList.clear();
        mFootLayout.setVisibility(View.GONE);
        mNoContentLyaout.setVisibility(View.GONE);
        mBsRefreshListView.setVisibility(View.VISIBLE);
    }

    /**
     * 初始化
     */
    private void setFixedMap() {
        // TODO Auto-generated method stub management
        mFixedMap = new HashMap<String, String>();
        // 状态（0"全部状态" ,1"进行中", 2"待初审", 3"待定审", 4"已完成", 5"已超期）
        // 0全部 1我发布的 2我负责的 3我跟进的 4知会我的
        mFixedMap.put("全部状态", 0 + "");
        mFixedMap.put("进行中", 1 + "");
        mFixedMap.put("待初审", 2 + "");
        mFixedMap.put("待定审", 3 + "");
        mFixedMap.put("已完成", 4 + "");
        mFixedMap.put("已超期", 5 + "");
        mFixedMap.put("全部任务", 0 + "");
        mFixedMap.put("我发布的", 1 + "");
        mFixedMap.put("我负责的", 2 + "");
        mFixedMap.put("我跟进的", 3 + "");
        mFixedMap.put("知会我的", 4 + "");
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(my_TaskHomeLAVA_Receiver);
        super.onDestroy();

    }
}
