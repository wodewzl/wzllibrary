
package com.bs.bsims.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.TaskEventListLAVAAdapter;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.constant.Constant4TaskEventPath;
import com.bs.bsims.constant.ExtrasBSVO;
import com.bs.bsims.interfaces.UpdateCallback;
import com.bs.bsims.model.TaskEventItem;
import com.bs.bsims.model.TaskEventItemVO;
import com.bs.bsims.model.TaskStatistics;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.utils.UrlUtil;
import com.bs.bsims.view.BSRefreshListView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * @author peck
 * @Description: 具体到任务统计中的每一项 比如：进行中、 待初审、待定审、已完成、已超期
 * @date 2015-6-15 下午2:28:56
 * @email 971371860@qq.com
 * @version V1.0
 */

public class EXTTaskStatisticsEveryoneListActivity extends BaseActivity {

    private static String TAG = "EXTTaskStatisticsEveryoneListActivity";
    private Context context;

    private List<TaskEventItem> add_datas = new ArrayList<TaskEventItem>();
    // 0为首次,1为上拉刷新 ，2为下拉刷新
    private int mState = 0;
    private TaskEventItemVO taskEventItemVO;
    private TaskEventListLAVAAdapter mTELLAVAAdapter;

    // listView

    private View mFootLayout;
    private TextView mMoreTextView;
    private ProgressBar mProgressBar;
    private LinearLayout mNoContentLyaout;

    private BSRefreshListView mBsRefreshListView;
    private TaskStatistics mTaskStatistics;
    private View asklist_hp_serach_layout;
    private View two_titie_one_layout;

    /**
     * 请求后台的参数 查询分月必须（如：2015-05） 由图表界面传入
     */
    private String urlDate;
    /**
     * 请求后台的参数 由图表界面传入 按状态查询（1"进行中", 2"待初审", 3"待定审", 4"已完成", 5"已超期）
     */
    private String urlStatusid;
    /**
     * 请求后台的参数 按部门部门ID查询 由图表界面传入 did 按部门部门ID查询
     */
    private String urlDid;
    /**
     * 头部标题
     */
    private String topTitle;
    private String refresh;

    @Override
    public void baseSetContentView() {
        // TODO Auto-generated method stub
        context = this;
        View layout = View.inflate(this, R.layout.ac_ext_home_task_hp, null);
        mContentLayout.addView(layout);
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
        mTitleTv.setText(getResources().getString(
                R.string.ac_label_taskstatistics));

        mBsRefreshListView = (BSRefreshListView) findViewById(R.id.bslistview_tasklist_hp_approval);
        mTELLAVAAdapter = new TaskEventListLAVAAdapter(context);
        mBsRefreshListView.setAdapter(mTELLAVAAdapter);
        mNoContentLyaout = (LinearLayout) findViewById(R.id.no_content_layout);
        two_titie_one_layout = findViewById(R.id.two_titie_one_layout);
        asklist_hp_serach_layout = (LinearLayout) findViewById(R.id.tasklist_hp_serach_layout);

        asklist_hp_serach_layout.setVisibility(View.GONE);
        two_titie_one_layout.setVisibility(View.GONE);

        initFoot();
    }

    @Override
    public void bindViewsListener() {
        // TODO Auto-generated method stub

        mFootLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mMoreTextView.setText("正在加载...");
                mProgressBar.setVisibility(View.VISIBLE);
                mState = 2;
                refresh = Constant.LASTID;
                new ThreadUtil(context, (UpdateCallback) context).start();
            }
        });

        mBsRefreshListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                Intent intent = new Intent();
                intent.setClass(context, EXTTaskEventDetailsActivity.class);
                intent.putExtra(ExtrasBSVO.Push.BREAK_ID, mTELLAVAAdapter.mList
                        .get(position - 1).getTaskid());
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Intent getIntent = getIntent();
        urlDate = getIntent.getStringExtra("currentDate");
        urlStatusid = getIntent.getStringExtra("currentStatusid");
        urlDid = getIntent.getStringExtra("currentDid");
        if (getIntent.hasExtra("TaskStatistics")) {
            mTaskStatistics = (TaskStatistics) getIntent
                    .getSerializableExtra("TaskStatistics");
            urlStatusid = mTaskStatistics.getStatus();
            urlDid = mTaskStatistics.getDid();
        }
    }

    @Override
    public void executeSuccess() {

        if (1 == mState) {
            mTELLAVAAdapter.updateDataFrist(taskEventItemVO.getArray());
        } else if (2 == mState) {
            mTELLAVAAdapter.updateDataLast(taskEventItemVO.getArray());
        } else {
            mTELLAVAAdapter.updateData(taskEventItemVO.getArray());
        }
        if (!TextUtils.isEmpty(topTitle)) {
            mTitleTv.setText(topTitle);
        }
        mState = 0;
        mTELLAVAAdapter.notifyDataSetChanged();
        mBsRefreshListView.onRefreshComplete();
        mLoading.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);
        mLoadingLayout.setVisibility(View.GONE);
        footViewIsVisibility(mTELLAVAAdapter.mList);
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
            CommonUtils.setNonetIcon(this, mLoading,this);
        }

        if (!TextUtils.isEmpty(topTitle)) {
            mTitleTv.setText(topTitle);
        }

        mTELLAVAAdapter.notifyDataSetChanged();
        mBsRefreshListView.onRefreshComplete();
        footViewIsVisibility(mTELLAVAAdapter.mList);
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

        Intent getIntent = getIntent();
        urlDate = getIntent.getStringExtra("currentDate");
        urlStatusid = getIntent.getStringExtra("currentStatusid");
        urlDid = getIntent.getStringExtra("currentDid");
        topTitle = getIntent.getStringExtra("nextTitle");
        if (getIntent.hasExtra("TaskStatistics")) {
            mTaskStatistics = (TaskStatistics) getIntent
                    .getSerializableExtra("TaskStatistics");
            urlStatusid = mTaskStatistics.getStatus();
            urlDid = mTaskStatistics.getDid();
        }
        String strUlr = UrlUtil.getTaskStatisticsUlr(
                Constant4TaskEventPath.TASKEVENTLIST_PATH_T, "1", "0", urlDate,
                urlStatusid, urlDid, refresh, id);
        // urlStatusid, "0", refresh, id);

        try {
            String jsonStr = HttpClientUtil.get(strUlr, Constant.ENCODING)
                    .trim();
            Gson gson = new Gson();
            taskEventItemVO = gson.fromJson(jsonStr, TaskEventItemVO.class);
            if (Constant.RESULT_CODE.equals(taskEventItemVO.getCode())) {
                return true;
            } else if (Constant.RESULT_CODE400
                    .equals(taskEventItemVO.getCode())) {
                // CustomToast.showShortToast(context,
                // taskEventItemVO.getRetinfo());
                // CustomDialog.closeProgressDialog();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

}
