
package com.bs.bsims.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.CreativeIdeaAdapter;
import com.bs.bsims.adapter.DiscussAdapter;
import com.bs.bsims.adapter.HeadAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.fragment.CommentFragment;
import com.bs.bsims.model.CreativeIdeaDetailResultVO;
import com.bs.bsims.model.CreativeIdeaDetailVO;
import com.bs.bsims.model.DiscussLeadResultVO;
import com.bs.bsims.model.DiscussResultVO;
import com.bs.bsims.model.DiscussVO;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.utils.FileSizeUtil;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.utils.UrlUtil;
import com.bs.bsims.utils.WebViewPictureUtil;
import com.bs.bsims.view.BSCircleImageView;
import com.bs.bsims.view.BSDialog;
import com.bs.bsims.view.BSGridView;
import com.bs.bsims.view.BSListView;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreativeIdeaDetailActivity extends BaseActivity implements OnClickListener {
    private static final int ADD_INFORM_PERSON = 10;

    private BSListView mDiscussGeneralLv, mDiscussLeadLv;
    private Button mApprovalBt, mUnapprovalBt;
    private CreativeIdeaDetailVO mCreativeIdeaDetailVO;
    private DiscussLeadResultVO mLeadResultVO;
    private DiscussResultVO mDiscussGerenalResultVO;
    private TextView mLeadTv, mGeneraTv;

    private TextView mName, mTypeTv, mDepartment, mTime;
    private TextView mTitle, mContent;
    private String mType;
    private String mId;
    private String mLead;
    private BSCircleImageView mHeadImage;
    private ImageLoader mImageLoader;
    private WebView mWebView;

    private ListView mApprovalListView;
    private CreativeIdeaAdapter mApprovlaIdeaAdapter;

    private DiscussResultVO mDiscussResultVO, mAccpetResultVO;
    private BSListView mDiscussLv;
    private DiscussAdapter mDiscussAdapter;
    private TextView mDiscussTitle;
    private CommentFragment fragment;

    // 显示上传的图片
    private ImageView mDetailImg01, mDetailImg02, mDetailImg03;
    private List<ImageView> mListImag;
    private LinearLayout mPictureLayout, mReplayLayout, approvalLayout;

    private CreativeIdeaDetailResultVO detailResultVO;

    private View mFootLayout;
    private TextView mMoreTextView;
    private ProgressBar mProgressBar;
    // 0为首次,1为上拉刷新 ，2为下拉刷新
    private int mState = 0;
    private boolean mFlag = true;
    private String mIsBoss;

    private String mAccept;
    private boolean mCommitFlag = true;

    /**
     * 推送返回来的标题
     */
    private String typename;
    private TextView mAcceptTv;

    // 知会人，审批人
    private BSGridView mInformGv;
    private HeadAdapter mInformAdapter;
    private TextView mInformTv, mInformGoTv;
    private LinearLayout mInformLayout;
    private StringBuffer mInformPerson;

    private BSDialog mDialog;
    private String mMessageid;

    // 添加员工使用
    protected List<EmployeeVO> mDataList = new ArrayList<EmployeeVO>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.idea_adopt, null);
        mContentLayout.addView(layout);
    }

    @Override
    public void initView() {
        mName = (TextView) findViewById(R.id.person_title01);
        mTypeTv = (TextView) findViewById(R.id.person_title03);
        mDepartment = (TextView) findViewById(R.id.person_title02);
        mDepartment.setTextSize(12);
        mTime = (TextView) findViewById(R.id.person_title04);

        mTitle = (TextView) findViewById(R.id.title);
        mWebView = (WebView) findViewById(R.id.content);
        mDiscussTitle = (TextView) findViewById(R.id.discuss_tv);

        mApprovalBt = (Button) findViewById(R.id.approval_bt);
        mUnapprovalBt = (Button) findViewById(R.id.unapproval_bt);
        mHeadImage = (BSCircleImageView) findViewById(R.id.head_icon);
        mImageLoader = ImageLoader.getInstance();
        initData();

        mApprovalListView = (ListView) findViewById(R.id.list_approval_view);
        mApprovlaIdeaAdapter = new CreativeIdeaAdapter(this);
        mApprovalListView.setAdapter(mApprovlaIdeaAdapter);

        // 显示界面
        fragment = new CommentFragment();
        Bundle bundle = new Bundle();
        /*
         * 这里的key必须是ReceiverAction.Key value就是你注册广播时候的actiion，
         * 手动在org.baiteng.oa.setting.ReceiverAction中添加此activity的action
         */
        bundle.putString(Constant.DocKey, Constant.NoticeDetailsAction);
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.bottom_view, fragment);
        transaction.commitAllowingStateLoss();

        mDiscussLv = (BSListView) findViewById(R.id.list_view);
        initFoot();
        mDiscussAdapter = new DiscussAdapter(this, mId, fragment, mDiscussTitle);
        mDiscussLv.setAdapter(mDiscussAdapter);
        mDiscussAdapter.setAgreeUrl(BSApplication.getInstance().getHttpTitle() + Constant.CREATIVE_AGREE_URL);
        mDiscussAdapter.setOpposeUrl(BSApplication.getInstance().getHttpTitle() + Constant.CREATIVE_OPPOSE_URL);
        mDiscussAdapter.setContentUrl(BSApplication.getInstance().getHttpTitle() + Constant.CREATIVE_DISCUSS_COMMIT_URL);
        mDiscussAdapter.setType(3);

        mReplayLayout = (LinearLayout) findViewById(R.id.bottom_view);
        approvalLayout = (LinearLayout) findViewById(R.id.bottom_layout);
        mAcceptTv = (TextView) findViewById(R.id.lead_tv);

        mInformGv = (BSGridView) findViewById(R.id.inform_gv);
        mInformAdapter = new HeadAdapter(this, true);
        mInformGv.setAdapter(mInformAdapter);
        mInformTv = (TextView) findViewById(R.id.inform_people_tv);
        mInformLayout = (LinearLayout) findViewById(R.id.inform_people_layout);
        mInformGoTv = (TextView) findViewById(R.id.inform_go_tv);
        mInformPerson = new StringBuffer();

        mInformTv.setVisibility(View.GONE);
        mInformGv.setVisibility(View.GONE);
    }

    public void initData() {
        getIntentData();
        mTitleTv.setText("意见分享");
    }

    public boolean getData() {
        getIntentData();
        if (0 == mState) {
            return getData("", "");
        } else if (2 == mState) {
            String id = mDiscussAdapter.mList.get(mDiscussAdapter.mList.size() - 1).getCommentid();
            return getData(Constant.LASTID, id);
        }
        return false;
    }

    public boolean getData(String refresh, String id) {
        try {
            Gson gson = new Gson();
            if (mFlag) {
                // 详细信息
                String strUrl = UrlUtil.getIdeaDteailUrl(Constant.CREATIVE_IDEA_DETAIL, mId, BSApplication.getInstance().getUserId(), mMessageid);
                String jsonUrlStr = HttpClientUtil.get(strUrl, Constant.ENCODING).trim();
                detailResultVO = gson.fromJson(jsonUrlStr, CreativeIdeaDetailResultVO.class);
            }

            String ideaUrl = UrlUtil.getIdeaDiscuss(Constant.CREATIVE_IDEA_DISCUSS, mId, "1", BSApplication.getInstance().getUserId(), refresh, id);

            String ideaJson = HttpClientUtil.get(ideaUrl, Constant.ENCODING).trim();
            mAccpetResultVO = gson.fromJson(ideaJson, DiscussResultVO.class);
            if (Constant.RESULT_CODE.equals(mAccpetResultVO.getCode())) {
                mApprovlaIdeaAdapter.mList.addAll(mAccpetResultVO.getArray());
            }
            String discussUrl = UrlUtil.getIdeaDiscuss(Constant.CREATIVE_IDEA_DISCUSS, mId, "0", BSApplication.getInstance().getUserId(), refresh, id);
            String discussJson = HttpClientUtil.get(discussUrl, Constant.ENCODING).trim();
            mDiscussResultVO = gson.fromJson(discussJson, DiscussResultVO.class);

            if (Constant.RESULT_CODE.equals(detailResultVO.getCode())) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("isnoread", "0");
                map.put("id", mId);
                CommonUtils.sendBroadcast(this, Constant.HOME_MSG, map);
                mCreativeIdeaDetailVO = detailResultVO.getInfo();
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
    public void bindViewsListener() {
        mApprovalBt.setOnClickListener(this);
        mUnapprovalBt.setOnClickListener(this);
        bindRefreshListener();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(this, CreativeIdeaAdoptionActivity.class);
        switch (v.getId()) {
            case R.id.approval_bt:

                if (mCommitFlag) {
                    mCommitFlag = false;
                    intent.putExtra("userid", BSApplication.getInstance().getUserId());
                    intent.putExtra("id", mId);
                    intent.putExtra("flag", "1");// 同意
                    if ("审核".equals(mApprovalBt.getText().toString())) {
                        uploadCheck("1");
                        intent.putExtra("type", "1");
                    } else {
                        showDialog("1");
                    }
                }

                break;
            case R.id.unapproval_bt:
                if (mCommitFlag) {
                    mCommitFlag = false;
                    intent.putExtra("userid", BSApplication.getInstance().getUserId());
                    intent.putExtra("id", mId);
                    intent.putExtra("flag", "2");// 反对
                    if ("驳回".equals(mUnapprovalBt.getText().toString())) {
                        uploadCheck("2");
                        intent.putExtra("type", "1");
                    } else {
                        showDialog("2");
                    }
                }

                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);

        if (arg0 == 10) {
            if (arg2 == null)
                return;
            mDataList.clear();
            mDataList = (List<EmployeeVO>) arg2.getSerializableExtra("checkboxlist");
            mInformAdapter.mList.clear();
            mInformAdapter.mList.addAll(mDataList);
            mInformAdapter.notifyDataSetChanged();
            mInformPerson.setLength(0);
            for (int i = 0; i < mDataList.size(); i++) {
                mInformPerson.append(mDataList.get(i).getUserid());
                if (i != mDataList.size() - 1) {
                    mInformPerson.append(",");
                }
            }
        } else {
            String status = arg2.getStringExtra("status");
            if ("1".equals(status)) {
                mReplayLayout.setVisibility(View.VISIBLE);
                approvalLayout.setVisibility(View.GONE);
            } else {
                approvalLayout.setVisibility(View.VISIBLE);
                mReplayLayout.setVisibility(View.GONE);
            }

            new ThreadUtil(CreativeIdeaDetailActivity.this, CreativeIdeaDetailActivity.this).start();
        }
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    @Override
    public void updateUi() {
        if (mFlag) {
            if ("1".equals(mCreativeIdeaDetailVO.getIsAnonymous())) {
                mName.setText("匿名");
                mDepartment.setText("");
                mImageLoader.displayImage(null, mHeadImage, CommonUtils.initImageLoaderOptions());
                mHeadImage.setUserId(null);// 匿名用户ID为空
            } else {
                mName.setText(mCreativeIdeaDetailVO.getFullname());
                mDepartment.setText(mCreativeIdeaDetailVO.getDname() + "/" + mCreativeIdeaDetailVO.getPname());
                mImageLoader.displayImage(mCreativeIdeaDetailVO.getHeadpic(), mHeadImage, CommonUtils.initImageLoaderOptions());
                mHeadImage.setUserId(mCreativeIdeaDetailVO.getUserid());// 获取头像对应的用户ID
                mHeadImage.setUserName(mCreativeIdeaDetailVO.getFullname());
                mHeadImage.setUrl(mCreativeIdeaDetailVO.getHeadpic());
            }

            mWebView.clearCache(true);
            mWebView.clearFormData();
            mWebView.clearHistory();
            mWebView.clearMatches();
            mTypeTv.setVisibility(View.INVISIBLE);
            long time = Long.parseLong(mCreativeIdeaDetailVO.getTime()) * 1000;
            mTime.setText(DateUtils.parseDate(time));
            mTitle.setText(mCreativeIdeaDetailVO.getTitle());
            DisplayImageOptions optionsPic = new DisplayImageOptions.Builder().showStubImage(R.drawable.common_ic_image_default).showImageForEmptyUri(R.drawable.common_ic_image_default)
                    .showImageOnFail(R.drawable.common_ic_image_default).cacheInMemory().cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
            String front = "<font color='#666666' size='2'>";
            String back = "</font>";
            String hang = "<br />";
            String divstart = "<div align = 'left'>";
            String divend = "</div>";
            String content = front + divstart + CommonUtils.replaceWebview2(mCreativeIdeaDetailVO.getContent()) + hang + divend + back;
            mWebView.getSettings().setJavaScriptEnabled(true); // 支持js
            mWebView.loadDataWithBaseURL(FileSizeUtil.SDPATH1, content, "text/html", "utf-8", null);
            mWebView.setWebViewClient(new WebViewPictureUtil(this, mWebView, "img", "this.src"));
            mFlag = false;
        }

        if (mAccpetResultVO.getCount() != null) {
            mApprovlaIdeaAdapter.notifyDataSetChanged();
            mAcceptTv.setVisibility(View.VISIBLE);
            mApprovalListView.setVisibility(View.VISIBLE);
        }

        if (Constant.RESULT_CODE.equals(mDiscussResultVO.getCode())) {

            if (2 == mState) {
                mDiscussAdapter.mList.addAll(mDiscussResultVO.getArray());
            } else {
                mDiscussAdapter.mList = mDiscussResultVO.getArray();
            }
            mState = 0;
        }

        if (mDiscussResultVO.getCount() != null) {
            mDiscussTitle.setVisibility(View.VISIBLE);
            String discussStr = String.format(getResources().getString(R.string.discuss), Integer.parseInt(mDiscussResultVO.getCount()));
            mDiscussTitle.setText(discussStr);
            mDiscussAdapter.notifyDataSetChanged();
            footViewIsVisibility(mDiscussAdapter.mList);

        }

        // accept状态值，checks审核 ,isadopt采纳
        if ("0".equals(mIsBoss)) {
            mReplayLayout.setVisibility(View.VISIBLE);
            approvalLayout.setVisibility(View.GONE);
        } else {
            if ("0".equals(mCreativeIdeaDetailVO.getChecks())) {
                approvalLayout.setVisibility(View.VISIBLE);
                mReplayLayout.setVisibility(View.GONE);
                mApprovalBt.setText("审核");
                mUnapprovalBt.setText("驳回");

            } else {
                if ("1".equals(mCreativeIdeaDetailVO.getIsadopt())) {
                    if ("0".equals(mCreativeIdeaDetailVO.getAccept()) && "1".equals(mCreativeIdeaDetailVO.getChecks())) {
                        approvalLayout.setVisibility(View.VISIBLE);
                        mApprovalBt.setText("采 纳");
                        mUnapprovalBt.setText("不采纳");

                        // 采纳时出现抄送人
                        mInformGv.setVisibility(View.GONE);// 1-2-1版本隐藏
                        mInformTv.setVisibility(View.GONE);// 1-2-1版本隐藏
                        mInformTv.setText("抄送人");
                    } else {
                        mReplayLayout.setVisibility(View.VISIBLE);
                        approvalLayout.setVisibility(View.GONE);
                    }
                } else {
                    mReplayLayout.setVisibility(View.VISIBLE);
                    approvalLayout.setVisibility(View.GONE);
                }
            }
        }

        if ("1".equals(mCreativeIdeaDetailVO.getAccept()) && "1".equals(mCreativeIdeaDetailVO.getChecks()) && mCreativeIdeaDetailVO.getInsUser() != null) {
            mInformAdapter.setmAdd(false);
            if (mCreativeIdeaDetailVO.getInsUser() != null) {
                mInformTv.setVisibility(View.GONE);// 1-2-1版本隐藏
                mInformLayout.setVisibility(View.GONE);// 1-2-1版本隐藏
                mInformGv.setVisibility(View.GONE);// 1-2-1版本隐藏
                mInformAdapter.updateData(mCreativeIdeaDetailVO.getInsUser());
            } else {
                mInformTv.setVisibility(View.GONE);
                mInformLayout.setVisibility(View.GONE);
            }

        }

    }

    // 加载更多数据
    public void initFoot() {
        mFootLayout = LayoutInflater.from(this).inflate(R.layout.listview_bottom_more, null);
        mMoreTextView = (TextView) mFootLayout.findViewById(R.id.txt_loading);
        mMoreTextView.setText("更多");
        mProgressBar = (ProgressBar) mFootLayout.findViewById(R.id.progressBar);
        mFootLayout.setVisibility(View.GONE);
        mDiscussLv.addFooterView(mFootLayout);
    }

    protected void footViewIsVisibility(List<DiscussVO> datas) {
        if (mDiscussResultVO == null) {
            return;
        }
        if (mDiscussResultVO.getCount() == null) {
            return;
        }

        if (Integer.parseInt(mDiscussResultVO.getCount()) <= 15) {
            mFootLayout.setVisibility(View.GONE);
            mDiscussLv.removeFooterView(mFootLayout);
        } else {
            mFootLayout.setVisibility(View.VISIBLE);
            mMoreTextView.setText("加载更多");
            mProgressBar.setVisibility(View.GONE);
        }
    }

    public void bindRefreshListener() {
        mFootLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mMoreTextView.setText("正在加载...");
                mProgressBar.setVisibility(View.VISIBLE);
                mState = 2;
                new ThreadUtil(CreativeIdeaDetailActivity.this, CreativeIdeaDetailActivity.this).start();
            }
        });
    }

    /**
     * 获取传递 的 参数
     */
    private void getIntentData() {
        mType = this.getIntent().getStringExtra("type");
        mId = this.getIntent().getStringExtra("id");
        mIsBoss = this.getIntent().getStringExtra("isboss");
        mMessageid = this.getIntent().getStringExtra("messageid");
    }

    public void uploadCheck(final String check) {
        CustomDialog.showProgressDialog(this, "正在提交数据...");
        RequestParams params = new RequestParams();
        try {
            params.put("ftoken", BSApplication.getInstance().getmCompany());
            params.put("userid", BSApplication.getInstance().getUserId());
            params.put("check", check);
            params.put("id", mId);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        String url = BSApplication.getInstance().getHttpTitle() + Constant.CREATIVE_CHECK_URL;

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                mCommitFlag = true;
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                mCommitFlag = true;

                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(new String(arg2));
                    String str = (String) jsonObject.get("retinfo");
                    String code = (String) jsonObject.get("code");
                    if ("200".equals(code)) {
                        CustomToast.showShortToast(CreativeIdeaDetailActivity.this, str);
                        mCreativeIdeaDetailVO.setChecks(check);
                        if ("2".equals(check)) {
                            mReplayLayout.setVisibility(View.VISIBLE);
                            approvalLayout.setVisibility(View.GONE);
                        }
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("id", mId);
                        map.put("check", check);
                        CommonUtils.sendBroadcast(CreativeIdeaDetailActivity.this, Constant.HOME_MSG, map);
                        updateUi();

                    }
                    CustomDialog.closeProgressDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDiscussAdapter.stopSound();
    }

    public void showDialog(final String accept) {

        View v = LayoutInflater.from(this).inflate(R.layout.dialog_edittext, null);
        final EditText textView = (EditText) v.findViewById(R.id.edit_content);
        mDialog = new BSDialog(this, "请输入内容", v, new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                uploadAdopt(textView.getText().toString(), accept);
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }

    public void uploadAdopt(String content, final String accept) {
        RequestParams params = new RequestParams();

        try {
            params.put("ftoken", BSApplication.getInstance().getmCompany());
            params.put("userid", BSApplication.getInstance().getUserId());
            params.put("accept", accept);
            params.put("users_name", mInformPerson.toString());

            params.put("id", mId);
            params.put("content", content);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        String url = BSApplication.getInstance().getHttpTitle() + Constant.CREATIVE_ADOPTION_URL;
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                mCommitFlag = true;
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                mCommitFlag = true;

                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(new String(arg2));
                    String str = (String) jsonObject.get("retinfo");
                    String code = (String) jsonObject.get("code");
                    if ("200".equals(code)) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("id", mId);
                        map.put("accept", accept);
                        CommonUtils.sendBroadcast(CreativeIdeaDetailActivity.this, Constant.HOME_MSG, map);

                        mReplayLayout.setVisibility(View.VISIBLE);
                        approvalLayout.setVisibility(View.GONE);
                        mInformGv.setVisibility(View.GONE);
                        mInformTv.setVisibility(View.GONE);
                    } else {
                        approvalLayout.setVisibility(View.VISIBLE);
                        mReplayLayout.setVisibility(View.GONE);
                    }
                    CustomToast.showShortToast(CreativeIdeaDetailActivity.this, str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }
}
