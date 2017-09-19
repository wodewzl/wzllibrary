
package com.bs.bsims.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.AnnexAdapter;
import com.bs.bsims.adapter.DiscussAdapter;
import com.bs.bsims.adapter.DiscussAdapter.DiscussCallback;
import com.bs.bsims.adapter.HeadAdapter;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.fragment.CommentFragment;
import com.bs.bsims.model.DiscussResultVO;
import com.bs.bsims.model.DiscussVO;
import com.bs.bsims.model.EmployeeVO;
import com.bs.bsims.model.PublishResultVO;
import com.bs.bsims.model.PublishVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomDialog;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.utils.FileIconMapping;
import com.bs.bsims.utils.FileSizeUtil;
import com.bs.bsims.utils.FileUtil;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.utils.UrlUtil;
import com.bs.bsims.utils.WebViewPictureUtil;
import com.bs.bsims.view.BSCirleDwLoadProgrossView;
import com.bs.bsims.view.BSListView;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NoticeDetailActivity extends BaseActivity implements OnClickListener, DiscussCallback {
    private TableLayout mTableLayout;
    private HeadAdapter mHeadAdapter, mNoReadHeadAdapter;// 指向已读未读头像
    private GridView mBsGridView;
    private BSListView mDiscussLv;
    private DiscussAdapter mDiscussAdapter;
    private String mArticleId;
    private PublishVO mPublishVO;
    private PublishResultVO mPublishResultVO;
    private TextView mTitile, mTime, mPublishName, mNumber, mDepartment, mDiscussTitle;
    private WebView mContent;
    private DiscussResultVO mDiscussResultVO;
    private Context context;
    private CommentFragment fragment;

    // listView
    private View mFootLayout;
    private TextView mMoreTextView;
    private ProgressBar mProgressBar;
    // 0为首次,1为上拉刷新 ，2为下拉刷新
    private int mState = 0;
    private boolean mFlag = true;

    private TextView mNumberTitle;

    private List<EmployeeVO> mVOList;
    private LinearLayout mBottomLayout;

    private ImageView mAnnexType, mAnnexDownLoad, mAnnexView;
    private TextView mAnnexName, mAnnexDes;
    private BSCirleDwLoadProgrossView mAnnexProgress;

    private BSListView mAnnexListView;
    private AnnexAdapter mAnnexAdapter;
    private TextView mAnnexTitle;
    private LinearLayout gride_layout, reminder_layout;
    private TextView mread, mNoRead;
    private GridView gv_no_read_head;// 未读头标
    private ImageView mLineImg;
    // one 、two用于动画偏移位置
    private int two;
    // 作为一种判断依据，1表示左边，2表示中间，3表示右边，
    private int index = 1;
    private LinearLayout left_layout, center_layout, right_layout;
    private List<EmployeeVO> readHeadList;
    private List<EmployeeVO> noReadHeadList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // baseSetContentView();
        // initView();
        // initData();
        context = this;
    }

    @Override
    public void initView() {
        String sortid = this.getIntent().getStringExtra("sortid");
        if ("3".equals(sortid)) {
            mTitleTv.setText("通知详情");
        } else if ("11".equals(sortid)) {
            mTitleTv.setText("公文详情");
        } else if ("19".equals(sortid)) {
            mTitleTv.setText("企业风采详情");
        } else if ("12".equals(sortid)) {
            mTitleTv.setText("制度详情");
        }
        mArticleId = getIntent().getStringExtra("articleid");
        mTitile = (TextView) findViewById(R.id.notice_title);
        mBsGridView = (GridView) findViewById(R.id.gv_head);// 已读头标
        mDiscussLv = (BSListView) findViewById(R.id.list_view);
        initFoot();
        mTime = (TextView) findViewById(R.id.notice_time);
        mPublishName = (TextView) findViewById(R.id.publisher_name);
        mNumber = (TextView) findViewById(R.id.number_text_tv);
        mNumberTitle = (TextView) findViewById(R.id.number_tv);

        mDepartment = (TextView) findViewById(R.id.department);
        mContent = (WebView) findViewById(R.id.content);
        mDiscussTitle = (TextView) findViewById(R.id.discuss_tv);
        gride_layout = (LinearLayout) findViewById(R.id.gride_layout);
        mread = (TextView) findViewById(R.id.read_text);
        mNoRead = (TextView) findViewById(R.id.no_read_text);
        gv_no_read_head = (GridView) findViewById(R.id.gv_no_read_head);
        reminder_layout = (LinearLayout) findViewById(R.id.reminder_layout);
        mLineImg = (ImageView) findViewById(R.id.line_img);

        left_layout = (LinearLayout) findViewById(R.id.left_layout);
        center_layout = (LinearLayout) findViewById(R.id.center_layout);
        right_layout = (LinearLayout) findViewById(R.id.right_layout);

        if ("19".equals(sortid)) {
            mNumber.setVisibility(View.GONE);
            mNumberTitle.setVisibility(View.GONE);
        }
        if ("12".equals(sortid)) {
            mDiscussTitle.setVisibility(View.GONE);
        } else {
            mDiscussTitle.setVisibility(View.VISIBLE);
        }
        String discussStr = String.format(getResources().getString(R.string.discuss), 0);
        mDiscussTitle.setText(discussStr);

        mHeadAdapter = new HeadAdapter(this, false);
        mBsGridView.setAdapter(mHeadAdapter);
        mNoReadHeadAdapter = new HeadAdapter(this, false);
        gv_no_read_head.setAdapter(mNoReadHeadAdapter);

        mDiscussAdapter = new DiscussAdapter(this, mArticleId, fragment, mDiscussTitle);
        mDiscussLv.setAdapter(mDiscussAdapter);
        mDiscussAdapter.setAgreeUrl(BSApplication.getInstance().getHttpTitle() + Constant.NOTICE_DISCUSS_OPP_ADD_AGE_URL);
        mDiscussAdapter.setOpposeUrl(BSApplication.getInstance().getHttpTitle() + Constant.NOTICE_DISCUSS_OPP_ADD_AGE_URL);
        mDiscussAdapter.setContentUrl(BSApplication.getInstance().getHttpTitle() + Constant.NOTICE_COMMIT_DISCUSS_URL);
        mDiscussAdapter.setType(2);

        // mGoTv = (TextView) findViewById(R.id.go_bt);展现更多图片，去掉

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

        mBottomLayout = (LinearLayout) findViewById(R.id.bottom_layout);

        mAnnexType = (ImageView) findViewById(R.id.annex_type);
        mAnnexName = (TextView) findViewById(R.id.annex_name);
        mAnnexDes = (TextView) findViewById(R.id.annex_des);
        mAnnexDownLoad = (ImageView) findViewById(R.id.annex_downlaod);
        mAnnexView = (ImageView) findViewById(R.id.annex_view);
        mAnnexProgress = (BSCirleDwLoadProgrossView) findViewById(R.id.annex_progress);

        mAnnexListView = (BSListView) findViewById(R.id.annex_listview);
        mAnnexAdapter = new AnnexAdapter(this);
        mAnnexListView.setAdapter(mAnnexAdapter);
        mAnnexTitle = (TextView) findViewById(R.id.annex_title);

        initData();
    }

    public boolean getData() {
        // match(0, "");
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
                String url = UrlUtil.getPublishDetailUrl(Constant.PUBLISH_DETAIL, mArticleId);
                String jsonUrlStr = HttpClientUtil.get(url, Constant.ENCODING).trim();
                mPublishResultVO = gson.fromJson(jsonUrlStr, PublishResultVO.class);

                if (mPublishResultVO.getTousers() != null) {
                    mVOList = mPublishResultVO.getTousers();
                }

            }
            String discussUrl = UrlUtil.getNoticeDiscuss(Constant.NOTICE_DISCUSS_URL, mArticleId, refresh, id);
            String discussJson = HttpClientUtil.get(discussUrl, Constant.ENCODING).trim();
            mDiscussResultVO = gson.fromJson(discussJson, DiscussResultVO.class);
            if (Constant.RESULT_CODE.equals(mDiscussResultVO.getCode())) {

            }

            if (Constant.RESULT_CODE.equals(mPublishResultVO.getCode())) {
                CommonUtils.sendBroadcast(this, Constant.HOME_MSG);
                mPublishVO = mPublishResultVO.getArray().get(0);
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void bindViewsListener() {
        mHeadBackImag.setOnClickListener(this);
        bindRefreshListener();
        String sortid = this.getIntent().getStringExtra("sortid");
        if (!"12".equals(sortid)) {
            left_layout.setOnClickListener(this);
        }
        center_layout.setOnClickListener(this);
        right_layout.setOnClickListener(this);
        reminder_layout.setOnClickListener(this);
        mDiscussAdapter.setCallback(this);
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void updateUi() {

        if (mFlag) {
            mTitile.setText(mPublishVO.getTitle());
            long time = Long.valueOf(mPublishVO.getTime()) * 1000;
            mTime.setText(DateUtils.parseDate(time));
            mPublishName.setText(mPublishVO.getFullname());
            mNumber.setText(mPublishVO.getSerial_number());

            String hang = "<br />";
            String front = "<font color='#666666' size='2'> ";
            String back = "</font>";
            String divstart = "<div align = 'left'>";
            String divend = "</div>";
            String content = front + divstart + CommonUtils.replaceWebview2(CommonUtils.replaceWebview1(mPublishVO.getContent())) + hang + divend + back;

            mContent.loadDataWithBaseURL(FileSizeUtil.SDPATH1, content, "text/html", "utf-8", null);
            mContent.setScrollContainer(false);
            mContent.setScrollbarFadingEnabled(false);
            mContent.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
            mContent.getSettings().setJavaScriptEnabled(true);
            mContent.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
            // mContent.setWebViewClient(new MyWebViewClient());
            // mContent.addJavascriptInterface(new JsObject(this), "viewlistner");
            mContent.setWebViewClient(new WebViewPictureUtil(this, mContent, "img", "this.src"));
            String sortid = this.getIntent().getStringExtra("sortid");

            // 所以通知人头像集合
            List<EmployeeVO> headList = mPublishResultVO.getTousers();
            readHeadList = getNeededList("1", headList);
            noReadHeadList = getNeededList("0", headList);
            mHeadAdapter.updateData(readHeadList);// 显示已读人头像列表
            mNoReadHeadAdapter.updateData(noReadHeadList);// 显示未读人头像列表
            mread.setText("已看 " + String.valueOf(readHeadList.size()));
            mNoRead.setText("未看 " + String.valueOf(noReadHeadList.size()));
            if ("12".equals(sortid)) {
                startToRead(sortid);
            }

            if (mPublishVO.getAnnexs() != null && mPublishVO.getAnnexs().size() > 0) {
                mAnnexAdapter.updateData(mPublishVO.getAnnexs());
                mAnnexTitle.setVisibility(View.VISIBLE);
            } else {
                mAnnexTitle.setVisibility(View.GONE);
            }
            mFlag = false;
        }
        if (Constant.RESULT_CODE.equals(mDiscussResultVO.getCode())) {
            if (2 == mState) {
                mDiscussAdapter.mList.addAll(mDiscussResultVO.getArray());
            } else {
                mDiscussAdapter.mList = mDiscussResultVO.getArray();
            }
            mState = 0;
        }

        if ("1".equals(mPublishVO.getIscomment())) {
            if (mDiscussResultVO.getCount() != null) {
                String discussStr = String.format(getResources().getString(R.string.discuss), Integer.parseInt(mDiscussResultVO.getCount()));
                mDiscussTitle.setText(discussStr);
                mDiscussTitle.setVisibility(View.VISIBLE);
                mDiscussAdapter.notifyDataSetChanged();
                footViewIsVisibility(mDiscussAdapter.mList);
            }

        } else {
            mBottomLayout.setVisibility(View.GONE);
            mDiscussLv.setVisibility(View.GONE);
            // mDiscussTitle.setVisibility(View.GONE);
        }
    }

    public void startToRead(String str) {
        if (index != 2) {
            startAnim(2);// 下划线动画
            index = 2;
        }
        clearTextColor();
        mread.setTextColor(getResources().getColor(R.color.C5));
        mDiscussLv.setVisibility(View.GONE);
        gride_layout.setVisibility(View.VISIBLE);
        mBsGridView.setVisibility(View.VISIBLE);
        gv_no_read_head.setVisibility(View.GONE);
        reminder_layout.setVisibility(View.GONE);
        if (readHeadList.size() == 0) {
            gride_layout.setVisibility(View.GONE);
        }
    }

    // 遍历列表，得到想要的 数据（“1”已读或“0”未读）
    public List<EmployeeVO> getNeededList(String str, List<EmployeeVO> voList) {
        List<EmployeeVO> mList = new ArrayList<EmployeeVO>();
        for (int i = 0; i < voList.size(); i++) {
            EmployeeVO vo = voList.get(i);
            if (str.equals(vo.getIsread())) {
                mList.add(vo);
            }
        }
        return mList;

    }

    @Override
    public void baseSetContentView() {
        View layout = View.inflate(this, R.layout.notice_detail, null);
        mContentLayout.addView(layout);
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
                new ThreadUtil(NoticeDetailActivity.this, NoticeDetailActivity.this).start();
            }
        });

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.img_head_back:
                this.setResult(2, intent);
                finish();
                break;
            case R.id.go_bt:

                intent.putExtra("head_list", (Serializable) mVOList);
                intent.putExtra("title", "通知人");
                intent.setClass(this, HeadActivity.class);
                startActivity(intent);
                break;
            case R.id.left_layout:
                if (index != 1) {
                    startAnim(1);// 下划线动画
                    index = 1;
                }
                clearTextColor();// 还原文字颜色
                gride_layout.setVisibility(View.GONE);
                mDiscussLv.setVisibility(View.VISIBLE);
                mDiscussTitle.setTextColor(getResources().getColor(R.color.C5));
                break;
            case R.id.center_layout:
                if (index != 2) {
                    startAnim(2);// 下划线动画
                    index = 2;
                }
                clearTextColor();
                mread.setTextColor(getResources().getColor(R.color.C5));
                mDiscussLv.setVisibility(View.GONE);
                gride_layout.setVisibility(View.VISIBLE);
                mBsGridView.setVisibility(View.VISIBLE);
                gv_no_read_head.setVisibility(View.GONE);
                reminder_layout.setVisibility(View.GONE);
                if (readHeadList.size() == 0) {
                    gride_layout.setVisibility(View.GONE);
                }
                break;
            case R.id.right_layout:
                if (index != 3) {
                    startAnim(3);// 下划线动画
                    index = 3;
                }
                clearTextColor();
                mNoRead.setTextColor(getResources().getColor(R.color.C5));
                mDiscussLv.setVisibility(View.GONE);
                gride_layout.setVisibility(View.VISIBLE);
                if (mPublishResultVO.getIsAgain().equals("0")) {
                    reminder_layout.setVisibility(View.GONE);
                } else if (mPublishResultVO.getIsAgain().equals("1")) {
                    reminder_layout.setVisibility(View.VISIBLE);
                }
                gv_no_read_head.setVisibility(View.VISIBLE);
                mBsGridView.setVisibility(View.GONE);
                if (noReadHeadList.size() == 0) {
                    gride_layout.setVisibility(View.GONE);
                }
                break;
            case R.id.reminder_layout:
                setSecondReminder();
                break;
            default:
                break;
        }
    }

    // 再次提醒未看成员
    public void setSecondReminder() {
        CustomDialog.showProgressDialog(this, "正在提交数据...");
        RequestParams params = new RequestParams();

        try {
            params.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            params.put("userid", BSApplication.getInstance().getUserId());
            params.put("articleid", mArticleId);

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        String url = BSApplication.getInstance().getHttpTitle() + Constant.NOTICE_SECOND_REMINDER;
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(url, params, new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                System.out.println(new String(arg2));

                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(new String(arg2));
                    String str = (String) jsonObject.get("retinfo");
                    String code = (String) jsonObject.get("code");
                    CustomToast.showShortToast(NoticeDetailActivity.this, str);

                    CustomDialog.closeProgressDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void startAnim(int dex) {
        Animation animation = null;
        switch (dex) {
            case 1:
                if (index == 2) {
                    animation = new TranslateAnimation(two * 2, 0, 0, 0);
                } else if (index == 3) {
                    animation = new TranslateAnimation(two * 3, 0, 0, 0);
                }
                break;
            case 2:
                if (index == 1) {
                    animation = new TranslateAnimation(0, two * 2, 0, 0);
                } else if (index == 3) {
                    animation = new TranslateAnimation(two * 3, two * 2, 0, 0);
                }
                break;
            case 3:
                if (index == 1) {
                    animation = new TranslateAnimation(0, two * 3, 0, 0);
                } else if (index == 2) {
                    animation = new TranslateAnimation(two * 2, two * 3, 0, 0);
                }
                break;

            default:
                break;
        }

        animation.setFillAfter(true);
        animation.setDuration(150);
        mLineImg.startAnimation(animation);
    }

    public void initData() {
        Display currDisplay = getWindowManager().getDefaultDisplay();// 获取屏幕当前分辨率
        int displayWidth = currDisplay.getWidth();
        int displayHeight = currDisplay.getHeight();
        two = displayWidth / 4; // 设置水平动画平移大小

        LayoutParams para = mLineImg.getLayoutParams();
        para.width = displayWidth / 4;
        mLineImg.setLayoutParams(para);

    }

    public void clearTextColor() {
        mDiscussTitle.setTextColor(Color.parseColor("#96A6A7"));
        mread.setTextColor(Color.parseColor("#96A6A7"));
        mNoRead.setTextColor(Color.parseColor("#96A6A7"));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            this.setResult(2, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDiscussAdapter.stopSound();
        if (!mContent.getSettings().getLoadsImagesAutomatically()) {
            mContent.getSettings().setLoadsImagesAutomatically(true);
        }
    }

    public void a(File file) {
        Integer icon = FileIconMapping.getIcon(FileUtil.getMIMEType(file));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void callback() {
        // TODO Auto-generated method stub
        if (index != 1) {
            startAnim(1);// 下划线动画
            index = 1;
        }
        clearTextColor();// 还原文字颜色
        gride_layout.setVisibility(View.GONE);
        mDiscussLv.setVisibility(View.VISIBLE);
        mDiscussTitle.setTextColor(getResources().getColor(R.color.C5));
    }

}
