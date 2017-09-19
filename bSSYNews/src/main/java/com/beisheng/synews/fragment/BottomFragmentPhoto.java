
package com.beisheng.synews.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beisheng.base.activity.BaseActivity;
import com.beisheng.base.fragment.BaseFragment;
import com.beisheng.base.utils.BaseCommonUtils;
import com.beisheng.base.utils.JsonUtil;
import com.beisheng.base.utils.ThreadUtil;
import com.beisheng.base.view.BSBadgeView;
import com.beisheng.synews.activity.DisscussActivity;
import com.beisheng.synews.activity.LoginActivity;
import com.beisheng.synews.application.AppApplication;
import com.beisheng.synews.constant.Constant;
import com.beisheng.synews.mode.CacheListVO;
import com.beisheng.synews.mode.LiveVO;
import com.beisheng.synews.mode.NewsVO;
import com.beisheng.synews.utils.PointsAddUtil;
import com.beisheng.synews.utils.ShareUtil;
import com.beisheng.synews.view.BSPopwindowEditText;
import com.beisheng.synews.view.BSPopwindowEditText.CommitCallback;
import com.im.zhsy.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

@SuppressLint("NewApi")
public class BottomFragmentPhoto extends BaseFragment implements OnClickListener {
    private String TAG = "BottomFragmentPhoto";
    private BaseActivity mActivity;
    private TextView mCommentTv;
    private boolean mCommitFlag = true;
    private String mContent;
    private String pid = "0";// 默认0一级评论
    private ImageView mImg01, mImg02, mImg03, mImg04, mImg05;
    private TextView mTextView02;
    private BSPopwindowEditText mPopEditText;
    public NewsVO newsVo;
    public LiveVO liveVo;// 直播对象
    private String mType;
    private String mContentId;
    private String mTitle;
    private String mFavorStr = "";

