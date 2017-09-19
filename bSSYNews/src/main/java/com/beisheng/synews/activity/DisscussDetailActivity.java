
package com.beisheng.synews.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.ThreadUtil;
import com.beisheng.base.view.BSAutoSwipeRefreshLayout;
import com.beisheng.synews.adapter.DiscussDetailAdapter;
import com.beisheng.synews.application.AppApplication;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.mode.NewsVO;
import com.beisheng.synews.view.BSListViewLoadMore;
import com.beisheng.synews.view.BSPopwindowEditText;
import com.beisheng.synews.view.BSPopwindowEditText.CommitCallback;
import com.im.zhsy.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DisscussDetailActivity extends BaseActivity implements OnClickListener {
    private NewsVO mNewsVO, mDisscussVO;
    private BSListViewLoadMore mListView;
    private DiscussDetailAdapter mAdapter;
    protected BSAutoSwipeRefreshLayout mSwipeLayout;
    private int mState = 0; // 0为首次,1为下拉刷新 ，2为加载更多
    private String mPage = "1";
    private String mContentId;
    private String mType;// suburl
    private String mUserid;
    private boolean mCommitFlag = false;
    private BSPopwindowEditText mPopEditText;
    private String mPid;
    private String mTitle;
    private boolean mLodMore = true;// 控制多次加载，即当前加载就不在会加载直到结束
    private List<NewsVO> mList = new ArrayList<NewsVO>();
    private TextView mCommitTv;
    private String mDisscussType = "1";// 1为评论已经的，2为评论多久

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.disscuss_detail_activity, mBaseContentLayout);
    }

    @Override
    public boolean getDataResult() {
        return true;
    }

    @Override
    public void initView() {
        mBaseTitleTv.setText("评论详情");
        mSwipeLayout = (BSAutoSwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeLayout.setEnabled(false);
        mPopEditText = new BSPopwindowEditText(this, mCallback);
        mListView = (BSListViewLoadMore) findViewById(R.id.list_view);
        mCommitTv = (TextView) findViewById(R.id.comment_et);
        mCommitTv.setBackground(BaseCommonUtils.setBackgroundShap(this, 20, R.color.devider_bg, R.color.C1));

        initData();

    }

    @Override
    public void bindViewsListener() {
        mCommitTv.setOnClickListener(this);
    }

    CommitCallback mCallback = new CommitCallback() {

        @Override
        public void commtiCallback(String content) {
            commit(mContentId, content, mType, mPid, mTitle, mUserid);

            NewsVO vo = new NewsVO();
            vo.setNickname(AppApplication.getInstance().getUserInfoVO().getNickname());
            if ("1".equals(mDisscussType)) {
                vo.setReplyname(mNewsVO.getNickname());
            } else {
                vo.setReplyname(mDisscussVO.getNickname());
            }

            vo.setContent(content);

            List<NewsVO> list = new ArrayList<NewsVO>();
            list.add(vo);
            mAdapter.updateDataFrist(list);
        }
    };

    public void initData() {
        Intent intent = this.getIntent();
        mContentId = intent.getStringExtra("id");
        mType = intent.getStringExtra("type");
        mTitle = intent.getStringExtra("title");
        mNewsVO = (NewsVO) intent.getSerializableExtra("vo");
        mAdapter = new DiscussDetailAdapter(this, mNewsVO, mNewsVO.getChildren());
        mListView.setAdapter(mAdapter);
        // mAdapter.updateData(mList);
    }

    public void commit(String conttentid, String content, String suburl, String pid, String title, String userid) {
        mCommitFlag = false;
        showProgressDialog();
        RequestParams params = new RequestParams();
        try {
            params.put("uid", AppApplication.getInstance().getUid());
            params.put("sessionid", AppApplication.getInstance().getSessionid());
            params.put("contentid", conttentid);
            params.put("type", suburl);

            params.put("reply", content);
            params.put("pid", pid);
            params.put("title", title);
            params.put("userid", userid);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        String allUrl = Constant.DOMAIN_NAME + Constant.DISSCUSS_URL;
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(allUrl, params, new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                mCommitFlag = true;
                dismissProgressDialog();
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                String result = new String(arg2);
                dismissProgressDialog();
                mCommitFlag = true;
                try {
                    JSONObject jsonObject = new JSONObject(new String(arg2));
                    String str = (String) jsonObject.get("retinfo");
                    String code = (String) jsonObject.get("code");
                    if (Constant.RESULT_SUCCESS_CODE.equals(code)) {
                        showCustomToast(str);
                        new ThreadUtil(DisscussDetailActivity.this, DisscussDetailActivity.this).start();
                    } else {
                        showCustomToast(str);
                    }
                } catch (Exception e) {
                }

            }
        });
    }

    public void disscussChild(NewsVO vo) {
        if (AppApplication.getInstance().getUserInfoVO() == null) {
            openActivity(LoginActivity.class);
            return;
        }
        mDisscussVO = vo;
        String hint = "回复#" + vo.getNickname() + "#：";
        mPopEditText.setSecondDisscuss(hint);
        // mPid = vo.getPid();
        mPid = vo.getParentid();
        mUserid = vo.getUserid();
        mPopEditText.showPopupWindow(mListView);
        mDisscussType = "2";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comment_et:
                if (AppApplication.getInstance().getUserInfoVO() == null) {
                    openActivity(LoginActivity.class);
                    return;
                }
                String hint = "回复#" + mNewsVO.getNickname() + "#：";
                mPopEditText.setSecondDisscuss(hint);
                mPid = mNewsVO.getPid();
                mUserid = mNewsVO.getUserid();
                mPopEditText.showPopupWindow(mListView);
                mDisscussType = "1";
                break;

            default:
                break;
        }
    }
}
