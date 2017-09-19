
package com.bs.bsims.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bs.bsims.R;
import com.bs.bsims.adapter.CrmVisitorImgSelectAdapter;
import com.bs.bsims.adapter.DiscussAdapter;
import com.bs.bsims.adapter.DiscussAdapter.DiscussCallback;
import com.bs.bsims.application.BSApplication;
import com.bs.bsims.constant.Constant;
import com.bs.bsims.fragment.CommentFragment;
import com.bs.bsims.model.CrmVisitRecordDetailVO;
import com.bs.bsims.model.DiscussResultVO;
import com.bs.bsims.model.DiscussVO;
import com.bs.bsims.utils.CommonUtils;
import com.bs.bsims.utils.CustomToast;
import com.bs.bsims.utils.DateUtils;
import com.bs.bsims.utils.HttpClientUtil;
import com.bs.bsims.utils.ThreadUtil;
import com.bs.bsims.view.BSCircleImageView;
import com.bs.bsims.view.BSListView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * 跟单详情界面
 */
public class CrmVisitRecordDetailActivity extends BaseActivity implements OnClickListener {
    private Context context;
    public static CrmVisitRecordDetailActivity aCrmVisitRecordDetailActivity;

    // 评论模块东西
    private CommentFragment fragment;
    private BSListView mDiscussLv;
    private DiscussAdapter mDiscussAdapter;
    private String mArticleId;
    private LinearLayout mBottomLayout;
    private int mState = 0;
    private String mLastid;
    private DiscussResultVO mDiscussResultVO;
    private TextView mDiscussTitle;

    private TextView mPersonTitle01, mPersonTitle02, mPersonTitle03, mPersonTitle04, mContentTv;
    private TextView mLinkBNametTv, mLinkCNametTv, mLinkAddressTv, mReplayTv, mPraiseTv,
            mDeclineTv, mVisitType, mModelType;
    private BSCircleImageView mHeadIcon;
    private ImageView mImgAgree, mImgOppose;
    private LinearLayout mAgreeLayout, mOpposeLayout;
    private CrmVisitRecordDetailVO mDetailVO;
    private boolean mFlag = true;

    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;
    private String mVid;
    private String mBid;
    private String mHid;
    private String mCid;
    private String mStatus = "";
    private GridView send_second_person_gv;
    private CrmVisitorImgSelectAdapter crmVimgsapdater;
    private TextView vistor_name, cname;

    // listView
    private View mFootLayout;
    private TextView mMoreTextView;
    private ProgressBar mProgressBar;
    private boolean mRefresh = false;
    private String mCount;

    private ScrollView so;
    DiscussCallback mDissCallback = new DiscussCallback() {

        @Override
        public void callback() {
            mState = 0;
            mRefresh = true;
            new ThreadUtil(CrmVisitRecordDetailActivity.this, CrmVisitRecordDetailActivity.this)
                    .start();
        }
    };

    @Override
    public void baseSetContentView() {
        View.inflate(this, R.layout.crm_visit_record_detail, mContentLayout);
        context = this;
        aCrmVisitRecordDetailActivity=this;
    }

    @Override
    public boolean getDataResult() {
        return getData();
    }

