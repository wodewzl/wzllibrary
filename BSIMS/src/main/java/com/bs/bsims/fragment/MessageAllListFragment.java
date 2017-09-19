
package com.bs.bsims.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.MessageListAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.interfaces.UpdateCallback;
import com.bs.bsims.model.MessageListVO;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSRefreshListView;
import com.bs.bsims.view.BSRefreshListView.OnRefreshListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint("ValidFragment")
public class MessageAllListFragment extends BaseFragment implements OnItemClickListener, OnClickListener, UpdateCallback {
    private String mFristid, mLastid;
    private int mState = 0; // 0为首次,1为上拉刷新 ，2为下拉刷新
    private MessageListVO mListVO;
    private Activity mActivity;
    private BSRefreshListView mRefreshListView;
    private MessageListAdapter mAdapter;

    // 下拉上拉刷新部分
    private View mFootLayout;
    private TextView mMoreTextView;
    private ProgressBar mProgressBar;
    private boolean mCanClickFlag = true;
    private boolean mAttentionFlag = false;
    private String mType;

    public MessageAllListFragment() {
    }

    public MessageAllListFragment(String type) {
        this.mType = type;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_list_my, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        bindViewsListener();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRefreshListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
        new ThreadUtil(mActivity, this).start();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean execute() {
        return getData();
    }

    @Override
    public void executeSuccess() {
        mRefreshListView.setVisibility(View.VISIBLE);
        mRefreshListView.onRefreshComplete();
        if (1 == mState) {
            mAdapter.updateDataFrist(mListVO.getArray());
        } else if (2 == mState) {
            mAdapter.updateDataLast(mListVO.getArray());
        } else {
            mAdapter.updateData(mListVO.getArray());
        }

        if ("".equals(mFristid) || mState != 1) {
            footViewIsVisibility();
        }
        mState = 0;
        mCanClickFlag = true;
    }

    @Override
    public void executeFailure() {
        // 列表展示的时候不能调用父类
        mAdapter.notifyDataSetChanged();
        mRefreshListView.onRefreshComplete();
        footViewIsVisibility();

        if (mListVO == null) {
            mAdapter.updateData(new ArrayList<MessageListVO>());
            mFootLayout.setVisibility(View.GONE);
        } else {
            if (mState == 0) {
                mAdapter.updateData(new ArrayList<MessageListVO>());
                mFootLayout.setVisibility(View.GONE);
            }
        }

        mState = 0;
        mCanClickFlag = true;
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onItemClick(AdapterView<?> parentView, View arg1, int position, long arg3) {
        if (mAdapter.mList.size() > 0 && mAdapter.getItemViewType((int) arg3) != -10) {
        }
    }

    public void initView(View view) {
        mRefreshListView = (BSRefreshListView) view.findViewById(R.id.refresh_listview);
        mAdapter = new MessageListAdapter(mActivity, mType);
        mRefreshListView.setAdapter(mAdapter);
        mRefreshListView.setAdapter(mAdapter);
        initFoot();

    }

    // 加载更多数据
    public void initFoot() {
        mFootLayout = LayoutInflater.from(mActivity).inflate(R.layout.listview_bottom_more, null);
        mMoreTextView = (TextView) mFootLayout.findViewById(R.id.txt_loading);
        mMoreTextView.setText("更多");
        mProgressBar = (ProgressBar) mFootLayout.findViewById(R.id.progressBar);
        mFootLayout.setVisibility(View.GONE);
        mRefreshListView.addFooterView(mFootLayout);
    }

    public void bindViewsListener() {
        mRefreshListView.setOnItemClickListener(this);

        bindRefreshListener();
    }

    public void bindRefreshListener() {
        mRefreshListView.setonRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mAdapter.mList.size() > 0) {
                    match(1, mAdapter.mList.get(0).getListid());
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
                    match(2, mAdapter.mList.get(mAdapter.mList.size() - 1).getListid());
                }
            }
        });
    }

    public boolean getData() {
        try {
            Gson gson = new Gson();
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userid", BSApplication.getInstance().getUserId());
            map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            map.put("isboss", BSApplication.getInstance().getUserFromServerVO().getIsboss());
            map.put("type", mType);
            if (0 == mState) {
                mFristid = "";
                mLastid = "";
            }

            map.put("firstid", mFristid);
            map.put("lastid", mLastid);
            String jsonStr1 = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle() + Constant.MESSAGE_LIST, map);
            mListVO = gson.fromJson(jsonStr1, MessageListVO.class);

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

            default:
                break;
        }
        mRefreshListView.changeHeaderViewByState(BSRefreshListView.REFRESHING);
        new ThreadUtil(mActivity, this).start();
    }

    @Override
    public String getFragmentName() {
        return null;
    }

    @Override
    public void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        // if (arg2 == null)
        // return;
        // if (!mAttentionFlag) {
        // List<MessageListVO> list = (List<MessageListVO>) arg2.getSerializableExtra("listvo");
        // mAdapter.updateData(list);
        // }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