    // private ImageView
    LinearLayout mLyaout01;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = (BaseActivity) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view;
        if (newsVo != null) {
            // 用于新闻图片
            view = inflater.inflate(R.layout.bottom_fragment_photo, container, false);
        } else {
            // 用于直播评论
            view = inflater.inflate(R.layout.bottom_fragment_live, container, false);
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        bindViewsListener();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void initViews(View view) {
        mImg01 = (ImageView) view.findViewById(R.id.img_01);
        if (newsVo != null) {
            mTextView02 = (TextView) view.findViewById(R.id.img_02);
            mTextView02.setBackground(BaseCommonUtils.setBackgroundShap(mActivity, 20, R.color.devider_bg, R.color.C1));
        } else {
            mImg02 = (ImageView) view.findViewById(R.id.img_02);
        }

        mImg03 = (ImageView) view.findViewById(R.id.img_03);
        mImg04 = (ImageView) view.findViewById(R.id.img_04);
        mImg05 = (ImageView) view.findViewById(R.id.img_05);
        mPopEditText = new BSPopwindowEditText(mActivity, mCallback);

        // 新闻图片评论
        if (newsVo != null) {
            if (!"0".equals(newsVo.getComments())) {
                BSBadgeView commentBadge = new BSBadgeView(mActivity, mImg03);
                commentBadge.setText(newsVo.getComments());
                commentBadge.setBadgeMargin(BaseCommonUtils.dip2px(mActivity, 5), BaseCommonUtils.dip2px(mActivity, 9));
                commentBadge.setTextSize(10);
                commentBadge.show();
            }
            mType = newsVo.getSuburl();
            mContentId = newsVo.getContentid();
            mTitle = newsVo.getTitle();

        }

        // 直播评论
        if (liveVo != null) {
            if (!"0".equals(liveVo.getComments())) {
                BSBadgeView commentBadge = new BSBadgeView(mActivity, mImg03);
                commentBadge.setText(liveVo.getComments());
                commentBadge.setBadgeMargin(BaseCommonUtils.dip2px(mActivity, 23), BaseCommonUtils.dip2px(mActivity, 9));
                commentBadge.setTextSize(10);
                commentBadge.show();
            }

            mType = liveVo.getSuburl();
            mContentId = liveVo.getLid();
            mTitle = liveVo.getTitle();
        }

    }

    CommitCallback mCallback = new CommitCallback() {

        @Override
        public void commtiCallback(String content) {
            mContent = content;
            commit(Constant.DISSCUSS_URL);
        }
    };

    private void bindViewsListener() {
        mImg01.setOnClickListener(this);
        if (mImg02 != null)
            mImg02.setOnClickListener(this);
        if (mTextView02 != null)
            mTextView02.setOnClickListener(this);
        mImg03.setOnClickListener(this);
        mImg04.setOnClickListener(this);
        mImg05.setOnClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_01:
                if (AppApplication.getInstance().getUserInfoVO() == null) {
                    mActivity.openActivity(LoginActivity.class);
                    return;
                }
                if (newsVo != null && "1".equals(newsVo.getIspraise())) {
                    mActivity.showCustomToast("亲，只能点一次哦");
                    return;
                }

                if (liveVo != null && "1".equals(liveVo.getIspraise())) {
                    mActivity.showCustomToast("亲，只能点一次哦");
                    return;
                }

                commit(Constant.PRAISE_URL);
                break;
            case R.id.img_02:
                if (AppApplication.getInstance().getUserInfoVO() == null) {
                    mActivity.openActivity(LoginActivity.class);
                    return;
                }
                mPopEditText.showPopupWindow(v);
                break;
            case R.id.img_03:
                Bundle bundle = new Bundle();
                bundle.putString("id", mContentId);
                bundle.putString("type", mType);
                bundle.putString("title", mTitle);
                mActivity.openActivity(DisscussActivity.class, bundle, 0);
                break;
            case R.id.img_04:
                if (newsVo != null) {
                    ShareUtil.share(mActivity, newsVo.getShare_img(), newsVo.getShare_tit(), newsVo.getShare_des(), newsVo.getShare_url());
                    PointsAddUtil.commitdPoints("4", newsVo.getContentid(), newsVo.getTitle());
                } else {
                    ShareUtil.share(mActivity, liveVo.getShare_img(), liveVo.getShare_tit(), liveVo.getShare_des(), liveVo.getShare_url());
                    PointsAddUtil.commitdPoints("4", liveVo.getLid(), liveVo.getTitle());

                }
                break;
            case R.id.img_05:
                CacheListVO vo = new CacheListVO();
                vo.setContentid(mContentId);
                vo.setSuburl(mType);
                if (liveVo != null) {
                    vo.setTitle(liveVo.getTitle());
                    vo.setCreatime(liveVo.getAddtime());
                    vo.setLink(liveVo.getLink());
                    vo.setComments(liveVo.getComments());
                } else {
                    vo.setTitle(newsVo.getTitle());
                    vo.setCreatime(newsVo.getCreatime());
                }

                if ("".equals(mFavorStr)) {
                    mFavorStr = JsonUtil.toJson(vo);
                    mActivity.saveJsonCache("favor_" + mContentId, null, mFavorStr);
                    mActivity.showCustomToast("收藏成功");
                    mImg05.setImageResource(R.drawable.img_05_select);
                } else {
                    mFavorStr = "";
                    mActivity.deleteByKey("favor_" + mContentId);
                    mActivity.showCustomToast("取消收藏");
                    if (newsVo != null) {
                        mImg05.setImageResource(R.drawable.photo_bottom_05);
                    } else {
                        mImg05.setImageResource(R.drawable.img_05_gray);
                    }
                }

                break;

            default:
                break;
        }
    }

    public void commit(String url) {
        mCommitFlag = false;
        mActivity.showProgressDialog();
        RequestParams params = new RequestParams();
        try {
            params.put("uid", AppApplication.getInstance().getUid());
            params.put("sessionid", AppApplication.getInstance().getSessionid());
            params.put("contentid", mContentId);
            params.put("reply", mContent);
            params.put("pid", pid);
            params.put("type", mType);
            params.put("title", mTitle);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        String allUrl = Constant.DOMAIN_NAME + url;
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(allUrl, params, new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                mCommitFlag = true;
                mActivity.dismissProgressDialog();
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                String result = new String(arg2);
                mActivity.dismissProgressDialog();
                mCommitFlag = true;
                try {
                    JSONObject jsonObject = new JSONObject(new String(arg2));
                    String str = (String) jsonObject.get("retinfo");
                    String code = (String) jsonObject.get("code");
                    if (Constant.RESULT_SUCCESS_CODE.equals(code)) {
                        mActivity.showCustomToast(str);
                        new ThreadUtil(mActivity, mActivity).start();
                    } else {
                        mActivity.showCustomToast(str);
                    }
                } catch (Exception e) {
                }

            }
        });
    }

    public NewsVO getNewsVo() {
        return newsVo;
    }

    public void setNewsVo(NewsVO newsVo) {
        this.newsVo = newsVo;
    }

    public LiveVO getLiveVo() {
        return liveVo;
    }

    public void setLiveVo(LiveVO liveVo) {
        this.liveVo = liveVo;
    }

    public String getFragmentName() {
        return TAG;// 不知道该方法有没有用
    }
}