    @Override
    public void updateUi() {

        if (mFlag) {
            mFlag = false;
            CrmVisitRecordDetailVO vo = mDetailVO.getArray();
            mImageLoader = ImageLoader.getInstance();
            mOptions = CommonUtils.initImageLoaderOptions();
            mImageLoader.displayImage(vo.getHeadpic(), mHeadIcon, mOptions);
            mHeadIcon.setUserId(vo.getUserid());//HL:获取跟单详情界面头像对应的用户ID，以便响应跳转
            mHeadIcon.setUserName(vo.getFullName());
            mHeadIcon.setUrl(vo.getHeadpic());
            mPersonTitle01.setText(vo.getFullName());
            mPersonTitle02.setText(vo.getDepartmentName() + " / " + vo.getPositionsName());
            mPersonTitle03.setText(DateUtils.parseDateDayAndHour(vo.getTime()));
            mContentTv.setText(vo.getInfo());
            mVisitType.setText("目的:" + vo.getObjectiveName());
            cname.setText(vo.getCname());
            vistor_name.setText(vo.getName());
            mBid = vo.getBid();
            mHid = vo.getHid();
            mCid = vo.getCid();
            /****
             * 放图片
             */

            crmVimgsapdater.updateList(vo.getImgs());
            String bid_show;
            if (null != getIntent().getStringExtra("bid_ishow")) {
                bid_show = getIntent().getStringExtra("bid_ishow");
            }
            else {
                bid_show = "";
            }
            if (!"0".equals(mBid) && !bid_show.equals("0")) {
                mLinkBNametTv.setText("关联商机：" + vo.getBname());
                mLinkBNametTv.setVisibility(View.VISIBLE);
            } else {
                mLinkBNametTv.setVisibility(View.GONE);
            }

            if (!"0".equals(mHid)) {
                mLinkCNametTv.setText("关联合同：" + vo.getHname());
                mLinkCNametTv.setVisibility(View.VISIBLE);
                mStatus = vo.getHinfo().getStatus();
            } else {
                mLinkCNametTv.setVisibility(View.GONE);
            }
            if (!vo.getAddress().equals(getResources().getString(R.string.error_php_interface))) {
                mLinkAddressTv.setText(vo.getAddress());
                mLinkAddressTv.setVisibility(View.VISIBLE);
            } else {
                mLinkAddressTv.setVisibility(View.GONE);
            }

            mModelType.setText(vo.getModeName());
            if ("1".equals(vo.getMode())) {
                mModelType.setCompoundDrawablesWithIntrinsicBounds(
                        getResources().getDrawable(R.drawable.crm_house),
                        null, null, null);
            } else if ("2".equals(vo.getMode())) {
                mModelType.setCompoundDrawablesWithIntrinsicBounds(
                        getResources().getDrawable(R.drawable.crm_phone),
                        null, null, null);
            } else if ("3".equals(vo.getMode())) {
                mModelType.setCompoundDrawablesWithIntrinsicBounds(
                        getResources().getDrawable(R.drawable.crm_message),
                        null, null, null);
            } else {
                mModelType.setCompoundDrawablesWithIntrinsicBounds(
                        getResources().getDrawable(R.drawable.crm_other),
                        null, null, null);
            }
        }

        if (Constant.RESULT_CODE.equals(mDiscussResultVO.getCode())) {
            if (2 == mState) {
                mDiscussAdapter.mList.addAll(mDiscussResultVO.getArray());
            } else {
                mDiscussAdapter.mList = mDiscussResultVO.getArray();
                mCount = mDiscussResultVO.getCount();
            }
            mState = 0;
        }

        if (mDiscussResultVO.getCount() != null) {
            String discussStr = String.format(getResources().getString(R.string.discuss),
                    Integer.parseInt(mDiscussResultVO.getCount()));
            mDiscussTitle.setText(discussStr);
            mDiscussTitle.setVisibility(View.VISIBLE);
            mDiscussAdapter.notifyDataSetChanged();
            footViewIsVisibility(mDiscussAdapter.mList);
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void initView() {
        mTitleTv.setText("跟单详情");
        initData();
        mPersonTitle01 = (TextView) findViewById(R.id.person_title01);
        mPersonTitle02 = (TextView) findViewById(R.id.person_title02);
        mPersonTitle03 = (TextView) findViewById(R.id.person_title03);
        mPersonTitle04 = (TextView) findViewById(R.id.person_title04);
        mHeadIcon = (BSCircleImageView) findViewById(R.id.head_icon);
        send_second_person_gv = (GridView) findViewById(R.id.send_second_person_gv);
        crmVimgsapdater = new CrmVisitorImgSelectAdapter(context);
        send_second_person_gv.setAdapter(crmVimgsapdater);
        mContentTv = (TextView) findViewById(R.id.content_tv);
        mLinkBNametTv = (TextView) findViewById(R.id.link_bname_tv);
        mLinkCNametTv = (TextView) findViewById(R.id.link_cname_tv);
        mLinkAddressTv = (TextView) findViewById(R.id.link_address_tv);
        mReplayTv = (TextView) findViewById(R.id.replay_tv);
        mPraiseTv = (TextView) findViewById(R.id.praise_tv);
        mDeclineTv = (TextView) findViewById(R.id.decline_tv);
        mVisitType = (TextView) findViewById(R.id.objective_type);
        mModelType = (TextView) findViewById(R.id.mode_type);

        mImgAgree = (ImageView) findViewById(R.id.img_agree);
        mImgOppose = (ImageView) findViewById(R.id.img_oppose);
        mAgreeLayout = (LinearLayout) findViewById(R.id.aggree_layout);
        mOpposeLayout = (LinearLayout) findViewById(R.id.oppose_layout);

        vistor_name = (TextView) findViewById(R.id.vistor_name);
        cname = (TextView) findViewById(R.id.cname);

        mDiscussLv = (BSListView) findViewById(R.id.list_view);
        mDiscussTitle = (TextView) findViewById(R.id.discuss_tv);
        mDiscussAdapter = new DiscussAdapter(this, mVid, fragment, mDiscussTitle);
        mDiscussAdapter.setCallback(mDissCallback);
        mDiscussLv.setAdapter(mDiscussAdapter);

        mDiscussAdapter.setAgreeUrl(BSApplication.getInstance().getHttpTitle()
                + Constant.CRM_CLIENT_DISSCUSS_AGREE);
        mDiscussAdapter.setOpposeUrl(BSApplication.getInstance().getHttpTitle()
                + Constant.CRM_CLIENT_DISSCUSS_OPPOSE);
        mDiscussAdapter.setContentUrl(BSApplication.getInstance().getHttpTitle()
                + Constant.CRM_VISIT_RECORD_DISSCUSS);
        mDiscussAdapter.setType(2);
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

        initFoot();
    }

    public void initData() {
        mVid = this.getIntent().getStringExtra("vid");
    }

    @Override
    public void bindViewsListener() {
        mHeadBackImag.setOnClickListener(this);
        mLinkBNametTv.setOnClickListener(this);
        mLinkCNametTv.setOnClickListener(this);
        mLinkAddressTv.setOnClickListener(this);
        send_second_person_gv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                // intent.putStringArrayListExtra("piclist", (ArrayList<String>) strlist);
                intent.putStringArrayListExtra("piclist", (ArrayList<String>) mDetailVO.getArray()
                        .getImgs());
                intent.setClass(context, ImagePreviewActivity.class);
                intent.putExtra("imgIndex", arg2);
                context.startActivity(intent);
            }
        });

        mFootLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mMoreTextView.setText("正在加载...");
                mProgressBar.setVisibility(View.VISIBLE);
                mState = 2;
                new ThreadUtil(CrmVisitRecordDetailActivity.this, CrmVisitRecordDetailActivity.this)
                        .start();
            }
        });
    }

    public boolean getData() {

        if (0 == mState) {
            mLastid = "";
        } else if (2 == mState) {
            mLastid = mDiscussAdapter.mList.get(mDiscussAdapter.mList.size() - 1).getCommentid();
        }

        try {
            Gson gson = new Gson();

            if (mFlag) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("userid", BSApplication.getInstance().getUserId());
                map.put("vid", mVid);
                map.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
                String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance()
                        .getHttpTitle() + Constant.CRM_VISIT_RECORD_DETAIL, map);
                mDetailVO = gson.fromJson(jsonStr, CrmVisitRecordDetailVO.class);
            }

            HashMap<String, String> mapDisscuss = new HashMap<String, String>();
            mapDisscuss.put("userid", BSApplication.getInstance().getUserId());
            mapDisscuss.put(Constant.FTOKEN_PARAMS, BSApplication.getInstance().getmCompany());
            mapDisscuss.put("id", mVid);
            mapDisscuss.put(Constant.LASTID, mLastid);
            String jsonStr = HttpClientUtil.getRequest(BSApplication.getInstance().getHttpTitle()
                    + Constant.CRM_VISIT_RECORD_DISSCUSS_LIST, mapDisscuss);
            mDiscussResultVO = gson.fromJson(jsonStr, DiscussResultVO.class);

            if (Constant.RESULT_CODE.equals(mDiscussResultVO.getCode())) {
            }

            if (Constant.RESULT_CODE.equals(mDetailVO.getCode())) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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
        } else {
            mFootLayout.setVisibility(View.VISIBLE);
            mMoreTextView.setText("加载更多");
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mRefresh) {
                Intent intent = new Intent();
                intent.putExtra("count", mCount);
                intent.putExtra("vid", mVid);
                this.setResult(1, intent);
            }
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View arg0) {
        Intent intent = new Intent();
        switch (arg0.getId()) {
            case R.id.img_head_back:
                if (mRefresh) {
                    intent.putExtra("count", mCount);
                    intent.putExtra("vid", mVid);
                    this.setResult(1, intent);
                }
                finish();
                break;
            case R.id.link_bname_tv:
                intent.putExtra("bid", mBid);
                intent.putExtra("stateUtilthread", "3");
                intent.setClass(context, CrmBusinessHomeIndexOneInfo.class);
                startActivity(intent);
                break;
            case R.id.link_cname_tv:
                intent.putExtra("hid", mHid);
                if (mStatus.equals("0") || mStatus.equals("5")) {
                    intent.setClass(context, CrmTradeContantDetailsIndexActivity.class);
                }
                else {
                    intent.setClass(context, CrmTradeContantDeatilsHomeTop3Activity.class);
                }
                startActivity(intent);
                break;

            case R.id.link_address_tv:
                try {
                    intent.putExtra("mLat", mDetailVO.getArray().getLat());
                    intent.putExtra("mLon", mDetailVO.getArray().getLng());
                    intent.putExtra("mAddress", mDetailVO.getArray().getAddress());
                    intent.putExtra("cid", mCid);
                    intent.putExtra("cName", mDetailVO.getArray().getCname());
                    intent.setClass(context, CrmGaoDeMapWithShowActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                    CustomToast.showShortToast(context, "数据错误,无法跳转");
                }
                break;
            default:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        aCrmVisitRecordDetailActivity =null;
//        if(mDiscussAdapter!=null){
//            mDiscussAdapter.
//        }
    }
}
